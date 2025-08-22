package com.tivic.manager.acd;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class UnidadeExecutoraInstituicaoServices {
	
	
	public static final int ST_VINCULACAO_INATIVA    = 0;
	public static final int ST_VINCULACAO_ATIVA      = 1;
	
	public static final String[] situacaoVinculacao = {"Inativa","Ativa"};
	
	public static Result save(UnidadeExecutoraInstituicao unidadeExecutoraInstituicao){
		return save(unidadeExecutoraInstituicao, null);
	}

	public static Result save(UnidadeExecutoraInstituicao unidadeExecutoraInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(unidadeExecutoraInstituicao==null)
				return new Result(-1, "Erro ao salvar. UnidadeExecutoraInstituicao é nulo");

			int retorno;
			UnidadeExecutoraInstituicao uei = UnidadeExecutoraInstituicaoDAO.get(
																		unidadeExecutoraInstituicao.getCdInstituicao(),
																		unidadeExecutoraInstituicao.getCdUnidadeExecutora(),
																		unidadeExecutoraInstituicao.getCdExercicio()
																	);
			if(uei==null){
				retorno = UnidadeExecutoraInstituicaoDAO.insert(unidadeExecutoraInstituicao, connect);
				unidadeExecutoraInstituicao.setCdInstituicao(retorno);
			}
			else {
				retorno = UnidadeExecutoraInstituicaoDAO.update(unidadeExecutoraInstituicao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "UNIDADEEXECUTORAINSTITUICAO", unidadeExecutoraInstituicao);
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
	public static Result remove(int cdInstituicao, int cdUnidadeExecutora, int cdExercicio){
		return remove(cdInstituicao, cdUnidadeExecutora, cdExercicio, false, null);
	}
	public static Result remove(int cdInstituicao, int cdUnidadeExecutora, int cdExercicio, boolean cascade){
		return remove(cdInstituicao, cdUnidadeExecutora, cdExercicio, cascade, null);
	}
	public static Result remove(int cdInstituicao, int cdUnidadeExecutora, int cdExercicio, boolean cascade, Connection connect){
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
			retorno = UnidadeExecutoraInstituicaoDAO.delete(cdInstituicao, cdUnidadeExecutora, cdExercicio, connect);
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

	public static ResultSetMap getAllByUnidadeExecutora( int cdUnidadeExecutora ) {
		return getAllByUnidadeExecutora(cdUnidadeExecutora, null);
	}
	
	public static ResultSetMap getAllByUnidadeExecutora(int cdUnidadeExecutora, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM "+
								" acd_unidade_executora_instituicao  A "+
								" JOIN acd_instituicao        B ON ( A.cd_instituicao = B.cd_instituicao ) "+
								" JOIN grl_pessoa_juridica    C ON ( B.cd_instituicao = C.cd_pessoa ) "+
								" JOIN grl_pessoa             D ON ( C.cd_pessoa = D.cd_pessoa ) "+
								" JOIN ctb_exercicio          E ON ( A.cd_exercicio = E.cd_exercicio ) "+
								" WHERE A.cd_unidade_executora = "+cdUnidadeExecutora );
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraInstituicaoServices.getAllByUnidadeExecutora: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraInstituicaoServices.getAllByUnidadeExecutora: " + e);
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
		return Search.find("SELECT * FROM acd_unidade_executora_instituicao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
