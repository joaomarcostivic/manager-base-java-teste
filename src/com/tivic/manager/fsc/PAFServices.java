package com.tivic.manager.fsc;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.text.DecimalFormat;
import com.tivic.manager.bpm.ReferenciaServices;
import com.tivic.sol.connection.Conexao;
import sol.util.Result;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import com.tivic.manager.util.Util;
import com.tivic.manager.alm.DocumentoSaidaServices;
import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ProdutoServicoEmpresaServices;;

public class PAFServices {
	/*
	 * @category PAF-ECF
	 */
	public static ResultSetMap getRegistroA2(int cdEmpresa){
		GregorianCalendar dtInicial = Util.getDataAtual();
		dtInicial.set(2014, 0, 1);
		GregorianCalendar dtFinal   = Util.getDataAtual();
		dtFinal.set(2014, 0, 31);
		return getRegistroA2(cdEmpresa, dtInicial, dtFinal, null);
	}
	
	public static ResultSetMap getRegistroA2(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		return getRegistroA2(cdEmpresa, dtInicial, dtFinal, null);
	}
	/**
	 * 
	 * @param cdEmpresa      Código da Empresa 
	 * @param dtInicial      Data inicial solicitada na pesquisa
	 * @param dtFinal        Data final solicitada na pesquisa
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getRegistroA2(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect){
		connect = Conexao.conectar();
		// Inicial
		dtInicial.set(Calendar.HOUR, 0);
		dtInicial.set(Calendar.MINUTE, 0);
		dtInicial.set(Calendar.SECOND, 0);
		// Final
		dtFinal.set(Calendar.HOUR_OF_DAY, 23);
		dtFinal.set(Calendar.MINUTE, 59);
		dtFinal.set(Calendar.SECOND, 59);
		try	{
			PreparedStatement pstmt = connect.prepareStatement(
					  " SELECT C.nm_forma_pagamento, A.tp_documento_saida, CAST(A.dt_documento_saida AS DATE) AS dt_documento_saida, "+
					  "        B.vl_pagamento " +
 					  "   FROM alm_documento_saida A " +
 					  "   JOIN adm_plano_pagto_documento_saida B ON (A.cd_documento_saida = B.cd_documento_saida) " +
 					  "   JOIN adm_forma_pagamento C ON (B.cd_forma_pagamento = C.cd_forma_pagamento) " +
 					  "  WHERE A.st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO +
 					  "    AND A.tp_saida           = " + DocumentoSaidaServices.SAI_VENDA +
 					  "    AND A.cd_empresa = " + cdEmpresa +
 					  "    AND A.dt_documento_saida BETWEEN ? AND ? " +
 					  "    AND (A.tp_documento_saida = " + DocumentoSaidaServices.TP_CUPOM_FISCAL +
 					  "     OR  A.tp_documento_saida = " + DocumentoSaidaServices.TP_NOTA_FISCAL_VENDA + ")" +
 					  "  ORDER BY CAST(A.dt_documento_saida AS DATE) ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			//
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			//	
			PreparedStatement pstmtNfe = connect.prepareStatement(
					  " SELECT C.nm_forma_pagamento, A.tp_documento_saida, CAST(A.dt_documento_saida AS DATE) AS dt_documento_saida, "+
					  "        B.vl_pagamento " +
					  "   FROM alm_documento_saida A " +
					  "   JOIN adm_plano_pagto_documento_saida B ON (A.cd_documento_saida = B.cd_documento_saida) " +
					  "   JOIN adm_forma_pagamento C ON (B.cd_forma_pagamento = C.cd_forma_pagamento) " +
					  "	  JOIN fsc_nota_fiscal_doc_vinculado D ON(D.cd_documento_saida = A.cd_documento_saida) " +
					  "	  JOIN fsc_nota_fiscal E ON (D.cd_nota_fiscal = E.cd_nota_fiscal) " +
					  "  WHERE A.st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO +
					  "    AND A.tp_saida           = " + DocumentoSaidaServices.SAI_VENDA +
					  "    AND A.cd_empresa = " + cdEmpresa +
					  "    AND A.dt_documento_saida BETWEEN ? AND ? " +
					  "    AND A.tp_documento_saida = " + DocumentoSaidaServices.TP_NOTA_FISCAL_VENDA + 
					  "	   AND E.st_nota_fiscal = " + NotaFiscalServices.AUTORIZADA +
					  "  ORDER BY CAST(A.dt_documento_saida AS DATE) ");
			pstmtNfe.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmtNfe.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsmNfe = new ResultSetMap(pstmtNfe.executeQuery());
			//		
			while(rsmNfe.next()){
				boolean achou = false;
				rsm.beforeFirst();
				while(rsm.next()){
					if(rsm.getString("nm_forma_pagamento").equals(rsmNfe.getString("nm_forma_pagamento")) && rsm.getInt("tp_documento_saida")==DocumentoSaidaServices.TP_NOTA_FISCAL_VENDA && Util.compareDates(rsmNfe.getGregorianCalendar("dt_documento_saida"), rsm.getGregorianCalendar("dt_documento_saida"))==0){
						achou = true;
						rsm.setValueToField("vl_pagamento", rsm.getFloat("vl_pagamento") + rsmNfe.getFloat("vl_pagamento"));
						break;
					}
				}
				if(!achou)
					rsm.addRegister(rsmNfe.getRegister());
			}
			rsm.beforeFirst();
			ArrayList<String> camposOrdenacao = new ArrayList<String>();
			camposOrdenacao.add("DT_DOCUMENTO_SAIDA");
			rsm.orderBy(camposOrdenacao);
			return rsm;
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
	}
	public static void main(String arg[]){
		gerarRegistroP2(3201, 1);
	}
	
	public static String gerarRegistroP2(int cdEmpresa, int cdTabelaPreco){
		try{
			//ItemComparator i = new ItemComparator("lgEstoque", "1", ItemComparator.EQUAL, java.sql.Types.INTEGER);
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("B.st_produto_empresa", "1", ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cdTabelaPreco", Integer.toString(cdTabelaPreco), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("lgEstoque", "1", ItemComparator.EQUAL, java.sql.Types.INTEGER));
			criterios.add(new ItemComparator("stPreco", "1", ItemComparator.EQUAL, java.sql.Types.INTEGER));
			criterios.add(new ItemComparator("notEstoqueNegativoOrZero", "1", ItemComparator.EQUAL, java.sql.Types.BOOLEAN));
			//	
			ResultSetMap rsmProdutos = com.tivic.manager.alm.ProdutoEstoqueServices.findProduto(cdEmpresa, criterios, null);
			//
			String texto = "";				
			/*********************************** IDENTIFICAÇÃO DO ESTABELECIMENTO USUÁRIO DO PAF-ECF E DO ECF ******************************************************************/
			Empresa empresa 		  = com.tivic.manager.grl.EmpresaDAO.get(cdEmpresa);
			String cnpj 			  = empresa.getNrCnpj();
	//		String inscricaoEstadual  = empresa.getNrInscricaoEstadual();
	//		String inscricaoMunicipal = empresa.getNrInscricaoMunicipal();
	//		String razaoSocial 		  = empresa.getNmRazaoSocial();			
	//		//Tipo de Registro
	//		texto += "P1"; 		
	//		//CNPJ
	//		texto += Util.fill(cnpj, 14, '0', 'E');		
	//		//Inscricão Estadual
	//		texto += Util.fill(inscricaoEstadual, 14, ' ', 'D');		
	//		//Inscricão Municipal
	//		texto += Util.fill(inscricaoMunicipal, 14, ' ', 'D');				
	//		//Razão Social
	//		texto += Util.fill(razaoSocial, 50, ' ', 'D');				
	//		texto += "\n";		
			/*********************************** RELAÇÃO DAS MERCADORIAS EM ESTOQUE ******************************************************************/
			while(rsmProdutos.next()){				
				    String cdProdutoServico = (rsmProdutos.getString("id_produto_servico") != null && !rsmProdutos.getString("id_produto_servico").equals("") ? rsmProdutos.getString("id_produto_servico") :
				    						   rsmProdutos.getString("id_reduzido") != null && !rsmProdutos.getString("id_reduzido").equals("") ? rsmProdutos.getString("id_reduzido") : rsmProdutos.getString("nr_referencia"));
				    if (cdProdutoServico.trim().trim().length() > 0){
						String dsProdutoServico = rsmProdutos.getString("nm_produto_servico");
						String unidadeMedida 	= rsmProdutos.getString("sg_unidade_medida");
						String iat  			= (rsmProdutos.getInt("qt_precisao_custo") > 0) ? "A" : "T";
						String ippt 			= "T";				
						//int cdTipoOperacao 		= Integer.parseInt(com.tivic.manager.grl.ParametroServices.getValorOfParametro("CD_TIPO_OPERACAO_VAREJO", cdEmpresa, null));						
						//int cdTabelaPreco  		= com.tivic.manager.adm.TipoOperacaoServices.getCdTabelaPrecoOfOperacao(cdTipoOperacao);				
						ItemComparator i0  		= new ItemComparator("cd_tabela_preco", "" + cdTabelaPreco, ItemComparator.EQUAL, java.sql.Types.INTEGER);
						ItemComparator i1  		= new ItemComparator("cd_produto_servico", "" + rsmProdutos.getInt("cd_produto_servico"), ItemComparator.EQUAL, java.sql.Types.INTEGER);
						ArrayList<ItemComparator> criterios2 = new ArrayList<ItemComparator>();
						criterios2.add(i0);
						criterios2.add(i1);
						String vlValorUnitario         = "";
						String vlValorUnitarioAuxiliar = "";
						ResultSetMap rsmProdutoPreco   = com.tivic.manager.adm.ProdutoServicoPrecoDAO.find(criterios2);								
						if(rsmProdutoPreco.next()){
							vlValorUnitario = "" + rsmProdutoPreco.getFloat("vl_preco");
						}				
						for(int k = 0; k < vlValorUnitario.length(); k++){
							if(!vlValorUnitario.substring(k, k+1).equals(",") && !vlValorUnitario.substring(k, k+1).equals(".")){
								vlValorUnitarioAuxiliar += vlValorUnitario.substring(k, k+1);
							}
						}
						//
						String situacao_aliquota  = "";
						String aliquota 		  = "";	
						situacao_aliquota         = ProdutoServicoEmpresaServices.getAliquotaOfTributo(rsmProdutos.getInt("cd_produto_servico"), cdEmpresa);										
						//
						String situacaoTributaria = situacao_aliquota.substring(4);
						String st = "";							
						aliquota = situacao_aliquota.substring(0, 4);								
						//
						if(situacaoTributaria.equals("Isento")){
							st = "I";
						}else if(situacaoTributaria.equals("Tributada Integralmente")){
							st = "T";
						}else if(situacaoTributaria.equals("Substituição tributária")){
							st = "F";
						}else if(situacaoTributaria.equals("Não tributado")){
							st = "N";
						}else
							st = "S";
						//Tipo de Registro
						texto += "P2"; 						
						//CNPJ
						texto += Util.fill(cnpj, 14, '0', 'E');			
						//Codigo do produto
						texto += Util.fill(Util.limparFormatos(cdProdutoServico), 14, ' ', 'D');				
						//Descricão do produto
						texto += Util.fill(dsProdutoServico.trim(), 50, ' ', 'D');					
						//Unidade de Medida do Produto
						texto += Util.fill(unidadeMedida.trim(), 6, ' ', 'D');				
						//Indicador de Arrendondamento ou Truncamento
						texto += Util.fillAlpha(iat, 1);
						//Indicador de Produção Própria ou de Terceiros
						texto += Util.fillAlpha(ippt, 1);				
						//Situação Tributária
						texto += Util.fillAlpha(st, 1);				
						//Aliquota
						texto += Util.fill(aliquota, 4, '0', 'E');				
						//Valor Unitario
						texto += Util.fill(vlValorUnitarioAuxiliar, 12, '0', 'E');				
						texto += "\n";	
				}
			}
			/*********************************** Totalização do Arquivo ******************************************************************/
	//		//Tipo de Registro
	//		texto += "P9"; 		
	//		//CNPJ
	//		texto += Util.fill(cnpj, 14, '0', 'E');
	//		//Inscricão Estadual
	//		texto += Util.fill(inscricaoEstadual, 14, ' ', 'D');				
	//		//Quantidade Total de Produtos
	//		texto += Util.fillNum(quantidadeDeRegistros, 6);
			//
	//		texto += "\n";
			/*********************************** Assinatura Digital ******************************************************************/
			//String assinaturaDigital = getEAD();
			
