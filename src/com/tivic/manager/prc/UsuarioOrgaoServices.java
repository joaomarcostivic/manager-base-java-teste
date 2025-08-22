package com.tivic.manager.prc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class UsuarioOrgaoServices {
	public static Result save(UsuarioOrgao orgao){
		return save(orgao, null);
	}
	
	public static Result save(UsuarioOrgao orgao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(orgao==null)
				return new Result(-1, "Erro ao salvar. Dados enviados são nulos");
			
			int retorno;
			
			if(UsuarioOrgaoDAO.get(orgao.getCdOrgao(), orgao.getCdUsuario(), connect) == null){
				retorno = UsuarioOrgaoDAO.insert(orgao, connect);
			}
			else {
				retorno = UsuarioOrgaoDAO.update(orgao, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "USUARIOORGAO", orgao);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdOrgao, int cdUsuario){
		return remove(cdOrgao, cdUsuario, false, null);
	}
	
	public static Result remove(int cdOrgao, int cdUsuario, boolean cascade){
		return remove(cdOrgao, cdUsuario, cascade, null);
	}
	
	public static Result remove(int cdOrgao, int cdUsuario, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
				
			if(!cascade || retorno>0)
				retorno = UsuarioOrgaoDAO.delete(cdOrgao, cdUsuario, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllByUsuario(int cdUsuario) {
		return getAllByUsuario(cdUsuario, null);
	}

	public static ResultSetMap getAllByUsuario(int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_orgao, B.cd_responsavel, C.nm_login, D.cd_pessoa, D.nm_pessoa " +
					"FROM prc_usuario_orgao A " +
					"LEFT OUTER JOIN prc_orgao B ON (B.cd_orgao = A.cd_orgao) " +
					"LEFT OUTER JOIN seg_usuario C ON (C.cd_usuario = A.cd_usuario) " +
					"LEFT OUTER JOIN grl_pessoa D ON (B.cd_pessoa = D.cd_pessoa) " +
					"WHERE A.cd_usuario = ? " +
					" ORDER BY B.nm_orgao");
			pstmt.setInt(1, cdUsuario);
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
	
	public static ResultSetMap getAllByOrgao(int cdOrgao) {
		return getAllByOrgao(cdOrgao, null);
	}

	public static ResultSetMap getAllByOrgao(int cdOrgao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_orgao, C.nm_login, D.cd_pessoa, D.nm_pessoa, D.nm_email " +
											" FROM prc_usuario_orgao A " +
											" LEFT OUTER JOIN prc_orgao B ON (B.cd_orgao = A.cd_orgao) " +
											" LEFT OUTER JOIN seg_usuario C ON (C.cd_usuario = A.cd_usuario) " +
											" LEFT OUTER JOIN grl_pessoa D ON (C.cd_pessoa = D.cd_pessoa)" +
											" WHERE A.cd_orgao = ? " +
											" ORDER BY C.nm_login");
			pstmt.setInt(1, cdOrgao);
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
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			String sql = "SELECT A.*, B.cd_orgao, B.nm_orgao, C.nm_login, D.cd_pessoa, D.nm_pessoa, " +
						" D.nm_email, D.cd_pessoa, D.cd_pessoa as cd_responsavel, D.nm_pessoa as nm_responsavel " +
						" FROM prc_usuario_orgao A " +
						" LEFT OUTER JOIN prc_orgao B ON (B.cd_orgao = A.cd_orgao) " +
						" LEFT OUTER JOIN seg_usuario C ON (C.cd_usuario = A.cd_usuario) " +
						" LEFT OUTER JOIN grl_pessoa D ON (C.cd_pessoa = D.cd_pessoa) ";
			
			return Search.find(sql, criterios, connect);
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}		
	}
}
