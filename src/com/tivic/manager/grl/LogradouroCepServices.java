package com.tivic.manager.grl;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class LogradouroCepServices {

	public static Result save(LogradouroCep logradouroCep){
		return save(logradouroCep, null);
	}

	public static Result save(LogradouroCep logradouroCep, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(logradouroCep==null)
				return new Result(-1, "Erro ao salvar. LogradouroCep é nulo");

			int retorno;
			if(logradouroCep.getCdLogradouro()==0){
				retorno = LogradouroCepDAO.insert(logradouroCep, connect);
				logradouroCep.setCdLogradouro(retorno);
			}
			else {
				retorno = LogradouroCepDAO.update(logradouroCep, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "LOGRADOUROCEP", logradouroCep);
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
	public static Result remove(int cdLogradouro, String nrCep){
		return remove(cdLogradouro, nrCep, false, null);
	}
	public static Result remove(int cdLogradouro, String nrCep, boolean cascade){
		return remove(cdLogradouro, nrCep, cascade, null);
	}
	public static Result remove(int cdLogradouro, String nrCep, boolean cascade, Connection connect){
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
			retorno = LogradouroCepDAO.delete(cdLogradouro, nrCep, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_logradouro_cep");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LogradouroCepServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LogradouroCepServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método para buscar parte do endereco atraves do CEP
	 * @return rsm com logradoro, estado, cidade, pais
	 */
	public static ResultSetMap getByCep(String nrCep) {
		return getByCep(nrCep, null);
	}

	public static ResultSetMap getByCep(String nrCep, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT 	A.nr_cep, A.cd_logradouro," +
											" A1.cd_distrito, A1.cd_cidade, A1.cd_tipo_logradouro, A1.nm_logradouro," +
											" C.nm_cidade, C.cd_estado," +
											" D.nm_distrito," +
											" E.sg_estado, E.nm_estado," +
											" F.nm_tipo_logradouro, F.sg_tipo_logradouro" +
											" FROM grl_logradouro_cep A" +
											" LEFT OUTER JOIN grl_logradouro A1 ON (A.cd_logradouro = A1.cd_logradouro)" +
											" LEFT OUTER JOIN grl_cidade C ON (A1.cd_cidade = C.cd_cidade)" +
											" LEFT OUTER JOIN grl_distrito D ON (A1.cd_distrito = D.cd_distrito AND A1.cd_cidade = D.cd_cidade)" +
											" LEFT OUTER JOIN grl_estado E ON (C.cd_estado = E.cd_estado)" +
											" LEFT OUTER JOIN grl_tipo_logradouro F ON (A1.cd_tipo_logradouro = F.cd_tipo_logradouro)" +
											" where A.nr_cep = '" + nrCep + "'");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LogradouroCepServices.getByCep: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LogradouroCepServices.getByCep: " + e);
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
		return Search.find("SELECT * from grl_logradouro_cep A, grl_logradouro B, grl_logradouro_bairro C, grl_bairro D, grl_cidade E, grl_estado F WHERE A.cd_logradouro = B.cd_logradouro AND A.cd_logradouro = C.cd_logradouro AND C.cd_bairro = D.cd_bairro AND D.cd_cidade = E.cd_cidade AND E.cd_estado = F.cd_estado", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
