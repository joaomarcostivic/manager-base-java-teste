package com.tivic.manager.seg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.security.Group;
import sol.security.GroupServices;
import sol.security.User;
import sol.util.Result;

public class GrupoServices implements GroupServices {

	public static Result save(Grupo grupo){
		return save(grupo, null);
	}
	
	public static Result save(Grupo grupo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(grupo==null)
				return new Result(-1, "Erro ao salvar. Grupo é nulo");
			
			int retorno;
			if(grupo.getCdGrupo()==0){
				retorno = GrupoDAO.insert(grupo, connect);
				grupo.setCdGrupo(retorno);
			}
			else {
				retorno = GrupoDAO.update(grupo, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "GRUPO", grupo);
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
	
	public static Result remove(int cdGrupo){
		return remove(cdGrupo, false, null);
	}
	
	public static Result remove(int cdGrupo, boolean cascade){
		return remove(cdGrupo, cascade, null);
	}
	
	public static Result remove(int cdGrupo, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			if(cascade){
				PreparedStatement pstmt = connect.prepareStatement("DELETE " +
						"FROM seg_usuario_grupo " +
						"WHERE cd_grupo = ?");
				pstmt.setInt(1, cdGrupo);
				retorno = pstmt.executeUpdate();
				
				if(retorno>0) {
					pstmt = connect.prepareStatement("DELETE " +
							"FROM seg_grupo_permissao_objeto " +
							"WHERE cd_grupo = ?");
					pstmt.setInt(1, cdGrupo);
					retorno = pstmt.executeUpdate();
				}
				
				if(retorno>0) {
					pstmt = connect.prepareStatement("DELETE " +
							"FROM seg_grupo_permissao_acao " +
							"WHERE cd_grupo = ?");
					pstmt.setInt(1, cdGrupo);
					retorno = pstmt.executeUpdate();
				}
			}
				
			if(!cascade || retorno>0)
				retorno = GrupoDAO.delete(cdGrupo, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este grupo está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Grupo excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir grupo!");
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_grupo ORDER BY nm_grupo");
			return new ResultSetMap(pstmt.executeQuery());
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


	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM seg_grupo ", " ORDER BY nm_grupo ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	
	
	
	public int insertGroup(Group group) {
		return GrupoDAO.insert(new Grupo(0, group.getName()));
	}

	public int updateGroup(Group group) {
		return GrupoDAO.update(new Grupo(group.getId(), group.getName()));
	}

	public int deleteGroup(int idGroup) {
		return GrupoDAO.delete(idGroup);
	}

	public Group[] getGroups() {
		ResultSetMap rsm = GrupoDAO.getAll();
		Group[] groups = new Group[rsm==null ? 0 : rsm.size()];
		for (int i = 0; rsm!=null && rsm.next(); i++)
			groups[i] = new Group(rsm.getInt("CD_GRUPO"), rsm.getString("NM_GRUPO"));
		return groups;
	}

	public Group[] findGroups(int idGroup, String nameGroup) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		if (idGroup > 0)
			criterios.add(new ItemComparator("CD_GRUPO", Integer.toString(idGroup), ItemComparator.EQUAL, Types.INTEGER));
		if (nameGroup != null && !nameGroup.trim().equals(""))
			criterios.add(new ItemComparator("NM_GRUPO", nameGroup.trim().toUpperCase(), ItemComparator.LIKE_BEGIN, Types.VARCHAR));
		ResultSetMap rsm = GrupoDAO.find(criterios);
		Group[] groups = rsm==null || rsm.size()==0 ? null : new Group[rsm.size()];
		for (int i=0; rsm!=null && rsm.next(); i++)
			groups[i] = new Group(rsm.getInt("CD_GRUPO"), rsm.getString("NM_GRUPO"));
		return groups;
	}

	public User[] getUsersOfGroup(int idGroup) {
		return getUsersOfGroup(idGroup, null);
	}

	public static User[] getUsersOfGroup(int idGroup, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM SEG_USUARIO_GRUPO A " +
															   "JOIN SEG_USUARIO B ON (A.CD_USUARIO = B.CD_USUARIO) " +
															   "LEFT OUTER JOIN GRL_PESSOA C ON (B.CD_PESSOA = C.CD_PESSOA) " +
															   "WHERE A.CD_GRUPO = ?");
			pstmt.setInt(1, idGroup);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			User[] users = rsm==null || rsm.size()==0 ? null : new User[rsm.size()];
			for (int i=0; rsm!=null && rsm.next(); i++)
				users[i] = new User(rsm.getInt("CD_USUARIO"), rsm.getString("NM_PESSOA"), rsm.getString("NM_LOGIN"), rsm.getString("NM_SENHA"));
			return users;
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

	public static ResultSetMap getUsuariosOfGrupo(int cdGrupo) {
		return getUsuariosOfGrupo(cdGrupo, null);
	}
	public static ResultSetMap getUsuariosOfGrupo(int cdGrupo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.*, C.nm_pessoa, C.nm_pessoa AS nm_usuario " +
					                                           "FROM seg_usuario_grupo A " +
															   "JOIN seg_usuario B ON (A.cd_usuario = B.cd_usuario) " +
															   "LEFT OUTER JOIN grl_pessoa C ON (B.cd_pessoa = C.cd_pessoa) " +
															   "WHERE A.cd_grupo = "+cdGrupo);
			return new ResultSetMap(pstmt.executeQuery());
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

	public static ResultSetMap getPermissoesAcao(int cdGrupo, int cdSistema, int cdModulo, int cdAgrupamento) {
		return getPermissoesAcao(cdGrupo, cdSistema, cdModulo, cdAgrupamento, null);
	}
	public static ResultSetMap getPermissoesAcao(int cdGrupo, int cdSistema, int cdModulo, int cdAgrupamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
				   "SELECT A.*, B.*, C.*, D.NM_AGRUPAMENTO, D.DS_AGRUPAMENTO, D.ID_AGRUPAMENTO, E.CD_GRUPO, E.NM_GRUPO, " +
				   "            (SELECT COUNT(*) FROM SEG_GRUPO_PERMISSAO_ACAO X " +
				   "			 WHERE X.CD_GRUPO = " +cdGrupo+
				   "			   AND X.CD_SISTEMA = A.CD_SISTEMA " +
				   "			   AND X.CD_MODULO = A.CD_MODULO " +
				   "			   AND X.CD_ACAO = A.CD_ACAO) AS LG_PERMISSAO " +
				   "FROM SEG_ACAO A " +
				   "JOIN SEG_MODULO B ON (A.CD_MODULO = B.CD_MODULO AND A.CD_SISTEMA = B.CD_SISTEMA) " +
				   "JOIN SEG_SISTEMA C ON (B.CD_SISTEMA = C.CD_SISTEMA) " +
				   "LEFT OUTER JOIN SEG_AGRUPAMENTO_ACAO D ON (A.CD_AGRUPAMENTO = D.CD_AGRUPAMENTO AND A.CD_MODULO = D.CD_MODULO AND A.CD_SISTEMA = D.CD_SISTEMA) " +
				   "JOIN SEG_GRUPO E ON (E.CD_GRUPO = " +cdGrupo+") " +
				   "WHERE 1=1 " +
				   (cdModulo==0 ? "" : " AND A.CD_MODULO = "+cdModulo) +
				   (cdSistema==0 ? "" : " AND A.CD_SISTEMA = "+cdSistema) +
				   (cdAgrupamento==0 ? "" : " AND A.CD_AGRUPAMENTO = "+cdAgrupamento)+
				   " ORDER BY D.nm_agrupamento, A.TP_ORGANIZACAO DESC, A.cd_acao_superior, A.nr_ordem");
			return new ResultSetMap(pstmt.executeQuery());
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


	
	public Result adicionarPermissaoAcao(int cdGrupo, int cdAcao, int cdModulo, int cdSistema) {
		int retorno = addPermissaoAcao(cdGrupo, cdAcao, cdModulo, cdSistema, null);
		return new Result(retorno, retorno>0 ? "Permissão incluída com sucesso" : "Erro ao incluir permissão");
	}
	
	public int addPermissaoAcao(int cdGrupo, int cdAcao, int cdModulo, int cdSistema) {
		return addPermissaoAcao(cdGrupo, cdAcao, cdModulo, cdSistema, null);
	}

	public int addPermissaoAcao(int cdGrupo, int cdAcao, int cdModulo, int cdSistema, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					     "SELECT * FROM seg_grupo_permissao_acao " +
						 "WHERE CD_GRUPO = " +cdGrupo+
						 "  AND CD_SISTEMA = " +cdSistema+
						 "  AND CD_MODULO = " +cdModulo+
						 "  AND CD_ACAO = "+cdAcao);
			if (!pstmt.executeQuery().next()) {
				pstmt = connect.prepareStatement("INSERT INTO SEG_GRUPO_PERMISSAO_ACAO (CD_GRUPO, CD_SISTEMA, CD_MODULO, CD_ACAO) VALUES(?, ?, ?, ?)");
				pstmt.setInt(1, cdGrupo);
				pstmt.setInt(2, cdSistema);
				pstmt.setInt(3, cdModulo);
				pstmt.setInt(4, cdAcao);
				pstmt.execute();
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
	
	public Result adicionarPermissoesAcoes(int cdGrupo, int[] cdsAcao, int cdModulo, int cdSistema) {
		return adicionarPermissoesAcoes(cdGrupo, cdsAcao, cdModulo, cdSistema, null);
	}
	
	public Result adicionarPermissoesAcoes(int cdGrupo, int[] cdsAcao, int cdModulo, int cdSistema, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			for (int i = 0; i < cdsAcao.length; i++) {
				retorno = addPermissaoAcao(cdGrupo, cdsAcao[i], cdModulo, cdSistema, connect);
				if(retorno<=0)
					break;
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Erro ao incluir permissões");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, retorno>0 ? "Permissões incluídas com sucesso" : "Erro ao incluir permissões");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao incluir permissões");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	
	public Result removerPermissaoAcao(int cdGrupo, int cdAcao, int cdModulo, int cdSistema) {
		int retorno = dropPermissaoAcao(cdGrupo, cdAcao, cdModulo, cdSistema, null);
		return new Result(retorno, retorno>0 ? "Permissão removida com sucesso" : "Erro ao remover permissão");
	}
	
	public int dropPermissaoAcao(int cdGrupo, int cdAcao, int cdModulo, int cdSistema) {
		return dropPermissaoAcao(cdGrupo, cdAcao, cdModulo, cdSistema, null);
	}
	public int dropPermissaoAcao(int cdGrupo, int cdAcao, int cdModulo, int cdSistema, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
				     "SELECT * FROM seg_grupo_permissao_acao " +
					 "WHERE CD_GRUPO = " +cdGrupo+
					 "  AND CD_SISTEMA = " +cdSistema+
					 "  AND CD_MODULO = " +cdModulo+
					 "  AND CD_ACAO = "+cdAcao);
			if (pstmt.executeQuery().next()) {
				pstmt = connect.prepareStatement(
						                 "DELETE FROM SEG_GRUPO_PERMISSAO_ACAO " +
										 "WHERE CD_GRUPO   = " + cdGrupo +
										 "  AND CD_SISTEMA = " + cdSistema +
										 "  AND CD_MODULO  = " + cdModulo +
										 "  AND CD_ACAO    = " + cdAcao);
				pstmt.executeUpdate();
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
	
	public Result removerPermissoesAcoes(int cdGrupo, int[] cdsAcao, int cdModulo, int cdSistema) {
		return removerPermissoesAcoes(cdGrupo, cdsAcao, cdModulo, cdSistema, null);
	}
	
	public Result removerPermissoesAcoes(int cdGrupo, int[] cdsAcao, int cdModulo, int cdSistema, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			for (int i = 0; i < cdsAcao.length; i++) {
				retorno = dropPermissaoAcao(cdGrupo, cdsAcao[i], cdModulo, cdSistema, connect);
				if(retorno<=0)
					break;
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Erro ao remover permissões");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, retorno>0 ? "Permissões removidas com sucesso" : "Erro ao remover permissões");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao remover permissões");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	

	public static int delete(int cdGrupo) {
		return delete(cdGrupo, null);
	}

	public static int delete(int cdGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			PreparedStatement pstmt = connect.prepareStatement("DELETE " +
					"FROM seg_usuario_grupo " +
					"WHERE cd_grupo = ?");
			pstmt.setInt(1, cdGrupo);
			pstmt.execute();

			pstmt = connect.prepareStatement("DELETE " +
					"FROM seg_grupo_permissao_objeto " +
					"WHERE cd_grupo = ?");
			pstmt.setInt(1, cdGrupo);
			pstmt.execute();

			pstmt = connect.prepareStatement("DELETE " +
					"FROM seg_grupo_permissao_acao " +
					"WHERE cd_grupo = ?");
			pstmt.setInt(1, cdGrupo);
			pstmt.execute();

			if (GrupoDAO.delete(cdGrupo, connect) <= 0) {
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
			System.err.println("Erro! GrupoServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}