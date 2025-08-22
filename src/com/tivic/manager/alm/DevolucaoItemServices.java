package com.tivic.manager.alm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.tivic.manager.adm.ContaPagar;
import com.tivic.manager.adm.ContaPagarCategoria;
import com.tivic.manager.adm.ContaPagarServices;
import com.tivic.manager.adm.EntradaItemAliquota;
import com.tivic.manager.adm.EntradaItemAliquotaDAO;
import com.tivic.manager.adm.NaturezaOperacao;
import com.tivic.manager.adm.NaturezaOperacaoDAO;
import com.tivic.manager.adm.SaidaItemAliquota;
import com.tivic.manager.adm.SaidaItemAliquotaDAO;
import com.tivic.manager.adm.SaidaItemAliquotaServices;
import com.tivic.manager.adm.TipoOperacaoDAO;
import com.tivic.manager.adm.TipoOperacaoServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.fsc.NotaFiscal;
import com.tivic.manager.fsc.NotaFiscalDAO;
import com.tivic.manager.fsc.NotaFiscalDocVinculado;
import com.tivic.manager.fsc.NotaFiscalDocVinculadoDAO;
import com.tivic.manager.fsc.NotaFiscalItem;
import com.tivic.manager.fsc.NotaFiscalItemDAO;
import com.tivic.manager.fsc.NotaFiscalItemTributo;
import com.tivic.manager.fsc.NotaFiscalItemTributoDAO;
import com.tivic.manager.fsc.NotaFiscalItemTributoServices;
import com.tivic.manager.fsc.NotaFiscalServices;
import com.tivic.manager.fsc.NotaFiscalTributo;
import com.tivic.manager.fsc.NotaFiscalTributoDAO;
import com.tivic.manager.fsc.NotaFiscalTributoServices;
import com.tivic.manager.fsc.SituacaoTributaria;
import com.tivic.manager.fsc.SituacaoTributariaDAO;
import com.tivic.manager.grl.*;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

public class DevolucaoItemServices {

	public static Result insertDevolucaoCliente(int cdDocumentoSaida, int cdLocalArmazenamento, int cdConta, int cdDigitador, ArrayList<DevolucaoItem> itens) {
		return insertDevolucaoCliente(cdDocumentoSaida, cdLocalArmazenamento, cdConta, cdDigitador, itens, null);
	}

