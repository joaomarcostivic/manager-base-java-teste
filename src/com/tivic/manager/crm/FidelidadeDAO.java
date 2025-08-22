package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class FidelidadeDAO{

	public static int insert(Fidelidade objeto) {
		return insert(objeto, null);
	}

	public static int insert(Fidelidade objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("crm_fidelidade", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFidelidade(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_fidelidade (cd_fidelidade,"+
			                                  "nm_fidelidade,"+
			                                  "txt_fidelidade,"+
			                                  "nm_unidade,"+
			                                  "vl_fator_conversao) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmFidelidade());
			pstmt.setString(3,objeto.getTxtFidelidade());
			pstmt.setString(4,objeto.getNmUnidade());
			pstmt.setFloat(5,objeto.getVlFatorConversao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Fidelidade objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Fidelidade objeto, int cdFidelidadeOld) {
		return update(objeto, cdFidelidadeOld, null);
	}

	public static int update(Fidelidade objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Fidelidade objeto, int cdFidelidadeOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_fidelidade SET cd_fidelidade=?,"+
												      		   "nm_fidelidade=?,"+
												      		   "txt_fidelidade=?,"+
												      		   "nm_unidade=?,"+
												      		   "vl_fator_conversao=? WHERE cd_fidelidade=?");
			pstmt.setInt(1,objeto.getCdFidelidade());
			pstmt.setString(2,objeto.getNmFidelidade());
			pstmt.setString(3,objeto.getTxtFidelidade());
			pstmt.setString(4,objeto.getNmUnidade());
			pstmt.setFloat(5,objeto.getVlFatorConversao());
			pstmt.setInt(6, cdFidelidadeOld!=0 ? cdFidelidadeOld : objeto.getCdFidelidade());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFidelidade) {
		return delete(cdFidelidade, null);
	}

	public static int delete(int cdFidelidade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_fidelidade WHERE cd_fidelidade=?");
			pstmt.setInt(1, cdFidelidade);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Fidelidade get(int cdFidelidade) {
		return get(cdFidelidade, null);
	}

	public static Fidelidade get(int cdFidelidade, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_fidelidade WHERE cd_fidelidade=?");
			pstmt.setInt(1, cdFidelidade);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Fidelidade(rs.getInt("cd_fidelidade"),
						rs.getString("nm_fidelidade"),
						rs.getString("txt_fidelidade"),
						rs.getString("nm_unidade"),
						rs.getFloat("vl_fator_conversao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_fidelidade");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM crm_fidelidade", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
