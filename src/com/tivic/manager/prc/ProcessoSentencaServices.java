package com.tivic.manager.prc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ProcessoSentencaServices {

	public static Result save(ProcessoSentenca processoSentenca){
		return save(processoSentenca, null, null);
	}

	public static Result save(ProcessoSentenca processoSentenca, AuthData authData){
		return save(processoSentenca, authData, null);
	}

	public static Result save(ProcessoSentenca processoSentenca, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(processoSentenca==null)
				return new Result(-1, "Erro ao salvar. ProcessoSentenca é nulo");

			int retorno;
			ProcessoSentenca ps = ProcessoSentencaDAO.get(processoSentenca.getCdSentenca(), processoSentenca.getCdProcesso(), connect);
			if(ps==null){
				retorno = ProcessoSentencaDAO.insert(processoSentenca, connect);
				processoSentenca.setCdSentenca(retorno);
			}
			else {
				retorno = ProcessoSentencaDAO.update(processoSentenca, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PROCESSOSENTENCA", processoSentenca);
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
	public static Result remove(int cdSentenca, int cdProcesso){
		return remove(cdSentenca, cdProcesso, false, null, null);
	}
	public static Result remove(int cdSentenca, int cdProcesso, boolean cascade){
		return remove(cdSentenca, cdProcesso, cascade, null, null);
	}
	public static Result remove(int cdSentenca, int cdProcesso, boolean cascade, AuthData authData){
		return remove(cdSentenca, cdProcesso, cascade, authData, null);
	}
	public static Result remove(int cdSentenca, int cdProcesso, boolean cascade, AuthData authData, Connection connect){
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
			retorno = ProcessoSentencaDAO.delete(cdSentenca, cdProcesso, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_processo_sentenca");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoSentencaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoSentencaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByProcesso(int cdProcesso) {
		return getAllByProcesso(cdProcesso, null);
	}

	public static ResultSetMap getAllByProcesso(int cdProcesso, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_processo_sentenca "
					+ " WHERE cd_processo="+cdProcesso+" ORDER BY dt_sentenca DESC");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoSentencaServices.getAllByProcesso: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoSentencaServices.getAllByProcesso: " + e);
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
		return Search.find("SELECT * FROM prc_processo_sentenca", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}