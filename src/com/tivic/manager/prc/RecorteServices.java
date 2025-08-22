package com.tivic.manager.prc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.EstadoServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.ComplianceManager;
import com.tivic.manager.util.LogUtils;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;
import sol.util.Util;

public class RecorteServices {
	
	public static final int ST_NAO_PROCESSADO = 0;
	public static final int ST_PROCESSADO 	  = 1;
	public static final int ST_COM_ERRO 	  = 2;
	public static final int ST_COM_ALERTA 	  = 3;
	public static final int ST_LIXO   	 	  = 4;
	
	public static final String[] situacoes = {"Não Processado", "Processado", "Com erro", "Necessita de Atenção", "Na lixeira"};
	
	public static Result save(Recorte recorte){
		return save(recorte, null, null);
	}
	
	public static Result save(Recorte recorte, AuthData auth){
		return save(recorte, auth, null);
	}
	
	public static Result save(Recorte recorte, AuthData auth, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(recorte==null)
				return new Result(-1, "Erro ao salvar. Recorte é nulo");
			
			//COMPLIANCE
			int tpAcao = ComplianceManager.TP_ACAO_ANY;
			
			if(recorte.getTxtRecorte()!=null)
				recorte.setTxtRecorte(recorte.getTxtRecorte());
			if(recorte.getTxtAndamento()!=null)
				recorte.setTxtAndamento(recorte.getTxtAndamento());
			
			int retorno;
			if(recorte.getCdRecorte()==0){
				retorno = RecorteDAO.insert(recorte, connect);
				recorte.setCdRecorte(retorno);
				
				tpAcao = ComplianceManager.TP_ACAO_INSERT;
			}
			else {
				retorno = RecorteDAO.update(recorte, connect);
				
				tpAcao = ComplianceManager.TP_ACAO_UPDATE;
			}
			

			ComplianceManager.process(recorte, auth, tpAcao, connect);
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "RECORTE", recorte);
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
	
	public static Result remove(int cdRecorte, AuthData auth){
		return remove(cdRecorte, false, auth, null);
	}
	
	public static Result remove(int cdRecorte, boolean cascade){
		return remove(cdRecorte, cascade, null, null);
	}
	
	public static Result remove(int cdRecorte, boolean cascade, AuthData auth){
		return remove(cdRecorte, cascade, auth, null);
	}
	
