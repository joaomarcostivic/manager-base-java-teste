package com.tivic.manager.alm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.tivic.manager.adm.EntradaItemAliquota;
import com.tivic.manager.adm.EntradaItemAliquotaDAO;
import com.tivic.manager.adm.EntradaItemAliquotaServices;
import com.tivic.manager.adm.TributoAliquota;
import com.tivic.manager.adm.TributoAliquotaDAO;
import com.tivic.manager.adm.TributoAliquotaServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.fsc.SituacaoTributaria;
import com.tivic.manager.fsc.SituacaoTributariaDAO;
import com.tivic.manager.grl.FormularioAtributoValor;
import com.tivic.manager.grl.FormularioAtributoValorDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ProdutoServico;
import com.tivic.manager.grl.ProdutoServicoDAO;
import com.tivic.manager.grl.ProdutoServicoEmpresa;
import com.tivic.manager.grl.ProdutoServicoEmpresaDAO;
import com.tivic.manager.grl.ProdutoServicoEmpresaServices;
import com.tivic.manager.grl.ProdutoServicoServices;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class DocumentoEntradaItemServices {

	public static final int ERR_DOC_BALANCO = -2;

	public static ResultSetMap getAllAliquotas(int cdDocumentoEntrada, int cdProdutoServico) {
		return getAllAliquotas(cdDocumentoEntrada, cdProdutoServico, null);
	}

	public static ResultSetMap getAllAliquotas(int cdDocumentoEntrada, int cdProdutoServico, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.pr_aliquota AS pr_aliquota_tributo, B.pr_credito, B.st_tributaria, " +
					                                         "       B.tp_operacao, C.nm_tributo, C.id_tributo " +
															 "FROM adm_entrada_item_aliquota A, adm_tributo_aliquota B, adm_tributo C " +
															 "WHERE A.cd_tributo_aliquota  = B.cd_tributo_aliquota " +
															 "  AND A.cd_tributo           = B.cd_tributo " +
															 "  AND B.cd_tributo           = C.cd_tributo " +
															 "  AND A.cd_documento_entrada = " +cdDocumentoEntrada+
															 "  AND A.cd_produto_servico   = "+cdProdutoServico).executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result insert(DocumentoEntradaItem item) {
		return insert(item, 0, false, null, null);
	}
	
	public static Result insert(DocumentoEntradaItem item, Connection connection) {
		return insert(item, 0, false, null, connection);
	}

	public static Result insert(DocumentoEntradaItem item, int cdLocalArmazenamento, Connection connection) {
		return insert(item, cdLocalArmazenamento, false, null, connection);
	}
	
	public static Result insert(DocumentoEntradaItem item, int cdLocalArmazenamento, boolean registerTributacao) {
		return insert(item, cdLocalArmazenamento, registerTributacao, null, null);
	}
	
	public static Result insert(DocumentoEntradaItem item, int cdLocalArmazenamento, boolean registerTributacao, Connection connection) {
		return insert(item, cdLocalArmazenamento, registerTributacao, null, connection);
	}
	
	
	public static Result insert(DocumentoEntradaItem item, int cdLocalArmazenamento, boolean registerTributacao, ArrayList<EntradaItemAliquota> entradasTributo) {
		return insert(item, cdLocalArmazenamento, registerTributacao, entradasTributo, null);
	}

	public static Result insert(DocumentoEntradaItem item, int cdLocalArmazenamento, boolean registerTributacao, ArrayList<EntradaItemAliquota> entradasTributo, Connection connection) {
		return insert(item, cdLocalArmazenamento, registerTributacao, null /*entradaLocalItens*/, null /*produtosReferencia*/,entradasTributo,connection);
	}
	
	public static Result insert(DocumentoEntradaItem item, int cdLocalArmazenamento, boolean registerTributacao, ArrayList<EntradaLocalItem> entradaLocalItens, ArrayList<ProdutoReferencia> produtosReferencia, ArrayList<EntradaItemAliquota> entradasTributo)	{
		return insert(item, cdLocalArmazenamento, registerTributacao, entradaLocalItens, produtosReferencia, entradasTributo, null);
	}
	public static Result insert(DocumentoEntradaItem item, int cdLocalArmazenamento, boolean registerTributacao, ArrayList<EntradaLocalItem> entradaLocalItens, ArrayList<ProdutoReferencia> produtosReferencia, ArrayList<EntradaItemAliquota> entradasTributo, Connection connection)	{
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM alm_balanco_doc_entrada " +
																  "WHERE cd_documento_entrada = "+item.getCdDocumentoEntrada());
			if (pstmt.executeQuery().next()) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Documento de entrada já faz parte de um balanço!");
			}

			if (ProdutoServicoEmpresaDAO.get(item.getCdEmpresa(), item.getCdProdutoServico(), connection) == null) {
				if (ProdutoServicoEmpresaDAO.insert(new ProdutoServicoEmpresa(item.getCdEmpresa(), item.getCdProdutoServico(),item.getCdUnidadeMedida(),
																			  "" /*idReduzido*/,0 /*vlPrecoMedio*/,0 /*vlCustoMedio*/,0 /*vlUltimoCusto*/,
																			  0 /*qtIdeal*/,0 /*qtMinima*/,0 /*qtMaxima*/,0 /*qtDiasEstoque*/,
																			  0 /*qtPrecisaoCusto*/,0 /*qtPrecisaoUnidade*/,0 /*qtDiasGarantia*/,
																			  0 /*tpReabastecimento*/,0 /*tpControleEstoque*/,0 /*tpTransporte*/,
																			  ProdutoServicoEmpresaServices.ST_ATIVO /*stProdutoEmpresa*/,
																			  null /*dtDesativacao*/,"" /*nrOrdem*/,0 /*lgEstoqueNegativo*/), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao salvar dados do produto na empresa!");
				}
			}

			item.setCdItem(DocumentoEntradaItemDAO.insert(item, connection));
			if (item.getCdItem() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao salvar dados do item na entrada!");
			}
//			// calculo de tributos */
//			ResultSetMap rsmTributos = null;
//			if (registerTributacao) {
//				rsmTributos = EntradaItemAliquotaServices.calcTributos(item, connection);
//				if (rsmTributos == null) {
//					if (isConnectionNull)
//						Conexao.rollback(connection);
//					return new Result(-1, "Falha ao incluir tributos!");
//				}
//			}
			

			//Insere a tributação do item de entrada
			for(int i = 0; entradasTributo != null && i < entradasTributo.size(); i++){
				int cdTributo    = entradasTributo.get(i).getCdTributo();
				if(cdTributo > 0){
					entradasTributo.get(i).setCdItem(item.getCdItem());
					
					float prAliquota = entradasTributo.get(i).getPrAliquota();
					int tpOperacao   = TributoAliquotaServices.OP_COMPRA;
					
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_tributo", "" + cdTributo, ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("pr_aliquota", "" + prAliquota, ItemComparator.EQUAL, Types.FLOAT));
					criterios.add(new ItemComparator("tp_operacao", "" + tpOperacao, ItemComparator.EQUAL, Types.INTEGER));
					
					ResultSetMap rsmTribAliq = TributoAliquotaDAO.find(criterios);
					
					if(rsmTribAliq.next()){
						entradasTributo.get(i).setCdTributoAliquota(rsmTribAliq.getInt("cd_tributo_aliquota"));
					}
					else{
						SituacaoTributaria sitTrib = SituacaoTributariaDAO.get(cdTributo, entradasTributo.get(i).getCdSituacaoTributaria());
						TributoAliquota tribAliquota = new TributoAliquota(0, cdTributo, (sitTrib.getLgSubstituicao()==0 ? prAliquota : 0), 0, (sitTrib.getLgSubstituicao() == 1 ? TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA : sitTrib.getLgMotivoIsencao() == 1 ? TributoAliquotaServices.ST_ISENTO : TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL), Util.getDataAtual(), null, 0, 1, 0, 0, tpOperacao, 0, sitTrib.getCdSituacaoTributaria(), 0, 0, 0, (sitTrib.getLgSubstituicao()==1 ? prAliquota : 0), 0, 0, 0);
						int cdTributoAliquota = TributoAliquotaDAO.insert(tribAliquota, null);
						if(cdTributoAliquota<=0){
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao inserir Tributo Aliquota");
						}
						entradasTributo.get(i).setCdTributoAliquota(cdTributoAliquota);
					}
					
					int ret = EntradaItemAliquotaDAO.insert(entradasTributo.get(i), connection);
					if (ret <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao inserir Entrada Item Aliquota");
					}
				}
			}
			
			// Insere Produto Referencia e entrada Local Item*/
			if (produtosReferencia != null && entradaLocalItens != null) {
				/* insere o item em cada entrada local item */
				for (int i = 0; i < produtosReferencia.size(); i++) {
					int cod = getProdutoReferencia(produtosReferencia.get(i), connection) != null ? getProdutoReferencia(produtosReferencia.get(i), connection).getCdReferencia() : 0;
					produtosReferencia.get(i).setCdReferencia(cod > 0 ? cod : ProdutoReferenciaDAO.insert(produtosReferencia.get(i), connection));
					if (produtosReferencia.get(i).getCdReferencia() > 0) {
						EntradaLocalItem entradaLocalItem = entradaLocalItens.get(i);
						entradaLocalItem.setCdItem(item.getCdItem());
						entradaLocalItem.setCdReferencia(produtosReferencia.get(i).getCdReferencia());
						
						int ret = EntradaLocalItemDAO.insert(entradaLocalItem, connection);
						if (ret <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar salvar entrada local item!");
						}
					} 
					else{
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao tentar salvar produto referencia!");
					}
				}
			} 
			else {
				// registra entrada no estoque
				if (cdLocalArmazenamento > 0) {
					int cdProdutoServico   = item.getCdProdutoServico();
					float qtEntrada        = item.getQtEntrada();
					int cdEmpresa 		   = item.getCdEmpresa();
					int cdDocumentoEntrada = item.getCdDocumentoEntrada();
					DocumentoEntrada documento = DocumentoEntradaDAO.get(cdDocumentoEntrada, connection);
					if (documento == null) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Documento de entrada inválido! [cdDocumentoEtnrada:"+cdDocumentoEntrada+"]");
					}
					EntradaLocalItem itemArmazenamento = new EntradaLocalItem(cdProdutoServico, cdDocumentoEntrada, cdEmpresa, cdLocalArmazenamento,
									                                          documento.getTpMovimentoEstoque()==DocumentoEntradaServices.MOV_ESTOQUE_NAO_CONSIGNADO || documento.getTpMovimentoEstoque()==DocumentoEntradaServices.MOV_AMBOS_TIPO_ESTOQUE ? qtEntrada : 0,
									                                          documento.getTpMovimentoEstoque() == DocumentoEntradaServices.MOV_ESTOQUE_CONSIGNADO ? qtEntrada : 0, 
									                                          0 /*cdLocalArmazenamentoItem*/, item.getCdItem());
					if (EntradaLocalItemServices.insert(itemArmazenamento, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Documento de entrada inválido! [cdDocumentoEtnrada:"+cdDocumentoEntrada+"]");
					}
				}
			}

			/*
			 * Informações de Importação
			 */
			DocumentoEntrada docEntrada = DocumentoEntradaDAO.get(item.getCdDocumentoEntrada(), connection);
			int cdNatOperacao = (item.getCdNaturezaOperacao() > 0 ? item.getCdNaturezaOperacao() : docEntrada.getCdNaturezaOperacao());
			if(cdNatOperacao > 0 && cdNatOperacao == ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_ENTRADA_IMPORTACAO_DEFAULT", 0)){
				Result resultado = recalcImpostosImportacao(item, connection);
				if(resultado.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return resultado;
				}
			}
			
			if (isConnectionNull)
				connection.commit();
			
			Result result = new Result(item.getCdItem());
//			result.addObject("rsmTributos", rsmTributos);
			result.addObject("cdItem", item.getCdItem());
			result.setMessage("Documento salvo com sucesso.");
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar incluir item no documento de entrada!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	private static Result recalcImpostosImportacao(DocumentoEntradaItem item, Connection connection) {
		//Busca o produto referente ao item
		ProdutoServico produto = ProdutoServicoDAO.get(item.getCdProdutoServico(), connection);
		//Busca o documento de entrada referente ao item
		DocumentoEntrada documentoEntrada = DocumentoEntradaDAO.get(item.getCdDocumentoEntrada(), connection);
		//Busca se existe uma adição de entrada referente a esse item
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("cd_ncm", "" + produto.getCdNcm(), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("cd_documento_entrada", "" + item.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsmAdicao = EntradaAdicaoDAO.find(criterios, connection);
		if(rsmAdicao.next()){
			EntradaAdicao adicao = EntradaAdicaoDAO.get(rsmAdicao.getInt("cd_entrada_adicao"), rsmAdicao.getInt("cd_entrada_declaracao_importacao"), rsmAdicao.getInt("cd_documento_entrada"), connection);
			
			//Conta quantos itens ja existe nessa adição, para que quando todos os itens já tiverem sido acrescentados, o sistema gerar as bases de cálculo para cada item
			int countItensAdicao = 0;
			//Faz a soma do valor total dos itens desse ncm
			float vlTotalItemNcm = 0;
			ResultSetMap rsmItensDocEntrada = DocumentoEntradaServices.getAllItens(documentoEntrada.getCdDocumentoEntrada(), connection);
			//Reune todos os itens que estão nesse NCM
			ResultSetMap rsmAllItens = new ResultSetMap();
			while(rsmItensDocEntrada.next()){
				if(rsmItensDocEntrada.getInt("cd_ncm") == produto.getCdNcm()){
					rsmAllItens.addRegister(rsmItensDocEntrada.getRegister());
					countItensAdicao++;
					vlTotalItemNcm += (rsmItensDocEntrada.getFloat("vl_unitario") * rsmItensDocEntrada.getFloat("qt_entrada") + rsmItensDocEntrada.getFloat("vl_acrescimo") - rsmItensDocEntrada.getFloat("vl_desconto"));
				}
			}
			rsmItensDocEntrada.beforeFirst();
			rsmAllItens.beforeFirst();
			
			//Calcula o valor total da adição a partir dos itens cadastrados para determinado ncm
			adicao.setVlTotal(vlTotalItemNcm);
			if(EntradaAdicaoDAO.update(adicao, connection) < 0){
				return new Result(-1, "Erro ao atualizar adição");
			}
			
			//Gera as bases de calculo, aliquotas e tributo dos itens com base no registro da adição, vendo a média com base na participação do total
			if(rsmAdicao.getFloat("qt_itens") == countItensAdicao){
				while(rsmAllItens.next()){
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_item", "" + rsmAllItens.getString("cd_item"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_documento_entrada", "" + rsmAllItens.getString("cd_documento_entrada"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_produto_servico", "" + rsmAllItens.getString("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_empresa", "" + rsmAllItens.getString("cd_empresa"), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmDocEntradaItem = DocumentoEntradaItemDAO.find(criterios, connection);
					rsmDocEntradaItem.next();
					DocumentoEntradaItem docItemNcm = DocumentoEntradaItemDAO.get(rsmDocEntradaItem.getInt("cd_documento_entrada"), rsmDocEntradaItem.getInt("cd_produto_servico"), rsmDocEntradaItem.getInt("cd_empresa"), rsmDocEntradaItem.getInt("cd_item"), connection);
					float vlBaseCalculoIPI = rsmAdicao.getFloat("vl_base_calculo_ipi");
					if(vlBaseCalculoIPI > 0){
						int cdTributo    = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_IPI", 0);
						int cdSituacaoTributaria = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_TRIBUTARIA_IMPORTACAO_IPI", 0);
						
						//Exclui uma entrada docItemNcm aliquota que já exista para esse docItemNcm
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_tributo", "" + cdTributo, ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_item", "" + docItemNcm.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_documento_entrada", "" + docItemNcm.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_produto_servico", "" + docItemNcm.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_empresa", "" + docItemNcm.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmEntradaItemAliquota = EntradaItemAliquotaDAO.find(criterios, connection);
						if(rsmEntradaItemAliquota.next()){
							if(EntradaItemAliquotaDAO.delete(rsmEntradaItemAliquota.getInt("cd_produto_servico"), rsmEntradaItemAliquota.getInt("cd_documento_entrada"), rsmEntradaItemAliquota.getInt("cd_empresa"), rsmEntradaItemAliquota.getInt("cd_tributo_aliquota"), rsmEntradaItemAliquota.getInt("cd_tributo"), rsmEntradaItemAliquota.getInt("cd_item"), connection) < 0){
								return new Result(-1, "Erro ao incluir entrada item aliquota!");
							}
						}
						
						EntradaItemAliquota entradaItemAliquota = new EntradaItemAliquota();
						
						entradaItemAliquota.setCdDocumentoEntrada(docItemNcm.getCdDocumentoEntrada());
						entradaItemAliquota.setCdEmpresa(docItemNcm.getCdEmpresa());
						entradaItemAliquota.setCdItem(docItemNcm.getCdItem());
						entradaItemAliquota.setCdProdutoServico(docItemNcm.getCdProdutoServico());
						entradaItemAliquota.setCdSituacaoTributaria(cdSituacaoTributaria);
						entradaItemAliquota.setCdTributo(cdTributo);
						float prAliquota = rsmAdicao.getFloat("pr_aliquota_ipi");
						entradaItemAliquota.setPrAliquota(prAliquota);
						if(cdTributo > 0){
							int tpOperacao   = TributoAliquotaServices.OP_COMPRA;
							
							criterios = new ArrayList<ItemComparator>();
							criterios.add(new ItemComparator("cd_tributo", "" + cdTributo, ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("pr_aliquota", "" + prAliquota, ItemComparator.EQUAL, Types.FLOAT));
							criterios.add(new ItemComparator("tp_operacao", "" + tpOperacao, ItemComparator.EQUAL, Types.INTEGER));
							
							ResultSetMap rsmTribAliq = TributoAliquotaDAO.find(criterios);
							
							if(rsmTribAliq.next()){
								entradaItemAliquota.setCdTributoAliquota(rsmTribAliq.getInt("cd_tributo_aliquota"));
							}
							else{
								SituacaoTributaria sitTrib = SituacaoTributariaDAO.get(cdTributo, entradaItemAliquota.getCdSituacaoTributaria());
								TributoAliquota tribAliquota = new TributoAliquota(0, cdTributo, (sitTrib.getLgSubstituicao()==0 ? prAliquota : 0), 0, (sitTrib.getLgSubstituicao() == 1 ? TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA : sitTrib.getLgMotivoIsencao() == 1 ? TributoAliquotaServices.ST_ISENTO : TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL), Util.getDataAtual(), null, 0, 1, 0, 0, tpOperacao, 0, sitTrib.getCdSituacaoTributaria(), 0, 0, 0, (sitTrib.getLgSubstituicao()==1 ? prAliquota : 0), 0, 0, 0);
								int cdTributoAliquota = TributoAliquotaDAO.insert(tribAliquota, connection);
								if(cdTributoAliquota<=0){
									return new Result(-1, "Erro ao inserir Tributo Aliquota");
								}
								entradaItemAliquota.setCdTributoAliquota(cdTributoAliquota);
							}
							
							//Calcula a particição do docItemNcm na base de calculo em razão da sua participação no total do documento por ncm
							float vlBaseCalculo = rsmAdicao.getFloat("vl_base_calculo_ipi") * (docItemNcm.getVlUnitario() * docItemNcm.getQtEntrada() + docItemNcm.getVlAcrescimo() - docItemNcm.getVlDesconto()) / vlTotalItemNcm;
							entradaItemAliquota.setVlBaseCalculo(vlBaseCalculo);
							
							int ret = EntradaItemAliquotaDAO.insert(entradaItemAliquota, connection);
							if (ret <= 0) {
								return new Result(-1, "Erro ao inserir Entrada Item Aliquota");
							}
						}
					}
					else{
						
						int tpOperacao   = TributoAliquotaServices.OP_COMPRA;
						int cdTributo    = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_IPI", 0);
						int cdSituacaoTributaria = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_TRIBUTARIA_IMPORTACAO_IPI", 0);
						
						//Exclui uma entrada docItemNcm aliquota que já exista para esse docItemNcm
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_tributo", "" + cdTributo, ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_item", "" + docItemNcm.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_documento_entrada", "" + docItemNcm.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_produto_servico", "" + docItemNcm.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_empresa", "" + docItemNcm.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmEntradaItemAliquota = EntradaItemAliquotaDAO.find(criterios, connection);
						if(rsmEntradaItemAliquota.next()){
							if(EntradaItemAliquotaDAO.delete(rsmEntradaItemAliquota.getInt("cd_produto_servico"), rsmEntradaItemAliquota.getInt("cd_documento_entrada"), rsmEntradaItemAliquota.getInt("cd_empresa"), rsmEntradaItemAliquota.getInt("cd_tributo_aliquota"), rsmEntradaItemAliquota.getInt("cd_tributo"), rsmEntradaItemAliquota.getInt("cd_item"), connection) < 0){
								return new Result(-1, "Erro ao incluir entrada item aliquota!");
							}
						}
						
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_tributo", "" + cdTributo, ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("pr_aliquota", "" + 0, ItemComparator.EQUAL, Types.FLOAT));
						criterios.add(new ItemComparator("tp_operacao", "" + tpOperacao, ItemComparator.EQUAL, Types.INTEGER));
						
						ResultSetMap rsmTribAliq = TributoAliquotaDAO.find(criterios);
						
						int cdTributoAliquota = 0;
						
						if(rsmTribAliq.next()){
							cdTributoAliquota = rsmTribAliq.getInt("cd_tributo_aliquota");
						}
						else{
							SituacaoTributaria sitTrib = SituacaoTributariaDAO.get(cdTributo, cdSituacaoTributaria);
							TributoAliquota tribAliquota = new TributoAliquota(0, cdTributo, 0, 0, (sitTrib.getLgSubstituicao() == 1 ? TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA : sitTrib.getLgMotivoIsencao() == 1 ? TributoAliquotaServices.ST_ISENTO : TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL), Util.getDataAtual(), null, 0, 1, 0, 0, tpOperacao, 0, sitTrib.getCdSituacaoTributaria(), 0, 0, 0, 0, 0, 0, 0);
							cdTributoAliquota = TributoAliquotaDAO.insert(tribAliquota, connection);
							if(cdTributoAliquota<=0){
								return new Result(-1, "Erro ao inserir Tributo Aliquota");
							}
						}
						
						int ret = EntradaItemAliquotaDAO.insert(new EntradaItemAliquota(docItemNcm.getCdProdutoServico(), docItemNcm.getCdDocumentoEntrada(), docItemNcm.getCdEmpresa(), cdTributoAliquota, cdTributo, docItemNcm.getCdItem(), 0, 0, cdSituacaoTributaria), connection);
						if (ret <= 0) {
							return new Result(-1, "Erro ao inserir Entrada Item Aliquota");
						}
					}
					float vlBaseCalculoPIS = rsmAdicao.getFloat("vl_base_calculo_pis");
					
					if(vlBaseCalculoPIS > 0){
						int cdTributo    = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0);
						int cdSituacaoTributaria = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_TRIBUTARIA_IMPORTACAO_PIS", 0);
						
						//Exclui uma entrada docItemNcm aliquota que já exista para esse docItemNcm
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_tributo", "" + cdTributo, ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_item", "" + docItemNcm.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_documento_entrada", "" + docItemNcm.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_produto_servico", "" + docItemNcm.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_empresa", "" + docItemNcm.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmEntradaItemAliquota = EntradaItemAliquotaDAO.find(criterios, connection);
						if(rsmEntradaItemAliquota.next()){
							if(EntradaItemAliquotaDAO.delete(rsmEntradaItemAliquota.getInt("cd_produto_servico"), rsmEntradaItemAliquota.getInt("cd_documento_entrada"), rsmEntradaItemAliquota.getInt("cd_empresa"), rsmEntradaItemAliquota.getInt("cd_tributo_aliquota"), rsmEntradaItemAliquota.getInt("cd_tributo"), rsmEntradaItemAliquota.getInt("cd_item"), connection) < 0){
								return new Result(-1, "Erro ao incluir entrada item aliquota!");
							}
						}
						
						EntradaItemAliquota entradaItemAliquota = new EntradaItemAliquota();
						
						entradaItemAliquota.setCdDocumentoEntrada(docItemNcm.getCdDocumentoEntrada());
						entradaItemAliquota.setCdEmpresa(docItemNcm.getCdEmpresa());
						entradaItemAliquota.setCdItem(docItemNcm.getCdItem());
						entradaItemAliquota.setCdProdutoServico(docItemNcm.getCdProdutoServico());
						entradaItemAliquota.setCdSituacaoTributaria(cdSituacaoTributaria);
						entradaItemAliquota.setCdTributo(cdTributo);
						float prAliquota = rsmAdicao.getFloat("pr_aliquota_pis");
						entradaItemAliquota.setPrAliquota(prAliquota);
						if(cdTributo > 0){
							int tpOperacao   = TributoAliquotaServices.OP_COMPRA;
							
							criterios = new ArrayList<ItemComparator>();
							criterios.add(new ItemComparator("cd_tributo", "" + cdTributo, ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("pr_aliquota", "" + prAliquota, ItemComparator.EQUAL, Types.FLOAT));
							criterios.add(new ItemComparator("tp_operacao", "" + tpOperacao, ItemComparator.EQUAL, Types.INTEGER));
							
							ResultSetMap rsmTribAliq = TributoAliquotaDAO.find(criterios);
							if(rsmTribAliq.next()){
								entradaItemAliquota.setCdTributoAliquota(rsmTribAliq.getInt("cd_tributo_aliquota"));
							}
							else{
								SituacaoTributaria sitTrib = SituacaoTributariaDAO.get(cdTributo, entradaItemAliquota.getCdSituacaoTributaria());
								TributoAliquota tribAliquota = new TributoAliquota(0, cdTributo, (sitTrib.getLgSubstituicao()==0 ? prAliquota : 0), 0, (sitTrib.getLgSubstituicao() == 1 ? TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA : sitTrib.getLgMotivoIsencao() == 1 ? TributoAliquotaServices.ST_ISENTO : TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL), Util.getDataAtual(), null, 0, 1, 0, 0, tpOperacao, 0, sitTrib.getCdSituacaoTributaria(), 0, 0, 0, (sitTrib.getLgSubstituicao()==1 ? prAliquota : 0), 0, 0, 0);
								int cdTributoAliquota = TributoAliquotaDAO.insert(tribAliquota, connection);
								if(cdTributoAliquota<=0){
									return new Result(-1, "Erro ao inserir Tributo Aliquota");
								}
								entradaItemAliquota.setCdTributoAliquota(cdTributoAliquota);
							}
							//Calcula a particição do docItemNcm na base de calculo em razão da sua participação no total do documento por ncm
							float vlBaseCalculo = rsmAdicao.getFloat("vl_base_calculo_pis") * (docItemNcm.getVlUnitario() * docItemNcm.getQtEntrada() + docItemNcm.getVlAcrescimo() - docItemNcm.getVlDesconto()) / vlTotalItemNcm;
							entradaItemAliquota.setVlBaseCalculo(vlBaseCalculo);
							int ret = EntradaItemAliquotaDAO.insert(entradaItemAliquota, connection);
							if (ret <= 0) {
								return new Result(-1, "Erro ao inserir Entrada Item Aliquota");
							}
						}
					}
					else{
						
						int tpOperacao   = TributoAliquotaServices.OP_COMPRA;
						int cdTributo    = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0);
						int cdSituacaoTributaria = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_TRIBUTARIA_IMPORTACAO_PIS", 0);
						
						//Exclui uma entrada docItemNcm aliquota que já exista para esse docItemNcm
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_tributo", "" + cdTributo, ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_item", "" + docItemNcm.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_documento_entrada", "" + docItemNcm.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_produto_servico", "" + docItemNcm.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_empresa", "" + docItemNcm.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmEntradaItemAliquota = EntradaItemAliquotaDAO.find(criterios, connection);
						if(rsmEntradaItemAliquota.next()){
							if(EntradaItemAliquotaDAO.delete(rsmEntradaItemAliquota.getInt("cd_produto_servico"), rsmEntradaItemAliquota.getInt("cd_documento_entrada"), rsmEntradaItemAliquota.getInt("cd_empresa"), rsmEntradaItemAliquota.getInt("cd_tributo_aliquota"), rsmEntradaItemAliquota.getInt("cd_tributo"), rsmEntradaItemAliquota.getInt("cd_item"), connection) < 0){
								return new Result(-1, "Erro ao incluir entrada item aliquota!");
							}
						}
						
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_tributo", "" + cdTributo, ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("pr_aliquota", "" + 0, ItemComparator.EQUAL, Types.FLOAT));
						criterios.add(new ItemComparator("tp_operacao", "" + tpOperacao, ItemComparator.EQUAL, Types.INTEGER));
						
						ResultSetMap rsmTribAliq = TributoAliquotaDAO.find(criterios);
						
						int cdTributoAliquota = 0;
						
						if(rsmTribAliq.next()){
							cdTributoAliquota = rsmTribAliq.getInt("cd_tributo_aliquota");
						}
						else{
							SituacaoTributaria sitTrib = SituacaoTributariaDAO.get(cdTributo, cdSituacaoTributaria);
							TributoAliquota tribAliquota = new TributoAliquota(0, cdTributo, 0, 0, (sitTrib.getLgSubstituicao() == 1 ? TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA : sitTrib.getLgMotivoIsencao() == 1 ? TributoAliquotaServices.ST_ISENTO : TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL), Util.getDataAtual(), null, 0, 1, 0, 0, tpOperacao, 0, sitTrib.getCdSituacaoTributaria(), 0, 0, 0, 0, 0, 0, 0);
							cdTributoAliquota = TributoAliquotaDAO.insert(tribAliquota, connection);
							if(cdTributoAliquota<=0){
								return new Result(-1, "Erro ao inserir Tributo Aliquota");
							}
						}
						
						int ret = EntradaItemAliquotaDAO.insert(new EntradaItemAliquota(docItemNcm.getCdProdutoServico(), docItemNcm.getCdDocumentoEntrada(), docItemNcm.getCdEmpresa(), cdTributoAliquota, cdTributo, docItemNcm.getCdItem(), 0, 0, cdSituacaoTributaria), connection);
						if (ret <= 0) {
							return new Result(-1, "Erro ao inserir Entrada Item Aliquota");
						}
					}
					float vlBaseCalculoCOFINS = rsmAdicao.getFloat("vl_base_calculo_cofins");
					
					if(vlBaseCalculoCOFINS > 0){
						int cdTributo    = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0);
						int cdSituacaoTributaria = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_TRIBUTARIA_IMPORTACAO_COFINS", 0);
						//Exclui uma entrada docItemNcm aliquota que já exista para esse docItemNcm
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_tributo", "" + cdTributo, ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_item", "" + docItemNcm.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_documento_entrada", "" + docItemNcm.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_produto_servico", "" + docItemNcm.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_empresa", "" + docItemNcm.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmEntradaItemAliquota = EntradaItemAliquotaDAO.find(criterios, connection);
						if(rsmEntradaItemAliquota.next()){
							if(EntradaItemAliquotaDAO.delete(rsmEntradaItemAliquota.getInt("cd_produto_servico"), rsmEntradaItemAliquota.getInt("cd_documento_entrada"), rsmEntradaItemAliquota.getInt("cd_empresa"), rsmEntradaItemAliquota.getInt("cd_tributo_aliquota"), rsmEntradaItemAliquota.getInt("cd_tributo"), rsmEntradaItemAliquota.getInt("cd_item"), connection) < 0){
								return new Result(-1, "Erro ao incluir entrada item aliquota!");
							}
						}
						EntradaItemAliquota entradaItemAliquota = new EntradaItemAliquota();
						entradaItemAliquota.setCdDocumentoEntrada(docItemNcm.getCdDocumentoEntrada());
						entradaItemAliquota.setCdEmpresa(docItemNcm.getCdEmpresa());
						entradaItemAliquota.setCdItem(docItemNcm.getCdItem());
						entradaItemAliquota.setCdProdutoServico(docItemNcm.getCdProdutoServico());
						entradaItemAliquota.setCdSituacaoTributaria(cdSituacaoTributaria);
						entradaItemAliquota.setCdTributo(cdTributo);
						float prAliquota = rsmAdicao.getFloat("pr_aliquota_cofins");
						entradaItemAliquota.setPrAliquota(prAliquota);
						if(cdTributo > 0){
							int tpOperacao   = TributoAliquotaServices.OP_COMPRA;
							
							criterios = new ArrayList<ItemComparator>();
							criterios.add(new ItemComparator("cd_tributo", "" + cdTributo, ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("pr_aliquota", "" + prAliquota, ItemComparator.EQUAL, Types.FLOAT));
							criterios.add(new ItemComparator("tp_operacao", "" + tpOperacao, ItemComparator.EQUAL, Types.INTEGER));
							
							ResultSetMap rsmTribAliq = TributoAliquotaDAO.find(criterios);
							if(rsmTribAliq.next()){
								entradaItemAliquota.setCdTributoAliquota(rsmTribAliq.getInt("cd_tributo_aliquota"));
							}
							else{
								SituacaoTributaria sitTrib = SituacaoTributariaDAO.get(cdTributo, entradaItemAliquota.getCdSituacaoTributaria());
								TributoAliquota tribAliquota = new TributoAliquota(0, cdTributo, (sitTrib.getLgSubstituicao()==0 ? prAliquota : 0), 0, (sitTrib.getLgSubstituicao() == 1 ? TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA : sitTrib.getLgMotivoIsencao() == 1 ? TributoAliquotaServices.ST_ISENTO : TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL), Util.getDataAtual(), null, 0, 1, 0, 0, tpOperacao, 0, sitTrib.getCdSituacaoTributaria(), 0, 0, 0, (sitTrib.getLgSubstituicao()==1 ? prAliquota : 0), 0, 0, 0);
								int cdTributoAliquota = TributoAliquotaDAO.insert(tribAliquota, connection);
								if(cdTributoAliquota<=0){
									return new Result(-1, "Erro ao inserir Tributo Aliquota");
								}
								entradaItemAliquota.setCdTributoAliquota(cdTributoAliquota);
							}
							//Calcula a particição do docItemNcm na base de calculo em razão da sua participação no total do documento por ncm
							float vlBaseCalculo = rsmAdicao.getFloat("vl_base_calculo_cofins") * (docItemNcm.getVlUnitario() * docItemNcm.getQtEntrada() + docItemNcm.getVlAcrescimo() - docItemNcm.getVlDesconto()) / vlTotalItemNcm;
							entradaItemAliquota.setVlBaseCalculo(vlBaseCalculo);
							int ret = EntradaItemAliquotaDAO.insert(entradaItemAliquota, connection);
							if (ret <= 0) {
								return new Result(-1, "Erro ao inserir Entrada Item Aliquota");
							}
						}
					}
					else{
						
						int tpOperacao   = TributoAliquotaServices.OP_COMPRA;
						int cdTributo    = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0);
						int cdSituacaoTributaria = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_TRIBUTARIA_IMPORTACAO_COFINS", 0);
						
						//Exclui uma entrada docItemNcm aliquota que já exista para esse docItemNcm
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_tributo", "" + cdTributo, ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_item", "" + docItemNcm.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_documento_entrada", "" + docItemNcm.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_produto_servico", "" + docItemNcm.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_empresa", "" + docItemNcm.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmEntradaItemAliquota = EntradaItemAliquotaDAO.find(criterios, connection);
						if(rsmEntradaItemAliquota.next()){
							if(EntradaItemAliquotaDAO.delete(rsmEntradaItemAliquota.getInt("cd_produto_servico"), rsmEntradaItemAliquota.getInt("cd_documento_entrada"), rsmEntradaItemAliquota.getInt("cd_empresa"), rsmEntradaItemAliquota.getInt("cd_tributo_aliquota"), rsmEntradaItemAliquota.getInt("cd_tributo"), rsmEntradaItemAliquota.getInt("cd_item"), connection) < 0){
								return new Result(-1, "Erro ao incluir entrada item aliquota!");
							}
						}
						
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_tributo", "" + cdTributo, ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("pr_aliquota", "" + 0, ItemComparator.EQUAL, Types.FLOAT));
						criterios.add(new ItemComparator("tp_operacao", "" + tpOperacao, ItemComparator.EQUAL, Types.INTEGER));
						
						ResultSetMap rsmTribAliq = TributoAliquotaDAO.find(criterios);
						
						int cdTributoAliquota = 0;
						
						if(rsmTribAliq.next()){
							cdTributoAliquota = rsmTribAliq.getInt("cd_tributo_aliquota");
						}
						else{
							SituacaoTributaria sitTrib = SituacaoTributariaDAO.get(cdTributo, cdSituacaoTributaria);
							TributoAliquota tribAliquota = new TributoAliquota(0, cdTributo, 0, 0, (sitTrib.getLgSubstituicao() == 1 ? TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA : sitTrib.getLgMotivoIsencao() == 1 ? TributoAliquotaServices.ST_ISENTO : TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL), Util.getDataAtual(), null, 0, 1, 0, 0, tpOperacao, 0, sitTrib.getCdSituacaoTributaria(), 0, 0, 0, 0, 0, 0, 0);
							cdTributoAliquota = TributoAliquotaDAO.insert(tribAliquota, connection);
							if(cdTributoAliquota<=0){
								return new Result(-1, "Erro ao inserir Tributo Aliquota");
							}
						}
						
						int ret = EntradaItemAliquotaDAO.insert(new EntradaItemAliquota(docItemNcm.getCdProdutoServico(), docItemNcm.getCdDocumentoEntrada(), docItemNcm.getCdEmpresa(), cdTributoAliquota, cdTributo, docItemNcm.getCdItem(), 0, 0, cdSituacaoTributaria), connection);
						if (ret <= 0) {
							return new Result(-1, "Erro ao inserir Entrada Item Aliquota");
						}
					}
					float vlBaseCalculoII = rsmAdicao.getFloat("vl_base_calculo_ii");
					if(vlBaseCalculoII > 0){
						int cdTributo    = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_II", 0);
						int cdSituacaoTributaria = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_TRIBUTARIA_IMPORTACAO_II", 0);
						if(cdSituacaoTributaria <= 0){
							return new Result(-1, "Parametro de Situação tributária de Imposto de Importação não configurado!");
						}
						//Exclui uma entrada docItemNcm aliquota que já exista para esse docItemNcm
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_tributo", "" + cdTributo, ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_item", "" + docItemNcm.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_documento_entrada", "" + docItemNcm.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_produto_servico", "" + docItemNcm.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_empresa", "" + docItemNcm.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmEntradaItemAliquota = EntradaItemAliquotaDAO.find(criterios, connection);
						if(rsmEntradaItemAliquota.next()){
							if(EntradaItemAliquotaDAO.delete(rsmEntradaItemAliquota.getInt("cd_produto_servico"), rsmEntradaItemAliquota.getInt("cd_documento_entrada"), rsmEntradaItemAliquota.getInt("cd_empresa"), rsmEntradaItemAliquota.getInt("cd_tributo_aliquota"), rsmEntradaItemAliquota.getInt("cd_tributo"), rsmEntradaItemAliquota.getInt("cd_item"), connection) < 0){
								return new Result(-1, "Erro ao incluir entrada item aliquota!");
							}
						}
						
						EntradaItemAliquota entradaItemAliquota = new EntradaItemAliquota();
						
						entradaItemAliquota.setCdDocumentoEntrada(docItemNcm.getCdDocumentoEntrada());
						entradaItemAliquota.setCdEmpresa(docItemNcm.getCdEmpresa());
						entradaItemAliquota.setCdItem(docItemNcm.getCdItem());
						entradaItemAliquota.setCdProdutoServico(docItemNcm.getCdProdutoServico());
						entradaItemAliquota.setCdSituacaoTributaria(cdSituacaoTributaria);
						entradaItemAliquota.setCdTributo(cdTributo);
						float prAliquota = rsmAdicao.getFloat("pr_aliquota_ii");
						entradaItemAliquota.setPrAliquota(prAliquota);
						if(cdTributo > 0){
							
							int tpOperacao   = TributoAliquotaServices.OP_COMPRA;
							
							criterios = new ArrayList<ItemComparator>();
							criterios.add(new ItemComparator("cd_tributo", "" + cdTributo, ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("pr_aliquota", "" + prAliquota, ItemComparator.EQUAL, Types.FLOAT));
							criterios.add(new ItemComparator("tp_operacao", "" + tpOperacao, ItemComparator.EQUAL, Types.INTEGER));
							
							ResultSetMap rsmTribAliq = TributoAliquotaDAO.find(criterios);
							
							if(rsmTribAliq.next()){
								entradaItemAliquota.setCdTributoAliquota(rsmTribAliq.getInt("cd_tributo_aliquota"));
							}
							else{
								SituacaoTributaria sitTrib = SituacaoTributariaDAO.get(cdTributo, entradaItemAliquota.getCdSituacaoTributaria());
								TributoAliquota tribAliquota = new TributoAliquota(0, cdTributo, (sitTrib.getLgSubstituicao()==0 ? prAliquota : 0), 0, (sitTrib.getLgSubstituicao() == 1 ? TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA : sitTrib.getLgMotivoIsencao() == 1 ? TributoAliquotaServices.ST_ISENTO : TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL), Util.getDataAtual(), null, 0, 1, 0, 0, tpOperacao, 0, sitTrib.getCdSituacaoTributaria(), 0, 0, 0, (sitTrib.getLgSubstituicao()==1 ? prAliquota : 0), 0, 0, 0);
								int cdTributoAliquota = TributoAliquotaDAO.insert(tribAliquota, connection);
								if(cdTributoAliquota<=0){
									return new Result(-1, "Erro ao inserir Tributo Aliquota");
								}
								entradaItemAliquota.setCdTributoAliquota(cdTributoAliquota);
							}
							
							//Calcula a particição do docItemNcm na base de calculo em razão da sua participação no total do documento por ncm
							float vlBaseCalculo = rsmAdicao.getFloat("vl_base_calculo_ii") * (docItemNcm.getVlUnitario() * docItemNcm.getQtEntrada() + docItemNcm.getVlAcrescimo() - docItemNcm.getVlDesconto()) / vlTotalItemNcm;
							entradaItemAliquota.setVlBaseCalculo(vlBaseCalculo);
							
							int ret = EntradaItemAliquotaDAO.insert(entradaItemAliquota, connection);
							if (ret <= 0) {
								return new Result(-1, "Erro ao inserir Entrada Item Aliquota");
							}
						}
					}
					else{
						
						int tpOperacao   = TributoAliquotaServices.OP_COMPRA;
						int cdTributo    = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_II", 0);
						int cdSituacaoTributaria = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_TRIBUTARIA_IMPORTACAO_II", 0);
						
						//Exclui uma entrada docItemNcm aliquota que já exista para esse docItemNcm
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_tributo", "" + cdTributo, ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_item", "" + docItemNcm.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_documento_entrada", "" + docItemNcm.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_produto_servico", "" + docItemNcm.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_empresa", "" + docItemNcm.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmEntradaItemAliquota = EntradaItemAliquotaDAO.find(criterios, connection);
						if(rsmEntradaItemAliquota.next()){
							if(EntradaItemAliquotaDAO.delete(rsmEntradaItemAliquota.getInt("cd_produto_servico"), rsmEntradaItemAliquota.getInt("cd_documento_entrada"), rsmEntradaItemAliquota.getInt("cd_empresa"), rsmEntradaItemAliquota.getInt("cd_tributo_aliquota"), rsmEntradaItemAliquota.getInt("cd_tributo"), rsmEntradaItemAliquota.getInt("cd_item"), connection) < 0){
								return new Result(-1, "Erro ao incluir entrada item aliquota!");
							}
						}
						
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_tributo", "" + cdTributo, ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("pr_aliquota", "" + 0, ItemComparator.EQUAL, Types.FLOAT));
						criterios.add(new ItemComparator("tp_operacao", "" + tpOperacao, ItemComparator.EQUAL, Types.INTEGER));
						
						ResultSetMap rsmTribAliq = TributoAliquotaDAO.find(criterios);
						
						int cdTributoAliquota = 0;
						
						if(rsmTribAliq.next()){
							cdTributoAliquota = rsmTribAliq.getInt("cd_tributo_aliquota");
						}
						else{
							SituacaoTributaria sitTrib = SituacaoTributariaDAO.get(cdTributo, cdSituacaoTributaria);
							TributoAliquota tribAliquota = new TributoAliquota(0, cdTributo, 0, 0, (sitTrib.getLgSubstituicao() == 1 ? TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA : sitTrib.getLgMotivoIsencao() == 1 ? TributoAliquotaServices.ST_ISENTO : TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL), Util.getDataAtual(), null, 0, 1, 0, 0, tpOperacao, 0, sitTrib.getCdSituacaoTributaria(), 0, 0, 0, 0, 0, 0, 0);
							cdTributoAliquota = TributoAliquotaDAO.insert(tribAliquota, connection);
							if(cdTributoAliquota<=0){
								return new Result(-1, "Erro ao inserir Tributo Aliquota");
							}
						}
						
						int ret = EntradaItemAliquotaDAO.insert(new EntradaItemAliquota(docItemNcm.getCdProdutoServico(), docItemNcm.getCdDocumentoEntrada(), docItemNcm.getCdEmpresa(), cdTributoAliquota, cdTributo, docItemNcm.getCdItem(), 0, 0, cdSituacaoTributaria), connection);
						if (ret <= 0) {
							return new Result(-1, "Erro ao inserir Entrada Item Aliquota");
						}
					}	
				}
			}
		}
		else{
			//Se a nota for de importação, é obrigatório que todos os itens sejam vinculados a uma adição
			if(documentoEntrada.getCdNaturezaOperacao() == ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_ENTRADA_IMPORTACAO_DEFAULT", 0)){
				return new Result(-1, "Item de importação sem adição referente. Produto: "+produto.getNmProdutoServico()+" Ref.:"+produto.getNrReferencia()+"!");
			}
		}
		return new Result(1);
	}

	public static ResultSetMap getAllLocaisArmazenamento(int cdDocumentoEntrada, int cdProdutoServico) {
		return getAllLocaisArmazenamento(cdDocumentoEntrada, cdProdutoServico, 0,  null);
	}

	public static ResultSetMap getAllLocaisArmazenamento(int cdDocumentoEntrada, int cdProdutoServico, int cdEmpresa) {
		return getAllLocaisArmazenamento(cdDocumentoEntrada, cdProdutoServico, cdEmpresa,  null);
	}

	public static ResultSetMap getAllLocaisArmazenamento(int cdDocumentoEntrada, int cdProdutoServico, int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.cd_documento_entrada, A.cd_produto_servico, A.cd_empresa, A.cd_local_armazenamento, A.qt_entrada, " +
											 "       A.qt_entrada_consignada, B.nm_local_armazenamento, B.id_local_armazenamento, B.cd_nivel_local, B.cd_setor, " +
											 "       C.nm_nivel_local, D.nm_setor, A.cd_entrada_local_item, E.cd_referencia " +
											 "FROM alm_entrada_local_item A " +
											 "JOIN alm_local_armazenamento B ON (A.cd_local_armazenamento = B.cd_local_armazenamento) " +
											 "LEFT OUTER JOIN alm_nivel_local C ON (B.cd_nivel_local = C.cd_nivel_local) " +
											 "LEFT OUTER JOIN grl_setor D ON (B.cd_setor = D.cd_setor) " +
											 "LEFT OUTER JOIN alm_produto_referencia E ON (A.cd_produto_servico = E.cd_produto_servico " +
											 "											   AND A.cd_empresa = E.cd_empresa AND A.cd_referencia = E.cd_referencia) " +
											 "WHERE A.cd_documento_entrada = " +cdDocumentoEntrada+
											 "  AND A.cd_produto_servico   = " +cdProdutoServico+
											 (cdEmpresa>0 ? "  AND A.cd_empresa = "+cdEmpresa : ""));
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllItemReferenciaLocalArmazenamento(int cdDocumentoEntrada, int cdProdutoServico, int cdEmpresa, int cdLocalArmazenamento, int cdItem) {
		return getAllItemReferenciaLocalArmazenamento(cdDocumentoEntrada, cdProdutoServico, cdEmpresa, cdLocalArmazenamento, cdItem, null);
	}
	
	public static ResultSetMap getAllItemReferenciaLocalArmazenamento(int cdDocumentoEntrada, int cdProdutoServico, int cdEmpresa, int cdLocalArmazenamento, int cdItem, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.cd_documento_entrada, A.cd_produto_servico, A.cd_empresa, A.cd_local_armazenamento, A.qt_entrada," +
												"  A.qt_entrada_consignada, B.cd_local_armazenamento,"+
												"  B.cd_referencia, B.nm_referencia, B.dt_validade "+
												"FROM alm_entrada_local_item A "+
												"LEFT OUTER JOIN alm_produto_referencia B ON (A.cd_referencia = B.cd_referencia)"+
												"WHERE A.cd_documento_entrada = "+cdDocumentoEntrada+
												"    AND A.cd_produto_servico   = "+cdProdutoServico+
												"    AND A.cd_empresa = "+cdEmpresa+
												"    AND A.cd_local_armazenamento = "+cdLocalArmazenamento+
												"    AND A.cd_item = "+cdItem+
												"    AND A.cd_empresa = B.cd_empresa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result update(DocumentoEntradaItem item, int cdLocalArmazenamento) {
		return update(item, cdLocalArmazenamento, false, (Connection)null);
	}

	public static Result update(DocumentoEntradaItem item, int cdLocalArmazenamento, boolean updateTributacao) {
		return update(item, cdLocalArmazenamento, updateTributacao, (Connection)null);
	}

	public static Result update(DocumentoEntradaItem item, int cdLocalArmazenamento, boolean updateTributacao, Connection connection) {
		return update(item, cdLocalArmazenamento, updateTributacao, null/*entradaLocalItens*/, null/*produtosReferencia*/, null, null);
	}
	
	public static Result update(DocumentoEntradaItem item, int cdLocalArmazenamento, boolean updateTributacao, ArrayList<EntradaItemAliquota> entradasTributo) {
		return update(item, cdLocalArmazenamento, updateTributacao, entradasTributo, null);
	}

	public static Result update(DocumentoEntradaItem item, int cdLocalArmazenamento, boolean updateTributacao, ArrayList<EntradaItemAliquota> entradasTributo, Connection connection) {
		return update(item, cdLocalArmazenamento, updateTributacao, null/*entradaLocalItens*/, null/*produtosReferencia*/, entradasTributo, null);
	}
	
	public static Result update(DocumentoEntradaItem item, int cdLocalArmazenamento, boolean updateTributacao, ArrayList<EntradaLocalItem> entradaLocalItens, ArrayList<ProdutoReferencia> produtosReferencia) {
		return update(item, cdLocalArmazenamento, updateTributacao, entradaLocalItens, produtosReferencia, null, null);
	}
	public static Result update(DocumentoEntradaItem item, int cdLocalArmazenamento, boolean updateTributacao, ArrayList<EntradaLocalItem> entradaLocalItens, ArrayList<ProdutoReferencia> produtosReferencia, Connection connection) {
		return update(item, cdLocalArmazenamento, updateTributacao, entradaLocalItens, produtosReferencia, null, null);
	}
	
	public static Result update(DocumentoEntradaItem item, int cdLocalArmazenamento, boolean updateTributacao, ArrayList<EntradaLocalItem> entradaLocalItens, ArrayList<ProdutoReferencia> produtosReferencia, ArrayList<EntradaItemAliquota> entradasTributo) {
		return update(item, cdLocalArmazenamento, updateTributacao, entradaLocalItens, produtosReferencia, entradasTributo, null);
	}
	public static Result update(DocumentoEntradaItem item, int cdLocalArmazenamento, boolean updateTributacao, ArrayList<EntradaLocalItem> entradaLocalItens, ArrayList<ProdutoReferencia> produtosReferencia, ArrayList<EntradaItemAliquota> entradasTributo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM alm_balanco_doc_entrada " +
					"WHERE cd_documento_entrada = ?");
			pstmt.setInt(1, item.getCdDocumentoEntrada());
			if (pstmt.executeQuery().next()) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Essa entrada já fez parte de um balanço não é mais possível altera-la!");
			}
			int cdProdutoServico = item.getCdProdutoServico();
			int cdEmpresa = item.getCdEmpresa();
			int cdDocumentoEntrada = item.getCdDocumentoEntrada();
			// remove registros de entrada anteriores à atualizacao do item
			if (cdLocalArmazenamento != 0) {
				pstmt = connection.prepareStatement("DELETE FROM alm_entrada_local_item " +
						"WHERE cd_documento_entrada = ? " +
						"  AND cd_empresa = ? " +
						"  AND cd_produto_servico = ?");
				pstmt.setInt(1, cdDocumentoEntrada);
				pstmt.setInt(2, cdEmpresa);
				pstmt.setInt(3, cdProdutoServico);
				pstmt.execute();
			}
			// atualiza item
			int cdRetorno = DocumentoEntradaItemDAO.update(item, connection);
			if (cdRetorno <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao tentar atualizar dados do item na entrada!");
			}
//			// caluclo de tributos
//			ResultSetMap rsmTributos = null;
//			if (updateTributacao) {
//				rsmTributos = EntradaItemAliquotaServices.calcTributos(item, connection);
//				if (rsmTributos == null) {
//					if (isConnectionNull)
//						Conexao.rollback(connection);
//					return new Result(-1, "Erro ao calcular tributos!");
//				}
//			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_item", "" + item.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_produto_servico", "" + item.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_empresa", "" + item.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_documento_entrada", "" + item.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsmEntradaItemAliquota = EntradaItemAliquotaDAO.find(criterios, connection);
			while(rsmEntradaItemAliquota.next()){
				if(EntradaItemAliquotaDAO.delete(rsmEntradaItemAliquota.getInt("cd_produto_servico"), rsmEntradaItemAliquota.getInt("cd_documento_entrada"), rsmEntradaItemAliquota.getInt("cd_empresa"), rsmEntradaItemAliquota.getInt("cd_tributo_aliquota"), rsmEntradaItemAliquota.getInt("cd_tributo"), rsmEntradaItemAliquota.getInt("cd_item"), connection) < 0){
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao deletar Entrada Item Aliquota");
				}
			}
			//Insere a tributação do item de entrada
			for(int i = 0; entradasTributo != null && i < entradasTributo.size(); i++){
				int cdTributo    = entradasTributo.get(i).getCdTributo();
				if(cdTributo > 0){
					entradasTributo.get(i).setCdItem(item.getCdItem());
					
					float prAliquota = entradasTributo.get(i).getPrAliquota();
					int tpOperacao   = TributoAliquotaServices.OP_COMPRA;
					
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_tributo", "" + cdTributo, ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("pr_aliquota", "" + prAliquota, ItemComparator.EQUAL, Types.FLOAT));
					criterios.add(new ItemComparator("tp_operacao", "" + tpOperacao, ItemComparator.EQUAL, Types.INTEGER));
					
					ResultSetMap rsmTribAliq = TributoAliquotaDAO.find(criterios, connection);
					
					if(rsmTribAliq.next()){
						entradasTributo.get(i).setCdTributoAliquota(rsmTribAliq.getInt("cd_tributo_aliquota"));
					}
					else{
						SituacaoTributaria sitTrib = SituacaoTributariaDAO.get(cdTributo, entradasTributo.get(i).getCdSituacaoTributaria());
						TributoAliquota tribAliquota = new TributoAliquota(0, cdTributo, (sitTrib.getLgSubstituicao()==0 ? prAliquota : 0), 0, (sitTrib.getLgSubstituicao() == 1 ? TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA : sitTrib.getLgMotivoIsencao() == 1 ? TributoAliquotaServices.ST_ISENTO : TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL), Util.getDataAtual(), null, 0, 1, 0, 0, tpOperacao, 0, sitTrib.getCdSituacaoTributaria(), 0, 0, 0, (sitTrib.getLgSubstituicao()==1 ? prAliquota : 0), 0, 0, 0);
						int cdTributoAliquota = TributoAliquotaDAO.insert(tribAliquota, connection);
						if(cdTributoAliquota<=0){
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao inserir Tributo Aliquota");
						}
						entradasTributo.get(i).setCdTributoAliquota(cdTributoAliquota);
					}
					
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_item", "" + item.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_produto_servico", "" + item.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_empresa", "" + item.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_documento_entrada", "" + item.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
					
					rsmEntradaItemAliquota = EntradaItemAliquotaDAO.find(criterios, connection);
					int ret = EntradaItemAliquotaDAO.insert(entradasTributo.get(i), connection);
					if (ret <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao inserir Entrada Item Aliquota");
					}
				}
			}

			// está comentado pois foi preferido, no caso dos "bois", nao permitir remover e/ou inserir se "isInsertItem" for "false"
			// em futuras melhorias descomentar e melhorar para realizar update. <esdras> 09-10-2013
			// atualiza Referencia e entrada Local Item*/
//			if (produtosReferencia != null) {
//				/* insere o item em cada entrada local item */
//				for (int i = 0; i < entradaLocalItens.size(); i++) {
//					int cod = produtosReferencia.get(i).getCdReferencia() == 0 ? ProdutoReferenciaDAO.insert(produtosReferencia.get(i), connection) : 0;
//					produtosReferencia.get(i).setCdReferencia(cod);
//					if (produtosReferencia.get(i).getCdReferencia() > 0) {
//						EntradaLocalItem entradaLocalItem = entradaLocalItens.get(i);
//						entradaLocalItem.setCdItem(item.getCdItem());
//						entradaLocalItem.setCdReferencia(produtosReferencia.get(i).getCdReferencia());
//						
//						int ret = entradaLocalItem.getCdEntradaLocalItem() == 0 && entradaLocalItem.getCdItem() == 0? EntradaLocalItemDAO.insert(entradaLocalItem, connection) : 0;
//						if (ret <= 0) {
//							if (isConnectionNull)
//								Conexao.rollback(connection);
//							return new Result(-1, "Erro ao tentar salvar entrada local item!");
//						}
//					} 
//					else{
//						if (isConnectionNull)
//							Conexao.rollback(connection);
//						return new Result(-1, "Erro ao tentar salvar produto referencia!");
//					}
//				}
//			} 
//			else {
				// registra saida no estoque
			if (cdLocalArmazenamento != 0) {
				float qtEntrada = item.getQtEntrada();
				DocumentoEntrada documento = DocumentoEntradaDAO.get(cdDocumentoEntrada, connection);
				if (documento == null) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Documento de entrada inválido! [cdDocumentoEntrada:"+cdDocumentoEntrada+"]");
				}
				EntradaLocalItem itenArmazenamento = new EntradaLocalItem(cdProdutoServico, cdDocumentoEntrada, cdEmpresa, cdLocalArmazenamento,
						documento.getTpMovimentoEstoque()==DocumentoEntradaServices.MOV_ESTOQUE_NAO_CONSIGNADO || documento.getTpMovimentoEstoque()==DocumentoEntradaServices.MOV_AMBOS_TIPO_ESTOQUE ? qtEntrada : 0,
						documento.getTpMovimentoEstoque() == DocumentoEntradaServices.MOV_ESTOQUE_CONSIGNADO ? qtEntrada : 0, 
								0 /*cdLocalArmazenamentoItem*/, item.getCdItem());
				if (EntradaLocalItemServices.insert(itenArmazenamento, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar inserir entrada no local de armazenamento!");
				}
			}
//			}

			/*
			 * Informações de Importação
			 */
			DocumentoEntrada docEntrada = DocumentoEntradaDAO.get(item.getCdDocumentoEntrada(), connection);
			int cdNatOperacao = (item.getCdNaturezaOperacao() > 0 ? item.getCdNaturezaOperacao() : docEntrada.getCdNaturezaOperacao());
			if(cdNatOperacao > 0 && cdNatOperacao == ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_ENTRADA_IMPORTACAO_DEFAULT", 0)){
				Result resultado = recalcImpostosImportacao(item, connection);
				if(resultado.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return resultado;
				}
			}
			
			if (isConnectionNull)
				connection.commit();

			return new Result(1, "Item da entrada atualizado com sucesso!", "rsmTributos", null);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar atualizar dados do item de entrada!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result delete(int cdDocumentoEntrada, int cdProdutoServico, int cdEmpresa, int cdItem) {
		return delete(cdDocumentoEntrada, cdProdutoServico, cdEmpresa, cdItem, 0, null);
	}
	
	public static Result delete(int cdDocumentoEntrada, int cdProdutoServico, int cdEmpresa, int cdItem, Connection connection) {
		return delete(cdDocumentoEntrada, cdProdutoServico, cdEmpresa, cdItem, 0, connection);
	}
	
	public static Result remove(int cdDocumentoEntrada, int cdProdutoServico, int cdEmpresa, int cdItem, int cdLocalArmazenamento) {
		return delete(cdDocumentoEntrada, cdProdutoServico, cdEmpresa, cdItem, cdLocalArmazenamento, null);
	}

	public static Result delete(int cdDocumentoEntrada, int cdProdutoServico, int cdEmpresa, int cdItem, int cdLocalArmazenamento, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM alm_balanco_doc_entrada " +
					                                              "WHERE cd_documento_entrada = "+cdDocumentoEntrada);
			if (pstmt.executeQuery().next())
				return new Result(ERR_DOC_BALANCO, "Essa entrada já faz parte de um balanço, não é permitido exclui-la!");
			
			/*
			 * Buscar entrada_local_item para apagar produto_referencia sem violar chave estrangeira
			 */
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT cd_referencia FROM alm_entrada_local_item WHERE cd_item = " + cdItem+ 
					"     AND cd_documento_entrada = "+cdDocumentoEntrada+
					"     AND cd_empresa           = "+cdEmpresa+
					"	  AND cd_produto_servico   = "+cdProdutoServico+
					"	  AND (SELECT count(*) FROM alm_entrada_local_item WHERE cd_documento_entrada <> "+cdDocumentoEntrada+ 
					"	  AND cd_empresa = "+cdEmpresa+
					"	  AND cd_produto_servico = "+cdProdutoServico+ 
//					"	  AND cd_item = "+cdItem+
					") = 0");
			ResultSetMap rsm = new ResultSetMap(preparedStatement.executeQuery());
			
			/*
			 * Excluindo tributos
			 */
			connection.prepareStatement("DELETE FROM adm_entrada_item_aliquota " +
										"WHERE cd_produto_servico   = "+cdProdutoServico+
										"  AND cd_documento_entrada = "+cdDocumentoEntrada+
										"  AND cd_empresa           = "+cdEmpresa+
										"  AND cd_item              = "+cdItem).execute();

			/*
			 * Excluindo informações de devolução
			 */
			
			connection.prepareStatement("DELETE FROM alm_devolucao_item " +
										"WHERE cd_produto_servico   = "+cdProdutoServico+
										"  AND cd_documento_entrada = "+cdDocumentoEntrada+
										"  AND cd_empresa           = "+cdEmpresa+
										"  AND cd_item_entrada      = "+cdItem).executeUpdate();

			/*
			 * Excluindo entradas por local
			 */
			connection.prepareStatement("DELETE FROM alm_entrada_local_item " +
					"WHERE cd_produto_servico   = "+cdProdutoServico+
					"  AND cd_documento_entrada = "+cdDocumentoEntrada+
					"  AND cd_empresa           = "+cdEmpresa+
					"  AND cd_item              = "+cdItem+
					(cdLocalArmazenamento > 0 ? " AND cd_local_armazenamento = " + cdLocalArmazenamento : "")).execute();
			
			
			/*
			 * Excluindo compra entrada item
			 */
			connection.prepareStatement("DELETE FROM adm_compra_entrada_item " +
					"WHERE cd_produto_servico   = "+cdProdutoServico+
					"  AND cd_documento_entrada = "+cdDocumentoEntrada+
					"  AND cd_empresa           = "+cdEmpresa+
					"  AND cd_item              = "+cdItem).execute();
			
			
			/*
			 * Excluindo produtos referencia
			 */
			while (rsm.next()){
				connection.prepareStatement("DELETE FROM alm_produto_referencia " +
						"WHERE cd_referencia = " + rsm.getInt("cd_referencia") +
						"  AND cd_produto_servico   = "+cdProdutoServico+
						"  AND cd_empresa           = "+cdEmpresa).execute();
			}
			

			if (DocumentoEntradaItemDAO.delete(cdDocumentoEntrada, cdProdutoServico, cdEmpresa, cdItem, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Falha ao tentar excluir item da entrada!");
			}

			if (isConnectionNull)
				connection.commit();

			return new Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Falha ao tentar excluir item da entrada!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap findCompletoRelatorio(ArrayList<ItemComparator> criterios, ArrayList<String> groupByFields,
			ArrayList<String> orderByFields) throws SQLException {
		return findCompletoRelatorio(criterios, groupByFields, orderByFields, null);
	}

	public static ResultSetMap findCompletoRelatorio(ArrayList<ItemComparator> criterios, ArrayList<String> groupByFields,
			ArrayList<String> orderByFields, Connection connect) {

		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			boolean hashEntregaPendente = false;
			boolean lgCombustivel = false;
			int cdEmpresa = 0;
			boolean isNotCombustivel = false;
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for( ItemComparator item : criterios.subList(0, criterios.size() ) ){
				if( item.getColumn().equalsIgnoreCase("lgEntregaPendente")) {
					hashEntregaPendente = true;
				}
				else if( item.getColumn().equalsIgnoreCase("A.cd_empresa")) {
					cdEmpresa = Integer.parseInt(item.getValue());
					crt.add(item);
				}
				else if( item.getColumn().equalsIgnoreCase("lgCombustivel")) {
					lgCombustivel = item.getValue().trim().equals("true") ? true : false;
				}
				else if( item.getColumn().equalsIgnoreCase("isNotCombustivel")) {
					isNotCombustivel = true;
				}
				else
					crt.add(item);
			}
			criterios = crt;
//			for (int i=0; criterios!=null && i<criterios.size()-1; i++){
//				if(criterios.get(i).getColumn().equalsIgnoreCase("lgEntregaPendente")) {
//					hashEntregaPendente = true;
//					criterios.remove(i);
//				}
//				if(criterios.get(i).getColumn().equalsIgnoreCase("A.cd_empresa")) {
//					cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
//				}
//				if(criterios.get(i).getColumn().equalsIgnoreCase("lgCombustivel")) {
//					lgCombustivel = criterios.get(i).getValue().trim().equals("true") ? true : false;
//					criterios.remove(i);
//				}
//				if(criterios.get(i).getColumn().equalsIgnoreCase("isNotCombustivel")) {
//					isNotCombustivel = true;
//					criterios.remove(i);
//				}
//			}
			int cdTabelaPreco        = 0;
			int cdTipoOperacaoVarejo = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0, cdEmpresa, connect);
			if(cdTipoOperacaoVarejo>0)	{
				com.tivic.manager.adm.TipoOperacao tipoOperacao = com.tivic.manager.adm.TipoOperacaoDAO.get(cdTipoOperacaoVarejo, connect);
				cdTabelaPreco                                   = tipoOperacao!=null ? tipoOperacao.getCdTabelaPreco() : 0;
			}
			int cdTributoICMS        = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0, 0, connect);
			String fields = "A.cd_documento_entrada, A.cd_transportadora, A.cd_empresa, A.cd_fornecedor, A.dt_documento_entrada, " +
							"A.st_documento_entrada, A.nr_documento_entrada, A.tp_documento_entrada, A.tp_entrada, A.tp_frete, tp_movimento_estoque, " +
							"A.cd_digitador, A.vl_total_documento, A.dt_emissao, A.dt_documento_entrada, " +
							"B.cd_unidade_medida AS cd_unidade_medida_item, B.cd_adicao, C.id_reduzido, C.cd_unidade_medida, " +
							"C.cd_produto_servico, D.nm_produto_servico, E.sg_unidade_medida, G.cd_grupo, G.nm_grupo, K.nm_razao_social AS nm_empresa, " +
							"H.nm_pessoa AS nm_fornecedor, O.nm_pessoa AS nm_digitador, J.nm_pessoa AS nm_transportadora, R.id_classificacao_fiscal, " +
							"S.cd_natureza_operacao, S.nr_codigo_fiscal, U.vl_preco, X.pr_aliquota, X.vl_base_calculo, Z.st_tributaria, " +
							"LA.nm_local_armazenamento, B.qt_entrada, B.vl_unitario, B.vl_acrescimo, B.vl_desconto, DLA.nm_local_armazenamento AS nm_local_armazenamento_produto_empresa," +
							" (SELECT SUM(qt_entrada * vl_unitario) " +
							"  FROM alm_documento_entrada_item DEI " +
							"  WHERE DEI.cd_documento_entrada = A.cd_documento_entrada) AS vl_total_produto " +
							(hashEntregaPendente ? ", (SELECT SUM(M.qt_entrada + M.qt_entrada_consignada) FROM alm_entrada_local_item M " +
							"                          WHERE M.cd_produto_servico   = B.cd_produto_servico " +
							"	                         AND M.cd_documento_entrada = B.cd_documento_entrada " +
							"	                         AND M.cd_empresa           = B.cd_empresa) AS qt_entrada_local" : "");
			String groups = "";
			groupByFields.add("DLA.nm_local_armazenamento");
			String [] retorno = Util.getFieldsAndGroupBy(groupByFields, fields, groups,
						        "SUM(B.vl_unitario * B.qt_entrada + B.vl_acrescimo - B.vl_desconto) AS vl_item, SUM(B.qt_entrada) AS qt_entradas, (SELECT SUM(DEI.qt_entrada * DEI.vl_unitario) FROM alm_documento_entrada_item DEI WHERE DEI.cd_documento_entrada = A.CD_DOCUMENTO_ENTRADA) AS vl_total_produto, LA.nm_local_armazenamento, DLA.nm_local_armazenamento AS nm_local_armazenamento_produto_empresa");
			fields = retorno[0];
			groups = retorno[1];
			ResultSetMap rsm = Search.find(
					"SELECT " + fields + " " +
					"FROM alm_documento_entrada                A " +
					"JOIN alm_documento_entrada_item           B ON (A.cd_documento_entrada = B.cd_documento_entrada) " +
					"JOIN grl_produto_servico_empresa   	   C ON (B.cd_produto_servico = C.cd_produto_servico " +
					"										     AND B.cd_empresa = C.cd_empresa) " +
					"JOIN grl_produto_servico           	   D ON (C.cd_produto_servico = D.cd_produto_servico) " +
					"LEFT OUTER JOIN alm_local_armazenamento   DLA ON (C.cd_local_armazenamento      = DLA.cd_local_armazenamento)  " +
					"LEFT OUTER JOIN grl_unidade_medida 	   E ON (B.cd_unidade_medida = E.cd_unidade_medida) " +
					"LEFT OUTER JOIN alm_produto_grupo         F ON (F.cd_produto_servico = C.cd_produto_servico " +
					"								      	     AND F.cd_empresa         = C.cd_empresa " +
					"								    	     AND F.lg_principal       = 1) " +
					"LEFT OUTER JOIN alm_grupo                 G ON (F.cd_grupo = G.cd_grupo) " +
					"LEFT OUTER JOIN grl_pessoa                H ON (A.cd_fornecedor = H.cd_pessoa) " +
					"LEFT OUTER JOIN seg_usuario               I ON (A.cd_digitador = I.cd_usuario) " +
					"LEFT OUTER JOIN grl_pessoa                O ON (I.cd_pessoa = O.cd_pessoa) " +
					"LEFT OUTER JOIN grl_pessoa                J ON (A.cd_transportadora = J.cd_pessoa) " +
					"LEFT OUTER JOIN grl_empresa               N ON (A.cd_empresa = N.cd_empresa) " +
					"LEFT OUTER JOIN grl_pessoa_juridica       K ON (N.cd_empresa  = K.cd_pessoa) " +
					"LEFT OUTER JOIN adm_classificacao_fiscal  R ON (D.cd_classificacao_fiscal = R.cd_classificacao_fiscal)  " +
					"LEFT OUTER JOIN adm_natureza_operacao     S ON (A.cd_natureza_operacao    = S.cd_natureza_operacao)  " +
					"LEFT OUTER JOIN adm_produto_servico_preco U ON (D.cd_produto_servico      = U.cd_produto_servico " +
					"                                            AND U.cd_tabela_preco = "+cdTabelaPreco +
					"                                            AND U.dt_termino_validade is NULL) " + 
					"LEFT OUTER JOIN adm_entrada_item_aliquota X ON (B.cd_documento_entrada = X.cd_documento_entrada " +
					"                                            AND B.cd_produto_servico   = X.cd_produto_servico " +
					"                                            AND B.cd_empresa           = X.cd_empresa " +
					"                                            AND B.cd_item              = X.cd_item " +
					"                                            AND X.cd_tributo           = "+cdTributoICMS+") " + 
					"LEFT OUTER JOIN adm_tributo_aliquota      Z ON (Z.cd_tributo_aliquota = X.cd_tributo_aliquota " +
					"                                            AND Z.cd_tributo          = X.cd_tributo) " +
					"LEFT OUTER JOIN alm_entrada_local_item  ELI ON (A.cd_documento_entrada 	   = ELI.cd_documento_entrada" +
					"                                            AND D.cd_produto_servico = ELI.cd_produto_servico" +
                    "                                            AND A.cd_empresa         = ELI.cd_empresa) " +
					"LEFT OUTER JOIN alm_local_armazenamento  LA ON (ELI.cd_local_armazenamento = LA.cd_local_armazenamento) " +
				    "WHERE 1 = 1 " +
				    (hashEntregaPendente ? "  AND B.qt_entrada>0 AND (B.qt_entrada <> (SELECT SUM(M.qt_entrada + M.qt_entrada_consignada) " +
				    "					  FROM alm_entrada_local_item M " +
				    "					  WHERE M.cd_produto_servico = B.cd_produto_servico " +
				    "						AND M.cd_documento_entrada = B.cd_documento_entrada " +
				    "						AND M.cd_empresa = B.cd_empresa) OR " +
				    "					  NOT EXISTS (SELECT M.cd_entrada_local_item " +
				    "								  FROM alm_entrada_local_item M " +
				    "								  WHERE M.cd_produto_servico = B.cd_produto_servico " +
				    "									AND M.cd_documento_entrada = B.cd_documento_entrada " +
				    "									AND M.cd_empresa = B.cd_empresa)) " : "") + 
				    (!lgCombustivel ? "  AND NOT EXISTS (SELECT GC.cd_grupo " +
				    "								  FROM alm_grupo GC " +
				    "								  WHERE GC.cd_grupo = G.cd_grupo " +
				    "									AND GC.cd_grupo = " + ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa) +
				    ") " : "") +
				    (isNotCombustivel ? " AND G.cd_grupo NOT IN " + GrupoServices.getAllCombustivel(cdEmpresa, connect) : "") +
				    (lgCombustivel && cdEmpresa > 0 ? "  AND G.cd_grupo IN " + GrupoServices.getAllCombustivel(cdEmpresa, connect) + 
				    " " : ""), groups + (orderByFields.size() == 1 ? " ORDER BY " + orderByFields.get(0) + "" : ""), criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			if (orderByFields != null && orderByFields.size()>1) {
				rsm.orderBy(orderByFields);
			}
			return rsm;
		
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, null, null, null);
	}
	
	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, ArrayList<String> groupByFields,
			ArrayList<String> orderByFields) {
		return findCompleto(criterios, groupByFields, orderByFields, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, ArrayList<String> groupByFields,
			ArrayList<String> orderByFields, Connection connect) {
		boolean hashEntregaPendente = false;
		for (int i=0; criterios!=null && i<criterios.size(); i++)
			if (criterios.get(i).getColumn().equalsIgnoreCase("lgEntregaPendente")) {
				hashEntregaPendente = true;
				criterios.remove(i);
				break;
			}

		String fields = "A.cd_documento_entrada, A.cd_transportadora, A.cd_empresa, A.cd_fornecedor, A.dt_documento_entrada, " +
						"A.st_documento_entrada, A.nr_documento_entrada, A.tp_documento_entrada, A.tp_entrada, A.tp_frete, tp_movimento_estoque, " +
						"A.cd_digitador, B.qt_entrada, B.vl_unitario, B.vl_acrescimo, B.vl_desconto, " +
						"B.cd_unidade_medida AS cd_unidade_medida_item, C.id_reduzido, C.cd_unidade_medida, " +
						"C.cd_produto_servico, D.nm_produto_servico, E.sg_unidade_medida, G.nm_grupo, K.nm_razao_social AS nm_empresa, " +
						"H.nm_pessoa AS nm_fornecedor, O.nm_pessoa AS nm_digitador, J.nm_pessoa AS nm_transportadora " +
						(hashEntregaPendente ? ", (SELECT SUM(M.qt_entrada + M.qt_entrada_consignada) FROM alm_entrada_local_item M " +
						"                          WHERE M.cd_produto_servico   = B.cd_produto_servico " +
						"	                         AND M.cd_documento_entrada = B.cd_documento_entrada " +
						"	                         AND M.cd_empresa           = B.cd_empresa) AS qt_entrada_local" : "");
		String groups = "";
		String [] retorno = Util.getFieldsAndGroupBy(groupByFields, fields, groups,
					        "SUM(B.vl_unitario * B.qt_entrada + B.vl_acrescimo - B.vl_desconto) AS vl_item, SUM(B.qt_entrada) AS qt_entradas");
		fields = retorno[0];
		groups = retorno[1];

		ResultSetMap rsm = Search.find("SELECT " + fields + " " +
				"FROM alm_documento_entrada A " +
				"JOIN alm_documento_entrada_item B ON (A.cd_documento_entrada = B.cd_documento_entrada) " +
				"JOIN grl_produto_servico_empresa C ON (B.cd_produto_servico = C.cd_produto_servico AND " +
				"										B.cd_empresa = C.cd_empresa) " +
				"JOIN grl_produto_servico D ON (C.cd_produto_servico = D.cd_produto_servico) " +
				"LEFT OUTER JOIN grl_unidade_medida E ON (B.cd_unidade_medida = E.cd_unidade_medida) " +
				"LEFT OUTER JOIN alm_produto_grupo F ON (C.cd_produto_servico = F.cd_produto_servico AND " +
				"										 C.cd_empresa = F.cd_empresa AND " +
				"										 ((EXISTS (SELECT G.cd_grupo " +
				"												   FROM alm_produto_grupo G " +
				"												   WHERE C.cd_produto_servico = G.cd_produto_servico " +
				"												     AND C.cd_empresa = G.cd_empresa " +
				"												     AND G.lg_principal = 1) AND " +
				"										   F.cd_grupo = (SELECT MAX(G.cd_grupo) " +
				"													     FROM alm_produto_grupo G " +
				"													     WHERE C.cd_produto_servico = G.cd_produto_servico " +
				"														   AND C.cd_empresa = G.cd_empresa " +
				"														   AND G.lg_principal = 1)) OR " +
				"										  (NOT EXISTS (SELECT G.cd_grupo " +
				"													   FROM alm_produto_grupo G " +
				"													   WHERE C.cd_produto_servico = G.cd_produto_servico " +
				"														 AND C.cd_empresa = G.cd_empresa " +
				"														 AND G.lg_principal = 1) AND " +
				"										   F.cd_grupo = (SELECT MAX(G.cd_grupo) " +
				"														 FROM alm_produto_grupo G " +
				"														 WHERE C.cd_produto_servico = G.cd_produto_servico " +
				"														   AND C.cd_empresa = G.cd_empresa)))) " +
				"LEFT OUTER JOIN alm_grupo G ON (F.cd_grupo = G.cd_grupo) " +
				"LEFT OUTER JOIN grl_pessoa H ON (A.cd_fornecedor = H.cd_pessoa) " +
				"LEFT OUTER JOIN seg_usuario I ON (A.cd_digitador = I.cd_usuario) " +
				"LEFT OUTER JOIN grl_pessoa O ON (I.cd_pessoa = O.cd_pessoa) " +
				"LEFT OUTER JOIN grl_pessoa J ON (A.cd_transportadora = J.cd_pessoa) " +
				"LEFT OUTER JOIN grl_empresa N ON (A.cd_empresa = N.cd_empresa) " +
				"LEFT OUTER JOIN grl_pessoa_juridica K ON (N.cd_empresa  = K.cd_pessoa) " +
			    "WHERE 1 = 1 " +
			    (hashEntregaPendente ? "  AND B.qt_entrada>0 AND (B.qt_entrada <> (SELECT SUM(M.qt_entrada + M.qt_entrada_consignada) " +
			    "					  FROM alm_entrada_local_item M " +
			    "					  WHERE M.cd_produto_servico = B.cd_produto_servico " +
			    "						AND M.cd_documento_entrada = B.cd_documento_entrada " +
			    "						AND M.cd_empresa = B.cd_empresa) OR " +
			    "					  NOT EXISTS (SELECT M.cd_entrada_local_item " +
			    "								  FROM alm_entrada_local_item M " +
			    "								  WHERE M.cd_produto_servico = B.cd_produto_servico " +
			    "									AND M.cd_documento_entrada = B.cd_documento_entrada " +
			    "									AND M.cd_empresa = B.cd_empresa)) " : ""), groups, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		if (orderByFields != null && orderByFields.size()>0) {
			rsm.orderBy(orderByFields);
		}
		return rsm;
	}
	
	public static ResultSetMap findResumoOfItensByGrupo(int cdEmpresa, GregorianCalendar dtEntradaInicial, GregorianCalendar dtEntradaFinal){
		Connection connect = Conexao.conectar();
		try {
			dtEntradaInicial.set(Calendar.HOUR, 0);
			dtEntradaInicial.set(Calendar.MINUTE, 0);
			dtEntradaInicial.set(Calendar.SECOND, 0);
			
			dtEntradaFinal.set(Calendar.HOUR, 23);
			dtEntradaFinal.set(Calendar.MINUTE, 59);
			dtEntradaFinal.set(Calendar.SECOND, 59);
			PreparedStatement pstmt;
			String sql = "SELECT G.cd_grupo, G.nm_grupo, SUM(DEI.qt_entrada * DEI.vl_unitario + DEI.vl_acrescimo - DEI.vl_desconto) AS vl_total_liquido "+
						 "FROM alm_documento_entrada_item     DEI " +
						 "INNER JOIN alm_documento_entrada    DE ON (DE.cd_documento_entrada = DEI.cd_documento_entrada) "+
						 "LEFT OUTER JOIN alm_produto_grupo PG ON (PG.cd_produto_servico = DEI.cd_produto_servico " +
						 "                                     AND PG.cd_empresa         = DEI.cd_empresa " +
						 "                                     AND PG.lg_principal       = 1) " +
						 "LEFT OUTER JOIN alm_grupo         G  ON (PG.cd_grupo = G.cd_grupo) "+
						 "WHERE DE.cd_empresa = "+ cdEmpresa +
						 " AND DE.dt_documento_entrada BETWEEN ? AND ? "+
						 "GROUP BY G.cd_grupo, G.nm_grupo ";
			
			pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtEntradaInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtEntradaFinal.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaItemServices.findResumoOfItensByGrupo: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findItensByGrupo(int cdEmpresa,int cdGrupo, GregorianCalendar dtEntradaInicial, GregorianCalendar dtEntradaFinal){
		Connection connect = Conexao.conectar();
		try {
			dtEntradaInicial.set(Calendar.HOUR, 0);
			dtEntradaInicial.set(Calendar.MINUTE, 0);
			dtEntradaInicial.set(Calendar.SECOND, 0);
			
			dtEntradaFinal.set(Calendar.HOUR, 23);
			dtEntradaFinal.set(Calendar.MINUTE, 59);
			dtEntradaFinal.set(Calendar.SECOND, 59);
			PreparedStatement pstmt;
			String sql = "SELECT DEI.qt_entrada,DEI.vl_unitario,DEI.vl_acrescimo,DEI.vl_desconto,PS.nm_produto_servico "+
						 "FROM alm_documento_entrada_item     DEI " +
						 "INNER JOIN alm_documento_entrada    DE ON (DE.cd_documento_entrada = DEI.cd_documento_entrada) "+
						 "LEFT OUTER JOIN alm_produto_grupo PG ON (PG.cd_produto_servico = DEI.cd_produto_servico " +
						 "                                     AND PG.cd_empresa         = DEI.cd_empresa " +
						 "                                     AND PG.lg_principal       = 1) " +
						 "LEFT OUTER JOIN grl_produto_servico PS ON(DEI.cd_produto_servico = PS.cd_produto_servico)" +
						 "LEFT OUTER JOIN alm_grupo         G  ON (PG.cd_grupo = G.cd_grupo) "+
						 "WHERE DE.cd_empresa = "+ cdEmpresa +
						 " AND  PG.cd_grupo = " + cdGrupo +
						 " AND  DE.dt_documento_entrada BETWEEN ? AND ? ";
			pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtEntradaInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtEntradaFinal.getTimeInMillis()));
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaItemServices.findItensByGrupo: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	//======
	public static Result insertGrade(int cdProdutoServico, int cdEmpresa, int cdDocumentoEntrada, int cdItem, ArrayList<String> grade){
		return insertGrade(cdProdutoServico, cdEmpresa, cdDocumentoEntrada, cdItem, grade, null);
	}
	
	public static Result insertGrade(int cdProdutoServico, int cdEmpresa, int cdDocumentoEntrada, int cdItem, ArrayList<String> grade, Connection connect) {
		boolean isConnectionNull = connect==null;
		
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			Result result = ProdutoServicoServices.insertGrade(cdProdutoServico, cdEmpresa, grade);
			@SuppressWarnings("unchecked")
			ArrayList<Integer> codigos = (ArrayList<Integer>) result.getObjects().get("codigos");
			DocumentoEntradaItem  aDocument = DocumentoEntradaItemDAO.get(cdDocumentoEntrada, cdProdutoServico, cdEmpresa, cdItem);
			int quantidade = 0;	
			
			for(int i = 1; i < codigos.size(); i++) {
				//pega a quantidade de cada item da grade
				java.util.StringTokenizer tokens = new java.util.StringTokenizer((String)grade.get(i - 1), "|", false);
				String txtQuantidade = tokens.nextToken().trim();
				if(Util.isNumber(txtQuantidade)) 
					quantidade	= Integer.parseInt(txtQuantidade);//[+]
				else
					return new Result(-1, "Quantidade Incorreta");
					
				if(quantidade > 0) {
					//altera os campos do novo item da grade
					aDocument.setCdDocumentoEntrada(cdDocumentoEntrada);
					aDocument.setCdProdutoServico(codigos.get(i));
					aDocument.setCdEmpresa(cdEmpresa);
					aDocument.setQtEntrada(quantidade);
					
				//insere o novo item
					if(DocumentoEntradaItemDAO.insert(aDocument) < 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao inserir novo item");
					}
				}
				
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Grade cadastrada com sucesso.");
		} catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaItemDAO.insert: " + sqlExpt);
			return new Result((-1)*sqlExpt.getErrorCode());
		} catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaItemDAO.insert: " +  e);
			return new Result(-1, e.getMessage());
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result apagaItemReferencia(int cdReferencia, int cdProdutoServico, int cdEmpresa, int cdLocalArmazenamento, int cdDocumentoEntrada, int cdItem) {
		return apagaItemReferencia(cdReferencia, cdProdutoServico, cdEmpresa, cdLocalArmazenamento, cdDocumentoEntrada, cdItem, null);
	}
	public static Result apagaItemReferencia(int cdReferencia, int cdProdutoServico, int cdEmpresa, int cdLocalArmazenamento, int cdDocumentoEntrada, int cdItem, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM alm_balanco_doc_entrada " +
					                                              "WHERE cd_documento_entrada = "+cdDocumentoEntrada);
			if (pstmt.executeQuery().next())
				return new Result(ERR_DOC_BALANCO, "Essa entrada já faz parte de um balanço, não é permitido exclui-la!");
			

			/*
			 * Excluindo entrada por local
			 */
			connection.prepareStatement("DELETE FROM alm_entrada_local_item " +
					"WHERE cd_produto_servico   = "+cdProdutoServico+
					"  AND cd_documento_entrada = "+cdDocumentoEntrada+
					"  AND cd_empresa           = "+cdEmpresa+
					"  AND cd_referencia        = "+cdReferencia+
					"  AND cd_item              = "+cdItem).execute();
			/*
			 * Excluindo produto referencia
			 */
			connection.prepareStatement("DELETE FROM alm_produto_referencia " +
					"WHERE cd_referencia = " + cdReferencia +
					"  AND cd_produto_servico   = "+cdProdutoServico+
					"  AND cd_empresa           = "+cdEmpresa).execute();

			if (isConnectionNull)
				connection.commit();

			return new Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Falha ao tentar excluir item da entrada!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ProdutoReferencia getProdutoReferencia(ProdutoReferencia produtoReferencia, Connection connection){
		return ProdutoReferenciaDAO.get(produtoReferencia.getCdReferencia(), produtoReferencia.getCdProdutoServico(), 
				produtoReferencia.getCdEmpresa(), produtoReferencia.getNmReferencia(), connection);
	}
//	metodo que consulta a existencia de um produto referencia de acordo com os parametros passados
//	public static boolean consultaProdutoReferencia(int cdReferencia, int cdProdutoServico, int cdEmpresa, String nmReferencia) {
//		return getProdutoReferencia(new ProdutoReferencia(cdReferencia, cdProdutoServico, cdEmpresa, nmReferencia, "", null, null, 0, 0, 0, 0, "", 0), null) != null;
//	}
	
	public static Result saveItens(ArrayList<DocumentoEntradaItem> itens) {
		return saveItens(itens, null);
	}
	
	public static Result saveItens(ArrayList<DocumentoEntradaItem> itens, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			Result result = null;
			
			for (DocumentoEntradaItem documentoEntradaItem : itens) {
				result = DocumentoEntradaItemServices.insert(documentoEntradaItem, connection);
				if(result.getCode()<0) {
					if(isConnectionNull)
						connection.rollback();
					return result;
				}
			}
			
			return new Result(1, "Os itens foram salvos com sucesso.");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Falha ao tentar excluir item da entrada!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
}
