package com.tivic.manager.seg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;
import sol.security.FormServices;
import sol.util.Result;

public class FormularioServices implements FormServices {

	public static final int GENERAL_ERROR = -1;
	public static final int ID_FORM_USED_ERROR = -2;


	public static int save(Formulario formulario){
		return save(formulario, null);
	}
	public static int save(Formulario formulario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if(formulario==null){
				return -1;
			}

			int retorno;
			if(formulario.getCdFormulario()==0){
				formulario.setCdSistema((formulario.getCdSistema()!=0)?formulario.getCdSistema():SistemaServices.getById("dotMng").getCdSistema());
				retorno = FormularioDAO.insert(formulario, connect);
			}
			else{
				retorno = FormularioDAO.update(formulario, connect);
				retorno = retorno>0?formulario.getCdFormulario():retorno;
			}

			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioServices.save: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static int save(ArrayList<Formulario> formularios){
		return save(formularios, null);
	}
	public static int save(ArrayList<Formulario> formularios, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if(formularios==null){
				return -1;
			}
			int retorno=0;
			for(int i=0; i<formularios.size(); i++){
				if((retorno=save(formularios.get(i), connect))<0)
					break;
			}
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioServices.save: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int importFormularioAndObjeto(int cdSistema, String xml) {
		return importFormularioAndObjeto(cdSistema, xml, null);
	}

	@SuppressWarnings("unchecked")
	public static int importFormularioAndObjeto(int cdSistema, String xml, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			SAXBuilder sb = new SAXBuilder();
			Document document = sb.build(new ByteArrayInputStream(xml.getBytes("ISO-8859-1")));
			Element rootElement = document.getRootElement();
			if (rootElement.getName().equals("formularios")) {
				List<Element> formularios = rootElement.getChildren("formulario");
				Iterator<Element> itFormularios = formularios.iterator();
				while (itFormularios.hasNext()) {
					Element formularioElement = itFormularios.next();

					String nmFormulario = formularioElement.getAttributeValue("nome");
					String nmTitulo = formularioElement.getAttributeValue("titulo");
					PreparedStatement pstmt = connection.prepareStatement("SELECT cd_formulario " +
							"FROM seg_formulario " +
							"WHERE nm_formulario = ? " +
							"  AND cd_sistema = ?");
					pstmt.setString(1, nmFormulario);
					pstmt.setInt(2, cdSistema);
					ResultSet rs = pstmt.executeQuery();
					int cdFormulario = rs.next() ? rs.getInt("cd_formulario") : 0;
					if (cdFormulario<=0 && (cdFormulario = FormularioDAO.insert(new Formulario(0 /*cdFormulario*/,
							cdSistema,
							nmFormulario,
							nmTitulo), connection))<=0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
					else {
						Formulario formulario = FormularioDAO.get(cdFormulario, cdSistema, connection);
						formulario.setNmTitulo(nmTitulo);
						if (FormularioDAO.update(formulario, connection) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return -1;
						}
					}

					List<Element> objetosFormulario = formularioElement.getChildren("objeto");
					Iterator<Element> itObjetos = objetosFormulario.iterator();
					while (itObjetos.hasNext()) {
						Element objetoElement = itObjetos.next();
						String nmObjeto = objetoElement.getAttributeValue("nome");
						String nmHint = objetoElement.getAttributeValue("hint");
						int tpObjeto = Integer.parseInt(objetoElement.getAttributeValue("tipo"));
						String idAgrupamento = objetoElement.getAttributeValue("idAgrupamento");
						String nmAgrupamento = objetoElement.getAttributeValue("nomeAgrupamento");
						String dsAgrupamento = objetoElement.getAttributeValue("descricaoAgrupamento");
						String nmAcao = objetoElement.getAttributeValue("nomeAcao");
						String dsAcao = objetoElement.getAttributeValue("descricaoAcao");
						String idModulo = objetoElement.getAttributeValue("idModulo");
						String nmModulo = objetoElement.getAttributeValue("nomeModulo");

						int cdModulo = 0;
						if (nmModulo != null && !nmModulo.equals("")) {
							pstmt = connection.prepareStatement("SELECT cd_modulo " +
									"FROM seg_modulo " +
									"WHERE id_modulo = ? " +
									"  AND cd_sistema = ?");
							pstmt.setString(1, idModulo);
							pstmt.setInt(2, cdSistema);
							rs = pstmt.executeQuery();
							cdModulo = rs.next() ? rs.getInt("cd_modulo") : 0;
							if (cdModulo<=0 && (cdModulo = ModuloDAO.insert(new Modulo(0 /*cdModulo*/,
									cdSistema,
									nmModulo,
									idModulo,
									"" /*nrVersao*/,
									"" /*nrRelease*/,
									0 /*lgAtivo*/), connection)) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return -1;
							}
							else {
								Modulo modulo = ModuloDAO.get(cdModulo, cdSistema, connection);
								modulo.setNmModulo(nmModulo);
								if (ModuloDAO.update(modulo, connection) <= 0) {
									if (isConnectionNull)
										Conexao.rollback(connection);
									return -1;
								}
							}
						}

						int cdAgrupamento = 0;
						if (cdModulo!=0 && idAgrupamento!=null && !idAgrupamento.equals("")) {
							pstmt = connection.prepareStatement("SELECT cd_agrupamento " +
									"FROM seg_agrupamento_acao " +
									"WHERE id_agrupamento = ? " +
									"  AND cd_modulo = ? " +
									"  AND cd_sistema = ?");
							pstmt.setString(1, idAgrupamento);
							pstmt.setInt(2, cdModulo);
							pstmt.setInt(3, cdSistema);
							rs = pstmt.executeQuery();
							cdAgrupamento = rs.next() ? rs.getInt("cd_agrupamento") : 0;
							if (cdAgrupamento<=0 && (cdAgrupamento = AgrupamentoAcaoDAO.insert(new AgrupamentoAcao(0 /*cdAgrupamento*/,
									1 /*cdModulo*/, 
									cdSistema,
									nmAgrupamento,
									idAgrupamento /*idAgrupamento*/,
									dsAgrupamento, 0, 1, 0/*nrOrdem*/), connection)) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return -1;
							}
							else {
								AgrupamentoAcao agrupamento = AgrupamentoAcaoDAO.get(cdAgrupamento, 1 /*cdModulo*/, cdSistema, connection);
								agrupamento.setDsAgrupamento(dsAgrupamento);
								agrupamento.setNmAgrupamento(nmAgrupamento);
								if (AgrupamentoAcaoDAO.update(agrupamento, connection) <= 0) {
									if (isConnectionNull)
										Conexao.rollback(connection);
									return -1;
								}
							}
						}

						int cdAcao = 0;
						if (cdModulo!=0 && nmModulo!=null && !nmModulo.equals("")) {
							pstmt = connection.prepareStatement("SELECT cd_acao " +
									"FROM seg_acao " +
									"WHERE nm_acao = ? " +
									"  AND cd_modulo = ? " +
									"  AND cd_sistema = ? " +
									(cdAgrupamento!=0 ? "  AND cd_agrupamento = ? " : " AND cd_agrupamento IS NULL"));
							pstmt.setString(1, nmAcao);
							pstmt.setInt(2, cdModulo);
							pstmt.setInt(3, cdSistema);
							if (cdAgrupamento!=0)
								pstmt.setInt(4, cdAgrupamento);
							rs = pstmt.executeQuery();
							cdAcao = rs.next() ? rs.getInt("cd_acao") : 0;
							if (cdAcao<=0 && (cdAcao = AcaoDAO.insert(new Acao(0 /*cdAcao*/, 
									1,
									cdSistema,
									nmAcao,
									dsAcao,
									cdAgrupamento, null, 0, 0, 0), connection)) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return -1;
							}
							else {
								Acao acao = AcaoDAO.get(cdAcao, 1, cdSistema, connection);
								acao.setDsAcao(dsAcao);
								if (AcaoDAO.update(acao, connection) <= 0) {
									if (isConnectionNull)
										Conexao.rollback(connection);
									return -1;
								}
							}
						}

						pstmt = connection.prepareStatement("SELECT cd_objeto " +
								"FROM seg_objeto " +
								"WHERE nm_objeto = ? " +
								"  AND cd_formulario = ? " +
								"  AND cd_sistema = ? ");
						pstmt.setString(1, nmObjeto);
						pstmt.setInt(2, cdFormulario);
						pstmt.setInt(3, cdSistema);
						rs = pstmt.executeQuery();
						int cdObjeto = rs.next() ? rs.getInt("cd_objeto") : 0;
						if (cdObjeto<=0 && (cdObjeto = ObjetoDAO.insert(new Objeto(cdFormulario,
								cdObjeto,
								cdSistema,
								cdAcao,
								cdModulo,
								tpObjeto,
								nmHint,
								nmObjeto), connection)) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return -1;
						}
						else {
							Objeto objeto = ObjetoDAO.get(cdFormulario, cdObjeto, cdSistema, connection);
							objeto.setNmHint(nmHint);
							objeto.setTpObjeto(tpObjeto);
							if (ObjetoDAO.update(objeto, connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return -1;
							}
						}
					}
				}
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result insertFromAgrupamentoAcao(int cdSistema, int cdModulo, int cdAgrupamento, String nmFormulario) {
		return insertFromAgrupamentoAcao(cdSistema, cdModulo, cdAgrupamento, nmFormulario, null);
	}

	public static Result insertFromAgrupamentoAcao(int cdSistema, int cdModulo, int cdAgrupamento, String nmFormulario,
			Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			AgrupamentoAcao agrupamento = AgrupamentoAcaoDAO.get(cdAgrupamento, 1 /*cdModulo*/, cdSistema, connection);
			nmFormulario = nmFormulario==null || nmFormulario.trim().equals("") ? "form" + agrupamento.getIdAgrupamento() : nmFormulario.trim();

			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM seg_formulario " +
					"WHERE nm_formulario = ?");
			pstmt.setString(1, nmFormulario);
			if (pstmt.executeQuery().next()) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Não é possível prosseguir com a inclusão de formulário e objetos referente ao agrupamento " +
						"selecionado. Certifique-se de que não haja nenhum formulário registrado com o id \"" + nmFormulario + "\".");
			}

			int cdFormulario = 0;
			if ((cdFormulario = FormularioDAO.insert(new Formulario(0 /*cdFormulario*/,
					cdSistema,
					nmFormulario /*nmFormulario*/,
					agrupamento.getNmAgrupamento() /*nmTitulo*/), connection)) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Problemas reportados ao tentar incluir registro de formulário com o id \"" +
						nmFormulario + "\". Entre em contato com o suporte técnico.");
			}

			pstmt = connection.prepareStatement("SELECT * " +
					"FROM seg_acao " +
					"WHERE cd_modulo = ? " +
					"  AND cd_sistema = ? " +
					"  AND cd_agrupamento = ?");
			pstmt.setInt(1, cdModulo);
			pstmt.setInt(2, cdSistema);
			pstmt.setInt(3, cdAgrupamento);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String[] tokens = Util.getTokens(rs.getString("nm_acao"), '.', false);
				String method = tokens==null || tokens.length==0 || tokens[tokens.length-1].trim().equals("") ? null : tokens[tokens.length-1].trim();
				String nmObjeto = method==null ? null : method.equals("insert") ? "btnNew" :
					method.equals("update") ? "btnEdit" : "btn" +  Character.toUpperCase(method.charAt(0)) + method.substring(1);
				if (nmObjeto!=null && ObjetoDAO.insert(new Objeto(cdFormulario,
						0 /*cdObjeto*/,
						cdSistema,
						rs.getInt("cd_acao"),
						cdModulo,
						ObjetoServices.TOOLBAR_ITEM /*tpObjeto*/,
						rs.getString("ds_acao") /*nmHint*/,
						nmObjeto), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Problemas reportados ao tentar incluir registro de objeto com o nome " +
							method + ". Entre em contato com o suporte técnico.");
				}
			}

			if (isConnectionNull)
				connection.commit();

			return new Result(1);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erros reportados ao incluir formulário e objetos referente ao agrupamento " +
					"selecionado. Anote a mensagem de erro abaixo e entre em contato com o suporte técnico:\n" +
					e.getLocalizedMessage());
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public int insertForm(String nameForm, String titleForm, int idSystem) {
		return FormularioDAO.insert(new Formulario(0, idSystem, nameForm, titleForm));
	}

	public int updateForm(int idForm, String nameForm, String titleForm, int idSystem) {
		return FormularioDAO.update(new Formulario(idForm, idSystem, nameForm, titleForm));
	}

	public int deleteForm(int idForm, int idSystem) {
		return FormularioDAO.delete(idForm, idSystem);
	}

	public ResultSetMap getFormsOfSystem(int idSystem) {
		return getFormsOfSystem(idSystem, null);
	}

	public static ResultSetMap getFormsOfSystem(int idSystem, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, CD_FORMULARIO AS ID_FORM, NM_FORMULARIO AS NAME_FORM, " +
					"NM_TITULO AS TITLE_FORM " +
                    "FROM seg_formulario A " +
                    "WHERE cd_sistema = ?");
			pstmt.setInt(1, idSystem);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static String getFormularioAndObjetoAsXml(int cdSistema) {
		return getFormularioAndObjetoAsXml(cdSistema, null);
	}

	public static String getFormularioAndObjetoAsXml(int cdSistema, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			ResultSetMap rsm = getFormularioAndObjeto(cdSistema, connection);

			Document doc = new Document();
			Element root = new Element("formularios");

			while (rsm.next()) {
				Element formulario = new Element("formulario");
				formulario.setAttribute("nome", rsm.getString("nm_formulario", "").trim());
				formulario.setAttribute("titulo", rsm.getString("nm_titulo", "").trim());

				ResultSetMap rsmObjetos = (ResultSetMap)rsm.getRegister().get("subResultSetMap");
				while (rsmObjetos.next()) {
					if (!rsmObjetos.getString("nm_objeto", "").trim().equals("")) {
						Element objeto = new Element("objeto");
						objeto.setAttribute("nome", rsmObjetos.getString("nm_objeto", "").trim());
						objeto.setAttribute("hint", rsmObjetos.getString("nm_hint", "").trim());
						objeto.setAttribute("tipo", Integer.toString(rsmObjetos.getInt("tp_objeto")));
						if (rsmObjetos.getInt("cd_acao") != 0) {
							objeto.setAttribute("nomeAcao", rsmObjetos.getString("nm_acao", "").trim());
							objeto.setAttribute("descricaoAcao", rsmObjetos.getString("ds_acao", "").trim());
							objeto.setAttribute("idModulo", rsmObjetos.getString("id_modulo", "").trim());
							objeto.setAttribute("nomeModulo", rsmObjetos.getString("nm_modulo", "").trim());
							if (rsmObjetos.getInt("cd_agrupamento")!=0 && !rsmObjetos.getString("id_agrupamento", "").trim().equals("")) {
								objeto.setAttribute("idAgrupamento", rsmObjetos.getString("id_agrupamento", "").trim());
								objeto.setAttribute("nomeAgrupamento", rsmObjetos.getString("nm_agrupamento", "").trim());
								objeto.setAttribute("descricaoAgrupamento", rsmObjetos.getString("ds_agrupamento", "").trim());
							}
						}
						formulario.addContent(objeto);
					}
				}

				root.addContent(formulario);
			}

			doc.addContent(root);

			XMLOutputter outp = new XMLOutputter();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			outp.setFormat(Format.getPrettyFormat().setEncoding("ISO-8859-1"));
			outp.output(doc, stream);
			return stream.toString();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getFormularioAndObjeto(int cdSistema) {
		return getFormularioAndObjeto(cdSistema, null);
	}

	public static ResultSetMap getFormularioAndObjeto(int cdSistema, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_acao, B.ds_acao, C.id_modulo, C.nm_modulo, " +
					"D.nm_agrupamento, D.ds_agrupamento, D.cd_agrupamento, D.id_agrupamento " +
					"FROM seg_objeto A " +
                    "LEFT OUTER JOIN seg_acao B ON (A.cd_sistema = B.cd_sistema " +
                    "                           AND A.cd_modulo  = B.cd_modulo " +
                    "                           AND A.cd_acao    = B.cd_acao) " +
                    "LEFT OUTER JOIN seg_modulo C ON (B.cd_modulo = C.cd_modulo AND " +
                    "								 B.cd_sistema = C.cd_sistema) " +
                    "LEFT OUTER JOIN seg_agrupamento_acao D ON (B.cd_agrupamento = D.cd_agrupamento AND " +
                    "											B.cd_modulo = D.cd_modulo AND " +
                    "											B.cd_sistema = D.cd_sistema) " +
                    "WHERE A.cd_sistema     = ? "+
                    "  AND A.cd_formulario  = ? " +
                    "ORDER BY nm_objeto");
			ResultSetMap rsmFormulario = new ResultSetMap(connection.prepareStatement("SELECT * FROM seg_formulario " +
					                                                               "WHERE cd_sistema = "+cdSistema+
					                                                               " ORDER BY nm_formulario").executeQuery());
			while(rsmFormulario.next())	{
				pstmt.setInt(1, rsmFormulario.getInt("cd_sistema"));
				pstmt.setInt(2, rsmFormulario.getInt("cd_formulario"));
				rsmFormulario.getRegister().put("subResultSetMap", new ResultSetMap(pstmt.executeQuery()));
			}
			rsmFormulario.beforeFirst();
			return rsmFormulario;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

//	public static ArrayList<HashMap<String,Object>> getObjects(byte[] content){
//		try {
//			Parser parser = ParserUtils.createParserParsingAnInputString(new String(content));
//			/* objetos buttons */
//			NodeFilter filter = new TagNameFilter("button");
//			NodeList list = parser.parse(filter);
//			ArrayList<HashMap<String,Object>> objects = new ArrayList<HashMap<String,Object>>();
//			for (int i=0; i<list.size(); i++){
//				if (list.elementAt(i) instanceof TagNode && ((TagNode)list.elementAt(i)).getAttribute("id") != null) {
//					HashMap<String,Object> object = new HashMap<String,Object>();
//					object.put("nmObjeto", ((TagNode)list.elementAt(i)).getAttribute("id"));
//					object.put("nmHint", ((TagNode)list.elementAt(i)).getAttribute("title"));
//					objects.add(object);
//				}
//			}
//
//			return objects;
//		}
//		catch(Exception e) {
//			e.printStackTrace(System.out);
//			return null;
//		}
//	}

//	public static ResultSetMap getObjectsAsResultSet(byte[] content){
//		ArrayList<HashMap<String,Object>> listObjects = getObjects(content);
//		ResultSetMap rsm = new ResultSetMap();
//		for (int i=0; listObjects!=null && i<listObjects.size(); i++) {
//			HashMap<String,Object> hash = listObjects.get(i);
//			hash.put("NM_OBJETO", hash.get("nmObjeto"));
//			hash.put("NM_HINT", hash.get("nmHint"));
//			hash.remove("nmObjeto");
//			hash.remove("nmHint");
//			rsm.addRegister(hash);
//		}
//		rsm.beforeFirst();
//		return rsm;
//	}

	public static int insert(Formulario objeto) {
		return insert(objeto, null);
	}

	public static int insert(Formulario objeto, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			objeto.setNmFormulario(objeto.getNmFormulario().trim());
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM seg_formulario A " +
					"WHERE A.cd_sistema = ? " +
					"  AND A.nm_formulario = ?");
			pstmt.setInt(1, objeto.getCdSistema());
			pstmt.setString(2, objeto.getNmFormulario());
			ResultSet rs = pstmt.executeQuery();
			int cdFormulario = 0;
			if (rs.next())
				return ID_FORM_USED_ERROR;
			else if ((cdFormulario = FormularioDAO.insert(objeto, connection)) <= 0) {
				return GENERAL_ERROR;
			}
			else {
				return cdFormulario;
			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioServices.insert: " +  e);
			return GENERAL_ERROR;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int delete(int cdFormulario, int cdSistema) {
		return delete(cdFormulario, cdSistema, null);
	}

	public static int delete(int cdFormulario, int cdSistema, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			/* exclusao de objetos */
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM seg_objeto " +
					"WHERE cd_sistema = ? " +
					"  AND cd_formulario = ?");
			pstmt.setInt(1, cdSistema);
			pstmt.setInt(2, cdFormulario);
			ResultSet rs = pstmt.executeQuery();
			while (rs != null && rs.next()) {
				if (ObjetoDAO.delete(cdFormulario, rs.getInt("CD_OBJETO"), cdSistema, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			if (FormularioDAO.delete(cdFormulario, cdSistema, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static HashMap<String, Object> getPermissoesObjetos(int cdUsuario, String idForm, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			HashMap<String, Object> hash = new HashMap<String, Object>();
			HashMap<String, Object> form = new HashMap<String, Object>();

			boolean putNotAccess = true;
			boolean putAccess = false;

			HashMap<String, Object> notAccess = !putNotAccess ? null : new HashMap<String, Object>();
			HashMap<String, Object> access = !putAccess ? null : new HashMap<String, Object>();

			ArrayList<HashMap<String, Object>> buttons1 = !putNotAccess ? null : new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> menus1 = !putNotAccess ? null : new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> toolbars1 = !putNotAccess ? null : new ArrayList<HashMap<String, Object>>();

			ArrayList<HashMap<String, Object>> buttons2 = !putAccess ? null : new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> menus2 = !putAccess ? null : new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> toolbars2 = !putAccess ? null : new ArrayList<HashMap<String, Object>>();

			if (notAccess!=null) {
				notAccess.put("buttons", buttons1);
				notAccess.put("menus", menus1);
				notAccess.put("toolbars", toolbars1);
			}

			if (access != null) {
				access.put("buttons", buttons2);
				access.put("menus", menus2);
				access.put("toolbars", toolbars2);
			}

			if (putAccess)
				form.put("access", access);
			if (putNotAccess)
				form.put("notAccess", notAccess);

			hash.put("form", form);

			Usuario usuario = UsuarioDAO.get(cdUsuario, connection);
			if(usuario.getTpUsuario()==UsuarioServices.ADMINISTRADOR && putNotAccess)
				return hash;

			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, E.lg_natureza, D.cd_acao, E.cd_acao AS cd_acao_usuario, " +
					"E.cd_modulo, " +
					"(SELECT COUNT(*) " +
					" FROM seg_grupo_permissao_acao F, seg_usuario_grupo G " +
					" WHERE F.cd_acao = D.cd_acao " +
					"	AND F.cd_modulo = D.cd_modulo " +
					"   AND F.cd_sistema = D.cd_sistema " +
					"   AND F.cd_grupo = G.cd_grupo " +
					"   AND G.cd_usuario = ?) AS qt_permissoes_grupo " +
					"FROM seg_objeto A " +
					"JOIN seg_formulario B ON (A.cd_formulario = B.cd_formulario AND " +
					"						   A.cd_sistema = B.cd_sistema) " +
					"JOIN seg_sistema C ON (B.cd_sistema = C.cd_sistema) " +
					"LEFT OUTER JOIN seg_acao D ON (A.cd_acao = D.cd_acao AND A.cd_modulo = D.cd_modulo AND A.cd_sistema = D.cd_sistema) " +
					"LEFT OUTER JOIN seg_usuario_permissao_acao E ON (D.cd_acao = E.cd_acao AND " +
					"												  D.cd_modulo = E.cd_modulo AND " +
					"												  D.cd_sistema = E.cd_sistema AND " +
					"												  E.cd_usuario = ?) " +
					"WHERE C.id_sistema = ? " +
					"  AND B.nm_formulario = ?");
			pstmt.setInt(1, cdUsuario);
			pstmt.setInt(2, cdUsuario);
			pstmt.setString(3, "dotMng");
			pstmt.setString(4, idForm);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while (rsm!=null && rsm.next()) {
				String name = rsm.getString("nm_objeto");
				int type = rsm.getInt("tp_objeto");
				boolean hashAction = rsm.getInt("cd_acao")>0;
				boolean hasGroupPermission = rsm.getInt("qt_permissoes_grupo")>0;
				boolean hashUserPermission = rsm.getInt("cd_acao_usuario")>0;
				int userPermission = rsm.getInt("lg_natureza");
				boolean isAccessible = !hashAction || ((hasGroupPermission && (!hashUserPermission ||
													   (userPermission==UsuarioPermissaoAcaoServices.PERMISSAO ||
													    userPermission==UsuarioPermissaoAcaoServices.PERMISSAO_OVER_GRUPO))) ||
													   (hashUserPermission && (userPermission==UsuarioPermissaoAcaoServices.PERMISSAO ||
													    userPermission==UsuarioPermissaoAcaoServices.PERMISSAO_OVER_GRUPO)));
				HashMap<String, Object> object = new HashMap<String, Object>();
				object.put("id", name);
				if (putNotAccess && !isAccessible) {
					switch (type) {
						case ObjetoServices.MENU_ITEM: menus1.add(object); break;
						case ObjetoServices.BUTTON: buttons1.add(object); break;
						case ObjetoServices.TOOLBAR_ITEM: toolbars1.add(object); break;
					}
				}
				if (putAccess && isAccessible) {
					switch (type) {
						case ObjetoServices.MENU_ITEM: menus2.add(object); break;
						case ObjetoServices.BUTTON: buttons2.add(object); break;
						case ObjetoServices.TOOLBAR_ITEM: toolbars2.add(object); break;
					}
				}
			}
			return hash;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public ResultSetMap getFormsInfoByIds(ArrayList<String> formIds) {
		Connection connect = null;
		try {
			connect = Conexao.conectar();
			String sql = "SELECT A.*" +
						"FROM SEG_FORMULARIO A " +
						"WHERE A.NM_FORMULARIO IN (" + formIds.toString().replaceAll("[\\[\\]]", "") + ") ";
			PreparedStatement pstmt = connect.prepareStatement(sql);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (connect != null)
				Conexao.desconectar(connect);
		}
	}
}