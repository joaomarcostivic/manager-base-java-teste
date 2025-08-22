package com.tivic.manager.ord;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.LogUtils;

public class OrdemServicoItemServices {

	public static Result save(OrdemServicoItem ordemServicoItem){
		return save(ordemServicoItem, null);
	}

	public static Result save(OrdemServicoItem ordemServicoItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ordemServicoItem==null)
				return new Result(-1, "Erro ao salvar. OrdemServicoItem é nulo");

			int retorno;
			if(ordemServicoItem.getCdOrdemServicoItem()==0){
				retorno = OrdemServicoItemDAO.insert(ordemServicoItem, connect);
				ordemServicoItem.setCdOrdemServicoItem(retorno);
			}
			else {
				retorno = OrdemServicoItemDAO.update(ordemServicoItem, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ORDEMSERVICOITEM", ordemServicoItem);
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
	public static Result remove(int cdOrdemServicoItem, int cdOrdemServico){
		return remove(cdOrdemServicoItem, cdOrdemServico, false, null);
	}
	public static Result remove(int cdOrdemServicoItem, int cdOrdemServico, boolean cascade){
		return remove(cdOrdemServicoItem, cdOrdemServico, cascade, null);
	}
	public static Result remove(int cdOrdemServicoItem, int cdOrdemServico, boolean cascade, Connection connect){
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
			retorno = OrdemServicoItemDAO.delete(cdOrdemServicoItem, cdOrdemServico, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM ord_ordem_servico_item");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByOrdemServico(int cdOrdemServico) {
		return getAllByOrdemServico(cdOrdemServico, null);
	}

	public static ResultSetMap getAllByOrdemServico(int cdOrdemServico, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					"SELECT A.*,"
					+ " B.nm_tipo_produto_servico, B.txt_descricao "
					+ " FROM ord_ordem_servico_item A "
					+ " LEFT OUTER JOIN ord_tipo_produto_servico B ON (A.cd_tipo_produto_servico = B.cd_tipo_produto_servico)"
					+ " WHERE A.cd_ordem_servico=?");
			pstmt.setInt(1, cdOrdemServico);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemServices.getAll: " + e);
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
		return Search.find("SELECT * FROM ord_ordem_servico_item", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
