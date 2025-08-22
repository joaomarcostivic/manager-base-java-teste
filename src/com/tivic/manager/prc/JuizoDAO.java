package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class JuizoDAO{

	public static int insert(Juizo objeto) {
		return insert(objeto, null);
	}

	public static int insert(Juizo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("PRC_JUIZO", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdJuizo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PRC_JUIZO (CD_JUIZO,"+
			                                  "NM_JUIZO,"+
			                                  "TP_JUIZO,"+
			                                  "ID_JUIZO,"+
			                                  "NR_JUIZO,"+
			                                  "SG_JUIZO,"+
			                                  "TP_INSTANCIA) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmJuizo());
			pstmt.setInt(3,objeto.getTpJuizo());
			pstmt.setString(4,objeto.getIdJuizo());
			pstmt.setString(5,objeto.getNrJuizo());
			pstmt.setString(6,objeto.getSgJuizo());
			pstmt.setInt(7,objeto.getTpInstancia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! JuizoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! JuizoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Juizo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Juizo objeto, int cdJuizoOld) {
		return update(objeto, cdJuizoOld, null);
	}

	public static int update(Juizo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Juizo objeto, int cdJuizoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_JUIZO SET CD_JUIZO=?,"+
												      		   "NM_JUIZO=?,"+
												      		   "TP_JUIZO=?,"+
												      		   "ID_JUIZO=?,"+
												      		   "NR_JUIZO=?,"+
												      		   "SG_JUIZO=?,"+
												      		   "TP_INSTANCIA=? WHERE CD_JUIZO=?");
			pstmt.setInt(1,objeto.getCdJuizo());
			pstmt.setString(2,objeto.getNmJuizo());
			pstmt.setInt(3,objeto.getTpJuizo());
			pstmt.setString(4,objeto.getIdJuizo());
			pstmt.setString(5,objeto.getNrJuizo());
			pstmt.setString(6,objeto.getSgJuizo());
			pstmt.setInt(7,objeto.getTpInstancia());
			pstmt.setInt(8, cdJuizoOld!=0 ? cdJuizoOld : objeto.getCdJuizo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! JuizoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! JuizoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdJuizo) {
		return delete(cdJuizo, null);
	}

	public static int delete(int cdJuizo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PRC_JUIZO WHERE CD_JUIZO=?");
			pstmt.setInt(1, cdJuizo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! JuizoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! JuizoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Juizo get(int cdJuizo) {
		return get(cdJuizo, null);
	}

	public static Juizo get(int cdJuizo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_JUIZO WHERE CD_JUIZO=?");
			pstmt.setInt(1, cdJuizo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Juizo(rs.getInt("CD_JUIZO"),
						rs.getString("NM_JUIZO"),
						rs.getInt("TP_JUIZO"),
						rs.getString("ID_JUIZO"),
						rs.getString("NR_JUIZO"),
						rs.getString("SG_JUIZO"),
						rs.getInt("TP_INSTANCIA"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! JuizoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! JuizoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_JUIZO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! JuizoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! JuizoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Juizo> getList() {
		return getList(null);
	}

	public static ArrayList<Juizo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Juizo> list = new ArrayList<Juizo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Juizo obj = JuizoDAO.get(rsm.getInt("CD_JUIZO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! JuizoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM PRC_JUIZO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
