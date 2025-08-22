package com.tivic.manager.ord;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.alm.DocumentoEntrada;
import com.tivic.manager.alm.DocumentoEntradaItem;
import com.tivic.manager.alm.DocumentoEntradaItemServices;
import com.tivic.manager.alm.DocumentoEntradaServices;
import com.tivic.manager.alm.DocumentoSaida;
import com.tivic.manager.alm.DocumentoSaidaItem;
import com.tivic.manager.alm.DocumentoSaidaItemServices;
import com.tivic.manager.alm.DocumentoSaidaServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class PlanoTrabalhoServices {
	
	public static Result save(PlanoTrabalho planoTrabalho, ArrayList<Integer> ordemServico, ResultSetMap rsmProduto, 
			int cdEmpresa, int cdLocalArmazenamento, AuthData authData){
		return save(planoTrabalho, ordemServico, rsmProduto, cdEmpresa, cdLocalArmazenamento, authData, null);
	}

	public static Result save(PlanoTrabalho planoTrabalho, ArrayList<Integer> ordemServico, ResultSetMap rsmProduto, 
			int cdEmpresa, int cdLocalArmazenamento, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Result result = null;

			if(planoTrabalho==null)
				return new Result(-1, "Erro ao salvar. PlanoTrabalho é nulo");

			int retorno;
			if(planoTrabalho.getCdPlanoTrabalho()==0){
				retorno = PlanoTrabalhoDAO.insert(planoTrabalho, connect);
				planoTrabalho.setCdPlanoTrabalho(retorno);
			}
			else {
				retorno = PlanoTrabalhoDAO.update(planoTrabalho, connect);
			}
			
			if(retorno<=0) {
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao salvar plano de trabalho.");
			}
			
			/**
			 * incluir OS no plano
			 */
			for (Integer cdOrdemServico : ordemServico) {
				OrdemServico os = OrdemServicoDAO.get(cdOrdemServico, connect);
				os.setCdPlanoTrabalho(planoTrabalho.getCdPlanoTrabalho());
				
				result = OrdemServicoServices.save(os, null, null, null, connect);
				if(result.getCode()<=0) {
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
			}
			
			/**
			 * entrada consignada
			 */
			DocumentoEntrada entradaConsignada = new DocumentoEntrada(0/*cdDocumentoEntrada*/, 
					cdEmpresa, 
					0/*cdTransportadora*/, 
					0/*cdFornecedor*/, 
					new GregorianCalendar()/*dtEmissao*/, 
					planoTrabalho.getDtTrabalho()/*dtDocumentoEntrada*/, 
					DocumentoEntradaServices.ST_LIBERADO/*stDocumentoEntrada*/, 
					DocumentoEntradaServices.TP_DOC_NAO_FISCAL/*tpDocumentoEntrada*/, 
					DocumentoEntradaServices.ENT_CONSIGNACAO/*tpEntrada*/, 
					0/*cdNaturezaOperacao*/, 
					DocumentoEntradaServices.MOV_ESTOQUE_CONSIGNADO/*tpMovimentoEstoque*/, 
					0/*cdMoeda*/, 
					0/*cdDigitador*/, 
					0/*nrSerie*/, 
					0/*cdViagem*/);
			result = DocumentoEntradaServices.save(entradaConsignada, connect);
			if(result.getCode()<=0) {
				if(isConnectionNull)
					Conexao.rollback(connect);
				return result;
			}
			entradaConsignada = (DocumentoEntrada)result.getObjects().get("DOCUMENTOENTRADA");
			
			while(rsmProduto.next()) {
				DocumentoEntradaItem item = new DocumentoEntradaItem(entradaConsignada.getCdDocumentoEntrada()/*cdDocumentoEntrada*/, 
						rsmProduto.getInt("cd_produto_servico")/*cdProdutoServico*/, 
						cdEmpresa, 
						rsmProduto.getInt("qt_saida")/*qtEntrada*/, 
						0/*vlUnitario*/, 0/*vlAcrescimo*/, 0/*vlDesconto*/, 0/*cdUnidadeMedida*/, null/*dtEntregaPrevista*/, 
						0/*cdNaturezaOperacao*/, 0/*cdAdicao*/, 0/*cdItem*/, 0/*vlVucv*/, 0/*vlDescontoGeral*/, 
						0/*cdTipoCredito*/);
				result = DocumentoEntradaItemServices.insert(item, cdLocalArmazenamento, connect);
				if(result.getCode()<=0) {
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
			} rsmProduto.beforeFirst();
			
			/**
			 * saida consignada
			 */
			DocumentoSaida saidaConsignada = new DocumentoSaida(0/*cdDocumentoSaida*/, 
					0/*cdTransportadora*/, 
					cdEmpresa, 
					0/*cdCliente*/, 
					planoTrabalho.getDtTrabalho()/*dtDocumentoSaida*/, 
					DocumentoSaidaServices.ST_CONCLUIDO/*stDocumentoSaida*/, 
					planoTrabalho.getIdPlano()/*nrDocumentoSaida*/, 
					DocumentoSaidaServices.TP_CONSUMO/*tpDocumentoSaida*/, 
					DocumentoSaidaServices.SAI_ORDEM_SERVICO/*tpSaida*/, 
					null/*nrConhecimento*/, 0/*vlDesconto*/, 0/*vlAcrescimo*/, 
					new GregorianCalendar()/*dtEmissao*/, 
					DocumentoSaidaServices.FRT_SEM_COBRANCA /*tpFrete*/, 
					null/*txtMensagem*/, null/*txtObservacao*/, null/*nrPlacaVeiculo*/, 
					null/*sgPlacaVeiculo*/, null/*nrVolumes*/, null/*dtSaidaTransportadora*/, 
					null/*dsViaTransporte*/, 0/*cdNaturezaOperacao*/, null/*txtCorpoNotaFiscal*/, 
					0/*vlPesoLiquido*/, 0/*vlPesoBruto*/, null/*dsEspecieVolumes*/, 
					null/*dsMarcaVolumes*/, 0/*qtVolumes*/, 
					DocumentoSaidaServices.MOV_ESTOQUE_CONSIGNADO/*tpMovimentoEstoque*/, 
					0/*cdVendedor*/, 0/*cdMoeda*/, 0/*cdReferenciaEcf*/, 
					0/*cdSolicitacaoMaterial*/, 0/*cdTipoOperacao*/, 
					0/*vlTotalDocumento*/, //TODO: soma de vl_ordem_servico 
					0/*cdContrato*/, 0/*vlFrete*/, 0/*vlSeguro*/, 
					0/*cdDigitador*/, 0/*cdDocumento*/, 0/*cdConta*/, 
					0/*cdTurno*/, 0/*vlTotalItens*/, 0/*nrSerie*/);
			result = DocumentoSaidaServices.insert(saidaConsignada, 0, connect);
			if(result.getCode()<=0) {
				if(isConnectionNull)
					Conexao.rollback(connect);
				return result;
			}
			saidaConsignada = (DocumentoSaida)result.getObjects().get("docSaida");
			
			while(rsmProduto.next()) {
				DocumentoSaidaItem item = new DocumentoSaidaItem(saidaConsignada.getCdDocumentoSaida()/*cdDocumentoSaida*/, 
						rsmProduto.getInt("cd_produto_servico")/*cdProdutoServico*/, 
						cdEmpresa, 
						rsmProduto.getInt("qtSaida")/*qtSaida*/, 
						0/*vlUnitario*/, 0/*vlAcrescimo*/, 0/*vlDesconto*/, 
						null/*dtEntregaPrevista*/, 0/*cdUnidadeMedida*/, 0/*cdTabelaPreco*/, 
						0/*cdItem*/, 0/*cdBico*/);
				result = DocumentoSaidaItemServices.insert(item, cdLocalArmazenamento, connect);
				if(result.getCode()<=0) {
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
				
			} rsmProduto.beforeFirst();
			
			//PLANO_TRABALHO_ITEM			
			PlanoTrabalhoItem itemConsignado = new PlanoTrabalhoItem(planoTrabalho.getCdPlanoTrabalho(), 0,
					saidaConsignada.getCdDocumentoSaida(), 
					entradaConsignada.getCdDocumentoEntrada(), 
					PlanoTrabalhoItemServices.TP_MOVIMENTO_CONSIGNADO);
			result = PlanoTrabalhoItemServices.save(itemConsignado, authData, connect);
			if(result.getCode()<0) {
				if(isConnectionNull)
					connect.rollback();
				return result;
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			result = new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PLANOTRABALHO", planoTrabalho);
			return result;
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
	public static Result remove(PlanoTrabalho planoTrabalho) {
		return remove(planoTrabalho.getCdPlanoTrabalho());
	}
	public static Result remove(int cdPlanoTrabalho){
		return remove(cdPlanoTrabalho, false, null, null);
	}
	public static Result remove(int cdPlanoTrabalho, boolean cascade){
		return remove(cdPlanoTrabalho, cascade, null, null);
	}
	public static Result remove(int cdPlanoTrabalho, boolean cascade, AuthData authData){
		return remove(cdPlanoTrabalho, cascade, authData, null);
	}
	public static Result remove(int cdPlanoTrabalho, boolean cascade, AuthData authData, Connection connect){
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
			retorno = PlanoTrabalhoDAO.delete(cdPlanoTrabalho, connect);
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

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ord_plano_trabalho");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result retornar(int cdPlanoTrabalho, ArrayList<Integer> rsmProdutos) {
		return retornar(cdPlanoTrabalho, rsmProdutos, null);
	}
	
	public static Result retornar(int cdPlanoTrabalho, ArrayList<Integer> rsmProdutos, Connection connection) {
		boolean isConnectionNull = connection==null;
		
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			/*
			 * 1. finalizar saídas consignadas
			 * 2. lançar saída com os produtos que não voltaram
			 * 3. concluir plano
			 */

			
			
			return new Result(1);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoServices.retornar: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		
		try {
			
			String sql = "SELECT A.* "
					+ " FROM ord_plano_trabalho A"
					+ " WHERE 1=1 ";
			
			
			return Search.find(sql, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoServices.find: " + e);
			return null;
		}
		
	}

}
