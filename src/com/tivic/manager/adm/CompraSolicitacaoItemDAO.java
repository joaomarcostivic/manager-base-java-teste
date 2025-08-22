package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class CompraSolicitacaoItemDAO{

	public static int insert(CompraSolicitacaoItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(CompraSolicitacaoItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_compra_solicitacao_item (cd_empresa,"+
			                                  "cd_pedido_compra,"+
			                                  "cd_produto_servico,"+
			                                  "cd_solicitacao_material,"+
			                                  "qt_atendida) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpresa());
			if(objeto.getCdPedidoCompra()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPedidoCompra());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdProdutoServico());
			if(objeto.getCdSolicitacaoMaterial()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdSolicitacaoMaterial());
			pstmt.setFloat(5,objeto.getQtAtendida());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CompraSolicitacaoItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CompraSolicitacaoItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CompraSolicitacaoItem objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(CompraSolicitacaoItem objeto, int cdEmpresaOld, int cdPedidoCompraOld, int cdProdutoServicoOld, int cdSolicitacaoMaterialOld) {
		return update(objeto, cdEmpresaOld, cdPedidoCompraOld, cdProdutoServicoOld, cdSolicitacaoMaterialOld, null);
	}

	public static int update(CompraSolicitacaoItem objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(CompraSolicitacaoItem objeto, int cdEmpresaOld, int cdPedidoCompraOld, int cdProdutoServicoOld, int cdSolicitacaoMaterialOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_compra_solicitacao_item SET cd_empresa=?,"+
												      		   "cd_pedido_compra=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_solicitacao_material=?,"+
												      		   "qt_atendida=? WHERE cd_empresa=? AND cd_pedido_compra=? AND cd_produto_servico=? AND cd_solicitacao_material=?");
			pstmt.setInt(1,objeto.getCdEmpresa());
			pstmt.setInt(2,objeto.getCdPedidoCompra());
			pstmt.setInt(3,objeto.getCdProdutoServico());
			pstmt.setInt(4,objeto.getCdSolicitacaoMaterial());
			pstmt.setFloat(5,objeto.getQtAtendida());
			pstmt.setInt(6, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(7, cdPedidoCompraOld!=0 ? cdPedidoCompraOld : objeto.getCdPedidoCompra());
			pstmt.setInt(8, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.setInt(9, cdSolicitacaoMaterialOld!=0 ? cdSolicitacaoMaterialOld : objeto.getCdSolicitacaoMaterial());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CompraSolicitacaoItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CompraSolicitacaoItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpresa, int cdPedidoCompra, int cdProdutoServico, int cdSolicitacaoMaterial) {
		return delete(cdEmpresa, cdPedidoCompra, cdProdutoServico, cdSolicitacaoMaterial, null);
	}

	public static int delete(int cdEmpresa, int cdPedidoCompra, int cdProdutoServico, int cdSolicitacaoMaterial, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_compra_solicitacao_item WHERE cd_empresa=? AND cd_pedido_compra=? AND cd_produto_servico=? AND cd_solicitacao_material=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdPedidoCompra);
			pstmt.setInt(3, cdProdutoServico);
			pstmt.setInt(4, cdSolicitacaoMaterial);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CompraSolicitacaoItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CompraSolicitacaoItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CompraSolicitacaoItem get(int cdEmpresa, int cdPedidoCompra, int cdProdutoServico, int cdSolicitacaoMaterial) {
		return get(cdEmpresa, cdPedidoCompra, cdProdutoServico, cdSolicitacaoMaterial, null);
	}

	public static CompraSolicitacaoItem get(int cdEmpresa, int cdPedidoCompra, int cdProdutoServico, int cdSolicitacaoMaterial, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_compra_solicitacao_item WHERE cd_empresa=? AND cd_pedido_compra=? AND cd_produto_servico=? AND cd_solicitacao_material=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdPedidoCompra);
			pstmt.setInt(3, cdProdutoServico);
			pstmt.setInt(4, cdSolicitacaoMaterial);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CompraSolicitacaoItem(rs.getInt("cd_empresa"),
						rs.getInt("cd_pedido_compra"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_solicitacao_material"),
						rs.getFloat("qt_atendida"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CompraSolicitacaoItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CompraSolicitacaoItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_compra_solicitacao_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CompraSolicitacaoItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CompraSolicitacaoItemDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_compra_solicitacao_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
