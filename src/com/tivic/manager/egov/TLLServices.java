package com.tivic.manager.egov;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import sol.dao.ResultSetMap;
import sol.util.Result;
import com.tivic.manager.adm.TipoDocumento;
import com.tivic.manager.adm.TipoDocumentoDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.grl.TipoLogradouro;
import com.tivic.manager.grl.TipoLogradouroDAO;
import com.tivic.manager.util.Util;

public class TLLServices {

	@SuppressWarnings("resource")
	public static Result getDAM(String nrInscricaoMunicipal)	{
		// GregorianCalendar init = new GregorianCalendar();
		Connection connect  = null;
		Connection conLocal = Conexao.conectar();
		try	{
			connect  = IptuServices.getConnection("ISS");
			// Verificando Ano Base
			int nrAnoBase = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("NR_ANO_BASE", 2014, 0);
			//if(new GregorianCalendar().get(java.util.Calendar.YEAR)!=nrAnoBase)
			//	return new Result(-1, "A TLL do ano de "+(new GregorianCalendar().get(java.util.Calendar.YEAR))+" ainda não foi gerado! Aguarde...");
			/*
			 * DADOS DO CONTRIBUINTE - Validando número da inscrição
			 */
			nrInscricaoMunicipal = nrInscricaoMunicipal!=null ? nrInscricaoMunicipal.replaceAll("[\\.]", "") : "";
			if(nrInscricaoMunicipal==null || nrInscricaoMunicipal.equals(""))
				return new Result(-1, "Informe a Inscrição Municipal!");
			//
			String nmContribuinte   = "";
			String nmTipoLogradouro = "";
			String nmLogradouro 	= "";
			String nrEndereco       = "";
			String nmComplemento    = "";
			String nmBairro         = "";
			String nrCep            = "";
			String nrCpfCnpj        = "";
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT indice1 AS nrInscricaoMunicipal, P2 AS nmContribuinte, "+ 
															        	 "       P8 AS nmTipoLogradouro, P9 AS nmLogradouro, P10 AS nrEndereco, " +
															        	 "       P11 AS nmComplementoEndereco, P12 AS nmBairro, P13 AS nrCep," +
															        	 "       P6 AS nrCpfCnpj "+
															        	 "FROM SQLUser.sqlISSBEC00 "+
															        	 "WHERE indice1 = \'"+nrInscricaoMunicipal+"\'"+
															        	 "  AND indice2 = 1 ").executeQuery());
			//
			Result result = new Result(1);
			if(rsm.next())	{
				result.addObject("resultset", conLocal.prepareStatement("SELECT * FROM grl_pessoa LIMIT 1 ").executeQuery());
				nmContribuinte   = rsm.getString("nmContribuinte");
				nmTipoLogradouro = rsm.getString("nmTipoLogradouro");
				nmLogradouro 	 = rsm.getString("nmLogradouro");
				nrEndereco       = rsm.getString("nrEndereco");
				nmComplemento    = rsm.getString("nmComplementoEndereco");
				nmBairro         = rsm.getString("nmBairro");
				nrCep            = rsm.getString("nrCep");
				nrCpfCnpj        = rsm.getString("nrCpfCnpj");
				nrCpfCnpj = nrCpfCnpj==null ? "" : nrCpfCnpj;
			}
			else
				return new Result(-1, "Nº de Inscrição Inválida!");
			// System.out.println(" \nBuscou dados cadastrais: "+((new GregorianCalendar().getTimeInMillis() - init.getTimeInMillis()) / 1000)+"s");
			// Salvando pessoa
			int cdPessoa = 0;
			int gnPessoa = (nrCpfCnpj.length()>11  ? PessoaServices.TP_JURIDICA : PessoaServices.TP_FISICA);
			ResultSet rs = conLocal.prepareStatement("SELECT * FROM grl_pessoa_"+(gnPessoa==PessoaServices.TP_FISICA?"fisica ":"juridica ")+
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
					pstmt.setString(3, nrInscricaoMunicipal); 
				pstmt.executeUpdate();
				// Salvando ENDEREÇO
				int cdEmpresa           = 0;
				rs = conLocal.prepareStatement("SELECT cd_empresa FROM grl_empresa ").executeQuery();
				if (rs.next())
					cdEmpresa = rs.getInt("cd_empresa");
				int cdTipoLogradouro = 0;
				pstmt  = conLocal.prepareStatement("SELECT * FROM grl_tipo_logradouro " +
                        						   "WHERE nm_tipo_logradouro = \'"+nmTipoLogradouro+"\'");
				ResultSet rsTemp = pstmt.executeQuery();
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
			// System.out.println(" \nSalvou pessoas: "+((new GregorianCalendar().getTimeInMillis() - init.getTimeInMillis()) / 1000)+"s");
			/*
			 *  Recuperando informações dos parametros
			 */
			float prJuros = 0, prMulta = 0;
			float vlJuros = 0, vlMulta = 0, vlTaxaExpediente = 0;
			float vlTotal = vlTaxaExpediente;
			int nrParcela = -1;
			String dsMensagem = "", nrDAM = "";
			GregorianCalendar dtVencimentoDAM = null;
			try	{
				prMulta             = Float.parseFloat(com.tivic.manager.grl.ParametroServices.getValorOfParametro("PR_MULTA_DAM_IPTU"));
				prJuros             = Float.parseFloat(com.tivic.manager.grl.ParametroServices.getValorOfParametro("PR_JUROS_DAM_IPTU"));
				vlTaxaExpediente    = Float.parseFloat(com.tivic.manager.grl.ParametroServices.getValorOfParametro("VL_TAXA_EXPEDIENTE"));
			}
			catch(Exception e) { };
			ResultSetMap rsmTributos  = new ResultSetMap();
			ResultSetMap rsmTributosR = new ResultSetMap();
			/*
			 *  Buscando TLL
			 */
			rs = connect.prepareStatement("SELECT Indice3 AS cdTributo, " +
					                      "       P01 AS vlParcela1, P02 AS dtVencimento1, P03 AS dtPagamento1, "+
										  "       P05 AS vlParcela2, P06 AS dtVencimento2, P07 AS dtPagamento2, "+
										  "       P09 AS vlParcela3, P10 AS dtVencimento3, P11 AS dtPagamento3, "+ 
										  "       P13 AS vlParcela4, P14 AS dtVencimento4, P15 AS dtPagamento4, "+
										  "       P17 AS vlParcela5, P18 AS dtVencimento5, P19 AS dtPagamento5, "+
										  "       P21 AS vlParcela6, P22 AS dtVencimento6, P23 AS dtPagamento6 "+
										  "FROM SQLUser.sqlISSFFC01 A "+
										  "WHERE Indice1  = "+nrAnoBase+
										  "  AND Indice2  = \'"+nrInscricaoMunicipal+"\'"+
										  "  AND Indice3  IN (2101, 2103) ").executeQuery();
			int qtParcela = 0;
			while(rs.next())	{
				for(int i=1; i<=6; i++)	{
					String venc  = rs.getString("dtVencimento"+i); 
					String pagto = rs.getString("dtPagamento"+i);
					// Se houver os dois pontos no campo do valor, não é uma parcela
					// Se a data de vencimento não existe dá o loop, se a data de pagamento estiver informada já foi PAGO
					if(rs.getString("vlParcela"+i)==null || rs.getString("vlParcela"+i).indexOf(":")>=0 ||
					   venc==null || venc.length()<8 || (pagto!=null && pagto.trim().length()>1)	) 
					{
						continue;
					}
					// Só gera uma parcela do tributo
					if(rs.getInt("cdTributo")!=2101) {
						qtParcela++;
						if(qtParcela > 1)
							continue;
					}
					GregorianCalendar dtVencimento = com.tivic.manager.util.Util.convStringToCalendar(venc.substring(0,2)+"/"+venc.substring(2,4)+"/"+venc.substring(4,8)+" 23:59:59");;
					if(dtVencimentoDAM == null)
						dtVencimentoDAM = (GregorianCalendar)dtVencimento.clone(); 
					float vlTributo  = rs.getFloat("vlParcela"+i) / 100; // Valor já vem sem casa decimais
					vlTotal         += vlTributo;
					if(vlTributo > 0) {
						String nmTributo = rs.getInt("cdTributo")==2101 ? "TLL - TAXA DE LICENÇA PARA FUNCIONAMENTO" : "ISSP - ISS SOCIEDAD.PROFISSIONAIS";
						String idTributo = rs.getInt("cdTributo")==2101 ? "TLL - TAXA DE LICENCA PARA FUNCIONAM" : "ISSP - ISS SOCIEDAD.PROFISSIONAIS";
						if(nrParcela == -1)
							nrParcela = i;
						//
						HashMap<String,Object> register = new HashMap<String,Object>();
						register.put("CD_TRIBUTO", Util.fillNum(rsmTributos.size()+1, 2));
						register.put("ID_TRIBUTO", idTributo);
						register.put("NM_TRIBUTO", nmTributo+" (Venc.: "+Util.formatDate(dtVencimento, "dd/MM/yyyy")+")");
						register.put("VL_TRIBUTO", vlTributo);
						rsmTributos.addRegister(register);
						rsmTributosR.addRegister(register);
						// Calculando juros da parcela em questão
						if(!dtVencimento.after(new GregorianCalendar()))	{
							// Multa
							vlMulta    += (prMulta * vlTotal / 100);
							// Juros
							int qtMeses = (int)((new GregorianCalendar().getTimeInMillis() - dtVencimento.getTimeInMillis()) / 1000 / 60 / 60 / 24 / 30);
							vlJuros    += ((prJuros*qtMeses) * vlTotal / 100);
							//
							dtVencimentoDAM = IptuServices.addDiasUteis(new GregorianCalendar(), 3, null);
							dsMensagem = "Recalculada em "+com.tivic.manager.util.Util.formatDateTime(new GregorianCalendar(), "dd/MM/yyyy")+" para pagamento até o dia "+
										 com.tivic.manager.util.Util.formatDateTime(dtVencimentoDAM, "dd/MM/yyyy")+".";
						}
					}
				}
			}
			// Taxa de Expediente
			if(vlTaxaExpediente > 0) {
				HashMap<String,Object> register = new HashMap<String,Object>();
				register.put("CD_TRIBUTO", Util.fillNum(rsmTributos.size()+1, 2));
				register.put("ID_TRIBUTO", "EXPE - TARIFA DE EXPEDIENTE");
				register.put("NM_TRIBUTO", "TE - TAXA DE EXPEDIENTE");
				register.put("VL_TRIBUTO", vlTaxaExpediente);
				rsmTributos.addRegister(register);
				rsmTributosR.addRegister(register);
			}
			/*
			 *  OUTROS TRIBUTOS
			 */
			rs = connect.prepareStatement("SELECT P2  AS vlParcela1, P3  AS dtVencimento1, P4  AS dtPagamento1, "+ 
										  "       P7  AS vlParcela2, P8  AS dtVencimento2, P9  AS dtPagamento2, "+
										  "       P12 AS vlParcela3, P13 AS dtVencimento3, P14 AS dtPagamento3, "+
										  "       P17 AS vlParcela4, P18 AS dtVencimento4, P19 AS dtPagamento4," +
										  "       Indice3 AS cdTributo " +
										  "FROM SQLUser.sqlISSFFC00 A "+
										  "WHERE Indice1  = "+nrAnoBase+
										  "  AND Indice2  = \'"+nrInscricaoMunicipal+"\'"+
										  "  AND Indice3  = 1305 ").executeQuery(); // 1305 = IISF
			qtParcela = 0;
			while(rs.next())	{
				for(int i=1; i<=4; i++)	{
					//
					String venc  = rs.getString("dtVencimento"+i); 
					String pagto = rs.getString("dtPagamento"+i);
					// Se a data de vencimento ou valor não existir dá o loop, se a data de pagamento estiver informada já foi PAGO
					if(rs.getString("vlParcela"+i)==null || venc==null || venc.length()<8 || (pagto!=null && pagto.trim().length()>1))
						continue;
					// Só gera uma parcela do tributo
					qtParcela++;
					if(qtParcela > 1)
						break;
					GregorianCalendar dtVencimento = com.tivic.manager.util.Util.convStringToCalendar(venc.substring(0,2)+"/"+venc.substring(2,4)+"/"+venc.substring(4,8)+" 23:59:59");;
					if(dtVencimentoDAM == null)
						dtVencimentoDAM = (GregorianCalendar)dtVencimento.clone(); 
					float vlTributo  = rs.getFloat("vlParcela"+i) / 100; // Valor já vem sem casa decimais
					vlTotal         += vlTributo;
					if(vlTributo > 0) {
						if(nrParcela == -1)
							nrParcela = i;
						//
						String nmTributo = "ISSF - IMPOSTO SOBRE SERVICOS OFICIO";
						HashMap<String,Object> register = new HashMap<String,Object>();
						register.put("CD_TRIBUTO", Util.fillNum(rsmTributos.size()+1, 2));
						register.put("ID_TRIBUTO", nmTributo);
						register.put("NM_TRIBUTO", nmTributo+" (Venc.: "+Util.formatDate(dtVencimento, "dd/MM/yyyy")+")");
						register.put("VL_TRIBUTO", vlTributo);
						rsmTributos.addRegister(register);
						rsmTributosR.addRegister(register);
						// Calculando juros da parcela em questão
						if(!dtVencimento.after(new GregorianCalendar()))	{
							// Multa
							vlMulta    += (prMulta * vlTotal / 100);
							// Juros
							int qtMeses = (int)((new GregorianCalendar().getTimeInMillis() - dtVencimento.getTimeInMillis()) / 1000 / 60 / 60 / 24 / 30);
							vlJuros    += ((prJuros*qtMeses) * vlTotal / 100);
							//
							dtVencimentoDAM = IptuServices.addDiasUteis(new GregorianCalendar(), 3, null);
							dsMensagem = "Recalculada em "+com.tivic.manager.util.Util.formatDateTime(new GregorianCalendar(), "dd/MM/yyyy")+" para pagamento até o dia "+
										 com.tivic.manager.util.Util.formatDateTime(dtVencimentoDAM, "dd/MM/yyyy")+".";
						}
					}
				}
			}
			// System.out.println("\nVerificou: ISS-F: "+((new GregorianCalendar().getTimeInMillis() - init.getTimeInMillis()) / 1000)+"s");
			if(rsmTributos.size()==0)
				return new Result(-1, "Não existe tributo do ano base "+nrAnoBase+" em aberto!");
			// Multa
			if(vlMulta > 0) {
				HashMap<String,Object> register = new HashMap<String,Object>();
				register.put("CD_TRIBUTO", Util.fillNum(rsmTributos.size()+1, 2));
				register.put("ID_TRIBUTO", "MULTA");
				register.put("NM_TRIBUTO", "MULTA");
				register.put("VL_TRIBUTO", vlMulta);
				rsmTributos.addRegister(register);
			}
			// Juros
			if(vlJuros > 0) {
				HashMap<String,Object> register = new HashMap<String,Object>();
				register.put("CD_TRIBUTO", Util.fillNum(rsmTributos.size()+1, 2));
				register.put("ID_TRIBUTO", "JUROS");
				register.put("NM_TRIBUTO", "JUROS");
				register.put("VL_TRIBUTO", vlJuros);
				rsmTributos.addRegister(register);
			}
			/*
			 *  NÚMERO DO DAM
			 */
			rs = connect.prepareStatement("SELECT * FROM sqlISSBII10 " +
					                      "WHERE indice1 = "+nrInscricaoMunicipal+
					                      "  AND indice2 = "+nrAnoBase+
					                      "  and indice3 = "+nrParcela).executeQuery();
			if(rs.next())	{
				nrDAM = rs.getString("P01");
				while(nrDAM!=null && nrDAM.substring(0,1).equals("0"))
					nrDAM = nrDAM.substring(1);
			}
			else
				return new Result(-1, "Não existe documento de arrecadação para essa inscrição. Comunique a SEFIN!");
			// System.out.println("\nBuscou número DAM. "+((new GregorianCalendar().getTimeInMillis() - init.getTimeInMillis()) / 1000)+"s");
			/*
			 *  CONTA A RECEBER - Registrando emissão
			 */
			int cdEmpresa = 0;
			ResultSet rsTemp = conLocal.prepareStatement("SELECT * FROM grl_empresa").executeQuery();
			if(rsTemp.next())
				cdEmpresa = rsTemp.getInt("cd_empresa");
			int cdTipoDocumento = 0;
			rsTemp = conLocal.prepareStatement("SELECT * FROM adm_tipo_documento WHERE sg_tipo_documento = \'TLL\'").executeQuery();
			if(rsTemp.next())
				cdTipoDocumento = rsTemp.getInt("cd_tipo_documento");
			else
				cdTipoDocumento = TipoDocumentoDAO.insert(new TipoDocumento(0,"Taxa de Licenciamento", "TLL", "TLL", 1 /*stTipoDocumento*/, 0, "99"), conLocal);
			int cdContaReceber = Conexao.getSequenceCode("adm_conta_receber", conLocal);
			PreparedStatement pstmt = conLocal.prepareStatement("INSERT INTO adm_conta_receber (cd_conta_receber,cd_pessoa,cd_empresa,"+
			                                                    "nr_documento,nr_parcela,dt_vencimento,dt_emissao,vl_abatimento,vl_acrescimo,vl_conta" +
			                                                    ",cd_tipo_documento) " +
			                                                    "VALUES (?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, cdContaReceber);
			pstmt.setInt(2, cdPessoa);
			pstmt.setInt(3, cdEmpresa);
			pstmt.setString(4, nrInscricaoMunicipal);
			pstmt.setInt(5, 0/*nrParcela*/);
			pstmt.setTimestamp(6, new Timestamp(dtVencimentoDAM.getTimeInMillis()));
			pstmt.setTimestamp(7, new Timestamp(new GregorianCalendar().getTimeInMillis())); // dtEmissao
			pstmt.setFloat(8, vlTotal);
			pstmt.setFloat(9, 0 /*vlDesconto*/);
			pstmt.setFloat(10, (vlMulta + vlJuros));
			pstmt.setInt(11, cdTipoDocumento);
			pstmt.executeUpdate();
			// System.out.println("\nRegistrou conta a receber: "+((new GregorianCalendar().getTimeInMillis() - init.getTimeInMillis()) / 1000)+"s");
			/*
			 *  ISSFFC01DAM - Registrando emissão
			 */
			// Excluindo
			pstmt = connect.prepareStatement("DELETE FROM sqlISSFFC01DAM WHERE IndiceAno = ? AND IndiceInscricao = ? AND IndiceNossoNumero = ? AND IndiceParcela = ? ");
			pstmt.setString(1, String.valueOf(nrAnoBase));
			pstmt.setString(2, nrInscricaoMunicipal);
			pstmt.setString(3, nrDAM);
			pstmt.setString(4, String.valueOf(nrParcela));
			pstmt.execute();
			//
			String sql    = "INSERT INTO sqlISSFFC01DAM (IndiceAno, IndiceInscricao, IndiceNossoNumero, IndiceParcela ";
			String values = " VALUES (?, ?, ?, ?";
			rsmTributos.beforeFirst();
			while(rsmTributos.next()) {
				sql    += ",Tributo"+(rsmTributos.getPosition()+1)+",Valor"+(rsmTributos.getPosition()+1);
				values += ",?,?";
			}
			sql += ") "+values+")";
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, String.valueOf(nrAnoBase));
			pstmt.setString(2, nrInscricaoMunicipal);
			pstmt.setString(3, nrDAM);
			pstmt.setString(4, String.valueOf(nrParcela));
			rsmTributos.beforeFirst();
			int l = 5;
			while(rsmTributos.next()) {
				pstmt.setString(l, rsmTributos.getString("ID_TRIBUTO"));
				pstmt.setString(l+1, String.valueOf(Math.round(rsmTributos.getFloat("VL_TRIBUTO") * 100)));
				l += 2;
			}
			pstmt.executeUpdate();
			/*
			 *  ATIVIDADES
			 */
			pstmt = connect.prepareStatement("SELECT * FROM SQLUser.sqlISSAEC00 " +
					                         "WHERE indice1 = ?");
			ResultSetMap rsmServicos = new ResultSetMap();
			rsTemp = connect.prepareStatement("SELECT * FROM SQLUser.sqlISSBEC00 B "+
                    						  "WHERE indice1 = \'"+nrInscricaoMunicipal+"\' "+ 
                    						  "  AND indice2 = 2 ").executeQuery();
			if(rsTemp.next()) {
				for (int i=1; i<9; i+=2)
					if(rsTemp.getString("P"+i)!=null && rsTemp.getString("P"+(i+1))!=null) {
						pstmt.setString(1, rsTemp.getString("P"+i));
						ResultSet rsServ = pstmt.executeQuery();
						String nmAtividade = "";
						float  prAliquota  = 0;
						if(rsServ.next()) {
							nmAtividade = rsServ.getString("P01");
							prAliquota  = rsServ.getString("P04")!=null ? Float.parseFloat(rsServ.getString("P04")) / 10000 : 0;
 						}
						//
						HashMap<String,Object> register = new HashMap<String,Object>();
						// Data
						String dtAtividade = rsTemp.getString("P"+(i+1));
						if(dtAtividade.length()<8) 
							dtAtividade = dtAtividade.substring(0,2)+"/"+dtAtividade.substring(2,4)+"/19"+dtAtividade.substring(4,6);
						else
							dtAtividade = dtAtividade.substring(0,2)+"/"+dtAtividade.substring(2,4)+"/"+dtAtividade.substring(4,8);
						// Código
						String cdAtividade = rsTemp.getString("P"+i); 
						while(cdAtividade.length() < 10)
							cdAtividade = "0"+cdAtividade;
						//
						register.put("CD_ATIVIDADE", cdAtividade);
						register.put("NM_ATIVIDADE", nmAtividade);
						register.put("DT_ATIVIDADE", dtAtividade);
						register.put("PR_ALIQUOTA", prAliquota);
						rsmServicos.addRegister(register);
					}
			}
			// System.out.println("\nBuscou atividades: "+((new GregorianCalendar().getTimeInMillis() - init.getTimeInMillis()) / 1000)+"s");
			result.addObject("rsmServicos", rsmServicos.getLines());
			result.addObject("rsmTributos", rsmTributos.getLines());
			result.addObject("rsmTributosR", rsmTributosR.getLines());
			//
			result.addObject("nrDAM", nrDAM);
			result.addObject("nrParcela", nrParcela);
			result.addObject("nrInscricaoMunicipal", nrInscricaoMunicipal.subSequence(0, nrInscricaoMunicipal.length()-1)+"-"+nrInscricaoMunicipal.subSequence(nrInscricaoMunicipal.length()-1, nrInscricaoMunicipal.length()));
			result.addObject("nmContribuinte", nmContribuinte);
			result.addObject("dsEndereco", Util.formatEndereco(nmTipoLogradouro, nmLogradouro, nrEndereco, nmComplemento, nmBairro, "" /*nrCep*/, "" /*nmMunicipio*/, ""/*sgEstado*/, ""));
			result.addObject("nmTipoLogradouro", nmTipoLogradouro);
			result.addObject("nmLogradouro", nmLogradouro);
			result.addObject("nrEndereco", nrEndereco);
			result.addObject("nmComplemento", nmComplemento);
			result.addObject("nmBairro", nmBairro);
			result.addObject("nrCep", nrCep);
			result.addObject("nmCidade", "VITÓRIA DA CONQUISTA");
			result.addObject("sgEstado", "BA");
			result.addObject("nrCpfCnpj",      (gnPessoa==PessoaServices.TP_FISICA ? Util.formatCpf(nrCpfCnpj) : Util.formatCnpj(nrCpfCnpj)));
			result.addObject("dsMensagem", dsMensagem);
			// Descrição dos Serviços
			// Dados da TLL
			result.addObject("nrAnoBase", nrAnoBase);
			result.addObject("dtVencimento", Util.formatDate(dtVencimentoDAM, "dd/MM/yyyy"));
			// Valores
			result.addObject("vlJuros", vlJuros>0 ? new DecimalFormat("#,##0.00;-#,##0.00", new java.text.DecimalFormatSymbols(new java.util.Locale("pt", "BR"))).format(vlJuros) : "");
			result.addObject("vlMulta", vlMulta>0 ? new DecimalFormat("#,##0.00;-#,##0.00", new java.text.DecimalFormatSymbols(new java.util.Locale("pt", "BR"))).format(vlMulta) : "");
			result.addObject("vlTotal", vlTotal);
			result.addObject("vlTotalGeral", vlTotal + vlJuros + vlMulta);
			//
			result.addObject("rs", conLocal.prepareStatement("SELECT * FROM grl_pessoa LIMIT 1").executeQuery());
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar gerar o DAM/TLL", e);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static String[] getCodigoBarras(String nrDAM, float vlParcela, String venc, int nrParcela)	{
		Connection conISS = null;
		try	{
			conISS = IptuServices.getConnection("ISS");
			//
			GregorianCalendar dtVencimento = com.tivic.manager.util.Util.convStringToCalendar(venc);
			final String TIPO_SISTEMA = "2"; // ISS e TLL's
				
			nrDAM = com.tivic.manager.util.Util.fill(nrDAM, 14, '0', 'E') + TIPO_SISTEMA + Util.fillNum(nrParcela, 2); 
			// Formando código de barras
			String nrCodigoBarras = "8"+ // 8 = Impostos
									"1"+ //
									"7"+ // Valor/Referência: 7 = Referência
									com.tivic.manager.util.Util.fillNum(Math.round(vlParcela * 100), 11)+ // Valor da parcela com 11 posições
									"4785"+ // Código FEBRABAN
									com.tivic.manager.util.Util.formatDate(dtVencimento, "yyyyMMdd")+
									nrDAM;
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
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(conISS);
		}
	}
}
