package com.tivic.manager.ord;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class TipoProdutoServicoServices {

	public static Result save(TipoProdutoServico tipoProdutoServico){
		return save(tipoProdutoServico, null, null);
	}
	
	public static Result save(TipoProdutoServico tipoProdutoServico, Connection connect){
		return save(tipoProdutoServico, null, connect);
	}

	public static Result save(TipoProdutoServico tipoProdutoServico, AuthData authData){
		return save(tipoProdutoServico, authData, null);
	}

	public static Result save(TipoProdutoServico tipoProdutoServico, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tipoProdutoServico==null)
				return new Result(-1, "Erro ao salvar. TipoProdutoServico é nulo");

			int retorno;
			if(tipoProdutoServico.getCdTipoProdutoServico()==0){
				retorno = TipoProdutoServicoDAO.insert(tipoProdutoServico, connect);
				tipoProdutoServico.setCdTipoProdutoServico(retorno);
			}
			else {
				retorno = TipoProdutoServicoDAO.update(tipoProdutoServico, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOPRODUTOSERVICO", tipoProdutoServico);
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
	public static Result remove(int cdTipoProdutoServico){
		return remove(cdTipoProdutoServico, false, null, null);
	}
	public static Result remove(int cdTipoProdutoServico, boolean cascade){
		return remove(cdTipoProdutoServico, cascade, null, null);
	}
	public static Result remove(int cdTipoProdutoServico, boolean cascade, AuthData authData){
		return remove(cdTipoProdutoServico, cascade, authData, null);
	}
	public static Result remove(int cdTipoProdutoServico, boolean cascade, AuthData authData, Connection connect){
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
			retorno = TipoProdutoServicoDAO.delete(cdTipoProdutoServico, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM ord_tipo_produto_servico");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoProdutoServicoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoProdutoServicoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM ord_tipo_produto_servico", " ORDER BY nm_tipo_produto_servico", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}