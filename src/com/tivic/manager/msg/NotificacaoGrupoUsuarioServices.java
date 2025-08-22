package com.tivic.manager.msg;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class NotificacaoGrupoUsuarioServices {

	public static Result save(NotificacaoGrupoUsuario notificacaoGrupoUsuario){
		return save(notificacaoGrupoUsuario, null, null);
	}

	public static Result save(NotificacaoGrupoUsuario notificacaoGrupoUsuario, AuthData authData){
		return save(notificacaoGrupoUsuario, authData, null);
	}

	public static Result save(NotificacaoGrupoUsuario notificacaoGrupoUsuario, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(notificacaoGrupoUsuario==null)
				return new Result(-1, "Erro ao salvar. NotificacaoGrupoUsuario é nulo");

			int retorno;
			NotificacaoGrupoUsuario obj = NotificacaoGrupoUsuarioDAO.get(notificacaoGrupoUsuario.getCdRegraNotificacao(), notificacaoGrupoUsuario.getCdGrupo(), connect);
			if(obj==null){
				retorno = NotificacaoGrupoUsuarioDAO.insert(notificacaoGrupoUsuario, connect);
			}
			else {
				retorno = NotificacaoGrupoUsuarioDAO.update(notificacaoGrupoUsuario, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "NOTIFICACAOGRUPOUSUARIO", notificacaoGrupoUsuario);
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
	public static Result remove(NotificacaoGrupoUsuario notificacaoGrupoUsuario) {
		return remove(notificacaoGrupoUsuario.getCdRegraNotificacao(), notificacaoGrupoUsuario.getCdGrupo());
	}
	public static Result remove(int cdRegraNotificacao, int cdGrupo){
		return remove(cdRegraNotificacao, cdGrupo, false, null, null);
	}
	public static Result remove(int cdRegraNotificacao, int cdGrupo, boolean cascade){
		return remove(cdRegraNotificacao, cdGrupo, cascade, null, null);
	}
	public static Result remove(int cdRegraNotificacao, int cdGrupo, boolean cascade, AuthData authData){
		return remove(cdRegraNotificacao, cdGrupo, cascade, authData, null);
	}
	public static Result remove(int cdRegraNotificacao, int cdGrupo, boolean cascade, AuthData authData, Connection connect){
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
			retorno = NotificacaoGrupoUsuarioDAO.delete(cdRegraNotificacao, cdGrupo, connect);
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
					.prepareStatement("DELETE FROM msg_notificacao_grupo_usuario WHERE cd_regra_notificacao = "+cdRegraNotificacao);
			
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
			pstmt = connect.prepareStatement("SELECT * FROM msg_notificacao_grupo_usuario");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoGrupoUsuarioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoGrupoUsuarioServices.getAll: " + e);
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
				" SELECT A.*,"
				+ " A1.nm_grupo "
				+ " FROM msg_notificacao_grupo_usuario A"
				+ " JOIN seg_grupo A1 ON (A.cd_grupo = A1.cd_grupo)", 
				criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
