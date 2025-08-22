package com.tivic.manager.log;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.servlet.ServletRequest;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.Acao;
import com.tivic.manager.seg.AcaoServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.DeveloperServices;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.security.Action;
import sol.security.GroupActions;
import sol.security.LogAccessObject;
import sol.security.LogActionExecution;
import sol.security.Module;
import sol.security.System;
import sol.security.User;
import sol.util.RequestUtilities;
import sol.util.Result;

public class LogServices implements sol.security.LogServices {

	public static final int VERBOSITY = ParametroServices.getValorOfParametroAsInteger("TP_VERBOSIDADE_LOG", 1);

	public static String[] tipoVerbosidade = {"[0] Nenhum Log",
		                                      "[1] Log Básico (Somente ações cadastradas)",
		                                      "[2] Log Completo (Todas as ações)"};

	public static final int DELETE = 0;
	public static final int INSERT = 1;
	public static final int UPDATE = 2;
	public static final int ANY = 3;
	
	public int getVerbosityLevel(){
		return VERBOSITY;
	}
	
	public static synchronized Result log(int type, String idAcao, AuthData auth, Object value) {
		return log(type, idAcao, auth, value, null, null, null);
	}
	
	public static synchronized Result log(int type, String idAcao, AuthData auth, Object newValue, Object oldValue) {
		return log(type, idAcao, auth, newValue, oldValue, null, null);
	}
	
	public static synchronized Result log(int type, String idAcao, AuthData auth, Object newValue, Object oldValue, String text) {
		return log(type, idAcao, auth, newValue, oldValue, text, null);
	}
	
