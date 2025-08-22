package com.tivic.manager.log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ExecucaoAcaoServices {

	public static Result save(ExecucaoAcao execucaoAcao){
		return save(execucaoAcao, null);
	}

	public static Result save(ExecucaoAcao execucaoAcao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(execucaoAcao==null)
				return new Result(-1, "Erro ao salvar. ExecucaoAcao é nulo");

			int retorno;
			if(execucaoAcao.getCdExecucao()==0){
				retorno = ExecucaoAcaoDAO.insert(execucaoAcao, connect);
				execucaoAcao.setCdExecucao(retorno);
			}
			else {
				retorno = ExecucaoAcaoDAO.update(execucaoAcao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "EXECUCAOACAO", execucaoAcao);
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
	public static Result remove(int cdExecucao, int cdAcao, int cdModulo, int cdSistema){
		return remove(cdExecucao, cdAcao, cdModulo, cdSistema, false, null);
	}
	public static Result remove(int cdExecucao, int cdAcao, int cdModulo, int cdSistema, boolean cascade){
		return remove(cdExecucao, cdAcao, cdModulo, cdSistema, cascade, null);
	}
	public static Result remove(int cdExecucao, int cdAcao, int cdModulo, int cdSistema, boolean cascade, Connection connect){
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
			retorno = ExecucaoAcaoDAO.delete(cdExecucao, cdAcao, cdModulo, cdSistema, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM log_execucao_acao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExecucaoAcaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExecucaoAcaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllByAcao() {
		return getAllByAcao(null);
	}

	public static ResultSetMap getAllByAcao(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * " + 
									"FROM log_execucao_acao "+
									"WHERE cd_acao = ? "+
									"AND cd_modulo = ? "+
									"AND cd_sistema = ?");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExecucaoAcaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExecucaoAcaoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM log_execucao_acao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	
//	public static int deleleLogs(ServletRequest request) {
//		return deleleLogs(request, null);
//	}
//
//	public static int deleleLogs(ServletRequest request, Connection connection) {
//		boolean isConnectionNull = connection==null;
//		try {
//			if (isConnectionNull) {
//				connection = Conexao.conectar();
//				connection.setAutoCommit(false);
//			}
//
//			int countObjetos = RequestUtilities.getAsInteger(request, "countObjetos", 0);
//			for (int i=0; i<countObjetos; i++) {
//				int cdExecucao = RequestUtilities.getAsInteger(request, "cdExecucao_" + i, 0);
//				if (ExecucaoAcaoDAO.delete(cdExecucao, connection) <= 0) {
//					if (isConnectionNull)
//						Conexao.rollback(connection);
//					return -1;
//				}
//
//			}
//
//			if (isConnectionNull)
//				connection.commit();
//
//			return 1;
//		}
//		catch(Exception e) {
//			e.printStackTrace(System.out);
//			if (isConnectionNull)
//				Conexao.rollback(connection);
//			return -1;
//		}
//		finally {
//			if (isConnectionNull)
//				Conexao.desconectar(connection);
//		}
//	}
//
//	public static int deleleLogs(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
//		return deleleLogs(dtInicial, dtFinal, null);
//	}
//
//	public static int deleleLogs(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connection) {
//		boolean isConnectionNull = connection==null;
//		try {
//			if (isConnectionNull)
//				connection = Conexao.conectar();
//
//			PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) AS COUNT " +
//					"FROM log_execucao_acao " +
//					"WHERE 1 = 1 " +
//					(dtInicial != null ? "  AND dt_execucao >= ? " : "") +
//					(dtFinal != null ? "  AND dt_execucao <= ?" : ""));
//			int i = 1;
//			if (dtInicial != null)
//				pstmt.setTimestamp(i++, Util.convCalendarToTimestamp(dtInicial));
//			if (dtFinal != null) {
//				dtFinal.set(Calendar.SECOND, 59);
//				dtFinal.set(Calendar.MILLISECOND, 999);
//				pstmt.setTimestamp(i++, Util.convCalendarToTimestamp(dtFinal));
//			}
//			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
//
//			if (rsm.next()) {
//				pstmt = connection.prepareStatement("DELETE FROM log_execucao_acao " +
//						"WHERE 1 = 1 " +
//						(dtInicial != null ? "  AND dt_execucao >= ? " : "") +
//						(dtFinal != null ? "  AND dt_execucao <= ?" : ""));
//				i = 1;
//				if (dtInicial != null)
//					pstmt.setTimestamp(i++, Util.convCalendarToTimestamp(dtInicial));
//				if (dtFinal != null) {
//					dtFinal.set(Calendar.SECOND, 59);
//					dtFinal.set(Calendar.MILLISECOND, 999);
//					pstmt.setTimestamp(i++, Util.convCalendarToTimestamp(dtFinal));
//				}
//				pstmt.execute();
//
//				return rsm.getInt("COUNT");
//			}
//			else
//				return -1;
//		}
//		catch(Exception e) {
//			e.printStackTrace(System.out);
//			if (isConnectionNull)
//				Conexao.rollback(connection);
//			return -1;
//		}
//		finally {
//			if (isConnectionNull)
//				Conexao.desconectar(connection);
//		}
//	}

}