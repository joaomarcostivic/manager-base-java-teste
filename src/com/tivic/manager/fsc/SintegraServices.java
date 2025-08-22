package com.tivic.manager.fsc;

import java.util.GregorianCalendar;
import java.sql.*;
import java.util.*;
// import br.gov.sintegra.ie.estados.InscricaoEstadualBA;
import com.tivic.manager.adm.TributoAliquotaServices;
import com.tivic.manager.adm.TributoServices;
import com.tivic.manager.alm.*;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.*;
import com.tivic.manager.util.Util;
import sol.dao.*;

public class SintegraServices	{
	public static final String[] estrutura = new String[] {"Selecione...",
		                                                   "Convênio ICMS 57/95 na versão do Convênio ICMS 31/99",
	                                                       "Convênio ICMS 57/95 na versão do Convênio ICMS 69/02 e 142/02",
	                                                       "Convênio ICMS 57/95 na versão do Convênio ICMS 76/03"};
	
	public static final String[] natureza = new String[] {"Selecione...",
		                                                  "Interestaduais, somente operações de Substituição Tributária",
														  "Interestaduais, operações com ou sem Substituição Tributária",
	 													  "Totalidade das operações do informante"};
	 
	public static final String[] finalidade = new String[] {"Selecione...",
		                                                    "Normal",
															"Retificação total de arquivo: substituição total de informações prestadas do período",
	                                                        "Retificação aditiva de arquivo: acréscimo de informação não incluída em arquivos já apresentados",
	                                                        "Desfazimento: arquivo de informação referente a operações/prestações não efetivadas"};
	
	public static String gerarSintegra(int cdEmpresa, int nrMes, int nrAno)	{
		return gerarSintegra(cdEmpresa, nrMes, nrAno, 3, 3, 1);
	}
	
