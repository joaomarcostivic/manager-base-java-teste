package com.tivic.manager.alm;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ProdutoServicoEmpresa;
import com.tivic.manager.grl.ProdutoServicoEmpresaDAO;
import com.tivic.manager.grl.ProdutoServicoEmpresaServices;

import sol.dao.ResultSetMap;

public class ProdutoGrupoServices {

	public static int insert(ProdutoGrupo objeto, boolean overPrincipal) {
		return insert(objeto, overPrincipal, null);
	}

	public static int insert(ProdutoGrupo objeto) {
		return insert(objeto, false, null);
	}

	public static int insert(ProdutoGrupo objeto, Connection connection) {
		return insert(objeto, false, connection);
	}

	public static int insert(ProdutoGrupo objeto, boolean overPrincipal, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			if (ProdutoServicoEmpresaDAO.get(objeto.getCdEmpresa(), objeto.getCdProdutoServico(), connection) == null) {
				if (ProdutoServicoEmpresaDAO.insert(new ProdutoServicoEmpresa(objeto.getCdEmpresa(),
						objeto.getCdProdutoServico(),
						0, /* unidade de medida */
						"", /* ID reduzido */
						0, /* vlPrecoMedio */
						0, /* vlCustoMedio */
						0, /* vlUltimoCusto */
						0, /* qtIdeal */
						0, /* qtMinima */
						0, /* qtMaxima */
						0, /* qtDiasEstoque */
						0, /* qtPrecisaoCusto */
						0, /* qtPrecisaoUnidade */
						0, /* qtDiasGarantia */
						ProdutoServicoEmpresaServices.TP_MANUAL, /* tpReabastecimento */
						ProdutoServicoEmpresaServices.CTL_QUANTIDADE, /* tpControleEstoque */
						ProdutoServicoEmpresaServices.TRANSP_COMUM, /* tpTransporte */
						ProdutoServicoEmpresaServices.ST_ATIVO, /* stProdutoServicoEmpresa */
						null /* dtDesativacao */,
						"" /*nrOrdem*/,
						0 /*lgEstoqueNegativo*/), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			/* Verifica se já existe um grupo definido como 'principal'
			   para este produto, se não existir, define o que está sendo
			   incluído como 'principal'
			*/
			if (overPrincipal && objeto.getLgPrincipal()==1) {
				PreparedStatement pstmt = connection.prepareStatement("UPDATE alm_produto_grupo " +
						"SET lg_principal = 0 " +
						"WHERE cd_produto_servico = ? " +
						"  AND cd_empresa = ? " +
						"  AND lg_principal = 1");
				pstmt.setInt(1, objeto.getCdProdutoServico());
				pstmt.setInt(2, objeto.getCdEmpresa());
				pstmt.execute();
			}
			else {
				PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM alm_produto_grupo " +
						"WHERE cd_produto_servico = ? " +
						"  AND cd_empresa = ?" +
						"  AND lg_principal = 1");
				pstmt.setInt(1, objeto.getCdProdutoServico());
				pstmt.setInt(2, objeto.getCdEmpresa());
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				objeto.setLgPrincipal((rsm == null || !rsm.next()) ? 1 : 0);
			}

			if (ProdutoGrupoDAO.insert(objeto, connection) <= 0) {
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
			System.err.println("Erro! ProdutoGrupoServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	public static int deleteAllProdutos(int cdEmpresa, int cdProdutoServico) {
		return deleteAllProdutos(cdEmpresa, cdProdutoServico, null);
	}

	public static int deleteAllProdutos(int cdEmpresa, int cdProdutoServico, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			PreparedStatement pstmt = connection.prepareStatement("DELETE FROM alm_produto_grupo " +
					"WHERE cd_produto_servico = ? " +
					"  AND cd_empresa = ?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdEmpresa);
			pstmt.execute();

			if (isConnectionNull)
				connection.commit();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
}
