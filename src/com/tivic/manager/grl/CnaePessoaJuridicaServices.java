package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import sol.dao.ResultSetMap;
import com.tivic.sol.connection.Conexao;


public class CnaePessoaJuridicaServices {

	public static int setCnaePrincipal(int cdPessoa, int cdCnae) {
		return setCnaePrincipal(cdPessoa, cdCnae, null);
	}

	public static int setCnaePrincipal(int cdPessoa, int cdCnae, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			PreparedStatement pstmt = connection.prepareStatement("UPDATE grl_cnae_pessoa_juridica " +
					"SET lg_principal = 0 " +
					"WHERE cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.executeUpdate();
			pstmt = connection.prepareStatement("UPDATE grl_cnae_pessoa_juridica " +
					"SET lg_principal = 1 " +
					"WHERE cd_pessoa = ? " +
					"  AND cd_cnae = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdCnae);
			pstmt.executeUpdate();
			if (isConnectionNull)
				connection.commit();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	
	public static int insert(CnaePessoaJuridica objeto) {
		return insert(objeto, null);
	}

	public static int insert(CnaePessoaJuridica objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if(CnaePessoaJuridicaDAO.insert(objeto, connect)<=0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			
			if(objeto.getLgPrincipal()==1){
				setCnaePrincipal(objeto.getCdPessoa(), objeto.getCdCnae(), connect);
			}
			if (isConnectionNull) {
				connect.commit();
			}
			return 1;
		}
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
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
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if(CnaePessoaJuridicaDAO.update(objeto, cdPessoaOld, cdCnaeOld, connect)<=0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			
			if(objeto.getLgPrincipal()==1){
				setCnaePrincipal(objeto.getCdPessoa(), objeto.getCdCnae(), connect);
			}
			else{
				ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_cnae_pessoa_juridica WHERE cd_pessoa = " + objeto.getCdPessoa() + " AND lg_principal = 1").executeQuery());
				if(!rsm.next()){
					setCnaePrincipal(objeto.getCdPessoa(), objeto.getCdCnae(), connect);
				}
			}
			if (isConnectionNull) {
				connect.commit();
			}
			return 1;
		}
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
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
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			CnaePessoaJuridica cnaePessoaJuridica = CnaePessoaJuridicaDAO.get(cdPessoa, cdCnae, connect);
			if(cnaePessoaJuridica.getLgPrincipal()==1){
				ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_cnae_pessoa_juridica WHERE cd_pessoa = " + cdPessoa + " AND cd_cnae <> " + cdCnae).executeQuery());
				if(rsm.next()){
					setCnaePrincipal(rsm.getInt("cd_pessoa"), rsm.getInt("cd_cnae"), connect);
				}
			}
			if(CnaePessoaJuridicaDAO.delete(cdPessoa, cdCnae, connect) <= 0){
				Conexao.rollback(connect);
				return -1;
			}
			if(isConnectionNull)
				connect.commit();
			
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
	
	public static ResultSetMap getAllCnaePessoaJuridica(int cdPessoa) {
		return getAllCnaePessoaJuridica(cdPessoa, null);
	}

	public static ResultSetMap getAllCnaePessoaJuridica(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			if (cdPessoa <= 0) {
				System.err.println("Erro! CnaeServices.getAllPessoaJuridicaCnae: código da Pessoa não informado.");
				return null;
			}
			PreparedStatement pstmt = connect.prepareStatement(
				"SELECT A.*, " +
				"	B.nm_cnae, B.nr_cnae, B.sg_cnae, B.id_cnae, " +
				"	C.nm_pessoa, D.nm_razao_social, D.nr_cnpj " +
				"FROM grl_cnae_pessoa_juridica A, grl_cnae B, grl_pessoa C, grl_pessoa_juridica D " +
				"WHERE (A.cd_cnae = B.cd_cnae) " +
				"	AND (A.cd_pessoa = C.cd_pessoa) " +
				"	AND (C.cd_pessoa = D.cd_pessoa) " +
				"	AND A.cd_pessoa = " + cdPessoa +
				" ORDER BY C.nm_pessoa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CnaeServices.getAllPessoaJuridicaCnae: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
