package com.tivic.manager.msg;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class MensagemDestinatarioDAO{

	public static int insert(MensagemDestinatario objeto) {
		return insert(objeto, null);
	}

	public static int insert(MensagemDestinatario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO msg_mensagem_destinatario (cd_pessoa,"+
			                                  "cd_mensagem,"+
			                                  "dt_leitura,"+
			                                  "lg_confirmada," +
			                                  "st_mensagem_destinatario) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdMensagem()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMensagem());
			if(objeto.getDtLeitura()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtLeitura().getTimeInMillis()));
			pstmt.setInt(4,objeto.getLgConfirmada());
			pstmt.setInt(5,objeto.getStMensagemDestinatario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MensagemDestinatarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MensagemDestinatarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MensagemDestinatario objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(MensagemDestinatario objeto, int cdUsuarioOld, int cdMensagemOld) {
		return update(objeto, cdUsuarioOld, cdMensagemOld, null);
	}

	public static int update(MensagemDestinatario objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(MensagemDestinatario objeto, int cdUsuarioOld, int cdMensagemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE msg_mensagem_destinatario SET cd_pessoa=?,"+
												      		   "cd_mensagem=?,"+
												      		   "dt_leitura=?,"+
												      		   "lg_confirmada=?," +
												      		   "st_mensagem_destinatario=? WHERE cd_pessoa=? AND cd_mensagem=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getCdMensagem());
			if(objeto.getDtLeitura()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtLeitura().getTimeInMillis()));
			pstmt.setInt(4,objeto.getLgConfirmada());
			pstmt.setInt(5,objeto.getStMensagemDestinatario());
			pstmt.setInt(6, cdUsuarioOld!=0 ? cdUsuarioOld : objeto.getCdPessoa());
			pstmt.setInt(7, cdMensagemOld!=0 ? cdMensagemOld : objeto.getCdMensagem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MensagemDestinatarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MensagemDestinatarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdUsuario, int cdMensagem) {
		return delete(cdUsuario, cdMensagem, null);
	}

	public static int delete(int cdUsuario, int cdMensagem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM msg_mensagem_destinatario WHERE cd_pessoa=? AND cd_mensagem=?");
			pstmt.setInt(1, cdUsuario);
			pstmt.setInt(2, cdMensagem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MensagemDestinatarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MensagemDestinatarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MensagemDestinatario get(int cdUsuario, int cdMensagem) {
		return get(cdUsuario, cdMensagem, null);
	}

	public static MensagemDestinatario get(int cdUsuario, int cdMensagem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM msg_mensagem_destinatario WHERE cd_pessoa=? AND cd_mensagem=?");
			pstmt.setInt(1, cdUsuario);
			pstmt.setInt(2, cdMensagem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MensagemDestinatario(rs.getInt("cd_pessoa"),
						rs.getInt("cd_mensagem"),
						(rs.getTimestamp("dt_leitura")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_leitura").getTime()),
						rs.getInt("lg_confirmada"),
						rs.getInt("st_mensagem_destinatario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MensagemDestinatarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MensagemDestinatarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM msg_mensagem_destinatario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MensagemDestinatarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MensagemDestinatarioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<MensagemDestinatario> getList() {
		return getList(null);
	}

	public static ArrayList<MensagemDestinatario> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<MensagemDestinatario> list = new ArrayList<MensagemDestinatario>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				MensagemDestinatario obj = MensagemDestinatarioDAO.get(rsm.getInt("cd_pessoa"), rsm.getInt("cd_mensagem"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MensagemDestinatarioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM msg_mensagem_destinatario", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
