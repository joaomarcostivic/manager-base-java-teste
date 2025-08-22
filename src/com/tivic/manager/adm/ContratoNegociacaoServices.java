package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.FeriadoServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

public class ContratoNegociacaoServices {

	/* codificação de erros retornados por rotinas da classe */
	public static final int ERR_GENERICO = -1;
	public static final int ERR_DELETE_CONTA_RECEBER = -2;
	public static final int ERR_GENERATE_CONTA_RECEBER = -3;

	/* tipos de negociacoes */
	public static final int TP_NEGOCIACAO_PARCELAS = 0;
	public static final int TP_ALTERACAO_CONTRATUAL = 1;
	public static final int TP_RESCISAO = 2;
	public static final int TP_FATURAMENTO_CONVENIO = 3;
	public static final int TP_OUTROS = 4;

	public static final String[] tiposNegociacao = {"Negociação de parcelas",
		"Alterações contratuais",
		"Rescisão",
		"Refaturamento (convênios)",
		"Outros"};

	/* situacao das negociacoes */
	public static final int ST_EM_ABERTO = 0;
	public static final int ST_ENCERRADO = 1;
	public static final int ST_CANCELADO = 3;

	public static final String[] situacoesNegociacao = {"Em aberto",
		"Encerrado",
		"Cancelado"};

	public static HashMap<String, Object> insert(ContratoNegociacao negociacao, ArrayList<ContaReceberNegociacao> contas) {
		return insert(negociacao, contas, null);
	}

