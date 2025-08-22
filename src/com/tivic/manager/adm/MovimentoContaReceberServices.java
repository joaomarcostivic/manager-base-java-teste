package com.tivic.manager.adm;

import java.sql.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.ComplianceManager;
import com.tivic.manager.util.Util;

import flex.messaging.io.ArrayCollection;
import sol.dao.Search;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.Result;

public class MovimentoContaReceberServices{

	public static Result save(MovimentoContaReceber movimentoContaReceber){
		return save(movimentoContaReceber, null);
	}

	public static Result save(MovimentoContaReceber movimentoContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(movimentoContaReceber==null)
				return new Result(-1, "Erro ao salvar. MovimentoContaReceber é nulo");

			int retorno;
			if( MovimentoContaReceberDAO.get( movimentoContaReceber.getCdConta() , movimentoContaReceber.getCdMovimentoConta(), movimentoContaReceber.getCdContaReceber()) == null ){
				retorno = MovimentoContaReceberDAO.insert(movimentoContaReceber, connect);
				movimentoContaReceber.setCdMovimentoConta(retorno);
			}
			else {
				retorno = MovimentoContaReceberDAO.update(movimentoContaReceber, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "MOVIMENTOCONTARECEBER", movimentoContaReceber);
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
	public static Result remove(int cdConta, int cdMovimentoConta, int cdContaReceber){
		return remove(cdConta, cdMovimentoConta, cdContaReceber, false, null);
	}
	public static Result remove(int cdConta, int cdMovimentoConta, int cdContaReceber, boolean cascade){
		return remove(cdConta, cdMovimentoConta, cdContaReceber, cascade, null);
	}
	public static Result remove(int cdConta, int cdMovimentoConta, int cdContaReceber, boolean cascade, Connection connect){
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
			//Verifica se há mais de um registro lançado para a mesma forma de pagamento ou recebimento
			//exenplo: Quando um cheque paga ou recebe duas ou mais contas			
			PreparedStatement pstmt = connect.prepareStatement("SELECT COUNT(A.cd_movimento_conta) AS qt_contas " +
															   "FROM adm_movimento_conta A "+
															   "JOIN adm_movimento_conta_receber B on ( A.cd_conta = B.cd_conta AND A.cd_movimento_conta = B.cd_movimento_conta ) " +
												 			   "  WHERE B.cd_conta_receber <> " +cdContaReceber+
												 			   "  AND A.cd_conta =" +cdConta+
												 			   "  AND A.cd_movimento_conta = " +cdMovimentoConta);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				if (rs.getInt("qt_contas") > 0){			
					Conexao.rollback(connect);
					return new Result(-2, "Este recebimento faz parte de uma movimentação em conjunto e não pode ser excluída isoladamente!");
				}
			}
			
			/**
			 * Verifica se existe movimentação com título de crédito e a excluí
			 */
			ResultSet rsMovTitulo = connect.prepareStatement("SELECT * FROM ADM_MOVIMENTO_TITULO_CREDITO  "
					+ "   WHERE cd_conta           = " + cdConta
					+ "   AND cd_movimento_conta = " + cdMovimentoConta).executeQuery();
			if( rsMovTitulo.next() ){
				int cdTituloCredito = rsMovTitulo.getInt("CD_TITULO_CREDITO");
				MovimentoContaTituloCreditoDAO.delete(cdTituloCredito, cdMovimentoConta, cdConta, connect);
				TituloCreditoDAO.delete(cdTituloCredito, connect);
			}
			
			/**
			 * Remove contas a receber geradas a partir deste movimento
			 * Ex.: Parcelas de recebimento de cartão de crédito, provenientes de um recebimento em n vezes 
			 */
			Result resOrigem = ContaMovimentoOrigemServices.removeAll(cdMovimentoConta, cdConta, connect);
			if( resOrigem.getCode() <= 0 ){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-12, "Erro ao remover conta a receber associadas");
			}
			
			/**
			 * Remove a movimentação da conta a receber, da conta financeira, arquivos, e suas classificações
			 */
			retorno = MovimentoContaReceberDAO.delete(cdConta, cdMovimentoConta, cdContaReceber, connect);
			connect.prepareStatement("DELETE FROM adm_movimento_conta_categoria "
								+ "   WHERE cd_conta           = " + cdConta
								+ "   AND cd_movimento_conta = " + cdMovimentoConta
								+ "   AND cd_conta_receber     = " + cdContaReceber).execute();
			MovimentoContaArquivoServices.removeAll(cdMovimentoConta, cdConta, connect);
			connect.prepareStatement("DELETE FROM adm_movimento_conta "
								+ "   WHERE cd_conta           = " + cdConta
								+ "   AND cd_movimento_conta = " + cdMovimentoConta).execute();
			
			/*
			 *  Atualiza a conta a receber
			 */
			int ret = setSituacaConta(cdContaReceber, null, connect);
			if(ret<=0)	{
				com.tivic.manager.util.Util.registerLog(new Exception("Erro ao tentar atualizar conta a receber!"));
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ret, "Erro ao atualizar situação da conta recebimento após exclui o recebimento!");
			}
			
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
	
	public static int insert(MovimentoContaReceber objeto) {
		return insert(objeto, null, false, false, null);
	}

	public static int insert(MovimentoContaReceber objeto, GregorianCalendar dtMovimento, Connection connect) {
		return insert(objeto, dtMovimento, false, false, connect);
	}

	public static sol.util.Result insert(int cdConta, int cdMovimentoConta, int cdContaReceber, float vlRecebido, float vlJuros, float vlMulta,
			                             float vlDesconto, boolean atualizaMovimentoConta)
	{
		MovimentoContaReceber objeto = new MovimentoContaReceber(cdConta,cdMovimentoConta,cdContaReceber,vlRecebido,vlJuros,vlMulta,vlDesconto,0,0,0);
		return new sol.util.Result(insert(objeto, null, false, atualizaMovimentoConta, null));
	}
	
	public static sol.util.Result insert(int cdConta, int cdMovimentoConta, int cdContaReceber, float vlRecebido, float vlJuros, float vlMulta,
            float vlAcrescimo, float vlDesconto, boolean atualizaMovimentoConta)
	{
		MovimentoContaReceber objeto = new MovimentoContaReceber(cdConta,cdMovimentoConta,cdContaReceber,vlRecebido,vlJuros,vlMulta,vlAcrescimo,vlDesconto,0,0,0);
		return new sol.util.Result(insert(objeto, null, false, atualizaMovimentoConta, null));
	}
	
