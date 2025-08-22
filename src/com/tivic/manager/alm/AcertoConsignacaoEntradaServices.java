package com.tivic.manager.alm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.NumeracaoDocumentoServices;
import com.tivic.manager.grl.ProdutoServicoEmpresa;
import com.tivic.manager.grl.ProdutoServicoEmpresaDAO;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class AcertoConsignacaoEntradaServices {

	public static final int ST_PENTENTE = 0;
	public static final int ST_CONCLUIDO = 1;

	public static final String[] situacoes = {"Pendente", "Concluído"};

	public static final int TP_ACERTO_FINANCEIRO = 0;
	public static final int TP_DEVOLUCAO = 1;

	public static final String[] tiposAcertos = {"Acerto Financeiro", "Devolução"};

	public static final int ERRO_CONFIRMACAO_DESCONHECIDO = -1;
	public static final int ERRO_CONFIRMACAO_ACERTO_NAO_PENDENTE = -2;
	public static final int ERRO_CONFIRMACAO_SALDO_INSUFICIENTE = -3;

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, 0, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, int cdEmpresa) {
		return findCompleto(criterios, cdEmpresa, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, int cdEmpresa, Connection connect) {
		int cdProdutoServico = -1;
		if (cdEmpresa > 0) {
			if (criterios==null)
				criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_empresa", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
		}
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("cd_produto_servico")) {
				cdProdutoServico = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
				criterios.remove(i);
				i--;
				break;
			}
		}
		ResultSetMap rsm = Search.find("SELECT A.*, B.nm_pessoa AS nm_fornecedor " +
				"FROM alm_acerto_consignacao_entrada A " +
				"JOIN grl_pessoa B ON (A.cd_fornecedor = B.cd_pessoa) " +
				"WHERE 1=1 " + (cdProdutoServico > 0 ? " AND " + cdProdutoServico + " IN (SELECT cd_produto_servico " +
				"					FROM alm_acerto_consignacao_ent_item C " +
				"					WHERE A.cd_acerto_consignacao = C.cd_acerto_consignacao)" : ""), criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
		if (rsm != null) {
			while (rsm.next()) {
				rsm.getRegister().put("DS_ST_ACERTO",situacoes[rsm.getInt("st_acerto")]);
				rsm.getRegister().put("DS_TP_ACERTO", tiposAcertos[rsm.getInt("tp_acerto")]);
			}
			rsm.beforeFirst();
		}
		return rsm;
	}

	public static ResultSetMap getAllItens(int cdAcertoConsignacao) {
		return getAllItens(cdAcertoConsignacao, null);
	}

	public static ResultSetMap getAllItens(int cdAcertoConsignacao, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, C.id_produto_servico, C.txt_produto_servico, C.tp_produto_servico, " +
					"C.nm_produto_servico, C.sg_produto_servico, B.st_produto_empresa, B.cd_unidade_medida, E.nm_unidade_medida, " +
					"E.sg_unidade_medida " +
					"FROM alm_acerto_consignacao_ent_item A " +
					"JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico = B.cd_produto_servico AND A.cd_empresa = B.cd_empresa) " +
					"JOIN grl_produto_servico C ON (B.cd_produto_servico = C.cd_produto_servico) " +
					"JOIN grl_produto D ON (C.cd_produto_servico = D.cd_produto_servico) " +
					"LEFT OUTER JOIN grl_unidade_medida E ON (B.cd_unidade_medida = E.cd_unidade_medida) " +
					"WHERE A.cd_acerto_consignacao = ?");
			pstmt.setInt(1, cdAcertoConsignacao);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntradaServices.getAllItens: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static HashMap<String,Object> confirmarAcertoConsignacao(int cdAcertoConsignacao, int cdLocalArmazenamento) {
		return confirmarAcertoConsignacao(cdAcertoConsignacao, cdLocalArmazenamento, null);
	}

	public static HashMap<String,Object> confirmarAcertoConsignacao(int cdAcertoConsignacao, int cdLocalArmazenamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		HashMap<String,Object> retornoConfirmacao = new HashMap<String,Object>();
		int cdDocumentoEntrada = 0;
		int cdDocumentoSaida = 0;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			AcertoConsignacaoEntrada acerto = AcertoConsignacaoEntradaDAO.get(cdAcertoConsignacao, connection);
			if (acerto==null || acerto.getStAcerto() != ST_PENTENTE)
				retornoConfirmacao.put("resultado", new Integer(acerto==null ? ERRO_CONFIRMACAO_DESCONHECIDO : ERRO_CONFIRMACAO_ACERTO_NAO_PENDENTE));
			else {
				/* verifica se existe saldo disponivel para acerto de consignacao */
				PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, D.nm_produto_servico, " +
						"	(SELECT SUM(G.qt_entrada_consignada) " +
						"	 FROM alm_documento_entrada_item E, alm_documento_entrada F, alm_entrada_local_item G " +
						"	 WHERE E.cd_documento_entrada = F.cd_documento_entrada " +
						"	   AND E.cd_produto_servico = A.cd_produto_servico " +
						"	   AND E.cd_documento_entrada = G.cd_documento_entrada " +
						"	   AND E.cd_produto_servico = G.cd_produto_servico " +
						"	   AND E.cd_empresa = G.cd_empresa " +
						"	   AND E.cd_empresa = ? " +
						"	   AND F.tp_entrada = " + DocumentoEntradaServices.ENT_CONSIGNACAO + " " +
						"      AND F.cd_fornecedor = ?) AS qt_entrada_consignacao, " +
						"	(SELECT SUM(E.qt_item) " +
						"	 FROM alm_acerto_consignacao_ent_item E, alm_acerto_consignacao_entrada F " +
						"	 WHERE E.cd_acerto_consignacao = F.cd_acerto_consignacao " +
						"	   AND E.cd_produto_servico = A.cd_produto_servico " +
						"	   AND E.cd_empresa = ? " +
						"	   AND F.st_acerto = 1 " +
						"	   AND F.cd_fornecedor = ?) AS qt_acerto_consignacao," +
						"	(SELECT SUM(G.qt_entrada_consignada) " +
						"	 FROM alm_entrada_local_item G, alm_documento_entrada H " +
						"	 WHERE G.cd_documento_entrada = H.cd_documento_entrada " +
						"	   AND G.cd_empresa = H.cd_empresa " +
						(acerto.getTpAcerto()==TP_DEVOLUCAO ? "	   AND G.cd_local_armazenamento = ? " : "") +
						"	   AND H.cd_empresa = ? " +
						"	   AND G.cd_produto_servico = A.cd_produto_servico) AS qt_entrada_estoque_consignacao," +
						"	(SELECT SUM(G.qt_saida_consignada) " +
						"	 FROM alm_saida_local_item G, alm_documento_saida H " +
						"	 WHERE G.cd_documento_saida = H.cd_documento_saida " +
						"	   AND G.cd_empresa = H.cd_empresa " +
						(acerto.getTpAcerto()==TP_DEVOLUCAO ? "	   AND G.cd_local_armazenamento = ? " : "") +
						"	   AND H.cd_empresa = ?) AS qt_saida_estoque_consignacao " +
						"FROM alm_acerto_consignacao_ent_item A, grl_produto_servico_empresa B, grl_produto C, grl_produto_servico D " +
						"WHERE A.cd_empresa = B.cd_empresa " +
						"  AND A.cd_produto_servico = B.cd_produto_servico " +
						"  AND B.cd_produto_servico = C.cd_produto_servico " +
						"  AND C.cd_produto_servico = D.cd_produto_servico " +
						"  AND A.cd_acerto_consignacao = ?");
				pstmt.setInt(1, acerto.getCdEmpresa());
				pstmt.setInt(2, acerto.getCdFornecedor());
				pstmt.setInt(3, acerto.getCdEmpresa());
				pstmt.setInt(4, acerto.getCdFornecedor());
				int i = 5;
				if (acerto.getTpAcerto() == TP_DEVOLUCAO)
					pstmt.setInt(i++, cdLocalArmazenamento);
				pstmt.setInt(i++, acerto.getCdEmpresa());
				if (acerto.getTpAcerto() == TP_DEVOLUCAO)
					pstmt.setInt(i++, cdLocalArmazenamento);
				pstmt.setInt(i++, acerto.getCdEmpresa());
				pstmt.setInt(i++, cdAcertoConsignacao);

				boolean isSaldoInsuficiente = false;
				boolean isEstoqueInsuficiente = false;
				ResultSetMap rsmItens = new ResultSetMap(pstmt.executeQuery());
				ResultSetMap rsmItensTemp = new ResultSetMap();
				for (i=0; i<rsmItens.size(); i++) {
					rsmItens.goTo(i);
					if (rsmItens.getFloat("qt_item") > rsmItens.getFloat("qt_entrada_consignacao") - rsmItens.getFloat("qt_acerto_consignacao")) {
						rsmItens.getRegister().put("ST_ITEM", "Saldo a acertar insuficiente");
						isSaldoInsuficiente = true;
					}
					else if (acerto.getTpAcerto() == TP_DEVOLUCAO && rsmItens.getFloat("qt_entrada_estoque_consignacao") - rsmItens.getFloat("qt_saida_estoque_consignacao") < rsmItens.getFloat("qt_item")) {
						rsmItens.getRegister().put("ST_ITEM", "Estoque consignado insuficiente");
						isEstoqueInsuficiente = true;
					}
					else {
						rsmItensTemp.getLines().add(rsmItens.getLines().remove(i));
						i--;
					}
				}

				if (isSaldoInsuficiente || isEstoqueInsuficiente) {
					retornoConfirmacao.put("resultado", new Integer(ERRO_CONFIRMACAO_SALDO_INSUFICIENTE));
					retornoConfirmacao.put("resultset", rsmItens);
					return retornoConfirmacao;
				}
				else {
					/* acerto - tipo financeiro */
					if (acerto.getTpAcerto() == TP_ACERTO_FINANCEIRO) {
						/* cria entrada (documento fiscal) */
						DocumentoEntrada entrada = new DocumentoEntrada(0 /*cdDocumentoEntrada*/,
								acerto.getCdEmpresa(),
								0 /*cdTransportadora*/,
								acerto.getCdFornecedor(),
								null /*dtEmissao*/,
								new GregorianCalendar() /*dtDocumentoEntrada*/,
								DocumentoEntradaServices.ST_EM_ABERTO /*stDocumentoEntrada*/,
								0 /*vlDesconto*/,
								0 /*vlAcrescimo*/,
								"AC" + acerto.getIdAcertoConsignacao()/*nrDocumentoEntrada*/,
								DocumentoEntradaServices.TP_NOTA_FISCAL /*tpDocumentoEntrada*/,
								"" /*nrConhecimento*/,
								DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO /*tpEntrada*/,
								"Acerto de Consignação (referência: " + acerto.getIdAcertoConsignacao() + ")" /*txtObservacao*/,
								0 /*cdNaturezaOperacao*/,
								DocumentoEntradaServices.FRT_CIF /*tpFrete*/,
								"" /*nrPlacaVeiculo*/,
								"" /*sgPlacaVeeiculo*/,
								0 /*qtVolumes*/,
								null /*dtSaidaTransportadora*/,
								"" /*dsViaTransporte*/,
								"" /*txtCorpoNotaFiscal*/,
								0 /*vlPresoBruto*/,
								0 /*vlPesoLiquido*/,
								"" /*dsEpescieVolumes*/,
								"" /*dsMarcaVolumes*/,
								"" /*nrVolumes*/,
								DocumentoEntradaServices.MOV_NENHUM /*tpMovimentoEstoque*/,
								0 /*cdMoeda*/,
								0 /*cdTabelaPreco*/,
								0 /*vlTotalDocumento*/,
								0 /*cdDocumentoSaidaOrigem*/,
								0 /*vlFrete*/,
								0 /*vlSeguro*/,
								0 /*cdDigitador*/,
								0 /*vlTotalItens*/, 
								1 /*nrSerie*/);
						if ((cdDocumentoEntrada = DocumentoEntradaDAO.insert(entrada, connection)) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							retornoConfirmacao.put("resultado", new Integer(ERRO_CONFIRMACAO_DESCONHECIDO));
							return retornoConfirmacao;
						}

						/* acrescenta itens na entrada */
						rsmItensTemp.beforeFirst();
						while (rsmItensTemp.next()) {
							DocumentoEntradaItem item = new DocumentoEntradaItem(cdDocumentoEntrada, rsmItensTemp.getInt("cd_produto_servico"), acerto.getCdEmpresa(), rsmItensTemp.getInt("cd_item"), rsmItensTemp.getFloat("qt_item"), rsmItensTemp.getFloat("vl_item"), 0, 0, 0, null);
							if (DocumentoEntradaItemDAO.insert(item, connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								retornoConfirmacao.put("resultado", new Integer(ERRO_CONFIRMACAO_DESCONHECIDO));
								return retornoConfirmacao;
							}
						}

						/* libera entrada */
						if (DocumentoEntradaServices.liberarEntrada(cdDocumentoEntrada, 0, connection).getCode() <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							retornoConfirmacao.put("resultado", new Integer(ERRO_CONFIRMACAO_DESCONHECIDO));
							return retornoConfirmacao;
						}
					}
					/* acerto - devolucao */
					else {
						/* cria saida (devolucao) */
						DocumentoSaida saida = new DocumentoSaida(0 /*cdDocumentoSaida*/,
								0 /*cdTransportadora*/,
								acerto.getCdEmpresa(),
								acerto.getCdFornecedor() /*cdCliente*/,
								new GregorianCalendar() /*dtDocumentoSaida*/,
								DocumentoSaidaServices.ST_EM_CONFERENCIA /*stDocumentoSaida*/,
								"AC" + acerto.getIdAcertoConsignacao() /*nrDocumentoSaida*/,
								DocumentoSaidaServices.TP_NOTA_DEVOLUCAO /*tpDocumentoSaida*/,
								DocumentoSaidaServices.SAI_DEVOLUCAO /*tpSaida*/,
								"" /*nrConhecimento*/,
								0 /*vlDesconto*/,
								0 /*vlAcrescimo*/,
								null /*dtEmissao*/,
								DocumentoSaidaServices.FRT_CIF /*tpFrete*/,
								"" /*txtMensagem*/,
								"Acerto de Consignação - Devolução (referência: " + acerto.getIdAcertoConsignacao() + ")" /*txtObservacao*/,
								"" /*nrPlacaVeiculo*/,
								"" /*sgPlacaVeiculo*/,
								"" /*nrVolumes*/,
								null /*dtSaidaTransportadora*/,
								"" /*dsViaTransporte*/,
								0 /*cdNaturezaOperacao*/,
								"" /*txtCorpoNotaFiscal*/,
								0 /*vlPesoLiquido*/,
								0 /*vlPesoBruto*/,
								"" /*dsEspecieVolumes*/,
								"" /*dsMarcaVolumes*/,
								0 /*qtVolumes*/,
								DocumentoSaidaServices.MOV_ESTOQUE_CONSIGNADO /*tpMovimentoEstoque*/,
								0 /*cdVendedor*/,
								0 /*cdMoeda*/,
								0 /*cdReferenciaEfc*/,
								0 /*cdSolicitacaoMaterial*/,
								0 /*cdTipoOperacao*/,
								0 /*vlTotalDocumento*/,
								0 /*cdContrato*/,
								0 /*vlFrete*/,
								0 /*vlSeguro*/,
								0 /*cdDigitador*/,
								0 /*cdDocumento*/,
								0 /*cdConta*/,
								0/*cdTurno*/,
								0/*vlTotalItens*/, 1 /*nrSerie*/);
						if ((cdDocumentoSaida = DocumentoSaidaDAO.insert(saida, connection)) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							retornoConfirmacao.put("resultado", new Integer(ERRO_CONFIRMACAO_DESCONHECIDO));
							return retornoConfirmacao;
						}

						/* acrescenta itens na saida */
						rsmItensTemp.beforeFirst();
						while (rsmItensTemp.next()) {
							ProdutoServicoEmpresa produto = ProdutoServicoEmpresaDAO.get(acerto.getCdEmpresa(), rsmItensTemp.getInt("cd_produto_servico"), connection);
							DocumentoSaidaItem item = new DocumentoSaidaItem(cdDocumentoSaida,
									rsmItensTemp.getInt("cd_produto_servico"),
									acerto.getCdEmpresa(),
									rsmItensTemp.getFloat("qt_item"),
									rsmItensTemp.getFloat("vl_item"),
									0 /*vlAcrescimo*/,
									0 /*vlDesconto*/,
									null /*dtEntregaPrevista*/,
									produto.getCdUnidadeMedida(),
									0 /*cdTabelaPreco*/,
									0 /*cdItem*/,
									0/*cdBico*/);
							if (DocumentoSaidaItemDAO.insert(item, connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								retornoConfirmacao.put("resultado", new Integer(ERRO_CONFIRMACAO_DESCONHECIDO));
								return retornoConfirmacao;
							}
						}

						/* libera saida */
						if (DocumentoSaidaServices.liberarSaida(cdDocumentoSaida, 0, connection).getCode() <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							retornoConfirmacao.put("resultado", new Integer(ERRO_CONFIRMACAO_DESCONHECIDO));
							return retornoConfirmacao;
						}

						/* registra saida de estoque dos itens devolvidos */
						rsmItensTemp.beforeFirst();
						while (rsmItensTemp.next()) {
							SaidaLocalItem itemSaida = new SaidaLocalItem(0 /*cdSaidaLocalItem*/,
									rsmItensTemp.getInt("cd_produto_servico"),
									cdDocumentoSaida,
									acerto.getCdEmpresa(),
									cdLocalArmazenamento,
									0 /*cdPedidoVenda*/,
									acerto.getDtAcerto() /*dtSaida*/,
									0 /*qtSaida*/,
									rsmItensTemp.getFloat("qt_item") /*qtSaidaConsignada*/,
									SaidaLocalItemServices.ST_ENVIADO /*stSaidaLocalItem*/,
									"" /*diSaidaLocalItem*/,
									0 /*cdItem*/);
							if (SaidaLocalItemDAO.insert(itemSaida, connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								retornoConfirmacao.put("resultado", new Integer(ERRO_CONFIRMACAO_DESCONHECIDO));
								return retornoConfirmacao;
							}
						}
					}

					/* altera status do acerto */
					acerto.setStAcerto(ST_CONCLUIDO);
					if (acerto.getTpAcerto() == TP_ACERTO_FINANCEIRO)
						acerto.setCdDocumentoEntrada(cdDocumentoEntrada);
					else
						acerto.setCdDocumentoSaida(cdDocumentoSaida);
					if ((AcertoConsignacaoEntradaDAO.update(acerto, connection)>0 ? 1 : -1) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						retornoConfirmacao.put("resultado", new Integer(ERRO_CONFIRMACAO_DESCONHECIDO));
						return retornoConfirmacao;
					}
				}
			}

			if (isConnectionNull)
				connection.commit();

			retornoConfirmacao.put("cdDocumentoEntrada", new Integer(cdDocumentoEntrada));
			retornoConfirmacao.put("cdDocumentoSaida", new Integer(cdDocumentoSaida));
			retornoConfirmacao.put("resultado", new Integer(1));
			return retornoConfirmacao;
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			retornoConfirmacao.put("resultado", new Integer(ERRO_CONFIRMACAO_DESCONHECIDO));
			return retornoConfirmacao;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static AcertoConsignacaoEntrada insert(AcertoConsignacaoEntrada acerto) {
		return insert(acerto, null);
	}

	public static AcertoConsignacaoEntrada insert(AcertoConsignacaoEntrada acerto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int nrAno = new GregorianCalendar().get(Calendar.YEAR);
			int nrAcertoEntrada = NumeracaoDocumentoServices.getProximoNumero("ACERTO_CONSIGNACAO_ENTRADA", nrAno, acerto.getCdEmpresa(), connect);
			if (nrAcertoEntrada <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return null;
			}

			acerto.setIdAcertoConsignacao(new DecimalFormat("00000").format(nrAcertoEntrada) + "/" + nrAno);

			int cdAcertoConsignacao = AcertoConsignacaoEntradaDAO.insert(acerto, connect);
			if (cdAcertoConsignacao <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return null;
			}
			else
				acerto.setCdAcertoConsignacao(cdAcertoConsignacao);

			if (isConnectionNull)
				connect.commit();

			return acerto;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntradaServices.insert: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AcertoConsignacaoEntrada update(AcertoConsignacaoEntrada acerto) {
		return update(acerto, null);
	}

	public static AcertoConsignacaoEntrada update(AcertoConsignacaoEntrada acerto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			if (AcertoConsignacaoEntradaDAO.update(acerto, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return null;
			}

			return acerto;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntradaServices.update: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
