package com.tivic.manager.egov;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.GregorianCalendar;
import com.intersys.jdbc.CacheDataSource;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.grl.Setor;
import com.tivic.manager.grl.SetorDAO;
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

public class AvaliacaoServices {
	@SuppressWarnings("resource")
	public static Result salvarAvaliacaoITBI(String nrInscricao, String nmZona,
			String nmNome, String nrRg, String nrCpf, String vlAreaConstrucao,
			String vlAreaTerreno, String nmEnderecoResidencia, String nmEnderecoImovel, String nmEmail) {
		Connection conLocal = Conexao.conectar();
		Connection connectIPT = null, connectISS = null;
		try {
			GregorianCalendar dataProtocolo = new GregorianCalendar();
			int gnPessoa = PessoaServices.TP_JURIDICA;
			nrInscricao = nrInscricao != null ? nrInscricao.replaceAll("[\\.]", "")
					.replaceAll("[\\-]", "").replaceAll("[\\/]", "") : "";
			nrCpf = nrCpf != null ? nrCpf.replaceAll("[\\.]", "")
					.replaceAll("[\\-]", "").replaceAll("[\\/]", "") : "";
			// Verificando CPF
			if (nrCpf == null || nrCpf.equals(""))
				return new Result(-1, "Informe o número do CPF ou CNPJ!");
			while (nrCpf.length() < 11)
				nrCpf = "0" + nrCpf;
			// Verificando se é um "CPF" Válido
			if (nrCpf.length() <= 11) {
				gnPessoa = PessoaServices.TP_FISICA;
				if (!com.tivic.manager.util.Util.isCpfValido(nrCpf))
					return new Result(-2, "Número do CPF inválido!");
			}
			// CNPJ
			else if (!com.tivic.manager.util.Util.isCNPJ(nrCpf))
				return new Result(-2, "Número do CNPJ inválido!");

			// Estabelece a conexao utilizando o namespace IPT 
			connectIPT = getConnection("IPT");
			
			
			nrInscricao = !nrInscricao.equals("null") ? nrInscricao.toUpperCase() : "";
			nmZona = !nmZona.equals("null") ? nmZona.toUpperCase() : "";
			nmNome = !nmNome.equals("null") ? nmNome.toUpperCase() : "";
			nrRg = !nrRg.equals("null") ? nrRg.toUpperCase() : "";
			nrCpf = !nrCpf.equals("null") ? nrCpf.toUpperCase() : "";
			vlAreaConstrucao = !vlAreaConstrucao.equals("null") ? vlAreaConstrucao.toUpperCase() : "";
			vlAreaTerreno = !vlAreaTerreno.equals("null") ? vlAreaTerreno.toUpperCase() : "";
			nmEnderecoResidencia = !nmEnderecoResidencia.equals("null") ? nmEnderecoResidencia.toUpperCase() : "";
			nmEnderecoImovel = !nmEnderecoImovel.equals("null") ? nmEnderecoImovel.toUpperCase() : "";
			
			Result result = new Result(1);
			
			// Verifica a posicao correta para inserir
			ResultSet rs = connectIPT.prepareStatement(
					"SELECT count(*) as total FROM IPT.SQLITBIAVALIACAO")
					.executeQuery();
			rs.next();
			String nrSequencial = "" + (rs.getInt("total") + 1);
			
			connectISS = getConnection("ISS");
			String nrInscMunicipal = "";
			ResultSetMap rsm = new ResultSetMap(connectISS.prepareStatement("SELECT B.indice1 AS nrInscricaoMunicipal "+ 
															        	   "FROM SQLUser.sqlISSBEI97 A, SQLUser.sqlISSBEC00 B "+
															        	   "WHERE A.indice3 = B.indice1 "+
															        	   "  AND A.indice1 = \'"+nrCpf+"\'"+
															        	   "  AND B.indice2 = 1 ").executeQuery());
			if(rsm.next())	{
				nrInscMunicipal  = rsm.getString("nrInscricaoMunicipal");
			}
			
			PreparedStatement pstmt;
			
			//
			int cdEmpresa = 0;
			rs = conLocal.prepareStatement("SELECT cd_empresa FROM grl_empresa ").executeQuery();
			if (rs.next())
				cdEmpresa = rs.getInt("cd_empresa");
			// Salvando pessoa
			int cdPessoa = 0;
			rs = conLocal.prepareStatement("SELECT * FROM grl_pessoa_"+(nrCpf.length()<=11?"fisica ":"juridica ")+
					"WHERE "+(gnPessoa==PessoaServices.TP_FISICA ? "nr_cpf " : "nr_cnpj ")+" = \'"+nrCpf+"\'").executeQuery();
			if(rs.next()) 
				cdPessoa = rs.getInt("cd_pessoa");
			else	{
				cdPessoa = Conexao.getSequenceCode("grl_pessoa", conLocal);
				pstmt = conLocal.prepareStatement("INSERT INTO grl_pessoa (cd_pessoa, nm_pessoa, dt_cadastro, gn_pessoa) " +
						"VALUES (?, ?, ?, ?)");
				pstmt.setInt(1, cdPessoa);
				pstmt.setString(2, nmNome);
				pstmt.setTimestamp(3, new Timestamp(new GregorianCalendar().getTimeInMillis()));
				pstmt.setInt(4, gnPessoa);
				pstmt.executeUpdate();
				// Inserindo FISICA / JURÍDICA
				if(nrCpf.length()<=11)
					pstmt = conLocal.prepareStatement("INSERT INTO grl_pessoa_fisica (cd_pessoa, nr_cpf) VALUES (?, ?)");
				else
					pstmt = conLocal.prepareStatement("INSERT INTO grl_pessoa_juridica (cd_pessoa, nr_cnpj, nr_inscricao_municipal) VALUES (?, ?, ?)");
				pstmt.setInt(1, cdPessoa);
				pstmt.setString(2, nrCpf);
				if(nrCpf.length() > 11)
					pstmt.setString(3, nrInscMunicipal); 
				pstmt.executeUpdate();
				// Salvando ENDEREÇO
//				int cdTipoLogradouro = 0;
//				pstmt  = conLocal.prepareStatement("SELECT * FROM grl_tipo_logradouro " +
//                        						   "WHERE nm_tipo_logradouro = \'"+nmTipoDeLogradouro+"\'");
//				rs = pstmt.executeQuery();
//				if(rs.next())	
//					cdTipoLogradouro = rs.getInt("cd_tipo_logradouro");
//				else
//					cdTipoLogradouro = TipoLogradouroDAO.insert(new TipoLogradouro(0, nmTipoDeLogradouro, nmTipoDeLogradouro), conLocal);
				//
				int cdTipoEndereco   = 0;
				int cdCidade         = ParametroServices.getValorOfParametroAsInteger("CD_CIDADE_END_DEFAULT", 0, cdEmpresa, conLocal);
				pstmt = conLocal.prepareStatement("INSERT INTO grl_pessoa_endereco (cd_pessoa, cd_endereco, ds_endereco, cd_cidade, lg_principal) " +
						"VALUES (?, ?, ?, ?, 1) ");
				pstmt.setInt(1, cdPessoa);
				pstmt.setInt(2, 1 /*cdEndereco*/);
				pstmt.setString(3, nmEnderecoResidencia);
				if(cdCidade > 0)
					pstmt.setInt(4, cdCidade);
				else
					pstmt.setNull(4, Types.INTEGER);
				pstmt.executeUpdate();
			}
			
			int cdTipoDocumento     = getCdTipoDocumentoITBI(conLocal);
			// Tipo do DOCUMENTO
			int cdSetor             = getCdSetorITBI(cdEmpresa, conLocal);
			int cdSituacaoArquivado = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_ARQUIVADO", 0, 0, conLocal);
			int cdFase              = ParametroServices.getValorOfParametroAsInteger("CD_FASE_INICIAL", 0, 0, conLocal);
			int cdUsuario           = 0;
			String nrDocumento      = TipoDocumentoServices.getNextNumeracao(cdTipoDocumento, cdEmpresa, true, conLocal);
			// Criando um md5 a partir do idDocumento
			MessageDigest md = MessageDigest.getInstance("MD5");  
			BigInteger hash = new BigInteger(1, md.digest((nrCpf+nrDocumento+new GregorianCalendar()).getBytes()));  
			String idDocumento      = hash.toString(16); 
//			String idDocumento      = Long.toHexString(new Long(nrCpf).longValue())+nrDocumento.substring(0, 3)+String.valueOf(new GregorianCalendar().getMaximum(Calendar.SECOND));
			while(idDocumento.length() < 16)
				idDocumento += "0";
			idDocumento = (idDocumento.substring( 0,  4)+"."+
					idDocumento.substring( 4,  8)+"."+
					idDocumento.substring( 8, 12)+"."+
					idDocumento.substring(12, 16)).toUpperCase();
			
			String txtObservacao = nrInscricao; // Inscricao imobiliária
			
			// DATA DE EMISSÃO E VALIDADE
			GregorianCalendar dtEmissao  = new GregorianCalendar();
			GregorianCalendar dtValidade = (GregorianCalendar)dtEmissao.clone();
			dtValidade.add(Calendar.DATE, 90);
			// Salvando ITBI
			Documento documento = new Documento(0, 0 /*cdArquivo*/, cdSetor, cdUsuario, "SEFIN" /*nmLocalOrigem*/, dtEmissao /*dtProtocolo*/,
					DocumentoServices.TP_PUBLICO/*tpDocumento*/, txtObservacao, idDocumento, nrDocumento,
					cdTipoDocumento, 0 /*cdServico*/, 0 /*cdAtendimento*/, "" /*txtDocumento*/, cdSetor /*Atual*/,
					cdSituacaoArquivado, cdFase, cdEmpresa, 0 /*cdProcesso*/, 0 /*tpPrioridade*/, 0/*cdDocumentoSuperior*/, null /*dsAssunto*/, 
					null /*nrAtendimento*/, 0/*lgNotificacao*/, 0 /*cdTipoDocumentoAnterior*/, null/*nrDocumentoExterno*/, null/*nrAssunto*/,null, null, 0, 1);
			int cdDocumento = DocumentoDAO.insert(documento, conLocal);
			if(cdDocumento <= 0)
				new Result(cdDocumento, "Falha ao salvar ITBI!");
			DocumentoPessoaDAO.insert(new DocumentoPessoa(cdDocumento,cdPessoa,"Solicitante"), conLocal);
			
			// objetos retonados para preenchimento do protocolo
			result.setMessage("ITBI cadastrado com sucesso!");
			result.addObject("nrDocumento", nrDocumento);
			result.addObject("idDocumento", idDocumento);
			result.addObject("dataProtocolo",  Util.formatDate(dataProtocolo, "dd/MM/yyyy"));
			
			// Insere na tabela SQLITBINET um novo ITBI
			pstmt = connectIPT.prepareStatement("INSERT INTO IPT.sqlITBIAvaliacao " +
							"(nrInscricaoMobiliaria, tpSolicitacao, nrSequencial, nmZona, nmRequerente, nrRG, nrCPFCNPJ, vlAreaConstrucao, " +
							" vlAreaTerreno, nmEnderecoResidencial, nmEnderecoImovel, dsEmail, nrProtocolo) " +
							" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
			pstmt.setString(1, nrInscricao);
			pstmt.setString(2, "0" /*tipo de solicitacao 0 = solicitacao de avaliacao*/);
			pstmt.setString(3, nrSequencial);
			pstmt.setString(4, nmZona);
			pstmt.setString(5, nmNome);
			pstmt.setString(6, nrRg);
			pstmt.setString(7, nrCpf);
			pstmt.setString(8, vlAreaConstrucao);
			pstmt.setString(9, vlAreaTerreno);
			pstmt.setString(10, nmEnderecoResidencia);
			pstmt.setString(11, nmEnderecoImovel);
			pstmt.setString(12, nmEmail);
			pstmt.setString(13, nrDocumento);
			pstmt.executeUpdate();
			result.addObject("inscricao", nrInscricao);
			result.addObject("nome", nmNome);
			result.addObject("rg", nrRg);
			result.addObject("cpf", nrCpf);
			result.addObject("areaConstrucao", vlAreaConstrucao);
			result.addObject("areaTerreno", vlAreaTerreno);
			result.addObject("enderecoResidencia", nmEnderecoResidencia);
			result.addObject("enderecoImovel", nmEnderecoImovel);

			return result;
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1000, "Falha desconhecida ao tentar gerar avaliacao de ITBI!",
					e);
		} finally {
			Conexao.desconectar(conLocal);
			try {
				connectIPT.close();
			} catch (Exception e) {
			}
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
	
	private static int getCdTipoDocumentoITBI(Connection connect) throws Exception	{
		String nmTipoDocumento = "Solicitacao de Avaliacao de Imovel";
		PreparedStatement pstmt  = connect.prepareStatement("SELECT * FROM ptc_tipo_documento " +
				   						   					"WHERE nm_tipo_documento = \'"+nmTipoDocumento+"\'");
		ResultSet rs = pstmt.executeQuery();
		if(rs.next())	
			return rs.getInt("cd_tipo_documento");
		else
			return TipoDocumentoDAO.insert(new TipoDocumento(0, nmTipoDocumento, null, com.tivic.manager.ptc.TipoDocumentoServices.TP_INC_COM_ANO,
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
	
	@SuppressWarnings("resource")
	public static Result verificaInscricao(String nrInscricao)	{
		Connection connect = Conexao.conectar();
		try	{
			nrInscricao = nrInscricao!=null ? nrInscricao.replaceAll("[\\.]", "") : "";
			connect   = getConnection("IPT");
			Result resultado = new Result(-1, "O número da inscrição informada é inválido!");
			/*
			 *  Validando número da inscrição
			 */
			if(nrInscricao==null || nrInscricao.equals(""))
				return new Result(-2, "Informe o número da inscrição do imóvel!");
			while(nrInscricao.length()<14)
				nrInscricao = "0"+nrInscricao;
			// Separando número de inscrição
			int i1 = Integer.parseInt(nrInscricao.substring(0, 2));
			int i2 = Integer.parseInt(nrInscricao.substring(2, 4));
			int i3 = Integer.parseInt(nrInscricao.substring(4, 7));
			int i4 = Integer.parseInt(nrInscricao.substring(7, 11));
			int i5 = Integer.parseInt(nrInscricao.substring(11, 14));
			
			// Dados do contribuinte
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(
										" SELECT * " +
									    " FROM IPT.SQLContribuinte A " +
									    " WHERE A.indice1 = "+i1+" AND A.indice2 = "+i2+" AND A.indice3 = "+i3+
					                    " 	   AND A.indice4 = "+i4+" AND A.indice5 = "+i5).executeQuery());
			if (!rsm.next()) {
				return resultado;
			}
			resultado.setCode(1);
			resultado.setMessage("Processado com sucesso!");
			resultado.addObject("total", rsm.getInt("total"));
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
}
