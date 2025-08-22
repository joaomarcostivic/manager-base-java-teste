package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class TrravAitServices {

	
	
	public static Result save(int cdTrrav, ArrayList<TrravAit> trravAits) {
		return save(cdTrrav, trravAits, null);
	}

	public static Result save(int cdTrrav, ArrayList<TrravAit> trravAits, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			int retorno = 0;
			for (TrravAit trravAit: trravAits) {
				trravAit.setCdTrrav(cdTrrav);
				retorno = TrravAitDAO.insert(trravAit, connect);
				
				if(retorno<=0)
					break;
			}
			
			if(retorno>0)
				return new Result(1, "TrravAit incluídos com sucesso.");
			else
				return new Result(-1, "Erro ao incluir TrravAit.");
				
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-2, "Erro ao incluir TrravAit.");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	
	public static Result save(TrravAit trravAit){
		return save(trravAit, null, null);
	}

	public static Result save(TrravAit trravAit, AuthData authData){
		return save(trravAit, authData, null);
	}

	public static Result save(TrravAit trravAit, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(trravAit==null)
				return new Result(-1, "Erro ao salvar. TrravAit é nulo");

			int retorno;
			if(trravAit.getCdTrrav()==0){
				retorno = TrravAitDAO.insert(trravAit, connect);
				trravAit.setCdTrrav(retorno);
			}
			else {
				retorno = TrravAitDAO.update(trravAit, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TRRAVAIT", trravAit);
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
	public static Result remove(TrravAit trravAit) {
		return remove(trravAit.getCdTrrav(), trravAit.getCdAit());
	}
	public static Result remove(int cdTrrav, int cdAit){
		return remove(cdTrrav, cdAit, false, null, null);
	}
	public static Result remove(int cdTrrav, int cdAit, boolean cascade){
		return remove(cdTrrav, cdAit, cascade, null, null);
	}
	public static Result remove(int cdTrrav, int cdAit, boolean cascade, AuthData authData){
		return remove(cdTrrav, cdAit, cascade, authData, null);
	}
	public static Result remove(int cdTrrav, int cdAit, boolean cascade, AuthData authData, Connection connect){
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
			retorno = TrravAitDAO.delete(cdTrrav, cdAit, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_trrav_ait");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrravAitServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravAitServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_trrav_ait", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}