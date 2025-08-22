package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContaReceberEventoDAO{

	public static int insert(ContaReceberEvento objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(ContaReceberEvento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_conta_receber");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdContaReceber()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_evento_financeiro");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdEventoFinanceiro()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_conta_receber_evento");
			keys[2].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("adm_conta_receber_evento", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdContaReceberEvento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_conta_receber_evento (cd_conta_receber,"+
			                                  "cd_evento_financeiro,"+
			                                  "vl_evento_financeiro,"+
			                                  "cd_conta_receber_evento,"+
			                                  "cd_pessoa,"+
			                                  "cd_contrato,"+
			                                  "st_evento) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContaReceber());
			if(objeto.getCdEventoFinanceiro()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEventoFinanceiro());
			pstmt.setFloat(3,objeto.getVlEventoFinanceiro());
			pstmt.setInt(4, code);
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdPessoa());
			if(objeto.getCdContrato()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdContrato());
			pstmt.setInt(7,objeto.getStEvento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberEventoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberEventoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaReceberEvento objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(ContaReceberEvento objeto, int cdContaReceberOld, int cdEventoFinanceiroOld, int cdContaReceberEventoOld) {
		return update(objeto, cdContaReceberOld, cdEventoFinanceiroOld, cdContaReceberEventoOld, null);
	}

	public static int update(ContaReceberEvento objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(ContaReceberEvento objeto, int cdContaReceberOld, int cdEventoFinanceiroOld, int cdContaReceberEventoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_receber_evento SET cd_conta_receber=?,"+
												      		   "cd_evento_financeiro=?,"+
												      		   "vl_evento_financeiro=?,"+
												      		   "cd_conta_receber_evento=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_contrato=?,"+
												      		   "st_evento=? WHERE cd_conta_receber=? AND cd_evento_financeiro=? AND cd_conta_receber_evento=?");
			pstmt.setInt(1,objeto.getCdContaReceber());
			pstmt.setInt(2,objeto.getCdEventoFinanceiro());
			pstmt.setFloat(3,objeto.getVlEventoFinanceiro());
			pstmt.setInt(4,objeto.getCdContaReceberEvento());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdPessoa());
			if(objeto.getCdContrato()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdContrato());
			pstmt.setInt(7,objeto.getStEvento());
			pstmt.setInt(8, cdContaReceberOld!=0 ? cdContaReceberOld : objeto.getCdContaReceber());
			pstmt.setInt(9, cdEventoFinanceiroOld!=0 ? cdEventoFinanceiroOld : objeto.getCdEventoFinanceiro());
			pstmt.setFloat(10, cdContaReceberEventoOld!=0 ? cdContaReceberEventoOld : objeto.getCdContaReceberEvento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberEventoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberEventoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContaReceber, int cdEventoFinanceiro, int cdContaReceberEvento) {
		return delete(cdContaReceber, cdEventoFinanceiro, cdContaReceberEvento, null);
	}

	public static int delete(int cdContaReceber, int cdEventoFinanceiro, int cdContaReceberEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_receber_evento WHERE cd_conta_receber=? AND cd_evento_financeiro=? AND cd_conta_receber_evento=?");
			pstmt.setInt(1, cdContaReceber);
			pstmt.setInt(2, cdEventoFinanceiro);
			pstmt.setInt(3, cdContaReceberEvento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberEventoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberEventoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaReceberEvento get(int cdContaReceber, int cdEventoFinanceiro, int cdContaReceberEvento) {
		return get(cdContaReceber, cdEventoFinanceiro, cdContaReceberEvento, null);
	}

	public static ContaReceberEvento get(int cdContaReceber, int cdEventoFinanceiro, int cdContaReceberEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_receber_evento WHERE cd_conta_receber=? AND cd_evento_financeiro=? AND cd_conta_receber_evento=?");
			pstmt.setInt(1, cdContaReceber);
			pstmt.setInt(2, cdEventoFinanceiro);
			pstmt.setInt(3, cdContaReceberEvento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaReceberEvento(rs.getInt("cd_conta_receber"),
						rs.getInt("cd_evento_financeiro"),
						rs.getFloat("vl_evento_financeiro"),
						rs.getInt("cd_conta_receber_evento"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_contrato"),
						rs.getInt("st_evento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberEventoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberEventoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_receber_evento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberEventoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberEventoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_conta_receber_evento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
