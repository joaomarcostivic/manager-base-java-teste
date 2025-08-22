package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class MailingEnvioDAO{

	public static int insert(MailingEnvio objeto) {
		return insert(objeto, null);
	}

	public static int insert(MailingEnvio objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_mailing_envio (cd_destino,"+
			                                  "cd_mailing,"+
			                                  "cd_planejamento,"+
			                                  "dt_envio,"+
			                                  "st_envio,"+
			                                  "txt_conteudo,"+
			                                  "cd_usuario) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdDestino()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdDestino());
			if(objeto.getCdMailing()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMailing());
			if(objeto.getCdPlanejamento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPlanejamento());
			if(objeto.getDtEnvio()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtEnvio().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStEnvio());
			pstmt.setString(6,objeto.getTxtConteudo());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdUsuario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingEnvioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingEnvioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MailingEnvio objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(MailingEnvio objeto, int cdDestinoOld, int cdMailingOld, int cdPlanejamentoOld) {
		return update(objeto, cdDestinoOld, cdMailingOld, cdPlanejamentoOld, null);
	}

	public static int update(MailingEnvio objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(MailingEnvio objeto, int cdDestinoOld, int cdMailingOld, int cdPlanejamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_mailing_envio SET cd_destino=?,"+
												      		   "cd_mailing=?,"+
												      		   "cd_planejamento=?,"+
												      		   "dt_envio=?,"+
												      		   "st_envio=?,"+
												      		   "txt_conteudo=?,"+
												      		   "cd_usuario=? WHERE cd_destino=? AND cd_mailing=? AND cd_planejamento=?");
			pstmt.setInt(1,objeto.getCdDestino());
			pstmt.setInt(2,objeto.getCdMailing());
			pstmt.setInt(3,objeto.getCdPlanejamento());
			if(objeto.getDtEnvio()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtEnvio().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStEnvio());
			pstmt.setString(6,objeto.getTxtConteudo());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdUsuario());
			pstmt.setInt(8, cdDestinoOld!=0 ? cdDestinoOld : objeto.getCdDestino());
			pstmt.setInt(9, cdMailingOld!=0 ? cdMailingOld : objeto.getCdMailing());
			pstmt.setInt(10, cdPlanejamentoOld!=0 ? cdPlanejamentoOld : objeto.getCdPlanejamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingEnvioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingEnvioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDestino, int cdMailing, int cdPlanejamento) {
		return delete(cdDestino, cdMailing, cdPlanejamento, null);
	}

	public static int delete(int cdDestino, int cdMailing, int cdPlanejamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_mailing_envio WHERE cd_destino=? AND cd_mailing=? AND cd_planejamento=?");
			pstmt.setInt(1, cdDestino);
			pstmt.setInt(2, cdMailing);
			pstmt.setInt(3, cdPlanejamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingEnvioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingEnvioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MailingEnvio get(int cdDestino, int cdMailing, int cdPlanejamento) {
		return get(cdDestino, cdMailing, cdPlanejamento, null);
	}

	public static MailingEnvio get(int cdDestino, int cdMailing, int cdPlanejamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_mailing_envio WHERE cd_destino=? AND cd_mailing=? AND cd_planejamento=?");
			pstmt.setInt(1, cdDestino);
			pstmt.setInt(2, cdMailing);
			pstmt.setInt(3, cdPlanejamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MailingEnvio(rs.getInt("cd_destino"),
						rs.getInt("cd_mailing"),
						rs.getInt("cd_planejamento"),
						(rs.getTimestamp("dt_envio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_envio").getTime()),
						rs.getInt("st_envio"),
						rs.getString("txt_conteudo"),
						rs.getInt("cd_usuario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingEnvioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingEnvioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_mailing_envio");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingEnvioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingEnvioDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM crm_mailing_envio", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
