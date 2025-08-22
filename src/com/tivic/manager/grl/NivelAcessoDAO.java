package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class NivelAcessoDAO{

	public static int insert(NivelAcesso objeto) {
		return insert(objeto, null);
	}

	public static int insert(NivelAcesso objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_nivel_acesso", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdNivelAcesso(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_nivel_acesso (cd_nivel_acesso,"+
			                                  "nm_nivel_acesso) VALUES (?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmNivelAcesso());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NivelAcessoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NivelAcessoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(NivelAcesso objeto) {
		return update(objeto, 0, null);
	}

	public static int update(NivelAcesso objeto, int cdNivelAcessoOld) {
		return update(objeto, cdNivelAcessoOld, null);
	}

	public static int update(NivelAcesso objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(NivelAcesso objeto, int cdNivelAcessoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_nivel_acesso SET cd_nivel_acesso=?,"+
												      		   "nm_nivel_acesso=? WHERE cd_nivel_acesso=?");
			pstmt.setInt(1,objeto.getCdNivelAcesso());
			pstmt.setString(2,objeto.getNmNivelAcesso());
			pstmt.setInt(3, cdNivelAcessoOld!=0 ? cdNivelAcessoOld : objeto.getCdNivelAcesso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NivelAcessoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NivelAcessoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdNivelAcesso) {
		return delete(cdNivelAcesso, null);
	}

	public static int delete(int cdNivelAcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_nivel_acesso WHERE cd_nivel_acesso=?");
			pstmt.setInt(1, cdNivelAcesso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NivelAcessoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NivelAcessoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static NivelAcesso get(int cdNivelAcesso) {
		return get(cdNivelAcesso, null);
	}

	public static NivelAcesso get(int cdNivelAcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_nivel_acesso WHERE cd_nivel_acesso=?");
			pstmt.setInt(1, cdNivelAcesso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new NivelAcesso(rs.getInt("cd_nivel_acesso"),
						rs.getString("nm_nivel_acesso"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NivelAcessoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NivelAcessoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_nivel_acesso");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NivelAcessoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NivelAcessoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator>  criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator>  criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_nivel_acesso", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
