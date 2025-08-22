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

public class AitCondutorServices {

	public static Result save(AitCondutor aitCondutor){
		return save(aitCondutor, null, null);
	}

	public static Result save(AitCondutor aitCondutor, AuthData authData){
		return save(aitCondutor, authData, null);
	}

	public static Result save(AitCondutor aitCondutor, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(aitCondutor==null)
				return new Result(-1, "Erro ao salvar. AitCondutor é nulo");

			int retorno;
			if(aitCondutor.getCdAitCondutor()==0){
				retorno = AitCondutorDAO.insert(aitCondutor, connect);
				aitCondutor.setCdAitCondutor(retorno);
			}
			else {
				retorno = AitCondutorDAO.update(aitCondutor, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AITCONDUTOR", aitCondutor);
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
	public static Result remove(AitCondutor aitCondutor) {
		return remove(aitCondutor.getCdAitCondutor(), aitCondutor.getCdAit());
	}
	public static Result remove(int cdAitCondutor, int cdAit){
		return remove(cdAitCondutor, cdAit, false, null, null);
	}
	public static Result remove(int cdAitCondutor, int cdAit, boolean cascade){
		return remove(cdAitCondutor, cdAit, cascade, null, null);
	}
	public static Result remove(int cdAitCondutor, int cdAit, boolean cascade, AuthData authData){
		return remove(cdAitCondutor, cdAit, cascade, authData, null);
	}
	public static Result remove(int cdAitCondutor, int cdAit, boolean cascade, AuthData authData, Connection connect){
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
			retorno = AitCondutorDAO.delete(cdAitCondutor, cdAit, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_condutor");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitCondutorServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitCondutorServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_ait_condutor", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
