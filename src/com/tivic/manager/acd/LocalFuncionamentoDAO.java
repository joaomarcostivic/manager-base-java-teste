package com.tivic.manager.acd;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class LocalFuncionamentoDAO{

	public static int insert(LocalFuncionamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(LocalFuncionamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_local_funcionamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLocalFuncionamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_local_funcionamento (cd_local_funcionamento,"+
			                                  "nm_local_funcionamento,"+
			                                  "id_local_funcionamento) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmLocalFuncionamento());
			pstmt.setString(3,objeto.getIdLocalFuncionamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalFuncionamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalFuncionamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LocalFuncionamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(LocalFuncionamento objeto, int cdLocalFuncionamentoOld) {
		return update(objeto, cdLocalFuncionamentoOld, null);
	}

	public static int update(LocalFuncionamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(LocalFuncionamento objeto, int cdLocalFuncionamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_local_funcionamento SET cd_local_funcionamento=?,"+
												      		   "nm_local_funcionamento=?,"+
												      		   "id_local_funcionamento=? WHERE cd_local_funcionamento=?");
			pstmt.setInt(1,objeto.getCdLocalFuncionamento());
			pstmt.setString(2,objeto.getNmLocalFuncionamento());
			pstmt.setString(3,objeto.getIdLocalFuncionamento());
			pstmt.setInt(4, cdLocalFuncionamentoOld!=0 ? cdLocalFuncionamentoOld : objeto.getCdLocalFuncionamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalFuncionamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalFuncionamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLocalFuncionamento) {
		return delete(cdLocalFuncionamento, null);
	}

	public static int delete(int cdLocalFuncionamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_local_funcionamento WHERE cd_local_funcionamento=?");
			pstmt.setInt(1, cdLocalFuncionamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalFuncionamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalFuncionamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LocalFuncionamento get(int cdLocalFuncionamento) {
		return get(cdLocalFuncionamento, null);
	}

	public static LocalFuncionamento get(int cdLocalFuncionamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_local_funcionamento WHERE cd_local_funcionamento=?");
			pstmt.setInt(1, cdLocalFuncionamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LocalFuncionamento(rs.getInt("cd_local_funcionamento"),
						rs.getString("nm_local_funcionamento"),
						rs.getString("id_local_funcionamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalFuncionamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalFuncionamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_local_funcionamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalFuncionamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalFuncionamentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_local_funcionamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
