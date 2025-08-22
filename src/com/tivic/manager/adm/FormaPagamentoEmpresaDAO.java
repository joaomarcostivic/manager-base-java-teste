package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class FormaPagamentoEmpresaDAO{

	public static int insert(FormaPagamentoEmpresa objeto) {
		return insert(objeto, null);
	}

	public static int insert(FormaPagamentoEmpresa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_forma_pagamento_empresa (cd_forma_pagamento,"+
			                                  "cd_empresa,"+
			                                  "cd_administrador,"+
			                                  "cd_tipo_documento,"+
			                                  "qt_dias_credito,"+
			                                  "vl_tarifa_transacao,"+
			                                  "pr_taxa_desconto,"+
			                                  "pr_desconto_rave,"+
			                                  "cd_conta_carteira,"+
			                                  "cd_conta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdFormaPagamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdFormaPagamento());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdAdministrador()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAdministrador());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTipoDocumento());
			pstmt.setInt(5,objeto.getQtDiasCredito());
			pstmt.setDouble(6,objeto.getVlTarifaTransacao());
			pstmt.setDouble(7,objeto.getPrTaxaDesconto());
			pstmt.setDouble(8,objeto.getPrDescontoRave());
			if(objeto.getCdContaCarteira()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdContaCarteira());
			if(objeto.getCdConta()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdConta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoEmpresaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoEmpresaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FormaPagamentoEmpresa objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(FormaPagamentoEmpresa objeto, int cdFormaPagamentoOld, int cdEmpresaOld) {
		return update(objeto, cdFormaPagamentoOld, cdEmpresaOld, null);
	}

	public static int update(FormaPagamentoEmpresa objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(FormaPagamentoEmpresa objeto, int cdFormaPagamentoOld, int cdEmpresaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_forma_pagamento_empresa SET cd_forma_pagamento=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_administrador=?,"+
												      		   "cd_tipo_documento=?,"+
												      		   "qt_dias_credito=?,"+
												      		   "vl_tarifa_transacao=?,"+
												      		   "pr_taxa_desconto=?,"+
												      		   "pr_desconto_rave=?,"+
												      		   "cd_conta_carteira=?,"+
												      		   "cd_conta=? WHERE cd_forma_pagamento=? AND cd_empresa=?");
			pstmt.setInt(1,objeto.getCdFormaPagamento());
			pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdAdministrador()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAdministrador());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTipoDocumento());
			pstmt.setInt(5,objeto.getQtDiasCredito());
			pstmt.setDouble(6,objeto.getVlTarifaTransacao());
			pstmt.setDouble(7,objeto.getPrTaxaDesconto());
			pstmt.setDouble(8,objeto.getPrDescontoRave());
			if(objeto.getCdContaCarteira()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdContaCarteira());
			if(objeto.getCdConta()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdConta());
			pstmt.setInt(11, cdFormaPagamentoOld!=0 ? cdFormaPagamentoOld : objeto.getCdFormaPagamento());
			pstmt.setInt(12, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoEmpresaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoEmpresaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFormaPagamento, int cdEmpresa) {
		return delete(cdFormaPagamento, cdEmpresa, null);
	}

	public static int delete(int cdFormaPagamento, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_forma_pagamento_empresa WHERE cd_forma_pagamento=? AND cd_empresa=?");
			pstmt.setInt(1, cdFormaPagamento);
			pstmt.setInt(2, cdEmpresa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoEmpresaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoEmpresaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FormaPagamentoEmpresa get(int cdFormaPagamento, int cdEmpresa) {
		return get(cdFormaPagamento, cdEmpresa, null);
	}

	public static FormaPagamentoEmpresa get(int cdFormaPagamento, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_forma_pagamento_empresa " +
					                         "WHERE cd_forma_pagamento=? AND cd_empresa=?");
			pstmt.setInt(1, cdFormaPagamento);
			pstmt.setInt(2, cdEmpresa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FormaPagamentoEmpresa(rs.getInt("cd_forma_pagamento"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_administrador"),
						rs.getInt("cd_tipo_documento"),
						rs.getInt("qt_dias_credito"),
						rs.getDouble("vl_tarifa_transacao"),
						rs.getDouble("pr_taxa_desconto"),
						rs.getDouble("pr_desconto_rave"),
						rs.getInt("cd_conta_carteira"),
						rs.getInt("cd_conta"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoEmpresaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoEmpresaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_forma_pagamento_empresa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoEmpresaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoEmpresaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_forma_pagamento_empresa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
