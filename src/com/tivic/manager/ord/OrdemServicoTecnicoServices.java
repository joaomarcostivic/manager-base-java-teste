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

public class OrdemServicoTecnicoServices {

	public static Result save(OrdemServicoTecnico ordemServicoTecnico){
		return save(ordemServicoTecnico, null, null);
	}

	public static Result save(OrdemServicoTecnico ordemServicoTecnico, AuthData authData){
		return save(ordemServicoTecnico, authData, null);
	}

	public static Result save(OrdemServicoTecnico ordemServicoTecnico, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ordemServicoTecnico==null)
				return new Result(-1, "Erro ao salvar. OrdemServicoTecnico é nulo");

			int retorno;
			OrdemServicoTecnico ost = OrdemServicoTecnicoDAO.get(ordemServicoTecnico.getCdOrdemServico(), ordemServicoTecnico.getCdPessoa(), connect);
			if(ost==null){
				retorno = OrdemServicoTecnicoDAO.insert(ordemServicoTecnico, connect);
				ordemServicoTecnico.setCdOrdemServico(retorno);
			}
			else {
				retorno = OrdemServicoTecnicoDAO.update(ordemServicoTecnico, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ORDEMSERVICOTECNICO", ordemServicoTecnico);
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
	public static Result remove(OrdemServicoTecnico ordemServicoTecnico) {
		return remove(ordemServicoTecnico.getCdOrdemServico(), ordemServicoTecnico.getCdPessoa());
	}
	public static Result remove(int cdOrdemServico, int cdPessoa){
		return remove(cdOrdemServico, cdPessoa, false, null, null);
	}
	public static Result remove(int cdOrdemServico, int cdPessoa, boolean cascade){
		return remove(cdOrdemServico, cdPessoa, cascade, null, null);
	}
	public static Result remove(int cdOrdemServico, int cdPessoa, boolean cascade, AuthData authData){
		return remove(cdOrdemServico, cdPessoa, cascade, authData, null);
	}
	public static Result remove(int cdOrdemServico, int cdPessoa, boolean cascade, AuthData authData, Connection connect){
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
			retorno = OrdemServicoTecnicoDAO.delete(cdOrdemServico, cdPessoa, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM ord_ordem_servico_tecnico");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoTecnicoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoTecnicoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result removeAll(int cdOrdemServico) {
		return removeAll(cdOrdemServico, null);
	}

	public static Result removeAll(int cdOrdemServico, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM ord_ordem_servico_tecnico WHERE cd_ordem_servico=?");
			pstmt.setInt(1, cdOrdemServico);
			
			if(pstmt.executeUpdate()<0) {
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao excluir técnicos");
			}
			
			return new Result(1, "Técnicos excluídos com sucesso!");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoTecnicoServices.removeAll: " + e);
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
		return Search.find(
				" SELECT A.*, B.nm_pessoa "
				+ " FROM ord_ordem_servico_tecnico A"
				+ " JOIN grl_pessoa B ON (A.cd_pessoa=B.cd_pessoa)", 
				criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
