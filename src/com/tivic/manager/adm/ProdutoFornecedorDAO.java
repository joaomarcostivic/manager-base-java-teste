package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ProdutoFornecedorDAO{

	public static int insert(ProdutoFornecedor objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProdutoFornecedor objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_produto_fornecedor (cd_fornecedor,"+
			                                  "cd_empresa,"+
			                                  "cd_produto_servico,"+
			                                  "cd_representante,"+
			                                  "vl_ultimo_preco,"+
			                                  "qt_dias_entrega,"+
			                                  "qt_dias_ultima_entrega,"+
			                                  "id_produto,"+
			                                  "qt_pedido_minimo,"+
			                                  "cd_moeda,"+
			                                  "cd_unidade_medida) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdFornecedor()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdFornecedor());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdProdutoServico());
			if(objeto.getCdRepresentante()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdRepresentante());
			pstmt.setFloat(5,objeto.getVlUltimoPreco());
			pstmt.setInt(6,objeto.getQtDiasEntrega());
			pstmt.setInt(7,objeto.getQtDiasUltimaEntrega());
			pstmt.setString(8,objeto.getIdProduto());
			pstmt.setFloat(9,objeto.getQtPedidoMinimo());
			if(objeto.getCdMoeda()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdMoeda());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdUnidadeMedida());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoFornecedorDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoFornecedorDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProdutoFornecedor objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(ProdutoFornecedor objeto, int cdFornecedorOld, int cdEmpresaOld, int cdProdutoServicoOld) {
		return update(objeto, cdFornecedorOld, cdEmpresaOld, cdProdutoServicoOld, null);
	}

	public static int update(ProdutoFornecedor objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(ProdutoFornecedor objeto, int cdFornecedorOld, int cdEmpresaOld, int cdProdutoServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_produto_fornecedor SET cd_fornecedor=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_representante=?,"+
												      		   "vl_ultimo_preco=?,"+
												      		   "qt_dias_entrega=?,"+
												      		   "qt_dias_ultima_entrega=?,"+
												      		   "id_produto=?,"+
												      		   "qt_pedido_minimo=?,"+
												      		   "cd_moeda=?,"+
												      		   "cd_unidade_medida=? WHERE cd_fornecedor=? AND cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1,objeto.getCdFornecedor());
			pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setInt(3,objeto.getCdProdutoServico());
			if(objeto.getCdRepresentante()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdRepresentante());
			pstmt.setFloat(5,objeto.getVlUltimoPreco());
			pstmt.setInt(6,objeto.getQtDiasEntrega());
			pstmt.setInt(7,objeto.getQtDiasUltimaEntrega());
			pstmt.setString(8,objeto.getIdProduto());
			pstmt.setFloat(9,objeto.getQtPedidoMinimo());
			if(objeto.getCdMoeda()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdMoeda());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdUnidadeMedida());
			pstmt.setInt(12, cdFornecedorOld!=0 ? cdFornecedorOld : objeto.getCdFornecedor());
			pstmt.setInt(13, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(14, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoFornecedorDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoFornecedorDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFornecedor, int cdEmpresa, int cdProdutoServico) {
		return delete(cdFornecedor, cdEmpresa, cdProdutoServico, null);
	}

	public static int delete(int cdFornecedor, int cdEmpresa, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_produto_fornecedor WHERE cd_fornecedor=? AND cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdFornecedor);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdProdutoServico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoFornecedorDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoFornecedorDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProdutoFornecedor get(int cdFornecedor, int cdEmpresa, int cdProdutoServico) {
		return get(cdFornecedor, cdEmpresa, cdProdutoServico, null);
	}

	public static ProdutoFornecedor get(int cdFornecedor, int cdEmpresa, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_produto_fornecedor WHERE cd_fornecedor=? AND cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdFornecedor);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdProdutoServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProdutoFornecedor(rs.getInt("cd_fornecedor"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_representante"),
						rs.getFloat("vl_ultimo_preco"),
						rs.getInt("qt_dias_entrega"),
						rs.getInt("qt_dias_ultima_entrega"),
						rs.getString("id_produto"),
						rs.getFloat("qt_pedido_minimo"),
						rs.getInt("cd_moeda"),
						rs.getInt("cd_unidade_medida"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoFornecedorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoFornecedorDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_produto_fornecedor");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoFornecedorDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoFornecedorDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_produto_fornecedor", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
