package com.tivic.manager.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.util.Result;

public class DeveloperServices {

	/**
	 * CRIA OS CODIGOS FONTE PARA AS CLASSES VO, DAO, Services, AS3 e JS
	 * @param nmTable
	 * @param nmPackage
	 * @param nmClass
	 * @param nmTableSuper
	 * @param nmClassSuper
	 * @param nmClassConnection
	 * @return
	 */
	public static Result getClassSources(String nmTable, String nmPackage, String nmClass, String nmTableSuper, String nmClassSuper, String nmClassConnection, boolean lgCompliance){
		return getClassSources(nmTable, nmPackage, nmClass, nmTableSuper, nmClassSuper, nmClassConnection, lgCompliance, null);
	}
	
	public static Result getClassSources(String nmTable, String nmPackage, String nmClass, String nmTableSuper, String nmClassSuper, String nmClassConnection, boolean lgCompliance, Connection connect){
		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			String sourceVO = getVOClass(nmTable, nmClass, nmTableSuper, nmClassSuper, nmPackage, connect);
			String sourceDAO = getDAOClass(nmTable, nmClass, nmTableSuper, nmClassSuper, nmPackage, nmClassConnection, connect);
			String sourceServices = getServicesClass(nmTable, nmClass, nmPackage, nmClassConnection, lgCompliance, connect);
			String sourceAS3 = getAS3Class(nmTable, nmClass, nmTableSuper, nmClassSuper, nmPackage, connect);
			String sourceTS = getTSClass(nmTable, nmClass, nmTableSuper, nmClassSuper, nmPackage, connect);
			String sourceTSServices = getTsServicesClass(nmClass, nmPackage, connect);
			String sourceJS = "";
			String sourceRest = getRestClass(nmTable, nmClass, nmPackage, nmClassConnection, lgCompliance, connect);
			
			Result result = new Result(1, "Classes geradas com sucesso.");
			result.addObject("SOURCE_VO", sourceVO);
			result.addObject("SOURCE_DAO", sourceDAO);
			result.addObject("SOURCE_SERVICES", sourceServices);
			result.addObject("SOURCE_AS3", sourceAS3);
			result.addObject("SOURCE_JS", sourceJS);
			result.addObject("SOURCE_TS", sourceTS);
			result.addObject("SOURCE_TS_SERVICES", sourceTSServices);
			result.addObject("SOURCE_REST", sourceRest);
			
			return result;
		}
	    catch(Exception e) {
	      e.printStackTrace(System.out);
	      System.err.println("Erro! getClassSources: " + e);
	      return new Result(-1, "Erro ao gerar código fonte das classes.");
	    }
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * CRIA UMA CLASSE DE VALOR DE OBJETO (VO) PARA A TABELA INDICADA
	 * @param nmTable
	 * @param nmClass
	 * @param nmTableSuper
	 * @param nmClassSuper
	 * @param nmPackage
	 * @return
	 */
	public static String getVOClass(String nmTable, String nmClass, String nmTableSuper, String nmClassSuper, String nmPackage){
		return getVOClass(nmTable, nmClass, nmTableSuper, nmClassSuper, nmPackage, null);
	}
	
	@SuppressWarnings("unchecked")
	public static String getVOClass(String nmTable, String nmClass, String nmTableSuper, String nmClassSuper, String nmPackage, Connection connect){
		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			HashMap<String, String> convertTypes = (HashMap<String, String>) getTypeMaps().getObjects().get("JAVA_TYPES");
			
		    ArrayList<String> fields = getFields(nmTable, connect);
		    HashMap<String, String> types = getFieldTypes(nmTable, connect);
		    ArrayList<String> primaryKeys = getPrimaryKeys(nmTable, connect);
		    ArrayList<String> fieldsSuper = null;
		    HashMap<String, String> typesSuper = null;
		    ArrayList<String> primaryKeysSuper = null;
	
		    if(!nmTableSuper.equals("")){
			    fieldsSuper = getFields(nmTableSuper, connect);
			    typesSuper = getFieldTypes(nmTableSuper, connect);
			    primaryKeysSuper = getPrimaryKeys(nmTableSuper, connect);
		    }
	
		    String source;
		    
	      //gerando codigo
	      source=(nmPackage.equals(""))?"":"package "+nmPackage+";\n\n";
	      
	      if (hasDateFields(nmTable, connect) || (!nmTableSuper.equals("") && hasDateFields(nmTableSuper, connect)))
	    	  source+="import java.util.GregorianCalendar;\n\n";
	      
	      source+="public class "+nmClass+ ((!nmClassSuper.equals(""))?" extends " + nmClassSuper:"") + " {\n\n";

	      //variaveis
	      for(int i=0;i<fields.size();i++) {
	      	if(!Util.existsInArray(fieldsSuper, fields.get(i))) {
	      		String type = convertTypes.get((String) types.get((String) fields.get(i))).toString();
	      		source+="\tprivate " + type + " " + getVariableNameByFieldName((String) fields.get(i)) +  ";\n";
	      		
	      	}
	      }
	      
	      source+="\n\tpublic "+ nmClass +"() { }\n";
	      
	      
	      source+="\n\tpublic "+ nmClass +"(";

	      //campos classe pai
	      if(!nmClassSuper.equals("")){
		      for(int i=0;i<fieldsSuper.size();i++){
				if(i!=0)
					source+=",\n\t\t\t";
				source+= convertTypes.get((String)typesSuper.get((String) fieldsSuper.get(i))) + " " + getVariableNameByFieldName((String) fieldsSuper.get(i));
			  }
		  }
	      //campos classe
	      for(int i=(!nmClassSuper.equals("") ? primaryKeys.size() : 0);i<fields.size();i++){
	      	if(!Util.existsInArray(fieldsSuper, fields.get(i))){
		      	if(i!=0)
		      		source+=",\n\t\t\t";
		      	String type = convertTypes.get((String) types.get((String) fields.get(i))).toString();
		      	source+=type + " " + getVariableNameByFieldName((String) fields.get(i));
		    }
	      }
	      source+=") {\n";
	      //super
	      if(!nmClassSuper.equals("")){
	      	source+="\t\tsuper(";
			for(int i=0;i<fieldsSuper.size();i++){
				if(i!=0)
					source+=",\n\t\t\t";
				source+=getVariableNameByFieldName((String) fieldsSuper.get(i));
			}
			source+=");\n";
	      }

	      int countKeysSuper = primaryKeysSuper!=null ? primaryKeysSuper.size() : 0;
	      for(int i=0;i<fields.size();i++)
	      	if(!Util.existsInArray(fieldsSuper, fields.get(i)))
	      		source+="\t\tset" + capitular(getVariableNameByFieldName((String) fields.get(i))) +  "("+getVariableNameByFieldName(countKeysSuper>0 && i<countKeysSuper ? (String)fieldsSuper.get(i) : (String)fields.get(i))+");\n";
		  source+="\t}\n";

		  for(int i=0;i<fields.size();i++){
		  	if(!Util.existsInArray(fieldsSuper, fields.get(i))){
		      	String type = convertTypes.get((String) types.get((String) fields.get(i))).toString();
		      	source+="\tpublic void set" + capitular(getVariableNameByFieldName((String) fields.get(i))) +  "("+ type + " " +getVariableNameByFieldName((String) fields.get(i))+"){\n";
		      	source+="\t\tthis."+getVariableNameByFieldName((String) fields.get(i)) +  "="+getVariableNameByFieldName((String) fields.get(i))+";\n\t}\n";
		      	source+="\tpublic "+ type +" get" + capitular(getVariableNameByFieldName((String) fields.get(i))) +  "(){\n";
		      	source+="\t\treturn this."+getVariableNameByFieldName((String) fields.get(i))+";\n\t}\n";
		    }
		  }
		  
		  source+="\tpublic String toString() {\n\t\tString valueToString = \"\";\n";
		  for(int i=0;i<fields.size();i++){
			  String type = (String)convertTypes.get((String) types.get((String) fields.get(i)));
			  if (type!=null && type.equalsIgnoreCase("GregorianCalendar"))
				  source += "\t\tvalueToString += \"" + (i!=0 ? ", " : "") + getVariableNameByFieldName((String) fields.get(i)) + ": \" + " + " sol.util.Util.formatDateTime(get" + capitular(getVariableNameByFieldName((String) fields.get(i))) +  "(), \"dd/MM/yyyy HH:mm:ss:SSS\", \"\");\n";	
			  else
				  source += "\t\tvalueToString += \"" + (i!=0 ? ", " : "") + getVariableNameByFieldName((String) fields.get(i)) + ": \" + " + " get" + capitular(getVariableNameByFieldName((String) fields.get(i))) +  "();\n";	
		  }
		  source+="\t\treturn \"{\" + valueToString + \"}\";\n\t}\n\n";
		  source+="\tpublic Object clone() {\n";
		  source+="\t\treturn new " + nmClass + "(";
	      if(!nmClassSuper.equals("")){
		      for(int i=0;i<fieldsSuper.size();i++){
				if(i!=0)
					source+=",\n\t\t\t";
				source+= "get" + capitular(getVariableNameByFieldName((String) fieldsSuper.get(i))) + "()";
			  }
		  }
		  for(int i=(!nmClassSuper.equals("") ? primaryKeys.size() : 0);i<fields.size();i++){
			  if(fieldsSuper!=null && Util.existsInArray(fieldsSuper, fields.get(i)))
				  continue;
			  source += (i>0 && i<fields.size() ? ",\n\t\t\t" : "");
			  String type = (String)convertTypes.get((String) types.get((String) fields.get(i)));
			  if (type!=null && type.equalsIgnoreCase("GregorianCalendar"))
				  source += "get" + capitular(getVariableNameByFieldName((String) fields.get(i))) + "()==null ? null : (GregorianCalendar)get" + capitular(getVariableNameByFieldName((String) fields.get(i))) + "().clone()";
			  else
				  source += "get" + capitular(getVariableNameByFieldName((String) fields.get(i))) + "()";
		  }
		  source+=");\n\t}\n\n";
		  
		  source+="}";

	      return source;
	    }
	    catch(Exception e) {
	      e.printStackTrace(System.out);
	      System.err.println("Erro! gerar: " + e);
	      return null;
	    }
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * CRIA UMA CLASSE DE PERSISTENCIA (DAO) PARA A TABELA INDICADA
	 * @param nmTable
	 * @param nmClass
	 * @param nmTableSuper
	 * @param nmClassSuper
	 * @param nmPackage
	 * @param nmClassConnection
	 * @return
	 */
	public static String getDAOClass(String nmTable, String nmClass, String nmTableSuper, String nmClassSuper, String nmPackage, String nmClassConnection){
		return getDAOClass(nmTable, nmClass, nmTableSuper, nmClassSuper, nmPackage, nmClassConnection, null);
	}
	
