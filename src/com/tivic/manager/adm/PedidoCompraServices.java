package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.NumeracaoDocumentoServices;
import com.tivic.manager.grl.ParametroServices;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class PedidoCompraServices {
	/* Situações (st_pedido_compra) */
	public static final int ST_EM_ABERTO = 0;
	public static final int ST_PROCESSADO = 1; // RESULTADO APURADO
	public static final int ST_ENCERRADO = 2;
	public static final int ST_CANCELADO = 3;

	/* Tipos (tp_pedido_compra) */
	public static final int PED_COMPRA = 0;
	public static final int PED_ORCAMENTO = 1;

	/* Tipos de processamento */
	public static final int PROC_MENOR_PRECO = 1;
	public static final int PROC_FORNECEDOR = 2;

	public static final int ERR_PEDIDO_COMPRA = -2;
	public static final int ERR_FORNECEDOR = -3;
	public static final int ERR_NOT_CD_MOEDA_DEFAULT = -4;
	public static final int ERR_ITEM_PEDIDO_COMPRA = -5;

	public static final String[] situacaoPedidoCompra = {"Em aberto", "Processado", "Encerrado", "Cancelado"};

	public static final String[] tipoPedidoCompra = {"Pedido de Compra", "Orçamento"};

	public static final String[] tipoProcessamento = {"Menor preço", "Fornecedor único"};

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

			if ((nrDocumento = NumeracaoDocumentoServices.getProximoNumero("PEDIDO_COMPRA", nrAno, cdEmpresa, connection)) <= 0)
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

	public static int setSituacaoPedidoCompra(int cdPedidoCompra, int stPedidoCompra) {
		return setSituacaoPedidoCompra(cdPedidoCompra, stPedidoCompra, null);
	}

	public static int setSituacaoPedidoCompra(int cdPedidoCompra, int stPedidoCompra, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			PedidoCompra documento = PedidoCompraDAO.get(cdPedidoCompra, connection);
			int situacaoAtual = documento.getStPedidoCompra();
			if (stPedidoCompra == situacaoAtual)
				return 1;
			else {
				documento.setStPedidoCompra(stPedidoCompra);
				if (PedidoCompraDAO.update(documento, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}
			if (isConnectionNull)
				connection.commit();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraServices.setSituacaoPedidoCompra: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int update(PedidoCompra pedidoCompra){
		return update(pedidoCompra, null);
	}

	public static int update(PedidoCompra pedidoCompra, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			int cdPedidoCompra = pedidoCompra.getCdPedidoCompra();
			if (pedidoCompra.getStPedidoCompra() != PedidoCompraServices.ST_EM_ABERTO) {
				/* Altera a situação do pedido de compra para: EM ABERTO */
				pedidoCompra.setStPedidoCompra(PedidoCompraServices.ST_EM_ABERTO);

				/* Anula o processamento efetuado antes */
				int cdProdutoServico = 0;
				ResultSetMap rsmPedidoCompraItem = getAllItens(cdPedidoCompra);
				while (rsmPedidoCompraItem != null && rsmPedidoCompraItem.next()) {
					cdProdutoServico = rsmPedidoCompraItem.getInt("cd_produto_servico");
					PedidoCompraItemServices.anularProcessamento(cdPedidoCompra, cdProdutoServico, connection);
				}
			}

			if (PedidoCompraDAO.update(pedidoCompra, connection) <= 0) {
				if (isConnectionNull)
					Conexao.desconectar(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.desconectar(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int delete(int cdPedidoCompra){
		return delete(cdPedidoCompra, null);
	}

	public static int delete(int cdPedidoCompra, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			PedidoCompra ordemCompra = PedidoCompraDAO.get(cdPedidoCompra, connection);
			int cdEmpresa = ordemCompra.getCdEmpresa();

			/* exclusão dos itens */
			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_produto_servico " +
					"FROM adm_pedido_compra_item A " +
					"WHERE A.cd_pedido_compra = ?");
			pstmt.setInt(1, cdPedidoCompra);
			ResultSet rs = pstmt.executeQuery();
			while (rs != null && rs.next()) {
				if (PedidoCompraItemDAO.delete(cdEmpresa, rs.getInt("cd_produto_servico"), cdPedidoCompra) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return ERR_ITEM_PEDIDO_COMPRA;
				}
			}

			if (PedidoCompraDAO.delete(cdPedidoCompra, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.* " +
						   "FROM adm_pedido_compra A ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap getItem(int cdPedidoCompra, int cdProdutoServico) {
		return getAllItens(cdPedidoCompra, cdProdutoServico, null);
	}

	public static ResultSetMap getAllItens(int cdPedidoCompra) {
		return getAllItens(cdPedidoCompra, 0, null);
	}

	public static ResultSetMap getAllItens(int cdPedidoCompra, int cdProdutoServico) {
		return getAllItens(cdPedidoCompra, cdProdutoServico, null);
	}

	public static ResultSetMap getAllItens(int cdPedidoCompra, int cdProdutoServico, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, " +
											 "B.id_reduzido, B.qt_minima, B.vl_ultimo_custo, B.qt_dias_estoque, B.st_produto_empresa, " +
											 "C.nm_produto_servico, C.tp_produto_servico, C.id_produto_servico," +
											 "C.sg_produto_servico, " +
											 "E.sg_unidade_medida, " +
											 "(A.qt_solicitada * B.vl_ultimo_custo) AS vl_custo_total_item, " +
											 "(A.qt_solicitada - A.qt_atendida) AS qt_pendente " +
											 "FROM adm_pedido_compra_item A " +
											 "JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico = B.cd_produto_servico AND A.cd_empresa = B.cd_empresa) " +
											 "JOIN grl_produto_servico C ON (B.cd_produto_servico = C.cd_produto_servico) " +
											 "JOIN grl_produto D ON (C.cd_produto_servico = D.cd_produto_servico) " +
											 "LEFT OUTER JOIN grl_unidade_medida E ON (A.cd_unidade_medida = E.cd_unidade_medida) " +
											 "WHERE (B.st_produto_empresa = " + com.tivic.manager.grl.ProdutoServicoEmpresaServices.ST_ATIVO + ") AND A.cd_pedido_compra = ? " +
											 (cdProdutoServico > 0 ? " AND A.cd_produto_servico = " + cdProdutoServico : ""));
			pstmt.setInt(1, cdPedidoCompra);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraServices.getAllItens: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllCotacao(int cdPedidoCompra) {
		return getAllCotacao(cdPedidoCompra, -1, null, null);
	}

	public static ResultSetMap getAllCotacao(int cdPedidoCompra, ArrayList<ItemComparator> criterios) {
		return getAllCotacao(cdPedidoCompra, -1, criterios, null);
	}

	public static ResultSetMap getAllCotacao(int cdPedidoCompra, int cdProdutoServico) {
		return getAllCotacao(cdPedidoCompra, cdProdutoServico, null, null);
	}

	public static ResultSetMap getAllCotacao(int cdPedidoCompra, int cdProdutoServico, ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String sql = "SELECT A.*, " +
						 "B.nm_pessoa AS nm_fornecedor, " +
						 "C.nm_plano_pagamento, " +
						 "D.nm_pessoa_contato, " +
						 "E.qt_solicitada, " +
						 "(A.vl_cotacao + A.vl_tributos + A.vl_frete - A.vl_desconto) AS vl_liquido_cotacao," +
						 "(E.qt_solicitada * A.vl_cotacao) AS vl_total_cotacao, " +
						 "(E.qt_solicitada * (A.vl_cotacao + A.vl_tributos + A.vl_frete - A.vl_desconto)) AS vl_total " +
						 "FROM adm_pedido_compra_item E, adm_cotacao_pedido_item A " +
						 "LEFT OUTER JOIN grl_pessoa B ON (A.cd_fornecedor = B.cd_pessoa) " +
						 "LEFT OUTER JOIN adm_plano_pagamento C ON (A.cd_plano_pagamento = C.cd_plano_pagamento) " +
						 "LEFT OUTER JOIN grl_pessoa_contato D ON (A.cd_pessoa_contato = D.cd_pessoa_contato AND A.cd_pessoa_juridica = D.cd_pessoa_juridica) " +
						 "WHERE (A.cd_empresa = E.cd_empresa) " +
						 "	AND (A.cd_pedido_compra = E.cd_pedido_compra) " +
						 "  AND (A.cd_produto_servico = E.cd_produto_servico) ";
			if (criterios == null) {
				criterios = new ArrayList<ItemComparator>();
			}
			criterios.add(new ItemComparator("A.cd_pedido_compra", Integer.toString(cdPedidoCompra), ItemComparator.EQUAL, Types.INTEGER));
			if (cdProdutoServico > 0) {
				criterios.add(new ItemComparator("A.cd_produto_servico", Integer.toString(cdProdutoServico), ItemComparator.EQUAL, Types.INTEGER));
			}
			return Search.find(sql, "ORDER BY A.cd_cotacao_pedido_item", criterios, connect, true);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraServices.getAllCotacao: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getResumoCotacaoFornecedor(int cdPedidoCompra) {
		return getResumoCotacaoFornecedor(cdPedidoCompra, -1, -1, null);
	}

	public static ResultSetMap getResumoCotacaoFornecedor(int cdPedidoCompra, int cdFornecedor) {
		return getResumoCotacaoFornecedor(cdPedidoCompra, cdFornecedor, -1, null);
	}

	public static ResultSetMap getResumoCotacaoFornecedor(int cdPedidoCompra, int cdFornecedor, int tipoResultado) {
		return getResumoCotacaoFornecedor(cdPedidoCompra, cdFornecedor, tipoResultado, null);
	}

	public static ResultSetMap getResumoCotacaoFornecedor(int cdPedidoCompra, int cdFornecedor, int tipoResultado, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.cd_fornecedor, A.cd_empresa, " +
											 "	A.cd_pedido_compra, " +
											 "	B.nm_pessoa AS nm_fornecedor, " +
											 "	COUNT(A.cd_produto_servico) AS total_produtos, " +
											 "	SUM(C.qt_solicitada * (A.vl_cotacao + A.vl_tributos + A.vl_frete - A.vl_desconto)) AS vl_total_fornecedor " +
											 "FROM adm_cotacao_pedido_item A, grl_pessoa B, adm_pedido_compra_item C " +
											 "WHERE (A.cd_fornecedor = B.cd_pessoa) " +
											 "	AND (A.cd_empresa = C.cd_empresa " +
											 "	   AND A.cd_pedido_compra = C.cd_pedido_compra " +
											 "     AND A.cd_produto_servico = C.cd_produto_servico) " +
											 "	AND (A.cd_pedido_compra = ?) AND (A.tp_resultado <> ?) " +
											 (cdFornecedor > 0 ? "AND A.cd_fornecedor = " + cdFornecedor : "") +
											 (tipoResultado > 0 ? "AND A.tp_resultado = " + tipoResultado : "") +
											 "GROUP BY A.cd_fornecedor, A.cd_empresa, " +
											 "	A.cd_pedido_compra, " +
											 "  B.nm_pessoa " +
											 "ORDER BY total_produtos DESC, vl_total_fornecedor ASC ");
			pstmt.setInt(1, cdPedidoCompra);
			pstmt.setInt(2, CotacaoPedidoItemServices.TP_FORA_ESPECIFICACAO);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraServices.getResumoCotacaoFornecedor: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllCotacaoFornecedor(int cdPedidoCompra) {
		return getAllCotacaoFornecedor(cdPedidoCompra, -1, -1, null);
	}

	public static ResultSetMap getAllCotacaoFornecedor(int cdPedidoCompra, int cdFornecedor) {
		return getAllCotacaoFornecedor(cdPedidoCompra, cdFornecedor, -1, null);
	}

	public static ResultSetMap getAllCotacaoFornecedor(int cdPedidoCompra, int cdFornecedor, int tipoResultado) {
		return getAllCotacaoFornecedor(cdPedidoCompra, cdFornecedor, tipoResultado, null);
	}

	public static ResultSetMap getAllCotacaoFornecedor(int cdPedidoCompra, int cdFornecedor, int tipoResultado, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, " +
											 "	B.nm_pessoa AS nm_fornecedor, " +
											 "  C.cd_unidade_medida " +
											 "FROM adm_cotacao_pedido_item A, grl_pessoa B, adm_pedido_compra_item C " +
											 "WHERE (A.cd_fornecedor = B.cd_pessoa) " +
											 "	AND (A.cd_empresa = C.cd_empresa AND A.cd_pedido_compra = C.cd_pedido_compra AND A.cd_produto_servico = C.cd_produto_servico) " +
											 "	AND (A.cd_pedido_compra = ?) AND (A.tp_resultado != ?) " +
											 (cdFornecedor > 0 ? "AND A.cd_fornecedor = " + cdFornecedor : "") +
											 (tipoResultado > 0 ? "AND A.tp_resultado = " + tipoResultado : "") +
											 "ORDER BY A.cd_fornecedor");
			pstmt.setInt(1, cdPedidoCompra);
			pstmt.setInt(2, CotacaoPedidoItemServices.TP_FORA_ESPECIFICACAO);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraServices.getAllCotacaoFornecedor: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllCompraSolicitaItem(int cdEmpresa, int cdProdutoServico, int cdPedidoCompra) {
		return getAllCompraSolicitaItem(cdEmpresa, cdProdutoServico, cdPedidoCompra, null);
	}

	public static ResultSetMap getAllCompraSolicitaItem(int cdEmpresa, int cdProdutoServico, int cdPedidoCompra, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.cd_empresa, A.cd_pedido_compra, A.cd_produto_servico, A.cd_solicitacao_material, A.qt_atendida AS qt_atendida, " +
											 "B.qt_solicitada AS qt_solicitada, " +
											 "C.dt_solicitacao, C.st_solicitacao_material, C.id_solicitacao_material, " +
											 "D.sg_unidade_medida, " +
											 "E.nm_setor " +
											 "FROM adm_compra_solicitacao_item A " +
											 "LEFT OUTER JOIN adm_solicitacao_material_item B ON (A.cd_solicitacao_material = B.cd_solicitacao_material AND A.cd_produto_servico = B.cd_produto_servico) " +
											 "LEFT OUTER JOIN adm_solicitacao_material C ON (B.cd_solicitacao_material = C.cd_solicitacao_material) " +
											 "LEFT OUTER JOIN grl_unidade_medida D ON (B.cd_unidade_medida = D.cd_unidade_medida) " +
											 "LEFT OUTER JOIN grl_setor E ON (C.cd_setor_solicitante = E.cd_setor) " +
											 "WHERE A.cd_empresa = ? AND A.cd_produto_servico = ? AND A.cd_pedido_compra = ?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdPedidoCompra);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraServices.getAllCompraSolicitaItem: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllFornecedoresPedidoCompra(int cdPedidoCompra) {
		return getAllFornecedoresPedidoCompra(cdPedidoCompra, null);
	}

	public static ResultSetMap getAllFornecedoresPedidoCompra(int cdPedidoCompra, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT DISTINCT A.cd_fornecedor, B.nm_pessoa AS nm_fornecedor " +
											 "FROM adm_cotacao_pedido_item A, grl_pessoa B " +
											 "WHERE (A.cd_fornecedor = B.cd_pessoa) AND (A.cd_pedido_compra = ?) " +
											 "ORDER BY A.cd_fornecedor");
			pstmt.setInt(1, cdPedidoCompra);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraServices.getAllFornecedoresPedidoCompra: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getOrdemCompraPedido(int cdPedidoCompra) {
		return getOrdemCompraPedido(cdPedidoCompra, null);
	}

	public static ResultSetMap getOrdemCompraPedido(int cdPedidoCompra, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT DISTINCT A.cd_ordem_compra, A.cd_local_entrega, " +
											 "	A.dt_ordem_compra, A.st_ordem_compra, A.dt_limite_entrega, A.id_ordem_compra, " +
											 "	A.nr_ordem_compra, A.cd_fornecedor, A.cd_local_entrega, A.cd_moeda, " +
											 "	A.cd_tabela_preco, A.cd_comprador, A.vl_desconto, A.vl_acrescimo, " +
											 "	A.tp_movimento_estoque, A.txt_observacao, A.vl_total_documento, " +
											 "	C.nm_local_armazenamento, " +
											 "	D.nm_pessoa AS nm_fornecedor, " +
											 "	SUM (B.qt_comprada * ((B.vl_unitario + B.vl_acrescimo) - B.vl_desconto)) AS TOTAL_ORDEM_COMPRA " +
											 "FROM adm_ordem_compra_item B, adm_ordem_compra A " +
											 "	LEFT OUTER JOIN alm_local_armazenamento C " +
											 "		ON (A.cd_local_entrega = C.cd_local_armazenamento) " +
											 "	LEFT OUTER JOIN grl_pessoa D " +
											 "		ON (A.cd_fornecedor = D.cd_pessoa) " +
											 "WHERE (A.cd_ordem_compra = B.cd_ordem_compra) " +
											 "	AND B.cd_cotacao_pedido_item IN " +
										     "	  (SELECT A.cd_cotacao_pedido_item " +
											 "	   FROM adm_cotacao_pedido_item A " +
											 "	   WHERE A.tp_resultado = ? " +
											 "		 AND A.cd_pedido_compra = ?) " +
											 "GROUP BY A.cd_ordem_compra, A.cd_local_entrega, " +
											 "	A.dt_ordem_compra, A.st_ordem_compra, A.dt_limite_entrega, A.id_ordem_compra, " +
											 "	A.nr_ordem_compra, A.cd_fornecedor, A.cd_local_entrega, A.cd_moeda, " +
											 "	A.cd_tabela_preco, A.cd_comprador, A.vl_desconto, A.vl_acrescimo, " +
											 "	A.tp_movimento_estoque, A.txt_observacao, A.vl_total_documento, " +
											 "	C.nm_local_armazenamento, " +
											 "	D.nm_pessoa");
			pstmt.setInt(1, CotacaoPedidoItemServices.TP_VENCEDOR);
			pstmt.setInt(2, cdPedidoCompra);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraServices.getOrdemCompraPedido: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/* Processa o pedido de compra inteiro ou somente um item */
	public static int processarResultado(int cdPedidoCompra) {
		return processarResultado(cdPedidoCompra, -1, -1, null);
	}

	public static int processarResultado(int cdPedidoCompra, int cdProdutoServico) {
		return processarResultado(cdPedidoCompra, cdProdutoServico, -1, null);
	}

	public static int processarResultado(int cdPedidoCompra, int cdProdutoServico, int tipoProcessamento) {
		return processarResultado(cdPedidoCompra, cdProdutoServico, tipoProcessamento, null);
	}

	public static int processarResultado(int cdPedidoCompra, int cdProdutoServico, int tipoProcessamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (cdPedidoCompra <= 0) {
				System.err.println("Erro! PedidoCompraServices.processarResultado: código do Pedido de Compra não informado.");
				return ERR_PEDIDO_COMPRA;
			}
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			/* Processa resultado do pedido de compra */
			float menorPreco = 0;
			int cdFornecedorVencedor = 0;
			ResultSetMap rsmPedidoCompraItem = getAllItens(cdPedidoCompra, cdProdutoServico);

			if (tipoProcessamento == PROC_FORNECEDOR) {
				menorPreco = 1000000000;
				int cdFornecedor = 0;
				ResultSetMap rsmFornecedores = getAllFornecedoresPedidoCompra(cdPedidoCompra);
				if (rsmFornecedores.size() <= 0) {
					return ERR_FORNECEDOR;
				}
				while (rsmFornecedores != null && rsmFornecedores.next()) {
					cdFornecedor = rsmFornecedores.getInt("cd_fornecedor");
					ResultSetMap rsmCotacaoFornecedor = getResumoCotacaoFornecedor(cdPedidoCompra, cdFornecedor);
					if (rsmCotacaoFornecedor != null && rsmCotacaoFornecedor.next()) {
						float valorTotalFornecedor = rsmCotacaoFornecedor.getFloat("vl_total_fornecedor");
						if (rsmCotacaoFornecedor.getInt("total_produtos") == rsmPedidoCompraItem.size()) {
							if (valorTotalFornecedor < menorPreco) {
								cdFornecedorVencedor = cdFornecedor;
								menorPreco = valorTotalFornecedor;
							}
						}
					}
				}

			}
			int cdProdutoServicoAtual = 0;
			while (rsmPedidoCompraItem != null && rsmPedidoCompraItem.next()) {
				cdProdutoServicoAtual = rsmPedidoCompraItem.getInt("cd_produto_servico");
				ResultSetMap rsmCotacaoPedidoItem = getAllCotacao(cdPedidoCompra, cdProdutoServicoAtual);
				float precoProdutoServico = 0;
				int cdCotacaoVencedora = 0;
				int cdCotacaoPedidoItem = 0;
				menorPreco = 1000000000;
				while (rsmCotacaoPedidoItem != null && rsmCotacaoPedidoItem.next()) {
					cdCotacaoPedidoItem = rsmCotacaoPedidoItem.getInt("cd_cotacao_pedido_item");
					precoProdutoServico = rsmCotacaoPedidoItem.getFloat("vl_liquido_cotacao");
					if (cdFornecedorVencedor > 0) {
						if (rsmCotacaoPedidoItem.getInt("cd_fornecedor") == cdFornecedorVencedor) {
							cdCotacaoVencedora = cdCotacaoPedidoItem;
						}
					}
					else {
						if ((precoProdutoServico < menorPreco) &&
							(rsmCotacaoPedidoItem.getInt("tp_resultado") != CotacaoPedidoItemServices.TP_FORA_ESPECIFICACAO)) {
							cdCotacaoVencedora = cdCotacaoPedidoItem;
							menorPreco = precoProdutoServico;
						}
					}
					CotacaoPedidoItem cotacaoPedidoItem = CotacaoPedidoItemDAO.get(cdCotacaoPedidoItem);
					/* Em aberto - todas as cotações são definidas como não-vencedoras */
					cotacaoPedidoItem.setTpResultado(CotacaoPedidoItemServices.TP_EM_ABERTO);
					if (CotacaoPedidoItemDAO.update(cotacaoPedidoItem, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1; /* ERRO ao alterar situação da cotação */
					}
				}

				/* Define a cotação vencedora (menor preço) */
				if (cdCotacaoVencedora > 0) {
					CotacaoPedidoItem cotacaoPedidoItem = CotacaoPedidoItemDAO.get(cdCotacaoVencedora);
					cotacaoPedidoItem.setTpResultado(CotacaoPedidoItemServices.TP_VENCEDOR);
					if (CotacaoPedidoItemDAO.update(cotacaoPedidoItem, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1; /* ERRO ao alterar situação da cotação */
					}

					/* Grava a quantidade atendida para o item que está sendo processado */
					PedidoCompraItem pedidoCompraItem = PedidoCompraItemDAO.get(cotacaoPedidoItem.getCdEmpresa(), cotacaoPedidoItem.getCdProdutoServico(), cdPedidoCompra);
					pedidoCompraItem.setQtAtendida(cotacaoPedidoItem.getQtDisponivel());
					if (PedidoCompraItemDAO.update(pedidoCompraItem, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -3; /* ERRO ao gravar a quantidade atendida */
					}
				}
			}
			/* Altera situação do pedido de compra para PROCESSADO */
			if (setSituacaoPedidoCompra(cdPedidoCompra, ST_PROCESSADO, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -2; /* ERRO ao alterar situação do pedido de compra */
			}
			if (isConnectionNull)
				connection.commit();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraServices.processarResultado: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}


	public static int processarResultadoManual(int cdPedidoCompra, int cdCotacaoVencedora, int cdProdutoServicoAtual) {

		Connection connection=null;
		boolean isConnectionNull = connection==null;
		try {
			if (cdPedidoCompra <= 0) {
				System.err.println("Erro! PedidoCompraServices.processarResultado: código do Pedido de Compra não informado.");
				return ERR_PEDIDO_COMPRA;
			}
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			/* Define a cotação vencedora */
			if (cdCotacaoVencedora > 0) {
					CotacaoPedidoItem cotacaoPedidoItem = CotacaoPedidoItemDAO.get(cdCotacaoVencedora);
					cotacaoPedidoItem.setTpResultado(CotacaoPedidoItemServices.TP_VENCEDOR);

					ResultSetMap rsmCotacaoPedidoItem = getAllCotacao(cdPedidoCompra, cdProdutoServicoAtual);

					while (rsmCotacaoPedidoItem != null && rsmCotacaoPedidoItem.next()) {

						CotacaoPedidoItem cotacaoPedidoItemAux = CotacaoPedidoItemDAO.get(rsmCotacaoPedidoItem.getInt("CD_COTACAO_PEDIDO_ITEM"));
						/* Em aberto - todas as cotações são definidas como não-vencedoras */
						cotacaoPedidoItemAux.setTpResultado(CotacaoPedidoItemServices.TP_EM_ABERTO);
						if (CotacaoPedidoItemDAO.update(cotacaoPedidoItemAux, connection) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							connection.close();
							return -1; /* ERRO ao alterar situação da cotação */
						}

						if (CotacaoPedidoItemDAO.update(cotacaoPedidoItem, connection) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							connection.close();
							return -1; /* ERRO ao alterar situação da cotação */
						}


					}
					/* Grava a quantidade atendida para o item que está sendo processado */
					PedidoCompraItem pedidoCompraItem = PedidoCompraItemDAO.get(cotacaoPedidoItem.getCdEmpresa(), cdProdutoServicoAtual, cdPedidoCompra);
					pedidoCompraItem.setQtAtendida(cotacaoPedidoItem.getQtDisponivel());
					if (PedidoCompraItemDAO.update(pedidoCompraItem, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -3; /* ERRO ao gravar a quantidade atendida */
					}
			}


			/* Altera situação do pedido de compra para PROCESSADO */
			if (isConnectionNull)
				connection.commit();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraServices.processarResultado: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}





	public static int gerarOrdemCompra(int cdPedidoCompra, int cdUsuario, int cdEmpresa) {
		return gerarOrdemCompra(cdPedidoCompra, cdUsuario, -1, 0, 0, null);
	}

	public static int gerarOrdemCompra(int cdPedidoCompra, int cdUsuario, int cdEmpresa, int tpMovimentoEstoque) {
		return gerarOrdemCompra(cdPedidoCompra, cdUsuario, cdEmpresa, tpMovimentoEstoque, 0, null);
	}

	public static int gerarOrdemCompra(int cdPedidoCompra, int cdUsuario, int cdEmpresa, int tpMovimentoEstoque, int cdLocalEntrega) {
		return gerarOrdemCompra(cdPedidoCompra, cdUsuario, cdEmpresa, tpMovimentoEstoque, cdLocalEntrega, null);
	}

	public static int gerarOrdemCompra(int cdPedidoCompra, int cdUsuario, int cdEmpresa, int tpMovimentoEstoque, int cdLocalEntrega, Connection connection) {

		System.out.println("cdpedidocompra="+cdPedidoCompra+"cdUsuario="+cdUsuario+"cdEmpresa="+cdEmpresa);


		boolean isConnectionNull = connection==null;
		try {
			if (cdPedidoCompra <= 0) {
				System.err.println("Erro! PedidoCompraServices.gerarOrdemCompra: código do Pedido de Compra não informado.");
				return ERR_PEDIDO_COMPRA;
			}
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			int cdMoedaDefault = ParametroServices.getValorOfParametroAsInteger("CD_MOEDA_DEFAULT", 0);
			if (cdMoedaDefault <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return ERR_NOT_CD_MOEDA_DEFAULT;
			}
			int cdCotacaoPedidoItem = 0;
			int cdProdutoServico = 0;
			int cdFornecedor = 0;
			float totalFornecedor = 0;
			ResultSetMap rsmCotacao = getAllCotacaoFornecedor(cdPedidoCompra, -1 /* todos os fornecedores */, CotacaoPedidoItemServices.TP_VENCEDOR);
			while (rsmCotacao != null && rsmCotacao.next()) {
				cdFornecedor = rsmCotacao.getInt("cd_fornecedor");
				cdCotacaoPedidoItem = rsmCotacao.getInt("cd_cotacao_pedido_item");
				cdProdutoServico = rsmCotacao.getInt("cd_produto_servico");
				GregorianCalendar dtEmissao = new GregorianCalendar();

				if (existsOrdemCompra(cdPedidoCompra, cdFornecedor)) {
					if (rsmCotacao.hasMore()) {
						continue;
					}
					else
						break;
				}
				ResultSetMap resumoFornecedor = getResumoCotacaoFornecedor(cdPedidoCompra, cdFornecedor, CotacaoPedidoItemServices.TP_VENCEDOR, connection);
				if (resumoFornecedor != null && resumoFornecedor.next())
					totalFornecedor = resumoFornecedor.getFloat("vl_total_fornecedor");
				else {
					continue;
				}
				String idOrdemCompra = getProximoNrDocumento(cdEmpresa);
				PedidoCompra pedidoCompra = PedidoCompraDAO.get(cdPedidoCompra);
				CotacaoPedidoItem cotacaoPedidoItem = CotacaoPedidoItemDAO.get(cdCotacaoPedidoItem);
				OrdemCompra ordemCompra = new OrdemCompra(0, // cdOrdemCompra
						  dtEmissao,
						  OrdemCompraServices.ST_EM_ABERTO, // stOrdemCompra
						  null, // dtLimiteEntrega
						  idOrdemCompra,
						  idOrdemCompra, // nrOrdemCompra
						  cotacaoPedidoItem.getCdFornecedor(), // cdFornecedor
						  cdLocalEntrega, // cdLocalEntrega
						  cdMoedaDefault, // cdMoeda
						  0, // cdTabelaPreco
						  (cdUsuario > 0 ? com.tivic.manager.seg.UsuarioDAO.get(cdUsuario).getCdPessoa() : null),// cdComprador
						  0, // vlDesconto
						  0, // vlAcrescimo
						  tpMovimentoEstoque,
						  "REF. PEDIDO DE COMPRA Nº " + pedidoCompra.getNrPedidoCompra() + " (COD. " + pedidoCompra.getCdPedidoCompra() + ")", // txtObservacao
						  totalFornecedor, // vlTotalDocumento
						  cotacaoPedidoItem.getCdEmpresa(), // cdEmpresa
						  0/**/,
						  0/**/
				);
				int cdOrdemCompra = OrdemCompraDAO.insert(ordemCompra, connection);
				if (cdOrdemCompra <= 0) {
					if (isConnectionNull)
					Conexao.rollback(connection);
					return -1; /* ERRO ao gravar ordem de compra */
				}
				while (rsmCotacao != null && rsmCotacao.getInt("cd_fornecedor") == cdFornecedor) {
					cdFornecedor = rsmCotacao.getInt("cd_fornecedor");
					cdCotacaoPedidoItem = rsmCotacao.getInt("cd_cotacao_pedido_item");
					cdEmpresa = rsmCotacao.getInt("cd_empresa");
					cdProdutoServico = rsmCotacao.getInt("cd_produto_servico");
					float vlAcrescimo = rsmCotacao.getFloat("vl_frete") + rsmCotacao.getFloat("vl_tributos");
					cotacaoPedidoItem = CotacaoPedidoItemDAO.get(cdCotacaoPedidoItem);
					PedidoCompraItem pedidoCompraItem = PedidoCompraItemDAO.get(cdEmpresa, cdProdutoServico, cdPedidoCompra);
					OrdemCompraItem ordemCompraItem = new OrdemCompraItem (cdOrdemCompra,
																		   cdEmpresa,
																		   cdProdutoServico,
																		   cdCotacaoPedidoItem,
																		   pedidoCompraItem.getQtSolicitada(), // qtComprada
																		   rsmCotacao.getFloat("vl_cotacao"), // vlUnitario
																		   rsmCotacao.getFloat("vl_desconto"), // vlDesconto
																		   vlAcrescimo,
																		   null, // dtPrevisaoEntrega,
																		   0, //cdPedidoVenda
																		   rsmCotacao.getInt("cd_unidade_medida") // cdUnidadeMedida
					);
					if (OrdemCompraItemDAO.insert(ordemCompraItem, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1; /* ERRO ao gravar item da ordem de compra */
					}
					if (!rsmCotacao.next())
						break;
					else {
						if (rsmCotacao.getInt("cd_fornecedor") != cdFornecedor) {
							rsmCotacao.previous();
							break;
						}
					}
				}
			}
			if (isConnectionNull)
				connection.commit();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraServices.gerarOrdemCompra: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllProdutoNaoCotado(int cdPedidoCompra, int cdFornecedor) {
		return getAllProdutoNaoCotado(cdPedidoCompra, cdFornecedor, null);
	}

	public static ResultSetMap getAllProdutoNaoCotado(int cdPedidoCompra, int cdFornecedor, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.cd_produto_servico, A.qt_solicitada, " +
											 "	B.nm_produto_servico, B.id_produto_servico, " +
											 "  D.sg_unidade_medida " +
											 "FROM adm_pedido_compra_item A, grl_produto_servico B, grl_produto_servico_empresa C " +
											 "	LEFT OUTER JOIN grl_unidade_medida D " +
											 "     ON (C.cd_unidade_medida = D.cd_unidade_medida) " +
											 "WHERE (A.cd_produto_servico = B.cd_produto_servico) " +
											 "	AND (A.cd_empresa = C.cd_empresa AND A.cd_produto_servico = C.cd_produto_servico) " +
											 "	AND (A.cd_pedido_compra = ?) " +
											 "	AND A.cd_produto_servico NOT IN " +
											 "  (SELECT Z.cd_produto_servico " +
											 "	 	FROM adm_cotacao_pedido_item Z " +
											 "      WHERE (Z.cd_pedido_compra = ?)" +
											 " 		AND Z.cd_fornecedor = ?) ");
			pstmt.setInt(1, cdPedidoCompra);
			pstmt.setInt(2, cdPedidoCompra);
			pstmt.setInt(3, cdFornecedor);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraServices.getAllProdutoNaoCotado: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static boolean existsOrdemCompra(int cdPedidoCompra, int cdFornecedor) {
		return existsOrdemCompra(cdPedidoCompra, cdFornecedor, null);
	}

	public static boolean existsOrdemCompra(int cdPedidoCompra, int cdFornecedor, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT DISTINCT A.cd_ordem_compra " +
											 "FROM adm_ordem_compra_item A " +
											 "WHERE A.cd_cotacao_pedido_item  IN " +
											 "(SELECT B.cd_cotacao_pedido_item " +
											 " FROM adm_cotacao_pedido_item B " +
											 " WHERE (B.cd_pedido_compra = ?) " +
											 " 	 AND (B.cd_fornecedor = ?))");
			pstmt.setInt(1, cdPedidoCompra);
			pstmt.setInt(2, cdFornecedor);
			ResultSetMap retorno = new ResultSetMap(pstmt.executeQuery());
			return retorno.size() > 0 ? true : false;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraServices.existsOrdemCompra: " + e);
			return false;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
