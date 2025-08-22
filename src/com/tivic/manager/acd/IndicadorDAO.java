package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class IndicadorDAO{

	public static int insert(Indicador objeto) {
		return insert(objeto, null);
	}

	public static int insert(Indicador objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_indicador", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdIndicador(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_indicador (cd_indicador,"+
			                                  "nm_indicador,"+
			                                  "txt_indicador,"+
			                                  "id_indicador,"+
			                                  "tp_indicador) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmIndicador());
			pstmt.setString(3,objeto.getTxtIndicador());
			pstmt.setString(4,objeto.getIdIndicador());
			pstmt.setInt(5,objeto.getTpIndicador());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IndicadorDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Indicador objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Indicador objeto, int cdIndicadorOld) {
		return update(objeto, cdIndicadorOld, null);
	}

	public static int update(Indicador objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Indicador objeto, int cdIndicadorOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_indicador SET cd_indicador=?,"+
												      		   "nm_indicador=?,"+
												      		   "txt_indicador=?,"+
												      		   "id_indicador=?,"+
												      		   "tp_indicador=? WHERE cd_indicador=?");
			pstmt.setInt(1,objeto.getCdIndicador());
			pstmt.setString(2,objeto.getNmIndicador());
			pstmt.setString(3,objeto.getTxtIndicador());
			pstmt.setString(4,objeto.getIdIndicador());
			pstmt.setInt(5,objeto.getTpIndicador());
			pstmt.setInt(6, cdIndicadorOld!=0 ? cdIndicadorOld : objeto.getCdIndicador());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IndicadorDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdIndicador) {
		return delete(cdIndicador, null);
	}

	public static int delete(int cdIndicador, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_indicador WHERE cd_indicador=?");
			pstmt.setInt(1, cdIndicador);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IndicadorDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Indicador get(int cdIndicador) {
		return get(cdIndicador, null);
	}

	public static Indicador get(int cdIndicador, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_indicador WHERE cd_indicador=?");
			pstmt.setInt(1, cdIndicador);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Indicador(rs.getInt("cd_indicador"),
						rs.getString("nm_indicador"),
						rs.getString("txt_indicador"),
						rs.getString("id_indicador"),
						rs.getInt("tp_indicador"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IndicadorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_indicador");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IndicadorDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Indicador> getList() {
		return getList(null);
	}

	public static ArrayList<Indicador> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Indicador> list = new ArrayList<Indicador>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Indicador obj = IndicadorDAO.get(rsm.getInt("cd_indicador"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_indicador", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}