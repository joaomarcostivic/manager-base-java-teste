package com.tivic.manager.grl;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class PessoaFichaMedicaServices {
	
	/* situações da matricula */
	public static final int TP_SANGUE_A    		 = 0;
	public static final int TP_SANGUE_B 		 = 1;
	public static final int TP_SANGUE_AB  		 = 2;
	public static final int TP_SANGUE_O 		 = 3;
	
	public static final String[] tipoSangue = {"A", "B", "AB", "O"};
	
	/*Tipo de Raça*/
	public static final int TP_FATOR_RH_POS = 0;
	public static final int TP_FATOR_RH_NEG = 1;
	
	public static final String[] tipoFatorRh = {"+", "-"};
	
	public static Result save(PessoaFichaMedica pessoaFichaMedica){
		return save(pessoaFichaMedica, null);
	}

	public static Result save(PessoaFichaMedica pessoaFichaMedica, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(pessoaFichaMedica==null)
				return new Result(-1, "Erro ao salvar. PessoaFichaMedica é nulo");

			int retorno;
			if(pessoaFichaMedica.getCdPessoa()==0){
				retorno = PessoaFichaMedicaDAO.insert(pessoaFichaMedica, connect);
				pessoaFichaMedica.setCdPessoa(retorno);
			}
			else {
				retorno = PessoaFichaMedicaDAO.update(pessoaFichaMedica, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PESSOAFICHAMEDICA", pessoaFichaMedica);
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
	public static Result remove(int cdPessoa){
		return remove(cdPessoa, false, null);
	}
	public static Result remove(int cdPessoa, boolean cascade){
		return remove(cdPessoa, cascade, null);
	}
	public static Result remove(int cdPessoa, boolean cascade, Connection connect){
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
			retorno = PessoaFichaMedicaDAO.delete(cdPessoa, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_ficha_medica");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaFichaMedicaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaFichaMedicaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_pessoa_ficha_medica", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
