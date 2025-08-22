package com.tivic.manager.grl;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletRequest;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Recursos;
import com.tivic.manager.util.Util;
import sol.dao.ResultSetMap;

public class FonteDadosServices {

	/* Tipos de fonte de dados */
	public static final int TP_GERAL = 0;
	public static final int TP_RELATORIO = 1;
	public static final int TP_MAILING = 2;
	public static final int TP_LISTA_DINAMICA = 3;

	public static final String[] tipoFonte = {"Geral", "Relatório", "Mailing", "Lista Dinâmica de Destinatários"};

	/* Tipos de origem */
	public static final int OR_SCRIPT_SQL = 0;
	public static final int OR_PROTOTIPO_FUNCAO = 1;

	public static final String[] tipoOrigem = {"Script SQL", "Protótipo de Função JAVA"};

	/* Tipos de campos de dados */
	public static final int COL_ENTRADA = 0;
	public static final int COL_SAIDA = 1;
	public static final int COL_ENTRADA_SAIDA = 2;

	public static final String[] tipoColuna = {"Entrada", "Saída", "Entrada/Saída"};

	/* Tipos de apresentacao de campos de dados */
	public static final int AP_TEXT = 0;
	public static final int AP_TEXTAREA = 1;
	public static final int AP_CHECKBOX = 2;
	public static final int AP_COMBO = 3;
	public static final int AP_RADIO = 4;
	public static final int AP_FILTRO = 5;
	public static final int AP_PASSWORD = 6;

	public static final String[] tipoApresentacao = {"Text", "Textarea", "Checkbox", "Combo", "Radio", "Filtro", "Password"};

	public static int save(int cdFonte, String nmFonte, String txtFonte, String txtScript, String txtColumns, String idFonte,
			int tpOrigem, int tpFonte){
		return save(cdFonte, nmFonte, txtFonte, txtScript, txtColumns, idFonte, tpOrigem, tpFonte, null);
	}

	public static int save(int cdFonte, String nmFonte, String txtFonte, String txtScript, String txtColumns,
			String idFonte, int tpOrigem, int tpFonte, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			FonteDados fonte = new FonteDados(cdFonte, nmFonte, txtFonte, txtScript, txtColumns, idFonte, tpOrigem, tpFonte);

			int retorno;
			if(cdFonte==0)
				retorno = FonteDadosDAO.insert(fonte, connect);
			else {
				retorno = FonteDadosDAO.update(fonte, connect);
				retorno = retorno>0?cdFonte:retorno;
			}
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FonteDadosServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}


	public static ResultSetMap getCamposFonteDados(int cdFonte) {
		return getCamposFonteDados(cdFonte, null);
	}

