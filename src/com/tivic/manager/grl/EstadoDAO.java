package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class EstadoDAO{

	public static int insert(Estado objeto) {
		return insert(objeto, null);
	}

	public static int insert(Estado objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_estado", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdEstado(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_estado (cd_estado,"+
			                                  "cd_pais,"+
			                                  "nm_estado,"+
			                                  "sg_estado,"+
			                                  "cd_regiao,"+
			                                  "id_estado) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPais()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPais());
			pstmt.setString(3,objeto.getNmEstado());
			pstmt.setString(4,objeto.getSgEstado());
			if(objeto.getCdRegiao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdRegiao());
			pstmt.setString(6, objeto.getIdEstado());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstadoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Estado objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Estado objeto, int cdEstadoOld) {
		return update(objeto, cdEstadoOld, null);
	}

	public static int update(Estado objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Estado objeto, int cdEstadoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_estado SET cd_estado=?,"+
												      		   "cd_pais=?,"+
												      		   "nm_estado=?,"+
												      		   "sg_estado=?,"+
												      		   "cd_regiao=?,"+
												      		   "id_estado=? WHERE cd_estado=?");
			pstmt.setInt(1,objeto.getCdEstado());
			if(objeto.getCdPais()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPais());
			pstmt.setString(3,objeto.getNmEstado());
			pstmt.setString(4,objeto.getSgEstado());
			if(objeto.getCdRegiao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdRegiao());
			pstmt.setString(6, objeto.getIdEstado());
			pstmt.setInt(7, cdEstadoOld!=0 ? cdEstadoOld : objeto.getCdEstado());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstadoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEstado) {
		return delete(cdEstado, null);
	}

	public static int delete(int cdEstado, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_estado WHERE cd_estado=?");
			pstmt.setInt(1, cdEstado);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstadoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Estado get(int cdEstado) {
		return get(cdEstado, null);
	}

	public static Estado get(int cdEstado, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_estado WHERE cd_estado=?");
			pstmt.setInt(1, cdEstado);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Estado(rs.getInt("cd_estado"),
						rs.getInt("cd_pais"),
						rs.getString("nm_estado"),
						rs.getString("sg_estado"),
						rs.getInt("cd_regiao"),
						rs.getString("id_estado"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstadoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_estado ORDER BY sg_estado");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstadoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_estado", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
