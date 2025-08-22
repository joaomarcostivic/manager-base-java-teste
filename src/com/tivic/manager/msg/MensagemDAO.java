package com.tivic.manager.msg;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class MensagemDAO{

	public static int insert(Mensagem objeto) {
		return insert(objeto, null);
	}

	public static int insert(Mensagem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("msg_mensagem", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMensagem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO MSG_MENSAGEM (CD_MENSAGEM,"+
                    "DS_ASSUNTO,"+
                    "TXT_MENSAGEM,"+
                    "CD_USUARIO_ORIGEM,"+
                    "DT_ENVIO,"+
                    "LG_CONFIRMACAO,"+
                    "LG_IMPORTANTE,"+
                    "LG_COPIA_EMAIL,"+
                    "CD_MENSAGEM_ORIGEM,"+
                    "CD_PROCESSO,"+
                    "CD_AGENDA_ITEM,"+
                    "ST_MENSAGEM_REMETENTE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getDsAssunto());
			pstmt.setString(3,objeto.getTxtMensagem());
			if(objeto.getCdUsuarioOrigem()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuarioOrigem());
			if(objeto.getDtEnvio()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtEnvio().getTimeInMillis()));
			pstmt.setInt(6,objeto.getLgConfirmacao());
			pstmt.setInt(7,objeto.getLgImportante());
			pstmt.setInt(8,objeto.getLgCopiaEmail());
			if(objeto.getCdMensagemOrigem()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdMensagemOrigem());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdProcesso());
			if(objeto.getCdAgendaItem()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdAgendaItem());
			pstmt.setInt(12,objeto.getStMensagemRemetente());
			
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MensagemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MensagemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Mensagem objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Mensagem objeto, int cdMensagemOld) {
		return update(objeto, cdMensagemOld, null);
	}

	public static int update(Mensagem objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Mensagem objeto, int cdMensagemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE MSG_MENSAGEM SET CD_MENSAGEM=?,"+
		      		   "DS_ASSUNTO=?,"+
		      		   "TXT_MENSAGEM=?,"+
		      		   "CD_USUARIO_ORIGEM=?,"+
		      		   "DT_ENVIO=?,"+
		      		   "LG_CONFIRMACAO=?,"+
		      		   "LG_IMPORTANTE=?,"+
		      		   "LG_COPIA_EMAIL=?,"+
		      		   "CD_MENSAGEM_ORIGEM=?,"+
		      		   "CD_PROCESSO=?,"+
		      		   "CD_AGENDA_ITEM=?,"+
		      		   "ST_MENSAGEM_REMETENTE=? WHERE CD_MENSAGEM=?");
			pstmt.setInt(1,objeto.getCdMensagem());
			pstmt.setString(2,objeto.getDsAssunto());
			pstmt.setString(3,objeto.getTxtMensagem());
			if(objeto.getCdUsuarioOrigem()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuarioOrigem());
			if(objeto.getDtEnvio()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtEnvio().getTimeInMillis()));
			pstmt.setInt(6,objeto.getLgConfirmacao());
			pstmt.setInt(7,objeto.getLgImportante());
			pstmt.setInt(8,objeto.getLgCopiaEmail());
			if(objeto.getCdMensagemOrigem()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdMensagemOrigem());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdProcesso());
			if(objeto.getCdAgendaItem()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdAgendaItem());
			pstmt.setInt(12,objeto.getStMensagemRemetente());
			pstmt.setInt(13, cdMensagemOld!=0 ? cdMensagemOld : objeto.getCdMensagem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MensagemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MensagemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMensagem) {
		return delete(cdMensagem, null);
	}

	public static int delete(int cdMensagem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM msg_mensagem WHERE cd_mensagem=?");
			pstmt.setInt(1, cdMensagem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MensagemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MensagemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Mensagem get(int cdMensagem) {
		return get(cdMensagem, null);
	}

	public static Mensagem get(int cdMensagem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM msg_mensagem WHERE cd_mensagem=?");
			pstmt.setInt(1, cdMensagem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Mensagem(rs.getInt("cd_mensagem"),
						rs.getString("ds_assunto"),
						rs.getString("txt_mensagem"),
						rs.getInt("cd_usuario_origem"),
						(rs.getTimestamp("dt_envio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_envio").getTime()),
						rs.getInt("lg_confirmacao"),
						rs.getInt("lg_importante"),
						rs.getInt("lg_copia_email"),
						rs.getInt("cd_mensagem_origem"),
						rs.getInt("cd_processo"),
						rs.getInt("cd_agenda_item"),
						rs.getInt("st_mensagem_remetente"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MensagemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MensagemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM msg_mensagem");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MensagemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MensagemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Mensagem> getList() {
		return getList(null);
	}

	public static ArrayList<Mensagem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Mensagem> list = new ArrayList<Mensagem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Mensagem obj = MensagemDAO.get(rsm.getInt("cd_mensagem"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MensagemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM msg_mensagem", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