	public static ResultSetMap getCamposFonteDados(int cdFonte, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
				      "FROM grl_fonte_dados " +
				      "WHERE cd_fonte = ?");
			pstmt.setInt(1, cdFonte);

			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				// String script = rs.getString("txt_script");
//				return getCamposFonteDados(script, rs.getInt("tp_origem"), connect);
				return null;
			}
			else
				return new ResultSetMap();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FonteDadosServices.getCamposFonteDados: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getCamposFonteDados(String script) {
		return getCamposFonteDados(script, OR_SCRIPT_SQL, null, null);
	}

	public static ResultSetMap getCamposFonteDados(String script, int tpOrigem) {
		return getCamposFonteDados(script, tpOrigem, null, null);
	}

	public static ResultSetMap getCamposFonteDados(String script, int tpOrigem, Connection connect) {
		return getCamposFonteDados(script, tpOrigem, null, connect);
	}

	public static ResultSetMap getCamposFonteDados(String script, int tpOrigem, ServletRequest request) {
		return getCamposFonteDados(script, tpOrigem, request, null);
	}

	public static ResultSetMap getCamposFonteDados(String script, int tpOrigem, ServletRequest request,
			Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			if (tpOrigem == OR_SCRIPT_SQL) {
				ResultSetMap rsm = new ResultSetMap();

				//entrada
				Matcher matcher = Pattern.compile("#\\w*").matcher(script);
				while (matcher.find()){
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("NM_COLUNA", matcher.group().toUpperCase());
					register.put("TP_COLUNA", COL_ENTRADA);
					rsm.addRegister(register);
		        }

				//saida
				String scriptTemp = script.substring(0).toLowerCase();
				if (scriptTemp.indexOf("where")==-1)
					scriptTemp += " where 1<>1";
				else
					scriptTemp = scriptTemp.substring(0, scriptTemp.lastIndexOf("where")) + " where 1<>1";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {
					pstmt = connect.prepareStatement(scriptTemp);
					rs = pstmt.executeQuery();
				}
				catch(Exception exception) {
					scriptTemp = script + " AND 1<>1";
					scriptTemp = Pattern.compile("\\B#\\w*\\b").matcher(scriptTemp).replaceAll("0");
					pstmt = connect.prepareStatement(scriptTemp);
					rs = pstmt.executeQuery();
				}
				ResultSetMetaData rsmd = rs.getMetaData();
				for(int i=1; i<=rsmd.getColumnCount(); i++){
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("NM_COLUNA", rsmd.getColumnLabel(i).toUpperCase());
					register.put("TP_COLUNA", COL_SAIDA);
					rsm.addRegister(register);
				}

				return rsm;
			}
			else
				return null;
//			else {
//				HttpServletReques requestTemp = new HttpServletRequest();
//				String className = "";
//				String method = "";
//				String[] tokens = Util.getTokens(script, '.', false);
//				for (int i=0; i<tokens.length-1; i++)
//					className = className + (className.equals("") ? "" : ".") + tokens[i];
//				if (tokens.length>0)
//					method = tokens[tokens.length-1];
//
//				requestTemp.setupGetParameter("className", className);
//				requestTemp.setupGetParameter("method", method.replaceAll("#\\w*", "const 0"));
//				MethodTrigger mt = new MethodTrigger(requestTemp, null, null, true);
//				Object objReturnCall = mt.getObjectResultCall();
//				if (!(objReturnCall instanceof ResultSetMap))
//					return null;
//				else {
//					ResultSetMap rsm = new ResultSetMap();
//					//entrada
//					Matcher matcher = Pattern.compile("#\\w*").matcher(script);
//					while (matcher.find()){
//						HashMap<String, Object> register = new HashMap<String, Object>();
//						register.put("NM_COLUNA", matcher.group().toUpperCase());
//						register.put("TP_COLUNA", COL_ENTRADA);
//						rsm.addRegister(register);
//			        }
//
//					//saida
//					ArrayList<String> columns = ((ResultSetMap)objReturnCall).getColumnNames();
//					for(int i=0; columns!=null && i<columns.size(); i++){
//						HashMap<String, Object> register = new HashMap<String, Object>();
//						register.put("NM_COLUNA", columns.get(i));
//						register.put("TP_COLUNA", COL_SAIDA);
//						rsm.addRegister(register);
//					}
//					return rsm;
//				}
//			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FonteDadosServices.getCamposFonteDados: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap executeFonteDados(int cdFonte) {
		FonteDados fonte = FonteDadosDAO.get(cdFonte);
		return executeFonteDados(fonte.getTxtScript(), null);
	}

	public static ResultSetMap executeFonteDados(String sql) {
		return executeFonteDados(sql, null);
	}

	public static ResultSetMap executeFonteDadosFunc(String func) {
		try {
//			CustomHttpServletRequest requestTemp = new CustomHttpServletRequest();
//			String className = "";
//			String method = "";
//			String[] tokens = Util.getTokens(func, '.', false);
//			for (int i=0; i<tokens.length-1; i++)
//				className = className + (className.equals("") ? "" : ".") + tokens[i];
//			if (tokens.length>0)
//				method = tokens[tokens.length-1];
//
//			requestTemp.setupGetParameter("className", className);
//			requestTemp.setupGetParameter("method", method);
//			MethodTrigger mt = new MethodTrigger(requestTemp, null, null, true);
//			Object objReturnCall = mt.getObjectResultCall();
//			return objReturnCall instanceof ResultSetMap ? (ResultSetMap)objReturnCall : null;
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public static ResultSetMap executeFonteDados(String sql, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement(sql);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FonteDadosServices.executeFonteDados: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFonte) {
		return delete(cdFonte, null);
	}

	public static int delete(int cdFonte, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			PreparedStatement pstmt = connect.prepareStatement("DELETE " +
					"FROM grl_modelo_fonte_dados " +
					"WHERE cd_fonte = ?");
			pstmt.setInt(1, cdFonte);
			pstmt.execute();

			if (FonteDadosDAO.delete(cdFonte, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FonteDadosServices.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getInfoToday() {
		ArrayList<HashMap<String, Object>> columns = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> column = new HashMap<String, Object>();
		column.put("name", "DT_TODAY");
		columns.add(column);
		column = new HashMap<String, Object>();
		column.put("name", "DT_TODAY_EXT");
		columns.add(column);
		ResultSetMap rsm = new ResultSetMap();
		HashMap<String, Object> register = new HashMap<String, Object>();
		GregorianCalendar dtAtual = new GregorianCalendar();
		register.put("DT_TODAY", Util.formatDateTime(dtAtual, "dd/MM/yyyy"));
		register.put("DT_TODAY_EXT", dtAtual.get(Calendar.DAY_OF_MONTH) + " de " +
				Recursos.meses[dtAtual.get(Calendar.MONTH)].toLowerCase() + " de " + dtAtual.get(Calendar.YEAR));
		rsm.addRegister(register);
		return rsm;
	}
}
