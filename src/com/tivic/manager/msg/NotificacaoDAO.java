package com.tivic.manager.msg;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class NotificacaoDAO{

	public static int insert(Notificacao objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(Notificacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "CD_NOTIFICACAO");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "CD_USUARIO");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdUsuario()));
			int code = Conexao.getSequenceCode("MSG_NOTIFICACAO", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdNotificacao(code);			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO MSG_NOTIFICACAO (CD_NOTIFICACAO,"+
			                                  "CD_USUARIO,"+
			                                  "DS_ASSUNTO,"+
			                                  "TP_NOTIFICACAO,"+
			                                  "TXT_NOTIFICACAO,"+
			                                  "DT_NOTIFICACAO,"+
			                                  "DT_LEITURA,"+
			                                  "CD_MENSAGEM,"+
			                                  "CD_REGRA_NOTIFICACAO,"+
			                                  "TXT_OBJETO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdUsuario());
			pstmt.setString(3,objeto.getDsAssunto());
			pstmt.setInt(4,objeto.getTpNotificacao());
			if(objeto.getTxtNotificacao()==null)
				pstmt.setNull(5, Types.BINARY);
			else
				pstmt.setString(5,objeto.getTxtNotificacao());
			if(objeto.getDtNotificacao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtNotificacao().getTimeInMillis()));
			if(objeto.getDtLeitura()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtLeitura().getTimeInMillis()));
			if(objeto.getCdMensagem()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdMensagem());
			if(objeto.getCdRegraNotificacao()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdRegraNotificacao());
			if(objeto.getTxtObjeto()==null)
				pstmt.setNull(10, Types.VARCHAR);
			else
				pstmt.setString(10,objeto.getTxtObjeto());
			
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Notificacao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Notificacao objeto, int cdNotificacaoOld, int cdUsuarioOld) {
		return update(objeto, cdNotificacaoOld, cdUsuarioOld, null);
	}

	public static int update(Notificacao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Notificacao objeto, int cdNotificacaoOld, int cdUsuarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE MSG_NOTIFICACAO SET CD_NOTIFICACAO=?,"+
												      		   "CD_USUARIO=?,"+
												      		   "DS_ASSUNTO=?,"+
												      		   "TP_NOTIFICACAO=?,"+
												      		   "TXT_NOTIFICACAO=?,"+
												      		   "DT_NOTIFICACAO=?,"+
												      		   "DT_LEITURA=?,"+
												      		   "CD_MENSAGEM=?,"+
												      		   "CD_REGRA_NOTIFICACAO=?,"+
												      		   "TXT_OBJETO=? WHERE CD_NOTIFICACAO=? AND CD_USUARIO=?");
			pstmt.setInt(1,objeto.getCdNotificacao());
			pstmt.setInt(2,objeto.getCdUsuario());
			pstmt.setString(3,objeto.getDsAssunto());
			pstmt.setInt(4,objeto.getTpNotificacao());
			if(objeto.getTxtNotificacao()==null)
				pstmt.setNull(5, Types.BINARY);
			else
				pstmt.setString(5,objeto.getTxtNotificacao());
			if(objeto.getDtNotificacao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtNotificacao().getTimeInMillis()));
			if(objeto.getDtLeitura()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtLeitura().getTimeInMillis()));
			if(objeto.getCdMensagem()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdMensagem());
			if(objeto.getCdRegraNotificacao()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdRegraNotificacao());
			if(objeto.getTxtObjeto()==null)
				pstmt.setNull(10, Types.VARCHAR);
			else
				pstmt.setString(10,objeto.getTxtObjeto());
			pstmt.setInt(11, cdNotificacaoOld!=0 ? cdNotificacaoOld : objeto.getCdNotificacao());
			pstmt.setInt(12, cdUsuarioOld!=0 ? cdUsuarioOld : objeto.getCdUsuario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdNotificacao, int cdUsuario) {
		return delete(cdNotificacao, cdUsuario, null);
	}

	public static int delete(int cdNotificacao, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM MSG_NOTIFICACAO WHERE CD_NOTIFICACAO=? AND CD_USUARIO=?");
			pstmt.setInt(1, cdNotificacao);
			pstmt.setInt(2, cdUsuario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Notificacao get(int cdNotificacao, int cdUsuario) {
		return get(cdNotificacao, cdUsuario, null);
	}

	public static Notificacao get(int cdNotificacao, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM MSG_NOTIFICACAO WHERE CD_NOTIFICACAO=? AND CD_USUARIO=?");
			pstmt.setInt(1, cdNotificacao);
			pstmt.setInt(2, cdUsuario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Notificacao(rs.getInt("CD_NOTIFICACAO"),
						rs.getInt("CD_USUARIO"),
						rs.getString("DS_ASSUNTO"),
						rs.getInt("TP_NOTIFICACAO"),
						rs.getString("TXT_NOTIFICACAO"),
						(rs.getTimestamp("DT_NOTIFICACAO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_NOTIFICACAO").getTime()),
						(rs.getTimestamp("DT_LEITURA")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_LEITURA").getTime()),
						rs.getInt("CD_MENSAGEM"),
						rs.getInt("CD_REGRA_NOTIFICACAO"),
						rs.getString("TXT_OBJETO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM MSG_NOTIFICACAO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Notificacao> getList() {
		return getList(null);
	}

	public static ArrayList<Notificacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Notificacao> list = new ArrayList<Notificacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Notificacao obj = NotificacaoDAO.get(rsm.getInt("CD_NOTIFICACAO"), rsm.getInt("CD_USUARIO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotificacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM MSG_NOTIFICACAO", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
