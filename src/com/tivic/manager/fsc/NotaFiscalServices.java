package com.tivic.manager.fsc;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.*;
import org.xml.sax.InputSource;

import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import sol.util.ConfManager;
import sol.util.Result;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.mail.SMTPClient;

import com.tivic.manager.adm.EntradaEventoFinanceiro;
import com.tivic.manager.adm.EntradaEventoFinanceiroDAO;
import com.tivic.manager.adm.EntradaItemAliquota;
import com.tivic.manager.adm.EntradaItemAliquotaDAO;
import com.tivic.manager.adm.EntradaItemAliquotaServices;
import com.tivic.manager.adm.EventoFinanceiro;
import com.tivic.manager.adm.EventoFinanceiroDAO;
import com.tivic.manager.adm.NaturezaOperacao;
import com.tivic.manager.adm.NaturezaOperacaoDAO;
import com.tivic.manager.adm.SaidaTributo;
import com.tivic.manager.adm.SaidaTributoDAO;
import com.tivic.manager.adm.Tributo;
import com.tivic.manager.adm.TributoAliquota;
import com.tivic.manager.adm.TributoAliquotaDAO;
import com.tivic.manager.adm.TributoAliquotaServices;
import com.tivic.manager.adm.TributoDAO;
import com.tivic.manager.adm.TributoServices;
import com.tivic.manager.alm.*;
import com.tivic.manager.fta.Viagem;
import com.tivic.manager.fta.ViagemDAO;
import com.tivic.manager.grl.*;
import com.tivic.manager.mob.Concessao;
import com.tivic.manager.mob.ConcessaoDAO;
import com.tivic.manager.mob.ConcessaoLoteNotaFiscal;
import com.tivic.manager.mob.ConcessaoLoteNotaFiscalDAO;
import com.tivic.manager.mob.ConcessaoLoteNotaFiscalServices;
import com.tivic.manager.mob.ConcessaoLoteServices;
import com.tivic.sol.report.ReportServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.seg.CertificadoServices;
import com.tivic.manager.util.ContextManager;
import com.tivic.manager.util.Util;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conexao.*;

public class NotaFiscalServices {
	/* Erro de dados do cliente ao enviar a nota fiscal*/
	private static final int ERROR_ENDERECO = -31;
	private static final int ERROR_TELEFONE = -30;
	/*TIPO DE AMBIENTE*/
	public static final int PRODUCAO    = 1;
	public static final int HOMOLOGACAO = 2;
	
	/*TIPO DE DOCUMENTO*/
	public static final int ENTRADA = 0;
	public static final int SAIDA   = 1;	
	public static final int MOV_ENTRADA = 0;
	public static final int MOV_SAIDA   = 1;	

	/*SITUACAO*/
	public static final int EM_DIGITACAO = 1;
	public static final int VALIDADA     = 2;
	public static final int TRANSMITIDA  = 3;
	public static final int AUTORIZADA   = 4;
	public static final int CANCELADA    = 5;
	public static final int DENEGADA     = 6;
	
	public static final int QUALQUER        = 0;
	public static final int ST_EM_DIGITACAO = 1;
	public static final int ST_VALIDADA     = 2;
	public static final int ST_TRANSMITIDA  = 3;
	public static final int ST_AUTORIZADA   = 4;
	public static final int ST_CANCELADA    = 5;
	public static final int ST_DENEGADA     = 6;
	public static final int ST_FORA_DO_SISTEMA = 7;//Notas fiscais que estão na base da SEFAZ mas não estão cadastradas no sistema
	
	public static String[] situacaoNotaFiscal = {"Qualquer", "Em Digitação", "Validada", "Transmitida", "Autorizada", "Cancelada", "Denegada"};
	
	/*FORMA DE EMISSAO*/
	public static final int EMI_NORMAL                 = 1;
	public static final int EMI_CONTIGENCIA_FS         = 2;
	public static final int EMI_CONTIGENCIA_COM_SCAN   = 3;
	public static final int EMI_CONTIGENCIA_VIA_EPEC   = 4;
	public static final int EMI_CONTIGENCIA_FSDA       = 5;
	public static final int EMI_CONTIGENCIA_SVC_AN     = 6;
	public static final int EMI_CONTIGENCIA_SVC_RS     = 7;
	public static final int EMI_CONTIGENCIA_OFF_NFCE   = 9;
	
	public static String[] tiposNotaFiscal = {"Qualquer", "Normal", "Contingência via FS", "Contingência com Scan", "Contingência via EPEC", "Contignência FSDA", "Contingencia SVC-AN", "Contingencia SVC-RS", "", "Contingência off-line NFC-e"};
	
	/*TIPO DE FINALIDADE*/
	public static final int NFE_NORMAL       = 1;
	public static final int NFE_COMPLEMENTAR = 2;
	public static final int NFE_DE_AJUSTE    = 3;
	public static final int NFE_DE_DEVOLUCAO = 4;
	
	/*TIPO DE DANFE*/
	public static final int RETRATO  = 1;
	public static final int PAISAGEM = 2;
	public static final int DANFE_RETRATO  = 1;
	public static final int DANFE_PAISAGEM = 2;
	
	/*MODALIDADE DE FRETE*/
	public static final int POR_CONTA_DO_EMITENTE     = 0;
	public static final int POR_CONTA_DO_DESTINATARIO = 1;
	public static final int POR_CONTA_DE_TERCEIROS    = 2;
	public static final int SEM_FRETE                 = 9;
	public static final int FRT_POR_CONTA_DO_EMITENTE     = 0;
	public static final int FRT_POR_CONTA_DO_DESTINATARIO = 1;
	public static final int FRT_POR_CONTA_DE_TERCEIROS    = 2;
	public static final int FRT_SEM_FRETE                 = 9;
	
	/*FORMA DE PAGAMENTO DE FRETE*/
	public static final int A_VISTA = 0;
	public static final int A_PRAZO = 1;
	public static final int OUTROS  = 2;
		
	/*TIPO DE AMBIENTE*/
	private static int tpAmbiente = -1;
	public static int getTipoAmbiente(int cdEmpresa)	{
		return getTipoAmbiente(cdEmpresa, null);
	}
	
	public static int getTipoAmbiente(int cdEmpresa, Connection connection)	{
		if(tpAmbiente==-1)
			tpAmbiente = ParametroServices.getValorOfParametroAsInteger("TP_AMBIENTE", 1/*PRODUCAO*/, cdEmpresa, connection == null ? Conexao.conectar() : connection);
		return tpAmbiente;
	}
	
	
	
	/*TIPO DE DESCONTO*/
	public static int NO_PRECO = 0;
	public static int NO_ITEM  = 1;
	
	/*Indicador de Presença da Venda*/
	public static int TP_PRESENCA_NAO_APLICA = 0; /*Não se aplica (por exemplo, para a Nota Fiscal complementar ou de ajuste)*/
	public static int TP_PRESENCA_PRESENCIAL = 1; /*Operação presencial*/
	public static int TP_PRESENCA_INTERNET   = 2; /*Operação não presencial, pela Internet*/
	public static int TP_PRESENCA_TELEATENDIMENTO = 3; /*Operação não presencial, Teleatendimento*/
	public static int TP_PRESENCA_NFCE_EM_DOMICILIO = 4; /*NFC-e em operação com entrega em domicílio*/
	public static int TP_PRESENCA_NAO_PRESENCIAL_OUTROS = 9; /*Operação não presencial, outros*/
	
	public static final String[] tiposPresenca = {"Não se aplica", "Presencial", "Internet", "Teleatendimento", "NFC-e, entrega em domicílio", "Não presencial - Outros"};
	
	public static String[] tiposDesconto = {"No Preço", "No Item"};
	
	public static Result save(NotaFiscal notaFiscal){
		return save(notaFiscal, 0, null, null, null);
	}
	
	public static Result save(NotaFiscal notaFiscal, int cdRecebimentoNotaFiscal, ArrayList<ConcessaoLoteNotaFiscal> concessoesLoteNotaFiscal){
		return save(notaFiscal, cdRecebimentoNotaFiscal, concessoesLoteNotaFiscal, null, null);
	}

	public static Result save(NotaFiscal notaFiscal, AuthData authData){
		return save(notaFiscal, 0, null, authData, null);
	}

	public static Result save(NotaFiscal notaFiscal, int cdRecebimentoNotaFiscal, ArrayList<ConcessaoLoteNotaFiscal> concessoesLoteNotaFiscal, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(notaFiscal==null)
				return new Result(-1, "Erro ao salvar. NotaFiscal é nulo");

			int retorno;
			if(notaFiscal.getCdNotaFiscal()==0){
				retorno = NotaFiscalDAO.insert(notaFiscal, connect);
				notaFiscal.setCdNotaFiscal(retorno);
			}
			else {
				retorno = NotaFiscalDAO.update(notaFiscal, connect);
			}
			
			if(cdRecebimentoNotaFiscal > 0){
				RecebimentoNotaFiscal recebimentoNotaFiscal = RecebimentoNotaFiscalDAO.get(cdRecebimentoNotaFiscal, connect);
				recebimentoNotaFiscal.setCdNotaFiscal(notaFiscal.getCdNotaFiscal());
				if(RecebimentoNotaFiscalDAO.update(recebimentoNotaFiscal, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar recebimento de nota fiscal");
				}
				
				if(concessoesLoteNotaFiscal != null && concessoesLoteNotaFiscal.size() == 0){
					Concessao concessao = ConcessaoDAO.get(recebimentoNotaFiscal.getCdConcessao(), connect);
					
					ResultSetMap rsmConcessaoLote = ConcessaoLoteServices.getAllByConcessao(concessao.getCdConcessao(), connect);
					while(rsmConcessaoLote.next()){
						ConcessaoLoteNotaFiscal concessaoLoteNotaFiscal = new ConcessaoLoteNotaFiscal(rsmConcessaoLote.getInt("cd_concessao_lote"), rsmConcessaoLote.getInt("cd_concessao"), notaFiscal.getCdNotaFiscal(), 0, 0F);
						int ret = ConcessaoLoteNotaFiscalDAO.insert(concessaoLoteNotaFiscal, connect);
						if(ret < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(ret, "Erro ao inserir concessao lote nota fiscal");
						}
					}
				}
				else if(concessoesLoteNotaFiscal != null && concessoesLoteNotaFiscal.size() == 0){
					Result result = ConcessaoLoteNotaFiscalServices.removeAllByNotaFiscal(notaFiscal.getCdNotaFiscal(), connect);
					if(result.getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return result;
					}
					
					for(ConcessaoLoteNotaFiscal concessaoLoteNotaFiscal : concessoesLoteNotaFiscal){
						int ret = ConcessaoLoteNotaFiscalDAO.insert(concessaoLoteNotaFiscal, connect);
						if(ret < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(ret, "Erro ao inserir concessão lote nota fiscal");
						}
					}
				}
				
			}
				
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "NOTAFISCAL", notaFiscal);
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
	
    /**
	 * Retorna um Document
	 */
	public static Result loadFromXML(int cdEmpresa, String fileName) {
		try {
			System.out.println("loadFromXML");
			fileName   = "C:/TIVIC/Dropbox/projetos/manager/web/nfe/29120910616123000105550010000056961000663211-NFe.xml";
			
			SAXBuilder saxObj = new SAXBuilder();
			Document doc      = saxObj.build(fileName);
			
			Namespace ns      = Namespace.getNamespace("http://www.portalfiscal.inf.br/nfe");
	    	Element infNFe    = (Element)doc.getRootElement().getChild("NFe", ns).getChild("infNFe", ns);
			//
			// String idNfe        = infNFe.getAttributeValue("Id");     
            // String versaoNfe    = infNFe.getAttributeValue("versao");
	    	// Identificação da NFe
            Element ide     	= (Element) infNFe.getChild("ide", ns);
            if(ide!=null)	{
            		
            }
            // Emissor
			Element emissor = (Element) infNFe.getChild("emit", ns);
			if(emissor!=null)	{
				
			}
            // Destinatário
			Element dest  = (Element) infNFe.getChild("dest", ns);
			if(dest!=null)	{
				// String nrCpfCnpjDest = dest.getChild("CNPJ", ns).getText();
			}
			List<?> itens = infNFe.getChildren("det", ns);
			/*
			 * IMPORTAÇÃO
			 */
			/***********************
			 * Importando produtos
			 ***********************/
			Connection connect = Conexao.conectar();
			if(connect==null)
				System.out.println("Conexão nula");
			PreparedStatement pesqProdutoServico = connect.prepareStatement("SELECT * FROM grl_produto_servico A, grl_produto_servico_empresa B " +
					                                                        "WHERE (id_produto_servico = ? OR id_reduzido = ?)  "+
																			"  AND A.cd_produto_servico = B.cd_produto_servico  ");
			PreparedStatement pesqNcm  			 = connect.prepareStatement("SELECT * FROM grl_ncm WHERE nr_ncm = ?");
			PreparedStatement pesqUnidade  		 = connect.prepareStatement("SELECT * FROM grl_unidade_medida WHERE sg_unidade_medida = ?");
			PreparedStatement pesqGrupo    		 = connect.prepareStatement("SELECT * FROM alm_grupo WHERE id_grupo = ?");
			PreparedStatement updProduto   		 = connect.prepareStatement("UPDATE grl_produto_servico SET cd_ncm = ?, id_produto_servico=? WHERE cd_produto_servico = ?");
			PreparedStatement updProdutoEmpresa  = connect.prepareStatement("UPDATE grl_produto_servico_empresa " +
														                    "SET cd_unidade_medida = ?, id_reduzido = ? " +
														                    "WHERE cd_produto_servico = ? AND cd_empresa = ?");
			int inserts = 0, updates = 0;
			for(int i=0; i<itens.size(); i++)	{
				Element xlmProduto   = ((Element)itens.get(i)).getChild("prod", ns);
				//
				String idReduzido 	  = xlmProduto.getChildText("cProd", ns);
				String nrCodigoBarras = xlmProduto.getChildText("cEAN", ns);
				String nmProduto      = xlmProduto.getChildText("xProd", ns);
				String nrNcm 		  = xlmProduto.getChildText("NCM", ns);
				//String nrCFOP 		= xlmProduto.getChildText("CFOP", ns);
				String sgUnidade      = xlmProduto.getChildText("uCom", ns);
				//String qtEntrada      = xlmProduto.getChildText("qCom", ns);
				//String vlUnitario     = xlmProduto.getChildText("vUnCom", ns);
				String idGrupo        = "";
				if(idReduzido!=null)	{
					idReduzido = idReduzido.replaceAll("\\.", "-");
					if(idReduzido.indexOf('-')>0)
						idGrupo = idReduzido.substring(idReduzido.indexOf('-')+1);
					// idReduzido = idReduzido.replaceAll("-", "");
				}
				// Unidade de Medida
				int cdUnidadeMedida = 0;
				if (sgUnidade!=null)	{
					pesqUnidade.setString(1, sgUnidade.toUpperCase());
					ResultSet rsT = pesqUnidade.executeQuery();
					if(rsT.next())
						cdUnidadeMedida = rsT.getInt("cd_unidade_medida");
					else{
						com.tivic.manager.grl.UnidadeMedida unidadeMedida = new com.tivic.manager.grl.UnidadeMedida(0, sgUnidade, sgUnidade, null, 0, 1);
						cdUnidadeMedida = com.tivic.manager.grl.UnidadeMedidaDAO.insert(unidadeMedida, connect);
					}
				}
				// Grupo
				int cdGrupo = 0;
				if (!idGrupo.equals(""))	{
					pesqGrupo.setString(1, idGrupo);
					ResultSet rsT = pesqGrupo.executeQuery();
					if(rsT.next())
						cdGrupo = rsT.getInt("cd_grupo");
					else{
						com.tivic.manager.alm.Grupo grupo = new com.tivic.manager.alm.Grupo(0,0,0,0,"GRUPO "+idGrupo,0,0,0,1,idGrupo);
						cdGrupo = com.tivic.manager.alm.GrupoDAO.insert(grupo, connect);
					}
						
				}
				// NCM
				int cdNcm = 0;
				if (nrNcm!=null)	{
					pesqNcm.setString(1, nrNcm.toUpperCase());
					ResultSet rsN = pesqNcm.executeQuery();
					if(rsN.next())
						cdNcm = rsN.getInt("cd_ncm");
					else	{
						com.tivic.manager.grl.Ncm ncm = new com.tivic.manager.grl.Ncm(0, nrNcm, cdUnidadeMedida, nrNcm);
						cdNcm = com.tivic.manager.grl.NcmDAO.insert(ncm, connect);
					}
						
				}
				// Se a tabela não estava vazia no início, pesquisa o produto
				ResultSet rsProd = null;
				pesqProdutoServico.setString(1, nrCodigoBarras);
				pesqProdutoServico.setString(2, idReduzido);
				rsProd = pesqProdutoServico.executeQuery();
				// Incluindo
				if(!rsProd.next())	{
					inserts++;
					Produto produto = new Produto(0,0/*cdCategoriaEconomica*/, nmProduto, null /*txtProdutoServico*/,
												  null /*txtEspecificacao*/, null /*txtDadoTecnico*/, null /*txtPrazoEntrega*/,
												  ProdutoServicoServices.TP_PRODUTO, null, null/*sgProdutoServico*/,
												  0 /*cdClassificacaoFiscal*/, 0, 0/*cdMarca*/, null, cdNcm/*cdNcm*/, null/*NrReferenci*/,
												  0 /*vlPesoUnitario*/, 0 /*vlPesoUnitarioEmbalagem*/, 0 /*vlComprimento*/, 0 /*vlLargura*/,
												  0 /*vlAltura*/, 0 /*vlComprimentoEmbalagem*/, 0 /*vlLarguraEmbalagem*/, 
												  0 /*vlAlturaEmbalagem*/,
												  0 /*qtEmbalagem*/);
					int cdProdutoServico = ProdutoDAO.insert(produto, connect);

					ProdutoServicoEmpresa produtoEmpresa = new ProdutoServicoEmpresa(cdEmpresa,cdProdutoServico,cdUnidadeMedida,
							                                                         idReduzido,0/*vlPrecoMedio*/,
							                                                         0/*vlCustoMedio*/,
							                                                         0/*vlUltimoCusto*/,0/*qtIdeal*/,0/*qtMinima*/,0/*qtMaxima*/,0/*qtDiasEstoque*/,
							                                                         2 /*qtPrecisaoCusto*/, 0/*qtPrecisaoUnidade*/,0 /*qtDiasGarantia*/,
							                                                         ProdutoServicoEmpresaServices.TP_MANUAL,
							                                                         ProdutoServicoEmpresaServices.CTL_QUANTIDADE,
							                                                         0/*tpTransporte*/,1/*stProdutoEmpresa*/,null/*dtDesativacao*/,
							                                                         null/*nrOrdem*/,0/*lgEstoqueNegativo*/);
					ProdutoServicoEmpresaDAO.insert(produtoEmpresa, connect);

					if(cdGrupo > 0){
						ProdutoGrupo produtoGrupo = new ProdutoGrupo(cdProdutoServico,cdGrupo,cdEmpresa,1/*lgPrincipal*/);

						ProdutoGrupoDAO.insert(produtoGrupo, connect);
					}
				}
				else	{
					updates++;
					int cdProdutoServico = rsProd.getInt("cd_produto_servico");
					// Atualizando o produto
					updProduto.setInt(1, cdNcm);
					updProduto.setString(2, nrCodigoBarras);
					updProduto.setInt(3, cdProdutoServico);
					updProduto.executeUpdate();
					// Atualizando unidade de medida
					if (cdUnidadeMedida > 0) {
						updProdutoEmpresa.setInt(1, cdUnidadeMedida);
						updProdutoEmpresa.setString(2, idReduzido);
						updProdutoEmpresa.setInt(3, cdProdutoServico);
						updProdutoEmpresa.setInt(4, cdEmpresa);
						updProdutoEmpresa.executeUpdate();
					}
				}
			}
			
			System.out.println(inserts+"inserts, updates: "+updates+"!");
	    	return new Result(1, inserts+"inserts, updates: "+updates+"!");
	    }
	    catch(Exception pce)	{
	    	pce.printStackTrace(System.out);
	    	return new Result(-1, "Erro ao tentar carregar NF-e", pce);
	    }
	}

	/**
	 * Metodo para transferir os dados de uma nota de saída e transforma-la em uma NFe
	 * @throws SQLException 
	 * */	
	public static Result fromDocSaidaToNF(int cdDocumentosSaida, int tpDesconto, float prDesconto) {
		try{
			ArrayList<Integer> docs = new ArrayList<Integer>();
			docs.add(new Integer(cdDocumentosSaida));
			return fromDocSaidaToNF(tpDesconto, prDesconto, docs);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			Util.registerLog(e); // Registra o log quando chamado pelo PDV
			return new Result(-1, "Falha ao tentar enviar documento para a fila de validação!", e);
		}
	}
	
	/**
	 * Cria uma nota fiscal a partir de um documento de saída
	 * 
	 * @param cdDocumentosSaida
	 * @return
	 * @see #fromDocSaidaToNF(int, float, ArrayList, Connection)
	 */
	public static Result fromDocSaidaToNF(int cdDocumentosSaida) {
		try{
			ArrayList<Integer> docs = new ArrayList<Integer>();
			docs.add(new Integer(cdDocumentosSaida));
			return fromDocSaidaToNF(0, 0, docs);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			Util.registerLog(e); // Registra o log quando chamado pelo PDV
			return new Result(-1, "Falha ao tentar enviar documento para a fila de validação!", e);
		}
	}
	
	public static Result fromDocSaidaToNF(int cdDocumentosSaida, Connection connect) {
		try{
			ArrayList<Integer> docs = new ArrayList<Integer>();
			docs.add(new Integer(cdDocumentosSaida));
			return fromDocSaidaToNF(0, 0, docs, connect);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			Util.registerLog(e); // Registra o log quando chamado pelo PDV
			return new Result(-1, "Falha ao tentar enviar documento para a fila de validação!", e);
		}
	}
	
	public static Result fromDocSaidaToNFArray(ArrayList<HashMap<String,Object>> registros)	{
		try	 {
			ArrayList<Integer> documentosSaida = new ArrayList<Integer>();
			for(int i = 0; i < registros.size(); i++){
				int cdDocumentoSaida = registros.get(i)==null || registros.get(i).get("cdDocumentoSaida")==null ? 0 : (Integer) registros.get(i).get("cdDocumentoSaida");
				documentosSaida.add(cdDocumentoSaida);
			}
			Result retorno   = fromDocSaidaToNF(-1, -1, documentosSaida);
			
			return retorno;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e); // Registra o log quando chamado pelo PDV
			return new Result(-1, "Erro: " + e, e);
		}
	}
	
	public static Result fromDocSaidaToNF(int tpDesconto, float prDesconto, ArrayList<Integer> documentosSaida) {
		return fromDocSaidaToNF(tpDesconto, prDesconto, documentosSaida, null);
	}
	
	/**
	 * Cria uma nota fiscal a partir de vários documentos de saída
	 * 
	 * @param tpDesconto
	 * @param prDesconto
	 * @param documentosSaida
	 * @param connect
	 * @return
	 */
	public static Result fromDocSaidaToNF(int tpDesconto, float prDesconto, ArrayList<Integer> documentosSaida, Connection connect) {
		boolean isConnectionNull = connect == null; 
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			// Verifica se a lista é válida ou é vazia
			if(documentosSaida.size() == 0 || documentosSaida == null)
				return new Result(-1, "Array de códigos de documento vazio ou inexistente");
			
			int cdCliente          = 0;
			int cdEmpresa          = 0;
			int cdNaturezaOperacao = 0;
			int cdTransportador    = 0;	
			float vlSeguro         = 0;
			float vlFrete          = 0;
			float vlIcmsSt	       = 0;
			float vlImpostosDiretos= 0;
			String nrDocumentosVinculados = "";
			//
			ArrayList<DocumentoSaida> docs = new ArrayList<DocumentoSaida>(); 
			//Verificação para não deixar fazer uma nota fiscal de documentos com clientes ou empresas diferentes
			for(int i = 0; i < documentosSaida.size(); i++)	{
				DocumentoSaida doc = DocumentoSaidaDAO.get(documentosSaida.get(i), connect);
				// Verifica se os documentos são todos da mesma empresa
				if(cdEmpresa <= 0)
					cdEmpresa = doc.getCdEmpresa();
				else if(cdEmpresa != doc.getCdEmpresa())
					return new Result(-1, "O Documento No " +doc.getNrDocumentoSaida()+" não Ã© da mesma empresa dos demais!");
				// Verifica cliente
				if(cdCliente <= 0 && doc.getCdCliente() > 0)
					cdCliente = doc.getCdCliente();
				else if(cdCliente>0 && doc.getCdCliente()>0 && cdCliente != doc.getCdCliente())
					return new Result(-1, "O Documento No " +doc.getNrDocumentoSaida()+" não Ã© da mesma cliente dos demais!");
				//
				docs.add(doc);
				nrDocumentosVinculados += doc.getNrDocumentoSaida();
				if( (i+1) != documentosSaida.size()){
					nrDocumentosVinculados += ", ";
				}
				
			}
			
			// Busca os telefones do cliente para verificar se possui ao menos 1
			PreparedStatement pstmCliente = connect.prepareStatement("SELECT A.*,B.*, C.*, D.nr_telefone1 as contato_nr_telefone1 , D.nr_telefone2 as contato_nr_telefone2, "
																		+ " D.nr_celular as contato_nr_celular, D.nr_celular2 as contato_nr_celular2, D.nr_celular3 as contato_nr_celular3, "
																		+ " D.nr_celular4 as contato_nr_celular4 "
																		+ " FROM grl_pessoa A "
																		+ " LEFT JOIN grl_pessoa_endereco B ON B.cd_pessoa = A.cd_pessoa "
																		+ " LEFT JOIN grl_pessoa_contato D ON D.cd_pessoa = A.cd_pessoa "
																		+ " LEFT JOIN grl_cidade C ON C.cd_cidade = B.cd_cidade " 
																		+ " WHERE A.cd_pessoa = ? AND B.lg_principal = 1");
			pstmCliente.setInt(1, cdCliente);
			ResultSet rsCliente = pstmCliente.executeQuery();
			//Buscar os telefones do cliente
			if(rsCliente.next()){
				if(isNullOrBlank(rsCliente.getString("contato_nr_telefone1"))
						&& isNullOrBlank(rsCliente.getString("contato_nr_telefone2"))
						&& isNullOrBlank(rsCliente.getString("contato_nr_celular"))
						&& isNullOrBlank(rsCliente.getString("contato_nr_celular2"))
						&& isNullOrBlank(rsCliente.getString("contato_nr_celular3"))
						&& isNullOrBlank(rsCliente.getString("contato_nr_celular4"))
						&& isNullOrBlank(rsCliente.getString("nr_telefone1")) 
						&& isNullOrBlank(rsCliente.getString("nr_telefone2")) 
						&& isNullOrBlank(rsCliente.getString("nr_celular")) 
						&& isNullOrBlank(rsCliente.getString("nr_fax")) ){
					return new Result(ERROR_TELEFONE, "O cliente precisa de ao menos 1 telefone cadastrado.");
				}
				
				if(isNullOrBlank(rsCliente.getString("nr_endereco"))
						|| isNullOrBlank(rsCliente.getString("nm_logradouro"))
						|| isNullOrBlank(rsCliente.getString("nm_bairro"))
						|| isNullOrBlank(rsCliente.getString("nm_cidade"))
						|| isNullOrBlank(rsCliente.getString("nr_cep"))
						|| isNullOrBlank(rsCliente.getString("id_ibge")) ){
					
					return new Result(ERROR_ENDERECO, "Os dados do endereço estão incompletos.");
				}
			}
			
			int cdEstadoEmi = 0;
			//:TODO Acrescentar um seletor de endereco para o cliente?
			//Buscar endereço principal de uma pessoa
			PreparedStatement pstmtEnd = connect.prepareStatement("SELECT A.*, B.nm_cidade, B.cd_estado FROM grl_pessoa_endereco A " +
					                                              "LEFT OUTER JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade) " +
                                                                  "WHERE cd_pessoa    = ? "+
          														  "  AND lg_principal = 1");
			pstmtEnd.setInt(1, cdEmpresa);
			ResultSet rsEnd = pstmtEnd.executeQuery();
			//Buscar o estado da empresa
			if(rsEnd.next())
				cdEstadoEmi = rsEnd.getInt("cd_estado");
			//Itera sobre os documentos para inserir as informações de transportador e natureza de operação em uma nota fiscal
			int cdNaturezaOperacaoVerificador = 0;
			for(int i = 0; i < docs.size(); i++) {
				//Faz a soma dos valores de frete e seguro, para incluir na nota
				vlFrete  += docs.get(i).getVlFrete();
				vlSeguro += docs.get(i).getVlSeguro();
				// Busca a Natureza da Operação, e a modifica dependendo do tipo de documento ou estado da empresa e cliente
				if(docs.get(i).getCdNaturezaOperacao() > 0){
					//Natureza de Operacao atual do documento
					cdNaturezaOperacao = docs.get(i).getCdNaturezaOperacao();
					//Busca do estado do cliente
					int cdEstadoDest   = 0;
					if(cdCliente > 0) {
						pstmtEnd.setInt(1, cdCliente);
						rsEnd = pstmtEnd.executeQuery(); 
						if(rsEnd.next())
							cdEstadoDest = rsEnd.getInt("cd_estado");

					}
					//Caso seja uma devolucao ou  uma nota de remessa, segue o CFOP do documento
					int cdNaturezaOperacaoPadrao = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_SAIDA_DEFAULT", 0, cdEmpresa);
					if(docs.get(i).getCdNaturezaOperacao() != cdNaturezaOperacaoPadrao){
						cdNaturezaOperacao = docs.get(i).getCdNaturezaOperacao();
					}
					//Verifica se o documento é um cupom fiscal e é de outro estado
					else if((docs.get(i).getTpDocumentoSaida() == DocumentoSaidaServices.TP_CUPOM_FISCAL) && (cdEstadoEmi != cdEstadoDest && cdEstadoDest>0)){
						int cdNatOpNovo = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_SUBST_CUPOM_OUTRO_ESTADO", 0);
						//Caso seja coloca o CFOP especifico para essa situação
						if(cdNatOpNovo != 0)
							cdNaturezaOperacao = cdNatOpNovo;
						else{
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Parametro de Natureza de Operação de Substituição de Cupom Fiscal para Outro Estado não configurado!");
						}
							
					}
					//Verifica se o documento é de outro estado
					else if(cdEstadoEmi != cdEstadoDest && cdEstadoDest>0){
						int cdNatOpNovo = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_OUTRO_ESTADO", 0);
						//Caso seja coloca o CFOP especifico para essa situação
						if(cdNatOpNovo != 0)
							cdNaturezaOperacao = cdNatOpNovo;
						else{
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Parametro de Natureza de Operação para Outro Estado não configurado!");
						}
					}
					//Verifica se o documento é um cupom fiscal
					else if(docs.get(i).getTpDocumentoSaida() == DocumentoSaidaServices.TP_CUPOM_FISCAL){
						int cdNatOpNovo = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_SUBST_CUPOM", 0);
						//Caso seja coloca o CFOP especifico para essa situação
						if(cdNatOpNovo != 0)
							cdNaturezaOperacao = cdNatOpNovo;
						else{
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Parametro de Natureza de Operação de Substituição de Cupom Fiscal não configurado!");
						}
					}
					
					if(cdNaturezaOperacaoVerificador > 0 && cdNaturezaOperacaoVerificador != cdNaturezaOperacao){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Mais de um CFOP para a mesma nota fiscal!");
					}
					cdNaturezaOperacaoVerificador = cdNaturezaOperacao;
					
				}
				// Permite criar sem o cliente para informar depois, mas não sem empresa ou natureza de operacao
				if (cdEmpresa <= 0 || cdNaturezaOperacao <= 0)	{
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Codigo de Empresa ou a Natureza de Operação não configurado para Documento de saída N. " +docs.get(i).getNrDocumentoSaida());
				}
				
				if(docs.get(i).getCdTransportadora()>0 && cdTransportador<=0)
					cdTransportador = docs.get(i).getCdTransportadora();
			}
			//Naturezas para Posto de Combustivel
			int cdNaturezaOperacaoComb = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_COMBUSTIVEL", 0);
			int cdGrupoCombustivel     = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa);
			int cdGrupoLubrificante    = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_LUBRIFICANTE", 0, cdEmpresa);
			
