package com.tivic.manager.adm;

import java.util.*;
import java.sql.*;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.EmpresaServices;
import com.tivic.manager.util.Util;

import sol.util.Result;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class FormaPagamentoServices	{

	public static final int MOEDA_CORRENTE = 0;
	public static final int TEF 		   = 1;
	public static final int TITULO_CREDITO = 2;

	public static final String[] tipoFormaPagamento = {"Moeda Corrente", "Transf. Eletrônica de Fundos", "Título de Crédito"};

	
	public static Result save(FormaPagamento formaPagamento){
		return save(formaPagamento, null);
	}

	public static Result save(FormaPagamento formaPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(formaPagamento==null)
				return new Result(-1, "Erro ao salvar. FormaPagamento é nulo");

			int retorno;
			if(formaPagamento.getCdFormaPagamento()==0){
				retorno = FormaPagamentoDAO.insert(formaPagamento, connect);
				formaPagamento.setCdFormaPagamento(retorno);
			}
			else {
				retorno = FormaPagamentoDAO.update(formaPagamento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "FORMAPAGAMENTO", formaPagamento);
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
	
	public static Result remove(int cdFormaPagamento){
		return remove(cdFormaPagamento, false, null);
	}
	public static Result remove(int cdFormaPagamento, boolean cascade){
		return remove(cdFormaPagamento, cascade, null);
	}
	public static Result remove(int cdFormaPagamento, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = FormaPagamentoDAO.delete(cdFormaPagamento, connect);
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
	
	public static void init(){
		
		Connection connection = Conexao.conectar();
		try {
			if(FormaPagamentoDAO.getAll().size() > 0 || PlanoPagamentoDAO.getAll().size() > 0 )
				return ;
			// Criando planos de pagamento
			ArrayList<PlanoPagamento> planos = new ArrayList<PlanoPagamento>();
			planos.add(new PlanoPagamento(0, "A VISTA", null, 0, Util.getDataAtual(), null));
			planos.add(new PlanoPagamento(0, "1X SEM ENTRADA", null, 0, Util.getDataAtual(), null));
			planos.add(new PlanoPagamento(0, "2X IGUAIS", null, 0, Util.getDataAtual(), null));
			planos.add(new PlanoPagamento(0, "3X IGUAIS", null, 0, Util.getDataAtual(), null));
			planos.add(new PlanoPagamento(0, "4X IGUAIS", null, 0, Util.getDataAtual(), null));
			planos.add(new PlanoPagamento(0, "5X IGUAIS", null, 0, Util.getDataAtual(), null));
			planos.add(new PlanoPagamento(0, "6X IGUAIS", null, 0, Util.getDataAtual(), null));
			planos.add(new PlanoPagamento(0, "ROTATIVO (A VISTA)", null, 0, Util.getDataAtual(), null));
			for (int i = 0; i < planos.size(); i++){
				planos.get(i).setCdPlanoPagamento(PlanoPagamentoDAO.insert(planos.get(i), connection));
				if(planos.get(i).getCdPlanoPagamento() < 0){
					Conexao.rollback(connection);
					return;
				}
			}
			// Informações de Parcelamento	
			ArrayList<PlanoParcelamento> parcelamentos = new ArrayList<PlanoParcelamento>();
			parcelamentos.add(new PlanoParcelamento(planos.get(0).getCdPlanoPagamento(), 1, 0, 100, 0, 1));
			parcelamentos.add(new PlanoParcelamento(planos.get(1).getCdPlanoPagamento(), 1, 30, 100, 7, 1));
			parcelamentos.add(new PlanoParcelamento(planos.get(2).getCdPlanoPagamento(), 1, 30, 100, 7, 2));
			parcelamentos.add(new PlanoParcelamento(planos.get(3).getCdPlanoPagamento(), 1, 30, 100, 7, 3));
			parcelamentos.add(new PlanoParcelamento(planos.get(4).getCdPlanoPagamento(), 1, 30, 100, 7, 4));
			parcelamentos.add(new PlanoParcelamento(planos.get(5).getCdPlanoPagamento(), 1, 30, 100, 7, 5));
			parcelamentos.add(new PlanoParcelamento(planos.get(6).getCdPlanoPagamento(), 1, 30, 100, 7, 6));
			parcelamentos.add(new PlanoParcelamento(planos.get(7).getCdPlanoPagamento(), 1, 0, 100, 0, 1));
				
			for (int i = 0; i < parcelamentos.size(); i++){
				if(PlanoParcelamentoDAO.insert(parcelamentos.get(i), connection) < 0){
					Conexao.rollback(connection);
					return;
				}
			}
			/*
			 * FORMAS DE PAGAMENTO
			 */
			ArrayList<FormaPagamento> formaPagamento = new ArrayList<FormaPagamento>();
			formaPagamento.add(new FormaPagamento(0, "DINHEIRO",           "DN", null, FormaPagamentoServices.MOEDA_CORRENTE, 0));
			formaPagamento.add(new FormaPagamento(0, "BOLETO",             "Bol", null, FormaPagamentoServices.TITULO_CREDITO, 1));
			formaPagamento.add(new FormaPagamento(0, "CHEQUE",             "CH", null, FormaPagamentoServices.TITULO_CREDITO, 0));
			formaPagamento.add(new FormaPagamento(0, "PROMISSORIA",        "NP", "NP", FormaPagamentoServices.TITULO_CREDITO, 0));
			formaPagamento.add(new FormaPagamento(0, "CRÉDITO DO CLIENTE", "CRED", null, FormaPagamentoServices.TITULO_CREDITO, 0));
			formaPagamento.add(new FormaPagamento(0, "VISA CRED",          "Visa", null, FormaPagamentoServices.TEF, 1));
			formaPagamento.add(new FormaPagamento(0, "VISA DEB",           "VisaElectr", null, FormaPagamentoServices.TEF, 1));
			formaPagamento.add(new FormaPagamento(0, "MASTER CRED",        "Master", null, FormaPagamentoServices.TEF, 1));
			formaPagamento.add(new FormaPagamento(0, "MASTER DEB",         "MDEB", null, FormaPagamentoServices.TEF, 1));
			formaPagamento.add(new FormaPagamento(0, "HIPERCARD",          "Hiper", null, FormaPagamentoServices.TEF, 1));
			formaPagamento.add(new FormaPagamento(0, "DINERS",             "DINERS", null, FormaPagamentoServices.TEF, 0));
			formaPagamento.add(new FormaPagamento(0, "ELO CRED",           "EloCred", null, FormaPagamentoServices.TEF, 1));
			formaPagamento.add(new FormaPagamento(0, "ELO DEB",            "EloDeb", null, FormaPagamentoServices.TEF, 1));
				
			ArrayList<PlanoPagamento> todos = planos;
			
			PlanoPagamento aVista = planos.remove(0);
			PlanoPagamento rotativoAVista = planos.get(6);
			ArrayList<PlanoPagamento> todosMenosAVista = planos;
			
			ArrayList<PlanoPagamento> rotativo = new ArrayList<PlanoPagamento>();
			rotativo.add(rotativoAVista);
			
			ArrayList<PlanoPagamento> AVista = new ArrayList<PlanoPagamento>();
			AVista.add(aVista);
			AVista.add(rotativoAVista);
			
			int tipoPlanoPagamento = -1;//0 - todos, 1 - todosMenosAVista, 2 - rotativo, 3 - AVista
			for (int i = 0; i < formaPagamento.size(); i++){
				int cdTipoDocumento = 0;
				int qt_dias_credito = 0;
				switch(i){
					case 0://DÉBITO EM CONTA
						cdTipoDocumento = 0;//Nenhum Valor
						qt_dias_credito = 0;
						tipoPlanoPagamento = 2;
						break;
					case 1://DINHEIRO
						cdTipoDocumento = 0;//Nenhum Valor
						qt_dias_credito = 0;
						tipoPlanoPagamento = 3;
						break;
					case 2://BIG CARD
						cdTipoDocumento = 4;//Cartão de Crédito
						qt_dias_credito = 0;
						tipoPlanoPagamento = 1;
						break;
					case 3://BOLETO
						cdTipoDocumento = 12;//Boleto Bancário
						qt_dias_credito = 0;
						tipoPlanoPagamento = 0;
						break;
					case 4://CHEQUE
						cdTipoDocumento = 6;//Cheque
						qt_dias_credito = 0;
						tipoPlanoPagamento = 0;
						break;
					case 5://CRÉDITO ANTERIOR
						cdTipoDocumento = 8;//Crédito do Cliente
						qt_dias_credito = 0;
						tipoPlanoPagamento = 3;
						break;
					case 6://CRÉDITO DO CLIENTE
						cdTipoDocumento = 8;//Crédito do Cliente
						qt_dias_credito = 0;
						tipoPlanoPagamento = 3;
						break;
					case 7://DINERS
						cdTipoDocumento = 4;//Cartão de Crédito
						qt_dias_credito = 30;
						tipoPlanoPagamento = 1;
						break;
					case 8://HIPERCARD
						cdTipoDocumento = 4;//Cartão de Crédito
						qt_dias_credito = 30;
						tipoPlanoPagamento = 1;
						break;
					case 9://MASTER CRED
						cdTipoDocumento = 4;//Cartão de Crédito
						qt_dias_credito = 30;
						tipoPlanoPagamento = 1;
						break;
					case 10://MASTER DEB
						cdTipoDocumento = 3;//Nota de Débito
						qt_dias_credito = 2;
						tipoPlanoPagamento = 2;
						break;
					case 11://PROMISSORIA
						cdTipoDocumento = 1;//Nota Promissória
						qt_dias_credito = 0;
						tipoPlanoPagamento = 1;
						break;
					case 12://VALE CARD
						cdTipoDocumento = 4;//Cartão de Crédito
						qt_dias_credito = 30;
						tipoPlanoPagamento = 1;
						break;
					case 13://VISA CRED
						cdTipoDocumento = 4;//Cartão de Crédito
						qt_dias_credito = 30;
						tipoPlanoPagamento = 1;
						break;
					case 14://VISA DEB
						cdTipoDocumento = 3;//Nota de Débito
						qt_dias_credito = 2;
						tipoPlanoPagamento = 2;
					case 15://ELO CRED
						cdTipoDocumento = 4;//Cartão de Crédito
						qt_dias_credito = 30;
						tipoPlanoPagamento = 1;
						break;
					case 16://ELO DEB
						cdTipoDocumento = 3;//Nota de Débito
						qt_dias_credito = 2;
						tipoPlanoPagamento = 2;
						break;
				}
				
				formaPagamento.get(i).setCdFormaPagamento(
									FormaPagamentoEmpresaServices.insert(new FormaPagamentoEmpresa(formaPagamento.get(i).getCdFormaPagamento(),
									EmpresaServices.getDefaultEmpresa().getCdEmpresa(), 0, cdTipoDocumento,
									qt_dias_credito, 0.0, 0.0, 0.0, 0/*ContaCarteira*/,
									0/*Conta*/), formaPagamento.get(i), connection));
				
				if(formaPagamento.get(i).getCdFormaPagamento() < 0){
					Conexao.rollback(connection);
					return;
				}
				
				switch(tipoPlanoPagamento){
				
					case 0:
						for(int j=0; j < todos.size(); j++)	{
							if(saveFormaPlanoPagamento(EmpresaServices.getDefaultEmpresa().getCdEmpresa(), formaPagamento.get(i).getCdFormaPagamento(), todos.get(j).getCdPlanoPagamento(), 0, 0, connection).getCode()<=0)	{
								Conexao.rollback(connection);
								return;
							}
						}
						break;
					
					case 1:
						for(int j=0; j < todosMenosAVista.size(); j++)	{
							if(saveFormaPlanoPagamento(EmpresaServices.getDefaultEmpresa().getCdEmpresa(), formaPagamento.get(i).getCdFormaPagamento(), todosMenosAVista.get(j).getCdPlanoPagamento(), 0, 0, connection).getCode()<=0)	{
								Conexao.rollback(connection);
								return;
							}
						}					
						break;
										
					case 2:
						for(int j=0; j < rotativo.size(); j++)	{
							if(saveFormaPlanoPagamento(EmpresaServices.getDefaultEmpresa().getCdEmpresa(), formaPagamento.get(i).getCdFormaPagamento(), rotativo.get(j).getCdPlanoPagamento(), 0, 0, connection).getCode()<=0)	{
								Conexao.rollback(connection);
								return;
							}
						}
						
						break;
					
					case 3:
						for(int j=0; j < AVista.size(); j++)	{
							if(saveFormaPlanoPagamento(EmpresaServices.getDefaultEmpresa().getCdEmpresa(), formaPagamento.get(i).getCdFormaPagamento(), AVista.get(j).getCdPlanoPagamento(), 0, 0, connection).getCode()<=0)	{
								Conexao.rollback(connection);
								return;
							}
						}
						break;
				}
			}
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return;
		}
		finally {
			Conexao.desconectar(connection);
		}
		return;
	}
	
	public static Result saveFormaPlanoPagamento(int cdEmpresa, int cdFormaPagamento, ArrayList<?> planos) {
		Connection connect = Conexao.conectar();
		Result result = new Result(-1, "Nenhum plano enviado!");
		try	{
			connect.setAutoCommit(false);
			ResultSet rs = connect.prepareStatement("SELECT * FROM adm_forma_pagamento_empresa WHERE cd_empresa = "+cdEmpresa+" AND cd_forma_pagamento = "+cdFormaPagamento).executeQuery();
			if(!rs.next())	{
				System.out.println("Forma de pagamento associado à empresa automaticamente.");
				connect.prepareStatement("INSERT INTO adm_forma_pagamento_empresa (cd_empresa, cd_forma_pagamento) " +
	                 	                           "VALUES ("+cdEmpresa+","+cdFormaPagamento+")").executeUpdate();
			}
			for(int i=0; i<planos.size(); i++)	{
				int cdPlano = planos.get(i) instanceof String ? Integer.parseInt((String)planos.get(i)) : ((Integer)planos.get(i)).intValue();
				result = saveFormaPlanoPagamento(cdEmpresa, cdFormaPagamento, cdPlano, 0, 0, connect);
				if(result.getCode()<=0)	{
					Conexao.rollback(connect);
					return result;
				}
			}
			connect.commit();
		}
		catch(Exception e)	{
			Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar associar plano à forma de pagamento!", e);
		}
		return result;
	}

	public static Result saveFormaPlanoPagamento(int cdEmpresa, int cdFormaPagamento, int cdPlanoPagamento, float prTaxaDesconto, float prDescontoMaximo) {
		return saveFormaPlanoPagamento(cdEmpresa, cdFormaPagamento, cdPlanoPagamento, prTaxaDesconto, prDescontoMaximo, null);
	}
	public static Result saveFormaPlanoPagamento(int cdEmpresa, int cdFormaPagamento, int cdPlanoPagamento, float prTaxaDesconto, float prDescontoMaximo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
																"FROM adm_forma_plano_pagamento " +
																"WHERE cd_empresa         = "+cdEmpresa+
																"  AND cd_forma_pagamento = "+cdFormaPagamento+
																"  AND cd_plano_pagamento = "+cdPlanoPagamento);
			int ret = 0;
			if(!pstmt.executeQuery().next())	{
				ret = connect.prepareStatement("INSERT INTO adm_forma_plano_pagamento (cd_empresa, cd_forma_pagamento,cd_plano_pagamento,pr_taxa_desconto,pr_desconto_maximo) " +
						                 	   "VALUES ("+cdEmpresa+","+cdFormaPagamento+","+cdPlanoPagamento+","+prTaxaDesconto+","+prDescontoMaximo+")").executeUpdate();
			}
			else	{
				ret = connect.prepareStatement("UPDATE adm_forma_plano_pagamento " +
						                       "SET pr_taxa_desconto = " +prTaxaDesconto+
						                       "   ,pr_desconto_maximo = "+prDescontoMaximo+
						                       " WHERE cd_empresa         = "+cdEmpresa+
						                       "   AND cd_forma_pagamento = "+cdFormaPagamento+
						                       "   AND cd_plano_pagamento = "+cdPlanoPagamento).executeUpdate();
			}
			return new Result(ret);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao incluir relação entre Forma de Pagamento e o Plano!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result deleteFormaPlanoPagamento(int cdEmpresa, int cdFormaPagamento, int cdPlanoPagamento) {
		return deleteFormaPlanoPagamento(cdEmpresa, cdFormaPagamento, cdPlanoPagamento, null);
	}
	public static Result deleteFormaPlanoPagamento(int cdEmpresa, int cdFormaPagamento, int cdPlanoPagamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			return new Result(connect.prepareStatement("DELETE FROM adm_forma_plano_pagamento " +
													   "WHERE cd_empresa         = "+cdEmpresa+
													   "  AND cd_forma_pagamento = "+cdFormaPagamento+
													   "  AND cd_plano_pagamento = "+cdPlanoPagamento).executeUpdate());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao excluir relação entre Forma de Pagamento e o Plano!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllFormasPagTransf() {
		return getAllFormasPagTransf(null);
	}

	public static ResultSetMap getAllFormasPagTransf(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM adm_forma_pagamento " +
					"WHERE lg_transferencia = 1");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllFormasPagMoedaCorrente() {
		return getAllFormasPagMoedaCorrente(null);
	}

	public static ResultSetMap getAllFormasPagMoedaCorrente(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM adm_forma_pagamento " +
					"WHERE tp_forma_pagamento = ?");
			pstmt.setInt(1, MOEDA_CORRENTE);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getPlanosOf(int cdEmpresa, int cdFormaPagamento) {
		return getPlanosOf(cdEmpresa, cdFormaPagamento, null);
	}

	public static ResultSetMap getPlanosOf(int cdEmpresa, int cdFormaPagamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.*, B.nm_plano_pagamento, B.id_plano_pagamento " +
					"FROM adm_forma_plano_pagamento A " +
					"JOIN adm_plano_pagamento B ON (A.cd_plano_pagamento = B.cd_plano_pagamento) " +
					"WHERE A.cd_empresa         = "+cdEmpresa+
					"  AND A.cd_forma_pagamento = "+cdFormaPagamento+
					" ORDER BY nm_plano_pagamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Double getTaxaDesconto(int cdEmpresa, int cdFormaPagamento, int cdPlanoPagamento, Double prDefault, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			ResultSet rs = connection.prepareStatement("SELECT * FROM adm_forma_plano_pagamento A " +
													   " WHERE cd_empresa         = "+cdEmpresa+
													   "  AND cd_forma_pagamento = "+cdFormaPagamento+
													   "  AND cd_plano_pagamento = "+cdPlanoPagamento).executeQuery();
			if(rs.next() && rs.getDouble("pr_taxa_desconto")>0)
				return rs.getDouble("pr_taxa_desconto");
			return prDefault;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return 0.0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getAsResultSetMap(int cdFormaPagamento) {
		Connection connection = null;
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_forma_pagamento", "" + cdFormaPagamento, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmFormaPagamento = FormaPagamentoDAO.find(criterios);
			return rsmFormaPagamento;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_forma_pagamento ORDER BY nm_forma_pagamento");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPagamentoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_forma_pagamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
}