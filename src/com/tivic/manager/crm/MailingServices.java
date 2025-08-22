package com.tivic.manager.crm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

@DestinationConfig(enabled = false)
public class MailingServices {

	public static int save(Mailing mailing){
		return save(mailing, null);
	}
	public static int save(Mailing mailing, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno;
			if(mailing.getCdMailing()==0){
				retorno = MailingDAO.insert(mailing, connect);
			}
			else{
				retorno = MailingDAO.update(mailing, connect);
				retorno = retorno>0?mailing.getCdMailing():retorno;
			}

			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingServices.save: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
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
			if(cdMailing<=0)
				return -1;

			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			//envios
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_mailing_envio WHERE cd_mailing=?");
			pstmt.setInt(1, cdMailing);
			pstmt.executeUpdate();

			//planejamento
			pstmt = connect.prepareStatement("DELETE FROM crm_mailing_planejamento WHERE cd_mailing=?");
			pstmt.setInt(1, cdMailing);
			pstmt.executeUpdate();

			//destino
			pstmt = connect.prepareStatement("DELETE FROM crm_mailing_destino WHERE cd_mailing=?");
			pstmt.setInt(1, cdMailing);
			pstmt.executeUpdate();

			if(MailingDAO.delete(cdMailing, connect)<=0){
				Conexao.rollback(connect);
				return -2;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingServices.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -3;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getMailings() {
		return getMailings(null);
	}

	public static ResultSetMap getMailings(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
				      "FROM crm_mailing_grupo ORDER BY nm_grupo");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			while(rsm.next()){
				pstmt = connect.prepareStatement("SELECT *  " +
					      "FROM crm_mailing A " +
					      "JOIN crm_mailing_grupo B ON (A.cd_grupo = B.cd_grupo) " +
					      "WHERE B.cd_grupo = ? ");
				pstmt.setInt(1, rsm.getInt("CD_GRUPO"));
				ResultSetMap subRsm = new ResultSetMap(pstmt.executeQuery());
				rsm.setValueToField("subResultSetMap", subRsm);
			}

			pstmt = connect.prepareStatement("SELECT *  " +
				      "FROM crm_mailing " +
				      "WHERE cd_grupo is null ");

			ResultSetMap subRsm = new ResultSetMap(pstmt.executeQuery());

			if(subRsm.hasMore()){
				HashMap<String,Object> register = new HashMap<String,Object>();
				register.put("CD_GRUPO", "0");
				register.put("NM_GRUPO", "SEM GRUPO");
				register.put("subResultSetMap", subRsm);
				rsm.addRegister(register);
			}
			ArrayList<String> order = new ArrayList<String>();
			order.add("NM_GRUPO");
			rsm.orderBy(order);
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingServices.getMailings: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findDestinatarios(String nmDestinatario) {
		return findDestinatarios(nmDestinatario, 100, null);
	}

	public static ResultSetMap findDestinatarios(String nmDestinatario, int nrRegistros) {
		return findDestinatarios(nmDestinatario, nrRegistros, null);
	}

	public static ResultSetMap findDestinatarios(String nmDestinatario, int nrRegistros, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_pessoa, nm_pessoa, nm_email, '' as cd_grupo, '' as nm_grupo  " +
															   "  FROM GRL_PESSOA " +
															   " WHERE gn_pessoa = 1 " +
															   "   AND nm_pessoa ilike '"+nmDestinatario+"%' " +
															   " ORDER BY nm_pessoa");

			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			pstmt = connect.prepareStatement("SELECT '' as cd_pessoa, '' as nm_pessoa, '' as nm_email, cd_grupo, nm_grupo  " +
					   "  FROM AGD_GRUPO " +
					   " WHERE nm_grupo ilike '"+nmDestinatario+"%' " +
					   " ORDER BY nm_grupo");

			ResultSetMap rsm2 = new ResultSetMap(pstmt.executeQuery());
			while(rsm2.next())
				rsm.addRegister(rsm2.getRegister());

			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingServices.findDestinatarios: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int addDestinatarios(ArrayList<MailingDestino> destinatarios){
		return addDestinatarios(destinatarios, null);
	}
	public static int addDestinatarios(ArrayList<MailingDestino> destinatarios, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno = 0;

			for(int i=0; i<destinatarios.size(); i++){
				MailingDestino destinatario = (MailingDestino) destinatarios.get(i);
				retorno = MailingDestinoDAO.insert(destinatario, connect);
				if(retorno<0)
					break;
			}
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingServices.addDestinatarios: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingServices.addDestinatarios: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int deleteDestinatario(int cdMailing, int cdDestino) {
		return deleteDestinatario(cdMailing, cdDestino, null);
	}

	public static int deleteDestinatario(int cdMailing, int cdDestino, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(cdMailing<=0)
				return -1;
			if(cdDestino<=0)
				return -2;

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM crm_mailing_envio WHERE cd_mailing=? AND cd_destino=? AND st_envio <> 0");
			pstmt.setInt(1, cdMailing);
			pstmt.setInt(2, cdDestino);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				return -4;
			}
			if(MailingDestinoDAO.delete(cdDestino, cdMailing, connect)<=0){
				Conexao.rollback(connect);
				return -5;
			}

			if(isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingServices.deleteDestinatario: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingServices.deleteDestinatario: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -6;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getDestinatarios(int cdMailing) {
		return getDestinatarios(cdMailing, null);
	}

	public static ResultSetMap getDestinatarios(int cdMailing, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, C.cd_fonte, C.nm_fonte, D.nm_pessoa, D.nm_email, E.nm_grupo, " +
					  "(SELECT COUNT(B.cd_destino) FROM crm_mailing_envio B WHERE B.cd_mailing = A.cd_mailing AND B.cd_destino = A.cd_destino) as qt_total_envios "+
					  "FROM crm_mailing_destino A " +
					  "LEFT OUTER JOIN grl_fonte_dados C ON (A.cd_fonte = C.cd_fonte) "+
					  "LEFT OUTER JOIN grl_pessoa D ON (A.cd_pessoa = D.cd_pessoa) "+
					  "LEFT OUTER JOIN agd_grupo E ON (A.cd_grupo = E.cd_grupo) "+
				      "WHERE A.cd_mailing = ? " +
				      "ORDER BY C.nm_fonte, E.nm_grupo, D.nm_pessoa" );
			pstmt.setInt(1, cdMailing);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingServices.getDestinatarios: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int deletePlanejamento(int cdMailing, int cdPlanejamento) {
		return deletePlanejamento(cdMailing, cdPlanejamento, null);
	}

	public static int deletePlanejamento(int cdMailing, int cdPlanejamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(cdMailing<=0)
				return -1;
			if(cdPlanejamento<=0)
				return -2;

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM crm_mailing_envio WHERE cd_mailing=? AND cd_planejamento=? AND st_envio <> 0");
			pstmt.setInt(1, cdMailing);
			pstmt.setInt(2, cdPlanejamento);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				return -4;
			}
			if(MailingPlanejamentoDAO.delete(cdPlanejamento, cdMailing, connect)<=0){
				Conexao.rollback(connect);
				return -5;
			}

			if(isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingServices.deletePlanejamento: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingServices.deletePlanejamento: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -6;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getPlanejamentos(int cdMailing) {
		return getPlanejamentos(cdMailing, null);
	}

	public static ResultSetMap getPlanejamentos(int cdMailing, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.*, C.nm_login, D.nm_pessoa " +
					  "FROM crm_mailing_planejamento A " +
					  "LEFT OUTER JOIN crm_mailing_conta B ON (B.cd_conta = A.cd_conta_envio) "+
					  "LEFT OUTER JOIN seg_usuario C ON (C.cd_usuario = A.cd_usuario) "+
					  "LEFT OUTER JOIN grl_pessoa D ON (D.cd_pessoa = C.cd_pessoa) "+
				      "WHERE cd_mailing = ? " +
				      "ORDER BY st_planejamento, dt_planejamento ASC, dt_envio ASC" );
			pstmt.setInt(1, cdMailing);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingServices.getPlanejamentos: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
