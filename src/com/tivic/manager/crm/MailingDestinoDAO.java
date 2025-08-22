package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class MailingDestinoDAO{

	public static int insert(MailingDestino objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(MailingDestino objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_destino");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_mailing");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdMailing()));
			int code = Conexao.getSequenceCode("crm_mailing_destino", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDestino(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_mailing_destino (cd_destino,"+
			                                  "cd_mailing,"+
			                                  "cd_grupo,"+
			                                  "cd_pessoa,"+
			                                  "cd_fonte,"+
			                                  "cd_agendamento) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdMailing()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMailing());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdGrupo());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdPessoa());
			if(objeto.getCdFonte()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdFonte());
			if(objeto.getCdAgendamento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdAgendamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingDestinoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingDestinoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MailingDestino objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(MailingDestino objeto, int cdDestinoOld, int cdMailingOld) {
		return update(objeto, cdDestinoOld, cdMailingOld, null);
	}

	public static int update(MailingDestino objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(MailingDestino objeto, int cdDestinoOld, int cdMailingOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_mailing_destino SET cd_destino=?,"+
												      		   "cd_mailing=?,"+
												      		   "cd_grupo=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_fonte=?,"+
												      		   "cd_agendamento=? WHERE cd_destino=? AND cd_mailing=?");
			pstmt.setInt(1,objeto.getCdDestino());
			pstmt.setInt(2,objeto.getCdMailing());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdGrupo());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdPessoa());
			if(objeto.getCdFonte()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdFonte());
			if(objeto.getCdAgendamento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdAgendamento());
			pstmt.setInt(7, cdDestinoOld!=0 ? cdDestinoOld : objeto.getCdDestino());
			pstmt.setInt(8, cdMailingOld!=0 ? cdMailingOld : objeto.getCdMailing());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingDestinoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingDestinoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDestino, int cdMailing) {
		return delete(cdDestino, cdMailing, null);
	}

	public static int delete(int cdDestino, int cdMailing, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_mailing_destino WHERE cd_destino=? AND cd_mailing=?");
			pstmt.setInt(1, cdDestino);
			pstmt.setInt(2, cdMailing);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingDestinoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingDestinoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MailingDestino get(int cdDestino, int cdMailing) {
		return get(cdDestino, cdMailing, null);
	}

	public static MailingDestino get(int cdDestino, int cdMailing, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_mailing_destino WHERE cd_destino=? AND cd_mailing=?");
			pstmt.setInt(1, cdDestino);
			pstmt.setInt(2, cdMailing);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MailingDestino(rs.getInt("cd_destino"),
						rs.getInt("cd_mailing"),
						rs.getInt("cd_grupo"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_fonte"),
						rs.getInt("cd_agendamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingDestinoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingDestinoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_mailing_destino");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingDestinoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingDestinoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM crm_mailing_destino", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
