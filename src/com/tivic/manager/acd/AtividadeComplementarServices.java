package com.tivic.manager.acd;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class AtividadeComplementarServices {

	public static Result save(AtividadeComplementar atividadeComplementar){
		return save(atividadeComplementar, null);
	}

	public static Result save(AtividadeComplementar atividadeComplementar, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(atividadeComplementar==null)
				return new Result(-1, "Erro ao salvar. AtividadeComplementar é nulo");

			int retorno;
			if(atividadeComplementar.getCdAtividadeComplementar()==0){
				retorno = AtividadeComplementarDAO.insert(atividadeComplementar, connect);
				atividadeComplementar.setCdAtividadeComplementar(retorno);
			}
			else {
				retorno = AtividadeComplementarDAO.update(atividadeComplementar, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ATIVIDADECOMPLEMENTAR", atividadeComplementar);
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
	public static Result remove(int cdAtividadeComplementar){
		return remove(cdAtividadeComplementar, false, null);
	}
	public static Result remove(int cdAtividadeComplementar, boolean cascade){
		return remove(cdAtividadeComplementar, cascade, null);
	}
	public static Result remove(int cdAtividadeComplementar, boolean cascade, Connection connect){
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
			retorno = AtividadeComplementarDAO.delete(cdAtividadeComplementar, connect);
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
			pstmt = connect.prepareStatement("SELECT A.* FROM acd_atividade_complementar A"
					+ "						    WHERE NOT EXISTS (SELECT cd_atividade_complementar FROM acd_atividade_complementar B"
					+ "												WHERE A.cd_atividade_complementar = B.cd_atividade_superior) AND A.st_atividade_complementar = 1 ORDER BY A.nm_atividade_complementar");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtividadeComplementarServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtividadeComplementarServices.getAll: " + e);
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
		ResultSetMap  _rsm = Search.find("SELECT * FROM acd_atividade_complementar", "ORDER BY nm_atividade_complementar" +limit, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	    return _rsm;
	
	}

}