			// Busca endereço da empresa emitente
			pstmtEnd.setInt(1, cdEmpresa);
			rsEnd = pstmtEnd.executeQuery();
			int cdEnderecoRetirada = 0;
			int cdCidade           = 0;
			if(rsEnd.next())	{
				cdEnderecoRetirada = rsEnd.getInt("cd_endereco");
				cdCidade           = rsEnd.getInt("cd_cidade");
			}
			else{
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Empresa emitente não possui endereço cadastrado");
			}
			// Endereço do CLIENTE
			pstmtEnd.setInt(1, cdCliente);
			ResultSet rsEndCliente = pstmtEnd.executeQuery();
			int cdEnderecoCliente = 0;
			if(rsEndCliente.next())
				cdEnderecoCliente = rsEndCliente.getInt("cd_endereco");
			else if(cdCliente > 0)	{
				Pessoa cliente = PessoaDAO.get(cdCliente, connect);
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Destinatário "+cliente.getNmPessoa()+" não possui endereço cadastrado");
			}
			// Busca e atualiza o número da nota fiscal
			int nrNotaFiscal= 0;
			//São irão tentar gerar o número caso não esteja parametrizado para geração na AUTORIZAÇÃO
			if(ParametroServices.getValorOfParametroAsInteger("LG_NUMERACAO_PARAMETRO", 0, cdEmpresa)!=1 && ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa)==0){
				ResultSetMap rsmNrNota = new ResultSetMap(connect.prepareStatement("SELECT * FROM FSC_NOTA_FISCAL " +
																				"		WHERE CD_EMPRESA = " + cdEmpresa +
																				"		AND NR_SERIE = '" + (getTipoAmbiente(cdEmpresa) == HOMOLOGACAO ? "888" : ParametroServices.getValorOfParametro("NR_SERIE", "1", cdEmpresa)) + "'" +
																				"		AND NR_NOTA_FISCAL IS NOT NULL " +	
																				"		ORDER BY CD_NOTA_FISCAL DESC LIMIT 1").executeQuery());
				if(rsmNrNota.next()){
					nrNotaFiscal = Integer.parseInt(rsmNrNota.getString("NR_NOTA_FISCAL")) + 1;
					ParametroServices.updateValueOfParametro(ParametroServices.getByName("NR_NOTA_FISCAL").getCdParametro(), String.valueOf(nrNotaFiscal), cdEmpresa, connect);
				}
				else{
					nrNotaFiscal = ParametroServices.getValorOfParametroAsInteger("NR_NOTA_FISCAL", 1, cdEmpresa, connect) + 1;
					ParametroServices.updateValueOfParametro(ParametroServices.getByName("NR_NOTA_FISCAL").getCdParametro(), String.valueOf(nrNotaFiscal), cdEmpresa, connect);
				}
			}
			else if(ParametroServices.getValorOfParametroAsInteger("LG_NUMERACAO_PARAMETRO", 0, cdEmpresa)==1 && ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa)==0){
				nrNotaFiscal = ParametroServices.getValorOfParametroAsInteger("NR_NOTA_FISCAL", 1, cdEmpresa, connect) + 1;
				ParametroServices.updateValueOfParametro(ParametroServices.getByName("NR_NOTA_FISCAL").getCdParametro(), String.valueOf(nrNotaFiscal), cdEmpresa, connect);
			}
			
			
			// verifica se algum documento tem faturamento boleto e atualiza o número do boleto para o número da nota
			String whereInBoleto = "";
			String whereInDocumento = "";
			
			
			for (Integer i : documentosSaida) {
				whereInDocumento += ", " + i; 
			}
			whereInDocumento = whereInDocumento.substring(1);
			
			ResultSetMap rsTiposBoleto = getTiposBoleto();
			while(rsTiposBoleto.next()){
				whereInBoleto += rsTiposBoleto.getString("VL_REAL");
				if(rsTiposBoleto.hasMore()){
					whereInBoleto += ", ";
				}
			}
			if(!whereInBoleto.equals("") && !whereInDocumento.equals("")){
				PreparedStatement updBoleto = connect.prepareStatement("UPDATE adm_conta_receber SET nr_documento = ? "
																		+ " WHERE cd_documento_saida IN ( "
																			+ " SELECT A.cd_documento_saida " 
																			+ " FROM alm_documento_saida A "
																			+ " LEFT JOIN adm_plano_pagto_documento_saida B ON B.cd_documento_saida = A.cd_documento_saida"  
																			+ " WHERE B.cd_forma_pagamento IN( " + whereInBoleto + ") "
																			+ " AND A.cd_documento_saida IN("  + whereInDocumento + ") "
																		+ ")");
				updBoleto.setString(1, String.valueOf(nrNotaFiscal));
				updBoleto.executeUpdate();
			}
			
			//Busca o Numero de Serie
			int nrSerie = ParametroServices.getValorOfParametroAsInteger("NR_SERIE", 1, cdEmpresa, connect);
			//Caso seja uma nota de homologação, a Série padrão é 888
			if(getTipoAmbiente(cdEmpresa)==HOMOLOGACAO)
				nrSerie = 888;
			// GRAVANDO NOTA FISCAL
			String txtObservacaoFisco = "DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL. NÃO GERA DIREITO A CRÉDITO FISCAL DE IPI/ICMS.";
			if(ParametroServices.getValorOfParametroAsInteger("TP_REGIME_TRIBUTARIO", 0) == 3){
				txtObservacaoFisco = "";
			}
			NotaFiscal nota = new NotaFiscal(0, cdEmpresa, cdEnderecoRetirada, cdNaturezaOperacao, cdCidade, cdCliente, cdEnderecoCliente, 
											 cdEnderecoCliente, 0, 55/*CAMPO FIXO*/, String.valueOf(nrSerie), (nrNotaFiscal == 0 ? null : String.valueOf(nrNotaFiscal)), 
											 EM_DIGITACAO, new GregorianCalendar(), SAIDA, new GregorianCalendar(), 
											  A_VISTA /*Pesquisar como foi feito o pagamento e colocar a constante aqui depois*/, EMI_NORMAL, NFE_NORMAL, 
											  RETRATO, 0 /*vlTotalProduto*/, vlSeguro/*vlSeguro*/, 0/*OUTRAS DESPESAS*/,  0/*vlTotalNota*/, SEM_FRETE, ""/*txtObservacao*/, 
											  txtObservacaoFisco/*INFORMACOES RELACIONADAS AO FISCO- 08-01-2013*/, 
											  0/*lgDanfeImpresso*/, null /*nrChaveAcesso: MUDAR PELO QUE SERA GERADO POR gerarIDNFE*/,
											  0 /*DV - deve ser gerado a partir da chave de acesso - Foi omitido ao atualizar a classe*/, 
											  null/*nrProtocoloAutorizacao - QUE RECEBE DA SEFAZ, buscar após transmitir a nota*/, 
											  null/*DATA DE AUTORIZAÇÃO*/,  cdTransportador/*cdTransportador*/, 0/*NATUREZA DE OPERACAO DO FRETE*/, 
											  vlFrete, 0/*vlFreteBaseIcms*/, 0/*vlFreteIcmsRetido*/, null/*nrRecebimento*/, 
											  null /*nrPlaca*/, null /*qtVolume*/, null /*dsEspecie*/, null /*dsMarca*/,
											  null /*dsNumeracao*/, (float)0 /*vlPesoBruto*/, (float)0 /*vlPesoLiquido*/,0 /*cdVeiculo*/,
											  null/*sgUfVeiculo*/, null/*nrRntc*/, 0/*cdMotivoCancelamento*/, null/*txtXml*/, 0/*prDesconto*/, 1/*lgConsumidorFinal*/, TP_PRESENCA_PRESENCIAL/*tpVendaPresenca*/, null/*nrChaveAcessoReferencia*/);
			
			
			//Caso seja configurado pelo cliente, as informações da nota fiscal eletronica seram recebidas da viagem como veiculo
			if(ParametroServices.getValorOfParametroAsInteger("LG_BUSCAR_INFORMACOES_VIAGEM", 0, cdEmpresa, connect) == 1){
				if(docs.isEmpty()){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Nota Fiscal sem documentos de venda");
				}
				
				DocumentoSaida docSaida = docs.get(0);
				//Pesquisa para saber se algum documento de saida passado tem uma viagem vinculada
				int i = 0;
				while(docSaida.getCdViagem() == 0 && i < docs.size()){
					docSaida = docs.get(i++);
				}
				
				if(docSaida.getCdViagem() > 0){
					Viagem viagem = ViagemDAO.get(docSaida.getCdViagem(), connect);
					nota.setCdVeiculo(viagem.getCdVeiculo());					
				}
				
			}
			
			nota.setCdNotaFiscal(NotaFiscalDAO.insert(nota, connect));
			if(nota.getCdNotaFiscal() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao cadastrar Nota");
			}
			// GERANDO ID - NFE
			if(nota.getNrNotaFiscal() != null){
				Result result = gerarNfeId(nota);
				if(result.getCode() <= 0)	{
					if(isConnectionNull)
						Conexao.rollback(connect);
					result.setMessage(result.getMessage()+" Falha ao tentar gerar ID da Nota Fiscal.");
					return result;
				}
				nota.setNrChaveAcesso((String)result.getObjects().get("nrChave"));
				nota.setNrDv((Integer)result.getObjects().get("nrDv"));
			}
			if(NotaFiscalDAO.update(nota, connect) <= 0)	{
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Falha ao atualizar ID da NFE");
			}
			//Insere os itens da nota fiscal a partir dos itens dos documentos
			int qtItensTotal = 0;
			String dsCupons = "", dsDAVs = "";
			//ArrayList para guardar as nota fiscal item tributo que serão adicionadas após a inserição das nota fiscal tributo
			ArrayList<NotaFiscalItemTributo> listaNotaFiscalItemTributo = new ArrayList<NotaFiscalItemTributo>();
			for(int i = 0; i < docs.size(); i++)	{
				if(docs.get(i) == null)	{
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Documento não encontrado!");
				}
				
				//Verifica se o documento foi cancelado
				if(docs.get(i).getStDocumentoSaida() == DocumentoSaidaServices.ST_CANCELADO)	{
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Documento de saída N. " +docs.get(i).getNrDocumentoSaida()+" cancelado!");
				}
				
				//Parametro que permiti lançar nota de documento em conferência
				String lgPerm = ParametroServices.getValorOfParametro("LG_PERMITIR_NOTA_DOC_CONFERENCIA", cdEmpresa, connect);
				boolean permitir = (Integer.parseInt((lgPerm == null || lgPerm.equals("")) ? "0" : lgPerm) == 0) ? false : true;
				if(!permitir){
					if(docs.get(i).getStDocumentoSaida() == DocumentoSaidaServices.ST_EM_CONFERENCIA)	{
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Documento de saída N. " +docs.get(i).getNrDocumentoSaida()+" em conferência!");
					}
				}
				//Impede lançar nota de um documento que ja esta vinculada a outra nota no sistema
				if(NotaFiscalDocVinculadoServices.findByDocSaida(docs.get(i).getCdDocumentoSaida()).next()){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Documento de saída N. " +docs.get(i).getNrDocumentoSaida()+" ja vinculado com uma nota fiscal!");
				}
				//Ocorre um erro caso haja documento de clientes diferentes
				if(docs.get(i).getCdCliente()>0 && docs.get(i).getCdCliente()!=cdCliente){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Os documentos de saída devem ser do mesmo cliente!");
				}
				// 
				if(NotaFiscalDocVinculadoDAO.insert(new NotaFiscalDocVinculado(nota.getCdNotaFiscal(), docs.get(i).getCdDocumentoSaida(), 0, docs.get(i).getCdDocumentoSaida(), 0, SAIDA), connect) <= 0)	{
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao vincular Nota Fiscal a Documento");
				}
				
				//Busca informações sobre os itens do documento
				ResultSet rsProds = connect.prepareStatement("SELECT A.*, D.*, H.cd_grupo, B.qt_precisao_custo, C.nr_precisao_medida, " +
									                        "     (SELECT SUM((J.pr_aliquota/100) * J.vl_base_calculo) " +
															 "      FROM adm_saida_item_aliquota J " + 
															 "      WHERE J.cd_produto_servico = D.cd_produto_servico " + 
															 "        AND J.cd_documento_saida = D.cd_documento_saida " + 
															 "        AND J.cd_empresa         = D.cd_empresa) AS vl_tributo  " +
															 "FROM alm_documento_saida_item D " +
															 "LEFT OUTER JOIN grl_produto_servico         A ON (A.cd_produto_servico  = D.cd_produto_servico)  " +
															 "LEFT OUTER JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico  = B.cd_produto_servico" +
															 "														AND B.cd_empresa = "+cdEmpresa+")  " +
															 "LEFT OUTER JOIN grl_unidade_medida        C ON (C.cd_unidade_medida = B.cd_unidade_medida) " +
															 "LEFT OUTER JOIN alm_produto_grupo        H ON (H.cd_produto_servico = D.cd_produto_servico " +
															 "                                           AND H.cd_empresa            = D.cd_empresa " +
															 "                                           AND H.lg_principal = 1) " +
															 "WHERE D.cd_documento_saida = " + docs.get(i).getCdDocumentoSaida()).executeQuery();
					
				//Marca a quantidade total de itens que pode entrar em uma nota
				int qtItens       = 0;
				//Itera sobre os produtos para cadastrar os itens da nota fiscal
				int cdNaturezaOperacaoPadrao = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_SAIDA_DEFAULT", 0, cdEmpresa);
				while(rsProds.next())	{
					//
					qtItens++;
					qtItensTotal++;
					
					
					if(rsProds.getFloat("vl_unitario") <= 0 || rsProds.getFloat("qt_saida") <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Produto " + rsProds.getString("nm_produto_servico") + " esta com valor unitário ou quantidade zerados no documento de saída No " + docs.get(i).getNrDocumentoSaida());
					}
					
					/*
					 *  INCLUIDO OS ITENS
					 */
					
					//Verifica se o item foi classificado como substituicao tributaria
					ResultSet rsTribAnt = connect.prepareStatement("SELECT A.* FROM adm_saida_item_aliquota A " +
																 "	WHERE A.cd_documento_saida = " + docs.get(i).getCdDocumentoSaida() +
																 "		AND A.cd_produto_servico = " + rsProds.getInt("cd_produto_servico")).executeQuery();
					boolean isSubstituicaoTributaria = false;
					if(rsTribAnt.next()){
						//Verifica se o produto é cobrado por substituição tributária
						TributoAliquota tribAliq = TributoAliquotaDAO.get(rsTribAnt.getInt("cd_tributo_aliquota"), rsTribAnt.getInt("cd_tributo"), connect);
						SituacaoTributaria situacaoTrib = null;
						if(tribAliq != null){
							situacaoTrib = SituacaoTributariaDAO.get(rsTribAnt.getInt("cd_tributo"), tribAliq.getCdSituacaoTributaria(), connect);
							if(situacaoTrib != null){
								isSubstituicaoTributaria = situacaoTrib.getLgSubstituicao()==1;
							}
							else{
								isSubstituicaoTributaria = tribAliq.getStTributaria()==TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA;
							}
						}
					}
					else
						isSubstituicaoTributaria = ParametroServices.getValorOfParametroAsInteger("CD_SUBSTITUICAO_TRIBUTARIA", 0)==rsProds.getInt("cd_classificacao_fiscal");
					
					//Verificação do CFOP do Item
					int cdNaturezaOperacaoItem = rsProds.getInt("cd_natureza_operacao");
					if(cdNaturezaOperacaoItem == 0)
						cdNaturezaOperacaoItem = docs.get(i).getCdNaturezaOperacao();
					int cdEstadoDest   = 0;
					int cdCidadeDest   = 0;
					//Busca estado e cidade do cliente
					if(cdCliente > 0) {
						pstmtEnd.setInt(1, cdCliente);
						rsEnd = pstmtEnd.executeQuery(); 
						if(rsEnd.next()){
							cdEstadoDest = rsEnd.getInt("cd_estado");
							cdCidadeDest = rsEnd.getInt("cd_cidade");
						}

					}
					//Caso seja uma devolucao, segue o CFOP do documento
					if((rsProds.getInt("cd_natureza_operacao") == 0 ? docs.get(i).getCdNaturezaOperacao() : rsProds.getInt("cd_natureza_operacao")) != cdNaturezaOperacaoPadrao){
						cdNaturezaOperacaoItem = (rsProds.getInt("cd_natureza_operacao") == 0 ? docs.get(i).getCdNaturezaOperacao() : rsProds.getInt("cd_natureza_operacao"));
					}
					//Verifica se o documento é automaticamente para outro estado e se ele é um cupom fiscal
					else if((docs.get(i).getTpDocumentoSaida() == DocumentoSaidaServices.TP_CUPOM_FISCAL) && (cdEstadoEmi != cdEstadoDest && cdEstadoDest>0)){
						int cdNatOpNovo = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_SUBST_CUPOM_OUTRO_ESTADO", 0);
						if(cdNatOpNovo != 0)
							cdNaturezaOperacaoItem = cdNatOpNovo;
						else{
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Parametro de Natureza de Operação de Substituição de Cupom Fiscal para Outro Estado não configurado!");
						}
							
					}
					//Verifica se o documento é para outro estado
					else if(cdEstadoEmi != cdEstadoDest && cdEstadoDest>0){
						//Verifica se o produto é cobrado por substituição tributaria
						if(isSubstituicaoTributaria){
							int cdNatOpNovo = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_SUBST_TRIBUTARIA_OUTRO_ESTADO", 0);
							if(cdNatOpNovo != 0)
								cdNaturezaOperacaoItem = cdNatOpNovo;
							else{
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Parametro de Natureza de Operação de Substituição Tributária para Outro Estado não configurado!");
							}
						}
						else{
							int cdNatOpNovo = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_OUTRO_ESTADO", 0);
							if(cdNatOpNovo != 0)
								cdNaturezaOperacaoItem = cdNatOpNovo;
							else{
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Parametro de Natureza de Operação para Outro Estado não configurado!");
							}
						}
					}
					//Verifica se o documento é um cupom fiscal apenas
					else if(docs.get(i).getTpDocumentoSaida() == DocumentoSaidaServices.TP_CUPOM_FISCAL){
						int cdNatOpNovo = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_SUBST_CUPOM", 0);
						if(cdNatOpNovo != 0)
							cdNaturezaOperacaoItem = cdNatOpNovo;
						else{
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Parametro de Natureza de Operação de Substituição de Cupom Fiscal não configurado!");
						}
					}
					//Verifica se o produto é cobrado por substituição tributaria apenas
					else if(isSubstituicaoTributaria){
						int cdNatOpNovo = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_SUBST_TRIBUTARIA", 0);
						if(cdNatOpNovo != 0)
							cdNaturezaOperacaoItem = cdNatOpNovo;
						else{
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Parametro de Natureza de Operação de Substituição Tributária não configurado!");
						}
					}
					//Verifica se o produto é um combustivel ou lubrificante
					else if(rsProds.getInt("cd_grupo")>0 && (rsProds.getInt("cd_grupo")==cdGrupoCombustivel || rsProds.getInt("cd_grupo")==cdGrupoLubrificante)){
						if(cdNaturezaOperacaoComb != 0)
							cdNaturezaOperacaoItem = cdNaturezaOperacaoComb;
					}
					//Código temporário para manter a regra do CFOP 6102 para qualquer operação para Minas Gerais (Atacadão), menos quando for devolução  e cupom fiscal
					if(cdEstadoDest == 5 && docs.get(i).getTpDocumentoSaida() != DocumentoSaidaServices.TP_CUPOM_FISCAL && cdNaturezaOperacao != ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR_SUBSTITUTO", 0) &&
																														   cdNaturezaOperacao != ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR_OUTRO_ESTADO", 0) &&
																														   cdNaturezaOperacao != ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR_SUBSTITUTO_OUTRO_ESTADO", 0) &&
																														   cdNaturezaOperacao != ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR", 0, cdEmpresa)){
						cdNaturezaOperacaoItem = 4;
					}
					
					if(cdEstadoDest == 0 || cdCidadeDest == 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Destinatário precisa ter cidade e estado cadastrados!");
					}
					//Calcula o valor dos tributos
					ResultSetMap rsmTributos = TributoServices.calcTributos(TributoAliquotaServices.OP_VENDA, cdNaturezaOperacaoItem, rsProds.getInt("cd_classificacao_fiscal"), 
																			rsProds.getInt("cd_produto_servico"), 0, cdEstadoDest, cdCidadeDest, (rsProds.getFloat("qt_saida") * rsProds.getFloat("vl_unitario") - rsProds.getFloat("vl_desconto")),
																			connect);
					rsmTributos.beforeFirst();
					
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_produto_servico", "" + rsProds.getInt("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_nota_fiscal", "" + nota.getCdNotaFiscal(), ItemComparator.EQUAL, Types.INTEGER));
					//Se ja tiver um item com esse produto, o sistema ira fazer uma media ponderada com o valor unitario, e irão acrescer no item a quantidade tributaria, o valor de acrescimo e o valor de desconto
					ResultSetMap rsm = NotaFiscalItemDAO.find(criterios, connect);
					rsm.beforeFirst();
					if(rsm.next()){
						/*
						 *  ATUALIZANDO OS TOTAIS DO ITEM
						 */
						float qtSaida         = (float)Util.arredondar(rsProds.getFloat("qt_saida"), (rsProds.getInt("nr_precisao_medida") > 4 ? 4 : (rsProds.getInt("nr_precisao_medida") <= 0 ? 0 : rsProds.getInt("nr_precisao_medida"))));
						float vlUnitario      = (float)Util.arredondar(rsProds.getFloat("vl_unitario"), (rsProds.getInt("qt_precisao_custo") > 10 ? 10 : (rsProds.getInt("qt_precisao_custo") <= 0 ? 2 : rsProds.getInt("qt_precisao_custo"))));
						NotaFiscalItem notaItem = NotaFiscalItemDAO.get(rsm.getInt("cd_nota_fiscal"), rsm.getInt("cd_item"), connect);
						//Calcula novo valor unitario a partir de uma média ponderada
						float novoVlUnitario = ((notaItem.getQtTributario() * notaItem.getVlUnitario()) + (qtSaida * vlUnitario)) / 
												(notaItem.getQtTributario() + qtSaida);
						//Retira dos valores da nota o valor da multiplicação entre o valor unitario antigo com a quantidade tributada antiga
						nota.setVlTotalProduto(nota.getVlTotalProduto() - (notaItem.getVlUnitario() * notaItem.getQtTributario()));
						nota.setVlTotalNota(nota.getVlTotalNota() - (notaItem.getVlUnitario() * notaItem.getQtTributario()));
						//A quantidade tributada é somada
						notaItem.setQtTributario(notaItem.getQtTributario() + qtSaida);
						//O valor unitario é substituido
						notaItem.setVlUnitario(novoVlUnitario);
						//O valor total de produtos é acrescido com a multiplicação do novo valor unitario e a nova quantidade tributada
						float vlProdutoTemp = ((float)Util.arredondar(notaItem.getVlUnitario(), (rsProds.getInt("qt_precisao_custo") > 10 ? 10 : (rsProds.getInt("qt_precisao_custo") <= 0 ? 2 : rsProds.getInt("qt_precisao_custo")))) *(float)Util.arredondar(notaItem.getQtTributario(), (rsProds.getInt("nr_precisao_medida") > 4 ? 4 : (rsProds.getInt("nr_precisao_medida") <= 0 ? 0 : rsProds.getInt("nr_precisao_medida")))));
						float novoVlTotalProduto = nota.getVlTotalProduto() + vlProdutoTemp;
//						float novoVlTotalProduto = (rsProds.getInt("qt_precisao_custo") <= 0 ? (float)Util.arredondar(nota.getVlTotalProduto() + (notaItem.getVlUnitario() * notaItem.getQtTributario()), 2) : (float)Util.arredondar(nota.getVlTotalProduto() + (notaItem.getVlUnitario() * notaItem.getQtTributario()), rsProds.getInt("qt_precisao_custo")));
						nota.setVlTotalProduto(novoVlTotalProduto);
						//Os valores de acrescimo e desconto são acrescidos
						notaItem.setVlAcrescimo(notaItem.getVlAcrescimo() + rsProds.getFloat("vl_acrescimo"));
						notaItem.setVlDesconto(notaItem.getVlDesconto() + rsProds.getFloat("vl_desconto"));
						//O valor da nota é acrescido pela multiplicação do novo valor unitario e a nova quantidade tributada menos o novo valor de desconto
						float novoVlTotalNota = (rsProds.getInt("qt_precisao_custo") <= 0 ? (float)Util.arredondar(nota.getVlTotalNota() + (vlProdutoTemp - rsProds.getFloat("vl_desconto")), 2) : (float)Util.arredondar(nota.getVlTotalNota() + (vlProdutoTemp - rsProds.getFloat("vl_desconto")), rsProds.getInt("qt_precisao_custo")));
						nota.setVlTotalNota(novoVlTotalNota);
						//Atualiza a nota fiscal item
						if(NotaFiscalItemDAO.update(notaItem, connect) <= 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao cadastrar Produto da Nota!");
						}
						
						/*
						 *  ATUALIZANDO OS TOTAIS DOS TRIBUTOS 
						 */
						while(rsmTributos.next()){
							int cdTributo            = rsmTributos.getInt("cd_tributo");
							float vlBaseCalculo      = (rsmTributos.getFloat("pr_aliquota") == 0) ? 0 : rsmTributos.getFloat("vl_base_calculo");
							float vlBaseCalculoRet   = (rsmTributos.getFloat("pr_aliquota_substituicao") == 0) ? 0 : rsmTributos.getFloat("vl_base_calculo");
							float vlTributo		  	 = vlBaseCalculo * rsmTributos.getFloat("pr_aliquota") / 100;
							float vlRetido 		  	 = vlBaseCalculo * rsmTributos.getFloat("pr_aliquota_substituicao") / 100;
							
							//Busca o tributo do item
							NotaFiscalItemTributo notaItemTributo = null;
							int j = 0;
							for(; j < listaNotaFiscalItemTributo.size(); j++){
								NotaFiscalItemTributo notaItemTribAux = listaNotaFiscalItemTributo.get(j);
								if(notaItemTribAux.getCdNotaFiscal() == nota.getCdNotaFiscal() &&
								   notaItemTribAux.getCdItem() == rsm.getInt("cd_item") &&
								   notaItemTribAux.getCdTributo() == cdTributo){
									notaItemTributo = (NotaFiscalItemTributo) notaItemTribAux.clone();
									break;
								}
							}
							
//							NotaFiscalItemTributo notaItemTributo = NotaFiscalItemTributoDAO.get(nota.getCdNotaFiscal(), rsm.getInt("cd_item"), cdTributo, connect);
							if(notaItemTributo == null){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro: Item sem tributo!");
							}
							//Aumenta a base de calculo com a nova
							//São entrarão se a base de calculo for maior do que zero, significando que não é o padrão configurado 102 sem permissão de credito
							if(notaItemTributo.getVlBaseCalculo() > 0){
								notaItemTributo.setVlBaseCalculo(notaItemTributo.getVlBaseCalculo() + vlBaseCalculo);
								notaItemTributo.setVlBaseRetencao(notaItemTributo.getVlBaseRetencao() + vlBaseCalculoRet);
								//Recalcula o valor de tributo e o valor de credito
								notaItemTributo.setVlTributo(notaItemTributo.getVlTributo() + vlTributo);
								notaItemTributo.setVlRetido(notaItemTributo.getVlRetido() + vlRetido);
								notaItemTributo.setVlCredito(notaItemTributo.getVlCredito() + vlBaseCalculo * notaItemTributo.getPrCredito() / 100);
								//Atualiza a nota fiscal item tributo
								listaNotaFiscalItemTributo.set(j, notaItemTributo);
//								if(NotaFiscalItemTributoDAO.update(notaItemTributo, connect) <= 0){
//									Conexao.rollback(connect);
//									return new Result(-1, "Erro: Erro ao atualizar tributo de item!");
//								}
							}
							
						} 
					}
					else{
						float qtSaida         = (float)Util.arredondar(rsProds.getFloat("qt_saida"), (rsProds.getInt("nr_precisao_medida") > 4 ? 4 : (rsProds.getInt("nr_precisao_medida") <= 0 ? 0 : rsProds.getInt("nr_precisao_medida"))));
						float vlUnitario      = (float)Util.arredondar(rsProds.getFloat("vl_unitario"), (rsProds.getInt("qt_precisao_custo") > 10 ? 10 : (rsProds.getInt("qt_precisao_custo") <= 0 ? 2 : rsProds.getInt("qt_precisao_custo"))));
						float vlDescontoItem  = rsProds.getFloat("vl_desconto");
						float vlAcrescimoItem = rsProds.getFloat("vl_acrescimo");
						int cdNotaFiscalItem = NotaFiscalItemDAO.insert(new NotaFiscalItem(nota.getCdNotaFiscal(), qtItensTotal, docs.get(i).getCdDocumentoSaida(), rsProds.getInt("cd_produto_servico"), 
				                                                                           docs.get(i).getCdEmpresa(), 0, cdNaturezaOperacaoItem, qtSaida, vlUnitario, 
				                                                                           rsProds.getString("txt_especificacao"), rsProds.getInt("cd_item"), vlAcrescimoItem, vlDescontoItem), connect);
						// Total dos Produtos
						if(cdNotaFiscalItem <= 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao cadastrar Produto da Nota");
						}
						
						
						/*
						 *  INCLUIDO OS TOTAIS DOS TRIBUTOS 
						 */
						//INSERE TRIBUTO COM SITUACAO padrão CONFIGURADA
						if(rsmTributos.getLines().size() == 0){
							int cdTributo         	 = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0);
							int cdTributoAliquota    = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ALIQUOTA_ICMS_VENDAS", 0);
							float prAliquota      	 = 0;
							float prCredito       	 = 0;
							float vlBaseCalculo   	 = 0;
							int tpCalculo            = 0;
							//Busca da situacao tributaria padrão 102
							criterios = new ArrayList<ItemComparator>();
							criterios.add(new ItemComparator("cd_tributo", "" + cdTributo, ItemComparator.EQUAL, Types.INTEGER));
							if(ParametroServices.getValorOfParametroAsInteger("TP_REGIME_TRIBUTARIO", 0)==1 || ParametroServices.getValorOfParametroAsInteger("TP_REGIME_TRIBUTARIO", 0)==2)
								criterios.add(new ItemComparator("nr_situacao_tributaria", "102", ItemComparator.LIKE, Types.VARCHAR));
							else
								criterios.add(new ItemComparator("nr_situacao_tributaria", "00", ItemComparator.LIKE, Types.VARCHAR));
							ResultSetMap rsmSitTrib = SituacaoTributariaDAO.find(criterios, connect);
							int cdSituacaoTributaria = 0;
							if(rsmSitTrib.next())
								cdSituacaoTributaria = rsmSitTrib.getInt("cd_situacao_tributaria");
							if(cdSituacaoTributaria == 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "CFOP padrão não configurado para ICMS!");
							}
							float vlBaseRetencao     = 0;
							float vlRetido           = 0;
							int tpRegime             = ParametroServices.getValorOfParametroAsInteger("TP_REGIME_TRIBUTARIO", 0);
							if(tpRegime == 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Regime Tributário não configurado!");
							}
							//Insere a nota fiscal item tributo
							listaNotaFiscalItemTributo.add(new NotaFiscalItemTributo(nota.getCdNotaFiscal(), qtItensTotal, cdTributo, 
															cdTributoAliquota, tpRegime, 0/*Tipo de Origem - 0 Nacional, 1 - Estrangeiro (Obs.: Ainda não trabalhamos com exportacoes)*/ , 
															tpCalculo, vlBaseCalculo, 0/*vlOutrasDespesas*/, 0/*vlOutrosImpostos*/, prAliquota, (vlBaseCalculo * prAliquota / 100)/*vlTributo*/, 
															prCredito, (vlBaseCalculo * prCredito / 100)/*vlCredito*/, null/*nrClasse*/, null/*nrEnquadramento*/, cdSituacaoTributaria, vlBaseRetencao, 
															vlRetido));
						}
						while(rsmTributos.next()){
							int cdTributo         	 = rsmTributos.getInt("cd_tributo");
							int cdTributoAliquota    = rsmTributos.getInt("cd_tributo_aliquota");
							float prAliquota      	 = rsmTributos.getFloat("pr_aliquota");
							float prCredito       	 = rsmTributos.getFloat("pr_credito");
							float vlBaseCalculo   	 = (rsmTributos.getFloat("pr_aliquota") > 0) ? rsmTributos.getFloat("vl_base_calculo") : 0;
							int tpCalculo            = rsmTributos.getInt("tp_base_calculo");
							int cdSituacaoTributaria = rsmTributos.getInt("cd_situacao_tributaria");
							float vlBaseRetencao     = (rsmTributos.getFloat("pr_aliquota_substituicao") > 0) ? rsmTributos.getFloat("vl_base_calculo") : 0;
							float vlRetido           = vlBaseRetencao * rsmTributos.getFloat("pr_aliquota_substituicao") / 100;
							int tpRegime             = ParametroServices.getValorOfParametroAsInteger("TP_REGIME_TRIBUTARIO", 0);
							if(tpRegime == 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Regime Tributário não configurado!");
							}
							//Insere a nota fiscal item tributo
							listaNotaFiscalItemTributo.add(new NotaFiscalItemTributo(nota.getCdNotaFiscal(), qtItensTotal, cdTributo, 
															cdTributoAliquota, tpRegime, 0/*Tipo de Origem - 0 Nacional, 1 - Estrangeiro (Obs.: Ainda não trabalhamos com exportacoes)*/ , 
															tpCalculo, vlBaseCalculo, 0/*vlOutrasDespesas*/, 0/*vlOutrosImpostos*/, prAliquota, (vlBaseCalculo * prAliquota / 100)/*vlTributo*/, 
															prCredito, (vlBaseCalculo * prCredito / 100)/*vlCredito*/, null/*nrClasse*/, null/*nrEnquadramento*/, cdSituacaoTributaria, vlBaseRetencao, 
															vlRetido));
						} 
						int precisao = rsProds.getInt("qt_precisao_custo") <= 0 ? 2 : rsProds.getInt("qt_precisao_custo");
						float vlTotalProduto = (float)Util.arredondar((nota.getVlTotalProduto() + (qtSaida * vlUnitario)), precisao);
						nota.setVlTotalProduto(vlTotalProduto);
						float vlTotalNota = (float)Util.arredondar((nota.getVlTotalNota() + (qtSaida * vlUnitario - vlDescontoItem)), precisao);
						nota.setVlTotalNota(vlTotalNota);
					}
				}
				
				// Verifica se a nota fiscal tinha itens
				if(qtItens == 0)	{
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "O Documento de Saída No "+docs.get(i).getNrDocumentoSaida()+" não possui itens!");
				}
				
				// 
				if(docs.get(i).getTpDocumentoSaida()==DocumentoSaidaServices.TP_CUPOM_FISCAL)
					dsCupons +=  (!dsCupons.equals("") ? ", " : "") + docs.get(i).getNrDocumentoSaida();