	public static HashMap<String, Object> insert(ContratoNegociacao negociacao, ArrayList<ContaReceberNegociacao> contas,
			Connection connect){
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("codeResult", ERR_GENERICO);
		boolean isConnectionNull = connect==null;
		try {
			ArrayList<ContaReceber> parcelas = new ArrayList<ContaReceber>();
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			int cdNegociacao = 0;
			if ((cdNegociacao = ContratoNegociacaoDAO.insert(negociacao, connect)) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return hash;
			}
			negociacao.setCdNegociacao(cdNegociacao);

			for (int i=0; contas!=null && i<contas.size(); i++) {
				contas.get(i).setCdNegociacao(cdNegociacao);
				if (ContaReceberNegociacaoDAO.insert(contas.get(i), connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return hash;
				}
			}

			if (isConnectionNull)
				connect.commit();

			hash.put("codeResult", 1);
			hash.put("cdNegociacao", cdNegociacao);
			hash.put("parcelas", parcelas);
			return hash;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoNegociacaoServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return hash;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static HashMap<String, Object> update(ContratoNegociacao negociacao, ArrayList<ContaReceberNegociacao> contas) {
		return update(negociacao, contas, null);
	}

	public static HashMap<String, Object> update(ContratoNegociacao negociacao, ArrayList<ContaReceberNegociacao> contas,
			Connection connect){
		HashMap<String, Object> hash = new HashMap<String, Object>();
		ArrayList<ContaReceber> parcelas = null;
		hash.put("codeResult", ERR_GENERICO);
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			if (ContratoNegociacaoDAO.update(negociacao, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return hash;
			}

			if (contas != null) {
				PreparedStatement pstmt = connect.prepareStatement("DELETE " +
						"FROM adm_conta_receber_negociacao " +
						"WHERE cd_contrato = ? " +
						"  AND cd_negociacao = ?");
				pstmt.setInt(1, negociacao.getCdContrato());
				pstmt.setInt(2, negociacao.getCdNegociacao());
				pstmt.execute();
			}

			for (int i=0; contas!=null && i<contas.size(); i++) {
				contas.get(i).setCdNegociacao(negociacao.getCdNegociacao());
				if (ContaReceberNegociacaoDAO.insert(contas.get(i), connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return hash;
				}
			}

			if (isConnectionNull)
				connect.commit();

			hash.put("codeResult", 1);
			hash.put("parcelas", parcelas);
			return hash;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoNegociacaoServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return hash;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result finalizarNegociacao(int cdContrato, int cdNegociacao, int qtParcelas, int nrDiaVencimento,
			GregorianCalendar dtPrimeiroVencimento, int cdConta, int cdContaCarteira, int cdTipoDocumento) {
		return finalizarNegociacao(cdContrato, cdNegociacao, qtParcelas, nrDiaVencimento, dtPrimeiroVencimento, cdConta, cdContaCarteira,
				cdTipoDocumento, null);
	}

	public static Result finalizarNegociacao(int cdContrato, int cdNegociacao, int qtParcelas, int nrDiaVencimento,
			GregorianCalendar dtPrimeiroVencimento, int cdConta, int cdContaCarteira, int cdTipoDocumento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			ContratoNegociacao negociacao = ContratoNegociacaoDAO.get(cdContrato, cdNegociacao, connection);
			negociacao.setQtParcelas(qtParcelas);
			negociacao.setNrDiaVencimento(nrDiaVencimento);
			negociacao.setDtPrimeiroVencimento(dtPrimeiroVencimento);
			if (ContratoNegociacaoDAO.update(negociacao, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Problemas encontrados ao finalizar negociação e realizar faturamento, ao atualizar dados referentes " +
						"às condições de pagamento e parcelamento. Entre em contato com o suporte técnico");
			}

			Result results = finalizarNegociacao(cdContrato, cdNegociacao, cdConta, cdContaCarteira, cdTipoDocumento, connection);
			if (results.getCode()<=0 && isConnectionNull)
				Conexao.rollback(connection);
			else if (isConnectionNull)
				connection.commit();

			return results;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Problemas encontrados ao finalizar negociação e realizar faturamento. Anote a mensagem de erro abaixo " +
					"e entre em contato com o suporte técnico.", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result finalizarNegociacao(int cdContrato, int cdNegociacao, int cdConta, int cdContaCarteira,
			int cdTipoDocumento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			ArrayList<ContaReceberCategoria> categorias = new ArrayList<ContaReceberCategoria>();

			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_conta_receber " +
					"FROM adm_conta_receber_negociacao " +
					"WHERE cd_contrato = ? " +
					"  AND cd_negociacao = ?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdNegociacao);
			ResultSet rs = pstmt.executeQuery();
			ArrayList<Integer> contasCanceladas = new ArrayList<Integer>();

			while (rs.next()) {
				ContaReceber contaReceber = ContaReceberDAO.get(rs.getInt("cd_conta_receber"), connection);
				contasCanceladas.add(contaReceber.getCdContaReceber());
				if (contaReceber.getStConta() != ContaReceberServices.ST_CANCELADA) {
					if (contaReceber.getStConta() != ContaReceberServices.ST_EM_ABERTO) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Problemas encontrados ao finalizar negociação e realizar faturamento. Certifique-se de " +
								"nesta negociação não estejam incluídas faturas que já estejam canceladas.");
					}
					contaReceber.setStConta(ContaReceberServices.ST_CANCELADA);
					if (ContaReceberDAO.update(contaReceber, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Problemas encontrados ao finalizar negociação e realizar faturamento, ao efetivar o " +
								"cancelamento de fatura incluída na negociação (n° documento " + contaReceber.getNrDocumento() + "). " +
								"Entre em contato com o suporte técnico.");
					}
				}

				if (contaReceber.getVlConta()<=0)
					continue;
				ArrayList<ContaReceberCategoria> categoriasConta = new ArrayList<ContaReceberCategoria>();

				pstmt = connection.prepareStatement("SELECT * " +
						"FROM adm_conta_receber_categoria " +
						"WHERE cd_conta_receber = ?");
				pstmt.setInt(1, rs.getInt("cd_conta_receber"));
				ResultSet rsCat = pstmt.executeQuery();
				while (rsCat.next())
					categoriasConta.add(new ContaReceberCategoria(0 /*cdContaReceber*/,
							rsCat.getInt("cd_categoria_economica"),
							rsCat.getFloat("vl_conta_categoria") +
							(rsCat.getFloat("vl_conta_categoria")/contaReceber.getVlConta().floatValue()) * contaReceber.getVlAcrescimo().floatValue() -
							(rsCat.getFloat("vl_conta_categoria")/contaReceber.getVlConta().floatValue()) * contaReceber.getVlAbatimento().floatValue(), rsCat.getInt("cd_centro_custo")));

				pstmt = connection.prepareStatement("SELECT * " +
						"FROM adm_movimento_conta_receber " +
						"WHERE cd_conta = ?");
				pstmt.setInt(1, rs.getInt("cd_conta_receber"));
				ResultSet rsPagamentos = pstmt.executeQuery();
				while (rsPagamentos.next()) {
					float vlPago = rsPagamentos.getFloat("vl_recebido") + rsPagamentos.getFloat("vl_desconto");
					for (int i=0; vlPago>0 && i<categoriasConta.size(); i++) {
						ContaReceberCategoria catTemp = categoriasConta.get(i);
						catTemp.setVlContaCategoria(catTemp.getVlContaCategoria() -
								catTemp.getVlContaCategoria() * vlPago/contaReceber.getVlConta().floatValue());
					}
				}

				for (int i=0; i<categoriasConta.size(); i++) {
					ContaReceberCategoria categoria = null;
					for (int j=0; j<categorias.size(); j++)
						if (categorias.get(j).getCdCategoriaEconomica() == categoriasConta.get(i).getCdCategoriaEconomica()) {
							categoria = categorias.get(j);
							break;
						}
					if (categoria==null)
						categorias.add(categoriasConta.get(i));
					else
						categoria.setVlContaCategoria(categoria.getVlContaCategoria() + categoriasConta.get(i).getVlContaCategoria());
				}
			}

			/* exclue parcelas referentes a esta negociações, se geradas anteriormente; se houver recebimentos relativos a
			 * estas parcelas, provalvemente a rotina acusará erro de exclusão e, consequentemente, impossibilitará uma nova finalização
			 * da negociação
			 */
			pstmt = connection.prepareStatement("SELECT * " +
					"FROM adm_conta_receber " +
					"WHERE cd_contrato = ? " +
					"  AND cd_negociacao = ?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdNegociacao);
			rs = pstmt.executeQuery();
			while (rs.next())
				if (ContaReceberServices.delete(rs.getInt("cd_conta_receber"), true, false, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Problemas encontrados ao finalizar negociação e realizar faturamento. Se o faturamento " +
							"já foi realizado anteriormente, verifique se já não existem pagamentos referentes à alguma fatura gerada " +
							"através desta negociação.");
				}

			Contrato contrato = ContratoDAO.get(cdContrato, connection);
			ContratoNegociacao negociacao = ContratoNegociacaoDAO.get(cdContrato, cdNegociacao, connection);
			GregorianCalendar dtVencimento = negociacao.getDtPrimeiroVencimento();
			GregorianCalendar dtEmissao = new GregorianCalendar();
			dtEmissao = new GregorianCalendar(dtEmissao.get(Calendar.YEAR), dtEmissao.get(Calendar.MONTH), dtEmissao.get(Calendar.DAY_OF_MONTH));
			int nrDiaVencimento = negociacao.getNrDiaVencimento();
			int qtParcelas = negociacao.getQtParcelas();
			qtParcelas = qtParcelas<=0 ? 1 : qtParcelas;
			GregorianCalendar dtVencimentoPrimeira = negociacao.getDtPrimeiroVencimento();
			if(nrDiaVencimento<=0 && dtVencimentoPrimeira!=null)
				nrDiaVencimento = dtVencimentoPrimeira.get(Calendar.DATE);
			boolean lgIgnorarDiasNaoUteis = ParametroServices.getValorOfParametroAsInteger("LG_IGNORAR_DIA_NAO_UTIL", 0,
					contrato.getCdEmpresa(), connection)==1;
			float vlTotal = 0;
			for (int i=0; i<categorias.size(); i++)
				vlTotal += categorias.get(i).getVlContaCategoria();

			for (int i=0; negociacao.getVlDesconto()>0 && i<categorias.size(); i++) {
				categorias.get(i).setVlContaCategoria(categorias.get(i).getVlContaCategoria() -
						categorias.get(i).getVlContaCategoria()/vlTotal * negociacao.getVlDesconto());
			}
			vlTotal -= negociacao.getVlDesconto();

			if (negociacao.getVlAcrescimo()>0) {
				int cdCategoriaDefault    = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_RECEITAS_DEFAULT",
						0, contrato.getCdEmpresa(), connection);
				if (cdCategoriaDefault<=0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Problemas encontrados ao finalizar negociação e realizar faturamento. Certifique-se de que " +
							"esteja configurada a categoria econômica default para enquadramento de receitas referentes a acréscimos informados na negociação.");
				}
				ContaReceberCategoria categoria = null;
				for (int j=0; j<categorias.size(); j++)
					if (categorias.get(j).getCdCategoriaEconomica() == cdCategoriaDefault) {
						categoria = categorias.get(j);
						break;
					}
				if (categoria==null)
					categorias.add(new ContaReceberCategoria(0 /*cdContaReceber*/,
							cdCategoriaDefault,
							negociacao.getVlAcrescimo(), 0));
				else
					categoria.setVlContaCategoria(categoria.getVlContaCategoria() + negociacao.getVlAcrescimo());
				vlTotal += negociacao.getVlAcrescimo();
			}

			if (negociacao.getVlMultaMora()>0) {
				int cdCategoriaMulta    = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_MULTA_RECEBIDA", 0,
						contrato.getCdEmpresa(), connection);
				if (cdCategoriaMulta<=0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Problemas encontrados ao finalizar negociação e realizar faturamento. Certifique-se de que " +
							"esteja configurada a categoria econômica default para enquadramento de receitas referentes a multas de mora informadas na negociação.");
				}
				ContaReceberCategoria categoria = null;
				for (int j=0; j<categorias.size(); j++)
					if (categorias.get(j).getCdCategoriaEconomica() == cdCategoriaMulta) {
						categoria = categorias.get(j);
						break;
					}
				if (categoria==null)
					categorias.add(new ContaReceberCategoria(0 /*cdContaReceber*/,
							cdCategoriaMulta,
							negociacao.getVlMultaMora(), 0));
				else
					categoria.setVlContaCategoria(categoria.getVlContaCategoria() + negociacao.getVlMultaMora());
				vlTotal += negociacao.getVlMultaMora();
			}

			if (negociacao.getVlJurosMora()>0) {
				int cdCategoriaJuros    = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_JUROS_RECEBIDO", 0,
						contrato.getCdEmpresa(), connection);
				if (cdCategoriaJuros<=0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Problemas encontrados ao finalizar negociação e realizar faturamento. Certifique-se de que " +
							"esteja configurada a categoria econômica default para enquadramento de receitas referentes a juros de mora informados na negociação.");
				}
				ContaReceberCategoria categoria = null;
				for (int j=0; j<categorias.size(); j++)
					if (categorias.get(j).getCdCategoriaEconomica() == cdCategoriaJuros) {
						categoria = categorias.get(j);
						break;
					}
				if (categoria==null)
					categorias.add(new ContaReceberCategoria(0 /*cdContaReceber*/,
							cdCategoriaJuros,
							negociacao.getVlJurosMora(), 0));
				else
					categoria.setVlContaCategoria(categoria.getVlContaCategoria() + negociacao.getVlJurosMora());
				vlTotal += negociacao.getVlJurosMora();
			}

			vlTotal = Util.roundFloat(vlTotal, 2);
			float vlTotalTemp = vlTotal;
			float vlConta = Util.roundFloat(vlTotal/qtParcelas, 2);

			String nrReferencia = contrato.getNrContrato() + "-N" + cdNegociacao;
			nrReferencia = nrReferencia.length()>15 ? nrReferencia.substring(0, 15) : nrReferencia;
			ArrayList<ContaReceber> parcelas = new ArrayList<ContaReceber>();

			pstmt = connection.prepareStatement("SELECT MAX(nr_parcela) " +
					"FROM adm_conta_receber " +
					"WHERE cd_contrato = ?");
			pstmt.setInt(1, cdContrato);
			rs = pstmt.executeQuery();
			int nrNextParcela = !rs.next() ? 1 : rs.getInt(1)+1;
			String dsHistorico = "REF. CONTRATO N° " + contrato.getNrContrato() + " NEGOCIAÇÃO " + cdNegociacao;
			dsHistorico = dsHistorico.length()>100 ? dsHistorico.substring(0, 100) : dsHistorico;

			for (int i=0; dtVencimento!=null && i<qtParcelas; i++) {
				GregorianCalendar venc = (GregorianCalendar)dtVencimento.clone();
				if(!lgIgnorarDiasNaoUteis)
					while(venc.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY || venc.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || FeriadoServices.isFeriado(venc, connection))
						venc.add(Calendar.DATE, 1);

				String nrDocumento = nrReferencia + "-" + new DecimalFormat("000").format(i+1);
				nrDocumento = nrDocumento.length()>15 ? nrDocumento.substring(0, 15) : nrDocumento;
				ContaReceber conta = new ContaReceber(0,
						contrato.getCdPessoa(),
						contrato.getCdEmpresa(),
						cdContrato /*cdContrato*/,
						0  /*cdContaOrigem*/,
						0 /*cdDocumentoSaida*/,
						cdContaCarteira,
						cdConta,
						0 /*cdFrete*/,
						nrDocumento /*nrDocumento*/,
						"N" + cdNegociacao + "-" + (i+1) /*idContaReceber*/,
						nrNextParcela++ /*nrParcela*/,
						nrReferencia /*nrReferencia*/,
						cdTipoDocumento,
						dsHistorico,
						venc,
						dtEmissao,
						null /*dtRecebimento*/,
						null /*dtProrrogacao*/,
						i<qtParcelas-1 ?  Double.valueOf( Float.toString(vlConta)) : Double.valueOf( Float.toString(vlTotalTemp)) /*vlConta*/,
						0.0d /*vlAbatimento*/,
						0.0d /*vlAcrescimo*/,
						0.0d /*vlRecebido*/,
						ST_EM_ABERTO /*stConta*/,
						ContaReceberServices.UNICA_VEZ /*tpFrequencia*/,
						0 /*qtParcelas*/,
						ContaReceberServices.TP_NEGOCIACAO /*tpContaReceber*/,
						cdNegociacao /*cdNegociacao*/,
						"" /*txtObservacao*/,
						0 /*cdPlanoPagamento*/,
						0 /*cdFormaPagamento*/,
						new GregorianCalendar(),
						venc, 
						0/*cdTurno*/,
						0.0d/*prJuros*/,
						0.0d/*prMulta*/, 
						0/*lgProtesto*/);
				vlTotalTemp -= vlConta;
				ContaReceberServices.gerarNossoNumeroAndRegistro(conta, connection);

				ArrayList<ContaReceberCategoria> categoriasConta = new ArrayList<ContaReceberCategoria>();
				for (int j=0; j<categorias.size(); j++)
					categoriasConta.add(new ContaReceberCategoria(0 /*cdContaReceber*/,
							categorias.get(j).getCdCategoriaEconomica(),
							conta.getVlConta().floatValue()/vlTotal * categorias.get(j).getVlContaCategoria(), categorias.get(j).getCdCentroCusto()));

				Result result = ContaReceberServices.insert(conta, categoriasConta, null /*tituloCredito*/, false, connection);
				if (result.getCode()<=0) {
					return new Result(-1, "Problemas encontrados ao finalizar negociação e realizar faturamento. Erros de ordem técnica " +
							"reportados ao incluir fatura. Entre em contato com o suporte técnico.");
				}

				dtVencimento = ContaPagarServices.getProximoVencimento(dtVencimento, ContaPagarServices.MENSAL, nrDiaVencimento);
			}

			negociacao.setVlParcela(vlConta);
			negociacao.setStNegociacao(ST_ENCERRADO);
			if (ContratoNegociacaoDAO.update(negociacao, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Problemas encontrados ao finalizar negociação e realizar faturamento, ao atualizar dados referentes " +
						"às condições de pagamento e parcelamento. Entre em contato com o suporte técnico");
			}

			TipoDocumento tipoDocumento = TipoDocumentoDAO.get(cdTipoDocumento, connection);

			if (isConnectionNull)
				connection.commit();

			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("parcelas", parcelas);
			hash.put("tipoDocumento", tipoDocumento);
			hash.put("negociacao", negociacao);
			hash.put("contasCanceladas", contasCanceladas);
			return new Result(1, "", "results", hash);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Problemas encontrados ao finalizar negociação e realizar faturamento. Anote a mensagem de erro abaixo " +
					"e entre em contato com o suporte técnico.", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int delete(int cdContrato, int cdNegociacao) {
		return delete(cdContrato, cdNegociacao, null);
	}

	public static int delete(int cdContrato, int cdNegociacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			/* modifica o status das contas a receber envolvidas na negociação */
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_receber " +
					"SET st_conta = ? " +
					"WHERE cd_conta_receber IN (SELECT cd_conta_receber " +
					"							FROM adm_conta_receber_negociacao " +
					"							WHERE cd_contrato = ? " +
					"							  AND cd_negociacao = ?) ");
			pstmt.setInt(1, ContaReceberServices.ST_EM_ABERTO);
			pstmt.setInt(2, cdContrato);
			pstmt.setInt(3, cdNegociacao);
			pstmt.execute();

			pstmt = connect.prepareStatement("DELETE " +
					"FROM adm_conta_receber_negociacao " +
					"WHERE cd_contrato = ? " +
					"  AND cd_negociacao = ?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdNegociacao);
			pstmt.execute();

			pstmt = connect.prepareStatement("SELECT cd_conta_receber " +
					"FROM adm_conta_receber " +
					"WHERE cd_contrato = ? " +
					"  AND cd_negociacao = ?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdNegociacao);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (ContaReceberServices.delete(rs.getInt("cd_conta_receber"), true, false, connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return -1;
				}
			}

			if (ContratoNegociacaoDAO.delete(cdContrato, cdNegociacao, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoNegociacaoServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllContasReceber(int cdContrato, int cdNegociacao) {
		return getAllContasReceber(cdContrato, cdNegociacao, null);
	}

	public static ResultSetMap getAllContasReceber(int cdContrato, int cdNegociacao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_contrato", Integer.toString(cdContrato), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.cd_documento_saida", "0", ItemComparator.ISNULL, Types.INTEGER));
			criterios.add(new ItemComparator("notNegociacao", Integer.toString(cdNegociacao), ItemComparator.DIFFERENT, Types.INTEGER));
			ResultSetMap rsm = ContaReceberServices.find(criterios, connection);

			rsm.beforeFirst();
			while (rsm.next())
				rsm.getRegister().put("LG_CONTA_RECEBER", 0);

			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_conta_receber " +
					"FROM adm_conta_receber_negociacao " +
					"WHERE cd_contrato = ? " +
					"  AND cd_negociacao = ?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdNegociacao);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				if (rsm.locate("cd_conta_receber", rs.getInt("cd_conta_receber"), false, false))
					rsm.getRegister().put("LG_CONTA_RECEBER", 1);

			rsm.beforeFirst();
			return rsm;
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

}
