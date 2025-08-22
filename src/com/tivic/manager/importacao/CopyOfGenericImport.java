package com.tivic.manager.importacao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import com.tivic.manager.adm.*;
import com.tivic.manager.alm.*;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.*;
import com.tivic.manager.ptc.*;
import com.tivic.manager.ptc.TipoDocumento;
import com.tivic.manager.util.Util;



public class CopyOfGenericImport {
	/*
	 * IMPORTAÇÃO POLIMEDIC
	 */
	public static sol.util.Result importPessoaFromFornecedor()	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = conectar();
		try {
			/***********************
			 * Importando pessoas
			 ***********************/
			PreparedStatement pesqPessoa  = connect.prepareStatement("SELECT * FROM grl_pessoa WHERE id_pessoa = ?");
			PreparedStatement pesqEstado  = connect.prepareStatement("SELECT * FROM grl_estado WHERE sg_estado = ?");
			PreparedStatement pesqCidade  = connect.prepareStatement("SELECT * FROM grl_cidade WHERE nm_cidade = ?");
			PreparedStatement pesqVinculo = connect.prepareStatement("SELECT * FROM grl_vinculo WHERE nm_vinculo = ?");
			System.out.println("Importando pessoas...");
			//
			ResultSet rsPessoa = conOrigem.prepareStatement("SELECT * FROM fornecedor").executeQuery();
			while(rsPessoa.next())	{
				pesqPessoa.setString(1, rsPessoa.getString("fornecedor"));
				ResultSet rsTemp = pesqPessoa.executeQuery();
				if(!rsTemp.next())	{
					int cdEmpresa = 2;
					int cdVinculo = 0;
					pesqVinculo.setString(1, "FORNECEDOR");
					rsTemp = pesqVinculo.executeQuery();
					if(!rsTemp.next())	{
						Vinculo vinculo = new Vinculo(0,"FORNECEDOR",1 /*lgEstatico*/, 0 /*lgFuncao*/, 0 /*cdFormulario*/, 1 /*lgCadastro*/);
						cdVinculo = VinculoDAO.insert(vinculo, connect);
					}
					else
						cdVinculo = rsTemp.getInt("cd_vinculo");
					//
					String nrTelefone1 = rsPessoa.getString("telefone");
					nrTelefone1 = nrTelefone1!=null ? nrTelefone1.replaceAll("[-]", "").replaceAll("[(]", "").replaceAll("[)]", "") : null;
					String nrTelefone2 = rsPessoa.getString("telefone2");
					nrTelefone2 = nrTelefone2!=null ? nrTelefone2.replaceAll("[-]", "").replaceAll("[(]", "").replaceAll("[)]", "") : null;
					String nrCelular   = null;
					nrCelular = nrCelular!=null ? nrCelular.replaceAll("[-]", "").replaceAll("[(]", "").replaceAll("[)]", "") : null;
					String nrCpfCnpj   = rsPessoa.getString("cgccpf");
					nrCpfCnpj = nrCpfCnpj!=null ? nrCpfCnpj.replaceAll("[ ]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[.]", "") : null;
					String nrRg   = rsPessoa.getString("identidade");
					if(nrRg!=null && nrRg.length()>15)
						nrRg = nrRg.trim().substring(0,14);
					Pessoa pessoa;
					// PESSOA FÍSICA
					String nmPessoa = rsPessoa.getString("Nome");
					if(nmPessoa!=null && nmPessoa.length()>50)
						nmPessoa = nmPessoa.substring(0,49);
					if(rsPessoa.getString("pessoaFJ").equals("F"))	{
						if(nrCpfCnpj!=null && nrCpfCnpj.length()>14)
							nrCpfCnpj = nrCpfCnpj.trim().substring(0,13);
						// Cidade - Naturalidade
						int cdNaturalidade = 0;
						/*
						pesqCidade.setString(1, rsPessoa.getString("cd_cidade_naturalidade"));
						rsTemp = pesqCidade.executeQuery();
						if(rsTemp.next())
							cdNaturalidade = rsTemp.getInt("cd_cidade");
						*/
						// Estado - RG
						int cdEstadoRg     = 0;
						/*
						pesqEstado.setString(1, rsPessoa.getString("sg_uf_rg"));
						rsTemp = pesqEstado.executeQuery();
						if(rsTemp.next())
							cdEstadoRg = rsTemp.getInt("cd_estado");
						*/
						pessoa = new PessoaFisica(0/*cdPessoa*/,0/*cdPessoaSuperior*/,0/*cdPais*/,nmPessoa,
								   							   nrTelefone1,nrTelefone2,nrCelular,null /*nrFax*/,rsPessoa.getString("email"),
								   							   Util.convTimestampToCalendar(rsPessoa.getTimestamp("datacadastro")),
								   							   PessoaServices.TP_FISICA,null /*imgFoto*/,1 /*stCadastro*/,
								   							   rsPessoa.getString("homepage"),null/*nmApelido*/,null/*observacao*/,
								   							   0/*lgNotificacao*/,rsPessoa.getString("fornecedor")/*idPessoa*/,
								   							   0/*cdClassificacao*/,0/*cdFormaDivulgacao*/,null /*dtChegadaPais*/,
								   							   cdNaturalidade, 0 /*cdEscolaridade*/, 
								   							   null /*dtNascimento*/,
								   							   nrCpfCnpj,"SSP"/*orgao rg*/,null /*nmMae*/,
								   							   null/*nmPai*/,0/*tpSexo*/,0/*stEstadoCivil*/,
								   							   nrRg,null/*nrCnh*/,null/*dtValidadeCnh*/,
								   							   null/*dtPrimeiraHabilitacao*/,0/*tpCategoriaHabilitacao*/,0/*tpRaca*/,
								   							   0/*lgDeficienteFisico*/,null/*nmFormaTratamento*/,cdEstadoRg,null/*dtEmissaoRg*/,
								   							   null /*blbFingerprint*/);
					}
					else	{
						String nmFantasia = rsPessoa.getString("Nome");
						if(rsPessoa.getString("fantasia")!=null)
							nmFantasia = rsPessoa.getString("fantasia");
						pessoa = new PessoaJuridica(0/*cdPessoa*/,0/*cdPessoaSuperior*/,0/*cdPais*/,nmFantasia,
	   							   nrTelefone1,nrTelefone2,nrCelular,null /*nrFax*/,rsPessoa.getString("email"),
	   							   Util.convTimestampToCalendar(rsPessoa.getTimestamp("datacadastro")),
	   							   PessoaServices.TP_JURIDICA,null /*imgFoto*/,1 /*stCadastro*/,
	   							   null/*nmUrl*/,null/*nmApelido*/,null/*txtObservacao*/,
	   							   0/*lgNotificacao*/,rsPessoa.getString("fornecedor")/*idPessoa*/,
	   							   0/*cdClassificacao*/,0/*cdFormaDivulgacao*/,null /*dtChegadaPais*/,
	   							   nrCpfCnpj,nmPessoa, rsPessoa.getString("inscestadual"), rsPessoa.getString("inscmunicipal"),
	   							   0/*nrFuncionarios*/,null/*dtInicioAtividade*/,0/*cdNaturezaJuridica*/,
	   							   0/*tpEmpresa*/,null/*dtTerminoAtividade*/);
					}
					// ENDEREÇO
					int cdCidade = 0;
					
					pesqCidade.setString(1, rsPessoa.getString("Cidade")!=null ? rsPessoa.getString("Cidade").toUpperCase() : null);
					rsTemp = pesqCidade.executeQuery();
					if(rsTemp.next())
						cdCidade = rsTemp.getInt("cd_cidade");
					else	{
						int cdEstado = 0;
						pesqEstado.setString(1, rsPessoa.getString("estado")!=null ? rsPessoa.getString("estado").toUpperCase() : "");
						rsTemp = pesqEstado.executeQuery();
						if(rsTemp.next())
							cdEstado = rsTemp.getInt("cd_estado");
						com.tivic.manager.grl.Cidade cidade = new com.tivic.manager.grl.Cidade(0,rsPessoa.getString("cidade"),null/*cep*/,0/* vlAltitude*/,
								 0/* vlLatitude*/,0/* vlLongitude*/,cdEstado,
								 null/* idCidade*/,0/* cdRegiao*/,null/* idIbge*/, null, 0, 0);
						cdCidade = com.tivic.manager.grl.CidadeDAO.insert(cidade, connect);
					}
					String nrCep = rsPessoa.getString("cep");
					if(nrCep!=null && nrCep.trim().length()>8)
						nrCep = nrCep.trim().substring(0,7);
					PessoaEndereco endereco = new PessoaEndereco(0,0/*cdPessoa*/,rsPessoa.getString("endereco"),0/*cdTipoLogradouro*/,0/*cdTipoEndereco*/,
															     0/*cdLogradouro*/,0/*cdBairro*/,cdCidade,rsPessoa.getString("endereco"),
															     rsPessoa.getString("bairro"),nrCep,
															     rsPessoa.getString("numero"),rsPessoa.getString("complemento"),
															     nrTelefone1,null/*nmPontoReferencia*/,0/*lgCobranca*/,1/*lgPrincipal*/);
					int cdPessoa = PessoaServices.insert(pessoa, endereco, cdEmpresa, cdVinculo).getCode();
					if(cdPessoa < 0)
						System.out.println("Não foi possivel incluir["+cdPessoa+"]: "+pessoa);
				}
			}
			System.out.println("Importação de pessoas concluída!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar pessoas!", e);
		}
		finally{
			Conexao.desconectar(connect);
			Conexao.desconectar(conOrigem);
		}
	}

	public static Connection conectar()	{
		try{
			Class.forName("org.firebirdsql.jdbc.FBDriver").newInstance();
	  		DriverManager.setLoginTimeout(80);
			return DriverManager.getConnection("jdbc:firebirdsql://127.0.0.1:3050//projetos/documents/manager/Imobiliário/proimo/BASE.GDB", "sysdba","masterkey");
			/*
			Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
	  		DriverManager.setLoginTimeout(80);
			return DriverManager.getConnection("jdbc:jtds:sqlserver://127.0.0.1:1433/polimedic", "tivic","13071980");
			*/
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public static sol.util.Result importPessoaFromClientes()	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = conectar();
		try {
			/***********************
			 * Importando pessoas
			 ***********************/
			PreparedStatement pesqPessoa  = connect.prepareStatement("SELECT * FROM grl_pessoa WHERE id_pessoa = ?");
			PreparedStatement pesqEstado  = connect.prepareStatement("SELECT * FROM grl_estado WHERE sg_estado = ?");
			PreparedStatement pesqCidade  = connect.prepareStatement("SELECT * FROM grl_cidade WHERE nm_cidade = ?");
			PreparedStatement pesqVinculo = connect.prepareStatement("SELECT * FROM grl_vinculo WHERE nm_vinculo = ?");
			System.out.println("Importando pessoas...");
			//
			ResultSet rsPessoa = conOrigem.prepareStatement("SELECT * FROM clientes").executeQuery();
			while(rsPessoa.next())	{
				pesqPessoa.setString(1, "CLI_"+rsPessoa.getString("cliente"));
				ResultSet rsTemp = pesqPessoa.executeQuery();
				if(!rsTemp.next())	{
					int cdEmpresa = 2;
					int cdVinculo = 0;
					pesqVinculo.setString(1, "CLIENTE");
					rsTemp = pesqVinculo.executeQuery();
					if(!rsTemp.next())	{
						Vinculo vinculo = new Vinculo(0,"CLIENTE",1 /*lgEstatico*/, 0 /*lgFuncao*/, 0 /*cdFormulario*/, 1 /*lgCadastro*/);
						cdVinculo = VinculoDAO.insert(vinculo, connect);
					}
					else
						cdVinculo = rsTemp.getInt("cd_vinculo");
					//
					String nrTelefone1 = rsPessoa.getString("telefone");
					nrTelefone1 = nrTelefone1!=null ? nrTelefone1.replaceAll("[-]", "").replaceAll("[(]", "").replaceAll("[)]", "") : null;
					String nrTelefone2 = rsPessoa.getString("telefone2");
					nrTelefone2 = nrTelefone2!=null ? nrTelefone2.replaceAll("[-]", "").replaceAll("[(]", "").replaceAll("[)]", "") : null;
					String nrFax = rsPessoa.getString("telefone2");
					nrFax = nrFax!=null ? nrFax.replaceAll("[-]", "").replaceAll("[(]", "").replaceAll("[)]", "") : null;
					String nrCelular   = null;
					nrCelular = nrCelular!=null ? nrCelular.replaceAll("[-]", "").replaceAll("[(]", "").replaceAll("[)]", "") : null;
					String nrCpfCnpj   = rsPessoa.getString("cgccpf");
					nrCpfCnpj = nrCpfCnpj!=null ? nrCpfCnpj.replaceAll("[ ]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[.]", "") : null;
					String nrRg   = rsPessoa.getString("identidade");
					if(nrRg!=null && nrRg.length()>15)
						nrRg = nrRg.trim().substring(0,14);
					Pessoa pessoa;
					// PESSOA FÍSICA
					String nmPessoa = rsPessoa.getString("Nome");
					if(nmPessoa!=null && nmPessoa.length()>50)
						nmPessoa = nmPessoa.substring(0,49);
					if(rsPessoa.getString("pessoaFJ").equals("F"))	{
						if(nrCpfCnpj!=null && nrCpfCnpj.length()>14)
							nrCpfCnpj = nrCpfCnpj.trim().substring(0,13);
						// Cidade - Naturalidade
						int cdNaturalidade = 0;
						/*
						pesqCidade.setString(1, rsPessoa.getString("cd_cidade_naturalidade"));
						rsTemp = pesqCidade.executeQuery();
						if(rsTemp.next())
							cdNaturalidade = rsTemp.getInt("cd_cidade");
						*/
						// Estado - RG
						int cdEstadoRg     = 0;
						/*
						pesqEstado.setString(1, rsPessoa.getString("sg_uf_rg"));
						rsTemp = pesqEstado.executeQuery();
						if(rsTemp.next())
							cdEstadoRg = rsTemp.getInt("cd_estado");
						*/
						pessoa = new PessoaFisica(0/*cdPessoa*/,0/*cdPessoaSuperior*/,0/*cdPais*/,nmPessoa,
								   							   nrTelefone1,nrTelefone2,nrCelular,nrFax,rsPessoa.getString("email"),
								   							   Util.convTimestampToCalendar(rsPessoa.getTimestamp("datacadastro")),
								   							   PessoaServices.TP_FISICA,null /*imgFoto*/,1 /*stCadastro*/,
								   							   rsPessoa.getString("homepage"),null/*nmApelido*/,null/*observacao*/,
								   							   0/*lgNotificacao*/,"CLI_"+rsPessoa.getString("cliente")/*idPessoa*/,
								   							   0/*cdClassificacao*/,0/*cdFormaDivulgacao*/,null /*dtChegadaPais*/,
								   							   cdNaturalidade, 0 /*cdEscolaridade*/, 
								   							   null /*dtNascimento*/,
								   							   nrCpfCnpj,"SSP"/*orgao rg*/,null /*nmMae*/,
								   							   null/*nmPai*/,0/*tpSexo*/,0/*stEstadoCivil*/,
								   							   nrRg,null/*nrCnh*/,null/*dtValidadeCnh*/,
								   							   null/*dtPrimeiraHabilitacao*/,0/*tpCategoriaHabilitacao*/,0/*tpRaca*/,
								   							   0/*lgDeficienteFisico*/,null/*nmFormaTratamento*/,cdEstadoRg,null/*dtEmissaoRg*/,
								   							   null /*blbFingerprint*/);
					}
					else	{
						String nmFantasia = rsPessoa.getString("Nome");
						if(rsPessoa.getString("fantasia")!=null)
							nmFantasia = rsPessoa.getString("fantasia");
						pessoa = new PessoaJuridica(0/*cdPessoa*/,0/*cdPessoaSuperior*/,0/*cdPais*/,nmFantasia,
	   							   nrTelefone1,nrTelefone2,nrCelular,null /*nrFax*/,rsPessoa.getString("email"),
	   							   Util.convTimestampToCalendar(rsPessoa.getTimestamp("datacadastro")),
	   							   PessoaServices.TP_JURIDICA,null /*imgFoto*/,1 /*stCadastro*/,
	   							   null/*nmUrl*/,null/*nmApelido*/,null/*txtObservacao*/,
	   							   0/*lgNotificacao*/,"CLI_"+rsPessoa.getString("cliente")/*idPessoa*/,
	   							   0/*cdClassificacao*/,0/*cdFormaDivulgacao*/,null /*dtChegadaPais*/,
	   							   nrCpfCnpj,nmPessoa, rsPessoa.getString("inscestadual"), rsPessoa.getString("inscmunicipal"),
	   							   0/*nrFuncionarios*/,null/*dtInicioAtividade*/,0/*cdNaturezaJuridica*/,
	   							   0/*tpEmpresa*/,null/*dtTerminoAtividade*/);
					}
					// ENDEREÇO
					int cdCidade = 0;
					
					pesqCidade.setString(1, rsPessoa.getString("Cidade")!=null ? rsPessoa.getString("Cidade").toUpperCase() : null);
					rsTemp = pesqCidade.executeQuery();
					if(rsTemp.next())
						cdCidade = rsTemp.getInt("cd_cidade");
					else	{
						int cdEstado = 0;
						pesqEstado.setString(1, rsPessoa.getString("estado")!=null ? rsPessoa.getString("estado").toUpperCase() : "");
						rsTemp = pesqEstado.executeQuery();
						if(rsTemp.next())
							cdEstado = rsTemp.getInt("cd_estado");
						com.tivic.manager.grl.Cidade cidade = new com.tivic.manager.grl.Cidade(0,rsPessoa.getString("cidade"),null/*cep*/,0/* vlAltitude*/,
								 0/* vlLatitude*/,0/* vlLongitude*/,cdEstado,
								 null/* idCidade*/,0/* cdRegiao*/,null/* idIbge*/, null, 0, 0);
						cdCidade = com.tivic.manager.grl.CidadeDAO.insert(cidade, connect);
					}
					String nrCep = rsPessoa.getString("cep");
					if(nrCep!=null && nrCep.trim().length()>8)
						nrCep = nrCep.trim().substring(0,7);
					PessoaEndereco endereco = new PessoaEndereco(0,0/*cdPessoa*/,rsPessoa.getString("endereco"),0/*cdTipoLogradouro*/,0/*cdTipoEndereco*/,
															     0/*cdLogradouro*/,0/*cdBairro*/,cdCidade,rsPessoa.getString("endereco"),
															     rsPessoa.getString("bairro"),nrCep,
															     rsPessoa.getString("numero"),rsPessoa.getString("complemento"),
															     nrTelefone1,null/*nmPontoReferencia*/,0/*lgCobranca*/,1/*lgPrincipal*/);
					int cdPessoa = PessoaServices.insert(pessoa, endereco, cdEmpresa, cdVinculo).getCode();
					if(cdPessoa < 0)	{
						System.out.println("Não foi possivel incluir["+cdPessoa+"]: "+pessoa);
					}
				}
			}
			System.out.println("Importação de pessoas concluída!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar pessoas!", e);
		}
		finally{
			Conexao.desconectar(connect);
			Conexao.desconectar(conOrigem);
		}
	}

	public static sol.util.Result importClientesFromSisDispo(int cdEmpresa)	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = conectar();
		try {
			/***********************
			 * Importando pessoas
			 ***********************/
			PreparedStatement pesqPessoa  = connect.prepareStatement("SELECT * FROM grl_pessoa WHERE id_pessoa = ?");
			PreparedStatement pesqEstado  = connect.prepareStatement("SELECT * FROM grl_estado WHERE sg_estado = ?");
			PreparedStatement pesqCidade  = connect.prepareStatement("SELECT * FROM grl_cidade WHERE nm_cidade = ?");
			PreparedStatement pesqVinculo = connect.prepareStatement("SELECT * FROM grl_vinculo WHERE nm_vinculo = ?");
			System.out.println("Importando pessoas...");
			//
			ResultSet rsPessoa = conOrigem.prepareStatement("SELECT * FROM clientes").executeQuery();
			while(rsPessoa.next())	{
				pesqPessoa.setString(1, "CLI_"+rsPessoa.getString("idclie"));
				ResultSet rsTemp = pesqPessoa.executeQuery();
				if(!rsTemp.next())	{
					int cdVinculo = 0;
					pesqVinculo.setString(1, "CLIENTE");
					rsTemp = pesqVinculo.executeQuery();
					if(!rsTemp.next())	{
						Vinculo vinculo = new Vinculo(0,"CLIENTE",1 /*lgEstatico*/, 0 /*lgFuncao*/, 0 /*cdFormulario*/, 1 /*lgCadastro*/);
						cdVinculo = VinculoDAO.insert(vinculo, connect);
					}
					else
						cdVinculo = rsTemp.getInt("cd_vinculo");
					//
					String nrDDD = rsPessoa.getString("PFXTELRES");
					if(nrDDD!=null)
						nrDDD = nrDDD.replaceAll("077", "77");
					String nrTelefone1 = nrDDD+rsPessoa.getString("TELRES");
					nrTelefone1 = nrTelefone1!=null ? nrTelefone1.replaceAll("[-]", "").replaceAll("[(]", "").replaceAll("[)]", "") : null;
					nrDDD = rsPessoa.getString("PFXTELCOM");
					if(nrDDD!=null)
						nrDDD = nrDDD.replaceAll("077", "77");
					String nrTelefone2 = nrDDD+rsPessoa.getString("TELCOM");
					nrTelefone2 = nrTelefone2!=null ? nrTelefone2.replaceAll("[-]", "").replaceAll("[(]", "").replaceAll("[)]", "") : null;
					String nrFax = null; 
					String nrCelular   = null;
					String nrCpfCnpj   = rsPessoa.getString("CPFCLI");
					nrCpfCnpj = nrCpfCnpj!=null ? nrCpfCnpj.replaceAll("[ ]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[.]", "") : null;
					String nrRg   = rsPessoa.getString("RGNUM");
					if(nrRg!=null && nrRg.length()>15)
						nrRg = nrRg.trim().substring(0,14);
					Pessoa pessoa;
					// PESSOA FÍSICA
					String nmPessoa = rsPessoa.getString("NOMCLI");
					if(nmPessoa!=null && nmPessoa.length()>50)
						nmPessoa = nmPessoa.substring(0,49);
					if(true /*Somente pessoa física*/)	{
						if(nrCpfCnpj!=null && nrCpfCnpj.length()>14)
							nrCpfCnpj = nrCpfCnpj.trim().substring(0,13);
						// Cidade - Naturalidade
						int cdNaturalidade = 0;
						/*
						pesqCidade.setString(1, rsPessoa.getString("cd_cidade_naturalidade"));
						rsTemp = pesqCidade.executeQuery();
						if(rsTemp.next())
							cdNaturalidade = rsTemp.getInt("cd_cidade");
						*/
						// Estado - RG
						int cdEstadoRg     = 0;
						int tpSexo = rsPessoa.getString("SEXO")!=null && !rsPessoa.getString("SEXO").trim().equals("") ? Integer.parseInt(rsPessoa.getString("SEXO").trim())-1 : -1;
						int stEstadoCivil = rsPessoa.getString("ESTCIV")!=null && !rsPessoa.getString("ESTCIV").trim().equals("") ? Integer.parseInt(rsPessoa.getString("ESTCIV").trim()) : -1;
						switch(stEstadoCivil){
							case 1: stEstadoCivil = 0; break;
							case 2: stEstadoCivil = 6; break;
							case 3: stEstadoCivil = 1; break;
							case 4: stEstadoCivil = 7; break;
							case 5: stEstadoCivil = 3; break;
							case 6: stEstadoCivil = 2; break;
							case 7: stEstadoCivil = 4 ; break;
							default:
								stEstadoCivil = 5;
						}
						GregorianCalendar dtNascimento = Util.convTimestampToCalendar(rsPessoa.getTimestamp("DTNASC"));
						GregorianCalendar dtEmissaoRg = Util.convTimestampToCalendar(rsPessoa.getTimestamp("RGDTEXP"));
						/*
						pesqEstado.setString(1, rsPessoa.getString("sg_uf_rg"));
						rsTemp = pesqEstado.executeQuery();
						if(rsTemp.next())
							cdEstadoRg = rsTemp.getInt("cd_estado");
						*/
						pessoa = new PessoaFisica(0/*cdPessoa*/,0/*cdPessoaSuperior*/,0/*cdPais*/,nmPessoa,
								   							   nrTelefone1,nrTelefone2,nrCelular,nrFax,null/*email*/,
								   							   new GregorianCalendar(),
								   							   PessoaServices.TP_FISICA,null /*imgFoto*/,1 /*stCadastro*/,
								   							   null/*homepage*/,null/*nmApelido*/,null/*observacao*/,
								   							   0/*lgNotificacao*/,"CLI_"+rsPessoa.getString("IDCLIE")/*idPessoa*/,
								   							   0/*cdClassificacao*/,0/*cdFormaDivulgacao*/,null /*dtChegadaPais*/,
								   							   cdNaturalidade, 0 /*cdEscolaridade*/, dtNascimento,
								   							   nrCpfCnpj,rsPessoa.getString("RGORGEX")/*orgao rg*/,null /*nmMae*/,
								   							   null/*nmPai*/,tpSexo,stEstadoCivil,
								   							   nrRg,null/*nrCnh*/,null/*dtValidadeCnh*/,
								   							   null/*dtPrimeiraHabilitacao*/,0/*tpCategoriaHabilitacao*/,0/*tpRaca*/,
								   							   0/*lgDeficienteFisico*/,null/*nmFormaTratamento*/,cdEstadoRg,dtEmissaoRg,
								   							   null /*blbFingerprint*/);
					}
					// ENDEREÇO
					String dsEndereco = rsPessoa.getString("endere"); 
					if(dsEndereco!=null && dsEndereco.length()>50)
						dsEndereco = dsEndereco.substring(0,49);
					int cdCidade = 0;
					pesqCidade.setString(1, rsPessoa.getString("Cidade")!=null ? rsPessoa.getString("Cidade").toUpperCase() : null);
					rsTemp = pesqCidade.executeQuery();
					if(rsTemp.next())
						cdCidade = rsTemp.getInt("cd_cidade");
					else	{
						int cdEstado = 0;
						pesqEstado.setString(1, rsPessoa.getString("estado")!=null ? rsPessoa.getString("estado").toUpperCase() : "");
						rsTemp = pesqEstado.executeQuery();
						if(rsTemp.next())
							cdEstado = rsTemp.getInt("cd_estado");
						com.tivic.manager.grl.Cidade cidade = new com.tivic.manager.grl.Cidade(0,rsPessoa.getString("cidade"),null/*cep*/,0/* vlAltitude*/,
								 0/* vlLatitude*/,0/* vlLongitude*/,cdEstado,
								 null/* idCidade*/,0/* cdRegiao*/,null/* idIbge*/, null, 0, 0);
						cdCidade = com.tivic.manager.grl.CidadeDAO.insert(cidade, connect);
					}
					String nrCep = rsPessoa.getString("cep");
					if(nrCep!=null && nrCep.trim().length()>8)
						nrCep = nrCep.trim().substring(0,7);
					PessoaEndereco endereco = new PessoaEndereco(0,0/*cdPessoa*/,dsEndereco,0/*cdTipoLogradouro*/,0/*cdTipoEndereco*/,
															     0/*cdLogradouro*/,0/*cdBairro*/,cdCidade,dsEndereco,
															     rsPessoa.getString("bairro"),nrCep, null/*numero*/,null/*complemento*/,
															     nrTelefone1,null/*nmPontoReferencia*/,0/*lgCobranca*/,1/*lgPrincipal*/);
					int cdPessoa = PessoaServices.insert(pessoa, endereco, cdEmpresa, cdVinculo).getCode();
					if(cdPessoa < 0)	{
						System.out.println("Não foi possivel incluir["+cdPessoa+"]: "+pessoa);
					}
				}
			}
			System.out.println("Importação de pessoas concluída!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar pessoas!", e);
		}
		finally{
			Conexao.desconectar(connect);
			Conexao.desconectar(conOrigem);
		}
	}
	
	private static int getNivelLocal(String nmNivelLocal, int cdNivelSuperior, Connection connect){
		try	{
			PreparedStatement pesqNivelLocal = connect.prepareStatement("SELECT * FROM alm_nivel_local WHERE nm_nivel_local = ?");
			pesqNivelLocal.setString(1, nmNivelLocal);
			ResultSet rsTemp = pesqNivelLocal.executeQuery();
			if(!rsTemp.next())	{
				NivelLocal nivelLocal = new NivelLocal(0,"EMPREENDIMENTO",0/*lgArmazena*/,0/*lgSetor*/,cdNivelSuperior);
				return NivelLocalDAO.insert(nivelLocal, connect);
			}
			else
				return rsTemp.getInt("cd_nivel_local");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return 0;
		}
	}
	
	public static sol.util.Result importUnidadesFromSisDispo(int cdEmpresa)	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = conectar();
		try {
			/***********************
			 * Importando Unidades
			 ***********************/
			PreparedStatement pesqProduto = connect.prepareStatement("SELECT * FROM grl_produto_servico WHERE id_produto_servico = ?");
			PreparedStatement pesqLocal   = connect.prepareStatement("SELECT * FROM alm_local_armazenamento WHERE id_local_armazenamento = ?");
			PreparedStatement pesqReferencia = connect.prepareStatement("SELECT * FROM alm_produto_referencia WHERE cd_produto_servico = ? AND id_referencia = ?");
			PreparedStatement pesqPessoa  = connect.prepareStatement("SELECT * FROM grl_pessoa WHERE id_pessoa = ?");
			// Verificando tipo de documento
			int cdTipoDocumento = 0;
			ResultSet rs = connect.prepareStatement("SELECT * FROM ptc_tipo_documento WHERE nm_tipo_documento = \'CONTRATO\'").executeQuery();
			if(!rs.next())	
				cdTipoDocumento = com.tivic.manager.ptc.TipoDocumentoDAO.insert(
						new com.tivic.manager.ptc.TipoDocumento(0, "CONTRATO", null, com.tivic.manager.ptc.TipoDocumentoServices.TP_INC_COM_ANO,
								0, 0, 0, 0, null, null, 0, 0, 0, null), 
						connect);
			else
				cdTipoDocumento = rs.getInt("cd_tipo_documento");
			// Verificando SETOR
			int cdSetor         = 0;
			rs = connect.prepareStatement("SELECT * FROM grl_setor WHERE nm_setor = \'DOCUMENTAÇÃO\' AND cd_empresa = "+cdEmpresa).executeQuery();
			if(!rs.next())
				cdSetor = SetorDAO.insert(new Setor(0,0,cdEmpresa,0,"DOCUMENTAÇÃO",1,null,null,null,null,null,null,null,0,null,"DOC",null,0,0,null), connect);
			else
				cdSetor = rs.getInt("cd_setor");
			// Verificando SETOR
			int cdTipoOperacao = 0;
			rs = connect.prepareStatement("SELECT * FROM adm_tipo_operacao WHERE nm_tipo_operacao = \'VENDA\'").executeQuery();
			if(!rs.next())
				cdTipoOperacao = TipoOperacaoDAO.insert(new TipoOperacao(0,"VENDA",null,1,1,0), connect);
			else
				cdTipoOperacao = rs.getInt("cd_tipo_operacao");
			//
			System.out.println("Importando Unidades...");
			//
			ResultSet rsTUnidade = conOrigem.prepareStatement("SELECT * FROM tipunid").executeQuery();
			while(rsTUnidade.next())	{
				int cdProdutoServico = 0;
				// Produto / Serviço
				pesqProduto.setString(1, "IMB_"+rsTUnidade.getString("idtipunid"));
				ResultSet rsTemp = pesqProduto.executeQuery();
				if(!rsTemp.next())	{
					ProdutoServico prodServ = new ProdutoServico(0,0,rsTUnidade.getString("DSTIPUNID"),null,null,null,null,
																		ProdutoServicoServices.TP_PRODUTO,"IMB_"+rsTUnidade.getString("idtipunid"),
																		rsTUnidade.getString("DSTIPUNID").substring(0,5),0,0,0,null, 0, null);
					cdProdutoServico = ProdutoServicoDAO.insert(prodServ, connect);
					ProdutoServicoEmpresa produtoEmpresa = new ProdutoServicoEmpresa(cdEmpresa,cdProdutoServico,0,null,(float)0,(float)0,(float)0,(float)0,(float)0,(float)0,(float)0,
							                                                          0,0,0,0, ProdutoServicoEmpresaServices.CTL_INDIVIDUAL,0,1,null,null,0);
					ProdutoServicoEmpresaDAO.insert(produtoEmpresa, connect);
				}
				else
					cdProdutoServico = rsTemp.getInt("cd_produto_servico");
				// Unidades
				ResultSet rsUnidades = conOrigem.prepareStatement("SELECT A.*, SUBSTRING(cdunid from 1 for 1) AS andar, B.cdcorretor, C.dscorretor," +
						                                          "       B.cdgerador, D.dsgerador " +
						                                          "FROM unidades A " +
						                                          "LEFT OUTER JOIN clientes B ON (A.idclie = B.idclie) " +
						                                          "LEFT OUTER JOIN corretores C ON (B.cdcorretor = C.cdcorretor) " +
						                                          "LEFT OUTER JOIN geradores  D ON (B.cdgerador = D.cdgerador) " +
						                                          "WHERE A.idtipunid = "+rsTUnidade.getString("idtipunid")).executeQuery();
				while(rsUnidades.next())	{
					int cdLocalArmazenamento = 0;
					// Pesquisando local
					pesqLocal.setString(1, "B"+rsUnidades.getString("IDBLOC")+"-"+rsUnidades.getString("ANDAR"));
					rsTemp = pesqLocal.executeQuery();
					if(rsTemp.next())
						cdLocalArmazenamento = rsTemp.getInt("cd_local_armazenamento");
					else
						return new sol.util.Result(-1, "B"+rsUnidades.getString("IDBLOC")+"-"+rsUnidades.getString("ANDAR")+" Não localizado.");
					//
					int cdReferencia = 0;
					pesqReferencia.setInt(1, cdProdutoServico);
					pesqReferencia.setString(2, rsUnidades.getString("idunid"));
					rsTemp = pesqReferencia.executeQuery();
					if(!rsTemp.next())	{
						String nmReferencia = rsUnidades.getString("cdunid");
						String idReferencia = rsUnidades.getString("idunid");
						String idReduzido   = null;
						// 
						ProdutoReferencia produtoReferencia = new ProdutoReferencia(0,cdProdutoServico,cdEmpresa,nmReferencia,idReferencia,null,null,
																						0,1/*stReferencia*/,0,0,idReduzido,cdLocalArmazenamento);
						cdReferencia = ProdutoReferenciaDAO.insert(produtoReferencia, connect);
					}
					else
						cdReferencia = rsTemp.getInt("cd_referencia");
					System.out.println("cdReferencia = "+cdReferencia);
					if(cdReferencia <= 0)
						return new sol.util.Result(-1, "Referência não localizada!");
					/*
					 * REGISTRANDO O CONTRATO
					 */
					if(rsUnidades.getInt("IDCLIE")>0)	{
						// Pesquisando o cliente
						int cdCliente  = 0;
						pesqPessoa.setString(1, "CLI_"+rsUnidades.getString("IDCLIE"));
						rsTemp = pesqPessoa.executeQuery();
						if(rsTemp.next())
							cdCliente  = rsTemp.getInt("cd_pessoa");
						// Pesquisando Agente / Correntor
						int cdAgente = 0;
						pesqPessoa.setString(1, "CRT_"+rsUnidades.getString("CDCORRETOR"));
						rsTemp = pesqPessoa.executeQuery();
						if(rsTemp.next())
							cdAgente  = rsTemp.getInt("cd_pessoa");
						else	{
							PessoaFisica pessoa = new PessoaFisica(0,0,0,rsUnidades.getString("dscorretor"),null,null,null,null ,null, new GregorianCalendar(),
		   							                               PessoaServices.TP_FISICA,null,1/*stCadastro*/, null,rsUnidades.getString("dscorretor"),null,
		   							                               0,"CRT_"+rsUnidades.getString("cdcorretor"),0,0,null,0,0,null,null,null,null,
		   							                               null,0,0,null,null,null,null,0,0,0,null,0,null,null);
							cdAgente = PessoaFisicaDAO.insert(pessoa, connect);
						}
						// Pesquisando USUARIO
						int cdUsuario = 0;
						if(rsUnidades.getString("dsgerador")!=null && !rsUnidades.getString("dsgerador").trim().equals(""))	{
							rsTemp = connect.prepareStatement("SELECT * FROM seg_usuario WHERE nm_login = \'"+rsUnidades.getString("dsgerador").toLowerCase()+"\'").executeQuery();
							if(rsTemp.next())
								cdUsuario = rsTemp.getInt("cd_usuario");
							else	{
								com.tivic.manager.seg.Usuario usuario = new com.tivic.manager.seg.Usuario(0,0,0,rsUnidades.getString("dsgerador").toLowerCase(),rsUnidades.getString("dsgerador").toLowerCase(),
									                                                    com.tivic.manager.seg.UsuarioServices.USUARIO_COMUM,null,1);
								cdUsuario = com.tivic.manager.seg.UsuarioDAO.insert(usuario, connect);
							}
						}
						// Informações do Contrato
						GregorianCalendar dtAssinatura = null, dtPrimeiraParcela = null;
						int nrDiaVencimento            = 0;
						float vlContrato               = 0;
						rsTemp = conOrigem.prepareStatement("SELECT MIN(dtvenc) AS dtvenc, SUM(valor) AS total FROM promis WHERE idclie = "+rsUnidades.getString("idclie")).executeQuery();
						if(rsTemp.next())	{
							dtAssinatura      = Util.convTimestampToCalendar(rsTemp.getTimestamp("dtvenc"));
							dtPrimeiraParcela = Util.convTimestampToCalendar(rsTemp.getTimestamp("dtvenc"));
							nrDiaVencimento   = dtAssinatura!=null ? dtAssinatura.get(Calendar.DAY_OF_MONTH) : 1;
							vlContrato        = rsTemp.getFloat("total");
						}
						rsTemp = conOrigem.prepareStatement("SELECT MIN(dtvenc) AS dtvenc, SUM(valor) AS total FROM promis WHERE idclie = "+rsUnidades.getString("idclie")).executeQuery();
						if(rsTemp.next())	{
							dtAssinatura      = Util.convTimestampToCalendar(rsTemp.getTimestamp("dtvenc"));
							dtPrimeiraParcela = Util.convTimestampToCalendar(rsTemp.getTimestamp("dtvenc"));
							nrDiaVencimento   = dtAssinatura!=null ? dtAssinatura.get(Calendar.DAY_OF_MONTH) : 1;
							vlContrato        = rsTemp.getFloat("total");
						}
						int nrParcelas   = 0;
						float vlParcelas = 0;
						rsTemp = conOrigem.prepareStatement("SELECT COUNT(*) AS quant, MIN(valor) AS valor FROM promis WHERE tipo = 2 AND idclie = "+rsUnidades.getString("idclie")).executeQuery();
						if(rsTemp.next())	{
							nrParcelas = rsTemp.getInt("quant");
							vlParcelas = rsTemp.getFloat("valor");
						}
						float vlAdesao = 0;
						rsTemp = conOrigem.prepareStatement("SELECT SUM(valor) AS valor FROM promis WHERE tipo = 1 AND idclie = "+rsUnidades.getString("idclie")).executeQuery();
						if(rsTemp.next())
							vlAdesao = rsTemp.getFloat("valor");
						int gnContrato = ContratoServices.gnCOMPRA_VENDA, tpContrato = ContratoServices.tpCONTRATADA;
						//
						int cdDocumento    = 0;
						String nrContrato = "", idContrato = "UND"+rsUnidades.getString("idunid");
						// Documento
						String nmLocalOrigem = "";
						String idDocumento   = idContrato;
						Documento documento  = new Documento(0,0,cdSetor,cdUsuario,nmLocalOrigem,dtAssinatura,DocumentoServices.TP_PUBLICO,
												null/*txtObservacao*/, idDocumento,null/*nrDocumento*/,cdTipoDocumento,0,0/*cdAtendimento*/,
												"Unidade nº "+rsUnidades.getString("idunid"),cdSetor,0/*cdSituacaoAtual*/,0/*cdFase*/,cdEmpresa, 
												0/*cdProcesso*/, 0 /*tpPrioridade*/, 0/*cdDocumentoSuperior*/, null /*dsAssunto*/, 
												null /*nrAtendimento*/, 0/*lgNotificacao*/, 0 /*cdTipoDocumentoAnterior*/, null/*nrDocumentoExterno*/,
												null/*nrAssunto*/, null, null, 0, 1);
						ArrayList<DocumentoPessoa> solicitantes = new ArrayList<DocumentoPessoa>();
						solicitantes.add(new DocumentoPessoa(0, cdCliente, "Cliente"));
						cdDocumento = DocumentoServices.insert(documento, solicitantes).getCode();
						// INCLUINDO O CONTRATO
						Contrato contrato = new Contrato(0,0,0,cdEmpresa,cdCliente,0,0,dtAssinatura,dtPrimeiraParcela,nrDiaVencimento,nrParcelas,
								                         0,0,0,0, tpContrato, vlParcelas, vlAdesao, vlContrato, nrContrato,null,1/*stContrato*/,
								                         idContrato, null /*dtInicioVigencia*/, null /*dtFinalVigencia*/, cdAgente, 0, 0, 0,
								                         gnContrato, 0, cdTipoOperacao, cdDocumento, 0, 0, 0, null, 0);
						int cdContrato = ContratoDAO.insert(contrato, connect);
						// Produto / Serviço
						ContratoProdutoServico crtProd = new ContratoProdutoServico(cdContrato,cdProdutoServico,1,vlContrato,dtAssinatura,0,0,cdReferencia,cdEmpresa);
						
						ContratoProdutoServicoDAO.insert(crtProd, connect);
					}
				}
			}
			System.out.println("Importação de unidades concluída!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar unidades!", e);
		}
		finally{
			Conexao.desconectar(connect);
			Conexao.desconectar(conOrigem);
		}
	}
	public static sol.util.Result importEmpreendimentoFromSisDispo(int cdEmpresa)	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = conectar();
		try {
			if(cdEmpresa <= 0)
				return new sol.util.Result(-1, "Empresa não informada!");
			/***********************
			 * Importando locais de armazenamento
			 ***********************/
			PreparedStatement pesqLocal      = connect.prepareStatement("SELECT * FROM alm_local_armazenamento WHERE id_local_armazenamento = ?");
			
			System.out.println("Importando empreendimentos...");
			/*
			 * EMPREENDIMENTOS
			 */
			ResultSet rsEmpreend = conOrigem.prepareStatement("SELECT * FROM empree").executeQuery();
			while(rsEmpreend.next())	{
				int cdEmpreendimento = 0;
				int cdNivelEmpreendimento = getNivelLocal("EMPREENDIMENTO", 0, connect);
				pesqLocal.setString(1, "EPR_"+rsEmpreend.getString("IDEMPRE"));
				ResultSet rsTemp = pesqLocal.executeQuery();
				if(!rsTemp.next())	{
					LocalArmazenamento localEmpre = new LocalArmazenamento(0,0/*cdSetor*/,cdNivelEmpreendimento,0/*cdResponsavel*/,rsEmpreend.getString("DSEMPRE"),
																			"EPR_"+rsEmpreend.getString("IDEMPRE")/*ID*/,0/*cdLocalArmazenamentoSuperior*/,cdEmpresa);
					cdEmpreendimento = LocalArmazenamentoDAO.insert(localEmpre, connect);
				}
				else
					cdEmpreendimento = rsTemp.getInt("cd_local_armazenamento");
				/*
				 * Blocos
				 */
				int cdBloco = 0;
				int cdNivelBloco = getNivelLocal("BLOCO", cdNivelEmpreendimento, connect);
				ResultSet rsBlocos = conOrigem.prepareStatement("SELECT * FROM blocos WHERE idempre = "+rsEmpreend.getInt("IDEMPRE")).executeQuery();
				while(rsBlocos.next())	{
					pesqLocal.setString(1, "BLC_"+rsBlocos.getString("IDBLOC"));
					rsTemp = pesqLocal.executeQuery();
					if(!rsTemp.next())	{
						LocalArmazenamento localBloco = new LocalArmazenamento(0,0/*cdSetor*/,cdNivelBloco,0/*cdResponsavel*/,rsBlocos.getString("EDIFIC"),
																				"BLC_"+rsBlocos.getString("IDBLOC")/*ID*/,cdEmpreendimento,cdEmpresa);
						cdBloco = LocalArmazenamentoDAO.insert(localBloco, connect);
					}
					else
						cdBloco = rsTemp.getInt("cd_local_armazenamento");
					/*
					 * ANDARES
					 */
					int cdNivelAndar = getNivelLocal("ANDAR", cdNivelBloco, connect);
					ResultSet rsAndares = conOrigem.prepareStatement("SELECT DISTINCT idbloc, substring(cdunid from 1 for 1) AS ANDAR FROM unidades " +
							                                         "WHERE idbloc = "+rsBlocos.getInt("IDBLOC")).executeQuery();
					while(rsAndares.next()){
						pesqLocal.setString(1, "B"+rsBlocos.getString("IDBLOC")+"-"+rsAndares.getString("ANDAR"));
						rsTemp = pesqLocal.executeQuery();
						if(!rsTemp.next())	{
							String nmAndar = rsAndares.getString("ANDAR").equals("0") ? "TÉRREO" : rsAndares.getString("ANDAR")+"º ANDAR";
							LocalArmazenamento localBloco = new LocalArmazenamento(0,0/*cdSetor*/,cdNivelAndar,0/*cdResponsavel*/,nmAndar,
																				   "B"+rsBlocos.getString("IDBLOC")+"-"+rsAndares.getString("ANDAR"),cdBloco,cdEmpresa);
							LocalArmazenamentoDAO.insert(localBloco, connect);
						}
						else
							rsTemp.getInt("cd_local_armazenamento");
					}
				}
			}
			System.out.println("Importação de empreendimentos e blocos concluída!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar empreendimentos e blocos!", e);
		}
		finally{
			Conexao.desconectar(connect);
			Conexao.desconectar(conOrigem);
		}
	}
}