//				else
//					dsDAVs +=  (!dsDAVs.equals("") ? ", " : "") + docs.get(i).getNrDocumentoSaida();
				
				
				nota.setTxtObservacao((docs.get(i).getTxtObservacao() == null || docs.get(i).getTxtObservacao().trim().equals("") ? "" : docs.get(i).getTxtObservacao() + ". ").trim());
				
			}
			
			//Confere se é uma operação de devolução para que possa fazer um espelhamento de nota
			if(cdNaturezaOperacao == ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR_SUBSTITUTO", 0) ||
			   cdNaturezaOperacao == ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR_OUTRO_ESTADO", 0) ||
			   cdNaturezaOperacao == ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR_SUBSTITUTO_OUTRO_ESTADO", 0) ||
			   cdNaturezaOperacao == ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR", 0, cdEmpresa)){
				
				listaNotaFiscalItemTributo = new ArrayList<NotaFiscalItemTributo>();
				
				int tpRegime             = ParametroServices.getValorOfParametroAsInteger("TP_REGIME_TRIBUTARIO", 0);
				if(tpRegime == 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Regime Tributário não configurado!");
				}
				
				for(DocumentoSaida doc : docs){
					ResultSetMap rsmItensDocSaida = DocumentoSaidaServices.getAllItens(doc.getCdDocumentoSaida(), connect);
					while(rsmItensDocSaida.next()){
						ProdutoServico produtoServico = ProdutoServicoDAO.get(rsmItensDocSaida.getInt("cd_produto_servico"), connect);
						ResultSetMap rsmItemAliquota = DocumentoSaidaItemServices.getAllAliquotas(rsmItensDocSaida.getInt("cd_documento_saida"), rsmItensDocSaida.getInt("cd_produto_servico"), rsmItensDocSaida.getInt("cd_item"), connect);
						while(rsmItemAliquota.next()){
							SituacaoTributaria situacaoTributaria = SituacaoTributariaDAO.get(rsmItemAliquota.getInt("cd_tributo"), rsmItemAliquota.getInt("cd_tributo_aliquota"), connect);
							float vlBaseCalculo = 0;
							float vlTributo = 0;
							float vlBaseRetencao = 0;
							float vlRetencao = 0;
							
							if(produtoServico.getCdClassificacaoFiscal() == ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTADO_INTEGRALMENTE", 0)){
								vlBaseCalculo = rsmItemAliquota.getFloat("vl_base_calculo");
								vlTributo = rsmItemAliquota.getFloat("vl_base_calculo") * rsmItemAliquota.getFloat("pr_aliquota") / 100;
								vlBaseRetencao = 0;
								vlRetencao = 0;
							}
							else if(produtoServico.getCdClassificacaoFiscal() == ParametroServices.getValorOfParametroAsInteger("CD_SUBSTITUICAO_TRIBUTARIA", 0)){
								vlBaseCalculo = 0;
								vlTributo = 0;
								vlBaseRetencao = rsmItemAliquota.getFloat("vl_base_calculo");
								vlRetencao = rsmItemAliquota.getFloat("vl_base_calculo") * rsmItemAliquota.getFloat("pr_aliquota") / 100;
							}
							else{
								vlBaseCalculo = 0;
								vlTributo = 0;
								vlBaseRetencao = 0;
								vlRetencao = 0;
							}
							
							ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
							criterios.add(new ItemComparator("cd_nota_fiscal", "" + nota.getCdNotaFiscal(), ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("cd_produto_servico", rsmItensDocSaida.getString("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("cd_empresa", "" + nota.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
							int cdItem = 0;
							ResultSetMap rsmItensNota = NotaFiscalItemDAO.find(criterios, connect);
							if(rsmItensNota.next())
								cdItem = rsmItensNota.getInt("cd_item");
							
							listaNotaFiscalItemTributo.add(new NotaFiscalItemTributo(nota.getCdNotaFiscal(), cdItem, rsmItemAliquota.getInt("cd_tributo"), rsmItemAliquota.getInt("cd_tributo_aliquota"), 
														  tpRegime, 0, 0, vlBaseCalculo, 0, 0, rsmItemAliquota.getFloat("pr_aliquota"), vlTributo, 0, 0, null, null, 
														  (situacaoTributaria != null ? situacaoTributaria.getCdSituacaoTributaria() : 0), vlBaseRetencao, vlRetencao));
						}
						
					}
					
				}
				
				//Itera sobre os tributos dos itens para fazer o tributo da nota
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_nota_fiscal", "" + nota.getCdNotaFiscal(), ItemComparator.EQUAL, Types.INTEGER));
				for(int k = 0; k < listaNotaFiscalItemTributo.size(); k++){
					NotaFiscalItemTributo notaItemTrib = listaNotaFiscalItemTributo.get(k);
					NotaFiscalTributo notaTributo = NotaFiscalTributoDAO.get(notaItemTrib.getCdNotaFiscal(), notaItemTrib.getCdTributo(), connect);
					if(notaTributo == null)	{
						notaTributo = new NotaFiscalTributo(nota.getCdNotaFiscal(), notaItemTrib.getCdTributo(), 
															notaItemTrib.getVlBaseCalculo(), 
															notaItemTrib.getVlOutrasDespesas(), 0/*Outros Impostos*/, 
															notaItemTrib.getVlTributo(), 
															notaItemTrib.getVlBaseRetencao(), notaItemTrib.getVlRetido());
						if(NotaFiscalTributoDAO.insert(notaTributo, connect) <= 0)	{
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao gerar Nota Fiscal Tributo");
						}
					}
					else	{
						notaTributo.setVlBaseCalculo(notaTributo.getVlBaseCalculo() + notaItemTrib.getVlBaseCalculo());
						notaTributo.setVlBaseRetencao(notaTributo.getVlBaseRetencao() + notaItemTrib.getVlBaseRetencao());
						notaTributo.setVlOutrasDespesas(notaTributo.getVlOutrasDespesas() +notaItemTrib.getVlOutrasDespesas());
						notaTributo.setVlOutrosImpostos(0);
						notaTributo.setVlRetido(notaTributo.getVlRetido() + notaItemTrib.getVlRetido());
						notaTributo.setVlTributo(notaTributo.getVlTributo() + notaItemTrib.getVlTributo());
						NotaFiscalTributoDAO.update(notaTributo, connect);
						
					}
					
				}
				
			}
			else{
				//Cria os tributos para nota a partir dos tributos do documento
				for(DocumentoSaida doc : docs){
					ResultSetMap rsmSaidasTributo = SaidaTributoServices.getAllByDocumentoSaida(doc.getCdDocumentoSaida(), connect);
					while(rsmSaidasTributo.next()){
						NotaFiscalTributo notaTributo = NotaFiscalTributoDAO.get(nota.getCdNotaFiscal(), rsmSaidasTributo.getInt("cd_tributo"), connect);
						if(notaTributo == null){
							notaTributo = new NotaFiscalTributo(nota.getCdNotaFiscal(), rsmSaidasTributo.getInt("cd_tributo"), 
															rsmSaidasTributo.getFloat("vl_base_calculo"), 
															0/*vlOutrasDespesas*/, 0/*Outros Impostos*/, 
															rsmSaidasTributo.getFloat("vl_tributo"), 
															rsmSaidasTributo.getFloat("vl_base_retencao"), 
															rsmSaidasTributo.getFloat("vl_retido"));
							if(NotaFiscalTributoDAO.insert(notaTributo, connect) <= 0)	{
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro ao gerar Nota Fiscal Tributo");
							}
						}
						
						else{
							notaTributo.setVlBaseCalculo(notaTributo.getVlBaseCalculo() + rsmSaidasTributo.getFloat("vl_base_calculo"));
							notaTributo.setVlBaseRetencao(notaTributo.getVlBaseRetencao() + rsmSaidasTributo.getFloat("vl_base_retencao"));
							notaTributo.setVlRetido(notaTributo.getVlRetido() + rsmSaidasTributo.getFloat("vl_retido"));
							notaTributo.setVlTributo(notaTributo.getVlTributo() + rsmSaidasTributo.getFloat("vl_tributo"));
							NotaFiscalTributoDAO.update(notaTributo, connect);
						}
					}
				}
				
				//Verifica a soma da nota para valores tributados integralmente e substituição tributaria
				float vlTributadoIntegralmente = 0;
				float vlSubstituicaoTributaria = 0;
				for(int k = 0; k < listaNotaFiscalItemTributo.size(); k++){
					NotaFiscalItemTributo notaItemTrib = listaNotaFiscalItemTributo.get(k);
					
					NotaFiscalItem notaFiscalItem = NotaFiscalItemDAO.get(notaItemTrib.getCdNotaFiscal(), notaItemTrib.getCdItem(), connect);
					ProdutoServico produtoServico = ProdutoServicoDAO.get(notaFiscalItem.getCdProdutoServico(), connect);
					
					if(produtoServico.getCdClassificacaoFiscal() == ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTADO_INTEGRALMENTE", 0)){
						vlTributadoIntegralmente += (notaFiscalItem.getQtTributario() * notaFiscalItem.getVlUnitario() + notaFiscalItem.getVlAcrescimo() - notaFiscalItem.getVlDesconto());
					}
					else if(produtoServico.getCdClassificacaoFiscal() == ParametroServices.getValorOfParametroAsInteger("CD_SUBSTITUICAO_TRIBUTARIA", 0)){
						vlSubstituicaoTributaria += (notaFiscalItem.getQtTributario() * notaFiscalItem.getVlUnitario() + notaFiscalItem.getVlAcrescimo() - notaFiscalItem.getVlDesconto());
					}
				}
				
				
				//Faz um rateio dos tributos da nota para os tributos dos itens
				ArrayList<Integer> codigosComValorTributo = new ArrayList<Integer>();
				for(int k = 0; k < listaNotaFiscalItemTributo.size(); k++){
					NotaFiscalItemTributo notaItemTrib = listaNotaFiscalItemTributo.get(k);
					NotaFiscalTributo notaTributo = NotaFiscalTributoDAO.get(notaItemTrib.getCdNotaFiscal(), notaItemTrib.getCdTributo(), connect);
					NotaFiscalItem notaFiscalItem = NotaFiscalItemDAO.get(notaItemTrib.getCdNotaFiscal(), notaItemTrib.getCdItem(), connect);
					ProdutoServico produtoServico = ProdutoServicoDAO.get(notaFiscalItem.getCdProdutoServico(), connect);
					
					if(notaTributo != null){
						float participacao = 0;
						if(produtoServico.getCdClassificacaoFiscal() == ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTADO_INTEGRALMENTE", 0) && (notaTributo.getVlBaseCalculo() > 0 || notaTributo.getVlTributo() > 0)){
							codigosComValorTributo.add(notaTributo.getCdTributo());
							
							participacao = (notaFiscalItem.getQtTributario() * notaFiscalItem.getVlUnitario() + notaFiscalItem.getVlAcrescimo() - notaFiscalItem.getVlDesconto()) / vlTributadoIntegralmente;
							float vlBaseCalculoParticipacao = notaTributo.getVlBaseCalculo() * participacao;
							float vlTributoParticipacao = notaTributo.getVlTributo() * participacao;
							
							listaNotaFiscalItemTributo.get(k).setVlBaseCalculo(vlBaseCalculoParticipacao);
							listaNotaFiscalItemTributo.get(k).setVlTributo(vlTributoParticipacao);
						}
						
						else if(produtoServico.getCdClassificacaoFiscal() == ParametroServices.getValorOfParametroAsInteger("CD_SUBSTITUICAO_TRIBUTARIA", 0) && (notaTributo.getVlBaseRetencao() > 0 || notaTributo.getVlRetido() > 0)){
							codigosComValorTributo.add(notaTributo.getCdTributo());
							
							participacao = (notaFiscalItem.getQtTributario() * notaFiscalItem.getVlUnitario() + notaFiscalItem.getVlAcrescimo() - notaFiscalItem.getVlDesconto()) / vlSubstituicaoTributaria;
							float vlBaseCalculoSubstitutoParticipacao = notaTributo.getVlBaseRetencao() * participacao;
							float vlTributoSubtitutoParticipacao = notaTributo.getVlRetido() * participacao;
							listaNotaFiscalItemTributo.get(k).setVlBaseRetencao(vlBaseCalculoSubstitutoParticipacao);
							listaNotaFiscalItemTributo.get(k).setVlRetido(vlTributoSubtitutoParticipacao);
						}
					}
					
				}
				
				//Itera sobre os tributos dos itens para fazer o tributo da nota
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_nota_fiscal", "" + nota.getCdNotaFiscal(), ItemComparator.EQUAL, Types.INTEGER));
				for(int k = 0; k < listaNotaFiscalItemTributo.size(); k++){
					NotaFiscalItemTributo notaItemTrib = listaNotaFiscalItemTributo.get(k);
					NotaFiscalTributo notaTributo = NotaFiscalTributoDAO.get(notaItemTrib.getCdNotaFiscal(), notaItemTrib.getCdTributo(), connect);
					if(notaTributo == null)	{
						notaTributo = new NotaFiscalTributo(nota.getCdNotaFiscal(), notaItemTrib.getCdTributo(), 
															notaItemTrib.getVlBaseCalculo(), 
															notaItemTrib.getVlOutrasDespesas(), 0/*Outros Impostos*/, 
															notaItemTrib.getVlTributo(), 
															notaItemTrib.getVlBaseRetencao(), notaItemTrib.getVlRetido());
						if(NotaFiscalTributoDAO.insert(notaTributo, connect) <= 0)	{
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao gerar Nota Fiscal Tributo");
						}
					}
					else	{
						if(!codigosComValorTributo.contains(notaTributo.getCdTributo())){
							notaTributo.setVlBaseCalculo(notaTributo.getVlBaseCalculo() + notaItemTrib.getVlBaseCalculo());
							notaTributo.setVlBaseRetencao(notaTributo.getVlBaseRetencao() + notaItemTrib.getVlBaseRetencao());
							notaTributo.setVlOutrasDespesas(notaTributo.getVlOutrasDespesas() +notaItemTrib.getVlOutrasDespesas());
							notaTributo.setVlOutrosImpostos(0);
							notaTributo.setVlRetido(notaTributo.getVlRetido() + notaItemTrib.getVlRetido());
							notaTributo.setVlTributo(notaTributo.getVlTributo() + notaItemTrib.getVlTributo());
							NotaFiscalTributoDAO.update(notaTributo, connect);
						}
					}
					
				}
				
			}
			//Insere a lista de nota fiscal item tributo no banco
			for(int k = 0; k < listaNotaFiscalItemTributo.size(); k++){
				if(NotaFiscalItemTributoDAO.insert(listaNotaFiscalItemTributo.get(k), connect) <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao gerar Nota Fiscal Item Tributo");
				}
			}
			
			//Itera sobre os tributos da nota para saber o que é imposto direto e qual é de icms por substituicao tributaria
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_nota_fiscal", "" + nota.getCdNotaFiscal(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = NotaFiscalTributoDAO.find(criterios, connect);
			rsm.beforeFirst();
			while(rsm.next()){
				Tributo tributo = TributoDAO.get(rsm.getInt("cd_tributo"), connect);
				if(tributo.getIdTributo().equals("ICMS")){
					vlIcmsSt += rsm.getFloat("vl_retido");
				}
				else if(tributo.getIdTributo().equals("IPI")){
					vlImpostosDiretos += rsm.getFloat("vl_tributo");					
				}
				else if(tributo.getIdTributo().equals("II")){
					vlImpostosDiretos += rsm.getFloat("vl_tributo");
				}
			}
			// Somando o total da nota - Acrescentar impostos diretos e icms st
			nota.setVlTotalNota(nota.getVlTotalNota() + vlFrete + vlSeguro + vlIcmsSt + vlImpostosDiretos);
			
			nota.setTxtObservacao(nota.getTxtObservacao() + (dsCupons.equals("") && dsDAVs.equals("") ? "" : "Em substituição ao(s) documento(s): "+(!dsCupons.equals("") ? "CUPOM(NS) No: "+dsCupons+"." : "")+
		               (!dsDAVs.equals("") ? " DAV(S) No: "+dsDAVs+"." : "")));
			
			if(ParametroServices.getValorOfParametroAsInteger("LG_EXIBIR_NUMERO_NOTA", 0, cdEmpresa)==1){ 
					nota.setTxtObservacao( nota.getTxtObservacao() + ((nota.getTxtObservacao() != null && !nota.getTxtObservacao().trim().equalsIgnoreCase("")) ? "" : "") + "No " + nrDocumentosVinculados);
			} else {
				nota.setTxtObservacao(nota.getTxtObservacao());
			}
			if(NotaFiscalDAO.update(nota, connect) <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar nota!");
			}
			if(isConnectionNull)
				connect.commit();
			//Descobrir desconto maximo que pode ser aplicado
			float prDescontoMaximo = 0;
			float somaItens        = 0;
			float somaDescontado   = 0;
			ResultSetMap rsmItens = NotaFiscalItemServices.getAllByCdNfe(nota.getCdNotaFiscal(), nota.getCdEmpresa(), connect);
			while(rsmItens.next()){
				somaItens += (Util.arredondar(rsmItens.getFloat("vl_unitario"), rsmItens.getInt("qt_precisao_custo")) * Util.arredondar(rsmItens.getFloat("qt_tributario"), rsmItens.getInt("nr_precisao_medida")));
				somaDescontado += (Util.arredondar(rsmItens.getFloat("vl_unitario"), rsmItens.getInt("qt_precisao_custo")) * Util.arredondar(rsmItens.getFloat("qt_tributario"), rsmItens.getInt("nr_precisao_medida"))) - ((Util.arredondar(rsmItens.getFloat("vl_unitario"), rsmItens.getInt("qt_precisao_custo")) * Util.arredondar(rsmItens.getFloat("qt_tributario"), rsmItens.getInt("nr_precisao_medida"))) * rsmItens.getFloat("pr_desconto_maximo") / 100);
			}
			float r = somaDescontado / somaItens;
			prDescontoMaximo = 100 - (r * 100);
			prDescontoMaximo = (float)Util.arredondar(prDescontoMaximo, 2);
			//--------------------------------- DESCONTO AUTORIZADO ---------------------------------//
			update(nota, prDesconto, prDescontoMaximo, tpDesconto, 0, 0/*Ignorar valor de acrescimo*/);
			//
			Result resultado = new Result(1, "Nota Fiscal EletrÃ´nica cadastrada com sucesso!");
			HashMap<String, Object> registro = new HashMap<String, Object>();
			registro.put("cdNotaFiscal", nota.getCdNotaFiscal());
			registro.put("prDescontoMaximo", prDescontoMaximo);			
			resultado.setObjects(registro);
			return resultado;
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			Util.registerLog(e); // Registra o log quando chamado pelo PDV
			return new Result(-10, "Falha ao tentar enviar documento para a fila de validação!", e);
		}
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	

	/**
	 * Retorna as formas de pagamento que sejam cadastrados como boleto
	 * 
	 * @return
	 * @author Luiz Romario Filho
	 * @since 13/11/2014
	 */
	public static ResultSetMap getTiposBoleto() {
		return ParametroServices.getValoresOfParametro("CD_TIPO_DOCUMENTO_BOLETO");
	}
	
	/**
	 * Testa se uma string é null ou vazia
	 * @param value
	 * @return
	 */
	private static boolean isNullOrBlank(String value){
		return value == null || "".equalsIgnoreCase(value.trim());
	}
	
	/**
	 * Metodo para transferir os dados de uma nota de entrada e transforma-la em uma NFe
	 * @throws SQLException 
	 * */	
	public static Result fromDocEntradaToNF(int cdDocumentosEntrada, int tpDesconto, float prDesconto) {
		return fromDocEntradaToNF(cdDocumentosEntrada, tpDesconto, prDesconto, null);
	}
	public static Result fromDocEntradaToNF(int cdDocumentosEntrada, int tpDesconto, float prDesconto, Connection connect) {
		try{
			ArrayList<Integer> docs = new ArrayList<Integer>();
			docs.add(new Integer(cdDocumentosEntrada));
			return fromDocEntradaToNF(tpDesconto, prDesconto, docs, connect);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			Util.registerLog(e); // Registra o log quando chamado pelo PDV
			return new Result(-1, "Falha ao tentar enviar documento para a fila de validação!", e);
		}
	}
	
	public static Result fromDocEntradaToNF(int cdDocumentosEntrada) {
		try{
			ArrayList<Integer> docs = new ArrayList<Integer>();
			docs.add(new Integer(cdDocumentosEntrada));
			return fromDocEntradaToNF(0, 0, docs);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			Util.registerLog(e); // Registra o log quando chamado pelo PDV
			return new Result(-1, "Falha ao tentar enviar documento para a fila de validação!", e);
		}
	}
	
	
	public static Result fromDocEntradaToNFArray(ArrayList<HashMap<String,Object>> registros)	{
		try	 {
			ArrayList<Integer> documentosEntrada = new ArrayList<Integer>();
			for(int i = 0; i < registros.size(); i++){
				int cdDocumentoEntrada = registros.get(i)==null || registros.get(i).get("cdDocumentoEntrada")==null ? 0 : (Integer) registros.get(i).get("cdDocumentoEntrada");
				documentosEntrada.add(cdDocumentoEntrada);
			}
			Result retorno   = fromDocEntradaToNF(-1, -1, documentosEntrada);
			return retorno;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e); // Registra o log quando chamado pelo PDV
			return new Result(-1, "Erro: " + e, e);
		}
	}
	
	public static Result fromDocEntradaToNF(int tpDesconto, float prDesconto, ArrayList<Integer> documentosEntrada) {
		return fromDocEntradaToNF(tpDesconto, prDesconto, documentosEntrada, null);
	}
	
	public static Result fromDocEntradaToNF(int tpDesconto, float prDesconto, ArrayList<Integer> documentosEntrada, Connection connect) {
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			// Verifica se a lista é válida ou é vazia
			if(documentosEntrada.size() == 0 || documentosEntrada == null)
				return new Result(-1, "Array de códigos de documento vazio ou inexistente");
			
			//Buscar endereço principal de uma pessoa
			PreparedStatement pstmtEnd = connect.prepareStatement("SELECT A.*, B.nm_cidade, B.cd_estado FROM grl_pessoa_endereco A " +
					                                              "LEFT OUTER JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade) " +
                                                                  "WHERE cd_pessoa    = ? "+
          														  "  AND lg_principal = 1");
			
			int cdFornecedor       = 0;
			int cdEmpresa          = 0;
			int cdNaturezaOperacao = 0;
			int cdTransportador    = 0;	
			float vlSeguro         = 0;
			float vlFrete          = 0;
			float vlIcmsSt	       = 0;
			float vlImpostosDiretos= 0;
			float vlPesoBruto = 0;
			float vlPesoLiquido = 0;
			String dsEspecie = "";
			float qtVolumes = 0;
			for(int i = 0; i < documentosEntrada.size(); i++) {
				DocumentoEntrada doc = DocumentoEntradaDAO.get(documentosEntrada.get(i), connect);
				vlFrete += doc.getVlFrete();
				vlSeguro += doc.getVlSeguro();
				// Cliente
				if(doc.getCdFornecedor() > 0)
					cdFornecedor = doc.getCdFornecedor();
				// Empresa
				if(doc.getCdEmpresa() > 0)
					cdEmpresa = doc.getCdEmpresa();
				// Natureza da Operação
				if(doc.getCdNaturezaOperacao() > 0){
					cdNaturezaOperacao = doc.getCdNaturezaOperacao();
				}
				//
				if (cdEmpresa <= 0 || cdFornecedor <= 0 || cdNaturezaOperacao <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Codigo de Empresa ou Cliente ou Natureza de Operação não configurado para Documento de saída N. " +doc.getNrDocumentoEntrada());
				}
				
				if(doc.getCdTransportadora() > 0)
					cdTransportador = doc.getCdTransportadora();
				vlPesoBruto += doc.getVlPesoBruto();
				vlPesoLiquido += doc.getVlPesoLiquido();
				dsEspecie = doc.getDsEspecieVolumes();
				qtVolumes += doc.getQtVolumes();
			}
			// Busca endereço da empresa emitente
			ResultSet rsEnd = connect.prepareStatement("SELECT * FROM grl_pessoa_endereco WHERE cd_pessoa = " + cdEmpresa).executeQuery();
			int cdEnderecoRetirada = 0;
			int cdCidade           = 0;
			if(rsEnd.next())	{
				cdEnderecoRetirada = rsEnd.getInt("cd_endereco");
				cdCidade           = rsEnd.getInt("cd_cidade");
			}
			else
				return new Result(-1, "Empresa emitente não possui endereço cadastrado");
			// Endereço do FORNECEDOR
			ResultSet rsEndFornecedor = connect.prepareStatement("SELECT * FROM grl_pessoa_endereco WHERE cd_pessoa = " + cdFornecedor).executeQuery();
			int cdEnderecoFornecedor = 0;
			if(rsEndFornecedor.next())
				cdEnderecoFornecedor = rsEnd.getInt("cd_endereco");
			else{
				Pessoa fornecedor = PessoaDAO.get(cdFornecedor, connect);
				return new Result(-1, "Destinatário "+fornecedor.getNmPessoa()+" não possui endereço cadastrado");
			}
			// Busca e atualiza o número da nota fiscal
			int nrNotaFiscal = 0;
			if(ParametroServices.getValorOfParametroAsInteger("LG_NUMERACAO_PARAMETRO", 0, cdEmpresa)!=1 && ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa)==0){
				ResultSetMap rsmNrNota = new ResultSetMap(connect.prepareStatement("SELECT * FROM FSC_NOTA_FISCAL " +
																				"		WHERE CD_EMPRESA = " + cdEmpresa + 
																				"		AND NR_SERIE = '" + (getTipoAmbiente(cdEmpresa) == HOMOLOGACAO ? "888" : ParametroServices.getValorOfParametro("NR_SERIE", "1", cdEmpresa)) + "'" +  
																				"		ORDER BY CD_NOTA_FISCAL DESC LIMIT 1").executeQuery());
				if(rsmNrNota.next()){
					nrNotaFiscal = Integer.parseInt(rsmNrNota.getString("NR_NOTA_FISCAL")) + 1;
					ParametroServices.updateValueOfParametro(ParametroServices.getByName("NR_NOTA_FISCAL").getCdParametro(), String.valueOf(nrNotaFiscal), cdEmpresa, connect);
				}
				else{
					nrNotaFiscal = ParametroServices.getValorOfParametroAsInteger("NR_NOTA_FISCAL", 1, cdEmpresa, connect) + 1;
					ParametroServices.updateValueOfParametro(ParametroServices.getByName("NR_NOTA_FISCAL").getCdParametro(), String.valueOf(nrNotaFiscal), cdEmpresa, connect);
				}
			}
			else if(ParametroServices.getValorOfParametroAsInteger("LG_NUMERACAO_PARAMETRO", 0, cdEmpresa)==1 && ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa)==0){
				nrNotaFiscal = ParametroServices.getValorOfParametroAsInteger("NR_NOTA_FISCAL", 1, cdEmpresa, connect) + 1;
				ParametroServices.updateValueOfParametro(ParametroServices.getByName("NR_NOTA_FISCAL").getCdParametro(), String.valueOf(nrNotaFiscal), cdEmpresa, connect);
			}
			//
			int nrSerie = ParametroServices.getValorOfParametroAsInteger("NR_SERIE", 1, cdEmpresa, connect);
			if(getTipoAmbiente(cdEmpresa)==HOMOLOGACAO)
				nrSerie = 888;
			// GRAVANDO NOTA FISCAL
			String txtObservacaoFisco = "DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL. NÃO GERA DIREITO A CRÉDITO FISCAL DE IPI/ICMS.";
			if(ParametroServices.getValorOfParametroAsInteger("TP_REGIME_TRIBUTARIO", 0) == 3){
				txtObservacaoFisco = "";
			}
			NotaFiscal nota = new NotaFiscal(0, cdEmpresa, cdEnderecoRetirada, cdNaturezaOperacao, cdCidade, cdFornecedor, cdEnderecoFornecedor, 
											 cdEnderecoFornecedor, 0, 55/*CAMPO FIXO*/, String.valueOf(nrSerie), (nrNotaFiscal == 0 ? null : String.valueOf(nrNotaFiscal)), 
											 EM_DIGITACAO, new GregorianCalendar(), ENTRADA, new GregorianCalendar(), 
											  A_VISTA /*Pesquisar como foi feito o pagamento e colocar a constante aqui depois*/, EMI_NORMAL, NFE_NORMAL, 
											  RETRATO, 0, vlSeguro/*vlSeguro*/, 0/*OUTRAS DESPESAS*/,  0, SEM_FRETE, ""/*txtObservacao*/, 
											  txtObservacaoFisco/*INFORMACOES RELACIONADAS AO FISCO*/, 0, 
											  null /*MUDAR PELO QUE SERA GERADO POR GERARIDNFE*/,
											  0 /*DV - deve ser gerado a partir da chave de acesso - Foi omitido ao atualizar a classe*/, 
											  null/*PROTOCOLO DE AUTORIZACAO - QUE RECEBE DA SEFAZ, buscar após transmitir a nota*/, 
											  null/*DATA DE AUTORIZAÇÃO*/,  cdTransportador/*cdTransportador*/, 0/*NATUREZA DE OPERACAO DO FRETE*/, 
											  vlFrete /*vlFrete*/, 0/*vlFreteBaseIcms*/, 0/*vlFreteIcmsRetido*/, null/*nrRecebimento*/, 
											  null /*nrPlaca*/, "" + qtVolumes /*qtVolume*/, dsEspecie /*dsEspecie*/, null /*dsMarca*/,
											  null /*dsNumeracao*/, vlPesoBruto /*vlPesoBruto*/, vlPesoLiquido /*vlPesoLiquido*/,0 /*cdVeiculo*/,
											  null/*sgUfVeiculo*/, null/*nrRntc*/, 0/*cdMotivoCancelamento*/, null/*txtXml*/, 0/*prDesconto*/, 1/*lgConsumidorFinal*/, TP_PRESENCA_PRESENCIAL/*tpVendaPresenca*/, null/*nrChaveAcessoReferencia*/);
			
//			float qtVolumes = 0;
//			float vlPesoBruto = 0;
//			float vlPesoLiquido = 0;
//			String dsEspecie = "";
			
			//Caso seja configurado pelo cliente, as informações da nota fiscal eletronica seram recebidas da viagem como veiculo
			if(ParametroServices.getValorOfParametroAsInteger("LG_BUSCAR_INFORMACOES_VIAGEM", 0, cdEmpresa, connect) == 1){
				if(documentosEntrada.isEmpty()){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Nota Fiscal sem documentos de entrada");
				}
				
				DocumentoEntrada docEntrada = DocumentoEntradaDAO.get(documentosEntrada.get(0), connect);
				//Pesquisa para saber se algum documento de saida passado tem uma viagem vinculada
				int i = 0;
				while(docEntrada.getCdViagem() == 0 && i < documentosEntrada.size()){
					docEntrada = DocumentoEntradaDAO.get(documentosEntrada.get(i++), connect);
				}
				
				if(docEntrada.getCdViagem() > 0){
					Viagem viagem = ViagemDAO.get(docEntrada.getCdViagem(), connect);
					nota.setCdVeiculo(viagem.getCdVeiculo());					
				}
				
//				qtVolumes += docEntrada.getQtVolumes();
//				vlPesoBruto += docEntrada.getVlPesoBruto();
//				vlPesoLiquido += docEntrada.getVlPesoLiquido();
//				dsEspecie = docEntrada.getDsEspecieVolumes();
				
			}
			
//			nota.setQtVolume(Util.formatNumber(qtVolumes, 2));
//			nota.setVlPesoBruto(vlPesoBruto);
//			nota.setVlPesoLiquido(vlPesoLiquido);
//			nota.setDsEspecie(dsEspecie);
			
			nota.setCdNotaFiscal(NotaFiscalDAO.insert(nota, connect));
			if(nota.getCdNotaFiscal() <= 0){
				Conexao.rollback(connect);
				return new Result(-1, "Erro ao cadastrar Nota");
			}
			// GERANDO ID - NFE
			if(nota.getNrNotaFiscal() != null){
				Result result = gerarNfeId(nota);
				if(result.getCode() <= 0)	{
					Conexao.rollback(connect);
					result.setMessage(result.getMessage()+" Falha ao tentar gerar ID da Nota Fiscal.");
					return result;
				}
				nota.setNrChaveAcesso((String)result.getObjects().get("nrChave"));
				nota.setNrDv((Integer)result.getObjects().get("nrDv"));
			}
			if(NotaFiscalDAO.update(nota, connect) <= 0)	{
				Conexao.rollback(connect);
				return new Result(-1, "Falha ao atualizar ID da NFE");
			}
			//
			String dsCupons = "", dsDAVs = "";
			int qtItensTotal = 0;
			float vlOutrasDespesas = 0;
			boolean isImportacao = false;
			
			//ArrayList para guardar as nota fiscal item tributo que serão adicionadas após a inserição das nota fiscal tributo
			ArrayList<NotaFiscalItemTributo> listaNotaFiscalItemTributo = new ArrayList<NotaFiscalItemTributo>();
			for(int i = 0; i < documentosEntrada.size(); i++)	{
				// Pega o documento vinculado
				DocumentoEntrada doc = DocumentoEntradaDAO.get(documentosEntrada.get(i), connect);
				if(doc == null)	{
					Conexao.rollback(connect);
					return new Result(-1, "Documento não encontrado!");
				}
				
				isImportacao = doc.getCdNaturezaOperacao() == ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_ENTRADA_IMPORTACAO_DEFAULT", 0);
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_documento_entrada", "" + doc.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmEventoFinanceiro = EntradaEventoFinanceiroDAO.find(criterios, connect);
				while(rsmEventoFinanceiro.next()){
					if(rsmEventoFinanceiro.getInt("lg_fiscal") == 1 && rsmEventoFinanceiro.getInt("lg_despesa_acessoria") == 1 && rsmEventoFinanceiro.getFloat("cd_evento_financeiro") != ParametroServices.getValorOfParametroAsInteger("CD_EVENTO_FINANCEIRO_CAPATAZIA", 0)){
						vlOutrasDespesas += rsmEventoFinanceiro.getFloat("vl_evento_financeiro");
					}
				}
				rsmEventoFinanceiro.beforeFirst();
						
				//Busca do estado do cliente
				int cdEstadoDest   = 0;
				int cdCidadeDest   = 0;
				if(cdFornecedor > 0) {
					pstmtEnd.setInt(1, cdFornecedor);
					rsEnd = pstmtEnd.executeQuery(); 
					if(rsEnd.next()){
						cdEstadoDest = rsEnd.getInt("cd_estado");
						cdCidadeDest = rsEnd.getInt("cd_cidade");
					}

				}
				
				if(doc.getStDocumentoEntrada() == DocumentoSaidaServices.ST_CANCELADO)	{
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Documento de entrada N. " +doc.getNrDocumentoEntrada()+" cancelado!");
				}
				boolean permitir = (Integer.parseInt(ParametroServices.getValorOfParametro("LG_PERMITIR_NOTA_DOC_CONFERENCIA", cdEmpresa, connect)) == 0) ? false : true;
				if(!permitir){
					if(doc.getStDocumentoEntrada() == DocumentoSaidaServices.ST_EM_CONFERENCIA)	{
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Documento de entrada N. " +doc.getNrDocumentoEntrada()+" em conferência!");
					}
				}
				//
				if(NotaFiscalDocVinculadoServices.findByDocEntrada(doc.getCdDocumentoEntrada(), connect).next()){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Documento de entrada N. " +doc.getNrDocumentoEntrada()+" ja vinculado com uma nota fiscal!");
				}
				//
				if(doc.getCdFornecedor()>0 && doc.getCdFornecedor()!=cdFornecedor){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Os documentos de entrada devem ser do mesmo fornecedor!");
				}
				// 
				if(NotaFiscalDocVinculadoDAO.insert(new NotaFiscalDocVinculado(nota.getCdNotaFiscal(), doc.getCdDocumentoEntrada(), 0, 0, doc.getCdDocumentoEntrada(), ENTRADA), connect) <= 0)	{
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao vincular Nota Fiscal a Documento");
				}
				
				
				ResultSetMap rsProds = new ResultSetMap(connect.prepareStatement("SELECT A.*, D.*, H.cd_grupo, B.qt_precisao_custo, C.nr_precisao_medida, " +
						                                     "     (SELECT SUM((J.pr_aliquota/100) * J.vl_base_calculo) " +
															 "      FROM adm_entrada_item_aliquota J " + 
															 "      WHERE J.cd_produto_servico   = D.cd_produto_servico " + 
															 "        AND J.cd_documento_entrada = D.cd_documento_entrada " + 
															 "        AND J.cd_empresa           = D.cd_empresa) AS vl_tributo  " +
															 "FROM alm_documento_entrada_item D " +
															 "LEFT OUTER JOIN grl_produto_servico         A ON (A.cd_produto_servico  = D.cd_produto_servico)  " +
															 "LEFT OUTER JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico  = B.cd_produto_servico" +
															 "														AND B.cd_empresa = "+cdEmpresa+")  " +
															 "LEFT OUTER JOIN grl_unidade_medida        C ON (C.cd_unidade_medida = B.cd_unidade_medida) " +
															 "LEFT OUTER JOIN alm_produto_grupo        H ON (H.cd_produto_servico = D.cd_produto_servico " +
															 "                                           AND H.cd_empresa            = D.cd_empresa " +
															 "                                           AND H.lg_principal = 1) " +
															 "WHERE D.cd_documento_entrada = " + doc.getCdDocumentoEntrada()).executeQuery());
								
				int qtItens       = 0;
				//Saber onde vai entrar
				while(rsProds.next())	{
					qtItens++;
					qtItensTotal++;
					
					if(rsProds.getFloat("vl_unitario") <= 0 || rsProds.getFloat("qt_entrada") <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Produto " + rsProds.getString("nm_produto_servico") + " esta com valor unitário ou quantidade zerados no documento de entrada No " + doc.getNrDocumentoEntrada());
					}
					
					/*
					 *  INCLUIDO OS ITENS
					 */
					int cdNaturezaOperacaoItem = rsProds.getInt("cd_natureza_operacao");
					if(cdNaturezaOperacaoItem == 0)
						cdNaturezaOperacaoItem = doc.getCdNaturezaOperacao();
					
					if(cdEstadoDest == 0 || cdCidadeDest == 0){
						Conexao.rollback(connect);
						return new Result(-1, "Destinatário precisa ter cidade e estado cadastrados!");
					}
					
					//Calcula o valor dos tributos
					ResultSetMap rsmTributos = TributoServices.calcTributos(TributoAliquotaServices.OP_COMPRA, cdNaturezaOperacaoItem, rsProds.getInt("cd_classificacao_fiscal"), 
																			rsProds.getInt("cd_produto_servico"), 0, cdEstadoDest, cdCidadeDest, (rsProds.getFloat("qt_entrada") * rsProds.getFloat("vl_unitario") - rsProds.getFloat("vl_desconto")),
																			connect);
					
					ResultSetMap rsmTributoOrigem = new ResultSetMap(connect.prepareStatement("SELECT * FROM adm_entrada_item_aliquota WHERE cd_documento_entrada = " + doc.getCdDocumentoEntrada() + " AND cd_produto_servico = " + rsProds.getInt("cd_produto_servico") + " AND cd_item = " + rsProds.getInt("cd_item")).executeQuery());
					
					
					rsmTributos.beforeFirst();
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_produto_servico", "" + rsProds.getInt("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_nota_fiscal", "" + nota.getCdNotaFiscal(), ItemComparator.EQUAL, Types.INTEGER));
					//Se ja tiver um item com esse produto, o sistema ira fazer uma media ponderada com o valor unitario, e irão acrescer no item a quantidade tributaria, o valor de acrescimo e o valor de desconto
					ResultSetMap rsm = NotaFiscalItemDAO.find(criterios, connect);
					if(rsm.next()){
						
						float qtEntrada       = (float)Util.arredondar(rsProds.getFloat("qt_entrada"), (rsProds.getInt("nr_precisao_medida") > 4 ? 4 : (rsProds.getInt("nr_precisao_medida") <= 0 ? 0 : rsProds.getInt("nr_precisao_medida"))));
						float vlUnitario      = (float)Util.arredondar(rsProds.getFloat("vl_unitario"), (rsProds.getInt("qt_precisao_custo") > 10 ? 10 : (rsProds.getInt("qt_precisao_custo") <= 0 ? 2 : rsProds.getInt("qt_precisao_custo"))));
						NotaFiscalItem notaItem = NotaFiscalItemDAO.get(rsm.getInt("cd_nota_fiscal"), rsm.getInt("cd_item"), connect);
						float novoVlUnitario = ((notaItem.getQtTributario() * notaItem.getVlUnitario()) + (qtEntrada * vlUnitario)) / (notaItem.getQtTributario() + qtEntrada);
						
						nota.setVlTotalProduto(nota.getVlTotalProduto() - (notaItem.getVlUnitario() * notaItem.getQtTributario()));
						nota.setVlTotalNota(nota.getVlTotalNota() - (notaItem.getVlUnitario() * notaItem.getQtTributario()));
						notaItem.setQtTributario(notaItem.getQtTributario() + qtEntrada);
						notaItem.setVlUnitario(novoVlUnitario);
						
						float novoVlTotalProduto = (rsProds.getInt("qt_precisao_custo") <= 0 ? (float)Util.arredondar(nota.getVlTotalProduto() + (notaItem.getVlUnitario() * notaItem.getQtTributario()), 2) : (float)Util.arredondar(nota.getVlTotalProduto() + (notaItem.getVlUnitario() * notaItem.getQtTributario()), rsProds.getInt("qt_precisao_custo")));
						nota.setVlTotalProduto(novoVlTotalProduto);
						//Os valores de acrescimo e desconto são acrescidos
						notaItem.setVlAcrescimo(notaItem.getVlAcrescimo() + rsProds.getFloat("vl_acrescimo"));
						notaItem.setVlDesconto(notaItem.getVlDesconto() + rsProds.getFloat("vl_desconto"));
						//O valor da nota é acrescido pela multiplicação do novo valor unitario e a nova quantidade tributada menos o novo valor de desconto
						float novoVlTotalNota = (rsProds.getInt("qt_precisao_custo") <= 0 ? (float)Util.arredondar(nota.getVlTotalNota() + (notaItem.getVlUnitario() * notaItem.getQtTributario() - rsProds.getFloat("vl_desconto")), 2) : (float)Util.arredondar(nota.getVlTotalNota() + (notaItem.getVlUnitario() * notaItem.getQtTributario() - rsProds.getFloat("vl_desconto")), rsProds.getInt("qt_precisao_custo")));
						nota.setVlTotalNota(novoVlTotalNota);
						
						if(NotaFiscalItemDAO.update(notaItem, connect) <= 0){
							connect.rollback();
							return new Result(-1, "Erro ao cadastrar Produto da Nota!");
						}
						
						/*
						 *  ATUALIZANDO OS TOTAIS DOS TRIBUTOS 
						 */
						
						while(!isImportacao && rsmTributos.next()){
							int cdTributo         	 = rsmTributos.getInt("cd_tributo");
							float vlBaseCalculo      = (rsmTributos.getFloat("pr_aliquota") > 0) ? rsmTributos.getFloat("vl_base_calculo") : 0;
							float vlBaseCalculoRet   = (rsmTributos.getFloat("pr_aliquota_substituicao") > 0) ? rsmTributos.getFloat("vl_base_calculo") : 0;
							float vlTributo		  	 = vlBaseCalculo * rsmTributos.getFloat("pr_aliquota") / 100;
							float vlRetido 		  	 = vlBaseCalculo * rsmTributos.getFloat("pr_aliquota_substituicao") / 100;
							
							//Busca o tributo do item
							NotaFiscalItemTributo notaItemTributo = null;
							int j = 0;
							for(; j < listaNotaFiscalItemTributo.size(); j++){
								NotaFiscalItemTributo notaItemTribAux = listaNotaFiscalItemTributo.get(j);
								if(notaItemTribAux.getCdNotaFiscal() == nota.getCdNotaFiscal() &&
								   notaItemTribAux.getCdItem() == rsm.getInt("cd_item") &&
								   notaItemTribAux.getCdTributo() == cdTributo){
									notaItemTributo = (NotaFiscalItemTributo) notaItemTribAux.clone();
									break;
								}
							}
							if(notaItemTributo == null){
								Conexao.rollback(connect);
								return new Result(-1, "Erro: Item sem tributo!");
							}
							//Aumenta a base de calculo com a nova
							//São entrarão se a base de calculo for maior do que zero, significando que não é o padrão configurado 102 sem permissão de credito
							if(notaItemTributo.getVlBaseCalculo() > 0){
								notaItemTributo.setVlBaseCalculo(notaItemTributo.getVlBaseCalculo() + vlBaseCalculo);
								notaItemTributo.setVlBaseRetencao(notaItemTributo.getVlBaseRetencao() + vlBaseCalculoRet);
								//Recalcula o valor de tributo e o valor de credito
								notaItemTributo.setVlTributo(notaItemTributo.getVlTributo() + vlTributo);
								notaItemTributo.setVlRetido(notaItemTributo.getVlRetido() + vlRetido);
								notaItemTributo.setVlCredito(notaItemTributo.getVlCredito() + vlBaseCalculo * notaItemTributo.getPrCredito() / 100);
								//Atualiza a nota fiscal item tributo
								listaNotaFiscalItemTributo.set(j, notaItemTributo);
							}
							
						} 
						
						while(isImportacao && rsmTributoOrigem.next()){
							int cdTributo         	 = rsmTributoOrigem.getInt("cd_tributo");
							float vlBaseCalculo      = (rsmTributoOrigem.getFloat("pr_aliquota") > 0) ? rsmTributoOrigem.getFloat("vl_base_calculo") : 0;
							float vlBaseCalculoRet   = (rsmTributoOrigem.getFloat("pr_aliquota_substituicao") > 0) ? rsmTributoOrigem.getFloat("vl_base_calculo") : 0;
							float vlTributo		  	 = vlBaseCalculo * rsmTributoOrigem.getFloat("pr_aliquota") / 100;
							float vlRetido 		  	 = vlBaseCalculo * rsmTributoOrigem.getFloat("pr_aliquota_substituicao") / 100;
							
							//Busca o tributo do item
							NotaFiscalItemTributo notaItemTributo = null;
							int j = 0;
							for(; j < listaNotaFiscalItemTributo.size(); j++){
								NotaFiscalItemTributo notaItemTribAux = listaNotaFiscalItemTributo.get(j);
								if(notaItemTribAux.getCdNotaFiscal() == nota.getCdNotaFiscal() &&
								   notaItemTribAux.getCdItem() == rsm.getInt("cd_item") &&
								   notaItemTribAux.getCdTributo() == cdTributo){
									notaItemTributo = (NotaFiscalItemTributo) notaItemTribAux.clone();
									break;
								}
							}
							if(notaItemTributo == null){
								Conexao.rollback(connect);
								return new Result(-1, "Erro: Item sem tributo!");
							}
							//Aumenta a base de calculo com a nova
							//São entrarão se a base de calculo for maior do que zero, significando que não é o padrão configurado 102 sem permissão de credito
							if(notaItemTributo.getVlBaseCalculo() > 0){
								notaItemTributo.setVlBaseCalculo(notaItemTributo.getVlBaseCalculo() + vlBaseCalculo);
								notaItemTributo.setVlBaseRetencao(notaItemTributo.getVlBaseRetencao() + vlBaseCalculoRet);
								//Recalcula o valor de tributo e o valor de credito
								notaItemTributo.setVlTributo(notaItemTributo.getVlTributo() + vlTributo);
								notaItemTributo.setVlRetido(notaItemTributo.getVlRetido() + vlRetido);
								notaItemTributo.setVlCredito(notaItemTributo.getVlCredito() + vlBaseCalculo * notaItemTributo.getPrCredito() / 100);
								//Atualiza a nota fiscal item tributo
								listaNotaFiscalItemTributo.set(j, notaItemTributo);
							}
						}
					}
					else	{
						float qtEntrada       = (float)Util.arredondar(rsProds.getFloat("qt_entrada"), (rsProds.getInt("nr_precisao_medida") > 4 ? 4 : (rsProds.getInt("nr_precisao_medida") <= 0 ? 0 : rsProds.getInt("nr_precisao_medida"))));
						float vlUnitario      = (float)Util.arredondar(rsProds.getFloat("vl_unitario"), (rsProds.getInt("qt_precisao_custo") > 10 ? 10 : (rsProds.getInt("qt_precisao_custo") <= 0 ? 2 : rsProds.getInt("qt_precisao_custo"))));
						float vlDescontoItem  = rsProds.getFloat("vl_desconto");
						float vlAcrescimoItem = rsProds.getFloat("vl_acrescimo");
						
						int cdNotaFiscalItem = NotaFiscalItemDAO.insert(new NotaFiscalItem(nota.getCdNotaFiscal(), qtItensTotal, 0, rsProds.getInt("cd_produto_servico"), 
				                                                                           doc.getCdEmpresa(), doc.getCdDocumentoEntrada(), cdNaturezaOperacaoItem, qtEntrada, vlUnitario, 
				                                                                           rsProds.getString("txt_especificacao"), rsProds.getInt("cd_item"), vlAcrescimoItem, vlDescontoItem), connect);
						// Total dos Produtos
						if(cdNotaFiscalItem <= 0){
							connect.rollback();
							return new Result(-1, "Erro ao cadastrar Produto da Nota");
						}
						
						/*
						 *  INCLUIDO OS TOTAIS DOS TRIBUTOS 
						 */
						//INSERE TRIBUTO COM SITUACAO padrão CONFIGURADA
						if(!isImportacao && rsmTributos.getLines().size() == 0){
							
							//:TODO atualizar para que o usuario defina a partir do parametro todos os valores padroes de tributo
							int cdTributo         	 = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0);
							int cdTributoAliquota    = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ALIQUOTA_ICMS_COMPRAS", 0);
							float prAliquota      	 = 0;
							if(ParametroServices.getValorOfParametroAsInteger("TP_REGIME_TRIBUTARIO", 0)==3)
								prAliquota = 17;
							float prCredito       	 = 0;
							float vlBaseCalculo   	 = 0;
							if(ParametroServices.getValorOfParametroAsInteger("TP_REGIME_TRIBUTARIO", 0)==3){
								vlUnitario    = (rsProds.getInt("qt_precisao_custo") <= 0 ? (float)Util.arredondar(vlUnitario * (1 - (prDesconto /100)), 2) : (float)Util.arredondar(vlUnitario * (1 - (prDesconto /100)), rsProds.getInt("qt_precisao_custo")));
								vlBaseCalculo = (rsProds.getInt("qt_precisao_custo") <= 0 ? (float)Util.arredondar((vlUnitario * qtEntrada + vlAcrescimoItem - vlDescontoItem), 2) : (float)Util.arredondar((vlUnitario * qtEntrada + vlAcrescimoItem - vlDescontoItem), rsProds.getInt("qt_precisao_custo")));
							}
							int tpCalculo            = 0;
							//Busca da situacao tributaria padrão 102
							criterios = new ArrayList<ItemComparator>();
							criterios.add(new ItemComparator("cd_tributo", "" + cdTributo, ItemComparator.EQUAL, Types.INTEGER));
							if(ParametroServices.getValorOfParametroAsInteger("TP_REGIME_TRIBUTARIO", 0)==1 || ParametroServices.getValorOfParametroAsInteger("TP_REGIME_TRIBUTARIO", 0)==2)
								criterios.add(new ItemComparator("nr_situacao_tributaria", "102", ItemComparator.LIKE, Types.VARCHAR));
							else
								criterios.add(new ItemComparator("nr_situacao_tributaria", "00", ItemComparator.LIKE, Types.VARCHAR));
							ResultSetMap rsmSitTrib = SituacaoTributariaDAO.find(criterios, connect);
							int cdSituacaoTributaria = 0;
							if(rsmSitTrib.next())
								cdSituacaoTributaria = rsmSitTrib.getInt("cd_situacao_tributaria");
							if(cdSituacaoTributaria == 0){
								Conexao.rollback(connect);
								return new Result(-1, "CFOP padrão não configurado para ICMS!");
							}
							float vlBaseRetencao     = 0;
							float vlRetido           = 0;
							int tpRegime             = ParametroServices.getValorOfParametroAsInteger("TP_REGIME_TRIBUTARIO", 0);
							if(tpRegime == 0){
								return new Result(-1, "Regime Tributário não configurado!");
							}
							//Insere a nota fiscal item tributo
							listaNotaFiscalItemTributo.add(new NotaFiscalItemTributo(nota.getCdNotaFiscal(), qtItensTotal, cdTributo, 
															cdTributoAliquota, tpRegime, 0/*Tipo de Origem - 0 Nacional, 1 - Estrangeiro (Obs.: Ainda não trabalhamos com exportacoes)*/ , 
															tpCalculo, vlBaseCalculo, 0/*vlOutrasDespesas*/, 0/*vlOutrosImpostos*/, prAliquota, (vlBaseCalculo * prAliquota / 100)/*vlTributo*/, 
															prCredito, (vlBaseCalculo * prCredito / 100)/*vlCredito*/, null/*nrClasse*/, null/*nrEnquadramento*/, cdSituacaoTributaria, vlBaseRetencao, 
															vlRetido));
							
						}
						
						while(!isImportacao && rsmTributos.next()){
							int cdTributo         	 = rsmTributos.getInt("cd_tributo");
							int cdTributoAliquota    = rsmTributos.getInt("cd_tributo_aliquota");
							float prAliquota      	 = rsmTributos.getFloat("pr_aliquota");
							float prCredito       	 = rsmTributos.getFloat("pr_credito");
							float vlBaseCalculo   	 = (rsmTributos.getFloat("pr_aliquota") > 0) ? rsmTributos.getFloat("vl_base_calculo") : 0;
							int tpCalculo            = rsmTributos.getInt("tp_base_calculo");
							int cdSituacaoTributaria = rsmTributos.getInt("cd_situacao_tributaria");
							float vlBaseRetencao     = (rsmTributos.getFloat("pr_aliquota_substituicao") > 0) ? rsmTributos.getFloat("vl_base_calculo") : 0;
							float vlRetido           = vlBaseRetencao * rsmTributos.getFloat("pr_aliquota_substituicao") / 100;
							int tpRegime             = ParametroServices.getValorOfParametroAsInteger("TP_REGIME_TRIBUTARIO", 0);
							if(tpRegime == 0){
								return new Result(-1, "Regime Tributário não configurado!");
							}
							
							//Insere a nota fiscal item tributo
							listaNotaFiscalItemTributo.add(new NotaFiscalItemTributo(nota.getCdNotaFiscal(), qtItensTotal, cdTributo, 
															cdTributoAliquota, tpRegime, 0/*Tipo de Origem - 0 Nacional, 1 - Estrangeiro (Obs.: Ainda não trabalhamos com exportacoes)*/ , 
															tpCalculo, vlBaseCalculo, 0/*vlOutrasDespesas*/, 0/*vlOutrosImpostos*/, prAliquota, (vlBaseCalculo * prAliquota / 100)/*vlTributo*/, 
															prCredito, (vlBaseCalculo * prCredito / 100)/*vlCredito*/, null/*nrClasse*/, null/*nrEnquadramento*/, cdSituacaoTributaria, vlBaseRetencao, 
															vlRetido));
							
						} 
						
						
						while(isImportacao && rsmTributoOrigem.next()){
							int cdTributo         	 = rsmTributoOrigem.getInt("cd_tributo");
							int cdTributoAliquota    = rsmTributoOrigem.getInt("cd_tributo_aliquota");
							float prAliquota      	 = rsmTributoOrigem.getFloat("pr_aliquota");
							float prCredito       	 = rsmTributoOrigem.getFloat("pr_credito");
							float vlBaseCalculo   	 = (rsmTributoOrigem.getFloat("pr_aliquota") > 0) ? rsmTributoOrigem.getFloat("vl_base_calculo") : 0;
							int tpCalculo            = rsmTributoOrigem.getInt("tp_base_calculo");
							int cdSituacaoTributaria = rsmTributoOrigem.getInt("cd_situacao_tributaria");
							float vlBaseRetencao     = (rsmTributoOrigem.getFloat("pr_aliquota_substituicao") > 0) ? rsmTributoOrigem.getFloat("vl_base_calculo") : 0;
							float vlRetido           = vlBaseRetencao * rsmTributoOrigem.getFloat("pr_aliquota_substituicao") / 100;
							int tpRegime             = ParametroServices.getValorOfParametroAsInteger("TP_REGIME_TRIBUTARIO", 0);
							if(tpRegime == 0){
								return new Result(-1, "Regime Tributário não configurado!");
							}
							//Insere a nota fiscal item tributo
							listaNotaFiscalItemTributo.add(new NotaFiscalItemTributo(nota.getCdNotaFiscal(), qtItensTotal, cdTributo, 
															cdTributoAliquota, tpRegime, 0/*Tipo de Origem - 0 Nacional, 1 - Estrangeiro (Obs.: Ainda não trabalhamos com exportacoes)*/ , 
															tpCalculo, vlBaseCalculo, 0/*vlOutrasDespesas*/, 0/*vlOutrosImpostos*/, prAliquota, (vlBaseCalculo * prAliquota / 100)/*vlTributo*/, 
															prCredito, (vlBaseCalculo * prCredito / 100)/*vlCredito*/, null/*nrClasse*/, null/*nrEnquadramento*/, cdSituacaoTributaria, vlBaseRetencao, 
															vlRetido));
							
						}
						
						float vlTotalProduto = (rsProds.getInt("qt_precisao_custo") <= 0 ? (float)Util.arredondar((nota.getVlTotalProduto() + (qtEntrada * vlUnitario)), 2) : (float)Util.arredondar((nota.getVlTotalProduto() + (qtEntrada * vlUnitario)), rsProds.getInt("qt_precisao_custo")));
						nota.setVlTotalProduto(vlTotalProduto);
						float vlTotalNota = (rsProds.getInt("qt_precisao_custo") <= 0 ? (float)Util.arredondar((nota.getVlTotalNota() + (qtEntrada * vlUnitario - vlDescontoItem)), 2) : (float)Util.arredondar((nota.getVlTotalNota() + (qtEntrada * vlUnitario - vlDescontoItem)), rsProds.getInt("qt_precisao_custo")));
						nota.setVlTotalNota(vlTotalNota);
						
					}
					
				}
				if(doc.getTpDocumentoEntrada()==DocumentoSaidaServices.TP_CUPOM_FISCAL)
					dsCupons +=  (!dsCupons.equals("") ? ", " : "") + doc.getNrDocumentoEntrada();
//				else
//					dsDAVs +=  (!dsDAVs.equals("") ? ", " : "") + doc.getNrDocumentoEntrada();
				// Verifica se a nota fiscal tinha itens
				if(qtItens == 0)	{
					connect.rollback();
					return new Result(-1, "O Documento de Entrada No "+doc.getNrDocumentoEntrada()+" não possui itens!");
				}
				
				/*
				 * Informações de IMPORTAÇÃO
				 */
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_documento_entrada", "" + doc.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmImportacao = EntradaDeclaracaoImportacaoDAO.find(criterios, connect);
				if(rsmImportacao.next()){
					
					EntradaDeclaracaoImportacao entradaImport = EntradaDeclaracaoImportacaoDAO.get(rsmImportacao.getInt("cd_entrada_declaracao_importacao"), rsmImportacao.getInt("cd_documento_entrada"), connect);
					
					String informacoesAdicionais = "";
					informacoesAdicionais += "MERCADORIAS CONFORME DOCUMENTO DE IMPORTACAO No " + entradaImport.getNrDeclaracaoImportacao() + " ";
					informacoesAdicionais += "DE " + Util.formatDate(entradaImport.getDtRegistro(), "dd/MM/yyyy") + "; ";
					informacoesAdicionais += "Taxa do Dólar R$ " + Util.formatNumber(entradaImport.getVlTaxaDolar()) + "; ";
					
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_documento_entrada", "" + doc.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmEntradaEventoFinanceiro = EntradaEventoFinanceiroDAO.find(criterios, connect);
					String despesasAduaneiras = "";
					while(rsmEntradaEventoFinanceiro.next()){
						EntradaEventoFinanceiro entradaEventoFinanceiro = EntradaEventoFinanceiroDAO.get(rsmEntradaEventoFinanceiro.getInt("cd_documento_entrada"), rsmEntradaEventoFinanceiro.getInt("cd_evento_financeiro"), connect);
						if(entradaEventoFinanceiro.getLgFiscal() == 1 && entradaEventoFinanceiro.getLgDespesaAcessoria() == 1){
							if(despesasAduaneiras.equals(""))
								despesasAduaneiras += "Despesas Aduaneiras: ";
							EventoFinanceiro eventoFinanceiro = EventoFinanceiroDAO.get(entradaEventoFinanceiro.getCdEventoFinanceiro(), connect);
							despesasAduaneiras += eventoFinanceiro.getNmEventoFinanceiro() + ": R$ " + Util.formatNumber(entradaEventoFinanceiro.getVlEventoFinanceiro())+ "; ";
						}
					}
					informacoesAdicionais += despesasAduaneiras;
					
					float vlICMS    = 0;
					float vlPIS    = 0;
					float vlCOFINS = 0;
					float vlII 	   = 0;
					
					for(NotaFiscalItemTributo notaFiscalItemTributo : listaNotaFiscalItemTributo){
						if(notaFiscalItemTributo.getCdTributo() == ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0)){
							vlICMS += notaFiscalItemTributo.getVlTributo();
						}
						if(notaFiscalItemTributo.getCdTributo() == ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0)){
							vlPIS += notaFiscalItemTributo.getVlTributo();
						}
						if(notaFiscalItemTributo.getCdTributo() == ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0)){
							vlCOFINS += notaFiscalItemTributo.getVlTributo();
						}
						if(notaFiscalItemTributo.getCdTributo() == ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_II", 0)){
							vlII += notaFiscalItemTributo.getVlTributo();
						}
					}
					if(vlPIS > 0 || vlCOFINS > 0 || vlII > 0)
						informacoesAdicionais+="Outros Tributos: ";
					
					if(vlPIS > 0)
						informacoesAdicionais+="PIS R$ " + Util.formatNumber(vlPIS)+ "; ";
					if(vlCOFINS > 0)
						informacoesAdicionais+="COFINS R$ " + Util.formatNumber(vlCOFINS)+ "; ";
					if(vlII > 0)
						informacoesAdicionais+="Imposto de Importação R$ " + Util.formatNumber(vlII)+ "; ";
					
