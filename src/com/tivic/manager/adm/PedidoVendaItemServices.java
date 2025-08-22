package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.alm.DocumentoSaida;
import com.tivic.manager.alm.DocumentoSaidaDAO;
import com.tivic.manager.alm.DocumentoSaidaServices;
import com.tivic.manager.alm.SaidaLocalItem;
import com.tivic.manager.alm.SaidaLocalItemServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.Util;


import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class PedidoVendaItemServices {

	public static final int ERR_ETG_LOCAL_ARMAZENAMENTO = -2;
	public static final int ERR_ETG_DOC_SAIDA = -3;
	public static final int ERR_ETG_QTD_INVALIDA = -4;
	public static final int ERR_ETG_PEDIDO_NAO_FECHADO = -5;


	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		String adicionalSql = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("dtEntrega")) {
				GregorianCalendar dtEntrega = Util.convStringToCalendar(criterios.get(i).getValue(), null);
				adicionalSql += dtEntrega==null ? " AND 1 <> 1" :
					" AND EXISTS (SELECT * " +
					"			  FROM alm_saida_local_item H " +
					"			  WHERE H.cd_pedido_venda = A.cd_pedido_venda " +
					"				AND H.cd_empresa = A.cd_empresa " +
					"				AND H.cd_produto_servico = A.cd_produto_servico " +
					"				AND NOT H.dt_saida IS NULL " +
					"				AND H.dt_saida " + ItemComparator.getOperatorComparation(criterios.get(i).getTypeComparation()) + " '" + Util.formatDate(dtEntrega, "dd/MM/yyyy HH:mm:ss") + "') ";
				criterios.remove(i);
				i--;
			}
		}
		return Search.find("SELECT A.cd_pedido_venda, A.cd_empresa, A.cd_produto_servico, A.cd_unidade_medida, A.qt_solicitada, " +
				"A.txt_pedido_item, A.vl_unitario, A.vl_desconto AS vl_desconto_item, A.vl_acrescimo AS vl_acrescimo_item, " +
				"A.vl_desconto_promocao, A.lg_reserva_estoque, A.dt_entrega_prevista, A.cd_tabela_preco, A.cd_tabela_preco_promocao, " +
				"A.cd_produto_servico_preco, A.cd_regra_promocao, A.cd_ordem_servico, A.cd_ordem_servico, " +
				"B.nm_produto_servico, C.nm_unidade_medida, C.sg_unidade_medida, " +
				"D.cd_cliente, D.dt_pedido, D.dt_limite_entrega, D.nr_pedido_venda, D.vl_acrescimo, D.vl_desconto, " +
				"D.tp_pedido_venda, D.st_pedido_venda, D.cd_tipo_operacao, E.nm_pessoa AS nm_cliente, " +
				"F.nm_tipo_operacao, G.nm_pessoa AS nm_vendedor, " +
				"(SELECT SUM(B.qt_saida + B.qt_saida_consignada) " +
				" FROM alm_saida_local_item B " +
				" WHERE B.cd_pedido_venda = A.cd_pedido_venda " +
				"   AND B.cd_empresa = A.cd_empresa " +
				"   AND B.cd_produto_servico = A.cd_produto_servico) AS qt_entregue, " +
				"(SELECT MAX(B.dt_saida) " +
				" FROM alm_saida_local_item B " +
				" WHERE B.cd_pedido_venda = A.cd_pedido_venda " +
				"   AND B.cd_empresa = A.cd_empresa " +
				"   AND B.cd_produto_servico = A.cd_produto_servico) AS dt_ultima_entrega " +
				"FROM adm_pedido_venda_item A " +
				"JOIN grl_produto_servico B ON (A.cd_produto_servico = B.cd_produto_servico) " +
				"LEFT OUTER JOIN grl_unidade_medida C ON (A.cd_unidade_medida = C.cd_unidade_medida) " +
				"JOIN adm_pedido_venda D ON (A.cd_pedido_venda = D.cd_pedido_venda) " +
				"LEFT OUTER JOIN grl_pessoa E ON (D.cd_cliente = E.cd_pessoa) " +
				"LEFT OUTER JOIN adm_tipo_operacao F ON (D.cd_tipo_operacao = F.cd_tipo_operacao) " +
				"LEFT OUTER JOIN grl_pessoa G ON (D.cd_vendedor = G.cd_pessoa) " +
				"WHERE 1 = 1" + adicionalSql, criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static HashMap<String, Object> registrarEntregas(ArrayList<HashMap<String, Object>> entregas, GregorianCalendar dtEntrega) {
		return registrarEntregas(entregas, dtEntrega, null);
	}

	public static HashMap<String, Object> registrarEntregas(ArrayList<HashMap<String, Object>> entregas, GregorianCalendar dtEntrega,
			Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			HashMap<String, Object> result = new HashMap<String, Object>();

			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			for (int i=0; entregas!=null && i<entregas.size(); i++) {
				HashMap<String, Object> hash = entregas.get(i);
				int cdPedidoVenda = (Integer)hash.get("cdPedidoVenda");
				int cdEmpresa = (Integer)hash.get("cdEmpresa");
				int cdProdutoServico = (Integer)hash.get("cdProdutoServico");
				float qtEntregue = (Float)hash.get("qtEntregue");

				if (registarEntrega(cdPedidoVenda, cdEmpresa, cdProdutoServico, qtEntregue, dtEntrega) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return null;
				}
			}

			if (isConnectionNull)
				connection.commit();

			result.put("code", 1);

			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int registarEntrega(int cdPedidoVenda, int cdEmpresa, int cdProdutoServico, float qtEntregue,
			GregorianCalendar dtEntrega) {
		return registarEntrega(cdPedidoVenda, cdEmpresa, cdProdutoServico, qtEntregue, dtEntrega, null);
	}

	public static int registarEntrega(int cdPedidoVenda, int cdEmpresa, int cdProdutoServico, float qtEntregue,
			GregorianCalendar dtEntrega, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (qtEntregue<=0 || dtEntrega==null)
				return -1;

			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			PedidoVenda pedido = PedidoVendaDAO.get(cdPedidoVenda, connection);
			if (pedido.getStPedidoVenda() != PedidoVendaServices.ST_FECHADO)
				return ERR_ETG_PEDIDO_NAO_FECHADO;

			PedidoVendaItem item = PedidoVendaItemDAO.get(cdPedidoVenda, cdEmpresa, cdProdutoServico, connection);

			PreparedStatement pstmt = connection.prepareStatement("SELECT SUM(B.qt_saida + B.qt_saida_consignada) AS qt_entregue " +
				"FROM alm_saida_local_item B " +
				"WHERE B.cd_pedido_venda = ? " +
				"  AND B.cd_empresa = ? " +
				"  AND B.cd_produto_servico = ?");
			pstmt.setInt(1, cdPedidoVenda);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdProdutoServico);
			ResultSet rs = pstmt.executeQuery();
			float qtEntregueOld = !rs.next() ? 0 : rs.getFloat("qt_entregue");
			if (item.getQtSolicitada() - qtEntregueOld < qtEntregue)
				return ERR_ETG_QTD_INVALIDA;

			pstmt = connection.prepareStatement("SELECT DISTINCT A.cd_documento_saida, " +
					"A.cd_item " +
					"FROM alm_documento_saida_item A, adm_venda_saida_item B " +
					"WHERE A.cd_documento_saida = B.cd_documento_saida " +
					"  AND A.cd_produto_servico = B.cd_produto_servico " +
					"  AND A.cd_empresa = B.cd_empresa " +
					"  AND A.cd_item = B.cd_item " +
					"  AND B.cd_pedido_venda = ? " +
					"  AND B.cd_produto_servico = ?");
			pstmt.setInt(1, cdPedidoVenda);
			pstmt.setInt(2, cdProdutoServico);
			rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdDocumentoSaida = isNext ? rs.getInt("cd_documento_saida") : 0;
			int cdItem = isNext ? rs.getInt("cd_item") : 0;
			if (cdDocumentoSaida<=0 || cdItem<=0)
				return ERR_ETG_DOC_SAIDA;

			DocumentoSaida docSaida = DocumentoSaidaDAO.get(cdDocumentoSaida, connection);
			if (docSaida.getStDocumentoSaida() == DocumentoSaidaServices.ST_CANCELADO)
				throw new Exception("Doc. Saída cancelado...");

			if (docSaida.getStDocumentoSaida() == DocumentoSaidaServices.ST_EM_CONFERENCIA) {
				pstmt = connection.prepareStatement("SELECT SUM(A.vl_unitario * A.qt_saida + A.vl_acrescimo - A.vl_desconto) AS vl_total " +
						"FROM alm_documento_saida_item A " +
						"WHERE A.cd_documento_saida = ?");
				pstmt.setInt(1, cdDocumentoSaida);
				rs = pstmt.executeQuery();
				float vlTotal = !rs.next() ? 0 : rs.getFloat("vl_total");
				if (DocumentoSaidaServices.updatTotaisDocumentoSaida(cdDocumentoSaida, vlTotal, 0, 0, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return ERR_ETG_DOC_SAIDA;
				}

				if (DocumentoSaidaServices.liberarSaida(cdDocumentoSaida, 0, connection).getCode() <= 0)
					throw new Exception("Erros reportados ao liberar Doc. de Saída...");
			}

			int cdLocal = ParametroServices.getValorOfParametroAsInteger("CD_LOCAL_ARMAZENAMENTO_DEFAULT", 0, cdEmpresa, connection);
			if (cdLocal<=0)
				return ERR_ETG_LOCAL_ARMAZENAMENTO;

			if (SaidaLocalItemServices.insert(new SaidaLocalItem(0 /*cdSaida*/,
					cdProdutoServico,
					cdDocumentoSaida,
					cdEmpresa,
					cdLocal,
					cdPedidoVenda,
					dtEntrega,
					qtEntregue,
					0 /*qtSaidaConsignada*/,
					SaidaLocalItemServices.ST_RECEBIDO_CLIENTE /*stSaudaLocalItem*/,
					"" /*idSaidaLocalItem*/,
					cdItem), connection) <= 0) {
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

}
