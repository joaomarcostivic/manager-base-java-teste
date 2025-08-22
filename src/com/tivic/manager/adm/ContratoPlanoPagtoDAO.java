package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContratoPlanoPagtoDAO{

	public static int insert(ContratoPlanoPagto objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(ContratoPlanoPagto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_contrato");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdContrato()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_pagamento");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("adm_contrato_plano_pagto", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPagamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_contrato_plano_pagto (cd_contrato,"+
			                                  "cd_plano_pagamento,"+
			                                  "cd_forma_pagamento,"+
			                                  "cd_pagamento,"+
			                                  "vl_pagamento,"+
			                                  "cd_usuario,"+
			                                  "cd_movimento_conta,"+
			                                  "cd_conta) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdContrato()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContrato());
			if(objeto.getCdPlanoPagamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPlanoPagamento());
			if(objeto.getCdFormaPagamento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdFormaPagamento());
			pstmt.setInt(4, code);
			pstmt.setFloat(5,objeto.getVlPagamento());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdUsuario());
			pstmt.setFloat(7,objeto.getCdMovimentoConta());
			if(objeto.getCdConta()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdConta());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoPlanoPagtoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoPlanoPagtoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContratoPlanoPagto objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ContratoPlanoPagto objeto, int cdContratoOld, int cdPagamentoOld) {
		return update(objeto, cdContratoOld, cdPagamentoOld, null);
	}

	public static int update(ContratoPlanoPagto objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ContratoPlanoPagto objeto, int cdContratoOld, int cdPagamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_contrato_plano_pagto SET cd_contrato=?,"+
												      		   "cd_plano_pagamento=?,"+
												      		   "cd_forma_pagamento=?,"+
												      		   "cd_pagamento=?,"+
												      		   "vl_pagamento=?,"+
												      		   "cd_usuario=?,"+
												      		   "cd_movimento_conta=?,"+
												      		   "cd_conta=? WHERE cd_contrato=? AND cd_pagamento=?");
			pstmt.setInt(1,objeto.getCdContrato());
			if(objeto.getCdPlanoPagamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPlanoPagamento());
			if(objeto.getCdFormaPagamento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdFormaPagamento());
			pstmt.setInt(4,objeto.getCdPagamento());
			pstmt.setFloat(5,objeto.getVlPagamento());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdUsuario());
			pstmt.setFloat(7,objeto.getCdMovimentoConta());
			if(objeto.getCdConta()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdConta());
			pstmt.setInt(9, cdContratoOld!=0 ? cdContratoOld : objeto.getCdContrato());
			pstmt.setInt(10, cdPagamentoOld!=0 ? cdPagamentoOld : objeto.getCdPagamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoPlanoPagtoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoPlanoPagtoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContrato, int cdPagamento) {
		return delete(cdContrato, cdPagamento, null);
	}

	public static int delete(int cdContrato, int cdPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_contrato_plano_pagto WHERE cd_contrato=? AND cd_pagamento=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdPagamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoPlanoPagtoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoPlanoPagtoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContratoPlanoPagto get(int cdContrato, int cdPagamento) {
		return get(cdContrato, cdPagamento, null);
	}

	public static ContratoPlanoPagto get(int cdContrato, int cdPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_plano_pagto WHERE cd_contrato=? AND cd_pagamento=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdPagamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContratoPlanoPagto(rs.getInt("cd_contrato"),
						rs.getInt("cd_plano_pagamento"),
						rs.getInt("cd_forma_pagamento"),
						rs.getInt("cd_pagamento"),
						rs.getFloat("vl_pagamento"),
						rs.getInt("cd_usuario"),
						rs.getFloat("cd_movimento_conta"),
						rs.getInt("cd_conta"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoPlanoPagtoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoPlanoPagtoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_plano_pagto");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoPlanoPagtoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoPlanoPagtoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_contrato_plano_pagto", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
