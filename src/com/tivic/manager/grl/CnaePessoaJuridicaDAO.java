package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class CnaePessoaJuridicaDAO{

	public static int insert(CnaePessoaJuridica objeto) {
		return insert(objeto, null);
	}

	public static int insert(CnaePessoaJuridica objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_cnae_pessoa_juridica (cd_pessoa,"+
			                                  "cd_cnae,"+
			                                  "lg_principal) VALUES (?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdCnae()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCnae());
			pstmt.setInt(3,objeto.getLgPrincipal());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CnaePessoaJuridicaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CnaePessoaJuridicaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CnaePessoaJuridica objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(CnaePessoaJuridica objeto, int cdPessoaOld, int cdCnaeOld) {
		return update(objeto, cdPessoaOld, cdCnaeOld, null);
	}

	public static int update(CnaePessoaJuridica objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(CnaePessoaJuridica objeto, int cdPessoaOld, int cdCnaeOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_cnae_pessoa_juridica SET cd_pessoa=?,"+
												      		   "cd_cnae=?,"+
												      		   "lg_principal=? WHERE cd_pessoa=? AND cd_cnae=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getCdCnae());
			pstmt.setInt(3,objeto.getLgPrincipal());
			pstmt.setInt(4, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(5, cdCnaeOld!=0 ? cdCnaeOld : objeto.getCdCnae());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CnaePessoaJuridicaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CnaePessoaJuridicaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoa, int cdCnae) {
		return delete(cdPessoa, cdCnae, null);
	}

	public static int delete(int cdPessoa, int cdCnae, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_cnae_pessoa_juridica WHERE cd_pessoa=? AND cd_cnae=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdCnae);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CnaePessoaJuridicaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CnaePessoaJuridicaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CnaePessoaJuridica get(int cdPessoa, int cdCnae) {
		return get(cdPessoa, cdCnae, null);
	}

	public static CnaePessoaJuridica get(int cdPessoa, int cdCnae, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_cnae_pessoa_juridica WHERE cd_pessoa=? AND cd_cnae=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdCnae);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CnaePessoaJuridica(rs.getInt("cd_pessoa"),
						rs.getInt("cd_cnae"),
						rs.getInt("lg_principal"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CnaePessoaJuridicaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CnaePessoaJuridicaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_cnae_pessoa_juridica");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CnaePessoaJuridicaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CnaePessoaJuridicaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_cnae_pessoa_juridica", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