	@SuppressWarnings("unchecked")
	public static String getDAOClass(String nmTable, String nmClass, String nmTableSuper, String nmClassSuper, String nmPackage, String nmClassConnection, Connection connect){
		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			HashMap<String, String> convertTypes = (HashMap<String, String>) getTypeMaps().getObjects().get("JAVA_TYPES");
			
		    ArrayList<String> fields = getFields(nmTable, connect);
		    HashMap<String, String> types = getFieldTypes(nmTable, connect);
		    ArrayList<String> foreignKeys = getForeignKeys(nmTable, connect);
		    ArrayList<String> primaryKeys = getPrimaryKeys(nmTable, connect);
		    ArrayList<String> nativePrimaryKeys = getNativePrimaryKeys(nmTable, connect);
		    ArrayList<String> importedPrimaryKeys = getImportedPrimaryKeys(nmTable, connect);
		    
		    ArrayList<String> fieldsSuper = null;
		    ArrayList<String> primaryKeysSuper = null;
		    ArrayList<String> nativePrimaryKeysSuper = null;
		    HashMap<String, String> typesSuper = null;
	
		    if(!nmTableSuper.equals("")){
			    fieldsSuper = getFields(nmTableSuper, connect);
			    typesSuper = getFieldTypes(nmTableSuper, connect);
			    primaryKeysSuper = getPrimaryKeys(nmTableSuper, connect);
			    nativePrimaryKeysSuper = getNativePrimaryKeys(nmTableSuper, connect);
		    }
	
			String source;

			//gerando codigo
			source=(nmPackage.equals(""))?"":"package "+nmPackage+";\n\n";

			source+="import java.sql.*;\n";
			source+="import " + nmClassConnection + ";\n";
			source+="import sol.dao.ResultSetMap;\n";
			source+="import sol.dao.ItemComparator;\n";
			source+="import sol.dao.Search;\n";
			if (hasDateFields(nmTable, connect) || (!nmTableSuper.equals("") && hasDateFields(nmTableSuper, connect)))	
				source+="import sol.dao.Util;\n";
			if (importedPrimaryKeys!=null && importedPrimaryKeys.size()>0 && nmClassSuper.equals("") && nativePrimaryKeys.size()>0)
				source+="import java.util.HashMap;\n";
			source+="import java.util.ArrayList;\n";
			source+="\npublic class "+nmClass+"DAO{\n\n";
			
			//metodo insert
			source+="\tpublic static int insert("+nmClass+" objeto) {\n\t\treturn insert(objeto, null);\n\t}\n\n";
			source+="\tpublic static int insert("+nmClass+" objeto, Connection connect){\n\t\t";
			source+="boolean isConnectionNull = connect==null;\n\t\t";
			source+="try {";
			if (!nmClassSuper.equals(""))
				source+="\n\t\t\tif (isConnectionNull) {\n\t\t\t\tconnect = Conexao.conectar();\n\t\t\t\tconnect.setAutoCommit(false);\n\t\t\t}\n\t\t\t";
			else
				source+="\n\t\t\tif (isConnectionNull)\n\t\t\t\tconnect = Conexao.conectar();\n\t\t\t";
			boolean notSetPrimaryKey = false;
			int nrNativeIntegerKeys = 0; 
			String sourcetemp = "";
			if (nativePrimaryKeys.size() > 0 || !nmTableSuper.equals("")) {		
				if (!nmTableSuper.equals("")) {
					sourcetemp+="int code = " + nmClassSuper + "DAO.insert(objeto, connect);\n\t\t\t";
				}
				else {
					if (importedPrimaryKeys==null || importedPrimaryKeys.size()==0)
						sourcetemp+="int code = Conexao.getSequenceCode(\"" + nmTable + "\", connect);\n\t\t\t";
					else {
						sourcetemp+="HashMap<String,Object>[] keys = new HashMap[" + primaryKeys.size() + "];\n\t\t\t";
						for (int i=0; i<primaryKeys.size(); i++) {
							
							System.out.println((String)primaryKeys.get(i));
				      		System.out.println((String)types.get((String)primaryKeys.get(i)));
				      		
							String type = (String)convertTypes.get(((String)types.get((String)primaryKeys.get(i))).toUpperCase());
							sourcetemp += "keys[" + i + "] = new HashMap<String,Object>();\n\t\t\t";
							sourcetemp += "keys[" + i + "].put(\"FIELD_NAME\", \"" + primaryKeys.get(i) + "\");\n\t\t\t";
							sourcetemp += "keys[" + i + "].put(\"IS_KEY_NATIVE\", \"" + (hasField(importedPrimaryKeys, (String)primaryKeys.get(i)) ? "NO" : "YES") + "\");\n\t\t\t";
							if (hasField(importedPrimaryKeys, (String)primaryKeys.get(i)))
								sourcetemp += "keys[" + i + "].put(\"FIELD_VALUE\", new Integer(objeto.get" + capitular(getVariableNameByFieldName((String)primaryKeys.get(i))) + "()));\n\t\t\t";
							else if (type.toLowerCase().equals("int"))
								nrNativeIntegerKeys++;
						}
						if (nrNativeIntegerKeys==0)
							notSetPrimaryKey = true;
						else
							sourcetemp+="int code = Conexao.getSequenceCode(\"" + nmTable + "\", keys, connect);\n\t\t\t";
					}				
				}
				if (!notSetPrimaryKey) {
					sourcetemp+="if (code <= 0) {\n\t\t\t\tif (isConnectionNull)\n\t\t\t\t\t" + nmClassConnection + ".rollback(connect);\n\t\t\t\treturn -1;\n\t\t\t}\n\t\t\t";
					if (!nmTableSuper.equals("")) {
						if (nativePrimaryKeysSuper!=null && nativePrimaryKeysSuper.size() > 0)
							sourcetemp+="objeto.set" + capitular(getVariableNameByFieldName((String)nativePrimaryKeysSuper.get(0))) + "(code);\n\t\t\t";
					}
					else {
						if (nativePrimaryKeys!=null && nativePrimaryKeys.size() > 0)
							sourcetemp+="objeto.set" + capitular(getVariableNameByFieldName((String)nativePrimaryKeys.get(0))) + "(code);\n\t\t\t";				
					}
				}
			}
			if (!notSetPrimaryKey)
				source += sourcetemp;
			source+="PreparedStatement pstmt = connect.prepareStatement(\"INSERT INTO " + nmTable+" (";
			for(int i=0;i<fields.size();i++){
				if(i!=0)
					source+=",\"+\n\t\t\t                                  \"";
				source+=(String) fields.get(i);
			}
			source+=") VALUES (";
			for(int i=0;i<fields.size();i++){
				if(i!=0)
					source+=", ";
				source+="?";
			}
			source+=")\");\n\t\t\t";

			for(int i=0;i<fields.size();i++){
				String type = (String)convertTypes.get(((String)types.get((String)fields.get(i))).toUpperCase());
				if (type == null)
					System.out.println("Erro na tabela " + nmTable + " na coluna " + fields.get(i) + " de tipo " + types.get((String) fields.get(i)));

				boolean isNativePrimaryKey = hasField(nativePrimaryKeys, (String)fields.get(i));
				boolean isPrimaryKey = hasField(primaryKeys, (String)fields.get(i));

				String varName = isPrimaryKey && !isNativePrimaryKey && !nmClassSuper.equals("") ? getVariableNameByFieldName((String)fieldsSuper.get(i)) : getVariableNameByFieldName((String) fields.get(i));
				
				if(isNativePrimaryKey)
					source+="pstmt.set" + capitular(type) + "(" + (i+1) + ", " + (notSetPrimaryKey ? "objeto.get" + capitular(varName) + "()" : "code") + ");\n\t\t\t";
				else{
					if(type!=null && type.equals("GregorianCalendar"))
						source+="if(objeto.get" + capitular(varName) + "()==null)\n\t\t\t\tpstmt.setNull("+(i+1)+", Types.TIMESTAMP);\n\t\t\telse\n\t\t\t\tpstmt.setTimestamp("+(i+1)+",new Timestamp(objeto.get" + capitular(varName) + "().getTimeInMillis()));\n\t\t\t";
					else if(type!=null && type.equals("int") && Util.existsInArray(foreignKeys, (String) fields.get(i)))
						source+="if(objeto.get" + capitular(varName) + "()==0)\n\t\t\t\tpstmt.setNull("+(i+1)+", Types.INTEGER);\n\t\t\telse\n\t\t\t\tpstmt.setInt("+(i+1)+",objeto.get" + capitular(varName) + "());\n\t\t\t";
					else if (type.equalsIgnoreCase("byte[]"))
						source+="if(objeto.get" + capitular(varName) + "()==null)\n\t\t\t\tpstmt.setNull("+(i+1)+", Types.BINARY);\n\t\t\telse\n\t\t\t\tpstmt.setBytes("+(i+1)+",objeto.get" + capitular(varName) + "());\n\t\t\t";
					else
						source+="pstmt.set"+capitular(type)+"("+(i+1)+",objeto.get" + capitular(varName) + "());\n\t\t\t";
				}
			}

			source+="pstmt.executeUpdate();\n\t\t\t";
			if (!nmClassSuper.equals(""))
				source+="if (isConnectionNull)\n\t\t\t\tconnect.commit();\n\t\t\t";
			source+="return " + ((nativePrimaryKeys.size()==0 || notSetPrimaryKey) && nmTableSuper.equals("") ? "1" : "code") + ";\n\t\t";
			source+="}\n\t\t";
			source+="catch(SQLException sqlExpt){\n\t\t\t";
			source+="sqlExpt.printStackTrace(System.out);\n\t\t\t";
			source+="System.err.println(\"Erro! "+ nmClass +"DAO.insert: \" + sqlExpt);\n\t\t\t";
			if (!nmClassSuper.equals(""))
				source+="if (isConnectionNull)\n\t\t\t\tConexao.rollback(connect);\n\t\t\t";
			source+="return (-1)*sqlExpt.getErrorCode();\n\t\t";
			source+="}\n\t\t";
			source+="catch(Exception e){\n\t\t\t";
			source+="e.printStackTrace(System.out);\n\t\t\t";
			source+="System.err.println(\"Erro! "+ nmClass +"DAO.insert: \" +  e);\n\t\t\t";
			if (!nmClassSuper.equals(""))
				source+="if (isConnectionNull)\n\t\t\t\tConexao.rollback(connect);\n\t\t\t";
			source+="return -1;\n\t\t";
			source+="}\n\t\t";
			source+="finally{\n\t\t\t";
			source+="if (isConnectionNull)\n\t\t\t\tConexao.desconectar(connect);\n\t\t";
			source+="}\n\t";
			source+="}\n";

			//metodo update
			source+="\n\tpublic static int update("+nmClass+" objeto) {\n\t\treturn update(objeto";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++) {
				if(types.get((String)primaryKeys.get(i))==null)
					continue;
				System.out.println(primaryKeys.get(i)+": "+types.get((String)primaryKeys.get(i)));
				String type = (String)convertTypes.get(((String)types.get((String)primaryKeys.get(i))).toUpperCase());
				String valueDefualt = type.toLowerCase().equals("string") ? "null" : "0";
				source += ", " + valueDefualt;							
			}
			source+=", null);\n\t}\n";
			source+="\n\tpublic static int update("+nmClass+" objeto";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
				source += ", " + (String)convertTypes.get((String) types.get((String)primaryKeys.get(i)))+" "+ getVariableNameByFieldName((String)primaryKeys.get(i)) + "Old";			
			source+=") {\n\t\treturn update(objeto";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
				source += ", " + getVariableNameByFieldName((String)primaryKeys.get(i)) + "Old";			
			source+=", null);\n\t}\n";
			source+="\n\tpublic static int update("+nmClass+" objeto, Connection connect) {\n\t\treturn update(objeto";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++) {
				if(types.get((String)primaryKeys.get(i))==null)
					continue;
				String type = (String)convertTypes.get(((String)types.get((String)primaryKeys.get(i))).toUpperCase());
				String valueDefualt = type.toLowerCase().equals("string") ? "null" : "0";
				source += ", " + valueDefualt;			
			}
			source+=", connect);\n\t}\n";
			source+="\n\tpublic static int update("+nmClass+" objeto";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
				source += ", " + (String)convertTypes.get((String) types.get((String)primaryKeys.get(i)))+" "+ getVariableNameByFieldName((String)primaryKeys.get(i)) + "Old";			
			source+=", Connection connect){\n\t\t";
			source+="boolean isConnectionNull = connect==null;\n\t\t";
			source+="try {";
			if (!nmClassSuper.equals(""))
				source+="\n\t\t\tif (isConnectionNull) {\n\t\t\t\tconnect = Conexao.conectar();\n\t\t\t\tconnect.setAutoCommit(false);\n\t\t\t}\n\t\t\t";
			else
				source+="\n\t\t\tif (isConnectionNull)\n\t\t\t\tconnect = Conexao.conectar();\n\t\t\t";
			if (!nmClassSuper.equals("")) {
				source+="PreparedStatement pstmt = null;";
				source+="\n\t\t\t" + nmClass + " objetoTemp = get(";
				for (int i=0; primaryKeysSuper!=null && i<primaryKeysSuper.size(); i++)
					source+=(i!=0 ? ", " : "") + "objeto.get" + capitular(getVariableNameByFieldName((String)primaryKeysSuper.get(i))) + "()";
				source+=", connect);";
				source+="\n\t\t\tif (objetoTemp == null) ";				
				source+="\n\t\t\t\tpstmt = connect.prepareStatement(\"INSERT INTO " + nmTable+" (";
				for(int i=0;i<fields.size();i++){
					if(i!=0)
						source+=",\"+\n\t\t\t                                  \"";
					source+=(String) fields.get(i);						
				}
				source+=") VALUES (";
				for(int i=0;i<fields.size();i++){
					if(i!=0)
						source+=", ";
					source+="?";
				}
				source+=")\");";
				source+="\n\t\t\telse";
				source+="\n\t\t\t\t";
			}
			else
				source+="PreparedStatement ";
			source+="pstmt = connect.prepareStatement(\"UPDATE " + nmTable+" SET ";
			for(int i=0;i<fields.size();i++){
				if(i != 0)
					source+=",\"+\n\t\t\t									      		   \"";
				source+=(String) fields.get(i)+"=?";
			}
			source+=" WHERE ";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++) {
				if (i != 0)
					source += " AND ";
				source += (String)primaryKeys.get(i) + "=?";
			}
			source+="\");\n\t\t\t";

			int nrField = 0;
			for(int i=0;i<fields.size();i++){
				boolean isPrimaryKey = hasField(primaryKeys, (String)fields.get(i));
				nrField++;
				String type = (String)convertTypes.get((String) types.get((String) fields.get(i)));
				boolean isNativePrimaryKey = hasField(nativePrimaryKeys, (String)fields.get(i));
				String varName = isPrimaryKey && !isNativePrimaryKey && !nmClassSuper.equals("") ? getVariableNameByFieldName((String)fieldsSuper.get(i)) : getVariableNameByFieldName((String) fields.get(i));
				if(!isPrimaryKey && type.equals("GregorianCalendar"))
					source+="if(objeto.get" + capitular(varName) + "()==null)\n\t\t\t\tpstmt.setNull("+nrField+", Types.TIMESTAMP);\n\t\t\telse\n\t\t\t\tpstmt.setTimestamp("+nrField+",new Timestamp(objeto.get" + capitular(varName) + "().getTimeInMillis()));\n\t\t\t";
				else if(!isPrimaryKey && type.equals("int") && Util.existsInArray(foreignKeys, (String) fields.get(i)))
					source+="if(objeto.get" + capitular(varName) + "()==0)\n\t\t\t\tpstmt.setNull("+nrField+", Types.INTEGER);\n\t\t\telse\n\t\t\t\tpstmt.setInt("+nrField+",objeto.get" + capitular(varName) + "());\n\t\t\t";
				else if (!isPrimaryKey && type.equalsIgnoreCase("byte[]"))
					source+="if(objeto.get" + capitular(varName) + "()==null)\n\t\t\t\tpstmt.setNull("+nrField+", Types.BINARY);\n\t\t\telse\n\t\t\t\tpstmt.setBytes("+nrField+",objeto.get" + capitular(varName) + "());\n\t\t\t";
				else
					source+="pstmt.set"+capitular(type)+"("+nrField+",objeto.get" + capitular(varName) + "());\n\t\t\t";
			}

			if (!nmClassSuper.equals("")) {
				source+="if (objetoTemp != null) {\n\t\t\t";						
			}		
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++) {
				if(types.get((String)primaryKeys.get(i))==null)
					continue;
				
				String type = (String)convertTypes.get(((String)types.get((String)primaryKeys.get(i))).toUpperCase());
				String valueDefualt = type.toLowerCase().equals("string") ? "null" : "0";
				source+=(!nmClassSuper.equals("") ? "\t" : "") + "pstmt.set"+capitular((String)convertTypes.get((String) types.get((String) fields.get(i))))+"("+(++nrField)+", " + getVariableNameByFieldName((String)primaryKeys.get(i)) + "Old!=" + valueDefualt + " ? " + getVariableNameByFieldName((String)primaryKeys.get(i)) + "Old : objeto.get" + capitular(getVariableNameByFieldName(!nmClassSuper.equals("") ? (String)primaryKeysSuper.get(i) : (String)primaryKeys.get(i))) + "());\n\t\t\t";				
			}
			if (!nmClassSuper.equals("")) {
				source+="}\n\t\t\t";						
			}

			source+="pstmt.executeUpdate();\n\t\t\t";
			
			if (!nmTableSuper.equals("")) {
				source+="if (" + nmClassSuper + "DAO.update(objeto, connect)<=0) {\n\t\t\t\t";
				source+="if (isConnectionNull)\n\t\t\t\t\tConexao.rollback(connect);\n\t\t\t\treturn -1;\n\t\t\t}\n\t\t\t";				
			}			
			
			if (!nmClassSuper.equals(""))
				source+="if (isConnectionNull)\n\t\t\t\tconnect.commit();\n\t\t\t";
			source+="return 1;\n\t\t";
			source+="}\n\t\t";
			source+="catch(SQLException sqlExpt){\n\t\t\t";
			source+="sqlExpt.printStackTrace(System.out);\n\t\t\t";
			source+="System.err.println(\"Erro! "+ nmClass +"DAO.update: \" + sqlExpt);\n\t\t\t";
			if (!nmClassSuper.equals(""))
				source+="if (isConnectionNull)\n\t\t\t\tConexao.rollback(connect);\n\t\t\t";
			source+="return (-1)*sqlExpt.getErrorCode();\n\t\t";
			source+="}\n\t\t";
			source+="catch(Exception e){\n\t\t\t";
			source+="e.printStackTrace(System.out);\n\t\t\t";
			source+="System.err.println(\"Erro! "+ nmClass +"DAO.update: \" +  e);\n\t\t\t";
			if (!nmClassSuper.equals(""))
				source+="if (isConnectionNull)\n\t\t\t\tConexao.rollback(connect);\n\t\t\t";
			source+="return -1;\n\t\t";
			source+="}\n\t\t";
			source+="finally{\n\t\t\t";
			source+="if (isConnectionNull)\n\t\t\t\tConexao.desconectar(connect);\n\t\t";
			source+="}\n\t";
			source+="}\n";

			//metodo delete
			source+="\n\tpublic static int delete(";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
				source += (i!=0 ? ", " : "") + (String)convertTypes.get((String) types.get((String)primaryKeys.get(i)))+" "+ getVariableNameByFieldName((String)primaryKeys.get(i));
			source+=") {\n\t\treturn delete(";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
				source += (i!=0 ? ", " : "") + getVariableNameByFieldName((String)primaryKeys.get(i));
			source+=", null);\n\t}\n";
			source+="\n\tpublic static int delete(";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
				source += (i!=0 ? ", " : "") + (String)convertTypes.get((String) types.get((String)primaryKeys.get(i)))+" "+ getVariableNameByFieldName((String)primaryKeys.get(i));
			source+=", Connection connect){\n\t\t";
			source+="boolean isConnectionNull = connect==null;\n\t\t";
			source+="try {";
			if (!nmClassSuper.equals(""))
				source+="\n\t\t\tif (isConnectionNull) {\n\t\t\t\tconnect = Conexao.conectar();\n\t\t\t\tconnect.setAutoCommit(false);\n\t\t\t}\n\t\t\t";
			else
				source+="\n\t\t\tif (isConnectionNull)\n\t\t\t\tconnect = Conexao.conectar();\n\t\t\t";

			source+="PreparedStatement pstmt = connect.prepareStatement(\"DELETE FROM " + nmTable+" WHERE";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
				source+=(i!=0 ? " AND " : " ") + ((String)primaryKeys.get(i)) + "=?";
			source+="\");\n\t\t\t";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
				source+="pstmt.set" + capitular((String)convertTypes.get((String) types.get((String)primaryKeys.get(i)))) + "(" + (i+1) + ", "+ getVariableNameByFieldName((String)primaryKeys.get(i)) + ");\n\t\t\t";
			source+="pstmt.executeUpdate();\n\t\t\t";

			if (!nmTableSuper.equals("")) {
				source+="if (" + nmClassSuper + "DAO.delete(";
				for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
					source += (i!=0 ? ", " : "") + getVariableNameByFieldName((String)primaryKeys.get(i));
				source+= ", connect)<=0) {\n\t\t\t\t";
				source+="if (isConnectionNull)\n\t\t\t\t\tConexao.rollback(connect);\n\t\t\t\treturn -1;\n\t\t\t}\n\n\t\t\t";				
				source+="if (isConnectionNull)\n\t\t\t\tconnect.commit();\n\n\t\t\t";				
			}			
			
			source+="return 1;\n\t\t";
			source+="}\n\t\t";
			source+="catch(SQLException sqlExpt){\n\t\t\t";
			source+="sqlExpt.printStackTrace(System.out);\n\t\t\t";
			source+="System.err.println(\"Erro! "+ nmClass +"DAO.delete: \" + sqlExpt);\n\t\t\t";
			if (!nmClassSuper.equals(""))
				source+="if (isConnectionNull)\n\t\t\t\tConexao.rollback(connect);\n\t\t\t";
			source+="return (-1)*sqlExpt.getErrorCode();\n\t\t";
			source+="}\n\t\t";
			source+="catch(Exception e){\n\t\t\t";
			source+="e.printStackTrace(System.out);\n\t\t\t";
			source+="System.err.println(\"Erro! "+ nmClass +"DAO.delete: \" +  e);\n\t\t\t";
			if (!nmClassSuper.equals(""))
				source+="if (isConnectionNull)\n\t\t\t\tConexao.rollback(connect);\n\t\t\t";
			source+="return -1;\n\t\t";
			source+="}\n\t\t";
			source+="finally{\n\t\t\t";
			source+="if (isConnectionNull)\n\t\t\t\tConexao.desconectar(connect);\n\t\t";
			source+="}\n\t";
			source+="}\n\n\t";


			//metodo get
			source+="public static "+ nmClass +" get(";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
				source+=(i!=0 ? ", " : "") + (String)convertTypes.get((String) types.get((String)primaryKeys.get(i)))+" "+ getVariableNameByFieldName((String)primaryKeys.get(i));
			source+=") {\n\t\treturn get(";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
				source+=(i!=0 ? ", " : "") + getVariableNameByFieldName((String)primaryKeys.get(i));
			source+=", null);\n\t}\n\n\t";
			source+="public static "+ nmClass +" get(";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
				source+=(i!=0 ? ", " : "") + (String)convertTypes.get((String) types.get((String)primaryKeys.get(i)))+" "+ getVariableNameByFieldName((String)primaryKeys.get(i));
			source+=", Connection connect){\n\t\t";
			source+="boolean isConnectionNull = connect==null;\n\t\tif (isConnectionNull)\n\t\t\tconnect = Conexao.conectar();\n\t\t";
			source+="PreparedStatement pstmt;\n\t\t";
			source+="ResultSet rs;\n\t\t";
			source+="try {\n\t\t\t";
			if(!nmClassSuper.equals("")) {
				source+="pstmt = connect.prepareStatement(\"SELECT * FROM " + nmTable + " A, "+ nmTableSuper +" B WHERE";
				for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
					source+=(i!=0 ? " AND" : "") + " A."+ ((String)primaryKeys.get(i)) + "=B." + ((String)fieldsSuper.get(i));
				for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)				
					source+=" AND A." +((String)primaryKeys.get(i)) +"=?";
			}
			else {
				source+="pstmt = connect.prepareStatement(\"SELECT * FROM " + nmTable + " WHERE";
				for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
					source+=(i!=0 ? " AND" : "") + " " + ((String)primaryKeys.get(i)) +"=?";
			}
			source+="\");\n\t\t\t";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)			
				source+="pstmt.set" + capitular((String)convertTypes.get((String) types.get((String)primaryKeys.get(i)))) + "(" + (i+1) + ", "+ getVariableNameByFieldName((String)primaryKeys.get(i)) +");\n\t\t\t";
			source+="rs = pstmt.executeQuery();\n\t\t\t";
			source+="if(rs.next()){\n\t\t\t\t";
			source+="return new "+ nmClass +"(";

			//campos classe pai
			if(!nmClassSuper.equals("")){
			  for(int i=0;i<fieldsSuper.size();i++){
			    String type = (String)convertTypes.get((String) typesSuper.get((String)fieldsSuper.get(i)));
				if(i!=0)
					source+=",\n\t\t\t\t\t\t";
				if(type.equals("GregorianCalendar"))
					source+="(rs.getTimestamp(\""+ ((String)fieldsSuper.get(i)) + "\")==null)?null:Util.longToCalendar("+"rs.getTimestamp(\""+ ((String)fieldsSuper.get(i)) + "\").getTime())";
				else if(type.equalsIgnoreCase("byte[]"))
					source+="rs.getBytes(\""+ ((String)fieldsSuper.get(i)) + "\")==null?null:"+"rs.getBytes(\""+ ((String)fieldsSuper.get(i)) + "\")";
				else
					source+="rs.get" + capitular(type) + "(\""+ ((String)fieldsSuper.get(i)) + "\")";
			  }
			}

			//campos classe
			for(int i=(!nmClassSuper.equals("") ? primaryKeys.size(): 0);i<fields.size();i++){
				String type = (String)convertTypes.get((String) types.get((String) fields.get(i)));
				if(!Util.existsInArray(fieldsSuper, fields.get(i))){
					if (i!=0)
						source+=",\n\t\t\t\t\t\t";
					if(type.equals("GregorianCalendar"))
						source+="(rs.getTimestamp(\""+ ((String)fields.get(i)) + "\")==null)?null:Util.longToCalendar("+"rs.getTimestamp(\""+ ((String)fields.get(i)) + "\").getTime())";
					else if(type.equalsIgnoreCase("byte[]"))
						source+="rs.getBytes(\""+ ((String)fields.get(i)) + "\")==null?null:"+"rs.getBytes(\""+ ((String)fields.get(i)) + "\")";
					else
						source+="rs.get" + capitular(type) + "(\""+ ((String)fields.get(i)) + "\")";
				}
			}

			source+=");\n\t\t\t";
			source+="}\n\t\t\t";
			source+="else{\n\t\t\t\t";
			source+="return null;\n\t\t\t";
			source+="}\n\t\t";
			source+="}\n\t\t";
			source+="catch(SQLException sqlExpt) {\n\t\t\t";
			source+="sqlExpt.printStackTrace(System.out);\n\t\t\t";
			source+="System.err.println(\"Erro! "+ nmClass +"DAO.get: \" + sqlExpt);\n\t\t\t";
			source+="return null;\n\t\t";
			source+="}\n\t\t";
			source+="catch(Exception e) {\n\t\t\t";
			source+="e.printStackTrace(System.out);\n\t\t\t";
			source+="System.err.println(\"Erro! "+ nmClass +"DAO.get: \" + e);\n\t\t\t";
			source+="return null;\n\t\t";
			source+="}\n\t\t";
			source+="finally {\n\t\t\t";
			source+="if (isConnectionNull)\n\t\t\t\tConexao.desconectar(connect);\n\t\t";
			source+="}\n\t";
			source+="}\n\n\t";

			//metodo getAll
			source+="public static ResultSetMap getAll() {\n\t\treturn getAll(null);\n\t}\n\n\t";
			source+="public static ResultSetMap getAll(Connection connect) {\n\t\t";
			source+="boolean isConnectionNull = connect==null;\n\t\tif (isConnectionNull)\n\t\t\tconnect = Conexao.conectar();\n\t\t";
			source+="PreparedStatement pstmt;\n\t\t";
			source+="try {\n\t\t\t";
			source+="pstmt = connect.prepareStatement(\"SELECT * FROM "+ nmTable +"\");\n\t\t\t";
			source+="return new ResultSetMap(pstmt.executeQuery());\n\t\t";
			source+="}\n\t\t";
			source+="catch(SQLException sqlExpt) {\n\t\t\t";
			source+="sqlExpt.printStackTrace(System.out);\n\t\t\t";
			source+="System.err.println(\"Erro! "+ nmClass +"DAO.getAll: \" + sqlExpt);\n\t\t\t";
			source+="return null;\n\t\t";
			source+="}\n\t\t";
			source+="catch(Exception e) {\n\t\t\t";
			source+="e.printStackTrace(System.out);\n\t\t\t";
			source+="System.err.println(\"Erro! "+ nmClass +"DAO.getAll: \" + e);\n\t\t\t";
			source+="return null;\n\t\t";
			source+="}\n\t\t";
			source+="finally {\n\t\t\t";
			source+="if (isConnectionNull)\n\t\t\t\tConexao.desconectar(connect);\n\t\t";
			source+="}\n\t";
			source+="}\n\n\t";
			
			//metodo getList
			source+="public static ArrayList<"+nmClass+"> getList() {\n\t\treturn getList(null);\n\t}\n\n\t";
			source+="public static ArrayList<"+nmClass+"> getList(Connection connect) {\n\t\t";
			source+="boolean isConnectionNull = connect==null;\n\t\tif (isConnectionNull)\n\t\t\tconnect = Conexao.conectar();\n\t\t";
			source+="try {\n\t\t\t";
			source+="ArrayList<"+nmClass+"> list = new ArrayList<"+nmClass+">();\n\t\t\t";
			source+="ResultSetMap rsm = getAll(connect);\n\t\t\t";
			source+="while(rsm.next()){\n\t\t\t\t";
			source+=nmClass+" obj = "+nmClass+"DAO.get(";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
				source+=(i!=0 ? ", " : "") + "rsm.getInt(\""+ (String)primaryKeys.get(i)+"\")";
			source+=", connect);\n\t\t\t\t";
			source+="list.add(obj);\n\t\t\t";
			source+="}\n\t\t\t";
			source+="return list;\n\t\t";
			source+="}\n\t\t";
			source+="catch(Exception e) {\n\t\t\t";
			source+="e.printStackTrace(System.out);\n\t\t\t";
			source+="System.err.println(\"Erro! "+ nmClass +"DAO.getList: \" + e);\n\t\t\t";
			source+="return null;\n\t\t";
			source+="}\n\t\t";
			source+="finally {\n\t\t\t";
			source+="if (isConnectionNull)\n\t\t\t\tConexao.desconectar(connect);\n\t\t";
			source+="}\n\t";
			source+="}\n\n\t";

			//metódo find
			source+="public static ResultSetMap find(ArrayList<ItemComparator> criterios) {\n\t\treturn find(criterios, null);\n\t}\n\n\t";
			source+="public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {\n\t\t";
			source+="return Search.find(\"SELECT * FROM " + nmTable + "\", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);\n\t}\n\n";
			//fim classe
			source+="}\n";

			return source;
	    }
	    catch(Exception e) {
	      e.printStackTrace(System.out);
	      System.err.println("Erro! getDAOClass: " + e);
	      return null;
	    }
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	/**
	 * CRIA UMA CLASSE DE SERVICOS (Services) PARA A TABELA INDICADA
	 * @param nmTable
	 * @param nmClass
	 * @param nmPackage
	 * @param nmClassConnection
	 * @return
	 */
	public static String getServicesClass(String nmTable, String nmClass, String nmPackage, String nmClassConnection, boolean lgCompliance){
		return getServicesClass(nmTable, nmClass, nmPackage, nmClassConnection, lgCompliance, null);
	}
	
	@SuppressWarnings("unchecked")
	public static String getServicesClass(String nmTable, String nmClass, String nmPackage, String nmClassConnection, boolean lgCompliance, Connection connect){
		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			HashMap<String, String> convertTypes = (HashMap<String, String>) getTypeMaps().getObjects().get("JAVA_TYPES");
			
			ArrayList<String> fields = getFields(nmTable, connect);
			ArrayList<String> primaryKeys = getPrimaryKeys(nmTable, connect);
			ArrayList<String> foreignKeys = getForeignKeys(nmTable, connect);
		    HashMap<String, String> types = getFieldTypes(nmTable, connect);
		    
		    String source;
		    
	      //gerando codigo
	    	source=(nmPackage.equals(""))?"":"package "+nmPackage+";\n\n";

			source+="import java.sql.*;\n";
			source+="import java.sql.PreparedStatement;\n";
			source+="import java.util.ArrayList;\n\n";
			
			source+="import sol.dao.ResultSetMap;\n";
			source+="import sol.dao.ItemComparator;\n";
			source+="import sol.dao.Search;\n";
			source+="import sol.util.Result;\n\n";

			source+="import " + nmClassConnection + ";\n";
			source+="import com.tivic.manager.grl.ParametroServices;\n";
			source+="import com.tivic.manager.seg.AuthData;\n";
			
			if(lgCompliance)
				source+="import com.tivic.manager.util.ComplianceManager;\n";
			
			source+="\npublic class "+nmClass+ "Services {\n\n";
		 
			//metodo save
			source+="	public static Result save("+nmClass+" "+Util.decapitular(nmClass)+"){\n";
			source+="		return save("+Util.decapitular(nmClass)+", null, null);\n";
			source+="	}\n\n";
			
			source+="	public static Result save("+nmClass+" "+Util.decapitular(nmClass)+", AuthData authData){\n";
			source+="		return save("+Util.decapitular(nmClass)+", authData, null);\n";
			source+="	}\n\n";
			
			source+="	public static Result save("+nmClass+" "+Util.decapitular(nmClass)+", AuthData authData, Connection connect){\n";
			source+="		boolean isConnectionNull = connect==null;\n";
			source+="		try {\n";
			source+="			if (isConnectionNull) {\n";
			source+="				connect = Conexao.conectar();\n";
			source+="				connect.setAutoCommit(false);\n";
			source+="			}\n\n";
						
			source+="			if("+Util.decapitular(nmClass)+"==null)\n";
			source+="				return new Result(-1, \"Erro ao salvar. "+nmClass+" é nulo\");\n\n";
			
			if(lgCompliance)
				source+="			int tpAcaoCompliance = ComplianceManager.TP_ACAO_ANY;\n\n";
			
			source+="			int retorno;\n";
			source+="			if("+Util.decapitular(nmClass)+".get" + capitular(getVariableNameByFieldName((String)primaryKeys.get(0))) + "()==0){\n";
			
			if(lgCompliance)
				source+="				tpAcaoCompliance = ComplianceManager.TP_ACAO_INSERT;\n";
			
			source+="				retorno = "+nmClass+"DAO.insert("+Util.decapitular(nmClass)+", connect);\n";
			source+="				"+Util.decapitular(nmClass)+".set" + capitular(getVariableNameByFieldName((String)primaryKeys.get(0))) + "(retorno);\n";
			source+="			}\n";
			source+="			else {\n";
			
			if(lgCompliance)
				source+="				tpAcaoCompliance = ComplianceManager.TP_ACAO_UPDATE;\n";
			
			source+="				retorno = "+nmClass+"DAO.update("+Util.decapitular(nmClass)+", connect);\n";
			source+="			}\n\n";
					
			source+="			if(retorno<=0)\n";
			source+="				Conexao.rollback(connect);\n";
			source+="			else if (isConnectionNull)\n";
			source+="				connect.commit();\n\n";
			
			if(lgCompliance) {
				source+="			//COMPLIANCE\n";
				source+="			ComplianceManager.process("+Util.decapitular(nmClass)+", authData, tpAcaoCompliance, connect);\n\n";
			}
			
			source+="			return new Result(retorno, (retorno<=0)?\"Erro ao salvar...\":\"Salvo com sucesso...\", \""+nmClass.toUpperCase()+"\", "+Util.decapitular(nmClass)+");\n";
			source+="		}\n";
			source+="		catch(Exception e){\n";
			source+="			e.printStackTrace();\n";
			source+="			if (isConnectionNull)\n";
			source+="				Conexao.rollback(connect);\n";
			source+="			return new Result(-1, e.getMessage());\n";
			source+="		}\n";
			source+="		finally{\n";
			source+="			if (isConnectionNull)\n";
			source+="				Conexao.desconectar(connect);\n";
			source+="		}\n";
			source+="	}\n";
				
			//metodo remove
			source+="	public static Result remove("+nmClass+" "+Util.decapitular(nmClass)+") {\n";
			source+="		return remove(";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
				source += (i!=0 ? ", " : "") +Util.decapitular(nmClass)+ ".get" + capitular(getVariableNameByFieldName((String) fields.get(i))) + "()";
			source+=");\n";
			source+="	}\n";
			
			source+="	public static Result remove(";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
				source += (i!=0 ? ", " : "") + (String)convertTypes.get((String) types.get((String)primaryKeys.get(i)))+" "+ getVariableNameByFieldName((String)primaryKeys.get(i));
			source+="){\n";
			source+="		return remove(";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
				source += (i!=0 ? ", " : "") + getVariableNameByFieldName((String)primaryKeys.get(i));
			source+=", false, null, null);\n";
			source+="	}\n";
			
			source+="	public static Result remove(";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
				source += (i!=0 ? ", " : "") + (String)convertTypes.get((String) types.get((String)primaryKeys.get(i)))+" "+ getVariableNameByFieldName((String)primaryKeys.get(i));
			source+=", boolean cascade){\n";
			source+="		return remove(";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
				source += (i!=0 ? ", " : "") + getVariableNameByFieldName((String)primaryKeys.get(i));
			source+=", cascade, null, null);\n";
			source+="	}\n";
			
			source+="	public static Result remove(";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
				source += (i!=0 ? ", " : "") + (String)convertTypes.get((String) types.get((String)primaryKeys.get(i)))+" "+ getVariableNameByFieldName((String)primaryKeys.get(i));
			source+=", boolean cascade, AuthData authData){\n";
			source+="		return remove(";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
				source += (i!=0 ? ", " : "") + getVariableNameByFieldName((String)primaryKeys.get(i));
			source+=", cascade, authData, null);\n";
			source+="	}\n";
			
			source+="	public static Result remove(";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
				source += (i!=0 ? ", " : "") + (String)convertTypes.get((String) types.get((String)primaryKeys.get(i)))+" "+ getVariableNameByFieldName((String)primaryKeys.get(i));
			source+=", boolean cascade, AuthData authData, Connection connect){\n";
			source+="		boolean isConnectionNull = connect==null;\n";
			source+="		try {\n";
			source+="			if (isConnectionNull) {\n";
			source+="				connect = Conexao.conectar();\n";
			source+="				connect.setAutoCommit(false);\n";
			source+="			}\n";
			
			source+="			int retorno = 0;\n";
			
			source+="			if(cascade){\n";
			source+="				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/\n";
			source+="				retorno = 1;\n";
			source+="			}\n";
				
			source+="			if(!cascade || retorno>0)\n";
			source+="			retorno = "+nmClass+"DAO.delete(";
			for (int i=0; primaryKeys!=null && i<primaryKeys.size(); i++)
				source += (i!=0 ? ", " : "") + getVariableNameByFieldName((String)primaryKeys.get(i));
			source+=", connect);\n";
			
			source+="			if(retorno<=0){\n";
			source+="				Conexao.rollback(connect);\n";
			source+="				return new Result(-2, \"Este registro está vinculado a outros e não pode ser excluído!\");\n";
			source+="			}\n";
			source+="			else if (isConnectionNull)\n";
			source+="				connect.commit();\n";	

			if(lgCompliance) {
				source+="			//COMPLIANCE\n";
				source+="			ComplianceManager.process("+Util.decapitular(nmClass)+", authData, ComplianceManager.TP_ACAO_DELETE, connect);\n\n";
			}
			
			source+="			return new Result(1, \"Registro excluído com sucesso!\");\n";
			source+="		}\n";
			source+="		catch(Exception e){\n";
			source+="			e.printStackTrace();\n";
			source+="			if (isConnectionNull)\n";
			source+="				Conexao.rollback(connect);\n";
			source+="			return new Result(-1, \"Erro ao excluir registro!\");\n";
			source+="		}\n";
			source+="		finally{\n";
			source+="			if (isConnectionNull)\n";
			source+="				Conexao.desconectar(connect);\n";
			source+="		}\n";
			source+="	}\n\n";
			

			//metodo getAll
			source+="	public static ResultSetMap getAll() {\n\t\treturn getAll(null);\n\t}\n\n\t";
			source+="public static ResultSetMap getAll(Connection connect) {\n\t\t";
			source+="boolean isConnectionNull = connect==null;\n\t\tif (isConnectionNull)\n\t\t\tconnect = Conexao.conectar();\n\t\t";
			source+="PreparedStatement pstmt;\n\t\t";
			source+="try {\n\t\t\t";
			source+="pstmt = connect.prepareStatement(\"SELECT * FROM "+ nmTable +"\");\n\t\t\t";
			source+="	return new ResultSetMap(pstmt.executeQuery());\n\t\t";
			source+="}\n\t\t";
			source+="catch(SQLException sqlExpt) {\n\t\t\t";
			source+="sqlExpt.printStackTrace(System.out);\n\t\t\t";
			source+="System.err.println(\"Erro! "+ nmClass +"Services.getAll: \" + sqlExpt);\n\t\t\t";
			source+="return null;\n\t\t";
			source+="}\n\t\t";
			source+="catch(Exception e) {\n\t\t\t";
			source+="e.printStackTrace(System.out);\n\t\t\t";
			source+="System.err.println(\"Erro! "+ nmClass +"Services.getAll: \" + e);\n\t\t\t";
			source+="return null;\n\t\t";
			source+="}\n\t\t";
			source+="finally {\n\t\t\t";
			source+="if (isConnectionNull)\n\t\t\t\tConexao.desconectar(connect);\n\t\t";
			source+="}\n\t";
			source+="}\n\n\t";
			
			//metódo find
			source+="public static ResultSetMap find(ArrayList<ItemComparator> criterios) {\n\t\treturn find(criterios, null);\n\t}\n\n\t";
			source+="public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {\n\t\t";
			source+="return Search.find(\"SELECT * FROM " + nmTable + "\", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);\n\t}\n\n";
		
			if(lgCompliance) {
				//metodo saveCompliance
				source+="	public static Result saveCompliance("+nmClass+" "+Util.decapitular(nmClass)+", AuthData authData, int tpAcao, Connection connect) {\n";
				source+="		try {\n\n";
				
				source+="			PreparedStatement pstmt = connect.prepareStatement(\"INSERT INTO " + nmTable+" (";
				for(int i=0;i<fields.size();i++){
					if(i!=0)
						source+=",\"+\n\t\t\t                                  \"";
					source+=(String) fields.get(i);
				}
				
				source+=",\"+\n\t\t\t                                  \"" +
						"cd_usuario_compliance,\"+\n\t\t\t                                  \""+
	                    "dt_compliance,\"+\n\t\t\t                                  \""+ 
	                    "tp_acao_compliance";
				
				source+=") VALUES (";
				for(int i=0;i<fields.size();i++){
					if(i!=0)
						source+=", ";
					source+="?";
				}
				source+=", ?, ?, ?)\");\n\t\t\t";
	
				for(int i=0;i<fields.size();i++){
					String type = (String)convertTypes.get(((String)types.get((String)fields.get(i))).toUpperCase());
					if (type == null)
						System.out.println("Erro na tabela " + nmTable + " na coluna " + fields.get(i) + " de tipo " + types.get((String) fields.get(i)));
	
					String varName = getVariableNameByFieldName((String) fields.get(i));
					
					if(type!=null && type.equals("GregorianCalendar"))
						source+="if(objeto.get" + capitular(varName) + "()==null)\n\t\t\t\tpstmt.setNull("+(i+1)+", Types.TIMESTAMP);\n\t\t\telse\n\t\t\t\tpstmt.setTimestamp("+(i+1)+",new Timestamp(objeto.get" + capitular(varName) + "().getTimeInMillis()));\n\t\t\t";
					else if(type!=null && type.equals("int") && Util.existsInArray(foreignKeys, (String) fields.get(i)))
						source+="if(objeto.get" + capitular(varName) + "()==0)\n\t\t\t\tpstmt.setNull("+(i+1)+", Types.INTEGER);\n\t\t\telse\n\t\t\t\tpstmt.setInt("+(i+1)+",objeto.get" + capitular(varName) + "());\n\t\t\t";
					else if (type.equalsIgnoreCase("byte[]"))
						source+="if(objeto.get" + capitular(varName) + "()==null)\n\t\t\t\tpstmt.setNull("+(i+1)+", Types.BINARY);\n\t\t\telse\n\t\t\t\tpstmt.setBytes("+(i+1)+",objeto.get" + capitular(varName) + "());\n\t\t\t";
					else
						source+="pstmt.set"+capitular(type)+"("+(i+1)+",objeto.get" + capitular(varName) + "());\n\t\t\t";
				}
				source+=		   "if(authData==null)\n"
						+ "				pstmt.setNull("+(fields.size()+1)+", Types.INTEGER);\n"
						+ "			else\n"
						+ "				pstmt.setInt("+(fields.size()+1)+", authData.getUsuario().getCdUsuario());\n"
						+ "			pstmt.setTimestamp("+(fields.size()+2)+", new Timestamp(new GregorianCalendar().getTimeInMillis()));\n"
						+ "			pstmt.setInt("+(fields.size()+3)+", tpAcao);\n";
				
				source+="			int retorno = pstmt.executeUpdate();\n\n";
				
				source+="			return new Result(retorno < 0 ? retorno : 1, (retorno < 0)?\"Erro ao salvar compliance...\":\"Compliance salvo com sucesso...\");\n";
				source+="		}\n";
				source+="		catch(Exception e){\n";
				source+="			e.printStackTrace();\n";
				source+="			return new Result(-1, e.getMessage());\n";
				source+="		}\n";
				source+="	}\n";
			}
			
			//fim classe
			source+="}\n";
		 
	      return source;
	    }
	    catch(Exception e) {
	      e.printStackTrace(System.out);
	      System.err.println("Erro! getServicesClass: " + e);
	      return null;
	    }
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}


	/**
	 * CRIA UMA CLASSE DE SERVICOS (Services) PARA A TABELA INDICADA
	 * @param nmTable
	 * @param nmClass
	 * @param nmPackage
	 * @param nmClassConnection
	 * @return
	 */
	public static String getRestClass(String nmTable, String nmClass, String nmPackage, String nmClassConnection, boolean lgCompliance){
		return getRestClass(nmTable, nmClass, nmPackage, nmClassConnection, lgCompliance, null);
	}
	
	@SuppressWarnings("unchecked")
	public static String getRestClass(String nmTable, String nmClass, String nmPackage, String nmClassConnection, boolean lgCompliance, Connection connect){
		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			HashMap<String, String> convertTypes = (HashMap<String, String>) getTypeMaps().getObjects().get("JAVA_TYPES");
			
			ArrayList<String> fields = getFields(nmTable, connect);
			ArrayList<String> primaryKeys = getPrimaryKeys(nmTable, connect);
			ArrayList<String> foreignKeys = getForeignKeys(nmTable, connect);
		    HashMap<String, String> types = getFieldTypes(nmTable, connect);
		    
		    String source;
		    
		    //gerando codigo
	    	source=(nmPackage.equals(""))?"":"package "+nmPackage+";\n\n";

			source+="import java.util.ArrayList;\n\n";
			
			source+="import sol.dao.ResultSetMap;\n";
			source+="import sol.dao.ItemComparator;\n";
			source+="import sol.util.Result;\n\n";

			source+="import javax.ws.rs.Consumes;\n";
			source+="import javax.ws.rs.POST;\n";
			source+="import javax.ws.rs.PUT;\n";
			source+="import javax.ws.rs.DELETE;\n";
			source+="import javax.ws.rs.Path;\n";
			source+="import javax.ws.rs.Produces;\n";
			source+="import javax.ws.rs.core.MediaType;\n";
			
			source+="import com.fasterxml.jackson.databind.ObjectMapper;\n";
			source+="import org.json.JSONObject;\n\n";
			
			source+="import com.tivic.manager.util.Util;\n\n";
			
			source+="@Path(\"/"+nmPackage.split("\\.")[3]+"/"+nmClass.toLowerCase()+"/\")\n";
			source+="\npublic class "+nmClass+ "Rest {\n\n";
		 
			//metodo save
			source+="	@PUT\n";
			source+="	@Path(\"/save\")\n";
			source+="	@Consumes(MediaType.APPLICATION_JSON)\n";
			source+="	@Produces(MediaType.APPLICATION_JSON)\n";
			source+="	public String save("+nmClass+" "+Util.decapitular(nmClass)+"){\n";
			source+="		try {\n";				
			source+="			Result result = "+nmClass+ "Services.save("+Util.decapitular(nmClass)+");\n";
			source+="			return new JSONObject(result).toString();\n";
			source+="		} catch(Exception e) {\n";
			source+="			e.printStackTrace(System.out);\n";
			source+="			return null;\n";
			source+="		}\n";
			source+="	}\n\n";
				
			//metodo remove
			source+="	@DELETE\n";
			source+="	@Path(\"/remove\")\n";
			source+="	@Consumes(MediaType.APPLICATION_JSON)\n";
			source+="	@Produces(MediaType.APPLICATION_JSON)\n";
			source+="	public String remove("+nmClass+" "+Util.decapitular(nmClass)+"){\n";
			source+="		try {\n";				
			source+="			Result result = "+nmClass+ "Services.remove("+Util.decapitular(nmClass)+");\n";
			source+="			return new JSONObject(result).toString();\n";
			source+="		} catch(Exception e) {\n";
			source+="			e.printStackTrace(System.out);\n";
			source+="			return null;\n";
			source+="		}\n";
			source+="	}\n\n";

			//metodo getAll
			source+="	@POST\n";
			source+="	@Path(\"/getAll\")\n";
			source+="	@Produces(MediaType.APPLICATION_JSON)\n";
			source+="	public static String getAll() {\n";
			source+="		try {\n";				
			source+="			ResultSetMap rsm = "+nmClass+ "Services.getAll();\n";
			source+="			return Util.rsmToJSON(rsm);\n";
			source+="		} catch(Exception e) {\n";
			source+="			e.printStackTrace(System.out);\n";
			source+="			return null;\n";
			source+="		}\n";
			source+="	}\n\n";
			
			
			//metódo find
			source+="	@POST\n";
			source+="	@Path(\"/find\")\n";
			source+="	@Consumes(MediaType.APPLICATION_JSON)\n";
			source+="	@Produces(MediaType.APPLICATION_JSON)\n";
			source+="	public static String find(ArrayList<ItemComparator> criterios) {\n";
			source+="		try {\n";				
			source+="			ResultSetMap rsm = "+nmClass+ "Services.find(criterios);\n";
			source+="			return Util.rsmToJSON(rsm);\n";
			source+="		} catch(Exception e) {\n";
			source+="			e.printStackTrace(System.out);\n";
			source+="			return null;\n";
			source+="		}\n";
			source+="	}\n\n";
			
			//fim classe
			source+="}\n";
		 
	      return source;
	    }
	    catch(Exception e) {
	      e.printStackTrace(System.out);
	      System.err.println("Erro! getServicesClass: " + e);
	      return null;
	    }
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * CRIA UMA CLASSE DE SERVICOS (Services) no TS PARA A TABELA INDICADA
	 * @param nmTable
	 * @param nmClass
	 * @param nmPackage
	 * @param nmClassConnection
	 * @return
	 */
	public static String getTsServicesClass(String nmClass, String nmPackage){
		return getTsServicesClass(nmClass, nmPackage, null);
	}
	
	@SuppressWarnings("unchecked")
	public static String getTsServicesClass(String nmClass, String nmPackage, Connection connect){
		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			String source;
		    
		    //gerando codigo
	    	source="import { Injectable }    from '@angular/core';\n\n";
			
			source+="import { Quark, Result } from 'sol/utils';\n";
			source+="import { ResultSetMap, ItemComparator } from 'sol/dao';\n";
			source+="import { "+nmClass+" } from './"+nmClass+"';\n\n";
			
			source+="@Injectable()\n";
			source+="export class "+nmClass+"Services {\n\n";
			
			//construtor vazio
			source+="	constructor() {}\n\n";
			
			//metodo salvar
			source+="	public static async save(register) : Promise<Result> {\n";
			source+="		return Object.assign(new Result(-1), await Quark.call(\n";
			source+="			'"+nmPackage.split("\\.")[3]+"/"+nmClass.toLowerCase()+"/save',\n";
			source+="			{args: register},\n";
			source+="			Quark.PUT,\n";
			source+="			Quark.REST\n";
			source+="		));\n";
			source+="	}\n\n";
			
			//metodo remover
			source+="	public static async remove(register) : Promise<Result> {\n";
			source+="		return Object.assign(new Result(-1), await Quark.call(\n";
			source+="			'"+nmPackage.split("\\.")[3]+"/"+nmClass.toLowerCase()+"/remove',\n";
			source+="			{args: register},\n";
			source+="			Quark.DELETE,\n";
			source+="			Quark.REST\n";
			source+="		));\n";
			source+="	}\n\n";
		 
			
			//metodo getAll
			source+="	public static async getAll() : Promise<ResultSetMap> {\n";
			source+="		return Object.assign(new ResultSetMap(), await Quark.call(\n";
			source+="			'"+nmPackage.split("\\.")[3]+"/"+nmClass.toLowerCase()+"/getAll',\n";
			source+="			[],\n";
			source+="			Quark.POST,\n";
			source+="			Quark.REST\n";
			source+="		));\n";
			source+="	}\n\n";
			
			//metodo find
			source+="	public static async find(itemComparator:ItemComparator[]) : Promise<ResultSetMap> {\n";
			source+="		return Object.assign(new ResultSetMap(), await Quark.call(\n";
			source+="			'"+nmPackage.split("\\.")[3]+"/"+nmClass.toLowerCase()+"/find',\n";
			source+="			{args: itemComparator},\n";
			source+="			Quark.POST,\n";
			source+="			Quark.REST\n";
			source+="		));\n";
			source+="	}\n\n";
			
			//fim classe
			source+="}\n";
		 
	      return source;
	    }
	    catch(Exception e) {
	      e.printStackTrace(System.out);
	      System.err.println("Erro! getServicesClass: " + e);
	      return null;
	    }
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * CRIA A CLASSE AS3 PARA A TABELA INDICADA
	 * @param nmTable
	 * @param nmClass
	 * @param nmTableSuper
	 * @param nmClassSuper
	 * @param nmPackage
	 * @return
	 */
	public static String getAS3Class(String nmTable, String nmClass, String nmTableSuper, String nmClassSuper, String nmPackage){
		return getAS3Class(nmTable, nmClass, nmTableSuper, nmClassSuper, nmPackage, null);
	}
	
	@SuppressWarnings("unchecked")
	public static String getAS3Class(String nmTable, String nmClass, String nmTableSuper, String nmClassSuper, String nmPackage, Connection connect){
		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			HashMap<String, String> convertTypes = (HashMap<String, String>) getTypeMaps().getObjects().get("AS3_TYPES");
			
		    ArrayList<String> fields = getFields(nmTable, connect);
		    HashMap<String, String> types = getFieldTypes(nmTable, connect);
		    
		    ArrayList<String> fieldsSuper = null;
	
		    if(!nmTableSuper.equals("")){
			    fieldsSuper = getFields(nmTableSuper, connect);
		    }
	
		    String source;
	    
	      //gerando codigo
	      source=(nmPackage.equals(""))?"":"package "+nmPackage+" {\n\n";
	      
	      if(hasByteFields(nmTable, connect))
	    	  source+="\timport flash.utils.ByteArray;\n\n";
	      
	      source+="\t[RemoteClass(alias=\""+nmPackage+"."+nmClass+"\")]\n\n";
	      source+="\t[Bindable]\n";
	      source+="\tpublic class "+nmClass+ " {\n\n";
	      //variaveis
			/*
			 * for(int i=0;i<fields.size();i++) { if(!Util.existsInArray(fieldsSuper,
			 * fields.get(i))) { System.out.print("field: "+(String) fields.get(i));
			 * System.out.print(", type: " + (String) types.get((String) fields.get(i)));
			 * System.out.print(", java type: " + convertTypes.get((String)
			 * types.get((String) fields.get(i))).toString() + "\n");
			 * 
			 * String type = convertTypes.get((String) types.get((String)
			 * fields.get(i))).toString(); source+="\t\tpublic var " +
			 * getVariableNameByFieldName((String) fields.get(i)) + ":" + type + ";\n"; } }
			 */
	      
	      //function getInstanceByRegister
	      source+="\n\t\tpublic static function getInstanceByRegister(register:Object):"+nmClass+" {\n";
	      source+="\t\t\tif(register==null)\n";
	      source+="\t\t\t\treturn null;\n\n";
	      source+="\t\t\tvar obj:"+nmClass+" = new "+nmClass+"();\n";
	      for(int i=0;i<fields.size();i++) {
	      	if(!Util.existsInArray(fieldsSuper, fields.get(i))) {
	      		
	      		if((convertTypes.get((String) types.get((String) fields.get(i))).toString()).equals("Number"))
	      			source+="\t\t\tobj." + getVariableNameByFieldName((String) fields.get(i)) +  " = isNaN(register." + ((String) fields.get(i)).toUpperCase() + ") ? 0 : register." + ((String) fields.get(i)).toUpperCase() + ";\n";
	      		else
	      			source+="\t\t\tobj." + getVariableNameByFieldName((String) fields.get(i)) +  " = register." + ((String) fields.get(i)).toUpperCase() + ";\n";
	      	}
	      }
	      source+="\n\t\t\treturn obj;\n";
	      source+="\t\t}\n";
	      
	    //function getRegisterByInstance
	      source+="\n\t\tpublic static function getRegisterByInstance(obj:"+nmClass+"):Object {\n";
	      source+="\t\t\tif(obj==null)\n";
	      source+="\t\t\t\treturn null;\n\n";
	      source+="\t\t\tvar register:Object = new Object();\n";
	      for(int i=0;i<fields.size();i++) {
	      	if(!Util.existsInArray(fieldsSuper, fields.get(i))) {
	      		source+="\t\t\tregister." + ((String) fields.get(i)).toUpperCase() +  " = obj." + getVariableNameByFieldName((String) fields.get(i)) + ";\n";
	      	}
	      }
	      source+="\n\t\t\treturn register;\n";
	      source+="\t\t}\n";
	      
	      
		  source+="\t}\n";
		  source+="}";

	      return source;
	    }
	    catch(Exception e) {
	      e.printStackTrace(System.out);
	      System.err.println("Erro! getAS3Class: " + e);
	      return null;
	    }
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

	/**
	 * CRIA A CLASSE TYPESCRIPT PARA A TABELA INDICADA
	 * @param nmTable
	 * @param nmClass
	 * @param nmTableSuper
	 * @param nmClassSuper
	 * @param nmPackage
	 * @return
	 */
	public static String getTSClass(String nmTable, String nmClass, String nmTableSuper, String nmClassSuper, String nmPackage){
		return getAS3Class(nmTable, nmClass, nmTableSuper, nmClassSuper, nmPackage, null);
	}
	
	@SuppressWarnings("unchecked")
	public static String getTSClass(String nmTable, String nmClass, String nmTableSuper, String nmClassSuper, String nmPackage, Connection connect){
		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			HashMap<String, String> convertTypes = (HashMap<String, String>) getTypeMaps().getObjects().get("TS_TYPES");
			
		    ArrayList<String> fields = getFields(nmTable, connect);
		    HashMap<String, String> types = getFieldTypes(nmTable, connect);
		    
		    ArrayList<String> fieldsSuper = null;
	
		    if(!nmTableSuper.equals("")){
			    fieldsSuper = getFields(nmTableSuper, connect);
		    }
	
		    String source;
	    
	      //gerando codigo
	      source="import { ObjectUtils } from 'sol/utils/ObjectUtils';\n\n";
	      
	      source+="export class "+nmClass+ " {\n\n";
	      //variaveis
	      for(int i=0;i<fields.size();i++) {
	      	if(!Util.existsInArray(fieldsSuper, fields.get(i))) {
	      		System.out.print("field: "+(String) fields.get(i));
	      		System.out.print(", type: " + (String) types.get((String) fields.get(i)));
//	      		System.out.print(", java type: " + convertTypes.get((String) types.get((String) fields.get(i))).toString() + "\n");
	      		
	      		String type = convertTypes.get((String) types.get((String) fields.get(i))).toString();
	      		source+="\tpublic " + getVariableNameByFieldName((String) fields.get(i)) +  ":" + type + " = void(0);\n";
	      	}
	      }
	      
	      source+="\n\tconstructor(){}\n";
	      
	      
	      source+="\n\tstatic getInstanceByString(serialized:string):"+nmClass+" {\n";
	      source+="\t\tif(serialized==null || serialized=='')\n";
	      source+="\t\t\treturn null;\n\n";
	      source+="\t\treturn ObjectUtils.getInstanceByString(serialized, "+nmClass+") as "+nmClass+";\n";
	      source+="\t}\n";
	      
	      source+="\n\tstatic getInstanceByRegister(register:object):"+nmClass+" {\n";
	      source+="\t\tif(register==null)\n";
	      source+="\t\t\treturn null;\n\n";
	      source+="\t\treturn ObjectUtils.getInstanceByRegister(register, "+nmClass+") as "+nmClass+";\n";
	      source+="\t}\n";

	      source+="\n\tstatic getRegisterByInstance(obj:"+nmClass+"):object {\n";
	      source+="\t\tif(obj==null)\n";
	      source+="\t\t\treturn null;\n\n";
	      source+="\t\treturn ObjectUtils.getRegisterByInstance(obj);\n";
	      source+="\t}\n";

		  source+="}";

	      return source;
	    }
	    catch(Exception e) {
	      e.printStackTrace(System.out);
	      System.err.println("Erro! getTSClass: " + e);
	      return null;
	    }
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * RETORNA AS TABELAS DO BANCO EM QUE A APLICACAO ESTÁ CONECTADA
	 * @return
	 */
	public static ResultSetMap getTables() {
		return getTables(null);
	}
	
	public static ResultSetMap getTables(Connection connect){
		
		boolean isConnectionNull = connect==null;
		
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}

			ResultSetMap rsmTables = new ResultSetMap();

	   		DatabaseMetaData dbmd = connect.getMetaData();
		    ResultSet rs = dbmd.getTables(connect.getCatalog(),null,null,null);
		    ResultSetMetaData rsmd = rs.getMetaData();
		    String nmColumnTypeTable = null;
		    String nmColumnNameTable = null;
		    
		    for (int i=0; i<rsmd.getColumnCount(); i++) {
		    	if (rsmd.getColumnName(i+1).equalsIgnoreCase("TABLE_TYPE")) {
		    		nmColumnTypeTable = rsmd.getColumnName(i+1);
		    		break;
		    	}
		    }
		    
		    for (int i=0; i<rsmd.getColumnCount(); i++) {
		    	if (rsmd.getColumnName(i+1).equalsIgnoreCase("TABLE_NAME")) {
		    		nmColumnNameTable = rsmd.getColumnName(i+1);
		    		break;
		    	}
		    }
		    
		    if (nmColumnTypeTable!=null && nmColumnNameTable!=null)
		    	while(rs!=null && rs.next())	{
		    		if (rs.getString(nmColumnTypeTable)!=null && rs.getString(nmColumnTypeTable).equals("TABLE")) {
		    			HashMap<String, Object> register = new HashMap<String, Object>();
		    			
		    			String tableName = rs.getString(nmColumnNameTable);
		    			register.put("TABLE_NAME", tableName.toLowerCase());
		    			register.put("CLASS_NAME", getClassNameByTableName(tableName, false));
		    			register.put("PREFIX", getPackageNameByTableName(tableName, null));

				    	rsmTables.addRegister(register);
		    		}
		    	}
		    
		    return rsmTables;
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    	System.out.println("Erro: "+e);
	    	return null;
	    }
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static boolean hasTable(String tableName) {
		return hasTable(tableName, null);
	}
	
	public static boolean hasTable(String tableName, Connection connect) {
		
		boolean isConnectionNull = connect==null;
		
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			
			ResultSetMap rsmTables = getTables(connect);
			
			for (HashMap<String, Object> register : rsmTables.getLines()) {
				if(register.get("TABLE_NAME").equals(tableName.toLowerCase()))
					return true;
			}
			
			return false;
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    	System.out.println("Erro: "+e);
	    	return false;
	    }
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static String camelCaseToUnderscore(String input) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            result.append((Character.isUpperCase(c) ? "_" : "") + Character.toLowerCase(c));
        }

        return result.toString();
    }
	
	public static String underscoreToCamelCase(String input) {
		StringBuilder result = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if(c=='_') {
            	result.append(Character.toUpperCase(input.charAt(i+1)));
            	i++;
            } else {
            	result.append(c);
            }
        }
		
		return result.toString();
	}
	
	public static Result getTypeMaps() {
		
		Result result = new Result(1, "Mapas de Tipos recuperados");
		
		
		/**
		 * TIPOS AS3
		 */
		HashMap<String, String> tiposAS3 = new HashMap<String, String>();
		
		tiposAS3.put("BLOB_SUB_TYPE_0", "String");
		tiposAS3.put("BLOB_SUB_TYPE_>1", "String");
		tiposAS3.put("BLOB_SUB_TYPE_1", "String");
		tiposAS3.put("BLOB_SUB_TYPE_2", "String");
		tiposAS3.put("CHAR", "String");
		tiposAS3.put("VARCHAR", "String");
		tiposAS3.put("TEXT", "String");
		tiposAS3.put("BPCHAR", "String");
				
		tiposAS3.put("DATE", "Date");
		tiposAS3.put("TIMESTAMP", "Date");
		tiposAS3.put("TIMESTAMPTZ", "Date");
				
		tiposAS3.put("FLOAT", "Number");
		tiposAS3.put("FLOAT8", "Number");
		tiposAS3.put("NUMERIC", "Number");
		tiposAS3.put("DOUBLE_PRECISION", "Number");
						
		tiposAS3.put("INTEGER", "int");
		tiposAS3.put("SMALLINT", "int");
		tiposAS3.put("INT4", "int");
		tiposAS3.put("INT2", "int");
				
		tiposAS3.put("BLOB", "ByteArray");
		tiposAS3.put("BYTEA", "ByteArray");

		tiposAS3.put("_INT4", "Array");
		tiposAS3.put("_INT2", "Array");
		
		result.addObject("AS3_TYPES", tiposAS3);
		
		/**
		 * TIPOS TYPESCRIPT
		 */
		HashMap<String, String> tiposTS = new HashMap<String, String>();
		
		tiposTS.put("BLOB_SUB_TYPE_0", "string");
		tiposTS.put("BLOB_SUB_TYPE_>1", "string");
		tiposTS.put("BLOB_SUB_TYPE_1", "string");
		tiposTS.put("BLOB_SUB_TYPE_2", "string");
		tiposTS.put("CHAR", "string");
		tiposTS.put("VARCHAR", "string");
		tiposTS.put("TEXT", "string");
		tiposTS.put("BPCHAR", "string");
				
		tiposTS.put("DATE", "any");
		tiposTS.put("TIMESTAMP", "any");
		tiposTS.put("TIMESTAMPTZ", "any");
				
		tiposTS.put("FLOAT", "number");
		tiposTS.put("FLOAT8", "number");
		tiposTS.put("NUMERIC", "number");
		tiposTS.put("DOUBLE_PRECISION", "number");
						
		tiposTS.put("INTEGER", "number");
		tiposTS.put("SMALLINT", "number");
		tiposTS.put("INT4", "number");
		tiposTS.put("INT2", "number");
				
		tiposTS.put("BLOB", "any");
		tiposTS.put("BYTEA", "any");

		tiposTS.put("_INT4", "Array");
		tiposTS.put("_INT2", "Array");
		
		result.addObject("TS_TYPES", tiposTS);
		
		/**
		 * TIPOS JAVA
		 */
		HashMap<String, String> tiposJava = new HashMap<String, String>();
		tiposJava.put("BLOB_SUB_TYPE_0", "String");
		tiposJava.put("BLOB_SUB_TYPE_>1", "String");
		tiposJava.put("BLOB_SUB_TYPE_1", "String");
		tiposJava.put("BLOB_SUB_TYPE_2", "String");
		tiposJava.put("CHAR", "String");
		tiposJava.put("VARCHAR", "String");
		tiposJava.put("TEXT", "String");
		tiposJava.put("BPCHAR", "String");
				
		tiposJava.put("DATE", "GregorianCalendar");
		tiposJava.put("TIMESTAMP", "GregorianCalendar");
		tiposJava.put("TIMESTAMPTZ", "GregorianCalendar");
				
		tiposJava.put("FLOAT", "Double");
		tiposJava.put("FLOAT8", "Double");
		tiposJava.put("NUMERIC", "Double");
		tiposJava.put("DOUBLE_PRECISION", "Double");
						
		tiposJava.put("INTEGER", "int");
		tiposJava.put("SMALLINT", "int");
		tiposJava.put("INT4", "int");
		tiposJava.put("INT2", "int");
				
		tiposJava.put("BLOB", "byte[]");
		tiposJava.put("BYTEA", "byte[]");
		
		tiposJava.put("INT8", "Long");

		result.addObject("JAVA_TYPES", tiposJava);
		
		return result;
	}
	
	
	public static ArrayList<String> getFields(String nmTable, Connection connect){
		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
		
			PreparedStatement pstmt;
		    ResultSet rs;
		    ResultSetMetaData rsmd;
		    ArrayList<String> fields = new ArrayList<String>();
		    try {
				pstmt = connect.prepareStatement("SELECT * FROM " + nmTable.toUpperCase() + " WHERE 1<>1");
				rs = pstmt.executeQuery();
				rsmd = rs.getMetaData();
	
				for(int i=1; i<=rsmd.getColumnCount(); i++)
					fields.add(rsmd.getColumnName(i).toLowerCase());
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return fields;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ArrayList<String> getPrimaryKeys(String nmTable, Connection connect){
		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			DatabaseMetaData dmd = connect.getMetaData();
			ArrayList<String> fields = new ArrayList<String>();
			ArrayList<String> fs = getFields(nmTable, connect);
			
			ResultSet rs = dmd.getPrimaryKeys(connect.getCatalog(), null, nmTable);
			
			while (rs != null && rs.next()) {
				boolean incluir = false;
				for (String fieldName : fs) {
					if(fieldName.equalsIgnoreCase(rs.getString("COLUMN_NAME")))
						incluir = true;
				}
				
				if(incluir)
					fields.add(rs.getString("COLUMN_NAME").toLowerCase());
			}
			return fields;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static boolean hasField(ArrayList<String> fields, String nmField) {
		try {
			return fields!=null && fields.indexOf(nmField.toLowerCase())>=0;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static ArrayList<String> getNativePrimaryKeys(String nmTable, Connection connect){
		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			DatabaseMetaData dmd = connect.getMetaData();
			ArrayList<String> fields = new ArrayList<String>();
			
			ResultSet rs = dmd.getPrimaryKeys(connect.getCatalog(), null, nmTable);
			while (rs != null && rs.next())
				fields.add(rs.getString("COLUMN_NAME").toLowerCase());
			
			ArrayList<String> foreignKeys = getForeignKeys(nmTable, connect);
			for (int i=0; fields!=null && i<fields.size() && foreignKeys!=null && foreignKeys.size()>0; i++) {
				if (foreignKeys.indexOf(fields.get(i).toLowerCase()) != -1) {
					fields.remove(i);
					i--;
				}
			}
			return fields;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	private static ArrayList<String> getImportedPrimaryKeys(String nmTable, Connection connect){
		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			DatabaseMetaData dmd = connect.getMetaData();
			ArrayList<String> fields = new ArrayList<String>();
			
			ResultSet rs = dmd.getPrimaryKeys(connect.getCatalog(), null, nmTable);
			while (rs != null && rs.next())
				fields.add(rs.getString("COLUMN_NAME").toLowerCase());
			
			ArrayList<String> foreignKeys = getForeignKeys(nmTable, connect);
			for (int i=0; fields!=null && i<fields.size(); i++) {
				if (foreignKeys.indexOf(fields.get(i).toLowerCase()) == -1) {
					fields.remove(i);
					i--;
				}
			}
			return fields;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static ArrayList<String> getForeignKeys(String nmTable, Connection connect){
		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
		    ResultSet rs;
		    ArrayList<String> fields = new ArrayList<String>();
		    
			//tabela
			DatabaseMetaData dbmd = connect.getMetaData();
			rs = dbmd.getImportedKeys(connect.getCatalog(), "", nmTable);

			while (rs.next())
				if (rs.getString("FK_NAME") != null)
					fields.add(rs.getString("FKCOLUMN_NAME").toLowerCase());
			

			return fields;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static HashMap<String, String> getFieldTypes(String nmTable, Connection connect){
		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			PreparedStatement pstmt;
		    ResultSet rs;
		    ResultSetMetaData rsmd;
		    HashMap<String, String> types = new HashMap<String, String>();
		    
			pstmt = connect.prepareStatement("SELECT * FROM " + nmTable.toUpperCase() + " WHERE 1<>1");
			rs = pstmt.executeQuery();
			rsmd = rs.getMetaData();

			for(int i=1; i<=rsmd.getColumnCount(); i++)
				types.put(rsmd.getColumnName(i).toLowerCase(), rsmd.getColumnTypeName(i).replaceAll(" ", "_").toUpperCase());
			
			return types;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static boolean hasDateFields(String nmTable, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			PreparedStatement pstmt;
		    ResultSet rs;
		    ResultSetMetaData rsmd;
	
			pstmt = connect.prepareStatement("SELECT * FROM " + nmTable.toUpperCase() + " WHERE 1<>1");
			rs = pstmt.executeQuery();
			rsmd = rs.getMetaData();
	
			for(int i=1; i<=rsmd.getColumnCount(); i++)
				if (rsmd.getColumnType(i) == Types.DATE || rsmd.getColumnType(i) == Types.TIME || rsmd.getColumnType(i) == Types.TIMESTAMP)
					return true;
			return false;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static boolean hasByteFields(String nmTable, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			PreparedStatement pstmt;
		    ResultSet rs;
		    ResultSetMetaData rsmd;
		    
			pstmt = connect.prepareStatement("SELECT * FROM " + nmTable.toUpperCase() + " WHERE 1<>1");
			rs = pstmt.executeQuery();
			rsmd = rs.getMetaData();

			for(int i=1; i<=rsmd.getColumnCount(); i++){
				//System.out.println(rsmd.getColumnType(i)+":"+rsmd.getColumnTypeName(i));
				if (rsmd.getColumnType(i) == Types.BLOB || rsmd.getColumnType(i) == Types.BINARY || rsmd.getColumnTypeName(i).toUpperCase().equals("BYTEA"))
					return true;
			}
			return false;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getData(String nmTable, int inicio, int fim, ArrayList<String> columns, String orderBy){
		return getData(nmTable, inicio, fim, columns, orderBy, null);
	}
	public static ResultSetMap getData(String nmTable, int inicio, int fim, ArrayList<String> columns, String orderBy, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}

			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(fim, inicio);
			PreparedStatement pstmt;
			
			ArrayList<String> cleanColumns = new ArrayList<String>();
			for(String column : columns){
				if(column.toLowerCase().indexOf("blb") == -1 && column.toLowerCase().indexOf("img") == -1){
					cleanColumns.add(column);
				}
			}
			
			pstmt = connect.prepareStatement("SELECT "+sqlLimit[0]+" " + String.join(", ", cleanColumns) + " FROM " + nmTable.toUpperCase() + (!orderBy.equals("") ? " ORDER BY " + orderBy : "") + " " + sqlLimit[1]);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getTableCount(String nmTable){
		return getTableCount(nmTable, null);
	}
	public static ResultSetMap getTableCount(String nmTable, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}

			PreparedStatement pstmt;
		    
			pstmt = connect.prepareStatement("SELECT COUNT(*) FROM " + nmTable.toUpperCase());
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			connect.close();
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	private static String getVariableNameByFieldName(String field){
		String varName = "";
		StringTokenizer token = new StringTokenizer(field.toLowerCase(),"_");
		for(int i=0; token.hasMoreTokens(); i++){
			if(i==0)
				varName+=token.nextToken();
			else
				varName+=capitular(token.nextToken());
		}
		return varName;
	}
	
	private static String getClassNameByTableName(String table, boolean includePrefix){
		String tableName = "";
		StringTokenizer token = new StringTokenizer(table.toLowerCase(),"_");
		if (!includePrefix)
			token.nextToken();
		while(token.hasMoreTokens())
			tableName+=capitular(token.nextToken());
		return tableName;
	}
	
	private static String getPackageNameByTableName(String table, String prefix){
		StringTokenizer token = new StringTokenizer(table.toLowerCase(),"_");
		return (prefix==null) ? token.nextToken() : prefix+token.nextToken();
	}
	
	private static String capitular(String texto){
	  	return (texto.length()>0)?texto.substring(0,1).toUpperCase()+texto.substring(1):texto;
	}
	
	
	/**
	 * Compara os campos de dois objetos (do mesmo tipo) e retorna uma lista com o nome dos campos (underscore) diferentes
	 * @param item1
	 * @param item2
	 * @return
	 */
	public static ArrayList<String> getDiff(Object item1, Object item2) {
		try {
			if(item1.getClass()!=item2.getClass())
				return null;
			
			ArrayList<String> diff = new ArrayList<>();
			
			Field[] field1 = item1.getClass().getDeclaredFields();
			Field[] field2 = item2.getClass().getDeclaredFields();
			
			for(int i=0; i<field1.length; i++) {
				field1[i].setAccessible(true);
				field2[i].setAccessible(true);
				
				if(field1[i].getType() == GregorianCalendar.class) {
					GregorianCalendar vl1 = (GregorianCalendar)field1[i].get(item1);
					GregorianCalendar vl2 = (GregorianCalendar)field2[i].get(item2);
										
					if(vl1==null || vl2==null)
						continue;
					
					if(Util.compareDates(vl1, vl2)!=0)
						diff.add(DeveloperServices.camelCaseToUnderscore(field1[i].getName()));
					
				} else if (field1[i].getType() == int.class) {
					int vl1 = field1[i].getInt(item1);
					int vl2 = field2[i].getInt(item2);
					
					if(vl1==0 || vl2==0)
						continue;
					
					if(vl1!=vl2)
						diff.add(DeveloperServices.camelCaseToUnderscore(field1[i].getName()));
					
				} else if (field1[i].getType() == String.class) {
					String vl1 = (String)field1[i].get(item1);
					String vl2 = (String)field2[i].get(item2);
										
					if(vl1==null || vl2==null)
						continue;
					
					if(!vl1.equals(vl2))
						diff.add(DeveloperServices.camelCaseToUnderscore(field1[i].getName()));
					
				} else if (field1[i].getType() == double.class) {
					double vl1 = field1[i].getDouble(item1);
					double vl2 = field2[i].getDouble(item2);
					
					if(vl1==0 || vl2==0)
						continue;
					
					if(vl1!=vl2)
						diff.add(DeveloperServices.camelCaseToUnderscore(field1[i].getName()));
				} else if (field1[i].getType() == Double.class) {
					Double vl1 = field1[i].getDouble(item1);
					Double vl2 = field2[i].getDouble(item2);
					
					if(vl1==0 || vl2==0)
						continue;
					
					if(vl1!=vl2)
						diff.add(DeveloperServices.camelCaseToUnderscore(field1[i].getName()));
				}
			}
			
			return diff;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
}