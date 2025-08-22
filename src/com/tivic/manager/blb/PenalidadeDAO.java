package com.tivic.manager.blb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class PenalidadeDAO{

	public static int insert(Penalidade objeto) {
		return insert(objeto, null);
	}

	public static int insert(Penalidade objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("blb_penalidade", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPenalidade(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO blb_penalidade (cd_penalidade,"+
			                                  "nm_penalidade,"+
			                                  "tp_penalidade,"+
			                                  "nr_dias_atraso,"+
			                                  "vl_penalidade,"+
			                                  "txt_advertencia,"+
			                                  "tp_incidencia,"+
			                                  "nr_dias_penalidade,"+
			                                  "nr_dias_multa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmPenalidade());
			pstmt.setInt(3,objeto.getTpPenalidade());
			pstmt.setInt(4,objeto.getNrDiasAtraso());
			pstmt.setFloat(5,objeto.getVlPenalidade());
			pstmt.setString(6,objeto.getTxtAdvertencia());
			pstmt.setInt(7,objeto.getTpIncidencia());
			pstmt.setInt(8,objeto.getNrDiasPenalidade());
			pstmt.setInt(9,objeto.getNrDiasMulta());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PenalidadeDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PenalidadeDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Penalidade objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Penalidade objeto, int cdPenalidadeOld) {
		return update(objeto, cdPenalidadeOld, null);
	}

	public static int update(Penalidade objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Penalidade objeto, int cdPenalidadeOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE blb_penalidade SET cd_penalidade=?,"+
												      		   "nm_penalidade=?,"+
												      		   "tp_penalidade=?,"+
												      		   "nr_dias_atraso=?,"+
												      		   "vl_penalidade=?,"+
												      		   "txt_advertencia=?,"+
												      		   "tp_incidencia=?,"+
												      		   "nr_dias_penalidade=?,"+
												      		   "nr_dias_multa=? WHERE cd_penalidade=?");
			pstmt.setInt(1,objeto.getCdPenalidade());
			pstmt.setString(2,objeto.getNmPenalidade());
			pstmt.setInt(3,objeto.getTpPenalidade());
			pstmt.setInt(4,objeto.getNrDiasAtraso());
			pstmt.setFloat(5,objeto.getVlPenalidade());
			pstmt.setString(6,objeto.getTxtAdvertencia());
			pstmt.setInt(7,objeto.getTpIncidencia());
			pstmt.setInt(8,objeto.getNrDiasPenalidade());
			pstmt.setInt(9,objeto.getNrDiasMulta());
			pstmt.setInt(10, cdPenalidadeOld!=0 ? cdPenalidadeOld : objeto.getCdPenalidade());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PenalidadeDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PenalidadeDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPenalidade) {
		return delete(cdPenalidade, null);
	}

	public static int delete(int cdPenalidade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM blb_penalidade WHERE cd_penalidade=?");
			pstmt.setInt(1, cdPenalidade);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PenalidadeDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PenalidadeDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Penalidade get(int cdPenalidade) {
		return get(cdPenalidade, null);
	}

	public static Penalidade get(int cdPenalidade, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM blb_penalidade WHERE cd_penalidade=?");
			pstmt.setInt(1, cdPenalidade);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Penalidade(rs.getInt("cd_penalidade"),
						rs.getString("nm_penalidade"),
						rs.getInt("tp_penalidade"),
						rs.getInt("nr_dias_atraso"),
						rs.getFloat("vl_penalidade"),
						rs.getString("txt_advertencia"),
						rs.getInt("tp_incidencia"),
						rs.getInt("nr_dias_penalidade"),
						rs.getInt("nr_dias_multa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PenalidadeDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PenalidadeDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM blb_penalidade");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PenalidadeDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PenalidadeDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Penalidade> getList() {
		return getList(null);
	}

	public static ArrayList<Penalidade> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Penalidade> list = new ArrayList<Penalidade>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Penalidade obj = PenalidadeDAO.get(rsm.getInt("cd_penalidade"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PenalidadeDAO.getList: " + e);
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
		return Search.find("SELECT * FROM blb_penalidade", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
