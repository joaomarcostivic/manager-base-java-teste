package com.tivic.manager.adm;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.EmpresaServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.ComplianceManager;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class MovimentoContaServices {
	/*
	 * Constantes para o movimento de contas bancárias
	 */
	public static final int ST_NAO_COMPENSADO = 0;
	public static final int ST_COMPENSADO     = 1;
	public static final int ST_CONCILIADO     = 2;
	public static final int ST_CANCELADO      = 3; // Serve também para contas do
	// tipo caixa
	public static final int ST_LIQUIDADO      = 4; // Serve também para contas do
	// tipo caixa
	/*
	 * Constantes para o movimento de contas caixa
	 */
	public static final int ST_NAO_CONFERIDO = 0;
	public static final int ST_CONFERIDO     = 1;
	public static final int ST_CX_FECHADO    = 2;

	public static final int CREDITO = 0;
	public static final int DEBITO  = 1;

	public static final int toCREDITO       = 0;
	public static final int toDEBITO        = 1;
	public static final int toINTERESSE     = 2;
	public static final int toDIVIDENDO     = 3;
	public static final int toTAXAS         = 4;
	public static final int toDEPOSITO      = 5;
	public static final int toRETIRADA_ATM  = 6;
	public static final int toTRANSFERENCIA = 7;
	public static final int toCHEQUE        = 8;
	public static final int toPAGAMENTO     = 9;
	public static final int toSAQUE         = 10;
	public static final int toDEPOSITO_SALARIO = 11;
	public static final int toOUTROS        = 12;
	public static final int toESTORNO       = 13;
	public static final int toREAJUSTE      = 14;

	public static final String[] tipoOrigem = { "Crédito", "Débito",
			"Interesse", "Dividendo", "Taxas", "Depósito", "Retirada em ATM",
			"Transferência", "Cheque", "Pagamento", "Saque",
			"Depósito Salário", "Outros", "Estorno", "Reajuste" };

	public static final String[] situacaoMovBancario = { "Não compensado",
			"Compensado", "Conciliado", "Cancelado", "Liquidado" };
	public static final String[] situacaoMovCaixa = { "Não conferido",
			"Conferido", "Cx. Fechado", "Cancelado", "Liquidado" };
	public static final String[] situacaoMov = { "Não compensado/conferido",
			"Compensado/conferido", "Conciliado/caixa fechado", "Cancelado",
	"Liquidado" };

	public static final int ERR_MOV_NAO_LOCALIZADO = -5;
	public static final int ERR_DELETE_MOV_FECHADO = -10;
	public static final int ERR_DELETE_TITULO_MOV = -11;
	public static final int ERR_DELETE_CONTA_RECEBER_MOV = -12;
	public static final int ERR_DELETE_CONTA_PAGAR_MOV = -13;
	public static final int ERR_DELETE_MOV_DEPENDENTE = -14;
	public static final int ERR_CAN_DELETE_MOV_ORIGEM = -15;
	public static final int ERR_DELETE_MOV_CAIXA_FECHADO = -16;
	public static final int ERR_DIVERGENCIA_VALORES_TRANSF = -1001;
	public static final int ERR_SAVE_MOVIMENTO = -1002;
	public static final int ERR_SAVE_MOV_ORIGEM = -1003;
	public static final int ERR_SAVE_MOV_DESTINO = -1004;
	public static final int ERR_SAVE_TITULO_MOV_ORIGEM = -1005;
	public static final int ERR_SAVE_TITULO_MOV_DESTINO = -1006;

	public static final int ERR_TRANSF_NOT_FORMA_PAG = -16;
	public static final int ERR_TRANSF_MULT_FORM_PAG = -17;
	public static final int ERR_TRANSF_VALOR_MOV = -18;
	public static final int ERR_TRANSF_NOT_LOC_CONTA_ORIGEM = -19;

	/*
	 * FUNÇÕES DO RECEBIMENTO NO PDV
	 */
	public static Result registraFormaRecebimento(int cdConta,
			int cdMovimentoOrigem, int cdFormaPagamento, float vlMovimento,
			String nrDocumento, String dsHistorico) {
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = true;
		try {
			Result result = new Result(1);
			MovimentoConta movOrigem = MovimentoContaDAO.get(cdMovimentoOrigem,
					cdConta, connect);
			/*
			 * Quando o valor pago é maior do que o a receber
			 */
			if (movOrigem.getVlMovimento() == vlMovimento) {
				movOrigem.setCdFormaPagamento(cdFormaPagamento);
				movOrigem.setStMovimento(MovimentoContaServices.ST_CONFERIDO);
				movOrigem
				.setDsHistorico("Recebimento PDV "
						+ (dsHistorico != null
						&& !dsHistorico.equals("") ? ": "
						+ dsHistorico : ""));
				movOrigem.setNrDocumento(nrDocumento);
				MovimentoContaDAO.update(movOrigem);
				return new Result(movOrigem.getCdMovimentoConta());
			}
			if (movOrigem.getVlMovimento() > vlMovimento)
				vlMovimento = movOrigem.getVlMovimento().floatValue();
			/*
			 * Cria o movimento de conta
			 */
			connect.setAutoCommit(false);
			int stMovimento = MovimentoContaServices.ST_CONFERIDO;
			dsHistorico = "Recebimento PDV"
					+ (dsHistorico != null && !dsHistorico.equals("") ? ": "
							+ dsHistorico : "");
			GregorianCalendar dtMovimento = new GregorianCalendar();
			MovimentoConta movimento = new MovimentoConta(
					0 /* cdMovimentoConta */, cdConta,
					cdConta /* cdContaOrigem */, cdMovimentoOrigem,
					movOrigem.getCdUsuario(), 0 /* cdCheque */, 0 /* cdViagem */,
					dtMovimento, vlMovimento, nrDocumento,
					MovimentoContaServices.CREDITO, 0 /* tpOrigem */,
					stMovimento, dsHistorico, null /* dtDeposito */,
					null /* idExtrato */, cdFormaPagamento, 0 /* cdFechamento */,
					0/* cdTurno */);
			ArrayList<MovimentoContaReceber> movContas = new ArrayList<MovimentoContaReceber>();
			/*
			 * Migra recebimentos
			 */
			ArrayList<MovimentoContaCategoria> movimentoCategoria = null;
			ResultSetMap rsmRecs = MovimentoContaReceberServices
					.getRecebimentoOfMovimento(cdConta, cdMovimentoOrigem,
							connect);
			while (rsmRecs.next() && vlMovimento > 0) {
				int cdContaReceber = rsmRecs.getInt("cd_conta_receber");
				float vlRecebido = rsmRecs.getFloat("vl_recebido"); // Pega o
				// valor
				// recebido
				// da conta
				if (vlRecebido > vlMovimento)
					vlRecebido = vlMovimento;
				float vlJuros = rsmRecs.getFloat("vl_juros")
						/ rsmRecs.getFloat("vl_recebido") * vlRecebido;
				float vlMulta = rsmRecs.getFloat("vl_multa")
						/ rsmRecs.getFloat("vl_recebido") * vlRecebido;
				float vlDesconto = rsmRecs.getFloat("vl_desconto")
						/ rsmRecs.getFloat("vl_recebido") * vlRecebido;
				movContas.add(new MovimentoContaReceber(cdConta, 0 /*
				 * cdMovimentoConta
				 * )
				 */,
				 rsmRecs.getInt("cd_conta_receber"), vlRecebido,
				 vlJuros, vlMulta, vlDesconto, 0 /* vlTarifaCobranca */,
				 0 /* cdArquivo */, 0 /* cdRegistro */));
				vlMovimento -= vlRecebido;
				/*
				 * Cria a classificação em categorias do movimento na conta
				 */
				// float vlTotalClassificado = 0;
				movimentoCategoria = new ArrayList<MovimentoContaCategoria>();
				ResultSetMap rsmCategorias = ContaReceberCategoriaServices
						.getCategoriaOfContaReceber(cdContaReceber, connect);
				while (rsmCategorias.next()) {
					float vlMovimentoCategoria = 0; // rsmCategorias.getFloat("vl_conta_categoria")
					// / conta.getVlConta() *
					// (vlRecebido-vlMulta-vlJuros+vlDesconto);
					// vlTotalClassificado += vlMovimentoCategoria;
					movimentoCategoria
					.add(new MovimentoContaCategoria(
							cdConta,
							0/* cdMovimentoConta */,
							rsmCategorias
							.getInt("cd_categoria_economica"),
							vlMovimentoCategoria,
							0 /* cdMovimentoContaCategoria */,
							0 /* cdContaPagar */,
							cdContaReceber,
							MovimentoContaCategoriaServices.TP_PRE_CLASSIFICACAO /* tpMovimento */,
							rsmCategorias.getInt("cd_centro_custo")/* cdCentroCusto */));
				}
			}
			/*
			 * Chama o método que faz o lançamento da conta, o título de
			 * crédito,
			 */
			result = insert(movimento, movContas, movimentoCategoria,
					new ArrayList<MovimentoContaTituloCredito>(), 0, connect);
			if (result.getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				System.out
				.println("substituiMovimento: Não foi possível gravar o movimento!");
				com.tivic.manager.util.Util
				.registerLog(new Exception(
						"insertRecebimento: Não foi possível gravar o movimento!"));
				return new Result(-40,
						"substituiMovimento: Não foi possível gravar o movimento!");
			}

			if (isConnectionNull)
				connect.commit();
			
			return result;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar lançar recebimento!", e);
		} finally {
			Conexao.desconectar(connect);
		}
	}

	/*
	 * FUNÇÕES DO RECEBIMENTO NO PDV
	 */
	public static Result insertRecebimento(int cdConta, int cdContaReceber,
			float vlMulta, float vlJuros, float vlDesconto, float vlRecebido,
			int cdUsuario) {
		return insertRecebimento(cdConta, cdContaReceber, vlMulta, vlJuros, 0, vlDesconto, vlRecebido, cdUsuario);
	}
	public static Result insertRecebimento(int cdConta, int cdContaReceber,
			float vlMulta, float vlJuros, float vlAcrescimo, float vlDesconto, float vlRecebido,
			int cdUsuario) {
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = true;
		try {
			/*
			 * Verifica classificação de Multa, Juros e Desconto
			 */
			ContaFinanceira contaFin = ContaFinanceiraDAO.get(cdConta, connect);
			int cdCategoriaMulta     = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_MULTA_RECEBIDA", 0, contaFin.getCdEmpresa());
			int cdCategoriaJuros     = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_JUROS_RECEBIDO", 0, contaFin.getCdEmpresa());
			int cdCategoriaDesconto  = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_DESCONTO_CONCEDIDO", 0, contaFin.getCdEmpresa());
			int cdCategoriaAcrescimo = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ACRESCIMO_RECEBIDO", 0, contaFin.getCdEmpresa());
			if ((vlMulta > 0 && cdCategoriaMulta <= 0)
					|| (vlJuros > 0 && cdCategoriaJuros <= 0)
					|| (vlDesconto > 0 && cdCategoriaDesconto <= 0)
					|| (vlAcrescimo > 0 && cdCategoriaAcrescimo <= 0)) {
				System.out
				.println("Classificação dos juros/multa ou acréscimo/desconto inexistente!");
				return new Result(
						-20,
						"Classificação dos juros/multa ou acréscimo/desconto inexistente",
						new Exception(
								"Classificação dos juros/multa ou desconto inexistente!"));
			}
			/*
			 * Instancia a conta a receber
			 */
			ContaReceber conta = ContaReceberDAO.get(cdContaReceber, connect);
			/*
			 * Cria o movimento de conta
			 */
			ResultSet rs = connect
					.prepareStatement(
							"SELECT * FROM adm_forma_pagamento WHERE UPPER(nm_forma_pagamento) = \'DINHEIRO\'")
					.executeQuery();
			int cdFormaPagamento = 0;
			if (rs.next())
				cdFormaPagamento = rs.getInt("cd_forma_pagamento");
			int stMovimento = MovimentoContaServices.ST_COMPENSADO;
			String dsHistorico = "Recebimento PDV - Lançamento não concluído";
			GregorianCalendar dtMovimento = new GregorianCalendar();
			MovimentoConta movimento = new MovimentoConta(
					0 /* cdMovimentoConta */, cdConta, 0 /* cdContaOrigem */,
					0 /* cdMovimentoOrigem */, cdUsuario, 0 /* cdCheque */,
					0 /* cdViagem */, dtMovimento, vlRecebido,
					null /* nrDocumento */, MovimentoContaServices.CREDITO,
					0 /* tpOrigem */, stMovimento, dsHistorico,
					null /* dtDeposito */, null /* idExtrato */,
					cdFormaPagamento, 0 /* cdFechamento */, 0/* cdTurno */);
			ArrayList<MovimentoContaReceber> movimentoContaReceber = new ArrayList<MovimentoContaReceber>();
			/*
			 * Cria o recebimento
			 */
			movimentoContaReceber.add(new MovimentoContaReceber(cdConta,
					0 /* cdMovimentoConta) */, cdContaReceber, vlRecebido,
					vlJuros, vlMulta, vlAcrescimo, vlDesconto, 0 /* vlTarifaCobranca */,
					0 /* cdArquivo */, 0 /* cdRegistro */));
			/*
			 * Cria a classificação em categorias do movimento na conta
			 */
			float vlTotalClassificado = 0;
			ArrayList<MovimentoContaCategoria> movimentoCategoria = new ArrayList<MovimentoContaCategoria>();
			ResultSetMap rsmCategorias = ContaReceberCategoriaServices
					.getCategoriaOfContaReceber(cdContaReceber, connect);
			while (rsmCategorias.next()) {
				float vlMovimentoCategoria = rsmCategorias.getFloat("vl_conta_categoria") / 
						conta.getVlConta().floatValue() * (vlRecebido - vlMulta - vlJuros + vlDesconto - vlAcrescimo);
				vlTotalClassificado += vlMovimentoCategoria;
				movimentoCategoria
				.add(new MovimentoContaCategoria(
						cdConta,
						0/* cdMovimentoConta */,
						rsmCategorias.getInt("cd_categoria_economica"),
						vlMovimentoCategoria,
						0 /* cdMovimentoContaCategoria */,
						0 /* cdContaPagar */,
						cdContaReceber,
						MovimentoContaCategoriaServices.TP_PRE_CLASSIFICACAO /* tpMovimento */,
						rsmCategorias.getInt("cd_centro_custo")/* cdCentroCusto */));
			}
			if (vlMulta > 0)
				movimentoCategoria
				.add(new MovimentoContaCategoria(
						cdConta,
						0/* cdMovimentoConta */,
						cdCategoriaMulta,
						vlMulta,
						0 /* cdMovimentoContaCategoria */,
						0 /* cdContaPagar */,
						cdContaReceber,
						MovimentoContaCategoriaServices.TP_MULTA /* tpMovimento */,
						rsmCategorias.getInt("cd_centro_custo")/* cdCentroCusto */));
			if (vlJuros > 0)
				movimentoCategoria.add(new MovimentoContaCategoria(
						cdConta,
						0/* cdMovimentoConta */,
						cdCategoriaJuros,
						vlJuros,
						0 /* cdMovimentoContaCategoria */,
						0 /* cdContaPagar */,
						cdContaReceber,
						MovimentoContaCategoriaServices.TP_JUROS /* tpMovimento */,
						rsmCategorias.getInt("cd_centro_custo")/* cdCentroCusto */));
			if (vlDesconto > 0)
				movimentoCategoria.add(new MovimentoContaCategoria(
						cdConta,
						0/* cdMovimentoConta */,
						cdCategoriaDesconto,
						vlDesconto,
						0 /* cdMovimentoContaCategoria */,
						0 /* cdContaPagar */,
						cdContaReceber,
						MovimentoContaCategoriaServices.TP_DESCONTO /* tpMovimento */,
						rsmCategorias.getInt("cd_centro_custo")/* cdCentroCusto */));
			if (vlAcrescimo > 0)
				movimentoCategoria.add(new MovimentoContaCategoria(
						cdConta,
						0/* cdMovimentoConta */,
						cdCategoriaAcrescimo,
						vlAcrescimo,
						0 /* cdMovimentoContaCategoria */,
						0 /* cdContaPagar */,
						cdContaReceber,
						MovimentoContaCategoriaServices.TP_ACRESCIMO /* tpMovimento */,
						rsmCategorias.getInt("cd_centro_custo")/* cdCentroCusto */));
			if (vlTotalClassificado + 0.01 < (vlRecebido - vlMulta - vlJuros - vlAcrescimo + vlDesconto)) {
				System.out
				.println("setBaixaResumida: Conta não classificada correntamente!");
				return new Result(-30,
						"insertRecebimento: Conta não classificada correntamente!");
			}
			/*
			 * Chama o método que faz o lançamento da conta
			 */
			Result result = MovimentoContaServices.insert(movimento,
					movimentoContaReceber, movimentoCategoria,
					new ArrayList<MovimentoContaTituloCredito>(), 0, connect);
			if (result.getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				System.out
				.println("insertRecebimento: Não foi possível gravar o movimento!");
				com.tivic.manager.util.Util
				.registerLog(new Exception(
						"insertRecebimento: Não foi possível gravar o movimento!"));
				return new Result(-40,
						"insertRecebimento: Não foi possível gravar o movimento!");
			}

			if (isConnectionNull){
				if (result != null){
					connect.setAutoCommit(false);
					connect.commit();	
				}

			}

			return result;
		} catch (Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar lançar recebimento!", e);
		} finally {
			Conexao.desconectar(connect);
		}
	}

	/**
	 * @author Alvaro
	 * @param movimentoConta
	 * @param contas( MovimentoContaPagar|MovimentoContaReceber )
	 * @param MovimentoContaCategoria
	 * @param MovimentoContaTitulo
	 * @param stCheque
	 * @param connect
	 * @return Result
	 */
	public static Result save(MovimentoConta objeto, ArrayList<?> contas,
			ArrayList<MovimentoContaCategoria> categorias,
			ArrayList<MovimentoContaTituloCredito> titulos, int stCheque){
		return save(objeto, contas, categorias, titulos, stCheque, null);
	}
	public static Result save(MovimentoConta objeto, ArrayList<?> contas,
			ArrayList<MovimentoContaCategoria> categorias,
			ArrayList<MovimentoContaTituloCredito> titulos, int stCheque, Connection connect){

		Result result = insert(objeto, contas, categorias, titulos, stCheque, connect);
		if( result.getCode() > 0 )
			result.setMessage("Movimento criado com sucesso!");
		return result;
	}
	/*
	 * FUNÇÕES DE INSERIR MOVIMENTO JUNTO COM A CONTA A PAGAR E RECEBER
	 */
	public static Result insert(MovimentoConta objeto,
			ArrayList<?> movimentoConta,
			ArrayList<MovimentoContaCategoria> categorias, boolean filaImpressao) {
		return insert(objeto, movimentoConta, categorias, filaImpressao, null);
	}

	public static Result insert(MovimentoConta objeto,
			ArrayList<?> movimentoConta,
			ArrayList<MovimentoContaCategoria> categorias,
			boolean filaImpressao, Connection connect) {
		return insert(objeto, movimentoConta, categorias, null,
				ChequeServices.EMITIDO, connect);
	}

	public static Result insert(MovimentoConta objeto, ArrayList<?> contas,
			ArrayList<MovimentoContaCategoria> categorias,
			ArrayList<MovimentoContaTituloCredito> titulos, int stCheque) {
		return insert(objeto, contas, categorias, titulos, stCheque, null);
	}

	public static Result insert(MovimentoConta objeto, ArrayList<?> contas,
			ArrayList<MovimentoContaCategoria> categorias,
			ArrayList<MovimentoContaTituloCredito> titulos, int stCheque,
			Connection connect) {
		boolean isConnectionNull = connect == null;
		try {			
			/*
			 * Verifica se o caixa está aberto para o usuário informado, se não
			 * há fechamento para o período e se o usuário tem permissão para o
			 * caixa
			 */
			if (  objeto.getTpMovimento() != MovimentoContaServices.CREDITO
					&& objeto.getTpOrigem() != MovimentoContaServices.toTRANSFERENCIA
					) {
				Result result = ContaFinanceiraServices.isCaixaAbertoTo(
						objeto.getCdConta(), 0/* cdMovimentoConta */,
						objeto.getCdUsuario(), objeto.getCdFechamento(),
						objeto.getDtMovimento(), connect);
				if (result.getCode() <= 0)
					return result;
			}

			boolean isTransferencia = objeto.getTpMovimento() == MovimentoContaServices.DEBITO
					&& objeto.getTpOrigem() == MovimentoContaServices.toTRANSFERENCIA;

			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			// Verifica se é um movimento em moeda corrente, não sendo o
			// movimento fica como não compensado
			FormaPagamento formaPagamento = FormaPagamentoDAO.get(objeto.getCdFormaPagamento(), connect);
			if (formaPagamento == null)
				return new Result(-1, "Forma de pagamento não informada!");

			if (formaPagamento.getTpFormaPagamento() != FormaPagamentoServices.MOEDA_CORRENTE) {
				ContaFinanceira conta = ContaFinanceiraDAO.get(objeto.getCdConta(), connect);
				if (conta.getTpConta() == ContaFinanceiraServices.TP_CAIXA)
					objeto.setStMovimento(ST_NAO_COMPENSADO);
			}
			objeto.getDtMovimento().set(Calendar.MINUTE, 0);
			objeto.getDtMovimento().set(Calendar.HOUR, 0);
			objeto.getDtMovimento().set(Calendar.SECOND, 0);
			objeto.getDtMovimento().set(Calendar.MILLISECOND, 0);
			if (isTransferencia) {
				Result result = insertTransfer(objeto, objeto.getCdContaOrigem(), titulos, connect);
				if (result.getCode() <= 0)
					return result;
				objeto.setCdMovimentoConta(result.getCode());
			} else{
				objeto.setCdMovimentoConta(MovimentoContaDAO.insert(objeto, connect));
				ComplianceManager.process(objeto, null, ComplianceManager.TP_ACAO_INSERT, connect);
			}
			//
			Result ret = new Result(objeto.getCdMovimentoConta());
			if (ret.getCode() <= 0) {
				Exception exception = new Exception("Não foi possível registrar o movimento! ERRO " + ret);
				exception.printStackTrace(System.out);
				com.tivic.manager.util.Util.registerLog(exception);
				if (isConnectionNull)
					Conexao.rollback(connect);
				ret.setMessage("Não foi possível registrar o movimento! ERRO "+ ret.getCode());
				return ret;
			}
			/*
			 * Grava recebimento e pagamentos
			 */
			if (objeto.getCdMovimentoConta() > 0 && !isTransferencia) {
				// SOMA OS TÍTULOS DE CRÉDITO
				float vlTotalTitulos = 0;
				for (int i = 0; titulos != null && i < titulos.size(); i++) {
					TituloCredito titulo = TituloCreditoDAO.get(titulos.get(i).getCdTituloCredito(), connect);
					vlTotalTitulos += titulo != null ? titulo.getVlTitulo() : 0;
				}
				/*
				 * Grava classificação
				 */
				Double vlTotalCategoria = 0.0;
				Double vlTotalCategoriaDesconto = 0.0;
				Double vlCategoriaDesconto1 = 0.0;
				Double vlCategoriaDesconto2 = 0.0;
				if (ret.getCode() > 0)
					for (int i = 0; categorias != null && i < categorias.size(); i++) {
						categorias.get(i).setCdMovimentoConta( objeto.getCdMovimentoConta());
						ret.setCode(MovimentoContaCategoriaServices.insert(categorias.get(i), connect));

						CategoriaEconomica categoria = CategoriaEconomicaDAO.get(categorias.get(i).getCdCategoriaEconomica(), connect);

						int tpCategoria = categoria.getTpCategoriaEconomica();
						if (categorias.get(i).getTpMovimento() == MovimentoContaCategoriaServices.TP_DESCONTO
								|| (objeto.getTpMovimento() == CREDITO && tpCategoria == CategoriaEconomicaServices.TP_DESPESA)
								|| (objeto.getTpMovimento() == DEBITO && tpCategoria == CategoriaEconomicaServices.TP_RECEITA)
								){
							vlTotalCategoriaDesconto += (categorias.get(i)).getVlMovimentoCategoria();
							if (vlCategoriaDesconto1 == 0)
								vlCategoriaDesconto1 = (categorias.get(i)).getVlMovimentoCategoria();
							else
								vlCategoriaDesconto2 = (categorias.get(i)).getVlMovimentoCategoria();
						} else{
							vlTotalCategoria += (categorias.get(i)).getVlMovimentoCategoria();
						}
						//
						if (ret.getCode() <= 0) {
							ret.setMessage("Falha ao tentar gravar categorias!");
							break;
						}
					}
				boolean lgRegistroTituloOK = (formaPagamento.getTpFormaPagamento() != FormaPagamentoServices.MOEDA_CORRENTE)
						&& (objeto.getTpMovimento() == CREDITO)
						&& (Math.abs(Math.abs(objeto.getVlMovimento()) - Math.abs(vlTotalTitulos)) <= 0.01);

				// Util.printInFile("/pdv.log",
				// "\n vlMovimento: "+objeto.getVlMovimento()+", vlTotalTitulos: "+vlTotalTitulos);

				Double vlTotalPagamento = 0.0;
				for (int i = 0; contas != null && i < contas.size(); i++) {
					if (contas.get(i) instanceof MovimentoContaReceber) {
						boolean isEstorno = objeto.getTpMovimento() == MovimentoContaServices.DEBITO;
						((MovimentoContaReceber) contas.get(i)).setCdMovimentoConta(objeto.getCdMovimentoConta());
						ret.setCode(MovimentoContaReceberServices.insert((MovimentoContaReceber) contas.get(i), objeto.getDtMovimento(), isEstorno, false, connect));
						vlTotalPagamento += ((MovimentoContaReceber) contas.get(i)).getVlRecebido();
					} else {
						((MovimentoContaPagar) contas.get(i)).setCdMovimentoConta(objeto.getCdMovimentoConta());
						ret.setCode(MovimentoContaPagarServices.insert( (MovimentoContaPagar) contas.get(i), connect));
						vlTotalPagamento += ((MovimentoContaPagar) contas.get(i)).getVlPago();
					}
					if (ret.getCode() <= 0)
						break;
				}

				if ((Math.abs(Math.abs(objeto.getVlMovimento()) - Math.abs(vlTotalPagamento)) > 0.01
						&& !isTransferencia && !lgRegistroTituloOK)
						|| ret.getCode() <= 0) {
					String dsMsg = ret.getCode() <= 0 ? "Não foi possível gravar em MovimentoContaServices"
							: "As contas pagas/recebidas nao foram informadas corretamente! \n\t vlMovimento = "
							+ objeto.getVlMovimento()
							+ "\n\t vlTotalPagamento = "
							+ vlTotalPagamento;
					ret.setMessage(ret.getMessage() + "\n" + dsMsg);
					ret.setCode(-1);
					ret.addObject("nrContaReceber", objeto.getNrDocumento());
					ret.addObject("vlMovimento", objeto.getVlMovimento());
					ret.addObject("vlTotalPagamento", vlTotalPagamento);
					// Exception exception = new Exception(ret.getMessage() +
					// "\n" + dsMsg+" Code: "+ret.getCode());
					// exception.printStackTrace(System.out);
					if (isConnectionNull)
						Conexao.rollback(connect);
					// com.tivic.manager.util.Util.registerLog(exception);
					return ret;
				}
				//
				if ((   vlTotalCategoria > 0 && Math.abs(objeto.getVlMovimento() - vlTotalCategoria) > 0.1
						&& Math.abs(objeto.getVlMovimento() - vlTotalCategoria + vlTotalCategoriaDesconto) > 0.1
						&& Math.abs(objeto.getVlMovimento() - vlTotalCategoria + vlCategoriaDesconto1) > 0.1
						&& Math.abs(objeto.getVlMovimento() - vlTotalCategoria + vlCategoriaDesconto2) > 0.1 && !isTransferencia)
						|| ret.getCode() <= 0
						) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					ret.setCode(-1);
					ret.setMessage("A classificação do movimento não foi informada corretamente! vlMovimento: "
							+ objeto.getVlMovimento()
							+ " vlTotalCategoria: "
							+ vlTotalCategoria
							+ " vlTotalCategoriaDesconto: "
							+ vlTotalCategoriaDesconto
							+ " vlCategoriaDesconto1: "
							+ vlCategoriaDesconto1
							+ " vlCategoriaDesconto2: " + vlCategoriaDesconto2);
					// Exception exception = new Exception(ret.getMessage());
					// exception.printStackTrace(System.out);
					// com.tivic.manager.util.Util.registerLog(exception);
					// Util.printInFile("/pdv.log", categorias.toString());
					return ret;
				}

				ContaFinanceira contaFinanceira = ContaFinanceiraDAO.get(objeto.getCdConta(), connect);
				// Por favor não acrescentar TEF, eu já havia tirado porque
				// existem situações corretas do dia a dia que não satisfazem
				// essa
				// exigência, o PDV pára de funcionar se for acrescentado
				if ((formaPagamento.getTpFormaPagamento() != FormaPagamentoServices.MOEDA_CORRENTE)
						&& lgRegistroTituloOK
						&& objeto.getCdCheque() <= 0
						) {
					// Categorias do Títulos de Crédito
					ArrayList<ContaReceberCategoria> categoriasTitulos = new ArrayList<ContaReceberCategoria>();
					for (int i = 0; contas != null && i < contas.size(); i++) {
						if (contas.get(i) instanceof MovimentoContaReceber) {
							MovimentoContaReceber movContaReceber = (MovimentoContaReceber) contas.get(i);
							double vlMovimentoConta = movContaReceber.getVlRecebido() - movContaReceber.getVlJuros() - movContaReceber.getVlMulta() + movContaReceber.getVlDesconto();
							for (int j = 0; j < categorias.size(); j++)
								if (categorias.get(j).getCdContaReceber() == movContaReceber.getCdContaReceber()
								&& categorias.get(j).getTpMovimento() != MovimentoContaCategoriaServices.TP_DESCONTO) {
									int k = 0;
									for (; k < categoriasTitulos.size(); k++)
										if (categoriasTitulos.get(k).getCdCategoriaEconomica() == categorias.get(j).getCdCategoriaEconomica())
											break;
									if (k >= categoriasTitulos.size()) {
										categoriasTitulos.add(new ContaReceberCategoria(0 /* cdContaReceber */, categorias.get(j).getCdCategoriaEconomica(), 
												categorias.get(j).getVlMovimentoCategoria() * (categorias.get(j).getTpMovimento() != MovimentoContaCategoriaServices.TP_PRE_CLASSIFICACAO ? 
														1 : new Double((vlMovimentoConta - movContaReceber.getVlDesconto()) / vlMovimentoConta).floatValue() ) ,
												categorias.get(j).getCdCentroCusto()));
									} else
										categoriasTitulos.get(k).setVlContaCategoria(categoriasTitulos.get(k).getVlContaCategoria() + categorias.get(j).getVlMovimentoCategoria() * 
												(categorias.get(j).getTpMovimento() != MovimentoContaCategoriaServices.TP_PRE_CLASSIFICACAO ? 
														1 : new Double((vlMovimentoConta - movContaReceber.getVlDesconto()) / vlMovimentoConta).floatValue()));
								}
						}
					}
					float vlTotalTitulosTemp = 0;
					for (int i = 0; i < categoriasTitulos.size(); i++)
						vlTotalTitulosTemp += categoriasTitulos.get(i).getVlContaCategoria();

					for (int i = 0; titulos != null && i < titulos.size(); i++) {
						// Salvando Títulos
						titulos.get(i).setCdMovimentoConta(objeto.getCdMovimentoConta());
						ret.setCode(MovimentoContaTituloCreditoDAO.insert(titulos.get(i), connect));
						TituloCredito titulo = TituloCreditoDAO.get(titulos.get(i).getCdTituloCredito(), connect);
						// Criando conta a receber
						if (titulo.getCdContaReceber() <= 0) {
							ContaReceber contaReceber = new ContaReceber(
									0 /* cdContaReceber */,
									0 /* cdPessoa */,
									contaFinanceira.getCdEmpresa(),
									0 /* cdContrato */,
									0 /* cdContaOrigem */,
									0 /* cdDocumentoSaida */,
									0 /* cdContaCarteira */,
									contaFinanceira.getCdConta(),
									0 /* cdFrete */,
									titulo.getNrDocumento(),
									"" /* idContaReceber */,
									0 /* nrParcela */,
									"" /* nrReferencia */,
									titulo.getCdTipoDocumento(),
									"TÍTULO DE CRÉDITO" /* dsHistorico */,
									titulo.getDtVencimento(),
									objeto.getDtMovimento() /* dtEmissao */,
									null /* dtRecebimento */,
									null /* dtProrrogacao */,
									titulo.getVlTitulo() /* vlConta */,
									0.0d /* vlAbatimento */,
									0.0d /* vlAcrescimo */,
									0.0d /* vlRecebido */,
									ContaReceberServices.ST_EM_ABERTO /* stConta */,
									ContaReceberServices.UNICA_VEZ /* tpFrequencia */,
									1 /* qtParcelas */,
									ContaReceberServices.TP_OUTRO /* tpContaReceber */,
									0 /* cdNegociacao */,
									"" /* txtObservacao */,
									0 /* cdPlanoPagamento */,
									0 /* cdFormaPagamento */,
									new GregorianCalendar(), titulo
									.getDtVencimento(), 0/* cdTurno */,
									0.0d/* prJuros */, 0.0d/* prMulta */, 0/* lgProtesto */);
							ArrayList<ContaReceberCategoria> categoriasConta = new ArrayList<ContaReceberCategoria>();
							for (int j = 0; j < categoriasTitulos.size(); j++)
								categoriasConta.add(new ContaReceberCategoria(
										0 /* cdContaReceber */, categoriasTitulos
										.get(j)
										.getCdCategoriaEconomica(),
										categoriasTitulos.get(j)
										.getVlContaCategoria()
										* titulo.getVlTitulo()
										/ vlTotalTitulosTemp, 0));
							Result result = ContaReceberServices.insert(
									contaReceber, categoriasConta, null,
									true /* ignorarDuplicidade */, connect);
							if (result.getCode() <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connect);
								return result;
							}

							titulo.setCdContaReceber(contaReceber
									.getCdContaReceber());
							if (TituloCreditoDAO.update(titulo, connect) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connect);
								ret.setCode(-1);
								return ret.getCode() > 0 ? new Result(-99)
										: ret;
							}
						}

						if (ret.getCode() <= 0) {
							ret.setCode(ret.getCode() * 200);
							break;
						}
					}


					if ((Math.abs(objeto.getVlMovimento() - vlTotalTitulos) > 0.01
							&& Math.abs(objeto.getVlMovimento()
									- vlTotalTitulos - vlCategoriaDesconto1) > 0.01
							&& Math.abs(objeto.getVlMovimento()
									- vlTotalTitulos - vlCategoriaDesconto2) > 0.01 && !isTransferencia)
							|| ret.getCode() <= 0) {
						Util.printInFile(
								"/pdv.log",
								"\n C1: "
										+ Math.abs(objeto.getVlMovimento()
												- vlTotalTitulos)
										+ " , C2: "
										+ Math.abs(objeto.getVlMovimento()
												- vlTotalTitulos
												- vlCategoriaDesconto1)
										+ " , C3: "
										+ Math.abs(objeto.getVlMovimento()
												- vlTotalTitulos
												- vlCategoriaDesconto2) + 
										" , isTransferencia: " + isTransferencia+
										" , getCode: " +ret.getCode()); 
						//
						Exception exception = new Exception(
								"A relação de titulos de credito não foi informada corretamente! "
										+ " Valor do movimento: "
										+ objeto.getVlMovimento()
										+ " Valor em titulos: "
										+ vlTotalTitulos
										+ " Total Descontos Categoria: "
										+ vlTotalCategoriaDesconto
										+ " Descontos Categoria 1: "
										+ vlCategoriaDesconto1
										+ " Descontos Categoria 2: "
										+ vlCategoriaDesconto2
										+ " Código de execução anterior: "
										+ ret.getCode());
						exception.printStackTrace(System.out);
						com.tivic.manager.util.Util.registerLog(exception);
						if (isConnectionNull)
							Conexao.rollback(connect);
						ret.setCode(-1);
						return ret.getCode() > 0 ? new Result(-99,
								exception.getMessage(), exception) : ret;
					}

				}
				/*
				 * Altera situação do cheque
				 */
				if (ret.getCode() > 0 && objeto.getCdCheque() > 0) {
					Result result = ChequeServices.verificaSituacaoCheque(
							objeto.getCdCheque(), stCheque, connect);
					if (result.getCode() <= 0) {
						ret.setMessage(result.getMessage());
						ret.setCode(result.getCode() * 1000);
					}
				}

			}
			if (isConnectionNull){
				if (ret.getCode() > 0){
					connect.commit();
				}else {
					Conexao.rollback(connect);
					com.tivic.manager.util.Util.registerLog(new Exception("MovimentoContaServices.insert - rollback"));
					return ret;
				}
			}
			
			return ret.getCode() <= 0 ? ret : new Result(
					objeto.getCdMovimentoConta());
		} catch (Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar incluir recebimento/pagamento!", e);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result update(MovimentoConta objeto, ArrayList<?> contas,
			ArrayList<MovimentoContaCategoria> categorias,
			ArrayList<MovimentoContaTituloCredito> titulos, int stCheque, boolean lgIgnorarSituacaoCaixa) {
		return update(objeto, contas, categorias, titulos, stCheque, lgIgnorarSituacaoCaixa, null);
	}
	public static Result update(MovimentoConta objeto, ArrayList<?> contas,
			ArrayList<MovimentoContaCategoria> categorias,
			ArrayList<MovimentoContaTituloCredito> titulos, int stCheque) {
		return update(objeto, contas, categorias, titulos, stCheque, false, null);
	}

	public static int update(MovimentoConta objeto, int situacaoCheque) {
		Connection connect = Conexao.conectar();
		try {
			if (objeto.getCdCheque() > 0 && situacaoCheque > 0) {
				ChequeServices.verificaSituacaoCheque(objeto.getCdCheque(),
						situacaoCheque, connect);
				if (situacaoCheque == ChequeServices.EMITIDO)
					objeto.setStMovimento(ST_NAO_COMPENSADO);
			}

			return MovimentoContaDAO.update(objeto, connect);
		} catch (Exception e) {
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaServices.insert: " + e);
			return -1;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	public static Result update(MovimentoConta objeto, ArrayList<?> contas,
			ArrayList<MovimentoContaCategoria> categorias,
			ArrayList<MovimentoContaTituloCredito> titulos, int stCheque, boolean lgIgnorarSituacaoCaixa,
			Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			Result result = null;
			/*
			 * Verifica se o caixa está aberto para o usuário informado, se não
			 * há fechamento para o período e se o usuário tem permissão para o
			 * caixa
			 */
			if( !lgIgnorarSituacaoCaixa  ){
				result = ContaFinanceiraServices.isCaixaAbertoTo(
						objeto.getCdConta(), objeto.getCdMovimentoConta(),
						objeto.getCdUsuario(), objeto.getCdFechamento(),
						objeto.getDtMovimento(), connect);
				if (result.getCode() <= 0)
					return result;
			}
			//
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect
					.getAutoCommit());
			
			
			boolean isTransferencia = objeto.getTpMovimento() == MovimentoContaServices.DEBITO
					&& objeto.getTpOrigem() == MovimentoContaServices.toTRANSFERENCIA;
			
			boolean isDestinoTransferencia = objeto.getTpMovimento() == MovimentoContaServices.CREDITO
					&& objeto.getTpOrigem() == MovimentoContaServices.toTRANSFERENCIA
					&& objeto.getCdMovimentoOrigem() > 0;
			int ret = MovimentoContaDAO.update(objeto, connect);
			if (ret <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ret,
						"Não foi possível atualizar o movimento! ERRO " + ret);
			}

			/*
			 * Grava recebimento e pagamentos
			 */
			if (objeto.getCdMovimentoConta() > 0 && !isTransferencia && !isDestinoTransferencia) {
			
				connect.prepareStatement(
						"DELETE FROM adm_movimento_conta_categoria "
								+ "WHERE cd_conta           = "
								+ objeto.getCdConta()
								+ "  AND cd_movimento_conta = "
								+ objeto.getCdMovimentoConta()).executeUpdate();
				
				ResultSet rs = connect.prepareStatement(
						"SELECT * FROM adm_movimento_conta_receber "
								+ "WHERE cd_conta 		  = " + objeto.getCdConta()
								+ "  AND cd_movimento_conta = "
								+ objeto.getCdMovimentoConta()).executeQuery();
				while (rs.next()) {
					result = MovimentoContaReceberServices.delete(
							rs.getInt("cd_conta"), rs.getInt("cd_movimento_conta"),
							rs.getInt("cd_conta_receber"), objeto.getCdUsuario(),
							false, connect);
					if (result.getCode() <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return result;
					}
				}
				// Excluindo pagamentos
				rs = connect.prepareStatement(
						"SELECT * FROM adm_movimento_conta_pagar "
								+ "WHERE cd_conta = " + objeto.getCdConta()
								+ "  AND cd_movimento_conta = "
								+ objeto.getCdMovimentoConta()).executeQuery();
				while (rs.next()) {
					result = MovimentoContaPagarServices.delete(
							rs.getInt("cd_conta"), rs.getInt("cd_movimento_conta"),
							rs.getInt("cd_conta_pagar"), objeto.getCdUsuario(),
							false, connect);
					if (result.getCode() <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						result.setMessage("Falha ao tentar atualizar os pagamentos do movimento. \n"
								+ result.getMessage());
						return result;
					}
				}
				// Lançando pagamentos/recebimentos
				Double vlTotalPagamento = 0.0;
				for (int i = 0; contas != null && i < contas.size(); i++) {
					if (contas.get(i) instanceof MovimentoContaReceber) {
						boolean isEstorno = objeto.getTpMovimento() == MovimentoContaServices.DEBITO;
						((MovimentoContaReceber) contas.get(i))
						.setCdMovimentoConta(objeto.getCdMovimentoConta());
						ret = MovimentoContaReceberServices.insert(
								(MovimentoContaReceber) contas.get(i),
								objeto.getDtMovimento(), isEstorno, false, connect);
						vlTotalPagamento += ((MovimentoContaReceber) contas.get(i))
								.getVlRecebido();
					} else {
						((MovimentoContaPagar) contas.get(i))
						.setCdMovimentoConta(objeto.getCdMovimentoConta());
						ret = MovimentoContaPagarServices.insert(
								(MovimentoContaPagar) contas.get(i), connect);
						vlTotalPagamento += ((MovimentoContaPagar) contas.get(i))
								.getVlPago();
					}
					if (ret <= 0) {
						ret *= 10;
						break;
					}
				}
				if ((Math.abs(objeto.getVlMovimento() - vlTotalPagamento) > 0.01 && !isTransferencia)
						|| ret <= 0) {
					Exception exception = new Exception(
							ret <= 0 ? "Não foi possível gravar o em MovimentoContaServices"
									: "As contas pagas/recebidas nao foram informadas corretamente! \n\t vlMovimento = "
									+ objeto.getVlMovimento()
									+ "\n\t vlTotalPagamento = "
									+ vlTotalPagamento);
					exception.printStackTrace(System.out);
					com.tivic.manager.util.Util.registerLog(exception);
					return new Result(ret, exception.getMessage(), exception);
				}
				/*
				 * Excluindo e gravando classificação
				 */
				connect.prepareStatement(
						"DELETE FROM adm_movimento_conta_categoria "
								+ "WHERE cd_conta           = "
								+ objeto.getCdConta()
								+ "  AND cd_movimento_conta = "
								+ objeto.getCdMovimentoConta()).execute();
				Double vlTotalCategoria = 0.0;
				if (ret > 0){
					for (int i = 0; categorias != null && i < categorias.size(); i++) {
						categorias.get(i).setCdMovimentoConta(
								objeto.getCdMovimentoConta());
						ret = MovimentoContaCategoriaServices.insert(
								categorias.get(i), connect);
						if (categorias.get(i).getTpMovimento() == MovimentoContaCategoriaServices.TP_DESCONTO)
							vlTotalCategoria -= (categorias.get(i))
							.getVlMovimentoCategoria();
						else
							vlTotalCategoria += (categorias.get(i))
							.getVlMovimentoCategoria();
						if (ret <= 0) {
							ret *= 100;
							break;
						}
					}
				}
				Boolean isClassificacaoCorreta = (Math.abs(objeto.getVlMovimento() - vlTotalCategoria) > 0.009)?false:true; 
				if (( !isClassificacaoCorreta && !isTransferencia) || ret <= 0) {
					ret = -1;
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(ret, "A classificação do movimento não foi informada corretamente!");
				}
				
				/*
				 * Excluindo e gravando a movimentação de títulos de crédito
				 */
				FormaPagamento formaPagamento = FormaPagamentoDAO.get(objeto.getCdFormaPagamento(), connect);
				connect.prepareStatement(
						"DELETE FROM adm_movimento_titulo_credito "
								+ "WHERE cd_conta = " + objeto.getCdConta()
								+ "  AND cd_movimento_conta = "
								+ objeto.getCdMovimentoConta()).execute();
				
				if (ret > 0
					&& formaPagamento != null
					&& 
					(	formaPagamento.getTpFormaPagamento() == FormaPagamentoServices.TEF 
						|| formaPagamento.getTpFormaPagamento() == FormaPagamentoServices.TITULO_CREDITO
					)
					&& objeto.getCdCheque() <= 0
				) {
					float vlTotalTitulos = 0;
					for (int i = 0; titulos != null && i < titulos.size(); i++) {
						titulos.get(i).setCdMovimentoConta(
								objeto.getCdMovimentoConta());
						ret = MovimentoContaTituloCreditoDAO.insert(titulos.get(i),
								connect);
						TituloCredito titulo = TituloCreditoDAO.get(titulos.get(i)
								.getCdTituloCredito(), connect);
						vlTotalTitulos += titulo.getVlTitulo();
						if (ret <= 0) {
							ret *= 200;
							break;
						}
					}
					
					boolean lgRegistroTituloOK = (formaPagamento.getTpFormaPagamento() != FormaPagamentoServices.MOEDA_CORRENTE)
									&& (objeto.getTpMovimento() == CREDITO)
									&& (Math.abs(Math.abs(objeto.getVlMovimento()) - Math.abs(vlTotalTitulos)) <= 0.01);
					
					if ((Math.abs(objeto.getVlMovimento() - vlTotalTitulos) > 0.009 && !isTransferencia && lgRegistroTituloOK)
							|| ret <= 0) {
						Exception exception = new Exception(
								"A relacao de titulos de credito nao foi informada corretamente! "
										+ " Valor do movimento: "
										+ objeto.getVlMovimento()
										+ " Valor em titulos: " + vlTotalTitulos
										+ " Código de execução anterior: " + ret);
						exception.printStackTrace(System.out);
						com.tivic.manager.util.Util.registerLog(exception);
						return new Result(ret, exception.getMessage(), exception);
					}
				}
				/*
				 * Altera situação do cheque
				 */
				if (ret > 0 && objeto.getCdCheque() > 0) {
					result = ChequeServices.verificaSituacaoCheque(
							objeto.getCdCheque(), stCheque, connect);
					if (result.getCode() <= 0)
						ret = result.getCode() * 1000;
						
				}
				
			}
			
			if(ret<=0) {
				if(isConnectionNull) {
					Conexao.rollback(connect);
//					"Erro ao tentar atualizar movimento de conta!"
					return new Result(ret, result.getMessage());
				}
			}

			//COMPLIANCE
			ComplianceManager.process(objeto, null, ComplianceManager.TP_ACAO_UPDATE, connect);
			
			if (isConnectionNull) 
				connect.commit();
			
			return new Result(objeto.getCdMovimentoConta(), "Movimento atualizado com sucesso!");
		} catch (Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connect);
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1,
					"Erro ao tentar atualizar movimento de conta!", e);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * @author Alvaro
	 * Método criado para encapsular a chamada em Flex
	 * @param cdConta
	 * @param cdMovimentoConta
	 * @param cdUsuario
	 * @return
	 */
	public static Result remove(int cdConta, int cdMovimentoConta, int cdUsuario) {
		Result result = remove(cdConta, cdMovimentoConta, cdUsuario, false, null);
		if( result.getCode() >0 )
			result.setMessage("Movimento excluido com Sucesso!");
		return result;
	}

	public static Result remove(int cdConta, int cdMovimentoConta, int cdUsuario, boolean lgIgnorarCaixa) {
		Result result = remove(cdConta, cdMovimentoConta, cdUsuario, lgIgnorarCaixa, null);
		if( result.getCode() >0 )
			result.setMessage("Movimento excluido com Sucesso!");
		return result;
	}

	public static Result remove(int cdConta, int cdMovimentoConta, int cdUsuario, boolean lgIgnorarCaixa, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			Result result = delete(cdConta, cdMovimentoConta, cdUsuario, true, false, lgIgnorarCaixa, connect);
			if( result.getCode() >0 )
				result.setMessage("Movimento excluido com Sucesso!");
			return result;
		} catch (Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new Result(-1,
					"Erro ao tentar excluir movimento!", e);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result delete(int cdConta, int cdMovimentoConta, int cdUsuario) {
		return delete(cdConta, cdMovimentoConta, cdUsuario, true, null);
	}

	public static Result delete(int cdConta, int cdMovimentoConta,
			int cdUsuario, boolean atualizaEmCascata, Connection connect) {
		return delete(cdConta, cdMovimentoConta, cdUsuario, atualizaEmCascata,
				false, false, connect);
	}

	public static Result delete(int cdConta, int cdMovimentoConta,
			int cdUsuario, boolean atualizaEmCascata, boolean ignoreTransfer, Connection connect) {
		return delete(cdConta, cdMovimentoConta, cdUsuario, atualizaEmCascata,
				ignoreTransfer, false, connect);
	}

	public static Result delete(int cdConta, int cdMovimentoConta,
			int cdUsuario, boolean atualizaEmCascata, boolean ignoreTransfer, boolean lgIgnorarCaixa,
			Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			/*
			 * Verifica se o caixa está aberto para o usuário informado, se não
			 * há fechamento para o período e se o usuário tem permissão para o
			 * caixa
			 */
			Result result;
			MovimentoConta movConta = MovimentoContaDAO.get(cdMovimentoConta,
					cdConta, connect);
			if(!lgIgnorarCaixa){
				result = ContaFinanceiraServices.isCaixaAbertoTo(cdConta,
						cdMovimentoConta, cdUsuario, 0, movConta.getDtMovimento(),
						connect);
				if (result.getCode() <= 0)
					return result;
			}
			// Conexão
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			/*
			 * SE FOR O DÉBITO REFERENTE A UMA TRANSFERÊNCIA (que possui sempre
			 * um débito e um crédito)
			 */
			if (movConta.getTpMovimento() == DEBITO && movConta.getTpOrigem() == toTRANSFERENCIA) {
				ResultSet rs = connect.prepareStatement(
						"SELECT cd_movimento_conta, cd_conta "
								+ "FROM adm_movimento_conta "
								+ "WHERE cd_conta_origem     = " + cdConta
								+ "  AND cd_movimento_origem = "
								+ cdMovimentoConta).executeQuery();
				while (rs.next()) {
					result = delete(rs.getInt("cd_conta"), rs.getInt("cd_movimento_conta"), cdUsuario, true, true, connect);
					if (result.getCode() <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return result;
					}
				}

				connect.prepareStatement(
						"UPDATE adm_titulo_credito SET cd_conta = "
								+ cdConta
								+ "WHERE cd_titulo_credito IN (SELECT cd_titulo_credito "
								+ "							  FROM adm_movimento_titulo_credito "
								+ "							  WHERE cd_movimento_conta = "
								+ cdMovimentoConta
								+ " 							    AND cd_conta           = "
								+ cdConta + ") ").executeUpdate();
			} else if (movConta.getTpMovimento() == CREDITO
					&& movConta.getTpOrigem() == toTRANSFERENCIA
					&& movConta.getCdMovimentoOrigem() > 0 && !ignoreTransfer) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				Util.registerLog(new Exception(
						"Não é possível excluir movimento por ser uma transferência!"));
				return new Result(-10,
						"Não é possível excluir movimento por ser uma transferência!");
			}
			
			/**
			 * Caso a movimentaao tenha gerado outras contas, as remove.
			 * Ex.: Pagamento com cartão, que gera contas a receber
			 */
			ResultSet rs = connect.prepareStatement(
									"SELECT * FROM ADM_CONTA_MOVIMENTO_ORIGEM "+
									" WHERE CD_CONTA = "+cdConta+
									" AND CD_MOVIMENTO_CONTA = "+cdMovimentoConta
								).executeQuery();
			if( rs.next() ){
				Integer[] cdContasReceber = new Integer[rs.getFetchSize()];
				for( int i=0; i<cdContasReceber.length;i++ ){
					cdContasReceber[i] = rs.getInt("CD_CONTA_RECEBER");
				}
				connect.prepareStatement(
						"DELETE FROM ADM_CONTA_MOVIMENTO_ORIGEM "+
						" WHERE CD_CONTA = "+cdConta+
						" AND CD_MOVIMENTO_CONTA = "+cdMovimentoConta
					).executeUpdate();
				ContaReceberServices.remove(cdContasReceber, connect);
			}
			/*
			 * Exclui classificação em categorias
			 */
			connect.prepareStatement(
					"DELETE FROM adm_movimento_conta_categoria "
							+ "WHERE cd_conta           = " + cdConta
							+ "  AND cd_movimento_conta = " + cdMovimentoConta)
			.executeUpdate();

			/*
			 * Se o parametro passado informa que os detalhes
			 * (pagamentos/recebimentos) deve ser atualizados
			 */
			if (atualizaEmCascata) {
				// Títulos de Crédito
				ResultSet rs2 = connect.prepareStatement(
						"SELECT * FROM adm_movimento_titulo_credito "
								+ "WHERE cd_movimento_conta = "
								+ cdMovimentoConta
								+ "  AND cd_conta           = " + cdConta)
						.executeQuery();
				while (rs2.next()) {
					if (MovimentoContaTituloCreditoDAO.delete(
							rs2.getInt("cd_titulo_credito"), cdMovimentoConta,
							cdConta, connect) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1,
								"Erro ao tentar excluir movimentação do Título de Crédito!");
					}

					ResultSet rsTemp = connect.prepareStatement(
							"SELECT * FROM adm_movimento_titulo_credito "
									+ "WHERE cd_titulo_credito = "
									+ rs2.getInt("cd_titulo_credito"))
							.executeQuery();
					if (!rsTemp.next()) {
						TituloCredito titulo = TituloCreditoDAO.get(
								rs2.getInt("cd_titulo_credito"), connect);
						if (titulo != null) {
							if (TituloCreditoDAO.delete(
									titulo.getCdTituloCredito(), connect) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1,
										"Erro ao tentar excluir Título de Crédito!");
							}
							//
							connect.prepareStatement(
									"DELETE FROM adm_movimento_conta_categoria "
											+ "WHERE cd_conta           = "
											+ cdConta
											+ "  AND cd_movimento_conta = "
											+ cdMovimentoConta
											+ "  AND cd_conta_receber   = "
											+ titulo.getCdContaReceber())
							.executeUpdate();
							//
							if (titulo.getCdContaReceber() > 0) {
								if (ContaReceberServices.delete(
										titulo.getCdContaReceber(), true,
										false, connect) <= 0) {
									if (isConnectionNull)
										Conexao.rollback(connect);
									return new Result(-1,
											"Erro ao tentar conta a receber gerada pelo movimento!");
								}
							}
						}
					}
				}
				// RECEBIMENTOS E ESTORNOS DE PAGAMENTOS
				rs2 = connect.prepareStatement(
						"SELECT * FROM adm_movimento_conta_receber "
								+ "WHERE cd_conta           = " + cdConta
								+ "  AND cd_movimento_conta = "
								+ cdMovimentoConta).executeQuery();
				while (rs2.next()) {
					result = MovimentoContaReceberServices.delete(cdConta,
							cdMovimentoConta, rs2.getInt("cd_conta_receber"),
							cdUsuario, false, connect);
					if (result.getCode() <= 0) {
						com.tivic.manager.util.Util
						.registerLog(new Exception(
								"Erro ao excluir movimento de conta a receber!"));
						if (isConnectionNull)
							Conexao.rollback(connect);
						return result;
					}
				}
				// PAGAMENTOS E ESTORNOS DE RECEBIMENTOS
				rs2 = connect.prepareStatement(
						"SELECT * FROM adm_movimento_conta_pagar "
								+ "WHERE cd_conta = " + cdConta
								+ "  AND cd_movimento_conta = "
								+ cdMovimentoConta).executeQuery();
				while (rs2.next()) {
					result = MovimentoContaPagarServices.delete(cdConta,
							cdMovimentoConta, rs2.getInt("cd_conta_pagar"),
							cdUsuario, false, connect);
					if (result.getCode() <= 0) {
						com.tivic.manager.util.Util.registerLog(new Exception(
								"Erro ao excluir movimento de conta a pagar!"));
						if (isConnectionNull)
							Conexao.rollback(connect);
						result.setMessage("Falha ao tentar excluir pagamentos! \n"
								+ result.getMessage());
						return result;
					}
				}
			}

			/*
			 * Atualiza cheque
			 */
			if (movConta.getCdCheque() > 0) {
				connect.prepareStatement(
						"UPDATE adm_cheque SET st_cheque = "
								+ ChequeServices.NAO_EMITIDO
								+ "   ,dt_impressao = null, dt_emissao = null "
								+ "WHERE cd_cheque  = "
								+ movConta.getCdCheque()).executeUpdate();
			}

			/*
			 * exclui registros de pagamento/faturamento de contratos
			 * relacionados
			 */
			connect.prepareStatement(
					"DELETE FROM adm_contrato_plano_pagto "
							+ "WHERE cd_conta           = " + cdConta
							+ "  AND cd_movimento_conta = " + cdMovimentoConta)
			.executeUpdate();

			/*
			 * Exclui arquivos
			 */

			Result res = MovimentoContaArquivoServices.removeAll(cdMovimentoConta, cdConta, connect);

			/*
			 * Exclui movimento da conta
			 */
			int ret = MovimentoContaDAO.delete(cdMovimentoConta, cdConta, connect);

			if (ret <= 0) {
				com.tivic.manager.util.Util.registerLog(new Exception(
						"Erro ao excluir movimento de conta!"));
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1,
						"Erro ao tentar excluir movimento de conta! [ERRO: "
								+ ret + "]");
			}

			if (isConnectionNull)
				connect.commit();

			return new Result(ret);
		} catch (Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new Result(-1,
					"Erro desconhecido ao tentar excluir movimento!", e);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/*
	 * Confirmar movimento como compensando/conferido
	 */
	public static Result confirmarMovimento(int cdConta, int cdMovimentoConta) {
		return confirmarMovimento(cdConta, cdMovimentoConta, true,
				ST_COMPENSADO, null);
	}

	public static Result confirmarMovimento(int cdConta, int cdMovimentoConta,
			int stMovimento) {
		return confirmarMovimento(cdConta, cdMovimentoConta, true, stMovimento,
				null);
	}

	public static Result confirmarMovimento(int cdConta, int cdMovimentoConta,
			boolean atualizaEmCascata, int stMovimento, Connection connect) {
		boolean isConnectionNull = connect == null;
		Result ret = new Result(1);
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect
					.getAutoCommit());
			/*
			 * Verifica se o lançamento já foi conferido/concilicado
			 */
			ret.setCode(connect
					.prepareStatement(
							"UPDATE adm_movimento_conta "
									+ "SET st_movimento = " + stMovimento
									+ " WHERE cd_conta = " + cdConta
									+ "   AND cd_movimento_conta = "
									+ cdMovimentoConta).executeUpdate());
			ResultSet rs = connect.prepareStatement(
					"SELECT * FROM adm_movimento_conta " + " WHERE cd_conta = "
							+ cdConta + "   AND cd_movimento_conta = "
							+ cdMovimentoConta).executeQuery();
			if (ret.getCode() > 0 && rs.next()) {
				ret = ChequeServices.verificaSituacaoCheque(
						rs.getInt("cd_cheque"), ChequeServices.COMPENSADO,
						connect);
			}

			if (isConnectionNull && ret.getCode() > 0)
				connect.commit();
			else if (isConnectionNull) {
				Conexao.rollback(connect);
				return new Result(-1,
						"Não foi possível marcar o movimento como conferido/compensado!");
			}

			return new Result(1);
		} catch (Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err
			.println("Erro! MovimentoContaServices.confirmarMovimento: "
					+ e);
			return new Result(-1, "Não foi possível confirmar movimento!", e);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result estornarMovimento(int cdConta, int cdMovimentoConta,
			GregorianCalendar dtEstorno, int cdAlinea, String dsHistorico,
			int cdUsuario) {
		int cdEmpresa = EmpresaServices.getDefaultEmpresa().getCdEmpresa();
		return estornarMovimento(cdConta, cdMovimentoConta, dtEstorno,
				cdAlinea, dsHistorico, cdUsuario, cdEmpresa);
	}

	public static Result estornarMovimento(int cdConta, int cdMovimentoConta,
			GregorianCalendar dtEstorno, int cdAlinea, String dsHistorico,
			int cdUsuario, Connection connect) {
		int cdEmpresa = EmpresaServices.getDefaultEmpresa().getCdEmpresa();
		return estornarMovimento(cdConta, cdMovimentoConta, dtEstorno,
				cdAlinea, dsHistorico, cdUsuario, cdEmpresa, connect);
	}

	public static Result estornarMovimento(int cdConta, int cdMovimentoConta,
			GregorianCalendar dtEstorno, int cdAlinea, String dsHistorico,
			int cdUsuario, int cdEmpresa) {
		return estornarMovimento(cdConta, cdMovimentoConta, dtEstorno,
				cdAlinea, dsHistorico, cdUsuario, cdEmpresa, null);
	}

	public static Result estornarMovimento(int cdConta, int cdMovimentoConta,
			GregorianCalendar dtEstorno, int cdAlinea, String dsHistorico,
			int cdUsuario, int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect == null;
		Result ret = new Result(1);
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect
					.getAutoCommit());
			/*
			 * Verifica se o lançamento já foi conferido/concilicado/
			 */
			MovimentoConta movimentoConta = MovimentoContaDAO.get(cdMovimentoConta, cdConta);
			if (movimentoConta == null)
				throw new Exception(
						"Movimento de conta não localizado: cdConta: "
								+ cdConta + ", cdMovimentoConta: "
								+ cdMovimentoConta);
			// Pagamentos e Recebimentos
			ArrayList<Object> contas = new ArrayList<Object>();
			// Recebimentos
			ResultSet rsRec = connect
					.prepareStatement(
							"SELECT * FROM adm_movimento_conta_receber "
									+ " WHERE cd_conta = " + cdConta
									+ "   AND cd_movimento_conta = "
									+ cdMovimentoConta).executeQuery();
			while (rsRec.next()) {
				MovimentoContaReceber recebimento = MovimentoContaReceberDAO
						.get(cdConta, cdMovimentoConta, rsRec.getInt("cd_conta_receber"), connect);
				recebimento.setCdMovimentoConta(0);
				recebimento.setVlRecebido(-1 * recebimento.getVlRecebido());
				recebimento.setCdArquivo(0);
				recebimento.setCdRegistro(0);
				recebimento.setVlDesconto(0.0);
				recebimento.setVlMulta(0.0);
				recebimento.setVlJuros(0.0);
				recebimento.setVlTarifaCobranca(0.0);
				contas.add(recebimento);
			}
			// Pagamentos
			ResultSet rsPag = connect
					.prepareStatement(
							"SELECT * FROM adm_movimento_conta_pagar "
									+ " WHERE cd_conta = " + cdConta
									+ "   AND cd_movimento_conta = "
									+ cdMovimentoConta).executeQuery();
			while (rsPag.next()) {
				MovimentoContaPagar pagamento = MovimentoContaPagarDAO.get(
						cdConta, cdMovimentoConta,
						rsPag.getInt("cd_conta_pagar"), connect);
				pagamento.setCdMovimentoConta(0);
				pagamento.setVlPago(-1 * pagamento.getVlPago());
				pagamento.setVlDesconto(0.0);
				pagamento.setVlMulta(0.0);
				pagamento.setVlJuros(0.0);
				contas.add(pagamento);
			}
			/*
			 * Cria movimento de conta
			 */
			movimentoConta.setDtMovimento(dtEstorno);
			movimentoConta.setCdContaOrigem(cdConta);
			movimentoConta.setCdMovimentoConta(0);
			movimentoConta.setCdMovimentoOrigem(cdMovimentoConta);
			movimentoConta.setTpMovimento(movimentoConta.getTpMovimento() == CREDITO ? DEBITO : CREDITO);
			movimentoConta.setStMovimento(ST_CONFERIDO);
			movimentoConta.setCdUsuario(cdUsuario);
			movimentoConta.setDsHistorico(dsHistorico);
			movimentoConta.setIdExtrato(null);
			movimentoConta.setTpOrigem(MovimentoContaServices.toESTORNO);
			/*
			 * Categorias economicas
			 */
			ArrayList<MovimentoContaCategoria> movimentoCategorias = new ArrayList<MovimentoContaCategoria>();
			ResultSet rs = connect.prepareStatement(
					"SELECT * FROM adm_movimento_conta_categoria "
							+ "WHERE cd_conta = " + cdConta
							+ "  AND cd_movimento_conta = " + cdMovimentoConta)
					.executeQuery();

			int cdCategoriaReceitaEstorno = ParametroServices
					.getValorOfParametroAsInteger(
							"CD_CATEGORIA_RECEITA_ESTORNO", 0, cdEmpresa, connect);
			int cdCategoriaDespesaEstorno = ParametroServices
					.getValorOfParametroAsInteger(
							"CD_CATEGORIA_DESPESA_ESTORNO", 0, cdEmpresa, connect);

			while (rs.next()) {

				CategoriaEconomica catEcon = CategoriaEconomicaDAO.get(rs.getInt("cd_categoria_economica"), connect);
				int cdCategoriaMov = 0;
				if (catEcon.getTpCategoriaEconomica() == CategoriaEconomicaServices.TP_RECEITA
						|| catEcon.getTpCategoriaEconomica() == CategoriaEconomicaServices.TP_DEDUCAO_DESPESA)
					cdCategoriaMov = cdCategoriaReceitaEstorno;
				else
					cdCategoriaMov = cdCategoriaDespesaEstorno;

				if (cdCategoriaMov == 0) {
					return new Result(-1, "Categoria de Estorno não parametrizada!");
				}

				movimentoCategorias.add(new MovimentoContaCategoria(
						cdConta,
						0,
						cdCategoriaMov,
						rs.getDouble("vl_movimento_categoria"),
						0 /* cdMovimentoContaCategoria */,
						rs.getInt("cd_conta_pagar"),
						rs.getInt("cd_conta_receber"),
						MovimentoContaCategoriaServices.TP_PRE_CLASSIFICACAO /* tpMovimento */,
						rs.getInt("cd_centro_custo")));
			}
			// INSERINDO MOVIMENTO
			ret = insert(movimentoConta, contas, movimentoCategorias, false, connect);
			if (ret.getCode() > 0 && movimentoConta.getCdCheque() > 0)
				ret = ChequeServices.verificaSituacaoCheque( movimentoConta.getCdCheque(), ChequeServices.CANCELADO, connect);
			if (isConnectionNull && ret.getCode() > 0)
				connect.commit();
			else if (isConnectionNull) {
				Conexao.rollback(connect);
				return new Result(-1, "Não foi possível estornar o movimento!");
			}
			return ret;
		} catch (Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new Result(-1, "Não foi possível estornar movimento!", e);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result cancelarMovimento(int cdConta, int cdMovimentoConta,
			int cdUsuario) {
		return cancelarMovimento(cdConta, cdMovimentoConta, cdUsuario,
				ChequeServices.CANCELADO, null);
	}

	public static Result cancelarMovimento(int cdConta, int cdMovimentoConta,
			int cdUsuario, int stCheque, Connection connect) {
		boolean isConnectionNull = connect == null;
		Result ret = new Result(1);
		try {
			/*
			 * Verifica se o caixa está aberto para o usuário informado, se não
			 * há fechamento para o período e se o usuário tem permissão para o
			 * caixa
			 */
			Result result = ContaFinanceiraServices.isCaixaAbertoTo(cdConta,
					cdMovimentoConta, cdUsuario, 0, new GregorianCalendar(),
					connect);
			if (result.getCode() <= 0)
				return result;
			//
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect
					.getAutoCommit());
			/*
			 * Verifica se o lançamento já foi conferido/concilicado
			 */
			MovimentoConta movimentoConta = MovimentoContaDAO.get(
					cdMovimentoConta, cdConta);
			if (movimentoConta == null)
				return new Result(-1,
						"Movimento de conta não localizado: cdConta: "
								+ cdConta + ", cdMovimentoConta: "
								+ cdMovimentoConta);
			if (movimentoConta.getTpOrigem() == toTRANSFERENCIA)
				return new Result(-1,
						"Não é permitido cancelar uma transferência!");
			if (movimentoConta.getTpOrigem() == toESTORNO)
				return new Result(-1, "Não é permitido cancelar um estorno!");
			// Cancela movimento
			ret.setCode(connect
					.prepareStatement(
							"UPDATE adm_movimento_conta "
									+ "SET st_movimento = " + ST_CANCELADO
									+ " WHERE cd_conta = " + cdConta
									+ "   AND cd_movimento_conta = "
									+ cdMovimentoConta).executeUpdate());
			// Pagamentos
			ResultSetMap rsPag = new ResultSetMap(connect
					.prepareStatement(
							"SELECT * FROM adm_movimento_conta_pagar "
									+ " WHERE cd_conta = " + cdConta
									+ "   AND cd_movimento_conta = "
									+ cdMovimentoConta).executeQuery());
			while (rsPag.next())
				MovimentoContaPagarServices.setSituacaConta(
						rsPag.getInt("cd_conta_pagar"), connect);
			// Recebimentos
			ResultSetMap rsRec = new ResultSetMap(connect
					.prepareStatement(
							"SELECT * FROM adm_movimento_conta_receber "
									+ " WHERE cd_conta = " + cdConta
									+ "   AND cd_movimento_conta = "
									+ cdMovimentoConta).executeQuery());
			while (rsRec.next())
				MovimentoContaReceberServices.setSituacaConta(
						rsRec.getInt("cd_conta_receber"), Util.getDataAtual(),
						connect);

			// Cancelando cheque
			if (ret.getCode() > 0 && movimentoConta.getCdCheque() > 0
					&& stCheque >= 0)
				ret = ChequeServices.verificaSituacaoCheque(
						movimentoConta.getCdCheque(), stCheque, connect);

			if (isConnectionNull) {
				if (ret.getCode() > 0)
					connect.commit();
				else if (isConnectionNull) {
					Conexao.rollback(connect);

					return new Result(-1,
							"Não foi possível cancelar o movimento!");
				}
			}
			return ret;
		} catch (Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new Result(-1, "Não foi possível cancelar movimento!", e);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getDestinoAsResultSet(int cdConta, int cdMovimentoConta) {
		return getDestinoAsResultSet(cdConta, cdMovimentoConta, null);
	}
	
	public static ResultSetMap getDestinoAsResultSet(int cdConta,
			int cdMovimentoConta, Connection connection) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_conta_origem", Integer.toString(cdConta), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.cd_movimento_origem", Integer.toString(cdMovimentoConta), ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios, connection, null);
	}
	public static ResultSetMap getAsResultSet(int cdConta, int cdMovimentoConta) {
		return getAsResultSet(cdConta, cdMovimentoConta, null);
	}

	public static ResultSetMap getAsResultSet(int cdConta,
			int cdMovimentoConta, Connection connection) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_conta", Integer.toString(cdConta), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.cd_movimento_conta", Integer.toString(cdMovimentoConta), ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios, connection, null);
	}

	/**
	 * 
	 * Álvaro
	 * 
	 * Obtêm o total por forma de pagamento das movimentações associadas a um fechamento
	 * @param fechamento
	 * @return
	 */
	public static ResultSetMap getMovFechamentoByFormaPag(ContaFechamento fechamento) {
		return getMovFechamentoByFormaPag(fechamento, null);
	}

	public static ResultSetMap getMovFechamentoByFormaPag(ContaFechamento fechamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			GregorianCalendar  dtPosicao = fechamento.getDtFechamento();
			dtPosicao.set(Calendar.HOUR_OF_DAY, 23);
			dtPosicao.set(Calendar.MINUTE, 59);
			dtPosicao.set(Calendar.SECOND, 59);
			dtPosicao.set(Calendar.MILLISECOND, 59);

			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.cd_forma_pagamento, B.nm_forma_pagamento, CAST(0.00 AS FLOAT) AS vl_saldo_ant, " +
							"       (SELECT COUNT(*) FROM adm_movimento_conta A2" +
							"        WHERE A2.cd_conta = "+ fechamento.getCdConta() +
							//							"          AND A2.st_movimento IN ("+ MovimentoContaServices.ST_CX_FECHADO +") " +
							"          AND A2.tp_movimento = " +MovimentoContaServices.CREDITO+
							"          AND A2.cd_fechamento = " +fechamento.getCdFechamento()+
							//							"		   AND A2.dt_movimento <= ? " +
							"          AND (A2.cd_forma_pagamento = A.cd_forma_pagamento" +
							"           OR (A2.cd_forma_pagamento IS NULL AND A.cd_forma_pagamento IS NULL))) AS qt_credito, " +
							"       (SELECT SUM(vl_movimento) FROM adm_movimento_conta A2" +
							"        WHERE A2.cd_conta = "+fechamento.getCdConta()+
							//							"          AND A2.st_movimento IN ("+ MovimentoContaServices.ST_CX_FECHADO +") " +
							//							"		   AND A2.dt_movimento <= ? " +
							"          AND A2.cd_fechamento = " +fechamento.getCdFechamento()+
							"          AND A2.tp_movimento = " +MovimentoContaServices.CREDITO+
							"          AND (A2.cd_forma_pagamento = A.cd_forma_pagamento" +
							"           OR (A2.cd_forma_pagamento IS NULL AND A.cd_forma_pagamento IS NULL))) AS vl_credito, " +
							"       (SELECT COUNT(*) FROM adm_movimento_conta A2" +
							"        WHERE A2.cd_conta = "+fechamento.getCdConta()+
							//							"          AND A2.st_movimento IN ("+ MovimentoContaServices.ST_CX_FECHADO +") " +
							//							"		   AND A2.dt_movimento <= ? " +
							"          AND A2.cd_fechamento = " +fechamento.getCdFechamento()+
							"          AND A2.tp_movimento = " +MovimentoContaServices.DEBITO+
							"          AND (A2.cd_forma_pagamento = A.cd_forma_pagamento" +
							"           OR (A2.cd_forma_pagamento IS NULL AND A.cd_forma_pagamento IS NULL))) AS qt_debito, " +
							"       (SELECT SUM(vl_movimento) FROM adm_movimento_conta A2" +
							"        WHERE A2.cd_conta = "+fechamento.getCdConta()+
							//							"          AND A2.st_movimento IN ("+ MovimentoContaServices.ST_CX_FECHADO +") " +
							//							"		   AND A2.dt_movimento <= ? " +
							"          AND A2.cd_fechamento = " +fechamento.getCdFechamento()+
							"          AND A2.tp_movimento = " +MovimentoContaServices.DEBITO+
							"          AND (A2.cd_forma_pagamento = A.cd_forma_pagamento" +
							"           OR (A2.cd_forma_pagamento IS NULL AND A.cd_forma_pagamento IS NULL))) AS vl_debito " +
							"FROM adm_movimento_conta A " +
							"LEFT OUTER JOIN adm_forma_pagamento B ON (A.cd_forma_pagamento = B.cd_forma_pagamento) " +
							"WHERE A.cd_conta = "+fechamento.getCdConta()+
							//							"  AND A.st_movimento IN ("+ MovimentoContaServices.ST_CX_FECHADO +") " +
							"  AND A.cd_fechamento = " +fechamento.getCdFechamento()+"  "+
					"GROUP BY A.cd_forma_pagamento, B.nm_forma_pagamento");
			//			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtPosicao));
			//			pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtPosicao));
			//			pstmt.setTimestamp(3, Util.convCalendarToTimestamp(dtPosicao));
			//			pstmt.setTimestamp(4, Util.convCalendarToTimestamp(dtPosicao));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			pstmt = connect.prepareStatement("SELECT A.tp_movimento, A.cd_forma_pagamento, B.nm_forma_pagamento, SUM(A.vl_movimento) AS vl_total " +
					"FROM adm_movimento_conta A, adm_forma_pagamento B " +
					"WHERE A.cd_forma_pagamento = B.cd_forma_pagamento " +
					"  AND NOT A.cd_fechamento IS NULL " +
					"  AND A.cd_conta = ? " +
					"  AND CAST(A.dt_movimento AS DATE) <= (SELECT MAX(dt_fechamento) " +
					"						  FROM adm_conta_fechamento " +
					"						  WHERE cd_conta = ? " +
					"							AND cd_fechamento = ?)" +
					"GROUP BY A.tp_movimento, A.cd_forma_pagamento, B.nm_forma_pagamento");
			pstmt.setInt(1, fechamento.getCdConta());
			pstmt.setInt(2, fechamento.getCdConta());
			pstmt.setInt(3, fechamento.getCdFechamento());
			//			dtPosicao.set(Calendar.HOUR_OF_DAY, 0);
			//			dtPosicao.set(Calendar.MINUTE, 0);
			//			dtPosicao.set(Calendar.SECOND, 0);
			//			dtPosicao.set(Calendar.MILLISECOND, 0);
			//			pstmt.setTimestamp(3, Util.convCalendarToTimestamp(dtPosicao));
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				boolean locate = rsm.locate("cd_forma_pagamento", rs.getInt("cd_forma_pagamento"));
				HashMap<String, Object> register = !locate ? new HashMap<String, Object>() : rsm.getRegister();
				if (!locate) {
					register.put("CD_FORMA_PAGAMENTO", rs.getInt("cd_forma_pagamento"));
					register.put("NM_FORMA_PAGAMENTO", rs.getString("nm_forma_pagamento"));
					register.put("VL_CREDITO", 0.00);
					register.put("QT_CREDITO", 0);
					register.put("VL_DEBITO", 0.00);
					register.put("QT_DEBITO", 0);
					register.put("VL_SALDO_ANT", 0.00);
					rsm.addRegister(register);
				}
				register.put("VL_SALDO_ANT", ((Double)register.get("VL_SALDO_ANT")).doubleValue() +
						(rs.getInt("tp_movimento")==MovimentoContaServices.CREDITO ? 1 : -1) * rs.getFloat("vl_total"));
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraServices.getMovimentosNotFechadosByFormaPag: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * 
	 * Busca Movimentações vinculadas ao fechamento informado
	 * Fluxo Pax
	 * @param fechamento
	 * @param cdFormaPagamento
	 * @return
	 */
	public static final ResultSetMap getMovimentosFechamento(ContaFechamento fechamento) {
		return getMovimentosFechamento(fechamento, 0, null);
	}
	public static final ResultSetMap getMovimentosFechamento(ContaFechamento fechamento, int cdFormaPagamento) {
		return getMovimentosFechamento(fechamento, cdFormaPagamento, null);
	}
	public static final ResultSetMap getMovimentosFechamento(ContaFechamento fechamento, int cdFormaPagamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if( isConnectionNull ){
				connection = Conexao.conectar();
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_conta", Integer.toString(fechamento.getCdConta()), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.cd_fechamento", Integer .toString(fechamento.getCdFechamento()), ItemComparator.EQUAL, Types.INTEGER));
			if( cdFormaPagamento > 0 )
				criterios.add(new ItemComparator("A.cd_forma_pagamento", Integer.toString(cdFormaPagamento), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = find(criterios, null, null );
			ResultSet rs;
			if(rsm != null )
				rsm.beforeFirst();
			
			while( rsm != null && rsm.next() ){
				rs = connection.prepareStatement(" SELECT * FROM ADM_FECHAMENTO_OCORRENCIA "+
												 " WHERE cd_conta = "+rsm.getInt("CD_CONTA")+
												 " 		AND cd_movimento_conta = "+rsm.getInt("CD_MOVIMENTO_CONTA")+
												 " 		AND cd_fechamento = "+rsm.getInt("CD_FECHAMENTO")+
												 " ORDER BY DT_OCORRENCIA DESC "+
												 " LIMIT 1"
						).executeQuery();
				if( rs.next() ){
					rsm.setValueToField("LG_REPROVADO", true);
					rsm.setValueToField("DS_REPROVACAO", rs.getString("DS_OCORRENCIA"));
				}
			}
			return rsm;
		}catch(Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null, null);
	}

	public static final ResultSetMap getAllMovimentosNaoFechados(int cdConta,
			int cdFormaPagamento, GregorianCalendar dtFechamento) {
		dtFechamento = dtFechamento == null ? new GregorianCalendar()
				: dtFechamento;
		dtFechamento.set(Calendar.HOUR_OF_DAY, 23);
		dtFechamento.set(Calendar.MINUTE, 59);
		dtFechamento.set(Calendar.SECOND, 59);
		dtFechamento.set(Calendar.MILLISECOND, 999);
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_conta", Integer
				.toString(cdConta), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.dt_movimento", Util.formatDateTime(
				dtFechamento, "dd/MM/yyyy HH:mm:ss:SSS"),
				ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
		if (cdFormaPagamento > 0)
			criterios.add(new ItemComparator("A.cd_forma_pagamento", Integer
					.toString(cdFormaPagamento), ItemComparator.EQUAL,
					Types.INTEGER));
		criterios.add(new ItemComparator("A.st_movimento", Integer
				.toString(ST_CX_FECHADO), ItemComparator.DIFFERENT,
				Types.SMALLINT));
		return find(criterios);
	}

	public static ResultSetMap findTemp(
			ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null, "1 AND ((2 AND 3) OR 4) ");
	}

	public static ResultSetMap find(
			ArrayList<sol.dao.ItemComparator> criterios, Connection connection) {
		return find(criterios, connection, "1 AND ((2 AND 3) OR 4) ");
	}

	public static ResultSetMap find(
			ArrayList<sol.dao.ItemComparator> criterios, String relations) {
		return find(criterios, null, relations);
	}

	public static ResultSetMap find(
			ArrayList<sol.dao.ItemComparator> criterios, Connection connect,
			String relations) {

		for(int i=0; criterios!=null && i<criterios.size(); i++){
			if(criterios.get(i).getColumn().toUpperCase().indexOf("DT_MOVIMENTO")>=0)
				criterios.get(i).setColumn("CAST(A.dt_movimento AS DATE)");
			//
		}

		String sql = "SELECT A.*, B2.nm_conta, B2.nr_conta, B.nm_conta AS nm_conta_origem, B.nr_conta AS nr_conta_origem, B.tp_conta AS tp_conta_origem, "
				+ "C.nm_pessoa AS nm_usuario, U.nm_login, D.nr_cheque, D.st_cheque, D.dt_emissao, D.dt_liberacao, D.dt_impressao, "
				+ "D.id_talao, E.nm_forma_pagamento, E.tp_forma_pagamento, CAST(A.dt_movimento AS DATE) AS dt_movimento_conta, "
				+ "F.tp_movimento AS tp_movimento_origem, F.nr_documento AS nr_documento_origem, "
				+ "F.dt_movimento AS dt_movimento_origem, F.vl_movimento AS vl_movimento_origem, "
				+ "F.st_movimento AS st_movimento_origem "
				+ "FROM adm_movimento_conta A "
				+ "LEFT OUTER JOIN adm_conta_financeira	  B  ON (A.cd_conta_origem = B.cd_conta) "
				+ "LEFT OUTER JOIN adm_conta_financeira	  B2 ON (A.cd_conta = B2.cd_conta) "
				+ "LEFT OUTER JOIN adm_movimento_conta    F  ON (A.cd_movimento_origem = F.cd_movimento_conta AND "
				+ "											      A.cd_conta_origem = F.cd_conta) "
				+ "LEFT OUTER JOIN seg_usuario 			  U  ON (A.cd_usuario = U.cd_usuario) "
				+ "LEFT OUTER JOIN grl_pessoa 			  C  ON (U.cd_pessoa = C.cd_pessoa) "
				+ "LEFT OUTER JOIN adm_cheque 			  D  ON (A.cd_cheque = D.cd_cheque) "
				+ "LEFT OUTER JOIN adm_forma_pagamento 	  E  ON (A.cd_forma_pagamento = E.cd_forma_pagamento) ";
		ResultSetMap rsm;
		if (relations == null)
			rsm = Search
			.find(sql,
					"ORDER BY CAST(A.dt_movimento AS DATE), A.cd_movimento_conta",
					criterios,
					connect != null ? connect : Conexao.conectar(),
							connect == null);
		else
			rsm = Search
			.find(sql,
					"ORDER BY CAST(A.dt_movimento AS DATE), A.cd_movimento_conta",
					criterios, relations, connect != null ? connect
							: Conexao.conectar(), connect == null,
							false, true);
		return rsm;
	}

	/**
	 * @author Alvaro
	 * @param criterios
	 * @return ResultSetMap Movimentações
	 * 
	 */
	public static ResultSetMap findMovimentacao(ArrayList<ItemComparator> criterios){
		ResultSetMap movimentacao = new ResultSetMap();
		try{
			Object[] result = findMovimentacaoConta(criterios);
			movimentacao = (ResultSetMap) result[1];
			if( movimentacao.getLines().size() > 0 ){
				movimentacao.goTo(0);
				movimentacao.setValueToField("VL_SALDO_ANTERIOR", result[0]);
			}
		}catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraServices.findMovimentacao: "+ e);
		}
		return movimentacao;
	}
	public static Object[] findMovimentacaoConta(
			ArrayList<ItemComparator> criterios) {
		Connection connect = null;
		try {
			connect = Conexao.conectar();
			int cdConta = 0;
			int cdEmpresa = 0;
			GregorianCalendar dtInicial = null;
			int cdFormaPagamento = 0;
			for (int i = 0; criterios != null && i < criterios.size(); i++)
				if (criterios.get(i).getColumn().equalsIgnoreCase("A.cd_conta"))
					cdConta = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn()
						.equalsIgnoreCase("A.cd_forma_pagamento"))
					cdFormaPagamento = Integer.parseInt(criterios.get(i)
							.getValue());
				else if (criterios.get(i).getColumn()
						.equalsIgnoreCase("B1.cd_empresa"))
					cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn()
						.equalsIgnoreCase("A.dt_movimento")
						&& criterios.get(i).getTypeComparation() == ItemComparator.GREATER_EQUAL)
					dtInicial = Util.convStringToCalendar(criterios.get(i)
							.getValue());

			/*
			 * Saldo Inicial
			 */
			Double vlSaldoAnterior = dtInicial == null ? 0
					: ContaFinanceiraServices.getSaldoAnterior(cdEmpresa,
							cdConta, cdFormaPagamento, dtInicial, false,
							connect);
			/*
			 * Movimentação
			 */
			String sql = "SELECT A.*, B.nm_conta AS nm_conta_origem, B.nr_conta AS nr_conta_origem, "
					+ "       C.nm_pessoa AS nm_usuario, U.nm_login, D.nr_cheque, D.st_cheque, "
					+ "       E.nm_forma_pagamento, E.tp_forma_pagamento, "
					+ " 	  B1.dt_saldo_inicial,	B1.nr_conta, B1.nr_dv, B1.nm_conta "
					+ "FROM adm_movimento_conta A "
					+ "LEFT OUTER JOIN adm_conta_financeira	B1 ON (A.cd_conta = B1.cd_conta) "
					+ "LEFT OUTER JOIN adm_conta_financeira	B ON (A.cd_conta_origem = B.cd_conta) "
					+ "LEFT OUTER JOIN seg_usuario 			U ON (A.cd_usuario = U.cd_usuario) "
					+ "LEFT OUTER JOIN grl_pessoa 			C ON (U.cd_pessoa = C.cd_pessoa) "
					+ "LEFT OUTER JOIN adm_cheque 			D ON (A.cd_cheque = D.cd_cheque) "
					+ "LEFT OUTER JOIN adm_forma_pagamento 	E ON (A.cd_forma_pagamento = E.cd_forma_pagamento) "
					+ "WHERE 1 = 1 ";
			PreparedStatement favorecidos = connect
					.prepareStatement("SELECT nm_pessoa FROM grl_pessoa A, adm_conta_pagar B, adm_movimento_conta_pagar C "
							+ "WHERE A.cd_pessoa      = B.cd_pessoa"
							+ "  AND B.cd_conta_pagar = C.cd_conta_pagar"
							+ "  AND C.cd_conta           = ?"
							+ "  AND C.cd_movimento_conta = ?");
			PreparedStatement sacados = connect
					.prepareStatement("SELECT nm_pessoa FROM grl_pessoa A, adm_conta_receber B, adm_movimento_conta_receber C "
							+ "WHERE A.cd_pessoa        = B.cd_pessoa"
							+ "  AND B.cd_conta_receber = C.cd_conta_receber "
							+ "  AND C.cd_conta           = ?"
							+ "  AND C.cd_movimento_conta = ?");
			PreparedStatement destinos = connect
					.prepareStatement("SELECT nm_conta, nr_conta, nr_dv "
							+ "FROM adm_movimento_conta A "
							+ "JOIN adm_conta_financeira B ON (A.cd_conta = B.cd_conta) "
							+ "WHERE A.cd_conta_origem 	 = ?"
							+ "  AND A.cd_movimento_origem = ?");
			PreparedStatement contaPagar = connect
					.prepareStatement("SELECT nr_documento  "
							+ "FROM adm_movimento_conta_pagar A "
							+ "JOIN adm_conta_pagar B ON (A.cd_conta = B.cd_conta AND "
							+ "							  A.cd_conta_pagar = B.cd_conta_pagar ) "
							+ "WHERE A.cd_conta = ? "
							+ "AND A.cd_movimento_conta = ?  ");
			PreparedStatement contaReceber = connect
					.prepareStatement("SELECT nr_documento "
							+ "FROM adm_movimento_conta_receber A "
							+ "JOIN adm_conta_receber B ON (A.cd_conta = B.cd_conta AND "
							+ "							  A.cd_conta_receber = B.cd_conta_receber ) "
							+ "WHERE A.cd_conta = ? "
							+ "AND A.cd_movimento_conta = ?  ");
			ResultSetMap rsm = Search
					.find(sql,
							"ORDER BY CAST(A.dt_movimento AS DATE), A.cd_movimento_conta",
							criterios, connect, false, true);
			/*
			 * Buscando pagamentos e recebimentos
			 */
			while (rsm.next()) {
				String nmPessoa = "";
				// Favorecidos
				favorecidos.setInt(1, rsm.getInt("cd_conta"));
				favorecidos.setInt(2, rsm.getInt("cd_movimento_conta"));
				ResultSet rs = favorecidos.executeQuery();
				while (rs.next())
					nmPessoa += (!nmPessoa.equals("") ? "," : "")
					+ rs.getString("nm_pessoa");
				// Sacado
				sacados.setInt(1, rsm.getInt("cd_conta"));
				sacados.setInt(2, rsm.getInt("cd_movimento_conta"));
				rs = sacados.executeQuery();
				while (rs.next())
					nmPessoa += (!nmPessoa.equals("") ? "," : "")
					+ rs.getString("nm_pessoa");
				rsm.setValueToField("NM_PESSOA", nmPessoa);
				if (rsm.getInt("tp_origem") == toTRANSFERENCIA
						&& rsm.getInt("tp_movimento") == DEBITO) {
					destinos.setInt(1, rsm.getInt("cd_conta"));
					destinos.setInt(2, rsm.getInt("cd_movimento_conta"));
					rs = destinos.executeQuery();
					String nmConta = "";
					while (rs.next())
						nmConta += (!nmPessoa.equals("") ? "," : "")
						+ rs.getString("nm_conta");
					rsm.setValueToField("NM_CONTA_DESTINO", nmConta);
				}
				//CONTA PAGAR OU RECEBER - INCLUIR NÚMERO DE DOCUMENTO DA CONTA
				if( rsm.getInt("tp_movimento") == DEBITO  ){
					contaPagar.setInt(1, rsm.getInt("cd_conta"));
					contaPagar.setInt(2, rsm.getInt("cd_movimento_conta"));
					ResultSet rsContaPagar = contaPagar.executeQuery();
					while(rsContaPagar.next())
						rsm.setValueToField("NR_DOCUMENTO_CONTA", rsContaPagar.getString("NR_DOCUMENTO"));
				}else{
					contaReceber.setInt(1, rsm.getInt("cd_conta"));
					contaReceber.setInt(2, rsm.getInt("cd_movimento_conta"));
					ResultSet rsContaReceber = contaReceber.executeQuery();
					while(rsContaReceber.next())
						rsm.setValueToField("NR_DOCUMENTO_CONTA", rsContaReceber.getString("NR_DOCUMENTO"));
				}
			}
			rsm.beforeFirst();
			/*
			 * Sumário por forma de pagamento
			 */
			ResultSetMap rsmSumario = TituloCreditoServices.getSaldoEmTitulo(
					cdConta, dtInicial, connect);
			/**
			 * PreparedStatement pstmt = connect.prepareStatement(
			 * "SELECT A.cd_forma_pagamento, nm_forma_pagamento, tp_forma_pagamento, st_movimento, tp_movimento, "
			 * + "       SUM(vl_movimento) AS vl_total " +
			 * "FROM adm_forma_pagamento A " +
			 * "LEFT OUTER JOIN adm_movimento_conta B ON (A.cd_forma_pagamento = B.cd_forma_pagamento"
			 * +
			 * "                                      AND cd_conta = "+cdConta+
			 * "  									  AND st_movimento IN ("
			 * +ST_NAO_COMPENSADO+","+ST_COMPENSADO+","+ST_CONCILIADO+")" +
			 * (dtInicial!=null ? "                   AND dt_movimento >= ? " :
			 * "") + (dtFinal!=null ? "					  AND dt_movimento <= ? " : "") +
			 * ") " +
			 * "GROUP BY A.cd_forma_pagamento, nm_forma_pagamento, tp_forma_pagamento, st_movimento, tp_movimento"
			 * ); int i = 1; if (dtInicial!=null) pstmt.setTimestamp(i++, new
			 * Timestamp(dtInicial.getTimeInMillis())); if (dtFinal!=null)
			 * pstmt.setTimestamp(i++, new
			 * Timestamp(dtFinal.getTimeInMillis())); ResultSet rs =
			 * pstmt.executeQuery(); while(rs.next()) { HashMap<String,Object>
			 * register; if(!rsmSumario.locate("cd_forma_pagamento", new
			 * Integer(rs.getInt("cd_forma_pagamento")))) { float vlSaldoInicial
			 * = dtInicial==null ? 0 :
			 * ContaFinanceiraServices.getSaldoAnterior(0, cdConta,
			 * rs.getInt("cd_forma_pagamento"), dtInicial, connect); register =
			 * new HashMap<String,Object>(); register.put("CD_FORMA_PAGAMENTO",
			 * rs.getInt("cd_forma_pagamento"));
			 * register.put("NM_FORMA_PAGAMENTO",
			 * rs.getString("nm_forma_pagamento"));
			 * register.put("TP_FORMA_PAGAMENTO",
			 * rs.getInt("tp_forma_pagamento"));
			 * register.put("VL_SALDO_INICIAL", new Float(vlSaldoInicial));
			 * register.put("VL_CREDITO", new Float(0));
			 * register.put("VL_DEBITO", new Float(0));
			 * register.put("VL_CREDITO_NC", new Float(0));
			 * register.put("VL_DEBITO_NC", new Float(0));
			 * rsmSumario.addRegister(register); } else register =
			 * rsmSumario.getRegister(); String nmField =
			 * rs.getInt("st_movimento"
			 * )==ST_NAO_COMPENSADO?"VL_CREDITO_NC":"VL_CREDITO";
			 * if(rs.getInt("tp_movimento")!=MovimentoContaServices.CREDITO)
			 * nmField =
			 * rs.getInt("st_movimento")==ST_NAO_COMPENSADO?"VL_DEBITO_NC"
			 * :"VL_DEBITO"; float vlAnterior = register.get(nmField)!=null ?
			 * (Float)register.get(nmField) : 0; register.put(nmField, new
			 * Float(vlAnterior+rs.getFloat("vl_total"))); }
			 * rsmSumario.beforeFirst(); while(rsmSumario.next()) { float
			 * vlSaldoTotal = rsmSumario.getFloat("vl_saldo_inicial") +
			 * rsmSumario.getFloat("vl_credito_nc") +
			 * rsmSumario.getFloat("vl_debito_nc") +
			 * rsmSumario.getFloat("vl_credito") +
			 * rsmSumario.getFloat("vl_debito"); if(Math.abs(vlSaldoTotal) <
			 * 0.01) { rsmSumario.removeRegister(); rsmSumario.beforeFirst(); }
			 * } rsmSumario.beforeFirst();
			 **/
			return new Object[] { vlSaldoAnterior, rsm, rsmSumario };
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraServices.findMovimentacaoConta: "
					+ e);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getMovimentacaoConta(int cdConta,
			GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int cdFormaPagamento, int stMovimento, boolean isPDV) {
		Connection connect = Conexao.conectar();
		ResultSetMap rsm = null;
		try {
			rsm = (ResultSetMap) getMovimentacaoConta(cdConta, dtInicial,
					dtFinal, cdFormaPagamento, stMovimento, -1, -1, null)[1];
			if (isPDV) {
				PreparedStatement pstmtFromTitulo = connect
						.prepareStatement("SELECT D.cd_pessoa, D.nm_pessoa "
								+ "FROM adm_movimento_titulo_credito A "
								+ "JOIN adm_titulo_credito   A1 ON (A1.cd_titulo_credito = A.cd_titulo_credito) "
								+ "JOIN adm_conta_receber    B ON (A1.cd_conta_receber   = B.cd_conta_receber) "
								+ "JOIN alm_documento_saida  C ON (B.cd_documento_saida = C.cd_documento_saida) "
								+ "JOIN grl_pessoa           D ON (C.cd_vendedor        = D.cd_pessoa) "
								+ "WHERE A.cd_conta         = ? "
								+ "  AND cd_movimento_conta = ? ");
				PreparedStatement pstmt = connect
						.prepareStatement("SELECT D.cd_pessoa, D.nm_pessoa "
								+ "FROM adm_movimento_conta_receber A "
								+ "JOIN adm_conta_receber   B ON (A.cd_conta_receber   = B.cd_conta_receber) "
								+ "JOIN alm_documento_saida C ON (B.cd_documento_saida = C.cd_documento_saida) "
								+ "JOIN grl_pessoa          D ON (C.cd_vendedor        = D.cd_pessoa) "
								+ "WHERE A.cd_conta         = ? "
								+ "  AND cd_movimento_conta = ? ");
				while (rsm.next()) {
					pstmt.setInt(1, rsm.getInt("cd_conta"));
					pstmt.setInt(2, rsm.getInt("cd_movimento_conta"));
					ResultSetMap rsmVendedor = new ResultSetMap(
							pstmt.executeQuery());
					if (rsmVendedor.next()) {
						rsm.setValueToField("CD_VENDEDOR",
								rsmVendedor.getInt("cd_pessoa"));
						rsm.setValueToField("NM_VENDEDOR",
								rsmVendedor.getString("nm_pessoa"));
					}
					// Se não gerou recebimento tenta encontrar pelos títulos de
					// crédito
					else if (rsm.getInt("tp_movimento") == CREDITO) {
						pstmtFromTitulo.setInt(1, rsm.getInt("cd_conta"));
						pstmtFromTitulo.setInt(2,
								rsm.getInt("cd_movimento_conta"));
						rsmVendedor = new ResultSetMap(
								pstmtFromTitulo.executeQuery());
						if (rsmVendedor.next()) {
							rsm.setValueToField("CD_VENDEDOR",
									rsmVendedor.getInt("cd_pessoa"));
							rsm.setValueToField("NM_VENDEDOR",
									rsmVendedor.getString("nm_pessoa"));
						}
					}
				}
				rsm.beforeFirst();
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
		}
		return rsm;
	}

	public static Object[] getMovimentacaoConta(int cdConta,
			GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int cdFormaPagamento, int stMovimento, String nrDocumento) {
		return getMovimentacaoConta(cdConta, dtInicial, dtFinal,
				cdFormaPagamento, stMovimento, -1, -1, nrDocumento);
	}
	/**
	 * @author Alvaro
	 * Método criado para encapsular a chamada para a janela em Flex, ignorando os argumentos não utilizados
	 *  na original em jsp
	 */
	public static Result getMovimentacaoConta(int cdEmpresa, int cdConta, GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			String nrDocumento, ArrayList<ItemComparator> criterios ){
		Result result = new Result(-1);
		try{
			Object[] movimentacao = getMovimentacaoConta( cdEmpresa, cdConta, dtInicial, dtFinal, -1, -1, -1, -1, nrDocumento, criterios);
			result = new Result( 1, "", "MOVIMENTACAO", movimentacao );
		}catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraServices.getMovimentacaoConta: "+ e);
			result.setMessage("Erro ao buscar movimentação");
		}
		return result;
	}
	public static Object[] getMovimentacaoConta(int cdConta,
			GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int cdFormaPagamento, int stMovimento, int tpMovimento,
			int tpOrigem, String nrDocumento){
		return getMovimentacaoConta(0, cdConta, dtInicial, dtFinal, cdFormaPagamento, stMovimento, tpMovimento, tpOrigem, nrDocumento, null);
	}
	public static Object[] getMovimentacaoConta(int cdEmpresa, int cdConta,
			GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int cdFormaPagamento, int stMovimento, int tpMovimento,
			int tpOrigem, String nrDocumento, ArrayList<ItemComparator> criterios) {
		Connection connect = Conexao.conectar();
		try {
			// Colocando a hora da data inicial para as primeiras horas do dia
			dtInicial.set(Calendar.HOUR, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtInicial.set(Calendar.SECOND, 0);
			// Colocando a hora da data final para as ultimas horas do dia
			dtFinal.set(Calendar.HOUR, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			dtFinal.set(Calendar.SECOND, 59);
			/*
			 * Saldo Inicial
			 */
			int cdFormaDinheiro = 0;
			// ResultSet rs =
			// connect.prepareStatement("SELECT * FROM adm_forma_pagamento WHERE tp_forma_pagamento = "+FormaPagamentoServices.MOEDA_CORRENTE).executeQuery();
			// if(rs.next())
			// cdFormaDinheiro = rs.getInt("cd_forma_pagamento");
			Double vlSaldoAnterior = ContaFinanceiraServices.getSaldoAnterior(cdEmpresa,
					cdConta, cdFormaDinheiro, dtInicial, false, connect);


			Double vlMovimentoInicial = 0.0;
			Double vlMovimentoFinal = 0.0;
			String nrDocConta = "";
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("VL_MOVIMENTO_INICIAL")){
					vlMovimentoInicial = Double.valueOf(criterios.get(i).getValue());			
				}else if(((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("VL_MOVIMENTO_FINAL")){
					vlMovimentoFinal = Double.valueOf(criterios.get(i).getValue());			
				}else if(((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("NR_DOC_CONTA")){
					nrDocConta = criterios.get(i).getValue();			
				}
			}

			/*
			 * Movimentação
			 */
			String sql = "SELECT A.*, B.tp_conta, B.nm_conta, B.nr_conta, "
					+ "       C.nm_pessoa AS nm_usuario, U.nm_login, "
					+ "       D.cd_cheque, D.nr_cheque, D.st_cheque, D.id_talao, D.dt_emissao as dt_emissao_cheque, "
					+ "		  D.dt_liberacao as dt_liberacao_cheque, D.dt_impressao as dt_impressao_cheque,    "
					+ "       E.nm_forma_pagamento, E.tp_forma_pagamento, F.nm_turno,"
					+ "       G.nr_documento AS nr_documento_origem,  "
					+ "       B2.nm_conta AS nm_conta_origem, B2.nr_conta AS nr_conta_origem, B2.tp_conta AS tp_conta_origem "
					+ " FROM adm_movimento_conta A "
					+ " LEFT OUTER JOIN adm_conta_financeira	 B ON (A.cd_conta = B.cd_conta) "
					+ " LEFT OUTER JOIN adm_conta_financeira	 B2 ON (A.cd_conta_origem = B2.cd_conta) "
					+ " LEFT OUTER JOIN seg_usuario 			 U ON (A.cd_usuario = U.cd_usuario) "
					+ " LEFT OUTER JOIN grl_pessoa 			     C ON (U.cd_pessoa = C.cd_pessoa) "
					+ " LEFT OUTER JOIN adm_cheque 			     D ON (A.cd_cheque = D.cd_cheque) "
					+ " LEFT OUTER JOIN adm_forma_pagamento 	 E ON (A.cd_forma_pagamento = E.cd_forma_pagamento) "
					+ " LEFT OUTER JOIN adm_turno                F ON (A.cd_turno = F.cd_turno) "
					+ " LEFT OUTER JOIN adm_movimento_conta	     G ON (A.cd_movimento_origem = G.cd_movimento_conta and A.cd_conta_origem = G.cd_conta) "
					+ " WHERE A.cd_conta = "+ cdConta
					+ "  AND (A.dt_movimento BETWEEN ? AND ? "
					//					+ "  OR (A.cd_cheque IS NOT NULL AND A.st_movimento = "+ ST_NAO_COMPENSADO + ") "
					//					+ (nrDocumento != null && !nrDocumento.equals("") ? " OR nr_documento = \'"+ nrDocumento.trim() + "\'": "")
					+ ") "
					+ (nrDocumento.length() > 0 ? " AND A.nr_documento ilike '%"+nrDocumento.trim()+"%'":"")
					+ (nrDocConta.length() > 0 ? " AND A.nr_doc_conta ilike '%"+nrDocConta.trim()+"%'":"")
					+ (cdFormaPagamento > 0 ? " AND A.cd_forma_pagamento = "+ cdFormaPagamento : "")
					+ (stMovimento >= 0 ? " AND A.st_movimento           = " + stMovimento : "")
					+ (tpOrigem >= 0 ? " AND A.tp_origem                 = "+ tpOrigem : "")
					+ (tpMovimento >= 0 ? " AND A.tp_movimento           = "+ tpMovimento : "")
					+ (vlMovimentoInicial > 0 ? " AND A.vl_movimento   >= ?" : "")
					+ (vlMovimentoFinal > 0 ? " AND A.vl_movimento   <= ?" : "")
					+ " ORDER BY A.dt_movimento, A.cd_movimento_conta";
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			int qtArgs = 2;
			if( vlMovimentoInicial > 0 )
				pstmt.setDouble(++qtArgs, vlMovimentoInicial );
			if( vlMovimentoFinal > 0 )
				pstmt.setDouble(++qtArgs, vlMovimentoFinal );

			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			PreparedStatement favorecidos = connect
					.prepareStatement("SELECT nm_pessoa FROM grl_pessoa A, adm_conta_pagar B, adm_movimento_conta_pagar C "
							+ "WHERE A.cd_pessoa      = B.cd_pessoa"
							+ "  AND B.cd_conta_pagar = C.cd_conta_pagar"
							+ "  AND C.cd_conta           = "
							+ cdConta
							+ "  AND C.cd_movimento_conta = ?");
			PreparedStatement sacados = connect
					.prepareStatement("SELECT nm_pessoa FROM grl_pessoa A, adm_conta_receber B, adm_movimento_conta_receber C "
							+ "WHERE A.cd_pessoa        = B.cd_pessoa"
							+ "  AND B.cd_conta_receber = C.cd_conta_receber "
							+ "  AND C.cd_conta           = "
							+ cdConta
							+ "  AND C.cd_movimento_conta = ?");
			PreparedStatement destinos = connect
					.prepareStatement("SELECT nm_conta, nr_conta, nr_dv "
							+ "FROM adm_movimento_conta A "
							+ "JOIN adm_conta_financeira B ON (A.cd_conta = B.cd_conta) "
							+ "WHERE A.cd_conta_origem 	 = ?"
							+ "  AND A.cd_movimento_origem = ?");
			/*
			 * Buscando pagamentos e recebimentos
			 */
			while (rsm.next()) {
				String nmPessoa = "";
				// Favorecidos
				favorecidos.setInt(1, rsm.getInt("cd_movimento_conta"));
				ResultSet rs = favorecidos.executeQuery();
				while (rs.next())
					nmPessoa += (!nmPessoa.equals("") ? "," : "")
					+ rs.getString("nm_pessoa");
				// Sacado
				sacados.setInt(1, rsm.getInt("cd_movimento_conta"));
				rs = sacados.executeQuery();
				while (rs.next())
					nmPessoa += (!nmPessoa.equals("") ? "," : "")
					+ rs.getString("nm_pessoa");
				rsm.setValueToField("NM_PESSOA", nmPessoa);
				if (rsm.getInt("tp_origem") == toTRANSFERENCIA
						&& rsm.getInt("tp_movimento") == DEBITO) {
					destinos.setInt(1, rsm.getInt("cd_conta"));
					destinos.setInt(2, rsm.getInt("cd_movimento_conta"));
					rs = destinos.executeQuery();
					String nmConta = "";
					while (rs.next())
						nmConta += (!nmConta.equals("") ? "," : "")
						+ rs.getString("nm_conta");
					rsm.setValueToField("NM_CONTA_DESTINO", nmConta);
				}
			}
			rsm.beforeFirst();

			//Calculando valores compensados/não compensados  e separando creditos de debitos
			//Deb. Cred. não compensado
			int qtCreditoNC=0, qtDebitoNC=0, qtCredito=0, qtDebito=0;
			BigDecimal vlCreditoNC = new BigDecimal(0);
			BigDecimal vlDebitoNC  = new BigDecimal(0);
			BigDecimal vlSaldo = new BigDecimal(vlSaldoAnterior);
			BigDecimal vlCredito = new BigDecimal(0);
			BigDecimal vlDebito = new BigDecimal(0);
			ContaFinanceira contaFinanceira = ContaFinanceiraDAO.get(cdConta);
			while( rsm.next() ){

				//Calcula valores de creditos/debitos
				if( rsm.getInt("TP_MOVIMENTO") == 0 ){
					rsm.setValueToField("VL_CREDITO", rsm.getDouble("VL_MOVIMENTO"));
					rsm.setValueToField("VL_DEBITO", 0);
				}
				else{
					rsm.setValueToField("VL_DEBITO", rsm.getDouble("VL_MOVIMENTO"));
					rsm.setValueToField("VL_CREDITO", 0);
				}

				//Ignora movimentações anteriores a data inicial da conta financeira
				if( contaFinanceira.getDtSaldoInicial() != null
						&& Util.compareDates(rsm.getGregorianCalendar("DT_MOVIMENTO"), contaFinanceira.getDtSaldoInicial() ) <= 0 ){
					rsm.setValueToField("DS_SALDO", "Movimento anterior a data de abertura da Conta Financeira!");
					rsm.setValueToField("LG_MOVIMENTO_IGNORADO", true);
					rsm.setValueToField("VL_SALDO", "0");
					continue;
				}	
				//Movimentações não compensadas
				if( rsm.getInt("ST_MOVIMENTO") == MovimentoContaServices.ST_CANCELADO ){
					rsm.setValueToField("DS_SALDO", "Movimento 'cancelado'  não computa saldo.");
					rsm.setValueToField("VL_SALDO", "0");	
				}else if( rsm.getInt("ST_MOVIMENTO") == MovimentoContaServices.ST_NAO_COMPENSADO ){
					rsm.setValueToField("DS_SALDO", "Movimento 'não compensado'  não computa saldo.");
					if( rsm.getInt("TP_MOVIMENTO") == 0 ){
						vlCreditoNC = vlCreditoNC.add(new BigDecimal(rsm.getString("VL_MOVIMENTO")));
						qtCreditoNC++;
					}else{
						vlDebitoNC = vlDebitoNC.add(new BigDecimal(rsm.getString("VL_MOVIMENTO")));
						qtDebitoNC++;

					}
					rsm.setValueToField("VL_SALDO", "0");	

					//Movimentações compensadas ou conciliadas 
				}else if( rsm.getInt("ST_MOVIMENTO") == MovimentoContaServices.ST_COMPENSADO  || rsm.getInt("ST_MOVIMENTO") == MovimentoContaServices.ST_CONCILIADO ){
					//Atualizando valor do saldo
					if(rsm.getInt("TP_MOVIMENTO") == 0){
						vlSaldo = vlSaldo.add( new BigDecimal(rsm.getString("VL_MOVIMENTO"))); 
						vlCredito = vlCredito.add(new BigDecimal(rsm.getString("VL_MOVIMENTO"))); 
						qtCredito++;
					}else{
						vlSaldo = vlSaldo.subtract( new BigDecimal(rsm.getString("VL_MOVIMENTO"))); 
						vlDebito =  vlDebito.add(new BigDecimal(rsm.getString("VL_MOVIMENTO"))); 
						qtDebito++;
					}
					rsm.setValueToField("VL_SALDO", vlSaldo.doubleValue());
				}
			}	
			rsm.beforeFirst();
			if( rsm.next() ){
				rsm.setValueToField("VL_CREDITO_TOTAL", vlCredito);
				rsm.setValueToField("QT_CREDITOS", qtCredito);
				rsm.setValueToField("VL_CREDITO_NC", vlCreditoNC );
				rsm.setValueToField("QT_CREDITOS_NC", qtCreditoNC );

				rsm.setValueToField("VL_DEBITO_TOTAL", vlDebito);
				rsm.setValueToField("QT_DEBITOS", qtDebito);
				rsm.setValueToField("VL_DEBITO_NC", vlDebitoNC );
				rsm.setValueToField("QT_DEBITOS_NC", qtDebitoNC );

				rsm.setValueToField("VL_SALDO_ATUAL", new BigDecimal(vlSaldoAnterior).add(vlCredito).subtract( vlDebito ) );
				rsm.setValueToField("VL_SALDO_PREVISTO", new BigDecimal(vlSaldoAnterior)
																			.add(vlCredito).subtract( vlDebito )
																			.add(vlCreditoNC).subtract( vlDebitoNC ) );
			}

			return new Object[] {
					vlSaldoAnterior,
					rsm,
					TituloCreditoServices.getSaldoEmTitulo(cdConta, dtInicial,	connect) /* rsmSumario */};
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err
			.println("Erro! ContaFinanceiraServices.getMovimentacaoConta: "
					+ e);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * 
	 * @param cdConta
	 * @param tpMovimento
	 * @param nrDocumento
	 * @param vlMovimento
	 * @param dtMovimento
	 * @return
	 */
	public static ResultSetMap getMovimentosByExtrato(int cdConta, int tpMovimento, String nrDocumento, Double vlMovimento, GregorianCalendar dtMovimento) {
		Connection connect = Conexao.conectar();
		try {
			PreparedStatement pstmt = connect
					.prepareStatement("SELECT * FROM adm_movimento_conta "
							+ "WHERE cd_conta = " + cdConta
							+ "  AND id_extrato IS NULL "
							+ "  AND st_movimento IN (" + ST_COMPENSADO + ","+ ST_NAO_COMPENSADO + ") "
							+ "  AND tp_movimento = " + tpMovimento
							+ "  AND dt_movimento = ? "
							+ "  AND vl_movimento >= ? "
							+ "  AND vl_movimento <= ? ");
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			pstmt.setDouble(2, vlMovimento - 0.01);
			pstmt.setDouble(3, vlMovimento + 0.01);
			
			return new ResultSetMap(pstmt.executeQuery());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err
			.println("Erro! ContaFinanceiraServices.getMovimentoByExtrato: "
					+ e);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getMovimentoByExtrato(int cdConta,
			int tpMovimento, String nrDocumento, float vlMovimento) {
		Connection connect = Conexao.conectar();
		try {
			PreparedStatement pstmt = connect
					.prepareStatement("SELECT * FROM adm_movimento_conta "
							+ "WHERE cd_conta = " + cdConta
							+ "  AND id_extrato IS NULL "
							+ "  AND st_movimento IN (" + ST_COMPENSADO + ","
							+ ST_NAO_COMPENSADO + ") "
							+ "  AND tp_movimento = " + tpMovimento
							+ "  AND nr_documento = \'" + nrDocumento + "\'");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			// Se encontrou algum registro com o mesmo número de documento
			if (rsm.next()) {
				rsm.beforeFirst();
				return rsm;
			}
			// Tenta encontrar algum com valor igual
			pstmt = connect
					.prepareStatement("SELECT * FROM adm_movimento_conta "
							+ "WHERE cd_conta = " + cdConta
							+ "  AND id_extrato IS NULL "
							+ "  AND st_movimento IN (" + ST_COMPENSADO + ","
							+ ST_NAO_COMPENSADO + ") "
							+ "  AND tp_movimento = " + tpMovimento
							+ "  AND vl_movimento = " + vlMovimento);
			return new ResultSetMap(pstmt.executeQuery());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err
			.println("Erro! ContaFinanceiraServices.getMovimentoByExtrato: "
					+ e);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	public static int saveMovimentoConciliacao( MovimentoConta movimentoConta, String idExtrato,
						Object conta, Object categoria ) {
		return saveMovimentoConciliacao(movimentoConta, idExtrato, conta, categoria, null ).getCode();
	}
	
	public static Result saveMovimentoConciliacao( MovimentoConta movimentoConta, String idExtrato,
			Object conta, Object categoria, Connection connect ) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull){
			connect = Conexao.conectar();
		}
		int retorno = 0;
		try {
			connect.setAutoCommit(false);	
			
			ArrayList<Object> movContas = new ArrayList<Object>();
			Result result;
			//salva conta
			if( conta instanceof ContaPagar ){
				ArrayList<ContaPagarCategoria> categoriasApagar = new ArrayList<ContaPagarCategoria>();
				categoriasApagar.add((ContaPagarCategoria)categoria);
				result = ContaPagarServices.save((ContaPagar)conta, true/*ignorarDuplicidade*/,
									false/*repetirEmissao*/, false/*atualizarProximas*/,
									categoriasApagar/*categorias*/, connect);
				if( result.getCode() <=0 ){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao cadastrar conta a pagar da nova movimentação.");
				}
				((ContaPagar)conta).setCdContaPagar( result.getCode() );
				MovimentoContaPagar movContaPagar = new MovimentoContaPagar();
				movContaPagar.setCdConta( movimentoConta.getCdConta() );
				movContaPagar.setCdContaPagar( result.getCode() );
				movContaPagar.setVlPago( movimentoConta.getVlMovimento() );
						
				movContas.add(movContaPagar);
			}else{
				ArrayList<ContaReceberCategoria> categoriasAreceber = new ArrayList<ContaReceberCategoria>();
				categoriasAreceber.add((ContaReceberCategoria)categoria);
				result = ContaReceberServices.save((ContaReceber)conta, null/*titulo*/, categoriasAreceber, connect);
				if( result.getCode() <=0 ){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao cadastrar conta a receber da nova movimentação.");
				}
				((ContaReceber)conta).setCdContaReceber( result.getCode() );
				MovimentoContaReceber movContaReceber = new MovimentoContaReceber();
				movContaReceber.setCdConta( movimentoConta.getCdConta() );
				movContaReceber.setCdContaReceber( result.getCode() );
				movContaReceber.setVlRecebido( movimentoConta.getVlMovimento() );
						
				movContas.add(movContaReceber);
			}
			
			//salva movimentação
			ArrayList<MovimentoContaCategoria> categorias = new ArrayList<MovimentoContaCategoria>();
			MovimentoContaCategoria movCategoria = new MovimentoContaCategoria();
			movCategoria.setCdConta( movimentoConta.getCdConta() );
			movCategoria.setTpMovimento( movimentoConta.getTpMovimento() );
			movCategoria.setVlMovimentoCategoria( movimentoConta.getVlMovimento() );
			if( conta instanceof ContaPagar ){
				movCategoria.setCdCategoriaEconomica( ((ContaPagarCategoria)categoria).getCdCategoriaEconomica() );
				movCategoria.setCdCentroCusto( ((ContaPagarCategoria)categoria).getCdCentroCusto() );
				movCategoria.setCdContaPagar( ((ContaPagar)conta).getCdContaPagar() );
			}else{
				movCategoria.setCdCategoriaEconomica( ((ContaReceberCategoria)categoria).getCdCategoriaEconomica() );
				movCategoria.setCdCentroCusto( ((ContaReceberCategoria)categoria).getCdCentroCusto() );
				movCategoria.setCdContaPagar( ((ContaReceber)conta).getCdContaReceber() );
			}
			categorias.add(movCategoria);
			
			result = save(movimentoConta, movContas, categorias, null/*titulos*/, -1/*stCheque*/, connect);
			if( result.getCode() <= 0 ){
				if( result.getCode() <=0 ){
					if(isConnectionNull)
						Conexao.rollback(connect);
					System.err.println("Erro! MovimentoContaServices.saveMovimentoConciliacao: "+ result.getMessage());
					return new Result(-1, "Erro lançar nova movimentação.");
				}
			}
			//concilia movimento
			retorno = setConciliacao( movimentoConta.getCdConta(), movimentoConta.getCdMovimentoConta(),
							movimentoConta.getVlMovimento(),  movimentoConta.getDtMovimento(),
							idExtrato, movimentoConta.getNrDocumento(),
							movimentoConta.getDsHistorico(), movimentoConta.getDtMovimento(), connect);
			
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "MOVIMENTOCONTA", movimentoConta);
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaServices.saveMovimentoConciliacao: "+ e);
			return new Result(-1, "Erro ao registrar nova movimentação!", e);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getFluxoDeCaixa(int cdEmpresa, int tpConta,
			int cdConta, int tpOrigem, int nrDiasIntervalo,
			GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			boolean onlyWithMovimento, boolean groupByConta, boolean showDetalhe){
		Integer contas[] = (cdConta>0)?new Integer[cdConta]:new Integer[]{};
		return getFluxoDeCaixa(cdEmpresa, tpConta, contas, tpOrigem, nrDiasIntervalo,
				dtInicial, dtFinal, onlyWithMovimento, groupByConta,
				showDetalhe);
	}
	
	public static ResultSetMap getFluxoDeCaixa(int cdEmpresa, int tpConta,
			Integer contas[], int tpOrigem, int nrDiasIntervalo,
			GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			boolean onlyWithMovimento, boolean groupByConta, boolean showDetalhe) {
		return getFluxoDeCaixa(cdEmpresa, tpConta, contas, tpOrigem, nrDiasIntervalo, dtInicial, dtFinal,
				onlyWithMovimento, groupByConta, showDetalhe, true/*lgIgnoreTransferencia*/);
	}
	
	@SuppressWarnings("unchecked")
	public static ResultSetMap getFluxoDeCaixa(int cdEmpresa, int tpConta,
			Integer contas[], int tpOrigem, int nrDiasIntervalo,
			GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			boolean onlyWithMovimento, boolean groupByConta, boolean showDetalhe, boolean lgIgnoreTransferencia) {
		Connection connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);

			String crtConta = (contas.length > 0)?" AND cd_conta IN("+StringUtils.join(contas, ',')+") ":"";
			ResultSet rsContas = connect
					.prepareStatement(
							"SELECT * FROM adm_conta_financeira "
									+ "WHERE 1=1 "
									+ crtConta
									+ (tpConta >= 0 ? " AND tp_conta = "
											+ tpConta : "")
									+ (cdEmpresa >= 0 ? " AND cd_empresa = "
											+ cdEmpresa : "")).executeQuery();
			Double vlSaldo = 0.0;
			while (rsContas.next())
				vlSaldo += ContaFinanceiraServices.getSaldoAnterior(cdEmpresa,
						rsContas.getInt("cd_conta"), 0, dtInicial, false,
						connect);

			crtConta = (contas.length > 0)?" AND A.cd_conta IN("+StringUtils.join(contas, ',')+") ":"";
			PreparedStatement pstmtRecs = connect
					.prepareStatement("SELECT A.cd_conta, A.cd_movimento_conta, A.dt_movimento, A.tp_origem, A.vl_movimento,"
							+ "       F.cd_conta_receber, F.vl_recebido, "
							+ "       F.vl_multa AS vl_multa_recebida, F.vl_juros AS vl_juros_recebido, F.vl_desconto AS vl_desconto_concedido, "
							+ "       G.dt_vencimento AS dt_vencimento_receber, H.nm_pessoa AS nm_sacado,"
							+ "       B.dt_saldo_inicial, B.tp_conta, B.nm_conta, B.nr_conta, B.id_conta, B.nr_dv "
							+ "FROM adm_movimento_conta A "
							+ "JOIN adm_conta_financeira 		 B ON (A.cd_conta = B.cd_conta) "
							+ " LEFT OUTER JOIN adm_movimento_conta_receber F ON (A.cd_conta = F.cd_conta "
							+ "                                              AND A.cd_movimento_conta = F.cd_movimento_conta) "
							+ " LEFT OUTER JOIN adm_conta_receber           G ON (G.cd_conta_receber = F.cd_conta_receber) "
							+ "LEFT OUTER JOIN grl_pessoa       H ON (H.cd_pessoa = G.cd_pessoa) "
							+ "WHERE A.st_movimento IN ("
							+ ST_CONFERIDO
							+ ","
							+ ST_CONCILIADO
							+ ") "
							//recebimentos/créditos
							+ "  AND A.tp_movimento = "+CREDITO
							+ "  AND A.dt_movimento >= B.dt_saldo_inicial "
							+ "  AND A.dt_movimento >= ? "
							+ "  AND A.dt_movimento <= ? "
							+ ((lgIgnoreTransferencia)?"  AND A.tp_origem <> " + toTRANSFERENCIA:"")
							+ crtConta
							//							(cdConta > 0 ? "  AND A.cd_conta = " + cdConta: "") 
							+ " ORDER BY A.dt_movimento ");
			
			PreparedStatement pstmtPags = connect
					.prepareStatement("SELECT A.cd_conta, A.cd_movimento_conta, A.dt_movimento, A.tp_origem, A.vl_movimento,"
							+ "       C.cd_conta_pagar, C.vl_pago, "
							+ "       C.vl_multa AS vl_multa_paga, C.vl_juros AS vl_juros_pago, C.vl_desconto AS vl_desconto_recebido, "
							+ "       D.dt_vencimento AS dt_vencimento_pagar, E.nm_pessoa AS nm_favorecido, "
							+ "       B.dt_saldo_inicial, B.tp_conta, B.nm_conta, B.nr_conta, B.id_conta, B.nr_dv "
							+ "FROM adm_movimento_conta A "
							+ "JOIN adm_conta_financeira 	   B ON (A.cd_conta = B.cd_conta) "
							+" LEFT OUTER JOIN adm_movimento_conta_pagar C ON (A.cd_conta = C.cd_conta "
							+ "                                           			 AND A.cd_movimento_conta = C.cd_movimento_conta) "
							+ "LEFT OUTER JOIN adm_conta_pagar           D ON (D.cd_conta_pagar = C.cd_conta_pagar) "
							+ "LEFT OUTER JOIN grl_pessoa     E ON (E.cd_pessoa = D.cd_pessoa) "
							+ "WHERE A.st_movimento IN ("
							+ ST_CONFERIDO
							+ ","
							+ ST_CONCILIADO
							+ ") "
							//pagamentos/débitos
							+ "  AND A.tp_movimento = "+ DEBITO
							+ "  AND A.dt_movimento >= B.dt_saldo_inicial "
							+ "  AND A.dt_movimento >= ? "
							+ "  AND A.dt_movimento <= ? "
							+ ((lgIgnoreTransferencia)?"  AND A.tp_origem <> " + toTRANSFERENCIA:"")
							+ crtConta
							//							+ (cdConta > 0 ? "  AND A.cd_conta = " + cdConta: "")
							+ "ORDER BY A.dt_movimento ");
			
//			PreparedStatement pstmtMov = connect
//					.prepareStatement("SELECT A.tp_movimento, A.tp_origem, "
//							+ (groupByConta ? "B.nr_conta, B.nr_dv, B.nm_conta, "
//									: "")
//							+ "       SUM(A.vl_movimento) AS vl_movimento "
//							+ "FROM adm_movimento_conta A "
//							+ (tpConta >= 0 || groupByConta || cdEmpresa > 0 ? "JOIN adm_conta_financeira B ON (A.cd_conta = B.cd_conta "
//									+ "             "
//									+ (tpConta >= 0 ? "AND B.tp_conta = "
//											+ tpConta : "")
//									+ "           "
//									+ (cdEmpresa >= 0 ? "AND B.cd_empresa = "
//											+ cdEmpresa : "") + ") "
//									: "")
//							+ "WHERE A.st_movimento IN ("
//							+ ST_CONFERIDO
//							+ ","
//							+ ST_CONCILIADO
//							+ ") "
//							+ "  AND A.dt_movimento >= ? "
//							+ "  AND A.dt_movimento <= ? "
//							+ ( (lgIgnoreTransferencia)?"  AND A.tp_origem <> " + toTRANSFERENCIA:"")
//							+ crtConta
//							//							+ (cdConta > 0 ? "  AND A.cd_conta = " + cdConta: "")
//							+ " GROUP BY A.tp_movimento, A.tp_origem"
//							+ (groupByConta ? ", B.nr_conta, B.nr_dv, B.nm_conta "
//									: ""));
			PreparedStatement pstmtMov = connect
					.prepareStatement("SELECT A.tp_movimento, A.tp_origem, "
							+ (groupByConta ? "B.nr_conta, B.nr_dv, B.nm_conta, "
									: "")
							+ "       SUM(A.vl_movimento) AS vl_movimento "
							+ " FROM adm_movimento_conta A "
							+ " JOIN adm_conta_financeira B ON (A.cd_conta = B.cd_conta "
									+ "             "
									+ (tpConta >= 0 ? "AND B.tp_conta = "+ tpConta : "")
									+ "           "
									+ (cdEmpresa >= 0 ? "AND B.cd_empresa = "+ cdEmpresa : "") + ") "
									
							+ "WHERE A.st_movimento IN ("
							+ ST_CONFERIDO
							+ ","
							+ ST_CONCILIADO
							+ ") "
							+ "  AND A.dt_movimento >= B.dt_saldo_inicial "
							+ "  AND A.dt_movimento >= ? "
							+ "  AND A.dt_movimento <= ? "
							+ ( (lgIgnoreTransferencia)?"  AND A.tp_origem <> " + toTRANSFERENCIA:"")
							+ crtConta
							//							+ (cdConta > 0 ? "  AND A.cd_conta = " + cdConta: "")
							+ " GROUP BY A.tp_movimento, A.tp_origem"
							+ (groupByConta ? ", B.nr_conta, B.nr_dv, B.nm_conta "
									: ""));
			
			ResultSetMap rsm = new ResultSetMap();
			dtInicial.set(Calendar.HOUR, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtFinal.set(Calendar.HOUR, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			HashMap<String, Object> register = new HashMap<String, Object>();
			//Saldo do dia anterior - Primeira linha a ser aprensentada no rsm
			GregorianCalendar dtAnterior = (GregorianCalendar) dtInicial.clone();
			dtAnterior.add(Calendar.DATE, -1);
			if (nrDiasIntervalo == 1)
				register.put("DS_MOVIMENTO", com.tivic.manager.util.Util
						.formatDate(dtAnterior, "dd/MM"));
			else
				register.put("DS_MOVIMENTO", com.tivic.manager.util.Util
						.formatDate(dtAnterior, "dd/MM/yyyy"));
			register.put("VL_SALDO", new Float(vlSaldo));
			register.put("DS_DETALHE_CREDITO", "Saldo Inicial");
			register.put("DS_DETALHE_DEBITO", "Saldo Inicial");
			register.put("VL_CREDITO", new Float(0));
			register.put("VL_DEBITO", new Float(0));
			rsm.addRegister(register);
			
			for (GregorianCalendar dtMovimento = (GregorianCalendar) dtInicial
					.clone(); dtMovimento.before(dtFinal); dtMovimento.add(
							Calendar.DATE, nrDiasIntervalo)) {
				boolean hasMovimento = false;
				GregorianCalendar dtMovimentoFinal = (GregorianCalendar) dtMovimento
						.clone();
				dtMovimentoFinal.add(Calendar.DATE, nrDiasIntervalo - 1);
				dtMovimentoFinal.set(Calendar.HOUR, 23);
				dtMovimentoFinal.set(Calendar.MINUTE, 59);
				if (dtMovimentoFinal.after(dtFinal))
					dtMovimentoFinal = dtFinal;
				register = new HashMap<String, Object>();
				if (nrDiasIntervalo == 1)
					register.put("DS_MOVIMENTO", com.tivic.manager.util.Util
							.formatDate(dtMovimento, "dd/MM"));
				else
					register.put(
							"DS_MOVIMENTO",
							com.tivic.manager.util.Util.formatDate(dtMovimento,
									"dd/MM")
							+ "-"
							+ com.tivic.manager.util.Util.formatDate(
									dtMovimentoFinal, "dd/MM"));
				register.put("VL_CREDITO", new Float(0));
				register.put("VL_DEBITO", new Float(0));
				// Somando
				pstmtMov.setTimestamp(1,
						new Timestamp(dtMovimento.getTimeInMillis()));
				pstmtMov.setTimestamp(2,
						new Timestamp(dtMovimentoFinal.getTimeInMillis()));
				ResultSetMap rs = new ResultSetMap(pstmtMov.executeQuery());
				rs.beforeFirst();
				while (rs.next()) {
					hasMovimento = true;
					if (rs.getInt("tp_movimento") == DEBITO) {
						vlSaldo -= rs.getFloat("vl_movimento");
						register.put("VL_DEBITO",rs.getDouble("vl_movimento"));
					} else {
						vlSaldo += rs.getFloat("vl_movimento");
						register.put("VL_CREDITO", rs.getDouble("vl_movimento"));
					}
				}
				register.put("VL_SALDO", new Float(vlSaldo));
				if (!onlyWithMovimento || hasMovimento || rsm.size() == 0) {
					rsm.addRegister(register);
					// Detalhes
					if (showDetalhe) {
						pstmtPags.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
						pstmtPags.setTimestamp(2, new Timestamp(dtMovimentoFinal.getTimeInMillis()));
						ResultSetMap rsPags = new ResultSetMap(pstmtPags.executeQuery());
						pstmtRecs.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
						pstmtRecs.setTimestamp(2, new Timestamp(dtMovimentoFinal.getTimeInMillis()));
						ResultSetMap rsRecs = new ResultSetMap( pstmtRecs.executeQuery() );
						rsRecs.beforeFirst();
						boolean hasPag = rsPags.next();
						boolean hasRec = rsRecs.next();
						while (hasPag || hasRec) {
							String dsDetalheDebito = "", dsDetalheCredito = "";
							HashMap<String, Object> reg = (HashMap<String, Object>) register
									.clone();
							if (hasRec) {
								String nome = (rsRecs.getString("nm_sacado") != null ? rsRecs
										.getString("nm_sacado") : "Anônimo");
								nome = nome.length() > 20 ? nome.substring(0, 17) + "..." : nome;
								if( rsRecs.getInt("tp_origem") == toTRANSFERENCIA ){
									nome = "Tranferência ";
									rsRecs.setValueToField("vl_recebido", rsRecs.getDouble("vl_movimento"));	
								}
								String dv = rsRecs.getString("nr_dv");
								String conta = (rsRecs.getString("id_conta") != null ? rsRecs
										.getString("id_conta") : rsRecs
										.getString("nr_conta")
										+ (dv != null && !dv.equals("") ? "-"
												+ dv : ""));
								dsDetalheCredito += nome
										+ (nrDiasIntervalo > 1 ? " - "
												+ Util.formatDateTime(
														rsPags.getGregorianCalendar("dt_movimento"),
														"dd/MM/yyyy")
												: "")
										+ " - ["
										+ conta
										+ "] - "
										+ Util.formatNumber(rsRecs
												.getFloat("vl_recebido"));
							}
							if (hasPag) {
								String nome = (rsPags
										.getString("nm_favorecido") != null ? rsPags
												.getString("nm_favorecido") : "Anônimo");
								if( rsPags.getInt("tp_origem") == toTRANSFERENCIA ){
									nome = "Tranferência ";
									rsPags.setValueToField("vl_pago", rsPags.getDouble("vl_movimento"));	
								}
								nome = nome.length() > 20 ? nome.substring(0, 17) + "..." : nome;
								String dv = rsPags.getString("nr_dv");
								String conta = (rsPags.getString("id_conta") != null ? rsPags
										.getString("id_conta") : rsPags
										.getString("nr_conta")
										+ (dv != null && !dv.equals("") ? "-" + dv : ""));
								
								dsDetalheDebito += Util.formatNumber( rsPags.getDouble("vl_pago") )
										+ " - "
										+ nome
										+ (nrDiasIntervalo > 1 ? " - "
												+ Util.formatDateTime(
														rsPags.getGregorianCalendar("dt_movimento"),
														"dd/MM/yyyy")
												: "") + " - [" + conta + "]";
							}
							reg.put("DS_DETALHE_CREDITO", dsDetalheCredito);
							reg.put("DS_DETALHE_DEBITO", dsDetalheDebito);
							reg.put("VL_CREDITO", (hasRec)?rsRecs.getDouble("vl_recebido"):0.0 );
							reg.put("VL_DEBITO", (hasPag)?rsPags.getDouble("vl_pago"):0.0 );
							rsm.addRegister(reg);
							//
							hasPag = rsPags.next();
							hasRec = rsRecs.next();
						}
					}
					register = null;
				}
			}
			// Garante que o ultimo período sempre será incluido
			if (register != null)
				rsm.addRegister(register);
			rsm.beforeFirst();
			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	@SuppressWarnings("unchecked")
	public static ResultSetMap getPrevisaoFluxoDeCaixa(int cdEmpresa, int tpConta, int cdConta,
			int cdContaCarteira, int cdTipoDocumento,
			int cdClassificacao, int nrDiasIntervalo,
			GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			boolean onlyWithMovimento, boolean showDetalhe){
		Integer contas[] = (cdConta>0)?new Integer[cdConta]:new Integer[]{};
		Integer contasCarteira[] = (cdContaCarteira>0)?new Integer[cdContaCarteira]:new Integer[]{};
		return getPrevisaoFluxoDeCaixa(cdEmpresa, tpConta, contas, contasCarteira, cdTipoDocumento,
				cdClassificacao, nrDiasIntervalo, dtInicial, dtFinal, onlyWithMovimento, showDetalhe, true/*lgIgnoreTransferencia*/);
	}

	//@SuppressWarnings("unchecked")
	public static ResultSetMap getPrevisaoFluxoDeCaixa(int cdEmpresa, Integer tpConta,
			Integer contas[], Integer  contasCarteira[],
			Integer cdTipoDocumento, Integer cdClassificacao, Integer nrDiasIntervalo,
			GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			boolean onlyWithMovimento, boolean showDetalhe, boolean lgIgnoreTransferencia) {
		Connection connect = Conexao.conectar();
		try {

			String crtConta = (contas.length > 0)?" AND cd_conta IN("+StringUtils.join(contas, ",")+") ":"";
			
			ResultSetMap rsContas = new ResultSetMap( connect
					.prepareStatement(
							"SELECT * FROM adm_conta_financeira "
									+ "WHERE 1=1 "
									+ crtConta
									//											+ (cdConta > 0 ? " AND cd_conta = "+ cdConta : "")
									+ (tpConta >= 0 ? " AND tp_conta = "+ tpConta : "")
									+ (cdEmpresa >= 0 ? " AND cd_empresa = "+ cdEmpresa : "")).executeQuery());
			dtInicial.set(Calendar.HOUR, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtInicial.set(Calendar.SECOND, 0);
			Double vlSaldo = 0.0;
			while (rsContas.next())
				vlSaldo += ContaFinanceiraServices.getSaldoAnterior(cdEmpresa,
						rsContas.getInt("cd_conta"), 0, dtInicial, false, connect);

			crtConta = (contas.length > 0)?" AND A.cd_conta IN("+StringUtils.join(contas, ',')+") ":"";
			String crtCarteira = ( contasCarteira != null && contasCarteira.length > 0)?" AND A.cd_conta_carteira IN("+StringUtils.join(contasCarteira, ',')+") ":"";
			// Contas a pagar em aberto
			PreparedStatement pstmtPags = connect
					.prepareStatement("SELECT A.*, B.nr_conta, B.nm_conta, B.id_conta, B.nr_dv, C.nm_pessoa, "
							+ "       (SELECT sum(vl_pago) FROM adm_movimento_conta_pagar PAG "
							+ "        WHERE A.cd_conta_pagar = PAG.cd_conta_pagar) AS vl_pago "
							+ "FROM adm_conta_pagar A "
							+ "LEFT OUTER JOIN adm_conta_financeira B ON (A.cd_conta = B.cd_conta) "
							+ "LEFT OUTER JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa) "
							+ "WHERE  "+((cdEmpresa > 0)
									?"A.cd_empresa ="+cdEmpresa+" AND ":
									"") 
							+ "  A.st_conta = "+ ContaPagarServices.ST_EM_ABERTO
							+ "  AND A.dt_vencimento >= ? "
							+ "  AND A.dt_vencimento <= ? "
							+ crtConta
							//							+ (cdConta > 0 ? "  AND A.cd_conta = " + cdConta: "")
							+ (tpConta >= 0 ? "  AND B.tp_conta = " + tpConta
									: "")
							+ crtCarteira
							//							+ (cdContaCarteira > 0 ? "  AND A.cd_conta_carteira = "+ cdContaCarteira: "")
							+ (cdTipoDocumento > 0 ? "  AND A.cd_tipo_documento = "
									+ cdTipoDocumento
									: ""));
			
			// Contas a receber em aberto
			PreparedStatement pstmtRecs = connect
					.prepareStatement("SELECT A.*, B.nr_conta, B.nm_conta, B.id_conta, B.nr_dv, C.nm_pessoa, "
							+ "       (SELECT sum(vl_recebido) FROM adm_movimento_conta_receber REC "
							+ "        WHERE A.cd_conta_receber = REC.cd_conta_receber) AS vl_recebido "
							+ "FROM adm_conta_receber A "
							+ "LEFT OUTER JOIN adm_conta_financeira B ON (A.cd_conta = B.cd_conta) "
							+ "LEFT OUTER JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa) "
							+ "WHERE  "+((cdEmpresa > 0)
									?"A.cd_empresa ="+cdEmpresa+" AND ":
									"") 
							+ "  A.st_conta   = "+ ContaPagarServices.ST_EM_ABERTO
							+ "  AND A.dt_vencimento >= ? "
							+ "  AND A.dt_vencimento <= ? "
							+ crtConta
							//							+ (cdConta > 0 ? "  AND A.cd_conta = " + cdConta: "")
							+ (tpConta >= 0 ? "  AND B.tp_conta = " + tpConta
									: "")
							+crtCarteira		
							//							+ (cdContaCarteira > 0 ? "  AND A.cd_conta_carteira = "+ cdContaCarteira: "")
							+ (cdTipoDocumento > 0 ? "  AND A.cd_tipo_documento = "
									+ cdTipoDocumento
									: ""));
			/*
			 * Total de contas a pagar
			 * e Total pago
			 */
			PreparedStatement pstmtContaPagar = connect
					.prepareStatement("SELECT SUM(A.vl_conta) AS vl_debito, SUM(A.vl_pago) AS vl_pago "
							+ "FROM adm_conta_pagar A "
							+ (tpConta >= 0 ? "JOIN adm_conta_financeira B ON (A.cd_conta = B.cd_conta "
									+ "                            AND B.tp_conta = "
									+ tpConta + ") "
									: "")
							+ (cdClassificacao >= 0 ? "JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa "
									+ "                  AND C.cd_classificacao = "
									+ cdClassificacao + ") "
									: "")
							+ "WHERE  "+((cdEmpresa > 0)
									?"A.cd_empresa ="+cdEmpresa+" AND ":
									"") 
							+ "  A.st_conta =  "+ContaPagarServices.ST_EM_ABERTO
							+ "  AND A.dt_vencimento >= ? "
							+ "  AND A.dt_vencimento <= ? "
							+ crtConta
							//							+ (cdConta > 0 ? "  AND A.cd_conta = " + cdConta: "")
							+ crtCarteira
							//							+ (cdContaCarteira > 0 ? "  AND A.cd_conta_carteira = "+ cdContaCarteira: "")
							+ (cdTipoDocumento > 0 ? "  AND A.cd_tipo_documento = "
									+ cdTipoDocumento
									: ""));
			/*
			 * Total de contas a receber
			 * e Total recebido
			 */
			PreparedStatement pstmtContaReceber = connect
					.prepareStatement("SELECT SUM(A.vl_conta) AS vl_credito, SUM(A.vl_recebido) AS vl_recebido "
							+ "FROM adm_conta_receber A "
							+ (tpConta >= 0 ? "JOIN adm_conta_financeira B ON (A.cd_conta = B.cd_conta "
									+ "                            AND B.tp_conta = "
									+ tpConta + ") "
									: "")
							+ (cdClassificacao >= 0 ? "JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa "
									+ "               AND C.cd_classificacao = "
									+ cdClassificacao + ") "
									: "")
							+ "WHERE  "+((cdEmpresa > 0)
									?"A.cd_empresa ="+cdEmpresa+" AND ":
									"") 
							+ "  A.st_conta = 0 "
							+ "  AND A.dt_vencimento BETWEEN ? AND ? "
							+ crtConta
							//							+ (cdConta > 0 ? "  AND A.cd_conta = " + cdConta: "")
							+ crtCarteira
							//							+ (cdContaCarteira > 0 ? "  AND A.cd_conta_carteira = "+ cdContaCarteira: "")
							+ (cdTipoDocumento > 0 ? "  AND A.cd_tipo_documento = "
									+ cdTipoDocumento
									: ""));
			/**
			 * Total de movimentações
			 * agrupado pelo tipo(Déb/Créd )
			 */
			PreparedStatement pstmtMov = connect
					.prepareStatement("SELECT A.tp_movimento, SUM(A.vl_movimento) AS vl_movimento "
							+ "FROM adm_movimento_conta A "
							+ "JOIN adm_conta_financeira B ON (A.cd_conta = B.cd_conta "
									+ "             "
									+ (tpConta >= 0 ? "AND B.tp_conta = " + tpConta : "")
									+ "           "
									+ (cdEmpresa >= 0 ? "AND B.cd_empresa = " + cdEmpresa : "") + ") "
							+ "WHERE A.st_movimento IN ("
							+ ST_NAO_CONFERIDO
							+ ","
							+ ST_NAO_COMPENSADO
							+ ","
							+ ST_CONFERIDO
							+ ","
							+ ST_CONCILIADO
							+ ") "
							+ "  AND A.dt_movimento >= B.dt_saldo_inicial "
							+ "  AND A.dt_movimento >= ? "
							+ "  AND A.dt_movimento <= ? "
							+ ( (lgIgnoreTransferencia)?"  AND A.tp_origem <> " + toTRANSFERENCIA:"")
							+ crtConta
							//							+ (cdConta > 0 ? "  AND A.cd_conta = " + cdConta: "")
							+ " GROUP BY A.tp_movimento");

			ResultSetMap rsm = new ResultSetMap();
			dtFinal.set(Calendar.HOUR, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			HashMap<String, Object> register = new HashMap<String, Object>();
			GregorianCalendar dtAnterior = (GregorianCalendar) dtInicial.clone();
			dtAnterior.add(Calendar.DATE, -1);
			if (nrDiasIntervalo == 1)
				register.put("DS_MOVIMENTO", com.tivic.manager.util.Util
						.formatDate(dtAnterior, "dd/MM"));
			else
				register.put("DS_MOVIMENTO", com.tivic.manager.util.Util
						.formatDate(dtAnterior, "dd/MM/yyyy"));
			register.put("VL_SALDO", vlSaldo);
			register.put("DS_DETALHE_CREDITO", "Saldo Inicial");
			register.put("DS_DETALHE_DEBITO", "Saldo Inicial");
			register.put("VL_CREDITO", new Float(0));
			register.put("VL_DEBITO", new Float(0));
			rsm.addRegister(register);
			for (GregorianCalendar dtMovimento = (GregorianCalendar) dtInicial.clone();
					dtMovimento.before(dtFinal);
					dtMovimento.add(Calendar.DATE, nrDiasIntervalo)
					) {
				GregorianCalendar dtMovimentoFinal = (GregorianCalendar) dtMovimento.clone();
				dtMovimento.set(Calendar.HOUR, 0);
				dtMovimento.set(Calendar.MINUTE, 0);
				dtMovimento.set(Calendar.SECOND, 0);
				dtMovimento.set(Calendar.MILLISECOND, 0);
				dtMovimentoFinal.add(Calendar.DATE, nrDiasIntervalo - 1);
				dtMovimentoFinal.set(Calendar.HOUR, 23);
				dtMovimentoFinal.set(Calendar.MINUTE, 59);
				dtMovimentoFinal.set(Calendar.SECOND, 59);
				dtMovimentoFinal.set(Calendar.MILLISECOND, 999);
				if (dtMovimentoFinal.after(dtFinal))
					dtMovimentoFinal = dtFinal;
				register = new HashMap<String, Object>();
				if (nrDiasIntervalo == 1)
					register.put("DS_MOVIMENTO", com.tivic.manager.util.Util
							.formatDate(dtMovimento, "dd/MM"));
				else
					register.put(
							"DS_MOVIMENTO",
							com.tivic.manager.util.Util.formatDate(dtMovimento,
									"dd/MM")
							+ "-"
							+ com.tivic.manager.util.Util.formatDate(
									dtMovimentoFinal, "dd/MM"));
				register.put("VL_CREDITO", new Float(0));
				register.put("VL_DEBITO", new Float(0));
				// somando e subtraindo a movimentação da conta
				pstmtMov.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
				pstmtMov.setTimestamp(2, new Timestamp(dtMovimentoFinal.getTimeInMillis()));
				float vlCreditos = 0, vlDebitos = 0;
				ResultSet rs = pstmtMov.executeQuery();
				while (rs.next())
					if (rs.getInt("tp_movimento") == DEBITO)
						vlDebitos += rs.getFloat("vl_movimento");
					else
						vlCreditos += rs.getFloat("vl_movimento");
				// Gravando os créditos e débitos do movimento da conta
				vlSaldo = vlSaldo + vlCreditos - vlDebitos;
				register.put("VL_CREDITO", vlCreditos);
				register.put("VL_DEBITO", vlDebitos);
				boolean hasMovimento = (vlCreditos + vlDebitos > 0);
				// somando contas à pagar
				pstmtContaPagar.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
				pstmtContaPagar.setTimestamp(2, new Timestamp(dtMovimentoFinal.getTimeInMillis()));
				rs = pstmtContaPagar.executeQuery();
				if (rs.next() && rs.getFloat("vl_debito") > 0) {
					hasMovimento = true;
					vlSaldo -= (rs.getFloat("vl_debito") - rs.getFloat("vl_pago"));
					register.put("VL_DEBITO",new Float(rs.getFloat("vl_debito") - rs.getFloat("vl_pago") + vlDebitos));
				}
				// somando contas à receber
				pstmtContaReceber.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
				pstmtContaReceber.setTimestamp(2, Util.convCalendarToTimestamp(dtMovimentoFinal));
				rs = pstmtContaReceber.executeQuery();
				boolean next = rs.next();
				if (next && rs.getFloat("vl_credito") > 0) {
					hasMovimento = true;
					vlSaldo += (rs.getFloat("vl_credito") - rs.getFloat("vl_recebido"));
					register.put("VL_CREDITO",new Float(rs.getFloat("vl_credito") - rs.getFloat("vl_recebido") + vlCreditos));
				}
				register.put("VL_SALDO", new Float(vlSaldo));
				if (!onlyWithMovimento || hasMovimento || rsm.size() == 0) {
					rsm.addRegister(register);
					// Detalhes
					if (showDetalhe) {
						if (vlCreditos + vlDebitos > 0) {
							HashMap<String, Object> reg = (HashMap<String, Object>) register.clone();
							reg.put("DS_DETALHE_CREDITO",
									vlCreditos > 0 ? "Recebimentos (créditos/desbloqueios) - "
											+ Util.formatNumber(vlCreditos)
											: "");
							reg.put("DS_DETALHE_DEBITO",
									vlDebitos > 0 
									? Util.formatNumber(vlDebitos)+ " - Pagamentos (débito em conta, etc) "
											: "");
							reg.put("VL_CREDITO", vlCreditos);
							reg.put("VL_DEBITO", vlDebitos);
							rsm.addRegister(reg);
						}
						pstmtPags.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
						pstmtPags.setTimestamp(2, new Timestamp( dtMovimentoFinal.getTimeInMillis()));
						ResultSetMap rsPags = new ResultSetMap(pstmtPags.executeQuery());
						pstmtRecs.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
						pstmtRecs.setTimestamp(2, new Timestamp(dtMovimentoFinal.getTimeInMillis()));
						ResultSetMap rsRecs = new ResultSetMap(pstmtRecs.executeQuery());
						boolean hasPag = rsPags.next();
						boolean hasRec = rsRecs.next();
						Double vlCredReg = 0.0;
						Double vlDebReg = 0.0;
						while (hasPag || hasRec) {
							vlCredReg = 0.0;
							vlDebReg = 0.0;
							String dsDetalheDebito = "", dsDetalheCredito = "";
							HashMap<String, Object> reg = (HashMap<String, Object>) register.clone();
							if (hasRec) {
								String nome = (rsRecs.getString("nm_pessoa") != null ? rsRecs.getString("nm_pessoa") : "Anônimo");
								nome = nome.length() > 20 ? nome.substring(0,17) + "..." : nome;
								String dv = rsRecs.getString("nr_dv");
								String conta = (rsRecs.getString("id_conta") != null ? rsRecs
										.getString("id_conta") : rsRecs
										.getString("nr_conta")
										+ (dv != null && !dv.equals("") ? "-"
												+ dv : ""));
								vlCredReg = rsRecs.getDouble("vl_conta") - rsRecs.getDouble("vl_recebido");
								dsDetalheCredito += nome
										+ (nrDiasIntervalo > 1 ? " - "
												+ Util.formatDateTime(
														rsPags.getGregorianCalendar("dt_movimento"),
														"dd/MM/yyyy")
												: "")
										+ " - ["
										+ conta
										+ "] - "
										+ Util.formatNumber(rsRecs
												.getFloat("vl_conta")
												- rsRecs.getFloat("vl_recebido"));
							}
							if (hasPag) {
								String nome = (rsPags.getString("nm_pessoa") != null ? rsPags
										.getString("nm_pessoa") : "Anônimo");
								nome = nome.length() > 20 ? nome.substring(0,
										17) + "..." : nome;
								String dv = rsPags.getString("nr_dv");
								String conta = (rsPags.getString("id_conta") != null ? rsPags
										.getString("id_conta") : rsPags
										.getString("nr_conta")
										+ (dv != null && !dv.equals("") ? "-"
												+ dv : ""));
								vlDebReg = rsPags.getDouble("vl_conta")- rsPags.getDouble("vl_pago");
								dsDetalheDebito += Util.formatNumber(rsPags
										.getFloat("vl_conta")
										- rsPags.getFloat("vl_pago"))
										+ " - "
										+ nome
										+ (nrDiasIntervalo > 1 ? " - "
												+ Util.formatDate(
														rsPags.getGregorianCalendar("dt_movimento"),
														"dd/MM/yyyy")
												: "") + " - [" + conta + "]";
							}
							reg.put("DS_DETALHE_CREDITO", dsDetalheCredito);
							reg.put("DS_DETALHE_DEBITO", dsDetalheDebito);
							reg.put("VL_CREDITO", vlCredReg);
							reg.put("VL_DEBITO", vlDebReg);
							rsm.addRegister(reg);
							//
							hasPag = rsPags.next();
							hasRec = rsRecs.next();
						}
					}
					register = null;
				}
			}
			// garante que o ultimo período sempre será incluido
			if (register != null)
				rsm.addRegister(register);
			rsm.beforeFirst();
			return rsm;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaServices.getPrevisaoFluxoDeCaixa: "
					+ sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaServices.getPrevisaoFluxoDeCaixa: "
					+ e);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	public static int setConciliacao(int cdConta, ArrayList<String> movimentos,
			ArrayList<String> extratoIds, ArrayList<String> docs,
			ArrayList<String> historicos, GregorianCalendar dtMovimentoExtrato) {
		Connection connect = Conexao.conectar();

		try {
			int ret = 0;
			for (int i = 0; i < movimentos.size(); i++) {
				MovimentoConta mov = MovimentoContaDAO.get(
						Integer.parseInt(movimentos.get(i)), cdConta, connect);
				ret = setConciliacao(cdConta, mov.getCdMovimentoConta(),
						mov.getVlMovimento(), mov.getDtMovimento(), extratoIds,
						docs, historicos, dtMovimentoExtrato);
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraServices.setConciliacao: "+ e);
			return -1;
		} finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static int setConciliacao(int cdConta, int cdMovimentoConta,
			Double vlMovimento, GregorianCalendar dtMovimento, String idExtrato,
			String nrDocumento, String dsHistorico,
			GregorianCalendar dtMovimentoExtrato){
		return setConciliacao(cdConta, cdMovimentoConta,
								vlMovimento, dtMovimento, idExtrato,
								nrDocumento, dsHistorico,
								dtMovimentoExtrato, null);
	}
	
	public static int setConciliacao(int cdConta, int cdMovimentoConta,
			Double vlMovimento, GregorianCalendar dtMovimento, String idExtrato,
			String nrDocumento, String dsHistorico,
			GregorianCalendar dtMovimentoExtrato, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			ArrayList<String> extratoIds = new ArrayList<String>();
			extratoIds.add(idExtrato);
			ArrayList<String> docs = new ArrayList<String>();
			docs.add(nrDocumento);
			ArrayList<String> historicos = new ArrayList<String>();
			historicos.add(dsHistorico);
			int retorno = setConciliacao(cdConta, cdMovimentoConta, vlMovimento,
					dtMovimento, extratoIds, docs, historicos,
					dtMovimentoExtrato, connect);
			if( retorno > 0 ){
				if(isConnectionNull)
					connect.commit();
				return retorno;
			}else{
				if (isConnectionNull)
					Conexao.rollback(connect);
				return retorno;
			}
				
		} catch (Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraServices.setConciliacao: " + e);
			return -1;
		}finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int setConciliacao(int cdConta, int cdMovimentoConta,
			Double vlMovimento, GregorianCalendar dtMovimento,
			ArrayList<String> extratoIds, ArrayList<String> docs,
			ArrayList<String> historicos, GregorianCalendar dtMovimentoExtrato) {
		return setConciliacao( cdConta, cdMovimentoConta,
								vlMovimento, dtMovimento,
								extratoIds, docs,
								historicos, dtMovimentoExtrato, null);
	}
	
	public static int setConciliacao(int cdConta, int cdMovimentoConta,
			Double vlMovimento, GregorianCalendar dtMovimento,
			ArrayList<String> extratoIds, ArrayList<String> docs,
			ArrayList<String> historicos, GregorianCalendar dtMovimentoExtrato, Connection connect) {
		
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			String idExtrato = "|" + extratoIds.get(0);
			String nrDocumento = docs.get(0);
			String dsHistorico = historicos.get(0);
			for (int i = 1; i < extratoIds.size(); i++) {
				idExtrato += "|" + extratoIds.get(i);
				nrDocumento += ", " + docs.get(i);
				dsHistorico += ", " + historicos.get(i);
			}

			PreparedStatement pstmt = connect
					.prepareStatement("UPDATE adm_movimento_conta "
							+ "SET id_extrato = \'" + idExtrato + "\', "
							+ "    st_movimento = " + ST_CONCILIADO + ", "
							+ "    nr_documento = \'" + nrDocumento + "\', "
							+ "    ds_historico = \'" + dsHistorico.substring(0, (dsHistorico.length()>100)?100:dsHistorico.length() ) + "\',"
							+ "    dt_movimento = ? " + ","
							+ "    dt_deposito = ? " + "WHERE cd_conta = "
							+ cdConta + "  AND cd_movimento_conta = "
							+ cdMovimentoConta);

			pstmt.setTimestamp(1,
					new Timestamp(dtMovimentoExtrato.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtMovimento.getTimeInMillis()));
			if (pstmt.executeUpdate() > 0){
				if (isConnectionNull)
					connect.commit();
				return cdMovimentoConta;
			}else{
				if (isConnectionNull)
					Conexao.rollback(connect);
				com.tivic.manager.util.Util.registerLog(new Exception("Erro ao conciliar movimento!"));
				return -1;
			}
		} catch (Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaServices.setConciliacao: "+ e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int setSituacaoMovimento(int cdConta, int cdMovimentoConta,
			int stMovimento) {
		Connection connect = Conexao.conectar();
		try {
			PreparedStatement pstmt = connect
					.prepareStatement("UPDATE adm_movimento_conta "
							+ "SET st_movimento = " + stMovimento
							+ " WHERE cd_conta = " + cdConta
							+ "   AND cd_movimento_conta = " + cdMovimentoConta);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err
			.println("Erro! ContaFinanceiraServices.setSituacaoMovimento: "
					+ e);
			return -1;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	/*
	 * Revisa corrigindo classificação em categorias
	 */
	public static Result reviewMovimento(int cdConta,
			GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		Connection connect = Conexao.conectar();
		try {
			dtInicial.set(Calendar.HOUR, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtFinal.set(Calendar.HOUR, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			PreparedStatement pstmt = connect
					.prepareStatement("SELECT * FROM adm_movimento_conta A "
							+ "WHERE cd_conta = "
							+ cdConta
							+ "  AND dt_movimento BETWEEN ? AND ? "
							+ "  AND tp_origem <> "
							+ toTRANSFERENCIA
							+ "  AND NOT EXISTS (SELECT * FROM adm_movimento_conta_categoria B "
							+ "                  WHERE A.cd_movimento_conta = B.cd_movimento_conta "
							+ "                    AND A.cd_conta           = B.cd_conta) ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSet rs = pstmt.executeQuery();
			int count = 0;
			connect.setAutoCommit(false);
			while (rs.next()) {
				ResultSetMap rsmRec = MovimentoContaReceberServices
						.getRecebimentoOfMovimento(cdConta,
								rs.getInt("cd_movimento_conta"), connect);
				while (rsmRec.next()) {
					ResultSetMap rsmCats = ContaReceberCategoriaServices
							.getCategoriaOfContaReceber(
									rsmRec.getInt("cd_conta_receber"), connect);
					while (rsmCats.next()) {
						float vlMovimentoCategoria = rsmCats
								.getFloat("vl_conta_categoria")
								/ rsmRec.getFloat("vl_conta")
								* rsmRec.getFloat("vl_recebido");
						MovimentoContaCategoriaServices
						.insert(new MovimentoContaCategoria(
								cdConta,
								rs.getInt("cd_movimento_conta"),
								rsmCats.getInt("cd_categoria_economica"),
								vlMovimentoCategoria,
								0 /* cdMovimentoContaCategoria */,
								0 /* cdContaPagar */,
								rsmRec.getInt("cd_conta_receber"),
								MovimentoContaCategoriaServices.TP_PRE_CLASSIFICACAO /* tpMovimento */,
								0/* cdCentroCusto */), connect);
					}
				}
				ResultSetMap rsmPag = MovimentoContaPagarServices
						.getPagamentoOfMovimento(cdConta,
								rs.getInt("cd_movimento_conta"), connect);
				while (rsmPag.next()) {
					ResultSetMap rsmCats = ContaPagarCategoriaServices
							.getCategoriaOfContaPagar(
									rsmPag.getInt("cd_conta_pagar"), connect);
					while (rsmCats.next()) {
						float vlMovimentoCategoria = rsmCats
								.getFloat("vl_conta_categoria")
								/ rsmPag.getFloat("vl_conta")
								* rsmPag.getFloat("vl_pago");
						MovimentoContaCategoriaServices
						.insert(new MovimentoContaCategoria(
								cdConta,
								rs.getInt("cd_movimento_conta"),
								rsmCats.getInt("cd_categoria_economica"),
								vlMovimentoCategoria,
								0 /* cdMovimentoContaCategoria */,
								rsmPag.getInt("cd_conta_pagar"),
								0 /* cdContaPagar */,
								MovimentoContaCategoriaServices.TP_PRE_CLASSIFICACAO /* tpMovimento */,
								0/* cdCentroCusto */), connect);
					}
				}
			}
			connect.commit();
			return new Result(count, count
					+ " registros corrigido(s) com sucesso!");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err
			.println("Erro! ContaFinanceiraServices.reviewMovimento: "
					+ e);
			return new Result(-1,
					"Erro ao tentar corrigir movimentos com problema!", e);
		} finally {
			Conexao.desconectar(connect);
		}
	}

	public static int deleteMovimentoTituloCredito(int cdConta,
			int cdMovimentoConta, int cdTituloCredito) {
		Connection connect = Conexao.conectar();
		try {
			return connect.prepareStatement(
					"DELETE FROM adm_movimento_titulo_credito "
							+ "WHERE cd_conta = " + cdConta
							+ "  AND cd_movimento_conta = " + cdMovimentoConta
							+ "  AND cd_titulo_credito = " + cdTituloCredito)
					.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err
			.println("Erro! MovimentoContaServices.deleteMovimentoTituloCredito: "
					+ e);
			return -1;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getTituloCreditoOfMovimento(int cdConta,
			int cdMovimentoConta) {
		return getTituloCreditoOfMovimento(cdConta, cdMovimentoConta, null);
	}

	public static ResultSetMap getTituloCreditoOfMovimento(int cdConta,
			int cdMovimentoConta, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			PreparedStatement pstmt = connect
					.prepareStatement("SELECT A.*, B.*, C.nr_banco AS nr_instituicao_financeira, C.nm_banco AS nm_instituicao_financeira, "
							+ " D.id_conta_receber, D.nr_parcela, D.nr_referencia, B.cd_tipo_documento, "
							+ " D.ds_historico, D.dt_vencimento, D.dt_emissao, D.dt_recebimento, D.vl_conta, D.vl_abatimento, "
							+ " D.vl_acrescimo, D.vl_recebido, D.st_conta, E.nm_tipo_documento, E.sg_tipo_documento, "
							+ " F.nm_pessoa as nm_portador, F.nr_telefone1 as nr_telefone_portador, "
							+ " G.nm_pessoa as nm_emissor_titulo, G.nr_telefone1 as nr_telefone_emissor "
							+ " FROM adm_movimento_titulo_credito   A "
							+ " JOIN adm_titulo_credito             B ON (A.cd_titulo_credito = B.cd_titulo_credito) "
							+ " LEFT OUTER JOIN grl_banco           C ON (B.cd_instituicao_financeira = C.cd_banco)  "
							+ " JOIN adm_conta_receber              D ON (B.cd_conta_receber = D.cd_conta_receber)   "
							+ " LEFT OUTER JOIN adm_tipo_documento  E ON (B.cd_tipo_documento = E.cd_tipo_documento) "
							+ " LEFT OUTER JOIN grl_pessoa          F ON (B.cd_portador = F.cd_pessoa ) "
							+ " LEFT OUTER JOIN grl_pessoa          G ON (B.cd_emissor = G.cd_pessoa ) "
							+ " WHERE A.cd_conta = "+ cdConta
							+ "  AND A.cd_movimento_conta = "+ cdMovimentoConta);
			return new ResultSetMap(pstmt.executeQuery());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err
			.println("Erro! ContaReceberCategoriaServices.getTituloCreditoOfMovimento: "
					+ e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	/**
	 * Encapsulamento do método insertTransfer para uso no Flex
	 * 
	 * @see #insertTransfer(int, int, int, GregorianCalendar, String, ArrayList, int, Connection)
	 * 
	 * 
	 * @author Alvaro
	 * @param cdUsuario
	 * @param cdContaOrigem
	 * @param cdContaDestino
	 * @param dtTransferencia
	 * @param nrDocumento
	 * @param titulos
	 * @param cdAlinea
	 * @param connection
	 * @since 06/05/2015
	 * @return
	 */
	public static Result transferirTitulo(int cdUsuario, int cdContaOrigem,
			int cdContaDestino, GregorianCalendar dtTransferencia,
			String nrDocumento, ArrayList<Integer> titulos, int cdAlinea ){

		return transferirTitulo(cdUsuario, cdContaOrigem, cdContaDestino, dtTransferencia, nrDocumento, titulos, cdAlinea, null);
	}

	public static Result transferirTitulo(int cdUsuario, int cdContaOrigem,
			int cdContaDestino, GregorianCalendar dtTransferencia,
			String nrDocumento, ArrayList<Integer> titulos, int cdAlinea, Connection connection ){

		Result result = new Result(-1);
		int cdRetorno = insertTransfer(cdUsuario, cdContaOrigem, cdContaDestino, dtTransferencia, nrDocumento, titulos, cdAlinea, connection);
		result.setCode(cdRetorno);

		if( cdRetorno > 0 ){
			result.setMessage("Transferência efetuada com sucesso!");
		}else{
			switch (cdRetorno) {
			case ERR_TRANSF_NOT_FORMA_PAG:
				result.setMessage("Certifique-se de todos os títulos de crédito estejam com a forma de transferência pré-configurada.");
				break;
			case ERR_TRANSF_MULT_FORM_PAG:
				result.setMessage("Certifique-se de selecionar apenas títulos com uma forma de transferência pré-configurada.");
				break;
			case ERR_TRANSF_VALOR_MOV:
				result.setMessage("Certifique-se de todos os títulos de crédito estejam com a forma de transferência pré-configurada.");
				break;
			case ERR_TRANSF_NOT_LOC_CONTA_ORIGEM:
				result.setMessage("Certifique-se de que todos os títulos selecionados estejam localizados na conta de origem.");
				break;
			}
		}

		return result;
	}

	public static int insertTransfer(int cdUsuario, int cdContaOrigem,
			int cdContaDestino, GregorianCalendar dtTransferencia,
			String nrDocumento, int[] titulos, int cdAlinea) {
		ArrayList<Integer> titulosTemp = new ArrayList<Integer>();
		for (int i = 0; titulos != null && i < titulos.length; i++)
			titulosTemp.add(titulos[i]);
		return insertTransfer(cdUsuario, cdContaOrigem, cdContaDestino,
				dtTransferencia, nrDocumento, titulosTemp, cdAlinea, null);
	}

	public static int insertTransfer(int cdUsuario, int cdContaOrigem,
			int cdContaDestino, GregorianCalendar dtTransferencia,
			String nrDocumento, ArrayList<Integer> titulos, int cdAlinea) {
		return insertTransfer(cdUsuario, cdContaOrigem, cdContaDestino,
				dtTransferencia, nrDocumento, titulos, cdAlinea, null);
	}

	public static int insertTransfer(int cdUsuario, int cdContaOrigem,
			int cdContaDestino, GregorianCalendar dtTransferencia,
			String nrDocumento, ArrayList<Integer> titulos, int cdAlinea,
			Connection connection) {
		boolean isConnectionNull = connection == null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection
					.getAutoCommit());

			String codigosTitulos = "";
			for (int i = 0; titulos != null && i < titulos.size(); i++)
				codigosTitulos += (i > 0 ? " OR " : "")
				+ " cd_titulo_credito = " + titulos.get(i);
			PreparedStatement pstmt = connection
					.prepareStatement("SELECT DISTINCT cd_forma_transferencia "
							+ "FROM adm_tipo_documento "
							+ "WHERE cd_tipo_documento IN (SELECT cd_tipo_documento "
							+ "							 FROM adm_titulo_credito "
							+ "							 WHERE " + codigosTitulos + ") ");
			ResultSetMap rsmFormasTransf = new ResultSetMap(
					pstmt.executeQuery());
			if (!rsmFormasTransf.next()
					|| rsmFormasTransf.getInt("cd_forma_transferencia") == 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return ERR_TRANSF_NOT_FORMA_PAG;
			} else if (rsmFormasTransf.size() > 1) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return ERR_TRANSF_MULT_FORM_PAG;
			}

			int cdFormaPagamento = rsmFormasTransf
					.getInt("cd_forma_transferencia");

			pstmt = connection.prepareStatement("SELECT SUM(vl_titulo) "
					+ "FROM adm_titulo_credito " + "WHERE " + codigosTitulos);
			ResultSet rs = pstmt.executeQuery();
			float vlMovimento = rs.next() ? rs.getFloat(1) : 0;
			if (vlMovimento <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return ERR_TRANSF_VALOR_MOV;
			}

			pstmt = connection.prepareStatement("SELECT cd_titulo_credito "
					+ "FROM adm_titulo_credito " + "WHERE cd_conta <> ? "
					+ "  AND (" + codigosTitulos + ") ");
			pstmt.setInt(1, cdContaOrigem);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return ERR_TRANSF_NOT_LOC_CONTA_ORIGEM;
			}

			MovimentoConta movimento = new MovimentoConta(
					0 /* cdMovimentoConta */, cdContaOrigem /* cdConta */,
					0 /* cdContaOrigem */, 0 /* cdMovimentoOrigem */, cdUsuario,
					0 /* cdCheque */, 0 /* cdViagem */,
					dtTransferencia /* dtMovimento */, vlMovimento,
					nrDocumento, DEBITO /* tpMovimento */,
					toTRANSFERENCIA /* tpOrigem */,
					ST_NAO_CONFERIDO /* stMovimento */,
					"Transferência de Títulos de Crédito" /* dsHistorico */,
					dtTransferencia /* dtDeposito */, "" /* idExtrato */,
					cdFormaPagamento, 0 /* cdFechamento */, 0 /* cdTurno */);

			ArrayList<MovimentoContaTituloCredito> titulosTemp = new ArrayList<MovimentoContaTituloCredito>();
			for (int i = 0; titulos != null && i < titulos.size(); i++)
				titulosTemp.add(new MovimentoContaTituloCredito(titulos.get(i),
						0 /* cdMovimentoConta */, 0 /* cdConta */));

			int code = insertTransfer(movimento, cdContaDestino, titulosTemp,
					connection).getCode();
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return code;
			}

			if (cdAlinea > 0) {
				pstmt = connection
						.prepareStatement("UPDATE adm_titulo_credito "
								+ "SET cd_alinea = ? " + "WHERE "
								+ codigosTitulos);
				pstmt.setInt(1, cdAlinea);
				pstmt.execute();
				pstmt.close();
			}

			if (isConnectionNull)
				connection.commit();

			return code;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}

	}

	public static Result insertTransfer(MovimentoConta objeto,
			int cdContaDestino) {
		return insertTransfer(objeto, cdContaDestino, null, null);
	}

	public static Result insertTransfer(MovimentoConta objeto,
			int cdContaDestino, ArrayList<Integer> titulosCredito) {
		ArrayList<MovimentoContaTituloCredito> titulosTemp = new ArrayList<MovimentoContaTituloCredito>();
		for (int i = 0; titulosCredito != null && i < titulosCredito.size(); i++)
			titulosTemp.add(new MovimentoContaTituloCredito(titulosCredito
					.get(i), 0 /* cdMovimentoConta */, 0 /* cdConta */));
		return insertTransfer(objeto, cdContaDestino, titulosTemp, null);
	}

	public static Result insertTransfer(MovimentoConta objeto,
			int cdContaDestino,
			ArrayList<MovimentoContaTituloCredito> titulosCredito,
			Connection connect) {
		boolean isConectionNull = connect == null;
		try {
			connect = isConectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConectionNull ? false : connect.getAutoCommit());

			// Verifica quando a transferência for de título de crédito
			String listaTitulos = "0";

			FormaPagamento formaPag = FormaPagamentoDAO.get(objeto.getCdFormaPagamento(), connect);

			objeto.setStMovimento(ST_COMPENSADO);
			if (formaPag.getTpFormaPagamento() != FormaPagamentoServices.MOEDA_CORRENTE) {
				ContaFinanceira conta = ContaFinanceiraDAO.get(objeto.getCdConta(), connect);
				if (conta.getTpConta() == ContaFinanceiraServices.TP_CAIXA)
					objeto.setStMovimento(ST_NAO_COMPENSADO);

				for (int i = 0; titulosCredito != null && i < titulosCredito.size(); i++)
					listaTitulos += (listaTitulos.equals("") ? "" : ",") + titulosCredito.get(i).getCdTituloCredito();

				ResultSet rs = connect.prepareCall(
						//								"SELECT SUM(TRUNC(vl_titulo * 100) / 100) AS vl_titulo FROM adm_titulo_credito "
						"SELECT SUM((vl_titulo * 100) / 100) AS vl_titulo FROM adm_titulo_credito "
						+ "WHERE cd_titulo_credito IN ("
						+ listaTitulos + ") ").executeQuery();
				if (rs.next() && Math.abs(objeto.getVlMovimento() - rs.getFloat("vl_titulo")) > 1) {
					return new Result(-1, "Transferencia de titulos com divergencia de valores! \n"
							+ "\tValor do Movimento: "
							+ objeto.getVlMovimento()
							+ "\tValor em Titulos..: "
							+ rs.getFloat("vl_titulo"));
				}
			}

			objeto.getDtMovimento().set(Calendar.MINUTE, 0);
			objeto.getDtMovimento().set(Calendar.HOUR, 0);
			objeto.getDtMovimento().set(Calendar.SECOND, 0);
			objeto.setTpMovimento(DEBITO);
			objeto.setTpOrigem(toTRANSFERENCIA);
			objeto.setCdContaOrigem(0);
			objeto.setCdMovimentoOrigem(0);

			int cdMovimentoOrigem = MovimentoContaDAO.insert(objeto, connect);
			if (cdMovimentoOrigem <= 0) {
				if (isConectionNull)
					Conexao.rollback(connect);
				com.tivic.manager.util.Util.registerLog(new Exception(
						"Erro ao salvar movimento na conta de origem."));
				return new Result(ERR_SAVE_MOV_ORIGEM,
						"Erro ao salvar movimento na conta de origem.");
			}
			/*
			 * Incluindo títulos na origem
			 */
			for (int i = 0; titulosCredito != null && i < titulosCredito.size(); i++) {
				titulosCredito.get(i).setCdMovimentoConta(cdMovimentoOrigem);
				titulosCredito.get(i).setCdConta(objeto.getCdConta());
				if (MovimentoContaTituloCreditoDAO.insert(
						titulosCredito.get(i), connect) <= 0) {
					if (isConectionNull)
						Conexao.rollback(connect);
					com.tivic.manager.util.Util
					.registerLog(new Exception(
							"Erro ao salvar os títulos no movimento na conta de origem."));
					return new Result(ERR_SAVE_TITULO_MOV_ORIGEM,
							"Erro ao salvar os títulos no movimento na conta de origem.");
				}
			}
			/*
			 * Alterando dados para o movimento de destino
			 */
			objeto.setCdContaOrigem(objeto.getCdConta());
			objeto.setCdMovimentoOrigem(cdMovimentoOrigem);
			objeto.setCdConta(cdContaDestino);
			objeto.setTpMovimento(MovimentoContaServices.CREDITO);
			objeto.setCdMovimentoConta(0);
			objeto.setCdFechamento(0);
			int cdMovimentoDestino = MovimentoContaDAO.insert(objeto, connect);
			if (cdMovimentoDestino <= 0) {
				if (isConectionNull)
					Conexao.rollback(connect);
				com.tivic.manager.util.Util.registerLog(new Exception(
						"Erro ao salvar movimento na conta de destino."));
				return new Result(ERR_SAVE_MOV_DESTINO,
						"Erro ao salvar movimento na conta de destino.");
			}
			/*
			 * Incluindo títulos no destino
			 */
			for (int i = 0; titulosCredito != null && i < titulosCredito.size(); i++) {
				titulosCredito.get(i).setCdMovimentoConta(cdMovimentoDestino);
				titulosCredito.get(i).setCdConta(cdContaDestino);
				if (MovimentoContaTituloCreditoDAO.insert(
						titulosCredito.get(i), connect) <= 0) {
					if (isConectionNull)
						Conexao.rollback(connect);
					com.tivic.manager.util.Util.registerLog(new Exception(
							"Erro incluindo títulos no destino."));
					return new Result(ERR_SAVE_TITULO_MOV_DESTINO,
							"Erro incluindo títulos no destino.");
				}
			}
			/*
			 * Se for transferência de título de crédito transfere a posse dos
			 * títulos
			 */
			if (formaPag.getTpFormaPagamento() != FormaPagamentoServices.MOEDA_CORRENTE) {
				PreparedStatement pstmt = connect
						.prepareStatement("UPDATE adm_titulo_credito "
								+ "SET cd_conta = ? "
								+ "WHERE cd_titulo_credito IN (" + listaTitulos
								+ ") ");
				pstmt.setInt(1, cdContaDestino);
				pstmt.execute();
			}

			if (isConectionNull)
				connect.commit();

			return new Result(cdMovimentoOrigem);
		} catch (Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			if (isConectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar registrar transferencia!", e);
		} finally {
			if (isConectionNull)
				Conexao.desconectar(connect);
		}
	}

	@SuppressWarnings("unchecked")
	public static Result transferir(ResultSetMap transferencias) {

		Connection connection = Conexao.conectar();
		try {
			connection.setAutoCommit(false);

			while (transferencias.next()) {
				MovimentoConta movimentoConta = (MovimentoConta) transferencias
						.getObject("MOVIMENTO_CONTA");
				ArrayList<MovimentoContaTituloCredito> titulos = (ArrayList<MovimentoContaTituloCredito>) transferencias
						.getObject("TITULOS");

				insertTransfer(movimentoConta,
						transferencias.getInt("CD_CONTA_DESTINO"), titulos,
						connection);
			}

			return new Result(1, "Transferencia realizada com sucesso.");

		} catch (Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar lançar transferências!", e);
		}
	}

	public static Result insertTransferArray(ArrayList<MovimentoConta> al,
			ArrayList<Object> cts, ArrayList<Object> titulosCreditos) {
		boolean erro = false;
		Result ret = null;
		ArrayList<MovimentoContaTituloCredito> titulosTemp = new ArrayList<MovimentoContaTituloCredito>();
		for (int i = 0; titulosCreditos != null && i < titulosCreditos.size(); i++)
			titulosTemp.add(new MovimentoContaTituloCredito(Integer
					.parseInt(titulosCreditos.get(i).toString()),
					0 /* cdMovimentoConta */, 0 /* cdConta */));
		for (int i = 0; i <= al.size() - 1; i++) {
			MovimentoConta objeto = (MovimentoConta) al.get(i);
			if (titulosCreditos == null)
				ret = insertTransfer(objeto,
						Integer.parseInt(cts.get(i).toString()));
			else
				ret = insertTransfer(objeto,
						Integer.parseInt(cts.get(i).toString()), titulosTemp,
						null);
			if (ret.getCode() <= 0) {
				erro = true;
				break;
			}
		}
		if (!erro)
			return ret;
		else
			return ret;
	}

	public static ResultSetMap getRecebimentosOf(int cdConta, int cdFechamento, GregorianCalendar dtFechamento, int cdTurno) {
		Connection connect = null;
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			PreparedStatement pstmt;
			ResultSetMap rsm = null;
			String sql = "SELECT A.*, B.dt_movimento, B.nr_documento, B.cd_forma_pagamento, B.ds_historico, "
					+ "       B.tp_origem, C.nr_conta, C.nr_dv, C.nm_conta, C.tp_conta, "
					+ "       D.dt_vencimento, D.vl_conta, D.vl_acrescimo, D.vl_abatimento, "
					+ "       D.nr_parcela, D.nr_referencia, D.cd_tipo_documento, D.qt_parcelas, D.cd_empresa, "
					+ "       E.nm_pessoa, F.nm_forma_pagamento, F.tp_forma_pagamento, G.nm_tipo_documento, G.sg_tipo_documento  "
					+ "FROM adm_movimento_conta_receber A "
					+ "JOIN adm_movimento_conta   B  ON (A.cd_conta = B.cd_conta "
					+ "                              AND A.cd_movimento_conta = B.cd_movimento_conta) "
					+ "JOIN adm_conta_financeira  C  ON (A.cd_conta = C.cd_conta) "
					+ "JOIN adm_conta_receber     D  ON (A.cd_conta_receber = D.cd_conta_receber) "
					+ "LEFT OUTER JOIN grl_pessoa E  ON (D.cd_pessoa = E.cd_pessoa) "
					+ "LEFT OUTER JOIN adm_forma_pagamento F ON (B.cd_forma_pagamento = F.cd_forma_pagamento) "
					+ "LEFT OUTER JOIN adm_tipo_documento  G ON (D.cd_tipo_documento  = G.cd_tipo_documento) "
					+ "WHERE (D.dt_emissao < ? OR D.cd_documento_saida = null) AND D.cd_conta = ? AND B.cd_fechamento=? ";

			pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtFechamento.getTimeInMillis()));
			pstmt.setInt(2, cdConta);
			pstmt.setInt(3, cdFechamento);
			rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err
			.println("Erro! DocumentoSaidaItemDAO.getAllItensDocumentoSaida: "
					+ e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getRelatorioConsolidacaoRecebimentos(
			ResultSetMap rsmFechamentos) {
		return getRelatorioConsolidacaoRecebimentos(rsmFechamentos, null);
	}

	public static ResultSetMap getRelatorioConsolidacaoRecebimentos(
			ResultSetMap rsmFechamentos, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			StringBuffer fechamentos = new StringBuffer();
			rsmFechamentos.beforeFirst();
			while (rsmFechamentos.next()) {
				fechamentos.append(rsmFechamentos.getString("CD_FECHAMENTO"));
				if (rsmFechamentos.hasMore())
					fechamentos.append(",");
			}
			return Search.find("SELECT A.*, B.dt_movimento, B.nr_documento, B.cd_forma_pagamento, B.ds_historico, "
					+ "       B.tp_origem, C.nr_conta, C.nr_dv, C.nm_conta, C.tp_conta, "
					+ "       D.dt_vencimento, D.vl_conta, D.vl_acrescimo, D.vl_abatimento, "
					+ "       D.nr_parcela, D.nr_referencia, D.cd_tipo_documento, D.qt_parcelas, D.cd_empresa, "
					+ "       E.nm_pessoa, F.nm_forma_pagamento, F.tp_forma_pagamento, G.nm_tipo_documento, G.sg_tipo_documento  "
					+ " FROM adm_movimento_conta_receber A "
					+ " JOIN adm_movimento_conta   B  ON (A.cd_conta = B.cd_conta "
					+ "                              AND A.cd_movimento_conta = B.cd_movimento_conta) "
					+ " JOIN adm_conta_financeira  C  ON (A.cd_conta = C.cd_conta) "
					+ " JOIN adm_conta_receber     D  ON (A.cd_conta_receber = D.cd_conta_receber) "
					+ " LEFT OUTER JOIN grl_pessoa E  ON (D.cd_pessoa = E.cd_pessoa) "
					+ " LEFT OUTER JOIN adm_forma_pagamento F ON (B.cd_forma_pagamento = F.cd_forma_pagamento) "
					+ " LEFT OUTER JOIN adm_tipo_documento  G ON (D.cd_tipo_documento  = G.cd_tipo_documento) "
					+ " WHERE B.cd_fechamento IN ("
					+ fechamentos.toString() + ") ",
					" ORDER BY G.nm_tipo_documento, E.nm_pessoa ",
					null, connect, true);

		} catch (Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getComparativoReceitasDespesas( int cdEmpresa, int nrAno ) {
		return getComparativoReceitasDespesas(cdEmpresa, nrAno, null);
	}
	
	public static ResultSetMap getComparativoReceitasDespesas( int cdEmpresa, int nrAno, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			ResultSetMap rsm = new ResultSetMap();
			for( int i=0; i<Util.meses.length;i++ ){
				HashMap<String, Object> newReg = new HashMap<String, Object>();
				newReg.put("CD_MES", i+1 );
				newReg.put("NM_MES", Util.meses[i] );
				rsm.addRegister(newReg);
			}
			rsm.beforeFirst();
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add( new ItemComparator("cd_empresa", String.valueOf(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER) );
			criterios.add( new ItemComparator("year", String.valueOf(nrAno), ItemComparator.EQUAL, Types.INTEGER) );
			ResultSetMap rsmDespesas = ContaPagarServices.getTotalDespesasMensais(criterios, connect);
			ResultSetMap rsmReceitas = ContaReceberServices.getTotalReceitasMensais(criterios, connect);
			Double vlDespesasAno = 0.0;
			Double vlReceitasAno = 0.0;
			while( rsm.next() ){
				rsm.setValueToField("VL_DESPESA", 0.0);
				rsm.setValueToField("VL_RECEITA", 0.0);
				if( rsmDespesas.locate("month", rsm.getInt("CD_MES") ) ){
					rsm.setValueToField("VL_DESPESA", rsmDespesas.getDouble("VL_CONTA"));
					vlDespesasAno += rsmDespesas.getDouble("VL_CONTA");
				}
				if( rsmReceitas.locate("month", rsm.getInt("CD_MES") ) ){
					rsm.setValueToField("VL_RECEITA", rsmReceitas.getDouble("VL_CONTA"));
					vlReceitasAno += rsmReceitas.getDouble("VL_CONTA");
				}
			}
			
			HashMap<String, Object> regAnoAtual = new HashMap<String, Object>();
			regAnoAtual.put("NM_MES", String.valueOf(nrAno));
			regAnoAtual.put("VL_DESPESA", vlDespesasAno);
			regAnoAtual.put("VL_RECEITA", vlReceitasAno);
			rsm.addRegister(regAnoAtual);
			
			
			vlDespesasAno = 0.0;
			vlReceitasAno = 0.0;
			ArrayList<ItemComparator> criteriosAnoAnterior = new ArrayList<ItemComparator>();
			criteriosAnoAnterior.add( new ItemComparator("cd_empresa", String.valueOf(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER) );
			criteriosAnoAnterior.add( new ItemComparator("year", String.valueOf(nrAno-1), ItemComparator.EQUAL, Types.INTEGER) );
			ResultSetMap rsmDespesasAnoAnterior = ContaPagarServices.getTotalDespesasMensais(criteriosAnoAnterior, connect);
			ResultSetMap rsmReceitasAnoAnterior = ContaReceberServices.getTotalReceitasMensais(criteriosAnoAnterior, connect);
			rsmDespesasAnoAnterior.beforeFirst();
			rsmReceitasAnoAnterior.beforeFirst();
			while( rsmDespesasAnoAnterior.next() ){
				vlDespesasAno += rsmDespesasAnoAnterior.getDouble("VL_CONTA");
			}
			while( rsmReceitasAnoAnterior.next() ){
				vlReceitasAno += rsmReceitasAnoAnterior.getDouble("VL_CONTA");
			}
			HashMap<String, Object> regAnoAnterior = new HashMap<String, Object>();
			regAnoAnterior.put("NM_MES", String.valueOf(nrAno-1));
			regAnoAnterior.put("VL_DESPESA", vlDespesasAno);
			regAnoAnterior.put("VL_RECEITA", vlReceitasAno);
			rsm.addRegister(regAnoAnterior);
			
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
	
	public static ResultSetMap getComparativoReceitasRecebimentos( int cdEmpresa, int nrAno ) {
		return getComparativoReceitasRecebimentos(cdEmpresa, nrAno, null);
	}
	
	public static ResultSetMap getComparativoReceitasRecebimentos( int cdEmpresa, int nrAno, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			ResultSetMap rsm = new ResultSetMap();
			for( int i=0; i<Util.meses.length;i++ ){
				HashMap<String, Object> newReg = new HashMap<String, Object>();
				newReg.put("CD_MES", i+1 );
				newReg.put("NM_MES", Util.meses[i] );
				rsm.addRegister(newReg);
			}
			rsm.beforeFirst();
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add( new ItemComparator("cd_empresa", String.valueOf(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER) );
			criterios.add( new ItemComparator("year", String.valueOf(nrAno), ItemComparator.EQUAL, Types.INTEGER) );
			ResultSetMap rsmRecebimentos = MovimentoContaReceberServices.getTotalRecebimentosMensais(criterios, connect);
			ResultSetMap rsmReceitas = ContaReceberServices.getTotalReceitasMensais(criterios, connect);
			Double vlReceitasAno = 0.0;
			Double vlRecebimentoAno = 0.0;
			while( rsm.next() ){
				rsm.setValueToField("VL_RECEITA", 0.0);
				rsm.setValueToField("VL_RECEBIMENTO", 0.0);
				if( rsmReceitas.locate("month", rsm.getInt("CD_MES") ) ){
					rsm.setValueToField("VL_RECEITA", rsmReceitas.getDouble("VL_CONTA"));
					vlReceitasAno += rsmReceitas.getDouble("VL_CONTA");
				}
				if( rsmRecebimentos.locate("month", rsm.getInt("CD_MES") ) ){
					rsm.setValueToField("VL_RECEBIMENTO", rsmRecebimentos.getDouble("VL_MOVIMENTO"));
					vlRecebimentoAno += rsmRecebimentos.getDouble("VL_MOVIMENTO");
				}
			}
			
			HashMap<String, Object> regAnoAtual = new HashMap<String, Object>();
			regAnoAtual.put("NM_MES", String.valueOf(nrAno));
			regAnoAtual.put("VL_RECEBIMENTO", vlRecebimentoAno);
			regAnoAtual.put("VL_RECEITA", vlReceitasAno);
			rsm.addRegister(regAnoAtual);
			
			
			vlRecebimentoAno = 0.0;
			vlReceitasAno = 0.0;
			ArrayList<ItemComparator> criteriosAnoAnterior = new ArrayList<ItemComparator>();
			criteriosAnoAnterior.add( new ItemComparator("cd_empresa", String.valueOf(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER) );
			criteriosAnoAnterior.add( new ItemComparator("year", String.valueOf(nrAno-1), ItemComparator.EQUAL, Types.INTEGER) );
			ResultSetMap rsmRecebimentosAnoAnterior = MovimentoContaReceberServices.getTotalRecebimentosMensais(criteriosAnoAnterior, connect);
			ResultSetMap rsmReceitasAnoAnterior = ContaReceberServices.getTotalReceitasMensais(criteriosAnoAnterior, connect);
			rsmRecebimentosAnoAnterior.beforeFirst();
			rsmReceitasAnoAnterior.beforeFirst();
			while( rsmRecebimentosAnoAnterior.next() ){
				vlRecebimentoAno += rsmRecebimentosAnoAnterior.getDouble("VL_MOVIMENTO");
			}
			while( rsmReceitasAnoAnterior.next() ){
				vlReceitasAno += rsmReceitasAnoAnterior.getDouble("VL_CONTA");
			}
			HashMap<String, Object> regAnoAnterior = new HashMap<String, Object>();
			regAnoAnterior.put("NM_MES", String.valueOf(nrAno-1));
			regAnoAnterior.put("VL_RECEBIMENTO", vlRecebimentoAno);
			regAnoAnterior.put("VL_RECEITA", vlReceitasAno);
			rsm.addRegister(regAnoAnterior);
			
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

	public static Result gerarRelatorioMovimentacoes(
			ArrayList<ItemComparator> criterios) {
		return gerarRelatorioMovimentacoes(criterios, null);
	}

	public static Result gerarRelatorioMovimentacoes(
			ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			ResultSetMap rsm = new ResultSetMap();
			Object[] obj = findMovimentacaoConta(criterios);
			Double vlTotalPeriodo = 0.0d;
			Double vlTotalPrevisto = 0.0d;
			Double vlCreditoCompensado = 0.0d;
			Double vlTotalCredito = 0.0d;
			Double vlDebitoCompensado = 0.0d;
			Double vlTotalDebito = 0.0d;


			Double vlSaldo = (Double) obj[0];
			rsm = (ResultSetMap) obj[1];

			rsm.beforeFirst();

			while (rsm.next()) {			
				rsm.setValueToField("TP_CONTA", rsm.getObject("TP_CONTA") != null ? rsm.getInt("TP_CONTA") : 0);				
				rsm.setValueToField("CL_SITUACAO", rsm.getObject("ST_MOVIMENTO") != null ? situacaoMovCaixa[rsm.getInt("ST_MOVIMENTO")] : 0);				
				rsm.setValueToField("CL_ORIGEM", rsm.getObject("TP_ORIGEM") != null ? tipoOrigem[rsm.getInt("TP_ORIGEM")] : 0);				
				rsm.setValueToField("CL_TIPO", (rsm.getInt("TP_MOVIMENTO")    == 0 ? "C" : "D"));				

				if( rsm.getInt("TP_MOVIMENTO") == 0 ){
					rsm.setValueToField("VL_CREDITO", rsm.getString("VL_MOVIMENTO"));
					vlTotalCredito += rsm.getDouble("VL_MOVIMENTO");
				}else{
					rsm.setValueToField("VL_DEBITO", rsm.getString("VL_MOVIMENTO"));
					vlTotalDebito += rsm.getDouble("VL_MOVIMENTO");
				}
				//				rsm.setValueToField("VL_CREDITO", (rsm.getInt("TP_MOVIMENTO") == 0 ? rsm.getString("VL_MOVIMENTO") : null));
				//				rsm.setValueToField("VL_DEBITO",  (rsm.getInt("TP_MOVIMENTO") == 1 ? rsm.getString("VL_MOVIMENTO") : null));


				if(rsm.getInt("ST_MOVIMENTO") == 0)	{
					rsm.setValueToField("VL_SALDO", new String("0"));
				} else if(rsm.getInt("ST_MOVIMENTO") == 1 || rsm.getInt("ST_MOVIMENTO") == 2)	{

					if( rsm.getInt("TP_MOVIMENTO") == 0 ){
						vlSaldo += rsm.getDouble("VL_MOVIMENTO");
						vlCreditoCompensado += rsm.getDouble("VL_MOVIMENTO");
					}else{
						vlSaldo -= rsm.getDouble("VL_MOVIMENTO");
						vlDebitoCompensado += rsm.getDouble("VL_MOVIMENTO");

					}

					rsm.setValueToField("VL_SALDO", vlSaldo.toString());
					//rsm.setValueToField("VL_SALDO", new Double(vlSaldo+(rsm.getInt("TP_MOVIMENTO") == 0 ? rsm.getDouble("VL_MOVIMENTO") : rsm.getDouble("VL_MOVIMENTO")*(-1))).toString() );
				}

				if(rsm.getInt("TP_ORIGEM") == toTRANSFERENCIA)	{
					if(rsm.getInt("TP_MOVIMENTO") == CREDITO)	{
						rsm.setValueToField("NM_PESSOA", "Origem: " + rsm.getString("NM_CONTA_ORIGEM"));
					} else {
						rsm.setValueToField("NM_PESSOA", "Destino: "+ (rsm.getString("NM_CONTA_DESTINO") == null ? "" : rsm.getString("NM_CONTA_DESTINO")));
					}
				}				
			}

			HashMap<String, Object> param = new HashMap<String, Object>();

			vlTotalPeriodo = vlCreditoCompensado-vlDebitoCompensado;
			vlTotalPrevisto = vlTotalPeriodo + (vlTotalCredito-vlCreditoCompensado) - (vlTotalDebito-vlDebitoCompensado);

			param.put("vlTotalPeriodo", Util.formatNumber(vlTotalPeriodo));
			param.put("vlTotalPrevisto", Util.formatNumber(vlTotalPrevisto));
			param.put("vlCreditoCompensado", Util.formatNumber(vlCreditoCompensado));
			param.put("vlCreditoPrevisto", Util.formatNumber(vlTotalCredito-vlCreditoCompensado));
			param.put("vlDebitoCompensado", Util.formatNumber(vlDebitoCompensado));
			param.put("vlDebitoPrevisto", Util.formatNumber(vlTotalDebito-vlDebitoCompensado));
			param.put("vlSaldoInicial", Util.formatNumber((Double)obj[0]) );
			param.put("vlSaldoFinal",  Util.formatNumber(vlSaldo));
			param.put("vlSaldoPrevisto", Util.formatNumber( vlSaldo+vlTotalPrevisto));
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			return result;

		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result gerarRelatorioFluxoCaixa(int cdEmpresa, int tpConta,
			int cdConta, int tpOrigem, int nrDiasIntervalo,
			GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			boolean onlyWithMovimento, boolean groupByConta, boolean showDetalhe) {
		return gerarRelatorioFluxoCaixa(cdEmpresa, tpConta,
				cdConta, tpOrigem, nrDiasIntervalo,
				dtInicial, dtFinal,
				onlyWithMovimento, groupByConta, showDetalhe, null);
	}

	public static Result gerarRelatorioFluxoCaixa(int cdEmpresa, int tpConta,
			int cdConta, int tpOrigem, int nrDiasIntervalo,
			GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			boolean onlyWithMovimento, boolean groupByConta, boolean showDetalhe, Connection connect) {
		boolean isConnectionNull = connect==null;
		try	{
			if(isConnectionNull)
				connect = Conexao.conectar();

			ResultSetMap rsm = new ResultSetMap();

			rsm = getFluxoDeCaixa(cdEmpresa, tpConta, cdConta, tpOrigem, nrDiasIntervalo, dtInicial, dtFinal, onlyWithMovimento, groupByConta, showDetalhe);

			while(rsm.next()){	
				rsm.setValueToField("VL_DEBITO", rsm.getString("VL_DEBITO"));
				rsm.setValueToField("VL_CREDITO", rsm.getString("VL_CREDITO"));
				rsm.setValueToField("VL_SALDO", rsm.getString("VL_SALDO"));
				rsm.setValueToField("DS_MOVIMENTO", rsm.getString("DS_MOVIMENTO"));
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

	public static Result gerarRelatorioPrevisaoFluxoCaixa(int cdEmpresa, int tpConta, int cdConta,
			int cdContaCarteira, int cdTipoDocumento,
			int cdClassificacao, int nrDiasIntervalo,
			GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			boolean onlyWithMovimento, boolean showDetalhe) {
		return gerarRelatorioPrevisaoFluxoCaixa(cdEmpresa, tpConta,
				cdConta, cdContaCarteira, cdTipoDocumento, cdClassificacao, nrDiasIntervalo,
				dtInicial, dtFinal,
				onlyWithMovimento, showDetalhe, null);
	}

	public static Result gerarRelatorioPrevisaoFluxoCaixa(int cdEmpresa, int tpConta, int cdConta,
			int cdContaCarteira, int cdTipoDocumento,
			int cdClassificacao, int nrDiasIntervalo,
			GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			boolean onlyWithMovimento, boolean showDetalhe, Connection connect) {
		boolean isConnectionNull = connect==null;
		try	{
			if(isConnectionNull)
				connect = Conexao.conectar();

			ResultSetMap rsm = new ResultSetMap();

			rsm = getPrevisaoFluxoDeCaixa(cdEmpresa, tpConta, cdConta, cdContaCarteira, cdTipoDocumento, cdClassificacao, nrDiasIntervalo, dtInicial, dtFinal, onlyWithMovimento, showDetalhe);

			while(rsm.next()){	
				rsm.setValueToField("VL_DEBITO", rsm.getString("VL_DEBITO"));
				rsm.setValueToField("VL_CREDITO", rsm.getString("VL_CREDITO"));
				rsm.setValueToField("VL_SALDO", rsm.getString("VL_SALDO"));
				rsm.setValueToField("DS_MOVIMENTO", rsm.getString("DS_MOVIMENTO"));
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

	public static Result gerarRelatorioMovimentoExtrato(int cdConta,
			GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int cdFormaPagamento, int stMovimento, int tpMovimento,
			int tpOrigem, String nrDocumento) {
		return gerarRelatorioMovimentoExtrato(cdConta, dtInicial, dtFinal, cdFormaPagamento, stMovimento, tpMovimento, tpOrigem, nrDocumento, null);
	}

	/**
	 * @author Edgard Hufelande
	 * @param cdConta int
	 * @param dtInicial GregorianCalendar
	 * @param dtFinal 
	 * @param cdFormaPagamento
	 * @param stMovimento
	 * @param tpMovimento
	 * @param tpOrigem
	 * @param nrDocumento
	 * @param connect
	 * @return
	 * Método de geração de relatório de movimentação de contas
	 */

	public static Result gerarRelatorioMovimentoExtrato(int cdConta, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdFormaPagamento, int stMovimento, int tpMovimento, int tpOrigem, String nrDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try	{
			if(isConnectionNull)
				connect = Conexao.conectar();

			Object[] object = getMovimentacaoConta(cdConta, dtInicial, dtFinal, cdFormaPagamento, stMovimento, tpMovimento, tpOrigem, nrDocumento);
			ResultSetMap rsm = new ResultSetMap();
			float vlSaldo = ((Double) object[0]).floatValue();
			float vlCreditoPendente = 0;
			float vlDebitoPendente = 0;

			rsm = (ResultSetMap) object[1];

			rsm.beforeFirst();
			while (rsm.next()) {
				rsm.setValueToField("TP_CONTA", rsm.getObject("TP_CONTA") != null ? rsm.getInt("TP_CONTA") : 0);				
				rsm.setValueToField("CL_SITUACAO", rsm.getObject("ST_MOVIMENTO") != null ? situacaoMovBancario[rsm.getInt("ST_MOVIMENTO")] : 0);				
				rsm.setValueToField("CL_ORIGEM", rsm.getObject("TP_ORIGEM") != null ? tipoOrigem[rsm.getInt("TP_ORIGEM")] : 0);				
				rsm.setValueToField("CL_TIPO", (rsm.getInt("TP_MOVIMENTO")    == 0 ? "C" : "D"));				
				rsm.setValueToField("VL_CREDITO", (rsm.getInt("TP_MOVIMENTO") == 0 ? rsm.getString("VL_MOVIMENTO") : null));
				rsm.setValueToField("VL_DEBITO",  (rsm.getInt("TP_MOVIMENTO") == 1 ? rsm.getString("VL_MOVIMENTO") : null));
				if(rsm.getInt("ST_MOVIMENTO") == 0)	{
					rsm.setValueToField("VL_SALDO", "0");
					if( rsm.getInt("TP_MOVIMENTO") == 0 ){
						vlCreditoPendente += rsm.getFloat("VL_MOVIMENTO");
					}else{
						vlDebitoPendente += rsm.getFloat("VL_MOVIMENTO");
					}
				} else if(rsm.getInt("ST_MOVIMENTO") == 1 || rsm.getInt("ST_MOVIMENTO") == 2)	{
					vlSaldo += (rsm.getInt("TP_MOVIMENTO") == 0)? rsm.getFloat("VL_MOVIMENTO") : rsm.getFloat("VL_MOVIMENTO")*(-1);
					rsm.setValueToField("VL_SALDO", new Double(vlSaldo).toString() );
				}

				if(rsm.getInt("TP_ORIGEM") == toTRANSFERENCIA)	{
					if(rsm.getInt("TP_MOVIMENTO") == CREDITO)	{
						rsm.setValueToField("NM_PESSOA", "Origem: " + rsm.getString("NM_CONTA_ORIGEM"));
					} else {
						rsm.setValueToField("NM_PESSOA", "Destino: "+ (rsm.getString("NM_CONTA_DESTINO") == null ? "" : rsm.getString("NM_CONTA_DESTINO")));
					}
				}
				rsm.setValueToField("ST_MOVIMENTO", rsm.getString("ST_MOVIMENTO"));
			}

			rsm.beforeFirst();

			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("vlSaldoInicial",  String.valueOf( object[0] ) );
			param.put("vlSaldoFinal",  String.valueOf( vlSaldo ) );
			param.put("vlSaldoPrevisto",  String.valueOf( vlSaldo+vlCreditoPendente-vlDebitoPendente ) );
			param.put("vlCreditoPendente",  String.valueOf( vlCreditoPendente ) );
			param.put("vlDebitoPendente",  String.valueOf( vlDebitoPendente ) );
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

	public static Result atualizarValoresFechamento(int cdConta, int cdTurno, int cdFormaPagamento, float vlMovimentoReajuste, GregorianCalendar dtMovimento, int cdUsuario){
		return atualizarValoresFechamento(cdConta, cdTurno, cdFormaPagamento, vlMovimentoReajuste, dtMovimento, cdUsuario, null);
	}

	public static Result atualizarValoresFechamento(int cdConta, int cdTurno, int cdFormaPagamento, float vlMovimentoReajuste, GregorianCalendar dtMovimento, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try	{
			if(isConnectionNull)
				connect = Conexao.conectar();

			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_conta", "" + cdConta, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.cd_turno", "" + cdTurno, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.cd_forma_pagamento", "" + cdFormaPagamento, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.dt_movimento", Util.formatDateTime(dtMovimento, "dd/MM/yyyy"), ItemComparator.EQUAL, Types.TIMESTAMP));
			criterios.add(new ItemComparator("A.tp_origem", "" + toREAJUSTE, ItemComparator.DIFFERENT, Types.INTEGER));

			ResultSetMap rsm = MovimentoContaServices.find(criterios, connect, null);
			float vlMovimentoTotal = 0;
			if(rsm.next()){
				if(rsm.getInt("tp_movimento") == CREDITO)
					vlMovimentoTotal = rsm.getFloat("vl_movimento");
				else
					vlMovimentoTotal = -rsm.getFloat("vl_movimento");
				while(rsm.next()){
					if(rsm.getInt("tp_movimento") == CREDITO)
						vlMovimentoTotal += rsm.getFloat("vl_movimento");
					else
						vlMovimentoTotal -= rsm.getFloat("vl_movimento");
				}
			}

			double vlMovimento = 0;
			int tpMovimento = 0;

			if(vlMovimentoReajuste > vlMovimentoTotal){
				vlMovimento = vlMovimentoReajuste - vlMovimentoTotal;
				tpMovimento = CREDITO;
			}
			else{
				vlMovimento = vlMovimentoTotal - vlMovimentoReajuste;
				tpMovimento = DEBITO;
			}

			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_conta", "" + cdConta, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.cd_turno", "" + cdTurno, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.cd_forma_pagamento", "" + cdFormaPagamento, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.tp_origem", "" + toREAJUSTE, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.dt_movimento", Util.formatDateTime(dtMovimento, "dd/MM/yyyy"), ItemComparator.EQUAL, Types.TIMESTAMP));

			ResultSetMap rsmMovimentoReajuste = MovimentoContaServices.find(criterios, connect, null);
			if(rsmMovimentoReajuste.next()){
				MovimentoConta movimentoConta = MovimentoContaDAO.get(rsmMovimentoReajuste.getInt("cd_movimento_conta"), rsmMovimentoReajuste.getInt("cd_conta"), connect);
				movimentoConta.setVlMovimento(vlMovimento);
				movimentoConta.setTpMovimento(tpMovimento);
				if(MovimentoContaDAO.update(movimentoConta, connect) <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar movimento de reajuste");
				}
			}
			else{
				MovimentoConta movimentoConta = new MovimentoConta(0, cdConta, 0/*cdContaOrigem*/, 0/*cdMovimentoOrigem*/, 
						cdUsuario, 0/*cdCheque*/, 0/*cdViagem*/, dtMovimento, 
						vlMovimento, null, tpMovimento, toREAJUSTE,
						ST_COMPENSADO, null/*dsHistorico*/, null/*dtDeposito*/, 
						null/*idExtrato*/, cdFormaPagamento, 0/*cdFechamento*/, cdTurno);
				if(MovimentoContaDAO.insert(movimentoConta, connect) <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir movimento de reajuste");
				}
			}

			ResultSetMap rsmDiferenca = ContaFechamentoServices.getResumoByPagamento(cdConta, dtMovimento, cdTurno, connect);
			float vlDiferenca = 0;
			while(rsmDiferenca.next()){
				if(rsmDiferenca.getInt("cd_forma_pagamento") == cdFormaPagamento){
					vlDiferenca = rsmDiferenca.getFloat("VL_DIFERENCA");							
				}
			}

			Result result = new Result(1);
			result.addObject("VL_DIFERENCA", vlDiferenca);
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

}
