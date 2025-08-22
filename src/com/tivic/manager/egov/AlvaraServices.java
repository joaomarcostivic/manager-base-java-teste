package com.tivic.manager.egov;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.GregorianCalendar;
import java.util.HashMap;
import sol.dao.ResultSetMap;
import sol.util.Result;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.grl.Setor;
import com.tivic.manager.grl.SetorDAO;
import com.tivic.manager.grl.TipoLogradouro;
import com.tivic.manager.grl.TipoLogradouroDAO;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoDAO;
import com.tivic.manager.ptc.DocumentoPessoa;
import com.tivic.manager.ptc.DocumentoPessoaDAO;
import com.tivic.manager.ptc.DocumentoServices;
import com.tivic.manager.ptc.TipoDocumento;
import com.tivic.manager.ptc.TipoDocumentoDAO;
import com.tivic.manager.ptc.TipoDocumentoServices;
import com.tivic.manager.util.Util;

public class AlvaraServices {

	@SuppressWarnings({ "resource" })
	public static Result getAlvara(String nrInscricaoMunicipal) {
		Connection connect  = null;
		Connection conLocal = Conexao.conectar();
		try {
			connect = IptuServices.getConnection("ISS");
			/*
			 * DADOS DO CONTRIBUINTE - Validando número da inscrição
			 */
			nrInscricaoMunicipal = nrInscricaoMunicipal != null ? nrInscricaoMunicipal.replaceAll("[\\.]", "") : "";
			if (nrInscricaoMunicipal == null || nrInscricaoMunicipal.equals(""))
				return new Result(-1, "Informe a Inscrição Municipal!");
			
			
			int cdMunicipio = 3965;
			int nrExercicio = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("NR_ANO_BASE", 2014, 0);
			String dsFinalidade         = "LOCALIZAÇÃO E FUNCIONAMENTO";
			String nmContribuinte = "";
			String nmFantasia = "";
			String nrInscricaoAnterior = "";
			String nrInscricaoEstadual = "";
			String nrCpfCnpj = "";
			// Atividade Principal
			String dtInicioAtividade    = "";
			String nmAtividadePrincipal = "";
			String nrAtividadePrincipal = "";
			// Endere~co
			String nmTipoLogradouro = "";
			String nmLogradouro = "";
			String nmBairro = "";
			String nmComplemento = "";
			String nrCep = "";
			String nrEndereco = "";
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(
									  "SELECT indice1 AS nrInscricaoMunicipal, P2 AS nmContribuinte, P3 AS nmFantasia, "
									+ "       P8 AS nmTipoLogradouro, P9 AS nmLogradouro, P10 AS nrEndereco, "
									+ "       P11 AS nmComplementoEndereco, P12 AS nmBairro, P13 AS nrCep,"
									+ "       P6 AS nrCpfCnpj "
									+ "FROM SQLUser.sqlISSBEC00 "
									+ "WHERE indice1 = \'" + nrInscricaoMunicipal + "\'"
									+ "  AND indice2 = 1 ").executeQuery());
			if (rsm.next()) {
				nmContribuinte   = rsm.getString("nmContribuinte");
				nmTipoLogradouro = rsm.getString("nmTipoLogradouro");
				nmLogradouro     = rsm.getString("nmLogradouro");
				nrEndereco       = rsm.getString("nrEndereco");
				nmComplemento    = rsm.getString("nmComplementoEndereco");
				nmBairro         = rsm.getString("nmBairro");
				nrCep            = rsm.getString("nrCep");
				nrCpfCnpj        = rsm.getString("nrCpfCnpj");
				nmFantasia       = rsm.getString("nmFantasia");
				nrCpfCnpj        = nrCpfCnpj == null ? "" : nrCpfCnpj;
			} 
			else
				return new Result(-1, "Nº de Inscrição Inválida!");
			// System.out.println(" \nBuscou dados cadastrais: "+((new GregorianCalendar().getTimeInMillis() - init.getTimeInMillis()) / 1000)+"s");
			/*****************************************************************************************************************************************************
			 * Salvando pessoa
			 ****************************************************************************************************************************************************/
			int cdPessoa = 0;
			int cdEmpresa           = 0;
			int gnPessoa 			= (nrCpfCnpj.length() > 11 ? PessoaServices.TP_JURIDICA : PessoaServices.TP_FISICA);
			ResultSet rs 			= conLocal.prepareStatement("SELECT cd_empresa FROM grl_empresa ").executeQuery();
			if (rs.next())
				cdEmpresa 			= rs.getInt("cd_empresa");
			rs = conLocal.prepareStatement( "SELECT * FROM grl_pessoa_" + (gnPessoa == PessoaServices.TP_FISICA ? "fisica " : "juridica ")+
							                "WHERE " + (gnPessoa == PessoaServices.TP_FISICA ? "nr_cpf " : "nr_cnpj ") + " = \'" + nrCpfCnpj + "\'").executeQuery();
			if (rs.next())
				cdPessoa = rs.getInt("cd_pessoa");
			else {
				cdPessoa = Conexao.getSequenceCode("grl_pessoa", conLocal);
				PreparedStatement pstmt = conLocal.prepareStatement("INSERT INTO grl_pessoa (cd_pessoa, nm_pessoa, dt_cadastro, gn_pessoa) VALUES (?, ?, ?, ?)");
				pstmt.setInt(1, cdPessoa);
				pstmt.setString(2, nmContribuinte);
				pstmt.setTimestamp(3, new Timestamp(new GregorianCalendar().getTimeInMillis()));
				pstmt.setInt(4, gnPessoa);
				pstmt.executeUpdate();
				// Inserindo FISICA / JURÍDICA
				if (nrCpfCnpj.length() <= 11)
					pstmt = conLocal.prepareStatement("INSERT INTO grl_pessoa_fisica (cd_pessoa, nr_cpf) VALUES (?, ?)");
				else
					pstmt = conLocal.prepareStatement("INSERT INTO grl_pessoa_juridica (cd_pessoa, nr_cnpj, nr_inscricao_municipal) VALUES (?, ?, ?)");
				pstmt.setInt(1, cdPessoa);
				pstmt.setString(2, nrCpfCnpj);
				if (nrCpfCnpj.length() > 11)
					pstmt.setString(3, nrInscricaoMunicipal);
				pstmt.executeUpdate();
				// Salvando ENDEREÇO
				rs = conLocal.prepareStatement("SELECT cd_empresa FROM grl_empresa ").executeQuery();
				if (rs.next())
					cdEmpresa = rs.getInt("cd_empresa");
				int cdTipoLogradouro = 0;
				pstmt = conLocal.prepareStatement("SELECT * FROM grl_tipo_logradouro "
								+ "WHERE nm_tipo_logradouro = \'"
								+ nmTipoLogradouro + "\'");
				ResultSet rsTemp = pstmt.executeQuery();
				if (rsTemp.next())
					cdTipoLogradouro = rsTemp.getInt("cd_tipo_logradouro");
				else
					cdTipoLogradouro = TipoLogradouroDAO.insert(new TipoLogradouro(0, nmTipoLogradouro, nmTipoLogradouro), conLocal);
				//
				int cdTipoEndereco = 0;
				int cdCidade = ParametroServices.getValorOfParametroAsInteger("CD_CIDADE_END_DEFAULT", 0, cdEmpresa, conLocal);
				pstmt = conLocal.prepareStatement("INSERT INTO grl_pessoa_endereco (cd_pessoa, cd_endereco, cd_tipo_logradouro, cd_tipo_endereco, cd_cidade, nm_logradouro, "
								+ "                                 nm_bairro, nr_cep, nr_endereco, nm_complemento, lg_principal) "
								+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1) ");
				pstmt.setInt(1, cdPessoa);
				pstmt.setInt(2, 1 /* cdEndereco */);
				pstmt.setInt(3, cdTipoLogradouro);
				if (cdTipoEndereco > 0)
					pstmt.setInt(4, cdTipoEndereco);
				else
					pstmt.setNull(4, Types.INTEGER);
				if (cdCidade > 0)
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
			/*****************************************************************************************************************************************************
			 * VERIFICANDO SE ESTÁ NA GLOBAL DE EXCEÇÃO (ISSNAOALVARA)
			 ****************************************************************************************************************************************************/
			rs = connect.prepareStatement("SELECT * FROM SQLUser.sqlISSNAOALVARA "+
					                      "WHERE nrInscricao  = \'"+nrInscricaoMunicipal+"\'").executeQuery();
			if(rs.next())
				return new Result(-1, "Contribuinte sem permissão para emitir Alvará!");
			/*****************************************************************************************************************************************************
			 * VERIFICANDO SE ESTÁ NA GLOBAL DE EXCEÇÃO (ISSBE190)
			 ****************************************************************************************************************************************************/
			rs = connect.prepareStatement("SELECT * FROM SQLUser.sqlISSBE190 "+
					                      "WHERE nrInscricao  = \'"+nrInscricaoMunicipal+"\'").executeQuery();
			if(rs.next())
				return new Result(-1, rs.getString("P01"));
			/*****************************************************************************************************************************************************
			 * VERIFICANDO SE A TLL FOI PAGA
			 ****************************************************************************************************************************************************/
			int nrAnoBase = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("NR_ANO_BASE", 2014, 0);
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
										  "  AND Indice3  = 2101").executeQuery();
			int i = 0;
			while(rs.next())	{
				i++;
				String venc  = rs.getString("dtVencimento"+i); 
				String pagto = rs.getString("dtPagamento"+i);
				// Se houver os dois pontos no campo do valor, não é uma parcela
				if(rs.getString("vlParcela"+i)==null || rs.getString("vlParcela"+i).indexOf(":")>=0 || venc==null || venc.length()<8) 
					continue;
				//
				if(pagto==null || pagto.trim().length()==0)
					return new Result(-1, "Pagamento da Taxa de Licenciamento e Localização ainda pendente!");
					
			}
			/*****************************************************************************************************************************************************
			 * SALVANDO O ALVARÁ (DOCUMENTO)
			 ****************************************************************************************************************************************************/
			int cdTipoDocumento     = getCdTipoDocumentoAlvara(conLocal);
			int cdSetor             = getCdSetorAlvara(cdEmpresa, conLocal);
			int cdSituacaoArquivado = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_ARQUIVADO", 0, 0, conLocal);
			int cdFase              = ParametroServices.getValorOfParametroAsInteger("CD_FASE_INICIAL", 0, 0, conLocal);
			int cdUsuario           = 0;
			String nrDocumento		= "";
			String idDocumento		= "";
			GregorianCalendar dtEmissao = null;
			GregorianCalendar dtValidade = null ;
			rs = conLocal.prepareStatement( "SELECT A.* FROM ptc_documento A, grl_pessoa_juridica B, ptc_documento_pessoa C " +
											"WHERE A.cd_tipo_documento      = " + cdTipoDocumento + 
											"  AND A.cd_setor               = "+cdSetor+ 
											"  AND A.cd_documento           = C.cd_documento "+ 
											"  AND C.cd_pessoa              = B.cd_pessoa "+
											"  AND B.cd_pessoa              = "+cdPessoa+ 
											"  AND B.nr_inscricao_municipal = \'"+ nrInscricaoMunicipal +"\' " + 
											"  AND EXTRACT(YEAR FROM A.dt_protocolo) = " + nrExercicio +
											" ORDER BY A.dt_protocolo DESC " +
											" LIMIT 1").executeQuery();
			if (rs.next()) {
				nrDocumento = rs.getString("nr_documento");
				idDocumento = rs.getString("id_documento");
				dtEmissao = Util.convTimestampToCalendar(rs.getTimestamp("dt_protocolo"));
				dtValidade = (GregorianCalendar) dtEmissao.clone();
				dtValidade.set(GregorianCalendar.DAY_OF_MONTH, 20);
				dtValidade.set(GregorianCalendar.MONTH, 1/*FEVEREIRO*/);
				dtValidade.add(GregorianCalendar.YEAR, 1);
			}
			else {
				nrDocumento      = TipoDocumentoServices.getNextNumeracao(cdTipoDocumento, cdEmpresa, true, conLocal);
				// Criando um md5 a partir do idDocumento
				MessageDigest md = MessageDigest.getInstance("MD5");  
				BigInteger hash = new BigInteger(1, md.digest((nrCpfCnpj+nrDocumento+new GregorianCalendar()).getBytes()));  
				idDocumento      = hash.toString(16); 
	//			String idDocumento      = Long.toHexString(new Long(nrCpfCnpj).longValue()) + nrDocumento.substring(0, 3) + String.valueOf(new GregorianCalendar().getMaximum(Calendar.SECOND));
				while (idDocumento.length() < 16)
					idDocumento += "0";
				idDocumento = (idDocumento.substring(0, 4) + "."+
						       idDocumento.substring(4, 8) + "."+
						       idDocumento.substring(8, 12) + "." + 
						       idDocumento.substring(12, 16)).toUpperCase();
	
				String txtObservacao = "";
				// DATA DE EMISSÃO E VALIDADE
				dtEmissao  = new GregorianCalendar();
				dtValidade = (GregorianCalendar) dtEmissao.clone();
				dtValidade.set(GregorianCalendar.DAY_OF_MONTH, 28);
				dtValidade.set(GregorianCalendar.MONTH, 1/*FEVEREIRO*/);
				dtValidade.add(GregorianCalendar.YEAR, 1);
				// Salvando documento Alvara
				Documento documento = new Documento(0, 0 /* cdArquivo */, cdSetor, cdUsuario, "SEFIN" /* nmLocalOrigem */,
													dtEmissao /* dtProtocolo */, DocumentoServices.TP_PUBLICO/* tpDocumento */,
													txtObservacao, idDocumento, nrDocumento, cdTipoDocumento,0 /* cdServico */, 0 /* cdAtendimento */,
													"" /* txtDocumento */, cdSetor /* Atual */,cdSituacaoArquivado, cdFase, cdEmpresa, 
													0 /* cdProcesso */, 0 /*tpPrioridade*/, 0/*cdDocumentoSuperior*/, 
													null /*dsAssunto*/, null /*nrAtendimento*/, 0/*lgNotificacao*/, 0 /*cdTipoDocumentoAnterior*/, 
													null/*nrDocumentoExterno*/, null/*nrAssunto*/,null, null, 0, 1);
				int cdDocumento = DocumentoDAO.insert(documento, conLocal);
				if (cdDocumento <= 0)
					new Result(cdDocumento, "Falha ao salvar Alvará!");
				DocumentoPessoaDAO.insert(new DocumentoPessoa(cdDocumento, cdPessoa, "Solicitante"), conLocal);
			}
			
			/*****************************************************************************************************************************************************
			 * ATIVIDADES
			 ****************************************************************************************************************************************************/
			PreparedStatement pstmtAtvBaixada = connect.prepareStatement("SELECT * FROM SQLUser.sqlISSBEC05 WHERE nrInscricao = ? AND nrAtividade = ?"); // Atividades Baixadas
			PreparedStatement pstmt  = connect.prepareStatement("SELECT * FROM SQLUser.sqlISSAEC00 WHERE indice1 = ?"); // Nome do Serviço
			ResultSetMap rsmServicos = new ResultSetMap();
			ResultSet rsTemp         = connect.prepareStatement("SELECT * FROM SQLUser.sqlISSBEC00 "+
					                                            "WHERE indice1 = \'" + nrInscricaoMunicipal + "\' " + 
					                                            "  AND indice2 = 2 ").executeQuery();
			if (rsTemp.next()) {
				for (i = 1; i < 9; i += 2) {
					if (rsTemp.getString("P" + i) != null) {
						// Buscando nome da atividade
						pstmt.setString(1, rsTemp.getString("P" + i));
						ResultSet rsServ = pstmt.executeQuery();
						String nmAtividade = "";
						if (rsServ.next())
							nmAtividade = rsServ.getString("P01");
						// Data
						String dtAtividade = rsTemp.getString("P" + (i + 1));
						if (dtAtividade.length() < 8)
							dtAtividade = dtAtividade.substring(0, 2) + "/" + dtAtividade.substring(2, 4) + "/19" + dtAtividade.substring(4, 6);
						else
							dtAtividade = dtAtividade.substring(0, 2) + "/" + dtAtividade.substring(2, 4) + "/" + dtAtividade.substring(4, 8);
						// Código
						String cdAtividade = rsTemp.getString("P" + i);
						if(i==1) {
							if(cdAtividade.equals("9999998"))
								return new Result(-1, "INATIVO. NÃO EXISTE NO ENDEREÇO INDICADO.");
							// Verificando Atividade
							pstmtAtvBaixada.setString(1, nrInscricaoMunicipal);
							pstmtAtvBaixada.setString(2, cdAtividade);
							if(pstmtAtvBaixada.executeQuery().next())
								return new Result(-1, "Atividade principal baixada. Não é permitido emitir alvará!");
							//
							while (cdAtividade.length() < 10)
								cdAtividade = "0" + cdAtividade;
							nrAtividadePrincipal = cdAtividade;
							dtInicioAtividade    = dtAtividade;
							nmAtividadePrincipal = nmAtividade;
						}
						else {
							// Inclui atividade apenas se não estiver baixada
							pstmtAtvBaixada.setString(1, nrInscricaoMunicipal);
							pstmtAtvBaixada.setString(2, cdAtividade);
							if(!pstmtAtvBaixada.executeQuery().next()) {
								while (cdAtividade.length() < 10)
									cdAtividade = "0" + cdAtividade;
								//
								HashMap<String, Object> register = new HashMap<String, Object>();
								register.put("CD_ATIVIDADE", cdAtividade);
								register.put("NM_ATIVIDADE", nmAtividade);
								register.put("DT_ATIVIDADE", dtAtividade);
								rsmServicos.addRegister(register);
							}
						}
					}
				}
			}
			
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("nrExercicio", nrExercicio);
			param.put("cdMunicipio", cdMunicipio);
			param.put("nmFantasia2", nmFantasia);
			param.put("nmRazaoSocial2", nmContribuinte);
			param.put("nrInscricao", nrInscricaoMunicipal.subSequence(0, nrInscricaoMunicipal.length() - 1) + "-" + nrInscricaoMunicipal.subSequence(nrInscricaoMunicipal.length() - 1, nrInscricaoMunicipal.length()));
			param.put("dsFinalidade", dsFinalidade);
			param.put("nrAlvara", nrDocumento);
			param.put("nrInscricaoAnterior", nrInscricaoAnterior);
			param.put("dsNaturezaJuridica", (gnPessoa == PessoaServices.TP_FISICA ? "PESSOA FÍSICA" : "PESSOA JURÍDICA"));
			param.put("nrCpfCnpj", (gnPessoa == PessoaServices.TP_FISICA ? Util.formatCpf(nrCpfCnpj) : Util.formatCnpj(nrCpfCnpj)));
			param.put("nrInscricaoEstadual", nrInscricaoEstadual);
			param.put("dsEndereco", Util.formatEndereco(nmTipoLogradouro, nmLogradouro, nrEndereco, nmComplemento, nmBairro, "" /* nrCep */, "" /* nmMunicipio */, ""/* sgEstado */, ""));
			param.put("nmBairro", nmBairro.trim());
			param.put("nrCep", Util.format(nrCep, "##.###-###", true));
			// Atividade Principal
			param.put("nrAtividadePrincipal", nrAtividadePrincipal);
			param.put("dtInicioAtividade", dtInicioAtividade);
			param.put("nmAtividadePrincipal", nmAtividadePrincipal);
			param.put("rsmServicos", rsmServicos.getLines());
			// Dados da Emissão
			param.put("hrEmissao",  Util.formatDate(dtEmissao, "HH:mm:ss"));
			param.put("dtEmissao",  Util.formatDate(dtEmissao, "dd/MM/yyyy"));
			param.put("dtValidade", Util.formatDate(dtValidade, "dd/MM/yyyy"));
			param.put("nrControle", idDocumento);
			Result result = new Result(1, "Alvará Nº: "+nrDocumento+", Nº Controle: "+idDocumento);
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			return result;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar gerar o Alvará", e);
		} finally {
			Conexao.desconectar(connect);
		}
	}

	private static int getCdTipoDocumentoAlvara(Connection connect) throws Exception {
		String nmTipoDocumento = "Alvara";
		PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM ptc_tipo_documento WHERE nm_tipo_documento = \'" + nmTipoDocumento + "\'");
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
			return rs.getInt("cd_tipo_documento");
		else
			return TipoDocumentoDAO.insert(new TipoDocumento(0, nmTipoDocumento, null, com.tivic.manager.ptc.TipoDocumentoServices.TP_INC_COM_ANO,
					0, 0, 0, 0, null, null, 0, 0, 0, null), connect);

	}

	private static int getCdSetorAlvara(int cdEmpresa, Connection connect) throws Exception {
		String nmSetor = "SEFIN";
		PreparedStatement pstmt = connect .prepareStatement("SELECT * FROM grl_setor WHERE nm_setor = \'" + nmSetor + "\'");
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
			return rs.getInt("cd_setor");
		else
			return SetorDAO.insert(new Setor(0, 0/* cdSetorSuperior */, cdEmpresa,0/* cdResponsavel */, nmSetor, 1/* stSetor */,
							""/* nmBairro */, ""/* nmLogradouro */, ""/* nrCep */,""/* nrEndereco */, ""/* nmComplemento */,
							""/* nrTelefone */, ""/* nmPontoReferencia */,0 /* lgEstoque */, ""/* nrRamal */, ""/* idSetor */,
							""/* sgSetor */, 0/* tpSetor */, 0 /*lgRecepcao*/, null/*cdSetorExterno*/), connect);
	}

	public static Result validarAlvara(String nrInscMunicipal, String nrAlvara, String nrControle)	{
		Connection connect  = Conexao.conectar();
		Connection conCACHE = null;
		try	{
			conCACHE = IptuServices.getConnection("ISS");
			//
			Result result = new Result(-1, "CERTIDÃO NÃO LOCALIZADA!");
			nrInscMunicipal = nrInscMunicipal!=null ? nrInscMunicipal.replaceAll("[\\.]", "").replaceAll("[\\-]", "").replaceAll("[\\/]", "") : "";
			// Verificando Inscrição Municipal
			if(nrInscMunicipal==null || nrInscMunicipal.equals(""))
				return new Result(-1, "Informe o número da Inscrição Municipal!");
			//
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, C.nm_pessoa "+
					                                           "FROM ptc_documento A, ptc_documento_pessoa B, grl_pessoa C, grl_pessoa_juridica D " +
										                       "WHERE A.cd_documento  = B.cd_documento " +
										                       "  AND B.cd_pessoa     = C.cd_pessoa " +
										                       "  AND B.cd_pessoa     = D.cd_pessoa " +
										                       "  AND A.nr_documento  = ? "+
										                       "  AND A.id_documento  = ? "+
										                       "  AND D.nr_inscricao_municipal = ? ");
			pstmt.setString(1, nrAlvara);
			pstmt.setString(2, nrControle.toUpperCase());
			pstmt.setString(3, nrInscMunicipal);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())	{
				String dsMsg = "Alvará VALIDADO com sucesso!";
				GregorianCalendar dtEmissao  = Util.convTimestampToCalendar(rs.getTimestamp("dt_protocolo"));
				GregorianCalendar dtValidade = Util.convTimestampToCalendar(rs.getTimestamp("dt_protocolo"));
				dtValidade.set(GregorianCalendar.DAY_OF_MONTH, 28);
				dtValidade.set(GregorianCalendar.MONTH, 1/*FEVEREIRO*/);
				dtValidade.add(GregorianCalendar.YEAR, 1);
				// VERIFICANDO EXCEÇÕES
				ResultSet rsTmp = conCACHE.prepareStatement("SELECT * FROM SQLUser.sqlISSNAOALVARA "+
						                                    "WHERE nrInscricao  = \'"+nrInscMunicipal+"\'").executeQuery();
				if(rsTmp.next())
					dsMsg = "REVOGADO! Contribuinte sem permissão para emitir Alvará!";
				// VERIFICANDO EXCEÇÕES - 2
				rsTmp = conCACHE.prepareStatement("SELECT * FROM SQLUser.sqlISSBE190 "+
						                          "WHERE nrInscricao  = \'"+nrInscMunicipal+"\'").executeQuery();
				if(rsTmp.next())
					dsMsg = "REVOGADO! "+rsTmp.getString("P01");
				/*****************************************************************************************************************************************************
				 * ATIVIDADES
				 ****************************************************************************************************************************************************/
				PreparedStatement pstmtAtvBaixada = conCACHE.prepareStatement("SELECT * FROM SQLUser.sqlISSBEC05 "+
						                                                      "WHERE nrInscricao = ? AND nrAtividade = ?"); // Atividades Baixadas
				rsTmp         = conCACHE.prepareStatement("SELECT * FROM SQLUser.sqlISSBEC00 "+
						                                  "WHERE indice1 = \'" + nrInscMunicipal + "\' " + 
						                                  "  AND indice2 = 2 ").executeQuery();
				if (rsTmp.next())
					if (rsTmp.getString("P1") != null) {
						// Código
						String cdAtividade = rsTmp.getString("P1");
						if(cdAtividade.equals("9999998"))
							dsMsg = "REVOGADO. CONTRIBUINTE INATIVO. NÃO EXISTE NO ENDEREÇO INDICADO.";
						// Verificando Atividade
						pstmtAtvBaixada.setString(1, nrInscMunicipal);
						pstmtAtvBaixada.setString(2, cdAtividade);
						if(pstmtAtvBaixada.executeQuery().next())
							dsMsg = "REVOGADO. CONTRIBUINTE INATIVO.";
					}
				//
				result.setCode(1);
				result.addObject("dtEmissao",  Util.formatDate(dtEmissao, "dd/MM/yyyy HH:mm:ss"));
				result.addObject("dtValidade", Util.formatDate(dtValidade, "dd/MM/yyyy"));
				result.addObject("dsFinalidade", "LOCALIZAÇÃO E FUNCIONAMENTO");
				result.addObject("nmContribuinte", rs.getString("nm_pessoa"));
				result.setMessage(dsMsg);
			}
			return result;
		}	
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1000, "Falha desconhecida ao tentar validar CND!", e);
		}
		finally	{
			Conexao.desconectar(conCACHE);
			Conexao.desconectar(connect);
		}
	}
}
