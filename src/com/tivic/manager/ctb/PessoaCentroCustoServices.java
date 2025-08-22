package com.tivic.manager.ctb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class PessoaCentroCustoServices {
	public static Result save(PessoaCentroCusto pessoaCentroCusto){
		return save(pessoaCentroCusto, null);
	}
	
	public static Result save(PessoaCentroCusto pessoaCentroCusto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(pessoaCentroCusto==null)
				return new Result(-1, "Erro ao salvar. Centro de Custo é nulo");
			
			int retorno;
			if(pessoaCentroCusto.getCdPessoaCentroCusto()==0){
				retorno = PessoaCentroCustoDAO.insert(pessoaCentroCusto, connect);
				pessoaCentroCusto.setCdPessoaCentroCusto(retorno);
			}
			else {
				retorno = PessoaCentroCustoDAO.update(pessoaCentroCusto, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PESSOACENTROCUSTO", pessoaCentroCusto);
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
	
	public static Result remove(int cdPessoaCentroCusto){
		return remove(cdPessoaCentroCusto, false, null);
	}
	
	public static Result remove(int cdPessoaCentroCusto, boolean cascade){
		return remove(cdPessoaCentroCusto, cascade, null);
	}
	
	public static Result remove(int cdPessoaCentroCusto, boolean cascade, Connection connect){
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
				retorno = PessoaCentroCustoDAO.delete(cdPessoaCentroCusto, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este centro de custo está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Centro de Custo excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir centro de custo!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByPessoa(int cdPessoa) {
		return getAllByPessoa(cdPessoa, null);
	}

	public static ResultSetMap getAllByPessoa(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_centro_custo, B.nr_centro_custo, C.nm_area_direito " +
					" FROM ctb_pessoa_centro_custo A " +
					" LEFT OUTER JOIN ctb_centro_custo B ON (B.cd_centro_custo = A.cd_centro_custo) " +
					" LEFT OUTER JOIN prc_area_direito C ON (C.cd_area_direito = A.cd_area_direito) " +
					" WHERE A.cd_pessoa = ? " +
					" ORDER BY B.nr_centro_custo");
			pstmt.setInt(1, cdPessoa);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaCentroCustoDAO.getAllByPessoa: " + e);
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
		return Search.find("SELECT * FROM "+
				" CTB_PESSOA_CENTRO_CUSTO A "+
				" JOIN GRL_PESSOA                  B ON ( A.CD_PESSOA = B.CD_PESSOA ) "+
				" JOIN CTB_CENTRO_CUSTO            C ON ( A.CD_CENTRO_CUSTO = C.CD_CENTRO_CUSTO ) "+
				" LEFT JOIN CTB_PLANO_CENTRO_CUSTO D ON ( C.CD_PLANO_CENTRO_CUSTO = D.CD_PLANO_CENTRO_CUSTO ) "+
				" LEFT JOIN PRC_AREA_DIREITO       D2 ON ( A.CD_AREA_DIREITO = D2.CD_AREA_DIREITO ) "+
				" WHERE 1=1 ",
				"", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
