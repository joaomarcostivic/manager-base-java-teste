package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class AitTranporteMovimentoDAO{

	public static int insert(AitTranporteMovimento objeto) {
		return insert(objeto, null);
	}

	public static int insert(AitTranporteMovimento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_ait_transporte_movimento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAitTranporteMovimento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_ait_transporte_movimento (cd_ait_tranporte_movimento,"+
			                                  "cd_ait_tranporte,"+
			                                  "dt_movimento,"+
			                                  "tp_status) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdAitTranporte()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAitTranporte());
			if(objeto.getDtMovimento()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtMovimento().getTimeInMillis()));
			pstmt.setInt(4,objeto.getTpStatus());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTranporteMovimentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTranporteMovimentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AitTranporteMovimento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AitTranporteMovimento objeto, int cdAitTranporteMovimentoOld) {
		return update(objeto, cdAitTranporteMovimentoOld, null);
	}

	public static int update(AitTranporteMovimento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AitTranporteMovimento objeto, int cdAitTranporteMovimentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_ait_transporte_movimento SET cd_ait_tranporte_movimento=?,"+
												      		   "cd_ait_tranporte=?,"+
												      		   "dt_movimento=?,"+
												      		   "tp_status=? WHERE cd_ait_tranporte_movimento=?");
			pstmt.setInt(1,objeto.getCdAitTranporteMovimento());
			if(objeto.getCdAitTranporte()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAitTranporte());
			if(objeto.getDtMovimento()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtMovimento().getTimeInMillis()));
			pstmt.setInt(4,objeto.getTpStatus());
			pstmt.setInt(5, cdAitTranporteMovimentoOld!=0 ? cdAitTranporteMovimentoOld : objeto.getCdAitTranporteMovimento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTranporteMovimentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTranporteMovimentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAitTranporteMovimento) {
		return delete(cdAitTranporteMovimento, null);
	}

	public static int delete(int cdAitTranporteMovimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_ait_transporte_movimento WHERE cd_ait_tranporte_movimento=?");
			pstmt.setInt(1, cdAitTranporteMovimento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTranporteMovimentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTranporteMovimentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitTranporteMovimento get(int cdAitTranporteMovimento) {
		return get(cdAitTranporteMovimento, null);
	}

	public static AitTranporteMovimento get(int cdAitTranporteMovimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_transporte_movimento WHERE cd_ait_tranporte_movimento=?");
			pstmt.setInt(1, cdAitTranporteMovimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AitTranporteMovimento(rs.getInt("cd_ait_tranporte_movimento"),
						rs.getInt("cd_ait_tranporte"),
						(rs.getTimestamp("dt_movimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_movimento").getTime()),
						rs.getInt("tp_status"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTranporteMovimentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTranporteMovimentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_transporte_movimento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTranporteMovimentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTranporteMovimentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AitTranporteMovimento> getList() {
		return getList(null);
	}

	public static ArrayList<AitTranporteMovimento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AitTranporteMovimento> list = new ArrayList<AitTranporteMovimento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AitTranporteMovimento obj = AitTranporteMovimentoDAO.get(rsm.getInt("cd_ait_tranporte_movimento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTranporteMovimentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_ait_transporte_movimento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
