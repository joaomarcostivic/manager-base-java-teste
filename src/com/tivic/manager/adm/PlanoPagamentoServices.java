package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.alm.*;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.FeriadoServices;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class PlanoPagamentoServices {

	public static final int OF_CONTRATO           = 0;
	public static final int OF_DOCUMENTO_SAIDA    = 1;
	public static final int OF_OUTRA_CONTA        = 2;
	public static final int OF_CONTRATO_DOC_SAIDA = 3;

	public static final int ERR_FAT_SAVE_CONTA_RECEBER 	  = -20;
	public static final int ERR_FAT_SAVE_MOVIMENTO        = -30;
	public static final int ERR_NOT_CONTA_DEFINIDO 		  = -40;
	public static final int ERR_NOT_CARTEIRA_DEFINIDO 	  = -50;
	public static final int ERR_VALOR_FATURADO_INVALIDO   = -60;
	public static final int ERR_FAT_SAVE_MOVIMENTO_TITULO = -70;

	public static final String[] tipoFrequencia = {"Sem intervalo","Quantidade fixa", "1 Dia","1 Semana","2 Semanas","2 Semanas por mês",
												   "4 Semanas","1 Mês","2 Meses","3 Meses","6 Meses", "1 Ano","2 Anos"};

	public static final int FRQ_QTD_FIXA 		 = 0;
	public static final int FRQ_DIARIA 			 = 1;
	public static final int FRQ_SEMANAL 		 = 2;
	public static final int FRQ_QUINZENAL 		 = 3;
	public static final int FRQ_DUAS_SEMANAS_MES = 4;
	public static final int FRQ_QUATRO_SEMANAS 	 = 5;
	public static final int FRQ_MENSAL 			 = 6;
	public static final int FRQ_BIMESTRAL 		 = 7;
	public static final int FRQ_TRIMESTRAL 		 = 8;
	public static final int FRQ_SEMESTRAL 		 = 9;
	public static final int FRQ_ANUAL 			 = 10;
	public static final int FRQ_BIANUAL 		 = 11;
	
	public static final int VALE_PROGRAMA_FATURA = 999;
	
	
	public static Result save(PlanoPagamento planoPagamento){
		return save(planoPagamento, null);
	}

	public static Result save(PlanoPagamento planoPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(planoPagamento==null)
				return new Result(-1, "Erro ao salvar. PlanoPagamento é nulo");

			int retorno;
			if(planoPagamento.getCdPlanoPagamento()==0){
				retorno = PlanoPagamentoDAO.insert(planoPagamento, connect);
				planoPagamento.setCdPlanoPagamento(retorno);
			}
			else {
				retorno = PlanoPagamentoDAO.update(planoPagamento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PLANOPAGAMENTO", planoPagamento);
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
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM adm_plano_pagamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static int delete(int cdPlanoPagamento) {
		return delete(cdPlanoPagamento, null);
	}

	public static int delete(int cdPlanoPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			connect.setAutoCommit(false);
			// Plano Parcelamento
			connect.prepareStatement("DELETE FROM adm_plano_parcelamento WHERE cd_plano_pagamento="+cdPlanoPagamento).executeUpdate();
			// Plano Pagamento Produto/Serviço
			connect.prepareStatement("DELETE FROM adm_plano_pagto_produto_servico WHERE cd_plano_pagamento="+cdPlanoPagamento).executeUpdate();

			int ret = PlanoPagamentoDAO.delete(cdPlanoPagamento, connect);

			if(ret>0)
				connect.commit();
			else
				Conexao.rollback(connect);
			return ret;
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagamentoServices.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result lancarFaturamento(int cdEmpresa, int cdForeignKey, int tpForeignKey, GregorianCalendar dtFaturamento,
			int cdConta, int cdContaCarteira, String prefixDocumento, int cdSacado, int cdFormaPagamento, ArrayList<ContaReceberCategoria> categorias,
			int cdUsuario, float vlFaturado, HashMap<String, Object> dadosConfigFat, boolean simulateFat) {
		return lancarFaturamento(cdEmpresa, cdForeignKey, tpForeignKey, dtFaturamento, cdConta, cdContaCarteira, prefixDocumento, cdSacado,
				cdFormaPagamento, categorias, 0, cdUsuario, vlFaturado, dadosConfigFat, simulateFat, null);
	}

	public static Result lancarFaturamento(int cdEmpresa, int cdForeignKey, int tpForeignKey, GregorianCalendar dtFaturamento,
			int cdConta, int cdContaCarteira, String prefixDocumento, int cdSacado, int cdFormaPagamento, ArrayList<ContaReceberCategoria> categorias,
			int cdUsuario, float vlFaturado, HashMap<String, Object> dadosConfigFat, boolean simulateFat, Connection connection) {
		return lancarFaturamento(cdEmpresa, cdForeignKey, tpForeignKey, dtFaturamento, cdConta, cdContaCarteira, prefixDocumento, cdSacado,
				cdFormaPagamento, categorias, 0, cdUsuario, vlFaturado, dadosConfigFat, simulateFat, connection);
	}

	public static Result lancarFaturamento(int cdEmpresa, int cdForeignKey, int tpForeignKey, GregorianCalendar dtFaturamento,
			int cdConta, int cdContaCarteira, String prefixDocumento, int cdSacado, int cdFormaPagamento,
			ArrayList<ContaReceberCategoria> categorias, int cdPlanoPagamento, int cdUsuario, float vlFaturado,
			HashMap<String, Object> dadosConfigFat, boolean simulateFat) {
		return lancarFaturamento(cdEmpresa, cdForeignKey, tpForeignKey, dtFaturamento, cdConta, cdContaCarteira, prefixDocumento, cdSacado,
				cdFormaPagamento, categorias, cdPlanoPagamento, cdUsuario, vlFaturado, dadosConfigFat, simulateFat, null);
	}

	public static Result lancarFaturamento(int cdEmpresa, int cdForeignKey, int tpForeignKey, GregorianCalendar dtFaturamento,
			int cdConta, int cdContaCarteira, String prefixDocumento, int cdSacado, int cdFormaPagamento,
			ArrayList<ContaReceberCategoria> categorias, int cdPlanoPagamento, int cdUsuario, float vlFaturado,
			HashMap<String, Object> dadosConfigFat, boolean simulateFat, Connection connection) 
	{
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			FormaPagamentoEmpresa formPagEmpresa = FormaPagamentoEmpresaDAO.get(cdFormaPagamento, cdEmpresa, connection);
			ArrayList<HashMap<String, Object>> valoresFat = new ArrayList<HashMap<String,Object>>();
			HashMap<String, Object> valorFat = new HashMap<String, Object>();
			valorFat.put("cdForeignKey", cdForeignKey);
			valorFat.put("tpForeignKey", tpForeignKey);
			valorFat.put("vlFaturar", vlFaturado);
			valorFat.put("categorias", categorias);
			valoresFat.add(valorFat);

			Result result = lancarFaturamento(cdEmpresa, valoresFat, dtFaturamento, cdConta, cdContaCarteira, cdSacado,
					                             formPagEmpresa, cdPlanoPagamento, cdUsuario, dadosConfigFat, simulateFat, connection);

			if (result.getCode() <=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return result;
			}

			return new Result(1);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			
			if (!simulateFat && isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar lançar faturamento!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result lancarFaturamento(int cdEmpresa, ArrayList<HashMap<String, Object>> valoresToFaturar,
			GregorianCalendar dtFaturamento, int cdConta, int cdSacado, int cdFormaPagamento,
			int cdPlanoPagamento, int cdUsuario, HashMap<String, Object> dadosConfigFat,
			boolean simulateFat, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			FormaPagamentoEmpresa formPagEmpresa = FormaPagamentoEmpresaDAO.get(cdFormaPagamento, cdEmpresa, connection);
			formPagEmpresa = formPagEmpresa==null ? new FormaPagamentoEmpresa(cdFormaPagamento, cdEmpresa, 0 /*cdAdministrador*/, 0 /*cdTipoDocumento*/,
																			  0 /*qtDiasCredito*/, 0.0 /*vlTarifaTransacao*/, 0.0 /*prTaxaDesconto*/,
																			  0.0 /*prDescontoRave*/, 0 /*cdContaCarteira*/, cdConta) : formPagEmpresa;

			Result result = lancarFaturamento(cdEmpresa, valoresToFaturar, dtFaturamento, cdConta, 0 /*cdContaCarteira*/, cdSacado,
					                             formPagEmpresa, cdPlanoPagamento, cdUsuario, dadosConfigFat, simulateFat, connection);
			if (result.getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return result;
			}

			return new Result(1);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			if (!simulateFat && isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar lançar faturamento!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@SuppressWarnings("unchecked")
	public static Result lancarFaturamento(int cdEmpresa, ArrayList<HashMap<String, Object>> valoresToFaturar,
			GregorianCalendar dtFaturamento, int cdConta, int cdContaCarteira, int cdSacado, FormaPagamentoEmpresa formPagEmpresa,
			int cdPlanoPagamento, int cdUsuario, HashMap<String, Object> dadosConfigFat, boolean simulateFat, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull && !simulateFat ? false : connection.getAutoCommit());

			ArrayList<HashMap<String, Object>> faturas = (ArrayList<HashMap<String,Object>>)dadosConfigFat.get("faturas");
			String nrDocumentoTef = (String)dadosConfigFat.get("nrDocumentoTef");
			ArrayList<TituloCredito> titulos = (ArrayList<TituloCredito>)dadosConfigFat.get("titulos");
			ArrayList<ContratoPlanoPagto> planosPagContrato = new ArrayList<ContratoPlanoPagto>();
			ArrayList<PlanoPagtoDocumentoSaida> planosPagDocSaida = new ArrayList<PlanoPagtoDocumentoSaida>();
			FormaPagamento formPag = FormaPagamentoDAO.get(formPagEmpresa.getCdFormaPagamento(), connection);

			/*
			 *  Se a carteira passada não for válida busca uma carteira da conta passada
			 */
			int cdContaFinanceira = formPagEmpresa.getCdContaCarteira()>0 ? formPagEmpresa.getCdConta() : cdConta;
			cdContaCarteira = formPagEmpresa.getCdContaCarteira()>0 ? formPagEmpresa.getCdContaCarteira() : cdContaCarteira;
			if (cdContaCarteira <= 0)	{
				/* Se a carteira da forma de pagamento não for informada... */
				ResultSet rs = connection.prepareStatement("SELECT cd_conta_carteira " +
						"FROM adm_conta_carteira " +
						"WHERE cd_conta = "+cdConta).executeQuery();
				cdContaCarteira = !rs.next() ? 0 : rs.getInt("cd_conta_carteira");
			}

			int cdTipoDocumentoFat = 0;
			ResultSet rs = connection.prepareStatement("SELECT cd_tipo_documento " +
					"FROM adm_tipo_documento " +
					"WHERE sg_tipo_documento = \'FAT\'").executeQuery();
			cdTipoDocumentoFat = !rs.next() ? 0 : rs.getInt("cd_tipo_documento");

			/*
			 *  Se o tipo de documento não for informado, localiza algum tipo de documento com sigla FAT (fatura)
			 */
			int cdTipoDocumento = formPagEmpresa.getCdTipoDocumento()<=0 ? cdTipoDocumentoFat : formPagEmpresa.getCdTipoDocumento();
			/*
			 *  Faz lançamento das parcelas levando em conta a configuração do plano
			 */
			ResultSetMap rsmParcelas = cdPlanoPagamento<=0 ? new ResultSetMap() :
				new ResultSetMap(connection.prepareStatement("SELECT * " +
					"FROM adm_plano_parcelamento " +
					"WHERE cd_plano_pagamento = "+cdPlanoPagamento+
					"ORDER BY nr_ordem").executeQuery());

			/*
			 * Se não foi definido nenhuma plano de parcelamento lança à vista
			 */
			if(rsmParcelas.size()==0)	{
				HashMap<String,Object> register = new HashMap<String,Object>();
				register.put("NR_DIAS", new Integer(0));
				register.put("QT_PARCELAS", new Integer(1));
				register.put("PR_VALOR_TOTAL", new Float(100));
				rsmParcelas.addRegister(register);
			}

			int cdContaOrigem = 0;
			GregorianCalendar dtEmissao = dtFaturamento!=null ? dtFaturamento : new GregorianCalendar();

			ArrayList<MovimentoContaTituloCredito> movimentoTitulo = new ArrayList<MovimentoContaTituloCredito>();
			ArrayList<MovimentoContaReceber> recebimentos = new ArrayList<MovimentoContaReceber>();
			ArrayList<ContaReceber> contas = new ArrayList<ContaReceber>();
			ArrayList<MovimentoContaCategoria> categoriasMov = new ArrayList<MovimentoContaCategoria>();
			GregorianCalendar dtVencimentoBase = dtFaturamento!=null ? dtFaturamento : new GregorianCalendar();
			dtVencimentoBase.set(Calendar.HOUR, 0);
			dtVencimentoBase.set(Calendar.MINUTE, 0);
			dtVencimentoBase.set(Calendar.SECOND, 0);
			float vlFaturado = 0;
			float vlMovimento = 0;
			ArrayList<ContaReceberCategoria> categorias = new ArrayList<ContaReceberCategoria>();
			/*
			 * Geracao das contas a receber de cada valor a ser faturado
			 */
			for (int i=0; valoresToFaturar!=null && i<valoresToFaturar.size(); i++) {
				HashMap<String, Object> valorToFaturar = (HashMap<String, Object>)valoresToFaturar.get(i);

				float vlConta = ((Float)valorToFaturar.get("vlFaturar")).floatValue();
				int tpForeignKey = ((Integer)valorToFaturar.get("tpForeignKey")).intValue();
				int cdForeignKey = valorToFaturar.get("cdForeignKey")==null ? 0 : ((Integer)valorToFaturar.get("cdForeignKey")).intValue();
				int tpContaReceber = valorToFaturar.get("tpContaReceber")==null ? ContaReceberServices.TP_PARCELA :
									((Integer)valorToFaturar.get("tpContaReceber")).intValue();
				int cdContratoTemp = valorToFaturar.get("cdContrato")==null ? 0 : ((Integer)valorToFaturar.get("cdContrato")).intValue();
				int cdDocumentoSaidaTemp = valorToFaturar.get("cdDocumentoSaida")==null ? 0 : ((Integer)valorToFaturar.get("cdDocumentoSaida")).intValue();
				String dsHistorico = valorToFaturar.get("dsHistorico")==null ? "" : (String)valorToFaturar.get("dsHistorico");

				vlFaturado += vlConta;
				vlMovimento += vlConta;
				ArrayList<ContaReceberCategoria> categoriasFat = (ArrayList<ContaReceberCategoria>)valorToFaturar.get("categorias");

				for (int j=0; categoriasFat!=null && j<categoriasFat.size(); j++) {
					ContaReceberCategoria categoriaTemp = null;
					for (int k=0; k<categorias.size(); k++)
						if (categorias.get(k).getCdCategoriaEconomica() == categoriasFat.get(j).getCdCategoriaEconomica()) {
							categoriaTemp = categorias.get(k);
							break;
						}
					if (categoriaTemp!=null)
						categoriaTemp.setVlContaCategoria(categoriaTemp.getVlContaCategoria() + categoriasFat.get(j).getVlContaCategoria());
					else
						categorias.add((ContaReceberCategoria)categoriasFat.get(j).clone());
				}

				String prefixDoc = (String)valorToFaturar.get("prefixDoc");
				int cdDocumentoSaida = tpForeignKey==OF_DOCUMENTO_SAIDA ? cdForeignKey : tpForeignKey==OF_CONTRATO_DOC_SAIDA ? cdDocumentoSaidaTemp : 0;
				int cdContrato = tpForeignKey==OF_CONTRATO ? cdForeignKey : tpForeignKey==OF_CONTRATO_DOC_SAIDA ? cdContratoTemp : 0;
				int cdFormaPagamento = formPagEmpresa!=null && formPagEmpresa.getCdFormaPagamento()>0 ? formPagEmpresa.getCdFormaPagamento() : 0;
				if (cdContrato>0 && cdDocumentoSaida<=0 && formPagEmpresa!=null)
					planosPagContrato.add(new ContratoPlanoPagto(cdContrato,
							cdPlanoPagamento,
							formPagEmpresa.getCdFormaPagamento(),
							0 /*cdPagamento*/,
							vlConta /*vlPagamento*/,
							cdUsuario,
							0 /*cdMovimentoConta*/,
							0 /*cdConta*/));
				else if (cdDocumentoSaida>0 && formPagEmpresa!=null)
					planosPagDocSaida.add(new PlanoPagtoDocumentoSaida(cdPlanoPagamento, cdDocumentoSaida, cdFormaPagamento, cdUsuario, vlConta, 0 /*vlDesconto*/, "", 0 /*vlAcrescimo*/));
				prefixDoc = prefixDoc==null ? "" : prefixDoc;
				ContaReceber contaReceber = new ContaReceber(0 /*cdContaReceber*/, cdSacado, cdEmpresa, cdContrato /*cdContrato*/,
						                                     (tpForeignKey==OF_OUTRA_CONTA ? cdForeignKey : 0) /*cdContaOrigem*/,
						                                     cdDocumentoSaida /*cdDocumentoSaida*/, cdContaCarteira, cdContaFinanceira, 0 /*cdFrete*/,
						                                     prefixDoc, prefixDoc, 1 /*nrParcela*/, null /*nrReferencia*/, cdTipoDocumentoFat,
						                                     dsHistorico /*dsHistorico*/, dtVencimentoBase, dtEmissao, dtVencimentoBase, null /*dtProrrogacao*/,
						                                     Double.valueOf( Float.toString(vlConta)), 0.0d /*vlAbatimento*/, 0.0d /*vlAcrescimo*/, 0.0d /*vlRecebido*/, ContaReceberServices.ST_EM_ABERTO /*stConta*/,
						                                     0 /*tpFrequencia*/, 0 /*qtParcelas*/, tpContaReceber, 0 /*cdNegociacao*/, "" /*txtObservacao*/,
						                                     cdDocumentoSaida<=0 ? 0 : cdFormaPagamento>0 ? cdPlanoPagamento : 0,
						                                     cdDocumentoSaida<=0 ? 0 : cdFormaPagamento, new GregorianCalendar(), dtVencimentoBase, 0/*cdTurno*/, 0.0d/*prJuros*/, 0.0d/*prMulta*/, 0/*lgProtesto*/);
				contas.add(contaReceber);
				/*
				 *  Insere conta a receber no banco de dados
				 */
				if (cdDocumentoSaida>0 && cdFormaPagamento>0 && cdPlanoPagamento>0) {
					if (PlanoPagtoDocumentoSaidaDAO.get(cdPlanoPagamento, cdDocumentoSaida, cdFormaPagamento, connection) == null) {
						if (PlanoPagtoDocumentoSaidaDAO.insert(new PlanoPagtoDocumentoSaida(cdPlanoPagamento, cdDocumentoSaida,cdFormaPagamento, cdUsuario, vlConta, 0/*vlDesconto*/, "", 0 /*vlAcrescimo*/), connection) <= 0){
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(ERR_FAT_SAVE_CONTA_RECEBER, "Erro ao salvar a forma e o plano de pagamento escolhidos!");
						}
					}
				}
				Result result = simulateFat ? new Result(0) : ContaReceberServices.insert(contaReceber, categoriasFat, null, true, connection);
				contaReceber.setCdContaReceber(result.getCode());
				if(contaReceber.getCdContaReceber() <= 0 && !simulateFat)	{
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(ERR_FAT_SAVE_CONTA_RECEBER, "Erro ao tentar inserir uma conta a receber");
				}

				/*
				 *  Lança recebimento da conta (associação entre conta a receber e movimento de conta)
				 */
				recebimentos.add(new MovimentoContaReceber(cdContaFinanceira,0/*cdMovimentoConta*/,contaReceber.getCdContaReceber(),
														   vlConta, 0 /*vlJuros*/,0 /*vlMulta*/,0 /*vlDesconto*/,0 /*vlTarifaCobranca*/,
														   0 /*cdArquivo*/,0 /*cdRegistro*/));
				contaReceber.setVlRecebido(Double.valueOf( Float.toString(vlConta)));
				contaReceber.setStConta(ContaReceberServices.ST_RECEBIDA);
				for(int x=0; categoriasFat!=null && x<categoriasFat.size(); x++)	{
					categoriasMov.add(new MovimentoContaCategoria(cdContaFinanceira, 0/*cdMovimentoConta*/, categoriasFat.get(x).getCdCategoriaEconomica(),
															      categoriasFat.get(x).getVlContaCategoria(),0 /*cdMovimentoContaCategoria*/,
															      0 /*cdContaPagar*/, contaReceber.getCdContaReceber(), 
															      MovimentoContaCategoriaServices.TP_PRE_CLASSIFICACAO /*tpMovimento*/, categoriasFat.get(x).getCdCentroCusto()));
				}
			}

			if (vlFaturado<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(ERR_VALOR_FATURADO_INVALIDO, "Valor a faturar inválido!");
			}

			if(formPagEmpresa!=null && formPag.getTpFormaPagamento()==FormaPagamentoServices.TEF)
				cdSacado 	= formPagEmpresa.getCdAdministrador();

			/*
			 * Cria contas à receber de acordo plano de pagamento e forma de pagamento
			 */
			String prefixDocumento = "";
			if (formPagEmpresa!=null && formPag.getTpFormaPagamento()!=FormaPagamentoServices.MOEDA_CORRENTE) {
				ResultSetMap rsmPessoa = null;
				rsmParcelas.beforeFirst();
				int nrParcela  = 0;
				int qtParcelas = 0;
				while(rsmParcelas.next())
					qtParcelas += rsmParcelas.getInt("qt_parcelas");
				rsmParcelas.beforeFirst();
				int indexTitulo = 0;
				while(rsmParcelas.next())	{
					/*
					 *  Cria conta a receber
					 */
					GregorianCalendar dtVencimento = (GregorianCalendar)dtVencimentoBase.clone();
					if(rsmParcelas.getInt("nr_dias")==30)
						dtVencimento.add(Calendar.MONTH, 1);
					else
						dtVencimento.add(Calendar.DATE, rsmParcelas.getInt("nr_dias"));
					Double vlDesconto = 0.0;
					// Se for TEF coloca as contas a receber para o dia configurado na forma de pagamento
					if(formPagEmpresa!=null && formPag.getTpFormaPagamento()==FormaPagamentoServices.TEF)	{
						if(formPagEmpresa.getQtDiasCredito()==30)
							dtVencimento.add(Calendar.MONTH, 1);
						else
							dtVencimento.add(Calendar.DATE, formPagEmpresa.getQtDiasCredito());
						vlDesconto = (vlFaturado * formPagEmpresa.getPrTaxaDesconto() / 100);
					}
					int nrDiaVencimento = dtVencimento!=null ? dtVencimento.get(Calendar.DATE) : 0;

					for(int i=0; i<rsmParcelas.getInt("qt_parcelas"); i++)	{
						GregorianCalendar venc = (GregorianCalendar)dtVencimento.clone();
						while(venc.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY || venc.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || FeriadoServices.isFeriado(venc, connection))
							venc.add(Calendar.DATE, 1);

						TituloCredito tituloCustom = formPag.getTpFormaPagamento()==FormaPagamentoServices.TITULO_CREDITO &&
													titulos!=null && indexTitulo<titulos.size() ? titulos.get(indexTitulo++) : null;

						Double vlConta = tituloCustom!=null ?
								tituloCustom.getVlTitulo() : (rsmParcelas.getDouble("pr_valor_total") / rsmParcelas.getInt("qt_parcelas") / 100) * vlFaturado;
						for (int j=0; faturas!=null && j<faturas.size(); j++)
							if (faturas.get(j).get("nrParcela")!=null && ((Integer)faturas.get(j).get("nrParcela")).intValue()==nrParcela) {
								vlConta = (Double) faturas.get(j).get("vlParcela");
								break;
							}

						String nrDocumento = formPag.getTpFormaPagamento()==FormaPagamentoServices.TEF && nrDocumentoTef!=null ?
								nrDocumentoTef + "-" + Util.fillNum(nrParcela, 2) : tituloCustom!=null ? tituloCustom.getNrDocumento() :
								prefixDocumento + "-" + Util.fillNum(nrParcela, 2);
						String idDocumento = formPag.getTpFormaPagamento()==FormaPagamentoServices.TEF && nrDocumentoTef!=null ?
								nrDocumentoTef : tituloCustom!=null ? tituloCustom.getNrDocumento() : prefixDocumento;
						ContaReceber contaReceber = new ContaReceber(0 /*cdContaReceber*/, cdSacado, cdEmpresa, 0 /*cdContrato*/, cdContaOrigem,
								                                     0 /*cdDocumentoSaida*/, cdContaCarteira, cdContaFinanceira, 0 /*cdFrete*/,
								                                     nrDocumento, idDocumento, nrParcela, idDocumento /*nrReferencia*/,
								                                     cdTipoDocumento, null /*dsHistorico*/, tituloCustom!=null ? tituloCustom.getDtVencimento() : venc,
								                                     dtEmissao, null /*dtRecebimento*/, null/*dtProrrogacao*/, vlConta ,
								                                     (vlDesconto / vlFaturado * vlConta),
								                                     0.0d/*vlAcrescimo*/, 0.0d /*vlRecebido*/,
								                                     ContaReceberServices.ST_EM_ABERTO, 0 /*tpFrequencia*/, qtParcelas,
								                                     ContaReceberServices.TP_PARCELA /*tpContaReceber*/, 0 /*cdNegociacao*/,
								                                     null /*txtObservacao*/, cdPlanoPagamento, formPagEmpresa.getCdFormaPagamento(),
								                                     new GregorianCalendar(), tituloCustom!=null ? tituloCustom.getDtVencimento() : venc, 0/*cdTurno*/, 0.0d/*prJuros*/, 0.0d/*prMulta*/, 0/*lgProtesto*/);
						nrParcela++;
						contas.add(contaReceber);
						ArrayList<ContaReceberCategoria> categoriasTemp = new ArrayList<ContaReceberCategoria>();
						for(int j=0; categorias!=null && j<categorias.size(); j++)	{
							Double vlCategoria = 0.0;
							if(vlFaturado > 0 && vlConta > 0)
								vlCategoria = categorias.get(j).getVlContaCategoria() / vlFaturado * vlConta;
							categoriasTemp.add(new ContaReceberCategoria(0/*cdContaReceber*/, categorias.get(j).getCdCategoriaEconomica(), vlCategoria, categorias.get(j).getCdCentroCusto()));
						}
						/*
						 *  Insere conta a receber no banco de dados
						 */
						Result result = simulateFat ? new Result(0) : ContaReceberServices.insert(contaReceber, categoriasTemp, null, true, connection);
						contaReceber.setCdContaReceber(result.getCode());
						if(contaReceber.getCdContaReceber() <= 0 && !simulateFat)	{
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(ERR_FAT_SAVE_CONTA_RECEBER, "Erro ao incluir conta a receber!");
						}

						if(rsmPessoa==null){
							ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
							crt.add(new ItemComparator("A.cd_pessoa", String.valueOf(cdSacado), ItemComparator.EQUAL, Types.INTEGER));
							rsmPessoa = PessoaServices.find(crt);
						}
						rsmPessoa.beforeFirst();
						boolean existsSacado = rsmPessoa.next();
						int tpDocumentoEmissor 		= TituloCreditoServices.tdocCPF;
						String nrDocumentoEmissor 	= existsSacado ? rsmPessoa.getString("nr_cpf") : null;
						if(existsSacado && rsmPessoa.getInt("gn_pessoa")==PessoaServices.TP_JURIDICA)	{
							nrDocumentoEmissor	= rsmPessoa.getString("nr_cnpj");
							tpDocumentoEmissor 	= TituloCreditoServices.tdocCNPJ;
						}

						TituloCredito tituloCredito = tituloCustom!=null ? tituloCustom : 
							                                               new TituloCredito(0, 0 /*cdInstituicaoFinanceira*/, 0 /*cdAlinea*/,
							                                            		   contaReceber.getNrDocumento(), nrDocumentoEmissor, tpDocumentoEmissor,
							                                            		   !existsSacado ? "" : rsmPessoa.getString("nm_pessoa"), vlConta,
							                                            		   TituloCreditoServices.teAPRAZO, "" /*dsObservacao*/, venc,
							                                            		   null /*dtCredito*/, TituloCreditoServices.stEM_ABERTO,
							                                            		   "" /*dsObservacaoo*/, cdTipoDocumento, TituloCreditoServices.tcPORTADOR,
							                                            		   cdContaFinanceira, contaReceber.getCdContaReceber(), 0, 0, "");
						if (tituloCustom != null) {
							tituloCredito.setCdConta(cdContaFinanceira);
							tituloCredito.setCdTipoDocumento(cdTipoDocumento);
							tituloCredito.setStTitulo(TituloCreditoServices.stEM_ABERTO);
							tituloCredito.setCdContaReceber(contaReceber.getCdContaReceber());
						}
						tituloCredito.setCdTituloCredito(simulateFat ? 0 : TituloCreditoDAO.insert(tituloCredito, connection));
						movimentoTitulo.add(new MovimentoContaTituloCredito(tituloCredito.getCdTituloCredito(), 0/*cdMovimentoConta*/, cdContaFinanceira));
						dtVencimento = ContaPagarServices.getProximoVencimento(dtVencimento, rsmParcelas.getInt("tp_intervalo"), nrDiaVencimento);
					}
				}
			}

			/*
			 *  Cria movimento de conta
			 */
			if (!simulateFat && formPagEmpresa!=null) {
				int stMovimentoConta = formPag.getTpFormaPagamento()==FormaPagamentoServices.MOEDA_CORRENTE ? MovimentoContaServices.ST_COMPENSADO
                        : MovimentoContaServices.ST_NAO_COMPENSADO;

				MovimentoConta movimento = new MovimentoConta(0 /*cdMovimentoConta*/,
						cdContaFinanceira,
						0 /*cdContaOrigem*/,
						0 /*cdMovimentoOrigem*/,
						cdUsuario,
						0 /*cdCheque*/,
						0/*cdViagem*/,
						dtEmissao /*dtMovimento*/,
						vlMovimento /*vlMovimento*/,
						"" /*nrDocumento*/,
						MovimentoContaServices.CREDITO,
						MovimentoContaServices.toPAGAMENTO,
						stMovimentoConta,
						"Recebimento" /*dsHistorico*/,
						null /*dtDeposito*/,
						null /*idExtrato*/,
						formPagEmpresa.getCdFormaPagamento(),
						0 /*cdFechamento*/,0/*cdTurno*/);

				/*
				*  Insere movimento da conta no banco de dados
				*/
				int cdMovimentoConta = MovimentoContaServices.insert(movimento, recebimentos, categoriasMov, movimentoTitulo, -1, connection).getCode();
				// Se o movimento da conta não foi incluido com sucesso aborta
				if(cdMovimentoConta <= 0)	{
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(ERR_FAT_SAVE_MOVIMENTO, "Erro ao salvar o movimento na conta!");
				}
				/*
				*  Classificação do movimento na conta
				*/

				for (int i=0; i<planosPagContrato.size(); i++) {
					planosPagContrato.get(i).setCdMovimentoConta(cdMovimentoConta);
					planosPagContrato.get(i).setCdConta(movimento.getCdConta());
					if (ContratoPlanoPagtoDAO.insert(planosPagContrato.get(i), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(ERR_FAT_SAVE_MOVIMENTO_TITULO, "Erro ao salvar o título de crédito no movimento de conta!");
					}
				}
			}

			if (!simulateFat) {
				for (int i=0; i<planosPagDocSaida.size(); i++) {
					PlanoPagtoDocumentoSaida planoPag = planosPagDocSaida.get(i);
					PlanoPagtoDocumentoSaida planoPagTemp = PlanoPagtoDocumentoSaidaDAO.get(planoPag.getCdPlanoPagamento(),
							planoPag.getCdDocumentoSaida(), planoPag.getCdFormaPagamento(), connection);
					
					if (planoPagTemp!=null) {
						planoPagTemp.setVlPagamento(planoPag.getVlPagamento() + planoPagTemp.getVlPagamento());
						if (PlanoPagtoDocumentoSaidaDAO.update(planoPag, connection) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao salvar a forma e plano de pagamento escolhido!");
						}
					}
					
					else if (PlanoPagtoDocumentoSaidaDAO.insert(planoPag, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao salvar a forma e plano de pagamento escolhido!");
					}
				}
			}

			if (!simulateFat && isConnectionNull)
				connection.commit();
			Result result = new Result(1);
			result.addObject("contas", contas);
			result.addObject("tipoDoc", TipoDocumentoDAO.get(cdTipoDocumento, connection));
			result.addObject("recebimentos", recebimentos);
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (!simulateFat && isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar lançar faturamento!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getPlanosPagamentoTo(int cdFormaPagamento, int cdDocumentoSaida) {
		return getPlanosPagamentoTo(cdFormaPagamento, cdDocumentoSaida, null);
	}

	public static ResultSetMap getPlanosPagamentoTo(int cdFormaPagamento, int cdDocumentoSaida, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			int cdEmpresa = 0;
			if(cdDocumentoSaida>0)	{
				DocumentoSaida docSaida = DocumentoSaidaDAO.get(cdDocumentoSaida, connect);
				cdEmpresa = docSaida!=null ? docSaida.getCdEmpresa() : 0;
			}
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement(
											 "SELECT B.*, A.pr_desconto_maximo, (SELECT SUM(nr_dias) FROM adm_plano_parcelamento X " +
					                         "             WHERE A.cd_plano_pagamento = X.cd_plano_pagamento) AS nr_dias, " +
					                         "            (SELECT SUM(qt_parcelas) FROM adm_plano_parcelamento X " +
							                 "             WHERE A.cd_plano_pagamento = X.cd_plano_pagamento) AS qt_parcelas " +
											 "FROM adm_forma_plano_pagamento A " +
											 "JOIN adm_plano_pagamento B ON (A.cd_plano_pagamento = B.cd_plano_pagamento) " +
											 "WHERE 1=1 " +
											 (cdEmpresa>0		? " AND A.cd_empresa         = "+cdEmpresa:"")+
											 (cdFormaPagamento>0? " AND A.cd_forma_pagamento = "+cdFormaPagamento:"")+
									         " ORDER BY nm_plano_pagamento ");
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

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement(
					                         "SELECT * " +
									         "FROM adm_plano_pagamento A " +
									         "ORDER BY nm_plano_pagamento ");
			return new ResultSetMap(pstmt.executeQuery());
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

	public static Result getDefaultTitulosCredito(int cdPlanoPagamento, int cdSacado, float vlFaturado, boolean toResultSet) {
		return getDefaultTitulosCredito(cdPlanoPagamento, cdSacado, vlFaturado, toResultSet, null);
	}

	public static Result getDefaultTitulosCredito(int cdPlanoPagamento, int cdSacado, float vlFaturado, boolean toResultSet, Connection connection) 
	{
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			ArrayList<TituloCredito> titulos = new ArrayList<TituloCredito>();
			ResultSetMap rsm = toResultSet ? new ResultSetMap() : null;

			ResultSetMap rsmParcelas = cdPlanoPagamento<=0 ? new ResultSetMap() :
				new ResultSetMap(connection.prepareStatement("SELECT * " +
					"FROM adm_plano_parcelamento " +
					"WHERE cd_plano_pagamento = "+cdPlanoPagamento+
					"ORDER BY nr_ordem").executeQuery());

			/*
			 * Se não foi definido nenhuma plano de parcelamento lança à vista
			 */
			if(rsmParcelas.size()==0)	{
				HashMap<String,Object> register = new HashMap<String,Object>();
				register.put("NR_DIAS", new Integer(0));
				register.put("QT_PARCELAS", new Integer(1));
				register.put("PR_VALOR_TOTAL", new Float(100));
				rsmParcelas.addRegister(register);
			}

			ResultSetMap rsmPessoa = null;

			GregorianCalendar dtVencimentoBase = new GregorianCalendar();
			dtVencimentoBase.set(Calendar.HOUR, 0);
			dtVencimentoBase.set(Calendar.MINUTE, 0);
			dtVencimentoBase.set(Calendar.SECOND, 0);

			rsmParcelas.beforeFirst();
			while(rsmParcelas.next())	{
				GregorianCalendar dtVencimento = (GregorianCalendar)dtVencimentoBase.clone();
				if(rsmParcelas.getInt("nr_dias")==30)
					dtVencimento.add(Calendar.MONTH, 1);
				else
					dtVencimento.add(Calendar.DATE, rsmParcelas.getInt("nr_dias"));
				int nrDiaVencimento = dtVencimento!=null ? dtVencimento.get(Calendar.DATE) : 0;
				for(int i=0; i<rsmParcelas.getInt("qt_parcelas"); i++)	{
					GregorianCalendar venc = (GregorianCalendar)dtVencimento.clone();
					while(venc.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY || venc.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || FeriadoServices.isFeriado(venc, connection))
						venc.add(Calendar.DATE, 1);

					Double vlConta = ((rsmParcelas.getDouble("pr_valor_total") / rsmParcelas.getInt("qt_parcelas") / 100) * vlFaturado);

					if(rsmPessoa==null){
						ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
						crt.add(new ItemComparator("A.cd_pessoa", String.valueOf(cdSacado), ItemComparator.EQUAL, Types.INTEGER));
						rsmPessoa = PessoaServices.find(crt);
					}
					rsmPessoa.beforeFirst();
					boolean existsSacado = rsmPessoa.next();
					int tpDocumentoEmissor 		= TituloCreditoServices.tdocCPF;
					String nrDocumentoEmissor 	= existsSacado ? rsmPessoa.getString("nr_cpf") : null;
					if(existsSacado && rsmPessoa.getInt("gn_pessoa")==PessoaServices.TP_JURIDICA)	{
						nrDocumentoEmissor	= rsmPessoa.getString("nr_cnpj");
						tpDocumentoEmissor 	= TituloCreditoServices.tdocCNPJ;
					}
					TituloCredito tituloCredito = new TituloCredito(0, 0 /*cdInstituicaoFinanceira*/, 0 /*cdAlinea*/, "" /*nrDocumento*/,
																	nrDocumentoEmissor, tpDocumentoEmissor, !existsSacado ? "" : rsmPessoa.getString("nm_pessoa"),
																    vlConta, TituloCreditoServices.teAPRAZO, "" /*dsObservacao*/, venc, null /*dtCredito*/,
																    TituloCreditoServices.stEM_ABERTO, "", 0 /*cdTipoDocumento*/, TituloCreditoServices.tcPORTADOR,
																    0 /*cdContaFinanceira*/, 0 /*cdContaReceber*/, 0, 0, "");
					titulos.add(tituloCredito);
					if (rsm!=null)
						rsm.addRegister(TituloCreditoServices.toRegister(tituloCredito));
					dtVencimento = ContaPagarServices.getProximoVencimento(dtVencimento, rsmParcelas.getInt("tp_intervalo"), nrDiaVencimento);
				}
			}
			if (rsm!=null)
				rsm.beforeFirst();
			Result result = new Result(1);
			result.addObject("rsm", rsm);
			result.addObject("titulos", titulos);
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar recuperar títulos de créditos padrão!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}

	}


	/**
	 * Metodo que busca todos os planos de pagamento, porém busca informações extras quando relacionados com o programa de fatura passado
	 * @since 09/08/2014
	 * @author Gabriel
	 * @param cdProgramaFatura
	 * @return
	 */
	public static ResultSetMap getAllByProgramaFatura(int cdProgramaFatura){
		return getAllByProgramaFatura(cdProgramaFatura, null);
	}
	public static ResultSetMap getAllByProgramaFatura(int cdProgramaFatura, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			//Buscar todos os planos de pagamento, porém para aqueles relacionados com o cdProgramaFatura passado em adm_programa_fatura_plano, trazer as informações da tabela B
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM adm_plano_pagamento A " +
					                        "						LEFT OUTER JOIN adm_programa_fatura_plano B ON (A.cd_plano_pagamento = B.cd_plano_pagamento"
					                        + "																			AND B.cd_programa_fatura = "+cdProgramaFatura+") " +
									        "					ORDER BY nm_plano_pagamento ");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagamentoServices.getAllByProgramaFatura: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}