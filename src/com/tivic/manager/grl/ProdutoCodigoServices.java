package com.tivic.manager.grl;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class ProdutoCodigoServices {

	public static Result save(ProdutoCodigo produtoCodigo){
		return save(produtoCodigo, null);
	}

	public static Result save(ProdutoCodigo produtoCodigo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(produtoCodigo==null)
				return new Result(-1, "Erro ao salvar. ProdutoCodigo é nulo");


			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_produto_servico WHERE id_produto_servico = '"+produtoCodigo.getIdProdutoServico()+"'").executeQuery());
			if(rsm.next()){
				Conexao.rollback(connect);
				return new Result(-1, "Código de Barras já existe!");
			}
			else{
				rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_produto_codigo WHERE id_produto_servico = '"+produtoCodigo.getIdProdutoServico()+"' AND cd_produto_codigo <> "+produtoCodigo.getCdProdutoCodigo()+" AND lg_codigo_barras = 1").executeQuery());
				if(rsm.next()){
					Conexao.rollback(connect);
					return new Result(-1, "Código de Barras já existe!");
				}
			}
			
			
			int retorno;
			if(produtoCodigo.getCdProdutoCodigo()==0){
				retorno = ProdutoCodigoDAO.insert(produtoCodigo, connect);
				produtoCodigo.setCdProdutoCodigo(retorno);
			}
			else {
				retorno = ProdutoCodigoDAO.update(produtoCodigo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PRODUTOCODIGO", produtoCodigo);
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
	public static Result remove(int cdProdutoCodigo){
		return remove(cdProdutoCodigo, false, null);
	}
	public static Result remove(int cdProdutoCodigo, boolean cascade){
		return remove(cdProdutoCodigo, cascade, null);
	}
	public static Result remove(int cdProdutoCodigo, boolean cascade, Connection connect){
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
			retorno = ProdutoCodigoDAO.delete(cdProdutoCodigo, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_produto_codigo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoCodigoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoCodigoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_produto_codigo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	/**
	 * Por padrão o método irá considerar ser de código de barras
	 * @param cdProdutoServico
	 * @return
	 */
	public static ResultSetMap getAll(int cdProdutoServico) {
		return getAll(cdProdutoServico, 1, null);
	}
	public static ResultSetMap getAll(int cdProdutoServico, int lgCodigoBarras) {
		return getAll(cdProdutoServico, lgCodigoBarras, null);
	}
	public static ResultSetMap getAll(int cdProdutoServico, int lgCodigoBarras, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_produto_codigo "+ 
					"						WHERE cd_produto_servico = " + cdProdutoServico + 
					"						  AND lg_codigo_barras = " + lgCodigoBarras);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoCodigoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoCodigoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}

