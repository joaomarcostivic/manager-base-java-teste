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

public class AitProprietarioServices {

	public static Result save(AitProprietario aitProprietario){
		return save(aitProprietario, null, null);
	}

	public static Result save(AitProprietario aitProprietario, AuthData authData){
		return save(aitProprietario, authData, null);
	}

	public static Result save(AitProprietario aitProprietario, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(aitProprietario==null)
				return new Result(-1, "Erro ao salvar. AitProprietario é nulo");

			int retorno;
			if(aitProprietario.getCdAitProprietario()==0){
				retorno = AitProprietarioDAO.insert(aitProprietario, connect);
				aitProprietario.setCdAitProprietario(retorno);
			}
			else {
				retorno = AitProprietarioDAO.update(aitProprietario, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AITPROPRIETARIO", aitProprietario);
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
	public static Result remove(AitProprietario aitProprietario) {
		return remove(aitProprietario.getCdAitProprietario(), aitProprietario.getCdAit());
	}
	public static Result remove(int cdAitProprietario, int cdAit){
		return remove(cdAitProprietario, cdAit, false, null, null);
	}
	public static Result remove(int cdAitProprietario, int cdAit, boolean cascade){
		return remove(cdAitProprietario, cdAit, cascade, null, null);
	}
	public static Result remove(int cdAitProprietario, int cdAit, boolean cascade, AuthData authData){
		return remove(cdAitProprietario, cdAit, cascade, authData, null);
	}
	public static Result remove(int cdAitProprietario, int cdAit, boolean cascade, AuthData authData, Connection connect){
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
			retorno = AitProprietarioDAO.delete(cdAitProprietario, cdAit, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_proprietario");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitProprietarioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitProprietarioServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_ait_proprietario", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