	public static Result insertDevolucaoCliente(int cdDocumentoSaida, int cdLocalArmazenamento, int cdConta, int cdDigitador, ArrayList<DevolucaoItem> itens, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (itens==null || itens.size()<=0)
				return new Result(-1, "Produtos a serem devolvidos não informados!");
			if (cdDigitador <= 0)
				return new Result(-1, "Informações do usuário não informadas!");
			// Conecta
			connection = isConnectionNull ? Conexao.conectar() : connection;
			// Busca documento de saída
			DocumentoSaida docSaida = DocumentoSaidaDAO.get(cdDocumentoSaida, connection);
			// Verifica natureza de operaçao padrão
			int cdNaturezaOperacaoDevolucao = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_CLIENTE", 0, 0, connection);
			if (cdNaturezaOperacaoDevolucao <= 0)	
				return new Result(-1, "A natureza de operação padrão para devoluções não foi informada. Comunique o suporte!");
			// Tipo de Documento para Devolucao
			int cdTipoDocumentoDevolucao = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_DEVOLUCAO_CLIENTE", 0, 0, connection);
			if (cdTipoDocumentoDevolucao <= 0)	
				return new Result(-1, "O tipo de documento padrão para devoluções não foi informado. Comunique o suporte!");
			// Categoria para Devolucao
			int cdCategoriaDevolucao = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_DEVOLUCAO_CLIENTE", 0, 0, connection);
			if (cdCategoriaDevolucao <= 0)	
				return new Result(-1, "A categoria para classificação das devoluções não foi informada. Comunique o suporte!");
			
			// Auto-Commit
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			/*
			 * Somando os totais e criando os itens da entrada (devolução)
			 */
			PreparedStatement pstmtDevolvido = connection.prepareStatement("SELECT SUM(qt_devolvida) AS qt_devolvida " +
					                                                       "FROM alm_devolucao_item A, alm_documento_entrada B "+
					                                                       "WHERE A.cd_documento_entrada = B.cd_documento_entrada "+
					                                                       "  AND B.st_documento_entrada = "+DocumentoEntradaServices.ST_LIBERADO+
					                                                       "  AND B.tp_entrada           = "+DocumentoEntradaServices.ENT_DEVOLUCAO+
					                                                       "  AND A.cd_documento_saida   = ? "+
					                                                       "  AND A.cd_produto_servico   = ? "+
					                                                       "  AND A.cd_empresa           = ? "+
					                                                       "  AND A.cd_item              = ? ");
			ArrayList<DocumentoEntradaItem> itensEntrada = new ArrayList<DocumentoEntradaItem>();
			ArrayList<EntradaItemAliquota> itensEntradaAliquota = new ArrayList<EntradaItemAliquota>();
			float vlTotalItens     = 0;
			float vlTotalDocumento = 0;
			float vlTotalAcrescimo = 0;
			float vlTotalDesconto  = 0;
			for (int i=0; itens!=null && i<itens.size(); i++) {
				DocumentoSaidaItem itemSaida = DocumentoSaidaItemDAO.get(cdDocumentoSaida, itens.get(i).getCdProdutoServico(), itens.get(i).getCdEmpresa(), itens.get(i).getCdItem(), connection);
				pstmtDevolvido.setInt(1, cdDocumentoSaida);
				pstmtDevolvido.setInt(2, itemSaida.getCdProdutoServico());
				pstmtDevolvido.setInt(3, itemSaida.getCdEmpresa());
				pstmtDevolvido.setInt(4, itemSaida.getCdItem());
				ResultSet rsDevolvido = pstmtDevolvido.executeQuery();
				rsDevolvido.next();
				if(itens.get(i).getQtDevolvida() > (itemSaida.getQtSaida()-rsDevolvido.getInt("qt_devolvida")))	{
					ProdutoServico produto = ProdutoServicoDAO.get(itemSaida.getCdProdutoServico(), connection);
					return new Result(-1, "A quantidade a ser devolvida do produto \""+produto.getNmProdutoServico()+"\" é invalida! " +
							              " Quantidade da saida original: "+itemSaida.getQtSaida()+
							              ", ja devolvido: "+rsDevolvido.getInt("qt_devolvida"));
				}
				float vlDescontoItem  = itemSaida.getVlDesconto() / itemSaida.getQtSaida() * itens.get(i).getQtDevolvida();
				float vlAcrescimoItem = itemSaida.getVlAcrescimo() / itemSaida.getQtSaida() * itens.get(i).getQtDevolvida();
				itensEntrada.add(new DocumentoEntradaItem(0, itens.get(i).getCdProdutoServico(), itens.get(i).getCdEmpresa(),
						                                  itens.get(i).getQtDevolvida() /*qtEntrada*/, itemSaida.getVlUnitario(),
						                                  vlAcrescimoItem, vlDescontoItem,
						                                  itens.get(i).getCdUnidadeMedida(), null /*dtEntregaPrevista*/, itens.get(i).getCdItem(), 0, 0, 0 ,0 ,0));
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_documento_saida", "" + itemSaida.getCdDocumentoSaida(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_produto_servico", "" + itemSaida.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_item", "" + itemSaida.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_empresa", "" + itemSaida.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
				
				ResultSetMap rsm = SaidaItemAliquotaDAO.find(criterios, connection);
				
				while(rsm.next()){
					itensEntradaAliquota.add(new EntradaItemAliquota(rsm.getInt("cd_produto_servico"), 0, rsm.getInt("cd_empresa"), rsm.getInt("cd_tributo_aliquota"), 
																	 rsm.getInt("cd_tributo"), 0, rsm.getFloat("pr_aliquota"), rsm.getFloat("vl_base_calculo"), 
																	 rsm.getInt("cd_situacao_tributaria")));
				}
				
				vlTotalDocumento += (itemSaida.getVlUnitario() * itens.get(i).getQtDevolvida()) - vlDescontoItem + vlAcrescimoItem;
				vlTotalAcrescimo += vlAcrescimoItem;
				vlTotalDesconto  += vlDescontoItem;
				vlTotalItens     += (itemSaida.getVlUnitario() * itens.get(i).getQtDevolvida());
			}
			/*
			 * Salvando o documento de entrada
			 */
			String nrDocumentoEntrada   = DocumentoEntradaServices.getProximoNrDocumento(docSaida.getCdEmpresa(), connection); 
			DocumentoEntrada docEntrada = new DocumentoEntrada(0 /*cdDocumentoEntrada*/, docSaida.getCdEmpresa(), 0 /*cdTransportadora*/,
															   docSaida.getCdCliente() /*cdFornecedor*/, new GregorianCalendar() /*dtEmissao*/,
															   new GregorianCalendar() /*dtDocumentoEntrada*/, DocumentoEntradaServices.ST_EM_ABERTO /*stDocumentoEntrada*/,
															   vlTotalDesconto, vlTotalAcrescimo, nrDocumentoEntrada,
															   DocumentoEntradaServices.TP_DOC_NAO_FISCAL /*tpDocumentoEntrada*/,
															   "" /*nrConhecimento*/, DocumentoEntradaServices.ENT_DEVOLUCAO /*tpEntrada*/,
															   ""/*txtObservacao*/, cdNaturezaOperacaoDevolucao, DocumentoEntradaServices.FRT_CIF /*tpFrete*/,
															   "" /*nrPlacaVeiculo*/, "" /*sgPlacaVeiculo*/, 0 /*qtVolumes*/, null /*dtSaidaTransportadora*/,
															   "" /*dsViaTransporte*/, "" /*txtNOtaFiscal*/, 0 /*vlPresoBruto*/, 0 /*vlPesoLiquido*/,
															   "" /*dsEspecieVolumes*/, "" /*dsMarcaVolumes*/, "" /*nrVolumes*/,
															   DocumentoEntradaServices.MOV_ESTOQUE_NAO_CONSIGNADO /*tpMovimentacao*/,
															   0 /*cdMoeda*/, 0 /*cdTabelaPreco*/, vlTotalDocumento /*vlTotalDocumento*/,
															   cdDocumentoSaida /*cdDocumentoSaidaOrigem*/, 0 /*vlFrete*/, 0 /*vlSeguro*/, cdDigitador, vlTotalItens, 1 /*nrSerie*/);
			int cdDocumentoEntrada = DocumentoEntradaDAO.insert(docEntrada, connection);
			if (cdDocumentoEntrada<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao tenta criar o documento de devolucao!");
			}
			/*
			 * Salvando os itens
			 */
			for (int i=0; i<itensEntrada.size(); i++) {
				itensEntrada.get(i).setCdDocumentoEntrada(cdDocumentoEntrada);
				int code = DocumentoEntradaItemDAO.insert(itensEntrada.get(i), connection);
				if (code <= 0) {
					if (isConnectionNull)	{
						Conexao.rollback(connection);
						return new Result(-1, "Erro ao incluir item na devolucao(entrada)!");
					}
				}
				itens.get(i).setCdDocumentoEntrada(cdDocumentoEntrada);
				if (DevolucaoItemDAO.insert(itens.get(i), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao registrar a devolucao do item!");
				}
				
				for (int j=0; j<itensEntradaAliquota.size(); j++) {
					if(itensEntrada.get(i).getCdProdutoServico() == itensEntradaAliquota.get(j).getCdProdutoServico()){
						itensEntradaAliquota.get(j).setCdDocumentoEntrada(cdDocumentoEntrada);
						itensEntradaAliquota.get(j).setCdItem(itensEntrada.get(i).getCdItem());
						if (EntradaItemAliquotaDAO.insert(itensEntradaAliquota.get(j), connection) <= 0) {
							if (isConnectionNull)	{
								Conexao.rollback(connection);
								return new Result(-1, "Erro ao incluir item aliquota na devolucao(entrada)!");
							}
						}
					}
				}
				
			}
			
			/*
			 * Liberando a entrada
			 */
//			Result result = DocumentoEntradaServices.liberarEntrada(cdDocumentoEntrada, cdLocalArmazenamento, connection);
//			if (result.getCode() <= 0) {
//				if (isConnectionNull)
//					Conexao.rollback(connection);
//				result.setMessage("Falha ao liberar devolucao! "+result.getMessage());
//				return result;
//			}
			/*
			 * Gerando conta a pagar
			 */
//			float vlCredito = vlTotalDocumento; 
//			ContaPagar contaPagar = new ContaPagar(0,0/*cdContrato*/, docSaida.getCdCliente(), docSaida.getCdEmpresa(), 0/*cdContaOrigem*/,
//					                          		cdDocumentoEntrada, cdConta, 0/*cdContaBancaria*/, new GregorianCalendar()/*Vencimento*/, 
//					                          		new GregorianCalendar()/*Emissão*/, null /*dtPagamento*/, new GregorianCalendar()/*Autorizada*/,
//					                          		nrDocumentoEntrada, ""/*nrReferencia*/, 1 /*nrParcela*/, cdTipoDocumentoDevolucao, vlCredito, 
//					                          		0/*vlTotalDesconto*/, 0/*vlTotalAcrescimo*/, 0 /*vlPago*/, "Crédito do Cliente (Devolução)", 
//					                          		ContaPagarServices.ST_EM_ABERTO, 1/*lgAutorizado*/, 0/*tpFrequencia*/, 1 /*qtParc*/, 
//					                          		0 /*vlBaseAutorizacao*/, 0 /*cdViagem*/, 0 /*cdManutencao*/, "" /*txtObservacao*/, 
//					                          		new GregorianCalendar()/*dtCadastro*/, new GregorianCalendar()/*Vencimento Original*/, 0/*cdTurno*/);
//			
//			ArrayList<ContaPagarCategoria> categorias = new ArrayList<ContaPagarCategoria>();
//			categorias.add(new ContaPagarCategoria(0/*cdContaPagar*/,cdCategoriaDevolucao, vlTotalDocumento, 0));
//			result = ContaPagarServices.insert(contaPagar, true, true, categorias, connection);
//			if(result.getCode()<=0)	{
//				if (isConnectionNull)
//					Conexao.rollback(connection);
//				result.setMessage("Erro ao gerar credito de troca: "+result.getMessage());
//				return result;
//			}
			
			
			/*
			 * Emissão de Nota Fiscal Eletrônica
			 */
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_documento_saida", "" + cdDocumentoSaida, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmDocVinculado = NotaFiscalDocVinculadoDAO.find(criterios, connection);
			if(rsmDocVinculado.next()){
				NotaFiscal notaFiscalOrigem = NotaFiscalDAO.get(rsmDocVinculado.getInt("cd_nota_fiscal"), connection);
				
				int nrNotaFiscal = 0;
				if(ParametroServices.getValorOfParametroAsInteger("LG_NUMERACAO_PARAMETRO", 0, notaFiscalOrigem.getCdEmpresa())!=1 && ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, notaFiscalOrigem.getCdEmpresa())==0){
					ResultSetMap rsmNrNota = new ResultSetMap(connection.prepareStatement("SELECT * FROM FSC_NOTA_FISCAL " +
																					"		WHERE CD_EMPRESA = " + notaFiscalOrigem.getCdEmpresa() + 
																					"		AND NR_SERIE = '" + (NotaFiscalServices.getTipoAmbiente(notaFiscalOrigem.getCdEmpresa()) == NotaFiscalServices.HOMOLOGACAO ? "888" : ParametroServices.getValorOfParametro("NR_SERIE", "1", notaFiscalOrigem.getCdEmpresa())) + "'" +  
																					"		ORDER BY CD_NOTA_FISCAL DESC LIMIT 1").executeQuery());
					if(rsmNrNota.next()){
						nrNotaFiscal = Integer.parseInt(rsmNrNota.getString("NR_NOTA_FISCAL")) + 1;
						ParametroServices.updateValueOfParametro(ParametroServices.getByName("NR_NOTA_FISCAL").getCdParametro(), String.valueOf(nrNotaFiscal), notaFiscalOrigem.getCdEmpresa(), connection);
					}
					else{
						nrNotaFiscal = ParametroServices.getValorOfParametroAsInteger("NR_NOTA_FISCAL", 1, notaFiscalOrigem.getCdEmpresa(), connection) + 1;
						ParametroServices.updateValueOfParametro(ParametroServices.getByName("NR_NOTA_FISCAL").getCdParametro(), String.valueOf(nrNotaFiscal), notaFiscalOrigem.getCdEmpresa(), connection);
					}
				}
				else if(ParametroServices.getValorOfParametroAsInteger("LG_NUMERACAO_PARAMETRO", 0, notaFiscalOrigem.getCdEmpresa())==1 && ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, notaFiscalOrigem.getCdEmpresa())==0){
					nrNotaFiscal = ParametroServices.getValorOfParametroAsInteger("NR_NOTA_FISCAL", 1, notaFiscalOrigem.getCdEmpresa(), connection) + 1;
					ParametroServices.updateValueOfParametro(ParametroServices.getByName("NR_NOTA_FISCAL").getCdParametro(), String.valueOf(nrNotaFiscal), notaFiscalOrigem.getCdEmpresa(), connection);
				}
				
				int cdNaturezaOperacaoNota = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_CLIENTE", 0);
				NaturezaOperacao natOperacaoOrigem = NaturezaOperacaoDAO.get(docSaida.getCdNaturezaOperacao(), connection);
				
				
				ResultSetMap rsmEnderecoDest = PessoaServices.getEnderecoPrincipalOfPessoa(notaFiscalOrigem.getCdDestinatario(), connection);
				
				if(rsmEnderecoDest.next() && EmpresaServices.getEstadoOfEmpresa(notaFiscalOrigem.getCdEmpresa(), connection) != CidadeDAO.get(rsmEnderecoDest.getInt("cd_cidade"), connection).getCdEstado()){
					cdNaturezaOperacaoNota = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_CLIENTE_OUTRO_ESTADO", 0);
					
					if(natOperacaoOrigem.getNrCodigoFiscal().substring(0, 2).equals("64")){
						cdNaturezaOperacaoNota = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_CLIENTE_SUBSTITUTO_OUTRO_ESTADO", 0);
					}
				}
				else{
					if(natOperacaoOrigem.getNrCodigoFiscal().substring(0, 2).equals("54")){
						cdNaturezaOperacaoNota = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_CLIENTE_SUBSTITUTO", 0);
					}
				}
				
				
				
				//Replicando Nota Fiscal
				NotaFiscal notaFiscalDevolucao = new NotaFiscal(0, notaFiscalOrigem.getCdEmpresa(), notaFiscalOrigem.getCdEnderecoRetirada(), cdNaturezaOperacaoNota,
						notaFiscalOrigem.getCdCidade(), notaFiscalOrigem.getCdDestinatario(), notaFiscalOrigem.getCdEnderecoDestinatario(), notaFiscalOrigem.getCdEnderecoEntrega(), notaFiscalOrigem.getCdLote(), 
						notaFiscalOrigem.getTpModelo(), notaFiscalOrigem.getNrSerie(), (nrNotaFiscal == 0 ? null : String.valueOf(nrNotaFiscal)), NotaFiscalServices.EM_DIGITACAO, Util.getDataAtual(), 
						NotaFiscalServices.MOV_ENTRADA, Util.getDataAtual(), notaFiscalOrigem.getTpPagamento(), NotaFiscalServices.EMI_NORMAL, NotaFiscalServices.NFE_DE_DEVOLUCAO, NotaFiscalServices.RETRATO, notaFiscalOrigem.getVlTotalProduto()/*vlTotalProduto*/, 
						notaFiscalOrigem.getVlSeguro()/*vlSeguro*/, notaFiscalOrigem.getVlOutrasDespesas()/*vlOutrasDespesas*/, notaFiscalOrigem.getVlTotalNota()/*vlTotalNota*/, NotaFiscalServices.SEM_FRETE, null/*txtObservacao*/, null/*txtInformacaoFisco*/, NotaFiscalServices.RETRATO, null/*nrChaveAcesso*/, 0/*nrDv*/, 
						null/*nrProtocoloAutorizacao*/, null/*dtAutorizacao*/, 0/*cdTransportador*/, 0/*cdNaturezaOperacaoFrete*/, notaFiscalOrigem.getVlFrete()/*vlFrete*/, notaFiscalOrigem.getVlFreteBaseIcms()/*vlFreteBaseIcms*/, notaFiscalOrigem.getVlFreteIcmsRetido()/*vlFreteIcmsRetido*/, null/*nrRecebimento*/, null/*nrPlaca*/, 
						notaFiscalOrigem.getQtVolume()/*qtVolume*/, notaFiscalOrigem.getDsEspecie()/*dsEspecie*/, null/*dsMarca*/, null/*dsNumeracao*/, notaFiscalOrigem.getVlPesoBruto()/*vlPesoBruto*/, notaFiscalOrigem.getVlPesoLiquido()/*vlPesoLiquido*/, 0/*cdVeiculo*/, null/*sgUfVeiculo*/, null/*nrRntc*/, 0/*cdMotivoCancelamento*/, null/*txtXml*/, notaFiscalOrigem.getPrDesconto(), 1/*lgConsumidorFinal*/, 1/*tpVendaPresenca*/, notaFiscalOrigem.getNrChaveAcesso().substring(3));
				
				int cdNotaFiscalDevolucao = NotaFiscalDAO.insert(notaFiscalDevolucao, connection);
				if(cdNotaFiscalDevolucao <= 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao inserir nota fiscal de devolução");
				}
				notaFiscalDevolucao.setCdNotaFiscal(cdNotaFiscalDevolucao);
				
				// GERANDO ID - NFE
				if(notaFiscalDevolucao.getNrNotaFiscal() != null){
					Result result = NotaFiscalServices.gerarNfeId(notaFiscalDevolucao);
					if(result.getCode() <= 0)	{
						Conexao.rollback(connection);
						result.setMessage(result.getMessage()+" Falha ao tentar gerar ID da Nota Fiscal.");
						return result;
					}
					notaFiscalDevolucao.setNrChaveAcesso((String)result.getObjects().get("nrChave"));
					notaFiscalDevolucao.setNrDv((Integer)result.getObjects().get("nrDv"));
				}
				if(NotaFiscalDAO.update(notaFiscalDevolucao, connection) <= 0)	{
					Conexao.rollback(connection);
					return new Result(-1, "Falha ao atualizar ID da NFE");
				}
				
				//Replicando Nota Fiscal Tributo
				ArrayList<NotaFiscalTributo> conjNotaFiscalTributo = NotaFiscalTributoServices.getAllTributo(notaFiscalOrigem.getCdNotaFiscal());
				for(NotaFiscalTributo notaFiscalTributoOrigem : conjNotaFiscalTributo){
					NotaFiscalTributo notaFiscalTributoDevolucao = new NotaFiscalTributo(cdNotaFiscalDevolucao, notaFiscalTributoOrigem.getCdTributo(), notaFiscalTributoOrigem.getVlBaseCalculo(), notaFiscalTributoOrigem.getVlOutrasDespesas(), 
																						notaFiscalTributoOrigem.getVlOutrosImpostos(), notaFiscalTributoOrigem.getVlTributo(), notaFiscalTributoOrigem.getVlBaseRetencao(), notaFiscalTributoOrigem.getVlRetido());
					
					if(NotaFiscalTributoDAO.insert(notaFiscalTributoDevolucao, connection) <= 0){
						if(isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao inserir nota fiscal tributo");
					}
					
				}
				
				ResultSetMap rsmItensNotaFiscalOrigem = NotaFiscalServices.getAllItens(notaFiscalOrigem.getCdNotaFiscal(), connection);
				ArrayList<Integer> itensJaVistos = new ArrayList<Integer>();
				int cdItem = 1;
				while(rsmItensNotaFiscalOrigem.next()){
					for (int i=0; i<itensEntrada.size(); i++) {
						if(rsmItensNotaFiscalOrigem.getInt("cd_produto_servico") == itensEntrada.get(i).getCdProdutoServico() && 
						  !itensJaVistos.contains(itensEntrada.get(i).getCdProdutoServico())){
							
							int cdNaturezaOperacaoItem = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_CLIENTE", 0);
							
							SituacaoTributaria situacaoTributariaOrigem = SituacaoTributariaDAO.get(ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0), Integer.parseInt(rsmItensNotaFiscalOrigem.getString("cd_situacao_tributaria_icms")), connection);
							
							if(EmpresaServices.getEstadoOfEmpresa(notaFiscalOrigem.getCdEmpresa(), connection) != CidadeDAO.get(rsmEnderecoDest.getInt("cd_cidade"), connection).getCdEstado()){
								
								cdNaturezaOperacaoItem = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_CLIENTE_OUTRO_ESTADO", 0);
								if(situacaoTributariaOrigem != null && situacaoTributariaOrigem.getLgSubstituicao()==1){
									cdNaturezaOperacaoItem = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_CLIENTE_SUBSTITUTO_OUTRO_ESTADO", 0);
								}
							}
							else{
								if(situacaoTributariaOrigem != null && situacaoTributariaOrigem.getLgSubstituicao()==1){
									cdNaturezaOperacaoItem = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_CLIENTE_SUBSTITUTO", 0);
								}
							}
							
							//Replicando Nota Fiscal Item
							NotaFiscalItem notaFiscalItemDevolucao = new NotaFiscalItem(cdNotaFiscalDevolucao, cdItem, 0, itensEntrada.get(i).getCdProdutoServico(), rsmItensNotaFiscalOrigem.getInt("cd_empresa"), 
																						cdDocumentoEntrada, cdNaturezaOperacaoItem, itensEntrada.get(i).getQtEntrada(), 
																						rsmItensNotaFiscalOrigem.getFloat("vl_unitario"), rsmItensNotaFiscalOrigem.getString("txt_informacao_adicional"), itensEntrada.get(i).getCdItem(), 
																						rsmItensNotaFiscalOrigem.getFloat("vl_acrescimo"), rsmItensNotaFiscalOrigem.getFloat("vl_desconto"));
							
							if(NotaFiscalItemDAO.insert(notaFiscalItemDevolucao, connection) <= 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return new Result(-1, "Erro ao inserir item de nota fiscal de devolução");
							}
									
							//Replicando Nota Fiscal Item Tributo
							ArrayList<NotaFiscalItemTributo> conjNotaFiscalItemTributo = NotaFiscalItemTributoServices.getAllItemTributo(notaFiscalOrigem.getCdNotaFiscal(), rsmItensNotaFiscalOrigem.getInt("cd_item"), connection);
							for(NotaFiscalItemTributo notaFiscalItemTributo : conjNotaFiscalItemTributo){
								NotaFiscalItemTributo notaFiscalItemTributoDevolucao = new NotaFiscalItemTributo(cdNotaFiscalDevolucao, cdItem, notaFiscalItemTributo.getCdTributo(), notaFiscalItemTributo.getCdTributoAliquota(),
										notaFiscalItemTributo.getTpRegimeTributario(), notaFiscalItemTributo.getTpOrigem(), notaFiscalItemTributo.getTpCalculo(), (notaFiscalItemDevolucao.getVlUnitario() * notaFiscalItemDevolucao.getQtTributario() + notaFiscalItemDevolucao.getVlAcrescimo() - notaFiscalItemDevolucao.getVlDesconto()), 
										notaFiscalItemTributo.getVlOutrasDespesas(), notaFiscalItemTributo.getVlOutrosImpostos(), notaFiscalItemTributo.getPrAliquota(), notaFiscalItemTributo.getVlTributo(), notaFiscalItemTributo.getPrCredito(), 
										notaFiscalItemTributo.getVlCredito(), notaFiscalItemTributo.getNrClasse(), notaFiscalItemTributo.getNrEnquadramento(), notaFiscalItemTributo.getCdSituacaoTributaria(), notaFiscalItemTributo.getVlBaseRetencao(), notaFiscalItemTributo.getVlRetido());
								
								if(NotaFiscalItemTributoDAO.insert(notaFiscalItemTributoDevolucao, connection) <= 0){
									if(isConnectionNull)
										Conexao.rollback(connection);
									return new Result(-1, "Erro ao inserir tributo do item de nota fiscal de devolução");
								}
								
							}
							
							cdItem++;
							itensJaVistos.add(itensEntrada.get(i).getCdProdutoServico());
							break;
						}
					}
				}
				
				NotaFiscalDocVinculado notaFiscalDocVinculadoDevolucao = new NotaFiscalDocVinculado(cdNotaFiscalDevolucao, 0, 0, 0, cdDocumentoEntrada, 1);
				
				if(NotaFiscalDocVinculadoDAO.insert(notaFiscalDocVinculadoDevolucao, connection) <= 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao inserir documento vinculado");
				}
				
			}
			
			
			if (isConnectionNull)
				connection.commit();
			Result result = new Result(cdDocumentoEntrada);
			result.addObject("vlCredito", vlTotalDocumento);
			result.addObject("nrCredito", nrDocumentoEntrada);
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro desconhecido ao tentar registrar devolução!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result insertDevolucaoFornecedor(int cdDocumentoEntrada, int cdLocalArmazenamento, int cdConta, int cdDigitador, ArrayList<DevolucaoItem> itens) {
		return insertDevolucaoFornecedor(cdDocumentoEntrada, cdLocalArmazenamento, cdConta, cdDigitador, itens, null);
	}

