package com.tivic.manager.msg;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class NotificacaoUsuarioServices {

	public static Result save(NotificacaoUsuario notificacaoUsuario){
		return save(notificacaoUsuario, null, null);
	}

	public static Result save(NotificacaoUsuario notificacaoUsuario, AuthData authData){
		return save(notificacaoUsuario, authData, null);
	}

	public static Result save(NotificacaoUsuario notificacaoUsuario, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(notificacaoUsuario==null)
				return new Result(-1, "Erro ao salvar. NotificacaoUsuario é nulo");

			int retorno;
			NotificacaoUsuario obj = NotificacaoUsuarioDAO.get(notificacaoUsuario.getCdRegraNotificacao(), notificacaoUsuario.getCdUsuario(), connect);
			if(obj==null) {
				retorno = NotificacaoUsuarioDAO.insert(notificacaoUsuario, connect);
			}
			else {
				retorno = NotificacaoUsuarioDAO.update(notificacaoUsuario, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "NOTIFICACAOUSUARIO", notificacaoUsuario);
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
	public static Result remove(NotificacaoUsuario notificacaoUsuario) {
		return remove(notificacaoUsuario.getCdRegraNotificacao(), notificacaoUsuario.getCdUsuario());
	}
	public static Result remove(int cdRegraNotificacao, int cdUsuario){
		return remove(cdRegraNotificacao, cdUsuario, false, null, null);
	}
	public static Result remove(int cdRegraNotificacao, int cdUsuario, boolean cascade){
		return remove(cdRegraNotificacao, cdUsuario, cascade, null, null);
	}
	public static Result remove(int cdRegraNotificacao, int cdUsuario, boolean cascade, AuthData authData){
		return remove(cdRegraNotificacao, cdUsuario, cascade, authData, null);
	}
	public static Result remove(int cdRegraNotificacao, int cdUsuario, boolean cascade, AuthData authData, Connection connect){
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
				retorno = NotificacaoUsuarioDAO.delete(cdRegraNotificacao, cdUsuario, connect);
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
	
	public static Result removeAll(int cdRegraNotificacao, AuthData authData) {
		return removeAll(cdRegraNotificacao, authData, null);
	}
	
	public static Result removeAll(int cdRegraNotificacao, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			
			PreparedStatement pstmt = connect
					.prepareStatement("DELETE FROM msg_notificacao_usuario WHERE cd_regra_notificacao = "+cdRegraNotificacao);
			
			retorno = pstmt.executeUpdate();
			
			if(retorno<0) {
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(retorno, "Erro ao remover usuários.");
			}
							
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
			pstmt = connect.prepareStatement("SELECT * FROM msg_notificacao_usuario");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoUsuarioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoUsuarioServices.getAll: " + e);
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
		return Search.find(
				" SELECT A.*, "
				+ " B.nm_pessoa AS nm_usuario "
				+ " FROM msg_notificacao_usuario A"
				+ " JOIN seg_usuario A1 ON (A.cd_usuario = A1.cd_usuario)"
				+ " JOIN grl_pessoa B ON (A1.cd_pessoa = B.cd_pessoa)", 
				criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
