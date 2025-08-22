package com.tivic.manager.msg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class NotificacaoServices {

	public static Result save(Notificacao notificacao){
		return save(notificacao, null, null);
	}

	public static Result save(Notificacao notificacao, AuthData authData){
		return save(notificacao, authData, null);
	}

	public static Result save(Notificacao notificacao, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(notificacao==null)
				return new Result(-1, "Erro ao salvar. Notificacao é nulo");

			int retorno;
			if(notificacao.getCdNotificacao()==0){
				retorno = NotificacaoDAO.insert(notificacao, connect);
				notificacao.setCdNotificacao(retorno);
			}
			else {
				retorno = NotificacaoDAO.update(notificacao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "NOTIFICACAO", notificacao);
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
	public static Result remove(Notificacao notificacao) {
		return remove(notificacao.getCdNotificacao(), notificacao.getCdUsuario());
	}
	public static Result remove(int cdNotificacao, int cdUsuario){
		return remove(cdNotificacao, cdUsuario, false, null, null);
	}
	public static Result remove(int cdNotificacao, int cdUsuario, boolean cascade){
		return remove(cdNotificacao, cdUsuario, cascade, null, null);
	}
	public static Result remove(int cdNotificacao, int cdUsuario, boolean cascade, AuthData authData){
		return remove(cdNotificacao, cdUsuario, cascade, authData, null);
	}
	public static Result remove(int cdNotificacao, int cdUsuario, boolean cascade, AuthData authData, Connection connect){
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
			retorno = NotificacaoDAO.delete(cdNotificacao, cdUsuario, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM msg_notificacao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result marcarLida(int cdNotificacao, int cdUsuario){
		return marcarLida(cdNotificacao, cdUsuario, null);
	}
	
	public static Result marcarLida(int cdNotificacao, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno;
			
			Notificacao notificacao = NotificacaoDAO.get(cdNotificacao, cdUsuario, connect);
			
			if(notificacao==null)
				return new Result(-2, "Notificação não existe.");
			
			notificacao.setDtLeitura(new GregorianCalendar());
			retorno = NotificacaoDAO.update(notificacao, connect);

			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, (retorno<0)?"Erro ao marcar notificação como lida...":"Marcada como lida com sucesso...", "NOTIFICACAO", notificacao);
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
	
	public static Result marcarTodasLida(ArrayList<HashMap<String, Object>> notificacoes) {
		return marcarTodasLida(notificacoes, null);
	}
	
	public static Result marcarTodasLida(ArrayList<HashMap<String, Object>> notificacoes, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
		
			for (HashMap<String, Object> hashMap : notificacoes) {
				retorno = marcarLida((int)hashMap.get("CD_NOTIFICACAO"), (int)hashMap.get("CD_USUARIO"), connect).getCode();
				
				if(retorno<0)
					break;
			}
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, (retorno<0)?"Erro ao marcar notificões como lida...":"Marcada como lida com sucesso...");
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
	
	public static ResultSetMap getAllByUsuario(int cdUsuario) {
		return getAllByUsuario(cdUsuario, null);
	}
	
	public static ResultSetMap getAllByUsuario(int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			String sql = 
					" SELECT A.*,"
					+ " B.tp_entidade, "
					+ " CASE B.tp_entidade"
					+ "		WHEN 0 THEN 'Processo' "  
					+ "     WHEN 1 THEN 'Andamento' " 
					+ "     WHEN 2 THEN 'Arquivo' "  
					+ "     WHEN 3 THEN 'Fatura' "  
					+ "     WHEN 4 THEN 'Agenda' "  
					+ " END AS nm_tp_entidade "
					+ " FROM msg_notificacao A"
					+ " LEFT OUTER JOIN msg_regra_notificacao B ON (A.cd_regra_notificacao = B.cd_regra_notificacao)"
					+ " WHERE A.cd_usuario=?"
					+ " AND A.dt_leitura is null"
					+ " ORDER BY A.dt_notificacao DESC, TP_NOTIFICACAO";
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, cdUsuario);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result getQtNaoLida(int cdUsuario) {
		return getQtNaoLida(cdUsuario, null);
	}
	
	public static Result getQtNaoLida(int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					" SELECT COUNT(*) AS qt_nao_lida FROM msg_notificacao "
					+ " WHERE cd_usuario = ? "
					+ " AND dt_leitura is null");
			pstmt.setInt(1, cdUsuario);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()) {
				return new Result(1, "", "QT_NAO_LIDA", rsm.getInt("qt_nao_lida"));
			}
			else {
				return new Result(1, "", "QT_NAO_LIDA", 0);
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		String sql = 
				" SELECT A.*,"
				+ " B.tp_entidade "
				+ " FROM msg_notificacao A"
				+ " LEFT OUTER JOIN msg_regra_notificacao B ON (A.cd_regra_notificacao = B.cd_regra_notificacao)";
		
		return Search.find(sql, " ORDER BY A.dt_notificacao DESC", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}