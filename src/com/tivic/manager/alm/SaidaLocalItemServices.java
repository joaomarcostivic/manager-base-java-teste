package com.tivic.manager.alm;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;


public class SaidaLocalItemServices {

	public static final int ST_ABERTO = 0;
	public static final int ST_ENVIADO = 1;
	public static final int ST_RECEBIDO_CLIENTE = 2;
	public static final int ST_CANCELADO = 3;
	
	public static final int ERR_DOC_BALANCO = -2;
	
	public static final String[] situacao = {"Aberto","Enviado","Recebido pelo cliente","Cancelado"};
	
	public static int insert(SaidaLocalItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(SaidaLocalItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM alm_balanco_doc_saida " +
					"WHERE cd_documento_saida = ?");
			pstmt.setInt(1, objeto.getCdDocumentoSaida());
			if (pstmt.executeQuery().next()) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return ERR_DOC_BALANCO;				
			}
			
			int cdEmpresa = objeto.getCdEmpresa();
			int cdLocalArmazenamento = objeto.getCdLocalArmazenamento();
			int cdProdutoServico = objeto.getCdProdutoServico();
			
			int code = 0;
			if ((code = SaidaLocalItemDAO.insert(objeto, connect)) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				com.tivic.manager.util.Util.registerLog(new Exception("Erro ao inserir saida no local de armazenamento!"));
				return -1;
			}
			
			boolean lgContabilizacaoManual = ParametroServices.getValorOfParametroAsInteger("LG_CONTABILIZACAO_ESTOQUE", 0, cdEmpresa, connect)==0;
			if (lgContabilizacaoManual) {
				ProdutoEstoque estoque = ProdutoEstoqueDAO.get(cdLocalArmazenamento, cdProdutoServico, cdEmpresa, connect);
				if (estoque==null) {
					estoque = new ProdutoEstoque(cdLocalArmazenamento,cdProdutoServico,cdEmpresa,0 /*qtEstoque*/,0 /*qtIdeal*/,0 /*qtMinima*/, 
												 0 /*qtMaxima*/,0 /*qtDiasEstoque*/,0 /*qtMinimaEcommerce*/, 0 /*qtEstoqueConsignado*/, 0 /*lgDefault*/);
					int ret = ProdutoEstoqueDAO.insert(estoque, connect); 
					if (ret <=0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						com.tivic.manager.util.Util.registerLog(new Exception("Erro ao salvar ProdutoEstoque!"));
						return -2;						
					}
				}
				estoque.setQtEstoque(estoque.getQtEstoque() - objeto.getQtSaida());
				int ret = ProdutoEstoqueServices.update(estoque, true, connect).getCode();
				if (ret <=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					com.tivic.manager.util.Util.registerLog(new Exception("Erro ao atualizar ProdutoEstoque!"));
					return -3;
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return code;
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int update(SaidaLocalItem objeto) {
		return update(objeto, null);
	}

	public static int update(SaidaLocalItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			int cdEmpresa            = objeto.getCdEmpresa();
			int cdLocalArmazenamento = objeto.getCdLocalArmazenamento();
			int cdProdutoServico     = objeto.getCdProdutoServico();
			int cdDocumentoSaida     = objeto.getCdDocumentoSaida();
			int cdSaida              = objeto.getCdSaida();
			int cdItem               = objeto.getCdItem();
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM alm_balanco_doc_saida " +
					                                           "WHERE cd_documento_saida = "+cdDocumentoSaida);
			if (pstmt.executeQuery().next()) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return ERR_DOC_BALANCO;				
			}
			
			SaidaLocalItem saidaEstoque = SaidaLocalItemDAO.get(cdSaida, cdProdutoServico, cdDocumentoSaida, cdEmpresa, 
																cdLocalArmazenamento, cdItem, connect);
			if (saidaEstoque==null) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			float qtSaidaAnterior = saidaEstoque.getQtSaida();
			
			if (SaidaLocalItemDAO.update(objeto, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			
			boolean lgContabilizacaoManuel = ParametroServices.getValorOfParametroAsInteger("LG_CONTABILIZACAO_ESTOQUE", 0, cdEmpresa, connect)==0;
			if (lgContabilizacaoManuel) {
				ProdutoEstoque estoque = ProdutoEstoqueDAO.get(cdLocalArmazenamento, cdProdutoServico, cdEmpresa, connect);
				if (estoque==null) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return -1;
				}
				estoque.setQtEstoque(estoque.getQtEstoque() + objeto.getQtSaida() - qtSaidaAnterior);
				if (ProdutoEstoqueServices.update(estoque, true, connect).getCode()<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return -1;
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SaidaLocalItemServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int delete(int cdSaidaLocalItem, int cdProdutoServico, int cdDocumentoSaida, int cdEmpresa, int cdLocalArmazenamento, 
			int cdItem) {
		return delete(cdSaidaLocalItem, cdProdutoServico, cdDocumentoSaida, cdEmpresa, cdLocalArmazenamento, cdItem, null);
	}

	public static int delete(int cdSaidaLocalItem, int cdProdutoServico, int cdDocumentoSaida, int cdEmpresa, int cdLocalArmazenamento, 
			int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM alm_balanco_doc_saida " +
					"WHERE cd_documento_saida = ?");
			pstmt.setInt(1, cdDocumentoSaida);
			if (pstmt.executeQuery().next()) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return ERR_DOC_BALANCO;				
			}
			
			SaidaLocalItem saidaEstoque = SaidaLocalItemDAO.get(cdSaidaLocalItem, cdProdutoServico, cdDocumentoSaida, cdEmpresa, 
					cdLocalArmazenamento, cdItem, connect);
			if (saidaEstoque==null) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			float qtSaidaEstoque = saidaEstoque.getQtSaida();
			
			if (SaidaLocalItemDAO.delete(cdSaidaLocalItem, cdProdutoServico, cdDocumentoSaida, cdEmpresa, cdLocalArmazenamento, cdItem, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			
			boolean lgContabilizacaoManuel = ParametroServices.getValorOfParametroAsInteger("LG_CONTABILIZACAO_ESTOQUE", 0, cdEmpresa, connect)==0;
			if (lgContabilizacaoManuel) {
				ProdutoEstoque estoque = ProdutoEstoqueDAO.get(cdLocalArmazenamento, cdProdutoServico, cdEmpresa, connect);
				if (estoque==null) {
					estoque = new ProdutoEstoque(cdLocalArmazenamento, 
						cdProdutoServico, 
						cdEmpresa, 
						0 /*qtEstoque*/, 
						0 /*qtIdeal*/, 
						0 /*qtMinima*/, 
						0 /*qtMaxima*/, 
						0 /*qtDiasEstoque*/, 
						0 /*qtMinimaEcoomerce*/, 
						0 /*qtEstoqueConsignado*/, 
						0 /*lgDefault*/);
					if (ProdutoEstoqueDAO.insert(estoque, connect)<=0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return -1;
					}
				}
				estoque.setQtEstoque(estoque.getQtEstoque() + qtSaidaEstoque);
				if (ProdutoEstoqueServices.update(estoque, true, connect).getCode()<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return -1;
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SaidaLocalItemServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}
