package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class OrdemCompraItemDAO{

	public static int insert(OrdemCompraItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(OrdemCompraItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_ordem_compra_item (cd_ordem_compra,"+
			                                  "cd_empresa,"+
			                                  "cd_produto_servico,"+
			                                  "cd_cotacao_pedido_item,"+
			                                  "qt_comprada,"+
			                                  "vl_unitario,"+
			                                  "vl_desconto,"+
			                                  "vl_acrescimo,"+
			                                  "dt_previsao_entrega,"+
			                                  "cd_pedido_venda,"+
			                                  "cd_unidade_medida) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdOrdemCompra()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdOrdemCompra());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdProdutoServico());
			if(objeto.getCdCotacaoPedidoItem()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCotacaoPedidoItem());
			pstmt.setFloat(5,objeto.getQtComprada());
			pstmt.setFloat(6,objeto.getVlUnitario());
			pstmt.setFloat(7,objeto.getVlDesconto());
			pstmt.setFloat(8,objeto.getVlAcrescimo());
			if(objeto.getDtPrevisaoEntrega()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtPrevisaoEntrega().getTimeInMillis()));
			if(objeto.getCdPedidoVenda()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdPedidoVenda());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdUnidadeMedida());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OrdemCompraItem objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(OrdemCompraItem objeto, int cdOrdemCompraOld, int cdEmpresaOld, int cdProdutoServicoOld) {
		return update(objeto, cdOrdemCompraOld, cdEmpresaOld, cdProdutoServicoOld, null);
	}

	public static int update(OrdemCompraItem objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(OrdemCompraItem objeto, int cdOrdemCompraOld, int cdEmpresaOld, int cdProdutoServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_ordem_compra_item SET cd_ordem_compra=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_cotacao_pedido_item=?,"+
												      		   "qt_comprada=?,"+
												      		   "vl_unitario=?,"+
												      		   "vl_desconto=?,"+
												      		   "vl_acrescimo=?,"+
												      		   "dt_previsao_entrega=?,"+
												      		   "cd_pedido_venda=?,"+
												      		   "cd_unidade_medida=? WHERE cd_ordem_compra=? AND cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1,objeto.getCdOrdemCompra());
			pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setInt(3,objeto.getCdProdutoServico());
			if(objeto.getCdCotacaoPedidoItem()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCotacaoPedidoItem());
			pstmt.setFloat(5,objeto.getQtComprada());
			pstmt.setFloat(6,objeto.getVlUnitario());
			pstmt.setFloat(7,objeto.getVlDesconto());
			pstmt.setFloat(8,objeto.getVlAcrescimo());
			if(objeto.getDtPrevisaoEntrega()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtPrevisaoEntrega().getTimeInMillis()));
			if(objeto.getCdPedidoVenda()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdPedidoVenda());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdUnidadeMedida());
			pstmt.setInt(12, cdOrdemCompraOld!=0 ? cdOrdemCompraOld : objeto.getCdOrdemCompra());
			pstmt.setInt(13, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(14, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOrdemCompra, int cdEmpresa, int cdProdutoServico) {
		return delete(cdOrdemCompra, cdEmpresa, cdProdutoServico, null);
	}

	public static int delete(int cdOrdemCompra, int cdEmpresa, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_ordem_compra_item WHERE cd_ordem_compra=? AND cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdOrdemCompra);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdProdutoServico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OrdemCompraItem get(int cdOrdemCompra, int cdEmpresa, int cdProdutoServico) {
		return get(cdOrdemCompra, cdEmpresa, cdProdutoServico, null);
	}

	public static OrdemCompraItem get(int cdOrdemCompra, int cdEmpresa, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_ordem_compra_item WHERE cd_ordem_compra=? AND cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdOrdemCompra);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdProdutoServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OrdemCompraItem(rs.getInt("cd_ordem_compra"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_cotacao_pedido_item"),
						rs.getFloat("qt_comprada"),
						rs.getFloat("vl_unitario"),
						rs.getFloat("vl_desconto"),
						rs.getFloat("vl_acrescimo"),
						(rs.getTimestamp("dt_previsao_entrega")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_previsao_entrega").getTime()),
						rs.getInt("cd_pedido_venda"),
						rs.getInt("cd_unidade_medida"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_ordem_compra_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraItemDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_ordem_compra_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
