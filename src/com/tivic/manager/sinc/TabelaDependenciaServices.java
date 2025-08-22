package com.tivic.manager.sinc;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class TabelaDependenciaServices {

	public static Result save(TabelaDependencia tabelaDependencia){
		return save(tabelaDependencia, 0, 0, null);
	}
	
	public static Result save(TabelaDependencia tabelaDependencia, Connection connect){
		return save(tabelaDependencia, 0, 0, connect);
	}
	
	public static Result save(TabelaDependencia tabelaDependencia, int cdDependenteOld, int cdProvedorOld){
		return save(tabelaDependencia, cdDependenteOld, cdProvedorOld, null);
	}

	public static Result save(TabelaDependencia tabelaDependencia, int cdDependenteOld, int cdProvedorOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tabelaDependencia==null)
				return new Result(-1, "Erro ao salvar. TabelaDependencia é nulo");

			int retorno;
			if(cdDependenteOld > 0 && cdProvedorOld > 0){
				if(TabelaDependenciaDAO.get(cdDependenteOld, cdProvedorOld, connect) != null){
					retorno = TabelaDependenciaDAO.update(tabelaDependencia, cdDependenteOld, cdProvedorOld, connect);
				}
				else{
					retorno = TabelaDependenciaDAO.insert(tabelaDependencia, connect);	
				}
			}
			else{
				if(TabelaDependenciaDAO.get(tabelaDependencia.getCdDependente(), tabelaDependencia.getCdProvedor(), connect) != null){
					retorno = TabelaDependenciaDAO.update(tabelaDependencia, connect);
				}
				else{
					retorno = TabelaDependenciaDAO.insert(tabelaDependencia, connect);
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TABELADEPENDENCIA", tabelaDependencia);
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
	
	public static Result remove(int cdDependente, int cdProvedor){
		return remove(cdDependente, cdProvedor, false, null);
	}
	public static Result remove(int cdDependente, int cdProvedor, boolean cascade){
		return remove(cdDependente, cdProvedor, cascade, null);
	}
	public static Result remove(int cdDependente, int cdProvedor, boolean cascade, Connection connect){
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
			retorno = TabelaDependenciaDAO.delete(cdDependente, cdProvedor, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM sinc_tabela_dependencia");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaDependenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaDependenciaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM sinc_tabela_dependencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
