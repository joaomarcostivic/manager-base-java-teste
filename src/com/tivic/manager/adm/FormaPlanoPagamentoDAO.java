package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class FormaPlanoPagamentoDAO{

	public static int insert(FormaPlanoPagamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(FormaPlanoPagamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_forma_plano_pagamento (cd_forma_pagamento,"+
			                                  "cd_empresa,"+
			                                  "cd_plano_pagamento,"+
			                                  "pr_taxa_desconto,"+
			                                  "pr_desconto_maximo,"+
			                                  "vl_minimo) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdFormaPagamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdFormaPagamento());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdPlanoPagamento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPlanoPagamento());
			pstmt.setDouble(4,objeto.getPrTaxaDesconto());
			pstmt.setDouble(5,objeto.getPrDescontoMaximo());
			pstmt.setDouble(6,objeto.getVlMinimo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormaPlanoPagamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPlanoPagamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FormaPlanoPagamento objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(FormaPlanoPagamento objeto, int cdFormaPagamentoOld, int cdEmpresaOld, int cdPlanoPagamentoOld) {
		return update(objeto, cdFormaPagamentoOld, cdEmpresaOld, cdPlanoPagamentoOld, null);
	}

	public static int update(FormaPlanoPagamento objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(FormaPlanoPagamento objeto, int cdFormaPagamentoOld, int cdEmpresaOld, int cdPlanoPagamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_forma_plano_pagamento SET cd_forma_pagamento=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_plano_pagamento=?,"+
												      		   "pr_taxa_desconto=?,"+
												      		   "pr_desconto_maximo=?,"+
												      		   "vl_minimo=? WHERE cd_forma_pagamento=? AND cd_empresa=? AND cd_plano_pagamento=?");
			pstmt.setInt(1,objeto.getCdFormaPagamento());
			pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setInt(3,objeto.getCdPlanoPagamento());
			pstmt.setDouble(4,objeto.getPrTaxaDesconto());
			pstmt.setDouble(5,objeto.getPrDescontoMaximo());
			pstmt.setDouble(6,objeto.getVlMinimo());
			pstmt.setInt(7, cdFormaPagamentoOld!=0 ? cdFormaPagamentoOld : objeto.getCdFormaPagamento());
			pstmt.setInt(8, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(9, cdPlanoPagamentoOld!=0 ? cdPlanoPagamentoOld : objeto.getCdPlanoPagamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormaPlanoPagamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPlanoPagamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFormaPagamento, int cdEmpresa, int cdPlanoPagamento) {
		return delete(cdFormaPagamento, cdEmpresa, cdPlanoPagamento, null);
	}

	public static int delete(int cdFormaPagamento, int cdEmpresa, int cdPlanoPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_forma_plano_pagamento WHERE cd_forma_pagamento=? AND cd_empresa=? AND cd_plano_pagamento=?");
			pstmt.setInt(1, cdFormaPagamento);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdPlanoPagamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormaPlanoPagamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPlanoPagamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FormaPlanoPagamento get(int cdFormaPagamento, int cdEmpresa, int cdPlanoPagamento) {
		return get(cdFormaPagamento, cdEmpresa, cdPlanoPagamento, null);
	}

	public static FormaPlanoPagamento get(int cdFormaPagamento, int cdEmpresa, int cdPlanoPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_forma_plano_pagamento WHERE cd_forma_pagamento=? AND cd_empresa=? AND cd_plano_pagamento=?");
			pstmt.setInt(1, cdFormaPagamento);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdPlanoPagamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FormaPlanoPagamento(rs.getInt("cd_forma_pagamento"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_plano_pagamento"),
						rs.getDouble("pr_taxa_desconto"),
						rs.getDouble("pr_desconto_maximo"),
						rs.getDouble("vl_minimo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormaPlanoPagamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPlanoPagamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_forma_plano_pagamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormaPlanoPagamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPlanoPagamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<FormaPlanoPagamento> getList() {
		return getList(null);
	}

	public static ArrayList<FormaPlanoPagamento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<FormaPlanoPagamento> list = new ArrayList<FormaPlanoPagamento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				FormaPlanoPagamento obj = FormaPlanoPagamentoDAO.get(rsm.getInt("cd_forma_pagamento"), rsm.getInt("cd_empresa"), rsm.getInt("cd_plano_pagamento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPlanoPagamentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM adm_forma_plano_pagamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}