	public static String gerarSintegra(int cdEmpresa, int nrMes, int nrAno, int tpEstrutura, int tpNatureza, int tpFinalidade)	{
		GregorianCalendar dtInicial = new GregorianCalendar(nrAno, nrMes-1, 1);
		GregorianCalendar dtFinal   = (GregorianCalendar)dtInicial.clone();
		dtFinal.set(Calendar.DAY_OF_MONTH, dtFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return gerarSintegra(cdEmpresa, dtInicial, dtFinal, tpEstrutura, tpNatureza, tpFinalidade);
	}
	
	public static String gerarSintegra(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int tpEstrutura, int tpNatureza, int tpFinalidade)	{
		int cdContato = ParametroServices.getValorOfParametroAsInteger("CD_CONTATO", 0, cdEmpresa);		
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSetMap rsmLog = new ResultSetMap();
		HashMap<String,Object> reg = new HashMap<String,Object>();
		try {
			if(cdContato == 0){
				reg.put("CD_ERRO","00");
				reg.put("DS_MENSAGEM","Contato não configurado nos parametros!");
				rsmLog.addRegister(reg);
			}
			if(dtFinal.after(new GregorianCalendar()))
				dtFinal = new GregorianCalendar();
			if (dtInicial==null || dtFinal==null || dtFinal.before(dtInicial))	{
				reg.put("CD_ERRO","01");
				reg.put("DS_MENSAGEM","Data final ou inicial inválida! [dtInicial="+Util.formatDateTime(dtInicial, "dd/MM/yyyy")+
						",dtFinal="+Util.formatDateTime(dtFinal, "dd/MM/yyyy")+"]");
				rsmLog.addRegister(reg);
				
			}
			/**
			 *  Modificar após a homologacao
			 */
			dtInicial = new GregorianCalendar(dtInicial.get(Calendar.YEAR), dtInicial.get(Calendar.MONTH), dtInicial.get(Calendar.DATE), 0, 0, 0);
			dtFinal   = new GregorianCalendar(dtFinal.get(Calendar.YEAR), dtFinal.get(Calendar.MONTH), dtFinal.get(Calendar.DATE), 23, 59, 59);
			/*********************************************************************************************************
			 * Pesquisando código dos tributos                                                                       *
			 * *******************************************************************************************************/
			int cdTributoICMS      = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0);
			if(cdTributoICMS <= 0) {
				ResultSet rs = connect.prepareStatement("SELECT * FROM adm_tributo WHERE id_tributo = \'ICMS\'").executeQuery();
				if(rs.next())
					cdTributoICMS = rs.getInt("cd_tributo");
			}
				
			int cdTributoIPI       = 0;
			ResultSet rs = connect.prepareStatement("SELECT * FROM adm_tributo WHERE id_tributo = \'IPI\'").executeQuery();
			if(rs.next())
				cdTributoIPI = rs.getInt("cd_tributo");			
			System.out.println(/**********************************************************************************************************/);
			System.out.println(/** REGISTRO TIPO 10 - Dados do Informante																 */);
			System.out.println(/**********************************************************************************************************/);
			Empresa empresa                      = EmpresaDAO.get(cdEmpresa, connect);
			PessoaJuridica pessoaJuridicaEmpresa = PessoaJuridicaDAO.get(cdEmpresa, connect);
			PessoaEndereco enderecoEmpresa       = PessoaEnderecoServices.getEnderecoPrincipal(cdEmpresa, connect);
			// InscricaoEstadualBA ieBa             = new InscricaoEstadualBA();
			if (empresa==null)	{
				reg.put("CD_ERRO","02");
				reg.put("DS_MENSAGEM","Empresa não localizada [cdEmpresa="+cdEmpresa+"]!");
				rsmLog.addRegister(reg);
			}
			Cidade  cidade  = CidadeDAO.get(EmpresaServices.getCidadeOfEmpresa(empresa.getCdEmpresa(), connect), connect);
			if (cidade==null)	{
				reg.put("CD_ERRO","03");
				reg.put("DS_MENSAGEM","Cidade da empresa não localizada!");
				rsmLog.addRegister(reg);
			}
			Estado  estado  = cidade!=null?EstadoDAO.get(cidade.getCdEstado(), connect):null;
			if (estado==null)	{
				reg.put("CD_ERRO","04");
				reg.put("DS_MENSAGEM","Estado da empresa não localizado!");
				rsmLog.addRegister(reg);
			}
			if (empresa.getNrCnpj()==null || empresa.getNrCnpj().equals("") || !Util.isCNPJ(empresa.getNrCnpj()))	{
				reg.put("CD_ERRO","05");
				reg.put("DS_MENSAGEM","CNPJ ausente ou incorreto!");
				rsmLog.addRegister(reg);
			}
			//Verificar se a empresa é da Bahia
			if (empresa.getNrInscricaoEstadual()==null || empresa.getNrInscricaoEstadual().equals("")/* || !ieBa.validar(empresa.getNrInscricaoEstadual())*/)	{
				reg.put("CD_ERRO","06");
				reg.put("DS_MENSAGEM","Inscrição estadual ausente ou in!");
				rsmLog.addRegister(reg);
			}
			//
			String nrIEEmp = pessoaJuridicaEmpresa.getNrInscricaoEstadual()==null ? "" : pessoaJuridicaEmpresa.getNrInscricaoEstadual();
			String nrFax   = empresa.getNrFax()==null?"":empresa.getNrFax().replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("-", "");
			String nrEnd   = enderecoEmpresa==null || enderecoEmpresa.getNrEndereco()==null?"":Util.limparFormatos(enderecoEmpresa.getNrEndereco(), 'N');
			String r10 = "10"+
							Util.fill(empresa.getNrCnpj(), 14, '0', 'E')+                 // nrCnpj
							Util.fill(nrIEEmp.trim(), 14, ' ', 'D')+                                // Inscrição Estadual
			                Util.fillAlpha(Util.limparTexto(pessoaJuridicaEmpresa.getNmRazaoSocial().trim()), 35)+ // Nome da Empresa
							Util.fillAlpha(Util.limparTexto(cidade==null?"":cidade.getNmCidade().trim()), 30)+     // Municipio (sede da emrpesa)
			                Util.fillAlpha(estado.getSgEstado().trim(), 2)+                      // sgUf
			                Util.fill(nrFax, 10, '0', 'E')+                               // Fax
			                Util.formatDateTime(dtInicial, "yyyyMMdd")+                   // Data inicial do período
			                Util.formatDateTime(dtFinal, "yyyyMMdd")+					  // Data final do período
			                tpEstrutura+
			                tpNatureza+
			                tpFinalidade+"\r\n";
			System.out.println(/**********************************************************************************************************/);
			System.out.println(/** REGISTRO TIPO 11 - Dados Complementares																 */);
			System.out.println(/**********************************************************************************************************/);
			Pessoa contato       = PessoaDAO.get(cdContato, connect);
			String nrCep         = enderecoEmpresa==null || enderecoEmpresa.getNrCep()==null ? "" : enderecoEmpresa.getNrCep().replaceAll("\\.", "").replaceAll("-", "");
			String nrTel         = pessoaJuridicaEmpresa==null || pessoaJuridicaEmpresa.getNrTelefone1()==null?"":pessoaJuridicaEmpresa.getNrTelefone1().replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("-", "");
			String nmLogradouro  = enderecoEmpresa == null || enderecoEmpresa.getNmLogradouro()==null?"":enderecoEmpresa.getNmLogradouro();
			String nmComplemento = enderecoEmpresa == null || enderecoEmpresa.getNmComplemento()==null?"":enderecoEmpresa.getNmComplemento();
			String nmBairro      = enderecoEmpresa == null || enderecoEmpresa.getNmBairro()==null?"":enderecoEmpresa.getNmBairro();
			String nmPessoa      = contato == null || contato.getNmPessoa()==null?"":contato.getNmPessoa();		
			if(nrCep.equals(""))
				nrCep = "45000000";
			if (nrCep.trim().equals(""))	{
				reg.put("CD_ERRO","07");
				reg.put("DS_MENSAGEM","REGISTRO 11: CEP ausente!");
				rsmLog.addRegister(reg);
			}
			if (nrTel.trim().equals(""))	{
				reg.put("CD_ERRO","08");
				reg.put("DS_MENSAGEM","REGISTRO 11: Telefone ausente!");
				rsmLog.addRegister(reg);
			}
			if (nmLogradouro.trim().equals(""))	{
				reg.put("CD_ERRO","09");
				reg.put("DS_MENSAGEM","REGISTRO 11: Logradouro ausente!");
				rsmLog.addRegister(reg);
			}
			if (nrEnd.trim().equals(""))	{
				reg.put("CD_ERRO","10");
				reg.put("DS_MENSAGEM","REGISTRO 11: nrDoc do Endereco ausente!");
				rsmLog.addRegister(reg);
			}
			if (nmComplemento.trim().equals(""))	{
				reg.put("CD_ERRO","11");
				reg.put("DS_MENSAGEM","REGISTRO 11: Complemento ausente!");
				rsmLog.addRegister(reg);
			}
			if (nmBairro.trim().equals(""))	{
				reg.put("CD_ERRO","12");
				reg.put("DS_MENSAGEM","REGISTRO 11: Bairro ausente!");
				rsmLog.addRegister(reg);
			}
			if (nmPessoa.trim().equals(""))	{
				reg.put("CD_ERRO","13");
				reg.put("DS_MENSAGEM","REGISTRO 11: Nome do contato ausente!");
				rsmLog.addRegister(reg);
			}
			
			String r11 = "11"+
							Util.fillAlpha(Util.limparTexto(nmLogradouro.trim()), 34)+
					        Util.fill(nrEnd.trim(), 5, '0', 'E')+
							Util.fillAlpha(Util.limparTexto(nmComplemento.trim()), 22)+
					        Util.fillAlpha(Util.limparTexto(nmBairro.trim()), 15)+
					        Util.fill(nrCep.trim(), 8, '0', 'E')+
					        Util.fillAlpha(Util.limparTexto(nmPessoa), 28)+
					        Util.fill(nrTel.trim(), 12, '0', 'E')+"\r\n";
			System.out.println(/**********************************************************************************************************/);
			System.out.println(/** REGISTRO TIPO 50 - Registro tipo 50 - Notas Fiscais de Compra/Venda						             */);
			System.out.println(/** *******************************************************************************************************/);
			pstmt = connect.prepareStatement("SELECT A.cd_documento_entrada, A.dt_documento_entrada, A.nr_documento_entrada, A.vl_total_documento, B.nr_cnpj, B.nr_inscricao_estadual, " +
					                         "       A.tp_documento_entrada, E.sg_estado, F.nr_codigo_fiscal, I.pr_aliquota, H.st_tributaria," +
					                         "       SUM(I.vl_base_calculo) AS vl_total_base_calculo, SUM(I.pr_aliquota * I.vl_base_calculo / 100) AS vl_total_tributo  " +
					                         "FROM alm_documento_entrada A " +
											 " LEFT OUTER JOIN grl_pessoa_juridica       B ON (A.cd_fornecedor = B.cd_pessoa) " +
											 " LEFT OUTER JOIN grl_pessoa_endereco       C ON (A.cd_fornecedor = C.cd_pessoa AND lg_principal = 1) " +
											 " LEFT OUTER JOIN grl_cidade                D ON (C.cd_cidade     = D.cd_cidade) " +
											 " LEFT OUTER JOIN grl_estado                E ON (E.cd_estado     = D.cd_estado) " +
											 " LEFT OUTER JOIN adm_natureza_operacao     F ON (A.cd_natureza_operacao = F.cd_natureza_operacao) " +
											 " LEFT OUTER JOIN adm_entrada_item_aliquota I ON (I.cd_documento_entrada = A.cd_documento_entrada" +
											 "                                             AND I.cd_tributo           = "+cdTributoICMS+") " +
											 " LEFT OUTER JOIN adm_tributo_aliquota      H ON (H.cd_tributo_aliquota = I.cd_tributo_aliquota " +
											 "                                             AND H.cd_tributo          = I.cd_tributo " +
											 "										       AND H.st_tributaria       <> "+TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA+") "+
											 "WHERE A.cd_empresa           = "+cdEmpresa+
											 "  AND A.tp_documento_entrada IN ("+DocumentoEntradaServices.TP_NOTA_FISCAL+","+DocumentoEntradaServices.TP_NOTA_ELETRONICA+")"+
											 "  AND A.st_documento_entrada = "+DocumentoEntradaServices.ST_LIBERADO+
											 "  AND A.tp_entrada           = "+DocumentoEntradaServices.ENT_COMPRA+
										     "  AND CAST(A.dt_documento_entrada AS DATE) BETWEEN ? AND ? " +
										     "GROUP BY A.cd_documento_entrada, A.dt_documento_entrada, A.nr_documento_entrada, A.vl_total_documento, B.nr_cnpj, B.nr_inscricao_estadual, " +
										     "         A.tp_documento_entrada, E.sg_estado, F.nr_codigo_fiscal, I.pr_aliquota, H.st_tributaria " +
										     "ORDER BY A.dt_documento_entrada");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSet rs50 = pstmt.executeQuery();
			int qtReg50 = 0;
			String r50  = "";
			while(rs50.next())	{
				String nrCnpj 	 = Util.limparFormatos(rs50.getString("nr_cnpj"));
				String nrIe 	 = Util.limparFormatos(rs50.getString("nr_inscricao_estadual"));
				GregorianCalendar dtEmissao = Util.convTimestampToCalendar(rs50.getTimestamp("dt_documento_entrada"));
				String sgUf       = rs50.getString("sg_estado");
				String nrModelo   = rs50.getInt("tp_documento_entrada")==DocumentoEntradaServices.TP_NOTA_ELETRONICA ? "55" : "01";
				String nrSerie    = "001";
				String nrDoc      = Util.limparFormatos(rs50.getString("nr_documento_entrada").trim());
				while(nrDoc!=null && nrDoc.length()>6) 
					nrDoc = nrDoc.substring(1);
				System.out.println(nrDoc);
				String nrCfop     = rs50.getString("nr_codigo_fiscal");				
				String tpEmi      = "T"; // Entradas
				String vlTotal    = Util.limparFormatos(Util.formatNumber(rs50.getFloat("vl_total_documento"), "0.00"));
				String vlBaseICMS = Util.limparFormatos(Util.formatNumber(rs50.getFloat("vl_total_base_calculo"), "0.00"));
				String vlICMS     = Util.limparFormatos(Util.formatNumber(rs50.getFloat("vl_total_tributo"), "0.00"));
				String vlIsenta   = "0";
				String vlOutras   = "0";
				String prAliquota =  Util.limparFormatos(Util.formatNumber(rs50.getFloat("pr_aliquota"), "0.00"));
				String stDoc      = "N"; // Situação Normal
				// Verificações
				if (nrCnpj == null || nrCnpj.trim().equals(""))	{
					reg.put("CD_ERRO","06");
					reg.put("DS_MENSAGEM","Registro 50: nrCnpj - ausente!");
					rsmLog.addRegister(reg);
				}
				System.out.println(nrCnpj);
				if (nrIe == null || nrIe.trim().equals(""))	{
					reg.put("CD_ERRO","06");
					reg.put("DS_MENSAGEM","Registro 50: Inscrição Estadual - ausente!");
					rsmLog.addRegister(reg);
				}
				if (nrCfop == null || nrCfop.trim().equals(""))	{
					reg.put("CD_ERRO","06");
					reg.put("DS_MENSAGEM","Registro 50: nrCfop - ausente!");
					rsmLog.addRegister(reg);
				}
				if (sgUf == null || sgUf.trim().equals(""))	{
					reg.put("CD_ERRO","06");
					reg.put("DS_MENSAGEM","Registro 50: sgUf -  - ausente!");
					rsmLog.addRegister(reg);
				}
				nrCfop = verificarCFOP(nrCfop, rs50.getInt("st_tributaria"), sgUf, cdEmpresa);
				// Montando registro
				qtReg50++;
				r50 += "50"+Util.fillAlpha(nrCnpj, 14)+
						Util.fillAlpha(nrIe, 14)+
						Util.formatDateTime(dtEmissao, "yyyyMMdd")+
						Util.fillAlpha(sgUf, 2)+
						Util.fillAlpha(nrModelo, 2)+
						Util.fillAlpha(nrSerie, 3)+
						Util.fill(nrDoc, 6, '0', 'E')+
						Util.fill(nrCfop, 4, '0', 'E')+
						Util.fillAlpha(tpEmi, 1)+
						Util.fill(vlTotal, 13, '0', 'E')+
						Util.fill(vlBaseICMS, 13, '0', 'E')+
						Util.fill(vlICMS, 13, '0', 'E')+
						Util.fill(vlIsenta, 13, '0', 'E')+
						Util.fill(vlOutras, 13, '0', 'E')+
						Util.fill(prAliquota, 4, '0', 'E')+
						Util.fill(stDoc, 1, ' ', 'D')+
				        "\r\n";
			}
			System.out.println(/**********************************************************************************************************/);
			System.out.println(/** REGISTRO TIPO 51 - Total de Nota Fiscal quanto ao IPI												**/);
			System.out.println(/**********************************************************************************************************/);
			pstmt = connect.prepareStatement("SELECT A.dt_documento_entrada, A.nr_documento_entrada, SUM(A.vl_total_documento) AS vl_total_documento, B.nr_cnpj, B.nr_inscricao_estadual, E.sg_estado, H.st_tributaria," +
					                         "       F.nr_codigo_fiscal, SUM(G.vl_tributo) AS vl_tributo " +
					                         "FROM alm_documento_entrada A " +
											 " LEFT OUTER JOIN grl_pessoa_juridica   B ON (A.cd_fornecedor = B.cd_pessoa) " +
											 " LEFT OUTER JOIN grl_pessoa_endereco   C ON (A.cd_fornecedor = C.cd_pessoa AND lg_principal = 1) " +
											 " LEFT OUTER JOIN grl_cidade            D ON (C.cd_cidade     = D.cd_cidade) " +
											 " LEFT OUTER JOIN grl_estado            E ON (E.cd_estado     = D.cd_estado) " +
											 " LEFT OUTER JOIN adm_natureza_operacao F ON (A.cd_natureza_operacao = F.cd_natureza_operacao) " +
											 " LEFT OUTER JOIN adm_entrada_item_aliquota I ON (I.cd_documento_entrada = A.cd_documento_entrada" +
											 "                                             AND I.cd_tributo           = "+cdTributoIPI+") " +
											 " LEFT OUTER JOIN adm_tributo_aliquota      H ON (H.cd_tributo_aliquota = I.cd_tributo_aliquota " +
											 "                                             AND H.cd_tributo          = I.cd_tributo " +
											 "										       AND H.st_tributaria       <> "+TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA+") "+
											 " JOIN adm_entrada_tributo              G ON (A.cd_documento_entrada = G.cd_documento_entrada" +
											 "                                         AND G.cd_tributo    = "+cdTributoIPI+") " +
											 "WHERE A.cd_empresa           = "+cdEmpresa+
											 "  AND A.tp_documento_entrada IN ("+DocumentoEntradaServices.TP_NOTA_FISCAL+","+DocumentoEntradaServices.TP_NOTA_ELETRONICA+")"+
											 "  AND A.tp_entrada           = "+DocumentoEntradaServices.ENT_COMPRA +
											 "  AND A.st_documento_entrada = "+DocumentoEntradaServices.ST_LIBERADO+
										     "  AND CAST(A.dt_documento_entrada AS DATE) BETWEEN ? AND ? " +
											 " GROUP BY A.dt_documento_entrada, A.nr_documento_entrada, B.nr_cnpj, B.nr_inscricao_estadual, E.sg_estado, H.st_tributaria, F.nr_codigo_fiscal " +
										     "ORDER BY A.dt_documento_entrada ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSet rs51 = pstmt.executeQuery();
			int qtReg51 = 0;
			String r51 = "";
			while(rs51.next())	{
				String nrCnpj 	 = Util.limparFormatos(rs51.getString("nr_cnpj"));
				System.out.println("CNPJ:" +nrCnpj );
				String ie 		 = Util.limparFormatos(rs51.getString("nr_inscricao_estadual"));
				GregorianCalendar dtEmissao = Util.convTimestampToCalendar(rs51.getTimestamp("dt_documento_entrada"));
				String sgUf      = rs51.getString("sg_estado");
				String serie     = "001";
				String nrDoc     = Util.limparFormatos(rs51.getString("nr_documento_entrada"));
				while(nrDoc!=null && nrDoc.length()>6) 
					nrDoc = nrDoc.substring(1);
				String nrCfop    = rs51.getString("nr_codigo_fiscal");
				String vlTotal   = Util.limparFormatos(Util.formatNumber(rs51.getFloat("vl_total_documento"), "#0.00"));
				String vlTributo = "0";
				String vlIsenta  = "0";
				if(rs51.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL)
					vlTributo = Util.limparFormatos(Util.formatNumber(rs51.getFloat("vl_tributo"), "#0.00"));
				else
					vlIsenta = Util.limparFormatos(Util.formatNumber(rs51.getFloat("vl_tributo"), "#0.00"));
				String outras    = "0";
				String situacao  = "N";//Situação Normal
				nrCfop = verificarCFOP(nrCfop, TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL, sgUf, cdEmpresa);
				if (nrCnpj.trim().equals(""))	{
					reg.put("CD_ERRO","06");
					reg.put("DS_MENSAGEM","CNPJ - Registro 51 - ausente!");
					rsmLog.addRegister(reg);
				}
				if (ie.trim().equals(""))	{
					reg.put("CD_ERRO","06");
					reg.put("DS_MENSAGEM","Inscrição Estadual - Registro 51 - ausente!");
					rsmLog.addRegister(reg);
				}
				if (nrCfop.trim().equals(""))	{
					reg.put("CD_ERRO","06");
					reg.put("DS_MENSAGEM","CFOP - Registro 51 - ausente!");
					rsmLog.addRegister(reg);
				}
				if (sgUf.trim().equals(""))	{
					reg.put("CD_ERRO","06");
					reg.put("DS_MENSAGEM","UF - Registro 51 - ausente!");
					rsmLog.addRegister(reg);
				}
				qtReg51++;
				r51 += "51"+Util.fillAlpha(nrCnpj, 14)+
						Util.fillAlpha(ie, 14)+
						Util.formatDateTime(dtEmissao, "yyyyMMdd")+
						Util.fillAlpha(sgUf, 2)+
						Util.fillAlpha(serie, 3)+
						Util.fill(nrDoc, 6, '0', 'E')+
						Util.fill(nrCfop, 4, '0', 'E')+
						Util.fill(vlTotal, 13, '0', 'E')+
						Util.fill(vlTributo, 13, '0', 'E')+
						Util.fill(vlIsenta, 13, '0', 'E')+
						Util.fill(outras, 13, '0', 'E')+
						Util.fill("", 20, ' ', 'D')+//Brancos
						Util.fill(situacao, 1, ' ', 'D')+
				        "\r\n";
			}
			/*********************************************************************************************************
			 * REGISTRO TIPO 53 - Substituição Tributária														 *
			 * *******************************************************************************************************/
			pstmt = connect.prepareStatement("SELECT A.dt_documento_entrada, A.nr_documento_entrada, A.vl_total_documento, A.vl_frete, A.vl_seguro, " +
					                         "       A.tp_documento_entrada, B.nr_cnpj, " +
					                         "       B.nr_inscricao_estadual, E.sg_estado, F.nr_codigo_fiscal, SUM(I.vl_base_calculo) AS vl_total_base_calculo, " +
					                         "       SUM(I.pr_aliquota * I.vl_base_calculo / 100) AS vl_total_tributo " +
					                         "FROM alm_documento_entrada A " +
											 " LEFT OUTER JOIN grl_pessoa_juridica  	  B ON (A.cd_fornecedor = B.cd_pessoa) " +
											 " LEFT OUTER JOIN grl_pessoa_endereco   	  C ON (A.cd_fornecedor = C.cd_pessoa AND lg_principal = 1) " +
											 " LEFT OUTER JOIN grl_cidade            	  D ON (C.cd_cidade     = D.cd_cidade) " +
											 " LEFT OUTER JOIN grl_estado            	  E ON (E.cd_estado     = D.cd_estado) " +
											 " LEFT OUTER JOIN adm_natureza_operacao 	  F ON (A.cd_natureza_operacao = F.cd_natureza_operacao) " +
											 " LEFT OUTER JOIN adm_entrada_item_aliquota  I ON (I.cd_documento_entrada = A.cd_documento_entrada" +
											 "                                              AND I.cd_tributo           = "+cdTributoICMS+") " +
											 " JOIN adm_tributo_aliquota                  H ON (H.cd_tributo_aliquota  = I.cd_tributo_aliquota " +
											 "                                              AND H.cd_tributo           = " +cdTributoICMS+
											 "                                              AND H.st_tributaria        = "+TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA+") " +
											 "WHERE A.cd_empresa           = "+cdEmpresa+
											 "  AND A.tp_documento_entrada IN ("+DocumentoEntradaServices.TP_NOTA_FISCAL+","+DocumentoEntradaServices.TP_NOTA_ELETRONICA+")"+
											 "  AND A.tp_entrada           = "+DocumentoEntradaServices.ENT_COMPRA+
											 "  AND A.st_documento_entrada = "+DocumentoEntradaServices.ST_LIBERADO+
								             "  AND A.dt_documento_entrada BETWEEN ? AND ? " +
								             "GROUP BY A.dt_documento_entrada, A.nr_documento_entrada, A.vl_total_documento, A.vl_frete, A.vl_seguro, A.tp_documento_entrada, " +
								             "      B.nr_cnpj, B.nr_inscricao_estadual, E.sg_estado, F.nr_codigo_fiscal " +
								             "ORDER BY A.dt_documento_entrada ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSet rs53 = pstmt.executeQuery();
			int qtReg53 = 0;
			String r53 = "";
			while(rs53.next()){
				String nrCnpj 	  = Util.limparFormatos(rs53.getString("nr_cnpj"));
				String ie 		  = Util.limparFormatos(rs53.getString("nr_inscricao_estadual"));
				GregorianCalendar dtEmissao = Util.convTimestampToCalendar(rs53.getTimestamp("dt_documento_entrada"));
				String sgUf       = rs53.getString("sg_estado");
				String nrModelo   = rs53.getInt("tp_documento_entrada")==DocumentoEntradaServices.TP_NOTA_ELETRONICA ? "55" : "01";
				String nrSerie    = "001";
				String nrDoc      = Util.limparFormatos(rs53.getString("nr_documento_entrada"));
				while(nrDoc!=null && nrDoc.length()>6) 
					nrDoc = nrDoc.substring(1);
				double valorICMSRetido = rs53.getFloat("vl_total_tributo");
				if(valorICMSRetido==0 && rs53.getFloat("vl_total_base_calculo")>0)
					valorICMSRetido = (rs53.getFloat("vl_total_base_calculo") * 0.17);
				String nrCfop     = rs53.getString("nr_codigo_fiscal");
				String vlBaseICMSSubts = Util.limparFormatos(Util.formatNumber(rs53.getFloat("vl_total_base_calculo"), "0.00"));
				String vlICMSRetido    = Util.limparFormatos(Util.formatNumber(valorICMSRetido, "0.00"));
				String vlDespesas      = Util.limparFormatos(Util.formatNumber((rs53.getFloat("vl_frete") + rs53.getFloat("vl_seguro")), "0.00"));
				String situacao        = "N"; // Situação Normal
				nrCfop = verificarCFOP(nrCfop, TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA, sgUf, cdEmpresa);
				if (nrCnpj.trim().equals(""))	{
					reg.put("CD_ERRO","06");
					reg.put("DS_MENSAGEM","Registro 53: CNPJ - ausente!");
					rsmLog.addRegister(reg);
				}
				if (ie.trim().equals(""))	{
					reg.put("CD_ERRO","06");
					reg.put("DS_MENSAGEM","Registro 53: Inscrição Estadual - ausente!");
					rsmLog.addRegister(reg);
				}
				if (nrCfop.trim().equals(""))	{
					reg.put("CD_ERRO","06");
					reg.put("DS_MENSAGEM","Registro 53: CFOP - ausente!");
					rsmLog.addRegister(reg);
				}
				if (sgUf.trim().equals(""))	{
					reg.put("CD_ERRO","06");
					reg.put("DS_MENSAGEM","Registro 53: UF - ausente!");
					rsmLog.addRegister(reg);
				}
				qtReg53++;
				r53 += "53"+Util.fillAlpha(nrCnpj, 14)+
						Util.fillAlpha(ie, 14)+
						Util.formatDateTime(dtEmissao, "yyyyMMdd")+
						Util.fillAlpha(sgUf, 2)+
						Util.fillAlpha(nrModelo, 2)+
						Util.fillAlpha(nrSerie, 3)+
						Util.fill(nrDoc, 6, '0', 'E')+
						Util.fill(nrCfop, 4, '0', 'E')+
						Util.fill("T", 1, ' ', 'D')+ // Tipo de Emissão = Terceiros (entradas)
						Util.fill(vlBaseICMSSubts, 13, '0', 'E')+
						Util.fill(vlICMSRetido, 13, '0', 'E')+
						Util.fill(vlDespesas, 13, '0', 'E')+
						Util.fill(situacao, 1, ' ', 'D')+
						Util.fill("", 30, ' ', 'D')+//Brancos
						"\r\n";
			}
			/*********************************************************************************************************
			 * REGISTRO TIPO 54 - Notas Fiscais de Compra/Venda																 *
			 * *******************************************************************************************************/
			pstmt = connect.prepareStatement("SELECT Z.cd_produto_servico, Z.vl_unitario, Z.qt_entrada, Z.vl_desconto, A.cd_documento_entrada, " +
					                         "       A.tp_documento_entrada, A.nr_documento_entrada, C.id_produto_servico, D.id_reduzido, " +
											 "       A.vl_total_documento, B.nr_cnpj, B.nr_inscricao_estadual, " +
											 "       F.nr_codigo_fiscal, H.st_tributaria, I.pr_aliquota AS pr_icms, " +
											 "       I.vl_base_calculo AS vl_base_icms, (I.pr_aliquota * I.vl_base_calculo / 100) AS vl_icms, " +
											 "       (I2.pr_aliquota * I2.vl_base_calculo / 100) AS vl_ipi, SG.sg_estado " +
											 "FROM alm_documento_entrada_item Z " +
											 " JOIN alm_documento_entrada                A ON (A.cd_documento_entrada = Z.cd_documento_entrada) " +
											 " LEFT OUTER JOIN grl_pessoa_juridica  	 B ON (A.cd_fornecedor        = B.cd_pessoa) " +
											 " LEFT OUTER JOIN grl_pessoa_endereco      PE ON (A.cd_fornecedor        = PE.cd_pessoa AND lg_principal = 1) " +
											 " LEFT OUTER JOIN grl_cidade               CI ON (PE.cd_cidade           = CI.cd_cidade) " +
											 " LEFT OUTER JOIN grl_estado               SG ON (CI.cd_estado     	  = SG.cd_estado) " +
											 " JOIN grl_produto_servico                  C ON (Z.cd_produto_servico   = C.cd_produto_servico)" +
											 " JOIN grl_produto_servico_empresa          D ON (Z.cd_produto_servico   = D.cd_produto_servico" +
											 "                                             AND D.cd_empresa           = "+cdEmpresa+")" +
											 " LEFT OUTER JOIN adm_natureza_operacao 	 F ON (A.cd_natureza_operacao = F.cd_natureza_operacao) " +
											 " LEFT OUTER JOIN adm_entrada_item_aliquota I ON (I.cd_documento_entrada = Z.cd_documento_entrada " +
											 "                                             AND I.cd_produto_servico   = Z.cd_produto_servico " +
											 "                                             AND I.cd_empresa           = Z.cd_empresa " +
											 "                                             AND I.cd_tributo           = "+cdTributoICMS+") " +
											 " LEFT OUTER JOIN adm_tributo_aliquota      H ON (H.cd_tributo_aliquota  = I.cd_tributo_aliquota " +
											 "                                             AND H.cd_tributo           = "+cdTributoICMS+") " +
											 " LEFT OUTER JOIN adm_entrada_item_aliquota I2 ON (I2.cd_documento_entrada = Z.cd_documento_entrada " +
											 "                                              AND I2.cd_produto_servico   = Z.cd_produto_servico " +
											 "                                              AND I2.cd_empresa           = Z.cd_empresa " +
											 "                                              AND I2.cd_tributo           = "+cdTributoIPI+") " +
											 "WHERE A.cd_empresa           = "+cdEmpresa+
											 "  AND A.tp_documento_entrada IN ("+DocumentoEntradaServices.TP_NOTA_FISCAL+","+DocumentoEntradaServices.TP_NOTA_ELETRONICA+")"+
											 "  AND A.tp_entrada           = "+DocumentoEntradaServices.ENT_COMPRA+
											 "  AND A.st_documento_entrada = "+DocumentoEntradaServices.ST_LIBERADO+
										     "  AND CAST(A.dt_documento_entrada AS DATE) BETWEEN ? AND ? " +
										     "ORDER BY B.nr_cnpj, A.nr_documento_entrada");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSet rs54 = pstmt.executeQuery();
			int qtReg54 = 0;
			String r54  = "";
			int nrItem  = 0;
			int cdDocEntrada = 0;
			String sgUf = "";
			while(rs54.next())	{
				if(cdDocEntrada!=rs54.getInt("cd_documento_entrada")) {
					nrItem       = 0;
					cdDocEntrada = rs54.getInt("cd_documento_entrada");
				}
				nrItem++;
				String nrCnpj 	  = Util.limparFormatos(rs54.getString("nr_cnpj"));
				String nrModelo   = rs54.getInt("tp_documento_entrada")==DocumentoEntradaServices.TP_NOTA_ELETRONICA ? "55" : "01";
				String nrSerie    = "001";
				String nrDoc      = Util.limparFormatos(rs54.getString("nr_documento_entrada"));
				while(nrDoc!=null && nrDoc.length()>6) 
					nrDoc = nrDoc.substring(1);
				String nrCfop     = rs54.getString("nr_codigo_fiscal");
				String idProduto  = rs54.getString("id_produto_servico");
				if(idProduto==null || idProduto.trim().equals(""))
					idProduto = rs54.getString("id_reduzido");
				if(idProduto==null || idProduto.trim().equals(""))
					idProduto = rs54.getString("cd_produto_servico");
				while(idProduto!=null && idProduto.length()>14) 
					idProduto = idProduto.substring(1);
				String qtEntrada  = Util.limparFormatos(Util.formatNumber(rs54.getFloat("qt_entrada"), "0.000"));
				String vlProduto  = Util.limparFormatos(Util.formatNumber(rs54.getFloat("vl_unitario")));
				String vlDesconto = Util.limparFormatos(Util.formatNumber(rs54.getFloat("vl_desconto")));
				String vlBaseICMS = Util.limparFormatos(Util.formatNumber(rs54.getFloat("vl_base_icms")));
				String vlICMS     = Util.limparFormatos(Util.formatNumber(rs54.getFloat("vl_icms")));
				String vlIPI      = Util.limparFormatos(Util.formatNumber(rs54.getFloat("vl_ipi")));
				String prICMS     = Util.limparFormatos(Util.formatNumber(rs54.getFloat("pr_icms")));
				String stTributaria = "000"; // TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL 
				switch(rs54.getInt("st_tributaria")) {
					case TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA:
						stTributaria = "010"; 
						break;  
					case TributoAliquotaServices.ST_SEM_TRIBUTACAO:
						stTributaria = "041"; 
						break;  
					case TributoAliquotaServices.ST_ISENTO:
						stTributaria = "040"; 
						break;  
				}
				sgUf = rs54.getString("sg_estado");
				nrCfop = verificarCFOP(nrCfop, rs54.getInt("st_tributaria"), sgUf, cdEmpresa);
				if (nrCnpj.trim().equals(""))	{
					reg.put("CD_ERRO","06");
					reg.put("DS_MENSAGEM","Registro 54: nrCnpj - ausente!");
					rsmLog.addRegister(reg);
				}
				if (nrCfop.trim().equals(""))	{
					reg.put("CD_ERRO","06");
					reg.put("DS_MENSAGEM","Registro 54: nrCfop - ausente!");
					rsmLog.addRegister(reg);
				}
				qtReg54++;
				r54 += "54"+Util.fillAlpha(nrCnpj, 14)+
						Util.fillAlpha(nrModelo, 2)+
						Util.fillAlpha(nrSerie, 3)+
						Util.fill(nrDoc, 6, '0', 'E')+
						Util.fill(nrCfop, 4, '0', 'E')+
						Util.fill(stTributaria, 3, '0', 'E')+ // Por enquanto apenas Tributado Integralmente
						Util.fillNum(nrItem, 3)+
						Util.fill(idProduto.trim(), 14, ' ', 'D')+
						Util.fill(qtEntrada, 11, '0', 'E')+
						Util.fill(vlProduto, 12, '0', 'E')+
						Util.fill(vlDesconto, 12, '0', 'E')+
						Util.fill(vlBaseICMS, 12, '0', 'E')+
						Util.fill(vlICMS, 12, '0', 'E')+
						Util.fill(vlIPI, 12, '0', 'E')+
						Util.fill(prICMS, 4, '0', 'E')+
				        "\r\n";
			}
			/*********************************************************************************************************
			 * REGISTRO TIPO 60(M,D,I,A) - Registro de Operações efetuadas por ECF	                                 *
			 * *******************************************************************************************************/
			pstmt = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
											 "WHERE CAST(dt_registro_ecf AS DATE) BETWEEN ? AND ? " +
											 "  AND tp_registro_ecf LIKE \'60_\'" +
											 "  AND tp_registro_ecf <> \'60R\'" +
											 "ORDER BY dt_registro_ecf, cd_registro_ecf ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSet rs60 = pstmt.executeQuery();
			int qtReg60 = 0;
			String r60 = "";
			while(rs60.next()){
				qtReg60++;
				r60 += rs60.getString("txt_registro_ecf")+"\r\n";
			}
			/*********************************************************************************************************
			 * REGISTRO TIPO 60(R) - Registro de Operações efetuadas por ECF	                                 *
			 * *******************************************************************************************************/
			pstmt = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
											 "WHERE CAST(dt_registro_ecf AS DATE) BETWEEN ? AND ? " +
											 "  AND tp_registro_ecf = \'60R\' " +
											 "ORDER BY dt_registro_ecf, cd_registro_ecf ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			rs60 = pstmt.executeQuery();
			while(rs60.next()){
				qtReg60++;
				r60 += rs60.getString("txt_registro_ecf")+"\r\n";
			}
			
			/*********************************************************************************************************
			 * REGISTRO TIPO 61 - Notas Fiscais de Compra/Venda																 *
			 * *******************************************************************************************************/
			pstmt = connect.prepareStatement("SELECT Z.cd_produto_servico, Z.vl_unitario, Z.qt_saida, Z.vl_desconto, A.cd_documento_saida, "+   
											 "       A.tp_documento_saida, A.nr_documento_saida, C.id_produto_servico, D.id_reduzido, E.nr_ncm, "+    	
											 "		 A.vl_total_documento, F.nr_codigo_fiscal, H.st_tributaria, I.pr_aliquota AS pr_icms, "+    
											 "       I.vl_base_calculo AS vl_base_icms, (I.pr_aliquota * I.vl_base_calculo / 100) AS vl_icms, "+    
											 "  	(I2.pr_aliquota * I2.vl_base_calculo / 100) AS vl_ipi, CAST(A.dt_documento_saida AS DATE) " +
											 "  FROM alm_documento_saida_item Z  "+  
											 "  JOIN alm_documento_saida                 A ON (A.cd_documento_saida = Z.cd_documento_saida) "+    
											 "  JOIN grl_produto_servico                 C ON (Z.cd_produto_servico   = C.cd_produto_servico) "+ 
											 "  JOIN grl_produto_servico_empresa         D ON (Z.cd_produto_servico   = D.cd_produto_servico "+  											 											 
											 "                                             AND D.cd_empresa           = "+cdEmpresa+" ) "+   
											 "	LEFT OUTER JOIN grl_ncm           		 E ON (E.cd_ncm               = C.cd_ncm) " +
											 "  LEFT OUTER JOIN adm_natureza_operacao 	 F ON (A.cd_natureza_operacao = F.cd_natureza_operacao) "+    
											 "  LEFT OUTER JOIN adm_saida_item_aliquota  I ON (I.cd_documento_saida = Z.cd_documento_saida "+    
											 "                                             AND I.cd_produto_servico   = Z.cd_produto_servico "+    
											 "                                             AND I.cd_empresa           = Z.cd_empresa "+    
											 "                                             AND I.cd_tributo           = "+cdTributoICMS+" ) "+    
											 "  LEFT OUTER JOIN adm_tributo_aliquota     H ON (H.cd_tributo_aliquota  = I.cd_tributo_aliquota "+    
											 "                                             AND H.cd_tributo           = "+cdTributoICMS+" ) "+    
											 "  LEFT OUTER JOIN adm_saida_item_aliquota I2 ON (I2.cd_documento_saida = Z.cd_documento_saida "+    
											 "                                             AND I2.cd_produto_servico   = Z.cd_produto_servico "+    
											 "                                             AND I2.cd_empresa           = Z.cd_empresa "+    
											 "                                             AND I2.cd_tributo           = "+cdTributoIPI+" ) "+    
											 " WHERE A.cd_empresa         = "+cdEmpresa+  
											 "   AND A.tp_documento_saida = "+ DocumentoSaidaServices.TP_NOTA_FISCAL_VENDA +  
											 "   AND A.tp_saida           = "+ DocumentoSaidaServices.SAI_VENDA+ 
											 "   AND A.st_documento_saida = "+ DocumentoSaidaServices.ST_CONCLUIDO+
											 "   AND CAST(A.dt_documento_saida AS DATE) BETWEEN ? AND ? "+    
											 " ORDER BY  A.nr_documento_saida", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSet rs61  = pstmt.executeQuery();
			int qtReg61 = 0;
			String r61  = "";
			int cdDocEntrada61 = 0;
			//
			GregorianCalendar dtEmissao = null;
			while(rs61.next())	{
				System.out.println("Registro 61.1");
				if(cdDocEntrada61!=rs61.getInt("cd_documento_saida")) {
					cdDocEntrada61 = rs61.getInt("cd_documento_saida");
				}
				String nrModelo   = "02";
//				String nrCfop61     = rs61.getString("nr_codigo_fiscal");
//				String idProduto61  = rs61.getString("id_produto_servico");
//				if(idProduto61==null || idProduto61.trim().equals(""))
//					idProduto61 = rs61.getString("id_reduzido");
//				if(idProduto61==null || idProduto61.trim().equals(""))
//					idProduto61 = rs61.getString("cd_produto_servico");
//				while(idProduto61!=null && idProduto61.length()>14) 
//					idProduto61 = idProduto61.substring(1);
				dtEmissao = Util.convTimestampToCalendar(rs61.getTimestamp("dt_documento_saida"));
//				String qtSaida    = Util.limparFormatos(Util.formatNumber(rs54.getFloat("qt_entrada"), "0.000"));
//				String vlProduto  = Util.limparFormatos(Util.formatNumber(rs54.getFloat("vl_unitario")));
//				String vlDesconto = Util.limparFormatos(Util.formatNumber(rs54.getFloat("vl_desconto")));
//				String vlBaseICMS = Util.limparFormatos(Util.formatNumber(rs54.getFloat("vl_base_icms")));
//				String vlICMS     = Util.limparFormatos(Util.formatNumber(rs54.getFloat("vl_icms")));
//				String vlIPI      = Util.limparFormatos(Util.formatNumber(rs54.getFloat("vl_ipi")));
//				String prICMS     = Util.limparFormatos(Util.formatNumber(rs54.getFloat("pr_icms")));
				String stTributaria = "000"; // TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL
				float vlIsentoSemTributacao = 0;
				switch(rs61.getInt("st_tributaria")) {
					case TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA:
						//stTributaria = "010";
						stTributaria = "0000000000000";
						break;  
					case TributoAliquotaServices.ST_SEM_TRIBUTACAO:
						vlIsentoSemTributacao = rs61.getFloat("vl_icms"); 
						break;  
					case TributoAliquotaServices.ST_ISENTO:
						vlIsentoSemTributacao = rs61.getFloat("vl_icms"); 
						break;  
				}
//				nrCfop61 = verificarCFOP(nrCfop61, rs61.getInt("st_tributaria"));
//				if (nrCfop61.trim().equals(""))	{
//					reg.put("CD_ERRO","06");
//					reg.put("DS_MENSAGEM","Registro 61: nrCfop - ausente!");
//					rsmLog.addRegister(reg);
//				}
				qtReg61++;
				float vlTotal    = 0;
				float vlBaseICMS = 0;
				float vlICMS     = 0;
				r61 += "61"+
						"              " +//Branco 
						"              " +//Branco
						Util.formatDateTime(dtEmissao, "yyyyMMdd")+
						nrModelo+
						"U  "+ //nrSerie`Iniciados por D sao os com seriacao iniciada por letra, do contrario utiliza U
						"  "; //subSerie
				//
				String firstDoc = "", lastDoc = "";
				GregorianCalendar dtComparar = Util.convTimestampToCalendar(rs61.getTimestamp("dt_documento_saida"));
				dtEmissao.set(Calendar.HOUR, 0);
				dtEmissao.set(Calendar.MINUTE, 0);
				dtEmissao.set(Calendar.SECOND, 0);
				//
				dtComparar.set(Calendar.HOUR, 0);
				dtComparar.set(Calendar.MINUTE, 0);
				dtComparar.set(Calendar.SECOND, 0);
				//
				do{
					// Primeiro documento do dia
					if ((firstDoc == "") || (firstDoc == null))
						firstDoc = Util.limparFormatos(rs61.getString("nr_documento_saida"));
					// Dados dos documentos
					if (!rs61.isAfterLast()){
						// último documento do dia
						lastDoc     = Util.limparFormatos(rs61.getString("nr_documento_saida"));
						vlTotal    += rs61.getFloat("vl_total_documento");
						vlBaseICMS += rs61.getFloat("vl_base_icms");
						vlICMS     += rs61.getFloat("vl_icms");
						//
						if (rs61.next()){
							dtComparar = Util.convTimestampToCalendar(rs61.getTimestamp("dt_documento_saida"));
							dtComparar.set(Calendar.HOUR, 0);
							dtComparar.set(Calendar.MINUTE, 0);
							dtComparar.set(Calendar.SECOND, 0); 
						}
						else
							break;
					}	
				}
				while(dtEmissao.equals(dtComparar));
				//
				rs61.previous();
				String vlrTotal    = Util.limparFormatos(Util.formatNumber(vlTotal));
				String vlrbaseICMS = Util.limparFormatos(Util.formatNumber(vlBaseICMS));
				String vlrICMS     = Util.limparFormatos(Util.formatNumber(vlICMS));
				while(firstDoc!=null && firstDoc.length()>6) 
					firstDoc = firstDoc.substring(1);
				while(lastDoc!=null && lastDoc.length()>6) 
					lastDoc = lastDoc.substring(1);
				//		//
				r61  += Util.fill(firstDoc, 6, '0', 'E')+
						Util.fill(lastDoc, 6, '0', 'E')+
						Util.fill(vlrTotal, 13, '0', 'E')+
						Util.fill(vlrbaseICMS, 13, '0', 'E')+ // Por enquanto apenas Tributado Integralmente
						Util.fill(vlrICMS, 12, '0', 'E')+
						"0000000000000"+ "0000000000000"+"0000" + " " +
						"\r\n";
			}			
			/*********************************************************************************************************
			 * REGISTRO TIPO 61R - Resumo Mensal por Item (61R)		                             *
			 * *******************************************************************************************************/
			ResultSet rs61R = rs61;
			rs61R.beforeFirst();
			int qtReg61R = 0;
			String r61R  = "", r61to75 = "";
			while (rs61R.next()){
				String idProduto  = rs61R.getString("id_produto_servico");
				if(idProduto==null || idProduto.trim().equals(""))
					idProduto = rs61.getString("id_reduzido");
				if(idProduto==null || idProduto.trim().equals(""))
					idProduto = rs61.getString("cd_produto_servico");
				while(idProduto!=null && idProduto.length()>14) 
					idProduto = idProduto.substring(1);
				//
				String qtSaida    = Util.limparFormatos(Util.formatNumber(rs61R.getFloat("qt_saida"), "0.000"));
				String vlProduto  = Util.limparFormatos(Util.formatNumber(rs61R.getFloat("vl_unitario")));
				String vlICMS     = Util.limparFormatos(Util.formatNumber(rs61R.getFloat("vl_icms")));
				String prICMS     = Util.limparFormatos(Util.formatNumber(rs61R.getFloat("pr_icms")));
				//
				r61R += "61" +
						"R" +
						Util.formatDateTime(dtEmissao, "MMyyyy")+
						Util.fill(Util.limparFormatos(idProduto.trim()), 14, ' ', 'D')+
						Util.fill(Util.limparFormatos(qtSaida.trim()), 13, '0', 'E')+
						Util.fill(Util.limparFormatos(vlProduto.trim()), 16, '0', 'E')+
						Util.fill(Util.limparFormatos(vlICMS.trim()), 16, '0', 'E')+
						Util.fill(Util.limparFormatos(prICMS.trim()), 4, '0', 'E')+
						"                                                      "+
						"\r\n";	
				qtReg61R++;
			}
			 
			
			/*********************************************************************************************************
			 * REGISTRO TIPO 75 - Registro tipo 75 Códigos de Produtos e Serviços		                             *
			 * *******************************************************************************************************/
			String r75  = "";
			int qtReg75 = 0;
			pstmt = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
					                         "WHERE CAST(dt_registro_ecf AS DATE) BETWEEN ? AND ? " +
					 						 "  AND tp_registro_ecf = \'75\'");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsm75 = new ResultSetMap(pstmt.executeQuery());
			while(rsm75.next()){
				qtReg75++;
				r75 += rsm75.getString("txt_registro_ecf")+"\r\n";
				rsm75.setValueToField("ID_PRODUTO", rsm75.getString("txt_registro_ecf").substring(18, 33).trim());
			}
			// Carregando total dos registros
			int cdNaturezaOperacao = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_VAREJO", 0);
			int cdPais = 0, cdEstado = 0, cdCidade = 0;
			rs = connect.prepareStatement("SELECT D.cd_cidade, B.cd_estado, C.cd_pais " +
										  "FROM grl_empresa A " +
										  "LEFT OUTER JOIN grl_pessoa_endereco D ON (A.cd_empresa = D.cd_pessoa AND D.lg_principal = 1) " +
									  	  "LEFT OUTER JOIN grl_cidade          B ON (D.cd_cidade = B.cd_cidade) " +
										  "LEFT OUTER JOIN grl_estado          C ON (B.cd_estado = C.cd_estado) " +
										  "WHERE A.cd_empresa = "+cdEmpresa).executeQuery();
			if(rs.next())	{
				cdCidade = rs.getInt("cd_cidade");
				cdEstado = rs.getInt("cd_estado");
				cdPais   = rs.getInt("cd_pais");
			}
			pstmt = connect.prepareStatement(
							 "SELECT A.*, B.id_reduzido, C.nr_ncm, D.sg_unidade_medida "+
		                     "FROM grl_produto_servico           A "+
				             "JOIN grl_produto_servico_empresa   B ON (B.cd_produto_servico = A.cd_produto_servico "+
				             "                                     AND B.cd_empresa         = "+cdEmpresa+") "+
				             "LEFT OUTER JOIN grl_ncm            C ON (C.cd_ncm             = A.cd_ncm) " + 
				             "LEFT OUTER JOIN grl_unidade_medida D ON (D.cd_unidade_medida  = B.cd_unidade_medida) "+
				             "WHERE EXISTS (SELECT * FROM alm_documento_entrada DE, alm_documento_entrada_item DEI " +
				             "              WHERE DE.cd_documento_entrada = DEI.cd_documento_entrada " +
				             "                AND DEI.cd_produto_servico  = B.cd_produto_servico " +
				             "                AND DEI.cd_empresa          = B.cd_empresa " +
				             "                AND DE.tp_documento_entrada IN ("+DocumentoEntradaServices.TP_NOTA_FISCAL+")"+ 
				             "                AND DE.tp_entrada           = "+DocumentoEntradaServices.ENT_COMPRA+
				             "                AND DE.st_documento_entrada = "+DocumentoEntradaServices.ST_LIBERADO+
				             "                AND CAST(DE.dt_documento_entrada AS DATE) BETWEEN ? AND ? ) "+
				             "	 OR EXISTS (SELECT * FROM alm_documento_saida DS, alm_documento_saida_item DSI " +
				             "              WHERE DS.cd_documento_saida   = DSI.cd_documento_saida " +
				             "                AND DSI.cd_produto_servico  = B.cd_produto_servico " +
				             "                AND DSI.cd_empresa          = B.cd_empresa " +
				             "                AND DS.tp_documento_saida IN ("+DocumentoSaidaServices.TP_NOTA_FISCAL_VENDA+")"+ 
				             "                AND DS.tp_saida             = "+DocumentoSaidaServices.SAI_VENDA+
				             "                AND DS.st_documento_saida   = "+DocumentoSaidaServices.ST_CONCLUIDO+
				             "                AND CAST(DS.dt_documento_saida AS DATE) BETWEEN ? AND ? )");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setTimestamp(3, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(4, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSet rs75 = pstmt.executeQuery();
			while(rs75.next())	{
				String sgUnidade = rs75.getString("sg_unidade_medida")==null ? "" : rs75.getString("sg_unidade_medida");
				String nmProduto = rs75.getString("nm_produto_servico")==null ? "" : (rs75.getString("nm_produto_servico"));
				nmProduto       += rs75.getString("txt_dado_tecnico")==null ? "" : (" "+rs75.getString("txt_dado_tecnico"));
				nmProduto       += rs75.getString("txt_especificacao")==null ? "" : (" "+rs75.getString("txt_especificacao"));
				String nrNcm     = rs75.getString("nr_ncm")==null ? "" : rs75.getString("nr_ncm");
				// Prioridade = Código de Barras
				String idProduto = rs75.getString("id_produto_servico").trim();
				// Depois ID Reduzido
				if(idProduto==null || idProduto.equals(""))
					idProduto = rs75.getString("id_reduzido");
				// Por fim o código
				if(idProduto==null || idProduto.equals(""))
					idProduto = rs75.getString("cd_produto_servico");
				while(idProduto!=null && idProduto.length()>14) 
					idProduto = idProduto.substring(1);
				float prAliquotaICMS     = 0; 
				float prAliquotaIPI      = 0;
				float prBaseSubstituicao = 0;
				float prReducaoBase      = 0;
				if(rsm75.locate("ID_PRODUTO", idProduto))
					continue;
				else	{
					reg = new HashMap<String,Object>();
					reg.put("ID_PRODUTO", idProduto);
					rsm75.addRegister(reg);
				}
				// Verificando impostos
				ResultSetMap rsm = TributoServices.calcTributos(TributoAliquotaServices.OP_VENDA, cdNaturezaOperacao, rs75.getInt("cd_classificacao_fiscal"), 
						                                        rs75.getInt("cd_produto_servico"), cdPais, cdEstado, cdCidade, 0, connect);
				while(rsm.next())
					// ICMS
					if(rsm.getInt("cd_tributo")==cdTributoICMS) {
						prAliquotaICMS = rsm.getFloat("pr_aliquota");
						if(rsm.getInt("st_tributaria")==TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA && 
						   rsm.getInt("tp_fator_variacao_base") == TributoAliquotaServices.FT_PERCENTUAL) 
						{
							prBaseSubstituicao = (100 - rsm.getFloat("vl_variacao_base"));
						}
						else
							prBaseSubstituicao = rsm.getFloat("vl_variacao_base");
					}
					// IPI
					else if (rsm.getInt("cd_tributo")==cdTributoIPI) {
						prAliquotaIPI = rsm.getFloat("pr_aliquota");
					}
				// O NCM é obrigatório apenas para contribuintes do IPI
				if (prAliquotaIPI>0 && nrNcm.trim().equals(""))	{
					reg.put("CD_ERRO","06");
					reg.put("DS_MENSAGEM","NCM ausente!");
					rsmLog.addRegister(reg);
				}
				qtReg75++;
				r75 += "75"+Util.formatDateTime(dtInicial, "yyyyMMdd")+
						Util.formatDateTime(dtFinal, "yyyyMMdd")+
						Util.fillAlpha(idProduto.trim(), 14)+
						Util.fillAlpha(nrNcm, 8)+
						Util.fillAlpha(Util.limparTexto(nmProduto.trim()), 53)+
						Util.fillAlpha(sgUnidade.trim(), 6)+
						Util.fillNum(Math.round(prAliquotaIPI * 100), 5)+ // Aliquota do IPI
						Util.fillNum(Math.round(prAliquotaICMS * 100), 4)+ // Aliquota do ICMS
						Util.fillNum(Math.round(prReducaoBase * 100), 5)+
						Util.fillNum(Math.round(prBaseSubstituicao), 13)+
				        "\r\n";
			}
			/*********************************************************************************************************
			 * REGISTRO TIPO 90 - Totalizador do Arquivo														     *
			 * *******************************************************************************************************/
			int qtTotalReg = qtReg50 + qtReg51 + qtReg53 + qtReg54 + qtReg60 + qtReg61 + qtReg61R + qtReg75 + 3/*Reg 11 + Reg 10 + Reg 90*/;
			String r90 = "90"+
						 Util.fill(empresa.getNrCnpj(), 14, '0', 'E')+ // nrCnpj
						 Util.fillAlpha(nrIEEmp, 14)+                  // Inscrição Estadual
						 "50"+Util.fillNum(qtReg50, 8)+                // Total de registros do tipo 50
						 "51"+Util.fillNum(qtReg51, 8)+                // Total de registros do tipo 51
						 "53"+Util.fillNum(qtReg53, 8)+                // Total de registros do tipo 53
						 "54"+Util.fillNum(qtReg54, 8)+                // Total de registros do tipo 54						 
						 "60"+Util.fillNum(qtReg60, 8)+                // Total de registros do tipo 60
						 "61"+Util.fillNum((qtReg61 + qtReg61R), 8)+                // Total de registros do tipo 61
						 "75"+Util.fillNum(qtReg75, 8)+                // Total de registros do tipo 75
						 "99"+Util.fillNum(qtTotalReg, 8)+             // Total de todos os registros do arquivo
						 Util.fill("", 15, ' ', 'D')+
						 "1";                                          // Quantidade de Registros tipo 90
			// Totalizador geral
			String arquivoStr = r10+r11+r50+r51+r53+r54+r60+r61+r61R+r75+r90;
			
			// Util.printInFile("/SINTEGRA_" + Util.formatDate(dtInicial, "ddMMyyyy") + "_" + Util.formatDate(dtFinal, "ddMMyyyy") + ".txt", arquivoStr);
			if(rsmLog.size() > 0)
				Util.printInFile("/logERRO_SINTEGRA_" + Util.formatDate(dtInicial, "ddMMyyyy") + "_" + Util.formatDate(dtFinal, "ddMMyyyy") + ".txt", rsmLog.toString());
			
			return arquivoStr;// 
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return e.getMessage();//new Object[] {null, rsmLog};
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	private static String verificarCFOP(String nrCfop, int stTributaria, String sgEstado, int cdEmpresa) {		
		// Verifica se o CFOP informado foi o de saída, modificar para entrada
		String sgEmpresa = EstadoServices.getEstadoByCdEmpresa(cdEmpresa).trim();
		if(nrCfop.length()>0 && (nrCfop.substring(0,1).equals("5") || nrCfop.substring(0,1).equals("6"))) {
			// SUBSTITUIÇÃO TRIBUTÁRIA
			if(stTributaria==TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA) {			
				if(nrCfop.substring(0,1).equals("5"))
					nrCfop = "1403";// Compra para comercialização
				else
					nrCfop = "2403";// Compra para comercialização
			}
			else {
				if(nrCfop.substring(0,1).equals("5"))
					nrCfop = "1102"; // Compra para comercialização
				else
					nrCfop = "2102"; // Compra para comercialização				
			}
		}else{
			// SUBSTITUIÇÃO TRIBUTÁRIA
			if(stTributaria==TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA) {		
				// Para CFOP's Iniciados por 2, UF deve ser diferente da UF do informante
				if((nrCfop.substring(0,1).equals("2") && sgEstado.trim().equalsIgnoreCase(sgEmpresa)) ||
						(nrCfop.substring(0,1).equals("1") && sgEstado.trim().equalsIgnoreCase(sgEmpresa)))
					nrCfop = "1403";// Compra para comercialização
				else if (!sgEstado.trim().equalsIgnoreCase(sgEmpresa))
					nrCfop = "2403";// Compra para comercialização
			}
			else {
				// Para CFOP's Iniciados por 2, UF deve ser diferente da UF do informante								
				if((nrCfop.substring(0,1).equals("2") && sgEstado != null && sgEstado.trim().equalsIgnoreCase(sgEmpresa)) ||
						(nrCfop.substring(0,1).equals("1") && sgEstado.trim().equalsIgnoreCase(sgEmpresa)))
					nrCfop = "1102"; // Compra para comercialização	
				else if(sgEstado != null && !sgEstado.trim().equalsIgnoreCase(sgEmpresa))
					nrCfop = "2102"; // Compra para comercialização				
			}			
		}
		
		return nrCfop;
	}
}