	public static int insert(MovimentoContaReceber objeto, GregorianCalendar dtMovimento, boolean isEstorno, boolean atualizaMovimentoConta, Connection connect){
		boolean isConnectionNull = connect==null;
		try	{
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			// Verifica se a conta já foi recebida ou se está cancelada
			ContaReceber conta = ContaReceberDAO.get(objeto.getCdContaReceber(), connect);
			if(!isEstorno && conta.getStConta()!=ContaReceberServices.ST_EM_ABERTO && conta.getStConta()!=ContaReceberServices.ST_PRORROGADA)	{
				Exception exception = new Exception("Conta já paga ou cancelada!");
				exception.printStackTrace(System.out);
				com.tivic.manager.util.Util.registerLog(exception);
				return -10;
			}

			//Lança pagamento
			int retorno = MovimentoContaReceberDAO.insert(objeto, connect);
			
			//COMPLIANCE
			ComplianceManager.process(objeto, null, ComplianceManager.TP_ACAO_INSERT, connect);

			if(atualizaMovimentoConta)	{
				PreparedStatement pstmt = connect.prepareStatement(
						                         "SELECT SUM(vl_recebido) AS vl_recebido, " +
						                         "       SUM(vl_juros) AS vl_juros, " +
						                         "       SUM(vl_multa) AS vl_multa, " +
						                         "		 SUM(vl_acrescimo) AS vl_acrescimo, " +
						                         "       SUM(vl_desconto) AS vl_desconto " +
	                                             "FROM adm_movimento_conta_receber " +
							                     "WHERE cd_conta           = "+objeto.getCdConta()+
							                     "  AND cd_movimento_conta = "+objeto.getCdMovimentoConta());
				ResultSet rs = pstmt.executeQuery();
				float vlMovimento = 0;
				if(rs.next())
					vlMovimento = rs.getFloat("vl_recebido") + rs.getFloat("vl_acrescimo") - rs.getFloat("vl_desconto");

				MovimentoConta movimentoConta = MovimentoContaDAO.get(objeto.getCdMovimentoConta(), objeto.getCdConta(), connect);
				movimentoConta.setVlMovimento( Double.parseDouble( Float.toString(vlMovimento)));
				MovimentoContaDAO.update(movimentoConta, connect);
			}
			// tenta gravar nova situação da conta
			retorno = retorno>0 ? setSituacaConta(conta, dtMovimento, connect) : retorno;
			//
			if(isConnectionNull)
				if(retorno>0)
					connect.commit();
				else
					Conexao.rollback(connect);

			return retorno;
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connect);
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MovimentoContaReceber objeto) {
		return update(objeto, null);
	}

