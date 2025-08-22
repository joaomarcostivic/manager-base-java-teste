package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class OrgaoCreditoDAO{

	public static int insert(OrgaoCredito objeto) {
		return insert(objeto, null);
	}

	public static int insert(OrgaoCredito objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int code = Conexao.getSequenceCode("grl_orgao_credito", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO grl_orgao_credito (cd_orgao,"+
			                                  "nm_orgao,"+
			                                  "lg_oficial,"+
			                                  "nr_telefone,"+
			                                  "nr_telefone2,"+
			                                  "nr_fax,"+
			                                  "nm_email,"+
			                                  "nm_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmOrgao());
			pstmt.setInt(3,objeto.getLgOficial());
			pstmt.setString(4,objeto.getNrTelefone());
			pstmt.setString(5,objeto.getNrTelefone2());
			pstmt.setString(6,objeto.getNrFax());
			pstmt.setString(7,objeto.getNmEmail());
			pstmt.setString(8,objeto.getNmUrl());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoCreditoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoCreditoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OrgaoCredito objeto) {
		return update(objeto, null);
	}

	public static int update(OrgaoCredito objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE grl_orgao_credito SET nm_orgao=?,"+
			                                  "lg_oficial=?,"+
			                                  "nr_telefone=?,"+
			                                  "nr_telefone2=?,"+
			                                  "nr_fax=?,"+
			                                  "nm_email=?,"+
			                                  "nm_url=? WHERE cd_orgao=?");
			pstmt.setString(1,objeto.getNmOrgao());
			pstmt.setInt(2,objeto.getLgOficial());
			pstmt.setString(3,objeto.getNrTelefone());
			pstmt.setString(4,objeto.getNrTelefone2());
			pstmt.setString(5,objeto.getNrFax());
			pstmt.setString(6,objeto.getNmEmail());
			pstmt.setString(7,objeto.getNmUrl());
			pstmt.setInt(8,objeto.getCdOrgao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoCreditoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoCreditoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOrgao) {
		return delete(cdOrgao, null);
	}

	public static int delete(int cdOrgao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM grl_orgao_credito WHERE cd_orgao=?");
			pstmt.setInt(1, cdOrgao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoCreditoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoCreditoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OrgaoCredito get(int cdOrgao) {
		return get(cdOrgao, null);
	}

	public static OrgaoCredito get(int cdOrgao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_orgao_credito WHERE cd_orgao=?");
			pstmt.setInt(1, cdOrgao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OrgaoCredito(rs.getInt("cd_orgao"),
						rs.getString("nm_orgao"),
						rs.getInt("lg_oficial"),
						rs.getString("nr_telefone"),
						rs.getString("nr_telefone2"),
						rs.getString("nr_fax"),
						rs.getString("nm_email"),
						rs.getString("nm_url"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoCreditoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoCreditoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_orgao_credito");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoCreditoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoCreditoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_orgao_credito", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
