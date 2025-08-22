package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class MovimentoContaPagarDAO{

	public static int insert(MovimentoContaPagar objeto) {
		return insert(objeto, null);
	}

	public static int insert(MovimentoContaPagar objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_movimento_conta_pagar (cd_conta,"+
			                                  "cd_movimento_conta,"+
			                                  "cd_conta_pagar,"+
			                                  "vl_pago,"+
			                                  "vl_multa,"+
			                                  "vl_juros,"+
			                                  "vl_desconto) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdConta()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdConta());
			if(objeto.getCdMovimentoConta()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMovimentoConta());
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContaPagar());
			pstmt.setDouble(4,objeto.getVlPago());
			pstmt.setDouble(5,objeto.getVlMulta());
			pstmt.setDouble(6,objeto.getVlJuros());
			pstmt.setDouble(7,objeto.getVlDesconto());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaPagarDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaPagarDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MovimentoContaPagar objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(MovimentoContaPagar objeto, int cdContaOld, int cdMovimentoContaOld, int cdContaPagarOld) {
		return update(objeto, cdContaOld, cdMovimentoContaOld, cdContaPagarOld, null);
	}

	public static int update(MovimentoContaPagar objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(MovimentoContaPagar objeto, int cdContaOld, int cdMovimentoContaOld, int cdContaPagarOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_movimento_conta_pagar SET cd_conta=?,"+
												      		   "cd_movimento_conta=?,"+
												      		   "cd_conta_pagar=?,"+
												      		   "vl_pago=?,"+
												      		   "vl_multa=?,"+
												      		   "vl_juros=?,"+
												      		   "vl_desconto=? WHERE cd_conta=? AND cd_movimento_conta=? AND cd_conta_pagar=?");
			pstmt.setInt(1,objeto.getCdConta());
			pstmt.setInt(2,objeto.getCdMovimentoConta());
			pstmt.setInt(3,objeto.getCdContaPagar());
			pstmt.setDouble(4,objeto.getVlPago());
			pstmt.setDouble(5,objeto.getVlMulta());
			pstmt.setDouble(6,objeto.getVlJuros());
			pstmt.setDouble(7,objeto.getVlDesconto());
			pstmt.setInt(8, cdContaOld!=0 ? cdContaOld : objeto.getCdConta());
			pstmt.setInt(9, cdMovimentoContaOld!=0 ? cdMovimentoContaOld : objeto.getCdMovimentoConta());
			pstmt.setInt(10, cdContaPagarOld!=0 ? cdContaPagarOld : objeto.getCdContaPagar());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaPagarDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaPagarDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConta, int cdMovimentoConta, int cdContaPagar) {
		return delete(cdConta, cdMovimentoConta, cdContaPagar, null);
	}

	public static int delete(int cdConta, int cdMovimentoConta, int cdContaPagar, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_movimento_conta_pagar WHERE cd_conta=? AND cd_movimento_conta=? AND cd_conta_pagar=?");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdMovimentoConta);
			pstmt.setInt(3, cdContaPagar);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaPagarDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaPagarDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MovimentoContaPagar get(int cdConta, int cdMovimentoConta, int cdContaPagar) {
		return get(cdConta, cdMovimentoConta, cdContaPagar, null);
	}

	public static MovimentoContaPagar get(int cdConta, int cdMovimentoConta, int cdContaPagar, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_movimento_conta_pagar WHERE cd_conta=? AND cd_movimento_conta=? AND cd_conta_pagar=?");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdMovimentoConta);
			pstmt.setInt(3, cdContaPagar);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MovimentoContaPagar(rs.getInt("cd_conta"),
						rs.getInt("cd_movimento_conta"),
						rs.getInt("cd_conta_pagar"),
						rs.getFloat("vl_pago"),
						rs.getFloat("vl_multa"),
						rs.getFloat("vl_juros"),
						rs.getFloat("vl_desconto"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaPagarDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaPagarDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_movimento_conta_pagar");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaPagarDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaPagarDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM adm_movimento_conta_pagar", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
