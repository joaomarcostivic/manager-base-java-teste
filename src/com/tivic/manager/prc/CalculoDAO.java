package com.tivic.manager.prc;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import sol.dao.Util;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class CalculoDAO{

	public static int insert(Calculo objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(Calculo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_processo");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdProcesso()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "nr_parcela");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("prc_calculo", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO prc_calculo (cd_processo,"+
			                                  "nr_parcela,"+
			                                  "dt_parcela,"+
			                                  "ds_coeficiente,"+
			                                  "vl_parcela,"+
			                                  "vl_corrigido,"+
			                                  "pr_juros) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdProcesso());
			pstmt.setInt(2, code);
			if(objeto.getDtParcela()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtParcela().getTimeInMillis()));
			pstmt.setString(4,objeto.getDsCoeficiente());
			pstmt.setFloat(5,objeto.getVlParcela());
			pstmt.setFloat(6,objeto.getVlCorrigido());
			pstmt.setFloat(7,objeto.getPrJuros());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CalculoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CalculoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Calculo objeto) {
		return update(objeto, null);
	}

	public static int update(Calculo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE prc_calculo SET dt_parcela=?,"+
			                                  "ds_coeficiente=?,"+
			                                  "vl_parcela=?,"+
			                                  "vl_corrigido=?,"+
			                                  "pr_juros=? WHERE cd_processo=? AND nr_parcela=?");
			if(objeto.getDtParcela()==null)
				pstmt.setNull(1, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(1,new Timestamp(objeto.getDtParcela().getTimeInMillis()));
			pstmt.setString(2,objeto.getDsCoeficiente());
			pstmt.setFloat(3,objeto.getVlParcela());
			pstmt.setFloat(4,objeto.getVlCorrigido());
			pstmt.setFloat(5,objeto.getPrJuros());
			pstmt.setInt(6,objeto.getCdProcesso());
			pstmt.setInt(7,objeto.getNrParcela());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CalculoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CalculoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProcesso, int nrParcela) {
		return delete(cdProcesso, nrParcela, null);
	}

	public static int delete(int cdProcesso, int nrParcela, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM prc_calculo WHERE cd_processo=? AND nr_parcela=?");
			pstmt.setInt(1, cdProcesso);
			pstmt.setInt(2, nrParcela);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CalculoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CalculoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Calculo get(int cdProcesso, int nrParcela) {
		return get(cdProcesso, nrParcela, null);
	}

	public static Calculo get(int cdProcesso, int nrParcela, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_calculo WHERE cd_processo=? AND nr_parcela=?");
			pstmt.setInt(1, cdProcesso);
			pstmt.setInt(2, nrParcela);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Calculo(rs.getInt("cd_processo"),
						rs.getInt("nr_parcela"),
						(rs.getTimestamp("dt_parcela")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_parcela").getTime()),
						rs.getString("ds_coeficiente"),
						rs.getFloat("vl_parcela"),
						rs.getFloat("vl_corrigido"),
						rs.getFloat("pr_juros"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CalculoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CalculoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_calculo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CalculoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CalculoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM prc_calculo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
