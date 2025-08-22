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

public class ProcessoRequerenteServices {

	public static Result save(ProcessoRequerente processoRequerente){
		return save(processoRequerente, null, null);
	}

	public static Result save(ProcessoRequerente processoRequerente, AuthData authData){
		return save(processoRequerente, authData, null);
	}

	public static Result save(ProcessoRequerente processoRequerente, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(processoRequerente==null)
				return new Result(-1, "Erro ao salvar. ProcessoRequerente é nulo");

			int retorno;
			if(processoRequerente.getCdRequerente()==0){
				retorno = ProcessoRequerenteDAO.insert(processoRequerente, connect);
				processoRequerente.setCdRequerente(retorno);
			}
			else {
				retorno = ProcessoRequerenteDAO.update(processoRequerente, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PROCESSOREQUERENTE", processoRequerente);
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
	public static Result remove(ProcessoRequerente processoRequerente) {
		return remove(processoRequerente.getCdRequerente(), processoRequerente.getCdProcesso());
	}
	public static Result remove(int cdRequerente, int cdProcesso){
		return remove(cdRequerente, cdProcesso, false, null, null);
	}
	public static Result remove(int cdRequerente, int cdProcesso, boolean cascade){
		return remove(cdRequerente, cdProcesso, cascade, null, null);
	}
	public static Result remove(int cdRequerente, int cdProcesso, boolean cascade, AuthData authData){
		return remove(cdRequerente, cdProcesso, cascade, authData, null);
	}
	public static Result remove(int cdRequerente, int cdProcesso, boolean cascade, AuthData authData, Connection connect){
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
			retorno = ProcessoRequerenteDAO.delete(cdRequerente, cdProcesso, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM str_processo_requerente");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoRequerenteServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoRequerenteServices.getAll: " + e);
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
		return Search.find("SELECT * FROM str_processo_requerente", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
