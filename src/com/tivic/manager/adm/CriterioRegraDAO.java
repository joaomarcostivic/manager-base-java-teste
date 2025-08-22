package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class CriterioRegraDAO{

	public static int insert(CriterioRegra objeto) {
		return insert(objeto, null);
	}

	public static int insert(CriterioRegra objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_criterio_regra", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCriterio(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_criterio_regra (cd_criterio,"+
			                                  "nm_criterio,"+
			                                  "txt_sql) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmCriterio());
			pstmt.setString(3,objeto.getTxtSql());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CriterioRegraDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CriterioRegraDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CriterioRegra objeto) {
		return update(objeto, 0, null);
	}

	public static int update(CriterioRegra objeto, int cdCriterioOld) {
		return update(objeto, cdCriterioOld, null);
	}

	public static int update(CriterioRegra objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(CriterioRegra objeto, int cdCriterioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_criterio_regra SET cd_criterio=?,"+
												      		   "nm_criterio=?,"+
												      		   "txt_sql=? WHERE cd_criterio=?");
			pstmt.setInt(1,objeto.getCdCriterio());
			pstmt.setString(2,objeto.getNmCriterio());
			pstmt.setString(3,objeto.getTxtSql());
			pstmt.setInt(4, cdCriterioOld!=0 ? cdCriterioOld : objeto.getCdCriterio());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CriterioRegraDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CriterioRegraDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCriterio) {
		return delete(cdCriterio, null);
	}

	public static int delete(int cdCriterio, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_criterio_regra WHERE cd_criterio=?");
			pstmt.setInt(1, cdCriterio);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CriterioRegraDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CriterioRegraDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CriterioRegra get(int cdCriterio) {
		return get(cdCriterio, null);
	}

	public static CriterioRegra get(int cdCriterio, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_criterio_regra WHERE cd_criterio=?");
			pstmt.setInt(1, cdCriterio);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CriterioRegra(rs.getInt("cd_criterio"),
						rs.getString("nm_criterio"),
						rs.getString("txt_sql"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CriterioRegraDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CriterioRegraDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_criterio_regra");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CriterioRegraDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CriterioRegraDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_criterio_regra", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
