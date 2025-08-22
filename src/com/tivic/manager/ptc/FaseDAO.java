package com.tivic.manager.ptc;

import java.sql.*;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class FaseDAO{

	public static int insert(Fase objeto) {
		return insert(objeto, null);
	}

	public static int insert(Fase objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ptc_fase", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFase(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_fase (cd_fase,"
																+ "nm_fase,"
																+ "id_fase,"
																+ "cd_setor,"
																+ "cd_empresa,"
																+ "nr_fase_externa,"
																+ "nr_ordem) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmFase());
			pstmt.setString(3,objeto.getIdFase());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdSetor());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEmpresa());
			pstmt.setString(6, objeto.getNrFaseExterna());
			pstmt.setInt(7,objeto.getNrOrdem());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Fase objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Fase objeto, int cdFaseOld) {
		return update(objeto, cdFaseOld, null);
	}

	public static int update(Fase objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Fase objeto, int cdFaseOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_fase SET cd_fase=?,"+
												      		   "nm_fase=?,"+
												      		   "id_fase=?,"+
												      		   "cd_setor=?,"+
												      		   "cd_empresa=?,"+
												      		   "nr_fase_externa=?,"+
												      		   "nr_ordem=?"+
												      		   " WHERE cd_fase=?");
			pstmt.setInt(1,objeto.getCdFase());
			pstmt.setString(2,objeto.getNmFase());
			pstmt.setString(3,objeto.getIdFase());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdSetor());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEmpresa());
			pstmt.setString(6, objeto.getNrFaseExterna());
			pstmt.setInt(7,objeto.getNrOrdem());
			pstmt.setInt(8, cdFaseOld!=0 ? cdFaseOld : objeto.getCdFase());
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFase) {
		return delete(cdFase, null);
	}

	public static int delete(int cdFase, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_fase WHERE cd_fase=?");
			pstmt.setInt(1, cdFase);
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Fase get(int cdFase) {
		return get(cdFase, null);
	}

	public static Fase get(int cdFase, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_fase WHERE cd_fase=?");
			pstmt.setInt(1, cdFase);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Fase(rs.getInt("cd_fase"),
						rs.getString("nm_fase"),
						rs.getString("id_fase"),
						rs.getInt("cd_setor"),
						rs.getInt("cd_empresa"),
						rs.getString("nr_fase_externa"),
						rs.getInt("nr_ordem"));
			}
			else{
				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		try {
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM ptc_fase ORDER BY nm_fase").executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		return Search.find("SELECT * FROM ptc_fase", "ORDER BY nm_fase", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
