package com.tivic.manager.msg;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class NotificacaoUsuarioDAO{

	public static int insert(NotificacaoUsuario objeto) {
		return insert(objeto, null);
	}

	public static int insert(NotificacaoUsuario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO msg_notificacao_usuario (cd_regra_notificacao,"+
			                                  "cd_usuario,"+
			                                  "lg_ativo,"+
			                                  "lg_email) VALUES (?, ?, ?, ?)");
			if(objeto.getCdRegraNotificacao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdRegraNotificacao());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdUsuario());
			pstmt.setInt(3,objeto.getLgAtivo());
			pstmt.setInt(4,objeto.getLgEmail());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoUsuarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoUsuarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(NotificacaoUsuario objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(NotificacaoUsuario objeto, int cdRegraNotificacaoOld, int cdUsuarioOld) {
		return update(objeto, cdRegraNotificacaoOld, cdUsuarioOld, null);
	}

	public static int update(NotificacaoUsuario objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(NotificacaoUsuario objeto, int cdRegraNotificacaoOld, int cdUsuarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE msg_notificacao_usuario SET cd_regra_notificacao=?,"+
												      		   "cd_usuario=?,"+
												      		   "lg_ativo=?,"+
												      		   "lg_email=? WHERE cd_regra_notificacao=? AND cd_usuario=?");
			pstmt.setInt(1,objeto.getCdRegraNotificacao());
			pstmt.setInt(2,objeto.getCdUsuario());
			pstmt.setInt(3,objeto.getLgAtivo());
			pstmt.setInt(4,objeto.getLgEmail());
			pstmt.setInt(5, cdRegraNotificacaoOld!=0 ? cdRegraNotificacaoOld : objeto.getCdRegraNotificacao());
			pstmt.setInt(6, cdUsuarioOld!=0 ? cdUsuarioOld : objeto.getCdUsuario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoUsuarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoUsuarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRegraNotificacao, int cdUsuario) {
		return delete(cdRegraNotificacao, cdUsuario, null);
	}

	public static int delete(int cdRegraNotificacao, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM msg_notificacao_usuario WHERE cd_regra_notificacao=? AND cd_usuario=?");
			pstmt.setInt(1, cdRegraNotificacao);
			pstmt.setInt(2, cdUsuario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoUsuarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoUsuarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static NotificacaoUsuario get(int cdRegraNotificacao, int cdUsuario) {
		return get(cdRegraNotificacao, cdUsuario, null);
	}

	public static NotificacaoUsuario get(int cdRegraNotificacao, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM msg_notificacao_usuario WHERE cd_regra_notificacao=? AND cd_usuario=?");
			pstmt.setInt(1, cdRegraNotificacao);
			pstmt.setInt(2, cdUsuario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new NotificacaoUsuario(rs.getInt("cd_regra_notificacao"),
						rs.getInt("cd_usuario"),
						rs.getInt("lg_ativo"),
						rs.getInt("lg_email"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoUsuarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoUsuarioDAO.get: " + e);
			return null;
		}
		finally {
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
			System.err.println("Erro! NotificacaoUsuarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoUsuarioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<NotificacaoUsuario> getList() {
		return getList(null);
	}

	public static ArrayList<NotificacaoUsuario> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<NotificacaoUsuario> list = new ArrayList<NotificacaoUsuario>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				NotificacaoUsuario obj = NotificacaoUsuarioDAO.get(rsm.getInt("cd_regra_notificacao"), rsm.getInt("cd_usuario"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoUsuarioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM msg_notificacao_usuario", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