	public static Result insertDevolucaoFornecedor(int cdDocumentoEntrada, int cdLocalArmazenamento, int cdConta, int cdDigitador, ArrayList<DevolucaoItem> itens, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (itens==null || itens.size()<=0)
				return new Result(-1, "Produtos a serem devolvidos não informados!");
			if (cdDigitador <= 0)
				return new Result(-1, "Informações do usuário não informadas!");
			// Conecta
			connection = isConnectionNull ? Conexao.conectar() : connection;
			// Busca documento de entrada
			DocumentoEntrada docEntrada = DocumentoEntradaDAO.get(cdDocumentoEntrada, connection);
			// Verifica natureza de operaçao padrão
			int cdNaturezaOperacaoDevolucao = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR", 0, docEntrada.getCdEmpresa(), connection);
			if (cdNaturezaOperacaoDevolucao <= 0)	
				return new Result(-1, "A natureza de operação padrão para devoluções não foi informada. Comunique o suporte!");
			// Tipo de Documento para Devolucao
			int cdTipoDocumentoDevolucao = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_DEVOLUCAO_FORNECEDOR", 0, 0, connection);
			if (cdTipoDocumentoDevolucao <= 0)	
				return new Result(-1, "O tipo de documento padrão para devoluções não foi informado. Comunique o suporte!");
			// Categoria para Devolucao
			int cdCategoriaDevolucao = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_DEVOLUCAO_FORNECEDOR", 0, 0, connection);
			if (cdCategoriaDevolucao <= 0)	
				return new Result(-1, "A categoria para classificação das devoluções não foi informada. Comunique o suporte!");
			
			// Auto-Commit
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			/*
			 * Somando os totais e criando os itens da entrada (devolução)
			 */
			PreparedStatement pstmtDevolvido = connection.prepareStatement("SELECT SUM(qt_devolvida) AS qt_devolvida " +
					                                                       "FROM alm_devolucao_item A, alm_documento_saida B "+
					                                                       "WHERE A.cd_documento_saida = B.cd_documento_saida "+
					                                                       "  AND B.st_documento_saida = "+DocumentoSaidaServices.ST_CONCLUIDO+
					                                                       "  AND B.tp_saida           = "+DocumentoSaidaServices.SAI_DEVOLUCAO+
					                                                       "  AND A.cd_documento_entrada = ? "+
					                                                       "  AND A.cd_produto_servico   = ? "+
					                                                       "  AND A.cd_empresa           = ? "+
					                                                       "  AND A.cd_item_entrada      = ? ");
			ArrayList<DocumentoSaidaItem> itensSaida = new ArrayList<DocumentoSaidaItem>();
			ArrayList<SaidaItemAliquota> itensSaidaAliquota = new ArrayList<SaidaItemAliquota>();
			float vlTotalItens     = 0;
			float vlTotalDocumento = 0;
			float vlTotalAcrescimo = 0;
			float vlTotalDesconto  = 0;
			for (int i=0; itens!=null && i<itens.size(); i++) {
				DocumentoEntradaItem itemEntrada = DocumentoEntradaItemDAO.get(cdDocumentoEntrada, itens.get(i).getCdProdutoServico(), itens.get(i).getCdEmpresa(), itens.get(i).getCdItemEntrada(), connection);
				pstmtDevolvido.setInt(1, cdDocumentoEntrada);
				pstmtDevolvido.setInt(2, itemEntrada.getCdProdutoServico());
				pstmtDevolvido.setInt(3, itemEntrada.getCdEmpresa());
				pstmtDevolvido.setInt(4, itemEntrada.getCdItem());
				ResultSetMap rsDevolvido = new ResultSetMap(pstmtDevolvido.executeQuery());
				rsDevolvido.next();
				if(itens.get(i).getQtDevolvida() > (itemEntrada.getQtEntrada()-rsDevolvido.getFloat("qt_devolvida")))	{
					ProdutoServico produto = ProdutoServicoDAO.get(itemEntrada.getCdProdutoServico(), connection);
					return new Result(-1, "A quantidade a ser devolvida do produto \""+produto.getNmProdutoServico()+"\" é invalida! " +
							              " Quantidade da entrada original: "+itemEntrada.getQtEntrada()+
							              ", ja devolvido: "+rsDevolvido.getFloat("qt_devolvida"));
				}
				float vlDescontoItem  = itemEntrada.getVlDesconto() / itemEntrada.getQtEntrada() * itens.get(i).getQtDevolvida();
				float vlAcrescimoItem = itemEntrada.getVlAcrescimo() / itemEntrada.getQtEntrada() * itens.get(i).getQtDevolvida();
				itensSaida.add(new DocumentoSaidaItem(0, itens.get(i).getCdProdutoServico(), itens.get(i).getCdEmpresa(),
						                                  itens.get(i).getQtDevolvida() /*qtSaida*/, itemEntrada.getVlUnitario(),
						                                  vlAcrescimoItem, vlDescontoItem, null /*dtEntregaPrevista*/,
						                                  itens.get(i).getCdUnidadeMedida(), 0/*cdTabelaPreco*/, itens.get(i).getCdItem(), 0/*cdBico*/));
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_documento_entrada", "" + itemEntrada.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_produto_servico", "" + itemEntrada.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_item", "" + itemEntrada.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_empresa", "" + itemEntrada.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
				
				ResultSetMap rsm = EntradaItemAliquotaDAO.find(criterios, connection);
				while(rsm.next()){
					itensSaidaAliquota.add(new SaidaItemAliquota(rsm.getInt("cd_produto_servico"), 0, rsm.getInt("cd_empresa"), rsm.getInt("cd_tributo_aliquota"), 
																	 rsm.getInt("cd_tributo"), rsm.getFloat("pr_aliquota"), rsm.getFloat("vl_base_calculo"), 
																	 0));
				}
				
				vlTotalDocumento += (itemEntrada.getVlUnitario() * itens.get(i).getQtDevolvida()) - vlDescontoItem + vlAcrescimoItem;
				vlTotalAcrescimo += vlAcrescimoItem;
				vlTotalDesconto  += vlDescontoItem;
				vlTotalItens     += (itemEntrada.getVlUnitario() * itens.get(i).getQtDevolvida());
			}
			/*
			 * Salvando o documento de saida
			 */
			String nrDocumentoSaida   = DocumentoSaidaServices.getProximoNrDocumento(docEntrada.getCdEmpresa(), connection);
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_tabela_preco", "" + docEntrada.getCdTabelaPreco(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("st_tipo_operacao", "1", ItemComparator.EQUAL, Types.INTEGER));
			int cdTipoOperacao = 0;
			ResultSetMap rsmTipoOperacao = TipoOperacaoDAO.find(criterios, connection);
			if(rsmTipoOperacao.next())
				cdTipoOperacao = rsmTipoOperacao.getInt("cd_tipo_operacao");
			if(cdTipoOperacao==0)
				cdTipoOperacao = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0, docEntrada.getCdEmpresa());
			DocumentoSaida docSaida = new DocumentoSaida(0 /*cdDocumentoSaida*/, 0 /*cdTransportadora*/, docEntrada.getCdEmpresa(), 
															   docEntrada.getCdFornecedor() /*cdFornecedor*/,
															   new GregorianCalendar() /*dtDocumentoSaida*/, DocumentoSaidaServices.ST_EM_CONFERENCIA /*stDocumentoSaida*/,nrDocumentoSaida, 
															   DocumentoSaidaServices.TP_DOC_NAO_FISCAL /*tpDocumentoSaida*/,DocumentoSaidaServices.SAI_DEVOLUCAO /*tpSaida*/,"" /*nrConhecimento*/, 
															   vlTotalDesconto, vlTotalAcrescimo, new GregorianCalendar() /*dtEmissao*/, 0 /*vlFrete*/, ""/*txtMensagem*/, ""/*txtObservacao*/, 
															   "" /*nrPlacaVeiculo*/, "" /*sgPlacaVeiculo*/, null /*nrVolumes*/, null /*dtSaidaTransportadora*/,
															   "" /*dsViaTransporte*/, cdNaturezaOperacaoDevolucao, "" /*txtCorpoNotaFiscal*/, 0 /*vlPesoLiquido*/,0 /*vlPresoBruto*/,
															   "" /*dsEspecieVolumes*/, "" /*dsMarcaVolumes*/, 0 /*qtVolumes*/, DocumentoSaidaServices.MOV_ESTOQUE_NAO_CONSIGNADO /*tpMovimentacao*/,
															   0/*cdVendedor*/, 0 /*cdMoeda*/, 0/*cdReferencialEcf*/, 0/*cdSolicitacaoMaterial*/, cdTipoOperacao, vlTotalDocumento /*vlTotalDocumento*/, 
															   0/*cdContrato*/, 0/*vlFrete*/, 0 /*vlSeguro*/, cdDigitador, 0/*cdDocumento*/, 0/*cdConta*/, 0/*cdTurno*/,  vlTotalItens, 1 /*nrSerie*/, 
															   0/*cdViagem*/, docEntrada.getCdDocumentoEntrada());
			int cdDocumentoSaida = DocumentoSaidaServices.insert(docSaida, 0, connection).getCode();
			if (cdDocumentoSaida<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao tentar criar o documento de devolucao!");
			}
			/*
			 * Salvando os itens
			 */
			for (int i=0; i<itensSaida.size(); i++) {
				
				itensSaida.get(i).setCdDocumentoSaida(cdDocumentoSaida);
				int code = DocumentoSaidaItemServices.insert(itensSaida.get(i), cdLocalArmazenamento, connection).getCode();
				if (code <= 0) {
					if (isConnectionNull)	{
						Conexao.rollback(connection);
						return new Result(-1, "Erro ao incluir item na devolucao(saida)!");
					}
				}
				itens.get(i).setCdDocumentoSaida(cdDocumentoSaida);
				itens.get(i).setCdItem(code);
				if (DevolucaoItemDAO.insert(itens.get(i), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao registrar a devolucao do item!");
				}
				
				for (int j=0; j<itensSaidaAliquota.size(); j++) {
					if(itensSaida.get(i).getCdProdutoServico() == itensSaidaAliquota.get(j).getCdProdutoServico()){
						itensSaidaAliquota.get(j).setCdDocumentoSaida(cdDocumentoSaida);
						itensSaidaAliquota.get(j).setCdItem(itensSaida.get(i).getCdItem());
						if (SaidaItemAliquotaDAO.insert(itensSaidaAliquota.get(j), connection) <= 0) {
							if (isConnectionNull)	{
								Conexao.rollback(connection);
								return new Result(-1, "Erro ao incluir item aliquota na devolucao(saida)!");
							}
						}
					}
				}
				
				
				
			}
			/*
			 * Liberando a saida
			 */
//			Result result = DocumentoSaidaServices.liberarSaida(cdDocumentoSaida, cdLocalArmazenamento, connection);
//			if (result.getCode() <= 0) {
//				if (isConnectionNull)
//					Conexao.rollback(connection);
//				result.setMessage("Falha ao liberar devolucao! "+result.getMessage());
//				return result;
//			}
			/*
			 * Gerando conta a pagar
			 */
//			float vlCredito = vlTotalDocumento; 
//			ContaPagar contaPagar = new ContaPagar(0,0/*cdContrato*/, docSaida.getCdFornecedor(), docSaida.getCdEmpresa(), 0/*cdContaOrigem*/,
//					                          		cdDocumentoSaida, cdConta, 0/*cdContaBancaria*/, new GregorianCalendar()/*Vencimento*/, 
//					                          		new GregorianCalendar()/*Emissão*/, null /*dtPagamento*/, new GregorianCalendar()/*Autorizada*/,
//					                          		nrDocumentoSaida, ""/*nrReferencia*/, 1 /*nrParcela*/, cdTipoDocumentoDevolucao, vlCredito, 
//					                          		0/*vlTotalDesconto*/, 0/*vlTotalAcrescimo*/, 0 /*vlPago*/, "Crédito do Fornecedor (Devolução)", 
//					                          		ContaPagarServices.ST_EM_ABERTO, 1/*lgAutorizado*/, 0/*tpFrequencia*/, 1 /*qtParc*/, 
//					                          		0 /*vlBaseAutorizacao*/, 0 /*cdViagem*/, 0 /*cdManutencao*/, "" /*txtObservacao*/, 
//					                          		new GregorianCalendar()/*dtCadastro*/, new GregorianCalendar()/*Vencimento Original*/, 0/*cdTurno*/);
//			
//			ArrayList<ContaPagarCategoria> categorias = new ArrayList<ContaPagarCategoria>();
//			categorias.add(new ContaPagarCategoria(0/*cdContaPagar*/,cdCategoriaDevolucao, vlTotalDocumento, 0));
//			result = ContaPagarServices.insert(contaPagar, true, true, categorias, connection);
//			if(result.getCode()<=0)	{
//				if (isConnectionNull)
//					Conexao.rollback(connection);
//				result.setMessage("Erro ao gerar credito de troca: "+result.getMessage());
//				return result;
//			}
			
			
			/*
			 * Emissão de Nota Fiscal Eletrônica
			 */
//			criterios = new ArrayList<ItemComparator>();
//			criterios.add(new ItemComparator("cd_documento_entrada", "" + cdDocumentoEntrada, ItemComparator.EQUAL, Types.INTEGER));
//			ResultSetMap rsmDocVinculado = NotaFiscalDocVinculadoDAO.find(criterios, connection);
//			if(rsmDocVinculado.next()){
//				NotaFiscal notaFiscalOrigem = NotaFiscalDAO.get(rsmDocVinculado.getInt("cd_nota_fiscal"), connection);
//				
//				int nrNotaFiscal = 0;
//				if(ParametroServices.getValorOfParametroAsInteger("LG_NUMERACAO_PARAMETRO", 0, notaFiscalOrigem.getCdEmpresa())!=1 && ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, notaFiscalOrigem.getCdEmpresa())==0){
//					ResultSetMap rsmNrNota = new ResultSetMap(connection.prepareStatement("SELECT * FROM FSC_NOTA_FISCAL " +
//																					"		WHERE CD_EMPRESA = " + notaFiscalOrigem.getCdEmpresa() + 
//																					"		AND NR_SERIE = '" + (NotaFiscalServices.getTipoAmbiente(notaFiscalOrigem.getCdEmpresa()) == NotaFiscalServices.HOMOLOGACAO ? "888" : ParametroServices.getValorOfParametro("NR_SERIE", "1", notaFiscalOrigem.getCdEmpresa())) + "'" +  
//																					"		ORDER BY CD_NOTA_FISCAL DESC LIMIT 1").executeQuery());
//					if(rsmNrNota.next()){
//						nrNotaFiscal = Integer.parseInt(rsmNrNota.getString("NR_NOTA_FISCAL")) + 1;
//						ParametroServices.updateValueOfParametro(ParametroServices.getByName("NR_NOTA_FISCAL").getCdParametro(), String.valueOf(nrNotaFiscal), notaFiscalOrigem.getCdEmpresa(), connection);
//					}
//					else{
//						nrNotaFiscal = ParametroServices.getValorOfParametroAsInteger("NR_NOTA_FISCAL", 1, notaFiscalOrigem.getCdEmpresa(), connection) + 1;
//						ParametroServices.updateValueOfParametro(ParametroServices.getByName("NR_NOTA_FISCAL").getCdParametro(), String.valueOf(nrNotaFiscal), notaFiscalOrigem.getCdEmpresa(), connection);
//					}
//				}
//				else if(ParametroServices.getValorOfParametroAsInteger("LG_NUMERACAO_PARAMETRO", 0, notaFiscalOrigem.getCdEmpresa())==1 && ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, notaFiscalOrigem.getCdEmpresa())==0){
//					nrNotaFiscal = ParametroServices.getValorOfParametroAsInteger("NR_NOTA_FISCAL", 1, notaFiscalOrigem.getCdEmpresa(), connection) + 1;
//					ParametroServices.updateValueOfParametro(ParametroServices.getByName("NR_NOTA_FISCAL").getCdParametro(), String.valueOf(nrNotaFiscal), notaFiscalOrigem.getCdEmpresa(), connection);
//				}
//				
//				int cdNaturezaOperacaoNota = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR", 0);
//				NaturezaOperacao natOperacaoOrigem = NaturezaOperacaoDAO.get(docEntrada.getCdNaturezaOperacao(), connection);
//				
//				
//				ResultSetMap rsmEnderecoDest = PessoaServices.getEnderecoPrincipalOfPessoa(notaFiscalOrigem.getCdDestinatario(), connection);
//				
//				if(rsmEnderecoDest.next() && EmpresaServices.getEstadoOfEmpresa(notaFiscalOrigem.getCdEmpresa(), connection) != CidadeDAO.get(rsmEnderecoDest.getInt("cd_cidade"), connection).getCdEstado()){
//					cdNaturezaOperacaoNota = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR_OUTRO_ESTADO", 0);
//					
//					if(natOperacaoOrigem.getNrCodigoFiscal().substring(0, 2).equals("24")){
//						cdNaturezaOperacaoNota = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR_SUBSTITUTO_OUTRO_ESTADO", 0);
//					}
//				}
//				else{
//					if(natOperacaoOrigem.getNrCodigoFiscal().substring(0, 2).equals("14")){
//						cdNaturezaOperacaoNota = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR_SUBSTITUTO", 0);
//					}
//				}
//
//				//Replicando Nota Fiscal
//				NotaFiscal notaFiscalDevolucao = new NotaFiscal(0, notaFiscalOrigem.getCdEmpresa(), notaFiscalOrigem.getCdEnderecoRetirada(), cdNaturezaOperacaoNota,
//						notaFiscalOrigem.getCdCidade(), notaFiscalOrigem.getCdDestinatario(), notaFiscalOrigem.getCdEnderecoDestinatario(), notaFiscalOrigem.getCdEnderecoEntrega(), notaFiscalOrigem.getCdLote(), 
//						notaFiscalOrigem.getTpModelo(), notaFiscalOrigem.getNrSerie(), (nrNotaFiscal == 0 ? null : String.valueOf(nrNotaFiscal)), NotaFiscalServices.EM_DIGITACAO, Util.getDataAtual(), 
//						NotaFiscalServices.MOV_SAIDA, Util.getDataAtual(), notaFiscalOrigem.getTpPagamento(), NotaFiscalServices.EMI_NORMAL, NotaFiscalServices.NFE_DE_DEVOLUCAO, NotaFiscalServices.RETRATO, 0/*vlTotalProduto*/, 
//						notaFiscalOrigem.getVlSeguro()/*vlSeguro*/, notaFiscalOrigem.getVlOutrasDespesas()/*vlOutrasDespesas*/, 0/*vlTotalNota*/, NotaFiscalServices.SEM_FRETE, null/*txtObservacao*/, null/*txtInformacaoFisco*/, NotaFiscalServices.RETRATO, null/*nrChaveAcesso*/, 0/*nrDv*/, 
//						null/*nrProtocoloAutorizacao*/, null/*dtAutorizacao*/, 0/*cdTransportador*/, 0/*cdNaturezaOperacaoFrete*/, 0/*vlFrete*/, 0/*vlFreteBaseIcms*/, notaFiscalOrigem.getVlFreteIcmsRetido()/*vlFreteIcmsRetido*/, null/*nrRecebimento*/, null/*nrPlaca*/, 
//						notaFiscalOrigem.getQtVolume()/*qtVolume*/, notaFiscalOrigem.getDsEspecie()/*dsEspecie*/, null/*dsMarca*/, null/*dsNumeracao*/, notaFiscalOrigem.getVlPesoBruto()/*vlPesoBruto*/, notaFiscalOrigem.getVlPesoLiquido()/*vlPesoLiquido*/, 0/*cdVeiculo*/, null/*sgUfVeiculo*/, null/*nrRntc*/, 0/*cdMotivoCancelamento*/, null/*txtXml*/, notaFiscalOrigem.getPrDesconto(), 1/*lgConsumidorFinal*/, 1/*tpVendaPresenca*/, notaFiscalOrigem.getNrChaveAcesso().substring(3));
//				
//				int cdNotaFiscalDevolucao = NotaFiscalDAO.insert(notaFiscalDevolucao, connection);
//				if(cdNotaFiscalDevolucao <= 0){
//					if(isConnectionNull)
//						Conexao.rollback(connection);
//					return new Result(-1, "Erro ao inserir nota fiscal de devolução");
//				}
//				notaFiscalDevolucao.setCdNotaFiscal(cdNotaFiscalDevolucao);
//				
//				// GERANDO ID - NFE
//				if(notaFiscalDevolucao.getNrNotaFiscal() != null){
//					Result result = NotaFiscalServices.gerarNfeId(notaFiscalDevolucao);
//					if(result.getCode() <= 0)	{
//						Conexao.rollback(connection);
//						result.setMessage(result.getMessage()+" Falha ao tentar gerar ID da Nota Fiscal.");
//						return result;
//					}
//					notaFiscalDevolucao.setNrChaveAcesso((String)result.getObjects().get("nrChave"));
//					notaFiscalDevolucao.setNrDv((Integer)result.getObjects().get("nrDv"));
//				}
//				if(NotaFiscalDAO.update(notaFiscalDevolucao, connection) <= 0)	{
//					Conexao.rollback(connection);
//					return new Result(-1, "Falha ao atualizar ID da NFE");
//				}
//				//Replicando Nota Fiscal Tributo
//				ArrayList<NotaFiscalTributo> conjNotaFiscalTributo = NotaFiscalTributoServices.getAllTributo(notaFiscalOrigem.getCdNotaFiscal());
//				for(NotaFiscalTributo notaFiscalTributoOrigem : conjNotaFiscalTributo){
//					NotaFiscalTributo notaFiscalTributoDevolucao = new NotaFiscalTributo(cdNotaFiscalDevolucao, notaFiscalTributoOrigem.getCdTributo(), notaFiscalTributoOrigem.getVlBaseCalculo(), notaFiscalTributoOrigem.getVlOutrasDespesas(), 
//																						notaFiscalTributoOrigem.getVlOutrosImpostos(), notaFiscalTributoOrigem.getVlTributo(), notaFiscalTributoOrigem.getVlBaseRetencao(), notaFiscalTributoOrigem.getVlRetido());
//					
//					if(NotaFiscalTributoDAO.insert(notaFiscalTributoDevolucao, connection) <= 0){
//						if(isConnectionNull)
//							Conexao.rollback(connection);
//						return new Result(-1, "Erro ao inserir nota fiscal tributo");
//					}
//					
//				}
//				
//				ResultSetMap rsmItensNotaFiscalOrigem = NotaFiscalServices.getAllItens(notaFiscalOrigem.getCdNotaFiscal(), connection);
//				ArrayList<Integer> itensJaVistos = new ArrayList<Integer>();
//				int cdItem = 1;
//				while(rsmItensNotaFiscalOrigem.next()){
//					for (int i=0; i<itensSaida.size(); i++) {
//						if(rsmItensNotaFiscalOrigem.getInt("cd_produto_servico") == itensSaida.get(i).getCdProdutoServico() && 
//						  !itensJaVistos.contains(itensSaida.get(i).getCdProdutoServico())){
//							
//							int cdNaturezaOperacaoItem = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR", 0);
//							
//							SituacaoTributaria situacaoTributariaOrigem = SituacaoTributariaDAO.get(ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0), Integer.parseInt(rsmItensNotaFiscalOrigem.getString("cd_situacao_tributaria_icms")), connection);
//							
//							if(EmpresaServices.getEstadoOfEmpresa(notaFiscalOrigem.getCdEmpresa(), connection) != CidadeDAO.get(rsmEnderecoDest.getInt("cd_cidade"), connection).getCdEstado()){
//								
//								cdNaturezaOperacaoItem = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR_OUTRO_ESTADO", 0);
//								if(situacaoTributariaOrigem != null && situacaoTributariaOrigem.getLgSubstituicao()==1){
//									cdNaturezaOperacaoItem = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR_SUBSTITUTO_OUTRO_ESTADO", 0);
//								}
//							}
//							else{
//								if(situacaoTributariaOrigem != null && situacaoTributariaOrigem.getLgSubstituicao()==1){
//									cdNaturezaOperacaoItem = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR_SUBSTITUTO", 0);
//								}
//							}
//							
//							//Replicando Nota Fiscal Item
//							NotaFiscalItem notaFiscalItemDevolucao = new NotaFiscalItem(cdNotaFiscalDevolucao, cdItem, 0, itensSaida.get(i).getCdProdutoServico(), rsmItensNotaFiscalOrigem.getInt("cd_empresa"), 
//																						cdDocumentoEntrada, cdNaturezaOperacaoItem, itensSaida.get(i).getQtSaida(), 
//																						rsmItensNotaFiscalOrigem.getFloat("vl_unitario"), rsmItensNotaFiscalOrigem.getString("txt_informacao_adicional"), itensSaida.get(i).getCdItem(), 
//																						rsmItensNotaFiscalOrigem.getFloat("vl_acrescimo"), rsmItensNotaFiscalOrigem.getFloat("vl_desconto"));
//							
//							if(NotaFiscalItemDAO.insert(notaFiscalItemDevolucao, connection) <= 0){
//								if(isConnectionNull)
//									Conexao.rollback(connection);
//								return new Result(-1, "Erro ao inserir item de nota fiscal de devolução");
//							}
//									
//							//Replicando Nota Fiscal Item Tributo
//							ArrayList<NotaFiscalItemTributo> conjNotaFiscalItemTributo = NotaFiscalItemTributoServices.getAllItemTributo(notaFiscalOrigem.getCdNotaFiscal(), rsmItensNotaFiscalOrigem.getInt("cd_item"), connection);
//							for(NotaFiscalItemTributo notaFiscalItemTributo : conjNotaFiscalItemTributo){
//								NotaFiscalItemTributo notaFiscalItemTributoDevolucao = new NotaFiscalItemTributo(cdNotaFiscalDevolucao, cdItem, notaFiscalItemTributo.getCdTributo(), notaFiscalItemTributo.getCdTributoAliquota(),
//										notaFiscalItemTributo.getTpRegimeTributario(), notaFiscalItemTributo.getTpOrigem(), notaFiscalItemTributo.getTpCalculo(), (notaFiscalItemDevolucao.getVlUnitario() * notaFiscalItemDevolucao.getQtTributario() + notaFiscalItemDevolucao.getVlAcrescimo() - notaFiscalItemDevolucao.getVlDesconto()), 
//										notaFiscalItemTributo.getVlOutrasDespesas(), notaFiscalItemTributo.getVlOutrosImpostos(), notaFiscalItemTributo.getPrAliquota(), notaFiscalItemTributo.getVlTributo(), notaFiscalItemTributo.getPrCredito(), 
//										notaFiscalItemTributo.getVlCredito(), notaFiscalItemTributo.getNrClasse(), notaFiscalItemTributo.getNrEnquadramento(), notaFiscalItemTributo.getCdSituacaoTributaria(), notaFiscalItemTributo.getVlBaseRetencao(), notaFiscalItemTributo.getVlRetido());
//								
//								if(NotaFiscalItemTributoDAO.insert(notaFiscalItemTributoDevolucao, connection) <= 0){
//									if(isConnectionNull)
//										Conexao.rollback(connection);
//									return new Result(-1, "Erro ao inserir tributo do item de nota fiscal de devolução");
//								}
//								
//							}
//							
//							cdItem++;
//							itensJaVistos.add(itensSaida.get(i).getCdProdutoServico());
//							break;
//						}
//					}
//				}
//				NotaFiscalDocVinculado notaFiscalDocVinculadoDevolucao = new NotaFiscalDocVinculado(cdNotaFiscalDevolucao, 0, 0, 0, cdDocumentoEntrada, 1);
//				
//				if(NotaFiscalDocVinculadoDAO.insert(notaFiscalDocVinculadoDevolucao, connection) <= 0){
//					if(isConnectionNull)
//						Conexao.rollback(connection);
//					return new Result(-1, "Erro ao inserir documento vinculado");
//				}
//				
//			}
			
			if (isConnectionNull)
				connection.commit();
			Result result = new Result(cdDocumentoEntrada);
			result.addObject("vlCredito", vlTotalDocumento);
			result.addObject("nrCredito", nrDocumentoSaida);
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro desconhecido ao tentar registrar devolução!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}


}
