package com.tivic.manager.importacao;

import java.sql.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.tivic.manager.acd.OcorrenciaQuadroVagas;
import com.tivic.manager.acd.OcorrenciaQuadroVagasServices;
import com.tivic.manager.acd.QuadroVagas;
import com.tivic.manager.acd.QuadroVagasDAO;
import com.tivic.manager.acd.QuadroVagasOcorrencia;
import com.tivic.manager.acd.QuadroVagasOcorrenciaServices;
import com.tivic.manager.acd.QuadroVagasServices;
import com.tivic.manager.adm.*;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.*;
import com.tivic.manager.ord.Modalidade;
import com.tivic.manager.ord.ModalidadeServices;
import com.tivic.manager.ord.SituacaoServico;
import com.tivic.manager.ord.SituacaoServicoServices;
import com.tivic.manager.ord.TipoAtendimento;
import com.tivic.manager.ord.TipoAtendimentoServices;
import com.tivic.manager.ord.TipoProdutoServico;
import com.tivic.manager.ord.TipoProdutoServicoServices;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.seg.UsuarioModulo;
import com.tivic.manager.seg.UsuarioModuloEmpresa;
import com.tivic.manager.seg.UsuarioModuloEmpresaServices;
import com.tivic.manager.seg.UsuarioModuloServices;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;


public class EDFImport {

