package com.tivic.manager.adm;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class FechamentoOcorrenciaServices {

	public static Result save(FechamentoOcorrencia fechamentoOcorrencia){
		return save(fechamentoOcorrencia, null);
	}

	public static Result save(FechamentoOcorrencia fechamentoOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(fechamentoOcorrencia==null)
				return new Result(-1, "Erro ao salvar. FechamentoOcorrencia é nulo");

			int retorno;
			if(fechamentoOcorrencia.getCdFechamentoOcorrencia()==0){
				retorno = FechamentoOcorrenciaDAO.insert(fechamentoOcorrencia, connect);
				fechamentoOcorrencia.setCdFechamentoOcorrencia(retorno);
			}
			else {
				retorno = FechamentoOcorrenciaDAO.update(fechamentoOcorrencia, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "FECHAMENTOOCORRENCIA", fechamentoOcorrencia);
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
	public static Result remove(int cdFechamentoOcorrencia){
		return remove(cdFechamentoOcorrencia, false, null);
	}
	public static Result remove(int cdFechamentoOcorrencia, boolean cascade){
		return remove(cdFechamentoOcorrencia, cascade, null);
	}
	public static Result remove(int cdFechamentoOcorrencia, boolean cascade, Connection connect){
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
			retorno = FechamentoOcorrenciaDAO.delete(cdFechamentoOcorrencia, connect);
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
	
	public static Result removeAll(int cdConta, int cdFechamento){
		return removeAll(cdConta, cdFechamento, null);
	}
	public static Result removeAll(int cdConta, int cdFechamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			connect.prepareStatement("DELETE FROM adm_fechamento_ocorrencia "+
					 		" WHERE cd_conta ="+cdConta+"  and cd_fechamento = "+cdFechamento).executeUpdate();
			
			if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registros excluídos com sucesso!");
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_fechamento_ocorrencia");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FechamentoOcorrenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoOcorrenciaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByFechamento(int cdFechamento) {
		return getAllByFechamento(cdFechamento, null);
	}

	public static ResultSetMap getAllByFechamento(int cdFechamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.*, C.nm_pessoa FROM adm_fechamento_ocorrencia A "+
											 " JOIN SEG_USUARIO  B ON ( A.cd_usuario = B.cd_usuario ) "+
											 " JOIN GRL_PESSOA   C ON ( B.cd_pessoa = C.cd_pessoa )   "+
											 " WHERE A.cd_fechamento = "+cdFechamento+
											 " ORDER BY A.dt_ocorrencia DESC ");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FechamentoOcorrenciaServices.getAllByFechamento: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoOcorrenciaServices.getAllByFechamento: " + e);
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
		return Search.find("SELECT * FROM adm_fechamento_ocorrencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
