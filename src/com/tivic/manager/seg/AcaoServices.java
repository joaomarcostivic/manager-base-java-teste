package com.tivic.manager.seg;

import java.io.File;
import java.sql.*;
import java.util.*;

import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.tivic.manager.conexao.*;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.util.LogUtils;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.security.Action;
import sol.security.Group;
import sol.security.GroupActions;
import sol.security.StatusPermissionActionUser;
import sol.security.StatusPermissionActionGroup;
import sol.security.User;
import sol.util.Result;

public class AcaoServices {

	public static final int PERMISSAO_USUARIO_SEM_SOBREPOR_PERMISSAO_GRUPO = 0;
	public static final int NAO_PERMISSAO_USUARIO_SOBREPOR_PERMISSAO_GRUPO = 1;
	public static final int PERMISSAO_USUARIO_SOBREPOR_PERMISSAO_GRUPO = 2;
	
	/**
	 * TIPOS DE ORGANIZAÇÃO
	 */
	public static final int COMUM = 0;
	public static final int DESTACADA = 1;
	public static final int DELETE = 2;
	public static final int UPDATE = 3;
	public static final int INSERT = 4;
	
	/**
	 * Salvar Ações
	 * @param acao
	 * @return Result
	 */
	public static Result save(Acao acao){
		return save(acao, null);
	}

