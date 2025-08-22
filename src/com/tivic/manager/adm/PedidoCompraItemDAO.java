package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.Util;

public class PedidoCompraItemDAO{

	public static int insert(PedidoCompraItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(PedidoCompraItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_pedido_compra_item (cd_empresa,"+
			                                  "cd_produto_servico,"+
			                                  "cd_pedido_compra,"+
			                                  "cd_unidade_medida,"+
			                                  "qt_solicitada,"+
			                                  "qt_atendida,"+
			                                  "nr_prazo_entrega,"+
			                                  "dt_inicial,"+
			                                  "dt_final,"+
			                                  "lg_ativo,"+
			                                  "txt_pedido_item) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpresa());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProdutoServico());
			if(objeto.getCdPedidoCompra()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPedidoCompra());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUnidadeMedida());
			pstmt.setFloat(5,objeto.getQtSolicitada());
			pstmt.setFloat(6,objeto.getQtAtendida());
			pstmt.setInt(7,objeto.getNrPrazoEntrega());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setInt(10,objeto.getLgAtivo());
			pstmt.setString(11,objeto.getTxtPedidoItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PedidoCompraItem objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(PedidoCompraItem objeto, int cdEmpresaOld, int cdProdutoServicoOld, int cdPedidoCompraOld) {
		return update(objeto, cdEmpresaOld, cdProdutoServicoOld, cdPedidoCompraOld, null);
	}

	public static int update(PedidoCompraItem objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(PedidoCompraItem objeto, int cdEmpresaOld, int cdProdutoServicoOld, int cdPedidoCompraOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_pedido_compra_item SET cd_empresa=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_pedido_compra=?,"+
												      		   "cd_unidade_medida=?,"+
												      		   "qt_solicitada=?,"+
												      		   "qt_atendida=?,"+
												      		   "nr_prazo_entrega=?,"+
												      		   "dt_inicial=?,"+
												      		   "dt_final=?,"+
												      		   "lg_ativo=?,"+
												      		   "txt_pedido_item=? WHERE cd_empresa=? AND cd_produto_servico=? AND cd_pedido_compra=?");
			pstmt.setInt(1,objeto.getCdEmpresa());
			pstmt.setInt(2,objeto.getCdProdutoServico());
			pstmt.setInt(3,objeto.getCdPedidoCompra());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUnidadeMedida());
			pstmt.setFloat(5,objeto.getQtSolicitada());
			pstmt.setFloat(6,objeto.getQtAtendida());
			pstmt.setInt(7,objeto.getNrPrazoEntrega());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setInt(10,objeto.getLgAtivo());
			pstmt.setString(11,objeto.getTxtPedidoItem());
			pstmt.setInt(12, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(13, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.setInt(14, cdPedidoCompraOld!=0 ? cdPedidoCompraOld : objeto.getCdPedidoCompra());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpresa, int cdProdutoServico, int cdPedidoCompra) {
		return delete(cdEmpresa, cdProdutoServico, cdPedidoCompra, null);
	}

	public static int delete(int cdEmpresa, int cdProdutoServico, int cdPedidoCompra, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_pedido_compra_item WHERE cd_empresa=? AND cd_produto_servico=? AND cd_pedido_compra=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdPedidoCompra);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PedidoCompraItem get(int cdEmpresa, int cdProdutoServico, int cdPedidoCompra) {
		return get(cdEmpresa, cdProdutoServico, cdPedidoCompra, null);
	}

	public static PedidoCompraItem get(int cdEmpresa, int cdProdutoServico, int cdPedidoCompra, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_pedido_compra_item WHERE cd_empresa=? AND cd_produto_servico=? AND cd_pedido_compra=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdPedidoCompra);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PedidoCompraItem(rs.getInt("cd_empresa"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_pedido_compra"),
						rs.getInt("cd_unidade_medida"),
						rs.getFloat("qt_solicitada"),
						rs.getFloat("qt_atendida"),
						rs.getInt("nr_prazo_entrega"),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()),
						rs.getInt("lg_ativo"),
						rs.getString("txt_pedido_item"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_pedido_compra_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraItemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM adm_pedido_compra_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
