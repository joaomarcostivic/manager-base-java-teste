package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class LogradouroBairroServices {
	public static Result save(LogradouroBairro logradouroBairro){
		return save(logradouroBairro, null);
	}
	
	public static Result save(LogradouroBairro logradouroBairro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(logradouroBairro==null)
				return new Result(-1, "Erro ao salvar. Tipo de endereço é nulo");
			
			int retorno;
			
			LogradouroBairro lb = LogradouroBairroDAO.get(logradouroBairro.getCdBairro(), logradouroBairro.getCdLogradouro(), connect);
			
			if(lb == null){
				retorno = LogradouroBairroDAO.insert(logradouroBairro, connect);
			}
			else {
				retorno = LogradouroBairroDAO.update(logradouroBairro, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "LOGRADOUROBAIRRO", logradouroBairro);
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
	
	public static Result remove(int cdBairro, int cdLogradouro){
		return remove(cdBairro, cdLogradouro, false, null);
	}
	
	public static Result remove(int cdBairro, int cdLogradouro, boolean cascade){
		return remove(cdBairro, cdLogradouro, cascade, null);
	}
	
	public static Result remove(int cdBairro, int cdLogradouro, boolean cascade, Connection connect){
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
				retorno = LogradouroBairroDAO.delete(cdBairro, cdLogradouro, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir!");
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_logradouro_bairro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LogradouroBairroDAO.getAll: " + e);
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
		String limit = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if(criterios.get(i).getColumn().equalsIgnoreCase("limit")) {
				limit += " LIMIT "+ criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
		}
		ResultSetMap rsm = Search.find("SELECT A.*, " +
				   "	   B.nm_logradouro, " +
				   "	   C.nm_bairro, " +
				   "	   D.sg_tipo_logradouro, " +
				   "	   D.nm_tipo_logradouro,  " +
				   "	   E.nm_cidade, " +
				   " 	   E.cd_cidade, " +
				   "	   F.nm_distrito, " +
				   "	   G.sg_estado, G.nm_estado, G.cd_estado, " +
				   "	   H.nr_cep " +
		           "FROM grl_logradouro_bairro A " +
		           "JOIN grl_logradouro B ON (A.cd_logradouro = B.cd_logradouro) " +
		           "JOIN grl_bairro C ON (A.cd_bairro = C.cd_bairro) " +
		           "LEFT OUTER JOIN grl_tipo_logradouro D ON (B.cd_tipo_logradouro = D.cd_tipo_logradouro) " + 
		           "LEFT OUTER JOIN grl_cidade E ON (B.cd_cidade = E.cd_cidade) " +
				   "LEFT OUTER JOIN grl_distrito F ON (B.cd_distrito = F.cd_distrito AND B.cd_cidade = F.cd_cidade) " +
				   "LEFT OUTER JOIN grl_estado G ON (E.cd_estado = G.cd_estado) " +
				   "LEFT OUTER JOIN grl_logradouro_cep H ON (H.cd_logradouro = A.cd_logradouro) "+limit, 
		           criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		return rsm;
	}
	
	public static ResultSetMap getAllByLogradouro(int cdLogradouro) {
		return getAllByLogradouro(cdLogradouro, null);
	}

	public static ResultSetMap getAllByLogradouro(int cdLogradouro, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM grl_logradouro_bairro A, grl_bairro B " +
																  "WHERE A.cd_bairro = B.cd_bairro " +
																  "  AND A.cd_logradouro = ?");
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
}