	/**
	 * Salvar Ações
	 * @param acao
	 * @param connect
	 * @return Result
	 */
	public static Result save(Acao acao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(acao==null)
				return new Result(-1, "Erro ao salvar. Acao é nulo");

			int retorno;
			if(acao.getCdAcao()==0){
				retorno = AcaoDAO.insert(acao, connect);
				acao.setCdAcao(retorno);
			}
			else {
				retorno = AcaoDAO.update(acao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ACAO", acao);
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
	
	
	public static Result remove(int cdAcao, int cdModulo, int cdSistema){
		return remove(cdAcao, cdModulo, cdSistema, false, null);
	}
	public static Result remove(int cdAcao, int cdModulo, int cdSistema, boolean cascade){
		return remove(cdAcao, cdModulo, cdSistema, cascade, null);
	}
	public static Result remove(int cdAcao, int cdModulo, int cdSistema, boolean cascade, Connection connect){
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
			retorno = AcaoDAO.delete(cdAcao, cdModulo, cdSistema, connect);
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
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_agrupamento FROM seg_acao A"
										   + " JOIN seg_agrupamento_acao B ON (B.cd_agrupamento = A.cd_agrupamento AND B.cd_sistema = A.cd_sistema AND B.cd_modulo = A.cd_modulo)"
										   + " ORDER BY A.cd_sistema, A.cd_modulo, B.nm_agrupamento, tp_organizacao DESC, nr_ordem");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllOrdered() {
		return getAll(null);
	}

	public static ResultSetMap getAllOrdered(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_agrupamento FROM seg_acao A"
										   + " JOIN seg_agrupamento_acao B ON (B.cd_agrupamento = A.cd_agrupamento AND B.cd_sistema = A.cd_sistema AND B.cd_modulo = A.cd_modulo)"
										   + " ORDER BY B.nm_agrupamento ASC ");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByAgrupamento(int cdSistema, int cdModulo, int cdAgrupamento) {
		return getAllByAgrupamento(cdSistema, cdModulo, cdAgrupamento, null);
	}

	public static ResultSetMap getAllByAgrupamento(int cdSistema, int cdModulo, int cdAgrupamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_agrupamento FROM seg_acao A"
										   + " JOIN seg_agrupamento_acao B ON (B.cd_agrupamento = A.cd_agrupamento AND B.cd_sistema = A.cd_sistema AND B.cd_modulo = A.cd_modulo) "
										   + " WHERE 1=1 "
										   + (cdSistema > 0 ? " AND A.cd_sistema = " + cdSistema : "")
										   + (cdModulo > 0 ? " AND A.cd_modulo = " + cdModulo : "")
										   + (cdAgrupamento > 0 ? " AND A.cd_agrupamento = " + cdAgrupamento : "")
										   + " ORDER BY B.nm_agrupamento, tp_organizacao DESC, cd_acao_superior, nr_ordem");
			
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcaoServices.getAllByAgrupamento: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcaoServices.getAllByAgrupamento: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findByAgrupamento(int cdSistema, int cdModulo, int cdAgrupamento) {
		return findByAgrupamento(cdSistema, cdModulo, cdAgrupamento, null);
	}

	public static ResultSetMap findByAgrupamento(int cdSistema, int cdModulo, int cdAgrupamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_acao WHERE cd_sistema = ? AND cd_modulo = ? AND cd_agrupamento = ? ORDER BY nr_ordem");
			pstmt.setInt(1, cdSistema);
			pstmt.setInt(2, cdModulo);
			pstmt.setInt(3, cdAgrupamento);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcaoServices.getAllByAgrupamento: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcaoServices.getAllByAgrupamento: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	@Deprecated
	public static int salvar(Acao acao){
		return save(acao, null).getCode();
	}

	public static Result save(ArrayList<Acao> acoes){
		return save(acoes, null, null);
	}
	public static Result save(ArrayList<Acao> acoes, AgrupamentoAcao agrupamento){
		return save(acoes, agrupamento, null);
	}
	public static Result save(ArrayList<Acao> acoes, Connection connect){
		return save(acoes, null, connect);
	}
	public static Result save(ArrayList<Acao> acoes, AgrupamentoAcao agrupamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if(acoes==null){
				return new Result(-1, "Lista de ações é nula.");
			}
			int retorno=0;

			if(agrupamento!=null){
				retorno = AgrupamentoAcaoServices.save(agrupamento, connect).getCode();
				if(retorno<0){
					Conexao.rollback(connect);
					return new Result(-2, "Erro ao salvar agrupamento de ações.");
				}
				agrupamento.setCdAgrupamento(retorno);
			}

			LogUtils.debug("ACOES:\n" + acoes.toString());
			
			for(int i=0; i<acoes.size(); i++){
				Acao acao = acoes.get(i);

				if(agrupamento!=null)
					acao.setCdAgrupamento(agrupamento.getCdAgrupamento());
				if((retorno=save(acao, connect).getCode())<0)
					break;
			}
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, retorno < 0 ? "Erro ao salvar ações." : "Ações salvas com sucesso");
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! AcaoServices.save: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result((-1)*sqlExpt.getErrorCode(), "Erro ao salvar ações.");
		}
		catch(Exception e){
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! AcaoServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao salvar ações.");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public int insertAction(ArrayList<HashMap<String, Object>> acoes) {
		return insertAction(acoes, null);
	}

	public int insertAction(ArrayList<HashMap<String, Object>> acoes, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			for (int i=0; acoes!=null && i<acoes.size(); i++) {
				HashMap<String, Object> hash = acoes.get(i);
				Acao acao = (Acao)hash.get("acao");
				if (AcaoDAO.insert(acao, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
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

	public int insertAction(String nameAction, String descriptionAction, int idModule, int idSystem, int idGroupActions, String nmAction) {
		return AcaoDAO.insert(new Acao(0, idModule, idSystem, nameAction, descriptionAction, idGroupActions, nmAction, 0, 0, 0));
	}

	public int updateAction(int idAction, String nameAction, String descriptionAction, int idModule, int idSystem, int idGroupActions, String nmAction) {
		return AcaoDAO.update(new Acao(idAction, idModule, idSystem, nameAction, descriptionAction, idGroupActions, nmAction, 0, 0, 0));
	}

	public int deleteAction(int idAction, int idModule, int idSystem) {
		return AcaoDAO.delete(idAction, idModule, idSystem);
	}

	public ResultSetMap getActionsOfSystem(int idSystem) {
		Connection connect = null;
		try {
			connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					                    "SELECT A.*, A.CD_ACAO AS ID_ACTION, A.NM_ACAO AS NAME_ACTION, A.DS_ACAO AS DESCRIPTION_ACTION, B.CD_AGRUPAMENTO AS ID_GROUP_ACTIONS  " +
										"FROM SEG_ACAO A " +
										"LEFT OUTER JOIN SEG_AGRUPAMENTO_ACAO B ON (A.CD_AGRUPAMENTO = B.CD_AGRUPAMENTO AND A.CD_SISTEMA = B.CD_SISTEMA) " +
										"WHERE A.CD_SISTEMA = ?");
			pstmt.setInt(1, idSystem);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("DESCRIPTION_ACTION");
			rsm.orderBy(fields);
			return rsm;
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


	public StatusPermissionActionGroup[] getStatusPermissionActionOfGroup(int idGroup, int idSystem) {
		return getStatusPermissionActionOfGroup(idGroup, 0, idSystem, null);
	}

	public int saveStatusPermissionActionGroup(StatusPermissionActionGroup status) {
		return saveStatusPermissionActionGroup(status, null);
	}

	public int saveStatusPermissionActionGroup(StatusPermissionActionGroup status, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			if (!status.isAccessible()) {
				PreparedStatement pstmt = connect.prepareStatement(
						                 "DELETE FROM SEG_GRUPO_PERMISSAO_ACAO " +
										 "WHERE CD_GRUPO = ? " +
										 "  AND CD_SISTEMA = ? " +
										 "  AND CD_ACAO = ?");
				pstmt.setInt(1, status.getGroup().getId());
				pstmt.setInt(2, status.getAction().getId());
				pstmt.execute();
			}
			else {
				PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM SEG_GRUPO_PERMISSAO_ACAO " +
						 "WHERE CD_GRUPO = ? " +
						 "  AND CD_SISTEMA = ? " +
						 "  AND CD_ACAO = ?");
				pstmt.setInt(1, status.getGroup().getId());
				pstmt.setInt(2, status.getAction().getModule().getSystem().getId());
				pstmt.setInt(3, status.getAction().getId());
				if (!pstmt.executeQuery().next()) {
					pstmt = connect.prepareStatement("INSERT INTO SEG_GRUPO_PERMISSAO_ACAO (CD_GRUPO, CD_SISTEMA, CD_ACAO) VALUES(?, ?, ?)");
					pstmt.setInt(1, status.getGroup().getId());
					pstmt.setInt(2, status.getAction().getModule().getSystem().getId());
					pstmt.setInt(3, status.getAction().getId());
					pstmt.execute();
				}
			}
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static StatusPermissionActionUser[] getStatusPermissionActionsOfUser(int idUser) {
		return new AcaoServices().getStatusPermissionActionsOfUser(idUser, 0, 0);
	}

	public static StatusPermissionActionUser[] getStatusPermissionActionsOfUser(int idUser, Connection connection) {
		return getStatusPermissionActionsOfUser(idUser, 0, 0, connection);
	}

	public static StatusPermissionActionUser[] getStatusPermissionActionsOfUser(int idUser, int idSystem) {
		return getStatusPermissionActionsOfUser(idUser, 0, idSystem, null);
	}

	public StatusPermissionActionUser[] getStatusPermissionActionsOfUser(int idUser, int idSystem, Connection connection) {
		return getStatusPermissionActionsOfUser(idUser, 0, idSystem, connection);
	}

	public int saveStatusPermissionActionUser(StatusPermissionActionUser status) {
		return saveStatusPermissionActionUser(status, null);
	}

	public static int saveStatusPermissionActionUser(StatusPermissionActionUser status, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement(
					                 "DELETE FROM SEG_USUARIO_PERMISSAO_ACAO " +
									 "WHERE CD_USUARIO = ? " +
									 "  AND CD_SISTEMA = ? " +
									 "  AND CD_ACAO = ?");
			pstmt.setInt(1, status.getUser().getId());
			pstmt.setInt(2, status.getAction().getModule().getId());
			pstmt.setInt(3, status.getAction().getId());
			pstmt.execute();

			if (status.isAccessible() || status.isOverGroupPermission()) {
				int lgNatureza = status.isAccessible() && !status.isOverGroupPermission() ? PERMISSAO_USUARIO_SEM_SOBREPOR_PERMISSAO_GRUPO :
								 status.isAccessible() && status.isOverGroupPermission() ? PERMISSAO_USUARIO_SOBREPOR_PERMISSAO_GRUPO :
								 NAO_PERMISSAO_USUARIO_SOBREPOR_PERMISSAO_GRUPO;
				pstmt = connect.prepareStatement("INSERT INTO SEG_USUARIO_PERMISSAO_ACAO (CD_USUARIO, CD_SISTEMA, CD_ACAO, LG_NATUREZA) VALUES(?, ?, ?, ?)");
				pstmt.setInt(1, status.getUser().getId());
				pstmt.setInt(2, status.getAction().getModule().getSystem().getId());
				pstmt.setInt(3, status.getAction().getId());
				pstmt.setInt(4, lgNatureza);
				pstmt.execute();
			}

			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public Action[] getActionByName(String nameAction) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("NM_ACAO", "NM_ACAO", nameAction, ItemComparator.EQUAL, Types.VARCHAR, false));
		ResultSetMap rsm = Search.find(
				     "SELECT A.*, C.NM_SISTEMA, C.ID_SISTEMA, D.NM_AGRUPAMENTO, D.DS_AGRUPAMENTO, D.ID_AGRUPAMENTO " +
					 "FROM seg_acao A " +
					 "LEFT OUTER JOIN seg_sistema C ON (B.CD_SISTEMA = C.CD_SISTEMA) " +
					 "LEFT OUTER JOIN seg_agrupamento_acao D ON (A.CD_AGRUPAMENTO = D.CD_AGRUPAMENTO AND A.CD_SISTEMA = D.CD_SISTEMA)", criterios, Conexao.conectar());
		Action[] actions = new Action[rsm!=null ? rsm.size() : 0];
		for (int i = 0; rsm!=null && rsm.next(); i++) {
			//System system = new System(rsm.getInt("CD_SISTEMA"), rsm.getString("NM_SISTEMA"), rsm.getString("ID_SISTEMA"));
			GroupActions groupActions = rsm.getInt("CD_AGRUPAMENTO")==0 ? null : new GroupActions(rsm.getInt("CD_AGRUPAMENTO"), rsm.getString("NM_AGRUPAMENTO"), rsm.getString("DS_AGRUPAMENTO"), null);
			actions[i] = new Action(rsm.getInt("CD_ACAO"), rsm.getString("NM_ACAO"),  rsm.getString("DS_ACAO"), null, groupActions);
		}
		return actions;
	}

	public Action[] getActionByDescription(String descriptionAction) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		if (descriptionAction!=null && !descriptionAction.trim().equals(""))
			criterios.add(new ItemComparator("DS_ACAO", descriptionAction, ItemComparator.LIKE_BEGIN, Types.VARCHAR));
		ResultSetMap rsm = Search.find("SELECT A.*, C.NM_SISTEMA, C.ID_SISTEMA, D.NM_AGRUPAMENTO, D.DS_AGRUPAMENTO, D.ID_AGRUPAMENTO " +
				 					   "FROM SEG_ACAO A " +
				 					   "LEFT OUTER JOIN SEG_SISTEMA C ON (B.CD_SISTEMA = C.CD_SISTEMA) " +
				 					   "LEFT OUTER JOIN SEG_AGRUPAMENTO_ACAO D ON (A.CD_AGRUPAMENTO = D.CD_AGRUPAMENTO AND A.CD_SISTEMA = D.CD_SISTEMA)", criterios, Conexao.conectar());
		Action[] actions = new Action[rsm!=null ? rsm.size() : 0];
		for (int i = 0; rsm!=null && rsm.next(); i++) {
			//System system = new System(rsm.getInt("CD_SISTEMA"), rsm.getString("NM_SISTEMA"), rsm.getString("ID_SISTEMA"));
			GroupActions groupActions = rsm.getInt("CD_AGRUPAMENTO")==0 ? null : new GroupActions(rsm.getInt("CD_AGRUPAMENTO"), rsm.getString("NM_AGRUPAMENTO"), rsm.getString("DS_AGRUPAMENTO"), null);
			actions[i] = new Action(rsm.getInt("CD_ACAO"), rsm.getString("NM_ACAO"),  rsm.getString("DS_ACAO"), null, groupActions);
		}
		return actions;
	}

	public ResultSetMap getActionsOfGroupActions(int idGroupActions, int idSystem) {
		return getActionsOfGroupActions(idGroupActions, idSystem, null);
	}

	public static ResultSetMap getActionsOfGroupActions(int idGroupActions, int idSystem, Connection connect) {
		boolean isConnectonNull = connect==null;
		try {
			if (isConnectonNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, " +
					"A.CD_ACAO AS ID_ACTION, A.NM_ACAO AS NAME_ACTION, A.DS_ACAO AS DESCRIPTION_ACTION, A.CD_AGRUPAMENTO AS ID_GROUP_ACTIONS  " +
					"FROM SEG_ACAO A " +
					"WHERE A.CD_AGRUPAMENTO = ? " +
					"  AND A.CD_SISTEMA = ?");
			pstmt.setInt(1, idGroupActions);
			pstmt.setInt(2, idSystem);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("DESCRIPTION_ACTION");
			rsm.orderBy(fields);
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectonNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getActionsWithoutGroup(int idSystem) {
		return getActionsWithoutGroup(idSystem, null);
	}

	public static ResultSetMap getActionsWithoutGroup(int idSystem, Connection connection) {
		boolean isConnectonNull = connection==null;
		try {
			connection = isConnectonNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, " +
					"A.CD_ACAO AS ID_ACTION, A.NM_ACAO AS NAME_ACTION, A.DS_ACAO AS DESCRIPTION_ACTION, A.CD_AGRUPAMENTO AS ID_GROUP_ACTIONS  " +
					"FROM SEG_ACAO A " +
					"WHERE A.CD_SISTEMA = ? " +
					"  AND A.cd_agrupamento IS NULL");
			pstmt.setInt(1, idSystem);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("DESCRIPTION_ACTION");
			rsm.orderBy(fields);
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectonNull)
				Conexao.desconectar(connection);
		}
	}

	public StatusPermissionActionGroup[] getStatusPermissionActionOfGroup(int idGroup, int idGroupActions, int idSystem) {
		return getStatusPermissionActionOfGroup(idGroup, idGroupActions, idSystem, null);
	}

	public static StatusPermissionActionGroup[] getStatusPermissionActionOfGroup(int idGroup, int idGroupActions, int idSystem, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.*, C.*, D.NM_AGRUPAMENTO, D.DS_AGRUPAMENTO, D.ID_AGRUPAMENTO, (SELECT COUNT(*) FROM SEG_GRUPO_PERMISSAO_ACAO " +
															   "			 WHERE CD_GRUPO = ? " +
															   "			   AND CD_SISTEMA = A.CD_SISTEMA " +
															   "			   AND CD_ACAO = A.CD_ACAO) AS LG_PERMISSAO " +
															   "FROM SEG_ACAO A " +
															   "JOIN SEG_SISTEMA C ON (B.CD_SISTEMA = C.CD_SISTEMA) " +
															   "LEFT OUTER JOIN SEG_AGRUPAMENTO_ACAO D ON (A.CD_AGRUPAMENTO = D.CD_AGRUPAMENTO AND A.CD_SISTEMA = D.CD_SISTEMA) " +
															   "WHERE 1=1 " +
															   (idSystem==0 ? "" : " AND A.CD_SISTEMA = ? ") +
															   (idGroupActions==0 ? "" : " AND A.CD_AGRUPAMENTO = ? "));
			pstmt.setInt(1, idGroup);
			int i = 2;
			if (idSystem!=0)
				pstmt.setInt(i++, idSystem);
			if (idGroupActions != 0)
				pstmt.setInt(i++, idGroupActions);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			StatusPermissionActionGroup[] statusList = new StatusPermissionActionGroup[rsm==null ? 0 : rsm.size()];
			Grupo grupo = GrupoDAO.get(idGroup);
			Group group = new Group(idGroup, grupo==null ? null : grupo.getNmGrupo());
			for (i=0; rsm!=null && rsm.next(); i++) {
				//System system = system = new System(rsm.getInt("CD_SISTEMA"), rsm.getString("NM_SISTEMA"), rsm.getString("ID_SISTEMA"));
				GroupActions groupActions = rsm.getInt("CD_AGRUPAMENTO")==0 ? null : new GroupActions(rsm.getInt("CD_AGRUPAMENTO"), rsm.getString("NM_AGRUPAMENTO"), rsm.getString("DS_AGRUPAMENTO"), null);
				statusList[i] = new StatusPermissionActionGroup(new Action(rsm.getInt("CD_ACAO"), rsm.getString("NM_ACAO"), rsm.getString("DS_ACAO"), null, groupActions), group, rsm.getInt("LG_PERMISSAO")>0);
			}
			return statusList;
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

	public StatusPermissionActionUser[] getStatusPermissionActionsOfUser(int idUser, int idGroupActions, int idSystem) {
		return getStatusPermissionActionsOfUser(idUser, idGroupActions, idSystem, null);
	}

	public static StatusPermissionActionUser[] getStatusPermissionActionsOfUser(int idUser, int idGroupActions, int idSystem, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, C.*, D.NM_AGRUPAMENTO, D.DS_AGRUPAMENTO, D.ID_AGRUPAMENTO, " +
															   "            (SELECT COUNT(*) FROM SEG_USUARIO_PERMISSAO_ACAO " +
															   "			 WHERE CD_USUARIO = ? " +
															   "			   AND CD_SISTEMA = A.CD_SISTEMA " +
															   "			   AND CD_ACAO = A.CD_ACAO) AS LG_PERMISSAO, " +
															   "			(SELECT MAX(LG_NATUREZA) FROM SEG_USUARIO_PERMISSAO_ACAO " +
															   "			 WHERE CD_USUARIO = ? " +
															   "			   AND CD_SISTEMA = A.CD_SISTEMA " +
															   "			   AND CD_ACAO = A.CD_ACAO) AS LG_NATUREZA, " +
															   "			(SELECT COUNT(*) FROM SEG_GRUPO_PERMISSAO_ACAO D, SEG_USUARIO_GRUPO E " +
															   "			 WHERE E.CD_USUARIO = ? " +
															   "			   AND D.CD_GRUPO = E.CD_GRUPO " +
															   "			   AND D.CD_SISTEMA = A.CD_SISTEMA " +
															   "			   AND D.CD_ACAO = A.CD_ACAO) AS LG_PERMISSAO_GRUPO " +
															   "FROM SEG_ACAO A " +
															   "JOIN SEG_SISTEMA C ON (A.CD_SISTEMA = C.CD_SISTEMA) " +
															   "LEFT OUTER JOIN SEG_AGRUPAMENTO_ACAO D ON (A.CD_AGRUPAMENTO = D.CD_AGRUPAMENTO AND A.CD_SISTEMA = D.CD_SISTEMA)" +
															   "WHERE 1=1 " +
															   (idSystem==0 ? "" : " AND A.CD_SISTEMA = ? ") +
															   (idGroupActions==0 ? "" : " AND A.CD_AGRUPAMENTO = ? "));
			pstmt.setInt(1, idUser);
			pstmt.setInt(2, idUser);
			pstmt.setInt(3, idUser);
			int i = 4;
			if (idSystem != 0)
				pstmt.setInt(i++, idSystem);
			if (idGroupActions != 0)
				pstmt.setInt(i++, idGroupActions);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			StatusPermissionActionUser[] statusList = new StatusPermissionActionUser[rsm==null ? 0 : rsm.size()];
			Usuario usuario = UsuarioDAO.get(idUser, connect);
			Pessoa pessoa = usuario==null || usuario.getCdPessoa()==0 ? null : PessoaDAO.get(usuario.getCdPessoa(), connect);
			User user = new User(idUser, pessoa==null ? null : pessoa.getNmPessoa(), usuario==null ? null : usuario.getNmLogin(), usuario==null ? null : usuario.getNmLogin());
			for (i=0; rsm!=null && rsm.next(); i++) {
				//System system = new System(rsm.getInt("CD_SISTEMA"), rsm.getString("NM_SISTEMA"), rsm.getString("ID_SISTEMA"));
				GroupActions groupActions = rsm.getInt("CD_AGRUPAMENTO")==0 ? null : new GroupActions(rsm.getInt("CD_AGRUPAMENTO"), rsm.getString("NM_AGRUPAMENTO"), rsm.getString("DS_AGRUPAMENTO"), null);
				boolean isAccessible = rsm.getInt("LG_PERMISSAO")>0 && (rsm.getInt("LG_NATUREZA")==PERMISSAO_USUARIO_SEM_SOBREPOR_PERMISSAO_GRUPO || rsm.getInt("LG_NATUREZA")==PERMISSAO_USUARIO_SOBREPOR_PERMISSAO_GRUPO);
				boolean isOverGroupPermission = rsm.getInt("LG_PERMISSAO")>0 && (rsm.getInt("LG_NATUREZA")==NAO_PERMISSAO_USUARIO_SOBREPOR_PERMISSAO_GRUPO || rsm.getInt("LG_NATUREZA")==PERMISSAO_USUARIO_SOBREPOR_PERMISSAO_GRUPO);
				boolean isAccessibleByGroups = rsm.getInt("LG_PERMISSAO_GRUPO")>0;
				statusList[i] = new StatusPermissionActionUser(new Action(rsm.getInt("CD_ACAO"), rsm.getString("NM_ACAO"), rsm.getString("DS_ACAO"), null, groupActions), user, isAccessible, isOverGroupPermission, isAccessibleByGroups);
			}
			return statusList;
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

	public static StatusPermissionActionUser getStatusPermissionAction(int cdUsuario, String nmAcao, String idSistema) {
		return getStatusPermissionAction(cdUsuario, nmAcao, idSistema, null);
	}

	public static StatusPermissionActionUser getStatusPermissionAction(int cdUsuario, String nmAcao, String idSistema,
			Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.*, C.*, D.NM_AGRUPAMENTO, D.DS_AGRUPAMENTO, D.ID_AGRUPAMENTO, (SELECT COUNT(*) FROM SEG_USUARIO_PERMISSAO_ACAO " +
															   "			 WHERE CD_USUARIO = ? " +
															   "			   AND CD_SISTEMA = A.CD_SISTEMA " +
															   "			   AND CD_ACAO = A.CD_ACAO) AS LG_PERMISSAO, " +
															   "					  (SELECT MAX(LG_NATUREZA) FROM SEG_USUARIO_PERMISSAO_ACAO " +
															   "			 WHERE CD_USUARIO = ? " +
															   "			   AND CD_SISTEMA = A.CD_SISTEMA " +
															   "			   AND CD_ACAO = A.CD_ACAO) AS LG_NATUREZA, " +
															   "					  (SELECT COUNT(*) FROM SEG_GRUPO_PERMISSAO_ACAO D, SEG_USUARIO_GRUPO E " +
															   "			 WHERE E.CD_USUARIO = ? " +
															   "			   AND D.CD_GRUPO = E.CD_GRUPO " +
															   "			   AND D.CD_SISTEMA = A.CD_SISTEMA " +
															   "			   AND D.CD_ACAO = A.CD_ACAO) AS LG_PERMISSAO_GRUPO " +
															   "FROM SEG_ACAO A " +
															   "JOIN SEG_SISTEMA C ON (B.CD_SISTEMA = C.CD_SISTEMA) " +
															   "LEFT OUTER JOIN SEG_AGRUPAMENTO_ACAO D ON (A.CD_AGRUPAMENTO = D.CD_AGRUPAMENTO AND A.CD_SISTEMA = D.CD_SISTEMA)" +
															   "WHERE C.id_sistema = ? " +
															   "  AND A.nm_acao = ?");
			pstmt.setInt(1, cdUsuario);
			pstmt.setInt(2, cdUsuario);
			pstmt.setInt(3, cdUsuario);
			pstmt.setString(4, idSistema);
			pstmt.setString(5, nmAcao);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			Usuario usuario = UsuarioDAO.get(cdUsuario, connection);
			Pessoa pessoa = usuario==null || usuario.getCdPessoa()==0 ? null : PessoaDAO.get(usuario.getCdPessoa(), connection);
			User user = new User(cdUsuario, pessoa==null ? null : pessoa.getNmPessoa(), usuario==null ? null : usuario.getNmLogin(), usuario==null ? null : usuario.getNmLogin());
			boolean isFound = rsm.next();
			//System system system = !isFound ? null : new System(rsm.getInt("CD_SISTEMA"), rsm.getString("NM_SISTEMA"), rsm.getString("ID_SISTEMA"));
			GroupActions groupActions = !isFound ? null : rsm.getInt("CD_AGRUPAMENTO")==0 ? null : new GroupActions(rsm.getInt("CD_AGRUPAMENTO"), rsm.getString("NM_AGRUPAMENTO"), rsm.getString("DS_AGRUPAMENTO"), null);
			boolean isAccessible = !isFound ? false : rsm.getInt("LG_PERMISSAO")>0 && (rsm.getInt("LG_NATUREZA")==PERMISSAO_USUARIO_SEM_SOBREPOR_PERMISSAO_GRUPO || rsm.getInt("LG_NATUREZA")==PERMISSAO_USUARIO_SOBREPOR_PERMISSAO_GRUPO);
			boolean isOverGroupPermission = !isFound ? false : rsm.getInt("LG_PERMISSAO")>0 && (rsm.getInt("LG_NATUREZA")==NAO_PERMISSAO_USUARIO_SOBREPOR_PERMISSAO_GRUPO || rsm.getInt("LG_NATUREZA")==PERMISSAO_USUARIO_SOBREPOR_PERMISSAO_GRUPO);
			boolean isAccessibleByGroups = !isFound ? false : rsm.getInt("LG_PERMISSAO_GRUPO")>0;
			return !isFound ? null : new StatusPermissionActionUser(new Action(rsm.getInt("CD_ACAO"), rsm.getString("NM_ACAO"), rsm.getString("DS_ACAO"),
					null, groupActions), user, isAccessible, isOverGroupPermission, isAccessibleByGroups);
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

	public static int delete(int cdAcao, int cdModulo, int cdSistema) {
		return delete(cdAcao, cdModulo, cdSistema, null);
	}

	public static int delete(int cdAcao, int cdModulo, int cdSistema, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			/* objetos vinculados a acao */
			PreparedStatement pstmt = connection.prepareStatement("UPDATE seg_objeto " +
					"SET cd_acao = NULL " +
					"WHERE cd_sistema = ? " +
					"  AND cd_modulo = ? " +
					"  AND cd_acao = ? ");
			pstmt.setInt(1, cdSistema);
			pstmt.setInt(2, cdModulo);
			pstmt.setInt(3, cdAcao);
			pstmt.execute();

			/* logs registrados da acao */
			pstmt = connection.prepareStatement("DELETE FROM log_execucao_acao " +
					"WHERE cd_sistema = ? " +
					"  AND cd_modulo = ? " +
					"  AND cd_acao = ? ");
			pstmt.setInt(1, cdSistema);
			pstmt.setInt(2, cdModulo);
			pstmt.setInt(3, cdAcao);
			pstmt.execute();

			/* permissoes de grupo da acao */
			pstmt = connection.prepareStatement("DELETE FROM seg_grupo_permissao_acao " +
					"WHERE cd_sistema = ? " +
					"  AND cd_modulo = ? " +
					"  AND cd_acao = ? ");
			pstmt.setInt(1, cdSistema);
			pstmt.setInt(2, cdModulo);
			pstmt.setInt(3, cdAcao);
			pstmt.execute();

			/* permissoes de usuario da acao */
			pstmt = connection.prepareStatement("DELETE FROM seg_usuario_permissao_acao " +
					"WHERE cd_sistema = ? " +
					"  AND cd_modulo = ? " +
					"  AND cd_acao = ? ");
			pstmt.setInt(1, cdSistema);
			pstmt.setInt(2, cdModulo);
			pstmt.setInt(3, cdAcao);
			pstmt.execute();

			if (AcaoDAO.delete(cdAcao, cdModulo, cdSistema, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! AcaoServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.nm_agrupamento, B.ds_agrupamento, B.id_agrupamento, " +
				"C.nm_sistema, C.id_sistema " +
				"FROM seg_acao A " +
				"LEFT OUTER JOIN seg_agrupamento_acao B ON (A.cd_agrupamento = B.cd_agrupamento AND" +
				"											A.cd_sistema = B.cd_sistema AND " +
				"											A.cd_sistema = B.cd_sistema) " +
				"JOIN seg_sistema C ON (A.cd_sistema = C.cd_sistema AND " +
				"					   A.cd_sistema = C.cd_sistema) ", criterios,
				connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	
	public static Acao getByIdAcao(String idAcao) {
		return getByIdAcao(idAcao, null);
	}

	public static Acao getByIdAcao(String idAcao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_acao WHERE id_acao=?");
			pstmt.setString(1, idAcao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Acao(rs.getInt("cd_acao"),
						rs.getInt("cd_modulo"),
						rs.getInt("cd_sistema"),
						rs.getString("nm_acao"),
						rs.getString("ds_acao"),
						rs.getInt("cd_agrupamento"),
						rs.getString("id_acao"),
						rs.getInt("tp_organizacao"),
						rs.getInt("nr_ordem"),
						rs.getInt("cd_acao_superior"));
			}else{
				System.out.println("Didnt Enter");
				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcaoServices.getByIdAcao: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	
	public static StatusPermissionActionUser getPermisaoFromSession(String nmAcao, HttpSession session) {
		try {
			StatusPermissionActionUser[] permissoes = session==null ? null : (StatusPermissionActionUser[])session.getAttribute("permissionsActionsUser");
			for (int i=0; permissoes!=null && i<permissoes.length; i++)
				if (permissoes[i].getAction().getName().equals(nmAcao)) {
					return permissoes[i];
				}
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public static boolean hasPermisaoFromSession(String nmAcao, HttpSession session) {
		try {
			StatusPermissionActionUser[] permissoes = session==null ? null : (StatusPermissionActionUser[])session.getAttribute("permissionsActionsUser");
			for (int i=0; permissoes!=null && i<permissoes.length; i++)
				if (permissoes[i].getAction().getName().equals(nmAcao)) {
					return !((permissoes[i].isAccessibleByGroups() && !permissoes[i].isAccessible() && permissoes[i].isOverGroupPermission()) ||
									   (!permissoes[i].isAccessible() && !permissoes[i].isAccessibleByGroups()));
				}
			return false;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return false;
		}
	}

	public static ArrayList<HashMap<String, Object>> hasPermissoesFromSession(String[] nmAcoes, HttpSession session) {
		try {
			StatusPermissionActionUser[] permissoes = session==null ? null : (StatusPermissionActionUser[])session.getAttribute("permissionsActionsUser");
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
			for (int j=0; nmAcoes!=null && j<nmAcoes.length; j++) {
				String nmAcao = nmAcoes[j];
				for (int i=0; permissoes!=null && i<permissoes.length; i++)
					if (permissoes[i].getAction().getName().equals(nmAcao)) {
						HashMap<String, Object> hash = new HashMap<String, Object>();
						hash.put("nmAcao", nmAcao);
						hash.put("hasPermissao", (permissoes[i].isAccessibleByGroups() && (!permissoes[i].isOverGroupPermission() || permissoes[i].isAccessible())) ||
										   permissoes[i].isAccessible());
						list.add(hash);
						break;
					}
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public ResultSetMap getActionsInfoByIds(ArrayList<String> actionIds) {
		Connection connect = null;
		try {
			connect = Conexao.conectar();
			String sql = "SELECT A.*" +
						"FROM SEG_ACAO A " +
						"WHERE A.NM_ACAO IN (" + actionIds.toString().replaceAll("[\\[\\]]", "") + ") ";
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

	public static int linkObjectsActions(HashMap<Acao, ArrayList<Objeto>> objectsActions){
		return linkObjectsActions(objectsActions, null);
	}
	public static int linkObjectsActions(HashMap<Acao, ArrayList<Objeto>> objectsActions, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if(objectsActions==null){
				return -1;
			}
			int retorno=0;

			Set<Acao> keys = objectsActions.keySet();
			Iterator<Acao> iterator = keys.iterator();

			while(iterator.hasNext()){
				Acao acao = iterator.next();
				ArrayList<Objeto> objetos = objectsActions.get(acao);

				for(int i=0; objetos!=null && i<objetos.size(); i++){
					Objeto objeto = ObjetoDAO.get(objetos.get(i).getCdFormulario(), objetos.get(i).getCdObjeto(), objetos.get(i).getCdSistema(), connect);
					objeto.setCdAcao(acao.getCdAcao());
					retorno = ObjetoDAO.update(objeto);

					if(retorno<0){
						Conexao.rollback(connect);
						return -2;
					}
				}
			}
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(java.lang.System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static int saveAgrupamentoAcao(AgrupamentoAcao agrupamento, Connection connect){
		try {
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT * FROM seg_agrupamento_acao " +
					"WHERE cd_sistema = ? " +
					"  AND cd_modulo  = ? " +
					"  AND id_agrupamento = ? ");
			pstmt.setInt(1, agrupamento.getCdSistema());
			pstmt.setInt(2, agrupamento.getCdModulo());
			pstmt.setString(3, agrupamento.getIdAgrupamento());
			ResultSetMap rsAgrupamento = new ResultSetMap(pstmt.executeQuery());
			
			boolean hasNext = rsAgrupamento.next();
			
			int cdAgrupamento =  hasNext ? rsAgrupamento.getInt("cd_agrupamento") : agrupamento.getCdAgrupamento();
			int cdAgrupamentoSuperior =  hasNext ? rsAgrupamento.getInt("cd_agrupamento_superior") : agrupamento.getCdAgrupamentoSuperior();
			int lgAtivo =  hasNext ? rsAgrupamento.getInt("lg_ativo") : agrupamento.getLgAtivo();
			int nrOrdem =  hasNext ? rsAgrupamento.getInt("nr_ordem") : agrupamento.getNrOrdem();
			
			agrupamento.setCdAgrupamento(cdAgrupamento);
			agrupamento.setCdAgrupamentoSuperior(cdAgrupamentoSuperior);
			agrupamento.setLgAtivo(lgAtivo);
			agrupamento.setNrOrdem(nrOrdem);
			
			Result result = AgrupamentoAcaoServices.save(agrupamento, connect);
			return (result.getCode()>0)?agrupamento.getCdAgrupamento():cdAgrupamento;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
	}
	
	public static int saveAcao( Acao acao, Connection connect ){
		try {
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT * FROM seg_acao " +
                    "WHERE  id_acao = ? ");
			pstmt.setString(1, acao.getIdAcao());
			ResultSet rsAcao = pstmt.executeQuery();
			
			boolean hasNext = rsAcao.next();
			
			int cdAcao =  hasNext ? rsAcao.getInt("cd_acao") : acao.getCdAcao();
			int cdAcaoSuperior =  hasNext ? rsAcao.getInt("cd_acao_superior") : acao.getCdAcaoSuperior();
			int nrOrdem =  hasNext ? rsAcao.getInt("nr_ordem") : acao.getNrOrdem();
			
			acao.setCdAcao(cdAcao);
			acao.setCdAcaoSuperior(cdAcaoSuperior);
			acao.setNrOrdem(nrOrdem);
			
			Result result = AcaoServices.save( acao, connect);
			return (result.getCode()>0)?acao.getCdAcao():cdAcao;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
	}
	
	/**
	 * Inicializa as permissões do módulo Gestão Eletrônica de Documentos.
	 * @param cdSistema
	 * @param cdModulo
	 * @param connect
	 */
	public static void initPermissoesGed(int cdSistema, int cdModulo, Connection connect){
		try	{
			
			/*
			 * MODELO PEÇAS
			 */
			int cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Modelo" /*nmAgrupamento*/,
					"GED_MODELO"/*idAgrupamento*/, "Permissões para gerenciamento de modelos"/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			int cdAcaoSuperior = saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Criar novo modelo"/*nmAcao*/, "Permitir o cadastro de novo modelo"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "GED.INS_MODELO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Gravar modelo"/*nmAcao*/, "Permitir alteração no modelo"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "GED.SAVE_MODELO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir modelo"/*nmAcao*/, "Permitir exclusão de modelo"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "GED.DEL_MODELO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 2/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Listar modelos"/*nmAcao*/, "Permitir listagem de modelos"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "GED.LIST_MODELO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 3/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			
			/*
			 * PEÇAS
			 */
			cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Geração de Peças" /*nmAgrupamento*/,
					"GED_PECA"/*idAgrupamento*/, "Permissões para gerenciamento de peças"/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 1/*nrOrdem*/), connect );
			
			cdAcaoSuperior = saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Gerar peça"/*nmAcao*/, "Permitir a geração de peças"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "GED.GEN_PECA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Anexar peça a um processo"/*nmAcao*/, "Permitir a anexação de peças em processos"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "GED.ADD_PECA_PROCESSO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Anexar peça a um documento"/*nmAcao*/, "Permitir a anexação de peças em documentos"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "GED.ADD_PECA_DOCUMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 2/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar peça anexada"/*nmAcao*/, "Permitir alteração de peças anexadas"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "GED.UPD_PECA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 3/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);

		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
	}
	
	/**
	 * Inicializa as permissões do módulo jurídico.
	 * @param cdSistema
	 * @param cdModulo
	 * @param connect
	 */
	public static void initPermissoesJur(int cdSistema, int cdModulo, Connection connect){
		try	{
			
			/*
			 * PROCESSO
			 */
			int cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Cadastro de Processo" /*nmAgrupamento*/,
					"PRC_PROCESSO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			int cdAcaoSuperior = saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar processo"/*nmAcao*/, "Permitir o cadastro de processos"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_PROCESSO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 31/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar processo"/*nmAcao*/, "Permitir alteração no processos"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_PROCESSO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 30/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir processo"/*nmAcao*/, "Permitir exclusão de processos"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_PROCESSO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 29/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar processos em lote"/*nmAcao*/, "Permitir alteração de processos em lote"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_PROCESSO_LOTE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 28/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir Processo"/*nmAcao*/, "Permitir a impressão de processos"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.PRINT_PROCESSO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 27/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Enviar email"/*nmAcao*/, "Permitir o envio de email de processos"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.ENVIO_EMAIL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 26/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			
			cdAcaoSuperior = saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Inserir testemunha"/*nmAcao*/, "Permitir o cadastro de testemunha no processo"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_TESTEMUNHA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 25/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar testemunha"/*nmAcao*/, "Permitir alteração no testemunha no processo"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_TESTEMUNHA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 24/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir testemunha"/*nmAcao*/, "Permitir exclusão de testemunha no processo"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_TESTEMUNHA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 23/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			
			cdAcaoSuperior = saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Inserir Oficial de Justiça"/*nmAcao*/, "Permitir o cadastro de Oficial de Justiça"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_OFICIAL_JUSTICA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 22/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Oficial de Justiça"/*nmAcao*/, "Permitir alteração no Oficial de Justiça"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_OFICIAL_JUSTICA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 21/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Oficial de Justiça"/*nmAcao*/, "Permitir exclusão de Oficial de Justiça"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_OFICIAL_JUSTICA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 20/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			
			cdAcaoSuperior = saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar andamento"/*nmAcao*/, "Permitir o cadastro de andamento"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_ANDAMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 19/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar andamento"/*nmAcao*/, "Permitir alteração no andamento"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_ANDAMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 18/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir andamento"/*nmAcao*/, "Permitir exclusão de andamento"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_ANDAMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 17/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar andamento coletivo"/*nmAcao*/, "Permitir o cadastro de andamento coletivo"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_ANDAMENTO_COLETIVO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 16/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			
			cdAcaoSuperior = saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar arquivo"/*nmAcao*/, "Permitir o cadastro de arquivo"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_ARQUIVO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 15/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar arquivo"/*nmAcao*/, "Permitir alteração no arquivo"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_ARQUIVO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 14/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir arquivo"/*nmAcao*/, "Permitir exclusão de arquivo"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_ARQUIVO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 13/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Visualizar arquivo"/*nmAcao*/, "Permitir visualização de arquivo"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.VIEW_ARQUIVO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 12/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			
			cdAcaoSuperior = saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar contrato"/*nmAcao*/, "Permitir o cadastro de contrato"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_CONTRATO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 11/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar contrato"/*nmAcao*/, "Permitir alteração no contrato"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_CONTRATO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 10/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir contrato"/*nmAcao*/, "Permitir exclusão de contrato"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_CONTRATO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 9/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			
			cdAcaoSuperior = saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar garantia"/*nmAcao*/, "Permitir o cadastro de garantia"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_GARANTIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 8/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar garantia"/*nmAcao*/, "Permitir alteração na garantia"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_GARANTIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 7/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir garantia"/*nmAcao*/, "Permitir exclusão de garantia"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_GARANTIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 6/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			
			cdAcaoSuperior = saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar Despesa"/*nmAcao*/, "Permitir o cadastro de Despesa"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_PROCESSO_FINANCEIRO_DESPESA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 5/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Despesa"/*nmAcao*/, "Permitir alteração na Despesa"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_PROCESSO_FINANCEIRO_DESPESA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 4/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Despesa"/*nmAcao*/, "Permitir exclusão de Despesa"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_PROCESSO_FINANCEIRO_DESPESA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 3/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			
			cdAcaoSuperior = saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar Receita"/*nmAcao*/, "Permitir o cadastro de Receita"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_PROCESSO_FINANCEIRO_RECEITA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Receita"/*nmAcao*/, "Permitir alteração na Receita"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_PROCESSO_FINANCEIRO_RECEITA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Receita"/*nmAcao*/, "Permitir exclusão de Receita"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_PROCESSO_FINANCEIRO_RECEITA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			
			cdAcaoSuperior = saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar Encerramento"/*nmAcao*/, "Permitir o cadastro de encerramento"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_PROCESSO_SENTENCA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Encerramento"/*nmAcao*/, "Permitir alteração no encerramento"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_PROCESSO_SENTENCA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Encerramento"/*nmAcao*/, "Permitir exclusão de encerramento"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_PROCESSO_SENTENCA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			
			cdAcaoSuperior = saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Marcar como Importante"/*nmAcao*/, "Permitir marcar o processo como Importante"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_LG_IMPORTANTE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			
			cdAcaoSuperior = saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Criar regra de faturamento"/*nmAcao*/, "Adicionar regra de faturamento via tela de processo"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_REGRA_FATURAMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			
			/*
			 * TRIBUNAL			
			 */
			cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Cadastro de Tribunal" /*nmAgrupamento*/,
					"PRC_TRIBUNAL"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar tribunal"/*nmAcao*/, "Permitir o cadastro de tribunal"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_TRIBUNAL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar tribunal"/*nmAcao*/, "Permitir alteração no tribunal"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_TRIBUNAL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir tribunal"/*nmAcao*/, "Permitir exclusão de tribunal"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_TRIBUNAL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			
			/*
			 * JUIZO			
			 */
			cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Cadastro de Juízo" /*nmAgrupamento*/,
					"PRC_JUIZO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar Juízo"/*nmAcao*/, "Permitir o cadastro de Juízo"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_JUIZO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Juízo"/*nmAcao*/, "Permitir alteração no Juízo"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_JUIZO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Juízo"/*nmAcao*/, "Permitir exclusão de Juízo"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_JUIZO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			
			/*
			 * GRUPO_PROCESSO
			 */
			cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Cadastro de Grupo de Processo" /*nmAgrupamento*/,
					"PRC_GRUPO_PROCESSO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar Grupo de Processo"/*nmAcao*/, "Permitir o cadastro de Grupo de Processo"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_GRUPO_PROCESSO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Grupo de Processo"/*nmAcao*/, "Permitir alteração no Grupo de Processo"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_GRUPO_PROCESSO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Grupo de Processo"/*nmAcao*/, "Permitir exclusão de Grupo de Processo"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_GRUPO_PROCESSO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			
			/*
			 * TIPO_ANDAMENTO
			 */
			cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Cadastro de Tipo de Andamento" /*nmAgrupamento*/,
					"PRC_TIPO_ANDAMENTO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar tipo de andamento"/*nmAcao*/, "Permitir o cadastro de tipo de andamento"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_TIPO_ANDAMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar tipo de andamento"/*nmAcao*/, "Permitir alteração no tipo de andamento"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_TIPO_ANDAMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir tipo de andamento"/*nmAcao*/, "Permitir exclusão de tipo de andamento"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_TIPO_ANDAMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			
			/*
			 * TIPO_DOCUMENTO
			 */
			cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Cadastro de Tipo de Documento" /*nmAgrupamento*/,
					"PRC_TIPO_DOCUMENTO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar tipo de documento"/*nmAcao*/, "Permitir o cadastro de tipo de documento"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_TIPO_DOCUMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar tipo de documento"/*nmAcao*/, "Permitir alteração no tipo de documento"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_TIPO_DOCUMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir tipo de documento"/*nmAcao*/, "Permitir exclusão de tipo de documento"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_TIPO_DOCUMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			
			/*
			 * TIPO_PRAZO
			 */
			cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Cadastro de Tipo de Prazo" /*nmAgrupamento*/,
					"PRC_TIPO_PRAZO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar tipo de prazo"/*nmAcao*/, "Permitir o cadastro de tipo de prazo"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_TIPO_PRAZO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar tipo de prazo"/*nmAcao*/, "Permitir alteração no tipo de prazo"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_TIPO_PRAZO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir tipo de prazo"/*nmAcao*/, "Permitir exclusão de tipo de prazo"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_TIPO_PRAZO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			
			/*
			 * TIPO_ACAO
			 */
			cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Cadastro de Tipo de Ação" /*nmAgrupamento*/,
					"PRC_TIPO_ACAO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar tipo de ação"/*nmAcao*/, "Permitir o cadastro de tipo de ação"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_TIPO_ACAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar tipo de ação"/*nmAcao*/, "Permitir alteração no tipo de ação"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_TIPO_ACAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir tipo de ação"/*nmAcao*/, "Permitir exclusão de tipo de ação"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_TIPO_ACAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			
			/*
			 * AREA_DIREITO
			 */
			cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Cadastro de Área do Direito" /*nmAgrupamento*/,
					"PRC_AREA_DIREITO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar Área do Direito"/*nmAcao*/, "Permitir o cadastro de Área do Direito"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_AREA_DIREITO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Área do Direito"/*nmAcao*/, "Permitir alteração no Área do Direito"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_AREA_DIREITO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Área do Direito"/*nmAcao*/, "Permitir exclusão de Área do Direito"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_AREA_DIREITO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			
			/*
			 * TIPO_SITUACAO
			 */
			cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Cadastro de Tipo de Situação" /*nmAgrupamento*/,
					"PRC_TIPO_SITUACAO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar tipo de situação"/*nmAcao*/, "Permitir o cadastro de tipo de situação"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_TIPO_SITUACAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar tipo de situação"/*nmAcao*/, "Permitir alteração no tipo de situação"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_TIPO_SITUACAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir tipo de situação"/*nmAcao*/, "Permitir exclusão de tipo de situação"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_TIPO_SITUACAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			
			/*
			 * TIPO_PEDIDO
			 */
			cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Cadastro de Tipo de Pedido" /*nmAgrupamento*/,
					"PRC_TIPO_PEDIDO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar tipo de pedido"/*nmAcao*/, "Permitir o cadastro de tipo de pedido"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_TIPO_PEDIDO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar tipo de pedido"/*nmAcao*/, "Permitir alteração no tipo de pedido"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_TIPO_PEDIDO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir tipo de pedido"/*nmAcao*/, "Permitir exclusão de tipo de pedido"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_TIPO_PEDIDO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			
			/*
			 * TIPO_OBJETO
			 */
			cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Cadastro de Tipo de Objeto" /*nmAgrupamento*/,
					"PRC_TIPO_OBJETO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar tipo de objeto"/*nmAcao*/, "Permitir o cadastro de tipo de objeto"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_TIPO_OBJETO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar tipo de objeto"/*nmAcao*/, "Permitir alteração no tipo de objeto"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_TIPO_OBJETO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir tipo de objeto"/*nmAcao*/, "Permitir exclusão de tipo de objeto"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_TIPO_OBJETO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			
			/*
			 * GRUPO_TRABALHO
			 */
			cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Cadastro de Grupo de Trabalho" /*nmAgrupamento*/,
					"PRC_GRUPO_TRABALHO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar Grupo de Trabalho"/*nmAcao*/, "Permitir o cadastro de Grupo de Trabalho"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_GRUPO_TRABALHO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Grupo de Trabalho"/*nmAcao*/, "Permitir alteração no Grupo de Trabalho"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_GRUPO_TRABALHO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Grupo de Trabalho"/*nmAcao*/, "Permitir exclusão de Grupo de Trabalho"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_GRUPO_TRABALHO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			
			/*
			 * SISTEMA_PROCESSO
			 */
			cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Cadastro de Sistema de Processos" /*nmAgrupamento*/,
					"PRC_SISTEMA_PROCESSO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar Sistema de Processos"/*nmAcao*/, "Permitir o cadastro de Sistema de Processos"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_SISTEMA_PROCESSO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Sistema de Processos"/*nmAcao*/, "Permitir alteração no Sistema de Processos"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_SISTEMA_PROCESSO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Sistema de Processos"/*nmAcao*/, "Permitir exclusão de Sistema de Processos"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_SISTEMA_PROCESSO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			

			/*
			 * SERVICO RECORTE
			 */
			cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Cadastro de Serviço de Recortes" /*nmAgrupamento*/,
					"PRC_SERVICO_RECORTE"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			cdAcaoSuperior = saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cadastrar Serviço de Recortes"/*nmAcao*/, "Permitir o cadastro de Serviço de Recortes"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.INS_SERVICO_RECORTE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Serviço de Recortes"/*nmAcao*/, "Permitir alteração no Serviço de Recortes"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.UPD_SERVICO_RECORTE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Serviço de Recortes"/*nmAcao*/, "Permitir exclusão de Serviço de Recortes"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.DEL_SERVICO_RECORTE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao(new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Executar Serviço de Recortes"/*nmAcao*/, "Permitir execução de Serviço de Recortes"/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "PRC.RUN_SERVICO_RECORTE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			
			/**
			 * FATURAMENTO
			 */
			//Agrupamento de Faturamento
			int cdAgrupamentoFaturamento = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Faturamento" /*nmAgrupamento*/,
					"JUR_FATURAMENTO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Revisar Faturamento
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Revisar Faturamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFaturamento/*cdAgrupamento*/, "REVISAR_FATURAMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Faturar
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Faturar"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFaturamento/*cdAgrupamento*/, "GERAR_FATURAMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Cancelar Revisão
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cancelar Revisão"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFaturamento/*cdAgrupamento*/, "CANCELAR_REVISAO_FATURAMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Faturamento
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Faturamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFaturamento/*cdAgrupamento*/, "EXCLUIR_FATURAMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 4/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			
			/**
			 * DASHBOARDS
			 */
			//Agrupamento de Dashboards
			int cdAgrupamentoDashboard = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Dashboards" /*nmAgrupamento*/,
					"JUR_DASHBOARD"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Controladoria
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Dashboard da Controladoria"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoDashboard/*cdAgrupamento*/, "VIEW_CONTROLADORIA_DASHBOARD"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
	}
	
	/**
	 * Inicializa as permissões de agenda. OBS: Será utilizada para a estrutura de Agenda/Agendamento e não para a agenda do módulo juridico (Agenda Item)
	 * @param cdSistema
	 * @param cdModulo
	 * @param connect
	 */
	public static void initPermissoesAgd(int cdSistema, int cdModulo, Connection connect){
		try	{
			
			/*
			 * GERAL
			 */
			int cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Geral" /*nmAgrupamento*/,
					"AGD_GERAL"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			//
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Visualizar todas"/*nmAcao*/, "Permitir a visualização da agenda de todos os usuários."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.VIEW_AGENDA_TODOS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Emitir lista"/*nmAcao*/, "Permitir a emissão de lista de agendas."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.VIEW_LISTA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Relação de agendas por dia"/*nmAcao*/, "Permitir a emissão de lista de agendas por dia."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.VIEW_AGENDA_DIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Relação de agendas por semana"/*nmAcao*/, "Permitir a emissão de lista de agendas por semana."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.VIEW_AGENDA_SEMANA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Relação de agendas por comarca"/*nmAcao*/, "Permitir a emissão de lista de agendas por comarca."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.VIEW_AGENDA_COMARCA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 4/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Exportar lista em excel"/*nmAcao*/, "Permitir a emissão de lista de agendas em excel."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.VIEW_AGENDA_EXCEL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 5/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			
			/*
			 * AUDIÊNCIA 
			 */
			cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Audiência" /*nmAgrupamento*/,
													"AGD_AUDIENCIA"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 1/*nrOrdem*/), connect );
			//insert
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Inserir Audiência"/*nmAcao*/, "Permitir o cadastro de novas Audiência."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.INS_AUDIENCIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 6/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			//update
			int cdAcaoUpd = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Audiência"/*nmAcao*/, "Permitir alteração de Audiência."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_AUDIENCIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 7/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar data inicial da Audiência"/*nmAcao*/, "Permitir alteração da data inicial da Audiência."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_AUDIENCIA_DT_INICIAL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 8/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar data final da Audiência"/*nmAcao*/, "Permitir alteração da data final da Audiência."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_AUDIENCIA_DT_FINAL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 9/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar situação da Audiência"/*nmAcao*/, "Permitir alteração da situação do prazo."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_AUDIENCIA_SITUACAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 10/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar detalhe da Audiência"/*nmAcao*/, "Permitir alteração dos detalhes do prazo."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_AUDIENCIA_DETALHE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 11/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar responsável pela Audiência"/*nmAcao*/, "Permitir alteração do responsável pelo prazo."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_AUDIENCIA_RESPONSAVEL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 12/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar data de realização da Audiência"/*nmAcao*/, "Permitir alteração da data de realização do prazo."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_AUDIENCIA_DT_REALIZACAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 13/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cumprir a Audiência"/*nmAcao*/, "Permitir cumprimento da Audiência."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_AUDIENCIA_CUMPRIR"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 14/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cancelar a Audiência"/*nmAcao*/, "Permitir cancelar a Audiência."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_AUDIENCIA_CANCELAR"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 15/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar tipo"/*nmAcao*/, "Permitir alteração do tipo da Audiência."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_AUDIENCIA_TIPO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 16/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			//delete
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Audiência"/*nmAcao*/, "Permitir a exclusão de Audiências."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.DEL_AUDIENCIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 17/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			
			/*
			 * PRAZO
			 */
			cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Prazo" /*nmAgrupamento*/,
													"AGD_PRAZO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 2/*nrOrdem*/), connect );
			//insert
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Inserir Prazo"/*nmAcao*/, "Permitir o cadastro de novos prazos."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.INS_PRAZO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 18/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			//update
			cdAcaoUpd = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Prazo"/*nmAcao*/, "Permitir alteração de prazos."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_PRAZO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 19/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar data inicial do Prazo"/*nmAcao*/, "Permitir alteração da data inicial do Prazo."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_PRAZO_DT_INICIAL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 20/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar data final do Prazo"/*nmAcao*/, "Permitir alteração da data final do prazo."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_PRAZO_DT_FINAL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 21/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar situação do Prazo"/*nmAcao*/, "Permitir alteração da situação do prazo."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_PRAZO_SITUACAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 21/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar detalhe do Prazo"/*nmAcao*/, "Permitir alteração dos detalhes do prazo."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_PRAZO_DETALHE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 22/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar responsável pelo Prazo"/*nmAcao*/, "Permitir alteração do responsável pelo prazo."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_PRAZO_RESPONSAVEL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 23/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar data de realização do Prazo"/*nmAcao*/, "Permitir alteração da data de realização do prazo."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_PRAZO_DT_REALIZACAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 24/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cumprir o Prazo"/*nmAcao*/, "Permitir cumprimento do prazo."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_PRAZO_CUMPRIR"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 25/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cancelar Prazo"/*nmAcao*/, "Permitir cancelar o prazo."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_PRAZO_CANCELAR"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 26/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar tipo"/*nmAcao*/, "Permitir alteração do tipo do Prazo."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_PRAZO_TIPO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 15/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			//delete
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Prazo"/*nmAcao*/, "Permitir a exclusão de prazos."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.DEL_PRAZO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 27/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			
			/*
			 * TAREFA
			 */
			cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Tarefa" /*nmAgrupamento*/,
													"AGD_TAREFA"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 3/*nrOrdem*/), connect );
			//insert
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Inserir Tarefa"/*nmAcao*/, "Permitir o cadastro de novas tarefas."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.INS_TAREFA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 28/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			//update
			cdAcaoUpd = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Tarefa"/*nmAcao*/, "Permitir alteração de Tarefa."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_TAREFA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 29/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar data inicial da Tarefa"/*nmAcao*/, "Permitir alteração da data inicial de Tarefa."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_TAREFA_DT_INICIAL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 30/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar data final da Tarefa"/*nmAcao*/, "Permitir alteração da data final de Tarefa."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_TAREFA_DT_FINAL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 31/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar situação da Tarefa"/*nmAcao*/, "Permitir alteração da situação de Tarefa."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_TAREFA_SITUACAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 32/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar detalhe da Tarefa"/*nmAcao*/, "Permitir alteração dos detalhes de Tarefa."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_TAREFA_DETALHE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 33/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar responsável pela Tarefa"/*nmAcao*/, "Permitir alteração do responsável pela Tarefa."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_TAREFA_RESPONSAVEL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 34/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar data de realização da Tarefa"/*nmAcao*/, "Permitir alteração da data de realização de Tarefa."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_TAREFA_DT_REALIZACAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 35/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cumprir a Tarefa"/*nmAcao*/, "Permitir cumprimento de Tarefa."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_TAREFA_CUMPRIR"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 36/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cancelar a Tarefa"/*nmAcao*/, "Permitir cancelamento da Tarefa."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_TAREFA_CANCELAR"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 37/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar tipo"/*nmAcao*/, "Permitir alteração do tipo da Tarefa."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_TAREFA_TIPO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 15/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			//delete
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Tarefa"/*nmAcao*/, "Permitir a exclusão de Tarefa."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.DEL_TAREFA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 38/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
						
			/*
			 * DILIGENCIA
			 */
			cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Diligência" /*nmAgrupamento*/,
													"AGD_DILIGENCIA"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 4/*nrOrdem*/), connect );
			//insert
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Inserir Diligência"/*nmAcao*/, "Permitir o cadastro de novas tarefas."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.INS_DILIGENCIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 38/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			//update
			cdAcaoUpd = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Diligência"/*nmAcao*/, "Permitir alteração de Diligência."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_DILIGENCIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 39/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar data inicial da Diligência"/*nmAcao*/, "Permitir alteração da data inicial de Diligência."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_DILIGENCIA_DT_INICIAL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 40/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar data final da Diligência"/*nmAcao*/, "Permitir alteração da data final de Diligência."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_DILIGENCIA_DT_FINAL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 41/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar situação da Diligência"/*nmAcao*/, "Permitir alteração da situação de Diligência."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_DILIGENCIA_SITUACAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 42/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar detalhe da Diligência"/*nmAcao*/, "Permitir alteração dos detalhes de Diligência."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_DILIGENCIA_DETALHE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 43/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar responsável pela Diligência"/*nmAcao*/, "Permitir alteração do responsável pela Diligência."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_DILIGENCIA_RESPONSAVEL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 44/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar data de realização da Diligência"/*nmAcao*/, "Permitir alteração da data de realização de Diligência."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_DILIGENCIA_DT_REALIZACAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 45/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cumprir a Diligência"/*nmAcao*/, "Permitir cumprimento de Diligência."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_DILIGENCIA_CUMPRIR"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 46/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cancelar a Diligência"/*nmAcao*/, "Permitir cancelamento da Diligência."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_DILIGENCIA_CANCELAR"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 47/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Valor do Serviço"/*nmAcao*/, "Permitir alteração no valor do serviços da Diligência."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_DILIGENCIA_VL_SERVICO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 48/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar tipo"/*nmAcao*/, "Permitir alteração do tipo da Diligência."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.UPD_DILIGENCIA_TIPO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 15/*nrOrdem*/, cdAcaoUpd/*cdAcaoSuperior*/), connect);
			//delete
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Diligência"/*nmAcao*/, "Permitir a exclusão de Diligência."/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "AGD.DEL_DILIGENCIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 49/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
						
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
	}
	
	public static void initPermissoesPtc(int cdSistema, int cdModulo, Connection connect){
		try	{
			
			//Sincronização E&L
			int cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"PTC - Protocolo" /*nmAgrupamento*/,
													"PTC_SINCRONIZACAO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Sincronização protocolo E&L
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Sincronização protocolo E&L"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "SINC_PROTOCOLO_EEL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			
			/**
			 * PROTOCOLO
			 */
			cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"PTC - Principal" /*nmAgrupamento*/,
					"PTC_DOCUMENTO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
					
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Incluir: Documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "INS_DOCUMENTO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir: Documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "DEL_DOCUMENTO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar: Documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "UPD_DOCUMENTO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Enviar Documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "ENV_DOCUMENTO"/*idAcao*/, AcaoServices.DESTACADA/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Receber Documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "REC_DOCUMENTO"/*idAcao*/, AcaoServices.DESTACADA/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Arquivar Documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "ARQ_DOCUMENTO"/*idAcao*/, AcaoServices.DESTACADA/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Vincular Documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "VIN_DOCUMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Detalhes"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "UPD_DETALHE"/*idAcao*/, AcaoServices.DESTACADA/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Anexar Documentação"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "ANX_DOCUMENTACAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Anexar Arquivos"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "ANX_ARQUIVO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Incluir Prazo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "INS_PRAZO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Prazo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "UPD_PRAZO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Prazo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "DEL_PRAZO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Incluir Ocorrência"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "INS_OCORRENCIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Ocorrência"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "UPD_OCORRENCIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Ocorrência"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "DEL_OCORRENCIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Incluir Pendência"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "INS_PENDENCIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Pendência"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "UPD_PENDENCIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Pendência"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "DEL_PENDENCIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Gerar relatório"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "GER_RELATORIO"/*idAcao*/, AcaoServices.DESTACADA/*tpOrganizacao*/, 4/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Abrir Documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "ABR_DOCUMENTO"/*idAcao*/, AcaoServices.DESTACADA/*tpOrganizacao*/, 5/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Inserir fluxo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "INS_FLUXO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar fluxo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "UPD_FLUXO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Fluxo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "DEL_FLUXO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Inserir fase"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "INS_FASE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar fase"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "UPD_FASE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Fase"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "DEL_FASE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Inserir tipo documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "INS_TIPO_DOCUMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar tipo documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "UPD_TIPO_DOCUMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir tipo documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "DEL_TIPO_DOCUMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Inserir tipo anexo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "INS_TIPO_ANEXO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar tipo anexo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "UPD_TIPO_ANEXO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir tipo anexo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "DEL_TIPO_ANEXO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Inserir situação documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "INS_SITUACAO_DOCUMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar situação documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "UPD_SITUACAO_DOCUMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir situação documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "DEL_SITUACAO_DOCUMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Inserir tipo ocorrência"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "INS_TIPO_OCORRENCIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar tipo ocorrência"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "UPD_TIPO_OCORRENCIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir tipo ocorrência"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "DEL_TIPO_OCORRENCIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Inserir tipo pendência"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "INS_TIPO_PENDENCIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar tipo pendência"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "UPD_TIPO_PENDENCIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir tipo pendência"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "DEL_TIPO_PENDENCIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Inserir tipo prazo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "INS_TIPO_PRAZO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar tipo prazo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "UPD_TIPO_PRAZO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir tipo prazo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "DEL_TIPO_PRAZO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Inserir setor"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "INS_SETOR"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar setor"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "UPD_SETOR"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir setor"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "DEL_SETOR"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar número, assunto e data"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "UPD_AVANCADO"/*idAcao*/, AcaoServices.DESTACADA/*tpOrganizacao*/, 6/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Painel Adm. de Documentos"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "ACESSO_PAINEL_PROTOCOLO"/*idAcao*/, AcaoServices.DESTACADA/*tpOrganizacao*/, 7/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
	}
	
	public static void initPermissoesAcd(int cdSistema, int cdModulo, Connection connect){
		try	{
			connect.setAutoCommit(false);
						
			/**
			 * INSTITUICAO
			 */
			//Agrupamento de Instituicao
			int cdAgrupamentoInstituicao = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"EDF - Instituição" /*nmAgrupamento*/,
					"EDF_INSTITUICAO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Instituicao
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Instituição"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoInstituicao/*cdAgrupamento*/, "INS_INSTITUICAO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Instituicao
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Instituição"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoInstituicao/*cdAgrupamento*/, "UPD_INSTITUICAO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Instituicao
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Instituição"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoInstituicao/*cdAgrupamento*/, "DEL_INSTITUICAO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Gerar Formulario Dinamico
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Gerar Formulário Dinâmico para Insituição"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoInstituicao/*cdAgrupamento*/, "GERAR_FORMULARIO_INSTITUICAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * CIRCULOS
			 */
			//Agrupamento de Circulo
			int cdAgrupamentoCirculo = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"EDF - Circulo" /*nmAgrupamento*/,
					"EDF_CIRCULO"/*idAgrupamento*/, ""/*dsAgrupamento*/, cdAgrupamentoInstituicao/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Circulo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Círculo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCirculo/*cdAgrupamento*/, "INS_CIRCULO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Circulo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Círculo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCirculo/*cdAgrupamento*/, "UPD_CIRCULO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Circulo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Círculo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCirculo/*cdAgrupamento*/, "DEL_CIRCULO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * CURSOS/ MODALIDADE
			 */
			//Agrupamento de Curso
			int cdAgrupamentoCurso = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"EDF - Curso/Modalidade" /*nmAgrupamento*/,
					"EDF_CURSO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Curso
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Curso/Modalidade"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCurso/*cdAgrupamento*/, "INS_CURSO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Curso
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Curso/Modalidade"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCurso/*cdAgrupamento*/, "UPD_CURSO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Curso
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Curso/Modalidade"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCurso/*cdAgrupamento*/, "DEL_CURSO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * ETAPA
			 */
			//Agrupamento de Etapa
			int cdAgrupamentoEtapa = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"EDF - Etapa" /*nmAgrupamento*/,
					"EDF_ETAPA"/*idAgrupamento*/, ""/*dsAgrupamento*/, cdAgrupamentoCurso/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Etapa
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Etapa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoEtapa/*cdAgrupamento*/, "INS_ETAPA"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Etapa
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Etapa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoEtapa/*cdAgrupamento*/, "UPD_ETAPA"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Etapa
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Etapa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoEtapa/*cdAgrupamento*/, "DEL_ETAPA"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * PERIODO LETIVO
			 */
			//Agrupamento de Periodo Letivo
			int cdAgrupamentoPeriodoLetivo = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"EDF - Período Letivo" /*nmAgrupamento*/,
					"EDF_PERIODO_LETIVO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Periodo Letivo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Período Letivo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPeriodoLetivo/*cdAgrupamento*/, "INS_PERIODO_LETIVO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Periodo Letivo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Período Letivo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPeriodoLetivo/*cdAgrupamento*/, "UPD_PERIODO_LETIVO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Periodo Letivo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Período Letivo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPeriodoLetivo/*cdAgrupamento*/, "DEL_PERIODO_LETIVO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * ALUNO
			 */
			//Agrupamento de Aluno
			int cdAgrupamentoAluno = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"EDF - Aluno" /*nmAgrupamento*/,
					"EDF_ALUNO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Aluno
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Aluno"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoAluno/*cdAgrupamento*/, "INS_ALUNO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Aluno
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Aluno"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoAluno/*cdAgrupamento*/, "UPD_ALUNO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Aluno
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Aluno"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoAluno/*cdAgrupamento*/, "DEL_ALUNO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * MATRICULA
			 */
			//Agrupamento de Matricula
			int cdAgrupamentoMatricula = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"EDF - Matrícula" /*nmAgrupamento*/,
					"EDF_MATRICULA"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Matricula
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Matrícula"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMatricula/*cdAgrupamento*/, "INS_MATRICULA"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Matricula
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Matrícula"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMatricula/*cdAgrupamento*/, "UPD_MATRICULA"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Matricula
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Matrícula"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMatricula/*cdAgrupamento*/, "DEL_MATRICULA"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * TURMA
			 */
			//Agrupamento de Turma
			int cdAgrupamentoTurma = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"EDF - Turma" /*nmAgrupamento*/,
					"EDF_TURMA"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Turma
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Turma"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTurma/*cdAgrupamento*/, "INS_TURMA"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Turma
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Turma"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTurma/*cdAgrupamento*/, "UPD_TURMA"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Turma
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Turma"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTurma/*cdAgrupamento*/, "DEL_TURMA"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * DISCIPLINA
			 */
			//Agrupamento de Disciplina
			int cdAgrupamentoDisciplina = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"EDF - Disciplina" /*nmAgrupamento*/,
					"EDF_PROFESSOR"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Disciplina
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Disciplina"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoDisciplina/*cdAgrupamento*/, "INS_DISCIPLINA"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Disciplina
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Disciplina"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoDisciplina/*cdAgrupamento*/, "UPD_DISCIPLINA"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Disciplina
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Disciplina"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoDisciplina/*cdAgrupamento*/, "DEL_DISCIPLINA"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * AREA DE CONHECIMENTO
			 */
			//Agrupamento de Disciplina
			int cdAgrupamentoAreaConhecimento = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"EDF - Área de Conhecimento" /*nmAgrupamento*/,
					"EDF_AREA_CONHECIMENTO"/*idAgrupamento*/, ""/*dsAgrupamento*/, cdAgrupamentoDisciplina/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Area de Conhecimento
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Área de Conhecimento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoAreaConhecimento/*cdAgrupamento*/, "INS_AREA_CONHECIMENTO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Area de Conhecimento
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Área de Conhecimento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoAreaConhecimento/*cdAgrupamento*/, "UPD_AREA_CONHECIMENTO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Area de Conhecimento
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Área de Conhecimento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoAreaConhecimento/*cdAgrupamento*/, "DEL_AREA_CONHECIMENTO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * AREA DE INTERESSE
			 */
			//Agrupamento de Area de Interesse
			int cdAgrupamentoAreaInteresse = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"EDF - Área de Interesse" /*nmAgrupamento*/,
					"EDF_AREA_INTERESSE"/*idAgrupamento*/, ""/*dsAgrupamento*/, cdAgrupamentoDisciplina/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Area de Interesse
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Área de Interesse"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoAreaInteresse/*cdAgrupamento*/, "INS_AREA_INTERESSE"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Area de Interesse
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Área de Interesse"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoAreaInteresse/*cdAgrupamento*/, "UPD_AREA_INTERESSE"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Area de Interesse
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Área de Interesse"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoAreaInteresse/*cdAgrupamento*/, "DEL_AREA_INTERESSE"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * CLASSE
			 */
			//Agrupamento de Classe
			int cdAgrupamentoClasse = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"EDF - Classe" /*nmAgrupamento*/,
					"EDF_CLASSE"/*idAgrupamento*/, ""/*dsAgrupamento*/, cdAgrupamentoDisciplina/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Classe
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Classe"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoClasse/*cdAgrupamento*/, "INS_CLASSE"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Classe
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Classe"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoClasse/*cdAgrupamento*/, "UPD_CLASSE"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Classe
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Classe"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoClasse/*cdAgrupamento*/, "DEL_CLASSE"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * AULA
			 */
			//Agrupamento de Aula
			int cdAgrupamentoAula = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"EDF - Aula" /*nmAgrupamento*/,
					"EDF_AULA"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Aula
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Aula"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoAula/*cdAgrupamento*/, "INS_AULA"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Aula
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Aula"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoAula/*cdAgrupamento*/, "UPD_AULA"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Aula
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Aula"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoAula/*cdAgrupamento*/, "DEL_AULA"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			connect.commit();
			connect.setAutoCommit(true);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
	}
	
	public static void initPermissoesCae(int cdSistema, int cdModulo, Connection connect){
		try	{
			connect.setAutoCommit(false);
			
			/**
			 * CARDAPIO
			 */
			//Agrupamento de Cardapio
			int cdAgrupamentoCardapio = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"CAE - Cardápio" /*nmAgrupamento*/,
					"CAE_CARDAPIO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Cardapio
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Cardápio"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCardapio/*cdAgrupamento*/, "CAE.INS_CARDAPIO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Cardapio
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Cardápio"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCardapio/*cdAgrupamento*/, "CAE.UPD_CARDAPIO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Cardapio
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Cardápio"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCardapio/*cdAgrupamento*/, "CAE.DEL_CARDAPIO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			//Inserir Refeicao
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Refeição"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCardapio/*cdAgrupamento*/, "CAE.INS_REFEICAO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 4/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Refeicao
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Refeição"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCardapio/*cdAgrupamento*/, "CAE.DEL_REFEICAO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 5/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * PREPARACAO
			 */
			//Agrupamento de Preparacao
			int cdAgrupamentoPreparacao = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"CAE - Preparação" /*nmAgrupamento*/,
					"CAE_PREPARACAO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Preparacao
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Preparação"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPreparacao/*cdAgrupamento*/, "CAE.INS_PREPARACAO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Preparacao
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Preparação"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPreparacao/*cdAgrupamento*/, "CAE.UPD_PREPARACAO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Preparacao
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Preparação"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPreparacao/*cdAgrupamento*/, "CAE.DEL_PREPARACAO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * INGREDIENTE
			 */
			//Agrupamento de Ingrediente
			int cdAgrupamentoIngrediente = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"CAE - Ingrediente" /*nmAgrupamento*/,
					"CAE_INGREDIENTE"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Ingrediente
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Ingrediente"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoIngrediente/*cdAgrupamento*/, "CAE.INS_INGREDIENTE"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Ingrediente
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Ingrediente"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoIngrediente/*cdAgrupamento*/, "CAE.UPD_INGREDIENTE"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Ingrediente
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Ingrediente"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoIngrediente/*cdAgrupamento*/, "CAE.DEL_INGREDIENTE"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * GRUPO DE INGREDIENTE
			 */
			//Agrupamento de Grupo de Ingrediente
			int cdAgrupamentoGrupoIngrediente = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"CAE - Grupo de Ingrediente" /*nmAgrupamento*/,
					"CAE_GRUPO_INGREDIENTE"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Grupo de Ingrediente
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Grupo de Ingrediente"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoGrupoIngrediente/*cdAgrupamento*/, "CAE.INS_GRUPO_INGREDIENTE"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Grupo de Ingrediente
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Grupo de Ingrediente"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoGrupoIngrediente/*cdAgrupamento*/, "CAE.UPD_GRUPO_INGREDIENTE"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Grupo de Ingrediente
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Grupo de Ingrediente"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoGrupoIngrediente/*cdAgrupamento*/, "CAE.DEL_GRUPO_INGREDIENTE"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * MODALIDADE
			 */
			//Agrupamento de Modalidade
			int cdAgrupamentoModalidade = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"CAE - Modalidade" /*nmAgrupamento*/,
					"CAE_MODALIDADE"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Modalidade
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Modalidade"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoModalidade/*cdAgrupamento*/, "CAE.INS_MODALIDADE"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Modalidade
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Modalidade"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoModalidade/*cdAgrupamento*/, "CAE.UPD_MODALIDADE"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Modalidade
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Modalidade"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoModalidade/*cdAgrupamento*/, "CAE.DEL_MODALIDADE"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * RECOMENDACAO NUTRICIONAL
			 */
			//Agrupamento de Recomendacao Nutricional
			int cdAgrupamentoRecomendacaoNutricional = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"CAE - Recomendação Nutricional" /*nmAgrupamento*/,
					"CAE_RECOMENDACAO_NUTRICIONAL"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Recomendacao Nutricional
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Recomendação Nutricional"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRecomendacaoNutricional/*cdAgrupamento*/, "CAE.INS_RECOMENDACAO_NUTRICIONAL"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Recomendacao Nutricional
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Recomendação Nutricional"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRecomendacaoNutricional/*cdAgrupamento*/, "CAE.UPD_RECOMENDACAO_NUTRICIONAL"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Recomendacao Nutricional
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Recomendação Nutricional"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRecomendacaoNutricional/*cdAgrupamento*/, "CAE.DEL_RECOMENDACAO_NUTRICIONAL"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * BALANCO
			 */
			//Agrupamento de Balanco
			int cdAgrupamentoBalanco = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"CAE - Balanço" /*nmAgrupamento*/,
					"CAE_BALANCO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Balanco
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Balanço"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoBalanco/*cdAgrupamento*/, "CAE.INS_BALANCO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Balanco
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Balanço"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoBalanco/*cdAgrupamento*/, "CAE.UPD_BALANCO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Balanco
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Balanço"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoBalanco/*cdAgrupamento*/, "CAE.DEL_BALANCO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Ajustar Estoque
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Ajustar Estoque"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoBalanco/*cdAgrupamento*/, "CAE.AJUSTAR_ESTOQUE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 4/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * PLANO DE ENTREGA (RomaneioForm)
			 */
			//Agrupamento de entrega
			int cdAgrupamentoPlanoEntega = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"CAE - Plano de Entrega" /*nmAgrupamento*/,
					"CAE_PLANO_ENTREGA"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Entrega
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Plano"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPlanoEntega/*cdAgrupamento*/, "CAE.INS_PLANO_ENTREGA"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Entrega
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Plano"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPlanoEntega/*cdAgrupamento*/, "CAE.UPD_PLANO_ENTREGA"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Entrega
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Plano"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPlanoEntega/*cdAgrupamento*/, "CAE.DEL_PLANO_ENTREGA"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Executar Plano
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Executar Plano"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPlanoEntega/*cdAgrupamento*/, "CAE.EXECUTAR_PLANO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 4/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Retorno
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Lançar Retorno"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPlanoEntega/*cdAgrupamento*/, "CAE.LANCAR_RETORNO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 5/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Imprimir Guias
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir Guias"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPlanoEntega/*cdAgrupamento*/, "CAE.IMPRIMIR_GUIAS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 6/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * CONTRATO DE COMPRA
			 */
			//Agrupamento de Contrato de Compra
			int cdAgrupamentoContratoCompra = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"CAE - Contrato de Compra" /*nmAgrupamento*/,
					"CAE_CONTRATO_COMPRA"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Inserir Contrato
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Contrato"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContratoCompra/*cdAgrupamento*/, "CAE.INS_CONTRATO_COMPRA"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Contrato
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Contrato"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContratoCompra/*cdAgrupamento*/, "CAE.UPD_CONTRATO_COMPRA"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Contrato
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Contrato"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContratoCompra/*cdAgrupamento*/, "CAE.DEL_CONTRATO_COMPRA"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Cancelar Contrato
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cancelar Contrato"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContratoCompra/*cdAgrupamento*/, "CAE.CANCELAR_CONTRATO_COMPRA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 4/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Liberar
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Liberar"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContratoCompra/*cdAgrupamento*/, "CAE.LIBERAR_CONTRATO_COMPRA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 5/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Renovar
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Renovar"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContratoCompra/*cdAgrupamento*/, "CAE.RENOVAR_CONTRATO_COMPRA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 6/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			connect.commit();
			connect.setAutoCommit(true);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
	}
	
	public static void initPermissoesAdm(int cdSistema, int cdModulo, Connection connect){
		try	{
			connect.setAutoCommit(false);
			//Agrupamento principal - FIN_PRINCIPAL
			int cdAgrupamentoPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"FIN - Principal" /*nmAgrupamento*/,
													"FIN_PRINCIPAL"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			//Acesso a aba Financeiro
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a aba Financeiro"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "ACESSO_ABA_FINANCEIRO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso a aba Tabelas
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a aba Tabelas"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "ACESSO_ABA_TABELAS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso a aba Utilitários
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a aba Utilitários"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "ACESSO_ABA_UTILITARIOS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso a aba Utilitários
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a aba Relatórios"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "ACESSO_ABA_RELATORIOS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 4/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso a aba Receitas
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Gráfico de Receitas"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "ACESSO_ABA_POD_RECEITAS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 5/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso a aba Despesas
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Gráfico de Despesas"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "ACESSO_ABA_POD_DESPESAS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 6/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso a aba Saldo em Conta
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a aba Saldo em Contas"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "ACESSO_ABA_POD_SALDO_CONTAS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 7/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso a aba Quadro Resumo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a aba Quadro Resumo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "ACESSO_ABA_POD_QUADRO_RESUMO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 8/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso a aba Contas a Pagar
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a aba Contas a Pagar na tela inicial"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "ACESSO_ABA_POD_CONTAS_PAGAR"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 9/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso a aba Contas a Receber
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a aba Contas a Receber na tela inicial"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "ACESSO_ABA_POD_CONTAS_RECEBER"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 10/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso a aba Últimas Recebidas
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a aba Últimas Contas Recebidas na tela inicial"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "ACESSO_ABA_POD_ULTIMAS_RECEBIDAS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 11/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso a aba Últimas Pagas
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a aba Últimas Contas Pagas na tela inicial"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPrincipal/*cdAgrupamento*/, "ACESSO_ABA_POD_ULTIMAS_PAGAS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 12/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			//Agrupamento aba Financeiro - FIN_ABA_FINANCEIRO
			int cdAgrupamentoFinanceiro = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"FIN - Aba Financeiro" /*nmAgrupamento*/,
					"FIN_ABA_FINANCEIRO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			
			//Acesso Grupo Contas
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Grupo Contas"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFinanceiro/*cdAgrupamento*/, "ACESSO_GRUPO_CONTAS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso Grupo Lançamentos
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Grupo Lançamentos"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFinanceiro/*cdAgrupamento*/, "ACESSO_GRUPO_LANCAMENTOS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso Grupo Cadastros
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Grupo Cadastros"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFinanceiro/*cdAgrupamento*/, "ACESSO_GRUPO_CADASTROS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso Conta Receber
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Contas a Receber"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFinanceiro/*cdAgrupamento*/, "ACESSO_CONTA_RECEBER"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 4/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso Conta Pagar
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Contas a Pagar"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFinanceiro/*cdAgrupamento*/, "ACESSO_CONTA_PAGAR"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 5/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso Pagamento Avulso
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Pagamento Avulso"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFinanceiro/*cdAgrupamento*/, "ACESSO_PAGAMENTO_AVULSO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 6/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso Custódia
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Custódia"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFinanceiro/*cdAgrupamento*/, "ACESSO_CUSTODIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 7/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso Movimentação de Conta
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Movimentação de Contas"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFinanceiro/*cdAgrupamento*/, "ACESSO_MOVIMENTACAO_CONTA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 8/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso Gerenciamento Fechamento de Caixa
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Gerenciamento de Fechamentos de Caixa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFinanceiro/*cdAgrupamento*/, "ACESSO_GERENCIAMENTO_FECHAMENTO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 9/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso Fechamento de Caixa
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Fechamento de Caixa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFinanceiro/*cdAgrupamento*/, "ACESSO_FECHAMENTO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 10/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso Cadastro Cliente
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Cadastro de Cliente"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFinanceiro/*cdAgrupamento*/, "ACESSO_CADASTRO_CLIENTE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 11/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso Cadastro Favorecido
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Cadastro de Favorecido"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFinanceiro/*cdAgrupamento*/, "ACESSO_CADASTRO_FAVORECIDO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 12/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso Cadastro Geral
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Cadastro Geral"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFinanceiro/*cdAgrupamento*/, "ACESSO_CADASTRO_GERAL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 13/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso Cadastro de Agências
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Agências"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFinanceiro/*cdAgrupamento*/, "ACESSO_AGENCIAS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 14/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso Gerenciamento de Contas Financeiras
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Contas Financeira"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFinanceiro/*cdAgrupamento*/, "ACESSO_CONTA_FINANCEIRA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 15/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso Categorias Econômicas
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Categoria Econômica"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFinanceiro/*cdAgrupamento*/, "ACESSO_CATEGORIA_ECONOMICA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 16/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso Centro de Custo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Centro de Custo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFinanceiro/*cdAgrupamento*/, "ACESSO_CENTRO_CUSTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 17/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			
			//Agrupamento aba Tabelas - FIN_ABA_TABELAS
			int cdAgrupamentoTabelas = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"FIN - Aba Tabelas" /*nmAgrupamento*/,
					"FIN_ABA_TABELAS"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			//Acesso Grupo Endereçamento
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Grupo Endereçamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTabelas/*cdAgrupamento*/, "ACESSO_GRUPO_ENDERECAMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso Grupo Cadastro Geral
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Grupo Cadastro Geral"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTabelas/*cdAgrupamento*/, "ACESSO_GRUPO_GERAL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso Grupo Tabelas
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Grupo Tabelas"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTabelas/*cdAgrupamento*/, "ACESSO_GRUPO_TABELAS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			//Agrupamento aba Utilitários - FIN_ABA_UTILITARIOS
			int cdAgrupamentoUtilitarios = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"FIN - Aba Utilitários" /*nmAgrupamento*/,
					"FIN_ABA_UTILITARIOS"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			//Acesso Grupo Utilitarios
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Grupo Utilitários"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoUtilitarios/*cdAgrupamento*/, "ACESSO_GRUPO_UTILITARIOS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso a Controle de Cheques
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Controle de Cheques"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoUtilitarios/*cdAgrupamento*/, "ACESSO_CONTROLE_CHEQUES"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso a Controle de Boletos
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Controle de Boletos"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoUtilitarios/*cdAgrupamento*/, "ACESSO_CONTROLE_BOLETOS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso a Controle de Recebíveis
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Controle de Recebíveis"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoUtilitarios/*cdAgrupamento*/, "ACESSO_CONTROLE_RECEBIVEIS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso a Agenda de Pagamentos
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Agenda de Pagamentos"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoUtilitarios/*cdAgrupamento*/, "ACESSO_AGENDA_PAGAMENTOS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 4/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso a Agenda de Recebimentos
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Agenda de Recebimentos"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoUtilitarios/*cdAgrupamento*/, "ACESSO_AGENDA_RECEBIMENTOS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 5/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso a Título de Crédito
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Título de Crédito"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoUtilitarios/*cdAgrupamento*/, "ACESSO_TITULO_CREDITO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 6/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			//Agrupamento aba Relatórios - FIN_ABA_RELATORIOS
			int cdAgrupamentoRelatorios = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"FIN - Aba Relatórios" /*nmAgrupamento*/,
					"FIN_ABA_RELATORIOS"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			//Acesso a Grupo Pagamento-Recebimento
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Grupo Pagamento/Recebimentos"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRelatorios/*cdAgrupamento*/, "ACESSO_GRUPO_PAGAMENTO_RECEBIMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso a Grupo Gerenciais
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Grupo Gerenciais"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRelatorios/*cdAgrupamento*/, "ACESSO_GRUPO_GERENCIAIS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso a Grupo Outros Relatórios
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Grupo Outros Relatórios"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRelatorios/*cdAgrupamento*/, "ACESSO_GRUPO_OUTROS_RELATORIOS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso ao Relatórios de Pagamentos
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Relatórios de Pagamentos"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRelatorios/*cdAgrupamento*/, "ACESSO_REL_PAGAMENTOS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 4/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso ao Relatórios de Contas a Pagar
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Relatórios de Contas a Pagar"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRelatorios/*cdAgrupamento*/, "ACESSO_REL_CONTAS_PAGAR"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 5/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso ao Relatórios de Recebimentos
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Relatórios de Recebimentos"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRelatorios/*cdAgrupamento*/, "ACESSO_REL_RECEBIMENTOS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 6/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso ao Relatórios de Contas a Receber
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Relatórios de Contas a Receber"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRelatorios/*cdAgrupamento*/, "ACESSO_REL_CONTAS_RECEBER"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 7/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso ao Relatórios de Movimentações
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Relatórios de Movimentações"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRelatorios/*cdAgrupamento*/, "ACESSO_REL_MOVIMENTACOES"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 8/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso ao Relatórios DRE
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Relatórios DRE"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRelatorios/*cdAgrupamento*/, "ACESSO_REL_DRE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 9/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso ao Relatórios de Fluxo de Caixa
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Relatórios de Fluxo de Caixa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRelatorios/*cdAgrupamento*/, "ACESSO_REL_FLUXO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 10/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso ao Relatórios de Previsão de Fluxo de Caixa
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Relatórios de Previsão de Fluxo de Caixa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRelatorios/*cdAgrupamento*/, "ACESSO_REL_PREVISAO_FLUXO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 11/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Acesso ao Relatórios de Previsão de Contas
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao Relatórios de Previsão de Contas"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRelatorios/*cdAgrupamento*/, "ACESSO_REL_CONTAS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 12/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * CONTA A RECEBER
			 */
			//Agrupamento de Contas a Receber
			int cdAgrupamentoContaReceber = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"FIN - Contas a Receber" /*nmAgrupamento*/,
					"FIN_CONTA_RECEBER"/*idAgrupamento*/, ""/*dsAgrupamento*/, cdAgrupamentoFinanceiro/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			//Inserir Conta Receber
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Conta Receber"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaReceber/*cdAgrupamento*/, "INS_CONTA_RECEBER"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Conta Receber
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Conta Receber"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaReceber/*cdAgrupamento*/, "UPD_CONTA_RECEBER"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Conta Receber
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Conta Receber"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaReceber/*cdAgrupamento*/, "DEL_CONTA_RECEBER"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Cancelar Conta Receber
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cancelar Conta Receber"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaReceber/*cdAgrupamento*/, "CANCELAR_CONTA_RECEBER"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Gerar Fatura
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Gerar Fatura de Conta Receber"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaReceber/*cdAgrupamento*/, "GERAR_FATURA_CONTA_RECEBER"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );

			//Imprimir Boleto
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir Boleto"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaReceber/*cdAgrupamento*/, "IMPRIMIR_CONTA_RECEBER_BOLETO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 4/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Imprimir Bloqueto
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir Bloqueto"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaReceber/*cdAgrupamento*/, "IMPRIMIR_CONTA_RECEBER_BLOQUETO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 5/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Inserir Recebimento
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Recebimento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaReceber/*cdAgrupamento*/, "INS_RECEBIMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 6/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Recebimento
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Recebimento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaReceber/*cdAgrupamento*/, "DEL_RECEBIMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 6/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Imprimir Recibo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir Recibo de Recebimento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaReceber/*cdAgrupamento*/, "IMPRIMIR_CONTA_RECEBER_RECIBO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 7/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Estornar Recebimento
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Estornar Recebimento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaReceber/*cdAgrupamento*/, "ESTORNAR_RECEBIMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 7/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Permutar Recebimento
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Permutar Recebimento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaReceber/*cdAgrupamento*/, "PERMUTAR_RECEBIMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 7/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Inserir Arquivo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Arquivo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaReceber/*cdAgrupamento*/, "INS_CONTA_RECEBER_ARQUIVO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 7/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Atualizar Arquivo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Arquivo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaReceber/*cdAgrupamento*/, "UPD_CONTA_RECEBER_ARQUIVO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 7/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Arquivo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Arquivo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaReceber/*cdAgrupamento*/, "DEL_CONTA_RECEBER_ARQUIVO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 7/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Visualizar Arquivo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Visualizar Arquivo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaReceber/*cdAgrupamento*/, "VISUALIZAR_CONTA_RECEBER_ARQUIVO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 7/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//download Arquivo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Download Arquivo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaReceber/*cdAgrupamento*/, "DOWNLOAD_CONTA_RECEBER_ARQUIVO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 7/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );

			//Refaturar conta a receber
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Solicitar refaturamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaReceber/*cdAgrupamento*/, "ADM.REFATURAR_CONTA_RECEBER"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 7/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * CONTAS A PAGAR
			 */
			//Agrupamento de Contas a Receber
			int cdAgrupamentoContaPagar = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"FIN - Contas a Pagar" /*nmAgrupamento*/,
					"FIN_CONTA_PAGAR"/*idAgrupamento*/, ""/*dsAgrupamento*/, cdAgrupamentoFinanceiro/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			//Inserir Conta Pagar
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Conta Pagar"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaPagar/*cdAgrupamento*/, "INS_CONTA_PAGAR"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Conta Pagar
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Conta Pagar"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaPagar/*cdAgrupamento*/, "UPD_CONTA_PAGAR"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Conta Pagar
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Conta Pagar"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaPagar/*cdAgrupamento*/, "DEL_CONTA_PAGAR"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Cancelar Conta Pagar
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cancelar Conta Pagar"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaPagar/*cdAgrupamento*/, "CANCELAR_CONTA_PAGAR"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 4/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Autorizar Pagamento
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Autorizar Pagamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaPagar/*cdAgrupamento*/, "AUTORIZAR_PAGAMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 4/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Adicionar Pagamento
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Pagamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaPagar/*cdAgrupamento*/, "INS_PAGAMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 5/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Remover Pagamento
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Pagamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaPagar/*cdAgrupamento*/, "DEL_PAGAMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 6/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Estornar Pagamento
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Estornar Pagamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaPagar/*cdAgrupamento*/, "ESTORNAR_PAGAMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 7/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Imprimir Recibo de Pagamento
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir Recibo de Pagamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaPagar/*cdAgrupamento*/, "IMPRIMIR_CONTA_PAGAR_RECIBO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 8/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Inserir Arquivo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Arquivo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaPagar/*cdAgrupamento*/, "INS_CONTA_PAGAR_ARQUIVO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 9/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Atualizar Arquivo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Arquivo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaPagar/*cdAgrupamento*/, "UPD_CONTA_PAGAR_ARQUIVO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 10/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Arquivo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Arquivo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaPagar/*cdAgrupamento*/, "DEL_CONTA_PAGAR_ARQUIVO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 11/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Visualizar Arquivo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Visualizar Arquivo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaPagar/*cdAgrupamento*/, "VISUALIZAR_CONTA_PAGAR_ARQUIVO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 12/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Download Arquivo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Download Arquivo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaPagar/*cdAgrupamento*/, "DOWNLOAD_CONTA_PAGAR_ARQUIVO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 13/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );

			//Refaturar conta a pagar
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Solicitar refaturamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaPagar/*cdAgrupamento*/, "ADM.REFATURAR_CONTA_PAGAR"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 7/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * CÚSTODIA DE CHEQUES
			 */
			//Agrupamento de Contas a Receber
			int cdAgrupamentoCustodia = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"FIN - Custódia" /*nmAgrupamento*/,
					"FIN_CUSTODIA"/*idAgrupamento*/, ""/*dsAgrupamento*/, cdAgrupamentoFinanceiro/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			//Inserir Conta Pagar
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Realizar baixa em cheques"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCustodia/*cdAgrupamento*/, "BAIXA_CHEQUE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * MOVIMENTAÇÃO DE CONTA
			 */
			//Agrupamento Movimmentação de Conta
			int cdAgrupamentoMovimentacaoConta = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"FIN - Movimentação de Conta" /*nmAgrupamento*/,
					"FIN_MOV_CONTA"/*idAgrupamento*/, ""/*dsAgrupamento*/, cdAgrupamentoFinanceiro/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			//Inserir Movimento de Conta
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Movimento de Conta"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMovimentacaoConta/*cdAgrupamento*/, "INS_MOVIMENTO_CONTA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Atualizar Movimento de Conta
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Movimento de Conta"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMovimentacaoConta/*cdAgrupamento*/, "UPD_MOVIMENTO_CONTA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Movimento de Conta
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Movimento de Conta"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMovimentacaoConta/*cdAgrupamento*/, "DEL_MOVIMENTO_CONTA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Realizar Transferência em Movimento de Conta
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Realizar transferência entre contas"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMovimentacaoConta/*cdAgrupamento*/, "TRANSFERIR_MOVIMENTO_CONTA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Imprimir extrato de Movimentação
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir extrato de movimentação de contas"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMovimentacaoConta/*cdAgrupamento*/, "IMPRIMIR_EXTRATO_MOVIMENTO_CONTA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Realizar conciliação de Movimentos
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Concilicar contas"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMovimentacaoConta/*cdAgrupamento*/, "CONCILIAR_MOVIMENTO_CONTA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Compensar Movimentação
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Compensar Movimentação"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMovimentacaoConta/*cdAgrupamento*/, "COMPENSAR_MOVIMENTO_CONTA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Estornar Movimentação
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Estornar Movimentação"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMovimentacaoConta/*cdAgrupamento*/, "ESTORNAR_MOVIMENTO_CONTA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Cancelar Movimentação
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cancelar Movimentação"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMovimentacaoConta/*cdAgrupamento*/, "CANCELAR_MOVIMENTO_CONTA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Emitir contra ordem de movimentação
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Emitir contra ordem Movimentação"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMovimentacaoConta/*cdAgrupamento*/, "CONTRA_ORDEM_MOVIMENTO_CONTA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * FECHAMENTO DE CAIXA
			 */
			//Agrupamento Fechamento de Caixa
			int cdAgrupamentoFechamentoCaixa = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"FIN - Fechamento de Caixa" /*nmAgrupamento*/,
					"FIN_FECHAMENTO_CAIXA"/*idAgrupamento*/, ""/*dsAgrupamento*/, cdAgrupamentoFinanceiro/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			//Iniciar Fechamento de Caixa
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Iniciar Fechamento de Caixa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFechamentoCaixa/*cdAgrupamento*/, "INICIAR_FECHAMENTO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Editar Fechamento de Caixa
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Editar Fechamento de Caixa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFechamentoCaixa/*cdAgrupamento*/, "EDITAR_FECHAMENTO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Concluir Fechamento de Caixa
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Concluir Fechamento de Caixa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFechamentoCaixa/*cdAgrupamento*/, "CONCLUIR_FECHAMENTO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Concluir Fechamento de Caixa
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Fechamento de Caixa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFechamentoCaixa/*cdAgrupamento*/, "EXCLUIR_FECHAMENTO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Enviar Conferencia Fechamento de Caixa
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Enviar Fechamento para conferência"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFechamentoCaixa/*cdAgrupamento*/, "ENVIAR_FECHAMENTO_CAIXA_CONFERENCIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Enviar Fechamento para correção"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFechamentoCaixa/*cdAgrupamento*/, "ENVIAR_FECHAMENTO_CAIXA_CORRECAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Movimentação ao Fechamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFechamentoCaixa/*cdAgrupamento*/, "ADD_MOV_FECHAMENTO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Editar Movimentação do Fechamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFechamentoCaixa/*cdAgrupamento*/, "EDIT_MOV_FECHAMENTO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Movimentação do Fechamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFechamentoCaixa/*cdAgrupamento*/, "EXCLUIR_MOV_FECHAMENTO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Conferir Movimentação do Fechamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFechamentoCaixa/*cdAgrupamento*/, "CONFERIR_MOV_FECHAMENTO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Reprovar Movimentação do Fechamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFechamentoCaixa/*cdAgrupamento*/, "REPROVAR_MOV_FECHAMENTO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Lançar Transferêcnia Fechamento de Caixa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFechamentoCaixa/*cdAgrupamento*/, "TRANSFERIR_FECHAMENTO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir Fechamento de Caixa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFechamentoCaixa/*cdAgrupamento*/, "IMPRIMIR_FECHAMENTO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "[Fech. Caixa] Realizar Upload de anexo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFechamentoCaixa/*cdAgrupamento*/, "UPLOAD_ANEXO_FECHAMENTO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "[Fech. Caixa] Realizar Download de anexo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFechamentoCaixa/*cdAgrupamento*/, "DOWNLOAD_ANEXO_FECHAMENTO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "[Fech. Caixa] Realizar Download de anexo da movimentação"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFechamentoCaixa/*cdAgrupamento*/, "DOWNLOAD_ANEXO_MOV_FECHAMENTO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "[Fech. Caixa] Realizar upload de anexo da movimentação"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFechamentoCaixa/*cdAgrupamento*/, "UPLOAD_ANEXO_MOV_FECHAMENTO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );

			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "[Fech. Caixa] Desbloquear Usuário"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFechamentoCaixa/*cdAgrupamento*/, "DESBLOQUEAR_USUARIO_FECHAMENTO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * AGÊNCIA BANCÁRIA
			 */
			//Agrupamento Fechamento de Caixa
			int cdAgrupamentoAgenciaBancaria = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"FIN - Agências Bancárias" /*nmAgrupamento*/,
					"FIN_AGENCIA_BANCARIA"/*idAgrupamento*/, ""/*dsAgrupamento*/, cdAgrupamentoFinanceiro/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			//Adicionar Agência Bancária
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Agência Bancaria"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoAgenciaBancaria/*cdAgrupamento*/, "INS_AGENCIA_BANCARIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Atualizar Agência Bancária
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Agência Bancaria"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoAgenciaBancaria/*cdAgrupamento*/, "UPD_AGENCIA_BANCARIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Agência Bancária
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Agência Bancaria"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoAgenciaBancaria/*cdAgrupamento*/, "DEL_AGENCIA_BANCARIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * CONTA FINANCEIRA
			 */
			//Agrupamento Fechamento de Caixa
			int cdAgrupamentoContaFinanceira = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"FIN - Conta Financeira" /*nmAgrupamento*/,
					"FIN_CONTA_FINANCEIRA"/*idAgrupamento*/, ""/*dsAgrupamento*/, cdAgrupamentoFinanceiro/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			//Adicionar Conta Financeira
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Conta Financeira"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaFinanceira/*cdAgrupamento*/, "INS_CONTA_FINANCEIRA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Atualizar Conta Financeira
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Conta Financeira"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaFinanceira/*cdAgrupamento*/, "UPD_CONTA_FINANCEIRA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Conta Financeira
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Conta Financeira"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaFinanceira/*cdAgrupamento*/, "DEL_CONTA_FINANCEIRA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Adicionar Conta carteira
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Conta Carteira"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaFinanceira/*cdAgrupamento*/, "INS_CONTA_FINANCEIRA_CARTEIRA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Atualizar Conta Carteira
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Conta Carteira"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaFinanceira/*cdAgrupamento*/, "UPD_CONTA_FINANCEIRA_CARTEIRA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Conta Carteira
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Conta Carteira"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaFinanceira/*cdAgrupamento*/, "DEL_CONTA_FINANCEIRA_CARTEIRA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Adicionar Usuário
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Permitir acesso ao Usuário"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaFinanceira/*cdAgrupamento*/, "INS_CONTA_FINANCEIRA_USUARIO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Usuario
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Remover acesso do Usuário"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoContaFinanceira/*cdAgrupamento*/, "DEL_CONTA_FINANCEIRA_USUARIO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * CATEGORIA ECONOMICA
			 */
			//Agrupamento Fechamento de Caixa
			int cdAgrupamentoCategoriaEconomica = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"FIN - Categorias Econômicas" /*nmAgrupamento*/,
					"FIN_CATEGORIA_ECONOMICA"/*idAgrupamento*/, ""/*dsAgrupamento*/, cdAgrupamentoFinanceiro/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			//Adicionar Conta Financeira
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Categoria"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCategoriaEconomica/*cdAgrupamento*/, "INS_CATEGORIA_ECONOMICA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Atualizar Conta Financeira
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Categoria"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCategoriaEconomica/*cdAgrupamento*/, "UPD_CATEGORIA_ECONOMICA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Conta Financeira
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Categoria"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCategoriaEconomica/*cdAgrupamento*/, "DEL_CATEGORIA_ECONOMICA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * CENTRO DE CUSTO
			 */
			//Agrupamento Centro de Custo
			int cdAgrupamentoCentroCusto = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"FIN - Centro de Custo" /*nmAgrupamento*/,
					"FIN_CENTRO_CUSTO"/*idAgrupamento*/, ""/*dsAgrupamento*/, cdAgrupamentoFinanceiro/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			//Adicionar Centro de Custo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Centro de Custo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCentroCusto/*cdAgrupamento*/, "INS_CENTRO_CUSTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Atualizar Centro de Custo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Centro de Custo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCentroCusto/*cdAgrupamento*/, "UPD_CENTRO_CUSTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Centro de Custo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Centro de Custo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCentroCusto/*cdAgrupamento*/, "DEL_CENTRO_CUSTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Adicionar Plano Centro de Custo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Plano de Centro de Custo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCentroCusto/*cdAgrupamento*/, "INS_PLANO_CENTRO_CUSTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Atualizar Plano Centro de Custo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Plano de Centro de Custo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCentroCusto/*cdAgrupamento*/, "UPD_PLANO_CENTRO_CUSTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Plano Centro de Custo
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Plano de Centro de Custo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCentroCusto/*cdAgrupamento*/, "DEL_PLANO_CENTRO_CUSTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * CONTROLE DE CHEQUES
			 */
			//Agrupamento Controle de Cheques
			int cdAgrupamentoControleCheque = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"FIN - Controle de Cheques" /*nmAgrupamento*/,
					"FIN_CONTROLE_CHEQUE"/*idAgrupamento*/, ""/*dsAgrupamento*/, cdAgrupamentoTabelas/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			//Adicionar Cheques
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Cheques"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoControleCheque/*cdAgrupamento*/, "INS_CHEQUE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Atualizar Cheque
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar cheque"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoControleCheque/*cdAgrupamento*/, "UPD_CHEQUE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Cheque
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir cheque"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoControleCheque/*cdAgrupamento*/, "DEL_CHEQUE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Imprimir Relatório de Cheque
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir relatório de cheques"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoControleCheque/*cdAgrupamento*/, "IMPRIMIR_RELATORIO_CHEQUE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			/**
			 * CONTROLE DE BOLETO
			 */
			//Agrupamento Controle de Boleto
			int cdAgrupamentoControleBoleto = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"FIN - Controle de Boletos" /*nmAgrupamento*/,
					"FIN_CONTROLE_BOLETO"/*idAgrupamento*/, ""/*dsAgrupamento*/, cdAgrupamentoTabelas/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			//Gerar Remessa
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Gerar Remessa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoControleBoleto/*cdAgrupamento*/, "GERAR_REMESSA_BOLETO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Atualizar Cheque
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Importar retorno bancário"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoControleBoleto/*cdAgrupamento*/, "IMPORTAR_RETORNO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Cheque
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir relatório de retorno"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoControleBoleto/*cdAgrupamento*/, "IMPRIMIR_RELATORIO_RETORNO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Imprimir Relatório de Cheque
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir relatório de log"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoControleBoleto/*cdAgrupamento*/, "IMPRIMIR_RELATORIO_LOG_BOLETO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * CONTROLE DE RECEBÍVEIS
			 */
			//Agrupamento Controle de Cheques
			int cdAgrupamentoControleRecebiveis = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"FIN - Controle de Recebíveis" /*nmAgrupamento*/,
					"FIN_CONTROLE_RECEBIVEIS"/*idAgrupamento*/, ""/*dsAgrupamento*/, cdAgrupamentoTabelas/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			//Tranferir Recebível
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Transferir recebível"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoControleRecebiveis/*cdAgrupamento*/, "TRANSFERIR_RECEBIVEL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Registrar Compensação
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Registar Compensação"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoControleRecebiveis/*cdAgrupamento*/, "REGISTRAR_COMPENSACAO_RECEBIVEL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Cancelar Recebível
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Cancelar Recebível"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoControleRecebiveis/*cdAgrupamento*/, "CANCELAR_RECEBIVEL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Imprimir Relatório de Cheque
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir relatório de recebíveis"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoControleRecebiveis/*cdAgrupamento*/, "IMPRIMIR_RELATORIO_RECEBIVEIS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			/**
			 * TÍTULOS DE CRÉDITO
			 */
			//Agrupamento Controle de Cheques
			int cdAgrupamentoTituloCredito = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"FIN - Títulos de Crédito" /*nmAgrupamento*/,
					"FIN_TITULO_CREDITO"/*idAgrupamento*/, ""/*dsAgrupamento*/, cdAgrupamentoTabelas/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			//Adicionar Título de Crédito
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Título de Crédito"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTituloCredito/*cdAgrupamento*/, "INS_TITULO_CREDITO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Atualizar Título de Crédito
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Título de Crédito"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTituloCredito/*cdAgrupamento*/, "UPD_TITULO_CREDITO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Excluir Título de Crédito
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Título de Crédito"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTituloCredito/*cdAgrupamento*/, "DEL_TITULO_CREDITO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			/**
			 * RELATÓRIOS
			 */
			//Relatório contas a pagar
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir relatório de contas a pagar"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRelatorios/*cdAgrupamento*/, "IMPRIMIR_RELATORIO_CONTAS_PAGAR"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Relatório Pagamentos
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir relatório de pagamentos"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRelatorios/*cdAgrupamento*/, "IMPRIMIR_RELATORIO_PAGAMENTOS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Relatório contas a receber
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir relatório de contas a receber"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRelatorios/*cdAgrupamento*/, "IMPRIMIR_RELATORIO_CONTAS_RECEBER"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Relatório Recebimentos
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir relatório de recebimentos"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRelatorios/*cdAgrupamento*/, "IMPRIMIR_RELATORIO_RECEBIMENTOS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Relatório Movimentações
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir relatório de movimentações"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRelatorios/*cdAgrupamento*/, "IMPRIMIR_RELATORIO_MOVIMENTACOES"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Relatório Fluxo de Caixa
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir relatório de Fluxo de Caixa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRelatorios/*cdAgrupamento*/, "IMPRIMIR_RELATORIO_FLUXO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Relatório Previsão Fluxo de Caixa
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir relatório de Previsão de Fluxo de Caixa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRelatorios/*cdAgrupamento*/, "IMPRIMIR_RELATORIO_PREVISAO_FLUXO_CAIXA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			//Relatório de Contas
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir relatório de Contas Financeira"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRelatorios/*cdAgrupamento*/, "IMPRIMIR_RELATORIO_CONTAS_FINANCEIRA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			connect.commit();
			connect.setAutoCommit(true);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
	}
	
	public static void initPermissoesGrl(int cdSistema, int cdModulo, Connection connect){
		try	{
			boolean isAutoCommit = connect.getAutoCommit();
			if(isAutoCommit)
				connect.setAutoCommit(false);
			//PESSOA
			int cdAgrupamentoPessoa = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Dados Pessoais" /*nmAgrupamento*/,
					"GRL_PESSOA"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a aba de manutenção de arquivos/documentos"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "ACESSO_PESSOA_ARQUIVO_DOC"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Arquivo/Documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "INS_PESSOA_ARQUIVO_DOC"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Arquivo/Documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "UPD_PESSOA_ARQUIVO_DOC"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Arquivo/Documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "DEL_PESSOA_ARQUIVO_DOC"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 4/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Visualizar Arquivo/Documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "VISUALIZAR_PESSOA_ARQUIVO_DOC"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 5/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Download Arquivo/Documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "DOWNLOAD_PESSOA_ARQUIVO_DOC"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 6/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			//cadastro - pessoa por vínculo
			cdAgrupamentoPessoa = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Cadastro de pessoas" /*nmAgrupamento*/,
					"GRL_CADASTRO_PESSOA"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			int cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Inserir Pessoa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "GRL.INS_PESSOA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 0/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Pessoa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "GRL.UPD_PESSOA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Pessoa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "GRL.DEL_PESSOA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 2/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			
			cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Inserir Adv. Empresa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "GRL.INS_PESSOA_ADV_EMPRESA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Adv. Empresa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "GRL.UPD_PESSOA_ADV_EMPRESA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 4/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Adv. Empresa"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "GRL.DEL_PESSOA_ADV_EMPRESA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 5/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			
			cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Inserir Adv. Contrário"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "GRL.INS_PESSOA_ADV_ADVERSO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Adv. Contrário"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "GRL.UPD_PESSOA_ADV_ADVERSO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 4/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Adv. Contrário"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "GRL.DEL_PESSOA_ADV_ADVERSO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 5/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			
			cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Inserir Cliente"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "GRL.INS_PESSOA_CLIENTE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 6/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Cliente"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "GRL.UPD_PESSOA_CLIENTE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 7/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Cliente"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "GRL.DEL_PESSOA_CLIENTE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 8/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			
			cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Inserir Adverso"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "GRL.INS_PESSOA_ADVERSO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 9/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Adverso"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "GRL.UPD_PESSOA_ADVERSO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 10/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Adverso"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "GRL.DEL_PESSOA_ADVERSO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 11/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			
			cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Inserir Colaborador"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "GRL.INS_PESSOA_COLABORADOR"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 12/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Colaborador"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "GRL.UPD_PESSOA_COLABORADOR"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 13/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Colaborador"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPessoa/*cdAgrupamento*/, "GRL.DEL_PESSOA_COLABORADOR"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 14/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect);
			
			
			//ALÍNEAS
			int cdAgrupamentoAlinea = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Alínea" /*nmAgrupamento*/,
					"GRL_ALINEA"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de alíneas"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoAlinea/*cdAgrupamento*/, "ACESSO_ALINEA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar alínea"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoAlinea/*cdAgrupamento*/, "INS_ALINEA"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar alínea"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoAlinea/*cdAgrupamento*/, "UPD_ALINEA"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir alínea"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoAlinea/*cdAgrupamento*/, "DEL_ALINEA"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			// REGIÕES
			int cdAgrupamentoRegiao = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Região" /*nmAgrupamento*/,
					"GRL_REGIAO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de regiões"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRegiao/*cdAgrupamento*/, "ACESSO_REGIAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Região"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRegiao/*cdAgrupamento*/, "INS_REGIAO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Região"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRegiao/*cdAgrupamento*/, "UPD_REGIAO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Região"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoRegiao/*cdAgrupamento*/, "DEL_REGIAO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			// TIPO DE ENDEREÇO
			int cdAgrupamentoTipoEndereco = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Tipo de Endereço" /*nmAgrupamento*/,
					"GRL_TIPO_ENDERECO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de Tipo de Endereço"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTipoEndereco/*cdAgrupamento*/, "ACESSO_TIPO_ENDERECO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Tipo de Endereço"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTipoEndereco/*cdAgrupamento*/, "INS_TIPO_ENDERECO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Tipo de Endereço"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTipoEndereco/*cdAgrupamento*/, "UPD_TIPO_ENDERECO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Tipo de Endereço"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTipoEndereco/*cdAgrupamento*/, "DEL_TIPO_ENDERECO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			//PAÍS
			int cdAgrupamentoPais = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"País" /*nmAgrupamento*/,
					"GRL_PAIS"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de País"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPais/*cdAgrupamento*/, "ACESSO_PAIS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar País"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPais/*cdAgrupamento*/, "INS_PAIS"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar País"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPais/*cdAgrupamento*/, "UPD_PAIS"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir País"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoPais/*cdAgrupamento*/, "DEL_PAIS"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			// ESTADOS
			int cdAgrupamentoEstado = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Estado" /*nmAgrupamento*/,
					"GRL_ESTADO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de Estado"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoEstado/*cdAgrupamento*/, "ACESSO_ESTADO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Estado"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoEstado/*cdAgrupamento*/, "INS_ESTADO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Estado"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoEstado/*cdAgrupamento*/, "UPD_ESTADO"/*idAcao*/, AcaoServices.UPDATE /*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Estado"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoEstado/*cdAgrupamento*/, "DEL_ESTADO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			// CIDADES
			int cdAgrupamentoCidade = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Cidade" /*nmAgrupamento*/,
					"GRL_CIDADE"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de Cidade"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCidade/*cdAgrupamento*/, "ACESSO_CIDADE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Cidade"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCidade/*cdAgrupamento*/, "INS_CIDADE"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Cidade"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCidade/*cdAgrupamento*/, "UPD_CIDADE"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Cidade"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCidade/*cdAgrupamento*/, "DEL_CIDADE"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			// BAIRRO
			int cdAgrupamentoBairro = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Bairro" /*nmAgrupamento*/,
					"GRL_BAIRRO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de Bairro"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoBairro/*cdAgrupamento*/, "ACESSO_BAIRRO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Bairro"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoBairro/*cdAgrupamento*/, "INS_BAIRRO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Bairro"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoBairro/*cdAgrupamento*/, "UPD_BAIRRO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Bairro"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoBairro/*cdAgrupamento*/, "DEL_BAIRRO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			// TIPO LOGRADOURO
			int cdAgrupamentoTipoLogradouro = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Tipo de Logradouro" /*nmAgrupamento*/,
					"GRL_TIPO_LOGRADOURO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de Tipo de Logradouro"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTipoLogradouro/*cdAgrupamento*/, "ACESSO_TIPO_LOGRADOURO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Tipo de Logradouro"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTipoLogradouro/*cdAgrupamento*/, "INS_TIPO_LOGRADOURO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Tipo de Logradouro"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTipoLogradouro/*cdAgrupamento*/, "UPD_TIPO_LOGRADOURO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Tipo de Logradouro"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTipoLogradouro/*cdAgrupamento*/, "DEL_TIPO_LOGRADOURO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			// LOGRADOURO
			int cdAgrupamentoLogradouro = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Logradouro" /*nmAgrupamento*/,
					"GRL_LOGRADOURO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de Logradouro"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoLogradouro/*cdAgrupamento*/, "ACESSO_LOGRADOURO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Logradouro"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoLogradouro/*cdAgrupamento*/, "INS_LOGRADOURO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Logradouro"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoLogradouro/*cdAgrupamento*/, "UPD_LOGRADOURO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Logradouro"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoLogradouro/*cdAgrupamento*/, "DEL_LOGRADOURO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			//CBO
			int cdAgrupamentoCBO = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"CBO - Ocupações" /*nmAgrupamento*/,
					"GRL_CBO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de CBO"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCBO/*cdAgrupamento*/, "ACESSO_CBO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar CBO"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCBO/*cdAgrupamento*/, "INS_CBO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar CBO"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCBO/*cdAgrupamento*/, "UPD_CBO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir CBO"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCBO/*cdAgrupamento*/, "DEL_CBO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar sinônimo de CBO"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCBO/*cdAgrupamento*/, "INS_SINONIMO_CBO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir sinônimo de CBO"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCBO/*cdAgrupamento*/, "DEL_SINONIMO_CBO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			//Classificação Financeira
			int cdAgrupamentoClassificacaoFinanceira = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Classificação Financeira" /*nmAgrupamento*/,
					"GRL_CBO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de Classificação Financeira"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoClassificacaoFinanceira/*cdAgrupamento*/, "ACESSO_CLASSIFICACAO_FINANCEIRA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Classificação Financeira"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoClassificacaoFinanceira/*cdAgrupamento*/, "INS_CLASSIFICACAO_FINANCEIRA"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Classificação Financeira"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoClassificacaoFinanceira/*cdAgrupamento*/, "UPD_CLASSIFICACAO_FINANCEIRA"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Classificação Financeira"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoClassificacaoFinanceira/*cdAgrupamento*/, "DEL_CLASSIFICACAO_FINANCEIRA"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			//CNAE
			int cdAgrupamentoCnae = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"CNAE" /*nmAgrupamento*/,
					"GRL_CNAE"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de CNAE"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCnae/*cdAgrupamento*/, "ACESSO_CNAE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar CNAE"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCnae/*cdAgrupamento*/, "INS_CNAE"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar CNAE"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCnae/*cdAgrupamento*/, "UPD_CNAE"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir CNAE"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoCnae/*cdAgrupamento*/, "DEL_CNAE"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			//Escolaridade
			int cdAgrupamentoEscolaridade = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Escolaridade" /*nmAgrupamento*/,
					"GRL_ESCOLARIDADE"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de Escolaridade"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoEscolaridade/*cdAgrupamento*/, "ACESSO_ESCOLARIDADE"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Escolaridade"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoEscolaridade/*cdAgrupamento*/, "INS_ESCOLARIDADE"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Escolaridade"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoEscolaridade/*cdAgrupamento*/, "UPD_ESCOLARIDADE"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Escolaridade"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoEscolaridade/*cdAgrupamento*/, "DEL_ESCOLARIDADE"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
		
			//Forma de Divulgação
			int cdAgrupamentoFormaDivulgacao = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Forma de Divulgação" /*nmAgrupamento*/,
					"GRL_FORMA_DIVULGACAO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de Forma de Divulgação"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFormaDivulgacao/*cdAgrupamento*/, "ACESSO_FORMA_DIVULGACAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Forma de Divulgação"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFormaDivulgacao/*cdAgrupamento*/, "INS_FORMA_DIVULGACAO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Forma de Divulgação"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFormaDivulgacao/*cdAgrupamento*/, "UPD_FORMA_DIVULGACAO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Forma de Divulgação"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFormaDivulgacao/*cdAgrupamento*/, "DEL_FORMA_DIVULGACAO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
		
			//Funções
			int cdAgrupamentoFuncao = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Função" /*nmAgrupamento*/,
					"GRL_FUNCAO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de função"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFuncao/*cdAgrupamento*/, "ACESSO_FUNCAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar função"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFuncao/*cdAgrupamento*/, "INS_FUNCAO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar função"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFuncao/*cdAgrupamento*/, "UPD_FUNCAO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir função"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFuncao/*cdAgrupamento*/, "DEL_FUNCAO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			//Tipo de Documento
			int cdAgrupamentoTipoDocumento = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Tipo de Documento" /*nmAgrupamento*/,
					"GRL_TIPO_DOCUMENTO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de Tipo de Documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTipoDocumento/*cdAgrupamento*/, "ACESSO_TIPO_DOCUMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Tipo de Documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTipoDocumento/*cdAgrupamento*/, "INS_TIPO_DOCUMENTO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Tipo de Documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTipoDocumento/*cdAgrupamento*/, "UPD_TIPO_DOCUMENTO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Tipo de Documento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTipoDocumento/*cdAgrupamento*/, "DEL_TIPO_DOCUMENTO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			//Tipo de Ocorrência
			int cdAgrupamentoTipoOcorrencia = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Tipo de Ocorrência" /*nmAgrupamento*/,
					"GRL_TIPO_OCORRENCIA"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de Tipo de Ocorrência"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTipoOcorrencia/*cdAgrupamento*/, "ACESSO_TIPO_OCORRENCIA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Tipo de Ocorrência"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTipoOcorrencia/*cdAgrupamento*/, "INS_TIPO_OCORRENCIA"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Tipo de Ocorrência"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTipoOcorrencia/*cdAgrupamento*/, "UPD_TIPO_OCORRENCIA"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Tipo de Ocorrência"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTipoOcorrencia/*cdAgrupamento*/, "DEL_TIPO_OCORRENCIA"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
		
			//Tipo de Vínculo
			int cdAgrupamentoTipoVinculo = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Tipo de Vínculo" /*nmAgrupamento*/,
					"GRL_TIPO_VINCULO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de Tipo de vínculo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTipoVinculo/*cdAgrupamento*/, "ACESSO_TIPO_VINCULO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Tipo de vínculo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTipoVinculo/*cdAgrupamento*/, "INS_TIPO_VINCULO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar Tipo de vínculo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTipoVinculo/*cdAgrupamento*/, "UPD_TIPO_VINCULO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Tipo de vínculo"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTipoVinculo/*cdAgrupamento*/, "DEL_TIPO_VINCULO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			//Natureza Júridica
			int cdAgrupamentoNaturezaJuridica = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Natureza Jurídica" /*nmAgrupamento*/,
					"GRL_NATUREZA_JURIDICA"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de natureza jurídica"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoNaturezaJuridica/*cdAgrupamento*/, "ACESSO_NATUREZA_JURIDICA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar natureza jurídica"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoNaturezaJuridica/*cdAgrupamento*/, "INS_NATUREZA_JURIDICA"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar natureza jurídica"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoNaturezaJuridica/*cdAgrupamento*/, "UPD_NATUREZA_JURIDICA"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir natureza jurídica"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoNaturezaJuridica/*cdAgrupamento*/, "DEL_NATUREZA_JURIDICA"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			//Bancos
			int cdAgrupamentoBanco = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Banco" /*nmAgrupamento*/,
					"GRL_BANCO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de banco"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoBanco/*cdAgrupamento*/, "ACESSO_BANCO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar banco"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoBanco/*cdAgrupamento*/, "INS_BANCO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar banco"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoBanco/*cdAgrupamento*/, "UPD_BANCO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir banco"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoBanco/*cdAgrupamento*/, "DEL_BANCO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			//Forma de Pagamento
			int cdAgrupamentoFormaPagamento = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Forma de Pagamento" /*nmAgrupamento*/,
					"GRL_FORMA_PAGAMENTO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de forma de pagamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFormaPagamento/*cdAgrupamento*/, "ACESSO_FORMA_PAGAMENTO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar forma de pagamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFormaPagamento/*cdAgrupamento*/, "INS_FORMA_PAGAMENTO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar forma de pagamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFormaPagamento/*cdAgrupamento*/, "UPD_FORMA_PAGAMENTO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir forma de pagamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFormaPagamento/*cdAgrupamento*/, "DEL_FORMA_PAGAMENTO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar forma de plano de pagamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFormaPagamento/*cdAgrupamento*/, "INS_FORMA_PAGAMENTO_PLANO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar forma de plano de pagamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFormaPagamento/*cdAgrupamento*/, "UPD_FORMA_PAGAMENTO_PLANO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir forma de plano de pagamento"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoFormaPagamento/*cdAgrupamento*/, "DEL_FORMA_PAGAMENTO_PLANO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			//Moedas
			int cdAgrupamentoMoeda = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Moeda" /*nmAgrupamento*/,
					"GRL_MOEDA"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de moeda"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMoeda/*cdAgrupamento*/, "ACESSO_MOEDA"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar moeda"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMoeda/*cdAgrupamento*/, "INS_MOEDA"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar moeda"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMoeda/*cdAgrupamento*/, "UPD_MOEDA"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir moeda"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMoeda/*cdAgrupamento*/, "DEL_MOEDA"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			/*
			 * FERIADOS
			 * */
			saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Feriado" /*nmAgrupamento*/,
					"GRL_FERIADO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			 
			/* 
			 *  permissões existentes
			 *  insert: FERIADOS_I
				update: FERIADOS_A
				remove: FERIADOS_E 
			 */
			//Indicadores
			int cdAgrupamentoIndicador = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Indicador" /*nmAgrupamento*/,
					"GRL_INDICADOR"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso ao cadastro de indicador"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoIndicador/*cdAgrupamento*/, "ACESSO_INDICADOR"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar indicador"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoIndicador/*cdAgrupamento*/, "INS_INDICADOR"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar indicador"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoIndicador/*cdAgrupamento*/, "UPD_INDICADOR"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir indicador"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoIndicador/*cdAgrupamento*/, "DEL_INDICADOR"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar indicador variação"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoIndicador/*cdAgrupamento*/, "INS_INDICADOR_VARIACAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Atualizar indicador variação"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoIndicador/*cdAgrupamento*/, "UPD_INDICADOR_VARIACAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir indicador variação"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoIndicador/*cdAgrupamento*/, "DEL_INDICADOR_VARIACAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			connect.commit();
			if(isAutoCommit)
				connect.setAutoCommit(true);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
	}
	
	public static void initPermissoesAlm(int cdSistema, int cdModulo, Connection connect){
		try	{
			connect.setAutoCommit(false);
			//Agrupamento Tabela Preço
			int cdAgrupamentoTabelaPreco = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Tabela de Preços" /*nmAgrupamento*/,
													"ALM_TABELA_PRECO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			//Acesso Tabela de Preços
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Tabela de Preços"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoTabelaPreco/*cdAgrupamento*/, "ACESSO_TABELA_PRECO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 15/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			connect.commit();
			connect.setAutoCommit(true);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
	}
	
	public static void initPermissoesMob(int cdSistema, int cdModulo, Connection connect){
		initPermissoesMob(cdSistema, cdModulo, null, connect);
	}
	
	public static void initPermissoesMob(int cdSistema, int cdModulo, File xmlPermissoes, Connection connect){
		
		if(connect == null)
			connect = Conexao.conectar();
		
		try	{
			connect.setAutoCommit(false);
			
			if(xmlPermissoes != null){
			
				JAXBContext jaxbContext;
				try {
					jaxbContext = JAXBContext.newInstance(AgrupamentosAcao.class);
			
					Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
					AgrupamentosAcao agrupamentosAcao = (AgrupamentosAcao) jaxbUnmarshaller.unmarshal(xmlPermissoes);
					for(AgrupamentoAcao agrupamentoAcao : agrupamentosAcao.getAgrupamentoAcao()){
						
						ResultSetMap rsmAgrupamentoAcao = AgrupamentoAcaoServices.getAgrupamentoById(agrupamentoAcao.getIdAgrupamento(), connect);
						if(rsmAgrupamentoAcao.next()){
							agrupamentoAcao.setCdAgrupamento(rsmAgrupamentoAcao.getInt("cd_agrupamento"));
							System.out.println(agrupamentoAcao.getNmAgrupamento());
							if(AgrupamentoAcaoDAO.update(agrupamentoAcao, connect) < 0){
								Conexao.rollback(connect);
								System.out.println("Erro ao atualizar agrupamento de ação");
								return;
							}else {
								System.out.println("Êxito ao atualizar agrupamento de ação");
							}
							
							for(Acao acao : agrupamentoAcao.getAcoes().getAcoes()){
								
								acao.setCdAgrupamento(agrupamentoAcao.getCdAgrupamento());
								
								Acao acaoBD = getByIdAcao(acao.getIdAcao(), connect);
								
								if(acaoBD != null && acaoBD.getCdAcao() >= 0){
									acao.setCdAcao(acaoBD.getCdAcao());
									if(AcaoDAO.update(acao, connect) < 0){
										Conexao.rollback(connect);
										System.out.println("Erro ao atualizar ação");
										return;
									}else {
										System.out.println("Êxito ao atualizar ação");
									}
								}else{
									if(AcaoDAO.insert(acao, connect) < 0){
										Conexao.rollback(connect);
										System.out.println("Erro ao inserir ação");
										return;
									}else {
										System.out.println("Êxito ao inserir ação");
									}
								}
							}
							
							
						}
						else{
							
							agrupamentoAcao.setCdAgrupamento(AgrupamentoAcaoDAO.insert(agrupamentoAcao, connect));
							
							if(agrupamentoAcao.getCdAgrupamento() <= 0){
								Conexao.rollback(connect);
								System.out.println("Erro ao inserir agrupamento de ação");
								return;
							}else {
								System.out.println("Êxito ao inserir agrupamento de ação");
							}
							
							for(Acao acao : agrupamentoAcao.getAcoes().getAcoes()){
								
								acao.setCdAgrupamento(agrupamentoAcao.getCdAgrupamento());
								
								Acao acaoBD = getByIdAcao(acao.getIdAcao(), connect);
								if(acaoBD != null){
									acao.setCdAcao(acaoBD.getCdAcao());
									if(AcaoDAO.update(acao, connect) < 0){
										Conexao.rollback(connect);
										System.out.println("Erro ao atualizar ação");
										return;
									}else {
										System.out.println("Êxito ao atualizao ação");
									}
								}
								else{
									if(AcaoDAO.insert(acao, connect) < 0){
										Conexao.rollback(connect);
										System.out.println("Erro ao inserir ação");
										return;
									}else {
										System.out.println("Êxito ao inserir ação");
									}
								}
							}
						}
						
					}
					
					System.out.println("Processo finalizado");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}else{
			
				//Agrupamento Tabela
				int cdAgrupamentoPneAcessoTabelas = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Acesso a Aba de Tabelas" /*nmAgrupamento*/,
														"PNE_ACESSO_ABA_TABELAS"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Aba de Tabelas"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPneAcessoTabelas/*cdAgrupamento*/, "PNE_ACESSO_ABA_TABELAS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 15/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				
				//Coletivo Urbano
				//Veiculos
				int cdAgrupamentoVeiculoColetivoUrbano = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Coletivo Urbano - Veículo" /*nmAgrupamento*/,
						"MOB_VEICULO_COLETIVO_URBANO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoColetivoUrbano/*cdAgrupamento*/, "MOB.INS_VEICULO_COLETIVO_URBANO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoColetivoUrbano/*cdAgrupamento*/, "MOB.UPD_VEICULO_COLETIVO_URBANO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoColetivoUrbano/*cdAgrupamento*/, "MOB.DEL_VEICULO_COLETIVO_URBANO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Consultar situação do veículo no DETRAN"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoColetivoUrbano/*cdAgrupamento*/, "MOB.CONSULTA_DETRAN_COLETIVO_URBANO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 4/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar equipamento ao veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoColetivoUrbano/*cdAgrupamento*/, "MOB.INS_VEICULO_EQUIPAMENTO_COLETIVO_URBANO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 5/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar equipamento do veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoColetivoUrbano/*cdAgrupamento*/, "MOB.UPD_VEICULO_EQUIPAMENTO_COLETIVO_URBANO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 6/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir equipamento do veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoColetivoUrbano/*cdAgrupamento*/, "MOB.DEL_VEICULO_EQUIPAMENTO_COLETIVO_URBANO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 7/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
	
				//Concessao - Concessões
				int cdAgrupamentoConcessao = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Coletivo Urbano - Concessão" /*nmAgrupamento*/,
						"MOB_CONCESSAO_COLETIVO_URBANO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
				int cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar concessão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoConcessao/*cdAgrupamento*/, "MOB.INS_CONCESSAO_COLETIVO_URBANO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar concessão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoConcessao/*cdAgrupamento*/, "MOB.UPD_CONCESSAO_COLETIVO_URBANO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir concessão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoConcessao/*cdAgrupamento*/, "MOB.DEL_CONCESSAO_COLETIVO_URBANO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				//aba Frota/Veículos
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Veículo na concessão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoConcessao/*cdAgrupamento*/, "MOB.INS_VEICULO_CONCESSAO_COLETIVO_URBANO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Veículo na concessão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoConcessao/*cdAgrupamento*/, "MOB.UPD_VEICULO_CONCESSAO_COLETIVO_URBANO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Veículo na concessão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoConcessao/*cdAgrupamento*/, "MOB.DEL_VEICULO_CONCESSAO_COLETIVO_URBANO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				//aba Representantes
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar representante"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoConcessao/*cdAgrupamento*/, "MOB.INS_REPRESENTANTE_CONCESSAO_COLETIVO_URBANO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar representante"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoConcessao/*cdAgrupamento*/, "MOB.UPD_REPRESENTANTE_CONCESSAO_COLETIVO_URBANO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir representante"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoConcessao/*cdAgrupamento*/, "MOB.DEL_REPRESENTANTE_CONCESSAO_COLETIVO_URBANO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				//aba Arquivos
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar arquivos"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoConcessao/*cdAgrupamento*/, "MOB.INS_ARQUIVO_CONCESSAO_COLETIVO_URBANO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar arquivos"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoConcessao/*cdAgrupamento*/, "MOB.UPD_ARQUIVO_CONCESSAO_COLETIVO_URBANO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir arquivos"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoConcessao/*cdAgrupamento*/, "MOB.DEL_ARQUIVO_CONCESSAO_COLETIVO_URBANO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				
				//Concessao - Concessionários
				
				//Concessao - Representantes
				
				//Concessao - Controle das Concessões
				
				// Vistoria - Agendamento de Vistoria
				int cdAgrupamentoAgdVistoriaColetivoUrbano = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Coletivo Urbano - Agendamento de Vistoria" /*nmAgrupamento*/,
						"MOB_AGD_VISTORIA_COLETIVO_URBANO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar vistoria"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAgdVistoriaColetivoUrbano/*cdAgrupamento*/, "MOB.INS_VISTORIA_COLETIVO_URBANO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar vistoria"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAgdVistoriaColetivoUrbano/*cdAgrupamento*/, "MOB.UPD_VISTORIA_COLETIVO_URBANO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir vistoria"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAgdVistoriaColetivoUrbano/*cdAgrupamento*/, "MOB.DEL_VISTORIA_COLETIVO_URBANO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir autorização de vistoria"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAgdVistoriaColetivoUrbano/*cdAgrupamento*/, "MOB.PRINT_VISTORIA_COLETIVO_URBANO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				
				// Vistoria - Gereciamento de Vistoria
				
				// Infrações - AIT
				int cdAgrupamentoAit = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Coletivo Urbano - Infração" /*nmAgrupamento*/,
						"MOB_AIT"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar AIT"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAit/*cdAgrupamento*/, "MOB.INS_AIT"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 5/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar AIT"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAit/*cdAgrupamento*/, "MOB.UPD_AIT"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 4/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir AIT"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAit/*cdAgrupamento*/, "MOB.DEL_AIT"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 3/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Encerrar AIT"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAit/*cdAgrupamento*/, "MOB.ENCERRAR_AIT"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 2/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				//aba Recursos
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Recurso-AIT"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAit/*cdAgrupamento*/, "MOB.INS_AIT_RECURSO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Recurso-AIT"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAit/*cdAgrupamento*/, "MOB.UPD_AIT_RECURSO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 0/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				
				//Infrações - Tipos de infração
				
				//Infrações - Notificar AIT
				
				//Infrações - Recurso Individual
				
		//Táxi
				//Veiculos
				int cdAgrupamentoVeiculoTaxi = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Táxi - Veículo" /*nmAgrupamento*/,
						"MOB_VEICULO_TAXI"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoTaxi/*cdAgrupamento*/, "MOB.INS_VEICULO_TAXI"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoTaxi/*cdAgrupamento*/, "MOB.UPD_VEICULO_TAXI"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoTaxi/*cdAgrupamento*/, "MOB.DEL_VEICULO_TAXI"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Consultar situação do veículo no DETRAN"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoTaxi/*cdAgrupamento*/, "MOB.CONSULTA_DETRAN_TAXI"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar equipamento ao veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoTaxi/*cdAgrupamento*/, "MOB.INS_VEICULO_EQUIPAMENTO_TAXI"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar equipamento do veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoTaxi/*cdAgrupamento*/, "MOB.UPD_VEICULO_EQUIPAMENTO_TAXI"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir equipamento do veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoTaxi/*cdAgrupamento*/, "MOB.DEL_VEICULO_EQUIPAMENTO_TAXI"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
	
				//Infrações - NIC
				int cdAgrupamentoNic = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Táxi - Infração" /*nmAgrupamento*/,
						"MOB_NIC"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar NIC"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoNic/*cdAgrupamento*/, "MOB.INS_NIC"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar NIC"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoNic/*cdAgrupamento*/, "MOB.UPD_NIC"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir NIC"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoNic/*cdAgrupamento*/, "MOB.DEL_NIC"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Encerrar NIC"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoNic/*cdAgrupamento*/, "MOB.ENCERRAR_NIC"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				//aba Recursos
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Recurso-NIC"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoNic/*cdAgrupamento*/, "MOB.INS_NIC_RECURSO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Recurso-NIC"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoNic/*cdAgrupamento*/, "MOB.UPD_NIC_RECURSO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				
				//Permissão
				int cdAgrupamentoPermissaoTaxi = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Táxi - Permissão" /*nmAgrupamento*/,
						"MOB_PERMISSAO_TAXI"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoTaxi/*cdAgrupamento*/, "MOB.INS_PERMISSAO_TAXI"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoTaxi/*cdAgrupamento*/, "MOB.UPD_PERMISSAO_TAXI"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoTaxi/*cdAgrupamento*/, "MOB.DEL_PERMISSAO_TAXI"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				//aba Frota/Veículos
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Veículo na permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoTaxi/*cdAgrupamento*/, "MOB.INS_VEICULO_PERMISSAO_TAXI"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Veículo na permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoTaxi/*cdAgrupamento*/, "MOB.UPD_VEICULO_PERMISSAO_TAXI"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Veículo na permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoTaxi/*cdAgrupamento*/, "MOB.DEL_VEICULO_PERMISSAO_TAXI"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				//aba Motorista auxiliar
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar motorista auxiliar"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoTaxi/*cdAgrupamento*/, "MOB.INS_AUXILIAR_PERMISSAO_TAXI"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar motorista auxiliar"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoTaxi/*cdAgrupamento*/, "MOB.UPD_AUXILIAR_PERMISSAO_TAXI"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir motorista auxiliar"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoTaxi/*cdAgrupamento*/, "MOB.DEL_AUXILIAR_PERMISSAO_TAXI"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				//aba Arquivos
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar arquivos"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoTaxi/*cdAgrupamento*/, "MOB.INS_ARQUIVO_PERMISSAO_TAXI"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar arquivos"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoTaxi/*cdAgrupamento*/, "MOB.UPD_ARQUIVO_PERMISSAO_TAXI"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir arquivos"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoTaxi/*cdAgrupamento*/, "MOB.DEL_ARQUIVO_PERMISSAO_TAXI"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				
				// Vistoria - Agendamento de Vistoria
					int cdAgrupamentoAgdVistoriaTaxi = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Táxi - Agendamento de Vistoria" /*nmAgrupamento*/,
						"MOB_AGD_VISTORIA_TAXI"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
					cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar vistoria"/*nmAcao*/, ""/*dsAcao*/,
							cdAgrupamentoAgdVistoriaTaxi/*cdAgrupamento*/, "MOB.INS_VISTORIA_TAXI"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
					saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar vistoria"/*nmAcao*/, ""/*dsAcao*/,
							cdAgrupamentoAgdVistoriaTaxi/*cdAgrupamento*/, "MOB.UPD_VISTORIA_TAXI"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
					saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir vistoria"/*nmAcao*/, ""/*dsAcao*/,
							cdAgrupamentoAgdVistoriaTaxi/*cdAgrupamento*/, "MOB.DEL_VISTORIA_TAXI"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
					saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir autorização de vistoria"/*nmAcao*/, ""/*dsAcao*/,
							cdAgrupamentoAgdVistoriaTaxi/*cdAgrupamento*/, "MOB.PRINT_VISTORIA_TAXI"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
							
		//Coletivo Rural
				//Veiculos
				int cdAgrupamentoVeiculoColetivoRural = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Coletivo Rural - Veículo" /*nmAgrupamento*/,
						"MOB_VEICULO_COLETIVO_RURAL"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoColetivoRural/*cdAgrupamento*/, "MOB.INS_VEICULO_COLETIVO_RURAL"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoColetivoRural/*cdAgrupamento*/, "MOB.UPD_VEICULO_COLETIVO_RURAL"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoColetivoRural/*cdAgrupamento*/, "MOB.DEL_VEICULO_COLETIVO_RURAL"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Consultar situação do veículo no DETRAN"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoColetivoRural/*cdAgrupamento*/, "MOB.CONSULTA_DETRAN_COLETIVO_RURAL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar equipamento ao veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoColetivoRural/*cdAgrupamento*/, "MOB.INS_VEICULO_EQUIPAMENTO_COLETIVO_RURAL"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar equipamento do veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoColetivoRural/*cdAgrupamento*/, "MOB.UPD_VEICULO_EQUIPAMENTO_COLETIVO_RURAL"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir equipamento do veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoColetivoRural/*cdAgrupamento*/, "MOB.DEL_VEICULO_EQUIPAMENTO_COLETIVO_RURAL"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
	
				//Permissão
				int cdAgrupamentoPermissaoColetivoRural = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Coletivo Rural - Permissão" /*nmAgrupamento*/,
						"MOB_PERMISSAO_COLETIVO_RURAL"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoColetivoRural/*cdAgrupamento*/, "MOB.INS_PERMISSAO_COLETIVO_RURAL"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoColetivoRural/*cdAgrupamento*/, "MOB.UPD_PERMISSAO_COLETIVO_RURAL"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoColetivoRural/*cdAgrupamento*/, "MOB.DEL_PERMISSAO_COLETIVO_RURAL"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				//aba Frota/Veículos
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Veículo na permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoColetivoRural/*cdAgrupamento*/, "MOB.INS_VEICULO_PERMISSAO_COLETIVO_RURAL"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Veículo na permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoColetivoRural/*cdAgrupamento*/, "MOB.UPD_VEICULO_PERMISSAO_COLETIVO_RURAL"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Veículo na permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoColetivoRural/*cdAgrupamento*/, "MOB.DEL_VEICULO_PERMISSAO_COLETIVO_RURAL"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				//aba Motorista auxiliar
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar motorista auxiliar"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoColetivoRural/*cdAgrupamento*/, "MOB.INS_AUXILIAR_PERMISSAO_COLETIVO_RURAL"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar motorista auxiliar"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoColetivoRural/*cdAgrupamento*/, "MOB.UPD_AUXILIAR_PERMISSAO_COLETIVO_RURAL"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir motorista auxiliar"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoColetivoRural/*cdAgrupamento*/, "MOB.DEL_AUXILIAR_PERMISSAO_COLETIVO_RURAL"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				//aba Arquivos
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar arquivos"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoColetivoRural/*cdAgrupamento*/, "MOB.INS_ARQUIVO_PERMISSAO_COLETIVO_RURAL"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar arquivos"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoColetivoRural/*cdAgrupamento*/, "MOB.UPD_ARQUIVO_PERMISSAO_COLETIVO_RURAL"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir arquivos"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoColetivoRural/*cdAgrupamento*/, "MOB.DEL_ARQUIVO_PERMISSAO_COLETIVO_RURAL"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				
				// Vistoria - Agendamento de Vistoria
				int cdAgrupamentoAgdVistoriaColetivoRural = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Coletivo Rural - Agendamento de Vistoria" /*nmAgrupamento*/,
					"MOB_AGD_VISTORIA_COLETIVO_RURAL"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar vistoria"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAgdVistoriaColetivoRural/*cdAgrupamento*/, "MOB.INS_VISTORIA_COLETIVO_RURAL"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar vistoria"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAgdVistoriaColetivoRural/*cdAgrupamento*/, "MOB.UPD_VISTORIA_COLETIVO_RURAL"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir vistoria"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAgdVistoriaColetivoRural/*cdAgrupamento*/, "MOB.DEL_VISTORIA_COLETIVO_RURAL"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir autorização de vistoria"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAgdVistoriaColetivoRural/*cdAgrupamento*/, "MOB.PRINT_VISTORIA_COLETIVO_RURAL"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
						
	
		//Escolar Urbano
				//Veiculos
				int cdAgrupamentoVeiculoEscolarUrbano = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Escolar Urbano - Veículo" /*nmAgrupamento*/,
						"MOB_VEICULO_ESCOLAR_URBANO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoEscolarUrbano/*cdAgrupamento*/, "MOB.INS_VEICULO_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoEscolarUrbano/*cdAgrupamento*/, "MOB.UPD_VEICULO_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoEscolarUrbano/*cdAgrupamento*/, "MOB.DEL_VEICULO_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Consultar situação do veículo no DETRAN"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoEscolarUrbano/*cdAgrupamento*/, "MOB.CONSULTA_DETRAN_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar equipamento ao veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoEscolarUrbano/*cdAgrupamento*/, "MOB.INS_VEICULO_EQUIPAMENTO_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar equipamento do veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoEscolarUrbano/*cdAgrupamento*/, "MOB.UPD_VEICULO_EQUIPAMENTO_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir equipamento do veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoEscolarUrbano/*cdAgrupamento*/, "MOB.DEL_VEICULO_EQUIPAMENTO_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
	
				//Permissão
				int cdAgrupamentoPermissaoEscolarUrbano = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Escolar Urbano - Permissão" /*nmAgrupamento*/,
						"MOB_PERMISSAO_ESCOLAR_URBANO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoEscolarUrbano/*cdAgrupamento*/, "MOB.INS_PERMISSAO_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoEscolarUrbano/*cdAgrupamento*/, "MOB.UPD_PERMISSAO_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoEscolarUrbano/*cdAgrupamento*/, "MOB.DEL_PERMISSAO_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				//aba Frota/Veículos
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Veículo na permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoEscolarUrbano/*cdAgrupamento*/, "MOB.INS_VEICULO_PERMISSAO_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Veículo na permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoEscolarUrbano/*cdAgrupamento*/, "MOB.UPD_VEICULO_PERMISSAO_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Veículo na permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoEscolarUrbano/*cdAgrupamento*/, "MOB.DEL_VEICULO_PERMISSAO_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				//aba Motorista auxiliar
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar motorista auxiliar"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoEscolarUrbano/*cdAgrupamento*/, "MOB.INS_AUXILIAR_PERMISSAO_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar motorista auxiliar"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoEscolarUrbano/*cdAgrupamento*/, "MOB.UPD_AUXILIAR_PERMISSAO_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir motorista auxiliar"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoEscolarUrbano/*cdAgrupamento*/, "MOB.DEL_AUXILIAR_PERMISSAO_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				//aba Arquivos
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar arquivos"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoEscolarUrbano/*cdAgrupamento*/, "MOB.INS_ARQUIVO_PERMISSAO_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar arquivos"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoEscolarUrbano/*cdAgrupamento*/, "MOB.UPD_ARQUIVO_PERMISSAO_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir arquivos"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoEscolarUrbano/*cdAgrupamento*/, "MOB.DEL_ARQUIVO_PERMISSAO_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				
				// Vistoria - Agendamento de Vistoria
				int cdAgrupamentoAgdVistoriaEscolarUrbano = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Escolar Urbano - Agendamento de Vistoria" /*nmAgrupamento*/,
					"MOB_AGD_VISTORIA_ESCOLAR_URBANO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar vistoria"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAgdVistoriaEscolarUrbano/*cdAgrupamento*/, "MOB.INS_VISTORIA_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar vistoria"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAgdVistoriaEscolarUrbano/*cdAgrupamento*/, "MOB.UPD_VISTORIA_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir vistoria"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAgdVistoriaEscolarUrbano/*cdAgrupamento*/, "MOB.DEL_VISTORIA_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir autorização de vistoria"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAgdVistoriaEscolarUrbano/*cdAgrupamento*/, "MOB.PRINT_VISTORIA_ESCOLAR_URBANO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
										
		//Contratados
				//Veiculos
				int cdAgrupamentoVeiculoContratados = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Contratados - Veículo" /*nmAgrupamento*/,
						"MOB_VEICULO_CONTRATO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoContratados/*cdAgrupamento*/, "MOB.INS_VEICULO_CONTRATO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoContratados/*cdAgrupamento*/, "MOB.UPD_VEICULO_CONTRATO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoContratados/*cdAgrupamento*/, "MOB.DEL_VEICULO_CONTRATO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Consultar situação do veículo no DETRAN"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoContratados/*cdAgrupamento*/, "MOB.CONSULTA_DETRAN_CONTRATO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar equipamento ao veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoContratados/*cdAgrupamento*/, "MOB.INS_VEICULO_EQUIPAMENTO_CONTRATO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar equipamento do veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoContratados/*cdAgrupamento*/, "MOB.UPD_VEICULO_EQUIPAMENTO_CONTRATO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir equipamento do veículo"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoVeiculoContratados/*cdAgrupamento*/, "MOB.DEL_VEICULO_EQUIPAMENTO_CONTRATO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
	
				//Permissão
				int cdAgrupamentoPermissaoContratados = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Contratados - Permissão" /*nmAgrupamento*/,
						"MOB_PERMISSAO_CONTRATO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoContratados/*cdAgrupamento*/, "MOB.INS_PERMISSAO_CONTRATO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoContratados/*cdAgrupamento*/, "MOB.UPD_PERMISSAO_CONTRATO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoContratados/*cdAgrupamento*/, "MOB.DEL_PERMISSAO_CONTRATO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				//aba Frota/Veículos
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar Veículo na permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoContratados/*cdAgrupamento*/, "MOB.INS_VEICULO_PERMISSAO_CONTRATO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar Veículo na permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoContratados/*cdAgrupamento*/, "MOB.UPD_VEICULO_PERMISSAO_CONTRATO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir Veículo na permissão"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoContratados/*cdAgrupamento*/, "MOB.DEL_VEICULO_PERMISSAO_CONTRATO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				//aba Motorista auxiliar
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar motorista auxiliar"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoContratados/*cdAgrupamento*/, "MOB.INS_AUXILIAR_PERMISSAO_CONTRATO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar motorista auxiliar"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoContratados/*cdAgrupamento*/, "MOB.UPD_AUXILIAR_PERMISSAO_CONTRATO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir motorista auxiliar"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoContratados/*cdAgrupamento*/, "MOB.DEL_AUXILIAR_PERMISSAO_CONTRATO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				//aba Arquivos
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar arquivos"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoContratados/*cdAgrupamento*/, "MOB.INS_ARQUIVO_PERMISSAO_CONTRATO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar arquivos"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoContratados/*cdAgrupamento*/, "MOB.UPD_ARQUIVO_PERMISSAO_CONTRATO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir arquivos"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoPermissaoContratados/*cdAgrupamento*/, "MOB.DEL_ARQUIVO_PERMISSAO_CONTRATO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, cdAcaoSuperior/*cdAcaoSuperior*/), connect  );
	
				// Vistoria - Agendamento de Vistoria
				int cdAgrupamentoAgdVistoriaContratados = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Contratados - Agendamento de Vistoria" /*nmAgrupamento*/,
					"MOB_AGD_VISTORIA_CONTRATO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
				cdAcaoSuperior = saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Adicionar vistoria"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAgdVistoriaContratados/*cdAgrupamento*/, "MOB.INS_VISTORIA_CONTRATO"/*idAcao*/, AcaoServices.INSERT/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Alterar vistoria"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAgdVistoriaContratados/*cdAgrupamento*/, "MOB.UPD_VISTORIA_CONTRATO"/*idAcao*/, AcaoServices.UPDATE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Excluir vistoria"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAgdVistoriaContratados/*cdAgrupamento*/, "MOB.DEL_VISTORIA_CONTRATO"/*idAcao*/, AcaoServices.DELETE/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir autorização de vistoria"/*nmAcao*/, ""/*dsAcao*/,
						cdAgrupamentoAgdVistoriaContratados/*cdAgrupamento*/, "MOB.PRINT_VISTORIA_CONTRATO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
				
				
				// eDAT - Imprimir declarações
				int cdPermissoesFormularioEDAT = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,  " eDAT - Formulário eDAT" /*nmAgrupamento*/,
						"MOB_PRINT_EDAT_DECLARACAO"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
				saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Imprimir autorização declarações"/*nmAcao*/, ""/*dsAcao*/,
						cdPermissoesFormularioEDAT/*cdAgrupamento*/, "MOB.PRINT_EDAT_DECLARACAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			}
			
			connect.commit();
			connect.setAutoCommit(true);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
	}
	
	public static void initPermissoesTra(int cdSistema, int cdModulo, Connection connect){
		initPermissoesTra(cdSistema, cdModulo, null, connect);
	}
	
	public static void initPermissoesTra(int cdSistema, int cdModulo, File xmlPermissoes, Connection connect) {
		
		if(connect == null)
			connect = Conexao.conectar();
		
		try	{
			connect.setAutoCommit(false);
			
			if(xmlPermissoes != null){
			
				JAXBContext jaxbContext;
				try {
					jaxbContext = JAXBContext.newInstance(AgrupamentosAcao.class);
			
					Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
					AgrupamentosAcao agrupamentosAcao = (AgrupamentosAcao) jaxbUnmarshaller.unmarshal(xmlPermissoes);
					for(AgrupamentoAcao agrupamentoAcao : agrupamentosAcao.getAgrupamentoAcao()){
						
						ResultSetMap rsmAgrupamentoAcao = AgrupamentoAcaoServices.getAgrupamentoByIdModulo(agrupamentoAcao.getIdAgrupamento(), cdModulo, connect);
						if(rsmAgrupamentoAcao.next()){
							agrupamentoAcao.setCdAgrupamento(rsmAgrupamentoAcao.getInt("cd_agrupamento"));
							System.out.println(agrupamentoAcao.getNmAgrupamento());
							if(AgrupamentoAcaoDAO.update(agrupamentoAcao, connect) < 0){
								Conexao.rollback(connect);
								System.out.println("Erro ao atualizar agrupamento de ação");
								return;
							}else {
								System.out.println("Êxito ao atualizar agrupamento de ação");
							}
							
							for(Acao acao : agrupamentoAcao.getAcoes().getAcoes()){
								
								acao.setCdAgrupamento(agrupamentoAcao.getCdAgrupamento());
								
								Acao acaoBD = getByIdAcao(acao.getIdAcao(), connect);
								
								if(acaoBD != null && acaoBD.getCdAcao() >= 0){
									acao.setCdAcao(acaoBD.getCdAcao());
									if(AcaoDAO.update(acao, connect) < 0){
										Conexao.rollback(connect);
										System.out.println("Erro ao atualizar ação");
										return;
									}else {
										System.out.println("Êxito ao atualizar ação");
									}
								}else{
									if(AcaoDAO.insert(acao, connect) < 0){
										Conexao.rollback(connect);
										System.out.println("Erro ao inserir ação");
										return;
									}else {
										System.out.println("Êxito ao inserir ação");
									}
								}
							}
							
							
						}
						else{
							
							agrupamentoAcao.setCdAgrupamento(AgrupamentoAcaoDAO.insert(agrupamentoAcao, connect));
							
							if(agrupamentoAcao.getCdAgrupamento() <= 0){
								Conexao.rollback(connect);
								System.out.println("Erro ao inserir agrupamento de ação");
								return;
							}else {
								System.out.println("Êxito ao inserir agrupamento de ação");
							}
							
							for(Acao acao : agrupamentoAcao.getAcoes().getAcoes()){
								
								acao.setCdAgrupamento(agrupamentoAcao.getCdAgrupamento());
								
								Acao acaoBD = getByIdAcao(acao.getIdAcao(), connect);
								if(acaoBD != null){
									acao.setCdAcao(acaoBD.getCdAcao());
									if(AcaoDAO.update(acao, connect) < 0){
										Conexao.rollback(connect);
										System.out.println("Erro ao atualizar ação");
										return;
									}else {
										System.out.println("Êxito ao atualizao ação");
									}
								}
								else{
									if(AcaoDAO.insert(acao, connect) < 0){
										Conexao.rollback(connect);
										System.out.println("Erro ao inserir ação");
										return;
									}else {
										System.out.println("Êxito ao inserir ação");
									}
								}
							}
						}
						
					}
					
					System.out.println("Processo finalizado");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			connect.commit();
			connect.setAutoCommit(true);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		
	}
	
	public static void initPermissoesSeg(int cdSistema, int cdModulo, Connection connect){
		try	{
			connect.setAutoCommit(false);
			//MENU PRINCIPAL
			int cdAgrupamentoMenuPrincipal = saveAgrupamentoAcao( new AgrupamentoAcao( 0 /*cdAgrupamento*/,cdModulo/*cdModulo*/, cdSistema/*cdSistema*/,"Menu Principal" /*nmAgrupamento*/,
					"SEG_MENU_PRINCIPAL"/*idAgrupamento*/, ""/*dsAgrupamento*/, 0/*cdAgrupamentoSuperior*/, 1/*lgAtivo*/, 0/*nrOrdem*/), connect );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Mensagens"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMenuPrincipal/*cdAgrupamento*/, "ACESSO_MENSAGEM"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 1/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Grupo de Usuários	"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMenuPrincipal/*cdAgrupamento*/, "ACESSO_GRUPO_USUARIO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 2/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Parâmetros"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMenuPrincipal/*cdAgrupamento*/, "ACESSO_PARAMETROS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 3/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Usuários"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMenuPrincipal/*cdAgrupamento*/, "ACESSO_USUARIO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 4/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Acesso a Logs"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMenuPrincipal/*cdAgrupamento*/, "ACESSO_LOGS"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 5/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			saveAcao( new Acao(0/*cdAcao*/, cdModulo/*cdModulo*/, cdSistema/*cdSistema*/, "Editar permissões"/*nmAcao*/, ""/*dsAcao*/,
					cdAgrupamentoMenuPrincipal/*cdAgrupamento*/, "SEG.UPD_PERMISSAO"/*idAcao*/, AcaoServices.COMUM/*tpOrganizacao*/, 6/*nrOrdem*/, 0/*cdAcaoSuperior*/), connect  );
			
			connect.commit();
			connect.setAutoCommit(true);
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
	}
	public static int initTra(int cdSistema){
		Modulo _modulo = ModuloServices.getModuloById("tra");
		if(_modulo == null) {
			Modulo moduloTra = new Modulo();
			moduloTra.setCdModulo(29);
			moduloTra.setCdSistema(cdSistema);
			moduloTra.setNmModulo("transito");
			moduloTra.setIdModulo("tra");
			moduloTra.setNrVersao("1.0");
			moduloTra.setNrVersao("0");
			moduloTra.setLgAtivo(1);
			ModuloDAO.insert(moduloTra);
			return moduloTra.getCdModulo();
		}else {
			return _modulo.getCdModulo();
		}
		
	}
	
}