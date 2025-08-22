package com.tivic.manager.mob.AitInconsistencia;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

public class AitInconsistenciaServices {
	public static final int PENDETE = 0;
	public static final int RESOLVIDO = 1;

	public Result save(AitInconsistencia aitInconsistencia){
		return save(aitInconsistencia, null);
	}

	public Result save(AitInconsistencia aitInconsistencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(aitInconsistencia==null)
				return new Result(-1, "Erro ao salvar. AitInconsistencia � nulo");

			int retorno;
			if(aitInconsistencia.getCdAitInconsistencia()==0){
				retorno = AitInconsistenciaDAO.insert(aitInconsistencia, connect);
				aitInconsistencia.setCdAitInconsistencia(retorno);
			}
			else {
				retorno = AitInconsistenciaDAO.update(aitInconsistencia, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AITINCONSISTENCIA", aitInconsistencia);
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
	public static Result remove(AitInconsistencia aitInconsistencia) {
		return remove(aitInconsistencia.getCdAitInconsistencia());
	}
	public static Result remove(int cdAitInconsistencia){
		return remove(cdAitInconsistencia, false, null, null);
	}
	public static Result remove(int cdAitInconsistencia, boolean cascade){
		return remove(cdAitInconsistencia, cascade, null, null);
	}
	public static Result remove(int cdAitInconsistencia, boolean cascade, AuthData authData){
		return remove(cdAitInconsistencia, cascade, authData, null);
	}
	public static Result remove(int cdAitInconsistencia, boolean cascade, AuthData authData, Connection connect){
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
			retorno = AitInconsistenciaDAO.delete(cdAitInconsistencia, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro est� vinculado a outros e n�o pode ser exclu�do!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro exclu�do com sucesso!");
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_inconsistencia");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitInconsistenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitInconsistenciaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_ait_inconsistencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
