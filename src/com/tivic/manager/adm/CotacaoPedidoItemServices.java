package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class CotacaoPedidoItemServices {

	public static final int TP_EM_ABERTO = 0;
	public static final int TP_VENCEDOR = 1;
	public static final int TP_FORA_ESPECIFICACAO = 2;

	public static final String[] tipoResultado = {"Em aberto",
												  "Vencedor",
												  "Fora especificação"};

	public static int insert(CotacaoPedidoItem objeto){
		return insert(objeto, null);
	}

	public static int insert(CotacaoPedidoItem objeto, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
			}

			PedidoCompra pedidoCompra = PedidoCompraDAO.get(objeto.getCdPedidoCompra());
			int cdPedidoCompra = objeto.getCdPedidoCompra();
			if (pedidoCompra.getStPedidoCompra() != PedidoCompraServices.ST_EM_ABERTO) {
				/* Altera a situação do pedido de compra para: EM ABERTO */
				if (PedidoCompraServices.setSituacaoPedidoCompra(cdPedidoCompra, PedidoCompraServices.ST_EM_ABERTO, connection) <= 0) {
					if (isConnectionNull)
						Conexao.desconectar(connection);
					return -1;
				}

				/* Anula o processamento efetuado antes */
				int cdProdutoServico = 0;
				ResultSetMap rsmPedidoCompraItem = PedidoCompraServices.getAllItens(cdPedidoCompra);
				while (rsmPedidoCompraItem != null && rsmPedidoCompraItem.next()) {
					cdProdutoServico = rsmPedidoCompraItem.getInt("cd_produto_servico");
					PedidoCompraItemServices.anularProcessamento(cdPedidoCompra, cdProdutoServico, connection);
				}
			}

			if (CotacaoPedidoItemDAO.insert(objeto, connection) <= 0) {
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

	public static int update(CotacaoPedidoItem objeto){
		return update(objeto, null);
	}

	public static int update(CotacaoPedidoItem objeto, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
			}

			PedidoCompra pedidoCompra = PedidoCompraDAO.get(objeto.getCdPedidoCompra());
			int cdPedidoCompra = objeto.getCdPedidoCompra();
			if (pedidoCompra.getStPedidoCompra() != PedidoCompraServices.ST_EM_ABERTO) {
				/* Altera a situação do pedido de compra para: EM ABERTO */
				if (PedidoCompraServices.setSituacaoPedidoCompra(cdPedidoCompra, PedidoCompraServices.ST_EM_ABERTO, connection) <= 0) {
					if (isConnectionNull)
						Conexao.desconectar(connection);
					return -1;
				}

				/* Anula o processamento efetuado antes */
				int cdProdutoServico = 0;
				ResultSetMap rsmPedidoCompraItem = PedidoCompraServices.getAllItens(cdPedidoCompra);
				while (rsmPedidoCompraItem != null && rsmPedidoCompraItem.next()) {
					cdProdutoServico = rsmPedidoCompraItem.getInt("cd_produto_servico");
					PedidoCompraItemServices.anularProcessamento(cdPedidoCompra, cdProdutoServico, connection);
				}
			}

			if (CotacaoPedidoItemDAO.update(objeto, connection) <= 0) {
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

	public static ResultSetMap getCotacaoPedidoItem(int cdCotacaoPedidoItem) {
		return getCotacaoPedidoItem(cdCotacaoPedidoItem, null);
	}

	public static ResultSetMap getCotacaoPedidoItem(int cdCotacaoPedidoItem, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					 "SELECT A.*, " +
					 "	B.nm_pessoa AS nm_fornecedor, " +
					 " 	C.nm_pessoa_contato, C.nm_setor AS nm_setor_pessoa_contato, C.nr_telefone1 AS nr_telefone_pessoa_contato, " +
					 "	C.nr_telefone2 AS nr_telefone2_pessoa_contato, C.nr_celular AS nr_celular_pessoa_contato, " +
					 "	C.nm_email AS nm_email_pessoa_contato, " +
					 "	D.nm_plano_pagamento, " +
					 "	E.st_produto_empresa, E.id_reduzido, " +
					 "	F.nm_produto_servico, F.txt_produto_servico, F.tp_produto_servico, F.id_produto_servico, " +
					 "	F.id_produto_servico, F.sg_produto_servico, " +
					 "  F.cd_classificacao_fiscal, F.cd_fabricante, " +
					 "  H.sg_unidade_medida, H.nm_unidade_medida, " +
					 "	I.id_pedido_compra, I.dt_pedido_compra, " +
					 "	J.qt_solicitada " +
					 "FROM adm_cotacao_pedido_item A " +
					 "	JOIN adm_pedido_compra I ON (A.cd_pedido_compra = I.cd_pedido_compra) " +
					 "	JOIN adm_pedido_compra_item J ON (A.cd_pedido_compra = J.cd_pedido_compra AND A.cd_produto_servico = J.cd_produto_servico AND A.cd_empresa = J.cd_empresa) " +
					 "	JOIN grl_produto_servico_empresa E ON (A.cd_produto_servico = E.cd_produto_servico AND A.cd_empresa = E.cd_empresa) " +
					 "	JOIN grl_produto_servico F ON (E.cd_produto_servico = F.cd_produto_servico) " +
					 "	LEFT OUTER JOIN grl_pessoa B ON (A.cd_fornecedor = B.cd_pessoa) " +
					 "	LEFT OUTER JOIN grl_pessoa_contato C ON (A.cd_pessoa_contato = C.cd_pessoa_contato AND A.cd_pessoa_juridica = C.cd_pessoa_juridica) " +
					 "	LEFT OUTER JOIN adm_plano_pagamento D ON (A.cd_plano_pagamento = D.cd_plano_pagamento) " +
					 "	LEFT OUTER JOIN grl_produto G ON (F.cd_produto_servico = G.cd_produto_servico) " +
					 "	LEFT OUTER JOIN grl_unidade_medida H ON (E.cd_unidade_medida = H.cd_unidade_medida) " +
					 "WHERE A.cd_cotacao_pedido_item = ?");
			pstmt.setInt(1, cdCotacaoPedidoItem);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CotacaoPedidoItemServices.getCotacaoPedidoItem: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
