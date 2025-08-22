package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ProdutoServicoParametroDAO{

	public static int insert(ProdutoServicoParametro objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProdutoServicoParametro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_produto_servico_parametro (cd_empresa,"+
			                                  "cd_produto_servico,"+
			                                  "lg_verificar_estoque_na_venda,"+
			                                  "lg_bloqueia_venda,"+
			                                  "lg_permite_desconto,"+
			                                  "lg_faz_entrega,"+
			                                  "lg_nao_controla_estoque,"+
			                                  "lg_imprime_na_tabela_preco,"+
			                                  "lg_produto_uso_consumo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpresa());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProdutoServico());
			pstmt.setInt(3,objeto.getLgVerificarEstoqueNaVenda());
			pstmt.setInt(4,objeto.getLgBloqueiaVenda());
			pstmt.setInt(5,objeto.getLgPermiteDesconto());
			pstmt.setInt(6,objeto.getLgFazEntrega());
			pstmt.setInt(7,objeto.getLgNaoControlaEstoque());
			pstmt.setInt(8,objeto.getLgImprimeNaTabelaPreco());
			pstmt.setInt(9,objeto.getLgProdutoUsoConsumo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoParametroDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoParametroDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProdutoServicoParametro objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ProdutoServicoParametro objeto, int cdEmpresaOld, int cdProdutoServicoOld) {
		return update(objeto, cdEmpresaOld, cdProdutoServicoOld, null);
	}

	public static int update(ProdutoServicoParametro objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ProdutoServicoParametro objeto, int cdEmpresaOld, int cdProdutoServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_produto_servico_parametro SET cd_empresa=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "lg_verificar_estoque_na_venda=?,"+
												      		   "lg_bloqueia_venda=?,"+
												      		   "lg_permite_desconto=?,"+
												      		   "lg_faz_entrega=?,"+
												      		   "lg_nao_controla_estoque=?,"+
												      		   "lg_imprime_na_tabela_preco=?,"+
												      		   "lg_produto_uso_consumo=? WHERE cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1,objeto.getCdEmpresa());
			pstmt.setInt(2,objeto.getCdProdutoServico());
			pstmt.setInt(3,objeto.getLgVerificarEstoqueNaVenda());
			pstmt.setInt(4,objeto.getLgBloqueiaVenda());
			pstmt.setInt(5,objeto.getLgPermiteDesconto());
			pstmt.setInt(6,objeto.getLgFazEntrega());
			pstmt.setInt(7,objeto.getLgNaoControlaEstoque());
			pstmt.setInt(8,objeto.getLgImprimeNaTabelaPreco());
			pstmt.setInt(9,objeto.getLgProdutoUsoConsumo());
			pstmt.setInt(10, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(11, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoParametroDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoParametroDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpresa, int cdProdutoServico) {
		return delete(cdEmpresa, cdProdutoServico, null);
	}

	public static int delete(int cdEmpresa, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_produto_servico_parametro WHERE cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoParametroDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoParametroDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProdutoServicoParametro get(int cdEmpresa, int cdProdutoServico) {
		return get(cdEmpresa, cdProdutoServico, null);
	}

	public static ProdutoServicoParametro get(int cdEmpresa, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_produto_servico_parametro WHERE cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdProdutoServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProdutoServicoParametro(rs.getInt("cd_empresa"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("lg_verificar_estoque_na_venda"),
						rs.getInt("lg_bloqueia_venda"),
						rs.getInt("lg_permite_desconto"),
						rs.getInt("lg_faz_entrega"),
						rs.getInt("lg_nao_controla_estoque"),
						rs.getInt("lg_imprime_na_tabela_preco"),
						rs.getInt("lg_produto_uso_consumo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoParametroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoParametroDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_produto_servico_parametro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoParametroDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoParametroDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_produto_servico_parametro", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
