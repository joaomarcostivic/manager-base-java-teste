package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class VendaSaidaItemDAO{

	public static int insert(VendaSaidaItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(VendaSaidaItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_venda_saida_item (cd_pedido_venda,"+
			                                  "cd_empresa,"+
			                                  "cd_produto_servico,"+
			                                  "cd_documento_saida,"+
			                                  "qt_saida,"+
			                                  "cd_item) VALUES (?, ?, ?, ?, ?, ?)");
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
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdDocumentoSaida());
			pstmt.setFloat(5,objeto.getQtSaida());
			if(objeto.getCdItem()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VendaSaidaItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VendaSaidaItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(VendaSaidaItem objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(VendaSaidaItem objeto, int cdPedidoVendaOld, int cdEmpresaOld, int cdProdutoServicoOld, int cdDocumentoSaidaOld) {
		return update(objeto, cdPedidoVendaOld, cdEmpresaOld, cdProdutoServicoOld, cdDocumentoSaidaOld, null);
	}

	public static int update(VendaSaidaItem objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(VendaSaidaItem objeto, int cdPedidoVendaOld, int cdEmpresaOld, int cdProdutoServicoOld, int cdDocumentoSaidaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_venda_saida_item SET cd_pedido_venda=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_documento_saida=?,"+
												      		   "qt_saida=?,"+
												      		   "cd_item=? WHERE cd_pedido_venda=? AND cd_empresa=? AND cd_produto_servico=? AND cd_documento_saida=?");
			pstmt.setInt(1,objeto.getCdPedidoVenda());
			pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setInt(3,objeto.getCdProdutoServico());
			pstmt.setInt(4,objeto.getCdDocumentoSaida());
			pstmt.setFloat(5,objeto.getQtSaida());
			if(objeto.getCdItem()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdItem());
			pstmt.setInt(7, cdPedidoVendaOld!=0 ? cdPedidoVendaOld : objeto.getCdPedidoVenda());
			pstmt.setInt(8, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(9, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.setInt(10, cdDocumentoSaidaOld!=0 ? cdDocumentoSaidaOld : objeto.getCdDocumentoSaida());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VendaSaidaItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VendaSaidaItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPedidoVenda, int cdEmpresa, int cdProdutoServico, int cdDocumentoSaida) {
		return delete(cdPedidoVenda, cdEmpresa, cdProdutoServico, cdDocumentoSaida, null);
	}

	public static int delete(int cdPedidoVenda, int cdEmpresa, int cdProdutoServico, int cdDocumentoSaida, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_venda_saida_item WHERE cd_pedido_venda=? AND cd_empresa=? AND cd_produto_servico=? AND cd_documento_saida=?");
			pstmt.setInt(1, cdPedidoVenda);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdProdutoServico);
			pstmt.setInt(4, cdDocumentoSaida);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VendaSaidaItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VendaSaidaItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static VendaSaidaItem get(int cdPedidoVenda, int cdEmpresa, int cdProdutoServico, int cdDocumentoSaida) {
		return get(cdPedidoVenda, cdEmpresa, cdProdutoServico, cdDocumentoSaida, null);
	}

	public static VendaSaidaItem get(int cdPedidoVenda, int cdEmpresa, int cdProdutoServico, int cdDocumentoSaida, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_venda_saida_item WHERE cd_pedido_venda=? AND cd_empresa=? AND cd_produto_servico=? AND cd_documento_saida=?");
			pstmt.setInt(1, cdPedidoVenda);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdProdutoServico);
			pstmt.setInt(4, cdDocumentoSaida);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new VendaSaidaItem(rs.getInt("cd_pedido_venda"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_documento_saida"),
						rs.getFloat("qt_saida"),
						rs.getInt("cd_item"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VendaSaidaItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VendaSaidaItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_venda_saida_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VendaSaidaItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VendaSaidaItemDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_venda_saida_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
