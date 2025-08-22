package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class PedidoVendaItemDAO{

	public static int insert(PedidoVendaItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(PedidoVendaItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_pedido_venda_item (cd_pedido_venda,"+
			                                  "cd_empresa,"+
			                                  "cd_produto_servico,"+
			                                  "cd_unidade_medida,"+
			                                  "qt_solicitada,"+
			                                  "txt_pedido_item,"+
			                                  "vl_unitario,"+
			                                  "vl_desconto,"+
			                                  "vl_acrescimo,"+
			                                  "vl_desconto_promocao,"+
			                                  "lg_reserva_estoque,"+
			                                  "dt_entrega_prevista,"+
			                                  "cd_tabela_preco,"+
			                                  "cd_tabela_preco_promocao,"+
			                                  "cd_produto_servico_preco,"+
			                                  "cd_regra_promocao,"+
			                                  "cd_ordem_servico_item,"+
			                                  "cd_ordem_servico) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdPedidoVenda()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPedidoVenda());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdProdutoServico());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUnidadeMedida());
			pstmt.setFloat(5,objeto.getQtSolicitada());
			pstmt.setString(6,objeto.getTxtPedidoItem());
			pstmt.setFloat(7,objeto.getVlUnitario());
			pstmt.setFloat(8,objeto.getVlDesconto());
			pstmt.setFloat(9,objeto.getVlAcrescimo());
			pstmt.setFloat(10,objeto.getVlDescontoPromocao());
			pstmt.setInt(11,objeto.getLgReservaEstoque());
			if(objeto.getDtEntregaPrevista()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtEntregaPrevista().getTimeInMillis()));
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdTabelaPreco());
			if(objeto.getCdTabelaPrecoPromocao()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdTabelaPrecoPromocao());
			if(objeto.getCdProdutoServicoPreco()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdProdutoServicoPreco());
			if(objeto.getCdRegraPromocao()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdRegraPromocao());
			if(objeto.getCdOrdemServicoItem()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdOrdemServicoItem());
			if(objeto.getCdOrdemServico()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdOrdemServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PedidoVendaItem objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(PedidoVendaItem objeto, int cdPedidoVendaOld, int cdEmpresaOld, int cdProdutoServicoOld) {
		return update(objeto, cdPedidoVendaOld, cdEmpresaOld, cdProdutoServicoOld, null);
	}

	public static int update(PedidoVendaItem objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(PedidoVendaItem objeto, int cdPedidoVendaOld, int cdEmpresaOld, int cdProdutoServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_pedido_venda_item SET cd_pedido_venda=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_unidade_medida=?,"+
												      		   "qt_solicitada=?,"+
												      		   "txt_pedido_item=?,"+
												      		   "vl_unitario=?,"+
												      		   "vl_desconto=?,"+
												      		   "vl_acrescimo=?,"+
												      		   "vl_desconto_promocao=?,"+
												      		   "lg_reserva_estoque=?,"+
												      		   "dt_entrega_prevista=?,"+
												      		   "cd_tabela_preco=?,"+
												      		   "cd_tabela_preco_promocao=?,"+
												      		   "cd_produto_servico_preco=?,"+
												      		   "cd_regra_promocao=?,"+
												      		   "cd_ordem_servico_item=?,"+
												      		   "cd_ordem_servico=? WHERE cd_pedido_venda=? AND cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1,objeto.getCdPedidoVenda());
			pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setInt(3,objeto.getCdProdutoServico());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUnidadeMedida());
			pstmt.setFloat(5,objeto.getQtSolicitada());
			pstmt.setString(6,objeto.getTxtPedidoItem());
			pstmt.setFloat(7,objeto.getVlUnitario());
			pstmt.setFloat(8,objeto.getVlDesconto());
			pstmt.setFloat(9,objeto.getVlAcrescimo());
			pstmt.setFloat(10,objeto.getVlDescontoPromocao());
			pstmt.setInt(11,objeto.getLgReservaEstoque());
			if(objeto.getDtEntregaPrevista()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtEntregaPrevista().getTimeInMillis()));
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdTabelaPreco());
			if(objeto.getCdTabelaPrecoPromocao()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdTabelaPrecoPromocao());
			if(objeto.getCdProdutoServicoPreco()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdProdutoServicoPreco());
			if(objeto.getCdRegraPromocao()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdRegraPromocao());
			if(objeto.getCdOrdemServicoItem()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdOrdemServicoItem());
			if(objeto.getCdOrdemServico()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdOrdemServico());
			pstmt.setInt(19, cdPedidoVendaOld!=0 ? cdPedidoVendaOld : objeto.getCdPedidoVenda());
			pstmt.setInt(20, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(21, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPedidoVenda, int cdEmpresa, int cdProdutoServico) {
		return delete(cdPedidoVenda, cdEmpresa, cdProdutoServico, null);
	}

	public static int delete(int cdPedidoVenda, int cdEmpresa, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_pedido_venda_item WHERE cd_pedido_venda=? AND cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdPedidoVenda);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdProdutoServico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PedidoVendaItem get(int cdPedidoVenda, int cdEmpresa, int cdProdutoServico) {
		return get(cdPedidoVenda, cdEmpresa, cdProdutoServico, null);
	}

	public static PedidoVendaItem get(int cdPedidoVenda, int cdEmpresa, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_pedido_venda_item WHERE cd_pedido_venda=? AND cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdPedidoVenda);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdProdutoServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PedidoVendaItem(rs.getInt("cd_pedido_venda"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_unidade_medida"),
						rs.getFloat("qt_solicitada"),
						rs.getString("txt_pedido_item"),
						rs.getFloat("vl_unitario"),
						rs.getFloat("vl_desconto"),
						rs.getFloat("vl_acrescimo"),
						rs.getFloat("vl_desconto_promocao"),
						rs.getInt("lg_reserva_estoque"),
						(rs.getTimestamp("dt_entrega_prevista")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_entrega_prevista").getTime()),
						rs.getInt("cd_tabela_preco"),
						rs.getInt("cd_tabela_preco_promocao"),
						rs.getInt("cd_produto_servico_preco"),
						rs.getInt("cd_regra_promocao"),
						rs.getInt("cd_ordem_servico_item"),
						rs.getInt("cd_ordem_servico"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaItemDAO.get: " + e);
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
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_pedido_venda_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaItemDAO.getAll: " + e);
			return null;
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
		return Search.find("SELECT * FROM adm_pedido_venda_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
