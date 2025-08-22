package com.tivic.manager.alm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class BalancoProdutoServicoDAO{

	public static int insert(BalancoProdutoServico objeto) {
		return insert(objeto, null);
	}

	public static int insert(BalancoProdutoServico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_balanco_produto_servico (cd_balanco,"+
			                                  "cd_empresa,"+
			                                  "cd_produto_servico,"+
			                                  "qt_estoque,"+
			                                  "qt_estoque_balanco,"+
			                                  "cd_unidade_medida) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdBalanco()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdBalanco());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdProdutoServico());
			pstmt.setDouble(4,objeto.getQtEstoque());
			pstmt.setDouble(5,objeto.getQtEstoqueBalanco());
			pstmt.setInt(6,objeto.getCdUnidadeMedida());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BalancoProdutoServicoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BalancoProdutoServicoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(BalancoProdutoServico objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(BalancoProdutoServico objeto, int cdBalancoOld, int cdEmpresaOld, int cdProdutoServicoOld) {
		return update(objeto, cdBalancoOld, cdEmpresaOld, cdProdutoServicoOld, null);
	}

	public static int update(BalancoProdutoServico objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(BalancoProdutoServico objeto, int cdBalancoOld, int cdEmpresaOld, int cdProdutoServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_balanco_produto_servico SET cd_balanco=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "qt_estoque=?,"+
												      		   "qt_estoque_balanco=?,"+
												      		   "cd_unidade_medida=? WHERE cd_balanco=? AND cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1,objeto.getCdBalanco());
			pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setInt(3,objeto.getCdProdutoServico());
			pstmt.setDouble(4,objeto.getQtEstoque());
			pstmt.setDouble(5,objeto.getQtEstoqueBalanco());
			pstmt.setInt(6,objeto.getCdUnidadeMedida());
			pstmt.setInt(7, cdBalancoOld!=0 ? cdBalancoOld : objeto.getCdBalanco());
			pstmt.setInt(8, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(9, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BalancoProdutoServicoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BalancoProdutoServicoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdBalanco, int cdEmpresa, int cdProdutoServico) {
		return delete(cdBalanco, cdEmpresa, cdProdutoServico, null);
	}

	public static int delete(int cdBalanco, int cdEmpresa, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_balanco_produto_servico WHERE cd_balanco=? AND cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdBalanco);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdProdutoServico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BalancoProdutoServicoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BalancoProdutoServicoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static BalancoProdutoServico get(int cdBalanco, int cdEmpresa, int cdProdutoServico) {
		return get(cdBalanco, cdEmpresa, cdProdutoServico, null);
	}

	public static BalancoProdutoServico get(int cdBalanco, int cdEmpresa, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_balanco_produto_servico WHERE cd_balanco=? AND cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdBalanco);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdProdutoServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new BalancoProdutoServico(rs.getInt("cd_balanco"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_produto_servico"),
						rs.getDouble("qt_estoque"),
						rs.getDouble("qt_estoque_balanco"),
						rs.getInt("cd_unidade_medida"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BalancoProdutoServicoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BalancoProdutoServicoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_balanco_produto_servico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BalancoProdutoServicoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BalancoProdutoServicoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM alm_balanco_produto_servico", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
