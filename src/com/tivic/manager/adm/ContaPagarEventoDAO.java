package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContaPagarEventoDAO{

	public static int insert(ContaPagarEvento objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContaPagarEvento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_conta_pagar_evento (cd_conta_pagar,"+
			                                  "cd_evento_financeiro,"+
			                                  "vl_evento_financeiro) VALUES (?, ?, ?)");
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContaPagar());
			if(objeto.getCdEventoFinanceiro()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEventoFinanceiro());
			pstmt.setFloat(3,objeto.getVlEventoFinanceiro());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarEventoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarEventoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaPagarEvento objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ContaPagarEvento objeto, int cdContaPagarOld, int cdEventoFinanceiroOld) {
		return update(objeto, cdContaPagarOld, cdEventoFinanceiroOld, null);
	}

	public static int update(ContaPagarEvento objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ContaPagarEvento objeto, int cdContaPagarOld, int cdEventoFinanceiroOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_pagar_evento SET cd_conta_pagar=?,"+
												      		   "cd_evento_financeiro=?,"+
												      		   "vl_evento_financeiro=? WHERE cd_conta_pagar=? AND cd_evento_financeiro=?");
			pstmt.setInt(1,objeto.getCdContaPagar());
			pstmt.setInt(2,objeto.getCdEventoFinanceiro());
			pstmt.setFloat(3,objeto.getVlEventoFinanceiro());
			pstmt.setInt(4, cdContaPagarOld!=0 ? cdContaPagarOld : objeto.getCdContaPagar());
			pstmt.setInt(5, cdEventoFinanceiroOld!=0 ? cdEventoFinanceiroOld : objeto.getCdEventoFinanceiro());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarEventoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarEventoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContaPagar, int cdEventoFinanceiro) {
		return delete(cdContaPagar, cdEventoFinanceiro, null);
	}

	public static int delete(int cdContaPagar, int cdEventoFinanceiro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_pagar_evento WHERE cd_conta_pagar=? AND cd_evento_financeiro=?");
			pstmt.setInt(1, cdContaPagar);
			pstmt.setInt(2, cdEventoFinanceiro);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarEventoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarEventoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaPagarEvento get(int cdContaPagar, int cdEventoFinanceiro) {
		return get(cdContaPagar, cdEventoFinanceiro, null);
	}

	public static ContaPagarEvento get(int cdContaPagar, int cdEventoFinanceiro, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_pagar_evento WHERE cd_conta_pagar=? AND cd_evento_financeiro=?");
			pstmt.setInt(1, cdContaPagar);
			pstmt.setInt(2, cdEventoFinanceiro);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaPagarEvento(rs.getInt("cd_conta_pagar"),
						rs.getInt("cd_evento_financeiro"),
						rs.getFloat("vl_evento_financeiro"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarEventoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarEventoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_pagar_evento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarEventoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarEventoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_conta_pagar_evento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
