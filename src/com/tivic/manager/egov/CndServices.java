package com.tivic.manager.egov;

import java.sql.Connection;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import sol.util.Result;
import sol.dao.ResultSetMap; 
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.grl.TipoLogradouroDAO;
import com.tivic.manager.grl.TipoLogradouro;
import com.tivic.manager.grl.Setor;
import com.tivic.manager.grl.SetorDAO;
import com.tivic.manager.ptc.*;
import com.tivic.manager.util.Util;

public class CndServices {

	@SuppressWarnings("resource")
	public static Result gerarCND(String nrCpfCnpj, int tpFinalidade, String nrImovelTransferencia)	{
		Connection conLocal     = Conexao.conectar();
		Connection connectIPT = null, connectISS = null, connectDIV = null;
		try	{
			GregorianCalendar init = new GregorianCalendar();
			/*
			 * VERIFICANDO VALIDADE DO CPF/CNPJ
			 */
			int nrAnoBase = new GregorianCalendar().get(Calendar.YEAR);
			int gnPessoa  = PessoaServices.TP_JURIDICA;
			nrCpfCnpj = nrCpfCnpj!=null ? nrCpfCnpj.replaceAll("[\\.]", "").replaceAll("[\\-]", "").replaceAll("[\\/]", "") : "";
			// Verificando CPF
			if(nrCpfCnpj==null || nrCpfCnpj.equals(""))
				return new Result(-1, "Informe o número do CPF ou CNPJ!");
			while(nrCpfCnpj.length()<11)
				nrCpfCnpj = "0"+nrCpfCnpj;
			// Verificando se é um "CPF" Válido
			if(nrCpfCnpj.length()<=11)	{
				gnPessoa  = PessoaServices.TP_FISICA;
				if(!com.tivic.manager.util.Util.isCpfValido(nrCpfCnpj))
					return new Result(-2, "Número do CPF inválido!");
			}
			// CNPJ
			else if(!com.tivic.manager.util.Util.isCNPJ(nrCpfCnpj))
				return new Result(-2, "Número do CNPJ inválido!");
			connectIPT = IptuServices.getConnection("IPT");
			connectISS = IptuServices.getConnection("ISS");
			connectDIV = IptuServices.getConnection("DIV");
			//System.out.println(" \nConectou: "+((new GregorianCalendar().getTimeInMillis() - init.getTimeInMillis()) / 1000)+"s");
			/*
			 * DADOS DO CONTRIBUINTE - Verificando
			 */
			String nrInscMunicipal  = "";
			String nmContribuinte   = "";
			String nmTipoLogradouro = "";
			String nmLogradouro 	= "";
			String nrEndereco       = "";
			String nmComplemento    = "";
			String nmBairro         = "";
			String nrCep            = "";
			Result result = new Result(1);
			// GLOBAL IPT.ADINOM00 (CÓDIGO)
			boolean lgEncontrado = false;
			ResultSetMap rsm  = new ResultSetMap(connectIPT.prepareStatement("SELECT * FROM IPT.sqlADINOM00 "+
									                                         "WHERE P02 = \'"+nrCpfCnpj+"\'").executeQuery());
			int qtCadastro = rsm.size();
			if(rsm.next())	{
				lgEncontrado = true;
				nmContribuinte   = rsm.getString("P01");
				nmTipoLogradouro = rsm.getString("P03");
				nmLogradouro 	 = rsm.getString("P04");
				nrEndereco       = rsm.getString("P05");
				nmComplemento    = rsm.getString("P06");
				nmBairro         = rsm.getString("P07");
				nrCep            = rsm.getString("P09");
			}
			// Verifica duplicidade nessa tabela somente pra pessoa jurídica
			if(gnPessoa == PessoaServices.TP_FISICA && rsm.size()>1) 
				return new Result(-140, "Pessoa Física com Cadastro em duplicidade!");
			/* *****************************************************************************************************************************************************
			 *********************************************************** HOMÔNIMOS *********************************************************************************
			 *******************************************************************************************************************************************************/
			/*
			if(nmContribuinte!=null && !nmContribuinte.trim().equals(""))	{
				ResultSet rsTemp = connectIPT.prepareStatement("SELECT * FROM IPT.sqlADINOM00 "+
                    									       "WHERE P01 = \'"+nmContribuinte+"\'"+
                    									       "  AND (P02 = \'\' OR P02 IS NULL) ").executeQuery();
				if(rsTemp.next())
					return new Result(-150, "Contribuinte com Homônimos!");
			}
			*/
			/* *****************************************************************************************************************************************************
			 *********************************************************** VERIFICA INSCRIÇÃO  ***********************************************************************
			 *******************************************************************************************************************************************************/
			//System.out.println(" \nVerificou ADINOM00: "+((new GregorianCalendar().getTimeInMillis() - init.getTimeInMillis()) / 1000)+"s");
			// GLOBAL ISSBEI97 (CPF/CNPJ, REG, INSC_ MUNICIPAL)
			// GLOBAL ISSBEC00 (INSC,REG) ESTA GLOBAL É O CADASTRO ECONÔMICO MUNICIPAL
			if(gnPessoa == PessoaServices.TP_JURIDICA) {
				rsm = new ResultSetMap(connectISS.prepareStatement("SELECT B.indice1 AS nrInscricaoMunicipal, B.P2 AS nmContribuinte, "+ 
													        	   "       B.P8 AS nmTipoLogradouro, B.P9 AS nmLogradouro, B.P10 AS nrEndereco, " +
													        	   "       B.P11 AS nmComplementoEndereco, B.P12 AS nmBairro, B.P13 AS nrCep "+
													        	   "FROM SQLUser.sqlISSBEI97 A, SQLUser.sqlISSBEC00 B "+
													        	   "WHERE A.indice3 = B.indice1 "+
													        	   "  AND A.indice1 = \'"+nrCpfCnpj+"\'"+
													        	   "  AND B.indice2 = 1 ").executeQuery());
				// Se existir mais de um cadastro para o CPF / CNPJ
				if(rsm.size()>1 || (rsm.size()==0 && qtCadastro>1))
					return new Result(-140, "Pessoa Jurídica com Cadastro em duplicidade!");
				//
				if(rsm.next())	{
					lgEncontrado     = true;
					nrInscMunicipal  = rsm.getString("nrInscricaoMunicipal");
					nmContribuinte   = rsm.getString("nmContribuinte");
					nmTipoLogradouro = rsm.getString("nmTipoLogradouro");
					nmLogradouro 	 = rsm.getString("nmLogradouro");
					nrEndereco       = rsm.getString("nrEndereco");
					nmComplemento    = rsm.getString("nmComplementoEndereco");
					nmBairro         = rsm.getString("nmBairro");
					nrCep            = rsm.getString("nrCep");
				}
				// System.out.println(" \nVerificou Cadastro Financeiro: "+((new GregorianCalendar().getTimeInMillis() - init.getTimeInMillis()) / 1000)+"s");
				// Verifica se existe pendência no cadastro da atividade principal
				ResultSet rsTemp = connectISS.prepareStatement("SELECT * FROM SQLUser.sqlISSBEC00 B "+
	                                                           "WHERE indice1 = \'"+nrInscMunicipal+"\' "+ 
	                                                           "  AND indice2 = 2 "+
						                                       "  AND P1      = \'9999998\'").executeQuery();
				if(rsTemp.next())
					return new Result(-104, "Contribuinte com pendência cadastral!");
			}
			// PESSOA FÍSICA
			else if (!lgEncontrado) {
				rsm = new ResultSetMap(connectISS.prepareStatement("SELECT TOP 1 * FROM SQLUser.sqlISSBEC00 "+
			        	                                           "WHERE P2 = \'"+nrCpfCnpj+"\'").executeQuery());
				// Se existir mais de um cadastro para o CPF / CNPJ
				if(rsm.next()) {
					nmContribuinte   = rsm.getString("P1");
					rsm = new ResultSetMap(connectIPT.prepareStatement("SELECT TOP 1 * FROM IPT.sqlADINOM00 "+
						       									       "WHERE P01 = \'"+nmContribuinte+"\'"+
						       									       "ORDER BY indice1 DESC").executeQuery());
					if(rsm.next()) {
						lgEncontrado = true;
						nmTipoLogradouro = rsm.getString("P03");
						nmLogradouro 	 = rsm.getString("P04");
						nrEndereco       = rsm.getString("P05");
						nmComplemento    = rsm.getString("P06");
						nmBairro         = rsm.getString("P07");
						nrCep            = rsm.getString("P09");
					}
				}
			}
			//
			if(!lgEncontrado) // Se não foi localizado na tabela ADINOM00
				return new Result(-3, "CPF/CNPJ não localizado!");
			/* *****************************************************************************************************************************************************
			 *********************************************************** DIVIDA ATIVA DO MUNICÍPIO - DIAT **********************************************************
			 *******************************************************************************************************************************************************/
			String nmContribuinteSQL =  nmContribuinte.replaceAll("'", "''");
			ResultSet rsTemp;
			// DIVCPF00 (nrCpfCnpj, nmContribuinte, nrAno, BLDA, nrBLDA)
			for(int i=0; i<2; i++)	{
				// Busca as dívidas por CPF / CNPJ
				if(i==0) // Procurando pelo CPF / CNPJ
					rsTemp = connectDIV.prepareStatement("SELECT A.Indice1 AS nrCpfCnpj, A.Indice2 AS nmContribuinte, A.Indice3 AS nrAno, " +
							                             "       A.indice4 dsBlda, A.Indice5 AS nrRegistro "+ 
													 	 "FROM sqlDIVCPF00 A "+
													 	 "WHERE A.indice1 = \'"+nrCpfCnpj+"\'").executeQuery();
				else {
					rsTemp = connectDIV.prepareStatement("SELECT Indice2 AS nrAno, indice3 dsBlda, Indice4 AS nrRegistro "+ 
						 	 							 "FROM SQLUser.sqlDIVCDI01 "+
						 	 							 "WHERE Indice1 = \'"+nmContribuinteSQL+"\'").executeQuery();
				}	
				while(rsTemp.next())	{
					String nrAno      = rsTemp.getString("nrAno");
					String dsBlda     = rsTemp.getString("dsBlda");
					String nrRegistro = rsTemp.getString("nrRegistro");
					/* Verifica se há nofiticação */
					ResultSet rsTmp = connectDIV.prepareStatement("SELECT * FROM sqlDIVCDC00 A, sqlDIVPDC00 B " +
							                                      "WHERE A.indice1 = "+nrAno+
							                                      "  AND A.indice2 = \'"+dsBlda+"\'" +
							                                      "  AND A.indice3 = "+nrRegistro+
							                                      "  AND A.indice4 = 3 " +
							                                      "  AND A.P03     = B.indice1 ").executeQuery();
					if(rsTmp.next())
						continue;
						// return new Result(-115, "Divida com notificação. Ano da Dívida: "+rsTemp.getString("nrAno")+", BLDA: "+rsTemp.getString("dsBlda")+", Nº registro: "+rsTemp.getString("nrRegistro"));
					
					// Dados de parcelamento e situação
					rsTmp = connectDIV.prepareStatement("SELECT A.P10 AS stBLDA, B.P03 AS dtVencimento, A.P07 AS dtPagamento " +
							                            		  "FROM sqlDIVCDC00 A, sqlDIVCDC01 B " +
										                          "WHERE A.indice1  = "  + nrAno      +
										                          "  AND A.indice2  = \'"+ dsBlda     +"\'" +
										                          "  AND A.indice3  = "  + nrRegistro +
										                          "  AND A.indice4 >= 24 " +
										                          "  AND A.P01 = B.indice1 "+
																  "  AND A.P02 = B.indice2 "+
																  "  AND A.P03 = B.indice3 "+
																  "  AND A.P04 = B.indice4 ").executeQuery();
					while(rsTmp.next())	{
						String stBLDA       = rsTmp.getString("stBLDA");
						String dtPagamento  = rsTmp.getString("dtPagamento");
						// String dtVencimento = rsTmp.getString("dtVencimento");
						// BLDA Cancelado
						if(stBLDA!=null && stBLDA.trim().equals("1")) {
							// System.out.println("BLDA Cancelado");
							break;
						}
						if(dtPagamento!=null && dtPagamento.trim().length()>1)
							continue;
						return new Result(-120, "Contribuinte com dívida com parcelamento não quitado! [nrAno: "+nrAno+", BLDA: "+dsBlda+", nrRegistro: "+nrRegistro+"]");
						/* 
						GregorianCalendar venc = new GregorianCalendar(1980, 01, 01);
						int qtDias            = (Integer.valueOf(dtVencimento) - 50769);
						venc.add(Calendar.DATE, qtDias);
						*/
					}
				}
			}
			/* *****************************************************************************************************************************************************
			 *********************************************************** GISS ON-LINE ******************************************************************************
			 *******************************************************************************************************************************************************/
			// ISSGISSDEBI(KEY=INSC,ANO,MÊS,DOC)
			//System.out.println(" \nVerificou débito no ISS-GISS: "+((new GregorianCalendar().getTimeInMillis() - init.getTimeInMillis()) / 1000)+"s");
			if(nrInscMunicipal!=null && !nrInscMunicipal.equals(""))	{
				rsTemp = connectISS.prepareStatement("SELECT * FROM SQLUser.sqlISSGISSDEBI A "+
													 "WHERE A.Indice1      = "+nrInscMunicipal+
													 "  AND NOT EXISTS (SELECT * FROM SQLUser.sqlISSGISSDEBIA B" +
													 "                  WHERE A.Indice1 = B.Indice1 " +
													 "                    AND A.Indice2 = B.Indice2 " +
													 "                    AND A.Indice3 = B.Indice3 " +
													 "                    AND A.Indice4 = B.Indice4)").executeQuery();
				if(rsTemp.next())
					return new Result(-130, "Contribuinte com pendência no GISS-ONLINE!");
			}
			/* *****************************************************************************************************************************************************
			 *********************************************************** TRIBUTOS [INSS, TLL, ETC] *****************************************************************
			 *******************************************************************************************************************************************************/
			// ISSFCC00  (KEY= ANO,INS,TRIB,REG) - ISS, ISSF e Outros
			//System.out.println(" \nVerificou pendência cadastral: "+((new GregorianCalendar().getTimeInMillis() - init.getTimeInMillis()) / 1000)+"s");
			rsTemp = connectISS.prepareStatement("SELECT P2  AS vlParcela1, P3  AS dtVencimento1, P4  AS dtPagamento1, "+ 
												 "       P7  AS vlParcela2, P8  AS dtVencimento2, P9  AS dtPagamento2, "+
												 "       P12 AS vlParcela3, P13 AS dtVencimento3, P14 AS dtPagamento3, "+
												 "       P17 AS vlParcela4, P18 AS dtVencimento4, P19 AS dtPagamento4 "+ 
												 "FROM SQLUser.sqlISSFFC00 "+
												 "WHERE Indice1      = "+nrAnoBase+
												 "  AND Indice2      = \'"+nrInscMunicipal+"\'"+
												 "  AND Indice3 NOT IN (1306) "+ // 1306 = ISSJ que é cobrado pela GISS-ONLINE
												 "  AND Indice4     <> 4").executeQuery();
			while(rsTemp.next())	{
				for(int i=1; i<=4; i++)	{
					String dtVencimento = rsTemp.getString("dtVencimento"+i); 
					String dtPagamento  = rsTemp.getString("dtPagamento"+i);
					// Sem vencimento ou paga
					if(dtVencimento==null || dtVencimento.length()<8 || (dtPagamento!=null && dtPagamento.trim().length()>1))
						continue;
					//
					GregorianCalendar dtVenc = com.tivic.manager.util.Util.stringToCalendar(dtVencimento.substring(0,2)+"/"+dtVencimento.substring(2,4)+"/"+dtVencimento.substring(4,8)+" 23:59:59");
					if(new GregorianCalendar().after(dtVenc))
						return new Result(-105, "Tributo (ISS) vencido!");
				}
			}
			//System.out.println(" \nVerificou ISS: "+((new GregorianCalendar().getTimeInMillis() - init.getTimeInMillis()) / 1000)+"s");
			// ISSFCC01 (KEY=ANO,INS,TRIB,REG) - TLL, ISSP e Outros Tributos
			rsTemp = connectISS.prepareStatement("SELECT P02 AS dtVencimento1, P03 AS dtPagamento1, "+
												 "       P06 AS dtVencimento2, P07 AS dtPagamento2, "+
												 "       P10 AS dtVencimento3, P11 AS dtPagamento3, "+ 
												 "       P14 AS dtVencimento4, P15 AS dtPagamento4, "+
												 "       P18 AS dtVencimento5, P19 AS dtPagamento5, "+
												 "       P22 AS dtVencimento6, P23 AS dtPagamento6 "+ 
												 "FROM SQLUser.sqlISSFFC01 A "+
												 "WHERE Indice1  = "+nrAnoBase+
												 "  AND Indice2  = \'"+nrInscMunicipal+"\'"+
												 "  AND Indice3 <> 2000 "+ // 2000 = Taxa de Expediente
												 "  AND Indice4  < 3 ").executeQuery();
			while(rsTemp.next())
				for(int i=1; i<=6; i++)	{
					String venc  = rsTemp.getString("dtVencimento"+i); 
					String pagto = rsTemp.getString("dtPagamento"+i);
					// Pago
					if(venc==null || venc.length()<8 || (pagto!=null && pagto.trim().length()>1))
						continue;
					// 
					GregorianCalendar dtVenc = com.tivic.manager.util.Util.stringToCalendar(venc.substring(0,2)+"/"+venc.substring(2,4)+"/"+venc.substring(4,8)+" 23:59:59");
					if(new GregorianCalendar().after(dtVenc))
						return new Result(-106, "Tributo[TLL e Outros] vencido e não pago!");
				}
//			System.out.println(" \nVerificou TLL e Outros: "+((new GregorianCalendar().getTimeInMillis() - init.getTimeInMillis()) / 1000)+"s");
			// ISSFF103(KEY=CPF,ANO,SEQ) - Tributos Avulsos
			rsTemp = connectISS.prepareStatement("SELECT A.indice1 AS nrCpf, B.P01 AS dsTributo, B.P13 dtVencimento, C.p01 AS dtPagamento "+ 
												 "FROM sqlISSFF103 A, sqlISSFFC10 B, sqlISSFFC10 C "+
												 "WHERE A.indice1 = \'"+nrCpfCnpj+"\'"+
												 "  AND A.indice2 = "+nrAnoBase+
												 "  AND B.indice1 = A.indice2 "+ // Ano
												 "  AND B.indice2 = A.indice3 "+ // Sequencial
												 "  AND B.indice3 = 2  "+ // Registro 2 
												 "  AND C.indice1 = A.indice2 "+ // Ano
												 "  AND C.indice2 = A.indice3 "+ // Sequencial
												 "  AND C.indice3 = 3"    /* Registro 3*/).executeQuery();
			while(rsTemp.next())	{
				String venc  = rsTemp.getString("dtVencimento"); 
				String pagto = rsTemp.getString("dtPagamento");
				// Pago
				if(venc==null || venc.length()<8 || (pagto!=null && pagto.trim().length()>1))
					continue;
				// Vencido?
				GregorianCalendar dtVenc = com.tivic.manager.util.Util.stringToCalendar(venc.substring(0,2)+"/"+venc.substring(2,4)+"/"+venc.substring(4,8)+" 23:59:59");
				if(new GregorianCalendar().after(dtVenc))
					return new Result(-107, "Tributo AVULSO vencido e não pago!");
			}
			//System.out.println(" \nVerificou tributos AVULSOS: "+((new GregorianCalendar().getTimeInMillis() - init.getTimeInMillis()) / 1000)+"s");
			/* *****************************************************************************************************************************************************
			 *********************************************************** AUTOS DE INFRAÇÃO *************************************************************************
			 *******************************************************************************************************************************************************/
			// EEASITI (INS,AUT,ANO_DO_PROC,PROC)  AUTOS DE INFRAÇÃO - Caso não haja, continua
			String[] situacoes = new String[] {"AUDITORIA-ARQUIVADO", "PAGO-ARQUIVADO-DIAT", "PAGO - ARQUIVADO", "PAGO-ARQUIVADO", "CANCELADO", "PAGO", "ARQUIVADO",   
					                           "CANCELADO AUT. IGR", "CANCELADO AUT IGR", "DIAT", "PARCELADO JUNTO AI", "DEFESA", "PARCELADO NA DIAT" , "PRIMEIRA INSTANCIA",    
					                           "MIGRADO P/PARCELAMTO", "PAGO E ARQUIVADO"};
			rsTemp = connectISS.prepareStatement("SELECT * FROM sqlEEAAUT "+
												 "WHERE indice1 = \'"+nrInscMunicipal+"\'").executeQuery();
			while(rsTemp.next())	{
				// Verifica situação do AUTO
				ResultSet rsSit = connectISS.prepareStatement("SELECT * FROM sqlEEASITI "+
						                                      "WHERE indice1 = \'"+nrInscMunicipal+"\'" +
						                                      "  AND indice2 = \'"+rsTemp.getString("indice2")+"\'" +
						                                      // "  AND indice3 = "+nrAnoBase+
						                                      "ORDER BY indice5 DESC").executeQuery();
				if(rsSit.next())	{
					String stAuto = rsSit.getString("P01")!=null ? rsSit.getString("P01").trim() : "";
					boolean lgOK = false;
					for(int i=0; i<situacoes.length; i++)
						if(stAuto.indexOf("PAGO")>=0 || stAuto.indexOf("ARQUIVADO")>=0 || stAuto.indexOf("CANCELADO")>=0)	{
							lgOK = true;
							break;
						}
					
					if(!lgOK)
						return new Result(-108, "Contribuinte com Autuação! Situação: "+rsSit.getString("P01"));
					else
						continue;
				}
				// Verifica se EXISTE CÁLCULO, se não existir continua
				ResultSet rsCalc = connectISS.prepareStatement("SELECT * FROM sqlEEACALC "+
										                       "WHERE indice1 = \'"+nrInscMunicipal+"\'" +
										                       "  AND indice2 = \'"+rsTemp.getString("indice2")+"\'"/*nrAuto*/).executeQuery();
				if(rsCalc.next()) {
					boolean lgExisteParcelamento = false;
					// Verifica o PARCELAMENTO, se não existir parcelamento não emite CND
					ResultSet rsParc = connectISS.prepareStatement("SELECT A.*, P02 AS dtVencimento, P03 AS dtPagamento FROM sqlEEAPARC A "+
											                       "WHERE indice1 = \'"+nrInscMunicipal+"\'" +
											                       "  AND indice2 = \'"+rsTemp.getString("indice2")+"\'" /* nrAuto */).executeQuery();
					while(rsParc.next())	{
						lgExisteParcelamento = true;
						//
						String venc  = rsParc.getString("dtVencimento"); 
						String pagto = rsParc.getString("dtPagamento");
						// Sem data de vencimento
						if(venc==null || venc.length()<8)
							continue;
						// Se houver parcela em aberto não emite CND
						if(pagto==null || pagto.trim().length()<8)
							return new Result(-110, "Auto com parcelamento que ainda não foi totalmente quitado!");
						// Não precisa verificar se está vencido, porque se estiver em aberto, vencido ou não, não emite CND
						//GregorianCalendar dtVenc = com.tivic.manager.util.Util.stringToCalendar(venc.substring(0,2)+"/"+venc.substring(2,4)+"/"+venc.substring(4,8)+" 23:59:59");
						//if(new GregorianCalendar().after(dtVenc) && (pagto==null || pagto.trim().length()<8))
						//	return new Result(-111, "Parcelamento de AUTO vencido e não pago! Vencimento: "+Util.formatDateTime(dtVenc, "dd/MM/yyyy"));
					}
					if(!lgExisteParcelamento)
						return new Result(-112, "Existe CALCULO para o AUTO, mas não existe o parcelamento! Nº Auto: "+rsTemp.getString("indice2"));
				}
			}
			//System.out.println(" \nVerificou AUTUAÇÕES: "+((new GregorianCalendar().getTimeInMillis() - init.getTimeInMillis()) / 1000)+"s");
			/* *****************************************************************************************************************************************************
			 *********************************************************** IPTU **************************************************************************************
			 *******************************************************************************************************************************************************/
			// Pega
			ArrayList<String> imoveis = new ArrayList<String>();
			for(int i=0; i<2; i++)	{
				if(i==0) // Pega os imóveis pelo CPF / CNPJ
					rsTemp = connectIPT.prepareStatement("SELECT * FROM IPT.sqlIPTBII80 WHERE indice1 = \'"+nrCpfCnpj+"\'").executeQuery();
				else
					rsTemp = connectIPT.prepareStatement("SELECT * FROM IPT.sqlCONTRIBUINTE WHERE nmContribuinte = \'"+nmContribuinteSQL+"\'").executeQuery();
				while(rsTemp.next())	{
					int i1 = 0, i2 = 0, i3 = 0, i4 = 0, i5 = 0;
					String nrImovel = "";
					if(i==0)	{
						nrImovel = rsTemp.getString("Indice2");
						// System.out.println("Imóvel Nº: "+nrImovel);
						if(nrImovel==null || nrImovel.equals(""))
							continue;
						nrImovel = nrImovel.substring(1); // Tira o primeiro zero
						// Tendo certeza que terá 14 dígitos
						while(nrImovel.length() < 14)
							nrImovel = "0" + nrImovel;
						// Separando número de inscrição
						i1 = Integer.parseInt(nrImovel.substring(0, 2));
						i2 = Integer.parseInt(nrImovel.substring(2, 4));
						i3 = Integer.parseInt(nrImovel.substring(4, 7));
						i4 = Integer.parseInt(nrImovel.substring(7, 11));
						i5 = Integer.parseInt(nrImovel.substring(11, 14));
					}
					else	{
						i1 = Integer.parseInt(rsTemp.getString("Indice1"));
						i2 = Integer.parseInt(rsTemp.getString("Indice2"));
						i3 = Integer.parseInt(rsTemp.getString("Indice3"));
						i4 = Integer.parseInt(rsTemp.getString("Indice4"));
						i5 = Integer.parseInt(rsTemp.getString("Indice5"));
						nrImovel = Util.fillNum(i1, 2)+Util.fillNum(i2, 2)+Util.fillNum(i3, 3)+Util.fillNum(i4, 4)+Util.fillNum(i5, 3);
					}
					// Verifica se o imóvel já foi verificado
					for(int l=0; l<imoveis.size(); l++)
						if(imoveis.get(l).equals(nrImovel))
							continue;
						
					// Inclui o imóvel na lista de verificados
					imoveis.add(nrImovel);
					//
					ResultSet rsParc = connectIPT.prepareStatement("SELECT A.IndiceParcela, dtVencimento, B.ID AS dtPagamento FROM IPT.SQLCalculoParcela A "+
																   "LEFT OUTER JOIN IPT.SQLPagamento B ON (A.Indice1 = B.Indice1 AND A.Indice2   = B.Indice2 "+
																   "                                   AND A.Indice3 = B.Indice3 AND A.Indice4   = B.Indice4 "+
																   "                                   AND A.Indice5 = B.Indice5 AND A.IndiceAno = B.IndiceAno "+
																   "                                   AND A.IndiceParcela = B.IndiceParcela) "+
																   "WHERE A.IndiceAno = "+nrAnoBase+
																   "  AND A.Indice1 = "+i1+" AND A.Indice2 = "+i2+
																   "  AND A.Indice3 = "+i3+" AND A.Indice4 = "+i4+
																   "  AND A.Indice5 = "+i5+ 
																   " ORDER BY A.IndiceParcela").executeQuery();
					while(rsParc.next())	{
						String venc  = rsParc.getString("dtVencimento"); 
						String pagto = rsParc.getString("dtPagamento");
						// Sem data de vencimento
						if(venc==null || venc.length()<8)
							continue;
						// Se parcela estiver paga sai
						if(pagto!=null && pagto.trim().length()>0)
							if(rsParc.getInt("IndiceParcela")==0)
								break;     // Se a parcela única foi paga, não precisa verificar as demais
							else
								continue;  // Se a parcela foi paga verifica a próxima
						//
						if(rsParc.getInt("IndiceParcela")==0)
							continue;
						// Verifica se está Vencida
						GregorianCalendar dtVenc = com.tivic.manager.util.Util.stringToCalendar(venc.substring(0,2)+"/"+venc.substring(2,4)+"/"+venc.substring(4,8)+" 23:59:59");
						if(new GregorianCalendar().after(dtVenc))
							return new Result(-113, "Parcela do IPTU vencida! Imóvel Nº: "+nrImovel+", Parcela Nº "+rsParc.getInt("IndiceParcela")+", Vencimento: "+Util.formatDateTime(dtVenc, "dd/MM/yyyy"));
						else
							break; // Se parcela verificar não estiver vencida não precisa continuar verificando as demais pois a data é superior
					}
				}
			}
			//System.out.println(" \nVerificou IPTU: "+((new GregorianCalendar().getTimeInMillis() - init.getTimeInMillis()) / 1000)+"s");
			nrImovelTransferencia = nrImovelTransferencia!=null ? nrImovelTransferencia.replaceAll("[\\.]", "") : "";
			if(tpFinalidade == 1)	{
				rsTemp = connectIPT.prepareStatement("SELECT * FROM IPT.sqlIPTBII80 "+
                                                     "WHERE indice1 = \'"+nrCpfCnpj+"\' " +
                                                     "  AND indice2 = \'"+"0"+nrImovelTransferencia+"\'").executeQuery();
				if(!rsTemp.next())
					return new Result(-15, "Inscrição inválida para o CPF/CNPJ informado!");
				/*
				 *  Validando número da inscrição
				 */
				if(nrImovelTransferencia==null || nrImovelTransferencia.equals(""))
					return new Result(-14, "Informe o número da inscrição do imóvel!");
				/*
				 *  Verificando existência de pagamento
				 */
			}
			//System.out.println(" \nVerificou DIVIDA: "+((new GregorianCalendar().getTimeInMillis() - init.getTimeInMillis()) / 1000)+"s");
			/* *****************************************************************************************************************************************************
			 ************************************************** EMITINDO DOCUMENTO *********************************************************************************
			 *******************************************************************************************************************************************************/
			int cdEmpresa           = 0;
			ResultSet rs = conLocal.prepareStatement("SELECT cd_empresa FROM grl_empresa ").executeQuery();
			if (rs.next())
				cdEmpresa = rs.getInt("cd_empresa");
			// Salvando pessoa
			int cdPessoa = 0;
			rs = conLocal.prepareStatement("SELECT * FROM grl_pessoa_"+(nrCpfCnpj.length()<=11?"fisica ":"juridica ")+
					                       "WHERE "+(gnPessoa==PessoaServices.TP_FISICA ? "nr_cpf " : "nr_cnpj ")+" = \'"+nrCpfCnpj+"\'").executeQuery();
			if(rs.next()) 
				cdPessoa = rs.getInt("cd_pessoa");
			else	{
				cdPessoa = Conexao.getSequenceCode("grl_pessoa", conLocal);
				PreparedStatement pstmt = conLocal.prepareStatement("INSERT INTO grl_pessoa (cd_pessoa, nm_pessoa, dt_cadastro, gn_pessoa) " +
		                 										    "VALUES (?, ?, ?, ?)");
				pstmt.setInt(1, cdPessoa);
				pstmt.setString(2, nmContribuinte);
				pstmt.setTimestamp(3, new Timestamp(new GregorianCalendar().getTimeInMillis()));
				pstmt.setInt(4, gnPessoa);
				pstmt.executeUpdate();
				// Inserindo FISICA / JURÍDICA
				if(nrCpfCnpj.length()<=11)
					pstmt = conLocal.prepareStatement("INSERT INTO grl_pessoa_fisica (cd_pessoa, nr_cpf) VALUES (?, ?)");
				else
					pstmt = conLocal.prepareStatement("INSERT INTO grl_pessoa_juridica (cd_pessoa, nr_cnpj, nr_inscricao_municipal) VALUES (?, ?, ?)");
				pstmt.setInt(1, cdPessoa);
				pstmt.setString(2, nrCpfCnpj);
				if(nrCpfCnpj.length() > 11)
					pstmt.setString(3, nrInscMunicipal); 
				pstmt.executeUpdate();
				// Salvando ENDEREÇO
				int cdTipoLogradouro = 0;
				pstmt  = conLocal.prepareStatement("SELECT * FROM grl_tipo_logradouro " +
                        						   "WHERE nm_tipo_logradouro = \'"+nmTipoLogradouro+"\'");
				rsTemp = pstmt.executeQuery();
				if(rsTemp.next())	
					cdTipoLogradouro = rsTemp.getInt("cd_tipo_logradouro");
				else
					cdTipoLogradouro = TipoLogradouroDAO.insert(new TipoLogradouro(0, nmTipoLogradouro, nmTipoLogradouro), conLocal);
				//
				int cdTipoEndereco   = 0;
				int cdCidade         = ParametroServices.getValorOfParametroAsInteger("CD_CIDADE_END_DEFAULT", 0, cdEmpresa, conLocal);
				pstmt = conLocal.prepareStatement("INSERT INTO grl_pessoa_endereco (cd_pessoa, cd_endereco, cd_tipo_logradouro, cd_tipo_endereco, cd_cidade, nm_logradouro, " +
						                          "                                 nm_bairro, nr_cep, nr_endereco, nm_complemento, lg_principal) " +
						                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1) ");
				pstmt.setInt(1, cdPessoa);
				pstmt.setInt(2, 1 /*cdEndereco*/);
				pstmt.setInt(3, cdTipoLogradouro);
				if(cdTipoEndereco > 0)
					pstmt.setInt(4, cdTipoEndereco);
				else
					pstmt.setNull(4, Types.INTEGER);
				if(cdCidade > 0)
					pstmt.setInt(5, cdCidade);
				else
					pstmt.setNull(5, Types.INTEGER);
				pstmt.setString(6, nmLogradouro);
				pstmt.setString(7, nmBairro);
				pstmt.setString(8, nrCep);
				pstmt.setString(9, nrEndereco);
				pstmt.setString(10, nmComplemento);
				pstmt.executeUpdate();
			}
			int cdTipoDocumento     = getCdTipoDocumentoCND(conLocal);
			// Tipo do DOCUMENTO
			int cdSetor             = getCdSetorCND(cdEmpresa, conLocal);
			int cdSituacaoArquivado = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_ARQUIVADO", 0, 0, conLocal);
			int cdFase              = ParametroServices.getValorOfParametroAsInteger("CD_FASE_INICIAL", 0, 0, conLocal);
			int cdUsuario           = 0;
			String nrDocumento      = TipoDocumentoServices.getNextNumeracao(cdTipoDocumento, cdEmpresa, true, conLocal);
			String idDocumento      = Long.toHexString(new Long(nrCpfCnpj).longValue())+nrDocumento.substring(0, 3)+String.valueOf(new GregorianCalendar().getMaximum(Calendar.SECOND));
			while(idDocumento.length() < 16)
				idDocumento += "0";
			idDocumento = (idDocumento.substring( 0,  4)+"."+
						   idDocumento.substring( 4,  8)+"."+
						   idDocumento.substring( 8, 12)+"."+
						   idDocumento.substring(12, 16)).toUpperCase();
			String txtObservacao = new String[] {"Geral", "Transf. de Imóvel Urbano", "Transf. de Imóvel Rural"}[tpFinalidade] + 
					               (tpFinalidade==1 ? " - "+nrImovelTransferencia : "");
			 
			// DATA DE EMISSÃO E VALIDADE
			GregorianCalendar dtEmissao  = new GregorianCalendar();
			GregorianCalendar dtValidade = (GregorianCalendar)dtEmissao.clone();
			dtValidade.add(Calendar.DATE, 90);
			// Salvando CND
			Documento documento = new Documento(0, 0 /*cdArquivo*/, cdSetor, cdUsuario, "SEFIN" /*nmLocalOrigem*/, dtEmissao /*dtProtocolo*/,
												DocumentoServices.TP_PUBLICO/*tpDocumento*/, txtObservacao, idDocumento, nrDocumento,
												cdTipoDocumento, 0 /*cdServico*/, 0 /*cdAtendimento*/, "" /*txtDocumento*/, cdSetor /*Atual*/,
												cdSituacaoArquivado, cdFase, cdEmpresa, 0 /*cdProcesso*/, 0 /*tpPrioridade*/, 0/*cdDocumentoSuperior*/, 
												null /*dsAssunto*/, null /*nrAtendimento*/, 0/*lgNotificacao*/, 0 /*cdTipoDocumentoAnterior*/, null/*nrDocumentoExterno*/,
												null/*nrAssunto*/,null, null, 0, 1);
			int cdDocumento = DocumentoDAO.insert(documento, conLocal);
			if(cdDocumento <= 0)
				new Result(cdDocumento, "Falha ao salvar CND!");
			
			DocumentoPessoaDAO.insert(new DocumentoPessoa(cdDocumento,cdPessoa,"Solicitante"), conLocal);
			String dsTransferenciaImovel = "Observação: Esta Certidão não tem validade para transferência de Imóvel. Para isto é necessário informar a " +
					                       "Inscrição Imobiliária identificando corretamente o Imóvel a ser transacionado. ";
			System.out.println(" \nVerificou SALVOU: "+((new GregorianCalendar().getTimeInMillis() - init.getTimeInMillis()) / 1000)+"s -> Contribuinte: "+nmContribuinte);
			//
			HashMap<String,Object> parametros = new HashMap<String,Object>();
			parametros.put("nrCND", nrDocumento);
			parametros.put("nmContribuinte", nmContribuinte);
			parametros.put("nrControle", idDocumento);
			parametros.put("dtValidade", Util.formatDate(dtValidade, "dd/MM/yyyy"));
			parametros.put("hrEmissao",  Util.formatDate(dtEmissao, "HH:mm:ss"));
			parametros.put("dtEmissao",  Util.formatDate(dtEmissao, "dd/MM/yyyy"));
			parametros.put("dsEndereco", Util.formatEndereco(nmTipoLogradouro, nmLogradouro, nrEndereco, nmComplemento, nmBairro, "" /*nrCep*/, "" /*nmMunicipio*/, ""/*sgEstado*/, ""));
			parametros.put("labelNatureza",  (gnPessoa==PessoaServices.TP_FISICA ? "Natureza Jurídica: Pessoa Física" : "Natureza Jurídica: Pessoa Jurídica"));
			parametros.put("nrCpfCnpj",      (gnPessoa==PessoaServices.TP_FISICA ? Util.formatCpf(nrCpfCnpj) : Util.formatCnpj(nrCpfCnpj)));
			parametros.put("labelDocumento", (gnPessoa==PessoaServices.TP_FISICA ? "CPF:" : "CNPJ:"));
			parametros.put("labelNome",      (gnPessoa==PessoaServices.TP_FISICA ? "Nome" : "Razão Social"));
			parametros.put("labelEndereco",  (gnPessoa==PessoaServices.TP_FISICA ? "Residência" : "Endereço Tributário"));
			parametros.put("labelInscricao", (tpFinalidade==1 ? "Inscrição do Imóvel: "+Util.format(nrImovelTransferencia, "##.##.###.####.###", true) : " "));
			parametros.put("dsNaoTransferencia", (tpFinalidade==0 ? dsTransferenciaImovel : " "));
			//
			result.setMessage("CND Nº: "+nrDocumento+", Nº Controle: "+idDocumento);
			result.addObject("parametros", parametros);
			result.addObject("resultset", conLocal.prepareStatement("SELECT * FROM grl_pessoa LIMIT 1 ").executeQuery());
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1000, "Falha desconhecida ao tentar gerar CND!", e);
		}
		finally	{
			Conexao.desconectar(conLocal);
			try {
				connectIPT.close();
				connectISS.close();
				connectDIV.close();
			}
			catch(Exception e){}
		}
	}
	
	public static Result validarCND(String nrCpfCnpj, String nrCertidao, String nrControle, GregorianCalendar dtEmissaoInformada)	{
		Connection connect = Conexao.conectar();
		try	{
			Result result = new Result(-1, "Certidão não localizada!");
			int gnPessoa  = PessoaServices.TP_JURIDICA;
			nrCpfCnpj = nrCpfCnpj!=null ? nrCpfCnpj.replaceAll("[\\.]", "").replaceAll("[\\-]", "").replaceAll("[\\/]", "") : "";
			// Verificando CPF
			if(nrCpfCnpj==null || nrCpfCnpj.equals(""))
				return new Result(-1, "Informe o número do CPF ou CNPJ!");
			while(nrCpfCnpj.length()<11)
				nrCpfCnpj = "0"+nrCpfCnpj;
			// Verificando se é um "CPF" Válido
			if(nrCpfCnpj.length()<=11)	{
				gnPessoa  = PessoaServices.TP_FISICA;
				if(!com.tivic.manager.util.Util.isCpfValido(nrCpfCnpj))
					return new Result(-2, "Número do CPF inválido!");
			}
			// CNPJ
			else if(!com.tivic.manager.util.Util.isCNPJ(nrCpfCnpj))
				return new Result(-2, "Número do CNPJ inválido!");
			String tablePessoa = (gnPessoa==PessoaServices.TP_FISICA) ? "fisica" : "juridica";
			String fieldPessoa = (gnPessoa==PessoaServices.TP_FISICA) ? "nr_cpf" : "nr_cnpj";
			//
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, C.nm_pessoa FROM ptc_documento A, ptc_documento_pessoa B, grl_pessoa C, grl_pessoa_"+tablePessoa+" D " +
										                       "WHERE A.cd_documento  = B.cd_documento " +
										                       "  AND B.cd_pessoa     = C.cd_pessoa " +
										                       "  AND B.cd_pessoa     = D.cd_pessoa " +
										                       "  AND A.nr_documento  = ? "+
										                       "  AND A.id_documento  = ? "+
										                       "  AND "+fieldPessoa+" = ? ");
			pstmt.setString(1, nrCertidao);
			pstmt.setString(2, nrControle);
			pstmt.setString(3, nrCpfCnpj);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())	{
				GregorianCalendar dtEmissao  = Util.convTimestampToCalendar(rs.getTimestamp("dt_protocolo"));
				GregorianCalendar dtValidade = Util.convTimestampToCalendar(rs.getTimestamp("dt_protocolo"));
				dtValidade.add(Calendar.DATE, 90);
				//
				result.setCode(1);
				result.addObject("dtEmissao",  Util.formatDate(dtEmissao, "dd/MM/yyyy HH:mm:ss"));
				result.addObject("dtValidade", Util.formatDate(dtValidade, "dd/MM/yyyy"));
				result.addObject("dsFinalidade", rs.getString("txt_observacao"));
				result.addObject("nmContribuinte", rs.getString("nm_pessoa"));
				result.setMessage("Certidão validada com sucesso!");
			}
			else if(dtEmissaoInformada==null) {
				result.setMessage("Você deve informar a data de emissão da CND!");
			}
			else if(dtEmissaoInformada.get(Calendar.YEAR)==2014 && (dtEmissaoInformada.get(Calendar.MONTH)==1 || dtEmissaoInformada.get(Calendar.MONTH)==2)) {
				pstmt = connect.prepareStatement("SELECT C.nm_pessoa FROM grl_pessoa C, grl_pessoa_"+tablePessoa+" D " +
	                       "WHERE C.cd_pessoa     = D.cd_pessoa " +
	                       "  AND "+fieldPessoa+" = ? ");
				pstmt.setString(1, nrCpfCnpj);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					GregorianCalendar dtValidade = (GregorianCalendar)dtEmissaoInformada.clone();
					dtValidade.add(Calendar.DATE, 90);
					
					result.setCode(1);
					result.addObject("dtEmissao",  Util.formatDate(dtEmissaoInformada, "dd/MM/yyyy HH:mm:ss"));
					result.addObject("dtValidade", Util.formatDate(dtValidade, "dd/MM/yyyy"));
					result.addObject("dsFinalidade", "Geral");
					result.addObject("nmContribuinte", rs.getString("nm_pessoa"));
					result.setMessage("Certidão validada com sucesso!");
				}
			}
			return result;
		}	
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1000, "Falha desconhecida ao tentar validar CND!", e);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	private static int getCdTipoDocumentoCND(Connection connect) throws Exception	{
		String nmTipoDocumento = "Certidao Negativa de Debito";
		PreparedStatement pstmt  = connect.prepareStatement("SELECT * FROM ptc_tipo_documento " +
				   						   					 "WHERE nm_tipo_documento = \'"+nmTipoDocumento+"\'");
		ResultSet rs = pstmt.executeQuery();
		if(rs.next())	
			return rs.getInt("cd_tipo_documento");
		else
			return TipoDocumentoDAO.insert(new TipoDocumento(0, nmTipoDocumento, null, com.tivic.manager.ptc.TipoDocumentoServices.TP_INC_SEQ_GERAL,
					0, 0, 0, 0, null, null, 0, 0, 0, null), connect);
		
	}

	private static int getCdSetorCND(int cdEmpresa, Connection connect) throws Exception	{
		String nmSetor = "SEFIN";
		PreparedStatement pstmt  = connect.prepareStatement("SELECT * FROM grl_setor " +
				   						   					 "WHERE nm_setor = \'"+nmSetor+"\'");
		ResultSet rs = pstmt.executeQuery();
		if(rs.next())	
			return rs.getInt("cd_setor");
		else
			return SetorDAO.insert(new Setor(0, 0/*cdSetorSuperior*/, cdEmpresa, 0/*cdResponsavel*/, nmSetor, 1/*stSetor*/, ""/*nmBairro*/,
					                         ""/*nmLogradouro*/, ""/*nrCep*/, ""/*nrEndereco*/, ""/*nmComplemento*/,
					                         ""/*nrTelefone*/, ""/*nmPontoReferencia*/, 0 /*lgEstoque*/,
					                         ""/*nrRamal*/, ""/*idSetor*/, ""/*sgSetor*/, 0/*tpSetor*/, 0/*lgRecepcao*/, null/*cdSetorExterno*/), connect);
	}

}
