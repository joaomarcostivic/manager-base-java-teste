package com.tivic.manager.prc;

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

public class CidadeOrgaoServicoServices {

	public static Result save(CidadeOrgaoServico cidadeOrgaoServico){
		return save(cidadeOrgaoServico, null, null);
	}

	public static Result save(CidadeOrgaoServico cidadeOrgaoServico, AuthData authData){
		return save(cidadeOrgaoServico, authData, null);
	}

	public static Result save(CidadeOrgaoServico cidadeOrgaoServico, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cidadeOrgaoServico==null)
				return new Result(-1, "Erro ao salvar. CidadeOrgaoServico é nulo");

			int retorno;
			CidadeOrgaoServico cos = CidadeOrgaoServicoDAO.
					get(cidadeOrgaoServico.getCdOrgao(), cidadeOrgaoServico.getCdCidade(), cidadeOrgaoServico.getCdProdutoServico(), connect);
			if(cos==null){
				retorno = CidadeOrgaoServicoDAO.insert(cidadeOrgaoServico, connect);
				cidadeOrgaoServico.setCdOrgao(retorno);
			}
			else {
				retorno = CidadeOrgaoServicoDAO.update(cidadeOrgaoServico, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CIDADEORGAOSERVICO", cidadeOrgaoServico);
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
	public static Result remove(CidadeOrgaoServico cidadeOrgaoServico) {
		return remove(cidadeOrgaoServico.getCdOrgao(), cidadeOrgaoServico.getCdCidade(), cidadeOrgaoServico.getCdProdutoServico());
	}
	public static Result remove(int cdOrgao, int cdCidade, int cdProdutoServico){
		return remove(cdOrgao, cdCidade, cdProdutoServico, false, null, null);
	}
	public static Result remove(int cdOrgao, int cdCidade, int cdProdutoServico, boolean cascade){
		return remove(cdOrgao, cdCidade, cdProdutoServico, cascade, null, null);
	}
	public static Result remove(int cdOrgao, int cdCidade, int cdProdutoServico, boolean cascade, AuthData authData){
		return remove(cdOrgao, cdCidade, cdProdutoServico, cascade, authData, null);
	}
	public static Result remove(int cdOrgao, int cdCidade, int cdProdutoServico, boolean cascade, AuthData authData, Connection connect){
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
			retorno = CidadeOrgaoServicoDAO.delete(cdOrgao, cdCidade, cdProdutoServico, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_cidade_orgao_servico");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoServicoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoServicoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getServicos(int cdOrgao, int cdCidade) {
		return getServicos(cdOrgao, cdCidade, null);
	}

	public static ResultSetMap getServicos(int cdOrgao, int cdCidade, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					  " SELECT A.*,"
					+ " B.nm_produto_servico "
					+ " FROM prc_cidade_orgao_servico A"
					+ " JOIN grl_produto_servico B ON (A.cd_produto_servico=B.cd_produto_servico)"
					+ " WHERE A.cd_orgao=?"
					+ " AND A.cd_cidade=?");
			pstmt.setInt(1, cdOrgao);
			pstmt.setInt(2, cdCidade);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoServicoServices.getServicos: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoServicoServices.getServicos: " + e);
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
		return Search.find("SELECT * FROM prc_cidade_orgao_servico", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
