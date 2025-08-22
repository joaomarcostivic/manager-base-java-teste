package com.tivic.manager.bdv;

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

public class ProprietarioServices {

	public static Result save(Proprietario proprietario){
		return save(proprietario, null, null);
	}

	public static Result save(Proprietario proprietario, AuthData authData){
		return save(proprietario, authData, null);
	}

	public static Result save(Proprietario proprietario, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(proprietario==null)
				return new Result(-1, "Erro ao salvar. Proprietario é nulo");

			int retorno;
			if(proprietario.getCdProprietario()==0){
				retorno = ProprietarioDAO.insert(proprietario, connect);
				proprietario.setCdProprietario(retorno);
			}
			else {
				retorno = ProprietarioDAO.update(proprietario, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PROPRIETARIO", proprietario);
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
	public static Result remove(Proprietario proprietario) {
		return remove(proprietario.getCdProprietario(), proprietario.getCdVeiculo());
	}
	public static Result remove(int cdProprietario, int cdVeiculo){
		return remove(cdProprietario, cdVeiculo, false, null, null);
	}
	public static Result remove(int cdProprietario, int cdVeiculo, boolean cascade){
		return remove(cdProprietario, cdVeiculo, cascade, null, null);
	}
	public static Result remove(int cdProprietario, int cdVeiculo, boolean cascade, AuthData authData){
		return remove(cdProprietario, cdVeiculo, cascade, authData, null);
	}
	public static Result remove(int cdProprietario, int cdVeiculo, boolean cascade, AuthData authData, Connection connect){
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
			retorno = ProprietarioDAO.delete(cdProprietario, cdVeiculo, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM bdv_proprietario");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProprietarioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProprietarioServices.getAll: " + e);
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
		return Search.find("SELECT * FROM bdv_proprietario", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
