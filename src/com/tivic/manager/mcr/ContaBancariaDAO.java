package com.tivic.manager.mcr;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class ContaBancariaDAO{

	public static int insert(ContaBancaria objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(ContaBancaria objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "nr_agencia");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "nm_banco");
			keys[1].put("IS_KEY_NATIVE", "YES");
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_pessoa");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdPessoa()));
			int code = Conexao.getSequenceCode("mcr_conta_bancaria", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO mcr_conta_bancaria (nr_agencia,"+
			                                  "nm_banco,"+
			                                  "cd_pessoa,"+
			                                  "vl_limite_cheque,"+
			                                  "txt_objetivo_poupanca,"+
			                                  "vl_saldo_medio) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2, code);
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPessoa());
			pstmt.setFloat(4,objeto.getVlLimiteCheque());
			pstmt.setString(5,objeto.getTxtObjetivoPoupanca());
			pstmt.setFloat(6,objeto.getVlSaldoMedio());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaBancariaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaBancariaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaBancaria objeto) {
		return update(objeto, null);
	}

	public static int update(ContaBancaria objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE mcr_conta_bancaria SET vl_limite_cheque=?,"+
			                                  "txt_objetivo_poupanca=?,"+
			                                  "vl_saldo_medio=? WHERE nr_agencia=? AND nm_banco=? AND cd_pessoa=?");
			pstmt.setFloat(1,objeto.getVlLimiteCheque());
			pstmt.setString(2,objeto.getTxtObjetivoPoupanca());
			pstmt.setFloat(3,objeto.getVlSaldoMedio());
			pstmt.setString(4,objeto.getNrAgencia());
			pstmt.setString(5,objeto.getNmBanco());
			pstmt.setInt(6,objeto.getCdPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaBancariaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaBancariaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(String nrAgencia, String nmBanco, int cdPessoa) {
		return delete(nrAgencia, nmBanco, cdPessoa, null);
	}

	public static int delete(String nrAgencia, String nmBanco, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM mcr_conta_bancaria WHERE nr_agencia=? AND nm_banco=? AND cd_pessoa=?");
			pstmt.setString(1, nrAgencia);
			pstmt.setString(2, nmBanco);
			pstmt.setInt(3, cdPessoa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaBancariaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaBancariaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaBancaria get(String nrAgencia, String nmBanco, int cdPessoa) {
		return get(nrAgencia, nmBanco, cdPessoa, null);
	}

	public static ContaBancaria get(String nrAgencia, String nmBanco, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_conta_bancaria WHERE nr_agencia=? AND nm_banco=? AND cd_pessoa=?");
			pstmt.setString(1, nrAgencia);
			pstmt.setString(2, nmBanco);
			pstmt.setInt(3, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaBancaria(rs.getString("nr_agencia"),
						rs.getString("nm_banco"),
						rs.getInt("cd_pessoa"),
						rs.getFloat("vl_limite_cheque"),
						rs.getString("txt_objetivo_poupanca"),
						rs.getFloat("vl_saldo_medio"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaBancariaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaBancariaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_conta_bancaria");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaBancariaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaBancariaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM mcr_conta_bancaria", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
