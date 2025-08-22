package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class FormaPagamentoDAO{

	public static int insert(FormaPagamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(FormaPagamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_forma_pagamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFormaPagamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_forma_pagamento (cd_forma_pagamento,"+
			                                  "nm_forma_pagamento,"+
			                                  "sg_forma_pagamento,"+
			                                  "id_forma_pagamento,"+
			                                  "tp_forma_pagamento,"+
			                                  "lg_transferencia) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmFormaPagamento());
			pstmt.setString(3,objeto.getSgFormaPagamento());
			pstmt.setString(4,objeto.getIdFormaPagamento());
			pstmt.setInt(5,objeto.getTpFormaPagamento());
			pstmt.setInt(6,objeto.getLgTransferencia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FormaPagamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(FormaPagamento objeto, int cdFormaPagamentoOld) {
		return update(objeto, cdFormaPagamentoOld, null);
	}

	public static int update(FormaPagamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(FormaPagamento objeto, int cdFormaPagamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_forma_pagamento SET cd_forma_pagamento=?,"+
												      		   "nm_forma_pagamento=?,"+
												      		   "sg_forma_pagamento=?,"+
												      		   "id_forma_pagamento=?,"+
												      		   "tp_forma_pagamento=?,"+
												      		   "lg_transferencia=? WHERE cd_forma_pagamento=?");
			pstmt.setInt(1,objeto.getCdFormaPagamento());
			pstmt.setString(2,objeto.getNmFormaPagamento());
			pstmt.setString(3,objeto.getSgFormaPagamento());
			pstmt.setString(4,objeto.getIdFormaPagamento());
			pstmt.setInt(5,objeto.getTpFormaPagamento());
			pstmt.setInt(6,objeto.getLgTransferencia());
			pstmt.setInt(7, cdFormaPagamentoOld!=0 ? cdFormaPagamentoOld : objeto.getCdFormaPagamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFormaPagamento) {
		return delete(cdFormaPagamento, null);
	}

	public static int delete(int cdFormaPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_forma_pagamento WHERE cd_forma_pagamento=?");
			pstmt.setInt(1, cdFormaPagamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FormaPagamento get(int cdFormaPagamento) {
		return get(cdFormaPagamento, null);
	}

	public static FormaPagamento get(int cdFormaPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_forma_pagamento WHERE cd_forma_pagamento=?");
			pstmt.setInt(1, cdFormaPagamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FormaPagamento(rs.getInt("cd_forma_pagamento"),
						rs.getString("nm_forma_pagamento"),
						rs.getString("sg_forma_pagamento"),
						rs.getString("id_forma_pagamento"),
						rs.getInt("tp_forma_pagamento"),
						rs.getInt("lg_transferencia"));
			}
			else
				return null;
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_forma_pagamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_forma_pagamento", "ORDER BY nm_forma_pagamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
