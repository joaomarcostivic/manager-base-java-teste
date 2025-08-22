package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ProdutoServicoEmpresa;
import com.tivic.manager.grl.ProdutoServicoEmpresaDAO;
import com.tivic.manager.grl.ProdutoServicoEmpresaServices;


public class ProdutoFornecedorServices {

	public static int insert(ProdutoFornecedor objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProdutoFornecedor item, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			/* verificacao de vinculo de produto/servico com empresa */
			if (ProdutoServicoEmpresaDAO.get(item.getCdEmpresa(), item.getCdProdutoServico(), connect) == null) {
				if (ProdutoServicoEmpresaDAO.insert(new ProdutoServicoEmpresa(item.getCdEmpresa(), 
						item.getCdProdutoServico(), 
						0 /*cdUnidadeMedida*/, 
						"" /*idReduzido*/, 
						0 /*vlPrecoMedio*/, 
						0 /*vlCustoMedio*/, 
						0 /*vlUltimoCusto*/,
						0 /*qtIdeal*/, 
						0 /*qtMinima*/, 
						0 /*qtMaxima*/, 
						0 /*qtDiasEstoque*/, 
						0 /*qtPrecisaoCusto*/, 
						0 /*qtPrecisaoUnidade*/, 
						0 /*qtDiasGarantia*/, 
						0 /*tpReabastecimento*/,
						ProdutoServicoEmpresaServices.CTL_INDIVIDUAL /*tpControleEstoque*/,
						ProdutoServicoEmpresaServices.TRANSP_COMUM /*tpTransporte*/,
						ProdutoServicoEmpresaServices.ST_ATIVO /*stProdutoEmpresa*/, 
						null /*dtDesativacao*/, 
						"" /*nrOrdem*/,
						0 /*lgEstoqueNegativo*/), connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return -1;
				}
			}
			
			// inclui item
			int cdRetorno = ProdutoFornecedorDAO.insert(item, connect);
			if (cdRetorno <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoFornecedorServices.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoFornecedorServices.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProdutoFornecedor objeto) {
		return update(objeto, null);
	}

	public static int update(ProdutoFornecedor objeto, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			ProdutoFornecedor fornecedor = ProdutoFornecedorDAO.get(objeto.getCdFornecedor(), objeto.getCdEmpresa(), objeto.getCdProdutoServico(), connection);
			if (fornecedor == null) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}
			
			objeto.setQtDiasUltimaEntrega(fornecedor.getQtDiasUltimaEntrega());
			objeto.setVlUltimoPreco(fornecedor.getVlUltimoPreco());
			
			if (ProdutoFornecedorDAO.update(objeto, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}
			
			if (isConnectionNull)
				connection.commit();
			
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoFornecedorServices.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result save(ProdutoFornecedor produtoFornecedor){
		return save(produtoFornecedor, null);
	}

	public static Result save(ProdutoFornecedor produtoFornecedor, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(produtoFornecedor==null)
				return new Result(-1, "Erro ao salvar. ProdutoFornecedor é nulo");

			int retorno;
			if(ProdutoFornecedorDAO.get(produtoFornecedor.getCdFornecedor(), produtoFornecedor.getCdEmpresa(), produtoFornecedor.getCdProdutoServico())==null){
				retorno = ProdutoFornecedorDAO.insert(produtoFornecedor, connect);
				produtoFornecedor.setCdFornecedor(retorno);
			}
			else {
				retorno = ProdutoFornecedorDAO.update(produtoFornecedor, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PRODUTOFORNECEDOR", produtoFornecedor);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(int cdFornecedor, int cdEmpresa, int cdProdutoServico){
		return remove(cdFornecedor, cdEmpresa, cdProdutoServico, false, null);
	}
	public static Result remove(int cdFornecedor, int cdEmpresa, int cdProdutoServico, boolean cascade){
		return remove(cdFornecedor, cdEmpresa, cdProdutoServico, cascade, null);
	}
	public static Result remove(int cdFornecedor, int cdEmpresa, int cdProdutoServico, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = ProdutoFornecedorDAO.delete(cdFornecedor, cdEmpresa, cdProdutoServico, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByFornecedor(int cdFornecedor) {
		return getAllByFornecedor(cdFornecedor, null);
	}

	public static ResultSetMap getAllByFornecedor(int cdFornecedor, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_produto_servico FROM adm_produto_fornecedor A, grl_produto_servico B WHERE A.cd_produto_servico = B.cd_produto_servico AND A.cd_fornecedor = " + cdFornecedor);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoFornecedorServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoFornecedorServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByProduto(int cdProdutoServico) {
		return getAllByProduto(cdProdutoServico, null);
	}

	public static ResultSetMap getAllByProduto(int cdProdutoServico, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_pessoa AS nm_fornecedor FROM adm_produto_fornecedor A, grl_pessoa B WHERE A.cd_fornecedor = B.cd_pessoa AND A.cd_produto_servico = " + cdProdutoServico);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoFornecedorServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoFornecedorServices.getAll: " + e);
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
			System.err.println("Erro! ProdutoFornecedorServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoFornecedorServices.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_produto_fornecedor", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
}
