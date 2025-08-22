package com.tivic.manager.acd;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class InstituicaoCompartilhamentoServices {

	public static Result save(InstituicaoCompartilhamento instituicaoCompartilhamento){
		return save(instituicaoCompartilhamento, null);
	}

	public static Result save(InstituicaoCompartilhamento instituicaoCompartilhamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(instituicaoCompartilhamento==null)
				return new Result(-1, "Erro ao salvar. InstituicaoCompartilhamento é nulo");

			int retorno;
			if(InstituicaoCompartilhamentoDAO.get(instituicaoCompartilhamento.getCdInstituicao(), instituicaoCompartilhamento.getNrInstituicaoCompartilha()) == null){
				retorno = InstituicaoCompartilhamentoDAO.insert(instituicaoCompartilhamento, connect);
				instituicaoCompartilhamento.setCdInstituicao(retorno);
			}
			else {
				retorno = InstituicaoCompartilhamentoDAO.update(instituicaoCompartilhamento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INSTITUICAOCOMPARTILHAMENTO", instituicaoCompartilhamento);
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
	public static Result remove(int cdInstituicao, String nrInstituicaoCompartilha){
		return remove(cdInstituicao, nrInstituicaoCompartilha, false, null);
	}
	public static Result remove(int cdInstituicao, String nrInstituicaoCompartilha, boolean cascade){
		return remove(cdInstituicao, nrInstituicaoCompartilha, cascade, null);
	}
	public static Result remove(int cdInstituicao, String nrInstituicaoCompartilha, boolean cascade, Connection connect){
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
			retorno = InstituicaoCompartilhamentoDAO.delete(cdInstituicao, nrInstituicaoCompartilha, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_compartilhamento");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCompartilhamentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCompartilhamentoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_compartilhamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
