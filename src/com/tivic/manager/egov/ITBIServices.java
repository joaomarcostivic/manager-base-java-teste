package com.tivic.manager.egov;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.intersys.jdbc.CacheDataSource;
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

import sol.dao.ResultSetMap;
import sol.util.Result;

public class ITBIServices {

	public static Result gerarProtocoloITBI(String nrInscricaoMobiliaria,
			String nrCpfCnpj, String nmAdquirente, String nmTipoLogradouro,
			String nmLogradouro, String nmComplemento, String nrEndereco,
			String nmBairro, String qtAreaTerreno, String qtAreaConstrucao,
			String vlM2Terreno, String vlM2Construcao, String vlTerreno,
			String vlConstrucao, String vlImovel, String prAliquota,
			String vlITBI, String vlValor, String dsEmail,
			String nmTipoContrato, String nmEntidadeFinanceira, String vlTransacao,
			String vlFinanciado, String nmResponsavelFinanciamento, String nmNacionalidadeAdquirente,
			String nmNaturalidadeAdquirente, String nmEstadoCivilAdquirente, String nmProfissaoAdquirente,
			String nrRGAdquirente, String nmEnderecoAdquirente, String nmTipoZona,
			String nmDistrito, String qtTestada, String qtFundo,
			String qtLadoDireito, String qtLadoEsquerdo, String nmCondicoesLegais,
			String nmCondicoesFiscais, String nmTransmitente, String nmEnderecoTransmitente,
			String nrCpfCnpjTransmitente, String qtAreaUtil, String qtAreaTotal,
			String nrPavimentos, String nrElevadores, String nrBanheiros,
			String nrDependencias, String vlFracaoIdeal, String nrGaragens,
			String nmDenominacaoPropriedade, String nmEstadoConservacao, String lgBenfeitorias, String txtDescricaoBenfeitorias) {
		Connection conLocal = Conexao.conectar();
		Connection connectIPT = null, connectISS = null;
		try {
			GregorianCalendar dataProtocolo = new GregorianCalendar();
			int gnPessoa = PessoaServices.TP_JURIDICA;
			nrInscricaoMobiliaria = nrInscricaoMobiliaria != null ? nrInscricaoMobiliaria.replaceAll("[\\.]", "")
					.replaceAll("[\\-]", "").replaceAll("[\\/]", "") : "";
			nrCpfCnpj = nrCpfCnpj != null ? nrCpfCnpj.replaceAll("[\\.]", "")
					.replaceAll("[\\-]", "").replaceAll("[\\/]", "") : "";
			// Verificando CPF
			if (nrCpfCnpj == null || nrCpfCnpj.equals(""))
				return new Result(-1, "Informe o número do CPF ou CNPJ!");
			while (nrCpfCnpj.length() < 11)
				nrCpfCnpj = "0" + nrCpfCnpj;
			// Verificando se é um "CPF" Válido
			if (nrCpfCnpj.length() <= 11) {
				gnPessoa = PessoaServices.TP_FISICA;
				if (!com.tivic.manager.util.Util.isCpfValido(nrCpfCnpj))
					return new Result(-2, "Número do CPF inválido!");
			}
			// CNPJ
			else if (!com.tivic.manager.util.Util.isCNPJ(nrCpfCnpj))
				return new Result(-2, "Número do CNPJ inválido!");

			// Estabelece a conexao utilizando o namespace IPT 
			connectIPT = getConnection("IPT");
			
			nrInscricaoMobiliaria 		= !nrInscricaoMobiliaria.equals("null") ? nrInscricaoMobiliaria.toUpperCase() : "";
			nmAdquirente 				= !nmAdquirente.equals("null") ? nmAdquirente.toUpperCase() : "";
			nmTipoLogradouro 			= !nmTipoLogradouro.equals("null") ? nmTipoLogradouro.toUpperCase() : "";
			nmLogradouro 				= !nmLogradouro.equals("null") ? nmLogradouro.toUpperCase() : "";
			nmComplemento 				= !nmComplemento.equals("null") ? nmComplemento.toUpperCase() : "";
			nrEndereco 					= !nrEndereco.equals("null") ? nrEndereco.toUpperCase() : "";
			nmBairro 					= !nmBairro.equals("null") ? nmBairro.toUpperCase() : "";
			qtAreaTerreno 				= !qtAreaTerreno.equals("null") ? qtAreaTerreno.toUpperCase() : "";
			qtAreaConstrucao 			= !qtAreaConstrucao.equals("null") ? qtAreaConstrucao.toUpperCase() : "";
			vlM2Terreno 				= !vlM2Terreno.equals("null") ? vlM2Terreno.toUpperCase() : "";
			vlM2Construcao 				= !vlM2Construcao.equals("null") ? vlM2Construcao.toUpperCase() : "";
			vlTerreno 					= !vlTerreno.equals("null") ? vlTerreno.toUpperCase() : "";
			vlConstrucao 				= !vlConstrucao.equals("null") ? vlConstrucao.toUpperCase() : "";
			vlImovel 					= !vlImovel.equals("null") ? vlImovel.toUpperCase() : "";
			prAliquota 					= !prAliquota.equals("null") ? prAliquota.toUpperCase() : "";
			vlITBI 						= !vlITBI.equals("null") ? vlITBI.toUpperCase() : "";
			vlValor 					= !vlValor.equals("null") ? vlValor.toUpperCase() : "";
			nmTipoContrato				= !nmTipoContrato.equals("null") ? nmTipoContrato.toUpperCase() : "";
			nmEntidadeFinanceira		= !nmEntidadeFinanceira.equals("null") ? nmEntidadeFinanceira.toUpperCase() : "";
			vlTransacao					= !vlTransacao.equals("null") ? vlTransacao.toUpperCase() : "";
			vlFinanciado				= !vlFinanciado.equals("null") ? vlFinanciado.toUpperCase() : "";
			nmResponsavelFinanciamento	= !nmResponsavelFinanciamento.equals("null") ? nmResponsavelFinanciamento.toUpperCase() : "";
			nmNacionalidadeAdquirente	= !nmNacionalidadeAdquirente.equals("null") ? nmNacionalidadeAdquirente.toUpperCase() : "";
			nmNaturalidadeAdquirente	= !nmNaturalidadeAdquirente.equals("null") ? nmNaturalidadeAdquirente.toUpperCase() : "";
			nmEstadoCivilAdquirente		= !nmEstadoCivilAdquirente.equals("null") ? nmEstadoCivilAdquirente.toUpperCase() : "";
			nmProfissaoAdquirente		= !nmProfissaoAdquirente.equals("null") ? nmProfissaoAdquirente.toUpperCase() : "";
			nrRGAdquirente				= !nrRGAdquirente.equals("null") ? nrRGAdquirente.toUpperCase() : "";
			nmEnderecoAdquirente		= !nmEnderecoAdquirente.equals("null") ? nmEnderecoAdquirente.toUpperCase() : "";
			nmTipoZona					= !nmTipoZona.equals("null") ? nmTipoZona.toUpperCase() : "";
			nmDistrito					= !nmDistrito.equals("null") ? nmDistrito.toUpperCase() : "";
			qtTestada					= !qtTestada.equals("null") ? qtTestada.toUpperCase() : "";
			qtFundo						= !qtFundo.equals("null") ? qtFundo.toUpperCase() : "";
			qtLadoDireito				= !qtLadoDireito.equals("null") ? qtLadoDireito.toUpperCase() : "";
			qtLadoEsquerdo				= !qtLadoEsquerdo.equals("null") ? qtLadoEsquerdo.toUpperCase() : "";
			nmCondicoesLegais			= !nmCondicoesLegais.equals("null") ? nmCondicoesLegais.toUpperCase() : "";
			nmCondicoesFiscais			= !nmCondicoesFiscais.equals("null") ? nmCondicoesFiscais.toUpperCase() : "";
			nmTransmitente				= !nmTransmitente.equals("null") ? nmTransmitente.toUpperCase() : "";
			nmEnderecoTransmitente		= !nmEnderecoTransmitente.equals("null") ? nmEnderecoTransmitente.toUpperCase() : "";
			nrCpfCnpjTransmitente		= !nrCpfCnpjTransmitente.equals("null") ? nrCpfCnpjTransmitente.toUpperCase() : "";
			qtAreaUtil					= !qtAreaUtil.equals("null") ? qtAreaUtil.toUpperCase() : "";
			qtAreaTotal					= !qtAreaTotal.equals("null") ? qtAreaTotal.toUpperCase() : "";
			nrPavimentos				= !nrPavimentos.equals("null") ? nrPavimentos.toUpperCase() : "";
			nrElevadores				= !nrElevadores.equals("null") ? nrElevadores.toUpperCase() : "";
			nrBanheiros					= !nrBanheiros.equals("null") ? nrBanheiros.toUpperCase() : "";
			nrDependencias				= !nrDependencias.equals("null") ? nrDependencias.toUpperCase() : "";
			vlFracaoIdeal				= !vlFracaoIdeal.equals("null") ? vlFracaoIdeal.toUpperCase() : "";
			nrGaragens					= !nrGaragens.equals("null") ? nrGaragens.toUpperCase() : "";
			nmDenominacaoPropriedade	= !nmDenominacaoPropriedade.equals("null") ? nmDenominacaoPropriedade.toUpperCase() : "";
			nmEstadoConservacao			= !nmEstadoConservacao.equals("null") ? nmEstadoConservacao.toUpperCase() : "";
			lgBenfeitorias				= !txtDescricaoBenfeitorias.equals("null") || !txtDescricaoBenfeitorias.trim().equalsIgnoreCase("") ? ""+1 : ""+0;
			txtDescricaoBenfeitorias	= !txtDescricaoBenfeitorias.equals("null") ? txtDescricaoBenfeitorias.toUpperCase() : "";
			
			Result result = new Result(1);
			
			// Verifica a posicao correta para inserir nova itbi
			ResultSet rs = connectIPT.prepareStatement(
					"SELECT count(*) as total FROM IPT.SQLITBINET")
					.executeQuery();
			rs.next();
			String nrSequencial = "" + (rs.getInt("total") + 1);
			
			// Verifica se ja existe uma solicitacao de avaliacao criada para esta inscricao 
			rs = connectIPT.prepareStatement(
					" SELECT count(*) as total, nrProtocolo as protocolo FROM IPT.SQLITBIAvaliacao av " +
					" where av.nrInscricaoMobiliaria = \'"+nrInscricaoMobiliaria+"\'"+
					" ORDER BY protocolo DESC").executeQuery();
			String nrDocumento = "";
			String idDocumento = "";
			int totalAvaliacao = 0;
			if (rs.next()) {			
				nrDocumento = rs.getString("protocolo"); 
				totalAvaliacao = rs.getInt("total");
			}
			connectISS = getConnection("ISS");
			String nrInscMunicipal = "";
			ResultSetMap rsm = new ResultSetMap(connectISS.prepareStatement("SELECT B.indice1 AS nrInscricaoMunicipal "+ 
															        	    "FROM SQLUser.sqlISSBEI97 A, SQLUser.sqlISSBEC00 B "+
															        	    "WHERE  A.indice3 = B.indice1 "+
															        	    "  		AND A.indice1 = \'"+nrCpfCnpj+"\'"+
															        	    "  		AND B.indice2 = 1 ").executeQuery());
			if(rsm.next())	{
				nrInscMunicipal  = rsm.getString("nrInscricaoMunicipal");
			}
			
			PreparedStatement pstmt;
			int cdEmpresa           = 0;
			rs = conLocal.prepareStatement("SELECT cd_empresa FROM grl_empresa ").executeQuery();
			if (rs.next())
				cdEmpresa = rs.getInt("cd_empresa");
			if (totalAvaliacao < 1) {
				//
				// Salvando pessoa
				int cdPessoa = 0;
				rs = conLocal.prepareStatement("SELECT * FROM grl_pessoa_"+(nrCpfCnpj.length()<=11?"fisica ":"juridica ")+
						"WHERE "+(gnPessoa==PessoaServices.TP_FISICA ? "nr_cpf " : "nr_cnpj ")+" = \'"+nrCpfCnpj+"\'").executeQuery();
				if(rs.next()) 
					cdPessoa = rs.getInt("cd_pessoa");
				else	{
					cdPessoa = Conexao.getSequenceCode("grl_pessoa", conLocal);
					pstmt = conLocal.prepareStatement("INSERT INTO grl_pessoa (cd_pessoa, nm_pessoa, dt_cadastro, gn_pessoa) " +
							"VALUES (?, ?, ?, ?)");
					pstmt.setInt(1, cdPessoa);
					pstmt.setString(2, nmAdquirente);
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
					rs = pstmt.executeQuery();
					if(rs.next())	
						cdTipoLogradouro = rs.getInt("cd_tipo_logradouro");
					else
						cdTipoLogradouro = TipoLogradouroDAO.insert(new TipoLogradouro(0, nmTipoLogradouro, nmTipoLogradouro), conLocal);
					//
					int cdTipoEndereco   = 0;
					int cdCidade         = ParametroServices.getValorOfParametroAsInteger("CD_CIDADE_END_DEFAULT", 0, cdEmpresa, conLocal);
					pstmt = conLocal.prepareStatement("INSERT INTO grl_pessoa_endereco (cd_pessoa, cd_endereco, cd_tipo_logradouro, cd_tipo_endereco, cd_cidade, nm_logradouro, " +
												  	  "									nm_bairro, nr_endereco, nm_complemento, lg_principal) " +
													  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 1) ");
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
					pstmt.setString(8, nrEndereco);
					pstmt.setString(9, nmComplemento);
					pstmt.executeUpdate();
				}
				
				int cdTipoDocumento     = getCdTipoDocumentoITBI(conLocal);
				// Tipo do DOCUMENTO
				int cdSetor             = getCdSetorITBI(cdEmpresa, conLocal);
				int cdSituacaoArquivado = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_ARQUIVADO", 0, 0, conLocal);
				int cdFase              = ParametroServices.getValorOfParametroAsInteger("CD_FASE_INICIAL", 0, 0, conLocal);
				int cdUsuario           = 0;
				nrDocumento      = TipoDocumentoServices.getNextNumeracao(cdTipoDocumento, cdEmpresa, true, conLocal);
				// Criando um md5 a partir do idDocumento
				MessageDigest md = MessageDigest.getInstance("MD5");  
				BigInteger hash = new BigInteger(1, md.digest((nrCpfCnpj+nrDocumento+new GregorianCalendar()).getBytes()));  
				idDocumento      = hash.toString(16);
//				idDocumento      = Long.toHexString(new Long(nrCpfCnpj).longValue())+nrDocumento.substring(0, 3)+String.valueOf(new GregorianCalendar().getMaximum(Calendar.SECOND));
				while(idDocumento.length() < 16)
					idDocumento += "0";
				idDocumento = (idDocumento.substring( 0,  4)+"."+
						idDocumento.substring( 4,  8)+"."+
						idDocumento.substring( 8, 12)+"."+
						idDocumento.substring(12, 16)).toUpperCase();
				
				String txtObservacao = nrInscricaoMobiliaria;
				
				// DATA DE EMISSÃO E VALIDADE
				GregorianCalendar dtEmissao  = new GregorianCalendar();
				GregorianCalendar dtValidade = (GregorianCalendar)dtEmissao.clone();
				dtValidade.add(Calendar.DATE, 90);
				// Salvando documento ITBI
				Documento documento = new Documento(0, 0 /*cdArquivo*/, cdSetor, cdUsuario, "SEFIN" /*nmLocalOrigem*/, dtEmissao /*dtProtocolo*/,
													DocumentoServices.TP_PUBLICO/*tpDocumento*/, txtObservacao, idDocumento, nrDocumento,
													cdTipoDocumento, 0 /*cdServico*/, 0 /*cdAtendimento*/, "" /*txtDocumento*/, cdSetor /*Atual*/,
													cdSituacaoArquivado, cdFase, cdEmpresa, 0 /*cdProcesso*/, 0 /*tpPrioridade*/, 0/*cdDocumentoSuperior*/, 
													null /*dsAssunto*/, null /*nrAtendimento*/, 0/*lgNotificacao*/, 0 /*cdTipoDocumentoAnterior*/, 
													null/*nrDocumentoExterno*/, null/*nrAssunto*/,null,null, 0, 1);
				int cdDocumento = DocumentoDAO.insert(documento, conLocal);
				if(cdDocumento <= 0)
					new Result(cdDocumento, "Falha ao salvar ITBI!");
				DocumentoPessoaDAO.insert(new DocumentoPessoa(cdDocumento,cdPessoa,"Solicitante"), conLocal);
			}
			
			rs = conLocal.prepareStatement(
					" SELECT id_documento FROM ptc_documento " +
				    " WHERE cd_tipo_documento = " + getCdTipoDocumentoITBI(conLocal) +
					" AND cd_setor = " + getCdSetorITBI(cdEmpresa, conLocal) +
					" AND nr_documento = \'"+ nrDocumento+"\'").executeQuery();
			if (rs.next()) {			
				idDocumento = rs.getString("id_documento"); 
			}
			
			result.setMessage("ITBI cadastrado com sucesso!");
			result.addObject("nrDocumento", nrDocumento);
			result.addObject("idDocumento", idDocumento);
			result.addObject("dataProtocolo",  Util.formatDate(dataProtocolo, "dd/MM/yyyy"));
			
			// Insere na tabela SQLITBINET um novo ITBI
			pstmt = connectIPT.prepareStatement(
									" INSERT INTO IPT.SQLITBINET " +
									"	(nrInscricaoMobiliaria, nrCpfCnpj, nmAdquirente, nmTipoLogradouro, nmLogradouro, nmComplemento, nrEndereco, nmBairro," +
									"	 qtAreaTerreno, qtAreaConstrucao, vlM2Terreno, vlM2Construcao, vlTerreno, " +
									"	 vlConstrucao, vlImovel, prAliquota, vlITBI, vlValor, nrSequencial, tpSolicitacao, dsEmail, nrProtocolo, "+ 
									"	 nmTipoContrato, nmEntidadeFinanceira, vlTransacao, vlFinanciado, nmResponsavelFinanciamento, nmNacionalidadeAdquirente, " +
									"	 nmNaturalidadeAdquirente, nmEstadoCivilAdquirente, nmProfissaoAdquirente, " +
									"	 nrRGAdquirente, nmEnderecoAdquirente, nmTipoZona, nmDistrito, qtTestada, qtFundo, qtLadoDireito, " +
									"	 qtLadoEsquerdo, nmCondicoesLegais, nmCondicoesFiscais, nmTransmitente, nmEnderecoTransmitente, nrCpfCnpjTransmitente, " +
									"	 qtAreaUtil, qtAreaTotal, nrPavimentos, nrElevadores, nrBanheiros, nrDependencias, vlFracaoIdeal, " +
									"	 nrGaragens, nmDenominacaoPropriedade, nmEstadoConservacao, lgBenfeitorias, txtDescricaoBenfeitorias)" +
									" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
									"		  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
									"		  ?, ?, ?, ?, ?, ?) ");
			pstmt.setString(1, nrInscricaoMobiliaria);
			pstmt.setString(2, nrCpfCnpj);
			pstmt.setString(3, nmAdquirente);
			pstmt.setString(4, nmTipoLogradouro);
			pstmt.setString(5, nmLogradouro);
			pstmt.setString(6, nmComplemento);
			pstmt.setString(7, nrEndereco);
			pstmt.setString(8, nmBairro);
			pstmt.setString(9, qtAreaTerreno);
			pstmt.setString(10, qtAreaConstrucao);
			pstmt.setString(11, vlM2Terreno);
			pstmt.setString(12, vlM2Construcao);
			pstmt.setString(13, vlTerreno);
			pstmt.setString(14, vlConstrucao);
			pstmt.setString(15, vlImovel);
			pstmt.setString(16, prAliquota);
			pstmt.setString(17, vlITBI);
			pstmt.setString(18, vlValor);
			pstmt.setString(19, nrSequencial);
			pstmt.setString(20, "1" /*tipo de solicitacao 1 = solicitacao de itbi*/);
			pstmt.setString(21, dsEmail);
			pstmt.setString(22, nrDocumento);
			pstmt.setString(23, nmTipoContrato);
			pstmt.setString(24, nmEntidadeFinanceira);
			pstmt.setString(25, vlTransacao);
			pstmt.setString(26, vlFinanciado);
			pstmt.setString(27, nmResponsavelFinanciamento);
			pstmt.setString(28, nmNacionalidadeAdquirente);
			pstmt.setString(29, nmNaturalidadeAdquirente);
			pstmt.setString(30, nmEstadoCivilAdquirente);
			pstmt.setString(31, nmProfissaoAdquirente);
			pstmt.setString(32, nrRGAdquirente);
			pstmt.setString(33, nmEnderecoAdquirente);
			pstmt.setString(34, nmTipoZona);
			pstmt.setString(35, nmDistrito);
			pstmt.setString(36, qtTestada);
			pstmt.setString(37, qtFundo);
			pstmt.setString(38, qtLadoDireito);
			pstmt.setString(39, qtLadoEsquerdo);
			pstmt.setString(40, nmCondicoesLegais);
			pstmt.setString(41, nmCondicoesFiscais);
			pstmt.setString(42, nmTransmitente);
			pstmt.setString(43, nmEnderecoTransmitente);
			pstmt.setString(44, nrCpfCnpjTransmitente);
			pstmt.setString(45, qtAreaUtil);
			pstmt.setString(46, qtAreaTotal);
			pstmt.setString(47, nrPavimentos);
			pstmt.setString(48, nrElevadores);
			pstmt.setString(49, nrBanheiros);
			pstmt.setString(50, nrDependencias);
			pstmt.setString(51, vlFracaoIdeal);
			pstmt.setString(52, nrGaragens);
			pstmt.setString(53, nmDenominacaoPropriedade);
			pstmt.setString(54, nmEstadoConservacao);
			pstmt.setString(55, lgBenfeitorias);
			pstmt.setString(56, txtDescricaoBenfeitorias);
			pstmt.executeUpdate();
			result.addObject("nrInscricaoMobiliaria", nrInscricaoMobiliaria);
			
			return result;
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1000, "Falha desconhecida ao tentar gerar ITBI!",
					e);
		} finally {
			Conexao.desconectar(conLocal);
			try {
				connectIPT.close();
			} catch (Exception e) {
			}
		}
	}
	
	private static int getCdTipoDocumentoITBI(Connection connect) throws Exception	{
		String nmTipoDocumento = "Imposto Sobre a Transmissao de Bens Imoveis";
		PreparedStatement pstmt  = connect.prepareStatement("SELECT * FROM ptc_tipo_documento " +
				   						   					 "WHERE nm_tipo_documento = \'"+nmTipoDocumento+"\'");
		ResultSet rs = pstmt.executeQuery();
		if(rs.next())	
			return rs.getInt("cd_tipo_documento");
		else
			return TipoDocumentoDAO.insert(new TipoDocumento(0, nmTipoDocumento, null, com.tivic.manager.ptc.TipoDocumentoServices.TP_INC_SEQ_GERAL,
					0, 0, 0, 0, null, null, 0, 0, 0, null), connect);
		
	}
	
	private static int getCdSetorITBI(int cdEmpresa, Connection connect) throws Exception	{
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
	
	public static Result getDadosOfInscricao(String nrInscricao)	{
		Connection connect = Conexao.conectar();
		try	{
			nrInscricao = nrInscricao!=null ? nrInscricao.replaceAll("[\\.]", "") : "";
			connect   = getConnection("IPT");
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
			
			// Dados do contribuinte
			String sql = "SELECT top 1 \'"+nrInscricao+"\' AS nrInscricao, "+
					     "       A1.nmCampo28/100 AS vlAreaTerreno, A1.nmCampo30/100 AS vlAreaConstrucao, A1.nmCampo2 AS tpCategoria, A1.nrCpfCnpj, "+
			             "       A.*, B.nrAnoBase, B.tpCategoria, B.vlTerreno, B.vlM2Terreno, B.vlAreaTerreno, "+
			             "       B.nmCampo5/100 AS vlFatorCorrTerreno, B.nmCampo6, B.nmCampo7, "+
					     "       B.nmCampo13, B.nmCampo14, B.vlM2Construcao, B.vlConstrucao, B.vlFatorCorrConstrucao, B.vlVenal, " +
					     "       B.vlC1C4 AS prAliquota, B.vlC2C4," +
					     "       B.vlC3C4, B.vlC4C4, B.nrAnoBase, " +
					     "       D.nmTipoLogradouro AS nmTipoLogradouroImovel, D.nmLogradouro AS nmLogradouroImovel, " +
					     "       A.nmCampo4 AS nrEndereco, A.nmCampo5 AS nmComplementoImovel, E.nmBairro AS nmBairroImovel " +
					     "FROM IPT.SQLContribuinte A " +
					     "JOIN IPT.IPTBIC0101               A1 ON (A1.indice1 = "+i1+" AND A1.indice2 = "+i2+" AND A1.indice3 = "+i3+" AND A1.indice4 = "+i4+" AND A1.indice5 = "+i5+" AND A1.indice6 = 1) " +
					     "JOIN IPT.SQLViewCalculo           B ON (B.indice1 = "+i1+" AND B.indice2 = "+i2+" AND B.indice3 = "+i3+" AND B.indice4 = "+i4+" AND B.indice5 = "+i5+") " +
					     "LEFT OUTER JOIN IPT.SQLLogradouro D ON (A.nmCampo2 = D.indice1) " +
					     "JOIN IPT.IPTBLC01                 E ON (A.nmCampo2 = E.indice3 AND E.indice4||E.indice5 = A.nmCampo3) " +
	                     "WHERE A.indice1 = "+i1+" AND A.indice2 = "+i2+" AND A.indice3 = "+i3+" AND A.indice4 = "+i4+" AND A.indice5 = "+i5;
			Result resultado = new Result(1, "Processado com sucesso!");
			resultado.addObject("rsm", new ResultSetMap(connect.prepareStatement(sql).executeQuery()));
			return resultado;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
		
	public static Result validaAvaliacao(String nrProtocolo, String nrInscricaoImobiliaria, String cdControle) {
		boolean isConnectionNull = true;
		Connection connectLocal = Conexao.conectar(), connection = null;
		try {
			ResultSetMap rsm = new ResultSetMap(connectLocal.prepareStatement("SELECT * FROM ptc_documento " +
												" WHERE txt_observacao = \'" + nrInscricaoImobiliaria + "\' AND id_documento = \'" + cdControle + "\'" + 
												" 	AND nr_documento = \'" + nrProtocolo + "\'").executeQuery());
			if(!rsm.next())
				return new Result(-3, "O documento não foi encontrado!");
			connection 			= isConnectionNull ? getConnection("IPT") : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			rsm = new ResultSetMap(connection.prepareStatement(
												" SELECT A.NRPROTOCOLO, A.NMREQUERENTE, A.NRCPFCNPJ, A.NRINSCRICAOMOBILIARIA, A.NMENDERECORESIDENCIAL, " + 
												"	 	 A.VLAREATERRENO, A.VLAREACONSTRUCAO, B.VLM2TERRENO, B.VLM2CONSTRUCAO, "+
												"	 	 B.VLIMOVEL, B.PRALIQUOTA, B.VLITBI, " + 
												"	 	 B.VLVALOR " + 
												" FROM IPT.SQLITBIAVALIACAO A "+ 
												" LEFT OUTER JOIN IPT.SQLITBINET B ON (A.NRPROTOCOLO = B.NRPROTOCOLO AND A.NRINSCRICAOMOBILIARIA= B.NRINSCRICAOMOBILIARIA) " + 
												" WHERE A.NRPROTOCOLO = \'"+nrProtocolo+"\' AND A.NRINSCRICAOMOBILIARIA = \'"+nrInscricaoImobiliaria+"\'").executeQuery());
			if (!rsm.next()) {
				return new Result(-4, "A Solicitação de Avaliação não foi encontrada!");
			}
			
			return new Result(1, "O laudo é valido!");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connectLocal);
			return new Result(-1, "Erro: " + e);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connectLocal);
		}
	}
	
	public static Result findAvaliacao(String nrProtocolo, String nrInscricaoImobiliaria) {
		boolean isConnectionNull = true;
		Connection connection = null, connectLocal = null;
		try {
			//
			String nrLote, nrQuadra;
			nrLote 					= nrQuadra = "---";
			//
			int   stAvaliacao 		= 0, stGuia = 0;
			float vlTaxaExpediente 	= 4;
			float prAliquota       	= 2;
			float vlTerritorial 	= 2, vlPredial = 3, vlITBI = 100;
			float vlTotal       	= vlITBI + vlTaxaExpediente;
			float vlImovel      	= vlTerritorial + vlPredial;
			String idDocumento		= "";
			GregorianCalendar dtValidade = null, dtEmissao = null;
			Result result = new Result(-2, "Falhou!");
			connectLocal = Conexao.conectar();
			ResultSetMap rsmTemp = new ResultSetMap(connectLocal.prepareStatement("SELECT dt_protocolo, id_documento FROM ptc_documento " +
																				  "WHERE txt_observacao = \'" + nrInscricaoImobiliaria + "\' " +
																				  "		 AND nr_documento = \'" + nrProtocolo + "\'").executeQuery());
			if (rsmTemp.next()) {
				dtEmissao 		= rsmTemp.getGregorianCalendar("dt_protocolo");
				dtValidade 		= (GregorianCalendar) dtEmissao.clone();
				dtValidade.add(GregorianCalendar.YEAR, 1);
				idDocumento		= rsmTemp.getString("id_documento");
			}
			else {
				result.setMessage("Documento não encontrado.");
				return result;
			}
			rsmTemp.beforeFirst();
			connection 			= isConnectionNull ? getConnection("IPT") : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement(
												" SELECT A.NRPROTOCOLO, A.NMREQUERENTE, A.NRCPFCNPJ, A.NRINSCRICAOMOBILIARIA, A.NMENDERECORESIDENCIAL, " + 
												"	 	 A.VLAREATERRENO, A.VLAREACONSTRUCAO, B.VLM2TERRENO, B.VLM2CONSTRUCAO, "+
												"	 	 B.VLIMOVEL, B.PRALIQUOTA, B.VLITBI, " + 
												"	 	 B.VLVALOR " + 
												" FROM IPT.SQLITBIAVALIACAO A "+ 
												" LEFT OUTER JOIN IPT.SQLITBINET B ON (A.NRPROTOCOLO = B.NRPROTOCOLO AND A.NRINSCRICAOMOBILIARIA = B.NRINSCRICAOMOBILIARIA) " + 
												" WHERE A.NRPROTOCOLO = \'"+nrProtocolo+"\' AND A.NRINSCRICAOMOBILIARIA = \'"+nrInscricaoImobiliaria+"\' AND B.TPSOLICITACAO = '0'").executeQuery());
			if (rsm.next()) {
				stAvaliacao = 1; // caso seja encontrado (teste)
				rsm.setValueToField("NRPROTOCOLO", rsm.getString("NRPROTOCOLO") != null ? rsm.getString("NRPROTOCOLO") : "");
				rsm.setValueToField("NMREQUERENTE", rsm.getString("NMREQUERENTE") != null ? rsm.getString("NMREQUERENTE") : "");
				rsm.setValueToField("NRCPFCNPJ", rsm.getString("NRCPFCNPJ") != null ? rsm.getString("NRCPFCNPJ") : "");
				rsm.setValueToField("NRINSCRICAOMOBILIARIA", rsm.getString("NRINSCRICAOMOBILIARIA") != null ? rsm.getString("NRINSCRICAOMOBILIARIA") : "");
				rsm.setValueToField("NMENDERECORESIDENCIAL", rsm.getString("NMENDERECORESIDENCIAL") != null ? rsm.getString("NMENDERECORESIDENCIAL") : "");
				rsm.setValueToField("NRLOTE", nrLote != null ? nrLote : "");
				rsm.setValueToField("NRQUADRA", nrQuadra != null ? nrQuadra : "");
				rsm.setValueToField("VLAREATERRENO", rsm.getString("VLAREATERRENO") != null ? rsm.getString("VLAREATERRENO") : "");
				rsm.setValueToField("VLAREACONSTRUCAO", rsm.getString("VLAREACONSTRUCAO") != null ? rsm.getString("VLAREACONSTRUCAO") : "");
				rsm.setValueToField("VLM2TERRENO", rsm.getString("VLM2TERRENO") != null ? rsm.getString("VLM2TERRENO") : "");
				rsm.setValueToField("VLM2CONSTRUCAO", rsm.getString("VLM2CONSTRUCAO") != null ? rsm.getString("VLM2CONSTRUCAO") : "");
				rsm.setValueToField("VLTERRITORIAL", "R$ "+Util.formatNumber(vlTerritorial));
				rsm.setValueToField("VLPREDIAL", "R$ "+Util.formatNumber(vlPredial));
				rsm.setValueToField("VLIMOVEL", "R$ "+Util.formatNumber(vlImovel));
				rsm.setValueToField("PRALIQUOTA", Util.formatNumber(prAliquota)+" %");
				rsm.setValueToField("VLITBI", "R$ "+Util.formatNumber(vlITBI));
				rsm.setValueToField("VLTAXAEXPEDIENTE", "R$ "+Util.formatNumber(vlTaxaExpediente));
				rsm.setValueToField("VLTOTAL", "R$ "+Util.formatNumber(vlTotal));
				rsm.setValueToField("STAVALIACAO", stAvaliacao==0 ? "Avaliação Pendente" : "Avaliação Disponível");
				rsm.setValueToField("STGUIA", stGuia==0 ? "Guia Pendente" : "Guia Disponível");
				// Dados da Emissão
				rsm.setValueToField("DTVALIDADE", Util.formatDate(dtValidade, "dd/MM/yyyy"));
				rsm.setValueToField("DTEMISSAO", Util.formatDate(dtEmissao, "dd/MM/yyyy"));
				rsm.setValueToField("HREMISSAO", Util.formatDate(dtEmissao, "HH:mm:ss"));
				rsm.setValueToField("NRCONTROLE", idDocumento);
			}
			else {
				result.setMessage("Solicitação de Avaliação não encontrada.");
				return result;
			}
			rsm.beforeFirst();
			result.setCode(1);
			result.setMessage("Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", new java.util.HashMap<String,Object>());
			
			return result;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result getDAM(String nrInscricaoImobiliaria)	{
		// GregorianCalendar init = new GregorianCalendar();
		Connection connect  = null, connectISS = null;
		Connection conLocal = Conexao.conectar();
		try	{
			
			connect   = getConnection("IPT");
			nrInscricaoImobiliaria = nrInscricaoImobiliaria!=null ? nrInscricaoImobiliaria.replaceAll("[\\.]", "") : "";
			int nrAnoBase = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("NR_ANO_BASE", 2014, 0);
			/*
			 *  Validando número da inscrição
			 */
			if(nrInscricaoImobiliaria==null || nrInscricaoImobiliaria.equals(""))
				return new Result(-1, "Informe o número da inscrição do imóvel!");
			while(nrInscricaoImobiliaria.length()<14)
				nrInscricaoImobiliaria = "0"+nrInscricaoImobiliaria;
			// Separando número de inscrição
			int i1 = Integer.parseInt(nrInscricaoImobiliaria.substring(0, 2));
			int i2 = Integer.parseInt(nrInscricaoImobiliaria.substring(2, 4));
			int i3 = Integer.parseInt(nrInscricaoImobiliaria.substring(4, 7));
			int i4 = Integer.parseInt(nrInscricaoImobiliaria.substring(7, 11));
			int i5 = Integer.parseInt(nrInscricaoImobiliaria.substring(11, 14));
			/*
			 * DADOS DO CONTRIBUINTE - Validando número da inscrição
			 */
			String nmContribuinte 		 = ""; // É o mesmo que Adquirente
			String nrCpfCnpjContribuinte = "";
			String nmTransmitente 		 = "";
			String nrCpfCnpjTransmitente = "";
			// Endereço
			String nmEnderecoAdquirente 	 	= "";
			String nmTipoLogradouro 	 		= "";
			String nmLogradouro 		 		= "";
			String nrEndereco 			 		= "";
			String nmBairro 			 		= "";
			String nmComplemento 		 		= "";
			String nrCep 				 		= "";
			// Documento
			String nrDocumento			 		= "";
			String nrInscMunicipal 				= "";
			
			// Dados do Adquirente
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM IPT.SQLITBInet " +
																		 "WHERE nrInscricaoMobiliaria = \'"+nrInscricaoImobiliaria+"\' AND tpSolicitacao = '1'").executeQuery());
			Result result = new Result(1);
			if(rsm.next())	{
				result.addObject("resultset", conLocal.prepareStatement("SELECT * FROM grl_pessoa LIMIT 1 ").executeQuery());
				nmContribuinte   			 = rsm.getString("nmAdquirente");
				nmEnderecoAdquirente   		 = rsm.getString("nmEnderecoAdquirente");
				nrCpfCnpjContribuinte        = rsm.getString("nrCpfCnpj");
				nrCpfCnpjContribuinte        = nrCpfCnpjContribuinte == null ? "" : nrCpfCnpjContribuinte;
				// Dados do Transmitente
				nmTransmitente   			 = rsm.getString("nmTransmitente");
				nrCpfCnpjTransmitente        = rsm.getString("nrCpfCnpjTransmitente");
				nrCpfCnpjTransmitente        = nrCpfCnpjTransmitente == null ? "" : nrCpfCnpjTransmitente;
				// Dados do Imovel
				nmTipoLogradouro    		 = rsm.getString("nmTipoLogradouro");
				nmLogradouro				 = rsm.getString("nmLogradouro");
				nmBairro   			 		 = rsm.getString("nmBairro");
				nmComplemento		 		 = rsm.getString("nmComplemento");
				nrCep   			 		 = rsm.getString("nrCep");
				nrEndereco   			 	 = rsm.getString("nrEndereco");
			} 
			else
				return new Result(-1, "Nº de Inscrição Inválida para o Adquirente!");
			// 
			connectISS = getConnection("ISS");
			rsm = new ResultSetMap(connectISS.prepareStatement("SELECT B.indice1 AS nrInscricaoMunicipal "+ 
												        	   "FROM SQLUser.sqlISSBEI97 A, SQLUser.sqlISSBEC00 B "+
												        	   "WHERE   A.indice3 = B.indice1 "+
												        	   "  		AND A.indice1 = \'"+nrCpfCnpjContribuinte+"\'"+
												        	   "  		AND B.indice2 = 1 ").executeQuery());
			if(rsm.next())	{
				nrInscMunicipal  = rsm.getString("nrInscricaoMunicipal");
			}
			else
				return new Result(-1, "Nº de Inscrição Municipal não encontrado!");
				
			
			// Dados do Adquirente
			rsm = new ResultSetMap(conLocal.prepareStatement("select * from ptc_documento A " +
															 "where A.txt_observacao = \'"+nrInscricaoImobiliaria+"\'" +
															 "ORDER BY A.cd_documento DESC LIMIT 1").executeQuery());
			if(rsm.next())	{
				nrDocumento					 = rsm.getString("nr_documento");
			} 
			else
				return new Result(-1, "Documento não encontrado!");
			
			// Salvando pessoa
			int cdPessoa = 0;
			int gnPessoa = (nrCpfCnpjContribuinte.length()>11  ? PessoaServices.TP_JURIDICA : PessoaServices.TP_FISICA);
			ResultSet rs = conLocal.prepareStatement("SELECT * FROM grl_pessoa_"+(gnPessoa==PessoaServices.TP_FISICA?"fisica ":"juridica ")+
					                                 "WHERE "+(gnPessoa==PessoaServices.TP_FISICA ? "nr_cpf " : "nr_cnpj ")+" = \'"+nrCpfCnpjContribuinte+"\'").executeQuery();
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
				if(nrCpfCnpjContribuinte.length()<=11)
					pstmt = conLocal.prepareStatement("INSERT INTO grl_pessoa_fisica (cd_pessoa, nr_cpf) VALUES (?, ?)");
				else
					pstmt = conLocal.prepareStatement("INSERT INTO grl_pessoa_juridica (cd_pessoa, nr_cnpj, nr_inscricao_municipal) VALUES (?, ?, ?)");
				pstmt.setInt(1, cdPessoa);
				pstmt.setString(2, nrCpfCnpjContribuinte);
				if(nrCpfCnpjContribuinte.length() > 11)
					pstmt.setString(3, nrInscricaoImobiliaria); 
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
			float prJuros 		= 0, prMulta = 0;
			float vlJuros 		= 0, vlMulta = 0, vlTaxaExpediente = 0;
			float vlTotal 		= vlTaxaExpediente;
			int nrParcela 		= 1;
			String dsMensagem 	= "", nrDAM = "28200000066892";
			String venc  		= "02022015";
			GregorianCalendar dtVencimento 		= com.tivic.manager.util.Util.convStringToCalendar(venc.substring(0,2)+"/"+venc.substring(2,4)+"/"+venc.substring(4,8)+" 23:59:59");;
			GregorianCalendar dtVencimentoDAM 	= (GregorianCalendar)dtVencimento.clone();
			dtVencimentoDAM 					= IptuServices.addDiasUteis(new GregorianCalendar(), 3, null);
			try	{
				prMulta             = Float.parseFloat(com.tivic.manager.grl.ParametroServices.getValorOfParametro("PR_MULTA_DAM_IPTU"));
				prJuros             = Float.parseFloat(com.tivic.manager.grl.ParametroServices.getValorOfParametro("PR_JUROS_DAM_IPTU"));
				vlTaxaExpediente    = Float.parseFloat(com.tivic.manager.grl.ParametroServices.getValorOfParametro("VL_TAXA_EXPEDIENTE"));
			}
			catch(Exception e) { };
			ResultSetMap rsmTributos  = new ResultSetMap();
			ResultSetMap rsmTributosR = new ResultSetMap();
			
			// Taxa de Expediente
			HashMap<String,Object> register = new HashMap<String,Object>();
			register.put("CD_TRIBUTO", Util.fillNum(rsmTributos.size()+1, 2));
			register.put("ID_TRIBUTO", "EXPE - TARIFA DE EXPEDIENTE");
			register.put("NM_TRIBUTO", "TE - TAXA DE EXPEDIENTE");
			register.put("VL_TRIBUTO", vlTaxaExpediente);
			rsmTributos.addRegister(register);
			rsmTributosR.addRegister(register);

			/*
			 *  CONTA A RECEBER - Registrando emissão
			 */
			int cdEmpresa 			= 0;
			ResultSet rsTemp 		= conLocal.prepareStatement("SELECT * FROM grl_empresa").executeQuery();
			if(rsTemp.next())
				cdEmpresa = rsTemp.getInt("cd_empresa");
			int cdTipoDocumento 	= getCdTipoDocumentoITBI(conLocal);
			int cdContaReceber 		= Conexao.getSequenceCode("adm_conta_receber", conLocal);
			PreparedStatement pstmt = conLocal.prepareStatement("INSERT INTO adm_conta_receber (cd_conta_receber,cd_pessoa,cd_empresa,"+
			                                                    "nr_documento,nr_parcela,dt_vencimento,dt_emissao,vl_abatimento,vl_acrescimo,vl_conta" +
			                                                    ",cd_tipo_documento) " +
			                                                    "VALUES (?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, cdContaReceber);
			pstmt.setInt(2, cdPessoa);
			pstmt.setInt(3, cdEmpresa);
			pstmt.setString(4, nrInscricaoImobiliaria);
			pstmt.setInt(5, 0/*nrParcela*/);
			pstmt.setTimestamp(6, new Timestamp(dtVencimentoDAM.getTimeInMillis()));
			pstmt.setTimestamp(7, new Timestamp(new GregorianCalendar().getTimeInMillis())); // dtEmissao
			pstmt.setFloat(8, vlTotal);
			pstmt.setFloat(9, 0 /*vlDesconto*/);
			pstmt.setFloat(10, (vlMulta + vlJuros));
			pstmt.setInt(11, cdTipoDocumento);
			pstmt.executeUpdate();
			
			/* Mensagem ITBI*/
			dsMensagem = "ITBI REF UM(A) "+"CASA RESIDENCIAL"/*tipo do imovel*/+" "+ nmTipoLogradouro + " " + nmLogradouro /*rua do imovel*/ +
						 "\nCONF GUIA "+nrDocumento+" E CND "+"12345/13"/*alterar para numero real de CND caso seja isso*/+
						 "\n\nTRANSMITENTE "+nmTransmitente;
			
//			result.addObject("rsmServicos", rsmServicos.getLines());
			result.addObject("rsmTributos", rsmTributos.getLines());
			result.addObject("rsmTributosR", rsmTributosR.getLines());
			//
			result.addObject("nrDAM", nrDAM);
			result.addObject("nrParcela", nrParcela);
			result.addObject("nrInscricaoMunicipal", nrInscMunicipal.subSequence(0, nrInscMunicipal.length()-1)+"-"+nrInscMunicipal.subSequence(nrInscMunicipal.length()-1, nrInscMunicipal.length()));
			result.addObject("nmContribuinte", nmContribuinte);
			result.addObject("dsEndereco", nmEnderecoAdquirente);
			result.addObject("nmTipoLogradouro", nmTipoLogradouro);
			result.addObject("nmLogradouro", nmLogradouro);
			result.addObject("nrEndereco", nrEndereco);
			result.addObject("nmComplemento", nmComplemento);
			result.addObject("nmBairro", nmBairro);
			result.addObject("nrCep", nrCep);
			result.addObject("nmCidade", "VITÓRIA DA CONQUISTA");
			result.addObject("sgEstado", "BA");
			result.addObject("nrCpfCnpj",      (gnPessoa==PessoaServices.TP_FISICA ? Util.formatCpf(nrCpfCnpjContribuinte) : Util.formatCnpj(nrCpfCnpjContribuinte)));
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
			return new Result(-1, "Erro ao tentar gerar o DAM/ITBI", e);
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
			nrCodigoBarras 	= nrCodigoBarras.subSequence(0,3)+
							 String.valueOf(com.tivic.manager.util.Util.getDvMod10(nrCodigoBarras))+
			                 nrCodigoBarras.substring(3, 43);
			String bloco1 	= nrCodigoBarras.subSequence(0, 11)+"-"+String.valueOf(com.tivic.manager.util.Util.getDvMod10((String)nrCodigoBarras.subSequence(0,11)));
			String bloco2 	= nrCodigoBarras.subSequence(11,22)+"-"+String.valueOf(com.tivic.manager.util.Util.getDvMod10((String)nrCodigoBarras.subSequence(11,22)));
			String bloco3 	= nrCodigoBarras.subSequence(22,33)+"-"+String.valueOf(com.tivic.manager.util.Util.getDvMod10((String)nrCodigoBarras.subSequence(22,33)));
			String bloco4 	= nrCodigoBarras.subSequence(33,44)+"-"+String.valueOf(com.tivic.manager.util.Util.getDvMod10((String)nrCodigoBarras.subSequence(33,44)));

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
	
	public static Result getGuiaITBI(String nrInscricaoImobiliaria) {
		Connection connect = null;
		try	{
			nrInscricaoImobiliaria = nrInscricaoImobiliaria!=null ? nrInscricaoImobiliaria.replaceAll("[\\.]", "") : "";
			connect   = getConnection("IPT");
			/*
			 *  Validando número da inscrição
			 */
			if(nrInscricaoImobiliaria==null || nrInscricaoImobiliaria.equals(""))
				return new Result(-1, "Informe o número da inscrição do imóvel!");
			while(nrInscricaoImobiliaria.length()<14)
				nrInscricaoImobiliaria = "0"+nrInscricaoImobiliaria;
			// Separando número de inscrição
			int i1 = Integer.parseInt(nrInscricaoImobiliaria.substring(0, 2));
			int i2 = Integer.parseInt(nrInscricaoImobiliaria.substring(2, 4));
			int i3 = Integer.parseInt(nrInscricaoImobiliaria.substring(4, 7));
			int i4 = Integer.parseInt(nrInscricaoImobiliaria.substring(7, 11));
			int i5 = Integer.parseInt(nrInscricaoImobiliaria.substring(11, 14));
			
			Result result = new Result(1);
			
			// Dados do Adquirente
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement( "SELECT top 1 \'"+nrInscricaoImobiliaria+"\' AS nrInscricao, "+
								"	A1.nmCampo28/100 AS vlAreaTerreno, A1.nmCampo30/100 AS vlAreaConstrucao, A1.nmCampo2 AS tpCategoria, A1.nrCpfCnpj AS nrCpfCnpjTr, "+
								"	A.*, B.nrAnoBase, B.tpCategoria, B.vlTerreno, B.vlM2Terreno, B.vlAreaTerreno, "+
								"	B.nmCampo5/100 AS vlFatorCorrTerreno, B.nmCampo6, B.nmCampo7, "+
								"	B.nmCampo13, B.nmCampo14, B.vlM2Construcao, B.vlConstrucao, B.vlFatorCorrConstrucao, B.vlVenal, " +
								"	B.vlC1C4 AS prAliquota, B.vlC2C4," +
								"	B.vlC3C4, B.vlC4C4, B.nrAnoBase, " +
								"	D.nmTipoLogradouro AS nmTipoLogradouroImovel, D.nmLogradouro AS nmLogradouroImovel, " +
								"	A.nmCampo4 AS nrEndereco, A.nmCampo5 AS nmComplementoImovel, E.nmBairro AS nmBairroImovel, A.nrImovel AS nrEnderecoImovel, " +
								"	F.nmAdquirente, F.nrCpfCnpj AS nrCpfCnpjAd, F.nmTipoLogradouro AS nmTipoLogradouroAd, F.nmLogradouro AS nmLogradouroAd, F.nrEndereco AS nrEnderecoAd, F.nmComplemento AS nmComplementoAd, F.nmBairro AS nmBairroAd " +
								" FROM IPT.SQLContribuinte A " +
								" JOIN IPT.IPTBIC0101               A1 ON (A1.indice1 = "+i1+" AND A1.indice2 = "+i2+" AND A1.indice3 = "+i3+" AND A1.indice4 = "+i4+" AND A1.indice5 = "+i5+" AND A1.indice6 = 1) " +
								" JOIN IPT.SQLViewCalculo           B ON (B.indice1 = "+i1+" AND B.indice2 = "+i2+" AND B.indice3 = "+i3+" AND B.indice4 = "+i4+" AND B.indice5 = "+i5+") " +
								" LEFT OUTER JOIN IPT.SQLLogradouro D ON (A.nmCampo2 = D.indice1) " +
								" JOIN IPT.IPTBLC01                 E ON (A.nmCampo2 = E.indice3 AND E.indice4||E.indice5 = A.nmCampo3) " +
								" LEFT OUTER JOIN IPT.SQLITBInet F ON (F.nrInscricaoMobiliaria = \'"+nrInscricaoImobiliaria+"\') " +
								" WHERE A.indice1 = "+i1+" AND A.indice2 = "+i2+" AND A.indice3 = "+i3+" AND A.indice4 = "+i4+" AND A.indice5 = "+i5).executeQuery());
			if(rsm.next())	{
				// dados do adquirente
				rsm.setValueToField("NomeAd", rsm.getString("nmAdquirente"));
				rsm.setValueToField("EstCivil", 	 null /*rsm.getString("nmEstadoCivil")*/);
				rsm.setValueToField("Nacionalidade", null /*rsm.getString("nmNacionalidade")*/);
				rsm.setValueToField("Naturalidade",  null /*rsm.getString("nmNaturalidade")*/);
				rsm.setValueToField("Profissao", 	 null /*rsm.getString("nmProfissao")*/);
				rsm.setValueToField("RG", 			 null /*rsm.getString("nrRG")*/);
				rsm.setValueToField("CPFAd", rsm.getString("nrCpfCnpjAd"));
				rsm.setValueToField("EndAd", rsm.getString("nmTipoLogradouroAd") + " " + rsm.getString("nmLogradouroAd") + " " + rsm.getString("nrEnderecoAd") + " " + rsm.getString("nmBairroAd") + (rsm.getString("nmComplementoAd") != null ? " " + rsm.getString("nmComplementoAd") : ""));
				// dados do transmitente
				rsm.setValueToField("NomeTr", rsm.getString("nmContribuinte"));
				rsm.setValueToField("EndTr", rsm.getString("nmTipoLogradouro") + " " + rsm.getString("nmLogradouro") + " " + rsm.getString("nrEndereco") + " " + rsm.getString("nmBairro") + (rsm.getString("nmComplemento") != null ? " " + rsm.getString("nmComplemento") : ""));
				rsm.setValueToField("CPFTr", rsm.getString("nrCpfCnpjTr"));
				// dados do imovel
				rsm.setValueToField("Testada", rsm.getString("vlTestada"));
				rsm.setValueToField("Fundo", rsm.getString("vlFundo"));
				rsm.setValueToField("LadoDir", rsm.getString("vlLadoDir"));
				rsm.setValueToField("LadoEsq", rsm.getString("vlLadoEsq"));
				rsm.setValueToField("AreaTerreno", rsm.getString("vlAreaTerreno"));
				rsm.setValueToField("CondLegais", rsm.getString("nrCpfCnpjTr"));
				rsm.setValueToField("CondFisicas", rsm.getString("nrCpfCnpjTr"));
				rsm.setValueToField("Benfeitorias", rsm.getString("nrCpfCnpjTr"));
				rsm.setValueToField("AreaOcupada", rsm.getString("vlAreaConstrucao"));
				rsm.setValueToField("TituloBenf", rsm.getString("nrCpfCnpjTr"));
				rsm.setValueToField("DescBenfeitorias", rsm.getString("nrCpfCnpjTr"));
				rsm.setValueToField("Especie", rsm.getString("nrCpfCnpjTr"));
				rsm.setValueToField("AreaUtil", rsm.getString("nrCpfCnpjTr"));
				rsm.setValueToField("AreaTotal", rsm.getString("nrCpfCnpjTr"));
				rsm.setValueToField("NroPavimentos", rsm.getString("nrCpfCnpjTr"));
				rsm.setValueToField("Garagens", rsm.getString("nrCpfCnpjTr"));
				rsm.setValueToField("Banheiros", rsm.getString("nrCpfCnpjTr"));
				rsm.setValueToField("Elevadores", rsm.getString("nrCpfCnpjTr"));
				rsm.setValueToField("NroDependencia", rsm.getString("nrCpfCnpjTr"));
				rsm.setValueToField("FracaoIdeal", rsm.getString("nrCpfCnpjTr"));
				rsm.setValueToField("EstConservacao", rsm.getString("nrCpfCnpjTr"));
				rsm.setValueToField("InscMunicipal", nrInscricaoImobiliaria);
				rsm.setValueToField("EndImovel", rsm.getString("nmTipoLogradouroImovel") + " " + rsm.getString("nmLogradouroImovel") + " " + rsm.getString("nrEnderecoImovel") + " " + rsm.getString("nmBairroImovel"));
				rsm.setValueToField("NroImovel", rsm.getString("nrImovel"));
				rsm.setValueToField("Denominacao", rsm.getString("nrCpfCnpjTr"));
				rsm.setValueToField("Distrito", rsm.getString("nmCidade"));
				rsm.setValueToField("TipoContrato", rsm.getString("nrCpfCnpjTr"));
				rsm.setValueToField("VlrTransacao", Util.formatNumber(Double.parseDouble(rsm.getString("vlAreaTerreno"))));
				rsm.setValueToField("EntFinanciadora", rsm.getString("nrCpfCnpjTr"));
				rsm.setValueToField("VlrFinanciado", Util.formatNumber(Double.parseDouble(rsm.getString("vlAreaTerreno"))));
				rsm.setValueToField("RespEncaminhamento", rsm.getString("nrCpfCnpjTr"));
			} 
			else
				return new Result(-1, "Nº de Inscrição Inválida!");
			result = new Result(1, "Processado com sucesso!");
			result.addObject("rsm", rsm);
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
}