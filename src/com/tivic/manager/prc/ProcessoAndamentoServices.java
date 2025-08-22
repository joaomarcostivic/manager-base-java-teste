package com.tivic.manager.prc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.agd.AgendaItem;
import com.tivic.manager.agd.AgendaItemServices;
import com.tivic.manager.agd.Grupo;
import com.tivic.manager.agd.GrupoDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.gpn.WorkflowAcaoServices;
import com.tivic.manager.gpn.WorkflowGatilhoServices;
import com.tivic.manager.gpn.WorkflowRegraServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.msg.Mensagem;
import com.tivic.manager.msg.MensagemServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.ComplianceManager;
import com.tivic.manager.util.DateUtil;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ProcessoAndamentoServices {
	public static final String[] tipoOrigem       = {"Digitação","Internet","Internet Revisado","Digitação WEB"};
	public static final String[] tipoVisibilidade = {"Interno","Processual","Confidencial"};

	public static final int TP_VISIBILIDADE_PRIVADO		 = 0; //Interno
	public static final int TP_VISIBILIDADE_PUBLICO 	 = 1; //Processual
	public static final int TP_VISIBILIDADE_CONFIDENCIAL = 2;
	
	public static final int ST_ANDAMENTO_INATIVO = 0;
	public static final int ST_ANDAMENTO_ATIVO = 1;
	
	public static final int TP_DESTINATARIO_RESPONSAVEL_PROCESSO = 0;
	public static final int TP_DESTINATARIO_RESPONSAVEL_GRUPO_TRABALHO = 1;
	
	public static Result save(ProcessoAndamento andamento){
		return save(andamento, null, null, null, null, null);
	}
	
	public static Result save(ProcessoAndamento andamento, ProcessoArquivo arquivo, AgendaItem audiencia, 
			ArrayList<AgendaItem> prazos){
		return save(andamento, arquivo, audiencia, prazos, null, null);
	}
	public static Result save(ProcessoAndamento andamento, ProcessoArquivo arquivo, AgendaItem audiencia, 
			ArrayList<AgendaItem> prazos, AuthData auth){
		return save(andamento, arquivo, audiencia, prazos, auth, null);
	}
	
	public static Result save(ProcessoAndamento andamento, ProcessoArquivo arquivo, AgendaItem audiencia, 
			ArrayList<AgendaItem> prazos, AuthData auth, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(andamento==null)
				return new Result(-1, "Erro ao salvar. Andamento é nulo");
			
			boolean workflow = false;
			//COMPLIANCE
			int tpAcao = ComplianceManager.TP_ACAO_ANY;

			ProcessoAndamento itemAntigo = ProcessoAndamentoDAO.get(andamento.getCdAndamento(), andamento.getCdProcesso(), connect);
						
			int retorno;
			if(andamento.getCdAndamento()==0){
				andamento.setDtLancamento(new GregorianCalendar());
				retorno = ProcessoAndamentoDAO.insert(andamento, connect);
				andamento.setCdAndamento(retorno);
				workflow=true;
				
				tpAcao = ComplianceManager.TP_ACAO_INSERT;
			}
			else {				
				retorno = ProcessoAndamentoDAO.update(andamento, connect);
				

				tpAcao = ComplianceManager.TP_ACAO_UPDATE;
			}
			
			if(retorno > 0){
				
				/*** ARQUIVO ***/
				if(arquivo!=null){
					arquivo.setCdAndamento(andamento.getCdAndamento());
					retorno = ProcessoArquivoServices.save(arquivo, true, connect).getCode();
					if(retorno<=0) {
						Conexao.rollback(connect);
						return new Result(-2, "Erro ao inserir arquivo.");
					}
				}
				
				/*** AUDIENCIA ***/
				if(retorno > 0 && audiencia!=null){
					retorno = AgendaItemServices.save(audiencia, auth, connect).getCode();
					if(retorno<=0) {
						Conexao.rollback(connect);
						return new Result(-2, "Erro ao inserir audiência.");
					}
				}
				
				/*** PRAZOS ***/
				if(retorno > 0 && prazos!=null && prazos.size()>0){
					for (int i=0; i<prazos.size(); i++) {
						retorno = AgendaItemServices.save(prazos.get(i), auth, connect).getCode();
						if(retorno <= 0) {
							return new Result(-2, "ProcessoAndamentoServices.save: Erro ao salvar prazos.");
						}
					}
				}
			}
			
			
			/**
			 * ENVIO DE NOTIFICAÇÂO POR EMAIL
			 * Se o parametro lgNotificacaoAutomaticaEmail é true e o tipo de andamento está indicando que permite envio automático (TipoAndamento.lgEmail == true); 
			 */
			boolean lgNotificacaoAutomaticaEmail = ParametroServices.getValorOfParametroAsInteger("LG_NOTIFICACAO_AUTOMATICA_EMAIL", 0, 0, connect)==1;
			TipoAndamento tipo = TipoAndamentoDAO.get(andamento.getCdTipoAndamento(), connect);
			Result rEmail = null;

			if(lgNotificacaoAutomaticaEmail && tipo.getLgEmail()==1) {
				rEmail = enviarNotificacaoAndamento(andamento, connect);
			}
			
			
			//WORKFLOW
			ResultSetMap rsmEventos = new ResultSetMap();
			if(workflow) {
				PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nm_tipo_prazo, C.nm_tipo_processo " +
					"FROM prc_andamento_prazo A " +
					"LEFT OUTER JOIN prc_tipo_prazo B ON (A.cd_tipo_prazo = B.cd_tipo_prazo) " +
					"LEFT OUTER JOIN prc_tipo_processo C ON (A.cd_tipo_processo = C.cd_tipo_processo) " +
					"WHERE A.cd_tipo_andamento = ? " +
					"   AND (A.cd_tipo_processo IS NULL OR A.cd_tipo_processo = ?) " +
					"   AND (A.cd_area_direito IS NULL OR A.cd_area_direito = ?) " +
					"   AND (A.tp_instancia IS NULL OR A.tp_posicao_cliente IN (-1, ?)) " +
					"   AND (A.tp_instancia IS NULL OR A.tp_instancia IN (-1, 0, ?)) " +
					"ORDER BY A.QT_DIAS");
				
				Processo processo = ProcessoDAO.get(andamento.getCdProcesso(), connect);
				TipoProcesso tipoProcesso = TipoProcessoDAO.get(processo.getCdTipoProcesso(), connect);
				
				pstmt.setInt(1, andamento.getCdTipoAndamento());
				pstmt.setInt(2, processo.getCdTipoProcesso());
				pstmt.setInt(3, tipoProcesso.getCdAreaDireito());
				pstmt.setInt(4, processo.getLgClienteAutor());
				pstmt.setInt(5, processo.getTpInstancia());
				
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				while(rsm.next()){
					HashMap<String, Object> register = new HashMap<String, Object>();
					
					int tpContagemPrazo = rsm.getInt("TP_CONTAGEM_PRAZO", -1);
					if(tpContagemPrazo<0) {
						tpContagemPrazo = ParametroServices.getValorOfParametroAsInteger("TP_CONTAGEM_PRAZO_DEFAULT", 0, 0, connect);
					}
					
					GregorianCalendar dtEvento = (GregorianCalendar)andamento.getDtAndamento().clone();
					if(tpContagemPrazo==AndamentoPrazoServices.TP_CONTAGEM_CORRIDO) {
						dtEvento.add(GregorianCalendar.DATE, rsm.getInt("QT_DIAS"));
					}
					else if(tpContagemPrazo==AndamentoPrazoServices.TP_CONTAGEM_UTIL) {
						dtEvento = Util.addDiasUteis((GregorianCalendar)dtEvento.clone(), rsm.getInt("QT_DIAS"), connect);
					}
					
					//metadados workflow
					register.put("NM_TIPO_PRAZO", rsm.getString("NM_TIPO_PRAZO"));
					register.put("ST_EVENTO_WORKFLOW", 0);//pendente
					
					//dados do evento
					register.put("DS_ASSUNTO", rsm.getString("NM_TIPO_PRAZO"));
					register.put("DS_DETALHE", rsm.getString("NM_TIPO_PRAZO"));
					register.put("CD_TIPO_PRAZO", rsm.getInt("CD_TIPO_PRAZO"));
					register.put("DT_INICIAL", dtEvento);
					register.put("DT_FINAL", dtEvento);
					register.put("ST_AGENDA_ITEM", 0);
					register.put("CD_PESSOA", processo.getCdAdvogado());
					register.put("NM_PESSOA", PessoaDAO.get(processo.getCdAdvogado(), connect).getNmPessoa());
					register.put("CD_PROCESSO", processo.getCdProcesso());
										
					rsmEventos.addRegister(register);
				}
			}
			
			//Atualizar dt_ultimo_andamento de prc_processo
			Processo processo = ProcessoDAO.get(andamento.getCdProcesso(), connect);
			processo.setDtUltimoAndamento(andamento.getDtAndamento());
			retorno = ProcessoDAO.update(processo, connect); 
			
			// Alterar fase do processo
			TipoAndamento tipoAndamento = TipoAndamentoDAO.get(andamento.getCdTipoAndamento(), connect);
			if(tipoAndamento!=null && tipoAndamento.getCdTipoSituacao()>0) {
				TipoSituacao tipoSituacaoAtual = TipoSituacaoDAO.get(processo.getCdTipoSituacao(), connect);
				TipoSituacao tipoSituacaoProx = TipoSituacaoDAO.get(tipoAndamento.getCdTipoSituacao(), connect);
				
				if(tipoSituacaoAtual!=null && tipoSituacaoProx!=null) {
					if((tipoSituacaoAtual.getLgRetrocede()==1) || (tipoSituacaoAtual.getLgRetrocede()==0 && tipoSituacaoAtual.getNrOrdem()<tipoSituacaoProx.getNrOrdem())) {
						processo.setCdTipoSituacao(tipoSituacaoProx.getCdTipoSituacao());
						retorno = ProcessoDAO.update(processo, connect); 
					}
				}
			}
			
			// GPN WORKFLOW ====================================================
			ResultSetMap rsmGpnRegrasWorkflow = null;
			int tpGatilho = (itemAntigo==null ? WorkflowGatilhoServices.TP_GATILHO_LANCAMENTO_ANDAMENTO : -1);
			rsmGpnRegrasWorkflow = WorkflowRegraServices
					.getByGatilho(tpGatilho, 0, 0, andamento.getCdTipoAndamento(), 0, connect);
			
			Result resultWorkflow = null;
			if(rsmGpnRegrasWorkflow!=null)
				resultWorkflow = aplicarRegrasGpnWorkflow(rsmGpnRegrasWorkflow, itemAntigo, andamento, auth, connect);
			//==================================================================
			
			//COMPLIANCE
			ComplianceManager.process(andamento, auth, tpAcao, connect);
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			Result result = new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ANDAMENTO", andamento);
			result.addObject("DT_ULTIMO_ANDAMENTO", processo.getDtUltimoAndamento());
			
			if(rsmEventos.size()>0){
				result.addObject("EVENTOS_WORKFLOW", rsmEventos);
			}
			
			result.addObject("RESULT_NOTIFICACAO", rEmail);
			result.addObject("CD_TIPO_SITUACAO", (tipoAndamento!=null ? tipoAndamento.getCdTipoSituacao() : 0));
			
			if(resultWorkflow!=null && resultWorkflow.getCode()>0) {
				result.addObject("LG_ENVIA_EMAIL", resultWorkflow.getObjects().get("LG_ENVIA_EMAIL"));
				result.addObject("AUTOTEXTO", resultWorkflow.getObjects().get("AUTOTEXTO"));
				result.addObject("DESTINATARIOS", resultWorkflow.getObjects().get("DESTINATARIOS"));
				result.addObject("ARQUIVOS", resultWorkflow.getObjects().get("ARQUIVOS"));
				result.addObject("ASSINATURA", resultWorkflow.getObjects().get("ASSINATURA"));

				result.addObject("GPN_EVENTOS_WORKFLOW", resultWorkflow.getObjects().get("EVENTOS_WORKFLOW"));
				result.addObject("GPN_ANDAMENTOS_WORKFLOW", resultWorkflow.getObjects().get("ANDAMENTOS_WORKFLOW"));
			}
			
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	private static Result enviarNotificacaoAndamento(ProcessoAndamento andamento, Connection connect) {
		try {
			int tpDestinatarioNotificacaoEmailAndamento = ParametroServices.getValorOfParametroAsInteger("TP_DESTINATARIO_NOTIFICACAO_EMAIL_ANDAMENTO", -1, 0, connect);
			Result rEmail = null;
			
			Processo processo = ProcessoDAO.get(andamento.getCdProcesso(), connect);
			int cdProcesso = processo!=null ? processo.getCdProcesso() : 0;
			
			Mensagem mensagem = new Mensagem();
			mensagem.setCdUsuarioOrigem(andamento.getCdUsuario());
			mensagem.setCdProcesso(cdProcesso);
			mensagem.setDsAssunto("[JurisManager] Novo andamento no processo Nº " + processo.getNrProcesso());
			
			Result rTextoEmail = getTxtEmailAndamento(cdProcesso, andamento.getCdAndamento(), connect);
			
			if(rTextoEmail.getCode()<=0) {
				rEmail = new Result(-6, "Não é possível enviar email automático. Parâmetro do modelo de email não configurado.");
			}
			else {		
				String source = new String((byte[])rTextoEmail.getObjects().get("BLB_DOCUMENTO"));
				
				Pessoa destinatario = null;
				
				if(tpDestinatarioNotificacaoEmailAndamento == TP_DESTINATARIO_RESPONSAVEL_GRUPO_TRABALHO) {
					Grupo grupo = GrupoDAO.get(processo.getCdGrupoTrabalho(), connect);
					if(grupo!=null)
						destinatario = PessoaDAO.get(grupo.getCdProprietario(), connect);
				}
				else
					destinatario = PessoaDAO.get(processo.getCdAdvogado(), connect);
				
				
				if(destinatario==null || (destinatario!=null && destinatario.getCdPessoa()==0)) { 
					//NÃO TROCAR CÓDIGO DE ERRO
					rEmail = new Result(-5, "Não é possível enviar mensagem. Destinatário ("+
							(tpDestinatarioNotificacaoEmailAndamento == TP_DESTINATARIO_RESPONSAVEL_GRUPO_TRABALHO ?
									"Responsável pelo Grupo de Trabalho" : 
									"Responsável pelo Processo")
							+") não encontrado.");
				}
				else {
				
					if(destinatario.getNmEmail()==null || destinatario.getNmEmail().equals("")) {
						rEmail = new Result(-5, "Não é possível enviar mensagem. Destinatário não possui email cadastrado.");
					}
					else {
						int indexInicial = source.indexOf("<p>");
						int indexFinal = source.indexOf("</p></TextFlow", indexInicial) + 4;
						source = source.substring(indexInicial, indexFinal);
						mensagem.setTxtMensagem(source);
						
						HashMap<String,Object> resp = new HashMap<String, Object>();
						resp.put("CD_PESSOA", destinatario.getCdPessoa());
						resp.put("NM_EMAIL", destinatario.getNmEmail());
												
						ArrayList<HashMap<String, Object>> destinatarios = new ArrayList<HashMap<String,Object>>();
						destinatarios.add(resp);
										
						Result r = MensagemServices.enviarMensagem(mensagem, destinatarios, null, null, connect);
						if(r.getCode()<=0) {
							rEmail = new Result(-4, "Erro ao enviar mensagem.");
						}
						else
							rEmail = new Result(1, "Email enviado com sucesso");
					}
				}
			}
			
			return rEmail;
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-7, "Erro desconhecido ao enviar mensagem.");
		}
	}
	
	public static Result getTxtEmailAndamento(int cdProcesso, int cdAndamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			/*
			 * código do modelo para notificação de andamentos via email.
			 */
			int cdModelo = ParametroServices.getValorOfParametroAsInteger("CD_MODELO_EMAIL_ANDAMENTO", 0);
			
			if(cdModelo<=0)
				return new Result(-1, "Parametro do modelo de email para notificação de andamento não encontrado");
			else {
				//Executa modelo com ou sem processo
				Result r = ProcessoServices.executeModeloWeb(cdModelo, 0, cdProcesso, 0, cdAndamento, connect);
						
				if(r.getCode()<=0)
					return new Result(-2, "Erro ao executar modelo.");
				
				byte[] blbDocumento = (byte[])r.getObjects().get("BLB_DOCUMENTO");
				
				if(blbDocumento==null)
					return new Result(-2, "Text de modelo nao encontrado");
				
				return new Result(1, "Executado com sucesso", "BLB_DOCUMENTO", blbDocumento);
			}
		}
		catch(Exception e){
			System.out.println("Erro! ProcessoAndamentoServices.getTxtEmailAndamento: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir Item!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result remove(int cdAndamento, int cdProcesso, AuthData auth){
		return remove(cdAndamento, cdProcesso, false, auth, null);
	}
	
	public static Result remove(int cdAndamento, int cdProcesso, boolean cascade, AuthData auth){
		return remove(cdAndamento, cdProcesso, cascade, auth, null);
	}
	
	public static Result remove(int cdAndamento, int cdProcesso, boolean cascade, AuthData auth, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			ProcessoAndamento andamento = ProcessoAndamentoDAO.get(cdAndamento, cdProcesso, connect);
			
			if(cascade){
				
				/** ARQUIVOS ASSOCIADOS ***/
				PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_processo_arquivo " +
						" WHERE cd_andamento =  "+cdAndamento +
						" AND cd_processo = "+cdProcesso);
								
				retorno = pstmt.executeUpdate();
			}
						
			if(!cascade || retorno>0)
				retorno = ProcessoAndamentoDAO.delete(cdAndamento, cdProcesso, connect);

			Processo processo = ProcessoDAO.get(andamento.getCdProcesso(), connect);
			if(retorno>0) {
				/**Atualizar dt_ultimo_andamento de prc_processo **/				
				processo.setDtUltimoAndamento(getUltimoAndamento(cdProcesso, connect).getDtAndamento());
				retorno = ProcessoDAO.update(processo, connect);
			}
			
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este andamento está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			

			ComplianceManager.process(andamento, auth, ComplianceManager.TP_ACAO_DELETE, connect);
			
			Result result = new Result(1, "Andamento excluído com sucesso!");
			result.addObject("DT_ULTIMO_ANDAMENTO", processo.getDtUltimoAndamento());
			
			return result;
		}
		catch(Exception e){
			System.out.println("Erro! ProcessoAndamentoServices.remove: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir processo!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	
	
	/***** VERIFICAR DEPOIS A UTILIDADE DAS ROTINAS DE FATURAMENTO AUTOMATICO ****/
	
	public static Result insert(ProcessoAndamento objeto, int cdNovaSituacao) {
		return insert(objeto, cdNovaSituacao, null);
	}

	public static Result insert(ProcessoAndamento objeto, int cdNovaSituacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)	{
				connect = Conexao.conectar();
				connect.setAutoCommit(!isConnectionNull);
			}
			//
			if(objeto.getCdUsuario()<=0)
				new Result(-1, "Informações do usuário não encontradas. Você deve sair do sistema e efetuar um novo login!");
			//
			objeto.setDtLancamento(new GregorianCalendar());
			objeto.setDtAlteracao(new GregorianCalendar());
			//System.out.println("debug 1");
			int cdAndamento = ProcessoAndamentoDAO.insert(objeto, connect);
			
			if(cdAndamento<=0)
				return new Result(cdAndamento, "Erro ao tentar incluir andamento");
			// Verifica lançamento automático de faturamento
			//System.out.println("debug 2");
			verificaFaturamentoAutomatico(objeto.getCdProcesso(), cdAndamento, connect);
			// Verifica se mudou a situação e grava a nova situação
			//System.out.println("debug 3");
			Processo processo = ProcessoDAO.get(objeto.getCdProcesso(), connect);
			if(processo.getCdTipoSituacao()!=cdNovaSituacao){
				processo.setCdTipoSituacao(cdNovaSituacao);
				processo.setDtSituacao(new GregorianCalendar());
				ProcessoDAO.update(processo, connect);
			}
			
			if(isConnectionNull)
				connect.commit();
			
			//System.out.println("debug 4");
			return new Result(cdAndamento);
		}
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar incluir andamento");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	static private Result verificaFaturamentoAutomatico(int cdProcesso, int cdAndamento, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)	{
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			ResultSet rs = connect.prepareStatement("SELECT A.cd_tipo_processo, A.cd_grupo_processo, A.cd_orgao, A.lg_cliente_autor, B.* " +
					                                "FROM prc_processo A, prc_processo_andamento B " +
					                                "WHERE A.cd_processo  = B.cd_processo "+
					                                "  AND A.cd_processo  = "+cdProcesso+
					                                "  AND B.cd_andamento = "+cdAndamento).executeQuery();
			if(!rs.next())
				return new Result(-1, "Andamento não localizado!");
			int cdUsuario = rs.getInt("cd_usuario");
			GregorianCalendar dtEventoFinanceiro = com.tivic.manager.util.Util.convTimestampToCalendar(rs.getTimestamp("dt_andamento"));
			//
			String clientes = "";
			ResultSetMap rsmParteCliente = ProcessoServices.getParteCliente(cdProcesso);
			while(rsmParteCliente.next())
				clientes += (clientes.equals("") ? "" : ",") + rsmParteCliente.getInt("cd_pessoa");
			//
			String sqlPesquisa = "SELECT A.*, B.nm_produto_servico, C.nm_orgao, D.nm_pessoa AS nm_cliente, E.nm_grupo_processo, F.nm_tipo_processo "+
					             "FROM prc_tipo_andamento_faturamento A "+
					             "LEFT OUTER JOIN grl_produto_servico B ON (A.cd_produto_servico = B.cd_produto_servico) "+
					             "LEFT OUTER JOIN prc_orgao           C ON (A.cd_orgao = C.cd_orgao) "+
					             "LEFT OUTER JOIN grl_pessoa          D ON (A.cd_cliente = D.cd_pessoa) "+
					             "LEFT OUTER JOIN prc_grupo_processo  E ON (A.cd_grupo_processo = E.cd_grupo_processo) "+
					             "LEFT OUTER JOIN prc_tipo_processo   F ON (A.cd_tipo_processo = F.cd_tipo_processo) "+
					             "WHERE A.cd_tipo_andamento = "+rs.getInt("cd_tipo_andamento")+
                                 "  AND (A.cd_tipo_processo  IS NULL OR A.cd_tipo_processo = "+rs.getInt("cd_tipo_processo")+")"+
                                 "  AND (A.cd_grupo_processo IS NULL OR A.cd_grupo_processo = "+rs.getInt("cd_grupo_processo")+") "+
                                 "  AND (A.cd_orgao IS NULL OR A.cd_orgao = "+rs.getInt("cd_orgao")+") "+
                                 "  AND (A.tp_parte_cliente < 0 OR A.tp_parte_cliente = "+rs.getInt("lg_cliente_autor")+")"+
                                 "  AND (A.cd_cliente IS NULL OR A.cd_cliente IN ("+clientes+"))";

			rs = connect.prepareStatement(sqlPesquisa).executeQuery();
			int cdPessoa = 0;
			while(rs.next())	{
				ProcessoFinanceiro financeiro = new ProcessoFinanceiro(cdProcesso,0/*cdEventoFinanceiro*/,rs.getInt("cd_produto_servico"),
						                                               cdAndamento,cdPessoa,rs.getInt("tp_natureza_evento"),dtEventoFinanceiro,
						                                               rs.getDouble("vl_servico"),new GregorianCalendar(),0 /*cdContaPagar*/,
						                                               0 /*cdContaReceber*/,cdUsuario,0 /*cdArquivo*/, null, 0, -1, -1, 0, 0, 0, 0,
						                                               ProcessoFinanceiroServices.NR_REFERENCIA_PARCELA_UNICA);
				ProcessoFinanceiroDAO.insert(financeiro, connect);
			}
			//
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1);
		}
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao verificar lançamento automático!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Insere um andamento (regAndamento) em uma lista de processos (cdProcessos).
	 * 
	 * @param cdProcessos Lista com os códigos dos processos
	 * @param regAndamento Dados do andamento
	 * @param cdUsuario Código do usuário
	 * @return
	 * 
	 * @since 19/11/2014
	 * @author Maurício
	 */
	public static Result saveLoteAndamentos(ArrayList<Integer> cdProcessos, HashMap<String, Object> regAndamento, int cdUsuario, AuthData auth){
		return saveLoteAndamentos(cdProcessos, regAndamento, cdUsuario, auth, null);
	}
	
	public static Result saveLoteAndamentos(ArrayList<Integer> cdProcessos, HashMap<String, Object> regAndamento, int cdUsuario, AuthData auth, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)	{
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Result result = null;
			ProcessoAndamento andamento;
			
			GregorianCalendar dtAndamento = new GregorianCalendar();
			if(regAndamento.get("DT_ANDAMENTO")!=null) {
				dtAndamento.setTimeInMillis(((java.util.Date)regAndamento.get("DT_ANDAMENTO")).getTime());
			}

			int cdTipoAndamento = (Integer)(regAndamento.get("CD_TIPO_ANDAMENTO")!=null ? regAndamento.get("CD_TIPO_ANDAMENTO") : 0);
			int tpVisibilidade = (Integer)(regAndamento.get("TP_VISIBILIDADE")!=null ? regAndamento.get("TP_VISIBILIDADE") : 0);
			String txtAndamento = (String)(regAndamento.get("TXT_ANDAMENTO")!=null ? regAndamento.get("TXT_ANDAMENTO") : 0); 
					
			for (Integer cdProcesso : cdProcessos) {
				andamento = new ProcessoAndamento(0/*cdAndamento*/, 
												  cdProcesso, 
												  cdTipoAndamento, 
												  dtAndamento, 
												  new GregorianCalendar() /*dtLancamento*/, 
												  txtAndamento, 
												  ST_ANDAMENTO_ATIVO /*stAndamento*/, 
												  0 /*tpInstancia*/, 
												  null /*txtAta*/, 
												  cdUsuario, 
												  new GregorianCalendar() /*dtAlteracao*/, 
												  0 /*tpOrigem*/, 
												  null /*blbAta*/, 
												  tpVisibilidade, 
												  0 /*tpEventoFinanceiro*/, 
												  0.0 /*vlEventoFinanceiro*/, 
												  0 /*cdContaPagar*/, 
												  0 /*cdContaReceber*/, 
												  null /*dtAtualizacaoEdi*/, 
												  0 /*stAtualizacaoEdi*/, 
												  null /*txtPublicacao*/,
												  0, /*cdDocumento*/
												  0 /*cdOrigemAndamento*/,
												  0 /*cdRecorte*/);
				result = save(andamento, null, null, null, auth, connect);
				if(result.getCode()<0) {
					return result;
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Andamentos salvos com sucesso!");
		}
		catch(Exception e) {
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace();
			return new Result(-1, "Erro ao lançar andamentos!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result aplicarRegrasGpnWorkflow(ResultSetMap rsmRegras, ProcessoAndamento itemAnterior, ProcessoAndamento itemAtual, AuthData auth){
		return aplicarRegrasGpnWorkflow(rsmRegras, itemAnterior, itemAtual, auth, null);
	}
	
	public static Result aplicarRegrasGpnWorkflow(ResultSetMap rsmRegras, ProcessoAndamento itemAnterior, ProcessoAndamento itemAtual, AuthData auth, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Result result = new Result(1);
			ResultSetMap rsmEventosWorkflow = new ResultSetMap();
			ResultSetMap rsmAndamentosWorkflow = new ResultSetMap();
			
			Processo processo = ProcessoDAO.get(itemAtual.getCdProcesso(), connect);
			
			while(rsmRegras.next()) {
				ResultSetMap rsmAcao = (ResultSetMap)rsmRegras.getObject("RSMACAO");				
				
				while(rsmAcao.next()) {
					switch(rsmAcao.getInt("tp_acao")) {
						case WorkflowAcaoServices.TP_ACAO_ENVIAR_EMAIL: {
							int cdModeloEmail = ParametroServices.getValorOfParametroAsInteger("CD_MODELO_EMAIL_ANDAMENTO", 0, 0, connect);
							result = ProcessoServices.getDadosEmail(itemAtual.getCdProcesso(), connect);
							
							if(result.getCode()<0) {
								return result;
							}
							//Result rAux = ProcessoServices.executeModeloWeb(cdModeloEmail, 0, itemAtual.getCdProcesso(), 0, itemAtual.getCdAndamento(), connect);
							Result rAux = ProcessoServices.executarAutotextoEmail(cdModeloEmail, itemAtual.getCdProcesso(), itemAtual.getCdAndamento(), 0, connect);
							if(rAux.getCode()>0) {
								result.addObject("LG_ENVIA_EMAIL", 1);
								String txtAutotexto = (String)rAux.getObjects().get("AUTOTEXTO");
								result.addObject("AUTOTEXTO", txtAutotexto);
								LogUtils.debug("autotexto: "+txtAutotexto);
							}
							else {
								LogUtils.debug("Erro ao executar aototexto");
							}
							
							break;
						}
						case WorkflowAcaoServices.TP_ACAO_LANCAR_AGENDA: {
							// CÁLCULO DE DATAS ==============================
							GregorianCalendar dtInicial = new GregorianCalendar();
							GregorianCalendar dtFinal = (GregorianCalendar) dtInicial.clone();
							
							if(rsmAcao.getInt("tp_contagem_prazo")==com.tivic.manager.gpn.WorkflowAcaoServices.TP_CONTAGEM_DIAS_CORRIDOS)
								DateUtil.addDays(dtFinal, rsmAcao.getInt("nr_dias"));
							else if(rsmAcao.getInt("tp_contagem_prazo")==com.tivic.manager.gpn.WorkflowAcaoServices.TP_CONTAGEM_DIAS_UTEIS)
								dtFinal = DateUtil.addUtilsDays(dtFinal, rsmAcao.getInt("nr_dias"));
							
							TipoPrazo tipoPrazo = TipoPrazoDAO.get(rsmAcao.getInt("cd_tipo_prazo"), connect);
							if(tipoPrazo.getTpAgendaItem()!=TipoPrazoServices.TP_TAREFA)
								dtInicial = (GregorianCalendar) dtFinal.clone();
							// ===============================================
							
							AgendaItem agenda = new AgendaItem();
							agenda.setCdProcesso(itemAtual.getCdProcesso());
							agenda.setCdTipoPrazo(tipoPrazo.getCdTipoPrazo());
							agenda.setDtLancamento(new GregorianCalendar());
							agenda.setDtInicial(dtInicial);
							agenda.setDtFinal(dtFinal);
							agenda.setStAgendaItem(AgendaItemServices.ST_AGENDA_A_CUMPRIR);
							agenda.setVlServico(0.0d);
							agenda.setCdUsuario(auth.getUsuario().getCdUsuario());
							
							//RESPONSAVEL PELA AGENDA
							if(rsmAcao.getInt("cd_pessoa", 0)>0)
								agenda.setCdPessoa(rsmAcao.getInt("cd_pessoa"));
							else if(rsmAcao.getInt("cd_grupo_trabalho", 0)>0)
								agenda.setCdGrupoTrabalho(rsmAcao.getInt("cd_grupo_trabalho"));
							else if(rsmAcao.getInt("tp_responsavel_agenda", -1)>-1) {
								switch(rsmAcao.getInt("tp_responsavel_agenda")) {
									case WorkflowAcaoServices.TP_RESPONSAVEL_ADV:
										agenda.setCdPessoa(processo.getCdAdvogado());										
										break;
									case WorkflowAcaoServices.TP_RESPONSAVEL_GRUPO:
										agenda.setCdGrupoTrabalho(processo.getCdGrupoTrabalho());
										break;
									case WorkflowAcaoServices.TP_RESPONSAVEL_USUARIO:
										agenda.setCdPessoa(auth.getUsuario().getCdPessoa());
										break;
								}
							}
							
							if(rsmAcao.getInt("lg_sugestao")==0) {
								result = AgendaItemServices.save(agenda, connect);
								if(result.getCode()<=0) 
									LogUtils.log("WORKFLOW ERROR> "+result.getMessage(), LogUtils.VERBOSE_LEVEL_INFO);
							}
							else {
								HashMap<String, Object> register = new HashMap<>();
								register.put("CD_PROCESSO", agenda.getCdProcesso());
								register.put("CD_TIPO_PRAZO", agenda.getCdTipoPrazo());
								register.put("DT_LANCAMENTO", agenda.getDtLancamento());
								register.put("DT_INICIAL", agenda.getDtInicial());
								register.put("DT_FINAL", agenda.getDtFinal());
								register.put("ST_AGENDA_ITEM", agenda.getStAgendaItem());
								register.put("VL_SERVICO", agenda.getVlServico());
								register.put("CD_USUARIO", agenda.getCdUsuario());
								if(agenda.getCdPessoa()>0) {
									register.put("CD_PESSOA", agenda.getCdPessoa());
									Pessoa pessoa = PessoaDAO.get(agenda.getCdPessoa(), connect);
									register.put("NM_RESPONSAVEL", pessoa.getNmPessoa());
									register.put("NM_PESSOA", pessoa.getNmPessoa());
								}
								else if(agenda.getCdGrupoTrabalho()>0) {
									register.put("CD_GRUPO_TRABALHO", agenda.getCdGrupoTrabalho());
									Grupo grupoTrabalho = GrupoDAO.get(agenda.getCdGrupoTrabalho(), connect);
									register.put("NM_RESPONSAVEL", grupoTrabalho.getNmGrupo());
									register.put("NM_GRUPO_TRABALHO", grupoTrabalho.getNmGrupo());
								}
								
								register.put("NM_TIPO_PRAZO", tipoPrazo.getNmTipoPrazo());
								register.put("ST_EVENTO_WORKFLOW", 0);//pendente
								register.put("TP_AGENDA_ITEM", tipoPrazo.getTpAgendaItem());
								
								rsmEventosWorkflow.addRegister(register);
							}
							
							break;
						}
						case WorkflowAcaoServices.TP_ACAO_LANCAR_ANDAMENTO: {
							
							ProcessoAndamento andamento = new ProcessoAndamento();
							andamento.setCdProcesso(itemAtual.getCdProcesso());
							andamento.setCdTipoAndamento(rsmAcao.getInt("cd_tipo_andamento"));
							andamento.setCdUsuario(auth.getUsuario().getCdUsuario());
							andamento.setDtLancamento(new GregorianCalendar());
							andamento.setDtAndamento(new GregorianCalendar());
							andamento.setStAndamento(ProcessoAndamentoServices.ST_ANDAMENTO_ATIVO);
							andamento.setTpVisibilidade(ProcessoAndamentoServices.TP_VISIBILIDADE_PUBLICO);
							//TODO: origem WORKFLOW
							andamento.setTpOrigem(-1);
							
							if(rsmAcao.getInt("lg_sugestao")==0) {
								result = ProcessoAndamentoServices.save(andamento, null, null, null, null, connect);
								if(result.getCode()<=0) 
									LogUtils.log("WORKFLOW ERROR> "+result.getMessage(), LogUtils.VERBOSE_LEVEL_INFO);
							}
							else {
								HashMap<String, Object> register = new HashMap<>();
								register.put("CD_PROCESSO", andamento.getCdProcesso());
								register.put("CD_TIPO_ANDAMENTO", andamento.getCdTipoAndamento());
								register.put("CD_USUARIO", andamento.getCdUsuario());
								register.put("DT_LANCAMENTO", andamento.getDtLancamento());
								register.put("DT_ANDAMENTO", andamento.getDtAndamento());
								register.put("ST_ANDAMENTO", andamento.getStAndamento());
								register.put("TP_ORIGEM", andamento.getTpOrigem());
								register.put("TP_VISIBILIDADE", andamento.getTpVisibilidade());
								
								rsmAndamentosWorkflow.addRegister(register);
							}
							
							break;
						}
						case WorkflowAcaoServices.TP_ACAO_LANCAR_DESPESA: {
							break;
						}
						case WorkflowAcaoServices.TP_ACAO_LANCAR_RECEITA: {
							break;
						}
						case WorkflowAcaoServices.TP_ACAO_MUDAR_FASE: {
							processo.setCdTipoSituacao(rsmAcao.getInt("cd_tipo_situacao"));
							result = ProcessoServices.save(processo, null, null, null, auth.getUsuario().getCdUsuario(), null, auth, connect);
							if(result.getCode()<=0) 
								LogUtils.log("WORKFLOW ERROR> "+result.getMessage(), LogUtils.VERBOSE_LEVEL_INFO);
							break;
						}
					}
				}
			}
			
			connect.commit();
						
			result.setMessage("Regras aplicadas com sucesso");
			result.addObject("EVENTOS_WORKFLOW", rsmEventosWorkflow);
			result.addObject("ANDAMENTOS_WORKFLOW", rsmAndamentosWorkflow);
			
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ProcessoAndamento getUltimoAndamento(int cdProcesso, Connection connection) {
		try {
			connection = connection==null ? Conexao.conectar() : connection;
			int cdAndamento = 0;
			
			ResultSet rs = connection.prepareStatement("SELECT MAX(cd_andamento) AS cd_andamento FROM prc_processo_andamento WHERE cd_processo="+cdProcesso).executeQuery();
			if(rs.next())
				cdAndamento = rs.getInt("cd_andamento");
						
			return ProcessoAndamentoDAO.get(cdAndamento, cdProcesso, connection);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public static ResultSetMap getAndamentos(int cdProcesso, int limite, ArrayList<?> visibilidade, Connection connect) {
		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			return new ResultSetMap(connect
					.prepareStatement(" SELECT A.*,"
						    		+ " B.nm_tipo_andamento "
						    		+ " FROM prc_processo_andamento A "
						    		+ " LEFT OUTER JOIN prc_tipo_andamento B ON (A.cd_tipo_andamento = B.cd_tipo_andamento)"
						    		+ " WHERE A.cd_processo="+cdProcesso
						    		+ " AND A.tp_visibilidade IN ("+Util.join(visibilidade, ",")+")"
						    		+ " ORDER BY A.dt_andamento DESC LIMIT "+limite)
					.executeQuery());
			
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoAndamentoServices.find: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}		
	}
	
	public static ResultSetMap getLogCompliance(int cdAndamento, int cdProcesso, boolean lgDelete) {
		return getLogCompliance(cdAndamento, cdProcesso, lgDelete, null);
	}
	
	public static ResultSetMap getLogCompliance(int cdAndamento, int cdProcesso, boolean lgDelete, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			ResultSetMap rsm = ComplianceManager
					.search(" SELECT * FROM prc_processo_andamento "
						+ " WHERE 1=1"
						+ (lgDelete ? 
						  " AND cd_processo="+cdProcesso+" AND tp_acao_compliance="+ComplianceManager.TP_ACAO_DELETE	
						  :
						  " AND cd_andamento="+cdAndamento+" AND cd_processo="+cdProcesso)
						+ " ORDER BY dt_compliance DESC ");
			
			while(rsm.next()) {
				if(rsm.getPointer()==0 && !lgDelete)
					rsm.setValueToField("tp_versao_compliance", "ATUAL");
				else
					rsm.setValueToField("tp_versao_compliance", "ANTIGA");
				
				rsm.setValueToField("nm_tp_acao", ComplianceManager.tipoAcao[rsm.getInt("tp_acao_compliance")].toUpperCase());
				
				if(rsm.getInt("cd_usuario_compliance", 0) > 0) {
					Usuario usuario = UsuarioDAO.get(rsm.getInt("cd_usuario_compliance"), connect);
					Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa(), connect);
					rsm.setValueToField("nm_usuario_compliance", pessoa.getNmPessoa());
				}
				
				if(rsm.getInt("cd_tipo_andamento", 0) > 0) {
					TipoAndamento tipoAndamento = TipoAndamentoDAO.get(rsm.getInt("cd_tipo_andamento"), connect);
					rsm.setValueToField("nm_tipo_andamento", tipoAndamento.getNmTipoAndamento());
				}
				
				if(rsm.getInt("cd_usuario", 0) > 0) {
					Usuario usuario = UsuarioDAO.get(rsm.getInt("cd_usuario"), connect);
					Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa(), connect);
					rsm.setValueToField("nm_usuario", pessoa.getNmPessoa());
				}
				
				if(lgDelete) {
					ResultSetMap rsmDetalhes = new ResultSetMap();
					HashMap<String, Object> regAtual = (HashMap<String, Object>)rsm.getRegister().clone();
					regAtual.put("TP_ITEM_COMPLIANCE", " ");
					rsmDetalhes.addRegister(regAtual);
					rsm.setValueToField("RSM_DETALHE", rsmDetalhes);
					
				}
			}
			rsm.beforeFirst();

			
			if(!lgDelete) {
				while(rsm.next()) {
					ResultSetMap rsmDetalhes = new ResultSetMap();
					HashMap<String, Object> regAtual = (HashMap<String, Object>)rsm.getRegister().clone();
					regAtual.put("TP_ITEM_COMPLIANCE", "PARA");
					rsmDetalhes.addRegister(regAtual);
					
					if(rsm.next()) { //como a ordem é decrescente, o próximo registro representa a versão anterior
						HashMap<String, Object> regAnterior = (HashMap<String, Object>)rsm.getRegister().clone();
						regAnterior.put("TP_ITEM_COMPLIANCE", "DE");
						rsmDetalhes.addRegister(regAnterior);
						rsm.previous();
					}
					
					ArrayList<String> fields = new ArrayList<>();
					fields.add("TP_ITEM_COMPLIANCE");
					rsmDetalhes.orderBy(fields);
					rsm.setValueToField("RSM_DETALHE", rsmDetalhes);
				}
				rsm.beforeFirst();
			}
			
			return rsm;
		}
		catch (Exception e) {
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}