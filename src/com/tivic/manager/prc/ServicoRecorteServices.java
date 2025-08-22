package com.tivic.manager.prc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.EstadoServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.FixServices;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ServicoRecorteServices {

	public static final int TP_TLEGAL = 0;
	public static final int TP_SERCORTES = 1;
	public static final int TP_ALERTE = 2;
	public static final int TP_KURIER = 3;
	public static final int TP_KURIER_PERSONALIZADO = 4;
	public static final int TP_KURIER_SOAP = 5;
	public static final int TP_PUBLICACOES_ONLINE = 6;
	
	public static Result save(ServicoRecorte servicoRecorte){
		return save(servicoRecorte, null, null);
	}

	public static Result save(ServicoRecorte servicoRecorte, AuthData authData){
		return save(servicoRecorte, authData, null);
	}

	public static Result save(ServicoRecorte servicoRecorte, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(servicoRecorte==null)
				return new Result(-1, "Erro ao salvar. ServicoRecorte é nulo");

			int retorno;
			if(servicoRecorte.getCdServico()==0){
				retorno = ServicoRecorteDAO.insert(servicoRecorte, connect);
				servicoRecorte.setCdServico(retorno);
			}
			else {
				retorno = ServicoRecorteDAO.update(servicoRecorte, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "SERVICORECORTE", servicoRecorte);
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
	public static Result remove(int cdServico){
		return remove(cdServico, false, null, null);
	}
	public static Result remove(int cdServico, boolean cascade){
		return remove(cdServico, cascade, null, null);
	}
	public static Result remove(int cdServico, boolean cascade, AuthData authData){
		return remove(cdServico, cascade, authData, null);
	}
	public static Result remove(int cdServico, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = ServicoRecorteDAO.delete(cdServico, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_servico_recorte ORDER BY nm_servico");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAtivos() {
		return getAtivos(null);
	}

	public static ResultSetMap getAtivos(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * "
					+ " FROM prc_servico_recorte "
					+ " WHERE st_servico=1"
					+ " ORDER BY nm_servico");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteServices.getAtivos: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteServices.getAtivos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM prc_servico_recorte", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	/*
	 * METODOS DE RECUPERACAO DE RECORTES DOS SERVICOS 
	 * PARA USO DO TASK AGENDADO
	 */
	
	/**
	 * Busca todos os recortes de todos os serviços de recortes de acordo com suas configurações
	 * @return
	 */
	public static Result executeServices() {
		return executeServices(null, null, null);
	}
	
	public static Result executeServices(String dtInicial, String dtFinal) {
		return executeServices(Util.convStringToCalendar4(dtInicial), Util.convStringToCalendar4(dtFinal), null);
	}
	
	/**
	 * Busca todos os recortes de todos os serviços de recortes de acordo com suas configurações
	 * @param dtInicial
	 * @param dtFinal
	 * @return
	 */
	public static Result executeServices(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return executeServices(dtInicial, dtFinal, null);
	}
	
	/**
	 * Busca todos os recortes de todos os serviços de recortes de acordo com suas configurações
	 * @param dtInicial
	 * @param dtFinal
	 * @param connect
	 * @return
	 */
	public static Result executeServices(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			/*
			 * 1. Buscar serviços
			 * 2. Criar lista de busca (todos os serviços ativos e, caso o serviço seja por estado, todos os estados para este serviço)
			 * 3. Calcular período de busca
			 * 4. Iniciar as buscas
			 * 5. Gravar os recortes que nao existem no banco ainda (verificando por id)
			 * 6. Confirmar leitura de recortes (para serviços que exijam)
			 */
			
			//1. Buscar serviços
			ArrayList<ServicoRecorte> servicos = ServicoRecorteDAO.getList(connect);
			
			//2. Criar lista de busca (todos os serviços ativos e, caso o serviço seja por estado, todos os estados para este serviço)
			ResultSetMap rsmRetrieves = new ResultSetMap();
			ResultSetMap rsmEstados = EstadoServices.getAll(connect);

			int qtServicosInativos = 0;
			for (ServicoRecorte servicoRecorte : servicos) {
				
				if(servicoRecorte.getStServico()==0) { //servico inativo
					qtServicosInativos++;
					continue;
				}
				
				if(servicoRecorte.getLgEstado() == 1) { //servico por estado
					while(rsmEstados.next()) {
						HashMap<String, Object> register = new HashMap<>();
						
						Estado estado = EstadoDAO.get(rsmEstados.getInt("CD_ESTADO"), connect);
						
						register.put("SERVICO", servicoRecorte);
						register.put("DT_INICIAL", dtInicial);
						register.put("DT_FINAL", dtFinal);
						register.put("ESTADO", estado);
												
						rsmRetrieves.addRegister(register);
					}
					rsmEstados.beforeFirst();
				}
				else { //servico
					HashMap<String, Object> register = new HashMap<>();
					
					register.put("SERVICO", servicoRecorte);
					register.put("DT_INICIAL", dtInicial);
					register.put("DT_FINAL", dtFinal);
											
					rsmRetrieves.addRegister(register);
				}
			}
			
			LogUtils.info(servicos.size() + " serviço(s) encontrado(s)");
			LogUtils.info(qtServicosInativos + " serviço(s) inativo(s)");
			
			//3. Calcular período de busca
			if(dtInicial==null) {
				dtInicial = new GregorianCalendar();
				dtInicial.set(Calendar.HOUR_OF_DAY, 0);
				dtInicial.set(Calendar.MINUTE, 0);
				dtInicial.set(Calendar.SECOND, 0);
				dtInicial.set(Calendar.MILLISECOND, 0);
			}
			
			if(dtFinal==null) {
				dtFinal = new GregorianCalendar();
				dtFinal.set(Calendar.HOUR_OF_DAY, 23);
				dtFinal.set(Calendar.MINUTE, 59);
				dtFinal.set(Calendar.SECOND, 59);
				dtFinal.set(Calendar.MILLISECOND, 59);
			}
			
			LogUtils.info("Buscando para o período: " + 
					Util.formatDate(dtInicial, "dd/MM/yyyy HH:mm") + " a " + 
					Util.formatDate(dtFinal, "dd/MM/yyyy HH:mm"));
			
			LogUtils.debug(rsmRetrieves.size() + " buscas...");			
			Result result = new Result(1);

			while(rsmRetrieves.next()) {
				
				ServicoRecorte servico = (ServicoRecorte)rsmRetrieves.getObject("SERVICO");
				Result rExecute;
								
				LogUtils.info("");
				LogUtils.info("Buscando recortes para o serviço: "+servico.getNmServico());
				
				Estado estado = (Estado)rsmRetrieves.getObject("ESTADO");
				String txtSearch = rsmRetrieves.getString("TXT_SEARCH") != null ? rsmRetrieves.getString("TXT_SEARCH") : "";
				
				String nmTribunal = rsmRetrieves.getString("NM_TRIBUNAL") != null ? rsmRetrieves.getString("NM_TRIBUNAL") : "";
				
				if(estado!=null)
					LogUtils.debug("Estado: "+estado.getNmEstado());
				
				//4. Iniciar as buscas
				switch (servico.getTpServico()) {
					case ServicoRecorteServices.TP_TLEGAL:
						rExecute = executeTLegal(servico, dtInicial, dtFinal, estado, txtSearch);
						if(rExecute.getCode()<=0)// && estado!=null)
							continue;
						
						break;
						
					case ServicoRecorteServices.TP_ALERTE:
						rExecute = new Result(-1, "Tipo de Serviço ainda não implementado. ["+servico.getNmServico()+"]");
						break;	
					
					case ServicoRecorteServices.TP_SERCORTES:
						rExecute = new Result(-1, "Tipo de Serviço ainda não implementado. ["+servico.getNmServico()+"]");
						break;	
					
					case ServicoRecorteServices.TP_KURIER:
						rExecute = executeKurier(servico);
						if(rExecute.getCode()<=0)
							rExecute = new Result(-1, "Tipo de Serviço ainda não implementado. ["+servico.getNmServico()+"]");
						break;
						
					case ServicoRecorteServices.TP_KURIER_PERSONALIZADO:
						rExecute = executeKurierPersonalizado(servico, dtInicial, dtFinal, estado, "", nmTribunal);
						if(rExecute.getCode()<=0)
							rExecute = new Result(-1, "Tipo de Serviço ainda não implementado. ["+servico.getNmServico()+"]");
						break;
						
					case ServicoRecorteServices.TP_KURIER_SOAP:
						
						/** O serviço da Kurier retorna apenas 50 recortes por consulta, sendo necessário várias consultas.
						 *  O fluxo é desviado para executeKurierSOAP 
						 *  
						 *  XGH feelings 
						 */
						Result rKurierSOAP = executeKurierSOAP(servico);
						if(rKurierSOAP.getCode()<=0) {
							rExecute = rKurierSOAP;
						}
						 
						rExecute = new Result(1, "", "RSM_RECORTES", new ResultSetMap());
						break;
						
					case ServicoRecorteServices.TP_PUBLICACOES_ONLINE:
						GregorianCalendar ontem = new GregorianCalendar();
//						ontem.add(Calendar.DAY_OF_MONTH, -1);
						ontem.set(Calendar.DAY_OF_MONTH, ontem.get(Calendar.DAY_OF_MONTH)-1);
//						if(new GregorianCalendar().get(Calendar.HOUR_OF_DAY) < 18) {
							Result rAux = executePublicacoesOnline(servico, ontem, ontem);
							System.out.println(rAux);
//						}
						rExecute = executePublicacoesOnline(servico, dtInicial, dtFinal);
						break;
					
					default:
						rExecute = new Result(-2, "Tipo de Serviço não existe. ["+servico.getNmServico()+"]");
						break;
				}
				
				
				if(rExecute.getCode()<=0) {
					if(isConnectionNull)
						Conexao.rollback(connect);
					return rExecute;
				}

				LogUtils.debug(rExecute.getMessage());
				
				// 5. Gravar os recortes que nao existem no banco ainda (verificando por id)
				ResultSetMap rsmRecortes = (ResultSetMap)rExecute.getObjects().get("RSM_RECORTES");
				result = persistRecortes(rsmRecortes, servico, connect);
				
				// LOG ---------------
				ServicoRecorteOcorrenciaServices
						.save(new ServicoRecorteOcorrencia(0, 
								servico.getCdServico(), 
								new GregorianCalendar(), 
								(result.getCode()>0 ? ServicoRecorteOcorrenciaServices.TP_SUCESSO : ServicoRecorteOcorrenciaServices.TP_ERRO), 
								result.getCode(), 
								result.getMessage(), 
								result.toString(), 
								0, 0), 
						null, connect);
				// -------------------
				
				// 6. Confirmar leitura
				if(result.getCode()>0 && servico.getLgConfirmarLeitura()==1) {
					switch (servico.getTpServico()) {
						case ServicoRecorteServices.TP_PUBLICACOES_ONLINE:
							Result resultConfirm = confirmRetrievePublicacoesOnlineJson(rsmRecortes, servico);
							if(resultConfirm!=null) {
								LogUtils.info("Confirmando leitura em PublicaçõesOnline: "+resultConfirm.getMessage());
							}
							break;
					}
				}
				
				if(result.getCode()<=0) {
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
			}
//			
//			if(isConnectionNull) {
//				System.out.println("ServicoRecorteServices.executeServices.commit");
//				connect.commit();
//			}
			
			System.out.println("ServicoRecorteServices.executeServices.commit");
			connect.commit();
			
			return new Result(1, "Serviços de recortes executados com sucesso.");
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteServices.executeServices: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	/*
	 * METODOS POR SERVICO
	 * 	execute
	 * 	retrieve
	 *  process
	 */
	
	//KURIER PERSONALIZADO
		
	/**
	 * Executa um servico de recortes personalizado do tipo 'Kurier'
	 * @param servico
	 * @param dtInicial
	 * @param dtFinal
	 * @param cdEstado
	 * @param keyword
	 * @return
	 */
	private static Result executeKurierPersonalizado(ServicoRecorte servico, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Estado estado, String keyword, String nmTribunal) {
		try {
			
			Result r = retrieveKurierPersonalizado(servico, dtInicial, dtFinal, estado, keyword, nmTribunal);
			
			if(r.getCode()>0) {
				
				String content = (String)r.getObjects().get("CONTENT");
								
				if(content == null || content.equals(""))
					return new Result(-1, "Erro ao executar o serviço de recortes do tipo 'Kurier'. Conteúdo vazio ou nulo.");
				else {
					r = processKurierPersonalizado(servico, content);
				}
			}
			else {
				LogUtils.debug(r.getMessage());
			}	
			
			return r;
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao executar o serviço de recortes do tipo 'Kurier'.");
		}
	}
	/**
	 * Busca conteúdo no serviço de recortes personalizado do tipo 'Kurier'
	 * @param servico
	 * @param dtInicial
	 * @param dtFinal
	 * @param sgEstado
	 * @param keyword
	 * @return
	 */
	private static Result retrieveKurierPersonalizado(ServicoRecorte servico, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Estado estado, String keyword, String nmTribunal) {
		try {
			
			String dsDtInicial = com.tivic.manager.util.Util.formatDate(dtInicial, "dd/MM/yyyy");
			String dsDtFinal = com.tivic.manager.util.Util.formatDate(dtFinal, "dd/MM/yyyy");
			
			String sgEstado = "";
			if(estado!=null)
				sgEstado = estado.getSgEstado().toUpperCase();
			
			HashMap<String, String> placeholdersMap = new HashMap<>();
			placeholdersMap.put("${URL}", servico.getDsUrl());
			placeholdersMap.put("${ID_CLIENTE}", servico.getIdCliente());
			placeholdersMap.put("${NM_SENHA}", servico.getNmSenha());
			placeholdersMap.put("${ID_TOKEN}", servico.getIdToken());
			placeholdersMap.put("${TXT_SEARCH}", keyword);
			placeholdersMap.put("${SG_ESTADO}", sgEstado);
			placeholdersMap.put("${DT_INICIAL}", dsDtInicial);
			placeholdersMap.put("${DT_FINAL}", dsDtFinal);
			placeholdersMap.put("${NM_TRIBUNAL}", nmTribunal);
			
			Result r = retrieveServicoRecortesBasicAuthentication(servico, placeholdersMap);
			
			if(r.getCode()==-1) {
				
				/**
				 * VALIDAR MENSAGENS PERSONALIZADAS
				
				String message = (String)r.getObjects().get("CONTENT");
				
				if(message.indexOf("mensagem personalizada")!=-1) {
					return new Result(-2, "Serviço em bloqueio temporário.");
				}
				**/
			}
			
			return r;
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao recuperar conteudo do servico de recortes!");
		}
	}

	/**
	 * Processa o conteúdo do serviço de recortes personalizado do tipo 'Kurier'
	 * @param servico
	 * @param content
	 * @return
	 */
	private static Result processKurierPersonalizado(ServicoRecorte servico, String content) {
		try {
		    
			ResultSetMap rsmRecortes = new ResultSetMap();
		    
			JSONArray array  = new JSONArray(content);
		    
			for(int i = 0; i<array.length(); i++){				
				JSONObject jOutput = array.getJSONObject(i);
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				
				register.put("TXT_RECORTE", (String)jOutput.toString());
				
				if(jOutput.has("N_RECORTE") && !jOutput.get("N_RECORTE").equals(null)) {
					register.put("ID_RECORTE", jOutput.get("N_RECORTE"));
					
					Recorte recorte = RecorteServices.getByIdRecorte(String.valueOf(register.get("ID_RECORTE")));
					
					register.put("ST_RECORTE", (recorte != null) ? recorte.getStRecorte() : 0);
				}

				if(jOutput.has("DATA_PUBLICACAO") && !jOutput.get("DATA_PUBLICACAO").equals(null)) {
					register.put("DS_DT_PUBLICACAO", (String)jOutput.get("DATA_PUBLICACAO"));
					
					GregorianCalendar dtPublicacao = com.tivic.manager.util.Util.convStringToCalendar((String)jOutput.get("DATA_PUBLICACAO"));
					register.put("DT_PUBLICACAO", dtPublicacao);
				}
				
				if(jOutput.has("DATA_DIVULGACAO") && !jOutput.get("DATA_DIVULGACAO").equals(null)) {
					register.put("DS_DT_DIVULGACAO", (String)jOutput.get("DATA_DIVULGACAO"));
					
					GregorianCalendar dtDivulgacao = com.tivic.manager.util.Util.convStringToCalendar((String)jOutput.get("DATA_DIVULGACAO"));
					register.put("DT_DIVULGACAO", dtDivulgacao);
				}
				
				if(jOutput.has("NUMERO_PROCESSO") && !jOutput.get("NUMERO_PROCESSO").equals(null)) {
					register.put("NR_PROCESSO", (String)jOutput.get("NUMERO_PROCESSO"));
				}
				
				if(jOutput.has("ESTADO") && !jOutput.get("ESTADO").equals(null)) {
					register.put("SG_ESTADO", (String)jOutput.get("ESTADO"));
				}
				
				if(jOutput.has("JORNAL") && !jOutput.get("JORNAL").equals(null)) {
					register.put("NM_DIARIO", (String)jOutput.get("JORNAL"));
				}
				
				if(jOutput.has("PAGINA") && !jOutput.get("PAGINA").equals(null)) {
					register.put("NR_PAGINA", (String)jOutput.get("PAGINA"));
				}

				if(jOutput.has("ORGAO_TRIBUNAL") && !jOutput.get("ORGAO_TRIBUNAL").equals(null)) {
					register.put("NM_ORGAO", (String)jOutput.get("ORGAO_TRIBUNAL"));
				}

				if(jOutput.has("JUIZO_SECRETARIA") && !jOutput.get("JUIZO_SECRETARIA").equals(null)) {
					register.put("NM_JUIZO", (String)jOutput.get("JUIZO_SECRETARIA"));
				}
				
				if(jOutput.has("AUTOR") && !jOutput.get("AUTOR").equals(null)) {
					register.put("NM_AUTOR", (String)jOutput.get("AUTOR"));
				}
				
				if(jOutput.has("REU") && !jOutput.get("REU").equals(null)) {
					register.put("NM_REU", (String)jOutput.get("REU"));
				}
				
				if(jOutput.has("PUBLICACAO") && !jOutput.get("PUBLICACAO").equals(null)) {
					register.put("TXT_ANDAMENTO", (String)jOutput.get("PUBLICACAO"));
					
					processAndamento((String)register.get("TXT_ANDAMENTO"), register);
				}
	    		
	    		rsmRecortes.addRegister(register);
			}		    
			
		    return new Result(1, rsmRecortes.size() + " recorte(s) encontrado(s)", "RSM_RECORTES", rsmRecortes);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao processar recortes!");
		} 
	}
	
	//KURIER

	/**
	 * Executa um servico de recortes do tipo 'Kurier'
	 * @param servico
	 * @param dtInicial
	 * @param dtFinal
	 * @param cdEstado
	 * @param keyword
	 * @return
	 */
	private static Result executeKurier(ServicoRecorte servico) {
		try {
			
			Result r = retrieveKurier(servico);
			
			if(r.getCode()>0) {
				
				String content = (String)r.getObjects().get("CONTENT");
								
				if(content == null || content.equals(""))
					return new Result(-1, "Erro ao executar o serviço de recortes do tipo 'Kurier'. Conteúdo vazio ou nulo.");
				else {
					r = processKurier(servico, content);
				}
			}
			else {
				LogUtils.debug(r.getMessage());
			}	
			
			return r;
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao executar o serviço de recortes do tipo 'Kurier'.");
		}
	}
	/**
	 * Busca conteúdo no serviço de recortes do tipo 'Kurier'
	 * @param servico
	 * @param dtInicial
	 * @param dtFinal
	 * @param sgEstado
	 * @param keyword
	 * @return
	 */
	private static Result retrieveKurier(ServicoRecorte servico) {
		try {
			
			HashMap<String, String> placeholdersMap = new HashMap<>();
			placeholdersMap.put("${URL}", servico.getDsUrl());
			placeholdersMap.put("${ID_CLIENTE}", servico.getIdCliente());
			placeholdersMap.put("${NM_SENHA}", servico.getNmSenha());
			
			Result r = retrieveServicoRecortesBasicAuthentication(servico, placeholdersMap);
			
			if(r.getCode()==-1) {
				
				/**
				 * VALIDAR MENSAGENS PERSONALIZADAS
				
				String message = (String)r.getObjects().get("CONTENT");
				
				if(message.indexOf("mensagem personalizada")!=-1) {
					return new Result(-2, "Serviço em bloqueio temporário.");
				}
				**/
			}
			
			return r;
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao recuperar conteudo do servico de recortes!");
		}
	}

	/**
	 * Processa o conteúdo do serviço de recortes do tipo 'Kurier'
	 * @param servico
	 * @param content
	 * @return
	 */
	private static Result processKurier(ServicoRecorte servico, String content) {
		try {
		    
			ResultSetMap rsmRecortes = new ResultSetMap();
		    
			JSONArray array  = new JSONArray(content);
		    
			for(int i = 0; i<array.length(); i++){				
				JSONObject jOutput = array.getJSONObject(i);
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				
				register.put("TXT_RECORTE", (String)jOutput.toString());
				
				if(jOutput.has("DataDiario") && !jOutput.get("DataDiario").equals(null)) {
					register.put("DS_DT_PUBLICACAO", (String)jOutput.get("DataDiario"));
					GregorianCalendar dtPublicacao = com.tivic.manager.util.Util.convStringToCalendar((String)jOutput.get("DataDiario"));
					register.put("DT_PUBLICACAO", dtPublicacao);
				}
				
				if(jOutput.has("IdProcesso") && !jOutput.get("IdProcesso").equals(null)) {
					register.put("ID_PROCESSO", jOutput.get("IdProcesso"));
					register.put("ID_RECORTE", ""+register.get("ID_PROCESSO")+(String)register.get("DS_DT_PUBLICACAO"));
					
					Recorte recorte = RecorteServices.getByIdRecorte((String)register.get("ID_RECORTE"));
					
					register.put("ST_RECORTE", (recorte != null) ? recorte.getStRecorte() : 0);
				}
				
				if(jOutput.has("Processo") && !jOutput.get("Processo").equals(null)) {
					register.put("NR_PROCESSO", (String)jOutput.get("Processo"));
				}
				
				if(jOutput.has("Estado") && !jOutput.get("Estado").equals(null)) {
					register.put("SG_ESTADO", (String)jOutput.get("Estado"));
				}
				
				if(jOutput.has("Diario") && !jOutput.get("Diario").equals(null)) {
					register.put("NM_DIARIO", (String)jOutput.get("Diario"));
				}
				
				if(jOutput.has("Pagina") && !jOutput.get("Pagina").equals(null)) {
					register.put("NR_PAGINA", (String)jOutput.get("Pagina"));
				}

				if(jOutput.has("Justica") && !jOutput.get("Justica").equals(null)) {
					register.put("NM_ORGAO", (String)jOutput.get("Justica"));
				}

				if(jOutput.has("Forum") && !jOutput.get("Forum").equals(null)) {
					register.put("NM_JUIZO", (String)jOutput.get("Forum"));
				}
				
				if(jOutput.has("Vara") && !jOutput.get("Vara").equals(null)) {
					register.put("NM_JUIZO", (register.get("NM_JUIZO")==null || ((String)register.get("NM_JUIZO")).equals("")) ? 
												(String)jOutput.get("Vara") : 
												register.get("NM_JUIZO") + " - " + (String)jOutput.get("Vara"));
				}
				
				if(jOutput.has("Texto") && !jOutput.get("Texto").equals(null)) {
					register.put("TXT_ANDAMENTO", (String)jOutput.get("Texto"));
					
					processAndamento((String)register.get("TXT_ANDAMENTO"), register);
				}
	    		
	    		rsmRecortes.addRegister(register);
			}		    
			
		    return new Result(1, rsmRecortes.size() + " recorte(s) encontrado(s)", "RSM_RECORTES", rsmRecortes);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao processar recortes!");
		} 
	}
	
	/**
	 * Executa um servico de recortes do tipo 'Kurier'
	 * @param servico
	 * @param dtInicial
	 * @param dtFinal
	 * @param cdEstado
	 * @param keyword
	 * 
	 * @category kurier
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Result executeKurierSOAP(ServicoRecorte servico) {
		try {
			//1. busca
			//2. processa
			//3. salva no bd
			// repete até 1. não retornar nada
			
			int countTimeout = 0;
			while(true) {
				Connection connect = Conexao.conectar();
				connect.setAutoCommit(false);
				
				Result r = retrieveKurierSOAP(servico);
				
				if(r.getCode()>0) {
					NodeList content = (NodeList)r.getObjects().get("CONTENT");
					Node innerContent = content.item(0).getChildNodes().item(0).getFirstChild();	
					
					if(content == null || content.getLength()==0) // || innerContent==null || innerContent.getTextContent()==null)
						return new Result(-2, "Erro ao executar o serviço de recortes do tipo 'Kurier SOAP'. Conteúdo vazio ou nulo.");
					else if(innerContent==null || (innerContent.getTextContent()==null || innerContent.getTextContent().equalsIgnoreCase(""))) 
						break;
					else if(countTimeout>=servico.getVlTimeout())
						break;
					else {
						r = processKurierSOAP(servico, content);
						if(r.getCode()<0) {
							connect.rollback();
							return r;
						}
						
						r = persistRecortes((ResultSetMap)r.getObjects().get("RSM_RECORTES"), servico, connect);
						if(r.getCode()<0) {
							connect.rollback();
							return r;
						}
						
						connect.commit();
						Conexao.desconectar(connect);
						
						if(servico.getLgConfirmarLeitura()==1) {
							atualizarPublicacoesRecebidaKurier((ArrayList<Recorte>)r.getObjects().get("RECORTES"), servico);
							atualizarPublicacoesRecebidaKurier((ArrayList<Recorte>)r.getObjects().get("RECORTES_EXISTENTES"), servico);
							atualizarPublicacoesRecebidaKurier((ArrayList<Recorte>)r.getObjects().get("RECORTES_DUPLICADOS"), servico);
						}
					}
				}
				else {
					LogUtils.debug(r.getMessage());
				}
				
				countTimeout++;
			}
			
			return new Result(1, "Serviço executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao executar o serviço de recortes do tipo 'Kurier'.");
		}
	}
	
	/**
	 * Busca conteúdo no serviço de recortes do tipo 'Kurier'
	 * @param servico
	 * @param dtInicial
	 * @param dtFinal
	 * @param sgEstado
	 * @param keyword
	 * 
	 * @category kurier
	 * 
	 * @return
	 */
	private static Result retrieveKurierSOAP(ServicoRecorte servico) {
		
		try {
			String ns 	 = "soap";
	        String nsURI = "http://tempuri.org/";
	        
	        /*
	         * REQUEST
	         */
			// conexão SOAP
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            
            //mensagem SOAP
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapRequest = messageFactory.createMessage();

            // envelope SOAP 
            SOAPPart soapPart = soapRequest.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration(ns, nsURI);

            //envelope.getHeader().addChildElement("InserirProcessos", null, nsURI);
            
            MimeHeaders headers = soapRequest.getMimeHeaders();
            headers.addHeader("SOAPAction", nsURI + "RecuperarPublicacoes");
            
            // SOAP Body
            SOAPBody soapBody = envelope.getBody();
            
            // ação
            SOAPElement soapAction = soapBody.addChildElement("RecuperarPublicacoes", null, nsURI);
            
            // autenticação
            SOAPElement soapLogin = soapAction.addChildElement("login");
            soapLogin.addTextNode(servico.getIdCliente());
            SOAPElement soapSenha = soapAction.addChildElement("senha");
            soapSenha.addTextNode(servico.getNmSenha());
			
            /*
             * RESPONSE
             */
            SOAPMessage soapResponse = soapConnection.call(soapRequest, servico.getDsUrl());
            
            if(soapResponse.getSOAPBody().hasFault()) { //fault
            	return new Result(-2, "Erro ao buscar recortes da Kurier.\n"+soapResponse.getSOAPBody().getFault().getFaultString());
            }
            
            NodeList bodyContent = soapResponse.getSOAPBody().getElementsByTagName("RecuperarPublicacoesResponse");
//            NodeList innerContent = bodyContent.item(0).getChildNodes();
			
			return new Result(1, "", "CONTENT", bodyContent);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao inserir processo na base da Kurier!");
		} 
	}

	/**
	 * Processa o conteúdo do serviço de recortes do tipo 'Kurier SOAP'
	 * @param servico
	 * @param content
	 * 
	 * @category kurier
	 * 
	 * @return
	 */
	private static Result processKurierSOAP(ServicoRecorte servico, NodeList content) {
		try {
		    
			ResultSetMap rsmRecortes = new ResultSetMap();
			SimpleDateFormat sdf = null;
			NodeList innerContent = content.item(0).getChildNodes(); //RecuperarPublicacoesResult
            for (int i=0; i<innerContent.getLength(); i++) {
            	NodeList listaPublicacao = innerContent.item(i).getChildNodes(); //CPublicacaoResult
            	for(int j=0; j<listaPublicacao.getLength(); j++) {

                	HashMap<String, Object> register = new HashMap<>();
            		NodeList publicacao = listaPublicacao.item(j).getChildNodes();
            		
            		String txtRecorte = "";
            		
            		for(int k=0; k<publicacao.getLength(); k++) {
            			
            			txtRecorte += "\""+publicacao.item(k).getNodeName()+"\":\""+publicacao.item(k).getTextContent()+"\"";
            			if(k+1<publicacao.getLength())
            				txtRecorte += ",";
            			
            			switch(publicacao.item(k).getNodeName()) {
		            		case "IdProcesso":
		            			if(publicacao.item(k).getTextContent()!=null || !publicacao.item(k).getTextContent().equals(""))
		            				register.put("ID_PROCESSO_RECORTE", publicacao.item(k).getTextContent());
		            				if(ProcessoDAO.get(Integer.parseInt(publicacao.item(k).getTextContent()))!=null) 
		            					register.put("CD_PROCESSO", Integer.parseInt(publicacao.item(k).getTextContent()));
		            				else 
	            						LogUtils.debug("\n\n==============================\n"
	            									 + "\tprocessKurierSOAP> cdProcesso:"+publicacao.item(k).getTextContent()+" não existe na base.");
		            			break;
		            		case "NumeroProcesso":
		            			if(publicacao.item(k).getTextContent()!=null && !publicacao.item(k).getTextContent().equals(""))
		            				register.put("NR_PROCESSO", publicacao.item(k).getTextContent());
		            			break;
		            		case "Cliente":
		            			register.put("NM_CLIENTE", publicacao.item(k).getTextContent());
		            			break;
		            		case "ParteContraria":
		            			register.put("NM_ADVERSO", publicacao.item(k).getTextContent());
		            			break;
		            		case "GrupoOperacional":
		            			break;
		            		case "DataPublicacao":
		            			try {
		            				register.put("DS_DT_PUBLICACAO", publicacao.item(k).getTextContent());
			            			
			            			sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss 'A'");
			            			GregorianCalendar dtPublicacao = Util.dateToCalendar(sdf.parse(publicacao.item(k).getTextContent()));
			            			register.put("DT_PUBLICACAO", dtPublicacao);
		            			} catch(java.text.ParseException e) {
		            				System.out.println("\t\t\tPARSER ERROR: "+publicacao.item(k).getTextContent());
		            			}
		            			
		            			break;
		            		case "Fonte":
		            			register.put("NM_DIARIO", publicacao.item(k).getTextContent());
		            			break;
		            		case "TeorPublicacao":
		            			register.put("TXT_ANDAMENTO", publicacao.item(k).getTextContent());
		            			break;
		            		case "DataDisponibilizacaoKurier":
		            			try {
		            				register.put("DS_DT_DIVULGACAO", publicacao.item(k).getTextContent());
			            			
			            			sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			            			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			            			GregorianCalendar dtDivulgacao = Util.dateToCalendar(sdf.parse(publicacao.item(k).getTextContent()));
			            			register.put("DT_DIVULGACAO", dtDivulgacao);
		            			} catch(java.text.ParseException e) {
		            				System.out.println("\t\t\tPARSE ERROR: "+publicacao.item(k).getTextContent());
		            			}
		            			break;
		            		case "TipoPesquisa":
		            			break;
		            		case "IndicadorExito":
		            			break;
		            		case "CodigoPublicacao":
		            			register.put("ID_RECORTE", publicacao.item(k).getTextContent());
		            			
		            			Recorte recorte = RecorteServices.getByIdRecorte(String.valueOf(register.get("ID_RECORTE")));
		            			
		            			register.put("ST_RECORTE", (recorte != null) ? recorte.getStRecorte() : 0);
		            			
		            			break;
		            		case "Relacionamento":
		            			break;
            			}
	            		
            		}

            		register.put("TXT_RECORTE", "{"+txtRecorte+"}");
            		processAndamento((String)register.get("TXT_ANDAMENTO"), register);
                	rsmRecortes.addRegister(register);
            	}
            	
    			
            }
			
		    return new Result(1, rsmRecortes.size() + " recorte(s) encontrado(s)", "RSM_RECORTES", rsmRecortes);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao processar recortes!");
		} 
	}
	
	private static Result persistRecortes(ResultSetMap rsmRecortes, ServicoRecorte servico, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			
			ArrayList<Recorte> recortes = new ArrayList<>();
			ArrayList<Recorte> recortesExistentes = new ArrayList<>();
			ArrayList<Recorte> recortesDuplicados = new ArrayList<>();
			
			while(rsmRecortes.next()) {
				boolean lgDuplicado = false;
				
				Recorte recorte = RecorteServices.getByIdRecorte(rsmRecortes.getString("ID_RECORTE"), servico.getCdServico(), connect);
								
				if(recorte==null) {
					for (Recorte r : recortes) {
						if(r.getIdRecorte().equals(rsmRecortes.getString("ID_RECORTE"))) {
							recorte = r;
							lgDuplicado = true;
							break;
						}
					}
				}
				
				if(recorte==null) {
					recorte = new Recorte();
				
					String nmDiario = rsmRecortes.getString("NM_DIARIO") != null ? Util.resumir(rsmRecortes.getString("NM_DIARIO"), 256) : "";
					String nrPagina = rsmRecortes.getString("NR_PAGINA") != null ? Util.resumir(rsmRecortes.getString("NR_PAGINA"), 256) : "";
					String nmOrgao = rsmRecortes.getString("NM_ORGAO") != null ? Util.resumir(rsmRecortes.getString("NM_ORGAO"), 256) : "";
					String nmJuizo = rsmRecortes.getString("NM_JUIZO") != null ? Util.resumir(rsmRecortes.getString("NM_JUIZO"), 256) : "";
					
					Estado estado = null;
					if(rsmRecortes.getString("SG_ESTADO")!=null && !rsmRecortes.getString("SG_ESTADO").equals(""))
						estado = EstadoServices.getBySg(rsmRecortes.getString("SG_ESTADO"), connect);
					
					recorte.setCdRecorte(0);
					recorte.setIdRecorte(rsmRecortes.getString("ID_RECORTE"));
					recorte.setTxtRecorte(rsmRecortes.getString("TXT_RECORTE"));
					recorte.setCdProcesso(rsmRecortes.getInt("CD_PROCESSO"));
					recorte.setDtBusca(new GregorianCalendar());
					recorte.setStRecorte(rsmRecortes.getInt("ST_RECORTE")==RecorteServices.ST_COM_ALERTA ? rsmRecortes.getInt("ST_RECORTE") : RecorteServices.ST_NAO_PROCESSADO);						
					recorte.setCdServico(servico.getCdServico());
					recorte.setDtPublicacao((GregorianCalendar)rsmRecortes.getObject("DT_PUBLICACAO"));
					recorte.setDtProcessamento((GregorianCalendar)rsmRecortes.getObject("DT_DIVULGACAO"));
					
					recorte.setNmDiario(nmDiario);
					recorte.setNrPagina(nrPagina);
					recorte.setNmOrgao(nmOrgao);
					recorte.setNmJuizo(nmJuizo);
					
					recorte.setTxtAndamento(rsmRecortes.getString("TXT_ANDAMENTO"));
					recorte.setCdEstado(estado != null ? estado.getCdEstado() : rsmRecortes.getInt("CD_ESTADO", 0));
					
					recorte.setIdProcessoRecorte(rsmRecortes.getString("ID_PROCESSO_RECORTE"));
					
					recortes.add(recorte);
				}
				else {
					if(lgDuplicado)
						recortesDuplicados.add(recorte);
					else
						recortesExistentes.add(recorte);
				}
			}
				
			servico.setDtUltimaBusca(new GregorianCalendar());
			save(servico, null, connect);
			
			
			LogUtils.info("");
			LogUtils.info((recortes.size()+recortesExistentes.size()+recortesDuplicados.size())+" recorte(s) encontrado(s)");
			LogUtils.info(recortesExistentes.size() + " recorte(s) existe(m) na base de dados");
			LogUtils.info(recortesDuplicados.size() + " recorte(s) duplicado(s) enviado(s) nas buscas");
			LogUtils.info(recortes.size() + " recorte(s) novo(s)");
			
			
			//5. Gravar os recortes que nao existem no banco ainda (verificando por id)
			Result rSave = new Result(1);
			for (Recorte recorte : recortes) {
				rSave = RecorteServices.save(recorte, null, connect);
				
				if(rSave.getCode()<=0)
					break;
			}
			
			if(rSave.getCode()>0) {
				if (isConnectionNull)
					connect.commit();
				
				LogUtils.info(recortes.size() + " recorte(s) gravados com sucesso.");
				
				Result rFinal = new Result(1, "Serviços de recorte executados.\n"+recortes.size()+" novos recortes aguardando processamento.");
				
				rFinal.addObject("RECORTES", recortes);
				rFinal.addObject("RECORTES_EXISTENTES", recortesExistentes);
				rFinal.addObject("RECORTES_DUPLICADOS", recortesDuplicados);
				
				return rFinal;
			}
			else {
				if (isConnectionNull)
					connect.rollback();
				
				LogUtils.info("Erro ao gravar recorte(s).");
				LogUtils.debug(rSave.getMessage());
				
				return rSave;
			}
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteServices.executeServices: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Insere processo(s) na base da kurier, via SOAP
	 *
	 * @param cdProcesso {@link Integer}
	 * @param rsmProcessos {@link ResultSetMap}
	 * @param servico {@link ServicoRecorte}
	 * @return {@link Result}
	 * 
	 * @category kurier
	 * 
	 * @author mauricio
	 */
	public static Result inserirProcessosKurier(ResultSetMap rsmProcessos) {
		return inserirProcessosKurier(rsmProcessos, ServicoRecorteDAO.get(8));
	}
	
	public static Result inserirProcessosKurier(int cdProcesso) {
		return inserirProcessosKurier(cdProcesso, null);
	}
	
	public static Result inserirProcessosKurier(int cdProcesso, Connection connection) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.CD_PROCESSO", Integer.toString(cdProcesso), ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsmProcessos = ProcessoServices.find(criterios, null, connection);

		// SERVIÇO DE RECORTES
		criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("TP_SERVICO", Integer.toString(ServicoRecorteServices.TP_KURIER_SOAP), ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsmServico = ServicoRecorteServices.find(criterios, connection);
		ServicoRecorte servico = null;
		if(rsmServico.next()) {
			servico = new ServicoRecorte(
					rsmServico.getInt("cd_servico"), 
					rsmServico.getString("nm_servico"), 
					rsmServico.getInt("tp_servico"), 
					rsmServico.getString("id_servico"), 
					rsmServico.getString("id_cliente"), 
					rsmServico.getString("nm_senha"), 
					rsmServico.getString("ds_url"), 
					rsmServico.getString("txt_formato"), 
					rsmServico.getInt("vl_timeout"), 
					rsmServico.getGregorianCalendar("dt_ultima_busca"), 
					rsmServico.getInt("lg_estado"), 
					rsmServico.getInt("st_servico"), 
					rsmServico.getInt("lg_confirmar_leitura")
				);
		}
		
		return inserirProcessosKurier(rsmProcessos, servico);
	}
	
	public static Result inserirProcessosKurier(ResultSetMap rsmProcessos, ServicoRecorte servico) {
		
		try {
			
			String ns 	 = "soap";
	        String nsURI = "http://tempuri.org/";
	        
	        /*
	         * REQUEST
	         */
			// conexão SOAP
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            
            //mensagem SOAP
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapRequest = messageFactory.createMessage();

            // envelope SOAP 
            SOAPPart soapPart = soapRequest.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration(ns, nsURI);

            //envelope.getHeader().addChildElement("InserirProcessos", null, nsURI);
            
            MimeHeaders headers = soapRequest.getMimeHeaders();
            headers.addHeader("SOAPAction", nsURI + "InserirProcessos");
            
            // SOAP Body
            SOAPBody soapBody = envelope.getBody();
            
            // ação
            SOAPElement soapAction = soapBody.addChildElement("InserirProcessos", null, nsURI);
            
            // autenticação
            SOAPElement soapLogin = soapAction.addChildElement("login");
            soapLogin.addTextNode(servico.getIdCliente());
            SOAPElement soapSenha = soapAction.addChildElement("senha");
            soapSenha.addTextNode(servico.getNmSenha());
            
            // processos
            SOAPElement soapProcessos = soapAction.addChildElement("processos");
            while(rsmProcessos.next()) {
            	if(rsmProcessos.getString("nr_processo")==null)
            		continue; 
            	
            	SOAPElement soapRegistro = soapProcessos.addChildElement("CRegistroProcesso");
            	
            	//idProcesso
            	SOAPElement idProcesso = soapRegistro.addChildElement("IdProcesso");
            	idProcesso.addTextNode(Util.fillNum(rsmProcessos.getInt("cd_processo"), 8));
            	
            	//NumeroProcesso
            	SOAPElement numeroProcesso = soapRegistro.addChildElement("NumeroProcesso");
            	numeroProcesso.addTextNode(rsmProcessos.getString("nr_processo"));
            	
            	//ParteCliente
            	SOAPElement parteCliente = soapRegistro.addChildElement("ParteCliente");
            	parteCliente.addTextNode(rsmProcessos.getString("nm_cliente1", ""));
            	
            	//ParteContraria
            	SOAPElement parteContraria = soapRegistro.addChildElement("ParteContraria");
            	parteContraria.addTextNode(rsmProcessos.getString("nm_adverso1", ""));
            	
            	//Uf
            	SOAPElement uf = soapRegistro.addChildElement("Uf");
            	uf.addTextNode(rsmProcessos.getString("sg_estado", ""));
            	
            	//AreaJustica
            	SOAPElement areaJustica = soapRegistro.addChildElement("AreaJustica");
            	areaJustica.addTextNode(rsmProcessos.getString("nm_area_direito", "").equalsIgnoreCase("TRABALHISTA") ? "TRABALHISTA" : "CIVEL");
            	
            	//GrupoOperacional
            	SOAPElement grupoOperacional = soapRegistro.addChildElement("GrupoOperacional");
            	grupoOperacional.addTextNode(rsmProcessos.getString("nm_grupo_trabalho", ""));
            	
            	System.out.print("|");
            }
//            System.out.println();
//            System.out.println("..:: REQUEST ::..");
//            soapRequest.writeTo(System.out);
            
            /*
             * RESPONSE
             */
            SOAPMessage soapResponse = soapConnection.call(soapRequest, servico.getDsUrl());
            
            if(soapResponse.getSOAPBody().hasFault()) {
            	return new Result(-2, "Erro ao inserir processos na base da Kurier.");
            }
            
            NodeList bodyContent = soapResponse.getSOAPBody().getElementsByTagName("InserirProcessosResponse");
            NodeList innerContent = bodyContent.item(0).getChildNodes();
            String nodeValue = innerContent.item(0).getTextContent();
            
//            NodeList bodyContent2 = soapResponse.getSOAPBody().getElementsByTagName("InserirProcessosResponse");
//            for (int i = 0; i < bodyContent.getLength(); i++) {
//                NodeList innerContent2 = bodyContent.item(i).getChildNodes();
//                for (int j = 0; j < innerContent.getLength(); j++) {
//                    System.out.println(innerContent.item(j).getNodeName());
//                    System.out.println(innerContent.item(j).getTextContent());
//                }
//            }
//            System.out.println("\n..:: RESPONSE: "+nodeValue);
                        
            return new Result(1, "Processos incluídos com sucesso!", "RESPONSE_VALUE", nodeValue);
			
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao inserir processo na base da Kurier!");
		} 
	}
	
	private static Result atualizarPublicacoesRecebidaKurier(ArrayList<Recorte> recortes, ServicoRecorte servico) {
		
		try {
			String ns 	 = "soap";
	        String nsURI = "http://tempuri.org/";
	        
	        /*
	         * REQUEST
	         */
			// conexão SOAP
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            
            //mensagem SOAP
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapRequest = messageFactory.createMessage();

            // envelope SOAP 
            SOAPPart soapPart = soapRequest.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration(ns, nsURI);

            MimeHeaders headers = soapRequest.getMimeHeaders();
            headers.addHeader("SOAPAction", nsURI + "AtualizarPublicacoesRecebida");
            
            // SOAP Body
            SOAPBody soapBody = envelope.getBody();
            
            // ação
            SOAPElement soapAction = soapBody.addChildElement("AtualizarPublicacoesRecebida", null, nsURI);
            
            // autenticação
            SOAPElement soapLogin = soapAction.addChildElement("login");
            soapLogin.addTextNode(servico.getIdCliente());
            SOAPElement soapSenha = soapAction.addChildElement("senha");
            soapSenha.addTextNode(servico.getNmSenha());
            
            // recortes
            SOAPElement soapRecortes = soapAction.addChildElement("publicacoesAtualizacao");
            for (Recorte recorte : recortes) {
            	
            	SOAPElement soapRegistro = soapRecortes.addChildElement("CAtualizacaoPublicacaoEnviada");
            	
            	//CodigoPublicacao
            	SOAPElement codigoPublicacao = soapRegistro.addChildElement("CodigoPublicacao");
            	codigoPublicacao.addTextNode(recorte.getIdRecorte());
            	
            	//IdProcesso
            	SOAPElement idProcesso = soapRegistro.addChildElement("IdProcesso");
            	String idAux = recorte.getIdProcessoRecorte();
            	
            	if(idAux==null && recorte.getCdProcesso()>0)
            		idAux = Util.fillNum(recorte.getCdProcesso(), 8);
            	
            	idProcesso.addTextNode(idAux);
            	            	
            }
            
            //soapRequest.writeTo(System.out);
			
            /*
             * RESPONSE
             */
            SOAPMessage soapResponse = soapConnection.call(soapRequest, servico.getDsUrl());
            
            if(soapResponse.getSOAPBody().hasFault()) { //fault
            	return new Result(-2, "Erro ao buscar atualizar lista de recortes da Kurier.\n"+soapResponse.getSOAPBody().getFault().getFaultString());
            }
            
            NodeList bodyContent = soapResponse.getSOAPBody().getElementsByTagName("AtualizarPublicacoesRecebidaResponse");
            NodeList innerContent = bodyContent.item(0).getChildNodes();
            String nodeValue = innerContent.item(0).getTextContent();
                        
			return new Result(nodeValue.equals("true")?1:-1, "", "CONTENT", bodyContent);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao atualizar lista de recortes na base da Kurier!");
		} 
	}
	
	
	//TLEGAL
	
	/**
	 * Executa um servico de recortes do tipo 'TLegal'
	 * @param servico
	 * @param dtInicial
	 * @param dtFinal
	 * @param cdEstado
	 * @param keyword
	 * @return
	 */
	private static Result executeTLegal(ServicoRecorte servico, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Estado estado, String keyword) {
		try {
						
			Result r = retrieveTLegal(servico, dtInicial, dtFinal, estado, keyword);
			
			if(r.getCode()>0) {
				
				String content = (String)r.getObjects().get("CONTENT");
				
				if(content == null || content.equals(""))
					return new Result(-1, "Erro ao executar o serviço de recortes do tipo 'T-Legal'. Conteúdo vazio ou nulo.");
				else {
					r = processTLegal(servico, content);
				}
			}
			else {
				LogUtils.debug(r.getMessage());
			}	
			
			return r;
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao executar o serviço de recortes do tipo 'T-Legal'.");
		}
	}
	/**
	 * Busca conteúdo no serviço de recortes do tipo 'TLegal'
	 * @param servico
	 * @param dtInicial
	 * @param dtFinal
	 * @param sgEstado
	 * @param keyword
	 * @return
	 */
	private static Result retrieveTLegal(ServicoRecorte servico, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Estado estado, String keyword) {
		try {
			String dsDtInicial = com.tivic.manager.util.Util.formatDate(dtInicial, "dd/MM/yyyy");
			String dsDtFinal = com.tivic.manager.util.Util.formatDate(dtFinal, "dd/MM/yyyy");
			
			String sgEstado = "";
			if(estado!=null)
				sgEstado = estado.getSgEstado().toUpperCase();
			
			HashMap<String, String> placeholdersMap = new HashMap<>();
			placeholdersMap.put("${URL}", servico.getDsUrl());
			placeholdersMap.put("${ID_CLIENTE}", servico.getIdCliente());
			placeholdersMap.put("${NM_SENHA}", servico.getNmSenha());
			placeholdersMap.put("${ID_TOKEN}", servico.getIdToken());
			placeholdersMap.put("${TXT_SEARCH}", keyword);
			placeholdersMap.put("${SG_ESTADO}", sgEstado);
			placeholdersMap.put("${DT_INICIAL}", dsDtInicial);
			placeholdersMap.put("${DT_FINAL}", dsDtFinal);
			
			Result r = retrieveServicoRecortesHttp(servico, placeholdersMap);
			
			if(r.getCode()==-1) {
				String message = (String)r.getObjects().get("CONTENT");
				
				if(message.indexOf("Consumo Indevido, aguarde o final do periodo de bloqueio")!=-1) {
					return new Result(-2, "Serviço em bloqueio temporário.");
				}
				
				if(message.indexOf("Nenhum registro atende a condicao")!=-1){
					return new Result(-3, "Nenhum registro encontrado. Verifique os campos de filtro e tente novamente.");
				}
			}
			
			return r;
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao recuperar conteudo do servico de recortes!");
		}
	}

	/**
	 * Processa o conteúdo do serviço de recortes do tipo 'TLegal'
	 * @param servico
	 * @param content
	 * @return
	 */
	private static Result processTLegal(ServicoRecorte servico, String content) {
		try {
		    
			ResultSetMap rsmRecortes = new ResultSetMap();
		    
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    
		    ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes("UTF-8"));
		    Document document = builder.parse(bais);
	
		    NodeList nodeList = document.getElementsByTagName("Table");
		    for (int i = 0; i < nodeList.getLength(); i++) {
		    	
		    	Node node = nodeList.item(i);
		    	
		    	
		    	if (node.getNodeType() == Node.ELEMENT_NODE) {
		    		
		    		HashMap<String, Object> register = new HashMap<String, Object>();
		    		
		    		/* XXX: 
		    		 * aparentemente, embora não reproduzido localmente, o 
		    		 * namespace 'diffgr' não foi declarado em algum ponto 
		    		 * do xml, disparando um erro.
		    		 * 
		    		 * em 14/12/2017
		    		 */
		    		register.put("TXT_RECORTE", node.getTextContent());//XmlServices.nodeToString(node));
		    		
		    		NodeList childNodes = node.getChildNodes();
		    		
		    		for (int j = 0; j < childNodes.getLength(); j++) {
		    			Node cNode = childNodes.item(j);
		    			
		    			if (cNode instanceof Element) {
		    				
		    				if(cNode.getFirstChild().getNodeType() == Node.TEXT_NODE) {
			    				
		    					String value = cNode.getFirstChild().getNodeValue();
			    				
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
			    					
			    					//System.out.println("\tvalue: "+value);
			    					value = StringEscapeUtils.unescapeXml(value);
			    					//System.out.println("\tvalueAfterUnescapeXml: "+value);
			    					
			    					register.put("TXT_ANDAMENTO", value);
			    					register.put("LG_PROCESSO", 1);
			    					
			    					processAndamento(value, register);
			    				} 
			    				else if(cNode.getNodeName().equals("CodigoRelacional")) {
			    					register.put("ID_RECORTE", value);
			    					
			    					Recorte recorte = RecorteServices.getByIdRecorte(value);
			    					
			    					register.put("ST_RECORTE", (recorte != null) ? recorte.getStRecorte() : 0);
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
		    
		    return new Result(1, rsmRecortes.size() + " recorte(s) encontrado(s)", "RSM_RECORTES", rsmRecortes);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao processar recortes!");
		} 
	}
	
	/*
	 * PUBLICAÇÕES ONLINE
	 */
	private static Result executePublicacoesOnline(ServicoRecorte servico, GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		try {		
			
			Result r = retrievePublicacoesOnline(servico, dtInicial, dtFinal);
			
			ServicoRecorteOcorrenciaServices
				.save(new ServicoRecorteOcorrencia(
						0, 
						servico.getCdServico(), 
						new GregorianCalendar(), 
						(r.getCode()<0 ? ServicoRecorteOcorrenciaServices.TP_ERRO : ServicoRecorteOcorrenciaServices.TP_SUCESSO), 
						r.getCode(), 
						r.getMessage(), 
						null, 0, 0));
			
			if(r.getCode()>0) {
				String content = (String)r.getObjects().get("CONTENT");
				if(content == null || content.equals(""))
					return new Result(-1, "Erro ao executar o serviço de recortes do tipo 'Publicações Online'. Conteúdo vazio ou nulo.");
				else
					r = processPublicacoesOnlineJson(servico, content);
			}
			else {
				LogUtils.debug(r.getMessage());
			}	
			
			return r;
		}
		catch(Exception e) {
			System.out.println("ERRO NO MÉTODO PRINCIPAL");
			e.printStackTrace();
			return new Result(-1, "Erro ao executar o serviço de recortes do tipo 'Publicações Online'.");
		}
	}
	
	private static Result retrievePublicacoesOnline(ServicoRecorte servico, GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		try {
			String dsDtInicial = com.tivic.manager.util.Util.formatDate(dtInicial, "yyyy-MM-dd");
			String dsDtFinal = com.tivic.manager.util.Util.formatDate(dtFinal, "yyyy-MM-dd");
			
			// FIX ---------------
			if(new GregorianCalendar().get(Calendar.HOUR_OF_DAY)%2==0 && new GregorianCalendar().get(Calendar.HOUR_OF_DAY)>12) {
				try {
					Result rFix = FixServices.fixNovoProcessoPublicacoesOnline(dsDtInicial);
					ServicoRecorteOcorrenciaServices
						.save(new ServicoRecorteOcorrencia(
								0/*cdOcorrencia*/, 
								servico.getCdServico(), 
								new GregorianCalendar()/*dtOcorrencia*/, 
								(rFix.getCode()<0 ? ServicoRecorteOcorrenciaServices.TP_ERRO : ServicoRecorteOcorrenciaServices.TP_SUCESSO)/*tpOcorrencia*/, 
								rFix.getCode(), 
								rFix.getMessage(), 
								(rFix.getObjects().get("JSON")!=null ? ((JSONObject)rFix.getObjects().get("JSON")).toString() : null), 
								0, 0));
				} catch (Exception e) {
					System.out.println("ERRO NO MÉTODO RETRIEVE");
					ServicoRecorteOcorrenciaServices
						.save(new ServicoRecorteOcorrencia(
								0/*cdOcorrencia*/, 
								servico.getCdServico(), 
								new GregorianCalendar()/*dtOcorrencia*/, 
								ServicoRecorteOcorrenciaServices.TP_ERRO/*tpOcorrencia*/, 
								e.hashCode()*(-1), 
								e.getMessage(), 
								null, 0, 0));
				}
			}
			// -------------------
			
			HashMap<String, String> placeholdersMap = new HashMap<>();
			placeholdersMap.put("${URL}", servico.getDsUrl());
			placeholdersMap.put("${ID_CLIENTE}", servico.getIdCliente());
			placeholdersMap.put("${NM_SENHA}", servico.getNmSenha());
			placeholdersMap.put("${ID_TOKEN}", servico.getIdToken());
			placeholdersMap.put("${DT_INICIAL}", dsDtInicial);
			placeholdersMap.put("${DT_FINAL}", dsDtFinal);
			
			Result r = retrieveServicoRecortesHttp(servico, placeholdersMap);
			if(r.getCode()==-1) {
				String message = (String)r.getObjects().get("CONTENT");
								
				if(message.indexOf("Consumo Indevido, aguarde o final do periodo de bloqueio")!=-1) {
					return new Result(-2, "Serviço em bloqueio temporário.");
				}
				
				if(message.indexOf("Nenhum registro atende a condicao")!=-1){
					return new Result(-3, "Nenhum registro encontrado. Verifique os campos de filtro e tente novamente.");
				}
			}
			
			return r;
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao recuperar conteudo do servico de recortes!");
		}
	}
	
	private static Result retrievePublicacoesOnlineDiaAnterior(ServicoRecorte servico, GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		try {
			String dsDtInicial = com.tivic.manager.util.Util.formatDate(dtInicial, "yyyy-MM-dd");
			String dsDtFinal = com.tivic.manager.util.Util.formatDate(dtFinal, "yyyy-MM-dd");

			
			// FIX ---------------
			if(new GregorianCalendar().get(Calendar.HOUR_OF_DAY)%2==0 && new GregorianCalendar().get(Calendar.HOUR_OF_DAY)>12) {
				try {
					Result rFix = FixServices.fixNovoProcessoPublicacoesOnline(dsDtInicial);
					ServicoRecorteOcorrenciaServices
						.save(new ServicoRecorteOcorrencia(
								0/*cdOcorrencia*/, 
								servico.getCdServico(), 
								new GregorianCalendar()/*dtOcorrencia*/, 
								(rFix.getCode()<0 ? ServicoRecorteOcorrenciaServices.TP_ERRO : ServicoRecorteOcorrenciaServices.TP_SUCESSO)/*tpOcorrencia*/, 
								rFix.getCode(), 
								rFix.getMessage(), 
								(rFix.getObjects().get("JSON")!=null ? ((JSONObject)rFix.getObjects().get("JSON")).toString() : null), 
								0, 0));
				} catch (Exception e) {
					System.out.println("ERRO MÉTODO NO DIA ANTERIOR");
					ServicoRecorteOcorrenciaServices
						.save(new ServicoRecorteOcorrencia(
								0/*cdOcorrencia*/, 
								servico.getCdServico(), 
								new GregorianCalendar()/*dtOcorrencia*/, 
								ServicoRecorteOcorrenciaServices.TP_ERRO/*tpOcorrencia*/, 
								e.hashCode()*(-1), 
								e.getMessage(), 
								null, 0, 0));
				}
			}
			// -------------------
			
			HashMap<String, String> placeholdersMap = new HashMap<>();
			placeholdersMap.put("${URL}", servico.getDsUrl());
			placeholdersMap.put("${ID_CLIENTE}", servico.getIdCliente());
			placeholdersMap.put("${NM_SENHA}", servico.getNmSenha());
			placeholdersMap.put("${ID_TOKEN}", servico.getIdToken());
			placeholdersMap.put("${DT_INICIAL}", dsDtInicial);
			placeholdersMap.put("${DT_FINAL}", dsDtFinal);
			
			Result r = retrieveServicoRecortesHttp(servico, placeholdersMap);
			if(r.getCode()==-1) {
				String message = (String)r.getObjects().get("CONTENT");
								
				if(message.indexOf("Consumo Indevido, aguarde o final do periodo de bloqueio")!=-1) {
					return new Result(-2, "Serviço em bloqueio temporário.");
				}
				
				if(message.indexOf("Nenhum registro atende a condicao")!=-1){
					return new Result(-3, "Nenhum registro encontrado. Verifique os campos de filtro e tente novamente.");
				}
			}
			
			return r;
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao recuperar conteudo do servico de recortes!");
		}
	}
	
	@Deprecated
	private static Result processPublicacoesOnlineXml(ServicoRecorte servico, String content) {
		try {
		    
			ResultSetMap rsmRecortes = new ResultSetMap();
			SimpleDateFormat sdf = null;
			
			//XXX:
			Files.write(Paths.get("./content.txt"), content.getBytes());
		    
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    
		    ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes("UTF-8"));
		    Document document = builder.parse(bais);
	
		    NodeList innerContent = document.getElementsByTagName("RecuperarPublicacoesResult");
		    
		    //XXX:
		    Files.write(Paths.get("./RecuperarPublicacoesResult.txt"), innerContent.item(0).toString().getBytes());
		    
		    for (int i=0; i<innerContent.getLength(); i++) {
            	NodeList listaPublicacao = innerContent.item(i).getChildNodes(); //CPublicacaoResult
            	
            	for(int j=0; j<listaPublicacao.getLength(); j++) {

                	HashMap<String, Object> register = new HashMap<>();
            		NodeList publicacao = listaPublicacao.item(j).getChildNodes();
            		            		
            		String txtRecorte = "";
            		
            		for(int k=0; k<publicacao.getLength(); k++) {
            			if(publicacao.item(k).getNodeName().equals("#text"))
            				continue;
            			
            			txtRecorte += "\""+publicacao.item(k).getNodeName()+"\":\""+publicacao.item(k).getTextContent()+"\"";
            			if(k+1<publicacao.getLength())
            				txtRecorte += ",";
            			            			
            			switch(publicacao.item(k).getNodeName()) {
		            		case "NumeroProcesso":
		            			if(publicacao.item(k).getTextContent()!=null && !publicacao.item(k).getTextContent().equals(""))
		            				register.put("NR_PROCESSO", publicacao.item(k).getTextContent());
		            			break;
		            		case "Cliente":
		            			register.put("NM_CLIENTE", publicacao.item(k).getTextContent());
		            			break;
		            		case "ParteContraria":
		            			register.put("NM_ADVERSO", publicacao.item(k).getTextContent());
		            			break;
		            		case "DataPublicacao":
		            			try {
		            				register.put("DS_DT_PUBLICACAO", publicacao.item(k).getTextContent());
			            			
			            			sdf = new SimpleDateFormat("dd/MM/yyyy");
			            			GregorianCalendar dtPublicacao = Util.dateToCalendar(sdf.parse(publicacao.item(k).getTextContent()));
			            			register.put("DT_PUBLICACAO", dtPublicacao);
		            			} catch(java.text.ParseException e) {
		            				System.out.println("\t\t\tPARSER ERROR: "+publicacao.item(k).getTextContent());
		            			}
		            			
		            			break;
		            		case "Fonte":
		            			register.put("NM_DIARIO", publicacao.item(k).getTextContent());
		            			break;
		            		case "TeorPublicacao":
		            			register.put("TXT_ANDAMENTO", publicacao.item(k).getTextContent());
		            			break;
		            		case "DataDisponibilizacao":
		            			try {
		            				register.put("DS_DT_DIVULGACAO", publicacao.item(k).getTextContent());
			            			
			            			sdf = new SimpleDateFormat("dd/MM/yyyy");
			            			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			            			GregorianCalendar dtDivulgacao = Util.dateToCalendar(sdf.parse(publicacao.item(k).getTextContent()));
			            			register.put("DT_DIVULGACAO", dtDivulgacao);
		            			} catch(java.text.ParseException e) {
		            				System.out.println("\t\t\tPARSE ERROR: "+publicacao.item(k).getTextContent());
		            			}
		            			break;
		            		case "CodigoPublicacao":
		            			register.put("ID_RECORTE", publicacao.item(k).getTextContent());
		            			
		            			Recorte recorte = RecorteServices.getByIdRecorte(String.valueOf(register.get("ID_RECORTE")));
		            			
		            			register.put("ST_RECORTE", (recorte != null) ? recorte.getStRecorte() : 0);
		            			
		            			break;
            			}
	            		
            		}
            		
            		register.put("TXT_RECORTE", "{"+txtRecorte+"}");
            		try {
            			processAndamento((String)register.get("TXT_ANDAMENTO"), register);
                    	rsmRecortes.addRegister(register);
            		} catch(Exception e) {
            			System.out.println("ERRO! processAndamento do recorte "+register.get("ID_RECORTE"));
            			e.printStackTrace(System.out);
            		}
            	}
            	
            }
		    
		    return new Result(1, rsmRecortes.size() + " recorte(s) encontrado(s)", "RSM_RECORTES", rsmRecortes);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao processar recortes!");
		} 
	}
	
	private static Result processPublicacoesOnlineJson(ServicoRecorte servico, String content) {
		try {
		    
			ResultSetMap rsmRecortes = new ResultSetMap();
			SimpleDateFormat sdf = null;
			
			Connection connection = Conexao.conectar();
					    
			JSONArray array  = new JSONArray(content);
			for(int i = 0; i<array.length(); i++) {	
				boolean isNew = true;
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				
				JSONObject jOutput = array.getJSONObject(i);
				
				register.put("TXT_RECORTE", (String)jOutput.toString());
				
				if(jOutput.has("idPublicacao") && !jOutput.get("idPublicacao").equals(null)) {
					register.put("ID_RECORTE", jOutput.get("idPublicacao"));
					Recorte recorte = RecorteServices.getByIdRecorte(String.valueOf(register.get("ID_RECORTE")), servico.getCdServico(), connection);
					register.put("ST_RECORTE", (recorte != null) ? recorte.getStRecorte() : 0);

					isNew = recorte==null;
				}
				
				if(jOutput.has("dataDisponibilizacao") && !jOutput.get("dataDisponibilizacao").equals(null)) {
					register.put("DS_DT_DIVULGACAO", (String)jOutput.get("dataDisponibilizacao"));
					
					GregorianCalendar dtDivulgacao = com.tivic.manager.util.Util.convStringToCalendar((String)jOutput.get("dataDisponibilizacao"));
					register.put("DT_DIVULGACAO", dtDivulgacao);
				}
				
				if(jOutput.has("dataPublicacao") && !jOutput.get("dataPublicacao").equals(null)) {
					register.put("DS_DT_PUBLICACAO", (String)jOutput.get("dataPublicacao"));
					
					GregorianCalendar dtPublicacao = com.tivic.manager.util.Util.convStringToCalendar((String)jOutput.get("dataPublicacao"));
					register.put("DT_PUBLICACAO", dtPublicacao);
				}
				
				if(jOutput.has("numeroProcessoCNJ")) {
					if(!jOutput.get("numeroProcessoCNJ").equals(null) || !((String)jOutput.get("numeroProcessoCNJ")).trim().equals("")) {
						register.put("NR_PROCESSO", ((String)jOutput.get("numeroProcessoCNJ")).trim());
					}
				}
				
				if(jOutput.has("numeroProcesso")) {
					if(!jOutput.get("numeroProcesso").equals(null) || !((String)jOutput.get("numeroProcesso")).trim().equals("")) {
						register.put("NR_PROCESSO_ANTIGO", (String)jOutput.get("numeroProcesso"));
					}
				}
				
				if(jOutput.has("nomeJornal") && !jOutput.get("nomeJornal").equals(null)) {
					register.put("NM_DIARIO", (String)jOutput.get("nomeJornal"));
				}
				
				if(jOutput.has("pagina") && !jOutput.get("pagina").equals(null)) {
					register.put("NR_PAGINA", (String)jOutput.get("pagina"));
				}
				
				if(jOutput.has("orgao") && !jOutput.get("orgao").equals(null)) {
					register.put("NM_ORGAO", (String)jOutput.get("orgao"));
				}
				
				if(jOutput.has("vara") && !jOutput.get("vara").equals(null)) {
					register.put("NM_JUIZO", (String)jOutput.get("vara"));
				}
				
				if(jOutput.has("autor") && !jOutput.get("autor").equals(null)) {
					register.put("NM_AUTOR", (String)jOutput.get("autor"));
				}
				
				if(jOutput.has("reu") && !jOutput.get("reu").equals(null)) {
					register.put("NM_REU", (String)jOutput.get("reu"));
				}
				
				if(jOutput.has("uf") && !jOutput.get("uf").equals(null)) {
					register.put("SG_ESTADO", (String)jOutput.get("uf"));
				}
				
				if(jOutput.has("conteudo") && !jOutput.get("conteudo").equals(null)) {
					register.put("TXT_ANDAMENTO", (String)jOutput.get("conteudo"));
					
					if(isNew)
						processAndamento((String)register.get("TXT_ANDAMENTO"), register, connection);
				}
				
				rsmRecortes.addRegister(register);
			}
		    
		    return new Result(1, rsmRecortes.size() + " recorte(s) encontrado(s)", "RSM_RECORTES", rsmRecortes);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao processar recortes!");
		} 
	}
	
	private static Result confirmRetrievePublicacoesOnlineJson(ResultSetMap rsmRecortes, ServicoRecorte servico) {
		
		try {
			ServicoRecorte servicoAux = (ServicoRecorte)servico.clone();
			servicoAux.setDsUrl("http://www.publicacoesonline.com.br/index_ws_processadas.php");
			servicoAux.setTxtFormato("${URL}?hashCliente=${NM_SENHA}&listaIdsRetorno=${DS_LISTA}&retorno=JSON");
			
			int batch = 50;
			int done  = 0;
			int left  = rsmRecortes.size();
			
			String[] ids = Util.join(rsmRecortes, "id_recorte", true).split(",");
			String idToSend = "";
			
			Result r = null;
			
			for (String id : ids) {
				done++;
				left--;
				
				idToSend += id;
				if(done % batch != 0 && left >= batch)
					idToSend += ",";
				
				if(done % batch == 0) {
					
					HashMap<String, String> placeholdersMap = new HashMap<>();
					placeholdersMap.put("${URL}", servicoAux.getDsUrl());
					placeholdersMap.put("${NM_SENHA}", servicoAux.getNmSenha());
					placeholdersMap.put("${DS_LISTA}", idToSend);
					
					r = retrieveServicoRecortesHttp(servicoAux, placeholdersMap);
										
					if(r.getCode() > 0) {
						String message = (String)r.getObjects().get("CONTENT");
						JSONObject jsonResponse = new JSONObject(message);
						
						if(jsonResponse.get("sucesso")!=null) 
							LogUtils.info("Info: "+((JSONObject)jsonResponse.get("sucesso")).getString("mensagem"));
						else if(jsonResponse.get("erros")!=null) 
							LogUtils.info("Info: "+((JSONObject)jsonResponse.get("erros")).getString("mensagem"));
						else
							LogUtils.info("Erro ao processar lista de retorno.\n\t"+idToSend);
					}

					idToSend = "";
					
				} else if(left < batch) {
					idToSend = "";
					for(int i=done-1; i<left; i++)
						idToSend += ids[i] + ",";
					
					HashMap<String, String> placeholdersMap = new HashMap<>();
					placeholdersMap.put("${URL}", servicoAux.getDsUrl());
					placeholdersMap.put("${NM_SENHA}", servicoAux.getNmSenha());
					placeholdersMap.put("${DS_LISTA}", idToSend);
					
					r = retrieveServicoRecortesHttp(servicoAux, placeholdersMap);
										
					if(r.getCode() > 0) {
						String message = (String)r.getObjects().get("CONTENT");
						JSONObject jsonResponse = new JSONObject(message);
						
						if(jsonResponse.get("sucesso")!=null) 
							LogUtils.info("Info: "+((JSONObject)jsonResponse.get("sucesso")).getString("mensagem"));
						else if(jsonResponse.get("erros")!=null) 
							LogUtils.info("Info: "+((JSONObject)jsonResponse.get("erros")).getString("mensagem"));
						else
							LogUtils.info("Erro ao processar lista de retorno.\n\t"+idToSend);
					}
					
					break;
				}
			}
						
			return new Result(1);
		}
		catch(Exception e) {
			System.out.println("Erro ao confirmar leitura de recortes (PublicaçõesOnline).\n"+e.getMessage());
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	/*
	 * METODOS GERAIS PARA TODOS OS SERVICOS
	 */
	
     /**
 	 * Busca o conteudo de um servico de recortes via conexao HTTP com autenticacao de acesso básica (Basic Authentication)
 	 * @param servico
 	 * @param placeholdersMap
 	 * @return
 	 */
 	private static Result retrieveServicoRecortesBasicAuthentication(ServicoRecorte servico, HashMap<String, String> placeholdersMap) {

		String url = processPlaceholders(servico.getTxtFormato(), placeholdersMap);
		String idCliente = placeholdersMap.get("${ID_CLIENTE}");
		String nmSenha = placeholdersMap.get("${NM_SENHA}");
		
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		
		LogUtils.debug("Conectando ao serviço de recortes: " + servico.getNmServico());
		LogUtils.debug("URL: "+url);
		
		String auth = idCliente+":"+nmSenha;
		
		//Encoder encoder = Base64.getEncoder();
		//String authHeader = "Basic " + encoder.encodeToString(auth.getBytes(Charset.forName("ISO-8859-1")));
		
		String authHeader = "Basic " + DatatypeConverter.printBase64Binary(auth.getBytes(Charset.forName("ISO-8859-1")));
		
		Header header = new Header(HttpHeaders.AUTHORIZATION, authHeader);
		method.setRequestHeader(header);
		
 		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
 				new DefaultHttpMethodRetryHandler(3, false));
 		
 		try {
 			String content = "";
 			
 			int statusCode = client.executeMethod(method);

 			content = method.getResponseBodyAsString();
 			
 			//System.out.println("CONTENT: " + content);
 			
 			if (statusCode != HttpStatus.SC_OK) {
 				String errorMessage = method.getStatusLine().toString();
 				
 				switch(statusCode) {
 					case HttpStatus.SC_NOT_FOUND: 
 						errorMessage = "O serviço de recortes está fora do ar no momento. Tente novamente mais tarde.";
 						break;
 					case HttpStatus.SC_FORBIDDEN: 
 						errorMessage = "Acesso ao serviço de recortes não permitido no momento.";
 						break;
 					case HttpStatus.SC_UNAUTHORIZED: 
 						errorMessage = "Acesso ao serviço de recortes não autorizado.";
 						break;
 					case HttpStatus.SC_INTERNAL_SERVER_ERROR: 
 						errorMessage = "Erro interno do serviço de recortes.";
 						break;
 				}
 				
 				return new Result(-1, errorMessage, "CONTENT", content);
 			}
 			
 			return new Result(1, "Dados retornados", "CONTENT", content);
 		} 
 		catch (HttpException e) {
 			e.printStackTrace();
 			return new Result(-2, "Erro ao acessar serviço de recortes. Violação de protocolo.");
 		} 
 		catch (IOException e) {
 			e.printStackTrace();
 			return new Result(-3, "Erro ao acessar serviço de recortes. Erro de comunicação.");
 		} 
 		catch (Exception e) {
 			e.printStackTrace();
 			return new Result(-3, "Erro ao acessar serviço de recortes.");
 		} 
 		finally {
 			method.releaseConnection();
 		}  
 	}
 	
	
	/**
	 * Busca o conteudo de um servico de recortes via conexao HTTP
	 * @param servico
	 * @param placeholdersMap
	 * @return
	 */
	private static Result retrieveServicoRecortesHttp(ServicoRecorte servico, HashMap<String, String> placeholdersMap) {

		String url = processPlaceholders(servico.getTxtFormato(), placeholdersMap);
				
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		
		LogUtils.debug("Conectando ao serviço de recortes: " + servico.getNmServico());
		LogUtils.debug("URL: "+url);

		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
				new DefaultHttpMethodRetryHandler(3, false));

		try {
			String content = "";
			
			int statusCode = client.executeMethod(method);

			content = method.getResponseBodyAsString();
			
			//System.out.println("CONTENT: " + content);
			
			if (statusCode != HttpStatus.SC_OK) {
				String errorMessage = method.getStatusLine().toString();
				
				switch(statusCode) {
					case HttpStatus.SC_NOT_FOUND: 
						errorMessage = "O serviço de recortes está fora do ar no momento. Tente novamente mais tarde.";
						break;
					case HttpStatus.SC_FORBIDDEN: 
						errorMessage = "Acesso ao serviço de recortes não permitido no momento.";
						break;
					case HttpStatus.SC_INTERNAL_SERVER_ERROR: 
						errorMessage = "Erro interno do serviço de recortes.";
						break;
				}
				
				return new Result(-1, errorMessage, "CONTENT", content);
			}
						
			return new Result(1, "Dados retornados", "CONTENT", content);
		} 
		catch (HttpException e) {
			e.printStackTrace();
			return new Result(-2, "Erro ao acessar serviço de recortes. Violação de protocolo.");
		} 
		catch (IOException e) {
			e.printStackTrace();
			return new Result(-3, "Erro ao acessar serviço de recortes. Erro de comunicação.");
		} 
		catch (Exception e) {
			e.printStackTrace();
			return new Result(-3, "Erro ao acessar serviço de recortes.");
		} 
		finally {
			method.releaseConnection();
		}  
	}

	
	
	private static String processPlaceholders(String text, HashMap<String, String> placeholdersMap) {
		
		String retorno = text;
				
		for (HashMap.Entry<String, String> entry : placeholdersMap.entrySet()) {
			retorno = retorno.replaceAll(Pattern.quote(entry.getKey()), entry.getValue());
		}
		
		return retorno;
	}
	
	private static void processAndamento(String value, HashMap<String, Object> register) {
		processAndamento((String)register.get("TXT_ANDAMENTO"), register, null);
	}
	
	private static void processAndamento(String value, HashMap<String, Object> register, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			// nrProcesso no texto da publicação
			String nrProcessoRegex = getNrProcesso(value, "([0-9]+[-|.][0-9]{2,}.[0-9]{4,}.[0-9]*.[0-9]{2,}.[0-9]{4,})|([0-9]{12,25})|([0-9]{5,}-[0-9]{1}/[0-9]{2})");
			if(nrProcessoRegex==null || nrProcessoRegex.equals("[Não encontrado]")) {
				if(register.get("NR_PROCESSO")!=null && !((String)register.get("NR_PROCESSO")).equals(""))
					nrProcessoRegex = getNrProcesso(value, "("+((String)register.get("NR_PROCESSO")).replaceAll("[-.]", "")+")|("+Util.removeZeroEsquerda(((String)register.get("NR_PROCESSO")).replaceAll("[-.]", ""))+")");
			}			
			
			if(register.get("CD_PROCESSO") != null) {				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.cd_processo", Integer.toString((Integer)register.get("CD_PROCESSO")), ItemComparator.EQUAL, Types.INTEGER));
			
				ResultSetMap rsmProcessos = ProcessoServices.find(criterios, new ArrayList<String>(), connect);
				if(rsmProcessos.next()) {	
					register.put("CD_ESTADO", rsmProcessos.getInt("CD_ESTADO"));
					rsmProcessos.beforeFirst();
					register.put("RSM_PROCESSOS", rsmProcessos);	
				}
			}
			
			if(register.get("RSM_PROCESSOS")==null) {
				if (register.get("NR_PROCESSO") != null && !register.get("NR_PROCESSO").equals("") && !register.get("NR_PROCESSO").equals("[Não encontrado]")) {
					
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("nr_processo", Util.removeZeroEsquerda((String)register.get("NR_PROCESSO")), ItemComparator.LIKE_ANY, Types.VARCHAR));
				
					ResultSetMap rsmProcessos = ProcessoServices.find(criterios, new ArrayList<String>(), connect);
					if(rsmProcessos.size()>1) {
						register.put("ST_RECORTE", RecorteServices.ST_NAO_PROCESSADO);
					}
					else {
						if(rsmProcessos.next()) {	
							register.put("CD_PROCESSO", rsmProcessos.getInt("CD_PROCESSO"));
							register.put("CD_ESTADO", rsmProcessos.getInt("CD_ESTADO"));
							rsmProcessos.beforeFirst();
							register.put("RSM_PROCESSOS", rsmProcessos);
							
							register.put("NR_PROCESSO", (String)register.get("NR_PROCESSO"));
						}
					}
					
				}
			}
			
			if(register.get("RSM_PROCESSOS")==null) {
				if (register.get("NR_PROCESSO_ANTIGO") != null && !register.get("NR_PROCESSO_ANTIGO").equals("") && !register.get("NR_PROCESSO_ANTIGO").equals("[Não encontrado]")) {
					
					String nrNumeroAntigo = Util.removeZeroEsquerda((String)register.get("NR_PROCESSO_ANTIGO"));
					
					String nmCliente = getCliente(register, connect);
//					String nmAdverso = getAdverso(register);
					
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("nr_processo", nrNumeroAntigo, ItemComparator.LIKE_ANY, Types.VARCHAR));
					if(nrNumeroAntigo.length()<14 && nmCliente!=null)
						criterios.add(new ItemComparator("nm_cliente", nmCliente, ItemComparator.LIKE_ANY, Types.VARCHAR));
									
					ResultSetMap rsmProcessos = ProcessoServices.find(criterios, new ArrayList<String>(), connect);
					if(rsmProcessos.size()>1) {
						register.put("ST_RECORTE", RecorteServices.ST_NAO_PROCESSADO);
					}
					else {
						if(rsmProcessos.next()) {	
							register.put("CD_PROCESSO", rsmProcessos.getInt("CD_PROCESSO"));
							register.put("CD_ESTADO", rsmProcessos.getInt("CD_ESTADO"));
							rsmProcessos.beforeFirst();
							register.put("RSM_PROCESSOS", rsmProcessos);	
							
							register.put("NR_PROCESSO", (String)register.get("NR_PROCESSO_ANTIGO"));
						}
					}
				}
			}
			
			if(register.get("RSM_PROCESSOS")==null) {
				if (nrProcessoRegex != null && !nrProcessoRegex.equals("") && !nrProcessoRegex.equals("[Não encontrado]")) {
					
					String nmCliente = getCliente(register, connect);
//					String nmAdverso = getAdverso(register);
					
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("nr_processo", Util.removeZeroEsquerda(nrProcessoRegex), ItemComparator.LIKE_ANY, Types.VARCHAR));
					if(Util.removeZeroEsquerda(nrProcessoRegex).length()<14 && nmCliente!=null)
						criterios.add(new ItemComparator("nm_cliente", nmCliente, ItemComparator.LIKE_ANY, Types.VARCHAR));
					
				
					ResultSetMap rsmProcessos = ProcessoServices.find(criterios, new ArrayList<String>(), connect);
					if(rsmProcessos.size()>1) {
						register.put("ST_RECORTE", RecorteServices.ST_NAO_PROCESSADO);
					}
					else {
						if(rsmProcessos.next()) {	
							register.put("CD_PROCESSO", rsmProcessos.getInt("CD_PROCESSO"));
							register.put("CD_ESTADO", rsmProcessos.getInt("CD_ESTADO"));
							rsmProcessos.beforeFirst();
							register.put("RSM_PROCESSOS", rsmProcessos);	

							register.put("NR_PROCESSO", nrProcessoRegex);
						}
					}
					
				}
			}
			
			if(register.get("RSM_PROCESSOS")==null) {
				register.put("NR_PROCESSO", "[Não encontrado]");
				//register.put("ST_RECORTE", RecorteServices.ST_COM_ALERTA);
			}
			
			//Datas
			Pattern r;
			Matcher m;
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
			}
		}
		catch(Exception e) {
			//e.printStackTrace(System.out);
			System.out.println("Alerta! ServicoRecorteServices.processAndamento: " + e.getMessage());
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static String getNrProcesso(String value, String regex) {			
		String nrProcesso = "[Não encontrado]";
		Pattern r = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL);
		Matcher m = r.matcher(value);
		if (m.find( )) {
			for(int i=0; i<m.groupCount(); i++){
				if(m.group(i)!=null) {
					nrProcesso = m.group(i);
					break;
				}
			}
			//verificando mascara
			String[] partes = nrProcesso.trim().split(" ");
			if(partes.length>1) {
				nrProcesso = partes[0];
				if(partes.length>2 || partes[0].length()<9) {
					nrProcesso+="."+partes[1];
				}
			}
			nrProcesso = nrProcesso.trim();
		} 
		return nrProcesso;
	}
	
	private static String getCliente(HashMap<String, Object> regRecorte) {
		return getCliente(regRecorte, null);
	}
	
	private static String getCliente(HashMap<String, Object> regRecorte, Connection connection) {
		String nmReu = (String)regRecorte.get("NM_REU");
		String nmAutor = (String)regRecorte.get("NM_AUTOR");
		
		ResultSetMap rsm = ProcessoServices.findClienteByName(nmReu, connection);
		if(rsm.next()) {
			return rsm.getString("NM_PESSOA");
		}
		else {
			rsm = ProcessoServices.findClienteByName(nmAutor, connection);
			if(rsm.next())
				return rsm.getString("NM_PESSOA");
			else
				return null;
		}
	}
	
	private static String getAdverso(HashMap<String, Object> regRecorte) {
		String nmReu = (String)regRecorte.get("NM_REU");
		String nmAutor = (String)regRecorte.get("NM_AUTOR");
		
		ResultSetMap rsm = ProcessoServices.findAdversoByName(nmReu);
		if(rsm.next()) {
			return rsm.getString("NM_PESSOA");
		}
		else {
			rsm = ProcessoServices.findAdversoByName(nmAutor);
			if(rsm.next())
				return rsm.getString("NM_PESSOA");
			else
				return null;
		}
	}
	
	/**
	 * Envia inforções de novos processos aos serviços de recorte (ativos)
	 * @param processo
	 * @return
	 */
	public static Result sendNewProcess(Processo processo) {
		try {
			
			ResultSetMap rsmServicos = getAtivos();
			Result result = null;
			while(rsmServicos.next()) {
				switch(rsmServicos.getInt("tp_servico")) {
					case TP_PUBLICACOES_ONLINE: 
						result = sendNewProcessPublicacoesOnline(processo);
						
						// LOG ---------------
						if(result==null) {
							ServicoRecorteOcorrenciaServices
								.save(new ServicoRecorteOcorrencia(0, 
										rsmServicos.getInt("cd_servico"), 
										new GregorianCalendar(), 
										ServicoRecorteOcorrenciaServices.TP_ERRO, 
										0, null, null, processo.getCdProcesso(), 0));
						}
						else if(result.getCode()<0) {
							ServicoRecorteOcorrenciaServices
								.save(new ServicoRecorteOcorrencia(0, 
										rsmServicos.getInt("cd_servico"), 
										new GregorianCalendar(), 
										ServicoRecorteOcorrenciaServices.TP_ERRO, 
										result.getCode(), 
										result.getMessage(), 
										(result.getObjects().get("JSON")!=null ? ((JSONObject)result.getObjects().get("JSON")).toString() : null), 
										processo.getCdProcesso(), 0));
						}
						else {
							ServicoRecorteOcorrenciaServices
								.save(new ServicoRecorteOcorrencia(0, 
									rsmServicos.getInt("cd_servico"), 
									new GregorianCalendar(), 
									ServicoRecorteOcorrenciaServices.TP_SUCESSO, 
									result.getCode(), 
									result.getMessage(), 
									(result.getObjects().get("JSON")!=null ? ((JSONObject)result.getObjects().get("JSON")).toString() : null), 
									processo.getCdProcesso(), 0));
						}
						// -------------------
						
						break;
					case TP_KURIER_SOAP:
						result = inserirProcessosKurier(processo.getCdProcesso(), null);
						break;
				}
			}
			
			return new Result(1);
		}
		catch(Exception e) {
			LogUtils.info("Erro! ServicoRecorteServices.sendNewProcess: "+e.getMessage());
//			e.printStackTrace(System.out);
			return null;
		}
	}
	
	/**
	 * Envia informações do novo processo a PublicaçõesOnline
	 * @param processo
	 * @return
	 * 
	 * @see #sendNewProcess(Processo)
	 */
	private static Result sendNewProcessPublicacoesOnline(Processo processo) {
		try {
			
			// JSON com informações do processo
			
			String nmClientes = ProcessoServices.getClientes(processo.getCdProcesso());
			String nmAdversos = ProcessoServices.getAdversos(processo.getCdProcesso());
			
			String payload = "{"
					+ "\"numero\":\""+processo.getNrProcesso()+"\","
					+ "\"autor\":\""+nmClientes.split(",")[0].trim()+"\","
					+ "\"reu\":\""+nmAdversos.split(",")[0].trim()+"\""
					+ "}";
			
			if(processo.getNrAntigo()!=null && !processo.getNrAntigo().equals(""))
				payload += ",{"
						+ "\"numero\":\""+processo.getNrAntigo()+"\","
						+ "\"autor\":\""+nmClientes.split(",")[0].trim()+"\","
						+ "\"reu\":\""+nmAdversos.split(",")[0].trim()+"\""
						+ "}";
						
			// Envio			
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target("https://publicacoesonline.com.br/sarmento_silva_processos.php");

			Response response = target.request(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON).post(Entity.json("["+payload+"]"));
			response.bufferEntity();
			
			// Validação de resposta
			if(response.getStatus() != 200) {
				LogUtils.info("Erro (-1)! sendNewProcessPublicacoesOnline: "+response.toString());
				return new Result(-1, response.toString());
			}
			
			JSONObject jsoResponse = new JSONObject(response.readEntity(String.class));
			
			if(!jsoResponse.getBoolean("success")) {
				LogUtils.info("Erro (-2)! sendNewProcessPublicacoesOnline: "+jsoResponse.getString("message"));
				return new Result(-2, jsoResponse.getString("message"), "JSON", jsoResponse);
			}
			
			LogUtils.info("Sucesso! sendNewProcessPublicacoesOnline: "+jsoResponse.getString("message"));
			
			return new Result(jsoResponse.getInt("code"), jsoResponse.getString("message"), "JSON", jsoResponse);
		}
		catch(Exception e) {
			LogUtils.info("Erro! ServicoRecorteServices.sendNewProcessPublicacoesOnline: "+e.getMessage());
			
			if(e.getClass()==ConnectException.class)
				return new Result(-3, "Erro! ServicoRecorteServices.sendNewProcessPublicacoesOnline: "+e.getMessage());

			return null;
		}
		
		
	}
	
}