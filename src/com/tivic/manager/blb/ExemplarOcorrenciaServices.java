package com.tivic.manager.blb;

import java.sql.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;

public class ExemplarOcorrenciaServices {

	public static Result save(ExemplarOcorrencia exemplarOcorrencia){
		return save(exemplarOcorrencia, null);
	}

	public static Result save(ExemplarOcorrencia exemplarOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(exemplarOcorrencia==null)
				return new Result(-1, "Erro ao salvar. ExemplarOcorrencia é nulo");
			
			exemplarOcorrencia.setDtOcorrencia(new GregorianCalendar());
			GregorianCalendar dtLimite = new GregorianCalendar();
			dtLimite.add(GregorianCalendar.DAY_OF_MONTH, ParametroServices.getValorOfParametroAsInteger("QT_DIAS_EMPRESTIMO", 0, 0, connect));
			exemplarOcorrencia.setDtLimite(dtLimite);

			int retorno;
			if(exemplarOcorrencia.getCdOcorrencia()==0){
				retorno = ExemplarOcorrenciaDAO.insert(exemplarOcorrencia, connect);
				exemplarOcorrencia.setCdOcorrencia(retorno);
			}
			else {
				retorno = ExemplarOcorrenciaDAO.update(exemplarOcorrencia, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "EXEMPLAROCORRENCIA", exemplarOcorrencia);
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
	public static Result remove(int cdOcorrencia, int cdExemplar, int cdPublicacao){
		return remove(cdOcorrencia, cdExemplar, cdPublicacao, false, null);
	}
	public static Result remove(int cdOcorrencia, int cdExemplar, int cdPublicacao, boolean cascade){
		return remove(cdOcorrencia, cdExemplar, cdPublicacao, cascade, null);
	}
	public static Result remove(int cdOcorrencia, int cdExemplar, int cdPublicacao, boolean cascade, Connection connect){
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
			retorno = ExemplarOcorrenciaDAO.delete(cdOcorrencia, cdExemplar, cdPublicacao, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM blb_exemplar_ocorrencia");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExemplarOcorrenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExemplarOcorrenciaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM blb_exemplar_ocorrencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
