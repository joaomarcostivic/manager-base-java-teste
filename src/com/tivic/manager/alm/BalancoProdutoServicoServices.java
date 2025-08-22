package com.tivic.manager.alm;

import java.sql.Connection;
import java.sql.ResultSet;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ProdutoServicoEmpresa;
import com.tivic.manager.grl.ProdutoServicoEmpresaDAO;
import com.tivic.manager.grl.ProdutoServicoEmpresaServices;

import sol.util.Result;

public class BalancoProdutoServicoServices {

	public static Result insert(BalancoProdutoServico objeto) {
		return insert(objeto, null);
	}

	public static Result insert(BalancoProdutoServico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			/*
			 * Verifica se existe o cadastro do produto na empresa, se não houver salva
			 */
			ResultSet rs = connect.prepareStatement("SELECT cd_produto_servico FROM grl_produto_servico_empresa " +
					                                "WHERE cd_empresa = " +objeto.getCdEmpresa()+
					                                "  AND cd_produto_servico = "+objeto.getCdProdutoServico()).executeQuery();
			if (!rs.next()) {
				if (ProdutoServicoEmpresaDAO.insert(new ProdutoServicoEmpresa(objeto.getCdEmpresa(), objeto.getCdProdutoServico(), 
																			  objeto.getCdUnidadeMedida(), "" /*idReduzido*/, 
																			  0 /*vlPrecoMedio*/, 0 /*vlCustoMedio*/, 0 /*vlUltimoPreco*/, 
																			  0 /*qtIdeal*/, 0 /*qtMinima*/, 0 /*qtMaxima*/, 
																			  0 /*qtDiasEstoque*/, 0 /*qtPrecisaoCusto*/, 0 /*qtPrecisaoUnidade*/, 
																			  0 /*qtDiasGarantia*/, 0 /*tpReabastecimento*/, 
																			  0 /*qtControleEstoque*/, 
																			  ProdutoServicoEmpresaServices.TRANSP_COMUM /*tpTransporte*/, 
																			  ProdutoServicoEmpresaServices.ST_ATIVO /*stProdutoEmpresa*/, 
																			  null /*dtDesativao*/, "" /*nrOrdem*/, 0 /*lgEstoqueNegativo*/), connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Falha ao tentar salvar dados do produto na empresa!");
				}
			}
			
			if (BalancoProdutoServicoDAO.insert(objeto, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Falha ao tentar incluir item no balanco!");				
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Falha ao tentar incluir item no balanco!", e);
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}	

	public static Result update(BalancoProdutoServico objeto) {
		return new Result(BalancoProdutoServicoDAO.update(objeto));
	}
}
