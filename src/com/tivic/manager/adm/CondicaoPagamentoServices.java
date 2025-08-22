package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.util.Util;

public class CondicaoPagamentoServices {

	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO   = 1;
	
	
	/**
	 * Metodo que busca as Condições de pagamento do Cliente
	 * @since 03/08/2014
	 * @author Gabriel
	 * @param cdPessoa
	 * @param cdEmpresa
	 * @return
	 */
	public static ResultSetMap getFormaPagamentoCliente(int cdPessoa,int cdEmpresa){
		return getFormaPagamentoCliente(cdPessoa, cdEmpresa, null);
	}
	
	public static ResultSetMap getFormaPagamentoCliente(int cdPessoa,int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			//Primeiramente tentasse buscar a forma de pagamento para o cliente, e então para a classificação. Caso em nenhum dos casos seja encontrada, então está não é incluida
			
			//Busca as formas de pagamento cadastradas
			ResultSetMap rsmGeral = new ResultSetMap(connect.prepareStatement("SELECT * FROM adm_forma_pagamento").executeQuery());
			Pessoa cliente = PessoaDAO.get(cdPessoa);
			ResultSetMap rsmFinal = new ResultSetMap();
			
			//Obs.: Os statement internos tem o problema de retornarem diversos registros iguais por conta da tabela condicao_plano_forma_pagamento ser replicada em seus 
			//multiplos planos de pagamento
			
			//Busca as condições de pagamento para esse cliente que tenham uma determinada forma de pagamento
			PreparedStatement pstmtCondicaoCliente = connect.prepareStatement("SELECT FP.cd_forma_pagamento,FP.nm_forma_pagamento,CPC.* " +
																 "FROM adm_condicao_pagamento_cliente CPC " +
																 "JOIN adm_condicao_forma_plano_pagamento CPFP ON (CPFP.cd_condicao_pagamento = CPC.cd_condicao_pagamento) " +
																 "JOIN adm_forma_pagamento FP ON(CPFP.cd_forma_pagamento = FP.cd_forma_pagamento) " +
																 "WHERE CPC.cd_pessoa = " + cdPessoa +
																 "  AND CPC.cd_empresa = " + cdEmpresa +
																 "  AND FP.cd_forma_pagamento = ?");
			
			//Busca as condições de pagamento para a classificacao desse cliente que tenham uma determinada forma de pagamento
			PreparedStatement pstmtCondicaoClassificacao = connect.prepareStatement("SELECT FP.cd_forma_pagamento,FP.nm_forma_pagamento,CPC.* " +
																 "FROM adm_condicao_pagamento_cliente CPC " +
																 "JOIN adm_condicao_forma_plano_pagamento CPFP ON (CPFP.cd_condicao_pagamento = CPC.cd_condicao_pagamento) " +
																 "JOIN adm_forma_pagamento FP ON(CPFP.cd_forma_pagamento = FP.cd_forma_pagamento) " +
																 "WHERE CPC.cd_classificacao = " + (cliente.getCdClassificacao() == 0 ? (ClassificacaoServices.getPadrao() != null ? ClassificacaoServices.getPadrao().getCdClassificacao() : 0) : cliente.getCdClassificacao()) + 
																 "  AND FP.cd_forma_pagamento = ?");
			
			//Itera sobre as formas de pagamento
			while(rsmGeral.next()){
				
				pstmtCondicaoCliente.setInt(1, rsmGeral.getInt("cd_forma_pagamento"));
				pstmtCondicaoClassificacao.setInt(1, rsmGeral.getInt("cd_forma_pagamento"));
				
				ResultSetMap rsmCondicaoCliente = new ResultSetMap(pstmtCondicaoCliente.executeQuery());
				ResultSetMap rsmCondicaoClassificacao = new ResultSetMap(pstmtCondicaoClassificacao.executeQuery());
				
				//Caso o cliente tenha uma condição de pagamento que tenha determinada forma de pagamento, ela é registrada
				if(rsmCondicaoCliente.next()){
					rsmCondicaoCliente.setValueToField("CL_VALIDADE_CONDICAO", rsmCondicaoCliente.getDateFormat("dt_validade_condicao", "dd/MM/yyyy"));
					rsmCondicaoCliente.setValueToField("CL_VALIDADE_LIMITE",   rsmCondicaoCliente.getDateFormat("dt_validade_limite", "dd/MM/yyyy"));
					rsmFinal.addRegister(rsmCondicaoCliente.getRegister());
				}
				//Caso o cliente não tenha diretamente essa forma de pagamento em uma condição de pagamento, então é procurada nas condições de pagamento de sua classificação
				else if(rsmCondicaoClassificacao.next()){
					rsmCondicaoClassificacao.setValueToField("CL_VALIDADE_CONDICAO", rsmCondicaoClassificacao.getDateFormat("dt_validade_condicao", "dd/MM/yyyy"));
					rsmCondicaoClassificacao.setValueToField("CL_VALIDADE_LIMITE",   rsmCondicaoClassificacao.getDateFormat("dt_validade_limite", "dd/MM/yyyy"));
					rsmFinal.addRegister(rsmCondicaoClassificacao.getRegister());
				}
			}
			return rsmFinal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			System.err.println("Erro! ClientePagamentoServices.getFormaPagamentoCliente: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo que busca as Condições de pagamento da classificação de cliente
	 * @since 03/08/2014
	 * @author Gabriel
	 * @param cdClassificacao
	 * @return
	 */
	public static ResultSetMap getFormaPagamentoCliente(int cdClassificacao){
		return getFormaPagamentoCliente(cdClassificacao, null);
	}
	
	public static ResultSetMap getFormaPagamentoCliente(int cdClassificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			//Busca as formas de pagamento cadastradas
			ResultSetMap rsmGeral = new ResultSetMap(connect.prepareStatement("SELECT * FROM adm_forma_pagamento").executeQuery());
			ResultSetMap rsmFinal = new ResultSetMap();
			
			//Busca as condições de pagamento para a classificacao desse cliente que tenham uma determinada forma de pagamento
			PreparedStatement pstmtCondicaoClassificacao = connect.prepareStatement("SELECT FP.nm_forma_pagamento,CPC.* " +
																 "FROM adm_condicao_pagamento_cliente CPC " +
																 "JOIN adm_condicao_forma_plano_pagamento CPFP ON (CPFP.cd_condicao_pagamento = CPC.cd_condicao_pagamento) " +
																 "JOIN adm_forma_pagamento FP ON(CPFP.cd_forma_pagamento = FP.cd_forma_pagamento) " +
																 "JOIN adm_condicao_pagamento CP ON(CPFP.cd_condicao_pagamento = CP.cd_condicao_pagamento) " +
																 "WHERE CPC.cd_classificacao = " + cdClassificacao + 
																 "  AND FP.cd_forma_pagamento = ? " + 
																 "  AND CP.st_condicao_pagamento = " + ST_ATIVO + 
																 "  AND CP.dt_validade_condicao  >= " + Util.convCalendarStringSql(Util.getDataAtual()));
			
			
			//Itera sobre as formas de pagamento
			while(rsmGeral.next()){
				
				pstmtCondicaoClassificacao.setInt(1, rsmGeral.getInt("cd_forma_pagamento"));
				ResultSetMap rsmCondicaoClassificacao = new ResultSetMap(pstmtCondicaoClassificacao.executeQuery());
				
				//Caso a classificacao tenha diretamente essa forma de pagamento em uma condição de pagamento, ela é registrada
				if(rsmCondicaoClassificacao.next()){
					rsmCondicaoClassificacao.setValueToField("CL_VALIDADE_CONDICAO", rsmCondicaoClassificacao.getDateFormat("dt_validade_condicao", "dd/MM/yyyy"));
					rsmCondicaoClassificacao.setValueToField("CL_VALIDADE_LIMITE",   rsmCondicaoClassificacao.getDateFormat("dt_validade_limite", "dd/MM/yyyy"));
					rsmFinal.addRegister(rsmCondicaoClassificacao.getRegister());
				}
			}
			return rsmFinal;
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClientePagamentoServices.getFormaPagamentoCliente: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo que insere uma nova condição de pagamento 
	 * @since 03/08/2014
	 * @author Gabriel
	 * @param objeto
	 * @return
	 */
	public static Result insert(CondicaoPagamento objeto, int cdFormaPagamento, int cdEmpresa) {
		return insert(objeto, cdFormaPagamento, cdEmpresa, null);
	}

	public static Result insert(CondicaoPagamento objeto, int cdFormaPagamento, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			//Garante que apenas um registro no sistema será o padrão
			if(objeto.getLgPadrao()==1){
				setPadrao(objeto.getCdCondicaoPagamento(), connect);
			}
			
			
			//Insere a condição de pagamento
			int code = CondicaoPagamentoDAO.insert(objeto, connect);
			
			if(code < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao cadastrar nova condicao de pagamento");
			}
			
			//Insere todas as relação de condição de pagamento com as formas e planos de pagamento que existem em formaPagamentoEmpresa
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_forma_pagamento", cdFormaPagamento + "", ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_empresa", cdEmpresa + "", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmFormaPlano = FormaPlanoPagamentoDAO.find(criterios, connect);
			while(rsmFormaPlano.next()){
				if(CondicaoFormaPlanoPagamentoDAO.insert(new CondicaoFormaPlanoPagamento(objeto.getCdCondicaoPagamento(), rsmFormaPlano.getInt("cd_plano_pagamento"), cdFormaPagamento, cdEmpresa), connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao cadastrar Condicao Forma Plano Pagamento");
				}
			}
			
			
			Result resultado = new Result(code);
			
			if(isConnectionNull){
				connect.commit();
			}
			
			return resultado;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoDAO.insert: " +  e);
			return new Result(-1, "Erro ao inserir Condicao Pagamento!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * Metodo que atualiza uma condição de pagamento 
	 * @since 03/08/2014
	 * @author Gabriel
	 * @param objeto
	 * @return
	 */
	public static Result update(CondicaoPagamento objeto, int cdFormaPagamento, int cdEmpresa) {
		return update(objeto, cdFormaPagamento, cdEmpresa, 0, null);
	}

	public static Result update(CondicaoPagamento objeto, int cdFormaPagamento, int cdEmpresa, int cdCondicaoPagamentoOld) {
		return update(objeto, cdFormaPagamento, cdEmpresa, cdCondicaoPagamentoOld, null);
	}

	public static Result update(CondicaoPagamento objeto, int cdFormaPagamento, int cdEmpresa, Connection connect) {
		return update(objeto, cdFormaPagamento, cdEmpresa, 0, connect);
	}

	public static Result update(CondicaoPagamento objeto, int cdFormaPagamento, int cdEmpresa, int cdCondicaoPagamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			//Garante que apenas um registro no sistema será o padrão
			if(objeto.getLgPadrao()==1){
				setPadrao(objeto.getCdCondicaoPagamento(), connect);
			}
			
			//Deleta todas as relações da condição de pagamento com a forma e plano de pagamento, para ser reconstruidas as relações novamente depois de atualizado o objeto
			//Isso previne o sistema caso a forma de pagamento tenha sido mudada
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_condicao_pagamento", objeto.getCdCondicaoPagamento() + "", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmCondicaoFormaPlano = CondicaoFormaPlanoPagamentoDAO.find(criterios, connect);
			while(rsmCondicaoFormaPlano.next()){
				if(CondicaoFormaPlanoPagamentoDAO.delete(objeto.getCdCondicaoPagamento(), rsmCondicaoFormaPlano.getInt("cd_plano_pagamento"), rsmCondicaoFormaPlano.getInt("cd_forma_pagamento"), rsmCondicaoFormaPlano.getInt("cd_empresa"), connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar Condicao Forma Plano");
				}
			}
			
			
			Result resultado = new Result(CondicaoPagamentoDAO.update(objeto, cdCondicaoPagamentoOld, connect));
			
			//Insere todas as relação de condição de pagamento com as formas e planos de pagamento que existem em formaPagamentoEmpresa
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_forma_pagamento", cdFormaPagamento + "", ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_empresa", cdEmpresa + "", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmFormaPlano = FormaPlanoPagamentoDAO.find(criterios, connect);
			while(rsmFormaPlano.next()){
				if(CondicaoFormaPlanoPagamentoDAO.insert(new CondicaoFormaPlanoPagamento(objeto.getCdCondicaoPagamento(), rsmFormaPlano.getInt("cd_plano_pagamento"), cdFormaPagamento, cdEmpresa), connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao cadastrar Condicao Forma Plano Pagamento");
				}
			}
			
			if(isConnectionNull){
				connect.commit();
			}
			
			return resultado;
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoDAO.update: " +  e);
			return new Result(-1, "Erro ao atualizar condicao pagamento!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * Metodo que deleta uma condição de pagamento 
	 * @since 03/08/2014
	 * @author Gabriel
	 * @param objeto
	 * @return
	 */
	public static Result delete(int cdCondicaoPagamento) {
		return delete(cdCondicaoPagamento, null);
	}

	public static Result delete(int cdCondicaoPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			//Deleta todas as relações dessa condição de pagamento com as formasPlanoPagamento existentes 
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_condicao_pagamento", cdCondicaoPagamento + "", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmCondicaoFormaPlano = CondicaoFormaPlanoPagamentoDAO.find(criterios, connect);
			while(rsmCondicaoFormaPlano.next()){
				if(CondicaoFormaPlanoPagamentoDAO.delete(cdCondicaoPagamento, rsmCondicaoFormaPlano.getInt("cd_plano_pagamento"), rsmCondicaoFormaPlano.getInt("cd_forma_pagamento"), rsmCondicaoFormaPlano.getInt("cd_empresa"), connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar Condicao Forma Plano Pagamento");
				}
			}
			
			Result resultado = new Result(CondicaoPagamentoDAO.delete(cdCondicaoPagamento, connect));
			
			if(isConnectionNull){
				connect.commit();
			}
			
			return resultado;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoDAO.delete: " +  e);
			return new Result(-1, "Erro ao deletar Condicao Pagamento");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * getStatusCliente
	 * 
	 * @author Joao Marlon
	 * @param  cdEmpresa            // codigo da empresa para consulta  
	 * @param  cdCliente            // codigo do cliente para consulta
	 * @return ResultSetMap com resultado da pesquisa do status do cliente para ser exibido no pdv.
	 * 		   Considerando as contas em aberto e os limites cadastrados.
	 * */	
	public static ResultSetMap getStatusCliente(int cdEmpresa, int cdCliente){
		return getStatusCliente(cdEmpresa, cdCliente, null);
	}
	
	public static ResultSetMap getStatusCliente(int cdEmpresa, int cdCliente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {			
			connect = isConnectionNull ? Conexao.conectar() : connect;
			boolean hasConta = false;
			String sql = "SELECT * FROM adm_conta_receber " +
						 "WHERE cd_pessoa  = " +cdCliente+
						 "  AND cd_empresa = " +cdEmpresa +
						 "  AND st_conta   = " + ContaReceberServices.ST_EM_ABERTO;
			PreparedStatement pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if (rsm.next())
				hasConta = true;
			
			pstmt.close();
			rsm = null;
			//
			sql = "SELECT A.cd_empresa, A.cd_forma_pagamento, B.nm_forma_pagamento, A.cd_pessoa, " +
			     "        C.vl_limite_credito, A.vl_limite AS vl_limite_pagamento, SUM(D.vl_conta)  AS vl_limite_utilizado " +
				 "FROM adm_condicao_pagamento A " +
				 "JOIN adm_condicao_pagamento_cliente AA ON (A.cd_condicao_pagamento = AA.cd_condicao_pagamento) " +
				 "LEFT OUTER JOIN adm_forma_pagamento B  ON (A.cd_forma_pagamento = B.cd_forma_pagamento) " +
				 "LEFT OUTER JOIN adm_cliente         C  ON (A.cd_pessoa          = C.cd_pessoa) " +
				 "LEFT OUTER JOIN adm_conta_receber   D  ON (A.cd_pessoa          = D.cd_pessoa " +
				 "									    AND A.cd_empresa         = D.cd_empresa " +
				 " 									    AND A.cd_forma_pagamento = D.cd_forma_pagamento) " +
//				 "LEFT OUTER JOIN adm_cliente_produto E ON (A.cd_pessoa          = E.cd_pessoa) "+
//				 "LEFT OUTER JOIN grl_produto_servico F ON (E.cd_produto_servico = F.cd_produto_servico)"+
				 (hasConta? "WHERE D.st_conta   = " + ContaReceberServices.ST_EM_ABERTO + 
						    "  AND AA.cd_pessoa  = " + cdCliente : "WHERE AA.cd_pessoa  = " + cdCliente)+
				 "  AND AA.cd_empresa = " + cdEmpresa +
				 "GROUP BY AA.cd_empresa, A.cd_forma_pagamento, B.nm_forma_pagamento, AA.cd_pessoa, " +
				 "         C.vl_limite_credito, A.vl_limite ";
			pstmt = connect.prepareStatement(sql);
			rsm = new ResultSetMap(pstmt.executeQuery());
			// Acrescenta o campo com o calculo do valor disponivel para compras em determinada forma de pagamento.
			double vlLimiteDisponivel = 0;
			while (rsm.next()){
				rsm.setValueToField("VL_PAGT_DISPONIVEL", (rsm.getDouble("vl_limite_pagamento") - rsm.getDouble("vl_limite_utilizado")));
				vlLimiteDisponivel += rsm.getDouble("vl_limite_utilizado");
			}
			//Calcula o limite disponivel geral para compras.
			rsm.setValueToField("VL_LIMITE_DISPONIVEL", (rsm.getDouble("vl_limite_credito") - vlLimiteDisponivel));
			pstmt.close();
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! ClientePagamentoServices.getStatusCliente: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static ResultSetMap getStatusProdutoCliente(int cdEmpresa, int cdCliente){
		return getStatusProdutoCliente(cdEmpresa, cdCliente, null);
	}
	public static ResultSetMap getStatusProdutoCliente(int cdEmpresa, int cdCliente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {			
			connect = isConnectionNull ? Conexao.conectar() : connect;
			String sql;
			// Acrescimo de combustiveis
			sql = "SELECT * FROM adm_cliente_produto A, grl_produto_servico B " +
				  "WHERE A.cd_produto_servico = B.cd_produto_servico " +
			      "  AND A.cd_empresa = " + cdEmpresa +
				  "  AND A.cd_pessoa  = " + cdCliente;
			PreparedStatement pstmt = connect.prepareStatement(sql);
			ResultSetMap rsmProduto = new ResultSetMap(pstmt.executeQuery());	
			pstmt.close();
			rsmProduto.beforeFirst();
			return rsmProduto;
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! ClientePagamentoServices.getStatusCliente: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo que faz um get na tabela de Condição de Pagamento, porém retorna o objeto dentro de um rsm
	 * @since 03/08/2014
	 * @author Gabriel
	 * @param cdCondicaoPagamento
	 * @return
	 */
	public static ResultSetMap get(int cdCondicaoPagamento) {
		return get(cdCondicaoPagamento, null);
	}

	public static ResultSetMap get(int cdCondicaoPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_condicao_pagamento WHERE cd_condicao_pagamento=?");
			pstmt.setInt(1, cdCondicaoPagamento);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			//Rotina usada para acrescentar o codigo da forma de pagamento dentro do get de condição de pagamento, não obtido naturalmente pela relação de 
			//condicao de pagamento ser nXn com forma_plano_pagamento
			if(rsm.next()){
				PreparedStatement pstmt2 = connect.prepareStatement("SELECT * FROM adm_condicao_forma_plano_pagamento WHERE cd_condicao_pagamento = " + rsm.getInt("cd_condicao_pagamento"));
				ResultSetMap rsm2 = new ResultSetMap(pstmt2.executeQuery());
				if(rsm2.next()){
					rsm.setValueToField("cd_forma_pagamento", rsm2.getInt("cd_forma_pagamento"));
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(SQLException sqlExpt) {
			Util.registerLog(sqlExpt);
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoServices.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoServices.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo para retornar um find da Condição de pagamento, trazendo algumas informações a mais do que o do DAO
	 * @since 03/08/2014
	 * @author Gabriel
	 * @param criterios
	 * @return
	 */
	public static ResultSetMap find() {
		return find(new ArrayList<ItemComparator>(), null);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		try{			
			ResultSetMap rsm = Search.find("SELECT * FROM adm_condicao_pagamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			while(rsm.next()){
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_condicao_pagamento", rsm.getString("cd_condicao_pagamento"), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmFormaPagamento = CondicaoFormaPlanoPagamentoDAO.find(criterios, connect);
				if(rsmFormaPagamento.next()){
					FormaPagamento formaPagamento = FormaPagamentoDAO.get(rsmFormaPagamento.getInt("cd_forma_pagamento"), connect);
					if(formaPagamento != null){
						rsm.setValueToField("CD_FORMA_PAGAMENTO", formaPagamento.getCdFormaPagamento());
						rsm.setValueToField("NM_FORMA_PAGAMENTO", formaPagamento.getNmFormaPagamento());
					}
					
				}
				rsm.setValueToField("CL_VALIDADE_CONDICAO", rsm.getDateFormat("dt_validade_condicao", "dd/MM/yyyy"));
				rsm.setValueToField("CL_VALIDADE_LIMITE",   rsm.getDateFormat("dt_validade_limite", "dd/MM/yyyy"));
				
			}
			rsm.beforeFirst();
	
			return rsm;
		}catch(Exception e){
			Util.registerLog(e);
			return null;
		}
	}
	
	/**
	 * Metodo que busca o registro de condição de pagamento padrão do sistema
	 * @since 15/08/2014
	 * @author Gabriel
	 * @return
	 */
	public static CondicaoPagamento getPadrao(){
		return getPadrao(null);
	}
	
	public static CondicaoPagamento getPadrao(Connection connection){
		boolean isConnectionNull = connection == null;
		try {
		
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
								
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("lg_padrao", "1", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = CondicaoPagamentoDAO.find(criterios, connection);
			while(rsm.next()){
				return CondicaoPagamentoDAO.get(rsm.getInt("cd_condicao_pagamento"), connection);
			}
			
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	/**
	 * Metodo que vai garantir que apenas um registro de condição de pagamento do sistema será a padrão
	 * @since 15/08/2014
	 * @author Gabriel
	 * @param cdCondicaoPagamento
	 */
	public static void setPadrao(int cdCondicaoPagamento){
		setPadrao(cdCondicaoPagamento, null);
	}
	
	public static void setPadrao(int cdCondicaoPagamento, Connection connection){
		boolean isConnectionNull = connection == null;
		try {
		
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
								
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("lg_padrao", "1", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = CondicaoPagamentoDAO.find(criterios, connection);
			while(rsm.next()){
				CondicaoPagamento condicaoPagamento = CondicaoPagamentoDAO.get(rsm.getInt("cd_condicao_pagamento"), connection);
				condicaoPagamento.setLgPadrao(0);
				if(CondicaoPagamentoDAO.update(condicaoPagamento, connection) < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
				}
			}
			
			CondicaoPagamento condicaoPagamento = CondicaoPagamentoDAO.get(cdCondicaoPagamento, connection);
			condicaoPagamento.setLgPadrao(1);
			if(CondicaoPagamentoDAO.update(condicaoPagamento, connection) < 0){
				if(isConnectionNull)
					Conexao.rollback(connection);
			}
			
			
			if(isConnectionNull)
				connection.commit();
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

}
