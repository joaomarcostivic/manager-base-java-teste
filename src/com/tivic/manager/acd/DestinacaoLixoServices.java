package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class DestinacaoLixoServices {

	public static Result save(DestinacaoLixo destinacaoLixo){
		return save(destinacaoLixo, null);
	}

	public static Result save(DestinacaoLixo destinacaoLixo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(destinacaoLixo==null)
				return new Result(-1, "Erro ao salvar. DestinacaoLixo é nulo");

			int retorno;
			if(destinacaoLixo.getCdDestinacaoLixo()==0){
				retorno = DestinacaoLixoDAO.insert(destinacaoLixo, connect);
				destinacaoLixo.setCdDestinacaoLixo(retorno);
			}
			else {
				retorno = DestinacaoLixoDAO.update(destinacaoLixo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "DESTINACAOLIXO", destinacaoLixo);
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
	public static Result remove(int cdDestinacaoLixo){
		return remove(cdDestinacaoLixo, false, null);
	}
	public static Result remove(int cdDestinacaoLixo, boolean cascade){
		return remove(cdDestinacaoLixo, cascade, null);
	}
	public static Result remove(int cdDestinacaoLixo, boolean cascade, Connection connect){
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
			retorno = DestinacaoLixoDAO.delete(cdDestinacaoLixo, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_destinacao_lixo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DestinacaoLixoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DestinacaoLixoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_destinacao_lixo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static DestinacaoLixo getById(String idDestinacaoLixo) {
		return getById(idDestinacaoLixo, null);
	}

	public static DestinacaoLixo getById(String idDestinacaoLixo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_destinacao_lixo WHERE id_destinacao_lixo=?");
			pstmt.setString(1, idDestinacaoLixo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DestinacaoLixo(rs.getInt("cd_destinacao_lixo"),
						rs.getString("nm_destinacao_lixo"),
						rs.getString("id_destinacao_lixo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DestinacaoLixoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DestinacaoLixoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
