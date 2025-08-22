package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;


public class FaixaRendaDAO{

	public static int insert(FaixaRenda objeto) {
		return insert(objeto, null);
	}

	public static int insert(FaixaRenda objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_faixa_renda", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFaixaRenda(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_faixa_renda (cd_faixa_renda,"+
			                                  "nm_faixa_renda,"+
			                                  "id_faixa_renda,"+
			                                  "vl_inicial,"+
			                                  "vl_final,"+
			                                  "st_faixa_renda,"+
			                                  "tp_renda) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmFaixaRenda());
			pstmt.setString(3,objeto.getIdFaixaRenda());
			pstmt.setFloat(4,objeto.getVlInicial());
			pstmt.setFloat(5,objeto.getVlFinal());
			pstmt.setInt(6,objeto.getStFaixaRenda());
			pstmt.setInt(7,objeto.getTpRenda());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FaixaRendaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaRendaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FaixaRenda objeto) {
		return update(objeto, 0, null);
	}

	public static int update(FaixaRenda objeto, int cdFaixaRendaOld) {
		return update(objeto, cdFaixaRendaOld, null);
	}

	public static int update(FaixaRenda objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(FaixaRenda objeto, int cdFaixaRendaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_faixa_renda SET cd_faixa_renda=?,"+
												      		   "nm_faixa_renda=?,"+
												      		   "id_faixa_renda=?,"+
												      		   "vl_inicial=?,"+
												      		   "vl_final=?,"+
												      		   "st_faixa_renda=?,"+
												      		   "tp_renda=? WHERE cd_faixa_renda=?");
			pstmt.setInt(1,objeto.getCdFaixaRenda());
			pstmt.setString(2,objeto.getNmFaixaRenda());
			pstmt.setString(3,objeto.getIdFaixaRenda());
			pstmt.setFloat(4,objeto.getVlInicial());
			pstmt.setFloat(5,objeto.getVlFinal());
			pstmt.setInt(6,objeto.getStFaixaRenda());
			pstmt.setInt(7,objeto.getTpRenda());
			pstmt.setInt(8, cdFaixaRendaOld!=0 ? cdFaixaRendaOld : objeto.getCdFaixaRenda());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FaixaRendaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaRendaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFaixaRenda) {
		return delete(cdFaixaRenda, null);
	}

	public static int delete(int cdFaixaRenda, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_faixa_renda WHERE cd_faixa_renda=?");
			pstmt.setInt(1, cdFaixaRenda);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FaixaRendaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaRendaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FaixaRenda get(int cdFaixaRenda) {
		return get(cdFaixaRenda, null);
	}

	public static FaixaRenda get(int cdFaixaRenda, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_faixa_renda WHERE cd_faixa_renda=?");
			pstmt.setInt(1, cdFaixaRenda);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FaixaRenda(rs.getInt("cd_faixa_renda"),
						rs.getString("nm_faixa_renda"),
						rs.getString("id_faixa_renda"),
						rs.getFloat("vl_inicial"),
						rs.getFloat("vl_final"),
						rs.getInt("st_faixa_renda"),
						rs.getInt("tp_renda"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FaixaRendaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaRendaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_faixa_renda");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FaixaRendaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaRendaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_faixa_renda", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
