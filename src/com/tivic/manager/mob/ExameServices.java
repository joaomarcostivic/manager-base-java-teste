package com.tivic.manager.mob;

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

public class ExameServices {

	public static Result save( ArrayList<Exame>  exames, int cdRrd, int cdTrrav){
		return save(exames,cdRrd, cdTrrav, null, null);
	}

	public static Result save(ArrayList<Exame>  exames,int cdRrd, int cdTrrav, AuthData authData){
		return save(exames,cdRrd, cdTrrav, authData, null);
	}

	public static Result save(ArrayList<Exame>  exames,int cdRrd, int cdTrrav, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(exames==null)
				return new Result(-1, "Erro ao salvar. Exame é nulo");

			

			int retorno = 0;
			for (Exame exame: exames) {
				
				if(cdRrd > 0){				
				exame.setCdRrd(cdRrd);
				}
				
				if(cdRrd > 0){				
					exame.setCdTrrav(cdTrrav);
				}
				
				retorno = ExameDAO.insert(exame, connect);
				
				if(retorno<=0)
					break;				
			}			
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "EXAME", exames);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(Exame exame) {
		return remove(exame.getCdExame(), exame.getCdTipoExame());
	}
	public static Result remove(int cdExame, int cdTipoExame){
		return remove(cdExame, cdTipoExame, false, null, null);
	}
	public static Result remove(int cdExame, int cdTipoExame, boolean cascade){
		return remove(cdExame, cdTipoExame, cascade, null, null);
	}
	public static Result remove(int cdExame, int cdTipoExame, boolean cascade, AuthData authData){
		return remove(cdExame, cdTipoExame, cascade, authData, null);
	}
	public static Result remove(int cdExame, int cdTipoExame, boolean cascade, AuthData authData, Connection connect){
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
			retorno = ExameDAO.delete(cdExame, cdTipoExame, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_exame");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExameServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExameServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_exame", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}