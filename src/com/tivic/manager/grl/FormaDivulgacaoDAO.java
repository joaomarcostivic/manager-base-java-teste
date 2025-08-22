package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class FormaDivulgacaoDAO{

	public static int insert(FormaDivulgacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(FormaDivulgacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_forma_divulgacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFormaDivulgacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_forma_divulgacao (cd_forma_divulgacao,"+
			                                  "nm_forma_divulgacao) VALUES (?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmFormaDivulgacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaDivulgacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FormaDivulgacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(FormaDivulgacao objeto, int cdFormaDivulgacaoOld) {
		return update(objeto, cdFormaDivulgacaoOld, null);
	}

	public static int update(FormaDivulgacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(FormaDivulgacao objeto, int cdFormaDivulgacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_forma_divulgacao SET cd_forma_divulgacao=?,"+
												      		   "nm_forma_divulgacao=? WHERE cd_forma_divulgacao=?");
			pstmt.setInt(1,objeto.getCdFormaDivulgacao());
			pstmt.setString(2,objeto.getNmFormaDivulgacao());
			pstmt.setInt(3, cdFormaDivulgacaoOld!=0 ? cdFormaDivulgacaoOld : objeto.getCdFormaDivulgacao());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaDivulgacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFormaDivulgacao) {
		return delete(cdFormaDivulgacao, null);
	}

	public static int delete(int cdFormaDivulgacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_forma_divulgacao WHERE cd_forma_divulgacao=?");
			pstmt.setInt(1, cdFormaDivulgacao);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaDivulgacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FormaDivulgacao get(int cdFormaDivulgacao) {
		return get(cdFormaDivulgacao, null);
	}

	public static FormaDivulgacao get(int cdFormaDivulgacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_forma_divulgacao WHERE cd_forma_divulgacao=?");
			pstmt.setInt(1, cdFormaDivulgacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FormaDivulgacao(rs.getInt("cd_forma_divulgacao"),
						rs.getString("nm_forma_divulgacao"));
			}
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaDivulgacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_forma_divulgacao ORDER BY nm_forma_divulgacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		return Search.find("SELECT * FROM grl_forma_divulgacao", "ORDER BY nm_forma_divulgacao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
