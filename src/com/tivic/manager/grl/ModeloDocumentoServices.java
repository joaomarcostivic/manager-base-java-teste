package com.tivic.manager.grl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.UrlValidator;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLException;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.POIXMLPropertiesTextExtractor;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageProperties;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.OneDriveClient;
import sol.util.Result;

import com.tivic.manager.adm.ModeloContrato;
import com.tivic.manager.adm.ModeloContratoDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.prc.Processo;
import com.tivic.manager.prc.ProcessoDAO;
import com.tivic.manager.prc.ProcessoServices;
import com.tivic.manager.prc.TipoDocumento;
import com.tivic.manager.prc.TipoDocumentoDAO;
import com.tivic.manager.print.Converter;
import com.tivic.manager.util.OneDriveUtils;
import com.tivic.manager.util.Util;

public class ModeloDocumentoServices {

	/* tipos de modelos */
	public static final int TP_CONTRATO = 0;
	public static final int TP_DOC_ELETRONICO = 1;
	public static final int TP_PRE_IMPRESSO = 2;
	public static final int TP_MAILING = 3;
	public static final int TP_DOCUMENTO_PADRAO = 4;
	public static final int TP_WORD = 5;
	public static final int TP_AUTOTEXTO = 6;
	
	public static final int TP_DOCUMENTO_PADRAO_WEB = 7;
	public static final int TP_AUTOTEXTO_WEB = 8;
	
	public static final String[] tipoModelo = {"Contrato",
		"Documento Eletrônico",
		"Pré-Impresso",
		"Mailing",
		"Documento Padrão",
		"Word",
		"Autotexto",
		"Documento Padrão (Web)",
		"Autotexto (Web)"};

	/* situação de modelos */
	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO = 1;
	
	
	public static ResultSetMap getAllByTipo(int tpModelo) {
		return getAllByTipo(tpModelo, null);
	}

	public static ResultSetMap getAllByTipo(int tpModelo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("TP_MODELO", Integer.toString(tpModelo), ItemComparator.EQUAL, Types.SMALLINT));
			return ModeloDocumentoDAO.find(criterios, connection);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoServices.getAllByTipo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result remove(int cdModelo) {
		int code = delete(cdModelo, null);
		return new Result(code, code<=0 ? "Erro ao excluir modelo de documento" : "Modelo excluído com sucesso.");
	}
	
	public static int delete(int cdModelo) {
		return delete(cdModelo, null);
	}

