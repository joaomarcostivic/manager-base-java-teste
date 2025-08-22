package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class MailingContaDAO{

	public static int insert(MailingConta objeto) {
		return insert(objeto, null);
	}

	public static int insert(MailingConta objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("crm_mailing_conta", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdConta(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_mailing_conta (cd_conta,"+
			                                  "nm_conta,"+
			                                  "nm_email,"+
			                                  "nm_servidor_pop,"+
			                                  "nm_servidor_smtp,"+
			                                  "ds_assinatura,"+
			                                  "nr_porta_pop,"+
			                                  "nr_porta_smtp,"+
			                                  "lg_autenticacao_pop,"+
			                                  "lg_autenticacao_smtp,"+
			                                  "nm_login,"+
			                                  "nm_senha,"+
			                                  "lg_ssl_pop,"+
			                                  "lg_ssl_smtp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmConta());
			pstmt.setString(3,objeto.getNmEmail());
			pstmt.setString(4,objeto.getNmServidorPop());
			pstmt.setString(5,objeto.getNmServidorSmtp());
			pstmt.setString(6,objeto.getDsAssinatura());
			pstmt.setInt(7,objeto.getNrPortaPop());
			pstmt.setInt(8,objeto.getNrPortaSmtp());
			pstmt.setInt(9,objeto.getLgAutenticacaoPop());
			pstmt.setInt(10,objeto.getLgAutenticacaoSmtp());
			pstmt.setString(11,objeto.getNmLogin());
			pstmt.setString(12,objeto.getNmSenha());
			pstmt.setInt(13,objeto.getLgSslPop());
			pstmt.setInt(14,objeto.getLgSslSmtp());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingContaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingContaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MailingConta objeto) {
		return update(objeto, 0, null);
	}

	public static int update(MailingConta objeto, int cdContaOld) {
		return update(objeto, cdContaOld, null);
	}

	public static int update(MailingConta objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(MailingConta objeto, int cdContaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_mailing_conta SET cd_conta=?,"+
												      		   "nm_conta=?,"+
												      		   "nm_email=?,"+
												      		   "nm_servidor_pop=?,"+
												      		   "nm_servidor_smtp=?,"+
												      		   "ds_assinatura=?,"+
												      		   "nr_porta_pop=?,"+
												      		   "nr_porta_smtp=?,"+
												      		   "lg_autenticacao_pop=?,"+
												      		   "lg_autenticacao_smtp=?,"+
												      		   "nm_login=?,"+
												      		   "nm_senha=?,"+
												      		   "lg_ssl_pop=?,"+
												      		   "lg_ssl_smtp=? WHERE cd_conta=?");
			pstmt.setInt(1,objeto.getCdConta());
			pstmt.setString(2,objeto.getNmConta());
			pstmt.setString(3,objeto.getNmEmail());
			pstmt.setString(4,objeto.getNmServidorPop());
			pstmt.setString(5,objeto.getNmServidorSmtp());
			pstmt.setString(6,objeto.getDsAssinatura());
			pstmt.setInt(7,objeto.getNrPortaPop());
			pstmt.setInt(8,objeto.getNrPortaSmtp());
			pstmt.setInt(9,objeto.getLgAutenticacaoPop());
			pstmt.setInt(10,objeto.getLgAutenticacaoSmtp());
			pstmt.setString(11,objeto.getNmLogin());
			pstmt.setString(12,objeto.getNmSenha());
			pstmt.setInt(13,objeto.getLgSslPop());
			pstmt.setInt(14,objeto.getLgSslSmtp());
			pstmt.setInt(15, cdContaOld!=0 ? cdContaOld : objeto.getCdConta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingContaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingContaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConta) {
		return delete(cdConta, null);
	}

	public static int delete(int cdConta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_mailing_conta WHERE cd_conta=?");
			pstmt.setInt(1, cdConta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingContaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingContaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MailingConta get(int cdConta) {
		return get(cdConta, null);
	}

	public static MailingConta get(int cdConta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_mailing_conta WHERE cd_conta=?");
			pstmt.setInt(1, cdConta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MailingConta(rs.getInt("cd_conta"),
						rs.getString("nm_conta"),
						rs.getString("nm_email"),
						rs.getString("nm_servidor_pop"),
						rs.getString("nm_servidor_smtp"),
						rs.getString("ds_assinatura"),
						rs.getInt("nr_porta_pop"),
						rs.getInt("nr_porta_smtp"),
						rs.getInt("lg_autenticacao_pop"),
						rs.getInt("lg_autenticacao_smtp"),
						rs.getString("nm_login"),
						rs.getString("nm_senha"),
						rs.getInt("lg_ssl_pop"),
						rs.getInt("lg_ssl_smtp"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingContaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingContaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_mailing_conta");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingContaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingContaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM crm_mailing_conta", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
