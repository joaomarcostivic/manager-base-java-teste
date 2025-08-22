package com.tivic.manager.ord;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.grl.ArquivoServices;
import com.tivic.manager.seg.AuthData;

public class OrdemServicoArquivoServices {

	public static Result save(OrdemServicoArquivo ordemServicoArquivo, Arquivo arquivo){
		return save(ordemServicoArquivo, arquivo, null, null);
	}

	public static Result save(OrdemServicoArquivo ordemServicoArquivo, Arquivo arquivo, AuthData authData){
		return save(ordemServicoArquivo, arquivo, authData, null);
	}

	public static Result save(OrdemServicoArquivo ordemServicoArquivo, Arquivo arquivo, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(arquivo==null)
				return new Result(-1, "Erro ao salvar. Arquivo é nulo");

			if(ordemServicoArquivo==null)
				return new Result(-1, "Erro ao salvar. OrdemServicoArquivo é nulo");
			
			int retorno;

			if(arquivo.getCdArquivo()==0){
				retorno = ArquivoDAO.insert(arquivo, connect);
				if(retorno<=0) {
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-2, "Erro ao inserir arquivo");
				}
				arquivo.setCdArquivo(retorno);
			}
			else {
				retorno = ArquivoDAO.update(arquivo, connect);
			}
			
			ordemServicoArquivo.setCdArquivo(arquivo.getCdArquivo());
			OrdemServicoArquivo osa = OrdemServicoArquivoDAO.get(ordemServicoArquivo.getCdOrdemServico(), ordemServicoArquivo.getCdArquivo(), connect);
			if(osa==null){
				retorno = OrdemServicoArquivoDAO.insert(ordemServicoArquivo, connect);
			}
			else {
				retorno = OrdemServicoArquivoDAO.update(ordemServicoArquivo, connect);
			}

			if(retorno<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-3, "Erro ao salvar.");
			}

			//System.out.println("isConnectionNull: "+isConnectionNull);
			if(isConnectionNull)
				connect.commit();

			return new Result(1, "Salvo com sucesso...", "ORDEMSERVICOARQUIVO", ordemServicoArquivo);
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
	public static Result remove(int cdOrdemServico, int cdArquivo){
		return remove(cdOrdemServico, cdArquivo, false, null, null);
	}
	public static Result remove(int cdOrdemServico, int cdArquivo, boolean cascade){
		return remove(cdOrdemServico, cdArquivo, cascade, null, null);
	}
	public static Result remove(int cdOrdemServico, int cdArquivo, boolean cascade, AuthData authData){
		return remove(cdOrdemServico, cdArquivo, cascade, authData, null);
	}
	public static Result remove(int cdOrdemServico, int cdArquivo, boolean cascade, AuthData authData, Connection connect){
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
			retorno = OrdemServicoArquivoDAO.delete(cdOrdemServico, cdArquivo, connect);
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
	
	public static Result remove(int cdOrdemServico){
		return remove(cdOrdemServico, null);
	}
	
	public static Result remove(int cdOrdemServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsm = new ResultSetMap(connect
					.prepareStatement("SELECT * FROM ord_ordem_servico_arquivo "
									+ " WHERE cd_ordem_servico="+cdOrdemServico).executeQuery());
			while(rsm.next()) {
				if(remove(rsm.getInt("cd_ordem_servico"), rsm.getInt("cd_arquivo"), true, null, connect).getCode()<=0) {
					if(isConnectionNull)
						connect.rollback();
					return new Result(-1, "Erro ao escluir arquivos.");
				}
				if(ArquivoServices.remove(rsm.getInt("cd_arquivo"), true, connect).getCode()<=0) {
					if(isConnectionNull)
						connect.rollback();
					return new Result(-1, "Erro ao escluir arquivos.");
				}
			}
							
			
			if (isConnectionNull)
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
			pstmt = connect.prepareStatement("SELECT * FROM ord_ordem_servico_arquivo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoArquivoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoArquivoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM ord_ordem_servico_arquivo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}