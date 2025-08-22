package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class BairroDAO{

	public static int insert(Bairro objeto) {
		return insert(objeto, null);
	}

	public static int insert(Bairro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_bairro", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdBairro(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_bairro (cd_bairro,"+
			                                  "cd_distrito,"+
			                                  "cd_cidade,"+
			                                  "nm_bairro,"+
			                                  "id_bairro)"+ 
			                                  " VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdDistrito()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDistrito());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCidade());
			pstmt.setString(4,objeto.getNmBairro());
			pstmt.setString(5,objeto.getIdBairro());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BairroDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BairroDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Bairro objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Bairro objeto, int cdBairroOld) {
		return update(objeto, cdBairroOld, null);
	}

	public static int update(Bairro objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Bairro objeto, int cdBairroOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_bairro SET cd_bairro=?,"+
												      		   "cd_distrito=?,"+
												      		   "cd_cidade=?,"+
												      		   "nm_bairro=?,"+
												      		   "id_bairro=?,"+
												      		   "cd_regiao=? WHERE cd_bairro=?");
			pstmt.setInt(1,objeto.getCdBairro());
			if(objeto.getCdDistrito()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDistrito());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCidade());
			pstmt.setString(4,objeto.getNmBairro());
			pstmt.setString(5,objeto.getIdBairro());
			if(objeto.getCdRegiao()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdRegiao());
			pstmt.setInt(7, cdBairroOld!=0 ? cdBairroOld : objeto.getCdBairro());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BairroDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BairroDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdBairro) {
		return delete(cdBairro, null);
	}

	public static int delete(int cdBairro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_bairro WHERE cd_bairro=?");
			pstmt.setInt(1, cdBairro);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BairroDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BairroDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Bairro get(int cdBairro) {
		return get(cdBairro, null);
	}

	public static Bairro get(int cdBairro, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_bairro WHERE cd_bairro=?");
			pstmt.setInt(1, cdBairro);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Bairro(rs.getInt("cd_bairro"),
						rs.getInt("cd_distrito"),
						rs.getInt("cd_cidade"),
						rs.getString("nm_bairro"),
						rs.getString("id_bairro"),
						rs.getInt("cd_regiao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BairroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BairroDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_bairro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BairroDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BairroDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_bairro", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
