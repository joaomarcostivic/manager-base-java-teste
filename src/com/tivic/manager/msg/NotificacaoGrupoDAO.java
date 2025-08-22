package com.tivic.manager.msg;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class NotificacaoGrupoDAO{

	public static int insert(NotificacaoGrupo objeto) {
		return insert(objeto, null);
	}

	public static int insert(NotificacaoGrupo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO msg_notificacao_grupo (cd_regra_notificacao,"+
			                                  "cd_grupo,"+
			                                  "lg_ativo,"+
			                                  "lg_email) VALUES (?, ?, ?, ?)");
			if(objeto.getCdRegraNotificacao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdRegraNotificacao());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdGrupo());
			pstmt.setInt(3,objeto.getLgAtivo());
			pstmt.setInt(4,objeto.getLgEmail());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoGrupoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoGrupoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(NotificacaoGrupo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(NotificacaoGrupo objeto, int cdRegraNotificacaoOld, int cdGrupoOld) {
		return update(objeto, cdRegraNotificacaoOld, cdGrupoOld, null);
	}

	public static int update(NotificacaoGrupo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(NotificacaoGrupo objeto, int cdRegraNotificacaoOld, int cdGrupoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE msg_notificacao_grupo SET cd_regra_notificacao=?,"+
												      		   "cd_grupo=?,"+
												      		   "lg_ativo=?,"+
												      		   "lg_email=? WHERE cd_regra_notificacao=? AND cd_grupo=?");
			pstmt.setInt(1,objeto.getCdRegraNotificacao());
			pstmt.setInt(2,objeto.getCdGrupo());
			pstmt.setInt(3,objeto.getLgAtivo());
			pstmt.setInt(4,objeto.getLgEmail());
			pstmt.setInt(5, cdRegraNotificacaoOld!=0 ? cdRegraNotificacaoOld : objeto.getCdRegraNotificacao());
			pstmt.setInt(6, cdGrupoOld!=0 ? cdGrupoOld : objeto.getCdGrupo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoGrupoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoGrupoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRegraNotificacao, int cdGrupo) {
		return delete(cdRegraNotificacao, cdGrupo, null);
	}

	public static int delete(int cdRegraNotificacao, int cdGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM msg_notificacao_grupo WHERE cd_regra_notificacao=? AND cd_grupo=?");
			pstmt.setInt(1, cdRegraNotificacao);
			pstmt.setInt(2, cdGrupo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoGrupoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoGrupoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static NotificacaoGrupo get(int cdRegraNotificacao, int cdGrupo) {
		return get(cdRegraNotificacao, cdGrupo, null);
	}

	public static NotificacaoGrupo get(int cdRegraNotificacao, int cdGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM msg_notificacao_grupo WHERE cd_regra_notificacao=? AND cd_grupo=?");
			pstmt.setInt(1, cdRegraNotificacao);
			pstmt.setInt(2, cdGrupo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new NotificacaoGrupo(rs.getInt("cd_regra_notificacao"),
						rs.getInt("cd_grupo"),
						rs.getInt("lg_ativo"),
						rs.getInt("lg_email"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoGrupoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoGrupoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM msg_notificacao_grupo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoGrupoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoGrupoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<NotificacaoGrupo> getList() {
		return getList(null);
	}

	public static ArrayList<NotificacaoGrupo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<NotificacaoGrupo> list = new ArrayList<NotificacaoGrupo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				NotificacaoGrupo obj = NotificacaoGrupoDAO.get(rsm.getInt("cd_regra_notificacao"), rsm.getInt("cd_grupo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoGrupoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM msg_notificacao_grupo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