	public static int delete(int cdModelo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(cdModelo<=0)
				return -1;

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			//datasources
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_modelo_fonte_dados WHERE cd_modelo=?");
			pstmt.setInt(1, cdModelo);
			pstmt.executeUpdate();

			if(ModeloDocumentoDAO.delete(cdModelo, connect)<=0){
				Conexao.rollback(connect);
				return -2;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoServices.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -3;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int setBlbModelo(byte[] blbModelo, int cdModelo) {
		return setBlbModelo(blbModelo, cdModelo, null);
	}

	public static int setBlbModelo(byte[] blbModelo, int cdModelo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_modelo_documento SET blb_conteudo=? WHERE cd_modelo=?");
			if(blbModelo==null)
				pstmt.setNull(1, Types.BINARY);
			else
				pstmt.setBytes(1, blbModelo);
			pstmt.setInt(2, cdModelo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoServices.setBlbModelo: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoServices.setBlbModelo: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int resetBlbModelo(int cdModelo) {
		return resetBlbModelo(cdModelo, null);
	}

	public static int resetBlbModelo(int cdModelo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			return setBlbModelo(null, cdModelo, connect);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoServices.resetBlbModelo: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static byte[] getBlbModelo(int cdModelo) {
		return getBlbModelo(cdModelo, false, null);
	}
	
	public static byte[] getBlbModelo(int cdModelo, boolean convert) {
		return getBlbModelo(cdModelo, convert, null);
	}

	public static byte[] getBlbModelo(int cdModelo, boolean convert, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			ModeloDocumento modelo = ModeloDocumentoDAO.get(cdModelo, connection);
			
			
			if(convert) {
				
				byte[] blbConteudo = modelo.getBlbConteudo();
				
				
					
				if(blbConteudo!=null) {
					
					if(modelo.getTpModelo()==TP_WORD) {
						blbConteudo = Converter.docxToHtml(blbConteudo);
					}
					else {
						//blbConteudo = ((String)register.get("BLB_CONTEUDO")).getBytes("UTF-8");
						
						if((new String(blbConteudo)).indexOf("\\rtf1")!=-1)
							blbConteudo = Converter.rtfToHtml(blbConteudo);
					}
					
					if(blbConteudo==null)
						blbConteudo = "[Este conteúdo não pôde ser convertido.]".getBytes();
				}
				else
					blbConteudo = "[Este conteúdo é nulo.]".getBytes();
				
				modelo.setBlbConteudo(blbConteudo);
			}
			
			return modelo==null ? null : modelo.getBlbConteudo();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoServices.getBlbModelo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static String getTxtConteudo(int cdModelo) {
		return getTxtConteudo(cdModelo, null);
	}

	public static String getTxtConteudo(int cdModelo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			ModeloDocumento modelo = ModeloDocumentoDAO.get(cdModelo, connection);
			return modelo==null ? null : modelo.getTxtConteudo();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoServices.getTxtConteudo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getFontesDados(int cdModelo) {
		return getFontesDados(cdModelo, null);
	}

	public static ResultSetMap getFontesDados(int cdModelo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt = connect.prepareStatement("SELECT B.*, A.cd_fonte_pai " +
				      "FROM grl_modelo_fonte_dados A, grl_fonte_dados B " +
				      "WHERE A.cd_fonte = B.cd_fonte " +
				      "  AND A.cd_modelo = ?");
			pstmt.setInt(1, cdModelo);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoServices.getFontesDados: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll(int tpModelo, int cdTipoDocumento) {
		return getAll(tpModelo, cdTipoDocumento, null);
	}
	
	public static ResultSetMap getAll(int tpModelo) {
		return getAll(tpModelo, -1, null);
	}

	public static ResultSetMap getAll() {
		return getAll(-1, -1, null);
	}

	public static ResultSetMap getAll(int tpModelo, int cdTipoDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.cd_modelo, A.nm_modelo, A.txt_modelo, A.tp_modelo," +
					" A.txt_conteudo, A.st_modelo, A.cd_tipo_documento, " +
					" B.nm_tipo_documento  " +
					" FROM grl_modelo_documento A" +
					" LEFT OUTER JOIN gpn_tipo_documento B ON (A.cd_tipo_documento = B.cd_tipo_documento) " +
					" WHERE st_modelo=1 " +
					(tpModelo > -1 ? " AND A.tp_modelo = "+tpModelo : "") +
					(cdTipoDocumento > -1 ? " AND A.cd_tipo_documento = "+cdTipoDocumento : "") +
					" ORDER BY B.nm_tipo_documento, A.nm_modelo");
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllModelsNotPreImpressos() {
		return getAllModelsNotPreImpressos(null);
	}

	public static ResultSetMap getAllModelsNotPreImpressos(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("TP_MODELO", Integer.toString(TP_PRE_IMPRESSO), ItemComparator.DIFFERENT, Types.SMALLINT));
			ResultSetMap rsm = ModeloDocumentoDAO.find(criterios, connection);
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_MODELO");
			rsm.orderBy(fields);
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoServices.getAllModelsNotPreImpressos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllFontesDados(int cdModelo) {
		return getAllFontesDados(cdModelo, null);
	}

	public static ResultSetMap getAllFontesDados(int cdModelo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.cd_fonte_pai, B.* " +
					"FROM grl_modelo_fonte_dados A, grl_fonte_dados B " +
					"WHERE A.cd_fonte = B.cd_fonte " +
					"  AND A.cd_modelo = ?");
			pstmt.setInt(1, cdModelo);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while (rsm != null && rsm.next()) {
				if (rsm.getInt("cd_fonte_pai") != 0) {
					int pointer = rsm.getPointer();
					int cdFonte = rsm.getInt("cd_fonte_pai");
					HashMap<String,Object> register = rsm.getRegister();
					if (rsm.locate("cd_fonte", new Integer(rsm.getInt("cd_fonte_pai")), false, true)) {
						HashMap<String,Object> parentNode = rsm.getRegister();
						boolean isFound = rsm.getInt("cd_fonte")==cdFonte;
						ResultSetMap subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
						while (!isFound && subRsm!=null) {
							subRsm = parentNode==null ? null : (ResultSetMap)parentNode.get("subResultSetMap");
							parentNode = subRsm==null ? null : subRsm.getRegister();
							isFound = subRsm==null ? false : subRsm.getInt("cd_fonte")==cdFonte;
						}
						subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
						if (subRsm==null) {
							subRsm = new ResultSetMap();
							parentNode.put("subResultSetMap", subRsm);
						}
						subRsm.beforeFirst();
						subRsm.addRegister(register);
						rsm.getLines().remove(register);
						pointer--;
					}
					rsm.goTo(pointer);
				}
			}
			rsm.beforeFirst();
			return rsm;
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

	public static Result save(ModeloDocumento modeloDocumento) {
		return save(modeloDocumento, false, null, null);
	}

	public static Result save(ModeloDocumento modeloDocumento, ArrayList<ModeloFonteDados> fontesDados) {
		return save(modeloDocumento, false, fontesDados, null);
	}

	public static Result save(ModeloDocumento modeloDocumento, boolean convert, ArrayList<ModeloFonteDados> fontesDados, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			if(convert){
				if(modeloDocumento.getTpModelo()==TP_WORD)
					modeloDocumento.setBlbConteudo( Converter.htmlToDocx(modeloDocumento.getBlbConteudo()) );
				else
					modeloDocumento.setBlbConteudo( Converter.htmlToRtf(modeloDocumento.getBlbConteudo()) );
				
				if(modeloDocumento.getBlbConteudo() == null)
					return new Result(-2, "Erro ao salvar. Conteúdo não pôde ser convertido.");
			}
			
			int code = -1;
			if(modeloDocumento.getTpModelo() == TP_CONTRATO)
				code = modeloDocumento.getCdModelo() <= 0 ? ModeloContratoDAO.insert((ModeloContrato)modeloDocumento, connection) :
					                                        ModeloContratoDAO.update((ModeloContrato)modeloDocumento, connection);
			else
				code = modeloDocumento.getCdModelo() <= 0 ? ModeloDocumentoDAO.insert(modeloDocumento, connection) :
					                                        ModeloDocumentoDAO.update(modeloDocumento, connection);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao salvar...");
			}

			if (fontesDados!=null) {
				connection.prepareStatement("UPDATE grl_modelo_fonte_dados SET cd_fonte_pai = null " +
											"WHERE cd_modelo = "+modeloDocumento.getCdModelo()).execute();

				connection.prepareStatement("DELETE FROM grl_modelo_fonte_dados WHERE cd_modelo = "+modeloDocumento.getCdModelo()).execute();

				for (int i=0; i<fontesDados.size(); i++) {
					if (ModeloFonteDadosDAO.insert(fontesDados.get(i), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-2, "Erro ao atualizar fonte de dados...");
					}
				}
			}

			if (isConnectionNull)
				connection.commit();

			return new Result(code, "Salvo com sucesso", "MODELODOCUMENTO", modeloDocumento);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public static String getValue(String column, ArrayList<HashMap<String, Object>> resultsFontesDados, ResultSetMap rsmTag) {
		try {
			String columnTemp = column.toString();
			String idResultSetColumn = column.indexOf(".")!=-1 ? column.substring(0, column.indexOf(".")) : "";
			column = column.indexOf(".")!=-1 ? column.substring(column.indexOf(".")+1) : column;
			for (int m=0; resultsFontesDados!=null && m<resultsFontesDados.size(); m++) {
				HashMap<String, Object> result = resultsFontesDados.get(m);
				ResultSetMap rsmTemp = (ResultSetMap)result.get("result");
				if (rsmTemp==null)
					continue;
				if ((idResultSetColumn.equals("") || (result.get("id")!=null && result.get("id").equals(idResultSetColumn))) &&
						rsmTemp.getColumnNames().indexOf(column)!=-1) {
					Scriptable scriptableColumns = (Scriptable)result.get("scriptableColumns");

					if (rsmTemp!=null && rsmTemp!=rsmTag)
						rsmTemp.beforeFirst();
					boolean isFound = rsmTemp==null ? false : rsmTag==rsmTemp ? true : rsmTemp.next();
					String value = !isFound ? "" : rsmTemp.getObject(column)instanceof Float ||
							rsmTemp.getObject(column)instanceof Double ?
							Util.formatNumber(rsmTemp.getFloat(column)) : rsmTemp.getString(column);
					if (scriptableColumns!=null) {
						Object[] objects = scriptableColumns.getIds();
						int countColumns = objects==null ? 0 : objects.length;
						for (int iColumn=0; iColumn<countColumns; iColumn++) {
							Scriptable scriptableColumn = (Scriptable)scriptableColumns.get(iColumn, scriptableColumns);
							if (scriptableColumn!=null && scriptableColumn.has("NM_COLUNA", scriptableColumn) &&
									scriptableColumn.get("NM_COLUNA", scriptableColumn).toString().equals(column)) {
								if (scriptableColumn.has("TXT_APRESENTACAO", scriptableColumn)) {
									Object object = scriptableColumn.get("TXT_APRESENTACAO", scriptableColumn);
									if (object instanceof String) {
										Context cx = Context.enter();
										cx.setLanguageVersion(Context.VERSION_1_2);
										Scriptable scope = cx.initStandardObjects();
										object = cx.evaluateString(scope, "obj = " + object, "", 1, null);
										Scriptable obj = (Scriptable) scope.get("obj", scope);
										if (obj.has("options", obj)) {
											Object options = obj.get("options", obj);
											if (options!=null && options instanceof NativeArray) {
												obj = (Scriptable)options;
												if (value!=null && !value.trim().equals("") && Integer.parseInt(value)>=0 && Integer.parseInt(value)<obj.getIds().length)
													value = obj.get(Integer.parseInt(value.trim()), obj).toString();
											}
										}
										else if (obj.has("sumary", obj)) {
											Object sumary = obj.get("sumary", obj);
											Object type = sumary instanceof Scriptable && ((Scriptable)sumary).has("type", (Scriptable)sumary) ?
													((Scriptable)sumary).get("type", (Scriptable)sumary) : null;
											if (type!=null && type.toString().equals("concat")) {
												value = "";
												int oldPosition = rsmTemp.getPosition();
												rsmTemp.beforeFirst();
												while (rsmTemp.next()) {
													String valueTemp = rsmTemp.getObject(column)instanceof Float ||
															rsmTemp.getObject(column)instanceof Double ?
															Util.formatNumber(rsmTemp.getFloat(column)) : rsmTemp.getString(column);
													if (valueTemp!=null && !valueTemp.trim().equals(""))
														value += (value.equals("") ? "" : ", ") + valueTemp.trim();
												}
												rsmTemp.goTo(oldPosition);
											}
										}
										else if (obj.has("format", obj)) {
											Object format = obj.get("format", obj);
											if (format!=null && format.toString().equals("datetime") && rsmTemp.getObject(column)!=null &&
													rsmTemp.getObject(column) instanceof GregorianCalendar) {
												value = Util.formatDateTime(rsmTemp.getGregorianCalendar(column), "dd/MM/yyyy HH:mm", "");
											}
											else if (format!=null && format.toString().equals("datetime") && rsmTemp.getObject(column)!=null &&
													rsmTemp.getObject(column) instanceof Timestamp) {
												value = Util.formatDateTime(rsmTemp.getTimestamp(column), "dd/MM/yyyy HH:mm");
											}
										}
										Context.exit();
									}
								}
								break;
							}
						}
					}
					return value;
				}
				else {
					if (rsmTemp.getPointer()!=-1 && rsmTemp.getPointer()<rsmTemp.size()) {
						HashMap<String, Object> register = rsmTemp.getRegister();
						ArrayList<HashMap<String, Object>> subResults = (ArrayList<HashMap<String,Object>>)register.get("subResults");
						String value = getValue(columnTemp, subResults, rsmTag);
						if (value!=null)
							return value;
					}
				}
			}
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	public static void executeFonteDados(HashMap<String, String> parametros, HashMap<String, Object> register,
			HashMap<String, Object> parentRegister, ArrayList<HashMap<String, Object>> results, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			String script = (String)register.get("TXT_SCRIPT");
			Set<String> keyset = parentRegister!=null ? parentRegister.keySet() : parametros==null ? null : parametros.keySet();
			Iterator<String> it = keyset==null ? null : keyset.iterator();
			while(it!=null && it.hasNext()){
				String key = it.next();
				script = script.replaceAll((parentRegister!=null ? "#" : "") + key.toUpperCase(), parentRegister!=null ? parentRegister.get(key).toString() : parametros.get(key));
			}
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("id", (String)register.get("ID_FONTE"));
			ResultSetMap rsmResults = register!=null && register.get("TP_ORIGEM")!=null && Integer.parseInt(register.get("TP_ORIGEM").toString())==FonteDadosServices.OR_SCRIPT_SQL ?
					FonteDadosServices.executeFonteDados(script, connection) : FonteDadosServices.executeFonteDadosFunc(script);
			hash.put("result", rsmResults);
			String txtColumns = (String)register.get("TXT_COLUMNS");
			if (txtColumns!=null && !txtColumns.trim().equals("")) {
				txtColumns = txtColumns.trim();
				try {
					Context cx = Context.enter();
					cx.setLanguageVersion(Context.VERSION_1_2);
					Scriptable scope = cx.initStandardObjects();
					cx.evaluateString(scope, "obj = " + txtColumns, "", 1, null);
					Scriptable obj = (Scriptable) scope.get("obj", scope);
					hash.put("scriptableColumns", obj);
				}
				catch(Exception e) {
					e.printStackTrace(System.out);
				}
			}

			ResultSetMap subFontesDados = (ResultSetMap)register.get("subResultSetMap");
			if (subFontesDados!=null && rsmResults!=null) {
				while (rsmResults.next()) {
					subFontesDados.beforeFirst();
					ArrayList<HashMap<String, Object>> subResults = new ArrayList<HashMap<String,Object>>();
					rsmResults.getRegister().put("subResults", subResults);
					while (subFontesDados!=null && subFontesDados.next()) {
						executeFonteDados(parametros, subFontesDados.getRegister(), rsmResults.getRegister(), subResults, connection);
					}
				}
			}
			if (rsmResults!=null)
				rsmResults.beforeFirst();

			results.add(hash);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoServices.executeModelo: " +  e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static String executeBand(String content, int level, ArrayList<HashMap<String, Object>> resultsFontesDados, ResultSetMap rsmRepeat) {
		try {
			Parser parser = new Parser(content);
			NodeList list = parser.parse(new AndFilter(new TagNameFilter("tr"),
					new AndFilter(new HasAttributeFilter("repeat"), new HasAttributeFilter("level", Integer.toString(level+1)))));
			Node[] arrayNodes = list.toNodeArray();
			String contentTemp = arrayNodes.length==0 ? content : content.substring(0, arrayNodes[0].getStartPosition());
			for (int i=0; arrayNodes!=null && i<arrayNodes.length; i++) {
				Node node = arrayNodes[i];
				Tag tag = node instanceof Tag ? (Tag)node : null;
				String idResultSetRepeat = tag==null ? null : tag.getTagName().equalsIgnoreCase("repeat") && tag.getAttribute("for")!=null ?
						tag.getAttribute("for") : tag.getAttribute("for");
				contentTemp += executeBandForResultSet(tag.toHtml(), level+1, resultsFontesDados, rsmRepeat, idResultSetRepeat);
				if (i<arrayNodes.length-1)
					contentTemp += content.substring(tag!=null && tag.getEndTag()!=null ? tag.getEndTag().getEndPosition() : node.getEndPosition(), arrayNodes[i+1].getStartPosition());
				else
					contentTemp += content.substring(tag!=null && tag.getEndTag()!=null ? tag.getEndTag().getEndPosition() : node.getEndPosition(), content.length());
			}

			Pattern pattern = Pattern.compile("#(.*?)#");
			Matcher matcher = pattern.matcher(contentTemp);
			ArrayList<Object[]> groups = new ArrayList<Object[]>();
			while (matcher.find()) {
				groups.add(new Object[] {matcher.group(1), matcher.start(), matcher.end()});
			}
			String finalBand = groups.size()<=0 ? contentTemp : contentTemp.substring(0, (Integer)(groups.get(0)[1]));
			for (int k=0; groups!=null && k<groups.size(); k++) {
				Object[] group = groups.get(k);
				String column = (String)group[0];
				String value = getValue(column, resultsFontesDados, rsmRepeat);
				value = value==null ? "" : value;
				finalBand += value;
				if (k<groups.size()-1) {
					Object[] nextGroup = groups.get(k+1);
					finalBand += contentTemp.substring((Integer)group[2], (Integer)nextGroup[1]);
				}
				else
					finalBand += contentTemp.substring((Integer)group[2], contentTemp.length());
			}
			return finalBand;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return "";
		}
	}

	@SuppressWarnings("unchecked")
	public static String executeBandForResultSet(String band, int level, ArrayList<HashMap<String, Object>> resultsFontesDados,
			ResultSetMap rsmRepeatParent, String idResultSetRepeat) {
		try {
			ResultSetMap rsmRepeat = null;
			if (rsmRepeatParent!=null) {
				HashMap<String, Object> register = rsmRepeatParent.getRegister();
				if (register!=null && register.get("subResults")!=null) {
					ArrayList<HashMap<String, Object>> subResults = (ArrayList<HashMap<String,Object>>)register.get("subResults");
					for (int k=0; k<subResults.size(); k++)
						if (((String)subResults.get(k).get("id")).equalsIgnoreCase(idResultSetRepeat) && subResults.get(k).get("result")!=null) {
							rsmRepeat = ((ResultSetMap)subResults.get(k).get("result"));
							break;
						}
				}
			}

			for (int j=0; rsmRepeat==null && j<resultsFontesDados.size(); j++) {
				if (resultsFontesDados.get(j).get("id")!=null && ((String)resultsFontesDados.get(j).get("id")).equalsIgnoreCase(idResultSetRepeat)) {
					rsmRepeat = (ResultSetMap)resultsFontesDados.get(j).get("result");
					break;
				}
			}

			String content = "";
			if (rsmRepeat!=null)
				rsmRepeat.beforeFirst();
			while (rsmRepeat!=null && rsmRepeat.next()) {
				/* não definitivo */
				HashMap<String, Object> register = rsmRepeat.getRegister();
				if (register!=null && register.get("subResults")!=null) {
					ArrayList<HashMap<String, Object>> subResults = (ArrayList<HashMap<String,Object>>)register.get("subResults");
					for (int k=0; k<subResults.size(); k++)
						if (subResults.get(k).get("result")!=null) {
							((ResultSetMap)subResults.get(k).get("result")).beforeFirst();
						}
				}
				String contentTemp = executeBand(band, level, resultsFontesDados, rsmRepeat);
				System.out.println(idResultSetRepeat + " - " + rsmRepeat.getPointer());
				if (level==1)
					System.out.println(band + " - " + contentTemp);
				content += contentTemp;
			}
			return content;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return "";
		}
	}

	public static String executeModelo(int cdModelo, HashMap<String, String> parametros) {
		return executeModelo(cdModelo, parametros, null);
	}

	public static String executeModelo(int cdModelo, HashMap<String, String> parametros, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			ResultSetMap rsm = ModeloDocumentoServices.getAllFontesDados(cdModelo, connection);
			ArrayList<HashMap<String, Object>> resultsFontesDados = new ArrayList<HashMap<String,Object>>();

			rsm.beforeFirst();
			while (rsm!=null && rsm.next())
				executeFonteDados(parametros, rsm.getRegister(), null, resultsFontesDados, connection);

			String content = getTxtConteudo(cdModelo, connection);

			try {
				Parser parser = new Parser(content);
				NodeList list = parser.parse(new AndFilter(new TagNameFilter("tr"),
						new AndFilter(new HasAttributeFilter("repeat"), new HasAttributeFilter("level", "0"))));
				Node[] arrayNodes = list.toNodeArray();
				String contentTemp = arrayNodes.length==0 ? content : content.substring(0, arrayNodes[0].getStartPosition());

				for (int i=0; arrayNodes!=null && i<arrayNodes.length; i++) {
					Node node = arrayNodes[i];
					Tag tag = node instanceof Tag ? (Tag)node : null;
					String idResultSetRepeat = tag==null ? null : tag.getTagName().equalsIgnoreCase("repeat") && tag.getAttribute("for")!=null ?
							tag.getAttribute("for") : tag.getAttribute("for");
					contentTemp += executeBandForResultSet(tag.toHtml(), 0, resultsFontesDados, null, idResultSetRepeat);
					if (i<arrayNodes.length-1)
						contentTemp += content.substring(tag!=null && tag.getEndTag()!=null ? tag.getEndTag().getEndPosition() : node.getEndPosition(), arrayNodes[i+1].getStartPosition());
					else
						contentTemp += content.substring(tag!=null && tag.getEndTag()!=null ? tag.getEndTag().getEndPosition() : node.getEndPosition(), content.length());
				}
				content = contentTemp;
			}
			catch(Exception e) {
				e.printStackTrace(System.out);
			}

			Pattern pattern = Pattern.compile("#(.*?)#");
			Matcher matcher = pattern.matcher(content);
			ArrayList<Object[]> groups = new ArrayList<Object[]>();
			while (matcher.find()) {
				groups.add(new Object[] {matcher.group(1), matcher.start(), matcher.end()});
			}
			String finalContent = groups.size()<=0 ? content : content.substring(0, (Integer)(groups.get(0)[1]));
			for (int i=0; groups!=null && i<groups.size(); i++) {
				Object[] group = groups.get(i);
				String column = (String)group[0];
				String value = getValue(column, resultsFontesDados, null);
				value = value==null ? "" : value;
				finalContent += value;
				if (i<groups.size()-1) {
					Object[] nextGroup = groups.get(i+1);
					finalContent += content.substring((Integer)group[2], (Integer)nextGroup[1]);
				}
				else
					finalContent += content.substring((Integer)group[2], content.length());
			}
			return finalContent;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoServices.executeModelo: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getModelosWeb() {
		return getModelosWeb(0, null);
	}
	
	public static ResultSetMap getModelosWeb(int cdEmpresa) {
		return getModelosWeb(cdEmpresa, null);
	}
	
	public static ResultSetMap getModelosWeb(int cdEmpresa, Connection connect) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.TP_MODELO", TP_AUTOTEXTO_WEB+", "+TP_DOCUMENTO_PADRAO_WEB, ItemComparator.IN, Types.INTEGER));
		if(cdEmpresa>0)
			criterios.add(new ItemComparator("A.CD_EMPRESA", cdEmpresa+"", ItemComparator.EQUAL, Types.INTEGER));
		
		return find(criterios, connect);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		
		String sql = "SELECT A.cd_modelo, A.nm_modelo, A.txt_modelo, A.tp_modelo," +
					" A.txt_conteudo, A.st_modelo, A.cd_tipo_documento, B.nm_tipo_documento  " +
					" FROM grl_modelo_documento A" +
					" LEFT OUTER JOIN gpn_tipo_documento B ON (A.cd_tipo_documento = B.cd_tipo_documento) ";
		return Search.find(sql, "ORDER BY tp_modelo, nm_modelo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	/**
	 * Executa modelo de documento de processo.
	 * 
	 * Um modelo de documento pode estar armazenado no banco de dados em formato docx (blbConteudo byte[]), pode conter uma url 
	 * indicando um link na internet onde arquivo está armazenado (urlModelo String), ou ainda, pode conter um ID de repositório que 
	 * poderá ser usado para buscar o arquivo em um repositório na nuvem (idRepositorio String). 
	 * Inicialmente o repositório utilizado é o OneDrive, podendo ser implementados outras integrações futuramente.
	 * 
	 * @param cdModelo	 código do modelo de documento
	 * @param cdProcesso código do processo do qual o modelo será executado
	 * @param cdEmpresa	 código da empresa
	 * @param lgPdf		 se true, o método retorna um .pdf, senão, retorna um .docx
	 * @return Result	 objeto "FILE" contendo o modelo executado (byte[])
	 * @since 09/06/2014
	 * @author Maurício Cordeiro
	 * @author Leonardo Sapucaia
	 */
	public static Result executarModeloProcesso(int cdModelo, int cdProcesso, int cdEmpresa, boolean lgPdf, boolean lgOneDrive) {
		return executarModeloProcesso(cdModelo, cdProcesso, cdEmpresa, lgPdf, lgOneDrive, null);
	}
	
	public static Result executarModeloProcesso(int cdModelo, int cdProcesso, int cdEmpresa, boolean lgPdf, boolean lgOneDrive, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			ModeloDocumento modelo = ModeloDocumentoDAO.get(cdModelo, connect);
			
			//XXX:
			System.out.println("\t\t\t> modelo carregado");
			
			if(modelo==null)
				return new Result(-2, "O modelo indicado não existe.");
			
			byte[] blbModelo = null;
			
			OneDriveClient odc = null;
			if(lgOneDrive) {
				odc = OneDriveUtils.getClient(connect);
				if(odc==null) {
					return new Result(-3, "Erro ao conectar no OneDrive.");
				}
			}
			
			//VERIFICAR SE URLMODELO É UMA URL VÁLIDA. CASO POSITIVO, BUSCAR O DOCUMENTO NA INTERNET
			if(modelo.getUrlModelo()!=null && !modelo.getUrlModelo().equals("")) {
				UrlValidator urlValidator = new UrlValidator(new String[] {"http","https"});
				
				if(!urlValidator.isValid(modelo.getUrlModelo()))
					return new Result(-4, "A URL informada no modelo é inválida.");
				
				//download arquivo via http
				//blbModelo = xxxxxx
				
			} //VERIFICAR SE EXISTE UM ACCESSTOKEN PARA O REPOSITORIO ONEDRIVE, CASO POSITIVO, BUSCAR ARQUIVO NO REPOSITÓRIO.
			else if(modelo.getIdRepositorio()!=null && !modelo.getIdRepositorio().equals("")) {
				
				
				//odc.listFolder(null);
				blbModelo = odc.downloadFile(modelo.getIdRepositorio());
				
			} //BUSCAR ARQUIVO NO BANCO
			else 
				blbModelo = modelo.getBlbConteudo();
			
			HashMap<String, String> fieldMap = ProcessoServices.getFieldsMapByProcesso(cdProcesso, cdEmpresa);
			ByteArrayInputStream in = new ByteArrayInputStream(blbModelo);
	
			ByteArrayOutputStream baos = null;
			
			try {
				XWPFDocument document = new XWPFDocument(in);
				
				//cria .pdf ou .docx
				baos = xwpfConverter(document, fieldMap);
				
			}
			catch(POIXMLException e) {
				HWPFDocument document = new HWPFDocument(in);
				baos = hwpfConverter(document, fieldMap);
			}
			
			if(baos==null) {
				return new Result(-5, "O modelo não está em formato válido.");
			}
			
			byte[] file = null;
			if(lgPdf)
				file = Converter.docxToPdf(baos.toByteArray());
			else
				file = baos.toByteArray();
			
//			FileOutputStream fos = new FileOutputStream(new File("C:\\Users\\Maurício\\Downloads\\"+modelo.getNmModelo()+new GregorianCalendar().getTimeInMillis()+".docx"));
//			document.write(fos);
//			fos.close();
			
			//upload do documento executado
			String fileId = null;
			
			TipoDocumento tipoDocumento = TipoDocumentoDAO.get(modelo.getCdTipoDocumento(), connect);
			String nmTipoDocumento = tipoDocumento!=null ? tipoDocumento.getNmTipoDocumento() : modelo.getNmModelo();
			Processo processo = ProcessoDAO.get(cdProcesso, connect);
			String nmCliente = ProcessoServices.getClientes(processo.getCdProcesso(), connect);
			String nmAdverso = ProcessoServices.getAdversos(processo.getCdProcesso(), connect);
			String nrProcesso = processo.getNrProcesso();
			String nmDocumento = (nmTipoDocumento!=null && !nmTipoDocumento.equals("") ? nmTipoDocumento : "")
					+(nmCliente!=null && !nmCliente.equals("") ? " - "+nmCliente : "")
					+(nmAdverso!=null && !nmAdverso.equals("") ? " X " + nmAdverso : "")
					+(nrProcesso!=null && !nrProcesso.equals("") ? " - "+nrProcesso : "");
			String nmArquivo = ""+new GregorianCalendar().getTimeInMillis();
			if(odc!=null) {
				fileId = odc.uploadFile(nmArquivo+".docx", null, file);
			}
			
			//Anexar arquivo ao processo/documento
			if(lgOneDrive && (fileId==null || fileId.equals(""))) {
				return new Result(-6, "Erro ao enviar arquivo ao repositório OneDrive");
			}
			
			/** o documento executado é devolvido à tela */
//			ProcessoArquivo arquivo = new ProcessoArquivo();
//			arquivo.setCdProcesso(cdProcesso);
//			//arquivo.setBlbArquivo(baos.toByteArray());
//			arquivo.setIdRepositorio(fileId);
//			arquivo.setNmArquivo(nmArquivo+".docx");
//			arquivo.setNmDocumento(nmDocumento);
//			arquivo.setCdTipoDocumento(tipoDocumento.getCdTipoDocumento());
//			
//			Result rArquivo = ProcessoArquivoServices.save(arquivo, false, connect);
//			if(rArquivo.getCode()<=0) {
//				System.out.println(rArquivo.getMessage());
//				return new Result(-7, "Erro ao gravar arquivo.");
//			}
			
			Result r = new Result (1, "Modelo executado com sucesso.", "FILE", file);
			
			if(lgOneDrive) {
				r.addObject("ID_FILE", fileId);
				
				String[] parts = fileId.split("\\.");
				String cid = parts[1];
				String resid = parts[2];
				
				r.addObject("URL_EDIT", "https://onedrive.live.com/view.aspx?cid="+cid+"&resid="+resid+"&app=Word");
				r.addObject("CID", cid);
				r.addObject("RESID", resid);
			}
			
			//XXX:
			System.out.println("\t\t\t> before return");
			
			return r;
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao executar modelo.\n"+e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	private static ByteArrayOutputStream xwpfConverter(XWPFDocument document, HashMap<String, String> fieldMap) throws IOException {
		
		
		List<XWPFParagraph> paragraphs = document.getParagraphs();
		for (XWPFParagraph xwpfParagraph : paragraphs) {
			List<XWPFRun> runs = xwpfParagraph.getRuns();
			
			for (XWPFRun run : runs) { 
				String text = run.getText(0);
				if(text != null) {
					for (Object key : fieldMap.keySet().toArray()) {
						
						text = text.replaceAll((String)key, (fieldMap.get(key)!=null ? fieldMap.get(key) : (String)key));
						//text = text.replaceAll(key.toString().replaceAll("<#", "").replaceAll(">", ""), (fieldMap.get(key)!=null ? fieldMap.get(key) : (String)key));
						//text = text.replaceAll("<#", "").replaceAll(">", "");
						
					}
					run.setText(text, 0);
				}
            }
			
		}

//		OneDriveUtils.replace(document, fieldMap);
		
		//cria .pdf ou .docx
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		document.write(baos);
		
		return baos;
		
	}
	
	private static ByteArrayOutputStream hwpfConverter(HWPFDocument document, HashMap<String, String> fieldMap) throws IOException {
		
		Range range = document.getRange();
		for(int i = 0; i<range.numParagraphs(); i++) {
			Paragraph paragraph = range.getParagraph(i);
			
			for (Object key : fieldMap.keySet().toArray()) {
				System.out.println("key: "+ key);
				System.out.println("value: " + fieldMap.get(key));
				if(key!=null && fieldMap!=null && fieldMap.get(key)!=null) {
					paragraph.replaceText(key.toString(), fieldMap.get(key));
					paragraph.replaceText(key.toString().replaceAll("<#", "").replaceAll(">", ""), fieldMap.get(key));
				}
			}
		}

		//cria .pdf ou .docx
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		document.write(baos);

		return baos;

	}
	
}
