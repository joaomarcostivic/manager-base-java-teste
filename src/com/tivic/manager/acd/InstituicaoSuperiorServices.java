package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;

public class InstituicaoSuperiorServices {

	public static final int TP_PUBLICA = 0;
	public static final int TP_PRIVADA = 1;
	
	public static final int ST_INATIVA = 0;
	public static final int ST_ATIVA   = 1;
	
	public static Result save(InstituicaoSuperior instituicaoSuperior){
		return save(instituicaoSuperior, null);
	}

	public static Result save(InstituicaoSuperior instituicaoSuperior, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(instituicaoSuperior==null)
				return new Result(-1, "Erro ao salvar. InstituicaoSuperior é nulo");

			int retorno;
			if(instituicaoSuperior.getCdInstituicaoSuperior()==0){
				retorno = InstituicaoSuperiorDAO.insert(instituicaoSuperior, connect);
				instituicaoSuperior.setCdInstituicaoSuperior(retorno);
			}
			else {
				retorno = InstituicaoSuperiorDAO.update(instituicaoSuperior, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INSTITUICAOSUPERIOR", instituicaoSuperior);
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
	public static Result remove(int cdInstituicaoSuperior){
		return remove(cdInstituicaoSuperior, false, null);
	}
	public static Result remove(int cdInstituicaoSuperior, boolean cascade){
		return remove(cdInstituicaoSuperior, cascade, null);
	}
	public static Result remove(int cdInstituicaoSuperior, boolean cascade, Connection connect){
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
			retorno = InstituicaoSuperiorDAO.delete(cdInstituicaoSuperior, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
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
	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_superior");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoSuperiorServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoSuperiorServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_superior", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static InstituicaoSuperior getById(String idInstituicaoSuperior) {
		return getById(idInstituicaoSuperior, null);
	}
	
	public static InstituicaoSuperior getById(String idInstituicaoSuperior, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_superior WHERE id_instituicao_superior=?");
			pstmt.setString(1, idInstituicaoSuperior);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return InstituicaoSuperiorDAO.get(rs.getInt("cd_instituicao_superior"), connect);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoSuperiorServices.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoSuperiorServices.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
