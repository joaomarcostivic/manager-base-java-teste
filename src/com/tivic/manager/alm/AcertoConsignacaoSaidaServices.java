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
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ProdutoServicoEmpresa;
import com.tivic.manager.grl.ProdutoServicoEmpresaDAO;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class AcertoConsignacaoSaidaServices {

	public static final int ST_PENTENTE = 0;
	public static final int ST_CONCLUIDO = 1;

	public static final String[] situacoes = {"Pendente", "Concluído"};

	public static final int TP_ACERTO_FINANCEIRO = 0;
	public static final int TP_DEVOLUCAO = 1;

	public static final String[] tiposAcertos = {"Acerto Financeiro", "Devolução"};

	public static final int ERRO_CONFIRMACAO_DESCONHECIDO = -1;
	public static final int ERRO_CONFIRMACAO_ACERTO_NAO_PENDENTE = -2;
	public static final int ERRO_CONFIRMACAO_SALDO_INSUFICIENTE = -3;

	public static AcertoConsignacaoSaida insert(AcertoConsignacaoSaida acerto) {
		return insert(acerto, null);
	}

	public static AcertoConsignacaoSaida insert(AcertoConsignacaoSaida acerto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int nrAno = new GregorianCalendar().get(Calendar.YEAR);
			int nrAcertoSaida = NumeracaoDocumentoServices.getProximoNumero("ACERTO_CONSIGNACAO_SAIDA", nrAno, acerto.getCdEmpresa(), connect);
			if (nrAcertoSaida <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return null;
			}

			acerto.setIdAcertoConsignacao(new DecimalFormat("00000").format(nrAcertoSaida) + "/" + nrAno);

			int cdAcertoConsignacao = AcertoConsignacaoSaidaDAO.insert(acerto, connect);
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
			System.err.println("Erro! AcertoConsignacaoSaidaServices.insert: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AcertoConsignacaoSaida update(AcertoConsignacaoSaida acerto) {
		return update(acerto, null);
	}

	public static AcertoConsignacaoSaida update(AcertoConsignacaoSaida acerto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			if (AcertoConsignacaoSaidaDAO.update(acerto, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return null;
			}

			return acerto;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoSaidaServices.update: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllItens(int cdAcertoConsignacao) {
		return getAllItens(cdAcertoConsignacao, null);
	}

	public static ResultSetMap getAllItens(int cdAcertoConsignacao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, C.id_produto_servico, C.txt_produto_servico, C.tp_produto_servico, " +
					"C.nm_produto_servico, C.sg_produto_servico, B.st_produto_empresa, B.cd_unidade_medida, " +
					"E.nm_unidade_medida, E.sg_unidade_medida " +
					"FROM alm_acerto_consignacao_sai_item A " +
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
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

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
		ResultSetMap rsm = Search.find("SELECT A.*, B.nm_pessoa AS nm_cliente " +
				"FROM alm_acerto_consignacao_saida A " +
				"JOIN grl_pessoa B ON (A.cd_cliente = B.cd_pessoa) " +
				"WHERE 1=1 " + (cdProdutoServico > 0 ? " AND " + cdProdutoServico + " IN (SELECT cd_produto_servico " +
				"					FROM alm_acerto_consignacao_sai_item C " +
				"					WHERE A.cd_acerto_consignacao = C.cd_acerto_consignacao)" : ""), criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
		if (rsm != null) {
			while (rsm.next()) {
				rsm.getRegister().put("DS_ST_ACERTO", situacoes[rsm.getInt("st_acerto")]);
				rsm.getRegister().put("DS_TP_ACERTO", tiposAcertos[rsm.getInt("tp_acerto")]);
			}
			rsm.beforeFirst();
		}
		return rsm;
	}

	public static Result confirmarAcertoConsignacao(int cdAcertoConsignacao, int cdLocalArmazenamento) {
		return confirmarAcertoConsignacao(cdAcertoConsignacao, cdLocalArmazenamento, false, null);
	}

	public static Result confirmarAcertoConsignacao(int cdAcertoConsignacao, int cdLocalArmazenamento, boolean isAcertoSimplificado) {
		return confirmarAcertoConsignacao(cdAcertoConsignacao, cdLocalArmazenamento, isAcertoSimplificado, null);
	}

	public static Result confirmarAcertoConsignacao(int cdAcertoConsignacao, int cdLocalArmazenamento, boolean isAcertoSimplificado, Connection connection) {
		boolean isConnectionNull = connection==null;
		Result retornoConfirmacao = new Result(0);
		int cdDocumentoEntrada    = 0;
		int cdDocumentoSaida      = 0;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			AcertoConsignacaoSaida acerto = AcertoConsignacaoSaidaDAO.get(cdAcertoConsignacao, connection);
			if (acerto==null || acerto.getStAcerto() != ST_PENTENTE)
				retornoConfirmacao.setCode(acerto==null ? ERRO_CONFIRMACAO_DESCONHECIDO : ERRO_CONFIRMACAO_ACERTO_NAO_PENDENTE);
			else {
				/* verifica se existe saldo disponivel para acerto de consigancao */
				PreparedStatement pstmt = connection.prepareStatement(
						"SELECT A.*, D.nm_produto_servico, " +
				        /* SOMANDO SAÍDAS */ 
						"	(SELECT SUM(G.qt_saida) FROM alm_documento_saida_item E, alm_documento_saida F, alm_saida_local_item G " +
						"	 WHERE E.cd_documento_saida = F.cd_documento_saida " +
						"	   AND E.cd_produto_servico = A.cd_produto_servico " +
						"	   AND E.cd_documento_saida = G.cd_documento_saida " +
						"	   AND E.cd_produto_servico = G.cd_produto_servico " +
						"	   AND E.cd_empresa         = G.cd_empresa " +
						"	   AND E.cd_empresa         = " + acerto.getCdEmpresa()+
						"	   AND F.tp_saida           = " + DocumentoSaidaServices.SAI_VENDA_CONSIGNACAO + " " +
						"      AND F.cd_cliente         = " + acerto.getCdCliente()+") AS qt_saida_consignada, " +
				        /* SOMANDO SAÍDAS CONSIGNADAS */ 
						"	(SELECT SUM(G.qt_saida_consignada) " +
						"	 FROM alm_documento_saida_item E, alm_documento_saida F, alm_saida_local_item G " +
						"	 WHERE E.cd_documento_saida = F.cd_documento_saida " +
						"	   AND E.cd_produto_servico = A.cd_produto_servico " +
						"	   AND E.cd_documento_saida = G.cd_documento_saida " +
						"	   AND E.cd_produto_servico = G.cd_produto_servico " +
						"	   AND E.cd_empresa = G.cd_empresa " +
						"	   AND E.cd_empresa = " +acerto.getCdEmpresa()+
						"	   AND F.tp_saida = " + DocumentoSaidaServices.SAI_VENDA_CONSIGNACAO + " " +
						"      AND F.cd_cliente = "+acerto.getCdCliente()+") AS qt_saida_nao_consignada, " +
				        /* SOMANDO SAÍDAS ACERTOS DE SAÍDAS CONSIGNADAS */ 
						"	(SELECT SUM(E.qt_item_consignado) " +
						"	 FROM alm_acerto_consignacao_sai_item E, alm_acerto_consignacao_saida F " +
						"	 WHERE E.cd_acerto_consignacao = F.cd_acerto_consignacao " +
						"	   AND E.cd_produto_servico = A.cd_produto_servico " +
						"	   AND E.cd_empresa = " +acerto.getCdEmpresa()+
						"	   AND F.st_acerto = 1 " +
						"	   AND F.cd_cliente = "+acerto.getCdCliente()+") AS qt_acerto_consignado, " +
				        /* SOMANDO SAÍDAS ACERTOS DE SAÍDAS CONSIGNADAS */ 
						"	 (SELECT SUM(E.qt_item_nao_consignado) " +
						"	 FROM alm_acerto_consignacao_sai_item E, alm_acerto_consignacao_saida F " +
						"	 WHERE E.cd_acerto_consignacao = F.cd_acerto_consignacao " +
						"	   AND E.cd_produto_servico = A.cd_produto_servico " +
						"	   AND E.cd_empresa = " +acerto.getCdEmpresa()+
						"	   AND F.st_acerto = 1 " +
						"	   AND F.cd_cliente = "+acerto.getCdCliente()+") AS qt_acerto_nao_consignado " +
						"FROM alm_acerto_consignacao_sai_item A, grl_produto_servico_empresa B, grl_produto C, grl_produto_servico D " +
						"WHERE A.cd_empresa = B.cd_empresa " +
						"  AND A.cd_produto_servico = B.cd_produto_servico " +
						"  AND B.cd_produto_servico = C.cd_produto_servico " +
						"  AND C.cd_produto_servico = D.cd_produto_servico " +
						"  AND A.cd_acerto_consignacao = "+cdAcertoConsignacao);

				boolean isSaldoInsuficiente = false;
				ResultSetMap rsmItens     = new ResultSetMap(pstmt.executeQuery());
				ResultSetMap rsmItensTemp = new ResultSetMap();
				for (int i=0; i<rsmItens.size(); i++) {
					rsmItens.goTo(i);
					/* em caso de acerto simplificado, verificacao de saldo leva em conta o saldo disponivel total,
					 * ao inves dos saldos individuais (consignado e nao-consignado)
					 */
					if (isAcertoSimplificado) {
						float qtTotalItem        = rsmItens.getFloat("qt_item_consignado") + rsmItens.getFloat("qt_item_nao_consignado");
						float qtTotalSaldoAcerto = rsmItens.getFloat("qt_saida_consignada")     - rsmItens.getFloat("qt_acerto_consignado") +
												   rsmItens.getFloat("qt_saida_nao_consignada") - rsmItens.getFloat("qt_acerto_nao_consignado");
						if (qtTotalItem > qtTotalSaldoAcerto) {
							rsmItens.getRegister().put("ST_ITEM", "Saldo a acertar insuficiente");
							isSaldoInsuficiente = true;
						}
						else {
							/* ajusta a quantidade (consignado e nao consignado) a ser acertada, com base no saldo
							 * de acerto disponivel; prioridade de abate de saldo nao consignado sobre saldo consignado
							 */
							float qtSaldoAcertoNaoConsignado = rsmItens.getFloat("qt_saida_nao_consignada") - rsmItens.getFloat("qt_acerto_nao_consignado") ;
							float qtItemConsignado = qtSaldoAcertoNaoConsignado < qtTotalItem ? qtTotalItem - qtSaldoAcertoNaoConsignado : 0;
							float qtItemNaoConsignado = qtTotalItem - qtItemConsignado;
							((HashMap<String,Object>)rsmItens.getLines().get(i)).put("qt_item_consignado", new Float(qtItemConsignado));
							((HashMap<String,Object>)rsmItens.getLines().get(i)).put("qt_item_nao_consignado", new Float(qtItemNaoConsignado));
							rsmItensTemp.getLines().add(rsmItens.getLines().remove(i));
							i--;
						}
					}
					else {
						if (rsmItens.getFloat("qt_item_consignado") > rsmItens.getFloat("qt_saida_consignada") - rsmItens.getFloat("qt_acerto_consignado")) {
							rsmItens.getRegister().put("ST_ITEM", "Saldo a acertar (consignado) insuficiente");
							isSaldoInsuficiente = true;
						}
						else if (rsmItens.getFloat("qt_item_nao_consignado") > rsmItens.getFloat("qt_saida_nao_consignada") - rsmItens.getFloat("qt_acerto_nao_consignado")) {
							rsmItens.getRegister().put("ST_ITEM", "Saldo a acertar (não consignado) insuficiente");
							isSaldoInsuficiente = true;
						}
						else {
							rsmItensTemp.getLines().add(rsmItens.getLines().remove(i));
							i--;
						}
					}
				}

				if (isSaldoInsuficiente) {
					retornoConfirmacao.setCode(ERRO_CONFIRMACAO_SALDO_INSUFICIENTE);
					retornoConfirmacao.addObject("resultset", rsmItens);
					if (isConnectionNull)
						Conexao.rollback(connection);
					return retornoConfirmacao;
				}
				else {
					/* atualiza quantidade (consignado e nao consignado) dos itens
					 * em caso de acerto simplificado
					 */
					rsmItensTemp.beforeFirst();
					while (rsmItensTemp.next()) {
						AcertoConsignacaoSaiItem itemAcerto = new AcertoConsignacaoSaiItem(cdAcertoConsignacao, acerto.getCdEmpresa(),
																							rsmItensTemp.getInt("cd_produto_servico"), rsmItensTemp.getFloat("vl_item"),
																							rsmItensTemp.getFloat("qt_item_consignado"), rsmItensTemp.getFloat("qt_item_nao_consignado"));
						int code = AcertoConsignacaoSaiItemServices.update(itemAcerto, connection);
						if (code <= 0) {
							
							if (isConnectionNull)
								Conexao.rollback(connection);
							retornoConfirmacao.setCode(ERRO_CONFIRMACAO_DESCONHECIDO);
							return retornoConfirmacao;
						}
					}
					/* Acerto - Tipo Financeiro */
					if (acerto.getTpAcerto() == TP_ACERTO_FINANCEIRO) {
						DocumentoSaida saida = new DocumentoSaida();
						saida.setCdEmpresa(acerto.getCdEmpresa());
						saida.setCdCliente(acerto.getCdCliente());
						saida.setDtDocumentoSaida(new GregorianCalendar()); 
						saida.setStDocumentoSaida(DocumentoSaidaServices.ST_EM_CONFERENCIA);
						saida.setNrDocumentoSaida("AC" + acerto.getIdAcertoConsignacao());
						saida.setTpDocumentoSaida(DocumentoSaidaServices.TP_NOTA_FISCAL_VENDA);
						saida.setTpSaida(DocumentoSaidaServices.SAI_ACERTO_CONSIGNACAO);
						saida.setDtEmissao(saida.getDtDocumentoSaida());
						saida.setTpFrete(DocumentoSaidaServices.FRT_CIF);
						saida.setTxtObservacao("Acerto de Consignação de Cliente (referência: " + acerto.getIdAcertoConsignacao() + ")");
						saida.setTpMovimentoEstoque(DocumentoSaidaServices.MOV_AMBOS_TIPO_ESTOQUE);
						int cdNatOperacaoDefault        = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_SAIDA_DEFAULT", 0, acerto.getCdEmpresa());
						saida.setCdNaturezaOperacao(cdNatOperacaoDefault);
						if ((cdDocumentoSaida = DocumentoSaidaDAO.insert(saida, connection)) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							retornoConfirmacao.setCode(ERRO_CONFIRMACAO_DESCONHECIDO);
							return retornoConfirmacao;
						}
						/* Acrescentando itens na saida */
						rsmItensTemp.beforeFirst();
						float vlTotalDocumento = 0;
						while (rsmItensTemp.next()) {
							
							vlTotalDocumento += (rsmItensTemp.getFloat("vl_item") * (rsmItensTemp.getFloat("qt_item_consignado") + rsmItensTemp.getFloat("qt_item_nao_consignado")));
							
							ProdutoServicoEmpresa produto = ProdutoServicoEmpresaDAO.get(acerto.getCdEmpresa(), rsmItensTemp.getInt("cd_produto_servico"), connection);
							DocumentoSaidaItem item = new DocumentoSaidaItem(cdDocumentoSaida, rsmItensTemp.getInt("cd_produto_servico"), acerto.getCdEmpresa(),
																			rsmItensTemp.getFloat("qt_item_consignado") + rsmItensTemp.getFloat("qt_item_nao_consignado"),
																			rsmItensTemp.getFloat("vl_item"), 0 /*vlAcrescimo*/, 0 /*vlDesconto*/, null /*dtEntregaPrevista*/,
																			produto.getCdUnidadeMedida(),
																			0 /*cdTabelaPreco*/, 0 /*cdItem*/, 0 /*cdBico*/, 0 /*vlDescontoGeral*/,  cdNatOperacaoDefault/*cdNaturezaOperacao*/, rsmItensTemp.getInt("cd_tipo_contribuicao_social"));
							if (DocumentoSaidaItemDAO.insert(item, connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								retornoConfirmacao.setCode(ERRO_CONFIRMACAO_DESCONHECIDO);
								return retornoConfirmacao;
							}
						}

						saida.setVlTotalDocumento(vlTotalDocumento);
						saida.setVlTotalItens(vlTotalDocumento);
						if ((DocumentoSaidaDAO.update(saida, connection)) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							retornoConfirmacao.setCode(ERRO_CONFIRMACAO_DESCONHECIDO);
							return retornoConfirmacao;
						}
						/* libera saida */
						Result resultado = DocumentoSaidaServices.liberarSaida(cdDocumentoSaida, cdLocalArmazenamento, connection);
						if (resultado.getCode() <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							retornoConfirmacao.setCode(ERRO_CONFIRMACAO_DESCONHECIDO);
							retornoConfirmacao.setMessage(resultado.getMessage());
							return retornoConfirmacao;
						}
					}
//					/* acerto - devolucao */
//					else {
					/* cria entrada (devolucao) */
					DocumentoEntrada entrada = new DocumentoEntrada();
					entrada.setCdEmpresa(acerto.getCdEmpresa());
					entrada.setDtDocumentoEntrada(new GregorianCalendar());
					entrada.setDtEmissao(new GregorianCalendar());
					entrada.setCdFornecedor(acerto.getCdCliente());
					entrada.setStDocumentoEntrada(DocumentoEntradaServices.ST_EM_ABERTO);
					entrada.setNrDocumentoEntrada("AC" + acerto.getIdAcertoConsignacao());
					entrada.setTpDocumentoEntrada(DocumentoEntradaServices.TP_DOC_NAO_FISCAL);
					entrada.setTpEntrada(DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO);
					entrada.setTpFrete(DocumentoEntradaServices.FRT_CIF);
					entrada.setTpMovimentoEstoque(DocumentoEntradaServices.MOV_AMBOS_TIPO_ESTOQUE);
					entrada.setTxtObservacao("Acerto de Consignação de Cliente - Devoluçao (referência: " + acerto.getIdAcertoConsignacao() + ")");
					int cdNatOperacaoDevolucaoDefault        = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_CLIENTE", 0);
					if(cdNatOperacaoDevolucaoDefault == 0){
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Configure o paramêtro de Natureza de Operação para Devolução de Cliente");
					}
					entrada.setCdNaturezaOperacao(cdNatOperacaoDevolucaoDefault);
					
					if ((cdDocumentoEntrada = DocumentoEntradaDAO.insert(entrada, connection)) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						retornoConfirmacao.setCode(ERRO_CONFIRMACAO_DESCONHECIDO);
						return retornoConfirmacao;
					}

					/* acrescenta itens na entrada */
					rsmItensTemp.beforeFirst();
					
					float vlTotalEntrada = 0;
					
					while (rsmItensTemp.next()) {
						DocumentoEntradaItem item = new DocumentoEntradaItem(cdDocumentoEntrada, rsmItensTemp.getInt("cd_produto_servico"), acerto.getCdEmpresa(), 
								rsmItensTemp.getFloat("qt_item_consignado") + rsmItensTemp.getFloat("qt_item_nao_consignado"), rsmItensTemp.getFloat("vl_item"), 0, 0, 0, null, 0, cdNatOperacaoDevolucaoDefault, rsmItensTemp.getInt("cd_item"), (float)0, (float)0, rsmItensTemp.getInt("cd_tipo_credito"));
						
						int cdItem = DocumentoEntradaItemDAO.insert(item, connection);
						rsmItensTemp.setValueToField("cd_item", cdItem);
						if (cdItem <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							retornoConfirmacao.setCode(ERRO_CONFIRMACAO_DESCONHECIDO);
							return retornoConfirmacao;
						}
						vlTotalEntrada += (rsmItensTemp.getFloat("vl_item") * (rsmItensTemp.getFloat("qt_item_consignado") + rsmItensTemp.getFloat("qt_item_nao_consignado")));
					}

					entrada.setVlTotalDocumento(vlTotalEntrada);
					entrada.setVlTotalItens(vlTotalEntrada);
					if ((DocumentoEntradaDAO.update(entrada, connection)) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						retornoConfirmacao.setCode(ERRO_CONFIRMACAO_DESCONHECIDO);
						return retornoConfirmacao;
					}
					
					/* libera entrada */
					Result resultado = DocumentoEntradaServices.liberarEntrada(cdDocumentoEntrada, cdLocalArmazenamento, connection);
					if (resultado.getCode() <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						retornoConfirmacao.setCode(ERRO_CONFIRMACAO_DESCONHECIDO);
						retornoConfirmacao.setMessage(resultado.getMessage());
						return retornoConfirmacao;
					}

					/* registra entrada de estoque dos itens devolvidos */
					rsmItensTemp.beforeFirst();
					while (rsmItensTemp.next()) {
						EntradaLocalItem itemEntrada = new EntradaLocalItem(rsmItensTemp.getInt("cd_produto_servico"), cdDocumentoEntrada,
											acerto.getCdEmpresa(), cdLocalArmazenamento, rsmItensTemp.getFloat("qt_item_nao_consignado"),
											rsmItensTemp.getFloat("qt_item_consignado"), 0 /*cdLocalArmazenamentoItem*/, rsmItensTemp.getInt("cd_item"));
						if (EntradaLocalItemServices.insert(itemEntrada, connection) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							retornoConfirmacao.setCode(ERRO_CONFIRMACAO_DESCONHECIDO);
							return retornoConfirmacao;
						}
					}
				}

				/* altera status do acerto */
				acerto.setStAcerto(ST_CONCLUIDO);
				if (acerto.getTpAcerto() == TP_ACERTO_FINANCEIRO)
					acerto.setCdDocumentoSaida(cdDocumentoSaida);
				else
					acerto.setCdDocumentoEntrada(cdDocumentoEntrada);
				if ((AcertoConsignacaoSaidaDAO.update(acerto, connection)>0 ? 1 : -1) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					retornoConfirmacao.setCode(ERRO_CONFIRMACAO_DESCONHECIDO);
					return retornoConfirmacao;
				}
//				}
			}

			if (isConnectionNull)
				connection.commit();

			retornoConfirmacao.addObject("cdDocumentoEntrada", new Integer(cdDocumentoEntrada));
			retornoConfirmacao.addObject("cdDocumentoSaida", new Integer(cdDocumentoSaida));
			retornoConfirmacao.setCode(1);
			return retornoConfirmacao;
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			retornoConfirmacao.setCode(ERRO_CONFIRMACAO_DESCONHECIDO);
			return retornoConfirmacao;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

}
