package com.tivic.manager.alm;

import java.sql.Connection;
import java.sql.PreparedStatement;

import sol.dao.ResultSetMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;


public class EntradaLocalItemServices {

	public static final int ERR_DOC_BALANCO = -1;
	
	public static int insert(EntradaLocalItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(EntradaLocalItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			int cdEmpresa = objeto.getCdEmpresa();
			int cdLocalArmazenamento = objeto.getCdLocalArmazenamento();
			int cdProdutoServico = objeto.getCdProdutoServico();

			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM alm_balanco_doc_entrada " +
															  "WHERE cd_documento_entrada = "+objeto.getCdDocumentoEntrada());
			if (pstmt.executeQuery().next()) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return ERR_DOC_BALANCO;				
			}
			
			int code = 0;
			
			if ((code = EntradaLocalItemDAO.insert(objeto, connect)) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			
			boolean lgContabilizacaoManuel = ParametroServices.getValorOfParametroAsInteger("LG_CONTABILIZACAO_ESTOQUE", 0, cdEmpresa, connect)==0;
			if (lgContabilizacaoManuel) {
				ProdutoEstoque estoque = ProdutoEstoqueDAO.get(cdLocalArmazenamento, cdProdutoServico, cdEmpresa, connect);
				if (estoque==null) {
					estoque = new ProdutoEstoque(cdLocalArmazenamento, cdProdutoServico, cdEmpresa, 0 /*qtEstoque*/, 0 /*qtIdeal*/, 0 /*qtMinima*/, 
							                     0 /*qtMaxima*/, 0 /*qtDiasEstoque*/, 0 /*qtMinimaEcommerce*/, 0 /*qtEstoqueConsignado*/, 0 /*lgDefault*/);
					if (ProdutoEstoqueDAO.insert(estoque, connect)<=0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return -1;						
					}
				}
				estoque.setQtEstoque(estoque.getQtEstoque() + objeto.getQtEntrada());
				if (ProdutoEstoqueServices.update(estoque, true, connect).getCode()<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return -1;
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaLocalItemServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int insertAll(java.util.ArrayList<EntradaLocalItem> objetos, int cdDocumentoEntrada, int cdEmpresa, int cdProdutoServico){
		Connection connect = Conexao.conectar();
		try{
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_entrada_local_item " +
					"WHERE cd_documento_entrada = ? " +
					"  AND cd_empresa = ? " +
					"  AND cd_produto_servico = ?");
			pstmt.setInt(1, cdDocumentoEntrada);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdProdutoServico);
			pstmt.execute();
			
			int code = 0;
			for(int cont = 0; cont < objetos.size(); cont++)
				if ((code = insert(objetos.get(cont))) <= 0) 
					return -1;
			
			return code;
		}
		catch(Exception e){
			Conexao.rollback(connect);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	public static int update(EntradaLocalItem objeto) {
		return update(objeto, null);
	}

	public static int update(EntradaLocalItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			int cdEmpresa            = objeto.getCdEmpresa();
			int cdLocalArmazenamento = objeto.getCdLocalArmazenamento();
			int cdProdutoServico     = objeto.getCdProdutoServico();
			int cdDocumentoEntrada   = objeto.getCdDocumentoEntrada();
			int cdEntradaLocalItem   = objeto.getCdEntradaLocalItem();
			int cdItem               = objeto.getCdItem();
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM alm_balanco_doc_entrada " +
					                                           "WHERE cd_documento_entrada = "+objeto.getCdDocumentoEntrada());
			if (pstmt.executeQuery().next()) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return ERR_DOC_BALANCO;				
			}
			
			EntradaLocalItem entradaEstoque = EntradaLocalItemDAO.get(cdProdutoServico, cdDocumentoEntrada, cdEmpresa, cdLocalArmazenamento, cdEntradaLocalItem, cdItem, connect);
			if (entradaEstoque==null) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			float qtEntradaAnterior = entradaEstoque.getQtEntrada();
			
			if (EntradaLocalItemDAO.update(objeto, connect) <= 0) {
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
				estoque.setQtEstoque(estoque.getQtEstoque() + objeto.getQtEntrada() - qtEntradaAnterior);
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
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int delete(int cdProdutoServico, int cdDocumentoEntrada, int cdEmpresa, int cdLocalArmazenamento, int cdEntradaLocalItem, int cdItem) {
		return delete(cdProdutoServico, cdDocumentoEntrada, cdEmpresa, cdLocalArmazenamento, cdEntradaLocalItem, cdItem, null);
	}

	public static int delete(int cdProdutoServico, int cdDocumentoEntrada, int cdEmpresa, int cdLocalArmazenamento, int cdEntradaLocalItem, 
			int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM alm_balanco_doc_entrada " +
					                                           "WHERE cd_documento_entrada = "+cdDocumentoEntrada);
			if (pstmt.executeQuery().next()) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return ERR_DOC_BALANCO;				
			}
			
			EntradaLocalItem entradaEstoque = EntradaLocalItemDAO.get(cdProdutoServico, cdDocumentoEntrada, cdEmpresa, cdLocalArmazenamento, 
					cdEntradaLocalItem, cdItem, connect);
			if (entradaEstoque==null) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			float qtEntradaEstoque = entradaEstoque.getQtEntrada();
			
			if (EntradaLocalItemDAO.delete(cdProdutoServico, cdDocumentoEntrada, cdEmpresa, cdLocalArmazenamento, cdEntradaLocalItem, cdItem, connect) <= 0) {
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
				estoque.setQtEstoque(estoque.getQtEstoque() - qtEntradaEstoque);
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
			System.err.println("Erro! EntradaLocalItemServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo para corrigir entradas que foram erradas para tanques
	 * @return
	 */
	public static int corrigirTanques() {
		return corrigirTanques(null);
	}

	public static int corrigirTanques(Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM pcb_tanque");
			
			ResultSetMap rsmTanques = new ResultSetMap(pstmt.executeQuery());
			ResultSetMap rsmConjuntoTanques = new ResultSetMap(pstmt.executeQuery());
			
			while(rsmTanques.next()){
				
				String mesmoCombustivel = "(";
				while(rsmConjuntoTanques.next()){
					if(rsmTanques.getInt("cd_produto_servico") == rsmConjuntoTanques.getInt("cd_produto_servico"))
						mesmoCombustivel += rsmConjuntoTanques.getInt("cd_tanque") + ", "; 
				}
				if(mesmoCombustivel.equals("("))
					mesmoCombustivel = null;
				else
					mesmoCombustivel = mesmoCombustivel.substring(0, mesmoCombustivel.length()-2) + ")";
				pstmt = connect.prepareStatement("UPDATE alm_entrada_local_item set cd_local_armazenamento = " + rsmTanques.getInt("cd_tanque") + " WHERE cd_produto_servico = " + rsmTanques.getInt("cd_produto_servico") + (mesmoCombustivel != null ? " AND cd_local_armazenamento NOT IN " + mesmoCombustivel : ""));
				
				pstmt.executeUpdate();
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaLocalItemServices.insert: " +  e);
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
