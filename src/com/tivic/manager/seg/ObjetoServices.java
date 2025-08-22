package com.tivic.manager.seg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletRequest;
import org.rawsocket.jspupload.FileUpload;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.security.*;
import sol.security.Module;
import sol.security.System;
import sol.security.Object;
import sol.util.RequestUtilities;
import sol.util.Result;

public class ObjetoServices implements ObjectServices {

	public static final int MENU_ITEM = 0;
	public static final int MENUBAR_ITEM = MENU_ITEM;
	public static final int TOOLBAR_ITEM = 1;
	public static final int BUTTON = 2;

	public static final String[] tipos = {"Menu Item",
		"Toolbar Item",
		"Botão"};

	public static final int GENERAL_ERROR = -1;
	public static final int NOT_DEFINED_SYSTEM_ERROR = -2;
	public static final int NOT_DEFINED_FORM_ERROR = -3;
	public static final int EXISTS_ID_ERROR = -4;
	public static final int BLANK_ID_OBJECT_ERROR = -5;
	public static final int BLANK_ID_FORM_ERROR = -6;
	public static final int BLANK_ID_SYSTEM_ERROR = -7;
	public static final int BLANK_ID_GROUP_ACTIONS_ERROR = -8;
	public static final int BLANK_ID_MODULE_ERROR =  -9;

	public static Result moveObjeto(int cdSistema, int cdFormulario, int cdObjeto, int cdNewSistema, int cdNewFormulario) {
		return moveObjeto(cdSistema, cdFormulario, cdObjeto, cdNewSistema, cdNewFormulario, null);
	}

