package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TabelaCatEconomicaDAO{

	public static int insert(TabelaCatEconomica objeto) {
		return insert(objeto, null);
	}

	public static int insert(TabelaCatEconomica objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_tabela_cat_economica", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTabelaCatEconomica(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_tabela_cat_economica (cd_tabela_cat_economica,"+
			                                  "nm_tabela_cat_economica) VALUES (?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTabelaCatEconomica());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaCatEconomicaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaCatEconomicaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TabelaCatEconomica objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TabelaCatEconomica objeto, int cdTabelaCatEconomicaOld) {
		return update(objeto, cdTabelaCatEconomicaOld, null);
	}

	public static int update(TabelaCatEconomica objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TabelaCatEconomica objeto, int cdTabelaCatEconomicaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_tabela_cat_economica SET cd_tabela_cat_economica=?,"+
												      		   "nm_tabela_cat_economica=? WHERE cd_tabela_cat_economica=?");
			pstmt.setInt(1,objeto.getCdTabelaCatEconomica());
			pstmt.setString(2,objeto.getNmTabelaCatEconomica());
			pstmt.setInt(3, cdTabelaCatEconomicaOld!=0 ? cdTabelaCatEconomicaOld : objeto.getCdTabelaCatEconomica());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaCatEconomicaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaCatEconomicaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTabelaCatEconomica) {
		return delete(cdTabelaCatEconomica, null);
	}

	public static int delete(int cdTabelaCatEconomica, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_tabela_cat_economica WHERE cd_tabela_cat_economica=?");
			pstmt.setInt(1, cdTabelaCatEconomica);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaCatEconomicaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaCatEconomicaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TabelaCatEconomica get(int cdTabelaCatEconomica) {
		return get(cdTabelaCatEconomica, null);
	}

	public static TabelaCatEconomica get(int cdTabelaCatEconomica, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_tabela_cat_economica WHERE cd_tabela_cat_economica=?");
			pstmt.setInt(1, cdTabelaCatEconomica);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TabelaCatEconomica(rs.getInt("cd_tabela_cat_economica"),
						rs.getString("nm_tabela_cat_economica"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaCatEconomicaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaCatEconomicaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_tabela_cat_economica");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaCatEconomicaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaCatEconomicaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_tabela_cat_economica", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
