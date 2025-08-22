package com.tivic.manager.adm;

import java.sql.*;
import java.text.NumberFormat;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.Util;

public class CondicaoPagamentoClienteServices {

	/**
	 * Metodo que relaciona uma condição de pagamento a um cliente, verificando se esse cliente já não possui uma condição de pagamento com a forma de pagamento
	 * da nova condição e verifica o limite de credito geral do cliente, para não estoura-lo
	 * @since 03/08/2014
	 * @author Gabriel
	 * @param condicaoPagamentoCliente
	 * @return
	 */
	public static Result save(CondicaoPagamentoCliente condicaoPagamentoCliente){
		return save(condicaoPagamentoCliente, null);
	}

	public static Result save(CondicaoPagamentoCliente condicaoPagamentoCliente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(condicaoPagamentoCliente==null)
				return new Result(-1, "Erro ao salvar. CondicaoPagamentoCliente é nulo");

			//Busca a forma de pagamento da condição passada
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_condicao_pagamento", condicaoPagamentoCliente.getCdCondicaoPagamento() + "", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmCondicaoFormaPlanoPagamentoPassada = CondicaoFormaPlanoPagamentoDAO.find(criterios, connect);
			if(rsmCondicaoFormaPlanoPagamentoPassada.next()){
				//Busca as condições de pagamento já cadastradas para o cliente
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_pessoa", condicaoPagamentoCliente.getCdPessoa() + "", ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_empresa", condicaoPagamentoCliente.getCdEmpresa() + "", ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmCondicaoCliente = CondicaoPagamentoClienteDAO.find(criterios, connect);
				while(rsmCondicaoCliente.next()){
					//Busca a forma de pagamento da condição atual
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_condicao_pagamento", rsmCondicaoCliente.getInt("cd_condicao_pagamento") + "", ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmCondicaoFormaPlanoPagamento = CondicaoFormaPlanoPagamentoDAO.find(criterios, connect);
					if(rsmCondicaoFormaPlanoPagamento.next()){
						//Se tiverem a mesma forma de pagamento o cliente não poderá ter aquela condição
						if(rsmCondicaoFormaPlanoPagamentoPassada.getInt("cd_forma_pagamento") == rsmCondicaoFormaPlanoPagamento.getInt("cd_forma_pagamento")){
							if (isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Esse cliente já possui uma condição de pagamento para essa forma de pagamento!");
						}
					}
				}
				//Busca as condições de pagamento já cadastradas para a classificacao do cliente
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_classificacao", condicaoPagamentoCliente.getCdClassificacao() + "", ItemComparator.EQUAL, Types.INTEGER));
				rsmCondicaoCliente = CondicaoPagamentoClienteDAO.find(criterios, connect);
				while(rsmCondicaoCliente.next()){
					//Busca a forma de pagamento da condição atual
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_condicao_pagamento", rsmCondicaoCliente.getInt("cd_condicao_pagamento") + "", ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmCondicaoFormaPlanoPagamento = CondicaoFormaPlanoPagamentoDAO.find(criterios, connect);
					if(rsmCondicaoFormaPlanoPagamento.next()){
						//Se tiverem a mesma forma de pagamento a classificacao não poderá ter aquela condição
						if(rsmCondicaoFormaPlanoPagamentoPassada.getInt("cd_forma_pagamento") == rsmCondicaoFormaPlanoPagamento.getInt("cd_forma_pagamento")){
							if (isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Esse classificação já possui uma condição de pagamento para essa forma de pagamento!");
						}
					}
				}
			}
			int retorno;
			if(condicaoPagamentoCliente.getCdCondicaoPagamentoCliente()==0){
				retorno = CondicaoPagamentoClienteDAO.insert(condicaoPagamentoCliente, connect);
				condicaoPagamentoCliente.setCdCondicaoPagamentoCliente(retorno);
			}
			else {
				retorno = CondicaoPagamentoClienteDAO.update(condicaoPagamentoCliente, connect);
			}

			if(retorno > 0){
				//Limite de credito
				Cliente cliente = ClienteDAO.get(condicaoPagamentoCliente.getCdEmpresa(), condicaoPagamentoCliente.getCdPessoa(), connect);
				if(cliente != null){
					float vlLimiteCredito = cliente.getVlLimiteCredito();
					if(vlLimiteCredito > 0){
						float somaLimite = 0;
						PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM adm_condicao_pagamento_cliente WHERE cd_pessoa = " + condicaoPagamentoCliente.getCdPessoa() + " AND cd_empresa = " + condicaoPagamentoCliente.getCdEmpresa());
						ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
						while(rsm.next()){
							CondicaoPagamento condicaoPagamento = CondicaoPagamentoDAO.get(rsm.getInt("cd_condicao_pagamento"), connect);
							somaLimite += condicaoPagamento.getVlLimite();
						}
						
						pstmt = connect.prepareStatement(" SELECT vl_limite FROM adm_cliente_produto A " +
															" WHERE A.cd_pessoa = " + condicaoPagamentoCliente.getCdPessoa() +
															" AND A.cd_empresa = "  + condicaoPagamentoCliente.getCdEmpresa() +
															" AND A.cd_produto_servico = ?");
						
						PreparedStatement pstmtCombustivel = connect.prepareStatement("SELECT B.nm_produto_servico AS nm_combustivel, B.cd_produto_servico AS cd_combustivel " +
																						  "FROM alm_produto_grupo A " +
																						  "JOIN grl_produto_servico B ON (A.cd_produto_servico = B.cd_produto_servico) " +
																						  "WHERE A.cd_grupo = " + ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, condicaoPagamentoCliente.getCdEmpresa()));
						
						ResultSetMap rsmCombustivel = new ResultSetMap(pstmtCombustivel.executeQuery());
						while(rsmCombustivel.next()){
							pstmt.setInt(1, rsmCombustivel.getInt("cd_combustivel"));
							rsm = new ResultSetMap(pstmt.executeQuery());
							while(rsm.next()){
								somaLimite += rsm.getFloat("vl_limite");
							}
						}
						
						
						somaLimite += cliente.getVlLimiteVale();
						somaLimite += cliente.getVlLimiteFactoring();
						
						if(vlLimiteCredito < somaLimite){
							if(isConnectionNull)
								Conexao.rollback(connect);
							NumberFormat formatoPreco = NumberFormat.getCurrencyInstance();  
					        formatoPreco.setMaximumFractionDigits(2);
					        
							return new Result(-1, "A soma dos limites de combustíveis, vale, troca de cheque e condições de pagamento ultrapassou o limite geral. " +
												  "O valor do limite é "+formatoPreco.format(vlLimiteCredito)+" e a soma desses limites foi de "+formatoPreco.format(somaLimite)+"!");
						}
					}
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CONDICAOPAGAMENTOCLIENTE", condicaoPagamentoCliente);
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
	public static Result remove(int cdCondicaoPagamentoCliente, int cdCondicaoPagamento){
		return remove(cdCondicaoPagamentoCliente, cdCondicaoPagamento, false, null);
	}
	public static Result remove(int cdCondicaoPagamentoCliente, int cdCondicaoPagamento, boolean cascade){
		return remove(cdCondicaoPagamentoCliente, cdCondicaoPagamento, cascade, null);
	}
	public static Result remove(int cdCondicaoPagamentoCliente, int cdCondicaoPagamento, boolean cascade, Connection connect){
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
			retorno = CondicaoPagamentoClienteDAO.delete(cdCondicaoPagamentoCliente, cdCondicaoPagamento, connect);
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
	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_condicao_pagamento_cliente");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoClienteServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoClienteServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo para buscar todas as condições de pagamento relacionadas a um cliente
	 * @since 03/04/2014
	 * @author Gabriel
	 * @param cdEmpresa
	 * @param cdPessoa
	 * @return
	 */
	public static ResultSetMap getAllCondicaoPagamentoByCliente(int cdEmpresa, int cdPessoa) {
		return getAllCondicaoPagamentoByCliente(cdEmpresa, cdPessoa, null);
	}

	public static ResultSetMap getAllCondicaoPagamentoByCliente(int cdEmpresa, int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
				ResultSetMap rsmCondicaoPagamentoFinal = new ResultSetMap();
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_empresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_pessoa", "" + cdPessoa, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsm = CondicaoPagamentoClienteDAO.find(criterios, connect);
				while(rsm.next()){
					ResultSetMap rsmCondicaoPagamento = CondicaoPagamentoServices.get(rsm.getInt("cd_condicao_pagamento"), connect);
					if(rsmCondicaoPagamento.next()){
						rsmCondicaoPagamento.setValueToField("CL_VALIDADE_CONDICAO", rsmCondicaoPagamento.getDateFormat("dt_validade_condicao", "dd/MM/yyyy"));
						rsmCondicaoPagamento.setValueToField("CL_VALIDADE_LIMITE",   rsmCondicaoPagamento.getDateFormat("dt_validade_limite", "dd/MM/yyyy"));
						rsmCondicaoPagamento.setValueToField("CD_CONDICAO_PAGAMENTO_CLIENTE", rsm.getInt("cd_condicao_pagamento_cliente"));
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_condicao_pagamento", "" + rsm.getInt("cd_condicao_pagamento"), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmCondicaoForma = CondicaoFormaPlanoPagamentoDAO.find(criterios, connect);
						if(rsmCondicaoForma.next()){
							rsmCondicaoPagamento.setValueToField("NM_FORMA_PAGAMENTO", FormaPagamentoDAO.get(rsmCondicaoForma.getInt("cd_forma_pagamento"), connect).getNmFormaPagamento());
						}
						rsmCondicaoPagamentoFinal.addRegister(rsmCondicaoPagamento.getRegister());
						
					}
				}
				return rsmCondicaoPagamentoFinal;
		}
		
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoClienteServices.getAllCondicaoPagamentoByCliente: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo para buscar todas as condições de pagamento relacionadas a uma classificacao
	 * @since 03/04/2014
	 * @author Gabriel
	 * @param cdEmpresa
	 * @param cdPessoa
	 * @return
	 */
	public static ResultSetMap getAllCondicaoPagamentoByClassificacao(int cdClassificacao) {
		return getAllCondicaoPagamentoByClassificacao(cdClassificacao, null);
	}

	public static ResultSetMap getAllCondicaoPagamentoByClassificacao(int cdClassificacao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
				ResultSetMap rsmCondicaoPagamentoFinal = new ResultSetMap();
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_classificacao", "" + cdClassificacao, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsm = CondicaoPagamentoClienteDAO.find(criterios, connect);
				while(rsm.next()){
					ResultSetMap rsmCondicaoPagamento = CondicaoPagamentoServices.get(rsm.getInt("cd_condicao_pagamento"), connect);
					if(rsmCondicaoPagamento.next()){
						rsmCondicaoPagamento.setValueToField("CL_VALIDADE_CONDICAO", rsmCondicaoPagamento.getDateFormat("dt_validade_condicao", "dd/MM/yyyy"));
						rsmCondicaoPagamento.setValueToField("CL_VALIDADE_LIMITE",   rsmCondicaoPagamento.getDateFormat("dt_validade_limite", "dd/MM/yyyy"));
						rsmCondicaoPagamento.setValueToField("CD_CONDICAO_PAGAMENTO_CLIENTE", rsm.getInt("cd_condicao_pagamento_cliente"));
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_condicao_pagamento", "" + rsm.getInt("cd_condicao_pagamento"), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmCondicaoForma = CondicaoFormaPlanoPagamentoDAO.find(criterios, connect);
						if(rsmCondicaoForma.next()){
							rsmCondicaoPagamento.setValueToField("NM_FORMA_PAGAMENTO", FormaPagamentoDAO.get(rsmCondicaoForma.getInt("cd_forma_pagamento"), connect).getNmFormaPagamento());
						}
						rsmCondicaoPagamentoFinal.addRegister(rsmCondicaoPagamento.getRegister());
					}
				}
				return rsmCondicaoPagamentoFinal;
		}
		
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoClienteServices.getAllCondicaoPagamentoByClassificacao: " + e);
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
		return Search.find("SELECT * FROM adm_condicao_pagamento_cliente", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	/**
	 * Metodo para buscar condições de pagamento relacionadas a um cliente e uma forma de pagamento
	 * @since 20/04/2015
	 * @author João Marlon
	 * @param cdEmpresa
	 * @param cdPessoa
	 * @param cdFormaPagamento
	 * @return resulta da sql
	 */
	public static ResultSetMap getCondicaoPagamentoByCliente(int cdEmpresa, int cdPessoa, int cdFormaPagamento) {
		return getCondicaoPagamentoByCliente(cdEmpresa, cdPessoa, cdFormaPagamento, null);
	}
	
	public static ResultSetMap getCondicaoPagamentoByCliente(int cdEmpresa, int cdPessoa, int cdFormaPagamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ResultSetMap rsm = new ResultSetMap();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM adm_condicao_pagamento A" +
											"	  JOIN adm_condicao_pagamento_cliente B ON (A.cd_condicao_pagamento = B.cd_condicao_pagamento) " +
											"	  JOIN adm_condicao_forma_plano_pagamento C ON (A.cd_condicao_pagamento = C.cd_condicao_pagamento) " +
											"     WHERE B.cd_pessoa  = ?" +
											" 	    AND B.cd_empresa = ?" +
											" 	    AND C.cd_forma_pagamento = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdFormaPagamento);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoClienteServices.getCondicaoPagamentoByCliente: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