	public static synchronized Result log(int type, String idAcao, AuthData auth, Object newValue, Object oldValue, String text, Connection connect) {

		/*
		 * 1. Verificar verbosidade log
		 * 2. Testar valores nulos enviados
		 * 3. Gerar texto de log (old e new values)
		 * 4. Gravar log
		 * 5. Retornar Result com dados da gravacao (nao precisa ser lido do outro lado)
		 */
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			//1.
			boolean lgLog = ParametroServices.getValorOfParametroAsInteger("TP_VERBOSIDADE_LOG", 1, 0, connect) > 0;
			if(!lgLog)
				return new Result(0, "Log desabilitado.");
			
			//2.
			if(auth==null)
				return new Result(-2, "Nenhuma informação do usuário!");
			if(idAcao==null || idAcao.equals(""))
				return new Result(-3, "Nenhum id de ação!");
			if(newValue==null)
				return new Result(-4, "Nenhuma informação de objeto!");
			
			//3.
			String txtLog = "";
			
			switch(type) {
				case INSERT:
					txtLog = formatInsert(auth.getIdForm(), newValue);
					break;
				case UPDATE:
					txtLog = formatUpdate(auth.getIdForm(), newValue, oldValue, connect);
					break;
				case DELETE:
					txtLog = formatDelete(auth.getIdForm(), newValue, connect);
					break;
				case ANY:
					txtLog = text;
					break;
			}
			
			//4.
			Result r = registrarAcao(idAcao, auth.getUsuario().getCdUsuario(), txtLog, connect);
			
			LogUtils.debug("Result registrar ação: "+r.toString());
			
			if(r.getCode()<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Erro ao gravar log de ações");
			}
			else if (isConnectionNull)
				connect.commit();
			
			//5.
			return r;
		}
		catch(Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

	public static synchronized Result registrarAcao(String idAcao, int cdUsuario, String txtLog){
		return registrarAcao(idAcao, cdUsuario, txtLog, null);
	}
	
	public static synchronized Result registrarAcao(String idAcao, int cdUsuario, String txtLog, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			
			if(VERBOSITY==VERBOSITY_NONE) 
				return new Result(-1, "Log está desligado.");
			
			if (isConnectionNull){
				connect = Conexao.conectar();
			}
			
			Acao acao = AcaoServices.getByIdAcao(idAcao, connect);
			if(acao==null)
				return new Result(-2, "Ação não existe");
			
			Usuario usuario = UsuarioDAO.get(cdUsuario, connect);
			if(usuario==null)
				return new Result(-3, "Usuário não existe");
			
			if(txtLog==null || txtLog.equals("")) {
				txtLog = acao.getNmAcao() + " pelo usuário " + usuario.getNmLogin() + " em " + Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm:ss");
			}
						
			ExecucaoAcao a = new ExecucaoAcao(0, //cdLog
					acao.getCdAcao(),
					acao.getCdModulo(),
					acao.getCdSistema(),
					cdUsuario,
					new GregorianCalendar(), //dtLog
					txtLog, //txtLog
					null, //txtResultadoExecucao
					1);
			
			long retorno = ExecucaoAcaoDAO.insert(a, connect); //tpResultadoExecucao
			
			if(retorno > 0)
				return new Result(1, "Log de ação registrada com sucesso!");
			else 
				return new Result(-4, "Erro ao registrar log de ação!");
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-5, "Erro ao registrar log de ação!");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static String formatInsert(String idForm, Object value) {
		return "Inclusão de novo " + idForm + ": \n" + formatValues(value);
	}
	
	public static String formatUpdate(String idForm, Object newValue, Object oldValue, Connection connect) {
		
		String txtPrimaryKeys = getPrimaryKeysText(newValue, connect);
		
		String text = "Alteração de " + idForm + (txtPrimaryKeys!=null && !txtPrimaryKeys.equals("") ? " de código(s) [" + txtPrimaryKeys : "") + "]\n";
		
		if(oldValue!=null) {
			text+= "-----------------------------------------\n" + 
				   "Dados anteriores:\n" + formatValues(oldValue);
		}
		
		text += "-----------------------------------------\n" + 
				"Dados atualizados:\n" + formatValues(newValue);
		
		return text;
	}
	
	public static String getPrimaryKeysText(Object value, Connection connect) {
		try {
			String className = value.getClass().getName();
			String[] parts = className.split("\\.");
			String prefix = parts[parts.length-2].toLowerCase();
			className = parts[parts.length-1];
			String tableName = prefix + DeveloperServices.camelCaseToUnderscore(className);
			
			ArrayList<String> primaryKeys = DeveloperServices.getPrimaryKeys(tableName, connect);
			Field[] objectFields =  value.getClass().getDeclaredFields();
			String txtPrimaryKeys = "";
			
			for (Field field : objectFields) {
				field.setAccessible(true);
				if(primaryKeys.contains(DeveloperServices.camelCaseToUnderscore(field.getName()))) {
					if(!txtPrimaryKeys.equals(""))
						txtPrimaryKeys+=", ";
					txtPrimaryKeys+=String.valueOf(field.get(value));
				}
			}
			
			return txtPrimaryKeys;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String formatDelete(String idForm, Object value, Connection connect) {
		String txtPrimaryKeys = getPrimaryKeysText(value, connect);
		
		return "Exclusão de " + idForm + (txtPrimaryKeys!=null && !txtPrimaryKeys.equals("") ? " de código(s) [" + txtPrimaryKeys : "") + "]\n" +
			((value!=null) ? "Dados de " + idForm + ":\n" + formatValues(value) : "");
	}
	
	public static String formatValues(Object obj) {
		try {
			
			String values = "";
			Field[] objectFields =  obj.getClass().getDeclaredFields();
			
			for (Field field : objectFields) {
				field.setAccessible(true);
				Object value = field.get(obj);
				String txtValue = "";
				
				if(value!=null){
					if(value instanceof GregorianCalendar)
						txtValue = Util.formatDate((GregorianCalendar)value, "dd/MM/yyyy HH:mm:ss");
					else if(value instanceof byte[])
						txtValue = "[bytes]";
					else
						txtValue = String.valueOf(value);
					
					values += DeveloperServices.camelCaseToUnderscore(field.getName()).toUpperCase() + ": " + txtValue + "\n";
				}
			}
			
			return values;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ResultSetMap auditarLogs(String[] idAgrupamentos, String[] idAcoes, String idModulo, String txtFiltro) {
		return auditarLogs(idAgrupamentos, idAcoes, idModulo, txtFiltro, 0, null, null, null);
	}
	
	public static ResultSetMap auditarLogs(String[] idAgrupamentos, String[] idAcoes, String idModulo, String txtFiltro, int cdUsuario, String dtInicial, String dtFinal) {
		return auditarLogs(idAgrupamentos, idAcoes, idModulo, txtFiltro, cdUsuario, dtInicial, dtFinal, null);
	}
	
	public static ResultSetMap auditarLogs(String[] idAgrupamentos, String[] idAcoes, String idModulo, String txtFiltro, 
			int cdUsuario, String dtInicial, String dtFinal, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			String idAgrupamentosJoined = Util.join(idAgrupamentos, "'", ",");
			
			//TODO: pegar acoes dos agrupamentos e concatenar com idAcoes
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.id_acao FROM seg_acao A"
																+ " JOIN seg_agrupamento_acao B on (A.cd_agrupamento = B.cd_agrupamento)"
																+ " WHERE B.id_agrupamento IN ("+idAgrupamentosJoined+")");
			LogUtils.debug("SELECT A.id_acao FROM seg_acao A"
					+ " JOIN seg_agrupamento_acao B on (A.cd_agrupamento = B.cd_agrupamento)"
					+ " WHERE B.id_agrupamento IN ("+idAgrupamentosJoined+")");
			
			ResultSetMap rsmAcoes = new ResultSetMap(pstmt.executeQuery());
			LogUtils.debug("rsmAcoes: "+rsmAcoes.toString());
			String[] idAcoesAux = new String[rsmAcoes.size()];
			while(rsmAcoes.next()) {
				idAcoesAux[rsmAcoes.getPointer()] = rsmAcoes.getString("id_acao");
			}
			
			idAcoes = Util.concat(idAcoes, idAcoesAux);
			
			return auditarLogs(idAcoes, idModulo, txtFiltro, 0, null, null, connection);
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap auditarLogs(String[] idAcoes, String idModulo, String txtFiltro) {
		return auditarLogs(idAcoes, idModulo, txtFiltro, 0, null, null, null);
	}
	
	public static ResultSetMap auditarLogs(String[] idAcoes, String idModulo, String txtFiltro, int cdUsuario, String dtInicial, String dtFinal) {
		return auditarLogs(idAcoes, idModulo, txtFiltro, cdUsuario, dtInicial, dtFinal, null);
	}
	
	public static ResultSetMap auditarLogs(String[] idAcoes, String idModulo, String txtFiltro, int cdUsuario, String dtInicial, String dtFinal, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
		
			int cdSistema = 0;
			int cdModulo = 0;
			
			//cd_modulo com base em id_acao
			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_modulo FROM seg_acao"
																+ " WHERE id_acao IN ("+Util.join(idAcoes, "'", ",")+")"
																+ " GROUP BY cd_modulo");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			String[] arrayCdModulo = new String[rsm.getLines().size()];
			while(rsm.next()) {
				arrayCdModulo[rsm.getPointer()] = ""+rsm.getInt("cd_modulo");
			}
			
			pstmt = connection.prepareStatement("SELECT * " +
					  "FROM seg_modulo A, seg_sistema B " +
					  "WHERE A.cd_sistema = B.cd_sistema " +
					  "  AND A.id_modulo = ? ");
			pstmt.setString(1, idModulo);
			rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()) {
				cdSistema = rsm.getInt("cd_sistema");
//				cdModulo = rsm.getInt("cd_modulo");
			}
			
			String sql = "SELECT * " +
					 " FROM seg_acao A " + 
					 " WHERE A.id_acao IN (";
			
			for (int i = 0; i < idAcoes.length; i++) {
				sql += "'"+idAcoes[i] + (i < idAcoes.length-1 ? "'," : "'");
			}
			
			sql += ")";
			
			LogUtils.debug(sql);
						
			pstmt = connection.prepareStatement(sql);
			rsm = new ResultSetMap(pstmt.executeQuery());
			
			String inCrit = "";
			for (int i = 0; rsm.next(); i++)
				inCrit += rsm.getString("cd_acao") + (i < rsm.size()-1 ? "," : "");
						
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("S.cd_sistema", String.valueOf(cdSistema), ItemComparator.EQUAL, Types.INTEGER));
//			criterios.add(new ItemComparator("S.cd_modulo", String.valueOf(cdModulo), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("S.cd_modulo", Util.join(arrayCdModulo, "", ","), ItemComparator.IN, Types.INTEGER));
			criterios.add(new ItemComparator("S.cd_acao", inCrit, ItemComparator.IN, Types.INTEGER));
			criterios.add(new ItemComparator("S.txt_execucao", txtFiltro, ItemComparator.LIKE_ANY, Types.VARCHAR));
			
			if(cdUsuario>0)
				criterios.add(new ItemComparator("S.cd_usuario", String.valueOf(cdUsuario), ItemComparator.EQUAL, Types.INTEGER));
			
			if(dtInicial!=null && !dtInicial.equals(""))
				criterios.add(new ItemComparator("S.dt_execucao", dtInicial, ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
			if(dtFinal!=null && !dtFinal.equals(""))
				criterios.add(new ItemComparator("S.dt_execucao", dtFinal, ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
				
			rsm = Search.find("SELECT S.*, A.*, B.NM_ACAO, E.NM_LOGIN, F.NM_PESSOA AS NM_USUARIO "+
									 "FROM LOG_EXECUCAO_ACAO S "+
									 "JOIN SEG_USUARIO E ON (S.CD_USUARIO = E.CD_USUARIO) "+
									 "LEFT OUTER JOIN LOG_SISTEMA A ON (A.CD_LOG = S.CD_EXECUCAO) "+
									 "LEFT OUTER JOIN SEG_ACAO B ON (S.CD_MODULO = B.CD_MODULO AND S.CD_SISTEMA = B.CD_SISTEMA AND S.CD_ACAO = B.CD_ACAO) "+
									 "LEFT OUTER JOIN GRL_PESSOA F ON (E.CD_PESSOA = F.CD_PESSOA)", 
									 "ORDER BY DT_EXECUCAO DESC",
									 criterios, connection!=null ? connection : Conexao.conectar(), connection==null);
			
			while(rsm.next()) {
				
				String txtExecucao = rsm.getString("TXT_EXECUCAO");
				ResultSetMap rsmDetalhes = new ResultSetMap();
				
				StringTokenizer tokenPares = new StringTokenizer(txtExecucao, "|");
			
				while(tokenPares.hasMoreElements()) {
					String par = tokenPares.nextToken();
					
					if(par.indexOf(":")!=-1) {
						String nmCampo = par.substring(0, par.indexOf(":"));
						String vlCampo = par.substring(par.indexOf(":")+1);
						
						HashMap<String, Object> register = new HashMap<String, Object>();
						register.put("NM_CAMPO", nmCampo);
						register.put("DS_CAMPO", getNameByFieldName(nmCampo));
						register.put("VL_CAMPO", vlCampo);
						
						rsmDetalhes.addRegister(register);
					}
				}
				
				rsm.setValueToField("RSM_DETALHES", rsmDetalhes.getLines());
//				java.lang.System.out.println(rsmDetalhes);
				
			}
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	private static String getNameByFieldName(String field){
		String varName = "";
		StringTokenizer token = new StringTokenizer(field.toLowerCase(),"_");
		boolean isLogic = false;
		for(int i=0; token.hasMoreTokens(); i++){
			if(i==0) {
				String t = token.nextToken();
				switch(t) {
					case "CD":
						varName+="Cod.";
						break;
					case "ID":
						varName+="Id.";
						break;
					case "DT":
						varName+="Data";
						break;
					case "TXT":
						varName+="";
						break;
					case "TP":
						varName+="Tipo";
						break;
					case "LG":
						varName+="";
						isLogic = true;
						break;
					default:
						varName+=Util.capitular(t);
				}
				
			}
			else
				varName+=" " + Util.capitular(token.nextToken());
		}
		
		if(isLogic)
			varName+="?";
		
		return varName;
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT S.*, A.*, B.NM_ACAO, B.DS_ACAO, C.NM_MODULO, D.NM_SISTEMA, D.ID_SISTEMA, " +
				   "	   E.NM_LOGIN, E.NM_SENHA, F.NM_PESSOA AS NM_USUARIO, G.NM_AGRUPAMENTO, G.DS_AGRUPAMENTO, G.ID_AGRUPAMENTO " +
				   "FROM LOG_SISTEMA S " +
				   "JOIN SEG_USUARIO E ON (S.CD_USUARIO = E.CD_USUARIO) " +
				   "LEFT OUTER JOIN LOG_EXECUCAO_ACAO A ON (S.CD_LOG = A.CD_EXECUCAO " + 
				   		(Util.getConfManager().getIdOfDbUsed().equals("FB") ? " AND A.DT_EXECUCAO is null " : "") +") " +
				   "LEFT OUTER JOIN SEG_ACAO B ON (A.CD_MODULO = B.CD_MODULO AND A.CD_SISTEMA = B.CD_SISTEMA AND A.CD_ACAO = B.CD_ACAO) " +
				   "LEFT OUTER JOIN SEG_MODULO C ON (B.CD_MODULO = C.CD_MODULO AND B.CD_SISTEMA = C.CD_SISTEMA) " +
				   "LEFT OUTER JOIN SEG_SISTEMA D ON (C.CD_SISTEMA = D.CD_SISTEMA) " +
				   "LEFT OUTER JOIN GRL_PESSOA F ON (E.CD_PESSOA = F.CD_PESSOA) " +
				   "LEFT OUTER JOIN SEG_AGRUPAMENTO_ACAO G ON (B.CD_AGRUPAMENTO = G.CD_AGRUPAMENTO AND B.CD_MODULO = G.CD_MODULO AND B.CD_SISTEMA = G.CD_SISTEMA) ", criterios,
				connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	
	
	
	/**
	 * DEPRECIADOS
	 */
	
	@Deprecated
	public synchronized int registerLog(LogActionExecution log) {

		if(VERBOSITY==VERBOSITY_COMPLETE && log.getAction()==null)	{
			return SistemaDAO.insert(new Sistema(0, //cdLog
												log.getExecutionDate(), //dtLog
												log.getExecutionDescription(), //txtLog
												SistemaServices.TP_LOG_GERAL, //tpLog
												log.getUser().getId())); //cdUsuario
		}
		else	{
			
			/*
			 * PARA MANTER RETROCOMPATIBILIDADE COM A FORMA ANTIGA DE GUARDAR LOGs
			 * GUARDAR TAMBEM O LOG DE SISTEMA
			 */
			SistemaDAO.insert(new Sistema(0, //cdLog
					log.getExecutionDate(), //dtLog
					log.getExecutionDescription(), //txtLog
					SistemaServices.TP_LOG_ACAO, //tpLog
					log.getUser().getId())); //cdUsuario
			
			//INSERIR LOGS DE ACAO E SISTEMA
			return ExecucaoAcaoDAO.insert(new ExecucaoAcao(0, //cdLog
												log.getAction().getId(), //cdAcao
												log.getAction().getModule().getId(), //cdModulo
												log.getAction().getModule().getSystem().getId(), //cdSistema
												log.getUser().getId(), //cdUsuario
												log.getExecutionDate(), //dtLog
												log.getExecutionDescription(), //txtLog
												log.getResultDescription(), //txtResultadoExecucao
												log.getResult())); //tpResultadoExecucao
		}
	}
	
	@Deprecated
	public synchronized int registerLog(LogAccessObject log) {
		return AcessoObjetoDAO.insert(new AcessoObjeto(0, //cdLog
				log.getAccessDate(), //dtLog
				log.getAccessDescription(), //txtLog
				SistemaServices.TP_LOG_ACESSO, //tpLog
				log.getUser().getId(), //cdUsuario
				log.getObject().getForm().getSystem().getId(), //cdFormulario
				log.getObject().getForm().getId(), //cdObjeto
				log.getObject().getForm().getSystem().getId(), //cdSistema
				log.getResultDescription(), //txtResultadoAcesso
				log.getResult()));//tpResultadoAcesso
	}

	@Deprecated
	public LogActionExecution[] getLogOfExecutionActions(int[] idUsers, int[] idGroups, Action[] actions, GregorianCalendar initialDate, GregorianCalendar finalDate) {
		return (LogActionExecution[])findLogs(idUsers, idGroups, actions, initialDate, finalDate, false, null);
	}
	
	@Deprecated
	public static LogActionExecution[] findLogs() {
		return (LogActionExecution[])findLogs(null, null, null, null, null, false, null);
	}

	@Deprecated
	public static ResultSetMap findLogs(ServletRequest request, GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			boolean justSucessoExecucao) {
		/* usuarios */
		int countUsuarios = RequestUtilities.getAsInteger(request, "countUsuarios", 0);
		int[] cdUsuarios = countUsuarios<=0 ? null : new int[countUsuarios];
		for (int i=0; countUsuarios>0 && i<countUsuarios; i++) {
			cdUsuarios[i] = RequestUtilities.getAsInteger(request, "cdUsuario_" + i, 0);
		}

		/* grupos */
		int countGrupos = RequestUtilities.getAsInteger(request, "countGrupos", 0);
		int[] cdGrupos = countGrupos<=0 ? null : new int[countGrupos];
		for (int i=0; countGrupos>0 && i<countGrupos; i++) {
			cdGrupos[i] = RequestUtilities.getAsInteger(request, "cdGrupo_" + i, 0);
		}

		/* acoes */
		int countAcoes = RequestUtilities.getAsInteger(request, "countAcoes", 0);
		Action[] acoes = countAcoes<=0 ? null : new Action[countAcoes];
		for (int i=0; countAcoes>0 && i<countAcoes; i++) {
			int cdAcao = RequestUtilities.getAsInteger(request, "cdAcao_" + i, 0);
			int cdModulo = RequestUtilities.getAsInteger(request, "cdModulo_" + i, 0);
			int cdSistema = RequestUtilities.getAsInteger(request, "cdSistema_" + i, 0);
			acoes[i] = new Action(cdAcao, null, null, new Module(cdModulo, null, new System(cdSistema, null, null)), null);
		}

		return (ResultSetMap)findLogs(cdUsuarios, cdGrupos, acoes, dtInicial, dtFinal, justSucessoExecucao, true, null);
	}

	@Deprecated
	public static Object findLogs(int[] cdUsuarios, int[] cdGrupos, Action[] acoes, GregorianCalendar dtInicial,
			GregorianCalendar dtFinal) {
		return findLogs(cdUsuarios, cdGrupos, acoes, dtInicial, dtFinal, false, false, null);
	}

	@Deprecated
	public static Object findLogs(int[] cdUsuarios, int[] cdGrupos, Action[] acoes, GregorianCalendar dtInicial,
			GregorianCalendar dtFinal, boolean justSucessoExecucao, Connection connection) {
		return findLogs(cdUsuarios, cdGrupos, acoes, dtInicial, dtFinal, justSucessoExecucao, false, connection);
	}

	@Deprecated
	public static Object findLogs(int[] cdUsuarios, int[] cdGrupos, Action[] acoes, GregorianCalendar dtInicial,
			GregorianCalendar dtFinal, boolean justSucessoExecucao, boolean returnAsResultSet, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			String statement = "SELECT S.*, A.*, B.NM_ACAO, B.DS_ACAO, C.NM_MODULO, D.NM_SISTEMA, D.ID_SISTEMA, " +
							   "	   E.NM_LOGIN, E.NM_SENHA, F.NM_PESSOA AS NM_USUARIO, G.NM_AGRUPAMENTO, G.DS_AGRUPAMENTO, G.ID_AGRUPAMENTO " +
							   "FROM LOG_SISTEMA S " +
							   "JOIN SEG_USUARIO E ON (S.CD_USUARIO = E.CD_USUARIO) " +
							   "LEFT OUTER JOIN LOG_EXECUCAO_ACAO A ON (S.CD_LOG = A.CD_EXECUCAO) " +
							   "LEFT OUTER JOIN SEG_ACAO B ON (A.CD_MODULO = B.CD_MODULO AND A.CD_SISTEMA = B.CD_SISTEMA AND A.CD_ACAO = B.CD_ACAO) " +
							   "LEFT OUTER JOIN SEG_MODULO C ON (B.CD_MODULO = C.CD_MODULO AND B.CD_SISTEMA = C.CD_SISTEMA) " +
							   "LEFT OUTER JOIN SEG_SISTEMA D ON (C.CD_SISTEMA = D.CD_SISTEMA) " +
							   "LEFT OUTER JOIN GRL_PESSOA F ON (E.CD_PESSOA = F.CD_PESSOA) " +
							   "LEFT OUTER JOIN SEG_AGRUPAMENTO_ACAO G ON (B.CD_AGRUPAMENTO = G.CD_AGRUPAMENTO AND B.CD_MODULO = G.CD_MODULO AND B.CD_SISTEMA = G.CD_SISTEMA) " +
							   "WHERE 1=1 " +
							   (dtInicial != null ? "  AND S.DT_LOG >= ? " : "") +
							   (dtFinal != null ? "  AND S.DT_LOG <= ? " : "");
			if (cdUsuarios!=null && cdUsuarios.length>0) {
				statement += " AND (";
				for (int i=0; i<cdUsuarios.length; i++) {
					statement += (i!=0 ? " OR " : "") + " S.CD_USUARIO = " + cdUsuarios[i] + " ";
				}
				statement += ") ";
			}
			if (cdGrupos != null && cdGrupos.length>0) {
				statement += " AND EXISTS (SELECT * FROM SEG_USUARIO_GRUPO G WHERE E.CD_USUARIO = G.CD_USUARIO AND (";
				for (int i=0; i<cdGrupos.length; i++)
					statement += (i!=0 ? " OR " : "") + " G.CD_GRUPO = " + cdGrupos[i] + " ";
				statement += ")) ";
			}
			if (acoes != null && acoes.length>0) {
				statement += " AND (";
				for (int i=0; i<acoes.length; i++) {
					if (acoes[i]==null)
						continue;
					statement += (i!=0 ? " OR " : "") + " (A.CD_ACAO = " + acoes[i].getId() + " AND A.CD_MODULO = " + acoes[i].getModule().getId() + " AND A.CD_SISTEMA = " + acoes[i].getModule().getSystem().getId() + ") ";
				}
				statement += ")";
			}
			if (justSucessoExecucao)
				statement += " AND tp_resultado_execucao > 0";
			PreparedStatement pstmt = connection.prepareStatement(statement);
			int i = 1;
			if (dtInicial != null)
				pstmt.setTimestamp(i++, Util.convCalendarToTimestamp(dtInicial));
			if (dtFinal != null)
				pstmt.setTimestamp(i++, Util.convCalendarToTimestamp(dtFinal));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			ArrayList<String> criterios = new ArrayList<String>();
			criterios.add("DT_LOG DESC");
			rsm.orderBy(criterios);
			if (returnAsResultSet)
				return rsm;
			else {
				LogActionExecution[] logs = rsm==null ? null : new LogActionExecution[rsm.size()];
				for (i=0; rsm!=null && rsm.next(); i++) {
					System system = new System(rsm.getInt("CD_SISTEMA"), rsm.getString("NM_SISTEMA"), rsm.getString("ID_SISTEMA"));
					Module module = new Module(rsm.getInt("CD_MODULO"), rsm.getString("NM_MODULO"), system);
					GroupActions groupActions = rsm.getInt("CD_AGRUPAMENTO")==0 ? null : new GroupActions(rsm.getInt("CD_AGRUPAMENTO"), rsm.getString("NM_AGRUPAMENTO"), rsm.getString("DS_AGRUPAMENTO"), module);
					logs[i] = new LogActionExecution(new Action(rsm.getInt("CD_ACAO"), rsm.getString("NM_ACAO"), rsm.getString("DS_ACAO"), module, groupActions),
													 new User(rsm.getInt("CD_USUARIO"), rsm.getString("NM_PESSOA"), rsm.getString("NM_LOGIN"), null),
													 Util.convTimestampToCalendar(rsm.getTimestamp("DT_EXECUCAO")),
													 rsm.getString("TXT_LOG"),
													 rsm.getInt("TP_RESULTADO_EXECUCAO"),
													 rsm.getString("TXT_RESULTADO_EXECUCAO"));
				}
				return logs;
			}
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public int removeLogs(GregorianCalendar inititalDate, GregorianCalendar finalDate) {
		Connection connect = null;
		try {
			connect = Conexao.conectar();
			connect.setAutoCommit(false);
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM LOG_ACESSO_OBJETO " +
															   "WHERE 1=1 " +
															   (inititalDate!=null ? " AND DT_ACESSO >= ?" : "") +
															   (finalDate!=null ? " AND DT_ACESSO <= ?" : ""));
			int i = 1;
			if (inititalDate!=null)
				pstmt.setTimestamp(i++, Util.convCalendarToTimestamp(inititalDate));
			if (finalDate!=null)
				pstmt.setTimestamp(i++, Util.convCalendarToTimestamp(finalDate));
			pstmt.execute();

			pstmt = connect.prepareStatement("DELETE FROM LOG_EXECUCAO_ACAO " +
											   "WHERE 1=1 " +
											   (inititalDate!=null ? " AND DT_EXECUCAO >= ?" : "") +
											   (finalDate!=null ? " AND DT_EXECUCAO <= ?" : ""));
			i = 1;
			if (inititalDate!=null)
				pstmt.setTimestamp(i++, Util.convCalendarToTimestamp(inititalDate));
			if (finalDate!=null)
				pstmt.setTimestamp(i++, Util.convCalendarToTimestamp(finalDate));
			pstmt.execute();

			connect.commit();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			Conexao.rollback(connect);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
}
