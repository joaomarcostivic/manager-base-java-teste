package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class CboPessoaFisicaDAO{

	public static int insert(CboPessoaFisica objeto) {
		return insert(objeto, null);
	}

	public static int insert(CboPessoaFisica objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_cbo_pessoa_fisica (cd_pessoa,"+
			                                  "cd_cbo,"+
			                                  "lg_principal) VALUES (?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdCbo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCbo());
			pstmt.setInt(3,objeto.getLgPrincipal());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CboPessoaFisicaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CboPessoaFisicaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CboPessoaFisica objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(CboPessoaFisica objeto, int cdPessoaOld, int cdCboOld) {
		return update(objeto, cdPessoaOld, cdCboOld, null);
	}

	public static int update(CboPessoaFisica objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(CboPessoaFisica objeto, int cdPessoaOld, int cdCboOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_cbo_pessoa_fisica SET cd_pessoa=?,"+
												      		   "cd_cbo=?,"+
												      		   "lg_principal=? WHERE cd_pessoa=? AND cd_cbo=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getCdCbo());
			pstmt.setInt(3,objeto.getLgPrincipal());
			pstmt.setInt(4, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(5, cdCboOld!=0 ? cdCboOld : objeto.getCdCbo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CboPessoaFisicaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CboPessoaFisicaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoa, int cdCbo) {
		return delete(cdPessoa, cdCbo, null);
	}

	public static int delete(int cdPessoa, int cdCbo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_cbo_pessoa_fisica WHERE cd_pessoa=? AND cd_cbo=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdCbo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CboPessoaFisicaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CboPessoaFisicaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CboPessoaFisica get(int cdPessoa, int cdCbo) {
		return get(cdPessoa, cdCbo, null);
	}

	public static CboPessoaFisica get(int cdPessoa, int cdCbo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_cbo_pessoa_fisica WHERE cd_pessoa=? AND cd_cbo=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdCbo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CboPessoaFisica(rs.getInt("cd_pessoa"),
						rs.getInt("cd_cbo"),
						rs.getInt("lg_principal"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CboPessoaFisicaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CboPessoaFisicaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_cbo_pessoa_fisica");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CboPessoaFisicaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CboPessoaFisicaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_cbo_pessoa_fisica", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
