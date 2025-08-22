package com.tivic.manager.crt;

import java.sql.*;
import sol.util.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.manager.conexao.*;
import com.tivic.sol.connection.Conexao;

public class FechamentoDAO{

	public static int insert(Fechamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(Fechamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int code = Conexao.getSequenceCode("SCE_FECHAMENTO");
			pstmt = connect.prepareStatement("INSERT INTO SCE_FECHAMENTO (CD_FECHAMENTO," +
					                          "CD_EMPRESA,"+
			                                  "DT_FECHAMENTO,"+
			                                  "VL_FECHAMENTO,"+
			                                  "ST_FECHAMENTO) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2, objeto.getCdEmpresa());
			if(objeto.getDtFechamento()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtFechamento().getTimeInMillis()));
			pstmt.setFloat(4,objeto.getVlFechamento());
			pstmt.setInt(5,objeto.getStFechamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FechamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Fechamento objeto) {
		return update(objeto, null);
	}

	public static int update(Fechamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE SCE_FECHAMENTO SET CD_EMPRESA=?,DT_FECHAMENTO=?,"+
			                                  "VL_FECHAMENTO=?,"+
			                                  "ST_FECHAMENTO=? WHERE CD_FECHAMENTO=?");
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1, objeto.getCdEmpresa());
			if(objeto.getDtFechamento()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtFechamento().getTimeInMillis()));
			pstmt.setFloat(3,objeto.getVlFechamento());
			pstmt.setInt(4,objeto.getStFechamento());
			pstmt.setInt(5,objeto.getCdFechamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FechamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFechamento) {
		return delete(cdFechamento, null);
	}

	public static int delete(int cdFechamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM SCE_FECHAMENTO WHERE CD_FECHAMENTO=?");
			pstmt.setInt(1, cdFechamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FechamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Fechamento get(int cdFechamento) {
		return get(cdFechamento, null);
	}

	public static Fechamento get(int cdFechamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM SCE_FECHAMENTO WHERE CD_FECHAMENTO=?");
			pstmt.setInt(1, cdFechamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Fechamento(rs.getInt("CD_FECHAMENTO"),
						rs.getInt("CD_EMPRESA"),
						(rs.getTimestamp("DT_FECHAMENTO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_FECHAMENTO").getTime()),
						rs.getFloat("VL_FECHAMENTO"),
						rs.getInt("ST_FECHAMENTO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FechamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM SCE_FECHAMENTO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT * FROM SCE_FECHAMENTO", criterios, Conexao.conectar());
	}

}