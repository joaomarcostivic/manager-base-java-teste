package com.tivic.manager.egov;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.StringTokenizer;

import sol.dao.ResultSetMap;
import sol.util.Result;
import com.intersys.jdbc.*;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.*;


public class IptuServices {
	public static GregorianCalendar dtLimiteDecreto = new GregorianCalendar(2013, 11, 20, 23, 59, 59);
	
	public static Result getLista()	{
		Connection connect = Conexao.conectar();
		try	{
			ResultSet rs = connect.prepareStatement("SELECT nr_documento FROM adm_conta_receber").executeQuery();
			String dsFile = "";
			while(rs.next()){
				dsFile += rs.getString("nr_documento")+"\n";
			}
			Result result = new Result(1);
			result.addObject("file", dsFile);
			return result;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar gerar lista de DAM´s impressos!", e);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static String[] getCodigoBarras(String nrInscricao, int nrAnoBase, int nrParcela)	{
		Connection connect = null;
		try	{
			nrInscricao = nrInscricao!=null ? nrInscricao.replaceAll("[\\.]", "") : "";
			connect     = getConnection("IPT");
			nrAnoBase   = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("NR_ANO_BASE", 2012);

			// Separando número de inscrição
			while(nrInscricao.length()<14)
				nrInscricao = "0"+nrInscricao;

			int i1 = Integer.parseInt(nrInscricao.substring(0, 2));
			int i2 = Integer.parseInt(nrInscricao.substring(2, 4));
			int i3 = Integer.parseInt(nrInscricao.substring(4, 7));
			int i4 = Integer.parseInt(nrInscricao.substring(7, 11));
			int i5 = Integer.parseInt(nrInscricao.substring(11, 14));
			// Recuperando informações dos parametros
			float prJuros = 0, prMulta = 0, prDescontoCotaUnica = 0, vlTaxaExpediente = 0;
			GregorianCalendar dtLimiteDesconto = null;
			try	{
				prMulta             = Float.parseFloat(com.tivic.manager.grl.ParametroServices.getValorOfParametro("PR_MULTA_DAM_IPTU"));
				prJuros             = Float.parseFloat(com.tivic.manager.grl.ParametroServices.getValorOfParametro("PR_JUROS_DAM_IPTU"));
				prDescontoCotaUnica = Float.parseFloat(com.tivic.manager.grl.ParametroServices.getValorOfParametro("PR_DESCONTO_COTA_UNICA"));
				vlTaxaExpediente    = Float.parseFloat(com.tivic.manager.grl.ParametroServices.getValorOfParametro("VL_TAXA_EXPEDIENTE"));
				if(com.tivic.manager.grl.ParametroServices.getValorOfParametro("DT_LIMITE_DESCONTO")!=null)
					dtLimiteDesconto    = com.tivic.manager.util.Util.convStringToCalendar(com.tivic.manager.grl.ParametroServices.getValorOfParametro("DT_LIMITE_DESCONTO")+" 23:59");
			}
			catch(Exception e){};
			if(nrAnoBase == 2013)
				prDescontoCotaUnica = 0;
			
			String sql = "SELECT B.IndiceParcela, B.nmCampo2, B.vlParcela, SUBSTRING(B.dtVencimento,1,8) AS dtVencimento  " +
					     "FROM IPT.SQLContribuinte A, IPT.SQLCalculoParcela B " +
	                     "WHERE A.indice1 = "+i1+" AND A.indice2 = "+i2+" AND A.indice3 = "+i3+" AND A.indice4 = "+i4+" AND A.indice5 = "+i5+
	                     "  AND B.indice1 = "+i1+" AND B.indice2 = "+i2+" AND B.indice3 = "+i3+" AND B.indice4 = "+i4+" AND B.indice5 = "+i5+
	                     "  AND B.IndiceAno = "+nrAnoBase+
	                     (nrParcela>=0 ? "  AND B.IndiceParcela = "+nrParcela : "");
			ResultSet rs = connect.prepareStatement(sql).executeQuery();
			if(rs.next()){
				String venc = rs.getString("dtVencimento");
				venc = venc.substring(0,2)+"/"+venc.substring(2,4)+"/"+venc.substring(4,8)+" 23:59:59";
				GregorianCalendar dtVencimento = com.tivic.manager.util.Util.convStringToCalendar(venc);
				// DECRETRO 2013
				if(nrAnoBase==2013 && new GregorianCalendar().before(dtLimiteDecreto))
					dtVencimento = dtLimiteDecreto;
				
				float vlParcela = rs.getFloat("vlParcela"); // Valor já vem sem casa decimais (*100)
				float vlBase    = vlParcela;
				// MULTA E JUROS
				if(!dtVencimento.after(new GregorianCalendar()) && (dtLimiteDesconto==null || !dtLimiteDesconto.after(new GregorianCalendar())))	{
					// Multa
					vlParcela += (prMulta * vlBase / 100);
					// Juros
					int qtMeses = (int)((new GregorianCalendar().getTimeInMillis() - dtVencimento.getTimeInMillis()) / 1000 / 60 / 60 / 24 / 30);
					//if(qtMeses == 0)
					//	qtMeses = 1;
					vlParcela += ((prJuros*qtMeses) * vlBase / 100);
					//
					dtVencimento = addDiasUteis(new GregorianCalendar(), 3, null);
				}
				// DESCONTO
				else if(nrParcela==0)	{
					vlParcela -= Math.round(vlBase * prDescontoCotaUnica / 100);
					if(dtLimiteDesconto!=null)
						dtVencimento = dtLimiteDesconto;
				}
				// TAXA DE EXPEDIENTE
				vlParcela += Math.round(vlTaxaExpediente * 100);
				// final String PREFIXO_DAM  = "5561400";
				final String TIPO_SISTEMA = "1";

				String nrDamIPTU = com.tivic.manager.util.Util.fillAlpha(nrInscricao, 14)+TIPO_SISTEMA+com.tivic.manager.util.Util.fillNum(nrParcela, 2);
				// Formando código de barras
				String nrCodigoBarras = "8"+ // 8 = Impostos
										"1"+ //
										"6"+ // Valor/Referência: 7 = Referência
										com.tivic.manager.util.Util.fillNum(Math.round(vlParcela), 11)+ // Valor da parcela com 11 posições
										"4785"+ // Código FEBRABAN
										com.tivic.manager.util.Util.formatDate(dtVencimento, "yyyyMMdd")+
										nrDamIPTU;
				// Acrescentando dígito verificador

				nrCodigoBarras = nrCodigoBarras.subSequence(0,3)+
								 String.valueOf(com.tivic.manager.util.Util.getDvMod10(nrCodigoBarras))+
				                 nrCodigoBarras.substring(3, 43);
				String bloco1 = nrCodigoBarras.subSequence(0, 11)+"-"+String.valueOf(com.tivic.manager.util.Util.getDvMod10((String)nrCodigoBarras.subSequence(0,11)));
				String bloco2 = nrCodigoBarras.subSequence(11,22)+"-"+String.valueOf(com.tivic.manager.util.Util.getDvMod10((String)nrCodigoBarras.subSequence(11,22)));
				String bloco3 = nrCodigoBarras.subSequence(22,33)+"-"+String.valueOf(com.tivic.manager.util.Util.getDvMod10((String)nrCodigoBarras.subSequence(22,33)));
				String bloco4 = nrCodigoBarras.subSequence(33,44)+"-"+String.valueOf(com.tivic.manager.util.Util.getDvMod10((String)nrCodigoBarras.subSequence(33,44)));

				return new String[] {nrCodigoBarras, bloco1, bloco2, bloco3, bloco4};
			}
			return new String[] {};
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getDadosOfInscricao(String nrInscricao, String nrCpf, int nrAno)	{
		try	{
			return new ResultSetMap((ResultSet)getDadosOfInscricao(nrInscricao, nrCpf, nrAno, -1).getObjects().get("resultset"));
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
	}

	@SuppressWarnings("resource")
	public static Result getDadosOfInscricao(String nrInscricao, String nrCpfCnpj, int nrAnoBase, int nrParcela)	{
		Connection connect = Conexao.conectar();
		try	{
			//
			nrInscricao = nrInscricao!=null ? nrInscricao.replaceAll("[\\.]", "") : "";
			connect   = getConnection("IPT");
			nrAnoBase = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("NR_ANO_BASE", 2011, 0);
			// Verificando CPF
			if(nrCpfCnpj==null || nrCpfCnpj.equals(""))
				return new Result(-1, "Informe o número do CPF ou CNPJ!");
			while(nrCpfCnpj.length()<11)
				nrCpfCnpj = "0"+nrCpfCnpj;
			// Verificando se é um Cpf Válido
			if(nrCpfCnpj.length()<11 && !com.tivic.manager.util.Util.isCpfValido(nrCpfCnpj))
				return new Result(-1, "Número do CPF inválido!");
			// CNPJ
			if(nrCpfCnpj.length()>11 && !com.tivic.manager.util.Util.isCNPJ(nrCpfCnpj))
				return new Result(-1, "Número do CNPJ inválido!");
			if(new GregorianCalendar().get(Calendar.YEAR)!=nrAnoBase)
				return new Result(-1, "O IPTU do ano de "+(new GregorianCalendar().get(Calendar.YEAR))+" ainda não foi gerado! Aguarde...");
			// Salvando pessoa
			Connection conLocal = Conexao.conectar();
			int cdPessoa = 0;
			ResultSet rs = conLocal.prepareStatement("SELECT * FROM grl_pessoa_"+(nrCpfCnpj.length()<=11?"fisica ":"juridica ")+
					                                 "WHERE "+(nrCpfCnpj.length()<=11?"nr_cpf ":"nr_cnpj ")+" = \'"+nrCpfCnpj+"\'").executeQuery();
			if(rs.next())
				cdPessoa = rs.getInt("cd_pessoa");
			else	{
				cdPessoa = Conexao.getSequenceCode("grl_pessoa", conLocal);
				PreparedStatement pstmt = conLocal.prepareStatement("INSERT INTO grl_pessoa (cd_pessoa, nm_pessoa, dt_cadastro, gn_pessoa) " +
		                 										   "VALUES (?, ?, ?, ?)");
				pstmt.setInt(1, cdPessoa);
				pstmt.setString(2, "CONTRIBUINTE CPF/CNPJ: "+nrCpfCnpj);
				pstmt.setTimestamp(3, new Timestamp(new GregorianCalendar().getTimeInMillis()));
				pstmt.setInt(4, nrCpfCnpj.length()<=11 ? 1 /*Física*/ : 0 /*Jurídica*/);
				pstmt.executeUpdate();
				// Inserindo pessoa física ou jurídica
				if(nrCpfCnpj.length()<=11)
					pstmt = conLocal.prepareStatement("INSERT INTO grl_pessoa_fisica (cd_pessoa, nr_cpf) VALUES (?, ?)");
				else
					pstmt = conLocal.prepareStatement("INSERT INTO grl_pessoa_juridica (cd_pessoa, nr_cnpj) VALUES (?, ?)");
				pstmt.setInt(1, cdPessoa);
				pstmt.setString(2, nrCpfCnpj);
				pstmt.executeUpdate();
			}
			/*
			 *  Recuperando informações dos parametros
			 */
			float prJuros = 0, prMulta = 0, prDescontoCotaUnica = 0;
			float vlJuros = 0, vlMulta = 0, vlDescontoCotaUnica = 0, vlTaxaExpediente = 0;
			String dsMensagem = "";
			GregorianCalendar dtLimiteDesconto = null;
			try	{
				prMulta             = Float.parseFloat(com.tivic.manager.grl.ParametroServices.getValorOfParametro("PR_MULTA_DAM_IPTU"));
				prJuros             = Float.parseFloat(com.tivic.manager.grl.ParametroServices.getValorOfParametro("PR_JUROS_DAM_IPTU"));
				prDescontoCotaUnica = Float.parseFloat(com.tivic.manager.grl.ParametroServices.getValorOfParametro("PR_DESCONTO_COTA_UNICA"));
				vlTaxaExpediente    = Float.parseFloat(com.tivic.manager.grl.ParametroServices.getValorOfParametro("VL_TAXA_EXPEDIENTE"));
				if(com.tivic.manager.grl.ParametroServices.getValorOfParametro("DT_LIMITE_DESCONTO")!=null)
					dtLimiteDesconto    = com.tivic.manager.util.Util.convStringToCalendar(com.tivic.manager.grl.ParametroServices.getValorOfParametro("DT_LIMITE_DESCONTO")+" 23:59");

			}catch(Exception e){};
			if(nrAnoBase == 2013)
				prDescontoCotaUnica = 0;

			/*
			 *  Validando número da inscrição
			 */
			if(nrInscricao==null || nrInscricao.equals(""))
				return new Result(-1, "Informe o número da inscrição do imóvel!");
			while(nrInscricao.length()<14)
				nrInscricao = "0"+nrInscricao;
			// Separando número de inscrição
			int i1 = Integer.parseInt(nrInscricao.substring(0, 2));
			int i2 = Integer.parseInt(nrInscricao.substring(2, 4));
			int i3 = Integer.parseInt(nrInscricao.substring(4, 7));
			int i4 = Integer.parseInt(nrInscricao.substring(7, 11));
			int i5 = Integer.parseInt(nrInscricao.substring(11, 14));
			/*
			 *  Verificando existência de pagamento
			 */
			String sql = "SELECT * FROM IPT.SQLPagamento " +
		     			 "WHERE indice1 = "+i1+" AND indice2 = "+i2+" AND indice3 = "+i3+" AND indice4 = "+i4+" AND indice5 = "+i5+
		     			 "  AND IndiceAno   = "+nrAnoBase+
		     			 (nrParcela>=0 ? "  AND IndiceParcela = "+nrParcela : "");
			rs = connect.prepareStatement(sql).executeQuery();
			if(rs.next())
				return new Result(-1, "O pagamento da parcela "+(nrParcela==0?"unica":"nº "+nrParcela)+", ano base "+nrAnoBase+", ja foi registrado!");
			/*
			 *  Verificando existência no banco
			 */
			sql = "SELECT B.IndiceParcela, B.nmCampo2, B.vlParcela, SUBSTRING(B.dtVencimento,1,8) AS dtVencimento  " +
     			  "FROM IPT.SQLContribuinte A, IPT.SQLCalculoParcela B " +
     			  "WHERE A.indice1 = "+i1+" AND A.indice2 = "+i2+" AND A.indice3 = "+i3+" AND A.indice4 = "+i4+" AND A.indice5 = "+i5+
     			  "  AND B.indice1 = "+i1+" AND B.indice2 = "+i2+" AND B.indice3 = "+i3+" AND B.indice4 = "+i4+" AND B.indice5 = "+i5+
     			  "  AND B.IndiceAno   = "+nrAnoBase+
     			 (nrParcela>=0 ? "  AND B.IndiceParcela = "+nrParcela : "");
			rs = connect.prepareStatement(sql).executeQuery();
			if(rs.next())	{
				/*
				 * CALCULOS
				 */
				String venc = rs.getString("dtVencimento");
				venc = venc.substring(0,2)+"/"+venc.substring(2,4)+"/"+venc.substring(4,8)+" 23:59:59";
				GregorianCalendar dtVencimento = com.tivic.manager.util.Util.convStringToCalendar(venc);
				// DECRETRO 2013
				if(nrAnoBase==2013 && new GregorianCalendar().before(dtLimiteDecreto))	{
					dtVencimento = dtLimiteDecreto;
					dsMensagem = "Recalculado em "+com.tivic.manager.util.Util.formatDateTime(new GregorianCalendar(), "dd/MM/yyyy")+" para pagamento até o dia "+
							     com.tivic.manager.util.Util.formatDateTime(dtVencimento, "dd/MM/yyyy")+".";
				}
				//
				float vlParcela = rs.getFloat("vlParcela") / 100; // Valor já vem sem casa decimais
				float vlBase    = vlParcela;
				// MULTA E JUROS
				if(!dtVencimento.after(new GregorianCalendar()) && (dtLimiteDesconto==null || !dtLimiteDesconto.after(new GregorianCalendar())))	{
					// Multa
					vlMulta = (prMulta * vlBase / 100);
					// Juros
					int qtMeses = (int)((new GregorianCalendar().getTimeInMillis() - dtVencimento.getTimeInMillis()) / 1000 / 60 / 60 / 24 / 30);
					// if(qtMeses == 0)
					//	 qtMeses = 1;
					vlJuros += ((prJuros*qtMeses) * vlBase / 100);
					//
					dtVencimento = addDiasUteis(new GregorianCalendar(), 3, null);
					dsMensagem = "Recalculado em "+com.tivic.manager.util.Util.formatDateTime(new GregorianCalendar(), "dd/MM/yyyy")+" para pagamento até o dia "+
								 com.tivic.manager.util.Util.formatDateTime(dtVencimento, "dd/MM/yyyy")+".";
				}
				// DESCONTO
				else if(nrParcela==0)	{
					vlDescontoCotaUnica = vlBase * prDescontoCotaUnica / 100;
					if(dtLimiteDesconto!=null && dtLimiteDesconto.after(new GregorianCalendar()))
						dsMensagem = "O pagamento da parcela única com desconto foi prorrogado até o dia "+
						             com.tivic.manager.util.Util.formatDateTime(dtLimiteDesconto, "dd/MM/yyyy")+".";
				}
				//
				int cdEmpresa = 0;
				ResultSet rsTemp = conLocal.prepareStatement("SELECT * FROM grl_empresa").executeQuery();
				if(rsTemp.next())
					cdEmpresa = rsTemp.getInt("cd_empresa");
				int cdTipoDocumento = 0;
				rsTemp = conLocal.prepareStatement("SELECT * FROM adm_tipo_documento").executeQuery();
				if(rsTemp.next())
					cdTipoDocumento = rsTemp.getInt("cd_tipo_documento");
				// Salvando conta a receber emitida
				int cdContaReceber = Conexao.getSequenceCode("adm_conta_receber", conLocal);
				PreparedStatement pstmt = conLocal.prepareStatement("INSERT INTO adm_conta_receber (cd_conta_receber,cd_pessoa,cd_empresa,"+
				                                                   "nr_documento,nr_parcela,dt_vencimento,dt_emissao,vl_abatimento,vl_acrescimo,vl_conta" +
				                                                   ",cd_tipo_documento) " +
				                                                   "VALUES (?,?,?,?,?,?,?,?,?,?,?)");
				pstmt.setInt(1, cdContaReceber);
				pstmt.setInt(2, cdPessoa);
				pstmt.setInt(3, cdEmpresa);
				pstmt.setString(4, nrInscricao);
				pstmt.setInt(5, nrParcela);
				pstmt.setTimestamp(6, new Timestamp(dtVencimento.getTimeInMillis()));
				pstmt.setTimestamp(7, new Timestamp(new GregorianCalendar().getTimeInMillis())); // dtEmissao
				pstmt.setFloat(8, vlParcela);
				pstmt.setFloat(9, vlDescontoCotaUnica);
				pstmt.setFloat(10, vlMulta + vlJuros);
				pstmt.setInt(11, cdTipoDocumento);
				pstmt.executeUpdate();
			}
			else
				return new Result(-1, "Nenhum imóvel encontrado para a inscrição informada!");
			// Dados do contribuinte
			sql =        "SELECT TOP 1 \'"+nrInscricao+"\' AS nrInscricao, \'"+dsMensagem+"\' AS dsMensagem, "+
			             "       "+vlTaxaExpediente+" AS vlTaxaExpediente, " +
					     "      "+(vlDescontoCotaUnica)+" AS vlDescontoCotaUnica, "+vlJuros+" AS vlJuros, "+vlMulta+" AS vlMulta, "+
					     "       A1.nmCampo28/100 AS vlAreaTerreno, A1.nmCampo30/100 AS vlAreaConstrucao, A1.nmCampo2 AS tpCategoria, "+
			             "       A.*, B.nrAnoBase, B.tpCategoria, B.vlTerreno, B.vlM2Terreno, B.vlAreaTerreno, "+
			             "       B.nmCampo5/100 AS vlFatorCorrTerreno, B.nmCampo6, B.nmCampo7, "+
					     "       B.nmCampo13, B.nmCampo14, B.vlM2Construcao, B.vlConstrucao, B.vlFatorCorrConstrucao, B.vlVenal, " +
					     "       B.vlC1C4 AS prAliquota, B.vlC2C4," +
					     "       B.vlC3C4, B.vlC4C4, C.IndiceAno, B.nrAnoBase, " +
					     "       C.IndiceParcela, C.nmCampo2, C.vlParcela, C.vlTaxaExpediente, SUBSTRING(C.dtVencimento,1,8) AS dtVencimento," +
					     "       D.nmTipoLogradouro AS nmTipoLogradouroImovel, D.nmLogradouro AS nmLogradouroImovel, " +
					     "       A.nmCampo4 AS nrEndereco, A.nmCampo5 AS nmComplementoImovel, E.nmBairro AS nmBairroImovel, F.indice6 AS nrSorte, " +
					     "       F.indice6 AS nrSorteT " +
					     "FROM IPT.SQLContribuinte A " +
					     "JOIN IPT.IPTBIC0101               A1 ON (A1.indice1 = "+i1+" AND A1.indice2 = "+i2+" AND A1.indice3 = "+i3+" AND A1.indice4 = "+i4+" AND A1.indice5 = "+i5+" AND A1.indice6 = 1) " +
					     "JOIN IPT.SQLViewCalculo           B ON (B.indice1 = "+i1+" AND B.indice2 = "+i2+" AND B.indice3 = "+i3+" AND B.indice4 = "+i4+" AND B.indice5 = "+i5+") " +
					     "JOIN IPT.SQLCalculoParcela        C ON (C.indice1 = "+i1+" AND C.indice2 = "+i2+" AND C.indice3 = "+i3+" AND C.indice4 = "+i4+" AND C.indice5 = "+i5+" AND C.IndiceAno = B.nrAnoBase) "+
					     "LEFT OUTER JOIN IPT.SQLLogradouro D ON (A.nmCampo2 = D.indice1) " +
					     "JOIN IPT.IPTBLC01                 E ON (A.nmCampo2 = E.indice3 AND E.indice4||E.indice5 = A.nmCampo3) " +
					     "LEFT OUTER JOIN IPT.SQLSorte      F ON (F.indice1 = A.indice1 AND F.indice2 = A.indice2 AND F.indice3 = A.indice3 AND F.indice4 = A.indice4 AND F.indice5 = A.indice5 AND F.indiceAno = "+nrAnoBase+") "+
	                     "WHERE A.indice1 = "+i1+" AND A.indice2 = "+i2+" AND A.indice3 = "+i3+" AND A.indice4 = "+i4+" AND A.indice5 = "+i5+
	                     "  AND B.nrAnoBase   = "+nrAnoBase+
	                     (nrParcela>=0 ? "  AND C.IndiceParcela = "+nrParcela : "");
			return new Result(1, "Processado com sucesso!", "resultset", connect.prepareStatement(sql).executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static Connection getConnection(String namespace) throws SQLException, ClassNotFoundException	{
		// CACHE5: jdbc:Cache://192.168.10.2:1972/IPT
		// com.intersys.jdbc.CacheDriver
		// Usuário
		String url      = com.tivic.manager.grl.ParametroServices.getValorOfParametro("URL_BD_NFE").replaceAll("IPT", "")+namespace;
		String username = com.tivic.manager.grl.ParametroServices.getValorOfParametro("USER_BD_NFE");
		String password = com.tivic.manager.grl.ParametroServices.getValorOfParametro("PASS_BD_NFE");
		CacheDataSource ds = new CacheDataSource();
		ds.setURL(url);
		ds.setUser(username);
		ds.setPassword(password);
		return ds.getConnection();
	}

	public static Result saveParametros(float prMulta, float prJuros, float vlTaxaExpediente, float prDescontoCotaUnica, int lgPermiteDamSemCpf, int lgSomenteParcelaUnica,
										int nrAnoBase, String dtLimiteDesconto,
			                            String urlDB, String driverDB, String userDB, String passDB)
	{
		Connection conn = Conexao.conectar();
		try	{
			// Parametros
			String[] params = new String[] {"PR_MULTA_DAM_IPTU", "PR_JUROS_DAM_IPTU", "VL_TAXA_EXPEDIENTE","PR_DESCONTO_COTA_UNICA",
					                        "LG_PERMITE_DAM_SEM_CPF", "LG_SOMENTE_PARCELA_UNICA", "NR_ANO_BASE", "DT_LIMITE_DESCONTO",
											"URL_BD_NFE", "DRIVER_BD_NFE", "USER_BD_NFE", "PASS_BD_NFE"};
			// Valores
			Object[] values = new Object[] {prMulta, prJuros, vlTaxaExpediente, prDescontoCotaUnica, lgPermiteDamSemCpf, lgSomenteParcelaUnica, nrAnoBase, dtLimiteDesconto,
					                        urlDB, driverDB, userDB, passDB};
			PreparedStatement pstmt = conn.prepareStatement("SELECT cd_parametro FROM grl_parametro WHERE nm_parametro = ?");
			// Multa
			for(int i=0; i<params.length; i++)	{
				pstmt.setString(1, params[i]);
				ResultSet rs = pstmt.executeQuery();
				int cdParametro = 0;
				if(rs.next())	{
					cdParametro = rs.getInt("cd_parametro");
					if(values[i] instanceof Float)
						ParametroServices.setValoresOfParametro(cdParametro, new Object[]{((Float)values[i]).floatValue()});
					else
						ParametroServices.setValoresOfParametro(cdParametro, new Object[]{values[i]});
				}
				else
					System.out.println("Parametro não localizado: "+params[i]);
			}
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar salvar parametros", e);
		}
		finally	{
			Conexao.desconectar(conn);
		}
		return new Result(1);
	}

	public final static String MASK_CURRENCY = "#,###,##0.00";
	public static String formatNumber(float number)	{
		DecimalFormatSymbols decimalformat = new DecimalFormatSymbols();
		decimalformat.setMonetaryDecimalSeparator(',');
		decimalformat.setGroupingSeparator('.');
		DecimalFormat formatador = new DecimalFormat(MASK_CURRENCY, decimalformat);
		return formatador.format(number);
	}

	/**
     * Adiciona o(a) dias uteis.
     *
     * @param data o(a) data
     * @param nrDias o(a) número do(a) dias
     * @param connect o(a) connect
     *
     * @return gregorian calendar
     */
    public static GregorianCalendar addDiasUteis(GregorianCalendar data, int nrDias, Connection connect) {
    	boolean isConnectionNull = connect==null;
    	if(isConnectionNull)
    		connect = Conexao.conectar();
    	try	{
	    	while(nrDias>0)	{
	    		data.add(Calendar.DATE, 1);
	    		if(data.get(Calendar.DAY_OF_WEEK)>1 && data.get(Calendar.DAY_OF_WEEK)<7 /*&& !com.tivic.manager.grl.FeriadoServices.isFeriado(data, connect)*/)
	    			nrDias--;
	    	}
	    	return data;
    	}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			System.err.println("Erro! Feriado.isFeriado: " +  e);
			return null;
		}
		finally{
	    	if(isConnectionNull)
	    		Conexao.desconectar(connect);
		}
    }

 	/**
	  * Exec sql.
	  *
	  * @param sql o(a) sql
	  *
	  * @return result set map
	  */
	 public static ResultSetMap execSQL(String sql, String namespace)	{
		Connection connect = null; 
		try {
			connect = getConnection(namespace);
			ResultSetMap rsm = new ResultSetMap();;
			StringTokenizer tokens = new StringTokenizer(sql, ";");
			while(tokens.hasMoreElements())	{
				sql = tokens.nextToken();
				if(sql.toUpperCase().indexOf("UPDATE")>=0 || sql.toUpperCase().indexOf("CREATE")>=0 ||
				   sql.toUpperCase().indexOf("INSERT")>=0 || sql.toUpperCase().indexOf("ALTER")>=0 ||
				   sql.toUpperCase().indexOf("DELETE")>=0 || sql.toUpperCase().indexOf("DROP")>=0)
				{
					rsm = new ResultSetMap();
					int ret = connect.prepareStatement(sql).executeUpdate();
					HashMap<String,Object> register = new HashMap<String,Object>();
					register.put("RETORNO", ret+" rows commited.");
					rsm.addRegister(register);
				}
				else
					rsm = new ResultSetMap(connect.prepareStatement(sql).executeQuery());
			}
			return rsm;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			System.err.println("Erro! Util.execSQL: " +  e);
			StackTraceElement[] st = e.getStackTrace();
			String log = e.getMessage()+"\n";
			for(int i=0; i<st.length; i++)
				log += "\t at "+st[i].getClassName()+"."+st[i].getMethodName()+"("+st[i].getFileName()+":"+st[i].getLineNumber()+") \n";
			log += "\n"+e.getMessage();
			//
			ResultSetMap rsm = new ResultSetMap();
			HashMap<String,Object> register = new HashMap<String,Object>();
			register.put("RETORNO", log);
			rsm.addRegister(register);
			return rsm;
		}
		finally{
			if(connect!=null)
				Conexao.desconectar(connect);
		}
	}
}
