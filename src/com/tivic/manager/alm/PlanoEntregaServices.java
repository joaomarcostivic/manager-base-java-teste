package com.tivic.manager.alm;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.acd.Circulo;
import com.tivic.manager.acd.CirculoServices;
import com.tivic.manager.acd.InstituicaoCirculoDAO;
import com.tivic.manager.acd.InstituicaoCirculoServices;
import com.tivic.manager.cae.CardapioGrupoServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.NumeracaoDocumentoServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ProdutoServico;
import com.tivic.manager.grl.ProdutoServicoDAO;
import com.tivic.manager.seg.AuthData;

import flex.messaging.io.ObjectProxy;
import flex.messaging.io.amf.ASObject;

public class PlanoEntregaServices {
	
	public static final int      ST_PLANEJAMENTO = 0;
	public static final int      ST_EXECUCAO     = 1;
	public static final int      ST_CONCLUIDO    = 2;
	public static final String[] situacoes       = {"Em planejamento", "Em execução", "Concluído"};

	public static Result save(PlanoEntrega planoEntrega){
		return save(planoEntrega, null, null);
	}

	public static Result save(PlanoEntrega planoEntrega, AuthData authData){
		return save(planoEntrega, authData, null);
	}

	public static Result save(PlanoEntrega planoEntrega, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(planoEntrega==null)
				return new Result(-1, "Erro ao salvar. PlanoEntrega é nulo");

			int retorno;
			if(planoEntrega.getCdPlano()==0){
				retorno = PlanoEntregaDAO.insert(planoEntrega, connect);
				planoEntrega.setCdPlano(retorno);
			}
			else {
				retorno = PlanoEntregaDAO.update(planoEntrega, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PLANOENTREGA", planoEntrega);
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
	

	public static Result savePlanoEntregaDocumentoSaida(PlanoEntrega planoEntrega, ArrayList<DocumentoSaida> documentoSaida, ArrayList<?> documentoSaidaItem){
		return savePlanoEntregaDocumentoSaida(planoEntrega, documentoSaida, documentoSaidaItem, null);
	}
	
	public static Result savePlanoEntregaDocumentoSaida(PlanoEntrega planoEntrega, ArrayList<DocumentoSaida> documentoSaida, ArrayList<?> documentoSaidaItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(planoEntrega==null)
				return new Result(-1, "Erro ao salvar. PlanoEntrega é nulo");
			
			int retorno = 0;
			int cdEmpresa = documentoSaida.get(0).getCdEmpresa();
			int cdLocalArmazenamento = ParametroServices.getValorOfParametroAsInteger("CD_LOCAL_ARMAZENAMENTO_DEFAULT_CAE", 0, 0, connect);
			
			if(planoEntrega.getCdPlano()==0){
				NumeracaoDocumentoServices.getProximoNumero("PLANO_ENTREGA", new GregorianCalendar().get(Calendar.YEAR), cdEmpresa, connect);
				/* Inserindo o plano de entrega e retornando o código do plano inserido. */
				retorno = PlanoEntregaDAO.insert(planoEntrega, connect);
				planoEntrega.setCdPlano(retorno);
			} else {
				retorno = PlanoEntregaDAO.update(planoEntrega, connect);
				
				if(retorno > 0){
					if(resetPlanoEntrega(planoEntrega.getCdPlano(), connect).getCode() <= 0){
						return new Result(-1, "Erro ao salvar. Houve um problema ao remover os documentos antigos.");						
					}
				}
			}
			

			/* Percorrendo as guias a serem incluídas no banco
			 * Sendo uma por instituição
			 */
			for(int iDoc = 0; iDoc < documentoSaida.size(); iDoc++){
				String nrDocumentoSaida = DocumentoSaidaServices.getProximoNrDocumento(cdEmpresa, connect);
				DocumentoSaida docSaida = documentoSaida.get(iDoc);
				docSaida.setNrDocumentoSaida(nrDocumentoSaida);			
				Result result = DocumentoSaidaServices.insert(docSaida, 0, connect);
				
				int cdDocumentoSaida = result.getCode();
					cdEmpresa 		 = docSaida.getCdEmpresa();
				
				
				/* Percorrendo os itens a serem incluídos no banco
				 * Sendo vários itens por guia
				 */
				for (Object obj : documentoSaidaItem) {
					@SuppressWarnings("unchecked")
					HashMap<String, Object> register = ((HashMap<String, Object>) (ObjectProxy)obj);						
					
					if(docSaida.getCdCliente() == (int)register.get("CD_INSTITUICAO")){
						DocumentoSaidaItem docSaidaItem = new DocumentoSaidaItem();
						docSaidaItem.setCdDocumentoSaida(cdDocumentoSaida);
						docSaidaItem.setCdEmpresa(docSaida.getCdEmpresa());
						docSaidaItem.setCdProdutoServico((int) register.get("CD_PRODUTO_SERVICO"));
						docSaidaItem.setQtSaida(Float.parseFloat(String.valueOf(register.get("QT_SAIDA"))));
						docSaidaItem.setCdItem(0);							
						
						if(register.get("CD_UNIDADE_MEDIDA") != null)
							docSaidaItem.setCdUnidadeMedida((int) register.get("CD_UNIDADE_MEDIDA"));
						
						DocumentoSaidaItemServices.insert(docSaidaItem, 0, connect);												
					}
				}
				
				if(PlanoEntregaItemDAO.get(retorno, cdDocumentoSaida, connect) == null){
					PlanoEntregaItem planoEntregaItem = new PlanoEntregaItem(planoEntrega.getCdPlano(), cdDocumentoSaida, 0, 0);
					PlanoEntregaItemDAO.insert(planoEntregaItem, connect);
				}
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PLANOENTREGA", planoEntrega);
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
	
	public static Result saveRetornoPlanoEntrega(PlanoEntrega planoEntrega, ResultSetMap rsmPlanoEntrega, int cdEmpresa){
		return saveRetornoPlanoEntrega(planoEntrega, rsmPlanoEntrega, cdEmpresa, null);		
	}
	
	public static Result saveRetornoPlanoEntrega(PlanoEntrega planoEntrega, ResultSetMap rsmPlanoEntrega, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(planoEntrega==null)
				return new Result(-1, "Erro ao salvar. PlanoEntrega é nulo");
			
			int retorno				 				= 0;
			int cdLocalArmazenamento 				= ParametroServices.getValorOfParametroAsInteger("CD_LOCAL_ARMAZENAMENTO_DEFAULT_CAE", 0, 0, connect);
			ResultSetMap rsmLocalArmazenamentoSaida = new ResultSetMap();;
			
			/* Percorrendo as entradas a serem incluídas no banco
			 * Sendo uma por instituição
			 */
			
			while(rsmPlanoEntrega.next()){
				int cdDocumentoSaida 				= rsmPlanoEntrega.getInt("CD_DOCUMENTO_SAIDA");
				PlanoEntregaItem planoEntregaItem 	= PlanoEntregaItemDAO.get(planoEntrega.getCdPlano(), cdDocumentoSaida, connect);
				
				/* Buscando saídas normais */
				DocumentoSaida docSaida       		= DocumentoSaidaDAO.get(planoEntregaItem.getCdDocumentoSaida(), connect);
				ResultSetMap   rsmSaidaItem  		= (ResultSetMap) rsmPlanoEntrega.getObject("RSMPRODUTOS");
				
				while(rsmSaidaItem.next()){			
					int   cdProdutoServico          = rsmSaidaItem.getInt("CD_PRODUTO_SERVICO"); 
					int   cdItem                    = rsmSaidaItem.getInt("CD_ITEM");
					float qtRecebida                = Float.parseFloat(String.valueOf(rsmSaidaItem.getObject("QT_RECEBIDA")));
					
					DocumentoSaidaItem docSaidaItem = DocumentoSaidaItemDAO.get(docSaida.getCdDocumentoSaida(), cdProdutoServico, cdEmpresa, cdItem, connect);
					docSaidaItem.setQtSaida(qtRecebida);
					retorno = DocumentoSaidaItemDAO.update(docSaidaItem, connect);		
										
					SaidaLocalItem saidaLocal = new SaidaLocalItem();		
					saidaLocal.setCdDocumentoSaida(docSaidaItem.getCdDocumentoSaida());
					saidaLocal.setCdEmpresa(docSaidaItem.getCdEmpresa());
					saidaLocal.setCdItem(docSaidaItem.getCdItem());
					saidaLocal.setCdProdutoServico(docSaidaItem.getCdProdutoServico());
					saidaLocal.setQtSaida(docSaidaItem.getQtSaida());
					saidaLocal.setStSaidaLocalItem(SaidaLocalItemServices.ST_RECEBIDO_CLIENTE);
					saidaLocal.setCdLocalArmazenamento(cdLocalArmazenamento);
					retorno = SaidaLocalItemDAO.insert(saidaLocal, connect);
				}
				
				if(retorno>=0) {
					docSaida.setStDocumentoSaida(DocumentoSaidaServices.ST_CONCLUIDO);
					DocumentoSaidaDAO.update(docSaida, connect);
				}
				
				/* Buscando saídas consignadas */
				DocumentoSaida   docSaidaConsig   	= DocumentoSaidaDAO.get(planoEntregaItem.getCdDocumentoSaidaConsignada(), connect);
				DocumentoEntrada docEntradaConsig 	= new DocumentoEntrada();
				String nrDocumentoEntrada 			= DocumentoEntradaServices.getProximoNrDocumento(cdEmpresa, connect);
				rsmLocalArmazenamentoSaida  		= LocalArmazenamentoServices.getAll(docSaida.getCdCliente(), connect);
				int cdLocalArmazenamentoSaida       = rsmLocalArmazenamentoSaida.size() > 0 ? (int) rsmLocalArmazenamentoSaida.getLines().get(0).get("CD_LOCAL_ARMAZENAMENTO") : 0;
				
				if(cdLocalArmazenamentoSaida <= 0){
					return new Result(-51, "Uma das instituições não possui um Local de Armazenamento disponível, favor entre em contato com o suporte do sistema.");
				}
				
				docEntradaConsig.setCdDocumentoSaidaOrigem(docSaidaConsig.getCdDocumentoSaida());
				docEntradaConsig.setCdEmpresa(docSaidaConsig.getCdEmpresa());
				docEntradaConsig.setCdFornecedor(docSaidaConsig.getCdCliente());
				docEntradaConsig.setNrDocumentoEntrada(nrDocumentoEntrada);
				docEntradaConsig.setDtEmissao(new GregorianCalendar());
				docEntradaConsig.setDtDocumentoEntrada(new GregorianCalendar());
				docEntradaConsig.setTpMovimentoEstoque(DocumentoEntradaServices.MOV_ESTOQUE_CONSIGNADO);
				docEntradaConsig.setStDocumentoEntrada(DocumentoEntradaServices.ST_LIBERADO);
				docEntradaConsig.setTpEntrada(DocumentoEntradaServices.ENT_CONSIGNACAO);
				
				retorno = DocumentoEntradaServices.save(docEntradaConsig, connect).getCode();
				
				if(retorno > 0) {
					ResultSetMap rsmSaidaItensConsig = DocumentoSaidaItemServices.getAllItensDocumentoSaida(cdEmpresa, planoEntregaItem.getCdDocumentoSaidaConsignada(), connect);
					
					rsmSaidaItensConsig.beforeFirst();
					int cdItem = 1;
					int cdDocumentoEntrada = 0;
					while(rsmSaidaItensConsig.next()){
						if(cdDocumentoEntrada == retorno)
							cdItem++;
						else 
							cdItem = 1;

						cdDocumentoEntrada = retorno;
						DocumentoEntradaItem documentoEntradaItem = new DocumentoEntradaItem();
						documentoEntradaItem.setCdDocumentoEntrada(retorno);
						documentoEntradaItem.setCdProdutoServico(rsmSaidaItensConsig.getInt("CD_PRODUTO_SERVICO"));
						documentoEntradaItem.setCdEmpresa(rsmSaidaItensConsig.getInt("CD_EMPRESA"));
						documentoEntradaItem.setCdItem(cdItem);
						documentoEntradaItem.setCdUnidadeMedida(rsmSaidaItensConsig.getInt("CD_UNIDADE_MEDIDA"));
						documentoEntradaItem.setQtEntrada(rsmSaidaItensConsig.getFloat("QT_SAIDA"));
						DocumentoEntradaItemServices.insert(documentoEntradaItem, connect).getCode();
						
						EntradaLocalItem localEntrada = new EntradaLocalItem();
						localEntrada.setCdDocumentoEntrada(documentoEntradaItem.getCdDocumentoEntrada());
						localEntrada.setCdEmpresa(documentoEntradaItem.getCdEmpresa());
						localEntrada.setCdItem(documentoEntradaItem.getCdItem());
						localEntrada.setQtEntradaConsignada(documentoEntradaItem.getQtEntrada());
						localEntrada.setCdLocalArmazenamento(cdLocalArmazenamento);
						localEntrada.setCdProdutoServico(documentoEntradaItem.getCdProdutoServico());
						EntradaLocalItemServices.insert(localEntrada, connect);
						
						planoEntregaItem.setCdDocumentoEntrada(retorno);
						PlanoEntregaItemDAO.update(planoEntregaItem, connect);
					}
					
				}

				nrDocumentoEntrada 					= DocumentoEntradaServices.getProximoNrDocumento(cdEmpresa, connect);
				DocumentoEntrada docEntradaCliente 	= (DocumentoEntrada) docEntradaConsig.clone();
				docEntradaCliente.setCdDocumentoEntrada(0);
				docEntradaCliente.setCdDocumentoSaidaOrigem(docSaida.getCdDocumentoSaida());
				docEntradaCliente.setCdEmpresa(docSaida.getCdEmpresa());
				docEntradaCliente.setCdFornecedor(docSaida.getCdCliente());
				docEntradaCliente.setNrDocumentoEntrada(nrDocumentoEntrada);
				docEntradaCliente.setDtEmissao(new GregorianCalendar());
				docEntradaCliente.setDtDocumentoEntrada(new GregorianCalendar());
				docEntradaCliente.setTpMovimentoEstoque(DocumentoEntradaServices.MOV_ESTOQUE_NAO_CONSIGNADO);
				docEntradaCliente.setTpEntrada(DocumentoEntradaServices.TP_DOC_NAO_FISCAL);
				docEntradaCliente.setStDocumentoEntrada(DocumentoEntradaServices.ST_LIBERADO);
				docEntradaCliente.setTpEntrada(DocumentoEntradaServices.ENT_COMPRA);
				
				retorno = DocumentoEntradaServices.save(docEntradaCliente, connect).getCode();
				
				if(cdLocalArmazenamentoSaida > 0){
					ResultSetMap rsmSaidaItens = DocumentoSaidaItemServices.getAllItensDocumentoSaida(cdEmpresa, planoEntregaItem.getCdDocumentoSaida(), connect);
					
					rsmSaidaItens.beforeFirst();
					while(rsmSaidaItens.next()){
						DocumentoEntradaItem documentoEntradaItem = new DocumentoEntradaItem();
						documentoEntradaItem.setCdDocumentoEntrada(retorno);
						documentoEntradaItem.setCdProdutoServico(rsmSaidaItens.getInt("CD_PRODUTO_SERVICO"));
						documentoEntradaItem.setCdEmpresa(rsmSaidaItens.getInt("CD_EMPRESA"));
						documentoEntradaItem.setCdItem(rsmSaidaItens.getInt("CD_ITEM"));
						documentoEntradaItem.setCdUnidadeMedida(rsmSaidaItens.getInt("CD_UNIDADE_MEDIDA"));
						documentoEntradaItem.setQtEntrada(rsmSaidaItens.getFloat("QT_SAIDA"));
						DocumentoEntradaItemServices.insert(documentoEntradaItem, connect).getCode();					
						
						EntradaLocalItem localEntradaCliente = new EntradaLocalItem();
						localEntradaCliente.setCdDocumentoEntrada(documentoEntradaItem.getCdDocumentoEntrada());
						localEntradaCliente.setCdEmpresa(documentoEntradaItem.getCdEmpresa());
						localEntradaCliente.setCdItem(documentoEntradaItem.getCdItem());
						localEntradaCliente.setQtEntradaConsignada(documentoEntradaItem.getQtEntrada());
						localEntradaCliente.setCdLocalArmazenamento(cdLocalArmazenamentoSaida);
						localEntradaCliente.setCdProdutoServico(documentoEntradaItem.getCdProdutoServico());
						EntradaLocalItemServices.insert(localEntradaCliente, connect);
					}
				}
				
			}
			
			planoEntrega.setStPlano(ST_CONCLUIDO);
			retorno = PlanoEntregaDAO.update(planoEntrega, connect);

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PLANOENTREGA", planoEntrega);
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
	
	public static Result remove(int cdPlano){
		return remove(cdPlano, false, null);
	}
	public static Result remove(int cdPlano, boolean cascade){
		return remove(cdPlano, cascade, null);
	}
	public static Result remove(int cdPlano, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				ResultSetMap rsmPlanoItem = PlanoEntregaItemServices.getAllByPlano(cdPlano, connect);
				
				while(rsmPlanoItem.next()){
					PreparedStatement pstmt = connect.prepareStatement("delete from alm_saida_local_item where cd_documento_saida = ?");
					pstmt.setInt(1, rsmPlanoItem.getInt("CD_DOCUMENTO_SAIDA"));
					pstmt.execute();
					
					int cdDocumentoSaida = rsmPlanoItem.getInt("CD_DOCUMENTO_SAIDA"); 
					retorno = DocumentoSaidaItemDAO.delete(cdDocumentoSaida, connect);								
					retorno = PlanoEntregaItemDAO.delete(cdPlano, cdDocumentoSaida, connect);			
					retorno = DocumentoSaidaDAO.delete(cdDocumentoSaida, connect);								
				}
			}
			if(!cascade || retorno>0)
			retorno = PlanoEntregaDAO.delete(cdPlano, connect);
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
	
	public static Result resetPlanoEntrega(int cdPlano, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno   = 0;
			
			ResultSetMap rsmAllItens = PlanoEntregaItemServices.getAllByPlano(cdPlano, connect);
			
			while(rsmAllItens.next()){
				int cdDocumentoSaida   = rsmAllItens.getInt("CD_DOCUMENTO_SAIDA");
				int cdDocumentoEntrada = rsmAllItens.getInt("CD_DOCUMENTO_ENTRADA");

				PlanoEntregaItemServices.remove(cdPlano, cdDocumentoSaida, false, connect);
				
				retorno = DocumentoSaidaItemDAO.delete(cdDocumentoSaida, connect);
				retorno = DocumentoEntradaItemDAO.delete(cdDocumentoEntrada, connect);
				retorno = DocumentoEntradaDAO.delete(cdDocumentoEntrada, connect);
				retorno = DocumentoSaidaDAO.delete(cdDocumentoSaida, connect);		
				connect.prepareStatement("DELETE FROM alm_saida_local_item WHERE cd_documento_saida = "+cdDocumentoSaida).executeUpdate();
				connect.prepareStatement("DELETE FROM alm_entrada_local_item WHERE cd_documento_entrada = "+cdDocumentoEntrada).executeUpdate();
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Estes registros estão vinculados a outros e não podem ser excluídos!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registros excluídos com sucesso!");
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
	
	public static String getProximoNrDocumento(int cdEmpresa){
		return getProximoNrDocumento(cdEmpresa, null);
	}
	
	public static String getProximoNrDocumento(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			int nrAno = new GregorianCalendar().get(Calendar.YEAR);
			int nrDocumento = 0;

			if ((nrDocumento = NumeracaoDocumentoServices.getProximoNumero2("PLANO_ENTREGA", nrAno, cdEmpresa, connection)) <= 0)
				return null;

			return new DecimalFormat("000000").format(nrDocumento) + "/" + nrAno;
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result planoBaixa(int cdPlano){
		return planoBaixa(cdPlano, null);
	}
	
	public static Result planoBaixa(int cdPlano, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull){
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			PlanoEntrega plano 			= PlanoEntregaDAO.get(cdPlano, connection);
			int cdEmpresa 				= ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0, 0, connection);
			int cdLocalArmazenamento 	= ParametroServices.getValorOfParametroAsInteger("CD_LOCAL_ARMAZENAMENTO_DEFAULT_CAE", 0, 0, connection);
			int retorno 				= 0;
			
			if(plano==null)
				return new Result(-1, "O plano informado não existe.");
			
			ResultSetMap rsmPlanoItens = PlanoEntregaItemServices.getAllByPlano(cdPlano, connection);
			
			
			if(rsmPlanoItens.size() <= 0)
				return new Result(-1, "Não existe itens atrelados a este plano.");
			
			rsmPlanoItens.beforeFirst();
			while(rsmPlanoItens.next()){

				int cdDocumentoSaida   = rsmPlanoItens.getInt("CD_DOCUMENTO_SAIDA");
				DocumentoSaida saida   = DocumentoSaidaDAO.get(cdDocumentoSaida, connection);
				ResultSetMap saidaItem = DocumentoSaidaItemServices.getAllItensDocumentoSaida(cdEmpresa, saida.getCdDocumentoSaida(), connection);
				
				if (cdLocalArmazenamento > 0) {

					/* Criando um espelho dos documentos de saídas e mudando o tipo de movimento de estoque para saída consignada. */
					DocumentoSaida    docSaidaConsignada = (DocumentoSaida) saida.clone();
					docSaidaConsignada.setCdDocumento(0);
					docSaidaConsignada.setTpMovimentoEstoque(DocumentoSaidaServices.MOV_ESTOQUE_CONSIGNADO);
					docSaidaConsignada.setStDocumentoSaida(DocumentoSaidaServices.ST_CONCLUIDO);
					docSaidaConsignada.setTpSaida(DocumentoSaidaServices.SAI_VENDA_CONSIGNACAO);
					int cdDocumentoSaidaConsignada    	 = DocumentoSaidaDAO.insert(docSaidaConsignada, connection);
					
					/* Verificando item a item do documento de saída e adicionando um espelho de cada */
					while(saidaItem.next()){
						int cdItem         						  = saidaItem.getInt("CD_ITEM");
						int cdProdutoServico					  = saidaItem.getInt("CD_PRODUTO_SERVICO");
						DocumentoSaidaItem docSaidaItemConsignada = DocumentoSaidaItemDAO.get(cdDocumentoSaida, cdProdutoServico, cdEmpresa, cdItem, connection);	
						docSaidaItemConsignada.setCdDocumentoSaida(cdDocumentoSaidaConsignada);
						DocumentoSaidaItemDAO.insert(docSaidaItemConsignada, connection);						
					}
					
					/* Atualizando o plano de entrega com as informações da execução do plano */
					PlanoEntregaItem  planoEntregaItem = new PlanoEntregaItem();
					planoEntregaItem.setCdPlano(cdPlano);
					planoEntregaItem.setCdDocumentoSaida(cdDocumentoSaida);
					planoEntregaItem.setCdDocumentoSaidaConsignada(cdDocumentoSaidaConsignada);
					PlanoEntregaItemDAO.update(planoEntregaItem, connection);
					
					if (saida==null || saida.getStDocumentoSaida()!=DocumentoSaidaServices.ST_EM_CONFERENCIA){				
						return new Result(-1, "Documento cancelado ou já liberado!");
					}
									
					ResultSet rs = connection.prepareStatement("SELECT * FROM alm_documento_saida_item WHERE cd_documento_saida = "+cdDocumentoSaidaConsignada).executeQuery();					
					while(rs.next()) {
						float qtSaida      	 	= docSaidaConsignada.getTpMovimentoEstoque()==DocumentoSaidaServices.MOV_ESTOQUE_NAO_CONSIGNADO || 
												  docSaidaConsignada.getTpMovimentoEstoque()==DocumentoSaidaServices.MOV_AMBOS_TIPO_ESTOQUE ? rs.getFloat("qt_saida") : 0;											   
						float qtSaidaConsignada = docSaidaConsignada.getTpMovimentoEstoque()==DocumentoSaidaServices.MOV_ESTOQUE_CONSIGNADO ? rs.getFloat("qt_saida") : 0;
						
						SaidaLocalItem saidaLocal = new SaidaLocalItem(0 /*cdSaidaLocalItem*/, rs.getInt("cd_produto_servico"), cdDocumentoSaidaConsignada,
								                                       rs.getInt("cd_empresa"), cdLocalArmazenamento, 0 /*cdPedidoVenda*/,
								                                       new GregorianCalendar() /*dtSaida*/, qtSaida, qtSaidaConsignada,
								                                       SaidaLocalItemServices.ST_ENVIADO /*stSaidaLocalItem*/, "" /*idSaidaLocalItem*/,
								                                       rs.getInt("cd_item") /*cdItem*/);				
						retorno = SaidaLocalItemDAO.insert(saidaLocal, connection);
						if(retorno <= 0)
							return new Result(-1, "Erro ao tentar registrar saída do local de armazenamento!");
					}
				
					PreparedStatement pstmtSaidaLocal = connection.prepareStatement("SELECT SUM(qt_saida) AS qt_saida, " +
	                        "       SUM(qt_saida_consignada) AS qt_saida_consignada " +
	                        "FROM alm_saida_local_item " +
	                        "WHERE cd_produto_servico = ? " +
	                        "  AND cd_item            = ? " +
	                        "  AND cd_empresa         = "+saida.getCdEmpresa()+
	                        "  AND cd_documento_saida = "+cdDocumentoSaidaConsignada);
					
					ResultSetMap rsItens = new ResultSetMap(connection.prepareStatement("SELECT * FROM alm_documento_saida_item WHERE cd_documento_saida = "+cdDocumentoSaidaConsignada).executeQuery());
					
					while (rsItens.next()) {
						ProdutoServico produto = ProdutoServicoDAO.get(rsItens.getInt("cd_produto_servico"), connection); 
						
						pstmtSaidaLocal.setInt(1, rsItens.getInt("cd_produto_servico"));
						pstmtSaidaLocal.setInt(2, rsItens.getInt("cd_item"));
						
						ResultSetMap rsTemp = new ResultSetMap(pstmtSaidaLocal.executeQuery());				
						float qtSaida        = rsItens.getFloat("qt_saida");
						float qtLocaisSaida = 0;				
						rsTemp.beforeFirst();
						
						while (rsTemp.next()){
							qtLocaisSaida  = rsTemp.getFloat("qt_saida") + rsTemp.getFloat("qt_saida_consignada");
						}
						
						if (qtLocaisSaida != qtSaida) {					
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Quantidade lançada no local de armazenamento é diferente do que o informado na saída! " +
									              "\n[qtLocaisSaida:"+qtLocaisSaida+",qtSaida:"+qtSaida+"]");
						}
					}
	
					rsItens.beforeFirst();

				}
				
			}
			
			if(retorno > 0) {
				plano.setStPlano(ST_EXECUCAO);
				retorno = PlanoEntregaDAO.update(plano, connection);
			}			
			
			plano = PlanoEntregaDAO.get(cdPlano, connection); 
			
			if(retorno<=0){
				Conexao.rollback(connection);
				return new Result(-3, "Houve um erro ao atualizar o estoque.");
			}
			else if (isConnectionNull)
				connection.commit();
			
			return new Result(1, "Plano de Entrega atualizado com sucesso!", "PLANOENTREGA", plano);
		} catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_plano_entrega");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getEntregaAndItem(int cdPlano) {
		return getEntregaAndItem(cdPlano, null);
	}

	public static ResultSetMap getEntregaAndItem(int cdPlano, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			ResultSetMap rsmDocumentosPlano = new ResultSetMap();
			ResultSetMap rsmDocumentoSaida  = new ResultSetMap();
			ResultSetMap rsmPlanoEntrega    = new ResultSetMap(); 
			
			pstmt = connect.prepareStatement(
					"SELECT A.*, B.*, B.nm_pessoa as nm_supervisor FROM alm_plano_entrega A "
				  + "LEFT OUTER JOIN grl_pessoa B ON (A.cd_supervisor = B.cd_pessoa) "
				  + "WHERE A.cd_plano = ?"
			);
			
			pstmt.setInt(1, cdPlano);
			rsmPlanoEntrega = new ResultSetMap(pstmt.executeQuery());
			
			pstmt = connect.prepareStatement(
					"SELECT * FROM alm_plano_entrega A "
				  + "JOIN alm_plano_entrega_item B ON (A.cd_plano = B.cd_plano) "
				  + "WHERE A.cd_plano = ?"
			);
			
			pstmt.setInt(1, cdPlano);
			ResultSetMap rsmPlanoSaidaItem = new ResultSetMap(pstmt.executeQuery());
			
			while(rsmPlanoSaidaItem.next()){
 				int cdDocumentoSaida   	= rsmPlanoSaidaItem.getInt("CD_DOCUMENTO_SAIDA");
 				int cdDocumentoEntrada  = rsmPlanoSaidaItem.getInt("CD_DOCUMENTO_ENTRADA");
				rsmDocumentoSaida      	= DocumentoSaidaServices.get(cdDocumentoSaida, connect);
				
				while(rsmDocumentoSaida.next()){
					
					int cdModalidade                = 0;
					int cdProdutoServico			= 0;
					int cdEmpresa                   = rsmDocumentoSaida.getInt("CD_EMPRESA");
					int cdInstituicao               = rsmDocumentoSaida.getInt("CD_CLIENTE");
					int nrMesReferencia             = (int) rsmPlanoEntrega.getLines().get(0).get("NR_MES_REFERENCIA");
					ResultSetMap documentoSaidaItem = DocumentoSaidaItemServices.getAllItensDocumentoSaida(cdEmpresa, cdDocumentoSaida, connect);
					
					/* Buscando círculo e modalidade para obter os valores de per capta e quantidade de vezes no cardápio */
					pstmt = connect.prepareStatement("SELECT B.cd_modalidade FROM acd_instituicao_circulo A, alm_circulo_modalidade B WHERE A.cd_instituicao = ? AND A.cd_circulo = B.cd_circulo LIMIT 1");					
					pstmt.setInt(1, cdInstituicao);	
					
					ResultSet rsCirculo = pstmt.executeQuery();
					
					if(rsCirculo.next())
						cdModalidade = rsCirculo.getInt("cd_modalidade");
					
					if(cdModalidade > 0) {
						while(documentoSaidaItem.next()){
							cdProdutoServico = documentoSaidaItem.getInt("CD_PRODUTO_SERVICO");
							int cdItem = documentoSaidaItem.getInt("CD_ITEM");
							ResultSetMap ingredientes   = CardapioGrupoServices.getIngredientesByModalidade(cdModalidade, nrMesReferencia, cdProdutoServico, connect);
							DocumentoEntradaItem docEntradaConsig = DocumentoEntradaItemDAO.get(cdDocumentoEntrada, cdProdutoServico, cdEmpresa, 1);
							ingredientes.beforeFirst();
							
							System.out.println(docEntradaConsig);
							while(ingredientes.next()){
								documentoSaidaItem.setValueToField("VL_PER_CAPTA", 		ingredientes.getDouble("VL_PER_CAPTA") / 1000);
								documentoSaidaItem.setValueToField("QT_CARDAPIO", 		ingredientes.getDouble("QTDCARDAPIO"));
								if(docEntradaConsig != null)
									documentoSaidaItem.setValueToField("QT_SAIDA_VIAGEM", 	docEntradaConsig.getQtEntrada());
							}
						}
					}
					
					rsmDocumentoSaida.setValueToField("rsmProdutos", documentoSaidaItem);
					rsmDocumentosPlano.addRegister(rsmDocumentoSaida.getRegister());
				}
			}
			
			while(rsmPlanoEntrega.next()){
				rsmPlanoEntrega.setValueToField("docSaida", rsmDocumentosPlano);
			}
			
			return rsmPlanoEntrega;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaServices.getEntregaAndItem: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaServices.getAll: " + e);
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
		return Search.find(
              "SELECT A.*, B.* FROM alm_plano_entrega A "
            + "LEFT OUTER JOIN grl_pessoa B ON (A.cd_supervisor = B.cd_pessoa) "
    		+ "WHERE 1 = 1 ",
		criterios, connect!=null ? connect : Conexao.conectar());
	}

}