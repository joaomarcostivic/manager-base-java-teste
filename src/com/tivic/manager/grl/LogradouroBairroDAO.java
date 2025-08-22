package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class LogradouroBairroDAO{

	public static int insert(LogradouroBairro objeto) {
		return insert(objeto, null);
	}

	public static int insert(LogradouroBairro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_logradouro_bairro (cd_bairro,"+
			                                  "cd_logradouro) VALUES (?, ?)");
			if(objeto.getCdBairro()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdBairro());
			if(objeto.getCdLogradouro()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdLogradouro());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LogradouroBairroDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LogradouroBairroDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LogradouroBairro objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(LogradouroBairro objeto, int cdBairroOld, int cdLogradouroOld) {
		return update(objeto, cdBairroOld, cdLogradouroOld, null);
	}

	public static int update(LogradouroBairro objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(LogradouroBairro objeto, int cdBairroOld, int cdLogradouroOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_logradouro_bairro SET cd_bairro=?,"+
												      		   "cd_logradouro=? WHERE cd_bairro=? AND cd_logradouro=?");
			pstmt.setInt(1,objeto.getCdBairro());
			pstmt.setInt(2,objeto.getCdLogradouro());
			pstmt.setInt(3, cdBairroOld!=0 ? cdBairroOld : objeto.getCdBairro());
			pstmt.setInt(4, cdLogradouroOld!=0 ? cdLogradouroOld : objeto.getCdLogradouro());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LogradouroBairroDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LogradouroBairroDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdBairro, int cdLogradouro) {
		return delete(cdBairro, cdLogradouro, null);
	}

	public static int delete(int cdBairro, int cdLogradouro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_logradouro_bairro WHERE cd_bairro=? AND cd_logradouro=?");
			pstmt.setInt(1, cdBairro);
			pstmt.setInt(2, cdLogradouro);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LogradouroBairroDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LogradouroBairroDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LogradouroBairro get(int cdBairro, int cdLogradouro) {
		return get(cdBairro, cdLogradouro, null);
	}

	public static LogradouroBairro get(int cdBairro, int cdLogradouro, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_logradouro_bairro WHERE cd_bairro=? AND cd_logradouro=?");
			pstmt.setInt(1, cdBairro);
			pstmt.setInt(2, cdLogradouro);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LogradouroBairro(rs.getInt("cd_bairro"),
						rs.getInt("cd_logradouro"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LogradouroBairroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LogradouroBairroDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_logradouro_bairro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LogradouroBairroDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LogradouroBairroDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_logradouro_bairro", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
