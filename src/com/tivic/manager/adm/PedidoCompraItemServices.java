package com.tivic.manager.adm;

import java.sql.Connection;

import com.tivic.manager.adm.PedidoCompraServices;
import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class PedidoCompraItemServices {

	public static int insert(PedidoCompraItem objeto){
		return insert(objeto, null);
	}

	public static int insert(PedidoCompraItem objeto, Connection connection){
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

			if (PedidoCompraItemDAO.insert(objeto, connection) <= 0) {
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

	public static int update(PedidoCompraItem objeto){
		return update(objeto, null);
	}

	public static int update(PedidoCompraItem objeto, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
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

			if (PedidoCompraItemDAO.update(objeto, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int anularProcessamento(int cdPedidoCompra, int cdProdutoServico) {
		return anularProcessamento(cdPedidoCompra, cdProdutoServico, null);
	}

	public static int anularProcessamento(int cdPedidoCompra, int cdProdutoServico, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			PedidoCompra pedidoCompra = PedidoCompraDAO.get(cdPedidoCompra);
			int cdEmpresa = pedidoCompra.getCdEmpresa();

			ResultSetMap rsmCotacaoPedidoItem = PedidoCompraServices.getAllCotacao(cdPedidoCompra, cdProdutoServico);
			int cdCotacaoPedidoItem = 0;
			while (rsmCotacaoPedidoItem != null && rsmCotacaoPedidoItem.next()) {
				cdCotacaoPedidoItem = rsmCotacaoPedidoItem.getInt("cd_cotacao_pedido_item");
				CotacaoPedidoItem cotacaoPedidoItem = CotacaoPedidoItemDAO.get(cdCotacaoPedidoItem);
				/* Não processadas - todas as cotações são definidas como NÃO-PROCESSADAS */
				cotacaoPedidoItem.setTpResultado(-1);
				if (CotacaoPedidoItemDAO.update(cotacaoPedidoItem) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1; /* ERRO ao alterar situação da cotação */
				}

				/* Zera a quantidade atendida para o item que está sendo processado */
				if (cdCotacaoPedidoItem > 0) {
					PedidoCompraItem objeto = PedidoCompraItemDAO.get(cdEmpresa, cdProdutoServico, cdPedidoCompra);
					objeto.setQtAtendida(0);
					if (PedidoCompraItemDAO.update(objeto) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -2; /* ERRO ao zerar a quantidade atendida */
					}
				}
			}
			if (isConnectionNull)
				connection.commit();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraItemServices.anularProcessamento: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

}
