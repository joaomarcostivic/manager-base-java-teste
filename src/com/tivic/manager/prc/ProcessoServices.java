package com.tivic.manager.prc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONObject;
import org.xlsx4j.sml.Workbook;

import com.sun.xml.internal.bind.v2.runtime.output.Pcdata;
import com.tivic.manager.agd.AgendaItem;
import com.tivic.manager.agd.AgendaItemDAO;
import com.tivic.manager.agd.AgendaItemServices;
import com.tivic.manager.agd.Grupo;
import com.tivic.manager.agd.GrupoDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.gpn.WorkflowAcaoServices;
import com.tivic.manager.gpn.WorkflowGatilhoServices;
import com.tivic.manager.gpn.WorkflowRegraServices;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.IndicadorVariacao;
import com.tivic.manager.grl.IndicadorVariacaoDAO;
import com.tivic.manager.grl.ModeloDocumento;
import com.tivic.manager.grl.ModeloDocumentoDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaFisicaServices;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaJuridicaDAO;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.grl.TipoLogradouro;
import com.tivic.manager.grl.TipoLogradouroDAO;
import com.tivic.manager.importacao.DesktopImport;
import com.tivic.manager.importacao.GenericImport;
import com.tivic.manager.msg.Mensagem;
import com.tivic.manager.msg.MensagemServices;
import com.tivic.manager.msg.Notificacao;
import com.tivic.manager.msg.NotificacaoServices;
import com.tivic.manager.msg.RegraNotificacao;
import com.tivic.manager.msg.RegraNotificacaoDAO;
import com.tivic.manager.msg.RegraNotificacaoServices;
import com.tivic.manager.print.Converter;
import com.tivic.manager.print.ExcelServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoDAO;
import com.tivic.manager.ptc.TipoDocumentoDAO;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.seg.GrupoServices;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.util.ComplianceManager;
import com.tivic.manager.util.DateUtil;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ProcessoServices {
	public static final String[] parteCliente 	  = {"Requerido", "Autor"};
	public static final String[] tipoInstancia    = {"Não Informada", "Primeiro Grau","Segundo Grau","Juizados Cíveis","Juizados Criminais",
		                                             "Turma Recursal","Execução Penal","Superiores"};
	public static final String[] situacaoProcesso = {"Inativo","Ativo","Suspenso","Descontratado", "Pendente de Patrocínio", "Reativado"};
	public static final String[] tipoPerda        = {"Possível","Provável","Remota","Não Contigenciável"};
	public static final String[] tipoSentenca     = {"Não sentenciado","Procedente","Improcedente","Parc. Procedente",
													 "Acordo", "Extinto sem Mérito", "Acordo após a condenação", "Acordo antes da instrução",
													 "Desistência", "Contumácia"};
	public static final String[] situacaoLiminar  = {"Sem Pedido","Pendente","Pedido Deferido","Pedido Indeferido", "Deferido - Cumprido", "Deferido - Revogado"};
	public static final String[] tiposAutos 	  = {"Físico", "Eletrônico"};
	public static final String[] tiposRito 	 	  = {"Rito Comum", "Rito Especial", "Rito dos Juizados Especial"};
	public static final String[] tiposRepasse 	  = {"Inicial", "Migração"};
	
	public static final int ST_PROCESSO_INATIVO       		= 0;
	public static final int ST_PROCESSO_ATIVO         		= 1;
	public static final int ST_PROCESSO_SUSPENSO      		= 2;
	public static final int ST_PROCESSO_DESCONTRATADO 		= 3;
	public static final int ST_PROCESSO_PENDENTE_PATROCINIO = 4;
	public static final int ST_PROCESSO_REATIVADO			= 5;
	
	public static Result save(Processo processo){
		return save(processo, null, null, null, 0, null, null);
	}
	
	public static Result save(Processo processo, ResultSetMap sentencas){
		return save(processo, null, null, null, 0, sentencas, null);
	}
	
	public static Result save(Processo processo, int cdUsuario, ResultSetMap sentencas){
		return save(processo, null, null, null, cdUsuario, sentencas, null);
	}
	
	public static Result save(Processo processo, int cdUsuario){
		return save(processo, null, null, null, cdUsuario, null, null);
	}
	
	public static Result save(Processo processo, Connection connect){
		return save(processo, null, null, null, 0, null, connect);
	}
	
	public static Result save(Processo processo, ResultSetMap parteCliente, 
							ResultSetMap parteAdverso, ResultSetMap tiposPedido){
		return save(processo, parteCliente, parteAdverso, tiposPedido, 0, null, null);
	}
	
	public static Result save(Processo processo, ResultSetMap parteCliente, 
			ResultSetMap parteAdverso, ResultSetMap tiposPedido, int cdUsuario){
		return save(processo, parteCliente, parteAdverso, tiposPedido, cdUsuario, null, null);
	}
	
	public static Result save(Processo processo, ResultSetMap parteCliente, 
			ResultSetMap parteAdverso, ResultSetMap tiposPedido, int cdUsuario, ResultSetMap sentencas) {
		return save(processo, parteCliente, parteAdverso, tiposPedido, cdUsuario, sentencas, null);
	}
	
	public static Result save(Processo processo, ResultSetMap parteCliente, 
			ResultSetMap parteAdverso, ResultSetMap tiposPedido, int cdUsuario, ResultSetMap sentencas, Connection connect){
		return save(processo, parteCliente, parteAdverso, tiposPedido, cdUsuario, sentencas, null, connect);
	}
	
	public static Result save(Processo processo, ResultSetMap parteCliente, ResultSetMap parteAdverso, 
			ResultSetMap tiposPedido, int cdUsuario, ResultSetMap sentencas, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(processo==null)
				return new Result(-1, "Erro ao salvar. Processo é nulo");
			
			int retorno;
			
			if(authData == null) {
				authData = new AuthData();
				authData.setUsuario(UsuarioDAO.get(cdUsuario, connect));
				authData.setIdModulo("JUR");
			}
			
			Processo pAntigo = ProcessoDAO.get(processo.getCdProcesso(), connect);
						
			int tpAcao = -1;
			
			if(processo.getCdProcesso()==0){
				tpAcao = ComplianceManager.TP_ACAO_INSERT;
				//verificar processo com o mesmo numero
				Result r = verificaNrProcesso(processo.getNrProcesso(), connect);
				if(r.getCode()<=0)
					return r;			
				
				processo.setDtCadastro(new GregorianCalendar());
				
				//verificar se eh um pre-cadastro
				if(processo.getNrProcesso()==null)
					processo.setNrInterno(getNextNrInterno());
				
				retorno = ProcessoDAO.insert(processo, connect);
				processo.setCdProcesso(retorno);
			}
			else {
				tpAcao = ComplianceManager.TP_ACAO_UPDATE;
				
				//ATIVO/INATIVO
				if(pAntigo.getStProcesso()!=ST_PROCESSO_INATIVO && processo.getStProcesso()==ST_PROCESSO_INATIVO) {
					processo.setDtInativacao(new GregorianCalendar());
				} else if(pAntigo.getStProcesso()==ST_PROCESSO_INATIVO && processo.getStProcesso()!=ST_PROCESSO_INATIVO) {
					processo.setDtInativacao(null);
				}
				
				retorno = ProcessoDAO.update(processo, connect);
			}
			
			//WORKFLOW AUTOMATICO: ANDAMENTOS
			if(retorno>0) {
				if((pAntigo==null || pAntigo.getDtDistribuicao()==null) && processo.getDtDistribuicao()!=null)
					addAndamento("distribuicao", processo, cdUsuario, authData, connect);
				if(processo.getTpSentenca()>0  && processo.getTpSentenca()<=3 && 
					(pAntigo==null || pAntigo.getDtSentenca() == null) && processo.getDtSentenca()!=null)
					addAndamento("sentenca", processo, cdUsuario, authData, connect);
				if((pAntigo==null || pAntigo.getTpSentenca()!=4) && processo.getTpSentenca()==4)
					addAndamento("acordo", processo, cdUsuario, authData, connect);
				if((pAntigo==null || pAntigo.getStProcesso()!=0) && processo.getStProcesso()==0)
					addAndamento("inativacao", processo, cdUsuario, authData, connect);
				if((pAntigo==null || pAntigo.getStProcesso()!=3) && processo.getStProcesso()==3)
					addAndamento("descontratacao", processo, cdUsuario, authData, connect);
				if((pAntigo==null || pAntigo.getDtRepasse()==null) && processo.getDtRepasse()!=null)
					addAndamento("repasse", processo, cdUsuario, authData, connect);
				if((pAntigo==null || pAntigo.getStProcesso()!=ST_PROCESSO_REATIVADO) && processo.getStProcesso()==ST_PROCESSO_REATIVADO)
					addAndamento("reativacao", processo, cdUsuario, authData, connect);
				
			}
			
			/* PARTE CLIENTE */
			while(retorno>0 && parteCliente!=null && parteCliente.next()) {
				retorno = insertParteCliente(processo.getCdProcesso(), parteCliente.getInt("CD_PESSOA"), connect).getCode();
				if(retorno==-2)//cliente já está no processo
					retorno = 1;
			}
			
			/* PARTE ADVERSO */
			while(retorno>0 && parteAdverso!=null && parteAdverso.next()) {
				retorno = insertAdverso(processo.getCdProcesso(), parteAdverso.getInt("CD_PESSOA"), connect).getCode();
				if(retorno==-2)//adverso já está no processo
					retorno = 1;
			}

			/* TIPO PEDIDO */
			while(retorno>0 && tiposPedido!=null && tiposPedido.next()) {
				retorno = insertTipoPedido(processo.getCdProcesso(), tiposPedido.getInt("CD_TIPO_PEDIDO"), connect).getCode();
				if(retorno==-2)//tipo pedido já está no processo
					retorno = 1;
			}
			
			/* SENTENCA */
			if(retorno>0 && sentencas!=null && sentencas.next()) {
				retorno = connect.prepareStatement("DELETE FROM prc_processo_sentenca WHERE cd_processo="+processo.getCdProcesso()).executeUpdate();
				
				sentencas.beforeFirst();
				while(sentencas.next()) {
					GregorianCalendar dtSentenca = sentencas.getGregorianCalendar("dt_sentenca");//Util.dateToCalendar((java.util.Date)sentencas.getObject("dt_sentenca"));
					ProcessoSentenca sentenca = new ProcessoSentenca(sentencas.getInt("cd_sentenca"), processo.getCdProcesso(), 
							sentencas.getInt("tp_sentenca"), dtSentenca, 
							sentencas.getDouble("vl_sentenca"), sentencas.getDouble("vl_acordo"), sentencas.getDouble("vl_total"));
					
					retorno = ProcessoSentencaServices.save(sentenca, null, connect).getCode();
					
					if(retorno>0) {
						if(sentencas.getInt("tp_sentenca")>0 && sentencas.getInt("tp_sentenca")<=3)
							addAndamento("sentenca", processo, cdUsuario, authData, connect);
					}
				}
			}
			
			//GPN WORKFLOW =====================================================
			Result resultWorkflow = null;
			if(tpAcao == ComplianceManager.TP_ACAO_INSERT) {
				ResultSetMap rsmGpnRegrasWorkflow = WorkflowRegraServices.getByGatilho(WorkflowGatilhoServices.TP_GATILHO_LANCAMENTO_PROCESSO, 0, 0, 0, 0, connect);
				if(retorno>0 && rsmGpnRegrasWorkflow!=null)
					resultWorkflow = aplicarRegrasGpnWorkflow(rsmGpnRegrasWorkflow, pAntigo, processo, authData, connect);
			}
			// =================================================================
				

			// NOTIFICAÇÃO =====================================================
			notificar((pAntigo!=null?pAntigo.getCdProcesso():0), processo.getCdProcesso(), connect);
			// =================================================================
			
			// COMPLIANCE ======================================================
			ComplianceManager.process(processo, authData, tpAcao, connect);
			// =================================================================			

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			Result result = new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PROCESSO", processo);
			
			if(resultWorkflow!=null && resultWorkflow.getCode()>0) {
				result.addObject("AUTOTEXTO", resultWorkflow.getObjects().get("AUTOTEXTO"));
				result.addObject("DESTINATARIOS", resultWorkflow.getObjects().get("DESTINATARIOS"));
				result.addObject("ARQUIVOS", resultWorkflow.getObjects().get("ARQUIVOS"));
				result.addObject("ASSINATURA", resultWorkflow.getObjects().get("ASSINATURA"));
				result.addObject("EVENTOS_WORKFLOW", resultWorkflow.getObjects().get("EVENTOS_WORKFLOW"));
				result.addObject("ANDAMENTOS_WORKFLOW", resultWorkflow.getObjects().get("ANDAMENTOS_WORKFLOW"));
			}
					
			/*
			 * informar ao serviços de recorte que há novo processo
			 */
			if(tpAcao==ComplianceManager.TP_ACAO_INSERT && processo.getNrProcesso()!=null)
				ServicoRecorteServices.sendNewProcess(processo);
			else if (tpAcao==ComplianceManager.TP_ACAO_UPDATE && (pAntigo.getNrProcesso()==null && processo.getNrProcesso()!=null))
				ServicoRecorteServices.sendNewProcess(processo);
				
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
	
	private static Result addAndamento(String nmAto, Processo processo, Connection connect) {
		return addAndamento(nmAto, processo, 0, null, connect);
	}
	
	private static Result addAndamento(String nmAto, Processo processo, int cdUsuario, AuthData auth, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdTipoAndamento = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_TIPO_ANDAMENTO_"+nmAto.toUpperCase(), 0);
		
			if(cdTipoAndamento <= 0) {
				System.out.println("Erro ao incluir andamento automático. Tipo de Ação não tem valor nos parâmetros. ["+nmAto+"]");
				return new Result(-1, "Erro ao incluir andamento automático. Tipo de Ação não tem valor nos parâmetros");
			}
			
			ProcessoAndamento andamento = new ProcessoAndamento(0, //cdAndamento, 
					processo.getCdProcesso(), 
					cdTipoAndamento, 
					new GregorianCalendar(), //dtAndamento, 
					new GregorianCalendar(), //dtLancamento, 
					null, //txtAndamento, 
					1, //stAndamento, 
					0, //tpInstancia, 
					null, //txtAta, 
					(cdUsuario>0 ? cdUsuario : processo.getCdUsuarioCadastro()), 
					null, //dtAlteracao, 
					0, //tpOrigem, 
					null, //blbAta, 
					ProcessoAndamentoServices.TP_VISIBILIDADE_PRIVADO, //tpVisibilidade, 
					0, //tpEventoFinanceiro, 
					0.0, //vlEventoFinanceiro, 
					0, //cdContaPagar, 
					0, //cdContaReceber, 
					null, //dtAtualizacaoEdi, 
					0, //stAtualizacaoEdi, 
					null, //txtPublicacao);
					0, /*cdDocumento*/
					0 /*cdOrigemAndamento*/,
					0 /*cdRecorte*/);
			
			boolean incluir = false;
			
			
			/************** DISTRIBUIÇÃO **********/
		    if(nmAto.equals("distribuicao") && processo.getDtDistribuicao() != null) {
		    	andamento.setDtAndamento(processo.getDtDistribuicao());
		    	andamento.setTxtAndamento("PROCESSO DISTRIBUIDO EM: "+Util.formatDate(processo.getDtDistribuicao(), "dd/MM/yyyy"));
		    	incluir = true;
		    }
		    /************** SENTENÇA **************/
		    else if(nmAto.equals("sentenca") && 
		    		 processo.getTpSentenca()>0  && processo.getTpSentenca()<=3 && 
		    		 processo.getDtSentenca() != null) {
		    	andamento.setDtAndamento(processo.getDtSentenca());
		    	andamento.setTxtAndamento("SENTENÇA PROFERIDA EM: "+Util.formatDate(processo.getDtSentenca(), "dd/MM/yyyy") + 
		    			 " SENDO O VALOR DE: R$ " + Util.formatNumber(processo.getVlSentenca(), "#.00"));
		    	incluir = true;
		    }
		    /************** ACORDO ****************/
		    else if(nmAto.equals("acordo") && 
		    		 processo.getTpSentenca()==4 && 
		    		 processo.getDtSentenca() != null) {
		    	andamento.setDtAndamento(processo.getDtSentenca());
		    	andamento.setTxtAndamento("ACORDO FIRMADO EM: "+Util.formatDate(processo.getDtSentenca(), "dd/MM/yyyy") + 
		    			 " NO VALOR DE: R$ " + Util.formatNumber(processo.getVlAcordo(), "#.00"));
		    	incluir = true;
		    }
		    /************** INATIVAÇÃO ************/
		    else if(nmAto.equals("inativacao") && processo.getStProcesso()==0) {
		    	andamento.setTxtAndamento("PROCESSO INATIVADO EM: "+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy"));
		    	incluir = true;
		    }
		    /************** REATIVAÇÃO ************/
		    else if(nmAto.equals("reativacao") && processo.getStProcesso()==ST_PROCESSO_REATIVADO) {
		    	andamento.setTxtAndamento("PROCESSO REATIVADO EM: "+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy"));
		    	incluir = true;
		    }
		    /************** REPASSE ***************/
		    else if(nmAto.equals("repasse") && processo.getDtRepasse() != null) {
		    	andamento.setDtAndamento(processo.getDtRepasse());
		    	andamento.setTxtAndamento("REPASSADO PELO CLIENTE EM: "+Util.formatDate(processo.getDtRepasse(), "dd/MM/yyyy"));
		    	incluir = true;
		    }
		    /***** PAGAMENTO DE CUSTAS ************/
		    else if(nmAto.equals("despesa")) {
		    	andamento.setTxtAndamento("SOLICITADO O PAGAMENTO DE CUSTAS EM: "+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy"));
		    	incluir = true;
		    }
		    /************** DESCONTRATAÇÃO ************/
		    if(nmAto.equals("descontratacao") && processo.getStProcesso()==ST_PROCESSO_DESCONTRATADO) {
		    	System.out.println("DESCONTRATAR");
		    	andamento.setTxtAndamento("PROCESSO DESCONTRATADO EM: "+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy"));
		    	incluir = true;
		    }
		 	
		    int retorno =  -1;
		    if(incluir) {
		    	Result r = ProcessoAndamentoServices.save(andamento, null, null, null, auth, connect);
		    	
		    	retorno = r.getCode();
			    andamento.setCdAndamento(retorno);
			     
				if(retorno<=0)
					Conexao.rollback(connect);
				else if (isConnectionNull)
					connect.commit();
		    }
		    
			return new Result(retorno, retorno > 0 ? "Andamento incluído com sucesso!" : "Erro ao incluir andamento!", "ANDAMENTO", andamento);
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
	
	public static Result verificaNrProcesso(String nrProcesso){
		return verificaNrProcesso(nrProcesso, null);
	}
	
	public static Result verificaNrProcesso(String nrProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT nr_processo FROM prc_processo " +
	                "WHERE nr_processo = ?");
			pstmt.setString(1, nrProcesso);
			if(pstmt.executeQuery().next()) {
				return new Result(-2, "Já existe um processo com esta numeração.");
			}
			else
				return new Result(1, "Número de processo permitido.");
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
	
	public static ResultSetMap find(String nrProcesso, String nmCliente, String nmConteiner1, 
			String nmConteiner2, String nmConteiner3, String nmContraParte, String nmCidade, 
			GregorianCalendar dtDistribuicaoInicial, GregorianCalendar dtDistribuicaoFinal, 
			String nmAdvogadoContraParte, String nmOutroInteressado, String nrInterno) {
		return find(nrProcesso, nmCliente, nmConteiner1, nmConteiner2, nmConteiner3, nmContraParte, nmCidade, 
				dtDistribuicaoInicial, dtDistribuicaoFinal, nmAdvogadoContraParte, nmOutroInteressado, nrInterno, 0, null); 
	}
	
	public static ResultSetMap find(String nrProcesso, String nmCliente, String nmConteiner1, 
			String nmConteiner2, String nmConteiner3, String nmContraParte, String nmCidade, 
			GregorianCalendar dtDistribuicaoInicial, GregorianCalendar dtDistribuicaoFinal, 
			String nmAdvogadoContraParte, String nmOutroInteressado, String nrInterno, int cdUsuario, String idProcesso) {
		return find(nrProcesso, nmCliente, nmConteiner1, nmConteiner2, nmConteiner3, nmContraParte, nmCidade, dtDistribuicaoInicial, dtDistribuicaoFinal, nmAdvogadoContraParte, nmOutroInteressado, nrInterno, cdUsuario, idProcesso, -1, -1);		
	}

	public static ResultSetMap find(String nrProcesso, String nmCliente, String nmConteiner1, 
			String nmConteiner2, String nmConteiner3, String nmContraParte, String nmCidade, 
			GregorianCalendar dtDistribuicaoInicial, GregorianCalendar dtDistribuicaoFinal, 
			String nmAdvogadoContraParte, String nmOutroInteressado, String nrInterno, int cdUsuario, 
			String idProcesso, int stProcesso, int lgImportante) {
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		if (nrProcesso != null && !nrProcesso.equals(""))
			criterios.add(new ItemComparator("nr_processo", nrProcesso, ItemComparator.LIKE_ANY, Types.VARCHAR));
		
		if (nmCliente != null && !nmCliente.equals("")) {
			criterios.add(new ItemComparator("nm_cliente", nmCliente, ItemComparator.LIKE_ANY, Types.VARCHAR));
		}
		if (nmConteiner1 != null && !nmConteiner1.equals(""))
			criterios.add(new ItemComparator("A.nm_conteiner1", nmConteiner1, ItemComparator.EQUAL, Types.VARCHAR));
		if (nmConteiner2 != null && !nmConteiner2.equals(""))
			criterios.add(new ItemComparator("A.nm_conteiner2", nmConteiner2, ItemComparator.EQUAL, Types.VARCHAR));
		if (nmConteiner3 != null && !nmConteiner3.equals("")) {
			nmConteiner3 = nmConteiner3.replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", "").replaceAll("\\_", "").replaceAll("\\ ", "");
			criterios.add(new ItemComparator("A.nm_conteiner3", nmConteiner3.toUpperCase(), ItemComparator.LIKE_ANY, Types.VARCHAR));
		}
		if (nmContraParte != null && !nmContraParte.equals(""))
			criterios.add(new ItemComparator("nm_parte_contraria", nmContraParte, ItemComparator.LIKE_ANY, Types.VARCHAR));
		if (nmCidade != null && !nmCidade.equals(""))
			criterios.add(new ItemComparator("nm_cidade", Util.limparAcentos(nmCidade), ItemComparator.LIKE_ANY, Types.VARCHAR));
		if (dtDistribuicaoInicial != null)
			criterios.add(new ItemComparator("dt_distribuicao", Util.formatDateTime(dtDistribuicaoInicial, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
		if (dtDistribuicaoFinal != null)
			criterios.add(new ItemComparator("dt_distribuicao", Util.formatDateTime(dtDistribuicaoFinal, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
		criterios.add(new ItemComparator("nrRegistros", Integer.toString(50), ItemComparator.EQUAL, Types.INTEGER));
		if (nrInterno != null && !nrInterno.equals(""))
			criterios.add(new ItemComparator("nr_interno", nrInterno, ItemComparator.EQUAL, Types.VARCHAR));
		
		if (cdUsuario>0)
			criterios.add(new ItemComparator("cdUsuario", String.valueOf(cdUsuario), ItemComparator.EQUAL, Types.VARCHAR));
		
		if (idProcesso != null && !idProcesso.equals(""))
			criterios.add(new ItemComparator("A.id_processo", idProcesso, ItemComparator.LIKE_ANY, Types.VARCHAR));
		
		if (stProcesso>=0)
			criterios.add(new ItemComparator("st_processo", Integer.toString(stProcesso), ItemComparator.IN, Types.INTEGER));
		
		if (lgImportante>=0)
			criterios.add(new ItemComparator("A.lg_importante", Integer.toString(lgImportante), ItemComparator.EQUAL, Types.INTEGER));
		
		if (nmAdvogadoContraParte != null && !nmAdvogadoContraParte.equals(""))
			criterios.add(new ItemComparator("nm_advogado_contrario", nmAdvogadoContraParte, ItemComparator.EQUAL, Types.VARCHAR));
//		if (nmOutroInteressado != null)
//			criterios.add(new ItemComparator("nm_outro", nmOutroInteressado, ItemComparator.EQUAL, Types.VARCHAR));
	
		return find(criterios, new ArrayList<String>(), null);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, new ArrayList<String>(), null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, ArrayList<String> groupBy) {
		return find(criterios, new ArrayList<String>(), null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, ArrayList<String> groupBy, Connection connect) {
		boolean isConnectionNull = connect==null;
		try	{
			if(isConnectionNull)
				connect = Conexao.conectar();

			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			String sql = "";

			LogUtils.debug("ProcessoServices.find");
			LogUtils.createTimer("PROCESSO_FIND_TIMER");
			
			//boolean lgMovimentacao = false;
			boolean lgDtAndamento = false;
			boolean lgDtLancamentoAndamento = false;
			boolean findAndamentos = false;
			boolean lgConfidenciais = false;
			boolean lgPrivados = false;
			int nrRegistros = 0;
			int cdUsuarioAndamento = 0;
			
			String cdTipoAndamento = null;
			String dtAndamentoInicial = null;
			String dtAndamentoFinal = null;
			String dtLancamentoAndamentoInicial = null;
			String dtLancamentoAndamentoFinal = null;
			
			for(int i=0; i<criterios.size(); i++)	{
				if(criterios.get(i).getColumn().equalsIgnoreCase("cd_cliente"))	{
					sql += " AND EXISTS (SELECT * FROM prc_parte_cliente PC " +
						   "             WHERE PC.cd_processo = A.cd_processo " +
						   "               AND PC.cd_pessoa = "+criterios.get(i).getValue()+")";
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("GN_PESSOA_CLIENTE"))	{
					sql += " AND EXISTS (SELECT * FROM prc_parte_cliente PC1"
							+ "				JOIN grl_pessoa PC2 on (PC1.cd_pessoa = PC2.cd_pessoa)"
							+ "			 	WHERE PC1.cd_processo = A.cd_processo"
							+ "				AND PC2.gn_pessoa = "+criterios.get(i).getValue()+")";
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("cd_adverso"))	{
					sql += " AND EXISTS (SELECT * FROM prc_outra_parte OP " +
						   "             WHERE OP.cd_processo = A.cd_processo " +
						   "               AND OP.cd_pessoa = "+criterios.get(i).getValue()+")";
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("withoutProcessoFinanceiro"))	{
					sql += " AND NOT EXISTS (SELECT * FROM prc_processo_financeiro PF"
							+ "			 	WHERE PF.cd_processo = A.cd_processo)";
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("nm_cliente"))	{
					String nmCliente = 	Util.limparTexto(criterios.get(i).getValue());
					nmCliente = nmCliente.replaceAll("[Ôô]", "O"); //o translate não funciona para "Ô"
					nmCliente = "%"+nmCliente.replaceAll(" ", "%")+"%";
					
					sql += " AND EXISTS (SELECT * FROM prc_parte_cliente PC, grl_pessoa P " +
						   "             WHERE PC.cd_processo = A.cd_processo " +
						   "               AND PC.cd_pessoa   = P.cd_pessoa " +
						   (Util.getConfManager().getIdOfDbUsed().equals("FB") ? 
								   (!nmCliente.equals("") ? " AND (P.nm_pessoa COLLATE PT_BR LIKE '"+nmCliente+"'"
								   							+ " OR P.nm_apelido COLLATE PT_BR LIKE '"+nmCliente+"'"
								   							+ " OR P.id_pessoa COLLATE PT_BR LIKE '"+nmCliente+"'))" : ")") :
   								(!nmCliente.equals("") ? 
						"               AND (TRANSLATE(nm_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '"+nmCliente+"' "+
						"				OR TRANSLATE(nm_apelido, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '"+nmCliente+"' "+
						"				OR TRANSLATE(id_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '"+nmCliente+"')) "
	   							: ")")); 	   
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("nm_parte_contraria"))	{
					String nmAdverso =	Util.limparTexto(criterios.get(i).getValue());
					nmAdverso = nmAdverso.replaceAll("[Ôô]", "O"); //o translate não funciona para "Ô"
					nmAdverso = "%"+nmAdverso.replaceAll(" ", "%")+"%";
										
					sql += " AND EXISTS (SELECT * FROM prc_outra_parte PC, grl_pessoa P " +
					   	   "             WHERE PC.cd_processo = A.cd_processo " +
					       "               AND PC.cd_pessoa   = P.cd_pessoa " +
					       (Util.getConfManager().getIdOfDbUsed().equals("FB") ? 
								   (!nmAdverso.equals("") ? " AND (P.nm_pessoa COLLATE PT_BR LIKE '"+nmAdverso+"'"
								   							+ " OR P.nm_apelido COLLATE PT_BR LIKE '"+nmAdverso+"'"
								   							+ " OR P.id_pessoa COLLATE PT_BR LIKE '"+nmAdverso+"'))" : ")") :
								   (!nmAdverso.equals("") ? 
							"               AND (TRANSLATE(nm_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '"+nmAdverso+"' "+
							"				OR TRANSLATE(nm_apelido, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '"+nmAdverso+"' "+
							"				OR TRANSLATE(id_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '"+nmAdverso+"')) "
		   							: ")")); 
					
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("sem_andamento_a"))	{
					GregorianCalendar data = new GregorianCalendar();
					data.add(GregorianCalendar.DATE, -1*Integer.parseInt(criterios.get(i).getValue()));
					sql += " AND NOT EXISTS (SELECT cd_processo FROM prc_processo_andamento PA " +
						   	   "             WHERE PA.cd_processo = A.cd_processo " +
						       "               AND PA.dt_andamento >= \'"+com.tivic.manager.util.Util.formatDate(data, "dd/MM/yyyy")+"\') ";
					
//					sql += " AND EXISTS (SELECT cd_processo FROM prc_processo PRC"
//							+ " WHERE dt_ultimo_andamento < \'"+com.tivic.manager.util.Util.formatDate(data, "MM/dd/yyyy")+"\')";
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("movimentado_nos_ultimos"))	{
					GregorianCalendar data = new GregorianCalendar();
					data.add(GregorianCalendar.DATE, -1*Integer.parseInt(criterios.get(i).getValue()));
					sql += " AND EXISTS (SELECT cd_processo FROM prc_processo_andamento PA " +
						   	   "             WHERE PA.cd_processo = A.cd_processo " +
						       "               AND PA.dt_andamento >= \'"+com.tivic.manager.util.Util.formatDate(data, "dd/MM/yyyy")+"\')";
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("lg_movimentacao"))	{
					;//lgMovimentacao = true;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("lg_cliente_autor"))	{
					int lgClienteAutor = Integer.parseInt(criterios.get(i).getValue());
					if(lgClienteAutor>=0)
						sql += " AND A.lg_cliente_autor = "+criterios.get(i).getValue()+" ";
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("nr_processo"))	{
					/*
					 * PESQUISA o NR DO PROCESSO EM 4 CAMPOS (LEGADO)
					 */
					String nrProcesso = criterios.get(i).getValue().replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", "");
                    sql += " AND (REPLACE(REPLACE(REPLACE(A.nr_processo, '.', ''), '-', ''), '/', '') iLIKE \'%"+ nrProcesso +"%\' " +
                            " OR REPLACE(REPLACE(REPLACE(A.nr_antigo, '.', ''), '-', ''), '/', '') LIKE \'%"+ nrProcesso +"%\' " +
                            " OR REPLACE(REPLACE(REPLACE(A.id_processo, '.', ''), '-', ''), '/', '') LIKE \'%"+ nrProcesso +"%\' " +
                            " OR UPPER(A.nm_conteiner3) LIKE \'%"+ nrProcesso.toUpperCase() +"%\') ";
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("A.nm_conteiner3"))	{
					String nmConteiner3 = criterios.get(i).getValue().replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", "");
					sql += " AND REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(A.nm_conteiner3, '.', ''), '-', ''), '/', ''), '_', ''), ' ', '') LIKE \'%"+ nmConteiner3 +"%\' " ;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("nr_interno"))	{
					/*
					 * PESQUISA o NR INTERNO DO PROCESSO
					 */
					String nrInterno = criterios.get(i).getValue().replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", "");
					sql += " AND REPLACE(REPLACE(REPLACE(A.nr_interno, '.', ''), '-', ''), '/', '') LIKE \'%"+ nrInterno +"%\' ";
				}
				// ANDAMENTO =====
				else if(criterios.get(i).getColumn().equalsIgnoreCase("cd_tipo_andamento"))	{
					cdTipoAndamento = criterios.get(i).getValue();
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("dt_andamento_inicial")) {
					dtAndamentoInicial = com.tivic.manager.util.Util.formatDate(Util.stringToCalendar(criterios.get(i).getValue()), "dd/MM/yyyy")+" 00:00:00";
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("dt_andamento_final")) {
					dtAndamentoFinal = com.tivic.manager.util.Util.formatDate(Util.stringToCalendar(criterios.get(i).getValue()), "dd/MM/yyyy")+" 23:59:59";					
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("dt_lancamento_andamento_inicial")) {
					dtLancamentoAndamentoInicial = com.tivic.manager.util.Util.formatDate(Util.stringToCalendar(criterios.get(i).getValue()), "dd/MM/yyyy")+" 00:00:00";
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("dt_lancamento_andamento_final")) {
					dtLancamentoAndamentoFinal = com.tivic.manager.util.Util.formatDate(Util.stringToCalendar(criterios.get(i).getValue()), "dd/MM/yyyy")+" 23:59:59";					
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("cd_usuario_andamento"))	{
					sql += " AND EXISTS ("
							+ " SELECT UA.cd_andamento"
							+ " FROM prc_processo_andamento UA "
							+ " WHERE A.cd_processo = UA.cd_processo "
							+ " AND UA.cd_usuario="+Integer.parseInt(criterios.get(i).getValue())
							+ " ) ";
					cdUsuarioAndamento = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					i--;
				}
				// ===============				
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nrRegistros")) {
					nrRegistros = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("findAndamentos")) {
					findAndamentos = Boolean.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgConfidenciais")) {
					lgConfidenciais = Boolean.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgPrivados")) {
					lgPrivados = Boolean.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdUsuario")) {
					/**
					 * REALIZA A PESQUISA DE PROCESSO BASEADO NOS GRUPOS DE PROCESSO QUE O USUÁRIO PODE ACESSAR
					 * Caso o critério cdUsuario seja passado para o método de busca é então
					 * realizado um teste para verificar o acesso aos grupos.
					 */
					
					int cdUsuario = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
					
					if(cdUsuario>0 &&  UsuarioGrupoServices.hasGrupoProcesso(cdUsuario)) {
						
						ResultSetMap rsm1 = UsuarioGrupoServices.getAllByUsuario(cdUsuario);
						String cdsGrupos = "";
						
						for (int j = 0; rsm1.next(); j++) {
							cdsGrupos += (((j>0) ? "," : "")+rsm1.getInt("CD_GRUPO_PROCESSO"));
						}
						
						criterios.add(new ItemComparator("A.cd_grupo_processo", cdsGrupos, ItemComparator.IN, Types.NULL));
						
					}
				}
				
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nm_cidade")) {
					String nmCidade = Util.limparTexto(criterios.get(i).getValue());
					nmCidade = "%"+nmCidade.replaceAll(" ", "%")+"%";
					
					sql += (Util.getConfManager().getIdOfDbUsed().equals("FB") ? 
							   (!nmCidade.equals("") ? " AND N.nm_cidade COLLATE PT_BR LIKE '"+nmCidade+"'" : "") :
								   (!nmCidade.equals("") ? " AND TRANSLATE(N.nm_cidade, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '"+nmCidade+"' " : ""));
					
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nm_advogado_contrario")) {
					String nmAdvogadoContrario = Util.limparTexto(criterios.get(i).getValue());
					nmAdvogadoContrario = "%"+nmAdvogadoContrario.replaceAll(" ", "%")+"%";
					
					sql += (Util.getConfManager().getIdOfDbUsed().equals("FB") ? 
							   (!nmAdvogadoContrario.equals("") ? " AND I.nm_pessoa COLLATE PT_BR LIKE '"+nmAdvogadoContrario+"'" : "") :
								   (!nmAdvogadoContrario.equals("") ? " AND TRANSLATE(I.nm_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '"+nmAdvogadoContrario+"' " : ""));
					
					criterios.remove(i);
					i--;
				}
				else
					crt.add(criterios.get(i));
			}
			
			//MONTANDO CRITERIOS DE ANDAMENTO >>
			//se algum critério de andamento estiver preenchido
			if(cdTipoAndamento!=null || dtAndamentoInicial!=null || dtAndamentoFinal!=null || dtLancamentoAndamentoInicial!=null || dtLancamentoAndamentoFinal!=null || cdUsuarioAndamento>0) {
				sql += " AND EXISTS ("
					 + " 	SELECT PA.* FROM prc_processo_andamento PA"
					 + " 	WHERE A.cd_processo = PA.cd_processo "
					 + (cdTipoAndamento!=null?" AND PA.cd_tipo_andamento IN ("+cdTipoAndamento+")":"")
					 + (dtAndamentoInicial!=null?" AND PA.dt_andamento >= '"+dtAndamentoInicial+"'":"")
					 + (dtAndamentoFinal!=null?" AND PA.dt_andamento <= '"+dtAndamentoFinal+"'":"")
					 + (dtLancamentoAndamentoInicial!=null?" AND PA.dt_lancamento >= '"+dtLancamentoAndamentoInicial+"'":"")
					 + (dtLancamentoAndamentoFinal!=null?" AND PA.dt_lancamento <= '"+dtLancamentoAndamentoFinal+"'":"")
					 + (cdUsuarioAndamento>0 ? " AND PA.cd_usuario = " + cdUsuarioAndamento : "")
					 + ")";
			}
			
			String groups = "";
			String fields = " A.*, " +
					 		" 	CAST(A.vl_processo AS double precision) AS ds_vl_processo, " +
							" 	CAST(A.vl_sentenca AS double precision) AS ds_vl_sentenca," +
							" 	CAST(A.vl_acordo AS double precision) AS ds_vl_acordo," +
							" B.nm_juizo, B.nm_juizo AS nm_orgao_judicial, B.id_juizo, B.id_juizo AS id_orgao_judicial, A.tp_perda, " +
					        " C.nm_tribunal, C.nm_tribunal AS nm_comarca, C.id_tribunal AS id_comarca, " +
					        " D.nm_tipo_processo, D.nm_parte, D.nm_contra_parte, "+
			                " D.nm_outro_interessado, E.nm_area_direito, F.nm_orgao, G.nm_tipo_situacao, "+
			                " H.nm_pessoa AS nm_advogado, I.cd_pessoa AS cd_advogado_contrario, I.nm_pessoa AS nm_advogado_contrario, "+
			                " J.nm_pessoa AS nm_advogado_titular, Q.nm_pessoa AS nm_responsavel_arquivo, "+
			                " K.nm_pessoa AS nm_juiz, "+
			                " \' \' AS nm_juiz, "+
			                " L.nm_grupo_processo, M.nm_pessoa AS nm_oficial_justica, "+
			                " N.nm_cidade AS nm_cidade_orgao, N.nm_cidade, N1.cd_estado, N1.sg_estado, O.nm_tipo_pedido, P.nm_tipo_objeto, " +
			                " R1.nm_pessoa AS nm_usuario_cadastro, T.nm_grupo AS NM_GRUPO_TRABALHO "
//			                "(SELECT nm_cliente FROM get_clientes(A.cd_processo)) AS nm_cliente, "+
//			                "(SELECT nm_contra_parte FROM get_contra_parte(A.cd_processo)) AS nm_adverso, "+

			                +" ,(SELECT MAX(cd_andamento) FROM prc_processo_andamento X "+
			                "  WHERE X.cd_processo  = A.cd_processo "+
			                "    AND X.dt_andamento = A.dt_ultimo_andamento) as cd_andamento  "
			                ;
			
			// Processa agrupamentos enviados em groupBy
			String [] retorno = com.tivic.manager.util.Util.getFieldsAndGroupBy(groupBy, fields, groups,
					                                                     "SUM(A.VL_CONTRATO) AS VL_CONTRATO, COUNT(*) AS QT_CONTRATO");
			fields = retorno[0];
			groups = retorno[1];
			
			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(nrRegistros, 0);
			
			String sqlFinal = "SELECT "+ sqlLimit[0] +
					fields+
	                "FROM prc_processo A "+
	                "LEFT OUTER JOIN prc_juizo          	B ON (A.cd_juizo = B.cd_juizo) "+
	                "LEFT OUTER JOIN prc_tribunal       	C ON (A.cd_tribunal = C.cd_tribunal) "+
	                "LEFT OUTER JOIN prc_tipo_processo  	D ON (A.cd_tipo_processo = D.cd_tipo_processo) "+
	                "LEFT OUTER JOIN prc_area_direito   	E ON (D.cd_area_direito = E.cd_area_direito) "+
	                "LEFT OUTER JOIN prc_orgao          	F ON (A.cd_orgao = F.cd_orgao) "+
	                "LEFT OUTER JOIN prc_tipo_situacao  	G ON (A.cd_tipo_situacao = G.cd_tipo_situacao) "+
	                "LEFT OUTER JOIN grl_pessoa         	H ON (A.cd_advogado = H.cd_pessoa) "+
	                "LEFT OUTER JOIN grl_pessoa         	I ON (A.cd_advogado_contrario = I.cd_pessoa) "+
	                "LEFT OUTER JOIN grl_pessoa         	J ON (A.cd_advogado_titular = J.cd_pessoa) "+
	                "LEFT OUTER JOIN grl_pessoa				K ON (A.cd_juiz = K.cd_pessoa)"+
	                "LEFT OUTER JOIN prc_grupo_processo 	L ON (A.cd_grupo_processo = L.cd_grupo_processo) "+
	                "LEFT OUTER JOIN grl_pessoa         	M ON (A.cd_oficial_justica = M.cd_pessoa) "+
	                "LEFT OUTER JOIN grl_cidade         	N ON (A.cd_cidade = N.cd_cidade) "+
					"LEFT OUTER JOIN grl_estado 			N1 ON (N.cd_estado = N1.cd_estado) " +
	                "LEFT OUTER JOIN prc_tipo_pedido    	O ON (A.cd_tipo_pedido = O.cd_tipo_pedido) "+
	                "LEFT OUTER JOIN prc_tipo_objeto   	 	P ON (A.cd_tipo_objeto = P.cd_tipo_objeto) "+
	                "LEFT OUTER JOIN grl_pessoa        	 	Q ON (A.cd_responsavel_arquivo = Q.cd_pessoa) "+
	                "LEFT OUTER JOIN seg_usuario 			R ON (R.cd_usuario = A.cd_usuario_cadastro) " +
	                "LEFT OUTER JOIN grl_pessoa 			R1 ON (R1.cd_pessoa = R.cd_pessoa) " +
//	                "LEFT OUTER JOIN prc_contrato 			S ON (S.cd_processo = A.cd_processo) " +
	                "LEFT OUTER JOIN agd_grupo				T ON (A.cd_grupo_trabalho = T.cd_grupo) "+ 
	                //(lgConfidenciais || lgPrivados ? "LEFT OUTER JOIN prc_processo_andamento U ON (A.cd_processo = U.cd_processo) " : "") +
	                "WHERE 1=1 "+sql;
			
			String orderBy = groups+sqlLimit[1];
			
			ResultSetMap rsm =  Search.find(sqlFinal, orderBy, crt, connect, isConnectionNull);
			
			/**
			 * INJECAO DE DADOS ADICIONAIS NO RESULTADO
			 */
			LogUtils.logTimer("PROCESSO_FIND_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
			LogUtils.debug("ProcessoServices.find: Iniciando injeção de dados adicionais...");
			
			// Acrescentando informação dos clientes e adversos
			while(rsm.next()) {
				
				if(rsm.getString("DS_VL_SENTENCA") != null && rsm.getString("DS_VL_SENTENCA").equals("NaN")) {
					rsm.setValueToField("DS_VL_SENTENCA", 0);
				}
				
				if(rsm.getString("DS_VL_ACORDO") != null && rsm.getString("DS_VL_ACORDO").equals("NaN")) {
					rsm.setValueToField("DS_VL_ACORDO", 0);
				}
				
				String nmCliente = "";
				String nmAdverso = "";
				
				if(rsm.getInt("CD_PROCESSO")>0) {
//					nmCliente = ProcessoServices.getClientes(rsm.getInt("CD_PROCESSO"), connect);
//					nmAdverso = ProcessoServices.getAdversos(rsm.getInt("CD_PROCESSO"), connect);
					
					ResultSetMap rsmSentenca = ProcessoSentencaServices.getAllByProcesso(rsm.getInt("CD_PROCESSO"));
					if(rsmSentenca.next()) { //sentenca mais recente
						rsm.setValueToField("cd_sentenca", rsmSentenca.getInt("cd_sentenca"));
						rsm.setValueToField("tp_sentenca", rsmSentenca.getInt("tp_sentenca"));
						rsm.setValueToField("dt_sentenca", Util.convTimestampToCalendar(rsmSentenca.getTimestamp("dt_sentenca")));
						rsm.setValueToField("vl_sentenca", rsmSentenca.getDouble("vl_sentenca"));
						rsm.setValueToField("vl_acordo", rsmSentenca.getDouble("vl_acordo"));
						rsm.setValueToField("vl_total_sentenca", rsmSentenca.getDouble("vl_total"));
						
						rsm.setValueToField("DS_ULTIMA_SENTENCA", tipoSentenca[rsmSentenca.getInt("tp_sentenca")].toUpperCase());
					}
					
					ResultSetMap rsmClientes = getParteCliente(rsm.getInt("CD_PROCESSO"));
					ResultSetMap rsmAdversos = getOutraParte(rsm.getInt("CD_PROCESSO"));
					
					ResultSetMap rsmContratos = getNrContrato(rsm.getInt("CD_PROCESSO"));
					rsm.setValueToField("NR_CONTRATO", Util.join(rsmContratos, "NR_CONTRATO"));
					
					while(rsmClientes.next()){
						nmCliente += rsmClientes.getString("NM_APELIDO") !=null ? rsmClientes.getString("NM_APELIDO") : rsmClientes.getString("ID_PESSOA") !=null ? rsmClientes.getString("ID_PESSOA") : rsmClientes.getString("NM_PESSOA");
						
						if(rsmClientes.hasMore())
							nmCliente += ", ";
						
						if(rsmClientes.getPosition()==0) {
							rsm.setValueToField("CD_CLIENTE1", rsmClientes.getString("CD_PESSOA"));
							rsm.setValueToField("NM_CLIENTE1", rsmClientes.getString("NM_PESSOA"));
							rsm.setValueToField("NR_CPF_CLIENTE1", rsmClientes.getString("NR_CPF"));
						}
					}
					
					while(rsmAdversos.next()){
						nmAdverso += rsmAdversos.getString("NM_APELIDO") !=null ? rsmAdversos.getString("NM_APELIDO") : rsmAdversos.getString("ID_PESSOA") !=null ? rsmAdversos.getString("ID_PESSOA") : rsmAdversos.getString("NM_PESSOA");
						
						if(rsmAdversos.hasMore())
							nmAdverso += ", ";
						
						if(rsmAdversos.getPosition()==0) {
							rsm.setValueToField("CD_ADVERSO1", rsmAdversos.getString("CD_PESSOA"));
							rsm.setValueToField("NM_ADVERSO1", rsmAdversos.getString("NM_PESSOA"));
							rsm.setValueToField("NR_CPF_ADVERSO1", rsmAdversos.getString("NR_CPF"));
						}
					}
					
					rsm.setValueToField("NM_CLIENTE", nmCliente);
					rsm.setValueToField("NM_ADVERSO", nmAdverso);
					rsm.setValueToField("NM_ST_PROCESSO", (rsm.getInt("ST_PROCESSO")>=0?situacaoProcesso[rsm.getInt("ST_PROCESSO")]:null));
					rsm.setValueToField("NM_TP_PERDA", (rsm.getInt("TP_PERDA")>=0?tipoPerda[rsm.getInt("TP_PERDA")]:null));
					
					
					if(findAndamentos) {
						Connection connAux = Conexao.conectar();
						int cdProcesso = rsm.getInt("CD_PROCESSO");
						
						PreparedStatement pstmt = connAux.prepareStatement("SELECT A.cd_andamento, A.dt_andamento, B.nm_tipo_andamento, A.txt_andamento"
																		+ " FROM prc_processo_andamento A"
																		+ " LEFT OUTER JOIN prc_tipo_andamento B ON (A.cd_tipo_andamento=B.cd_tipo_andamento)"
																		+ " WHERE A.cd_processo = "+cdProcesso
																		+ (!lgConfidenciais ? "AND A.tp_visibilidade<>"+ProcessoAndamentoServices.TP_VISIBILIDADE_CONFIDENCIAL : "")
																		+ (!lgPrivados ? "AND A.tp_visibilidade<>"+ProcessoAndamentoServices.TP_VISIBILIDADE_PRIVADO : "")
																		+ "ORDER BY A.dt_andamento DESC"
						);
						
						ResultSetMap rsmAndamentos = new ResultSetMap(pstmt.executeQuery());
						if(rsmAndamentos.next()) {
							rsm.setValueToField("ANDAMENTOS", rsmAndamentos.getLines());
							rsm.setValueToField("DS_ULTIMO_ANDAMENTO", rsmAndamentos.getString("nm_tipo_andamento")+" ("+Util.formatDate(rsmAndamentos.getGregorianCalendar("dt_andamento"), "dd/MM/yyyy")+")");
						}
						Conexao.desconectar(connAux);
					}
					
					rsm.setValueToField("NM_ST_LIMINAR", situacaoLiminar[rsm.getInt("st_liminar")].toUpperCase());
					rsm.setValueToField("DS_FORO", 
							(rsm.getString("nr_juizo")!=null ? rsm.getString("nr_juizo")+" " : "") + 
							(rsm.getString("nm_juizo")!=null ? rsm.getString("nm_juizo") : "") + 
							(rsm.getString("nm_tribunal")!=null ? ", "+rsm.getString("nm_tribunal") : "") + 
							(rsm.getInt("tp_instancia", -1)>-1 ? " - "+tipoInstancia[rsm.getInt("tp_instancia")].toUpperCase() : "") + 
							(rsm.getString("nm_cidade")!=null ? " - "+rsm.getString("nm_cidade")+"/"+rsm.getString("sg_estado") : "") 
						);
				}
			}
			rsm.beforeFirst();
			
			LogUtils.logTimer("PROCESSO_FIND_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
			LogUtils.destroyTimer("PROCESSO_FIND_TIMER");
			return rsm;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.find: " + e);
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}

	}

	public static Result remove(int cdProcesso){
		return remove(cdProcesso, false, null);
	}
	
	public static Result remove(int cdProcesso, boolean cascade){
		return remove(cdProcesso, cascade, null);
	}
	
	public static Result remove(int cdProcesso, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
						
			if(cascade){				
				retorno = deleteAgendas(cdProcesso, 0, connect).getCode();
				if(retorno>0){
					retorno = deleteMensagens(cdProcesso, connect).getCode();
				}
				if(retorno>0){
					retorno = deleteAtendimentos(cdProcesso, 0, connect).getCode();
				}
				if(retorno>0){
					retorno = deleteGarantias(cdProcesso, 0, connect).getCode();
				}
				if(retorno>0){
					retorno = deleteCalculos(cdProcesso, 0, connect).getCode();
				}
				if(retorno>0){
					retorno = deleteContratos(cdProcesso, 0, connect).getCode();
				}
				if(retorno>0){
					retorno = deleteListaProcesso(cdProcesso, 0, connect).getCode();
				}
				if(retorno>0){
					retorno = deleteAdverso(cdProcesso, 0, connect).getCode();
				}
				if(retorno>0){
					retorno = deleteOutrosInteressados(cdProcesso, 0, connect).getCode();
				}
				if(retorno>0){
					retorno = deleteParteCliente(cdProcesso, 0, connect).getCode();
				}
				if(retorno>0) {
					// set NULL para desvincular o processo dos outros
					PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_processo set cd_processo_principal = NULL "
																	+ " WHERE cd_processo_principal = "+cdProcesso);
					retorno = pstmt.executeUpdate();
				}
				if(retorno>0) {
					PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_lista_processo "
																	+ " WHERE cd_processo = "+cdProcesso);
					retorno = pstmt.executeUpdate();
				}
				if(retorno>0){
					retorno = deleteOutrosAdvogados(cdProcesso, 0, connect).getCode();
				}
				if(retorno>0){
					retorno = deleteAndamentos(cdProcesso, 0, connect).getCode();
				}
				if(retorno>0){
					retorno = deleteArquivos(cdProcesso, 0, connect).getCode();
				}
				if(retorno>0){
					retorno = deleteEventoFinanceiro(cdProcesso, 0, connect).getCode();
				}
				if(retorno>0){
					retorno = deleteProcessoInstancia(cdProcesso, connect).getCode();
				}
				if(retorno>0){
					retorno = deleteRecortes(cdProcesso, connect).getCode();
				}
				if(retorno>0){
					retorno = deleteTestemunhas(cdProcesso, 0, connect).getCode();
				}
				if(retorno>0){
					retorno = deleteDocumentos(cdProcesso, connect).getCode();
				}
				if(retorno>0){
					retorno = deleteTipoPedido(cdProcesso, 0, connect).getCode();
				}
				retorno = retorno<0 ? retorno : 1;
			}
				
			LogUtils.debug("cascade: "+cascade);
			LogUtils.debug("retorno: "+retorno);
			if(!cascade || retorno>0) {
				LogUtils.debug("DELETE PROCESSO");
				retorno = ProcessoDAO.delete(cdProcesso, connect);
			}
			LogUtils.debug("retorno2: "+retorno);
			
			if(retorno>0) {
				notificar(cdProcesso, 0, connect);
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este processo está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Processo excluído com sucesso!");
		}
		catch(Exception e){
			System.out.println("Erro! ProcessoServices.remove: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir processo!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static HashMap<String, Object> getProcessoRegister(int cdProcesso) {
		return getProcessoRegister(cdProcesso, null);
	}

	public static HashMap<String, Object> getProcessoRegister(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {

			pstmt = connect.prepareStatement("SELECT A.*, B.nm_juizo, B.nm_juizo AS nm_orgao_judicial, B.id_juizo, B.id_juizo AS id_orgao_judicial, " +
					        " C.nm_tribunal, C.nm_tribunal AS nm_comarca, C.id_tribunal AS id_comarca, " +
					        " D.nm_tipo_processo, E.NM_CIDADE, E1.SG_ESTADO, F.nm_pessoa AS nm_advogado_contrario " +
//					        "(SELECT nm_cliente FROM get_clientes(A.cd_processo)) AS nm_cliente, "+
//			                "(SELECT nm_contra_parte FROM get_contra_parte(A.cd_processo)) AS nm_adverso "+
					        " FROM PRC_PROCESSO A "+
			                " LEFT OUTER JOIN prc_juizo          B ON (A.cd_juizo = B.cd_juizo) "+
			                " LEFT OUTER JOIN prc_tribunal       C ON (A.cd_tribunal = C.cd_tribunal) "+
			                " LEFT OUTER JOIN prc_tipo_processo  D ON (A.cd_tipo_processo = D.cd_tipo_processo) "+
			                " LEFT OUTER JOIN grl_cidade E ON (A.cd_cidade = E.cd_cidade) " +
							" LEFT OUTER JOIN grl_estado E1 ON (E.cd_estado = E1.cd_estado) " +
			                " LEFT OUTER JOIN grl_pessoa F ON (A.cd_advogado_contrario = F.cd_pessoa)"+
					        " WHERE CD_PROCESSO=?");
			pstmt.setInt(1, cdProcesso);
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
					
					int cdClientePreposto = ParametroServices.getValorOfParametroAsInteger("CD_CLIENTE_PREPOSTO", 0);
					if(cdClientePreposto>0) {
						ResultSetMap rsmCliente = getParteCliente(rsm.getInt("CD_PROCESSO"), connect);
						while(rsmCliente.next()) {
							if(rsmCliente.getInt("cd_pessoa")==cdClientePreposto) {
								rsm.setValueToField("CD_CLIENTE", cdClientePreposto);
								break;
							}
						}
					}
				}
			}
			rsm.beforeFirst();
			
			if(rsm.next()){
				return rsm.getRegister();
			}
			else{
				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getProcessoRegister: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Processo getProcessoByNrProcesso(String nrProcesso) {
		return getProcessoByNrProcesso(nrProcesso, null);
	}

	public static Processo getProcessoByNrProcesso(String nrProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {

			nrProcesso = nrProcesso.replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", "");
			
			String sql = "SELECT A.cd_processo " +
			        " FROM PRC_PROCESSO A "+
	                " WHERE (REPLACE(REPLACE(REPLACE(A.nr_processo, '.', ''), '-', ''), '/', '') LIKE \'%"+ nrProcesso +"%\' " +
					" OR REPLACE(REPLACE(REPLACE(A.nr_antigo, '.', ''), '-', ''), '/', '') LIKE \'%"+ nrProcesso +"%\' " +
					" OR REPLACE(REPLACE(REPLACE(A.id_processo, '.', ''), '-', ''), '/', '') LIKE \'%"+ nrProcesso +"%\' " +
					" OR UPPER(A.nm_conteiner3) LIKE \'%"+ nrProcesso.toUpperCase() +"%\') ";

			pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next()){
				return ProcessoDAO.get(rsm.getInt("CD_PROCESSO"), connect);
			}
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getProcessoByNrProcesso: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result insertParteCliente(int cdProcesso, int cdPessoa){
		return insertParteCliente(cdProcesso, cdPessoa, null);
	}
	
	public static Result insertParteCliente(int cdProcesso, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_pessoa FROM prc_parte_cliente WHERE cd_processo = ? AND cd_pessoa = ?");
			pstmt.setInt(1, cdProcesso);
			pstmt.setInt(2, cdPessoa);
			
			if(pstmt.executeQuery().next())
				return new Result(-2, "Cliente já existe no processo");
			
			pstmt = connect.prepareStatement("INSERT INTO prc_parte_cliente (cd_processo,cd_pessoa) VALUES (?,?)");
			pstmt.setInt(1, cdProcesso);
			pstmt.setInt(2, cdPessoa);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.insertParteCliente: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao inserir parte cliente");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getParteCliente(int cdProcesso){
		return getParteCliente(cdProcesso, null);
	}
	
	public static ResultSetMap getParteCliente(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
		
			String sql = "SELECT A.cd_processo, A.cd_pessoa, B.id_pessoa, B.nm_pessoa, B.nm_apelido, B.gn_pessoa,"
					   + " C.nr_cpf, D.nr_cnpj, B.gn_pessoa, B.st_cadastro, C.lg_documento_ausente as lg_cpf_ausente, D.lg_documento_ausente as lg_cnpj_ausente "
					   + " FROM prc_parte_cliente A"
					   + " JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa)"
					   + " LEFT OUTER JOIN grl_pessoa_fisica C ON (A.cd_pessoa = C.cd_pessoa)"
					   + " LEFT OUTER JOIN grl_pessoa_juridica D ON (A.cd_pessoa = D.cd_pessoa)"
					   + " WHERE A.cd_processo = " + cdProcesso;
			
			PreparedStatement pstmt = connect.prepareStatement(sql);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getParteCliente: " + e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static String getClientes(int cdProcesso){
		return getClientes(cdProcesso, null);
	}
	
	public static String getClientes(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
		
			ResultSetMap rsmClientes = ProcessoServices.getParteCliente(cdProcesso, connect);
			
			String nmCliente = "";
			while(rsmClientes.next()){
				nmCliente += rsmClientes.getString("NM_APELIDO") !=null ? rsmClientes.getString("NM_APELIDO") : rsmClientes.getString("ID_PESSOA") !=null ? rsmClientes.getString("ID_PESSOA") : rsmClientes.getString("NM_PESSOA");
				
				if(rsmClientes.hasMore())
					nmCliente += ", ";
			}
			
			return nmCliente;
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getClientes: " + e);
			return "";
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static String getAdversos(int cdProcesso){
		return getAdversos(cdProcesso, null);
	}
	
	public static String getAdversos(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
		
			ResultSetMap rsmAdverso = ProcessoServices.getOutraParte(cdProcesso, connect);
			
			String nmAdverso = "";
			while(rsmAdverso.next()){
				nmAdverso += rsmAdverso.getString("NM_APELIDO") !=null ? rsmAdverso.getString("NM_APELIDO") : rsmAdverso.getString("ID_PESSOA") !=null ? rsmAdverso.getString("ID_PESSOA") : rsmAdverso.getString("NM_PESSOA");
				
				if(rsmAdverso.hasMore())
					nmAdverso += ", ";
			}
			
			return nmAdverso;
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getAdversos: " + e);
			return "";
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result deleteParteCliente(int cdProcesso, int cdPessoa){
		return deleteParteCliente(cdProcesso, cdPessoa, null);
	}
	
	public static Result deleteParteCliente(int cdProcesso, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_parte_cliente " +
					" WHERE cd_processo = ? " +
					((cdPessoa>0)?"  AND cd_pessoa="+cdPessoa:""));
			pstmt.setInt(1, cdProcesso);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno<0 ? retorno : 1);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.deleteParteCliente: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir parte cliente no processo");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result insertAdverso(int cdProcesso, int cdPessoa){
		return insertAdverso(cdProcesso, cdPessoa, null);
	}
	
	public static Result insertAdverso(int cdProcesso, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_pessoa FROM prc_outra_parte WHERE cd_processo = ? AND cd_pessoa = ?");
			pstmt.setInt(1, cdProcesso);
			pstmt.setInt(2, cdPessoa);
			
			if(pstmt.executeQuery().next())
				return new Result(-2, "Adverso já existe no processo");
			
			pstmt = connect.prepareStatement("INSERT INTO prc_outra_parte (cd_processo,cd_pessoa) VALUES (?,?)");
			pstmt.setInt(1, cdProcesso);
			pstmt.setInt(2, cdPessoa);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.insertAdverso: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao inserir adverso");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result insertProcessoVinculado(Processo processo){
		return insertProcessoVinculado(processo, null);
	}
	
	public static Result insertProcessoVinculado(Processo processo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			int retorno = ProcessoDAO.update(processo, connect);
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, "Processo vinculado com sucesso.");
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.insertProcessoVinculado: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao inserir processo vinculado");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result removeProcessoVinculado(int cdProcesso){
		return removeProcessoVinculado(cdProcesso, null);
	}
	
	public static Result removeProcessoVinculado(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Processo processo = ProcessoDAO.get(cdProcesso, connect);
			
			if(processo==null) {
				return new Result(-1, "Erro ao vincular processo.");
			}
			
			processo.setCdProcessoPrincipal(0);
			int retorno = ProcessoDAO.update(processo, connect);
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, "Processo removido com sucesso.");
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.removeProcessoVinculado: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao remover processo vinculado");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getOutraParte(int cdProcesso){
		return getOutraParte(cdProcesso, null);
	}
	
	public static ResultSetMap getOutraParte(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			String sql = "SELECT A.cd_processo, A.cd_pessoa, B.id_pessoa, B.nm_pessoa, B.nm_apelido,"
					   + " C.nr_cpf, D.nr_cnpj, B.gn_pessoa, B.st_cadastro, C.lg_documento_ausente as lg_cpf_ausente, D.lg_documento_ausente as lg_cnpj_ausente "
					   + " FROM prc_outra_parte A"
					   + " JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa)"
					   + " LEFT OUTER JOIN grl_pessoa_fisica C ON (A.cd_pessoa = C.cd_pessoa)"
					   + " LEFT OUTER JOIN grl_pessoa_juridica D ON (A.cd_pessoa = D.cd_pessoa)"
					   + " WHERE A.cd_processo = " + cdProcesso;
		
			PreparedStatement pstmt = connect.prepareStatement(sql);
			
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getOutraParte: " + e);

			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getNrContrato(int cdProcesso){
		return getNrContrato(cdProcesso, null);
	}
	
	public static ResultSetMap getNrContrato(int cdProcesso, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT B.nr_contrato from prc_processo A" + 
					" LEFT OUTER JOIN prc_contrato B on (A.cd_processo=B.cd_processo)" + 
					" WHERE B.cd_processo = " + cdProcesso);
			
			return new ResultSetMap(pstmt.executeQuery());

		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getNrContrato: " + e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result deleteAdverso(int cdProcesso, int cdPessoa){
		return deleteAdverso(cdProcesso, cdPessoa, null);
	}
	
	public static Result deleteAdverso(int cdProcesso, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_outra_parte " +
					" WHERE cd_processo = ? " +
					((cdPessoa>0)?"  AND cd_pessoa="+cdPessoa:""));
			pstmt.setInt(1, cdProcesso);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno<0 ? retorno : 1);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.deleteAdverso: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir parte cliente no processo");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result insertOutroAdvogado(int cdProcesso, int cdPessoa){
		return insertOutroAdvogado(cdProcesso, cdPessoa, null);
	}
	
	public static Result insertOutroAdvogado(int cdProcesso, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_pessoa FROM prc_processo_advogado WHERE cd_processo = ? AND cd_pessoa = ?");
			pstmt.setInt(1, cdProcesso);
			pstmt.setInt(2, cdPessoa);
			
			if(pstmt.executeQuery().next())
				return new Result(-2, "Advogado já existe no processo");
			
			pstmt = connect.prepareStatement("INSERT INTO prc_processo_advogado (cd_processo,cd_pessoa) VALUES (?,?)");
			pstmt.setInt(1, cdProcesso);
			pstmt.setInt(2, cdPessoa);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.insertOutroAdvogado: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao inserir Advogado");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getOutrosAdvogados(int cdProcesso){
		return getOutrosAdvogados(cdProcesso, null);
	}
	
	public static ResultSetMap getOutrosAdvogados(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nm_pessoa FROM prc_processo_advogado A, grl_pessoa B " +
						                     "WHERE A.cd_pessoa = B.cd_pessoa " +
						                     "  AND A.cd_processo = "+cdProcesso);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getOutrosAdvogados: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result deleteOutrosAdvogados(int cdProcesso, int cdPessoa){
		return deleteOutrosAdvogados(cdProcesso, cdPessoa, null);
	}
	
	public static Result deleteOutrosAdvogados(int cdProcesso, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_processo_advogado " +
					" WHERE cd_processo = ? " +
					((cdPessoa>0)?"  AND cd_pessoa="+cdPessoa:""));
			pstmt.setInt(1, cdProcesso);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno<0 ? retorno : 1);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.deleteOutrosAdvogados: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir outros advogados no processo");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result insertTipoPedido(int cdProcesso, int cdTipoPedido){
		return insertTipoPedido(cdProcesso, cdTipoPedido, null);
	}
	
	public static Result insertTipoPedido(int cdProcesso, int cdTipoPedido, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_tipo_pedido FROM prc_processo_tipo_pedido WHERE cd_processo = ? AND cd_tipo_pedido = ?");
			pstmt.setInt(1, cdProcesso);
			pstmt.setInt(2, cdTipoPedido);
			
			if(pstmt.executeQuery().next())
				return new Result(-2, "Tipo de pedido já existe no processo");
			
			pstmt = connect.prepareStatement("INSERT INTO prc_processo_tipo_pedido (cd_processo,cd_tipo_pedido) VALUES (?,?)");
			pstmt.setInt(1, cdProcesso);
			pstmt.setInt(2, cdTipoPedido);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.insertTipoPedido: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao inserir tipo de pedido no processo");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result deleteTipoPedido(int cdProcesso, int cdTipoPedido){
		return deleteTipoPedido(cdProcesso, cdTipoPedido, null);
	}
	
	public static Result deleteTipoPedido(int cdProcesso, int cdTipoPedido, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_processo_tipo_pedido " +
					" WHERE cd_processo = ? " +
					((cdTipoPedido>0)?"  AND cd_tipo_pedido="+cdTipoPedido:""));
			pstmt.setInt(1, cdProcesso);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno<0 ? retorno : 1, "Tipo de Pedido escluído com sucesso.");
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.deleteTipoPedido: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir tipo de pedido no processo");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getTipoPedido(int cdProcesso){
		return getTipoPedido(cdProcesso, null);
	}
	
	public static ResultSetMap getTipoPedido(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, nm_tipo_pedido FROM prc_processo_tipo_pedido A, prc_tipo_pedido B " +
						                     "WHERE A.cd_tipo_pedido = B.cd_tipo_pedido " +
						                     "  AND A.cd_processo    = "+cdProcesso);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getTipoPedido: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result getTipoSituacao(int cdProcesso) {
		return getTipoSituacao(cdProcesso, null);
	}
	
	public static Result getTipoSituacao(int cdProcesso, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
		
			Processo processo = ProcessoDAO.get(cdProcesso, connect);
			if(processo==null) {
				return new Result(-1, "Erro! ProcessoServices.getTipoSituacao: Processo é nulo.");
			}
			
			Result result = new Result(1, "");
			result.addObject("CD_TIPO_SITUACAO", processo.getCdTipoSituacao());
			
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getTipoSituacao: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAgendas(int cdProcesso){
		return getAgendas(cdProcesso, 0, null);
	}
	
	public static ResultSetMap getAgendas(int cdProcesso, int cdPessoaOrgao){
		return getAgendas(cdProcesso, cdPessoaOrgao, null);
	}
	
	public static ResultSetMap getAgendas(int cdProcesso, int cdPessoaOrgao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			PreparedStatement pstmt = connect.prepareStatement(
					" SELECT A.*, "
					+ " B.nm_tipo_prazo, B.tp_agenda_item, "
					+ " C.nm_pessoa, C.nm_email, "
					+ " D.tp_contratacao, " 
					+ " E.nm_pessoa AS nm_usuario_cadastro,"
					+ " F.cd_grupo AS cd_grupo_trabalho_responsavel, F.nm_grupo AS nm_grupo_trabalho_responsavel, F.nm_email AS nm_email_grupo_trabalho "
					+ " FROM agd_agenda_item A " 
					+ " LEFT OUTER JOIN prc_tipo_prazo B ON (A.cd_tipo_prazo = B.cd_tipo_prazo) " 
					+ " LEFT OUTER JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa)" 
					+ " LEFT OUTER JOIN prc_orgao D ON (A.cd_pessoa = D.cd_pessoa)" 
					+ " LEFT OUTER JOIN seg_usuario E1 ON (A.cd_usuario = E1.cd_usuario)" 
					+ " LEFT OUTER JOIN grl_pessoa E ON (E1.cd_pessoa = E.cd_pessoa)"
					+ " LEFT OUTER JOIN agd_grupo F ON (A.cd_grupo_trabalho = F.cd_grupo)" 
					+ " WHERE A.cd_processo = "+cdProcesso
					+ (cdPessoaOrgao!=0 ? " AND A.cd_pessoa = " + cdPessoaOrgao : "") //agendas atribuidas a algum orgao
					+ " ORDER BY dt_inicial");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
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
			}
						
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getAgendas: " + e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result deleteAgendas(int cdProcesso, int cdAgendaItem){
		return deleteAgendas(cdProcesso, cdAgendaItem, null);
	}
	
	public static Result deleteAgendas(int cdProcesso, int cdAgendaItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_agenda_item " +
					" WHERE cd_processo = ? " +
					((cdAgendaItem>0)?"  AND cd_agenda_item="+cdAgendaItem:""));
			pstmt.setInt(1, cdProcesso);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno<0 ? retorno : 1);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.deleteAgendas: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir agenda no processo");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getOutrosInteressados(int cdProcesso){
		return getOutrosInteressados(cdProcesso, null);
	}
	
	public static ResultSetMap getOutrosInteressados(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM prc_outro_interessado WHERE cd_processo = "+cdProcesso);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getOutrosInteressados: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result deleteOutrosInteressados(int cdProcesso, int cdPessoa){
		return deleteOutrosInteressados(cdProcesso, cdPessoa, null);
	}
	
	public static Result deleteOutrosInteressados(int cdProcesso, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_outro_interessado " +
					" WHERE cd_processo = ? " +
					((cdPessoa>0)?"  AND cd_outro_interessado="+cdPessoa:""));
			pstmt.setInt(1, cdProcesso);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno<0 ? retorno : 1);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.deleteOutrosInteressados: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir outros interessados no processo");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca os andamentos de um determinado processo.
	 * 
	 * @param cdProcesso código do processo
	 * @return ResultSetMap lista de andamentos
	 */
	public static ResultSetMap getAndamentos(int cdProcesso){
		return getAndamentos(cdProcesso, false, false, null);
	}
	
	public static ResultSetMap getAndamentos(int cdProcesso, boolean lgVisualizarConfidencial){
		return getAndamentos(cdProcesso, lgVisualizarConfidencial, false, null);
	}
	
	public static ResultSetMap getAndamentos(int cdProcesso, boolean lgVisualizarConfidencial, boolean lgProtocolo){
		return getAndamentos(cdProcesso, lgVisualizarConfidencial, lgProtocolo, null);
	}
	
	public static ResultSetMap getAndamentos(int cdProcesso, boolean lgVisualizarConfidencial, boolean lgProtocolo, Connection connect){
		return getAndamentos(cdProcesso, 0, lgVisualizarConfidencial, lgProtocolo, connect);
	}
	
	public static ResultSetMap getAndamentos(int cdProcesso, int cdAndamento, boolean lgVisualizarConfidencial, boolean lgProtocolo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.cd_tipo_andamento, B.nm_tipo_andamento, D.nm_pessoa as nm_usuario, F.nr_processo, G.nm_tipo_processo" +
//					" (SELECT Z.cd_arquivo FROM prc_processo_arquivo Z "+
//					"	WHERE Z.cd_processo=A.cd_processo AND Z.cd_andamento=A.cd_andamento) AS cd_arquivo"+
//					 " E.cd_arquivo, E.nm_arquivo"+
					 " FROM prc_processo_andamento A" +
					 " LEFT OUTER JOIN prc_tipo_andamento B ON (A.cd_tipo_andamento = B.cd_tipo_andamento)" +
					 " LEFT OUTER JOIN seg_usuario C ON (A.cd_usuario = C.cd_usuario)" +
					 " LEFT OUTER JOIN grl_pessoa D ON (C.cd_pessoa = D.cd_pessoa)" +
//					 " LEFT OUTER JOIN prc_processo_arquivo E ON (A.cd_processo=E.cd_processo AND A.cd_andamento=E.cd_andamento)" +
 					"LEFT OUTER JOIN prc_processo F ON (A.cd_processo = F.cd_processo)" +
					"LEFT OUTER JOIN prc_tipo_processo G ON (F.cd_tipo_processo = G.cd_tipo_processo)" +
                     " WHERE A.cd_processo = "+cdProcesso+
                     (cdAndamento > 0 ? " AND A.cd_andamento = "+cdAndamento : "") +
                     (!lgVisualizarConfidencial ? " AND A.tp_visibilidade <> " + ProcessoAndamentoServices.TP_VISIBILIDADE_CONFIDENCIAL : "") +
                     " ORDER BY dt_andamento DESC");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			pstmt = connect.prepareStatement("SELECT A.cd_arquivo, A.nm_arquivo, A.cd_processo "
											+ " FROM prc_processo_arquivo A"
											+ " LEFT OUTER JOIN prc_processo_andamento B ON (A.cd_processo=B.cd_processo AND A.cd_andamento=B.cd_andamento)"
											+ " WHERE A.cd_processo=?"
											+ " AND A.cd_andamento=?");
			while(rsm.next()) {
				rsm.setValueToField("NM_CLIENTE", ProcessoServices.getClientes(cdProcesso));
				rsm.setValueToField("NM_ADVERSO", ProcessoServices.getAdversos(cdProcesso));
				pstmt.setInt(1, rsm.getInt("cd_processo"));
				pstmt.setInt(2, rsm.getInt("cd_andamento"));
				ResultSetMap rsmAux = new ResultSetMap(pstmt.executeQuery());
				if(!rsmAux.next()) 
					continue;
				
				rsmAux.beforeFirst();
				rsm.setValueToField("lg_arquivo", new Integer(1));
				rsm.setValueToField("RSM_ARQUIVO", rsmAux);
			}
			rsm.beforeFirst();
			
			if(lgProtocolo) {
				while(rsm.next()) {
					int cdDocumento = rsm.getInt("CD_DOCUMENTO", 0);
					String dsProtocolo = "";
					if(cdDocumento>0) {
						Documento doc = DocumentoDAO.get(cdDocumento, connect);
						
						dsProtocolo = Util.removeZeroEsquerda(doc.getNrDocumento()) + " - " + TipoDocumentoDAO.get(doc.getCdTipoDocumento(), connect).getNmTipoDocumento();
					}
					rsm.setValueToField("DS_PROTOCOLO", dsProtocolo);
					
					LogUtils.debug("ProcessoServices.getAndamentos");
					LogUtils.debug("cdDocumento: "+cdDocumento);
					LogUtils.debug("dsProtocolo: "+dsProtocolo);
					
				}
			}
				
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getAndamentos: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca apenas os andamentos de um processo aos quais um correspondente tem acesso, ou seja, 
	 * andamentos públicos e arquivos compartilhados.
	 * 
	 * @param cdProcesso código do processo
	 * @return ResultSetMap lista de andamentos
	 */
	public static ResultSetMap getAndamentosCorrespondente(int cdProcesso){
		return getAndamentosCorrespondente(cdProcesso, null);
	}
	
	public static ResultSetMap getAndamentosCorrespondente(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			//ANDAMENTOS PUBLICOS + ARQUIVOS COMPARTILHADOS
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nm_tipo_andamento, D.nm_pessoa as nm_usuario "
					+ " FROM prc_processo_andamento A "
					+ " LEFT OUTER JOIN prc_tipo_andamento B ON (A.cd_tipo_andamento = B.cd_tipo_andamento)"
					+ " LEFT OUTER JOIN seg_usuario C ON (A.cd_usuario = C.cd_usuario)"
					+ " LEFT OUTER JOIN grl_pessoa D ON (C.cd_pessoa = D.cd_pessoa)"
					+ " WHERE A.cd_processo = "+cdProcesso
					+ " AND (A.tp_visibilidade = "+ProcessoAndamentoServices.TP_VISIBILIDADE_PUBLICO
					+ " OR cd_andamento IN (SELECT A1.cd_andamento"
					+ "                        FROM prc_processo_arquivo A1, prc_processo_arquivo_agenda B1"
					+ "                        WHERE A1.cd_arquivo = B1.cd_arquivo"
					+ "                          AND A1.cd_processo = B1.cd_processo"
					+ "                          AND A1.cd_processo = A.cd_processo ))"
					+ " ORDER BY dt_andamento DESC, cd_andamento DESC");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getAndamentosCorrespondente: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result deleteAndamentos(int cdProcesso, int cdAndamento){
		return deleteAndamentos(cdProcesso, cdAndamento, null);
	}
	
	public static Result deleteAndamentos(int cdProcesso, int cdAndamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_processo_andamento " +
					" WHERE cd_processo = ? " +
					((cdAndamento>0)?"  AND cd_andamento="+cdAndamento:""));
			pstmt.setInt(1, cdProcesso);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno<0 ? retorno : 1);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.deleteAndamentos: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir andamento no processo");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result insertTestemunha(int cdProcesso, int cdPessoa){
		return insertTestemunha(cdProcesso, cdPessoa, null);
	}
	
	public static Result insertTestemunha(int cdProcesso, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_pessoa FROM prc_testemunha WHERE cd_processo = ? AND cd_pessoa = ?");
			pstmt.setInt(1, cdProcesso);
			pstmt.setInt(2, cdPessoa);
			
			if(pstmt.executeQuery().next())
				return new Result(-2, "Testemunha já existe no processo");
			
			pstmt = connect.prepareStatement("INSERT INTO prc_testemunha (cd_processo,cd_pessoa) VALUES (?,?)");
			pstmt.setInt(1, cdProcesso);
			pstmt.setInt(2, cdPessoa);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.insertTestemunha: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao inserir testemunha");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getTestemunhas(int cdProcesso){
		return getTestemunhas(cdProcesso, null);
	}
	
	public static ResultSetMap getTestemunhas(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nm_pessoa FROM prc_testemunha A, grl_pessoa B " +
						                     "WHERE A.cd_pessoa = B.cd_pessoa " +
						                     "  AND A.cd_processo = "+cdProcesso+
						                     "ORDER BY nm_pessoa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getTestemunhas: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result deleteTestemunhas(int cdProcesso, int cdPessoa){
		return deleteTestemunhas(cdProcesso, cdPessoa, null);
	}
	
	public static Result deleteTestemunhas(int cdProcesso, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_testemunha " +
					" WHERE cd_processo = ? " +
					((cdPessoa>0)?"  AND cd_pessoa="+cdPessoa:""));
			pstmt.setInt(1, cdProcesso);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno<0 ? retorno : 1);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.deleteTestemunhas: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir testemunhas no processo");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getGarantias(int cdProcesso){
		return getGarantias(cdProcesso, null);
	}
	
	public static ResultSetMap getGarantias(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM prc_bem_penhora A " +
						                     "WHERE A.cd_processo = "+cdProcesso);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getGarantias: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result deleteGarantias(int cdProcesso, int cdBem){
		return deleteGarantias(cdProcesso, cdBem, null);
	}
	
	public static Result deleteGarantias(int cdProcesso, int cdBem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_bem_penhora " +
					" WHERE cd_processo = ? " +
					((cdBem>0)?"  AND cd_bem="+cdBem:""));
			pstmt.setInt(1, cdProcesso);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno<0 ? retorno : 1);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.deleteGarantias: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir garantias no processo");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getMensagens(int cdProcesso){
		return getMensagens(cdProcesso, null);
	}
	
	public static ResultSetMap getMensagens(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.* FROM msg_mensagem A " +
						                     "WHERE A.cd_processo = "+cdProcesso);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getMensagens: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result deleteMensagens(int cdProcesso){
		return deleteMensagens(cdProcesso, null);
	}
	
	public static Result deleteMensagens(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM msg_mensagem " +
					" WHERE cd_processo = ? ");
			pstmt.setInt(1, cdProcesso);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno<0 ? retorno : 1);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.deleteMensagens: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir mensagens no processo");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAtendimentos(int cdProcesso){
		return getAtendimentos(cdProcesso, null);
	}
	
	public static ResultSetMap getAtendimentos(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.* FROM prc_atendimento A " +
						                     "WHERE A.cd_processo = "+cdProcesso);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getAtendimentos: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result deleteAtendimentos(int cdProcesso, int cdPessoa){
		return deleteAtendimentos(cdProcesso, cdPessoa, null);
	}
	
	public static Result deleteAtendimentos(int cdProcesso, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_atendimento " +
					" WHERE cd_processo = ? " +
					(cdPessoa>0 ? " AND cd_pessoa = " + cdPessoa: ""));
			pstmt.setInt(1, cdProcesso);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno<0 ? retorno : 1);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.deleteAtendimentos: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir atendimentos no processo");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getCalculos(int cdProcesso){
		return getCalculos(cdProcesso, null);
	}
	
	public static ResultSetMap getCalculos(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.* FROM prc_calculo A " +
						                     "WHERE A.cd_processo = "+cdProcesso);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getCalculos: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result deleteCalculos(int cdProcesso, int nrParcela){
		return deleteCalculos(cdProcesso, nrParcela, null);
	}
	
	public static Result deleteCalculos(int cdProcesso, int nrParcela, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_calculo " +
					" WHERE cd_processo = ? "+
					(nrParcela>0 ? " AND nr_parcela = "+nrParcela : ""));
			pstmt.setInt(1, cdProcesso);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno<0 ? retorno : 1);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.deleteCalculos: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir calculos no processo");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getListaProcesso(int cdProcesso, int cdUsuario){
		return getListaProcesso(cdProcesso, cdUsuario, null);
	}
	
	public static ResultSetMap getListaProcesso(int cdProcesso, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.* FROM prc_lista_processo A " +
						                     " WHERE A.cd_processo = "+cdProcesso +
						                     (cdUsuario>0 ? "AND A.cd_usuario = "+cdUsuario : ""));
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getListaProcesso: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result deleteListaProcesso(int cdProcesso, int cdUsuario){
		return deleteListaProcesso(cdProcesso, cdUsuario, null);
	}
	
	public static Result deleteListaProcesso(int cdProcesso, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_lista_processo " +
					" WHERE cd_processo = ? "+
					(cdUsuario>0 ? " AND cd_usuario = "+cdUsuario : ""));
			pstmt.setInt(1, cdProcesso);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno<0 ? retorno : 1);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.deleteListaProcesso: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir lista de processos no processo");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getProcessoInstancia(int cdProcesso){
		return getProcessoInstancia(cdProcesso, null);
	}
	
	public static ResultSetMap getProcessoInstancia(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.* FROM prc_processo_instancia A " +
						                     "WHERE A.cd_processo = "+cdProcesso);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getProcessoInstancia: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result deleteProcessoInstancia(int cdProcesso){
		return deleteProcessoInstancia(cdProcesso, null);
	}
	
	public static Result deleteProcessoInstancia(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_processo_instancia " +
					" WHERE cd_processo = ? ");
			pstmt.setInt(1, cdProcesso);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno<0 ? retorno : 1);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.deleteProcessoInstancia: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir processoInstancia no processo");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getRecortes(int cdProcesso){
		return getRecortes(cdProcesso, null);
	}
	
	public static ResultSetMap getRecortes(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.* FROM prc_recorte A " +
						                     "WHERE A.cd_processo = "+cdProcesso);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getRecorte: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result deleteRecortes(int cdProcesso){
		return deleteRecortes(cdProcesso, null);
	}
	
	public static Result deleteRecortes(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_recorte " +
					" WHERE cd_processo = ? ");
			pstmt.setInt(1, cdProcesso);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno<0 ? retorno : 1);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.deleteRecortes: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir recortes no processo");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getDocumentos(int cdProcesso){
		return getDocumentos(cdProcesso, null);
	}
	
	public static ResultSetMap getDocumentos(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.* FROM ptc_documento A " +
						                     "WHERE A.cd_processo = "+cdProcesso);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getDocumentos: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result deleteDocumentos(int cdProcesso){
		return deleteDocumentos(cdProcesso, null);
	}
	
	public static Result deleteDocumentos(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_documento " +
					" WHERE cd_processo = ? ");
			pstmt.setInt(1, cdProcesso);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno<0 ? retorno : 1);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.deleteDocumentos: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir documentos no processo");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getEventoFinanceiro(int cdProcesso){
		return getEventoFinanceiro(cdProcesso, true, true, null);
	}
	
	public static ResultSetMap getEventoFinanceiro(int cdProcesso, boolean lgDespesas, boolean lgReceitas){
		return getEventoFinanceiro(cdProcesso, lgDespesas, lgReceitas, null);
	}
	
	public static ResultSetMap getEventoFinanceiro(int cdProcesso, boolean lgDespesas, boolean lgReceitas, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_processo", String.valueOf(cdProcesso), ItemComparator.EQUAL, Types.INTEGER));
			if(!lgDespesas)
				criterios.add(new ItemComparator("A.tp_natureza_evento", String.valueOf(ProcessoFinanceiroServices.TP_NATUREZA_DESPESA), ItemComparator.DIFFERENT, Types.INTEGER));
			if(!lgReceitas)
				criterios.add(new ItemComparator("A.tp_natureza_evento", String.valueOf(ProcessoFinanceiroServices.TP_NATUREZA_RECEITA), ItemComparator.DIFFERENT, Types.INTEGER));
			return ProcessoFinanceiroServices.find(criterios, connect);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getMovimentoFinanceiro: " + e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result deleteEventoFinanceiro(int cdProcesso, int cdEventoFinanceiro){
		return deleteEventoFinanceiro(cdProcesso, cdEventoFinanceiro, null);
	}
	
	public static Result deleteEventoFinanceiro(int cdProcesso, int cdEventoFinanceiro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_processo_financeiro " +
					" WHERE cd_processo = ? " +
					((cdEventoFinanceiro>0)?" AND cd_evento_financeiro="+cdEventoFinanceiro:""));
			pstmt.setInt(1, cdProcesso);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno<0 ? retorno : 1);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.deleteEventoFinanceiro: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir evento financeiro no processo");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getContratos(int cdProcesso){
		return getContratos(cdProcesso, null);
	}

	public static ResultSetMap getContratos(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, "
					+ " B.cd_pessoa, B.cd_pessoa as cd_devedor, B.nm_pessoa, B.nm_pessoa as nm_devedor FROM prc_contrato A " +
					" LEFT OUTER JOIN grl_pessoa B ON (B.cd_pessoa = A.cd_devedor) " +
					" WHERE A.cd_processo = "+cdProcesso +
					" ORDER BY dt_inicio ");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getContratos: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result deleteContratos(int cdProcesso, int cdContrato){
		return deleteContratos(cdProcesso, cdContrato, null);
	}
	
	public static Result deleteContratos(int cdProcesso, int cdContrato, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_contrato " +
					" WHERE cd_processo = ? " +
					((cdContrato>0)?"  AND cd_contrato="+cdContrato:""));
			pstmt.setInt(1, cdProcesso);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno<0 ? retorno : 1);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.deleteContratos: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir contratos no processo");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getArquivos(int cdProcesso){
		return getArquivos(cdProcesso, false, null);
	}
	
	public static ResultSetMap getArquivos(int cdProcesso, boolean lgVisualizarConfidencial){
		return getArquivos(cdProcesso, lgVisualizarConfidencial, null);
	}
	
	public static ResultSetMap getArquivos(int cdProcesso, boolean lgVisualizarConfidencial, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.cd_arquivo, A.cd_processo, A.cd_andamento, A.nm_arquivo, A.nm_documento, " +
					"A.dt_arquivamento, A.lg_comprimido, A.cd_tipo_documento, " +
					"B.dt_andamento, C.nm_tipo_andamento, D.nr_processo, E.nm_tipo_processo, F.nm_tipo_documento, " +
					" \' \' AS nm_cliente, \' \' AS nm_adverso, A.id_repositorio "+
	                "FROM prc_processo_arquivo A "+
	                "LEFT OUTER JOIN prc_processo_andamento B ON (A.cd_processo  = B.cd_processo "+ 
	                "                                         AND A.cd_andamento = B.cd_andamento) "+
	                "LEFT OUTER JOIN prc_tipo_andamento     C ON (B.cd_tipo_andamento  = C.cd_tipo_andamento) "+
	                "LEFT OUTER JOIN prc_processo           D ON (A.cd_processo = D.cd_processo) "+
	                "LEFT OUTER JOIN prc_tipo_processo      E ON (D.cd_tipo_processo = E.cd_tipo_processo) "+
	                "LEFT OUTER JOIN gpn_tipo_documento     F ON (A.cd_tipo_documento = F.cd_tipo_documento) "+
                    "WHERE A.cd_processo = "+cdProcesso+
                    (!lgVisualizarConfidencial ? " AND (B.tp_visibilidade <> " + ProcessoAndamentoServices.TP_VISIBILIDADE_CONFIDENCIAL +" OR"
                    											+ "	A.cd_andamento IS NULL)" : "") +
                    
                    " ORDER BY A.dt_arquivamento, A.nm_documento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getArquivos: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result deleteArquivos(int cdProcesso, int cdArquivo){
		return deleteArquivos(cdProcesso, cdArquivo, null);
	}
	
	public static Result deleteArquivos(int cdProcesso, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_processo_arquivo " +
					" WHERE cd_processo = ? " +
					((cdArquivo>0)?"  AND cd_arquivo="+cdArquivo:""));
			pstmt.setInt(1, cdProcesso);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno<0 ? retorno : 1);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.deleteArquivos: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir arquivos no processo");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findAdvogadosEmpresa(String nmPessoa){
		return getAdvogadosEmpresa(-1, null);
	}
	
	public static ResultSetMap getAdvogadosEmpresa(){
		return getAdvogadosEmpresa(-1, null);
	}
	
	public static ResultSetMap getAdvogadosEmpresa(int stPessoa){
		return getAdvogadosEmpresa(stPessoa, null);
	}
	
	public static ResultSetMap getAdvogadosEmpresa(int stPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			int cdVinculoAdvogado = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_ADVOGADO", 0);
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					   " FROM grl_pessoa A " +
					   " JOIN grl_pessoa_empresa B ON (A.cd_pessoa = B.cd_pessoa " +
					   "                          AND B.cd_vinculo = "+cdVinculoAdvogado+")" +
					   (stPessoa!=-1 ? " WHERE A.st_cadastro = "+stPessoa : " WHERE A.st_cadastro = 1") +
					   " ORDER BY nm_pessoa");

			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getAdvogados: " + e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAdvogadosAdverso(){
		return getAdvogadosAdverso(null);
	}
	
	public static ResultSetMap getAdvogadosAdverso(Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			int cdVinculoAdvogadoAdverso = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_ADVOGADO_ADVERSO", -21);
	 		
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					   " FROM grl_pessoa A " +
					   " JOIN grl_pessoa_empresa B ON (A.cd_pessoa = B.cd_pessoa " +
					   "                          AND B.cd_vinculo = "+cdVinculoAdvogadoAdverso+") " +
					   " WHERE A.st_cadastro = " + PessoaServices.ST_ATIVO +
					   " ORDER BY nm_pessoa");

			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getAdvogados: " + e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}	
	
	public static Result getDadosEmail(int cdProcesso){
		return getDadosEmail(cdProcesso, null);
	}
	
	public static Result getDadosEmail(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			Processo processo = ProcessoDAO.get(cdProcesso, connect);
			Result result = new Result(1, "Mensagem gerada a partir do processo ["+processo.getNrProcesso()+"]");
		
			result.addObject("DESTINATARIOS", getDestinatariosEmail(cdProcesso, connect));
			result.addObject("ARQUIVOS", getAnexosEmail(cdProcesso, connect));
			result.addObject("ASSINATURA", getAssinaturaEmail(connect));
			
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getDadosEmail: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getDestinatariosEmail(int cdProcesso){
		return getDestinatariosEmail(cdProcesso, null);
	}
	
	public static ResultSetMap getDestinatariosEmail(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			Processo processo = ProcessoDAO.get(cdProcesso, connect);
			
			if(processo==null)
				return null;
			
			ResultSetMap rsm = new ResultSetMap();
			
			//Responsável pelo grupo de trabalho
			PreparedStatement pstmt = connect.prepareStatement("SELECT B.cd_pessoa, B.nm_pessoa, B.nm_email " +
					"FROM agd_grupo A " +
					"LEFT OUTER JOIN grl_pessoa B ON (B.cd_pessoa = A.cd_proprietario AND B.st_cadastro <> 0) " +
					"LEFT OUTER JOIN prc_processo C ON (C.cd_grupo_trabalho = A.cd_grupo) " +
					"WHERE C.cd_processo = ? ");
			pstmt.setInt(1, cdProcesso);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next() && rs.getInt("CD_PESSOA")>0 && rs.getString("NM_EMAIL")!=null && !rs.getString("NM_EMAIL").equals("")){
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("ID_GRUPO", "1");
				register.put("NM_GRUPO", "GRUPO DE TRABALHO");
				register.put("CD_PESSOA", rs.getInt("CD_PESSOA"));
				register.put("NM_PESSOA", rs.getString("NM_PESSOA"));
				register.put("NM_EMAIL", rs.getString("NM_EMAIL"));
				register.put("DS_LABEL", rs.getString("NM_PESSOA") + " <"+rs.getString("NM_EMAIL")+">");
				
				rsm.addRegister(register);
			}
			
			//Advogado responsável
			Pessoa advgResponsavel = PessoaDAO.get(processo.getCdAdvogado(), connect);
			if(advgResponsavel!=null && advgResponsavel.getStCadastro()!=0 && advgResponsavel.getNmEmail()!=null && !advgResponsavel.getNmEmail().equals("")) {
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("ID_GRUPO", "2");
				register.put("NM_GRUPO", "ADVOGADO RESPONSÁVEL");
				register.put("CD_PESSOA", advgResponsavel.getCdPessoa());
				register.put("NM_PESSOA", advgResponsavel.getNmPessoa());
				register.put("NM_EMAIL", advgResponsavel.getNmEmail());
				register.put("DS_LABEL", advgResponsavel.getNmPessoa() + " <"+advgResponsavel.getNmEmail()+">");
				
				rsm.addRegister(register);
			}
			
			//Advogado titular
			Pessoa advgTitular = PessoaDAO.get(processo.getCdAdvogadoTitular(), connect);
			if(advgTitular!=null && advgTitular.getStCadastro()!=0  && advgTitular.getNmEmail()!=null && !advgTitular.getNmEmail().equals("")) {
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("ID_GRUPO", "3");
				register.put("NM_GRUPO", "ADVOGADO TITULAR");
				register.put("CD_PESSOA", advgTitular.getCdPessoa());
				register.put("NM_PESSOA", advgTitular.getNmPessoa());
				register.put("NM_EMAIL", advgTitular.getNmEmail());
				register.put("DS_LABEL", advgTitular.getNmPessoa() + " <"+advgTitular.getNmEmail()+">");
				
				rsm.addRegister(register);
			}
			
			//Outros Advogados
			pstmt = connect.prepareStatement("SELECT B.cd_pessoa, B.nm_pessoa, B.nm_email " +
					"FROM prc_processo_advogado A " +
					"LEFT OUTER JOIN grl_pessoa B ON (B.cd_pessoa = A.cd_pessoa AND B.st_cadastro <> 0) "+
					"WHERE A.cd_processo = ?");
			pstmt.setInt(1, cdProcesso);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				if(rs.getString("NM_EMAIL")==null || rs.getString("NM_EMAIL").equals(""))
					continue;
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("ID_GRUPO", "4");
				register.put("NM_GRUPO", "OUTROS ADVOGADOS");
				register.put("CD_PESSOA", rs.getInt("CD_PESSOA"));
				register.put("NM_PESSOA", rs.getString("NM_PESSOA"));
				register.put("NM_EMAIL", rs.getString("NM_EMAIL"));
				register.put("DS_LABEL", rs.getString("NM_PESSOA") + " <"+rs.getString("NM_EMAIL")+">");
				
				rsm.addRegister(register);
			}
			
			//partes cliente
			pstmt = connect.prepareStatement("SELECT B.cd_pessoa, B.nm_pessoa, B.nm_email " +
					"FROM prc_parte_cliente A " +
					"LEFT OUTER JOIN grl_pessoa B ON (B.cd_pessoa = A.cd_pessoa) "+
					"WHERE A.cd_processo = ? ");
			pstmt.setInt(1, cdProcesso);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				if(rs.getString("NM_EMAIL")==null || rs.getString("NM_EMAIL").equals(""))
					continue;
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("ID_GRUPO", "5");
				register.put("NM_GRUPO", "PARTE CLIENTE");
				register.put("CD_PESSOA", rs.getInt("CD_PESSOA"));
				register.put("NM_PESSOA", rs.getString("NM_PESSOA"));
				register.put("NM_EMAIL", rs.getString("NM_EMAIL"));
				register.put("DS_LABEL", rs.getString("NM_PESSOA") + " <"+rs.getString("NM_EMAIL")+">");
				
				rsm.addRegister(register);
			}
			
			//correspondente
			pstmt = connect.prepareStatement("SELECT B.cd_pessoa as cd_pessoa_responsavel, B.nm_pessoa as nm_pessoa_responsavel, B.nm_email as nm_email_responsavel, " +
					"C.cd_pessoa, C.nm_pessoa, C.nm_email " +
					"FROM prc_orgao A " +
					"LEFT OUTER JOIN grl_pessoa B ON (B.cd_pessoa = A.cd_responsavel AND B.st_cadastro <> 0) "+
					"LEFT OUTER JOIN grl_pessoa C ON (C.cd_pessoa = A.cd_pessoa AND C.st_cadastro <> 0) "+
					"WHERE A.cd_orgao = ? ");
			pstmt.setInt(1, processo.getCdOrgao());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				
				
				if(rs.getInt("CD_PESSOA")>0 && rs.getString("NM_EMAIL")!=null && !rs.getString("NM_EMAIL").equals("")) {
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("ID_GRUPO", "6");
					register.put("NM_GRUPO", "CORRESPONDENTE");
					register.put("CD_PESSOA", rs.getInt("CD_PESSOA"));
					register.put("NM_PESSOA", rs.getString("NM_PESSOA"));
					register.put("NM_EMAIL", rs.getString("NM_EMAIL"));
					register.put("DS_LABEL", rs.getString("NM_PESSOA") + " <"+rs.getString("NM_EMAIL")+">");
					rsm.addRegister(register);
				}
				
				
				if(rs.getInt("CD_PESSOA_RESPONSAVEL")>0 && rs.getString("NM_EMAIL_RESPONSAVEL")!=null && !rs.getString("NM_EMAIL_RESPONSAVEL").equals("")) {
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("ID_GRUPO", "6");
					register.put("NM_GRUPO", "CORRESPONDENTE");
					register.put("CD_PESSOA", rs.getInt("CD_PESSOA_RESPONSAVEL"));
					register.put("NM_PESSOA", rs.getString("NM_PESSOA_RESPONSAVEL"));
					register.put("NM_EMAIL", rs.getString("NM_EMAIL_RESPONSAVEL"));
					register.put("DS_LABEL", rs.getString("NM_PESSOA_RESPONSAVEL") + " <"+rs.getString("NM_EMAIL_RESPONSAVEL")+">");
					rsm.addRegister(register);
				}
			}
			
			//adversos
			pstmt = connect.prepareStatement("SELECT B.cd_pessoa, B.nm_pessoa, B.nm_email " +
					"FROM prc_outra_parte A " +
					"LEFT OUTER JOIN grl_pessoa B ON (B.cd_pessoa = A.cd_pessoa) "+
					"WHERE A.cd_processo = ? ");
			pstmt.setInt(1, cdProcesso);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				if(rs.getString("NM_EMAIL")==null || rs.getString("NM_EMAIL").equals(""))
					continue;
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("ID_GRUPO", "7");
				register.put("NM_GRUPO", "ADVERSOS");
				register.put("CD_PESSOA", rs.getInt("CD_PESSOA"));
				register.put("NM_PESSOA", rs.getString("NM_PESSOA"));
				register.put("NM_EMAIL", rs.getString("NM_EMAIL"));
				register.put("DS_LABEL", rs.getString("NM_PESSOA") + " <"+rs.getString("NM_EMAIL")+">");
				
				rsm.addRegister(register);
			}
			
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getDestinatariosEmail: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAnexosEmail(int cdProcesso){
		return getAnexosEmail(cdProcesso, null);
	}
	
	public static ResultSetMap getAnexosEmail(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			Processo processo = ProcessoDAO.get(cdProcesso, connect);
			
			if(processo==null)
				return null;
			
			ResultSetMap rsm = new ResultSetMap();
			
			//Documentos processo
			PreparedStatement pstmt = connect.prepareStatement("SELECT * "+
	                "FROM prc_processo_arquivo A "+
                    "WHERE A.cd_processo = ?");
			pstmt.setInt(1, cdProcesso);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				HashMap<String, Object> register = new HashMap<String, Object>();
				
				int length = rs.getBytes("BLB_ARQUIVO")!=null ? rs.getBytes("BLB_ARQUIVO").length : 0;
				
				register.put("ID_GRUPO", "1");
				register.put("CD_PROCESSO", rs.getInt("CD_PROCESSO"));
				register.put("CD_ARQUIVO", rs.getInt("CD_ARQUIVO"));
				register.put("NM_GRUPO", "ARQUIVOS DO PROCESSO");
				register.put("NM_ARQUIVO", rs.getString("NM_ARQUIVO"));
				register.put("NM_DOCUMENTO", rs.getString("NM_DOCUMENTO"));
				//register.put("BLB_ARQUIVO", rs.getBytes("BLB_ARQUIVO"));
				register.put("DS_LABEL", rs.getString("NM_DOCUMENTO") + " ("+(length/1024)+"kb)");
				
				rsm.addRegister(register);
			}
			
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getAnexosEmail: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static String getAssinaturaEmail(){
		return getAssinaturaEmail(null);
	}
	
	public static String getAssinaturaEmail(Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			String txtAssinatura = ParametroServices.getValorOfParametroAsString("DS_EMAIL_ASSINATURA", "");
			return txtAssinatura;
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getAssinaturaEmail: " + e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getMensagensByProcesso(int cdProcesso){
		return getMensagensByProcesso(cdProcesso, null);
	}
	
	public static ResultSetMap getMensagensByProcesso(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, D.nm_pessoa as nm_usuario_origem "+
					" FROM msg_mensagem A " +
					" LEFT OUTER JOIN seg_usuario B ON (A.cd_usuario_origem = B.cd_usuario) " +
					" LEFT OUTER JOIN grl_pessoa D ON (D.cd_pessoa = B.cd_usuario) " +
					" WHERE A.cd_processo = ? " +
					" ORDER BY A.dt_envio DESC, A.cd_mensagem DESC ");
			pstmt.setInt(1, cdProcesso);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()) {
				HashMap<String, Object> register = rsm.getRegister();
				pstmt = connect.prepareStatement("SELECT A.*, C.nm_pessoa as nm_usuario, C.nm_pessoa " +
						"FROM msg_mensagem_destinatario A " +
						"LEFT OUTER JOIN seg_usuario B ON (A.cd_pessoa = B.cd_usuario) " +
						"LEFT OUTER JOIN grl_pessoa C ON (C.cd_pessoa = A.cd_pessoa) " +
						"WHERE cd_mensagem = "+rsm.getInt("CD_MENSAGEM") +
						" ORDER BY nm_pessoa");
				register.put("DESTINATARIOS", new ResultSetMap(pstmt.executeQuery()));
				rsm.updateRegister(register);
			}
			
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getAssinaturaEmail: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findAdvogadoEmail(ArrayList<ItemComparator> criterios) {
		Connection connect = Conexao.conectar();
		int cdVinculoAdvogado = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_ADVOGADO", 0);
		return Search.find("SELECT * " +
						   "FROM grl_pessoa A " +
						   "JOIN grl_pessoa_empresa B ON (A.cd_pessoa = B.cd_pessoa " +
						   "                          AND B.cd_vinculo = "+cdVinculoAdvogado+")"+
						   "WHERE A.NM_EMAIL is not null ",
						   "ORDER BY nm_pessoa", criterios, connect, connect==null);
	}
	
	public static Result executarAutotextoEmail(int cdModelo, int cdProcesso) {
		return executarAutotextoEmail(cdModelo, cdProcesso, 0, 0, null);
	}

	public static Result executarAutotextoEmail(int cdModelo, int cdProcesso, int cdAndamento, int cdAgendaItem) {
		return executarAutotextoEmail(cdModelo, cdProcesso, cdAndamento, cdAgendaItem, null);
	}

	public static Result executarAutotextoEmail(int cdModelo, int cdProcesso, int cdAndamento, int cdAgendaItem, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			ModeloDocumento modelo = ModeloDocumentoDAO.get(cdModelo, connect);
			Processo processo = ProcessoDAO.get(cdProcesso, connect);
			
			String autotexto = new String(modelo.getBlbConteudo(), "UTF-8");
			
			if(autotexto.indexOf("\\rtf1")!=-1) {
				autotexto = new String(Converter.rtfToHtml(autotexto.getBytes()));
				autotexto = autotexto.replaceAll("(?s)\\<html\\>.*\\<body\\>", "");
				autotexto = autotexto.replaceAll("(?s)\\</body>.*\\</html\\>", "").trim();
			}
			
			Pessoa advResponsavel = PessoaDAO.get(processo.getCdAdvogado(), connect);
			autotexto = autotexto.replaceAll("<#Advogado_responsavel>", (advResponsavel!=null ? advResponsavel.getNmPessoa() : "<#Advogado_responsavel>"));
			autotexto = autotexto.replaceAll("<#Numero_processo>", processo.getNrProcesso());
			autotexto = autotexto.replaceAll("<#cd_agenda>", Integer.toString(cdAgendaItem));
			
			HashMap<String, String> fieldMap = getFieldsMapByProcesso(cdAgendaItem, cdProcesso, 0, cdAndamento, connect);
			for (Object key : fieldMap.keySet().toArray()) {
				
				String v = fieldMap.get(key) == null ? "" : fieldMap.get(key);
				String k = ((String)key).replaceAll("<", "&lt;").replaceAll(">", "&gt;");
				
				autotexto = autotexto.replaceAll(k, v);
				
			}
						
			return new Result(1, "Autotexto executado com sucesso", "AUTOTEXTO", autotexto);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao executar autotexto...");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	
	
	public static ResultSetMap getSumarioProcessos(int dtInicioMes, int dtInicioAno, int dtFinalMes, int dtFinalAno) {
		return getSumarioProcessos(dtInicioMes, dtInicioAno, dtFinalMes, dtFinalAno, null);
	}

	public static ResultSetMap getSumarioProcessos(int dtInicioMes, int dtInicioAno, int dtFinalMes, int dtFinalAno, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			GregorianCalendar dtInicial = new GregorianCalendar(dtInicioAno, dtInicioMes, 1);
			dtInicial.set(Calendar.HOUR_OF_DAY, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtInicial.set(Calendar.SECOND, 0);
			
			GregorianCalendar dtFinal = new GregorianCalendar(dtFinalAno, dtFinalMes, 1);
			dtFinal.set(Calendar.DAY_OF_MONTH, dtFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
			dtFinal.set(Calendar.HOUR_OF_DAY, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			dtFinal.set(Calendar.SECOND, 59);
			
			ResultSetMap rsm = new ResultSetMap();
			
			GregorianCalendar dtTemp = new GregorianCalendar(dtInicioAno, dtInicioMes, 1);
			dtTemp.set(Calendar.HOUR_OF_DAY, 0);
			dtTemp.set(Calendar.MINUTE, 0);
			dtTemp.set(Calendar.SECOND, 0);
			
			while(dtTemp.before(dtFinal)){
				HashMap<String, Object> register = new HashMap<String, Object>();
				
				GregorianCalendar dt = (GregorianCalendar)dtTemp.clone();
				dt.set(Calendar.DAY_OF_MONTH, dt.getActualMaximum(Calendar.DAY_OF_MONTH));
				dt.set(Calendar.HOUR_OF_DAY, 23);
				dt.set(Calendar.MINUTE, 59);
				dt.set(Calendar.SECOND, 59);
				
				register.put("DT_SUMARIO", dtTemp.clone());
				register.put("DT_SUMARIO_LABEL", Util.formatDate(dtTemp, "MMM")+"/"+
							dtTemp.get(Calendar.YEAR));
				
				//QUANTIDADE DE CADASTRADOS
				pstmt = connect.prepareStatement("SELECT count(*) as QT_PROCESSOS, sum(vl_processo) as VL_PROCESSOS"+
						" FROM prc_processo "+
						" WHERE dt_cadastro >= ? " +
						" AND dt_cadastro <= ? ");
				pstmt.setTimestamp(1,new Timestamp(dtTemp.getTimeInMillis()));
				pstmt.setTimestamp(2,new Timestamp(dt.getTimeInMillis()));
				ResultSet rs = pstmt.executeQuery();
				
				register.put("QT_TOTAL_CADASTRADOS", rs.next()?rs.getInt("QT_PROCESSOS"):0);
				register.put("VL_TOTAL_CADASTRADOS", rs.getDouble("VL_PROCESSOS"));
				
				//QUANTIDADE DE FINALIZADOS
				pstmt = connect.prepareStatement("SELECT count(*) as QT_PROCESSOS, sum(vl_processo) as VL_PROCESSOS"+
						" FROM prc_processo "+
						" WHERE dt_sentenca >= ? " +
						" AND dt_sentenca <= ? ");
				pstmt.setTimestamp(1,new Timestamp(dtTemp.getTimeInMillis()));
				pstmt.setTimestamp(2,new Timestamp(dt.getTimeInMillis()));
				rs = pstmt.executeQuery();
				
				register.put("QT_TOTAL_FINALIZADOS", rs.next()?rs.getInt("QT_PROCESSOS"):0);
				register.put("VL_TOTAL_FINALIZADOS", rs.getDouble("VL_PROCESSOS"));
				
				//QUANTIDADE DE ACORDOS
				pstmt = connect.prepareStatement("SELECT count(*) as QT_PROCESSOS, sum(vl_acordo) as VL_PROCESSOS"+
						" FROM prc_processo "+
						" WHERE dt_situacao >= ? " +
						" AND dt_situacao <= ? " +
						" AND vl_acordo is not null");
				pstmt.setTimestamp(1,new Timestamp(dtTemp.getTimeInMillis()));
				pstmt.setTimestamp(2,new Timestamp(dt.getTimeInMillis()));
				rs = pstmt.executeQuery();
				
				register.put("QT_TOTAL_ACORDOS", rs.next()?rs.getInt("QT_PROCESSOS"):0);
				register.put("VL_TOTAL_ACORDOS", rs.getDouble("VL_PROCESSOS"));
				
				rsm.addRegister(register);
				dtTemp.add(Calendar.MONTH, 1);
				rs.close();
			}
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioProcessos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getSumarioTiposProcesso(int mes, int ano) {
		return getSumarioTiposProcesso(mes, ano, null);
	}

	public static ResultSetMap getSumarioTiposProcesso(int mes, int ano, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			GregorianCalendar dtInicial = new GregorianCalendar(ano, mes, 1);
			dtInicial.set(Calendar.HOUR_OF_DAY, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtInicial.set(Calendar.SECOND, 0);
			
			GregorianCalendar dtFinal = new GregorianCalendar(ano, mes, 1);
			dtFinal.set(Calendar.DAY_OF_MONTH, dtFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
			dtFinal.set(Calendar.HOUR_OF_DAY, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			dtFinal.set(Calendar.SECOND, 59);
			
			pstmt = connect.prepareStatement("SELECT count(*) as QT_PROCESSOS, B.NM_TIPO_PROCESSO "+
					" FROM prc_processo A "+
					" LEFT OUTER JOIN prc_tipo_processo B ON (A.cd_tipo_processo = B.cd_tipo_processo) "+
					" WHERE dt_cadastro >= ? " +
					" AND dt_cadastro <= ? " +
					" GROUP BY B.NM_TIPO_PROCESSO");
			pstmt.setTimestamp(1,new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2,new Timestamp(dtFinal.getTimeInMillis()));
				
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioTiposProcesso: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getSumarioFinanceiro(int mes, int ano) {
		return getSumarioFinanceiro(mes, ano, null);
	}

	public static ResultSetMap getSumarioFinanceiro(int mes, int ano, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			GregorianCalendar dtInicial = new GregorianCalendar(ano, mes, 1);
			dtInicial.set(Calendar.HOUR_OF_DAY, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtInicial.set(Calendar.SECOND, 0);
			
			GregorianCalendar dtFinal = new GregorianCalendar(ano, mes, 1);
			dtFinal.set(Calendar.DAY_OF_MONTH, dtFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
			dtFinal.set(Calendar.HOUR_OF_DAY, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			dtFinal.set(Calendar.SECOND, 59);
			
			ResultSetMap rsm = new ResultSetMap();
			
			//total processos
			HashMap<String, Object> register = new HashMap<String, Object>();
			pstmt = connect.prepareStatement("SELECT sum(VL_PROCESSO) as VL_TOTAL "+
					" FROM prc_processo A "+
					" WHERE dt_cadastro >= ? " +
					" AND dt_cadastro <= ? ");
			pstmt.setTimestamp(1,new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2,new Timestamp(dtFinal.getTimeInMillis()));
			ResultSet rs = pstmt.executeQuery();
			
			register.put("VL_TOTAL", rs.next()?rs.getDouble("VL_TOTAL"):0.0);
			register.put("NM_TOTAL", "Valor total dos processos (R$)");
			rsm.addRegister(register);
			
			//total sentenças
			register = new HashMap<String, Object>();
			pstmt = connect.prepareStatement("SELECT sum(VL_SENTENCA) as VL_TOTAL "+
					" FROM prc_processo A "+
					" WHERE dt_cadastro >= ? " +
					" AND dt_cadastro <= ? ");
			pstmt.setTimestamp(1,new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2,new Timestamp(dtFinal.getTimeInMillis()));
			rs = pstmt.executeQuery();
			
			register.put("VL_TOTAL", rs.next()?rs.getDouble("VL_TOTAL"):0.0);
			register.put("NM_TOTAL", "Valor total das sentenças (R$)");
			rsm.addRegister(register);
			
			//total acordos
			register = new HashMap<String, Object>();
			pstmt = connect.prepareStatement("SELECT sum(VL_ACORDO) as VL_TOTAL "+
					" FROM prc_processo A "+
					" WHERE dt_cadastro >= ? " +
					" AND dt_cadastro <= ? ");
			pstmt.setTimestamp(1,new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2,new Timestamp(dtFinal.getTimeInMillis()));
			rs = pstmt.executeQuery();
			
			register.put("VL_TOTAL", rs.next()?rs.getDouble("VL_TOTAL"):0.0);
			register.put("NM_TOTAL", "Valor total dos Acordos (R$)");
			rsm.addRegister(register);
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFinanceiro: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getSumarioFase(int mes, int ano) {
		return getSumarioFase(mes, ano, null);
	}

	public static ResultSetMap getSumarioFase(int mes, int ano, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			GregorianCalendar dtInicial = new GregorianCalendar(ano, mes, 1);
			dtInicial.set(Calendar.HOUR_OF_DAY, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtInicial.set(Calendar.SECOND, 0);
			
			GregorianCalendar dtFinal = new GregorianCalendar(ano, mes, 1);
			dtFinal.set(Calendar.DAY_OF_MONTH, dtFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
			dtFinal.set(Calendar.HOUR_OF_DAY, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			dtFinal.set(Calendar.SECOND, 59);
			
			//total processos por situacao
			pstmt = connect.prepareStatement("select b.nm_tipo_situacao, count(*) as qt_processos "+
								" from prc_processo a, prc_tipo_situacao b "+
								" where a.cd_tipo_situacao = b.cd_tipo_situacao "+
								"   AND a.dt_cadastro >= ? " +
								"   AND a.dt_cadastro <= ? " +
								" group by 1");
			pstmt.setTimestamp(1,new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2,new Timestamp(dtFinal.getTimeInMillis()));	
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	

	public static Result executeModeloWeb(int cdModelo, int cdProcesso, int cdEmpresa) {
		return executeModeloWeb(cdModelo, 0, cdProcesso, cdEmpresa, null);
	}
	
	public static Result executeModeloWeb(int cdModelo, int cdAgendaItem, int cdProcesso, int cdEmpresa) {
		return executeModeloWeb(cdModelo, cdAgendaItem, cdProcesso, cdEmpresa, null);
	}

	public static Result executeModeloWeb(int cdModelo, int cdAgendaItem, int cdProcesso, int cdEmpresa, Connection connection) {
		return executeModeloWeb(cdModelo, cdAgendaItem, cdProcesso, cdEmpresa, 0, connection);
	}
	
	public static Result executeModeloWeb(int cdModelo, int cdAgendaItem, int cdProcesso, int cdEmpresa, int cdAndamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			ModeloDocumento modelo = ModeloDocumentoDAO.get(cdModelo, connection);
			if(modelo==null)
				return new Result(-1, "Modelo indicado não existe.");
			
			if(ProcessoDAO.get(cdProcesso, connection)==null)
				return new Result(-1, "Processo indicado não existe.");
			
//			if(EmpresaDAO.get(cdEmpresa, connection)==null)
//				return new Result(-1, "Empresa indicada não existe.");
			
			String source = new String(modelo.getBlbConteudo(), "UTF-8");
			
//			LogUtils.info("source:\n"+source);
			
			HashMap<String, String> fieldMap = getFieldsMapByProcesso(cdAgendaItem, cdProcesso, cdEmpresa, cdAndamento, connection);
			for (Object key : fieldMap.keySet().toArray()) {
				
				String v = fieldMap.get(key) == null ? "" : fieldMap.get(key);
				String k = ((String)key).replaceAll("<", "&lt;").replaceAll(">", "&gt;");
				
				//System.out.println("key: "+k+", value: "+v);
				source = source.replaceAll(k, v);
			}
			
			Result result = new Result(1, "Processo executado com sucesso.", "BLB_DOCUMENTO", source.getBytes());
			result.addObject("AUTOTEXTO", source);
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoServices.executeModeloWeb: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static HashMap<String, String> getFieldsMapByProcesso(int cdProcesso, int cdEmpresa) {
		return getFieldsMapByProcesso(0, cdProcesso, cdEmpresa, null);
	}
	
	public static HashMap<String, String> getFieldsMapByProcesso(int cdProcesso, int cdEmpresa, Connection connect) {
		return getFieldsMapByProcesso(0, cdProcesso, cdEmpresa, connect);
	}
	
	public static HashMap<String, String> getFieldsMapByProcesso(int cdAgendaItem, int cdProcesso, int cdEmpresa) {
		return getFieldsMapByProcesso(cdAgendaItem, cdProcesso, cdEmpresa, 0, null);
	}
	
	public static HashMap<String, String> getFieldsMapByProcesso(int cdAgendaItem, int cdProcesso, int cdEmpresa, Connection connect){
		return getFieldsMapByProcesso(cdAgendaItem, cdProcesso, cdEmpresa, 0, connect);
	}

	public static HashMap<String, String> getFieldsMapByProcesso(int cdAgendaItem, int cdProcesso, int cdEmpresa, int cdAndamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			HashMap<String, String> fieldMap = new HashMap<String, String>();
			
			String urlPortal = ParametroServices.getValorOfParametroAsString("NM_URL_PORTAL", "");
			
			/* EMPRESA */
			fieldMap.put("<#Url_portal>", urlPortal);
			
			/* GERAL */			
			fieldMap.put("<#Data_impressao>", Util.formatDate(new GregorianCalendar(), "dd 'de' MMMM 'de' yyyy"));
			
			PessoaEndereco endereco = PessoaEnderecoServices.getEnderecoPrincipal(cdEmpresa, connect);
			if(endereco!=null) {
				Cidade cidadeEndereco = CidadeDAO.get(endereco.getCdCidade(), connect);
				if(cidadeEndereco!=null)
					fieldMap.put("<#Cidade>", cidadeEndereco.getNmCidade());
			}
			
			/* CLIENTE */
			ResultSetMap rsmClientes = ProcessoServices.getParteCliente(cdProcesso, connect);
			
			if(rsmClientes.next()) {
				
				Pessoa cliente;
				if(rsmClientes.getInt("GN_PESSOA") == PessoaServices.TP_FISICA) {
					cliente = PessoaFisicaDAO.get(rsmClientes.getInt("CD_PESSOA"), connect);
					
					if(cliente != null && (PessoaFisica)cliente!=null) {
						
						fieldMap.put("<#Data_nascimento>", Util.formatDate(((PessoaFisica)cliente).getDtNascimento(), "dd/MM/yyyy"));
						
						Cidade cidadeNaturalidadeCliente = CidadeDAO.get(((PessoaFisica)cliente).getCdNaturalidade(), connect);
						if(cidadeNaturalidadeCliente!=null)
							fieldMap.put("<#Naturalidade>", cidadeNaturalidadeCliente.getNmCidade());
						
						fieldMap.put("<#Mae>", ((PessoaFisica)cliente).getNmMae());
						fieldMap.put("<#Pai>", ((PessoaFisica)cliente).getNmPai());
	
						fieldMap.put("<#CPF_CNPJ>", "nº "+Util.formatCpf(((PessoaFisica)cliente).getNrCpf(), ""));
						fieldMap.put("<#CPF>", "nº "+Util.formatCpf(((PessoaFisica)cliente).getNrCpf(), ""));
						fieldMap.put("<#RG>", ((PessoaFisica)cliente).getNrRg());
						fieldMap.put("<#RG_Orgao>", ((PessoaFisica)cliente).getSgOrgaoRg());
	
						fieldMap.put("<#Estado_civil>", PessoaFisicaServices.situacaoEstadoCivil[((PessoaFisica)cliente).getStEstadoCivil()]);
					}
				}
				else {
					cliente = PessoaJuridicaDAO.get(rsmClientes.getInt("CD_PESSOA"), connect);
					if(cliente != null && (PessoaJuridica)cliente!=null) {
						fieldMap.put("<#CPF_CNPJ>", "nº "+Util.formatCnpj(((PessoaJuridica)cliente).getNrCnpj(), ""));
						fieldMap.put("<#CPF>", "nº "+Util.formatCnpj(((PessoaJuridica)cliente).getNrCnpj(), ""));
					}
				}
				
				if(cliente==null)
					cliente = PessoaDAO.get(rsmClientes.getInt("CD_PESSOA"), connect);
								
				PessoaEndereco enderecoCliente = null;
				if(cliente!=null) {
					enderecoCliente = PessoaEnderecoServices.getEnderecoPrincipal(cliente.getCdPessoa(), connect);
					
					if(enderecoCliente!=null) {
						Cidade cidadeEnderecoCliente = CidadeDAO.get(enderecoCliente.getCdCidade(), connect);
						Estado ufEnderecoCliente =null;
						if(cidadeEnderecoCliente!=null)
							ufEnderecoCliente = EstadoDAO.get(cidadeEnderecoCliente.getCdEstado(), connect);
						
						TipoLogradouro tpLogradouro = TipoLogradouroDAO.get(enderecoCliente.getCdTipoLogradouro(), connect);
						fieldMap.put("<#Endereco>", (tpLogradouro!=null ? tpLogradouro.getSgTipoLogradouro()+" " : "")+enderecoCliente.getNmLogradouro());
						fieldMap.put("<#Bairro>", enderecoCliente.getNmBairro());
						fieldMap.put("<#Complemento_Endereco>", enderecoCliente.getNmComplemento());
						fieldMap.put("<#Nr_endereco>", "nº "+enderecoCliente.getNrEndereco());
						
						if(cidadeEnderecoCliente!=null)
							fieldMap.put("<#Cidade_endereco>", cidadeEnderecoCliente.getNmCidade() + (ufEnderecoCliente!=null ? " - "+ufEnderecoCliente.getSgEstado() : ""));
						
						fieldMap.put("<#CEP>", "CEP "+Util.formatCep(enderecoCliente.getNrCep(), ""));
						
						
						fieldMap.put("<#Endereco_completo>", (tpLogradouro!=null ? tpLogradouro.getSgTipoLogradouro()+" " : "")+enderecoCliente.getNmLogradouro() + ", " +
															"Nº "+enderecoCliente.getNrEndereco() + ", " +
															enderecoCliente.getNmBairro() + ", " +
															(cidadeEnderecoCliente!=null ? cidadeEnderecoCliente.getNmCidade() : "") + (ufEnderecoCliente!=null ? " - "+ufEnderecoCliente.getSgEstado() : "") + ", " +
															"CEP "+Util.formatCep(enderecoCliente.getNrCep(), ""));
					}
					fieldMap.put("<#Nome>", cliente.getNmPessoa());
					
					fieldMap.put("<#Celular>", cliente.getNrCelular());
					fieldMap.put("<#Telefone>", cliente.getNrTelefone1());
					fieldMap.put("<#EMail>", cliente.getNmEmail());
				}
				
				
			}
			
			/* PROCESSO */
			Processo processo = ProcessoDAO.get(cdProcesso, connect);
			
			if(processo!=null) {
				Pessoa advResponsavel = PessoaDAO.get(processo.getCdAdvogado(), connect);
				Pessoa advContrario = PessoaDAO.get(processo.getCdAdvogadoContrario(), connect);
				
				if(advResponsavel!=null)
					fieldMap.put("<#Advogado_responsavel>", advResponsavel.getNmPessoa());
				
				if(advContrario!=null)
					fieldMap.put("<#Advogado_adverso>", advContrario.getNmPessoa());
				
				fieldMap.put("<#Armario>", processo.getNmConteiner1());
				fieldMap.put("<#Gaveta>", processo.getNmConteiner2());
				fieldMap.put("<#Pasta>", processo.getNmConteiner3());
				
				Cidade cidadeComarca = CidadeDAO.get(processo.getCdCidade(), connect);
				if(cidadeComarca!=null) {
					Estado estadoComarca = EstadoDAO.get(cidadeComarca.getCdEstado(), connect);
					
					fieldMap.put("<#Comarca>", cidadeComarca.getNmCidade());
					fieldMap.put("<#Estado_Comarca>", estadoComarca.getNmEstado());
				}
				
				Orgao correspondente = OrgaoDAO.get(processo.getCdOrgao(), connect);
				if(correspondente!=null)
					fieldMap.put("<#Instituicao>", correspondente.getNmOrgao());
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.CD_ORGAO", Integer.toString(processo.getCdOrgao()), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmCorrespondente = OrgaoServices.find(criterios, connect);
				if(rsmCorrespondente.next()) {
					if(rsmCorrespondente.getString("nm_pessoa")!=null) {
						fieldMap.put("<#Instituicao>", rsmCorrespondente.getString("nm_pessoa"));
					}
					if(rsmCorrespondente.getString("nm_email")!=null) {
						fieldMap.put("<#Email_correspondente>", rsmCorrespondente.getString("nm_email"));
					}
				}
				
				if(processo.getDtSituacao()!=null)
					fieldMap.put("<#Data_fase>", Util.formatDate(processo.getDtSituacao(), "dd/MM/yyyy"));
				if(processo.getDtAutuacao()!=null)
					fieldMap.put("<#Data_autuacao>", Util.formatDate(processo.getDtAutuacao(), "dd/MM/yyyy"));
				if(processo.getDtCadastro()!=null)
					fieldMap.put("<#Data_cadastro>", Util.formatDate(processo.getDtCadastro(), "dd/MM/yyyy"));
				if(processo.getDtDistribuicao()!=null)
					fieldMap.put("<#Data_distribuicao>", Util.formatDate(processo.getDtDistribuicao(), "dd/MM/yyyy"));
				if(processo.getDtSentenca()!=null)
					fieldMap.put("<#Data_sentenca>", Util.formatDate(processo.getDtSentenca(), "dd/MM/yyyy"));
				
				TipoSituacao fase = TipoSituacaoDAO.get(processo.getCdTipoSituacao(), connect);
				if(fase != null)
					fieldMap.put("<#Fase_processo>", fase.getNmTipoSituacao());
				
				GrupoProcesso grupo = GrupoProcessoDAO.get(processo.getCdGrupoProcesso(), connect);
				if(grupo!=null)
					fieldMap.put("<#Grupo_processo>", grupo.getNmGrupoProcesso());
				
				fieldMap.put("<#Instancia>", ProcessoServices.tipoInstancia[processo.getTpInstancia()]);
				
				Juizo juizo = JuizoDAO.get(processo.getCdJuizo(), connect);
				if(juizo!=null) {
					String nrJuizo = ((processo.getNrJuizo()!=null && !processo.getNrJuizo().equals("")) ? processo.getNrJuizo() + " " : "");
					
					fieldMap.put("<#Sub_orgao>", nrJuizo + juizo.getSgJuizo()+"/"+juizo.getNmJuizo());
					fieldMap.put("<#Sigla_juizo>", nrJuizo + juizo.getSgJuizo());
					fieldMap.put("<#Nome_juizo>", nrJuizo + juizo.getNmJuizo());
				}
				
				ResultSetMap rsmAdverso = ProcessoServices.getOutraParte(processo.getCdProcesso(), connect);
				if(rsmAdverso.next())
					fieldMap.put("<#Nome_adverso>", rsmAdverso.getString("NM_PESSOA"));
				
				fieldMap.put("<#cd_processo>", Integer.toString(processo.getCdProcesso()));
				fieldMap.put("<#Numero_processo>", processo.getNrProcesso());
				fieldMap.put("<#Objeto_processo>", processo.getTxtObjeto());
				fieldMap.put("<#Possibilidade_perda>", ProcessoServices.tipoPerda[processo.getTpPerda()]);
				fieldMap.put("<#Situacao>", ProcessoServices.situacaoProcesso[processo.getStProcesso()]);
				fieldMap.put("<#Url_processo>", urlPortal+"/flex/Juris.jsp?prc="+Integer.toString(processo.getCdProcesso()));
				
				TipoProcesso tipoProcesso = TipoProcessoDAO.get(processo.getCdTipoProcesso(), connect);
				if(tipoProcesso != null)
					fieldMap.put("<#Tipo_acao>", tipoProcesso.getNmTipoProcesso());
				
				TipoPedido tipoPedido = TipoPedidoDAO.get(processo.getCdTipoPedido(), connect);
				if(tipoPedido != null)
					fieldMap.put("<#Tipo_pedido>", tipoPedido.getNmTipoPedido());
				
				
				fieldMap.put("<#Orgao_judicial>", "");
				
				fieldMap.put("<#Valor_causa>", "R$ "+Util.formatNumber(processo.getVlProcesso()));
				fieldMap.put("<#Valor_causa_extenso>", Util.formatExtenso(processo.getVlProcesso(), true));
				
				fieldMap.put("<#Valor_acordo>", "R$ "+Util.formatNumber(processo.getVlAcordo()));
				fieldMap.put("<#Valor_acordo_extenso>", Util.formatExtenso(processo.getVlAcordo(), true));
				
				ResultSetMap rsmOutrosInteressados = getOutrosInteressados(cdProcesso, connect);
				String outrosInteressados = "";
				while(rsmOutrosInteressados.next()) {
					outrosInteressados += rsmOutrosInteressados.getString("nm_qualificacao") + " " + rsmOutrosInteressados.getString("nm_outro_interessado");
					if(rsmOutrosInteressados.hasMore())
						outrosInteressados += ", ";
				}
				fieldMap.put("<#Litisconsorte>", outrosInteressados);
			}
			
			/* AGENDA */
			if(cdAgendaItem>0) {
				AgendaItem agenda = AgendaItemDAO.get(cdAgendaItem, connect);
				Pessoa responsavel = PessoaDAO.get(agenda.getCdPessoa(), connect);
								
				fieldMap.put("<#cd_agenda>", Integer.toString(cdAgendaItem));

				fieldMap.put("<#Url_agenda>", urlPortal+"/flex/Juris.jsp?agd="+Integer.toString(cdAgendaItem));
				
				if(responsavel!=null)
					fieldMap.put("<#Responsavel_agenda>", responsavel.getNmPessoa());
				
				if(agenda.getDtInicial()!=null)
					fieldMap.put("<#Data_agenda>", Util.formatDate(agenda.getDtInicial(), "dd/MM/yyyy HH:mm"));
				
				if(agenda.getDtFinal()!=null)
					fieldMap.put("<#Data_final_agenda>", Util.formatDate(agenda.getDtFinal(), "dd/MM/yyyy"));
				
				if(agenda.getDtAceite()!=null)
					fieldMap.put("<#Data_aceite_agenda>", Util.formatDate(agenda.getDtAceite(), "dd/MM/yyyy"));
				
				if(agenda.getDtRejeite()!=null)
					fieldMap.put("<#Data_rejeite_agenda>", Util.formatDate(agenda.getDtRejeite(), "dd/MM/yyyy"));
				
				if(agenda.getDsDetalhe()!=null && !agenda.getDsDetalhe().equals(""))
					fieldMap.put("<#Detalhe_agenda>", agenda.getDsDetalhe());
				
				TipoPrazo tipoPrazo = TipoPrazoDAO.get(agenda.getCdTipoPrazo(), connect);
				if(tipoPrazo != null)
					fieldMap.put("<#Tipo_prazo_agenda>", tipoPrazo.getNmTipoPrazo());
				
				fieldMap.put("<#Situacao_agenda>", AgendaItemServices.situacoesAgenda[agenda.getStAgendaItem()]);
			}
			
			/* ANDAMENTO */
			if(cdAndamento>0 && cdProcesso>0) {
				ProcessoAndamento andamento = ProcessoAndamentoDAO.get(cdAndamento, cdProcesso, connect);
								
				if(andamento.getDtAndamento()!=null)
					fieldMap.put("<#Data_andamento>", Util.formatDate(andamento.getDtAndamento(), "dd/MM/yyyy"));
				
				if(andamento.getDtLancamento()!=null)
					fieldMap.put("<#Data_lancamento_andamento>", Util.formatDate(andamento.getDtLancamento(), "dd/MM/yyyy HH:mm"));
				
				TipoAndamento tipo = TipoAndamentoDAO.get(andamento.getCdTipoAndamento(), connect);
				if(tipo != null)
					fieldMap.put("<#Tipo_andamento>", tipo.getNmTipoAndamento());
				
				if(andamento.getTxtAndamento()!=null && !andamento.getTxtAndamento().equals(""))
					fieldMap.put("<#Detalhe_andamento>", andamento.getTxtAndamento());
				
				fieldMap.put("<#Visibilidade_andamento>", ProcessoAndamentoServices.tipoVisibilidade[andamento.getTpVisibilidade()]);
			}
			
			return fieldMap;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoServices.getFieldsMapByProcesso: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap getPlaceholders() {
		
		
		/* EMPRESA */
		LinkedHashMap<String, String> phEmpresa = new LinkedHashMap<String, String>();
		phEmpresa.put("<#Cidade>", "Cidade (Local)");
		phEmpresa.put("<#Data_impressao>", "Data de Impressão");
		phEmpresa.put("<#Url_portal>", "Endereço do Portal (URL)");
		
		/* CLIENTE */	
		LinkedHashMap<String, String> phCliente = new LinkedHashMap<String, String>();

		phCliente.put("<#Bairro>", "Bairro");
		phCliente.put("<#CEP>", "CEP");
		phCliente.put("<#Cidade_endereco>", "Cidade (Endereço)");
		phCliente.put("<#Complemento_Endereco>", "Complemento Endereço");
		phCliente.put("<#Data_nascimento>", "Dt. Nascimento");
		phCliente.put("<#EMail>", "E-Mail");
		phCliente.put("<#Endereco>", "Endereço");
		phCliente.put("<#Endereco_completo>", "Endereço Completo");
		phCliente.put("<#Estado_civil>", "Estado Civil");
		phCliente.put("<#Naturalidade>", "Naturalidade");
		phCliente.put("<#Nome>", "Nome");
		phCliente.put("<#Mae>", "Nome da Mãe");
		phCliente.put("<#Pai>", "Nome do Pai");
		phCliente.put("<#Celular>", "Nº Celular");
		phCliente.put("<#CPF_CNPJ>", "Nº CPF/CNPJ");
		phCliente.put("<#Nr_endereco>", "Nº Endereço");
		phCliente.put("<#RG>", "Nº RG");
		phCliente.put("<#Telefone>", "Nº Telefone");
		phCliente.put("<#RG_Orgao>", "Órgão RG");
		
		/* PROCESSO */
		LinkedHashMap<String, String> phProcesso = new LinkedHashMap<String, String>();
		phProcesso.put("<#Advogado_responsavel>", "Advogado Responsável");
		phProcesso.put("<#Advogado_adverso>", "Advogado do Adverso");
		phProcesso.put("<#Armario>", "Arquivo: Armário");
		phProcesso.put("<#Gaveta>", "Arquivo: Gaveta");
		phProcesso.put("<#Pasta>", "Arquivo: Pasta");
		phProcesso.put("<#Comarca>", "Comarca");
		phProcesso.put("<#Instituicao>", "Correspondente");
		phProcesso.put("<#Data_fase>", "Data da Fase");
		phProcesso.put("<#Data_autuacao>", "Data de Autuação");
		phProcesso.put("<#Data_cadastro>", "Data de Cadastro");
		phProcesso.put("<#Data_distribuicao>", "Data de Distribuição");
		phProcesso.put("<#Data_sentenca>", "Data de Sentença");
		phProcesso.put("<#Estado_Comarca>", "Estado da Comarca");
		phProcesso.put("<#Email_correspondente>", "Email do Correspondente");
		phProcesso.put("<#Fase_processo>", "Fase");
		phProcesso.put("<#Grupo_processo>", "Grupo");
		phProcesso.put("<#Instancia>", "Instância");
		phProcesso.put("<#Litisconsorte>", "Litisconsorte");
		phProcesso.put("<#Nome_adverso>", "Nome do Adverso");
		phProcesso.put("<#Numero_processo>", "Nº do Processo");
		phProcesso.put("<#Objeto_processo>", "Objeto");
		phProcesso.put("<#Possibilidade_perda>", "Possibilidade de Perda");
		phProcesso.put("<#Sub_orgao>", "Juizo (Vara/Turma)");//Sub-orgão - Sigla/Nome
//		phProcesso.put("<#Sigla_juizo>", "Sigla do Juizo");//Sub-orgão - Sigla/Nome
//		phProcesso.put("<#Nome_juizo>", "");//Sub-orgão - Nome
		phProcesso.put("<#Situacao>", "Situação");
		phProcesso.put("<#Tipo_acao>", "Tipo de Ação");
		phProcesso.put("<#Tipo_pedido>", "Tipo de Pedido");
		//phProcesso.put("<#Orgao_judicial>", "");
		phProcesso.put("<#Valor_causa>", "Valor da Causa");
		phProcesso.put("<#Valor_causa_extenso>", "Valor da Causa por extenso");
		phProcesso.put("<#Valor_acordo>", "Valor do Acordo");
		phProcesso.put("<#Valor_acordo_extenso>", "Valor do Acordo por extenso");
		
		/* AGENDA */
		LinkedHashMap<String, String> phAgenda = new LinkedHashMap<String, String>();
		phAgenda.put("<#Data_agenda>", "Data da agenda");
		phAgenda.put("<#Data_final_agenda>", "Data Final da agenda");
		phAgenda.put("<#Data_aceite_agenda>", "Data do Aceite");
		phAgenda.put("<#Data_rejeite_agenda>", "Data do Rejeite");
		phAgenda.put("<#Detalhe_agenda>", "Detalhe da agenda");
		phAgenda.put("<#Responsavel_agenda>", "Responsável pela agenda");
		phAgenda.put("<#Situacao_agenda>", "Situação da agenda");
		phAgenda.put("<#Tipo_prazo_agenda>", "Tipo de agenda");
		
		/* ANDAMENTO */
		LinkedHashMap<String, String> phAndamento = new LinkedHashMap<String, String>();
		phAndamento.put("<#Data_andamento>", "Data do Andamento");
		phAndamento.put("<#Data_lancamento_andamento>", "Data de Lançamento do Andamento");
		phAndamento.put("<#Detalhe_andamento>", "Detalhe do andamento");
		phAndamento.put("<#Tipo_andamento>", "Tipo de Andamento");
		phAndamento.put("<#Visibilidade_andamento>", "Tipo de Visibilidade do Andamento");

		ResultSetMap rsmPlaceholders = new ResultSetMap();
		
		Iterator<?> it = phEmpresa.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
	        HashMap<String, Object> reg = new HashMap<String, Object>();

	        reg.put("NM_GRUPO_PLACEHOLDER", "EMPRESA");
	        reg.put("ID_PLACEHOLDER", pair.getKey());
	        reg.put("DS_PLACEHOLDER", pair.getValue());
	        
	        it.remove();
	        rsmPlaceholders.addRegister(reg);
	    }
	    
	    it = phCliente.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
	        HashMap<String, Object> reg = new HashMap<String, Object>();

	        reg.put("NM_GRUPO_PLACEHOLDER", "CLIENTE");
	        reg.put("ID_PLACEHOLDER", pair.getKey());
	        reg.put("DS_PLACEHOLDER", pair.getValue());
	        
	        it.remove();
	        rsmPlaceholders.addRegister(reg);
	    }
	    
	    it = phProcesso.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
	        HashMap<String, Object> reg = new HashMap<String, Object>();

	        reg.put("NM_GRUPO_PLACEHOLDER", "PROCESSO");
	        reg.put("ID_PLACEHOLDER", pair.getKey());
	        reg.put("DS_PLACEHOLDER", pair.getValue());
	        
	        it.remove();
	        rsmPlaceholders.addRegister(reg);
	    }

	    it = phAgenda.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
	        HashMap<String, Object> reg = new HashMap<String, Object>();

	        reg.put("NM_GRUPO_PLACEHOLDER", "AGENDA");
	        reg.put("ID_PLACEHOLDER", pair.getKey());
	        reg.put("DS_PLACEHOLDER", pair.getValue());
	        
	        it.remove();
	        rsmPlaceholders.addRegister(reg);
	    }

	    it = phAndamento.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
	        HashMap<String, Object> reg = new HashMap<String, Object>();

	        reg.put("NM_GRUPO_PLACEHOLDER", "ANDAMENTO");
	        reg.put("ID_PLACEHOLDER", pair.getKey());
	        reg.put("DS_PLACEHOLDER", pair.getValue());
	        
	        it.remove();
	        rsmPlaceholders.addRegister(reg);
	    }
	    
	    return rsmPlaceholders;
	}
	
	
	/*
	 * DEPRECATED
	 */
	public static ResultSetMap findCidade(ArrayList<ItemComparator> criterios) {
		Connection connect = Conexao.conectar();
 		return Search.find("SELECT A.* FROM grl_cidade A ",
						   "ORDER BY nm_cidade", criterios, connect, connect==null);
	}

	public static ResultSetMap findClienteByName(String nmPessoa) {
		return findClienteByName(nmPessoa, null);
	}
	
	public static ResultSetMap findClienteByName(String nmPessoa, Connection connection) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		if (nmPessoa != null && !nmPessoa.equals(""))
			criterios.add(new ItemComparator("A.nm_pessoa", nmPessoa, ItemComparator.EQUAL, Types.VARCHAR));
		return findCliente(criterios, connection);
	}
	
	public static ResultSetMap findCliente(ArrayList<ItemComparator> criterios) {
		return findCliente(criterios, null);
	}
	
	public static ResultSetMap findCliente(ArrayList<ItemComparator> criterios, Connection connection) {
		boolean isConnectionNull = connection == null;
		
		if(isConnectionNull)
			connection = Conexao.conectar();
		
		int cdVinculoCliente = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CLIENTE", -10, 0, connection);
		
		String nrDocumento = null;
		String nmPessoa = null;
		int cdProcesso = 0;
		String qtLimite = "";
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("NR_DOCUMENTO")) {
				nrDocumento = criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("A.NM_PESSOA")) {
				nmPessoa = Util.limparTexto(criterios.get(i).getValue());
				nmPessoa = "%"+nmPessoa.replaceAll(" ", "%")+"%";
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("E.CD_PROCESSO")) {
				cdProcesso = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				i--;
			} 
			else if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
				qtLimite = criterios.get(i).getValue();
			}
			else
				crt.add(criterios.get(i));
		}
		
		return Search.find("SELECT A.*, C.nr_cpf, D.nr_cnpj, C.lg_documento_ausente as lg_cpf_ausente, D.lg_documento_ausente as lg_cnpj_ausente " +
						   "FROM grl_pessoa A " +
						   "JOIN grl_pessoa_empresa B ON (A.cd_pessoa = B.cd_pessoa " +
						   "                          AND B.cd_vinculo = "+cdVinculoCliente+") "+
						   " LEFT OUTER JOIN grl_pessoa_fisica C ON (A.cd_pessoa = C.cd_pessoa) "+
						   " LEFT OUTER JOIN grl_pessoa_juridica D ON (A.cd_pessoa = D.cd_pessoa) "+
						   (cdProcesso>0 ? " LEFT OUTER JOIN prc_parte_cliente E ON (A.cd_pessoa = E.cd_pessoa) " : "")+
						   " WHERE 1=1 "+
						   (cdProcesso>0 ? " AND E.cd_processo="+cdProcesso : "")+
						   (nrDocumento!=null ? " AND (C.nr_cpf LIKE '"+nrDocumento+"' OR D.nr_cnpj LIKE '"+nrDocumento+"')" : "")+
						   (nmPessoa!=null ? 
								   (Util.getConfManager().getIdOfDbUsed().equals("FB") ? (" AND A.nm_pessoa LIKE '"+nmPessoa+"' ") :  
									   (" AND TRANSLATE(A.nm_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '"+nmPessoa+"' "))
								:
									""),
						   "ORDER BY nm_pessoa", crt, connection, isConnectionNull);
	}

	public static ResultSetMap findAdvogado(ArrayList<ItemComparator> criterios) {
		Connection connect = Conexao.conectar();
		int cdVinculoAdvogado = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_ADVOGADO", 0);
		criterios.add(new ItemComparator("A.st_cadastro", Integer.toString(PessoaServices.ST_ATIVO), ItemComparator.EQUAL, Types.INTEGER));
		String nmPessoa = null;
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("A.NM_PESSOA")) {
				nmPessoa = criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
		}
		return Search.find("SELECT * " +
						   "FROM grl_pessoa A " +
						   "JOIN grl_pessoa_empresa B ON (A.cd_pessoa = B.cd_pessoa " +
						   "                          AND B.cd_vinculo = "+cdVinculoAdvogado+")"+
						   "WHERE A.st_cadastro = 1 "+
						   (nmPessoa!=null ? 
								   (Util.getConfManager().getIdOfDbUsed().equals("FB") ? (" AND A.nm_pessoa LIKE '%"+nmPessoa+"%' ") :  
									   (" AND TRANSLATE(A.nm_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmPessoa)+"%' "))
								:
									""),
						   "ORDER BY nm_pessoa", criterios, connect, connect==null);
	}

	public static ResultSetMap findAdvogadoAdverso(ArrayList<ItemComparator> criterios) {
		Connection connect = Conexao.conectar();
		int cdVinculoAdvogadoAdverso = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_ADVOGADO_ADVERSO", -21);
		criterios.add(new ItemComparator("A.st_cadastro", Integer.toString(PessoaServices.ST_ATIVO), ItemComparator.EQUAL, Types.INTEGER));
		String nmPessoa = null;
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("A.NM_PESSOA")) {
				nmPessoa = criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
		}
 		return Search.find("SELECT * " +
						   " FROM grl_pessoa A " +
						   " JOIN grl_pessoa_empresa B ON (A.cd_pessoa = B.cd_pessoa " +
						   "                          AND B.cd_vinculo = "+cdVinculoAdvogadoAdverso+") " +
						   " WHERE 1=1 ",
						   " AND TRANSLATE(A.nm_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmPessoa)+"%' " +
						   " ORDER BY nm_pessoa", criterios, connect, connect==null);
	}
	
	public static ResultSetMap findAdversoByName(String nmPessoa) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		if (nmPessoa != null && !nmPessoa.equals(""))
			criterios.add(new ItemComparator("A.nm_pessoa", nmPessoa, ItemComparator.EQUAL, Types.VARCHAR));
		return findAdverso(criterios);
	}
	
	public static ResultSetMap findAdverso(ArrayList<ItemComparator> criterios) {
		Connection connect = Conexao.conectar();
		int cdVinculoAdverso = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_ADVERSO", -20);
		
		String nrDocumento = null;
		String nmPessoa = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("NR_DOCUMENTO")) {
				nrDocumento = criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("A.NM_PESSOA")) {
				nmPessoa = criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
		}
		
 		return Search.find("SELECT A.*, C.nr_cpf, D.nr_cnpj, C.lg_documento_ausente as lg_cpf_ausente, D.lg_documento_ausente as lg_cnpj_ausente " +
						   "FROM grl_pessoa A " +
						   "JOIN grl_pessoa_empresa B ON (A.cd_pessoa = B.cd_pessoa " +
						   "                          AND B.cd_vinculo = "+cdVinculoAdverso+")"+
						   " LEFT OUTER JOIN grl_pessoa_fisica C ON (A.cd_pessoa = C.cd_pessoa) "+
						   " LEFT OUTER JOIN grl_pessoa_juridica D ON (A.cd_pessoa = D.cd_pessoa) "+
						   " WHERE 1=1 "+
						   (nrDocumento!=null ? " AND (C.nr_cpf LIKE '"+nrDocumento+"' OR D.nr_cnpj LIKE '"+nrDocumento+"')" : "") +
						   (Util.getConfManager().getIdOfDbUsed().equals("FB") ? 
								   (!nmPessoa.equals("") ? " AND A.nm_pessoa LIKE '%" + nmPessoa + "%'":"") :
								   (!nmPessoa.equals("") ?
										   " AND TRANSLATE(A.nm_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmPessoa)+"%' ": "")),
						   "ORDER BY nm_pessoa", criterios, connect, connect==null);
	}

	public static ResultSetMap getGrupoProcessoOfUsuario(int cdUsuario) {
		Connection connect = Conexao.conectar();
 		return Search.find("SELECT * " +
						   "FROM prc_usuario_grupo " +
						   "WHERE cd_usuario = "+cdUsuario, new ArrayList<ItemComparator>(), connect, connect==null);
	}

	public static ResultSetMap getOrgaoOfUsuario(int cdUsuario) {
		Connection connect = Conexao.conectar();
 		return Search.find("SELECT * " +
						   "FROM prc_usuario_orgao " +
						   "WHERE cd_usuario = "+cdUsuario, new ArrayList<ItemComparator>(), connect, connect==null);
	}

	public static ResultSetMap findSite(ArrayList<ItemComparator> criterios, ArrayList<String> groupBy) {
		return findSite(criterios, groupBy, null);
	}

	public static ResultSetMap findSite(ArrayList<ItemComparator> criterios, ArrayList<String> groupBy, Connection connect) {
		boolean isConnectionNull = connect==null;
		try	{
			if (isConnectionNull)
				connect = Conexao.conectar();
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			String sql = "";
			boolean lgMovimentacao = false;
			for(int i=0; i<criterios.size(); i++)	{
				if(criterios.get(i).getColumn().equalsIgnoreCase("cd_cliente"))	{
					sql += " AND EXISTS (SELECT * FROM prc_parte_cliente PC " +
						   "             WHERE PC.cd_processo = A.cd_processo " +
						   "               AND PC.cd_pessoa = "+criterios.get(i).getValue()+")";
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("cd_adverso"))	{
					sql += " AND EXISTS (SELECT * FROM prc_outra_parte OP " +
						   "             WHERE OP.cd_processo = A.cd_processo " +
						   "               AND OP.cd_pessoa = "+criterios.get(i).getValue()+")";
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("lg_movimentacao"))	{
					lgMovimentacao = true;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("nm_cliente"))	{
					sql += " AND EXISTS (SELECT * FROM prc_parte_cliente PC, grl_pessoa P " +
						   "             WHERE PC.cd_processo = A.cd_processo " +
						   "               AND PC.cd_pessoa   = P.cd_pessoa " +
						   "               AND nm_pessoa LIKE \'%"+criterios.get(i).getValue()+"%\')";
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("nm_parte_contraria"))	{
					sql += " AND EXISTS (SELECT * FROM prc_outra_parte PC, grl_pessoa P " +
					   	   "             WHERE PC.cd_processo = A.cd_processo " +
					       "               AND PC.cd_pessoa   = P.cd_pessoa " +
					       "               AND nm_pessoa LIKE \'%"+criterios.get(i).getValue()+"%\')";
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("sem_andamento_a"))	{
					GregorianCalendar data = new GregorianCalendar();
					data.add(GregorianCalendar.DATE, -1*Integer.parseInt(criterios.get(i).getValue()));
					sql += " AND (A.dt_ultimo_andamento < \'"+com.tivic.manager.util.Util.formatDate(data, "MM/dd/yyyy")+"\'"+
						   "  OR A.dt_ultimo_andamento IS NULL)";
				}
				else
					crt.add(criterios.get(i));
			}
			String groups = "";
			String fields = " A.*, B.nm_juizo, B.nm_juizo AS nm_orgao_judicial, B.id_juizo, B.id_juizo AS id_orgao_judicial," +
					        " C.nm_tribunal, C.nm_tribunal AS nm_comarca, C.id_tribunal, C.id_tribunal AS id_comarca, " +
					        " D.nm_tipo_processo, D.nm_parte, D.nm_contra_parte, "+
			                " D.nm_outro_interessado, E.nm_area_direito, F.nm_orgao, G.nm_tipo_situacao, "+
			                " H.nm_pessoa AS nm_advogado, I.nm_pessoa AS nm_advogado_contrario, "+
			                " J.nm_pessoa AS nm_advogado_titular, Q.nm_pessoa AS nm_responsavel_arquivo, "+
			                " \' \' AS nm_juiz, L.nm_grupo_processo, M.nm_pessoa AS nm_oficial_justica, "+
			                " N.nm_cidade AS nm_cidade_orgao, O.nm_tipo_pedido, P.nm_tipo_objeto, "+
			                " (SELECT MAX(cd_andamento) FROM prc_processo_andamento X "+
			                "  WHERE X.cd_processo  = A.cd_processo "+
			                "    AND X.dt_andamento = A.dt_ultimo_andamento) as cd_ultimo_andamento "+
			                (lgMovimentacao ? ", Z.*, Y.nm_tipo_andamento " : "");
			// Processa agrupamentos enviados em groupBy
			String [] retorno = com.tivic.manager.util.Util.getFieldsAndGroupBy(groupBy, fields, groups,
					                                                     "SUM(A.VL_PROCESSO) AS VL_PROCESSO, COUNT(*) AS QT_PROCESSO");
			fields = retorno[0];
			groups = retorno[1];
			ResultSetMap rsm = Search.find(
					"SELECT "+fields+
	                "FROM prc_processo A "+
	                "LEFT OUTER JOIN prc_juizo          B ON (A.cd_juizo = B.cd_juizo) "+
	                "LEFT OUTER JOIN prc_tribunal       C ON (A.cd_tribunal = C.cd_tribunal) "+
	                "LEFT OUTER JOIN prc_tipo_processo  D ON (A.cd_tipo_processo = D.cd_tipo_processo) "+
	                "LEFT OUTER JOIN prc_area_direito   E ON (D.cd_area_direito = E.cd_area_direito) "+
	                "LEFT OUTER JOIN prc_orgao          F ON (A.cd_orgao = F.cd_orgao) "+
	                "LEFT OUTER JOIN prc_tipo_situacao  G ON (A.cd_tipo_situacao = G.cd_tipo_situacao) "+
	                "LEFT OUTER JOIN grl_pessoa         H ON (A.cd_advogado = H.cd_pessoa) "+
	                "LEFT OUTER JOIN grl_pessoa         I ON (A.cd_advogado_contrario = I.cd_pessoa) "+
	                "LEFT OUTER JOIN grl_pessoa         J ON (A.cd_advogado_titular = J.cd_pessoa) "+
	                "LEFT OUTER JOIN prc_grupo_processo L ON (A.cd_grupo_processo = L.cd_grupo_processo) "+
	                "LEFT OUTER JOIN grl_pessoa         M ON (A.cd_oficial_justica = M.cd_pessoa) "+
	                "LEFT OUTER JOIN grl_cidade         N ON (A.cd_cidade = N.cd_cidade) "+
	                "LEFT OUTER JOIN prc_tipo_pedido    O ON (A.cd_tipo_pedido = O.cd_tipo_pedido) "+
	                "LEFT OUTER JOIN prc_tipo_objeto    P ON (A.cd_tipo_objeto = P.cd_tipo_objeto) "+
	                "LEFT OUTER JOIN grl_pessoa         Q ON (A.cd_responsavel_arquivo = Q.cd_pessoa) "+
	                (lgMovimentacao ? "LEFT OUTER JOIN prc_processo_andamento Z ON (A.cd_processo = Z.cd_processo) " +
	                		          "LEFT OUTER JOIN prc_tipo_andamento     Y ON (Z.cd_tipo_andamento = Y.cd_tipo_andamento) " : "")+
	                "WHERE 1=1 "+sql, groups+" ORDER BY nr_processo"+(lgMovimentacao?", Z.dt_andamento, Z.cd_andamento":""), crt, connect, false);
			connect = Conexao.conectar();

			PreparedStatement pstmtCliente = connect.prepareStatement("SELECT nm_pessoa, id_pessoa FROM prc_parte_cliente A, grl_pessoa B " +
					                                                  "WHERE A.cd_pessoa   = B.cd_pessoa " +
					                                                  "  AND A.cd_processo = ?");
			PreparedStatement pstmtAdverso = connect.prepareStatement("SELECT nm_pessoa, id_pessoa FROM prc_outra_parte A, grl_pessoa B " +
                    												  "WHERE A.cd_pessoa   = B.cd_pessoa " +
                    												  "  AND A.cd_processo = ?");
			while(rsm.next())	{
				// Cliente
				String nmCliente = "";
				pstmtCliente.setInt(1, rsm.getInt("cd_processo"));
				ResultSet rs = pstmtCliente.executeQuery();
				while(rs.next())
					nmCliente += (nmCliente.equals("")?"":", ")+
					             (rs.getString("id_pessoa")!=null && !rs.getString("id_pessoa").equals("")? rs.getString("id_pessoa") : rs.getString("nm_pessoa"));
				// Adverso
				String nmAdverso = "";
				pstmtAdverso.setInt(1, rsm.getInt("cd_processo"));
				rs = pstmtAdverso.executeQuery();
				while(rs.next())
					nmAdverso += (nmAdverso.equals("")?"":", ")+
					             (rs.getString("id_pessoa")!=null && !rs.getString("id_pessoa").equals("") ? rs.getString("id_pessoa") : rs.getString("nm_pessoa"));
				//
				rsm.setValueToField("NM_CLIENTE", nmCliente);
				rsm.setValueToField("NM_ADVERSO", nmAdverso);
			}
			rsm.beforeFirst();
	        return rsm;
		}
		catch(Exception e)	{
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result findAgenda(ArrayList<ItemComparator> criterios)	{
		Connection connect = Conexao.conectar();
		try	{
			boolean lgSomentePeriodo = false;
			GregorianCalendar dtInicial = null;
			GregorianCalendar dtFinal   = null;
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for(int i=0; i<criterios.size(); i++)
				if(criterios.get(i).getColumn().equalsIgnoreCase("lg_somente_periodo"))	{
					lgSomentePeriodo = true;
					criterios.remove(i);
					break;
				}

			String sql = "SELECT A.*, B.nm_tipo_prazo, C.nm_pessoa, F.nm_grupo_processo, G.nm_orgao, " +
					     "       CAST(dt_inicial AS DATE) AS data, " +
				         "       D.nr_processo, D.nr_juizo, D.nm_conteiner3, H.nm_juizo, H.nm_juizo AS nm_orgao_judicial, " +
				         "       I.nm_tribunal, I.nm_tribunal AS nm_comarca, I.id_tribunal, I.id_tribunal AS id_comarca, " +
				         "       J.nm_cidade AS nm_cidade_orgao "+
				         "FROM agd_agenda_item A "+
				         "LEFT OUTER JOIN prc_tipo_prazo     B ON (A.cd_tipo_prazo = B.cd_tipo_prazo) "+
				         "LEFT OUTER JOIN grl_pessoa   		 C ON (A.cd_pessoa = C.cd_pessoa) "+
				         "LEFT OUTER JOIN prc_processo 		 D ON (A.cd_processo = D.cd_processo) "+
				         "LEFT OUTER JOIN prc_grupo_processo F ON (D.cd_grupo_processo = F.cd_grupo_processo) " +
				         "LEFT OUTER JOIN prc_orgao          G ON (D.cd_orgao = G.cd_orgao) " +
			             "LEFT OUTER JOIN prc_juizo          H ON (D.cd_juizo = H.cd_juizo) "+
			             "LEFT OUTER JOIN prc_tribunal       I ON (D.cd_tribunal = I.cd_tribunal) " +
			             "LEFT OUTER JOIN grl_cidade         J ON (D.cd_cidade = J.cd_cidade) "+
				         "WHERE 1=1 ";
			if(!lgSomentePeriodo)	{
				for(int i=0; i<criterios.size(); i++)	{
					if(criterios.get(i).getColumn().equalsIgnoreCase("dt_inicial") && criterios.get(i).getTypeComparation()==ItemComparator.MINOR_EQUAL)	{
						dtFinal = Util.stringToCalendar(criterios.get(i).getValue());
						dtFinal.set(GregorianCalendar.HOUR, 23);
						dtFinal.set(GregorianCalendar.MINUTE, 59);
					}
					else if(criterios.get(i).getColumn().equalsIgnoreCase("dt_inicial") && criterios.get(i).getTypeComparation()==ItemComparator.GREATER_EQUAL)	{
						dtInicial = Util.stringToCalendar(criterios.get(i).getValue());
						dtInicial.set(GregorianCalendar.HOUR, 23);
						dtInicial.set(GregorianCalendar.MINUTE, 59);
					}
					else
						crt.add(criterios.get(i));
				}
				sql +=  "  AND (dt_inicial >= \'"+com.tivic.manager.util.Util.formatDate(dtInicial, "MM/dd/yyyy")+" 00:00\' "+
		                "    OR st_agenda_item = 0) "+
		                "  AND dt_inicial <= \'"+com.tivic.manager.util.Util.formatDate(dtFinal, "MM/dd/yyyy")+" 23:59\' ";
			}
			else
				crt = criterios;
			ResultSetMap rsm = Search.find(sql, " ORDER BY dt_inicial", crt, connect, false);

			connect = Conexao.conectar();
			PreparedStatement pstmtCliente = connect.prepareStatement("SELECT nm_pessoa FROM prc_parte_cliente A, grl_pessoa B " +
												                      "WHERE A.cd_pessoa = B.cd_pessoa " +
												                      "  AND A.cd_processo = ?");
			PreparedStatement pstmtAdverso = connect.prepareStatement("SELECT nm_pessoa FROM prc_outra_parte A, grl_pessoa B " +
																	  "WHERE A.cd_pessoa = B.cd_pessoa " +
																	  "  AND A.cd_processo = ?");
			while(rsm.next())	{
				// Cliente
				String nmCliente = "";
				pstmtCliente.setInt(1, rsm.getInt("cd_processo"));
				ResultSet rs = pstmtCliente.executeQuery();
				while(rs.next())
					nmCliente += (nmCliente.equals("")?"":", ")+rs.getString("nm_pessoa");
				// Adverso
				String nmAdverso = "";
				pstmtAdverso.setInt(1, rsm.getInt("cd_processo"));
				rs = pstmtAdverso.executeQuery();
				while(rs.next())
					nmAdverso += (nmAdverso.equals("")?"":", ")+rs.getString("nm_pessoa");
				//
				rsm.setValueToField("NM_CLIENTE", nmCliente);
				rsm.setValueToField("NM_ADVERSO", nmAdverso);
			}
			rsm.beforeFirst();
			return new Result(1, "", "rsm", rsm);
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar pesquisar agenda!", e);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Salva os dados de ajuizamento em um processo pré-cadastrado
	 * 
	 * @param cdProcesso código de processo
	 * @param nrProcesso número do processo
	 * @param nrJuizo número do juízo
	 * @param cdJuizo código do juízo
	 * @return Result
	 * @since 26/06/2014
	 * @author Maurício
	 */
	public static Result ajuizar(int cdProcesso, String nrProcesso, String nrJuizo, int cdJuizo) {
		return ajuizar(cdProcesso, nrProcesso, nrJuizo, cdJuizo, null);
	}
	
	public static Result ajuizar(int cdProcesso, String nrProcesso, String nrJuizo, int cdJuizo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			Processo processo = ProcessoDAO.get(cdProcesso, connect);
			
			if(processo==null) {
				return new Result(-1, "Erro ao ajuizar o processo. Objeto processo é nulo.");
			}
			
			processo.setNrProcesso(nrProcesso);
			processo.setNrJuizo(nrJuizo);
			processo.setCdJuizo(cdJuizo);
			
			return ProcessoServices.save(processo, connect);
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static String getNextNrInterno() {
		return getNextNrInterno(null);
	}
	
	public static String getNextNrInterno(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			GregorianCalendar hoje = new GregorianCalendar();
			
			int code = Conexao.getSequenceCode("PRC_PROCESSO_NR_INTERNO_"+hoje.get(Calendar.YEAR), connect);
			
			DecimalFormat df = new DecimalFormat("00000");
			
			return "PRE."+df.format(code)+"."+hoje.get(Calendar.YEAR);
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
		finally {
			
		}
	}
	
	/**
	 * Atualiza campos dos processos de uma lista
	 * 
	 * @param cdProcessos lista de códigos de processos
	 * @param regUpdate atributos a serem atualizados
	 * @return
	 */
	public static Result updateProcessos(ArrayList<Integer> cdProcessos, HashMap<String, Object> regUpdate) {
		return updateProcessos(cdProcessos, regUpdate, 0, null);
	}
	
	public static Result updateProcessos(ArrayList<Integer> cdProcessos, HashMap<String, Object> regUpdate, int cdUsuario) {
		return updateProcessos(cdProcessos, regUpdate, cdUsuario, null);
	}
	
	public static Result updateProcessos(ArrayList<Integer> cdProcessos, HashMap<String, Object> regUpdate, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			Result result = null;
			for (int cdProcesso : cdProcessos) {
				
				ResultSetMap rsmCliente = new ResultSetMap();
				ResultSetMap rsmAdverso = new ResultSetMap();
				
				Processo processo = ProcessoDAO.get(cdProcesso, connect);
				
				if(processo==null) {
					return new Result(-2, "Erro ao atualizar processos.");
				}
				
				// ADVOGADO
				int cdAdvogado = (Integer)(regUpdate.get("CD_ADVOGADO")!=null ? regUpdate.get("CD_ADVOGADO") : 0);
				if(cdAdvogado>0) {
					processo.setCdAdvogado(cdAdvogado);
				}
				
				// ADVOGADO ADVERSO
				int cdAdvogadoAdverso = (Integer)(regUpdate.get("CD_ADVOGADO_ADVERSO")!=null ? regUpdate.get("CD_ADVOGADO_ADVERSO") : 0);
				if(cdAdvogadoAdverso>0) {
					processo.setCdAdvogadoContrario(cdAdvogadoAdverso);
				}
				
				// CLIENTE
				int cdCliente = (Integer)(regUpdate.get("CD_CLIENTE")!=null ? regUpdate.get("CD_CLIENTE") : 0);
				int cdClienteOld = (Integer)(regUpdate.get("CD_CLIENTE_OLD")!=null ? regUpdate.get("CD_CLIENTE_OLD") : 0);
				if(cdCliente>0) {
					if(cdClienteOld>0) {
						result = ProcessoServices.deleteParteCliente(cdProcesso, cdClienteOld, connect);
						if(result.getCode()<0) {
							return result;
						}
					}
					
					HashMap<String, Object> regCliente = new HashMap<String, Object>();
					regCliente.put("CD_PESSOA", cdCliente);
					rsmCliente.addRegister(regCliente);
				}
				
				// ADVERSO
				int cdAdverso = (Integer)(regUpdate.get("CD_ADVERSO")!=null ? regUpdate.get("CD_ADVERSO") : 0);
				int cdAdversoOld = (Integer)(regUpdate.get("CD_ADVERSO_OLD")!=null ? regUpdate.get("CD_ADVERSO_OLD") : 0);
				if(cdAdverso>0) {
					if(cdAdversoOld>0) {
						result = ProcessoServices.deleteAdverso(cdProcesso, cdAdversoOld, connect);
						if(result.getCode()<0) {
							return result;
						}
					}
					
					HashMap<String, Object> regAdverso = new HashMap<String, Object>();
					regAdverso.put("CD_PESSOA", cdAdverso);
					rsmAdverso.addRegister(regAdverso);
				}
				
				// GRUPO DE PROCESSO
				int cdGrupoProcesso = (Integer)(regUpdate.get("CD_GRUPO_PROCESSO")!=null ? regUpdate.get("CD_GRUPO_PROCESSO") : 0);
				if(cdGrupoProcesso>0) {
					processo.setCdGrupoProcesso(cdGrupoProcesso);
				}
				
				// CORRESPONDENTE
				int cdOrgao = (Integer)(regUpdate.get("CD_ORGAO")!=null ? regUpdate.get("CD_ORGAO") : 0);
				if(cdOrgao>0) {
					processo.setCdOrgao(cdOrgao);
				}
				
				// TIPO PROCESSO
				int cdTipoProcesso = (Integer)(regUpdate.get("CD_TIPO_PROCESSO")!=null ? regUpdate.get("CD_TIPO_PROCESSO") : 0);
				if(cdTipoProcesso>0) {
					processo.setCdTipoProcesso(cdTipoProcesso);
				}
				
				// TIPO SITUACAO (FASE)
				int cdTipoSituacao = (Integer)(regUpdate.get("CD_TIPO_SITUACAO")!=null ? regUpdate.get("CD_TIPO_SITUACAO") : 0);
				if(cdTipoSituacao>0) {
					processo.setCdTipoSituacao(cdTipoSituacao);
				}
				
				// SITUACAO PROCESSO
				int stProcesso = (Integer)(regUpdate.get("ST_PROCESSO")!=null ? regUpdate.get("ST_PROCESSO") : -1);
				if(stProcesso>-1) {
					processo.setStProcesso(stProcesso);
				}
				
				// COMARCA
				int cdCidade = (Integer)(regUpdate.get("CD_CIDADE")!=null ? regUpdate.get("CD_CIDADE") : 0);
				if(cdCidade>0) {
					processo.setCdCidade(cdCidade);
				}
				
				// NR JUIZO
				String nrJuizo = (String)(regUpdate.get("NR_JUIZO")!=null ? regUpdate.get("NR_JUIZO") : "");
				if(!nrJuizo.equals("")) {
					processo.setNrJuizo(nrJuizo);
				}
				// JUIZO
				int cdJuizo = (Integer)(regUpdate.get("CD_JUIZO")!=null ? regUpdate.get("CD_JUIZO") : 0);
				if(cdJuizo>0) {
					processo.setCdJuizo(cdJuizo);
				}
				// GRUPO DE TRABALHO
				int cdGrupoTrabalho = (Integer)(regUpdate.get("CD_GRUPO")!=null ? regUpdate.get("CD_GRUPO") : 0);
				if(cdGrupoTrabalho>0) {
					processo.setCdGrupoTrabalho(cdGrupoTrabalho);
				}
				
				result = save(processo, rsmCliente, rsmAdverso, null, cdUsuario, null, connect);
				if(result.getCode()<0) {
					return result;
				}
			}
			
			return new Result(1, "Processos salvos com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			if(isConnectionNull) {
				Conexao.desconectar(connect);
			}
		}
	}	
	
	public static ResultSetMap getAllProcessosVinculados(int cdProcessoPrincipal) {
		return getAllProcessosVinculados(cdProcessoPrincipal, null);
	}
	
	public static ResultSetMap getAllProcessosVinculados(int cdProcessoPrincipal, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_PROCESSO_PRINCIPAL", Integer.toString(cdProcessoPrincipal), ItemComparator.EQUAL, Types.INTEGER));
			
			return find(criterios);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getAllProcessosVinculados: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap findProcessoAgrupado(ArrayList<ItemComparator> criterios, String nmAgrupamento){
		return findProcessoAgrupado(criterios, nmAgrupamento, null);
	}
	
	public static ResultSetMap findProcessoAgrupado(ArrayList<ItemComparator> criterios, String nmAgrupamento, Connection connect){
		boolean isConnectionNull = connect==null;
		ResultSetMap rsm = new ResultSetMap();
		String orderField = "QT_PROCESSO";
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsmProcessos = find(criterios, new ArrayList<String>(), connect);
			ArrayList<Integer> processos = new ArrayList<>();
			while(rsmProcessos.next()) {
				processos.add(rsmProcessos.getInt("cd_processo"));
			}
			
			/**
			 * Buscar por Advogado da causa
			 **/
			if (nmAgrupamento.equals("AGRUPADO_ADVOGADO_RESPONSAVEL") || nmAgrupamento.equals("AGRUPADO_ADVOGADO_ADVERSO")) {
				rsm = ProcessoServices.getAdvogadoAgrupado(nmAgrupamento, processos, connect);
			}
			
			/**
			 * Buscar por Orgão
			 **/
			if (nmAgrupamento.equals("AGRUPADO_INSTITUICAO")) {
				rsm = OrgaoServices.getOrgaoAgrupado(nmAgrupamento, processos, connect);
			}
			
			/**
			 * Buscar por Instancia
			 **/
			if (nmAgrupamento.equals("AGRUPADO_INSTANCIA")) {
				rsm = ProcessoServices.getInstanciaAgrupada(nmAgrupamento, processos, connect);
				while(rsm.next()){
					rsm.setValueToField("NM_AGRUPAMENTO", rsm.getInt("TP_INSTANCIA") == 0 ? "Não definido" : tipoInstancia[rsm.getInt("TP_INSTANCIA")-1]);
				}
			}
			
			/**
			 * Buscar por Fase e Situação
			 **/	
			if (nmAgrupamento.equals("AGRUPADO_FASE_SITUACAO")) {		
				rsm = ProcessoServices.getSituacaoAgrupado(nmAgrupamento, processos, connect);
			}
			
			/**
			 * Buscar por nome da Cidade e Juízo
			 **/	
			if(nmAgrupamento.equals("AGRUPADO_CIDADE_JUIZO")) {
				rsm = JuizoServices.getCidadeJuizoAgrupado(nmAgrupamento, processos, connect);
			}
			
			/**
			 * Buscar por Tribunal
			 **/	
			if(nmAgrupamento.equals("AGRUPADO_TRIBUNAL")) {
				rsm = TribunalServices.getTribunalAgrupado(nmAgrupamento, processos, connect);
			}
			
			/**
			 * Buscar por Fase e Area Direito e Tipo de Ação
			 **/	
			if(nmAgrupamento.equals("AGRUPADO_AREA_DIREITO_TIPO_ACAO")) {
				rsm = AreaDireitoServices.getAreaDireitoTipoAcaoAgrupado(processos, connect);
			}
			
			/**
			 * Buscar por Fase e Area Direito
			 **/	
			if(nmAgrupamento.equals("AGRUPADO_AREA_DIREITO")) {
				rsm = AreaDireitoServices.getAreaDireitoAgrupado(processos, connect);
			}
			
			/**
			 * Buscar por Cidade
			 **/	
			if(nmAgrupamento.equals("AGRUPADO_CIDADE")) {
				rsm = ProcessoServices.getCidadeAgrupada(nmAgrupamento, processos, connect);
			}
						
			/**
			 * Buscar por Mês de Distribuição
			 **/	
			if(nmAgrupamento.equals("AGRUPADO_MES_DISTRIBUICAO") || nmAgrupamento.equals("AGRUPADO_MES_REPASSE")) { 				
				rsm = ProcessoServices.getMesDistribuicaoAgrupado(nmAgrupamento, processos, connect);
			}

			ArrayList<String> orderFields = new ArrayList<String>();
			orderFields.add(orderField + " DESC");
			rsm.orderBy(orderFields);
			
			return rsm;
		} catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.findProcessoAdvogado: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}	
	
	public static ResultSetMap getAdvogadoAgrupado(String nmAgrupamento, ArrayList<Integer> processos, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					"SELECT B.NM_PESSOA AS NM_AGRUPAMENTO, COUNT(A.CD_PROCESSO) AS QT_PROCESSOS, (COUNT(A.CD_PROCESSO) / cast ((SELECT COUNT(*) FROM PRC_PROCESSO) as numeric(15,5)) * 100) AS QT_PERCENTUAL " +
					"FROM PRC_PROCESSO A " +
					(nmAgrupamento.equals("AGRUPADO_ADVOGADO_RESPONSAVEL") ? "JOIN GRL_PESSOA B ON (A.CD_ADVOGADO = B.CD_PESSOA) " : "JOIN GRL_PESSOA B ON (A.CD_ADVOGADO_CONTRARIO = B.CD_PESSOA) ") +
					(processos.size()>0 ? " WHERE A.cd_processo in ("+Util.join(processos)+")" : "") +
					"GROUP BY B.NM_PESSOA " +
					"ORDER BY B.NM_PESSOA "
			);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getAdvogadoAgrupado: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getInstanciaAgrupada(String nmAgrupamento, ArrayList<Integer> processos, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					"SELECT A.TP_INSTANCIA, COUNT(A.CD_PROCESSO) AS QT_PROCESSOS, (COUNT(A.CD_PROCESSO) / cast ((SELECT COUNT(*) FROM PRC_PROCESSO) as numeric(15,5)) * 100) AS QT_PERCENTUAL " +
							"FROM PRC_PROCESSO A " + 
							"LEFT JOIN PRC_TIPO_PROCESSO B ON (A.CD_TIPO_PROCESSO = B.CD_TIPO_PROCESSO) " +
							(processos.size()>0 ? " WHERE A.cd_processo in ("+Util.join(processos)+")" : "") +
							"GROUP BY A.TP_INSTANCIA "
					);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getInstanciaAgrupada: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getCidadeAgrupada(String nmAgrupamento, ArrayList<Integer> processos, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					"SELECT B.NM_CIDADE as NM_AGRUPAMENTO, COUNT(A.CD_PROCESSO) AS QT_PROCESSOS, (COUNT(A.CD_PROCESSO) / cast ((SELECT COUNT(*) FROM PRC_PROCESSO) as numeric(15,5)) * 100) AS QT_PERCENTUAL " +
					"FROM PRC_PROCESSO      A " + 
					"LEFT JOIN GRL_CIDADE B ON (A.CD_CIDADE = B.CD_CIDADE) " +
					(processos.size()>0 ? " WHERE A.cd_processo in ("+Util.join(processos)+")" : "") +
					"GROUP BY B.NM_CIDADE "
					);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getCidadeAgrupada: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getSituacaoAgrupado(String nmAgrupamento, ArrayList<Integer> processos, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					"SELECT B.CD_TIPO_SITUACAO, B.NM_TIPO_SITUACAO AS NM_AGRUPAMENTO, COUNT(A.CD_PROCESSO) AS QT_PROCESSOS, (COUNT(A.CD_PROCESSO) / cast ((SELECT COUNT(*) FROM PRC_PROCESSO) as numeric(15,5)) * 100) AS QT_PERCENTUAL " +
					"FROM PRC_PROCESSO A " + 
					"LEFT JOIN PRC_TIPO_SITUACAO B ON (A.CD_TIPO_SITUACAO = B.CD_TIPO_SITUACAO) " +
					(processos.size()>0 ? " WHERE A.cd_processo in ("+Util.join(processos)+")" : "") +
					"GROUP BY B.NM_TIPO_SITUACAO, B.CD_TIPO_SITUACAO "
					);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getCidadeAgrupada: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getMesDistribuicaoAgrupado(String nmAgrupamento, ArrayList<Integer> processos, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			String dtTipo = nmAgrupamento.equals("AGRUPADO_MES_DISTRIBUICAO") ? "DT_DISTRIBUICAO" : "DT_REPASSE";
			pstmt = connect.prepareStatement(
					"SELECT A." + dtTipo + ", COUNT(A.CD_PROCESSO) AS QT_PROCESSOS, (COUNT(A.CD_PROCESSO) / cast ((SELECT COUNT(*) FROM PRC_PROCESSO) as numeric(15,5)) * 100) AS QT_PERCENTUAL " +
					"FROM PRC_PROCESSO      A " + 
					(processos.size()>0 ? " WHERE A.cd_processo in ("+Util.join(processos)+")" : "") +		
					"GROUP BY A." + dtTipo + " "
					);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while (rsm.next()){
				rsm.setValueToField("NM_AGRUPAMENTO", rsm.getDateFormat(dtTipo, "dd/MM/yyyy"));
			}
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getCidadeAgrupada: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Buscando dados para o relatório de Correspondentes e Comarcas.
	 * @author Edgard Hufelande
	 * @param tpReport   Define o tipo de relatório que será impresso, se é agrupado por comarca ou correspondente.
	 * @param connect      Envia um parametro de conexão já existente caso haja uma.
	 * 
	 */
	
	public static Result findProcessoComarcaCorrespondente(int tpReport){
		return findProcessoComarcaCorrespondente(tpReport, null);
	}
	
	public static Result findProcessoComarcaCorrespondente(int tpReport, Connection connect){
		
		boolean isConnectionNull = connect==null;
		ResultSetMap rsm = new ResultSetMap();		
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> Dataset = new HashMap<String, Object>();
		PreparedStatement pstmt;
		
		try {
			
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
						
			pstmt = connect.prepareStatement(
					"SELECT A.CD_ORGAO, A.NM_ORGAO, C.NM_CIDADE, C.CD_CIDADE, D.NM_LOGRADOURO, D.NM_BAIRRO, D.NR_TELEFONE, E.NR_TELEFONE1, E.NR_TELEFONE2, E.NR_CELULAR, F.SG_ESTADO " +
					"  FROM PRC_ORGAO A, PRC_CIDADE_ORGAO B, GRL_CIDADE C, GRL_PESSOA_ENDERECO D, GRL_PESSOA E, GRL_ESTADO F " +
					" WHERE A.CD_ORGAO   = B.CD_ORGAO " +
					"   AND B.CD_CIDADE  = C.CD_CIDADE " +
					"   AND A.CD_PESSOA  = D.CD_PESSOA " +
					"   AND A.CD_PESSOA  = E.CD_PESSOA " +
					"   AND C.CD_ESTADO  = F.CD_ESTADO " +
					" ORDER BY C.NM_CIDADE, E.NM_EMAIL, D.NR_TELEFONE, E.NR_TELEFONE1, E.NR_TELEFONE2, E.NR_CELULAR ASC"				
			);
				
			ResultSetMap rsmStmt = new ResultSetMap(pstmt.executeQuery());
			
			rsmStmt.beforeFirst();			
						
			while(rsmStmt.next()){				
				rsmStmt.setValueToField("NM_ENDERECO",((rsmStmt.getString("NM_LOGRADOURO") != null && !rsmStmt.getString("NM_LOGRADOURO").equals("")) ? rsmStmt.getString("NM_LOGRADOURO") : "") +
												   (rsmStmt.getString("NM_BAIRRO") != null && !rsmStmt.getString("NM_BAIRRO").equals("") ? ((rsmStmt.getString("NM_ENDERECO") == null || rsmStmt.getString("NM_ENDERECO").equals("")) ? ", " : "") + rsmStmt.getString("NM_BAIRRO") : ""));				
				if(rsmStmt.getString("NM_ENDERECO") == null || rsmStmt.getString("NM_ENDERECO").equals(""))
					rsmStmt.setValueToField("NM_ENDERECO", "Não informado.");
				if(rsmStmt.getString("NM_EMAIL") == null || rsmStmt.getString("NM_EMAIL").equals(""))
					rsmStmt.setValueToField("NM_EMAIL", "Não informado.");
			}			
			
			Dataset.put("rsmDataset", rsmStmt.getLines());			
			rsm.addRegister(Dataset);
			
			param.put("NM_RELATORIO", tpReport == 0 ? "Relatório por Comarcas" : "Relatório por Correspondentes");
			param.put("TP_RELATORIO", tpReport);	
			
						
			Result result = new Result(1, "Sucesso!");
			result.addObject("RSM", rsm);
			result.addObject("PARAMNS", param);
			
			return result;
			
		} catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.findProcessoComarcaCorrespondente: " +  e);
			return new Result(-1, "Erro: " + e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Buscando informações da situação e tipo de ação de cada cliente, onde será transformado em gráficos para um relatório.
	 * @author Edgard Hufelande
	 * @since  2015-02-26
	 * @param cdProcessos  Array contendo uma lista de processos.
	 * @return ResultSetMap
	 */
	public static ResultSetMap getGraficoCliente (ArrayList<Integer> cdProcessos) {
		return getGraficoCliente(cdProcessos, null);
	}
	public static ResultSetMap getGraficoCliente(ArrayList<Integer> cdProcessos, Connection connect) {
		
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {	
						
			String listProcessos = new String();
			
			for(int i : cdProcessos){
				/* Transformando a array em string */
				listProcessos += i + ", ";
			}
			
			
			/* Buscando todos os clientes através dos processos que o método recebe. */			
			PreparedStatement pstmtClientes = connect.prepareStatement(
					" SELECT DISTINCT C.CD_PESSOA, C.NM_PESSOA, " +
					" (SELECT COUNT(*) FROM PRC_PROCESSO) AS TOTAL_PROCESSOS " +
					" FROM PRC_PROCESSO A " +
					" JOIN PRC_PARTE_CLIENTE B ON (A.CD_PROCESSO = B.CD_PROCESSO) " +
					" JOIN GRL_PESSOA C ON (B.CD_PESSOA = C.CD_PESSOA) " +
					" WHERE A.CD_PROCESSO IN ( " + listProcessos.substring(0, listProcessos.length()-2) + " ) "
					);
			
			ResultSetMap rsmCliente = new ResultSetMap(pstmtClientes.executeQuery());
			
			ResultSetMap rsm = new ResultSetMap();
			
			/* Separando resultados da primeira consulta pstmtSituacoes
			 * para que o jFreChart possa processar e gerar o gráfico.
			 */
			
			rsm.beforeFirst();
			while(rsmCliente.next()){
				HashMap<String, Object> cliente = new HashMap<String, Object>();
				
				cliente.put("CD_PESSOA", rsmCliente.getInt("CD_PESSOA"));				
				cliente.put("NM_PESSOA", rsmCliente.getString("NM_PESSOA"));				
				
				/* Buscando todos os processos do cliente agrupado por situações */
				PreparedStatement pstmtSituacoes = connect.prepareStatement(
						"SELECT COUNT(A.CD_PROCESSO) AS QT_PROCESSOS, A.ST_PROCESSO, B.CD_PESSOA, C.NM_PESSOA " +
						"FROM PRC_PROCESSO A " +
						"JOIN PRC_PARTE_CLIENTE B ON (A.CD_PROCESSO = B.CD_PROCESSO) " +
						"JOIN GRL_PESSOA C ON (B.CD_PESSOA = C.CD_PESSOA) " +
						"WHERE B.CD_PESSOA = " + rsmCliente.getInt("CD_PESSOA") +
						"GROUP BY B.CD_PESSOA, A.ST_PROCESSO, C.NM_PESSOA " +
						"ORDER BY C.NM_PESSOA"
						);
				
				/* Buscando todos os processos do cliente agrupado por Tipo de Ação */
				PreparedStatement pstmtTipoAcao = connect.prepareStatement(
						"SELECT COUNT(A.CD_PROCESSO) AS QT_PROCESSOS, A.CD_TIPO_PROCESSO, B.CD_PESSOA, C.NM_PESSOA, D.NM_TIPO_PROCESSO " +
						"FROM PRC_PROCESSO A " +
						"LEFT JOIN PRC_PARTE_CLIENTE B ON (A.CD_PROCESSO = B.CD_PROCESSO) " +
						"JOIN GRL_PESSOA C ON (B.CD_PESSOA = C.CD_PESSOA) "+
						"JOIN PRC_TIPO_PROCESSO D ON (A.CD_TIPO_PROCESSO = D.CD_TIPO_PROCESSO) " +
						"WHERE B.CD_PESSOA = " + rsmCliente.getInt("CD_PESSOA") +
						"GROUP BY B.CD_PESSOA, A.CD_TIPO_PROCESSO, C.NM_PESSOA, D.NM_TIPO_PROCESSO " +
						"ORDER BY B.CD_PESSOA "
						);
				
				
				ResultSetMap rsmPessoasSituacoes = new ResultSetMap(pstmtSituacoes.executeQuery());
				ResultSetMap rsmPessoaTipoAcao = new ResultSetMap(pstmtTipoAcao.executeQuery());
				
				rsmPessoasSituacoes.beforeFirst();				
				while (rsmPessoasSituacoes.next()){
					rsmPessoasSituacoes.setValueToField("TOTAL_PROCESSOS", rsmCliente.getInt("TOTAL_PROCESSOS"));
				}
				
				rsmPessoaTipoAcao.beforeFirst();
				while(rsmPessoaTipoAcao.next()){
					rsmPessoaTipoAcao.setValueToField("TOTAL_PROCESSOS", rsmCliente.getInt("TOTAL_PROCESSOS"));
				}
				
				cliente.put("rsmSituacoes", rsmPessoasSituacoes.getLines());				
				cliente.put("rsmTipoAcao", rsmPessoaTipoAcao.getLines());			

				rsm.addRegister(cliente);
			}
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getGraficoCliente: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getProcessosMovimentados(int cdUsuario){
		return getProcessosMovimentados(cdUsuario, -1);
	}
	
	public static ResultSetMap getProcessosMovimentados(int cdUsuario, int nrRegistros){
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("movimentado_nos_ultimos", "3", ItemComparator.EQUAL, Types.TIMESTAMP));
		
		int cdAdvogado = UsuarioDAO.get(cdUsuario).getCdPessoa();
		criterios.add(new ItemComparator("A.CD_ADVOGADO", Integer.toString(cdAdvogado), ItemComparator.EQUAL, Types.INTEGER));
		if(nrRegistros>=0) 
			criterios.add(new ItemComparator("nrRegistros", Integer.toString(nrRegistros), ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsm = find(criterios);
		
		while(rsm.next()) {
			ResultSetMap rsmAux = getAndamentos(rsm.getInt("cd_processo"), true);
			if(rsmAux.next()) {
							
				rsm.setValueToField("NM_TIPO_ANDAMENTO", rsmAux.getString("NM_TIPO_ANDAMENTO"));
				rsm.setValueToField("DT_ANDAMENTO", Util.convTimestampToCalendar(rsmAux.getTimestamp("DT_ANDAMENTO")));
				rsm.setValueToField("TXT_ANDAMENTO", rsmAux.getString("TXT_ANDAMENTO"));
			}
		}
		
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("DT_ANDAMENTO DESC");
		rsm.orderBy(columns);
		rsm.beforeFirst();
		
		return rsm;
	}
	
	public static Result gerarListaExcel(ArrayList<ItemComparator> criterios, Empresa empresa, boolean exportarAndamentos){
		return gerarListaExcel(criterios, empresa, exportarAndamentos, null);
	}
	
	public static Result gerarListaExcel(ArrayList<ItemComparator> criterios, Empresa empresa, boolean exportarAndamentos, Connection connect){
		
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			
			String dsFiltroPeriodo = null;
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("filtarPeloPeriodo")) {
					dsFiltroPeriodo = criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
			}
			
			ResultSetMap rsm = find(criterios, null, connect);
			
			if(rsm.size()<=0) {
				return new Result(-2, "Nenhum processo encontrado.");
			}
			
			LogUtils.debug("ProcessoServices.gerarListaExcel: gerando lista...");
			LogUtils.createTimer("PROCESSO_EXCEL_TIMER");
			
			Result result = ExcelServices.gerarListaProcessoXls(rsm, empresa, exportarAndamentos, dsFiltroPeriodo);
			LogUtils.logTimer("PROCESSO_EXCEL_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
			
			LogUtils.destroyTimer("PROCESSO_EXCEL_TIMER");
			
			return result;
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.gerarListaExcel: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result gerarAuditoriaExcel(ArrayList<ItemComparator> criterios, Empresa empresa, boolean exportarAndamentos){
		return gerarAuditoriaExcel(criterios, empresa, exportarAndamentos, null);
	}
	
	public static Result gerarAuditoriaExcel(ArrayList<ItemComparator> criterios, Empresa empresa, boolean exportarAndamentos, Connection connect){
		
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			
			String dsFiltroPeriodo = null;
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("filtarPeloPeriodo")) {
					dsFiltroPeriodo = criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
			}
			
			ResultSetMap rsm = find(criterios, null, connect);
			
			if(rsm.size()<=0) {
				return new Result(-2, "Nenhum processo encontrado.");
			}
			
			LogUtils.debug("ProcessoServices.gerarAuditoriaExcel: gerando lista...");
			LogUtils.createTimer("PROCESSO_EXCEL_TIMER");
			
			Result result = ExcelServices.gerarListaProcessoXls(rsm, empresa, exportarAndamentos, dsFiltroPeriodo);
			LogUtils.logTimer("PROCESSO_EXCEL_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
			
			LogUtils.destroyTimer("PROCESSO_EXCEL_TIMER");
			
			return result;
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.gerarListaExcel: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getEstatisticaAgenda(ArrayList<ItemComparator> criterios, ArrayList<Integer> processos){
		return getEstatisticaAgenda(criterios, processos, null);
	}
	
	public static ResultSetMap getEstatisticaAgenda(ArrayList<ItemComparator> criterios, ArrayList<Integer> processos, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			int tpAgendaItem = -1;
			int cdTipoPrazo = 0;
			int stAgendaItem = -1;
			GregorianCalendar dtInicial = null;
			GregorianCalendar dtFinal = null;
			for(int i=0; i<criterios.size(); i++)	{
				if(criterios.get(i).getColumn().equalsIgnoreCase("tpAgendaItem"))	{
					tpAgendaItem = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("cdTipoPrazo"))	{
					cdTipoPrazo = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("stAgendaItem"))	{
					stAgendaItem = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("dtInicialAgenda"))	{
					dtInicial = Util.convStringToCalendar(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("dtFinalAgenda"))	{
					dtFinal = Util.convStringToCalendar(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
			}
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT COUNT(AA.cd_agenda_item) AS qtd_agenda_item, "
					+ " AB.nm_tipo_prazo,"
					+ " AB.tp_agenda_item "
					+ " FROM agd_agenda_item AA"
					+ " JOIN prc_tipo_prazo AB ON (AA.cd_tipo_prazo = AB.cd_tipo_prazo)"
					+ " WHERE AA.cd_processo IN ("+Util.join(processos)+")"
					+ (tpAgendaItem>=0 ? " AB.tp_agenda_item="+tpAgendaItem : "")
					+ (cdTipoPrazo>0 ? " AA.cd_tipo_prazo="+cdTipoPrazo : "")
					+ (stAgendaItem>=0 ? " AB.st_agenda_item="+stAgendaItem : "")
					+ (dtInicial!=null ? "AA.dt_final>='"+Util.convCalendarToTimestamp(dtInicial)+"'" : "")
					+ (dtFinal!=null ? "AA.dt_final<='"+Util.convCalendarToTimestamp(dtFinal)+"'" : "")
					+ " GROUP BY AB.tp_agenda_item, AB.nm_tipo_prazo");
			
			LogUtils.debug("ProcessoServices.getEstatisticaAgenda");
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.getEstatisticaAgenda: " + e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static String getSqlFind(ArrayList<ItemComparator> criterios) {
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		String sql = "";

		LogUtils.debug("ProcessoServices.find");
		LogUtils.createTimer("PROCESSO_FIND_TIMER");
		
		//boolean lgMovimentacao = false;
		boolean lgDtAndamento = false;
		boolean lgDtLancamentoAndamento = false;
		boolean findAndamentos = false;
		boolean lgConfidenciais = false;
		boolean lgPrivados = false;
		int nrRegistros = 0;
		for(int i=0; i<criterios.size(); i++)	{
			if(criterios.get(i).getColumn().equalsIgnoreCase("cd_cliente"))	{
				sql += " AND EXISTS (SELECT * FROM prc_parte_cliente PC " +
					   "             WHERE PC.cd_processo = A.cd_processo " +
					   "               AND PC.cd_pessoa = "+criterios.get(i).getValue()+")";
			}
			else if(criterios.get(i).getColumn().equalsIgnoreCase("cd_adverso"))	{
				sql += " AND EXISTS (SELECT * FROM prc_outra_parte OP " +
					   "             WHERE OP.cd_processo = A.cd_processo " +
					   "               AND OP.cd_pessoa = "+criterios.get(i).getValue()+")";
			}
			else if(criterios.get(i).getColumn().equalsIgnoreCase("nm_cliente"))	{
				String nmCliente = 	Util.limparTexto(criterios.get(i).getValue());
				nmCliente = "%"+nmCliente.replaceAll(" ", "%")+"%";
				
				sql += " AND EXISTS (SELECT * FROM prc_parte_cliente PC, grl_pessoa P " +
					   "             WHERE PC.cd_processo = A.cd_processo " +
					   "               AND PC.cd_pessoa   = P.cd_pessoa " +
					   (Util.getConfManager().getIdOfDbUsed().equals("FB") ? 
							   (!nmCliente.equals("") ? " AND (P.nm_pessoa COLLATE PT_BR LIKE '"+nmCliente+"'"
							   							+ " OR P.nm_apelido COLLATE PT_BR LIKE '"+nmCliente+"'"
							   							+ " OR P.id_pessoa COLLATE PT_BR LIKE '"+nmCliente+"'))" : ")") :
							   (!nmCliente.equals("") ? 
						"               AND (nm_pessoa LIKE \'%"+criterios.get(i).getValue()+"%\' " +
						"				OR nm_apelido LIKE \'%"+criterios.get(i).getValue()+"%\' "+
						"				OR id_pessoa LIKE \'%"+criterios.get(i).getValue()+"%\')) "
	   							 : ")"));	   
			}
			else if(criterios.get(i).getColumn().equalsIgnoreCase("nm_parte_contraria"))	{
				String nmAdverso =	Util.limparTexto(criterios.get(i).getValue());
				nmAdverso = "%"+nmAdverso.replaceAll(" ", "%")+"%";
									
				sql += " AND EXISTS (SELECT * FROM prc_outra_parte PC, grl_pessoa P " +
				   	   "             WHERE PC.cd_processo = A.cd_processo " +
				       "               AND PC.cd_pessoa   = P.cd_pessoa " +
				       (Util.getConfManager().getIdOfDbUsed().equals("FB") ? 
							   (!nmAdverso.equals("") ? " AND (P.nm_pessoa COLLATE PT_BR LIKE '"+nmAdverso+"'"
							   							+ " OR P.nm_apelido COLLATE PT_BR LIKE '"+nmAdverso+"'"
							   							+ " OR P.id_pessoa COLLATE PT_BR LIKE '"+nmAdverso+"'))" : ")") :
							   (!nmAdverso.equals("") ? 
						"               AND (nm_pessoa LIKE \'%"+criterios.get(i).getValue()+"%\' "+
						"				OR nm_apelido LIKE \'%"+criterios.get(i).getValue()+"%\' "+
						"				OR id_pessoa LIKE \'%"+criterios.get(i).getValue()+"%\')) "
	   							: ")")); 
				       
			}
			else if(criterios.get(i).getColumn().equalsIgnoreCase("sem_andamento_a"))	{
				GregorianCalendar data = new GregorianCalendar();
				data.add(GregorianCalendar.DATE, -1*Integer.parseInt(criterios.get(i).getValue()));
				sql += " AND EXISTS (SELECT cd_processo FROM prc_processo_andamento PA " +
					   	   "             WHERE PA.cd_processo = A.cd_processo " +
					       "               AND PA.dt_andamento < \'"+com.tivic.manager.util.Util.formatDate(data, "MM/dd/yyyy")+"\') ";
			}
			else if(criterios.get(i).getColumn().equalsIgnoreCase("movimentado_nos_ultimos"))	{
				GregorianCalendar data = new GregorianCalendar();
				data.add(GregorianCalendar.DATE, -1*Integer.parseInt(criterios.get(i).getValue()));
				sql += " AND EXISTS (SELECT cd_processo FROM prc_processo_andamento PA " +
					   	   "             WHERE PA.cd_processo = A.cd_processo " +
					       "               AND PA.dt_andamento >= \'"+com.tivic.manager.util.Util.formatDate(data, "MM/dd/yyyy")+"\')";
			}
			else if(criterios.get(i).getColumn().equalsIgnoreCase("lg_movimentacao"))	{
				;//lgMovimentacao = true;
			}
			else if(criterios.get(i).getColumn().equalsIgnoreCase("lg_cliente_autor"))	{
				int lgClienteAutor = Integer.parseInt(criterios.get(i).getValue());
				if(lgClienteAutor>=0)
					sql += " AND A.lg_cliente_autor = "+criterios.get(i).getValue()+" ";
			}
			else if(criterios.get(i).getColumn().equalsIgnoreCase("nr_processo"))	{
				/*
				 * PESQUISA o NR DO PROCESSO EM 4 CAMPOS (LEGADO)
				 */
				String nrProcesso = criterios.get(i).getValue().replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", "");
				sql += " AND (REPLACE(REPLACE(REPLACE(A.nr_processo, '.', ''), '-', ''), '/', '') LIKE \'%"+ nrProcesso +"%\' " +
						" OR REPLACE(REPLACE(REPLACE(A.nr_antigo, '.', ''), '-', ''), '/', '') LIKE \'%"+ nrProcesso +"%\' " +
						" OR REPLACE(REPLACE(REPLACE(A.id_processo, '.', ''), '-', ''), '/', '') LIKE \'%"+ nrProcesso +"%\' " +
						" OR UPPER(A.nm_conteiner3) LIKE \'%"+ nrProcesso.toUpperCase() +"%\') ";
			}
			else if(criterios.get(i).getColumn().equalsIgnoreCase("nr_interno"))	{
				/*
				 * PESQUISA o NR INTERNO DO PROCESSO
				 */
				String nrInterno = criterios.get(i).getValue().replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", "");
				sql += " AND REPLACE(REPLACE(REPLACE(A.nr_interno, '.', ''), '-', ''), '/', '') LIKE \'%"+ nrInterno +"%\' ";
			}	
			else if(criterios.get(i).getColumn().equalsIgnoreCase("dt_andamento_inicial") || 
					criterios.get(i).getColumn().equalsIgnoreCase("dt_andamento_final"))	{
				
				if(!lgDtAndamento) {
					
					String dtAndamentoInicial = "";
					String dtAndamentoFinal = "";
					int cdTipoAndamento = 0;
					
					for (ItemComparator item : criterios) {
						if(item.getColumn().equalsIgnoreCase("dt_andamento_inicial"))
							dtAndamentoInicial = com.tivic.manager.util.Util.formatDate(Util.stringToCalendar(item.getValue()), "dd/MM/yyyy")+" 00:00:00";
						
						if(item.getColumn().equalsIgnoreCase("dt_andamento_final"))
							dtAndamentoFinal = com.tivic.manager.util.Util.formatDate(Util.stringToCalendar(item.getValue()), "dd/MM/yyyy")+" 23:59:59";
						
						if(item.getColumn().equalsIgnoreCase("cd_tipo_andamento")) {
							cdTipoAndamento = Integer.parseInt(item.getValue());
						}
					}
					
					sql += " AND EXISTS (SELECT cd_processo FROM prc_processo_andamento PA " +
						   	   "             WHERE PA.cd_processo = A.cd_processo " +
						       "               AND PA.dt_andamento >= \'"+dtAndamentoInicial+"\'" +
						       		"		   AND PA.dt_andamento <= \'"+dtAndamentoFinal+"\' " +
						       		(cdTipoAndamento!=0 ? " AND PA.cd_tipo_andamento = "+cdTipoAndamento : "") +
						       		")";
				}
				
				lgDtAndamento = true;
			}
			
			else if(criterios.get(i).getColumn().equalsIgnoreCase("dt_lancamento_andamento_inicial") || 
					criterios.get(i).getColumn().equalsIgnoreCase("dt_lancamento_andamento_final"))	{
				
				if(!lgDtLancamentoAndamento) {
					
					String dtLancamentoAndamentoInicial = "";
					String dtLancamentoAndamentoFinal = "";
					int cdTipoAndamento = 0;
					
					for (ItemComparator item : criterios) {
						if(item.getColumn().equalsIgnoreCase("dt_lancamento_andamento_inicial"))
							dtLancamentoAndamentoInicial = com.tivic.manager.util.Util.formatDate(Util.stringToCalendar(item.getValue()), "MM/dd/yyyy")+" 00:00:00";
						
						if(item.getColumn().equalsIgnoreCase("dt_lancamento_andamento_final"))
							dtLancamentoAndamentoFinal = com.tivic.manager.util.Util.formatDate(Util.stringToCalendar(item.getValue()), "MM/dd/yyyy")+" 23:59:59";
						
						if(item.getColumn().equalsIgnoreCase("cd_tipo_andamento")) {
							cdTipoAndamento = Integer.parseInt(item.getValue());
						}
					}
					
					sql += " AND EXISTS (SELECT cd_processo FROM prc_processo_andamento PA " +
						   	   "             WHERE PA.cd_processo = A.cd_processo " +
						       "               AND PA.dt_lancamento >= \'"+dtLancamentoAndamentoInicial+"\'" +
						       		"		   AND PA.dt_lancamento <= \'"+dtLancamentoAndamentoFinal+"\'" +
						       		(cdTipoAndamento!=0 ? " AND PA.cd_tipo_andamento = "+cdTipoAndamento : "") +
						       		")";
				}

				lgDtLancamentoAndamento = true;
			}
			else if(criterios.get(i).getColumn().equalsIgnoreCase("cd_tipo_andamento"))	{
				sql += " AND EXISTS (SELECT cd_processo FROM prc_processo_andamento PA " +
					   	   "             WHERE PA.cd_processo = A.cd_processo " +
					       "               AND PA.cd_tipo_andamento ="+criterios.get(i).getValue()+")";
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("nrRegistros")) {
				nrRegistros = Integer.valueOf(criterios.get(i).getValue().toString().trim());
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("findAndamentos")) {
				findAndamentos = Boolean.valueOf(criterios.get(i).getValue().toString().trim());
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("lgConfidenciais")) {
				lgConfidenciais = Boolean.valueOf(criterios.get(i).getValue().toString().trim());
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("lgPrivados")) {
				lgPrivados = Boolean.valueOf(criterios.get(i).getValue().toString().trim());
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("cdUsuario")) {
				/**
				 * REALIZA A PESQUISA DE PROCESSO BASEADO NOS GRUPOS DE PROCESSO QUE O USUÁRIO PODE ACESSAR
				 * Caso o critério cdUsuario seja passado para o método de busca é então
				 * realizado um teste para verificar o acesso aos grupos.
				 */
				
				int cdUsuario = Integer.valueOf(criterios.get(i).getValue().toString().trim());
				criterios.remove(i);
				i--;
				
				if(cdUsuario>0 &&  UsuarioGrupoServices.hasGrupoProcesso(cdUsuario)) {
					
					ResultSetMap rsm1 = UsuarioGrupoServices.getAllByUsuario(cdUsuario);
					String cdsGrupos = "";
					
					for (int j = 0; rsm1.next(); j++) {
						cdsGrupos += (((j>0) ? "," : "")+rsm1.getInt("CD_GRUPO_PROCESSO"));
					}
					
					criterios.add(new ItemComparator("A.cd_grupo_processo", cdsGrupos, ItemComparator.IN, Types.NULL));
					
				}
			}
			
			else if (criterios.get(i).getColumn().equalsIgnoreCase("nm_cidade")) {
				String nmCidade = Util.limparTexto(criterios.get(i).getValue());
				nmCidade = "%"+nmCidade.replaceAll(" ", "%")+"%";
				
				sql += (Util.getConfManager().getIdOfDbUsed().equals("FB") ? 
						   (!nmCidade.equals("") ? " AND N.nm_cidade COLLATE PT_BR LIKE '"+nmCidade+"'" : "") :
							   (!nmCidade.equals("") ? " AND TRANSLATE(N.nm_cidade, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '"+nmCidade+"' " : ""));
				
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("nm_advogado_contrario")) {
				String nmAdvogadoContrario = Util.limparTexto(criterios.get(i).getValue());
				nmAdvogadoContrario = "%"+nmAdvogadoContrario.replaceAll(" ", "%")+"%";
				
				sql += (Util.getConfManager().getIdOfDbUsed().equals("FB") ? 
						   (!nmAdvogadoContrario.equals("") ? " AND I.nm_pessoa COLLATE PT_BR LIKE '"+nmAdvogadoContrario+"'" : "") :
							   (!nmAdvogadoContrario.equals("") ? " AND TRANSLATE(I.nm_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '"+nmAdvogadoContrario+"' " : ""));
				
				criterios.remove(i);
				i--;
			}
			else
				crt.add(criterios.get(i));
		}
		
		String groups = "";
		String fields = " A.cd_processo ";
		
		// Processa agrupamentos enviados em groupBy
//		String [] retorno = com.tivic.manager.util.Util.getFieldsAndGroupBy(groupBy, fields, groups,
//				                                                     "SUM(A.VL_CONTRATO) AS VL_CONTRATO, COUNT(*) AS QT_CONTRATO");
//		fields = retorno[0];
//		groups = retorno[1];
		
		String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(nrRegistros, 0);
		
		System.out.println("sqlLimit[0]: "+sqlLimit[0]);
		System.out.println("sqlLimit[1]: "+sqlLimit[1]);
		
		String sqlFinal = "SELECT "+ sqlLimit[0] +
				fields+
                "FROM prc_processo A "+
                "LEFT OUTER JOIN prc_juizo          	B ON (A.cd_juizo = B.cd_juizo) "+
                "LEFT OUTER JOIN prc_tribunal       	C ON (A.cd_tribunal = C.cd_tribunal) "+
                "LEFT OUTER JOIN prc_tipo_processo  	D ON (A.cd_tipo_processo = D.cd_tipo_processo) "+
                "LEFT OUTER JOIN prc_area_direito   	E ON (D.cd_area_direito = E.cd_area_direito) "+
                "LEFT OUTER JOIN prc_orgao          	F ON (A.cd_orgao = F.cd_orgao) "+
                "LEFT OUTER JOIN prc_tipo_situacao  	G ON (A.cd_tipo_situacao = G.cd_tipo_situacao) "+
                "LEFT OUTER JOIN grl_pessoa         	H ON (A.cd_advogado = H.cd_pessoa) "+
                "LEFT OUTER JOIN grl_pessoa         	I ON (A.cd_advogado_contrario = I.cd_pessoa) "+
                "LEFT OUTER JOIN grl_pessoa         	J ON (A.cd_advogado_titular = J.cd_pessoa) "+
                "LEFT OUTER JOIN prc_grupo_processo 	L ON (A.cd_grupo_processo = L.cd_grupo_processo) "+
                "LEFT OUTER JOIN grl_pessoa         	M ON (A.cd_oficial_justica = M.cd_pessoa) "+
                "LEFT OUTER JOIN grl_cidade         	N ON (A.cd_cidade = N.cd_cidade) "+
				"LEFT OUTER JOIN grl_estado 			N1 ON (N.cd_estado = N1.cd_estado) " +
                "LEFT OUTER JOIN prc_tipo_pedido    	O ON (A.cd_tipo_pedido = O.cd_tipo_pedido) "+
                "LEFT OUTER JOIN prc_tipo_objeto   	 	P ON (A.cd_tipo_objeto = P.cd_tipo_objeto) "+
                "LEFT OUTER JOIN grl_pessoa        	 	Q ON (A.cd_responsavel_arquivo = Q.cd_pessoa) "+
                "LEFT OUTER JOIN seg_usuario 			R ON (R.cd_usuario = A.cd_usuario_cadastro) " +
                "LEFT OUTER JOIN grl_pessoa 			R1 ON (R1.cd_pessoa = R.cd_pessoa) " +
                "LEFT OUTER JOIN prc_contrato 			S ON (S.cd_processo = A.cd_processo) " +
                "LEFT OUTER JOIN agd_grupo				T ON (A.cd_grupo_trabalho = T.cd_grupo) "+ 
                //(lgConfidenciais || lgPrivados ? "LEFT OUTER JOIN prc_processo_andamento U ON (A.cd_processo = U.cd_processo) " : "") +
                "WHERE 1=1 "+sql;
		
		String orderBy = groups+sqlLimit[1];
		
		return sqlFinal+orderBy;
	}
	
	/**
	 * Cálculo de correção monetária sobre valor de processo
	 * 
	 * @param cdProcesso
	 * @return
	 * 
	 * @author Maurício
	 */
	@Deprecated
	public static Result calcular(GregorianCalendar dtPeriodoInicial, GregorianCalendar dtPeriodoFinal, boolean lgPeriodoFinalInclusivo,
			GregorianCalendar dtReferencia, int cdIndicadorBase, boolean lgDecimoTerceiro, boolean lgParcelaMensal, int cdIndicadorCorrecao,
			GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdIndicadorCorrecaoSecundario, Double vlTaxaJuros){
		
		return calcular(0, dtPeriodoInicial, dtPeriodoFinal, lgPeriodoFinalInclusivo, 
				dtReferencia, cdIndicadorBase, lgDecimoTerceiro, lgParcelaMensal, cdIndicadorCorrecao, 
				dtInicial, dtFinal, cdIndicadorCorrecaoSecundario, vlTaxaJuros, null);
	}
	
	@Deprecated
	public static Result calcular(int cdProcesso, GregorianCalendar dtPeriodoInicial, GregorianCalendar dtPeriodoFinal, boolean lgPeriodoFinalInclusivo,
			GregorianCalendar dtReferencia, int cdIndicadorBase, boolean lgDecimoTerceiro, boolean lgParcelaMensal, int cdIndicadorCorrecao,
			GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdIndicadorCorrecaoSecundario, Double vlTaxaJuros){
		
		return calcular(cdProcesso, dtPeriodoInicial, dtPeriodoFinal, lgPeriodoFinalInclusivo, 
				dtReferencia, cdIndicadorBase, lgDecimoTerceiro, lgParcelaMensal, cdIndicadorCorrecao, 
				dtInicial, dtFinal, cdIndicadorCorrecaoSecundario, vlTaxaJuros, null);
	}
	
	@Deprecated
	public static Result calcular(int cdProcesso, GregorianCalendar dtPeriodoInicial, GregorianCalendar dtPeriodoFinal, boolean lgPeriodoFinalInclusivo,
			GregorianCalendar dtReferencia, int cdIndicadorBase, boolean lgDecimoTerceiro, boolean lgParcelaMensal, int cdIndicadorCorrecao,
			GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdIndicadorCorrecaoSecundario, Double vlTaxaJuros, Connection connect){
		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsmCalculos = new ResultSetMap();
			
			int nrParcela = 0;
			int nrMesesDT = 0;
			Double vlTotalDT = 0.0;
			GregorianCalendar dtCM = null;
			Double prAliquotaCM = 0.0;
			Double prMes = 0.0;
			
			GregorianCalendar dtParcela = (GregorianCalendar)dtPeriodoInicial.clone();
			
			while((lgPeriodoFinalInclusivo && Util.compareDates(dtParcela, dtPeriodoFinal)<=0) || Util.compareDates(dtParcela, dtPeriodoFinal)<0) {
				nrParcela++;
				
				LogUtils.debug("ProcessoServices.calcular.nrParcela: "+nrParcela);
				LogUtils.debug("ProcessoServices.calcular.dtPeriodoInicial: "+Util.formatDate(dtParcela, "dd/MM/yyyy"));
				LogUtils.debug("ProcessoServices.calcular.dtPeriodoFinal: "+Util.formatDate(dtPeriodoFinal, "dd/MM/yyyy"));
				System.out.println("\n\n");
				
				IndicadorVariacao variacaoBase = IndicadorVariacaoDAO.get(cdIndicadorBase, Util.getPrimeiroDiaMes(dtReferencia.get(Calendar.MONTH), dtReferencia.get(Calendar.YEAR)), connect);
				if(variacaoBase==null) {
					return new Result(-1, "Não existe variação cadastrada para o indicador base.");
				}
				Double vlParcela = new Double(variacaoBase.getPrVariacao());//new Double(variacaoBase==null ? 0.0 : variacaoBase.getPrVariacao());
				
				GregorianCalendar ultimoDiaMes = Util.getUltimoDiaMes(dtParcela.get(Calendar.MONTH), dtParcela.get(Calendar.YEAR));
				if(Util.compareDates(ultimoDiaMes, dtPeriodoFinal)>0) {
					dtParcela = (GregorianCalendar)dtPeriodoFinal.clone();
				}
				
				if((dtParcela.get(Calendar.DAY_OF_MONTH)<=15) && (Util.compareDates(dtParcela, dtPeriodoFinal)!=0 || dtParcela.get(Calendar.DAY_OF_MONTH)>15)) {
					nrMesesDT++;
					vlTotalDT += vlParcela;
				}
				
				if(Util.compareDates(dtParcela, dtPeriodoFinal)==0) {
					vlParcela = (vlParcela/30) * dtParcela.get(Calendar.DAY_OF_MONTH);
				}
				else if(dtParcela.get(Calendar.DAY_OF_MONTH)!=1) {
					vlParcela = (vlParcela/30) * (30 - dtParcela.get(Calendar.DAY_OF_MONTH) + 1);
				}
				
				/* ****************************** */
				/* ***** CORRECAO MONETARIA ***** */
				/* ****************************** */
				GregorianCalendar dtReferenciaValor = null;
				if(lgPeriodoFinalInclusivo) {
					dtReferenciaValor = (GregorianCalendar)dtReferencia.clone();
				}
				else {
					dtReferenciaValor = (GregorianCalendar)dtReferencia.clone();
					dtReferenciaValor.add(Calendar.DATE, -1);
				}
				
				dtCM = (GregorianCalendar)dtParcela.clone();
				
				while(Util.compareDates(dtCM, dtReferenciaValor)<0) {
					
//					ResultSetMap rsmIndicadorCM = IndicadorVariacaoServices.getAllByDtInicio(dtCM, connect);
//					if(rsmIndicadorCM.next()) {
//						prMes = rsmIndicadorCM.getDouble("pr_variacao");
//					}
//					else {
//						if(isConnectionNull)
//							connect.rollback();
//						return new Result(-1, "Nenhum indicador encontrado para o período.");
//					}
					
					IndicadorVariacao indicadorCM = IndicadorVariacaoDAO.get(cdIndicadorCorrecao, Util.getPrimeiroDiaMes(dtCM.get(Calendar.MONTH), dtCM.get(Calendar.YEAR)), connect);
					if(indicadorCM==null) {
						return new Result(-1, "Não existe variação cadastrada para o indicador de Correção Monetária.");
					}
					prMes = new Double(indicadorCM.getPrVariacao());//indicadorCM==null ? 0.0 : new Double(indicadorCM.getPrVariacao());
					
					prAliquotaCM  += (prMes / 100) + (prMes * prAliquotaCM / 100);
					
					dtCM.add(Calendar.MONTH, 1);
					
				}
				
				/* ****************************** */
				/* *********** JUROS ************ */
				/* ****************************** */
				Double prJuros = (vlTaxaJuros!=null ? vlTaxaJuros : 1.0);
				GregorianCalendar dtJuros = (GregorianCalendar)dtParcela.clone();
				
				if(Util.compareDates(dtJuros, dtInicial)<0) {
					dtJuros = (GregorianCalendar)dtInicial.clone();
				}
				
				while(Util.compareDates(dtJuros, dtReferenciaValor)<0) {
					prMes = 0.0;
					
//					ResultSetMap rsmIndicadorJuros = IndicadorVariacaoServices.getAllByDtInicio(dtJuros, connect);
//					if(rsmIndicadorJuros.next()) {
//						prMes = rsmIndicadorJuros.getDouble("pr_variacao");
//					}
//					else {
//						if(isConnectionNull)
//							connect.rollback();
//						return new Result(-1, "Nenhum indicador de juros encontrado para o período.");
//					}
					
					IndicadorVariacao indicadorJuros = IndicadorVariacaoDAO.get(cdIndicadorCorrecaoSecundario, 
							Util.getPrimeiroDiaMes(dtJuros.get(Calendar.MONTH), dtJuros.get(Calendar.YEAR)), connect);
					if(indicadorJuros==null) {
						return new Result(-1, "Não existe variação cadastrada para o indicador de Juros.");
					}
					prMes = new Double(indicadorJuros.getPrVariacao());//indicadorJuros==null ? 0.0 : new Double(indicadorJuros.getPrVariacao());
					
					prJuros += prMes;
					dtJuros.add(Calendar.MONTH, 1);
				}
				
				if(Util.compareDates(dtParcela, dtReferencia)>=0) {
					prAliquotaCM = 0.0;
				}
				
				//INSERINDO PARCELA
				Calculo parcela = new Calculo(cdProcesso, nrParcela, dtParcela, new Double(prAliquotaCM+1).toString(), vlParcela.floatValue(), 
						new Double((vlParcela*(prAliquotaCM+1))).floatValue(), prJuros.floatValue());
				
				HashMap<String, Object> register = new HashMap<>();
				register.put("NR_PARCELA", nrParcela);
				register.put("DT_PARCELA", dtParcela.clone());
				register.put("DS_COEFICIENTE", Util.arredondar(new Double(prAliquotaCM+1), 2)+"");
				register.put("VL_PARCELA", vlParcela.floatValue());
				register.put("VL_CORRIGIDO", new Double((vlParcela*(prAliquotaCM+1))).floatValue());
				register.put("PR_JUROS", prJuros.floatValue());
				
				rsmCalculos.addRegister(register);
				
//				Result result = CalculoServices.save(parcela, null, connect);
//				if(result.getCode()<0) {
//					if(isConnectionNull)
//						connect.rollback();
//					return result;
//				}
				
				//13o.
				if(dtParcela.get(Calendar.MONTH)==12 && lgDecimoTerceiro && nrMesesDT>0) {
					nrParcela++;
					Calculo decTerceiro = new Calculo(cdProcesso, nrParcela, Util.getUltimoDiaMes(dtParcela.get(Calendar.MONTH), dtParcela.get(Calendar.YEAR)), 
							new Double(prAliquotaCM+1).toString(), new Double((vlTotalDT/12*100)/100).floatValue(), new Double((vlParcela*(prAliquotaCM+1))).floatValue(), prJuros.floatValue());
					
					HashMap<String, Object> reg = new HashMap<>();
					reg.put("NR_PARCELA", nrParcela);
					reg.put("DT_PARCELA", Util.getUltimoDiaMes(dtParcela.get(Calendar.MONTH), dtParcela.get(Calendar.YEAR)).clone());
					register.put("DS_COEFICIENTE", Util.arredondar(new Double(prAliquotaCM+1), 2)+"");
					reg.put("VL_PARCELA", new Double((vlTotalDT/12*100)/100).floatValue());
					reg.put("VL_CORRIGIDO", new Double((vlParcela*(prAliquotaCM+1))).floatValue());
					reg.put("PR_JUROS", prJuros.floatValue());
					
					rsmCalculos.addRegister(reg);
					
//					result = CalculoServices.save(decTerceiro, null, connect);
//					if(result.getCode()<0) {
//						if(isConnectionNull)
//							connect.rollback();
//						return result;
//					}
					
					nrMesesDT = 0;
					vlTotalDT = 0.0;
				}
				
				dtParcela.add(Calendar.MONTH, 1);
					
			}
			
			if(isConnectionNull)
				connect.commit();
			
			//ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			//criterios.add(new ItemComparator("A.CD_PROCESSO", Integer.toString(cdProcesso), ItemComparator.EQUAL, Types.INTEGER));
			//ResultSetMap rsmCalculo = CalculoServices.find(criterios, connect);
			
			rsmCalculos.beforeFirst();
			
			System.out.println(rsmCalculos);
			
			Result result = new Result(1, "Cálculo realizado com sucesso.");
			result.addObject("RSMCALCULOS", rsmCalculos);
			
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! ProcessoServices.calcular: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result aplicarRegrasGpnWorkflow(ResultSetMap rsmRegras, Processo itemAnterior, Processo itemAtual, AuthData auth){
		return aplicarRegrasGpnWorkflow(rsmRegras, itemAnterior, itemAtual, auth, null);
	}
	
	public static Result aplicarRegrasGpnWorkflow(ResultSetMap rsmRegras, Processo itemAnterior, Processo itemAtual, AuthData auth, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Result result = new Result(1);
			
			ResultSetMap rsmEventosWorkflow = new ResultSetMap();
			ResultSetMap rsmAndamentosWorkflow = new ResultSetMap();
			
			while(rsmRegras.next()) {
				ResultSetMap rsmAcao = (ResultSetMap)rsmRegras.getObject("RSMACAO");				
				
				while(rsmAcao.next()) {
					switch(rsmAcao.getInt("tp_acao")) {
						case WorkflowAcaoServices.TP_ACAO_ENVIAR_EMAIL: {
							int cdModeloEmail = ParametroServices.getValorOfParametroAsInteger("CD_MODELO_EMAIL_PROCESSO", 0, 0, connect);
							result = getDadosEmail(itemAtual.getCdProcesso(), connect);
							if(result.getCode()<=0) {
								return result;
							}

							Result rAux = executeModeloWeb(cdModeloEmail, 0, itemAtual.getCdProcesso(), 0, connect);
							if(rAux.getCode()>0) {
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
							
							if(rsmAcao.getInt("tp_contagem_prazo")==WorkflowAcaoServices.TP_CONTAGEM_DIAS_CORRIDOS)
								DateUtil.addDays(dtFinal, rsmAcao.getInt("nr_dias"));
							else if(rsmAcao.getInt("tp_contagem_prazo")==WorkflowAcaoServices.TP_CONTAGEM_DIAS_UTEIS)
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
										agenda.setCdPessoa(itemAtual.getCdAdvogado());										
										break;
									case WorkflowAcaoServices.TP_RESPONSAVEL_GRUPO:
										agenda.setCdGrupoTrabalho(itemAtual.getCdGrupoTrabalho());
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
							itemAtual.setCdTipoSituacao(rsmAcao.getInt("cd_tipo_situacao"));
							result = ProcessoServices.save(itemAtual, null, null, null, auth.getUsuario().getCdUsuario(), null, auth, connect);
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
	
	public static Result getProcessoSemPagamento() {
		return getProcessoSemPagamento(0, null);
	}
	
	public static Result getProcessoSemPagamento(int lgAlerta) {
		return getProcessoSemPagamento(lgAlerta, null);
	}
	
	public static Result getProcessoSemPagamento(int lgAlerta, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(true);
			}

			Result result = new Result(1);
			
			int nrMeses = ParametroServices.getValorOfParametroAsInteger("NR_MESES_PROCESSO_SEM_PAGAMENTO", 0, 0, connect);
			
			GregorianCalendar hoje = new GregorianCalendar();
			hoje.add(Calendar.MONTH, (nrMeses*-1));
						
			PreparedStatement pstmt = connect.prepareStatement(
					" SELECT A.cd_processo "
					+ " FROM prc_processo A"
					+ " LEFT OUTER JOIN prc_processo_financeiro B ON (A.cd_processo = B.cd_processo)"
					+ " LEFT OUTER JOIN adm_conta_receber C ON (B.cd_conta_receber=C.cd_conta_receber)"
					+ " WHERE NOT EXISTS (SELECT X.* FROM adm_movimento_conta_receber X "
					+ "						JOIN adm_movimento_conta Y ON (X.cd_conta=Y.cd_conta)"
					+ "						WHERE X.cd_conta_receber = C.cd_conta_receber "
					+ "						AND Y.dt_movimento>=?)");
			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(hoje));
			ResultSetMap rsmAux = new ResultSetMap(pstmt.executeQuery());
			ArrayList<Integer> processos = new ArrayList<>();
			while(rsmAux.next()) {
				processos.add(rsmAux.getInt("cd_processo"));
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_PROCESSO", Util.join(processos), ItemComparator.IN, Types.INTEGER));
						
			ResultSetMap rsm = find(criterios, null, connect);
			
			if(lgAlerta==0)
				lgAlerta = ParametroServices.getValorOfParametroAsInteger("LG_ALERTA", 0, 0, connect);
			if(lgAlerta>0) {
				if(rsm.next()) {
					Mensagem msg = new Mensagem(0, "Processos sem pagamento", "Existem processos sem pagamento a "+nrMeses+" meses.", 
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
						System.out.println("ERRO! ProcessoServices.getProcessoSemPagamento:\n"+result.getMessage());
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
	
	public static Result getProcessosCadastrados(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int tpPeriodo, 
			int cdCliente, int cdGrupoTrabalho) {
		return getProcessosCadastrados(dtInicial, dtFinal, tpPeriodo, cdCliente, cdGrupoTrabalho, 0, null);
	}
	
	public static Result getProcessosCadastrados(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int tpPeriodo, 
			int cdCliente, int cdGrupoTrabalho, int lgAlerta) {
		return getProcessosCadastrados(dtInicial, dtFinal, tpPeriodo, cdCliente, cdGrupoTrabalho, lgAlerta, null);
	}
	
	public static Result getProcessosCadastrados(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int tpPeriodo, 
			int cdCliente, int cdGrupoTrabalho, int lgAlerta, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int fator = Calendar.DAY_OF_MONTH;
			if(tpPeriodo==0) //diario
				fator = Calendar.DAY_OF_MONTH;
			else if(tpPeriodo==1) //semanal
				fator = Calendar.WEEK_OF_MONTH;
			else if(tpPeriodo==2) //mensal
				fator = Calendar.MONTH;
				
			
			GregorianCalendar dtTemp = (GregorianCalendar)dtInicial.clone();

			Result result = new Result(1);
			ResultSetMap rsm = new ResultSetMap();
			
			while(dtTemp.compareTo(dtFinal)<=0) {
				HashMap<String, Object> register = new HashMap<String, Object>();
				
				register.put("DT_INFO", dtTemp);
				register.put("DT_INFO_LABEL", Util.formatDate(dtTemp, Util.formatDate(dtTemp, "MM/yyyy")));//+"/"+dtTemp.get(Calendar.YEAR)));
				
				String sql = " SELECT COUNT(A.*) as QT_PROCESSOS "
						+ " FROM prc_processo A"
						+ " LEFT OUTER JOIN prc_parte_cliente B ON (A.cd_processo=B.cd_processo)"
						+ " LEFT OUTER JOIN agd_grupo C ON (A.cd_grupo_trabalho=C.cd_grupo)"
						+ " WHERE A.dt_cadastro>=? AND A.dt_cadastro<=?"
						+ (cdCliente>0?" AND B.cd_pessoa="+cdCliente:"")
						+ (cdGrupoTrabalho>0?" AND C.cd_grupo="+cdGrupoTrabalho:"");
				
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtTemp));
				GregorianCalendar dtTempAux = (GregorianCalendar)dtTemp.clone();
				dtTempAux.add(fator, 1);
				pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtTempAux));
				
				System.out.println(pstmt);
				
				ResultSet rs = pstmt.executeQuery();
				
				register.put("QT_PROCESSOS_CADASTRADOS", (rs.next()?rs.getInt("QT_PROCESSOS"):null));
				
				rsm.addRegister(register);
				
				//-----------------\\
				dtTemp.add(fator, 1);
			}
			
			if(isConnectionNull)
				connect.commit();
			
			rsm.beforeFirst();
			
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
	
	public static Result getProcessosEncerrados(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int tpPeriodo, 
			int cdCliente, int cdGrupoTrabalho) {
		return getProcessosEncerrados(dtInicial, dtFinal, tpPeriodo, cdCliente, cdGrupoTrabalho, 0, null);
	}
	
	public static Result getProcessosEncerrados(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int tpPeriodo, 
			int cdCliente, int cdGrupoTrabalho, int lgAlerta) {
		return getProcessosEncerrados(dtInicial, dtFinal, tpPeriodo, cdCliente, cdGrupoTrabalho, lgAlerta, null);
	}
	
	public static Result getProcessosEncerrados(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int tpPeriodo, 
			int cdCliente, int cdGrupoTrabalho, int lgAlerta, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int fator = Calendar.DAY_OF_MONTH;
			if(tpPeriodo==0) //diario
				fator = Calendar.DAY_OF_MONTH;
			else if(tpPeriodo==1) //semanal
				fator = Calendar.WEEK_OF_MONTH;
			else if(tpPeriodo==2) //mensal
				fator = Calendar.MONTH;
				
			
			GregorianCalendar dtTemp = (GregorianCalendar)dtInicial.clone();

			Result result = new Result(1);
			ResultSetMap rsm = new ResultSetMap();
			
			while(dtTemp.compareTo(dtFinal)<=0) {
				HashMap<String, Object> register = new HashMap<String, Object>();
				
				register.put("DT_INFO", dtTemp);
				register.put("DT_INFO_LABEL", Util.formatDate(dtTemp, Util.formatDate(dtTemp, "MM/yyyy")));//+"/"+dtTemp.get(Calendar.YEAR)));
				
				String sql = " SELECT COUNT(A.*) as QT_PROCESSOS "
						+ " FROM prc_processo A"
						+ " LEFT OUTER JOIN prc_parte_cliente B ON (A.cd_processo=B.cd_processo)"
						+ " LEFT OUTER JOIN agd_grupo C ON (A.cd_grupo_trabalho=C.cd_grupo)"
						+ " WHERE 1=1"
						+ (cdCliente>0?" AND B.cd_pessoa="+cdCliente:"")
						+ (cdGrupoTrabalho>0?" AND C.cd_grupo="+cdGrupoTrabalho:"")
						+ " AND EXISTS ("
						+ "		SELECT X.* FROM prc_processo_sentenca X"
						+ "     WHERE A.cd_processo = X.cd_processo"
						+ "		AND X.dt_sentenca>=? AND X.dt_sentenca<=?"
						+ ")";
				
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtTemp));
				GregorianCalendar dtTempAux = (GregorianCalendar)dtTemp.clone();
				dtTempAux.add(fator, 1);
				pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtTempAux));
				
				ResultSet rs = pstmt.executeQuery();
				
				register.put("QT_PROCESSOS_ENCERRADOS", (rs.next()?rs.getInt("QT_PROCESSOS"):null));
				
				rsm.addRegister(register);
				
				//-----------------\\
				dtTemp.add(fator, 1);
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
	
	public static Result getProcessosSemAndamento(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdCliente, int cdGrupoTrabalho) {
		return getProcessosSemAndamento(dtInicial, dtFinal, cdCliente, cdGrupoTrabalho, 0, null);
	}
	
	public static Result getProcessosSemAndamento(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdCliente, int cdGrupoTrabalho, int lgAlerta) {
		return getProcessosSemAndamento(dtInicial, dtFinal, cdCliente, cdGrupoTrabalho, lgAlerta, null);
	}
	
	public static Result getProcessosSemAndamento(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdCliente, int cdGrupoTrabalho, int lgAlerta, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
							
			Result result = new Result(1);
			
			PreparedStatement pstmt = connect.prepareStatement(
					" SELECT A.cd_processo FROM prc_processo A"
					+ " LEFT OUTER JOIN prc_parte_cliente A1 ON (A.cd_processo = A1.cd_processo)"
					+ " WHERE NOT EXISTS (SELECT B.* FROM prc_processo_andamento B"
					+ "						WHERE A.cd_processo=B.cd_processo"
					+ "						AND B.dt_andamento>=? AND B.dt_andamento<=?)"
					+ " AND A.st_processo=?"
					+ (cdCliente>0 ? " AND A1.cd_pessoa="+cdCliente : "")
					+ (cdGrupoTrabalho>0 ? " AND A.cd_grupo_trabalho="+cdGrupoTrabalho : "")
					+ " ORDER BY A.dt_cadastro DESC"
					+ " LIMIT 100");
			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
			pstmt.setInt(3,  ST_PROCESSO_ATIVO);
			
			ResultSet rs = pstmt.executeQuery();
			ArrayList<Integer> processos = new ArrayList<>();
			while(rs.next()) {
				processos.add(rs.getInt("cd_processo"));
			}
			rs.close();
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_PROCESSO", Util.join(processos), ItemComparator.IN, Types.INTEGER));
						
			ResultSetMap rsm = find(criterios, null, connect);
			
			while(rsm.next()) {
				ResultSetMap rsmAux = getAndamentos(rsm.getInt("cd_processo"), true);
				if(rsmAux.next()) {
								
					rsm.setValueToField("NM_TIPO_ANDAMENTO", rsmAux.getString("NM_TIPO_ANDAMENTO"));
					rsm.setValueToField("DT_ANDAMENTO", Util.convTimestampToCalendar(rsmAux.getTimestamp("DT_ANDAMENTO")));
					rsm.setValueToField("TXT_ANDAMENTO", rsmAux.getString("TXT_ANDAMENTO"));
				}
			}
			rsm.beforeFirst();
			
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
	
	public static Result importarProcesso(byte[] blbArquivo, AuthData auth) {
		try {
			
			FileOutputStream fos = new FileOutputStream("prc_to_import.csv"); 
			fos.write(blbArquivo);
		    fos.flush();
		    fos.close();
		    
		    Result result = GenericImport.importPrcProcesso("prc_to_import.csv", auth);
			
			return result;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Result calculoPrevidenciario(GregorianCalendar dtPeriodoInicial, GregorianCalendar dtPeriodoFinal, Boolean lgPeriodoFinalInclusivo, 
			int cdIndicadorBase, Double prJuros, Boolean lgDecTerceiro, int cdIndicadorCorrecao) {
		return calculoPrevidenciario(dtPeriodoInicial, dtPeriodoFinal, lgPeriodoFinalInclusivo, cdIndicadorBase, prJuros, lgDecTerceiro, cdIndicadorCorrecao, null);
	}
	
	public Result calculoPrevidenciario(GregorianCalendar dtPeriodoInicial, GregorianCalendar dtPeriodoFinal, Boolean lgPeriodoFinalInclusivo, 
			int cdIndicadorBase, Double prJuros, Boolean lgDecTerceiro, int cdIndicadorCorrecao, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsmCalculos = new ResultSetMap();
			Result result = new Result(1);
			
			int nrParcela = 0;
			Double vlOriginal = 0.0;
			Double vlCorrigido = 0.0;
			Double vlJuros = 0.0;
			Double vlTotal = 0.0;		
			
			int qtMeses = Util.getQuantidadeMeses(dtPeriodoInicial, dtPeriodoFinal);
			prJuros *= qtMeses;
			
			GregorianCalendar dtParcela = (GregorianCalendar)dtPeriodoInicial.clone();
			
			while((lgPeriodoFinalInclusivo && Util.compareDates(dtParcela, dtPeriodoFinal)<=0) || Util.compareDates(dtParcela, dtPeriodoFinal)<0) {
				int ultimoDiaMes = Util.getUltimoDiaMes(dtParcela.get(Calendar.MONTH), dtParcela.get(Calendar.YEAR)).get(Calendar.DATE);
				nrParcela++;
				
				IndicadorVariacao variacaoBase = IndicadorVariacaoDAO.get(cdIndicadorBase, Util.getPrimeiroDiaMes(dtParcela.get(Calendar.MONTH), dtParcela.get(Calendar.YEAR)), connect);
				if(variacaoBase==null) {
					return new Result(-1, "Não existe variação cadastrada para o indicador base.");
				}
				vlOriginal = new Double(variacaoBase.getPrVariacao());
				
				/*
				 * corrigir vlOriginal do 1o e ultimo mes, caso nao tenha sido 
				 * completo
				 */
				if(Util.compareDates(dtPeriodoInicial, dtParcela)==0) {//1o. mes
					vlOriginal = (vlOriginal / ultimoDiaMes) * (ultimoDiaMes - dtParcela.get(Calendar.DATE));
				}
				if(Util.compareDates(dtPeriodoFinal, dtParcela)==0) {//ultimo mes
					vlOriginal = (vlOriginal / ultimoDiaMes) * (dtParcela.get(Calendar.DATE));
				}
				
				/*
				 * correção monetária
				 */	
				IndicadorVariacao indicadorCM = IndicadorVariacaoDAO.get(cdIndicadorCorrecao, Util.getPrimeiroDiaMes(dtParcela.get(Calendar.MONTH), dtParcela.get(Calendar.YEAR)), connect);
				if(indicadorCM==null) {
					return new Result(-1, "Não existe variação cadastrada para o indicador de Correção Monetária.");
				}
				Double prMes = new Double(indicadorCM.getPrVariacao());
				prMes += 1.0;
				
				vlCorrigido = vlOriginal * prMes;
				
				/*
				 * juros
				 */
				if(prJuros>0) {
					vlJuros = vlCorrigido * prJuros;
				}
				
				vlTotal = vlCorrigido + vlJuros;
				
				/*
				 * registro
				 */
				HashMap<String, Object> register = new HashMap<>();
				register.put("NR_PARCELA", nrParcela);
				register.put("DT_PARCELA", dtParcela.clone());
				register.put("DS_MES_PARCELA", Util.formatDate(dtParcela, "MM/yyyy"));
				register.put("VL_PARCELA", vlOriginal);
				register.put("DS_COEFICIENTE", new Double(prMes));
				register.put("VL_CORRIGIDO", vlCorrigido);
				register.put("PR_JUROS", prJuros);
				register.put("VL_JUROS", vlJuros);
				register.put("VL_TOTAL", vlTotal);
				
				rsmCalculos.addRegister(register);
				
				/*
				 * 13o.
				 */
				if(lgDecTerceiro) {
					Double diff = 0.0;
					if(dtPeriodoInicial.get(Calendar.YEAR)==dtParcela.get(Calendar.YEAR) && dtParcela.get(Calendar.MONTH)==Calendar.DECEMBER) { //1o. ano
						diff = (vlOriginal/12)*(12 - (dtParcela.get(Calendar.MONTH)));
					}
					else if((dtPeriodoFinal.get(Calendar.YEAR) == dtParcela.get(Calendar.YEAR)) && (dtParcela.get(Calendar.MONTH) == dtPeriodoFinal.get(Calendar.MONTH))) { //ultimo ano
						diff = (vlOriginal/12)*(dtParcela.get(Calendar.MONTH)+1);
					}
					else if(dtParcela.get(Calendar.MONTH) == Calendar.DECEMBER) { // demais anos
						diff = vlOriginal;
					}
					
					if(diff>0.0) {
						vlCorrigido = diff * prMes;
						if(prJuros>0) {
							vlJuros = vlCorrigido * prJuros;
						}
						
						vlTotal = vlCorrigido + vlJuros;
						
						nrParcela++;
						register = new HashMap<>();
						register.put("NR_PARCELA", nrParcela);
						register.put("DT_PARCELA", dtParcela.clone());
						register.put("DS_MES_PARCELA", "13/"+dtParcela.get(Calendar.YEAR));
						register.put("VL_PARCELA", diff);
						register.put("DS_COEFICIENTE", new Double(prMes));
						register.put("VL_CORRIGIDO", vlCorrigido);
						register.put("PR_JUROS", prJuros);
						register.put("VL_JUROS", vlJuros);
						register.put("VL_TOTAL", vlTotal);
						
						rsmCalculos.addRegister(register);
					}
				}
				
				dtParcela.add(Calendar.MONTH, 1);
				prJuros -= 1;
			}
			
			
			result.setMessage("Cálculo realizado com sucesso.");
			result.addObject("RSMCALCULOS", rsmCalculos);
			
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
	
	private static Result notificar(int cdProcessoOld, int cdProcesso, Connection connect) {
		try {
			ResultSetMap rsmRegras = new ResultSetMap();//RegraNotificacaoServices.getRegrasProcesso(connect);
			Processo processoOld   = ProcessoDAO.get(cdProcessoOld, connect);
			Processo processo      = ProcessoDAO.get(cdProcesso, connect);
			
			String dsProcesso = null;
			
			if(cdProcessoOld==0 && cdProcesso>0) {//INSERT
				rsmRegras = RegraNotificacaoServices.getRegrasProcesso(RegraNotificacaoServices.TP_ACAO_INSERT, connect);
				dsProcesso = processo.getResumo(connect);
			}
			else if(cdProcessoOld>0 && cdProcesso>0) {//UPDATE
				rsmRegras = RegraNotificacaoServices.getRegrasProcesso(RegraNotificacaoServices.TP_ACAO_UPDATE, connect);	
				dsProcesso = processo.getResumo(connect);
			}
			else if(cdProcessoOld>0 && cdProcesso==0) {//DELETE
				rsmRegras = RegraNotificacaoServices.getRegrasProcesso(RegraNotificacaoServices.TP_ACAO_DELETE, connect);
				processoOld = ProcessoDAO.get(cdProcessoOld);
				processoOld.getResumo(null);
				dsProcesso = processoOld.getResumo(connect);
				
				processo = processoOld;
				cdProcesso = cdProcessoOld;
			}
			
			while(rsmRegras.next()) {
				
				boolean notificar = true;
				
				//se existe o critério na regra e ele é diferente do valor no processo, NÃO notifica
				if(notificar && (rsmRegras.getInt("cd_grupo_trabalho", 0) > 0)) {
					if(rsmRegras.getInt("cd_grupo_trabalho") != processo.getCdGrupoTrabalho())
						notificar = false;
				}
				if(notificar && (rsmRegras.getInt("cd_advogado", 0) > 0)) {
					if(rsmRegras.getInt("cd_advogado") != processo.getCdAdvogado())
						notificar = false;
				}
				if(notificar && (rsmRegras.getInt("cd_cliente", 0) > 0)) {
					if(!Util.contains(getParteCliente(cdProcesso, connect), "CD_PESSOA", rsmRegras.getInt("cd_cliente")))
						notificar = false;
				}
				
				if(notificar) {
					Notificacao notificacao = new Notificacao(0 /*cdNotificacao*/, 
							0/*cdUsuario*/, 
							rsmRegras.getString("NM_REGRA_NOTIFICACAO")/*dsAssunto*/, 
							Notificacao.INFO/*tpNotificacao*/, 
							dsProcesso/*txtNotificacao*/, 
							new GregorianCalendar()/*dtNotificacao*/, 
							null/*dtLeitura*/, 
							0/*cdMensagem*/, 
							rsmRegras.getInt("CD_REGRA_NOTIFICACAO"), 
							Integer.toString(cdProcesso)/*txtObjeto*/);
					
					RegraNotificacaoServices.notificar(rsmRegras.getInt("cd_regra_notificacao"), notificacao, cdProcesso, 0, connect);
				}
				
			}
			
			return new Result(1);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		
	}
	
	/**
	 * Notifica um determinado grupo de trabalho sobre processos não ajuizados
	 * 
	 * @category PRC
	 * @return
	 */
	public static Result notificarProcessoNaoAjuizado() {
		Connection connect = Conexao.conectar();
		try	{
			connect.setAutoCommit(false);
			
			//Regra
			RegraNotificacao regraNotificacao = null;
			ArrayList<ItemComparator> crt = new ArrayList<>();
			crt.add(new ItemComparator("A.tp_acao", Integer.toString(RegraNotificacaoServices.TP_ACAO_NAO_AJUIZADO), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = RegraNotificacaoServices.find(crt, connect);
			if(rsm.next()) {
				regraNotificacao = RegraNotificacaoDAO.get(rsm.getInt("cd_regra_notificacao"), connect);
			}
			
			if(regraNotificacao==null) {
				connect.rollback();
				return new Result(-2, "Nenhuma regra encontrada");
			}
			
			GregorianCalendar dtCorteInicial = DateUtil.todayWithoutTime();
			dtCorteInicial.add(Calendar.DATE, -regraNotificacao.getQtHorasFinalPrazo());

			GregorianCalendar dtCorteFinal = (GregorianCalendar)dtCorteInicial.clone();
			dtCorteFinal.set(Calendar.HOUR,   23);
			dtCorteFinal.set(Calendar.MINUTE, 59);
			dtCorteFinal.set(Calendar.SECOND, 59);
			
			PreparedStatement ps = connect.prepareStatement(
								   " SELECT cd_processo "
								 + " FROM prc_processo "
					   			 + " WHERE nr_processo IS NULL "
					   			 + " AND nr_juizo IS NULL "
					   			 + " AND cd_juizo IS NULL "
					   			 + " AND (dt_cadastro BETWEEN ? AND ?) ");
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtCorteInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtCorteFinal));
			
			rsm = new ResultSetMap(ps.executeQuery());
			while(rsm.next()) {
				Processo processo = ProcessoDAO.get(rsm.getInt("cd_processo"), connect);
				
				Notificacao notificacao = new Notificacao(0 /*cdNotificacao*/, 
						0/*cdUsuario*/, 
						regraNotificacao.getNmRegraNotificacao()/*dsAssunto*/, 
						Notificacao.INFO/*tpNotificacao*/, 
						processo.getResumo(connect)/*txtNotificacao*/, 
						new GregorianCalendar()/*dtNotificacao*/, 
						null/*dtLeitura*/, 
						0/*cdMensagem*/, 
						regraNotificacao.getCdRegraNotificacao()/*cdRegraNotificacao*/, 
						Integer.toString(processo.getCdProcesso())/*txtObjeto*/);
				
				Result result = RegraNotificacaoServices.notificar(regraNotificacao.getCdRegraNotificacao(), notificacao,processo.getCdProcesso(), 0, connect);
			}
			
			connect.commit();
			
			return new Result(1);
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.notificarProcessoNaoAjuizado: " +  e);
			return new Result(-1, "");
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Lembrete para verificação do processos sob responsabilidade 
	 * 
	 * @category PRC
	 * @return
	 */
	public static Result notificarAdvResponsavel() {
		Connection connect = Conexao.conectar();
		try	{
			connect.setAutoCommit(false);
			
			PreparedStatement ps = connect.prepareStatement(
								   " SELECT cd_processo, cd_advogado "
								 + " FROM prc_processo "
					   			 + " WHERE LG_IMPORTANTE = 1  ");
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			while(rsm.next()) {
				Processo processo = ProcessoDAO.get(rsm.getInt("cd_processo"), connect);
				JSONObject jsonProcesso = processo.toJson();
				
				Usuario usuario = UsuarioServices.getByPessoa(rsm.getInt("cd_advogado"), connect);
				
				if(usuario==null) 
					continue;
				
				Notificacao notificacao = new Notificacao(0 /*cdNotificacao*/, 
						usuario.getCdUsuario()/*cdUsuario*/, 
						"LEBRETE: Verificar processo"/*dsAssunto*/, 
						Notificacao.INFO/*tpNotificacao*/, 
						processo.getResumo(connect)/*txtNotificacao*/, 
						new GregorianCalendar()/*dtNotificacao*/, 
						null/*dtLeitura*/, 
						0/*cdMensagem*/, 
						0/*cdRegraNotificacao*/, 
						jsonProcesso.toString()/*txtObjeto*/);
				
				Result result = NotificacaoServices.save(notificacao, null, connect);
			}
			
			connect.commit();
			
			return new Result(1);
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.notificarProcessoNaoAjuizado: " +  e);
			return new Result(-1, "");
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getLogCompliance(int cdProcesso, boolean lgDelete) {
		return getLogCompliance(cdProcesso, lgDelete, null);
	}
	
	public static ResultSetMap getLogCompliance(int cdProcesso, boolean lgDelete, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			ResultSetMap rsm = ComplianceManager
					.search(" SELECT * FROM prc_processo "
						+ " WHERE 1=1"
						+ (lgDelete ? 
						  " AND tp_acao_compliance="+ComplianceManager.TP_ACAO_DELETE	
						  :
						  " AND cd_processo="+cdProcesso)
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
				
				//partes
				rsm.setValueToField("nm_clientes", getClientes(rsm.getInt("cd_processo"), connect));
				rsm.setValueToField("nm_adversos", getAdversos(rsm.getInt("cd_processo"), connect));
				
				//adv responsavel
				if(rsm.getInt("cd_advogado", 0) > 0) {
					Pessoa pessoa = PessoaDAO.get(rsm.getInt("cd_advogado"), connect);
					rsm.setValueToField("nm_advogado_responsavel", pessoa.getNmPessoa());
				}
				
				//adv adverso
				if(rsm.getInt("cd_advogado_contrario", 0) > 0) {
					Pessoa pessoa = PessoaDAO.get(rsm.getInt("cd_advogado_contrario"), connect);
					rsm.setValueToField("nm_advogado_adverso", pessoa.getNmPessoa());
				}
				
				//grupo de processo
				if(rsm.getInt("cd_grupo_processo", 0) > 0) {
					GrupoProcesso grupoProcesso = GrupoProcessoDAO.get(rsm.getInt("cd_grupo_processo"), connect);
					rsm.setValueToField("nm_grupo_processo", grupoProcesso.getNmGrupoProcesso());
				}
				
				//grupo de trabalho
				if(rsm.getInt("cd_grupo_trabalho", 0) > 0) {
					Grupo grupoTrabalho = GrupoDAO.get(rsm.getInt("cd_grupo_trabalho"), connect);
					rsm.setValueToField("nm_grupo_trabalho", grupoTrabalho.getNmGrupo());
				}
				
				//situacao
				rsm.setValueToField("nm_st_processo", situacaoProcesso[rsm.getInt("st_processo")]);
				
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
			System.out.println("Erro ao buscar log: "+e.getMessage());
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	// ================================================================================
	/**
	 * @category angular-dashboard
	 */
	
	public static ResultSetMap getBaseAtiva() {
		return getBaseAtiva(null);
	}
	public static ResultSetMap getBaseAtiva(Connection connection) {
		boolean isConnectionNull = connection == null;
		try {
			if(isConnectionNull)
				connection = Conexao.conectar();			
			
			ResultSetMap rsm = new ResultSetMap();
			HashMap<String, Object> register = new HashMap<String, Object>();
			
			//  AÇÕES SINGULARES
			int cdGrupoProcesso = 0;
			PreparedStatement ps = connection.prepareStatement(
					  " SELECT * FROM prc_grupo_processo"
					+ " WHERE nm_grupo_processo iLike 'A__ES SINGULARES'");
			ResultSetMap rsmAux = new ResultSetMap(ps.executeQuery());
			if(rsmAux.next()) {
				cdGrupoProcesso = rsmAux.getInt("cd_grupo_processo");
			}
			
			ps = connection.prepareStatement(
					" SELECT COUNT(A.*) AS qt_processo"
					+ " FROM prc_processo A"
					+ " WHERE st_processo IN (?, ?)"
					+ " AND cd_grupo_processo = ?");
			ps.setInt(1, ST_PROCESSO_ATIVO);
			ps.setInt(2, ST_PROCESSO_REATIVADO);
			ps.setInt(3, cdGrupoProcesso);
			
			rsmAux = new ResultSetMap(ps.executeQuery());
			if(rsmAux.next()) {
				register.put("NM_CAT", "Ações Singulares");
				register.put("QT_PROCESSO", rsmAux.getInt("qt_processo"));
			} else {
				register.put("NM_CAT", "Ações Singulares");
				register.put("QT_PROCESSO", 0);
			}
			rsm.addRegister(register);
			
			// NÃO MASSA
			register = new HashMap<String, Object>();
			ps = connection.prepareStatement(
					" SELECT COUNT(A.*) AS qt_processo"
					+ " FROM prc_processo A"
					+ " JOIN agd_grupo B ON (A.cd_grupo_trabalho = B.cd_grupo)"
					+ " WHERE A.st_processo IN (?, ?)"
					+ " AND A.cd_grupo_processo <> ?"
					+ " AND B.nm_grupo iLike 'JUR_DICO 1'");
			ps.setInt(1, ST_PROCESSO_ATIVO);
			ps.setInt(2, ST_PROCESSO_REATIVADO);
			ps.setInt(3, cdGrupoProcesso);
			
			rsmAux = new ResultSetMap(ps.executeQuery());
			if(rsmAux.next()) {
				register.put("NM_CAT", "Não Massa");
				register.put("QT_PROCESSO", rsmAux.getInt("qt_processo"));
			} else {
				register.put("NM_CAT", "Não Massa");
				register.put("QT_PROCESSO", 0);
			}
			rsm.addRegister(register);
			
			// MASSA
			register = new HashMap<String, Object>();
			ps = connection.prepareStatement(
					" SELECT COUNT(A.*) AS qt_processo"
					+ " FROM prc_processo A"
					+ " JOIN agd_grupo B ON (A.cd_grupo_trabalho = B.cd_grupo)"
					+ " WHERE A.st_processo IN (?, ?)"
					+ " AND A.cd_grupo_processo <> ?"
					+ " AND ("
					+ "		B.nm_grupo iLike 'JUR_DICO 2'"
					+ "		OR B.nm_grupo iLike 'JUR_DICO 3'"
					+ "		OR B.nm_grupo iLike 'JUR_DICO 5'"
					+ ")");
			ps.setInt(1, ST_PROCESSO_ATIVO);
			ps.setInt(2, ST_PROCESSO_REATIVADO);
			ps.setInt(3, cdGrupoProcesso);
			
			rsmAux = new ResultSetMap(ps.executeQuery());
			if(rsmAux.next()) {
				register.put("NM_CAT", "Massa");
				register.put("QT_PROCESSO", rsmAux.getInt("qt_processo"));
			} else {
				register.put("NM_CAT", "Massa");
				register.put("QT_PROCESSO", 0);
			}
			rsm.addRegister(register);		
			
			
			return rsm;
		}
		catch (Exception e) {
			System.out.println("Erro ao buscar dados: "+e.getMessage());
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getCadastrosMensais(int mes) {
		return getCadastrosMensais(mes, new GregorianCalendar().get(Calendar.YEAR), null);
	}
	public static ResultSetMap getCadastrosMensais(int mes, int ano) {
		return getCadastrosMensais(mes, ano, null);
	}
	public static ResultSetMap getCadastrosMensais(int mes, int ano, Connection connection) {
		boolean isConnectionNull = connection == null;
		try {
			if(isConnectionNull)
				connection = Conexao.conectar();			
			
			GregorianCalendar dtInicial = new GregorianCalendar(ano, mes, 1, 0, 0, 0);
			GregorianCalendar dtFinal = (GregorianCalendar)dtInicial.clone();
			dtFinal.add(Calendar.MONTH, 1);
			
			PreparedStatement ps = connection.prepareStatement(
					  " SELECT COUNT(A.*) AS qt_processos, C.nm_grupo "
					+ " FROM prc_processo A"
					+ " LEFT OUTER JOIN agd_grupo C ON (A.cd_grupo_trabalho = C.cd_grupo)"
					+ " WHERE A.dt_cadastro BETWEEN ? AND ?"
					+ " GROUP BY C.nm_grupo"
					+ " ORDER BY C.nm_grupo");
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			return rsm;
		}
		catch (Exception e) {
			System.out.println("Erro ao buscar dados: "+e.getMessage());
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getEncerramentosMensais(int mes) {
		return getEncerramentosMensais(mes, null);
	}
	public static ResultSetMap getEncerramentosMensais(int mes, Connection connection) {
		boolean isConnectionNull = connection == null;
		try {
			if(isConnectionNull)
				connection = Conexao.conectar();
			
			ResultSetMap rsm = new ResultSetMap();
			
			
			
			
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
	
	public static ResultSetMap getAtivosPorGrupo() {
		return getAtivosPorGrupo(null);
	}
	public static ResultSetMap getAtivosPorGrupo(Connection connection) {
		boolean isConnectionNull = connection == null;
		try {
			if(isConnectionNull)
				connection = Conexao.conectar();
			
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement(
					  " SELECT COUNT(A.*) as QT_PROCESSOS, C.nm_grupo"
					+ " FROM prc_processo A"
					+ " LEFT OUTER JOIN agd_grupo C ON (A.cd_grupo_trabalho = C.cd_grupo)"
					+ " WHERE 1=1"
					+ " AND A.st_processo IN (1, 5)"
					+ " AND C.st_grupo = 1"
					+ " GROUP BY C.nm_grupo"
					+ " ORDER BY C.nm_grupo").executeQuery());
			
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
	
	public static ResultSetMap getAtivosPorFase() {
		return getAtivosPorFase(null);
	}
	public static ResultSetMap getAtivosPorFase(Connection connection) {
		boolean isConnectionNull = connection == null;
		try {
			if(isConnectionNull)
				connection = Conexao.conectar();
			
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement(
					  " SELECT COUNT(A.*) as QT_PROCESSOS, C.nm_tipo_situacao"
					+ " FROM prc_processo A"
					+ " LEFT OUTER JOIN prc_tipo_situacao C ON (A.cd_tipo_situacao = C.cd_tipo_situacao)"
					+ " WHERE 1=1"
					+ " AND A.st_processo IN (1, 5)"
					+ " GROUP BY C.nm_tipo_situacao"
					+ " ORDER BY C.nm_tipo_situacao").executeQuery());
			
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
	
	public static ResultSetMap getAtivosPorCidade(int limite) {
		return getAtivosPorCidade(limite, null);
	}
	public static ResultSetMap getAtivosPorCidade(int limite, Connection connection) {
		boolean isConnectionNull = connection == null;
		try {
			if(isConnectionNull)
				connection = Conexao.conectar();
			
			PreparedStatement ps = connection.prepareStatement(
					  " SELECT COUNT(A.*) as QT_PROCESSOS, C.nm_cidade"
					+ " FROM prc_processo A"
					+ " LEFT OUTER JOIN grl_cidade C ON (A.cd_cidade = C.cd_cidade)"
					+ " WHERE 1=1"
					+ " AND A.st_processo IN (?, ?)"
					+ " GROUP BY C.nm_cidade"
					+ " ORDER BY QT_PROCESSOS DESC"
					+ " LIMIT ?");
			ps.setInt(1, ST_PROCESSO_ATIVO);
			ps.setInt(2, ST_PROCESSO_REATIVADO);
			ps.setInt(3, limite);
			
			return new ResultSetMap(ps.executeQuery());
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
	
	public static ResultSetMap getAtivosPorEstado() {
		return getAtivosPorEstado(null);
	}
	public static ResultSetMap getAtivosPorEstado(Connection connection) {
		boolean isConnectionNull = connection == null;
		try {
			if(isConnectionNull)
				connection = Conexao.conectar();
			
			String[] uf = new String[10];
			
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement(
					  " SELECT COUNT(A.*) AS qt_processos, C.cd_estado"
					+ " FROM prc_processo A"
					+ " LEFT OUTER JOIN grl_cidade C ON (A.cd_cidade = C.cd_cidade)"
					+ " WHERE 1=1"
					+ " AND A.st_processo IN (1, 5)"
					+ " GROUP BY C.cd_estado"
					+ " ORDER BY qt_processos DESC").executeQuery());
			
			ResultSetMap rsmAux = new ResultSetMap();
			int totalOutros = 0;
			
			while(rsm.next()) {
				
				if(rsm.getPointer()<10) {
					Estado estado = EstadoDAO.get(rsm.getInt("cd_estado"), connection);				
					rsm.setValueToField("SG_ESTADO", estado.getSgEstado());
					rsmAux.addRegister(rsm.getRegister());
				} else {
					totalOutros += rsm.getInt("qt_processos");
				}				
			} rsm.beforeFirst();
			
			if(totalOutros>0) {
				HashMap<String, Object> reg = new HashMap<String, Object>();
				reg.put("SG_ESTADO", "OUTROS");
				reg.put("QT_PROCESSOS", totalOutros);
				rsmAux.addRegister(reg);
			}
			rsmAux.beforeFirst();
			
			return rsmAux;
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
	
	public static ResultSetMap getRepasseMensal() {
		return getRepasseMensal(null);
	}
	public static ResultSetMap getRepasseMensal(Connection connection) {
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
			
			ResultSetMap rsm = new ResultSetMap();
			
			while(dtInicial.before(dtFinal)) {
				
				GregorianCalendar dtAux = (GregorianCalendar)dtInicial.clone();
				dtAux.add(Calendar.MONTH, 1);
				
				PreparedStatement ps = connection.prepareStatement(
						  " SELECT COUNT(A.*) AS qt_processos "
						+ " FROM prc_processo A"
						+ " WHERE A.dt_repasse BETWEEN ? AND ?");
				ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
				ps.setTimestamp(2, Util.convCalendarToTimestamp(dtAux));
				
				ResultSetMap rsmAux = new ResultSetMap(ps.executeQuery());
				if(rsmAux.next()) {
					HashMap<String, Object> reg = new HashMap<String, Object>();
					reg.put("QT_PROCESSOS", rsmAux.getInt("QT_PROCESSOS"));
					reg.put("NM_MES", Util.formatDate(dtInicial, "MMM/yyyy"));
					rsm.addRegister(reg);
				}
				
				dtInicial.add(Calendar.MONTH, 1);
			}
			
			rsm.beforeFirst();			
			
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
	
	public static ResultSetMap getEncerramentoMensal() {
		return getEncerramentoMensal(null);
	}
	public static ResultSetMap getEncerramentoMensal(Connection connection) {
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
			
			
			ResultSetMap rsm = new ResultSetMap();
			
			while(dtInicial.before(dtFinal)) {
				
				GregorianCalendar dtAux = (GregorianCalendar)dtInicial.clone();
				dtAux.add(Calendar.MONTH, 1);
				
				PreparedStatement ps = connection.prepareStatement(
						  " SELECT COUNT(A.*) AS qt_processos "
						+ " FROM prc_processo A"
						+ " WHERE EXISTS ("
						+ " 	SELECT B.* "
						+ "		FROM prc_processo_sentenca B"
						+ "		WHERE B.cd_processo = A.cd_processo"
						+ "		AND B.dt_sentenca BETWEEN ? AND ?"
						+ " )");
				ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
				ps.setTimestamp(2, Util.convCalendarToTimestamp(dtAux));
				
				ResultSetMap rsmAux = new ResultSetMap(ps.executeQuery());
				if(rsmAux.next()) {
					HashMap<String, Object> reg = new HashMap<String, Object>();
					reg.put("QT_PROCESSOS", rsmAux.getInt("QT_PROCESSOS"));
					reg.put("NM_MES", Util.formatDate(dtInicial, "MMM/yyyy"));
					rsm.addRegister(reg);
				}
				
				dtInicial.add(Calendar.MONTH, 1);
			}
			
			rsm.beforeFirst();			
			
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
	
	public static ResultSetMap getEncerramentoAgrupado() {
		return getEncerramentoAgrupado(null);
	}
	public static ResultSetMap getEncerramentoAgrupado(Connection connection) {
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
			
			PreparedStatement ps = connection.prepareStatement(
					  " SELECT COUNT(A.*) AS qt_processos, B.nm_grupo"
					+ " FROM prc_processo A"
					+ " LEFT OUTER JOIN agd_grupo B ON (A.cd_grupo_trabalho = B.cd_grupo)"
					+ " WHERE EXISTS ("
					+ "		SELECT * FROM prc_processo_sentenca Z"
					+ "		WHERE A.cd_processo = Z.cd_processo"
					+ "		AND Z.dt_sentenca BETWEEN ? AND ?"
					+ " )"
					+ " GROUP BY B.nm_grupo"
					+ " ORDER BY B.nm_grupo");
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
					
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
	
	public static ResultSetMap getEncerramentosTipo() {
		return getEncerramentosTipo(null);
	}
	public static ResultSetMap getEncerramentosTipo(Connection connection) {
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
						
			PreparedStatement ps = connection.prepareStatement(
					  " SELECT COUNT(A.*) AS qt_sentenca, A.tp_sentenca"
					  + " FROM prc_processo_sentenca A"
					  + " WHERE A.dt_sentenca BETWEEN ? AND ?"
					  + " GROUP BY A.tp_sentenca"
					  + " ORDER BY qt_sentenca DESC");
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));

			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			while(rsm.next()) {
				rsm.setValueToField("NM_SENTENCA", tipoSentenca[rsm.getInt("TP_SENTENCA")]);
			}
			rsm.beforeFirst();
			
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