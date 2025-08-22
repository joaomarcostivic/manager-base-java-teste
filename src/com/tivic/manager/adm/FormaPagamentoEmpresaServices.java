package com.tivic.manager.adm;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

public class FormaPagamentoEmpresaServices	{
	
	public static Result save(FormaPagamentoEmpresa formaPagamentoEmpresa, FormaPagamento formaPagamento){
		return save(formaPagamentoEmpresa, formaPagamento, null);
	}

	public static Result save(FormaPagamentoEmpresa formaPagamentoEmpresa, FormaPagamento formaPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(formaPagamentoEmpresa==null)
				return new Result(-1, "Erro ao salvar. FormaPagamentoEmpresa é nulo");

			int retorno = 0;
			
			Result r = FormaPagamentoServices.save(formaPagamento, connect);
			
			if(r.getCode()>0) {
				formaPagamentoEmpresa.setCdFormaPagamento(formaPagamento.getCdFormaPagamento());
				
				
				FormaPagamentoEmpresa f = FormaPagamentoEmpresaDAO.get(formaPagamentoEmpresa.getCdFormaPagamento(), formaPagamentoEmpresa.getCdEmpresa(), connect);
				
				if(f==null){
					retorno = FormaPagamentoEmpresaDAO.insert(formaPagamentoEmpresa, connect);
					formaPagamentoEmpresa.setCdFormaPagamento(retorno);
				}
				else {
					retorno = FormaPagamentoEmpresaDAO.update(formaPagamentoEmpresa, connect);
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "FORMAPAGAMENTOEMPRESA", formaPagamentoEmpresa);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
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
			pstmt = connect.prepareStatement("SELECT A.cd_forma_pagamento, A.tp_forma_pagamento, A.id_forma_pagamento, " +
					                         "       A.nm_forma_pagamento, B.cd_empresa, B.cd_administrador, B.cd_tipo_documento," +
					                         "       B.qt_dias_credito, B.vl_tarifa_transacao, B.pr_taxa_desconto, B.pr_desconto_rave," +
					                         "       B.cd_conta_carteira, B.cd_conta " +
					                         "FROM adm_forma_pagamento A " +
					                         "LEFT OUTER JOIN adm_forma_pagamento_empresa B ON (A.cd_forma_pagamento = B.cd_forma_pagamento" +
					                         "                                              AND B.cd_empresa         = "+cdEmpresa+") " +
					                         "WHERE A.cd_forma_pagamento = "+cdFormaPagamento);
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
			else
				return null;
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}


	public static ResultSetMap getAll(int cdEmpresa) {
		return getAll(cdEmpresa, null);
	}

	public static ResultSetMap getAll(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement(
					                         "SELECT A.*, B.cd_empresa, B.cd_administrador, B.cd_tipo_documento, B.qt_dias_credito," +
					                         "       B.vl_tarifa_transacao, B.pr_taxa_desconto, B.pr_desconto_rave, B.cd_conta_carteira," +
					                         "       B.cd_conta, C.nm_tipo_documento, C.sg_tipo_documento," +
									         "       D.nm_pessoa AS nm_administrador, D.nm_pessoa, E.nr_conta, E.nr_dv, " +
									         "       E.nm_conta, F.nr_agencia, G.nr_banco, G.nm_banco, " +
									         "       H.nm_carteira, H.sg_carteira " +
									         "FROM adm_forma_pagamento A " +
									         "LEFT OUTER JOIN adm_forma_pagamento_empresa B ON (A.cd_forma_pagamento = B.cd_forma_pagamento" +
									         "                                              AND B.cd_empresa = "+cdEmpresa+") " +
									         "LEFT OUTER JOIN adm_tipo_documento          C ON (B.cd_tipo_documento = C.cd_tipo_documento) " +
									         "LEFT OUTER JOIN grl_pessoa                  D ON (B.cd_administrador = D.cd_pessoa) " +
									         "LEFT OUTER JOIN adm_conta_financeira        E ON (B.cd_conta = E.cd_conta) " +
									         "LEFT OUTER JOIN grl_agencia                 F ON (E.cd_agencia = F.cd_agencia)" +
									         "LEFT OUTER JOIN grl_banco                   G ON (F.cd_banco = G.cd_banco) "+
									         "LEFT OUTER JOIN adm_conta_carteira          H ON (B.cd_conta = H.cd_conta " +
									         "                                              AND B.cd_conta_carteira = H.cd_conta_carteira) " +
									         "ORDER BY nm_forma_pagamento");
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoEmpresaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdFormaPagamento, int cdEmpresa){
		return remove(cdFormaPagamento, cdEmpresa, false, null);
	}
	public static Result remove(int cdFormaPagamento, int cdEmpresa, boolean cascade){
		return remove(cdFormaPagamento, cdEmpresa, cascade, null);
	}
	public static Result remove(int cdFormaPagamento, int cdEmpresa, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				retorno = FormaPagamentoServices.remove(cdFormaPagamento, true, connect).getCode();
			}
			if(!cascade || retorno>0)
			retorno = FormaPagamentoEmpresaDAO.delete(cdFormaPagamento, cdEmpresa, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllTituloCredito(int cdEmpresa) {
		return getAllTituloCredito(cdEmpresa, null);
	}

	public static ResultSetMap getAllTituloCredito(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement(
					                         "SELECT A.*, B.cd_empresa, B.cd_administrador, B.cd_tipo_documento, B.qt_dias_credito," +
					                         "       B.vl_tarifa_transacao, B.pr_taxa_desconto, B.pr_desconto_rave, B.cd_conta_carteira," +
					                         "       B.cd_conta, C.nm_tipo_documento, C.sg_tipo_documento," +
									         "       D.nm_pessoa AS nm_administrador, E.nr_conta, E.nr_dv, " +
									         "       E.nm_conta, F.nr_agencia, G.nr_banco, G.nm_banco, " +
									         "       H.nm_carteira, H.sg_carteira " +
									         "FROM adm_forma_pagamento A " +
									         "LEFT OUTER JOIN adm_forma_pagamento_empresa B ON (A.cd_forma_pagamento = B.cd_forma_pagamento" +
									         "                                              AND B.cd_empresa = "+cdEmpresa+") " +
									         "LEFT OUTER JOIN adm_tipo_documento          C ON (B.cd_tipo_documento = C.cd_tipo_documento) " +
									         "LEFT OUTER JOIN grl_pessoa                  D ON (B.cd_administrador = D.cd_pessoa) " +
									         "LEFT OUTER JOIN adm_conta_financeira        E ON (B.cd_conta = E.cd_conta) " +
									         "LEFT OUTER JOIN grl_agencia                 F ON (E.cd_agencia = F.cd_agencia)" +
									         "LEFT OUTER JOIN grl_banco                   G ON (F.cd_banco = G.cd_banco) "+
									         "LEFT OUTER JOIN adm_conta_carteira          H ON (B.cd_conta = H.cd_conta " +
									         "                                              AND B.cd_conta_carteira = H.cd_conta_carteira) " +
									         "WHERE A.tp_forma_pagamento = " + FormaPagamentoServices.TITULO_CREDITO +
									         "ORDER BY nm_forma_pagamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoEmpresaServices.getAll: " + e);
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
		return Search.find("SELECT DISTINCT(A.*), B.cd_empresa, B.cd_administrador, B.cd_tipo_documento, B.qt_dias_credito, " +
                           "       B.vl_tarifa_transacao, B.pr_taxa_desconto, B.pr_desconto_rave, B.cd_conta_carteira," +
                           "       B.cd_conta, C.nm_tipo_documento, C.sg_tipo_documento," +
				           "       D.nm_pessoa AS nm_administrador, E.nr_conta, E.nr_dv, " +
				           "       E.nm_conta, F.nr_agencia, G.nr_banco, G.nm_banco, " +
				           "       H.nm_carteira, H.sg_carteira " +
				           "FROM adm_forma_pagamento A " +
				           "LEFT OUTER JOIN adm_forma_pagamento_empresa B ON (A.cd_forma_pagamento = B.cd_forma_pagamento) " +
				           "LEFT OUTER JOIN adm_tipo_documento C ON (B.cd_tipo_documento = C.cd_tipo_documento) " +
				           "LEFT OUTER JOIN grl_pessoa D ON (B.cd_administrador = D.cd_pessoa) " +
				           "LEFT OUTER JOIN adm_conta_financeira        E ON (B.cd_conta = E.cd_conta) " +
					       "LEFT OUTER JOIN grl_agencia                 F ON (E.cd_agencia = F.cd_agencia)" +
					       "LEFT OUTER JOIN grl_banco                   G ON (F.cd_banco = G.cd_banco) "+
					       "LEFT OUTER JOIN adm_conta_carteira          H ON (B.cd_conta = H.cd_conta " +
					       "                                              AND B.cd_conta_carteira = H.cd_conta_carteira) ",
					       "ORDER BY nm_forma_pagamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null, false);
	}

	public static int insert(FormaPagamentoEmpresa formPagEmpresa, FormaPagamento formPag) {
		return insert(formPagEmpresa, formPag, null);
	}

	public static int insert(FormaPagamentoEmpresa formPagEmpresa, FormaPagamento formPag, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			int code = FormaPagamentoDAO.insert(formPag, connection);
			if (code<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			formPagEmpresa.setCdFormaPagamento(code);
			if (FormaPagamentoEmpresaDAO.insert(formPagEmpresa, connection) <= 0){
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return code;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.rollback(connection);
		}
	}

	public static int update(FormaPagamentoEmpresa formPagEmpresa, FormaPagamento formPag) {
		return update(formPagEmpresa, formPag, null);
	}

	public static int update(FormaPagamentoEmpresa formPagEmpresa, FormaPagamento formPag, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			int code = FormaPagamentoDAO.update(formPag, connection);
			if (code<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			formPagEmpresa.setCdFormaPagamento(formPag.getCdFormaPagamento());

			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM adm_forma_pagamento_empresa " +
					"WHERE cd_forma_pagamento = ? " +
					"  AND cd_empresa = ?");
			pstmt.setInt(1, formPag.getCdFormaPagamento());
			pstmt.setInt(2, formPagEmpresa.getCdEmpresa());
			ResultSet rs = pstmt.executeQuery();

			if ((!rs.next() ? FormaPagamentoEmpresaDAO.insert(formPagEmpresa, connection) :
				FormaPagamentoEmpresaDAO.update(formPagEmpresa, connection)) <= 0){
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.rollback(connection);
		}
	}

	public static int delete(int cdFormaPagamento, int cdEmpresa) {
		return delete(cdFormaPagamento, cdEmpresa, null);
	}

	public static int delete(int cdFormaPagamento, int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			if (FormaPagamentoEmpresaDAO.delete(cdFormaPagamento, cdEmpresa, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM adm_forma_pagamento_empresa " +
					"WHERE cd_forma_pagamento = ?");
			pstmt.setInt(1, cdFormaPagamento);
			if (!pstmt.executeQuery().next() && FormaPagamentoDAO.delete(cdFormaPagamento, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.rollback(connection);
		}
	}
	
	public static ResultSetMap getPlanoPagamentoByFormaPagamento(int cdEmpresa,int cdFormaPagamento){
		return getPlanoPagamentoByFormaPagamento(cdEmpresa, cdFormaPagamento, null);
	}
	
	public static ResultSetMap getPlanoPagamentoByFormaPagamento(int cdEmpresa,int cdFormaPagamento,Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			String sql = "SELECT FPP.cd_plano_pagamento,PP.nm_plano_pagamento " +
						 " FROM adm_forma_plano_pagamento FPP " +
						 "JOIN adm_plano_pagamento PP ON(FPP.cd_plano_pagamento = PP.cd_plano_pagamento) "+
						 "WHERE FPP.cd_empresa = " + cdEmpresa +
						 " AND FPP.cd_forma_pagamento = " + cdFormaPagamento;
			PreparedStatement pstmt = connect.prepareStatement(sql);
					                       
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoEmpresaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}