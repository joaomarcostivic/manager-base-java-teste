package com.tivic.manager.alm;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class EntradaAdicaoServices {

	public static Result save(EntradaAdicao entradaAdicao){
		return save(entradaAdicao, null);
	}
	
	public static Result save(EntradaAdicao entradaAdicao, Connection connect){
		return save(entradaAdicao, 0, 0, connect);
	}
	
	public static Result save(EntradaAdicao entradaAdicao, float vlIpi, float vlIi){
		return save(entradaAdicao, vlIpi, vlIi, null);
	}
	
	public static Result save(EntradaAdicao entradaAdicao, float vlIpi, float vlIi, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(entradaAdicao==null)
				return new Result(-1, "Erro ao salvar. EntradaAdicao é nulo");

			if(vlIpi > 0){
				entradaAdicao.setVlBaseCalculoIpi(vlIpi * 100 / entradaAdicao.getPrAliquotaIpi());
			}
			
			if(vlIi > 0){
				entradaAdicao.setVlBaseCalculoIi(vlIi * 100 / entradaAdicao.getPrAliquotaIi());
			}
			
			
			int retorno;
			if(entradaAdicao.getCdEntradaAdicao()==0){
				retorno = EntradaAdicaoDAO.insert(entradaAdicao, connect);
				entradaAdicao.setCdEntradaAdicao(retorno);
			}
			else {
				retorno = EntradaAdicaoDAO.update(entradaAdicao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ENTRADAADICAO", entradaAdicao);
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
	public static Result remove(int cdEntradaAdicao, int cdEntradaDeclaracaoImportacao, int cdDocumentoEntrada){
		return remove(cdEntradaAdicao, cdEntradaDeclaracaoImportacao, cdDocumentoEntrada, false, null);
	}
	public static Result remove(int cdEntradaAdicao, int cdEntradaDeclaracaoImportacao, int cdDocumentoEntrada, boolean cascade){
		return remove(cdEntradaAdicao, cdEntradaDeclaracaoImportacao, cdDocumentoEntrada, cascade, null);
	}
	public static Result remove(int cdEntradaAdicao, int cdEntradaDeclaracaoImportacao, int cdDocumentoEntrada, boolean cascade, Connection connect){
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
			retorno = EntradaAdicaoDAO.delete(cdEntradaAdicao, cdEntradaDeclaracaoImportacao, cdDocumentoEntrada, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_entrada_adicao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EntradaAdicaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaAdicaoServices.getAll: " + e);
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
		return Search.find("SELECT A.*, B.nm_ncm, B.nr_ncm FROM alm_entrada_adicao A " + 
				"			  JOIN grl_ncm B ON (A.cd_ncm = B.cd_ncm) ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
