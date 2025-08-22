package com.tivic.manager.adm;

import java.sql.*;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;

import java.util.ArrayList;

public class CondicaoFormaPlanoPagamentoDAO {
	public static int insert(CondicaoFormaPlanoPagamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(CondicaoFormaPlanoPagamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_condicao_forma_plano_pagamento (cd_condicao_pagamento,"+
			                                  "cd_plano_pagamento,"+
			                                  "cd_forma_pagamento,"+
			                                  "cd_empresa) VALUES (?, ?, ?, ?)");
			
			if(objeto.getCdCondicaoPagamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdCondicaoPagamento());
			if(objeto.getCdPlanoPagamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2, objeto.getCdPlanoPagamento());
			if(objeto.getCdFormaPagamento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdFormaPagamento());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEmpresa());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CondicaoFormaPlanoPagamento objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(CondicaoFormaPlanoPagamento objeto, int cdCondicaoPagamentoOld, int cdPlanoPagamentoOld, int cdFormaPagamentoOld, int cdEmpresaOld) {
		return update(objeto, cdCondicaoPagamentoOld, cdPlanoPagamentoOld, cdFormaPagamentoOld, cdEmpresaOld, null);
	}

	public static int update(CondicaoFormaPlanoPagamento objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(CondicaoFormaPlanoPagamento objeto, int cdCondicaoPagamentoOld, int cdPlanoPagamentoOld, int cdFormaPagamentoOld, int cdEmpresaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_condicao_forma_plano_pagamento SET cd_condicao_pagamento=?,"+
												      		   "cd_plano_pagamento=?,"+
												      		   "cd_forma_pagamento=?,"+
												      		   "cd_empresa=? WHERE cd_condicao_pagamento=?, cd_plano_pagamento=?, cd_forma_pagamento=?, cd_empresa=?");
			if(objeto.getCdCondicaoPagamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdCondicaoPagamento());
			if(objeto.getCdPlanoPagamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2, objeto.getCdPlanoPagamento());
			if(objeto.getCdFormaPagamento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdFormaPagamento());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEmpresa());
			pstmt.setInt(5, cdCondicaoPagamentoOld!=0 ? cdCondicaoPagamentoOld : objeto.getCdCondicaoPagamento());
			pstmt.setInt(6, cdPlanoPagamentoOld!=0 ? cdPlanoPagamentoOld : objeto.getCdPlanoPagamento());
			pstmt.setInt(7, cdFormaPagamentoOld!=0 ? cdFormaPagamentoOld : objeto.getCdFormaPagamento());
			pstmt.setInt(8, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCondicaoPagamento, int cdPlanoPagamento, int cdFormaPagamento, int cdEmpresa) {
		return delete(cdCondicaoPagamento, cdPlanoPagamento, cdFormaPagamento, cdEmpresa, null);
	}

	public static int delete(int cdCondicaoPagamento, int cdPlanoPagamento, int cdFormaPagamento, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_condicao_forma_plano_pagamento WHERE cd_condicao_pagamento=? AND cd_plano_pagamento=? AND cd_forma_pagamento=? AND cd_empresa=?");
			pstmt.setInt(1, cdCondicaoPagamento);
			pstmt.setInt(2, cdPlanoPagamento);
			pstmt.setInt(3, cdFormaPagamento);
			pstmt.setInt(4, cdEmpresa);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CondicaoFormaPlanoPagamento get(int cdCondicaoPagamento, int cdPlanoPagamento, int cdFormaPagamento, int cdEmpresa) {
		return get(cdCondicaoPagamento, cdPlanoPagamento, cdFormaPagamento, cdEmpresa, null);
	}

	public static CondicaoFormaPlanoPagamento get(int cdCondicaoPagamento, int cdPlanoPagamento, int cdFormaPagamento, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_condicao_forma_plano_pagamento WHERE cd_condicao_pagamento=? AND cd_plano_pagamento=? AND cd_forma_pagamento=? AND cd_empresa=?");
			pstmt.setInt(1, cdCondicaoPagamento);
			pstmt.setInt(2, cdPlanoPagamento);
			pstmt.setInt(3, cdFormaPagamento);
			pstmt.setInt(4, cdEmpresa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CondicaoFormaPlanoPagamento(rs.getInt("cd_condicao_pagamento"),
						rs.getInt("cd_plano_pagamento"),
						rs.getInt("cd_forma_pagamento"),
						rs.getInt("cd_empresa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CondicaoFormaPlanoPagamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoFormaPlanoPagamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_condicao_forma_plano_pagamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CondicaoFormaPlanoPagamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoFormaPlanoPagamentoDAO.getAll: " + e);
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
		try{
			return Search.find("SELECT * FROM adm_condicao_forma_plano_pagamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
		}catch(Exception e){ Util.registerLog(e); return null;}
	}
}