	public static Result moveObjeto(int cdSistema, int cdFormulario, int cdObjeto, int cdNewSistema, int cdNewFormulario,
			Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			Objeto objeto = ObjetoDAO.get(cdFormulario, cdObjeto, cdSistema, connection);

			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM seg_objeto " +
					"WHERE cd_sistema = ? " +
					"  AND cd_formulario = ? " +
					"  AND nm_objeto = ?");
			pstmt.setInt(1, cdNewSistema);
			pstmt.setInt(2, cdNewFormulario);
			pstmt.setString(3, objeto.getNmObjeto());
			if (pstmt.executeQuery().next()) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Não é possível mover o objeto. O sistema acusa que o formulário selecionado para transferência já " +
						"apresenta um objeto com o mesmo identificador do objeto selecionado para transferência.");
			}

			Objeto newObjeto = (Objeto)objeto.clone();
			newObjeto.setCdObjeto(0);
			newObjeto.setCdFormulario(cdNewFormulario);
			newObjeto.setCdSistema(cdNewSistema);
			int cdNewObjeto = 0;
			if ((cdNewObjeto = ObjetoDAO.insert(newObjeto, connection)) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erros reportados ao mover objeto.");
			}

			pstmt = connection.prepareStatement("UPDATE log_acesso_objeto " +
					"SET cd_sistema = ?, " +
					"	 cd_formulario = ?, " +
					"	 cd_objeto = ? " +
					"WHERE cd_sistema = ? " +
					"  AND cd_formulario = ? " +
					"  AND cd_objeto = ?");
			pstmt.setInt(1, cdNewSistema);
			pstmt.setInt(2, cdNewFormulario);
			pstmt.setInt(3, cdNewObjeto);
			pstmt.setInt(4, cdSistema);
			pstmt.setInt(5, cdFormulario);
			pstmt.setInt(6, cdObjeto);
			pstmt.execute();

			pstmt = connection.prepareStatement("UPDATE seg_grupo_permissao_objeto " +
					"SET cd_sistema = ?, " +
					"	 cd_formulario = ?, " +
					"	 cd_objeto = ? " +
					"WHERE cd_sistema = ? " +
					"  AND cd_formulario = ? " +
					"  AND cd_objeto = ?");
			pstmt.setInt(1, cdNewSistema);
			pstmt.setInt(2, cdNewFormulario);
			pstmt.setInt(3, cdNewObjeto);
			pstmt.setInt(4, cdSistema);
			pstmt.setInt(5, cdFormulario);
			pstmt.setInt(6, cdObjeto);
			pstmt.execute();

			pstmt = connection.prepareStatement("UPDATE seg_usuario_permissao_objeto " +
					"SET cd_sistema = ?, " +
					"	 cd_formulario = ?, " +
					"	 cd_objeto = ? " +
					"WHERE cd_sistema = ? " +
					"  AND cd_formulario = ? " +
					"  AND cd_objeto = ?");
			pstmt.setInt(1, cdNewSistema);
			pstmt.setInt(2, cdNewFormulario);
			pstmt.setInt(3, cdNewObjeto);
			pstmt.setInt(4, cdSistema);
			pstmt.setInt(5, cdFormulario);
			pstmt.setInt(6, cdObjeto);
			pstmt.execute();

			if (ObjetoDAO.delete(cdFormulario, cdObjeto, cdSistema, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erros reportados ao mover objeto.");
			}

			if (isConnectionNull)
				connection.commit();

			return new Result(cdNewObjeto);
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erros reportados ao mover objeto. Anote a mensagem de erro e entre em contato com " +
					"o suporte técnico.\n" + e.getLocalizedMessage());
		}
		finally {
			if (connection != null)
				Conexao.desconectar(connection);
		}
	}

	public synchronized int insertObject(String nameObject, String hintObject, int typeObject, int idForm, int idSystem, int idAction, int idModule) {
		return ObjetoDAO.insert(new Objeto(idForm, 0, idSystem, idAction, idModule, typeObject, hintObject, nameObject));
	}

	public int updateObject(int idObject, String nameObject, String hintObject, int typeObject, int idForm, int idSystem, int idAction, int idModule) {
		return ObjetoDAO.update(new Objeto(idForm, idObject, idSystem, idAction, idModule, typeObject, hintObject, nameObject));
	}

	public int deleteObject(int idObject, int idForm, int idSystem) {
		return ObjetoDAO.delete(idForm, idObject, idSystem);
	}

	public ResultSetMap getObjectsOfForm(int idForm, int idSystem) {
		Connection connect = null;
		try {
			connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					                    "SELECT CD_OBJETO AS ID_OBJECT, NM_OBJETO AS NAME_OBJECT, NM_HINT AS HINT_OBJECT, " +
										"TP_OBJETO AS TYPE_OBJECT, CD_MODULO AS ID_MODULE, CD_ACAO AS ID_ACTION " +
										"FROM SEG_OBJETO " +
										"WHERE CD_FORMULARIO  = " + idForm +
										"  AND CD_SISTEMA = " + idSystem);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			return null;
		}
		finally {
			if (connect != null)
				Conexao.desconectar(connect);
		}
	}

	public StatusPermissionObjectGroup[] getStatusPermissionObjectOfGroup(int idGroup, int idForm, int idSystem) {
		Connection connect = null;
		try {
			connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.NM_FORMULARIO, B.NM_TITULO, C.NM_SISTEMA, C.ID_SISTEMA, " +
															   "       D.CD_MODULO, D.NM_ACAO, E.NM_MODULO, G.NM_AGRUPAMENTO, G.DS_AGRUPAMENTO,G.ID_AGRUPAMENTO, " +
															   "	   (SELECT COUNT(*) FROM SEG_GRUPO_PERMISSAO_OBJETO " +
															   "			 WHERE CD_GRUPO = ? " +
															   "			   AND CD_SISTEMA = A.CD_SISTEMA " +
															   "			   AND CD_FORMULARIO = A.CD_FORMULARIO " +
															   "			   AND CD_OBJETO = A.CD_OBJETO) AS LG_PERMISSAO " +
															   "FROM SEG_OBJETO A " +
															   "JOIN SEG_FORMULARIO B ON (A.CD_FORMULARIO = B.CD_FORMULARIO AND A.CD_SISTEMA = B.CD_SISTEMA) " +
															   "JOIN SEG_SISTEMA C ON (B.CD_SISTEMA = C.CD_SISTEMA) " +
															   "LEFT OUTER JOIN SEG_ACAO D ON (A.CD_MODULO = D.CD_MODULO AND A.CD_SISTEMA = D.CD_SISTEMA AND A.cd_acao = D.cd_acao) " +
															   "LEFT OUTER JOIN SEG_MODULO E ON (D.CD_MODULO = E.CD_MODULO AND D.CD_SISTEMA = E.CD_SISTEMA) " +
															   "LEFT OUTER JOIN SEG_AGRUPAMENTO_ACAO G ON (D.CD_AGRUPAMENTO = G.CD_AGRUPAMENTO AND D.CD_MODULO = G.CD_MODULO AND D.CD_SISTEMA = G.CD_SISTEMA) " +
															   "WHERE A.CD_FORMULARIO = ? " +
															   "  AND A.CD_SISTEMA = ?");
			pstmt.setInt(1, idGroup);
			pstmt.setInt(2, idForm);
			pstmt.setInt(3, idSystem);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			StatusPermissionObjectGroup[] statusList = new StatusPermissionObjectGroup[rsm==null ? 0 : rsm.size()];
			System system = null;
			Form form = null;
			Grupo grupo = GrupoDAO.get(idGroup);
			Group group = new Group(idGroup, grupo==null ? null : grupo.getNmGrupo());
			for (int i=0; rsm!=null && rsm.next(); i++) {
				system = new System(rsm.getInt("CD_SISTEMA"), rsm.getString("NM_SISTEMA"), rsm.getString("ID_SISTEMA"));
				form = new Form(rsm.getInt("CD_FORMULARIO"), rsm.getString("NM_FORMULARIO"), rsm.getString("NM_TITULO"), system);
				Module module = rsm.getInt("CD_MODULO")==0 ? null : new Module(rsm.getInt("CD_MODULO"), rsm.getString("NM_MODULO"), system);
				GroupActions groupActions = rsm.getInt("CD_AGRUPAMENTO")==0 ? null : new GroupActions(rsm.getInt("CD_AGRUPAMENTO"), rsm.getString("NM_AGRUPAMENTO"), rsm.getString("DS_AGRUPAMENTO"), module);
				Action action = module==null ? null : new Action(rsm.getInt("CD_ACAO"), rsm.getString("NM_ACAO"), rsm.getString("DS_ACAO"), module, groupActions);
				statusList[i] = new StatusPermissionObjectGroup(new Object(rsm.getInt("CD_OBJETO"), rsm.getString("NM_OBJETO"), rsm.getString("NM_HINT"), rsm.getInt("TP_OBJETO"), form, action), group, rsm.getInt("LG_PERMISSAO")>0);
			}
			return statusList;
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			return null;
		}
		finally {
			if (connect != null)
				Conexao.desconectar(connect);
		}
	}

	public int saveStatusPermissionObjectGroup(StatusPermissionObjectGroup status) {
		return saveStatusPermissionObjectGroup(status, null);
	}

	public int saveStatusPermissionObjectGroup(StatusPermissionObjectGroup status, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			if (!status.isAccessible()) {
				PreparedStatement pstmt = connect.prepareStatement("DELETE FROM SEG_GRUPO_PERMISSAO_OBJETO " +
										 "WHERE CD_GRUPO = ? " +
										 "  AND CD_SISTEMA = ? " +
										 "  AND CD_FORMULARIO = ? " +
										 "  AND CD_OBJETO = ?");
				pstmt.setInt(1, status.getGroup().getId());
				pstmt.setInt(2, status.getObject().getForm().getSystem().getId());
				pstmt.setInt(3, status.getObject().getForm().getId());
				pstmt.setInt(4, status.getObject().getId());
				pstmt.execute();
			}
			else {
				PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM SEG_GRUPO_PERMISSAO_OBJETO " +
						 "WHERE CD_GRUPO = ? " +
						 "  AND CD_SISTEMA = ? " +
						 "  AND CD_FORMULARIO = ? " +
						 "  AND CD_OBJETO = ?");
				pstmt.setInt(1, status.getGroup().getId());
				pstmt.setInt(2, status.getObject().getForm().getSystem().getId());
				pstmt.setInt(3, status.getObject().getForm().getId());
				pstmt.setInt(4, status.getObject().getId());
				if (!pstmt.executeQuery().next()) {
					pstmt = connect.prepareStatement("INSERT INTO SEG_GRUPO_PERMISSAO_OBJETO (CD_GRUPO, CD_SISTEMA, CD_FORMULARIO, CD_OBJETO) VALUES(?, ?, ?, ?)");
					pstmt.setInt(1, status.getGroup().getId());
					pstmt.setInt(2, status.getObject().getForm().getSystem().getId());
					pstmt.setInt(3, status.getObject().getForm().getId());
					pstmt.setInt(4, status.getObject().getId());
					pstmt.execute();
				}
			}
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static StatusPermissionObjectUser[] getStatusPermissionObjectsOfUser(int idUser) {
		return new ObjetoServices().getStatusPermissionObjectsOfUser(idUser, 0, 0);
	}

	public StatusPermissionObjectUser[] getStatusPermissionObjectsOfUser(int idUser, int idForm, int idSystem) {
		return new ObjetoServices().getStatusPermissionObjectsOfUser(idUser, idForm, idSystem, null);
	}

	public StatusPermissionObjectUser[] getStatusPermissionObjectsOfUser(int idUser, int idForm, int idSystem, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.NM_FORMULARIO, B.NM_TITULO, C.NM_SISTEMA, C.ID_SISTEMA, " +
					"D.CD_MODULO, D.NM_ACAO, E.NM_MODULO, G.NM_AGRUPAMENTO, G.DS_AGRUPAMENTO, G.ID_AGRUPAMENTO, " +
				    "			(SELECT COUNT(*) FROM SEG_USUARIO_PERMISSAO_OBJETO " +
				    "			 WHERE CD_USUARIO = ? " +
				    "			   AND CD_SISTEMA = A.CD_SISTEMA " +
				    "			   AND CD_FORMULARIO = A.CD_FORMULARIO " +
				    "			   AND CD_OBJETO = A.CD_OBJETO) AS LG_PERMISSAO, " +
				    "	   		(SELECT MAX(LG_NATUREZA) FROM SEG_USUARIO_PERMISSAO_OBJETO " +
				    "			 WHERE CD_USUARIO = ? " +
				    "			   AND CD_SISTEMA = A.CD_SISTEMA " +
				    "			   AND CD_FORMULARIO = A.CD_FORMULARIO " +
				    "			   AND CD_OBJETO = A.CD_OBJETO) AS LG_NATUREZA, " +
				    " 			(SELECT COUNT(*) FROM SEG_GRUPO_PERMISSAO_OBJETO D, SEG_USUARIO_GRUPO E " +
				    "			 WHERE E.CD_USUARIO = ? " +
				    "			   AND D.CD_GRUPO = E.CD_GRUPO " +
				    "			   AND D.CD_SISTEMA = A.CD_SISTEMA " +
				    "			   AND D.CD_FORMULARIO = A.CD_FORMULARIO " +
				    "			   AND D.CD_OBJETO = A.CD_OBJETO) AS LG_PERMISSAO_GRUPO " +
				    "FROM SEG_OBJETO A " +
				    "JOIN SEG_FORMULARIO B ON (A.CD_FORMULARIO = B.CD_FORMULARIO AND A.CD_SISTEMA = B.CD_SISTEMA) " +
				    "JOIN SEG_SISTEMA C ON (B.CD_SISTEMA = C.CD_SISTEMA) " +
				    "LEFT OUTER JOIN SEG_ACAO D ON (A.CD_MODULO = D.CD_MODULO AND A.CD_SISTEMA = D.CD_SISTEMA AND A.cd_acao = D.cd_acao) " +
				    "LEFT OUTER JOIN SEG_MODULO E ON (D.CD_MODULO = E.CD_MODULO AND D.CD_SISTEMA = E.CD_SISTEMA) " +
				    "LEFT OUTER JOIN SEG_AGRUPAMENTO_ACAO G ON (D.CD_AGRUPAMENTO = G.CD_AGRUPAMENTO AND D.CD_MODULO = G.CD_MODULO AND D.CD_SISTEMA = G.CD_SISTEMA) " +
				    "WHERE (1=1)" + (idForm==0 ? "" : " AND A.CD_FORMULARIO = ? ") +
				    (idSystem==0 ? "" : " AND A.CD_SISTEMA = ?"));
			pstmt.setInt(1, idUser);
			pstmt.setInt(2, idUser);
			pstmt.setInt(3, idUser);
			int i = 4;
			if (idForm != 0)
				pstmt.setInt(i++, idForm);
			if (idSystem != 0)
				pstmt.setInt(i, idSystem);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			StatusPermissionObjectUser[] statusList = new StatusPermissionObjectUser[rsm==null ? 0 : rsm.size()];
			System system = null;
			Form form = null;
			Usuario usuario = UsuarioDAO.get(idUser, connection);
			Pessoa pessoa = usuario==null || usuario.getCdPessoa()==0 ? null : PessoaDAO.get(usuario.getCdPessoa());
			User user = new User(idUser, pessoa==null ? null : pessoa.getNmPessoa(), usuario==null ? null : usuario.getNmLogin(), usuario==null ? null : usuario.getNmLogin());
			for (i=0; rsm!=null && rsm.next(); i++) {
				system = new System(rsm.getInt("CD_SISTEMA"), rsm.getString("NM_SISTEMA"), rsm.getString("ID_SISTEMA"));
				form = new Form(rsm.getInt("CD_FORMULARIO"), rsm.getString("NM_FORMULARIO"), rsm.getString("NM_TITULO"), system);
				Module module = rsm.getInt("CD_MODULO")==0 ? null : new Module(rsm.getInt("CD_MODULO"), rsm.getString("NM_MODULO"), system);
				GroupActions groupActions = rsm.getInt("CD_AGRUPAMENTO")==0 ? null : new GroupActions(rsm.getInt("CD_AGRUPAMENTO"), rsm.getString("NM_AGRUPAMENTO"), rsm.getString("DS_AGRUPAMENTO"), module);
				Action action = module==null ? null : new Action(rsm.getInt("CD_ACAO"), rsm.getString("NM_ACAO"), rsm.getString("DS_ACAO"), module, groupActions);
				boolean isAccessible = rsm.getInt("LG_PERMISSAO")>0 && (rsm.getInt("LG_NATUREZA")==UsuarioPermissaoAcaoServices.PERMISSAO ||
						rsm.getInt("LG_NATUREZA")==UsuarioPermissaoAcaoServices.PERMISSAO_OVER_GRUPO);
				boolean isOverGroupPermission = rsm.getInt("LG_PERMISSAO")>0 &&
												(rsm.getInt("LG_NATUREZA")==UsuarioPermissaoAcaoServices.NOT_PERMISSAO_OVER_GRUPO ||
														rsm.getInt("LG_NATUREZA")==UsuarioPermissaoAcaoServices.PERMISSAO_OVER_GRUPO);
				boolean isAccessibleByGroups = rsm.getInt("LG_PERMISSAO_GRUPO")>0;
				statusList[i] = new StatusPermissionObjectUser(new Object(rsm.getInt("CD_OBJETO"), rsm.getString("NM_OBJETO"), rsm.getString("NM_HINT"), rsm.getInt("TP_OBJETO"), form, action), user, isAccessible, isOverGroupPermission, isAccessibleByGroups);
			}
			return statusList;
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

	public int saveStatusPermissionObjectUser(StatusPermissionObjectUser status) {
		return saveStatusPermissionObjectUser(status, null);
	}

	public static int saveStatusPermissionObjectUser(StatusPermissionObjectUser status, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM SEG_USUARIO_PERMISSAO_OBJETO " +
									 "WHERE CD_USUARIO = ? " +
									 "  AND CD_SISTEMA = ? " +
									 "  AND CD_FORMULARIO = ? " +
									 "  AND CD_OBJETO = ?");
			pstmt.setInt(1, status.getUser().getId());
			pstmt.setInt(2, status.getObject().getForm().getSystem().getId());
			pstmt.setInt(3, status.getObject().getForm().getId());
			pstmt.setInt(4, status.getObject().getId());
			pstmt.execute();

			if (status.isAccessible() || status.isOverGroupPermission()) {
				int lgNatureza = status.isAccessible() && !status.isOverGroupPermission() ? UsuarioPermissaoAcaoServices.PERMISSAO :
								 status.isAccessible() && status.isOverGroupPermission() ? UsuarioPermissaoAcaoServices.PERMISSAO_OVER_GRUPO :
									 UsuarioPermissaoAcaoServices.NOT_PERMISSAO_OVER_GRUPO;
				pstmt = connect.prepareStatement("INSERT INTO SEG_USUARIO_PERMISSAO_OBJETO (CD_USUARIO, CD_SISTEMA, CD_FORMULARIO, CD_OBJETO, LG_NATUREZA) VALUES(?, ?, ?, ?, ?)");
				pstmt.setInt(1, status.getUser().getId());
				pstmt.setInt(2, status.getObject().getForm().getSystem().getId());
				pstmt.setInt(3, status.getObject().getForm().getId());
				pstmt.setInt(4, status.getObject().getId());
				pstmt.setInt(5, lgNatureza);
				pstmt.execute();
			}

			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Object[] getObjectBySigleSystem(String sigleSystem) {
		return getObjectBySigleSystem(sigleSystem, null);
	}

	public static Object[] getObjectBySigleSystem(String sigleSystem, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.NM_FORMULARIO, B.NM_TITULO, C.NM_SISTEMA, C.ID_SISTEMA, D.NM_ACAO, D.DS_ACAO, E.NM_MODULO, G.NM_AGRUPAMENTO, G.DS_AGRUPAMENTO, G.ID_AGRUPAMENTO " +
															   "FROM SEG_OBJETO A " +
															   "JOIN SEG_FORMULARIO B ON (A.CD_FORMULARIO = B.CD_FORMULARIO AND A.CD_SISTEMA = B.CD_SISTEMA) " +
															   "JOIN SEG_SISTEMA C ON (B.CD_SISTEMA = C.CD_SISTEMA) " +
															   "LEFT OUTER JOIN SEG_ACAO D ON (A.CD_MODULO = D.CD_MODULO AND A.CD_SISTEMA = D.CD_SISTEMA AND A.cd_acao = D.cd_acao) " +
															   "LEFT OUTER JOIN SEG_MODULO E ON (D.CD_MODULO = E.CD_MODULO AND D.CD_SISTEMA = E.CD_SISTEMA) " +
															   "LEFT OUTER JOIN SEG_AGRUPAMENTO_ACAO G ON (D.CD_AGRUPAMENTO = G.CD_AGRUPAMENTO AND D.CD_MODULO = G.CD_MODULO AND D.CD_SISTEMA = G.CD_SISTEMA) " +
															   "WHERE C.ID_SISTEMA = ?");
			pstmt.setString(1, sigleSystem);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			Object[] objectList = new Object[rsm==null ? 0 : rsm.size()];
			System system = null;
			Form form = null;
			for (int i=0; rsm!=null && rsm.next(); i++) {
				system = new System(rsm.getInt("CD_SISTEMA"), rsm.getString("NM_SISTEMA"), rsm.getString("ID_SISTEMA"));
				form = new Form(rsm.getInt("CD_FORMULARIO"), rsm.getString("NM_FORMULARIO"), rsm.getString("NM_TITULO"), system);
				Module module = rsm.getInt("CD_MODULO")==0 ? null : new Module(rsm.getInt("CD_MODULO"), rsm.getString("NM_MODULO"), system);
				GroupActions groupActions = rsm.getInt("CD_AGRUPAMENTO")==0 ? null : new GroupActions(rsm.getInt("CD_AGRUPAMENTO"), rsm.getString("NM_AGRUPAMENTO"), rsm.getString("DS_AGRUPAMENTO"), module);
				Action action = module==null ? null : new Action(rsm.getInt("CD_ACAO"), rsm.getString("NM_ACAO"), rsm.getString("DS_ACAO"), module, groupActions);
				objectList[i] = new Object(rsm.getInt("CD_OBJETO"), rsm.getString("NM_OBJETO"), rsm.getString("NM_HINT"), rsm.getInt("TP_OBJETO"), form, action);
			}
			return objectList;
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public int isAccessibleActionByObject(String nameObject, String nameForm, String sgSystem, int idUser) {
		return isAccessibleActionByObject(nameObject, nameForm, sgSystem, idUser, null);
	}

	public static int isAccessibleActionByObject(String nameObject, String nameForm, String sgSystem, int idUser, Conexao connect) {
		return -1;
	}

	public static HashMap<String, java.lang.Object> saveObjetos(ServletRequest request) {
		return saveObjetos(request, null);
	}

	public static HashMap<String, java.lang.Object> saveObjetos(ServletRequest request, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			HashMap<String, java.lang.Object> hashResults = new HashMap<String,java.lang.Object>();
			ArrayList<java.lang.Object> listObjetos = new ArrayList<java.lang.Object>();
			int countSucessImport = 0;
			int countErrorsImport = 0;
			int countErrorsIdExistImport = 0;

			int countObjetos = RequestUtilities.getAsInteger(request, "countObjetos", 0);
			for (int i=0; i<countObjetos; i++) {
				String nmObjeto  = RequestUtilities.getAsString(request, "nmObjeto_" + i, "");
				String nmHint    = RequestUtilities.getAsString(request, "nmHint_" + i, "");
				int tpObjeto     = RequestUtilities.getAsInteger(request, "tpObjeto_" + i, 0);
				int cdSistema    = RequestUtilities.getAsInteger(request, "cdSistema_" + i, 0);
				int cdFormulario = RequestUtilities.getAsInteger(request, "cdFormulario_" + i, 0);
				int cdModulo     = RequestUtilities.getAsInteger(request, "cdModulo_" + i, 0);
				int cdAcao       = RequestUtilities.getAsInteger(request, "cdAcao_" + i, 0);
				Objeto objeto    = new Objeto(cdFormulario, 0, cdSistema, cdAcao, cdModulo, tpObjeto, nmHint, nmObjeto);
				HashMap<String,java.lang.Object> hash = new HashMap<String,java.lang.Object>();
				hash.put("objeto", objeto);
				hash.put("index", i);
				int status = insert(objeto, connection);
				if (status > 0)
					countSucessImport++;
				else {
					countErrorsImport++;
					if (status == EXISTS_ID_ERROR)
						countErrorsIdExistImport++;
				}
				hash.put("status", status);
				listObjetos.add(hash);
			}

			hashResults.put("listObjetos", listObjetos);
			hashResults.put("countObjetos", countObjetos);
			hashResults.put("countSucessImport", countSucessImport);
			hashResults.put("countErrorsImport", countErrorsImport);
			hashResults.put("countErrorsIdExistImport", countErrorsIdExistImport);

			if (isConnectionNull)
				connection.commit();

			return hashResults;
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int insert(Objeto objeto){
		return insert(objeto, null);
	}

	public static int insert(Objeto objeto, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			if (objeto.getCdSistema() <= 0)
				return NOT_DEFINED_SYSTEM_ERROR;
			else if (objeto.getCdFormulario() <= 0)
				return NOT_DEFINED_FORM_ERROR;
			else {
				PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
						"FROM seg_objeto A " +
						"WHERE A.cd_sistema = ? " +
						"  AND A.cd_formulario = ? " +
						"  AND A.nm_objeto = ?");
				pstmt.setInt(1, objeto.getCdSistema());
				pstmt.setInt(2, objeto.getCdFormulario());
				pstmt.setString(3, objeto.getNmObjeto());
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
					return EXISTS_ID_ERROR;
				else
					return ObjetoDAO.insert(objeto, connection);
			}
		}
		catch(Exception e){
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! ObjetoServices.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	
	public static ResultSetMap sincronizar(ArrayList<String> ids) {
		return sincronizar(ids, null);
	}

	public static ResultSetMap sincronizar(ArrayList<String> ids, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
			}
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.* " +
					"FROM SEG_OBJETO A " +
					"LEFT OUTER JOIN SEG_ACAO B ON(A.CD_ACAO = B.CD_ACAO) " +
					"WHERE NM_OBJETO IN (" + ids.toString().replaceAll("[\\[\\]]", "") + ")");
			return new ResultSetMap(pstmt.executeQuery());
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
	
	
	public static HashMap<String, java.lang.Object> sincronizeObjects(ServletRequest request) {
		return sincronizeObjects(request, null);
	}

	public static HashMap<String, java.lang.Object> sincronizeObjects(ServletRequest request, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			HashMap<String, java.lang.Object> hashResults = new HashMap<String,java.lang.Object>();
			ArrayList<HashMap<String, java.lang.Object>> listObjetos = new ArrayList<HashMap<String, java.lang.Object>>();
			int countSucessImport = 0;
			int countErrorsImport = 0;
			int countErrorsIdExistImport = 0;
			int countNewFormularios = 0;
			int countNewSistemas = 0;
			int countNewModulos = 0;
			int countNewGruposAcoes = 0;
			int countNewAcoes = 0;
			int countNewObjetos = 0;
			int countUpdateFormularios = 0;
			int countUpdateSistemas = 0;
			int countUpdateModulos = 0;
			int countUpdateGruposAcoes = 0;
			int countUpdateAcoes = 0;
			int countUpdateObjetos = 0;
			int countDeleteFormularios = 0;
			int countDeleteSistemas = 0;
			int countDeleteModulos = 0;
			int countDeleteGruposAcoes = 0;
			int countDeleteAcoes = 0;
			int countDeleteObjetos = 0;
			ArrayList<Sistema> sistemas 		= new ArrayList<Sistema>();
			ArrayList<Formulario> formularios 	= new ArrayList<Formulario>();
			ArrayList<Objeto> objetos 			= new ArrayList<Objeto>();
			ArrayList<Modulo> modulos 			= new ArrayList<Modulo>();
			ArrayList<AgrupamentoAcao> agrupamentosAcoes = new ArrayList<AgrupamentoAcao>();
			ArrayList<Acao> acoes 				= new ArrayList<Acao>();

			int countObjetos = RequestUtilities.getAsInteger(request, "countObjetos", 0);
			for (int i=0; i<countObjetos; i++) {
				String nmObjeto = RequestUtilities.getParameterAsString(request, "nmObjeto_" + i, "", true, RequestUtilities.NORMAL_CASE);
				String nmHint = RequestUtilities.getParameterAsString(request, "nmHint_" + i, "", true, RequestUtilities.NORMAL_CASE);
				int tpObjeto = RequestUtilities.getParameterAsInteger(request, "tpObjeto_" + i, 0);
				String nmFormulario = RequestUtilities.getParameterAsString(request, "nmFormulario_" + i, "", true, RequestUtilities.NORMAL_CASE);
				String nmTitulo = RequestUtilities.getParameterAsString(request, "nmTitulo_" + i, "", true, RequestUtilities.NORMAL_CASE);
				String nmSistema = RequestUtilities.getParameterAsString(request, "nmSistema_" + i, "", true, RequestUtilities.NORMAL_CASE);
				String idSistema = RequestUtilities.getParameterAsString(request, "idSistema_" + i, "", true, RequestUtilities.NORMAL_CASE);
				String nmAgrupamento = RequestUtilities.getParameterAsString(request, "nmAgrupamento_" + i, "", true, RequestUtilities.NORMAL_CASE);
				String idAgrupamento = RequestUtilities.getParameterAsString(request, "idAgrupamento_" + i, "", true, RequestUtilities.NORMAL_CASE);
				String nmModulo = RequestUtilities.getParameterAsString(request, "nmModulo_" + i, "", true, RequestUtilities.NORMAL_CASE);
				String idModulo = RequestUtilities.getParameterAsString(request, "idModulo_" + i, "", true, RequestUtilities.NORMAL_CASE);
				String nmAcao = RequestUtilities.getParameterAsString(request, "nmAcao_" + i, "", true, RequestUtilities.NORMAL_CASE);
				String dsAcao = RequestUtilities.getParameterAsString(request, "dsAcao_" + i, "", true, RequestUtilities.NORMAL_CASE);

				int cdModulo = 0;
				int cdAcao = 0;
				int cdAgrupamento = 0;

				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				HashMap<String, java.lang.Object> hash = new HashMap<String,java.lang.Object>();
				hash.put("index", i);

				/* identificacao de sistema */
				if (idSistema == null || idSistema.trim().equals("")) {
					hash.put("status", BLANK_ID_SYSTEM_ERROR);
					listObjetos.add(hash);
					countErrorsImport++;
					continue;
				}
				criterios.clear();
				criterios.add(new ItemComparator("ID_SISTEMA", idSistema, ItemComparator.EQUAL, Types.VARCHAR));
				ResultSetMap rsm = SistemaDAO.find(criterios, connection);
				int cdSistema = rsm.next() ? rsm.getInt("CD_SISTEMA") : 0;
				if (cdSistema <= 0) {
					/* inclusao de sistema nao existente */
					Sistema sistema = new Sistema(0,
							nmSistema,
							idSistema,
							1/*lgAtivo*/);
					if ((cdSistema = SistemaDAO.insert(sistema, connection)) <= 0){
						if (isConnectionNull)
							Conexao.rollback(connection);
						return null;
					}
					else {
						sistema.setCdSistema(cdSistema);
						sistemas.add(sistema);
						countNewSistemas++;
					}
				}
				/* atualizacao de sistema */
				else {
					/* consulta de sistema atualizados */
					int j = 0;
					for (j=0; j<sistemas.size(); j++)
						if (((Sistema)sistemas.get(j)).getCdSistema() == cdSistema)
							break;
					if (j == sistemas.size()) {
						Sistema sistema = new Sistema(cdSistema,
								nmSistema,
								idSistema,
								1/*lgAtivo*/);
						if (SistemaDAO.update(sistema, connection) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return null;
						}
						else {
							sistemas.add(sistema);
							countUpdateSistemas++;
						}
					}
				}

				/* identificacao de formulario */
				if (nmFormulario == null || nmFormulario.trim().equals("")) {
					hash.put("status", BLANK_ID_FORM_ERROR);
					listObjetos.add(hash);
					countErrorsImport++;
					continue;
				}
				criterios.clear();
				criterios.add(new ItemComparator("CD_SISTEMA", Integer.toString(cdSistema), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("NM_FORMULARIO", nmFormulario, ItemComparator.EQUAL, Types.VARCHAR));
				rsm = FormularioDAO.find(criterios, connection);
				int cdFormulario = rsm.next() ? rsm.getInt("CD_FORMULARIO") : 0;
				if (cdFormulario <= 0) {
					/* inclusao de formulario nao existente */
					Formulario formulario = new Formulario(0, cdSistema, nmFormulario, nmTitulo);
					if ((cdFormulario = FormularioDAO.insert(formulario, connection)) <= 0){
						if (isConnectionNull)
							Conexao.rollback(connection);
						return null;
					}
					else {
						formulario.setCdFormulario(cdFormulario);
						formularios.add(formulario);
						countNewFormularios++;
					}
				}
				/* atualizacao de formulario */
				else {
					/* consulta de formularios atualizados */
					int j = 0;
					for (j=0; j<formularios.size(); j++)
						if (((Formulario)formularios.get(j)).getCdSistema() == cdSistema && ((Formulario)formularios.get(j)).getCdFormulario() == cdFormulario)
							break;
					if (j == formularios.size()) {
						Formulario formulario = new Formulario(cdFormulario, cdSistema, nmFormulario, nmTitulo);
						if (FormularioDAO.update(formulario, connection) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return null;
						}
						else {
							formularios.add(formulario);
							countUpdateFormularios++;
						}
					}
				}

				/* identifica se objeto eh vinculado a alguma acao */
				java.lang.System.out.println(nmAcao + " - " + idModulo);
				if (nmAcao != null && !nmAcao.trim().equals("")) {
					/* identificacao de modulo */
					if (idModulo == null || idModulo.trim().equals("")) {
						hash.put("status", BLANK_ID_MODULE_ERROR);
						listObjetos.add(hash);
						countErrorsImport++;
						continue;
					}
					criterios.clear();
					criterios.add(new ItemComparator("CD_SISTEMA", Integer.toString(cdSistema), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("ID_MODULO", idModulo, ItemComparator.EQUAL, Types.VARCHAR));
					rsm = ModuloDAO.find(criterios, connection);
					cdModulo = rsm.next() ? rsm.getInt("CD_MODULO") : 0;
					if (cdModulo <= 0) {
						/* inclusao de modulo nao existente */
						Modulo modulo = new Modulo(0,
								cdSistema,
								nmModulo,
								idModulo,
								""/*nrVersao*/,
								""/*nrRelease*/,
								1/*lgAtivo*/);
						if ((cdModulo = ModuloDAO.insert(modulo, connection)) <= 0){
							if (isConnectionNull)
								Conexao.rollback(connection);
							return null;
						}
						else {
							modulo.setCdModulo(cdModulo);
							modulos.add(modulo);
							countNewModulos++;
						}
					}
					/* atualizacao de modulo */
					else {
						/* consulta de modulos atualizados */
						int j = 0;
						for (j=0; j<modulos.size(); j++)
							if (((Modulo)modulos.get(j)).getCdSistema() == cdSistema && ((Modulo)modulos.get(j)).getCdModulo() == cdModulo)
								break;
						if (j == modulos.size()) {
							Modulo modulo = new Modulo(cdModulo,
									cdSistema,
									nmModulo,
									idModulo,
									""/*nrVersao*/,
									""/*nrRelease*/,
									1/*lgAtivo*/);
							if (ModuloDAO.update(modulo, connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return null;
							}
							else {
								modulos.add(modulo);
								countUpdateModulos++;
							}
						}
					}

					/* identificacao de agrupamento de acoes */
					if (idAgrupamento == null || idAgrupamento.trim().equals("")) {
						hash.put("status", BLANK_ID_GROUP_ACTIONS_ERROR);
						listObjetos.add(hash);
						countErrorsImport++;
						continue;
					}
					criterios.clear();
					criterios.add(new ItemComparator("CD_SISTEMA", Integer.toString(cdSistema), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("CD_MODULO", Integer.toString(cdModulo), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("id_agrupamento", idAgrupamento, ItemComparator.EQUAL, Types.VARCHAR));
					rsm = AgrupamentoAcaoDAO.find(criterios, connection);
					cdAgrupamento = rsm.next() ? rsm.getInt("cd_agrupamento") : 0;
					if (cdAgrupamento <= 0) {
						/* inclusao de agrupamento nao existente */
						AgrupamentoAcao agrupamento = new AgrupamentoAcao(0, 1 /*cdModulo*/, 
								cdSistema, nmAgrupamento, idAgrupamento, nmAgrupamento, 0, 1, 0/*nrOrdem*/);
						if ((cdAgrupamento = AgrupamentoAcaoDAO.insert(agrupamento, connection)) <= 0){
							if (isConnectionNull)
								Conexao.rollback(connection);
							return null;
						}
						else {
							agrupamento.setCdAgrupamento(cdAgrupamento);
							agrupamentosAcoes.add(agrupamento);
							countNewGruposAcoes++;
						}
					}
					/* atualizacao de agrupamento */
					else {
						/* consulta de agrupamentos atualizados */
						int j = 0;
						for (j=0; j<agrupamentosAcoes.size(); j++)
							if (((AgrupamentoAcao)agrupamentosAcoes.get(j)).getCdSistema() == cdSistema &&
									((AgrupamentoAcao)agrupamentosAcoes.get(j)).getCdAgrupamento() == cdAgrupamento)
								break;
						if (j == modulos.size()) {
							AgrupamentoAcao agrupamento = new AgrupamentoAcao(cdAgrupamento, 1 /*cdModulo*/, 
									cdSistema, nmAgrupamento, idAgrupamento, nmAgrupamento, 0, 1, 0/*nrOrdem*/);
							if (AgrupamentoAcaoDAO.update(agrupamento, connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return null;
							}
							else {
								agrupamentosAcoes.add(agrupamento);
								countUpdateGruposAcoes++;
							}
						}
					}

					/* identificacao de acao */
					criterios.clear();
					criterios.add(new ItemComparator("CD_SISTEMA", Integer.toString(cdSistema), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("CD_MODULO", Integer.toString(cdModulo), ItemComparator.EQUAL, Types.INTEGER));
					if (cdAgrupamento > 0)
						criterios.add(new ItemComparator("CD_AGRUPAMENTO", Integer.toString(cdAgrupamento), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("nm_acao", nmAcao, ItemComparator.EQUAL, Types.VARCHAR));
					rsm = AcaoDAO.find(criterios, connection);
					cdAcao = rsm.next() ? rsm.getInt("cd_acao") : 0;
					if (cdAcao <= 0) {
						/* inclusao de acao nao existente */
						Acao acao = new Acao(0, cdModulo, cdSistema, nmAcao, dsAcao, cdAgrupamento, null, 0, 0, 0);
						if ((cdAcao = AcaoDAO.insert(acao, connection)) <= 0){
							if (isConnectionNull)
								Conexao.rollback(connection);
							return null;
						}
						else {
							acao.setCdAcao(cdAcao);
							acoes.add(acao);
							countNewAcoes++;
						}
					}
					/* atualizacao de acao */
					else {
						/* consulta de acoes atualizados */
						int j = 0;
						for (j=0; j<acoes.size(); j++)
							if (((Acao)acoes.get(j)).getCdSistema() == cdSistema &&
									((Acao)acoes.get(j)).getCdAcao() == cdAcao)
								break;
						if (j == acoes.size()) {
							Acao acao = new Acao(cdAcao, cdModulo, cdSistema, nmAcao, dsAcao, cdAgrupamento, null, 0, 0, 0);
							if (AcaoDAO.update(acao, connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return null;
							}
							else {
								acoes.add(acao);
								countUpdateAcoes++;
							}
						}
					}
				}

				/* identificacao de objetos */
				if (nmObjeto == null || nmObjeto.trim().equals("")) {
					hash.put("status", BLANK_ID_OBJECT_ERROR);
					listObjetos.add(hash);
					countErrorsImport++;
					continue;
				}
				criterios.clear();
				criterios.add(new ItemComparator("CD_SISTEMA", Integer.toString(cdSistema), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("CD_FORMULARIO", Integer.toString(cdFormulario), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("NM_OBJETO", nmObjeto, ItemComparator.EQUAL, Types.VARCHAR));
				rsm = ObjetoDAO.find(criterios, connection);
				int cdObjeto = rsm.next() ? rsm.getInt("CD_OBJETO") : 0;
				if (cdObjeto <= 0) {
					/* inclusao de objeto nao existente */
					Objeto objeto = new Objeto(cdFormulario, 0, cdSistema, cdAcao, cdModulo, tpObjeto, nmHint, nmObjeto);
					if ((cdObjeto = insert(objeto, connection)) <= 0) {
						countErrorsImport++;
						if (cdObjeto == EXISTS_ID_ERROR){
							countErrorsIdExistImport++;
							hash.put("status", cdObjeto);
						}
						else {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return null;
						}
					}
					else {
						countSucessImport++;
						hash.put("status", cdObjeto);
						objetos.add(objeto);
						countNewObjetos++;
					}
				}
				/* atualizacao de objeto */
				else {
					/* consulta de objetos atualizados */
					int j = 0;
					for (j=0; j<objetos.size(); j++)
						if (((Objeto)objetos.get(j)).getCdSistema() == cdSistema && ((Objeto)objetos.get(j)).getCdFormulario() == cdFormulario &&
								((Objeto)objetos.get(j)).getCdObjeto() == cdObjeto)
							break;
					if (j == objetos.size()) {
						Objeto objeto = new Objeto(cdFormulario, cdObjeto, cdSistema, cdAcao, cdModulo, tpObjeto, nmHint, nmObjeto);
						if (ObjetoDAO.update(objeto, connection) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return null;
						}
						else {
							objetos.add(objeto);
							countUpdateObjetos++;
						}
					}
				}

				listObjetos.add(hash);
			}

			hashResults.put("listObjetos", listObjetos);
			hashResults.put("countObjetos", countObjetos);
			hashResults.put("countSucessImport", countSucessImport);
			hashResults.put("countErrorsImport", countErrorsImport);
			hashResults.put("countNewFormularios", countNewFormularios);
			hashResults.put("countNewSistemas", countNewSistemas);
			hashResults.put("countNewModulos", countNewModulos);
			hashResults.put("countNewGruposAcoes", countNewGruposAcoes);
			hashResults.put("countNewAcoes", countNewAcoes);
			hashResults.put("countNewObjetos", countNewObjetos);
			hashResults.put("countUpdateFormularios", countUpdateFormularios);
			hashResults.put("countUpdateSistemas", countUpdateSistemas);
			hashResults.put("countUpdateModulos", countUpdateModulos);
			hashResults.put("countUpdateGruposAcoes", countUpdateGruposAcoes);
			hashResults.put("countUpdateAcoes", countUpdateAcoes);
			hashResults.put("countUpdateObjetos", countUpdateObjetos);
			hashResults.put("countDeleteFormularios", countDeleteFormularios);
			hashResults.put("countDeleteSistemas", countDeleteSistemas);
			hashResults.put("countDeleteModulos", countDeleteModulos);
			hashResults.put("countDeleteGruposAcoes", countDeleteGruposAcoes);
			hashResults.put("countDeleteAcoes", countDeleteAcoes);
			hashResults.put("countDeleteObjetos", countDeleteObjetos);

			hashResults.put("countErrorsIdExistImport", countErrorsIdExistImport);

			if (isConnectionNull)
				connection.commit();

			return hashResults;
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		if (criterios == null)
			criterios = new ArrayList<ItemComparator>();
		return findCompleto(criterios, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, Connection connection) {
		return Search.find("SELECT A.*, B.nm_formulario, B.nm_titulo, " +
				"C.nm_sistema, C.id_sistema, D.nm_acao, D.ds_acao, D.cd_agrupamento, E.nm_agrupamento, " +
				"E.id_agrupamento, E.ds_agrupamento, F.nm_modulo, F.id_modulo " +
				"FROM seg_objeto A " +
				"JOIN seg_formulario B ON (A.cd_formulario = B.cd_formulario AND" +
				"						   A.cd_sistema = B.cd_sistema) " +
				"JOIN seg_sistema C ON (B.cd_sistema = C.cd_sistema) " +
				"LEFT OUTER JOIN seg_acao D ON (A.cd_acao = D.cd_acao AND " +
				"								A.cd_modulo = D.cd_modulo AND " +
				"								A.cd_sistema = D.cd_sistema) " +
				"LEFT OUTER JOIN seg_agrupamento_acao E ON (D.cd_agrupamento = E.cd_agrupamento AND " +
				"											D.cd_modulo = E.cd_modulo AND " +
				"											D.cd_sistema = E.cd_sistema) " +
				"LEFT OUTER JOIN seg_modulo F ON (D.cd_modulo = F.cd_modulo AND " +
				"								  D.cd_sistema = F.cd_sistema)", criterios, connection!=null ? connection : Conexao.conectar(), connection==null);
	}

	public static ResultSetMap mountResultSet(ServletRequest request, FileUpload fileUpload) {
		ResultSetMap rsm = new ResultSetMap();
		int countObjetos = RequestUtilities.getParameterAsInteger(request, "countObjetos", 0, fileUpload);
		for (int i=0; i<countObjetos; i++) {
			String nmObjeto = RequestUtilities.getParameterAsString(request, "nmObjeto_" + i, "", true, RequestUtilities.NORMAL_CASE, fileUpload, true);
			String nmHint = RequestUtilities.getParameterAsString(request, "nmHint_" + i, "", true, RequestUtilities.NORMAL_CASE, fileUpload, true);
			int tpObjeto = RequestUtilities.getParameterAsInteger(request, "tpObjeto_" + i, 0, fileUpload);
			String nmFormulario = RequestUtilities.getParameterAsString(request, "nmFormulario_" + i, "", true, RequestUtilities.NORMAL_CASE, fileUpload, true);
			String nmTitulo = RequestUtilities.getParameterAsString(request, "nmTitulo_" + i, "", true, RequestUtilities.NORMAL_CASE, fileUpload, true);
			String nmSistema = RequestUtilities.getParameterAsString(request, "nmSistema_" + i, "", true, RequestUtilities.NORMAL_CASE, fileUpload, true);
			String idSistema = RequestUtilities.getParameterAsString(request, "idSistema_" + i, "", true, RequestUtilities.NORMAL_CASE, fileUpload, true);
			String nmAgrupamento = RequestUtilities.getParameterAsString(request, "nmAgrupamento_" + i, "", true, RequestUtilities.NORMAL_CASE, fileUpload, true);
			String idAgrupamento = RequestUtilities.getParameterAsString(request, "idAgrupamento_" + i, "", true, RequestUtilities.NORMAL_CASE, fileUpload, true);
			String nmModulo = RequestUtilities.getParameterAsString(request, "nmModulo_" + i, "", true, RequestUtilities.NORMAL_CASE, fileUpload, true);
			String idModulo = RequestUtilities.getParameterAsString(request, "idModulo_" + i, "", true, RequestUtilities.NORMAL_CASE, fileUpload, true);
			String nmAcao = RequestUtilities.getParameterAsString(request, "nmAcao_" + i, "", true, RequestUtilities.NORMAL_CASE, fileUpload, true);
			String dsAcao = RequestUtilities.getParameterAsString(request, "dsAcao_" + i, "", true, RequestUtilities.NORMAL_CASE, fileUpload, true);
			HashMap<String,java.lang.Object> register = new HashMap<String,java.lang.Object>();
			register.put("NM_OBJETO", nmObjeto);
			register.put("NM_HINT", nmHint);
			register.put("TP_OBJETO", tpObjeto);
			register.put("NM_FORMULARIO", nmFormulario);
			register.put("NM_TITULO", nmTitulo);
			register.put("NM_SISTEMA", nmSistema);
			register.put("ID_SISTEMA", idSistema);
			register.put("NM_AGRUPAMENTO", nmAgrupamento);
			register.put("ID_AGRUPAMENTO", idAgrupamento);
			register.put("NM_MODULO", nmModulo);
			register.put("ID_MODULO", idModulo);
			register.put("NM_ACAO", nmAcao);
			register.put("DS_ACAO", dsAcao);

			rsm.addRegister(register);
		}

		return rsm;
	}
	public static int save(Objeto objeto){
		return save(objeto, null);
	}
	public static int save(Objeto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if(objeto==null){
				return -1;
			}

			int retorno;
			if(objeto.getCdObjeto()==0){
				objeto.setCdSistema((objeto.getCdSistema()!=0)?objeto.getCdSistema():SistemaServices.getById("dotMng").getCdSistema());

				if(objeto.getNmHint()==null || objeto.getNmHint().equals("")){
					String nmHint = (objeto.getTpObjeto()==MENU_ITEM)?"Menu ":(objeto.getTpObjeto()==TOOLBAR_ITEM)?"Botão ":"Botão ";
					String[] parts = objeto.getNmObjeto().split("\\.");
					nmHint+=parts[parts.length-1].replaceAll("^btn|^bt|^mnb|^mn|^mi", "");
					objeto.setNmHint(nmHint);
				}

				retorno = ObjetoDAO.insert(objeto, connect);
			}
			else{
				retorno = ObjetoDAO.update(objeto, connect);
				retorno = retorno>0?objeto.getCdObjeto():retorno;
			}

			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! FormularioServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static int register(ArrayList<Objeto> objetos){
		return register(objetos, null);
	}
	public static int register(ArrayList<Objeto> objetos, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if(objetos==null){
				return -1;
			}
			int retorno=0;
			for(int i=0; i<objetos.size(); i++){
				if((retorno=save(objetos.get(i), connect))<0)
					break;
			}
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! FormularioServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public ResultSetMap getObjectsInfoByIds(ArrayList<String> ids) {
		Connection connect = null;
		try {
			connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.* " +
					"FROM SEG_OBJETO A " +
					"LEFT OUTER JOIN SEG_ACAO B ON(A.CD_ACAO = B.CD_ACAO) " +
					"WHERE NM_OBJETO IN (" + ids.toString().replaceAll("[\\[\\]]", "") + ")");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			return null;
		}
		finally {
			if (connect != null)
				Conexao.desconectar(connect);
		}
	}

	public ResultSetMap getObjectsInfoByFormIds(ArrayList<String> formIds) {
		Connection connect = null;
		try {
			connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.NM_FORMULARIO, B.NM_TITULO, C.CD_ACAO, C.NM_ACAO, C.DS_ACAO, C.CD_AGRUPAMENTO, C.CD_MODULO " +
					"FROM SEG_OBJETO A " +
					"JOIN SEG_FORMULARIO B ON(A.CD_FORMULARIO = B.CD_FORMULARIO " +
											" AND A.CD_SISTEMA = B.CD_SISTEMA " +
											" AND B.NM_FORMULARIO IN (" + formIds.toString().replaceAll("[\\[\\]]", "") + ")) " +
					"LEFT OUTER JOIN SEG_ACAO C ON(A.CD_ACAO = C.CD_ACAO AND A.CD_MODULO = C.CD_MODULO) ");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			return null;
		}
		finally {
			if (connect != null)
				Conexao.desconectar(connect);
		}
	}
}