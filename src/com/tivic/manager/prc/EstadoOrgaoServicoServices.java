package com.tivic.manager.prc;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class EstadoOrgaoServicoServices {

	public static Result save(EstadoOrgaoServico estadoOrgaoServico){
		return save(estadoOrgaoServico, null, null);
	}

	public static Result save(EstadoOrgaoServico estadoOrgaoServico, AuthData authData){
		return save(estadoOrgaoServico, authData, null);
	}

	public static Result save(EstadoOrgaoServico estadoOrgaoServico, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(estadoOrgaoServico==null)
				return new Result(-1, "Erro ao salvar. EstadoOrgaoServico é nulo");

			int retorno;
			EstadoOrgaoServico eos = EstadoOrgaoServicoDAO.
					get(estadoOrgaoServico.getCdEstado(), estadoOrgaoServico.getCdOrgao(), estadoOrgaoServico.getCdProdutoServico(), connect);
			if(eos==null){
				retorno = EstadoOrgaoServicoDAO.insert(estadoOrgaoServico, connect);
				estadoOrgaoServico.setCdEstado(retorno);
			}
			else {
				retorno = EstadoOrgaoServicoDAO.update(estadoOrgaoServico, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ESTADOORGAOSERVICO", estadoOrgaoServico);
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
	public static Result remove(EstadoOrgaoServico estadoOrgaoServico) {
		return remove(estadoOrgaoServico.getCdEstado(), estadoOrgaoServico.getCdOrgao(), estadoOrgaoServico.getCdProdutoServico());
	}
	public static Result remove(int cdEstado, int cdOrgao, int cdProdutoServico){
		return remove(cdEstado, cdOrgao, cdProdutoServico, false, null, null);
	}
	public static Result remove(int cdEstado, int cdOrgao, int cdProdutoServico, boolean cascade){
		return remove(cdEstado, cdOrgao, cdProdutoServico, cascade, null, null);
	}
	public static Result remove(int cdEstado, int cdOrgao, int cdProdutoServico, boolean cascade, AuthData authData){
		return remove(cdEstado, cdOrgao, cdProdutoServico, cascade, authData, null);
	}
	public static Result remove(int cdEstado, int cdOrgao, int cdProdutoServico, boolean cascade, AuthData authData, Connection connect){
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
			retorno = EstadoOrgaoServicoDAO.delete(cdEstado, cdOrgao, cdProdutoServico, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_estado_orgao_servico");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoServicoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoServicoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getServicos(int cdOrgao, int cdEstado) {
		return getServicos(cdOrgao, cdEstado, null);
	}

	public static ResultSetMap getServicos(int cdOrgao, int cdEstado, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					  " SELECT A.*,"
					+ " B.nm_produto_servico "
					+ " FROM prc_estado_orgao_servico A"
					+ " JOIN grl_produto_servico B ON (A.cd_produto_servico = B.cd_produto_servico)"
					+ " WHERE A.cd_orgao = ?"
					+ " AND A.cd_estado = ?");
			pstmt.setInt(1, cdOrgao);
			pstmt.setInt(2, cdEstado);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoServicoServices.getServicos: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoServicoServices.getServicos: " + e);
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
		return Search.find("SELECT * FROM prc_estado_orgao_servico", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}