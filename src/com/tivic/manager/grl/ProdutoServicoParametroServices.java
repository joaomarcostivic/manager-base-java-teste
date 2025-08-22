package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.manager.seg.AuthData;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class ProdutoServicoParametroServices {

	public static Result save(ProdutoServicoParametro produtoServicoParametro){
		return save(produtoServicoParametro, null, null);
	}

	public static Result save(ProdutoServicoParametro produtoServicoParametro, AuthData authData){
		return save(produtoServicoParametro, authData, null);
	}

	public static Result save(ProdutoServicoParametro produtoServicoParametro, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(produtoServicoParametro==null)
				return new Result(-1, "Erro ao salvar. ProdutoServicoParametro é nulo");

			int retorno;
			ProdutoServicoParametro parametroTmp = ProdutoServicoParametroDAO.get( produtoServicoParametro.getCdEmpresa(),
																					produtoServicoParametro.getCdProdutoServico(), connect);
			if(parametroTmp==null){
				retorno = ProdutoServicoParametroDAO.insert(produtoServicoParametro, connect);
			}else {
				retorno = ProdutoServicoParametroDAO.update(produtoServicoParametro, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PRODUTOSERVICOPARAMETRO", produtoServicoParametro);
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
	public static Result remove(int cdEmpresa, int cdProdutoServico){
		return remove(cdEmpresa, cdProdutoServico, false, null, null);
	}
	public static Result remove(int cdEmpresa, int cdProdutoServico, boolean cascade){
		return remove(cdEmpresa, cdProdutoServico, cascade, null, null);
	}
	public static Result remove(int cdEmpresa, int cdProdutoServico, boolean cascade, AuthData authData){
		return remove(cdEmpresa, cdProdutoServico, cascade, authData, null);
	}
	public static Result remove(int cdEmpresa, int cdProdutoServico, boolean cascade, AuthData authData, Connection connect){
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
			retorno = ProdutoServicoParametroDAO.delete(cdEmpresa, cdProdutoServico, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_produto_servico_parametro");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoParametroServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoParametroServices.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_produto_servico_parametro", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}