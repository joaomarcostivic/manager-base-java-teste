package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CompraEntradaItemDAO{

	public static int insert(CompraEntradaItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(CompraEntradaItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_compra_entrada_item (cd_empresa,"+
			                                  "cd_ordem_compra,"+
			                                  "cd_documento_entrada,"+
			                                  "cd_produto_servico,"+
			                                  "qt_recebida,"+
			                                  "cd_item) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpresa());
			if(objeto.getCdOrdemCompra()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdOrdemCompra());
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdDocumentoEntrada());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProdutoServico());
			pstmt.setFloat(5,objeto.getQtRecebida());
			if(objeto.getCdItem()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CompraEntradaItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CompraEntradaItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CompraEntradaItem objeto) {
		return update(objeto, 0, 0, 0, 0, 0, null);
	}

	public static int update(CompraEntradaItem objeto, int cdEmpresaOld, int cdOrdemCompraOld, int cdDocumentoEntradaOld, int cdProdutoServicoOld, int cdItemOld) {
		return update(objeto, cdEmpresaOld, cdOrdemCompraOld, cdDocumentoEntradaOld, cdProdutoServicoOld, cdItemOld, null);
	}

	public static int update(CompraEntradaItem objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, 0, connect);
	}

	public static int update(CompraEntradaItem objeto, int cdEmpresaOld, int cdOrdemCompraOld, int cdDocumentoEntradaOld, int cdProdutoServicoOld, int cdItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_compra_entrada_item SET cd_empresa=?,"+
												      		   "cd_ordem_compra=?,"+
												      		   "cd_documento_entrada=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "qt_recebida=?,"+
												      		   "cd_item=? WHERE cd_empresa=? AND cd_ordem_compra=? AND cd_documento_entrada=? AND cd_produto_servico=? AND cd_item=?");
			pstmt.setInt(1,objeto.getCdEmpresa());
			pstmt.setInt(2,objeto.getCdOrdemCompra());
			pstmt.setInt(3,objeto.getCdDocumentoEntrada());
			pstmt.setInt(4,objeto.getCdProdutoServico());
			pstmt.setFloat(5,objeto.getQtRecebida());
			pstmt.setInt(6,objeto.getCdItem());
			pstmt.setInt(7, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(8, cdOrdemCompraOld!=0 ? cdOrdemCompraOld : objeto.getCdOrdemCompra());
			pstmt.setInt(9, cdDocumentoEntradaOld!=0 ? cdDocumentoEntradaOld : objeto.getCdDocumentoEntrada());
			pstmt.setInt(10, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.setFloat(11, cdItemOld!=0 ? cdItemOld : objeto.getCdItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CompraEntradaItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CompraEntradaItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpresa, int cdOrdemCompra, int cdDocumentoEntrada, int cdProdutoServico, int cdItem) {
		return delete(cdEmpresa, cdOrdemCompra, cdDocumentoEntrada, cdProdutoServico, cdItem, null);
	}

	public static int delete(int cdEmpresa, int cdOrdemCompra, int cdDocumentoEntrada, int cdProdutoServico, int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_compra_entrada_item WHERE cd_empresa=? AND cd_ordem_compra=? AND cd_documento_entrada=? AND cd_produto_servico=? AND cd_item=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdOrdemCompra);
			pstmt.setInt(3, cdDocumentoEntrada);
			pstmt.setInt(4, cdProdutoServico);
			pstmt.setInt(5, cdItem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CompraEntradaItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CompraEntradaItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CompraEntradaItem get(int cdEmpresa, int cdOrdemCompra, int cdDocumentoEntrada, int cdProdutoServico, int cdItem) {
		return get(cdEmpresa, cdOrdemCompra, cdDocumentoEntrada, cdProdutoServico, cdItem, null);
	}

	public static CompraEntradaItem get(int cdEmpresa, int cdOrdemCompra, int cdDocumentoEntrada, int cdProdutoServico, int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_compra_entrada_item WHERE cd_empresa=? AND cd_ordem_compra=? AND cd_documento_entrada=? AND cd_produto_servico=? AND cd_item=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdOrdemCompra);
			pstmt.setInt(3, cdDocumentoEntrada);
			pstmt.setInt(4, cdProdutoServico);
			pstmt.setInt(5, cdItem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CompraEntradaItem(rs.getInt("cd_empresa"),
						rs.getInt("cd_ordem_compra"),
						rs.getInt("cd_documento_entrada"),
						rs.getInt("cd_produto_servico"),
						rs.getFloat("qt_recebida"),
						rs.getInt("cd_item"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CompraEntradaItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CompraEntradaItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_compra_entrada_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CompraEntradaItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CompraEntradaItemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CompraEntradaItem> getList() {
		return getList(null);
	}

	public static ArrayList<CompraEntradaItem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CompraEntradaItem> list = new ArrayList<CompraEntradaItem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CompraEntradaItem obj = CompraEntradaItemDAO.get(rsm.getInt("cd_empresa"), rsm.getInt("cd_ordem_compra"), rsm.getInt("cd_documento_entrada"), rsm.getInt("cd_produto_servico"), rsm.getInt("cd_item"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CompraEntradaItemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM adm_compra_entrada_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
