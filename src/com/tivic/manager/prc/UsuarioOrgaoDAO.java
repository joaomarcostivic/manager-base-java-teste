package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class UsuarioOrgaoDAO{

	public static int insert(UsuarioOrgao objeto) {
		return insert(objeto, null);
	}

	public static int insert(UsuarioOrgao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
//			int code = Conexao.getSequenceCode("PRC_USUARIO_ORGAO", connect);
//			if (code <= 0) {
//				if (isConnectionNull)
//					Conexao.rollback(connect);
//				return -1;
//			}
//			objeto.setCdOrgao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PRC_USUARIO_ORGAO (CD_ORGAO,"+
			                                  "CD_USUARIO) VALUES (?, ?)");
			pstmt.setInt(1, objeto.getCdOrgao());
			pstmt.setInt(2, objeto.getCdUsuario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioOrgaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioOrgaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(UsuarioOrgao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(UsuarioOrgao objeto, int cdOrgaoOld, int cdUsuarioOld) {
		return update(objeto, cdOrgaoOld, cdUsuarioOld, null);
	}

	public static int update(UsuarioOrgao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(UsuarioOrgao objeto, int cdOrgaoOld, int cdUsuarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_USUARIO_ORGAO SET CD_ORGAO=?,"+
												      		   "CD_USUARIO=? WHERE CD_ORGAO=? AND CD_USUARIO=?");
			pstmt.setInt(1,objeto.getCdOrgao());
			pstmt.setInt(2,objeto.getCdUsuario());
			pstmt.setInt(3, cdOrgaoOld!=0 ? cdOrgaoOld : objeto.getCdOrgao());
			pstmt.setInt(4, cdUsuarioOld!=0 ? cdUsuarioOld : objeto.getCdUsuario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioOrgaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioOrgaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOrgao, int cdUsuario) {
		return delete(cdOrgao, cdUsuario, null);
	}

	public static int delete(int cdOrgao, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PRC_USUARIO_ORGAO WHERE CD_ORGAO=? AND CD_USUARIO=?");
			pstmt.setInt(1, cdOrgao);
			pstmt.setInt(2, cdUsuario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioOrgaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioOrgaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static UsuarioOrgao get(int cdOrgao, int cdUsuario) {
		return get(cdOrgao, cdUsuario, null);
	}

	public static UsuarioOrgao get(int cdOrgao, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_USUARIO_ORGAO WHERE CD_ORGAO=? AND CD_USUARIO=?");
			pstmt.setInt(1, cdOrgao);
			pstmt.setInt(2, cdUsuario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new UsuarioOrgao(rs.getInt("CD_ORGAO"),
						rs.getInt("CD_USUARIO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioOrgaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioOrgaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_USUARIO_ORGAO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioOrgaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioOrgaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<UsuarioOrgao> getList() {
		return getList(null);
	}

	public static ArrayList<UsuarioOrgao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<UsuarioOrgao> list = new ArrayList<UsuarioOrgao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				UsuarioOrgao obj = UsuarioOrgaoDAO.get(rsm.getInt("CD_ORGAO"), rsm.getInt("CD_USUARIO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioOrgaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM PRC_USUARIO_ORGAO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
