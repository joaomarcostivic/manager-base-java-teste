package com.tivic.manager.mcr;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.Util;

public class GrupoSolidarioDAO{

	public static int insert(GrupoSolidario objeto) {
		return insert(objeto, null);
	}

	public static int insert(GrupoSolidario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int code = Conexao.getSequenceCode("mcr_grupo_solidario", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO mcr_grupo_solidario (cd_grupo_solidario,"+
			                                  "nm_grupo_solidario,"+
			                                  "dt_constituicao,"+
			                                  "id_grupo_solidario," +
			                                  "cd_empresa) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmGrupoSolidario());
			if(objeto.getDtConstituicao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtConstituicao().getTimeInMillis()));
			pstmt.setString(4,objeto.getIdGrupoSolidario());
			pstmt.setInt(5,objeto.getCdEmpresa());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(GrupoSolidario objeto) {
		return update(objeto, null);
	}

	public static int update(GrupoSolidario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE mcr_grupo_solidario SET nm_grupo_solidario=?,"+
			                                  "dt_constituicao=?,"+
			                                  "id_grupo_solidario=?," +
			                                  "cd_empresa=? WHERE cd_grupo_solidario=?");
			pstmt.setString(1,objeto.getNmGrupoSolidario());
			if(objeto.getDtConstituicao()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtConstituicao().getTimeInMillis()));
			pstmt.setString(3,objeto.getIdGrupoSolidario());
			pstmt.setInt(4,objeto.getCdEmpresa());
			pstmt.setInt(5,objeto.getCdGrupoSolidario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdGrupoSolidario) {
		return delete(cdGrupoSolidario, null);
	}

	public static int delete(int cdGrupoSolidario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM mcr_grupo_solidario WHERE cd_grupo_solidario=?");
			pstmt.setInt(1, cdGrupoSolidario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static GrupoSolidario get(int cdGrupoSolidario) {
		return get(cdGrupoSolidario, null);
	}

	public static GrupoSolidario get(int cdGrupoSolidario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_grupo_solidario WHERE cd_grupo_solidario=?");
			pstmt.setInt(1, cdGrupoSolidario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new GrupoSolidario(rs.getInt("cd_grupo_solidario"),
						rs.getString("nm_grupo_solidario"),
						(rs.getTimestamp("dt_constituicao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_constituicao").getTime()),
						rs.getString("id_grupo_solidario"),
						rs.getInt("cd_empresa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_grupo_solidario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM mcr_grupo_solidario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