	public static sol.util.Result importUnidades()	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = conectar();
		try {
			/**
			 * IMPORTANDO UNIDADES ESCOLARES
			 **/
			PreparedStatement pesqRegiao = connect.prepareStatement("SELECT * FROM grl_regiao WHERE cd_regiao = ?");
			PreparedStatement pesqEstado = connect.prepareStatement("SELECT * FROM grl_estado WHERE sg_estado = ?");
			PreparedStatement pesqCidade = connect.prepareStatement("SELECT * FROM grl_cidade WHERE nm_cidade = ?");
			PreparedStatement pesqDistri = connect.prepareStatement("SELECT * FROM grl_distrito WHERE cd_cidade = ? AND nm_distrito = \'DISTRITO SEDE\'");
			PreparedStatement pesqBairro = connect.prepareStatement("SELECT * FROM grl_bairro WHERE cd_cidade = ? AND nm_bairro = ?");
			PreparedStatement pesqLograd = connect.prepareStatement("SELECT * FROM grl_logradouro WHERE id_logradouro = ?");
			
			System.out.println("Importando Unidades Escolares...");
			
			ResultSet rsCidade = conOrigem.prepareStatement("SELECT * FROM cidade").executeQuery();
			while(rsCidade.next())	{
				int cdCidade = 0;
				pesqCidade.setString(1, rsCidade.getString("nm_cidade"));
				ResultSet rsTemp = pesqCidade.executeQuery();
				if(!rsTemp.next())	{
					int cdEstado = 0;
					pesqEstado.setString(1, rsCidade.getString("sg_estado"));
					ResultSet rsEstado = pesqEstado.executeQuery();
					if(rsEstado.next())
						cdEstado = rsEstado.getInt("cd_estado");
					Cidade cidade = new Cidade(0,rsCidade.getString("nm_cidade"),null /*nrCep*/,0 /*vlAltitude*/,0 /*vlLatitude*/,
											   0 /*vlLongitude*/,cdEstado,rsCidade.getString("cd_cidade"), 0 /*cdRegiao*/,null /*idIbge*/, null, 0, 0);
					cdCidade = CidadeDAO.insert(cidade, connect);
				}
				else
					cdCidade = rsTemp.getInt("cd_cidade");
				// Distrito
				if(cdCidade > 0)	{
					pesqDistri.setInt(1, cdCidade);
					if(!pesqDistri.executeQuery().next())	{
						Distrito distrito = new Distrito(0,cdCidade,"DISTRITO SEDE",null /*nrCep*/);
						DistritoDAO.insert(distrito, connect);
					}
				}
			}
			/***********************
			 * Importando REGIÕES
			 ***********************/
			//
			System.out.println("Importando regiões...");
			ResultSet rsRegiao = conOrigem.prepareStatement("SELECT * FROM regiao").executeQuery();
			while(rsRegiao.next())	{
				pesqRegiao.setInt(1, rsRegiao.getInt("cd_regiao"));
				if(!pesqRegiao.executeQuery().next())	{
					Regiao regiao = new Regiao(rsRegiao.getInt("cd_regiao"),rsRegiao.getString("ds_regiao"),RegiaoServices._LOGRADOUROS);
					RegiaoDAO.insert(regiao, connect);
				}
			}
			/***********************
			 * Importando BAIRROS
			 ***********************/
			//
			System.out.println("Importando bairros...");
			ResultSet rsBairro = conOrigem.prepareStatement("SELECT A.*, B.nm_cidade FROM bairro A " +
					                                        "JOIN cidade B ON (A.cd_cidade = B.cd_cidade)").executeQuery();
			while(rsBairro.next())	{
				// Cidade
				int cdCidade = 0;
				pesqCidade.setString(1, rsBairro.getString("nm_cidade"));
				ResultSet rsTemp = pesqCidade.executeQuery();
				if(rsTemp.next())
					cdCidade = rsTemp.getInt("cd_cidade");
				//
				pesqBairro.setInt(1, cdCidade);
				pesqBairro.setString(2, rsBairro.getString("cd_bairro"));
				if(!pesqBairro.executeQuery().next())	{
					// Distrito
					int cdDistrito = 0;
					pesqDistri.setInt(1, cdCidade);
					rsTemp = pesqDistri.executeQuery();
					if(rsTemp.next())
						cdDistrito = rsTemp.getInt("cd_distrito");
					//
					Bairro bairro = new Bairro(0,cdDistrito,cdCidade,rsBairro.getString("nm_bairro"),rsBairro.getString("cd_bairro"),0 /*cdRegiao*/);
					BairroDAO.insert(bairro, connect);
				}
			}
			/***********************
			 * Importando LOGRADOUROS
			 ***********************/
			//
			System.out.println("Importando logradouros...");
			ResultSet rsLograd = conOrigem.prepareStatement("SELECT A.*, B.nm_bairro, C.nm_cidade FROM rua A " +
					                                        "JOIN bairro B ON (A.cd_bairro = B.cd_bairro) " +
					                                        "JOIN cidade C ON (B.cd_cidade = C.cd_cidade)").executeQuery();
			while(rsLograd.next())	{
				pesqLograd.setString(1, rsLograd.getString("nm_rua"));
				if(!pesqLograd.executeQuery().next())	{
					// Região
					int cdRegiao = 0;
					pesqRegiao.setInt(1, rsLograd.getInt("cd_regiao"));
					ResultSet rsTemp = pesqRegiao.executeQuery();
					if(rsTemp.next())
						cdRegiao = rsTemp.getInt("cd_regiao");
					// Cidade
					int cdCidade = 0;
					pesqCidade.setString(1, rsLograd.getString("nm_cidade"));
					rsTemp = pesqCidade.executeQuery();
					if(rsTemp.next())
						cdCidade = rsTemp.getInt("cd_cidade");
					// Distrito
					int cdDistrito = 0;
					pesqDistri.setInt(1, cdCidade);
					rsTemp = pesqDistri.executeQuery();
					if(rsTemp.next())
						cdDistrito = rsTemp.getInt("cd_distrito");
					// Bairro
					int cdBairro = 0;
					pesqBairro.setInt(1, cdCidade);
					pesqBairro.setString(2, rsLograd.getString("nm_bairro"));
					rsTemp = pesqBairro.executeQuery();
					if(rsTemp.next())
						cdBairro = rsTemp.getInt("cd_bairro");
					// Tipo de Logradouro
					int cdTipoLogradouro = 0;
					//
					Logradouro lograd = new Logradouro(0,cdDistrito,cdCidade,cdTipoLogradouro,
														rsLograd.getString("nm_rua"), rsLograd.getString("cd_rua"));
					int cdLogradouro = LogradouroDAO.insert(lograd, connect);
					connect.prepareStatement("UPDATE grl_logradouro SET cd_regiao = "+cdRegiao+" WHERE cd_logradouro = "+cdLogradouro).executeUpdate();
					LogradouroBairro logradBairro = new LogradouroBairro(cdBairro,cdLogradouro);
					if(LogradouroBairroDAO.insert(logradBairro, connect) <= 0)
						throw new Exception("Erro ao tentar incluir bairro e logradouro!");
				}
			}
			System.out.println("Importacao de ruas, bairros, cidade e distritos concluida!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar tabelas de endereçamento!", e);
		}
		finally{
			Conexao.desconectar(connect);
			Conexao.desconectar(conOrigem);
		}
	}

	public static sol.util.Result importContaPortador()	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = conectar();
		try {
			/***********************
			 * Importando Contas e Carteiras (Portadores)
			 ***********************/
			PreparedStatement pesqConta   = connect.prepareStatement("SELECT * FROM adm_conta_financeira WHERE id_conta = ?");
			PreparedStatement pesqAgencia = connect.prepareStatement("SELECT * FROM grl_agencia WHERE nr_agencia = ?");
			PreparedStatement pesqBanco   = connect.prepareStatement("SELECT * FROM grl_banco WHERE nr_banco = ?");
			System.out.println("Importando contas...");
			//
			ResultSet rs = conOrigem.prepareStatement("SELECT * FROM conta_caixa_corrente").executeQuery();
			while(rs.next())	{
				pesqConta.setString(1, rs.getString("cd_conta"));
				ResultSet rsTemp = pesqConta.executeQuery();
				if(!rsTemp.next())	{
					int cdEmpresa  = getCdEmpresa(rs.getInt("cd_empresa"));
					// Agencia
					int cdAgencia  = 0;
					pesqAgencia.setString(1, rs.getString("nr_agencia"));
					ResultSet rsAg = pesqAgencia.executeQuery();
					if(!rsAg.next())	{
						String nrBanco = rs.getString("cd_portador");
						String nmAgencia = nrBanco==null ? "" : (nrBanco.equals("104") ? "CAIXA ECONOMICA - " : (nrBanco.equals("004") ? "BANCO DO NORDESTE - " : "BANCO AGENCIA "));
						pesqBanco.setString(1, nrBanco);
						ResultSet rsBanco = pesqBanco.executeQuery();
						int cdBanco = 0;
						if(rsBanco.next())
							cdBanco = rsBanco.getInt("cd_banco");
						Agencia agencia = new Agencia(0,0,0,nmAgencia+rs.getString("nr_agencia"),rs.getString("nr_telefone"),
													  null,null,null,null,new GregorianCalendar(),PessoaServices.TP_JURIDICA,null,
													  1,null,null,null,0,rs.getString("nr_agencia"),0,0,null,null,null,null,null,
													  0,null,0,0,null,cdBanco,rs.getString("nm_gerente"),rs.getString("nr_agencia"));
						cdAgencia = AgenciaDAO.insert(agencia, connect);
					}
					else
						cdAgencia = rsAg.getInt("cd_agencia");
					//
					int cdResponsavel = 0;
					String nrConta = rs.getString("nr_conta");
					String nrDv = nrConta.indexOf("-")>=0 ? nrConta.substring(nrConta.indexOf("-")+1) : null;
					nrConta = nrConta.indexOf("-")>=0 ? nrConta.substring(0, nrConta.indexOf("-")) : nrConta;
					ContaFinanceira contaFinanceira = new ContaFinanceira(0,cdResponsavel,cdEmpresa,cdAgencia,rs.getString("ds_conta"),
																	  rs.getInt("gn_conta"),nrConta,nrDv, rs.getString("nr_operacao_conta")!=null ? rs.getInt("nr_operacao_conta") : 0,
																	  rs.getFloat("vl_limite"), null /*dtFechamento*/, rs.getString("cd_conta"),
																	  null /*dtVencimentoLimite*/, 0 /*vlSaldoInicial*/, null /*dtSaldoInicial*/,
																	  0 /*cdTurno*/);
					ContaFinanceiraDAO.insert(contaFinanceira, connect);
				}
			}
			System.out.println("Importacao de contas!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar tabelas de endereçamento!", e);
		}
		finally{
			Conexao.desconectar(connect);
			Conexao.desconectar(conOrigem);
		}
	}

	public static int getCdEmpresa(int cdOldEmpresa)	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = conectar();
		try {
			PreparedStatement pesqEmpresa = connect.prepareStatement("SELECT * FROM grl_empresa WHERE id_empresa = ?");
			pesqEmpresa.setString(1, String.valueOf(cdOldEmpresa));
			ResultSet rs = pesqEmpresa.executeQuery();
			if(rs.next())
				return rs.getInt("cd_empresa");
			return -1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
			Conexao.desconectar(conOrigem);
		}
	}
	
	public static Connection conectar() {
		return conectar("org.firebirdsql.jdbc.FBDriver", "jdbc:firebirdsql://127.0.0.1:3050//xCORP/Dados/gecap.gdb",
				"sysdba","masterkey");
	}

	public static Connection conectar(String driver, String database, String user, String pass)	{
		try{
			Class.forName(driver).newInstance();
	  		DriverManager.setLoginTimeout(80);
			return DriverManager.getConnection(database, user, pass);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public static Result importUsuarios() {
		
		Connection conOrigem  = Conexao.conectar();
		Connection conDestino = conectar("org.postgresql.Driver", "jdbc:postgresql://127.0.0.1/edf_dinamite_original",
				"postgres", "s31l4c4r4");
		
		Result r = null;
		try {
			conDestino.setAutoCommit(false);

			/*
			 * ORIGEM
			 */
			PreparedStatement pstmtOrigem = conOrigem.prepareStatement(
					"SELECT A.*, "
					+ " B.nm_pessoa, B.gn_pessoa, B.nr_telefone1, B.nr_telefone2, B.nr_celular, B.nm_email, "
					+ " C.nr_rg, C.nr_cpf, C.tp_sexo, C.dt_nascimento "
					+ " FROM seg_usuario A"
					+ " JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa)"
					+ " LEFT OUTER JOIN grl_pessoa_fisica C ON (A.cd_pessoa = C.cd_pessoa)");
			ResultSetMap rsmOrigem = new ResultSetMap(pstmtOrigem.executeQuery());
			
			/*
			 * DESTINO
			 */
			while(rsmOrigem.next()) {
				//USUARIO
				String nmLogin = rsmOrigem.getString("nm_login");
				String nmSenha = rsmOrigem.getString("nm_senha");
				int tpUsuario = rsmOrigem.getInt("tp_usuario");
				int stUsuario = rsmOrigem.getInt("st_usuario");
				
				//PESSOA
				int cdPessoaOrigem = rsmOrigem.getInt("cd_pessoa");
				int cdPessoa = 0;
				String nmPessoa = rsmOrigem.getString("nm_pessoa");
				int gnPessoa = rsmOrigem.getInt("gn_pessoa");
				String nrTelefone1 = rsmOrigem.getString("nr_telefone1");
				String nrTelefone2 = rsmOrigem.getString("nr_telefone2");
				String nrCelular = rsmOrigem.getString("nr_celular");
				String nmEmail = rsmOrigem.getString("nm_email");
				
				//PESSOA FISICA
				String nrRg = rsmOrigem.getString("nr_rg");
				String nrCpf = rsmOrigem.getString("nr_cpf");
				int tpSexo = rsmOrigem.getInt("tp_sexo");
				GregorianCalendar dtNascimento = Util.convTimestampToCalendar(rsmOrigem.getTimestamp("dt_nascimento"));
				
				int cdUsuarioOrigem = rsmOrigem.getInt("cd_usuario");
				
				/*
				 * PESSOA
				 */
				
				System.out.println(nmLogin);
				System.out.println("cdPessoaOrigem: "+cdPessoaOrigem);
				
				
				PessoaFisica pessoa = new PessoaFisica(cdPessoa, 
						0,//cdPessoaSuperior, 
						0,//cdPais, 
						nmPessoa, 
						nrTelefone1, 
						nrTelefone2, 
						nrCelular, 
						null,//nrFax, 
						nmEmail, 
						new GregorianCalendar(), 
						gnPessoa, 
						null,//imgFoto, 
						PessoaServices.ST_ATIVO,//stCadastro, 
						null,//nmUrl, 
						null,//nmApelido, 
						null,//txtObservacao, 
						0,//lgNotificacao, 
						null,//idPessoa, 
						0,//cdClassificacao, 
						0,//cdFormaDivulgacao, 
						null,//dtChegadaPais, 
						0,//cdNaturalidade, 
						0,//cdEscolaridade, 
						dtNascimento, 
						nrCpf, 
						null,//sgOrgaoRg, 
						null,//nmMae, 
						null,//nmPai, 
						tpSexo, 
						0,//stEstadoCivil, 
						nrRg, 
						null,//nrCnh, 
						null,//dtValidadeCnh, 
						null,//dtPrimeiraHabilitacao, 
						0,//tpCategoriaHabilitacao, 
						0,//tpRaca, 
						0,//lgDeficienteFisico, 
						null,//nmFormaTratamento, 
						0,//cdEstadoRg, 
						null,//dtEmissaoRg, 
						null,//blbFingerprint, 
						0,//cdConjuge, 
						0,//qtMembrosFamilia, 
						0,//vlRendaFamiliarPerCapta, 
						0,//tpNacionalidade, 
						0);//tpFiliacaoPai);
				r = PessoaFisicaServices.save(pessoa, conDestino);
				
				cdPessoa = r.getCode();
				System.out.println("cdPessoa: "+cdPessoa);
				
				if(r.getCode()<=0) {
					conDestino.rollback();
					System.out.println(r.getMessage());
					return r;
				}
				
				PessoaEndereco endereco = PessoaEnderecoServices.getEnderecoPrincipal(cdPessoaOrigem, conOrigem);

				if(endereco!=null) {
					endereco.setCdEndereco(0);
					endereco.setCdPessoa(cdPessoa);
					r = PessoaEnderecoServices.save(endereco, conDestino);
					if(r.getCode()<=0) {
						conDestino.rollback();
						System.out.println(r.getMessage());
						return r;
					}
				}
				
				
				
				/*
				 * VINCULOS
				 */
				ResultSetMap rsmPessoaEmpresa = PessoaEmpresaServices.getAllByPessoa(cdPessoaOrigem, conOrigem);
				while(rsmPessoaEmpresa.next()) {
					r = PessoaEmpresaServices.save(new PessoaEmpresa(rsmPessoaEmpresa.getInt("cd_empresa"), cdPessoa, rsmPessoaEmpresa.getInt("cd_vinculo"), 
							Util.convTimestampToCalendar(rsmPessoaEmpresa.getTimestamp("dt_vinculo")), rsmPessoaEmpresa.getInt("st_vinculo")), 
							conDestino);
					if(r.getCode()<=0) {
						conDestino.rollback();
						System.out.println(r.getMessage());
						return r;
					}
				}
				
				/*
				 * USUARIO
				 */
				int cdUsuario = 0;
				
				PreparedStatement pstmt = conDestino.prepareStatement("SELECT * FROM seg_usuario " +
						   "WHERE nm_login	 = ? ");
				pstmt.setString(1, nmLogin);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next())
					cdUsuario = rs.getInt("CD_USUARIO");
				
				Usuario u = UsuarioServices.getByPessoa(cdPessoa, conDestino);
				if(u==null) {
					Usuario user = new Usuario(cdUsuario, 
							cdPessoa, 
							0,//cdPerguntaSecreta, 
							nmLogin, 
							nmSenha, 
							tpUsuario, 
							null,//nmRespostaSecreta, 
							stUsuario);
					r = UsuarioServices.save(user, conDestino);
					cdUsuario = r.getCode();
					if(r.getCode()<=0) {
						conDestino.rollback();
						System.out.println(r.getMessage());
						return r;
					}
				}
				else {
					cdUsuario = u.getCdUsuario();
				}
				
				/*
				 * USUARIO_MODULO
				 */
				System.out.println("cdUsuario: "+cdUsuario);
				ResultSetMap rsmUsuarioModulo = UsuarioModuloServices.getAllByUsuario(cdUsuarioOrigem, conOrigem);
				System.out.println(rsmUsuarioModulo);
				while(rsmUsuarioModulo.next()) {
					r = UsuarioModuloServices.save(new UsuarioModulo(cdUsuario, rsmUsuarioModulo.getInt("cd_modulo"), rsmUsuarioModulo.getInt("cd_sistema"), rsmUsuarioModulo.getInt("lg_natureza")), 
							conDestino);
					if(r.getCode()<=0) {
						conDestino.rollback();
						System.out.println(r.getMessage());
						return r;
					}
				}
				
				/*
				 * USUARIO_MODULO_EMPRESA
				 */
				ResultSetMap rsmUME = UsuarioModuloEmpresaServices.getAllByUsuario(cdUsuarioOrigem, conOrigem);
				System.out.println(rsmUME);
				while(rsmUME.next()) {
					r = UsuarioModuloEmpresaServices.save(new UsuarioModuloEmpresa(cdUsuario, rsmUME.getInt("cd_empresa"), rsmUME.getInt("cd_modulo"), rsmUME.getInt("cd_sistema")), 
							conDestino);
					if(r.getCode()<=0) {
						conDestino.rollback();
						System.out.println(r.getMessage());
						return r;
					}
				}
				
				
				/**
				 * SOLICITACOES QUADRO VAGAS
				 */
				ResultSetMap rsmOcorrencias = QuadroVagasOcorrenciaServices.getAllOcorrenciasByUsuario(cdUsuarioOrigem, conOrigem);
				while(rsmOcorrencias.next()) {
					System.out.println(rsmOcorrencias);
					
					//verificar se o quadro de vagas existe
					QuadroVagas quadro = QuadroVagasDAO.get(rsmOcorrencias.getInt("cd_quadro_vagas"), rsmOcorrencias.getInt("cd_instituicao"), conDestino);
					if(quadro==null) {
						quadro = new QuadroVagas(rsmOcorrencias.getInt("cd_quadro_vagas"), 
								rsmOcorrencias.getInt("cd_instituicao"), 
								rsmOcorrencias.getInt("cd_periodo_letivo"), 
								rsmOcorrencias.getGregorianCalendar("dt_caastro"), 
								rsmOcorrencias.getInt("st_quadro_vagas"), 
								rsmOcorrencias.getString("txt_observacao"));
						 ;
						
						if(QuadroVagasDAO.insert(quadro, conDestino)<=0) {
							conDestino.rollback();
							System.out.println(-1);
							return new Result(-1, "Erro ao inserir Quadro de Vagas na importacao");
						}
					}
					
					System.out.println("teste "+QuadroVagasDAO.get(rsmOcorrencias.getInt("cd_quadro_vagas"), rsmOcorrencias.getInt("cd_instituicao"), conDestino));
							
					OcorrenciaQuadroVagas ocorrencia = new OcorrenciaQuadroVagas(0, 
							cdPessoa, //cdPessoa
							rsmOcorrencias.getString("txt_ocorrencia"), //txtOcorrencia 
							rsmOcorrencias.getGregorianCalendar("dt_ocorrencia"), //dtOcorrencia
							rsmOcorrencias.getInt("cd_tipo_ocorrencia"), //cdTipoOcorrencia
							rsmOcorrencias.getInt("st_ocorrencia"), //stOcorrencia
							rsmOcorrencias.getInt("cd_sistema"), //cdSistema
							cdUsuario, //cdUsuario
							rsmOcorrencias.getString("txt_resposta"), //txtResposta
							rsmOcorrencias.getGregorianCalendar("dt_resposta"), //dtResposta
							rsmOcorrencias.getInt("cd_pessoa_resposta"),
							rsmOcorrencias.getString("nm_assunto")); //cdPessoaResposta
					
					r = OcorrenciaQuadroVagasServices.save(ocorrencia, rsmOcorrencias.getInt("cd_quadro_vagas"), rsmOcorrencias.getInt("cd_instituicao"), "", 0, false, conDestino);
					if(r.getCode()<=0) {
						conDestino.rollback();
						System.out.println(r.getMessage());
						return r;
					}
				}
				
			}
			
			if(r.getCode()>0) {
				System.out.println("commit.");
				conDestino.commit();
			}
			System.out.println("FIM.");
			return new Result(1, "Usuários importados com sucesso.");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			Conexao.desconectar(conOrigem);
			Conexao.desconectar(conDestino);
		}
		
	}
	
	public static Result importVinculos() {
		Connection conOrigem  = Conexao.conectar();
		Connection conDestino = conectar("org.postgresql.Driver", "jdbc:postgresql://127.0.0.1/edf_dinamite_original",
				"postgres", "t1v1k!");
				
		try {
			conDestino.setAutoCommit(false);
			Result r = null;
			
			ResultSetMap rsmUserOrigem = new ResultSetMap(conOrigem.prepareStatement(
					"SELECT cd_pessoa, nm_login FROM seg_usuario "
					+ " WHERE cd_pessoa IS NOT NULL").executeQuery());
			while(rsmUserOrigem.next()) {
				// dados do banco de origem
				int cdPessoaOrigem = rsmUserOrigem.getInt("cd_pessoa");
				String nmLoginOrigem = rsmUserOrigem.getString("nm_login");
				ResultSetMap rsmPessoaEmpresaOrigem = PessoaEmpresaServices.getAllByPessoa(cdPessoaOrigem, conOrigem);
								
				// pessoa equivalente no destino
				PreparedStatement pstmt = conDestino.prepareStatement("SELECT cd_pessoa FROM seg_usuario WHERE nm_login LIKE '"+nmLoginOrigem+"'");
				ResultSetMap rsmPessoaDestino = new ResultSetMap(pstmt.executeQuery());
				int cdPessoaDestino = 0;
				if(rsmPessoaDestino.next()) {
					cdPessoaDestino = rsmPessoaDestino.getInt("cd_pessoa");
				}
				
				if(cdPessoaDestino<=0) {
					conDestino.rollback();
					return new Result(-2, "Pessoa não encontrada no banco de destino.");
				}
				
				// importação dos vínculos
				while(rsmPessoaEmpresaOrigem.next()) {
					int cdEmpresaDestino = rsmPessoaEmpresaOrigem.getInt("cd_empresa");
					int cdVinculoDestino = rsmPessoaEmpresaOrigem.getInt("cd_vinculo");
					GregorianCalendar dtVinculoDestino = Util.convTimestampToCalendar(rsmPessoaEmpresaOrigem.getTimestamp("dt_vinculo"));
					int stVinculoDestino = rsmPessoaEmpresaOrigem.getInt("st_vinculo");

					r = PessoaEmpresaServices.save(new PessoaEmpresa(cdEmpresaDestino, cdPessoaDestino, cdVinculoDestino, dtVinculoDestino, stVinculoDestino), 
							conDestino);
					if(r.getCode()<=0) {
						conDestino.rollback();
						return new Result(-3, r.getMessage());
					}
				}
			}
			
			if(r.getCode()>0) {
				conDestino.commit();
			}
			
			return new Result(1, "Vínculos importados com sucesso.");
		}
		catch(Exception e) {
			System.out.println("Erro ao importar vínculos.");
			return null;
		}
		finally {
			Conexao.desconectar(conOrigem);
			Conexao.desconectar(conDestino);
		}
	}
	
	public static Result fixDuplicidadePessoa() {
		Connection conOrigem  = Conexao.conectar();
//		Connection conDestino = conectar("org.postgresql.Driver", "jdbc:postgresql://127.0.0.1/edf_dinamite_original",
//				"postgres", "t1v1k!");
				
		try {
			int result = -1;
			conOrigem.setAutoCommit(false);
			
			PreparedStatement pstmt = conOrigem.prepareStatement("select A.cd_pessoa as cd_pessoa_atual, A.cd_usuario, B.nm_pessoa from seg_usuario A"
					+ " join grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa)");
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int cdPessoaAtual = rs.getInt("cd_pessoa_atual");
				int cdUsuario = rs.getInt("cd_usuario");
				String nmPessoa = rs.getString("nm_pessoa");
				
				pstmt = conOrigem.prepareStatement("select cd_pessoa from grl_pessoa "
						+ " where cd_pessoa<>"+cdPessoaAtual
						+ " and nm_pessoa = '"+nmPessoa+"'");
				ResultSet rsAux = pstmt.executeQuery();
				int cdPessoa = 0;
				if(rsAux.next()) {
					cdPessoa = rsAux.getInt("cd_pessoa");
					
					Usuario user = UsuarioDAO.get(cdUsuario, conOrigem);
					user.setCdPessoa(cdPessoa);
					result = UsuarioDAO.update(user, conOrigem);
					
					if(result<=0) {
						conOrigem.rollback();
						return new Result(result, "Erro ao alterar usuário.");
					}
					
					Pessoa p = PessoaDAO.get(cdPessoaAtual, conOrigem);
					p.setStCadastro(PessoaServices.ST_INATIVO);
					result = PessoaDAO.update(p, conOrigem);
					
					//result = PessoaServices.remove(cdPessoaAtual, PessoaServices.TP_FISICA, true, conOrigem).getCode();
					
					if(result<=0) {
						conOrigem.rollback();
						return new Result(result, "Erro ao inativar pessoa. cd_pessoa="+cdPessoaAtual);
					}
				}
				
			}
			
			if(result>0) {
				conOrigem.commit();
			}
			
			return new Result(1, "Vínculos importados com sucesso.");
		}
		catch(Exception e) {
			System.out.println("Erro ao importar vínculos.");
			return null;
		}
		finally {
			Conexao.desconectar(conOrigem);
		}
	}
	
	public static sol.util.Result importTiposOs()	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = conectar("org.postgresql.Driver", "jdbc:postgresql://127.0.0.1/centaurus",
				"postgres", "t1v1k!");
		try {
			Result result = null;
			connect.setAutoCommit(false);
			
			/* **************
			 * ORD_MODALIDADE
			 * **************/
			ResultSetMap rsmTipoAtendimentoCentaurus = new ResultSetMap(
					conOrigem.prepareStatement(
							" SELECT id_tipo_atendimento, nm_tipo_atendimento FROM tipo_atendimento "
							).executeQuery()
					);
			while(rsmTipoAtendimentoCentaurus.next()) {
				Modalidade modalidade = new Modalidade();
				modalidade.setNmModalidade(rsmTipoAtendimentoCentaurus.getString("nm_tipo_atendimento").toUpperCase());
				
				result = ModalidadeServices.save(modalidade, connect);
				if(result.getCode()<=0) {
					Conexao.rollback(connect);
					return new Result(-2, "Erro ao importar tipo_atendimento >> ord_modalidade");
				}
			}
			
			/* ********************
			 * ORD_SITUACAO_SERVICO
			 * ********************/
			ResultSetMap rsmSituacaoServicoCentaurus = new ResultSetMap(
					conOrigem.prepareStatement(
							" SELECT * FROM situacao_servico "
							).executeQuery()
					);
			while(rsmSituacaoServicoCentaurus.next()) {
				SituacaoServico situacaoServico = new SituacaoServico(0, 
						rsmSituacaoServicoCentaurus.getString("nm_situacao_servico").toUpperCase(), 
						rsmSituacaoServicoCentaurus.getString("ds_situacao_servico").toUpperCase(), 
						rsmSituacaoServicoCentaurus.getInt("ordem"), 
						0, 0, 0, 0);
				
				result = SituacaoServicoServices.save(situacaoServico, null, connect);
				if(result.getCode()<=0) {
					Conexao.rollback(connect);
					return new Result(-3, "Erro ao importar situacao_servico >> ord_situacao_servico");
				}
			}
			
			/* ************************
			 * ORD_TIPO_PRODUTO_SERVICO
			 * ************************/
			ResultSetMap rsmTipoItemCentaurus = new ResultSetMap(
					conOrigem.prepareStatement(
							" SELECT nm_tipo_item FROM tipo_item "
							).executeQuery()
					);
			while(rsmTipoItemCentaurus.next()) {
				TipoProdutoServico tipoProduto = new TipoProdutoServico();
				tipoProduto.setNmTipoProdutoServico(rsmTipoItemCentaurus.getString("nm_tipo_item").toUpperCase());
				
				result = TipoProdutoServicoServices.save(tipoProduto, connect);
				if(result.getCode()<=0) {
					Conexao.rollback(connect);
					return new Result(-4, "Erro ao importar tipo_item >> ord_tipo_produto_servico");
				}
			}
			
			/* ********************
			 * ORD_TIPO_ATENDIMENTO
			 * ********************/
			ResultSetMap rsmTipoServicoCentaurus = new ResultSetMap(
					conOrigem.prepareStatement(
							" SELECT nm_tipo_servico, ds_tipo_servico FROM tipo_servico "
							).executeQuery()
					);
			
			while(rsmTipoServicoCentaurus.next()) {
				TipoAtendimento tipoAtendimento = new TipoAtendimento();
				tipoAtendimento.setNmTipoAtendimento(rsmTipoServicoCentaurus.getString("nm_tipo_servico").toUpperCase());
				tipoAtendimento.setTxtTipoAtendimento(rsmTipoServicoCentaurus.getString("ds_tipo_servico").toUpperCase());
				
				result = TipoAtendimentoServices.save(tipoAtendimento, connect);
				if(result.getCode()<=0) {
					Conexao.rollback(connect);
					return new Result(-5, "Erro ao importar tipo_servico >> ord_tipo_atendimento");
				}
			}
			
			connect.commit();
			return new sol.util.Result(1, "Dados importados com sucesso");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar tabelas de tipos de OS do Centaurus!", e);
		}
		finally{
			Conexao.desconectar(connect);
			Conexao.desconectar(conOrigem);
		}
	}
}
