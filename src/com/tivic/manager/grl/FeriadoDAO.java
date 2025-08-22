package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class FeriadoDAO{

	public static int insert(Feriado objeto) {
		return insert(objeto, null);
	}

	public static int insert(Feriado objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_feriado", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFeriado(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_feriado (cd_feriado,"+
			                                  "nm_feriado,"+
			                                  "dt_feriado,"+
			                                  "tp_feriado,"+
			                                  "id_feriado,"+
			                                  "cd_estado) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmFeriado());
			if(objeto.getDtFeriado()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtFeriado().getTimeInMillis()));
			pstmt.setInt(4,objeto.getTpFeriado());
			pstmt.setString(5,objeto.getIdFeriado());
			if(objeto.getCdEstado()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEstado());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FeriadoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FeriadoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Feriado objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Feriado objeto, int cdFeriadoOld) {
		return update(objeto, cdFeriadoOld, null);
	}

	public static int update(Feriado objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Feriado objeto, int cdFeriadoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_feriado SET cd_feriado=?,"+
												      		   "nm_feriado=?,"+
												      		   "dt_feriado=?,"+
												      		   "tp_feriado=?,"+
												      		   "id_feriado=?,"+
												      		   "cd_estado=? WHERE cd_feriado=?");
			pstmt.setInt(1,objeto.getCdFeriado());
			pstmt.setString(2,objeto.getNmFeriado());
			if(objeto.getDtFeriado()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtFeriado().getTimeInMillis()));
			pstmt.setInt(4,objeto.getTpFeriado());
			pstmt.setString(5,objeto.getIdFeriado());
			if(objeto.getCdEstado()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEstado());
			pstmt.setInt(7, cdFeriadoOld!=0 ? cdFeriadoOld : objeto.getCdFeriado());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FeriadoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FeriadoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFeriado) {
		return delete(cdFeriado, null);
	}

	public static int delete(int cdFeriado, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_feriado WHERE cd_feriado=?");
			pstmt.setInt(1, cdFeriado);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FeriadoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FeriadoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Feriado get(int cdFeriado) {
		return get(cdFeriado, null);
	}

	public static Feriado get(int cdFeriado, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_feriado WHERE cd_feriado=?");
			pstmt.setInt(1, cdFeriado);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Feriado(rs.getInt("cd_feriado"),
						rs.getString("nm_feriado"),
						(rs.getTimestamp("dt_feriado")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_feriado").getTime()),
						rs.getInt("tp_feriado"),
						rs.getString("id_feriado"),
						rs.getInt("cd_estado"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FeriadoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FeriadoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_feriado");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FeriadoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FeriadoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_feriado", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
