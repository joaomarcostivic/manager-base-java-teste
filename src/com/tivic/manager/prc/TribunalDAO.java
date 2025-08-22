package com.tivic.manager.prc;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TribunalDAO{

	public static int insert(Tribunal objeto) {
		return insert(objeto, null);
	}

	public static int insert(Tribunal objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("prc_tribunal", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if(objeto.getCdTribunal()<=0)
				objeto.setCdTribunal(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_tribunal (cd_tribunal,"+
			                                  "nm_tribunal,"+
			                                  "tp_segmento,"+
			                                  "id_tribunal,"+
			                                  "sg_tribunal) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdTribunal());
			pstmt.setString(2,objeto.getNmTribunal());
			pstmt.setInt(3,objeto.getTpSegmento());
			pstmt.setString(4,objeto.getIdTribunal());
			pstmt.setString(5,objeto.getSgTribunal());
			pstmt.executeUpdate();
			return objeto.getCdTribunal();
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TribunalDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TribunalDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Tribunal objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Tribunal objeto, int cdTribunalOld) {
		return update(objeto, cdTribunalOld, null);
	}

	public static int update(Tribunal objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Tribunal objeto, int cdTribunalOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_tribunal SET cd_tribunal=?,"+
												      		   "nm_tribunal=?,"+
												      		   "tp_segmento=?,"+
												      		   "id_tribunal=?,"+
												      		   "sg_tribunal=? WHERE cd_tribunal=?");
			pstmt.setInt(1,objeto.getCdTribunal());
			pstmt.setString(2,objeto.getNmTribunal());
			pstmt.setInt(3,objeto.getTpSegmento());
			pstmt.setString(4,objeto.getIdTribunal());
			pstmt.setString(5,objeto.getSgTribunal());
			pstmt.setInt(6, cdTribunalOld!=0 ? cdTribunalOld : objeto.getCdTribunal());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TribunalDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TribunalDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTribunal) {
		return delete(cdTribunal, null);
	}

	public static int delete(int cdTribunal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_tribunal WHERE cd_tribunal=?");
			pstmt.setInt(1, cdTribunal);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TribunalDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TribunalDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Tribunal get(int cdTribunal) {
		return get(cdTribunal, null);
	}

	public static Tribunal get(int cdTribunal, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_tribunal WHERE cd_tribunal=?");
			pstmt.setInt(1, cdTribunal);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Tribunal(rs.getInt("cd_tribunal"),
						rs.getString("nm_tribunal"),
						rs.getInt("tp_segmento"),
						rs.getString("id_tribunal"),
						rs.getString("sg_tribunal"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TribunalDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TribunalDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_tribunal");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TribunalDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TribunalDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM prc_tribunal", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
