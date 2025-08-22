package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class MailingDAO{

	public static int insert(Mailing objeto) {
		return insert(objeto, null);
	}

	public static int insert(Mailing objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("crm_mailing", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMailing(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_mailing (cd_mailing,"+
			                                  "nm_mailing,"+
			                                  "txt_mailing,"+
			                                  "cd_modelo,"+
			                                  "cd_grupo,"+
			                                  "cd_conta_envio,"+
			                                  "nm_assunto) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmMailing());
			pstmt.setString(3,objeto.getTxtMailing());
			if(objeto.getCdModelo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdModelo());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdGrupo());
			if(objeto.getCdContaEnvio()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdContaEnvio());
			pstmt.setString(7,objeto.getNmAssunto());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Mailing objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Mailing objeto, int cdMailingOld) {
		return update(objeto, cdMailingOld, null);
	}

	public static int update(Mailing objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Mailing objeto, int cdMailingOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_mailing SET cd_mailing=?,"+
												      		   "nm_mailing=?,"+
												      		   "txt_mailing=?,"+
												      		   "cd_modelo=?,"+
												      		   "cd_grupo=?,"+
												      		   "cd_conta_envio=?,"+
												      		   "nm_assunto=? WHERE cd_mailing=?");
			pstmt.setInt(1,objeto.getCdMailing());
			pstmt.setString(2,objeto.getNmMailing());
			pstmt.setString(3,objeto.getTxtMailing());
			if(objeto.getCdModelo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdModelo());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdGrupo());
			if(objeto.getCdContaEnvio()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdContaEnvio());
			pstmt.setString(7,objeto.getNmAssunto());
			pstmt.setInt(8, cdMailingOld!=0 ? cdMailingOld : objeto.getCdMailing());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMailing) {
		return delete(cdMailing, null);
	}

	public static int delete(int cdMailing, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_mailing WHERE cd_mailing=?");
			pstmt.setInt(1, cdMailing);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Mailing get(int cdMailing) {
		return get(cdMailing, null);
	}

	public static Mailing get(int cdMailing, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_mailing WHERE cd_mailing=?");
			pstmt.setInt(1, cdMailing);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Mailing(rs.getInt("cd_mailing"),
						rs.getString("nm_mailing"),
						rs.getString("txt_mailing"),
						rs.getInt("cd_modelo"),
						rs.getInt("cd_grupo"),
						rs.getInt("cd_conta_envio"),
						rs.getString("nm_assunto"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_mailing");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM crm_mailing", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