//					vlOutrasDespesas += vlICMS + vlPIS + vlCOFINS;
					nota.setTxtObservacao(nota.getTxtObservacao() + (nota.getTxtObservacao().equals("") ? "" : ". ") + informacoesAdicionais);
					
					Estado estadoDesembaraco = EstadoServices.getBySg(entradaImport.getSgUfDesembaraco(), connect);
					
					Pessoa fornecedor = PessoaDAO.get(nota.getCdDestinatario(), connect);
					
					HashMap<Integer, Integer> regAdicaoItem = new HashMap<Integer, Integer>();
					
					//Armazena o codigo de todos os NCM pertencentes aos itens do documento, sem repetir
					ResultSetMap rsmNotaItens = NotaFiscalServices.getAllItens(nota.getCdNotaFiscal(), 0, connect);
					
					while(rsmNotaItens.next()){
						
						int nAdicao = 1;
						
						// Cadastrar a declaração de IMPORTAÇÃO
						DeclaracaoImportacao declaracao = new DeclaracaoImportacao(0, fornecedor.getCdPessoa(), estadoDesembaraco.getCdEstado(), 
												rsmNotaItens.getInt("cd_item"), nota.getCdNotaFiscal(), entradaImport.getNrDeclaracaoImportacao(), 
												entradaImport.getDtRegistro(), entradaImport.getNmLocal(), entradaImport.getDtDesembaraco(), entradaImport.getTpViaTransporte(),
												entradaImport.getTpIntermedio(), entradaImport.getNrCnpjIntermediario(), entradaImport.getCdEstadoIntermediario()); 
						int code = DeclaracaoImportacaoDAO.insert(declaracao, connect);
						if(code <= 0){
							Conexao.rollback(connect);
							return new Result(-1, "Erro ao cadastrar a declaração de importação!");
						}
						declaracao.setCdDeclaracaoImportacao(code);
						
						Ncm ncm = NcmDAO.get(rsmNotaItens.getInt("cd_ncm")); 
						
						int nrSeqAdicao = 1;
						if(regAdicaoItem.get(rsmNotaItens.getInt("cd_ncm")) != null){
							nrSeqAdicao = regAdicaoItem.get(rsmNotaItens.getInt("cd_ncm")) + 1;
							regAdicaoItem.put(regAdicaoItem.get(rsmNotaItens.getInt("cd_ncm")), nrSeqAdicao);
						}
						else{
							regAdicaoItem.put(regAdicaoItem.get(rsmNotaItens.getInt("cd_ncm")), nrSeqAdicao);
						}
						
						Adicao adicao = new Adicao(0, declaracao.getCdDeclaracaoImportacao(), 0/*cdPaisForn*/, rsmNotaItens.getInt("cd_ncm"), 0, 0, nAdicao++, nrSeqAdicao, 0, null, null, 0);
						code = AdicaoDAO.insert(adicao, connect);
						if(code <= 0){
							Conexao.rollback(connect);
							return new Result(-1, "Erro ao cadastrar a declarar Adicao do NCM: " + ncm.getNmNcm());
						}
						
					}
					rsmNotaItens.beforeFirst();
										
				}
				
				nota.setTxtObservacao((doc.getTxtObservacao() == null || doc.getTxtObservacao().trim().equals("") ? nota.getTxtObservacao() : doc.getTxtObservacao() + ". ").trim());
			}
			nota.setTxtObservacao(nota.getTxtObservacao() + (dsCupons.equals("") && dsDAVs.equals("") ? "" : "Em substituição ao(s) documento(s): "+(!dsCupons.equals("") ? "CUPOM(NS) No: "+dsCupons+"." : "")+
		               (!dsDAVs.equals("") ? " DAV(S) No: "+dsDAVs+"." : "")));
			
			//Itera sobre os tributos dos itens para fazer o tributo da nota
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_nota_fiscal", "" + nota.getCdNotaFiscal(), ItemComparator.EQUAL, Types.INTEGER));
			for(int k = 0; k < listaNotaFiscalItemTributo.size(); k++){
				NotaFiscalItemTributo notaItemTrib = listaNotaFiscalItemTributo.get(k);
				NotaFiscalTributo notaTributo = NotaFiscalTributoDAO.get(notaItemTrib.getCdNotaFiscal(), notaItemTrib.getCdTributo(), connect);
				if(notaTributo == null)	{
					notaTributo = new NotaFiscalTributo(nota.getCdNotaFiscal(), notaItemTrib.getCdTributo(), 
														notaItemTrib.getVlBaseCalculo(), 
														notaItemTrib.getVlOutrasDespesas(), 0/*Outros Impostos*/, 
														notaItemTrib.getVlTributo(), 
														notaItemTrib.getVlBaseRetencao(), notaItemTrib.getVlRetido());
					if(NotaFiscalTributoDAO.insert(notaTributo, connect) <= 0)	{
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao gerar Nota Fiscal Tributo");
					}
				}
				else	{
					//Faz a soma dos valores antigos com os novos
					notaTributo.setVlBaseCalculo(notaTributo.getVlBaseCalculo() + notaItemTrib.getVlBaseCalculo());
					notaTributo.setVlBaseRetencao(notaTributo.getVlBaseRetencao() + notaItemTrib.getVlBaseRetencao());
					notaTributo.setVlOutrasDespesas(notaTributo.getVlOutrasDespesas() +notaItemTrib.getVlOutrasDespesas());
					notaTributo.setVlOutrosImpostos(0);
					notaTributo.setVlRetido(notaTributo.getVlRetido() + notaItemTrib.getVlRetido());
					notaTributo.setVlTributo(notaTributo.getVlTributo() + notaItemTrib.getVlTributo());
					NotaFiscalTributoDAO.update(notaTributo, connect);
				}
				
			}
			
			//Insere a lista de nota fiscal item tributo no banco
			for(int k = 0; k < listaNotaFiscalItemTributo.size(); k++){
				if(NotaFiscalItemTributoDAO.insert(listaNotaFiscalItemTributo.get(k), connect) <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao gerar Nota Fiscal Item Tributo");
				}
			}
			
			//Itera sobre os tributos da nota para saber o que é imposto direto e qual é de icms por substituicao tributaria
			ResultSetMap rsm = NotaFiscalTributoDAO.find(criterios, connect);
			rsm.beforeFirst();
			float vlICMS = 0;
			float vlPIS = 0;
			float vlCOFINS = 0;
			while(rsm.next()){
				Tributo tributo = TributoDAO.get(rsm.getInt("cd_tributo"), connect);
				if(tributo.getIdTributo().equals("ICMS")){
					vlIcmsSt += rsm.getFloat("vl_retido");
					vlICMS += rsm.getFloat("vl_tributo");	
				}
				else if(tributo.getIdTributo().equals("IPI")){
					vlImpostosDiretos += rsm.getFloat("vl_tributo");					
				}
				else if(tributo.getIdTributo().equals("II")){
					vlImpostosDiretos += rsm.getFloat("vl_tributo");
				}
				else if(tributo.getIdTributo().equals("PIS")){
					vlPIS += rsm.getFloat("vl_tributo");					
				}
				else if(tributo.getIdTributo().equals("COFINS")){
					vlCOFINS += rsm.getFloat("vl_tributo");					
				}
			}
			
			
			// Somando o total da nota - Acrescentar impostos diretos e icms st
			nota.setVlTotalNota(nota.getVlTotalNota() + vlFrete + vlSeguro + vlIcmsSt + vlImpostosDiretos + vlOutrasDespesas);
			
			if(isImportacao){
				nota.setVlTotalNota(nota.getVlTotalNota() + vlICMS + vlPIS + vlCOFINS);
			}
			
			//Teste para saber se a nota é uma nota de IMPORTAÇÃO - Caso o pais da empresa e destinatario sejam diferentes
			Pessoa empresa = PessoaDAO.get(nota.getCdEmpresa(), connect);
			Pessoa fornecedor = PessoaDAO.get(nota.getCdDestinatario(), connect);
			
			//Busca do pais e estado do emitente
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_pessoa", "" + empresa.getCdPessoa(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("lg_principal", "1", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmPessoaEnd = PessoaEnderecoDAO.find(criterios, connect);
			int cdEstadoEmi = 0; 
			int cdPaisEmi   = 0;
			if(rsmPessoaEnd.next()){
				cdCidade = rsmPessoaEnd.getInt("cd_cidade");
				Cidade cidade = CidadeDAO.get(cdCidade, connect);
				if(cidade == null){
					Conexao.rollback(connect);
					return new Result(-1, "Emitente sem cidade declarada!");
				}
				Estado estado = EstadoDAO.get(cidade.getCdEstado(), connect);
				if(estado == null){
					Conexao.rollback(connect);
					return new Result(-1, "Emitente sem estado declarada!");
				}
				cdEstadoEmi = estado.getCdEstado();
			}
			Estado estado = EstadoDAO.get(cdEstadoEmi, connect);
			cdPaisEmi = estado.getCdPais();
			if(cdPaisEmi == 0){
				Conexao.rollback(connect);
				return new Result(-1, "Emitente sem pais declarado!");
			}
			
			//Busca do pais do fornecedor
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_pessoa", "" + fornecedor.getCdPessoa(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("lg_principal", "1", ItemComparator.EQUAL, Types.INTEGER));
			rsmPessoaEnd = PessoaEnderecoDAO.find(criterios, connect);
			int cdEstadoForn = 0; 
			int cdPaisForn   = 0;
			if(rsmPessoaEnd.next()){
				cdCidade = rsmPessoaEnd.getInt("cd_cidade");
				Cidade cidade = CidadeDAO.get(cdCidade, connect);
				if(cidade == null){
					Conexao.rollback(connect);
					return new Result(-1, "Fornecedor sem cidade declarada!");
				}
				estado = EstadoDAO.get(cidade.getCdEstado(), connect);
				if(estado == null){
					Conexao.rollback(connect);
					return new Result(-1, "Fornecedor sem estado declarado!");
				}
				cdEstadoForn = estado.getCdEstado();
			}
			estado = EstadoDAO.get(cdEstadoForn, connect);
			cdPaisForn = estado.getCdPais();
			if(cdPaisForn == 0){
				Conexao.rollback(connect);
				return new Result(-1, "Fornecedor sem pais declarado!");
			}
			
			nota.setVlOutrasDespesas(vlOutrasDespesas);
			if(NotaFiscalDAO.update(nota, connect) <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar nota!");
			}
			
			
			float prDescontoMaximo = 0;
			float somaItens        = 0;
			float somaDescontado   = 0;
			ResultSetMap rsmItens = NotaFiscalItemServices.getAllByCdNfe(nota.getCdNotaFiscal(), nota.getCdEmpresa(), connect);
			while(rsmItens.next()){
				somaItens += (Util.arredondar(rsmItens.getFloat("vl_unitario"), rsmItens.getInt("qt_precisao_custo")) * Util.arredondar(rsmItens.getFloat("qt_tributario"), rsmItens.getInt("nr_precisao_medida")));
				somaDescontado += (Util.arredondar(rsmItens.getFloat("vl_unitario"), rsmItens.getInt("qt_precisao_custo")) * Util.arredondar(rsmItens.getFloat("qt_tributario"), rsmItens.getInt("nr_precisao_medida"))) - ((Util.arredondar(rsmItens.getFloat("vl_unitario"), rsmItens.getInt("qt_precisao_custo")) * Util.arredondar(rsmItens.getFloat("qt_tributario"), rsmItens.getInt("nr_precisao_medida"))) * rsmItens.getFloat("pr_desconto_maximo") / 100);
			}
			float r = somaDescontado / somaItens;
			prDescontoMaximo = 100 - (r * 100);
			prDescontoMaximo = (float)Util.arredondar(prDescontoMaximo, 2);
			//--------------------------------- DESCONTO AUTORIZADO ---------------------------------//
			update(nota, prDesconto, prDescontoMaximo, tpDesconto, 0, 0/*Ignorar valor de acrescimo*/, connect);
			//
			if(isConnectionNull)
				connect.commit();
			Result resultado = new Result(1, "Nota Fiscal EletrÃ´nica cadastrada com sucesso!");
			HashMap<String, Object> registro = new HashMap<String, Object>();
			registro.put("cdNotaFiscal", nota.getCdNotaFiscal());
			registro.put("prDescontoMaximo", prDescontoMaximo);			
			resultado.setObjects(registro);
			return resultado;
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			Util.registerLog(e); // Registra o log quando chamado pelo PDV
			return new Result(-10, "Falha ao tentar enviar documento para a fila de validação!", e);
		}
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}	
	
	public static ResultSetMap findNotasFiscais(int cdNotaFiscal){
		return findNotasFiscais(cdNotaFiscal, 0, null, null, 0, 0, null, null, null, null, null, null, null);
	}
	
	public static ResultSetMap findNotasFiscais(int cdNotaFiscal, int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int tpNotaFiscal, 
    		                                    int stNotaFiscal, String nrSerie, String nrInicial, String nrFinal, String nrCpfCnpj, String nrChaveAcesso)
	{
		return findNotasFiscais(cdNotaFiscal, cdEmpresa, dtInicial, dtFinal, tpNotaFiscal, stNotaFiscal, nrSerie, nrInicial, nrFinal, nrCpfCnpj, nrChaveAcesso, null, null);
	}
	
	public static ResultSetMap findNotasFiscais(int cdNotaFiscal, int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int tpNotaFiscal, 
            int stNotaFiscal, String nrSerie, String nrInicial, String nrFinal, String nrCpfCnpj, String nrChaveAcesso, String nrDocumentoSaida)
	{
	return findNotasFiscais(cdNotaFiscal, cdEmpresa, dtInicial, dtFinal, tpNotaFiscal, stNotaFiscal, nrSerie, nrInicial, nrFinal, nrCpfCnpj, nrChaveAcesso, nrDocumentoSaida, null);
	}
	
	public static ResultSetMap findNotasFiscais(int cdNotaFiscal, int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int tpNotaFiscal, 
    		                                    int stNotaFiscal, String nrSerie, String nrInicial, String nrFinal, String  nrCpfCnpj, String nrChaveAcesso, Connection connect)
	{
		return findNotasFiscais(cdNotaFiscal, cdEmpresa, dtInicial, dtFinal, tpNotaFiscal, stNotaFiscal, nrSerie, nrInicial, nrFinal, nrCpfCnpj, nrChaveAcesso, null, connect);
	}
	
	public static ResultSetMap findNotasFiscais(int cdNotaFiscal, int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int tpNotaFiscal, 
                int stNotaFiscal, String nrSerie, String nrInicial, String nrFinal, String  nrCpfCnpj, String nrChaveAcesso, String nrDocumentoSaida, Connection connect)
	{	
    	boolean isConnectionNull = connect==null;
    	try {
    		connect = isConnectionNull ? Conexao.conectar() : connect;
    		if (isConnectionNull)
				connect = Conexao.conectar();
    		
    		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();

    		String tipoNotaFiscal = "";
    		String situacaoNotaFiscal = "";
    		String between = "";
    		String nrCpf  = "";
    		String nrCnpj = "";
    		String betweenDate = "";
    		
    		int cdIcms 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0);
    		int cdIpi 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_IPI", 0);
    		int cdIi 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_II", 0);
    		if(cdNotaFiscal != 0)
    			criterios.add(new ItemComparator("A.cd_nota_fiscal", String.valueOf(cdNotaFiscal), ItemComparator.EQUAL, Types.INTEGER));
    		
    		if(cdEmpresa != 0)
    			criterios.add(new ItemComparator("A.cd_empresa", String.valueOf(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
    		    		
    		if(tpNotaFiscal != 0){
    			criterios.add(new ItemComparator("A.tp_emissao", String.valueOf(tpNotaFiscal), ItemComparator.EQUAL, Types.INTEGER));
    			tipoNotaFiscal =  ", '" + tiposNotaFiscal[tpNotaFiscal] + "' AS nm_tp_nota_fiscal ";
    		}
    		
    		if(stNotaFiscal != 0){
    			criterios.add(new ItemComparator("A.st_nota_fiscal", String.valueOf(stNotaFiscal), ItemComparator.EQUAL, Types.INTEGER));
    			situacaoNotaFiscal = ", '" + NotaFiscalServices.situacaoNotaFiscal[stNotaFiscal] + "' AS nm_st_nota_fiscal ";
    		}
    		
    		if(nrCpfCnpj != null && !nrCpfCnpj.equals("")){
    			if(nrCpfCnpj.length() == 11)
    				nrCpf = nrCpfCnpj;
    			if(nrCpfCnpj.length() == 14)
    				nrCnpj = nrCpfCnpj;
    		}
    		
    		if(nrSerie != null && !nrSerie.equals(""))
    			criterios.add(new ItemComparator("A.nr_serie", nrSerie, ItemComparator.LIKE_ANY, Types.VARCHAR));
    		
    		
    		if(nrChaveAcesso != null && !nrChaveAcesso.equals("")){
    			criterios.add(new ItemComparator("A.nr_chave_acesso", nrChaveAcesso, ItemComparator.LIKE_ANY, Types.VARCHAR));
    		}
    		//
    		if(nrInicial!=null && !nrInicial.equals("")){
    			int inicio = 0;
    			int fim    = 0;
    			if(nrFinal.equals("")){
    				inicio = Integer.parseInt(nrInicial);
    				fim = Integer.parseInt(nrInicial);
    				nrFinal = nrInicial;
    			}
    			else{
    				inicio = Integer.parseInt(nrInicial);
    				fim = Integer.parseInt(nrFinal);
    			}	
    			
    			if(inicio <= fim)
					between = "AND CAST (A.nr_nota_fiscal AS INTEGER) BETWEEN CAST ('"+nrInicial+"' AS INTEGER) AND CAST ('"+nrFinal+"' AS INTEGER) ";
				else
					return null;
    		}
    		else if(nrFinal!=null && !nrFinal.equals(""))
    			between = "AND CAST (A.nr_nota_fiscal AS INTEGER) BETWEEN CAST ('"+nrFinal+"' AS INTEGER) AND CAST ('"+nrFinal+"' AS INTEGER) ";
    		
    		//
    		if(dtInicial != null)	{
    			criterios.add(new ItemComparator("CAST(A.dt_emissao AS DATE)", Util.formatDate(dtInicial, "dd/MM/yyyy"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
	    	}    		
    		if(dtFinal != null)	{
    			criterios.add(new ItemComparator("CAST(A.dt_emissao AS DATE)", Util.formatDate(dtFinal, "dd/MM/yyyy"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
    		}
    		//
    		
    		ResultSetMap rsm =  Search.find(
					"SELECT A.*, F.nm_pessoa, " +
					"       (SELECT SUM(vl_desconto) " +
					"			FROM fsc_nota_fiscal_item NFI " +
					"			WHERE A.cd_nota_fiscal = NFI.cd_nota_fiscal) AS vl_desconto, " + 
					"       (SELECT SUM(vl_tributo)  " +
					"			FROM fsc_nota_fiscal_item_tributo NFI " +
					"			WHERE A.cd_nota_fiscal = NFI.cd_nota_fiscal" +
					"				AND NFI.cd_tributo = "+cdIcms+") AS vl_icms, " +
					"       (SELECT SUM(vl_retido)  " +
					"			FROM fsc_nota_fiscal_item_tributo NFI " +
					"			WHERE A.cd_nota_fiscal = NFI.cd_nota_fiscal" +
					"				AND NFI.cd_tributo = "+cdIcms+") AS vl_icms_st, " +
					"       (SELECT SUM(vl_tributo)  " +
					"			FROM fsc_nota_fiscal_item_tributo NFI " +
					"			WHERE A.cd_nota_fiscal = NFI.cd_nota_fiscal" +
					"				AND NFI.cd_tributo = "+cdIpi+") AS vl_ipi, " +
					"       (SELECT SUM(vl_tributo)  " +
					"			FROM fsc_nota_fiscal_item_tributo NFI " +
					"			WHERE A.cd_nota_fiscal = NFI.cd_nota_fiscal" +
					"				AND NFI.cd_tributo = "+cdIi+") AS vl_ii, " +
					(nrCpf!=null && !nrCpf.equals("") ? "B.nr_cpf AS nr_cpf_cnpj, E.sg_estado " : 
						                               (nrCnpj!=null && !nrCnpj.equals("") ? "B.nm_razao_social, B.nr_cnpj AS nr_cpf_cnpj " : "D.nm_razao_social, B.nr_cpf, D.nr_cnpj, E.sg_estado ")) +
					(!tipoNotaFiscal.equals("") ? tipoNotaFiscal : "") +
					(!situacaoNotaFiscal.equals("") ? situacaoNotaFiscal : "") +
					", CAST (nr_nota_fiscal AS INTEGER) AS numero, PD.cd_pais AS cd_pais_dest, PE.cd_pais AS cd_pais_emi, NO.nr_codigo_fiscal " +
					" FROM fsc_nota_fiscal A " + 
					(nrCpf!=null && !nrCpf.equals("")   ? "LEFT OUTER JOIN grl_pessoa_fisica   B ON (B.cd_pessoa = A.cd_destinatario) " +
					  					                  "LEFT OUTER JOIN grl_estado          E ON (B.cd_estado_rg = E.cd_estado) " : 
					(nrCnpj!=null && !nrCnpj.equals("") ? "LEFT OUTER JOIN grl_pessoa_juridica B ON (B.cd_pessoa = A.cd_destinatario) " : 
					"LEFT OUTER JOIN grl_pessoa                    C ON (C.cd_pessoa = A.cd_destinatario) " +
					"LEFT OUTER JOIN grl_pessoa_fisica             B ON (B.cd_pessoa = A.cd_destinatario) " +
					"LEFT OUTER JOIN grl_estado                    E ON (B.cd_estado_rg = E.cd_estado) " +
					"LEFT OUTER JOIN grl_pessoa_juridica           D ON (A.cd_destinatario = D.cd_pessoa) ")) +
					"LEFT OUTER JOIN grl_pessoa			           F ON (A.cd_destinatario = F.cd_pessoa) " +
					"LEFT OUTER JOIN grl_pessoa_endereco		   PED ON (A.cd_destinatario = PED.cd_pessoa" +
					"														AND PED.lg_principal = 1) " +
					"LEFT OUTER JOIN grl_pessoa_endereco		   PEE ON (A.cd_empresa = PEE.cd_pessoa" +
					"														AND PEE.lg_principal = 1) " +
					"LEFT OUTER JOIN grl_cidade			           CD ON (CD.cd_cidade = PED.cd_cidade) " +
					"LEFT OUTER JOIN grl_cidade			           CE ON (CE.cd_cidade = PEE.cd_cidade) " +
					"LEFT OUTER JOIN grl_estado		           	   ED ON (ED.cd_estado = CD.cd_estado) " +
					"LEFT OUTER JOIN grl_estado			           EE ON (EE.cd_estado = CE.cd_estado) " +
					"LEFT OUTER JOIN grl_pais			           PD ON (PD.cd_pais = ED.cd_pais) " +
					"LEFT OUTER JOIN grl_pais			           PE ON (PE.cd_pais = EE.cd_pais) " +
					(nrDocumentoSaida != null && !"".equalsIgnoreCase(nrDocumentoSaida) 
						? " LEFT JOIN fsc_nota_fiscal_doc_vinculado DOC_VINC ON (DOC_VINC.cd_nota_fiscal = A.cd_nota_fiscal)"
						+ "  JOIN alm_documento_saida DOC_SAI ON (DOC_SAI.cd_documento_saida = DOC_VINC.cd_documento_saida AND DOC_SAI.nr_documento_saida LIKE '" + nrDocumentoSaida +"')" : "") +  
					"LEFT OUTER JOIN adm_natureza_operacao         NO ON (NO.cd_natureza_operacao = A.cd_natureza_operacao) " +
					"WHERE 1 = 1 "+ (nrCpf!=null && !nrCpf.equals("")   ? " AND B.nr_cpf = \'"+nrCpf+"\'" : (nrCnpj!=null && !nrCnpj.equals("") ? " AND B.nr_cnpj = \'"+nrCnpj+"\'" : "")) + between.toString() + betweenDate.toString(), "ORDER BY numero DESC ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
    		
    		PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_doc_vinculado WHERE cd_nota_fiscal = ? LIMIT 1");
    		
    		while(rsm.next()){
    			pstmt.setInt(1, rsm.getInt("cd_nota_fiscal"));
    			ResultSetMap rsmDocVinc = new ResultSetMap(pstmt.executeQuery());
	    		if(rsmDocVinc.next()){
	    			if(rsmDocVinc.getInt("cd_documento_entrada") != 0){
	    				rsm.setValueToField("TP_DOC_VINC", 0);
	    			}
	    			else if(rsmDocVinc.getInt("cd_documento_saida") != 0){
	    				rsm.setValueToField("TP_DOC_VINC", 1);
	    			}
	    				
	    		}
	    		if(rsm.getFloat("vl_desconto") <= 0)
	    			rsm.setValueToField("vl_desconto", 0);
    		}
    		rsm.beforeFirst();
    		return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
    	finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
    }
	//--
	public static String validaCPFCNPJ(String nrCPFCNPJ){
		nrCPFCNPJ = nrCPFCNPJ.replaceAll(".", "");
		nrCPFCNPJ = nrCPFCNPJ.replaceAll("/", "");
		nrCPFCNPJ = nrCPFCNPJ.replaceAll("-", "");
		return nrCPFCNPJ;
	}
	
	
	/**
	 * 
	 * @param cdNotaFiscal
	 * @return
	 * @see #getWithDest(int, Connection)
	 */
	public static ResultSetMap getWithDest(int cdNotaFiscal) {
		return getWithDest(cdNotaFiscal, null);
	}

	/**
	 * Retorna uma nota fiscal e os dados do cliente da nota fiscal a partir da chave primária 
	 * 
	 * @param cdNotaFiscal
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getWithDest(int cdNotaFiscal, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_email, B.nm_pessoa AS nm_destinatario, B.nr_telefone1, B.nr_telefone2, B.nr_celular, B.nr_fax, " +
					                         "       C.*, D.*, E.*, F.*,  F2.nr_telefone1 as contato_nr_telefone1 , F2.nr_telefone2 as contato_nr_telefone2, " +
					                         " F2.nr_celular as contato_nr_celular, F2.nr_celular2 as contato_nr_celular2, F2.nr_celular3 as contato_nr_celular3, " +
											 " F2.nr_celular4 as contato_nr_celular4, J.cd_declaracao_importacao, J.nr_declaracao_importacao, J.ds_local_desembaraco, CAST(J.dt_registro AS DATE) AS dt_registro, CAST(J.dt_desembaraco AS DATE) AS dt_desembaraco, " +
					                         "       G.nm_cidade, G.id_ibge, E.nm_estado, E.sg_estado, H.nm_tipo_logradouro, I.nm_pessoa AS nm_transportador " +
					                         "FROM fsc_nota_fiscal A " +
											 "LEFT OUTER JOIN grl_pessoa          		 B ON (A.cd_destinatario = B.cd_pessoa) " +
											 "LEFT OUTER JOIN grl_pessoa_endereco 		 F ON (B.cd_pessoa = F.cd_pessoa) "+
											 "LEFT OUTER JOIN grl_pessoa_contato 		 F2 ON (B.cd_pessoa = F2.cd_pessoa) "+
											 "LEFT OUTER JOIN grl_pessoa_fisica   		 C ON (C.cd_pessoa = B.cd_pessoa) " +
											 "LEFT OUTER JOIN grl_pessoa_juridica 		 D ON (D.cd_pessoa = B.cd_pessoa) " +
											 "LEFT OUTER JOIN grl_cidade          		 G ON (F.cd_cidade = G.cd_cidade) " +
											 "LEFT OUTER JOIN grl_estado          		 E ON (G.cd_estado = E.cd_estado) " + 
											 "LEFT OUTER JOIN grl_tipo_logradouro 		 H ON (F.cd_tipo_logradouro = H.cd_tipo_logradouro) " + 
											 "LEFT OUTER JOIN grl_pessoa          		 I ON (A.cd_transportador = I.cd_pessoa) " +
											 "LEFT OUTER JOIN fsc_declaracao_importacao  J ON (A.cd_nota_fiscal = J.cd_nota_fiscal) " +
											 "WHERE A.cd_nota_fiscal=?");
			pstmt.setInt(1, cdNotaFiscal);
			rs = pstmt.executeQuery();
			ResultSetMap rsm = new ResultSetMap(rs);
			return rsm;
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
	
	public static Result updateItem(NotaFiscal nota, ArrayList<HashMap<String, Object>> registros){
		return updateItem(nota, registros, null, new ArrayList<Adicao>());
	}
	
	public static Result updateItem(NotaFiscal nota, ArrayList<HashMap<String, Object>> registros, DeclaracaoImportacao declaracao, ArrayList<Adicao> listaAdicoes){
		Connection connect = Conexao.conectar();
	
		try	 {
			if(nota.getCdDestinatario() <= 0)
				return new Result(-1, "O destinatário/cliente deve ser informado!");
			
			connect.setAutoCommit(false);
			
			NotaFiscal notaVelha = NotaFiscalDAO.get(nota.getCdNotaFiscal(), connect);
			
			//Caso tenha sido alterado o numero da nota, também irão gerar nova chave de acesso e novo numero de dv
			if(!notaVelha.getNrNotaFiscal().equals(nota.getNrNotaFiscal()) ||
			    notaVelha.getTpEmissao() != nota.getTpEmissao()){
				if(nota.getNrNotaFiscal() != null){
					Result result = gerarNfeId(nota);
					if(result.getCode() <= 0)	{
						Conexao.rollback(connect);
						result.setMessage(result.getMessage()+" Falha ao tentar gerar ID da Nota Fiscal.");
						return result;
					}
					nota.setNrChaveAcesso((String)result.getObjects().get("nrChave"));
					nota.setNrDv((Integer)result.getObjects().get("nrDv"));
				}
			}
			
			if(NotaFiscalDAO.update(nota, connect) <= 0){
				connect.rollback();
				return new Result(-1, "Erro ao atualizar a nota!");
			}
			for(int i = 0; i < registros.size(); i++){
				int cdNotaFiscal   = registros.get(i)==null || registros.get(i).get("cdNotaFiscal")==null   ? 0 : (Integer) registros.get(i).get("cdNotaFiscal");
				int cdItem         = registros.get(i)==null || registros.get(i).get("cdItem")==null         ? 0 : (Integer) registros.get(i).get("cdItem");
				int nrCodigoFiscal = registros.get(i)==null || registros.get(i).get("nrCodigoFiscal")==null ? 0 : (Integer) registros.get(i).get("nrCodigoFiscal");
				String txtInformacaoAdicional = registros.get(i)==null || registros.get(i).get("txtInformacaoAdicional")==null ? null : (String) registros.get(i).get("txtInformacaoAdicional");
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("nr_codigo_fiscal", "" + nrCodigoFiscal, ItemComparator.EQUAL, Types.VARCHAR));
				ResultSetMap rsmNO     = NaturezaOperacaoDAO.find(criterios);
				int cdNaturezaOperacao = 0;
				if(rsmNO.next())
					cdNaturezaOperacao = rsmNO.getInt("cd_natureza_operacao");
				
				if(cdNaturezaOperacao == 0)	{
					connect.rollback();
					return new Result(-1, "Erro ao atualizar o CFOP! Verificar Código!");
				}
				NotaFiscalItem notaItem  = NotaFiscalItemDAO.get(cdNotaFiscal, cdItem);
				notaItem.setCdNaturezaOperacao(cdNaturezaOperacao);
				notaItem.setTxtInformacaoAdicional(txtInformacaoAdicional);
				if(NotaFiscalItemDAO.update(notaItem, connect) <= 0){
					connect.rollback();
					return new Result(-1, "Erro ao atualizar a item da nota!");
				}
			}
			// Atualizacao de Declaração
			if(declaracao != null){
				int verificador = DeclaracaoImportacaoDAO.update(declaracao, connect);
				if(verificador == 0){
					Pessoa empresa = PessoaDAO.get(nota.getCdEmpresa());
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_pessoa", "" + empresa.getCdPessoa(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("lg_principal", "1", ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmPessoaEnd = PessoaEnderecoDAO.find(criterios);
					int cdEstadoEmi = 0;
					if(rsmPessoaEnd.next()){
						int cdCidade = rsmPessoaEnd.getInt("cd_cidade");
						Cidade cidade = CidadeDAO.get(cdCidade);
						if(cidade == null){
							connect.rollback();
							return new Result(-1, "Erro: Empresa sem cidade cadastrada!");
						}
						Estado estado = EstadoDAO.get(cidade.getCdEstado());
						if(estado == null){
							connect.rollback();
							return new Result(-1, "Erro: Empresa sem estado cadastrada!");
						}
						cdEstadoEmi = estado.getCdEstado();
					}
					declaracao.setCdEstado(cdEstadoEmi);
					declaracao.setCdExportador(empresa.getCdPessoa());
					declaracao.setCdItem(1);
					declaracao.setCdNotaFiscal(nota.getCdNotaFiscal());
					if(DeclaracaoImportacaoDAO.insert(declaracao, connect) <= 0){
						connect.rollback();
						return new Result(-1, "Erro: Ao cadastrar importação!");
					}
				}
				
				else if(verificador < 0){
					connect.rollback();
					return new Result(-1, "Erro: Ao atualizar importação!");
				}
				
				
				for(int i = 0; i < listaAdicoes.size(); i++){
					Adicao adicao = (Adicao) listaAdicoes.get(i);
					verificador = AdicaoDAO.update(adicao, connect);
					if(verificador <= 0){
						connect.rollback();
						return new Result(-1, "Erro: Adicao não atualizada!");
					}
				}
			}
			
			connect.commit();
			return new Result(1, "Atualização realizada com sucesso!");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.out.println("Erro: " + e);
			return new Result(-1, "Erro: " + e);
		}
		
		finally{
			Conexao.desconectar(connect);
		}
		
	}
	
	public static Result recalcTributoNota(NotaFiscal nota, Connection connect){
		try{
			//Buscar endereço principal de uma pessoa
			PreparedStatement pstmtEnd = connect.prepareStatement("SELECT A.*, B.nm_cidade, B.cd_estado FROM grl_pessoa_endereco A " +
					                                              "LEFT OUTER JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade) " +
	                                                              "WHERE cd_pessoa    = ? "+
	      														  "  AND lg_principal = 1");
			int cdCliente = nota.getCdDestinatario();
			int cdEstadoDest   = 0;
			int cdCidadeDest   = 0;
			//Busca estado e cidade do cliente
			if(cdCliente > 0) {
				pstmtEnd.setInt(1, cdCliente);
				ResultSetMap rsEnd = new ResultSetMap(pstmtEnd.executeQuery()); 
				if(rsEnd.next()){
					cdEstadoDest = rsEnd.getInt("cd_estado");
					cdCidadeDest = rsEnd.getInt("cd_cidade");
				}
	
			}
			
			float vlIcmsSt 			= 0;
			float vlImpostosDiretos = 0;
			float vlFrete			= nota.getVlFrete();
			float vlSeguro			= nota.getVlSeguro();
			float vlOutrasDespesas  = nota.getVlOutrasDespesas();
			
			//Zera os valores totais da nota
			nota.setVlTotalNota(0);
			nota.setVlTotalProduto(0);
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_nota_fiscal", "" + nota.getCdNotaFiscal(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmItens = NotaFiscalItemDAO.find(criterios, connect);
			while(rsmItens.next()){
				//Calcula o valor dos tributos
				ProdutoServico produto = ProdutoServicoDAO.get(rsmItens.getInt("cd_produto_servico"), connect);
				ResultSetMap rsmTributos = TributoServices.calcTributos(TributoAliquotaServices.OP_VENDA, rsmItens.getInt("cd_natureza_operacao"), produto.getCdClassificacaoFiscal(), 
																		rsmItens.getInt("cd_produto_servico"), 0, cdEstadoDest, cdCidadeDest, (rsmItens.getFloat("qt_tributario") * rsmItens.getFloat("vl_unitario") - rsmItens.getFloat("vl_desconto")), 
																		connect);
				rsmTributos.beforeFirst();
				int count = 0;
				/*
				 *  ATUALIZANDO OS TOTAIS DOS TRIBUTOS 
				 */
				while(rsmTributos.next()){
					int cdTributo         = rsmTributos.getInt("cd_tributo");
					
					//Busca o tributo do item
					NotaFiscalItemTributo notaItemTributo = NotaFiscalItemTributoDAO.get(nota.getCdNotaFiscal(), rsmItens.getInt("cd_item"), cdTributo, connect);
					if(notaItemTributo == null){
						Conexao.rollback(connect);
						return new Result(-1, "Erro: Item sem tributo!");
					}
					float vlBaseCalculo      = (notaItemTributo.getPrAliquota() > 0) ? rsmTributos.getFloat("vl_base_calculo") : 0;
					float vlBaseCalculoRet   = (rsmTributos.getFloat("pr_aliquota_substituicao") > 0) ? rsmTributos.getFloat("vl_base_calculo") : 0;
					
					
					//Instrução para zerar a base de calculo que havia antigamente
					if(count == 0){
						notaItemTributo.setVlBaseCalculo(0);
						notaItemTributo.setVlBaseRetencao(0);
						notaItemTributo.setVlTributo(0);
						notaItemTributo.setVlCredito(0);
						notaItemTributo.setVlRetido(0);
					}
					count++;
					//Aumenta a base de calculo com a nova
					notaItemTributo.setVlBaseCalculo(vlBaseCalculo);
					notaItemTributo.setVlBaseRetencao(vlBaseCalculoRet);
					//Recalcula o valor de tributo e o valor de credito
					notaItemTributo.setVlTributo(notaItemTributo.getVlBaseCalculo()  * notaItemTributo.getPrAliquota() / 100);
					notaItemTributo.setVlCredito(notaItemTributo.getVlBaseCalculo()  * notaItemTributo.getPrCredito() / 100);
					notaItemTributo.setVlRetido(notaItemTributo.getVlBaseRetencao() * rsmTributos.getFloat("pr_aliquota_substituicao") / 100);
					//Atualiza a nota fiscal item tributo
					if(NotaFiscalItemTributoDAO.update(notaItemTributo, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar tributo de item!");
					}
					
				}
				nota.setVlTotalProduto((nota.getVlTotalProduto() + (rsmItens.getFloat("qt_tributario") * rsmItens.getFloat("vl_unitario"))));
				nota.setVlTotalNota((nota.getVlTotalNota() + (rsmItens.getFloat("qt_tributario") * rsmItens.getFloat("vl_unitario") - rsmItens.getFloat("vl_desconto"))));
			}
			
			//Itera sobre os tributos da nota para zera-los
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_nota_fiscal", "" + nota.getCdNotaFiscal(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = NotaFiscalTributoDAO.find(criterios, connect);
			while(rsm.next()){
				NotaFiscalTributo notaTributo = NotaFiscalTributoDAO.get(nota.getCdNotaFiscal(), rsm.getInt("cd_tributo"), connect);
				notaTributo.setVlBaseCalculo(0);
				notaTributo.setVlBaseRetencao(0);
				notaTributo.setVlOutrasDespesas(0);
				notaTributo.setVlOutrosImpostos(0);
				notaTributo.setVlRetido(0);
				notaTributo.setVlTributo(0);
				if(NotaFiscalTributoDAO.update(notaTributo, connect) <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao zerar tributo da nota!");
				}
			}
			//Itera sobre os tributos dos itens para fazer o tributo da nota
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_nota_fiscal", "" + nota.getCdNotaFiscal(), ItemComparator.EQUAL, Types.INTEGER));
			rsm = NotaFiscalItemTributoDAO.find(criterios, connect);
			while(rsm.next()){
				NotaFiscalTributo notaTributo = NotaFiscalTributoDAO.get(nota.getCdNotaFiscal(), rsm.getInt("cd_tributo"), connect);
				if(notaTributo == null)	{
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao gerar Nota Fiscal Tributo");
					
				}
				else{
					//Faz a soma dos valores antigos com os novos
					notaTributo.setVlBaseCalculo(notaTributo.getVlBaseCalculo() + rsm.getFloat("vl_base_calculo"));
					notaTributo.setVlBaseRetencao(notaTributo.getVlBaseRetencao() + rsm.getFloat("vl_base_retencao"));
					notaTributo.setVlOutrasDespesas(notaTributo.getVlOutrasDespesas() + rsm.getFloat("vl_outras_despesas"));
					notaTributo.setVlOutrosImpostos(0);
					notaTributo.setVlRetido(notaTributo.getVlRetido() + rsm.getFloat("vl_retido"));
					notaTributo.setVlTributo(notaTributo.getVlTributo() + rsm.getFloat("vl_tributo"));
					if(NotaFiscalTributoDAO.update(notaTributo, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao zerar tributo da nota!");
					}
				}
				
			}

			//Itera sobre os tributos da nota para saber o que é imposto direto e qual é de icms por substituicao tributaria
			rsm = NotaFiscalTributoDAO.find(criterios, connect);
			while(rsm.next()){
				Tributo tributo = TributoDAO.get(rsm.getInt("cd_tributo"), connect);
				if(tributo.getIdTributo().equals("ICMS")){
					vlIcmsSt += rsm.getFloat("vl_retido");
				}
				else if(tributo.getIdTributo().equals("IPI")){
					vlImpostosDiretos += rsm.getFloat("vl_tributo");					
				}
				else if(tributo.getIdTributo().equals("II")){
					vlImpostosDiretos += rsm.getFloat("vl_tributo");
				}
			}
			
			// Somando o total da nota - Acrescentar impostos diretos e icms st
			nota.setVlTotalNota(nota.getVlTotalNota() + vlFrete + vlSeguro + vlIcmsSt + vlImpostosDiretos + vlOutrasDespesas);
			
			if(NotaFiscalDAO.update(nota, connect) <= 0){
				Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar total da nota!");
			}
			
			return new Result(1);
		}
		
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro: " + e.getMessage(), e);
		}
		
	}
	
	/**
	 * 
	 * @param nota
	 * @param prDesconto
	 * @param prDescontoMaximo
	 * @param tpDesconto
	 * @param vlAcrescimo
	 * @param tpAcrescimo
	 * @return
	 * @see #update(NotaFiscal, float, float, int, float, int, Connection)
	 */
	public static Result update(NotaFiscal nota, float prDesconto, float prDescontoMaximo, int tpDesconto, float vlAcrescimo, int tpAcrescimo){
		return update(nota, prDesconto, prDescontoMaximo, tpDesconto, vlAcrescimo, tpAcrescimo, null);
	}
	
	/**
	 * Atualiza uma nota fiscal gerada em documento saída a partir dos dados alterados pelo usuário
	 * 
	 * @param nota
	 * @param prDesconto porcentagem de desconto que o usuário pode dar no valor da nota 
	 * @param prDescontoMaximo porcentagem de desconto máximo calculada a partir dos produtos
	 * @param tpDesconto tipo de desconto por item ou por preço
	 * @param vlAcrescimo quantidade de valor do acrécimo
	 * @param tpAcrescimo rateado nos itens, acrescentado na nota, 
	 * @param connect
	 * @return
	 */
	public static Result update(NotaFiscal nota, float prDesconto, float prDescontoMaximo, int tpDesconto, float vlAcrescimo, int tpAcrescimo, Connection connect){//tpAcrescimo 0: Desconsiderar, 1:Colocar no Outras Despesas, 2: Ratear nos itens 
		boolean isConnectionNull = connect == null; 
	
		try	 {
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(prDesconto > 100 || prDesconto < 0)
				return new Result(-1, "Valor de desconto deve estar na faixa de 1 ate 100");
			//
			// GERANDO ID - NFE
			if(nota.getNrNotaFiscal() != null){
				Result result = gerarNfeId(nota);
				if(result.getCode() <= 0)	{
					if(isConnectionNull)
						Conexao.rollback(connect);
					result.setMessage(result.getMessage()+" Falha ao tentar gerar ID da Nota Fiscal.");
					return result;
				}
				nota.setNrChaveAcesso((String)result.getObjects().get("nrChave"));
				nota.setNrDv((Integer)result.getObjects().get("nrDv"));
			}
			if(NotaFiscalDAO.update(nota, connect) <= 0)	{
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Falha ao atualizar ID da NFE");
			}
			//Para notas de apenas um documento e que vao acrescentar o valor de acrescimo
			if(vlAcrescimo <= 0 && tpAcrescimo > 0){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_nota_fiscal", "" + nota.getCdNotaFiscal(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmDocs = NotaFiscalDocVinculadoDAO.find(criterios, connect);
				while(rsmDocs.next()){
					if(rsmDocs.getInt("cd_documento_saida") > 0){
						DocumentoSaida docSaida = DocumentoSaidaDAO.get(rsmDocs.getInt("cd_documento_saida"), connect);
						vlAcrescimo += docSaida.getVlAcrescimo();
					}
					else if(rsmDocs.getInt("cd_documento_entrada") > 0){
						DocumentoEntrada docEntrada = DocumentoEntradaDAO.get(rsmDocs.getInt("cd_documento_entrada"), connect);
						vlAcrescimo += docEntrada.getVlAcrescimo();
					}
				}
			}
		
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_nota_fiscal", "" + nota.getCdNotaFiscal(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmItens = NotaFiscalItemDAO.find(criterios, connect);
			//Dividi o valor de acrescimo no valor unitario dos itens da nota
			float somaItens = 0;
			float somaDescontos = 0;
			while(rsmItens.next())	{
				somaItens     += rsmItens.getFloat("vl_unitario") * rsmItens.getFloat("qt_tributario");
				somaDescontos += rsmItens.getFloat("vl_desconto");
			}
			rsmItens.beforeFirst();
			if(vlAcrescimo > 0 ){
				//Zera o valor de acrescimo dos itens
				if(tpAcrescimo == 0){
					while(rsmItens.next())	{
						int cdNotaFiscal 	     = nota.getCdNotaFiscal();
						int cdItem    		     = rsmItens.getInt("cd_item");
						NotaFiscalItem notaItem  = NotaFiscalItemDAO.get(cdNotaFiscal, cdItem, connect);
						notaItem.setVlAcrescimo(0);
						if(NotaFiscalItemDAO.update(notaItem, connect) <= 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao atualizar a item da nota!");
						}
					}
				}
				
				//Acrescentar o valor de acrescimo do documento em Outras Despesas da Nota
				if(tpAcrescimo == 1){
					if(nota.getVlOutrasDespesas() > 0){
						nota.setVlTotalNota(nota.getVlTotalNota() - nota.getVlOutrasDespesas());
					}
					float vlAcrescimoItens = 0;
					while(rsmItens.next())	{
						int cdNotaFiscal 	     = nota.getCdNotaFiscal();
						int cdItem    		     = rsmItens.getInt("cd_item");
						NotaFiscalItem notaItem  = NotaFiscalItemDAO.get(cdNotaFiscal, cdItem, connect);
						vlAcrescimoItens += notaItem.getVlAcrescimo();
						notaItem.setVlAcrescimo(0);
						if(NotaFiscalItemDAO.update(notaItem, connect) <= 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao atualizar a item da nota!");
						}
					}
					nota.setVlOutrasDespesas(vlAcrescimo + vlAcrescimoItens);
					nota.setVlTotalNota(nota.getVlTotalNota() + nota.getVlOutrasDespesas());
					
					if(NotaFiscalDAO.update(nota, connect) <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao acrescentar o acrescimo em outras despesas da nota!");
					}
				}
				//Acrescentar o valor de acrescimodo documento rateado nos itens
				else if(tpAcrescimo == 2){
					rsmItens.beforeFirst();
					while(rsmItens.next())	{
						int cdNotaFiscal 	     = nota.getCdNotaFiscal();
						int cdItem    		     = rsmItens.getInt("cd_item");
						NotaFiscalItem notaItem  = NotaFiscalItemDAO.get(cdNotaFiscal, cdItem, connect);
						ProdutoServicoEmpresa produtoEmpresa = ProdutoServicoEmpresaDAO.get(notaItem.getCdEmpresa(), notaItem.getCdProdutoServico(), connect);
						float parte = (notaItem.getVlUnitario() * notaItem.getQtTributario()) / somaItens;
						float aDistribuir = vlAcrescimo * parte;
						float aSomar	  = (aDistribuir+rsmItens.getFloat("vl_acrescimo")) / notaItem.getQtTributario();
						//Soma o valor unitario com a sua parte no valor de acrescimo
						float novoVlUnitario = (produtoEmpresa.getQtPrecisaoCusto() <= 0 ? (float)Util.arredondar(notaItem.getVlUnitario() + aSomar, 2) : (float)Util.arredondar(notaItem.getVlUnitario() + aSomar, produtoEmpresa.getQtPrecisaoCusto()));
						notaItem.setVlUnitario(novoVlUnitario);
						notaItem.setVlAcrescimo(0);
						if(NotaFiscalItemDAO.update(notaItem, connect) <= 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao atualizar a item da nota!");
						}
					}
					
					//Faz o recalculo dos tributos da nota por conta da alteração nas bases de calculo
					Result resultado = recalcTributoNota(nota, connect);
					if(resultado.getCode() <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, resultado.getMessage());
					}
					
					if(NotaFiscalDAO.update(nota, connect) <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar a nota!");
					}
				}
				
			}
			//Rateia o valor dos acrescimos e descontos de item no valor unitario
			else{
				rsmItens.beforeFirst();
				while(rsmItens.next())	{
					int cdNotaFiscal 	     = nota.getCdNotaFiscal();
					int cdItem    		     = rsmItens.getInt("cd_item");
					NotaFiscalItem notaItem  = NotaFiscalItemDAO.get(cdNotaFiscal, cdItem, connect);
					ProdutoServicoEmpresa produtoEmpresa = ProdutoServicoEmpresaDAO.get(notaItem.getCdEmpresa(), notaItem.getCdProdutoServico(), connect);
					float aSomar	  = (rsmItens.getFloat("vl_acrescimo")) / notaItem.getQtTributario();
					float aDiminuir	  = (rsmItens.getFloat("vl_desconto")) / notaItem.getQtTributario();
					//Soma o valor unitario com a sua parte no valor de acrescimo
					float novoVlUnitario = (produtoEmpresa.getQtPrecisaoCusto() <= 0 ? (float)Util.arredondar(notaItem.getVlUnitario() + aSomar - aDiminuir, 2) : (float)Util.arredondar(notaItem.getVlUnitario() + aSomar - aDiminuir, produtoEmpresa.getQtPrecisaoCusto()));
					notaItem.setVlUnitario(novoVlUnitario);
					notaItem.setVlAcrescimo(0);
					notaItem.setVlDesconto(0);
					if(NotaFiscalItemDAO.update(notaItem, connect) <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar a item da nota!");
					}
				}
			}
			if(prDesconto == 0)	{
				if(isConnectionNull)
					connect.commit();
				return new Result(1, "Atualizado com sucesso!");
			}
			
			rsmItens.beforeFirst();
			float vlDescontado = somaItens - (prDesconto / 100 * somaItens);
			float vlDescontadoDesconto = somaDescontos - (prDesconto / 100 * somaDescontos);
			
			float vlDescontadoDoTotal = 0;
			float vlDescontadoDoTotalDesconto = 0;
			
			while(rsmItens.next())	{
				int cdNotaFiscal 	     = nota.getCdNotaFiscal();
				int cdItem    		     = rsmItens.getInt("cd_item");
				
				NotaFiscalItem notaItem  = NotaFiscalItemDAO.get(cdNotaFiscal, cdItem, connect);
				ProdutoServicoEmpresa produtoEmpresa = ProdutoServicoEmpresaDAO.get(notaItem.getCdEmpresa(), notaItem.getCdProdutoServico(), connect);

				float prDescMax = produtoEmpresa.getPrDescontoMaximo();
				if(tpDesconto == NotaFiscalServices.NO_PRECO)	{
					int precisao = produtoEmpresa.getQtPrecisaoCusto() > 0 ? produtoEmpresa.getQtPrecisaoCusto() : 2;
					float novoVlUnitario = (float)Util.arredondar(notaItem.getVlUnitario(), precisao) - (float)Util.arredondar((notaItem.getVlUnitario() * prDescMax/ 100), precisao);
					float novoVlDesconto = notaItem.getVlDesconto() - (notaItem.getVlDesconto() * prDescMax / 100);
					rsmItens.setValueToField("novoVlUnitario", novoVlUnitario);
					rsmItens.setValueToField("novoVlDesconto", novoVlDesconto);
					
					vlDescontadoDoTotal += (novoVlUnitario * notaItem.getQtTributario());
					vlDescontadoDoTotalDesconto += novoVlDesconto;
				}
				else	{
					float novoVlDesconto = (produtoEmpresa.getQtPrecisaoCusto() <= 0 ? (float)Util.arredondar((notaItem.getVlUnitario() * notaItem.getQtTributario()) * prDescMax / 100, 2): (float)Util.arredondar((notaItem.getVlUnitario() * notaItem.getQtTributario()) * prDescMax / 100, produtoEmpresa.getQtPrecisaoCusto()));
					rsmItens.setValueToField("novoVlDesconto", novoVlDesconto);
					vlDescontadoDoTotalDesconto += novoVlDesconto;
				}
			}
			rsmItens.beforeFirst();
			float vlDiferenca 		  = vlDescontado - vlDescontadoDoTotal;
			float vlDiferencaDesconto = vlDescontadoDesconto - vlDescontadoDoTotalDesconto;
			float somaVlDescontadoTotal = 0;
			float somaVlDescontadoTotalDesconto = 0;
			// float somaVlDiferenca = 0;
			NotaFiscalItem notaPodeReceber = null; 
			while(rsmItens.next()){
				int precisao = rsmItens.getInt("qt_precisao_custo") <= 0 ? 2 : rsmItens.getInt("qt_precisao_custo");
				int cdNotaFiscal 	     = nota.getCdNotaFiscal();
				int cdItem    		     = rsmItens.getInt("cd_item");
				NotaFiscalItem notaItem  = NotaFiscalItemDAO.get(cdNotaFiscal, cdItem, connect);
				ProdutoServicoEmpresa produtoEmpresa = ProdutoServicoEmpresaDAO.get(nota.getCdEmpresa(), rsmItens.getInt("cd_produto_servico"), connect);
				// ProdutoServico produto = ProdutoServicoDAO.get(rsmItens.getInt("cd_produto_servico"), connect);
				float prDescMax = produtoEmpresa.getPrDescontoMaximo();
				if(prDescMax != 0)
					notaPodeReceber = notaItem;
				
				float vlSobra 		 = (rsmItens.getFloat("vl_unitario") * rsmItens.getFloat("qt_tributario")) * prDescMax / 100;
				float parte 		 = vlSobra / (somaItens - vlDescontadoDoTotal) * 100;
				float aSomar 		 = (parte / 100 * vlDiferenca) / rsmItens.getFloat("qt_tributario");
				somaVlDescontadoTotal +=aSomar * rsmItens.getFloat("qt_tributario");
				aSomar = (float)Util.arredondar(aSomar, precisao);
				
				float vlSobraDesconto = rsmItens.getFloat("vl_desconto") * prDescMax / 100;
				float parteDesconto = vlSobraDesconto / ((somaDescontos - vlDescontadoDoTotalDesconto) == 0 ? 1 : (somaDescontos - vlDescontadoDoTotalDesconto)) * 100;
				float aSomarDesconto = parteDesconto / 100 * vlDiferencaDesconto;
				somaVlDescontadoTotalDesconto +=Util.arredondar(aSomarDesconto, precisao);
				
				
				if(tpDesconto == NotaFiscalServices.NO_PRECO){
					notaItem.setVlUnitario(rsmItens.getFloat("novoVlUnitario") + aSomar);
					notaItem.setVlDesconto(rsmItens.getFloat("novoVlDesconto") + aSomarDesconto);
				}
				else{
					notaItem.setVlDesconto(rsmItens.getFloat("novoVlDesconto") + aSomarDesconto);
				}
				if(NotaFiscalItemDAO.update(notaItem, connect) <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar a item da nota!");
				}
			}
			
//			if(tpDesconto == NotaFiscalServices.NO_PRECO){
//				notaPodeReceber.setVlUnitario(notaPodeReceber.getVlUnitario() + (float)Util.arredondar((vlDiferenca - somaVlDescontadoTotal), precisao));
//				notaPodeReceber.setVlDesconto(notaPodeReceber.getVlDesconto() + (float)Util.arredondar((vlDiferencaDesconto - somaVlDescontadoTotalDesconto), precisao));
//			}
//			else{
//				notaPodeReceber.setVlDesconto(notaPodeReceber.getVlDesconto() + (vlDescontadoDesconto - somaVlDescontadoTotalDesconto));
//			}
			
			if(NotaFiscalItemDAO.update(notaPodeReceber, connect) <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar a item da nota!");
			}
			
			//Faz o recalculo dos tributos da nota por conta da alteração nas bases de calculo
			Result resultado = recalcTributoNota(nota, connect);
			if(resultado.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao recalcular tributos da nota:" + resultado.getMessage());
			}
			
			//Atualizar a nota para incluir a porcentagem de desconto usada
			nota.setPrDesconto(prDesconto);
			
			if(NotaFiscalDAO.update(nota, connect) <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar a nota!");
			}
			
			if(isConnectionNull)
				connect.commit();
			return new Result(1, "Atualização realizada com sucesso!");
		}
		catch(Exception e) {
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro: " + e.getMessage(), e);
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static float getVlDesconto(int cdNotaFiscal){
		return getVlDesconto(cdNotaFiscal, null);
	}
	public static float getVlDesconto(int cdNotaFiscal, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT A.vl_desconto FROM fsc_nota_fiscal_item A WHERE A.cd_nota_fiscal=?");
			pstmt.setInt(1, cdNotaFiscal);
			rs = pstmt.executeQuery();
			float vlTotalDesconto = 0;
			while(rs.next()){
				vlTotalDesconto += rs.getFloat("vl_desconto");
			}
			return vlTotalDesconto;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! getVlDesconto.get: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static float getValorAllItens(int cdNotaFiscal) {
		return getValorAllItens(cdNotaFiscal, null);
	}

	public static float getValorAllItens(int cdNotaFiscal, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			float valorOfProdutos = 0;
			
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement(
					 "SELECT A.* FROM fsc_nota_fiscal_item A " +
					 "WHERE A.cd_nota_fiscal = ?");
			pstmt.setInt(1, cdNotaFiscal);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				float qtTributario = rsm.getFloat("qt_tributario");
				float vlUnitario = rsm.getFloat("vl_unitario");
				float vlAcrescimo = rsm.getFloat("vl_acrescimo");
				float vlDesconto = rsm.getFloat("vl_desconto");
				
				valorOfProdutos += (qtTributario * vlUnitario) + vlAcrescimo - vlDesconto;
				
			}
			
			return valorOfProdutos;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaServices.getValorAllItens: " + e);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static float getQuantidadeAllItens(int cdNotaFiscal) {
		return getQuantidadeAllItens(cdNotaFiscal, null);
	}

	public static float getQuantidadeAllItens(int cdNotaFiscal, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			float quantidadeTotal = 0;
			
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement(
					 "SELECT A.* FROM fsc_nota_fiscal_item A " +
					 "WHERE A.cd_nota_fiscal = ?");
			pstmt.setInt(1, cdNotaFiscal);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				float qtTributario = rsm.getFloat("qt_tributario");
				
				quantidadeTotal += qtTributario;
				
			}
			
			return quantidadeTotal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaServices.getValorAllItens: " + e);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap getAllItens(int cdNotaFiscal, int tpNatDoc) {
		return getAllItens(cdNotaFiscal, tpNatDoc, null);
	}
	
	public static ResultSetMap getAllItens(int cdNotaFiscal) {
		return getAllItens(cdNotaFiscal, 1, null);
	}

	public static ResultSetMap getAllItens(int cdNotaFiscal, Connection connect) {
		return getAllItens(cdNotaFiscal, 1, connect);
	}
	
	public static ResultSetMap getAllItens(int cdNotaFiscal, int tpNatDoc/*0 - Entrada ou 1 - Saida*/, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			
			int cdTributoICMS    = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0, 0, connect);
			int cdTributoIPI 	 = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_IPI", 0, 0, connect);
			int cdTributoII      = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_II", 0, 0, connect);
			int cdTributoPIS 	 = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0, 0, connect);
			int cdTributoCOFINS  = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0, 0, connect);
			
			
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement(
					 "SELECT A.*, C.nm_produto_servico, C.txt_produto_servico, C.tp_produto_servico, C.id_produto_servico, " +
					 "       C.id_produto_servico, C.sg_produto_servico, C.cd_ncm, B.st_produto_empresa, B.id_reduzido, " +
					 "       C.cd_classificacao_fiscal, E.nm_classificacao_fiscal, " +
					 "       G.cd_unidade_medida, G.sg_unidade_medida, G.nm_unidade_medida, txt_especificacao, txt_dado_tecnico, " +
					 "       H.nm_pessoa AS nm_fabricante, C.id_produto_servico, B.qt_precisao_unidade, J.nr_codigo_fiscal, " +
					 "       (SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) " +
					 "        FROM adm_saida_item_aliquota I " +
					 "        WHERE I.cd_produto_servico = A.cd_produto_servico " +
					 "          AND I.cd_documento_saida = A.cd_documento_saida " +
					 "          AND I.cd_empresa = A.cd_empresa) AS vl_total_tributos " +
					 "FROM fsc_nota_fiscal_item    A " +
					 "JOIN grl_produto_servico_empresa         B ON (A.cd_produto_servico = B.cd_produto_servico AND A.cd_empresa = B.cd_empresa) " +
					 "JOIN grl_produto_servico                 C ON (B.cd_produto_servico = C.cd_produto_servico) " +
					 "LEFT OUTER JOIN grl_produto              D ON (C.cd_produto_servico = D.cd_produto_servico) " +
					 "LEFT OUTER JOIN adm_classificacao_fiscal E ON (C.cd_classificacao_fiscal = E.cd_classificacao_fiscal) " +
					 (tpNatDoc == 1 ? "JOIN alm_documento_saida_item            I ON (I.cd_documento_saida = A.cd_documento_saida AND I.cd_item = A.cd_item_documento) " : 
							 		  "JOIN alm_documento_entrada_item          I ON (I.cd_documento_entrada = A.cd_documento_entrada AND A.cd_produto_servico = I.cd_produto_servico AND A.cd_empresa = I.cd_empresa) ") +
					 "LEFT OUTER JOIN grl_unidade_medida 	   G ON (I.cd_unidade_medida = G.cd_unidade_medida) " +
					 "LEFT OUTER JOIN grl_pessoa               H ON (C.cd_fabricante = H.cd_pessoa) " +
					 "LEFT OUTER JOIN adm_natureza_operacao    J ON (A.cd_natureza_operacao = J.cd_natureza_operacao) " +
					 "WHERE A.cd_nota_fiscal = ?");
			pstmt.setInt(1, cdNotaFiscal);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.size() == 0){
				pstmt = connect.prepareStatement(
						 "SELECT A.*, C.nm_produto_servico, C.txt_produto_servico, C.tp_produto_servico, C.id_produto_servico, " +
						 "       C.id_produto_servico, C.sg_produto_servico, C.cd_ncm, B.st_produto_empresa, B.id_reduzido, " +
						 "       C.cd_classificacao_fiscal, E.nm_classificacao_fiscal, " +
						 "       G.cd_unidade_medida, G.sg_unidade_medida, G.nm_unidade_medida, txt_especificacao, txt_dado_tecnico, " +
						 "       H.nm_pessoa AS nm_fabricante, C.id_produto_servico, B.qt_precisao_unidade, J.nr_codigo_fiscal, " +
						 "       (SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) " +
						 "        FROM adm_saida_item_aliquota I " +
						 "        WHERE I.cd_produto_servico = A.cd_produto_servico " +
						 "          AND I.cd_documento_saida = A.cd_documento_saida " +
						 "          AND I.cd_empresa = A.cd_empresa) AS vl_total_tributos " +
						 "FROM fsc_nota_fiscal_item    A " +
						 "JOIN grl_produto_servico_empresa         B ON (A.cd_produto_servico = B.cd_produto_servico AND A.cd_empresa = B.cd_empresa) " +
						 "JOIN grl_produto_servico                 C ON (B.cd_produto_servico = C.cd_produto_servico) " +
						 "LEFT OUTER JOIN grl_produto              D ON (C.cd_produto_servico = D.cd_produto_servico) " +
						 "LEFT OUTER JOIN adm_classificacao_fiscal E ON (C.cd_classificacao_fiscal = E.cd_classificacao_fiscal) " +
						 (tpNatDoc == 0 ? "JOIN alm_documento_saida_item            I ON (I.cd_documento_saida = A.cd_documento_saida AND I.cd_item = A.cd_item_documento) " : 
								 		  "JOIN alm_documento_entrada_item          I ON (I.cd_documento_entrada = A.cd_documento_entrada AND A.cd_produto_servico = I.cd_produto_servico AND A.cd_empresa = I.cd_empresa) ") +
						 "LEFT OUTER JOIN grl_unidade_medida 	   G ON (I.cd_unidade_medida = G.cd_unidade_medida) " +
						 "LEFT OUTER JOIN grl_pessoa               H ON (C.cd_fabricante = H.cd_pessoa) " +
						 "LEFT OUTER JOIN adm_natureza_operacao    J ON (A.cd_natureza_operacao = J.cd_natureza_operacao) " +
						 "WHERE A.cd_nota_fiscal = ?");
				pstmt.setInt(1, cdNotaFiscal);
				rsm = new ResultSetMap(pstmt.executeQuery());
			}
			
			while(rsm.next()){
				//Total Tributos
				ResultSetMap rsm2 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_total_tributos, SUM(I.vl_base_calculo) AS vl_total_base_calculo FROM fsc_nota_fiscal_item_tributo I " +
																		 "        WHERE I.cd_item =  " + rsm.getInt("cd_item") +
																		 "          AND I.cd_nota_fiscal = " +rsm.getInt("cd_nota_fiscal")).executeQuery());
				while(rsm2.next()){
					rsm.setValueToField("vl_total_tributos", rsm2.getFloat("vl_total_tributos"));
					rsm.setValueToField("vl_total_base_calculo", rsm2.getFloat("vl_total_base_calculo"));
				}
				
				//Total Tributos do Documento
				ResultSetMap rsm3 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_total_tributos_documento, SUM(I.vl_base_calculo) AS vl_total_base_calculo_documento FROM fsc_nota_fiscal_item_tributo I " +
																		 "        WHERE I.cd_nota_fiscal = " +rsm.getInt("cd_nota_fiscal")).executeQuery());
				while(rsm3.next()){
					rsm.setValueToField("vl_total_tributos_documento", rsm3.getFloat("vl_total_tributos_documento"));
					rsm.setValueToField("vl_total_base_calculo_documento", rsm3.getFloat("vl_total_base_calculo_documento"));
				}
				
				//Total Tributo ICMS
				ResultSetMap rsm4 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_icms, I.pr_aliquota, SUM(I.vl_base_calculo) AS vl_base_icms, P.st_tributaria, Q.nr_situacao_tributaria, Q.cd_situacao_tributaria FROM fsc_nota_fiscal_item_tributo I " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " + 
																		 "        WHERE I.cd_item =  " + rsm.getInt("cd_item") +
																		 "          AND I.cd_nota_fiscal = " +rsm.getInt("cd_nota_fiscal") +
																		 "          AND P.cd_tributo           = "+cdTributoICMS + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria, Q.cd_situacao_tributaria, I.pr_aliquota").executeQuery());
				if(rsm4.next()){
					rsm.setValueToField("vl_icms", rsm4.getFloat("vl_icms"));
					rsm.setValueToField("vl_base_icms", rsm4.getFloat("vl_base_icms"));
					rsm.setValueToField("pr_icms", rsm4.getFloat("pr_aliquota"));
					rsm.setValueToField("st_tributaria", rsm4.getInt("st_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria", rsm4.getString("nr_situacao_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria_icms", rsm4.getString("nr_situacao_tributaria"));
					rsm.setValueToField("cd_situacao_tributaria_icms", rsm4.getString("cd_situacao_tributaria"));
				}
				if(rsm4.next()){
					rsm.setValueToField("ErroAliq", 1);
				}
				
				//Total Tributo ICMS Documento
				ResultSetMap rsm5 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_icms, SUM(I.vl_base_calculo) AS vl_base_icms, P.st_tributaria, Q.nr_situacao_tributaria, Q.cd_situacao_tributaria FROM fsc_nota_fiscal_item_tributo I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_nota_fiscal = " +rsm.getInt("cd_nota_fiscal") +
																		 "          AND P.cd_tributo           = "+cdTributoICMS + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria, Q.cd_situacao_tributaria").executeQuery());
				
				while(rsm5.next()){
					if(rsm5.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
						rsm.setValueToField("vl_icms_documento", rsm5.getFloat("vl_icms"));
						rsm.setValueToField("vl_base_icms_documento", rsm5.getFloat("vl_base_icms"));
						rsm.setValueToField("st_tributaria_documento", rsm5.getInt("st_tributaria"));
					}
					if(rsm5.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
						rsm.setValueToField("vl_icms_substituto_documento", rsm5.getFloat("vl_icms"));
						rsm.setValueToField("vl_base_icms_substituto_documento", rsm5.getFloat("vl_base_icms"));
						rsm.setValueToField("st_tributaria_substituto_documento", rsm5.getInt("st_tributaria"));
					}
				}
				
				//Total Tributo IPI
				ResultSetMap rsm6 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_ipi, I.pr_aliquota, SUM(I.vl_base_calculo) AS vl_base_ipi, P.st_tributaria, Q.nr_situacao_tributaria, Q.cd_situacao_tributaria FROM fsc_nota_fiscal_item_tributo I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_item =  " + rsm.getInt("cd_item") +
																		 "          AND I.cd_nota_fiscal = " +rsm.getInt("cd_nota_fiscal") +
																		 "          AND P.cd_tributo           = "+cdTributoIPI + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria, Q.cd_situacao_tributaria, I.pr_aliquota").executeQuery());
				if(rsm6.next()){
					rsm.setValueToField("vl_ipi", rsm6.getFloat("vl_ipi"));
					rsm.setValueToField("vl_base_ipi", rsm6.getFloat("vl_base_ipi"));
					rsm.setValueToField("pr_ipi", rsm6.getFloat("pr_aliquota"));
					rsm.setValueToField("st_tributaria", rsm6.getInt("st_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria", rsm6.getString("nr_situacao_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria_ipi", rsm6.getString("nr_situacao_tributaria"));
					rsm.setValueToField("cd_situacao_tributaria_ipi", rsm6.getString("cd_situacao_tributaria"));
				}
				if(rsm6.next()){
					rsm.setValueToField("ErroAliq", 2);
				}
				
				//Total Tributo IPI do Documento
				ResultSetMap rsm7 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_ipi, SUM(I.vl_base_calculo) AS vl_base_ipi, P.st_tributaria, Q.nr_situacao_tributaria, Q.cd_situacao_tributaria FROM fsc_nota_fiscal_item_tributo I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_nota_fiscal = " +rsm.getInt("cd_nota_fiscal") +
																		 "			AND P.cd_tributo           = "+cdTributoIPI + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria, Q.cd_situacao_tributaria").executeQuery());
				while(rsm7.next()){
					if(rsm7.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
						rsm.setValueToField("vl_ipi_documento", rsm7.getFloat("vl_ipi"));
						rsm.setValueToField("vl_base_ipi_documento", rsm7.getFloat("vl_base_ipi"));
						rsm.setValueToField("st_tributaria_documento", rsm7.getInt("st_tributaria"));
					}
					if(rsm7.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
						rsm.setValueToField("vl_ipi_substituto_documento", rsm7.getFloat("vl_ipi"));
						rsm.setValueToField("vl_base_ipi_substituto_documento", rsm7.getFloat("vl_base_ipi"));
						rsm.setValueToField("st_tributaria_substituto_documento", rsm7.getInt("st_tributaria"));
					}
				}
				
				//Total Tributo II
				ResultSetMap rsm8 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_ii, I.pr_aliquota, SUM(I.vl_base_calculo) AS vl_base_ii, P.st_tributaria, Q.nr_situacao_tributaria, Q.cd_situacao_tributaria FROM fsc_nota_fiscal_item_tributo I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_item =  " + rsm.getInt("cd_item") +
																		 "          AND I.cd_nota_fiscal = " +rsm.getInt("cd_nota_fiscal") +
																		 "          AND P.cd_tributo           = "+cdTributoII + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria, Q.cd_situacao_tributaria, I.pr_aliquota").executeQuery());
				if(rsm8.next()){
					rsm.setValueToField("vl_ii", rsm8.getFloat("vl_ii"));
					rsm.setValueToField("vl_base_ii", rsm8.getFloat("vl_base_ii"));
					rsm.setValueToField("pr_ii", rsm8.getFloat("pr_aliquota"));
					rsm.setValueToField("st_tributaria", rsm8.getInt("st_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria", rsm8.getString("nr_situacao_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria_ii", rsm8.getString("nr_situacao_tributaria"));
					rsm.setValueToField("cd_situacao_tributaria_ii", rsm8.getString("cd_situacao_tributaria"));
				}
				if(rsm8.next()){
					rsm.setValueToField("ErroAliq", 3);
				}
				
				//Total Tributo II do Documento
				ResultSetMap rsm9 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_ii, SUM(I.vl_base_calculo) AS vl_base_ii, P.st_tributaria, Q.nr_situacao_tributaria, Q.cd_situacao_tributaria FROM fsc_nota_fiscal_item_tributo I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_nota_fiscal = " +rsm.getInt("cd_nota_fiscal") +
																		 "			AND P.cd_tributo           = "+cdTributoII + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria, Q.cd_situacao_tributaria").executeQuery());
				while(rsm9.next()){
					if(rsm9.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
						rsm.setValueToField("vl_ii_documento", rsm9.getFloat("vl_ii"));
						rsm.setValueToField("vl_base_ii_documento", rsm9.getFloat("vl_base_ii"));
						rsm.setValueToField("st_tributaria_documento", rsm9.getInt("st_tributaria"));
					}
					if(rsm9.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
						rsm.setValueToField("vl_ii_substituto_documento", rsm9.getFloat("vl_ii"));
						rsm.setValueToField("vl_base_ii_substituto_documento", rsm9.getFloat("vl_base_ii"));
						rsm.setValueToField("st_tributaria_substituto_documento", rsm9.getInt("st_tributaria"));
					}
				}
				
				//Total Tributo PIS
				ResultSetMap rsm10 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_pis, I.pr_aliquota, SUM(I.vl_base_calculo) AS vl_base_pis, P.st_tributaria, Q.nr_situacao_tributaria, Q.cd_situacao_tributaria FROM fsc_nota_fiscal_item_tributo I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_item =  " + rsm.getInt("cd_item") +
																		 "          AND I.cd_nota_fiscal = " +rsm.getInt("cd_nota_fiscal") +
																		 "          AND P.cd_tributo           = "+cdTributoPIS + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria, Q.cd_situacao_tributaria, I.pr_aliquota").executeQuery());
				if(rsm10.next()){
					rsm.setValueToField("vl_pis", rsm10.getFloat("vl_pis"));
					rsm.setValueToField("vl_base_pis", rsm10.getFloat("vl_base_pis"));
					rsm.setValueToField("pr_pis", rsm10.getFloat("pr_aliquota"));
					rsm.setValueToField("st_tributaria", rsm10.getInt("st_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria", rsm10.getString("nr_situacao_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria_pis", rsm10.getString("nr_situacao_tributaria"));
					rsm.setValueToField("cd_situacao_tributaria_pis", rsm10.getString("cd_situacao_tributaria"));
				}
				if(rsm10.next()){
					rsm.setValueToField("ErroAliq", 4);
				}
				
				//Total Tributo PIS do Documento
				ResultSetMap rsm11 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_pis, SUM(I.vl_base_calculo) AS vl_base_pis, P.st_tributaria, Q.nr_situacao_tributaria, Q.cd_situacao_tributaria FROM fsc_nota_fiscal_item_tributo I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_nota_fiscal = " +rsm.getInt("cd_nota_fiscal") +
																		 "			AND P.cd_tributo           = "+cdTributoPIS + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria, Q.cd_situacao_tributaria").executeQuery());
				while(rsm11.next()){
					if(rsm11.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
						rsm.setValueToField("vl_pis_documento", rsm11.getFloat("vl_pis"));
						rsm.setValueToField("vl_base_pis_documento", rsm11.getFloat("vl_base_pis"));
						rsm.setValueToField("st_tributaria_documento", rsm11.getInt("st_tributaria"));
					}
					if(rsm11.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
						rsm.setValueToField("vl_pis_substituto_documento", rsm11.getFloat("vl_pis"));
						rsm.setValueToField("vl_base_pis_substituto_documento", rsm11.getFloat("vl_base_pis"));
						rsm.setValueToField("st_tributaria_substituto_documento", rsm11.getInt("st_tributaria"));
					}
				}
				
				//Total Tributo COFINS
				ResultSetMap rsm12 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_cofins, I.pr_aliquota, SUM(I.vl_base_calculo) AS vl_base_cofins, P.st_tributaria, Q.nr_situacao_tributaria, Q.cd_situacao_tributaria FROM fsc_nota_fiscal_item_tributo I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_item =  " + rsm.getInt("cd_item") +
																		 "          AND I.cd_nota_fiscal = " +rsm.getInt("cd_nota_fiscal") +
																		 "          AND P.cd_tributo           = "+cdTributoCOFINS + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria, Q.cd_situacao_tributaria, I.pr_aliquota").executeQuery());
				if(rsm12.next()){
					rsm.setValueToField("vl_cofins", rsm12.getFloat("vl_cofins"));
					rsm.setValueToField("vl_base_cofins", rsm12.getFloat("vl_base_cofins"));
					rsm.setValueToField("pr_cofins", rsm12.getFloat("pr_aliquota"));
					rsm.setValueToField("st_tributaria", rsm12.getInt("st_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria", rsm12.getString("nr_situacao_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria_cofins", rsm12.getString("nr_situacao_tributaria"));
					rsm.setValueToField("cd_situacao_tributaria_cofins", rsm12.getString("cd_situacao_tributaria"));
				}
				if(rsm12.next()){
					rsm.setValueToField("ErroAliq", 5);
				}
				
				//Total Tributo COFINS do Documento
				ResultSetMap rsm13 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_cofins, SUM(I.vl_base_calculo) AS vl_base_cofins, P.st_tributaria, Q.nr_situacao_tributaria, Q.cd_situacao_tributaria FROM fsc_nota_fiscal_item_tributo I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_nota_fiscal = " +rsm.getInt("cd_nota_fiscal") +
																		 "			AND P.cd_tributo           = "+cdTributoCOFINS + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria, Q.cd_situacao_tributaria").executeQuery());
				while(rsm13.next()){
					if(rsm13.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
						rsm.setValueToField("vl_cofins_documento", rsm13.getFloat("vl_cofins"));
						rsm.setValueToField("vl_base_cofins_documento", rsm13.getFloat("vl_base_cofins"));
						rsm.setValueToField("st_tributaria_documento", rsm13.getInt("st_tributaria"));
					}
					if(rsm13.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
						rsm.setValueToField("vl_cofins_substituto_documento", rsm13.getFloat("vl_cofins"));
						rsm.setValueToField("vl_base_cofins_substituto_documento", rsm13.getFloat("vl_base_cofins"));
						rsm.setValueToField("st_tributaria_substituto_documento", rsm13.getInt("st_tributaria"));
					}
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalServices.getAllItens: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/*
	 * FOI NECESSÁRIO CRIAR ESSE MÉTODO NESSA CLASSE PARA QUE ESSA CLASSE EVITA CHAMAR A CLASSE NfeServices que não estão sendo suportada pelo PDV
	 */
	
	public static Result gerarNfeId(NotaFiscal nota) { 
		try {  
	    	GregorianCalendar data = nota.getDtEmissao();
	    	String ano = ("" + data.get(Calendar.YEAR)).substring(2);
	    	String mes = (((data.get(Calendar.MONTH) + 1) < 10) ? "0" + (data.get(Calendar.MONTH) + 1) : "" + (data.get(Calendar.MONTH) + 1));
	    	PessoaJuridica emitente = PessoaJuridicaDAO.get(nota.getCdEmpresa());
	    	int cnf 		= (int)(Math.random() * 99999999);
	    	
	    	String cUF      = "29";          // Código da UF do emitente do Documento Fiscal.  
	        String dataAAMM = ano + mes;                             // Ano e Mês de emissão da NF-e.  
	        String cnpjCpf  = Util.formatCnpj(emitente.getNrCnpj()); // CNPJ do emitente.  
	        String mod      = "55";                                  // Modelo do Documento Fiscal.  
	        String serie    = nota.getNrSerie();                     // Série do Documento Fiscal.  
	        String nNF      = nota.getNrNotaFiscal();                // número do Documento Fiscal.  
	        String tpEmis   = String.valueOf(nota.getTpEmissao());   // Forma de emissão da NF-e  
	        String cNF      = String.valueOf(cnf);                   // Código Numérico que compara a Chave de Acesso.  
	          
	        StringBuilder chave = new StringBuilder();  
	        chave.append(Util.fill(cUF, 2, '0', 'E'));  
	        chave.append(Util.fill(dataAAMM, 4, '0', 'E'));  
	        chave.append(Util.fill(cnpjCpf.replaceAll("\\D",""), 14, '0', 'E'));
	        chave.append(Util.fill(mod, 2, '0', 'E'));  
	        chave.append(Util.fill(serie, 3, '0', 'E'));  
	        chave.append(Util.fill(nNF, 9, '0', 'E'));  
	        chave.append(Util.fill(tpEmis, 1, '0', 'E'));  
	        chave.append(Util.fill(cNF, 8, '0', 'E'));  
	        int dv = Util.modulo11(chave.toString());
	        chave.append(dv);
	        
	        chave.insert(0, "NFe"); 
	        
	        Result result = new Result(1); 
	        result.addObject("nrChave", chave.toString());
	        result.addObject("nrDv", dv);
	        
	        return result;
	    } 
	    catch (Exception e) {
	    	e.printStackTrace(System.out);
	    	Util.registerLog(e);
	        return new Result(-1, "Falha ao tentar gerar o ID da Nota Fiscal EletrÃ´nica!", e);
	    }  
	}
	
	public static Result delete(ArrayList<HashMap<String,Object>> registros){
		int i = 0;
		Result resultado = new Result(1, "Exclusões realizadas com sucesso!");
	
		ResultSetMap rs = new ResultSetMap();
		for(i = 0; i < registros.size(); i++){
			int cdNotaFiscal    = registros.get(i)==null || registros.get(i).get("cdNotaFiscal")==null ? 0 : (Integer) registros.get(i).get("cdNotaFiscal");			
			NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal);			
			Result retorno = delete(cdNotaFiscal);			
			if(retorno.getCode() <= 0)
				resultado.setMessage("Nem todas as exclusões foram realizadas com sucesso");
			
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("NUMERO", nota.getNrNotaFiscal());
			register.put("RESULTADO", retorno.getMessage());
			rs.addRegister(register);
		}
		
		resultado.addObject("resultado", rs);
		
		if(i == 0)
			return new Result(-1, "Nenhuma nota foi excluída!");
		
		return resultado;
	}
	
	public static Result delete(int cdNotaFiscal){
		Connection connect = Conexao.conectar();
		try	{
			NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal, connect);
			if(nota.getStNotaFiscal()==ST_AUTORIZADA || nota.getStNotaFiscal()==ST_CANCELADA || nota.getStNotaFiscal()==ST_TRANSMITIDA)
				return new Result(-1, "Não Ã© permitido excluir notas fiscais nas situações: Autorizada, Cancelada ou Transmitida!");
			//Excluir Declaracao de Importacao
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_nota_fiscal", "" + cdNotaFiscal, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmDeclaracao = DeclaracaoImportacaoDAO.find(criterios, connect);
			while(rsmDeclaracao.next()){
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_declaracao_importacao", "" + rsmDeclaracao.getInt("cd_declaracao_importacao"), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmAdicoes = AdicaoDAO.find(criterios, connect);
				while(rsmAdicoes.next()){
					if(AdicaoDAO.delete(rsmAdicoes.getInt("cd_adicao"), connect) <= 0)	{
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao excluir Adição!");
					}
				}
				if(DeclaracaoImportacaoDAO.delete(rsmDeclaracao.getInt("cd_declaracao_importacao"), connect) <= 0)	{
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao excluir Declaracao de Importacao!");
				}
			}
			// Exclui ITENS
			connect.setAutoCommit(false);
			ArrayList<NotaFiscalItem> itens = NotaFiscalItemServices.getAllByCdNfe(cdNotaFiscal);
			for(int i = 0; i < itens.size(); i++)	{
				ArrayList<NotaFiscalItemTributo> notasItemTributo = NotaFiscalItemTributoServices.getAllItemTributo(itens.get(i).getCdNotaFiscal(), itens.get(i).getCdItem(), connect);
				
				for(int j = 0; j < notasItemTributo.size(); j++)
					if(NotaFiscalItemTributoDAO.delete(itens.get(i).getCdNotaFiscal(), itens.get(i).getCdItem(), notasItemTributo.get(j).getCdTributo(), connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao excluir tributos de item!");
					}
				
				if(NotaFiscalItemDAO.delete(itens.get(i).getCdNotaFiscal(), itens.get(i).getCdItem(), connect) <= 0)	{
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao excluir itens!");
				}
			}
			// Exclui DOCUMENTOS VINCULADOS
			ResultSetMap rsmDocs = NotaFiscalDocVinculadoServices.findByNotaFiscal(cdNotaFiscal, connect);
			while(rsmDocs.next()){
				int ver = NotaFiscalDocVinculadoDAO.delete(cdNotaFiscal, rsmDocs.getInt("cd_doc_vinculado"), connect);
				if(ver <= 0)	{
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao excluir Documento Vinculado!");
				}
			}
			// Exclui TRIBUTOS
			ArrayList<NotaFiscalTributo> notasTributo = NotaFiscalTributoServices.getAllTributo(cdNotaFiscal);
			for(int i = 0; i < notasTributo.size(); i++)
				if(NotaFiscalTributoDAO.delete(cdNotaFiscal, notasTributo.get(i).getCdTributo(), connect) <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao excluir Tributos!");
				}
			// Exclui NOTAFISCAL HISTORICO
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_nota_fiscal", "" + cdNotaFiscal, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmNotaHistorico = NotaFiscalHistoricoDAO.find(criterios, connect);
			while(rsmNotaHistorico.next()){
				if(NotaFiscalHistoricoDAO.delete(cdNotaFiscal, rsmNotaHistorico.getInt("cd_historico"), connect) <= 0)	{
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao excluir Historico de Nota Fiscal!");
				}
			}
			//
			if(NotaFiscalDAO.delete(cdNotaFiscal, connect) <= 0)	{	
				Conexao.rollback(connect);
				return new Result(-1, "Erro ao excluir Nota Fiscal!");
			}
			
			connect.commit();
			
			return new Result(1, "Sucesso ao excluir!");
		}
		catch (Exception e) {  
            e.printStackTrace(System.out);
            Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir: " + e);
        } 
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static boolean validaSiglaEstado(String sigla){
		String[] siglasValidas = {"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PR", "PB", "PA", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SE", "SP", "TO", "EX"};
		
		for(int i = 0; i < siglasValidas.length; i++)
			if(sigla.equals(siglasValidas[i]))
				return true;
		
		return false;
		
	}
	
	public static boolean isPlaca(String placa){
		
		Pattern padrao = Pattern.compile("\\p{Upper}\\p{Upper}\\p{Upper}\\d\\d\\d\\d");
		
		Matcher pesquisa = padrao.matcher(placa);
		
		if(pesquisa.matches())
			return true;
		else
			return false;
		
	}
	
	public Result gerarRelatorioNotaFiscalEletronica(int cdNotaFiscal, int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int tpNotaFiscal, 
			                                         int stNotaFiscal, String nrSerie, String nrInicial, String nrFinal, String nrCpfCnpj, String nrChaveAcesso)
	{
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			if(dtInicial != null){
				dtInicial.set(Calendar.HOUR, 0);
				dtInicial.set(Calendar.MINUTE, 0);
				dtInicial.set(Calendar.SECOND, 0);
			}
			if(dtFinal != null){
				dtFinal.set(Calendar.HOUR, 23);
				dtFinal.set(Calendar.MINUTE, 59);
				dtFinal.set(Calendar.SECOND, 59);
			}
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			ResultSetMap rsm = findNotasFiscais(cdNotaFiscal, cdEmpresa, dtInicial, dtFinal, tpNotaFiscal, stNotaFiscal, nrSerie, nrInicial, nrFinal, nrCpfCnpj, nrChaveAcesso, connection);
			
			while(rsm.next()){
				if(rsm.getString("NR_CPF") != null && rsm.getString("NR_CNPJ") == null)
					rsm.setValueToField("NR_CPF_CNPJ", rsm.getString("NR_CPF"));
					
	 			else if(rsm.getString("NR_CPF") == null && rsm.getString("NR_CNPJ") != null)
	 				rsm.setValueToField("NR_CPF_CNPJ", rsm.getString("NR_CNPJ"));
	 			
	 			if(rsm.getString("NM_ST_NOTA_FISCAL") == null)
	 				rsm.setValueToField("NM_ST_NOTA_FISCAL", situacaoNotaFiscal[rsm.getInt("ST_NOTA_FISCAL")]);
	 			
	 			rsm.setValueToField("DT_EMISSAO", rsm.getString("DT_EMISSAO").substring(0, 10));
	 			if(rsm.getString("DT_AUTORIZACAO") != null)
	 				rsm.setValueToField("DT_AUTORIZACAO", rsm.getString("DT_AUTORIZACAO").substring(0, 10));
	 			
	 			if(rsm.getString("NM_RAZAO_SOCIAL") != null)
	 				rsm.setValueToField("NM_DESTINATARIO", (rsm.getString("NM_RAZAO_SOCIAL").length() > 20) ? rsm.getString("NM_RAZAO_SOCIAL").substring(0, 20) : rsm.getString("NM_RAZAO_SOCIAL"));
	 			else if(rsm.getString("NM_PESSOA") != null)
	 				rsm.setValueToField("NM_DESTINATARIO",  (rsm.getString("NM_PESSOA").length() > 20) ? rsm.getString("NM_PESSOA").substring(0, 20) : rsm.getString("NM_PESSOA"));
			}
			rsm.beforeFirst();
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("DS_GRUPO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			HashMap<String, Object> param = new HashMap<String, Object>();
			if(dtInicial != null)
				param.put("dtInicial", Util.convCalendarString(dtInicial));
			if(dtFinal != null)
				param.put("dtFinal", Util.convCalendarString(dtFinal));
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			if (isConnectionNull)
				connection.commit();

			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getHistoricoOf(int cdNotaFiscal) {
		return getHistoricoOf(cdNotaFiscal, null);
	}

	public static ResultSetMap getHistoricoOf(int cdNotaFiscal, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_historico " +
					 										 "WHERE cd_nota_fiscal = "+cdNotaFiscal).executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalServices.getAllItens: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result enviarEmail(int cdNotaFiscal){
		return enviarEmail(cdNotaFiscal, null);
	}
	public static Result enviarEmail(int cdNotaFiscal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
		    
			NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal, connect);
			
			if(nota.getStNotaFiscal() != AUTORIZADA){
				return new Result(-1, "Nota não autorizada!");
			}
			
			Pessoa destinatario = PessoaDAO.get(nota.getCdDestinatario(), connect);
			Empresa empresa = EmpresaDAO.get(nota.getCdEmpresa(), connect);
			int cdEmpresa = empresa.getCdEmpresa();
			if(destinatario.getNmEmail() == null || destinatario.getNmEmail().equals("")){
				return new Result(-1, "O Destinatário não possui um email cadastrado para que se envie o email!");
			}
			if(!Util.isEmail(destinatario.getNmEmail())){
				return new Result(-1, "O Destinatário não possui um email válido!");
			}
			String assunto = "Nota Fiscal Eletrônica - No " + nota.getNrNotaFiscal() + " - da Empresa: " + empresa.getNmRazaoSocial(); 
			String texto = "";//Assinatura da empresa ao desenvolvedor
			String emailDestinatario = destinatario.getNmEmail();
			String xmlSemAssinar = NfeServices.getXmlComProtocolo(cdNotaFiscal, (NotaFiscalHistorico)null, cdEmpresa);
			String xml = "";
			if(CertificadoServices.getTipoCertificado(cdEmpresa) == CertificadoServices.A3)
			   xml = (String) NfeServices.assinarNFeA3(xmlSemAssinar, NfeServices.ENVIO, cdEmpresa).getObjects().get("xmlAssinado"); 
		    else{
			   xml = (String) NfeServices.assinarNFeA1(xmlSemAssinar, NfeServices.ENVIO, cdEmpresa).getObjects().get("xmlAssinado"); 
		    }
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
		    DocumentBuilder db         = dbf.newDocumentBuilder();  
			InputSource inStream = new InputSource();  
	        inStream.setCharacterStream(new StringReader(xmlSemAssinar));  
		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	        factory.setNamespaceAware(true);  
	        
	        org.w3c.dom.Document doc                  = db.parse(inStream);  
		    String recordPath             = "/nfeProc/NFe/infNFe/det";//estrutura do xml  
		    JRXmlDataSource xmlDataSource = new JRXmlDataSource(doc, recordPath);
//		    String path = ContextManager.getRealPath();
//		    JasperPrint jp = JasperFillManager.fillReport(path+"/fsc/danfe.jasper", new HashMap<String,Object>(), xmlDataSource);
		    HashMap<String, Object> params = new HashMap<String, Object>();
		    params.put("LOGO", empresa.getImgLogomarca());
		    params.put("TPEMISSAO", NotaFiscalServices.EMI_NORMAL);
		    
		    NaturezaOperacao natOp = NaturezaOperacaoDAO.get(nota.getCdNaturezaOperacao());
		    params.put("NMCFOP", natOp.getNmNaturezaOperacao());
		    
			ConfManager conf = Util.getConfManager();
	    	String reportPath = conf.getProps().getProperty("REPORT_PATH");
	    	String path = ContextManager.getRealPath()+"/"+reportPath + "/fsc/danfe";
	    	
	    	JasperPrint jp = JasperFillManager.fillReport(ReportServices.getJasperReport(path), params, xmlDataSource);
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    JasperExportManager.exportReportToPdfStream(jp, baos);
			
			HashMap<String, Object> arquivo = new HashMap<String, Object>();
			arquivo.put("BLB_ARQUIVO", xml.getBytes());
			arquivo.put("NM_ARQUIVO", nota.getNrChaveAcesso() + ".xml");
			HashMap<String, Object> arquivo2 = new HashMap<String, Object>();
			arquivo2.put("BLB_ARQUIVO", baos.toByteArray());
			arquivo2.put("NM_ARQUIVO", "DANFE.pdf");
			ArrayList<HashMap<String, Object>> anexos = new ArrayList<HashMap<String, Object>>();
			anexos.add(arquivo);
			anexos.add(arquivo2);
			
			if(ParametroServices.getValorOfParametroAsString("NM_SERVIDOR_SMTP", "").equals(""))
				return new Result(-1, "Nome do Servidor SMTP não configurado nos parametros!");
			if(ParametroServices.getValorOfParametroAsString("NR_PORTA_SMTP", "").equals(""))
				return new Result(-1, "NÃºmero da porta do Servidor SMTP não configurado nos parametros!");
			if(ParametroServices.getValorOfParametroAsString("NM_LOGIN_SERVIDOR_SMTP", "").equals(""))
				return new Result(-1, "Login do Servidor SMTP não configurado nos parametros!");
			if(ParametroServices.getValorOfParametroAsString("NM_SENHA_SERVIDOR_SMTP", "").equals(""))
				return new Result(-1, "Senha do Servidor SMTP não configurado nos parametros!");
			if(ParametroServices.getValorOfParametroAsString("NM_EMAIL_REMETENTE_SMTP", "").equals(""))
				return new Result(-1, "Remetente SMTP não configurado nos parametros!");
			
			if(ParametroServices.getValorOfParametroAsInteger("LG_DEBUG_SMTP", 0) == 1){
				System.out.println("SERV  = " + ParametroServices.getValorOfParametroAsString("NM_SERVIDOR_SMTP", ""));
				System.out.println("PORTA = " + ParametroServices.getValorOfParametroAsInteger("NR_PORTA_SMTP", 0));
				System.out.println("LOGIN = " + ParametroServices.getValorOfParametroAsString("NM_LOGIN_SERVIDOR_SMTP", "")); 
				System.out.println("SENHA = " + ParametroServices.getValorOfParametroAsString("NM_SENHA_SERVIDOR_SMTP", ""));
				System.out.println("TRANS = " + ParametroServices.getValorOfParametroAsString("NM_TRANSPORT_SMTP", "smtp"));								
				System.out.println("AUT   = " + ParametroServices.getValorOfParametroAsInteger("LG_AUTENTICACAO_SMTP", 0));
				System.out.println("SSL   = " + ParametroServices.getValorOfParametroAsInteger("LG_SSL_SMTP", 0));
			}
			SMTPClient cliente = new SMTPClient(ParametroServices.getValorOfParametroAsString("NM_SERVIDOR_SMTP", ""), 
												ParametroServices.getValorOfParametroAsInteger("NR_PORTA_SMTP", 0), 
												ParametroServices.getValorOfParametroAsString("NM_LOGIN_SERVIDOR_SMTP", ""), 
												ParametroServices.getValorOfParametroAsString("NM_SENHA_SERVIDOR_SMTP", ""), 
												ParametroServices.getValorOfParametroAsInteger("LG_AUTENTICACAO_SMTP", 0)==1, 
												ParametroServices.getValorOfParametroAsInteger("LG_SSL_SMTP", 0)==1, ParametroServices.getValorOfParametroAsString("NM_TRANSPORT_SMTP", "smtp"));
			if(ParametroServices.getValorOfParametroAsInteger("LG_DEBUG_SMTP", 0) == 1){
				cliente.setDebug(true);	
			}
			if(cliente.connect()) {
			
				@SuppressWarnings("unchecked")
				HashMap<String, Object>[] attachments = new HashMap[anexos.size()];
				
				for (int i = 0; i < anexos.size(); i++) {
					HashMap<String, Object> register = (HashMap<String, Object>)anexos.get(i); 
					register.put("BYTES", register.get("BLB_ARQUIVO"));
					register.put("NAME", register.get("NM_ARQUIVO"));
					attachments[i] = register;
				}
				String[] to = {emailDestinatario};
				cliente.send(ParametroServices.getValorOfParametroAsString("NM_EMAIL_REMETENTE_SMTP", ""), new String[] {ParametroServices.getValorOfParametroAsString("NM_EMAIL_REMETENTE_SMTP", "")}, to, null, null, 
						assunto, texto, null, null, attachments);
			}
			else{
				return new Result(-1, "Erro de conexão!");
			}
			
			cliente.disconnect();
			
			return new Result(1, "Email enviado com sucesso!");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalServices.enviarEmail: " + e);
			return new Result(-1, "Erro ao enviar o email!");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static boolean isCean(String ean){
		char[] car = ean.trim().toCharArray();
		String[] caract = new String[ean.length()];
		for(int i = 0; i < caract.length; i++)
			caract[i] = String.valueOf(car[i]);
		int soma = 0;
		for(int i = 1; i < caract.length - 1; i+=2){
			int num = Integer.parseInt(caract[i]) * 3;
			soma += num;
		}
		for(int i = 0; i < caract.length - 1; i+=2){
			int num = Integer.parseInt(String.valueOf(caract[i]));
			soma += num;
		}
		int mod = soma % 10;
		int div = soma / 10;
		
		if(mod > 0)
			div++;
		
		int resp = div * 10;
		return (resp - soma) == Integer.parseInt(String.valueOf(caract[caract.length - 1]));
	}

	public static ArrayList<DocumentoSaida> getAllDocSaidaReferenciado(int cdNotaFiscal) {
		NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal);
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("CD_NOTA_FISCAL", Integer.toString(nota.getCdNotaFiscal()), ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsmNotaDocVinc = NotaFiscalDocVinculadoDAO.find(criterios);
		ArrayList<DocumentoSaida> docSaidaArray = new ArrayList<DocumentoSaida>();
		while(rsmNotaDocVinc.next()){
			DocumentoSaida docSaida = DocumentoSaidaDAO.get(rsmNotaDocVinc.getInt("cd_documento_saida"));
			docSaidaArray.add(docSaida);
		}
		return docSaidaArray;
	}
	
	public static ArrayList<DocumentoEntrada> getAllDocEntradaReferenciado(int cdNotaFiscal) {
		NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal);
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("CD_NOTA_FISCAL", Integer.toString(nota.getCdNotaFiscal()), ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsmNotaDocVinc = NotaFiscalDocVinculadoDAO.find(criterios);
		ArrayList<DocumentoEntrada> docEntradaArray = new ArrayList<DocumentoEntrada>();
		while(rsmNotaDocVinc.next()){
			DocumentoEntrada docEntrada = DocumentoEntradaDAO.get(rsmNotaDocVinc.getInt("cd_documento_entrada"));
			docEntradaArray.add(docEntrada);
		}
		return docEntradaArray;
	}
	
	
	public Result gerarRelatorioMapaFiscal(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int hasCuponsFiscais, int hasNotasSaida, int hasNotasEntrada)
	{
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			if(dtInicial != null){
				dtInicial.set(Calendar.HOUR, 0);
				dtInicial.set(Calendar.MINUTE, 0);
				dtInicial.set(Calendar.SECOND, 0);
			}
			if(dtFinal != null){
				dtFinal.set(Calendar.HOUR, 23);
				dtFinal.set(Calendar.MINUTE, 59);
				dtFinal.set(Calendar.SECOND, 59);
			}
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			PreparedStatement pstmtSaida   = null;
			// PreparedStatement pstmtEntrada = null;
			
			ResultSetMap rsmSaidas = new ResultSetMap();
			ResultSetMap rsmEntradas = new ResultSetMap();
			
			HashMap<String, Object> param = new HashMap<String, Object>();
			
			
			if(hasNotasSaida == 1 || hasCuponsFiscais == 1){
			
				
				pstmtSaida = connection.prepareStatement("SELECT * FROM alm_documento_saida A " +
				        "									JOIN adm_natureza_operacao B ON (A.cd_natureza_operacao = B.cd_natureza_operacao) " +
						"									WHERE A.st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO + 
				        "										AND A.dt_documento_saida >= ? " +
						"										AND A.dt_documento_saida <= ?" +
						"										AND A.tp_documento_saida <> " + DocumentoSaidaServices.TP_DOC_NAO_FISCAL 
						+ 			 (hasCuponsFiscais != 1 ? " AND A.tp_documento_saida <> " + DocumentoSaidaServices.TP_CUPOM_FISCAL : "")
						+ 			 (hasNotasSaida != 1 ? "    AND A.tp_documento_saida = " + DocumentoSaidaServices.TP_CUPOM_FISCAL : "") +
						"										AND EXISTS (SELECT C.cd_documento_saida FROM alm_documento_saida C " +
						"														JOIN adm_saida_item_aliquota D ON (C.cd_documento_saida = D.cd_documento_saida) " +
						"													WHERE A.cd_documento_saida = C.cd_documento_saida)" +										
						"									ORDER BY A.dt_documento_saida, A.tp_documento_saida, nr_codigo_fiscal");
				pstmtSaida.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
				pstmtSaida.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
					
				rsmSaidas = new ResultSetMap(pstmtSaida.executeQuery());
				
			}
			
			if(hasNotasEntrada == 1 || hasCuponsFiscais == 1){
				
//				pstmtEntrada = connection.prepareStatement("SELECT * FROM alm_documento_entrada A " +
//				        "									JOIN adm_natureza_operacao B ON (A.cd_natureza_operacao = B.cd_natureza_operacao) " +
//						"									WHERE A.st_documento_entrada = " + DocumentoEntradaServices.ST_LIBERADO+
//						"										AND A.dt_documento_entrada >= ? " +
//						"										AND A.dt_documento_entrada <= ?" +
//						"										AND A.tp_documento_entrada <> " + DocumentoEntradaServices.TP_DOC_NAO_FISCAL 
//						+ 			 (hasCuponsFiscais != 1 ? " AND A.tp_documento_entrada <> " + DocumentoEntradaServices.TP_CUPOM_FISCAL : "") 
//						+ 			 (hasNotasEntrada != 1 ? "  AND A.tp_documento_entrada = " + DocumentoEntradaServices.TP_CUPOM_FISCAL : "") +
//						"										AND EXISTS (SELECT C.cd_documento_entrada FROM alm_documento_entrada C " +
//						"														JOIN adm_entrada_item_aliquota D ON (C.cd_documento_entrada = D.cd_documento_entrada) " +
//						"													WHERE A.cd_documento_entrada = C.cd_documento_entrada)" +
//						"									ORDER BY A.dt_documento_entrada, A.tp_documento_entrada, nr_codigo_fiscal");
//				pstmtEntrada.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
//				pstmtEntrada.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
//			
//				rsmEntradas = new ResultSetMap(pstmtEntrada.executeQuery());
			}
			
			
			HashMap<String, Object> registro = new HashMap<String, Object>();
			GregorianCalendar dataAtual = null;
			int tpDocumentoSaidaAtual = -1;
			String nrCodigoFiscalAtual = null;
			
			ResultSetMap rsmPrincipal = new ResultSetMap();
			
			double vlGtAnterior = 0;
			
			while(rsmSaidas.next()){
				//Para a primeira passagem
				if(rsmSaidas.getPosition() == 0){
					dataAtual = rsmSaidas.getGregorianCalendar("dt_documento_saida");
					tpDocumentoSaidaAtual = rsmSaidas.getInt("tp_documento_saida");
					nrCodigoFiscalAtual = rsmSaidas.getString("nr_codigo_fiscal");
					registro.put("PRIMEIRO_NUMERO", rsmSaidas.getString("nr_documento_saida"));
					registro.put("VL_COMBUSTIVEL", (float)0);
					registro.put("VL_LUBRIFICANTE", (float)0);
					registro.put("VL_OUTROS", (float)0);
					registro.put("VL_VENDAS_LIQUIDAS", (float)0);
					registro.put("VL_SUBSTITUICAO_TRIBUTARIA",(float)0);
					registro.put("VL_7", (float)0);
					registro.put("VL_17", (float)0);
					registro.put("VL_27", (float)0);
					registro.put("VL_CANCELAMENTO", (float)0);
					registro.put("VL_DESCONTO", (float)0);
					registro.put("VL_ACRESCIMO", (float)0);
					registro.put("VL_ISENTO", (float)0);
					registro.put("VL_NAO_TRIBUTADO", (float)0);
					registro.put("VL_GT_FINAL", (double)0);
					registro.put("VL_GT_INICIAL", (float)0);
					registro.put("VL_VENDA_BRUTA", (float)0);
					registro.put("DATA", Util.formatDate(rsmSaidas.getGregorianCalendar("dt_documento_saida"), "dd/MM/yyyy"));
					registro.put("ESPECIE", DocumentoSaidaServices.tiposDocumentoSaida2[rsmSaidas.getInt("tp_documento_saida")] + (rsmSaidas.getInt("tp_documento_saida") != DocumentoSaidaServices.TP_CUPOM_FISCAL ? "-" + rsmSaidas.getString("nr_codigo_fiscal") : ""));
					
				}
				//Caso os tres sejam iguais havera o somatorio dos atributos no registro
				if((nrCodigoFiscalAtual.equals(rsmSaidas.getString("nr_codigo_fiscal")) || rsmSaidas.getInt("tp_documento_saida") == DocumentoSaidaServices.TP_CUPOM_FISCAL) &&
					tpDocumentoSaidaAtual == rsmSaidas.getInt("tp_documento_saida") &&
					Util.compareDates(dataAtual, rsmSaidas.getGregorianCalendar("dt_documento_saida"))==0)
				{
					incluirNoRegistro("saida", rsmSaidas.getInt("cd_documento_saida"), rsmSaidas.getString("nr_documento_saida"), cdEmpresa, registro, dtInicial, dtFinal, connection);
					if((rsmSaidas.getPosition() == (rsmSaidas.getLines().size()-1))){
						//Antes de mudar o registro, coloca a faixa de numeros que o registro anterior armazenou dados
						registro.put("FAIXA_NUMERO", registro.get("PRIMEIRO_NUMERO") + " - " + registro.get("ULTIMO_NUMERO"));
						//Tira a Reducao Z
						double vlGtAnteriorTemp = getValorReducaoZ(dataAtual, registro, param, vlGtAnterior, connection);
						if(vlGtAnteriorTemp > 0)
							vlGtAnterior = vlGtAnteriorTemp;
						//e coloca a soma de combustivel com lubrificante
						registro.put("VL_COMBUSTIVEL_LUBRIFICANTE", ((Float)registro.get("VL_COMBUSTIVEL") + (Float)registro.get("VL_LUBRIFICANTE")));
//						if(rsmSaidas.getInt("tp_documento_saida") == DocumentoSaidaServices.TP_CUPOM_FISCAL)
//							registro.put("VL_OUTROS", (Float)registro.get("VL_SUBSTITUICAO_TRIBUTARIA") - (Float)registro.get("VL_COMBUSTIVEL_LUBRIFICANTE"));
						
						//e o subtotal (Combustivel + Lubrificante + Outros)
						registro.put("VL_SUBTOTAL", ((Float)registro.get("VL_COMBUSTIVEL_LUBRIFICANTE") + (Float)registro.get("VL_OUTROS")));
						registro.put("VL_00_3", (float)0);
						//O que foi acumulado eh colocado no rsm Principal como registro 
						rsmPrincipal.addRegister(registro);
					}
				}
				//Se qualquer um deles for diferente, entao outro registro sera criado
				else{
					//Antes de mudar o registro, coloca a faixa de numeros que o registro anterior armazenou dados
					registro.put("FAIXA_NUMERO", registro.get("PRIMEIRO_NUMERO") + " - " + registro.get("ULTIMO_NUMERO"));
					//Tira a Reducao Z
					double vlGtAnteriorTemp = getValorReducaoZ(dataAtual, registro, param, vlGtAnterior, connection);
					if(vlGtAnteriorTemp > 0)
						vlGtAnterior = vlGtAnteriorTemp;
					if(rsmPrincipal.size() == 0){
						registro.put("VL_GT_INICIAL", (Double)registro.get("VL_GT_FINAL") - (Float)registro.get("VL_VENDA_BRUTA"));
					}
					//e coloca a soma de combustivel com lubrificante
					registro.put("VL_COMBUSTIVEL_LUBRIFICANTE", ((Float)registro.get("VL_COMBUSTIVEL") + (Float)registro.get("VL_LUBRIFICANTE")));
//					if(rsmSaidas.getInt("tp_documento_saida") == DocumentoSaidaServices.TP_CUPOM_FISCAL)
//						registro.put("VL_OUTROS", (Float)registro.get("VL_SUBSTITUICAO_TRIBUTARIA") - (Float)registro.get("VL_COMBUSTIVEL_LUBRIFICANTE"));
					//e o subtotal (Combustivel + Lubrificante + Outros)
					registro.put("VL_SUBTOTAL", ((Float)registro.get("VL_COMBUSTIVEL_LUBRIFICANTE") + (Float)registro.get("VL_OUTROS")));
					//ICMS 00_3
					registro.put("VL_00_3", (float)0);
					//O que foi acumulado eh colocado no rsm Principal como registro 
					rsmPrincipal.addRegister(registro);
					
					//Eh criado um novo registro para armazenar as novas informaceos
					registro = new HashMap<String, Object>();
					//Pega o primeiro numero da faixa
					registro.put("PRIMEIRO_NUMERO", rsmSaidas.getString("nr_documento_saida"));
					registro.put("VL_COMBUSTIVEL", (float)0);
					registro.put("VL_LUBRIFICANTE", (float)0);
					registro.put("VL_OUTROS", (float)0);
					registro.put("VL_VENDAS_LIQUIDAS", (float)0);
					registro.put("VL_SUBSTITUICAO_TRIBUTARIA",(float)0);
					registro.put("VL_7", (float)0);
					registro.put("VL_17", (float)0);
					registro.put("VL_27", (float)0);
					registro.put("VL_CANCELAMENTO", (float)0);
					registro.put("VL_DESCONTO", (float)0);
					registro.put("VL_ACRESCIMO", (float)0);
					registro.put("VL_ISENTO", (float)0);
					registro.put("VL_NAO_TRIBUTADO", (float)0);
					registro.put("VL_GT_FINAL", (double)0);
					registro.put("VL_GT_INICIAL", (float)0);
					registro.put("VL_VENDA_BRUTA", (float)0);
					registro.put("DATA", Util.formatDate(rsmSaidas.getGregorianCalendar("dt_documento_saida"), "dd/MM/yyyy"));
					registro.put("ESPECIE", DocumentoSaidaServices.tiposDocumentoSaida2[rsmSaidas.getInt("tp_documento_saida")] + (rsmSaidas.getInt("tp_documento_saida") != DocumentoSaidaServices.TP_CUPOM_FISCAL ? "-" + rsmSaidas.getString("nr_codigo_fiscal") : ""));
					
					//Este registro que nao foi agrupado com o anterior coloca seus parametros
					dataAtual = rsmSaidas.getGregorianCalendar("dt_documento_saida");
					tpDocumentoSaidaAtual = rsmSaidas.getInt("tp_documento_saida");
					nrCodigoFiscalAtual = rsmSaidas.getString("nr_codigo_fiscal");
					
					//E entao inclui suas informacoes no registro
					incluirNoRegistro("saida", rsmSaidas.getInt("cd_documento_saida"), rsmSaidas.getString("nr_documento_saida"), cdEmpresa, registro, dtInicial, dtFinal, connection);
					if((rsmSaidas.getPosition() == (rsmSaidas.getLines().size()-1))){
						//Antes de mudar o registro, coloca a faixa de numeros que o registro anterior armazenou dados
						registro.put("FAIXA_NUMERO", registro.get("PRIMEIRO_NUMERO") + " - " + registro.get("ULTIMO_NUMERO"));
						//Tira a Reducao Z
						vlGtAnteriorTemp = getValorReducaoZ(dataAtual, registro, param, vlGtAnterior, connection);
						if(vlGtAnteriorTemp > 0)
							vlGtAnterior = vlGtAnteriorTemp;
						//e coloca a soma de combustivel com lubrificante
						registro.put("VL_COMBUSTIVEL_LUBRIFICANTE", ((Float)registro.get("VL_COMBUSTIVEL") + (Float)registro.get("VL_LUBRIFICANTE")));
//						if(rsmSaidas.getInt("tp_documento_saida") == DocumentoSaidaServices.TP_CUPOM_FISCAL)
//							registro.put("VL_OUTROS", (Float)registro.get("VL_SUBSTITUICAO_TRIBUTARIA") - (Float)registro.get("VL_COMBUSTIVEL_LUBRIFICANTE"));
						//e o subtotal (Combustivel + Lubrificante + Outros)
						registro.put("VL_SUBTOTAL", ((Float)registro.get("VL_COMBUSTIVEL_LUBRIFICANTE") + (Float)registro.get("VL_OUTROS")));
						//ICMS 00_3
						registro.put("VL_00_3", (float)0);
						//O que foi acumulado eh colocado no rsm Principal como registro
						rsmPrincipal.addRegister(registro);
					}
				}
					
			}
			dataAtual = null;
			int tpDocumentoEntradaAtual = -1;
			nrCodigoFiscalAtual = null;
			registro = new HashMap<String, Object>();
			while(rsmEntradas.next()){
				//Para a primeira passagem
				if(rsmEntradas.getPosition() == 0){
					dataAtual = rsmEntradas.getGregorianCalendar("dt_documento_entrada");
					tpDocumentoEntradaAtual = rsmEntradas.getInt("tp_documento_entrada");
					nrCodigoFiscalAtual = rsmEntradas.getString("nr_codigo_fiscal");
					registro.put("PRIMEIRO_NUMERO", rsmEntradas.getString("nr_documento_entrada"));
					registro.put("VL_COMBUSTIVEL", (float)0);
					registro.put("VL_LUBRIFICANTE", (float)0);
					registro.put("VL_OUTROS", (float)0);
					registro.put("VL_VENDAS_LIQUIDAS", (float)0);
					registro.put("VL_SUBSTITUICAO_TRIBUTARIA",(float)0);
					registro.put("VL_7", (float)0);
					registro.put("VL_17", (float)0);
					registro.put("VL_27", (float)0);
					registro.put("VL_CANCELAMENTO", (float)0);
					registro.put("VL_DESCONTO", (float)0);
					registro.put("VL_ACRESCIMO", (float)0);
					registro.put("VL_ISENTO", (float)0);
					registro.put("VL_NAO_TRIBUTADO", (float)0);
					registro.put("VL_GT_FINAL", (double)0);
					registro.put("VL_GT_INICIAL", (float)0);
					registro.put("VL_VENDA_BRUTA", (float)0);
					registro.put("DATA", Util.formatDate(rsmEntradas.getGregorianCalendar("dt_documento_entrada"), "dd/MM/yyyy"));
					registro.put("ESPECIE", DocumentoEntradaServices.tiposDocumentoEntrada[rsmEntradas.getInt("tp_documento_entrada")] + (rsmEntradas.getInt("tp_documento_entrada") != DocumentoEntradaServices.TP_CUPOM_FISCAL ? "-" + rsmEntradas.getString("nr_codigo_fiscal") : ""));
				}
				//Caso os tres sejam iguais havera o somatorio dos atributos no registro
				if((nrCodigoFiscalAtual.equals(rsmEntradas.getString("nr_codigo_fiscal")) || rsmEntradas.getInt("tp_documento_entrada") == DocumentoEntradaServices.TP_CUPOM_FISCAL) &&
					tpDocumentoEntradaAtual == rsmEntradas.getInt("tp_documento_entrada") &&
					Util.compareDates(dataAtual, rsmEntradas.getGregorianCalendar("dt_documento_entrada"))==0){
					incluirNoRegistro("entrada", rsmEntradas.getInt("cd_documento_entrada"), rsmEntradas.getString("nr_documento_entrada"), cdEmpresa, registro, dtInicial, dtFinal, connection);
					if((rsmEntradas.getPosition() == (rsmEntradas.getLines().size()-1))){
						//Antes de mudar o registro, coloca a faixa de numeros que o registro anterior armazenou dados
						registro.put("FAIXA_NUMERO", registro.get("PRIMEIRO_NUMERO") + " - " + registro.get("ULTIMO_NUMERO"));
						//e coloca a soma de combustivel com lubrificante
						registro.put("VL_COMBUSTIVEL_LUBRIFICANTE", ((Float)registro.get("VL_COMBUSTIVEL") + (Float)registro.get("VL_LUBRIFICANTE")));
						//e o subtotal (Combustivel + Lubrificante + Outros)
						registro.put("VL_SUBTOTAL", ((Float)registro.get("VL_COMBUSTIVEL_LUBRIFICANTE") + (Float)registro.get("VL_OUTROS")));
						//e a soma de ICMS (17)
						registro.put("VL_SOMA_ICMS", ((Float)registro.get("VL_17")));
						//ICMS 00_1
						registro.put("VL_00_1", (float)0);
						//ICMS 00_2
						registro.put("VL_00_2", (float)0);
						//ICMS 00_3
						registro.put("VL_00_3", (float)0);
						//ICMS Debito (VL_17 * 0,17)
						registro.put("VL_ICMS_DEBITO", ((Float)registro.get("VL_17") * (float)0.17));
						//Total (SubTotal + Soma ICMS)
						registro.put("VL_TOTAL", ((Float)registro.get("VL_SUBTOTAL") + (Float)registro.get("VL_ICMS_DEBITO")));
						//Venda Liquidada Red. Z
						registro.put("VL_VENDAS_LIQUIDAS", (float)0);
						//Diferencial
						registro.put("VL_DIFERENCIAL", ((Float)registro.get("VL_TOTAL") - (Float)registro.get("VL_VENDAS_LIQUIDAS")));
						//Total (SubTotal + Soma ICMS)
						registro.put("VL_SERVICO",(float) 0);
						//O que foi acumulado eh colocado no rsm Principal como registro
						rsmPrincipal.addRegister(registro);
					}
				}
				//Se qualquer um deles for diferente, entao outro registro sera criado
				else{
					//Antes de mudar o registro, coloca a faixa de numeros que o registro anterior armazenou dados
					registro.put("FAIXA_NUMERO", registro.get("PRIMEIRO_NUMERO") + " - " + registro.get("ULTIMO_NUMERO"));
					//e coloca a soma de combustivel com lubrificante
					registro.put("VL_COMBUSTIVEL_LUBRIFICANTE", ((Float)registro.get("VL_COMBUSTIVEL") + (Float)registro.get("VL_LUBRIFICANTE")));
					//e o subtotal (Combustivel + Lubrificante + Outros)
					registro.put("VL_SUBTOTAL", ((Float)registro.get("VL_COMBUSTIVEL_LUBRIFICANTE") + (Float)registro.get("VL_OUTROS")));
					//e a soma de ICMS (17)
					registro.put("VL_SOMA_ICMS", ((Float)registro.get("VL_17")));
					//ICMS 00_1
					registro.put("VL_00_1", (float)0);
					//ICMS 00_2
					registro.put("VL_00_2", (float)0);
					//ICMS 00_3
					registro.put("VL_00_3", (float)0);
					//ICMS Debito (VL_17 * 0,17)
					registro.put("VL_ICMS_DEBITO", ((Float)registro.get("VL_17") * (float)0.17));
					//Total (SubTotal + Soma ICMS)
					registro.put("VL_TOTAL", ((Float)registro.get("VL_SUBTOTAL") + (Float)registro.get("VL_ICMS_DEBITO")));
					//Venda Liquidada Red. Z
					registro.put("VL_VENDAS_LIQUIDAS", (float)0);
					//Diferencial
					registro.put("VL_DIFERENCIAL", ((Float)registro.get("VL_TOTAL") - (Float)registro.get("VL_VENDAS_LIQUIDAS")));
					//Total (SubTotal + Soma ICMS)
					registro.put("VL_SERVICO", (float)0);
					//O que foi acumulado eh colocado no rsm Principal como registro
					rsmPrincipal.addRegister(registro);
					
					//Eh criado um novo registro para armazenar as novas informaceos
					registro = new HashMap<String, Object>();
					//Pega o primeiro numero da faixa
					registro.put("PRIMEIRO_NUMERO", rsmEntradas.getString("nr_documento_entrada"));
					registro.put("VL_COMBUSTIVEL", (float)0);
					registro.put("VL_LUBRIFICANTE", (float)0);
					registro.put("VL_OUTROS", (float)0);
					registro.put("VL_VENDAS_LIQUIDAS", (float)0);
					registro.put("VL_SUBSTITUICAO_TRIBUTARIA",(float)0);
					registro.put("VL_7", (float)0);
					registro.put("VL_17", (float)0);
					registro.put("VL_27", (float)0);
					registro.put("VL_CANCELAMENTO", (float)0);
					registro.put("VL_DESCONTO", (float)0);
					registro.put("VL_ACRESCIMO", (float)0);
					registro.put("VL_ISENTO", (float)0);
					registro.put("VL_NAO_TRIBUTADO", (float)0);
					registro.put("VL_GT_FINAL", (double)0);
					registro.put("VL_GT_INICIAL", (float)0);
					registro.put("VL_VENDA_BRUTA", (float)0);
					registro.put("DATA", Util.formatDate(rsmEntradas.getGregorianCalendar("dt_documento_entrada"), "dd/MM/yyyy"));
					registro.put("ESPECIE", DocumentoEntradaServices.tiposDocumentoEntrada[rsmEntradas.getInt("tp_documento_entrada")] + (rsmEntradas.getInt("tp_documento_entrada") != DocumentoEntradaServices.TP_CUPOM_FISCAL ? "-" + rsmEntradas.getString("nr_codigo_fiscal") : ""));
					
					//Este registro que nao foi agrupado com o anterior coloca seus parametros
					dataAtual = rsmEntradas.getGregorianCalendar("dt_documento_entrada");
					tpDocumentoEntradaAtual = rsmEntradas.getInt("tp_documento_entrada");
					nrCodigoFiscalAtual = rsmEntradas.getString("nr_codigo_fiscal");
					
					//E entao inclui suas informacoes no registro
					incluirNoRegistro("entrada", rsmEntradas.getInt("cd_documento_entrada"), rsmEntradas.getString("nr_documento_entrada"), cdEmpresa, registro, dtInicial, dtFinal, connection);
					if((rsmEntradas.getPosition() == (rsmEntradas.getLines().size()-1))){
						//Antes de mudar o registro, coloca a faixa de numeros que o registro anterior armazenou dados
						registro.put("FAIXA_NUMERO", registro.get("PRIMEIRO_NUMERO") + " - " + registro.get("ULTIMO_NUMERO"));
						//e coloca a soma de combustivel com lubrificante
						registro.put("VL_COMBUSTIVEL_LUBRIFICANTE", ((Float)registro.get("VL_COMBUSTIVEL") + (Float)registro.get("VL_LUBRIFICANTE")));
						//e o subtotal (Combustivel + Lubrificante + Outros)
						registro.put("VL_SUBTOTAL", ((Float)registro.get("VL_COMBUSTIVEL_LUBRIFICANTE") + (Float)registro.get("VL_OUTROS")));
						//e a soma de ICMS (17)
						registro.put("VL_SOMA_ICMS", ((Float)registro.get("VL_17")));
						//ICMS 00_1
						registro.put("VL_00_1", (float)0);
						//ICMS 00_2
						registro.put("VL_00_2", (float)0);
						//ICMS 00_3
						registro.put("VL_00_3", (float)0);
						//ICMS Debito (VL_17 * 0,17)
						registro.put("VL_ICMS_DEBITO", ((Float)registro.get("VL_17") * (float)0.17));
						//Total (SubTotal + Soma ICMS)
						registro.put("VL_TOTAL", ((Float)registro.get("VL_SUBTOTAL") + (Float)registro.get("VL_ICMS_DEBITO")));
						//Venda Liquidada Red. Z
						registro.put("VL_VENDAS_LIQUIDAS", (float)0);
						//Diferencial
						registro.put("VL_DIFERENCIAL", ((Float)registro.get("VL_TOTAL") - (Float)registro.get("VL_VENDAS_LIQUIDAS")));
						//Total (SubTotal + Soma ICMS)
						registro.put("VL_SERVICO",(float) 0);
						//O que foi acumulado eh colocado no rsm Principal como registro
						rsmPrincipal.addRegister(registro);
					}
				}
					
			}
			if(dtInicial != null)
				param.put("dtInicial", Util.convCalendarString(dtInicial));
			if(dtFinal != null)
				param.put("dtFinal", Util.convCalendarString(dtFinal));
			PessoaJuridica empresaJuridica = PessoaJuridicaDAO.get(cdEmpresa);
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_pessoa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("lg_principal", "1", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmEmpresaEndereco = PessoaEnderecoDAO.find(criterios);
			PessoaEndereco empresaEndereco = null;
			if(rsmEmpresaEndereco.next()){
				empresaEndereco = PessoaEnderecoDAO.get(rsmEmpresaEndereco.getInt("cd_endereco"), rsmEmpresaEndereco.getInt("cd_pessoa"));
			}
			Cidade empresaCidade = CidadeDAO.get(empresaEndereco.getCdCidade());
			Estado empresaEstado = EstadoDAO.get(empresaCidade.getCdEstado());
			
			param.put("nmEndereco", empresaEndereco.getNmLogradouro() + " - " + empresaEndereco.getNmBairro());
			param.put("nmCidadeCliente", empresaCidade.getNmCidade());
			param.put("sgUf", empresaEstado.getSgEstado());
			param.put("nrCnpj", empresaJuridica.getNrCnpj());
			param.put("nrIe", empresaJuridica.getNrInscricaoEstadual());
			
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsmPrincipal);
			result.addObject("params", param);
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static double getValorReducaoZ(GregorianCalendar dtReducao, HashMap<String, Object> registro, HashMap<String, Object> param, double vlGtAnterior, Connection connection){
		try {
			String nrCrz = "";
			double vlTotalGtInicial = 0;
			double vlTotalGtFinal = 0;
			float vlTotalVendaBruta = 0;
			float vlTotalVendasLiquidas = 0;
			float vlTotalSubstituicaoTributaria = 0;
			float vlTotal7 = 0;
			float vlTotal17 = 0;
			float vlTotal27 = 0;
			float vlTotalCancelamento = 0;
			float vlTotalDesconto = 0;
			float vlTotalAcrescimo = 0;
			float vlTotalIsento = 0;
			float vlTotalNaoTributado = 0;
			
			PreparedStatement pstmtEcf = connection.prepareStatement("SELECT * FROM fsc_registro_ecf " +
									 "WHERE CAST(dt_registro_ecf AS DATE) = ? " +
									 "  AND (tp_registro_ecf LIKE \'C405\' OR tp_registro_ecf LIKE \'C400\')");
			PreparedStatement pstmtEcfC420 = connection.prepareStatement("SELECT * FROM fsc_registro_ecf " +
													"WHERE cd_registro_ecf = ?");
			ResultSetMap rsC420 = new ResultSetMap();
			pstmtEcf.setTimestamp(1, new Timestamp(dtReducao.getTimeInMillis()));
			ResultSetMap rs = new ResultSetMap(pstmtEcf.executeQuery());
			if(rs.next()){
				int cdRegistroEcf = rs.getInt("cd_registro_ecf");
				String[] setC405 = rs.getString("txt_registro_ecf").split("\\|");
				
				int posCrz = (rs.getString("tp_registro_ecf").equals("C400") ? 10 : 4);
				nrCrz = (setC405[posCrz].replaceAll(",", "."));
				registro.put("CRZ", nrCrz);
				
				vlTotalGtInicial = vlGtAnterior;
				registro.put("VL_GT_INICIAL", vlTotalGtInicial);					
				
				int posVlGtFinal = (rs.getString("tp_registro_ecf").equals("C400") ? 12 : 6);
				vlTotalGtFinal = new Double(setC405[posVlGtFinal].replaceAll(",", "."));
				registro.put("VL_GT_FINAL", vlTotalGtFinal);					
				
				int posVlVendaBruta = (rs.getString("tp_registro_ecf").equals("C400") ? 13 : 7);
				vlTotalVendaBruta = Float.parseFloat(setC405[posVlVendaBruta].replaceAll(",", "."));
				registro.put("VL_VENDA_BRUTA", vlTotalVendaBruta);
				vlTotalVendasLiquidas = vlTotalVendaBruta;
				
				if(rs.getString("tp_registro_ecf").equals("C400")){
					param.put("nrFab", setC405[4]);
				}
				
				while(true){
					pstmtEcfC420.setInt(1, ++cdRegistroEcf);
					rsC420 = new ResultSetMap(pstmtEcfC420.executeQuery());
					if(rsC420.next() && (rsC420.getString("tp_registro_ecf").equals("C420") || rsC420.getString("tp_registro_ecf").equals("C425"))){
						if(rsC420.getString("tp_registro_ecf").equals("C420")){
							String reg = rsC420.getString("txt_registro_ecf");
							String[] set = reg.split("\\|");
							if(set[2].matches("DT") ||
							   set[2].matches("DS") ||
							   set[2].matches("DO") ||
							   set[2].matches("Can-T") ||
							   set[2].matches("Can-S") ||
							   set[2].matches("Can-O"))
								vlTotalVendasLiquidas -= Float.parseFloat(set[3].replaceAll(",", "."));
							if(set[2].matches("F\\d"))
								vlTotalSubstituicaoTributaria += Float.parseFloat(set[3].replaceAll(",", "."));
							if(set[2].matches("T0700") ||
							   set[2].matches("\\d\\dT0700") ||
							   set[2].matches("S0700") ||
							   set[2].matches("\\d\\dS0700"))
								vlTotal7 += Float.parseFloat(set[3].replaceAll(",", "."));
							if(set[2].matches("T1700") ||
							   set[2].matches("\\d\\dT1700") ||
							   set[2].matches("S1700") ||
							   set[2].matches("\\d\\dS1700"))
								vlTotal17 += Float.parseFloat(set[3].replaceAll(",", "."));
							if(set[2].matches("T2700") ||
							   set[2].matches("\\d\\dT2700") ||
							   set[2].matches("S2700") ||
							   set[2].matches("\\d\\dS2700"))
								vlTotal27 += Float.parseFloat(set[3].replaceAll(",", "."));
							if(set[2].matches("Can-T") ||
							   set[2].matches("Can-S") ||
							   set[2].matches("Can-O"))
								vlTotalCancelamento += Float.parseFloat(set[3].replaceAll(",", "."));
							if(set[2].matches("DT") ||
							   set[2].matches("DS") ||
							   set[2].matches("DO"))
								vlTotalDesconto += Float.parseFloat(set[3].replaceAll(",", "."));
							if(set[2].matches("AT") ||
							   set[2].matches("AS") ||
							   set[2].matches("AO"))
								vlTotalAcrescimo += Float.parseFloat(set[3].replaceAll(",", "."));
							if(set[2].matches("I\\d"))
								vlTotalIsento += Float.parseFloat(set[3].replaceAll(",", "."));
							if(set[2].matches("N\\d"))
								vlTotalNaoTributado += Float.parseFloat(set[3].replaceAll(",", "."));
							
						}
					}
					else
						break;
				}
				
				registro.put("VL_VENDAS_LIQUIDAS", vlTotalVendasLiquidas);
				registro.put("VL_SUBSTITUICAO_TRIBUTARIA", vlTotalSubstituicaoTributaria);
				registro.put("VL_7", vlTotal7 * 0.07);
				registro.put("VL_17", vlTotal17 * 0.17);
				registro.put("VL_27", vlTotal27 * 0.27);
				registro.put("VL_CANCELAMENTO", vlTotalCancelamento);
				registro.put("VL_DESCONTO", vlTotalDesconto);
				registro.put("VL_ACRESCIMO", vlTotalAcrescimo);
				registro.put("VL_ISENTO", vlTotalIsento);
				registro.put("VL_NAO_TRIBUTADO", vlTotalNaoTributado);
			}
			return vlTotalGtFinal;
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
	}

	@SuppressWarnings("unchecked")
	public static void incluirNoRegistro(String tpMov, int cdDocumento, String nrDocumento, int cdEmpresa, HashMap<String, Object> registro, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connection){
		try {
							
			DocumentoSaida docSaida = null;
			DocumentoEntrada docEntrada = null;
			GregorianCalendar dtMovimento = null;
			boolean isCupomFiscal = false;
			ArrayList<Integer> codigosCombustivel = com.tivic.manager.alm.GrupoServices.getAllCombustivelAsArray(cdEmpresa, connection);
			ArrayList<Integer> codigosLubrificante = com.tivic.manager.alm.GrupoServices.getAllLubrificanteAsArray(cdEmpresa, connection);
			
			
			if(tpMov.equals("saida")){
				docSaida = DocumentoSaidaDAO.get(cdDocumento);
				dtMovimento = docSaida.getDtDocumentoSaida();
				isCupomFiscal = docSaida.getTpDocumentoSaida() == DocumentoSaidaServices.TP_CUPOM_FISCAL;
			}
			else{
				docEntrada = DocumentoEntradaDAO.get(cdDocumento);
				dtMovimento = docEntrada.getDtDocumentoEntrada();
				isCupomFiscal = docEntrada.getTpDocumentoEntrada() == DocumentoEntradaServices.TP_CUPOM_FISCAL;
			}
			
			
			if(!isCupomFiscal){
				PreparedStatement pstmtSaidaItens = connection.prepareStatement("SELECT A.*, I.cd_situacao_tributaria, G.pr_aliquota, G.vl_base_calculo, I.lg_substituicao, I.lg_motivo_isencao FROM alm_documento_"+tpMov+"_item A " +
						"															JOIN grl_produto_servico B ON (A.cd_produto_servico = B.cd_produto_servico) " +
						" 															LEFT OUTER JOIN adm_"+tpMov+"_item_aliquota  G ON (A.cd_produto_servico      	  = G.cd_produto_servico " +
						"                                            																AND A.cd_item                 = G.cd_item " +
						"                                            																AND A.cd_documento_"+tpMov+"      = G.cd_documento_"+tpMov+" " +
						"                                            																AND G.cd_empresa = "+cdEmpresa+")  " +
						"															LEFT OUTER JOIN adm_tributo_aliquota H ON (G.cd_tributo = H.cd_tributo " +
						"																								AND G.cd_tributo_aliquota = H.cd_tributo_aliquota) " +
						"															LEFT OUTER JOIN fsc_situacao_tributaria I ON (H.cd_situacao_tributaria = I.cd_situacao_tributaria" +
						"																								AND I.cd_tributo = H.cd_tributo) " +																	
						"															WHERE A.cd_documento_"+tpMov+" = ?");
				
				PreparedStatement pstmtSaidaItensGrupos = connection.prepareStatement("SELECT * FROM alm_produto_grupo A " +
							"															LEFT OUTER JOIN alm_grupo B ON (A.cd_grupo = B.cd_grupo)" +
							"															WHERE A.cd_produto_servico = ?" +
							"																AND A.cd_empresa = " + cdEmpresa + 
							"																AND A.lg_principal = 1");
				
				
				ResultSetMap rsmItens = new ResultSetMap();
				ResultSetMap rsmGrupos = new ResultSetMap();
				pstmtSaidaItens.setInt(1, cdDocumento);
				rsmItens = new ResultSetMap(pstmtSaidaItens.executeQuery());
				
				while(rsmItens.next()){
					//SUBSTITUICAO TRIBUTARIA
					if(rsmItens.getInt("lg_substituicao") == 1 || rsmItens.getInt("cd_situacao_tributaria") == 0){
						pstmtSaidaItensGrupos.setInt(1, rsmItens.getInt("cd_produto_servico"));
						
						rsmGrupos = new ResultSetMap(pstmtSaidaItensGrupos.executeQuery());
						while(rsmGrupos.next()){
							if(codigosCombustivel.contains(rsmGrupos.getInt("cd_grupo"))){
								registro.put("VL_COMBUSTIVEL", ((Float)registro.get("VL_COMBUSTIVEL") + (float)Util.arredondar(rsmItens.getFloat("vl_unitario") * (float)Util.arredondar(rsmItens.getFloat("qt_saida"), 3) - rsmItens.getFloat("vl_desconto") + rsmItens.getFloat("vl_acrescimo"), 2)));
								
							}
							else if(codigosLubrificante.contains(rsmGrupos.getInt("cd_grupo"))){
								registro.put("VL_LUBRIFICANTE", ((Float)registro.get("VL_LUBRIFICANTE") + (float)Util.arredondar(rsmItens.getFloat("vl_unitario") * (float)Util.arredondar(rsmItens.getFloat("qt_saida"), 3) - rsmItens.getFloat("vl_desconto") + rsmItens.getFloat("vl_acrescimo"), 2)));
							}
							else{
								registro.put("VL_OUTROS", ((Float)registro.get("VL_OUTROS") + (float)Util.arredondar(rsmItens.getFloat("vl_unitario") * (float)Util.arredondar(rsmItens.getFloat("qt_saida"), 3) - rsmItens.getFloat("vl_desconto") + rsmItens.getFloat("vl_acrescimo"), 2)));
							}
						}
					}
					//ISENTO E NAO TRIBUTADA
					else if(rsmItens.getInt("lg_motivo_isencao") == 1){
						registro.put("VL_ISENTO", ((Float)registro.get("VL_ISENTO") + (float)Util.arredondar(rsmItens.getFloat("vl_unitario") * (float)Util.arredondar(rsmItens.getFloat("qt_saida"), 3) - rsmItens.getFloat("vl_desconto") + rsmItens.getFloat("vl_acrescimo"), 2)));
					}
					//TRIBUTADO INTEGRALMENTE
					else{
						if(rsmItens.getFloat("pr_aliquota") == 7){
							registro.put("VL_7", ((Float)registro.get("VL_7") + ((float)Util.arredondar(rsmItens.getFloat("vl_unitario") * (float)Util.arredondar(rsmItens.getFloat("qt_saida"), 3) - rsmItens.getFloat("vl_desconto") + rsmItens.getFloat("vl_acrescimo"), 2) * 0.07)));
						}
						else if(rsmItens.getFloat("pr_aliquota") == 7){
							registro.put("VL_17", ((Float)registro.get("VL_17") + ((float)Util.arredondar(rsmItens.getFloat("vl_unitario") * (float)Util.arredondar(rsmItens.getFloat("qt_saida"), 3) - rsmItens.getFloat("vl_desconto") + rsmItens.getFloat("vl_acrescimo"), 2) * 0.17)));
						}
						else if(rsmItens.getFloat("pr_aliquota") == 27){
							registro.put("VL_27", ((Float)registro.get("VL_27") + ((float)Util.arredondar(rsmItens.getFloat("vl_unitario") * (float)Util.arredondar(rsmItens.getFloat("qt_saida"), 3) - rsmItens.getFloat("vl_desconto") + rsmItens.getFloat("vl_acrescimo"), 2) * 0.27)));
						}
					}
				}
				
				return;
			}
			
			
			
			
			PreparedStatement pstmtEcf = connection.prepareStatement("SELECT * FROM fsc_registro_ecf " +
																	"WHERE CAST (dt_registro_ecf AS DATE) = ? " +
																	"  AND tp_registro_ecf LIKE \'C460\' " +
																	"ORDER BY cd_registro_ecf");
			pstmtEcf.setTimestamp(1, Util.convCalendarToTimestamp(dtMovimento));
			ResultSetMap rsmEcf = new ResultSetMap();
			rsmEcf = new ResultSetMap(pstmtEcf.executeQuery());
			

			PreparedStatement pstmtEcfC470  = connection.prepareStatement("SELECT * FROM fsc_registro_ecf " +
																			"WHERE cd_registro_ecf = ?");
			
			PreparedStatement pstmtEcfItem1  = connection.prepareStatement("SELECT A.cd_produto_servico FROM grl_produto_servico A " +
																		  " JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico = B.cd_produto_servico " +
																		  "											AND B.cd_empresa = "+cdEmpresa+") " +
																		  " WHERE id_reduzido = ?");
			
			PreparedStatement pstmtEcfItem2  = connection.prepareStatement("SELECT cd_produto_servico FROM grl_produto_servico " +
																		  " WHERE id_produto_servico = ?");

			PreparedStatement pstmtEcfItem3  = connection.prepareStatement("SELECT cd_produto_servico FROM grl_produto_servico " +
																		   " WHERE cd_produto_servico = ?");

			
			PreparedStatement pstmtItens = connection.prepareStatement("SELECT A.*, I.cd_situacao_tributaria, G.pr_aliquota, G.vl_base_calculo, I.lg_substituicao, I.lg_motivo_isencao FROM alm_documento_"+tpMov+"_item A " +
					"															JOIN grl_produto_servico B ON (A.cd_produto_servico = B.cd_produto_servico) " +
					" 															LEFT OUTER JOIN adm_"+tpMov+"_item_aliquota  G ON (A.cd_produto_servico      	  = G.cd_produto_servico " +
					"                                            																AND A.cd_item                 = G.cd_item " +
					"                                            																AND A.cd_documento_"+tpMov+"      = G.cd_documento_"+tpMov+" " +
					"                                            																AND G.cd_empresa = "+cdEmpresa+")  " +
					"															LEFT OUTER JOIN adm_tributo_aliquota H ON (G.cd_tributo = H.cd_tributo " +
					"																								AND G.cd_tributo_aliquota = H.cd_tributo_aliquota) " +
					"															LEFT OUTER JOIN fsc_situacao_tributaria I ON (H.cd_situacao_tributaria = I.cd_situacao_tributaria" +
					"																								AND I.cd_tributo = H.cd_tributo) " +																	
					"															WHERE A.cd_documento_"+tpMov+" = ?" +
					"																	AND A.cd_produto_servico = ?" +
					"															ORDER BY A.cd_documento_"+tpMov+", A.cd_produto_servico, A.cd_item");
			
			PreparedStatement pstmtItensGrupos = connection.prepareStatement("SELECT * FROM alm_produto_grupo A " +
					"															LEFT OUTER JOIN alm_grupo B ON (A.cd_grupo = B.cd_grupo)" +
					"															WHERE A.cd_produto_servico = ?" +
					"																AND A.cd_empresa = " + cdEmpresa + 
					"																AND A.lg_principal = 1");
			
			
			ResultSetMap rsmProduto = new ResultSetMap();
			ResultSetMap rsmProduto1 = new ResultSetMap();
			ResultSetMap rsmProduto2 = new ResultSetMap();
			ResultSetMap rsmProduto3 = new ResultSetMap();
			ResultSetMap rsmItem = new ResultSetMap();
			ResultSetMap rsmGrupos = new ResultSetMap();
			
			while(rsmEcf.next()){
				String reg1 = rsmEcf.getString("txt_registro_ecf");
				String[] set1 = reg1.split("\\|");
				
				if(set1[4].equals(nrDocumento)){
					int cdRegistroEcf = rsmEcf.getInt("cd_registro_ecf");
					HashMap<String, Object> regItemDoc = new HashMap<String, Object>();
					while(true){
						pstmtEcfC470.setInt(1, ++cdRegistroEcf);
						ResultSetMap rsC470 = new ResultSetMap(pstmtEcfC470.executeQuery());
						if(rsC470.next() && (rsC470.getString("tp_registro_ecf").equals("C470"))){
							String reg = rsC470.getString("txt_registro_ecf");
							String[] set = reg.split("\\|");
							String codItem = set[2];
							pstmtEcfItem1.setString(1, codItem);
							pstmtEcfItem2.setString(1, codItem);
							pstmtEcfItem3.setInt(1, Integer.parseInt(codItem));
							rsmProduto1 = new ResultSetMap(pstmtEcfItem1.executeQuery());
							rsmProduto2 = new ResultSetMap(pstmtEcfItem2.executeQuery());
							rsmProduto3 = new ResultSetMap(pstmtEcfItem3.executeQuery());
							if(rsmProduto1.next()){
								rsmProduto1.beforeFirst();
								rsmProduto = rsmProduto1;
							}
							else if(rsmProduto2.next()){
								rsmProduto2.beforeFirst();
								rsmProduto = rsmProduto2;
							}
							else if(rsmProduto3.next()){
								rsmProduto3.beforeFirst();
								rsmProduto = rsmProduto3;
							}
							while(rsmProduto.next()){
								pstmtItens.setInt(1, cdDocumento); 
								pstmtItens.setInt(2, rsmProduto.getInt("cd_produto_servico"));
								rsmItem = new ResultSetMap(pstmtItens.executeQuery());
								while(rsmItem.next()){
									ArrayList<Integer> itens = new ArrayList<Integer>();
									itens = (ArrayList<Integer>)regItemDoc.get(cdDocumento + " - " + rsmProduto.getInt("cd_produto_servico"));
									if(itens == null)
										itens = new ArrayList<Integer>();
									
									boolean hasItem = false;
									for(int i = 0; i < itens.size(); i++){
										if(itens.get(i) == rsmItem.getInt("cd_item")){
											hasItem = true;
											break;
										}
									}
									
									if(hasItem){
										continue;
									}
									
									itens.add(rsmItem.getInt("cd_item"));
									regItemDoc.put(cdDocumento + " - " + rsmProduto.getInt("cd_produto_servico"), itens);
									
									if(rsmItem.getInt("lg_substituicao") == 1 || rsmItem.getInt("cd_situacao_tributaria") == 0){
										pstmtItensGrupos.setInt(1, rsmItem.getInt("cd_produto_servico"));
										rsmGrupos = new ResultSetMap(pstmtItensGrupos.executeQuery());
										while(rsmGrupos.next()){
											if(codigosCombustivel.contains(rsmGrupos.getInt("cd_grupo"))){
												registro.put("VL_COMBUSTIVEL", (Float)registro.get("VL_COMBUSTIVEL") + Float.parseFloat(set[6].replaceAll(",", ".")));
											}
						
											if(codigosLubrificante.contains(rsmGrupos.getInt("cd_grupo"))){
												registro.put("VL_LUBRIFICANTE", (Float)registro.get("VL_LUBRIFICANTE") + Float.parseFloat(set[6].replaceAll(",", ".")));
											}
											
											else{
												registro.put("VL_OUTROS", (Float)registro.get("VL_OUTROS") + Float.parseFloat(set[6].replaceAll(",", ".")));
											}
										}
									}
									break;
								}
								
							}
							
						}
						else
							break;
					
					}
					
				}
				
			}
				
			registro.put("ULTIMO_NUMERO", nrDocumento);
			registro.put("SERIE", "001");
		}
		
		catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	public static ResultSetMap findCompletoByViagem(int cdViagem, boolean isRemessa) {
		return findCompletoByViagem(cdViagem, isRemessa, null);
	}

	public static ResultSetMap findCompletoByViagem(int cdViagem, boolean isRemessa, Connection connection)
	{
		
		ResultSetMap rsm = Search.find(" SELECT A.cd_nota_fiscal " +
						   "	FROM fsc_nota_fiscal A " +
						   "	JOIN fsc_nota_fiscal_doc_vinculado   B ON (A.cd_nota_fiscal = B.cd_nota_fiscal) " +
						   "	JOIN alm_documento_saida             C ON (B.cd_documento_saida = C.cd_documento_saida) " +
						   "  WHERE 1 = 1 AND C.cd_viagem = " + cdViagem + " AND C.tp_documento_saida " + (isRemessa ? " = " :" <> ") + DocumentoSaidaServices.TP_NOTA_REMESSA + 
						   "  ORDER BY dt_autorizacao ", new ArrayList<ItemComparator>(), connection!=null ? connection : Conexao.conectar());
		
		
		ResultSetMap rsmFinal = new ResultSetMap();
		
		while(rsm.next()){
			ResultSetMap rsmNotaFiscal = findNotasFiscais(rsm.getInt("cd_nota_fiscal"));
			if(rsmNotaFiscal.next())
				rsmFinal.addRegister(rsmNotaFiscal.getRegister());
		}
		
		return rsmFinal;
		
	}
	
	public static ResultSetMap findCompletoByViagemRetorno(int cdViagem) {
		return findCompletoByViagemRetorno(cdViagem, null);
	}

	public static ResultSetMap findCompletoByViagemRetorno(int cdViagem, Connection connection)
	{
		
		ResultSetMap rsm = Search.find("  SELECT A.cd_nota_fiscal " +
									   "	FROM fsc_nota_fiscal A " +
									   "	JOIN fsc_nota_fiscal_doc_vinculado   B ON (A.cd_nota_fiscal = B.cd_nota_fiscal) " +
									   "	JOIN alm_documento_entrada             C ON (B.cd_documento_entrada = C.cd_documento_entrada) " +
									   "  WHERE 1 = 1 AND C.cd_viagem = " + cdViagem +  
									   "  ORDER BY dt_autorizacao ", new ArrayList<ItemComparator>(), connection!=null ? connection : Conexao.conectar());
		
		ResultSetMap rsmFinal = new ResultSetMap();
		
		while(rsm.next()){
			ResultSetMap rsmNotaFiscal = findNotasFiscais(rsm.getInt("cd_nota_fiscal"));
			if(rsmNotaFiscal.next())
				rsmFinal.addRegister(rsmNotaFiscal.getRegister());
		}
		
		return rsmFinal;
		
	}

	/**
	 * Metodo alternativo para buscar o numero da proxima nota fiscal, quando utilizado o parametro para gerar numero de nota fiscal na autorizacao
	 * @param nrSerie
	 * @return
	 */
	public static String getNrNotaFiscalAtual(String nrSerie){
		return getNrNotaFiscalAtual(nrSerie, null);
	}
	
	public static String getNrNotaFiscalAtual(String nrSerie, Connection connection){
		boolean isConnectionNull = true;
		try {
		
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
									
			
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT * FROM fsc_nota_fiscal " +
					"															WHERE ((st_nota_fiscal = " + ST_AUTORIZADA +
					"																		OR st_nota_fiscal = " + ST_CANCELADA + 
					"																		OR st_nota_fiscal = " + ST_DENEGADA + ")" + 
					"																		AND st_nota_fiscal <> " + ST_FORA_DO_SISTEMA + ")" +																			
					"															  AND nr_serie = '" + nrSerie + "'" +
					"															ORDER BY dt_emissao DESC LIMIT 1").executeQuery());
			if(rsm.next())
				return String.valueOf(Integer.parseInt(rsm.getString("nr_nota_fiscal")) + 1);
			else
				return "1";
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getByCodigo(int cdNotaFiscal) {
		return getByCodigo(cdNotaFiscal, null);
	}

	public static ResultSetMap getByCodigo(int cdNotaFiscal, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_nota_fiscal WHERE cd_nota_fiscal = " + cdNotaFiscal);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalDAO.getByCodigo: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalDAO.getByCodigo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	
 }
