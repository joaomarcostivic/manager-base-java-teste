package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class AitTranporteMovimentoServices {

	public static Result save(AitTranporteMovimento aitTranporteMovimento){
		return save(aitTranporteMovimento, null);
	}

	public static Result save(AitTranporteMovimento aitTranporteMovimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(aitTranporteMovimento==null)
				return new Result(-1, "Erro ao salvar. AitTranporteMovimento é nulo");

			int retorno;
			if(aitTranporteMovimento.getCdAitTranporteMovimento()==0){
				retorno = AitTranporteMovimentoDAO.insert(aitTranporteMovimento, connect);
				aitTranporteMovimento.setCdAitTranporteMovimento(retorno);
			}
			else {
				retorno = AitTranporteMovimentoDAO.update(aitTranporteMovimento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AITTRANPORTEMOVIMENTO", aitTranporteMovimento);
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
	public static Result remove(int cdAitTranporteMovimento){
		return remove(cdAitTranporteMovimento, false, null);
	}
	public static Result remove(int cdAitTranporteMovimento, boolean cascade){
		return remove(cdAitTranporteMovimento, cascade, null);
	}
	public static Result remove(int cdAitTranporteMovimento, boolean cascade, Connection connect){
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
			retorno = AitTranporteMovimentoDAO.delete(cdAitTranporteMovimento, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_transporte_movimento");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTranporteMovimentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTranporteMovimentoServices.getAll: " + e);
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
		System.out.println("criterios: " + criterios);
		ResultSetMap rsm = Search.find("SELECT * FROM mob_ait_transporte_movimento ",
				criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		 
		return rsm;
		
	}

}