			//Tipo de Registro
			//texto += "EAD";
			
			//texto += Util.fill(assinaturaDigital, 256, ' ', 'D');
			
			//texto += "\n";						
			return texto;
		} catch(Exception e){
			e.printStackTrace(System.out);
			return null;
	      } 
	}
	
	public static String gerarRegistroE2(int cdEmpresa, int cdTabelaPreco, String cdProdutos){	
		try{
			//ItemComparator i = new ItemComparator("lgEstoque", "1", ItemComparator.EQUAL, java.sql.Types.INTEGER);
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("B.st_produto_empresa", "1", ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cdTabelaPreco", Integer.toString(cdTabelaPreco), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("lgEstoque", "1", ItemComparator.EQUAL, java.sql.Types.INTEGER));
			criterios.add(new ItemComparator("stPreco", "1", ItemComparator.EQUAL, java.sql.Types.INTEGER));
			criterios.add(new ItemComparator("notEstoqueNegativoOrZero", "1", ItemComparator.EQUAL, java.sql.Types.BOOLEAN));
			//criterios.add(i);
			ResultSetMap rsmProdutos;
			if ((cdProdutos == null) || (cdProdutos == ""))
				rsmProdutos = com.tivic.manager.alm.ProdutoEstoqueServices.findProduto(cdEmpresa, criterios, null);
			else
				rsmProdutos = com.tivic.manager.alm.ProdutoEstoqueServices.findProduto(cdEmpresa, cdProdutos, criterios);
			String texto = "";		
			/*********************************** IDENTIFICAÇÃO DO ESTABELECIMENTO USUÁRIO DO PAF-ECF E DO ECF ******************************************************************/
			Empresa   empresa 			 = com.tivic.manager.grl.EmpresaDAO.get(cdEmpresa);
			String    cnpj    			 = empresa.getNrCnpj();
	//		String    inscricaoEstadual  = empresa.getNrInscricaoEstadual();
	//		String    inscricaoMunicipal = empresa.getNrInscricaoMunicipal();
	//		String    razaoSocial 		 = empresa.getNmRazaoSocial();
	//		String    numeroFabricacao   = null;
	//		String    mfAdiciona 		 = null;
	//		String    tipoECF 			 = null;
	//		String    marca 			 = null;
	//		String    modelo 			 = null;
	//		Calendar calendario 		 = Calendar.getInstance();
	//		String   data 				 = calendario.get(Calendar.YEAR) + Util.fillNum(calendario.get(Calendar.MONTH), 2) + Util.fillNum(calendario.get(Calendar.DAY_OF_MONTH), 2);
	//		String   hora 				 = Util.fillNum(calendario.get(Calendar.HOUR_OF_DAY), 2) + Util.fillNum(calendario.get(Calendar.MINUTE), 2) + Util.fillNum(calendario.get(Calendar.SECOND), 2);				
			//Tipo de Registro
	//		texto += "E1"; 		
	//		//CNPJ
	//		texto += Util.fill(cnpj, 14, '0', 'E');		
	//		//Inscricão Estadual
	//		texto += Util.fill(inscricaoEstadual, 14, ' ', 'D');				
	//		//Inscricão Municipal
	//		texto += Util.fill(inscricaoMunicipal, 14, ' ', 'D');				
	//		//Razão Social
	//		texto += Util.fill(razaoSocial, 50, ' ', 'D');				
	//		//Número de Fabricação
	//		texto += Util.fill(numeroFabricacao, 20, ' ', 'D');				
	//		//MF Adicional
	//		texto += Util.fillAlpha(mfAdiciona, 1);		
	//		//Tipo de ECF
	//		texto += Util.fill(tipoECF, 7, ' ', 'D');		
	//		//Marca do ECF
	//		texto += Util.fill(marca, 20, ' ', 'D');		
	//		//Modelo do ECF
	//		texto += Util.fill(modelo, 20, ' ', 'D');		
	//		//Data do Estoque
	//	    texto += Util.fillAlpha(data, 8);		
	//	    //Hora do estoque
	//	    texto += Util.fillAlpha(hora, 6);	  		
	//		texto += "\n";
			/*********************************** RELAÇÃO DAS MERCADORIAS EM ESTOQUE ******************************************************************/				
			while(rsmProdutos.next()){			
				String codigoProdutoServico      = rsmProdutos.getString("id_produto_servico");
				if ((codigoProdutoServico.trim().length() > 0)){
					String dsProdutoServico          = (rsmProdutos.getString("nm_produto_servico") != null) ? rsmProdutos.getString("nm_produto_servico") : "";			
					String unidadeMedida 		     = rsmProdutos.getString("sg_unidade_medida");			
					//String vlUnitario                = String.valueOf(rsmProdutos.getFloat("vl_preco"));
		//			String vlUnitario                = String.valueOf(rsmProdutos.getFloat("vl_unitario"));			
		//			String nmStTributaria 		     = rsmProdutos.getString("nm_classificacao_fiscal");			
		//			String idStTributaria 		     = rsmProdutos.getString("id_classificacao_fiscal");			
					boolean estoqueNegativo 	     = ((rsmProdutos.getFloat("qt_estoque") + rsmProdutos.getFloat("qt_estoque_consignado")) < 0);			
					String mensuracao 		         = (estoqueNegativo) ? "-" : "+";			
					java.text.DecimalFormat df       = new java.text.DecimalFormat("#,###.000");			
					String quantidadeEstoque         = df.format(rsmProdutos.getFloat("qt_estoque") + rsmProdutos.getFloat("qt_estoque_consignado"));			
					String quantidadeEstoqueAuxiliar = "";
					for(int k = 0; k < quantidadeEstoque.length(); k++){
						if(!quantidadeEstoque.substring(k, k+1).equals(",") && !quantidadeEstoque.substring(k, k+1).equals(".")){
							quantidadeEstoqueAuxiliar += quantidadeEstoque.substring(k, k+1);
						}
					}		
					//Tipo de Registro
					texto += "E2"; 		
					//CNPJ
					texto += Util.fill(cnpj, 14, '0', 'E');						
					//Codigo do produto
					texto += Util.fill(Util.limparFormatos(codigoProdutoServico), 14, ' ', 'D');						
					//Descrissão do produto
					texto += Util.fill(dsProdutoServico.trim(), 50, ' ', 'D');			
					//Unidade de Medida do Produto
					texto += Util.fill(unidadeMedida.trim(), 6, ' ', 'D');				
					//Mensuração de Estoque
					texto += Util.fillAlpha(mensuracao, 1);			
		//			//Valor Unitários
		//			texto += Util.fill(vlUnitario, 6, '0', 'D');			
		//			//Situação Tributária
		//			texto += Util.fill(nmStTributaria, 50, ' ', 'D');
		//			//Código Situação Tributária
		//			texto += Util.fill(idStTributaria.trim(), 6, '0', 'D');						
					//Quantidade em estoque
					texto += Util.fill(quantidadeEstoqueAuxiliar, 9, '0', 'E');			
					texto += "\n";
				}
			}
			/*********************************** Totalização do Arquivo ******************************************************************/
			//Tipo de Registro
	//		texto += "E9"; 
	//				
	//		//CNPJ
	//		texto += Util.fill(cnpj, 14, '0', 'E');
	//				
	//		//Inscrissão Estadual
	//		texto += Util.fill(inscricaoEstadual, 14, ' ', 'D');
	//				
	//		//Quantidade Total de Produtos
	//		texto += Util.fillNum(quantidadeDeRegistros, 6);
	//		
	//		texto += "\n";
			/*********************************** Assinatura Digital ******************************************************************/
			//String assinaturaDigital = getEAD();		
			//Tipo de Registro
		//	texto += "EAD";		
		//	texto += Util.fill(assinaturaDigital, 256, ' ', 'D');		
		//	texto += "\n";		
			  
			/*********************************** Gera Arquivo ******************************************************************/	    
	         return texto;
		}catch(Exception e){
			 Util.registerLog(e);
			 return null;
		}
	}
	
	/**
	 * Registro E3
	 * @param cdEmpresa
	 * @return
	 */
	public static String gerarRegistroE3(int cdEmpresa, int cdReferencia){
		return gerarRegistroE3(cdEmpresa, cdReferencia, null);
	}
	
	public static String gerarRegistroE3(int cdEmpresa, int cdReferencia, String nmPath){
		Connection connect = null;
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			ItemComparator i = new ItemComparator("lgEstoque", "1", ItemComparator.EQUAL, java.sql.Types.INTEGER);
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(i);
			ResultSetMap rsmEcf = new ResultSetMap(connect.prepareStatement("SELECT A.*, B.* FROM bpm_referencia A " +
																			" JOIN bpm_marca B ON (A.cd_marca = B.cd_marca) " +
																			" WHERE A.cd_empresa    = " + cdEmpresa+
													   (cdReferencia > 0 ?  "   AND A.cd_referencia = " + cdReferencia : " ")).executeQuery());
			String texto = "";				
			PreparedStatement pstmtUltimoDoc = connect.prepareStatement("SELECT * FROM alm_documento_saida " +
																		" WHERE cd_referencia_ecf = ? " +
																		"  AND st_documento_saida = "+DocumentoSaidaServices.ST_CONCLUIDO +
																		"  AND tp_documento_saida = " + DocumentoSaidaServices.TP_CUPOM_FISCAL +
																		" ORDER BY dt_documento_saida DESC " +
																		" LIMIT 1");
			while(rsmEcf.next()){				
				pstmtUltimoDoc.setInt(1, rsmEcf.getInt("cd_referencia"));
				ResultSetMap rsmUltimoDoc = new ResultSetMap(pstmtUltimoDoc.executeQuery());
				
				
				String nrFabricacao = rsmEcf.getString("nr_serie");
				String mfAdicional 	= "";
				String tpEcf 		= rsmEcf.getString("tp_referencia");
				String nmMarca 		= rsmEcf.getString("nm_marca");
				String nmModelo     = rsmEcf.getString("nm_modelo");
				String dtEstoque    = "";
				String hrEstoque    = "";
			
				if(rsmUltimoDoc.next()){
					dtEstoque = Util.formatDate(rsmUltimoDoc.getGregorianCalendar("dt_documento_saida"), "yyyyMMdd");
					hrEstoque = Util.formatDate(rsmUltimoDoc.getGregorianCalendar("dt_documento_saida"), "HHmmss");
				}
				// Tipo de Registro
				texto += "E3"; 						
				// Número de Fabricacao
				texto += Util.fill(nrFabricacao, 20, ' ', 'D');			
				// MF Adicional - Letra indicativa
				texto += Util.fill(mfAdicional, 1, ' ', 'D');				
				// Tipo de Ecf
				texto += Util.fill(tpEcf, 7, ' ', 'D');					
				// Marca do Ecf
				texto += Util.fill(nmMarca, 20, ' ', 'D');				
				// Modelo do Ecf
				texto += Util.fillAlpha(nmModelo, 20);
				// Data de Atualização do Estoque
				texto += Util.fillAlpha(dtEstoque, 8);				
				// Hora de Atualização do Estoque
				texto += Util.fillAlpha(hrEstoque, 6);
				texto += "\n";
			}
			/*********************************** Gera Arquivo ******************************************************************/
				return texto;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return "";
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Registro R01
	 * @param cdEmpresa
	 * @return
	 */
	public static String gerarRegistroR01(int cdEmpresa, int cdReferencia, String nrMD5,
											  GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		return gerarRegistroR01(cdEmpresa, cdReferencia, nrMD5, dtInicial, dtFinal, "", null);
	}
	
	public static String gerarRegistroR01(int cdEmpresa, int cdReferencia, String nrMD5, 
			      GregorianCalendar dtInicial, GregorianCalendar dtFinal, String nmPath, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			ItemComparator i = new ItemComparator("lgEstoque", "1", ItemComparator.EQUAL, java.sql.Types.INTEGER);
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(i);
			ResultSetMap rsmEcf             = new ResultSetMap(connect.prepareStatement("SELECT A.*, B.* FROM bpm_referencia A " +
																					    "  JOIN bpm_marca B ON (A.cd_marca = B.cd_marca) " +
																					    " WHERE A.cd_empresa    = " + cdEmpresa+
																					    "   AND A.st_referencia = " + ReferenciaServices.ST_ATIVO +
																    (cdReferencia > 0 ? "   AND A.cd_referencia = " + cdReferencia : " ")).executeQuery());
			String texto = "";
			//
			while(rsmEcf.next()){
				PreparedStatement pstmtUltimoDoc = connect.prepareStatement("SELECT * FROM alm_documento_saida " +
																			" WHERE st_documento_saida =   " + DocumentoSaidaServices.ST_CONCLUIDO +
																			"   AND cd_referencia_ecf  = ? " +
																			"   AND cd_empresa         =   " + cdEmpresa +
																			"   AND tp_documento_saida =   " + DocumentoSaidaServices.TP_CUPOM_FISCAL +
																			" ORDER BY dt_documento_saida DESC " +
																			" LIMIT 1");
				//
				ResultSetMap rsmEmpresa       = new ResultSetMap(connect.prepareStatement("SELECT B.nr_cnpj, B.nr_inscricao_estadual FROM grl_pessoa A " +
																			   		      "LEFT OUTER JOIN grl_pessoa_juridica B ON (A.cd_pessoa = B.cd_pessoa) " +
																			   		      "WHERE A.cd_pessoa = " + cdEmpresa).executeQuery());
				//
				int cdDesenvolvedor           = ParametroServices.getValorOfParametroAsInteger("CD_DESENVOLVEDOR", 0, 0);
				ResultSetMap rsmDesenvolvedor = new ResultSetMap(connect.prepareStatement("SELECT B.nr_cnpj, B.nr_inscricao_estadual, B.nr_inscricao_municipal, B.nm_razao_social, " +
				   																		  " C.nm_comercial, C.nr_versao " +
				   																		  "FROM grl_pessoa A " +
				   																		  "LEFT OUTER JOIN grl_pessoa_juridica   B ON (A.cd_pessoa = B.cd_pessoa) " +
				   																		  "LEFT OUTER JOIN fsc_dados_homologacao C ON (A.cd_pessoa = C.cd_desenvolvedor) " +
				   																		  "WHERE A.cd_pessoa = " + cdDesenvolvedor).executeQuery());
				//												    
				pstmtUltimoDoc.setInt(1, rsmEcf.getInt("cd_referencia"));
				ResultSetMap rsmUltimoDoc = new ResultSetMap(pstmtUltimoDoc.executeQuery());
				//
				String nrFabricacao = rsmEcf.getString("nr_serie");		
				String mfAdicional 	= "";
				String tpEcf 		= rsmEcf.getString("tp_referencia");
				String nmMarca 		= rsmEcf.getString("nm_marca");
				String nmModelo     = rsmEcf.getString("nm_modelo");
				String versaoSB     = rsmEcf.getString("txt_versao");
				String dtEstoque    = "";
				String hrEstoque    = "";
				String nrSequencial = "";
				String nrCNPJEstab  = "";
				String iEEmpresa    = "";
				String nrCNPJDesenv = "";
				String iEDesenv     = "";
				String iMDesenv     = "";
				String nmRSDesenv   = "";
				String nmComercial  = "";
				String nrVersao     = "";
				String dtInicialR   = "";
				String dtFinalR     = "";
				if (dtInicial != null)
					dtInicialR      = Util.formatDate(dtInicial, "yyyyMMdd");
				if (dtFinal != null)
					dtFinalR        = Util.formatDate(dtFinal, "yyyyMMdd");
				//
				if(rsmUltimoDoc.next()){
					dtEstoque    = Util.formatDate(rsmUltimoDoc.getGregorianCalendar("dt_documento_saida"), "yyyyMMdd");
					hrEstoque    = Util.formatDate(rsmUltimoDoc.getGregorianCalendar("dt_documento_saida"), "HHmmss");
					nrSequencial = rsmUltimoDoc.getString("nr_documento_saida");
				}
				//
				if(rsmEmpresa.next()){
					nrCNPJEstab  = rsmEmpresa.getString("nr_cnpj");
					iEEmpresa    = rsmEcf.getString("nr_inscricao_estadual");
				}
				//
				while(rsmDesenvolvedor.next()){
					nrCNPJDesenv = rsmDesenvolvedor.getString("nr_cnpj");
					iEDesenv     = rsmDesenvolvedor.getString("nr_inscricao_estadual");
					iMDesenv     = rsmDesenvolvedor.getString("nr_inscricao_municipal");
					nmRSDesenv   = rsmDesenvolvedor.getString("nm_razao_social");
					nmComercial  = rsmDesenvolvedor.getString("nm_comercial");
					nrVersao     = rsmDesenvolvedor.getString("nr_versao");
				}
				/**
				 * R01
				 * REGISTRO TIPO R01 - IDENTIFICAÇÃO DO ECF, DO USUÁRIO, DO PAF-ECF E DA EMPRESA DESENVOLVEDORA
				 * */
				//Tipo de Registro
				texto += "R01"; 						
				//Número de Fabricacao
				texto += Util.fill(nrFabricacao, 20, ' ', 'D');			
				//MF Adicional - Letra indicativa
				texto += Util.fill(mfAdicional, 1, ' ', 'D');				
				//Tipo de Ecf
				texto += Util.fill(tpEcf, 7, ' ', 'D');					
				//Marca do Ecf
				texto += Util.fill(nmMarca, 20, ' ', 'D');				
				//Modelo do Ecf
				texto += Util.fill(nmModelo, 20, ' ', 'D');
				//Versão do SB
				texto += Util.fill(versaoSB, 10, ' ', 'D');
				//Data de Atualização do Estoque
				texto += Util.fillAlpha(dtEstoque, 8);				
				//Hora de Atualização do Estoque
				texto += Util.fillAlpha(hrEstoque, 6);
				//Número Sequencial do ECF
				texto += Util.fill(nrSequencial, 3, '0', 'D');
				//CNPJ do usuário
				texto += Util.fill(nrCNPJEstab, 14, '0', 'D');
				//Inscrição Estadual do usuário
				texto += Util.fill(iEEmpresa, 14, ' ', 'D');
				//CNPJ da desenvolvedora
				texto += Util.fill(nrCNPJDesenv, 14, '0', 'D');
				//Inscrição Estadual da desenvolvedora
				texto += Util.fill(iEDesenv, 14, ' ', 'D');
				//Inscrição Municipal da desenvolvedora
				texto += Util.fill(iMDesenv, 14, '0', 'D');
				//Denominação da empresa desenvolvedora
				texto += Util.fill(nmRSDesenv, 40, ' ', 'D');
				//Nome do PAF-ECF
				texto += Util.fill(nmComercial, 40, ' ', 'D');
				//Versão do PAF-ECF
				texto += Util.fill(nrVersao, 10, ' ', 'D');
				//Código MD-5 do PAF-ECF
				texto += Util.fill(nrMD5, 32, ' ', 'D');
				//Data Inicial
				texto += Util.fillAlpha(dtInicialR, 8);
				//Data Final
				texto += Util.fillAlpha(dtFinalR, 8);
				//Versão da ER-PAF-ECF
				texto += "0201";
				texto += "\n";
			}
			//
			return texto;
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	/**
	 * REGISTRO TIPO R02 - RELAÇÃO DE REDUÇÕES Z
	 * @param cdEmpresa
	 * @param cdReferenciaEcf
	 * @param dtMovimento
	 * @param txtRegistro02
	 * @return
	 */
	public static Result saveRegistroR02 (int cdEmpresa, int cdReferenciaEcf, GregorianCalendar dtMovimento, String txtRegistro02) {
		Connection connect = Conexao.conectar();
		try {
			// Pesquisando existência do registro
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
															   " WHERE CAST(dt_registro_ecf AS DATE) = ? "+
															   "   AND cd_referencia_ecf             = " +cdReferenciaEcf+
															   "   AND tp_registro_ecf               = \'R02\'");
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			ResultSet rsR02 = pstmt.executeQuery();
			int cdRegistroEcf = 0;
			if(rsR02.next()) 
				cdRegistroEcf = rsR02.getInt("cd_registro_ecf");			
			// Gravando
			RegistroEcf regR02 = new RegistroEcf(cdRegistroEcf, "R02", dtMovimento, 0/*vlRegistroEcf*/, txtRegistro02, cdReferenciaEcf,0 /*lgSped*/,""/*status*/);
			if(cdRegistroEcf>0)
				RegistroEcfDAO.update(regR02, connect);
			else
				RegistroEcfDAO.insert(regR02, connect);
			//
			return new Result(1);
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar salvar registro 02", e);
		}
	}
	/**
	 * REGISTRO TIPO R03 - DETALHE DA REDUÇÃO Z
	 * @param cdEmpresa
	 * @param cdReferenciaEcf
	 * @param dtMovimento
	 * @param txtRegistro03
	 * @return
	 */
	public static Result saveRegistroR03 (int cdEmpresa, int cdReferenciaEcf, GregorianCalendar dtMovimento, String txtRegistro03) {
		Connection connect = Conexao.conectar();
		try {
			// Pesquisando existência do registro
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
															   " WHERE CAST(dt_registro_ecf AS DATE) = ? "+
															   "   AND cd_referencia_ecf             = " +cdReferenciaEcf+
															   "   AND tp_registro_ecf               = \'R03\'");
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			ResultSet rsR03 = pstmt.executeQuery();
			int cdRegistroEcf = 0;
			if(rsR03.next()) 
				cdRegistroEcf = rsR03.getInt("cd_registro_ecf");  
			// Gravando
			Scanner line = new Scanner(txtRegistro03);
			while (line.hasNextLine()){
				//
	            RegistroEcf regR03 = new RegistroEcf(cdRegistroEcf, "R03", dtMovimento, 0/*vlRegistroEcf*/, line.nextLine(), cdReferenciaEcf,0 /*lgSped*/, ""/*status*/);	          
	            if(cdRegistroEcf>0)
	            	RegistroEcfDAO.update(regR03, connect);
	            else
	            	RegistroEcfDAO.insert(regR03, connect);
			}
			return new Result(1);
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar salvar registro 03", e);
		}
	}
	/**
	 * REGISTRO TIPO R04 - CUPOM FISCAL, NOTA FISCAL DE VENDA A CONSUMIDOR E BILHETE DE PASSAGEM
	 * @param cdEmpresa
	 * @param nrDocumento
	 * @param cdReferenciaEcf
	 * @param dtMovimento
	 * @param txtRegistro04
	 * @return
	 */
	public static Result saveRegistroR04 (int cdEmpresa, String nrDocumento, int cdReferenciaEcf, GregorianCalendar dtMovimento, String txtRegistro04) {
		Connection connect = Conexao.conectar();
		try {
			// Pesquisando existência do registro
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
											 				   " WHERE CAST(dt_registro_ecf AS DATE) = ? "+
											 				   "   AND SUBSTRING (txt_registro_ecf, 53, 6) LIKE \'"+ nrDocumento+ "\'"+
											 				   "   AND cd_referencia_ecf             = " +cdReferenciaEcf+
									 	     				   "   AND tp_registro_ecf               = \'R04\'");
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			ResultSet rsR04    = pstmt.executeQuery();
			int cdRegistroEcf  = 0;
			//
			if(rsR04.next()) {
				cdRegistroEcf  = rsR04.getInt("cd_registro_ecf");
			}
			pstmt.close();
			// Pesquisando existência do registro
			String nmColunas = "A.nr_documento_saida, CAST(A.dt_documento_saida AS DATE), A.vl_total_documento, A.vl_desconto, A.vl_acrescimo, " +
					           "B.nm_pessoa, C.nr_cpf, D.nr_cnpj, A.tp_documento_saida, A.st_documento_saida ";
			pstmt = connect.prepareStatement("SELECT SUM(A.vl_total_documento - A.vl_acrescimo + A.vl_desconto) AS vl_total, " + nmColunas +
											 "  FROM alm_documento_saida A " +
				 						     "  LEFT JOIN grl_pessoa          B ON (A.cd_cliente = B.cd_pessoa) "+
				 						     "  LEFT JOIN grl_pessoa_fisica   C ON (B.cd_pessoa  = C.cd_pessoa) "+
				 						     "  LEFT JOIN grl_pessoa_juridica D ON (B.cd_pessoa  = D.cd_pessoa) "+
				 						     " WHERE A.cd_empresa         = " + cdEmpresa +
				 						     "   AND A.cd_referencia_ecf  = " + cdReferenciaEcf+
				 						     "   AND A.nr_documento_saida = \'" + nrDocumento+"\'"+
				 						     "   AND A.tp_documento_saida = " + DocumentoSaidaServices.TP_CUPOM_FISCAL+
				 						     "   AND A.st_documento_saida <> " + DocumentoSaidaServices.ST_EM_CONFERENCIA+  
				 						     "   AND A.dt_documento_saida BETWEEN ? AND ? " +
				 						     "GROUP BY "+nmColunas);
			// Inicial
			dtMovimento.set(Calendar.HOUR, 0);
			dtMovimento.set(Calendar.MINUTE, 0);
			dtMovimento.set(Calendar.SECOND, 0);						
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			// Final
			dtMovimento.set(Calendar.HOUR_OF_DAY, 23);
			dtMovimento.set(Calendar.MINUTE, 59);
			dtMovimento.set(Calendar.SECOND, 59);			
			pstmt.setTimestamp(2, new Timestamp(dtMovimento.getTimeInMillis()));
			//
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			pstmt.close();
			DecimalFormat df = new DecimalFormat("0.00");
			//
			if(rsm.next()){				
				//Valor Total do Documento
				txtRegistro04 += Util.fill(Util.limparFormatos(df.format(rsm.getFloat("vl_total"))), 14, '0', 'E');
				//Desconto sobre o Valor Total
				txtRegistro04 += Util.fill(Util.limparFormatos(df.format(rsm.getFloat("vl_desconto"))), 13, '0', 'E');
				//Indicador do Tipo de Desconto
				if (rsm.getFloat("vl_desconto") > 0)
					txtRegistro04 += "V";
				else
					txtRegistro04 += " ";
				//Acrescimo sobre o Valor Total
				txtRegistro04 += Util.fill(Util.limparFormatos(df.format(rsm.getFloat("vl_acrescimo"))), 13, '0', 'E');
				//Indicador do Tipo de Acrescimo
				if (rsm.getFloat("vl_acrescimo") > 0)
					txtRegistro04 += "V";
				else
					txtRegistro04 += " ";
				//Valor Total Líquido
				txtRegistro04 += Util.fill(Util.limparFormatos(df.format(rsm.getFloat("vl_total_documento"))), 14, '0', 'E');
				//Indicador de Cancelamento
				if (cdRegistroEcf > 0){					
					txtRegistro04 += "S";
					//Cancelamento de Acréscimo no Subtotal
					txtRegistro04 += Util.fill(Util.limparFormatos(df.format(rsm.getFloat("vl_acrescimo"))), 13, '0', 'E');	
				}
				else{
					txtRegistro04 += "N";
					//Cancelamento de Acréscimo no Subtotal
					txtRegistro04 += Util.fill(Util.limparFormatos(df.format(rsm.getFloat("vl_acrescimo"))), 13, '0', 'E');
				}
				//Ordem de aplicação de Desconto e Acréscimo
				if (rsm.getFloat("vl_desconto") > 0)
					txtRegistro04 += "D";
				else if(rsm.getFloat("vl_acrescimo")>0)
					txtRegistro04 += "A";
				else
					txtRegistro04 += " ";
				//Nome do Cliente
				txtRegistro04 += Util.fill(rsm.getString("nm_pessoa"), 40, ' ', 'D');
				//CPF ou CNPJ do adquirente
				if(!(rsm.getString("nr_cpf") == null))
					txtRegistro04 += Util.fill(rsm.getString("nr_cpf"), 14, '0', 'E');
				else
					txtRegistro04 += Util.fill(rsm.getString("nr_cnpj"), 14, '0', 'E');
			}else
				txtRegistro04 += "000000000000000000000000000 0000000000000 00000000000000S0000000000000                                        00000000000000";
			// Gravando			
            RegistroEcf regR04 = new RegistroEcf(cdRegistroEcf, "R04", dtMovimento, 0/*vlRegistroEcf*/, txtRegistro04, cdReferenciaEcf,0 /*lgSped*/, ""/*status*/);	          
            if(cdRegistroEcf>0)
            	RegistroEcfDAO.update(regR04, connect);
            else
            	RegistroEcfDAO.insert(regR04, connect);
            
			return new Result(1);
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar salvar registro 04", e);
		}
	}
	/**
	 * REGISTRO TIPO R04 - CUPOM FISCAL, NOTA FISCAL DE VENDA A CONSUMIDOR E BILHETE DE PASSAGEM
	 * @param cdEmpresa
	 * @param cdReferenciaEcf
	 * @param nrDocumento
	 * @param nrItem
	 * @param dtMovimento
	 * @param txtRegistro05
	 * @return
	 */
	public static Result saveRegistroR05 (int cdEmpresa, int cdReferenciaEcf, String nrDocumento, String nrItem, GregorianCalendar dtMovimento, String txtRegistro05) {
		Connection connect = Conexao.conectar();
		try {
			// Pesquisando existência do registro
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
															   " WHERE CAST(dt_registro_ecf AS DATE) = ? "+
															   "   AND cd_referencia_ecf             = " +cdReferenciaEcf+
															   "   AND tp_registro_ecf               = \'R05\'" +
															   "   AND SUBSTRING (txt_registro_ecf, 47, 6) LIKE \'"+ nrDocumento+ "\'"+
															   ((nrItem == null) ? "" : " AND SUBSTRING (txt_registro_ecf, 59, 3) LIKE \'"+ nrItem+ "\'"));
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			ResultSetMap rsmR05 = new ResultSetMap(pstmt.executeQuery());
			int cdRegistroEcf = 0;
			//Alterando
			RegistroEcf regR05;
			while (rsmR05.next()){ 
				cdRegistroEcf = rsmR05.getInt("cd_registro_ecf");
				//
				txtRegistro05 = rsmR05.getString("txt_registro_ecf");
				txtRegistro05 = txtRegistro05.substring(0, 230) + "S" + txtRegistro05.substring(175, 182) +"00000"+ txtRegistro05.substring(185, 193) + 
							    "00000" + txtRegistro05.substring(201, 209) + txtRegistro05.substring(264, 268);
				//
				regR05 = new RegistroEcf(cdRegistroEcf, "R05", dtMovimento, 0/*vlRegistroEcf*/, txtRegistro05, cdReferenciaEcf,0 /*lgSped*/, ""/*status*/);
				RegistroEcfDAO.update(regR05, connect);
			}
			// Gravando	Novo Registro				
			if (cdRegistroEcf == 0){
				regR05 = new RegistroEcf(cdRegistroEcf, "R05", dtMovimento, 0/*vlRegistroEcf*/, txtRegistro05, cdReferenciaEcf,0 /*lgSped*/, ""/*status*/);
				RegistroEcfDAO.insert(regR05, connect);
			}
			//
			return new Result(1);
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);			
			return new Result(-1, "Falha ao tentar salvar registro 05", e);
		}
	}
	/**
	 * REGISTRO TIPO R06 - DEMAIS DOCUMENTOS EMITIDOS PELO ECF
	 * @param cdEmpresa
	 * @param cdReferenciaEcf
	 * @param dtMovimento
	 * @param txtRegistro06
	 * @return
	 */
	public static Result saveRegistroR06 (int cdEmpresa, int cdReferenciaEcf, GregorianCalendar dtMovimento, String txtRegistro06) {
		Connection connect = Conexao.conectar();
		try {
			// Pesquisando existência do registro
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
															   " WHERE CAST(dt_registro_ecf AS DATE) = ? "+
															   "   AND cd_referencia_ecf             = " +cdReferenciaEcf+
															   "   AND tp_registro_ecf               = \'R06\'"+
															   "   AND SUBSTRING (txt_registro_ecf, 79, 6) LIKE \'"+ txtRegistro06.substring(79, 84)+ "\'");
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			ResultSet rsR06 = pstmt.executeQuery();
			int cdRegistroEcf = 0;
			if(rsR06.next()) 
				cdRegistroEcf = rsR06.getInt("cd_registro_ecf");  
			// Gravando
			RegistroEcf regR06 = new RegistroEcf(cdRegistroEcf, "R06", dtMovimento, 0/*vlRegistroEcf*/, txtRegistro06, cdReferenciaEcf,0 /*lgSped*/, ""/*status*/);
			if(cdRegistroEcf>0)
				RegistroEcfDAO.update(regR06, connect);
			else
				RegistroEcfDAO.insert(regR06, connect);
			//
			return new Result(1);
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar salvar registro 06", e);
		}
	}
	/**
	 * REGISTRO TIPO R07 - DETALHE DO CUPOM FISCAL E DO DOCUMENTO NÃO FISCAL - MEIO DE PAGAMENTO
	 * @param cdEmpresa
	 * @param cdReferenciaEcf
	 * @param dtMovimento
	 * @param txtRegistro07
	 * @return
	 */
	public static Result saveRegistroR07 (int cdEmpresa, int cdReferenciaEcf, GregorianCalendar dtMovimento, String txtRegistro07) {
		Connection connect = Conexao.conectar();
		try {
			// Pesquisando existência do registro
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
															   " WHERE CAST(dt_registro_ecf AS DATE) = ? "+
															   "   AND cd_referencia_ecf             = " +cdReferenciaEcf+
															   "   AND tp_registro_ecf               = \'R07\'"+
															   "   AND SUBSTRING (txt_registro_ecf, 47, 6) LIKE \'"+ txtRegistro07.substring(46, 52) + "\'"+ /*COO*/
															   "   AND SUBSTRING (txt_registro_ecf, 65, 15) LIKE \'"+ txtRegistro07.substring(64, 79) + "\'"); /*Meio de pagamento*/
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			ResultSet rsR07 = pstmt.executeQuery();
			int cdRegistroEcf = 0;
			if (rsR07.next()){ 
				cdRegistroEcf = rsR07.getInt("cd_registro_ecf");
			}
				// Gravando
			RegistroEcf regR07 = new RegistroEcf(cdRegistroEcf, "R07", dtMovimento, 0/*vlRegistroEcf*/, txtRegistro07, cdReferenciaEcf,0 /*lgSped*/, ""/*status*/);
			if(cdRegistroEcf>0)
				RegistroEcfDAO.update(regR07, connect);
			else
				RegistroEcfDAO.insert(regR07, connect);
			//
			return new Result(1);
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar salvar registro 07", e);
		}
	}
	// Registros Utilizados em Postos de combustíveis.
	/**
	 * REGISTRO TIPO B2 - REGISTROS DE SUBSTITUIÇÃO DA PLACA ELETRÔNICA DE GERENCIAMENTO DE BOMBA DE COMBUSTÍVEL
	 * @param cdEmpresa
	 * @param cdReferenciaEcf
	 * @param dtMovimento
	 * @param txtRegistroB2
	 * @return
	 */
	public static Result saveRegistroB2 (int cdEmpresa, int cdReferenciaEcf, GregorianCalendar dtMovimento, String txtRegistroB2) {
		Connection connect = Conexao.conectar();
		
		try {
			// Pesquisando existência do registro
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
															   " WHERE CAST(dt_registro_ecf AS DATE) = ? "+
															   "   AND tp_registro_ecf               = \'B2\'" +
															   "   AND SUBSTRING (txt_registro_ecf, 17, 6) LIKE \'"+ txtRegistroB2.substring(16, 22) + "\'" +/*Bomba e Bico*/
															   "   AND SUBSTRING (txt_registro_ecf, 31, 6) LIKE \'"+ txtRegistroB2.substring(30, 36) + "\'" /*Bomba e Bico*/);
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			ResultSet rsB2 = pstmt.executeQuery();
			int cdRegistroEcf = 0;
			if(rsB2.next()) 
				cdRegistroEcf = rsB2.getInt("cd_registro_ecf");			
			// Gravando
			RegistroEcf regB2 = new RegistroEcf(cdRegistroEcf, "B2", dtMovimento, 0/*vlRegistroEcf*/, txtRegistroB2, cdReferenciaEcf,0 /*lgSped*/,""/*status*/);
			if(cdRegistroEcf>0)
				RegistroEcfDAO.update(regB2, connect);
			else
				RegistroEcfDAO.insert(regB2, connect);
			//
			return new Result(1);
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar salvar registro B2", e);
		}
	}
	
	/**
	 * REGISTRO TIPO C2 - CONTROLE DE ABASTECIMENTOS E ENCERRANTES
	 * @param cdEmpresa             codigo da empresa que o lancamento foi efetuado
	 * @param cdReferenciaEcf       codigo de referencia da impressora registrada no banco de dados
	 * @param dtMovimento           data do lancamento
	 * @param txtRegistroC2         String enviada pelo PDV para registro.
	 * */
	public static Result saveRegistroC2 (int cdEmpresa, int cdReferenciaEcf, GregorianCalendar dtMovimento, String txtRegistroC2) {
		Connection connect = Conexao.conectar();
		
		try {
			// Pesquisando existência do registro
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
															   " WHERE CAST(dt_registro_ecf AS DATE) = ? "+
										(cdReferenciaEcf > 0 ? "   AND cd_referencia_ecf             = " +cdReferenciaEcf : "") +
															   "   AND tp_registro_ecf               = \'C2\'" +
															   "   AND SUBSTRING (txt_registro_ecf, 15, 3) LIKE \'"+ txtRegistroC2.substring(14, 17) + "\'" + /*Chave Bomba*/
															   "   AND SUBSTRING (txt_registro_ecf, 35, 6) LIKE \'"+ txtRegistroC2.substring(34, 40) + "\'" + /*Bomba e Bico*/
															   "   AND SUBSTRING (txt_registro_ecf, 90, 15) LIKE \'"+ txtRegistroC2.substring(89, 104) + "\'" /*Encerrante Final*/);
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			ResultSet rsC2 = pstmt.executeQuery();
			int cdRegistroEcf = 0;
			if(rsC2.next()) 
				cdRegistroEcf = rsC2.getInt("cd_registro_ecf");
			// Gravando
			RegistroEcf regC2; 
			if(cdRegistroEcf>0){
				regC2 = new RegistroEcf(cdRegistroEcf, "C2", dtMovimento, 0/*vlRegistroEcf*/, txtRegistroC2, cdReferenciaEcf,0 /*lgSped*/,txtRegistroC2.substring(104, 114)/*status*/);
				RegistroEcfDAO.update(regC2, connect);
			}
			else{
				regC2 = new RegistroEcf(cdRegistroEcf, "C2", dtMovimento, 0/*vlRegistroEcf*/, txtRegistroC2, cdReferenciaEcf,0 /*lgSped*/,"PENDENTE" /*status*/);
				RegistroEcfDAO.insert(regC2, connect);
			}
			//
			return new Result(1);
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar salvar registro C2", e);
		}
	}
	//
	/**
	 * Relatório Gerencial denominado CONTROLE DE ENCERRANTES
	 * 
	 * */
	public static Result saveRegistroCE (int cdEmpresa, int cdReferenciaEcf, GregorianCalendar dtMovimento, String txtRegistroCE) {
		Connection connect = Conexao.conectar();
		
		try {
			// Pesquisando existência do registro
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
															   " WHERE CAST(dt_registro_ecf AS DATE) = ? "+
															   "   AND cd_referencia_ecf             = " +cdReferenciaEcf+
															   "   AND tp_registro_ecf               = \'CE\'"+
															   "   AND SUBSTRING (txt_registro_ecf, 9, 13) LIKE \'"+ txtRegistroCE.substring(8, 21) + "\'" /*Encerrante Inicial*/);
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			ResultSet rsCE = pstmt.executeQuery();
			int cdRegistroEcf = 0;
			if(rsCE.next()) 
				cdRegistroEcf = rsCE.getInt("cd_registro_ecf");			
			// Gravando
			RegistroEcf regCE = new RegistroEcf(cdRegistroEcf, "CE", dtMovimento, 0/*vlRegistroEcf*/, txtRegistroCE, cdReferenciaEcf,0 /*lgSped*/,""/*status*/);
			if(cdRegistroEcf>0)
				RegistroEcfDAO.update(regCE, connect);
			else
				RegistroEcfDAO.insert(regCE, connect);
			//
			return new Result(1);
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar salvar registro B2", e);
		}
	}
	//
	public static Result saveRegistroAP(int cdEmpresa, GregorianCalendar dtMovimento, String txtRegistroAP) {
		Connection connect = Conexao.conectar();
		
		try {
			// Pesquisando existência do registro		
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
															   " WHERE CAST(dt_registro_ecf AS DATE) = ? "+
															   "   AND tp_registro_ecf               = \'AP\'"+
															   "   AND txt_registro_ecf LIKE \'"+ txtRegistroAP + "\'" /**/);
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			ResultSet rsAP = pstmt.executeQuery();
			int cdRegistroEcf = 0;
			if(rsAP.next()) 
				cdRegistroEcf = rsAP.getInt("cd_registro_ecf");			
			// Gravando
			RegistroEcf regAP = new RegistroEcf(cdRegistroEcf, "AP", dtMovimento, 0/*vlRegistroEcf*/, txtRegistroAP, 0 /*cdReferenciaEcf*/, 0 /*lgSped*/, "PENDENTE"/*status*/);
			if(cdRegistroEcf>0)
				RegistroEcfDAO.delete(cdRegistroEcf);
			else
				RegistroEcfDAO.insert(regAP, connect);
			//
			return new Result(1);
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar salvar registro AP", e);
		}
	}
	//
	public static ResultSetMap getRegistroCE(int cdEmpresa, int cdReferenciaEcf, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		Connection connect = Conexao.conectar();
		// Inicial
		dtInicial.set(Calendar.HOUR, 0);
		dtInicial.set(Calendar.MINUTE, 0);
		dtInicial.set(Calendar.SECOND, 0);
		// Final
		dtFinal.set(Calendar.HOUR, 23);
		dtFinal.set(Calendar.MINUTE, 59);
		dtFinal.set(Calendar.SECOND, 59);

		try	{
			//
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_registro_ecf, txt_registro_ecf FROM fsc_registro_ecf " + 
															   " WHERE dt_registro_ecf BETWEEN ? AND ? "+
										(cdReferenciaEcf > 0 ? "   AND cd_referencia_ecf = " + cdReferenciaEcf : " ")+
															   "   AND tp_registro_ecf LIKE \'CE\' " +
															   " ORDER BY txt_registro_ecf ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			//
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			//	
			return rsm;
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
	}
	//
	public static ResultSetMap getRegistroAP(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		Connection connect = Conexao.conectar();
		// Inicial
		dtInicial.set(Calendar.HOUR, 0);
		dtInicial.set(Calendar.MINUTE, 0);
		dtInicial.set(Calendar.SECOND, 0);
		// Final
		dtFinal.set(Calendar.HOUR, 23);
		dtFinal.set(Calendar.MINUTE, 59);
		dtFinal.set(Calendar.SECOND, 59);

		try	{
			//
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_registro_ecf, txt_registro_ecf FROM fsc_registro_ecf " + 
															   " WHERE dt_registro_ecf BETWEEN ? AND ? "+
															   "   AND tp_registro_ecf LIKE \'AP\' " +
															   " ORDER BY txt_registro_ecf ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			//
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			//	
			return rsm;
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
	}
	//
	public static String getRegistrosPAFECF(int cdEmpresa, int cdReferenciaEcf, int cdTabelaPreco, String nrMD5, String txtRegistros, String cdProdutos, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		return getRegistrosPAFECF(cdEmpresa, cdReferenciaEcf, cdTabelaPreco, nrMD5, txtRegistros, cdProdutos, dtInicial, dtFinal, null);
	}
	
	public static String getRegistrosPAFECF(int cdEmpresa, int cdReferenciaEcf, int cdTabelaPreco, String nrMD5, String txtRegistros, String cdProdutos, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect){
		connect = Conexao.conectar();
		// Inicial
		dtInicial.set(Calendar.HOUR, 0);
		dtInicial.set(Calendar.MINUTE, 0);
		dtInicial.set(Calendar.SECOND, 0);
		// Final
		dtFinal.set(Calendar.HOUR, 23);
		dtFinal.set(Calendar.MINUTE, 59);
		dtFinal.set(Calendar.SECOND, 59);
		try	{
			//
			txtRegistros += gerarRegistroP2(cdEmpresa, cdTabelaPreco);
			txtRegistros += gerarRegistroE2(cdEmpresa, cdTabelaPreco, cdProdutos);
			txtRegistros += gerarRegistroE3(cdEmpresa, cdReferenciaEcf);
			// Dados para posto de Combustível
			// Registro B2
			PreparedStatement pstmt = connect.prepareStatement("SELECT txt_registro_ecf FROM fsc_registro_ecf " + 
					   										   " WHERE dt_registro_ecf BETWEEN ? AND ? "+
					   					(cdReferenciaEcf > 0 ? "   AND cd_referencia_ecf = " + cdReferenciaEcf : " ")+
					   										   "   AND tp_registro_ecf LIKE \'B2\' " +
					   										   " ORDER BY tp_registro_ecf, cd_referencia_ecf, dt_registro_ecf ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while (rsm.next()){
				txtRegistros += rsm.getString("txt_registro_ecf");
				txtRegistros += "\n" + "";
			}
			pstmt.close();
			rsm = null;
			// Registro C2
			pstmt = connect.prepareStatement("SELECT txt_registro_ecf FROM fsc_registro_ecf " + 
											 " WHERE dt_registro_ecf BETWEEN ? AND ? "+
											 "   AND tp_registro_ecf LIKE \'C2\' " +
					   					     " ORDER BY tp_registro_ecf, cd_referencia_ecf, dt_registro_ecf ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			rsm = new ResultSetMap(pstmt.executeQuery());
			while (rsm.next()){
				txtRegistros += rsm.getString("txt_registro_ecf");
				txtRegistros += "\n" + "";
			}
			pstmt.close();
			rsm = null;
			//
			txtRegistros += gerarRegistroR01(cdEmpresa, cdReferenciaEcf, nrMD5, dtInicial, dtFinal);
			//
			pstmt = connect.prepareStatement("SELECT txt_registro_ecf FROM fsc_registro_ecf " + 
											 " WHERE dt_registro_ecf BETWEEN ? AND ? "+
					  (cdReferenciaEcf > 0 ? "   AND cd_referencia_ecf = " + cdReferenciaEcf : " ")+
					  					     "   AND tp_registro_ecf LIKE \'R0%\' " +
						 				     " ORDER BY tp_registro_ecf, cd_referencia_ecf, dt_registro_ecf ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			//
			rsm = new ResultSetMap(pstmt.executeQuery());
			//	
			while (rsm.next()){
				txtRegistros += rsm.getString("txt_registro_ecf");
				txtRegistros += "\n" + "";
			}
			return txtRegistros;
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
	}
}
