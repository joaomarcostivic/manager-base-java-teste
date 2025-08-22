package com.tivic.manager.str;

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

public class ProcessoArquivoServices {

	public static Result save(ProcessoArquivo processoArquivo){
		return save(processoArquivo, null, null);
	}

	public static Result save(ProcessoArquivo processoArquivo, AuthData authData){
		return save(processoArquivo, authData, null);
	}

	public static Result save(ProcessoArquivo processoArquivo, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(processoArquivo==null)
				return new Result(-1, "Erro ao salvar. ProcessoArquivo é nulo");

			int retorno;
			ProcessoArquivo pa = ProcessoArquivoDAO.get(processoArquivo.getCdProcesso(), processoArquivo.getCdArquivo(), connect);
			if(pa == null){
				retorno = ProcessoArquivoDAO.insert(processoArquivo, connect);
				//processoArquivo.setCdProcesso(retorno);
			}
			else {
				retorno = ProcessoArquivoDAO.update(processoArquivo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PROCESSOARQUIVO", processoArquivo);
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
	public static Result remove(ProcessoArquivo processoArquivo) {
		return remove(processoArquivo.getCdProcesso(), processoArquivo.getCdArquivo());
	}
	public static Result remove(int cdProcesso, int cdArquivo){
		return remove(cdProcesso, cdArquivo, false, null, null);
	}
	public static Result remove(int cdProcesso, int cdArquivo, boolean cascade){
		return remove(cdProcesso, cdArquivo, cascade, null, null);
	}
	public static Result remove(int cdProcesso, int cdArquivo, boolean cascade, AuthData authData){
		return remove(cdProcesso, cdArquivo, cascade, authData, null);
	}
	public static Result remove(int cdProcesso, int cdArquivo, boolean cascade, AuthData authData, Connection connect){
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
			retorno = ProcessoArquivoDAO.delete(cdProcesso, cdArquivo, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM str_processo_arquivo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM str_processo_arquivo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
