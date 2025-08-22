package com.tivic.manager.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.ConfManager;
import sol.util.Result;

/**
 * Gerencia a persistencia de registros em um banco de compliance
 * 
 * @author Maurício, Sapucaia
 */
public class ComplianceManager {
	
	// tipos de ações registradas
	public static final int TP_ACAO_DELETE = 0;
	public static final int TP_ACAO_INSERT = 1;
	public static final int TP_ACAO_UPDATE = 2;
	public static final int TP_ACAO_ANY = 3;
	
	public static final String[] tipoAcao = {"Exclusão", "Cadastro", "Atualização", "Qualquer"};
	
	//poll de conexões
	private static ArrayList<Connection> connectionPool = new ArrayList<Connection>();
	private static final int MAX_CONNECTIONS  = 50;
	
	/**
	 * Cria conexão com o banco de compliance, utilizando pool de conexões.
	 * 
	 * @return {@link Connection}
	 */
	synchronized public static Connection conectar() {
		try {		
			ConfManager conf = Util.getConfManager();
			HashMap<?,?> dbInfo = conf.getResourceOfDbUsed();
			
			if(connectionPool.size() > 0)	{
				Connection connect = connectionPool.get(0);
				connectionPool.remove(0);
				if(connect.isClosed())
					return conectar();
				//
				return connect;
			}

			Class.forName((String)dbInfo.get("DRIVER")).newInstance();
			String dbPath = ParametroServices.getValorOfParametroAsString("DS_COMPLIANCE_DATABASE", null).trim();
			String user = ParametroServices.getValorOfParametroAsString("DS_COMPLIANCE_USER", null).trim();
			String pass = ParametroServices.getValorOfParametroAsString("DS_COMPLIANCE_PASS", null);
			
			LogUtils.debug(dbPath);
			LogUtils.debug(user);
			LogUtils.debug(pass);

			return DriverManager.getConnection(dbPath, user, pass);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	/**
	 * Fecha a conexão com o banco de compliance, utilizando ool de conexões
	 * @param connect Conexão a ser fechada {@link Connection}
	 * 
	 */
	synchronized public static void desconectar(Connection connect){
		try	{
			if(connect!=null && (connectionPool.size() < MAX_CONNECTIONS) && !connect.isClosed() && 
			   connect.getClass().toString().indexOf("Cache")<0 && // Não empilha conexões do mysql e Caché 
			   connect.getClass().toString().toLowerCase().indexOf("mysql")<0)	{
				if(!connect.getAutoCommit())
					connect.rollback();
				connect.setAutoCommit(true);
				boolean found = false;
				for (Connection c : connectionPool) {
					if(c == connect){
						found = true;
					}
				}
				
				if(!found)
					connectionPool.add(connect);
			}
			else if(connect!=null)
				connect.close();
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
		}
	}
	
	/**
	 * 
	 * @param object
	 * @param auth {@link AuthData}
	 * @param tpAcao
	 * @param sourceConnect
	 * @return {@link Result}
	 * 
	 * @see
	 * {@link #process(String, String, Object, AuthData, int, Connection)}
	 */
	public static Result process(Object object, AuthData auth, int tpAcao, Connection sourceConnect) {
		return process(null, null, object, auth, tpAcao, sourceConnect);
	}
	
	/**
	 * 
	 * @param methodName
	 * @param object
	 * @param auth {@link AuthData}
	 * @param tpAcao
	 * @param sourceConnect
	 * @return {@link Result}
	 * 
	 * @see
	 * {@link #process(String, String, Object, AuthData, int, Connection)}
	 */
	public static Result process(String methodName, Object object, AuthData auth, int tpAcao, Connection sourceConnect) {
		return process(null, methodName, object, auth, tpAcao, sourceConnect);
	}
	
	/**
	 * Processa os dados necessários antes de sarvar o registro no compliance
	 * 
	 * @param className Nome da Classe onde está o método para salvar o registro
	 * @param methodName Método que salva o registro
	 * @param object Objeto que será salvo
	 * @param auth Objeto com dados do usuário {@link AuthData}
	 * @param tpAcao Tipo da Ação realizada
	 * @param sourceConnect Conexão do banco de origem
	 * @return {@link Result}
	 *
	 * @see
	 * {@link #process(Object, AuthData, int, Connection)}
	 * {@link #process(String, Object, AuthData, int, Connection)}
	 * 
	 */
	public static Result process(String className, String methodName, Object object, AuthData auth, int tpAcao, Connection sourceConnect) {
		Connection connect = null;
		try {
			boolean lgCompliance = ParametroServices.getValorOfParametroAsInteger("LG_COMPLIANCE", 0, 0, sourceConnect)>0;
			if(!lgCompliance) {
				return new Result(0, "Compliance desabilitado.");
			}
			
			connect = conectar();
			
			if(connect==null)
				return new Result(-1, "Não foi possível estabelecer conexão com o compliance.");
			
			Object objectReturn = null;
			
			if(methodName==null) {
				objectReturn = save(object, auth, tpAcao, connect);
			}
			else {
				if(className==null)
					className = object.getClass().getName()+"Services";
				
				String classObject = object.getClass().getName();
				
				Object[] params = {object, auth, tpAcao, connect};
				Method method = Class.forName(className).getDeclaredMethod(methodName, new Class[]{Class.forName(classObject), AuthData.class, int.class, Connection.class});
				
				objectReturn = method.invoke(Class.forName(className), params);
				if(objectReturn==null)
					return new Result(-2, "Erro ao registrar informação no compliance.");
			}
			
			return (Result)objectReturn;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			desconectar(connect);
		}
	}
	
	/**
	 * Save genérico de compliance para qualquer tabela
	 * @param object
	 * @param auth {@link AuthData}
	 * @param tpAcao
	 * @param connect
	 * @return {@link Result}
	 * 
	 */
	private static Result save(Object object, AuthData authData, int tpAcao, Connection connect) {
		try {
			/*
			 * 1. Verificar a existencia da tabela no banco de compliance
			 * 2. Buscar os campos na tabela de compliance
			 * 3. Criar SQL de INSERT baseado nos campos da tabela e nos campos do objeto
			 * 4. Executar INSERT 		
			 */
			
			String className = object.getClass().getName();
			String[] parts = className.split("\\.");
			
			String prefix = parts[parts.length-2].toLowerCase();
			className = parts[parts.length-1];
			
			String tableName = prefix + DeveloperServices.camelCaseToUnderscore(className);
			
			//1.
			if(!DeveloperServices.hasTable(tableName, connect)) {
				return new Result(-1, "Tabela não existe no banco de compliance.");
			}
			
			//2.
			ArrayList<String> tableFields = DeveloperServices.getFields(tableName, connect);
			Field[] objectFields =  object.getClass().getDeclaredFields();
			
			//3.
			ArrayList<HashMap<String, Object>> finalFields = new ArrayList<HashMap<String,Object>>();
			
			for (Field attribute : objectFields) {
				String fieldName = DeveloperServices.camelCaseToUnderscore(attribute.getName());
				if(DeveloperServices.hasField(tableFields, fieldName)) {
					
					HashMap<String, Object> f = new HashMap<>();
					
					f.put("FIELD_NAME", fieldName);
					f.put("ATTRIBUTE", attribute);
					
					finalFields.add(f);
				}
			}
			
			String sqlFields = "";
			String sqlMarkers = "";
			
			for (HashMap<String, Object> f : finalFields) {
				if(!sqlFields.equals(""))
					sqlFields+=", ";
				
				sqlFields+= f.get("FIELD_NAME");
				
				if(!sqlMarkers.equals(""))
					sqlMarkers+=", ";
				
				sqlMarkers+="?";				
			}
			
			//CAMPOS PADRAO COMPLIANCE
			sqlFields+= ", CD_USUARIO_COMPLIANCE, DT_COMPLIANCE, TP_ACAO_COMPLIANCE";
			sqlMarkers+=", ?, ?, ?";
			
			//SQL
			String sql = "INSERT INTO "+tableName+"(" + 
					sqlFields +
				  ") VALUES ("+ 
				  	sqlMarkers +
				  ")";
			
			LogUtils.debug(sql);
			
			//4.
			PreparedStatement pstmt = connect.prepareStatement(sql);
			
			for (int i = 0; i < finalFields.size(); i++) {
				Field attribute = (Field)finalFields.get(i).get("ATTRIBUTE");
				attribute.setAccessible(true);
				Object value = attribute.get(object);
				
				if (attribute.getType()==String.class) 
					pstmt.setString(i+1, (String)value);
				else if (attribute.getType()==Double.class || attribute.getType()==double.class) 
					pstmt.setDouble(i+1, (Double)value);
				else if (attribute.getType()==Float.class || attribute.getType()==float.class) 
					pstmt.setFloat(i+1, (Float)value);
				else if (attribute.getType()==Integer.class || attribute.getType()==int.class) 
					pstmt.setInt(i+1, (Integer)value);
				else if(attribute.getType()==GregorianCalendar.class) {
					if(value==null || value.equals(null))
						pstmt.setNull(i+1, Types.TIMESTAMP);
					else
						pstmt.setTimestamp(i+1, new Timestamp(((GregorianCalendar)value).getTimeInMillis()));
				}
				else if (attribute.getType()==byte[].class) {
					if(value==null || value.equals(null))
						pstmt.setNull(i+1, Types.BINARY);
					else
						pstmt.setBytes(i+1, (byte[])value);
				}
				else 
					pstmt.setString(i+1, String.valueOf(value));
			}
			
			//SET DE CAMPOS PADRAO COMPLIANCE
			if(authData==null)
				pstmt.setNull(finalFields.size()+1, Types.INTEGER);
			else
				pstmt.setInt(finalFields.size()+1, authData.getUsuario().getCdUsuario());
			pstmt.setTimestamp(finalFields.size()+2, new Timestamp(System.currentTimeMillis()));
			pstmt.setInt(finalFields.size()+3, tpAcao);
			
			LogUtils.debug(pstmt.toString());
			
			int retorno = pstmt.executeUpdate();
			
//			System.out.println("\tretorno: "+retorno);
			
			return  new Result(retorno < 0 ? retorno : 1, (retorno < 0)?"Erro ao salvar compliance...":"Compliance salvo com sucesso...");
		}
		catch(Exception e){
			e.printStackTrace();
			return new Result(-1, e.getMessage());
		}
	}

	/**
	 * 
	 * @param query
	 * @return
	 */
	public static ResultSetMap search(String query) {
		Connection connect = conectar();
		try {
			PreparedStatement pstmt = connect.prepareStatement(query);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			try {
				connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//TODO:
	public static ResultSetMap find(String module, String entity, int code) {
		Connection connect = conectar();
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM "+module+"_"+entity+" WHERE cd_"+entity+"="+code);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			try {
				connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//TODO:
	public static Result restoreRegister() {
		return null;
	}
}
