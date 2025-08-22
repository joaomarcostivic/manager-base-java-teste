package com.tivic.manager.agd;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.json.JSONObject;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.EmpresaDAO;
import com.tivic.manager.grl.ModeloDocumento;
import com.tivic.manager.grl.ModeloDocumentoDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.log.LogServices;
import com.tivic.manager.msg.Mensagem;
import com.tivic.manager.msg.MensagemServices;
import com.tivic.manager.msg.Notificacao;
import com.tivic.manager.msg.NotificacaoGrupoServices;
import com.tivic.manager.msg.NotificacaoGrupoUsuarioServices;
import com.tivic.manager.msg.NotificacaoServices;
import com.tivic.manager.msg.NotificacaoUsuarioServices;
import com.tivic.manager.msg.RegraNotificacaoServices;
import com.tivic.manager.prc.Orgao;
import com.tivic.manager.prc.OrgaoServices;
import com.tivic.manager.prc.PrazoSecundarioServices;
import com.tivic.manager.prc.Processo;
import com.tivic.manager.prc.ProcessoAndamento;
import com.tivic.manager.prc.ProcessoAndamentoDAO;
import com.tivic.manager.prc.ProcessoAndamentoServices;
import com.tivic.manager.prc.ProcessoArquivoAgenda;
import com.tivic.manager.prc.ProcessoArquivoAgendaServices;
import com.tivic.manager.prc.ProcessoDAO;
import com.tivic.manager.prc.ProcessoSentencaServices;
import com.tivic.manager.prc.ProcessoServices;
import com.tivic.manager.prc.TipoAndamento;
import com.tivic.manager.prc.TipoAndamentoDAO;
import com.tivic.manager.prc.TipoAndamentoServices;
import com.tivic.manager.prc.TipoPrazo;
import com.tivic.manager.prc.TipoPrazoDAO;
import com.tivic.manager.prc.TipoPrazoServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoDAO;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.DocumentoOcorrenciaDAO;
import com.tivic.manager.ptc.DocumentoOcorrenciaServices;
import com.tivic.manager.ptc.DocumentoPendencia;
import com.tivic.manager.ptc.DocumentoPendenciaDAO;
import com.tivic.manager.ptc.DocumentoServices;
import com.tivic.manager.ptc.DocumentoTramitacao;
import com.tivic.manager.ptc.DocumentoTramitacaoServices;
import com.tivic.manager.ptc.TipoPendencia;
import com.tivic.manager.ptc.TipoPendenciaDAO;
import com.tivic.manager.ptc.WorkflowAcaoServices;
import com.tivic.manager.ptc.WorkflowGatilhoServices;
import com.tivic.manager.ptc.WorkflowRegraServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.seg.GrupoServices;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.util.ComplianceManager;
import com.tivic.manager.util.DateUtil;
import com.tivic.manager.util.DeveloperServices;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class AgendaItemServices {

	public static final String[] situacoesAgenda = {"A cumprir", "Cumprido", "Cancelado", "Vencido", "A auditar", "Repassado", "Rejeitado"};
	
	public static final int ST_AGENDA_A_CUMPRIR = 0;
	public static final int ST_AGENDA_CUMPRIDO = 1;
	public static final int ST_AGENDA_CANCELADO = 2;
	public static final int ST_AGENDA_VENCIDO = 3;
	public static final int ST_AGENDA_PENDENTE = 4;
	public static final int ST_AGENDA_REPASSADO = 5;
	public static final int ST_AGENDA_REJEITADO = 6;

	public static final int TP_DESTINATARIO_RESPONSAVEL_AGENDA = 0;
	public static final int TP_DESTINATARIO_RESPONSAVEL_PROCESSO = 1;
	public static final int TP_DESTINATARIO_RESPONSAVEL_GRUPO_TRABALHO = 2;

	private static int CHANGE_PESSOA_PESSOA = 0;
	private static int CHANGE_PESSOA_GRUPO  = 1;
	private static int CHANGE_GRUPO_PESSOA  = 2;
	private static int CHANGE_GRUPO_GRUPO   = 3;
	
	public static Result save(AgendaItem item){
		return save(item, null, false, false, 0, null, null);
	}
	
	public static Result save(AgendaItem item, AuthData auth){
		return save(item, null, false, false, 0, auth, null);
	}
	
	public static Result save(AgendaItem item, Connection connect){
		return save(item, null, false, false, 0, null, connect);
	}
	
	public static Result save(AgendaItem item, AuthData auth, Connection connect){
		return save(item, null, false, false, 0, auth, connect);
	}
	
	public static Result save(AgendaItem item, ArrayList<ProcessoArquivoAgenda> arquivosCompartilhados){
		return save(item, arquivosCompartilhados, false, false, 0, null);
	}
	
	public static Result save(AgendaItem item, ArrayList<ProcessoArquivoAgenda> arquivosCompartilhados, boolean lgAtualizarCorrespondente){
		return save(item, arquivosCompartilhados, lgAtualizarCorrespondente, false, 0, null);
	}
	
	public static Result save(AgendaItem item, ArrayList<ProcessoArquivoAgenda> arquivosCompartilhados, boolean lgAtualizarCorrespondente, boolean lgEnviarMensagem, int cdEmpresa){
		return save(item, arquivosCompartilhados, lgAtualizarCorrespondente, lgEnviarMensagem, cdEmpresa, null, null);
	}
	
	public static Result save(AgendaItem item, ArrayList<ProcessoArquivoAgenda> arquivosCompartilhados, boolean lgAtualizarCorrespondente, 
			boolean lgEnviarMensagem, int cdEmpresa, AuthData authData){
		return save(item, arquivosCompartilhados, lgAtualizarCorrespondente, lgEnviarMensagem, cdEmpresa, authData, null);
	}
	
	public static Result save(AgendaItem item, ArrayList<ProcessoArquivoAgenda> arquivosCompartilhados, boolean lgAtualizarCorrespondente, 
			boolean lgEnviarMensagem, int cdEmpresa, AuthData authData, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(item==null)
				return new Result(-1, "Erro ao salvar. Item é nulo");
						
			//COMPLIANCE
			int tpAcao = ComplianceManager.TP_ACAO_ANY;
			
			//LOG
			String idAcao = "";
			AgendaItem oldValue = null;
			
			
			//Ativar para audiências cumpridas
			TipoPrazo tpPrazo = TipoPrazoDAO.get(item.getCdTipoPrazo(), connect);
			boolean workflow = true;
//			if(tpPrazo!=null)
//				workflow = (tpPrazo.getTpAgendaItem()==TipoPrazoServices.TP_AUDIENCIA && item.getStAgendaItem()==ST_AGENDA_CUMPRIDO);//false;
			
			boolean andamentoAutomatico = false;

			AgendaItem itemAnterior = AgendaItemDAO.get(item.getCdAgendaItem(), connect);
			
			int retorno;
			if (item.getStAgendaItem()!=ST_AGENDA_CUMPRIDO)
				item.setDtRealizacao(null);
			
			//verificação de agendas cumpridas sem data
			if(item.getStAgendaItem()==ST_AGENDA_CUMPRIDO && item.getDtRealizacao()==null) {
				LogUtils.debug("\n\n");
				LogUtils.debug("CUMPRIDO_SEM_DATA");
				LogUtils.debug(item.toString());
			}
			
			if(item.getDtFinal()==null)
				item.setDtFinal(item.getDtInicial());
			
			// Validar Horario (verificar se não ha outra agenda marcada para o mesmo local e horário)
			int validar = ParametroServices.getValorOfParametroAsInteger("LG_VALIDAR_HORARIO", 0, item.getCdEmpresa(), connect);
			
			if(validar ==  1) {
				String data = Util.formatDateTime(item.getDtInicial(), "dd/MM/yyyy HH:mm");
				ResultSetMap rsmAgenda = new ResultSetMap();
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("O.CD_LOCAL", Integer.toString(item.getCdLocal()), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("A.DT_INICIAL", data, ItemComparator.EQUAL, Types.TIMESTAMP));	
				criterios.add(new ItemComparator("A.CD_EMPRESA", Integer.toString(item.getCdEmpresa()), ItemComparator.EQUAL, Types.INTEGER));
				
				rsmAgenda = getList(criterios);
				
				if (rsmAgenda.size()>0)
					return new Result(-5, "Agenda ocupada.");
				
				//nao permite salvar horarios bloqueados
				if (LocalHorarioBloqueadoServices.verificarBloqueados(item.getCdLocal(), item.getDtInicial()))
					return new Result(-5, "Agenda bloqueada.");
			}

			// PTC WORKFLOW ====================
			ResultSetMap rsmRegras = null;
			boolean lgPtcWorkflow = ParametroServices.getValorOfParametroAsInteger("LG_PTC_WORKFLOW", 0, 0, connect)==1;
			if(lgPtcWorkflow) {
				ArrayList<Integer> gatilhos = new ArrayList<>();
				gatilhos.add(WorkflowGatilhoServices.TP_GATILHO_LANCAMENTO_AGENDA);
				gatilhos.add(WorkflowGatilhoServices.TP_GATILHO_CUMPRIMENTO_AGENDA);
				//TODO: mudança de campo
				
				rsmRegras = WorkflowRegraServices.getAllByGatilhos(gatilhos, connect);
				//rsmRegras = WorkflowRegraServices.getAllByNmEntidade("AGENDAMENTO", connect);
				
				LogUtils.debug("AgendaItemServices.save");
				LogUtils.debug("lgPtcWorkflow: "+lgPtcWorkflow);
				LogUtils.debug("rsmRegras: "+rsmRegras);
			}
			// =================================
			
			boolean lgOcorrenciaInsert = false;
			if(item.getCdAgendaItem()==0){
				
				//Compliance
				tpAcao = ComplianceManager.TP_ACAO_INSERT;
				
				//Log
				if(authData!=null)
					idAcao = authData.getIdAcaoInsert();
				
				lgOcorrenciaInsert = true;
				item.setDtLancamento(new GregorianCalendar());
				retorno = AgendaItemDAO.insert(item, connect);
				item.setCdAgendaItem(retorno);
				if(item.getCdProcesso()>0)
					andamentoAutomatico = true;
					//TODO: verificar o funcionamento do workflow aqui
					//workflow=true;
			}
			else {
				
				//Compliance e Log
				tpAcao = ComplianceManager.TP_ACAO_UPDATE;
				
				//Log
				
				if (authData!=null)
					idAcao = authData.getIdAcaoUpdate();
				oldValue = AgendaItemDAO.get(item.getCdAgendaItem(), connect);
				
				retorno = AgendaItemDAO.update(item, connect);
				
				//ativa workflow apenas no momento do cumprimento
				//workflow = item.getStAgendaItem()==ST_AGENDA_CUMPRIDO && oldValue.getStAgendaItem()!=ST_AGENDA_CUMPRIDO;
				
				// se cancelar, 
				// cancela as derivadas (workflow)
				if(item.getStAgendaItem() == AgendaItemServices.ST_AGENDA_CANCELADO) {
					PreparedStatement ps = connect.prepareStatement("SELECT cd_agenda_item FROM agd_agenda_item WHERE cd_agenda_item_superior="+item.getCdAgendaItem());
					ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
					while(rsm.next()) {
						AgendaItem agendaAux = AgendaItemDAO.get(rsm.getInt("cd_agenda_item"), connect);
						agendaAux.setStAgendaItem(item.getStAgendaItem());
						agendaAux.setDtRealizacao(item.getDtRealizacao());
						
						Result rAux = save(agendaAux, authData, connect);
						if(rAux.getCode()<=0) {
							if(isConnectionNull)
								connect.rollback();
							return rAux;
						}
					}
				}
			}
			
			if(retorno<0) {
				Conexao.rollback(connect);
				if(retorno==-666) {
					//LOG
					LogServices.log(LogServices.ANY, idAcao, authData, item, oldValue, 
							"CHANGE_PROCESSO_EXCEPTION\n"
							+ "A mudança de processo não é permitida.\n"
							+ "De:\n"+LogServices.formatValues(oldValue)+"\nPara:\n"+LogServices.formatValues(item));
					return new Result(retorno, "A mudança de processo não é permitida.");
				}
				else 
					return new Result(-2, "Erro ao salvar agenda.");
			}
			
			Orgao orgao = null;
			
			if(lgAtualizarCorrespondente) {
				
				Processo processo = ProcessoDAO.get(item.getCdProcesso(), connect);
				orgao = OrgaoServices.getOrgaoByPessoa(item.getCdPessoa(), connect);
				if(orgao==null) {
					return new Result(-4, "Correspondente indicado não existe.");
				}
				
				processo.setCdOrgao(orgao.getCdOrgao());
				retorno = ProcessoDAO.update(processo, connect);
				if(retorno < 0) {
					Conexao.rollback(connect);
					return new Result(-3, "Erro ao atualizar correspondente.");
				}
			}
			
			/**
			 * ENVIO DE NOTIFICAÇÃO POR EMAIL
			 * Se lgEnviarMensagem é true, enviar mensagem sempre. Pode ser usada para forçar o envio de email de qualquer agenda.
			 * Se o parametro lgNotificacaoAutomaticaEmail é true e o tipo de prazo da agenda está indicando que permite envio automático (TipoPrazo.lgEmail == true); 
			 */
			boolean lgNotificacaoAutomaticaEmail = ParametroServices.getValorOfParametroAsInteger("LG_NOTIFICACAO_AUTOMATICA_EMAIL", 0, 0, connect)==1;
			boolean lgTipoPrazoEmail = false;
			
			if(item.getCdTipoPrazo()>0) {
				TipoPrazo tipo = TipoPrazoDAO.get(item.getCdTipoPrazo(), connect);
				lgTipoPrazoEmail = tipo.getLgEmail()==1;
			}
			else {
				
				if (item.getCdTipoPrazoDocumento()>0) {
					com.tivic.manager.ptc.TipoPrazo tipoD = com.tivic.manager.ptc.TipoPrazoDAO.get(item.getCdTipoPrazoDocumento(), connect);
					lgTipoPrazoEmail = tipoD.getLgEmail()==1;
				
				} else {
					/**
					 * MOBILIDADE 
					 * Desativando o Workflow quando não tem um tipo de prazo na agenda.
					 */
					workflow = false;
				}
			}
			
			Result rEmail = null;

			if(lgEnviarMensagem || (lgNotificacaoAutomaticaEmail && lgTipoPrazoEmail)) {
				rEmail = enviarNotificacaoAgenda(item, cdEmpresa, connect);
			}
			
			//remover todos os vinculos
			Result r = ProcessoArquivoAgendaServices.removeByProcessoAgenda(item.getCdAgendaItem(), item.getCdProcesso(), connect);
			
			if(item.getCdProcesso()>0 && item.getCdAgendaItem()>0 && arquivosCompartilhados!=null && arquivosCompartilhados.size()>0) {
				//update vinculos de arquivos
				if(r.getCode()>0) {
					for (ProcessoArquivoAgenda processoArquivoAgenda : arquivosCompartilhados) {
						processoArquivoAgenda.setCdAgendaItem(item.getCdAgendaItem());
						r = ProcessoArquivoAgendaServices.save(processoArquivoAgenda, connect);
						if(r.getCode()<=0) {
							retorno = r.getCode();
							break;
						}
					}
				}
				else
					retorno = r.getCode();
				
				if(retorno<0) {
					Conexao.rollback(connect);
					return new Result(-4, "Erro ao salvar arquivos compartilhados.");
				}
			}
			
			//WORKFLOW EVENTOS
			ResultSetMap rsmEventosWorkflow = new ResultSetMap();
			if(workflow) {
				int tpAcaoWorklow = -1;
				switch (tpAcao) {
					case ComplianceManager.TP_ACAO_INSERT:
						tpAcaoWorklow = PrazoSecundarioServices.TP_ACAO_INSERT;					
						break;
					case ComplianceManager.TP_ACAO_DELETE:
						tpAcaoWorklow = PrazoSecundarioServices.TP_ACAO_DELETE;					
						break;
					case ComplianceManager.TP_ACAO_UPDATE:
						if(item.getStAgendaItem()==ST_AGENDA_CUMPRIDO && oldValue.getStAgendaItem()!=ST_AGENDA_CUMPRIDO)
							tpAcaoWorklow = PrazoSecundarioServices.TP_ACAO_DONE;
						else 
							tpAcaoWorklow = PrazoSecundarioServices.TP_ACAO_UPDATE;
						break;
					default:
						tpAcaoWorklow = -1;//TODOS
						break;
				}
				
				PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nm_tipo_prazo, B.tp_agenda_item, C.nm_tipo_processo " +
					"FROM prc_prazo_secundario A " +
					"LEFT OUTER JOIN prc_tipo_prazo B ON (A.cd_prazo_secundario = B.cd_tipo_prazo) " +
					"LEFT OUTER JOIN prc_tipo_processo C ON (A.cd_tipo_processo = C.cd_tipo_processo) " +
					"WHERE A.cd_tipo_prazo = ? " +
					"   AND (A.cd_tipo_processo IS NULL OR A.cd_tipo_processo = ?) " +
					"   AND A.tp_acao IN (-1, ?) " +
					"ORDER BY A.QT_DIAS");
				
				Processo processo = ProcessoDAO.get(item.getCdProcesso(), connect);
				
				pstmt.setInt(1, item.getCdTipoPrazo());
				pstmt.setInt(2, (processo!=null ? processo.getCdProcesso() : 0));
				pstmt.setInt(3, tpAcaoWorklow);
				
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				while(rsm.next()){
					HashMap<String, Object> register = new HashMap<String, Object>();
					
					int tpContagemPrazo = rsm.getInt("TP_CONTAGEM_PRAZO", -1);
					if(tpContagemPrazo<0) {
						tpContagemPrazo = ParametroServices.getValorOfParametroAsInteger("TP_CONTAGEM_PRAZO_DEFAULT", 0, 0, connect);
					}
					
					GregorianCalendar dtEvento = (GregorianCalendar)item.getDtInicial().clone();
					if(tpContagemPrazo==PrazoSecundarioServices.TP_CONTAGEM_CORRIDO) {
						dtEvento.add(GregorianCalendar.DATE, rsm.getInt("QT_DIAS"));
					}
					else if(tpContagemPrazo==PrazoSecundarioServices.TP_CONTAGEM_UTIL) {
						dtEvento = Util.addDiasUteis((GregorianCalendar)dtEvento.clone(), rsm.getInt("QT_DIAS"), connect);
					}
					
					
					//metadados workflow
					register.put("NM_TIPO_PRAZO", rsm.getString("NM_TIPO_PRAZO"));
					register.put("ST_EVENTO_WORKFLOW", 0);//pendente
					
					//dados do evento
					register.put("DS_ASSUNTO", rsm.getString("NM_TIPO_PRAZO"));
					register.put("DS_DETALHE", rsm.getString("NM_TIPO_PRAZO"));
					register.put("CD_TIPO_PRAZO", rsm.getInt("CD_PRAZO_SECUNDARIO"));
					register.put("DT_INICIAL", dtEvento);
					register.put("DT_FINAL", dtEvento);
					register.put("ST_AGENDA_ITEM", 0);
					register.put("TP_AGENDA_ITEM", rsm.getString("tp_agenda_item"));
					register.put("CD_PROCESSO", (processo!=null ? processo.getCdProcesso() : 0));
					register.put("CD_AGENDA_ITEM_SUPERIOR", item.getCdAgendaItem());
					
					if(tpPrazo.getTpAgendaItem()==TipoPrazoServices.TP_AUDIENCIA &&
							ParametroServices.getValorOfParametroAsInteger("CD_PRAZO_COMPARECIMENTO_AUDIENCIA", 0, cdEmpresa, connect)==rsm.getInt("CD_PRAZO_SECUNDARIO")) {
						
						register.put("DT_INICIAL", item.getDtInicial());
						register.put("DT_FINAL", item.getDtFinal());
					}
										
					rsmEventosWorkflow.addRegister(register);
				}
			}
			
			//WORKFLOW ANDAMENTO
			TipoPrazo tipoPrazoWorkflow = null;
			if(workflow || item.getStAgendaItem() == AgendaItemServices.ST_AGENDA_CUMPRIDO) { 
				tipoPrazoWorkflow = TipoPrazoDAO.get(item.getCdTipoPrazo(), connect);
				TipoAndamento tipoAndamento = TipoAndamentoDAO.get(tipoPrazoWorkflow.getCdTipoAndamento(), connect);
				
				//garantir que só andamentos de tipos ativos sejam sugeridos
				if(tipoAndamento!=null && tipoAndamento.getStCadastro()==TipoAndamentoServices.ST_CADASTRO_INATIVO) {
					tipoPrazoWorkflow = null;
				}
			}
			
			if(andamentoAutomatico){
				TipoPrazo tipoPrazo = TipoPrazoDAO.get(item.getCdTipoPrazo(), connect);
				TipoAndamento tipoAndamento = TipoAndamentoDAO.get(tipoPrazo.getCdTipoAndamento(), connect);
				if(tipoPrazo.getCdTipoAndamento()>0 && tipoAndamento.getStCadastro()==TipoAndamentoServices.ST_CADASTRO_ATIVO) {
					ProcessoAndamento andamento = new ProcessoAndamento(0, //cdAndamento, 
							item.getCdProcesso(), 
							tipoPrazo.getCdTipoAndamento(), 
							new GregorianCalendar(), //dtAndamento, 
							new GregorianCalendar(), //dtLancamento, 
							Util.formatDate(item.getDtInicial(), "dd/MM/yyyy HH:mm")+" \"" + tipoPrazo.getNmTipoPrazo().toUpperCase()+"\"", //txtAndamento, 
							1, //stAndamento, 
							0, //tpInstancia, 
							null, //txtAta, 
							item.getCdUsuario(), 
							null, //dtAlteracao, 
							0, //tpOrigem, 
							null, //blbAta, 
							tipoAndamento.getTpVisibilidade(), //tpVisibilidade, 
							0, //tpEventoFinanceiro, 
							0.0, //vlEventoFinanceiro, 
							0, //cdContaPagar, 
							0, //cdContaReceber, 
							null, //dtAtualizacaoEdi, 
							0, //stAtualizacaoEdi, 
							null, //txtPublicacao
							0, /*cdDocumento*/
							0 /*cdOrigemAndamento*/,
							0 /*cdRecorte*/);
					
					retorno = ProcessoAndamentoServices.save(andamento, null, null, null, authData, connect).getCode();
				}
			}
			
			// PTC WORKFLOW ====================
			if(rsmRegras!=null)
				retorno = aplicarRegrasPtcWorkflow(rsmRegras, itemAnterior, item, connect).getCode();
			// =================================
			
			// GPN WORKFLOW ====================================================
			int tpGatilho = -1;
			if(itemAnterior==null)
				tpGatilho = com.tivic.manager.gpn.WorkflowGatilhoServices.TP_GATILHO_LANCAMENTO_AGENDA;
			else if((itemAnterior.getStAgendaItem()==ST_AGENDA_A_CUMPRIR || itemAnterior.getStAgendaItem()==ST_AGENDA_REPASSADO) && item.getStAgendaItem()==ST_AGENDA_CUMPRIDO)
				tpGatilho = com.tivic.manager.gpn.WorkflowGatilhoServices.TP_GATILHO_CUMPRIMENTO_AGENDA;
			
			ResultSetMap rsmGpnRegrasWorkflow = com.tivic.manager.gpn.WorkflowRegraServices
					.getByGatilho(tpGatilho, 0, 0, 0, 0, connect);
			
			Result resultWorkflow = null;
			if(rsmGpnRegrasWorkflow!=null)
				resultWorkflow = aplicarRegrasGpnWorkflow(rsmGpnRegrasWorkflow, itemAnterior, item, authData, connect);
			//==================================================================
			
			// OCORRENCIA AUTOMATICA ===========
			int lgOcorrenciaAutomatica = ParametroServices.getValorOfParametroAsInteger("LG_OCORRENCIA_AUTOMATICA_AGENDA", 0, 0, connect);
			if(lgOcorrenciaAutomatica>0) {
				int cdTipoOcorrencia = 0;
				String txtOcorrencia = "";
				
				if(lgOcorrenciaInsert) {
					cdTipoOcorrencia = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OCORRENCIA_INSERT_AGENDA", 0, 0, connect);
				}
				else {
					cdTipoOcorrencia = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OCORRENCIA_UPDATE_AGENDA", 0, 0, connect);
					
					txtOcorrencia = "Agenda modificada.";
				}
					
				AgendaItemOcorrencia ocorrencia = new AgendaItemOcorrencia(0, 
						item.getCdAgendaItem(), 
						cdTipoOcorrencia, 
						item.getCdUsuario(), 
						new GregorianCalendar(), 
						txtOcorrencia, 
						AgendaItemOcorrenciaServices.TP_PUBLICO,
						new GregorianCalendar());
				
				Result res = AgendaItemOcorrenciaServices.save(ocorrencia, connect);
				retorno = res.getCode();
			}
			
			// NOTIFICAÇÃO =====================================================
			Result resultNotificacao = notificar(oldValue, item, connect);
			// =================================================================
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			Result result = new Result(item.getCdAgendaItem(), (retorno<0)?"Erro ao salvar...":"Salvo com sucesso...", "AGENDAITEM", item);
			
			if(rsmEventosWorkflow.size()>0){
				result.addObject("EVENTOS_WORKFLOW", rsmEventosWorkflow);
			}
			
			if(tipoPrazoWorkflow!=null) {
				result.addObject("TIPO_PRAZO_WORKFLOW", tipoPrazoWorkflow);
			}
			
			if(orgao!=null) {
				result.addObject("ORGAO", orgao);
			}
			
			result.addObject("RESULT_NOTIFICACAO", rEmail);
			
			if(resultWorkflow!=null && resultWorkflow.getCode()>0) {
				result.addObject("LG_ENVIA_EMAIL", resultWorkflow.getObjects().get("LG_ENVIA_EMAIL"));
				result.addObject("AUTOTEXTO", resultWorkflow.getObjects().get("AUTOTEXTO"));
				result.addObject("DESTINATARIOS", resultWorkflow.getObjects().get("DESTINATARIOS"));
				result.addObject("ARQUIVOS", resultWorkflow.getObjects().get("ARQUIVOS"));
				result.addObject("ASSINATURA", resultWorkflow.getObjects().get("ASSINATURA"));
				
				result.addObject("GPN_EVENTOS_WORKFLOW", resultWorkflow.getObjects().get("EVENTOS_WORKFLOW"));
				
				result.addObject("GPN_ANDAMENTOS_WORKFLOW", resultWorkflow.getObjects().get("ANDAMENTOS_WORKFLOW"));
			}
			
			//COMPLIANCE
			ComplianceManager.process("saveCompliance", item, authData, tpAcao, connect);
			
			//LOG
			LogServices.log(tpAcao, idAcao, authData, item, oldValue);
			
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
	
	private static Result enviarNotificacaoAgenda(AgendaItem item, int cdEmpresa, Connection connect) {
		try {
			int tpDestinatarioNotificacaoEmailAgenda = ParametroServices.getValorOfParametroAsInteger("TP_DESTINATARIO_NOTIFICACAO_EMAIL_AGENDA", -1, 0, connect);
			Result rEmail = null;
			
			Processo processo = ProcessoDAO.get(item.getCdProcesso(), connect);
			int cdProcesso = processo!=null ? processo.getCdProcesso() : 0;
			
			Mensagem mensagem = new Mensagem();
			mensagem.setCdUsuarioOrigem(item.getCdUsuario());
			mensagem.setCdProcesso(cdProcesso);
			
			int tpAgendaItem = -1;
			
			if(item.getCdTipoPrazo()>0) {
				TipoPrazo tipo = TipoPrazoDAO.get(item.getCdTipoPrazo(), connect);
				tpAgendaItem = tipo.getTpAgendaItem();
			}
			else {
				com.tivic.manager.ptc.TipoPrazo tipoD = com.tivic.manager.ptc.TipoPrazoDAO.get(item.getCdTipoPrazoDocumento(), connect);
				tpAgendaItem = tipoD.getTpAgendaItem();
			}
			
			Result rTextoEmail = getTxtEmailAgenda(cdProcesso, cdEmpresa, item.getCdAgendaItem(), tpAgendaItem, connect);
			
			if(rTextoEmail.getCode()<=0) {
				rEmail = new Result(-6, "Não é possível enviar email automático. Parâmetro do modelo de email não configurado.");
			}
							
			String source = new String((byte[])rTextoEmail.getObjects().get("BLB_DOCUMENTO"));
			
			Pessoa responsavel = null;
			
			if(tpAgendaItem == TipoPrazoServices.TP_DILIGENCIA) { //email para correspondente
				mensagem.setDsAssunto("[JurisManager] Encaminhamento do processo Nº " + processo.getNrProcesso());
				
				Orgao correspondente = OrgaoServices.getOrgaoByPessoa(item.getCdPessoa(), connect);
				responsavel = PessoaDAO.get(correspondente.getCdResponsavel(), connect);
				
				if(responsavel==null || (responsavel!=null && responsavel.getCdPessoa()==0)) { 
					//NÃO TROCAR CÓDIGO DE ERRO
					rEmail = new Result(-5, "Não é possível enviar mensagem. O correspondente não possui responsável cadastrado.");
				}
			}
			else { //email para responsável de outros tipos de agenda
				mensagem.setDsAssunto("[JurisManager] Novo(a) "+TipoPrazoServices.tiposAgendaItem[tpAgendaItem] + (processo!=null ? " no processo Nº " + processo.getNrProcesso() : ""));
				
				if(tpDestinatarioNotificacaoEmailAgenda == TP_DESTINATARIO_RESPONSAVEL_PROCESSO)
					responsavel = PessoaDAO.get(processo.getCdAdvogado(), connect);
				else if(tpDestinatarioNotificacaoEmailAgenda == TP_DESTINATARIO_RESPONSAVEL_GRUPO_TRABALHO) {
					Grupo grupo = GrupoDAO.get(processo.getCdGrupoTrabalho(), connect);
					if(grupo!=null)
						responsavel = PessoaDAO.get(grupo.getCdProprietario(), connect);
				}
				else
					responsavel = PessoaDAO.get(item.getCdPessoa(), connect);
			}
			
			if(responsavel==null || (responsavel!=null && responsavel.getCdPessoa()==0)) { 
				//NÃO TROCAR CÓDIGO DE ERRO
				rEmail = new Result(-5, "Não é possível enviar mensagem. Destinatário ("+
						(tpDestinatarioNotificacaoEmailAgenda == TP_DESTINATARIO_RESPONSAVEL_PROCESSO ?
								"Responsável pelo Processo" :
						tpDestinatarioNotificacaoEmailAgenda == TP_DESTINATARIO_RESPONSAVEL_GRUPO_TRABALHO ?
								"Responsável pelo Grupo de Trabalho" : 
								"Responsável pela Agenda")
						+") não encontrado.");
			}
			
			if(responsavel!=null && responsavel.getNmEmail()==null || responsavel.getNmEmail().equals("")) {
				rEmail = new Result(-5, "Não é possível enviar mensagem. Destinatário não possui email cadastrado.");
			}
			
			int indexInicial = source.indexOf("<p>");
			int indexFinal = source.indexOf("</p></TextFlow", indexInicial) + 4;
			source = source.substring(indexInicial, indexFinal);
			mensagem.setTxtMensagem(source);
			
			HashMap<String,Object> resp = new HashMap<String, Object>();
			resp.put("CD_PESSOA", responsavel.getCdPessoa());
			resp.put("NM_EMAIL", responsavel.getNmEmail());
			
			ArrayList<HashMap<String, Object>> destinatarios = new ArrayList<HashMap<String,Object>>();
			destinatarios.add(resp);
			
			/*
			 * Add a destinatários os outros participantes da agenda
			 */
//			ResultSetMap rsmParticipantes = getParticipantes(item.getCdAgendaItem(), connect);
//			while(rsmParticipantes.next()) {
//				HashMap<String,Object> part = new HashMap<String, Object>();
//				if(rsmParticipantes.getString("nm_email")!=null || !rsmParticipantes.getString("nm_email").equals("")) {
//					part.put("CD_PESSOA", rsmParticipantes.getInt("cd_pessoa"));
//					part.put("NM_EMAIL", rsmParticipantes.getString("nm_email"));
//					
//					destinatarios.add(part);
//				}
//			}
			
							
			Result r = MensagemServices.enviarMensagem(mensagem, destinatarios, null, null, connect);
			if(r.getCode()<=0) {
				rEmail = new Result(-4, "Erro ao enviar mensagem.");
			}
			else
				rEmail = new Result(1, "Email enviado com sucesso");
			
			return rEmail;
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-7, "Erro desconhecido ao enviar mensagem.");
		}
	}

	public static Result saveAll(ArrayList<AgendaItem> itens){
		return saveAll(itens, null, null);
	}
	
	public static Result saveAll(ArrayList<AgendaItem> itens, AuthData authData){
		return saveAll(itens, authData, null);
	}
	
	public static Result saveAll(ArrayList<AgendaItem> itens, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			for (AgendaItem agendaItem : itens) {
				//retorno = save(agendaItem, connect).getCode();
				retorno = save(agendaItem, authData, connect).getCode();
				if(retorno<=0)
					break;
			}
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<0)?"Erro ao salvar...":"Salvo com sucesso...");
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
	
	public static ResultSetMap find(int cdPessoa, GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return find(cdPessoa, dtInicial, dtFinal, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, null);
	}
	
	public static ResultSetMap find(int cdPessoa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int tpAgendaItem) {
		return find(cdPessoa, dtInicial, dtFinal, 0, 0, 0, 0, tpAgendaItem, 0, 0, -1, 0, 0, 0, null);
	}

	public static ResultSetMap findEventos(int cdPessoa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, 
			int cdTipoPrazo, int cdOrgao, int cdGrupoProcesso, int cdCliente, 
			int tpAgendaItem, int cdCidade, int cdEstado, int stAgendaItem, 
			int cdTribunal, int cdJuizo, int cdPessoaResponsavel){
		return find(cdPessoa, dtInicial, dtFinal, cdTipoPrazo, cdOrgao, cdGrupoProcesso, 
				cdCliente, tpAgendaItem, cdCidade, cdEstado, stAgendaItem, 
				cdTribunal, cdJuizo, cdPessoaResponsavel, null);
	}
			
	public static ResultSetMap find(int cdPessoa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, 
			int cdTipoPrazo, int cdOrgao, int cdGrupoProcesso, int cdCliente, 
			int tpAgendaItem, int cdCidade, int cdEstado, int stAgendaItem, 
			int cdTribunal, int cdJuizo, int cdPessoaResponsavel, Connection connect) {
		
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			boolean lgAtrasados = dtInicial==null && dtFinal==null;
			
			if(!lgAtrasados) {
				dtInicial.set(Calendar.HOUR_OF_DAY, 0);
				dtInicial.set(Calendar.MINUTE, 0);
				dtInicial.set(Calendar.SECOND, 0);
				
				
				dtFinal.set(Calendar.HOUR_OF_DAY, 23);
				dtFinal.set(Calendar.MINUTE, 59);
				dtFinal.set(Calendar.SECOND, 59);
			}
			
			pstmt = connect.prepareStatement("SELECT DISTINCT(CD_AGENDA_ITEM), A.*, " +
					" B.TP_AGENDA_ITEM, B.NM_TIPO_PRAZO, " +
					" B1.TP_AGENDA_ITEM as TP_AGENDA_ITEM_DOCUMENTO, B1.NM_TIPO_PRAZO as NM_TIPO_PRAZO_DOCUMENTO, " +
					" C.NM_PESSOA, I.NM_JUIZO, I.SG_JUIZO, J.NM_TRIBUNAL, K.CD_ORGAO, K.NM_ORGAO, D.VL_PROCESSO, L.NM_TIPO_PROCESSO, " +
					" D.NR_PROCESSO, D.NR_JUIZO, D.tp_autos, E.NM_CIDADE, E1.SG_ESTADO, " +
//					(Util.getConfManager().getIdOfDbUsed().equals("FB") ? 
//							"(SELECT nm_cliente FROM get_clientes(A.cd_processo)) AS nm_cliente, " : " '' as nm_cliente, ") +
//	                (Util.getConfManager().getIdOfDbUsed().equals("FB") ? 
//	                		"(SELECT nm_contra_parte FROM get_contra_parte(A.cd_processo)) AS nm_adverso, " : " '' as nm_adverso,") +
	                " H.nm_pessoa AS nm_advogado" + 
					" FROM agd_agenda_item A " +
					" LEFT OUTER JOIN prc_tipo_prazo B ON (A.cd_tipo_prazo = B.cd_tipo_prazo) " +
					" LEFT OUTER JOIN ptc_tipo_prazo B1 ON (A.cd_tipo_prazo_documento = B1.cd_tipo_prazo) " +
					" LEFT OUTER JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa)" +
					" LEFT OUTER JOIN prc_processo D ON (A.cd_processo = D.cd_processo) " +
					" LEFT OUTER JOIN ptc_documento D1 ON (A.cd_documento = D1.cd_documento) " +
					" LEFT OUTER JOIN grl_cidade E ON (D.cd_cidade = E.cd_cidade) " +
					" LEFT OUTER JOIN grl_estado E1 ON (E.cd_estado = E1.cd_estado) " +
					" LEFT OUTER JOIN prc_parte_cliente F ON (D.cd_processo = F.cd_processo) " +
					" LEFT OUTER JOIN grl_pessoa H ON (D.cd_advogado = H.cd_pessoa) "+
					" LEFT OUTER JOIN prc_juizo I ON (D.cd_juizo = I.cd_juizo) "+
	                " LEFT OUTER JOIN prc_tribunal J ON (D.cd_tribunal = J.cd_tribunal) "+
	                " LEFT OUTER JOIN prc_orgao K ON (K.cd_orgao = D.cd_orgao) "+
	                " LEFT OUTER JOIN prc_tipo_processo L ON (L.cd_tipo_processo = D.cd_tipo_processo) "+
	                " LEFT OUTER JOIN grl_pessoa M ON (M.cd_pessoa = A.cd_pessoa) "+
	                " WHERE 1=1 " +
	                (!lgAtrasados ? " AND A.dt_inicial >= ? AND A.dt_inicial <= ? " : "") +
	                (lgAtrasados ? " AND A.st_agenda_item <> 1 AND A.st_agenda_item <> 2 " : "") + 
					((cdPessoa>0)?" AND A.cd_pessoa = " + cdPessoa : "")+
					((cdTipoPrazo>0)?" AND A.cd_tipo_prazo = " + cdTipoPrazo : "")+
					((cdOrgao>0)?" AND D.cd_orgao = " + cdOrgao : "")+
					((cdGrupoProcesso>0)?" AND D.cd_grupo_processo = " + cdGrupoProcesso : "")+
					((cdCliente>0)?" AND F.cd_pessoa = " + cdCliente : "")+
					((tpAgendaItem>=0)?" AND B.tp_agenda_item = " + tpAgendaItem : "")+
					((cdCidade>0)?" AND D.cd_cidade = " + cdCidade : "")+
					((cdEstado>0)?" AND E.cd_estado = " + cdEstado : "")+
					((stAgendaItem>-1)?" AND A.st_agenda_item = " + stAgendaItem : "")+
					((cdTribunal>0)?" AND J.cd_tribunal = " + cdTribunal : "")+
					((cdJuizo>0)?" AND I.cd_juizo = " + cdJuizo : "")+
					((cdPessoaResponsavel>0)?" AND M.cd_pessoa = " + cdPessoaResponsavel : "")+
					" ORDER BY dt_inicial");
			
			
			if(!lgAtrasados) {
				pstmt.setTimestamp(1,new Timestamp(dtInicial.getTimeInMillis()));
				pstmt.setTimestamp(2,new Timestamp(dtFinal.getTimeInMillis()));
			}
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
						
			// Acrescentando informação dos clientes e adversos
			while(rsm.next()) {
				String nmCliente = "";
				String nmAdverso = "";
				
				if(rsm.getInt("CD_PROCESSO")>0) {
					nmCliente = ProcessoServices.getClientes(rsm.getInt("CD_PROCESSO"), connect);
					nmAdverso = ProcessoServices.getAdversos(rsm.getInt("CD_PROCESSO"), connect);
					
					rsm.setValueToField("NM_CLIENTE", nmCliente);
					rsm.setValueToField("NM_ADVERSO", nmAdverso);
				}
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemServices.find: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemServices.find: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ArrayList<AgendaItem> getAll() {
		return getAll(null);
	}

	public static ArrayList<AgendaItem> getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT cd_agenda_item FROM agd_agenda_item");
			
			ResultSet rs = pstmt.executeQuery();
			
			ArrayList<AgendaItem> itens = new ArrayList<AgendaItem>();
			AgendaItem c;
			while(rs.next()){
				c = AgendaItemDAO.get(rs.getInt("cd_agenda_item"), connect);
				itens.add(c);
			}
			rs.close();
			
			return itens;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getByDocumento(int cdDocumento) {
		return getByDocumento(cdDocumento, false, null);
	}
	
	public static ResultSetMap getByDocumento(int cdDocumento, boolean lgResponsavel) {
		return getByDocumento(cdDocumento, lgResponsavel, null);
	}

	public static ResultSetMap getByDocumento(int cdDocumento, boolean lgResponsavel, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_agenda_item A " +
											"LEFT OUTER JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa) " + 
											"WHERE A.cd_documento = " + cdDocumento);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemServices.getByDocumento: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemServices.getByDocumento: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static HashMap<String, Object> getAgendaRegister(int cdAgendaItem, int cdEmpresa) {
		return getAgendaRegister(cdAgendaItem, cdEmpresa, null);
	}

	public static HashMap<String, Object> getAgendaRegister(int cdAgendaItem, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();

		try {
			
			ArrayList<ItemComparator> crt = new ArrayList<>();
			crt.add(new ItemComparator("A.cd_agenda_item", Integer.toString(cdAgendaItem), ItemComparator.EQUAL, Types.INTEGER));
			crt.add(new ItemComparator("A.cd_empresa", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = getList(crt, connect);
			
//			ResultSetMap rsm = new ResultSetMap(connect
//					.prepareStatement("SELECT * FROM agd_agenda_item WHERE cd_agenda_item="+cdAgendaItem)
//					.executeQuery());
			
			if(rsm.next())
				return rsm.getRegister();
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.out.println("Erro! AgendaItemServices.getAgendaRegister: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca um modelo de documento de email
	 * 
	 * @param cdProcesso código do processo
	 * @param cdEmpresa código da empresa
	 * @param cdAgenda código da agenda
	 * @param tpAgendaItem tipo da agenda
	 * 
	 * @return Result com o objeto BLB_DOCUMENTO que corresponde ao email
	 * 
	 * @author Maurício
	 * @since 04/03/2015
	 */
	public static Result getTxtEmailAgenda(int cdProcesso, int cdEmpresa, int cdAgenda, int tpAgendaItem) {
		return getTxtEmailAgenda(cdProcesso, cdEmpresa, cdAgenda, tpAgendaItem, null);
	}
	
	public static Result getTxtEmailAgenda(int cdProcesso, int cdEmpresa, int cdAgenda, int tpAgendaItem, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			/*
			 * código do modelo de documento dependendo do tipo de agenda, 
			 * sendo um modelo para DILIGENCIA e outro para demais os tipos.
			 */
			int cdModelo = ParametroServices.getValorOfParametroAsInteger(tpAgendaItem==TipoPrazoServices.TP_DILIGENCIA ? 
											"CD_MODELO_EMAIL_DILIGENCIA_CORRESPONDENTE":"CD_MODELO_EMAIL_AGENDA", 0);
			
			if(cdModelo<=0)
				return new Result(-1, "Parametro do modelo de email nao encontrado");
			
			//Executa modelo com ou sem processo
			Result r = cdProcesso==0 ? 
					executeModeloWeb(cdModelo, cdAgenda, cdEmpresa, connect) : 
						ProcessoServices.executeModeloWeb(cdModelo, cdAgenda, cdProcesso, cdEmpresa, connect);
					
			if(r.getCode()<=0)
				return new Result(-2, "Erro ao executar modelo.");
			
			byte[] blbDocumento = (byte[])r.getObjects().get("BLB_DOCUMENTO");
			
			if(blbDocumento==null)
				return new Result(-2, "Text de modelo nao encontrado");
			
			return new Result(1, "Executado com sucesso", "BLB_DOCUMENTO", blbDocumento);
		}
		catch(Exception e){
			System.out.println("Erro! AgendaItemServices.getTxtEmailAgenda: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir Item!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Executa um modelo de documento
	 * 
	 * @param cdModelo Código do Modelo
	 * @param cdAgendaItem Código da Agenda
	 * @param cdEmpresa Código da Empresa
	 * 
	 * @return Result com objeto BLB_DOCUMENTO (modelo executado)
	 * 
	 * @author Maurício
	 * @since 05/06/2015
	 */
	public static Result executeModeloWeb(int cdModelo, int cdAgendaItem, int cdEmpresa) {
		return executeModeloWeb(cdModelo, cdAgendaItem, cdEmpresa, null);
	}

	public static Result executeModeloWeb(int cdModelo, int cdAgendaItem, int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			ModeloDocumento modelo = ModeloDocumentoDAO.get(cdModelo, connection);
			if(modelo==null)
				return new Result(-1, "Modelo indicado não existe.");
						
			if(EmpresaDAO.get(cdEmpresa, connection)==null)
				return new Result(-1, "Empresa indicada não existe.");
			
			String source = new String(modelo.getBlbConteudo(), "UTF-8");
			
			HashMap<String, String> fieldMap = getFieldsMap(cdAgendaItem, cdEmpresa, connection);
			for (Object key : fieldMap.keySet().toArray()) {
				
				String v = fieldMap.get(key) == null ? "" : fieldMap.get(key);
				String k = ((String)key).replaceAll("<", "&lt;").replaceAll(">", "&gt;");
				
				//System.out.println("key: "+k+", value: "+v);
				source = source.replaceAll(k, v);
			}
			
			return new Result(1, "Processo executado com sucesso.", "BLB_DOCUMENTO", source.getBytes());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemServices.executeModeloWeb: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Monta um HashMap com placeholder e valor para a execução de um modelo de documento
	 * 
	 * @param cdAgendaItem código da AgendaItem
	 * @param cdEmpresa código da empresa
	 * @param connect conexão com o BD
	 * 
	 * @return HashMap<String, String> placeholder e valor
	 * 
	 * @author Maurício
	 * @since 05/03/2015
	 */
	public static HashMap<String, String> getFieldsMap(int cdAgendaItem, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			HashMap<String, String> fieldMap = new HashMap<String, String>();
			
			/* EMPRESA */
			fieldMap.put("<#Url_portal>", ParametroServices.getValorOfParametroAsString("NM_URL_PORTAL", ""));
			
			/* GERAL */			
			fieldMap.put("<#Data_impressao>", Util.formatDate(new GregorianCalendar(), "dd 'de' MMMM 'de' yyyy"));
			
			PessoaEndereco endereco = PessoaEnderecoServices.getEnderecoPrincipal(cdEmpresa, connect);
			if(endereco!=null) {
				Cidade cidadeEndereco = CidadeDAO.get(endereco.getCdCidade(), connect);
				if(cidadeEndereco!=null)
					fieldMap.put("<#Cidade>", cidadeEndereco.getNmCidade());
			}
			
			/* AGENDA */
			if(cdAgendaItem>0) {
				AgendaItem agenda = AgendaItemDAO.get(cdAgendaItem, connect);
				Pessoa responsavel = PessoaDAO.get(agenda.getCdPessoa(), connect);

				fieldMap.put("<#cd_agenda>", Integer.toString(agenda.getCdAgendaItem()));
				fieldMap.put("<#Responsavel_agenda>", responsavel.getNmPessoa());
			}
			
			return fieldMap;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemServices.getFieldsMap: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdAgendaItem){
		return remove(cdAgendaItem, false, null);
	}
	
	public static Result remove(int cdAgendaItem, AuthData auth){
		return remove(cdAgendaItem, false, auth, null);
	}
	
	public static Result remove(int cdAgendaItem, boolean cascade){
		return remove(cdAgendaItem, cascade, null, null);
	}
	
	public static Result remove(int cdAgendaItem, boolean cascade, AuthData auth){
		return remove(cdAgendaItem, cascade, auth, null);
	}
	
	public static Result remove(int cdAgendaItem, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			AgendaItem item = AgendaItemDAO.get(cdAgendaItem, connect);
			
//			if(cascade){
//				retorno = TipoUsuarioServices.removeAllByProduto(cdAgendaItem, connect).getCode();
//				if(retorno>0)
//					retorno = ReleaseServices.removeAllByProduto(cdAgendaItem, connect).getCode();
//			}
			
			PreparedStatement ps = connect.prepareStatement("SELECT cd_agenda_item FROM agd_agenda_item WHERE cd_agenda_item_superior="+item.getCdAgendaItem());
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			while(rsm.next()) {
				if(AgendaItemDAO.delete(rsm.getInt("cd_agenda_item"), connect)<=0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-3, "Erro ao excluir agenda derivada");
				}
			}
				
			if(!cascade || retorno>0)
				retorno = AgendaItemDAO.delete(cdAgendaItem, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este item está vinculada a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			//COMPLIANCE
			ComplianceManager.process("saveCompliance", item, authData, ComplianceManager.TP_ACAO_DELETE, connect);
			
			//LOG
			if(authData!=null) {
				LogServices.log(LogServices.DELETE, authData.getIdAcaoDelete(), authData, item);
			}
			else {
				LogUtils.info("AgendaItemServices.remove: Não foi possível salvar log. AuthData é nulo.");
			}
			
			return new Result(1, "Item excluído com sucesso!");
		}
		catch(Exception e){
			System.out.println("Erro! AgendaItemServices.remove: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir Item!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Lista todos os usuários que podem ser responsáveis por uma agenda.
	 * 
	 * @param criterios
	 * @return ResultSetMap lista de responsáveis
	 */
	public static ResultSetMap findResponsavel(ArrayList<ItemComparator> criterios) {
		Connection connect = Conexao.conectar();
		String sql = null;
		
		String nmPessoa = null;
		String qtLimite = "";
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("A.NM_PESSOA")) {
				nmPessoa = Util.limparTexto(criterios.get(i).getValue());
				nmPessoa = "%"+nmPessoa.replaceAll(" ", "%")+"%";
				criterios.remove(i);
				i--;
			} else
			if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
				qtLimite = criterios.get(i).getValue();
			}
			else
				crt.add(criterios.get(i));
		}
		
		if(ParametroServices.getValorOfParametroAsInteger("LG_RESPONSAVEIS_VINCULO", 0)==0) {
			int cdVinculoAdvogado = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_ADVOGADO", 0);
			int cdVinculoColaborador = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_COLABORADOR", 0);
			int cdVinculoFuncionario = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_FUNCIONARIO", 0);
			
			sql = " SELECT DISTINCT(A.cd_pessoa), A.*, C.cd_usuario " +
					   " FROM grl_pessoa A " +
					   " JOIN grl_pessoa_empresa B ON (A.cd_pessoa = B.cd_pessoa AND " +
					   "                              (B.cd_vinculo = "+cdVinculoAdvogado+" OR " + 
					   "							   B.cd_vinculo = "+cdVinculoColaborador+" OR " + 
					   "							   B.cd_vinculo = "+cdVinculoFuncionario+"))"+
					   " JOIN seg_usuario C ON (A.cd_pessoa = C.cd_pessoa AND C.st_usuario = "+UsuarioServices.ST_ATIVO+")" +
					   " WHERE  1=1 "+
					   (nmPessoa!=null ? 
							   (Util.getConfManager().getIdOfDbUsed().equals("FB") ? (" AND A.nm_pessoa LIKE '"+nmPessoa+"' ") :  
								   (" AND TRANSLATE(A.nm_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '"+nmPessoa+"' "))
							:
								"");
		}
		else {
			ArrayList<Object> vinculos = ParametroServices.getValoresOfParametroAsArrayList("TP_RESPONSAVEIS_VINCULO", 0, connect);
			
			sql = " SELECT DISTINCT(A.cd_pessoa), A.*, C.cd_usuario " +
					   " FROM grl_pessoa A " +
					   " JOIN grl_pessoa_empresa B ON (A.cd_pessoa = B.cd_pessoa AND B.cd_vinculo IN ("+Util.join(vinculos)+"))"+
					   " JOIN seg_usuario C ON (A.cd_pessoa = C.cd_pessoa AND C.st_usuario = "+UsuarioServices.ST_ATIVO+")" +
					   " WHERE  1=1 "+
					   (nmPessoa!=null ? 
							   (Util.getConfManager().getIdOfDbUsed().equals("FB") ? (" AND A.nm_pessoa LIKE '"+nmPessoa+"' ") :  
								   (" AND TRANSLATE(A.nm_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '"+nmPessoa+"' "))
							:
								"");
			
		}
		
		return Search.find(sql, " ORDER BY nm_pessoa ", crt, connect, connect==null);
	}
	
	public static ResultSetMap getResponsaveis() {
		return findResponsavel(new ArrayList<ItemComparator>());
	}
	
	/**
	 * Buscar agendas de acordo com os critérios passados
	 * 
	 * @param criterios Critérios da busca
	 * @return ResultSetMap Lista com agendas encontradas
	 * 
	 * @author Maurício
	 * @since 09/10/2014
	 */
	
	public static ResultSetMap getListByParameters(int tpCompromisso, int cdResponsavel, int tpPeriodo, String dtInicial, String dtFinal, int cdEnvolvido){
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		if(tpCompromisso > 0) {
			criterios.add(new ItemComparator("C.TP_AGENDA_ITEM", String.valueOf(tpCompromisso), ItemComparator.EQUAL, Types.INTEGER));
		}
		
		if(cdResponsavel > 0) {
			criterios.add(new ItemComparator("A.CD_USUARIO", String.valueOf(cdResponsavel), ItemComparator.EQUAL, Types.INTEGER));
		}
		
		if(tpPeriodo > 0) {
			criterios.add(new ItemComparator("A.CD_TIPO_PRAZO", String.valueOf(tpPeriodo), ItemComparator.EQUAL, Types.INTEGER));
		}
		
		if(dtInicial != null) {
			criterios.add(new ItemComparator(tpPeriodo<=0?"A.DT_INICIAL":"A.DT_REALIZACAO", dtInicial + " 00:00:00", ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
		}
		
		if(dtFinal != null) {
			criterios.add(new ItemComparator(tpPeriodo<=0?"A.DT_INICIAL":"A.DT_REALIZACAO", dtFinal + " 23:59:59", ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
		}
		
		if(cdEnvolvido > 0) {
			criterios.add(new ItemComparator("F1.CD_PESSOA", String.valueOf(cdEnvolvido), ItemComparator.EQUAL, Types.INTEGER));
		}
		
		criterios.add(new ItemComparator("orderByField", "DT_INICIAL", ItemComparator.EQUAL, Types.INTEGER));		
		criterios.add(new ItemComparator("A.cd_empresa", String.valueOf(2), ItemComparator.EQUAL, Types.INTEGER));
		
		return getList(criterios, null);
	}
	
	public static ResultSetMap getList(ArrayList<ItemComparator> criterios){
		return getList(criterios, null);
	}
	
	public static ResultSetMap getList(ArrayList<ItemComparator> criterios, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
//			LogUtils.debug("AgendaItemServices.getList");
//			LogUtils.createTimer("AGENDA_LIST_TIMER");
			
			int tpModulo=-1;
			int cdEmpresa = 0;
			int cdPessoa = 0;
			int cdParticipante = 0;
			int lgOcorrencias = 0;			
			String orderByField = null;
			int lgFaturamento = 0;
			int nrRegistros = 0;
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("tpModulo")) {
					tpModulo = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("orderByField")) {
					orderByField = " ORDER BY " + criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("A.cd_empresa")) {
					cdEmpresa = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("A.cd_pessoa")) {
					cdPessoa = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdParticipante")) {
					cdParticipante = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgFaturamento")) {
					lgFaturamento = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgOcorrencias")) {
					lgOcorrencias = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nrRegistros")) {
					nrRegistros = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
			}
			
			ResultSetMap rsmGrupoPessoa = GrupoPessoaServices.getGrupos(cdPessoa, connect);
			
			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(nrRegistros, 0);
			
			String fields = " SELECT " +sqlLimit[0]
							+ " DISTINCT(A.cd_agenda_item), A.*, "
							+ " B.cd_processo, B.vl_processo, B.nr_processo, B.nr_juizo, B.tp_autos, B.NM_CONTEINER3, B.TP_INSTANCIA, B.tp_sentenca,"
							+ " C.cd_tipo_prazo, C.nm_tipo_prazo, C.tp_agenda_item,"
							+ " D.cd_pessoa, D.nm_pessoa, D.nm_pessoa AS nm_pessoa_responsavel,"
							+ " E.cd_usuario, E1.nm_pessoa AS nm_pessoa_cadastro,"
							+ " F.nr_documento, "
							+ " G.cd_tipo_prazo as cd_tipo_prazo_documento, G.nm_tipo_prazo as nm_tipo_prazo_documento,"
							+ "		G.tp_agenda_item as tp_agenda_item_documento, "
							+ "	O.nm_local,"
							+ " Q.nm_grupo, Q.nm_grupo as nm_grupo_trabalho,"
							+ " S.nm_orgao,"
							+ " W1.nm_pessoa AS nm_usuario_cumprimento,"
							+ " X.cd_grupo AS cd_grupo_trabalho_responsavel, X.nm_grupo AS nm_grupo_trabalho_responsavel, X.nm_email AS nm_email_grupo_trabalho ";
			
			String joins =  " FROM agd_agenda_item A"
					      + " LEFT OUTER JOIN prc_processo B ON (A.cd_processo = B.cd_processo)"
					      + " LEFT OUTER JOIN prc_tipo_prazo C ON (A.cd_tipo_prazo = C.cd_tipo_prazo)"
					      + " LEFT OUTER JOIN grl_pessoa D ON (A.cd_pessoa = D.cd_pessoa)"
					      + " LEFT OUTER JOIN seg_usuario E ON (A.cd_usuario = E.cd_usuario)"
					      + " LEFT OUTER JOIN grl_pessoa E1 ON (E.cd_pessoa = E1.cd_pessoa)"
					      + " LEFT OUTER JOIN ptc_documento F ON (A.cd_documento = F.cd_documento)"
					      + " LEFT OUTER JOIN ptc_documento_pessoa F1 ON (A.cd_documento = F1.cd_documento)"
					      + " LEFT OUTER JOIN ptc_tipo_prazo G ON (A.cd_tipo_prazo_documento = G.cd_tipo_prazo)"
						  + " LEFT OUTER JOIN agd_local O ON (A.cd_local = O.cd_local)"
						  + " LEFT OUTER JOIN agd_tipo_local P ON (O.cd_tipo_local = P.cd_tipo_local)"
						  + " LEFT OUTER JOIN agd_grupo Q ON (B.cd_grupo_trabalho = Q.cd_grupo)"
						  // + " LEFT OUTER JOIN agd_agenda_item_participante R ON (A.cd_agenda_item = R.cd_agenda_item AND R.cd_pessoa="+cdParticipante+")";
						  //+ (cdParticipante>0?" JOIN agd_agenda_item_participante R ON (A.cd_agenda_item = R.cd_agenda_item AND R.cd_pessoa="+cdParticipante+")":"")
						  + " LEFT OUTER JOIN prc_orgao S ON (B.cd_orgao = S.cd_orgao)"
						  + " LEFT OUTER JOIN seg_usuario W ON (A.cd_usuario_cumprimento = W.cd_usuario)"
						  + " LEFT OUTER JOIN grl_pessoa W1 ON (W.cd_pessoa = W1.cd_pessoa)"
						  + " LEFT OUTER JOIN agd_grupo X ON (A.cd_grupo_trabalho = X.cd_grupo)";
						
			switch(tpModulo) {
				case 0: //Processo
					fields+= " ,H.nm_cidade, H1.sg_estado,"
							+ " J.nm_pessoa as nm_advogado,"
							+ " K.nm_juizo, K.sg_juizo,"
							+ " L.nm_tribunal,"
							+ " M.cd_orgao AS cd_correspondente_responsavel, M.nm_orgao AS nm_correspondente_responsavel , M.tp_contratacao,"
							+ " N.nm_tipo_processo, T.nm_grupo_processo"
							+ (lgFaturamento==1?", U.vl_evento_financeiro, U1.nm_produto_servico ":"");
					
					joins+= " LEFT OUTER JOIN grl_cidade H ON (B.cd_cidade = H.cd_cidade)"
					      + " LEFT OUTER JOIN grl_estado H1 ON (H.cd_estado = H1.cd_estado)"
					      + " LEFT OUTER JOIN prc_parte_cliente I ON (B.cd_processo = I.cd_processo)"
					      + " LEFT OUTER JOIN grl_pessoa J ON (B.cd_advogado = J.cd_pessoa)"
					      + " LEFT OUTER JOIN prc_juizo K ON (B.cd_juizo = K.cd_juizo)"
					      + " LEFT OUTER JOIN prc_tribunal L ON (B.cd_tribunal = L.cd_tribunal)"
					      + " LEFT OUTER JOIN prc_orgao M ON (D.cd_pessoa = M.cd_pessoa)"
					      + " LEFT OUTER JOIN prc_tipo_processo N ON (N.cd_tipo_processo = B.cd_tipo_processo)"
					      + " LEFT OUTER JOIN prc_grupo_processo T ON (B.cd_grupo_processo = T.cd_grupo_processo)"
					      + (lgFaturamento==1?
					    		  " LEFT OUTER JOIN prc_processo_financeiro U ON (A.cd_agenda_item = U.cd_agenda_item)"+
					    		  " LEFT OUTER JOIN grl_produto_servico U1 ON (U.cd_produto_servico=U1.cd_produto_servico)":"");
					break;
				case 1: //Documento
					joins+= "";
					break;
				// case n ...
				default:
					joins+= " LEFT OUTER JOIN grl_cidade H ON (B.cd_cidade = H.cd_cidade)"
						      + " LEFT OUTER JOIN grl_estado H1 ON (H.cd_estado = H1.cd_estado)"
						      + " LEFT OUTER JOIN prc_parte_cliente I ON (B.cd_processo = I.cd_processo)"
						      + " LEFT OUTER JOIN grl_pessoa J ON (B.cd_advogado = J.cd_pessoa)"
						      + " LEFT OUTER JOIN prc_juizo K ON (B.cd_juizo = K.cd_juizo)"
						      + " LEFT OUTER JOIN prc_tribunal L ON (B.cd_tribunal = L.cd_tribunal)"
						      + " LEFT OUTER JOIN prc_orgao M ON (M.cd_orgao = B.cd_orgao)"
						      + " LEFT OUTER JOIN prc_tipo_processo N ON (N.cd_tipo_processo = B.cd_tipo_processo)";
					break;						
			}
			
			//Buscar agendas com o código da empresa preenchido ou null
			if(cdEmpresa > 0 ) {
				joins += " WHERE (A.cd_empresa = "+cdEmpresa + " OR A.cd_empresa is null) ";	
			}
//					+ (cdParticipante>0 ? 
//							" AND EXISTS ("
//							+ " SELECT * FROM agd_agenda_item_participante "
//							+ " WHERE cd_pessoa="+cdParticipante
//							+ " AND cd_agenda_item=A.cd_agenda_item) " : "");
			
			//RESPONSÁVEIS
			if(cdPessoa>0 && (rsmGrupoPessoa==null || rsmGrupoPessoa.getLines().size()<=0)) {
				joins += " AND A.cd_pessoa = "+cdPessoa;
			}
			else if(cdPessoa>0 && (rsmGrupoPessoa!=null && rsmGrupoPessoa.getLines().size()>0)) {
				joins += "AND ("
						+ " A.CD_PESSOA = "+cdPessoa
						+ " OR "
						+ " A.CD_GRUPO_TRABALHO IN ("+Util.join(rsmGrupoPessoa, "CD_GRUPO", true)+")"
						+ ")";
			}
			
			
			String sql = fields+joins;
			orderByField = orderByField!=null ? orderByField : " ";
			
			ResultSetMap rsm = Search.find(sql, orderByField+sqlLimit[1], criterios, connect, isConnectionNull);
			
			/**
			 * INJECAO DE DADOS ADICIONAIS NO RESULTADO
			 */
//			LogUtils.logTimer("AGENDA_LIST_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
//			LogUtils.debug("AgendaItemServices.find: Iniciando injeção de dados adicionais...");
			
			boolean lgAtendimento = ParametroServices.getValorOfParametroAsInteger("LG_MODULO_ATENDIMENTO", 0)>0;
			while(rsm.next()) {
				
				if(rsm.getInt("CD_PESSOA", 0)>0) {
					rsm.setValueToField("CD_RESPONSAVEL", rsm.getInt("CD_PESSOA"));
					rsm.setValueToField("NM_RESPONSAVEL", rsm.getString("NM_PESSOA"));
					rsm.setValueToField("NM_EMAIL", rsm.getString("NM_EMAIL"));
				}
				else {
					rsm.setValueToField("CD_RESPONSAVEL", rsm.getInt("CD_GRUPO_TRABALHO_RESPONSAVEL"));
					rsm.setValueToField("NM_RESPONSAVEL", rsm.getString("NM_GRUPO_TRABALHO_RESPONSAVEL"));
					rsm.setValueToField("NM_EMAIL", rsm.getString("NM_EMAIL_GRUPO_TRABALHO"));
				}
				
				ResultSetMap rsmSentenca = ProcessoSentencaServices.getAllByProcesso(rsm.getInt("CD_PROCESSO"));
				if(rsmSentenca.next()) {
					rsm.setValueToField("cd_sentenca", rsmSentenca.getInt("cd_sentenca"));
					rsm.setValueToField("tp_sentenca", rsmSentenca.getInt("tp_sentenca"));
					rsm.setValueToField("dt_sentenca", rsmSentenca.getGregorianCalendar("dt_sentenca"));
					rsm.setValueToField("vl_sentenca", rsmSentenca.getDouble("vl_sentenca"));
					rsm.setValueToField("vl_acordo", rsmSentenca.getDouble("vl_acordo"));
				}
				
				String nmAdverso = "";
				String nmCliente = "";
				String nmEnvolvidos = "";
				if(rsm.getInt("CD_PROCESSO")>0) {
					nmAdverso = ProcessoServices.getAdversos(rsm.getInt("CD_PROCESSO"));
					nmCliente = ProcessoServices.getClientes(rsm.getInt("CD_PROCESSO"));
				}
				else if(rsm.getInt("CD_DOCUMENTO")>0) {
					if(lgAtendimento) {
						nmAdverso = DocumentoServices.getReclamados(rsm.getInt("CD_DOCUMENTO"));
						nmCliente = DocumentoServices.getReclamantes(rsm.getInt("CD_DOCUMENTO"));	
					}
					
					Result r = DocumentoServices.getSolicitantes(rsm.getInt("CD_DOCUMENTO"));
					nmEnvolvidos = r != null ? (String)r.getObjects().get("nmSolicitantes") : "";
				}
				
				rsm.setValueToField("NM_CLIENTE", nmCliente);
				rsm.setValueToField("NM_ADVERSO", nmAdverso);
				rsm.setValueToField("NM_ENVOLVIDOS", nmEnvolvidos);
				
				if(lgOcorrencias>0) {
					rsm.setValueToField("RSM_OCORRENCIA", AgendaItemOcorrenciaServices.getAllByAgenda(rsm.getInt("CD_AGENDA_ITEM")));
				}
			}
			rsm.beforeFirst();
			
//			LogUtils.logTimer("AGENDA_LIST_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
//			LogUtils.destroyTimer("AGENDA_LIST_TIMER");
			
			return rsm;
		}
		catch(Exception e){
			System.out.println("Erro! AgendaItemServices.getList: " +  e);
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getListMobilidade(ArrayList<ItemComparator> criterios){
		return getList(criterios, null);
	}
	
	public static ResultSetMap getListMobilidade(ArrayList<ItemComparator> criterios, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			int cdEmpresa = 0;
			int cdPessoa = 0;
			int lgOcorrencias = 0;			
			String orderByField = null;
			int nrRegistros = 0;
			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {

				if (criterios.get(i).getColumn().equalsIgnoreCase("orderByField")) {
					orderByField = " ORDER BY " + criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("A.cd_empresa")) {
					cdEmpresa = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("A.cd_pessoa")) {
					cdPessoa = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgOcorrencias")) {
					lgOcorrencias = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nrRegistros")) {
					nrRegistros = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
			}
			
			ResultSetMap rsmGrupoPessoa = GrupoPessoaServices.getGrupos(cdPessoa, connect);
			
			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(nrRegistros, 0);
			
			String fields = "SELECT *";
			
			String joins =  " FROM agd_agenda_item A"
								+ " LEFT OUTER JOIN grl_pessoa D ON (A.cd_pessoa = D.cd_pessoa)"
								+ " LEFT OUTER JOIN seg_usuario E ON (A.cd_usuario = E.cd_usuario)"
								+ " LEFT OUTER JOIN grl_pessoa E1 ON (E.cd_pessoa = E1.cd_pessoa)"
								+ " LEFT OUTER JOIN ptc_documento F ON (A.cd_documento = F.cd_documento)"
								+ " LEFT OUTER JOIN ptc_documento_pessoa F1 ON (A.cd_documento = F1.cd_documento)"
								+ " LEFT OUTER JOIN ptc_tipo_prazo G ON (A.cd_tipo_prazo_documento = G.cd_tipo_prazo)"
								+ " LEFT OUTER JOIN agd_local O ON (A.cd_local = O.cd_local)"
								+ " LEFT OUTER JOIN agd_tipo_local P ON (O.cd_tipo_local = P.cd_tipo_local)"
								
								+ " LEFT OUTER JOIN mob_concessao Q ON (A.cd_pessoa = Q.cd_concessionario)";
						
						
			//Buscar agendas com o codigo da empresa preenchido ou null
			joins += " WHERE (A.cd_empresa = "+ cdEmpresa + " OR A.cd_empresa is null) ";

			
			//RESPONSÁVEIS
			if(cdPessoa>0 && (rsmGrupoPessoa==null || rsmGrupoPessoa.getLines().size()<=0)) {
				joins += " AND A.cd_pessoa = "+ cdPessoa;
			}
			else if(cdPessoa>0 && (rsmGrupoPessoa!=null && rsmGrupoPessoa.getLines().size()>0)) {
				joins += "AND ("
						+ " A.CD_PESSOA = "+cdPessoa
						+ " OR "
						+ " A.CD_GRUPO_TRABALHO IN ("+Util.join(rsmGrupoPessoa, "CD_GRUPO", true)+")"
						+ ")";
			}
			
			
			String sql = fields+joins;
			orderByField = orderByField!=null ? orderByField : " ";
			
			LogUtils.debug("SQL:\n"+Search.getStatementSQL(sql, orderByField+sqlLimit[1], criterios, true));
			
			ResultSetMap rsm = Search.find(sql, orderByField+sqlLimit[1], criterios, connect, isConnectionNull);
			
			/**
			 * INJECAO DE DADOS ADICIONAIS NO RESULTADO
			 */
//			LogUtils.logTimer("AGENDA_LIST_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
//			LogUtils.debug("AgendaItemServices.find: Iniciando injeção de dados adicionais...");
			
			//boolean lgAtendimento = ParametroServices.getValorOfParametroAsInteger("LG_MODULO_ATENDIMENTO", 0)>0;
			while(rsm.next()) {
				
				if(rsm.getInt("CD_PESSOA", 0)>0) {
					rsm.setValueToField("CD_RESPONSAVEL", rsm.getInt("CD_PESSOA"));
					rsm.setValueToField("NM_RESPONSAVEL", rsm.getString("NM_PESSOA"));
					rsm.setValueToField("NM_EMAIL", rsm.getString("NM_EMAIL"));
				}
				else {
					rsm.setValueToField("CD_RESPONSAVEL", rsm.getInt("CD_GRUPO_TRABALHO_RESPONSAVEL"));
					rsm.setValueToField("NM_RESPONSAVEL", rsm.getString("NM_GRUPO_TRABALHO_RESPONSAVEL"));
					rsm.setValueToField("NM_EMAIL", rsm.getString("NM_EMAIL_GRUPO_TRABALHO"));
				}
				
				String nmAdverso = "";
				String nmCliente = "";
				String nmEnvolvidos = "";
				
				if(rsm.getInt("CD_DOCUMENTO")>0) {
					//if(lgAtendimento) {
						nmAdverso = DocumentoServices.getReclamados(rsm.getInt("CD_DOCUMENTO"));
						nmCliente = DocumentoServices.getReclamantes(rsm.getInt("CD_DOCUMENTO"));	
					//}
					
					Result r = DocumentoServices.getSolicitantes(rsm.getInt("CD_DOCUMENTO"));
					nmEnvolvidos = r != null ? (String)r.getObjects().get("nmSolicitantes") : "";
				}
				
				rsm.setValueToField("NM_CLIENTE", nmCliente);
				rsm.setValueToField("NM_ADVERSO", nmAdverso);
				rsm.setValueToField("NM_ENVOLVIDOS", nmEnvolvidos);
				
				if(lgOcorrencias>0) {
					rsm.setValueToField("RSM_OCORRENCIA", AgendaItemOcorrenciaServices.getAllByAgenda(rsm.getInt("CD_AGENDA_ITEM")));
				}
			}
			rsm.beforeFirst();
			
//			LogUtils.logTimer("AGENDA_LIST_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
//			LogUtils.destroyTimer("AGENDA_LIST_TIMER");
			
			return rsm;
		}
		catch(Exception e){
			System.out.println("Erro! AgendaItemServices.getListMobilidade: " +  e);
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca resultados de uma agenda e os agrupa como solicitado na assinatura do método
	 * @author Edgard Hufelande
	 * @since  2015-02-26
	 * 
	 * @param cdAgenda   Um arraylist contendo todas as agendas na qual serão agrupadas
	 * @param tpGrafico  Tipo de agrupamento:
	 *                   0: Agrupado por Cidades
	 *                   1: *            Clientes
	 *                   2: *            Adverso
	 *                   3: *            Correspondente
	 *                    
	 * @return ResultSetMap
	 */
	public static ResultSetMap getAgendaAgrupada (ArrayList<Integer> cdAgenda, int tpGrafico) {
		return getAgendaAgrupada(cdAgenda, tpGrafico, null);
	}
	public static ResultSetMap getAgendaAgrupada(ArrayList<Integer> cdAgenda, int tpGrafico, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {	
			
			PreparedStatement pStmt;
						
			String listAgenda = new String();
			
			for(int i : cdAgenda){
				/* Transformando a array em string */
				listAgenda += i + ", ";
			}
			
			String sql = " SELECT " +
					(tpGrafico == 0 ? " H.NM_CIDADE || ' - ' || H1.sg_estado AS NM_GRAFICO_LABEL, " : "") +
					(tpGrafico == 1 ? " I1.NM_PESSOA AS NM_GRAFICO_LABEL, " : "") +
					(tpGrafico == 2 ? " J.NM_PESSOA AS NM_GRAFICO_LABEL, " : "") +
					(tpGrafico == 3 ? " M1.NM_PESSOA AS NM_GRAFICO_LABEL, " : "") +
					" (SELECT COUNT(*) FROM agd_agenda_item) AS QT_TOTAL_AGENDA, " +
					" COUNT(*) AS QT_AGENDA " +
					" FROM AGD_AGENDA_ITEM A " +
					" LEFT OUTER JOIN PRC_PROCESSO B ON (A.CD_PROCESSO = B.CD_PROCESSO) " +
					" LEFT OUTER JOIN GRL_CIDADE H ON (B.CD_CIDADE = H.CD_CIDADE) " +
					" LEFT OUTER JOIN GRL_ESTADO H1 ON (H.CD_ESTADO = H1.CD_ESTADO) " +
					" LEFT OUTER JOIN PRC_PARTE_CLIENTE I ON (B.CD_PROCESSO = I.CD_PROCESSO) " +
					" LEFT OUTER JOIN GRL_PESSOA I1 ON (I.CD_PESSOA = I1.CD_PESSOA) " +
					" LEFT OUTER JOIN GRL_PESSOA J ON (B.cd_advogado = J.cd_pessoa) " +
					" LEFT OUTER JOIN PRC_ORGAO M ON (M.cd_orgao = B.cd_orgao) " +
					" LEFT OUTER JOIN GRL_PESSOA M1 ON (M.CD_PESSOA = M1.CD_PESSOA) " +
					" WHERE A.CD_AGENDA_ITEM IN ( " + listAgenda.substring(0, listAgenda.length()-2) + " ) " +
					" GROUP BY NM_GRAFICO_LABEL ";
					
			pStmt = connect.prepareStatement(sql);
			
			ResultSetMap rsm = new ResultSetMap(pStmt.executeQuery());
			
			rsm.beforeFirst();
			
			while(rsm.next()){
				rsm.setValueToField("NM_GRAFICO_LABEL", 
						rsm.getString("NM_GRAFICO_LABEL") == null ? "Sem processos" : rsm.getString("NM_GRAFICO_LABEL")
				);
			}
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemServices.getAgendaAgrupada: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap getParticipantes(int cdAgendaItem)	{
		return getParticipantes(cdAgendaItem, null);
	}
	
	public static ResultSetMap getParticipantes(int cdAgendaItem, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_agenda_item", Integer.toString(cdAgendaItem), ItemComparator.EQUAL, Types.INTEGER));
			
			return AgendaItemParticipanteServices.find(criterios, connect);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result aplicarRegrasPtcWorkflow(ResultSetMap rsmRegras, AgendaItem itemAnterior, AgendaItem itemAtual){
		return aplicarRegrasPtcWorkflow(rsmRegras, itemAnterior, itemAtual, null);
	}
	
	public static Result aplicarRegrasPtcWorkflow(ResultSetMap rsmRegras, AgendaItem itemAnterior, AgendaItem itemAtual, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			while(rsmRegras.next()) {
				int tpGatilho = rsmRegras.getInt("TP_GATILHO");
				int tpAcao = rsmRegras.getInt("TP_ACAO");
				Documento doc = DocumentoDAO.get(itemAtual.getCdDocumento(), connect);
				
				switch (tpGatilho) {
					case WorkflowGatilhoServices.TP_GATILHO_LANCAMENTO_AGENDA:
						if(itemAnterior==null) { //AO LANÇAR NOVA AGENDA
							switch(tpAcao) {
								case WorkflowAcaoServices.TP_ACAO_TRAMITAR:
									DocumentoTramitacao tramitacao = new DocumentoTramitacao(0, doc.getCdDocumento(), rsmRegras.getInt("CD_SETOR"), 
											0, doc.getCdSetorAtual(), itemAtual.getCdUsuario(), new GregorianCalendar(),
											null, null, "Tramitação gerada via workflow.", 0, 0, null, null);
									
									retorno = DocumentoTramitacaoServices.save(tramitacao, connect).getCode();
									break;
									
								case WorkflowAcaoServices.TP_ACAO_MUDAR_FASE :
									doc.setCdFase(rsmRegras.getInt("CD_FASE"));
									retorno = DocumentoServices.update(doc, null, connect, null).getCode();
									break;
								
								case WorkflowAcaoServices.TP_ACAO_LANCAR_OCORRENCIA:
									com.tivic.manager.ptc.TipoPrazo tipoPrazo = com.tivic.manager.ptc.TipoPrazoDAO.get(itemAtual.getCdTipoPrazoDocumento(), connect);
									Pessoa resp = PessoaDAO.get(itemAtual.getCdPessoa(), connect);
									
									DocumentoOcorrencia ocorrencia = new DocumentoOcorrencia(
											itemAtual.getCdDocumento(), 
											rsmRegras.getInt("cd_tipo_ocorrencia"), 
											0, //cdOcorrencia, 
											itemAtual.getCdUsuario(), //cdUsuario, 
											new GregorianCalendar(), //dtOcorrencia, 
											tipoPrazo.getNmTipoPrazo()+" lançado: "+Util.formatDate(itemAtual.getDtLancamento(), "dd/MM/yyyy")+"\n"
											+ " Responsável: "+resp.getNmPessoa(),//txtOcorrencia, 
											DocumentoOcorrenciaServices.TP_VISIBILIDADE_PUBLICO, 0);
									
									retorno = DocumentoOcorrenciaDAO.insert(ocorrencia, connect);
									break;
									
								case WorkflowAcaoServices.TP_ACAO_LANCAR_PENDENCIA:
									TipoPendencia tipoPendencia = TipoPendenciaDAO.get(rsmRegras.getInt("CD_TIPO_PENDENCIA"), connect);
									
									DocumentoPendencia pendencia = new DocumentoPendencia(doc.getCdDocumento(), 
											rsmRegras.getInt("CD_TIPO_PENDENCIA"), new GregorianCalendar(), null, 
											tipoPendencia.getNmTipoPendencia()+" [lançado pelo workflow]", 0, 0, null);
									retorno = DocumentoPendenciaDAO.insert(pendencia, connect);
									break;
							}
						}
						
						break;
						
					case WorkflowGatilhoServices.TP_GATILHO_CUMPRIMENTO_AGENDA:
						if(itemAtual.getStAgendaItem()==ST_AGENDA_CUMPRIDO && itemAnterior.getStAgendaItem()!=ST_AGENDA_CUMPRIDO) {
							switch(tpAcao) {
								case WorkflowAcaoServices.TP_ACAO_TRAMITAR:
									DocumentoTramitacao tramitacao = new DocumentoTramitacao(0, doc.getCdDocumento(), rsmRegras.getInt("CD_SETOR"), 
											0, doc.getCdSetorAtual(), itemAtual.getCdUsuario(), new GregorianCalendar(),
											null, null, "Tramitação gerada via workflow.", 0, 0, null, null);
									
									retorno = DocumentoTramitacaoServices.save(tramitacao, connect).getCode();
									break;
									
								case WorkflowAcaoServices.TP_ACAO_MUDAR_FASE :
									doc.setCdFase(rsmRegras.getInt("CD_FASE"));
									retorno = DocumentoServices.update(doc, null, connect, null).getCode();
									break;
								
								case WorkflowAcaoServices.TP_ACAO_LANCAR_OCORRENCIA:
									com.tivic.manager.ptc.TipoPrazo tipoPrazo = com.tivic.manager.ptc.TipoPrazoDAO.get(itemAtual.getCdTipoPrazoDocumento(), connect);
									Pessoa resp = PessoaDAO.get(itemAtual.getCdPessoa(), connect);
									
									DocumentoOcorrencia ocorrencia = new DocumentoOcorrencia(
											itemAtual.getCdDocumento(), 
											rsmRegras.getInt("cd_tipo_ocorrencia"), 
											0, //cdOcorrencia, 
											itemAtual.getCdUsuario(), //cdUsuario, 
											new GregorianCalendar(), //dtOcorrencia, 
											tipoPrazo.getNmTipoPrazo()+" cumprido: "+Util.formatDate(itemAtual.getDtRealizacao(), "dd/MM/yyyy")+"\n"
											+ " Responsável: "+resp.getNmPessoa(),//txtOcorrencia, 
											DocumentoOcorrenciaServices.TP_VISIBILIDADE_PUBLICO, 0);
									
									retorno = DocumentoOcorrenciaServices.save(ocorrencia, connect).getCode();
									break;
									
								case WorkflowAcaoServices.TP_ACAO_LANCAR_PENDENCIA:
									TipoPendencia tipoPendencia = TipoPendenciaDAO.get(rsmRegras.getInt("CD_TIPO_PENDENCIA"), connect);
									
									DocumentoPendencia pendencia = new DocumentoPendencia(doc.getCdDocumento(), 
											rsmRegras.getInt("CD_TIPO_PENDENCIA"), new GregorianCalendar(), null, 
											tipoPendencia.getNmTipoPendencia()+" [lançado pelo workflow]", 0, 0, null);
									retorno = DocumentoPendenciaDAO.insert(pendencia, connect);
									break;	
							}
						}
		
						break;
					case WorkflowGatilhoServices.TP_GATILHO_MUDANCA_CAMPO:
		
						break;
	
					default:
						break;
				}
				
			}
						
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<0)?"Erro ao aplicar...":"Aplicado com sucesso...");
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
	
	public static Result aplicarRegrasGpnWorkflow(ResultSetMap rsmRegras, AgendaItem itemAnterior, AgendaItem itemAtual, AuthData auth){
		return aplicarRegrasGpnWorkflow(rsmRegras, itemAnterior, itemAtual, auth, null);
	}
	
	public static Result aplicarRegrasGpnWorkflow(ResultSetMap rsmRegras, AgendaItem itemAnterior, AgendaItem itemAtual, AuthData auth, Connection connect){
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
			TipoPrazo tipoPrazoItemAtual = TipoPrazoDAO.get(itemAtual.getCdTipoPrazo(), connect);
			
			while(rsmRegras.next()) {
				ResultSetMap rsmAcao = (ResultSetMap)rsmRegras.getObject("RSMACAO");	
				
				if(rsmRegras.getInt("TP_AGENDA_ITEM", -1) >= 0) {
					if(rsmRegras.getInt("TP_AGENDA_ITEM") != tipoPrazoItemAtual.getTpAgendaItem())
						continue;
				}
				
				if(rsmRegras.getInt("CD_TIPO_PRAZO", 0) > 0) {
					if(rsmRegras.getInt("CD_TIPO_PRAZO") != itemAtual.getCdTipoPrazo())
						continue;
				}
				
				while(rsmAcao.next()) {
					switch(rsmAcao.getInt("tp_acao")) {
						case com.tivic.manager.gpn.WorkflowAcaoServices.TP_ACAO_ENVIAR_EMAIL: {
							int cdModeloEmail = ParametroServices
									.getValorOfParametroAsInteger(
											(tipoPrazoItemAtual.getTpAgendaItem()==TipoPrazoServices.TP_DILIGENCIA ? 
													"CD_MODELO_EMAIL_DILIGENCIA_CORRESPONDENTE" : 
													"CD_MODELO_EMAIL_AGENDA"), 0, 0, connect);
							
							result = ProcessoServices.getDadosEmail(itemAtual.getCdProcesso(), connect);
							if(result.getCode()<=0) {
								return result;
							}

							Result rAux = ProcessoServices.executeModeloWeb(cdModeloEmail, itemAtual.getCdAgendaItem(), itemAtual.getCdProcesso(), 0, connect);
 							if(rAux.getCode()>0) {
								result.addObject("LG_ENVIA_EMAIL", 1);
								String txtAutotexto = (String)rAux.getObjects().get("AUTOTEXTO");
								result.addObject("AUTOTEXTO", txtAutotexto);
//								LogUtils.info("autotexto: "+txtAutotexto);
							}
							else {
								LogUtils.debug("Erro ao executar autotexto");
							}
							
							break;
						}
						case com.tivic.manager.gpn.WorkflowAcaoServices.TP_ACAO_LANCAR_AGENDA: {
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
									case com.tivic.manager.gpn.WorkflowAcaoServices.TP_RESPONSAVEL_ADV:
										agenda.setCdPessoa(processo.getCdAdvogado());										
										break;
									case com.tivic.manager.gpn.WorkflowAcaoServices.TP_RESPONSAVEL_GRUPO:
										agenda.setCdGrupoTrabalho(processo.getCdGrupoTrabalho());
										break;
									case com.tivic.manager.gpn.WorkflowAcaoServices.TP_RESPONSAVEL_USUARIO:
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
						case com.tivic.manager.gpn.WorkflowAcaoServices.TP_ACAO_LANCAR_ANDAMENTO: {
							
							ProcessoAndamento andamento = new ProcessoAndamento();
							andamento.setCdProcesso(itemAtual.getCdProcesso());
							andamento.setCdTipoAndamento(rsmAcao.getInt("cd_tipo_andamento"));
							andamento.setCdUsuario(auth.getUsuario().getCdUsuario());
							andamento.setDtLancamento(new GregorianCalendar());
							andamento.setDtAndamento(new GregorianCalendar());
							andamento.setStAndamento(ProcessoAndamentoServices.ST_ANDAMENTO_ATIVO);
							andamento.setTpVisibilidade(ProcessoAndamentoServices.TP_VISIBILIDADE_PUBLICO);
							andamento.setTxtAndamento(Util.formatDate(itemAtual.getDtInicial(), "dd/MM/yyyy HH:mm")+" \"" + tipoPrazoItemAtual.getNmTipoPrazo().toUpperCase()+"\"");
							//TODO: origem WORKFLOW
							andamento.setTpOrigem(-1);
							
							if(rsmAcao.getInt("lg_sugestao")==0) {
								result = ProcessoAndamentoServices.save(andamento, null, null, null, auth, connect);
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
								register.put("TXT_ANDAMENTO", andamento.getTxtAndamento());
								register.put("CD_TIPO_DOCUMENTO_CUMPRIMENTO", rsmAcao.getInt("CD_TIPO_DOCUMENTO"));
								
								rsmAndamentosWorkflow.addRegister(register);
							}
							
							break;
						}
						case com.tivic.manager.gpn.WorkflowAcaoServices.TP_ACAO_LANCAR_DESPESA: {
							break;
						}
						case com.tivic.manager.gpn.WorkflowAcaoServices.TP_ACAO_LANCAR_RECEITA: {
							break;
						}
						case com.tivic.manager.gpn.WorkflowAcaoServices.TP_ACAO_MUDAR_FASE: {
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
	
	public static ResultSetMap getEstatisticaDashboard(int cdUsuario) {
		return getEstatisticaDashboard(cdUsuario, null);
	}
	
	public static ResultSetMap getEstatisticaDashboard(int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(true);
			}
						
			String sql =  "SELECT  COUNT(*), B.TP_AGENDA_ITEM, "
						+ "(SELECT COUNT(*) from agd_agenda_item SA WHERE SA.st_agenda_item = 0 AND SA.cd_usuario = ?) as ABERTOS, "
						+ "(SELECT COUNT(*) from agd_agenda_item SA WHERE SA.dt_inicial < CURRENT_TIMESTAMP AND SA.st_agenda_item = 0 AND SA.cd_usuario = ?) as ATRASADOS "
						+ "FROM agd_agenda_item A, ptc_tipo_prazo B WHERE A.cd_tipo_prazo_documento = B.cd_tipo_prazo "
						+ "AND A.cd_usuario = ?"
						+ "GROUP BY B.TP_AGENDA_ITEM ";	
			
			PreparedStatement pstmt = connect.prepareStatement(sql);	
			pstmt.setInt(1, cdUsuario);
			pstmt.setInt(2, cdUsuario);
			pstmt.setInt(3, cdUsuario);			
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result getAgendasPendentes() {
		return getAgendasPendentes(0, 0, 0, 0, null, 0, null);
	}
	
	public static Result getAgendasPendentes(int lgAlerta) {
		return getAgendasPendentes(0, 0, 0, 0, null, lgAlerta, null);
	}
	
	public static Result getAgendasPendentes(int tpAgendaItem, int cdTipoPrazo, int cdCliente, int cdGrupoTrabalho, GregorianCalendar dtInicial) {
		return getAgendasPendentes(tpAgendaItem, cdTipoPrazo, cdCliente, cdGrupoTrabalho, dtInicial, 0, null);
	}
	
	public static Result getAgendasPendentes(int tpAgendaItem, int cdTipoPrazo, int cdCliente, int cdGrupoTrabalho, GregorianCalendar dtInicial, int lgAlerta) {
		return getAgendasPendentes(tpAgendaItem, cdTipoPrazo, cdCliente, cdGrupoTrabalho, dtInicial, lgAlerta, null);
	}
	
	public static Result getAgendasPendentes(int tpAgendaItem, int cdTipoPrazo, int cdCliente, int cdGrupoTrabalho, GregorianCalendar dtInicial, int lgAlerta, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(true);
			}

			System.out.println("AgendaItemServices.getAgendasPendentes");
			Result result = new Result(1);
						
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.ST_AGENDA_ITEM", ST_AGENDA_PENDENTE+","+ST_AGENDA_A_CUMPRIR, ItemComparator.IN, Types.INTEGER));
			
			criterios.add(new ItemComparator("A.DT_FINAL", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(new GregorianCalendar().getTimeInMillis())), ItemComparator.MINOR, Types.TIMESTAMP));
			criterios.add(new ItemComparator("B.ST_PROCESSO", ProcessoServices.ST_PROCESSO_ATIVO+"", ItemComparator.IN, Types.INTEGER));
			criterios.add(new ItemComparator("nrRegistros", "100", ItemComparator.EQUAL, Types.VARCHAR));
			criterios.add(new ItemComparator("orderByField", "A.DT_FINAL DESC", ItemComparator.EQUAL, Types.VARCHAR));
			if(tpAgendaItem>0)
				criterios.add(new ItemComparator("C.TP_AGENDA_ITEM", tpAgendaItem+"", ItemComparator.EQUAL, Types.INTEGER));
			if(cdTipoPrazo>0)
				criterios.add(new ItemComparator("A.CD_TIPO_PRAZO", cdTipoPrazo+"", ItemComparator.EQUAL, Types.INTEGER));
			if(cdCliente>0)
				criterios.add(new ItemComparator("I.CD_PESSOA", cdCliente+"", ItemComparator.EQUAL, Types.INTEGER));
			if(cdGrupoTrabalho>0)
				criterios.add(new ItemComparator("B.CD_GRUPO_TRABALHO", cdGrupoTrabalho+"", ItemComparator.EQUAL, Types.INTEGER));
			if(dtInicial!=null)
				criterios.add(new ItemComparator("A.DT_FINAL", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(dtInicial.getTimeInMillis())), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
			
			ResultSetMap rsm = getList(criterios, connect);
			
			if(lgAlerta==0)
				lgAlerta = ParametroServices.getValorOfParametroAsInteger("LG_ALERTA", 0, 0, connect);
			if(lgAlerta>0) {
				if(rsm.next()) {
					Mensagem msg = new Mensagem(0, "Agendas Pendentes", "Existem agendas pendentes de validação", 
							0, new GregorianCalendar(), 0, 0, 0, 0, 0, 0, 1);
					
					//DESTINATARIOS
					ArrayList<Object> grupos = ParametroServices.getValoresOfParametroAsArrayList("TP_GRUPO_USUARIO_ALERTA", 0, connect);
					ArrayList<HashMap<String, Object>> destinatarios = new ArrayList<>();
					for (Object cdGrupo : grupos) {
						ResultSetMap rsmUsers = GrupoServices.getUsuariosOfGrupo((int)cdGrupo, connect);
						while(rsmUsers.next()) {
							HashMap<String, Object> dest = new HashMap<>();
							dest.put("CD_PESSOA", rsmUsers.getInt("cd_pessoa"));
							
							destinatarios.add(dest);
						}
					}
										
					result = MensagemServices.save(msg, destinatarios, connect);
					if(result.getCode()<0) {
						System.out.println("ERRO! AgendaItemServices.getAgendasPendentes:\n"+result.getMessage());
						return result;
					}
					
					rsm.beforeFirst();
				}
			}
			
			result.addObject("RSM", rsm);
			
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result getContestacaoRecursoACumprir() {
		return getContestacaoRecursoACumprir(0, 0, null, 0, null);
	}
	
	public static Result getContestacaoRecursoACumprir(int lgAlerta) {
		return getContestacaoRecursoACumprir(0, 0, null, lgAlerta, null);
	}
	
	public static Result getContestacaoRecursoACumprir(int cdCliente, int cdGrupoTrabalho, GregorianCalendar dtInicial) {
		return getContestacaoRecursoACumprir(cdCliente, cdGrupoTrabalho, dtInicial, 0, null);
	}
	
	public static Result getContestacaoRecursoACumprir(int cdCliente, int cdGrupoTrabalho, GregorianCalendar dtInicial, int lgAlerta) {
		return getContestacaoRecursoACumprir(cdCliente, cdGrupoTrabalho, dtInicial, lgAlerta, null);
	}
	
	public static Result getContestacaoRecursoACumprir(int cdCliente, int cdGrupoTrabalho, GregorianCalendar dtInicial, int lgAlerta, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			System.out.println("AgendaItemServices.getContestacaoRecursoACumprir");
			Result result = new Result(1);

			ArrayList<Object> tiposPrazo = new ArrayList<>();
			ArrayList<Object> tpPrazoContestacao = ParametroServices.getValoresOfParametroAsArrayList("TP_PRAZO_CONTESTACAO", 0, connect);
			for (Object tpPrazo : tpPrazoContestacao) {
				tiposPrazo.add(tpPrazo);
			}
			
			ArrayList<Object> tpPrazoRecurso = ParametroServices.getValoresOfParametroAsArrayList("TP_PRAZO_RECURSO", 0, connect);
			for (Object tpPrazo : tpPrazoRecurso) {
				tiposPrazo.add(tpPrazo);
			}
						
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_TIPO_PRAZO", Util.join(tiposPrazo), ItemComparator.IN, Types.INTEGER));
			criterios.add(new ItemComparator("A.ST_AGENDA_ITEM", Integer.toString(ST_AGENDA_A_CUMPRIR), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.DT_FINAL", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(new GregorianCalendar().getTimeInMillis())), 
					ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
			criterios.add(new ItemComparator("B.ST_PROCESSO", ProcessoServices.ST_PROCESSO_ATIVO+"", ItemComparator.IN, Types.INTEGER));
			criterios.add(new ItemComparator("nrRegistros", "100", ItemComparator.EQUAL, Types.VARCHAR));
			criterios.add(new ItemComparator("orderByField", "A.DT_FINAL DESC", ItemComparator.EQUAL, Types.VARCHAR));
			if(cdCliente>0)
				criterios.add(new ItemComparator("I.CD_PESSOA", cdCliente+"", ItemComparator.EQUAL, Types.INTEGER));
			if(cdGrupoTrabalho>0)
				criterios.add(new ItemComparator("B.CD_GRUPO_TRABALHO", cdGrupoTrabalho+"", ItemComparator.EQUAL, Types.INTEGER));
			if(dtInicial!=null)
				criterios.add(new ItemComparator("A.DT_FINAL", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(dtInicial.getTimeInMillis())), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
			
			ResultSetMap rsm = getList(criterios, connect);
			
			if(lgAlerta==0)
				lgAlerta = ParametroServices.getValorOfParametroAsInteger("LG_ALERTA", 0, 0, connect);
			if(lgAlerta>0) {
				if(rsm.next()) {
					Mensagem msg = new Mensagem(0, "Contestação/Recurso não cumprido", "Existem prazos de Contestações ou Recursos não cumpridos", 
							0, new GregorianCalendar(), 0, 0, 0, 0, 0, 0, 1);
					
					//DESTINATARIOS
					ArrayList<Object> grupos = ParametroServices.getValoresOfParametroAsArrayList("TP_GRUPO_USUARIO_ALERTA", 0, connect);
					ArrayList<HashMap<String, Object>> destinatarios = new ArrayList<>();
					for (Object cdGrupo : grupos) {
						ResultSetMap rsmUsers = GrupoServices.getUsuariosOfGrupo((int)cdGrupo, connect);
						while(rsmUsers.next()) {
							HashMap<String, Object> dest = new HashMap<>();
							dest.put("CD_PESSOA", rsmUsers.getInt("cd_pessoa"));
							
							destinatarios.add(dest);
						}
					}
										
					result = MensagemServices.save(msg, destinatarios, connect);
					if(result.getCode()<0) {
						if(isConnectionNull)
							connect.rollback();
						System.out.println("ERRO! AgendaItemServices.getContestacaoRecursoACumprir:\n"+result.getMessage());
						return result;
					}
					
					rsm.beforeFirst();
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			result.addObject("RSM", rsm);
			
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	
	public Result isFaturado(int cdAgendaItem) {
		return isFaturado(cdAgendaItem, null);
	}
	
	public Result isFaturado(int cdAgendaItem, Connection connection) {
		boolean isConnectionNull = connection==null;
		if(isConnectionNull)
			connection = Conexao.conectar();
		
		try {
			boolean lgFaturado = false;
			
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.cd_evento_financeiro"
					+ " FROM prc_processo_financeiro A"
					+ " JOIN agd_agenda_item B ON (A.cd_agenda_item = B.cd_agenda_item)"
					+ " WHERE A.cd_agenda_item = "+cdAgendaItem
					+ " AND (A.cd_conta_pagar IS NOT NULL OR A.cd_conta_receber IS NOT NULL)");
			
			if(pstmt.executeQuery().next())
				lgFaturado = true;
			
			LogUtils.debug("LG_FATURADO: "+lgFaturado);
			
			Result result = new Result(1);
			result.addObject("LG_FATURADO", lgFaturado);
			
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Método para verificar se o ja existe agenda para o local
	 * @param códido do local e data
	 * @return
	 */
	public static boolean isOcupado(int cdLocal, GregorianCalendar data) {
		return isOcupado(cdLocal, data, null);
	}
	
	public static boolean isOcupado(int cdLocal, GregorianCalendar data, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT cd_agenda_item, dt_inicial FROM agd_agenda_item" +
											" WHERE cd_local = " + cdLocal +
											" AND dt_inicial = '" + Util.convCalendarStringSql(data) + " " + data.get(Calendar.HOUR_OF_DAY)+":"+data.get(Calendar.MINUTE) + "'");
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				
				if (rsm!=null && rsm.size()!=0)
					return true;
				
				return false;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemServices.isOcupado: " + sqlExpt);
			return false;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemServices.isOcupado: " + e);
			return false;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result saveCompliance(AgendaItem objeto, AuthData authData, int tpAcao, Connection connect) {
		try {
						
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO AGD_AGENDA_ITEM (CD_AGENDA_ITEM,"+
                    "DT_INICIAL,"+
                    "DT_FINAL,"+
                    "DT_LANCAMENTO,"+
                    "DS_DETALHE,"+
                    "ST_AGENDA_ITEM,"+
                    "CD_TIPO_PRAZO,"+
                    "CD_PESSOA,"+
                    "CD_PROCESSO,"+
                    "CD_TIPO_AGENDAMENTO,"+
                    "DT_REALIZACAO,"+
                    "DS_OBSERVACAO,"+
                    "DT_ALARME,"+
                    "CD_USUARIO,"+
                    "DT_ALTERACAO,"+
                    "DS_ASSUNTO,"+
                    "CD_DOCUMENTO,"+
                    "CD_TIPO_PRAZO_DOCUMENTO,"+
                    "DT_ACEITE,"+
                    "CD_USUARIO_ACEITE,"+
                    "CD_LOCAL,"+
                    "CD_EMPRESA,"+
                    "DT_REJEITE,"+
                    "CD_USUARIO_REJEITE,"+
                    "VL_SERVICO,"+
                    "TXT_PARECER,"+
                    "LG_PREPOSTO,"+
                    "CD_USUARIO_COMPLIANCE,"+
                    "DT_COMPLIANCE,"+ 
                    "TP_ACAO_COMPLIANCE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			pstmt.setInt(1, objeto.getCdAgendaItem());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			pstmt.setString(5,objeto.getDsDetalhe());
			pstmt.setInt(6,objeto.getStAgendaItem());
			if(objeto.getCdTipoPrazo()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTipoPrazo());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdPessoa());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdProcesso());
			pstmt.setInt(10,objeto.getCdTipoAgendamento());
			if(objeto.getDtRealizacao()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtRealizacao().getTimeInMillis()));
			pstmt.setString(12,objeto.getDsObservacao());
			if(objeto.getDtAlarme()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtAlarme().getTimeInMillis()));
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdUsuario());
			if(objeto.getDtAlteracao()==null)
				pstmt.setNull(15, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(15,new Timestamp(objeto.getDtAlteracao().getTimeInMillis()));
			pstmt.setString(16,objeto.getDsAssunto());
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdDocumento());
			if(objeto.getCdTipoPrazoDocumento()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdTipoPrazoDocumento());
			if(objeto.getDtAceite()==null)
				pstmt.setNull(19, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(19,new Timestamp(objeto.getDtAceite().getTimeInMillis()));
			if(objeto.getCdUsuarioAceite()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdUsuarioAceite());
			if(objeto.getCdLocal()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdLocal());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22,objeto.getCdEmpresa());
			if(objeto.getDtRejeite()==null)
				pstmt.setNull(23, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(23,new Timestamp(objeto.getDtRejeite().getTimeInMillis()));
			if(objeto.getCdUsuarioRejeite()==0)
				pstmt.setNull(24, Types.INTEGER);
			else
				pstmt.setInt(24, objeto.getCdUsuarioRejeite());
			pstmt.setDouble(25, objeto.getVlServico());
			pstmt.setString(26, objeto.getTxtParecer());
			pstmt.setInt(27, objeto.getLgPreposto());
			if(authData==null)
				pstmt.setNull(28, Types.INTEGER);
			else
				pstmt.setInt(28, authData.getUsuario().getCdUsuario());
			pstmt.setTimestamp(29, new Timestamp(new GregorianCalendar().getTimeInMillis()));
			pstmt.setInt(30, tpAcao);
			
			int retorno = pstmt.executeUpdate();
						
			return new Result((retorno<0 ? retorno : objeto.getCdAgendaItem()));
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public static ResultSetMap findCompliance(int cdAgendaItem) {
		return ComplianceManager.find("agd", "agenda_item", cdAgendaItem);
	}
	
	private static Result notificar(AgendaItem agendaItemOld, AgendaItem agendaItem, Connection connect) {
		try {
			ResultSetMap rsmRegras = new ResultSetMap();//RegraNotificacaoServices.getRegrasProcesso(connect);
			int cdAgendaItem    = agendaItem.getCdAgendaItem();
			int cdAgendaItemOld = (agendaItemOld!=null ? agendaItemOld.getCdAgendaItem() : 0);
			
			String dsAgendaItem = null;
			
			if(cdAgendaItemOld==0 && cdAgendaItem>0) {//INSERT
				rsmRegras = RegraNotificacaoServices.getRegrasAgenda(RegraNotificacaoServices.TP_ACAO_INSERT, connect);
				dsAgendaItem = agendaItem.getResumo(connect);
			}
			else if(cdAgendaItemOld>0 && cdAgendaItem>0) {//UPDATE
				rsmRegras = RegraNotificacaoServices.getRegrasAgenda(RegraNotificacaoServices.TP_ACAO_UPDATE, connect);	
				dsAgendaItem = agendaItem.getResumo(connect);
			}
			else if(cdAgendaItemOld>0 && cdAgendaItem==0) {//DELETE
				rsmRegras = RegraNotificacaoServices.getRegrasAgenda(RegraNotificacaoServices.TP_ACAO_DELETE, connect);
				agendaItemOld = AgendaItemDAO.get(cdAgendaItemOld);
				agendaItemOld.getResumo(null);
				dsAgendaItem = agendaItemOld.getResumo(connect);
				
				agendaItem = agendaItemOld;
			}
			
			ResultSetMap rsmRegrasPeriodicas = RegraNotificacaoServices
					.getRegras(RegraNotificacaoServices.TP_ENTIDADE_AGENDA, RegraNotificacaoServices.TP_ACAO_PERIODICAL, connect);
			while(rsmRegrasPeriodicas.next()) {
				rsmRegras.addRegister(rsmRegrasPeriodicas.getRegister());
			}
			
			while(rsmRegras.next()) {
				boolean notificar = true;
								
				//se existe o critério na regra e ele é diferente do valor no processo, NÃO notifica
				if(notificar && (rsmRegras.getInt("cd_responsavel_agenda", 0) > 0)) {
					if(rsmRegras.getInt("cd_responsavel_agenda") != agendaItem.getCdPessoa())
						notificar = false;
				}
				if(notificar && (rsmRegras.getInt("tp_agenda_item", -1) >= 0)) {
					TipoPrazo tipoPrazo = TipoPrazoDAO.get(agendaItem.getCdTipoPrazo(), connect);
					if(rsmRegras.getInt("tp_agenda_item") != tipoPrazo.getTpAgendaItem())
						notificar = false;
				}
				if(notificar && rsmRegras.getInt("cd_tipo_prazo", 0) > 0) {
					if(rsmRegras.getInt("cd_tipo_prazo") != agendaItem.getCdTipoPrazo())
						notificar = false;
				}
				if(notificar && (rsmRegras.getInt("st_agenda_item", 0) > 0)) {
					if(rsmRegras.getInt("st_agenda_item") != agendaItem.getStAgendaItem())
						notificar = false;
				}
				if(notificar && (rsmRegras.getInt("LG_MUDANCA_PRAZO")==1)) {
					if((agendaItemOld.getDtFinal().compareTo(agendaItem.getDtFinal())==0) && (agendaItemOld.getDtInicial().compareTo(agendaItem.getDtInicial())==0))
						notificar = false;
				}
				if(notificar && (rsmRegras.getInt("LG_MUDANCA_RESPONSAVEL")==1)) {
					if(verificarMudancaResponsavel(agendaItemOld, agendaItem)==-1)
						notificar = false;
				} 
				
				Processo processo = ProcessoDAO.get(agendaItem.getCdProcesso(), connect);
				if(processo!=null) {
					if(notificar && (rsmRegras.getInt("cd_grupo_trabalho", 0) > 0)) {
						if(rsmRegras.getInt("cd_grupo_trabalho") != processo.getCdGrupoTrabalho())
							notificar = false;
					}
					if(notificar && (rsmRegras.getInt("cd_advogado", 0) > 0)) {
						if(rsmRegras.getInt("cd_advogado") != processo.getCdAdvogado())
							notificar = false;
					}
					if(notificar && (rsmRegras.getInt("cd_cliente", 0) > 0)) {
						if(!Util.contains(ProcessoServices.getParteCliente(processo.getCdProcesso(), connect), "CD_PESSOA", rsmRegras.getInt("cd_cliente")))
							notificar = false;
					}
				}
				
				if(rsmRegras.getInt("tp_acao")==RegraNotificacaoServices.TP_ACAO_PERIODICAL) {
					GregorianCalendar deadline = new GregorianCalendar();
					deadline.add(Calendar.HOUR, rsmRegras.getInt("qt_horas_final_prazo"));
					
					GregorianCalendar hoje = new GregorianCalendar();
					
					GregorianCalendar dtFinal = agendaItem.getDtFinal();
					
					notificar = dtFinal.after(hoje) && dtFinal.before(deadline);
				}
				
				if(notificar) {
					Notificacao notificacao = new Notificacao(0, 		   /*cdNotificacao*/
							0,											   /*cdUsuario*/ 
							rsmRegras.getString("NM_REGRA_NOTIFICACAO"),   /*dsAssunto*/ 
							Notificacao.INFO,							   /*tpNotificacao*/
							dsAgendaItem,								   /*txtNotificacao*/
							new GregorianCalendar(),					   /*dtNotificacao*/
							null,										   /*dtLeitura*/ 
							0,											   /*cdMensagem*/
							rsmRegras.getInt("CD_REGRA_NOTIFICACAO"), 	   /*cdRegraNotificacao*/
							Integer.toString(agendaItem.getCdAgendaItem()) /*txtObjeto*/);
					
					RegraNotificacaoServices.notificar(rsmRegras.getInt("cd_regra_notificacao"), notificacao, agendaItem.getCdProcesso(), agendaItem.getCdAgendaItem(), connect);
				}
			}
			
			return new Result(1);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		
	}
	
	public static Result notificarPeriodico() {
		try {
			Connection connect = Conexao.conectar();
						
			//1. REGRAS
			ResultSetMap rsmRegras = RegraNotificacaoServices
					.getRegras(RegraNotificacaoServices.TP_ENTIDADE_AGENDA, RegraNotificacaoServices.TP_ACAO_PERIODICAL, connect);
						
			while(rsmRegras.next()) {
				if(rsmRegras.getInt("qt_horas_final_prazo") <= 0)
					continue;
				
				GregorianCalendar deadline = new GregorianCalendar();
								  deadline.set(Calendar.MINUTE, 0);
								  deadline.set(Calendar.SECOND, 0);
								  deadline.set(Calendar.MILLISECOND, 0);
				deadline.add(Calendar.HOUR, rsmRegras.getInt("qt_horas_final_prazo"));
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				if(rsmRegras.getInt("cd_responsavel_agenda", 0) > 0) {
					criterios.add(new ItemComparator("A.CD_PESSOA", Integer.toString(rsmRegras.getInt("cd_responsavel_agenda")), ItemComparator.EQUAL, Types.INTEGER));
				}
				if(rsmRegras.getInt("tp_agenda_item", -1) >= 0) {
					criterios.add(new ItemComparator("C.TP_AGENDA_ITEM", Integer.toString(rsmRegras.getInt("tp_agenda_item")), ItemComparator.EQUAL, Types.INTEGER));
				}
				if(rsmRegras.getInt("st_agenda_item", 0) > 0) {
					criterios.add(new ItemComparator("A.ST_AGENDA_ITEM", Integer.toString(rsmRegras.getInt("st_agenda_item")), ItemComparator.EQUAL, Types.INTEGER));
				}
				if(rsmRegras.getInt("cd_tipo_prazo", 0) > 0) {
					criterios.add(new ItemComparator("A.CD_TIPO_PRAZO", Integer.toString(rsmRegras.getInt("cd_tipo_prazo")), ItemComparator.EQUAL, Types.INTEGER));
				}
				if(rsmRegras.getInt("cd_grupo_trabalho", 0) > 0) {
					criterios.add(new ItemComparator("B.CD_GRUPO_TRABALHO", Integer.toString(rsmRegras.getInt("cd_grupo_trabalho")), ItemComparator.EQUAL, Types.INTEGER));
				}
				if(rsmRegras.getInt("cd_advogado", 0) > 0) {
					criterios.add(new ItemComparator("B.CD_ADVOGADO", Integer.toString(rsmRegras.getInt("cd_advogado")), ItemComparator.EQUAL, Types.INTEGER));
				}
				if(rsmRegras.getInt("cd_cliente", 0) > 0) {
					criterios.add(new ItemComparator("I.CD_PESSOA", Integer.toString(rsmRegras.getInt("cd_cliente")), ItemComparator.EQUAL, Types.INTEGER));
				}
				criterios.add(new ItemComparator("A.DT_FINAL", Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy")+" 00:00:00", ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
				criterios.add(new ItemComparator("A.DT_FINAL", Util.formatDate(deadline, "dd/MM/yyyy")+" 23:59:59", ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
				criterios.add(new ItemComparator("A.CD_EMPRESA", ParametroServices.getValorOfParametro("CD_EMPRESA_DEFAULT", "3", connect), ItemComparator.EQUAL, Types.INTEGER));				
				
				//2. AGENDAS
				ResultSetMap rsmAgendas = getList(criterios, connect);
				while(rsmAgendas.next()) {
					AgendaItem agenda = AgendaItemDAO.get(rsmAgendas.getInt("cd_agenda_item"), connect);
					
					GregorianCalendar dtFinal = (GregorianCalendar)agenda.getDtFinal().clone();
									  dtFinal.set(Calendar.MINUTE, 0);
									  dtFinal.set(Calendar.SECOND, 0);
									  dtFinal.set(Calendar.MILLISECOND, 0);
									  					
					if(deadline.compareTo(dtFinal) == 0) {
						Notificacao notificacao = new Notificacao(0 /*cdNotificacao*/, 
								0/*cdUsuario*/, 
								rsmRegras.getString("NM_REGRA_NOTIFICACAO")+" EM "+rsmRegras.getInt("qt_horas_final_prazo")+" HORA(S)"/*dsAssunto*/, 
								Notificacao.ALERTA/*tpNotificacao*/, 
								agenda.getResumo(connect)/*txtNotificacao*/, 
								new GregorianCalendar()/*dtNotificacao*/, 
								null/*dtLeitura*/, 
								0/*cdMensagem*/, 
								rsmRegras.getInt("CD_REGRA_NOTIFICACAO"), 
								Integer.toString(agenda.getCdAgendaItem())/*txtObjeto*/);
						
						RegraNotificacaoServices.notificar(rsmRegras.getInt("cd_regra_notificacao"), notificacao, agenda.getCdProcesso(), agenda.getCdAgendaItem(), connect);
					}
				}
			}
			
			return new Result(1);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public static Result salvarLote(AgendaItem agenda, ArrayList<Integer> processos, AuthData auth) {
		return salvarLote(agenda, processos, auth, null);
	}
	
	public static Result salvarLote(AgendaItem agenda, ArrayList<Integer> processos, AuthData auth, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if(isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			if(agenda==null)
				return new Result(-2, "A agenda é nula.");
			
			if(processos==null || processos.size()<=0)
				return new Result(-3, "A lista de processos é inválida.");
			
			for (Integer cdProcesso : processos) {
				agenda.setCdAgendaItem(0);
				agenda.setCdProcesso(cdProcesso);
				
				Result result = save(agenda, auth, connection);
				if(result.getCode()<=0) {
					if(isConnectionNull)
						connection.rollback();
					return result;
				}
			}
			
			if(isConnectionNull) {
				connection.commit();
			}
						
			return new Result(1, "Agendas gravadas com sucesso.");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao salvar agendas.");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getLogCompliance(int cdAgendaItem, int cdProcesso, boolean lgDelete) {
		return getLogCompliance(cdAgendaItem, cdProcesso, lgDelete, null);
	}
	
	public static ResultSetMap getLogCompliance(int cdAgendaItem, int cdProcesso, boolean lgDelete, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			ResultSetMap rsm = ComplianceManager
					.search(" SELECT * FROM agd_agenda_item "
						+ " WHERE 1=1"
						+ (lgDelete ? 
						  " AND cd_processo="+cdProcesso+" AND tp_acao_compliance="+ComplianceManager.TP_ACAO_DELETE	
						  :
						  " AND cd_agenda_item="+cdAgendaItem+" AND cd_processo="+cdProcesso)
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
				
				//tipo prazo
				if(rsm.getInt("cd_tipo_prazo", 0) > 0) {
					TipoPrazo tipoPrazo = TipoPrazoDAO.get(rsm.getInt("cd_tipo_prazo"), connect);
					rsm.setValueToField("nm_tipo_prazo", tipoPrazo.getNmTipoPrazo());
					rsm.setValueToField("nm_tp_agenda_item", TipoPrazoServices.tiposAgendaItem[tipoPrazo.getTpAgendaItem()]);
				}
				
				//situação
				rsm.setValueToField("nm_st_agenda_item", AgendaItemServices.situacoesAgenda[rsm.getInt("st_agenda_item")].toUpperCase());
				
				//responsável
				if(rsm.getInt("cd_pessoa", 0) > 0) {
					Pessoa pessoa = PessoaDAO.get(rsm.getInt("cd_pessoa"), connect);
					rsm.setValueToField("nm_responsavel", pessoa.getNmPessoa());
				} else if(rsm.getInt("cd_grupo_trabalho", 0) > 0) {
					Grupo grupoTrabaho = GrupoDAO.get(rsm.getInt("cd_grupo_trabalho"), connect);
					rsm.setValueToField("nm_responsavel", grupoTrabaho.getNmGrupo());
				}
				
				//usuário cadastro
				if(rsm.getInt("cd_usuario", 0) > 0) {
					Usuario usuario = UsuarioDAO.get(rsm.getInt("cd_usuario"), connect);
					Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa(), connect);
					rsm.setValueToField("nm_usuario", pessoa.getNmPessoa());
				}
				
				//usuário cumprimento
				if(rsm.getInt("cd_usuario_cumprimento", 0) > 0) {
					Usuario usuario = UsuarioDAO.get(rsm.getInt("cd_usuario_cumprimento"), connect);
					Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa(), connect);
					rsm.setValueToField("nm_usuario_cumprimento", pessoa.getNmPessoa());
				} else if(rsm.getInt("st_agenda_item")==ST_AGENDA_CUMPRIDO) {
					rsm.setValueToField("nm_usuario_cumprimento", rsm.getString("nm_usuario_compliance"));
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
	

	/**
	 * indica a mudança de responsável ocorrida no item
	 * 
	 * @param item1 versão anterior da agenda
	 * @param item2 versão atual da agenda
	 * @return a mudança que ocorreu
	 */
	private static int verificarMudancaResponsavel(AgendaItem item1, AgendaItem item2) {
		
		if((item1.getCdPessoa()>0 && item1.getCdGrupoTrabalho()==0) && (item2.getCdPessoa()>0 && item2.getCdGrupoTrabalho()==0) && (item1.getCdPessoa() != item2.getCdPessoa())) {
			return CHANGE_PESSOA_PESSOA;
		} else if((item1.getCdPessoa()>0 && item1.getCdGrupoTrabalho()==0) && (item2.getCdPessoa()==0 && item2.getCdGrupoTrabalho()>0)) {
			return CHANGE_PESSOA_GRUPO;
		} else if((item1.getCdGrupoTrabalho()>0 && item1.getCdPessoa()==0) && (item2.getCdGrupoTrabalho()>0 && item2.getCdPessoa()==0) && (item1.getCdGrupoTrabalho() != item2.getCdGrupoTrabalho())) {
			return CHANGE_GRUPO_GRUPO;
		} else if((item1.getCdGrupoTrabalho()>0 && item1.getCdPessoa()==0) && (item2.getCdPessoa()>0 && item2.getCdGrupoTrabalho()==0)) {
			return CHANGE_GRUPO_PESSOA;
		}
		
		return -1;
	}
	
	// ================================================================================
	/**
	 * @angular	
	 */
	
	public static ResultSetMap getAgendasAgrupado(int tpAgendaItem) {
		return getAgendasAgrupado(tpAgendaItem, null);
	}
	public static ResultSetMap getAgendasAgrupado(int tpAgendaItem, Connection connection) {
		
		boolean isConnectionNull = connection == null;
		try {
			if(isConnectionNull)
				connection = Conexao.conectar();
			
			GregorianCalendar hoje = new GregorianCalendar();
			
			GregorianCalendar dtInicial = (GregorianCalendar)hoje.clone();
			dtInicial.add(Calendar.YEAR, -1);
			dtInicial.set(Calendar.HOUR, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtInicial.set(Calendar.SECOND, 0);
			
			GregorianCalendar dtFinal = (GregorianCalendar)hoje.clone();
			dtFinal.set(Calendar.HOUR, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			dtFinal.set(Calendar.SECOND, 59);
			
			ResultSetMap rsmGrupos = com.tivic.manager.agd.GrupoServices.getAtivos(connection);
			
			ResultSetMap rsmLabels = new ResultSetMap();
			
			while(dtInicial.before(dtFinal)) {	
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("NM_MES", Util.formatDate(dtInicial, "MMM/yyyy"));
				register.put("NR_MES", dtInicial.get(Calendar.MONTH));
				rsmLabels.addRegister(register);
				
				dtInicial.add(Calendar.MONTH, 1);
			} 
			rsmLabels.beforeFirst();
			
			dtInicial = (GregorianCalendar)hoje.clone();
			dtInicial.add(Calendar.YEAR, -1);
			dtInicial.set(Calendar.HOUR, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtInicial.set(Calendar.SECOND, 0);
			
			ResultSetMap rsm = new ResultSetMap();
			
			while(rsmGrupos.next()) {

				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("RSM_LABELS", rsmLabels);
				
				dtInicial = (GregorianCalendar)hoje.clone();
				dtInicial.add(Calendar.YEAR, -1);
				dtInicial.set(Calendar.HOUR, 0);
				dtInicial.set(Calendar.MINUTE, 0);
				dtInicial.set(Calendar.SECOND, 0);
				
				int cdGrupo = rsmGrupos.getInt("CD_GRUPO");
				
				register.put("NM_GRUPO", rsmGrupos.getString("NM_GRUPO"));
				
				int[] data = new int[rsmLabels.size()];
				int i = 0;
				while(dtInicial.before(dtFinal)) {	
					
					GregorianCalendar dtAux = (GregorianCalendar)dtInicial.clone();
					dtAux.add(Calendar.MONTH, 1);
					
					PreparedStatement ps = connection.prepareStatement(
							  " SELECT COUNT(A.*) AS qt_agendas"
							+ " FROM agd_agenda_item A"
							+ " LEFT OUTER JOIN prc_processo B ON (A.cd_processo = B.cd_processo)"
							+ " LEFT OUTER JOIN agd_grupo C ON (B.cd_grupo_trabalho = C.cd_grupo)"
							+ " JOIN prc_tipo_prazo D ON (A.cd_tipo_prazo = D.cd_tipo_prazo)"
							+ " WHERE D.tp_agenda_item = ?"
							+ " AND A.st_agenda_item = ?"
							+ " AND A.dt_inicial BETWEEN ? AND ?"
							+ " AND B.cd_grupo_trabalho = ?");
					ps.setInt(1, tpAgendaItem);
					ps.setInt(2, ST_AGENDA_CUMPRIDO);
					ps.setTimestamp(3, Util.convCalendarToTimestamp(dtInicial));
					ps.setTimestamp(4, Util.convCalendarToTimestamp(dtAux));
					ps.setInt(5, cdGrupo);
					
					ResultSetMap rsmAux = new ResultSetMap(ps.executeQuery());
					if(rsmAux.next()) {
						data[i] = rsmAux.getInt("qt_agendas");
					} else {
						data[i] = 0;
					}
					
					i++;
					dtInicial.add(Calendar.MONTH, 1);
				} 
				
				register.put("ARRAY_DATA", data);
				
				rsm.addRegister(register);
			}
			
			
					
			return rsm;
		}
		catch (Exception e) {
			System.out.println("Erro ao buscar log: "+e.getMessage());
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connection);
		}
		
	}
	// ================================================================================
	
}
