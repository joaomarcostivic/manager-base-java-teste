package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class UsuarioGrupoDAO{

	public static int insert(UsuarioGrupo objeto) {
		return insert(objeto, null);
	}

	public static int insert(UsuarioGrupo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PRC_USUARIO_GRUPO (CD_GRUPO_PROCESSO,"+
			                                  "CD_USUARIO) VALUES (?, ?)");
			pstmt.setInt(1, objeto.getCdGrupoProcesso());
			pstmt.setInt(2, objeto.getCdUsuario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioGrupoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioGrupoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(UsuarioGrupo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(UsuarioGrupo objeto, int cdGrupoProcessoOld, int cdUsuarioOld) {
		return update(objeto, cdGrupoProcessoOld, cdUsuarioOld, null);
	}

	public static int update(UsuarioGrupo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(UsuarioGrupo objeto, int cdGrupoProcessoOld, int cdUsuarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_USUARIO_GRUPO SET CD_GRUPO_PROCESSO=?,"+
												      		   "CD_USUARIO=? WHERE CD_GRUPO_PROCESSO=? AND CD_USUARIO=?");
			pstmt.setInt(1,objeto.getCdGrupoProcesso());
			pstmt.setInt(2,objeto.getCdUsuario());
			pstmt.setInt(3, cdGrupoProcessoOld!=0 ? cdGrupoProcessoOld : objeto.getCdGrupoProcesso());
			pstmt.setInt(4, cdUsuarioOld!=0 ? cdUsuarioOld : objeto.getCdUsuario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioGrupoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioGrupoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdGrupoProcesso, int cdUsuario) {
		return delete(cdGrupoProcesso, cdUsuario, null);
	}

	public static int delete(int cdGrupoProcesso, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PRC_USUARIO_GRUPO WHERE CD_GRUPO_PROCESSO=? AND CD_USUARIO=?");
			pstmt.setInt(1, cdGrupoProcesso);
			pstmt.setInt(2, cdUsuario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioGrupoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioGrupoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static UsuarioGrupo get(int cdGrupoProcesso, int cdUsuario) {
		return get(cdGrupoProcesso, cdUsuario, null);
	}

	public static UsuarioGrupo get(int cdGrupoProcesso, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_USUARIO_GRUPO WHERE CD_GRUPO_PROCESSO=? AND CD_USUARIO=?");
			pstmt.setInt(1, cdGrupoProcesso);
			pstmt.setInt(2, cdUsuario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new UsuarioGrupo(rs.getInt("CD_GRUPO_PROCESSO"),
						rs.getInt("CD_USUARIO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioGrupoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioGrupoDAO.get: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_USUARIO_GRUPO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioGrupoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioGrupoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<UsuarioGrupo> getList() {
		return getList(null);
	}

	public static ArrayList<UsuarioGrupo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<UsuarioGrupo> list = new ArrayList<UsuarioGrupo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				UsuarioGrupo obj = UsuarioGrupoDAO.get(rsm.getInt("CD_GRUPO_PROCESSO"), rsm.getInt("CD_USUARIO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioGrupoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM PRC_USUARIO_GRUPO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
