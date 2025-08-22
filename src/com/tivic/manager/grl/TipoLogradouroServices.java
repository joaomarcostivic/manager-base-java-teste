package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class TipoLogradouroServices {
	public static Result save(TipoLogradouro tipoLogradouro){
		return save(tipoLogradouro, null);
	}
	
	public static Result save(TipoLogradouro tipoLogradouro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tipoLogradouro==null)
				return new Result(-1, "Erro ao salvar. Tipo de logradouro é nulo");
			
			int retorno;
			if(tipoLogradouro.getCdTipoLogradouro()==0){
				retorno = TipoLogradouroDAO.insert(tipoLogradouro, connect);
				tipoLogradouro.setCdTipoLogradouro(retorno);
			}
			else {
				retorno = TipoLogradouroDAO.update(tipoLogradouro, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOLOGRADOURO", tipoLogradouro);
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
	
	public static Result remove(int cdTipoLogradouro){
		return remove(cdTipoLogradouro, false, null);
	}
	
	public static Result remove(int cdTipoLogradouro, boolean cascade){
		return remove(cdTipoLogradouro, cascade, null);
	}
	
	public static Result remove(int cdTipoLogradouro, boolean cascade, Connection connect){
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
				retorno = TipoLogradouroDAO.delete(cdTipoLogradouro, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este tipo de logradouro está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Tipo de logradouro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir tipo de logradouro!");
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_logradouro ORDER BY nm_tipo_logradouro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoLogradouroDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static ResultSetMap getAllByLogradouro(int cdLogradouro) {
		return getAllByLogradouro(cdLogradouro, null);
	}

	public static ResultSetMap getAllByLogradouro(int cdLogradouro, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM grl_tipo_logradouro A, grl_logradouro B " +
																  "WHERE A.cd_tipo_logradouro = B.cd_tipo_logradouro " +
																  "  AND B.cd_logradouro = ?");
			pstmt.setInt(1, cdLogradouro);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		String limit = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if(criterios.get(i).getColumn().equalsIgnoreCase("limit")) {
				limit += " LIMIT "+ criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
		}
		ResultSetMap rsm = Search.find("SELECT * FROM grl_tipo_logradouro", "ORDER BY nm_tipo_logradouro"+limit, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
        return rsm; 	
	}
}
