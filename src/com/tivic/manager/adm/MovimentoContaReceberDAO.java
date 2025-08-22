package com.tivic.manager.adm;

import java.sql.*;

import sol.dao.ResultSetMap;
import sol.dao.Search;

import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class MovimentoContaReceberDAO{

	public static int insert(MovimentoContaReceber objeto) {
		return insert(objeto, null);
	}

	public static int insert(MovimentoContaReceber objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_movimento_conta_receber (cd_conta,"+
			                                  "cd_movimento_conta,"+
			                                  "cd_conta_receber,"+
			                                  "vl_recebido,"+
			                                  "vl_juros,"+
			                                  "vl_multa,"+
			                                  "vl_desconto,"+
			                                  "vl_tarifa_cobranca,"+
			                                  "cd_arquivo,"+
			                                  "cd_registro," +
			                                  "vl_acrescimo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdConta()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdConta());
			if(objeto.getCdMovimentoConta()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMovimentoConta());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContaReceber());
			pstmt.setDouble(4,objeto.getVlRecebido());
			pstmt.setDouble(5,objeto.getVlJuros());
			pstmt.setDouble(6,objeto.getVlMulta());
			pstmt.setDouble(7,objeto.getVlDesconto());
			pstmt.setDouble(8,objeto.getVlTarifaCobranca());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdArquivo());
			if(objeto.getCdRegistro()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdRegistro());
			pstmt.setDouble(11,objeto.getVlAcrescimo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			Util.registerLog(sqlExpt);
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaReceberDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaReceberDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MovimentoContaReceber objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(MovimentoContaReceber objeto, int cdContaOld, int cdMovimentoContaOld, int cdContaReceberOld) {
		return update(objeto, cdContaOld, cdMovimentoContaOld, cdContaReceberOld, null);
	}

	public static int update(MovimentoContaReceber objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(MovimentoContaReceber objeto, int cdContaOld, int cdMovimentoContaOld, int cdContaReceberOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_movimento_conta_receber SET cd_conta=?,"+
												      		   "cd_movimento_conta=?,"+
												      		   "cd_conta_receber=?,"+
												      		   "vl_recebido=?,"+
												      		   "vl_juros=?,"+
												      		   "vl_multa=?,"+
												      		   "vl_desconto=?,"+
												      		   "vl_tarifa_cobranca=?,"+
												      		   "cd_arquivo=?,"+
												      		   "cd_registro=?, "+ 
												      		   "vl_acrescimo=? WHERE cd_conta=? AND cd_movimento_conta=? AND cd_conta_receber=?");
			pstmt.setInt(1,objeto.getCdConta());
			pstmt.setInt(2,objeto.getCdMovimentoConta());
			pstmt.setInt(3,objeto.getCdContaReceber());
			pstmt.setDouble(4,objeto.getVlRecebido());
			pstmt.setDouble(5,objeto.getVlJuros());
			pstmt.setDouble(6,objeto.getVlMulta());
			pstmt.setDouble(7,objeto.getVlDesconto());
			pstmt.setDouble(8,objeto.getVlTarifaCobranca());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdArquivo());
			if(objeto.getCdRegistro()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdRegistro());
			pstmt.setDouble(11,objeto.getVlAcrescimo());
			pstmt.setInt(12, cdContaOld!=0 ? cdContaOld : objeto.getCdConta());
			pstmt.setInt(13, cdMovimentoContaOld!=0 ? cdMovimentoContaOld : objeto.getCdMovimentoConta());
			pstmt.setInt(14, cdContaReceberOld!=0 ? cdContaReceberOld : objeto.getCdContaReceber());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			Util.registerLog(sqlExpt);
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaReceberDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaReceberDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConta, int cdMovimentoConta, int cdContaReceber) {
		return delete(cdConta, cdMovimentoConta, cdContaReceber, null);
	}

	public static int delete(int cdConta, int cdMovimentoConta, int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_movimento_conta_receber WHERE cd_conta=? AND cd_movimento_conta=? AND cd_conta_receber=?");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdMovimentoConta);
			pstmt.setInt(3, cdContaReceber);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			Util.registerLog(sqlExpt);
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaReceberDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaReceberDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MovimentoContaReceber get(int cdConta, int cdMovimentoConta, int cdContaReceber) {
		return get(cdConta, cdMovimentoConta, cdContaReceber, null);
	}

	public static MovimentoContaReceber get(int cdConta, int cdMovimentoConta, int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_movimento_conta_receber WHERE cd_conta=? AND cd_movimento_conta=? AND cd_conta_receber=?");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdMovimentoConta);
			pstmt.setInt(3, cdContaReceber);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MovimentoContaReceber(rs.getInt("cd_conta"),
						rs.getInt("cd_movimento_conta"),
						rs.getInt("cd_conta_receber"),
						rs.getFloat("vl_recebido"),
						rs.getFloat("vl_juros"),
						rs.getFloat("vl_multa"),
						rs.getFloat("vl_acrescimo"),
						rs.getFloat("vl_desconto"),
						rs.getFloat("vl_tarifa_cobranca"),
						rs.getInt("cd_arquivo"),
						rs.getInt("cd_registro"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			Util.registerLog(sqlExpt);
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaReceberDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaReceberDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_movimento_conta_receber");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			Util.registerLog(sqlExpt);
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaReceberDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaReceberDAO.getAll: " + e);
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
		try{
			return Search.find("SELECT * FROM adm_movimento_conta_receber", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaReceberDAO.find: " + e);
			return null;
		}
	}

}
