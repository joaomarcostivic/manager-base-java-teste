package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class DistritoDAO{

	public static int insert(Distrito objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(Distrito objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			int code = Conexao.getSequenceCode("GRL_DISTRITO", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDistrito(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_distrito (cd_distrito,"+
			                                  "cd_cidade,"+
			                                  "nm_distrito,"+
			                                  "nr_cep,"+
			                                  "id_distrito) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdCidade()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCidade());
			pstmt.setString(3,objeto.getNmDistrito());
			pstmt.setString(4,objeto.getNrCep());
			pstmt.setString(5,objeto.getIdDistrito());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DistritoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DistritoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Distrito objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Distrito objeto, int cdDistritoOld, int cdCidadeOld) {
		return update(objeto, cdDistritoOld, cdCidadeOld, null);
	}

	public static int update(Distrito objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Distrito objeto, int cdDistritoOld, int cdCidadeOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_distrito SET cd_distrito=?,"+
												      		   "cd_cidade=?,"+
												      		   "nm_distrito=?,"+
												      		   "nr_cep=?,"+
												      		   "id_distrito=? WHERE cd_distrito=? AND cd_cidade=?");
			pstmt.setInt(1,objeto.getCdDistrito());
			pstmt.setInt(2,objeto.getCdCidade());
			pstmt.setString(3,objeto.getNmDistrito());
			pstmt.setString(4,objeto.getNrCep());
			pstmt.setString(5,objeto.getIdDistrito());
			pstmt.setInt(6, cdDistritoOld!=0 ? cdDistritoOld : objeto.getCdDistrito());
			pstmt.setInt(7, cdCidadeOld!=0 ? cdCidadeOld : objeto.getCdCidade());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DistritoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DistritoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDistrito, int cdCidade) {
		return delete(cdDistrito, cdCidade, null);
	}

	public static int delete(int cdDistrito, int cdCidade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_distrito WHERE cd_distrito=? AND cd_cidade=?");
			pstmt.setInt(1, cdDistrito);
			pstmt.setInt(2, cdCidade);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DistritoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DistritoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Distrito get(int cdDistrito, int cdCidade) {
		return get(cdDistrito, cdCidade, null);
	}

	public static Distrito get(int cdDistrito, int cdCidade, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_distrito WHERE cd_distrito=? AND cd_cidade=?");
			pstmt.setInt(1, cdDistrito);
			pstmt.setInt(2, cdCidade);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Distrito(rs.getInt("cd_distrito"),
						rs.getInt("cd_cidade"),
						rs.getString("nm_distrito"),
						rs.getString("nr_cep"),
						rs.getString("id_distrito"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DistritoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DistritoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_distrito");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DistritoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DistritoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_distrito", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
