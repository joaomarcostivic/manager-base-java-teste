package com.tivic.manager.grl;

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

public class FiltroCampoServices {
	
	public static final int TP_CAMPO_OBJECT  = 0;
	public static final int TP_CAMPO_INTEGER = 1;
	public static final int TP_CAMPO_FLOAT   = 2;
	public static final int TP_CAMPO_DOUBLE  = 3;
	public static final int TP_CAMPO_STRING  = 4;
	public static final int TP_CAMPO_DATE    = 5;

	public static Result save(FiltroCampo filtroCampo){
		return save(filtroCampo, null, null);
	}

	public static Result save(FiltroCampo filtroCampo, AuthData authData){
		return save(filtroCampo, authData, null);
	}

	public static Result save(FiltroCampo filtroCampo, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(filtroCampo==null)
				return new Result(-1, "Erro ao salvar. FiltroCampo é nulo");

			int retorno;
			if(filtroCampo.getCdCampo()==0){
				retorno = FiltroCampoDAO.insert(filtroCampo, connect);
				filtroCampo.setCdCampo(retorno);
			}
			else {
				retorno = FiltroCampoDAO.update(filtroCampo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "FILTROCAMPO", filtroCampo);
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
	public static Result remove(FiltroCampo filtroCampo) {
		return remove(filtroCampo.getCdCampo());
	}
	public static Result remove(int cdCampo){
		return remove(cdCampo, false, null, null);
	}
	public static Result remove(int cdCampo, boolean cascade){
		return remove(cdCampo, cascade, null, null);
	}
	public static Result remove(int cdCampo, boolean cascade, AuthData authData){
		return remove(cdCampo, cascade, authData, null);
	}
	public static Result remove(int cdCampo, boolean cascade, AuthData authData, Connection connect){
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
			retorno = FiltroCampoDAO.delete(cdCampo, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_filtro_campo ORDER BY nm_campo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FiltroCampoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FiltroCampoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_filtro_campo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
