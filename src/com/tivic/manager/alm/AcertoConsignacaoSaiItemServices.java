package com.tivic.manager.alm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ProdutoServicoEmpresa;
import com.tivic.manager.grl.ProdutoServicoEmpresaDAO;
import com.tivic.manager.grl.ProdutoServicoEmpresaServices;


public class AcertoConsignacaoSaiItemServices {

	/* codificacao de erros relacionados a itens de acerto de consignacao */
	public static final int ERR_ITEM_DUPLICADO = -2;

	public static int insert(AcertoConsignacaoSaiItem item) {
		return insert(item, null);
	}

	public static int insert(AcertoConsignacaoSaiItem item, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			int cdProdutoServico = item.getCdProdutoServico();
			int cdEmpresa = item.getCdEmpresa();
			
			/* verifica se o item a ser incluido já existe na relação de itens do acerto */
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM alm_acerto_consignacao_sai_item " +
					"WHERE cd_acerto_consignacao = ? " +
					"  AND cd_empresa = ? " +
					"  AND cd_produto_servico = ?");
			pstmt.setInt(1, item.getCdAcertoConsignacao());
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdProdutoServico);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return ERR_ITEM_DUPLICADO;
			}
			
			if (ProdutoServicoEmpresaDAO.get(cdEmpresa, cdProdutoServico, connect)==null) {
				ProdutoServicoEmpresa produtoEmpresa = new ProdutoServicoEmpresa(cdEmpresa, cdProdutoServico,
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
						0 /*tpControleEstoque*/, 
						0 /*tpTransporte*/, 
						ProdutoServicoEmpresaServices.ST_ATIVO /*stProdutoEmpresa*/, 
						null /*dtDesativacao*/,
						"" /*nrOrdem*/, 
						0 /*lgEstoqueNegativo*/);
				if (ProdutoServicoEmpresaDAO.insert(produtoEmpresa, connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return -1;
				}
			}
			
			if (AcertoConsignacaoSaiItemDAO.insert(item, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoSaiItemServices.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int update(AcertoConsignacaoSaiItem item) {
		return update(item, item.getCdAcertoConsignacao(), item.getCdEmpresa(), item.getCdProdutoServico(), null);
	}

	public static int update(AcertoConsignacaoSaiItem item, int cdAcertoConsignacaoOld, int cdEmpresaOld, int cdProdutoServicoOld) {
		return update(item, cdAcertoConsignacaoOld, cdEmpresaOld, cdProdutoServicoOld, null);
	}

	public static int update(AcertoConsignacaoSaiItem item, Connection connect) {
		return update(item, item.getCdAcertoConsignacao(), item.getCdEmpresa(), item.getCdProdutoServico(), connect);
	}

	public static int update(AcertoConsignacaoSaiItem item, int cdAcertoConsignacaoOld, int cdEmpresaOld, int cdProdutoServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdProdutoServico = item.getCdProdutoServico();
			int cdEmpresa = item.getCdEmpresa();
			
			/* verifica se o item a ser incluido já existe na relação de itens do acerto */
			if (cdProdutoServico != cdProdutoServicoOld) {
				PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
						"FROM alm_acerto_consignacao_sai_item " +
						"WHERE cd_acerto_consignacao = ? " +
						"  AND cd_empresa = ? " +
						"  AND cd_produto_servico = ?");
				pstmt.setInt(1, item.getCdAcertoConsignacao());
				pstmt.setInt(2, cdEmpresa);
				pstmt.setInt(3, cdProdutoServico);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					return ERR_ITEM_DUPLICADO;
				}				
			}
			
			if (ProdutoServicoEmpresaDAO.get(cdEmpresa, cdProdutoServico, connect)==null) {
				ProdutoServicoEmpresa ProdutoServicoEmpresa = new ProdutoServicoEmpresa(cdEmpresa, cdProdutoServico,
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
						0 /*tpControleEstoque*/, 
						0 /*tpTransporte*/, 
						ProdutoServicoEmpresaServices.ST_ATIVO /*stProdutoEmpresa*/, 
						null /*dtDesativacao*/,
						"" /*nrOrdem*/,
						0 /*lgEstoqueNegativo*/);
				if (ProdutoServicoEmpresaDAO.insert(ProdutoServicoEmpresa, connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return -1;
				}
			}
			
			if (AcertoConsignacaoSaiItemDAO.update(item, cdAcertoConsignacaoOld, cdEmpresaOld, cdProdutoServicoOld, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoSaiItemServices.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}
