package com.tivic.manager.alm;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.mail.SMTPClient;
import sol.util.Result;

import com.tivic.manager.adm.ChequeServices;
import com.tivic.manager.adm.Classificacao;
import com.tivic.manager.adm.ClassificacaoDAO;
import com.tivic.manager.adm.ClassificacaoServices;
import com.tivic.manager.adm.Cliente;
import com.tivic.manager.adm.ClienteDAO;
import com.tivic.manager.adm.ClienteProgramaFaturaServices;
import com.tivic.manager.adm.ClienteServices;
import com.tivic.manager.adm.CondicaoPagamento;
import com.tivic.manager.adm.CondicaoPagamentoServices;
import com.tivic.manager.adm.ContaFinanceiraServices;
import com.tivic.manager.adm.ContaPagarServices;
import com.tivic.manager.adm.ContaReceber;
import com.tivic.manager.adm.ContaReceberCategoria;
import com.tivic.manager.adm.ContaReceberDAO;
import com.tivic.manager.adm.ContaReceberServices;
import com.tivic.manager.adm.FormaPagamento;
import com.tivic.manager.adm.FormaPagamentoDAO;
import com.tivic.manager.adm.FormaPagamentoEmpresa;
import com.tivic.manager.adm.FormaPagamentoEmpresaServices;
import com.tivic.manager.adm.FormaPagamentoServices;
import com.tivic.manager.adm.MovimentoConta;
import com.tivic.manager.adm.MovimentoContaCategoria;
import com.tivic.manager.adm.MovimentoContaCategoriaServices;
import com.tivic.manager.adm.MovimentoContaReceber;
import com.tivic.manager.adm.MovimentoContaReceberServices;
import com.tivic.manager.adm.MovimentoContaServices;
import com.tivic.manager.adm.MovimentoContaTituloCredito;
import com.tivic.manager.adm.NaturezaOperacaoDAO;
import com.tivic.manager.adm.PlanoPagamento;
import com.tivic.manager.adm.PlanoPagamentoDAO;
import com.tivic.manager.adm.PlanoPagtoDocumentoSaida;
import com.tivic.manager.adm.PlanoPagtoDocumentoSaidaDAO;
import com.tivic.manager.adm.PlanoPagtoDocumentoSaidaServices;
import com.tivic.manager.adm.ProgramaFatura;
import com.tivic.manager.adm.SaidaItemAliquota;
import com.tivic.manager.adm.SaidaItemAliquotaDAO;
import com.tivic.manager.adm.SaidaItemAliquotaServices;
import com.tivic.manager.adm.SaidaTributo;
import com.tivic.manager.adm.TituloCredito;
import com.tivic.manager.adm.TituloCreditoDAO;
import com.tivic.manager.adm.TituloCreditoServices;
import com.tivic.manager.adm.Tributo;
import com.tivic.manager.adm.TributoAliquotaServices;
import com.tivic.manager.adm.TributoDAO;
import com.tivic.manager.adm.TributoServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.fsc.NotaFiscalDocVinculadoServices;
import com.tivic.manager.fsc.NotaFiscalServices;
import com.tivic.manager.fta.Viagem;
import com.tivic.manager.fta.ViagemDAO;
import com.tivic.manager.fta.ViagemServices;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.grl.ArquivoServices;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.EmpresaDAO;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.FeriadoServices;
import com.tivic.manager.grl.NumeracaoDocumentoServices;
import com.tivic.manager.grl.Parametro;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ParametroValorDAO;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaContatoDAO;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaJuridicaDAO;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.grl.ProdutoServico;
import com.tivic.manager.grl.ProdutoServicoDAO;
import com.tivic.manager.grl.ProdutoServicoServices;
import com.tivic.manager.grl.UnidadeMedidaDAO;
import com.tivic.manager.util.Util;

/**
 * Serviços do documento de saída
 * @author Hugo Azevedo
 * @version 1.0
 */
public class DocumentoSaidaServices {

	/* tipos de saida */
	public static final int SAI_VENDA              = 0;
	public static final int SAI_VENDA_CONSIGNACAO  = 1;
	public static final int SAI_TRANSFERENCIA      = 2;
	public static final int SAI_DOACAO             = 3;
	public static final int SAI_DESCARTE           = 4;
	public static final int SAI_AJUSTE             = 5;
	public static final int SAI_DEVOLUCAO 		   = 6;
	public static final int SAI_ACERTO_CONSIGNACAO = 7;
	public static final int SAI_ORDEM_SERVICO 	   = 8;
	public static final int SAI_MANUTENCAO	 	   = 9;

	public static String[] tiposSaida = {"Venda", "Venda Consignada", "Transferência", "Doação", "Descarte/Baixa", "Ajuste (perdas)", "Devolução",
								         "Acerto Consignação", "Ordem de Serviço", "Manutenção", "Consumo"};

	/* situacoes de documentos de saidas */
	public static final int ST_EM_CONFERENCIA = 0;
	public static final int ST_CONCLUIDO      = 1;
	public static final int ST_CANCELADO      = 2;

	public static String[] situacoes             = {"Em conferência", "Concluída", "Cancelada"};
	public static String[] situacoesTransf       = {"Em aberto", "Concluída", "Cancelada"};
	public static String[] situacoesSaidaServico = {"Em aberto", "Concluída", "Cancelada"};

	/* tipos de documentos de saidas */
	public static final int TP_NOTA_FISCAL_VENDA = 0;
	public static final int TP_NOTA_REMESSA      = 1;
	public static final int TP_NOTA_DEVOLUCAO    = 2;
	public static final int TP_DOC_TRANSFERENCIA = 3;
	public static final int TP_DOC_NAO_FISCAL    = 4;
	public static final int TP_CUPOM_FISCAL      = 5;
	public static final int TP_NFE               = 6;
	public static final int TP_NOTA_FISCAL_02    = 7;
	public static final int TP_AFERICAO          = 8;
	public static final int TP_CONSUMO           = 9;

	public static String[] tiposDocumentoSaida = {"Nota Fiscal", "Nota de Remessa", "Nota de Devolução", "Transf. Interna",
	                                              "Outros", "Cupom Fiscal", "NF-e", "Nota Fiscal 02", "Aferição"};
	
	public static String[] tiposDocumentoSaida2 = {"NF", "NR", "ND", "TI",
												  "Outros", "CF", "NF-e", "NF02", "Afer"};

	public static Object[] tiposDocumentoSaidaServico = {new Object[] {TP_NOTA_FISCAL_VENDA, "Nota Fiscal de Serviços"},
		                                                 new Object[] {TP_DOC_NAO_FISCAL, "Documento não fiscal"}};

	/* tipo de movimento */
	public static final int MOV_ESTOQUE_NAO_CONSIGNADO = 0;
	public static final int MOV_ESTOQUE_CONSIGNADO     = 1;
	public static final int MOV_AMBOS_TIPO_ESTOQUE     = 2;
	public static final int MOV_NENHUM                 = 3;

	public static final String[] tiposMovimento = {"Normal", "Consignado", "Ambos"};

	/* tipos de fretes */
	public static final int FRT_CIF               = 0;
	public static final int FRT_FOB               = 1;
	public static final int FRT_FOB_TRANSPORTADOR = 2;
	public static final int FRT_SEM_COBRANCA      = 3;

	public static String[] tiposFrete = {"Emitente", "Destinatário", "Destinatário (pago ao transp.)", "Sem Cobrança"};

	/* codificação de erros retornados por rotinas relacionadas a documentos de saída */
	public static final int ERR_EXISTENCIA_CONTAS_RECEBER = -2;
	public static final int ERR_QTD_SAIDA_SUPERIOR     	  = -3;
	public static final int ERR_VALOR_TOTAL            	  = -4;
	public static final int ERR_FAT_DOC_NULO_CANC         = -10;
	public static final int ERR_FAT_INICIO_FECHAMENTO     = -11;
	public static final int ERR_FAT_SAVE_CONTA_RECEBER    = -20;
	public static final int ERR_FAT_SAVE_MOVIMENTO        = -30;
	public static final int ERR_FAT_SAVE_PLANO_PAG        = -50;
	public static final int ERR_FAT_UPDATE_DOC            = -60;
	public static final int ERR_FAT_LANCAMENTO            = -70;
	public static final int ERR_FAT_FORMA_PAG_INVALIDA    = -80;
	public static final int ERR_DOC_BALANCO               = -5;
	public static final int ERR_ENVIO_EMAIL               = -19;
	public static final int ERR_EMAIL_JA_ENVIADO          = -21;
	public static final int ERR_CLIENTE_SEM_CREDITO       = -22;

	/* codificacao de retornos */
	public static final int RET_PERMITE_LIMITE_ULTRAPASSADO = -101;
	
	public static final int RET_REABASTECIMENTO = 2;
	
	/**
     *
     * @author Hugo
     * @category Estoque
     * @param cdDocumentoSaida Código do documento de saída de origem
     * @param cdEmpresa Código da empresa destino
     * @param cdLocalArmazenamento Código do local de armazenamento destino
     * @return Resultado das operações (1) processado com sucesso
     */
	public static int copyDocumentoSaida(int cdDocumentoSaida, int cdEmpresa, int cdLocalArmazenamento) {
		return copyDocumentoSaida(cdDocumentoSaida, cdEmpresa, cdLocalArmazenamento, null);
	}
	/**
     *
     * @author Hugo
     * @category Estoque
     * @param cdDocumentoSaida Código do documento de saída de origem
     * @param cdEmpresa Código da empresa destino
     * @param cdLocalArmazenamento Código do local de armazenamento destino
     * @param connect Conexão com o banco de dados
     * @return Resultado das operações (1) processado com sucesso
     */
	public static int copyDocumentoSaida(int cdDocumentoSaida, int cdEmpresa, int cdLocalArmazenamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			/* inclusao do novo documento de saida */
			DocumentoSaida documento = DocumentoSaidaDAO.get(cdDocumentoSaida, connection);
			int oldSituacaoDoc = documento.getStDocumentoSaida();
			documento.setCdDocumentoSaida(0);
			documento.setCdEmpresa(cdEmpresa);
			documento.setStDocumentoSaida(ST_EM_CONFERENCIA);
			int cdNewDocumentoSaida = 0;
			if ((cdNewDocumentoSaida = DocumentoSaidaDAO.insert(documento, connection)) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			/* inclusao dos itens */
			ResultSetMap rsmItens = getAllItens(cdDocumentoSaida, connection);
			while (rsmItens.next()) {
				DocumentoSaidaItem item = new DocumentoSaidaItem(cdNewDocumentoSaida, rsmItens.getInt("cd_produto_servico"), cdEmpresa, rsmItens.getFloat("qt_saida"),
						                                         rsmItens.getFloat("vl_unitario"), rsmItens.getFloat("vl_acrescimo"), rsmItens.getFloat("vl_desconto"),
						                                         rsmItens.getGregorianCalendar("dt_entrega_prevista"), rsmItens.getInt("cd_unidade_medida"),
						                                         rsmItens.getInt("cd_tabela_preco"), 0 /*cdItem*/, 0/*cdBico*/);
				if (DocumentoSaidaItemServices.insert(item, 0 /*cdLocal*/, 0 /*cdLocalDestino*/, false /*registerTributacao*/, connection).getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}

				/* inclusao de aliquotas */
				ResultSetMap rsmAliquotas = DocumentoSaidaItemServices.getAllAliquotas(cdDocumentoSaida, item.getCdProdutoServico(),
						item.getCdItem(), connection);
				while (rsmAliquotas.next()) {
					SaidaItemAliquota aliquota = new SaidaItemAliquota(rsmAliquotas.getInt("cd_produto_servico"),
							cdNewDocumentoSaida,
							cdEmpresa,
							rsmAliquotas.getInt("cd_tributo_aliquota"),
							rsmAliquotas.getInt("cd_tributo"),
							rsmAliquotas.getFloat("pr_aliquota"),
							rsmAliquotas.getFloat("vl_base_calculo"),
							item.getCdItem() /*cdItem*/);
					if (SaidaItemAliquotaDAO.insert(aliquota, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
				}
			}

			/* executa a liberacao da saida */
			if (oldSituacaoDoc == ST_CONCLUIDO) {
				Result result = liberarSaida(cdNewDocumentoSaida, cdLocalArmazenamento, connection);
				if (result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return result.getCode();
				}
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	/**
     * Inicia o faturamento do documento de saída (altera situação do documento de saída para concluído,
     * evitando que itens sejam incluidos, excluidos ou alterados)
     * @author Hugo
     * @category PDV
     * @param DocumentoSaida Documento de saída
     * @return Resultado das operações (1) processado com sucesso
     */
	public static Result iniciarFaturamento(DocumentoSaida documentoSaida, int cdConta, int cdUsuario, float vlDesconto, float vlAcrescimo)  {
		return iniciarFaturamento(documentoSaida, cdConta, cdUsuario, vlDesconto, vlAcrescimo, false, null);
	}

	/**
     * Inicia o faturamento do documento de saída (altera situação do documento de saída para concluído,
     * evitando que itens sejam incluidos, excluidos ou alterados) permitindo o início do faturamento
     * @author Hugo
     * @category PDV
     * @param DocumentoSaida Documento de saída
     * @param simulateFat
     * @param connect Conexão com o banco de dados
     * @return Resultado das operações (1) processado com sucesso
     */
	public static Result iniciarFaturamento(DocumentoSaida documentoSaida, int cdConta, int cdUsuario, float vlDesconto, Connection connect) {
		return iniciarFaturamento(documentoSaida, cdConta, cdUsuario, vlDesconto, 0, false, connect);
	}

	/**
     * Inicia o faturamento do documento de saída (altera situação do documento de saída para concluído,
     * evitando que itens sejam incluidos, excluidos ou alterados) permitindo o início do faturamento
     * @author Hugo
     * @category PDV
     * @param DocumentoSaida Documento de saída
     * @param simulateFat
     * @param connect Conexão com o banco de dados
     * @return Resultado das operações (1) processado com sucesso
     */
	public static Result iniciarFaturamento(DocumentoSaida documentoSaida, int cdConta, int cdUsuario, float vlDesconto, float vlAcrescimo, boolean simulateFat, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		try {
			/*
			 * Verifica se o caixa está aberto para o usuário informado, se não há fechamento para o período e se o usuário tem permissão para o caixa
			 */
			Result result = ContaFinanceiraServices.isCaixaAbertoTo(cdConta, 0/*cdMovimentoConta*/, cdUsuario, 0/*cdFechamento*/, new GregorianCalendar(), connect);
			if(result.getCode() <= 0)
				return result;
			/*
			 * Verifica se o documento de saída é valido e se não está cancelado
			 */
			if (documentoSaida == null || (!simulateFat && documentoSaida.getStDocumentoSaida()==ST_CANCELADO))	{
				com.tivic.manager.util.Util.registerLog(new Exception("Documento de saída nulo ou cancelado. cdDocumentoSaida = "+(documentoSaida!=null?documentoSaida.getStDocumentoSaida():"null")));
				return new Result(ERR_FAT_DOC_NULO_CANC, "Documento de saída nulo ou cancelado. cdDocumentoSaida = "+(documentoSaida!=null?documentoSaida.getStDocumentoSaida():"null"));
			}

			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull && !simulateFat ? false : connect.getAutoCommit());

			/*
			 * Calcula totais
			 */
			ResultSet rs = connect.prepareStatement("SELECT SUM(qt_saida * vl_unitario) as vl_total, " +
								                    "       SUM(vl_acrescimo) AS vl_acrescimo," +
								                    "		SUM(vl_desconto) AS vl_desconto " +
								                    "FROM alm_documento_saida_item A " +
								                    "WHERE A.cd_documento_saida = "+documentoSaida.getCdDocumentoSaida()).executeQuery();
			rs.next();
			float vlTotal              = rs.getFloat("vl_total");
			float vlTotalAcrescimoItem = rs.getFloat("vl_acrescimo");
			float vlTotalDescontoItem  = rs.getFloat("vl_desconto");
			if(vlTotalDescontoItem > 0)
				vlDesconto = vlTotalDescontoItem;
			if(vlTotalAcrescimoItem > 0)
				vlAcrescimo = vlTotalAcrescimoItem;
			vlTotal = vlTotal + vlAcrescimo - vlDesconto;
			documentoSaida.setVlAcrescimo(vlAcrescimo);
			documentoSaida.setVlDesconto(vlDesconto);
			documentoSaida.setVlTotalDocumento(vlTotal);
			documentoSaida.setStDocumentoSaida(ST_CONCLUIDO);
			result.addObject("vlAcrescimo", vlAcrescimo);
			result.addObject("vlDesconto", vlDesconto);
			result.addObject("vlTotal", vlTotal);
			int ret = simulateFat ? 1 : DocumentoSaidaDAO.update(documentoSaida, connect);
			if (ret <= 0)	{
				if (isConnectionNull)
					Conexao.rollback(connect);
				com.tivic.manager.util.Util.registerLog(new Exception("Erro ao atualizar documento de saída! erro: "+ret));
				return new Result(ERR_FAT_UPDATE_DOC, "Erro ao atualizar documento de saída! erro: "+ret);
			}
			/*
			 * Distribui o valor do desconto para os itens quando os descontos já não foram por item
			 */
			if(vlTotalDescontoItem <= 0)	{
				PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_documento_saida_item SET vl_desconto = ? " +
						                                           "WHERE cd_documento_saida = "+documentoSaida.getCdDocumentoSaida()+
						                                           "  AND cd_empresa         = "+documentoSaida.getCdEmpresa()+
						                                           "  AND cd_produto_servico = ? " +
						                                           "  AND cd_item            = ? ");
				ResultSet rsItens = connect.prepareStatement("SELECT * FROM alm_documento_saida_item A " +
	                    									 "WHERE A.cd_documento_saida = "+documentoSaida.getCdDocumentoSaida()).executeQuery();
				ArrayList<HashMap<String, Object>> itens = new ArrayList<HashMap<String,Object>>();
				while(rsItens.next())	{
					float vlDescontoItem = (vlDesconto / (vlTotal + vlDesconto)) * (rsItens.getFloat("qt_saida")*rsItens.getFloat("vl_unitario"));
					pstmt.setFloat(1, vlDescontoItem);
					pstmt.setInt(2, rsItens.getInt("cd_produto_servico"));
					pstmt.setInt(3, rsItens.getInt("cd_item"));
					pstmt.executeUpdate();
					HashMap<String, Object> item = new HashMap<String, Object>();
					item.put("cdProdutoServico", rsItens.getInt("cd_produto_servico"));
					item.put("vlDescontoItem", vlDescontoItem);
					itens.add(item);
				}
				result.addObject("itens", itens);
			}

			if (isConnectionNull)
				connect.commit();

			return result;
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			Util.registerLog(e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar iniciar o faturamento de uma saída!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result estornarPagamento(int cdDocumentoSaida, int cdFormaPagamento, int cdPlanoPagamento, int cdUsuario) {
		return estornarPagamento(cdDocumentoSaida, cdFormaPagamento, cdPlanoPagamento, cdUsuario, null);
	}

	public static Result estornarPagamento(int cdDocumentoSaida, int cdFormaPagamento, int cdPlanoPagamento, int cdUsuario, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			DocumentoSaida documento = DocumentoSaidaDAO.get(cdDocumentoSaida, connection);

			if (documento==null) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				throw new Exception("Estorno de Pagamento: Documento de saída nao localizado! [cdDocumentoSaida:"+cdDocumentoSaida+"]");
			}
			/*
			 * Exclui as contas geradas pelo recebimento que está sendo excluido
			 */
			ResultSetMap rsmContas = getAllContasReceber(cdDocumentoSaida, cdFormaPagamento, cdPlanoPagamento, connection);
			while(rsmContas.next())	{
				// Aqui somente as contas recebidas tem retorno
				ResultSetMap rsmRecs = MovimentoContaReceberServices.getRecebimentoOfContaReceber(rsmContas.getInt("cd_conta_receber"), connection);
				while(rsmRecs.next())	{
					Result result = MovimentoContaReceberServices.delete(rsmRecs.getInt("cd_conta"), rsmRecs.getInt("cd_movimento_conta"), rsmRecs.getInt("cd_conta_receber"), cdUsuario, true, connection);
					if(result.getCode() <= 0)
						return result;
				}
				/* TERIA QUE EXCLUIR OS TÍTULOS DA TRANSFERÊNCIA DE FINAL DE CAIXA E DIMINUIR O VALOR DA TRANSFERÊNCIA */
				// Entradas de títulos de crédito
				ResultSet rsMTC = connection.prepareStatement("SELECT B.cd_conta, B.cd_movimento_conta, A.cd_titulo_credito " +
						                                      "FROM adm_titulo_credito A " +
													          "JOIN adm_movimento_titulo_credito B ON (A.cd_titulo_credito  = B.cd_titulo_credito) " +
													          "JOIN adm_movimento_conta          C ON (B.cd_conta           = C.cd_conta " +
													          "                                    AND B.cd_movimento_conta = C.cd_movimento_conta) " +
													          "WHERE A.cd_conta_receber = "+rsmContas.getInt("cd_conta_receber")+
													          "  AND C.tp_movimento     = "+MovimentoContaServices.CREDITO+
													          (documento.getCdConta()>0 ? "  AND C.cd_conta         = "+documento.getCdConta() : "" )+
													          " ORDER BY C.dt_movimento ").executeQuery();
				int cdConta = 0;
				while(rsMTC.next()) {
					if(cdConta == 0)
						cdConta = rsMTC.getInt("cd_conta");
					else if(cdConta != rsMTC.getInt("cd_conta"))
						break;
					//
					connection.prepareStatement("DELETE FROM adm_movimento_titulo_credito WHERE cd_titulo_credito = "+rsMTC.getInt("cd_titulo_credito")).executeUpdate();
					//
					Result resultado = MovimentoContaServices.delete(rsMTC.getInt("cd_conta"), rsMTC.getInt("cd_movimento_conta"), cdUsuario, true, false, connection);
					if (resultado.getCode()<0){
						return resultado;
					}
				}
				// Exclui os títulos de crédito
				if (TituloCreditoServices.deleteAllOfContaReceber(rsmContas.getInt("cd_conta_receber"), connection)<0)
					throw new Exception("Falha ao tentar excluir títulos de crédito gerados pelo recebimento estornado!");
				// Exclui a conta
				if (ContaReceberServices.delete(rsmContas.getInt("cd_conta_receber"), true, false, connection) <= 0)
					throw new Exception("Erro ao tentar excluir conta a receber gerada pelo recebimento!");
			}
			/*
			 * Verifica se foi possível excluir todas as contas, se não abortar o estorno da forma de pagamento
			 */
			if(PlanoPagtoDocumentoSaidaDAO.delete(cdPlanoPagamento, cdDocumentoSaida, cdFormaPagamento, connection) <= 0)
				throw new Exception("Não foi possível excluir o registro do plano e da forma de pagamento!");

			if (isConnectionNull)
				connection.commit();
			return new Result(1);
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, e.getMessage(), e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	/**
     * Método que executa faturamento resumido do documento de saída (fecha documento, cria conta a receber,
     * classifica em categorias, lança movimento de conta, lança pagamento da conta, e informa plano de pagamento
     * utilizado) - PDV
     * @author Hugo
     * @category PDV
     */
	public static Result faturamentoResumido(DocumentoSaida documentoSaida, float vlDesconto, float vlAcrescimo, int cdConta, int cdContaCarteira,
			int cdFormaPagamento, int cdPlanoPagamento, int cdUsuario) {
		return faturamentoResumido(documentoSaida, vlDesconto, vlAcrescimo, cdConta, cdContaCarteira, cdFormaPagamento, cdPlanoPagamento, cdUsuario, null);
	}

	/**
     * Método que executa faturamento resumido do documento de saída (fecha documento, cria conta a receber,
     * classifica em categorias, lança movimento de conta, lança pagamento da conta, e informa plano de pagamento
     * utilizado)
     * @author Hugo
     * @category PDV
     * @param DocumentoSaida Documento de saída
     * @param cdConta Código da conta caixa que irá receber o lançamento (crédito)
     * @param cdContaCarteira Código da carteira para a qual será criada a conta a receber
     * @param cdFormaPagamento Código da forma de pagamento escolhida (somente dos tipos moeda corrente serão aceitas)
     * @param cdPlanoPagamento Código do plano de pagamento usado no fechamento resumido
     * @param cdUsuario Código do usuário responsável pelos lançamentos
     * @param simulateFat se verdadeiro apenas simula o faturamento, sem registro de contas e movimentos
     * @param connect Conexão com o banco de dados
     * @return Resultado das operações (1) processado com sucesso
     */
	public static Result faturamentoResumido(DocumentoSaida documentoSaida, float vlDesconto, float vlAcrescimo, int cdConta, int cdContaCarteira, int cdFormaPagamento,
			int cdPlanoPagamento, int cdUsuario, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		try {
			/*
			 *  Verifica se o documento de saída passado é um documento válido
			 */
			if (documentoSaida == null || documentoSaida.getStDocumentoSaida()==ST_CANCELADO)	{
				com.tivic.manager.util.Util.registerLog(new Exception("Erro: Documento de saída nulo ou cancelado!"));
				return new Result(ERR_FAT_DOC_NULO_CANC, "Erro: Documento de saída nulo ou cancelado!");
			}

			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			/*
			 *  Verifica se a forma de pagamento pode ser aceita para fechamento resumido
			 */
			FormaPagamentoEmpresa formaPagamentoEmpresa = FormaPagamentoEmpresaServices.get(cdFormaPagamento, documentoSaida.getCdEmpresa(), connect);
			FormaPagamento formPag 						= FormaPagamentoDAO.get(cdFormaPagamento, connect);
			if (formPag.getTpFormaPagamento() != FormaPagamentoServices.MOEDA_CORRENTE)	{
				if (isConnectionNull)
					Conexao.rollback(connect);
				com.tivic.manager.util.Util.registerLog(new Exception("Erro: forma de pagamento não permitida!"));
				return new Result(ERR_FAT_FORMA_PAG_INVALIDA, "Erro: forma de pagamento não permitida!");
			}
			/*
			 *  Inicia faturamento, muda situação do documento e calcula o totais do documento
			 */
			Result objResult = iniciarFaturamento(documentoSaida, cdConta, cdUsuario, vlDesconto, 0, false/*simulateFat*/, connect);
			if (objResult.getCode() <= 0)	{
				if (isConnectionNull)
					Conexao.rollback(connect);
				com.tivic.manager.util.Util.registerLog(new Exception("Erro ao iniciar faturamento! erro: "+objResult.getCode()));
				return objResult;
			}
			/*
			 *  Cria fatura do documento (cria conta a receber, classifica em categorias, lança movimentação na conta,
			 *  lança pagamento classificando e por fim registra o plano de pagamento utilizado)
			 */
			objResult = lancarFaturamento(documentoSaida, cdConta, cdContaCarteira, formaPagamentoEmpresa, cdPlanoPagamento, cdUsuario,
					                      documentoSaida.getVlTotalDocumento() - documentoSaida.getVlDesconto() + documentoSaida.getVlAcrescimo(), vlDesconto,
					                      null/*dados das parcelas*/, false /*simulateFat*/, null /*nrAutorizacao*/, 0 /*vlAcrescimo*/, false, false, connect);

			if (objResult.getCode() <= 0)	{
				if (isConnectionNull)
					Conexao.rollback(connect);
				com.tivic.manager.util.Util.registerLog(new Exception("Erro ao lançar faturamento! erro: "+objResult.getCode()));
				return objResult;
			}
			/*
			if (objReturn instanceof HashMap && objReturnTemp instanceof HashMap) {
				Iterator<String> it = ((HashMap<String, Object>)objReturnTemp).keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					((HashMap<String, Object>)objReturn).put(key, ((HashMap<String, Object>)objReturnTemp).get(key));
				}
			}
			*/
			if (isConnectionNull)
				connect.commit();

			return objResult;
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(1, "Erro ao iniciar fechamento!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	/**
     * Faturamento do documento de saída cria as contas a receber e os devidos movimentos na conta (pagamento da conta,
     *  ou apenas registro, a depender da forma de pagamento, se em moeda corrente ou outros [tef/títulos de crédito])
     *  Verifica se o cliente possue limite para efetuar a compra, caso nao tenha ele retornar e pede a senha
     *  do supervisor.
     * @author Hugo
     * @category PDV
     */
	public static Result lancarFaturamento(DocumentoSaida documentoSaida, int cdConta, int cdContaCarteira, int cdFormaPagamento,
			int cdPlanoPagamento, int cdUsuario, float vlFaturado, float vlDesconto, String nrAutorizacao, float vlAcrescimo, boolean withEmail, boolean permitirLimiteUltrapassado)
	{
		ArrayList<HashMap<String, Object>> dadosParcelas = null;
		if(nrAutorizacao != null)	{
			dadosParcelas = new ArrayList<HashMap<String, Object>>();
			for(int i=0; i<=20; i++)	{
				HashMap<String, Object> dado = new HashMap<String, Object>();
				dado.put("nrParcela", new Integer(i));
				dado.put("idContaReceber", nrAutorizacao+"-"+Util.fillNum(i, 2));
				dadosParcelas.add(dado);
			}
		}
		return lancarFaturamento(documentoSaida, cdConta, cdContaCarteira, FormaPagamentoEmpresaServices.get(cdFormaPagamento, documentoSaida.getCdEmpresa()),
					             cdPlanoPagamento, cdUsuario, vlFaturado, vlDesconto, dadosParcelas, false, nrAutorizacao, vlAcrescimo, withEmail, permitirLimiteUltrapassado, null);

	}

	/**
     * Faturamento do documento de saída cria as contas a receber e os devidos movimentos na conta (pagamento da conta,
     *  ou apenas registro, a depender da forma de pagamento, se em moeda corrente ou outros [tef/títulos de crédito])
     * @author Hugo
     * @category WEB
     */
	public static Result lancarFaturamento(int cdDocumentoSaida, int cdConta, int cdContaCarteira,
			int cdFormaPagamento, int cdPlanoPagamento, int cdUsuario, float vlFaturado, float vlDesconto, boolean simulateFat) {
		return lancarFaturamento(cdDocumentoSaida, cdConta, cdContaCarteira, cdFormaPagamento, cdPlanoPagamento, cdUsuario,
				                 null /*dadosParcelas*/, vlFaturado, vlDesconto, simulateFat, null, false, false);
	}
	
	/**
     * Faturamento do documento de saída cria as contas a receber e os devidos movimentos na conta (pagamento da conta,
     *  ou apenas registro, a depender da forma de pagamento, se em moeda corrente ou outros [tef/títulos de crédito])
     * @author Hugo
     * @category WEB
     */
	public static Result lancarFaturamento(int cdDocumentoSaida, int cdConta, int cdContaCarteira,
			int cdFormaPagamento, int cdPlanoPagamento, int cdUsuario, ArrayList<HashMap<String, Object>> dadosParcelas, 
			float vlFaturado, float vlDesconto, boolean simulateFat, String nrAutorizacao, boolean withEmail, boolean permitirLimiteUltrapassado)
	{
		Connection connection = Conexao.conectar();
		try {
			connection.setAutoCommit(simulateFat ? connection.getAutoCommit() : false);
			
			DocumentoSaida documentoSaida               = DocumentoSaidaDAO.get(cdDocumentoSaida, connection);
			FormaPagamentoEmpresa formaPagamentoEmpresa = FormaPagamentoEmpresaServices.get(cdFormaPagamento, documentoSaida.getCdEmpresa(), connection);

			Result objResult = lancarFaturamento(documentoSaida, cdConta, cdContaCarteira, formaPagamentoEmpresa, cdPlanoPagamento, cdUsuario, vlFaturado, vlDesconto, dadosParcelas, simulateFat, nrAutorizacao, 0, withEmail, permitirLimiteUltrapassado, connection);
			if ((objResult.getCode() <= 0 && objResult.getCode() != ERR_CLIENTE_SEM_CREDITO) || simulateFat )
				Conexao.rollback(connection);
			else 
				connection.commit();

			return objResult;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (!simulateFat)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar lançar faturamento!", e);
		}
		finally {
			Conexao.desconectar(connection);
		}
	}
	
	

	/**
     * Faturamento do documento de saída cria as contas a receber e os devidos movimentos na conta (pagamento da conta,
     *  ou apenas registro, a depender da forma de pagamento, se em moeda corrente ou outros [tef/títulos de crédito])
     * @author Hugo
     * @category PDV
     * @param DocumentoSaida Documento de saída
     * @param cdConta Código da conta caixa que irá receber o lançamento (crédito)
     * @param cdContaCarteira Código da carteira para a qual será criada a conta a receber
     * @param FormaPagamento Forma de pagamento escolhida
     * @param cdPlanoPagamento Código do plano de pagamento usado nesse faturamento
     * @param cdUsuario Código do usuário responsável pelos lançamentos
     * @param vlFaturado Valor faturado na forma e plano de pagamento escolhido
     * @param dadosParcelas dados das parcelas a serem faturadas, como data de vencimento e valor
     * @param returnHash se verdadeiro retorna hash com informações diversas a respeito do processo de faturamento
     * @param simulateFat se verdadeiro apenas simula o lancamento do faturamento, sem registro de movimentos de conta
     * @param connect Conexão com o banco de dados
     * @return Resultado das operações (1) processado com sucesso
     */
	public static Result lancarFaturamento(DocumentoSaida saida, int cdConta, int cdContaCarteira, FormaPagamentoEmpresa formaPagamentoEmpresa,
			int cdPlanoPagamento, int cdUsuario, float vlFaturado, float vlDesconto, ArrayList<HashMap<String, Object>> dadosParcelas,
			boolean simulateFat, String nrAutorizacao, float vlAcrescimo, boolean withEmail, boolean permitirLimiteUltrapassado, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull && !simulateFat ? false : connect.getAutoCommit());

			FormaPagamento formPag = FormaPagamentoDAO.get(formaPagamentoEmpresa.getCdFormaPagamento(), connect);
			
			//Verifica se o cliente tem algum programa de fatura vigente para o dia da venda
			Result resultadoProgramaFatura = ClienteProgramaFaturaServices.getProgramaFaturaVigente(saida.getCdEmpresa(), saida.getCdCliente(), saida.getDtDocumentoSaida(), connect);
			ProgramaFatura programaVigente = null;

			Cliente cliente = ClienteDAO.get(saida.getCdEmpresa(), saida.getCdCliente(), connect);
			
			if(resultadoProgramaFatura != null && resultadoProgramaFatura.getCode() > 0){
				programaVigente = (ProgramaFatura) resultadoProgramaFatura.getObjects().get("programaFatura");
				
				//Caso o cliente tenha programa de fatura, verifica se alguma conta desse cliente que esteja em aberta ultrapassou o numero de dias para suspender cadastrado 
				//no programa de fatura
//				if(programaVigente.getQtSuspenderApos() > 0){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				//Busca as contas da pessoa que estejam concluidas e cuja data de vencimento seja menor que a data atual
				criterios.add(new ItemComparator("cd_pessoa", "" + saida.getCdCliente(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_empresa", "" + saida.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("st_conta", "" + ContaReceberServices.ST_RECEBIDA, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("dt_vencimento", "" + Util.formatDateTime(Util.getDataAtual(), "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR, Types.TIMESTAMP));
				ResultSetMap rsmContaReceber = ContaReceberDAO.find(criterios, connect);
				while(rsmContaReceber.next()){
					//Caso a quantidade de dias uteis entre a data de vencimento e a data atual for maior do que a quantidade de dias apos cadastrado do programa de fatura, 
					//o cliente será suspenso
					int diasVencidos = Util.getQuantidadeDiasUteis(rsmContaReceber.getGregorianCalendar("dt_vencimento"), Util.getDataAtual(), connect);
					
					//A conta a receber será protestada caso ultrapasse os dias do programa de fatura vigente
					if(programaVigente.getLgProtestar()==1 && programaVigente.getQtDiasProtesto() > 0 && diasVencidos >= programaVigente.getQtDiasProtesto()){
						ContaReceber contaReceber = ContaReceberDAO.get(rsmContaReceber.getInt("cd_conta_receber"), connect);
						contaReceber.setLgProtesto(1);
						if(ContaReceberDAO.update(contaReceber, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao atualizar conta a receber");
						}
					}
					if(diasVencidos >= programaVigente.getQtSuspenderApos() && programaVigente.getQtSuspenderApos() > 0){
						if(cliente != null && cliente.getTpCredito() != ClienteServices.TP_CREDITO_CANCELADO){
							cliente.setTpCredito(ClienteServices.TP_CREDITO_SUSPENSO);
							if(ClienteDAO.update(cliente, connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro ao atualizar status de credito do cliente");
							}
						}
					}
				}
//				}
			}
			
			if(formPag==null)	{
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro: Forma de pagamento inválida! cdFormaPagamento="+formaPagamentoEmpresa.getCdFormaPagamento());
			}
			
			
			if(cliente != null && ParametroServices.getValorOfParametroAsInteger("LG_CONTROLE_CREDITO_CLIENTE", 0, formaPagamentoEmpresa.getCdEmpresa())==1){
				FormaPagamento formaPagamento = FormaPagamentoDAO.get(formPag.getCdFormaPagamento(), connect);
				if(formaPagamento.getTpFormaPagamento() == FormaPagamentoServices.TITULO_CREDITO && (cliente.getTpCredito() == ClienteServices.TP_CREDITO_CANCELADO || cliente.getTpCredito() == ClienteServices.TP_CREDITO_SUSPENSO || cliente.getTpCredito() == ClienteServices.TP_CREDITO_SEM_CREDITO)){
					if(isConnectionNull)
						connect.commit();
					return new Result(ERR_CLIENTE_SEM_CREDITO, "Cliente com crédito suspenso/cancelado/sem crédito. Não é possível o pagamento com titulo de crédito!");
				}
			}
			
			Result objResult = new Result(1);
			if (!simulateFat) {
				/*
				 *  Verifica se o documento de saída é um documento válido
				 */
				if (saida == null || saida.getStDocumentoSaida()==ST_CANCELADO)	{
					com.tivic.manager.util.Util.registerLog(new Exception("Erro: Documento de saída nulo ou cancelado!"));
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(ERR_FAT_DOC_NULO_CANC, "Erro: Documento de saída nulo ou cancelado!");
				}
				/*
				 *  Verifica se o documento ainda não foi finalizado e se possui itens válidos
				 */
				if (saida.getStDocumentoSaida()==ST_EM_CONFERENCIA)	{
					objResult = iniciarFaturamento(saida, cdConta, cdUsuario, saida.getVlDesconto(), connect);
					saida     = DocumentoSaidaDAO.get(saida.getCdDocumentoSaida(), connect);
				}

				if (objResult.getCode() <= 0 || (saida.getVlTotalDocumento() /*+ saida.getVlAcrescimo() - saida.getVlDesconto()*/)<=0)	{
					if (isConnectionNull)
						Conexao.rollback(connect);
					Exception e = new Exception("Erro: Não foi possível iniciar o fechamento ou documento não possui itens válidos! " +
                                                "\n\tcdDocumentoSaida = "+(saida!=null?saida.getCdDocumentoSaida():"null")+
                                                "\n\tvlTotalDocumento = "+saida.getVlTotalDocumento()+
                                                "\n\tgetCode() = "+objResult.getCode()+
                                                "\n\tMensagem: "+objResult.getMessage());
					com.tivic.manager.util.Util.registerLog(e);
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(ERR_FAT_INICIO_FECHAMENTO, e.getMessage(), e);
				}
				/*
				 * Verificando forma de pagamento com crédito do cliente (DEVOLUÇÃO)
				 */
				int cdFormaPagCreditoDevolucao = ParametroServices.getValorOfParametroAsInteger("CD_FORMA_PAGAMENTO_CREDITO_DEVOLUCAO", 0);
				if(cdFormaPagCreditoDevolucao==formaPagamentoEmpresa.getCdFormaPagamento())	{
					int cdTipoDocumentoDevolucao = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_DEVOLUCAO_CLIENTE", 0, 0, connect);
					PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, nm_pessoa, gn_pessoa, nr_cpf, nr_cnpj " +
							                                           "FROM adm_conta_pagar A " +
							                                           "LEFT OUTER JOIN grl_pessoa          B ON (A.cd_pessoa = B.cd_pessoa) " +
							                                           "LEFT OUTER JOIN grl_pessoa_fisica   C ON (A.cd_pessoa = C.cd_pessoa)" +
							                                           "LEFT OUTER JOIN grl_pessoa_juridica D ON (A.cd_pessoa = D.cd_pessoa)" +
							                                           "WHERE A.cd_empresa = "+saida.getCdEmpresa()+
							                                           "  AND nr_documento = ? " +
							                                           (cdTipoDocumentoDevolucao>0 ? " AND cd_tipo_documento = "+cdTipoDocumentoDevolucao : ""));
					pstmt.setString(1, nrAutorizacao);
					ResultSet rs = pstmt.executeQuery();
					if(!rs.next()){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Credito de devolucao nao localizado! Documento: "+nrAutorizacao);
					}
					// Verifica cliente titular do crédito
					if(rs.getInt("cd_pessoa")>0 && rs.getInt("cd_pessoa") != saida.getCdCliente()){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Esse credito so podera ser usado por: "+rs.getString("nm_pessoa"));
					}
					// Verifica valor
					if(vlFaturado > (rs.getFloat("vl_conta") - rs.getFloat("vl_pago"))){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Valor maior do que o crédito disponível! Crédito Disponível: "+(rs.getFloat("vl_conta") - rs.getFloat("vl_pago")));
					}
					// Verifica existência do título de crédito, caso não exista cria
					int cdTituloCredito = 0;
					ResultSet rsTitulo = connect.prepareStatement("SELECT * FROM adm_titulo_credito " +
                                                                  "WHERE nr_documento = \'"+nrAutorizacao+"\'"+
                                                                  (cdTipoDocumentoDevolucao>0 ? " AND cd_tipo_documento = "+cdTipoDocumentoDevolucao : "")).executeQuery();
					if(!rsTitulo.next() || (Math.abs(rsTitulo.getFloat("vl_titulo")-vlFaturado)>0.01))	{
						String nrDocumentoEmissor = rs.getString("nr_cpf")!=null ? rs.getString("nr_cpf") : rs.getString("nr_cnpj");
						TituloCredito tituloCredito = new TituloCredito(0, 0 /*cdInstituicaoFinanceira*/, 0 /*cdAlinea*/, nrAutorizacao,
                                                                        nrDocumentoEmissor, rs.getInt("gn_pessoa"), rs.getString("nm_pessoa"),
                                                                        Double.parseDouble( String.valueOf(vlFaturado)), TituloCreditoServices.teAPRAZO, "" /*dsObservacao*/, new GregorianCalendar() /*Vencimento*/,
                                                                        new GregorianCalendar() /*dtCredito*/, TituloCreditoServices.stLIQUIDADO, "",cdTipoDocumentoDevolucao,
                                                                        TituloCreditoServices.tcPORTADOR, cdConta, 0, 0, 0, "");
						cdTituloCredito = TituloCreditoDAO.insert(tituloCredito, connect);
					}
					else
						cdTituloCredito = rsTitulo.getInt("cd_titulo_credito");
					
					Result result = ContaPagarServices.setPagamentoResumido(rs.getInt("cd_conta_pagar"), cdConta, 0.0/*vlMulta*/, 0/*cdCategoriaMulta*/, 0.0/*vlJuros*/, 0/*cdCategoriaJuros*/, 
							                                0.0/*vlDesconto*/, 0/*cdCategoriaDesconto*/, new Double(vlFaturado), formaPagamentoEmpresa.getCdFormaPagamento(), 
							                                0 /*cdCheque*/, "Crédito de cliente", cdUsuario, new GregorianCalendar(), -1/*stCheque*/, cdTituloCredito, saida.getCdTurno(), 
							                                null/*authData*/, connect);
					if(result.getCode() <= 0)	{
						if(isConnectionNull)
							Conexao.rollback(connect);
						result.setMessage("Erro ao registrar baixa em credito de devolucao do cliente! "+result.getMessage());
						if(isConnectionNull)
							Conexao.rollback(connect);
						return result;
					}
				}
			}
			/*
			 *  Registra plano de pagamento utilizado
			 */
			//Forma de pagamento para programa de fatura
			int cdFormaPagamentoPadrao = ParametroServices.getValorOfParametroAsInteger("CD_FORMA_PAGAMENTO_PROGRAMA_FATURA", 0, saida.getCdEmpresa());
			
			
			int ret = PlanoPagtoDocumentoSaidaServices.insert(new PlanoPagtoDocumentoSaida(cdPlanoPagamento, saida.getCdDocumentoSaida(),
			                                                                               formaPagamentoEmpresa.getCdFormaPagamento(), cdUsuario, vlFaturado, 0/*vlDesconto*/, nrAutorizacao, 0/*vlAcrescimo*/), connect);
			if (ret <= 0)	{
				if (isConnectionNull)
					Conexao.rollback(connect);
				com.tivic.manager.util.Util.registerLog(new Exception("Erro ao gravar planos de pagamento! erro: "));
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ERR_FAT_SAVE_PLANO_PAG, "Erro ao gravar planos de pagamento! erro: ");
			}
			/*
			 *  Se a carteira passada não for válida busca uma carteira da conta passada
			 */
			int cdContaFinanceira = cdConta;
			if(formaPagamentoEmpresa.getCdContaCarteira() > 0)	{
				cdContaFinanceira 	= formaPagamentoEmpresa.getCdConta();
				cdContaCarteira 	= formaPagamentoEmpresa.getCdContaCarteira();
			}
			// Se a carteira da forma de pagamento não for válida...
			if (cdContaCarteira <= 0)	{
				ResultSet rs = connect.prepareStatement("SELECT cd_conta_carteira " +
					                          			"FROM adm_conta_carteira " +
					                          			"WHERE cd_conta = "+cdConta).executeQuery();
				if(rs.next())
					cdContaCarteira = rs.getInt("cd_conta_carteira");
			}
			/*
			 *  Busca tipo de documento default
			 */
			int cdTipoDocumento = formaPagamentoEmpresa.getCdTipoDocumento();
			/*
			 *  Busca categorias levando em conta os produtos
			 */
			int cdCategoriaDesconto    = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_DESCONTO_CONCEDIDO", 0, saida.getCdEmpresa());
			int cdCategoriaDescontoTEF = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_DESCONTO_TEF", 0);
			if (cdCategoriaDescontoTEF <= 0)
				cdCategoriaDescontoTEF = cdCategoriaDesconto;
			int cdCategoriaDescontoProgramaFatura = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_DESCONTO_PROGRAMA_FATURA", 0);
			if (cdCategoriaDescontoProgramaFatura <= 0)
				cdCategoriaDescontoProgramaFatura = cdCategoriaDesconto;
			ArrayList<ContaReceberCategoria> contaReceberCategorias = getClassificacaoEmCategorias(saida, connect);
			/*
			 *  Faz lançamento das parcelas levando em conta a configuração do plano
			 */
			ResultSetMap rsmParcelas 	= new ResultSetMap(connect.prepareStatement("SELECT * FROM adm_plano_parcelamento " +
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
			
			
			/*
			 *  Quando forma de pagamento em TEF gera contas a receber em nome da operadora (administradora)
			 */
			int cdSacado      = formPag.getTpFormaPagamento()==FormaPagamentoServices.TEF ? formaPagamentoEmpresa.getCdAdministrador() : saida.getCdCliente();
			int cdContaOrigem = 0;
			boolean isAVista  = formPag.getTpFormaPagamento()==FormaPagamentoServices.MOEDA_CORRENTE;
			GregorianCalendar dtEmissao = new GregorianCalendar();
			GregorianCalendar dtRec 	= isAVista ? dtEmissao : null;

			ArrayList<MovimentoContaTituloCredito> movimentoTitulo = new ArrayList<MovimentoContaTituloCredito>();
			ArrayList<MovimentoContaReceber> recebimentos          = new ArrayList<MovimentoContaReceber>();
			ArrayList<MovimentoContaCategoria> categoriasMov       = new ArrayList<MovimentoContaCategoria>();
			ArrayList<ContaReceber> contas = new ArrayList<ContaReceber>();
			GregorianCalendar dtVencimento = new GregorianCalendar();
			dtVencimento.set(Calendar.HOUR, 0);
			dtVencimento.set(Calendar.MINUTE, 0);
			dtVencimento.set(Calendar.SECOND, 0);
			
			if(cdTipoDocumento <= 0 || formPag.getTpFormaPagamento()==FormaPagamentoServices.TEF)	{
				ResultSet rs = connect.prepareStatement("SELECT cd_tipo_documento FROM adm_tipo_documento " +
					                          			"WHERE sg_tipo_documento = \'FAT\'").executeQuery();
				if(rs.next())
					cdTipoDocumento = cdTipoDocumento>0 ? cdTipoDocumento : rs.getInt("cd_tipo_documento");
			}
			float vlTotalDocumento = saida.getVlTotalDocumento() + saida.getVlAcrescimo() - saida.getVlDesconto();
			/*
			 * Cria contas à receber de acordo plano de pagamento e forma de pagamento
			 */
			//Se o cliente estiver usando seu programa de fatura para fazer o pagamento, o sistema entrara no bloco onde a data de vencimento será calculada 
			//a partir das configuracoes do programa de fatura, e o desconto será aplicado a partir do programa de fatura
			if(programaVigente != null && cdFormaPagamentoPadrao > 0 && formaPagamentoEmpresa.getCdFormaPagamento() == cdFormaPagamentoPadrao){
				GregorianCalendar dataVencimento = saida.getDtDocumentoSaida();
				int nrDiaHoje = dataVencimento.get(Calendar.DAY_OF_MONTH);
				if(programaVigente.getLgPeriodo()==1){
					int nrDiaVencimento = programaVigente.getNrVencimentoPeriodo();
					if(nrDiaHoje > nrDiaVencimento)
						dataVencimento.add(Calendar.MONTH, 1);
					dataVencimento.set(Calendar.DAY_OF_MONTH, nrDiaVencimento);
				}
				else if(programaVigente.getLgVencimentoFixo()==1){
					int nrDiaCarencia = programaVigente.getNrDiaFixo() - programaVigente.getQtDiasCarencia();
					if(nrDiaHoje >= nrDiaCarencia)
						dataVencimento.add(Calendar.MONTH, 1);
					dataVencimento.set(Calendar.DAY_OF_MONTH, programaVigente.getNrDiaFixo());
				}
				
				Double vlDescontoTEF = 0.0;
				// Se for TEF coloca as contas a receber para o dia configurado na forma de pagamento
				if(formPag.getTpFormaPagamento()==FormaPagamentoServices.TEF)	{
//					if(formaPagamentoEmpresa.getQtDiasCredito()==30)
//						dataVencimento.add(Calendar.MONTH, 1);
//					else
//						dataVencimento.add(Calendar.DATE, formaPagamentoEmpresa.getQtDiasCredito());
					
					double prTaxaDesconto = FormaPagamentoServices.getTaxaDesconto(saida.getCdEmpresa(),
												formPag.getCdFormaPagamento(), cdPlanoPagamento, 
												formaPagamentoEmpresa.getPrTaxaDesconto(), connect);
					vlDescontoTEF = new Double( Math.round(vlFaturado * prTaxaDesconto) / 100 );
				}
				
				//Quando há programa de fatura e desconto a aplicar para esse programa, acrescentar
				Double vlDescontoProgramaFatura = 0.0;
				if(programaVigente != null && programaVigente.getPrDesconto() > 0){
					vlDescontoProgramaFatura = new Double( Math.round(vlFaturado * programaVigente.getPrDesconto()) / 100);
				}
				// Calculando a data de vencimento
				GregorianCalendar venc = (GregorianCalendar)dataVencimento.clone();
				boolean lgIgnorarDiasNaoUteis = ParametroServices.getValorOfParametroAsInteger("LG_IGNORAR_DIA_NAO_UTIL", 0, saida.getCdEmpresa(), connect)==1;
				if(!lgIgnorarDiasNaoUteis)
					while(venc.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY || venc.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || FeriadoServices.isFeriado(venc, connect))
						venc.add(Calendar.DATE, 1);
				
				String idContaReceber = saida.getNrDocumentoSaida();
				
				// Valor
				Double vlConta = Double.valueOf(String.valueOf(vlFaturado));
				
				float vlDescontoConta    = (float)Math.round((vlDesconto) / vlFaturado * vlConta * 100) / 100;
				float vlDescontoContaTEF = (float)Math.round((vlDescontoTEF) / vlFaturado * vlConta * 100) / 100;
				float vlDescontoContaProgramaFatura = (float)Math.round((vlDescontoProgramaFatura) / vlFaturado * vlConta * 100) / 100;
				
				// Cria conta a receber
				ContaReceber contaReceber = new ContaReceber(0 /*cdContaReceber*/, cdSacado, saida.getCdEmpresa(),
															 0/*cdContrato*/, cdContaOrigem, saida.getCdDocumentoSaida(),
															 cdContaCarteira, cdContaFinanceira, 0/*cdFrete*/, idContaReceber,
															 saida.getNrDocumentoSaida()+"-"+com.tivic.manager.util.Util.fillNum(0, 2),
															 0,"" /*nrReferencia*/,cdTipoDocumento,"" /*dsHistorico*/,
															 venc, dtEmissao, dtRec, null /*dtProrrogacao*/,
															 (vlConta + vlDescontoConta),
															 ( vlDescontoContaTEF + vlDescontoConta + vlDescontoContaProgramaFatura),
															 0.0d/*vlAcrescimo*/,
															 0.0d /*vlRecebido*/, ContaReceberServices.ST_EM_ABERTO, 0 /*tpFrequencia*/,
															 1, ContaReceberServices.TP_PARCELA /*tpContaReceber*/,
															 0 /*cdNegociacao*/, "" /*txtObservacao*/, cdPlanoPagamento,
															 formaPagamentoEmpresa.getCdFormaPagamento(), new GregorianCalendar(), venc,
															 saida.getCdTurno(),
															 Double.valueOf( Float.toString(programaVigente.getPrDesconto())) , 0.0d, 0/*lgProtesto*/);
				contas.add(contaReceber);
				ArrayList<ContaReceberCategoria> contaCategorias = new ArrayList<ContaReceberCategoria>();
				for(int x=0; x<contaReceberCategorias.size(); x++)	{
					Double vlCategoria = contaReceberCategorias.size()==1 ? (vlConta+vlDescontoConta) : 0;
					if(vlTotalDocumento > 0 && vlConta > 0 && vlCategoria <= 0)
						vlCategoria = new Double(contaReceberCategorias.get(x).getVlContaCategoria() / vlTotalDocumento * (vlConta+vlDescontoConta));
					contaCategorias.add(new ContaReceberCategoria(0/*cdContaReceber*/, contaReceberCategorias.get(x).getCdCategoriaEconomica(), vlCategoria, 0));
				}
				
				/* DESCONTO - TEF */
				if(vlDescontoTEF > 0 && cdCategoriaDescontoTEF>0)
					contaCategorias.add(new ContaReceberCategoria(0/*cdContaReceber*/, cdCategoriaDescontoTEF, vlDescontoContaTEF, 0));
				
				/* DESCONTO - Programa de Fatura */
				if(vlDescontoContaProgramaFatura > 0){
					/*Acrescentando desconto de programa de fatura ao plano de pagamento*/
					PlanoPagtoDocumentoSaida planoPag = PlanoPagtoDocumentoSaidaDAO.get(cdPlanoPagamento, saida.getCdDocumentoSaida(), formaPagamentoEmpresa.getCdFormaPagamento(), connect);
					if(planoPag != null){
						planoPag.setVlDesconto(planoPag.getVlDesconto() + vlDescontoContaProgramaFatura);
						if(PlanoPagtoDocumentoSaidaDAO.update(planoPag, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao atualizar plano de pagamento");
						}
					}
					
					if(cdCategoriaDescontoProgramaFatura>0)
						contaCategorias.add(new ContaReceberCategoria(0/*cdContaReceber*/, cdCategoriaDescontoProgramaFatura, vlDescontoContaProgramaFatura, 0));
					
				}
				
				/* DESCONTO - CONCEDIDO AO CLIENTE */
				if(vlDesconto > 0 && cdCategoriaDesconto>0)
					contaCategorias.add(new ContaReceberCategoria(0/*cdContaReceber*/, cdCategoriaDesconto, vlDescontoConta, 0));
				
				
				
				/*
				 *  Insere conta a receber no banco de dados
				 */
				Result result = simulateFat ? new Result(0) : ContaReceberServices.insert(contaReceber, contaCategorias, null, true, connect);

				contaReceber.setCdContaReceber(result.getCode());
				if(contaReceber.getCdContaReceber() <= 0 && !simulateFat)	{
					if (isConnectionNull)
						Conexao.rollback(connect);
					com.tivic.manager.util.Util.registerLog(new Exception("Não foi possível gravar a conta a receber! erro: "+contaReceber.getCdContaReceber()));
					return new Result(ERR_FAT_SAVE_CONTA_RECEBER, "Não foi possível gravar a conta a receber! erro: "+contaReceber.getCdContaReceber());
				}
				// Registra categorias
				for (int x=0; x<contaCategorias.size(); x++) {
					boolean categoriaPrexistente = false;
					// Agrupa categorias
					for(int c=0; c<categoriasMov.size(); c++)
						if(categoriasMov.get(c).getCdCategoriaEconomica() == contaCategorias.get(x).getCdCategoriaEconomica())	{
							categoriaPrexistente = true;
							categoriasMov.get(c).setVlMovimentoCategoria(categoriasMov.get(c).getVlMovimentoCategoria() + contaCategorias.get(x).getVlContaCategoria());
							break;
						}
					//
					if(!categoriaPrexistente) {
						categoriasMov.add(new MovimentoContaCategoria(cdConta,0/*cdMovimentoConta*/,contaCategorias.get(x).getCdCategoriaEconomica(),
																  	  contaCategorias.get(x).getVlContaCategoria(),0 /*cdMovimentoContaCategoria*/,
																  	  0 /*cdContaPagar*/,contaReceber.getCdContaReceber(),
																  	  MovimentoContaCategoriaServices.TP_PRE_CLASSIFICACAO /*tpMovimento*/, 
																  	  contaCategorias.get(x).getCdCentroCusto()));
					}
				}
				/*
				 *  Lança recebimento da conta (associação entre conta a receber e movimento de conta)
				 */
				if(formPag.getTpFormaPagamento()==FormaPagamentoServices.MOEDA_CORRENTE)	{
					recebimentos.add(new MovimentoContaReceber(cdConta, 0/*cdMovimentoConta*/, contaReceber.getCdContaReceber(),
															   vlConta, 0 /*vlJuros*/, 0 /*vlMulta*/, 0 /*vlDesconto*/,
															   0 /*vlTarifaCobranca*/, 0 /*cdArquivo*/, 0 /*cdRegistro*/));
					contaReceber.setStConta(ContaReceberServices.ST_RECEBIDA);

				}
				// Se não for moeda corrente, é TEF ou TÍTULO DE CRÉDITO
				else	{
					ResultSetMap rsmPessoa = null;
					if(rsmPessoa==null){
						ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
						crt.add(new ItemComparator("A.cd_pessoa", String.valueOf(cdSacado), ItemComparator.EQUAL, Types.INTEGER));
						rsmPessoa = PessoaServices.find(crt, Conexao.conectar());
					}

					rsmPessoa.beforeFirst();
					boolean existsSacado        = rsmPessoa.next();
					int tpDocumentoEmissor 		= TituloCreditoServices.tdocCPF;
					String nrDocumentoEmissor 	= existsSacado ? rsmPessoa.getString("nr_cpf") : null;
					if(existsSacado && rsmPessoa.getInt("gn_pessoa")==PessoaServices.TP_JURIDICA)	{
						nrDocumentoEmissor	= rsmPessoa.getString("nr_cnpj");
						tpDocumentoEmissor 	= TituloCreditoServices.tdocCNPJ;
					}
					TituloCredito tituloCredito = new TituloCredito(0, 0 /*cdInstituicaoFinanceira*/, 0 /*cdAlinea*/, contaReceber.getNrDocumento(),
							                                        nrDocumentoEmissor, tpDocumentoEmissor, !existsSacado ? "" : rsmPessoa.getString("nm_pessoa"),
							                                        vlConta, TituloCreditoServices.teAPRAZO, "" /*dsObservacao*/, venc,
							                                        null /*dtCredito*/, TituloCreditoServices.stEM_ABERTO, "",cdTipoDocumento,
							                                        TituloCreditoServices.tcPORTADOR, cdConta, contaReceber.getCdContaReceber(), 0, 0, "");
					tituloCredito.setCdTituloCredito(simulateFat ? 0 : TituloCreditoDAO.insert(tituloCredito, connect));
					movimentoTitulo.add(new MovimentoContaTituloCredito(tituloCredito.getCdTituloCredito(), 0/*cdMovimentoConta*/, cdConta));
				}
				
			}
			else{
				ResultSetMap rsmPessoa = null;
				rsmParcelas.beforeFirst();
				int nrParcela   = 0;
				int qtParcelas  = 0;
				float vlLancado = 0;
				boolean lgIgnorarDiasNaoUteis = ParametroServices.getValorOfParametroAsInteger("LG_IGNORAR_DIA_NAO_UTIL", 0, saida.getCdEmpresa(), connect)==1;
				// Contando a quantidade de parcelas
				while(rsmParcelas.next())
					qtParcelas += rsmParcelas.getInt("qt_parcelas");
				rsmParcelas.beforeFirst();
				while(rsmParcelas.next())	{
					// Inicia a data de vencimento
					dtVencimento = new GregorianCalendar();
					dtVencimento.set(Calendar.HOUR, 0);
					dtVencimento.set(Calendar.MINUTE, 0);
					dtVencimento.set(Calendar.SECOND, 0);
					dtVencimento.set(Calendar.MILLISECOND, 0);
					/*
					 *  Cria conta a receber
					 */
					if (rsmParcelas.getInt("nr_dias")==30)
						dtVencimento.add(Calendar.MONTH, 1);
					else
						dtVencimento.add(Calendar.DATE, rsmParcelas.getInt("nr_dias"));
					float vlDescontoTEF = 0;
					// Se for TEF coloca as contas a receber para o dia configurado na forma de pagamento
					if(formPag.getTpFormaPagamento()==FormaPagamentoServices.TEF)	{
						if(formaPagamentoEmpresa.getQtDiasCredito()==30)
							dtVencimento.add(Calendar.MONTH, 1);
						else
							dtVencimento.add(Calendar.DATE, formaPagamentoEmpresa.getQtDiasCredito());
						double prTaxaDesconto = FormaPagamentoServices.getTaxaDesconto(saida.getCdEmpresa(), formPag.getCdFormaPagamento(), cdPlanoPagamento, formaPagamentoEmpresa.getPrTaxaDesconto(), connect);
						vlDescontoTEF = (float)Math.round(vlFaturado * prTaxaDesconto) / 100;
					}
					int nrDiaVencimento = dtVencimento.get(Calendar.DATE);
					for(int i=0; i<rsmParcelas.getInt("qt_parcelas"); i++)	{
						// Calculando a data de vencimento
						GregorianCalendar venc = (GregorianCalendar)dtVencimento.clone();
						if(!lgIgnorarDiasNaoUteis)
							while(venc.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY || venc.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || FeriadoServices.isFeriado(venc, connect))
								venc.add(Calendar.DATE, 1);
						String idContaReceber = saida.getNrDocumentoSaida();
						// Valor
						Double vlConta = (Math.round(rsmParcelas.getDouble("pr_valor_total") / rsmParcelas.getInt("qt_parcelas") * vlFaturado) / 100.0);
						// Se for título de crédito para facilitar o preenchimento joga os centavos pra o ultimo
						if(formPag.getTpFormaPagamento()==FormaPagamentoServices.TITULO_CREDITO)
							vlConta =  new Double(Math.round(vlConta));
						
						// Verifica se é a ultima conta lançada e lança o restante para evitar valor residual decorrente de aproximação
						if (rsmParcelas.getPosition()==rsmParcelas.size()-1 && i==rsmParcelas.getInt("qt_parcelas")-1)
							vlConta = new Double(vlFaturado - vlLancado);
						vlLancado += vlConta;
						// pega informações diferenciadas
						for (int j=0; dadosParcelas!=null && j<dadosParcelas.size(); j++)
							if (dadosParcelas.get(j).get("nrParcela")!=null && ((Integer)dadosParcelas.get(j).get("nrParcela")).intValue()==nrParcela) {
								// Acrescentado para permitir o PDV de usar essa função sem necessidade de determinar o valor
								if(dadosParcelas.get(j).get("vlParcela")!=null && ((Float)dadosParcelas.get(j).get("vlParcela")).floatValue()>0)
									vlConta = ((Double)dadosParcelas.get(j).get("vlParcela")).doubleValue();
								if(dadosParcelas.get(j).get("idContaReceber")!=null)
									idContaReceber = (String)dadosParcelas.get(j).get("idContaReceber");
								break;
							}
						float vlDescontoConta    = (float)Math.round((vlDesconto) / vlFaturado * vlConta * 100) / 100;
						float vlDescontoContaTEF = (float)Math.round((vlDescontoTEF) / vlFaturado * vlConta * 100) / 100;
						// Cria conta a receber
						ContaReceber contaReceber = new ContaReceber(0 /*cdContaReceber*/, cdSacado, saida.getCdEmpresa(),
																	 0/*cdContrato*/, cdContaOrigem, saida.getCdDocumentoSaida(),
																	 cdContaCarteira, cdContaFinanceira, 0/*cdFrete*/, idContaReceber,
																	 saida.getNrDocumentoSaida()+"-"+com.tivic.manager.util.Util.fillNum(nrParcela, 2),
																	 nrParcela,"" /*nrReferencia*/,cdTipoDocumento,"" /*dsHistorico*/,
																	 venc, dtEmissao, dtRec, null /*dtProrrogacao*/,
																	  vlConta + vlDescontoConta,
																	 Double.valueOf( Float.toString(  vlDescontoContaTEF + vlDescontoConta )),
																	 0.0d/*vlAcrescimo*/,
																	 0.0d /*vlRecebido*/, ContaReceberServices.ST_EM_ABERTO, 0 /*tpFrequencia*/,
																	 qtParcelas, ContaReceberServices.TP_PARCELA /*tpContaReceber*/,
																	 0 /*cdNegociacao*/, "" /*txtObservacao*/, cdPlanoPagamento,
																	 formaPagamentoEmpresa.getCdFormaPagamento(), new GregorianCalendar(), venc,
																	 saida.getCdTurno(), 0.0d, 0.0d, 0/*lgProtesto*/);
						nrParcela++;
						contas.add(contaReceber);
						ArrayList<ContaReceberCategoria> contaCategorias = new ArrayList<ContaReceberCategoria>();
						for(int x=0; x<contaReceberCategorias.size(); x++)	{
							Double vlCategoria = contaReceberCategorias.size()==1 ? (vlConta+vlDescontoConta) : 0;
							if(vlTotalDocumento > 0 && vlConta > 0 && vlCategoria <= 0)
								vlCategoria =  contaReceberCategorias.get(x).getVlContaCategoria() / vlTotalDocumento * (vlConta+vlDescontoConta);
							contaCategorias.add(new ContaReceberCategoria(0/*cdContaReceber*/, contaReceberCategorias.get(x).getCdCategoriaEconomica(), vlCategoria, 0));
						}
						
						/* DESCONTO - TEF */
						if(vlDescontoTEF > 0 && cdCategoriaDescontoTEF>0)
							contaCategorias.add(new ContaReceberCategoria(0/*cdContaReceber*/, cdCategoriaDescontoTEF, vlDescontoContaTEF, 0));
						
						/* DESCONTO - CONCEDIDO AO CLIENTE */
						if(vlDesconto > 0 && cdCategoriaDesconto>0)
							contaCategorias.add(new ContaReceberCategoria(0/*cdContaReceber*/, cdCategoriaDesconto, vlDescontoConta, 0));
						
						/*
						 *  Insere conta a receber no banco de dados
						 */
						Result result = simulateFat ? new Result(0) : ContaReceberServices.insert(contaReceber, contaCategorias, null, true, connect);
	
						contaReceber.setCdContaReceber(result.getCode());
						if(contaReceber.getCdContaReceber() <= 0 && !simulateFat)	{
							if (isConnectionNull)
								Conexao.rollback(connect);
							com.tivic.manager.util.Util.registerLog(new Exception("Não foi possível gravar a conta a receber! erro: "+contaReceber.getCdContaReceber()));
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(ERR_FAT_SAVE_CONTA_RECEBER, "Não foi possível gravar a conta a receber! erro: "+contaReceber.getCdContaReceber());
						}
						// Registra categorias
						for (int x=0; x<contaCategorias.size(); x++) {
							boolean categoriaPrexistente = false;
							// Agrupa categorias
							for(int c=0; c<categoriasMov.size(); c++)
								if(categoriasMov.get(c).getCdCategoriaEconomica() == contaCategorias.get(x).getCdCategoriaEconomica())	{
									categoriaPrexistente = true;
									categoriasMov.get(c).setVlMovimentoCategoria(categoriasMov.get(c).getVlMovimentoCategoria() + contaCategorias.get(x).getVlContaCategoria());
									break;
								}
							//
							if(!categoriaPrexistente) {
								categoriasMov.add(new MovimentoContaCategoria(cdConta,0/*cdMovimentoConta*/,contaCategorias.get(x).getCdCategoriaEconomica(),
																		  	  contaCategorias.get(x).getVlContaCategoria(),0 /*cdMovimentoContaCategoria*/,
																		  	  0 /*cdContaPagar*/,contaReceber.getCdContaReceber(),
																		  	  MovimentoContaCategoriaServices.TP_PRE_CLASSIFICACAO /*tpMovimento*/, 
																		  	  contaCategorias.get(x).getCdCentroCusto()));
							}
						}
						/*
						 *  Lança recebimento da conta (associação entre conta a receber e movimento de conta)
						 */
						if(formPag.getTpFormaPagamento()==FormaPagamentoServices.MOEDA_CORRENTE)	{
							recebimentos.add(new MovimentoContaReceber(cdConta, 0/*cdMovimentoConta*/, contaReceber.getCdContaReceber(),
																	   vlConta, 0 /*vlJuros*/, 0 /*vlMulta*/, 0 /*vlDesconto*/,
																	   0 /*vlTarifaCobranca*/, 0 /*cdArquivo*/, 0 /*cdRegistro*/));
							contaReceber.setStConta(ContaReceberServices.ST_RECEBIDA);
	
						}
						// Se não for moeda corrente, é TEF ou TÍTULO DE CRÉDITO
						else	{
							if(rsmPessoa==null){
								ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
								crt.add(new ItemComparator("A.cd_pessoa", String.valueOf(cdSacado), ItemComparator.EQUAL, Types.INTEGER));
								rsmPessoa = PessoaServices.find(crt, Conexao.conectar());
							}
	
							rsmPessoa.beforeFirst();
							boolean existsSacado        = rsmPessoa.next();
							int tpDocumentoEmissor 		= TituloCreditoServices.tdocCPF;
							String nrDocumentoEmissor 	= existsSacado ? rsmPessoa.getString("nr_cpf") : null;
							if(existsSacado && rsmPessoa.getInt("gn_pessoa")==PessoaServices.TP_JURIDICA)	{
								nrDocumentoEmissor	= rsmPessoa.getString("nr_cnpj");
								tpDocumentoEmissor 	= TituloCreditoServices.tdocCNPJ;
							}
							TituloCredito tituloCredito = new TituloCredito(0, 0 /*cdInstituicaoFinanceira*/, 0 /*cdAlinea*/, contaReceber.getNrDocumento(),
									                                        nrDocumentoEmissor, tpDocumentoEmissor, !existsSacado ? "" : rsmPessoa.getString("nm_pessoa"),
									                                        vlConta, TituloCreditoServices.teAPRAZO, "" /*dsObservacao*/, venc,
									                                        null /*dtCredito*/, TituloCreditoServices.stEM_ABERTO, "",cdTipoDocumento,
									                                        TituloCreditoServices.tcPORTADOR, cdConta, contaReceber.getCdContaReceber(), 0, 0, "");
							tituloCredito.setCdTituloCredito(simulateFat ? 0 : TituloCreditoDAO.insert(tituloCredito, connect));
							movimentoTitulo.add(new MovimentoContaTituloCredito(tituloCredito.getCdTituloCredito(), 0/*cdMovimentoConta*/, cdConta));
						}
						dtVencimento = ContaPagarServices.getProximoVencimento(dtVencimento, rsmParcelas.getInt("tp_intervalo"), nrDiaVencimento);
					}
				}
			}
			/*
			 *  Cria movimento de conta
			 */
			if (!simulateFat) {
				int stMovimentoConta = formPag.getTpFormaPagamento()==FormaPagamentoServices.MOEDA_CORRENTE ? MovimentoContaServices.ST_COMPENSADO
                        : MovimentoContaServices.ST_NAO_COMPENSADO;

				MovimentoConta movimento = new MovimentoConta(0 /*cdMovimentoConta*/,cdConta, 0 /*cdContaOrigem*/,0 /*cdMovimentoOrigem*/,
															  cdUsuario,0 /*cdCheque*/, 0/*cdViagem*/, dtEmissao /*dtMovimento*/,
															  vlFaturado /*vlMovimento*/,
															  saida.getNrConhecimento()!=null ? "T:"+saida.getNrConhecimento() : "Fat_"+saida.getNrDocumentoSaida() /*nrDocumento*/,
															  MovimentoContaServices.CREDITO, MovimentoContaServices.toCREDITO, stMovimentoConta,
															  "Venda Nº "+saida.getNrDocumentoSaida()+
															  (saida.getNrConhecimento()!=null ? ", Ticket Nº "+saida.getNrConhecimento():""),
															  null /*dtDeposito*/, null /*idExtrato*/,	formaPagamentoEmpresa.getCdFormaPagamento(),
															  0 /*cdFechamento*/, saida.getCdTurno());
				/*
				 *  Insere movimento da conta no banco de dados
				 */
				Result result = MovimentoContaServices.insert(movimento, recebimentos, categoriasMov, movimentoTitulo, ChequeServices.EMITIDO, connect);
				// Se o movimento da conta não foi incluido com sucesso aborta
				if(result.getCode() <= 0)	{
					if (isConnectionNull)
						Conexao.rollback(connect);
					result.setMessage("Falha ao tentar lancar faturamento! \n"+result.getMessage());
					com.tivic.manager.util.Util.registerLog(new Exception(result.getMessage()));
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
			}
			
			/**
			 * Verificando Limites do cliente - Titulo de Credito3
			 */
			float vlLimiteGasto  = 0;
			float vlLimitePessoa = 0;
			Pessoa pessoaCliente = PessoaDAO.get(saida.getCdCliente());
			int lgPermiteLimiteUltrapassado = ParametroServices.getValorOfParametroAsIntegerByPessoa("lgPermiteLimiteUltrapassado", 0, saida.getCdCliente());
			int lgPermissaoSupervisor = ParametroServices.getValorOfParametroAsIntegerByPessoa("lgPedeSenhaSupervisor", 0, saida.getCdCliente());
			if(formPag.getTpFormaPagamento() == FormaPagamentoServices.TITULO_CREDITO && !permitirLimiteUltrapassado){
				//Limite por condição de pagamento
				PreparedStatement pstmtCondicoesCliente = connect.prepareStatement("SELECT * FROM adm_condicao_pagamento A" +
																				"	  JOIN adm_condicao_pagamento_cliente B ON (A.cd_condicao_pagamento = B.cd_condicao_pagamento) " +
																				"	  JOIN adm_condicao_forma_plano_pagamento C ON (A.cd_condicao_pagamento = C.cd_condicao_pagamento) " +
																				"     WHERE B.cd_pessoa  = ?" +
																				" 	    AND B.cd_empresa = ?" +
																				" 	    AND C.cd_forma_pagamento = ?");
				
				PreparedStatement pstmtCondicoesClassificacao = connect.prepareStatement("SELECT * FROM adm_condicao_pagamento A" +
																					"	  JOIN adm_condicao_pagamento_cliente B ON (A.cd_condicao_pagamento = B.cd_condicao_pagamento) " +
																					"	  JOIN adm_condicao_forma_plano_pagamento C ON (A.cd_condicao_pagamento = C.cd_condicao_pagamento) " +
																					"     WHERE B.cd_classificacao  = ?" +
																					" 	    AND C.cd_forma_pagamento = ?");

				PreparedStatement pstmtContasCondicao = connect.prepareStatement("SELECT SUM(A.vl_conta - (A.vl_recebido + A.vl_abatimento - A.vl_acrescimo)) AS vl_pendente FROM adm_conta_receber A " +
																				"  WHERE A.st_conta   = " + ContaReceberServices.ST_EM_ABERTO +
																				"    AND A.cd_pessoa  = " + saida.getCdCliente() +
																				"    AND A.cd_empresa = " + saida.getCdEmpresa() +
																				"    AND A.cd_forma_pagamento = ?");
				
				pstmtCondicoesCliente.setInt(1, saida.getCdCliente());
				pstmtCondicoesCliente.setInt(2, saida.getCdEmpresa());
				pstmtCondicoesCliente.setInt(3, formaPagamentoEmpresa.getCdFormaPagamento());
				//Reune as condições do cliente
				ResultSetMap rsmCondicoesCliente = new ResultSetMap(pstmtCondicoesCliente.executeQuery());
				
				//Caso o cliente não tenha condicoes de pagamento vinculadas, reune as condições da classificacao
				if(rsmCondicoesCliente.size() == 0){
					if(cliente != null){
						Classificacao classificacao = ClassificacaoDAO.get(cliente.getCdClassificacaoCliente(), connect);
						//Se o cliente não tiver classificacao, o sistema usará a padrão
						if(classificacao != null){
							pstmtCondicoesClassificacao.setInt(1, classificacao.getCdClassificacao());
							rsmCondicoesCliente = new ResultSetMap(pstmtCondicoesClassificacao.executeQuery());
						}
						else{
							classificacao = ClassificacaoServices.getPadrao();
							if(classificacao != null){
								pstmtCondicoesClassificacao.setInt(1, classificacao.getCdClassificacao());
								rsmCondicoesCliente = new ResultSetMap(pstmtCondicoesClassificacao.executeQuery());
							}
						}
					}
					
				}
				
				//Caso nem o cliente nem sua classificacao nem a classificacao padrão não tenha condicoes de pagamento vinculadas, busca a condição padrão
				if(rsmCondicoesCliente.size() == 0){
					CondicaoPagamento condicaoPadrao = CondicaoPagamentoServices.getPadrao();
					if(condicaoPadrao != null){
						rsmCondicoesCliente = CondicaoPagamentoServices.get(condicaoPadrao.getCdCondicaoPagamento(), connect);
					}
				}
				
				boolean hasCondicao = false;
				while(rsmCondicoesCliente.next()){
					if(rsmCondicoesCliente.getInt("st_condicao_pagamento")==1 && rsmCondicoesCliente.getGregorianCalendar("dt_validade_limite") != null && Util.getQuantidadeDiasUteis(rsmCondicoesCliente.getGregorianCalendar("dt_validade_limite"), Util.getDataAtual(), connect) <= 1){
						FormaPagamento formaPagamento = FormaPagamentoDAO.get(rsmCondicoesCliente.getInt("cd_forma_pagamento"));						
						pstmtContasCondicao.setInt(1, formaPagamento.getCdFormaPagamento());
						ResultSetMap rsmContasCondicao = new ResultSetMap(pstmtContasCondicao.executeQuery());
						if (rsmCondicoesCliente.getFloat("vl_limite") > 0){ 
							if(rsmContasCondicao.next()){
								hasCondicao = true;
								if(rsmCondicoesCliente.getFloat("vl_limite") < rsmContasCondicao.getFloat("vl_pendente")){
									float vlDisponivel = (rsmCondicoesCliente.getFloat("vl_limite") - (rsmContasCondicao.getFloat("vl_pendente") - vlFaturado));	
									vlDisponivel = (vlDisponivel < 0 ? 0 : vlDisponivel);
									if (isConnectionNull)
										Conexao.rollback(connect);								
									Result result = new Result((lgPermiteLimiteUltrapassado==0?-1:RET_PERMITE_LIMITE_ULTRAPASSADO), "Limite para pagamento em " + formaPagamento.getNmFormaPagamento() + " do cliente "+pessoaCliente.getNmPessoa()+" foi ultrapassado. " +
																																	"\nLimite total do cliente: R$ "+Util.formatNumber(rsmCondicoesCliente.getFloat("vl_limite"), Util.MASK_CURRENCY)+". " +
																																	"\nLimite total utilizado:   R$ "+Util.formatNumber((rsmCondicoesCliente.getFloat("vl_limite") - vlDisponivel), Util.MASK_CURRENCY)+
																																	"\nLimite total disponivel: R$ "+ Util.formatNumber((vlDisponivel), Util.MASK_CURRENCY));
									if(isConnectionNull)
										Conexao.rollback(connect);
									result.addObject("lgPermissaoSupervisor", lgPermissaoSupervisor);									
									return result;
								}
								break;
							}
						}
						rsmContasCondicao.beforeFirst();
					}
				}
				if(ParametroServices.getValorOfParametroAsInteger("LG_CONDICAO_PAGAMENTO_OBRIGATORIA", 0, saida.getCdEmpresa())==1 && !hasCondicao){
					if (isConnectionNull)
						Conexao.rollback(connect);
					Result result = new Result(-1, "O cliente "+pessoaCliente.getNmPessoa()+" nao possui condicao de pagamento cadastrada para essa forma de pagamento");
					return result;
				}						
				//Limite de Combustiveis
				PreparedStatement pstmtDocumentoCombustivel = connect.prepareStatement("SELECT B.cd_documento_saida, SUM(A.vl_conta - (A.vl_recebido + A.vl_abatimento - A.vl_acrescimo)) AS vl_pendente FROM adm_conta_receber A " +
																					"JOIN alm_documento_saida B ON (A.cd_documento_saida = B.cd_documento_saida) " +
																					"WHERE B.st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO +
																					" AND A.st_conta   = " + ContaReceberServices.ST_EM_ABERTO + 
																					" AND B.cd_cliente = " + saida.getCdCliente() +
																					" AND B.cd_empresa = " + saida.getCdEmpresa() +
																					" AND EXISTS(SELECT C.cd_documento_saida FROM alm_documento_saida_item C WHERE C.cd_documento_saida = B.cd_documento_saida AND C.cd_produto_servico = ?)" +
																				  	" GROUP BY B.cd_documento_saida");
	
				
				PreparedStatement pstmtCombustivel = connect.prepareStatement("SELECT B.cd_produto_servico AS cd_combustivel, B.nm_produto_servico AS nm_combustivel " +
																				  "FROM alm_produto_grupo A " +
																				  "JOIN grl_produto_servico B ON (A.cd_produto_servico = B.cd_produto_servico) " +
																				  "WHERE A.cd_grupo = "
								+ ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, saida.getCdEmpresa()) + 
																				  "  AND A.cd_produto_servico = ?");
	
				PreparedStatement pstmtLimite = connect.prepareStatement(" SELECT vl_limite FROM adm_cliente_produto A " +
																		" WHERE A.cd_pessoa = " + saida.getCdCliente() +
																		" AND A.cd_empresa = "  + saida.getCdEmpresa() +
																		" AND A.cd_produto_servico = ?");
				
				ResultSetMap rsmItens = DocumentoSaidaServices.getAllItens(saida.getCdDocumentoSaida(), connect);
				
				while(rsmItens.next()){
					pstmtCombustivel.setInt(1, rsmItens.getInt("cd_produto_servico"));
					ResultSetMap rsmCombustivel = new ResultSetMap(pstmtCombustivel.executeQuery());
					if(rsmCombustivel.next()){
						
						float vlLimiteGastoCombustivel = 0;
						
						pstmtDocumentoCombustivel.setInt(1, rsmCombustivel.getInt("cd_combustivel"));
						ResultSetMap rsmDocumentoCombustivel = new ResultSetMap(pstmtDocumentoCombustivel.executeQuery());
						
						float vlTotalItemDocAtual = 0;
						
						while(rsmDocumentoCombustivel.next()){
							DocumentoSaida docComb = DocumentoSaidaDAO.get(rsmDocumentoCombustivel.getInt("cd_documento_saida"), connect);
							ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
							criterios.add(new ItemComparator("cd_documento_saida", "" + docComb.getCdDocumentoSaida(), ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("cd_produto_servico", rsmCombustivel.getString("cd_combustivel"), ItemComparator.EQUAL, Types.INTEGER));
							
							ResultSetMap rsmItemDoc = DocumentoSaidaItemDAO.find(criterios, connect);
							
							float vlTotalItem = 0;
							
							while(rsmItemDoc.next()){
								vlTotalItem += (rsmItemDoc.getFloat("vl_unitario") * rsmItemDoc.getFloat("qt_saida") + rsmItemDoc.getFloat("vl_acrescimo") - rsmItemDoc.getFloat("vl_desconto"));
							}
							
							float prItem = vlTotalItem / docComb.getVlTotalDocumento();
							
							vlLimiteGastoCombustivel += rsmDocumentoCombustivel.getFloat("vl_pendente") * prItem;
							if(docComb.getCdDocumentoSaida() == saida.getCdDocumentoSaida())
								vlTotalItemDocAtual += (rsmDocumentoCombustivel.getFloat("vl_pendente") * prItem);
						}
						
						
						pstmtLimite.setInt(1, rsmCombustivel.getInt("CD_COMBUSTIVEL"));
						ResultSetMap rsmLimite = new ResultSetMap(pstmtLimite.executeQuery());
						if(rsmLimite.next() && rsmLimite.getFloat("vl_limite") > 0 && (rsmLimite.getFloat("vl_limite") < vlLimiteGastoCombustivel)){
							if (isConnectionNull)
								Conexao.rollback(connect);
							float vlDisponivel = (rsmLimite.getFloat("vl_limite") - (vlLimiteGastoCombustivel - vlTotalItemDocAtual));
							vlDisponivel = (vlDisponivel < 0 ? 0 : vlDisponivel);
							
							Result result = new Result((lgPermiteLimiteUltrapassado==0?-1:RET_PERMITE_LIMITE_ULTRAPASSADO), "Limite para " + rsmCombustivel.getString("nm_combustivel") + " do cliente "+pessoaCliente.getNmPessoa()+" foi ultrapassado. " +
									 																						"\nLimite total do cliente: R$ "+Util.formatNumber(rsmLimite.getFloat("vl_limite"), Util.MASK_CURRENCY)+". " +
									 																						"\nLimite total utilizado:   R$ "+Util.formatNumber((rsmLimite.getFloat("vl_limite") - vlDisponivel), Util.MASK_CURRENCY)+
									 																						"\nLimite total disponivel: R$ " + Util.formatNumber((vlDisponivel), Util.MASK_CURRENCY));
							result.addObject("lgPermissaoSupervisor", lgPermissaoSupervisor);
							if(isConnectionNull)
								Conexao.rollback(connect);
							return result;
						}
		  				
					}
				}				
				//LimiteGeral
				PreparedStatement pstmtLimiteGeral = connect.prepareStatement("SELECT SUM(A.vl_conta - (A.vl_recebido + A.vl_abatimento - A.vl_acrescimo)) AS vl_pendente FROM adm_conta_receber A " +
																			"    JOIN alm_documento_saida B ON (A.cd_documento_saida = B.cd_documento_saida) " +
																			"   WHERE A.cd_pessoa          = B.cd_cliente " +
																			"     AND B.st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO +
																			"     AND A.st_conta   		   = " + ContaReceberServices.ST_EM_ABERTO +
																			"     AND B.cd_cliente 		   = " + saida.getCdCliente());
				
				
				ResultSetMap rsmLimiteGeral = new ResultSetMap(pstmtLimiteGeral.executeQuery());
//				Util.printInFile("/marlon", texto)
				while(rsmLimiteGeral.next()){
					vlLimiteGasto += rsmLimiteGeral.getFloat("vl_pendente");
				}
				if(cliente != null){
					vlLimitePessoa = cliente.getVlLimiteCredito();
					if(vlLimitePessoa > 0 && vlLimitePessoa < vlLimiteGasto){
						if (isConnectionNull)
							Conexao.rollback(connect);
						float vlDisponivel = (vlLimitePessoa - (vlLimiteGasto - vlFaturado));
						vlDisponivel = (vlDisponivel < 0 ? 0 : vlDisponivel);
						
						Result result = new Result((lgPermiteLimiteUltrapassado==0?-1:RET_PERMITE_LIMITE_ULTRAPASSADO), "Limite de credito do cliente "+pessoaCliente.getNmPessoa()+" ultrapassa o valor cadastrado."+
						                                                                                                "\nLimite total do cliente: R$ "+ Util.formatNumber(vlLimitePessoa, Util.MASK_CURRENCY)+"; "+
						                                                                                                "\nLimite total utilizado:   R$ " + Util.formatNumber((vlLimiteGasto - vlFaturado), Util.MASK_CURRENCY)+"; "+
						                                                                                                "\nLimite total disponivel: R$ " + Util.formatNumber((vlDisponivel), Util.MASK_CURRENCY)+".");
						result.addObject("lgPermissaoSupervisor", lgPermissaoSupervisor);
						if(isConnectionNull)
							Conexao.rollback(connect);
						return result;
					}
				}
			}
			
			/**
			 * Caso o cliente esteja configurado, emite uma nota fiscal eletronica para o documento de saída automáticamente
			 */
			Parametro parametro = ParametroServices.getByNameCdPessoa("lgEmiteNfeFaturamento", saida.getCdCliente());
			if (parametro != null){
				int cdParametro = parametro.getCdParametro();
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_pessoa", "" + saida.getCdCliente(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_parametro", "" + cdParametro, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmParametro = ParametroValorDAO.find(criterios, connect);
				int lgEmiteNfeFaturamento = 0;
				if(rsmParametro.next()){
					lgEmiteNfeFaturamento = Integer.parseInt(rsmParametro.getString("vl_inicial"));
				}
				
				if(lgEmiteNfeFaturamento == 1){
					Result resultado = NotaFiscalServices.fromDocSaidaToNF(saida.getCdDocumentoSaida(), connect);
					if(resultado.getCode() <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return resultado;
					}
				}
			}
			/**
			 * Caso esteja configurado, envia email para o cliente sobre o debito lancado - DEIXAR ESSA PARTE SEMPRE POR ULTIMO PARA NÃO SE ENVIAR EMAIL SE HOUVER ALGUM ERRO EM OUTRA PARTE
			 */			
			parametro = ParametroServices.getByNameCdPessoa("lgEnviaAvisoDebitoLancado", saida.getCdCliente());
			if (parametro != null && withEmail){
				int cdParametro = parametro.getCdParametro();
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_pessoa", "" + saida.getCdCliente(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_parametro", "" + cdParametro, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmParametro = ParametroValorDAO.find(criterios);
				int lgEnviaAvisoDebitoLancado = 0;
				if(rsmParametro.next()){
					lgEnviaAvisoDebitoLancado = Integer.parseInt(rsmParametro.getString("vl_inicial"));
				}
		
				FormaPagamento formaPagamento = FormaPagamentoDAO.get(formaPagamentoEmpresa.getCdFormaPagamento(), connect);
				PlanoPagamento planoPagamento = PlanoPagamentoDAO.get(cdPlanoPagamento, connect);
				
				if(formaPagamento.getTpFormaPagamento() == FormaPagamentoServices.TITULO_CREDITO && lgEnviaAvisoDebitoLancado > 0 && saida.getTpDocumentoSaida() == DocumentoSaidaServices.TP_CUPOM_FISCAL){
					
					Pessoa clientePessoa = PessoaDAO.get(saida.getCdCliente(), connect);
					PessoaFisica clienteFisica = PessoaFisicaDAO.get(saida.getCdCliente(), connect);
					
					Empresa empresa = EmpresaDAO.get(saida.getCdEmpresa(), connect);
					
					NumberFormat formatoPreco = NumberFormat.getCurrencyInstance();  
			        formatoPreco.setMaximumFractionDigits(2);
			        
			        String textoEmail = "<img src=\'cid:logoEmpresa\' width=\"30%\" height=\"30%\" /><br />";
			        textoEmail += "Prezad" + (clienteFisica == null || clienteFisica.getTpSexo() == 1 ? "a" : "o") + " " + (clienteFisica != null && clienteFisica.getNmFormaTratamento() != null && !clienteFisica.getNmFormaTratamento().equals("") ? clienteFisica.getNmFormaTratamento() : "") + " " + clientePessoa.getNmPessoa() + "<br /><br />";
					textoEmail += "Este é apenas um aviso de que foi lançado um débito em sua conta, referente a compra abaixo descriminada.<br /><br /><br />";
					textoEmail += "DESCRIÇÃO DA COMPRA<br /><br />";
					textoEmail += "<b>Forma de Pagamento:</b> " + formaPagamento.getNmFormaPagamento() + " <br />";
					textoEmail += "<b>Plano de Pagamento:</b> " + planoPagamento.getNmPlanoPagamento() + " <br />";
					textoEmail += "Cupom Fiscal Nº: " + saida.getNrDocumentoSaida() + " <br />";
					textoEmail += Util.fill("Data da compra: " + Util.formatDate(saida.getDtDocumentoSaida(), "dd/MM/yyyy"), 28, "&nbsp;", 'D')+ " Hora: "+Util.formatDate(saida.getDtDocumentoSaida(), "HH:mm:ss")+"<br />";
					textoEmail += Util.fill("Placa do Veículo: ", 35, "&nbsp;", 'D') + " Motorista: <br />";
					textoEmail += Util.fill("Valor: " + formatoPreco.format(saida.getVlTotalDocumento()), 35, "&nbsp;", 'D') + " Nº Parcelas: " + contas.size()+ "<br />";
					textoEmail += "Parcelas:  <br /><br />";
					
					for(int i = 0; i < contas.size(); i++){
						ResultSetMap rsmConta = new ResultSetMap(connect.prepareStatement("SELECT * FROM adm_conta_receber WHERE cd_conta_receber = " + contas.get(i).getCdContaReceber()).executeQuery());
						rsmConta.next();
						textoEmail += "Parcela Nº: " + (rsmConta.getInt("nr_parcela")+ 1) + " <br />";
						textoEmail += "Valor " + formatoPreco.format(rsmConta.getDouble("vl_conta")) + " <br />";
						textoEmail += "Data de Vencimento " + Util.formatDate(rsmConta.getGregorianCalendar("dt_vencimento"), "dd/MM/yyyy") + " <br /><br />"; 
					}
									
					textoEmail += Util.fill("Total Limite", 38, '.', 'D') + ": " + formatoPreco.format(vlLimitePessoa) + "<br />";
					textoEmail += Util.fill("Total compras efetuadas", 30, '.', 'D') + ": " + formatoPreco.format(vlLimiteGasto) + "<br /><br />";
					textoEmail += Util.fill("<b>Total saldo de limite", 30, '.', 'D') + ": " + formatoPreco.format((vlLimitePessoa - vlLimiteGasto)) + "</b><br /><br />";
					
					textoEmail += "Estamos a sua inteira disposição para mais esclarecimentos.<br /><br /><br />";
					textoEmail += "Atenciosamente,<br /><br />";
					textoEmail += "<b>" + empresa.getNmRazaoSocial() + "</b><br />";
					textoEmail += "<b>Fone: " + Util.formatTelefone(empresa.getNrTelefone1())+ "</b><br /><br /><br /><br /><br />";
					
					textoEmail += "*enviado automaticamente por DNA Suíte ERP, CRM e SCM<br /><br />";
					textoEmail += "<img src=\'cid:logoDesenvolvedora\' width=\"10%\" height=\"10%\" />";
					
					Result resultado = enviarEmailCupomDebito(saida.getCdDocumentoSaida(), saida.getCdCliente(), "Débito lançado", textoEmail, DocumentoSaidaArquivoServices.ST_FATURAR, connect);
					if(resultado.getCode() <= 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return resultado;
					}
					
				}
				
			}
			
			if (!simulateFat && isConnectionNull)
				connect.commit();
			Result result = new Result(1);
			result.addObject("contas", contas);
			result.addObject("recebimentos", recebimentos);
			result.addObject("lgPermissaoSupervisor", lgPermissaoSupervisor);
			return result;
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			if (!simulateFat && isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar lançar faturamento. "+e.getMessage(), e);
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
		try{
			int stDocumentoSaida = 0;
			boolean hasNotaFiscal = false;
			String nrNotaFiscal = "";
			for(int i=0; criterios!=null && i<criterios.size(); i++){
				if(criterios.get(i).getColumn().toUpperCase().indexOf("DT_DOCUMENTO_SAIDA")>=0)
					criterios.get(i).setColumn("CAST(dt_documento_saida AS DATE)");
				//
				if (criterios.get(i).getColumn().equalsIgnoreCase("stDocumentoSaida")) {
					stDocumentoSaida = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
				}
				if(criterios.get(i).getColumn().equalsIgnoreCase("nr_nota_fiscal")){
					hasNotaFiscal = true;
					nrNotaFiscal = criterios.get(i).getValue();
					criterios.remove(i);
				}
			}
			
			String[] limit = Util.getLimitAndSkip(1, 0);
			return Search.find("SELECT A.*, CAST(dt_documento_saida AS TIME) AS hr_documento_saida, B.nm_pessoa AS nm_cliente, C.nm_pessoa AS nm_transportadora, E.nr_cnpj AS nr_cnpj_transportadora, " +
							   "       D.nm_natureza_operacao, D.nr_codigo_fiscal, F.nm_pessoa AS nm_vendedor, G.nm_tipo_operacao, " +
							   "	   I.nm_pessoa AS nm_digitador, B.gn_pessoa, J.nr_cpf, J.nr_rg, J.dt_nascimento, L.nr_cnpj, " +
							   "       L.nr_inscricao_estadual, L.nr_inscricao_municipal, M.nm_pessoa AS nm_empresa, T.nm_turno, (X.nm_cidade || '-' || Z.nm_cidade) AS cl_viagem," +
							   "	  (SELECT SUM(E.qt_saida * vl_unitario + vl_acrescimo - vl_desconto) FROM alm_documento_saida_item E " +
							   "       WHERE E.cd_documento_saida = A.cd_documento_saida) AS vl_total_liquido, " +
							   "(SELECT SUM(E.qt_saida * vl_unitario) FROM alm_documento_saida_item E " +
							   "       WHERE E.cd_documento_saida = A.cd_documento_saida) AS vl_total_saida, " +
							   "(SELECT "+limit[0]+" vl_unitario FROM alm_documento_saida_item E " +
							   "       WHERE E.cd_documento_saida = A.cd_documento_saida  "+limit[1]+" ) AS vl_unitario, " +
							   "(SELECT "+limit[0]+" PS.nm_produto_servico FROM alm_documento_saida_item DS2, grl_produto_servico PS " +
							   "       WHERE DS2.cd_documento_saida = A.cd_documento_saida AND DS2.cd_produto_servico = PS.cd_produto_servico "+limit[1]+" ) AS nm_produto_servico, " +
							   "(SELECT "+limit[0]+" qt_saida FROM alm_documento_saida_item E " +
							   "       WHERE E.cd_documento_saida = A.cd_documento_saida "+limit[1]+") AS qt_saida, " +
							   "(SELECT H.vl_base_calculo " +
							   "       FROM adm_saida_tributo H " +
							   "       WHERE H.cd_documento_saida = A.cd_documento_saida AND H.cd_tributo = (SELECT I.cd_tributo FROM adm_tributo I WHERE I.id_tributo = 'ICMS' )) AS vl_base_calculo_icms, " +
							   "(SELECT H.vl_tributo " +
							   "       FROM adm_saida_tributo H " +
							   "       WHERE H.cd_documento_saida = A.cd_documento_saida AND H.cd_tributo = (SELECT I.cd_tributo FROM adm_tributo I WHERE I.id_tributo = 'ICMS' )) AS vl_icms, " +
							   "(SELECT H.vl_retido " +
							   "       FROM adm_saida_tributo H " +
							   "       WHERE H.cd_documento_saida = A.cd_documento_saida AND H.cd_tributo = (SELECT I.cd_tributo FROM adm_tributo I WHERE I.id_tributo = 'ICMS' )) AS vl_icms_substituto, " +
							   "(SELECT H.vl_base_retencao " +
							   "       FROM adm_saida_tributo H " +
							   "       WHERE H.cd_documento_saida = A.cd_documento_saida AND H.cd_tributo = (SELECT I.cd_tributo FROM adm_tributo I WHERE I.id_tributo = 'ICMS' )) AS vl_base_calculo_icms_substituto, " +
							   "(SELECT SUM(F.vl_desconto) " +
							   " FROM alm_documento_saida_item F " +
							   " WHERE F.cd_documento_saida = A.cd_documento_saida) AS vl_total_descontos, " +
							   "(SELECT SUM(G.vl_acrescimo) " +
							   " FROM alm_documento_saida_item G " +
							   " WHERE G.cd_documento_saida = A.cd_documento_saida) AS vl_total_acrescimos, " +
							   "(SELECT SUM(H.qt_saida) " +
							   " FROM alm_documento_saida_item H " +
							   " WHERE H.cd_documento_saida = A.cd_documento_saida) AS qt_total_produtos " +
							   "FROM alm_documento_saida A " +
							   "LEFT OUTER JOIN grl_pessoa 			  B ON (A.cd_cliente = B.cd_pessoa) " +
							   "LEFT OUTER JOIN grl_pessoa 			  C ON (A.cd_transportadora = C.cd_pessoa) " +
							   "LEFT OUTER JOIN adm_natureza_operacao D ON (A.cd_natureza_operacao = D.cd_natureza_operacao) " +
							   "LEFT OUTER JOIN grl_pessoa_juridica   E ON (C.cd_pessoa = E.cd_pessoa) " +
							   "LEFT OUTER JOIN grl_pessoa 		 	  F ON (A.cd_vendedor = F.cd_pessoa) " +
							   "LEFT OUTER JOIN adm_tipo_operacao 	  G ON (A.cd_tipo_operacao = G.cd_tipo_operacao) " +
							   "LEFT OUTER JOIN seg_usuario 		  H ON (A.cd_digitador = H.cd_usuario) " +
							   "LEFT OUTER JOIN grl_pessoa 			  I ON (H.cd_pessoa = I.cd_pessoa) " +
							   "LEFT OUTER JOIN grl_pessoa_fisica 	  J ON (B.cd_pessoa = J.cd_pessoa) "+
							   "LEFT OUTER JOIN grl_pessoa_juridica	  L ON (B.cd_pessoa = L.cd_pessoa) "+
							   "LEFT OUTER JOIN grl_pessoa            M ON (A.cd_empresa = M.cd_pessoa) "+
							   "LEFT OUTER JOIN adm_turno             T ON (A.cd_turno = T.cd_turno) "+
							   "LEFT OUTER JOIN fta_viagem            U ON (A.cd_viagem = U.cd_viagem) "+
							   "LEFT OUTER JOIN fta_rota              V ON (U.cd_rota = V.cd_rota) "+
							   "LEFT OUTER JOIN grl_cidade              X ON (V.cd_cidade_origem  = X.cd_cidade) "+
							   "LEFT OUTER JOIN grl_cidade              Z ON (V.cd_cidade_destino = Z.cd_cidade) "+
							   "WHERE 1=1 "+
							   // se caso tenha preenchido o número da nota no filtro
							   (hasNotaFiscal ? "AND A.cd_documento_saida IN (SELECT cd_documento_saida FROM fsc_nota_fiscal_item B LEFT JOIN fsc_nota_fiscal C ON (C.cd_nota_fiscal = B.cd_nota_fiscal ) WHERE C.nr_nota_fiscal ILIKE '%" + nrNotaFiscal + "%')" : "") +
							   (stDocumentoSaida == 2 ? " AND A.st_documento_saida <> " + ST_CANCELADO + " AND A.nr_documento_saida IS NOT NULL" : ""), 
							   "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		}catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public static ResultSetMap getMeiosPagamentos(int cdEmpresa){
		GregorianCalendar dtInicial = Util.getDataAtual();
		dtInicial.set(2014, 0, 1);
		GregorianCalendar dtFinal   = Util.getDataAtual();
		dtFinal.set(2014, 0, 31);
		return getMeiosPagamentos(cdEmpresa, dtInicial, dtFinal, null);
	}
	
	public static ResultSetMap getMeiosPagamentos(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		return getMeiosPagamentos(cdEmpresa, dtInicial, dtFinal, null);
	}
	public static ResultSetMap getMeiosPagamentos(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect){
		connect = Conexao.conectar();
		// Inicial
		dtInicial.set(Calendar.HOUR, 0);
		dtInicial.set(Calendar.MINUTE, 0);
		dtInicial.set(Calendar.SECOND, 0);
		// Final
		dtFinal.set(Calendar.HOUR_OF_DAY, 23);
		dtFinal.set(Calendar.MINUTE, 59);
		dtFinal.set(Calendar.SECOND, 59);
		try	{
			PreparedStatement pstmt = connect.prepareStatement(
					  " SELECT C.nm_forma_pagamento, A.tp_documento_saida, CAST(A.dt_documento_saida AS DATE) AS dt_documento_saida, "+
					  "        B.vl_pagamento " +
 					  " FROM alm_documento_saida A " +
 					  " JOIN adm_plano_pagto_documento_saida B ON (A.cd_documento_saida = B.cd_documento_saida) " +
 					  " JOIN adm_forma_pagamento C ON (B.cd_forma_pagamento = C.cd_forma_pagamento) " +
 					  " WHERE A.st_documento_saida  = " + ST_CONCLUIDO +
 					  "   AND A.tp_saida            = " + SAI_VENDA +
 					  "   AND A.cd_empresa          = " + cdEmpresa +
 					  "   AND A.dt_documento_saida BETWEEN ? AND ? " +
 					  "   AND (A.tp_documento_saida = " + TP_CUPOM_FISCAL +
 					  "    OR  A.tp_documento_saida = " + TP_NOTA_FISCAL_VENDA + ")" +
 					  " ORDER BY CAST(A.dt_documento_saida AS DATE) ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));

			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			PreparedStatement pstmtNfe = connect.prepareStatement(
					  " SELECT C.nm_forma_pagamento, A.tp_documento_saida, CAST(A.dt_documento_saida AS DATE) AS dt_documento_saida, "+
					  "        B.vl_pagamento " +
					  " FROM alm_documento_saida A " +
					  " JOIN adm_plano_pagto_documento_saida B ON (A.cd_documento_saida = B.cd_documento_saida) " +
					  " JOIN adm_forma_pagamento C ON (B.cd_forma_pagamento = C.cd_forma_pagamento) " +
					  "	JOIN fsc_nota_fiscal_doc_vinculado D ON(D.cd_documento_saida = A.cd_documento_saida) " +
					  "	JOIN fsc_nota_fiscal E ON (D.cd_nota_fiscal = E.cd_nota_fiscal) " +
					  " WHERE A.st_documento_saida = " + ST_CONCLUIDO +
					  "   AND A.tp_saida           = " + SAI_VENDA +
					  "   AND A.cd_empresa = " + cdEmpresa +
					  "   AND A.dt_documento_saida BETWEEN ? AND ? " +
					  "   AND A.tp_documento_saida = " + TP_NOTA_FISCAL_VENDA + 
					  "	  AND E.st_nota_fiscal = " + NotaFiscalServices.AUTORIZADA +
					  " ORDER BY CAST(A.dt_documento_saida AS DATE) ");
			pstmtNfe.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmtNfe.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsmNfe = new ResultSetMap(pstmtNfe.executeQuery());
			
			
			while(rsmNfe.next()){
				boolean achou = false;
				rsm.beforeFirst();
				while(rsm.next()){
					if(rsm.getString("nm_forma_pagamento").equals(rsmNfe.getString("nm_forma_pagamento")) && rsm.getInt("tp_documento_saida")==TP_NOTA_FISCAL_VENDA && Util.compareDates(rsmNfe.getGregorianCalendar("dt_documento_saida"), rsm.getGregorianCalendar("dt_documento_saida"))==0){
						achou = true;
						rsm.setValueToField("vl_pagamento", rsm.getFloat("vl_pagamento") + rsmNfe.getFloat("vl_pagamento"));
						break;
					}
				}
				if(!achou)
					rsm.addRegister(rsmNfe.getRegister());
			}
			rsm.beforeFirst();
			ArrayList<String> camposOrdenacao = new ArrayList<String>();
			camposOrdenacao.add("DT_DOCUMENTO_SAIDA");
			rsm.orderBy(camposOrdenacao);
			return rsm;
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, ArrayList<String> groupByFields,
			ArrayList<String> orderByFields) {
		return findCompleto(criterios, groupByFields, orderByFields, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, ArrayList<String> groupByFields,
			ArrayList<String> orderByFields, Connection connection)
	{		
		/*
		 * Incluido para acrescentar ao relatório informações de faturamento
		 */
		boolean includeFaturamento  = false;
		int skip=0, limit=0, having = 0, cdFornecedor = 0;
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		String havingComp = "";
		int cdProdutoServico = 0;
		int cdGrupo = 0;
		for(int l=0; l<criterios.size(); l++) {
			includeFaturamento = includeFaturamento || (criterios.get(l).getColumn().toLowerCase().indexOf("forma_pagamento")>=0) ||
			                                           (criterios.get(l).getColumn().toLowerCase().indexOf("plano_pagamento")>=0);
			if(criterios.get(l).getColumn().equalsIgnoreCase("faturamento"))
				includeFaturamento = true;
			else if(criterios.get(l).getColumn().equalsIgnoreCase("cdFornecedor"))
				cdFornecedor = Integer.valueOf(criterios.get(l).getValue());
			else if(criterios.get(l).getColumn().equalsIgnoreCase("nrRegistroInicial"))
				skip = Integer.valueOf(criterios.get(l).getValue());
			else if(criterios.get(l).getColumn().equalsIgnoreCase("nrRegistroFinal"))
				limit = Integer.valueOf(criterios.get(l).getValue());
			else if(criterios.get(l).getColumn().equalsIgnoreCase("having"))	{
				having     = Integer.valueOf(criterios.get(l).getValue());
				havingComp = criterios.get(l).getOperatorComparation();
			}
			else
				crt.add(criterios.get(l));
		}
		limit = limit - skip;

		for(int i=0; i<groupByFields.size(); i++)
			includeFaturamento = includeFaturamento || (groupByFields.get(i).toLowerCase().indexOf("forma_pagamento")>=0) ||
								                       (groupByFields.get(i).toLowerCase().indexOf("plano_pagamento")>=0);
		String fields = "A.cd_documento_saida, A.cd_transportadora, A.cd_empresa, A.cd_cliente, A.dt_documento_saida, A.cd_digitador, S.nm_login, " +
						"A.st_documento_saida, A.nr_documento_saida, A.tp_documento_saida, A.tp_saida, A.tp_frete, tp_movimento_estoque, " +
						"A.cd_vendedor, A.cd_tipo_operacao, K.nm_razao_social AS nm_empresa, A.vl_total_documento, A.vl_acrescimo, A.vl_desconto, " +
						"H.nm_pessoa AS nm_cliente, I.nm_pessoa AS nm_vendedor, J.nm_pessoa AS nm_transportadora, L.nm_tipo_operacao, T.nm_turno " +
						(includeFaturamento ? ", nm_forma_pagamento, nm_plano_pagamento, M.vl_pagamento " : "");
		String groups = "";
		String [] retorno = Util.getFieldsAndGroupBy(groupByFields, fields, groups,
					        " COUNT(DISTINCT A.cd_documento_saida) AS qt_documento, SUM(A.vl_total_documento + A.vl_acrescimo - A.vl_desconto) AS vl_documento " +
					        (includeFaturamento ? ", SUM(vl_pagamento) AS vl_pagamento " : ""));
		fields = retorno[0];
		groups = retorno[1];
		String[] limitFirst = Util.getLimitAndSkip(limit, skip);
		
		ResultSetMap rsm = Search.find("SELECT "+limitFirst[0]+" "+ fields + " " +
									   "FROM alm_documento_saida A " +
									   "LEFT OUTER JOIN grl_pessoa H ON (A.cd_cliente = H.cd_pessoa) " +
									   "LEFT OUTER JOIN grl_pessoa I ON (A.cd_vendedor = I.cd_pessoa) " +
									   "LEFT OUTER JOIN grl_pessoa J ON (A.cd_transportadora = J.cd_pessoa) " +
									   "LEFT OUTER JOIN grl_empresa N ON (A.cd_empresa = N.cd_empresa) " +
									   "LEFT OUTER JOIN grl_pessoa_juridica K ON (N.cd_empresa  = K.cd_pessoa)" +
									   "LEFT OUTER JOIN adm_tipo_operacao   L ON (A.cd_tipo_operacao = L.cd_tipo_operacao) " +
									   "LEFT OUTER JOIN adm_turno           T ON (A.cd_turno = T.cd_turno) "+
									   "LEFT OUTER JOIN seg_usuario         S ON (A.cd_digitador = S.cd_usuario) "+
									   (includeFaturamento ?
											   "LEFT OUTER JOIN adm_plano_pagto_documento_saida M ON (A.cd_documento_saida = M.cd_documento_saida) " +
											   "LEFT OUTER JOIN adm_forma_pagamento O ON (M.cd_forma_pagamento = O.cd_forma_pagamento) " +
											   "LEFT OUTER JOIN adm_plano_pagamento P ON (M.cd_plano_pagamento = P.cd_plano_pagamento) " : "")+
									   "WHERE 1 = 1 "+
									   (cdFornecedor>0 ? " AND EXISTS (SELECT * FROM alm_documento_saida_item DSI, alm_documento_entrada_item DEI, alm_documento_entrada DE " +
									   		             "             WHERE A.cd_documento_saida     = DSI.cd_documento_saida " +
									   		             "               AND DEI.cd_produto_servico   = DSI.cd_produto_servico " +
									   		             "               AND DEI.cd_documento_entrada = DE.cd_documento_entrada " +
									   		             "               AND DE.cd_empresa            = A.cd_empresa " +
									   		             "               AND DE.cd_fornecedor         = "+cdFornecedor+")" : "")+
									   (cdProdutoServico>0 ? " AND NOT EXISTS (SELECT * FROM alm_documento_saida_item DSI " +
									   		             "             WHERE A.cd_documento_saida     = DSI.cd_documento_saida " +
									   		             "               AND DSI.cd_empresa           = A.cd_empresa " +
									   		             "               AND DSI.cd_produto_servico   <> "+cdProdutoServico+")" : "") + 
				   		                (cdGrupo>0 ? " AND EXISTS (SELECT * FROM alm_documento_saida_item DSI, alm_produto_grupo APG " +
									   		             "             WHERE A.cd_documento_saida     = DSI.cd_documento_saida " +
									   		             "               AND DSI.cd_empresa           = A.cd_empresa " +
									   		             "               AND DSI.cd_produto_servico   = APG.cd_produto_servico " +
									   		             "               AND APG.cd_empresa           = A.cd_empresa " +
									   		             "               AND APG.cd_grupo             = "+cdGrupo+")" : ""), 
									   groups+" "+
					                   (having>0 ? " HAVING COUNT(*) "+havingComp+" "+having: " ")+
									   (groupByFields.size()>0?" ORDER BY 1"/*+(groupByFields.size()+2)+" DESC "*/:" ORDER BY dt_documento_saida ")+
									   limitFirst[1], crt, connection!=null ? connection : Conexao.conectar(), connection==null);
		if (orderByFields != null && orderByFields.size()>0)
			rsm.orderBy(orderByFields);
		rsm.beforeFirst();
		return rsm;
	}

	
	public static ResultSetMap findCompletoByNfe(int cdNotaFiscal) {
		return findCompletoByNfe(cdNotaFiscal, null);
	}

	public static ResultSetMap findCompletoByNfe(int cdNotaFiscal, Connection connection)
	{
		
		return Search.find("SELECT A.*, I.nm_pessoa AS nm_vendedor, H.nm_pessoa AS nm_cliente, J.nm_pessoa AS nm_transportadora, " +
	                       "       N.*, K.nr_cnpj, L.nm_tipo_operacao, T.nm_turno, U.nr_serie AS nr_serie_ecf " +
						   "FROM alm_documento_saida A " +
						   "JOIN fsc_nota_fiscal_doc_vinculado  B ON (A.cd_documento_saida = B.cd_documento_saida AND B.cd_nota_fiscal = " +cdNotaFiscal+ ")  " +
						   "LEFT OUTER JOIN grl_pessoa          H ON (A.cd_cliente = H.cd_pessoa) " +
						   "LEFT OUTER JOIN grl_pessoa          I ON (A.cd_vendedor = I.cd_pessoa) " +
						   "LEFT OUTER JOIN grl_pessoa          J ON (A.cd_transportadora = J.cd_pessoa) " +
						   "LEFT OUTER JOIN grl_empresa         N ON (A.cd_empresa = N.cd_empresa) " +
						   "LEFT OUTER JOIN grl_pessoa_juridica K ON (N.cd_empresa  = K.cd_pessoa)" +
						   "LEFT OUTER JOIN adm_tipo_operacao   L ON (A.cd_tipo_operacao = L.cd_tipo_operacao) " +
						   "LEFT OUTER JOIN adm_turno           T ON (A.cd_turno = T.cd_turno) " +
						   "LEFT OUTER JOIN bpm_referencia      U ON (A.cd_referencia_ecf = U.cd_referencia) "+
						   "WHERE 1 = 1 " +
						   "ORDER BY dt_documento_saida ", new ArrayList<ItemComparator>(), connection!=null ? connection : Conexao.conectar());
	}
	
	public static ResultSetMap findCompletoByViagem(int cdViagem, boolean isRemessa) {
		return findCompletoByViagem(cdViagem, isRemessa, null);
	}

	public static ResultSetMap findCompletoByViagem(int cdViagem, boolean isRemessa, Connection connection)
	{
		
		return Search.find("  SELECT A.*, I.nm_pessoa AS nm_vendedor, H.nm_pessoa AS nm_cliente, J.nm_pessoa AS nm_transportadora, " +
	                       "       N.*, K.nr_cnpj, L.nm_tipo_operacao, " +
	                       "	  (SELECT SUM(E.qt_saida * vl_unitario + vl_acrescimo - vl_desconto) FROM alm_documento_saida_item E " +
						   "       WHERE E.cd_documento_saida = A.cd_documento_saida) AS vl_total_liquido " +
						   "	FROM alm_documento_saida A " +
						   "	LEFT OUTER JOIN grl_pessoa          H ON (A.cd_cliente = H.cd_pessoa) " +
						   "	LEFT OUTER JOIN grl_pessoa          I ON (A.cd_vendedor = I.cd_pessoa) " +
						   "	LEFT OUTER JOIN grl_pessoa          J ON (A.cd_transportadora = J.cd_pessoa) " +
						   "	LEFT OUTER JOIN grl_empresa         N ON (A.cd_empresa = N.cd_empresa) " +
						   "	LEFT OUTER JOIN grl_pessoa_juridica K ON (N.cd_empresa  = K.cd_pessoa)" +
						   "	LEFT OUTER JOIN adm_tipo_operacao   L ON (A.cd_tipo_operacao = L.cd_tipo_operacao) " +
						   "  WHERE 1 = 1 AND A.cd_viagem = " + cdViagem + " AND tp_documento_saida " + (isRemessa ? " = " :" <> ") + TP_NOTA_REMESSA + 
						   "  ORDER BY dt_documento_saida ", new ArrayList<ItemComparator>(), connection!=null ? connection : Conexao.conectar());
	}
	
	public static ResultSetMap getResumoPorVendedor(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdFormaPagamentoExcluida, int cdVendedor, int tpDescontoDevolucao){
		return getResumoPorVendedor(cdEmpresa, dtInicial, dtFinal, cdFormaPagamentoExcluida, cdVendedor, tpDescontoDevolucao, 0);
	}
	
	public static ResultSetMap getResumoPorVendedor(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, 
			int cdFormaPagamentoExcluida, int cdVendedor, int tpDescontoDevolucao, int cdTipoOperacao)
	{
		Connection connect = Conexao.conectar();
		// Inicial
		dtInicial.set(Calendar.HOUR_OF_DAY, 0);
		dtInicial.set(Calendar.MINUTE, 0);
		// Final
		dtFinal.set(Calendar.HOUR_OF_DAY, 23);
		dtFinal.set(Calendar.MINUTE, 59);
		try	{
			PreparedStatement pstmt = connect.prepareStatement(
				                       "SELECT A.cd_vendedor, B.nm_pessoa AS nm_vendedor, " +
				                       "       COUNT(*) AS qt_total, SUM(A.vl_total_documento) AS vl_total " +
									   "FROM alm_documento_saida A " +
									   "LEFT OUTER JOIN grl_pessoa B ON (A.cd_vendedor = B.cd_pessoa) " +
									   "WHERE A.cd_empresa          = " +cdEmpresa+
									   "  AND A.st_documento_saida  = "+ST_CONCLUIDO+
									   "  AND A.tp_saida            = "+SAI_VENDA+
									   "  AND A.dt_documento_saida >= ? " +
									   "  AND A.dt_documento_saida <= ? " +
									   ((cdTipoOperacao != 0) ? " AND A.cd_tipo_operacao = " + cdTipoOperacao + " " : "") + 
									   (cdVendedor>0 ? " AND A.cd_vendedor = "+cdVendedor : "")+
									   " GROUP BY A.cd_vendedor, B.nm_pessoa ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			// Desconto de vendas internas
			int cdVinculoColaborador = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_COLABORADOR", 0);
			int cdVinculoVendedor    = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_VENDEDOR", 0);
			PreparedStatement pstmtVendaInterna = connect.prepareStatement(
                    "SELECT COUNT(*) AS qt_total, SUM(A.vl_total_documento + A.vl_acrescimo - A.vl_desconto) AS vl_total " +
					   "FROM alm_documento_saida A " +
					   "WHERE A.cd_empresa          = " +cdEmpresa+
					   "  AND A.st_documento_saida  = "+ST_CONCLUIDO+
					   "  AND A.tp_saida            = "+SAI_VENDA+
					   "  AND A.dt_documento_saida >= ? " +
					   "  AND A.dt_documento_saida <= ? " +
					   "  AND A.cd_vendedor         = ? "+
					   ((cdTipoOperacao != 0) ? " AND A.cd_tipo_operacao = " + cdTipoOperacao + " " : "") + 
					   "  AND EXISTS (SELECT * FROM grl_pessoa_empresa X " +
					   "              WHERE X.cd_pessoa = A.cd_cliente " +
					   "                AND X.cd_empresa = A.cd_empresa" +
					   "                AND X.cd_vinculo IN ("+cdVinculoColaborador+","+cdVinculoVendedor+"))");
			// Forma de pagamento a ser descontado
			int cdFormaPagCreditoDevolucao = ParametroServices.getValorOfParametroAsInteger("CD_FORMA_PAGAMENTO_CREDITO_DEVOLUCAO", 0);
			PreparedStatement pstmtCreditoCliente = connect.prepareStatement(
                       "SELECT COUNT(*) AS qt_total, SUM(B.vl_pagamento) AS vl_total " +
					   "FROM alm_documento_saida A " +
					   "JOIN adm_plano_pagto_documento_saida B ON (A.cd_documento_saida = B.cd_documento_saida " +
					   "                                       AND B.cd_forma_pagamento = "+cdFormaPagCreditoDevolucao+")"+
					   "WHERE A.cd_empresa          = " +cdEmpresa+
					   "  AND A.st_documento_saida  = "+ST_CONCLUIDO+
					   "  AND A.tp_saida            = "+SAI_VENDA+
					   // Exclui as vendas administrativas para não descontar duas vezes
					   "  AND NOT EXISTS (SELECT * FROM grl_pessoa_empresa X " +
					   "                  WHERE X.cd_pessoa = A.cd_cliente " +
					   "                    AND X.cd_empresa = A.cd_empresa" +
					   "                    AND X.cd_vinculo IN ("+cdVinculoColaborador+","+cdVinculoVendedor+")) "+
					   "  AND A.dt_documento_saida >= ? " +
					   "  AND A.dt_documento_saida <= ? " +
					   ((cdTipoOperacao != 0) ? " AND A.cd_tipo_operacao = " + cdTipoOperacao + " " : "") + 
					   "  AND A.cd_vendedor         = ? ");
			
			PreparedStatement pstmtDesconto = connect.prepareStatement(
                       "SELECT COUNT(*) AS qt_total, SUM(B.vl_pagamento) AS vl_total " +
					   "FROM alm_documento_saida A " +
					   "JOIN adm_plano_pagto_documento_saida B ON (A.cd_documento_saida = B.cd_documento_saida " +
					   "                                       AND B.cd_forma_pagamento = "+cdFormaPagamentoExcluida+")"+
					   "WHERE A.cd_empresa          = " +cdEmpresa+
					   "  AND A.st_documento_saida  = "+ST_CONCLUIDO+
					   "  AND A.tp_saida            = "+SAI_VENDA+
					   // Exclui as vendas administrativas para não descontar duas vezes
					   "  AND NOT EXISTS (SELECT * FROM grl_pessoa_empresa X " +
					   "                  WHERE X.cd_pessoa = A.cd_cliente " +
					   "                    AND X.cd_empresa = A.cd_empresa" +
					   "                    AND X.cd_vinculo IN ("+cdVinculoColaborador+","+cdVinculoVendedor+")) "+
					   "  AND A.dt_documento_saida >= ? " +
					   "  AND A.dt_documento_saida <= ? " +
					   ((cdTipoOperacao != 0) ? " AND A.cd_tipo_operacao = " + cdTipoOperacao + " " : "") + 
					   "  AND A.cd_vendedor         = ? ");
			// Devoluções
			pstmt = connect.prepareStatement("SELECT COUNT(*) AS qt_devolucao, " +
					                         "       SUM(A.vl_total_documento + A.vl_acrescimo - A.vl_desconto) AS vl_devolucao " +
					                         "FROM alm_documento_entrada A " +
					                         "WHERE A.cd_empresa            = "+cdEmpresa+
					                         "  AND A.tp_entrada            = "+DocumentoEntradaServices.ENT_DEVOLUCAO+
					                         "  AND A.dt_documento_entrada >= ? " +
					                         "  AND A.dt_documento_entrada <= ? " +
					                         "  AND EXISTS (SELECT * FROM alm_devolucao_item B, alm_documento_saida C " +
					                         "              WHERE A.cd_documento_entrada = B.cd_documento_entrada " +
					                         "                AND B.cd_documento_saida   = C.cd_documento_saida " +
					  					     // Exclui as vendas administrativas para não descontar duas vezes
					  					     "                AND NOT EXISTS (SELECT * FROM grl_pessoa_empresa X " +
										     "                                WHERE X.cd_pessoa  = C.cd_cliente " +
										     "                                  AND X.cd_empresa = C.cd_empresa" +
										     "                                  AND X.cd_vinculo IN ("+cdVinculoColaborador+","+cdVinculoVendedor+")) "+
					                         "                AND C.cd_vendedor = ?)");
			while(rsm.next())	{
				rsm.setValueToField("VL_LIQUIDO", rsm.getFloat("vl_total"));
				// Desconta as devoluções do vendedor original
				if(tpDescontoDevolucao == 0)	{
					pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
					pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
					pstmt.setInt(3, rsm.getInt("cd_vendedor"));
					ResultSet rs = pstmt.executeQuery();
					if(rs.next())	{
						rsm.setValueToField("QT_DEVOLUCAO", rs.getInt("qt_devolucao"));
						rsm.setValueToField("VL_DEVOLUCAO", rs.getFloat("vl_devolucao"));
						rsm.setValueToField("VL_LIQUIDO", rsm.getFloat("vl_liquido")-rs.getFloat("vl_devolucao"));
					}
				}
				// Descontando a forma de pagamento crédito de devolução
				else	{
					pstmtCreditoCliente.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
					pstmtCreditoCliente.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
					pstmtCreditoCliente.setInt(3, rsm.getInt("cd_vendedor"));
					ResultSet rs = pstmtCreditoCliente.executeQuery(); 
					if(rs.next())	{
						rsm.setValueToField("QT_DEVOLUCAO", rs.getInt("qt_total"));
						rsm.setValueToField("VL_DEVOLUCAO", rs.getFloat("vl_total"));
						rsm.setValueToField("VL_LIQUIDO", rsm.getFloat("vl_liquido") - rs.getFloat("vl_total"));
					}
				}
				// Descontando outras formas de pagamento
				if (cdFormaPagamentoExcluida > 0 && cdFormaPagamentoExcluida!=cdFormaPagCreditoDevolucao)	{
					pstmtDesconto.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
					pstmtDesconto.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
					pstmtDesconto.setInt(3, rsm.getInt("cd_vendedor"));
					ResultSet rs = pstmtDesconto.executeQuery(); 
					if(rs.next())	{
						rsm.setValueToField("QT_DESCONTO", rs.getInt("qt_total"));
						rsm.setValueToField("VL_DESCONTO", rs.getFloat("vl_total"));
						rsm.setValueToField("VL_LIQUIDO", rsm.getFloat("vl_liquido") - rs.getFloat("vl_total"));
					}
				}
				// Vendas Internas
				pstmtVendaInterna.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
				pstmtVendaInterna.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
				pstmtVendaInterna.setInt(3, rsm.getInt("cd_vendedor"));
				ResultSet rs = pstmtVendaInterna.executeQuery(); 
				if(rs.next())	{
					rsm.setValueToField("QT_VENDA_INTERNA", rs.getInt("qt_total"));
					rsm.setValueToField("VL_VENDA_INTERNA", rs.getFloat("vl_total"));
					rsm.setValueToField("VL_LIQUIDO", rsm.getFloat("vl_liquido") - rs.getFloat("vl_total"));
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getForPrint(int cdDocumentoSaida) {
		return getForPrint(cdDocumentoSaida, null);
	}

	public static ResultSetMap getForPrint(int cdDocumentoSaida, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_pessoa AS nm_cliente, " +
					"C.nm_pessoa AS nm_transportadora, E.nr_cnpj AS nr_cnpj_transportadora, L.nm_razao_social, " +
					"B.nr_telefone1 AS nr_telefone_cliente1, L.nr_inscricao_estadual AS nr_inscricao_estadual_cliente, " +
				    "D.nm_natureza_operacao, D.nr_codigo_fiscal, F.nm_pessoa AS nm_vendedor, G.nm_tipo_operacao, " +
				    "E.nr_inscricao_municipal AS nr_inscricao_municipal_cliente, " +
				    "I.nm_pessoa AS nm_digitador, B.gn_pessoa AS gn_pessoa_cliente, J.nr_cpf AS nr_cpf_cliente, L.nr_cnpj AS nr_cnpj_cliente, " +
				    "P.ds_endereco AS ds_endereco_cliente, P.nm_logradouro AS nm_logradouro_cliente, P.nm_bairro AS nm_bairro_cliente, " +
				    "P.nr_cep AS nr_cep_cliente, P.nr_endereco AS nr_endereco_cliente, P.nm_complemento AS nm_complemento_cliente, " +
				    "P.nm_ponto_referencia AS nm_ponto_referencia_cliente, Q.nm_tipo_logradouro AS nm_tipo_logradouro_cliente, " +
				    "Q.sg_tipo_logradouro AS sg_tipo_logradouro_cliente, R.nm_logradouro AS nm_logradouro_end_cliente, " +
				    "S.nm_tipo_logradouro AS nm_tipo_logradouro_end_cliente, S.sg_tipo_logradouro AS sg_tipo_logradouro_end_cliente, " +
				    "T.nm_bairro AS nm_bairro_end_cliente, U.nm_cidade AS nm_cidade_cliente, V.nm_estado AS nm_estado_cliente, " +
				    "V.sg_estado AS sg_estado_cliente, C.gn_pessoa AS gn_pessoa_transp, M.nr_cpf AS nr_cpf_transp, " +
				    "E.nr_cnpj AS nr_cnpj_transp, P1.ds_endereco AS ds_endereco_transp, P1.nm_logradouro AS nm_logradouro_transp, " +
				    "P1.nm_bairro AS nm_bairro_transp, E.nr_inscricao_estadual AS nr_inscricao_estadual_transp, " +
				    "P1.nr_cep AS nr_cep_transp, P1.nr_endereco AS nr_endereco_transp, P1.nm_complemento AS nm_complemento_transp, " +
				    "P1.nm_ponto_referencia AS nm_ponto_referencia_transp, Q1.nm_tipo_logradouro AS nm_tipo_logradouro_transp, " +
				    "Q1.sg_tipo_logradouro AS sg_tipo_logradouro_transp, R1.nm_logradouro AS nm_logradouro_end_transp, " +
				    "S1.nm_tipo_logradouro AS nm_tipo_logradouro_end_transp, S1.sg_tipo_logradouro AS sg_tipo_logradouro_end_transp, " +
				    "T1.nm_bairro AS nm_bairro_end_transp, U1.nm_cidade AS nm_cidade_transp, V1.nm_estado AS nm_estado_transp, " +
				    "V1.sg_estado AS sg_estado_transp, " +
				    "(SELECT SUM(E.qt_saida * vl_unitario + vl_acrescimo - vl_desconto) FROM alm_documento_saida_item E " +
				    " WHERE E.cd_documento_saida = A.cd_documento_saida) AS vl_total_itens, " +
				    "(SELECT SUM(F.vl_desconto) " +
				    " FROM alm_documento_saida_item F " +
				    " WHERE F.cd_documento_saida = A.cd_documento_saida) AS vl_descontos_itens, " +
				    "(SELECT SUM(G.vl_acrescimo) " +
				    " FROM alm_documento_saida_item G " +
				    " WHERE G.cd_documento_saida = A.cd_documento_saida) AS vl_acrescimos_itens, " +
				    "(SELECT SUM(E.qt_saida * vl_unitario + vl_acrescimo - vl_desconto) " +
				    " FROM alm_documento_saida_item E, grl_produto_servico F " +
				    " WHERE E.cd_produto_servico = F.cd_produto_servico " +
				    "   AND E.cd_documento_saida = A.cd_documento_saida " +
				    "   AND F.tp_produto_servico = " + ProdutoServicoServices.TP_PRODUTO + ") AS vl_total_produtos, " +
				    "(SELECT SUM(E.qt_saida * vl_unitario + vl_acrescimo - vl_desconto) " +
				    " FROM alm_documento_saida_item E, grl_produto_servico F " +
				    " WHERE E.cd_produto_servico = F.cd_produto_servico " +
				    "   AND E.cd_documento_saida = A.cd_documento_saida " +
				    "   AND F.tp_produto_servico = " + ProdutoServicoServices.TP_SERVICO + ") AS vl_total_servicos " +
				    "FROM alm_documento_saida A " +
				    "LEFT OUTER JOIN grl_pessoa B ON (A.cd_cliente = B.cd_pessoa) " +
				    "LEFT OUTER JOIN grl_pessoa_fisica J ON (B.cd_pessoa = J.cd_pessoa) " +
				    "LEFT OUTER JOIN grl_pessoa_juridica L ON (B.cd_pessoa = L.cd_pessoa)" +
				    "LEFT OUTER JOIN grl_pessoa_endereco P ON (B.cd_pessoa = P.cd_pessoa AND P.lg_principal = 1 AND ((" +
				    "										  EXISTS (SELECT P1.cd_endereco FROM grl_pessoa_endereco P1 " +
				    "												  WHERE P1.cd_pessoa = B.cd_pessoa " +
				    "													AND P1.lg_principal = 1)) OR " +
				    "										  P.cd_endereco = (SELECT MAX(P1.cd_endereco) FROM grl_pessoa_endereco P1 " +
				    "														   WHERE P1.cd_pessoa = B.cd_pessoa))) " +
				    "LEFT OUTER JOIN grl_tipo_logradouro Q ON (P.cd_tipo_logradouro = Q.cd_tipo_logradouro) " +
				    "LEFT OUTER JOIN grl_logradouro R ON (P.cd_logradouro = R.cd_logradouro) " +
				    "LEFT OUTER JOIN grl_tipo_logradouro S ON (R.cd_tipo_logradouro = S.cd_tipo_logradouro) " +
				    "LEFT OUTER JOIN grl_bairro T ON (P.cd_bairro = T.cd_bairro) " +
				    "LEFT OUTER JOIN grl_cidade U ON (P.cd_cidade = U.cd_cidade) " +
				    "LEFT OUTER JOIN grl_estado V ON (U.cd_estado = V.cd_estado) " +
				    "LEFT OUTER JOIN grl_pessoa C ON (A.cd_transportadora = C.cd_pessoa) " +
				    "LEFT OUTER JOIN adm_natureza_operacao D ON (A.cd_natureza_operacao = D.cd_natureza_operacao) " +
				    "LEFT OUTER JOIN grl_pessoa_juridica E ON (C.cd_pessoa = E.cd_pessoa) " +
				    "LEFT OUTER JOIN grl_pessoa_fisica M ON (C.cd_pessoa = M.cd_pessoa) " +
				    "LEFT OUTER JOIN grl_pessoa_endereco P1 ON (C.cd_pessoa = P1.cd_pessoa AND P1.lg_principal = 1 AND ((" +
				    "										  EXISTS (SELECT P2.cd_endereco FROM grl_pessoa_endereco P2 " +
				    "												  WHERE P2.cd_pessoa = C.cd_pessoa " +
				    "													AND P2.lg_principal = 1)) OR " +
				    "										  P1.cd_endereco = (SELECT MAX(P2.cd_endereco) FROM grl_pessoa_endereco P2 " +
				    "														   WHERE P2.cd_pessoa = C.cd_pessoa))) " +
				    "LEFT OUTER JOIN grl_tipo_logradouro Q1 ON (P1.cd_tipo_logradouro = Q1.cd_tipo_logradouro) " +
				    "LEFT OUTER JOIN grl_logradouro R1 ON (P1.cd_logradouro = R1.cd_logradouro) " +
				    "LEFT OUTER JOIN grl_tipo_logradouro S1 ON (R1.cd_tipo_logradouro = S1.cd_tipo_logradouro) " +
				    "LEFT OUTER JOIN grl_bairro T1 ON (P1.cd_bairro = T1.cd_bairro) " +
				    "LEFT OUTER JOIN grl_cidade U1 ON (P1.cd_cidade = U1.cd_cidade) " +
				    "LEFT OUTER JOIN grl_estado V1 ON (U1.cd_estado = V1.cd_estado) " +
				    "LEFT OUTER JOIN grl_pessoa 		 	F ON (A.cd_vendedor = F.cd_pessoa) " +
				    "LEFT OUTER JOIN adm_tipo_operacao 	G ON (A.cd_tipo_operacao = G.cd_tipo_operacao) " +
				    "LEFT OUTER JOIN seg_usuario H ON (A.cd_digitador = H.cd_usuario) " +
				    "LEFT OUTER JOIN grl_pessoa I ON (H.cd_pessoa = I.cd_pessoa) " +
				    "WHERE A.cd_documento_saida = " + cdDocumentoSaida);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while (rsm!=null && rsm.next()) {
				HashMap<String, Object> reg = rsm.getRegister();
				reg.put("FIELD_FORM_NR_PLACA_VEICULO", rsm.getInt("cd_transportadora")<=0 ? "" : rsm.getString("nr_placa_veiculo"));
				reg.put("FIELD_FORM_SG_PLACA_VEICULO", rsm.getInt("cd_transportadora")<=0 ? "" : rsm.getString("sg_placa_veiculo"));
				reg.put("FIELD_FORM_QT_VOLUMES", rsm.getInt("cd_transportadora")<=0 ? "" : Util.formatNumber(rsm.getFloat("qt_volumes")));
				reg.put("FIELD_FORM_DS_ESPECIE_VOLUMES", rsm.getInt("cd_transportadora")<=0 ? "" : rsm.getString("ds_especie_volumes"));
				reg.put("FIELD_FORM_DS_MARCA_VOLUMES", rsm.getInt("cd_transportadora")<=0 ? "" : rsm.getString("ds_marca_volumes"));
				reg.put("FIELD_FORM_NR_VOLUMES", rsm.getInt("cd_transportadora")<=0 ? "" : rsm.getString("nr_volumes"));
				reg.put("FIELD_FORM_VL_PESO_LIQUIDO", rsm.getInt("cd_transportadora")<=0 ? "" : Util.formatNumber(rsm.getFloat("vl_peso_liquido")));
				reg.put("FIELD_FORM_VL_PESO_BRUTO", rsm.getInt("cd_transportadora")<=0 ? "" : Util.formatNumber(rsm.getFloat("vl_peso_bruto")));
				reg.put("SG_TP_FRETE", rsm.getInt("cd_transportadora") <= 0 ? "" : tiposFrete[rsm.getInt("tp_frete")]);
				reg.put("NR_DOC_CLIENTE", "");
				try {
					reg.put("NR_DOC_CLIENTE", rsm.getInt("gn_pessoa_cliente")==PessoaServices.TP_FISICA ?
							Util.formatCpf(rsm.getString("nr_cpf_cliente")) : Util.formatCnpj(rsm.getString("nr_cnpj_cliente")));
				}
				catch(Exception e) { e.printStackTrace(System.out); }
				reg.put("NR_DOC_TRANSP", "");
				try {
					reg.put("NR_DOC_TRANSP", rsm.getInt("gn_pessoa_transp")==PessoaServices.TP_FISICA ?
							Util.formatCpf(rsm.getString("nr_cpf_transp")) : Util.formatCnpj(rsm.getString("nr_cnpj_transp")));
				}
				catch(Exception e) { e.printStackTrace(System.out); }
				int tpEnderecamento = ParametroServices.getValorOfParametroAsInteger("TP_ENDERECAMENTO", PessoaEnderecoServices.TP_DIGITAVEL,
						0, connection);
				String nmLogradouroFormat = tpEnderecamento==PessoaEnderecoServices.TP_DIGITAVEL ?
						((rsm.getString("nm_tipo_logradouro_cliente")==null ? "" : rsm.getString("nm_tipo_logradouro_cliente").trim()) + " " +
								(rsm.getString("nm_logradouro_cliente")==null ? "" : rsm.getString("nm_logradouro_cliente").trim())) :
						((rsm.getString("nm_tipo_logradouro_end_cliente")==null ? "" : rsm.getString("nm_tipo_logradouro_end_cliente").trim() + " " +
								rsm.getStreamToString("nm_logradouro_end_cliente")==null ? "" : rsm.getStreamToString("nm_logradouro_end_cliente").trim()));
				reg.put("NM_LOGRADOURO_FORMAT_CLIENTE", nmLogradouroFormat);
				nmLogradouroFormat = tpEnderecamento==PessoaEnderecoServices.TP_DIGITAVEL ?
						((rsm.getString("nm_tipo_logradouro_transp")==null ? "" : rsm.getString("nm_tipo_logradouro_transp").trim()) + " " +
								(rsm.getString("nm_logradouro_transp")==null ? "" : rsm.getString("nm_logradouro_transp").trim())) :
						((rsm.getString("nm_tipo_logradouro_end_transp")==null ? "" : rsm.getString("nm_tipo_logradouro_end_transp").trim() + " " +
								rsm.getStreamToString("nm_logradouro_end_transp")==null ? "" : rsm.getStreamToString("nm_logradouro_end_transp").trim()));
				reg.put("NM_LOGRADOURO_FORMAT_TRANSP", nmLogradouroFormat);
				reg.put("HR_SAIDA", Util.formatDateTime(rsm.getGregorianCalendar("dt_documento_saida"), "HH:mm", ""));

				ResultSetMap rsmTributos = TributoDAO.getAll(connection);
				String[] fieldsAdd = {"VL_BASE_CALCULO_", "VL_TRIBUTO_", "VL_BASE_CALCULO_SERVICOS_", "VL_TRIBUTO_SERVICOS_",
						"FIELD_FORM_VL_BASE_CALCULO_SERVICOS_", "FIELD_FORM_VL_TRIBUTO_SERVICOS_"};
				while (rsmTributos.next()) {
					for (int i=0; i<fieldsAdd.length; i++) {
						reg.put(fieldsAdd[i] + rsmTributos.getString("id_tributo").replaceAll("[/]", ""), i<4 ? 0 : "");
					}
				}

				/* totais de tributos por tipo de tributo */
				pstmt = connection.prepareStatement("SELECT A.cd_tributo, B.id_tributo, SUM(vl_base_calculo) AS vl_base_calculo, " +
						"SUM(pr_aliquota/100 * vl_base_calculo) AS vl_tributo " +
						"FROM adm_saida_item_aliquota A, adm_tributo B " +
						"WHERE A.cd_tributo = B.cd_tributo " +
						"  AND A.cd_documento_saida = ? " +
						"GROUP BY A.cd_tributo, B.id_tributo");
				pstmt.setInt(1, cdDocumentoSaida);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					reg.put("VL_BASE_CALCULO_" + rs.getString("id_tributo"), rs.getFloat("vl_base_calculo"));
					reg.put("VL_TRIBUTO_" + rs.getString("id_tributo"), rs.getFloat("vl_tributo"));
				}

				/* totais de tributos por tipo de tributo sobre serviços */
				pstmt = connection.prepareStatement("SELECT A.cd_tributo, B.id_tributo, SUM(vl_base_calculo) AS vl_base_calculo, " +
						"SUM(pr_aliquota/100 * vl_base_calculo) AS vl_tributo " +
						"FROM adm_saida_item_aliquota A, adm_tributo B, grl_produto_servico C " +
						"WHERE A.cd_tributo = B.cd_tributo " +
						"  AND A.cd_produto_servico = C.cd_produto_servico " +
						"  AND C.tp_produto_servico = ?" +
						"  AND A.cd_documento_saida = ? " +
						"GROUP BY A.cd_tributo, B.id_tributo");
				pstmt.setInt(1, ProdutoServicoServices.TP_SERVICO);
				pstmt.setInt(2, cdDocumentoSaida);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					reg.put("VL_BASE_CALCULO_SERVICOS_" + rs.getString("id_tributo"), rs.getFloat("vl_base_calculo"));
					reg.put("VL_TRIBUTO_SERVICOS_" + rs.getString("id_tributo"), rs.getFloat("vl_tributo"));
					reg.put("FIELD_FORM_VL_BASE_CALCULO_SERVICOS_" + rs.getString("id_tributo"), Util.formatNumber(rs.getFloat("vl_base_calculo")));
					reg.put("FIELD_FORM_VL_TRIBUTO_SERVICOS_" + rs.getString("id_tributo"), Util.formatNumber(rs.getFloat("vl_tributo")));
				}
				reg.put("VL_TOTAL_DOC_PROCESS", rsm.getFloat("vl_total_itens") + rsm.getFloat("vl_acrescimo") -
						rsm.getFloat("vl_desconto") + rsm.getFloat("vl_frete") + rsm.getFloat("vl_frete"));

				/* descricao de faturamento da nota */
				ResultSetMap rsmContas = getAllContasReceber(cdDocumentoSaida, connection);
				String txtFaturamento = "";
				for (int i=0; rsmContas!=null && rsmContas.next(); i++) {
					txtFaturamento += (i>0 ? "; " : "") + (i+1) + "ª Parc.: R$ " + Util.formatNumber(rsmContas.getFloat("vl_conta") +
							rsmContas.getFloat("vl_acrescimo") - rsmContas.getFloat("vl_abatimento")) + " Venc. " +
							Util.formatDateTime(rsmContas.getGregorianCalendar("dt_vencimento"), "dd/MM/yyyy", "");
				}
				reg.put("TXT_FATURAMENTO", txtFaturamento);
				reg.put("NM_RAZAO_OR_PESSOA", rsm.getInt("gn_pessoa_cliente")==PessoaServices.TP_JURIDICA && rsm.getString("nm_razao_social")!=null &&
						!rsm.getString("nm_razao_social").equals("") ? rsm.getString("nm_razao_social") : rsm.getString("nm_cliente"));
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
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllItensForPrint(int cdDocumentoSaida) {
		return getAllItensForPrint(cdDocumentoSaida, -1, null);
	}

	public static ResultSetMap getAllItensForPrint(int cdDocumentoSaida, int tpProdutoServico) {
		return getAllItensForPrint(cdDocumentoSaida, tpProdutoServico, null);
	}

	public static ResultSetMap getAllItensForPrint(int cdDocumentoSaida, int tpProdutoServico, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.id_reduzido, C.nm_produto_servico, " +
					"C.id_produto_servico, D.nm_classificacao_fiscal, D.id_classificacao_fiscal, E.nm_unidade_medida, " +
					"E.sg_unidade_medida, (A.vl_unitario * A.qt_saida + A.vl_acrescimo - A.vl_desconto) AS vl_liquido " +
					"FROM alm_documento_saida_item A " +
					"LEFT OUTER JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico = B.cd_produto_servico AND " +
					"												   A.cd_empresa = B.cd_empresa) " +
					"LEFT OUTER JOIN grl_produto_servico C ON (B.cd_produto_servico = C.cd_produto_servico) " +
					"LEFT OUTER JOIN adm_classificacao_fiscal D ON (C.cd_classificacao_fiscal = D.cd_classificacao_fiscal) " +
					"LEFT OUTER JOIN grl_unidade_medida E ON (A.cd_unidade_medida = E.cd_unidade_medida) " +
					"WHERE 1=1 " +
					(tpProdutoServico!=-1 ? "  AND tp_produto_servico = " + tpProdutoServico + " " : "") +
					"  AND cd_documento_saida = " + cdDocumentoSaida);
			String[] columnsBase = {"VL_BASE_CALCULO_", "PR_ALIQUOTA_", "ST_TRIBUTARIA_", "SG_ST_TRIBUTARIA_"};
			ArrayList<HashMap<String, Object>> aditionalColumns = new ArrayList<HashMap<String, Object>>();
			ResultSetMap rsmTributos = TributoDAO.getAll(connection);
			while (rsmTributos.next())
				for (int i=0; i<columnsBase.length; i++) {
					HashMap<String, Object> column = new HashMap<String, Object>();
					column.put("name", columnsBase[i] + rsmTributos.getString("id_tributo").replaceAll("[/]", ""));
					aditionalColumns.add(column);
				}
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while (rsm!=null && rsm.next()) {
				HashMap<String, Object> reg = rsm.getRegister();
				rsmTributos.beforeFirst();
				while (rsmTributos.next()) {
					for (int i=0; i<columnsBase.length; i++)
						reg.put(columnsBase[i] + rsmTributos.getString("id_tributo").replaceAll("[/]", ""), i<2 ? 0 : "");
				}
				/* totais de tributos por tipo de tributo */
				pstmt = connection.prepareStatement("SELECT A.cd_tributo, B.id_tributo, C.st_tributaria, A.pr_aliquota, A.vl_base_calculo  " +
						"FROM adm_saida_item_aliquota A, adm_tributo B, adm_tributo_aliquota C " +
						"WHERE A.cd_tributo = B.cd_tributo " +
						"  AND A.cd_tributo_aliquota = C.cd_tributo_aliquota " +
						"  AND A.cd_tributo = C.cd_tributo " +
						"  AND A.cd_documento_saida = ? ");
				pstmt.setInt(1, cdDocumentoSaida);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					reg.put("VL_BASE_CALCULO_" + rs.getString("id_tributo"), rs.getFloat("vl_base_calculo"));
					reg.put("PR_ALIQUOTA_" + rs.getString("id_tributo"), rs.getFloat("pr_aliquota"));
					reg.put("ST_TRIBUTARIA_" + rs.getString("id_tributo"), rs.getInt("st_tributaria"));
					reg.put("SG_ST_TRIBUTARIA_" + rs.getString("id_tributo"), rs.getInt("st_tributaria")>4 ? "" :
						TributoAliquotaServices.situacaoTributariaEcf[rs.getInt("st_tributaria")]);
				}
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
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap findTranferencias(ArrayList<ItemComparator> criterios) {
		return findTranferencias(criterios, null);
	}

	public static ResultSetMap findTranferencias(ArrayList<ItemComparator> criterios, Connection connect) {
		for(int i=0; criterios!=null && i<criterios.size(); i++){
			if(criterios.get(i).getColumn().toUpperCase().indexOf("DT_DOCUMENTO_SAIDA")>=0)
				criterios.get(i).setColumn("CAST(dt_documento_saida AS DATE)");
		}
		return Search.find("SELECT A.*, B.cd_local_armazenamento AS cd_local_armazenamento_origem, " +
				           "       B.nm_local_armazenamento AS nm_local_armazenamento_origem, " +
				           "       E.nm_local_armazenamento AS nm_local_armazenamento_destino, " +
						   "E.cd_local_armazenamento AS cd_local_armazenamento_destino " +
						   "FROM alm_documento_saida A " +
						   "LEFT OUTER JOIN alm_local_armazenamento B ON (B.cd_local_armazenamento = (SELECT MAX(C.cd_local_armazenamento) " +
						   "																		   FROM alm_saida_local_item C " +
						   "																		   WHERE C.cd_documento_saida = A.cd_documento_saida)) " +
						   "LEFT OUTER JOIN alm_documento_entrada D ON (A.cd_documento_saida = D.cd_documento_saida_origem) " +
						   "LEFT OUTER JOIN alm_local_armazenamento E ON (E.cd_local_armazenamento = (SELECT MAX(F.cd_local_armazenamento) " +
						   "																		   FROM alm_entrada_local_item F " +
						   "																		   WHERE F.cd_documento_entrada = D.cd_documento_entrada)) " +
						   "WHERE A.tp_saida = " + SAI_TRANSFERENCIA, criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getAllItens(int cdDocumentoSaida) {
		return getAllItens(cdDocumentoSaida, null);
	}
	
	public static ResultSetMap getAllItens(int cdDocumentoSaida, int isPDV) {
		return getAllItens(cdDocumentoSaida, false, isPDV, null);
	}
	
	public static ResultSetMap getAllItens(int cdDocumentoSaida, boolean hasTributos) {
		return getAllItens(cdDocumentoSaida, hasTributos, 0, null);
	}

	public static ResultSetMap getAllItens(int cdDocumentoSaida, Connection connect) {
		return getAllItens(cdDocumentoSaida, false, 0, connect);
	}
	
	public static ResultSetMap getAllItens(int cdDocumentoSaida, boolean hasTributos, Connection connect) {
		return getAllItens(cdDocumentoSaida, hasTributos, 0, connect);
	}
	
	public static ResultSetMap getAllItens(int cdDocumentoSaida, boolean hasTributos, int isPDV, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
				
			int cdTributoICMS    = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0, 0, connect);
			int cdTributoIPI 	 = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_IPI", 0, 0, connect);
			int cdTributoII      = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_II", 0, 0, connect);
			int cdTributoPIS 	 = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0, 0, connect);
			int cdTributoCOFINS  = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0, 0, connect);
			
			//Busca documento para pesquisar parametro no relatório
			ResultSetMap documentoSaida = get(cdDocumentoSaida, connect);
			int relatorioGrupo          = 0;
			if(documentoSaida.next())
				relatorioGrupo          = ParametroServices.getValorOfParametroAsInteger("LG_RELATORIO_ORDENADO_GRUPO", 0, documentoSaida.getInt("cd_empresa"));			
			//			
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement(
					 "SELECT A.*, C.nm_produto_servico, C.txt_produto_servico, C.tp_produto_servico, C.id_produto_servico, I.txt_observacao, " +
					 "       C.id_produto_servico, C.sg_produto_servico, B.st_produto_empresa, B.id_reduzido, B.qt_precisao_custo, C.nr_referencia, C.cd_ncm," +
					 "       C.cd_classificacao_fiscal, E.nm_classificacao_fiscal, " +
					 "       G.sg_unidade_medida, G.nm_unidade_medida, G.nr_precisao_medida, txt_especificacao, txt_dado_tecnico, " +
					 "       H.nm_pessoa AS nm_fabricante, C.id_produto_servico, B.qt_precisao_unidade, " +
					 "		J.nr_codigo_fiscal, K.nr_codigo_fiscal AS nr_codigo_fiscal_item, " +
					 "		K.nm_natureza_operacao AS nm_natureza_operacao_item, K.cd_natureza_operacao AS cd_natureza_operacao_item " +
					 "FROM alm_documento_saida_item    A " +
					 "JOIN grl_produto_servico_empresa         B ON (A.cd_produto_servico = B.cd_produto_servico AND A.cd_empresa = B.cd_empresa) " +
					 "JOIN grl_produto_servico                 C ON (B.cd_produto_servico = C.cd_produto_servico) " +
					 "LEFT OUTER JOIN grl_produto              D ON (C.cd_produto_servico = D.cd_produto_servico) " +
					 "LEFT OUTER JOIN adm_classificacao_fiscal E ON (C.cd_classificacao_fiscal = E.cd_classificacao_fiscal) " +
					 "LEFT OUTER JOIN grl_unidade_medida 	   G ON (A.cd_unidade_medida = G.cd_unidade_medida) " +
					 "LEFT OUTER JOIN grl_pessoa               H ON (C.cd_fabricante = H.cd_pessoa) " +
					 "JOIN alm_documento_saida                 I ON (A.cd_documento_saida = I.cd_documento_saida) " + 
					 "LEFT OUTER JOIN adm_natureza_operacao    J ON (J.cd_natureza_operacao = I.cd_natureza_operacao) " + 
					 "LEFT OUTER JOIN adm_natureza_operacao    K ON (K.cd_natureza_operacao = A.cd_natureza_operacao) " + 
					 "LEFT OUTER JOIN alm_produto_grupo    	   L ON (L.cd_produto_servico = B.cd_produto_servico AND L.cd_empresa = B.cd_empresa AND L.lg_principal = 1) " +
					 "LEFT OUTER JOIN alm_grupo                N ON (N.cd_grupo = L.cd_grupo) " +
					 "WHERE A.cd_documento_saida = ? " 		
					 +(isPDV!=1?"":( relatorioGrupo == 0 ? "" :"  ORDER BY CAST (N.id_grupo AS INTEGER), CAST(REPLACE(id_reduzido, \'-\', \'\') AS INTEGER)" ))
//					+( relatorioGrupo == 0 ? "" :"" )
					);		
			pstmt.setInt(1, cdDocumentoSaida);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next() && hasTributos){
				//Total Tributos
				ResultSetMap rsm2 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_total_tributos, SUM(I.vl_base_calculo) AS vl_total_base_calculo FROM adm_saida_item_aliquota I " +
																		 "        WHERE I.cd_item =  " + rsm.getInt("cd_item") +
																		 "			AND I.cd_produto_servico = " +rsm.getInt("cd_produto_servico") +
																		 "          AND I.cd_documento_saida = " +rsm.getInt("cd_documento_saida") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa")).executeQuery());
				while(rsm2.next()){
					rsm.setValueToField("vl_total_tributos", rsm2.getFloat("vl_total_tributos"));
					rsm.setValueToField("vl_total_base_calculo", rsm2.getFloat("vl_total_base_calculo"));
				}
				
				//Total Tributos do Documento
				ResultSetMap rsm3 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_total_tributos_documento, SUM(I.vl_base_calculo) AS vl_total_base_calculo_documento FROM adm_saida_item_aliquota I " +
																		 "        WHERE I.cd_documento_saida = " +rsm.getInt("cd_documento_saida") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa")).executeQuery());
				while(rsm3.next()){
					rsm.setValueToField("vl_total_tributos_documento", rsm3.getFloat("vl_total_tributos_documento"));
					rsm.setValueToField("vl_total_base_calculo_documento", rsm3.getFloat("vl_total_base_calculo_documento"));
				}
				
				//Total Tributo ICMS
				ResultSetMap rsm4 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_icms, I.pr_aliquota, SUM(I.vl_base_calculo) AS vl_base_icms, P.st_tributaria, Q.nr_situacao_tributaria, Q.lg_substituicao FROM adm_saida_item_aliquota I " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " + 
																		 "        WHERE I.cd_item =  " + rsm.getInt("cd_item") +
																		 "			AND I.cd_produto_servico = " +rsm.getInt("cd_produto_servico") +
																		 "          AND I.cd_documento_saida = " +rsm.getInt("cd_documento_saida") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa") + 
																		 "          AND P.cd_tributo           = "+cdTributoICMS + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria, I.pr_aliquota, Q.lg_substituicao").executeQuery());
				if(rsm4.next()){
					rsm.setValueToField("vl_icms", rsm4.getFloat("vl_icms"));
					rsm.setValueToField("vl_base_icms", rsm4.getFloat("vl_base_icms"));
					rsm.setValueToField("pr_icms", rsm4.getFloat("pr_aliquota"));
					rsm.setValueToField("st_tributaria", rsm4.getInt("st_tributaria"));
					rsm.setValueToField("st_tributaria_icms", rsm4.getInt("st_tributaria"));
					rsm.setValueToField("lg_substituicao_icms", rsm4.getInt("lg_substituicao"));
					rsm.setValueToField("nr_situacao_tributaria", rsm4.getString("nr_situacao_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria_icms", rsm4.getString("nr_situacao_tributaria"));
				}
				if(rsm4.next()){
					rsm.setValueToField("ErroAliq", 1);
				}
				
				//Total Tributo ICMS Documento
				ResultSetMap rsm5 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_icms, SUM(I.vl_base_calculo) AS vl_base_icms, P.st_tributaria, Q.nr_situacao_tributaria FROM adm_saida_item_aliquota I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_documento_saida = " +rsm.getInt("cd_documento_saida") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa") + 
																		 "          AND P.cd_tributo           = "+cdTributoICMS + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria").executeQuery());
				
				while(rsm5.next()){
					if(rsm5.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
						rsm.setValueToField("vl_icms_documento", rsm5.getFloat("vl_icms"));
						rsm.setValueToField("vl_base_icms_documento", rsm5.getFloat("vl_base_icms"));
						rsm.setValueToField("st_tributaria_documento", rsm5.getInt("st_tributaria"));
					}
					if(rsm5.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
						rsm.setValueToField("vl_icms_substituto_documento", rsm5.getFloat("vl_icms"));
						rsm.setValueToField("vl_base_icms_substituto_documento", rsm5.getFloat("vl_base_icms"));
						rsm.setValueToField("st_tributaria_substituto_documento", rsm5.getInt("st_tributaria"));
					}
				}
				
				//Total Tributo IPI
				ResultSetMap rsm6 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_ipi, I.pr_aliquota, SUM(I.vl_base_calculo) AS vl_base_ipi, P.st_tributaria, Q.nr_situacao_tributaria FROM adm_saida_item_aliquota I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_item =  " + rsm.getInt("cd_item") +
																		 "			AND I.cd_produto_servico = " +rsm.getInt("cd_produto_servico") +
																		 "          AND I.cd_documento_saida = " +rsm.getInt("cd_documento_saida") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa") + 
																		 "          AND P.cd_tributo           = "+cdTributoIPI + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria, I.pr_aliquota").executeQuery());
				if(rsm6.next()){
					rsm.setValueToField("vl_ipi", rsm6.getFloat("vl_ipi"));
					rsm.setValueToField("vl_base_ipi", rsm6.getFloat("vl_base_ipi"));
					rsm.setValueToField("pr_ipi", rsm6.getFloat("pr_aliquota"));
					rsm.setValueToField("st_tributaria", rsm6.getInt("st_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria", rsm6.getString("nr_situacao_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria_ipi", rsm6.getString("nr_situacao_tributaria"));
				}
				if(rsm6.next()){
					rsm.setValueToField("ErroAliq", 2);
				}
				
				//Total Tributo IPI do Documento
				ResultSetMap rsm7 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_ipi, SUM(I.vl_base_calculo) AS vl_base_ipi, P.st_tributaria, Q.nr_situacao_tributaria FROM adm_saida_item_aliquota I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_documento_saida = " +rsm.getInt("cd_documento_saida") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa") + 
																		 "			AND P.cd_tributo           = "+cdTributoIPI + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria").executeQuery());
				while(rsm7.next()){
					if(rsm7.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
						rsm.setValueToField("vl_ipi_documento", rsm7.getFloat("vl_ipi"));
						rsm.setValueToField("vl_base_ipi_documento", rsm7.getFloat("vl_base_ipi"));
						rsm.setValueToField("st_tributaria_documento", rsm7.getInt("st_tributaria"));
					}
					if(rsm7.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
						rsm.setValueToField("vl_ipi_substituto_documento", rsm7.getFloat("vl_ipi"));
						rsm.setValueToField("vl_base_ipi_substituto_documento", rsm7.getFloat("vl_base_ipi"));
						rsm.setValueToField("st_tributaria_substituto_documento", rsm7.getInt("st_tributaria"));
					}
				}
				
				//Total Tributo II
				ResultSetMap rsm8 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_ii, I.pr_aliquota, SUM(I.vl_base_calculo) AS vl_base_ii, P.st_tributaria, Q.nr_situacao_tributaria FROM adm_saida_item_aliquota I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_item =  " + rsm.getInt("cd_item") +
																		 "			AND I.cd_produto_servico = " +rsm.getInt("cd_produto_servico") +
																		 "          AND I.cd_documento_saida = " +rsm.getInt("cd_documento_saida") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa") + 
																		 "          AND P.cd_tributo           = "+cdTributoII + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria, I.pr_aliquota").executeQuery());
				if(rsm8.next()){
					rsm.setValueToField("vl_ii", rsm8.getFloat("vl_ii"));
					rsm.setValueToField("vl_base_ii", rsm8.getFloat("vl_base_ii"));
					rsm.setValueToField("pr_ii", rsm8.getFloat("pr_aliquota"));
					rsm.setValueToField("st_tributaria", rsm8.getInt("st_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria", rsm8.getString("nr_situacao_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria_ii", rsm8.getString("nr_situacao_tributaria"));
				}
				if(rsm8.next()){
					rsm.setValueToField("ErroAliq", 3);
				}
				
				//Total Tributo II do Documento
				ResultSetMap rsm9 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_ii, SUM(I.vl_base_calculo) AS vl_base_ii, P.st_tributaria, Q.nr_situacao_tributaria FROM adm_saida_item_aliquota I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_documento_saida = " +rsm.getInt("cd_documento_saida") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa") + 
																		 "			AND P.cd_tributo           = "+cdTributoII + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria").executeQuery());
				while(rsm9.next()){
					if(rsm9.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
						rsm.setValueToField("vl_ii_documento", rsm9.getFloat("vl_ii"));
						rsm.setValueToField("vl_base_ii_documento", rsm9.getFloat("vl_base_ii"));
						rsm.setValueToField("st_tributaria_documento", rsm9.getInt("st_tributaria"));
					}
					if(rsm9.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
						rsm.setValueToField("vl_ii_substituto_documento", rsm9.getFloat("vl_ii"));
						rsm.setValueToField("vl_base_ii_substituto_documento", rsm9.getFloat("vl_base_ii"));
						rsm.setValueToField("st_tributaria_substituto_documento", rsm9.getInt("st_tributaria"));
					}
				}
				
				//Total Tributo PIS
				ResultSetMap rsm10 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_pis, I.pr_aliquota, SUM(I.vl_base_calculo) AS vl_base_pis, P.st_tributaria, Q.nr_situacao_tributaria FROM adm_saida_item_aliquota I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_item =  " + rsm.getInt("cd_item") +
																		 "			AND I.cd_produto_servico = " +rsm.getInt("cd_produto_servico") +
																		 "          AND I.cd_documento_saida = " +rsm.getInt("cd_documento_saida") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa") + 
																		 "          AND P.cd_tributo           = "+cdTributoPIS + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria, I.pr_aliquota").executeQuery());
				if(rsm10.next()){
					rsm.setValueToField("vl_pis", rsm10.getFloat("vl_pis"));
					rsm.setValueToField("vl_base_pis", rsm10.getFloat("vl_base_pis"));
					rsm.setValueToField("pr_pis", rsm10.getFloat("pr_aliquota"));
					rsm.setValueToField("st_tributaria", rsm10.getInt("st_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria", rsm10.getString("nr_situacao_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria_pis", rsm10.getString("nr_situacao_tributaria"));
				}
				if(rsm10.next()){
					rsm.setValueToField("ErroAliq", 4);
				}
				
				//Total Tributo PIS do Documento
				ResultSetMap rsm11 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_pis, SUM(I.vl_base_calculo) AS vl_base_pis, P.st_tributaria, Q.nr_situacao_tributaria FROM adm_saida_item_aliquota I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_documento_saida = " +rsm.getInt("cd_documento_saida") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa") + 
																		 "			AND P.cd_tributo           = "+cdTributoPIS + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria").executeQuery());
				while(rsm11.next()){
					if(rsm11.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
						rsm.setValueToField("vl_pis_documento", rsm11.getFloat("vl_pis"));
						rsm.setValueToField("vl_base_pis_documento", rsm11.getFloat("vl_base_pis"));
						rsm.setValueToField("st_tributaria_documento", rsm11.getInt("st_tributaria"));
					}
					if(rsm11.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
						rsm.setValueToField("vl_pis_substituto_documento", rsm11.getFloat("vl_pis"));
						rsm.setValueToField("vl_base_pis_substituto_documento", rsm11.getFloat("vl_base_pis"));
						rsm.setValueToField("st_tributaria_substituto_documento", rsm11.getInt("st_tributaria"));
					}
				}
				
				//Total Tributo COFINS
				ResultSetMap rsm12 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_cofins, I.pr_aliquota, SUM(I.vl_base_calculo) AS vl_base_cofins, P.st_tributaria, Q.nr_situacao_tributaria FROM adm_saida_item_aliquota I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_item =  " + rsm.getInt("cd_item") +
																		 "			AND I.cd_produto_servico = " +rsm.getInt("cd_produto_servico") +
																		 "          AND I.cd_documento_saida = " +rsm.getInt("cd_documento_saida") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa") + 
																		 "          AND P.cd_tributo           = "+cdTributoCOFINS + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria, I.pr_aliquota").executeQuery());
				if(rsm12.next()){
					rsm.setValueToField("vl_cofins", rsm12.getFloat("vl_cofins"));
					rsm.setValueToField("vl_base_cofins", rsm12.getFloat("vl_base_cofins"));
					rsm.setValueToField("pr_cofins", rsm12.getFloat("pr_aliquota"));
					rsm.setValueToField("st_tributaria", rsm12.getInt("st_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria", rsm12.getString("nr_situacao_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria_cofins", rsm12.getString("nr_situacao_tributaria"));
				}
				if(rsm12.next()){
					rsm.setValueToField("ErroAliq", 5);
				}
				
				//Total Tributo COFINS do Documento
				ResultSetMap rsm13 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_cofins, SUM(I.vl_base_calculo) AS vl_base_cofins, P.st_tributaria, Q.nr_situacao_tributaria FROM adm_saida_item_aliquota I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_documento_saida = " +rsm.getInt("cd_documento_saida") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa") + 
																		 "			AND P.cd_tributo           = "+cdTributoCOFINS + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria").executeQuery());
				while(rsm13.next()){
					if(rsm13.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
						rsm.setValueToField("vl_cofins_documento", rsm13.getFloat("vl_cofins"));
						rsm.setValueToField("vl_base_cofins_documento", rsm13.getFloat("vl_base_cofins"));
						rsm.setValueToField("st_tributaria_documento", rsm13.getInt("st_tributaria"));
					}
					if(rsm13.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
						rsm.setValueToField("vl_cofins_substituto_documento", rsm13.getFloat("vl_cofins"));
						rsm.setValueToField("vl_base_cofins_substituto_documento", rsm13.getFloat("vl_base_cofins"));
						rsm.setValueToField("st_tributaria_substituto_documento", rsm13.getInt("st_tributaria"));
					}
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);			
			System.err.println("Erro! DocumentoSaidaServices.getAllItens: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getResumoVendasDia(GregorianCalendar dtMovimento) {
		return getResumoVendasDia(dtMovimento, null);
	}

	public static ResultSetMap getResumoVendasDia(GregorianCalendar dtMovimento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.cd_produto_servico, A.cd_empresa, id_reduzido, id_produto_servico, "+
                    "       A.vl_unitario, CAST(C.nm_produto_servico AS VARCHAR(100)) AS nm_produto_servico, " +
                    "       CAST(C.txt_dado_tecnico AS VARCHAR(100)) AS txt_dado_tecnico, " +
                    "       CAST(C.txt_especificacao AS VARCHAR(100)) AS txt_especificacao, "+
                    "       SUM(A.qt_saida) AS qt_saida,  "+
                    "       SUM(vl_unitario * qt_saida) AS cl_total_item "+
                    "FROM alm_documento_saida_item A "+
                    "JOIN grl_produto_servico_empresa   B ON (A.cd_produto_servico = B.cd_produto_servico "+
                    "                                     AND A.cd_empresa = B.cd_empresa) " +
                    "JOIN grl_produto_servico           C ON (B.cd_produto_servico = C.cd_produto_servico) " +
                    "JOIN alm_documento_saida           D ON (A.cd_documento_saida = D.cd_documento_saida) " +
                    "LEFT OUTER JOIN grl_unidade_medida E ON (A.cd_unidade_medida = E.cd_unidade_medida) " +
                    "WHERE st_documento_saida = "+ST_CONCLUIDO+
                    "  AND CAST(dt_documento_saida AS DATE) = ? "+
                    "GROUP BY 1, 2, 3, 4, 5, 6, 7, 8");
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllFormasPlanosPag(int cdDocumentoSaida) {
		return getAllFormasPlanosPag(cdDocumentoSaida, null);
	}

	public static ResultSetMap getAllFormasPlanosPag(int cdDocumentoSaida, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.*, B.nm_forma_pagamento, B.sg_forma_pagamento, B.tp_forma_pagamento, B.id_forma_pagamento, " +
					"       C.nm_plano_pagamento, C.id_plano_pagamento " +
					"FROM adm_plano_pagto_documento_saida A, adm_forma_pagamento B, adm_plano_pagamento C " +
					"WHERE A.cd_forma_pagamento = B.cd_forma_pagamento " +
					"  AND A.cd_plano_pagamento = C.cd_plano_pagamento " +
					"  AND A.cd_documento_saida = ?");
			pstmt.setInt(1, cdDocumentoSaida);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaServices.getAllFormasPlanosPag: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ContaReceberCategoria> getClassificacaoEmCategorias(DocumentoSaida documentoSaida, Connection connect) {
		boolean isConnectionNull = connect==null;
		ArrayList<ContaReceberCategoria> result = new ArrayList<ContaReceberCategoria>();
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			/*
			 * Sumariza por grupo
			 */
			ResultSetMap rsmPorGrupo = new ResultSetMap(connect.prepareStatement(
							"SELECT C.cd_grupo, C.cd_grupo_superior, C.cd_categoria_receita, " +
							"       SUM((qt_saida * vl_unitario) + vl_acrescimo - vl_desconto) AS vl_total_grupo " +
		                    "FROM alm_documento_saida_item A " +
		                    "LEFT OUTER JOIN alm_produto_grupo B ON (A.cd_produto_servico = B.cd_produto_servico" +
		                    "                                    AND B.lg_principal       = 1" +
		                    "                                    AND B.cd_empresa         = "+documentoSaida.getCdEmpresa()+") " +
		                    "LEFT OUTER JOIN alm_grupo         C ON (B.cd_grupo           = C.cd_grupo) " +
		                    "WHERE A.cd_documento_saida = "+documentoSaida.getCdDocumentoSaida()+
		                    " GROUP BY C.cd_grupo, C.cd_grupo_superior, C.cd_categoria_receita").executeQuery());
			while(rsmPorGrupo.next())	{
				int cdCategoria 	= rsmPorGrupo.getInt("cd_categoria_receita");
				int cdGrupoSuperior = rsmPorGrupo.getInt("cd_grupo_superior");
				while(cdCategoria <= 0 && cdGrupoSuperior>0)	{
					ResultSet rs = connect.prepareStatement("SELECT cd_categoria_receita, cd_grupo_superior " +
							                                "FROM alm_grupo " +
							                                "WHERE cd_grupo = "+cdGrupoSuperior).executeQuery();
					if(rs.next())	{
						cdGrupoSuperior = rs.getInt("cd_grupo_superior");
						cdCategoria 	= rs.getInt("cd_categoria_receita");
					}
					else
						break;
				}
				/*
				 * Se não encontrou a categoria nem no grupo nem no grupo superior, busca a categoria default
				 */
				if(cdCategoria <= 0)	{
					cdCategoria = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_RECEITAS_DEFAULT", 0, documentoSaida.getCdEmpresa());
					if(cdCategoria <=0)	{
						com.tivic.manager.util.Util.registerLog(new Exception("Erro: Categoria default não informada!"));
						System.out.println("Erro: Categoria default não informada!");
						return null;
					}
				}
				/*
				 * Verifica se a categoria já não está na lista, não estando inclui, se já estiver acrescenta o valor
				 */
				boolean found = false;
				for(int i=0; i<result.size(); i++)	{
					found = result.get(i).getCdCategoriaEconomica()==cdCategoria;
					if(found)	{
						result.get(i).setVlContaCategoria(result.get(i).getVlContaCategoria() + rsmPorGrupo.getFloat("vl_total_grupo"));
						break;
					}
				}
				if(!found)
					result.add(new ContaReceberCategoria(0 /*cdContaReceber*/, cdCategoria, rsmPorGrupo.getFloat("vl_total_grupo"), 0));
			}
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaServices.getClassificacaoEmCategorias: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllTributos(int cdDocumentoSaida) {
		return getAllTributos(cdDocumentoSaida, null);
	}

	public static ResultSetMap getAllTributos(int cdDocumentoSaida, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT SUM((A.pr_aliquota/100) * A.vl_base_calculo) AS vl_tributo, " +
					"SUM(A.vl_base_calculo) AS vl_base_calculo, C.nm_tributo, C.id_tributo, A.cd_tributo " +
					"FROM adm_saida_item_aliquota A, adm_tributo_aliquota B, adm_tributo C " +
					"WHERE A.cd_tributo_aliquota = B.cd_tributo_aliquota " +
					"  AND A.cd_tributo = B.cd_tributo " +
					"  AND B.cd_tributo = C.cd_tributo " +
					"  AND cd_documento_saida = ? " +
					"GROUP BY A.cd_tributo, C.nm_tributo, C.id_tributo");
			pstmt.setInt(1, cdDocumentoSaida);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaServices.getAllTributos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllContasReceber(int cdDocumentoSaida) {
		return getAllContasReceber(cdDocumentoSaida, 0, 0, null);
	}

	public static ResultSetMap getAllContasReceber(int cdDocumentoSaida, Connection connect) {
		return getAllContasReceber(cdDocumentoSaida, 0, 0, null);
	}

	public static ResultSetMap getAllContasReceber(int cdDocumentoSaida, int cdFormaPagamento, int cdPlanoPagamento) {
		return getAllContasReceber(cdDocumentoSaida, cdFormaPagamento, cdPlanoPagamento, null);
	}

	public static ResultSetMap getAllContasReceber(int cdDocumentoSaida, int cdFormaPagamento, int cdPlanoPagamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.*, B.nm_tipo_documento, B.sg_tipo_documento, " +
					"       C.tp_conta, C.nr_conta, C.nr_dv, C.nm_conta, " +
					"       D.sg_carteira, D.nm_carteira, E.nm_forma_pagamento, " +
					"       F.nm_plano_pagamento " +
					"FROM adm_conta_receber A " +
					"JOIN adm_tipo_documento        B ON (A.cd_tipo_documento = B.cd_tipo_documento)" +
					"LEFT JOIN adm_conta_financeira C ON (A.cd_conta = C.cd_conta)" +
					"LEFT JOIN adm_conta_carteira   D ON (A.cd_conta = D.cd_conta" +
					"                                 AND A.cd_conta_carteira = D.cd_conta_carteira)" +
					"LEFT JOIN adm_forma_pagamento  E ON (A.cd_forma_pagamento = E.cd_forma_pagamento)" +
					"LEFT JOIN adm_plano_pagamento  F ON (A.cd_plano_pagamento = F.cd_plano_pagamento)" +
					"WHERE A.cd_documento_saida = " +cdDocumentoSaida+
					(cdFormaPagamento>0?" AND A.cd_forma_pagamento = " +cdFormaPagamento:"")+
					(cdPlanoPagamento>0?" AND A.cd_plano_pagamento = " +cdPlanoPagamento:"")+
					" ORDER BY dt_vencimento");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while (rsm != null && rsm.next()) {
				rsm.getRegister().put("DS_ST_CONTA", ContaReceberServices.situacaoContaReceber[rsm.getInt("st_conta")]);
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaServices.getAllItens: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result liberarSaida(int cdDocumentoSaida) {
		return liberarSaida(cdDocumentoSaida, 0, null);
	}
	
	public static Result liberarSaida(int cdDocumentoSaida, Connection connect) {
		return liberarSaida(cdDocumentoSaida, 0, connect);
	}

	public static Result liberarSaida(int cdDocumentoSaida, int cdLocalArmazenamento) {
		return liberarSaida(cdDocumentoSaida, cdLocalArmazenamento, null);
	}

	public static Result liberarSaida(int cdDocumentoSaida, int cdLocalArmazenamento, Connection connection) {
		return liberarSaida(cdDocumentoSaida, cdLocalArmazenamento, false, connection);
	}
	public static Result liberarSaida(int cdDocumentoSaida, boolean withEmail) {
		return liberarSaida(cdDocumentoSaida, 0, withEmail, null);
	}

	public static Result liberarSaida(int cdDocumentoSaida, int cdLocalArmazenamento, boolean withEmail) {
		return liberarSaida(cdDocumentoSaida, cdLocalArmazenamento, withEmail, null);
	}

	public static Result liberarSaida(int cdDocumentoSaida, int cdLocalArmazenamento, boolean withEmail, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			DocumentoSaida saida = DocumentoSaidaDAO.get(cdDocumentoSaida, connection);
			
			ResultSetMap rsmParametroValor = new ResultSetMap(connection.prepareStatement("SELECT * FROM grl_parametro_valor where cd_pessoa = " + saida.getCdCliente()).executeQuery());
			PreparedStatement pstmtParametro = connection.prepareStatement("SELECT * FROM grl_parametro WHERE cd_parametro = ?");
			while(rsmParametroValor.next()){
				pstmtParametro.setInt(1, rsmParametroValor.getInt("cd_parametro"));
				ResultSetMap rsmParametroMap = new ResultSetMap(pstmtParametro.executeQuery());
				if(rsmParametroMap.next()){
					if(rsmParametroMap.getString("nm_parametro").equals("lgAutorizadoCompra") && rsmParametroValor.getString("vl_inicial").equals("0")){
						if(isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Cliente não possui autorização para compras!");
					}
				}
			}
			
			if (cdLocalArmazenamento > 0) {
				connection.prepareStatement("DELETE FROM alm_saida_local_item " +
                                            "WHERE cd_documento_saida = "+cdDocumentoSaida).executeUpdate();
				ResultSet rs = connection.prepareStatement("SELECT * FROM alm_documento_saida_item " +
                                                           "WHERE cd_documento_saida = "+cdDocumentoSaida).executeQuery();
				while(rs.next())	{
					float qtSaida           = saida.getTpMovimentoEstoque()==MOV_ESTOQUE_NAO_CONSIGNADO || saida.getTpMovimentoEstoque()==MOV_AMBOS_TIPO_ESTOQUE ? rs.getFloat("qt_saida") : 0;
					float qtSaidaConsignada = saida.getTpMovimentoEstoque()==MOV_ESTOQUE_CONSIGNADO ? rs.getFloat("qt_saida") : 0;
					SaidaLocalItem saidaLocal = new SaidaLocalItem(0 /*cdSaidaLocalItem*/, rs.getInt("cd_produto_servico"), cdDocumentoSaida,
							                                       rs.getInt("cd_empresa"), cdLocalArmazenamento, 0 /*cdPedidoVenda*/,
							                                       new GregorianCalendar() /*dtSaida*/, qtSaida, qtSaidaConsignada,
							                                       SaidaLocalItemServices.ST_ENVIADO /*stSaidaLocalItem*/, "" /*idSaidaLocalItem*/,
							                                       rs.getInt("cd_item") /*cdItem*/);					
					if( SaidaLocalItemDAO.insert(saidaLocal, connection) <= 0)
						return new Result(-1, "Erro ao tentar registrar saída do local de armazenamento!");
				}
			}

			Result result = liberarSaida(cdDocumentoSaida, withEmail, cdLocalArmazenamento, connection);
			if (result.getCode()<=0) {				
				if (isConnectionNull)
					Conexao.rollback(connection);
				return result;
			}

			if (isConnectionNull)
				connection.commit();

			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);			
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar liberar saída!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result liberarSaida(int cdDocumentoSaida, boolean withEmail, Connection connection) {
		return liberarSaida(cdDocumentoSaida, withEmail, 0, connection);
	}
	
	public static Result liberarSaida(int cdDocumentoSaida, boolean withEmail, int cdLocalArmazenamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			/*
			 * Verifica se já faz parte de um balanço
			 */
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM alm_balanco_doc_saida WHERE cd_documento_saida = "+cdDocumentoSaida);
			if (pstmt.executeQuery().next()) {				
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(ERR_DOC_BALANCO, "Essa saída já faz parte de um balanço não é mais possível libera-la!");
			}
			/*
			 * Verifica se já está liberada
			 */
			DocumentoSaida documento = DocumentoSaidaDAO.get(cdDocumentoSaida, connection);
			if (documento==null || documento.getStDocumentoSaida()!=ST_EM_CONFERENCIA){				
				return new Result(-1, "Documento cancelado ou já liberado!");
			}

			/* *
			 * Verifica se as quantidades dos itens e locais de armazenamento batem 
			 * */
			PreparedStatement pstmtSaidaLocal = connection.prepareStatement("SELECT SUM(qt_saida) AS qt_saida, " +
					                                                        "       SUM(qt_saida_consignada) AS qt_saida_consignada " +
					                                                        "FROM alm_saida_local_item " +
					                                                        "WHERE cd_produto_servico = ? " +
					                                                        "  AND cd_item            = ? " +
					                                                        "  AND cd_empresa         = "+documento.getCdEmpresa()+
					                                                        "  AND cd_documento_saida = "+cdDocumentoSaida);
			PreparedStatement pstmtProdutoParametro = connection.prepareStatement("SELECT * FROM grl_produto_servico_parametro " +
															                      "WHERE cd_produto_servico = ? " +
															                      "  AND cd_empresa         = "+documento.getCdEmpresa());

			float vlTotal = 0;
			ResultSetMap rsItens = new ResultSetMap(connection.prepareStatement("SELECT * FROM alm_documento_saida_item WHERE cd_documento_saida = "+cdDocumentoSaida).executeQuery());
			while (rsItens.next()) {
				ProdutoServico produto = ProdutoServicoDAO.get(rsItens.getInt("cd_produto_servico"), connection); 
				//Verifica o parametro dos produtos
				pstmtProdutoParametro.setInt(1, rsItens.getInt("cd_produto_servico"));
				ResultSetMap rsmProdutoParametro = new ResultSetMap(pstmtProdutoParametro.executeQuery());
				while(rsmProdutoParametro.next()){
					//Bloqueio de Venda
					if(rsmProdutoParametro.getInt("lg_bloqueia_venda")==1 && documento.getTpSaida() != SAI_AJUSTE){
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Produto " + produto.getNmProdutoServico() + " está bloqueado para venda");
					}
					
					//Verifica Estoque - Não permite que o estoque desse produto fique negativo
					if(rsmProdutoParametro.getInt("lg_verificar_estoque_na_venda")==1){
						ProdutoEstoqueServices.getSaldoEstoqueOf(documento.getCdEmpresa(), rsItens.getInt("cd_produto_servico"), 0, Util.getDataAtual());
						ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
						if(cdLocalArmazenamento > 0)
							criterios.add(new ItemComparator("cdLocalArmazenamento", "" + cdLocalArmazenamento, ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cdEmpresa", "" + documento.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cdProdutoServico", "" + rsItens.getInt("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("dtMovimento", "" + Util.formatDate(Util.getDataAtual(), "dd/MM/yyyy"), ItemComparator.EQUAL, Types.VARCHAR));
						criterios.add(new ItemComparator("lgFiscal", "" + "1", ItemComparator.EQUAL, Types.INTEGER));
						Result resultado = ProdutoEstoqueServices.getEstoqueAtual(criterios, connection);
						float qtEstoque = (float)(resultado.getObjects().get("QT_ESTOQUE"));
						if((qtEstoque - rsItens.getFloat("qt_saida")) < 0){
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Para o produto " + produto.getNmProdutoServico() + " não é permitido estoque negativo");
						}
					}
					
					//Permitir Desconto
					if(rsmProdutoParametro.getInt("lg_permite_desconto")==0 && (rsItens.getFloat("vl_desconto") > 0)){
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Para o produto " + produto.getNmProdutoServico() + " não é permitido desconto");
					}
					
				}
				
				// Verifica preço unitário
				if(rsItens.getFloat("vl_unitario") <= 0 && documento.getTpSaida()!=SAI_AJUSTE && documento.getTpSaida()!=SAI_TRANSFERENCIA)	
					return new Result(-1, "O item nº "+rsItens.getInt("cd_item")+" não possui o preço unitário!");
				
				if(documento.getTpSaida()==SAI_TRANSFERENCIA){
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					if(cdLocalArmazenamento > 0)
						criterios.add(new ItemComparator("cdLocalArmazenamento", "" + cdLocalArmazenamento, ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cdEmpresa", "" + documento.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cdProdutoServico", "" + rsItens.getInt("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
					Result resultado = ProdutoEstoqueServices.getEstoqueAtual(criterios, connection);
					
					float qtEstoque = ((Float)resultado.getObjects().get("QT_ESTOQUE")) + ((Float)resultado.getObjects().get("QT_ESTOQUE_CONSIGNADO"));
					
					LocalArmazenamento localArmazenamento = LocalArmazenamentoDAO.get(cdLocalArmazenamento, connection);
					
					if(rsItens.getFloat("qt_saida") > qtEstoque){
						if(isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "O item nº "+rsItens.getInt("cd_item")+" não estoque suficiente em "+localArmazenamento.getNmLocalArmazenamento()+" para fazer a transferência. Estoque Atual de "+qtEstoque+" itens!");
					}
				}
				
				vlTotal += (rsItens.getFloat("qt_saida") * rsItens.getFloat("vl_unitario")) + rsItens.getFloat("vl_acrescimo") - rsItens.getFloat("vl_desconto");
				// Verifica saída do local
				pstmtSaidaLocal.setInt(1, rsItens.getInt("cd_produto_servico"));
				pstmtSaidaLocal.setInt(2, rsItens.getInt("cd_item"));
				
				ResultSetMap rsTemp = new ResultSetMap(pstmtSaidaLocal.executeQuery());				
				//rsTemp.next();				
				float qtSaida        = rsItens.getFloat("qt_saida");
				float qtLocaisSaida = 0;				
				rsTemp.beforeFirst();
				while (rsTemp.next()){
					qtLocaisSaida  = rsTemp.getFloat("qt_saida") + rsTemp.getFloat("qt_saida_consignada");
				}
				if (qtLocaisSaida != qtSaida) {					
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Quantidade lançada no local de armazenamento diferente do que o informado na saída! " +
							              "\n[qtLocaisSaida:"+qtLocaisSaida+",qtSaida:"+qtSaida+"]");
				}
			}
			rsItens.beforeFirst();
			
			/*
			 * Verifica se ja existe um documento de saida com esse numero
			 */
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("st_documento_saida", "1", ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("nr_documento_saida", documento.getNrDocumentoSaida(), ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsmDocumentoExistentes = DocumentoSaidaServices.find(criterios);
			if(rsmDocumentoExistentes.next()){
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Não podem haver dois documentos com mesmo número no sistema.");
			}
			
			/*
			 *  Confere TOTAIS
			 */
			float vlConferencia = documento.getVlTotalDocumento();
			vlTotal += documento.getVlAcrescimo() - documento.getVlDesconto() + (documento.getTpFrete() == FRT_CIF ? documento.getVlFrete() : 0);
			vlTotal = Util.roundFloat(vlTotal, 2);
			vlConferencia = Util.roundFloat(vlConferencia, 2);
			if (Math.abs(vlConferencia - vlTotal) > 1) {				
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Valor total não confere! [Valor informado no documento: "+vlConferencia+", Soma dos itens: "+vlTotal+"]");
			}
			/*
			 *  Atualiza SITUAÇÃO do documento de saída
			 */
			documento.setStDocumentoSaida(ST_CONCLUIDO);
			if (DocumentoSaidaDAO.update(documento, connection) <= 0) {				
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Falha ao tentar atualizar situação do documento!");
			}
			
			/* *
			 * Em caso de transferencias, efetua a liberacao da entrada relacionada a saida 
			 * */
			pstmt = connection.prepareStatement("SELECT A.cd_documento_entrada FROM alm_documento_entrada A " +
					                            "WHERE A.cd_documento_saida_origem = ?");
			pstmt.setInt(1, cdDocumentoSaida);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Result result = DocumentoEntradaServices.liberarEntrada(rs.getInt("cd_documento_entrada"), 0 /*cdLocalArmazenamento*/, connection);
				if (result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return result;
				}
			}

			//Validacao Fiscal
			if(ParametroServices.getValorOfParametroAsInteger("LG_GERADOR_SPED_SINTEGRA", 0, documento.getCdEmpresa()) != 0){
				//--------------------------------------------------------------------------------------------------------------------------------------
				//Cliente
				String dsValidadorCliente = "";
				
				//Cpf/Cnpj
				PessoaFisica pessoaFisica = PessoaFisicaDAO.get(documento.getCdCliente(), connection);
				if(pessoaFisica != null && !Util.isCpfValido(pessoaFisica.getNrCpf())){
					dsValidadorCliente += "CPF";
				}
				PessoaJuridica pessoaJuridica = PessoaJuridicaDAO.get(documento.getCdCliente(), connection);
				if(pessoaJuridica != null && !Util.isCNPJ(pessoaJuridica.getNrCnpj())){
					dsValidadorCliente += (dsValidadorCliente.equals("") ? "" : ", ") + "CNPJ";
				}
				
				//Email
				Pessoa pessoa = PessoaDAO.get(documento.getCdCliente(), connection);
				if(pessoa != null && (pessoa.getNmEmail() == null || pessoa.getNmEmail().trim().equals("") || !Util.isEmail(pessoa.getNmEmail().trim()))){
					dsValidadorCliente += (dsValidadorCliente.equals("") ? "" : ", ") + "Email";
				}
				
				//Telefone
				if(pessoa != null && ((pessoa.getNrTelefone1() == null || pessoa.getNrTelefone1().trim().equals("")))){
					dsValidadorCliente += (dsValidadorCliente.equals("") ? "" : ", ") + "Telefone";
				}
				
				//Endereco
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("lg_principal", "1", ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_pessoa", "" + documento.getCdCliente(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmEndereco = PessoaEnderecoDAO.find(criterios, connection);
				if(rsmEndereco.next()){
					PessoaEndereco pessoaEndereco = PessoaEnderecoDAO.get(rsmEndereco.getInt("cd_endereco"), rsmEndereco.getInt("cd_pessoa"), connection);
					//Tipo de Endereco
					if(pessoaEndereco != null && ((pessoaEndereco.getCdTipoEndereco()== 0))){
						dsValidadorCliente += (dsValidadorCliente.equals("") ? "" : ", ") + "Tipo de Endereço";
					}
					//Tipo de Logradouro
					if(pessoaEndereco != null && ((pessoaEndereco.getCdTipoLogradouro()== 0))){
						dsValidadorCliente += (dsValidadorCliente.equals("") ? "" : ", ") + "Tipo de Logradouro";
					}
					
					//Logradouro
					if(pessoaEndereco != null && ((pessoaEndereco.getNmLogradouro()== null || pessoaEndereco.getNmLogradouro().equals("")))){
						dsValidadorCliente += (dsValidadorCliente.equals("") ? "" : ", ") + "Logradouro";
					}
					
					//Numero do endereço
					if(pessoaEndereco != null && ((pessoaEndereco.getNrEndereco()== null || pessoaEndereco.getNrEndereco().equals("")))){
						dsValidadorCliente += (dsValidadorCliente.equals("") ? "" : ", ") + "Número do endereço";
					}
					
					//Complemento
					if(pessoaEndereco != null && ((pessoaEndereco.getNmComplemento()== null || pessoaEndereco.getNmComplemento().equals("")))){
						dsValidadorCliente += (dsValidadorCliente.equals("") ? "" : ", ") + "Complemento";
					}
					
					//Bairro
					if(pessoaEndereco != null && ((pessoaEndereco.getNmBairro()== null || pessoaEndereco.getNmBairro().equals("")))){
						dsValidadorCliente += (dsValidadorCliente.equals("") ? "" : ", ") + "Bairro";
					}

					//CEP
					if(pessoaEndereco != null && ((pessoaEndereco.getNrCep()== null || pessoaEndereco.getNrCep().equals("")))){
						dsValidadorCliente += (dsValidadorCliente.equals("") ? "" : ", ") + "CEP";
					}
					
					//Cidade
					if(pessoaEndereco != null && ((pessoaEndereco.getCdCidade()== 0))){
						dsValidadorCliente += (dsValidadorCliente.equals("") ? "" : ", ") + "Cidade";
					}
					else{
						Cidade cidade = CidadeDAO.get(pessoaEndereco.getCdCidade(), connection);
						//ID IBGE da Cidade
						if(cidade.getIdIbge() == null || cidade.getIdIbge().equals("")){
							dsValidadorCliente += (dsValidadorCliente.equals("") ? "" : ", ") + "ID IBGE da Cidade";
						}
					}
				}

				int cdVinculoCliente = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CLIENTE", 0);
				if(cdVinculoCliente > 0){
					ResultSetMap rsmVinculos = PessoaServices.getAllVinculosOfPessoa(documento.getCdCliente(), documento.getCdEmpresa(), connection);
					boolean achou = false;
					while(rsmVinculos.next()){
						if(cdVinculoCliente == rsmVinculos.getInt("cd_vinculo"))
							achou = true;
					}
					
					if(!achou){
						dsValidadorCliente += (dsValidadorCliente.equals("") ? "" : ", ") + "Vinculo";
					}
				}
				if(!dsValidadorCliente.equals("")){
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Dados do cliente faltando ou inválidos para geração de SPED/SINTEGRA: " + dsValidadorCliente);
				}
				
				//--------------------------------------------------------------------------------------------------------------------------------------				
				
				//Produto
				String dsValidadorProduto = "";
				ResultSetMap rsmItens = DocumentoSaidaServices.getAllItens(cdDocumentoSaida, connection);
				while(rsmItens.next()){
					//NCM
					if(rsmItens.getInt("cd_ncm") == 0){
						dsValidadorProduto += "NCM";
					}

					//Classificacao Fiscal
					if(rsmItens.getInt("cd_classificacao_fiscal") == 0){
						dsValidadorProduto += (dsValidadorProduto.equals("") ? "" : ", ") + "Classificação Fiscal";
					}
					
					//Precisao de Custo
					if(rsmItens.getFloat("qt_precisao_custo") == 0){
						dsValidadorProduto += (dsValidadorProduto.equals("") ? "" : ", ") + "Precição de Custo";
					}
					
					if(!dsValidadorProduto.equals("")){
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Dados do produto "+rsmItens.getString("nm_produto_servico")+" faltando ou inválidos para geração de SPED/SINTEGRA: " + dsValidadorProduto);
					}
				}
				
			}
			
			Result resultadoFinal = new Result(1);
			
			/*
			 * Verificação de estoque atual para reabastecimento
			 * */
			HashMap<Integer, ArrayList<DocumentoEntradaItem>> listaDocumentoEntradaItem = new HashMap<Integer, ArrayList<DocumentoEntradaItem>>();
			HashMap<Integer, ArrayList<Integer>> listaLocalArmazenamentoDestino = new HashMap<Integer, ArrayList<Integer>>();
			//Agrupado por empresa
			HashMap<Integer, ArrayList<DocumentoSaidaItem>> listaDocumentoSaidaItem = new HashMap<Integer, ArrayList<DocumentoSaidaItem>>();
			HashMap<Integer, ArrayList<Integer>> listaLocalArmazenamentoOrigem  = new HashMap<Integer, ArrayList<Integer>>();
			
			ResultSetMap retornoReabastecimento = new ResultSetMap();
			String mensagemEntrada = "";
			String mensagemSaida   = "";
			
			//Itera sobre os itens do documento
			while(rsItens.next()){
				
				ProdutoServico produtoServico = ProdutoServicoDAO.get(rsItens.getInt("cd_produto_servico"), connection);
				
				//Busca os locais de armazenamento de cada item
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_produto_servico", "" + produtoServico.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_documento_saida", rsItens.getString("cd_documento_saida"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_empresa", rsItens.getString("cd_empresa"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_item", rsItens.getString("cd_item"), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmSaidaLocalItem = SaidaLocalItemDAO.find(criterios, connection);
				while(rsmSaidaLocalItem.next()){
					
					//Busca possíveis cadastrados de Estoque para que haja um reabastecimento a partir do Local de Armazenamento de Destino
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("A.cd_local_armazenamento", rsmSaidaLocalItem.getString("cd_local_armazenamento"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("A.cd_produto_servico", "" + produtoServico.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("A.cd_empresa", rsmSaidaLocalItem.getString("cd_empresa"), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmEstoque = ProdutoEstoqueServices.find(criterios, connection);
					//Apenas registra se o estoque estiver ativado
					if(rsmEstoque.next() && rsmEstoque.getInt("st_estoque") == ProdutoEstoqueServices.ST_ATIVADO){
						
						//Busca os locais de armazenamento Destino e Origem
						LocalArmazenamento localArmazenamentoDestino = LocalArmazenamentoDAO.get(rsmEstoque.getInt("cd_local_armazenamento"), connection);
						LocalArmazenamento localArmazenamentoOrigem  = LocalArmazenamentoDAO.get(rsmEstoque.getInt("cd_local_armazenamento_origem"), connection);
						
						//Busca o estoque atual do produto no local de armazenamento
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cdLocalArmazenamento", "" + localArmazenamentoDestino.getCdLocalArmazenamento(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cdEmpresa", "" + localArmazenamentoDestino.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cdProdutoServico", "" + produtoServico.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("dtMovimento", "" + Util.formatDate(Util.getDataAtual(), "dd/MM/yyyy"), ItemComparator.EQUAL, Types.VARCHAR));
						Result resultado = ProdutoEstoqueServices.getEstoqueAtual(criterios, connection);
						float quantEstoqueTotalDestino = (Float)resultado.getObjects().get("QT_ESTOQUE") + (Float)resultado.getObjects().get("QT_ESTOQUE_CONSIGNADO");
						//Busca o estoque do local de onde será transferido
						float quantEstoqueTotalOrigem = 0;
						ProdutoEstoque produtoEstoqueOrigem = null;
						if(localArmazenamentoOrigem != null){
							criterios = new ArrayList<ItemComparator>();
							criterios.add(new ItemComparator("cdLocalArmazenamento", "" + localArmazenamentoOrigem.getCdLocalArmazenamento(), ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("cdEmpresa", "" + localArmazenamentoOrigem.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("cdProdutoServico", "" + produtoServico.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("dtMovimento", "" + Util.formatDate(Util.getDataAtual(), "dd/MM/yyyy"), ItemComparator.EQUAL, Types.VARCHAR));
							resultado = ProdutoEstoqueServices.getEstoqueAtual(criterios, connection);
							produtoEstoqueOrigem = ProdutoEstoqueDAO.get(localArmazenamentoOrigem.getCdLocalArmazenamento(), produtoServico.getCdProdutoServico(), localArmazenamentoOrigem.getCdEmpresa(), connection);
							quantEstoqueTotalOrigem = (Float)resultado.getObjects().get("QT_ESTOQUE") + (Float)resultado.getObjects().get("QT_ESTOQUE_CONSIGNADO");
						}
						
						//Se o estoque atual estiver abaixo do mínimo, ele precisará de reposição
						if(quantEstoqueTotalDestino < rsmEstoque.getFloat("qt_minima") && produtoEstoqueOrigem != null){
							
							HashMap<String, Object> retornoProduto = new HashMap<String, Object>();
							retornoProduto.put("CD_PRODUTO_SERVICO", produtoServico.getCdProdutoServico());
							retornoProduto.put("NM_PRODUTO_SERVICO", produtoServico.getNmProdutoServico());
							
							//A quantidade da transferencia será a cadastrada. Caso não haja cadastro, o sistema irá transferir o necessário para que a quantidade fique a ideal
							float quantATransferir = 0;
							if(rsmEstoque.getFloat("qt_transferencia") > 0 && quantEstoqueTotalOrigem >= rsmEstoque.getFloat("qt_transferencia")){
								quantATransferir = rsmEstoque.getFloat("qt_transferencia");
								retornoProduto.put("MENSAGEM", "Transferência de " + Util.formatNumber(quantATransferir) + " do Local de armazenamento " + localArmazenamentoOrigem.getNmLocalArmazenamento() + " para o Local de armazenamento " + localArmazenamentoDestino.getNmLocalArmazenamento());
							}
							else if((rsmEstoque.getFloat("qt_ideal") - quantEstoqueTotalDestino) > 0 && quantEstoqueTotalOrigem >= (rsmEstoque.getFloat("qt_ideal") - quantEstoqueTotalDestino)){
								quantATransferir = rsmEstoque.getFloat("qt_ideal") - quantEstoqueTotalDestino;
								retornoProduto.put("MENSAGEM", "Transferência de " + Util.formatNumber(quantATransferir) + " do Local de armazenamento " + localArmazenamentoOrigem.getNmLocalArmazenamento() + " para o Local de armazenamento " + localArmazenamentoDestino.getNmLocalArmazenamento());
							}
//							else{
//								retornoProduto.put("MENSAGEM", "Não há uma quantidade suficiente no local origem para ser transferido para o destino. Necessidade de Compra");
//							}
							
							//Guarda a mensagem que será mostrada para o usuário para aquele produto
							retornoReabastecimento.addRegister(retornoProduto);
							
							//Guarda na lista os documentos de entrada e saída para transferencia e os locais armazenamento de origem e destino
							if(quantATransferir > 0){
								//O agrupamento é feito por empresa, por isso é necessário a certificação de que exista um registro com o código da empresa que vai se inserir
								if(!listaDocumentoEntradaItem.containsKey(localArmazenamentoDestino.getCdEmpresa()) && !listaLocalArmazenamentoDestino.containsKey(localArmazenamentoDestino.getCdEmpresa())){
									ArrayList<DocumentoEntradaItem> listaItem = new ArrayList<DocumentoEntradaItem>();
									listaDocumentoEntradaItem.put(localArmazenamentoDestino.getCdEmpresa(), listaItem);
									ArrayList<Integer> listaLocalDestino = new ArrayList<Integer>();
									listaLocalArmazenamentoDestino.put(localArmazenamentoDestino.getCdEmpresa(), listaLocalDestino);
								}
								ArrayList<DocumentoEntradaItem> listaEntradaItem = listaDocumentoEntradaItem.get(localArmazenamentoDestino.getCdEmpresa());
								listaEntradaItem.add(new DocumentoEntradaItem(0, rsmEstoque.getInt("cd_produto_servico"), rsmEstoque.getInt("cd_empresa"), 0, quantATransferir, rsItens.getFloat("vl_unitario"), 0, 0, rsItens.getInt("cd_unidade_medida"), null));
								listaDocumentoEntradaItem.put(localArmazenamentoDestino.getCdEmpresa(), listaEntradaItem);
								
								ArrayList<Integer> listaLAO = listaLocalArmazenamentoDestino.get(localArmazenamentoDestino.getCdEmpresa());
								listaLAO.add(rsmEstoque.getInt("cd_local_armazenamento"));
								listaLocalArmazenamentoDestino.put(localArmazenamentoDestino.getCdEmpresa(), listaLAO);
								
								//O agrupamento é feito por empresa, por isso é necessário a certificação de que exista um registro com o código da empresa que vai se inserir
								if(!listaDocumentoSaidaItem.containsKey(localArmazenamentoOrigem.getCdEmpresa()) && !listaLocalArmazenamentoOrigem.containsKey(localArmazenamentoOrigem.getCdEmpresa())){
									ArrayList<DocumentoSaidaItem> listaItem = new ArrayList<DocumentoSaidaItem>();
									listaDocumentoSaidaItem.put(localArmazenamentoOrigem.getCdEmpresa(), listaItem);
									ArrayList<Integer> listaLocalOrigem = new ArrayList<Integer>();
									listaLocalArmazenamentoOrigem.put(localArmazenamentoOrigem.getCdEmpresa(), listaLocalOrigem);
								}
								
								//Insere o registro relacionando a empresa com o documento saida item criado
								ArrayList<DocumentoSaidaItem> listaItem = listaDocumentoSaidaItem.get(localArmazenamentoOrigem.getCdEmpresa());
								listaItem.add(new DocumentoSaidaItem(0, rsmEstoque.getInt("cd_produto_servico"), localArmazenamentoOrigem.getCdEmpresa(), quantATransferir, rsItens.getFloat("vl_unitario"), 0, 0, null, rsItens.getInt("cd_unidade_medida"), 0, 0, 0));
								listaDocumentoSaidaItem.put(localArmazenamentoOrigem.getCdEmpresa(), listaItem);
								
								//Insere o registro relacionando a empresa com o local de armazenamento
								listaLAO = listaLocalArmazenamentoOrigem.get(localArmazenamentoOrigem.getCdEmpresa());
								listaLAO.add(rsmEstoque.getInt("cd_local_armazenamento_origem"));
								listaLocalArmazenamentoOrigem.put(localArmazenamentoOrigem.getCdEmpresa(), listaLAO);
							}
						}
						else if(quantEstoqueTotalDestino < rsmEstoque.getFloat("qt_minima") && produtoEstoqueOrigem == null){
							
							HashMap<String, Object> retornoProduto = new HashMap<String, Object>();
							retornoProduto.put("CD_PRODUTO_SERVICO", produtoServico.getCdProdutoServico());
							retornoProduto.put("NM_PRODUTO_SERVICO", produtoServico.getNmProdutoServico());
							retornoProduto.put("MENSAGEM", "Não há uma quantidade suficiente no local "+localArmazenamentoDestino.getNmLocalArmazenamento()+". Necessidade de Compra");
							retornoReabastecimento.addRegister(retornoProduto);
						}
					}
				}
				
			}
			rsItens.beforeFirst();
			
			//Variavel para jogar o documento de saida como documento de saide origem do documento de entrada
			HashMap<Integer, Integer> registroEmpresaDocumento = new HashMap<Integer, Integer>();
			
			//Faz os documentos de saida do reabastecimento
			for(Integer cdEmpresa : listaDocumentoSaidaItem.keySet()){
				int cdNaturezaOperacaoTransferencia = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_SAIDA_TRANSFERENCIA", 0);
				if(cdNaturezaOperacaoTransferencia < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Configure o parâmetro de natureza de operação para transferência");
				}
				
				DocumentoSaida docSaida = new DocumentoSaida(0, cdEmpresa.intValue(), documento.getCdEmpresa(), Util.getDataAtual(), DocumentoSaidaServices.ST_EM_CONFERENCIA, null/*nrDocumentoSaida*/,
															DocumentoSaidaServices.TP_DOC_NAO_FISCAL, DocumentoSaidaServices.SAI_TRANSFERENCIA, Util.getDataAtual(), cdNaturezaOperacaoTransferencia, 
															documento.getCdVendedor(), documento.getCdMoeda(), 0/*vlTotalDocumento*/, documento.getCdDigitador(), 0/*vlTotalItens*/);
				
				Result resultadoDocumentoSaida = insert(docSaida, 0, 0, 0, 0, 0, connection);
				docSaida = DocumentoSaidaDAO.get(resultadoDocumentoSaida.getCode(), connection);
				mensagemSaida += (mensagemSaida.equals("") ? "" : ", ") + docSaida.getNrDocumentoSaida();
				registroEmpresaDocumento.put(cdEmpresa.intValue(), docSaida.getCdDocumentoSaida());
				if(resultadoDocumentoSaida.getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return resultadoDocumentoSaida;
				}
				
				ArrayList<DocumentoSaidaItem> listaItem = listaDocumentoSaidaItem.get(cdEmpresa);
				
				ArrayList<Integer> listaLocalArmazenamento = listaLocalArmazenamentoOrigem.get(cdEmpresa);
				
				float vlTotalItens = 0;
				
				for(int i = 0; i < listaItem.size(); i++){
					DocumentoSaidaItem docSaidaItem = listaItem.get(i);
					docSaidaItem.setCdDocumentoSaida(docSaida.getCdDocumentoSaida());
					docSaidaItem.setCdNaturezaOperacao(docSaida.getCdNaturezaOperacao());
					vlTotalItens += (docSaidaItem.getVlUnitario() * docSaidaItem.getQtSaida());
					
					Result resultadoDocumentoSaidaItem = DocumentoSaidaItemServices.insert(docSaidaItem, listaLocalArmazenamento.get(i), connection);
					if(resultadoDocumentoSaidaItem.getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connection);
						return resultadoDocumentoSaidaItem;
					}
				}
				
				docSaida.setVlTotalDocumento(vlTotalItens);
				docSaida.setVlTotalItens(vlTotalItens);
				
				if(DocumentoSaidaDAO.update(docSaida, connection) < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao atualizar documento");
				}
				
			}
			
			//Faz os documentos de entrada do reabastecimento
			for(Integer cdEmpresa : listaDocumentoEntradaItem.keySet()){
				int cdNaturezaOperacaoTransferencia = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_ENTRADA_TRANSFERENCIA", 0);
				if(cdNaturezaOperacaoTransferencia < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Configure o parâmetro de natureza de operação para transferência");
				}
				DocumentoEntrada docEntrada = new DocumentoEntrada(0, documento.getCdEmpresa(), cdEmpresa.intValue(), Util.getDataAtual(), Util.getDataAtual(), DocumentoEntradaServices.ST_EM_ABERTO, 
																	DocumentoEntradaServices.getProximoNrDocumento2(cdEmpresa.intValue(), true, connection), DocumentoEntradaServices.TP_DOC_NAO_FISCAL, DocumentoEntradaServices.ENT_TRANSFERENCIA, cdNaturezaOperacaoTransferencia, 
																	DocumentoEntradaServices.MOV_ESTOQUE_NAO_CONSIGNADO, ParametroServices.getValorOfParametroAsInteger("CD_MOEDA_DEFAULT", 0), 0, 
																	registroEmpresaDocumento.get(cdEmpresa.intValue()), documento.getCdDigitador(), 0);
				
				Result resultadoDocumentoEntrada = DocumentoEntradaServices.save(docEntrada, connection);
				docEntrada = DocumentoEntradaDAO.get(resultadoDocumentoEntrada.getCode(), connection);
				mensagemEntrada += (mensagemEntrada.equals("") ? "" : ", ") + docEntrada.getNrDocumentoEntrada();
				if(resultadoDocumentoEntrada.getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return resultadoDocumentoEntrada;
				}
				

				ArrayList<DocumentoEntradaItem> listaItem = listaDocumentoEntradaItem.get(cdEmpresa);
				
				ArrayList<Integer> listaLocalArmazenamento = listaLocalArmazenamentoDestino.get(cdEmpresa);
				
				float vlTotalItens = 0;
				
				for(int i = 0; i < listaItem.size(); i++){
					DocumentoEntradaItem docEntradaItem = listaItem.get(i);
					docEntradaItem.setCdDocumentoEntrada(docEntrada.getCdDocumentoEntrada());
					
					vlTotalItens += (docEntradaItem.getVlUnitario() * docEntradaItem.getQtEntrada());
					
					Result resultadoDocumentoEntradaItem = DocumentoEntradaItemServices.insert(docEntradaItem, listaLocalArmazenamento.get(i), connection);
					if(resultadoDocumentoEntradaItem.getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connection);
						return resultadoDocumentoEntradaItem;
					}
				}
				
				docEntrada.setVlTotalDocumento(vlTotalItens);
				docEntrada.setVlTotalItens(vlTotalItens);
				
				if(DocumentoEntradaDAO.update(docEntrada, connection) < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao atualizar documento");
				}
				
			}
			
			if(!mensagemEntrada.equals(""))
				mensagemEntrada = "Documento de Entrada a Liberar: " + mensagemEntrada;
			if(!mensagemSaida.equals(""))
				mensagemSaida = ". Documento de Saida a Liberar: " + mensagemSaida;
			if(retornoReabastecimento.size()>0){
				resultadoFinal.setCode(RET_REABASTECIMENTO);
				resultadoFinal.setMessage(mensagemEntrada + mensagemSaida);
				resultadoFinal.addObject("reabastecimento", retornoReabastecimento);
			}
			
			/**
			 * Caso esteja configurado, envia email para o cliente sobre o cupom emitido - DEIXAR ESSA PARTE SEMPRE POR ULTIMO PARA NÃO SE ENVIAR EMAIL SE HOUVER ALGUM ERRO EM OUTRA PARTE
			 */			
			Parametro parametro = ParametroServices.getByNameCdPessoa("lgEnviaAvisoCupomEmitido", documento.getCdCliente());
			if (parametro != null && withEmail){
				int cdParametro = parametro.getCdParametro();
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_pessoa", "" + documento.getCdCliente(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_parametro", "" + cdParametro, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmParametro = ParametroValorDAO.find(criterios);
				int lgEnviaAvisoCupomEmitido = 0;
				if(rsmParametro.next()){
					lgEnviaAvisoCupomEmitido = Integer.parseInt(rsmParametro.getString("vl_inicial"));
				}
				
				if(lgEnviaAvisoCupomEmitido > 0 && documento.getTpDocumentoSaida() == DocumentoSaidaServices.TP_CUPOM_FISCAL){
					
					NumberFormat formatoPreco = NumberFormat.getCurrencyInstance();  
			        formatoPreco.setMaximumFractionDigits(2);
			        NumberFormat formatoQuantidade = NumberFormat.getNumberInstance();  
			        
			        Pessoa pessoa = PessoaDAO.get(documento.getCdCliente(), connection);
			        PessoaFisica clienteFisica = PessoaFisicaDAO.get(documento.getCdCliente(), connection);
					Empresa empresa = EmpresaDAO.get(documento.getCdEmpresa(), connection);
			        
			        ResultSetMap rsmItens = DocumentoSaidaServices.getAllItens(cdDocumentoSaida);
					
			        
			        String textoEmail = "<img src=\'cid:logoEmpresa\' width=\"30%\" height=\"30%\" /><br />";
			        textoEmail += "Prezad" + (clienteFisica == null || clienteFisica.getTpSexo() == 1 ? "a" : "o") + " " + (clienteFisica != null && clienteFisica.getNmFormaTratamento() != null && !clienteFisica.getNmFormaTratamento().equals("") ? clienteFisica.getNmFormaTratamento() : "") + " " + pessoa.getNmPessoa() + "<br /><br />";
					textoEmail += "Este é apenas um aviso de que foi lançado um débito em sua conta, referente a compra abaixo descriminada.<br /><br /><br />";
					textoEmail += "DESCRIÇÃO DA COMPRA<br /><br />";
					
					textoEmail += "Cupom Fiscal Nº: " + documento.getNrDocumentoSaida() + " <br />";
					textoEmail += Util.fill("Data da compra: " + Util.formatDate(documento.getDtDocumentoSaida(), "dd/MM/yyyy"), 28, "&nbsp;", 'D')+ " Hora: "+Util.formatDate(documento.getDtDocumentoSaida(), "HH:mm:ss")+"<br />";
					textoEmail += Util.fill("Placa do Veículo: ", 35, "&nbsp;", 'D') + " Motorista: <br />";
					textoEmail += Util.fill("Valor: " + formatoPreco.format(documento.getVlTotalDocumento()), 35, "&nbsp;", 'D') + " Nº Itens: " + rsmItens.size()+ "<br />";
			        textoEmail += "Itens:  <br /><br />";
				
					while(rsmItens.next()){
						formatoPreco.setMaximumFractionDigits(rsmItens.getInt("qt_precisao_custo") > 0 ? rsmItens.getInt("qt_precisao_custo") : 2);
						formatoQuantidade.setMaximumFractionDigits(rsmItens.getInt("nr_precisao_medida"));
						textoEmail += rsmItens.getString("nm_produto_servico") + " <br />" +  
 									  "Quantidade " + formatoQuantidade.format(Util.arredondar(rsmItens.getDouble("qt_saida"), rsmItens.getInt("nr_precisao_medida"))) + " " + rsmItens.getString("sg_unidade_medida") + " <br />" +  
									  "Valor Unitário " + formatoPreco.format(Util.arredondar(rsmItens.getDouble("vl_unitario"), (rsmItens.getInt("qt_precisao_custo") > 0 ? rsmItens.getInt("qt_precisao_custo") : 2))) + " <br />";
						formatoPreco.setMaximumFractionDigits(2);
						textoEmail += "Valor Total "+formatoPreco.format(rsmItens.getDouble("qt_saida") * rsmItens.getDouble("vl_unitario")) +" <br /> <br />";
					}
					
					textoEmail += "Estamos a sua inteira disposição para mais esclarecimentos.<br /><br /><br />";
					textoEmail += "Atenciosamente,<br /><br />";
					textoEmail += "<b>" + empresa.getNmRazaoSocial() + "</b><br />";
					textoEmail += "<b>Fone: " + Util.formatTelefone(empresa.getNrTelefone1())+ "</b><br /><br /><br /><br /><br />";
					
					textoEmail += "*enviado automaticamente por DNA Suíte ERP, CRM e SCM<br /><br />";
					textoEmail += "<img src=\'cid:logoDesenvolvedora\' width=\"10%\" height=\"10%\" />";
			        
					Result resultado = enviarEmailCupomDebito(cdDocumentoSaida, documento.getCdCliente(), "Cupom Fiscal emitido", textoEmail, DocumentoSaidaArquivoServices.ST_LIBERAR, connection);
					if(resultado.getCode() <= 0){
						if (isConnectionNull)
							Conexao.rollback(connection);
						return resultado;
					}
				}
			}
			if (isConnectionNull)
				connection.commit();

			return resultadoFinal;
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar liberar saída!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@SuppressWarnings("unchecked")
	public static Result enviarEmailCupomDebito(int cdDocumentoSaida, int cdCliente, String titulo, String textoEmail, int stDocumentoSaidaArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
		    
			
			DocumentoSaida docSaida = DocumentoSaidaDAO.get(cdDocumentoSaida, connect);
			
			Pessoa pessoaCliente = PessoaDAO.get(docSaida.getCdCliente(), connect);
			PessoaFisica pessoaClienteFisica = PessoaFisicaDAO.get(docSaida.getCdCliente(), connect);
			PessoaJuridica pessoaClienteJuridica = PessoaJuridicaDAO.get(docSaida.getCdCliente(), connect);
			
			String nmEmail = (pessoaClienteFisica != null ? pessoaCliente.getNmEmail() : "");
			
			Empresa empresa = EmpresaDAO.get(docSaida.getCdEmpresa(), connect);
			if(nmEmail == null || nmEmail.equals("")){
				if(pessoaClienteFisica != null)
					return new Result(ERR_ENVIO_EMAIL, "O cliente não possui um email cadastrado para que se envie o email. Deseja "+(stDocumentoSaidaArquivo==0?"faturar":"liberar")+" o documento mesmo assim?");
				else if(pessoaClienteJuridica != null){
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_pessoa_juridica", "" + pessoaCliente.getCdPessoa(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("lg_email_principal", "1", ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmPessoasContato = PessoaContatoDAO.find(criterios);
					if(rsmPessoasContato.next()){
						nmEmail = rsmPessoasContato.getString("nm_email");
					}
					else{
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_pessoa_juridica", "" + pessoaCliente.getCdPessoa(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("lg_email_cobranca", "1", ItemComparator.EQUAL, Types.INTEGER));
						rsmPessoasContato = PessoaContatoDAO.find(criterios);
						if(rsmPessoasContato.next()){
							nmEmail = rsmPessoasContato.getString("nm_email");
						}
						else
							return new Result(ERR_ENVIO_EMAIL, "O cliente não possui um email cadastrado para que se envie o email. Deseja "+(stDocumentoSaidaArquivo==0?"faturar":"liberar")+" o documento mesmo assim?");
					}
					
				}
				else
					return new Result(-1, "Problema com cadastro do cliente. Contate o desenvolvedor!");
			}
			if(!Util.isEmail(nmEmail)){
				return new Result(ERR_ENVIO_EMAIL, "O cliente não possui um email válido. Deseja "+(stDocumentoSaidaArquivo==0?"faturar":"liberar")+" o documento mesmo assim?");
			}
			
			String assunto = titulo + " - Nº " + docSaida.getNrDocumentoSaida() + " - da Empresa: " + empresa.getNmRazaoSocial(); 
			String texto = textoEmail;//Assinatura da empresa ao desenvolvedor
			String emailDestinatario = nmEmail;
			
			//Caso o sistema identifique que o email já foi enviado para o cliente, ele dará prosseguimento a próxima analise, não enviando novamente o email
			//Identifica se ja um arquivo de email que já tenha sido enviado
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_documento_saida", "" + docSaida.getCdDocumentoSaida(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("CAST(dt_criacao AS DATE)", Util.formatDate(Util.getDataAtual(), "dd/MM/yyyy"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
			criterios.add(new ItemComparator("st_documento_saida_arquivo", "" + stDocumentoSaidaArquivo, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("st_arquivo", "" + ArquivoServices.ST_ENVIADO, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmContaArquivo = DocumentoSaidaArquivoServices.find(criterios);
			
			//Identifica se não há nenhum arquivo email que esteja a espera de ser enviado
			//Esse criterio é utilizado por conta do campo "Dias entre envio de cobrança" pois pode-se ter mais de um envio para cobranca da
			//mesma conta, logo se ela foi criada, deve ser enviada
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_documento_saida", "" + docSaida.getCdDocumentoSaida(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("CAST(dt_criacao AS DATE)", Util.formatDate(Util.getDataAtual(), "dd/MM/yyyy"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
			criterios.add(new ItemComparator("st_documento_saida_arquivo", "" + stDocumentoSaidaArquivo, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("st_arquivo", "" + ArquivoServices.ST_NAO_ENVIADO, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmContaArquivoNaoEnviado = DocumentoSaidaArquivoServices.find(criterios);
			if(rsmContaArquivo.next() && !rsmContaArquivoNaoEnviado.next()){
				return new Result(ERR_EMAIL_JA_ENVIADO);
			}
			
			Arquivo emailArquivo;
			//Faz a busca para saber se o arquivo do email já existe, caso exista, ele apenas atualiza as informações
			//Caso contrário, cria um novo arquivo e vincula a conta
			rsmContaArquivoNaoEnviado.beforeFirst();
			if(rsmContaArquivoNaoEnviado.next()){
				emailArquivo = ArquivoDAO.get(rsmContaArquivoNaoEnviado.getInt("cd_arquivo"));
				emailArquivo.setBlbArquivo(texto.getBytes());
				emailArquivo.setNmArquivo("Email de Cobranca. Para " +emailDestinatario);
				emailArquivo.setNmDocumento(assunto+emailDestinatario);
				if(ArquivoDAO.update(emailArquivo) < 0){
					System.out.println("Erro ao atualizar arquivo de email para " + emailDestinatario);
				}
			}
			else{
				emailArquivo = new Arquivo(0, "Email Para " +emailDestinatario, Util.getDataAtual(),
											assunto+emailDestinatario, texto.getBytes(),
											ParametroServices.getValorOfParametroAsInteger("CD_TIPO_ARQUIVO_EMAIL", 0),
											Util.getDataAtual(), 0/*cdUsuario*/, ArquivoServices.ST_NAO_ENVIADO, null, null, 0, null, 0, null, null);
				int code = ArquivoDAO.insert(emailArquivo);
				if(code < 0){
					System.out.println("Erro ao crar arquivo de email para " + emailDestinatario);
				}
				emailArquivo.setCdArquivo(code);
				
				if(DocumentoSaidaArquivoDAO.insert(new DocumentoSaidaArquivo(docSaida.getCdDocumentoSaida(), emailArquivo.getCdArquivo(), stDocumentoSaidaArquivo)) < 0){
					System.out.println("Erro ao crar arquivo de email para " + emailDestinatario);
				}
			}
			
			if(ParametroServices.getValorOfParametroAsString("NM_SERVIDOR_SMTP", "").equals(""))
				return new Result(-1, "Nome do Servidor SMTP não configurado nos parametros!");
			if(ParametroServices.getValorOfParametroAsString("NR_PORTA_SMTP", "").equals(""))
				return new Result(-1, "Número da porta do Servidor SMTP não configurado nos parametros!");
			if(ParametroServices.getValorOfParametroAsString("NM_LOGIN_SERVIDOR_SMTP", "").equals(""))
				return new Result(-1, "Login do Servidor SMTP não configurado nos parametros!");
			if(ParametroServices.getValorOfParametroAsString("NM_SENHA_SERVIDOR_SMTP", "").equals(""))
				return new Result(-1, "Senha do Servidor SMTP não configurado nos parametros!");
			if(ParametroServices.getValorOfParametroAsString("NM_EMAIL_REMETENTE_SMTP", "").equals(""))
				return new Result(-1, "Remetente SMTP não configurado nos parametros!");
			
			if(ParametroServices.getValorOfParametroAsInteger("LG_DEBUG_SMTP", 0) == 1){
				System.out.println("PORTA = " + ParametroServices.getValorOfParametroAsInteger("NR_PORTA_SMTP", 0));
				System.out.println("AUT   = " + ParametroServices.getValorOfParametroAsInteger("LG_AUTENTICACAO_SMTP", 0));
				System.out.println("SSL   = " + ParametroServices.getValorOfParametroAsInteger("LG_SSL_SMTP", 0));
			}
			SMTPClient cliente = new SMTPClient(ParametroServices.getValorOfParametroAsString("NM_SERVIDOR_SMTP", ""), 
												ParametroServices.getValorOfParametroAsInteger("NR_PORTA_SMTP", 0), 
												ParametroServices.getValorOfParametroAsString("NM_LOGIN_SERVIDOR_SMTP", ""), 
												ParametroServices.getValorOfParametroAsString("NM_SENHA_SERVIDOR_SMTP", ""), 
												ParametroServices.getValorOfParametroAsInteger("LG_AUTENTICACAO_SMTP", 0)==1, 
												ParametroServices.getValorOfParametroAsInteger("LG_SSL_SMTP", 0)==1, ParametroServices.getValorOfParametroAsString("NM_TRANSPORT_SMTP", "smtp"));
			if(ParametroServices.getValorOfParametroAsInteger("LG_DEBUG_SMTP", 0) == 1){
				cliente.setDebug(true);	
			}
			if(cliente.connect()) {
				String[] to = {emailDestinatario};
				String caminho = ParametroServices.getValorOfParametro("NM_ARQUIVO_DESENVOLVEDOR", empresa.getCdEmpresa());
				HashMap<String, Object> attachments[];
				if(caminho != null && !caminho.equals(""))	{
					 attachments = new HashMap[2];
				}
				else{
					 attachments = new HashMap[1];
				}
				
				HashMap<String, Object> register = new HashMap<String, Object>(); 
				register.put("BYTES", empresa.getImgLogomarca());
				register.put("NAME", "empresa.jpg");
				register.put("CID", "logoEmpresa");
				attachments[0] = register;
				
				if(caminho != null && !caminho.equals(""))	{
					try	{
						File file = new File(caminho);
						if(file != null){
							byte[] data = Util.getBytesFromFile(file);
							register = new HashMap<String, Object>(); 
							register.put("BYTES", data);
							register.put("NAME", "desenvolvedora.jpg");
							register.put("CID", "logoDesenvolvedora");
							attachments[1] = register;
						}
					}
					catch(Exception e) {
						e.printStackTrace(System.out);
					}
				}
				
				try{
					cliente.send(ParametroServices.getValorOfParametroAsString("NM_EMAIL_REMETENTE_SMTP", ""), new String[] {ParametroServices.getValorOfParametroAsString("NM_EMAIL_REMETENTE_SMTP", "")}, to, null, null, 
								assunto, texto, null, null, attachments);
					
					//Indica para o sistema que não houve problema e o email foi enviado com sucesso
					emailArquivo.setStArquivo(ArquivoServices.ST_ENVIADO);
					emailArquivo.setDtCriacao(Util.getDataAtual());
					if(ArquivoDAO.update(emailArquivo) < 0){
						System.out.println("Erro na atualização do arquivo");
					}
					System.out.println("Envio realizado com sucesso");
				}
				catch(Exception e){
					cliente.disconnect();
					return new Result(ERR_ENVIO_EMAIL, "Erro de envio!");
				}
			}
			else{
				return new Result(ERR_ENVIO_EMAIL, "Erro de conexão!");
			}
			
			cliente.disconnect();
			
			return new Result(1, "Email enviado com sucesso!");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalServices.enviarEmail: " + e);
			return new Result(-1, "Erro ao enviar o email!");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result cancelarSaida(int cdDocumentoSaida, int cdUsuario) {
		return cancelarSaida(cdDocumentoSaida, cdUsuario, true, null);
	}

	public static Result cancelarSaida(int cdDocumentoSaida, int cdUsuario, boolean deleteItens) {
		return cancelarSaida(cdDocumentoSaida, cdUsuario, deleteItens, null);
	}

	public static Result cancelarSaida(int cdDocumentoSaida, int cdUsuario, boolean deleteItens, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM alm_balanco_doc_saida " +
					                                              "WHERE cd_documento_saida = "+cdDocumentoSaida);
			if (pstmt.executeQuery().next())
				return new Result(ERR_DOC_BALANCO, "Essa saída já faz parte de um balanço, não é permitido exclui-la!");

			DocumentoSaida documento = DocumentoSaidaDAO.get(cdDocumentoSaida, connection);
			if (documento==null)
				return new Result(-1, "Documento de saída não localizado. [cdDocumentoSaida: "+cdDocumentoSaida+"]");

			documento.setStDocumentoSaida(ST_CANCELADO);
			if (DocumentoSaidaDAO.update(documento, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao tentar cancelar documento de saída!");
			}

			ResultSetMap rsmItens = getAllItens(cdDocumentoSaida, connection);
			while (rsmItens.next())
				if(documento.getCdReferenciaEcf()>0)
					if (DocumentoSaidaItemServices.cancelarItem(cdDocumentoSaida, rsmItens.getInt("cd_produto_servico"), documento.getCdEmpresa(), rsmItens.getInt("cd_item"), deleteItens, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Falha ao registrar o item cancelado!");
					}
				else if (deleteItens && documento.getTpSaida() != SAI_TRANSFERENCIA) {
					if (DocumentoSaidaItemServices.delete(cdDocumentoSaida, rsmItens.getInt("cd_produto_servico"), documento.getCdEmpresa(), rsmItens.getInt("cd_item"), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao tentar excluir itens do documento de saída cancelado!");
					}
				}
			/* em caso de transferencias, cancela a entradada relacionada */
			ResultSet rs = connection.prepareStatement("SELECT cd_documento_entrada " +
													   "FROM alm_documento_entrada " +
													   "WHERE cd_documento_saida_origem = "+cdDocumentoSaida).executeQuery();
			while (rs.next()) {
				if (DocumentoEntradaServices.cancelarEntrada(rs.getInt("cd_documento_entrada"), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar cancelar a entrada relacionada à saída!");
				}
			}
			/* estornando pagamentos */
			ResultSetMap rsmFaturamento = getAllFormasPlanosPag(cdDocumentoSaida, connection);
			while(rsmFaturamento.next())	{
				Result result = estornarPagamento(cdDocumentoSaida, rsmFaturamento.getInt("cd_forma_pagamento"), rsmFaturamento.getInt("cd_plano_pagamento"), cdUsuario, connection);
				if (result.getCode()<=0)	{
					if(isConnectionNull)
						Conexao.rollback(connection);
					return result;
				}
			}
			/* cancela contas e recebimentos */
			ResultSetMap rsmContas = getAllContasReceber(cdDocumentoSaida, connection);
			int ret = 1;
			while(rsmContas.next() && ret>0)	{
				ResultSetMap rsmRecs = MovimentoContaReceberServices.getRecebimentoOfContaReceber(rsmContas.getInt("cd_conta_receber"), connection);
				while(rsmRecs.next() && ret>0)	{
					Result result = MovimentoContaReceberServices.delete(rsmRecs.getInt("cd_conta"), rsmRecs.getInt("cd_movimento_conta"), rsmRecs.getInt("cd_conta_receber"), cdUsuario, true, connection);
					if (result.getCode() <= 0)	{
						if(isConnectionNull)
							Conexao.rollback(connection);
						return result;
					}
				}
				
				if(ret > 0)
					ret = ContaReceberServices.delete(rsmContas.getInt("cd_conta_receber"), true, false, connection);
			}

			if (ret <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(ret, "Erro ao tentar cancelar contas e recebimentos do doc. de saída!");
			}

			if (isConnectionNull)
				connection.commit();

			return new Result(1);
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro desconhecido ao tentar cancelar doc. de saída!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int updatTotaisDocumentoSaida(int cdDocumentoSaida, float vlTotalDocumento, float vlAcrescimo, float vlDesconto) {
		return updatTotaisDocumentoSaida(cdDocumentoSaida, vlTotalDocumento, vlAcrescimo, vlDesconto, null);
	}

	public static int updatTotaisDocumentoSaida(int cdDocumentoSaida, float vlTotalDocumento, float vlAcrescimo, float vlDesconto, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			DocumentoSaida docSaida = DocumentoSaidaDAO.get(cdDocumentoSaida, connection);
			docSaida.setVlTotalDocumento(vlTotalDocumento);
			docSaida.setVlDesconto(vlDesconto);
			docSaida.setVlAcrescimo(vlAcrescimo);
			return DocumentoSaidaDAO.update(docSaida, connection);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static String getProximoNrDocumento(int cdEmpresa) {
		return getProximoNrDocumento(cdEmpresa, null);
	}

	public static String getProximoNrDocumento(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			int nrAno = new GregorianCalendar().get(Calendar.YEAR);
			int nrDocumento = 0;

			if ((nrDocumento = NumeracaoDocumentoServices.getProximoNumero("DOCUMENTO_SAIDA", nrAno, cdEmpresa, connection)) <= 0)
				return null;

			return new DecimalFormat("000000").format(nrDocumento) + "/" + nrAno;
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result remove(int cdDocumentoSaida) {
		return delete(cdDocumentoSaida, null);
	}
	
	public static Result delete(int cdDocumentoSaida) {
		return delete(cdDocumentoSaida, null);
	}

	public static Result delete(int cdDocumentoSaida, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM alm_balanco_doc_saida WHERE cd_documento_saida = "+cdDocumentoSaida);
			if (pstmt.executeQuery().next()) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(ERR_DOC_BALANCO, "Esta saída já faz parte de um balanço!");
			}

			DocumentoSaida docSaida = DocumentoSaidaDAO.get(cdDocumentoSaida, connection);
			int cdEmpresa = docSaida.getCdEmpresa();

			/* verifica se existem faturas (contas a receber) geradas a partir deste documento de saída */
			ResultSet rs = connection.prepareStatement("SELECT A.cd_conta_receber " +
					                                   "FROM adm_conta_receber A " +
					                                   "WHERE A.cd_documento_saida = "+cdDocumentoSaida).executeQuery();
			if (rs.next()) {
				Conexao.rollback(connection);
				return new Result(ERR_EXISTENCIA_CONTAS_RECEBER, "Existem contas relacionadas a essa saída!");
			}

			/*verificar se existem notas fiscais relacionadas ao documento*/
			if(NotaFiscalDocVinculadoServices.findByDocSaida(cdDocumentoSaida, connection).next()){
				Conexao.rollback(connection);
				return new Result(-1, "Existem notas fiscais relacionadas a essa saída!");
			}
			
			connection.prepareStatement("DELETE FROM alm_devolucao_item WHERE cd_documento_saida = "+cdDocumentoSaida).execute();
			
			/* exclusão dos itens */
			rs = connection.prepareStatement("SELECT cd_produto_servico, cd_item FROM alm_documento_saida_item A " +
					                         "WHERE A.cd_documento_saida = "+cdDocumentoSaida).executeQuery();
			while (rs.next()) {
				if (DocumentoSaidaItemServices.delete(cdDocumentoSaida, rs.getInt("cd_produto_servico"), cdEmpresa, rs.getInt("cd_item"), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar excluir os itens!");
				}
			}

			if (docSaida.getTpSaida() == SAI_TRANSFERENCIA) {
				rs = connection.prepareStatement("SELECT cd_documento_entrada FROM alm_documento_entrada " +
						                         "WHERE cd_documento_saida_origem = "+cdDocumentoSaida).executeQuery();
				while (rs.next()) {
					Result result = DocumentoEntradaServices.delete(rs.getInt("cd_documento_entrada"), connection);
					if (result.getCode() <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						result.setMessage("Falha ao tentar excluir entrada relacionada a essa saída! "+result.getMessage());
						return result;
					}
				}
			}

			connection.prepareStatement("DELETE FROM adm_plano_pagto_documento_saida WHERE cd_documento_saida = "+cdDocumentoSaida).execute();

			/* exclusão dos tributos */
			connection.prepareStatement("DELETE FROM adm_saida_tributo A " +
					                    "WHERE A.cd_documento_saida = "+cdDocumentoSaida).executeUpdate();
			
			if (DocumentoSaidaDAO.delete(cdDocumentoSaida, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao tentar excluir o documento de saída!");
			}

			if (isConnectionNull)
				connection.commit();

			return new Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			Util.registerLog(e);
			System.err.println("Erro! DocumentoSaidaServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar excluir documento de saída!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}


	public static int insertTransferencia(DocumentoSaida transferencia, int cdLocalOrigem, int cdLocalDestino, ArrayList<DocumentoSaidaItem> itens) {
		return insertTransferencia(transferencia, cdLocalOrigem, cdLocalDestino, itens, null);
	}

	public static int insertTransferencia(DocumentoSaida transf, int cdLocalOrigem, int cdLocalDestino, ArrayList<DocumentoSaidaItem> itens, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			/*
			 * INSERE O DOCUMENTO DE SAÍDA
			 */
			int cdDocumentoSaida = DocumentoSaidaDAO.insert(transf, connection);
			if (cdDocumentoSaida <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}
			/*
			 * INSERE OS ITENS e os LOCAIS DE ARMAZENAMENTO
			 */
			for (int i=0; i<itens.size(); i++) {
				// ITENS
				DocumentoSaidaItem item = itens.get(i);
				item.setCdDocumentoSaida(cdDocumentoSaida);
				if (DocumentoSaidaItemDAO.insert(item, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
				// Saída por LOCAL
				item.setCdDocumentoSaida(cdDocumentoSaida);
				if (SaidaLocalItemDAO.insert(new SaidaLocalItem(0 /*cdSaidaLocalItem*/,item.getCdProdutoServico(),cdDocumentoSaida,
																transf.getCdEmpresa(),cdLocalOrigem /*cdLocalArmazenamento*/,0 /*cdPeditoVenda*/,
																transf.getDtDocumentoSaida(),
																transf.getTpMovimentoEstoque() == MOV_ESTOQUE_NAO_CONSIGNADO ? item.getQtSaida() : 0 /*qtSaida*/,
																transf.getTpMovimentoEstoque() == MOV_ESTOQUE_CONSIGNADO ? item.getQtSaida() : 0 /*qtSaidaConsignada*/,
																SaidaLocalItemServices.ST_ABERTO /*stSaidaLocalItem*/,"" /*idSaidaLocalItem*/,item.getCdItem()), connection) <= 0) 
				{
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}
			/*
			 * INSERINDO ENTRADA
			 */
			DocumentoEntrada docEntrada = new DocumentoEntrada(0 /*cdDocumentoEntrada*/, transf.getCdEmpresa(), transf.getCdTransportadora(),
                                                               transf.getCdCliente() /*cdFornecedor*/, transf.getDtEmissao(), transf.getDtDocumentoSaida(),
                                                               DocumentoEntradaServices.ST_EM_ABERTO /*stDocumentoEntrada*/, transf.getVlDesconto(),
                                                               transf.getVlAcrescimo(), transf.getNrDocumentoSaida() /*nrDocumentoEntrada*/,
                                                               DocumentoEntradaServices.TP_DOC_NAO_FISCAL /*tpDocumentoEntrada*/, transf.getNrConhecimento(),
                                                               DocumentoEntradaServices.ENT_TRANSFERENCIA/*tpEntrada*/, transf.getTxtObservacao(),
                                                               0 /*cdNaturezaOperacao*/,transf.getTpFrete(),transf.getNrPlacaVeiculo(),transf.getSgPlacaVeiculo(),
                                                               transf.getQtVolumes(),transf.getDtSaidaTransportadora(),transf.getDsViaTransporte(),
                                                               transf.getTxtCorpoNotaFiscal(),transf.getVlPesoBruto(),transf.getVlPesoLiquido(),
                                                               transf.getDsEspecieVolumes(),transf.getDsMarcaVolumes(),transf.getNrVolumes(),
                                                               transf.getTpMovimentoEstoque(),transf.getCdMoeda(),0 /*cdTabelaPreco*/,transf.getVlTotalDocumento(),
                                                               cdDocumentoSaida /*cdDocumentoSaidaOrigem*/,transf.getVlFrete(),transf.getVlSeguro(),transf.getCdDigitador(),
                                                               transf.getVlTotalItens(), 1 /*nrSerie*/);
			int cdDocumentoEntrada = DocumentoEntradaDAO.insert(docEntrada, connection);
			if (cdDocumentoEntrada <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}
			/*
			 * INCLUINDO ITENS e ENTRADA POR LOCAL
			 */
			for (int i=0; i<itens.size(); i++) {
				// ITENS
				DocumentoSaidaItem item = itens.get(i);
				int cdItem = DocumentoEntradaItemDAO.insert(new DocumentoEntradaItem(cdDocumentoEntrada, item.getCdProdutoServico(),
						                                                             transf.getCdEmpresa(), 0/*cdItem*/, item.getQtSaida() /*qtEntrada*/,
						                                                             0 /*vlUnitario*/,0 /*vlAcrescimo*/, 0 /*vlDesconto*/,
						                                                             item.getCdUnidadeMedida(), null /*dtEntregaPrevista*/), connection);
				if (cdItem <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
				item.setCdDocumentoSaida(cdDocumentoSaida);
				// Locais
				if (EntradaLocalItemDAO.insert(new EntradaLocalItem(item.getCdProdutoServico(), cdDocumentoEntrada, transf.getCdEmpresa(),
																	cdLocalDestino /*cdLocalArmazenamento*/,
																	transf.getTpMovimentoEstoque() == MOV_ESTOQUE_NAO_CONSIGNADO ? item.getQtSaida() : 0 /*qtEntrada*/,
																	transf.getTpMovimentoEstoque() == MOV_ESTOQUE_CONSIGNADO ? item.getQtSaida() : 0 /*qtEntradaConsignada*/,
																	0 /*cdReferencia*/, cdItem), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			if (isConnectionNull)
				connection.commit();

			return cdDocumentoSaida;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			System.err.println("Erro! DocumentoSaidaServices.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int updateTransferencia(DocumentoSaida transf, int cdLocalOrigem, int cdLocalDestino) {
		return updateTransferencia(transf, cdLocalOrigem, cdLocalDestino, null);
	}

	public static int updateTransferencia(DocumentoSaida transferencia, int cdLocalOrigem, int cdLocalDestino, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			if (DocumentoSaidaDAO.update(transferencia, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			PreparedStatement pstmt = connection.prepareStatement("UPDATE alm_saida_local_item SET cd_local_armazenamento = " +cdLocalOrigem+
					                                              "WHERE cd_documento_saida = "+transferencia.getCdDocumentoSaida());
			pstmt.execute();

			pstmt = connection.prepareStatement("SELECT A.cd_documento_entrada FROM alm_documento_entrada A " +
					                            "WHERE A.cd_documento_saida_origem = "+transferencia.getCdDocumentoSaida());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				connection.prepareStatement("UPDATE alm_entrada_local_item SET cd_local_armazenamento = " +cdLocalDestino+
						                    " WHERE cd_documento_entrada = "+rs.getInt("cd_documento_entrada")).execute();
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAsResultSet(int cdDocumentoSaida) {
		return getAsResultSet(cdDocumentoSaida, null);
	}

	public static ResultSetMap getAsResultSet(int cdDocumentoSaida, Connection connection) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_documento_saida", Integer.toString(cdDocumentoSaida), ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios, connection);
	}

	public static Result insert(DocumentoSaida objeto, int cdContaReceber) {
		return insert(objeto, cdContaReceber, 0, 0, 0, 0, null);
	}
	
	public static Result insert(DocumentoSaida objeto, int cdContaReceber, Connection connect) {
		return insert(objeto, cdContaReceber, 0, 0, 0, 0, connect);
	}
	
	public static Result insert(DocumentoSaida objeto, int cdContaReceber, float vlBaseCalculoIcms, float vlIcms, float vlBaseCalculoIcmsSubstituto, float vlIcmsSubstituto) {
		return insert(objeto, cdContaReceber, vlBaseCalculoIcms, vlIcms, vlBaseCalculoIcmsSubstituto, vlIcmsSubstituto, null);
	}

	public static Result insert(DocumentoSaida objeto, int cdContaReceber, float vlBaseCalculoIcms, float vlIcms, float vlBaseCalculoIcmsSubstituto, float vlIcmsSubstituto, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			String nrDocumento = objeto.getNrDocumentoSaida();
			nrDocumento = nrDocumento==null || nrDocumento.trim().equals("") ? getProximoNrDocumento(objeto.getCdEmpresa(), connection) : nrDocumento;
			if(ParametroServices.getValorOfParametro("NR_ID_CLIENTE", connection) != null)
				nrDocumento = ParametroServices.getValorOfParametro("NR_ID_CLIENTE", connection) +"-"+ nrDocumento; 
			objeto.setNrDocumentoSaida(nrDocumento);
			
			ResultSet rs = connection.prepareCall("SELECT * FROM alm_documento_saida " +
					                              "WHERE cd_cliente "+(objeto.getCdCliente()<0 ? " IS NULL " : " = "+objeto.getCdCliente())+
					                              "  AND nr_documento_saida = \'"+objeto.getNrDocumentoSaida()+"\'").executeQuery();
			if(rs.next())
				return new Result(-1, "Já existe um documento com o número: "+objeto.getNrDocumentoSaida()+" lançado para o cliente informado!");

			
			if(objeto.getVlDesconto() > objeto.getVlTotalDocumento()){
				Conexao.rollback(connection);
				return new Result(-1, "Valor do desconto superior ao valor do documento!");
			}
			
			//Caso seja uma nota de remessa, coloca as informações proprias desse tipo de venda
			if(objeto.getTpDocumentoSaida() == TP_NOTA_REMESSA){
				incluirRemessa(objeto);
			}
			
			
			int code = DocumentoSaidaDAO.insert(objeto, connection);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1);
			}

			
			
			if (cdContaReceber > 0) {
				ContaReceber conta = ContaReceberDAO.get(cdContaReceber, connection);
				conta.setCdDocumentoSaida(code);
				if (ContaReceberDAO.update(conta, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1);
				}
			}

			objeto.setNrDocumentoSaida(nrDocumento);
			objeto.setCdDocumentoSaida(code);
			
			// EXCLUI TODOS OS TRIBUTOS E LANÇA NOVAMENTE
			connection.prepareStatement("DELETE FROM adm_saida_tributo " +
					                 "WHERE cd_documento_saida = "+objeto.getCdDocumentoSaida()).execute();
//			connection.setAutoCommit(!isConnectionNull);
			/*
			 * Gravando informação do ICMS (Incluindo o substituto
			 */
			if(vlIcms > 0 || vlIcmsSubstituto > 0){
				int cdTributo        = TributoServices.getCdTributoById("ICMS", connection);
				float vlBaseCalculo  = vlBaseCalculoIcms;
				float prAliquota     = 0;
				float vlBaseRetencao = vlBaseCalculoIcmsSubstituto;
				SaidaTributo saiTributo = new SaidaTributo(objeto.getCdDocumentoSaida(), cdTributo, vlBaseCalculo, prAliquota, vlIcms, vlBaseRetencao, vlIcmsSubstituto);
				if(com.tivic.manager.adm.SaidaTributoDAO.insert(saiTributo, connection) <= 0){
					if (isConnectionNull){
						connection.rollback();
					}
					return new Result(-1);
				}
			}
			
			if (isConnectionNull)
				connection.commit();
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_documento_saida", "" + objeto.getCdDocumentoSaida(), ItemComparator.EQUAL, Types.INTEGER));
			Result resultado = new Result(code, "Documento Saída salvo com sucesso!", "docSaida", objeto);
			resultado.addObject("docSaidaRsm", find(criterios));
			return resultado;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar salvar dados do documento de saída!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result getLastDocumentoOf(int cdReferenciaEcf) {
		return getLastDocumentoOf(cdReferenciaEcf, null);
	}

	public static Result getLastDocumentoOf(int cdReferenciaEcf, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			ResultSet rs = connection.prepareStatement("SELECT MAX(cd_documento_saida) AS cd_documento_saida " +
													   "FROM alm_documento_saida " +
													   "WHERE cd_referencia_ecf = "+cdReferenciaEcf).executeQuery();
			return rs.next() ? new Result(rs.getInt("cd_documento_saida")) : new Result(-1, "Nenhum documento localizado!");
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return new Result(-1, "Erro ao buscar ultimo documento do ECF", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result update(DocumentoSaida docSaida) {
		return update(docSaida, 0, 0, 0, 0, null);
	}
	
	public static Result update(DocumentoSaida docSaida, Connection connect) {
		return update(docSaida, 0, 0, 0, 0, connect);
	}
	
	public static Result update(DocumentoSaida docSaida, float vlBaseCalculoIcms, float vlIcms, float vlBaseCalculoIcmsSubstituto, float vlIcmsSubstituto) {
		return update(docSaida, vlBaseCalculoIcms, vlIcms, vlBaseCalculoIcmsSubstituto, vlIcmsSubstituto, null);
	}

	public static Result update(DocumentoSaida docSaida, float vlBaseCalculoIcms, float vlIcms, float vlBaseCalculoIcmsSubstituto, float vlIcmsSubstituto, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM alm_balanco_doc_saida " +
															   "WHERE cd_documento_saida = "+docSaida.getCdDocumentoSaida());
			if (pstmt.executeQuery().next()) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ERR_DOC_BALANCO, "Essa saída já faz parte de um balanço!");
			}
			
			//Caso seja uma nota de remessa, coloca as informações proprias desse tipo de venda
			if(docSaida.getTpDocumentoSaida() == TP_NOTA_REMESSA){
				incluirRemessa(docSaida);
			}
			
			//Caso seja uma nota de venda e houver uma viagem vinculada a ela, fará as validações necessárias (Parametrizado para o atacadao)
			else{
				if(ParametroServices.getValorOfParametroAsInteger("LG_PRECO_ITEM_REMESSA_SINCRONIZADO", 0, docSaida.getCdEmpresa())==1){
					incluirVenda(docSaida);
				}
			}
			
			if(DocumentoSaidaDAO.update(docSaida, connect) < 0){
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar remessa!");
			}
			
			// EXCLUI TODOS OS TRIBUTOS E LANÇA NOVAMENTE
			connect.prepareStatement("DELETE FROM adm_saida_tributo " +
					                 "WHERE cd_documento_saida = "+docSaida.getCdDocumentoSaida()).execute();
			/*
			 * Gravando informação do ICMS (Incluindo o substituto
			 */
			if(vlIcms > 0 || vlIcmsSubstituto > 0){
				int cdTributo        = TributoServices.getCdTributoById("ICMS", connect);
				float vlBaseCalculo  = vlBaseCalculoIcms;
				float prAliquota     = 0;
				float vlBaseRetencao = vlBaseCalculoIcmsSubstituto;
				SaidaTributo saiTributo = new SaidaTributo(docSaida.getCdDocumentoSaida(), cdTributo, vlBaseCalculo, prAliquota, vlIcms, vlBaseRetencao, vlIcmsSubstituto);
				if(com.tivic.manager.adm.SaidaTributoDAO.insert(saiTributo, connect) <= 0){
					if (isConnectionNull){
						connect.rollback();
					}
					return new Result(-1);
				}
			}
			
			
			if(isConnectionNull)
				connect.commit();
			Result resultado = new Result(1, "Documento Saída atualizado com sucesso!");
			resultado.addObject("docSaida", docSaida);
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_documento_saida", "" + docSaida.getCdDocumentoSaida(), ItemComparator.EQUAL, Types.INTEGER));
			resultado.addObject("docSaidaRsm", find(criterios));
			return resultado;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return new Result(-1, "Erro ao tentar atualiza dados do documento de saída!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static void gerarNfe(int cdDocSaida){
		/*
		 * Dados gerais da nota
		 */

		/*
		 * Objetos necessários à nf-e
		 */
		DocumentoSaida docSaida        = DocumentoSaidaDAO.get(cdDocSaida);
		PessoaJuridica transportadora  = PessoaJuridicaDAO.get(docSaida.getCdTransportadora());
		PessoaJuridica emitente        = PessoaJuridicaDAO.get(docSaida.getCdEmpresa());
		PessoaJuridica cliente         = PessoaJuridicaDAO.get(docSaida.getCdCliente());
		PessoaEndereco enderecoCliente = PessoaEnderecoDAO.get(1, docSaida.getCdEmpresa());
		try{
			/*
			 * Atributos que possuem o comentário "item obrigatorio" são indispensaveis à nota fiscal,
			 * os demais atributos são opcionais. Todos as variáveis estão declaradas na sequencia exata em que será utilizado na nota;
			 */

			String nrDocSaida=docSaida.getNrDocumentoSaida();//item obrigatorio
			String nrSerie="";//ver depois
			String cnpjBaseRemetente=emitente.getNrCnpj();//item obrigatorio
			if(cnpjBaseRemetente.length()<8){
				//mensagem: é necessário cadastrar cnpj da empresa emitente
			}else{
				cnpjBaseRemetente=emitente.getNrCnpj().substring(0,8);
			}
			String cnpjFilialRemetente=emitente.getNrCnpj();//item obrigatorio
			if(cnpjFilialRemetente.length()<4){
				//mensagem: é necessário cadastrar cnpj da empresa emitente
			}else{
				cnpjFilialRemetente=emitente.getNrCnpj().substring(8,12);
			}
			String digCnpjRemetente=emitente.getNrCnpj();//item obrigatorio
			if(digCnpjRemetente.length()<2){
				//mensagem: é necessário cadastrar cnpj da empresa emitente
			}else{
				digCnpjRemetente=emitente.getNrCnpj().substring(12,14);
			}
			String dtEmissao=Util.formatDate(Util.convCalendarToTimestamp(docSaida.getDtEmissao()),"dd/MM/yyyy");//item obrigatorio
			String inscrEstadual=emitente.getNrInscricaoEstadual();
			String unFederacaoEmitente="BA";//item obrigatorio
			if(unFederacaoEmitente=="BA" && inscrEstadual==""){
				//retornar mensagem para que seja preenchida a inscricao estadual do remetente
			}
			String dtEntradaSaida=Util.formatDate(Util.convCalendarToTimestamp(docSaida.getDtDocumentoSaida()),"dd/MM/yyyy");//item obrigatorio
			String cdNaturezaOperacao=NaturezaOperacaoDAO.get(docSaida.getCdNaturezaOperacao()).getNrCodigoFiscal();//item obrigatorio
			if(cdNaturezaOperacao==""){
				//menagem: necessário preencher a natureza da operação
			}
			if(cdNaturezaOperacao.length()!=4){
				//retornar mensagem para o usuário entrar com um cod de tam = 4
			}
			double vlBaseCalculoIcms=0;//item obrigatorio
			double vlBaseCalculoIcmsSubst=0;//ver depois
			double vlIcms=0;//item Obrigatorio -- ver depois
			double vlIcmsSubst=0;//ver depois
			double somaVlTotalItensNota=DocumentoSaidaItemServices.getSomatorioDosItensNota(docSaida.getCdEmpresa(), docSaida.getCdDocumentoSaida());//item obrigatorio -- Esse campo deverá conter o somatório do valor total de todos os produtos da Nota Fiscal.
			double vlFrete=docSaida.getVlFrete();
			double vlSeguro=docSaida.getVlSeguro();
			double vlDespAdicionais=0;//ver depois
			double vlIpi=0;//ver depois
			double vlSubtotalNota=somaVlTotalItensNota + vlFrete + vlSeguro + vlDespAdicionais + vlIpi + vlIcms;//item obrigatorio
			String inscEstadualTranportador=transportadora!=null?transportadora.getNrInscricaoEstadual():"";
			if(inscEstadualTranportador!="" && unFederacaoEmitente!="BA"){
				//mensagem: este campo só deve ser preenchido por contribuintes da bahia
			}
			String cnpjBaseTransportador=transportadora!=null && transportadora.getNrCnpj()!=null?transportadora.getNrCnpj().substring(0,8):"";
			String cnpjFilialTransportador=transportadora!=null && transportadora.getNrCnpj()!=null?transportadora.getNrCnpj().substring(8,12):"";
			String digCnpjTranportador=emitente==null?"": emitente.getNrCnpj().length()<14?"":emitente.getNrCnpj().substring(12, 14);
			String nmRazaoSocialTranportador=transportadora!=null && transportadora.getNmRazaoSocial()!=null?transportadora.getNmRazaoSocial():"";
			if(cnpjBaseTransportador!="" && nmRazaoSocialTranportador==""){
				//mensagem:necessário digitar a razao social do transportador
			}
			String unFederacaoTransportador="BA";

			if(unFederacaoTransportador!="" && cnpjBaseTransportador==""){
				//mensagem: necessário preecher cnpj do transportador
			}
			String municipioTransportador="";
			if(municipioTransportador!="" && cnpjBaseTransportador==""){
				//mensagem:necessário preencher cnpj transportador
			}
			double vlVolume=0;
			if(vlVolume!=0 && cnpjBaseTransportador==""){
				//mensagem: necessário preencher cnjp transportador
			}
			String descEspecieVolume="";
			if(descEspecieVolume!="" && cnpjBaseTransportador==""){
				//mensagem: necessário preencher cnpj transportador
			}
			double vlPesoBrutoMercadoria=0;
			if(vlPesoBrutoMercadoria!=0 && cnpjBaseTransportador==""){
				//mensagem: necessário preencher cnpj transportador
			}
			double vlPesoLiquidoMercadoria=0;
			if(vlPesoLiquidoMercadoria!=0 && cnpjBaseTransportador==""){
				//mensagem: necessário preencher cnpj transportador
			}
			String nrPlacaVeiculo=docSaida.getNrPlacaVeiculo().toUpperCase();
			if(nrPlacaVeiculo!="" && cnpjBaseTransportador==""){
				//mensagem: necessário preencher cnpj transpordador
			}
			String unFederacaoVeiculo=docSaida.getSgPlacaVeiculo().toUpperCase();
			if(unFederacaoVeiculo!="" && cnpjBaseTransportador==""){
				//mensagem: necessário preencher cnpj transpordador
			}

			String municipioVeiculo="";//(não existe este campo no sistema)
			if(municipioVeiculo!="" && cnpjBaseTransportador==""){
				//mensagem: necessário preencher cnpj transpordador
			}
			String observacoes="";
			int statusFrete=docSaida.getTpFrete()==1||docSaida.getTpFrete()==2?0:1;//item obrigatorio - 1 por conta remetente 0 por conta destinatario
			String nrEmpenho="";//Esse campo deverá ser informado por contribuintes que fornecem para o Estado
			String cnpjBaseDestinatario=cliente!=null && cliente.getNrCnpj()!=null?cliente.getNrCnpj().substring(0,8):"";//item obrigatorio
			if(cnpjBaseDestinatario==""){
				//mensagem: é necessário o cnpj do destinatario
			}
			String cnpjFilialDestinatario=cliente!=null && cliente.getNrCnpj()!=null?cliente.getNrCnpj().substring(8,12):"";//item obrigatorio
			if(cnpjFilialDestinatario==""){
				//mesagem:é necessário digitar o cnpj do destinatario
			}

			String digCnpjDestinatario=cliente!=null && cliente.getNrCnpj()!=null?cliente.getNrCnpj():"";//item obrigatorio
			if(digCnpjDestinatario==""){
				//mensagem:necessário digitar cnpj do destinatario
			}
			String nmRazaoSocialDestinatario=cliente!=null && cliente.getNmRazaoSocial()!=null?cliente.getNmRazaoSocial():"";//item obrigatorio
			if(nmRazaoSocialDestinatario==""){
				//mensagem:necessário digitar razão social do destinatario
			}
			String enderecoDestinatario=enderecoCliente!=null && enderecoCliente.getNmLogradouro()!=null?enderecoCliente.getNmLogradouro():"";//item obrigatorio
			if(enderecoDestinatario==""){
				//mensagem:necessário digitar endereco do destinatario
			}
			String bairroDestinatario=enderecoCliente!=null && enderecoCliente.getNmBairro()!=null?enderecoCliente.getNmBairro():"";//item obrigatorio
			String cepDestinatario=enderecoCliente!=null && enderecoCliente.getNrCep()!=null?enderecoCliente.getNrCep():"";//item obrigatorio
			String dddDestinatario="";
			String foneDestinatario=cliente!=null && cliente.getNrTelefone1()!=null?cliente.getNrTelefone1():"";
			String unFederacaoDestinatario=EstadoDAO.get(CidadeDAO.get(enderecoCliente.getCdCidade()).getCdEstado())!=null?EstadoDAO.get(CidadeDAO.get(enderecoCliente.getCdCidade()).getCdEstado()).getSgEstado():"";//item obrigatorio
			if(unFederacaoDestinatario==""){
				//mensagem: é necessário digitar a unidade de federacao do destinatario
			}
			String municipioDestinatario=CidadeDAO.get(enderecoCliente.getCdCidade()).getNmCidade();//item obrigatorio
			if(municipioDestinatario==""){
				//mensagem: necessário digitar o municipio do destinatario
			}
			double percentualDesconto=0;//quando este campo for informado, o Valor de Desconto não deve ser informado.
			double vlDesconto=0;//quando este campo for informado, o Percentual de Desconto não deve ser informado
			double valorGeralNota=vlSubtotalNota - vlDesconto;//item obrigatorio - este campo foi trocado de ordem com vldeconto
			int statusRequisicaoRecursosConveniados=0;//itens obrigatorio -- 0 :no caso da não utilização desse campo. 1: no caso da aquisição ter sido efetuada com recursos conveniados.
			int cdEsfera=0;// Caso o campo Status da requisição de recursos conveniados tenha o valor 1, este campo deve ser preenchido e ter os seguintes valores: 1- União 2- Estadual 3- Municipal
			int nrConvenio=0;//Caso o campo Código da Esfera tenha o valor 2 (Esfera Estadual), este campo (Número do Convênio) deverá ser  informado e os campos Sigla da Unidade da Federação do Convênio e Descrição do município do convênio devem estar vazios.
			int unFederacaoConvenio=0;//Caso o campo Código da Esfera tenha o valor 3 (Esfera Municipal), este campo (Sigla da Unidade da Federação do Convênio) deverá ser  informado e o campo Número do Convênio deve estar vazio. O Estado informado deve ser BA.
			int municipioConvenio=0;//Caso o campo Código da Esfera tenha o valor 3 (Esfera Municipal), este campo (Descrição do município do convênio) deverá ser  informado e o campo Número do Convênio deve estar vazio. O município informado deve  ser do Estado da Bahia.

			String cabecalho="1|"+nrDocSaida+"|"+nrSerie+"|"+cnpjBaseRemetente+"|"+cnpjFilialRemetente+"|"+digCnpjRemetente+"|"+
						 dtEmissao+"|"+inscrEstadual+"|"+unFederacaoEmitente+"|"+dtEntradaSaida+"|"+cdNaturezaOperacao+"|"+
						 vlBaseCalculoIcms+"|"+(vlBaseCalculoIcmsSubst==0?"":vlBaseCalculoIcmsSubst)+"|"+(vlIcms==0?"":vlIcms)+"|"+
						 (vlIcmsSubst==0?"":vlIcmsSubst)+"|"+somaVlTotalItensNota+"|"+(vlFrete==0?"":vlFrete)+"|"+
						 (vlSeguro==0?"":vlSeguro)+"|"+(vlDespAdicionais==0?"":vlDespAdicionais)+"|"+(vlIpi==0?"":vlIpi)+"|"+vlSubtotalNota+"|"+
						 inscEstadualTranportador+"|"+cnpjBaseTransportador+"|"+cnpjFilialTransportador+"|"+digCnpjTranportador+"|"+nmRazaoSocialTranportador+"|\n"+
						 unFederacaoTransportador+"|"+municipioTransportador+"|"+(vlVolume==0?"":vlVolume)+"|"+descEspecieVolume+"|"+(vlPesoBrutoMercadoria==0?"":vlPesoBrutoMercadoria)+"|"+
						 (vlPesoLiquidoMercadoria==0?"":vlPesoLiquidoMercadoria)+"|"+nrPlacaVeiculo+"|"+unFederacaoVeiculo+"|"+municipioVeiculo+"|"+observacoes+"|"+statusFrete+"|"+
						 nrEmpenho+"|"+cnpjBaseDestinatario+"|"+cnpjFilialDestinatario+"|"+digCnpjDestinatario+"|"+nmRazaoSocialDestinatario+"|"+enderecoDestinatario+"|"+
						 bairroDestinatario+"|"+cepDestinatario+"|"+dddDestinatario+"|"+foneDestinatario+"|"+unFederacaoDestinatario.toUpperCase()+"|"+municipioDestinatario+"|"+
						 (percentualDesconto==0?"":percentualDesconto)+"|"+valorGeralNota+"|"+(vlDesconto==0?"":vlDesconto)+"|"+statusRequisicaoRecursosConveniados+"|"+
						 (cdEsfera==0?"":cdEsfera)+"|"+(nrConvenio==0?"":nrConvenio)+"|"+(unFederacaoConvenio==0?"":unFederacaoConvenio)+"|"+(municipioConvenio==0?"":municipioConvenio)+"|";

			/*
			 * Os seguintes atributos se referem aos itens da nota. Estão na sequencia correta necessária a mesma.
			 * No lugar dos atributos já declarados anteriormente existe um comentário com o nome do mesmo,
			 * foi colocado somente para orientar na hora de gerar o corpo da nota.
			 */
			int nrLinha=2;//este é o contador de linha para dados dos itens


			//String nrDocSaida="";
			//String  nrSerie="";
			//String cnpjBaseRemetente="";
			//String cnpjFilialRemetente="";
			//String digCnpjRemetente="";
			//String dtEmissão="";
			int seqItemNota=1;//sequencia do item na nota fiscal
			int cdProdutoServico=0;//código da mercadoria
			String descMercadoria="";//item obrigatorio
			int cdSituacaoTributaria=0;
			String unMedida="";//item obrigatorio
			int qtSaida=0;//item o ibrigatorio --- quantidade de mercadoria
			double vlProduto=0;//item obrigatorio
			double ipi=0;//percentual da aliquota do ipi
			double icms=0;//percentual da aliquota icms
			double vlDescontoItem=0;
			double percentualDescontoItem=0;
			double vlSubtotal=0;//item obrigatorio
			double vlTotal=0;//item obrigatorio
			String corpoNota="";
			ResultSetMap rsm=DocumentoSaidaItemServices.getAllItensDocumentoSaida(docSaida.getCdEmpresa(), docSaida.getCdDocumentoSaida());

			while(rsm.next()){
				cdProdutoServico=rsm.getInt("cd_produto_servico");
				descMercadoria=ProdutoServicoDAO.get(cdProdutoServico).getNmProdutoServico();
				cdSituacaoTributaria=0;//varia de acordo com o tributo //SaidaItemAliquotaDAO.get(cdProdutoServico, docSaida.getCdDocumentoSaida(), docSaida.getCdEmpresa(), rs.getInt("cd_item"));
				unMedida=UnidadeMedidaDAO.get(rsm.getInt("cd_unidade_medida")).getSgUnidadeMedida();
				qtSaida=rsm.getInt("qt_saida");
				vlProduto=rsm.getInt("vl_unitario");
				ipi=0;//ver depois
				icms=0;//ver depois
				vlDescontoItem=rsm.getDouble("vl_desconto");
				percentualDesconto=0;//se este campo for informado o valor do desconto não precisa ser informado
				vlSubtotal=(qtSaida*vlProduto);
				vlTotal=(vlSubtotal - vlDesconto);
				corpoNota+= nrLinha+"|"+seqItemNota+"|"+nrDocSaida+"|"+nrSerie+"|"+cnpjBaseRemetente+"|"+cnpjFilialRemetente+"|"+digCnpjRemetente+"|"+
				dtEmissao+"|"+seqItemNota+"|"+cdProdutoServico+"|"+descMercadoria+"|"+cdSituacaoTributaria+"|"+unMedida+"|"+qtSaida+"|"+vlProduto+"|"+
				ipi+"|"+icms+"|"+vlDescontoItem+"|"+percentualDescontoItem+"|"+vlSubtotal+"|"+vlTotal+"|\n";
				seqItemNota++;
				nrLinha++;
			}
			System.out.println(corpoNota);
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaItemServices.delete: " +  e);

		}
	}

	public static ResultSetMap getSaidasPorProcesso(int cdProcesso) {
		Connection connect=null;
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			return new ResultSetMap(connect.prepareStatement("SELECT A.* FROM alm_documento_saida A " +
															 "WHERE  A.cd_documento = "+cdProcesso).executeQuery());
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

	public static Result retornaSituacao(int cdDocumentoSaida) {
		Connection connect = Conexao.conectar();
		try {
			return new Result(connect.prepareStatement("UPDATE alm_documento_saida SET st_documento_saida = "+ST_EM_CONFERENCIA+
				                                       " WHERE cd_documento_saida = "+cdDocumentoSaida).executeUpdate());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar voltar situação para Em Aberto!", e);
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getNotaFiscal(int cdDocumentoSaida){
		Connection connect=null;
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, F.nm_pessoa, B.nr_cpf, D.nr_cnpj, E.sg_estado " +
					", CAST (nr_nota_fiscal AS INTEGER) AS numero FROM fsc_nota_fiscal A " + 
					"JOIN fsc_nota_fiscal_doc_vinculado            G ON (G.cd_documento_saida = "+cdDocumentoSaida+" AND A.cd_nota_fiscal = G.cd_nota_fiscal) " +		
					"JOIN grl_pessoa                               C ON (C.cd_pessoa = A.cd_destinatario) " +
					"LEFT OUTER JOIN grl_pessoa_fisica             B ON (B.cd_pessoa = A.cd_destinatario) " +
					"LEFT OUTER JOIN grl_estado                    E ON (B.cd_estado_rg = E.cd_estado) " +
					"LEFT OUTER JOIN grl_pessoa_juridica           D ON (A.cd_destinatario = D.cd_pessoa) " +
					"LEFT OUTER JOIN grl_pessoa			           F ON (A.cd_destinatario = F.cd_pessoa) " +
					"WHERE 1 = 1 ").executeQuery());
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
	
	public static float getValorDocumento(int cdDocumentoSaida) {
		return getValorDocumento(cdDocumentoSaida, null);
	}

	public static float getValorDocumento(int cdDocumentoSaida, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			float valorTotal = getValorAllItens(cdDocumentoSaida, connect);
			
			DocumentoSaida docSaida = DocumentoSaidaDAO.get(cdDocumentoSaida, connect);
			
			valorTotal += docSaida.getVlAcrescimo() - docSaida.getVlDesconto() + docSaida.getVlFrete() + docSaida.getVlSeguro();
			
			//Itera sobre os tributos da nota para saber o que é imposto direto e qual é de icms por substituicao tributaria
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_documento_saida", "" + cdDocumentoSaida, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = SaidaItemAliquotaServices.find(criterios, connect);
			rsm.beforeFirst();
			float vlICMSST = 0;
			float vlICMS = 0;
			float vlPIS = 0;
			float vlCOFINS = 0;
			float vlImpostosDiretos = 0;
			while(rsm.next()){
				Tributo tributo = TributoDAO.get(rsm.getInt("cd_tributo"), connect);
				if(tributo.getIdTributo().equals("ICMS")){
					vlICMSST += rsm.getFloat("vl_retido");
					vlICMS += rsm.getFloat("vl_tributo");	
				}
				else if(tributo.getIdTributo().equals("IPI")){
					vlImpostosDiretos += rsm.getFloat("vl_tributo");					
				}
				else if(tributo.getIdTributo().equals("II")){
					vlImpostosDiretos += rsm.getFloat("vl_tributo");
				}
				else if(tributo.getIdTributo().equals("PIS")){
					vlPIS += rsm.getFloat("vl_tributo");					
				}
				else if(tributo.getIdTributo().equals("COFINS")){
					vlCOFINS += rsm.getFloat("vl_tributo");					
				}
			}
			
			valorTotal += vlICMSST + vlImpostosDiretos;
			
			return valorTotal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaServices.getValorAllItens: " + e);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static float getValorAllItens(int cdDocumentoSaida) {
		return getValorAllItens(cdDocumentoSaida, null);
	}

	public static float getValorAllItens(int cdDocumentoSaida, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			float valorOfProdutos = 0;
			
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement(
					 "SELECT A.* FROM alm_documento_saida_item A " +
					 "WHERE A.cd_documento_saida = ?");
			pstmt.setInt(1, cdDocumentoSaida);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				float qtSaida = rsm.getFloat("qt_saida");
				float vlUnitario = rsm.getFloat("vl_unitario");
				float vlAcrescimo = rsm.getFloat("vl_acrescimo");
				float vlDesconto = rsm.getFloat("vl_desconto");
				
				valorOfProdutos += (qtSaida * vlUnitario) + vlAcrescimo - vlDesconto;
				
			}
			
			return valorOfProdutos;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaServices.getValorAllItens: " + e);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo para buscar a soma de descontos dos itens do documento de saída passado
	 * @param cdDocumentoSaida
	 * @return
	 */
	public static float getValorDescontosAllItens(int cdDocumentoSaida) {
		return getValorDescontosAllItens(cdDocumentoSaida, null);
	}
	public static float getValorDescontosAllItens(int cdDocumentoSaida, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			float valorOfProdutos = 0;
			
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement(
					 "SELECT A.* FROM alm_documento_saida_item A " +
					 "WHERE A.cd_documento_saida = ?");
			pstmt.setInt(1, cdDocumentoSaida);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				float vlDesconto = rsm.getFloat("vl_desconto");
				valorOfProdutos += vlDesconto;
			}
			
			return valorOfProdutos;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaServices.getValorAllItens: " + e);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public Result gerarRelatorioSaldoComplementar(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdTurno){
		
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			dtInicial.set(Calendar.HOUR, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtInicial.set(Calendar.SECOND, 0);
			dtFinal.set(Calendar.HOUR_OF_DAY, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			dtFinal.set(Calendar.SECOND, 59);
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			// Carregando Parametros
			String cdGrupoCombustivel = GrupoServices.getAllCombustivel(cdEmpresa, connection);
			int cdPais = 0, cdEstado = 0, cdCidade = 0;
			ResultSet rs = connection.prepareStatement("SELECT D.cd_cidade, B.cd_estado, C.cd_pais " +
													   "FROM grl_empresa A " +
													   "LEFT OUTER JOIN grl_pessoa_endereco D ON (A.cd_empresa = D.cd_pessoa AND D.lg_principal = 1) " +
												  	   "LEFT OUTER JOIN grl_cidade          B ON (D.cd_cidade = B.cd_cidade) " +
													   "LEFT OUTER JOIN grl_estado          C ON (B.cd_estado = C.cd_estado) " +
													   "WHERE A.cd_empresa = "+cdEmpresa).executeQuery();
			if(rs.next())	{
				cdCidade = rs.getInt("cd_cidade");
				cdEstado = rs.getInt("cd_estado");
				cdPais   = rs.getInt("cd_pais");
			}
			PreparedStatement pstmt = connection.prepareStatement(
					  "SELECT A.cd_produto_servico, B.nm_produto_servico, F.cd_classificacao_fiscal, F.id_classificacao_fiscal, " +
					  "       A.vl_unitario, I.cd_grupo, I.nm_grupo, " +
                      "       SUM(A.vl_unitario * A.qt_saida) AS vl_geral, " +
                      "       SUM(A.vl_desconto - A.vl_acrescimo) AS vl_diferenca, " +
				      // Notas Fiscais Eletrônicas
					  "	     (SELECT SUM((D.vl_unitario * D.qt_saida)-D.vl_desconto+D.vl_acrescimo) " +
					  "  	  FROM alm_documento_saida_item D " +
					  "	 	  JOIN alm_documento_saida      E ON (D.cd_documento_saida = E.cd_documento_saida) " +
					  "	 	  WHERE A.cd_produto_servico  = D.cd_produto_servico " +
					  "			AND A.vl_unitario         = D.vl_unitario" +
					  "			AND E.dt_documento_saida >= ? " +
					  "			AND E.dt_documento_saida <= ? " +
					  (cdTurno >= 0 ? " AND E.cd_turno = "+cdTurno : "")+
					  "         AND E.tp_saida            = "+DocumentoSaidaServices.SAI_VENDA +
					  "			AND E.cd_empresa          = "+cdEmpresa+	
					  "   		AND E.st_documento_saida  = "+DocumentoSaidaServices.ST_CONCLUIDO + 
					  "			AND E.tp_documento_saida  = "+DocumentoSaidaServices.TP_DOC_NAO_FISCAL+
					  "         AND EXISTS (SELECT * FROM fsc_nota_fiscal NF, fsc_nota_fiscal_item NFI " +
					  "                     WHERE NF.st_nota_fiscal      = "+NotaFiscalServices.ST_AUTORIZADA+
					  "                       AND NFI.cd_nota_fiscal     = NF.cd_nota_fiscal " +
					  "                       AND NFI.cd_documento_saida = D.cd_documento_saida" +
					  "                       AND NFI.cd_produto_servico = D.cd_produto_servico" +
					  "                       AND NFI.cd_empresa         = D.cd_empresa" +
					  "                       AND NFI.cd_item_documento  = D.cd_item)) AS vl_nota_fiscal,  " +
					  // Cupons Fiscais e Nota Fiscal D1
					  "	     (SELECT SUM((D.vl_unitario * D.qt_saida)-D.vl_desconto+D.vl_acrescimo)  " +
					  "       FROM alm_documento_saida_item D " +
					  "		  JOIN alm_documento_saida E ON (D.cd_documento_saida = E.cd_documento_saida) " +
					  "		  WHERE A.cd_produto_servico = D.cd_produto_servico " +
					  "			AND A.vl_unitario = D.vl_unitario" +
					  "			AND E.dt_documento_saida >= ? " +
					  "			AND E.dt_documento_saida <= ? " +
					  (cdTurno >= 0 ? " AND E.cd_turno = "+cdTurno : "")+
					  "         AND E.tp_saida            = "+DocumentoSaidaServices.SAI_VENDA +
					  "			AND E.cd_empresa          = "+cdEmpresa+
					  "   		AND E.st_documento_saida  = "+DocumentoSaidaServices.ST_CONCLUIDO + 
					  "			AND E.tp_documento_saida  IN ("+DocumentoSaidaServices.TP_CUPOM_FISCAL+","+DocumentoSaidaServices.TP_NOTA_FISCAL_VENDA+")) AS vl_cupom_fiscal  " +
					  //
					  " FROM alm_documento_saida_item            A  " + 
					  " JOIN grl_produto_servico                 B ON (A.cd_produto_servico      = B.cd_produto_servico)  " + 
					  " JOIN alm_documento_saida                 C ON (A.cd_documento_saida      = C.cd_documento_saida)  " + 
					  " LEFT OUTER JOIN adm_classificacao_fiscal F ON (B.cd_classificacao_fiscal = F.cd_classificacao_fiscal)  " + 
					  " LEFT OUTER JOIN alm_produto_grupo        H ON (A.cd_produto_servico      = H.cd_produto_servico " +
					  "                                            AND lg_principal              = 1)  " + 
					  " LEFT OUTER JOIN alm_grupo       		 I ON (H.cd_grupo                = I.cd_grupo)  " + 
					  " WHERE C.dt_documento_saida >= ? " +
					  "   AND C.dt_documento_saida <= ? " +
					  ((cdTurno >= 0) ? " AND C.cd_turno = "+cdTurno : "") +
					  "   AND C.tp_saida            =   " + DocumentoSaidaServices.SAI_VENDA +
					  "   AND C.cd_empresa          =   " + cdEmpresa +
					  "   AND C.st_documento_saida  =   " + DocumentoSaidaServices.ST_CONCLUIDO +
//					  "   AND (H.cd_grupo IS NULL OR H.cd_grupo NOT IN "+cdGrupoCombustivel+") "+
					  " GROUP BY A.cd_produto_servico, B.nm_produto_servico, F.cd_classificacao_fiscal, F.id_classificacao_fiscal, A.vl_unitario, " +
					  "          I.cd_grupo, I.nm_grupo  " + 
					  " ORDER BY I.nm_grupo, B.nm_produto_servico");
			
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis())); 
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setTimestamp(3, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(4, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setTimestamp(5, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(6, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			rsm.beforeFirst();
			
			// COMBUSTÍVEIS
//			String sql= "SELECT P.cd_produto_servico, P.nm_produto_servico, F.cd_classificacao_fiscal, F.id_classificacao_fiscal, BE.vl_preco AS vl_unitario, " +        
//						"       SUM(BE.qt_litros * BE.vl_preco) AS vl_geral, 	      					" +
//					    // Notas Fiscais Eletrônicas
//						"	   (SELECT SUM((D.vl_unitario * D.qt_saida)-D.vl_desconto+D.vl_acrescimo) " +
//						"  	    FROM alm_documento_saida_item D " +
//						"	 	JOIN alm_documento_saida      E ON (D.cd_documento_saida = E.cd_documento_saida) " +
//						"	 	WHERE D.cd_produto_servico  = P.cd_produto_servico " +
//						"		  AND E.cd_empresa          = "+cdEmpresa+
//						"		  AND E.dt_documento_saida >= ? " +
//						"		  AND E.dt_documento_saida <= ? " +
//						(cdTurno >= 0 ? " AND E.cd_turno = "+cdTurno : "")+
//						"         AND E.tp_saida            = "+DocumentoSaidaServices.SAI_VENDA +
//						"		  AND E.cd_empresa          = "+cdEmpresa+	
//						"   	  AND E.st_documento_saida  = "+DocumentoSaidaServices.ST_CONCLUIDO + 
//						"		  AND E.tp_documento_saida  = "+DocumentoSaidaServices.TP_DOC_NAO_FISCAL+
//						"         AND EXISTS (SELECT * FROM fsc_nota_fiscal NF, fsc_nota_fiscal_item NFI " +
//						"                     WHERE NF.st_nota_fiscal      = "+NotaFiscalServices.ST_AUTORIZADA+
//						"                       AND NFI.cd_nota_fiscal     = NF.cd_nota_fiscal " +
//						"                       AND NFI.cd_documento_saida = D.cd_documento_saida" +
//						"                       AND NFI.cd_produto_servico = D.cd_produto_servico" +
//						"                       AND NFI.cd_empresa         = D.cd_empresa" +
//						"                       AND NFI.cd_item_documento  = D.cd_item)) AS vl_nota_fiscal,  " +
//						// Cupons Fiscais e Nota Fiscal D1
//						"	   (SELECT SUM((D.vl_unitario * D.qt_saida)-D.vl_desconto+D.vl_acrescimo)  " +
//						"       FROM alm_documento_saida_item D " +
//						"		JOIN alm_documento_saida E ON (D.cd_documento_saida = E.cd_documento_saida) " +
//						"	 	WHERE D.cd_produto_servico  = P.cd_produto_servico " +
//						"		  AND E.cd_empresa          = "+cdEmpresa+
//						// "		  AND D.cd_bico             = BE.cd_bico " +
//						"		  AND E.dt_documento_saida >= ? " +
//						"		  AND E.dt_documento_saida <= ? " +
//						(cdTurno >= 0 ? " AND E.cd_turno = "+cdTurno : "")+
//						"         AND E.tp_saida            = "+DocumentoSaidaServices.SAI_VENDA +
//						"   	  AND E.st_documento_saida  = "+DocumentoSaidaServices.ST_CONCLUIDO + 
//						"		  AND E.tp_documento_saida  IN ("+DocumentoSaidaServices.TP_CUPOM_FISCAL+","+DocumentoSaidaServices.TP_NOTA_FISCAL_VENDA+")) AS vl_cupom_fiscal  " +
//						//
//						"FROM pcb_bico_encerrante  BE  " +
//						" JOIN pcb_bico                            B  ON (BE.cd_bico                = B.cd_bico)  " +
//						" JOIN pcb_tanque                          T  ON (B.cd_tanque               = T.cd_tanque)  " +
//						" JOIN adm_conta_fechamento                CF ON (BE.cd_fechamento          = CF.cd_fechamento)  " +
//						" JOIN adm_conta_financeira                C  ON (CF.cd_conta               = C.cd_conta)  " +
//						" JOIN grl_produto_servico                 P  ON (T.cd_produto_servico      = P.cd_produto_servico)  " +
//						" JOIN grl_produto_servico_empresa         PE ON (PE.cd_produto_servico     = P.cd_produto_servico  " +
//						"                                             AND PE.cd_empresa             = C.cd_empresa)  " +
//						" LEFT OUTER JOIN adm_classificacao_fiscal F  ON (P.cd_classificacao_fiscal = F.cd_classificacao_fiscal) " +
//						"WHERE C.cd_empresa         = "+cdEmpresa+
//						"  AND CF.dt_fechamento    >= ? " +
//						"  AND CF.dt_fechamento    <= ? " + (cdTurno > 0 ? " AND CF.cd_turno = "+cdTurno : " ")+
//						"  AND BE.qt_litros > 0 " + 
//						" GROUP BY P.cd_produto_servico, P.nm_produto_servico, F.cd_classificacao_fiscal, F.id_classificacao_fiscal, BE.vl_preco ";
//			String sql = "SELECT A.cd_produto_servico, B.nm_produto_servico, F.cd_classificacao_fiscal, F.id_classificacao_fiscal, " +
//					  "       A.vl_unitario, I.cd_grupo, I.nm_grupo, " +
//                      "       SUM(A.vl_unitario * A.qt_saida) AS vl_geral, " +
//                      "       SUM(A.vl_desconto - A.vl_acrescimo) AS vl_diferenca, " +
//				      // Notas Fiscais Eletrônicas
//					  "	     (SELECT SUM((D.vl_unitario * D.qt_saida)-D.vl_desconto+D.vl_acrescimo) " +
//					  "  	  FROM alm_documento_saida_item D " +
//					  "	 	  JOIN alm_documento_saida      E ON (D.cd_documento_saida = E.cd_documento_saida) " +
//					  "	 	  WHERE A.cd_produto_servico  = D.cd_produto_servico " +
//					  "			AND A.vl_unitario         = D.vl_unitario" +
//					  "			AND E.dt_documento_saida >= ? " +
//					  "			AND E.dt_documento_saida <= ? " +
//					  (cdTurno >= 0 ? " AND E.cd_turno = "+cdTurno : "")+
//					  "         AND E.tp_saida            = "+DocumentoSaidaServices.SAI_VENDA +
//					  "			AND E.cd_empresa          = "+cdEmpresa+	
//					  "   		AND E.st_documento_saida  = "+DocumentoSaidaServices.ST_CONCLUIDO + 
//					  "			AND E.tp_documento_saida  = "+DocumentoSaidaServices.TP_DOC_NAO_FISCAL+
//					  "         AND EXISTS (SELECT * FROM fsc_nota_fiscal NF, fsc_nota_fiscal_item NFI " +
//					  "                     WHERE NF.st_nota_fiscal      = "+NotaFiscalServices.ST_AUTORIZADA+
//					  "                       AND NFI.cd_nota_fiscal     = NF.cd_nota_fiscal " +
//					  "                       AND NFI.cd_documento_saida = D.cd_documento_saida" +
//					  "                       AND NFI.cd_produto_servico = D.cd_produto_servico" +
//					  "                       AND NFI.cd_empresa         = D.cd_empresa" +
//					  "                       AND NFI.cd_item_documento  = D.cd_item)) AS vl_nota_fiscal,  " +
//					  // Cupons Fiscais e Nota Fiscal D1
//					  "	     (SELECT SUM((D.vl_unitario * D.qt_saida)-D.vl_desconto+D.vl_acrescimo)  " +
//					  "       FROM alm_documento_saida_item D " +
//					  "		  JOIN alm_documento_saida E ON (D.cd_documento_saida = E.cd_documento_saida) " +
//					  "		  WHERE A.cd_produto_servico = D.cd_produto_servico " +
//					  "			AND A.vl_unitario = D.vl_unitario" +
//					  "			AND E.dt_documento_saida >= ? " +
//					  "			AND E.dt_documento_saida <= ? " +
//					  (cdTurno >= 0 ? " AND E.cd_turno = "+cdTurno : "")+
//					  "         AND E.tp_saida            = "+DocumentoSaidaServices.SAI_VENDA +
//					  "			AND E.cd_empresa          = "+cdEmpresa+
//					  "   		AND E.st_documento_saida  = "+DocumentoSaidaServices.ST_CONCLUIDO + 
//					  "			AND E.tp_documento_saida  IN ("+DocumentoSaidaServices.TP_CUPOM_FISCAL+","+DocumentoSaidaServices.TP_NOTA_FISCAL_VENDA+")) AS vl_cupom_fiscal  " +
//					  //
//					  " FROM alm_documento_saida_item            A  " + 
//					  " JOIN grl_produto_servico                 B ON (A.cd_produto_servico      = B.cd_produto_servico)  " + 
//					  " JOIN alm_documento_saida                 C ON (A.cd_documento_saida      = C.cd_documento_saida)  " + 
//					  " LEFT OUTER JOIN adm_classificacao_fiscal F ON (B.cd_classificacao_fiscal = F.cd_classificacao_fiscal)  " + 
//					  " LEFT OUTER JOIN alm_produto_grupo        H ON (A.cd_produto_servico      = H.cd_produto_servico " +
//					  "                                            AND lg_principal              = 1)  " + 
//					  " LEFT OUTER JOIN alm_grupo       		 I ON (H.cd_grupo                = I.cd_grupo)  " + 
//					  " WHERE C.dt_documento_saida >= ? " +
//					  "   AND C.dt_documento_saida <= ? " +
//					  ((cdTurno >= 0) ? " AND C.cd_turno = "+cdTurno : "") +
//					  "   AND C.tp_saida            =   " + DocumentoSaidaServices.SAI_VENDA +
//					  "   AND C.cd_empresa          =   " + cdEmpresa +
//					  "   AND C.st_documento_saida  =   " + DocumentoSaidaServices.ST_CONCLUIDO +
//					  "   AND (H.cd_grupo IS NULL OR H.cd_grupo IN "+cdGrupoCombustivel+") "+
//					  " GROUP BY A.cd_produto_servico, B.nm_produto_servico, F.cd_classificacao_fiscal, F.id_classificacao_fiscal, A.vl_unitario, " +
//					  "          I.cd_grupo, I.nm_grupo  " + 
//					  " ORDER BY I.nm_grupo, B.nm_produto_servico";
//			PreparedStatement pstmtComb = connection.prepareStatement(sql);
//			pstmtComb.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis())); 
//			pstmtComb.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
//			pstmtComb.setTimestamp(3, new Timestamp(dtInicial.getTimeInMillis())); 
//			pstmtComb.setTimestamp(4, new Timestamp(dtFinal.getTimeInMillis()));
//			pstmtComb.setTimestamp(5, new Timestamp(dtInicial.getTimeInMillis())); 
//			pstmtComb.setTimestamp(6, new Timestamp(dtFinal.getTimeInMillis()));
//			ResultSetMap rsmComb = new ResultSetMap(pstmtComb.executeQuery());
//			HashMap<String,Object> reg = null; 
//			while(rsmComb.next()) {
//				reg = new HashMap<String, Object>(); 
//				reg.put("CD_PRODUTO_SERVICO",      rsmComb.getInt("cd_produto_servico"));
//				reg.put("NM_PRODUTO_SERVICO",      rsmComb.getString("nm_produto_servico"));
//				reg.put("CD_CLASSIFICACAO_FISCAL", rsmComb.getInt("cd_classificacao_fiscal"));
//				reg.put("ID_CLASSIFICACAO_FISCAL", rsmComb.getString("id_classificacao_fiscal"));
//				reg.put("VL_UNITARIO",             rsmComb.getFloat("vl_unitario"));
//				reg.put("VL_GERAL",                rsmComb.getFloat("vl_geral"));
//				reg.put("Vl_DIFERENCA",        0);
//				reg.put("CD_GRUPO",        ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa));
//				reg.put("NM_GRUPO",        "COMBUSTIVEL");
//				reg.put("VL_NOTA_FISCAL",  rsmComb.getFloat("vl_nota_fiscal"));
//				reg.put("VL_CUPOM_FISCAL", rsmComb.getFloat("vl_cupom_fiscal"));
//				
//				rsm.addRegister(reg);
//			}
			int cdNaturezaOperacaoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_COMBUSTIVEL", 0, cdEmpresa);
			int cdNaturezaOperacaoVarejo      = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_VAREJO", 0, cdEmpresa);
//			rsmComb.beforeFirst();
			rsm.beforeFirst();
			while(rsm.next()){
				int cdNaturezaOperacao = cdNaturezaOperacaoVarejo;
				rsm.setValueToField("DS_GRUPO", rsm.getString("nm_grupo"));
				rsm.setValueToField("VL_NOTA_FISCAL", rsm.getFloat("vl_nota_fiscal") + rsm.getFloat("vl_nota_fiscal_eletronica"));
				// Em caso de Combustível faz calculos diferentes
				if(GrupoServices.getAllCombustivelAsArray(cdEmpresa, connection).contains(rsm.getInt("cd_grupo"))) {
					rsm.setValueToField("DS_GRUPO", "0" + rsm.getString("nm_produto_servico"));
					cdNaturezaOperacao = cdNaturezaOperacaoCombustivel;
				}	
				ResultSetMap rsmAli = new ResultSetMap(connection.prepareStatement(
															"SELECT A.*, B.* " +
															"FROM adm_tributo A, adm_tributo_aliquota B " +
															"WHERE A.cd_tributo  = B.cd_tributo " +
															"  AND B.tp_operacao = "+TributoAliquotaServices.OP_VENDA+
															"  AND EXISTS (SELECT * FROM adm_produto_servico_tributo C " +
															"              WHERE A.cd_tributo  = C.cd_tributo " +
															"                AND B.cd_tributo_aliquota = C.cd_tributo_aliquota " +
															"                AND C.cd_natureza_operacao = "+cdNaturezaOperacao+
															"                AND (C.cd_pais   = "+cdPais+" OR C.cd_pais IS NULL) "+
															"                AND (C.cd_estado = "+cdEstado+" OR C.cd_estado IS NULL) "+
															"                AND (C.cd_cidade = "+cdCidade+" OR C.cd_cidade IS NULL) "+
															"                AND C.cd_classificacao_fiscal = "+rsm.getInt("cd_classificacao_fiscal")+")").executeQuery());
				float prAliquota = 0;
				if(rsmAli.next()){
					prAliquota = rsmAli.getFloat("pr_aliquota");
				}
				rsm.setValueToField("PR_ALIQUOTA", prAliquota); 
				rsm.setValueToField("VL_DIFERENCA", rsm.getFloat("vl_diferenca"));
				rsm.setValueToField("VL_CP_FISCAL", rsm.getFloat("vl_cupom_fiscal"));
				rsm.setValueToField("VL_NOTA_FISCAL", ((rsm.getString("vl_nota_fiscal") == null || rsm.getString("vl_nota_fiscal").equals("0.0")) ? 0 : rsm.getFloat("vl_nota_fiscal")));
				rsm.setValueToField("VL_SALDO", (rsm.getFloat("VL_GERAL") - (rsm.getFloat("VL_NOTA_FISCAL") + rsm.getFloat("VL_CP_FISCAL") + rsm.getFloat("VL_DIFERENCA"))));
			}
			rsm.beforeFirst();
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("DS_GRUPO");
			rsm.orderBy(fields);
			//
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("dtInicial", Util.convCalendarString(dtInicial));
			param.put("dtFinal", Util.convCalendarString(dtFinal));
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			if (isConnectionNull)
				connection.commit();

			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public Result gerarRelatorioVendaProduto(int cdEmpresa, GregorianCalendar dtInicial,GregorianCalendar dtFinal, int cdTurno){
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
		
			dtInicial.set(Calendar.HOUR, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtInicial.set(Calendar.SECOND, 0);
			dtFinal.set(Calendar.HOUR, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			dtFinal.set(Calendar.SECOND, 59);
			//
			int cdGrupoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa);
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			// Carregando Parametros
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.cd_produto_servico, B.nm_produto_servico, SUM(B2.vl_ultimo_custo * A.qt_saida) AS vl_custo, F.cd_classificacao_fiscal, SUM(A.qt_saida) AS qt_saidas, " +
			        "   SUM((A.vl_unitario * A.qt_saida)-A.vl_desconto+A.vl_acrescimo) AS vl_geral, I.cd_grupo, I.nm_grupo, G.pr_aliquota " + 
					"  FROM alm_documento_saida_item          			  A  " + 
					"  JOIN grl_produto_servico                           B ON (A.cd_produto_servico      = B.cd_produto_servico)  " + 
					"  LEFT OUTER JOIN grl_produto_servico_empresa        B2 ON (A.cd_produto_servico     = B2.cd_produto_servico AND B2.cd_empresa = "+cdEmpresa+")  " + 
					"  JOIN alm_documento_saida							  C ON (A.cd_documento_saida      = C.cd_documento_saida)  " + 
					"  LEFT OUTER JOIN adm_classificacao_fiscal		      F ON (B.cd_classificacao_fiscal = F.cd_classificacao_fiscal)  " + 
					"  LEFT OUTER JOIN adm_saida_item_aliquota  		  G ON (A.cd_produto_servico      = G.cd_produto_servico AND A.cd_item AND A.cd_documento_saida = G.cd_documento_saida AND C.cd_empresa = G.cd_empresa)  " + 
					"  LEFT OUTER JOIN alm_produto_grupo       			  H ON (A.cd_produto_servico      = H.cd_produto_servico AND lg_principal = 1)  " + 
					"  LEFT OUTER JOIN alm_grupo       		  			  I ON (H.cd_grupo                = I.cd_grupo)  " + 
					" WHERE C.dt_documento_saida >= ? " +
					"   AND C.dt_documento_saida <= ? " +
					"   AND C.cd_empresa          =   " + cdEmpresa+
					"   AND C.st_documento_saida  = "  + DocumentoSaidaServices.ST_CONCLUIDO + 
					"   AND C.tp_saida            = "  + DocumentoSaidaServices.SAI_VENDA + 
					((cdTurno >= 0) ? " AND cd_turno = "+cdTurno : "") +
					"   		AND (H.cd_grupo IS NULL OR H.cd_grupo <> "+cdGrupoCombustivel+") "+
					" GROUP BY A.cd_produto_servico, B.nm_produto_servico, F.cd_classificacao_fiscal, " +
					"          I.cd_grupo, I.nm_grupo, G.pr_aliquota  " + 
					" ORDER BY nm_grupo, B.nm_produto_servico");
			
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis())); 
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()){
				rsm.setValueToField("QT_VENDA", rsm.getFloat("qt_saidas"));
				rsm.setValueToField("VL_VENDA", rsm.getFloat("vl_geral"));
				rsm.setValueToField("VL_CUSTO", rsm.getFloat("vl_custo"));
				rsm.setValueToField("VL_MARGEM", rsm.getFloat("VL_VENDA") - rsm.getFloat("VL_CUSTO"));
				rsm.setValueToField("PR_MARGEM", rsm.getFloat("VL_MARGEM") / (rsm.getFloat("VL_VENDA") == 0 ? 1 : rsm.getFloat("VL_VENDA")) * 100);
				rsm.setValueToField("DS_GRUPO", rsm.getString("NM_GRUPO")!=null ? rsm.getString("NM_GRUPO") : "SEM GRUPO");
				rsm.setValueToField("DS_SUB_GRUPO", rsm.getString("NM_SUB_GRUPO"));
				rsm.setValueToField("NM_SUB_TOTAL", "Total " + rsm.getString("DS_GRUPO"));
			}
			
			rsm.beforeFirst();
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("DS_GRUPO");
			fields.add("DS_SUB_GRUPO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("dtInicial", Util.convCalendarString(dtInicial));
			param.put("dtFinal", Util.convCalendarString(dtFinal));
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			if (isConnectionNull)
				connection.commit();

			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	
	public Result gerarRelatorioVendaProdutoCompleto(int cdEmpresa, ArrayList<ItemComparator> crt, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		boolean isConnectionNull = true;
		Connection connection = null;
		try {					
			String strDtInicial = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( dtInicial.getTime() );
			String strDtFinal   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( dtFinal.getTime() );
			
			int cdFornecedor = 0;
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			
			for(int l=0; l<crt.size(); l++) {
				if(crt.get(l).getColumn().equalsIgnoreCase("cdFornecedor")) {
					cdFornecedor = Integer.valueOf(crt.get(l).getValue()); 
				} else {
					criterios.add(crt.get(l));
				}
			}
			
//			int cdGrupoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa);
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			// Carregando Parametros

			String sql = " SELECT A.cd_produto_servico, B.nm_produto_servico, SUM(B2.vl_ultimo_custo * A.qt_saida) AS vl_custo, " +
					     " SUM(A.qt_saida) AS QT_SAIDAS, SUM((A.vl_unitario * A.qt_saida)-A.vl_desconto+A.vl_acrescimo) AS VL_GERAL, A.vl_unitario, I.cd_grupo, C.cd_vendedor, " +
					     " I.nm_grupo, J.cd_plano_pagamento as CD_PLANO_PAGAMENTO, J.cd_forma_pagamento, C.tp_documento_saida, C.cd_transportadora," +
					     
					     "( SELECT M.vl_inicial FROM grl_pessoa        N" +
					     "  LEFT OUTER JOIN grl_parametro			   L ON (L.cd_pessoa = N.cd_pessoa AND L.nm_parametro = 'vlComissao') " +
					     "  LEFT OUTER JOIN grl_parametro_valor		   M ON (M.cd_pessoa = N.cd_pessoa AND L.cd_parametro = M.cd_parametro) " +
					     "            WHERE N.cd_pessoa = C.cd_vendedor " +
					     ") AS VL_COMISSAO " +
					     
					     " FROM alm_documento_saida_item               A " +
					     " JOIN grl_produto_servico                    B  ON (A.cd_produto_servico      =  B.cd_produto_servico) " +
					     " LEFT OUTER JOIN grl_produto_servico_empresa B2 ON (A.cd_produto_servico      =  B2.cd_produto_servico AND B2.cd_empresa = " + cdEmpresa + ") " +
					     " LEFT OUTER JOIN alm_documento_saida                    C  ON (A.cd_documento_saida      =  C.cd_documento_saida) " +
					     " LEFT OUTER JOIN grl_pessoa                             N  ON (C.cd_vendedor             =  N.cd_pessoa) " +
					     " LEFT OUTER JOIN adm_classificacao_fiscal    F  ON (B.cd_classificacao_fiscal =  F.cd_classificacao_fiscal) " +
					     " LEFT OUTER JOIN alm_produto_grupo           H  ON (A.cd_produto_servico      =  H.cd_produto_servico) " +
					     " LEFT OUTER JOIN alm_grupo                   I  ON (H.cd_grupo                =  I.cd_grupo) " +
					     " JOIN adm_plano_pagto_documento_saida        J  ON (A.cd_documento_saida      =  J.cd_documento_saida) " +				     
					     " WHERE C.dt_documento_saida >= '" + strDtInicial + "' " + 
						 " AND C.dt_documento_saida   <= '" + strDtFinal + "' " + 
					     " AND C.cd_empresa            =  " + cdEmpresa +
					     " AND (H.cd_grupo IS NULL OR H.cd_grupo NOT IN " + GrupoServices.getAllCombustivel(cdEmpresa, connection) + ") ";
	
			String orderBy = " GROUP BY A.cd_produto_servico, A.vl_unitario, B.nm_produto_servico, F.cd_classificacao_fiscal, I.cd_grupo, I.nm_grupo, J.cd_plano_pagamento, " +
					         " J.cd_forma_pagamento, C.cd_vendedor, C.tp_documento_saida, C.cd_transportadora " +
					         " ORDER BY nm_grupo, B.nm_produto_servico ";

			ResultSetMap rsm = Search.find(sql, orderBy, criterios, connection!=null ? connection : Conexao.conectar(), connection==null);
						
			ResultSetMap rsmFinal = new ResultSetMap();
			
			String[] limit = Util.getLimitAndSkip(1, 0);
			while(rsm.next()){
				String sql2 = "SELECT "+limit[0]+" cd_fornecedor FROM alm_documento_entrada_item A JOIN alm_documento_entrada B ON ( A.cd_documento_entrada = B.cd_documento_entrada ) WHERE A.cd_produto_servico = " + rsm.getInt("cd_produto_servico");
				ResultSetMap rsm2 = Search.find(sql2, "ORDER BY DT_DOCUMENTO_ENTRADA DESC "+limit[1], null, connection!=null ? connection : Conexao.conectar(), connection==null);
				
				rsm.setValueToField("VL_COMISSAO",   (rsm.getString("VL_COMISSAO") == null || rsm.getString("VL_COMISSAO") == "" ? "0.0" :  rsm.getString("VL_COMISSAO")));
				rsm.setValueToField("QT_VENDA",       rsm.getDouble("QT_SAIDAS"));
				rsm.setValueToField("VL_VENDA",       rsm.getDouble("VL_GERAL"));
				rsm.setValueToField("VL_CUSTO",       rsm.getDouble("VL_CUSTO"));
				rsm.setValueToField("DS_GRUPO",       rsm.getString("NM_GRUPO")!=null ? rsm.getString("NM_GRUPO") : "SEM GRUPO");
				rsm.setValueToField("DS_SUB_GRUPO",   rsm.getString("NM_SUB_GRUPO"));
				rsm.setValueToField("NM_SUB_TOTAL",   "Total " + rsm.getString("DS_GRUPO"));
				rsm.setValueToField("VL_COMISSAO_RS", ( (  Util.arredondar(new Double(rsm.getString("VL_GERAL")), 0) * (new Double(rsm.getString("VL_COMISSAO")))  / 100 ) ) );
				
				if(rsm2.next() && cdFornecedor > 0){
					int cdFornecedorEntrada = rsm2.getInt("cd_fornecedor");
					if(cdFornecedor == cdFornecedorEntrada)
						rsmFinal.addRegister(rsm.getRegister());
				}
				else
					rsmFinal.addRegister(rsm.getRegister());
				
			}
			
			rsmFinal.beforeFirst();
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("DS_GRUPO");
			rsmFinal.orderBy(fields);
			rsmFinal.beforeFirst();
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("dtInicial", Util.convCalendarString(dtInicial));
			param.put("dtFinal", Util.convCalendarString(dtFinal));
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsmFinal);
			result.addObject("params", param);
			if (isConnectionNull)
				connection.commit();

			return result;
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public Result gerarRelatorioRentabilidade(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, ArrayList<ItemComparator> criterios){
	
		boolean isConnectionNull = true;
		Connection connection = null;
		
		try {
			
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			if( criterios == null )
				criterios = new ArrayList<ItemComparator>();
			
			criterios.add( new ItemComparator("D.CD_EMPRESA",String.valueOf( cdEmpresa ), ItemComparator.EQUAL, Types.INTEGER) );
			criterios.add( new ItemComparator("C.DT_DOCUMENTO_SAIDA", new SimpleDateFormat("dd/MM/yyyy").format( dtInicial.getTime() ), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP) );
			criterios.add( new ItemComparator("C.DT_DOCUMENTO_SAIDA", new SimpleDateFormat("dd/MM/yyyy").format( dtFinal.getTime() ), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP) );
			criterios.add( new ItemComparator("C.ST_DOCUMENTO_SAIDA", String.valueOf( DocumentoSaidaServices.ST_CONCLUIDO ), ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsm = Search.find(" SELECT  SUM( A.qt_saida ) as qt_vendido, "+
												" 	SUM( A.vl_unitario * A.qt_saida )     AS vl_venda_bruta, "+
												" 	SUM( A.vl_acrescimo - A.vl_desconto ) AS vl_diferenca, "+
												" 	SUM( A.qt_saida * D.vl_ultimo_custo ) AS vl_custo_reposicao, "+
												" B.nm_produto_servico, B.cd_produto_servico, B2.nm_tabela_preco, B2.cd_tabela_preco, "+
												" D.vl_ultimo_custo, D.vl_custo_medio, D.id_reduzido, "+
												" I.cd_grupo, I.nm_grupo, I.cd_grupo_superior, "+
												" I2.nm_grupo as nm_grupo_superior "+
												" FROM alm_documento_saida_item A "+
												" JOIN grl_produto_servico B ON (A.cd_produto_servico = B.cd_produto_servico) "+
												" JOIN adm_tabela_preco B2 ON (A.cd_tabela_preco = B2.cd_tabela_preco) "+
												" LEFT OUTER JOIN grl_produto_servico_empresa D ON ( A.cd_produto_servico = D.cd_produto_servico ) "+
												" JOIN alm_documento_saida C ON (A.cd_documento_saida = C.cd_documento_saida) "+
												" LEFT OUTER JOIN alm_produto_grupo H ON (A.cd_produto_servico = H.cd_produto_servico "+
												"                                        AND lg_principal = 1) "+
												" LEFT OUTER JOIN alm_grupo I ON (H.cd_grupo = I.cd_grupo) "+
												" LEFT OUTER JOIN alm_grupo I2 ON (I.cd_grupo_superior = I2.cd_grupo) "+
												" JOIN adm_conta_receber J ON (C.cd_documento_saida = J.cd_documento_saida) "+
												" WHERE 1=1 ",
												" GROUP BY B.nm_produto_servico, B.cd_produto_servico, "+
												"	 B2.nm_tabela_preco, B2.cd_tabela_preco, "+
												"	 D.vl_ultimo_custo, D.vl_custo_medio, D.id_reduzido, "+
												"	 I.cd_grupo, I.nm_grupo, I.cd_grupo_superior, "+
												"	 I2.cd_grupo, I2.nm_grupo "+
												" ORDER BY I2.cd_grupo, I.cd_grupo, "+
												"	 B.nm_produto_servico, B2.nm_tabela_preco",
												criterios, connection, false);
			

			ArrayList<ItemComparator> crtEncerrantes = new ArrayList<ItemComparator>();
			crtEncerrantes.add( new ItemComparator("E.DT_FECHAMENTO", new SimpleDateFormat("dd/MM/yyyy").format( dtInicial.getTime() ), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP) );
			crtEncerrantes.add( new ItemComparator("E.DT_FECHAMENTO", new SimpleDateFormat("dd/MM/yyyy").format( dtFinal.getTime() ), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP) );
			crtEncerrantes.add( new ItemComparator("DE.CD_EMPRESA", String.valueOf( cdEmpresa ), ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsmEncerrantes = Search.find("SELECT D.cd_produto_servico, D.nm_produto_servico, "+
												 "  DE.vl_ultimo_custo, DE.vl_custo_medio, " +
											     "  SUM(A.qt_litros) AS qt_vendido, " +
											     "  SUM(A.vl_preco * qt_litros) AS vl_venda_bruta, " +
											     "  SUM(DE.vl_ultimo_custo * A.qt_litros) AS vl_custo_reposicao " +
												 " FROM pcb_bico_encerrante A " +
												 " JOIN pcb_bico                    	B   ON (A.cd_bico = B.cd_bico) " +
												 " JOIN pcb_tanque                  	C   ON (B.cd_tanque = C.cd_tanque)  " +
												 " JOIN grl_produto_servico         	D   ON (C.cd_produto_servico = D.cd_produto_servico)  " +
												 " JOIN grl_produto_servico_empresa 	DE  ON (C.cd_produto_servico = DE.cd_produto_servico )  " +
												 " JOIN adm_conta_fechamento            E   ON (A.cd_fechamento = E.cd_fechamento) " +
												 " WHERE 1=1 ", 
												 " GROUP BY D.cd_produto_servico, D.nm_produto_servico, "+
												 " DE.vl_ultimo_custo, DE.vl_custo_medio "+
												 " ORDER BY D.nm_produto_servico ",
												 crtEncerrantes, connection, false);
			float prTotalVlLucroBruto = 0;
			int cdGrupoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa);
			int cdTabelaPreco = ParametroServices.getValorOfParametroAsInteger("CD_TABELA_PRECO_AVISTA", 0, cdEmpresa);
			rsm.beforeFirst();
			rsmEncerrantes.beforeFirst();
			while(rsm.next()){
				if( rsm.getInt("CD_GRUPO_SUPERIOR") == cdGrupoCombustivel ){
					rsm.setValueToField("DS_GRUPO", "0COMBUSTIVEL");
					rsm.setValueToField("NM_SUB_TOTAL", "TOTAL COMBUSTÍVEL");
					while( rsmEncerrantes.next() ){
						if( rsm.getInt("CD_TABELA_PRECO") == cdTabelaPreco && rsm.getInt("CD_PRODUTO_SERVICO") == rsmEncerrantes.getInt("CD_PRODUTO_SERVICO") ){
							rsm.setValueToField( "QT_VENDIDO" , rsm.getDouble("QT_VENDIDO") + ( rsm.getDouble("QT_VENDIDO")-rsmEncerrantes.getDouble("QT_VENDIDO") ) );
							rsm.setValueToField( "VL_VENDA_BRUTA" , rsm.getDouble("VL_VENDA_BRUTA") + ( rsm.getDouble("VL_VENDA_BRUTA")-rsmEncerrantes.getDouble("VL_VENDA_BRUTA") ) );
							rsm.setValueToField( "VL_CUSTO_REPOSICAO" , rsm.getDouble("VL_CUSTO_REPOSICAO") + ( rsm.getDouble("VL_CUSTO_REPOSICAO")-rsmEncerrantes.getDouble("VL_CUSTO_REPOSICAO") ) );
							rsmEncerrantes.deleteRow();
							break;
						}
					}
					rsmEncerrantes.beforeFirst();
				}else{
					rsm.setValueToField("DS_GRUPO", rsm.getString("nm_grupo_superior"));
					rsm.setValueToField("NM_SUB_TOTAL", "TOTAL PRODUTOS");
				}
				rsm.setValueToField("VL_LUCRO_BRUTO", (rsm.getFloat("vl_venda_bruta") + rsm.getFloat("vl_diferenca") - rsm.getFloat("vl_custo_reposicao")));
				prTotalVlLucroBruto += rsm.getFloat("VL_LUCRO_BRUTO");
				rsm.setValueToField("VL_MARGEM", (rsm.getFloat("VL_LUCRO_BRUTO") / ((rsm.getString("QT_VENDIDO") == null || rsm.getString("QT_VENDIDO").equals("") || rsm.getString("QT_VENDIDO").equals("0.0") || rsm.getString("QT_VENDIDO").equals("0")) ? 1 : rsm.getFloat("QT_VENDIDO"))));
				rsm.setValueToField("PR_MARGEM", (((rsm.getFloat("vl_venda_bruta") + rsm.getFloat("vl_diferenca")) / ((rsm.getString("vl_custo_reposicao") == null || rsm.getString("vl_custo_reposicao").equals("") || rsm.getString("vl_custo_reposicao").equals("0") || rsm.getString("vl_custo_reposicao").equals("0.0")) ? 1 : rsm.getFloat("vl_custo_reposicao")))));
				// Informando Percentual de LUCRO
				rsm.setValueToField("PR_LUCRO", ((rsm.getFloat("VL_LUCRO_BRUTO") / prTotalVlLucroBruto) * 100));
				rsm.setValueToField("NM_RECEITAS", rsm.getString("NM_PRODUTO_SERVICO") +"  "+rsm.getString("NM_TABELA_PRECO")  );
				
			}
			rsm.beforeFirst();
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("DS_GRUPO");
			fields.add("DS_SUB_GRUPO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("vlLucroTotal", prTotalVlLucroBruto);
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Result gerarRelatorioExtratoNotas(int cdEmpresa, int cdCliente, int stDocumento, ArrayList<ItemComparator> criterios,  HashMap<String, Object> params){
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			Result resultNotas = new Result(1);
			Empresa empresa = EmpresaDAO.get(cdEmpresa);
			PessoaEndereco enderecoEmpresa = PessoaEnderecoServices.getEnderecoPrincipal( empresa.getCdPessoa() );
			Cidade cidade = CidadeDAO.get( enderecoEmpresa.getCdCidade() );
			Estado estado = EstadoDAO.get( cidade.getCdEstado() );
			String nmCidadeEstado = cidade.getNmCidade() != null? cidade.getNmCidade():"" ;
			nmCidadeEstado += cidade.getNmCidade() != null && estado.getNmEstado()!= null ? " - "+estado.getNmEstado():"" ;
			String dsEndereco = enderecoEmpresa.getDsEndereco() == ""?enderecoEmpresa.getDsEndereco():enderecoEmpresa.getNmLogradouro();
			
			params.put("NR_INSCRICAO_ESTADUAL", empresa.getNrInscricaoEstadual());
			params.put("NR_CNPJ", empresa.getNrCnpj());
			params.put("NR_TELEFONE", empresa.getNrTelefone1());
			params.put("DS_ENDERECO", dsEndereco );
			params.put("NM_BAIRRO", enderecoEmpresa.getNmBairro());
			params.put("NM_CIDADE_ESTADO", nmCidadeEstado);
			params.put("NR_CEP", enderecoEmpresa.getNrCep());
			params.put("NM_EMAIL", empresa.getNmEmail());
			
			Pessoa cliente = PessoaDAO.get(cdCliente);
			
			HashMap<String, Object> clienteEndereco = (HashMap<String, Object>) PessoaEnderecoServices.getEnderecoPrincipalCompleto( cliente.getCdPessoa() ).getObjects().get("PESSOAENDERECO");
			String nrIdentidadeCliente, nrCpfCnpj = "";
			params.put("CD_CLIENTE", cliente.getCdPessoa() );
			params.put("NM_CLIENTE", cliente.getNmPessoa() );
			params.put("NR_CLIENTE", cliente.getNmPessoa() );
			params.put("NR_TELEFONE_CLIENTE", cliente.getNrTelefone1()+" / "+cliente.getNrTelefone2());
			params.put("DS_ENDERECO_CLIENTE", clienteEndereco.get("NM_LOGRADOURO"));
			params.put("NM_BAIRRO_CLIENTE", clienteEndereco.get("NM_BAIRRO"));
			params.put("NM_CIDADE_CLIENTE", clienteEndereco.get("NM_CIDADE"));
			params.put("NM_UF_CLIENTE", clienteEndereco.get("SG_ESTADO"));
			params.put("NR_CEP_CLIENTE", clienteEndereco.get("NR_CEP"));
			
			
			if( cliente.getGnPessoa() ==  0 ){
				PessoaJuridica clienteJuridico = PessoaJuridicaDAO.get(cliente.getCdPessoa());
				nrIdentidadeCliente = clienteJuridico.getNrInscricaoEstadual();
				nrCpfCnpj =  Util.formatCnpj( clienteJuridico.getNrCnpj() ) ;
			}else{
				PessoaFisica clienteFisica = PessoaFisicaDAO.get( cliente.getCdPessoa() );
				nrIdentidadeCliente = clienteFisica.getNrRg();
				nrCpfCnpj =  Util.formatCpf( clienteFisica.getNrCpf() ) ;
			}
			
			params.put("NR_IDENTIDADE_CLIENTE", nrIdentidadeCliente);
			params.put("NR_CPF_CNPJ", nrCpfCnpj);
			
			
			ResultSetMap rsmNotas = Search.find( "SELECT A.*, A2.id_turno, B.*, B3.*, C.*, C2.nm_produto_servico, D.nm_conta "+
												 "FROM alm_documento_saida A  "+
												 "LEFT OUTER JOIN adm_turno A2 ON ( A.cd_turno = A2.cd_turno ) "+
												 "JOIN alm_documento_saida_item B ON ( A.cd_documento_saida = B.cd_documento_saida ) "+
												 "JOIN alm_documento_saida B2 ON ( B.cd_documento_saida = B2.cd_documento_saida ) "+
												 "LEFT OUTER JOIN bpm_referencia B3 ON ( B2.cd_referencia_ecf = B3.cd_referencia ) "+
												 "JOIN grl_produto_servico_empresa C ON ( B.cd_produto_servico = C.cd_produto_servico AND B.cd_empresa = C.cd_empresa ) "+ 
												 "JOIN grl_produto_servico C2 ON ( C.cd_produto_servico = C2.cd_produto_servico )  "+
												 "LEFT OUTER JOIN adm_conta_financeira D ON ( A.cd_conta = D.cd_conta ) ",
											"ORDER BY DT_DOCUMENTO_SAIDA, NR_DOCUMENTO_SAIDA, NM_PRODUTO_SERVICO ",
											criterios,
											connection, false);
			rsmNotas.beforeFirst();
			ResultSetMap rsmResumo = Search.find( "SELECT  SUM(B.QT_SAIDA) as QT_SAIDA, SUM( B.QT_SAIDA*B.VL_UNITARIO ) AS VL_TOTAL,  C2.nm_produto_servico "+
														"FROM alm_documento_saida A  JOIN adm_turno A2 ON ( A.cd_turno = A2.cd_turno ) "+
														"JOIN alm_documento_saida_item B ON ( A.cd_documento_saida = B.cd_documento_saida ) "+
														"JOIN grl_produto_servico_empresa C ON ( B.cd_produto_servico = C.cd_produto_servico AND B.cd_empresa = C.cd_empresa ) "+ 
														"JOIN grl_produto_servico C2 ON ( C.cd_produto_servico = C2.cd_produto_servico )", 
														" GROUP BY NM_PRODUTO_SERVICO ORDER BY NM_PRODUTO_SERVICO ",
											criterios,
											connection, false);
			
			rsmResumo.beforeFirst();
			
			params.put("rsmResumo", rsmResumo.getLines());
			resultNotas.addObject("rsm", rsmNotas);
			resultNotas.addObject("params", params );
			return resultNotas;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public Result gerarRelatorioCartoesCredito(int cdEmpresa, GregorianCalendar dtInicial,GregorianCalendar dtFinal){
		
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			PreparedStatement pstmt = connection.prepareStatement("");  
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("NM_SUB_TOTAL", "TOTAL DO DIA " + Util.convCalendarString(rsm.getGregorianCalendar("dt_deposito")));
			}
			rsm.beforeFirst();
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("dtInicial", Util.convCalendarString(dtInicial));
			param.put("dtFinal", Util.convCalendarString(dtFinal));
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			if (isConnectionNull)
				connection.commit();
	
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public Result gerarRelatorioVendasExternas(int cdEmpresa, int cdViagem){
		return gerarRelatorioVendasExternas(cdEmpresa, null, null, 0, cdViagem, -1, -1, 0, 0, -1, 0, 0, 0, null);
	}
	
	public Result gerarRelatorioVendasExternas(int cdEmpresa, ArrayList<ItemComparator> documentosSaida){
		return gerarRelatorioVendasExternas(cdEmpresa, null, null, 0, 0, -1, -1, 0, 0, -1, 0, 0, 0, documentosSaida);
	}
	
	public Result gerarRelatorioVendasExternas(int cdEmpresa, GregorianCalendar dtInicial,GregorianCalendar dtFinal, 
											   int cdTurno, int cdViagem, int stDocumentoSaida, int tpSaida, int cdTipoOperacao, 
											   int cdCliente, int tpMovimentoEstoque, int cdVendedor, int cdTransportadora, int cdFornecedor, ArrayList<ItemComparator> documentosSaida){
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			String whereIn = "";
			if(documentosSaida != null){
				for (ItemComparator itemComparator : documentosSaida) {
					whereIn += itemComparator.getValue();
					whereIn += ", ";
				}
				if(!"".equals(whereIn ))
					whereIn = whereIn.substring(0, whereIn.lastIndexOf(','));
			}
			if(dtInicial != null){
				dtInicial.set(Calendar.HOUR, 0);
				dtInicial.set(Calendar.MINUTE, 0);
				dtInicial.set(Calendar.SECOND, 0);
			}
			if(dtFinal != null){
				dtFinal.set(Calendar.HOUR, 23);
				dtFinal.set(Calendar.MINUTE, 59);
				dtFinal.set(Calendar.SECOND, 59);
			}
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			// Carregando Parametros
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.nr_documento_saida, D.nm_pessoa AS nm_cliente, EC.nm_cidade AS nm_cidade_cliente, " +
																  " SUM((C.vl_unitario * C.qt_saida)-C.vl_desconto+C.vl_acrescimo) AS vl_vendas, " +
																  "	SUM((C.vl_preco_tabela * C.qt_saida)-C.vl_desconto+C.vl_acrescimo) AS vl_vendas_tabela " + 
																  " FROM alm_documento_saida                 A  " + 
																  " JOIN alm_documento_saida_item            C ON (A.cd_documento_saida      = C.cd_documento_saida)  " +
																  " LEFT OUTER JOIN grl_pessoa                          D ON (A.cd_cliente = D.cd_pessoa) " +
																  " LEFT OUTER JOIN grl_pessoa_endereco                 E ON (A.cd_cliente = E.cd_pessoa " +
																  "														AND E.lg_principal = 1) " +
																  " LEFT OUTER JOIN grl_cidade                          EC ON (EC.cd_cidade = E.cd_cidade) " + 
																  ((cdViagem > 0) ? " JOIN fta_viagem        F ON (F.cd_viagem = "+cdViagem+") " : "") +
																  " WHERE A.tp_documento_saida <> " + DocumentoSaidaServices.TP_NOTA_REMESSA + 
																  ((dtInicial != null) ? "   AND A.dt_documento_saida >= ? " : "")+ 
																  ((dtFinal != null)   ? "   AND A.dt_documento_saida <= ? " : "")+
																  "   AND A.cd_empresa          =   " + cdEmpresa+
																  ((stDocumentoSaida >= 0) ? "   AND A.st_documento_saida  = "  + stDocumentoSaida : "")+ 
																  ((tpSaida >= 0) ? "   AND A.tp_saida            = "  + tpSaida : "") + 
																  ((cdTurno > 0) ? " AND cd_turno = "+cdTurno : "") +
																  ((cdViagem > 0) ? " AND A.cd_viagem = F.cd_viagem " : "") +
																  ((cdTipoOperacao > 0) ? " AND A.cd_tipo_operacao = " + cdTipoOperacao : "") +
																  ((cdCliente > 0) ? " AND A.cd_cliente = " + cdCliente : "") +
																  ((tpMovimentoEstoque >= 0) ? " AND A.tp_movimento_estoque = " + tpMovimentoEstoque : "") +
																  ((cdVendedor > 0) ? " AND A.cd_vendedor = " + cdVendedor : "") +
																  ((cdTransportadora > 0) ? " AND A.cd_transportadora = " + cdTransportadora : "") +
																  ((cdFornecedor > 0) ? " AND A.cd_fornecedor = " + cdFornecedor : "") +
																  (!"".equals(whereIn ) ? " AND A.cd_documento_saida IN ("+ whereIn +")" : " ")+ 
																  " GROUP BY A.nr_documento_saida, D.nm_pessoa, EC.nm_cidade  " + 
																  " ORDER BY EC.nm_cidade, D.nm_pessoa");
			
			if(dtInicial != null){
				pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
				if(dtFinal != null)
					pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			}
			else{
				if(dtFinal != null)
					pstmt.setTimestamp(1, new Timestamp(dtFinal.getTimeInMillis()));
			}
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(rsm.getString("VL_VENDAS_TABELA") == null)
					rsm.setValueToField("VL_VENDAS_TABELA", 0);
				rsm.setValueToField("VL_DIFERENCA", (rsm.getFloat("VL_VENDAS") - rsm.getFloat("VL_VENDAS_TABELA")));
			}
			rsm.beforeFirst();
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("DS_GRUPO");
			fields.add("DS_SUB_GRUPO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			HashMap<String, Object> param = new HashMap<String, Object>();
			Viagem viagem = ViagemDAO.get(cdViagem);
			if(viagem != null){
				if(dtInicial != null)
					param.put("dtInicial", Util.convCalendarString(dtInicial));
				else
					if(viagem.getDtSaida() != null)
						param.put("dtInicial", Util.convCalendarString(viagem.getDtSaida()));
				if(dtFinal != null)
					param.put("dtFinal", Util.convCalendarString(dtFinal));
				else
					if(viagem.getDtChegada() != null)
						param.put("dtFinal", Util.convCalendarString(viagem.getDtChegada()));
			}
			param.put("nmTotal", "Total: " + rsm.getLines().size());
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			if (isConnectionNull)
				connection.commit();

			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public Result gerarRelatorioEmpilhadeira(int cdEmpresa, int cdViagem){
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
		
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			// Carregando Parametros
			PreparedStatement pstmt = connection.prepareStatement("SELECT C.cd_produto_servico, C.nm_produto_servico, D.id_reduzido, F.nm_grupo, LA.nm_local_armazenamento, SUM(A.qt_saida) AS qt_saida FROM alm_documento_saida_item A" +
																"	JOIN alm_documento_saida B ON (A.cd_documento_saida = B.cd_documento_saida) " +
																"	JOIN grl_produto_servico C ON (A.cd_produto_servico = C.cd_produto_servico) " +
																"	JOIN grl_produto_servico_empresa D ON (A.cd_produto_servico = D.cd_produto_servico" +
																"											AND D.cd_empresa = "+cdEmpresa+") " +
																"   LEFT OUTER JOIN alm_local_armazenamento            LA ON (D.cd_local_armazenamento      = LA.cd_local_armazenamento)  " +
																"	JOIN alm_produto_grupo E ON (A.cd_produto_servico = E.cd_produto_servico" +
																"											AND E.lg_principal = 1 " + 
																" 										    AND E.cd_empresa = "+cdEmpresa+") " +
																"	JOIN alm_grupo F ON (E.cd_grupo = F.cd_grupo) " +
																"  WHERE B.cd_viagem  = " + cdViagem +
																"    AND B.cd_empresa = " + cdEmpresa + 
																"	 AND B.tp_documento_saida <> " + DocumentoSaidaServices.TP_NOTA_REMESSA +
																"  GROUP BY C.cd_produto_servico, C.nm_produto_servico, D.id_reduzido, F.nm_grupo, LA.nm_local_armazenamento");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			HashMap<String, Object> param = new HashMap<String, Object>();
			
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			if (isConnectionNull)
				connection.commit();

			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Cria um relatório de empilhadeira a partir dos documentos de saída selecionados nos relatórios de vendas
	 * 
	 * @param cdEmpresa
	 * @param documentosSaida
	 * @return
	 * @author Luiz Romario Filho
	 * @since 01/12/2014
	 */
	public Result gerarRelatorioEmpilhadeiraFromSaida(int cdEmpresa, ArrayList<ItemComparator> documentosSaida){
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			String whereIn = "";
			for (ItemComparator itemComparator : documentosSaida) {
				whereIn += itemComparator.getValue();
				whereIn += ", ";
			}
			if(!"".equals(whereIn ))
				whereIn = whereIn.substring(0, whereIn.lastIndexOf(','));
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			PreparedStatement pstmt = connection.prepareStatement("SELECT C.cd_produto_servico, C.nm_produto_servico, F.id_reduzido, E.nm_grupo, H.nm_local_armazenamento, SUM(B.qt_saida) AS qt_saida "
																 	+ " FROM alm_documento_saida A " 
																 	+ " LEFT JOIN alm_documento_saida_item B ON (B.cd_documento_saida = A.cd_documento_saida AND B.cd_empresa = A.cd_empresa ) "
																 	+ " LEFT JOIN grl_produto_servico  C ON (B.cd_produto_servico = C.cd_produto_servico) "
																 	+ " LEFT JOIN alm_produto_grupo D ON (D.cd_produto_servico = B.cd_produto_servico AND D.cd_empresa = A.cd_empresa) "
																 	+ " LEFT JOIN alm_grupo E ON (E.cd_grupo = D.cd_grupo) "
																 	+ " LEFT JOIN grl_produto_servico_empresa F ON (F.cd_produto_servico = B.cd_produto_servico AND F.cd_empresa = A.cd_empresa) "
																 	+ " LEFT JOIN alm_saida_local_item G ON (G.cd_produto_servico = B.cd_produto_servico AND G.cd_documento_saida = A.cd_documento_saida AND A.cd_empresa = G.cd_empresa)"
																 	+ " LEFT JOIN alm_local_armazenamento H ON (H.cd_local_armazenamento = G.cd_local_armazenamento) "
																 	+ " WHERE A.cd_empresa = " + cdEmpresa
																	+ (!"".equals(whereIn ) ? " AND A.cd_documento_saida IN ("+ whereIn +")" : " ")
																	+ " GROUP BY C.cd_produto_servico, C.nm_produto_servico, F.id_reduzido, E.nm_grupo, H.nm_local_armazenamento "
																	+ " ORDER BY C.nm_produto_servico"
															 	);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			HashMap<String, Object> param = new HashMap<String, Object>();
			
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			if (isConnectionNull)
				connection.commit();

			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	
	/**
	 * Vincula uma nota de remessa (Documento de Saída) a uma viagem. No jsp o objeto documentoSaida já tem seu atributo de cdViagem atualizado
	 * @since 29/07/2014
	 * @author Gabriel
	 * @param documentoSaida
	 * @return
	 */
	public static void incluirRemessa(DocumentoSaida documentoSaida){
		documentoSaida.setTpDocumentoSaida(TP_NOTA_REMESSA);
		documentoSaida.setCdNaturezaOperacao(ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_REMESSA", 0));
		documentoSaida.setCdCliente(documentoSaida.getCdEmpresa());
		documentoSaida.setTpMovimentoEstoque(MOV_ESTOQUE_CONSIGNADO);
		documentoSaida.setTpSaida(SAI_VENDA_CONSIGNACAO);
		
		Viagem viagem = ViagemDAO.get(documentoSaida.getCdViagem());
		viagem.setStViagem(ViagemServices.ST_EM_VIAGEM);
		ViagemDAO.update(viagem);
	}
	
	/**
	 * Vincula uma nota de venda (Documento de Saída) a uma viagem. No jsp o objeto documentoSaida já tem seu atributo de cdViagem atualizado
	 * Modifica os valores unitarios das vendas baseado na nota de remessa vinculada a viagem
	 * @since 29/07/2014
	 * @author Gabriel
	 * @param documentoSaida
	 * @return
	 */
	public static Result incluirVenda(DocumentoSaida documentoSaida){
		return incluirVenda(documentoSaida, null);
	}
	public static Result incluirVenda(DocumentoSaida documentoSaida, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
		
			//
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			int cdViagem = documentoSaida.getCdViagem();
			
			if(cdViagem == 0){
				return new Result(1, "Documento não pertence a uma viagem externa");
			}
			
			//Testando para saber se esse documento já esta incluido em outra viagem
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_viagem", "" + cdViagem, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_documento_saida", "" + TP_NOTA_REMESSA, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = DocumentoSaidaDAO.find(criterios);
			
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_viagem", "" + cdViagem, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("tp_documento_saida", "" + TP_NOTA_REMESSA, ItemComparator.EQUAL, Types.INTEGER));
			rsm = DocumentoSaidaDAO.find(criterios);
			
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_documento_saida_item A SET vl_unitario = (SELECT B.vl_unitario FROM alm_documento_saida_item B" +
					"																										WHERE B.cd_documento_saida = ?" +
					"																										  AND B.cd_produto_servico = A.cd_produto_servico)" +
					"											WHERE A.cd_documento_saida = ?");
			
			float vlTotalProduto = 0;
			if(rsm.next()){
				pstmt.setInt(1, rsm.getInt("cd_documento_saida"));
				pstmt.setInt(2, documentoSaida.getCdDocumentoSaida());
				if(pstmt.executeUpdate() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Falha ao atualizar documento de venda");
				}
			}
			ResultSetMap rsmItens = DocumentoSaidaServices.getAllItens(documentoSaida.getCdDocumentoSaida(), connect);
			while(rsmItens.next()){
				vlTotalProduto += rsmItens.getFloat("vl_unitario") * rsmItens.getFloat("qt_saida") + rsmItens.getFloat("vl_acrescimo") - rsmItens.getFloat("vl_desconto");
			}
			float vlTributosDiretos = 0; 
			ResultSetMap rsmTributos = new ResultSetMap(connect.prepareStatement("SELECT * FROM adm_saida_tributo A, adm_tributo B " +
																                    "WHERE A.cd_tributo           = B.cd_tributo " +
																                    "  AND A.cd_documento_saida = "+documentoSaida.getCdDocumentoSaida()).executeQuery());
			while(rsmTributos.next())	{
				if(rsmTributos.getInt("tp_cobranca")==1/*Direta*/)
					vlTributosDiretos += rsmTributos.getFloat("vl_tributo");
				//
				vlTributosDiretos += rsmTributos.getFloat("vl_retido");
			}
			
			documentoSaida.setVlTotalItens(vlTotalProduto);
			documentoSaida.setVlTotalDocumento(vlTotalProduto + documentoSaida.getVlAcrescimo() - documentoSaida.getVlDesconto() + documentoSaida.getVlFrete() + documentoSaida.getVlSeguro() + vlTributosDiretos);
			
			Viagem viagem = ViagemDAO.get(cdViagem, connect);
			viagem.setStViagem(ViagemServices.ST_EM_VIAGEM);
			if(ViagemDAO.update(viagem, connect) < 0){
				return new Result(-1, "Falha ao atualizar a viagem");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1);
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connect);
			return new Result(-1);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public Result gerarRelatorioDocSaida(ArrayList<ItemComparator> crt){
		
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
						
			ResultSetMap rsm = find(crt);
			rsm.beforeFirst();

			while(rsm.next()){
				rsm.setValueToField("VL_TOTAL_DOCUMENTO", String.valueOf(rsm.getDouble("VL_TOTAL_DOCUMENTO")));
				rsm.setValueToField("VL_DESCONTO",  String.valueOf(rsm.getDouble("VL_DESCONTO")));
				rsm.setValueToField("VL_ACRESCIMO", String.valueOf(rsm.getDouble("VL_ACRESCIMO")));
				rsm.setValueToField("NM_ST_DOCUMENTO", situacoes[rsm.getInt("ST_DOCUMENTO")]);
			}
			
			HashMap<String, Object> param = new HashMap<String, Object>();
			
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Busca o documento de saída e retorna um ResultSetMap
	 * @since 30/07/2014
	 * @author Gabriel
	 * @param cdDocumentoSaida
	 * @return
	 */
	public static ResultSetMap get(int cdDocumentoSaida){
		return getDocumentoSaida(cdDocumentoSaida, null);
	}
	public static ResultSetMap get(int cdDocumentoSaida, Connection connection){
		return getDocumentoSaida(cdDocumentoSaida, connection);
	}
	
	public static ResultSetMap getDocumentoSaida(int cdDocumentoSaida, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
		
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.*, B.nm_pessoa as nm_cliente FROM alm_documento_saida A " +
					"LEFT OUTER JOIN grl_pessoa B ON (A.cd_cliente = B.cd_pessoa) " +
					"WHERE cd_documento_saida = ? ");
			pstmt.setInt(1, cdDocumentoSaida);
			return new ResultSetMap(pstmt.executeQuery());
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
	
}