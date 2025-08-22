package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class MailingPlanejamentoDAO{

	public static int insert(MailingPlanejamento objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(MailingPlanejamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_planejamento");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_mailing");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdMailing()));
			int code = Conexao.getSequenceCode("crm_mailing_planejamento", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPlanejamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_mailing_planejamento (cd_planejamento,"+
			                                  "cd_mailing,"+
			                                  "dt_planejamento,"+
			                                  "dt_envio,"+
			                                  "st_planejamento,"+
			                                  "cd_usuario,"+
			                                  "cd_conta_envio,"+
			                                  "nm_assunto,"+
			                                  "txt_parametros) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdMailing()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMailing());
			if(objeto.getDtPlanejamento()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtPlanejamento().getTimeInMillis()));
			if(objeto.getDtEnvio()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtEnvio().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStPlanejamento());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdUsuario());
			if(objeto.getCdContaEnvio()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdContaEnvio());
			pstmt.setString(8,objeto.getNmAssunto());
			pstmt.setString(9,objeto.getTxtParametros());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingPlanejamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingPlanejamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MailingPlanejamento objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(MailingPlanejamento objeto, int cdPlanejamentoOld, int cdMailingOld) {
		return update(objeto, cdPlanejamentoOld, cdMailingOld, null);
	}

	public static int update(MailingPlanejamento objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(MailingPlanejamento objeto, int cdPlanejamentoOld, int cdMailingOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_mailing_planejamento SET cd_planejamento=?,"+
												      		   "cd_mailing=?,"+
												      		   "dt_planejamento=?,"+
												      		   "dt_envio=?,"+
												      		   "st_planejamento=?,"+
												      		   "cd_usuario=?,"+
												      		   "cd_conta_envio=?,"+
												      		   "nm_assunto=?,"+
												      		   "txt_parametros=? WHERE cd_planejamento=? AND cd_mailing=?");
			pstmt.setInt(1,objeto.getCdPlanejamento());
			pstmt.setInt(2,objeto.getCdMailing());
			if(objeto.getDtPlanejamento()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtPlanejamento().getTimeInMillis()));
			if(objeto.getDtEnvio()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtEnvio().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStPlanejamento());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdUsuario());
			if(objeto.getCdContaEnvio()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdContaEnvio());
			pstmt.setString(8,objeto.getNmAssunto());
			pstmt.setString(9,objeto.getTxtParametros());
			pstmt.setInt(10, cdPlanejamentoOld!=0 ? cdPlanejamentoOld : objeto.getCdPlanejamento());
			pstmt.setInt(11, cdMailingOld!=0 ? cdMailingOld : objeto.getCdMailing());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingPlanejamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingPlanejamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPlanejamento, int cdMailing) {
		return delete(cdPlanejamento, cdMailing, null);
	}

	public static int delete(int cdPlanejamento, int cdMailing, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_mailing_planejamento WHERE cd_planejamento=? AND cd_mailing=?");
			pstmt.setInt(1, cdPlanejamento);
			pstmt.setInt(2, cdMailing);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingPlanejamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingPlanejamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MailingPlanejamento get(int cdPlanejamento, int cdMailing) {
		return get(cdPlanejamento, cdMailing, null);
	}

	public static MailingPlanejamento get(int cdPlanejamento, int cdMailing, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_mailing_planejamento WHERE cd_planejamento=? AND cd_mailing=?");
			pstmt.setInt(1, cdPlanejamento);
			pstmt.setInt(2, cdMailing);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MailingPlanejamento(rs.getInt("cd_planejamento"),
						rs.getInt("cd_mailing"),
						(rs.getTimestamp("dt_planejamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_planejamento").getTime()),
						(rs.getTimestamp("dt_envio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_envio").getTime()),
						rs.getInt("st_planejamento"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_conta_envio"),
						rs.getString("nm_assunto"),
						rs.getString("txt_parametros"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingPlanejamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingPlanejamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_mailing_planejamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingPlanejamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingPlanejamentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM crm_mailing_planejamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