	public static Result remove(int cdRecorte, boolean cascade, AuthData auth, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			Recorte recorte = RecorteDAO.get(cdRecorte, connect);
			
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
				
			if(!cascade || retorno>0)
				retorno = RecorteDAO.delete(cdRecorte, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este recorte está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			ComplianceManager.process(recorte, auth, ComplianceManager.TP_ACAO_DELETE, connect);
			
			return new Result(1, "Recorte excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir recorte!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByProcesso(int cdProcesso) {
		return getAllByProcesso(cdProcesso, null);
	}
	
	public static ResultSetMap getAllByProcesso(int cdProcesso, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM prc_recorte " +
															 " WHERE cd_processo  = "+cdProcesso +
															 " ORDER BY dt_processamento").executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		
		int nrRegistros = -1;
		for(int i=0; i<criterios.size(); i++)	{
			if (criterios.get(i).getColumn().equalsIgnoreCase("nrRegistros")) {
				nrRegistros = Integer.valueOf(criterios.get(i).getValue().toString().trim());
				criterios.remove(i);
				i--;
			}
		}
		
		String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(nrRegistros, 0);
		
		String sql = "SELECT "+sqlLimit[0]
				+ " A.*, "
				+ " B.nr_processo, B.cd_advogado, "
				+ " C.nm_estado, "
				+ " D.nm_servico, D.tp_servico, "
				+ " E.nm_pessoa AS nm_advogado "
				+ " FROM prc_recorte A "
				+ " LEFT OUTER JOIN prc_processo B ON (A.cd_processo = B.cd_processo) "
				+ " LEFT OUTER JOIN grl_estado C ON (A.cd_estado = C.cd_estado) "
				+ " LEFT OUTER JOIN prc_servico_recorte D ON (A.cd_servico = D.cd_servico)"
				+ " LEFT OUTER JOIN grl_pessoa E ON (B.cd_advogado = E.cd_pessoa)";
		
		String orderBySql = "ORDER BY cd_processo, cd_estado, dt_publicacao";
		
		LogUtils.debug(Search.getStatementSQL(sql, orderBySql, criterios, true));
		
		ResultSetMap rsm = Search.find(sql, orderBySql + sqlLimit[1], criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		
		while(rsm.next()) {
			rsm.setValueToField("TXT_RECORTE", StringEscapeUtils.unescapeXml(rsm.getString("TXT_RECORTE")));
		}
		
		return rsm;
	}
	

	public static Recorte getByIdRecorte(String idRecorte) {
		return getByIdRecorte(idRecorte, 0, null);
	}
	
	public static Recorte getByIdRecorte(String idRecorte, int cdServico) {
		return getByIdRecorte(idRecorte, cdServico, null);
	}

	public static Recorte getByIdRecorte(String idRecorte, int cdServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_RECORTE WHERE ID_RECORTE=?"
							 +(cdServico>0 ? " AND CD_SERVICO="+cdServico : ""));
			pstmt.setString(1, idRecorte);
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Recorte(rs.getInt("cd_recorte"),
						rs.getString("id_recorte"),
						rs.getString("txt_recorte"),
						rs.getInt("cd_processo"),
						(rs.getTimestamp("dt_processamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_processamento").getTime()),
						(rs.getTimestamp("dt_busca")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_busca").getTime()),
						rs.getInt("st_recorte"),
						rs.getInt("cd_servico"),
						(rs.getTimestamp("dt_publicacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_publicacao").getTime()),
						rs.getString("nm_diario"),
						rs.getString("nr_pagina"),
						rs.getString("nm_orgao"),
						rs.getString("nm_juizo"),
						rs.getString("txt_andamento"),
						rs.getInt("cd_estado"),
						rs.getInt("st_anterior"),
						rs.getString("id_processo_recorte"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecorteServices.getByIdRecorte: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecorteServices.getByIdRecorte: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result marcarProcessado(Recorte recorte){
		return marcarProcessado(recorte, 0, null);
	}
	
	public static Result marcarProcessado(Recorte recorte, AuthData auth){
		return marcarProcessado(recorte, 0, auth, null);
	}
	
	public static Result marcarProcessado(Recorte recorte, int cdProcesso, AuthData auth){
		return marcarProcessado(recorte, cdProcesso, auth, null);
	}
	
	public static Result marcarProcessado(Recorte recorte, int cdProcesso, AuthData auth, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			if(recorte==null)
				return new Result(-1, "Erro ao marcar como processado. Recorte é nulo");
			
			if(cdProcesso>0)
				recorte.setCdProcesso(cdProcesso);
			
			recorte.setDtProcessamento(new GregorianCalendar());
			recorte.setStRecorte(ST_PROCESSADO);
			
			Result r = save(recorte, auth, connect);
			
			if(r.getCode()>0) {
				r.setMessage("Recorte marcado como processado com sucesso");
				return r;
			}
			else 
				return r;
		}
		catch(Exception e){
			e.printStackTrace();
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getLogCompliance(int cdRecorte, boolean lgDelete) {
		return getLogCompliance(cdRecorte, lgDelete, null);
	}
	
	public static ResultSetMap getLogCompliance(int cdRecorte, boolean lgDelete, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			ResultSetMap rsm = ComplianceManager
					.search(" SELECT * FROM prc_recorte "
						+ " WHERE 1=1"
						+ (lgDelete ? 
						  " AND tp_acao_compliance="+ComplianceManager.TP_ACAO_DELETE	
						  :
						  " AND cd_recorte="+cdRecorte)
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
				
				if(rsm.getInt("cd_processo", 0) > 0) {
					Processo processo = ProcessoDAO.get(rsm.getInt("cd_processo"), connect);
					rsm.setValueToField("nr_processo", processo.getNrProcesso());
				}
				
				rsm.setValueToField("nm_st_recorte", situacoes[rsm.getInt("st_recorte")].toUpperCase());
				
				if(rsm.getInt("cd_servico", 0) > 0) {
					ServicoRecorte servico = ServicoRecorteDAO.get(rsm.getInt("cd_servico"), connect);
					rsm.setValueToField("nm_servico", servico.getNmServico());
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
			System.out.println("Erro ao buscar log: "+e.getMessage());
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	
	/**
	 * DEPRECIADOS
	 **/
	
	public static Result loadRecortes(int cdServico, GregorianCalendar dtInicial, GregorianCalendar dtFinal, String sgEstado, String keyword, String idCliente) {
		
		switch (cdServico) {
			case 0: //T-Legal
				return loadTLegalRecortesWebService(dtInicial, dtFinal, sgEstado, keyword, idCliente);
			case 1: //Sercortes
				return loadSercortesWebService(dtInicial, dtFinal, idCliente);
			case 2: //Alerte
				return loadAlerteWebService(dtInicial, dtFinal);
			default:
				return null;
		}
		
	}
	
	
	public static Result loadTLegalRecortesWebService(GregorianCalendar dtInicial, GregorianCalendar dtFinal, String sgEstado, String keyword, String idCliente) {
		try {
			String dsDtInicial = com.tivic.manager.util.Util.formatDate(dtInicial, "dd/MM/yyyy");
			String dsDtFinal = com.tivic.manager.util.Util.formatDate(dtFinal, "dd/MM/yyyy");
			
			String content = "";
			
			GregorianCalendar hoje = new GregorianCalendar();

			//a url é diferente para recortes com mais de 3 dias de publicação
			String url = (sol.util.DateServices.countDaysBetween(dtInicial, hoje)<=3 ? 
					"http://www.t-legal.com/WebService.asmx/WebPubsTLegal" : 
					"http://www.t-legal.com/webservice.asmx/WebPubsTLegal_Antigas");
			
			//String url = "http://www.t-legal.com/WebService.asmx/WebPubsTLegal";
			
			HttpClient client = new HttpClient();
			HttpMethod method = new GetMethod(url+"?dtinicial="+dsDtInicial+
					"&dtfinal="+dsDtFinal+
					"&uf="+sgEstado+
					"&keyword="+keyword+
					"&cliente="+idCliente);
			
			LogUtils.debug(url+"?dtinicial="+dsDtInicial+
					"&dtfinal="+dsDtFinal+
					"&uf="+sgEstado+
					"&keyword="+keyword+
					"&cliente="+idCliente);

			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
					new DefaultHttpMethodRetryHandler(3, false));

			try {
				int statusCode = client.executeMethod(method);

				if (statusCode != HttpStatus.SC_OK) {
					//System.err.println("Method failed: " + method.getStatusLine());
					
					String errorMessage = method.getStatusLine().toString();
					
					switch(statusCode) {
						case HttpStatus.SC_NOT_FOUND: 
							errorMessage = "O serviço da T-Legal está fora do ar no momento. Tente novamente mais tarde.";
							break;
						case HttpStatus.SC_FORBIDDEN: 
							errorMessage = "Acesso ao serviço da T-Legal não permitido através de sua rede.";
							break;
						case HttpStatus.SC_INTERNAL_SERVER_ERROR: 
							errorMessage = "Erro interno do servidor da T-Legal. Verifique os campos enviados.";
							
							String message = method.getResponseBodyAsString();
							
							if(message.indexOf("Nenhum registro atende a condicao")!=-1){
								return new Result(-2, "Nenhum registro encontrado. Verifique os campos de filtro e tente novamente.");
							}
							
							break;
					}
					
					
					return new Result(-1, errorMessage);
				}

				content = method.getResponseBodyAsString();

				return new Result(1, "Recortes encontrados", "RECORTES", content);
			} 
			catch (HttpException e) {
				System.err.println("Fatal protocol violation: " + e.getMessage());
				e.printStackTrace();
				return new Result(-1, "Erro ao acessar serviço da T-Legal. Violação de protocolo.");
			} 
			catch (IOException e) {
				System.err.println("Fatal transport error: " + e.getMessage());
				e.printStackTrace();
				return new Result(-1, "Erro ao acessar serviço da T-Legal. Erro de comunicação.");
			} 
			finally {
				method.releaseConnection();
			}  
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao acessar o webservice!");
		}
	}
	
	public static Result loadSercortesWebService(GregorianCalendar dtInicial, GregorianCalendar dtFinal, String idCliente) {
		try {
			String content = "";
			
			String dsDtInicial = com.tivic.manager.util.Util.formatDate(dtInicial, "MM/dd/yyyy");
			String dsDtFinal = com.tivic.manager.util.Util.formatDate(dtFinal, "MM/dd/yyyy");
			
			String token = "00609c7b8e59ece9952e9b5dd649a4fb";
			String pwCliente = "sercortes123";
			
			HttpClient client = new HttpClient();
			HttpMethod method = new GetMethod("http://acessoweb.brasilia.me:8090/sapweb/1/webservice/listarPublicacoes.php?"
					+ "token="+token
					+ "&inicio="+dsDtInicial
					+ "&fim="+dsDtFinal
					+ "&codigoCliente="+idCliente
					+ "&senhaCliente="+pwCliente);
			
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
					new DefaultHttpMethodRetryHandler(3, false));

			try {
				int statusCode = client.executeMethod(method);

				if (statusCode != HttpStatus.SC_OK) {
					//System.err.println("Method failed: " + method.getStatusLine());
					
					String errorMessage = method.getStatusLine().toString();
					
					switch(statusCode) {
						case HttpStatus.SC_NOT_FOUND: 
							errorMessage = "O serviço da Sercortes está fora do ar no momento. Tente novamente mais tarde.";
							break;
						case HttpStatus.SC_FORBIDDEN: 
							errorMessage = "Acesso ao serviço da Sercortes não permitido através de sua rede.";
							break;
						case HttpStatus.SC_INTERNAL_SERVER_ERROR: 
							errorMessage = "Erro interno do servidor da Sercortes. Verifique os campos enviados.";
							
							String message = method.getResponseBodyAsString();
							
							if(message.indexOf("Nenhum registro atende a condicao")!=-1){
								return new Result(-2, "Nenhum registro encontrado. Verifique os campos de filtro e tente novamente.");
							}
							
							break;
					}
					
					
					return new Result(-1, errorMessage);
				}

				content = method.getResponseBodyAsString();

				return new Result(1, "Recortes encontrados", "RECORTES", content);
			}
			catch (HttpException e) {
				System.err.println("Fatal protocol violation: " + e.getMessage());
				e.printStackTrace();
				return new Result(-1, "Erro ao acessar serviço da Sercortes. Violação de protocolo.");
			} 
			catch (IOException e) {
				System.err.println("Fatal transport error: " + e.getMessage());
				e.printStackTrace();
				return new Result(-1, "Erro ao acessar serviço da Sercortes. Erro de comunicação.");
			} 
			finally {
				method.releaseConnection();
			}  
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao acessar o webservice!");
		}
	}
	
	public static Result loadAlerteWebService(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		try {
			String dsDtInicial = com.tivic.manager.util.Util.formatDate(dtInicial, "YYYY-MM-dd");
			String dsDtFinal = com.tivic.manager.util.Util.formatDate(dtFinal, "YYYY-MM-dd");
			String content = "";

			HttpClient client = new HttpClient();
			HttpMethod method = new GetMethod("http://ws.alerte.com.br/recortes/"+dsDtInicial+"/"+dsDtFinal);

			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
					new DefaultHttpMethodRetryHandler(3, false));
			
			Credentials defaultcreds = new UsernamePasswordCredentials("conquista", "conquista1234");
			client.getState().setCredentials(AuthScope.ANY, defaultcreds);

			try {
				int statusCode = client.executeMethod(method);

				if (statusCode != HttpStatus.SC_OK) {
					//System.err.println("Method failed: " + method.getStatusLine());
					
					String errorMessage = method.getStatusLine().toString();
					
					switch(statusCode) {
						case HttpStatus.SC_NOT_FOUND: 
							errorMessage = "O serviço da Alerte está fora do ar no momento. Tente novamente mais tarde.";
							break;
						case HttpStatus.SC_FORBIDDEN: 
							errorMessage = "Acesso ao serviço da Alerte não permitido através de sua rede.";
							break;
						case HttpStatus.SC_INTERNAL_SERVER_ERROR: 
							errorMessage = "Erro interno do servidor da Alerte. Verifique os campos enviados.";
							
							String message = method.getResponseBodyAsString();
							
							if(message.indexOf("Nenhum registro atende a condicao")!=-1){
								return new Result(-2, "Nenhum registro encontrado. Verifique os campos de filtro e tente novamente.");
							}
							
							break;
					}
					
					
					return new Result(-1, errorMessage);
				}

				content = method.getResponseBodyAsString();

				return new Result(1, "Recortes encontrados", "RECORTES", content);
			} 
			catch (HttpException e) {
				System.err.println("Fatal protocol violation: " + e.getMessage());
				e.printStackTrace();
				return new Result(-1, "Erro ao acessar serviço da Alerte. Violação de protocolo.");
			} 
			catch (IOException e) {
				System.err.println("Fatal transport error: " + e.getMessage());
				e.printStackTrace();
				return new Result(-1, "Erro ao acessar serviço da Alerte. Erro de comunicação.");
			} 
			finally {
				method.releaseConnection();
			}  
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao acessar o webservice!");
		}
	}
	
	public static Result processarRecortes(int cdServico, String content) {
		switch (cdServico) {
			case 0: //T-Legal
				return processarXMLRecortesTLegal(content);
			case 1: //Sercortes
				return processarJSONRecortesSercortes(content);
			case 2: //Alerte
				return processarXMLRecortesAlerte(content);
			default:
				return null;
		}
	}
	

	public static Result processarXMLRecortesTLegal(String content) {
		try {
		    
			ResultSetMap rsmRecortes = new ResultSetMap();
		    
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    
		    ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes("UTF-8"));
		    Document document = builder.parse(bais);
	
		    NodeList nodeList = document.getElementsByTagName("Table");
		    for (int i = 0; i < nodeList.getLength(); i++) {
		    	Node node = nodeList.item(i);
		    	
		    	//System.out.println("["+i+"] "+node.getNodeName() +": "+ node.getNodeType());
		    	if (node.getNodeType() == Node.ELEMENT_NODE) {
		    		
		    		HashMap<String, Object> register = new HashMap<String, Object>();
		    		
		    		NodeList childNodes = node.getChildNodes();
		    		
		    		for (int j = 0; j < childNodes.getLength(); j++) {
		    			Node cNode = childNodes.item(j);
		    			
		    			if (cNode instanceof Element) {
		    				
		    				if(cNode.getFirstChild().getNodeType() == Node.TEXT_NODE) {
			    				
		    					//String value = new String(cNode.getFirstChild().getNodeValue().getBytes(), "ISO-8859-1");
		    					String value = cNode.getFirstChild().getNodeValue();
			    				
		    					//System.out.println(value);
		    					
			    				if(cNode.getNodeName().equals("Empresa")) {
			    					register.put("NM_EMPRESA", value);
			    				} 
			    				else if(cNode.getNodeName().equals("Partes")) {
			    					register.put("NM_PARTE", value);
			    				} 
			    				else if(cNode.getNodeName().equals("DataPublicacao")) {
			    					register.put("DS_DT_PUBLICACAO", value);
			    					
			    					String pattern = "yyyy-MM-dd'T'HH:mm:ss";
			    					SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			    					
			    					GregorianCalendar dtPublicacao = new GregorianCalendar();
			    					dtPublicacao.setTimeInMillis(sdf.parse(value).getTime());
			    					
			    					register.put("DT_PUBLICACAO", dtPublicacao);
			    				} 
			    				else if(cNode.getNodeName().equals("Processo")) {
			    					if(!value.equals("Não Informado")) {
			    						register.put("NR_PROCESSO", value);
			    						register.put("NR_PROCESSO_SCORE", 0);
			    					}
			    				} 
			    				else if(cNode.getNodeName().equals("Diario")) {
			    					register.put("NM_DIARIO", value);
			    				} 
			    				else if(cNode.getNodeName().equals("Pagina")) {
			    					register.put("NR_PAGINA", value);
			    				} 
			    				else if(cNode.getNodeName().equals("Orgao")) {
			    					register.put("NM_ORGAO", value);
			    				} 
			    				else if(cNode.getNodeName().equals("Juizo")) {
			    					
			    					value = value.replaceAll("=", "");
			    					
			    					if(value.indexOf("|")!=-1) {
			    						StringTokenizer stoken = new StringTokenizer(value, "|");
			    						
			    						value = "";
			    						while(stoken.hasMoreElements()){
			    							String token = ((String)stoken.nextElement()).trim();
			    							if(!token.equals(""))
			    								value += token+"\n";
			    						}
			    					}
			    					
			    					register.put("NM_JUIZO", value);
			    				} 
			    				else if(cNode.getNodeName().equals("Andamento")) {
			    					register.put("TXT_ANDAMENTO", value);
			    					register.put("LG_PROCESSO", 1);
			    					
			    					//if(i==4)
			    					//	System.out.println(value);
			    					scanAndamento(value, register);
			    				} 
			    				else if(cNode.getNodeName().equals("CodigoRelacional")) {
			    					register.put("ID_RECORTE", value);
			    					
			    					Result r = processarSituacaoRecorte(value);
			    					if(r.getCode() >=0)
			    						register.put("ST_RECORTE", r.getCode());
			    				} 
			    				else if(cNode.getNodeName().equals("DataCirculacao")) {
			    					register.put("DS_DT_CIRCULACAO", value);
			    					
			    					String pattern = "yyyy-MM-dd'T'HH:mm:ss";
			    					SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			    					
			    					GregorianCalendar dtCirculacao = new GregorianCalendar();
			    					dtCirculacao.setTimeInMillis(sdf.parse(value).getTime());
			    					
			    					register.put("DT_CIRCULACAO", dtCirculacao);
			    				} 
		    				}
		    			}
		    		}
		    		
		    		rsmRecortes.addRegister(register);
		    	}

		    }
		        
		    ArrayList<String> columns = new ArrayList<String>();
		    columns.add("LG_PROCESSO");
		    rsmRecortes.orderBy(columns);
		    rsmRecortes.beforeFirst();

		    return new Result(1, rsmRecortes.size() + " recorte(s) encontrado(s)", "RSM_RECORTES", rsmRecortes);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao processar recortes!");
		} 
	}
	
	public static Result processarJSONRecortesSercortes(String content) {
		try {
		    
			ResultSetMap rsmRecortes = new ResultSetMap();
			
			JSONObject json = new JSONObject(content);
			JSONArray array  = json.getJSONArray("resposta");
		    
			for(int i = 0; i<array.length(); i++){				
				JSONObject jOutput = array.getJSONObject(i);
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				
				register.put("NM_EMPRESA", "Actio");
				
				if(jOutput.has("dataDivulgacao") && !jOutput.get("dataDivulgacao").equals(null)) {
					register.put("DS_DT_PUBLICACAO", (String)jOutput.get("dataDivulgacao"));
					
					GregorianCalendar dtPublicacao = com.tivic.manager.util.Util.convStringToCalendar((String)jOutput.get("dataDivulgacao"));
					
					register.put("DT_PUBLICACAO", dtPublicacao);
				}
				
				if(jOutput.has("nProcesso") && !jOutput.get("nProcesso").equals(null)) {
					register.put("NR_PROCESSO", (String)jOutput.get("nProcesso"));
					register.put("NR_PROCESSO_SCORE", 0);
				}
				
				if(jOutput.has("orgao") && !jOutput.get("orgao").equals(null)) {
					register.put("NM_ORGAO", (String)jOutput.get("orgao"));
				}
				
				if(jOutput.has("publicacao") && !jOutput.get("publicacao").equals(null)) {
					
					byte[] andamento = org.apache.commons.codec.binary.Base64.decodeBase64((String)jOutput.getString("publicacao"));
					String txtAndamento = new String(andamento);
					
					register.put("TXT_ANDAMENTO", txtAndamento);
					register.put("LG_PROCESSO", 1);
					
					scanAndamento(txtAndamento, register);
				}
				
				if(jOutput.has("codPublicacao") && !jOutput.get("codPublicacao").equals(null)) {
					String cdPublicacao = (String)jOutput.get("codPublicacao");
					
					register.put("ID_RECORTE", cdPublicacao);
					
					Result r = processarSituacaoRecorte(cdPublicacao);
					if(r.getCode() >=0)
						register.put("ST_RECORTE", r.getCode());
				}
				
				if(jOutput.has("dataPublicacao") && !jOutput.get("dataPublicacao").equals(null)) {
					register.put("DS_DT_CIRCULACAO", (String)jOutput.get("dataPublicacao"));
					
					GregorianCalendar dtCirculacao = com.tivic.manager.util.Util.convStringToCalendar((String)jOutput.get("dataPublicacao"));
				
					register.put("DT_CIRCULACAO", dtCirculacao);
				}
				
				rsmRecortes.addRegister(register);
			}
		    
		    ArrayList<String> columns = new ArrayList<String>();
		    columns.add("LG_PROCESSO");
		    rsmRecortes.orderBy(columns);
		    rsmRecortes.beforeFirst();

		    return new Result(1, rsmRecortes.size() + " recorte(s) encontrado(s)", "RSM_RECORTES", rsmRecortes);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao processar recortes!");
		} 
	}
	
	public static Result processarXMLRecortesAlerte(String content) {
		try {
		    
			ResultSetMap rsmRecortes = new ResultSetMap();
		    
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    
		    ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes("UTF-8"));
		    Document document = builder.parse(bais);
	
		    NodeList nodeList = document.getElementsByTagName("publicacao");
		    for (int i = 0; i < nodeList.getLength(); i++) {
		    	Node node = nodeList.item(i);
		    	
		    	//System.out.println("["+i+"] "+node.getNodeName() +": "+ node.getNodeType());
		    	if (node.getNodeType() == Node.ELEMENT_NODE) {
		    		
		    		HashMap<String, Object> register = new HashMap<String, Object>();
		    		
		    		NodeList childNodes = node.getChildNodes();
		    		
		    		for (int j = 0; j < childNodes.getLength(); j++) {
		    			Node cNode = childNodes.item(j);
		    			
		    			if (cNode instanceof Element) {
		    				
		    				if(cNode.getFirstChild().getNodeType() == Node.TEXT_NODE) {
			    				
		    					//String value = new String(cNode.getFirstChild().getNodeValue().getBytes(), "ISO-8859-1");
		    					String value = cNode.getFirstChild().getNodeValue();
			    				
		    					//System.out.println(value);
		    					
		    					register.put("NM_EMPRESA", "PROCURADORIA GERAL DO MUNICÍPIO - PMVC");
			    				
		    					if(cNode.getNodeName().equals("data_publicacao")) {
			    					register.put("DS_DT_PUBLICACAO", value);
			    					register.put("DS_DT_CIRCULACAO", value);
			    					
//			    					String pattern = "yyyy-MM-dd'T'HH:mm:ss";
//			    					SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//			    					GregorianCalendar dtPublicacao = new GregorianCalendar();
//			    					dtPublicacao.setTimeInMillis(sdf.parse(value).getTime());
			    					
			    					GregorianCalendar dtPublicacao = com.tivic.manager.util.Util.convStringToCalendar2(value);
			    					
			    					register.put("DT_PUBLICACAO", dtPublicacao);
			    					register.put("DT_CIRCULACAO", dtPublicacao);
			    				} 
			    				else if(cNode.getNodeName().equals("processo")) {
			    					if(!value.equals("Não Informado")) {
			    						register.put("NR_PROCESSO", value);
			    						register.put("NR_PROCESSO_SCORE", 0);
			    					}
			    				} 
			    				else if(cNode.getNodeName().equals("caderno")) {
			    					register.put("NM_DIARIO", value);
			    				} 
			    				else if(cNode.getNodeName().equals("pagina")) {
			    					register.put("NR_PAGINA", value);
			    				} 
			    				else if(cNode.getNodeName().equals("texto")) {
			    					register.put("TXT_ANDAMENTO", value);
			    					register.put("LG_PROCESSO", 1);
			    					
			    					//if(i==4)
			    					//	System.out.println(value);
			    					scanAndamento(value, register);
			    				} 
			    				else if(cNode.getNodeName().equals("codigo")) {
			    					register.put("ID_RECORTE", value);
			    					
			    					Result r = processarSituacaoRecorte(value);
			    					if(r.getCode() >=0)
			    						register.put("ST_RECORTE", r.getCode());
			    				} 
		    				}
		    			}
		    		}
		    		
		    		rsmRecortes.addRegister(register);
		    	}

		    }
		        
		    ArrayList<String> columns = new ArrayList<String>();
		    columns.add("LG_PROCESSO");
		    rsmRecortes.orderBy(columns);
		    rsmRecortes.beforeFirst();
		    
		    return new Result(1, rsmRecortes.size() + " recorte(s) encontrado(s)", "RSM_RECORTES", rsmRecortes);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao processar recortes!");
		} 
	}
	
	private static Result processarSituacaoRecorte(String idRecorte) {
		
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT cd_recorte FROM prc_recorte" +
	                " WHERE id_recorte = ? ");
			
			pstmt.setString(1, idRecorte);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			return rsm.next() ? new Result(1, "Recorte encontrado.") : new Result(0, "Recorte não encontrado.");			
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecorteServices.scanAndamento: " + sqlExpt);
			return new Result(-1, "Erro ao processar situação do recorte");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecorteServices.scanAndamento: " + e);
			return new Result(-1, "Erro ao processar situação do recorte");
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	private static void scanAndamento(String value, HashMap<String, Object> register) {
		Connection connect = Conexao.conectar();
		try {
			
			Pattern r;
			Matcher m;
			
			//Nº Processo
			if(register.get("NR_PROCESSO")==null) {
	
//				r = Pattern.compile("^[0-9]+\\)\\s([0-9\\\\./\\s{1}]+)|"+
//									"^([0-9\\\\.\\s{1}]+)|"+
//									"\\bprocesso\\s:{0,1}([0-9\\\\.\\s{1}]+)\\b|"+
//									"\\bprocesso\\sn[º|°]\\s[a-z]{2,5}([0-9\\\\.\\s{1}]+)\\b|"+
//									"\\bnumera[ç|c][ã|a]o [ú|u]nica[:\\s|\\s]([0-9\\\\.\\s{1}]+)\\b" +
//									"\\bet\\s([0-9\\\\.\\s{1}]+)\\b" +
//									"\\bord\\s([0-9\\\\.\\s{1}]+)\\b" +
//									"\\bn[º|°]*[:\\s|\\s]*([0-9\\\\.\\s{1}]+)\\b", 
//									Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL);
				
				//r = Pattern.compile("([0-9[\\\\.\\s]{1}]{23,25})", 
				
				String regex = "([0-9]+-[0-9]{2,}.[0-9]{4,}.[0-9]*.[0-9]{2,}.[0-9]{4,})|([0-9]{20,25})";
				r = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL);
				
				m = r.matcher(value);
	
				if (m.find( )) {
					
//					System.out.println("("+m.toString()+")");
//					for(int i=0; i<m.groupCount(); i++){
//						System.out.print(" group "+i+": "+m.group(i));
//					}
//					
//					System.out.println("Found value: " + m.group(1) );
//					
					String nrProcesso = "[Não encontrado]";
					for(int i=0; i<m.groupCount(); i++){
						if(m.group(i)!=null) {
							nrProcesso = m.group(i);
							break;
						}
					}
										
									    
					//verificando mascara
					String[] partes = nrProcesso.trim().split(" ");
					
					//System.out.println("("+nrProcesso+")");
					
//					for (int i=0; i<partes.length ;i++) {
//						System.out.print(i+": "+partes[i]+"\t");
//					}
					
					if(partes.length>1) {
						nrProcesso = partes[0];
						
						if(partes.length>2 || partes[0].length()<9) {
							nrProcesso+="."+partes[1];
						}
					}
					
					//System.out.println("Nº: "+nrProcesso);
					
					register.put("NR_PROCESSO", nrProcesso.trim());
					register.put("NR_PROCESSO_SCORE", 1);
					

					
				} 
				else {
					//System.out.println("NO MATCH");
					register.put("NR_PROCESSO", "[Não encontrado]");
					register.put("NR_PROCESSO_SCORE", 3);
				}
	
			}
			
			if (register.get("NR_PROCESSO") != null && !register.get("NR_PROCESSO").equals("") && !register.get("NR_PROCESSO").equals("[Não encontrado]")) {
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("nr_processo", (String)register.get("NR_PROCESSO"), ItemComparator.LIKE_ANY, Types.VARCHAR));
			
				ResultSetMap rsmProcessos = ProcessoServices.find(criterios, new ArrayList<String>(), null);
			
				if(rsmProcessos.next()) {
					
					register.put("REG_PROCESSO", rsmProcessos.getRegister());
					register.put("LG_PROCESSO", 0);
				}
			}
			
			//Datas
			r = Pattern.compile("(data\\s\\w*\\s\\w*):(\\d{2}/\\d{2}/\\d{4})\\s", 
					Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL);
			m = r.matcher(value);

			ResultSetMap rsmDatas = new ResultSetMap();
			while (m.find( )) {
				HashMap<String, Object> regData = new HashMap<String, Object>();
				regData.put("NM_DT_SIGNIFICATIVA", m.group(1).trim());
				regData.put("DT_SIGNIFICATIVA", m.group(2).trim());
				
				rsmDatas.addRegister(regData);
				
			}
			register.put("RSM_DATAS", rsmDatas);
			
			//Julgamento
			r = Pattern.compile("julgo.*|"+
								"conclus[aã]o\\s?:.*|"+
								"decis[aã]o\\s?:.*|"+
								"despacho:\\s.*|"+
								"sendo assim,\\s.*|"+
								"\\s[\\w\\.\\(\\)]*\\sexarou[\\s:](.*)|"+
								"intimese.*|"+
								"intima[cç][aã]o.*|"+
								"manifestese.*|"+
								"extingo.*", 
								Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL);
			m = r.matcher(value);
			
			register.put("TXT_JULGAMENTO", "[Não encontrado]");
			if (m.find( )) {
				register.put("TXT_JULGAMENTO", m.group(0).trim());
				register.put("TXT_JULGAMENTO_SCORE", 1);
				
				
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecorteServices.scanAndamento: " + e);
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getSumario(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdServico) {
		return getSumario(dtInicial, dtFinal, cdServico, null);
	}
	
	public static ResultSetMap getSumario(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdServico, Connection connection) {
		boolean isConnectonNull = connection==null;
		try {
			if(isConnectonNull)
				connection = Conexao.conectar();
						
			PreparedStatement ps = connection.prepareStatement(""
					+ " SELECT COUNT(A.*) AS qt_recorte, A.cd_estado, A.st_recorte"
					+ " FROM prc_recorte A"
					+ " WHERE A.dt_publicacao BETWEEN ? AND ?"
					+ " AND A.cd_servico = ?"
					+ " GROUP BY A.cd_estado, A.st_recorte"
					+ " ORDER BY A.cd_estado, qt_recorte DESC");
			ps.setTimestamp(1, com.tivic.manager.util.Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, com.tivic.manager.util.Util.convCalendarToTimestamp(dtFinal));
			ps.setInt(3, cdServico);
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());		
			while(rsm.next()) {
				Estado uf = EstadoDAO.get(rsm.getInt("cd_estado"), connection);
				rsm.setValueToField("sg_estado", (uf!=null ? uf.getSgEstado() : "---"));
				rsm.setValueToField("nm_st_recorte", situacoes[rsm.getInt("st_recorte")]);
			}
			rsm.beforeFirst();
			
			if(rsm.getLines().size() <= 0) {
				HashMap<String, Object> notFound = new HashMap<>();
				notFound.put("QT_RECORTE", 0);
				notFound.put("SG_ESTADO", "---");
				notFound.put("NM_ST_RECORTE", "---");
				rsm.getLines().add(notFound);
			}
			
			return rsm;
		} catch(Exception e) {
			System.out.println("Erro! RecorteServices.getSumario");
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectonNull)
				Conexao.desconectar(connection);
		}
	}
	

}