	public static int update(MovimentoContaReceber objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try	{
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			if (MovimentoContaReceberDAO.update(objeto, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (setSituacaConta(objeto.getCdContaReceber(), null, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaReceberServices.update: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int setSituacaConta(int cdContaReceber, GregorianCalendar dtSituacao, Connection connect)	{
		ContaReceber conta = ContaReceberDAO.get(cdContaReceber, connect);
		return setSituacaConta(conta, dtSituacao, connect);
	}

	public static int setSituacaConta(ContaReceber conta, GregorianCalendar dtSituacao, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try	{
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			PreparedStatement pstmt = connect.prepareStatement(
					   "SELECT SUM(vl_recebido) AS vl_recebido, SUM(vl_multa) AS vl_multa, " +
					   "       SUM(vl_juros) AS vl_juros, SUM(vl_desconto) AS vl_desconto, " +
					   "       MAX(B.dt_movimento) AS dt_movimento, MAX(B.dt_deposito) AS dt_deposito " +
			           "FROM adm_movimento_conta_receber A " +
			           "JOIN adm_movimento_conta B ON (A.cd_conta = B.cd_conta " +
			           "                           AND A.cd_movimento_conta = B.cd_movimento_conta) " +
			           "WHERE A.cd_conta_receber = "+conta.getCdContaReceber()+
			           "  AND B.st_movimento    <> "+MovimentoContaServices.ST_CANCELADO);
			ResultSet rs = pstmt.executeQuery();
			if(!rs.next())
				return -10;

			Double vlRecebido = rs.getDouble("vl_recebido") - rs.getDouble("vl_juros") - rs.getDouble("vl_multa") + rs.getDouble("vl_desconto");
			Double vlAReceber = (conta.getVlConta() + conta.getVlAcrescimo() - conta.getVlAbatimento() - vlRecebido);
			int stConta      = (vlAReceber >= 0.01) ? ContaReceberServices.ST_EM_ABERTO : ContaReceberServices.ST_RECEBIDA;
			Timestamp dtRecebimento = null;
			if(stConta == ContaReceberServices.ST_RECEBIDA)	{
				dtRecebimento = rs.getTimestamp("dt_movimento");
				if(dtRecebimento!=null && rs.getTimestamp("dt_deposito")!=null && rs.getTimestamp("dt_deposito").before(dtRecebimento))
					dtRecebimento = rs.getTimestamp("dt_deposito");

				ContaReceberServices.gerarParcelasOutraConta(conta, connect);
			}

			pstmt = connect.prepareStatement("UPDATE adm_conta_receber SET vl_recebido = ?,  st_conta = ?, dt_recebimento = ? " +
											 "WHERE cd_conta_receber = "+conta.getCdContaReceber());
			pstmt.setDouble(1, vlRecebido);
			pstmt.setInt(2, stConta);
			if(dtRecebimento!=null)
				pstmt.setTimestamp(3, dtRecebimento);
			else
				pstmt.setNull(3, Types.TIMESTAMP);

			int ret = pstmt.executeUpdate();
			if(ret > 0)	{
				ret = TituloCreditoServices.setSituacaoTitulosOfConta(conta.getCdContaReceber(), stConta, dtSituacao, connect);
				if(ret<=0 && isConnectionNull)
					Conexao.rollback(connect);
				else if (isConnectionNull)
					connect.commit();
			}
			else if (isConnectionNull)
				Conexao.rollback(connect);

			return ret;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getRecebimentoOfContaReceber(int cdContaReceber) {
		return getRecebimentoOfContaReceber(cdContaReceber, null);
	}

	public static ResultSetMap getRecebimentoOfContaReceber(int cdContaReceber, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			String sql = "SELECT B.*, A.*, C.nm_conta, C.nr_conta, C.nr_dv, C.tp_conta, E.nr_agencia, " +
					   "       F.nr_banco, F.nm_banco, D.nm_forma_pagamento, D.id_forma_pagamento, D.tp_forma_pagamento, " +
					   "       H.nm_pessoa AS nm_sacado, H.gn_pessoa AS gn_sacado, J.nr_cnpj AS nr_cnpj_sacado, I.nr_cpf AS nr_cpf_sacado, " +
					   "       M.nm_pessoa AS nm_fantasia, G.ds_historico " +
			           "FROM adm_movimento_conta_receber    A " +
			           "JOIN adm_movimento_conta            B ON (A.cd_conta = B.cd_conta " +
			           "                                      AND A.cd_movimento_conta = B.cd_movimento_conta) " +
			           "JOIN adm_conta_financeira           C ON (B.cd_conta = C.cd_conta) " +
			           "JOIN adm_conta_receber 				G ON (A.cd_conta_receber = G.cd_conta_receber) " +
			           "LEFT OUTER JOIN grl_pessoa			H ON (G.cd_pessoa = H.cd_pessoa) " +
			           "LEFT OUTER JOIN grl_pessoa_fisica	I ON (I.cd_pessoa = H.cd_pessoa) " +
			           "LEFT OUTER JOIN grl_pessoa_juridica J ON (J.cd_pessoa = H.cd_pessoa) " +
			           "LEFT OUTER JOIN adm_forma_pagamento D ON (B.cd_forma_pagamento = D.cd_forma_pagamento) "+
			           "LEFT OUTER JOIN grl_agencia         E ON (C.cd_agencia = E.cd_agencia) "+
			           "LEFT OUTER JOIN grl_banco           F ON (E.cd_banco = F.cd_banco) " +
			           "LEFT OUTER JOIN grl_empresa			L ON (G.cd_empresa = L.cd_empresa) " +
			           "LEFT OUTER JOIN grl_pessoa			M ON (L.cd_empresa = M.cd_pessoa)"+
			           "WHERE A.cd_conta_receber = "+cdContaReceber+
			           "  AND B.st_movimento <> ";
			if(lgBaseAntiga)
				sql = "SELECT C.*, E.nr_agencia, " +
						   "       F.nr_banco, F.nm_banco, " +
						   "       H.nm_pessoa AS nm_sacado, H.gn_pessoa AS gn_sacado, J.nr_cnpj AS nr_cnpj_sacado, I.nr_cpf AS nr_cpf_sacado, " +
						   "       M.nm_pessoa AS nm_fantasia, G.ds_historico " +
//				           "FROM adm_movimento_conta_receber    A " +
//				           "JOIN adm_movimento_conta            B ON (A.cd_conta = B.cd_conta " +
//				           "                                      AND A.cd_movimento_conta = B.cd_movimento_conta) " +
				           "FROM adm_conta_financeira           C " +
				           "JOIN adm_conta_receber 				G ON (C.cd_conta = G.cd_conta) " +
				           "LEFT OUTER JOIN grl_pessoa			H ON (G.cd_pessoa = H.cd_pessoa) " +
				           "LEFT OUTER JOIN grl_pessoa_fisica	I ON (I.cd_pessoa = H.cd_pessoa) " +
				           "LEFT OUTER JOIN grl_pessoa_juridica J ON (J.cd_pessoa = H.cd_pessoa) " +
//				           "LEFT OUTER JOIN adm_forma_pagamento D ON (B.cd_forma_pagamento = D.cd_forma_pagamento) "+
				           "LEFT OUTER JOIN grl_agencia         E ON (C.cd_agencia = E.cd_agencia) "+
				           "LEFT OUTER JOIN grl_banco           F ON (E.cd_banco = F.cd_banco) " +
				           "LEFT OUTER JOIN grl_empresa			L ON (G.cd_empresa = L.cd_empresa) " +
				           "LEFT OUTER JOIN grl_pessoa			M ON (L.cd_empresa = M.cd_pessoa)"+
				           "WHERE G.cd_conta_receber = "+cdContaReceber;
			PreparedStatement pstmt = connect.prepareStatement(sql+MovimentoContaServices.ST_CANCELADO);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaReceberServices.getRecebimentoOfContaReceber: " + e);
			com.tivic.manager.util.Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getRecebimentoOfMovimento(int cdConta, int cdMovimentoConta) {
		return getRecebimentoOfMovimento(cdConta, cdMovimentoConta, null);
	}

	public static ResultSetMap getRecebimentoOfMovimento(int cdConta, int cdMovimentoConta, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					   "SELECT B.*, A.*, C.nm_pessoa, D.sg_tipo_documento, D.nm_tipo_documento  " +
			           "FROM adm_movimento_conta_receber A " +
			           "JOIN adm_conta_receber     B ON (A.cd_conta_receber = B.cd_conta_receber) "+
			           "LEFT OUTER JOIN grl_pessoa C ON (B.cd_pessoa = C.cd_pessoa) " +
			           "LEFT OUTER JOIN adm_tipo_documento D ON (B.cd_tipo_documento = D.cd_tipo_documento) "+
			           "WHERE A.cd_conta = " +cdConta+
			           "  AND A.cd_movimento_conta = "+cdMovimentoConta);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		String ordenacao = "ASC";
		for (int i=0; criterios!=null && i<criterios.size(); i++){
			if (criterios.get(i).getColumn().equalsIgnoreCase("ordenacao")) {
				ordenacao = criterios.get(i).getValue();
				criterios.remove(i);
				if( ordenacao.equals("ASC") || !ordenacao.equals("DESC")  )
					ordenacao = "ASC";
				break;
			}
		}
		
		String sql = "SELECT A.*, B.dt_movimento, B.nr_documento, B.cd_forma_pagamento, B.ds_historico, "+
				"       B.tp_origem, C.nr_conta, C.nr_dv, C.nm_conta, C.tp_conta, "+
				"       D.dt_vencimento, D.vl_conta, D.vl_acrescimo, D.vl_abatimento, "+
				"       D.nr_documento as nr_doc_conta, D.nr_parcela, D.nr_referencia, D.cd_tipo_documento, D.qt_parcelas, D.cd_empresa, "+
				"       E.nm_pessoa, F.nm_forma_pagamento, F.sg_forma_pagamento, F.tp_forma_pagamento, G.nm_tipo_documento, G.sg_tipo_documento  "+
				"FROM adm_movimento_conta_receber A "+
				"JOIN adm_movimento_conta   B ON (A.cd_conta = B.cd_conta "+
				"                             AND A.cd_movimento_conta = B.cd_movimento_conta) "+
				"JOIN adm_conta_financeira  C ON (A.cd_conta = C.cd_conta) "+
				"JOIN adm_conta_receber     D ON (A.cd_conta_receber = D.cd_conta_receber) "+
				"LEFT OUTER JOIN grl_pessoa E ON (D.cd_pessoa = E.cd_pessoa)"+
				"LEFT OUTER JOIN adm_forma_pagamento F ON (B.cd_forma_pagamento = F.cd_forma_pagamento)" +
				"LEFT OUTER JOIN adm_tipo_documento  G ON (D.cd_tipo_documento  = G.cd_tipo_documento) ";
		return Search.find(sql, " ORDER BY B.DT_MOVIMENTO "+ordenacao, criterios, Conexao.conectar(), true, false);
		
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, ArrayList<String> groupBy) {
		return findCompleto(criterios, groupBy, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, ArrayList<String> groupBy, Connection connect) {
		boolean isConnNull = connect==null;
		try	{
			if(isConnNull)
				connect = Conexao.conectar();
			boolean lgCategoria = false, lgConferencia = false;
			String ordenacao = "ASC";
			for(int i=0; i<criterios.size() && groupBy.size()==0;){
				if(criterios.get(i).getColumn().equalsIgnoreCase("lgcategoria"))	{
					criterios.remove(i);
					lgCategoria = true;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("lgconferencia"))	{
					criterios.remove(i);
					lgConferencia = true;
				}else if(criterios.get(i).getColumn().equalsIgnoreCase("ordenacao"))	{
					ordenacao = criterios.get(i).getValue();
					criterios.remove(i);
					if( ordenacao.equals("ASC") || !ordenacao.equals("DESC")  )
						ordenacao = "ASC";
				}
				else
					i++;
			}
			String nmPessoa = "";
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("E.NM_PESSOA")) {
					nmPessoa =	Util.limparTexto(criterios.get(i).getValue());
					nmPessoa = nmPessoa.trim();
					criterios.remove(i);
					i--;
				}
			}
			String groups = "dt_movimento";
			String fields = " A.*, B.dt_movimento, B.nr_documento, B.cd_forma_pagamento, B.vl_movimento, "+
						 	" B.tp_origem, B.st_movimento, B.tp_movimento, C.nr_conta, C.nr_dv, C.nm_conta, C.tp_conta, "+
						 	" D.dt_vencimento, D.vl_conta, D.vl_acrescimo, D.vl_abatimento, "+
						 	" D.nr_documento as nr_doc_conta, D.nr_parcela, D.nr_referencia, D.cd_tipo_documento, D.qt_parcelas, D.cd_empresa, "+
						 	" E.nm_pessoa, F.nm_forma_pagamento, F.sg_forma_pagamento, F.tp_forma_pagamento, G.nm_tipo_documento, G.sg_tipo_documento," +
						 	" J.nm_pessoa AS nm_fantasia, L.nm_razao_social AS nm_empresa, I.nm_login ";
			// Processa agrupamentos enviados em groupBy
			String [] retorno = com.tivic.manager.util.Util.getFieldsAndGroupBy(groupBy, fields, groups,"SUM(A.VL_RECEBIDO) AS VL_RECEBIDO, COUNT(*) AS QT_RECEBIMENTO");
			fields = retorno[0];
			groups = retorno[1];
			String orderBy = groups.toLowerCase().indexOf("dt_movimento")>=0 ? "ORDER BY B.DT_MOVIMENTO "+ordenacao : "";
			String sql = 	"SELECT "+fields+
							"FROM adm_movimento_conta_receber A "+
							"JOIN adm_movimento_conta   B ON (A.cd_conta = B.cd_conta "+
							"                             AND A.cd_movimento_conta = B.cd_movimento_conta) "+
							"JOIN adm_conta_financeira  C ON (A.cd_conta = C.cd_conta) "+
							"JOIN adm_conta_receber     D ON (A.cd_conta_receber = D.cd_conta_receber) "+
							"LEFT OUTER JOIN grl_pessoa E ON (D.cd_pessoa = E.cd_pessoa)"+
							"LEFT OUTER JOIN adm_forma_pagamento F ON (B.cd_forma_pagamento = F.cd_forma_pagamento)" +
							"LEFT OUTER JOIN adm_tipo_documento  G ON (D.cd_tipo_documento  = G.cd_tipo_documento) " +
							"JOIN grl_empresa                    H ON (C.cd_empresa = H.cd_empresa) " +
							"LEFT OUTER JOIN seg_usuario         I ON (I.cd_usuario = B.cd_usuario) " +
							"JOIN grl_pessoa                     J ON (C.cd_empresa = J.cd_pessoa) " +
							"LEFT OUTER JOIN grl_pessoa_juridica L ON (C.cd_empresa = L.cd_pessoa) "+
	        				"LEFT OUTER JOIN grl_agencia         M ON (C.cd_agencia = M.cd_agencia) "+
	        				"LEFT OUTER JOIN grl_banco           N ON (M.cd_banco = N.cd_banco) "+
	        				" WHERE 1=1 "+
	    					(!nmPessoa.equals("") ?
	    					"AND TRANSLATE (E.nm_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', " + 
	    					"				'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmPessoa)+"%' "
	    					: "");
			ResultSetMap rsm = Search.find(sql, groups+" "+orderBy, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		
			PreparedStatement pstmt = connect.prepareStatement(
					   "SELECT A.*, B.* " +
			           "FROM adm_movimento_conta_categoria A " +
			           "JOIN adm_categoria_economica B ON (A.cd_categoria_economica = B.cd_categoria_economica) "+
			           "WHERE A.cd_conta           = ? " +
			           "  AND A.cd_movimento_conta = ? " +
			           "  AND EXISTS (SELECT * FROM adm_conta_receber_categoria CPC " +
			           "         	  WHERE CPC.cd_conta_receber       = ? " +
			           "            	AND CPC.cd_categoria_economica = A.cd_categoria_economica)");
			//Verifica irregularidade no lançamento da classificação
			while(lgCategoria && rsm.next())	{
				pstmt.setInt(1, rsm.getInt("cd_conta"));
				pstmt.setInt(2, rsm.getInt("cd_movimento_conta"));
				pstmt.setInt(3, rsm.getInt("cd_conta_receber"));
				ResultSetMap rsmCategoriasConta = new ResultSetMap(pstmt.executeQuery());
				String dsCategoria = "", dsAlerta = "";
				// Verifica a classificação total do movimento
				float vlTotalCategoria = 0;
				ResultSetMap rsmCategoriasMov = MovimentoContaCategoriaServices.getCategoriaOfMovimento(rsm.getInt("cd_conta"), rsm.getInt("cd_movimento_conta"), connect);
				while(rsmCategoriasMov.next()){
					int tpMovimento = rsmCategoriasMov.getInt("tp_movimento");
					int tpCategoria = rsmCategoriasMov.getInt("tp_categoria_economica");
					if(tpMovimento == MovimentoContaCategoriaServices.TP_DESCONTO || tpCategoria == CategoriaEconomicaServices.TP_DESPESA)
						vlTotalCategoria -= rsmCategoriasMov.getFloat("vl_movimento_categoria");
					else
						vlTotalCategoria += rsmCategoriasMov.getFloat("vl_movimento_categoria");
				}
				
				if(Math.abs(vlTotalCategoria - rsm.getFloat("vl_movimento")) > 0.01){
					dsAlerta = (dsAlerta.equals("") ? "" : ", ")+"Diferença no movimento: Movimento: "+Util.formatNumber(rsm.getFloat("vl_movimento"))+", Categorias: "+Util.formatNumber(vlTotalCategoria);
				}
				// Verifica a classificação do recebimento
				// Se não tiver a classificação verifica se foi porque a classificação da conta tá diferente
				if(rsmCategoriasConta.size()==0)	{
					ResultSetMap rsmContas = getRecebimentoOfMovimento(rsm.getInt("cd_conta"), rsm.getInt("cd_movimento_conta"), connect);
					if(rsmCategoriasMov.size()>0)
						dsAlerta = (dsAlerta.equals("") ? "" : ", ")+"Classificação da conta diferente do recebimento";
					// Se houver classificação e também o movimento tiver pago mais de uma conta, a classificação encontrada pode ser da outra conta
					if(rsmContas.size()==1)
						rsmCategoriasConta = rsmCategoriasMov;
				}
				float vlAClassificar = rsm.getFloat("vl_recebido")-rsm.getFloat("vl_multa")-rsm.getFloat("vl_juros")+rsm.getFloat("vl_desconto");
				rsmCategoriasConta.beforeFirst();
				while(rsmCategoriasConta.next()){
					dsCategoria    += (dsCategoria.equals("") ? "" : ", ")+rsmCategoriasConta.getString("nr_categoria_economica")+"-"+
					                                                       rsmCategoriasConta.getString("nm_categoria_economica");
					vlAClassificar -= rsmCategoriasConta.getFloat("vl_movimento_categoria");
					// Verificando se alguma categoria de despesa foi usada
					if(lgConferencia)	{
						int tpCategoria = rsmCategoriasConta.getInt("tp_categoria_economica");
						if((tpCategoria!=CategoriaEconomicaServices.TP_RECEITA && tpCategoria!=CategoriaEconomicaServices.TP_DEDUCAO_DESPESA) &&
							rsmCategoriasConta.getFloat("vl_movimento_categoria")>rsm.getFloat("vl_abatimento"))
							dsAlerta += (dsAlerta.equals("") ? "" : ", ")+"Categoria de despesa";
					}
				}
				if(lgConferencia && vlAClassificar>0.009)
					dsAlerta += (dsAlerta.equals("") ? "" : ", ")+"Diferença na classificacao: "+Util.formatNumber(vlAClassificar);
				if(dsAlerta.equals(""))
					dsAlerta = "OK";
				rsm.setValueToField("ds_categoria_economica", dsCategoria);
				rsm.setValueToField("ds_alerta", dsAlerta);
			}
			if( rsm != null )
				rsm.beforeFirst();
			return rsm;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result delete(int cdConta, int cdMovimentoConta, int cdContaReceber, int cdUsuario) {
		return delete(cdConta, cdMovimentoConta, cdContaReceber, cdUsuario, true, null);
	}

	public static Result delete(int cdConta, int cdMovimentoConta, int cdContaReceber, int cdUsuario, boolean atualizaMovimentoConta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			// TODO: Verificar fechamento de caixa
			/*
			 *  Verifica se o lançamento já foi conferido/concilicado
			 *
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM adm_movimento_conta " +
					                                           "WHERE cd_conta           = "+cdConta+
					                                           "  AND cd_movimento_conta = "+cdMovimentoConta);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next() && (rs.getInt("st_movimento")==MovimentoContaServices.ST_CX_FECHADO))		{
				com.tivic.manager.util.Util.registerLog(new Exception("Movimento da conta já conferido/conciliado!"));
				return new Result(-10, "O recebimento ja faz parte de um caixa fechado, operação não permitida!");
			}
			*/
			connect.prepareStatement("DELETE FROM adm_movimento_conta_categoria " +
									 "WHERE cd_conta           = " +cdConta+
									 "  AND cd_movimento_conta = " +cdMovimentoConta+
									 "  AND cd_conta_receber   = "+cdContaReceber).execute();
			/*
			 *  Exclui o recebimento
			 */
			int ret = MovimentoContaReceberDAO.delete(cdConta, cdMovimentoConta, cdContaReceber, connect);
			if(ret<=0)	{
				com.tivic.manager.util.Util.registerLog(new Exception("Erro ao tentar excluir recebimento!"));
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ret, "Erro ao tentar excluir o registro do recebimento!");
			}
			/*
			 *  Dependendo do parametro passado atualiza ou não o movimento da conta
			 */
			if(atualizaMovimentoConta)	{
				PreparedStatement pstmt = connect.prepareStatement("SELECT SUM(vl_recebido) AS vl_recebido, " +
											                       "       SUM(vl_juros) AS vl_juros, " +
											                       "       SUM(vl_multa) AS vl_multa, " +
											                       "       SUM(vl_desconto) AS vl_desconto " +
						                                           "FROM adm_movimento_conta_receber " +
												                   "WHERE cd_conta           = "+cdConta+
												                   "  AND cd_movimento_conta = "+cdMovimentoConta);
				ResultSet rs = pstmt.executeQuery();
				float vlMovimento = 0;
				Result result = new Result(1);
				if(rs.next())
					vlMovimento = rs.getFloat("vl_recebido") - rs.getFloat("vl_desconto");

				if(vlMovimento > 0)		{
					MovimentoConta movimentoConta = MovimentoContaDAO.get(cdMovimentoConta, cdConta, connect);
					movimentoConta.setVlMovimento(Double.parseDouble( Float.toString(vlMovimento)));
					result = new Result(MovimentoContaDAO.update(movimentoConta, connect));
					if(result.getCode() <= 0)
						result.setMessage("Falha ao atualizar movimento da conta após excluir recebimento!");
				}
				else {
					pstmt = connect.prepareStatement("SELECT * FROM adm_movimento_conta_receber " +
													 "WHERE cd_conta           = "+cdConta+
													 "  AND cd_movimento_conta = "+cdMovimentoConta+
													 "  AND cd_conta_receber  <> "+cdContaReceber);
					result = MovimentoContaServices.delete(cdConta, cdMovimentoConta, cdUsuario, !pstmt.executeQuery().next() /*Não atualiza movimento*/, connect);
				}

				if(result.getCode()<=0)	{
					Util.printInFile("/log.log", "Erro ao tentar excluir/atualizar movimento de conta!");
					com.tivic.manager.util.Util.registerLog(new Exception("Erro ao tentar excluir/atualizar movimento de conta!"));
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
			}
			/*
			 *  Atualiza a conta a receber
			 */
			ret = setSituacaConta(cdContaReceber, null, connect);
			if(ret<=0)	{
				com.tivic.manager.util.Util.registerLog(new Exception("Erro ao tentar atualizar conta a receber!"));
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ret, "Erro ao atualizar situação da conta recebimento após exclui o recebimento!");
			}
			/*
			 *  Se a conexão não foi passada por outro método chamado, chama o commit
			 */
			if (isConnectionNull)
				connect.commit();

			return new Result(ret);
		}
		catch(Exception e)	{
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar excluir o recebimento de uma conta!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result estornar(int cdConta, int cdMovimentoConta, int cdContaReceber, int cdUsuario) {
		return estornar(cdConta, cdMovimentoConta, cdContaReceber, cdUsuario, null);
	}

	public static Result estornar(int cdConta, int cdMovimentoConta, int cdContaReceber, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			/*
			 *  Busca informações do recebimento
			 */
			MovimentoContaReceber recebimento = MovimentoContaReceberDAO.get(cdConta, cdMovimentoConta, cdContaReceber, connect);
			recebimento.setCdMovimentoConta(0);
			recebimento.setVlRecebido(-1 * recebimento.getVlRecebido());
			recebimento.setCdArquivo(0);
			recebimento.setCdRegistro(0);
			recebimento.setVlDesconto(0.0);
			recebimento.setVlMulta(0.0);
			recebimento.setVlJuros(0.0);
			recebimento.setVlTarifaCobranca(0.0);

			ArrayList<MovimentoContaReceber> recebimentos = new ArrayList<MovimentoContaReceber>();
			recebimentos.add(recebimento);

			ContaReceber contaReceber = ContaReceberDAO.get(cdContaReceber, connect);
			
			/*
			 *  Cria movimento de conta
			 */
			MovimentoConta movimentoConta = MovimentoContaDAO.get(cdMovimentoConta, cdConta, connect);
			movimentoConta.setCdContaOrigem(cdConta);
			movimentoConta.setCdUsuario(cdUsuario);
			movimentoConta.setDtMovimento(new GregorianCalendar());
			movimentoConta.setVlMovimento(Math.abs(recebimento.getVlRecebido()));
			movimentoConta.setTpMovimento(MovimentoContaServices.DEBITO);
			movimentoConta.setDsHistorico("Estorno de Recebimento");
			movimentoConta.setStMovimento(MovimentoContaServices.ST_CONFERIDO);
			movimentoConta.setIdExtrato(null);
			movimentoConta.setTpOrigem(MovimentoContaServices.toESTORNO);

			/*
			 *  Categorias economicas
			 */
			ArrayList<MovimentoContaCategoria> movimentoCategorias = new ArrayList<MovimentoContaCategoria>();
			ResultSet rs = connect.prepareStatement("SELECT * FROM adm_movimento_conta_categoria " +
					                                "WHERE cd_conta = "+cdConta+
					                                "  AND cd_movimento_conta = "+cdMovimentoConta).executeQuery();
			
			int cdCategoriaReceitaEstorno = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_RECEITA_ESTORNO", 0, contaReceber.getCdEmpresa());
			int cdCategoriaDespesaEstorno = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_DESPESA_ESTORNO", 0, contaReceber.getCdEmpresa());
			
			while(rs.next())	{
				
				CategoriaEconomica catEcon = CategoriaEconomicaDAO.get(rs.getInt("cd_categoria_economica"), connect);
				int cdCategoriaMov = 0;
				if(catEcon.getTpCategoriaEconomica() == CategoriaEconomicaServices.TP_RECEITA || catEcon.getTpCategoriaEconomica() == CategoriaEconomicaServices.TP_DEDUCAO_DESPESA)
					cdCategoriaMov = cdCategoriaReceitaEstorno;
				else
					cdCategoriaMov = cdCategoriaDespesaEstorno;
				
				movimentoCategorias.add(new MovimentoContaCategoria(cdConta,
						0,
						cdCategoriaMov,
						rs.getFloat("vl_movimento_categoria"),
						0 /*cdMovimentoContaCategoria*/,
						0 /*cdContaPagar*/,
						cdContaReceber,
						MovimentoContaCategoriaServices.TP_PRE_CLASSIFICACAO /*tpMovimento*/, 
						0/*cdCentroCusto*/));
			}
			Result result = MovimentoContaServices.insert(movimentoConta, recebimentos, movimentoCategorias, false, connect);
			if(result.getCode() <= 0)	{
				result.setMessage("Falha ao tentar lancar registro de estorno! \n"+result.getMessage());
				com.tivic.manager.util.Util.registerLog(new Exception(result.getMessage()));
				if(isConnectionNull)
					Conexao.rollback(connect);
				return result;
			}
			/*
			 *  Atualiza a conta a receber
			 */
			int ret = setSituacaConta(cdContaReceber, null, connect);
			if(ret<=0)	{
				com.tivic.manager.util.Util.registerLog(new Exception("Erro ao tentar atualizar conta a receber!"));
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ret, "Erro ao tentar atualizar conta a receber!");
			}

			/*;
			 *  Se a conexão não foi passada por outro método chamado, chama o commit
			 */
			if (isConnectionNull)
				connect.commit();
			
			result.setMessage("Extorno realizado com sucesso.");
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar lancar estorno!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getExtratoOfMovimento(int cdConta, int cdMovimentoConta, String dtInicial, String dtFinal) {
		return getExtratoOfMovimento(cdConta, cdMovimentoConta, dtInicial, dtFinal, null);
	}

	public static ResultSetMap getExtratoOfMovimento(int cdConta, int cdMovimentoConta, String dtInicial, String dtFinal, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSetMap rsm  = null;
		try {
			pstmt = connect.prepareStatement(
					   "SELECT B.*, A.*, " +
					   "C.nm_pessoa,  " +
					   "D.sg_tipo_documento, D.nm_tipo_documento, " +
					   "E.nm_pessoa AS nm_emitente, E.nr_telefone1 AS nr_telefone, E.nm_email," +
					   "F.nr_inscricao_estadual, F.nr_cnpj, " +
					   "G.nm_logradouro, G.nm_bairro, " +
					   "H.nm_cidade, H.nr_cep " + 
			           "FROM adm_movimento_conta_receber A " +
			           "JOIN adm_conta_receber     B ON (A.cd_conta_receber = B.cd_conta_receber) "+
			           "LEFT OUTER JOIN grl_pessoa C ON (B.cd_pessoa = C.cd_pessoa) " +
			           "LEFT OUTER JOIN adm_tipo_documento D ON (B.cd_tipo_documento = D.cd_tipo_documento) "+
			           "LEFT OUTER JOIN grl_pessoa E ON (E.cd_pessoa = B.cd_empresa) " +
			           "LEFT OUTER JOIN grl_pessoa_juridica F ON (F.cd_pessoa = B.cd_empresa) " +
			           "LEFT OUTER JOIN grl_pessoa_endereco G ON (G.cd_pessoa = B.cd_empresa) " +
			           "LEFT OUTER JOIN grl_cidade H ON (H.cd_cidade = G.cd_cidade) " +
			           "WHERE A.cd_conta = " +cdConta+
			           "  AND A.cd_movimento_conta = "+cdMovimentoConta);
			
			rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()){
				rsm.setValueToField("DT_INICIAL", dtInicial);
				rsm.setValueToField("DT_FINAL", dtFinal);
				rsm.setValueToField("CL_VL_CONTA", NumberFormat.getCurrencyInstance().format(rsm.getFloat("vl_conta")).toString());
				rsm.setValueToField("CL_VL_MULTA", NumberFormat.getCurrencyInstance().format(rsm.getFloat("vl_multa")).toString());
				rsm.setValueToField("CL_VL_JUROS", NumberFormat.getCurrencyInstance().format(rsm.getFloat("vl_juros")).toString());
				rsm.setValueToField("CL_VL_DESCONTO", NumberFormat.getCurrencyInstance().format(rsm.getFloat("vl_desconto")).toString());
				rsm.setValueToField("CL_VL_RECEBIDO", NumberFormat.getCurrencyInstance().format(rsm.getFloat("vl_recebido")).toString());
			}
			
			
			rsm.beforeFirst();
			
			return rsm;
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getProdutosOfMovimento(int cdConta, int cdMovimentoConta) {
		return getProdutosOfMovimento(cdConta, cdMovimentoConta, null);
	}
	
	public static ResultSetMap getProdutosOfMovimento(int cdConta, int cdMovimentoConta, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSetMap rsm  = null;
		try {
			pstmt = connect.prepareStatement(
					   		   "SELECT A.*, B.vl_unitario AS vl_custo, B.qt_saida AS vl_quantidade, (B.qt_saida * B.vl_unitario) AS vl_total " + 
					           "FROM adm_movimento_conta_receber E "+
					           "JOIN adm_conta_receber           D ON (E.cd_conta_receber = D.cd_conta_receber) "+
					           "JOIN alm_documento_saida_item    B ON (B.cd_documento_saida    = D.cd_documento_saida) "+
					           "JOIN grl_produto_servico         A ON (A.cd_produto_servico    = B.cd_produto_servico) "+
					           "WHERE E.cd_movimento_conta = " +cdMovimentoConta+
					           " AND E.cd_conta = " + cdConta);
			
			rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()){
				rsm.setValueToField("CL_VL_CUSTO", NumberFormat.getCurrencyInstance().format(rsm.getFloat("vl_custo")).toString());
				rsm.setValueToField("CL_VL_QUANTIDADE", NumberFormat.getInstance().format(rsm.getFloat("vl_quantidade")).toString());
				rsm.setValueToField("CL_VL_TOTAL", NumberFormat.getCurrencyInstance().format(rsm.getFloat("vl_total")).toString());
			}
			
			
			rsm.beforeFirst();
			
			return rsm;
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getTotalRecebimentosMensais( ArrayList<ItemComparator> criterios ) {
		return getTotalRecebimentosMensais(criterios, null);
	}
	
	public static ResultSetMap getTotalRecebimentosMensais( ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			ResultSetMap rsm = Search.find(
					" SELECT SUM( vl_movimento ) as vl_movimento, cd_empresa, 	"+
					" CAST( year AS INTEGER), CAST( month AS INTEGER) 			"+
					" FROM (  													"+
					"	SELECT  vl_movimento, cd_empresa,  						"+
					"	EXTRACT( month FROM dt_movimento) as month, 	 		"+
					"	EXTRACT( year FROM dt_movimento) as year    			"+
					"	FROM adm_movimento_conta_receber A						"+
					"	JOIN adm_movimento_conta B ON ( A.CD_CONTA = B.CD_CONTA AND A.CD_MOVIMENTO_CONTA = B.CD_MOVIMENTO_CONTA ) "+
					"	JOIN adm_conta_financeira C ON ( A.CD_CONTA = C.CD_CONTA )							"+
					"	WHERE dt_movimento IS NOT NULL						"+
					"		AND ST_MOVIMENTO IN ( "+MovimentoContaServices.ST_COMPENSADO+","+MovimentoContaServices.ST_CONCILIADO+" ) "+
					" ) AS tmp      "+
					" WHERE 1=1     ",
					" GROUP BY tmp.month,  tmp.cd_empresa, tmp.year "+
					" ORDER BY cd_empresa, year, month ASC",
					criterios, connect, isConnectionNull);
			return rsm;
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findGroupByFormaPagamento(ArrayList<ItemComparator> criterios) {
		return findGroupByFormaPagamento(criterios, null);
	}
	public static ResultSetMap findGroupByFormaPagamento(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnNull = connect == null;
		try {
			if (isConnNull)
				connect = Conexao.conectar();
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			String ordenacao = "ASC";
			Boolean hasCriterioEmpresa = false;
			
			ResultSetMap rsm = new ResultSetMap();
			ResultSetMap rsmMovimentacoes = null;
			
			String sql =
				 " SELECT D.CD_FORMA_PAGAMENTO, D.NM_FORMA_PAGAMENTO, COUNT(B.CD_MOVIMENTO_CONTA) as QT_MOVIMENTOS, "+
				 "	   SUM( VL_MOVIMENTO  ) AS VL_MOVIMENTOS, C.TP_CONTA, B.ST_MOVIMENTO	   "+
				 " FROM adm_movimento_conta_receber     A "+
				 " JOIN adm_movimento_conta             B ON ( A.cd_conta = B.cd_conta  AND A.cd_movimento_conta = B.cd_movimento_conta) "+ 
				 " JOIN adm_conta_financeira            C ON ( A.cd_conta = C.cd_conta ) "+
				 " JOIN adm_forma_pagamento             D ON ( B.cd_forma_pagamento = D.cd_forma_pagamento ) ";
			
			String groupBy = " GROUP BY D.CD_FORMA_PAGAMENTO, D.NM_FORMA_PAGAMENTO, B.CD_FORMA_PAGAMENTO, C.TP_CONTA, B.ST_MOVIMENTO "+
							 " ORDER BY D.CD_FORMA_PAGAMENTO, NM_FORMA_PAGAMENTO ";
			rsmMovimentacoes = Search.find(sql, groupBy, criterios, connect, connect==null);
			
			if (rsmMovimentacoes != null && rsmMovimentacoes.size() > 0){
				int cdFormaPagamentoAtual = 0;
				rsmMovimentacoes.beforeFirst();
				while( rsmMovimentacoes.next() ){
					if( cdFormaPagamentoAtual !=  rsmMovimentacoes.getInt("CD_FORMA_PAGAMENTO") ){
						HashMap<String, Object> register = new HashMap<String, Object>();
						register.put("NM_FORMA_PAGAMENTO", rsmMovimentacoes.getString("NM_FORMA_PAGAMENTO") );
						register.put("CD_FORMA_PAGAMENTO", rsmMovimentacoes.getInt("CD_FORMA_PAGAMENTO") );
						register.put("QT_MOVIMENTOS", 0 );
						register.put("VL_MOVIMENTOS", 0.0 );
						register.put("MOVIMENTOS", new ArrayList< HashMap<String, Object>>() );
						rsm.addRegister(register);
						rsm.next();
						cdFormaPagamentoAtual = rsmMovimentacoes.getInt("CD_FORMA_PAGAMENTO"); 
					}
					
					((ArrayList< HashMap<String, Object>>) rsm.getObject("MOVIMENTOS")).add( rsmMovimentacoes.getRegister() );
					rsm.setValueToField("QT_MOVIMENTOS", rsm.getInt("QT_MOVIMENTOS")+rsmMovimentacoes.getInt("QT_MOVIMENTOS")  );
					rsm.setValueToField("VL_MOVIMENTOS", rsm.getDouble("VL_MOVIMENTOS")+rsmMovimentacoes.getDouble("VL_MOVIMENTOS")  );
				}
			}
			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result gerarRelatorioRecebimentos ( ArrayList<ItemComparator> criterios ){
		return gerarRelatorioRecebimentos(criterios, null);
	}
	
	public static Result gerarRelatorioRecebimentos (ArrayList<ItemComparator> criterios, Connection connect){
		boolean isConnectionNull = connect==null;
		try	{
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			ResultSetMap rsm = new ResultSetMap();
			
			rsm = find(criterios);
			
			while(rsm.next()){
				rsm.setValueToField("VL_CONTA", rsm.getString("VL_CONTA"));				
				rsm.setValueToField("VL_JUROS", rsm.getString("VL_JUROS"));
				rsm.setValueToField("VL_DESCONTO", rsm.getString("VL_DESCONTO"));
				rsm.setValueToField("VL_MULTA", rsm.getString("VL_MULTA"));
				rsm.setValueToField("VL_RECEBIDO", rsm.getString("VL_RECEBIDO"));
			}
			
			rsm.beforeFirst();
			
			HashMap<String, Object> param = new HashMap<String, Object>();
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			
			return result;
			
		} catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getLogCompliance(int cdMovimento, int cdConta, boolean lgDelete) {
		return getLogCompliance(cdMovimento, cdConta, lgDelete, null);
	}
	
	public static ResultSetMap getLogCompliance(int cdMovimento, int cdConta, boolean lgDelete, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			ResultSetMap rsm = ComplianceManager
					.search(" SELECT * FROM adm_movimento_conta_receber "
						+ " WHERE 1=1"
						+ (lgDelete ? 
						  " AND tp_acao_compliance="+ComplianceManager.TP_ACAO_DELETE	
						  :
						  " AND cd_movimento_conta="+cdMovimento+" AND cd_conta_receber="+cdConta)
						+ " ORDER BY dt_compliance DESC ");
			
			while(rsm.next()) {
				if(rsm.getPointer()==0 && !lgDelete)
					rsm.setValueToField("tp_versao_compliance", "ATUAL");
				else
					rsm.setValueToField("tp_versao_compliance", "ANTIGA");
				
				rsm.setValueToField("nm_tp_acao", ComplianceManager.tipoAcao[rsm.getInt("tp_acao_compliance")].toUpperCase());
				
				if(rsm.getInt("cd_usuario_compliance", 0) > 0) {
					Usuario usuario = UsuarioDAO.get(rsm.getInt("cd_usuario_compliance"), connect);
					Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa(), connect);
					rsm.setValueToField("nm_usuario_compliance", pessoa.getNmPessoa());
				}
				
				if(lgDelete) {
					ResultSetMap rsmDetalhes = new ResultSetMap();
					HashMap<String, Object> regAtual = (HashMap<String, Object>)rsm.getRegister().clone();
					regAtual.put("TP_ITEM_COMPLIANCE", " ");
					rsmDetalhes.addRegister(regAtual);
					rsm.setValueToField("RSM_DETALHE", rsmDetalhes);
					
				}
			}
			rsm.beforeFirst();

			
			if(!lgDelete) {
				while(rsm.next()) {
					ResultSetMap rsmDetalhes = new ResultSetMap();
					HashMap<String, Object> regAtual = (HashMap<String, Object>)rsm.getRegister().clone();
					regAtual.put("TP_ITEM_COMPLIANCE", "PARA");
					rsmDetalhes.addRegister(regAtual);
					
					if(rsm.next()) { //como a ordem é decrescente, o próximo registro representa a versão anterior
						HashMap<String, Object> regAnterior = (HashMap<String, Object>)rsm.getRegister().clone();
						regAnterior.put("TP_ITEM_COMPLIANCE", "DE");
						rsmDetalhes.addRegister(regAnterior);
						rsm.previous();
					}
					
					ArrayList<String> fields = new ArrayList<>();
					fields.add("TP_ITEM_COMPLIANCE");
					rsmDetalhes.orderBy(fields);
					rsm.setValueToField("RSM_DETALHE", rsmDetalhes);
					
				}
				rsm.beforeFirst();
			}
			
			return rsm;
		}
		catch (Exception e) {
			System.out.println("Erro ao buscar log: "+e.getMessage());
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
