package com.tivic.manager.importacao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.tivic.manager.acd.Aluno;
import com.tivic.manager.acd.AlunoDAO;
import com.tivic.manager.acd.CursoModulo;
import com.tivic.manager.acd.CursoModuloDAO;
import com.tivic.manager.acd.Instituicao;
import com.tivic.manager.acd.InstituicaoDAO;
import com.tivic.manager.acd.InstituicaoEducacensoServices;
import com.tivic.manager.acd.InstituicaoPeriodo;
import com.tivic.manager.acd.InstituicaoPeriodoDAO;
import com.tivic.manager.acd.InstituicaoPeriodoServices;
import com.tivic.manager.acd.InstituicaoServices;
import com.tivic.manager.acd.Matricula;
import com.tivic.manager.acd.MatriculaDAO;
import com.tivic.manager.acd.MatriculaServices;
import com.tivic.manager.acd.OcorrenciaAlunoServices;
import com.tivic.manager.acd.OcorrenciaMatriculaServices;
import com.tivic.manager.acd.Oferta;
import com.tivic.manager.acd.OfertaDAO;
import com.tivic.manager.acd.ProgramaContaFinanceira;
import com.tivic.manager.acd.ProgramaContaFinanceiraServices;
import com.tivic.manager.acd.UnidadeExecutora;
import com.tivic.manager.acd.UnidadeExecutoraDAO;
import com.tivic.manager.adm.CategoriaEconomica;
import com.tivic.manager.adm.CategoriaEconomicaDAO;
import com.tivic.manager.adm.CategoriaEconomicaServices;
import com.tivic.manager.adm.CondicaoFormaPlanoPagamento;
import com.tivic.manager.adm.CondicaoFormaPlanoPagamentoDAO;
import com.tivic.manager.adm.CondicaoPagamento;
import com.tivic.manager.adm.CondicaoPagamentoCliente;
import com.tivic.manager.adm.CondicaoPagamentoClienteDAO;
import com.tivic.manager.adm.CondicaoPagamentoDAO;
import com.tivic.manager.adm.ContaFinanceira;
import com.tivic.manager.adm.ContaFinanceiraServices;
import com.tivic.manager.adm.ContaReceber;
import com.tivic.manager.adm.ContaReceberCategoria;
import com.tivic.manager.adm.ContaReceberServices;
import com.tivic.manager.adm.Contrato;
import com.tivic.manager.adm.ContratoDAO;
import com.tivic.manager.adm.ContratoProdutoServico;
import com.tivic.manager.adm.ContratoProdutoServicoDAO;
import com.tivic.manager.adm.ContratoServices;
import com.tivic.manager.adm.EntradaItemAliquota;
import com.tivic.manager.adm.EntradaItemAliquotaDAO;
import com.tivic.manager.adm.EntradaItemAliquotaServices;
import com.tivic.manager.adm.ProdutoServicoPreco;
import com.tivic.manager.adm.ProdutoServicoPrecoServices;
import com.tivic.manager.adm.SaidaItemAliquota;
import com.tivic.manager.adm.SaidaItemAliquotaDAO;
import com.tivic.manager.adm.SaidaItemAliquotaServices;
import com.tivic.manager.adm.TipoOperacao;
import com.tivic.manager.adm.TipoOperacaoDAO;
import com.tivic.manager.adm.TributoAliquotaServices;
import com.tivic.manager.alm.DocumentoEntrada;
import com.tivic.manager.alm.DocumentoEntradaDAO;
import com.tivic.manager.alm.DocumentoEntradaItem;
import com.tivic.manager.alm.DocumentoEntradaItemDAO;
import com.tivic.manager.alm.DocumentoEntradaServices;
import com.tivic.manager.alm.DocumentoSaida;
import com.tivic.manager.alm.DocumentoSaidaDAO;
import com.tivic.manager.alm.DocumentoSaidaItem;
import com.tivic.manager.alm.DocumentoSaidaItemDAO;
import com.tivic.manager.alm.DocumentoSaidaServices;
import com.tivic.manager.alm.EntradaLocalItem;
import com.tivic.manager.alm.EntradaLocalItemDAO;
import com.tivic.manager.alm.GrupoServices;
import com.tivic.manager.alm.LocalArmazenamento;
import com.tivic.manager.alm.LocalArmazenamentoDAO;
import com.tivic.manager.alm.NivelLocal;
import com.tivic.manager.alm.NivelLocalDAO;
import com.tivic.manager.alm.ProdutoReferencia;
import com.tivic.manager.alm.ProdutoReferenciaDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.fsc.MotivoCancelamento;
import com.tivic.manager.fsc.MotivoCancelamentoDAO;
import com.tivic.manager.fsc.NfeServices;
import com.tivic.manager.fsc.NotaFiscal;
import com.tivic.manager.fsc.NotaFiscalDAO;
import com.tivic.manager.fsc.NotaFiscalDocVinculadoDAO;
import com.tivic.manager.fsc.NotaFiscalHistorico;
import com.tivic.manager.fsc.NotaFiscalItemDAO;
import com.tivic.manager.fsc.NotaFiscalItemTributo;
import com.tivic.manager.fsc.NotaFiscalItemTributoDAO;
import com.tivic.manager.fsc.NotaFiscalServices;
import com.tivic.manager.fsc.NotaFiscalTributo;
import com.tivic.manager.fsc.NotaFiscalTributoDAO;
import com.tivic.manager.fsc.SituacaoTributaria;
import com.tivic.manager.fsc.SituacaoTributariaDAO;
import com.tivic.manager.grl.Bairro;
import com.tivic.manager.grl.BairroDAO;
import com.tivic.manager.grl.BairroServices;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Cnae;
import com.tivic.manager.grl.CnaeServices;
import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.EmpresaServices;
import com.tivic.manager.grl.Escolaridade;
import com.tivic.manager.grl.EscolaridadeDAO;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.Formulario;
import com.tivic.manager.grl.FormularioAtributo;
import com.tivic.manager.grl.FormularioAtributoDAO;
import com.tivic.manager.grl.FormularioAtributoOpcao;
import com.tivic.manager.grl.FormularioAtributoOpcaoDAO;
import com.tivic.manager.grl.FormularioAtributoServices;
import com.tivic.manager.grl.FormularioDAO;
import com.tivic.manager.grl.Logradouro;
import com.tivic.manager.grl.LogradouroBairro;
import com.tivic.manager.grl.LogradouroBairroDAO;
import com.tivic.manager.grl.LogradouroBairroServices;
import com.tivic.manager.grl.LogradouroCep;
import com.tivic.manager.grl.LogradouroCepDAO;
import com.tivic.manager.grl.LogradouroCepServices;
import com.tivic.manager.grl.LogradouroDAO;
import com.tivic.manager.grl.LogradouroServices;
import com.tivic.manager.grl.Ncm;
import com.tivic.manager.grl.NcmDAO;
import com.tivic.manager.grl.Pais;
import com.tivic.manager.grl.PaisDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaFisicaServices;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.grl.PessoaTipoDocumentacao;
import com.tivic.manager.grl.PessoaTipoDocumentacaoDAO;
import com.tivic.manager.grl.PessoaTipoDocumentacaoServices;
import com.tivic.manager.grl.ProdutoServico;
import com.tivic.manager.grl.ProdutoServicoDAO;
import com.tivic.manager.grl.ProdutoServicoEmpresa;
import com.tivic.manager.grl.ProdutoServicoEmpresaDAO;
import com.tivic.manager.grl.ProdutoServicoEmpresaServices;
import com.tivic.manager.grl.ProdutoServicoServices;
import com.tivic.manager.grl.Setor;
import com.tivic.manager.grl.SetorDAO;
import com.tivic.manager.grl.TipoLogradouro;
import com.tivic.manager.grl.TipoLogradouroDAO;
import com.tivic.manager.grl.Vinculo;
import com.tivic.manager.grl.VinculoDAO;
import com.tivic.manager.pcb.TanqueServices;
import com.tivic.manager.prc.Processo;
import com.tivic.manager.prc.ProcessoServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoPessoa;
import com.tivic.manager.ptc.DocumentoServices;
import com.tivic.manager.ptc.Fase;
import com.tivic.manager.ptc.FaseDAO;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.seg.CertificadoServices;
import com.tivic.manager.util.MigrationManager;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.ConfManager;
import sol.util.Result;

public class GenericImport {

	/*
	 * IMPORTAï¿½ï¿½O BASIC STORE
	 */
	public static sol.util.Result importPessoaFromFornecedor2(int cdEmpresa)	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = conectarMySQL();
		try {
			/***********************
			 * Importando pessoas
			 ***********************/
			PreparedStatement pesqPessoa  = connect.prepareStatement("SELECT A.* FROM grl_pessoa A " +
					                                                 "LEFT OUTER JOIN grl_pessoa_juridica B ON (A.cd_pessoa = B.cd_pessoa) " +
					                                                 "WHERE id_pessoa = ? OR nm_pessoa = ? OR nr_cnpj = ?");
			PreparedStatement pesqEstado  = connect.prepareStatement("SELECT * FROM grl_estado  WHERE sg_estado = ?");
			PreparedStatement pesqCidade  = connect.prepareStatement("SELECT * FROM grl_cidade  WHERE nm_cidade = ?");
			PreparedStatement pesqVinculo = connect.prepareStatement("SELECT * FROM grl_vinculo WHERE nm_vinculo = ?");
			System.out.println("Importando fornecedores...");
			//
			ResultSet rs = conOrigem.prepareStatement("SELECT * FROM tbfornecedor").executeQuery();
			while(rs.next())	{
				pesqPessoa.setString(1, "FOR"+rs.getString("Controle"));
				pesqPessoa.setString(2, rs.getString("Empresa"));
				pesqPessoa.setString(3, rs.getString("CGC"));
				ResultSet rsTemp = pesqPessoa.executeQuery();
				if(!rsTemp.next())	{
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
					String nrTelefone1 = rs.getString("telefone");
					nrTelefone1        = nrTelefone1!=null ? nrTelefone1.replaceAll("[-]", "").replaceAll("[(]", "").replaceAll("[)]", "") : null;
					String nrFax       = rs.getString("fax");
					nrFax = nrFax!=null ? nrFax.replaceAll("[-]", "").replaceAll("[(]", "").replaceAll("[)]", "") : null;
					String nrCelular   = null;
					String nrCpfCnpj   = rs.getString("cgc");
					nrCpfCnpj          = nrCpfCnpj!=null ? nrCpfCnpj.replaceAll("[ ]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[.]", "") : null;
					// PESSOA Jurï¿½dica
					String nmPessoa = rs.getString("Empresa");
					if(nmPessoa!=null && nmPessoa.length()>50)
						nmPessoa = nmPessoa.substring(0,49);
					String nmFantasia = nmPessoa;
					//
					Pessoa pessoa = new PessoaJuridica(0/*cdPessoa*/,0/*cdPessoaSuperior*/,0/*cdPais*/,nmFantasia,
   							   					nrTelefone1,null/*nrTelefone2*/,nrCelular,nrFax,rs.getString("email"),
   							   					new GregorianCalendar(),
   							   					PessoaServices.TP_JURIDICA,null /*imgFoto*/,1 /*stCadastro*/,
   							   					null/*nmUrl*/,null/*nmApelido*/,null/*txtObservacao*/,
   							   					0/*lgNotificacao*/,"FOR"+rs.getString("Controle")/*idPessoa*/,
   							   					0/*cdClassificacao*/,0/*cdFormaDivulgacao*/,null /*dtChegadaPais*/,
   							   					nrCpfCnpj,nmPessoa, rs.getString("inscricao"), null/*nrInscMunicipal*/,
   							   					0/*nrFuncionarios*/,null/*dtInicioAtividade*/,0/*cdNaturezaJuridica*/,
   							   					0/*tpEmpresa*/,null/*dtTerminoAtividade*/);

					// ENDEREï¿½O
					int cdCidade = 0;
					if(rs.getString("Cidade")!=null)	{
						pesqCidade.setString(1, rs.getString("Cidade")!=null ? rs.getString("Cidade").toUpperCase() : null);
						rsTemp = pesqCidade.executeQuery();
						if(rsTemp.next())
							cdCidade = rsTemp.getInt("cd_cidade");
						else	{
							int cdEstado = 0;
							pesqEstado.setString(1, rs.getString("estado")!=null ? rs.getString("estado").toUpperCase() : "");
							rsTemp = pesqEstado.executeQuery();
							if(rsTemp.next())
								cdEstado = rsTemp.getInt("cd_estado");
							com.tivic.manager.grl.Cidade cidade = new com.tivic.manager.grl.Cidade(0,rs.getString("cidade"),null/*cep*/,0/* vlAltitude*/,
									 0/* vlLatitude*/,0/* vlLongitude*/,cdEstado,
									 null/* idCidade*/,0/* cdRegiao*/,null/* idIbge*/, null, 0, 0);
							cdCidade = com.tivic.manager.grl.CidadeDAO.insert(cidade, connect);
						}
					}
					String nrCep = rs.getString("cep");
					if(nrCep!=null && nrCep.trim().length()>8)
						nrCep = nrCep.trim().substring(0,7);
					PessoaEndereco endereco = new PessoaEndereco(0,0/*cdPessoa*/,rs.getString("endereco"),0/*cdTipoLogradouro*/,0/*cdTipoEndereco*/,
															     0/*cdLogradouro*/,0/*cdBairro*/,cdCidade,rs.getString("endereco"),
															     rs.getString("bairro"),nrCep,
															     null/*nrEndereco*/,null/*complemento*/,
															     nrTelefone1,null/*nmPontoReferencia*/,0/*lgCobranca*/,1/*lgPrincipal*/);
					int cdPessoa = PessoaServices.insert(pessoa, endereco, cdEmpresa, cdVinculo).getCode();
					if(cdPessoa < 0)	{
						System.out.println("Nï¿½o foi possivel incluir["+cdPessoa+"]: "+pessoa);
					}
				}
			}
			System.out.println("Importaï¿½ï¿½o de fornecedores concluï¿½da!");
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

	/*
	 * IMPORTAï¿½ï¿½O BASIC STORE
	 */
	public static sol.util.Result importPessoaFromCliente(int cdEmpresa)	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = conectarMySQL();
		try {
			/***********************
			 * Importando pessoas
			 ***********************/
			PreparedStatement pesqPessoa  = connect.prepareStatement("SELECT A.* FROM grl_pessoa A " +
					                                                 "LEFT OUTER JOIN grl_pessoa_fisica B ON (A.cd_pessoa = B.cd_pessoa) " +
					                                                 "WHERE id_pessoa = ? OR nm_pessoa = ? OR nr_cpf = ? ");
			PreparedStatement pesqEstado  = connect.prepareStatement("SELECT * FROM grl_estado  WHERE sg_estado = ?");
			PreparedStatement pesqCidade  = connect.prepareStatement("SELECT * FROM grl_cidade  WHERE nm_cidade = ?");
			PreparedStatement pesqVinculo = connect.prepareStatement("SELECT * FROM grl_vinculo WHERE nm_vinculo = ?");
			PreparedStatement updPessFis  = connect.prepareStatement("UPDATE grl_pessoa_fisica SET nm_pai = ?, nm_mae = ?, nr_cpf = ? WHERE cd_pessoa = ?");
			PreparedStatement updPessEnd  = connect.prepareStatement("UPDATE grl_pessoa_endereco SET nr_endereco = ? WHERE cd_pessoa = ?");
			System.out.println("Importando clientes ...");
			int cdVinculo = 0;
			pesqVinculo.setString(1, "CLIENTE");
			ResultSet rsTemp = pesqVinculo.executeQuery();
			if(!rsTemp.next())	{
				Vinculo vinculo = new Vinculo(0,"CLIENTE",1 /*lgEstatico*/, 0 /*lgFuncao*/, 0 /*cdFormulario*/, 1 /*lgCadastro*/);
				cdVinculo = VinculoDAO.insert(vinculo, connect);
			}
			else
				cdVinculo = rsTemp.getInt("cd_vinculo");
			//
			ResultSet rs = conOrigem.prepareStatement("SELECT * FROM tbclientes").executeQuery();
			int qtRegistros    = 0;
			int qtJaCadastrado = 0;
			int qtIncluido     = 0;
			while(rs.next())	{
				String nrCpfCnpj   = (rs.getString("cgc")!=null ? rs.getString("cgc") :  (rs.getString("cpf")!=null ? rs.getString("cpf") : ""));
				nrCpfCnpj          = nrCpfCnpj!=null ? nrCpfCnpj.replaceAll("[ ]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[.]", "") : null;
				//
				qtRegistros++;
				pesqPessoa.setString(1, "CLI_"+rs.getString("Controle"));
				pesqPessoa.setString(2, rs.getString("NOME"));
				pesqPessoa.setString(3, rs.getString("CPF"));
				rsTemp = pesqPessoa.executeQuery();
				if(rsTemp.next())	{
					int cdPessoa = rsTemp.getInt("cd_pessoa");
					qtJaCadastrado++;
					// Inclui vï¿½nculo
					rsTemp = connect.prepareStatement("SELECT * FROM grl_pessoa_empresa " +
							                          "WHERE cd_empresa = "+cdEmpresa+
							                          "  AND cd_vinculo = "+cdVinculo+
							                          "  AND cd_pessoa  = "+cdPessoa).executeQuery();
					if(!rsTemp.next())	{
						connect.prepareStatement("INSERT INTO grl_pessoa_empresa (cd_empresa,cd_vinculo,cd_pessoa) " +
		                                         " VALUES ("+cdEmpresa+","+cdVinculo+","+cdPessoa+")").executeUpdate();
					}
					// Atualiza CPF / CNPJ e Nï¿½mero do Endereï¿½o
					if(rs.getString("Pessoa")==null || rs.getString("Pessoa").equals("Fisica")) {
						updPessFis.setString(1, rs.getString("pai"));
						updPessFis.setString(2, rs.getString("mae"));
						updPessFis.setString(3, nrCpfCnpj);
						updPessFis.setInt(4, cdPessoa);
						updPessFis.executeUpdate();
					}
					// 
					updPessEnd.setString(1, rs.getString("numero"));
					updPessEnd.setInt(2, cdPessoa);
					updPessEnd.executeUpdate();
					continue;
				}
				//
				int tpSexo = rs.getString("Sexo")!=null && rs.getString("Sexo").equalsIgnoreCase("Feminino") ? 1 : 0;
				String nrTelefone1 = rs.getString("telefone");
				nrTelefone1        = nrTelefone1!=null ? nrTelefone1.replaceAll("[-]", "").replaceAll("[(]", "").replaceAll("[)]", "") : null;
				String nrFax       = rs.getString("fax");
				nrFax = nrFax!=null ? nrFax.replaceAll("[-]", "").replaceAll("[(]", "").replaceAll("[)]", "") : null;
				// PESSOA Jurï¿½dica
				String nmPessoa = rs.getString("Nome");
				if(nmPessoa!=null && nmPessoa.length()>50)
					nmPessoa = nmPessoa.substring(0,49);
				Pessoa pessoa = null;
				if (nrCpfCnpj==null || nrCpfCnpj.length()<=11) {
						String nrRg        = rs.getString("Identidade");
						pessoa = new PessoaFisica(0/*cdPessoa*/,0/*cdPessoaSuperior*/,0/*cdPais*/,nmPessoa,
						   nrTelefone1,nrFax,null/*nrCelular*/,nrFax,rs.getString("internet"),
						   Util.convTimestampToCalendar(rs.getTimestamp("datacadastro")),
						   PessoaServices.TP_FISICA,null /*imgFoto*/,1 /*stCadastro*/,
						   null/*homepage*/,null/*nmApelido*/,null/*observacao*/,
						   0/*lgNotificacao*/,"CLI_"+rs.getString("Controle")/*idPessoa*/,
						   0/*cdClassificacao*/,0/*cdFormaDivulgacao*/,null /*dtChegadaPais*/,
						   0/*cdNaturalidade*/, 0 /*cdEscolaridade*/,
						   Util.convTimestampToCalendar(rs.getTimestamp("Data")),
						   nrCpfCnpj, "SSP"/*orgao rg*/, rs.getString("mae"),
						   rs.getString("pai"),tpSexo,0/*stEstadoCivil*/,
						   nrRg,null/*nrCnh*/,null/*dtValidadeCnh*/,
						   null/*dtPrimeiraHabilitacao*/,0/*tpCategoriaHabilitacao*/,0/*tpRaca*/,
						   0/*lgDeficienteFisico*/,null/*nmFormaTratamento*/,0/*cdEstadoRg*/,null/*dtEmissaoRg*/,
						   null /*blbFingerprint*/);
				}
				else	{
					pessoa = new PessoaJuridica(0/*cdPessoa*/,0/*cdPessoaSuperior*/,0/*cdPais*/,nmPessoa,
			   					                nrTelefone1,null/*nrTelefone2*/,null/*nrCelular*/,nrFax,null,
			   					                new GregorianCalendar(),
			   					                PessoaServices.TP_JURIDICA,null /*imgFoto*/,1 /*stCadastro*/,
			   					                null/*nmUrl*/,null/*nmApelido*/,null/*txtObservacao*/,
			   					                0/*lgNotificacao*/,"CLI_"+rs.getString("Controle")/*idPessoa*/,
			   					                0/*cdClassificacao*/,0/*cdFormaDivulgacao*/,null /*dtChegadaPais*/,
			   					                nrCpfCnpj,nmPessoa,rs.getString("inscricao"), null/*nrInscMunicipal*/,
			   					                0/*nrFuncionarios*/,null/*dtInicioAtividade*/,0/*cdNaturezaJuridica*/,
			   					                0/*tpEmpresa*/,null/*dtTerminoAtividade*/);
				}

				// ENDEREï¿½O
				int cdCidade = 0;
				if(rs.getString("Cidade")!=null)	{
					if(rsTemp!=null)
						rsTemp.close();
					pesqCidade.setString(1, rs.getString("Cidade")!=null ? rs.getString("Cidade").toUpperCase() : null);
					rsTemp = pesqCidade.executeQuery();
					if(rsTemp.next())
						cdCidade = rsTemp.getInt("cd_cidade");
					else	{
						int cdEstado = 0;
						pesqEstado.setString(1, rs.getString("estado")!=null ? rs.getString("estado").toUpperCase() : "");
						rsTemp = pesqEstado.executeQuery();
						if(rsTemp.next())
							cdEstado = rsTemp.getInt("cd_estado");
						com.tivic.manager.grl.Cidade cidade = new com.tivic.manager.grl.Cidade(0,rs.getString("cidade"),null/*cep*/,0/* vlAltitude*/,
								 0/* vlLatitude*/,0/* vlLongitude*/,cdEstado,
								 null/* idCidade*/,0/* cdRegiao*/,null/* idIbge*/, null, 0, 0);
						cdCidade = com.tivic.manager.grl.CidadeDAO.insert(cidade, connect);
					}
					
				}
				if(cdCidade < 0)
					cdCidade = 0;
				String nrEndereco = rs.getString("numero");
				String nrCep = rs.getString("cep");
				if(nrCep!=null && nrCep.trim().length()>8)
					nrCep = nrCep.trim().substring(0,7);
				String dsEndereco = rs.getString("endereco");
				if(dsEndereco!=null && dsEndereco.trim().length()>50)
					dsEndereco = dsEndereco.trim().substring(0,49);
				PessoaEndereco endereco = new PessoaEndereco(0,0/*cdPessoa*/,dsEndereco,0/*cdTipoLogradouro*/,0/*cdTipoEndereco*/,
														     0/*cdLogradouro*/,0/*cdBairro*/,cdCidade,dsEndereco,
														     rs.getString("bairro"),nrCep,
														     nrEndereco,null/*complemento*/,
														     nrTelefone1,null/*nmPontoReferencia*/,0/*lgCobranca*/,1/*lgPrincipal*/);
				int cdPessoa = PessoaServices.insert(pessoa, endereco, cdEmpresa, cdVinculo).getCode();
				if(cdPessoa < 0)
					System.out.println("Nï¿½o foi possivel incluir["+cdPessoa+"]: "+pessoa);
				qtIncluido++;
			}
			System.out.println("Importaï¿½ï¿½o de clientes concluï¿½da! [Registros: "+qtRegistros+", Incluidos: "+qtIncluido+", qtJacadastrados: "+qtJaCadastrado);
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

	/*
	 * IMPORTAï¿½ï¿½O BASIC STORE
	 */
	public static sol.util.Result importContaReceber(int cdEmpresa)	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = conectarMySQL();
		try {
			/***********************
			 * Importando Contas a receber
			 ***********************/
			PreparedStatement pesqContaReceber = connect.prepareStatement("SELECT * FROM adm_conta_receber " +
					                                                      "WHERE cd_empresa = " +cdEmpresa+
					                                                      "  AND id_conta_receber = ?");
			PreparedStatement pesqPessoa  = connect.prepareStatement("SELECT * FROM grl_pessoa WHERE id_pessoa = ?");
			PreparedStatement pesqPessoa2  = connect.prepareStatement("SELECT A.* FROM grl_pessoa A " +
                    												  "LEFT OUTER JOIN grl_pessoa_fisica B ON (A.cd_pessoa = B.cd_pessoa) " +
                    												  "WHERE nm_pessoa = ? OR nr_cpf = ? ");
			// Tipo Documento
			ResultSet rsTemp = connect.prepareStatement("SELECT * FROM adm_tipo_documento WHERE nm_tipo_documento LIKE \'%FAT%\'").executeQuery();
			int cdTipoDocumento  = 0;
			if(rsTemp.next())
				cdTipoDocumento = rsTemp.getInt("cd_tipo_documento");
			// Conta e Carteira
			rsTemp = connect.prepareStatement("SELECT * FROM adm_conta_financeira A, adm_conta_carteira B " +
					                          "WHERE A.cd_conta = B.cd_conta AND tp_conta = 0" +
					                          "  AND A.cd_empresa = "+cdEmpresa).executeQuery();
			int cdConta          = 0;
			int cdContaCarteira  = 0;
			if(rsTemp.next())	{
				cdConta 		= rsTemp.getInt("cd_conta");
				cdContaCarteira = rsTemp.getInt("cd_conta_carteira");
			}
			// Categoria
			int cdCategoriaSaida = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_SAIDAS_DEFAULT", 0, cdEmpresa);
			if(cdCategoriaSaida <= 0)
				return new Result(-1, "Vocï¿½ deve informar a categoria padrï¿½o para saï¿½das!");
			System.out.println("Importando contas a receber SORAIA BRANDï¿½O...");
			//
			ResultSet rs = conOrigem.prepareStatement("SELECT A.*, B.NOME, B.CPF FROM tcontasr A, tbclientes B " +
					                                  "WHERE A.Cliente = B.Controle").executeQuery();
			while(rs.next())	{
				pesqContaReceber.setString(1, "CR"+rs.getString("Controle"));
				rsTemp = pesqContaReceber.executeQuery();
				if(!rsTemp.next())	{
					// Pesquisando pessoa
					pesqPessoa.setString(1, "CLI_"+rs.getString("Cliente"));
					rsTemp = pesqPessoa.executeQuery();
					if(!rsTemp.next())	{
						pesqPessoa2.setString(1, rs.getString("NOME"));
						pesqPessoa2.setString(2, rs.getString("CPF"));
						rsTemp = pesqPessoa2.executeQuery();
						if(!rsTemp.next())	{
							System.out.println("Cliente nï¿½o localizado: "+rs.getString("Cliente")+", Nome: "+rs.getString("NOME"));
							continue;
						}
					}
					int cdPessoa = rsTemp.getInt("cd_pessoa");
					int stConta  = rs.getTimestamp("Quitacao")!=null ? 1/*Recebida*/ : 0/*Em Aberto*/;
					ArrayList<ContaReceberCategoria> categorias = new ArrayList<ContaReceberCategoria>();
					categorias.add(new ContaReceberCategoria(0,cdCategoriaSaida,rs.getFloat("Valor"), 0));
					//
					ContaReceber contaReceber = new ContaReceber(0/*cdContaReceber*/,cdPessoa,cdEmpresa,0/*cdContrato*/,0/*cd_conta_origem*/,0/*cd_documento_saida*/,
							                                     cdContaCarteira,cdConta,0/*cd_frete*/,rs.getString("NumTitulo"),"CR"+rs.getString("Controle"),
							                                     rs.getInt("Parcela"),""/*nr_referencia*/,cdTipoDocumento,rs.getString("Historico"),
							                                     (rs.getTimestamp("Vencimento")==null)?null:Util.longToCalendar(rs.getTimestamp("Vencimento").getTime()),
							                                     (rs.getTimestamp("Emissao")==null)?null:Util.longToCalendar(rs.getTimestamp("Emissao").getTime()),
							                                     (rs.getTimestamp("Quitacao")==null)?null:Util.longToCalendar(rs.getTimestamp("Quitacao").getTime()),
							                                     null/*dt_prorrogacao*/,rs.getDouble("Valor"),rs.getDouble("Desconto"),rs.getDouble("Juros"),
							                                     stConta==1?rs.getDouble("Valor") + rs.getDouble("Juros") - rs.getDouble("Desconto") : 0,
							                                     stConta,0/*tp_frequencia*/,0/*qt_parcelas*/,0/*tp_conta_receber*/,0/*cd_negociacao*/,
							                                     null/*txtObservacao*/,0/*cd_plano_pagamento*/,0/*cd_forma_pagamento*/,
							                                     new GregorianCalendar(),
							                                    (rs.getTimestamp("Vencimento")==null)?null:Util.longToCalendar(rs.getTimestamp("Vencimento").getTime()), 0/*cdTurno*/, 0.0d/*prJuros*/, 0.0d/*prMulta*/, 0/*lgProtesto*/);
					Result result = ContaReceberServices.insert(contaReceber, categorias, null/*tituloCredito*/, true, connect);
					if(result.getCode() < 0)
						System.out.println("Nï¿½o foi possivel incluir["+result.getCode()+"]: "+result.getMessage()+"\n"+contaReceber);

				}
			}
			System.out.println("Importaï¿½ï¿½o de clientes concluï¿½da!");
			return new sol.util.Result(1);
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar pessoas!", e);
		}
		finally	{
			Conexao.desconectar(connect);
			Conexao.desconectar(conOrigem);
		}
	}

	public static sol.util.Result updatePreco(int cdEmpresa, int cdTabelaPreco1, int cdTabelaPreco2)	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = conectarMySQL();
		try {
			PreparedStatement pesqProduto = conOrigem.prepareStatement("SELECT PrecoVenda, PrecoPrazo, CustoMedio, PrecoCusto " +
					                                                   "FROM tbestoque WHERE codigo = ? " +
					                                                   "ORDER BY precovenda DESC");

			PreparedStatement updPreco  = connect.prepareStatement("UPDATE grl_produto_servico_empresa " +
					                                               "SET qt_ideal = ?, vl_custo_medio = ?, vl_preco_medio=?, vl_ultimo_custo=? " +
					                                               "WHERE cd_produto_servico = ? " +
					                                               "  AND cd_empresa         = "+cdEmpresa);
			//
			ResultSet rs = connect.prepareStatement("SELECT * FROM grl_produto_servico_empresa B ").executeQuery();
			int updates = 0, notfounds = 0;
			while(rs.next()){
				String nrReferencia = rs.getString("id_reduzido")!=null ? rs.getString("id_reduzido").toUpperCase() : "";
				pesqProduto.setString(1, nrReferencia);
				ResultSet rsProd = pesqProduto.executeQuery();
				if(rsProd.next())	{
					float vlPrecoAtacado = rsProd.getFloat("PrecoVenda");
					float vlPrecoVarejo  = rsProd.getFloat("PrecoPrazo");
					updates++;
					// System.out.println("Atualizando: Atacado: "+vlPrecoAtacado+" [ID: "+nrReferencia+"]");
					// Atualizando
					updPreco.setFloat(1, rsProd.getFloat("PrecoVenda"));
					updPreco.setFloat(2, rsProd.getFloat("CustoMedio"));
					updPreco.setFloat(3, rsProd.getFloat("PrecoCusto"));
					updPreco.setFloat(4, rsProd.getFloat("PrecoVenda"));
					updPreco.setInt(5, rs.getInt("cd_produto_servico"));
					updPreco.executeUpdate();
					//
					ProdutoServicoPreco preco1 = new ProdutoServicoPreco(cdTabelaPreco1,rs.getInt("cd_produto_servico"),1,null,vlPrecoAtacado);
					ProdutoServicoPreco preco2 = new ProdutoServicoPreco(cdTabelaPreco2,rs.getInt("cd_produto_servico"),1,null,vlPrecoVarejo);
					ProdutoServicoPrecoServices.insert(preco1, connect);
					ProdutoServicoPrecoServices.insert(preco2, connect);
				}
				else	{
					System.out.println("Produto nï¿½o localizado! "+nrReferencia);
					notfounds++;
				}
			}
			return new sol.util.Result(1, "Updates: "+updates+", Nï¿½o Localizados: "+notfounds);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar atualizar preï¿½o!", e);
		}
		finally	{
			Conexao.desconectar(connect);
			Conexao.desconectar(conOrigem);
		}
	}
	
    public static boolean isNumeric (String s) {  
        try {  
        	s = s.replaceAll(",", ".");
        	Double.parseDouble(s);   
            return true;  
        } catch (NumberFormatException ex) {  
            return false;  
        }  
    }  

    public static sol.util.Result importClientesFromTxtCSV()	{
		Connection connect   = Conexao.conectar();
		RandomAccessFile raf = null;
		try {
			/***********************
			 * Importando clientes
			 ***********************/
			
			PreparedStatement pesqSgEstado  		= connect.prepareStatement("SELECT * FROM grl_estado WHERE sg_estado = ?");
			PreparedStatement pesqNmCidade  		= connect.prepareStatement("SELECT * FROM grl_cidade WHERE nm_cidade = ?");
			PreparedStatement pesqNmLogradouro  	= connect.prepareStatement("SELECT * FROM grl_tipo_logradouro WHERE nm_tipo_logradouro = ?");
			
			System.out.println("Importando clientes de arquivo CSV...");
			//
			ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile("d:/CLIENTES2.csv", ";", true);
	  		System.out.println("Arquivo carregado...");
			connect.setAutoCommit(false);
			int l = 0;
			int inserts = 0, updates = 0;
			while(rsm.next())	{
	  			if((l%500==0) && l>0)	{
					System.out.println(l+" registros gravados. [inserts:"+inserts+",updates:"+updates+"]");
					connect.commit();
					connect.close();
					System.gc();
					connect = Conexao.conectar();
					connect.setAutoCommit(false);
					pesqSgEstado  	   = connect.prepareStatement("SELECT * FROM grl_estado WHERE sg_estado = ?");
					pesqNmCidade  	   = connect.prepareStatement("SELECT * FROM grl_cidade WHERE nm_cidade = ?");
					pesqNmLogradouro   = connect.prepareStatement("SELECT * FROM grl_tipo_logradouro WHERE nm_tipo_logradouro = ?");
				}
	  			
	  			String nome = rsm.getString("NOME").toUpperCase();
				String rg = rsm.getString("IE");
				String cpfAux = rsm.getString("CGC");
				String cpf_cnpj = "";
				if(cpfAux != null && cpfAux.length() > 1){
					for(int i = 0; i < cpfAux.length(); i++){
						if(!cpfAux.substring(i, i + 1).equals("-") && !cpfAux.substring(i, i + 1).equals(".") && !cpfAux.substring(i, i + 1).equals("/"))
							cpf_cnpj += cpfAux.substring(i, i + 1);
					}
				}
				String tipo = rsm.getString("TIPO").toUpperCase();
				String endereco = rsm.getString("ENDERE").toUpperCase();
				String numero = rsm.getString("NUMERO");
				String complemento = rsm.getString("CMPL").toUpperCase();
				String complemento2 = rsm.getString("COMPLE").toUpperCase();
				String cidade = rsm.getString("CIDADE").toUpperCase();
				String estado = rsm.getString("ESTADO").toUpperCase();
				String cepAux = rsm.getString("CEP");
				String cep = "";
				if(cepAux != null && cepAux.length() > 1){
					for(int i = 0; i < cepAux.length(); i++){
						if(!cepAux.substring(i, i + 1).equals("-") && !cepAux.substring(i, i + 1).equals("."))
							cep += cepAux.substring(i, i + 1);
					}
				}
				String telefone = rsm.getString("FONE");
				String fax = rsm.getString("FAX");
				String email = rsm.getString("EMAIL");
				String ie = rsm.getString("IE");
				String im = rsm.getString("IM");
				String observacao = rsm.getString("OBS");
				String celular = rsm.getString("CELULAR");
				String dataNascimento = rsm.getString("DATANAS");
				String cadastro = rsm.getString("CADASTRO");
	  			
				Pessoa pessoa = new Pessoa();
				
				if (cpf_cnpj.length() < 14)	{
						
						pessoa = new PessoaFisica(0/*cdPessoa*/,0/*cdPessoaSuperior*/,0/*cdPais*/,nome,
								   telefone,null,celular,fax /*nrFax*/,email,
	   							   Util.convStringToCalendar(cadastro),
	   							   PessoaServices.TP_FISICA,null /*imgFoto*/,1 /*stCadastro*/,
	   							   null,null/*nmApelido*/,observacao/*observacao*/,
	   							   0/*lgNotificacao*/,null/*idPessoa*/,
	   							   0/*cdClassificacao*/,0/*cdFormaDivulgacao*/,null /*dtChegadaPais*/,
	   							   0, 0 /*cdEscolaridade*/,
	   							   Util.convStringToCalendar(dataNascimento) /*dtNascimento*/,
	   							   cpf_cnpj,"SSP"/*orgao rg*/,null /*nmMae*/,
	   							   null/*nmPai*/,0/*tpSexo*/,0/*stEstadoCivil*/,
	   							   rg,null/*nrCnh*/,null/*dtValidadeCnh*/,
	   							   null/*dtPrimeiraHabilitacao*/,0/*tpCategoriaHabilitacao*/,0/*tpRaca*/,
	   							   0/*lgDeficienteFisico*/,null/*nmFormaTratamento*/,0,null/*dtEmissaoRg*/,
	   							   null /*blbFingerprint*/);
				}
				
				else{
					
						String nmFantasia = nome;
	
						pessoa = new PessoaJuridica(0/*cdPessoa*/,0/*cdPessoaSuperior*/,0/*cdPais*/,nmFantasia,
	   							   telefone,null,celular,null /*nrFax*/,email,
	   							   Util.convStringToCalendar(cadastro),
	   							   PessoaServices.TP_JURIDICA,null /*imgFoto*/,1 /*stCadastro*/,
	   							   null/*nmUrl*/,null/*nmApelido*/,observacao/*txtObservacao*/,
	   							   0/*lgNotificacao*/,null/*idPessoa*/,
	   							   0/*cdClassificacao*/,0/*cdFormaDivulgacao*/,null /*dtChegadaPais*/,
	   							   cpf_cnpj,nmFantasia, ie, im,
	   							   0/*nrFuncionarios*/,null/*dtInicioAtividade*/,0/*cdNaturezaJuridica*/,
	   							   0/*tpEmpresa*/,null/*dtTerminoAtividade*/);
					
				}
				
				int cdEstado = 0;
				pesqSgEstado.setString(1, estado);
				ResultSet rsE = pesqSgEstado.executeQuery();
				if(rsE.next())
					cdEstado = rsE.getInt("cd_estado");
				
				int cdCidade = 0;
				pesqNmCidade.setString(1, cidade);
				ResultSet rsC = pesqNmCidade.executeQuery();
				if(rsC.next()){
					cdCidade = rsC.getInt("cd_cidade");
					
				}
				else{
					cdCidade = com.tivic.manager.grl.CidadeDAO.insert(new Cidade(0, cidade, cep, 0, 0, 0, cdEstado, null, 0, null, null, 0, 0), connect);
					if(cdCidade <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar o cidade!");
					}
				}
					
				int cdTipoLogradouro = 0;
				pesqNmLogradouro.setString(1, tipo);
				ResultSet rsL = pesqNmLogradouro.executeQuery();
				if(rsL.next())
					cdTipoLogradouro = rsL.getInt("cd_tipo_logradouro");
				else{
					cdTipoLogradouro = com.tivic.manager.grl.TipoLogradouroDAO.insert(new TipoLogradouro(0, tipo, tipo), connect);
					if(cdTipoLogradouro <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar o Logradouro!");
					}
				}
				
				PessoaEndereco pessoaEndereco = new PessoaEndereco(0,0/*cdPessoa*/,(endereco.length() > 50) ? endereco.substring(1, 49) : endereco,cdTipoLogradouro/*cdTipoLogradouro*/,0/*cdTipoEndereco*/,
																   0/*cdLogradouro*/,0/*cdBairro*/,cdCidade,endereco,
														           null,cep,
														           numero, ((complemento + ", " + complemento2).length() > 50) ? (complemento + ", " + complemento2).substring(1, 49) : (complemento + ", " + complemento2),
												        		   telefone,null/*nmPontoReferencia*/,0/*lgCobranca*/,1/*lgPrincipal*/);
						
				
				PessoaServices.insert(pessoa, pessoaEndereco, 0, 0, connect).getCode();
					
			}
			connect.commit();
			System.out.println("Importaï¿½ï¿½o de pessoas concluï¿½da!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar produtos!", e);
		}
		finally{
			if(raf!=null)
		  		try {raf.close(); } catch(Exception e){};
			Conexao.desconectar(connect);
		}
	}
    
    public static sol.util.Result importAdmNCMFromCSV(int cdEmpresa)	{
		Connection connect   = Conexao.conectar();
		RandomAccessFile raf = null;
		try {
			/***********************
			 * Importando NCM
			 ***********************/
			
			System.out.println("Importando ncm de arquivo CSV...");
			//
			ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile("/resources/NCMCSV.csv", ";", true);
			System.out.println("rsm = " +rsm);
	  		System.out.println("Arquivo carregado...");
			connect.setAutoCommit(false);
			PreparedStatement pstmtAdmNcm = connect.prepareStatement("SELECT * FROM adm_ncm WHERE cd_ncm = ? and cd_empresa = ? and cd_classificacao_fiscal = ?");
  			int cdClassificacaoFiscal = 3;
			int lines = 0;
			while(rsm.next())	{
	  			String nrNcm = rsm.getString("NCM").toUpperCase();
	  			nrNcm = Util.limparFormatos(nrNcm);
	  			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_ncm WHERE nr_ncm LIKE '"+nrNcm+"%'");
	  			ResultSetMap rsmNcm = new ResultSetMap(pstmt.executeQuery());
	  			System.out.println("Numero = " + nrNcm);
	  			System.out.println("rsmNcm = " + rsmNcm);
	  			System.out.println();
	  			while(rsmNcm.next()){
	  				int cdNcm = rsmNcm.getInt("cd_ncm");
	  				pstmtAdmNcm.setInt(1, cdNcm);
	  				pstmtAdmNcm.setInt(2, cdEmpresa);
	  				pstmtAdmNcm.setInt(3, cdClassificacaoFiscal);
	  				ResultSetMap rsmAdmNcm = new ResultSetMap(pstmtAdmNcm.executeQuery());
	  				if(!rsmAdmNcm.next()){
	  					PreparedStatement pstmtInsert = connect.prepareStatement("INSERT INTO adm_ncm (cd_ncm, cd_empresa, cd_classificacao_fiscal) VALUES ("+cdNcm+", "+cdEmpresa+", "+cdClassificacaoFiscal+")");
	  					if(pstmtInsert.executeUpdate() <= 0){
	  						Conexao.rollback(connect);
	  						return new Result(-1, "Erro ao inserir adm_ncm");
	  					}
	  					else{
	  						lines++;
	  					}
	  				}
	  			}
	  			
			}
			connect.commit();
			System.out.println("Importaï¿½ï¿½o de ncm concluï¿½da - "+lines+" registros inseridos!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar produtos!", e);
		}
		finally{
			if(raf!=null)
		  		try {raf.close(); } catch(Exception e){};
			Conexao.desconectar(connect);
		}
	}
    //
    public static sol.util.Result importNCMFromCSV(String url)	{
    	RandomAccessFile raf = null;
		
		/***********************
		 * Importando NCM
		 ***********************/
		
		System.out.println("Importando ncm de arquivo CSV...");
		//
		ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile(url, ";", true);
		//System.out.println("rsm = " +rsm);
  		System.out.println("Arquivo carregado...");
//		connect.setAutoCommit(false);			
		int lines = 0;
		while(rsm.next())	{
			Connection connect   = Conexao.conectar();
			try {
			
//				System.out.println("rsm.getRegister = " + rsm.getRegister());
				if(rsm.getString("nm_ncm") != null && !rsm.getString("nm_ncm").equals("") &&
				   rsm.getString("nr_ncm") != null && !rsm.getString("nr_ncm").equals("")){
//				String sgUnidadeMedida = rsm.getString("nm_sigla");
					String nmNCM           = rsm.getString("nm_ncm").substring(0, (rsm.getString("nm_ncm").length() > 200 ? 200 : rsm.getString("nm_ncm").length()));
					
					String nrNCM           = rsm.getString("nr_ncm").replaceAll("\\.", "");
//					System.out.println("nrNCM = " + nrNCM);
//				PreparedStatement pstmt = connect.prepareStatement("SELECT cd_unidade_medida FROM grl_unidade_medida " +
//																   " WHERE sg_unidade_medida = \'" + sgUnidadeMedida.toUpperCase()+ "\'");
//	  			ResultSetMap rsmUnidadeMedida = new ResultSetMap(pstmt.executeQuery());
//	  			pstmt.close();
//	  			while(rsmUnidadeMedida.next()){
//					System.out.println("nrNCM = " + nrNCM);
//	  				int cdNCM = 0;
//	  				int    cdUnidadeMedida = rsmUnidadeMedida.getInt("cd_unidade_medida");
	  				PreparedStatement pstmt     = connect.prepareStatement("SELECT * FROM grl_ncm WHERE nr_ncm = \'" + nrNCM + "\'");
	  				ResultSetMap rsmNCM = new ResultSetMap(pstmt.executeQuery());
//	  				System.out.println("ncm = " + ncm);
	  				if (!rsmNCM.next()){
	  					Ncm ncm   = new Ncm(lines++, nmNCM.trim(), 0, nrNCM);
	  					System.out.println("nmNCM = " + nmNCM + ", nrNCM = " + nrNCM);
	  					NcmDAO.insert(ncm);
	  				}
//  					if(cdNCM <= 0){
//  						Conexao.rollback(connect);
//  						return new Result(-1, "Erro ao inserir grl_ncm");
//  					}	  				
//	  			}
	  			}
			
			}
			catch(Exception e){
				e.printStackTrace(System.out);
				return new sol.util.Result(-1, "Erro ao tentar importar produtos!", e);
			}
			finally{
				if(raf!=null)
			  		try {raf.close(); } catch(Exception e){};
				Conexao.desconectar(connect);
			}
		}
		
//		rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_ncm").executeQuery());
//		System.out.println("rsm = " + rsm);
		
//		connect.commit();
		System.out.println("Importaï¿½ï¿½o de ncm concluï¿½da - "+lines+" registros inseridos!");
		return new sol.util.Result(1);
    }
	public static sol.util.Result importNCMFromCSV()	{
		return importNCMFromCSV(ParametroServices.getValorOfParametro("NM_PATH_PRINCIPAL") + "/resources/NCMCSV.csv");
	}
    /**
     * Atualiza tabela grl_ncm com valores de aliquotas referentes ao 
     * IBPTax 0.0.2 - Download 09/06/2014
     * @param nrNcm             numero do ncm no arquivo CSV
     * @param vlNcmNacional     valor da aliquota de produtos nacionais no arquivo CSV
     * @param vlNcmImportado    valor da aliquota de importados no arquivo CSV
     * @param cdNcm             codigo do ncm existente no banco de dados
     * @author Joao Marlon Souto Ferraz
     * @return 
     */
    public static sol.util.Result updateNCMFromCSVIBPtax()	{
		Connection connect   = Conexao.conectar();
		RandomAccessFile raf = null;
		try {
			/*******************************************************
			 * Importando valor do tributo IBPTax segundo NCM 0.0.2
			 *******************************************************/
			System.out.println("Importando ncm de arquivo CSV...");
			//
			ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile("C:/TIVIC/DATABASES/IBPTax.0.0.2.csv", ";", true);
			System.out.println("rsm = " +rsm);
	  		System.out.println("Arquivo carregado...");
			connect.setAutoCommit(false);
			int lines = 0;
			while(rsm.next())	{
	  			String nrNcm 		  = rsm.getString("codigo");
	  			String vlNcmNacional  = rsm.getString("aliqNac");
	  			String vlNcmImportado = rsm.getString("aliqImp");
	  			nrNcm = Util.limparFormatos(nrNcm);
	  			nrNcm = Util.fill(nrNcm, 8, '0', 'E');
	  			System.out.println("NCM = " + nrNcm);
	  			// Verifica se existe numero do NCM no banco
	  			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_ncm WHERE nr_ncm LIKE \'"+nrNcm+"\'");
	  			ResultSetMap rsmCdNcm = new ResultSetMap(pstmt.executeQuery());
	  			pstmt.close();
	  			while (rsmCdNcm.next()){
	  				int cdNcm  = rsmCdNcm.getInt("cd_ncm");
	  				int result = connect.prepareStatement("UPDATE grl_ncm SET (vl_aliquota_nacional, vl_aliquota_importado) = ("+
		  						  					  	  Double.valueOf(vlNcmNacional)+","+ Double.valueOf(vlNcmImportado) + ") " +
		  						  					  	  " WHERE cd_ncm = "+cdNcm).executeUpdate();
					if(result < 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar tabela de NCM's");
					}else
						lines++;
		  			//
	  			}
			}
			//
			connect.commit();
			System.out.println("Atualizacao de Tabela do NCM de acordo com a IBPTax foi concluï¿½da concluï¿½da - "+lines+" registros atualizados!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar Atualizar NCM!", e);
		}
		finally{
			if(raf!=null)
		  		try {raf.close(); } catch(Exception e){};
			Conexao.desconectar(connect);
		}
	}
    
    public static sol.util.Result importarTributos(){
		Connection connect   = Conexao.conectar();
		RandomAccessFile raf = null;
		try {
			/***********************
			 * Importando clientes
			 ***********************/
			
			System.out.println("Importando tributos...");
			//
			connect.setAutoCommit(false);
			
			int result = connect.prepareStatement("DELETE FROM fsc_nota_fiscal_item_tributo").executeUpdate();
			if(result < 0){
				Conexao.rollback(connect);
				return new Result(-1, "Erro ao deletar tabela de nota fiscal tributo item");
			}
			HashMap<Integer, Float> hashNT = new HashMap<Integer, Float>(); 
			ResultSetMap rsmNotaTributo = new ResultSetMap(connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_tributo").executeQuery());
			while(rsmNotaTributo.next()){
				hashNT.put(rsmNotaTributo.getInt("cd_nota_fiscal"), rsmNotaTributo.getFloat("pr_desconto"));
			}
			
			result = connect.prepareStatement("DELETE FROM fsc_nota_fiscal_tributo").executeUpdate();
			if(result < 0){
				Conexao.rollback(connect);
				return new Result(-1, "Erro ao deletar tabela de nota fiscal tributo");
			}
			
			PreparedStatement pstmtNotaFiscal = connect.prepareStatement("SELECT cd_nota_fiscal, nr_nota_fiscal, cd_empresa, vl_total_nota FROM fsc_nota_fiscal");
			ResultSetMap rsmNotaFiscal = new ResultSetMap(pstmtNotaFiscal.executeQuery());
			while(rsmNotaFiscal.next()){
				float vlBaseRetencaoTotal = 0;
				float prDesconto = 0;
				float vlDocEntrada = 0;
				float vlDocSaida   = 0;
				prDesconto = hashNT.get(rsmNotaFiscal.getInt("cd_nota_fiscal"));
				//
				PreparedStatement pstmtDocEntrada = connect.prepareStatement("SELECT A.cd_documento_entrada, B.vl_total_documento " +
																				"	FROM fsc_nota_fiscal_doc_vinculado A " +
																				"	JOIN alm_documento_entrada B ON (A.cd_documento_entrada = B.cd_documento_entrada) " +
																				"	WHERE A.cd_nota_fiscal = ? AND A.cd_documento_entrada > 0");
				pstmtDocEntrada.setInt(1, rsmNotaFiscal.getInt("cd_nota_fiscal"));
				ResultSetMap rsmDocEntrada = new ResultSetMap(pstmtDocEntrada.executeQuery());
				ArrayList<NotaFiscalItemTributo> notaIT = new ArrayList<NotaFiscalItemTributo>();
				while(rsmDocEntrada.next()){
					PreparedStatement pstmtDocEntradaItem = connect.prepareStatement("SELECT * FROM alm_documento_entrada_item WHERE cd_documento_entrada = ? AND cd_empresa = ?");
					pstmtDocEntradaItem.setInt(1, rsmDocEntrada.getInt("cd_documento_entrada"));
					pstmtDocEntradaItem.setInt(2, rsmNotaFiscal.getInt("cd_empresa"));
					vlDocEntrada = rsmDocEntrada.getFloat("vl_total_documento");
					ResultSetMap rsmDocEntradaItem = new ResultSetMap(pstmtDocEntradaItem.executeQuery());
					while(rsmDocEntradaItem.next()){
						PreparedStatement pstmtDocEntradaItemAliquota = connect.prepareStatement("SELECT A.*, B.tp_origem, C.tp_base_calculo, C.cd_situacao_tributaria AS cd_situacao_tributaria_original, C.st_tributaria " +
																								"	FROM adm_entrada_item_aliquota A " +
																								"	JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico = B.cd_produto_servico " +
																								"											AND B.cd_empresa = A.cd_empresa) " +
																								"	JOIN adm_tributo_aliquota C ON (A.cd_tributo = C.cd_tributo " +
																								"										AND A.cd_tributo_aliquota = C.cd_tributo_aliquota) " +
																								"	WHERE cd_documento_entrada = ? AND A.cd_empresa = ? AND A.cd_produto_servico = ? AND cd_item = ?");
						ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_produto_servico", "" + rsmDocEntradaItem.getInt("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_nota_fiscal", "" + rsmNotaFiscal.getString("cd_nota_fiscal"), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsm = NotaFiscalItemDAO.find(criterios, connect);
						while(rsm.next()){
							pstmtDocEntradaItemAliquota.setInt(1, rsmDocEntradaItem.getInt("cd_documento_entrada"));
							pstmtDocEntradaItemAliquota.setInt(2, rsmDocEntradaItem.getInt("cd_empresa"));
							pstmtDocEntradaItemAliquota.setInt(3, rsmDocEntradaItem.getInt("cd_produto_servico"));
							pstmtDocEntradaItemAliquota.setInt(4, rsm.getInt("cd_item"));
							ResultSetMap rsmDocEntradaItemAliquota = new ResultSetMap(pstmtDocEntradaItemAliquota.executeQuery());
							while(rsmDocEntradaItemAliquota.next()){
								int tpRegime = ParametroServices.getValorOfParametroAsInteger("TP_REGIME_TRIBUTARIO", 0);
								if(tpRegime == 0){
									Conexao.rollback(connect);
									return new Result(-1, "Regime Tributï¿½rio nï¿½o configurado!");
								}
								
								
								float vlBaseRetencao = 0;
								SituacaoTributaria situacaoTrib = SituacaoTributariaDAO.get(8, rsmDocEntradaItemAliquota.getInt("cd_situacao_tributaria_original"));
								if(situacaoTrib!=null && situacaoTrib.getLgSubstituicao()==1){
									float prDescontoNovo = 0;
	//								if(prDesconto == 0 && ((rsmNotaFiscal.getFloat("vl_total_nota") / vlDocEntrada) > 0)){
	//									prDescontoNovo = 100 - (rsmNotaFiscal.getFloat("vl_total_nota") / vlDocEntrada) * 100;
	//								}
	//								else
									
										prDescontoNovo = 100 - prDesconto;
									
									vlBaseRetencao = (rsm.getFloat("vl_unitario") * rsm.getFloat("qt_tributario") + rsm.getFloat("vl_acrescimo") - rsm.getFloat("vl_desconto"));
								}
								vlBaseRetencaoTotal += vlBaseRetencao;
								
								if(rsm.next()){
									NotaFiscalItemTributo notaItemTributo = new NotaFiscalItemTributo(rsmNotaFiscal.getInt("cd_nota_fiscal"), rsm.getInt("cd_item"), 8, rsmDocEntradaItemAliquota.getInt("cd_tributo_aliquota"), tpRegime, rsmDocEntradaItemAliquota.getInt("tp_origem"), rsmDocEntradaItemAliquota.getInt("tp_base_calculo"), rsmDocEntradaItemAliquota.getFloat("vl_base_calculo"), 0/*vlOutrasDespesas*/, 0/*vlOutrosImpostos*/, rsmDocEntradaItemAliquota.getFloat("pr_aliquota"), 0/*vlTributo*/, 0/*prCredito*/, 0/*vlCredito*/, null/*nrClasse*/, null/*nrEnquadramento*/, rsmDocEntradaItemAliquota.getInt("cd_situacao_tributaria"), vlBaseRetencao, 0/*vlRetido*/);
									int indice = inArrayNotaFiscalItemTributo(notaItemTributo, notaIT);
									if(indice == 0)
										notaIT.add(notaItemTributo);
									else{
										notaIT.get(indice).setVlBaseCalculo(notaIT.get(indice).getVlBaseCalculo() + notaItemTributo.getVlBaseCalculo());
										notaIT.get(indice).setVlBaseRetencao(notaIT.get(indice).getVlBaseRetencao() + notaItemTributo.getVlBaseRetencao());
									}
								}
							}
						}
					}
				}
				//
				PreparedStatement pstmtDocSaida = connect.prepareStatement("SELECT A.cd_documento_saida, B.vl_total_documento " +
																			"	FROM fsc_nota_fiscal_doc_vinculado A " +
																			"	JOIN alm_documento_saida B ON (A.cd_documento_saida = B.cd_documento_saida) " +
																			"	WHERE A.cd_nota_fiscal = ? AND A.cd_documento_saida > 0");
				pstmtDocSaida.setInt(1, rsmNotaFiscal.getInt("cd_nota_fiscal"));
				ResultSetMap rsmDocSaida = new ResultSetMap(pstmtDocSaida.executeQuery());
				while(rsmDocSaida.next()){
					PreparedStatement pstmtDocSaidaItem = connect.prepareStatement("SELECT * FROM alm_documento_saida_item WHERE cd_documento_saida = ? AND cd_empresa = ?");
					pstmtDocSaidaItem.setInt(1, rsmDocSaida.getInt("cd_documento_saida"));
					pstmtDocSaidaItem.setInt(2, rsmNotaFiscal.getInt("cd_empresa"));
					vlDocSaida = rsmDocSaida.getFloat("vl_total_documento");
					ResultSetMap rsmDocSaidaItem = new ResultSetMap(pstmtDocSaidaItem.executeQuery());
					rsmDocSaidaItem.beforeFirst();
					while(rsmDocSaidaItem.next()){
						PreparedStatement pstmtDocSaidaItemAliquota = connect.prepareStatement("SELECT A.*, B.tp_origem, C.tp_base_calculo, C.cd_situacao_tributaria AS cd_situacao_tributaria_original, C.st_tributaria " +
																								"	FROM adm_saida_item_aliquota A " +
																								"	JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico = B.cd_produto_servico " +
																								"											AND B.cd_empresa = A.cd_empresa) " +
																								"	JOIN adm_tributo_aliquota C ON (A.cd_tributo = C.cd_tributo " +
																								"										AND A.cd_tributo_aliquota = C.cd_tributo_aliquota) " +
																								"	WHERE A.cd_documento_saida = ? AND A.cd_empresa = ? AND A.cd_produto_servico = ? AND A.cd_item = ?");
						ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_produto_servico", "" + rsmDocSaidaItem.getInt("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_nota_fiscal", "" + rsmNotaFiscal.getString("cd_nota_fiscal"), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsm = NotaFiscalItemDAO.find(criterios, connect);
						while(rsm.next()){
							pstmtDocSaidaItemAliquota.setInt(1, rsmDocSaidaItem.getInt("cd_documento_saida"));
							pstmtDocSaidaItemAliquota.setInt(2, rsmDocSaidaItem.getInt("cd_empresa"));
							pstmtDocSaidaItemAliquota.setInt(3, rsmDocSaidaItem.getInt("cd_produto_servico"));
							pstmtDocSaidaItemAliquota.setInt(4, rsm.getInt("cd_item"));
							ResultSetMap rsmDocSaidaItemAliquota = new ResultSetMap(pstmtDocSaidaItemAliquota.executeQuery());
							rsmDocSaidaItemAliquota.beforeFirst();
							while(rsmDocSaidaItemAliquota.next()){
								
								int tpRegime = ParametroServices.getValorOfParametroAsInteger("TP_REGIME_TRIBUTARIO", 0);
								if(tpRegime == 0){
									Conexao.rollback(connect);
									return new Result(-1, "Regime Tributï¿½rio nï¿½o configurado!");
								}
								
								
								float vlBaseRetencao = 0;
								SituacaoTributaria situacaoTrib = SituacaoTributariaDAO.get(8, rsmDocSaidaItemAliquota.getInt("cd_situacao_tributaria_original"));
								System.out.println("situacaoTrib =" + situacaoTrib);
								if(situacaoTrib!=null && situacaoTrib.getLgSubstituicao()==1){
									float prDescontoNovo = 0;
	//								if(prDesconto == 0 && ((rsmNotaFiscal.getFloat("vl_total_nota") / vlDocSaida) > 0)){
	//									prDescontoNovo = 100 - (rsmNotaFiscal.getFloat("vl_total_nota") / vlDocSaida) * 100;
	//								}
	//								else
									prDescontoNovo = 100 - prDesconto;
									System.out.println("vlUnitario = " + rsm.getFloat("vl_unitario"));
									System.out.println("qtTributario = " + rsm.getFloat("qt_tributario"));
									vlBaseRetencao = (rsm.getFloat("vl_unitario") * rsm.getFloat("qt_tributario") + rsm.getFloat("vl_acrescimo") - rsm.getFloat("vl_desconto"));
									System.out.println("vlBaseRetencao = " + vlBaseRetencao);
								}
								vlBaseRetencaoTotal += vlBaseRetencao;
								NotaFiscalItemTributo notaItemTributo = new NotaFiscalItemTributo(rsmNotaFiscal.getInt("cd_nota_fiscal"), rsm.getInt("cd_item"), 8, rsmDocSaidaItemAliquota.getInt("cd_tributo_aliquota"), tpRegime, rsmDocSaidaItemAliquota.getInt("tp_origem"), rsmDocSaidaItemAliquota.getInt("tp_base_calculo"), rsmDocSaidaItemAliquota.getFloat("vl_base_calculo"), 0/*vlOutrasDespesas*/, 0/*vlOutrosImpostos*/, rsmDocSaidaItemAliquota.getFloat("pr_aliquota"), 0/*vlTributo*/, 0/*prCredito*/, 0/*vlCredito*/, null/*nrClasse*/, null/*nrEnquadramento*/, rsmDocSaidaItemAliquota.getInt("cd_situacao_tributaria"), vlBaseRetencao, 0/*vlRetido*/);
								int indice = inArrayNotaFiscalItemTributo(notaItemTributo, notaIT);
								System.out.println("indice = " + indice);
								if(indice == 0){
									notaIT.add(notaItemTributo);
									System.out.println("notaItemTributo = " + notaItemTributo);	
								}
								else{
									notaIT.get(indice).setVlBaseCalculo(notaIT.get(indice).getVlBaseCalculo() + notaItemTributo.getVlBaseCalculo());
									notaIT.get(indice).setVlBaseRetencao(notaIT.get(indice).getVlBaseRetencao() + notaItemTributo.getVlBaseRetencao());
									System.out.println("notaItemTributoJATEM = " + notaIT.get(indice));	
								}									
								
							}
						}
					}
				}
				
				NotaFiscalTributo notaTributo = new NotaFiscalTributo(rsmNotaFiscal.getInt("cd_nota_fiscal"), 8, 0/*vlBaseCalculo*/, 0/*vlOutrasDespesas*/, 0/*vlOutrosImpostos*/, 0/*vlTributo*/, vlBaseRetencaoTotal, 0/*vlRetido*/);
				if(NotaFiscalTributoDAO.insert(notaTributo, connect) < 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir NotaFiscalTributo!");
				}
				
				for(int i = 0; i < notaIT.size(); i++){
					if(NotaFiscalItemTributoDAO.insert(notaIT.get(i), connect) < 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao inserir Nota Fiscal Item Tributo");
					}
				}
			}
			
			connect.commit();
			System.out.println("Importaï¿½ï¿½o de tributos concluï¿½da!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar tributos!", e);
		}
		finally{
			if(raf!=null)
		  		try {raf.close(); } catch(Exception e){};
			Conexao.desconectar(connect);
		}
	}
    
    public static int inArrayNotaFiscalItemTributo(NotaFiscalItemTributo elem, ArrayList<NotaFiscalItemTributo> lista){
		 for(int i = 0; i < lista.size(); i++){
			 if(lista.get(i).getCdItem() == elem.getCdItem() 
				&& lista.get(i).getCdNotaFiscal() == elem.getCdNotaFiscal())
				 return i;
		 }
		 return 0;
	 }
    
	/*
	 * IMPORTAï¿½ï¿½O POLIMEDIC
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
					// PESSOA Fï¿½SICA
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
					// ENDEREï¿½O
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
						com.tivic.manager.grl.Cidade cidade = new com.tivic.manager.grl.Cidade(0,rsPessoa.getString("MUNICIPIO"),null/*cep*/,0/* vlAltitude*/,
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
						System.out.println("Nï¿½o foi possivel incluir["+cdPessoa+"]: "+pessoa);
				}
			}
			System.out.println("Importaï¿½ï¿½o de pessoas concluï¿½da!");
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
			return DriverManager.getConnection("jdbc:firebirdsql://127.0.0.1:3050/C:/TIVIC/LOJA.GDB", "SYSDBA","masterkey");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public static Connection conectarFirebird(String url)	{
		try{
			Class.forName("org.firebirdsql.jdbc.FBDriver").newInstance();
	  		DriverManager.setLoginTimeout(80);
			return DriverManager.getConnection("jdbc:firebirdsql:"+url, "SYSDBA","masterkey");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public static Connection conectarServer() {
		try	{
			Class.forName("org.postgresql.Driver").newInstance();
	  		DriverManager.setLoginTimeout(80);
	  		return DriverManager.getConnection("jdbc:postgresql://127.0.0.1/managerInit",
	  										   "postgres",
	  										   "t1v1k!");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public static int getNextCodeServer(String table, Connection connection) {
		try {
			String sql = "SELECT (cd_generator + 1) FROM grl_generator " +
						 "WHERE nm_generator = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, table.toLowerCase());
			ResultSet rs = pstmt.executeQuery();
			int codigo = rs.next() ? rs.getInt(1) : 0;
			if (codigo>0) {
				pstmt = connection.prepareStatement("UPDATE grl_generator " +
										     "SET cd_generator = ? " +
										     "WHERE nm_generator = ?");
				pstmt.setInt(1, codigo);
				pstmt.setString(2, table.toLowerCase());
				pstmt.execute();
			}
			else {
				codigo = 1;
				ArrayList<HashMap<String, Object>> keys = Conexao.getPrimaryKeysNames(table);
				if (keys != null && keys.size() == 1 && ((Boolean)keys.get(0).get("IS_NATIVE_KEY")).booleanValue()) {
					rs = connection.prepareStatement("SELECT MAX(" + keys.get(0).get("NM_KEY") + ") " +
							                         "FROM " + table).executeQuery();
					codigo = rs.next() ? rs.getInt(1) + 1 : 1;
				}
				if(pstmt!=null)
					pstmt.close();
				pstmt = connection.prepareStatement("INSERT INTO GRL_GENERATOR (nm_generator, cd_generator) VALUES(?, ?)");
				pstmt.setString(1, table.toLowerCase());
				pstmt.setInt(2, codigo);
				pstmt.execute();
			}
			return codigo;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
	}
	
	public static int getNextCode(String table, HashMap<String,Object>[] primaryKeys, Connection connection) {
		try {
			if (primaryKeys==null || primaryKeys.length==0)
				return getNextCodeServer(table, (Connection)null);
			else {
				String sql = "SELECT MAX(";
				for (int i=0; i<primaryKeys.length; i++)
					if (primaryKeys[i].get("IS_KEY_NATIVE")!=null && primaryKeys[i].get("IS_KEY_NATIVE").equals("YES")) {
						sql += primaryKeys[i].get("FIELD_NAME") + ") FROM " + table + " WHERE";
						break;
					}
				int countOfForeignKeys = 0;
				for (int i=0; i<primaryKeys.length; i++)
					if (primaryKeys[i].get("IS_KEY_NATIVE")==null || !primaryKeys[i].get("IS_KEY_NATIVE").equals("YES")) {
						sql += (countOfForeignKeys!=0 ? " AND " : " ") + primaryKeys[i].get("FIELD_NAME") + " = " + primaryKeys[i].get("FIELD_VALUE");
						countOfForeignKeys++;
					}
				PreparedStatement pstmt = connection.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();
				return !rs.next() ? 1 : rs.getInt(1) + 1;
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
	}
	
	public static Connection conectarSqlServer(String databaseName)	{
		try{
			Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
	  		DriverManager.setLoginTimeout(80);
			return DriverManager.getConnection("jdbc:jtds:sqlserver://127.0.0.1:1433/"+databaseName, "tivic","t1v1k!");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public static Connection conectarMySQL()	{
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
	  		DriverManager.setLoginTimeout(80);
			return DriverManager.getConnection("jdbc:mysql://192.168.1.50/sigecom", "root","sql");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public static Connection conectarSisDispo()	{
		try{
			Class.forName("org.firebirdsql.jdbc.FBDriver").newInstance();
	  		DriverManager.setLoginTimeout(80);
			return DriverManager.getConnection("jdbc:firebirdsql://127.0.0.1:3050//projetos/documents/manager/Imobiliï¿½rio/sisdispo/BASE.GDB", "sysdba","masterkey");
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

	public static Connection conectarProimo()	{
		try{
			Class.forName("org.firebirdsql.jdbc.FBDriver").newInstance();
	  		DriverManager.setLoginTimeout(80);
			return DriverManager.getConnection("jdbc:firebirdsql://127.0.0.1:3050//proimo/BASE.GDB", "sysdba","masterkey");
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
					// PESSOA Fï¿½SICA
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
					// ENDEREï¿½O
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
						System.out.println("Nï¿½o foi possivel incluir["+cdPessoa+"]: "+pessoa);
				}
			}
			System.out.println("Importaï¿½ï¿½o de pessoas concluï¿½da!");
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
		Connection conOrigem = conectarSisDispo();
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
					// PESSOA Fï¿½SICA
					String nmPessoa = rsPessoa.getString("NOMCLI");
					if(nmPessoa!=null && nmPessoa.length()>50)
						nmPessoa = nmPessoa.substring(0,49);
					if(true /*Somente pessoa fï¿½sica*/)	{
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
					// ENDEREï¿½O
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
					if(cdPessoa < 0)
						System.out.println("Nï¿½o foi possivel incluir["+cdPessoa+"]: "+pessoa);
				}
			}
			System.out.println("Importaï¿½ï¿½o de pessoas concluï¿½da!");
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

	public static sol.util.Result importClientesFromProimo(int cdEmpresa)	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = conectarProimo();
		try {
			/***********************
			 * Importando pessoas
			 ***********************/
			PreparedStatement pesqPessoa  = connect.prepareStatement("SELECT * FROM grl_pessoa_fisica WHERE nr_cpf = ?");
			PreparedStatement pesqEstado  = connect.prepareStatement("SELECT * FROM grl_estado WHERE sg_estado = ?");
			PreparedStatement pesqCidade  = connect.prepareStatement("SELECT * FROM grl_cidade WHERE nm_cidade = ?");
			PreparedStatement pesqVinculo = connect.prepareStatement("SELECT * FROM grl_vinculo WHERE nm_vinculo = ?");
			System.out.println("Importando pessoas...");
			//
			ResultSet rsPessoa = conOrigem.prepareStatement("SELECT * FROM ficha").executeQuery();
			while(rsPessoa.next())	{
				pesqPessoa.setString(1, rsPessoa.getString("CPF"));
				ResultSet rsTemp = pesqPessoa.executeQuery();
				if(!rsTemp.next())	{
					// Verificando CPF
					int cdVinculo = 0;
					pesqVinculo.setString(1, "CLIENTE");
					rsTemp = pesqVinculo.executeQuery();
					if(!rsTemp.next())	{
						Vinculo vinculo = new Vinculo(0,"CLIENTE",1 /*lgEstatico*/, 0 /*lgFuncao*/, 0 /*cdFormulario*/, 1 /*lgCadastro*/);
						cdVinculo = VinculoDAO.insert(vinculo, connect);
					}
					else
						cdVinculo = rsTemp.getInt("cd_vinculo");
					// Telefone
					String nrTelefone1 = rsPessoa.getString("FONERESPRO");
					nrTelefone1 = nrTelefone1!=null ? nrTelefone1.replaceAll("[-]", "").replaceAll("[(]", "").replaceAll("[)]", "") : null;
					// Telefone 2
					String nrTelefone2 = rsPessoa.getString("FONEREC");
					nrTelefone2 = nrTelefone2!=null ? nrTelefone2.replaceAll("[-]", "").replaceAll("[(]", "").replaceAll("[)]", "") : null;
					String nrFax       = null;
					// Celular
					String nrCelular   = rsPessoa.getString("CELPROPRIO");
					// CPF
					String nrCpfCnpj   = rsPessoa.getString("CPF");
					nrCpfCnpj = nrCpfCnpj!=null ? nrCpfCnpj.replaceAll("[ ]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[.]", "") : null;
					// RG
					String nrRg   = rsPessoa.getString("NRID");
					if(nrRg!=null && nrRg.length()>15)
						nrRg = nrRg.trim().substring(0,14);

					Pessoa pessoa;
					// PESSOA Fï¿½SICA
					String nmPessoa = rsPessoa.getString("NOME");
					if(nmPessoa!=null && nmPessoa.length()>50)
						nmPessoa = nmPessoa.substring(0,49);
					if(true /*Somente pessoa fï¿½sica*/)	{
						if(nrCpfCnpj!=null && nrCpfCnpj.length()>14)
							nrCpfCnpj = nrCpfCnpj.trim().substring(0,13);
						// Cidade - Naturalidade
						int cdNaturalidade = 0;
						pesqCidade.setString(1, rsPessoa.getString("NATCIDA"));
						rsTemp = pesqCidade.executeQuery();
						if(rsTemp.next())
							cdNaturalidade = rsTemp.getInt("cd_cidade");
						else	{
							// Estado da cidade de naturalidade
							int cdEstado = 0;
							pesqEstado.setString(1, rsPessoa.getString("NATUF"));
							rsTemp = pesqEstado.executeQuery();
							if(rsTemp.next())
								cdEstado = rsTemp.getInt("cd_estado");
							Cidade cidade = new Cidade(0,rsPessoa.getString("NATCIDA"),null,9,0,0,cdEstado,null,0,null, null, 0, 0);
							cdNaturalidade = CidadeDAO.insert(cidade, connect);
						}
						// Escolaridade
						int cdEscolaridade = rsPessoa.getInt("GRAUINST");
						Escolaridade escol = EscolaridadeDAO.get(cdEscolaridade, connect);
						if(escol==null)	{
							connect.prepareStatement("INSERT INTO grl_escolaridade (cd_escolaridade, nm_escolaridade) VALUES ("+cdEscolaridade+",\'ESCOLARIDADE "+cdEscolaridade+"\')").execute();
						}
						// Estado - RG
						int cdEstadoRg     = 0;
						//
						int tpSexo = rsPessoa.getString("SEXO")!=null && !rsPessoa.getString("SEXO").trim().equals("") ? Integer.parseInt(rsPessoa.getString("SEXO").trim())-1 : -1;
						int stEstadoCivil = rsPessoa.getString("ESCIVIL")!=null && !rsPessoa.getString("ESCIVIL").trim().equals("") ? Integer.parseInt(rsPessoa.getString("ESCIVIL").trim()) : -1;
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
						GregorianCalendar dtEmissaoRg = Util.convTimestampToCalendar(rsPessoa.getTimestamp("DTEMISID"));
						// Estado do RG
						pesqEstado.setString(1, rsPessoa.getString("UFID"));
						rsTemp = pesqEstado.executeQuery();
						if(rsTemp.next())
							cdEstadoRg = rsTemp.getInt("cd_estado");

						pessoa = new PessoaFisica(0/*cdPessoa*/,0/*cdPessoaSuperior*/,0/*cdPais*/,nmPessoa,
								   							   nrTelefone1,nrTelefone2,nrCelular,nrFax,rsPessoa.getString("email"),
								   							   new GregorianCalendar(),
								   							   PessoaServices.TP_FISICA,null /*imgFoto*/,1 /*stCadastro*/,
								   							   null/*homepage*/,null/*nmApelido*/,null/*observacao*/,
								   							   0/*lgNotificacao*/,rsPessoa.getString("CPF")/*idPessoa*/,
								   							   0/*cdClassificacao*/,0/*cdFormaDivulgacao*/,null /*dtChegadaPais*/,
								   							   cdNaturalidade, cdEscolaridade, dtNascimento,
								   							   nrCpfCnpj,rsPessoa.getString("ORGID")/*orgao rg*/,rsPessoa.getString("MAE"),
								   							   rsPessoa.getString("PAI"),tpSexo,stEstadoCivil,
								   							   nrRg,null/*nrCnh*/,null/*dtValidadeCnh*/,
								   							   null/*dtPrimeiraHabilitacao*/,0/*tpCategoriaHabilitacao*/,0/*tpRaca*/,
								   							   0/*lgDeficienteFisico*/,null/*nmFormaTratamento*/,cdEstadoRg,dtEmissaoRg,
								   							   null /*blbFingerprint*/);
					}
					// ENDEREï¿½O
					String dsEndereco = rsPessoa.getString("ENDE");
					if(dsEndereco!=null && dsEndereco.length()>50)
						dsEndereco = dsEndereco.substring(0,49);
					int cdCidade = 0;
					pesqCidade.setString(1, rsPessoa.getString("MUNICIPIO")!=null ? rsPessoa.getString("MUNICIPIO").toUpperCase() : null);
					rsTemp = pesqCidade.executeQuery();
					if(rsTemp.next())
						cdCidade = rsTemp.getInt("cd_cidade");
					else	{
						int cdEstado = 0;
						pesqEstado.setString(1, rsPessoa.getString("UF")!=null ? rsPessoa.getString("UF").toUpperCase() : "");
						rsTemp = pesqEstado.executeQuery();
						if(rsTemp.next())
							cdEstado = rsTemp.getInt("cd_estado");
						com.tivic.manager.grl.Cidade cidade = new com.tivic.manager.grl.Cidade(0,rsPessoa.getString("MUNICIPIO"),null/*cep*/,0/* vlAltitude*/,
																				 0/* vlLatitude*/,0/* vlLongitude*/,cdEstado,
																				 null/* idCidade*/,0/* cdRegiao*/,null/* idIbge*/, null, 0, 0);
						cdCidade = com.tivic.manager.grl.CidadeDAO.insert(cidade, connect);
					}
					String nrCep = rsPessoa.getString("cep");
					if(nrCep!=null && nrCep.trim().length()>8)
						nrCep = nrCep.trim().substring(0,7);
					PessoaEndereco endereco = new PessoaEndereco(0,0/*cdPessoa*/,dsEndereco,0/*cdTipoLogradouro*/,0/*cdTipoEndereco*/,
															     0/*cdLogradouro*/,0/*cdBairro*/,cdCidade,dsEndereco,
															     rsPessoa.getString("bairro"),nrCep, rsPessoa.getString("NRO"),rsPessoa.getString("COMPLEMENTO"),
															     nrTelefone1,null/*nmPontoReferencia*/,0/*lgCobranca*/,1/*lgPrincipal*/);
					int cdPessoa = PessoaServices.insert(pessoa, endereco, cdEmpresa, cdVinculo).getCode();
					if(cdPessoa  < 0)
						System.out.println("Nï¿½o foi possivel incluir["+cdPessoa+"]: "+pessoa);
				}
			}
			System.out.println("Importaï¿½ï¿½o de pessoas concluï¿½da!");
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
				NivelLocal nivelLocal = new NivelLocal(0,nmNivelLocal,0/*lgArmazena*/,0/*lgSetor*/,cdNivelSuperior);
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

	public static sol.util.Result importUnidadesFromProimo(int cdEmpresa)	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = conectarProimo();
		try {
			/***********************
			 * Importando Unidades
			 ***********************/
			PreparedStatement pesqProduto = connect.prepareStatement("SELECT * FROM grl_produto_servico WHERE id_produto_servico = ?");
			PreparedStatement pesqLocal   = connect.prepareStatement("SELECT * FROM alm_local_armazenamento WHERE id_local_armazenamento = ?");
			PreparedStatement pesqReferencia = connect.prepareStatement("SELECT * FROM alm_produto_referencia WHERE cd_produto_servico = ? AND id_referencia = ?");
			PreparedStatement pesqPessoa  = connect.prepareStatement("SELECT * FROM grl_pessoa_fisica WHERE nr_cpf = ?");
			// Verificando tipo de documento
			int cdTipoDocumento = 0;
			ResultSet rs = connect.prepareStatement("SELECT * FROM ptc_tipo_documento WHERE nm_tipo_documento = \'CONTRATO\'").executeQuery();
			if(!rs.next())
				cdTipoDocumento = com.tivic.manager.ptc.TipoDocumentoDAO.insert(new com.tivic.manager.ptc.TipoDocumento(0, "CONTRATO", null, com.tivic.manager.ptc.TipoDocumentoServices.TP_INC_COM_ANO,
						0, 0, 0, 0, null, null, 0, 0, 0, null), connect);
			else
				cdTipoDocumento = rs.getInt("cd_tipo_documento");
			// Verificando SETOR
			int cdSetor         = 0;
			rs = connect.prepareStatement("SELECT * FROM grl_setor WHERE nm_setor = \'DOCUMENTAï¿½ï¿½O\' AND cd_empresa = "+cdEmpresa).executeQuery();
			if(!rs.next())
				cdSetor = SetorDAO.insert(new Setor(0,0,cdEmpresa,0,"DOCUMENTAï¿½ï¿½O",1,null,null,null,null,null,null,null,0,null,"DOC",null,0,0,null), connect);
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
			int cdProdutoServico = 0;
			// Produto / Serviï¿½o
			String dsTUnidade = "DESC";
			pesqProduto.setString(1, "IMB_"+dsTUnidade);
			ResultSet rsTemp = pesqProduto.executeQuery();
			if(!rsTemp.next())	{
				ProdutoServico prodServ = new ProdutoServico(0,0,"DESCONHECIDO",null,null,null,null,
																	ProdutoServicoServices.TP_PRODUTO,"IMB_"+dsTUnidade,
																	dsTUnidade,0,0,0,null, 0/*cdNcm*/, null/*NrReferencia*/);
				cdProdutoServico = ProdutoServicoDAO.insert(prodServ, connect);
				ProdutoServicoEmpresa produtoEmpresa = new ProdutoServicoEmpresa(cdEmpresa,cdProdutoServico,0,null,(float)0,(float)0,(float)0,(float)0,(float)0,(float)0,(float)0,
						                                                          0,0,0,0, ProdutoServicoEmpresaServices.CTL_INDIVIDUAL,0,1,null,null,0);
				ProdutoServicoEmpresaDAO.insert(produtoEmpresa, connect);
			}
			else
				cdProdutoServico = rsTemp.getInt("cd_produto_servico");
			// Unidades
			ResultSet rsUnidades = conOrigem.prepareStatement("SELECT A.*, SUBSTRING(un_unid from 1 for 1) AS andar, B.cod_cor, C.cor_nome, " +
					                                          "       dataven, valorven "+
					                                          "FROM unidades A " +
					                                          "LEFT OUTER JOIN vendas     B ON (A.un_bloco = B.anda_emp " +
					                                          "                             AND A.un_unid = B.uni_emp) " +
					                                          "LEFT OUTER JOIN corretores C ON (B.cod_cor = C.cor_cod) ").executeQuery();
			while(rsUnidades.next())	{
				int cdLocalArmazenamento = 0;
				// Pesquisando local
				pesqLocal.setString(1, rsUnidades.getString("UN_BLOCO")+"-"+rsUnidades.getString("ANDAR"));
				rsTemp = pesqLocal.executeQuery();
				if(rsTemp.next())
					cdLocalArmazenamento = rsTemp.getInt("cd_local_armazenamento");
				else
					return new sol.util.Result(-1, rsUnidades.getString("UN_BLOCO")+"-"+rsUnidades.getString("ANDAR")+" Nï¿½o localizado.");
				//
				String idReferencia = rsUnidades.getString("un_unid")+"_"+rsUnidades.getString("un_desc");
				int cdReferencia = 0;
				pesqReferencia.setInt(1, cdProdutoServico);
				pesqReferencia.setString(2, idReferencia);
				rsTemp = pesqReferencia.executeQuery();
				if(!rsTemp.next())	{
					String nmReferencia = rsUnidades.getString("un_unid");
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
					return new sol.util.Result(-1, "Referï¿½ncia nï¿½o localizada!");
				/*
				 * REGISTRANDO O CONTRATO
				 */
				if(rsUnidades.getString("UN_CPFCOMP")!="" && rsUnidades.getString("UN_CPFCOMP")!=null)	{
					// Pesquisando o cliente
					int cdCliente  = 0;
					pesqPessoa.setString(1, rsUnidades.getString("UN_CPFCOMP"));
					rsTemp = pesqPessoa.executeQuery();
					if(rsTemp.next())
						cdCliente  = rsTemp.getInt("cd_pessoa");
					else	{
						System.out.println("Cliente nï¿½o localizado: "+rsUnidades.getString("un_nomecomp"));
						continue;
					}
					// Pesquisando Agente / Corretor
					int cdAgente = 0;
					pesqPessoa.setString(1, "CRT_"+rsUnidades.getString("COD_COR"));
					rsTemp = pesqPessoa.executeQuery();
					if(rsTemp.next())
						cdAgente  = rsTemp.getInt("cd_pessoa");
					else	{
						PessoaFisica pessoa = new PessoaFisica(0,0,0,rsUnidades.getString("COR_NOME"),null,null,null,null ,null, new GregorianCalendar(),
	   							                               PessoaServices.TP_FISICA,null,1/*stCadastro*/, null,rsUnidades.getString("COR_NOME"),null,
	   							                               0,"CRT_"+rsUnidades.getString("COD_COR"),0,0,null,0,0,null,null,null,null,
	   							                               null,0,0,null,null,null,null,0,0,0,null,0,null,null);
						cdAgente = PessoaFisicaDAO.insert(pessoa, connect);
					}
					/* Pesquisando USUARIO
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
					*/
					// Informaï¿½ï¿½es do Contrato
					GregorianCalendar dtAssinatura = null, dtPrimeiraParcela = null;
					int nrDiaVencimento            = 0;
					float vlContrato               = 0;
					dtAssinatura      = Util.convTimestampToCalendar(rsUnidades.getTimestamp("dataven"));
					dtPrimeiraParcela = Util.convTimestampToCalendar(rsUnidades.getTimestamp("dataven"));
					nrDiaVencimento   = dtAssinatura!=null ? dtAssinatura.get(Calendar.DAY_OF_MONTH) : 1;
					vlContrato        = rsUnidades.getFloat("valorven");

					int nrParcelas   = 0;
					float vlParcelas = 0;
					float vlAdesao   = 0;
					int gnContrato = ContratoServices.gnCOMPRA_VENDA, tpContrato = ContratoServices.tpCONTRATADA;
					//
					int cdUsuario     = 0;
					int cdDocumento   = 0;
					String nrContrato = "", idContrato = idReferencia;
					// Documento
					String nmLocalOrigem = "";
					String idDocumento   = idContrato;
					Documento documento  = new Documento(0,0,cdSetor,cdUsuario,nmLocalOrigem,dtAssinatura,DocumentoServices.TP_PUBLICO,null/*txtObservacao*/,
														 idDocumento,null/*nrDocumento*/,cdTipoDocumento,0,0/*cdAtendimento*/,"Unidade nï¿½ "+rsUnidades.getString("un_unid"),
														 cdSetor,0/*cdSituacaoAtual*/,0/*cdFase*/,cdEmpresa, 0/*cdProcesso*/, 0 /*tpPrioridade*/, 0/*cdDocumentoSuperior*/, 
														 null /*dsAssunto*/, null /*nrAtendimento*/, 0/*lgNotificacao*/, 0 /*cdTipoDocumentoAnterior*/, null/*nrDocumentoExterno*/,
														 null/*nrAssunto*/,null, null, 0, 1);
					ArrayList<DocumentoPessoa> solicitantes = new ArrayList<DocumentoPessoa>();
					solicitantes.add(new DocumentoPessoa(0, cdCliente, "Cliente"));
					cdDocumento = DocumentoServices.insert(documento, solicitantes).getCode();
					// INCLUINDO O CONTRATO
					Contrato contrato = new Contrato(0,0,0,cdEmpresa,cdCliente,0,0,dtAssinatura,dtPrimeiraParcela,nrDiaVencimento,nrParcelas,
							                         0,0,0,0, tpContrato, vlParcelas, vlAdesao, vlContrato, nrContrato,null,1/*stContrato*/,
							                         idContrato, null /*dtInicioVigencia*/, null /*dtFinalVigencia*/, cdAgente, 0, 0, 0,
							                         gnContrato, 0, cdTipoOperacao, cdDocumento, 0, 0, 0, null, 0);
					int cdContrato = ContratoDAO.insert(contrato, connect);
					// Produto / Serviï¿½o
					ContratoProdutoServico crtProd = new ContratoProdutoServico(cdContrato,cdProdutoServico,1,vlContrato,dtAssinatura,0,0,cdReferencia,cdEmpresa);

					ContratoProdutoServicoDAO.insert(crtProd, connect);
				}
			}
			System.out.println("Importaï¿½ï¿½o de unidades concluï¿½da!");
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

	public static sol.util.Result importEmpreendimentoFromProimo(int cdEmpresa)	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = conectarProimo();
		try {
			if(cdEmpresa <= 0)
				return new sol.util.Result(-1, "Empresa nï¿½o informada!");
			/***********************
			 * Importando locais de armazenamento
			 ***********************/
			PreparedStatement pesqLocal      = connect.prepareStatement("SELECT * FROM alm_local_armazenamento WHERE id_local_armazenamento = ?");

			System.out.println("Importando empreendimentos...");
			/*
			 * EMPREENDIMENTOS
			 */
			ResultSet rsEmpreend = conOrigem.prepareStatement("SELECT * FROM empreend").executeQuery();
			while(rsEmpreend.next())	{
				int cdEmpreendimento = 0;
				int cdNivelEmpreendimento = getNivelLocal("EMPREENDIMENTO", 0, connect);
				pesqLocal.setString(1, "EPR_"+rsEmpreend.getString("EM_COD"));
				ResultSet rsTemp = pesqLocal.executeQuery();
				if(!rsTemp.next())	{
					LocalArmazenamento localEmpre = new LocalArmazenamento(0,0/*cdSetor*/,cdNivelEmpreendimento,0/*cdResponsavel*/,rsEmpreend.getString("EM_NOME"),
																			"EPR_"+rsEmpreend.getString("EM_COD")/*ID*/,0/*cdLocalArmazenamentoSuperior*/,cdEmpresa);
					cdEmpreendimento = LocalArmazenamentoDAO.insert(localEmpre, connect);
				}
				else
					cdEmpreendimento = rsTemp.getInt("cd_local_armazenamento");
				/*
				 * Blocos
				 */
				int cdBloco = 0;
				int cdNivelBloco = getNivelLocal("BLOCO", cdNivelEmpreendimento, connect);
				ResultSet rsBlocos = conOrigem.prepareStatement("SELECT DISTINCT un_bloco FROM unidades WHERE un_codemp = "+rsEmpreend.getInt("EM_COD")).executeQuery();
				while(rsBlocos.next())	{
					pesqLocal.setString(1, "BLC_"+rsBlocos.getString("UN_BLOCO"));
					rsTemp = pesqLocal.executeQuery();
					if(!rsTemp.next())	{
						LocalArmazenamento localBloco = new LocalArmazenamento(0,0/*cdSetor*/,cdNivelBloco,0/*cdResponsavel*/,rsBlocos.getString("UN_BLOCO"),
																				"BLC_"+rsBlocos.getString("UN_BLOCO")/*ID*/,cdEmpreendimento,cdEmpresa);
						cdBloco = LocalArmazenamentoDAO.insert(localBloco, connect);
					}
					else
						cdBloco = rsTemp.getInt("cd_local_armazenamento");
					/*
					 * ANDARES
					 */
					int cdNivelAndar = getNivelLocal("ANDAR", cdNivelBloco, connect);
					ResultSet rsAndares = conOrigem.prepareStatement("SELECT DISTINCT un_bloco, substring(un_unid from 1 for 1) AS ANDAR FROM unidades " +
							                                         "WHERE un_bloco = \'"+rsBlocos.getString("UN_BLOCO")+"\'").executeQuery();
					while(rsAndares.next()){
						pesqLocal.setString(1, rsBlocos.getString("UN_BLOCO")+"-"+rsAndares.getString("ANDAR"));
						rsTemp = pesqLocal.executeQuery();
						if(!rsTemp.next())	{
							String nmAndar = rsAndares.getString("ANDAR").equals("0") ? "Tï¿½RREO" : rsAndares.getString("ANDAR")+"ï¿½ ANDAR";
							LocalArmazenamento localBloco = new LocalArmazenamento(0,0/*cdSetor*/,cdNivelAndar,0/*cdResponsavel*/,nmAndar,
																				   rsBlocos.getString("UN_BLOCO")+"-"+rsAndares.getString("ANDAR"),cdBloco,cdEmpresa);
							LocalArmazenamentoDAO.insert(localBloco, connect);
						}
						else
							rsTemp.getInt("cd_local_armazenamento");
					}
				}
			}
			System.out.println("Importaï¿½ï¿½o de empreendimentos e blocos concluï¿½da!");
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

	public static sol.util.Result importUnidadesFromSisDispo(int cdEmpresa)	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = conectarSisDispo();
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
				cdTipoDocumento = com.tivic.manager.ptc.TipoDocumentoDAO.insert(new com.tivic.manager.ptc.TipoDocumento(0, "CONTRATO", null, com.tivic.manager.ptc.TipoDocumentoServices.TP_INC_COM_ANO,
						0, 0, 0, 0, null, null, 0, 0, 0, null), connect);
			else
				cdTipoDocumento = rs.getInt("cd_tipo_documento");
			// Verificando SETOR
			int cdSetor         = 0;
			rs = connect.prepareStatement("SELECT * FROM grl_setor WHERE nm_setor = \'DOCUMENTAï¿½ï¿½O\' AND cd_empresa = "+cdEmpresa).executeQuery();
			if(!rs.next())
				cdSetor = SetorDAO.insert(new Setor(0,0,cdEmpresa,0,"DOCUMENTAï¿½ï¿½O",1,null,null,null,null,null,null,null,0,null,"DOC",null,0,0,null), connect);
			else
				cdSetor = rs.getInt("cd_setor");
			// Verificando Tipo de Operaï¿½ï¿½o
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
				// Produto / Serviï¿½o
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
						return new sol.util.Result(-1, "B"+rsUnidades.getString("IDBLOC")+"-"+rsUnidades.getString("ANDAR")+" Nï¿½o localizado.");
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
						return new sol.util.Result(-1, "Referï¿½ncia nï¿½o localizada!");
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
						// Informaï¿½ï¿½es do Contrato
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
						Documento documento  = new Documento(0,0,cdSetor,cdUsuario,nmLocalOrigem,dtAssinatura,DocumentoServices.TP_PUBLICO,null/*txtObservacao*/,
															 idDocumento,null/*nrDocumento*/,cdTipoDocumento,0,0/*cdAtendimento*/,"Unidade nï¿½ "+rsUnidades.getString("idunid"),
															 cdSetor,0/*cdSituacaoAtual*/,0/*cdFase*/,cdEmpresa, 0/*cdProcesso*/, 0 /*tpPrioridade*/, 0/*cdDocumentoSuperior*/, 
															 null /*dsAssunto*/, null /*nrAtendimento*/, 0/*lgNotificacao*/, 0 /*cdTipoDocumentoAnterior*/, null/*nrDocumentoExterno*/,
															 null/*nrAssunto*/,null, null, 0, 1);
						ArrayList<DocumentoPessoa> solicitantes = new ArrayList<DocumentoPessoa>();
						solicitantes.add(new DocumentoPessoa(0, cdCliente, "Cliente"));
						cdDocumento = DocumentoServices.insert(documento, solicitantes).getCode();
						// INCLUINDO O CONTRATO
						Contrato contrato = new Contrato(0,0,0,cdEmpresa,cdCliente,0,0,dtAssinatura,dtPrimeiraParcela,nrDiaVencimento,nrParcelas,
								                         0,0,0,0, tpContrato, vlParcelas, vlAdesao, vlContrato, nrContrato,null,1/*stContrato*/,
								                         idContrato, null /*dtInicioVigencia*/, null /*dtFinalVigencia*/, cdAgente, 0, 0, 0,
								                         gnContrato, 0, cdTipoOperacao, cdDocumento, 0, 0, 0, null, 0);
						int cdContrato = ContratoDAO.insert(contrato, connect);
						// Produto / Serviï¿½o
						ContratoProdutoServico crtProd = new ContratoProdutoServico(cdContrato,cdProdutoServico,1,vlContrato,dtAssinatura,0,0,cdReferencia,cdEmpresa);

						ContratoProdutoServicoDAO.insert(crtProd, connect);
					}
				}
			}
			System.out.println("Importaï¿½ï¿½o de unidades concluï¿½da!");
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
		Connection conOrigem = conectarSisDispo();
		try {
			if(cdEmpresa <= 0)
				return new sol.util.Result(-1, "Empresa nï¿½o informada!");
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
							String nmAndar = rsAndares.getString("ANDAR").equals("0") ? "Tï¿½RREO" : rsAndares.getString("ANDAR")+"ï¿½ ANDAR";
							LocalArmazenamento localBloco = new LocalArmazenamento(0,0/*cdSetor*/,cdNivelAndar,0/*cdResponsavel*/,nmAndar,
																				   "B"+rsBlocos.getString("IDBLOC")+"-"+rsAndares.getString("ANDAR"),cdBloco,cdEmpresa);
							LocalArmazenamentoDAO.insert(localBloco, connect);
						}
						else
							rsTemp.getInt("cd_local_armazenamento");
					}
				}
			}
			System.out.println("Importaï¿½ï¿½o de empreendimentos e blocos concluï¿½da!");
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

	public static sol.util.Result importDocumentoSIMTRANS(int cdEmpresa, String nmArquivo)	{
		Connection connect   = Conexao.conectar();
		String line = "";
  		int i = 0;
  		RandomAccessFile raf = null;
  		try {
			if(cdEmpresa <= 0)
				return new sol.util.Result(-1, "Empresa nï¿½o informada!");
			int cdVinculo = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CLIENTE", 0);
			int cdVinculoAutor = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_AUTOR", 0);
			/***********************
			 * Importando locais de armazenamento
			 ***********************/
			int cdFaseCancelado = 9;

			PreparedStatement pesqPessoa     = connect.prepareStatement("SELECT * FROM grl_pessoa WHERE nm_pessoa = ?");
			PreparedStatement pesqDocumento  = connect.prepareStatement("SELECT * FROM ptc_documento WHERE cd_tipo_documento = ? AND id_documento = ?");
			PreparedStatement pesqDocumento2 = connect.prepareStatement("SELECT * FROM ptc_documento WHERE id_documento = ? AND txt_observacao LIKE ?");
			PreparedStatement updDocumento   = connect.prepareStatement("UPDATE ptc_documento SET dt_protocolo = ?, txt_observacao = ? WHERE cd_documento = ?");
			PreparedStatement updTramitacao  = connect.prepareStatement("UPDATE ptc_documento_tramitacao SET dt_tramitacao = ?, dt_recebimento = ? WHERE cd_documento = ?");
			String[] fields = new String[] {"nmProprietario", "nrRegistro", "nrAnoRegistro", "nmSituacao", "nmSetor", "nrDiaAtualizacao", "nrMesAtualizacao",
					                        "nrAnoAtualizacao", "nmTipoLogradouro", "nmLogradouro", "nrLogradouro", "nmComplemento", "nmBairro",
					                        "nrDiaEntrada", "nrMesEntrada", "nrAnoEntrada", "nmTipoDocumento", "nmAutor", "txtObservacao",
					                        "nrLote","nrQuadra","nrRegistro2","nrAnoRegistro2"};
			raf = new RandomAccessFile("c:/"+nmArquivo, "rw");
	  		System.out.println("Arquivo carregado...");
	  		while((line = raf.readLine()) != null)	{
	  			StringTokenizer tokens = new StringTokenizer(line, ";", false);
	  			HashMap<String,String> reg = new HashMap<String,String>();
	  			int f = 0;
	  			while(tokens.hasMoreTokens())	{
	  				if(f>=fields.length)
	  					break;
	  				reg.put(fields[f], tokens.nextToken());
	  				f++;
	  			}
	  			/*
	  			 *  Tipo de Documento
	  			 */
	  			String nmTipoDocumento = reg.get("nmTipoDocumento");
	  			if(nmTipoDocumento==null || nmTipoDocumento.trim().equals(""))
	  				nmTipoDocumento = "DESCONHECIDO";
	  			/* MUDANDO FASE PARA CANCELADO */
	  			if(nmTipoDocumento.trim().equals("CANCELADO"))	{
	  				String nrDocumento   = reg.get("nrRegistro")+"/"+reg.get("nrAnoRegistro");
	  				String nrAlvara      = reg.get("txtObservacao");
	  				pesqDocumento2.setString(1, nrDocumento);
	  				pesqDocumento2.setString(2, "%"+nrAlvara+"%");
	  				ResultSet rs = pesqDocumento2.executeQuery();
	  				if(!rs.next())
	  					System.out.println("Documento nao localizado: "+nrDocumento+", Alvarï¿½: "+nrAlvara);
	  				else	{
	  					String txtFase = "";
	  					String nrQuadra = reg.get("nrQuadra");
	  					if(nrQuadra!=null && !nrQuadra.trim().equals(""))
	  						txtFase = "QUADRA Nï¿½ "+nrQuadra;
	  					String nrLote   = reg.get("nrLote");
	  					if(nrLote!=null && !nrLote.trim().equals(""))
	  						txtFase += "\nLOTE Nï¿½ "+nrLote;
	  					if(reg.get("nrRegistro2")!=null && !reg.get("nrRegistro2").trim().equals(""))	{
	  						txtFase += "\nREGISTRO 2: "+reg.get("nrRegistro2")+"/"+reg.get("nrAnoRegistro2");
	  					}
	  					DocumentoServices.setNovaFase(rs.getInt("cd_documento"), cdFaseCancelado, 1/*cdUsuario*/, txtFase);
	  					continue;
	  				}
	  			}
	  			ResultSet rs = connect.prepareStatement("SELECT * FROM ptc_tipo_documento WHERE nm_tipo_documento = \'"+nmTipoDocumento+"\'").executeQuery();
	  			int cdTipoDocumento = 0;
	  			if(rs.next())
	  				cdTipoDocumento = rs.getInt("cd_tipo_documento");
	  			else	{
	  				com.tivic.manager.ptc.TipoDocumento tipoDoc = new com.tivic.manager.ptc.TipoDocumento(0, nmTipoDocumento, null, com.tivic.manager.ptc.TipoDocumentoServices.TP_INC_COM_ANO,
	  						0, 0, 0, 0, null, null, 0, 0, 0, null);
	  				cdTipoDocumento = com.tivic.manager.ptc.TipoDocumentoDAO.insert(tipoDoc, connect);
	  			}
	  			/*
	  			 *  Fase
	  			 */
	  			String nmFase = reg.get("nmSituacao");
	  			rs = connect.prepareStatement("SELECT * FROM ptc_fase WHERE nm_fase = \'"+nmFase+"\'").executeQuery();
	  			int cdFase = 0;
	  			if(rs.next())
	  				cdFase = rs.getInt("cd_fase");
	  			else	{
	  				Fase fase = new Fase(0,nmFase,null,0, cdEmpresa,null,0);
	  				cdFase = FaseDAO.insert(fase, connect);
	  			}
	  			/*
	  			 *  Setor
	  			 */
	  			String nmSetor = reg.get("nmSetor");
	  			rs = connect.prepareStatement("SELECT * FROM grl_setor WHERE cd_empresa = "+cdEmpresa+" AND nm_setor = \'"+nmSetor+"\'").executeQuery();
	  			int cdSetor = 0;
	  			if(rs.next())
	  				cdSetor = rs.getInt("cd_setor");
	  			else	{
	  				Setor setor = new Setor(0,0,cdEmpresa,0,nmSetor,1,null,null,null,null,null,null,null,0,null,null,null,0,0,null);
	  				cdSetor = SetorDAO.insert(setor, connect);
	  			}
	  			/*
	  			 *  Gravando PESSOA
	  			 */
	  			String nmPessoa = reg.get("nmProprietario");
	  			pesqPessoa.setString(1, nmPessoa);
	  			rs = pesqPessoa.executeQuery();
	  			int cdPessoa = 0;
	  			if(rs.next())
	  				cdPessoa = rs.getInt("cd_pessoa");
	  			else	{
		  			PessoaFisica pessoa = new PessoaFisica(0/*cdPessoa*/,0/*cdPessoaSuperior*/,0/*cdPais*/,nmPessoa,
							   null,null,null,null /*nrFax*/,null,new GregorianCalendar(),PessoaServices.TP_FISICA,null /*imgFoto*/,1 /*stCadastro*/,
							   null,null/*nmApelido*/,null/*observacao*/,0/*lgNotificacao*/,null/*idPessoa*/,
							   0/*cdClassificacao*/,0/*cdFormaDivulgacao*/,null /*dtChegadaPais*/,
							   0, 0 /*cdEscolaridade*/,null /*dtNascimento*/,null,"SSP"/*orgao rg*/,null /*nmMae*/,
							   null/*nmPai*/,0/*tpSexo*/,0/*stEstadoCivil*/,null,null/*nrCnh*/,null/*dtValidadeCnh*/,
							   null/*dtPrimeiraHabilitacao*/,0/*tpCategoriaHabilitacao*/,0/*tpRaca*/,
							   0/*lgDeficienteFisico*/,null/*nmFormaTratamento*/,0,null/*dtEmissaoRg*/, null /*blbFingerprint*/);
		  			cdPessoa = PessoaServices.insert(pessoa, null/*endereï¿½o*/, cdEmpresa, cdVinculo).getCode();
		  			// Tipo de Logradouro
		  			int cdTipoLogradouro = 0;
		  			String nmTipoLogradouro = reg.get("nmTipoLogradouro");
		  			if(nmTipoLogradouro!=null && !nmTipoLogradouro.equals(""))	{
		  				rs = connect.prepareStatement("SELECT * FROM grl_tipo_logradouro WHERE nm_tipo_logradouro = \'"+nmTipoLogradouro.replaceAll("\'", "")+"\'").executeQuery();
		  				if(rs.next())
		  					cdTipoLogradouro = rs.getInt("cd_tipo_logradouro");
		  				else	{
		  					String sgTipoLogradouro = nmTipoLogradouro!=null && nmTipoLogradouro.length()>10 ? nmTipoLogradouro.substring(0,9) : "";
		  					TipoLogradouro tipo = new TipoLogradouro(0, nmTipoLogradouro, sgTipoLogradouro);
		  					cdTipoLogradouro = TipoLogradouroDAO.insert(tipo, connect);
		  				}
		  			}
		  			// PessoaEndereco
		  			String nmLogradouro = reg.get("nmLogradouro");
		  			String nmBairro     = reg.get("nmBairro");
		  			PessoaEndereco endereco = new PessoaEndereco(0,cdPessoa,nmLogradouro,cdTipoLogradouro,0,0,0,0 /*cdCidade*/,
		  					                                     nmLogradouro,nmBairro,null /*nrCep*/,reg.get("nrEndereco"),reg.get("nmComplemento"),null,null,1,1);
		  			PessoaEnderecoDAO.insert(endereco, connect);
	  			}
	  			if(cdPessoa<=0)	{
	  				System.out.println("Erro ao tentar incluir solicitante! cdPessoa = "+cdPessoa);
	  				raf.close();
	  				return new sol.util.Result(cdPessoa);
	  			}
	  			/*
	  			 *  Autor
	  			 */
	  			int cdAutor    = 0;
	  			String nmAutor = reg.get("nmAutor");
	  			if(nmAutor!=null && !nmAutor.trim().equals(""))	{
		  			pesqPessoa.setString(1, nmAutor);
		  			rs = pesqPessoa.executeQuery();
		  			if(rs.next())
		  				cdAutor = rs.getInt("cd_pessoa");
		  			else	{
			  			PessoaFisica pessoa = new PessoaFisica(0/*cdPessoa*/,0/*cdPessoaSuperior*/,0/*cdPais*/,nmAutor,
								   null,null,null,null /*nrFax*/,null,new GregorianCalendar(),PessoaServices.TP_FISICA,null /*imgFoto*/,1 /*stCadastro*/,
								   null,null/*nmApelido*/,null/*observacao*/,0/*lgNotificacao*/,null/*idPessoa*/,
								   0/*cdClassificacao*/,0/*cdFormaDivulgacao*/,null /*dtChegadaPais*/,
								   0, 0 /*cdEscolaridade*/,null /*dtNascimento*/,null,"SSP"/*orgao rg*/,null /*nmMae*/,
								   null/*nmPai*/,0/*tpSexo*/,0/*stEstadoCivil*/,null,null/*nrCnh*/,null/*dtValidadeCnh*/,
								   null/*dtPrimeiraHabilitacao*/,0/*tpCategoriaHabilitacao*/,0/*tpRaca*/,
								   0/*lgDeficienteFisico*/,null/*nmFormaTratamento*/,0,null/*dtEmissaoRg*/, null /*blbFingerprint*/);
			  			cdAutor = PessoaServices.insert(pessoa, null, cdEmpresa, cdVinculoAutor).getCode();
		  			}
	  			}
	  			/*
	  			 * DOCUMENTO
	  			 */
	  			String[] meses = new String[] {"JAN","FEV","MAR","ABR","MAI","JUN","JUL","AGO","SET","OUT","NOV","DEZ"};
	  			int nrMes = 0;
	  			int nrAno = 0;
	  			int nrDia = 0;
	  			try	{
		  			nrAno = Integer.parseInt(reg.get("nrAnoEntrada"));
		  			nrDia = Integer.parseInt(reg.get("nrDiaEntrada"));
	  			}
	  			catch(Exception e){
	  				nrAno = 2011;
	  				nrDia = 05;
	  			}
	  			String nrMesRegistro = reg.get("nrMesEntrada");
	  			for(int m=0; m<meses.length; m++)
	  				if(meses[m].equals(nrMesRegistro))	{
	  					nrMes = m;
	  					break;
	  				}
				int cdUsuario = 1;
				GregorianCalendar dtProtocolo = new GregorianCalendar(nrAno, nrMes, nrDia);
				// Documento
				int cdDocumento      = 0;
				String nmLocalOrigem = "";
				String nrDocumento   = reg.get("nrRegistro")+"/"+reg.get("nrAnoRegistro");
				String txtObservacao = reg.get("txtObservacao");
				if(txtObservacao!=null && !txtObservacao.trim().equals(""))
					txtObservacao = "ALVARï¿½ Nï¿½ "+txtObservacao;
				String nrQuadra = reg.get("nrQuadra");
				if(nrQuadra!=null && !nrQuadra.trim().equals(""))
					txtObservacao += "\nQUADRA Nï¿½ "+nrQuadra;
				String nrLote   = reg.get("nrLote");
				if(nrLote!=null && !nrLote.trim().equals(""))
					txtObservacao += "\nLOTE Nï¿½ "+nrLote;
				if(reg.get("nrRegistro2")!=null && !reg.get("nrRegistro2").trim().equals(""))	{
					txtObservacao += "\nREGISTRO 2: "+reg.get("nrRegistro2")+"/"+reg.get("nrAnoRegistro2");
				}
				pesqDocumento.setInt(1, cdTipoDocumento);
				pesqDocumento.setString(2, nrDocumento);
				rs = pesqDocumento.executeQuery();
				if(!rs.next())	{
					Documento documento = new Documento(0,0,cdSetor,cdUsuario,nmLocalOrigem,dtProtocolo,DocumentoServices.TP_PUBLICO,txtObservacao,
														nrDocumento,nrDocumento,cdTipoDocumento,0,0/*cdAtendimento*/,"",
														cdSetor,0/*cdSituacaoAtual*/,cdFase,cdEmpresa, 0/*cdProcesso*/, 0 /*tpPrioridade*/, 0/*cdDocumentoSuperior*/, 
														null /*dsAssunto*/, null /*nrAtendimento*/, 0/*lgNotificacao*/, 0 /*cdTipoDocumentoAnterior*/, 
														null/*nrDocumentoExterno*/, null/*nrAssunto*/,null, null, 0, 1);
					ArrayList<DocumentoPessoa> solicitantes = new ArrayList<DocumentoPessoa>();
					solicitantes.add(new DocumentoPessoa(0, cdPessoa, "Proprietï¿½rio"));
					if(cdAutor>0 && cdAutor!=cdPessoa)
						solicitantes.add(new DocumentoPessoa(0, cdAutor, "Autor"));
					try	{
						Result result = DocumentoServices.insert(documento, solicitantes);
						cdDocumento = result.getCode();
						if (result.getCode() <= 0)
							System.out.println("Erro: "+result.getMessage());
					}
					catch(Exception e){
						e.printStackTrace(System.out);
						continue;
					}
				}
				else
					cdDocumento = rs.getInt("cd_documento");
				try	{
					updDocumento.setTimestamp(1, new java.sql.Timestamp(dtProtocolo.getTimeInMillis()));
					updDocumento.setString(2, txtObservacao);
					updDocumento.setInt(3, cdDocumento);
					updDocumento.executeUpdate();
				}
				catch(Exception e){
					e.printStackTrace(System.out);
					continue;
				}
				//
				try	{
					updTramitacao.setTimestamp(1, new java.sql.Timestamp(dtProtocolo.getTimeInMillis()));
					updTramitacao.setTimestamp(2, new java.sql.Timestamp(dtProtocolo.getTimeInMillis()));
					updTramitacao.setInt(3, cdDocumento);
					updTramitacao.executeUpdate();
				}
				catch(Exception e){
					e.printStackTrace(System.out);
					continue;
				}
				//
	  			i++;
	  		}
			System.out.println("Importaï¿½ï¿½o de Documentos Concluï¿½da!");
			raf.close();
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.out.println("Linha "+i+": "+line);
			return new sol.util.Result(-1, "Erro ao tentar importar documentos!", e);
		}
		finally{
			if(raf!=null)
		  		try {raf.close(); } catch(Exception e){};
			Conexao.desconectar(connect);
		}
	}

	public static sol.util.Result scpToText()	{
  		RandomAccessFile raf = null;
		try {
			System.out.println("Importando produtos de arquivo SCP...");
			String nmArquivo = "users/leo/desktop/ARQCLI.SCP", line = "";
			raf = new RandomAccessFile("c:/"+nmArquivo, "rw");
			RandomAccessFile raf2 = new RandomAccessFile("c:/users/leo/desktop/clientes.txt", "rw");
	  		System.out.println("Arquivo carregado...");
			//int l = 0;
	  		while((line = raf.readLine()) != null)	{
	  			String newLine = "";
  				for(int i=0; i<line.length(); i++)	{
  					if((int)line.charAt(i) > 30)
  						newLine += line.charAt(i); 
  				}
  				if(!newLine.trim().equals(""))	{
  					if(newLine.indexOf("ï¿½")>=0)
  						newLine = newLine.substring(newLine.indexOf("ï¿½")+1);
  					if(newLine.length() > 20)
  						raf2.writeBytes(newLine+"\n");
  				}
	  			// l++;
	  		}
	  		raf2.close();
	  		raf.close();
	  		System.out.println("Concluido com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
  		return new Result(1); 
	}

	public static sol.util.Result importClientesLEZALEZ(int cdEmpresa)	{
		Connection connect   = Conexao.conectar();
		try {
			/***********************
			 * Importando pessoas
			 ***********************/
			PreparedStatement pesqPessoa  = connect.prepareStatement("SELECT A.* FROM grl_pessoa A " +
					                                                 "LEFT OUTER JOIN grl_pessoa_fisica   B ON (A.cd_pessoa = B.cd_pessoa) " +
					                                                 "LEFT OUTER JOIN grl_pessoa_juridica C ON (A.cd_pessoa = C.cd_pessoa) " +
					                                                 "WHERE id_pessoa = ? OR nm_pessoa = ? OR nr_cpf = ? OR nr_cnpj = ? ");
			PreparedStatement pesqEstado  = connect.prepareStatement("SELECT * FROM grl_estado  WHERE sg_estado = ?");
			PreparedStatement pesqCidade  = connect.prepareStatement("SELECT * FROM grl_cidade  WHERE nm_cidade = ?");
			PreparedStatement pesqVinculo = connect.prepareStatement("SELECT * FROM grl_vinculo WHERE nm_vinculo = ?");
			System.out.println("Importando clientes L\'CLUB...");
			int cdVinculo = 0;
			pesqVinculo.setString(1, "CLIENTE");
			ResultSet rsTemp = pesqVinculo.executeQuery();
			if(!rsTemp.next())	{
				Vinculo vinculo = new Vinculo(0,"CLIENTE",1 /*lgEstatico*/, 0 /*lgFuncao*/, 0 /*cdFormulario*/, 1 /*lgCadastro*/);
				cdVinculo = VinculoDAO.insert(vinculo, connect);
			}
			else
				cdVinculo = rsTemp.getInt("cd_vinculo");
			//
			RandomAccessFile raf = new RandomAccessFile("c:/users/adm/desktop/clientes.txt", "rw");
			int qtRegistros    = 0;
			int qtJaCadastrado = 0;
			int qtIncluido     = 0;
			String line = null;
			while((line = raf.readLine()) != null)	{
				String nmCliente  = line.substring(0, 40).trim();
				String idCliente  = line.substring(40, 46).trim();
				String nmApelido  = line.substring(46, 86).trim();
				String dsEndereco = line.substring(86, 126).trim();
				String nmBairro   = line.substring(126, 146).trim();
				String nrCep      = line.substring(146, 154).trim();
				String nmCidade   = line.substring(154, 174).trim();
				String sgEstado   = line.substring(174, 176).trim();
				String gnPessoa   = line.substring(176, 177).trim();
				String nrIE       = line.substring(177, 197).trim();
				String nrCpfCnpj  = line.substring(197, 211).trim();
				String dsObs   	  = line.substring(217, 257).trim();
				String nrTelefones= line.substring(257, 297).trim();
				String nmPai      = line.substring(444, 484).trim();
				String nmMae      = line.substring(484, 524).trim();
				String dsRefCom	  = line.substring(524, 644).trim();
				String dsNasc     = line.substring(644, 652).trim();
				dsNasc = dsNasc!=null ? dsNasc.substring(0,2)+"/"+dsNasc.substring(2,4)+"/"+dsNasc.substring(4,8) : "";  
				String dsEmail	  = line.substring(701, 739).trim();
				String dsObs2  	  = line.substring(747, 939).trim();
				String txtObservacao = nrTelefones+"\n"+dsObs+"\n"+dsRefCom+"\n"+dsObs2;
				if(nrCpfCnpj.equals("00000000000000"))
					nrCpfCnpj = "";
				if(nrIE.length()>15)
					nrIE = nrIE.substring(0,14);
				qtRegistros++;
				pesqPessoa.setString(1, "CLI_"+idCliente);
				pesqPessoa.setString(2, nmCliente);
				pesqPessoa.setString(3, nrCpfCnpj.equals("") ? "X" : nrCpfCnpj);
				pesqPessoa.setString(4, nrCpfCnpj.equals("") ? "X" : nrCpfCnpj);
				rsTemp = pesqPessoa.executeQuery();
				if(rsTemp.next())	{
					int cdPessoa = rsTemp.getInt("cd_pessoa");
					qtJaCadastrado++;
					rsTemp = connect.prepareStatement("SELECT * FROM grl_pessoa_empresa " +
							                          "WHERE cd_empresa = "+cdEmpresa+
							                          "  AND cd_vinculo = "+cdVinculo+
							                          "  AND cd_pessoa  = "+cdPessoa).executeQuery();
					if(!rsTemp.next())	{
						connect.prepareStatement("INSERT INTO grl_pessoa_empresa (cd_empresa,cd_vinculo,cd_pessoa) " +
		                                         " VALUES ("+cdEmpresa+","+cdVinculo+","+cdPessoa+")").executeUpdate();
					}
					PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_pessoa_fisica SET dt_nascimento = ? " +
	                                                                   "WHERE cd_pessoa  = "+cdPessoa);
					pstmt.setTimestamp(1, Util.convCalendarToTimestamp(Util.convStringToCalendar(dsNasc)));
					pstmt.executeUpdate();
					if(qtJaCadastrado % 100 == 0)
						System.out.println(qtJaCadastrado+" atualizados");
					continue;
				}
				//
				int tpSexo = -1; // rs.getString("Sexo")!=null && rs.getString("Sexo").equalsIgnoreCase("Feminino") ? 1 : 0;
				// nrTelefone1        = nrTelefone1!=null ? nrTelefone1.replaceAll("[-]", "").replaceAll("[(]", "").replaceAll("[)]", "") : null;
				// nrFax = nrFax!=null ? nrFax.replaceAll("[-]", "").replaceAll("[(]", "").replaceAll("[)]", "") : null;
				nrCpfCnpj          = nrCpfCnpj!=null ? nrCpfCnpj.replaceAll("[ ]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[.]", "") : null;
				String nrTelefone1 = null;
				String nrFax       = null;
				// PESSOA Jurï¿½dica
				if(nmCliente!=null && nmCliente.length()>50)
					nmCliente = nmCliente.substring(0,49);
				Pessoa pessoa = null;
				if (gnPessoa.equals("F")) {
						if(nrCpfCnpj.length()>11)
							nrCpfCnpj = nrCpfCnpj.substring(0,10);
						pessoa = new PessoaFisica(0/*cdPessoa*/,0/*cdPessoaSuperior*/,0/*cdPais*/,nmCliente,
						   nrTelefone1,nrFax,null/*nrCelular*/,nrFax,dsEmail,
						   new GregorianCalendar(),
						   PessoaServices.TP_FISICA,null /*imgFoto*/,1 /*stCadastro*/,
						   null/*homepage*/,nmApelido,txtObservacao,
						   0/*lgNotificacao*/,"CLI_"+idCliente/*idPessoa*/,
						   0/*cdClassificacao*/,0/*cdFormaDivulgacao*/,null /*dtChegadaPais*/,
						   0/*cdNaturalidade*/, 0 /*cdEscolaridade*/,
						   null/*dtNascimento*/,
						   nrCpfCnpj,"SSP"/*orgao rg*/,nmMae,
						   nmPai,tpSexo,0/*stEstadoCivil*/,
						   nrIE/*nrRG*/,null/*nrCnh*/,null/*dtValidadeCnh*/,
						   null/*dtPrimeiraHabilitacao*/,0/*tpCategoriaHabilitacao*/,0/*tpRaca*/,
						   0/*lgDeficienteFisico*/,null/*nmFormaTratamento*/,0/*cdEstadoRg*/,null/*dtEmissaoRg*/,
						   null /*blbFingerprint*/);
				}
				else	{
					pessoa = new PessoaJuridica(0/*cdPessoa*/,0/*cdPessoaSuperior*/,0/*cdPais*/,nmCliente,
			   					                nrTelefone1,null/*nrTelefone2*/,null/*nrCelular*/,nrFax,null,
			   					                new GregorianCalendar(),
			   					                PessoaServices.TP_JURIDICA,null /*imgFoto*/,1 /*stCadastro*/,
			   					                null/*nmUrl*/,null/*nmApelido*/,txtObservacao,
			   					                0/*lgNotificacao*/,"CLI_"+idCliente/*idPessoa*/,
			   					                0/*cdClassificacao*/,0/*cdFormaDivulgacao*/,null /*dtChegadaPais*/,
			   					                nrCpfCnpj,nmCliente,nrIE, null/*nrInscMunicipal*/,
			   					                0/*nrFuncionarios*/,null/*dtInicioAtividade*/,0/*cdNaturezaJuridica*/,
			   					                0/*tpEmpresa*/,null/*dtTerminoAtividade*/);
				}

				// ENDEREï¿½O
				int cdCidade = 0;
				if(nmCidade!=null)	{
					if(rsTemp!=null)
						rsTemp.close();
					pesqCidade.setString(1, nmCidade!=null ? nmCidade.toUpperCase() : null);
					rsTemp = pesqCidade.executeQuery();
					if(rsTemp.next())
						cdCidade = rsTemp.getInt("cd_cidade");
					else	{
						int cdEstado = 0;
						pesqEstado.setString(1, sgEstado!=null ? sgEstado.toUpperCase() : "");
						rsTemp = pesqEstado.executeQuery();
						if(rsTemp.next())
							cdEstado = rsTemp.getInt("cd_estado");
						com.tivic.manager.grl.Cidade cidade = new com.tivic.manager.grl.Cidade(0,nmCidade,null/*cep*/,0/* vlAltitude*/,
																				 0/* vlLatitude*/,0/* vlLongitude*/,cdEstado,
																				 null/* idCidade*/,0/* cdRegiao*/,null/* idIbge*/, null, 0, 0);
						cdCidade = com.tivic.manager.grl.CidadeDAO.insert(cidade, connect);
					}
				}
				if(cdCidade < 0)
					cdCidade = 0;
				if(nrCep!=null && nrCep.trim().length()>8)
					nrCep = nrCep.trim().substring(0,7);
				if(dsEndereco!=null && dsEndereco.trim().length()>50)
					dsEndereco = dsEndereco.trim().substring(0,49);
				PessoaEndereco endereco = new PessoaEndereco(0,0/*cdPessoa*/,dsEndereco,0/*cdTipoLogradouro*/,0/*cdTipoEndereco*/,
														     0/*cdLogradouro*/,0/*cdBairro*/,cdCidade,dsEndereco,
														     nmBairro,nrCep,
														     null/*nrEndereco*/,null/*complemento*/,
														     nrTelefone1,null/*nmPontoReferencia*/,0/*lgCobranca*/,1/*lgPrincipal*/);
				int cdPessoa = PessoaServices.insert(pessoa, endereco, cdEmpresa, cdVinculo).getCode();
				if(cdPessoa < 0)
					System.out.println("Nï¿½o foi possivel incluir["+cdPessoa+"]: "+pessoa);
				qtIncluido++;
				if(qtIncluido%10==0)
					System.out.println(qtIncluido+" incluidos");
			}
			System.out.println("Importaï¿½ï¿½o de clientes concluï¿½da! [Registros: "+qtRegistros+", Incluidos: "+qtIncluido+", qtJacadastrados: "+qtJaCadastrado);
			raf.close();
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar pessoas!", e);
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	/*
	 * IMPORTAï¿½ï¿½O BASIC STORE
	 */
	public static sol.util.Result importContaReceberFromSCP(int cdEmpresa)	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = conectarMySQL();
		try {
			/***********************
			 * Importando Contas a receber
			 ***********************/
			PreparedStatement pesqContaReceber = connect.prepareStatement("SELECT * FROM adm_conta_receber " +
					                                                      "WHERE cd_empresa = " +cdEmpresa+
					                                                      "  AND id_conta_receber = ?");
			PreparedStatement pesqPessoa  = connect.prepareStatement("SELECT * FROM grl_pessoa WHERE id_pessoa = ?");
			PreparedStatement pesqPessoa2  = connect.prepareStatement("SELECT A.* FROM grl_pessoa A " +
                    												  "LEFT OUTER JOIN grl_pessoa_fisica B ON (A.cd_pessoa = B.cd_pessoa) " +
                    												  "WHERE nm_pessoa = ? OR nr_cpf = ? ");
			// Tipo Documento
			ResultSet rsTemp = connect.prepareStatement("SELECT * FROM adm_tipo_documento WHERE nm_tipo_documento LIKE \'%NP%\'").executeQuery();
			int cdTipoDocumento  = 0;
			if(rsTemp.next())
				cdTipoDocumento = rsTemp.getInt("cd_tipo_documento");
			// Conta e Carteira
			rsTemp = connect.prepareStatement("SELECT * FROM adm_conta_financeira A, adm_conta_carteira B " +
					                          "WHERE A.cd_conta = B.cd_conta AND tp_conta = 0" +
					                          "  AND A.cd_empresa = "+cdEmpresa).executeQuery();
			int cdConta          = 0;
			int cdContaCarteira  = 0;
			if(rsTemp.next())	{
				cdConta 		= rsTemp.getInt("cd_conta");
				cdContaCarteira = rsTemp.getInt("cd_conta_carteira");
			}
			// Categoria
			int cdCategoriaSaida = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_SAIDAS_DEFAULT", 0, cdEmpresa);
			if(cdCategoriaSaida <= 0)
				return new Result(-1, "Vocï¿½ deve informar a categoria padrï¿½o para saï¿½das!");
			System.out.println("Importando contas a receber L CLUB...");
			//
			String nmArquivo = "users/ADM/desktop/ARQCRED.SCP", line = "";
			RandomAccessFile raf = new RandomAccessFile("c:/"+nmArquivo, "rw");
	  		System.out.println("Arquivo carregado...");
			// int l = 0;
	  		while((line = raf.readLine()) != null)	{
	  			System.out.println(line);
	  			
	  			String idContaReceber          = line.substring(7,13).trim();
	  			String idCliente               = line.substring(13,23);
	  			GregorianCalendar dtVencimento = Util.stringToCalendar(line.substring(29,31)+"/"+line.substring(31,33)+"/"+line.substring(33,37));
	  			float vlConta                  = Float.parseFloat(line.substring(119,128)+"/"+line.substring(128,130));
	  			String dsHistorico             = line.substring(59,119);
	  			GregorianCalendar dtBaixa = null,  dtEmissao = null;
	  			float vlDesconto = 0, vlPago = 0;
	  			int nrParcela = 0;
	  			String nrCpf = "";
	  			//l++;
	  			//
				pesqContaReceber.setString(1, "CR"+idContaReceber);
				rsTemp = pesqContaReceber.executeQuery();
				if(!rsTemp.next())	{
					// Pesquisando pessoa
					pesqPessoa.setString(1, "CLI_"+idCliente);
					rsTemp = pesqPessoa.executeQuery();
					if(!rsTemp.next())	{
						pesqPessoa2.setString(2, nrCpf);
						rsTemp = pesqPessoa2.executeQuery();
						if(!rsTemp.next())	{
							System.out.println("Cliente nï¿½o localizado: "+idCliente);
							continue;
						}
					}
					int cdPessoa = rsTemp.getInt("cd_pessoa");
					int stConta  = dtBaixa !=null ? 1/*Recebida*/ : 0/*Em Aberto*/;
					ArrayList<ContaReceberCategoria> categorias = new ArrayList<ContaReceberCategoria>();
					categorias.add(new ContaReceberCategoria(0,cdCategoriaSaida, vlConta, 0));
					//
					ContaReceber contaReceber = new ContaReceber(0/*cdContaReceber*/,cdPessoa,cdEmpresa,0/*cdContrato*/,0/*cd_conta_origem*/,0/*cd_documento_saida*/,
							                                     cdContaCarteira,cdConta,0/*cd_frete*/,idContaReceber,"CR"+idContaReceber,
							                                     nrParcela,""/*nr_referencia*/,cdTipoDocumento,dsHistorico,
							                                     dtVencimento,
							                                     dtEmissao,
							                                     dtBaixa,
							                                     null/*dt_prorrogacao*/,
							                                     Double.valueOf( Float.toString(vlConta)),
							                                     Double.valueOf( Float.toString(vlDesconto)),0.0d/*vlAcrescimo*/,
							                                     stConta==1? Double.valueOf( Float.toString(vlPago)) : 0.0d,
							                                     stConta,0/*tp_frequencia*/,0/*qt_parcelas*/,0/*tp_conta_receber*/,0/*cd_negociacao*/,
							                                     null/*txtObservacao*/,0/*cd_plano_pagamento*/,0/*cd_forma_pagamento*/,
							                                     new GregorianCalendar(),
							                                    dtVencimento, 0/*cdTurno*/, 0.0d/*prJuros*/, 0.0d/*prMulta*/, 0/*lgProtesto*/);
					Result result = ContaReceberServices.insert(contaReceber, categorias, null/*tituloCredito*/, true, connect);
					if(result.getCode() < 0)
						System.out.println("Nï¿½o foi possivel incluir["+result.getCode()+"]: "+result.getMessage()+"\n"+contaReceber);

				}
			}
			System.out.println("Importaï¿½ï¿½o de clientes concluï¿½da!");
			raf.close();
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
	
	public static Result getTabelasSincronizacao(){
		
		ArrayList<String> tabelas 						= new ArrayList<String>();
		ArrayList<String> chaves  						= new ArrayList<String>();
		HashMap <String, Object> chavesEstrangeiras 	= new HashMap <String, Object>();
		ArrayList<String> tabelaEstrangeira  			= new ArrayList<String>();
		HashMap <String, Object> tabelasSincronizadas 	= new HashMap<String, Object>();
		
		tabelas.add("grl_unidade_medida");
		chaves.add("cd_unidade_medida");
		tabelasSincronizadas.put("grl_unidade_medida", chaves);
		
		tabelas.add("adm_classificacao");
		chaves  = new ArrayList<String>();
		chaves.add("cd_classificacao");
		tabelasSincronizadas.put("adm_classificacao", chaves);
		
		tabelas.add("grl_forma_divulgacao");
		chaves  = new ArrayList<String>();
		chaves.add("cd_forma_divulgacao");
		tabelasSincronizadas.put("grl_forma_divulgacao", chaves);
		
		tabelas.add("grl_regiao");
		chaves  = new ArrayList<String>();
		chaves.add("cd_regiao");
		tabelasSincronizadas.put("grl_regiao", chaves);
		
		tabelas.add("grl_pais");
		chaves  = new ArrayList<String>();
		chaves.add("cd_pais");
		tabelasSincronizadas.put("grl_pais", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("grl_regiao");
		tabelaEstrangeira.add("cd_regiao");
		chavesEstrangeiras.put("cd_regiao", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_paisRef", chavesEstrangeiras);
		
		tabelas.add("adm_categoria_economica");
		chaves  = new ArrayList<String>();
		chaves.add("cd_categoria_economica");
		tabelasSincronizadas.put("adm_categoria_economica", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("adm_categoria_economica");
		tabelaEstrangeira.add("cd_categoria_economica");
		chavesEstrangeiras.put("cd_categoria_superior", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_categoria_economicaRef", chavesEstrangeiras);
		
		tabelas.add("adm_classificacao_fiscal");
		chaves  = new ArrayList<String>();
		chaves.add("cd_classificacao_fiscal");
		tabelasSincronizadas.put("adm_classificacao_fiscal", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("adm_classificacao_fiscal");
		tabelaEstrangeira.add("cd_classificacao_fiscal");
		chavesEstrangeiras.put("cd_classificacao_superior", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_classificacao_fiscalRef", chavesEstrangeiras);
		
		tabelas.add("grl_pessoa");
		chaves  = new ArrayList<String>();
		chaves.add("cd_pessoa");
		tabelasSincronizadas.put("grl_pessoa", chaves);
		tabelasSincronizadas.put("adm_classificacao_fiscal", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("adm_classificacao");
		tabelaEstrangeira.add("cd_classificacao");
		chavesEstrangeiras.put("cd_classificacao", tabelaEstrangeira);
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("grl_forma_divulgacao");
		tabelaEstrangeira.add("cd_forma_divulgacao");
		chavesEstrangeiras.put("cd_forma_divulgacao", tabelaEstrangeira);
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pais");
		tabelaEstrangeira.add("cd_pais");
		chavesEstrangeiras.put("cd_pais", tabelaEstrangeira);
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_pessoa_superior", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_pessoaRef", chavesEstrangeiras);
		
		tabelas.add("grl_ncm");
		chaves  = new ArrayList<String>();
		chaves.add("cd_ncm");
		tabelasSincronizadas.put("grl_ncm", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("grl_unidade_medida");
		tabelaEstrangeira.add("cd_unidade_medida");
		chavesEstrangeiras.put("cd_unidade_medida", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_ncmRef", chavesEstrangeiras);
		
		tabelas.add("grl_estado");
		chaves  = new ArrayList<String>();
		chaves.add("cd_estado");
		tabelasSincronizadas.put("grl_estado", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pais");
		tabelaEstrangeira.add("cd_pais");
		chavesEstrangeiras.put("cd_pais", tabelaEstrangeira);
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("grl_regiao");
		tabelaEstrangeira.add("cd_regiao");
		chavesEstrangeiras.put("cd_regiao", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_estadoRef", chavesEstrangeiras);
		
		
		tabelas.add("bpm_classificacao");
		chaves  = new ArrayList<String>();
		chaves.add("cd_classificacao");
		tabelasSincronizadas.put("bpm_classificacao", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("bpm_classificacao");
		tabelaEstrangeira.add("cd_classificacao");
		chavesEstrangeiras.put("cd_classificacao_superior", tabelaEstrangeira);
		tabelasSincronizadas.put("bpm_classificacaoRef", chavesEstrangeiras);
		
		tabelas.add("grl_produto_servico");
		chaves  = new ArrayList<String>();
		chaves.add("cd_produto_servico");
		tabelasSincronizadas.put("grl_produto_servico", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("adm_categoria_economica");
		tabelaEstrangeira.add("cd_categoria_economica");
		chavesEstrangeiras.put("cd_categoria_despesa", tabelaEstrangeira);
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("adm_categoria_economica");
		tabelaEstrangeira.add("cd_categoria_economica");
		chavesEstrangeiras.put("cd_categoria_economica", tabelaEstrangeira);
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("adm_categoria_economica");
		tabelaEstrangeira.add("cd_categoria_economica");
		chavesEstrangeiras.put("cd_categoria_receita", tabelaEstrangeira);
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("adm_classificacao_fiscal");
		tabelaEstrangeira.add("cd_classificacao_fiscal");
		chavesEstrangeiras.put("cd_classificacao_fiscal", tabelaEstrangeira);
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_fabricante", tabelaEstrangeira);
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("grl_ncm");
		tabelaEstrangeira.add("cd_ncm");
		chavesEstrangeiras.put("cd_ncm", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_produto_servicoRef", chavesEstrangeiras);
		
		tabelas.add("grl_produto");
		chaves  = new ArrayList<String>();
		chaves.add("cd_produto_servico");
		tabelasSincronizadas.put("grl_produto", chaves);
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_produtoRef", chavesEstrangeiras);
		
		tabelas.add("grl_cidade");
		chaves  = new ArrayList<String>();
		chaves.add("cd_cidade");
		tabelasSincronizadas.put("grl_cidade", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("grl_estado");
		tabelaEstrangeira.add("cd_estado");
		chavesEstrangeiras.put("cd_estado", tabelaEstrangeira);
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("grl_regiao");
		tabelaEstrangeira.add("cd_regiao");
		chavesEstrangeiras.put("cd_regiao", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_cidadeRef", chavesEstrangeiras);
		
		tabelas.add("bpm_marca");
		chaves  = new ArrayList<String>();
		chaves.add("cd_marca");
		tabelasSincronizadas.put("bpm_marca", chaves);
		
		tabelas.add("bpm_bem");
		chaves  = new ArrayList<String>();
		chaves.add("cd_bem");
		tabelasSincronizadas.put("bpm_bem", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("bpm_classificacao");
		tabelaEstrangeira.add("cd_classificacao");
		chavesEstrangeiras.put("cd_classificacao", tabelaEstrangeira);
		tabelaEstrangeira = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_bem", tabelaEstrangeira);
		tabelasSincronizadas.put("bpm_bemRef", chavesEstrangeiras);
		
		tabelas.add("grl_setor");
		chaves  = new ArrayList<String>();
		chaves.add("cd_setor");
		tabelasSincronizadas.put("grl_setor", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_responsavel", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_setor");
		tabelaEstrangeira.add("cd_setor");
		chavesEstrangeiras.put("cd_setor_superior", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_setorRef", chavesEstrangeiras);
		
		tabelas.add("prc_area_direito");
		chaves  = new ArrayList<String>();
		chaves.add("cd_area_direito");
		tabelasSincronizadas.put("prc_area_direito", chaves);
		
		tabelas.add("prc_comarca");
		chaves  = new ArrayList<String>();
		chaves.add("cd_comarca");
		tabelasSincronizadas.put("prc_comarca", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_cidade");
		tabelaEstrangeira.add("cd_cidade");
		chavesEstrangeiras.put("cd_cidade", tabelaEstrangeira);
		tabelasSincronizadas.put("prc_comarcaRef", chavesEstrangeiras);
		
		tabelas.add("prc_tipo_orgao");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tipo_orgao");
		tabelasSincronizadas.put("prc_tipo_orgao", chaves);
		
		tabelas.add("grl_distrito");
		chaves  = new ArrayList<String>();
		chaves.add("cd_distrito");
		chaves.add("cd_cidade");
		tabelasSincronizadas.put("grl_distrito", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_cidade");
		tabelaEstrangeira.add("cd_cidade");
		chavesEstrangeiras.put("cd_cidade", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_distritoRef", chavesEstrangeiras);
		
		tabelas.add("grl_tipo_logradouro");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tipo_logradouro");
		tabelasSincronizadas.put("grl_tipo_logradouro", chaves);
		
		tabelas.add("grl_logradouro");
		chaves  = new ArrayList<String>();
		chaves.add("cd_logradouro");
		tabelasSincronizadas.put("grl_logradouro", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_distrito");
		tabelaEstrangeira.add("cd_distrito");
		chavesEstrangeiras.put("cd_distrito", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_regiao");
		tabelaEstrangeira.add("cd_regiao");
		chavesEstrangeiras.put("cd_regiao", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_tipo_logradouro");
		tabelaEstrangeira.add("cd_tipo_logradouro");
		chavesEstrangeiras.put("cd_tipo_logradouro", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_logradouroRef", chavesEstrangeiras);
		
		tabelas.add("fta_tipo_rota");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tipo_rota");
		tabelasSincronizadas.put("fta_tipo_rota", chaves);
		
		tabelas.add("prc_tipo_situacao");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tipo_situacao");
		tabelasSincronizadas.put("prc_tipo_situacao", chaves);
		
		tabelas.add("prc_tipo_processo");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tipo_processo");
		tabelasSincronizadas.put("prc_tipo_processo", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("prc_area_direito");
		tabelaEstrangeira.add("cd_area_direito");
		chavesEstrangeiras.put("cd_area_direito", tabelaEstrangeira);
		tabelasSincronizadas.put("prc_tipo_processoRef", chavesEstrangeiras);
		
		tabelas.add("prc_orgao_judicial");
		chaves  = new ArrayList<String>();
		chaves.add("cd_orgao_judicial");
		chaves.add("cd_comarca");
		tabelasSincronizadas.put("prc_orgao_judicial", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("prc_comarca");
		tabelaEstrangeira.add("cd_comarca");
		chavesEstrangeiras.put("cd_comarca", tabelaEstrangeira);
		tabelasSincronizadas.put("prc_orgao_judicialRef", chavesEstrangeiras);
		
		tabelas.add("prc_orgao");
		chaves  = new ArrayList<String>();
		chaves.add("cd_orgao");
		tabelasSincronizadas.put("prc_orgao", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("prc_tipo_orgao");
		tabelaEstrangeira.add("cd_tipo_orgao");
		chavesEstrangeiras.put("cd_tipo_orgao", tabelaEstrangeira);
		tabelasSincronizadas.put("prc_orgaoRef", chavesEstrangeiras);
		
		tabelas.add("bpm_referencia");
		chaves  = new ArrayList<String>();
		chaves.add("cd_referencia");
		tabelasSincronizadas.put("bpm_referencia", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("bpm_bem");
		tabelaEstrangeira.add("cd_bem");
		chavesEstrangeiras.put("cd_bem", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("bpm_marca");
		tabelaEstrangeira.add("cd_marca");
		chavesEstrangeiras.put("cd_marca", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_setor");
		tabelaEstrangeira.add("cd_setor");
		chavesEstrangeiras.put("cd_setor", tabelaEstrangeira);
		tabelasSincronizadas.put("bpm_referenciaRef", chavesEstrangeiras);
		
		tabelas.add("fta_rota");
		chaves  = new ArrayList<String>();
		chaves.add("cd_rota");
		tabelasSincronizadas.put("fta_rota", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_cidade");
		tabelaEstrangeira.add("cd_cidade");
		chavesEstrangeiras.put("cd_cidade_destino", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_cidade");
		tabelaEstrangeira.add("cd_cidade");
		chavesEstrangeiras.put("cd_cidade_origem", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_logradouro");
		tabelaEstrangeira.add("cd_logradouro");
		chavesEstrangeiras.put("cd_logradouro_destino", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_logradouro");
		tabelaEstrangeira.add("cd_logradouro");
		chavesEstrangeiras.put("cd_logradouro_origem", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fta_tipo_rota");
		tabelaEstrangeira.add("cd_tipo_rota");
		chavesEstrangeiras.put("cd_tipo_rota", tabelaEstrangeira);
		tabelasSincronizadas.put("fta_rotaRef", chavesEstrangeiras);
		
		tabelas.add("fta_motivo_viagem");
		chaves  = new ArrayList<String>();
		chaves.add("cd_motivo");
		tabelasSincronizadas.put("fta_motivo_viagem", chaves);
		
		tabelas.add("grl_modelo_documento");
		chaves  = new ArrayList<String>();
		chaves.add("cd_modelo");
		tabelasSincronizadas.put("grl_modelo_documento", chaves);
		
		tabelas.add("prc_grupo_processo");
		chaves  = new ArrayList<String>();
		chaves.add("cd_modelo");
		tabelasSincronizadas.put("prc_grupo_processo", chaves);
		
		tabelas.add("prc_processo");
		chaves  = new ArrayList<String>();
		chaves.add("cd_processo");
		tabelasSincronizadas.put("prc_processo", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_advogado_contrario", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_advogado", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("prc_grupo_processo");
		tabelaEstrangeira.add("cd_grupo_processo");
		chavesEstrangeiras.put("cd_grupo_processo", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("prc_orgao");
		tabelaEstrangeira.add("cd_orgao");
		chavesEstrangeiras.put("cd_orgao", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("prc_orgao_judicial");
		tabelaEstrangeira.add("cd_orgao_judicial");
		chavesEstrangeiras.put("cd_orgao_judicial", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("prc_tipo_processo");
		tabelaEstrangeira.add("cd_tipo_processo");
		chavesEstrangeiras.put("cd_tipo_processo", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("prc_tipo_situacao");
		tabelaEstrangeira.add("cd_tipo_situacao");
		chavesEstrangeiras.put("cd_tipo_situacao", tabelaEstrangeira);
		tabelasSincronizadas.put("prc_processoRef", chavesEstrangeiras);
		
		tabelas.add("pcb_tipo_tanque");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tipo_tanque");
		tabelasSincronizadas.put("pcb_tipo_tanque", chaves);
		
		tabelas.add("grl_formulario");
		chaves  = new ArrayList<String>();
		chaves.add("cd_formulario");
		tabelasSincronizadas.put("grl_formulario", chaves);
		
		tabelas.add("alm_grupo");
		chaves  = new ArrayList<String>();
		chaves.add("cd_grupo");
		tabelasSincronizadas.put("alm_grupo", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_categoria_economica");
		tabelaEstrangeira.add("cd_categoria_economica");
		chavesEstrangeiras.put("cd_categoria_despesa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_categoria_economica");
		tabelaEstrangeira.add("cd_categoria_economica");
		chavesEstrangeiras.put("cd_categoria_receita", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_formulario");
		tabelaEstrangeira.add("cd_formulario");
		chavesEstrangeiras.put("cd_formulario", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_grupo");
		tabelaEstrangeira.add("cd_grupo");
		chavesEstrangeiras.put("cd_grupo_superior", tabelaEstrangeira);
		tabelasSincronizadas.put("alm_grupoRef", chavesEstrangeiras);
		
		tabelas.add("grl_natureza_juridica");
		chaves  = new ArrayList<String>();
		chaves.add("cd_natureza_juridica");
		tabelasSincronizadas.put("grl_natureza_juridica", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_natureza_juridica");
		tabelaEstrangeira.add("cd_natureza_juridica");
		chavesEstrangeiras.put("cd_natureza_superior", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_natureza_juridicaRef", chavesEstrangeiras);
		
		tabelas.add("grl_pessoa_juridica");
		chaves  = new ArrayList<String>();
		chaves.add("cd_pessoa");
		tabelasSincronizadas.put("grl_pessoa_juridica", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_natureza_juridica");
		tabelaEstrangeira.add("cd_natureza_juridica");
		chavesEstrangeiras.put("cd_natureza_juridica", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_pessoa", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_pessoa_juridicaRef", chavesEstrangeiras);
		
		tabelas.add("grl_empresa");
		chaves  = new ArrayList<String>();
		chaves.add("cd_empresa");
		tabelasSincronizadas.put("grl_empresa", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa_juridica");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_empresaRef", chavesEstrangeiras);
		
		tabelas.add("grl_moeda");
		chaves  = new ArrayList<String>();
		chaves.add("cd_moeda");
		tabelasSincronizadas.put("grl_moeda", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_indicador");
		tabelaEstrangeira.add("cd_indicador");
		chavesEstrangeiras.put("cd_indicador", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_moedaRef", chavesEstrangeiras);
				
		tabelas.add("adm_tabela_preco");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tabela_preco");
		tabelasSincronizadas.put("adm_tabela_preco", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_moeda");
		tabelaEstrangeira.add("cd_moeda");
		chavesEstrangeiras.put("cd_moeda", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_tabela_precoRef", chavesEstrangeiras);
		
		tabelas.add("fta_tipo_veiculo");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tipo_veiculo");
		tabelasSincronizadas.put("fta_tipo_veiculo", chaves);
		
		tabelas.add("fta_reboque");
		chaves  = new ArrayList<String>();
		chaves.add("cd_reboque");
		tabelasSincronizadas.put("fta_reboque", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("bpm_referencia");
		tabelaEstrangeira.add("cd_referencia");
		chavesEstrangeiras.put("cd_reboque", tabelaEstrangeira);
		tabelasSincronizadas.put("fta_reboqueRef", chavesEstrangeiras);
		
		tabelas.add("fta_modelo_veiculo");
		chaves  = new ArrayList<String>();
		chaves.add("cd_modelo");
		tabelasSincronizadas.put("fta_modelo_veiculo", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("bpm_marca");
		tabelaEstrangeira.add("cd_marca");
		chavesEstrangeiras.put("cd_marca", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("bpm_bem");
		tabelaEstrangeira.add("cd_bem");
		chavesEstrangeiras.put("cd_modelo", tabelaEstrangeira);
		tabelasSincronizadas.put("fta_modelo_veiculoRef", chavesEstrangeiras);
		
		tabelas.add("fta_veiculo");
		chaves  = new ArrayList<String>();
		chaves.add("cd_veiculo");
		tabelasSincronizadas.put("fta_veiculo", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fta_modelo_veiculo");
		tabelaEstrangeira.add("cd_modelo");
		chavesEstrangeiras.put("cd_modelo", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_proprietario", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fta_reboque");
		tabelaEstrangeira.add("cd_reboque");
		chavesEstrangeiras.put("cd_reboque", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fta_tipo_veiculo");
		tabelaEstrangeira.add("cd_tipo_veiculo");
		chavesEstrangeiras.put("cd_tipo_veiculo", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("bpm_referencia");
		tabelaEstrangeira.add("cd_referencia");
		chavesEstrangeiras.put("cd_veiculo", tabelaEstrangeira);
		tabelasSincronizadas.put("fta_veiculoRef", chavesEstrangeiras);
		
		tabelas.add("seg_pergunta_secreta");
		chaves  = new ArrayList<String>();
		chaves.add("cd_pergunta_secreta");
		tabelasSincronizadas.put("seg_pergunta_secreta", chaves);
		
		tabelas.add("seg_usuario");
		chaves  = new ArrayList<String>();
		chaves.add("cd_usuario");
		tabelasSincronizadas.put("seg_usuario", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("seg_pergunta_secreta");
		tabelaEstrangeira.add("cd_pergunta_secreta");
		chavesEstrangeiras.put("cd_pergunta_secreta", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_pessoa", tabelaEstrangeira);
		tabelasSincronizadas.put("seg_usuarioRef", chavesEstrangeiras);
		
		tabelas.add("adm_turno");
		chaves  = new ArrayList<String>();
		chaves.add("cd_turno");
		tabelasSincronizadas.put("adm_turno", chaves);
		
		tabelas.add("seg_sistema");
		chaves  = new ArrayList<String>();
		chaves.add("cd_sistema");
		tabelasSincronizadas.put("seg_sistema", chaves);
		
		tabelas.add("adm_tabela_preco_regra");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tabela_preco");
		chaves.add("cd_regra");
		tabelasSincronizadas.put("adm_tabela_preco_regra", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_fornecedor", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_grupo");
		tabelaEstrangeira.add("cd_grupo");
		chavesEstrangeiras.put("cd_grupo", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tabela_preco");
		tabelaEstrangeira.add("cd_tabela_preco");
		chavesEstrangeiras.put("cd_tabela_preco", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tabela_preco");
		tabelaEstrangeira.add("cd_tabela_preco");
		chavesEstrangeiras.put("cd_tabela_preco_base", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_tabela_preco_regraRef", chavesEstrangeiras);
		
		tabelas.add("grl_tipo_arquivo");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tipo_arquivo");
		tabelasSincronizadas.put("grl_tipo_arquivo", chaves);
		
		tabelas.add("fta_viagem");
		chaves  = new ArrayList<String>();
		chaves.add("cd_viagem");
		tabelasSincronizadas.put("fta_viagem", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fta_motivo_viagem");
		tabelaEstrangeira.add("cd_motivo");
		chavesEstrangeiras.put("cd_motivo", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fta_rota");
		tabelaEstrangeira.add("cd_rota");
		chavesEstrangeiras.put("cd_rota", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fta_veiculo");
		tabelaEstrangeira.add("cd_veiculo");
		chavesEstrangeiras.put("cd_veiculo", tabelaEstrangeira);
		tabelasSincronizadas.put("fta_viagemRef", chavesEstrangeiras);
		
		tabelas.add("grl_agencia");
		chaves  = new ArrayList<String>();
		chaves.add("cd_agencia");
		tabelasSincronizadas.put("grl_agencia", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_natureza_juridica");
		tabelaEstrangeira.add("cd_natureza_juridica");
		chavesEstrangeiras.put("cd_natureza_juridica", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_agenciaRef", chavesEstrangeiras);
		
		tabelas.add("adm_conta_financeira");
		chaves  = new ArrayList<String>();
		chaves.add("cd_conta");
		tabelasSincronizadas.put("adm_conta_financeira", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_agencia");
		tabelaEstrangeira.add("cd_agencia");
		chavesEstrangeiras.put("cd_agencia", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_responsavel", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_turno");
		tabelaEstrangeira.add("cd_turno");
		chavesEstrangeiras.put("cd_turno", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_conta_financeiraRef", chavesEstrangeiras);
		
		tabelas.add("adm_conta_fechamento");
		chaves  = new ArrayList<String>();
		chaves.add("cd_conta");
		chaves.add("cd_fechamento");
		tabelasSincronizadas.put("adm_conta_fechamento", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_conta_financeira");
		tabelaEstrangeira.add("cd_conta");
		chavesEstrangeiras.put("cd_conta", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("seg_usuario");
		tabelaEstrangeira.add("cd_usuario");
		chavesEstrangeiras.put("cd_responsavel", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("seg_usuario");
		tabelaEstrangeira.add("cd_usuario");
		chavesEstrangeiras.put("cd_supervisor", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_turno");
		tabelaEstrangeira.add("cd_turno");
		chavesEstrangeiras.put("cd_turno", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_conta_fechamentoRef", chavesEstrangeiras);
		
		tabelas.add("grl_banco");
		chaves  = new ArrayList<String>();
		chaves.add("cd_banco");
		tabelasSincronizadas.put("grl_banco", chaves);
		
		tabelas.add("grl_indicador");
		chaves  = new ArrayList<String>();
		chaves.add("cd_indicador");
		tabelasSincronizadas.put("grl_indicador", chaves);
		
		tabelas.add("adm_cheque");
		chaves  = new ArrayList<String>();
		chaves.add("cd_cheque");
		tabelasSincronizadas.put("adm_cheque", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_conta_financeira");
		tabelaEstrangeira.add("cd_conta");
		chavesEstrangeiras.put("cd_conta", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_chequeRef", chavesEstrangeiras);
		
		tabelas.add("adm_modelo_contrato");
		chaves  = new ArrayList<String>();
		chaves.add("cd_modelo_contrato");
		tabelasSincronizadas.put("adm_modelo_contrato", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_indicador");
		tabelaEstrangeira.add("cd_indicador");
		chavesEstrangeiras.put("cd_indicador", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_modelo_documento");
		tabelaEstrangeira.add("cd_modelo");
		chavesEstrangeiras.put("cd_modelo_contrato", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_modelo_contratoRef", chavesEstrangeiras);
		
		tabelas.add("grl_arquivo");
		chaves  = new ArrayList<String>();
		chaves.add("cd_arquivo");
		tabelasSincronizadas.put("grl_arquivo", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_tipo_arquivo");
		tabelaEstrangeira.add("cd_tipo_arquivo");
		chavesEstrangeiras.put("cd_tipo_arquivo", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_arquivoRef", chavesEstrangeiras);
		
		tabelas.add("ptc_tipo_documento");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tipo_documento");
		tabelasSincronizadas.put("ptc_tipo_documento", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_formulario");
		tabelaEstrangeira.add("cd_formulario");
		chavesEstrangeiras.put("cd_formulario", tabelaEstrangeira);
		tabelasSincronizadas.put("ptc_tipo_documentoRef", chavesEstrangeiras);
		
		tabelas.add("ptc_documento");
		chaves  = new ArrayList<String>();
		chaves.add("cd_documento");
		tabelasSincronizadas.put("ptc_documento", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_arquivo");
		tabelaEstrangeira.add("cd_arquivo");
		chavesEstrangeiras.put("cd_arquivo", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("prc_processo");
		tabelaEstrangeira.add("cd_processo");
		chavesEstrangeiras.put("cd_processo", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_setor");
		tabelaEstrangeira.add("cd_setor");
		chavesEstrangeiras.put("cd_setor", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("ptc_tipo_documento");
		tabelaEstrangeira.add("cd_tipo_documento");
		chavesEstrangeiras.put("cd_tipo_documento", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("seg_usuario");
		tabelaEstrangeira.add("cd_usuario");
		chavesEstrangeiras.put("cd_usuario", tabelaEstrangeira);
		tabelasSincronizadas.put("ptc_documentoRef", chavesEstrangeiras);
		
		tabelas.add("alm_nivel_local");
		chaves  = new ArrayList<String>();
		chaves.add("cd_nivel_local");
		tabelasSincronizadas.put("alm_nivel_local", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_nivel_local");
		tabelaEstrangeira.add("cd_nivel_local");
		chavesEstrangeiras.put("cd_nivel_local_superior", tabelaEstrangeira);
		tabelasSincronizadas.put("alm_nivel_localRef", chavesEstrangeiras);
		
		tabelas.add("alm_local_armazenamento");
		chaves  = new ArrayList<String>();
		chaves.add("cd_local_armazenamento");
		tabelasSincronizadas.put("alm_local_armazenamento", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_local_armazenamento");
		tabelaEstrangeira.add("cd_local_armazenamento");
		chavesEstrangeiras.put("cd_local_armazenamento_superior", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_nivel_local");
		tabelaEstrangeira.add("cd_nivel_local");
		chavesEstrangeiras.put("cd_nivel_local", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_responsavel", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_setor");
		tabelaEstrangeira.add("cd_setor");
		chavesEstrangeiras.put("cd_setor", tabelaEstrangeira);
		tabelasSincronizadas.put("alm_local_armazenamentoRef", chavesEstrangeiras);
		
		tabelas.add("pcb_tanque");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tanque");
		tabelasSincronizadas.put("pcb_tanque", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_local_armazenamento");
		tabelaEstrangeira.add("cd_local_armazenamento");
		chavesEstrangeiras.put("cd_tanque", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("pcb_tipo_tanque");
		tabelaEstrangeira.add("cd_tipo_tanque");
		chavesEstrangeiras.put("cd_tipo_tanque", tabelaEstrangeira);
		tabelasSincronizadas.put("pcb_tanqueRef", chavesEstrangeiras);
		
		tabelas.add("pcb_bombas");
		chaves  = new ArrayList<String>();
		chaves.add("cd_bomba");
		tabelasSincronizadas.put("pcb_bombas", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa_juridica");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_fabricante", tabelaEstrangeira);
		tabelasSincronizadas.put("pcb_bombasRef", chavesEstrangeiras);
		
		tabelas.add("grl_bairro");
		chaves  = new ArrayList<String>();
		chaves.add("cd_bairro");
		tabelasSincronizadas.put("grl_bairro", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_distrito");
		tabelaEstrangeira.add("cd_distrito");
		chavesEstrangeiras.put("cd_distrito", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_bairroRef", chavesEstrangeiras);
		
		tabelas.add("grl_tipo_endereco");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tipo_endereco");
		tabelasSincronizadas.put("grl_tipo_endereco", chaves);
		
		tabelas.add("grl_pessoa_endereco");
		chaves  = new ArrayList<String>();
		chaves.add("cd_endereco");
		chaves.add("cd_pessoa");
		tabelasSincronizadas.put("grl_pessoa_endereco", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_bairro");
		tabelaEstrangeira.add("cd_bairro");
		chavesEstrangeiras.put("cd_bairro", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_cidade");
		tabelaEstrangeira.add("cd_cidade");
		chavesEstrangeiras.put("cd_cidade", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_logradouro");
		tabelaEstrangeira.add("cd_logradouro");
		chavesEstrangeiras.put("cd_logradouro", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_pessoa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_tipo_endereco");
		tabelaEstrangeira.add("cd_tipo_endereco");
		chavesEstrangeiras.put("cd_tipo_endereco", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_tipo_logradouro");
		tabelaEstrangeira.add("cd_tipo_logradouro");
		chavesEstrangeiras.put("cd_tipo_logradouro", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_pessoa_enderecoRef", chavesEstrangeiras);
		
		tabelas.add("adm_plano_pagamento");
		chaves  = new ArrayList<String>();
		chaves.add("cd_plano_pagamento");
		tabelasSincronizadas.put("adm_plano_pagamento", chaves);
		
		tabelas.add("adm_tipo_operacao");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tipo_operacao");
		tabelasSincronizadas.put("adm_tipo_operacao", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tabela_preco");
		tabelaEstrangeira.add("cd_tabela_preco");
		chavesEstrangeiras.put("cd_tabela_preco", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_tipo_operacaoRef", chavesEstrangeiras);
		
		tabelas.add("grl_escolaridade");
		chaves  = new ArrayList<String>();
		chaves.add("cd_escolaridade");
		tabelasSincronizadas.put("grl_escolaridade", chaves);
		
		tabelas.add("grl_pessoa_fisica");
		chaves  = new ArrayList<String>();
		chaves.add("cd_pessoa");
		tabelasSincronizadas.put("grl_pessoa_fisica", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_escolaridade");
		tabelaEstrangeira.add("cd_escolaridade");
		chavesEstrangeiras.put("cd_escolaridade", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_estado");
		tabelaEstrangeira.add("cd_estado");
		chavesEstrangeiras.put("cd_estado_rg", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_cidade");
		tabelaEstrangeira.add("cd_cidade");
		chavesEstrangeiras.put("cd_naturalidade", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_pessoa", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_pessoa_fisicaRef", chavesEstrangeiras);
		
		tabelas.add("adm_pedido_venda");
		chaves  = new ArrayList<String>();
		chaves.add("cd_pedido_venda");
		tabelasSincronizadas.put("adm_pedido_venda", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_cliente", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa_endereco");
		tabelaEstrangeira.add("cd_endereco");
		chavesEstrangeiras.put("cd_endereco_cobranca", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa_endereco");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_cliente", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_plano_pagamento");
		tabelaEstrangeira.add("cd_plano_pagamento");
		chavesEstrangeiras.put("cd_plano_pagamento", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tipo_operacao");
		tabelaEstrangeira.add("cd_tipo_operacao");
		chavesEstrangeiras.put("cd_tipo_operacao", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa_fisica");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_vendedor", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_pedido_vendaRef", chavesEstrangeiras);
		
		tabelas.add("seg_modulo");
		chaves  = new ArrayList<String>();
		chaves.add("cd_modulo");
		chaves.add("cd_sistema");
		tabelasSincronizadas.put("seg_modulo", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("seg_sistema");
		tabelaEstrangeira.add("cd_sistema");
		chavesEstrangeiras.put("cd_sistema", tabelaEstrangeira);
		tabelasSincronizadas.put("seg_moduloRef", chavesEstrangeiras);
		
		tabelas.add("adm_tributo");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tributo");
		tabelasSincronizadas.put("adm_tributo", chaves);
		
		tabelas.add("adm_tributo_aliquota");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tributo_aliquota");
		chaves.add("cd_tributo");
		tabelasSincronizadas.put("adm_tributo_aliquota", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tributo");
		tabelaEstrangeira.add("cd_tributo");
		chavesEstrangeiras.put("cd_tributo", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_tributo_aliquotaRef", chavesEstrangeiras);
		
		tabelas.add("fsc_motivo_cancelamento");
		chaves  = new ArrayList<String>();
		chaves.add("cd_motivo_cancelamento");
		tabelasSincronizadas.put("fsc_motivo_cancelamento", chaves);
		
		tabelas.add("fsc_tipo_origem");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tipo_origem");
		tabelasSincronizadas.put("fsc_tipo_origem", chaves);
		
		tabelas.add("grl_produto_servico_empresa");
		chaves  = new ArrayList<String>();
		chaves.add("cd_empresa");
		chaves.add("cd_produto_servico");
		tabelasSincronizadas.put("grl_produto_servico_empresa", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fsc_tipo_origem");
		tabelaEstrangeira.add("cd_tipo_origem");
		chavesEstrangeiras.put("cd_tipo_origem", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_unidade_medida");
		tabelaEstrangeira.add("cd_unidade_medida");
		chavesEstrangeiras.put("cd_unidade_medida", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_produto_servico_empresaRef", chavesEstrangeiras);
		
		tabelas.add("adm_produto_servico_preco");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tabela_preco");
		chaves.add("cd_produto_servico");
		chaves.add("cd_produto_servico_preco");
		tabelasSincronizadas.put("adm_produto_servico_preco", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tabela_preco");
		tabelaEstrangeira.add("cd_tabela_preco");
		chavesEstrangeiras.put("cd_tabela_preco", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_produto_servico_precoRef", chavesEstrangeiras);
		
		tabelas.add("adm_pedido_venda_item");
		chaves  = new ArrayList<String>();
		chaves.add("cd_pedido_venda");
		chaves.add("cd_empresa");
		chaves.add("cd_produto_servico");
		tabelasSincronizadas.put("adm_pedido_venda_item", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico_empresa");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_pedido_venda");
		tabelaEstrangeira.add("cd_pedido_venda");
		chavesEstrangeiras.put("cd_pedido_venda", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_produto_servico_preco");
		tabelaEstrangeira.add("cd_tabela_preco");
		chavesEstrangeiras.put("cd_tabela_preco", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_produto_servico_preco");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_produto_servico_preco");
		tabelaEstrangeira.add("cd_produto_servico_preco");
		chavesEstrangeiras.put("cd_produto_servico_preco", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tabela_preco_regra");
		tabelaEstrangeira.add("cd_tabela_preco");
		chavesEstrangeiras.put("cd_tabela_preco_promocao", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tabela_preco_regra");
		tabelaEstrangeira.add("cd_regra");
		chavesEstrangeiras.put("cd_regra_promocao", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_unidade_medida");
		tabelaEstrangeira.add("cd_unidade_medida");
		chavesEstrangeiras.put("cd_unidade_medida", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_pedido_venda_itemRef", chavesEstrangeiras);
		
		tabelas.add("adm_classificacao_cliente");
		chaves  = new ArrayList<String>();
		chaves.add("cd_classificacao_cliente");
		tabelasSincronizadas.put("adm_classificacao_cliente", chaves);
		
		tabelas.add("adm_forma_pagamento");
		chaves  = new ArrayList<String>();
		chaves.add("cd_forma_pagamento");
		tabelasSincronizadas.put("adm_forma_pagamento", chaves);
		
		tabelas.add("adm_forma_plano_pagamento");
		chaves  = new ArrayList<String>();
		chaves.add("cd_forma_pagamento");
		chaves.add("cd_empresa");
		chaves.add("cd_plano_pagamento");
		tabelasSincronizadas.put("adm_forma_plano_pagamento", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_forma_pagamento");
		tabelaEstrangeira.add("cd_forma_pagamento");
		chavesEstrangeiras.put("cd_forma_pagamento", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_plano_pagamento");
		tabelaEstrangeira.add("cd_plano_pagamento");
		chavesEstrangeiras.put("cd_plano_pagamento", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_forma_plano_pagamentoRef", chavesEstrangeiras);
		
		tabelas.add("adm_faixa_renda");
		chaves  = new ArrayList<String>();
		chaves.add("cd_faixa_renda");
		tabelasSincronizadas.put("adm_faixa_renda", chaves);
		
		tabelas.add("grl_vinculo");
		chaves  = new ArrayList<String>();
		chaves.add("cd_vinculo");
		tabelasSincronizadas.put("grl_vinculo", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_formulario");
		tabelaEstrangeira.add("cd_formulario");
		chavesEstrangeiras.put("cd_formulario", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_vinculoRef", chavesEstrangeiras);
		
		tabelas.add("srh_funcao");
		chaves  = new ArrayList<String>();
		chaves.add("cd_funcao");
		tabelasSincronizadas.put("srh_funcao", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelasSincronizadas.put("srh_funcaoRef", chavesEstrangeiras);
		
		tabelas.add("grl_nivel_acesso");
		chaves  = new ArrayList<String>();
		chaves.add("cd_nivel_acesso");
		tabelasSincronizadas.put("grl_nivel_acesso", chaves);
		
		tabelas.add("adm_movimento_conta");
		chaves  = new ArrayList<String>();
		chaves.add("cd_movimento_conta");
		chaves.add("cd_conta");
		tabelasSincronizadas.put("adm_movimento_conta", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_cheque");
		tabelaEstrangeira.add("cd_cheque");
		chavesEstrangeiras.put("cd_cheque", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_conta_financeira");
		tabelaEstrangeira.add("cd_conta");
		chavesEstrangeiras.put("cd_conta", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_forma_pagamento");
		tabelaEstrangeira.add("cd_forma_pagamento");
		chavesEstrangeiras.put("cd_forma_pagamento", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_movimento_conta");
		tabelaEstrangeira.add("cd_movimento_conta");
		chavesEstrangeiras.put("cd_movimento_conta_origem", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_movimento_conta");
		tabelaEstrangeira.add("cd_conta");
		chavesEstrangeiras.put("cd_conta_origem", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_turno");
		tabelaEstrangeira.add("cd_turno");
		chavesEstrangeiras.put("cd_turno", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("seg_usuario");
		tabelaEstrangeira.add("cd_usuario");
		chavesEstrangeiras.put("cd_usuario", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fta_viagem");
		tabelaEstrangeira.add("cd_viagem");
		chavesEstrangeiras.put("cd_viagem", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_movimento_contaRef", chavesEstrangeiras);
		
		tabelas.add("adm_solicitacao_material");
		chaves  = new ArrayList<String>();
		chaves.add("cd_solicitacao_material");
		tabelasSincronizadas.put("adm_solicitacao_material", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("ptc_documento");
		tabelaEstrangeira.add("cd_documento");
		chavesEstrangeiras.put("cd_documento", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_setor");
		tabelaEstrangeira.add("cd_setor");
		chavesEstrangeiras.put("cd_setor_solicitante", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa_fisica");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_solicitante", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_solicitacao_materialRef", chavesEstrangeiras);
		
		tabelas.add("adm_conta_carteira");
		chaves  = new ArrayList<String>();
		chaves.add("cd_conta_carteira");
		chaves.add("cd_conta");
		tabelasSincronizadas.put("adm_conta_carteira", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_conta_financeira");
		tabelaEstrangeira.add("cd_conta");
		chavesEstrangeiras.put("cd_conta", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_conta_carteiraRef", chavesEstrangeiras);
		
		tabelas.add("adm_contrato");
		chaves  = new ArrayList<String>();
		chaves.add("cd_contrato");
		tabelasSincronizadas.put("adm_contrato", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_agente", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_categoria_economica");
		tabelaEstrangeira.add("cd_categoria_economica");
		chavesEstrangeiras.put("cd_categoria_economica", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_conta_carteira");
		tabelaEstrangeira.add("cd_conta_carteira");
		chavesEstrangeiras.put("cd_conta_carteira", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_conta_carteira");
		tabelaEstrangeira.add("cd_conta");
		chavesEstrangeiras.put("cd_conta", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_contrato");
		tabelaEstrangeira.add("cd_contrato");
		chavesEstrangeiras.put("cd_convenio", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("ptc_documento");
		tabelaEstrangeira.add("cd_documento");
		chavesEstrangeiras.put("cd_documento", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_indicador");
		tabelaEstrangeira.add("cd_indicador");
		chavesEstrangeiras.put("cd_indicador", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_modelo_contrato");
		tabelaEstrangeira.add("cd_modelo_contrato");
		chavesEstrangeiras.put("cd_modelo_contrato", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_pessoa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tipo_operacao");
		tabelaEstrangeira.add("cd_tipo_operacao");
		chavesEstrangeiras.put("cd_tipo_operacao", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_contratoRef", chavesEstrangeiras);
		
		tabelas.add("adm_natureza_operacao");
		chaves  = new ArrayList<String>();
		chaves.add("cd_natureza_operacao");
		tabelasSincronizadas.put("adm_natureza_operacao", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_natureza_operacao");
		tabelaEstrangeira.add("cd_natureza_operacao");
		chavesEstrangeiras.put("cd_natureza_entrada", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_natureza_operacao");
		tabelaEstrangeira.add("cd_natureza_operacao");
		chavesEstrangeiras.put("cd_natureza_entrada_substituicao", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_natureza_operacao");
		tabelaEstrangeira.add("cd_natureza_operacao");
		chavesEstrangeiras.put("cd_natureza_superior", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_natureza_operacaoRef", chavesEstrangeiras);
		
		tabelas.add("pcb_bico");
		chaves  = new ArrayList<String>();
		chaves.add("cd_bico");
		tabelasSincronizadas.put("pcb_bico", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("pcb_bombas");
		tabelaEstrangeira.add("cd_bomba");
		chavesEstrangeiras.put("cd_bomba", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tabela_preco");
		tabelaEstrangeira.add("cd_tabela_preco");
		chavesEstrangeiras.put("cd_tabela_preco", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("pcb_tanque");
		tabelaEstrangeira.add("cd_tanque");
		chavesEstrangeiras.put("cd_tanque", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_turno");
		tabelaEstrangeira.add("cd_turno");
		chavesEstrangeiras.put("cd_turno", tabelaEstrangeira);
		tabelasSincronizadas.put("pcb_bicoRef", chavesEstrangeiras);
		
		tabelas.add("grl_tipo_documento");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tipo_documento");
		tabelasSincronizadas.put("grl_tipo_documento", chaves);
		
		tabelas.add("alm_produto_referencia");
		chaves  = new ArrayList<String>();
		chaves.add("cd_referencia");
		chaves.add("cd_produto_servico");
		chaves.add("cd_empresa");
		tabelasSincronizadas.put("alm_produto_referencia", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico_empresa");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_produto_referencia");
		tabelaEstrangeira.add("cd_referencia");
		chavesEstrangeiras.put("cd_referencia_superior", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_produto_referencia");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_produto_referencia");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelasSincronizadas.put("alm_produto_referenciaRef", chavesEstrangeiras);
		
		tabelas.add("grl_produto_servico_composicao");
		chaves  = new ArrayList<String>();
		chaves.add("cd_composicao");
		tabelasSincronizadas.put("grl_produto_servico_composicao", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico_empresa");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_produto_referencia");
		tabelaEstrangeira.add("cd_referencia");
		chavesEstrangeiras.put("cd_referencia", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_produto_referencia");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico_componente", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_produto_referencia");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_unidade_medida");
		tabelaEstrangeira.add("cd_unidade_medida");
		chavesEstrangeiras.put("cd_unidade_medida", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_produto_servico_composicaoRef", chavesEstrangeiras);
		
		tabelas.add("grl_produto_similar");
		chaves  = new ArrayList<String>();
		chaves.add("cd_produto_servico");
		chaves.add("cd_similar");
		tabelasSincronizadas.put("grl_produto_similar", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_similar", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_produto_similarRef", chavesEstrangeiras);
		
		tabelas.add("alm_produto_estoque");
		chaves  = new ArrayList<String>();
		chaves.add("cd_local_armazenamento");
		chaves.add("cd_produto_servico");
		chaves.add("cd_empresa");
		tabelasSincronizadas.put("alm_produto_estoque", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico_empresa");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_local_armazenamento");
		tabelaEstrangeira.add("cd_local_armazenamento");
		chavesEstrangeiras.put("cd_local_armazenamento", tabelaEstrangeira);
		tabelasSincronizadas.put("alm_produto_estoqueRef", chavesEstrangeiras);
		
		tabelas.add("alm_produto_grupo");
		chaves  = new ArrayList<String>();
		chaves.add("cd_produto_servico");
		chaves.add("cd_grupo");
		chaves.add("cd_empresa");
		tabelasSincronizadas.put("alm_produto_grupo", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico_empresa");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_grupo");
		tabelaEstrangeira.add("cd_grupo");
		chavesEstrangeiras.put("cd_grupo", tabelaEstrangeira);
		tabelasSincronizadas.put("alm_produto_grupoRef", chavesEstrangeiras);
		
		tabelas.add("adm_produto_fornecedor");
		chaves  = new ArrayList<String>();
		chaves.add("cd_fornecedor");
		chaves.add("cd_empresa");
		chaves.add("cd_produto_servico");
		tabelasSincronizadas.put("adm_produto_fornecedor", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico_empresa");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_fornecedor", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_moeda");
		tabelaEstrangeira.add("cd_moeda");
		chavesEstrangeiras.put("cd_moeda", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_representante", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_unidade_medida");
		tabelaEstrangeira.add("cd_unidade_medida");
		chavesEstrangeiras.put("cd_unidade_medida", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_produto_fornecedorRef", chavesEstrangeiras);
		
		tabelas.add("adm_produto_servico_tributo");
		chaves  = new ArrayList<String>();
		chaves.add("cd_produto_servico_tributo");
		chaves.add("cd_tributo_aliquota");
		chaves.add("cd_tributo");
		tabelasSincronizadas.put("adm_produto_servico_tributo", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_cidade");
		tabelaEstrangeira.add("cd_cidade");
		chavesEstrangeiras.put("cd_cidade", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_classificacao_fiscal");
		tabelaEstrangeira.add("cd_classificacao_fiscal");
		chavesEstrangeiras.put("cd_classificacao_fiscal", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_estado");
		tabelaEstrangeira.add("cd_estado");
		chavesEstrangeiras.put("cd_estado", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_natureza_operacao");
		tabelaEstrangeira.add("cd_natureza_operacao");
		chavesEstrangeiras.put("cd_natureza_operacao", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pais");
		tabelaEstrangeira.add("cd_pais");
		chavesEstrangeiras.put("cd_pais", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tributo_aliquota");
		tabelaEstrangeira.add("cd_tributo_aliquota");
		chavesEstrangeiras.put("cd_tributo_aliquota", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tributo_aliquota");
		tabelaEstrangeira.add("cd_tributo");
		chavesEstrangeiras.put("cd_tributo", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_produto_servico_tributoRef", chavesEstrangeiras);
		
		tabelas.add("adm_plano_pagto_produto_servico");
		chaves  = new ArrayList<String>();
		chaves.add("cd_plano_pagto_produto_servico");
		chaves.add("cd_plano_pagamento");
		tabelasSincronizadas.put("adm_plano_pagto_produto_servico", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico_empresa");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_forma_pagamento");
		tabelaEstrangeira.add("cd_forma_pagamento");
		chavesEstrangeiras.put("cd_forma_pagamento", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_grupo");
		tabelaEstrangeira.add("cd_grupo");
		chavesEstrangeiras.put("cd_grupo", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_plano_pagamento");
		tabelaEstrangeira.add("cd_plano_pagamento");
		chavesEstrangeiras.put("cd_plano_pagamento", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tabela_preco");
		tabelaEstrangeira.add("cd_tabela_preco");
		chavesEstrangeiras.put("cd_tabela_preco", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tipo_operacao");
		tabelaEstrangeira.add("cd_tipo_operacao");
		chavesEstrangeiras.put("cd_tipo_operacao", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_plano_pagto_produto_servicoRef", chavesEstrangeiras);
		
		tabelas.add("adm_plano_pagto_produto_fornec");
		chaves  = new ArrayList<String>();
		chaves.add("cd_plano_pagto_produto_fornec");
		tabelasSincronizadas.put("adm_plano_pagto_produto_fornec", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico_empresa");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_fornecedor", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_grupo");
		tabelaEstrangeira.add("cd_grupo");
		chavesEstrangeiras.put("cd_grupo", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_plano_pagamento");
		tabelaEstrangeira.add("cd_plano_pagamento");
		chavesEstrangeiras.put("cd_plano_pagamento", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_plano_pagto_produto_fornecRef", chavesEstrangeiras);
		
		tabelas.add("grl_pessoa_arquivo");
		chaves  = new ArrayList<String>();
		chaves.add("cd_arquivo");
		chaves.add("cd_pessoa");
		tabelasSincronizadas.put("grl_pessoa_arquivo", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_arquivo");
		tabelaEstrangeira.add("cd_arquivo");
		chavesEstrangeiras.put("cd_arquivo", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_nivel_acesso");
		tabelaEstrangeira.add("cd_nivel_acesso");
		chavesEstrangeiras.put("cd_nivel_acesso", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_pessoa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_setor");
		tabelaEstrangeira.add("cd_setor");
		chavesEstrangeiras.put("cd_setor", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_pessoa_arquivoRef", chavesEstrangeiras);
		
		tabelas.add("grl_pessoa_conta_bancaria");
		chaves  = new ArrayList<String>();
		chaves.add("cd_conta_bancaria");
		chaves.add("cd_pessoa");
		tabelasSincronizadas.put("grl_pessoa_conta_bancaria", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_banco");
		tabelaEstrangeira.add("cd_banco");
		chavesEstrangeiras.put("cd_banco", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_pessoa", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_pessoa_conta_bancariaRef", chavesEstrangeiras);
		
		tabelas.add("grl_pessoa_contato");
		chaves  = new ArrayList<String>();
		chaves.add("cd_pessoa_contato");
		chaves.add("cd_pessoa_juridica");
		tabelasSincronizadas.put("grl_pessoa_contato", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa_endereco");
		tabelaEstrangeira.add("cd_endereco");
		chavesEstrangeiras.put("cd_endereco", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa_endereco");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_pessoa_juridica", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("srh_funcao");
		tabelaEstrangeira.add("cd_funcao");
		chavesEstrangeiras.put("cd_funcao", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa_juridica");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_pessoa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("seg_usuario");
		tabelaEstrangeira.add("cd_usuario");
		chavesEstrangeiras.put("cd_usuario", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_pessoa_contatoRef", chavesEstrangeiras);
		
		tabelas.add("grl_pessoa_empresa");
		chaves  = new ArrayList<String>();
		chaves.add("cd_empresa");
		chaves.add("cd_pessoa");
		chaves.add("cd_vinculo");
		tabelasSincronizadas.put("grl_pessoa_empresa", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_pessoa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_vinculo");
		tabelaEstrangeira.add("cd_vinculo");
		chavesEstrangeiras.put("cd_vinculo", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_pessoa_empresaRef", chavesEstrangeiras);
		
		tabelas.add("grl_pessoa_orgao_credito");
		chaves  = new ArrayList<String>();
		chaves.add("cd_pessoa");
		chaves.add("cd_orgao_credito");
		chaves.add("dt_analise");
		tabelasSincronizadas.put("grl_pessoa_orgao_credito", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_orgao_credito", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_pessoa", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_pessoa_orgao_creditoRef", chavesEstrangeiras);
		
		tabelas.add("grl_pessoa_referencia");
		chaves  = new ArrayList<String>();
		chaves.add("cd_pessoa_referencia");
		tabelasSincronizadas.put("grl_pessoa_referencia", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_pessoa", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_pessoa_referenciaRef", chavesEstrangeiras);
		
		tabelas.add("adm_cliente");
		chaves  = new ArrayList<String>();
		chaves.add("cd_empresa");
		chaves.add("cd_pessoa");
		tabelasSincronizadas.put("adm_cliente", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_cidade");
		tabelaEstrangeira.add("cd_cidade");
		chavesEstrangeiras.put("cd_cidade", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_classificacao_cliente");
		tabelaEstrangeira.add("cd_classificacao_cliente");
		chavesEstrangeiras.put("cd_classificacao_cliente", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_classificacao_cliente");
		tabelaEstrangeira.add("cd_classificacao_cliente");
		chavesEstrangeiras.put("cd_classificacao_cliente", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_convenio", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_faixa_renda");
		tabelaEstrangeira.add("cd_faixa_renda");
		chavesEstrangeiras.put("cd_faixa_renda", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_pessoa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tabela_preco");
		tabelaEstrangeira.add("cd_tabela_preco");
		chavesEstrangeiras.put("cd_tabela_preco", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_tipo_logradouro");
		tabelaEstrangeira.add("cd_tipo_logradouro");
		chavesEstrangeiras.put("cd_tipo_logradouro", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_clienteRef", chavesEstrangeiras);
		
		tabelas.add("adm_cliente_pagamento");
		chaves  = new ArrayList<String>();
		chaves.add("cd_empresa");
		chaves.add("cd_plano_pagamento");
		chaves.add("cd_forma_pagamento");
		chaves.add("cd_pessoa");
		tabelasSincronizadas.put("adm_cliente_pagamento", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_cliente");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_cliente");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_pessoa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_forma_pagamento");
		tabelaEstrangeira.add("cd_forma_pagamento");
		chavesEstrangeiras.put("cd_forma_pagamento", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_plano_pagamento");
		tabelaEstrangeira.add("cd_plano_pagamento");
		chavesEstrangeiras.put("cd_plano_pagamento", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_cliente_pagamentoRef", chavesEstrangeiras);
		
		tabelas.add("adm_cliente_produto");
		chaves  = new ArrayList<String>();
		chaves.add("cd_empresa");
		chaves.add("cd_produto_servico");
		chaves.add("cd_pessoa");
		tabelasSincronizadas.put("adm_cliente_produto", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_cliente");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_cliente");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_pessoa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tabela_preco");
		tabelaEstrangeira.add("cd_tabela_preco");
		chavesEstrangeiras.put("cd_tabela_preco", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_cliente_produtoRef", chavesEstrangeiras);
			
		tabelas.add("grl_tipo_documento_pessoa");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tipo_documento");
		chaves.add("cd_pessoa");
		tabelasSincronizadas.put("grl_tipo_documento_pessoa", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_pessoa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_tipo_documento");
		tabelaEstrangeira.add("cd_tipo_documento");
		chavesEstrangeiras.put("cd_tipo_documento", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_tipo_documento_pessoaRef", chavesEstrangeiras);
		
		tabelas.add("alm_documento_saida");
		chaves  = new ArrayList<String>();
		chaves.add("cd_documento_saida");
		tabelasSincronizadas.put("alm_documento_saida", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_conta_financeira");
		tabelaEstrangeira.add("cd_conta");
		chavesEstrangeiras.put("cd_conta", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_contrato");
		tabelaEstrangeira.add("cd_contrato");
		chavesEstrangeiras.put("cd_contrato", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("seg_usuario");
		tabelaEstrangeira.add("cd_usuario");
		chavesEstrangeiras.put("cd_digitador", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_moeda");
		tabelaEstrangeira.add("cd_moeda");
		chavesEstrangeiras.put("cd_moeda", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_natureza_operacao");
		tabelaEstrangeira.add("cd_natureza_operacao");
		chavesEstrangeiras.put("cd_natureza_operacao", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_pessoa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("bpm_referencia");
		tabelaEstrangeira.add("cd_referencia");
		chavesEstrangeiras.put("cd_referencia_ecf", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_solicitacao_material");
		tabelaEstrangeira.add("cd_solicitacao_material");
		chavesEstrangeiras.put("cd_solicitacao_material", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tipo_operacao");
		tabelaEstrangeira.add("cd_tipo_operacao");
		chavesEstrangeiras.put("cd_tipo_operacao", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_transportadora", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_turno");
		tabelaEstrangeira.add("cd_turno");
		chavesEstrangeiras.put("cd_turno", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_vendedor", tabelaEstrangeira);
		tabelasSincronizadas.put("alm_documento_saidaRef", chavesEstrangeiras);
		
		tabelas.add("alm_documento_saida_item");
		chaves  = new ArrayList<String>();
		chaves.add("cd_documento_saida");
		chaves.add("cd_produto_servico");
		chaves.add("cd_empresa");
		chaves.add("cd_item");
		tabelasSincronizadas.put("alm_documento_saida_item", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("pcb_bico");
		tabelaEstrangeira.add("cd_bico");
		chavesEstrangeiras.put("cd_bico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_documento_saida");
		tabelaEstrangeira.add("cd_documento_saida");
		chavesEstrangeiras.put("cd_documento_saida", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_natureza_operacao");
		tabelaEstrangeira.add("cd_natureza_operacao");
		chavesEstrangeiras.put("cd_natureza_operacao", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico_empresa");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_produto_servico_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tabela_preco");
		tabelaEstrangeira.add("cd_tabela_preco");
		chavesEstrangeiras.put("cd_tabela_preco", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_unidade_medida");
		tabelaEstrangeira.add("cd_unidade_medida");
		chavesEstrangeiras.put("cd_unidade_medida", tabelaEstrangeira);
		tabelasSincronizadas.put("alm_documento_saida_itemRef", chavesEstrangeiras);
		
		tabelas.add("alm_saida_local_item");
		chaves  = new ArrayList<String>();
		chaves.add("cd_saida");
		chaves.add("cd_produto_servico");
		chaves.add("cd_documento_saida");
		chaves.add("cd_empresa");
		chaves.add("cd_local_armazenamento");
		chaves.add("cd_item");
		tabelasSincronizadas.put("alm_saida_local_item", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_documento_saida_item");
		tabelaEstrangeira.add("cd_documento_saida");
		chavesEstrangeiras.put("cd_documento_saida", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_documento_saida_item");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_documento_saida_item");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_documento_saida_item");
		tabelaEstrangeira.add("cd_item");
		chavesEstrangeiras.put("cd_item", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_local_armazenamento");
		tabelaEstrangeira.add("cd_local_armazenamento");
		chavesEstrangeiras.put("cd_local_armazenamento", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_pedido_venda");
		tabelaEstrangeira.add("cd_pedido_venda");
		chavesEstrangeiras.put("cd_pedido_venda", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_pedido_venda");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_pedido_venda");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_produto_referencia");
		tabelaEstrangeira.add("cd_referencia");
		chavesEstrangeiras.put("cd_referencia", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_produto_referencia");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_produto_referencia");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelasSincronizadas.put("alm_saida_local_itemRef", chavesEstrangeiras);
		
		tabelas.add("adm_venda_saida_item");
		chaves  = new ArrayList<String>();
		chaves.add("cd_pedido_venda");
		chaves.add("cd_empresa");
		chaves.add("cd_produto_servico");
		chaves.add("cd_documento_saida");
		tabelasSincronizadas.put("adm_venda_saida_item", chaves);
		
		tabelas.add("adm_plano_pagto_documento_saida");
		chaves  = new ArrayList<String>();
		chaves.add("cd_plano_pagamento");
		chaves.add("cd_documento_saida");
		chaves.add("cd_forma_pagamento");
		tabelasSincronizadas.put("adm_plano_pagto_documento_saida", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_documento_saida");
		tabelaEstrangeira.add("cd_documento_saida");
		chavesEstrangeiras.put("cd_documento_saida", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_forma_pagamento");
		tabelaEstrangeira.add("cd_forma_pagamento");
		chavesEstrangeiras.put("cd_forma_pagamento", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_plano_pagamento");
		tabelaEstrangeira.add("cd_plano_pagamento");
		chavesEstrangeiras.put("cd_plano_pagamento", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("seg_usuario");
		tabelaEstrangeira.add("cd_usuario");
		chavesEstrangeiras.put("cd_usuario", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_plano_pagto_documento_saidaRef", chavesEstrangeiras);
		
		tabelas.add("alm_balanco");
		chaves  = new ArrayList<String>();
		chaves.add("cd_balanco");
		chaves.add("cd_empresa");
		tabelasSincronizadas.put("alm_balanco", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_grupo");
		tabelaEstrangeira.add("cd_grupo");
		chavesEstrangeiras.put("cd_grupo", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_local_armazenamento");
		tabelaEstrangeira.add("cd_local_armazenamento");
		chavesEstrangeiras.put("cd_local_armazenamento", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_pessoa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("seg_usuario");
		tabelaEstrangeira.add("cd_usuario");
		chavesEstrangeiras.put("cd_usuario", tabelaEstrangeira);
		tabelasSincronizadas.put("alm_balancoRef", chavesEstrangeiras);
		
		tabelas.add("alm_balanco_doc_saida");
		chaves  = new ArrayList<String>();
		chaves.add("cd_balanco");
		chaves.add("cd_empresa");
		chaves.add("cd_documento_saida");
		tabelasSincronizadas.put("alm_balanco_doc_saida", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_balanco");
		tabelaEstrangeira.add("cd_balanco");
		chavesEstrangeiras.put("cd_balanco", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_documento_saida");
		tabelaEstrangeira.add("cd_documento_saida");
		chavesEstrangeiras.put("cd_documento_saida", tabelaEstrangeira);
		tabelasSincronizadas.put("alm_balanco_doc_saidaRef", chavesEstrangeiras);
		
		tabelas.add("adm_saida_item_aliquota");
		chaves  = new ArrayList<String>();
		chaves.add("cd_produto_servico");
		chaves.add("cd_documento_saida");
		chaves.add("cd_empresa");
		chaves.add("cd_tributo_aliquota");
		chaves.add("cd_tributo");
		chaves.add("cd_item");
		tabelasSincronizadas.put("adm_saida_item_aliquota", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_documento_saida_item");
		tabelaEstrangeira.add("cd_documento_saida");
		chavesEstrangeiras.put("cd_documento_saida", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_documento_saida_item");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_documento_saida_item");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_documento_saida_item");
		tabelaEstrangeira.add("cd_item");
		chavesEstrangeiras.put("cd_item", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tributo_aliquota");
		tabelaEstrangeira.add("cd_tributo_aliquota");
		chavesEstrangeiras.put("cd_tributo_aliquota", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tributo_aliquota");
		tabelaEstrangeira.add("cd_tributo");
		chavesEstrangeiras.put("cd_tributo", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_saida_item_aliquotaRef", chavesEstrangeiras);
		
		tabelas.add("adm_saida_tributo");
		chaves  = new ArrayList<String>();
		chaves.add("cd_documento_saida");
		chaves.add("cd_tributo");
		tabelasSincronizadas.put("adm_saida_tributo", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_documento_saida");
		tabelaEstrangeira.add("cd_documento_saida");
		chavesEstrangeiras.put("cd_documento_saida", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tributo");
		tabelaEstrangeira.add("cd_tributo");
		chavesEstrangeiras.put("cd_tributo", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_saida_tributoRef", chavesEstrangeiras);
		
		tabelas.add("fsc_nota_fiscal");
		chaves  = new ArrayList<String>();
		chaves.add("cd_nota_fiscal");
		tabelasSincronizadas.put("fsc_nota_fiscal", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_cidade");
		tabelaEstrangeira.add("cd_cidade");
		chavesEstrangeiras.put("cd_cidade", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_destinatario", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa_endereco");
		tabelaEstrangeira.add("cd_endereco");
		chavesEstrangeiras.put("cd_endereco_destinatario", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa_endereco");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_destinatario", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa_endereco");
		tabelaEstrangeira.add("cd_endereco");
		chavesEstrangeiras.put("cd_endereco_entrega", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa_endereco");
		tabelaEstrangeira.add("cd_endereco");
		chavesEstrangeiras.put("cd_endereco_retirada", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa_endereco");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_natureza_operacao");
		tabelaEstrangeira.add("cd_natureza_operacao");
		chavesEstrangeiras.put("cd_natureza_operacao", tabelaEstrangeira);
		chavesEstrangeiras.put("cd_natureza_operacao_frete", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_transportador", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fta_veiculo");
		tabelaEstrangeira.add("cd_veiculo");
		chavesEstrangeiras.put("cd_veiculo", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fsc_motivo_cancelamento");
		tabelaEstrangeira.add("cd_motivo_cancelamento");
		chavesEstrangeiras.put("cd_motivo_cancelamento", tabelaEstrangeira);
		tabelasSincronizadas.put("fsc_nota_fiscalRef", chavesEstrangeiras);
		
		tabelas.add("fsc_nota_fiscal_historico");
		chaves  = new ArrayList<String>();
		chaves.add("cd_nota_fiscal");
		chaves.add("cd_historico");
		tabelasSincronizadas.put("fsc_nota_fiscal_historico", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fsc_nota_fiscal");
		tabelaEstrangeira.add("cd_nota_fiscal");
		chavesEstrangeiras.put("cd_nota_fiscal", tabelaEstrangeira);
		tabelasSincronizadas.put("fsc_nota_fiscal_historicoRef", chavesEstrangeiras);
		
//		tabelas.add("alm_documento_entrada");
//		chaves  = new ArrayList<String>();
//		chaves.add("cd_documento_entrada");
//		tabelasSincronizadas.put("alm_documento_entrada", chaves);
//		
//		tabelas.add("alm_documento_entrada_item");
//		chaves  = new ArrayList<String>();
//		chaves.add("cd_documento_entrada");
//		chaves.add("cd_produto_servico");
//		chaves.add("cd_empresa");
//		tabelasSincronizadas.put("alm_documento_entrada_item", chaves);
		
		tabelas.add("fsc_nota_fiscal_item");
		chaves  = new ArrayList<String>();
		chaves.add("cd_nota_fiscal");
		chaves.add("cd_item");
		tabelasSincronizadas.put("fsc_nota_fiscal_item", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_documento_saida_item");
		tabelaEstrangeira.add("cd_documento_saida");
		chavesEstrangeiras.put("cd_documento_saida", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_documento_saida_item");
		tabelaEstrangeira.add("cd_produto_servico");
		chavesEstrangeiras.put("cd_produto_servico", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_documento_saida_item");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_documento_saida_item");
		tabelaEstrangeira.add("cd_item");
		chavesEstrangeiras.put("cd_item", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_natureza_operacao");
		tabelaEstrangeira.add("cd_natureza_operacao");
		chavesEstrangeiras.put("cd_natureza_operacao", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fsc_nota_fiscal");
		tabelaEstrangeira.add("cd_nota_fiscal");
		chavesEstrangeiras.put("cd_nota_fiscal", tabelaEstrangeira);
		tabelasSincronizadas.put("fsc_nota_fiscal_itemRef", chavesEstrangeiras);
		
		tabelas.add("fsc_nota_fiscal_tributo");
		chaves  = new ArrayList<String>();
		chaves.add("cd_nota_fiscal");
		chaves.add("cd_tributo");
		tabelasSincronizadas.put("fsc_nota_fiscal_tributo", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fsc_nota_fiscal");
		tabelaEstrangeira.add("cd_nota_fiscal");
		chavesEstrangeiras.put("cd_nota_fiscal", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tributo");
		tabelaEstrangeira.add("cd_tributo");
		chavesEstrangeiras.put("cd_tributo", tabelaEstrangeira);
		tabelasSincronizadas.put("fsc_nota_fiscal_tributoRef", chavesEstrangeiras);
		
		tabelas.add("fsc_nota_fiscal_item_tributo");
		chaves  = new ArrayList<String>();
		chaves.add("cd_nota_fiscal");
		chaves.add("cd_item");
		chaves.add("cd_tributo");
		tabelasSincronizadas.put("fsc_nota_fiscal_item_tributo", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fsc_nota_fiscal_tributo");
		tabelaEstrangeira.add("cd_nota_fiscal");
		chavesEstrangeiras.put("cd_nota_fiscal", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fsc_nota_fiscal_tributo");
		tabelaEstrangeira.add("cd_tributo");
		chavesEstrangeiras.put("cd_tributo", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fsc_nota_fiscal_item");
		tabelaEstrangeira.add("cd_nota_fiscal");
		chavesEstrangeiras.put("cd_nota_fiscal", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fsc_nota_fiscal_item");
		tabelaEstrangeira.add("cd_item");
		chavesEstrangeiras.put("cd_item", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tributo_aliquota");
		tabelaEstrangeira.add("cd_tributo_aliquota");
		chavesEstrangeiras.put("cd_tributo_aliquota", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tributo_aliquota");
		tabelaEstrangeira.add("cd_tributo");
		chavesEstrangeiras.put("cd_tributo", tabelaEstrangeira);
		tabelasSincronizadas.put("fsc_nota_fiscal_item_tributoRef", chavesEstrangeiras);
		
		tabelas.add("fsc_nota_fiscal_doc_vinculado");
		chaves  = new ArrayList<String>();
		chaves.add("cd_nota_fiscal");
		chaves.add("cd_doc_vinculado");
		tabelasSincronizadas.put("fsc_nota_fiscal_doc_vinculado", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_documento_saida");
		tabelaEstrangeira.add("cd_documento_saida");
		chavesEstrangeiras.put("cd_documento_saida", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fsc_nota_fiscal");
		tabelaEstrangeira.add("cd_nota_fiscal");
		chavesEstrangeiras.put("cd_nota_fiscal", tabelaEstrangeira);
		chavesEstrangeiras.put("cd_nota_fiscal_vinculada", tabelaEstrangeira);
		tabelasSincronizadas.put("fsc_nota_fiscal_doc_vinculadoRef", chavesEstrangeiras);
		
		tabelas.add("fsc_registro_ecf");
		chaves  = new ArrayList<String>();
		chaves.add("cd_registro_ecf");
		tabelasSincronizadas.put("fsc_registro_ecf", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("bpm_referencia");
		tabelaEstrangeira.add("cd_referencia");
		chavesEstrangeiras.put("cd_referencia", tabelaEstrangeira);
		tabelasSincronizadas.put("fsc_registro_ecfRef", chavesEstrangeiras);
		
		tabelas.add("fsc_situacao_tributaria");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tributo");
		chaves.add("cd_situacao_tributaria");
		tabelasSincronizadas.put("fsc_situacao_tributaria", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tributo");
		tabelaEstrangeira.add("cd_tributo");
		chavesEstrangeiras.put("cd_tributo", tabelaEstrangeira);
		tabelasSincronizadas.put("fsc_situacao_tributariaRef", chavesEstrangeiras);
		
		tabelas.add("fta_frete");
		chaves  = new ArrayList<String>();
		chaves.add("cd_frete");
		tabelasSincronizadas.put("fta_frete", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_cliente", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fta_viagem");
		tabelaEstrangeira.add("cd_viagem");
		chavesEstrangeiras.put("cd_viagem", tabelaEstrangeira);
		tabelasSincronizadas.put("fta_freteRef", chavesEstrangeiras);
		
		tabelas.add("adm_contrato_negociacao");
		chaves  = new ArrayList<String>();
		chaves.add("cd_contrato");
		chaves.add("cd_negociacao");
		tabelasSincronizadas.put("adm_contrato_negociacao", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_contrato");
		tabelaEstrangeira.add("cd_contrato");
		chavesEstrangeiras.put("cd_contrato", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_documento_saida");
		tabelaEstrangeira.add("cd_documento_saida");
		chavesEstrangeiras.put("cd_documento_saida", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa_fisica");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_responsavel", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("seg_usuario");
		tabelaEstrangeira.add("cd_usuario");
		chavesEstrangeiras.put("cd_usuario", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_contrato_negociacaoRef", chavesEstrangeiras);
		
		
		tabelas.add("adm_conta_receber");
		chaves  = new ArrayList<String>();
		chaves.add("cd_conta_receber");
		tabelasSincronizadas.put("adm_conta_receber", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_conta_carteira");
		tabelaEstrangeira.add("cd_conta_carteira");
		chavesEstrangeiras.put("cd_conta_carteira", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_conta_financeira");
		tabelaEstrangeira.add("cd_conta");
		chavesEstrangeiras.put("cd_conta", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_conta_receber");
		tabelaEstrangeira.add("cd_conta_receber");
		chavesEstrangeiras.put("cd_conta_origem", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_contrato");
		tabelaEstrangeira.add("cd_contrato");
		chavesEstrangeiras.put("cd_contrato", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("alm_documento_saida");
		tabelaEstrangeira.add("cd_documento_saida");
		chavesEstrangeiras.put("cd_documento_saida", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fta_frete");
		tabelaEstrangeira.add("cd_frete");
		chavesEstrangeiras.put("cd_frete", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_pessoa");
		tabelaEstrangeira.add("cd_pessoa");
		chavesEstrangeiras.put("cd_pessoa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_plano_pagto_documento_saida");
		tabelaEstrangeira.add("cd_plano_pagamento");
		chavesEstrangeiras.put("cd_plano_pagamento", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_plano_pagto_documento_saida");
		tabelaEstrangeira.add("cd_documento_saida");
		chavesEstrangeiras.put("cd_documento_saida", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_plano_pagto_documento_saida");
		tabelaEstrangeira.add("cd_forma_pagamento");
		chavesEstrangeiras.put("cd_forma_pagamento", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tipo_documento");
		tabelaEstrangeira.add("cd_tipo_documento");
		chavesEstrangeiras.put("cd_tipo_documento", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_turno");
		tabelaEstrangeira.add("cd_turno");
		chavesEstrangeiras.put("cd_turno", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_contrato_negociacao");
		tabelaEstrangeira.add("cd_contrato");
		chavesEstrangeiras.put("cd_contrato", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_contrato_negociacao");
		tabelaEstrangeira.add("cd_negociacao");
		chavesEstrangeiras.put("cd_negociacao", tabelaEstrangeira);
		//Tomar Cuidado pois esta em ciclo
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_titulo_credito");
		tabelaEstrangeira.add("cd_titulo_credito");
		chavesEstrangeiras.put("cd_titulo_credito", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_conta_receberRef", chavesEstrangeiras);
		
		tabelas.add("ctb_tabela_centro_custo");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tabela_centro_custo");
		tabelasSincronizadas.put("ctb_tabela_centro_custo", chaves);
		
		tabelas.add("ctb_centro_custo");
		chaves  = new ArrayList<String>();
		chaves.add("cd_centro_custo");
		tabelasSincronizadas.put("ctb_centro_custo", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("ctb_centro_custo");
		tabelaEstrangeira.add("cd_centro_custo");
		chavesEstrangeiras.put("cd_centro_custo_superior", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_setor");
		tabelaEstrangeira.add("cd_setor");
		chavesEstrangeiras.put("cd_setor", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("ctb_tabela_centro_custo");
		tabelaEstrangeira.add("cd_tabela_centro_custo");
		chavesEstrangeiras.put("cd_tabela_centro_custo", tabelaEstrangeira);
		tabelasSincronizadas.put("ctb_centro_custoRef", chavesEstrangeiras);
		
		tabelas.add("adm_conta_receber_categoria");
		chaves  = new ArrayList<String>();
		chaves.add("cd_conta_receber");
		chaves.add("cd_categoria_economica");
		tabelasSincronizadas.put("adm_conta_receber_categoria", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_categoria_economica");
		tabelaEstrangeira.add("cd_categoria_economica");
		chavesEstrangeiras.put("cd_categoria_economica", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("ctb_centro_custo");
		tabelaEstrangeira.add("cd_centro_custo");
		chavesEstrangeiras.put("cd_centro_custo", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_conta_receber");
		tabelaEstrangeira.add("cd_conta_receber");
		chavesEstrangeiras.put("cd_conta_receber", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_conta_receber_categoriaRef", chavesEstrangeiras);
		
		tabelas.add("adm_alinea");
		chaves  = new ArrayList<String>();
		chaves.add("cd_alinea");
		tabelasSincronizadas.put("adm_alinea", chaves);
		
		tabelas.add("adm_titulo_credito");
		chaves  = new ArrayList<String>();
		chaves.add("cd_titulo_credito");
		tabelasSincronizadas.put("adm_titulo_credito", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_alinea");
		tabelaEstrangeira.add("cd_alinea");
		chavesEstrangeiras.put("cd_alinea", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_conta_financeira");
		tabelaEstrangeira.add("cd_conta");
		chavesEstrangeiras.put("cd_conta", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_conta_receber");
		tabelaEstrangeira.add("cd_conta_receber");
		chavesEstrangeiras.put("cd_conta_receber", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_banco");
		tabelaEstrangeira.add("cd_banco");
		chavesEstrangeiras.put("cd_instituicao_financeira", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tipo_documento");
		tabelaEstrangeira.add("cd_tipo_documento");
		chavesEstrangeiras.put("cd_tipo_documento", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_titulo_creditoRef", chavesEstrangeiras);
		
		tabelas.add("adm_movimento_conta");
		chaves  = new ArrayList<String>();
		chaves.add("cd_movimento_conta");
		chaves.add("cd_conta");
		tabelasSincronizadas.put("adm_movimento_conta", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_cheque");
		tabelaEstrangeira.add("cd_cheque");
		chavesEstrangeiras.put("cd_cheque", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_conta_financeira");
		tabelaEstrangeira.add("cd_conta");
		chavesEstrangeiras.put("cd_conta", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_forma_pagamento");
		tabelaEstrangeira.add("cd_forma_pagamento");
		chavesEstrangeiras.put("cd_forma_pagamento", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_movimento_conta");
		tabelaEstrangeira.add("cd_movimento_conta");
		chavesEstrangeiras.put("cd_movimento_conta_origem", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_movimento_conta");
		tabelaEstrangeira.add("cd_conta");
		chavesEstrangeiras.put("cd_conta_origem", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_tipo_documento");
		tabelaEstrangeira.add("cd_tipo_documento");
		chavesEstrangeiras.put("cd_tipo_documento", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_turno");
		tabelaEstrangeira.add("cd_turno");
		chavesEstrangeiras.put("cd_turno", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("seg_usuario");
		tabelaEstrangeira.add("cd_usuario");
		chavesEstrangeiras.put("cd_usuario", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("fta_viagem");
		tabelaEstrangeira.add("cd_viagem");
		chavesEstrangeiras.put("cd_viagem", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_movimento_contaRef", chavesEstrangeiras);
		
		tabelas.add("grl_arquivo_edi");
		chaves  = new ArrayList<String>();
		chaves.add("cd_arquivo");
		tabelasSincronizadas.put("grl_arquivo_edi", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("gr_arquivo");
		tabelaEstrangeira.add("cd_arquivo");
		chavesEstrangeiras.put("cd_arquivo", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_conta_financeira");
		tabelaEstrangeira.add("cd_conta");
		chavesEstrangeiras.put("cd_conta", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_arquivo_ediRef", chavesEstrangeiras);
		
		tabelas.add("grl_arquivo_tipo_erro");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tipo_erro");
		tabelasSincronizadas.put("grl_arquivo_tipo_erro", chaves);
		
		tabelas.add("grl_arquivo_tipo_registro");
		chaves  = new ArrayList<String>();
		chaves.add("cd_tipo_registro");
		tabelasSincronizadas.put("grl_arquivo_tipo_registro", chaves);
		
		tabelas.add("grl_arquivo_registro");
		chaves  = new ArrayList<String>();
		chaves.add("cd_registro");
		tabelasSincronizadas.put("grl_arquivo_registro", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("gr_arquivo_edi");
		tabelaEstrangeira.add("cd_arquivo");
		chavesEstrangeiras.put("cd_arquivo", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_conta_receber");
		tabelaEstrangeira.add("cd_conta_receber");
		chavesEstrangeiras.put("cd_conta_receber", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_arquivo_tipo_erro");
		tabelaEstrangeira.add("cd_tipo_erro");
		chavesEstrangeiras.put("cd_tipo_erro", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_arquivo_tipo_registro");
		tabelaEstrangeira.add("cd_tipo_registro");
		chavesEstrangeiras.put("cd_tipo_registro", tabelaEstrangeira);
		tabelasSincronizadas.put("grl_arquivo_registroRef", chavesEstrangeiras);
		
		tabelas.add("adm_movimento_conta_receber");
		chaves  = new ArrayList<String>();
		chaves.add("cd_conta");
		chaves.add("cd_movimento_conta");
		chaves.add("cd_conta_receber");
		tabelasSincronizadas.put("adm_movimento_conta_receber", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_arquivo_registro");
		tabelaEstrangeira.add("cd_registro");
		chavesEstrangeiras.put("cd_registro", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_conta_receber");
		tabelaEstrangeira.add("cd_conta_receber");
		chavesEstrangeiras.put("cd_conta_receber", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_movimento_conta");
		tabelaEstrangeira.add("cd_movimento_conta");
		chavesEstrangeiras.put("cd_movimento_conta", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_movimento_conta");
		tabelaEstrangeira.add("cd_conta");
		chavesEstrangeiras.put("cd_conta", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_arquivo");
		tabelaEstrangeira.add("cd_arquivo");
		chavesEstrangeiras.put("cd_arquivo", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_movimento_conta_receberRef", chavesEstrangeiras);
		
		tabelas.add("adm_movimento_conta_categoria");
		chaves  = new ArrayList<String>();
		chaves.add("cd_conta");
		chaves.add("cd_movimento_conta");
		chaves.add("cd_categoria_economica");
		chaves.add("cd_movimento_conta_categoria");
		tabelasSincronizadas.put("adm_movimento_conta_categoria", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_categoria_economica");
		tabelaEstrangeira.add("cd_categoria_economica");
		chavesEstrangeiras.put("cd_categoria_economica", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("ctb_centro_custo");
		tabelaEstrangeira.add("cd_centro_custo");
		chavesEstrangeiras.put("cd_centro_custo", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_conta_receber");
		tabelaEstrangeira.add("cd_conta_receber");
		chavesEstrangeiras.put("cd_conta_receber", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_movimento_conta");
		tabelaEstrangeira.add("cd_conta");
		chavesEstrangeiras.put("cd_conta", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_movimento_conta");
		tabelaEstrangeira.add("cd_movimento_conta");
		chavesEstrangeiras.put("cd_movimento_conta", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		//Tomar Cuidado pois nao ha ainda adm_conta_pagar na sincronizacao
		tabelaEstrangeira.add("adm_conta_pagar");
		tabelaEstrangeira.add("cd_conta_pagar");
		chavesEstrangeiras.put("cd_conta_pagar", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_movimento_conta_categoriaRef", chavesEstrangeiras);
		
		tabelas.add("adm_movimento_titulo_credito");
		chaves  = new ArrayList<String>();
		chaves.add("cd_conta");
		chaves.add("cd_movimento_conta");
		chaves.add("cd_titulo_credito");
		tabelasSincronizadas.put("adm_movimento_titulo_credito", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_titulo_credito");
		tabelaEstrangeira.add("cd_titulo_credito");
		chavesEstrangeiras.put("cd_titulo_credito", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_movimento_conta");
		tabelaEstrangeira.add("cd_conta");
		chavesEstrangeiras.put("cd_conta", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("adm_movimento_conta");
		tabelaEstrangeira.add("cd_movimento_conta");
		chavesEstrangeiras.put("cd_movimento_conta", tabelaEstrangeira);
		tabelasSincronizadas.put("adm_movimento_titulo_creditoRef", chavesEstrangeiras);
		
		tabelas.add("seg_usuario_modulo");
		chaves  = new ArrayList<String>();
		chaves.add("cd_usuario");
		chaves.add("cd_modulo");
		chaves.add("cd_sistema");
		tabelasSincronizadas.put("seg_usuario_modulo", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("seg_modulo");
		tabelaEstrangeira.add("cd_modulo");
		chavesEstrangeiras.put("cd_modulo", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("seg_modulo");
		tabelaEstrangeira.add("cd_sistema");
		chavesEstrangeiras.put("cd_sistema", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("seg_usuario");
		tabelaEstrangeira.add("cd_usuario");
		chavesEstrangeiras.put("cd_usuario", tabelaEstrangeira);
		tabelasSincronizadas.put("seg_usuario_moduloRef", chavesEstrangeiras);
		
		tabelas.add("seg_usuario_modulo_empresa");
		chaves  = new ArrayList<String>();
		chaves.add("cd_usuario");
		chaves.add("cd_empresa");
		chaves.add("cd_modulo");
		chaves.add("cd_sistema");
		tabelasSincronizadas.put("seg_usuario_modulo_empresa", chaves);
		chavesEstrangeiras = new HashMap <String, Object>();
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("grl_empresa");
		tabelaEstrangeira.add("cd_empresa");
		chavesEstrangeiras.put("cd_empresa", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("seg_usuario_modulo");
		tabelaEstrangeira.add("cd_modulo");
		chavesEstrangeiras.put("cd_modulo", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("seg_usuario_modulo");
		tabelaEstrangeira.add("cd_sistema");
		chavesEstrangeiras.put("cd_sistema", tabelaEstrangeira);
		tabelaEstrangeira  = new ArrayList <String>();
		tabelaEstrangeira.add("seg_usuario_modulo");
		tabelaEstrangeira.add("cd_usuario");
		chavesEstrangeiras.put("cd_usuario", tabelaEstrangeira);
		tabelasSincronizadas.put("seg_usuario_modulo_empresaRef", chavesEstrangeiras);
		
		Result resultado = new Result(1);
		
		resultado.addObject("tabelas", tabelas);
		resultado.addObject("tabelasSincronizadas", tabelasSincronizadas);
		
		return resultado;
	}

	//Metodo para criar os campos de data versao para as tabelas que nao a possuem e as trigger que atualizam a data de versao
	@SuppressWarnings("unchecked")
	public static Result initSincronizacao(int tpBanco){//tpBanco 0 - Servidor, tpBanco 1 - Cliente
		Connection connect   = Conexao.conectar();
		try {
			connect.setAutoCommit(false);
		
			ArrayList<String> tabelas = new ArrayList<String>();
			Result resultado = getTabelasSincronizacao();
			tabelas = (ArrayList<String>) resultado.getObjects().get("tabelas");
			for(int i = 0; i < tabelas.size(); i++){
				//SQL para identificar o nome das colunas de uma determinada tabela
				System.out.println("Processando inclusï¿½o de dt_versao e id_cliente na tabela: " + tabelas.get(i));
				PreparedStatement pstmt = connect.prepareStatement("SELECT a.attname AS \"column\" " +
																	"	FROM pg_catalog.pg_attribute a " +
																	"	INNER JOIN pg_stat_user_tables c on a.attrelid = c.relid " +
																	"	WHERE " +
																	"	a.attnum > 0 AND " +
																	"	NOT a.attisdropped " +
																	"	AND relname = '"+tabelas.get(i)+"' " +
																	" ORDER BY a.attname");
				//Verifica se na tabela ja existe a coluna dt_versao, se nï¿½o existir, ela ï¿½ adicionada 
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				boolean hasDtVersao  = false;
				while(rsm.next()){
					if(rsm.getString("column").equals("dt_versao")){
						System.out.println("dt_versï¿½o ja existe na tabela " + tabelas.get(i));
						hasDtVersao = true;
						break;
					}
				}	
				if(!hasDtVersao){
					PreparedStatement pstmtCriacao = connect.prepareStatement("ALTER TABLE " + tabelas.get(i) + " ADD dt_versao TIMESTAMP WITHOUT TIME ZONE");
					pstmtCriacao.executeUpdate();
				}
				System.out.println("Executada inclusï¿½o de dt_versao na tabela: " + tabelas.get(i));
				//Cria ou replica a procedure que irï¿½ tratar da atualizaï¿½ï¿½o do dt_versao para a data atual
				System.out.println("Processando procedure da tabela: " + tabelas.get(i));
				//Execuï¿½ï¿½o para Servidor
				if(tpBanco == 0){
					//Var alterar a data da versï¿½o sempre
					String procedure = "CREATE OR REPLACE FUNCTION atualizar_data_versao_"+tabelas.get(i)+"() RETURNS TRIGGER AS $$  " +
									   "  BEGIN " +
									   " 	NEW.dt_versao = CURRENT_TIMESTAMP; "+
									   "  	RETURN NEW;" +
									   "  END;" +  
							   		   "$$ LANGUAGE PLPGSQL VOLATILE;";
					PreparedStatement pstmtUpdate = connect.prepareStatement(procedure);
					pstmtUpdate.executeUpdate();
				}
				//Execuï¿½ï¿½o para Cliente
				else{
					//Vai alterar a data da versao apenas se ela nï¿½o for nula inicialmente
					String procedure = "CREATE OR REPLACE FUNCTION atualizar_data_versao_"+tabelas.get(i)+"() RETURNS TRIGGER AS $$  " +
									   "  BEGIN " +
									   "    IF OLD.dt_versao IS NOT NULL THEN " +
									   " 		NEW.dt_versao = CURRENT_TIMESTAMP; "+
									   "	END IF;" +
									   "	RETURN NEW;" +
									   "  END;" +  
							   		   "$$ LANGUAGE PLPGSQL VOLATILE;";
					PreparedStatement pstmtUpdate = connect.prepareStatement(procedure);
					pstmtUpdate.executeUpdate();
				}
				System.out.println("Executada procedure da tabela: " + tabelas.get(i));
				
				//SQL usado para buscar todos os nomes de triggers do banco
				PreparedStatement pstmtNomeTrigger = connect.prepareStatement("SELECT tgname FROM pg_trigger");
				ResultSetMap rsmNomeTrigger = new ResultSetMap(pstmtNomeTrigger.executeQuery());
				boolean hasTrigger = false;
				while(rsmNomeTrigger.next()){
					if(rsmNomeTrigger.getString("tgname").equals("trigger_atualizar_data_versao_"+tabelas.get(i))){
						hasTrigger = true;
						System.out.println("trigger_atualizar_data_versao_"+tabelas.get(i)+" jï¿½ existe!");
						break;
					}
					
				}
				//Caso nï¿½o haja trigger com o nome da tabela, ela ï¿½ adicionada
				if(!hasTrigger){
					System.out.println("Processando criaï¿½ï¿½o da trigger da tabela: " + tabelas.get(i));
					//Criando Trigger Nova
					String trigger = "CREATE TRIGGER trigger_atualizar_data_versao_"+tabelas.get(i)+" BEFORE "+((tpBanco == 0/*Inclui a execuï¿½ï¿½o da procedure para inserï¿½ï¿½o apenas no servidor*/) ? "INSERT OR" : "")+" UPDATE ON "+tabelas.get(i)+" FOR EACH ROW EXECUTE PROCEDURE atualizar_data_versao_"+tabelas.get(i)+"();";
					PreparedStatement pstmtTrigger = connect.prepareStatement(trigger);
					pstmtTrigger.executeUpdate();
					System.out.println("Executada criaï¿½ï¿½o da trigger da tabela: " + tabelas.get(i));
				}
				System.out.println();
			}
			
			connect.commit();
			
			System.out.println("Inicializaï¿½ï¿½o da sincronizaï¿½ï¿½o feita com sucesso!");
			
			return new Result(1, "Inicializaï¿½ï¿½o da sincronizaï¿½ï¿½o feita com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			Conexao.rollback(connect);
			return new sol.util.Result(-1, "Erro ao tentar inicializar sincronizaï¿½ï¿½o!", e);
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	
	//Sincroniza o banco servidor com o banco local - Passando informaï¿½ï¿½es atualizadas do banco local para o banco servidor
	public static Result sincronizar(int cdEmpresa){
		//Conecta os Bancos que serï¿½o sincronizados
		Connection connect = conectarServer();
		Connection conLocal = Conexao.conectar();
		try {
			//Nao permite que eles sejam commitados automaticamente
			connect.setAutoCommit(false);
			conLocal.setAutoCommit(false);
			ArrayList<String> tabelas = new ArrayList<String>();
			//Busca as tabelas que serï¿½o sincronizadas com suas informaï¿½ï¿½es de referencia
			HashMap<String, Object> tabelasSincronizadas = new HashMap<String, Object>();
			/**
			 * Estrutura tabelas
			 * 				tabelas (ArrayList)
			 * 	                 tabela
			 * 
			 *  		 tabelasSincronizadas
			 *  			tabela -> chaves (ArrayList)
			 *  					  	 campo	
			 *  			tabela'Ref' -> chavesEstrangeiras (HashMap)
			 *  								campo -> tabelaEstrangeira (ArrayList)
			 *  											0 - Tabela
			 *  											1 - Nome de Origem do Campo
			 *  			
			 */
			Result resultado = getTabelasSincronizacao();
			tabelas = (ArrayList<String>) resultado.getObjects().get("tabelas"); 
			tabelasSincronizadas = (HashMap<String, Object>) resultado.getObjects().get("tabelasSincronizadas");
			
			//Hash para identificar os registros que foram inseridos pelo cliente, para que sejam apagados e atualizados novamente pelo servidor
			/**
			 * Estrutura tabelasDelecao
			 *  			tabela	-> registros (ArrayList)
			 *  					   		chaves (HashMap)
			 *  								campo -> valor
			 *  			
			 */
			HashMap<String, Object> tabelasDelecao = new HashMap<String, Object>();
			
			//Mapeia os codigos das pessoas inseridas com os codigos velhos e novos para que se atualize corretamente na hora das vendas no documento de saida e na conta a receber
			ArrayList<Integer> cdPessoaAntigo = new ArrayList<Integer>();
			HashMap<Integer, Integer> mapaCdPessoa = new HashMap<Integer, Integer>();
			
			
			//Atualiza servidor com dados do banco local - Cliente
			Result rs = atualizarServidorCliente(cdEmpresa, tabelasDelecao, cdPessoaAntigo, mapaCdPessoa, connect, conLocal);
			if(rs.getCode() <= 0){
				Conexao.rollback(conLocal);
				try{connect.rollback();}catch(Exception e){}
				System.out.println(rs.getMessage());
				return rs;
			}
			//Atualiza servidor com dados do banco local - Vendas
			rs = atualizarServidorVendas(tabelasDelecao, cdPessoaAntigo, mapaCdPessoa, connect, conLocal);
			if(rs.getCode() <= 0){
				Conexao.rollback(conLocal);
				try{connect.rollback();}catch(Exception e){}
				System.out.println(rs.getMessage());
				return rs;
			}
			//Apaga tabelas do banco local (Obs.: ï¿½ feito de trï¿½s para frente pois ï¿½ preciso deletar primeiro as tabelas que referenciam outras para depois deletar as proprias)
			for(int i = tabelas.size() - 1; i >= 0; i--){
				rs = apagarTabela(tabelas.get(i), tabelasDelecao, conLocal);
				if(rs.getCode() <= 0){
					Conexao.rollback(conLocal);
					try{connect.rollback();}catch(Exception e){}
					System.out.println(rs.getMessage());
					return rs;
				}
			}
			
			//Se a data nï¿½o estiver configurada nos parametros, o sistema pegara uma data muito antiga para que tudo seja sincronizado
			String dtSincronizacao = ParametroServices.getValorOfParametro("DT_SINCRONIZACAO", conLocal);
			if(dtSincronizacao == null){
				ParametroServices.updateValueOfParametro(ParametroServices.getByName("DT_SINCRONIZACAO").getCdParametro(), "01/01/1970 00:00:00", conLocal);
			}
			
			//Atualiza tabelas do banco local com informaï¿½ï¿½es do servidor
			for(int i = 0; i < tabelas.size(); i++){
				rs = atualizarLocal(cdEmpresa, tabelas.get(i), (ArrayList<String>)tabelasSincronizadas.get(tabelas.get(i)), conLocal, connect);
				if(rs.getCode() <= 0){
					Conexao.rollback(conLocal);
					try{connect.rollback();}catch(Exception e){}
					System.out.println(rs.getMessage());
					return rs;
				}
			}
			
			//Atualizaï¿½ï¿½o da data de sincronizaï¿½ï¿½o no banco local
			ParametroServices.updateValueOfParametro(ParametroServices.getByName("DT_SINCRONIZACAO").getCdParametro(), Util.convCalendarStringCompleto(Util.getDataAtual()), conLocal);
			
			connect.commit();
			conLocal.commit();
			
			return new Result(1, "Sincronizaï¿½ï¿½o feita com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar sincronizar bancos: " + e);
		}
		finally{
			try {connect.close();} catch (SQLException e) {e.printStackTrace();}
			Conexao.rollback(conLocal);
		}
	}
	
	//Metodo que excluir do banco local os registros que foram atualizados para o servidor, para que este retorne os dados novamente
	//para o cliente com os codigos primarios gerados novos
	public static sol.util.Result apagarTabela(String tabela, HashMap<String, Object> tabelasDelecao, Connection conLocal){
		try {
			ArrayList<Object> arrayRegistros = ((ArrayList<Object>)tabelasDelecao.get(tabela));
			if(arrayRegistros != null && arrayRegistros.size() > 0){
				//Monta o SQL
				for(int i = 0; i < arrayRegistros.size(); i++){
					//Busca chaves primarias com seus respectivos valores para ser deletado no banco local
					HashMap<String, Object> chaves = (HashMap<String, Object>) arrayRegistros.get(i);
					//Constroi a clausula WHERE para deletar somente os registros que foram inseridos do banco local para o servidor
					String where = " WHERE 1=1 ";
					for(String key : chaves.keySet()){
						where += " AND " + key + " =  ? ";
					}
					//Efetua o delecao e insere os dados no preparedStatement
					PreparedStatement pstmtDelete = conLocal.prepareStatement("DELETE FROM "+tabela+where);
					int count = 1;
					for(String key : chaves.keySet()){
						pstmtDelete.setObject(count, chaves.get(key));
						count++;
					}
					int y = pstmtDelete.executeUpdate();
					if(y < 0){
						return new Result(-1, "Erro ao deletar a tabela "+tabela+" do banco local = " + y);
					}
					System.out.println("Deleï¿½ï¿½o "+i+" da tabela "+tabela+"!");
				}
				System.out.println("Deleï¿½ï¿½o da tabela "+tabela+" concluï¿½da!");
			}
			else
				System.out.println("0 Ocorrencias de deleï¿½ï¿½o na tabela "+tabela+"!");
			
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar sincronizar bancos!", e);
		}
	}
	
	//Insere as vendas do banco local no banco do servidor
	@SuppressWarnings("unchecked")
	public static sol.util.Result atualizarServidorVendas(HashMap<String, Object> tabelasDelecao, ArrayList<Integer> cdPessoaAntigo, HashMap<Integer, Integer> mapaCdPessoa, Connection connect, Connection conLocal){
		try {
			
			//Pesquisa do banco de dados local onde a data de versao ï¿½ nula, ou seja, ï¿½ um dado novo
			PreparedStatement pstmtLocal = conLocal.prepareStatement("SELECT * FROM alm_documento_saida WHERE dt_versao IS NULL");
			ResultSetMap rsm = new ResultSetMap(pstmtLocal.executeQuery());
			
			//Contagem de insercoes
			int count = 0;
			
			//Mapeia os codigos do documento de saida da tabela local e armazenamento os novos codigos ao se inserir no banco do servidor
			ArrayList<Integer> cdDocumentoSaidaAntigo = new ArrayList<Integer>();
			HashMap<Integer, Integer> mapaCdDocumentoSaida = new HashMap<Integer, Integer>();
			
			//Mapeia os codigos de conta a receber da tabela local e armazenamento os novos codigos ao se inserir no banco do servidor
			ArrayList<Integer> cdContaReceberAntigo = new ArrayList<Integer>();
			HashMap<Integer, Integer> mapaCdContaReceber = new HashMap<Integer, Integer>();

			//Mapeia os codigos de titulo de credito da tabela local e armazenamento os novos codigos ao se inserir no banco do servidor
			ArrayList<Integer> cdTituloCreditoAntigo = new ArrayList<Integer>();
			HashMap<Integer, Integer> mapaCdTituloCredito = new HashMap<Integer, Integer>();
			
			//SQL para identificar o nome das colunas de uma determinada tabela
			PreparedStatement pstmtColunas = connect.prepareStatement("SELECT a.attname AS \"column\" " +
																"	FROM pg_catalog.pg_attribute a " +
																"	INNER JOIN pg_stat_user_tables c on a.attrelid = c.relid " +
																"	WHERE " +
																"	a.attnum > 0 AND " +
																"	NOT a.attisdropped " +
																"	AND relname = ? " +
																"ORDER BY a.attname");
			
			//Inserir a tabela com um array vazio, e ir acrescentando de acordo a valores que precisando ser inseridos
			ArrayList<Object> hashChaves = new ArrayList<Object>();
			tabelasDelecao.put("alm_documento_saida", hashChaves);
			
			//Busca tabelas sincronizadas do sistema
			Result resultado = getTabelasSincronizacao();
			HashMap<String, Object> tabelasSincronizadas = (HashMap<String, Object>) resultado.getObjects().get("tabelasSincronizadas"); 
			while(rsm.next()){
				System.out.println();
				System.out.println("1: Inserï¿½ï¿½o "+count+" da tabela alm_documento_saida, Nï¿½ " + rsm.getString("nr_documento_saida"));
				count++;
				/**
				 * INSERCAO EM ALM_DOCUMENTO_SAIDA
				 */
				//Acrescenta as colunas a um array
				pstmtColunas.setString(1, "alm_documento_saida");
				ResultSetMap rsmColunas 			 = new ResultSetMap(pstmtColunas.executeQuery());
				ArrayList<String> colunas 			 = new ArrayList<String>();
				while(rsmColunas.next()){
					//cd_documento_saida sera inserido manualmente
					if(!rsmColunas.getString("column").equals("dt_versao") && !rsmColunas.getString("column").equals("cd_documento_saida") && !rsmColunas.getString("column").equals("cd_cliente"))
						colunas.add(rsmColunas.getString("column"));
				}
				
				//O sistema buscarï¿½ todas as colunas da tabela alm_documento_saida para montar o SQL referente a insercao junto com as interrogaï¿½ï¿½es
				String camposInsercao = "";
				String interrogacao = "";
				for(int j = 0; j < colunas.size(); j++){
					camposInsercao += colunas.get(j) + ",";
					interrogacao += "?,";
				}
				//Retirada das ultimas virgulas
				camposInsercao = camposInsercao.substring(0, camposInsercao.length()-1);
				interrogacao = interrogacao.substring(0, interrogacao.length()-1);
				
				//Acrescenta +1 no array que representa os registros que serï¿½o deletados
				HashMap<String, Object> valor = new HashMap<String, Object>();
				valor.put("cd_documento_saida", rsm.getObject("cd_documento_saida"));
				hashChaves.add(valor);
				
				//Busca um array com as chaves estrangeiras
				HashMap <String, Object> chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("alm_documento_saidaRef"); 
				ArrayList<String> chavesEstrangeiras = new ArrayList<String>();
				for(String chave : chavesEstrangeirasHash.keySet()){
					chavesEstrangeiras.add(chave);
				}
				//Monta o SQL
				PreparedStatement pstmtInsert = connect.prepareStatement("INSERT INTO alm_documento_saida (cd_documento_saida, cd_cliente, "+camposInsercao+") VALUES (?, ?, "+interrogacao+")");
				//Produz o novo codigo a partir do metodo
				int cdDocumentoSaida = getNextCodeServer("alm_documento_saida", connect);
				//Mapeia novo codigo para futura referencia
				mapaCdDocumentoSaida.put(rsm.getInt("cd_documento_saida"), cdDocumentoSaida);
				cdDocumentoSaidaAntigo.add(rsm.getInt("cd_documento_saida"));
				//Acrescenta os valores no pstmt
				pstmtInsert.setObject(1, cdDocumentoSaida);
				//Caso esteja no array que guarda os codigos antigos, o sistema mapeia para o sistema novo associado a esse antigo, para saber o codigo novo no servidor, caso nao esteja, coloca o codigo antigo
				if(Util.inArrayInteger(rsm.getInt("cd_cliente"), cdPessoaAntigo)){
					pstmtInsert.setInt(2, mapaCdPessoa.get(rsm.getInt("cd_cliente")));
				}
				else{
					if(rsm.getInt("cd_cliente") == 0)
						pstmtInsert.setNull(2, Types.INTEGER);
					else	
						pstmtInsert.setInt(2, rsm.getInt("cd_cliente"));
				}
				for(int i = 3; i <= colunas.size()+2; i++){
					//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
					//nas tabelas originais
					if(rsm.getObject(colunas.get(i-3)) instanceof Integer && (Integer)rsm.getObject(colunas.get(i-3)) == 0 && Util.inArrayString(colunas.get(i-3), chavesEstrangeiras))
						pstmtInsert.setNull(i, Types.INTEGER);
					else 
						pstmtInsert.setObject(i, rsm.getObject(colunas.get(i-3)));	
				}
				//Executa
				if(pstmtInsert.executeUpdate() <= 0){
					return new Result(-1, "Erro ao inserir na tabela alm_documento_saida no banco servidor!");
				}
				
				
				//*******************************************************************************************************************************************************
				//Pesquisa do banco de dados local onde o codigo de documento de saida ï¿½ o mesmo do registro que vai ser acrescentado no banco servidor
				PreparedStatement pstmtDocumentoSaidaItem = conLocal.prepareStatement("SELECT * FROM alm_documento_saida_item WHERE cd_documento_saida = " + rsm.getInt("cd_documento_saida"));
				ResultSetMap rsmSaidaItem = new ResultSetMap(pstmtDocumentoSaidaItem.executeQuery());
				//Inserir a tabela com um array vazio, e ir acrescentando de acordo a valores que precisando ser inseridos
				ArrayList<Object> hashChavesSaidaItem = new ArrayList<Object>();
				//Tratamento para que os registros nao sejam sobrepostos
				if(tabelasDelecao.get("alm_documento_saida_item") == null)
					tabelasDelecao.put("alm_documento_saida_item", hashChavesSaidaItem);
				else
					hashChavesSaidaItem = (ArrayList<Object>) tabelasDelecao.get("alm_documento_saida_item");
				while(rsmSaidaItem.next()){
					/**
					 * INSERCAO EM ALM_DOCUMENTO_SAIDA_ITEM
					 */
					System.out.println("2: Inserï¿½ï¿½o "+count+" da tabela alm_documento_saida_item, Cod. Produto " + rsmSaidaItem.getString("cd_produto_servico"));
					
					//Acrescenta as colunas a um array
					pstmtColunas.setString(1, "alm_documento_saida_item");
					rsmColunas 			 = new ResultSetMap(pstmtColunas.executeQuery());
					colunas 			 = new ArrayList<String>();
					while(rsmColunas.next()){
						if(!rsmColunas.getString("column").equals("dt_versao") && !rsmColunas.getString("column").equals("cd_documento_saida"))
							colunas.add(rsmColunas.getString("column"));
					}
					
					camposInsercao = "";
					interrogacao = "";
					for(int j = 0; j < colunas.size(); j++){
						camposInsercao += colunas.get(j) + ",";
						interrogacao += "?,";
					}
					//Retira as virgulas ao final
					camposInsercao = camposInsercao.substring(0, camposInsercao.length()-1);
					interrogacao = interrogacao.substring(0, interrogacao.length()-1);
					
					//Acrescenta +1 no array que representa os registros que serï¿½o deletados
					valor = new HashMap<String, Object>();
					valor.put("cd_documento_saida", rsmSaidaItem.getObject("cd_documento_saida"));
					valor.put("cd_produto_servico", rsmSaidaItem.getObject("cd_produto_servico"));
					valor.put("cd_empresa", rsmSaidaItem.getObject("cd_empresa"));
					valor.put("cd_item", rsmSaidaItem.getObject("cd_item"));
					hashChavesSaidaItem.add(valor);
					//Busca um array com as chaves estrangeiras
					chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("alm_documento_saida_itemRef"); 
					chavesEstrangeiras = new ArrayList<String>();
					for(String chave : chavesEstrangeirasHash.keySet()){
						chavesEstrangeiras.add(chave);
					}
					//Monta o SQL
					pstmtInsert = connect.prepareStatement("INSERT INTO alm_documento_saida_item (cd_documento_saida, "+camposInsercao+") VALUES (?, "+interrogacao+")");
					//Busca o codigo de documento de saida que foi produzido para o documento de saida
					pstmtInsert.setObject(1, cdDocumentoSaida);
					for(int i = 2; i <= colunas.size()+1; i++){
						//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
						//nas tabelas originais
						if(rsmSaidaItem.getObject(colunas.get(i-2)) instanceof Integer && (Integer)rsmSaidaItem.getObject(colunas.get(i-2)) == 0 && Util.inArrayString(colunas.get(i-2), chavesEstrangeiras))
							pstmtInsert.setNull(i, Types.INTEGER);
						else 
							pstmtInsert.setObject(i, rsmSaidaItem.getObject(colunas.get(i-2)));	
					}
					if(pstmtInsert.executeUpdate() <= 0){
						return new Result(-1, "Erro ao inserir na tabela alm_documento_saida_item no banco servidor!");
					}
				}
				//*******************************************************************************************************************************************************
				/**
				 * INSERCAO EM ALM_SAIDA_LOCAL_ITEM
				 */
				//Pesquisa do banco de dados local onde o codigo de documento de saida ï¿½ o mesmo do registro que vai ser acrescentado no banco servidor
				PreparedStatement pstmtSaidaLocalItem = conLocal.prepareStatement("SELECT * FROM alm_saida_local_item WHERE cd_documento_saida = " + rsm.getInt("cd_documento_saida"));
				ResultSetMap rsmSaidaLocalItem = new ResultSetMap(pstmtSaidaLocalItem.executeQuery());
				//Tratamento para que os registros nao sejam sobrepostos
				ArrayList<Object> hashChavesSaidaLocalItem = new ArrayList<Object>();
				if(tabelasDelecao.get("alm_saida_local_item") == null)
					tabelasDelecao.put("alm_saida_local_item", hashChavesSaidaLocalItem);
				else
					hashChavesSaidaLocalItem = (ArrayList<Object>) tabelasDelecao.get("alm_saida_local_item");
				//Inserir a tabela com um array vazio, e ir acrescentando de acordo a valores que precisando ser inseridos
				while(rsmSaidaLocalItem.next()){
					System.out.println("3: Inserï¿½ï¿½o "+count+" da tabela alm_saida_local_item, Cod. Produto " + rsmSaidaLocalItem.getString("cd_produto_servico") + ", Cod. Local Armazenamento " + rsmSaidaLocalItem.getString("cd_local_armazenamento"));
					
					//Acrescenta as colunas a um array
					pstmtColunas.setString(1, "alm_saida_local_item");
					rsmColunas 			 = new ResultSetMap(pstmtColunas.executeQuery());
					colunas 			 = new ArrayList<String>();
					while(rsmColunas.next()){
						if(!rsmColunas.getString("column").equals("dt_versao") && !rsmColunas.getString("column").equals("cd_documento_saida"))
							colunas.add(rsmColunas.getString("column"));
					}
					
					camposInsercao = "";
					interrogacao = "";
					for(int j = 0; j < colunas.size(); j++){
						camposInsercao += colunas.get(j) + ",";
						interrogacao += "?,";
					}
					//Retira as virgulas finaiss
					camposInsercao = camposInsercao.substring(0, camposInsercao.length()-1);
					interrogacao = interrogacao.substring(0, interrogacao.length()-1);
					
					//Acrescenta +1 no array que representa os registros que serï¿½o deletados
					valor = new HashMap<String, Object>();
					valor.put("cd_saida", rsmSaidaLocalItem.getObject("cd_saida"));
					valor.put("cd_produto_servico", rsmSaidaLocalItem.getObject("cd_produto_servico"));
					valor.put("cd_documento_saida", rsmSaidaLocalItem.getObject("cd_documento_saida"));
					valor.put("cd_empresa", rsmSaidaLocalItem.getObject("cd_empresa"));
					valor.put("cd_local_armazenamento", rsmSaidaLocalItem.getObject("cd_local_armazenamento"));
					valor.put("cd_item", rsmSaidaLocalItem.getObject("cd_item"));
					hashChavesSaidaLocalItem.add(valor);
					//Busca um array com as chaves estrangeiras
					chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("alm_saida_local_itemRef"); 
					chavesEstrangeiras = new ArrayList<String>();
					for(String chave : chavesEstrangeirasHash.keySet()){
						chavesEstrangeiras.add(chave);
					}
					//Monta o SQL
					pstmtInsert = connect.prepareStatement("INSERT INTO alm_saida_local_item (cd_documento_saida, "+camposInsercao+") VALUES (?, "+interrogacao+")");
					pstmtInsert.setInt(1, cdDocumentoSaida);
					for(int i = 2; i <= colunas.size()+1; i++){
						//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
						//nas tabelas originais
						if(rsmSaidaLocalItem.getObject(colunas.get(i-2)) instanceof Integer && (Integer)rsmSaidaLocalItem.getObject(colunas.get(i-2)) == 0 && Util.inArrayString(colunas.get(i-2), chavesEstrangeiras)){
							pstmtInsert.setNull(i, Types.INTEGER);
						}
						else 
							pstmtInsert.setObject(i, rsmSaidaLocalItem.getObject(colunas.get(i-2)));	
					}
					if(pstmtInsert.executeUpdate() <= 0){
						return new Result(-1, "Erro ao inserir na tabela alm_saida_local_item no banco servidor!");
					}
				}
				//*******************************************************************************************************************************************************
				//Pesquisa do banco de dados local onde o codigo de documento de saida ï¿½ o mesmo do registro que vai ser acrescentado no banco servidor
				PreparedStatement pstmtPlanoPagtoDocSaida = conLocal.prepareStatement("SELECT * FROM adm_plano_pagto_documento_saida WHERE cd_documento_saida = " + rsm.getInt("cd_documento_saida"));
				ResultSetMap rsmPlanoPagtoDocSaida = new ResultSetMap(pstmtPlanoPagtoDocSaida.executeQuery());
				//Tratamento para que os registros nao sejam sobrepostos
				ArrayList<Object> hashChavesPlanoPagtoDoc = new ArrayList<Object>();
				if(tabelasDelecao.get("adm_plano_pagto_documento_saida") == null)
					tabelasDelecao.put("adm_plano_pagto_documento_saida", hashChavesPlanoPagtoDoc);
				else
					hashChavesPlanoPagtoDoc = (ArrayList<Object>) tabelasDelecao.get("adm_plano_pagto_documento_saida");
				//Inserir a tabela com um array vazio, e ir acrescentando de acordo a valores que precisando ser inseridos
				while(rsmPlanoPagtoDocSaida.next()){
					/**
					 * INSERCAO EM ADM_PLANO_PAGTO_DOCUMENTO_SAIDA
					 */
					System.out.println("4: Inserï¿½ï¿½o "+count+" da tabela adm_plano_pagto_documento_saida, Cod. Forma Pagamento " + rsmPlanoPagtoDocSaida.getString("cd_forma_pagamento") + ", Cod. Plano Pagamento " + rsmPlanoPagtoDocSaida.getString("cd_plano_pagamento"));
					//Acrescenta as colunas a um array
					pstmtColunas.setString(1, "adm_plano_pagto_documento_saida");
					rsmColunas 			 = new ResultSetMap(pstmtColunas.executeQuery());
					colunas 			 = new ArrayList<String>();
					while(rsmColunas.next()){
						if(!rsmColunas.getString("column").equals("dt_versao") && !rsmColunas.getString("column").equals("cd_documento_saida"))
							colunas.add(rsmColunas.getString("column"));
					}
					
					camposInsercao = "";
					interrogacao = "";
					for(int j = 0; j < colunas.size(); j++){
						camposInsercao += colunas.get(j) + ",";
						interrogacao += "?,";
					}
					camposInsercao = camposInsercao.substring(0, camposInsercao.length()-1);
					interrogacao = interrogacao.substring(0, interrogacao.length()-1);
					
					//Acrescenta +1 no array que representa os registros que serï¿½o deletados
					valor = new HashMap<String, Object>();
					valor.put("cd_plano_pagamento", rsmPlanoPagtoDocSaida.getObject("cd_plano_pagamento"));
					valor.put("cd_documento_saida", rsmPlanoPagtoDocSaida.getObject("cd_documento_saida"));
					valor.put("cd_forma_pagamento", rsmPlanoPagtoDocSaida.getObject("cd_forma_pagamento"));
					hashChavesPlanoPagtoDoc.add(valor);
					//Busca um array com as chaves estrangeiras
					chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("adm_plano_pagto_documento_saidaRef"); 
					chavesEstrangeiras = new ArrayList<String>();
					for(String chave : chavesEstrangeirasHash.keySet()){
						chavesEstrangeiras.add(chave);
					}
					//Monta o SQL
					pstmtInsert = connect.prepareStatement("INSERT INTO adm_plano_pagto_documento_saida (cd_documento_saida, "+camposInsercao+") VALUES (?, "+interrogacao+")");
					//Insere os dados do banco do servidor no novo registro do banco local
					//Busca o codigo de documento de saida que foi produzido para o documento de saida
					pstmtInsert.setObject(1, cdDocumentoSaida);
					for(int i = 2; i <= colunas.size()+1; i++){
						//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
						//nas tabelas originais
						if(rsmPlanoPagtoDocSaida.getObject(colunas.get(i-2)) instanceof Integer && (Integer)rsmPlanoPagtoDocSaida.getObject(colunas.get(i-2)) == 0 && Util.inArrayString(colunas.get(i-2), chavesEstrangeiras))
							pstmtInsert.setNull(i, Types.INTEGER);
						else 
							pstmtInsert.setObject(i, rsmPlanoPagtoDocSaida.getObject(colunas.get(i-2)));	
					}
					if(pstmtInsert.executeUpdate() <= 0){
						return new Result(-1, "Erro ao inserir na tabela adm_plano_pagto_documento_saida no banco servidor!");
					}
				}
				//*******************************************************************************************************************************************************
				
			}
			
			/**
			 * INSERCAO EM ADM_CONTA_RECEBER
			 */
			//Pesquisa do banco de dados local onde a data de versao ï¿½ nula, ou seja, ï¿½ um dado novo
			pstmtLocal = conLocal.prepareStatement("SELECT * FROM adm_conta_receber WHERE dt_versao IS NULL");
			rsm = new ResultSetMap(pstmtLocal.executeQuery());
			//Inserir a tabela com um array vazio, e ir acrescentando de acordo a valores que precisando ser inseridos
			hashChaves = new ArrayList<Object>();
			tabelasDelecao.put("adm_conta_receber", hashChaves);
			while(rsm.next()){
				System.out.println();
				System.out.println("5: Inserï¿½ï¿½o "+count+" da tabela adm_conta_receber, ID " + rsm.getString("id_conta_receber"));
				count++;
								
				//Acrescenta as colunas a um array
				pstmtColunas.setString(1, "adm_conta_receber");
				ResultSetMap rsmColunas 			 = new ResultSetMap(pstmtColunas.executeQuery());
				ArrayList<String> colunas 			 = new ArrayList<String>();
				while(rsmColunas.next()){
					if(!rsmColunas.getString("column").equals("dt_versao") && !rsmColunas.getString("column").equals("cd_conta_receber") && !rsmColunas.getString("column").equals("cd_documento_saida") && !rsmColunas.getString("column").equals("cd_pessoa"))
						colunas.add(rsmColunas.getString("column"));
				}
				
				String camposInsercao = "";
				String interrogacao = "";
				for(int j = 0; j < colunas.size(); j++){
					camposInsercao += colunas.get(j) + ",";
					interrogacao += "?,";
				}
				camposInsercao = camposInsercao.substring(0, camposInsercao.length()-1);
				interrogacao = interrogacao.substring(0, interrogacao.length()-1);
				
				//Acrescenta +1 no array que representa os registros que serï¿½o deletados
				HashMap<String, Object> valor = new HashMap<String, Object>();
				valor.put("cd_conta_receber", rsm.getObject("cd_conta_receber"));
				hashChaves.add(valor);
				//Busca um array com as chaves estrangeiras
				HashMap <String, Object> chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("adm_conta_receberRef"); 
				ArrayList<String> chavesEstrangeiras = new ArrayList<String>();
				for(String chave : chavesEstrangeirasHash.keySet()){
					chavesEstrangeiras.add(chave);
				}
				//Monta o SQL
				PreparedStatement pstmtInsert = connect.prepareStatement("INSERT INTO adm_conta_receber (cd_conta_receber, cd_documento_saida, cd_pessoa, "+camposInsercao+") VALUES (?, ?, ?, "+interrogacao+")");
				
				//Produz o novo codigo a partir do metodo
				int cdContaReceber = getNextCodeServer("adm_conta_receber", connect);
				//Mapeia novo codigo para futura referencia
				mapaCdContaReceber.put(rsm.getInt("cd_conta_receber"), cdContaReceber);
				cdContaReceberAntigo.add(rsm.getInt("cd_conta_receber"));
				pstmtInsert.setObject(1, cdContaReceber);
				//Caso esteja no array que guarda os codigos antigos, o sistema mapeia para o sistema novo associado a esse antigo, para saber o codigo novo no servidor, caso nao esteja, coloca o codigo antigo
				if(Util.inArrayInteger(rsm.getInt("cd_documento_saida"), cdDocumentoSaidaAntigo))
					pstmtInsert.setInt(2, mapaCdDocumentoSaida.get(rsm.getInt("cd_documento_saida")));
				else
					if(rsm.getInt("cd_documento_saida") == 0)
						pstmtInsert.setNull(2, Types.INTEGER);
					else
						pstmtInsert.setInt(2, rsm.getInt("cd_documento_saida"));
				//Caso esteja no array que guarda os codigos antigos, o sistema mapeia para o sistema novo associado a esse antigo, para saber o codigo novo no servidor, caso nao esteja, coloca o codigo antigo
				if(Util.inArrayInteger(rsm.getInt("cd_pessoa"), cdPessoaAntigo)){
					pstmtInsert.setInt(3, mapaCdPessoa.get(rsm.getInt("cd_pessoa")));
				}
				else{
					if(rsm.getInt("cd_pessoa") == 0)
						pstmtInsert.setNull(3, Types.INTEGER);
					else	
						pstmtInsert.setInt(3, rsm.getInt("cd_pessoa"));
				}
				for(int i = 4; i <= colunas.size()+3; i++){
					//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
					//nas tabelas originais
					if(rsm.getObject(colunas.get(i-4)) instanceof Integer && (Integer)rsm.getObject(colunas.get(i-4)) == 0 && Util.inArrayString(colunas.get(i-4), chavesEstrangeiras))
						pstmtInsert.setNull(i, Types.INTEGER);
					else 
						pstmtInsert.setObject(i, rsm.getObject(colunas.get(i-4)));	
				}
				if(pstmtInsert.executeUpdate() <= 0){
					return new Result(-1, "Erro ao inserir na tabela adm_conta_receber no banco servidor!");
				}
				//*******************************************************************************************************************************************************
				//Pesquisa do banco de dados local onde o codigo de conta a receber ï¿½ o mesmo do registro que vai ser acrescentado no banco servidor
				PreparedStatement pstmtContaReceberCategoria = conLocal.prepareStatement("SELECT * FROM adm_conta_receber_categoria WHERE cd_conta_receber = " + rsm.getInt("cd_conta_receber"));
				ResultSetMap rsmContaReceberCategoria = new ResultSetMap(pstmtContaReceberCategoria.executeQuery());
				//Tratamento para que os registros nao sejam sobrepostos
				ArrayList<Object> hashChavesReceberCategoria = new ArrayList<Object>();
				if(tabelasDelecao.get("adm_conta_receber_categoria") == null)
					tabelasDelecao.put("adm_conta_receber_categoria", hashChavesReceberCategoria);
				else
					hashChavesReceberCategoria = (ArrayList<Object>) tabelasDelecao.get("adm_conta_receber_categoria");
				//Inserir a tabela com um array vazio, e ir acrescentando de acordo a valores que precisando ser inseridos
				while(rsmContaReceberCategoria.next()){
					/**
					 * INSERCAO EM ADM_CONTA_RECEBER_CATEGORIA
					 */
					System.out.println("6: Inserï¿½ï¿½o "+count+" da tabela adm_conta_receber_categoria, Cod. Categoria " + rsmContaReceberCategoria.getString("cd_categoria_economica"));
					//Acrescenta as colunas a um array
					pstmtColunas.setString(1, "adm_conta_receber_categoria");
					rsmColunas 			 = new ResultSetMap(pstmtColunas.executeQuery());
					colunas 			 = new ArrayList<String>();
					while(rsmColunas.next()){
						if(!rsmColunas.getString("column").equals("dt_versao") && !rsmColunas.getString("column").equals("cd_conta_receber"))
							colunas.add(rsmColunas.getString("column"));
					}
					
					camposInsercao = "";
					interrogacao = "";
					for(int j = 0; j < colunas.size(); j++){
						camposInsercao += colunas.get(j) + ",";
						interrogacao += "?,";
					}
					camposInsercao = camposInsercao.substring(0, camposInsercao.length()-1);
					interrogacao = interrogacao.substring(0, interrogacao.length()-1);
					
					//Acrescenta +1 no array que representa os registros que serï¿½o deletados
					valor = new HashMap<String, Object>();
					valor.put("cd_conta_receber", rsmContaReceberCategoria.getObject("cd_conta_receber"));
					valor.put("cd_categoria_economica", rsmContaReceberCategoria.getObject("cd_categoria_economica"));
					hashChavesReceberCategoria.add(valor);
					//Busca um array com as chaves estrangeiras
					chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("adm_conta_receber_categoriaRef"); 
					chavesEstrangeiras = new ArrayList<String>();
					for(String chave : chavesEstrangeirasHash.keySet()){
						chavesEstrangeiras.add(chave);
					}
					//Monta o SQL
					pstmtInsert = connect.prepareStatement("INSERT INTO adm_conta_receber_categoria (cd_conta_receber, "+camposInsercao+") VALUES (?, "+interrogacao+")");
					//Busca o codigo de conta a receber
					pstmtInsert.setObject(1, cdContaReceber);
					for(int i = 2; i <= colunas.size()+1; i++){
						//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
						//nas tabelas originais
						if(rsmContaReceberCategoria.getObject(colunas.get(i-2)) instanceof Integer && (Integer)rsmContaReceberCategoria.getObject(colunas.get(i-2)) == 0 && Util.inArrayString(colunas.get(i-2), chavesEstrangeiras))
							pstmtInsert.setNull(i, Types.INTEGER);
						else 
							pstmtInsert.setObject(i, rsmContaReceberCategoria.getObject(colunas.get(i-2)));	
					}
					if(pstmtInsert.executeUpdate() <= 0){
						return new Result(-1, "Erro ao inserir na tabela adm_conta_receber_categoria no banco servidor!");
					}
				}
				//*******************************************************************************************************************************************************
			}
			//*******************************************************************************************************************************************************
			//Pesquisa do banco de dados local onde o codigo de conta a receber ï¿½ o mesmo do registro que vai ser acrescentado no banco servidor
			/**
			 * INSERCAO EM ADM_TITULO_CREDITO
			 */
			PreparedStatement pstmtTituloCredito = conLocal.prepareStatement("SELECT * FROM adm_titulo_credito WHERE dt_versao IS NULL");
			ResultSetMap rsmTituloCredito = new ResultSetMap(pstmtTituloCredito.executeQuery());
			//Tratamento para que os registros nao sejam sobrepostos
			ArrayList<Object> hashChavesTitulo = new ArrayList<Object>();
			if(tabelasDelecao.get("adm_titulo_credito") == null)
				tabelasDelecao.put("adm_titulo_credito", hashChavesTitulo);
			else
				hashChavesTitulo = (ArrayList<Object>) tabelasDelecao.get("adm_titulo_credito");
			//Inserir a tabela com um array vazio, e ir acrescentando de acordo a valores que precisando ser inseridos
			while(rsmTituloCredito.next()){
				System.out.println("7: Inserï¿½ï¿½o "+count+" da tabela adm_titulo_credito, Cod. Titulo Credito " + rsmTituloCredito.getString("cd_titulo_credito"));
				//Acrescenta as colunas a um array
				pstmtColunas.setString(1, "adm_titulo_credito");
				ResultSetMap rsmColunas 			 = new ResultSetMap(pstmtColunas.executeQuery());
				ArrayList<String> colunas 			 = new ArrayList<String>();
				while(rsmColunas.next()){
					if(!rsmColunas.getString("column").equals("dt_versao") && !rsmColunas.getString("column").equals("cd_titulo_credito") && !rsmColunas.getString("column").equals("cd_conta_receber"))
						colunas.add(rsmColunas.getString("column"));
				}
				
				String camposInsercao = "";
				String interrogacao = "";
				for(int j = 0; j < colunas.size(); j++){
					camposInsercao += colunas.get(j) + ",";
					interrogacao += "?,";
				}
				camposInsercao = camposInsercao.substring(0, camposInsercao.length()-1);
				interrogacao = interrogacao.substring(0, interrogacao.length()-1);
				
				//Acrescenta +1 no array que representa os registros que serï¿½o deletados
				HashMap<String, Object> valor = new HashMap<String, Object>();
				valor.put("cd_titulo_credito", rsmTituloCredito.getObject("cd_titulo_credito"));
				hashChavesTitulo.add(valor);
				//Busca um array com as chaves estrangeiras
				HashMap <String, Object> chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("adm_titulo_creditoRef"); 
				ArrayList<String> chavesEstrangeiras = new ArrayList<String>();
				for(String chave : chavesEstrangeirasHash.keySet()){
					chavesEstrangeiras.add(chave);
				}
				//Monta o SQL
				PreparedStatement pstmtInsert = connect.prepareStatement("INSERT INTO adm_titulo_credito (cd_titulo_credito, cd_conta_receber, "+camposInsercao+") VALUES (?, ?, "+interrogacao+")");
				//Produz codigo para titulo de credito
				int cdTituloCredito = getNextCodeServer("adm_titulo_credito", connect);
				//Mapeia novo codigo para futura referencia
				mapaCdTituloCredito.put(rsmTituloCredito.getInt("cd_titulo_credito"), cdTituloCredito);
				cdTituloCreditoAntigo.add(rsmTituloCredito.getInt("cd_titulo_credito"));
				pstmtInsert.setObject(1, cdTituloCredito);
				//Caso esteja no array que guarda os codigos antigos, o sistema mapeia para o sistema novo associado a esse antigo, para saber o codigo novo no servidor, caso nao esteja, coloca o codigo antigo
				if(Util.inArrayInteger(rsmTituloCredito.getInt("cd_conta_receber"), cdContaReceberAntigo))
					pstmtInsert.setInt(2, mapaCdContaReceber.get(rsmTituloCredito.getInt("cd_conta_receber")));
				else
					if(rsmTituloCredito.getInt("cd_conta_receber") == 0)
						pstmtInsert.setNull(2, Types.INTEGER);
					else	
						pstmtInsert.setInt(2, rsmTituloCredito.getInt("cd_conta_receber"));
				for(int i = 3; i <= colunas.size()+2; i++){
					//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
					//nas tabelas originais
					if(rsmTituloCredito.getObject(colunas.get(i-3)) instanceof Integer && (Integer)rsmTituloCredito.getObject(colunas.get(i-3)) == 0 && Util.inArrayString(colunas.get(i-3), chavesEstrangeiras))
						pstmtInsert.setNull(i, Types.INTEGER);
					else 
						pstmtInsert.setObject(i, rsmTituloCredito.getObject(colunas.get(i-3)));	
				}
				if(pstmtInsert.executeUpdate() <= 0){
					return new Result(-1, "Erro ao inserir na tabela adm_titulo_credito no banco servidor!");
				}
			}
			//*******************************************************************************************************************************************************
			/**
			 * INSERCAO EM ADM_MOVIMENTO_CONTA
			 */
			//Pesquisa do banco de dados local onde a data de versao ï¿½ nula, ou seja, ï¿½ um dado novo
			pstmtLocal = conLocal.prepareStatement("SELECT * FROM adm_movimento_conta WHERE dt_versao IS NULL");
			rsm = new ResultSetMap(pstmtLocal.executeQuery());
			//Inserir a tabela com um array vazio, e ir acrescentando de acordo a valores que precisando ser inseridos
			hashChaves = new ArrayList<Object>();
			tabelasDelecao.put("adm_movimento_conta", hashChaves);
			while(rsm.next()){
				System.out.println();
				System.out.println("8: Inserï¿½ï¿½o "+count+" da tabela adm_movimento_conta, Cod Movimento Conta " + rsm.getInt("cd_movimento_conta"));
				count++;
								
				//Acrescenta as colunas a um array
				pstmtColunas.setString(1, "adm_movimento_conta");
				ResultSetMap rsmColunas 			 = new ResultSetMap(pstmtColunas.executeQuery());
				ArrayList<String> colunas 			 = new ArrayList<String>();
				while(rsmColunas.next()){
					if(!rsmColunas.getString("column").equals("dt_versao") && !rsmColunas.getString("column").equals("cd_movimento_conta"))
						colunas.add(rsmColunas.getString("column"));
				}
				
				String camposInsercao = "";
				String interrogacao = "";
				for(int j = 0; j < colunas.size(); j++){
					camposInsercao += colunas.get(j) + ",";
					interrogacao += "?,";
				}
				camposInsercao = camposInsercao.substring(0, camposInsercao.length()-1);
				interrogacao = interrogacao.substring(0, interrogacao.length()-1);
				
				//Acrescenta +1 no array que representa os registros que serï¿½o deletados
				HashMap<String, Object> valor = new HashMap<String, Object>();
				valor.put("cd_movimento_conta", rsm.getObject("cd_movimento_conta"));
				valor.put("cd_conta", rsm.getObject("cd_conta"));
				hashChaves.add(valor);
				//Busca um array com as chaves estrangeiras
				HashMap <String, Object> chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("adm_movimento_contaRef"); 
				ArrayList<String> chavesEstrangeiras = new ArrayList<String>();
				for(String chave : chavesEstrangeirasHash.keySet()){
					chavesEstrangeiras.add(chave);
				}
				//Monta o SQL
				PreparedStatement pstmtInsert = connect.prepareStatement("INSERT INTO adm_movimento_conta (cd_movimento_conta, "+camposInsercao+") VALUES (?, "+interrogacao+")");
				//Produz o novo codigo a partir do metodo
				HashMap<String,Object>[] keys = new HashMap[2];
				keys[0] = new HashMap<String,Object>();
				keys[0].put("FIELD_NAME", "cd_movimento_conta");
				keys[0].put("IS_KEY_NATIVE", "YES");
				keys[1] = new HashMap<String,Object>();
				keys[1].put("FIELD_NAME", "cd_conta");
				keys[1].put("IS_KEY_NATIVE", "NO");
				keys[1].put("FIELD_VALUE", new Integer(rsm.getInt("cd_conta")));
				int cdMovimentoConta = getNextCode("adm_movimento_conta", keys, connect);
				pstmtInsert.setObject(1, cdMovimentoConta);
				for(int i = 2; i <= colunas.size()+1; i++){
					//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
					//nas tabelas originais
					if(rsm.getObject(colunas.get(i-2)) instanceof Integer && (Integer)rsm.getObject(colunas.get(i-2)) == 0 && Util.inArrayString(colunas.get(i-2), chavesEstrangeiras))
						pstmtInsert.setNull(i, Types.INTEGER);
					else 
						pstmtInsert.setObject(i, rsm.getObject(colunas.get(i-2)));	
				}
				if(pstmtInsert.executeUpdate() <= 0){
					return new Result(-1, "Erro ao inserir na tabela adm_movimento_conta no banco servidor!");
				}
				//*******************************************************************************************************************************************************
				//Pesquisa do banco de dados local onde o codigo de movimento conta ï¿½ o mesmo do registro que vai ser acrescentado no banco servidor
				PreparedStatement pstmtMovimentoContaReceber = conLocal.prepareStatement("SELECT * FROM adm_movimento_conta_receber WHERE cd_movimento_conta = " + rsm.getInt("cd_movimento_conta") + " AND cd_conta = " + rsm.getInt("cd_conta"));
				ResultSetMap rsmMovimentoContaReceber = new ResultSetMap(pstmtMovimentoContaReceber.executeQuery());
				//Tratamento para que os registros nao sejam sobrepostos
				ArrayList<Object> hashChavesMovContaReceber = new ArrayList<Object>();
				if(tabelasDelecao.get("adm_movimento_conta_receber") == null)
					tabelasDelecao.put("adm_movimento_conta_receber", hashChavesMovContaReceber);
				else
					hashChavesMovContaReceber = (ArrayList<Object>) tabelasDelecao.get("adm_movimento_conta_receber");
				//Inserir a tabela com um array vazio, e ir acrescentando de acordo a valores que precisando ser inseridos
				while(rsmMovimentoContaReceber.next()){
					/**
					 * INSERCAO EM ADM_MOVIMENTO_CONTA_RECEBER
					 */
					System.out.println("9: Inserï¿½ï¿½o "+count+" da tabela adm_movimento_conta_receber, Cod. Conta Receber " + rsm.getInt("cd_conta_receber"));
					//Acrescenta as colunas a um array
					pstmtColunas.setString(1, "adm_movimento_conta_receber");
					rsmColunas 			 = new ResultSetMap(pstmtColunas.executeQuery());
					colunas 			 = new ArrayList<String>();
					while(rsmColunas.next()){
						if(!rsmColunas.getString("column").equals("dt_versao") && !rsmColunas.getString("column").equals("cd_movimento_conta") && !rsmColunas.getString("column").equals("cd_conta_receber"))
							colunas.add(rsmColunas.getString("column"));
					}
					
					camposInsercao = "";
					interrogacao = "";
					for(int j = 0; j < colunas.size(); j++){
						camposInsercao += colunas.get(j) + ",";
						interrogacao += "?,";
					}
					camposInsercao = camposInsercao.substring(0, camposInsercao.length()-1);
					interrogacao = interrogacao.substring(0, interrogacao.length()-1);
					
					//Acrescenta +1 no array que representa os registros que serï¿½o deletados
					valor = new HashMap<String, Object>();
					valor.put("cd_movimento_conta", rsmMovimentoContaReceber.getObject("cd_movimento_conta"));
					valor.put("cd_conta", rsmMovimentoContaReceber.getObject("cd_conta"));
					valor.put("cd_conta_receber", rsmMovimentoContaReceber.getObject("cd_conta_receber"));
					hashChavesMovContaReceber.add(valor);
					//Busca um array com as chaves estrangeiras
					chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("adm_movimento_conta_receberRef"); 
					chavesEstrangeiras = new ArrayList<String>();
					for(String chave : chavesEstrangeirasHash.keySet()){
						chavesEstrangeiras.add(chave);
					}
					//Monta o SQL
					pstmtInsert = connect.prepareStatement("INSERT INTO adm_movimento_conta_receber (cd_movimento_conta, cd_conta_receber, "+camposInsercao+") VALUES (?, ?, "+interrogacao+")");
					pstmtInsert.setObject(1, cdMovimentoConta);
					//Caso esteja no array que guarda os codigos antigos, o sistema mapeia para o sistema novo associado a esse antigo, para saber o codigo novo no servidor, caso nao esteja, coloca o codigo antigo
					if(Util.inArrayInteger(rsmMovimentoContaReceber.getInt("cd_conta_receber"), cdContaReceberAntigo))
						pstmtInsert.setInt(2, mapaCdContaReceber.get(rsmMovimentoContaReceber.getInt("cd_conta_receber")));
					else
						if(rsmMovimentoContaReceber.getInt("cd_conta_receber") == 0)
							pstmtInsert.setNull(2, Types.INTEGER);
						else	
							pstmtInsert.setInt(2, rsmMovimentoContaReceber.getInt("cd_conta_receber"));
					
					for(int i = 3; i <= colunas.size()+2; i++){
						//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
						//nas tabelas originais
						if(rsmMovimentoContaReceber.getObject(colunas.get(i-3)) instanceof Integer && (Integer)rsmMovimentoContaReceber.getObject(colunas.get(i-3)) == 0 && Util.inArrayString(colunas.get(i-3), chavesEstrangeiras))
							pstmtInsert.setNull(i, Types.INTEGER);
						else 
							pstmtInsert.setObject(i, rsmMovimentoContaReceber.getObject(colunas.get(i-3)));	
					}
					if(pstmtInsert.executeUpdate() <= 0){
						return new Result(-1, "Erro ao inserir na tabela adm_titulo_credito no banco servidor!");
					}
				}
				//*******************************************************************************************************************************************************
				//Pesquisa do banco de dados local onde o codigo de movimento conta ï¿½ o mesmo do registro que vai ser acrescentado no banco servidor
				PreparedStatement pstmtMovimentoContaCategoria = conLocal.prepareStatement("SELECT * FROM adm_movimento_conta_categoria WHERE cd_movimento_conta = " + rsm.getInt("cd_movimento_conta") + " AND cd_conta = " + rsm.getInt("cd_conta"));
				ResultSetMap rsmMovimentoContaCategoria = new ResultSetMap(pstmtMovimentoContaCategoria.executeQuery());
				//Tratamento para que os registros nao sejam sobrepostos
				ArrayList<Object> hashChavesMovCategoria = new ArrayList<Object>();
				if(tabelasDelecao.get("adm_movimento_conta_categoria") == null)
					tabelasDelecao.put("adm_movimento_conta_categoria", hashChavesMovCategoria);
				else
					hashChavesMovCategoria = (ArrayList<Object>) tabelasDelecao.get("adm_movimento_conta_categoria");
				//Inserir a tabela com um array vazio, e ir acrescentando de acordo a valores que precisando ser inseridos
				while(rsmMovimentoContaCategoria.next()){
					/**
					 * INSERCAO EM ADM_MOVIMENTO_CONTA_CATEGORIA
					 */
					System.out.println("10: Inserï¿½ï¿½o "+count+" da tabela adm_movimento_conta_categoria, Cod. Categoria " + rsmMovimentoContaCategoria.getInt("cd_categoria_economica"));
					//Acrescenta as colunas a um array
					pstmtColunas.setString(1, "adm_movimento_conta_categoria");
					rsmColunas 			 = new ResultSetMap(pstmtColunas.executeQuery());
					colunas 			 = new ArrayList<String>();
					while(rsmColunas.next()){
						if(!rsmColunas.getString("column").equals("dt_versao") && !rsmColunas.getString("column").equals("cd_movimento_conta_categoria")  && !rsmColunas.getString("column").equals("cd_movimento_conta") && !rsmColunas.getString("column").equals("cd_conta_receber"))
							colunas.add(rsmColunas.getString("column"));
					}
					
					camposInsercao = "";
					interrogacao = "";
					for(int j = 0; j < colunas.size(); j++){
						camposInsercao += colunas.get(j) + ",";
						interrogacao += "?,";
					}
					camposInsercao = camposInsercao.substring(0, camposInsercao.length()-1);
					interrogacao = interrogacao.substring(0, interrogacao.length()-1);
					
					//Acrescenta +1 no array que representa os registros que serï¿½o deletados
					valor = new HashMap<String, Object>();
					valor.put("cd_movimento_conta", rsmMovimentoContaCategoria.getObject("cd_movimento_conta"));
					valor.put("cd_conta", rsmMovimentoContaCategoria.getObject("cd_conta"));
					valor.put("cd_categoria_economica", rsmMovimentoContaCategoria.getObject("cd_categoria_economica"));
					valor.put("cd_movimento_conta_categoria", rsmMovimentoContaCategoria.getObject("cd_movimento_conta_categoria"));
					hashChavesMovCategoria.add(valor);
					//Busca um array com as chaves estrangeiras
					chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("adm_movimento_conta_categoriaRef"); 
					chavesEstrangeiras = new ArrayList<String>();
					for(String chave : chavesEstrangeirasHash.keySet()){
						chavesEstrangeiras.add(chave);
					}
					//Monta o SQL
					pstmtInsert = connect.prepareStatement("INSERT INTO adm_movimento_conta_categoria (cd_movimento_conta_categoria, cd_movimento_conta, cd_conta_receber, "+camposInsercao+") VALUES (?, ?, ?, "+interrogacao+")");
					int cdMovimentoContaCategoria = getNextCodeServer("adm_movimento_conta_categoria", connect);					
					pstmtInsert.setObject(1, cdMovimentoContaCategoria);
					pstmtInsert.setObject(2, cdMovimentoConta);
					//Caso esteja no array que guarda os codigos antigos, o sistema mapeia para o sistema novo associado a esse antigo, para saber o codigo novo no servidor, caso nao esteja, coloca o codigo antigo
					if(Util.inArrayInteger(rsmMovimentoContaCategoria.getInt("cd_conta_receber"), cdContaReceberAntigo)){
						pstmtInsert.setInt(3, mapaCdContaReceber.get(rsmMovimentoContaCategoria.getInt("cd_conta_receber")));
					}
					else{
						if(rsmMovimentoContaCategoria.getInt("cd_conta_receber") == 0)
							pstmtInsert.setNull(3, Types.INTEGER);
						else	
							pstmtInsert.setInt(3, rsmMovimentoContaCategoria.getInt("cd_conta_receber"));
					}
					for(int i = 4; i <= colunas.size()+3; i++){
						//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
						//nas tabelas originais
						if(rsmMovimentoContaCategoria.getObject(colunas.get(i-4)) instanceof Integer && (Integer)rsmMovimentoContaCategoria.getObject(colunas.get(i-4)) == 0 && Util.inArrayString(colunas.get(i-4), chavesEstrangeiras))
							pstmtInsert.setNull(i, Types.INTEGER);
						else 
							pstmtInsert.setObject(i, rsmMovimentoContaCategoria.getObject(colunas.get(i-4)));	
					}
					if(pstmtInsert.executeUpdate() <= 0){
						return new Result(-1, "Erro ao inserir na tabela adm_movimento_conta_categoria no banco servidor!");
					}
				}
				//*******************************************************************************************************************************************************
				//Pesquisa do banco de dados local onde o codigo de movimento conta ï¿½ o mesmo do registro que vai ser acrescentado no banco servidor
				PreparedStatement pstmtMovimentoTituloCredito = conLocal.prepareStatement("SELECT * FROM adm_movimento_titulo_credito WHERE cd_movimento_conta = " + rsm.getInt("cd_movimento_conta") + " AND cd_conta = " + rsm.getInt("cd_conta"));
				ResultSetMap rsmMovimentoTituloCredito = new ResultSetMap(pstmtMovimentoTituloCredito.executeQuery());
				//Tratamento para que os registros nao sejam sobrepostos
				ArrayList<Object> hashChavesMovTitulo = new ArrayList<Object>();
				if(tabelasDelecao.get("adm_movimento_titulo_credito") == null)
					tabelasDelecao.put("adm_movimento_titulo_credito", hashChavesMovTitulo);
				else
					hashChavesMovTitulo = (ArrayList<Object>) tabelasDelecao.get("adm_movimento_titulo_credito");
				//Inserir a tabela com um array vazio, e ir acrescentando de acordo a valores que precisando ser inseridos
				while(rsmMovimentoTituloCredito.next()){
					/**
					 * INSERCAO EM ADM_MOVIMENTO_TITULO_CREDITO
					 */
					System.out.println("11: Inserï¿½ï¿½o "+count+" da tabela adm_movimento_conta_receber, Cod. Titulo Credito " + rsm.getInt("cd_titulo_credito"));
					//Acrescenta as colunas a um array
					pstmtColunas.setString(1, "adm_movimento_titulo_credito");
					rsmColunas 			 = new ResultSetMap(pstmtColunas.executeQuery());
					colunas 			 = new ArrayList<String>();
					while(rsmColunas.next()){
						if(!rsmColunas.getString("column").equals("dt_versao") && !rsmColunas.getString("column").equals("cd_movimento_conta") && !rsmColunas.getString("column").equals("cd_titulo_credito"))
							colunas.add(rsmColunas.getString("column"));
					}
					
					camposInsercao = "";
					interrogacao = "";
					for(int j = 0; j < colunas.size(); j++){
						camposInsercao += colunas.get(j) + ",";
						interrogacao += "?,";
					}
					camposInsercao = camposInsercao.substring(0, camposInsercao.length()-1);
					interrogacao = interrogacao.substring(0, interrogacao.length()-1);
					
					//Acrescenta +1 no array que representa os registros que serï¿½o deletados
					valor = new HashMap<String, Object>();
					valor.put("cd_movimento_conta", rsmMovimentoTituloCredito.getObject("cd_movimento_conta"));
					valor.put("cd_conta", rsmMovimentoTituloCredito.getObject("cd_conta"));
					valor.put("cd_titulo_credito", rsmMovimentoTituloCredito.getObject("cd_titulo_credito"));
					hashChavesMovTitulo.add(valor);
					//Busca um array com as chaves estrangeiras
					chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("adm_movimento_titulo_creditoRef"); 
					chavesEstrangeiras = new ArrayList<String>();
					for(String chave : chavesEstrangeirasHash.keySet()){
						chavesEstrangeiras.add(chave);
					}
					//Monta o SQL
					pstmtInsert = connect.prepareStatement("INSERT INTO adm_movimento_titulo_credito (cd_movimento_conta, cd_titulo_credito, "+camposInsercao+") VALUES (?, ?, "+interrogacao+")");
					pstmtInsert.setObject(1, cdMovimentoConta);
					//Caso esteja no array que guarda os codigos antigos, o sistema mapeia para o sistema novo associado a esse antigo, para saber o codigo novo no servidor, caso nao esteja, coloca o codigo antigo
					if(Util.inArrayInteger(rsmMovimentoTituloCredito.getInt("cd_titulo_credito"), cdTituloCreditoAntigo)){
						pstmtInsert.setInt(2, mapaCdTituloCredito.get(rsmMovimentoTituloCredito.getInt("cd_titulo_credito")));
					}
					else{
						if(rsmMovimentoTituloCredito.getInt("cd_titulo_credito") == 0)
							pstmtInsert.setNull(2, Types.INTEGER);
						else	
							pstmtInsert.setInt(2, rsmMovimentoTituloCredito.getInt("cd_titulocredito"));
					}
					for(int i = 3; i <= colunas.size()+2; i++){
						//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
						//nas tabelas originais
						if(rsmMovimentoTituloCredito.getObject(colunas.get(i-3)) instanceof Integer && (Integer)rsmMovimentoTituloCredito.getObject(colunas.get(i-3)) == 0 && Util.inArrayString(colunas.get(i-3), chavesEstrangeiras))
							pstmtInsert.setNull(i, Types.INTEGER);
						else 
							pstmtInsert.setObject(i, rsmMovimentoTituloCredito.getObject(colunas.get(i-3)));	
					}
					if(pstmtInsert.executeUpdate() <= 0){
						return new Result(-1, "Erro ao inserir na tabela adm_movimento_titulo_credito no banco servidor!");
					}
				}
				//*******************************************************************************************************************************************************
			}
			
			
			System.out.println("Concluida atualizaï¿½ï¿½o de Vendas - Cliente -> Servidor !");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar sincronizar bancos: " + e, e);
		}
	}

	@SuppressWarnings("unchecked")
	public static sol.util.Result atualizarServidorCliente(int cdEmpresa, HashMap<String, Object> tabelasDelecao, ArrayList<Integer> cdPessoaAntigo, HashMap<Integer, Integer> mapaCdPessoa, Connection connect, Connection conLocal){
		try {
			
			//Pesquisa do banco de dados local onde a data de versao ï¿½ nula, ou seja, ï¿½ um dado novo
			PreparedStatement pstmtLocal = conLocal.prepareStatement("SELECT A.*, B.*, C.*, D.* FROM grl_pessoa A " +
																     "LEFT OUTER JOIN grl_pessoa_fisica   B ON (A.cd_pessoa = B.cd_pessoa) " +
																     "LEFT OUTER JOIN grl_pessoa_juridica C ON (A.cd_pessoa = C.cd_pessoa) " +
																     "LEFT OUTER JOIN grl_pessoa_endereco D ON (A.cd_pessoa = D.cd_pessoa" +
																     "												AND D.lg_principal = 1) " +
																     " WHERE A.dt_versao IS NULL");
			
			//Pesquisa do banco de dados servidor as mesmas informacoes do banco local
			PreparedStatement pstmtServidor = connect.prepareStatement("SELECT A.*, B.*, C.*, D.* FROM grl_pessoa A " +
																	   "LEFT OUTER JOIN grl_pessoa_fisica   B ON (A.cd_pessoa = B.cd_pessoa) " +
																	   "LEFT OUTER JOIN grl_pessoa_juridica C ON (A.cd_pessoa = C.cd_pessoa) " +
																	   "LEFT OUTER JOIN grl_pessoa_endereco D ON (A.cd_pessoa = D.cd_pessoa" +
																	     "												AND D.lg_principal = 1) " +
																	   "WHERE ((B.nr_cpf <> '' AND B.nr_cpf IS NOT NULL AND B.nr_cpf = ?) OR (C.nr_cnpj <> '' AND C.nr_cnpj IS NOT NULL AND C.nr_cnpj = ?))");
			
			//Array que guarda os codigos antigos de pessoa para identificar se algo foi atualizado em pessoa fisica, juridica ou endereco que nao eh de uma pessoa nova no sistema
			ArrayList<Integer> cdPessoaNovo = new ArrayList<Integer>();
			
			//SQL para identificar o nome das colunas de uma determinada tabela
			PreparedStatement pstmt = connect.prepareStatement("SELECT a.attname AS \"column\" " +
																"	FROM pg_catalog.pg_attribute a " +
																"	INNER JOIN pg_stat_user_tables c on a.attrelid = c.relid " +
																"	WHERE " +
																"	a.attnum > 0 AND " +
																"	NOT a.attisdropped " +
																"	AND relname = ? " +
																"ORDER BY a.attname");
			
			//Pesquisa do banco de dados local os enderecos de uma pessoa
			PreparedStatement pstmtPessoaEnderecoLocal  = conLocal.prepareStatement("SELECT * FROM grl_pessoa_endereco WHERE cd_pessoa = ?");
			//Pesquisa do banco de dados local onde a data de versao ï¿½ nula e o registro nao tem relacao com uma pessoa nova, ou seja, foi acrescido um endereï¿½o ao cliente
			PreparedStatement pstmtLocalEndereco = conLocal.prepareStatement("SELECT * FROM grl_pessoa_endereco WHERE dt_versao IS NULL");
			
			//Pesquisa do banco de dados local onde a data de versao ï¿½ nula e o registro nao tem relacao com uma pessoa nova, ou seja, foi acrescido um endereï¿½o ao cliente
			PreparedStatement pstmtLocalPessoaEmpresa = conLocal.prepareStatement("SELECT * FROM grl_pessoa_empresa WHERE dt_versao IS NULL");
			
			//Busca os enderecos da pessoa no banco servidor
			PreparedStatement pstmtPessoaEnderecoServidor = connect.prepareStatement("SELECT * FROM grl_pessoa_endereco WHERE cd_pessoa = ?"); 
			
			//Busca os vinculos da pessoa em uma empresa
			PreparedStatement pstmtPessoaEmpresaServidor = connect.prepareStatement("SELECT * FROM grl_pessoa_empresa WHERE cd_pessoa = ? AND cd_vinculo = ? AND cd_empresa = " +cdEmpresa); 
			
			//Busca os vinculos da pessoa em uma empresa
			PreparedStatement pstmtPessoaEmpresaLocal = conLocal.prepareStatement("SELECT * FROM grl_pessoa_empresa WHERE cd_pessoa = ? AND cd_empresa = " +cdEmpresa); 
			
			//Inserir a tabela com um array vazio, e ir acrescentando de acordo a valores que precisando ser inseridos
			ArrayList<Object> hashChaves = new ArrayList<Object>();
			tabelasDelecao.put("grl_pessoa", hashChaves);
			ResultSetMap rsmLocal = new ResultSetMap(pstmtLocal.executeQuery());
			//Contagem de registros inseridos ou atualizados
			int count = 0;
			//Busca as informaï¿½ï¿½es das tabelas a serem sincronizadas
			Result resultado = getTabelasSincronizacao();
			HashMap<String, Object> tabelasSincronizadas = (HashMap<String, Object>) resultado.getObjects().get("tabelasSincronizadas"); 
			while(rsmLocal.next()){
				//*******************************************************************************************************************************************************
				//Tratamento do caso de dois clientes inserirem a mesma pessoa no sistema, e ambos tentarem inseri-la no servidor
				//Sera considerada a mesma pessoa
				pstmtServidor.setString(1, rsmLocal.getString("nr_cpf"));
				pstmtServidor.setString(2, rsmLocal.getString("nr_cnpj"));
				ResultSetMap rsmServidor = new ResultSetMap(pstmtServidor.executeQuery());
				if(rsmServidor.next()){
					//ATUALIZA GRL_PESSOA
					System.out.println();
					System.out.println("1: Atualizaï¿½ï¿½o "+count+" da tabela grl_pessoa " + rsmLocal.getString("nm_pessoa"));
					count++;
					
					//Montando colunas
					pstmt.setString(1, "grl_pessoa");
					ResultSetMap rsmColunas = new ResultSetMap(pstmt.executeQuery());
					ArrayList<String> colunas = new ArrayList<String>();
					while(rsmColunas.next())
						if(!rsmColunas.getString("column").equals("cd_pessoa"))
							colunas.add(rsmColunas.getString("column"));
					
					//O sistema buscarï¿½ todas as colunas da tabela para montar o SQL referente a atualizaï¿½ï¿½o junto com as chaves
					String camposAtualizacao = "";
					for(int j = 0; j < colunas.size(); j++){
						camposAtualizacao += colunas.get(j) + "=?,";
					}
					camposAtualizacao = camposAtualizacao.substring(0, camposAtualizacao.length()-1);
					
					//Montando chave primaria
					ArrayList<String> chaves = new ArrayList<String>();
					chaves.add("cd_pessoa");
					String chave = "";
					for(int i = 0; i < chaves.size(); i++)
						chave += chaves.get(i)+"=? AND ";
					chave = chave.substring(0, chave.length() - 5);
					//Busca um array com as chaves estrangeiras
					HashMap <String, Object> chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("grl_pessoaRef"); 
					ArrayList<String> chavesEstrangeiras = new ArrayList<String>();
					for(String chaveE : chavesEstrangeirasHash.keySet()){
						chavesEstrangeiras.add(chaveE);
					}
					//Monta o SQL
					PreparedStatement pstmtUpdate = connect.prepareStatement("UPDATE grl_pessoa set "+camposAtualizacao+" WHERE "+chave);
					//Insere os novos valores no SQL, primeiro os valores de atualizaï¿½ï¿½o, depois os valores da chave
					int i = 1;
					for(; i <= colunas.size(); i++){
						//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
						//nas tabelas originais
						if(rsmLocal.getObject(colunas.get(i-1)) instanceof Integer && (Integer)rsmLocal.getObject(colunas.get(i-1)) == 0 && Util.inArrayString(colunas.get(i-1), chavesEstrangeiras)){
							pstmtUpdate.setNull(i, Types.INTEGER);
						}
						else 
							//Caso no servidor tenha algum dado e no cliente o campo estiver nulo, prevalece o dado do servidor
							if(rsmLocal.getObject(colunas.get(i-1)) == null && rsmServidor.getObject(colunas.get(i-1)) != null)
								pstmtUpdate.setObject(i, rsmServidor.getObject(colunas.get(i-1)));
							else
								pstmtUpdate.setObject(i, rsmLocal.getObject(colunas.get(i-1)));
					}
					for(int j = 0, k = i ; j < chaves.size(); j++, k++){
						pstmtUpdate.setObject(k, rsmServidor.getObject(chaves.get(j)));
					}
					if(pstmtUpdate.executeUpdate() <= 0){
						return new Result(-1, "Erro ao atualizar na tabela grl_pessoa no banco servidor!");
					}
					//Apenas entrarï¿½ se a pessoa for pessoa fisica
					if(rsmServidor.getString("nr_cpf") != null && !rsmServidor.getString("nr_cpf").equals("")){
						//ATUALIZA GRL_PESSOA_FISICA
						System.out.println("2: Atualizaï¿½ï¿½o "+count+" da tabela grl_pessoa_fisica");
						
						//Montando colunas
						pstmt.setString(1, "grl_pessoa_fisica");
						ResultSetMap rsmColunasFisica = new ResultSetMap(pstmt.executeQuery());
						ArrayList<String> colunasFisica = new ArrayList<String>();
						while(rsmColunasFisica.next())
							if(!rsmColunasFisica.getString("column").equals("cd_pessoa"))
								colunasFisica.add(rsmColunasFisica.getString("column"));
						
						//O sistema buscarï¿½ todas as colunas da tabela para montar o SQL referente a atualizaï¿½ï¿½o junto com as chaves
						camposAtualizacao = "";
						for(int j = 0; j < colunasFisica.size(); j++){
							camposAtualizacao += colunasFisica.get(j) + "=?,";
						}
						camposAtualizacao = camposAtualizacao.substring(0, camposAtualizacao.length()-1);
						
						//Montando chave primaria
						chaves = new ArrayList<String>();
						chaves.add("cd_pessoa");
						chave = "";
						for(i = 0; i < chaves.size(); i++)
							chave += chaves.get(i)+"=? AND ";
						chave = chave.substring(0, chave.length() - 5);
						//Busca um array com as chaves estrangeiras
						chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("grl_pessoa_fisicaRef"); 
						chavesEstrangeiras = new ArrayList<String>();
						for(String chaveE : chavesEstrangeirasHash.keySet()){
							chavesEstrangeiras.add(chaveE);
						}
						
						//Monta o SQL
						pstmtUpdate = connect.prepareStatement("UPDATE grl_pessoa_fisica set "+camposAtualizacao+" WHERE "+chave);
						//Insere os novos valores no SQL, primeiro os valores de atualizaï¿½ï¿½o, depois os valores da chave
						i = 1;
						for(; i <= colunasFisica.size(); i++){
							//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
							//nas tabelas originais
							if(rsmLocal.getObject(colunasFisica.get(i-1)) instanceof Integer && (Integer)rsmLocal.getObject(colunasFisica.get(i-1)) == 0 && Util.inArrayString(colunasFisica.get(i-1), chavesEstrangeiras)){
								pstmtUpdate.setNull(i, Types.INTEGER);
							}
							else 
								//Caso no servidor tenha algum dado e no cliente o campo estiver nulo, prevalece o dado do servidor
								if(rsmLocal.getObject(colunasFisica.get(i-1)) == null && rsmServidor.getObject(colunasFisica.get(i-1)) != null)
									pstmtUpdate.setObject(i, rsmServidor.getObject(colunasFisica.get(i-1)));
								else
									pstmtUpdate.setObject(i, rsmLocal.getObject(colunasFisica.get(i-1)));
						}
						for(int j = 0, k = i ; j < chaves.size(); j++, k++){
							pstmtUpdate.setObject(k, rsmServidor.getObject(chaves.get(j)));
						}
						if(pstmtUpdate.executeUpdate() <= 0){
							return new Result(-1, "Erro ao atualizar na tabela grl_pessoa_fisica no banco servidor!");
						}
					}
					
					//Apenas entrarï¿½ se a pessoa for pessoa juridica
					else if(rsmServidor.getString("nr_cnpj") != null && !rsmServidor.getString("nr_cnpj").equals("")){
						//ATUALIZA GRL_PESSOA_JURIDICA
						System.out.println("3: Atualizaï¿½ï¿½o "+count+" da tabela grl_pessoa_juridica");
						
						//Montando colunas
						pstmt.setString(1, "grl_pessoa_juridica");
						ResultSetMap rsmColunasJuridica = new ResultSetMap(pstmt.executeQuery());
						ArrayList<String> colunasJuridica = new ArrayList<String>();
						while(rsmColunasJuridica.next())
							if(!rsmColunasJuridica.getString("column").equals("cd_pessoa"))
								colunasJuridica.add(rsmColunasJuridica.getString("column"));
						
						//O sistema buscarï¿½ todas as colunas da tabela para montar o SQL referente a atualizaï¿½ï¿½o junto com as chaves
						camposAtualizacao = "";
						for(int j = 0; j < colunasJuridica.size(); j++){
							camposAtualizacao += colunasJuridica.get(j) + "=?,";
						}
						camposAtualizacao = camposAtualizacao.substring(0, camposAtualizacao.length()-1);
						
						//Montando chave primaria
						chaves = new ArrayList<String>();
						chaves.add("cd_pessoa");
						chave = "";
						for(i = 0; i < chaves.size(); i++)
							chave += chaves.get(i)+"=? AND ";
						chave = chave.substring(0, chave.length() - 5);
						//Busca um array com as chaves estrangeiras
						chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("grl_pessoa_juridicaRef"); 
						chavesEstrangeiras = new ArrayList<String>();
						for(String chaveE : chavesEstrangeirasHash.keySet()){
							chavesEstrangeiras.add(chaveE);
						}
						//Monta o SQL
						pstmtUpdate = connect.prepareStatement("UPDATE grl_pessoa_juridica set "+camposAtualizacao+" WHERE "+chave);
						//Insere os novos valores no SQL, primeiro os valores de atualizaï¿½ï¿½o, depois os valores da chave
						i = 1;
						for(; i <= colunasJuridica.size(); i++){
							//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
							//nas tabelas originais
							if(rsmLocal.getObject(colunasJuridica.get(i-1)) instanceof Integer && (Integer)rsmLocal.getObject(colunasJuridica.get(i-1)) == 0 && Util.inArrayString(colunasJuridica.get(i-1), chavesEstrangeiras)){
								pstmtUpdate.setNull(i, Types.INTEGER);
							}
							else 
								//Caso no servidor tenha algum dado e no cliente o campo estiver nulo, prevalece o dado do servidor
								if(rsmLocal.getObject(colunasJuridica.get(i-1)) == null && rsmServidor.getObject(colunasJuridica.get(i-1)) != null)
									pstmtUpdate.setObject(i, rsmServidor.getObject(colunasJuridica.get(i-1)));
								else
									pstmtUpdate.setObject(i, rsmLocal.getObject(colunasJuridica.get(i-1)));
						}
						for(int j = 0, k = i ; j < chaves.size(); j++, k++){
							pstmtUpdate.setObject(k, rsmServidor.getObject(chaves.get(j)));
						}
						if(pstmtUpdate.executeUpdate() <= 0){
							return new Result(-1, "Erro ao atualizar na tabela grl_pessoa_juridica no banco servidor!");
						}
					}
					//Tratamento para que os registros nao sejam sobrepostos
					ArrayList<Object> hashChavesPessoaEndereco = new ArrayList<Object>();
					if(tabelasDelecao.get("grl_pessoa_endereco") == null)
						tabelasDelecao.put("grl_pessoa_endereco", hashChavesPessoaEndereco);
					else
						hashChavesPessoaEndereco = (ArrayList<Object>) tabelasDelecao.get("grl_pessoa_endereco");
					
					//:TODO modificar para que possa iterar sobre todos os endereï¿½os da pessoa tanto no banco de dados do cliente quanto do servidor
					//Inclui ou atualiza enderecos, baseado no cep ou numero do endereï¿½o do registro do servidor e do local
					if(((rsmLocal.getString("nr_cep") != null || rsmServidor.getString("nr_cep") == null) || (rsmLocal.getString("nr_cep") != null && rsmServidor.getString("nr_cep") != null && rsmLocal.getString("nr_cep").equals(rsmServidor.getString("nr_cep")))) || 
						((rsmLocal.getString("nr_endereco") == null || rsmServidor.getString("nr_endereco") == null) || (rsmLocal.getString("nr_endereco") != null && rsmServidor.getString("nr_endereco") != null && rsmLocal.getString("nr_endereco").equals(rsmServidor.getString("nr_endereco"))))){
						//ATUALIZA GRL_PESSOA_ENDERECO
						System.out.println("4: Atualizaï¿½ï¿½o "+count+" da tabela grl_pessoa_endereco, Cod Endereco " + rsmLocal.getInt("cd_endereco"));
						
						//Montando colunas
						pstmt.setString(1, "grl_pessoa_endereco");
						ResultSetMap rsmColunasEndereco = new ResultSetMap(pstmt.executeQuery());
						ArrayList<String> colunasEndereco = new ArrayList<String>();
						while(rsmColunasEndereco.next())
							if(!rsmColunasEndereco.getString("column").equals("cd_pessoa") && !rsmColunasEndereco.getString("column").equals("cd_endereco"))
								colunasEndereco.add(rsmColunasEndereco.getString("column"));
						
						//O sistema buscarï¿½ todas as colunas da tabela para montar o SQL referente a atualizaï¿½ï¿½o junto com as chaves
						camposAtualizacao = "";
						for(int j = 0; j < colunasEndereco.size(); j++){
							camposAtualizacao += colunasEndereco.get(j) + "=?,";
						}
						camposAtualizacao = camposAtualizacao.substring(0, camposAtualizacao.length()-1);
						
						//Montando chave primaria
						chaves = new ArrayList<String>();
						chaves.add("cd_pessoa");
						chave = "";
						for(i = 0; i < chaves.size(); i++)
							chave += chaves.get(i)+"=? AND ";
						chave = chave.substring(0, chave.length() - 5);
						//Busca um array com as chaves estrangeiras
						chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("grl_pessoa_enderecoRef"); 
						chavesEstrangeiras = new ArrayList<String>();
						for(String chaveE : chavesEstrangeirasHash.keySet()){
							chavesEstrangeiras.add(chaveE);
						}
						//Monta o SQL
						pstmtUpdate = connect.prepareStatement("UPDATE grl_pessoa_endereco set "+camposAtualizacao+" WHERE "+chave);
						//Insere os novos valores no SQL, primeiro os valores de atualizaï¿½ï¿½o, depois os valores da chave
						i = 1;
						for(; i <= colunasEndereco.size(); i++){
							//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
							//nas tabelas originais
							if(rsmLocal.getObject(colunasEndereco.get(i-1)) instanceof Integer && (Integer)rsmLocal.getObject(colunasEndereco.get(i-1)) == 0 && Util.inArrayString(colunasEndereco.get(i-1), chavesEstrangeiras)){
								pstmtUpdate.setNull(i, Types.INTEGER);
							}
							else 
								//Caso no servidor tenha algum dado e no cliente o campo estiver nulo, prevalece o dado do servidor
								if(rsmLocal.getObject(colunasEndereco.get(i-1)) == null && rsmServidor.getObject(colunasEndereco.get(i-1)) != null)
									pstmtUpdate.setObject(i, rsmServidor.getObject(colunasEndereco.get(i-1)));
								else
									pstmtUpdate.setObject(i, rsmLocal.getObject(colunasEndereco.get(i-1)));
						}
						for(int j = 0, k = i ; j < chaves.size(); j++, k++){
							pstmtUpdate.setObject(k, rsmServidor.getObject(chaves.get(j)));
						}
						if(pstmtUpdate.executeUpdate() <= 0){
							return new Result(-1, "Erro ao atualizar na tabela grl_pessoa_endereco no banco servidor!");
						}
					}
					else{
						/**
						 * INSERCAO EM GRL_PESSOA_ENDERECO
						 */
						System.out.println("12: Inserï¿½ï¿½o "+count+" da tabela grl_pessoa_endereco");
						//Acrescenta as colunas a um array
						pstmt.setString(1, "grl_pessoa_endereco");
						ResultSetMap rsmColunasEndereco 			 = new ResultSetMap(pstmt.executeQuery());
						ArrayList<String> colunasEndereco 			 = new ArrayList<String>();
						while(rsmColunasEndereco.next()){
							if(!rsmColunasEndereco.getString("column").equals("dt_versao") && !rsmColunasEndereco.getString("column").equals("cd_pessoa"))
								colunasEndereco.add(rsmColunasEndereco.getString("column"));
						}
						
						//O sistema buscarï¿½ todas as colunas da tabela alm_documento_saida para montar o SQL referente a insercao junto com as interrogaï¿½ï¿½es
						String camposInsercao = "";
						String interrogacao = "";
						for(int j = 0; j < colunasEndereco.size(); j++){
							camposInsercao += colunasEndereco.get(j) + ",";
							interrogacao += "?,";
						}
						camposInsercao = camposInsercao.substring(0, camposInsercao.length()-1);
						interrogacao = interrogacao.substring(0, interrogacao.length()-1);
						
						//Acrescenta +1 no array que representa os registros que serï¿½o deletados
						HashMap<String, Object> valor = new HashMap<String, Object>();
						valor.put("cd_endereco", rsmLocal.getObject("cd_endereco"));
						valor.put("cd_pessoa", rsmLocal.getObject("cd_pessoa"));
						hashChavesPessoaEndereco.add(valor);
						//Busca um array com as chaves estrangeiras
						chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("grl_pessoa_enderecoRef"); 
						chavesEstrangeiras = new ArrayList<String>();
						for(String chaveE : chavesEstrangeirasHash.keySet()){
							chavesEstrangeiras.add(chaveE);
						}
						//Monta o SQL
						PreparedStatement pstmtInsert = connect.prepareStatement("INSERT INTO grl_pessoa_endereco (cd_pessoa, "+camposInsercao+") VALUES (?, "+interrogacao+")");
						//Insere os dados do banco do servidor no novo registro do banco local
						pstmtInsert.setObject(1, rsmServidor.getObject("cd_pessoa"));
						for(int j = 2; j <= colunas.size()+1; j++){
							//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
							//nas tabelas originais
							if(rsmLocal.getObject(colunasEndereco.get(j-2)) instanceof Integer && (Integer)rsmLocal.getObject(colunasEndereco.get(j-2)) == 0 && Util.inArrayString(colunasEndereco.get(j-2), chavesEstrangeiras))
								pstmtInsert.setNull(j, Types.INTEGER);
							else 
								pstmtInsert.setObject(j, rsmLocal.getObject(colunasEndereco.get(j-2)));	
						}
						if(pstmtInsert.executeUpdate() <= 0){
							return new Result(-1, "Erro ao inserir na tabela grl_pessoa_endereco no banco servidor!");
						}
					}
					//*******************************************************************************************************************************************************
					//Tratamento para que os registros nao sejam sobrepostos
					ArrayList<Object> hashChavesPessoaEmpresa = new ArrayList<Object>();
					if(tabelasDelecao.get("grl_pessoa_empresa") == null)
						tabelasDelecao.put("grl_pessoa_empresa", hashChavesPessoaEmpresa);
					else
						hashChavesPessoaEmpresa = (ArrayList<Object>) tabelasDelecao.get("grl_pessoa_empresa");
					
					pstmtPessoaEmpresaLocal.setInt(1, rsmLocal.getInt("cd_pessoa"));
					ResultSetMap rsmPessoaEmpresaLocal = new ResultSetMap(pstmtPessoaEmpresaLocal.executeQuery());
					//Itera sobre todos os vinculos daquela pessoa no banco local, e vï¿½ se algum deles ï¿½ diferente do banco servidor, incluindo neste
					while(rsmPessoaEmpresaLocal.next()){
						pstmtPessoaEmpresaServidor.setInt(1, rsmServidor.getInt("cd_pessoa"));
						pstmtPessoaEmpresaServidor.setInt(2, rsmPessoaEmpresaLocal.getInt("cd_vinculo"));
						//Inclui pessoa empresa, caso determinado vinculo para uma determinada pessoa ainda nï¿½o esteja no servidor
						if(!new ResultSetMap(pstmtPessoaEmpresaServidor.executeQuery()).next()){
							/**
							 * INSERCAO EM GRL_PESSOA_EMPRESA
							 */
							System.out.println("13: Inserï¿½ï¿½o "+count+" da tabela grl_pessoa_empresa");
							//Acrescenta as colunas a um array
							pstmt.setString(1, "grl_pessoa_empresa");
							ResultSetMap rsmColunasEmpresa			 = new ResultSetMap(pstmt.executeQuery());
							ArrayList<String> colunasEmpresa		 = new ArrayList<String>();
							while(rsmColunasEmpresa.next()){
								if(!rsmColunasEmpresa.getString("column").equals("dt_versao") && !rsmColunasEmpresa.getString("column").equals("cd_pessoa") && !rsmColunasEmpresa.getString("column").equals("cd_vinculo"))
									colunasEmpresa.add(rsmColunasEmpresa.getString("column"));
							}
							
							//O sistema buscarï¿½ todas as colunas da tabela alm_documento_saida para montar o SQL referente a insercao junto com as interrogaï¿½ï¿½es
							String camposInsercao = "";
							String interrogacao = "";
							for(int j = 0; j < colunasEmpresa.size(); j++){
								camposInsercao += colunasEmpresa.get(j) + ",";
								interrogacao += "?,";
							}
							camposInsercao = camposInsercao.substring(0, camposInsercao.length()-1);
							interrogacao = interrogacao.substring(0, interrogacao.length()-1);
							
							//Acrescenta +1 no array que representa os registros que serï¿½o deletados
							HashMap<String, Object> valor = new HashMap<String, Object>();
							valor.put("cd_empresa", rsmPessoaEmpresaLocal.getObject("cd_empresa"));
							valor.put("cd_pessoa", rsmPessoaEmpresaLocal.getObject("cd_pessoa"));
							valor.put("cd_vinculo", rsmPessoaEmpresaLocal.getObject("cd_vinculo"));
							hashChavesPessoaEmpresa.add(valor);
							//Busca um array com as chaves estrangeiras
							chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("grl_pessoa_empresaRef"); 
							chavesEstrangeiras = new ArrayList<String>();
							for(String chaveE : chavesEstrangeirasHash.keySet()){
								chavesEstrangeiras.add(chaveE);
							}
							//Monta o SQL
							PreparedStatement pstmtInsert = connect.prepareStatement("INSERT INTO grl_pessoa_empresa (cd_pessoa, cd_vinculo, "+camposInsercao+") VALUES (?, ?, "+interrogacao+")");
							//Insere os dados do banco do servidor no novo registro do banco local
							pstmtInsert.setObject(1, rsmServidor.getObject("cd_pessoa"));
							pstmtInsert.setObject(2, rsmPessoaEmpresaLocal.getInt("cd_vinculo"));
							for(int j = 3; j <= colunas.size()+2; j++){
								//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
								//nas tabelas originais
								if(rsmPessoaEmpresaLocal.getObject(colunasEmpresa.get(j-3)) instanceof Integer && (Integer)rsmPessoaEmpresaLocal.getObject(colunasEmpresa.get(j-3)) == 0 && Util.inArrayString(colunasEmpresa.get(j-3), chavesEstrangeiras))
									pstmtInsert.setNull(j, Types.INTEGER);
								else 
									pstmtInsert.setObject(j, rsmPessoaEmpresaLocal.getObject(colunasEmpresa.get(j-3)));	
							}
							if(pstmtInsert.executeUpdate() <= 0){
								return new Result(-1, "Erro ao inserir na tabela grl_pessoa_empresa no banco servidor!");
							}
						}
						//*******************************************************************************************************************************************************
					}
					
				}
				else{
					System.out.println();
					System.out.println("14: Inserï¿½ï¿½o "+count+" da tabela grl_pessoa, Nome " + rsmLocal.getString("nm_pessoa"));
					count++;
					/**
					 * INSERCAO EM GRL_PESSOA
					 */
					
					//Acrescenta as colunas a um array
					pstmt.setString(1, "grl_pessoa");
					ResultSetMap rsmColunas 			 = new ResultSetMap(pstmt.executeQuery());
					ArrayList<String> colunas 			 = new ArrayList<String>();
					while(rsmColunas.next()){
						if(!rsmColunas.getString("column").equals("dt_versao") && !rsmColunas.getString("column").equals("cd_pessoa"))
							colunas.add(rsmColunas.getString("column"));
					}
					
					String camposInsercao = "";
					String interrogacao = "";
					for(int j = 0; j < colunas.size(); j++){
						camposInsercao += colunas.get(j) + ",";
						interrogacao += "?,";
					}
					camposInsercao = camposInsercao.substring(0, camposInsercao.length()-1);
					interrogacao = interrogacao.substring(0, interrogacao.length()-1);
					
					//Acrescenta +1 no array que representa os registros que serï¿½o deletados
					HashMap<String, Object> valor = new HashMap<String, Object>();
					valor.put("cd_pessoa", rsmLocal.getObject("cd_pessoa"));
					hashChaves.add(valor);
					//Busca um array com as chaves estrangeiras
					HashMap <String, Object> chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("grl_pessoaRef"); 
					ArrayList<String> chavesEstrangeiras = new ArrayList<String>();
					for(String chaveE : chavesEstrangeirasHash.keySet()){
						chavesEstrangeiras.add(chaveE);
					}
					//Monta o SQL
					PreparedStatement pstmtInsert = connect.prepareStatement("INSERT INTO grl_pessoa (cd_pessoa, "+camposInsercao+") VALUES (?, "+interrogacao+")");
					//Produz o novo codigo a partir do metodo
					int cdPessoa = getNextCodeServer("grl_pessoa", connect);
					//Acrescenta nos codigos de pessoa para comparaï¿½ï¿½o posterior nas outras tabelas
					cdPessoaNovo.add(rsmLocal.getInt("cd_pessoa"));
					pstmtInsert.setObject(1, cdPessoa);
					//Mapeia novo codigo para futura referencia
					mapaCdPessoa.put(rsmLocal.getInt("cd_pessoa"), cdPessoa);
					cdPessoaAntigo.add(rsmLocal.getInt("cd_pessoa"));
					for(int i = 2; i <= colunas.size()+1; i++){
						//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
						//nas tabelas originais
						if(rsmLocal.getObject(colunas.get(i-2)) instanceof Integer && (Integer)rsmLocal.getObject(colunas.get(i-2)) == 0 && Util.inArrayString(colunas.get(i-2), chavesEstrangeiras))
							pstmtInsert.setNull(i, Types.INTEGER);
						else 
							pstmtInsert.setObject(i, rsmLocal.getObject(colunas.get(i-2)));	
					}
					if(pstmtInsert.executeUpdate() <= 0){
						return new Result(-1, "Erro ao inserir na tabela grl_pessoa no banco servidor!");
					}
					
					//Caso seja uma pessoa fisica
					if((rsmLocal.getString("nr_cpf") != null && !rsmLocal.getString("nr_cpf").equals("")) || (rsmLocal.getInt("gn_pessoa") == 1)){
						//*******************************************************************************************************************************************************
						//Pesquisa do banco de dados local onde a data de versao ï¿½ nula, ou seja, ï¿½ um dado novo
						PreparedStatement pstmtPessoaFisica = conLocal.prepareStatement("SELECT * FROM grl_pessoa_fisica WHERE cd_pessoa = " + rsmLocal.getObject("cd_pessoa"));
						ResultSetMap rsmPessoaFisica = new ResultSetMap(pstmtPessoaFisica.executeQuery());
						//Tratamento para que os registros nao sejam sobrepostos
						ArrayList<Object> hashChavesPessoaFisica = new ArrayList<Object>();
						if(tabelasDelecao.get("grl_pessoa_fisica") == null)
							tabelasDelecao.put("grl_pessoa_fisica", hashChavesPessoaFisica);
						else
							hashChavesPessoaFisica = (ArrayList<Object>) tabelasDelecao.get("grl_pessoa_fisica");
						//Inserir a tabela com um array vazio, e ir acrescentando de acordo a valores que precisando ser inseridos
						while(rsmPessoaFisica.next()){
							/**
							 * INSERCAO EM GRL_PESSOA_FISICA
							 */
							System.out.println("15: Inserï¿½ï¿½o "+count+" da tabela grl_pessoa_fisica");
							//Acrescenta as colunas a um array
							pstmt.setString(1, "grl_pessoa_fisica");
							rsmColunas 			 = new ResultSetMap(pstmt.executeQuery());
							colunas 			 = new ArrayList<String>();
							while(rsmColunas.next()){
								if(!rsmColunas.getString("column").equals("dt_versao") && !rsmColunas.getString("column").equals("cd_pessoa"))
									colunas.add(rsmColunas.getString("column"));
							}
							
							//O sistema buscarï¿½ todas as colunas da tabela alm_documento_saida para montar o SQL referente a insercao junto com as interrogaï¿½ï¿½es
							camposInsercao = "";
							interrogacao = "";
							for(int j = 0; j < colunas.size(); j++){
								camposInsercao += colunas.get(j) + ",";
								interrogacao += "?,";
							}
							camposInsercao = camposInsercao.substring(0, camposInsercao.length()-1);
							interrogacao = interrogacao.substring(0, interrogacao.length()-1);
							
							//Acrescenta +1 no array que representa os registros que serï¿½o deletados
							valor = new HashMap<String, Object>();
							valor.put("cd_pessoa", rsmPessoaFisica.getObject("cd_pessoa"));
							hashChavesPessoaFisica.add(valor);
							//Busca um array com as chaves estrangeiras
							chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("grl_pessoa_fisicaRef"); 
							chavesEstrangeiras = new ArrayList<String>();
							for(String chaveE : chavesEstrangeirasHash.keySet()){
								chavesEstrangeiras.add(chaveE);
							}
							//Monta o SQL
							pstmtInsert = connect.prepareStatement("INSERT INTO grl_pessoa_fisica (cd_pessoa, "+camposInsercao+") VALUES (?, "+interrogacao+")");
							//Insere os dados do banco do servidor no novo registro do banco local
							pstmtInsert.setObject(1, cdPessoa);
							for(int i = 2; i <= colunas.size()+1; i++){
								//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
								//nas tabelas originais
								if(rsmPessoaFisica.getObject(colunas.get(i-2)) instanceof Integer && (Integer)rsmPessoaFisica.getObject(colunas.get(i-2)) == 0 && Util.inArrayString(colunas.get(i-2), chavesEstrangeiras))
									pstmtInsert.setNull(i, Types.INTEGER);
								else 
									pstmtInsert.setObject(i, rsmPessoaFisica.getObject(colunas.get(i-2)));	
							}
							if(pstmtInsert.executeUpdate() <= 0){
								return new Result(-1, "Erro ao inserir na tabela grl_pessoa_fisica no banco servidor!");
							}
						}
						//*******************************************************************************************************************************************************
					}
					
					else if((rsmLocal.getString("nr_cnpj") != null && !rsmLocal.getString("nr_cnpj").equals("")) || (rsmLocal.getInt("gn_pessoa") == 0)){
						PreparedStatement pstmtPessoaJuridica = conLocal.prepareStatement("SELECT * FROM grl_pessoa_juridica WHERE cd_pessoa = " + rsmLocal.getObject("cd_pessoa"));
						ResultSetMap rsmPessoaJuridica = new ResultSetMap(pstmtPessoaJuridica.executeQuery());
						//Tratamento para que os registros nao sejam sobrepostos
						ArrayList<Object> hashChavesPessoaJuridica = new ArrayList<Object>();
						if(tabelasDelecao.get("grl_pessoa_juridica") == null){
							tabelasDelecao.put("grl_pessoa_juridica", hashChavesPessoaJuridica);
						}
						else{
							hashChavesPessoaJuridica = (ArrayList<Object>) tabelasDelecao.get("grl_pessoa_juridica");
						}
						//Inserir a tabela com um array vazio, e ir acrescentando de acordo a valores que precisando ser inseridos
						rsmPessoaJuridica.beforeFirst();
						while(rsmPessoaJuridica.next()){
							/**
							 * INSERCAO EM GRL_PESSOA_JURIDICA
							 */
							System.out.println("16: Inserï¿½ï¿½o "+count+" da tabela grl_pessoa_juridica");
							//Acrescenta as colunas a um array
							pstmt.setString(1, "grl_pessoa_juridica");
							rsmColunas 			 = new ResultSetMap(pstmt.executeQuery());
							colunas 			 = new ArrayList<String>();
							while(rsmColunas.next()){
								if(!rsmColunas.getString("column").equals("dt_versao") && !rsmColunas.getString("column").equals("cd_pessoa"))
									colunas.add(rsmColunas.getString("column"));
							}
							
							//O sistema buscarï¿½ todas as colunas da tabela alm_documento_saida para montar o SQL referente a insercao junto com as interrogaï¿½ï¿½es
							camposInsercao = "";
							interrogacao = "";
							for(int j = 0; j < colunas.size(); j++){
								camposInsercao += colunas.get(j) + ",";
								interrogacao += "?,";
							}
							camposInsercao = camposInsercao.substring(0, camposInsercao.length()-1);
							interrogacao = interrogacao.substring(0, interrogacao.length()-1);
							
							//Acrescenta +1 no array que representa os registros que serï¿½o deletados
							valor = new HashMap<String, Object>();
							valor.put("cd_pessoa", rsmPessoaJuridica.getObject("cd_pessoa"));
							hashChavesPessoaJuridica.add(valor);
							//Busca um array com as chaves estrangeiras
							chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("grl_pessoa_juridicaRef"); 
							chavesEstrangeiras = new ArrayList<String>();
							for(String chaveE : chavesEstrangeirasHash.keySet()){
								chavesEstrangeiras.add(chaveE);
							}
							//Monta o SQL
							pstmtInsert = connect.prepareStatement("INSERT INTO grl_pessoa_juridica (cd_pessoa, "+camposInsercao+") VALUES (?, "+interrogacao+")");
							//Insere os dados do banco do servidor no novo registro do banco local
							pstmtInsert.setObject(1, cdPessoa);
							for(int i = 2; i <= colunas.size()+1; i++){
								//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
								//nas tabelas originais
								if(rsmPessoaJuridica.getObject(colunas.get(i-2)) instanceof Integer && (Integer)rsmPessoaJuridica.getObject(colunas.get(i-2)) == 0 && Util.inArrayString(colunas.get(i-2), chavesEstrangeiras))
									pstmtInsert.setNull(i, Types.INTEGER);
								else 
									pstmtInsert.setObject(i, rsmPessoaJuridica.getObject(colunas.get(i-2)));	
							}
							if(pstmtInsert.executeUpdate() <= 0){
								return new Result(-1, "Erro ao inserir na tabela grl_pessoa_juridica no banco servidor!");
							}
						}
						//*******************************************************************************************************************************************************
					}
					
					//*******************************************************************************************************************************************************
					pstmtPessoaEnderecoLocal.setInt(1, rsmLocal.getInt("cd_pessoa"));
					ResultSetMap rsmPessoaEndereco = new ResultSetMap(pstmtPessoaEnderecoLocal.executeQuery());
					//Tratamento para que os registros nao sejam sobrepostos
					ArrayList<Object> hashChavesPessoaEndereco = new ArrayList<Object>();
					if(tabelasDelecao.get("grl_pessoa_endereco") == null)
						tabelasDelecao.put("grl_pessoa_endereco", hashChavesPessoaEndereco);
					else
						hashChavesPessoaEndereco = (ArrayList<Object>) tabelasDelecao.get("grl_pessoa_endereco");
					//Inserir a tabela com um array vazio, e ir acrescentando de acordo a valores que precisando ser inseridos
					while(rsmPessoaEndereco.next()){
						/**
						 * INSERCAO EM GRL_PESSOA_ENDERECO
						 */
						System.out.println("17: Inserï¿½ï¿½o "+count+" da tabela grl_pessoa_endereco");
						//Acrescenta as colunas a um array
						pstmt.setString(1, "grl_pessoa_endereco");
						rsmColunas 			 = new ResultSetMap(pstmt.executeQuery());
						colunas 			 = new ArrayList<String>();
						while(rsmColunas.next()){
							if(!rsmColunas.getString("column").equals("dt_versao") && !rsmColunas.getString("column").equals("cd_pessoa"))
								colunas.add(rsmColunas.getString("column"));
						}
						
						//O sistema buscarï¿½ todas as colunas da tabela alm_documento_saida para montar o SQL referente a insercao junto com as interrogaï¿½ï¿½es
						camposInsercao = "";
						interrogacao = "";
						for(int j = 0; j < colunas.size(); j++){
							camposInsercao += colunas.get(j) + ",";
							interrogacao += "?,";
						}
						camposInsercao = camposInsercao.substring(0, camposInsercao.length()-1);
						interrogacao = interrogacao.substring(0, interrogacao.length()-1);
						
						//Acrescenta +1 no array que representa os registros que serï¿½o deletados
						valor = new HashMap<String, Object>();
						valor.put("cd_endereco", rsmPessoaEndereco.getObject("cd_endereco"));
						valor.put("cd_pessoa", rsmPessoaEndereco.getObject("cd_pessoa"));
						hashChavesPessoaEndereco.add(valor);
						//Busca um array com as chaves estrangeiras
						chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("grl_pessoa_enderecoRef"); 
						chavesEstrangeiras = new ArrayList<String>();
						for(String chaveE : chavesEstrangeirasHash.keySet()){
							chavesEstrangeiras.add(chaveE);
						}
						//Monta o SQL
						pstmtInsert = connect.prepareStatement("INSERT INTO grl_pessoa_endereco (cd_pessoa, "+camposInsercao+") VALUES (?, "+interrogacao+")");
						//Insere os dados do banco do servidor no novo registro do banco local
						pstmtInsert.setObject(1, cdPessoa);
						for(int i = 2; i <= colunas.size()+1; i++){
							//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
							//nas tabelas originais
							if(rsmPessoaEndereco.getObject(colunas.get(i-2)) instanceof Integer && (Integer)rsmPessoaEndereco.getObject(colunas.get(i-2)) == 0 && Util.inArrayString(colunas.get(i-2), chavesEstrangeiras))
								pstmtInsert.setNull(i, Types.INTEGER);
							else 
								pstmtInsert.setObject(i, rsmPessoaEndereco.getObject(colunas.get(i-2)));	
						}
						if(pstmtInsert.executeUpdate() <= 0){
							return new Result(-1, "Erro ao inserir na tabela grl_pessoa_endereco no banco servidor!");
						}
					}
					//*******************************************************************************************************************************************************
					pstmtPessoaEmpresaLocal.setInt(1, rsmLocal.getInt("cd_pessoa"));
					ResultSetMap rsmPessoaEmpresa = new ResultSetMap(pstmtPessoaEmpresaLocal.executeQuery());
					//Tratamento para que os registros nao sejam sobrepostos
					ArrayList<Object> hashChavesPessoaEmpresa = new ArrayList<Object>();
					if(tabelasDelecao.get("grl_pessoa_empresa") == null)
						tabelasDelecao.put("grl_pessoa_empresa", hashChavesPessoaEmpresa);
					else
						hashChavesPessoaEmpresa = (ArrayList<Object>) tabelasDelecao.get("grl_pessoa_empresa");
					while(rsmPessoaEmpresa.next()){
						/**
						 * INSERCAO EM GRL_PESSOA_EMPRESA
						 */
						System.out.println("18: Inserï¿½ï¿½o "+count+" da tabela grl_pessoa_empresa");
						//Acrescenta as colunas a um array
						pstmt.setString(1, "grl_pessoa_empresa");
						rsmColunas 			 = new ResultSetMap(pstmt.executeQuery());
						colunas 			 = new ArrayList<String>();
						while(rsmColunas.next()){
							if(!rsmColunas.getString("column").equals("dt_versao") && !rsmColunas.getString("column").equals("cd_pessoa"))
								colunas.add(rsmColunas.getString("column"));
						}
						
						//O sistema buscarï¿½ todas as colunas da tabela alm_documento_saida para montar o SQL referente a insercao junto com as interrogaï¿½ï¿½es
						camposInsercao = "";
						interrogacao = "";
						for(int j = 0; j < colunas.size(); j++){
							camposInsercao += colunas.get(j) + ",";
							interrogacao += "?,";
						}
						camposInsercao = camposInsercao.substring(0, camposInsercao.length()-1);
						interrogacao = interrogacao.substring(0, interrogacao.length()-1);
						
						//Acrescenta +1 no array que representa os registros que serï¿½o deletados
						valor = new HashMap<String, Object>();
						valor.put("cd_empresa", rsmPessoaEmpresa.getObject("cd_empresa"));
						valor.put("cd_pessoa", rsmPessoaEmpresa.getObject("cd_pessoa"));
						valor.put("cd_vinculo", rsmPessoaEmpresa.getObject("cd_vinculo"));
						hashChavesPessoaEmpresa.add(valor);
						//Busca um array com as chaves estrangeiras
						chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("grl_pessoa_empresaRef"); 
						chavesEstrangeiras = new ArrayList<String>();
						for(String chaveE : chavesEstrangeirasHash.keySet()){
							chavesEstrangeiras.add(chaveE);
						}
						//Monta o SQL
						pstmtInsert = connect.prepareStatement("INSERT INTO grl_pessoa_empresa (cd_pessoa, "+camposInsercao+") VALUES (?, "+interrogacao+")");
						//Insere os dados do banco do servidor no novo registro do banco local
						pstmtInsert.setObject(1, cdPessoa);
						for(int i = 2; i <= colunas.size()+1; i++){
							//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
							//nas tabelas originais
							if(rsmPessoaEmpresa.getObject(colunas.get(i-2)) instanceof Integer && (Integer)rsmPessoaEmpresa.getObject(colunas.get(i-2)) == 0 && Util.inArrayString(colunas.get(i-2), chavesEstrangeiras))
								pstmtInsert.setNull(i, Types.INTEGER);
							else 
								pstmtInsert.setObject(i, rsmPessoaEmpresa.getObject(colunas.get(i-2)));	
						}
						if(pstmtInsert.executeUpdate() <= 0){
							return new Result(-1, "Erro ao inserir na tabela grl_pessoa_empresa no banco servidor!");
						}
					}
					//*******************************************************************************************************************************************************
				}
				
			}
			
			//*******************************************************************************************************************************************************
			//Tratamento para o caso de grl_pessoa_fisica, grl_pessoa_juridica ou grl_pessoa_endereco serem alterados sem alterar grl_pessoa
			
			//Pesquisa do banco de dados local onde a data de versao ï¿½ nï¿½o nula - Atualizacao de cliente antigo 
			pstmtLocal = conLocal.prepareStatement("SELECT * FROM grl_pessoa_fisica WHERE dt_versao IS NOT NULL");
			PreparedStatement pstmtFisicaServ = connect.prepareStatement("SELECT * FROM grl_pessoa_fisica WHERE cd_pessoa = ? AND dt_versao < ?");
			rsmLocal = new ResultSetMap(pstmtLocal.executeQuery());
			while(rsmLocal.next()){
				pstmtFisicaServ.setInt(1, rsmLocal.getInt("cd_pessoa"));
				pstmtFisicaServ.setTimestamp(2, rsmLocal.getTimestamp("dt_versao"));
				ResultSetMap rsmFisicaServ = new ResultSetMap(pstmtFisicaServ.executeQuery());
				//Encontrou uma pessoa fisica que tem uma data de atualizacao mais recente do que o registro do servidor
				if(rsmFisicaServ.next()){
					System.out.println();
					System.out.println("5: Atualizaï¿½ï¿½o "+count+" da tabela grl_pessoa_fisica, Codigo da Pessoa " + rsmFisicaServ.getString("cd_pessoa"));
					count++;
					
					//Montando colunas
					pstmt.setString(1, "grl_pessoa_fisica");
					ResultSetMap rsmColunas = new ResultSetMap(pstmt.executeQuery());
					ArrayList<String> colunas = new ArrayList<String>();
					while(rsmColunas.next())
						if(!rsmColunas.getString("column").equals("cd_pessoa"))
							colunas.add(rsmColunas.getString("column"));
					
					//O sistema buscarï¿½ todas as colunas da tabela para montar o SQL referente a atualizaï¿½ï¿½o junto com as chaves
					String camposAtualizacao = "";
					for(int j = 0; j < colunas.size(); j++){
						camposAtualizacao += colunas.get(j) + "=?,";
					}
					camposAtualizacao = camposAtualizacao.substring(0, camposAtualizacao.length()-1);
					
					//Montando chave primaria
					ArrayList<String> chaves = new ArrayList<String>();
					chaves.add("cd_pessoa");
					String chave = "";
					for(int i = 0; i < chaves.size(); i++)
						chave += chaves.get(i)+"=? AND ";
					chave = chave.substring(0, chave.length() - 5);
					//Busca um array com as chaves estrangeiras
					HashMap <String, Object> chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("grl_pessoa_fisicaRef"); 
					ArrayList<String> chavesEstrangeiras = new ArrayList<String>();
					for(String chaveE : chavesEstrangeirasHash.keySet()){
						chavesEstrangeiras.add(chaveE);
					}
					//Monta o SQL
					PreparedStatement pstmtUpdate = connect.prepareStatement("UPDATE grl_pessoa_fisica set "+camposAtualizacao+" WHERE "+chave);
					//Insere os novos valores no SQL, primeiro os valores de atualizaï¿½ï¿½o, depois os valores da chave
					int i = 1;
					for(; i <= colunas.size(); i++){
						//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
						//nas tabelas originais
						if(rsmLocal.getObject(colunas.get(i-1)) instanceof Integer && (Integer)rsmLocal.getObject(colunas.get(i-1)) == 0 && Util.inArrayString(colunas.get(i-1), chavesEstrangeiras)){
							pstmtUpdate.setNull(i, Types.INTEGER);
						}
						else{
							//Caso no servidor tenha algum dado e no cliente o campo estiver nulo, prevalece o dado do servidor
							if(rsmLocal.getObject(colunas.get(i-1)) == null && rsmFisicaServ.getObject(colunas.get(i-1)) != null)
								pstmtUpdate.setObject(i, rsmFisicaServ.getObject(colunas.get(i-1)));
							else
								pstmtUpdate.setObject(i, rsmLocal.getObject(colunas.get(i-1)));
						}
					}
					for(int j = 0, k = i ; j < chaves.size(); j++, k++){
						pstmtUpdate.setObject(k, rsmFisicaServ.getObject(chaves.get(j)));
					}
					if(pstmtUpdate.executeUpdate() <= 0){
						return new Result(-1, "Erro ao atualizar na tabela grl_pessoa_fisica no banco servidor!");
					}
				}
				
			}
			
			//Pesquisa do banco de dados local onde a data de versao ï¿½ nï¿½o nula  - Atualizacao de cliente antigo
			pstmtLocal = conLocal.prepareStatement("SELECT * FROM grl_pessoa_juridica WHERE dt_versao IS NOT NULL");
			PreparedStatement pstmtJuridicaServ = connect.prepareStatement("SELECT * FROM grl_pessoa_fisica WHERE cd_pessoa = ? AND dt_versao < ?");
			rsmLocal = new ResultSetMap(pstmtLocal.executeQuery());
			while(rsmLocal.next()){
				pstmtJuridicaServ.setInt(1, rsmLocal.getInt("cd_pessoa"));
				pstmtJuridicaServ.setTimestamp(2, rsmLocal.getTimestamp("dt_versao"));
				ResultSetMap rsmJuridicaServ = new ResultSetMap(pstmtJuridicaServ.executeQuery());
				//Encontrou uma pessoa juridica que tem uma data de atualizacao mais recente do que o registro do servidor
				if(rsmJuridicaServ.next()){
					System.out.println();
					System.out.println("6: Atualizaï¿½ï¿½o "+count+" da tabela grl_pessoa_juridica, Codigo da Pessoa " + rsmJuridicaServ.getString("cd_pessoa"));
					count++;
					
					//Montando colunas
					pstmt.setString(1, "grl_pessoa_juridica");
					ResultSetMap rsmColunas = new ResultSetMap(pstmt.executeQuery());
					ArrayList<String> colunas = new ArrayList<String>();
					while(rsmColunas.next())
						if(!rsmColunas.getString("column").equals("cd_pessoa"))
							colunas.add(rsmColunas.getString("column"));
					
					//O sistema buscarï¿½ todas as colunas da tabela para montar o SQL referente a atualizaï¿½ï¿½o junto com as chaves
					String camposAtualizacao = "";
					for(int j = 0; j < colunas.size(); j++){
						camposAtualizacao += colunas.get(j) + "=?,";
					}
					camposAtualizacao = camposAtualizacao.substring(0, camposAtualizacao.length()-1);
					
					//Montando chave primaria
					ArrayList<String> chaves = new ArrayList<String>();
					chaves.add("cd_pessoa");
					String chave = "";
					for(int i = 0; i < chaves.size(); i++)
						chave += chaves.get(i)+"=? AND ";
					chave = chave.substring(0, chave.length() - 5);
					//Busca um array com as chaves estrangeiras
					HashMap <String, Object> chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("grl_pessoa_juridicaRef"); 
					ArrayList<String> chavesEstrangeiras = new ArrayList<String>();
					for(String chaveE : chavesEstrangeirasHash.keySet()){
						chavesEstrangeiras.add(chaveE);
					}
					//Monta o SQL
					PreparedStatement pstmtUpdate = connect.prepareStatement("UPDATE grl_pessoa_juridica set "+camposAtualizacao+" WHERE "+chave);
					//Insere os novos valores no SQL, primeiro os valores de atualizaï¿½ï¿½o, depois os valores da chave
					int i = 1;
					for(; i <= colunas.size(); i++){
						//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
						//nas tabelas originais
						if(rsmLocal.getObject(colunas.get(i-1)) instanceof Integer && (Integer)rsmLocal.getObject(colunas.get(i-1)) == 0 && Util.inArrayString(colunas.get(i-1), chavesEstrangeiras)){
							pstmtUpdate.setNull(i, Types.INTEGER);
						}
						else{
							//Caso no servidor tenha algum dado e no cliente o campo estiver nulo, prevalece o dado do servidor
							if(rsmLocal.getObject(colunas.get(i-1)) == null && rsmJuridicaServ.getObject(colunas.get(i-1)) != null)
								pstmtUpdate.setObject(i, rsmJuridicaServ.getObject(colunas.get(i-1)));
							else
								pstmtUpdate.setObject(i, rsmLocal.getObject(colunas.get(i-1)));
						}
					}
					for(int j = 0, k = i ; j < chaves.size(); j++, k++){
						pstmtUpdate.setObject(k, rsmJuridicaServ.getObject(chaves.get(j)));
					}
					if(pstmtUpdate.executeUpdate() <= 0){
						return new Result(-1, "Erro ao atualizar na tabela grl_pessoa_juridica no banco servidor!");
					}
				}
			}
			
			//Pesquisa do banco de dados local onde a data de versao ï¿½ nï¿½o nula  - Atualizacao de cliente antigo
			pstmtLocal = conLocal.prepareStatement("SELECT * FROM grl_pessoa_endereco WHERE dt_versao IS NOT NULL");
			PreparedStatement pstmtEnderecoServ = connect.prepareStatement("SELECT * FROM grl_pessoa_endereco WHERE cd_pessoa = ? AND cd_endereco = ? AND dt_versao < ?");
			rsmLocal = new ResultSetMap(pstmtLocal.executeQuery());
			while(rsmLocal.next()){
				pstmtEnderecoServ.setInt(1, rsmLocal.getInt("cd_pessoa"));
				pstmtEnderecoServ.setInt(2, rsmLocal.getInt("cd_endereco"));
				pstmtEnderecoServ.setTimestamp(3, rsmLocal.getTimestamp("dt_versao"));
				ResultSetMap rsmEnderecoServ = new ResultSetMap(pstmtEnderecoServ.executeQuery());
				//Encontrou uma pessoa endereco que tem uma data de atualizacao mais recente do que o registro do servidor
				if(rsmEnderecoServ.next()){
					System.out.println();
					System.out.println("7: Atualizaï¿½ï¿½o "+count+" da tabela grl_pessoa_endereco, Codigo da Pessoa " + rsmEnderecoServ.getString("cd_pessoa"));
					count++;
					
					//Montando colunas
					pstmt.setString(1, "grl_pessoa_endereco");
					ResultSetMap rsmColunas = new ResultSetMap(pstmt.executeQuery());
					ArrayList<String> colunas = new ArrayList<String>();
					while(rsmColunas.next())
						if(!rsmColunas.getString("column").equals("cd_pessoa") && !rsmColunas.getString("column").equals("cd_endereco"))
							colunas.add(rsmColunas.getString("column"));
					
					//O sistema buscarï¿½ todas as colunas da tabela para montar o SQL referente a atualizaï¿½ï¿½o junto com as chaves
					String camposAtualizacao = "";
					for(int j = 0; j < colunas.size(); j++){
						camposAtualizacao += colunas.get(j) + "=?,";
					}
					camposAtualizacao = camposAtualizacao.substring(0, camposAtualizacao.length()-1);
					
					//Montando chave primaria
					ArrayList<String> chaves = new ArrayList<String>();
					chaves.add("cd_pessoa");
					chaves.add("cd_endereco");
					String chave = "";
					for(int i = 0; i < chaves.size(); i++)
						chave += chaves.get(i)+"=? AND ";
					chave = chave.substring(0, chave.length() - 5);
					//Busca um array com as chaves estrangeiras
					HashMap <String, Object> chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("grl_pessoa_enderecoRef"); 
					ArrayList<String> chavesEstrangeiras = new ArrayList<String>();
					for(String chaveE : chavesEstrangeirasHash.keySet()){
						chavesEstrangeiras.add(chaveE);
					}
					//Monta o SQL
					PreparedStatement pstmtUpdate = connect.prepareStatement("UPDATE grl_pessoa_endereco set "+camposAtualizacao+" WHERE "+chave);
					//Insere os novos valores no SQL, primeiro os valores de atualizaï¿½ï¿½o, depois os valores da chave
					int i = 1;
					for(; i <= colunas.size(); i++){
						//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
						//nas tabelas originais
						if(rsmLocal.getObject(colunas.get(i-1)) instanceof Integer && (Integer)rsmLocal.getObject(colunas.get(i-1)) == 0 && Util.inArrayString(colunas.get(i-1), chavesEstrangeiras)){
							pstmtUpdate.setNull(i, Types.INTEGER);
						}
						else{
							//Caso no servidor tenha algum dado e no cliente o campo estiver nulo, prevalece o dado do servidor
							if(rsmLocal.getObject(colunas.get(i-1)) == null && rsmEnderecoServ.getObject(colunas.get(i-1)) != null)
								pstmtUpdate.setObject(i, rsmEnderecoServ.getObject(colunas.get(i-1)));
							else
								pstmtUpdate.setObject(i, rsmLocal.getObject(colunas.get(i-1)));
						}
					}
					for(int j = 0, k = i ; j < chaves.size(); j++, k++){
						pstmtUpdate.setObject(k, rsmEnderecoServ.getObject(chaves.get(j)));
					}
					if(pstmtUpdate.executeUpdate() <= 0){
						return new Result(-1, "Erro ao atualizar na tabela grl_pessoa_endereco no banco servidor!");
					}
				}
			}
			
			//:TODO incluir alteraï¿½ï¿½o de grl_pessoa_empresa tanto aqui quanto nos demais casos no metodo (a alteraï¿½ï¿½o serï¿½ apenas de st_vinculo, mas como deve ser raramente usado, entao deixei para depois)
			
			//*******************************************************************************************************************************************************
			//Insercao de grl_pessoa_endereco de pessoas que ja existem
			
			rsmLocal = new ResultSetMap(pstmtLocalEndereco.executeQuery());
			//Tratamento para que os registros nao sejam sobrepostos
			ArrayList<Object> hashChavesPessoaEndereco = new ArrayList<Object>();
			if(tabelasDelecao.get("grl_pessoa_endereco") == null)
				tabelasDelecao.put("grl_pessoa_endereco", hashChavesPessoaEndereco);
			else
				hashChavesPessoaEndereco = (ArrayList<Object>) tabelasDelecao.get("grl_pessoa_endereco");
			//Inserir a tabela com um array vazio, e ir acrescentando de acordo a valores que precisando ser inseridos
			while(rsmLocal.next()){
				//Sï¿½ vai acrescentar o endereco se ï¿½ uma pessoa antiga que acrescentou um endereco
				if(!Util.inArrayInteger(rsmLocal.getInt("cd_pessoa"), cdPessoaNovo)){
					System.out.println("19: Inserï¿½ï¿½o "+count+" da tabela grl_pessoa_endereco, Codigo da Pessoa " + rsmLocal.getString("cd_pessoa"));
					count++;
					//Busca um cd endereco novo para nao dar conflito com possiveis atualizacoes de outros clientes
					pstmtPessoaEnderecoServidor.setInt(1, rsmLocal.getInt("cd_pessoa"));
					ResultSetMap rsmEnderecoPessoa = new ResultSetMap(pstmtPessoaEnderecoServidor.executeQuery());
					int cdEndereco = rsmEnderecoPessoa.getLines().size() + 1;
					
					//Acrescenta as colunas a um array
					pstmt.setString(1, "grl_pessoa_endereco");
					ResultSetMap rsmColunas	 = new ResultSetMap(pstmt.executeQuery());
					ArrayList<String> colunas 			 = new ArrayList<String>();
					while(rsmColunas.next()){
						if(!rsmColunas.getString("column").equals("dt_versao") && !rsmColunas.getString("column").equals("cd_pessoa") && !rsmColunas.getString("column").equals("cd_endereco"))
							colunas.add(rsmColunas.getString("column"));
					}
					
					//O sistema buscarï¿½ todas as colunas da tabela alm_documento_saida para montar o SQL referente a insercao junto com as interrogaï¿½ï¿½es
					String camposInsercao = "";
					String interrogacao = "";
					for(int j = 0; j < colunas.size(); j++){
						camposInsercao += colunas.get(j) + ",";
						interrogacao += "?,";
					}
					camposInsercao = camposInsercao.substring(0, camposInsercao.length()-1);
					interrogacao = interrogacao.substring(0, interrogacao.length()-1);
					
					//Acrescenta +1 no array que representa os registros que serï¿½o deletados
					HashMap<String, Object> valor = new HashMap<String, Object>();
					valor.put("cd_endereco", rsmLocal.getObject("cd_endereco"));
					valor.put("cd_pessoa", rsmLocal.getObject("cd_pessoa"));
					hashChavesPessoaEndereco.add(valor);
					//Busca um array com as chaves estrangeiras
					HashMap <String, Object> chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("grl_pessoa_enderecoRef"); 
					ArrayList<String> chavesEstrangeiras = new ArrayList<String>();
					for(String chaveE : chavesEstrangeirasHash.keySet()){
						chavesEstrangeiras.add(chaveE);
					}
					//Monta o SQL
					PreparedStatement pstmtInsert = connect.prepareStatement("INSERT INTO grl_pessoa_endereco (cd_pessoa, cd_endereco, "+camposInsercao+") VALUES (?, ?, "+interrogacao+")");
					//Insere os dados do banco do servidor no novo registro do banco local
					pstmtInsert.setObject(1, rsmLocal.getObject("cd_pessoa"));
					pstmtInsert.setObject(2, cdEndereco);
					for(int i = 3; i <= colunas.size()+2; i++){
						//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
						//nas tabelas originais
						if(rsmLocal.getObject(colunas.get(i-3)) instanceof Integer && (Integer)rsmLocal.getObject(colunas.get(i-3)) == 0 && Util.inArrayString(colunas.get(i-3), chavesEstrangeiras))
							pstmtInsert.setNull(i, Types.INTEGER);
						else 
							pstmtInsert.setObject(i, rsmLocal.getObject(colunas.get(i-3)));	
					}
					if(pstmtInsert.executeUpdate() <= 0){
						return new Result(-1, "Erro ao inserir na tabela grl_pessoa_endereco no banco servidor!");
					}
					
				}
			}
			//*******************************************************************************************************************************************************
			//Insercao de grl_pessoa_empresa de pessoas que ja existem
			
			rsmLocal = new ResultSetMap(pstmtLocalPessoaEmpresa.executeQuery());
			//Tratamento para que os registros nao sejam sobrepostos
			ArrayList<Object> hashChavesPessoaEmpresa = new ArrayList<Object>();
			if(tabelasDelecao.get("grl_pessoa_empresa") == null)
				tabelasDelecao.put("grl_pessoa_empresa", hashChavesPessoaEmpresa);
			else
				hashChavesPessoaEmpresa = (ArrayList<Object>) tabelasDelecao.get("grl_pessoa_empresa");
			
			while(rsmLocal.next()){
				pstmtPessoaEmpresaLocal.setInt(1, rsmLocal.getInt("cd_pessoa"));
				ResultSetMap rsmPessoaEmpresaLocal = new ResultSetMap(pstmtPessoaEmpresaLocal.executeQuery());
				
				while(rsmPessoaEmpresaLocal.next()){
					pstmtPessoaEmpresaServidor.setInt(1, rsmPessoaEmpresaLocal.getInt("cd_pessoa"));
					pstmtPessoaEmpresaServidor.setInt(2, rsmPessoaEmpresaLocal.getInt("cd_vinculo"));
					//Sï¿½ vai acrescentar o vinculo se ï¿½ uma pessoa antiga que acrescentou um vinculo e o servidor nao tem ainda esse vinculo
					if(!Util.inArrayInteger(rsmPessoaEmpresaLocal.getInt("cd_pessoa"), cdPessoaNovo) && !new ResultSetMap(pstmtPessoaEmpresaServidor.executeQuery()).next()){
						System.out.println("20: Inserï¿½ï¿½o "+count+" da tabela grl_pessoa_empresa, Codigo da Pessoa " + rsmPessoaEmpresaLocal.getString("cd_pessoa"));
						count++;
						//Acrescenta as colunas a um array
						pstmt.setString(1, "grl_pessoa_empresa");
						ResultSetMap rsmColunas	 = new ResultSetMap(pstmt.executeQuery());
						ArrayList<String> colunas 			 = new ArrayList<String>();
						while(rsmColunas.next()){
							if(!rsmColunas.getString("column").equals("dt_versao"))
								colunas.add(rsmColunas.getString("column"));
						}
						//O sistema buscarï¿½ todas as colunas da tabela alm_documento_saida para montar o SQL referente a insercao junto com as interrogaï¿½ï¿½es
						String camposInsercao = "";
						String interrogacao = "";
						for(int j = 0; j < colunas.size(); j++){
							camposInsercao += colunas.get(j) + ",";
							interrogacao += "?,";
						}
						camposInsercao = camposInsercao.substring(0, camposInsercao.length()-1);
						interrogacao = interrogacao.substring(0, interrogacao.length()-1);
						//Acrescenta +1 no array que representa os registros que serï¿½o deletados
						HashMap<String, Object> valor = new HashMap<String, Object>();
						valor.put("cd_empresa", rsmPessoaEmpresaLocal.getObject("cd_empresa"));
						valor.put("cd_pessoa", rsmPessoaEmpresaLocal.getObject("cd_pessoa"));
						valor.put("cd_vinculo", rsmPessoaEmpresaLocal.getObject("cd_vinculo"));
						hashChavesPessoaEmpresa.add(valor);
						//Busca um array com as chaves estrangeiras
						HashMap <String, Object> chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get("grl_pessoa_empresaRef"); 
						ArrayList<String> chavesEstrangeiras = new ArrayList<String>();
						for(String chaveE : chavesEstrangeirasHash.keySet()){
							chavesEstrangeiras.add(chaveE);
						}
						//Monta o SQL
						PreparedStatement pstmtInsert = connect.prepareStatement("INSERT INTO grl_pessoa_empresa ("+camposInsercao+") VALUES ("+interrogacao+")");
						//Insere os dados do banco do servidor no novo registro do banco local
						for(int i = 1; i <= colunas.size(); i++){
							//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
							//nas tabelas originais
							if(rsmPessoaEmpresaLocal.getObject(colunas.get(i-1)) instanceof Integer && (Integer)rsmPessoaEmpresaLocal.getObject(colunas.get(i-1)) == 0 && Util.inArrayString(colunas.get(i-1), chavesEstrangeiras))
								pstmtInsert.setNull(i, Types.INTEGER);
							else 
								pstmtInsert.setObject(i, rsmPessoaEmpresaLocal.getObject(colunas.get(i-1)));	
						}
						if(pstmtInsert.executeUpdate() <= 0){
							return new Result(-1, "Erro ao inserir na tabela grl_pessoa_empresa no banco servidor!");
						}
						
					}
				}
			}
			System.out.println("Concluida atualizaï¿½ï¿½o de Pessoas - Cliente -> Servidor !");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar sincronizar bancos: " + e, e);
		}
	}
	
	public static sol.util.Result atualizarLocal(int cdEmpresa, String tabela, ArrayList<String> chaves, Connection connect, Connection conServer){
		try {
			System.out.println("Sincronizando tabela " + tabela + "...");
			//
			String dtSincronizacao = ParametroServices.getValorOfParametro("DT_SINCRONIZACAO", connect);
			String dtSincronizacaoSql = Util.convCalendarStringSqlCompleto(Util.convStringToCalendar(dtSincronizacao));
			
			//Montando chave primaria
			String chave = "";
			for(int i = 0; i < chaves.size(); i++)
				chave += chaves.get(i)+"=? AND ";
			chave = chave.substring(0, chave.length() - 5);
			
			//Sincronizacao da tabela entre os bancos
			System.out.println("Data da ultima sincronizacao no Banco Local = " + dtSincronizacaoSql);
			//Pesquisa do banco de dados do servidor, da tabela em questï¿½o, todas as linhas que foram atualizadas depois da data parametrizada no cliente
			//:TODO permanecer com a dt_versao nula?
			PreparedStatement pstmtServidor = conServer.prepareStatement("SELECT * FROM "+tabela+" WHERE dt_versao > '"+dtSincronizacaoSql+"' OR dt_versao IS NULL");
			ResultSetMap rsm = new ResultSetMap(pstmtServidor.executeQuery());
			int count = 0;
			//Busca as tabelas a serem sincronizadas no sistema
			Result resultado = getTabelasSincronizacao();
			HashMap<String, Object> tabelasSincronizadas = (HashMap<String, Object>) resultado.getObjects().get("tabelasSincronizadas"); 
			while(rsm.next()){
				
				//Caso a data da versao do servidor esteja vazia, ela serï¿½ automaticamente atualizada para a data atual
				if(rsm.getString("dt_versao") == null || rsm.getString("dt_versao").equals("")){
					PreparedStatement pstmtUpdate = conServer.prepareStatement("UPDATE "+tabela+" set dt_versao = '"+Util.convCalendarStringSqlCompleto(Util.getDataAtual())+"' WHERE "+chave);
					for(int j = 0, k = 1 ; j < chaves.size(); j++, k++){
						pstmtUpdate.setObject(k, rsm.getObject(chaves.get(j)));
					}
					if(pstmtUpdate.executeUpdate() <= 0){
						return new Result(-1, "Erro ao atualizar na tabela "+tabela+" no banco servidor!");
					}
				}
				
				//Faz a busca com a mesma chave encontrada do servidor para o banco local
				PreparedStatement pstmtLocal = connect.prepareStatement("SELECT * FROM "+tabela+" WHERE "+chave);
				for(int i = 0, j = 1; i < chaves.size(); i++, j++){
					pstmtLocal.setObject(j, rsm.getObject(chaves.get(i)));
				}
				ResultSetMap rsmLocal = new ResultSetMap(pstmtLocal.executeQuery());
				
				//SQL para identificar o nome das colunas de uma determinada tabela
				PreparedStatement pstmt = connect.prepareStatement("SELECT a.attname AS \"column\" " +
																	"	FROM pg_catalog.pg_attribute a " +
																	"	INNER JOIN pg_stat_user_tables c on a.attrelid = c.relid " +
																	"	WHERE " +
																	"	a.attnum > 0 AND " +
																	"	NOT a.attisdropped " +
																	"	AND relname = '"+tabela+"' " +
																	" ORDER BY a.attname");
				ResultSetMap rsmColunas = new ResultSetMap(pstmt.executeQuery());
				ArrayList<String> colunas = new ArrayList<String>();
				while(rsmColunas.next())
					colunas.add(rsmColunas.getString("column"));
				
				
				//Se encontrar um registro com a mesma chave , esse registro serï¿½ atualizado com os dados do registro do servidor
				if(rsmLocal.next()){
					System.out.println("8: Atualizaï¿½ï¿½o "+count+" da tabela " + tabela);
					System.out.println();
					count++;
					//O sistema buscarï¿½ todas as colunas da tabela para montar o SQL referente a atualizaï¿½ï¿½o junto com as chaves
					String camposAtualizacao = "";
					for(int j = 0; j < colunas.size(); j++){
						camposAtualizacao += colunas.get(j) + "=?,";
					}
					camposAtualizacao = camposAtualizacao.substring(0, camposAtualizacao.length()-1);
					//Busca um array com as chaves estrangeiras
					HashMap <String, Object> chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get(tabela + "Ref"); 
					ArrayList<String> chavesEstrangeiras = new ArrayList<String>();
					//Teste realizado pois algumas tabelas nao apresentam chaves estrangeiras
					if(chavesEstrangeirasHash != null){
						for(String chaveE : chavesEstrangeirasHash.keySet()){
							chavesEstrangeiras.add(chaveE);
						}
					}
					//Monta o SQL
					PreparedStatement pstmtUpdate = connect.prepareStatement("UPDATE "+tabela+" set "+camposAtualizacao+" WHERE "+chave);
					//Insere os novos valores no SQL, primeiro os valores de atualizaï¿½ï¿½o, depois os valores da chave
					int i = 1;
					for(; i <= colunas.size(); i++){
						//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
						//nas tabelas originais
						if(rsm.getObject(colunas.get(i-1)) instanceof Integer && (Integer)rsm.getObject(colunas.get(i-1)) == 0 && Util.inArrayString(colunas.get(i-1), chavesEstrangeiras)){
							pstmtUpdate.setNull(i, Types.INTEGER);
						}
						else 
							pstmtUpdate.setObject(i, rsm.getObject(colunas.get(i-1)));
					}
					for(int j = 0, k = i ; j < chaves.size(); j++, k++){
						pstmtUpdate.setObject(k, rsmLocal.getObject(chaves.get(j)));
					}
					if(pstmtUpdate.executeUpdate() <= 0){
						return new Result(-1, "Erro ao atualizar na tabela "+tabela+" do banco local!");
					}
				}
				//Se nï¿½o encontrar um registro com a mesma chave, esse registro serï¿½ inserido no banco local
				else{
					System.out.println("21: Inserï¿½ï¿½o "+count+" da tabela " + tabela);
					System.out.println();
					count++;
					//O sistema buscarï¿½ todas as colunas da tabela para montar o SQL referente a insercao junto com as interrogaï¿½ï¿½es
					String camposInsercao = "";
					String interrogacao = "";
					for(int j = 0; j < colunas.size(); j++){
						//A data da versï¿½o nï¿½o sera acrescentada
						camposInsercao += colunas.get(j) + ",";
						interrogacao += "?,";
					}
					camposInsercao = camposInsercao.substring(0, camposInsercao.length()-1);
					interrogacao = interrogacao.substring(0, interrogacao.length()-1);
					//Busca um array com as chaves estrangeiras
					HashMap <String, Object> chavesEstrangeirasHash = (HashMap <String, Object>)tabelasSincronizadas.get(tabela + "Ref"); 
					ArrayList<String> chavesEstrangeiras = new ArrayList<String>();
					//Tratamento para caso a tabela nï¿½o tenha chaves estrangeiras
					if(chavesEstrangeirasHash != null){
						for(String chaveE : chavesEstrangeirasHash.keySet()){
							chavesEstrangeiras.add(chaveE);
						}
					}
					//Monta o SQL
					PreparedStatement pstmtInsert = connect.prepareStatement("INSERT INTO "+tabela+" ("+camposInsercao+") VALUES ("+interrogacao+")");
					//Insere os dados do banco do servidor no novo registro do banco local
					for(int i = 1; i <= colunas.size(); i++){
						//Tratamento para variaveis inteiras pois nï¿½o podem ser zeradas para chaves estrangeiras pois nï¿½o hï¿½ referencia
						//nas tabeslas originais
						if(rsm.getObject(colunas.get(i-1)) instanceof Integer && (Integer)rsm.getObject(colunas.get(i-1)) == 0 && Util.inArrayString(colunas.get(i-1), chavesEstrangeiras))
							pstmtInsert.setNull(i, Types.INTEGER);
						else 
							pstmtInsert.setObject(i, rsm.getObject(colunas.get(i-1)));
						
						
					}
					if(pstmtInsert.executeUpdate() <= 0){
						return new Result(-1, "Erro ao inserir na tabela no banco local!");
					}
				}
			}
			
			System.out.println("Sincronizaï¿½ï¿½o da tabela "+tabela+" - Servidor -> Cliente concluï¿½da!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar sincronizar bancos, Tabela: "+tabela+", Erro: " + e);
		}
	}
	
	public static Result gerarXmlNotas(int cdEmpresa, int cdNotaFiscal){
		return gerarXmlNotas(cdEmpresa, "2", cdNotaFiscal, null);
	}
	
	public static Result gerarXmlNotas(int cdEmpresa){
		return gerarXmlNotas(cdEmpresa, "2", 0, null);
	}
	public static Result gerarXmlNotas(int cdEmpresa, String nrSerie){
		return gerarXmlNotas(cdEmpresa, nrSerie, 0, null);
	}
	public static Result gerarXmlNotas(int cdEmpresa, String nrSerie, int cdNotaFiscalOriginal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
		    ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM fsc_nota_fiscal A " +
																		 " WHERE (A.st_nota_fiscal = 4 OR A.st_nota_fiscal = 5) AND A.cd_empresa = "+cdEmpresa+" AND A.nr_serie = '"+nrSerie+"' AND A.dt_emissao >= '2015-08-01' AND A.dt_emissao <= '2015-08-31' " + (cdNotaFiscalOriginal > 0 ? " AND A.cd_nota_fiscal = " + cdNotaFiscalOriginal : "")).executeQuery());
		    
		    rsm.beforeFirst();
			while(rsm.next()){
			
				int cdNotaFiscal = rsm.getInt("cd_nota_fiscal");
				
				NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal, connect);
				System.out.println(nota);
				
				String xml = NfeServices.getXmlComProtocolo(cdNotaFiscal, (NotaFiscalHistorico)null, cdEmpresa, connect);
				
				File arquivo = new File(nota.getNrChaveAcesso() + ".xml"); 
				FileOutputStream gravar;
				
				String caminhoPadrao = ParametroServices.getValorOfParametro("NM_REPOSITORIO_XML", cdEmpresa, connect);
				
				if(caminhoPadrao == null || caminhoPadrao.trim().equals("")){
					connect.rollback() ; 
					return new Result(-1, "Parametro de Caminho Padrï¿½o XML nï¿½o esta configurado!");
				}
				
//				String caminhoPasta = caminhoPadrao + "\\" + nota.getDtAutorizacao().get(Calendar.YEAR) + "_" + (nota.getDtAutorizacao().get(Calendar.MONTH) + 1);
				String caminhoPasta = caminhoPadrao + "\\AGOSTO";
				
				File diretorio = new File(caminhoPasta);
				if(!diretorio.exists()){
					diretorio.mkdir();
				}
	    		gravar = new FileOutputStream(caminhoPasta + "\\" + arquivo);
				gravar.write(xml.getBytes());
				gravar.close();
				
				if(nota.getStNotaFiscal() == NotaFiscalServices.CANCELADA){
					xml = "";
					MotivoCancelamento motivoCanc = MotivoCancelamentoDAO.get(nota.getCdMotivoCancelamento(), connect);
					Result resultado = NfeServices.gerarXmlNFECanc(cdNotaFiscal, (motivoCanc == null ? "Motivo nï¿½o informado" : motivoCanc.getDsMotivoCancelamento()),connect);
					String xmlSemAssinar = (String) resultado.getObjects().get("xml");
					if(CertificadoServices.getTipoCertificado(cdEmpresa) == CertificadoServices.A3)
					   xml = (String) NfeServices.assinarNFeA3(xmlSemAssinar, NfeServices.ENVIO, cdEmpresa).getObjects().get("xmlAssinado"); 
				    else{
					   xml = (String) NfeServices.assinarNFeA1(xmlSemAssinar, NfeServices.ENVIO, cdEmpresa).getObjects().get("xmlAssinado"); 
				    }
					
					arquivo = new File("NFeCanc" + nota.getNrChaveAcesso().substring(3) + ".xml"); 
					
					gravar = new FileOutputStream(caminhoPasta + "\\" + arquivo);
					gravar.write(xml.getBytes());
					gravar.close();
				}
			}
			
			return new Result(1, "Xml das notas geradas com sucesso!");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalServices.enviarEmail: " + e);
			return new Result(-1, "Erro ao gerar Xmls: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int updateCombustivelTanque()	{
		Connection connect   = Conexao.conectar();
		try {
			PreparedStatement pstmtAtualizacaoCombustivel = connect.prepareStatement("UPDATE pcb_bico_encerrante A " +
																			"			SET cd_combustivel = (SELECT cd_produto_servico FROM pcb_tanque B WHERE A.cd_tanque = B.cd_tanque ) " +
																			"		  WHERE cd_combustivel IS NULL");

			pstmtAtualizacaoCombustivel.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static sol.util.Result importCidadeByUFDNE()	{
		Connection connect   = Conexao.conectar();
		RandomAccessFile raf = null;
		try {
			/*******************************************************
			 * Importando Estado e cidade DNE
			 *******************************************************/
			System.out.println("Importando Cidades do arquivo CSV...");
			//
			ResultSetMap rsmCidade = ResultSetMap.getResultsetMapFromCSVFile("C:/TIVIC/DATABASES/CIDADE.CSV", ";", true);
			//System.out.println("rsm = " +rsmCidade);
	  		System.out.println("Arquivo carregado...");
			connect.setAutoCommit(false);
			int lines = 0;
			while(rsmCidade.next())	{
				String nmCidade  = rsmCidade.getString("nm_cidade");
	  			String sgEstado  = rsmCidade.getString("sg_estado");
	  			String nrCEP     = (rsmCidade.getString("nr_cep") != null?rsmCidade.getString("nr_cep"): "");
	  			int    cdCidade  = Integer.valueOf(rsmCidade.getString("cd_cidade"));
	  			// Verifica o codigo do estado cadastrado no banco
	  			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_estado FROM grl_estado WHERE sg_estado iLIKE TRIM(\'"+sgEstado+"\')");
	  			ResultSetMap rsmCidadeByUF = new ResultSetMap(pstmt.executeQuery());
	  			//System.out.println(nmCidade + " " + sgEstado + " " + nrCEP);
	  			pstmt.close();
	  			while (rsmCidadeByUF.next()){
	  				int cdEstado  = rsmCidadeByUF.getInt("cd_estado");
	  				Cidade cidade = new Cidade(cdCidade, nmCidade, nrCEP, 0/*vlAltitude*/, 0/*vlLatitude*/, 0 /*vlLongitude*/, cdEstado, ""/*idCidade*/, 0/*cdRegiao*/, ""/*idIbge*/, ""/*sgCidade*/, 0/*qtDistanciaCapital*/, 0/*qtDistanciaBase*/);
	  				int result = CidadeDAO.insert(cidade);
					if(result < 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar tabela grl_cidade");
					}else
						lines++;
		  			//
	  			}
			}
			//
			connect.commit();
			System.out.println("Atualizacao de Tabela grl_cidade foi concluï¿½da concluï¿½da - "+lines+" registros atualizados!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar Atualizar grl_cidade!", e);
		}
		finally{
			if(raf!=null)
		  		try {raf.close(); } catch(Exception e){};
			Conexao.desconectar(connect);
		}
	}
	
	public static sol.util.Result configuraTxtImportacao()	{
		Connection connect   = Conexao.conectar();
		RandomAccessFile raf = null;
		try {
			/*******************************************************
			 * Importando Estado e cidade DNE
			 *******************************************************/
			InputStream is = new FileInputStream("C:/TIVIC/DATABASES/TabelaNcm.txt");  
	        OutputStream os = new FileOutputStream("C:/TIVIC/DATABASES/TabelaNcm1.txt");  
	        OutputStreamWriter osw = new OutputStreamWriter(os);  
	        BufferedWriter bw = new BufferedWriter(osw);  
	        
	        Scanner entrada = new Scanner(is);  
	        while (entrada.hasNextLine()) {  
	            String linha = entrada.nextLine();  
	            //if (linha == null || linha == "")
	            //acrescenta o ';' na linha 
	            System.out.println(linha.substring(0,9));
	            String newLinha = "";
	            if (linha.substring(2,10) != "--------"){
	            	newLinha = linha.substring(0,8)+";"+ linha.replace(linha.substring(0,8), "");	 
	            	
	            	//String newlinha = linha.substring(0, 8);  
	            	bw.write(newLinha + ";");   
	            	bw.newLine();  
	            }
	  
	        }  
	  
	        bw.close();  
	        entrada.close();  
	       

			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar Atualizar grl_cidade!", e);
		}
		finally{
			if(raf!=null)
		  		try {raf.close(); } catch(Exception e){};
			Conexao.desconectar(connect);
		}
	}
	
	/*Atualizara os itens de documentos de entrada e saida anteriores a data atual em relaï¿½ï¿½o as regras de PIS/COFINS*/
	public static Result atualizarPisCofins(){
		Connection connect = Conexao.conectar();
		try{
			connect.setAutoCommit(false);
			
			GregorianCalendar dtInicioJaneiro = new GregorianCalendar();
			dtInicioJaneiro.set(Calendar.DAY_OF_MONTH, 1);
			dtInicioJaneiro.set(Calendar.MONTH, 0);
			dtInicioJaneiro.set(Calendar.YEAR, 2016);
			
			GregorianCalendar dtFinalJaneiro = new GregorianCalendar();
			dtFinalJaneiro.set(Calendar.DAY_OF_MONTH, 31);
			dtFinalJaneiro.set(Calendar.MONTH, 11);
			dtFinalJaneiro.set(Calendar.YEAR, 2016);
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("CAST(dt_documento_entrada AS DATE)", Util.formatDate(dtInicioJaneiro, "dd/MM/yyyy"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
			criterios.add(new ItemComparator("CAST(dt_documento_entrada AS DATE)", Util.formatDate(dtFinalJaneiro, "dd/MM/yyyy"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
			ResultSetMap rsmDocumentoSaida = DocumentoSaidaDAO.find(criterios, connect);
			ResultSetMap rsmDocumentoSaidaItem;
			ResultSetMap rsmDocumentoSaidaItemTributo;
			while(rsmDocumentoSaida.next()){
				/*Analisar item por item do documento para saber quais estï¿½o sujeitos a PIS e COFINS e aplicar a regra*/
				rsmDocumentoSaidaItem = DocumentoSaidaServices.getAllItens(rsmDocumentoSaida.getInt("cd_documento_entrada"), false, connect);
				while(rsmDocumentoSaidaItem.next()){
					rsmDocumentoSaidaItemTributo = calcTributos(DocumentoSaidaItemDAO.get(rsmDocumentoSaidaItem.getInt("cd_documento_entrada"), rsmDocumentoSaidaItem.getInt("cd_produto_servico"), rsmDocumentoSaidaItem.getInt("cd_empresa"), rsmDocumentoSaidaItem.getInt("cd_item"), connect), connect);
					if(rsmDocumentoSaidaItemTributo == null){
						Conexao.rollback(connect);
						return new Result(-1, "Erro na tributacao de um item");
					}
				}
			}
			
			ResultSetMap rsmDocumentoEntrada = DocumentoEntradaDAO.find(criterios, connect);
			ResultSetMap rsmDocumentoEntradaItem;
			ResultSetMap rsmDocumentoEntradaItemTributo;
			while(rsmDocumentoEntrada.next()){
				/*Analisar item por item do documento para saber quais estï¿½o sujeitos a PIS e COFINS e aplicar a regra*/
				rsmDocumentoEntradaItem = DocumentoEntradaServices.getAllItens(rsmDocumentoEntrada.getInt("cd_documento_entrada"), false, connect);
				while(rsmDocumentoEntradaItem.next()){
					rsmDocumentoEntradaItemTributo = calcTributos(DocumentoEntradaItemDAO.get(rsmDocumentoEntradaItem.getInt("cd_documento_entrada"), rsmDocumentoEntradaItem.getInt("cd_produto_servico"), rsmDocumentoEntradaItem.getInt("cd_empresa"), rsmDocumentoEntradaItem.getInt("cd_item"), connect), connect);
					if(rsmDocumentoEntradaItemTributo == null){
						Conexao.rollback(connect);
						return new Result(-1, "Erro na tributacao de um item");
					}
				}
			}
			
			return new Result(1, "Sucesso");
		}
		
		catch(Exception e){
			Conexao.rollback(connect);
			return new Result(-1, "Erro");
		}
		
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	
	/**
     * Busca os tributos (SOMENTE PIS E COFINS) a ser calculado numa determinada operaï¿½ï¿½o de venda levando em consideraï¿½ï¿½o a
     * o destino e o produto/serviï¿½o ou grupo do qual faï¿½a parte
     * @param item Item (produto/serviï¿½o) da Saï¿½da
     * @return Impostos e alï¿½quotas que incidem sobre a operaï¿½ï¿½o de venda
     */
	public static ResultSetMap calcTributos(DocumentoSaidaItem item, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			/*
			 *  Instanciando Documento de Saï¿½da
			 */
			DocumentoSaida doc = DocumentoSaidaDAO.get(item.getCdDocumentoSaida(), connect);
			/*
			 *  Descobrindo dados do endereï¿½o
			 */
			int cdCidade=0, cdEstado=0, cdPais=0;
			ResultSet rs = connect.prepareStatement("SELECT A.cd_cidade, B.cd_estado, C.cd_pais " +
													"FROM grl_pessoa_endereco A " +
													"LEFT OUTER JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade)" +
													"LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado)" +
													"WHERE A.cd_pessoa    = "+doc.getCdCliente()+
													"  AND A.lg_principal = 1").executeQuery();
			if(rs.next())	{
				cdCidade = rs.getInt("cd_cidade");
				cdEstado = rs.getInt("cd_estado");
				cdPais   = rs.getInt("cd_pais");
			}
			else	{
				rs = connect.prepareStatement("SELECT D.cd_cidade, B.cd_estado, C.cd_pais " +
											  "FROM grl_empresa A " +
											  "LEFT OUTER JOIN grl_pessoa_endereco D ON (A.cd_empresa = D.cd_pessoa AND D.lg_principal = 1) " +
											  "LEFT OUTER JOIN grl_cidade B ON (D.cd_cidade = B.cd_cidade)" +
											  "LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado)" +
											  "WHERE A.cd_empresa = "+doc.getCdEmpresa()).executeQuery();
				if(rs.next())	{
					cdCidade = rs.getInt("cd_cidade");
					cdEstado = rs.getInt("cd_estado");
					cdPais   = rs.getInt("cd_pais");
				}
			}
			
			
			/*
			 *  Excluindo aliquotas pre-existentes
			 */
			int cdTributoPIS 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0);
			int cdTributoCOFINS = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0);
			
			connect.prepareStatement("DELETE FROM adm_saida_item_aliquota " +
					                 "WHERE cd_documento_saida = "+item.getCdDocumentoSaida()+
					                 "  AND cd_produto_servico = "+item.getCdProdutoServico() + 
					                 "  AND (cd_tributo = "+cdTributoPIS+" OR cd_tributo = "+cdTributoCOFINS+")").executeUpdate();
			/*
			 *  Pesquisa classificacao fiscal do produto/serviï¿½o
			 */
			rs = connect.prepareStatement("SELECT * FROM grl_produto_servico " +
					                      "WHERE cd_produto_servico = " +item.getCdProdutoServico()).executeQuery();
			int cdClassificacaoFiscal = 0;
			if(rs.next())
				cdClassificacaoFiscal = rs.getInt("cd_classificacao_fiscal");
			/*
			 *  Calcula base de cï¿½lculo
			 */
			float vlBaseCalculo = (item.getQtSaida() * item.getVlUnitario()) - item.getVlDesconto() + item.getVlAcrescimo();
			
			//PIS
			ResultSetMap rsm = calcTributos(cdTributoPIS, TributoAliquotaServices.OP_VENDA, doc.getCdNaturezaOperacao(),
					                                        cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
					                                        vlBaseCalculo, connect);
			/*
			 *  Insere impostos calculados
			 */
			while(rsm.next())	{
				if (SaidaItemAliquotaDAO.delete(item.getCdProdutoServico(), item.getCdDocumentoSaida(), item.getCdEmpresa(),
						rsm.getInt("cd_tributo_aliquota"), rsm.getInt("cd_tributo"), item.getCdItem(), connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return null;
				}
				SaidaItemAliquota itemAliquota = new SaidaItemAliquota(item.getCdProdutoServico(),
						item.getCdDocumentoSaida(),
						item.getCdEmpresa(),
						rsm.getInt("cd_tributo_aliquota"),
						rsm.getInt("cd_tributo"),
						rsm.getFloat("pr_aliquota"),
						rsm.getFloat("vl_base_calculo"),
						item.getCdItem());
				if (SaidaItemAliquotaDAO.insert(itemAliquota, connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return null;
				}
			}
			
			//COFINS
			rsm = calcTributos(cdTributoCOFINS, TributoAliquotaServices.OP_VENDA, doc.getCdNaturezaOperacao(),
                    cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
                    vlBaseCalculo, connect);
			/*
			*  Insere impostos calculados
			*/
			while(rsm.next())	{
				if (SaidaItemAliquotaDAO.delete(item.getCdProdutoServico(), item.getCdDocumentoSaida(), item.getCdEmpresa(),
					rsm.getInt("cd_tributo_aliquota"), rsm.getInt("cd_tributo"), item.getCdItem(), connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
						return null;
				}
				SaidaItemAliquota itemAliquota = new SaidaItemAliquota(item.getCdProdutoServico(),
																		item.getCdDocumentoSaida(),
																		item.getCdEmpresa(),
																		rsm.getInt("cd_tributo_aliquota"),
																		rsm.getInt("cd_tributo"),
																		rsm.getFloat("pr_aliquota"),
																		rsm.getFloat("vl_base_calculo"),
																		item.getCdItem());
				if (SaidaItemAliquotaDAO.insert(itemAliquota, connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return null;
				}
			}
			if (isConnectionNull)
				connect.commit();
			/* Pesquisa impostos gerados*/
			ArrayList<sol.dao.ItemComparator> crt = new ArrayList<sol.dao.ItemComparator> ();
			crt.add(new ItemComparator("A.cd_documento_saida", String.valueOf(item.getCdDocumentoSaida()), ItemComparator.EQUAL, java.sql.Types.INTEGER));
			crt.add(new ItemComparator("A.cd_produto_servico", String.valueOf(item.getCdProdutoServico()), ItemComparator.EQUAL, java.sql.Types.INTEGER));
			rsm = SaidaItemAliquotaServices.find(crt, connect);
			return rsm;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap calcTributos(DocumentoEntradaItem item, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			/*
			 *  Instanciando Documento de Saï¿½da
			 */
			DocumentoEntrada doc = DocumentoEntradaDAO.get(item.getCdDocumentoEntrada(), connect);
			/*
			 *  Descobrindo dados do endereï¿½o
			 */
			int cdCidade=0, cdEstado=0, cdPais=0;
			ResultSet rs = connect.prepareStatement("SELECT A.cd_cidade, B.cd_estado, C.cd_pais " +
													"FROM grl_pessoa_endereco A " +
													"LEFT OUTER JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade)" +
													"LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado)" +
													"WHERE A.cd_pessoa    = "+doc.getCdFornecedor()+
													"  AND A.lg_principal = 1").executeQuery();
			if(rs.next())	{
				cdCidade = rs.getInt("cd_cidade");
				cdEstado = rs.getInt("cd_estado");
				cdPais   = rs.getInt("cd_pais");
			}
			else	{
				rs = connect.prepareStatement("SELECT D.cd_cidade, B.cd_estado, C.cd_pais " +
											  "FROM grl_empresa A " +
											  "LEFT OUTER JOIN grl_pessoa_endereco D ON (A.cd_empresa = D.cd_pessoa AND D.lg_principal = 1) " +
											  "LEFT OUTER JOIN grl_cidade B ON (D.cd_cidade = B.cd_cidade)" +
											  "LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado)" +
											  "WHERE A.cd_empresa = "+doc.getCdEmpresa()).executeQuery();
				if(rs.next())	{
					cdCidade = rs.getInt("cd_cidade");
					cdEstado = rs.getInt("cd_estado");
					cdPais   = rs.getInt("cd_pais");
				}
			}
			
			
			/*
			 *  Excluindo aliquotas pre-existentes
			 */
			int cdTributoPIS 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0);
			int cdTributoCOFINS = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0);
			
			connect.prepareStatement("DELETE FROM adm_entrada_item_aliquota " +
					                 "WHERE cd_documento_entrada = "+item.getCdDocumentoEntrada()+
					                 "  AND cd_produto_servico = "+item.getCdProdutoServico() + 
					                 "  AND (cd_tributo = "+cdTributoPIS+" OR cd_tributo = "+cdTributoCOFINS+")").executeUpdate();
			/*
			 *  Pesquisa classificacao fiscal do produto/serviï¿½o
			 */
			rs = connect.prepareStatement("SELECT * FROM grl_produto_servico " +
					                      "WHERE cd_produto_servico = " +item.getCdProdutoServico()).executeQuery();
			int cdClassificacaoFiscal = 0;
			if(rs.next())
				cdClassificacaoFiscal = rs.getInt("cd_classificacao_fiscal");
			/*
			 *  Calcula base de cï¿½lculo
			 */
			float vlBaseCalculo = (item.getQtEntrada() * item.getVlUnitario()) - item.getVlDesconto() + item.getVlAcrescimo();
			
			//PIS
			ResultSetMap rsm = calcTributos(cdTributoPIS, TributoAliquotaServices.OP_COMPRA, doc.getCdNaturezaOperacao(),
					                                        cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
					                                        vlBaseCalculo, connect);
			/*
			 *  Insere impostos calculados
			 */
			while(rsm.next())	{
				if (EntradaItemAliquotaDAO.delete(item.getCdProdutoServico(), item.getCdDocumentoEntrada(), item.getCdEmpresa(),
						rsm.getInt("cd_tributo_aliquota"), rsm.getInt("cd_tributo"), item.getCdItem(), connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return null;
				}
				EntradaItemAliquota itemAliquota = new EntradaItemAliquota(item.getCdProdutoServico(),
						item.getCdDocumentoEntrada(),
						item.getCdEmpresa(),
						rsm.getInt("cd_tributo_aliquota"),
						rsm.getInt("cd_tributo"),
						rsm.getInt("cd_item"),
						rsm.getFloat("pr_aliquota"),
						rsm.getFloat("vl_base_calculo"),
						item.getCdItem());
				if (EntradaItemAliquotaDAO.insert(itemAliquota, connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return null;
				}
			}
			
			//COFINS
			rsm = calcTributos(cdTributoCOFINS, TributoAliquotaServices.OP_VENDA, doc.getCdNaturezaOperacao(),
                    cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
                    vlBaseCalculo, connect);
			/*
			*  Insere impostos calculados
			*/
			while(rsm.next())	{
				if (EntradaItemAliquotaDAO.delete(item.getCdProdutoServico(), item.getCdDocumentoEntrada(), item.getCdEmpresa(),
					rsm.getInt("cd_tributo_aliquota"), rsm.getInt("cd_tributo"), item.getCdItem(), connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
						return null;
				}
				EntradaItemAliquota itemAliquota = new EntradaItemAliquota(item.getCdProdutoServico(),
																		item.getCdDocumentoEntrada(),
																		item.getCdEmpresa(),
																		rsm.getInt("cd_tributo_aliquota"),
																		rsm.getInt("cd_tributo"),
																		rsm.getInt("cd_item"),
																		rsm.getFloat("pr_aliquota"),
																		rsm.getFloat("vl_base_calculo"),
																		item.getCdItem());
				if (EntradaItemAliquotaDAO.insert(itemAliquota, connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return null;
				}
			}
			if (isConnectionNull)
				connect.commit();
			/* Pesquisa impostos gerados*/
			ArrayList<sol.dao.ItemComparator> crt = new ArrayList<sol.dao.ItemComparator> ();
			crt.add(new ItemComparator("A.cd_documento_entrada", String.valueOf(item.getCdDocumentoEntrada()), ItemComparator.EQUAL, java.sql.Types.INTEGER));
			crt.add(new ItemComparator("A.cd_produto_servico", String.valueOf(item.getCdProdutoServico()), ItemComparator.EQUAL, java.sql.Types.INTEGER));
			rsm = EntradaItemAliquotaServices.find(crt, connect);
			return rsm;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
     * Busca os tributos (SOMENTE PIS E COFINS) a ser calculado numa determinada operaï¿½ï¿½o de venda levando em consideraï¿½ï¿½o a
     * o destino e o produto/serviï¿½o ou grupo do qual faï¿½a parte
     * @param item Item (produto/serviï¿½o) da Saï¿½da
     * @return Impostos e alï¿½quotas que incidem sobre a operaï¿½ï¿½o de venda
     */
	public static ResultSetMap calcTributos(int cdTributo, int tpOperacao, int cdNaturezaOperacao, int cdClassificacaoFiscal, int cdProdutoServico,
			int cdPais, int cdEstado, int cdCidade, float vlBaseCalculo, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			/*
			 *  Pesquisa alï¿½quotas especï¿½ficas para o produto
			 */
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(
					"SELECT A.*, B.* " +
					"FROM adm_tributo A, adm_tributo_aliquota B " +
					"WHERE A.cd_tributo  = B.cd_tributo " +
					"  AND B.tp_operacao = "+tpOperacao+
					"  AND A.cd_tributo  = "+cdTributo+
					"  AND EXISTS (SELECT * FROM adm_produto_servico_tributo C " +
					"              WHERE A.cd_tributo  = C.cd_tributo " +
					"                AND B.cd_tributo_aliquota = C.cd_tributo_aliquota " +
					"                AND C.cd_natureza_operacao = "+cdNaturezaOperacao+
					"                AND (C.cd_pais   = "+cdPais+" OR C.cd_pais IS NULL) "+
					"                AND (C.cd_estado = "+cdEstado+" OR C.cd_estado IS NULL) "+
					"                AND (C.cd_cidade = "+cdCidade+" OR C.cd_cidade IS NULL) "+
					"                AND C.cd_produto_servico = "+cdProdutoServico+")").executeQuery());
			/*
			 *  Pesquisa alï¿½quotas configuradas para a classificaï¿½ï¿½o fiscal
			 */
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.*, B.* " +
					"FROM adm_tributo A, adm_tributo_aliquota B " +
					"WHERE A.cd_tributo  = B.cd_tributo " +
					"  AND B.tp_operacao = "+tpOperacao+
					"  AND A.cd_tributo  = "+cdTributo+
					"  AND EXISTS (SELECT * FROM adm_produto_servico_tributo C " +
					"              WHERE A.cd_tributo  = C.cd_tributo " +
					"                AND B.cd_tributo_aliquota = C.cd_tributo_aliquota " +
					"                AND C.cd_natureza_operacao = "+cdNaturezaOperacao+
					"                AND (C.cd_pais   = "+cdPais+" OR C.cd_pais   IS NULL) "+
					"                AND (C.cd_estado = "+cdEstado+" OR C.cd_estado IS NULL) "+
					"                AND (C.cd_cidade = "+cdCidade+" OR C.cd_cidade IS NULL) "+
					"                AND C.cd_classificacao_fiscal = "+cdClassificacaoFiscal+")");
			ResultSetMap rsm2 = new ResultSetMap(pstmt.executeQuery());
			while(rsm2.next())	{
				if(!rsm.locate("cd_tributo", rsm2.getInt("cd_tributo"))){
					rsm.addRegister(rsm2.getRegister());
				}
			}
			/*
			 *  Calculando alï¿½quotas
			 */
			float vlBaseCalculoOriginal = vlBaseCalculo;
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("nr_ordem_calculo");
			rsm.orderBy(orderBy);
			rsm.beforeFirst();
			while(rsm.next())	{
				vlBaseCalculo = vlBaseCalculoOriginal;
				/*
				 *  Verificando impostos que compï¿½em a base de cï¿½lculo
				 */
				rsm2 = new ResultSetMap(connect.prepareStatement("SELECT * FROM adm_tributo_base_calculo " +
						                                         "WHERE cd_tributo = "+rsm.getInt("cd_tributo")).executeQuery());
				int pos = rsm.getPosition();
				while(rsm2.next()){ 
					if(rsm.locate("cd_tributo", rsm2.getInt("cd_tributo_base")))
						vlBaseCalculo += rsm.getFloat("vl_tributo");
				}
				rsm.goTo(pos);
				/*
				 * Alterando base de cï¿½lculo
				 */
				if(rsm.getFloat("vl_variacao_base")>0)	{
					if(rsm.getInt("tp_fator_variacao_base") == TributoAliquotaServices.FT_VALOR)
						vlBaseCalculo -= rsm.getFloat("vl_variacao_base");
					else
						vlBaseCalculo -= (rsm.getFloat("vl_variacao_base") * vlBaseCalculo / 100);
				}
				/*
				 * Realizando cï¿½lculo
				 */
				rsm.setValueToField("vl_base_calculo", new Float(vlBaseCalculo));
				float vlTributo = vlBaseCalculo * rsm.getFloat("pr_aliquota") / 100;
				/*
				 * Alterando resultado obtido
				 */
				if(rsm.getFloat("vl_variacao_resultado")>0)	{
					if(rsm.getInt("tp_fator_variacao_resultado")==TributoAliquotaServices.FT_VALOR)
						vlTributo -= rsm.getFloat("vl_variacao_resultado");
					else
						vlTributo -= (rsm.getFloat("vl_variacao_resultado") * vlBaseCalculo / 100);
				}
				rsm.setValueToField("vl_tributo", new Float(vlTributo));
			}
			//
			rsm.beforeFirst();
			return rsm;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo para corrigir a antiga tabela de adm_cliente_pagamento, fazendo com que a parte de pagamento seja independente de cliente
	 * @since 01/08/2014
	 * @author Gabriel
	 * @return
	 */
	public static Result correcaoCondicaoPagamento()
	{
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = true;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			
			System.out.println("Iniciando correï¿½ï¿½o");
			
			//Criaï¿½ï¿½o de uma tabela temporaria que armazena os dados jï¿½ cadastrados em cliente_pagamento enquanto a tabela estï¿½ sendo modificada
			connect.prepareStatement("CREATE TABLE TAB_TEMP (" +
					"					cd_empresa INTEGER," +
					"					cd_plano_pagamento INTEGER," +
					"					cd_forma_pagamento INTEGER," +
					"					cd_pessoa INTEGER," +
					"					vl_limite DOUBLE PRECISION," +
					"					vl_limite_mensal DOUBLE PRECISION," +
					"					vl_troco DOUBLE PRECISION," +
					"					id_cliente_pagamento VARCHAR(50), " +
					"					st_cliente_pagamento INTEGER," +
					"					lg_padrao INTEGER," +
					"					nm_cliente_pagamento VARCHAR(100)," +
					"					nm_descricao VARCHAR(500)," +
					"					dt_validade_condicao TIMESTAMP," +
					"					dt_validade_limite TIMESTAMP," +
					"					lg_permite_troco INTEGER," +
					"					cd_classificacao INTEGER," +
					"					cd_cliente_pagamento INTEGER" +
					")").executeUpdate();
			
			//Insere os dados de cliente pagamento para a tabela temporaria
			PreparedStatement pstmtUpdateTemp = connect.prepareStatement("INSERT INTO TAB_TEMP VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM adm_cliente_pagamento").executeQuery());
			while(rsm.next()){
				pstmtUpdateTemp.setInt(1, rsm.getInt("cd_empresa"));
				pstmtUpdateTemp.setInt(2, rsm.getInt("cd_plano_pagamento"));
				pstmtUpdateTemp.setInt(3, rsm.getInt("cd_forma_pagamento"));
				pstmtUpdateTemp.setInt(4, rsm.getInt("cd_pessoa"));
				pstmtUpdateTemp.setFloat(5, rsm.getFloat("vl_limite"));
				pstmtUpdateTemp.setFloat(6, rsm.getFloat("vl_limite_mensal"));
				pstmtUpdateTemp.setFloat(7, rsm.getFloat("vl_troco"));
				pstmtUpdateTemp.setString(8, rsm.getString("id_cliente_pagamento"));
				pstmtUpdateTemp.setInt(9, rsm.getInt("st_cliente_pagamento"));
				pstmtUpdateTemp.setInt(10, rsm.getInt("lg_padrao"));
				pstmtUpdateTemp.setString(11, rsm.getString("nm_cliente_pagamento"));
				pstmtUpdateTemp.setString(12, rsm.getString("nm_descricao"));
				pstmtUpdateTemp.setTimestamp(13, (rsm.getGregorianCalendar("dt_validade_condicao") != null ? new Timestamp(rsm.getGregorianCalendar("dt_validade_condicao").getTimeInMillis()) : null));
				pstmtUpdateTemp.setTimestamp(14, (rsm.getGregorianCalendar("dt_validade_limite") != null ? new Timestamp(rsm.getGregorianCalendar("dt_validade_limite").getTimeInMillis()) : null));
				pstmtUpdateTemp.setInt(15, rsm.getInt("lg_permite_troco"));
				pstmtUpdateTemp.setInt(16, rsm.getInt("cd_classificacao"));
				pstmtUpdateTemp.setInt(17, rsm.getInt("cd_cliente_pagamento"));
				pstmtUpdateTemp.executeUpdate();
			}
			
			
			//Deleta a tabela cliente_pagamento do Banco de Dados
			connect.prepareStatement("DROP TABLE adm_cliente_pagamento").executeUpdate();
			
			//Cria a nota tabela de condiï¿½ï¿½o de pagamento
			connect.prepareStatement("CREATE TABLE ADM_CONDICAO_PAGAMENTO (" +
					"					cd_condicao_pagamento INTEGER," +
					"					vl_limite NUMERIC," +
					"					vl_limite_mensal NUMERIC," +
					"					vl_troco NUMERIC," +
					"					id_condicao_pagamento VARCHAR(50), " +
					"					st_condicao_pagamento INTEGER," +
					"					lg_padrao INTEGER," +
					"					nm_condicao_pagamento VARCHAR(100)," +
					"					txt_descricao VARCHAR(500)," +
					"					dt_validade_condicao TIMESTAMP," +
					"					dt_validade_limite TIMESTAMP," +
					"					lg_permite_troco INTEGER" +
					")").executeUpdate();
			connect.prepareStatement("ALTER TABLE ADM_CONDICAO_PAGAMENTO " +
					"					ADD PRIMARY KEY (cd_condicao_pagamento)").executeUpdate();
			
			//Cria a tabela que relaciona condiï¿½ï¿½o de pagamento, forma de pagamento e plano de pagamento
			connect.prepareStatement("CREATE TABLE ADM_CONDICAO_FORMA_PLANO_PAGAMENTO (" +
					"					cd_condicao_pagamento INTEGER," +
					"					cd_plano_pagamento INTEGER," +
					"					cd_forma_pagamento INTEGER," +
					"					cd_empresa INTEGER)").executeUpdate();
			connect.prepareStatement("ALTER TABLE ADM_CONDICAO_FORMA_PLANO_PAGAMENTO " +
					"					ADD PRIMARY KEY (cd_condicao_pagamento, cd_plano_pagamento, cd_forma_pagamento, cd_empresa)").executeUpdate();
			connect.prepareStatement("ALTER TABLE ADM_CONDICAO_FORMA_PLANO_PAGAMENTO " +
					"					ADD FOREIGN KEY (cd_condicao_pagamento) REFERENCES ADM_CONDICAO_PAGAMENTO").executeUpdate();
			connect.prepareStatement("ALTER TABLE ADM_CONDICAO_FORMA_PLANO_PAGAMENTO " +
					"					ADD FOREIGN KEY (cd_plano_pagamento, cd_forma_pagamento, cd_empresa) REFERENCES ADM_FORMA_PLANO_PAGAMENTO(cd_plano_pagamento, cd_forma_pagamento, cd_empresa)").executeUpdate();
			
			
			//Cria a tabela que relaciona condiï¿½ï¿½o de pagamento com cliente ou com classificaï¿½ï¿½o
			connect.prepareStatement("CREATE TABLE ADM_CONDICAO_PAGAMENTO_CLIENTE (" + 
					"					cd_condicao_pagamento_cliente INTEGER," +
					"					cd_condicao_pagamento INTEGER," +
					"					cd_empresa INTEGER," +
					"					cd_pessoa INTEGER," +
					"					cd_classificacao INTEGER)").executeUpdate();
			connect.prepareStatement("ALTER TABLE ADM_CONDICAO_PAGAMENTO_CLIENTE " +
					"					ADD PRIMARY KEY (cd_condicao_pagamento_cliente, cd_condicao_pagamento)").executeUpdate();
			connect.prepareStatement("ALTER TABLE ADM_CONDICAO_PAGAMENTO_CLIENTE " +
					"					ADD FOREIGN KEY (cd_condicao_pagamento) REFERENCES ADM_CONDICAO_PAGAMENTO").executeUpdate();
			connect.prepareStatement("ALTER TABLE ADM_CONDICAO_PAGAMENTO_CLIENTE " +
					"					ADD FOREIGN KEY (cd_empresa, cd_pessoa) REFERENCES ADM_CLIENTE(cd_empresa, cd_pessoa)").executeUpdate();
			connect.prepareStatement("ALTER TABLE ADM_CONDICAO_PAGAMENTO_CLIENTE " +
					"					ADD FOREIGN KEY (cd_classificacao) REFERENCES ADM_CLASSIFICACAO").executeUpdate();
			
			//Selecionando registros da tabela temporaria para redistribuir corretamente nas tabelas novas
			ResultSetMap rsmSelectTemp = new ResultSetMap(connect.prepareStatement("SELECT * FROM TAB_TEMP").executeQuery());
			PreparedStatement pstmtCondicaoPagamento = connect.prepareStatement("SELECT * FROM adm_condicao_pagamento A " +
					"															   JOIN adm_condicao_forma_plano_pagamento B ON (A.cd_condicao_pagamento = B.cd_condicao_pagamento) " +
					"															 WHERE B.cd_forma_pagamento = ? " + 
					"															   AND A.vl_limite          = ?");
			ResultSetMap rsmCondicaoPagamento;
			
			PreparedStatement pstmtFormaPlanoPagamento = connect.prepareStatement("SELECT * FROM adm_forma_plano_pagamento A " +
					"															 WHERE cd_forma_pagamento = ?");
			ResultSetMap rsmFormaPlanoPagamento;
			
			while(rsmSelectTemp.next()){
				//Busca uma condiï¿½ï¿½o de pagamento que jï¿½ esteja cadastrado com determinada forma de pagamento e limite
				pstmtCondicaoPagamento.setInt(1, rsmSelectTemp.getInt("cd_forma_pagamento"));
				pstmtCondicaoPagamento.setFloat(2, rsmSelectTemp.getFloat("vl_limite"));
				rsmCondicaoPagamento = new ResultSetMap(pstmtCondicaoPagamento.executeQuery());
				//Encontrado jï¿½ um registro de uma condiï¿½ï¿½o de pagamento com determinada forma de pagamento e limite cadastrado
				if(rsmCondicaoPagamento.next()){
					if(CondicaoPagamentoClienteDAO.insert(new CondicaoPagamentoCliente(0, rsmCondicaoPagamento.getInt("cd_condicao_pagamento"), rsmSelectTemp.getInt("cd_empresa"), rsmSelectTemp.getInt("cd_pessoa"), 0), connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao inserir CondicaoPagamentoCliente");
					}
				}
				//Nï¿½o encontrado - Incluir registro na tabela de condiï¿½ï¿½o de pagamento e na tabela de condiï¿½ï¿½o de pagamento cliente
				else{
					//Insere a nova condiï¿½ï¿½o de pagamento
					int code = CondicaoPagamentoDAO.insert(new CondicaoPagamento(0, rsmSelectTemp.getFloat("vl_limite"), rsmSelectTemp.getFloat("vl_limite_mensal"), rsmSelectTemp.getFloat("vl_troco"), 
																					rsmSelectTemp.getString("id_cliente_pagamento"), rsmSelectTemp.getInt("st_cliente_pagamento"), rsmSelectTemp.getInt("lg_padrao"), 
																					rsmSelectTemp.getString("nm_cliente_pagamento"), rsmSelectTemp.getString("nm_descricao"), rsmSelectTemp.getGregorianCalendar("dt_validade_condicao"), 
																					rsmSelectTemp.getGregorianCalendar("dt_validade_limite"), rsmSelectTemp.getInt("lg_permite_troco")), connect);
					if(code < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao inserir CondicaoPagamento");
					}
					
					//Insere a relaï¿½ï¿½o dela com forma de pagamento e plano de pagamento
					pstmtFormaPlanoPagamento.setInt(1, rsmSelectTemp.getInt("cd_forma_pagamento"));
					rsmFormaPlanoPagamento = new ResultSetMap(pstmtFormaPlanoPagamento.executeQuery());
					while(rsmFormaPlanoPagamento.next()){
						if(CondicaoFormaPlanoPagamentoDAO.insert(new CondicaoFormaPlanoPagamento(code, rsmFormaPlanoPagamento.getInt("cd_plano_pagamento"), rsmFormaPlanoPagamento.getInt("cd_forma_pagamento"), rsmFormaPlanoPagamento.getInt("cd_empresa")), connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao inserir CondicaoFormaPlanoPagamento");
						}
					}
					
					//Insere a relaï¿½ï¿½o dela com o cliente
					if(CondicaoPagamentoClienteDAO.insert(new CondicaoPagamentoCliente(0, code, rsmSelectTemp.getInt("cd_empresa"), rsmSelectTemp.getInt("cd_pessoa"), 0), connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao inserir CondicaoPagamentoCliente");
					}
				}
				
			}
			
			//Por fim a tabela temporaria ï¿½ destruida
			connect.prepareStatement("DROP TABLE tab_temp").executeUpdate();
			
			System.out.println("Finalizando correï¿½ï¿½o");			
			
			connect.commit();
			
			return new Result(1);
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	/**
	 * Metodo para corrigir tornar plano de pagamento e programa de fatura em uma relaï¿½ï¿½o de N para N
	 * @since 08/08/2014
	 * @author Gabriel
	 * @return
	 */
	public static Result correcaoProgramaFaturaPlanoPagamento()
	{
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = true;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			
			System.out.println("Iniciando correï¿½ï¿½o");
			
			
			//Cria a nota tabela que relaciona programa de fatura e plano de pagamento
			connect.prepareStatement("CREATE TABLE adm_programa_fatura_plano (" +
					"					cd_programa_fatura INTEGER," +
					"					cd_plano_pagamento INTEGER" +
					")").executeUpdate();
			connect.prepareStatement("ALTER TABLE adm_programa_fatura_plano " +
					"					ADD PRIMARY KEY (cd_programa_fatura, cd_plano_pagamento)").executeUpdate();
			connect.prepareStatement("ALTER TABLE adm_programa_fatura_plano " +
					"					ADD FOREIGN KEY (cd_programa_fatura) REFERENCES adm_programa_fatura").executeUpdate();
			connect.prepareStatement("ALTER TABLE adm_programa_fatura_plano " +
					"					ADD FOREIGN KEY (cd_plano_pagamento) REFERENCES adm_plano_pagamento").executeUpdate();
			
			
			//Selecionando registros do programa de fatura para relacionar com o plano de pagamento
			ResultSetMap rsmSelectTemp = new ResultSetMap(connect.prepareStatement("SELECT * FROM adm_programa_fatura " +
					 															"		WHERE cd_plano_pagamento IS NOT NULL").executeQuery());
			
			//Percorre os programas de fatura que contem algum plano de pagamento, e coloca sua relaï¿½ï¿½o na nova tabela
			while(rsmSelectTemp.next()){
				connect.prepareStatement("INSERT INTO adm_programa_fatura_plano VALUES ("+rsmSelectTemp.getInt("cd_programa_fatura")+", "+rsmSelectTemp.getInt("cd_plano_pagamento")+")").executeUpdate();		
			}
			
			//Exclui o campo de cd_plano_pagamento de programa de fatura
			connect.prepareStatement("ALTER TABLE adm_programa_fatura " + 
					"					DROP CONSTRAINT adm_programa_fatura_cd_plano_pagamento_fkey;").executeUpdate();
			connect.prepareStatement("ALTER TABLE adm_programa_fatura " +
					"					DROP CONSTRAINT adm_programa_fatura_cd_plano_pagamento_fkey1;").executeUpdate();
			connect.prepareStatement("ALTER TABLE adm_programa_fatura " +
  				  	"					DROP COLUMN cd_plano_pagamento;").executeUpdate();
			
			System.out.println("Finalizando correï¿½ï¿½o");			
			
			connect.commit();
			
			return new Result(1);
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	/**
	 * Metodo para corrigir local de armazenamento de tanques de Outubro
	 * @since 20/10/2014
	 * @author Gabriel
	 * @return
	 */
	public static Result correcaoLocalDeArmazenamentoCombustivel()
	{
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = true;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			Empresa empresa = EmpresaServices.getDefaultEmpresa();
			
			int cdEmpresa = empresa.getCdEmpresa();
			
			int cdGrupoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa);
			if(cdGrupoCombustivel == 0)
				return new Result(-1, "Cliente nï¿½o usa combustï¿½veis");

			System.out.println("Iniciando correï¿½ï¿½o");
			
			String grupoCombustivel = GrupoServices.getAllCombustivel(cdEmpresa, connect);
			ArrayList<Integer> codigosGrupoCombustivel = GrupoServices.getAllCombustivelAsArray(cdEmpresa, connect);
			ArrayList<Integer> codigosTanque = TanqueServices.getAllAsArray();
			System.out.println("0 - codigosGrupoCombustivel: " + codigosGrupoCombustivel);
			System.out.println("0 - codigosTanque: " + codigosTanque);
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT A.* FROM alm_entrada_local_item A " +
					 													"	JOIN alm_documento_entrada_item B ON (A.cd_produto_servico = B.cd_produto_servico"
					 													+ "									  AND A.cd_documento_entrada = B.cd_documento_entrada"
					 													+ "									  AND A.cd_empresa = B.cd_empresa"
					 													+ "									  AND A.cd_item = B.cd_item) "
					 													+ " JOIN alm_documento_entrada C ON (B.cd_documento_entrada = C.cd_documento_entrada)"
					 													+ " JOIN alm_produto_grupo D ON (B.cd_produto_servico = D.cd_produto_servico"
					 													+ "							 AND B.cd_empresa = D.cd_empresa "
					 													+ "							 AND D.lg_principal = 1) "
					 													+ " JOIN alm_grupo E ON (D.cd_grupo = E.cd_grupo)"
					 													+ " WHERE C.dt_documento_entrada >= '2014-10-01 00:00:00' "
					 													+ "   AND C.dt_documento_entrada <= '2014-10-31 23:59:59' "
					 													+ "	  AND E.cd_grupo IN " + grupoCombustivel).executeQuery());
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM pcb_tanque WHERE cd_produto_servico = ?");
			System.out.println("1 - rsm: " + rsm);
			while(rsm.next()){
				pstmt.setInt(1, rsm.getInt("cd_produto_servico"));
				System.out.println("2 - pstmt: " + pstmt);
				ResultSetMap rsmTanque = new ResultSetMap(pstmt.executeQuery());
				System.out.println("3 - rsmTanque: " + rsmTanque);
				if(rsmTanque.next()){
					EntradaLocalItem entradaLocalItem = EntradaLocalItemDAO.get(rsm.getInt("cd_produto_servico"), rsm.getInt("cd_documento_entrada"), rsm.getInt("cd_empresa"), rsm.getInt("cd_local_armazenamento"), rsm.getInt("cd_entrada_local_item"), rsm.getInt("cd_item"), connect);
					System.out.println("4 - entradaLocalItem: " + entradaLocalItem);
					if(entradaLocalItem != null){
						System.out.println("5 - codigosTanque.contains(entradaLocalItem.getCdLocalArmazenamento()): " + codigosTanque.contains(entradaLocalItem.getCdLocalArmazenamento()));
						if(codigosTanque.contains(entradaLocalItem.getCdLocalArmazenamento())){
							continue;
						}
						int cdLocalArmazenamentoOld = entradaLocalItem.getCdLocalArmazenamento();
						entradaLocalItem.setCdLocalArmazenamento(rsmTanque.getInt("cd_tanque"));
						System.out.println("6 - entradaLocalItem: " + entradaLocalItem);
						if(EntradaLocalItemDAO.update(entradaLocalItem, 0, 0, 0, cdLocalArmazenamentoOld, 0, 0, connect) < 0){
							Conexao.rollback(connect);
							return new Result(-1, "Erro ao atualizar entrada local item");
						}
						System.out.println("7 - Atualizaï¿½ï¿½o realizada");
					}
				}
			}
						
			System.out.println("Finalizando correï¿½ï¿½o");			
			
			connect.commit();
			
			return new Result(1);
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result corrigirInformacoesFisco(){
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = true;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			ResultSetMap rsmNotaFiscal = new ResultSetMap(connect.prepareStatement("SELECT * FROM fsc_nota_fiscal WHERE txt_informacao_fisco IS NULL OR txt_informacao_fisco = ''").executeQuery());
			while(rsmNotaFiscal.next()){
				NotaFiscal notaFiscal = NotaFiscalDAO.get(rsmNotaFiscal.getInt("cd_nota_fiscal"), connect);
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_nota_fiscal", rsmNotaFiscal.getString("cd_nota_fiscal"), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmNotaFiscalDoc = NotaFiscalDocVinculadoDAO.find(criterios, connect);
				if(rsmNotaFiscalDoc.next()){
					if(rsmNotaFiscalDoc.getInt("cd_documento_saida") > 0){
						notaFiscal.setTxtInformacaoFisco(SaidaItemAliquotaServices.getAliquotaOfDocSaida(rsmNotaFiscalDoc.getInt("cd_documento_saida"), notaFiscal.getCdEmpresa(), notaFiscal.getVlTotalNota()));
						if(NotaFiscalDAO.update(notaFiscal, connect) <= 0){
							Conexao.rollback(connect);
							return new Result(-1);
						}
					}
					else if(rsmNotaFiscalDoc.getInt("cd_documento_entrada") > 0){
						notaFiscal.setTxtInformacaoFisco("SEM INFORMAï¿½ï¿½O DE FISCO");
						if(NotaFiscalDAO.update(notaFiscal, connect) <= 0){
							Conexao.rollback(connect);
							return new Result(-1);
						}
					}
				}
				
			}
			
			
			connect.commit();
			
			return new Result(1);
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result corrigirInformacoesClassificacaoFiscal(){
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = true;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			ResultSetMap rsmProdutos = new ResultSetMap(connect.prepareStatement("SELECT A.cd_produto_servico FROM grl_produto_servico A, grl_produto_servico_empresa B WHERE A.cd_produto_servico = B.cd_produto_servico  AND (tp_origem = 2 OR tp_origem = 1) AND cd_classificacao_fiscal = 1").executeQuery());
			while(rsmProdutos.next()){
				ProdutoServico produtoServico = ProdutoServicoDAO.get(rsmProdutos.getInt("cd_produto_servico"), connect);
				produtoServico.setCdClassificacaoFiscal(5);
				if(ProdutoServicoDAO.update(produtoServico, connect) <= 0){
					Conexao.rollback(connect);
					return new Result(-1);
				}
				
			}
			
			rsmProdutos = new ResultSetMap(connect.prepareStatement("SELECT A.cd_produto_servico FROM grl_produto_servico A, grl_produto_servico_empresa B WHERE A.cd_produto_servico = B.cd_produto_servico  AND (tp_origem = 2 OR tp_origem = 1) AND cd_classificacao_fiscal = 3").executeQuery());
			while(rsmProdutos.next()){
				ProdutoServico produtoServico = ProdutoServicoDAO.get(rsmProdutos.getInt("cd_produto_servico"), connect);
				produtoServico.setCdClassificacaoFiscal(6);
				if(ProdutoServicoDAO.update(produtoServico, connect) <= 0){
					Conexao.rollback(connect);
					return new Result(-1);
				}
				
			}
			
			connect.commit();
			
			return new Result(1, "correto");
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result initFormularioDinamicoInstituicao(){
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = true;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdSecretaria, connect);
			int cdPeriodoLetivoAtual = 0;
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			ResultSetMap rsmInstituicoes = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_instituicao A, acd_instituicao_educacenso B, acd_instituicao_periodo C WHERE A.cd_instituicao = B.cd_instituicao AND A.cd_instituicao = C.cd_instituicao AND C.cd_periodo_letivo_superior = "+cdPeriodoLetivoAtual+" AND C.cd_periodo_letivo = B.cd_periodo_letivo AND B.st_instituicao_publica = "+InstituicaoServices.ST_INSTITUICAO_EM_ATIVIDADE).executeQuery());
			while(rsmInstituicoes.next()){
				
				Instituicao instituicao = InstituicaoDAO.get(rsmInstituicoes.getInt("CD_INSTITUICAO"), connect);
				
				if(instituicao.getCdFormulario() <= 0){
					Formulario formulario = new Formulario(0/*cdFormulario*/, "Infraestrutura", "001", Util.getDataAtual(), 0/*stFormulario*/, null/*dsFormulario*/, null/*nmLinkFormulario*/, Util.getDataAtual(), null/*dtFimFormulario*/);
					int cdFormulario = FormularioDAO.insert(formulario, connect);
					if(cdFormulario <= 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao gravar formulï¿½rio dinï¿½mico!");
					}
					
					formulario.setCdFormulario(cdFormulario);
					instituicao.setCdFormulario(cdFormulario);
					if(InstituicaoDAO.update(instituicao, connect) < 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar instituiï¿½ï¿½o com formulï¿½rio dinï¿½mico!");
					}
					
					
				}
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_formulario", "" + instituicao.getCdFormulario(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("sg_atributo", "EPI", ItemComparator.EQUAL, Types.VARCHAR));
				ResultSetMap rsmFormularioAtributo = FormularioAtributoDAO.find(criterios, connect);
				if(!rsmFormularioAtributo.next()){
					FormularioAtributo formularioAtributo = new FormularioAtributo(0/*cdFormularioAtributo*/, instituicao.getCdFormulario(), "Possui Internet", "EPI", 0/*lgObrigatorio*/, FormularioAtributoServices.TP_BOOLEAN, 0/*nrCasasDecimais*/, 0/*nrOrdem*/, 0/*vlMaximo*/, 0/*vlMinimo*/, null/*txtFormula*/, "lgPossuiInternet", 0/*tpRestricaoPessoa*/, 0/*cdVinculo*/, 0/*cdUnidadeMedida*/);
					int cdFormularioAtributo = FormularioAtributoDAO.insert(formularioAtributo, connect);
					if(cdFormularioAtributo <= 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao gravar atributo do formulï¿½rio dinï¿½mico!");
					}
				}
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_formulario", "" + instituicao.getCdFormulario(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("sg_atributo", "ETC", ItemComparator.EQUAL, Types.VARCHAR));
				rsmFormularioAtributo = FormularioAtributoDAO.find(criterios, connect);
				if(!rsmFormularioAtributo.next()){
					FormularioAtributo formularioAtributo = new FormularioAtributo(0/*cdFormularioAtributo*/, instituicao.getCdFormulario(), "Tipo de Conexï¿½o", "ETC", 0/*lgObrigatorio*/, FormularioAtributoServices.TP_OPCOES, 0/*nrCasasDecimais*/, 0/*nrOrdem*/, 0/*vlMaximo*/, 0/*vlMinimo*/, null/*txtFormula*/, "tpConexao", 0/*tpRestricaoPessoa*/, 0/*cdVinculo*/, 0/*cdUnidadeMedida*/);
					int cdFormularioAtributo = FormularioAtributoDAO.insert(formularioAtributo, connect);
					if(cdFormularioAtributo <= 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao gravar atributo do formulï¿½rio dinï¿½mico!");
					}
					
					formularioAtributo.setCdFormularioAtributo(cdFormularioAtributo);
					
					FormularioAtributoOpcao formularioAtributoOpcao = new FormularioAtributoOpcao(0/*cdOpcao*/, cdFormularioAtributo, "Velox", InstituicaoServices.TP_OPCAO_CONEXAO_VELOX, "001", 1);
					if(FormularioAtributoOpcaoDAO.insert(formularioAtributoOpcao, connect) <= 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao gravar a opï¿½ï¿½o do atributo do formulï¿½rio dinï¿½mico!");
					}
					
					formularioAtributoOpcao = new FormularioAtributoOpcao(0/*cdOpcao*/, cdFormularioAtributo, "Claro", InstituicaoServices.TP_OPCAO_CONEXAO_CLARO, "002", 2);
					if(FormularioAtributoOpcaoDAO.insert(formularioAtributoOpcao, connect) <= 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao gravar a opï¿½ï¿½o do atributo do formulï¿½rio dinï¿½mico!");
					}
					
					formularioAtributoOpcao = new FormularioAtributoOpcao(0/*cdOpcao*/, cdFormularioAtributo, "Fibra", InstituicaoServices.TP_OPCAO_CONEXAO_FIBRA, "003", 3);
					if(FormularioAtributoOpcaoDAO.insert(formularioAtributoOpcao, connect) <= 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao gravar a opï¿½ï¿½o do atributo do formulï¿½rio dinï¿½mico!");
					}
				}

				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_formulario", "" + instituicao.getCdFormulario(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("sg_atributo", "EIF", ItemComparator.EQUAL, Types.VARCHAR));
				rsmFormularioAtributo = FormularioAtributoDAO.find(criterios, connect);
				if(!rsmFormularioAtributo.next()){
					FormularioAtributo formularioAtributo = new FormularioAtributo(0/*cdFormularioAtributo*/, instituicao.getCdFormulario(), "Internet funcionando", "EIF", 0/*lgObrigatorio*/, FormularioAtributoServices.TP_BOOLEAN, 0/*nrCasasDecimais*/, 0/*nrOrdem*/, 0/*vlMaximo*/, 0/*vlMinimo*/, null/*txtFormula*/, "lgInternetFuncionando", 0/*tpRestricaoPessoa*/, 0/*cdVinculo*/, 0/*cdUnidadeMedida*/);
					int cdFormularioAtributo = FormularioAtributoDAO.insert(formularioAtributo, connect);
					if(cdFormularioAtributo <= 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao gravar atributo do formulï¿½rio dinï¿½mico!");
					}
				}
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_formulario", "" + instituicao.getCdFormulario(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("sg_atributo", "ECN", ItemComparator.EQUAL, Types.VARCHAR));
				rsmFormularioAtributo = FormularioAtributoDAO.find(criterios, connect);
				if(!rsmFormularioAtributo.next()){
					FormularioAtributo formularioAtributo = new FormularioAtributo(0/*cdFormularioAtributo*/, instituicao.getCdFormulario(), "Computador novo", "ECN", 0/*lgObrigatorio*/, FormularioAtributoServices.TP_BOOLEAN, 0/*nrCasasDecimais*/, 0/*nrOrdem*/, 0/*vlMaximo*/, 0/*vlMinimo*/, null/*txtFormula*/, "lgComputadorNovo", 0/*tpRestricaoPessoa*/, 0/*cdVinculo*/, 0/*cdUnidadeMedida*/);
					int cdFormularioAtributo = FormularioAtributoDAO.insert(formularioAtributo, connect);
					if(cdFormularioAtributo <= 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao gravar atributo do formulï¿½rio dinï¿½mico!");
					}
				}
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_formulario", "" + instituicao.getCdFormulario(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("sg_atributo", "EDRC", ItemComparator.EQUAL, Types.VARCHAR));
				rsmFormularioAtributo = FormularioAtributoDAO.find(criterios, connect);
				if(!rsmFormularioAtributo.next()){
					FormularioAtributo formularioAtributo = new FormularioAtributo(0/*cdFormularioAtributo*/, instituicao.getCdFormulario(), "Data de Recebimento", "EDRC", 0/*lgObrigatorio*/, FormularioAtributoServices.TP_DATA, 0/*nrCasasDecimais*/, 0/*nrOrdem*/, 0/*vlMaximo*/, 0/*vlMinimo*/, null/*txtFormula*/, "lgDataRecebimentoComputadorNovo", 0/*tpRestricaoPessoa*/, 0/*cdVinculo*/, 0/*cdUnidadeMedida*/);
					int cdFormularioAtributo = FormularioAtributoDAO.insert(formularioAtributo, connect);
					if(cdFormularioAtributo <= 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao gravar atributo do formulï¿½rio dinï¿½mico!");
					}
				}
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_formulario", "" + instituicao.getCdFormulario(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("sg_atributo", "ECCE", ItemComparator.EQUAL, Types.VARCHAR));
				rsmFormularioAtributo = FormularioAtributoDAO.find(criterios, connect);
				if(!rsmFormularioAtributo.next()){
					FormularioAtributo formularioAtributo = new FormularioAtributo(0/*cdFormularioAtributo*/, instituicao.getCdFormulario(), "Computador com estabilizador", "ECCE", 0/*lgObrigatorio*/, FormularioAtributoServices.TP_BOOLEAN, 0/*nrCasasDecimais*/, 0/*nrOrdem*/, 0/*vlMaximo*/, 0/*vlMinimo*/, null/*txtFormula*/, "lgEstabilizadorComputadorNovo", 0/*tpRestricaoPessoa*/, 0/*cdVinculo*/, 0/*cdUnidadeMedida*/);
					int cdFormularioAtributo = FormularioAtributoDAO.insert(formularioAtributo, connect);
					if(cdFormularioAtributo <= 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao gravar atributo do formulï¿½rio dinï¿½mico!");
					}
				}
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_formulario", "" + instituicao.getCdFormulario(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("sg_atributo", "ECNTC", ItemComparator.EQUAL, Types.VARCHAR));
				rsmFormularioAtributo = FormularioAtributoDAO.find(criterios, connect);
				if(!rsmFormularioAtributo.next()){
					FormularioAtributo formularioAtributo = new FormularioAtributo(0/*cdFormularioAtributo*/, instituicao.getCdFormulario(), "Nï¿½mero de tombo do Computador", "ECNTC", 0/*lgObrigatorio*/, FormularioAtributoServices.TP_STRING, 0/*nrCasasDecimais*/, 0/*nrOrdem*/, 0/*vlMaximo*/, 0/*vlMinimo*/, null/*txtFormula*/, "lgNumeroTomboComputadorNovo", 0/*tpRestricaoPessoa*/, 0/*cdVinculo*/, 0/*cdUnidadeMedida*/);
					int cdFormularioAtributo = FormularioAtributoDAO.insert(formularioAtributo, connect);
					if(cdFormularioAtributo <= 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao gravar atributo do formulï¿½rio dinï¿½mico!");
					}
				}
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_formulario", "" + instituicao.getCdFormulario(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("sg_atributo", "ECNTM", ItemComparator.EQUAL, Types.VARCHAR));
				rsmFormularioAtributo = FormularioAtributoDAO.find(criterios, connect);
				if(!rsmFormularioAtributo.next()){
					FormularioAtributo formularioAtributo = new FormularioAtributo(0/*cdFormularioAtributo*/, instituicao.getCdFormulario(), "Nï¿½mero de tombo do Monitor", "ECNTM", 0/*lgObrigatorio*/, FormularioAtributoServices.TP_STRING, 0/*nrCasasDecimais*/, 0/*nrOrdem*/, 0/*vlMaximo*/, 0/*vlMinimo*/, null/*txtFormula*/, "lgNumeroTomboMonitorNovo", 0/*tpRestricaoPessoa*/, 0/*cdVinculo*/, 0/*cdUnidadeMedida*/);
					int cdFormularioAtributo = FormularioAtributoDAO.insert(formularioAtributo, connect);
					if(cdFormularioAtributo <= 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao gravar atributo do formulï¿½rio dinï¿½mico!");
					}
				}
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_formulario", "" + instituicao.getCdFormulario(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("sg_atributo", "EDUV", ItemComparator.EQUAL, Types.VARCHAR));
				rsmFormularioAtributo = FormularioAtributoDAO.find(criterios, connect);
				if(!rsmFormularioAtributo.next()){
					FormularioAtributo formularioAtributo = new FormularioAtributo(0/*cdFormularioAtributo*/, instituicao.getCdFormulario(), "Data da ï¿½ltima visita", "EDUV", 0/*lgObrigatorio*/, FormularioAtributoServices.TP_DATA, 0/*nrCasasDecimais*/, 0/*nrOrdem*/, 0/*vlMaximo*/, 0/*vlMinimo*/, null/*txtFormula*/, "lgDataUltimaVisita", 0/*tpRestricaoPessoa*/, 0/*cdVinculo*/, 0/*cdUnidadeMedida*/);
					int cdFormularioAtributo = FormularioAtributoDAO.insert(formularioAtributo, connect);
					if(cdFormularioAtributo <= 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao gravar atributo do formulï¿½rio dinï¿½mico!");
					}
				}
				
			}
			
			connect.commit();
			
			return new Result(1, "correto");
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result correcaoDuplicidadeAlunos(){
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = true;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			System.out.println("1");
			ResultSetMap rsmAlunosComEspacos = new ResultSetMap(connect.prepareStatement("SELECT cd_pessoa, nm_pessoa FROM grl_pessoa A, acd_aluno B WHERE A.cd_pessoa = B.cd_aluno").executeQuery());
			while(rsmAlunosComEspacos.next()){
				String nmAlunoArray [] = rsmAlunosComEspacos.getString("nm_pessoa").split(" ");
				String nmAlunoSql = "";
				for(String parteNome : nmAlunoArray){
					if(!parteNome.trim().equals(""))
						nmAlunoSql += parteNome.trim().replaceAll("'", "").replaceAll("\\.", "").replaceAll("-", "").replaceAll(";", "").replaceAll(":", "").replaceAll(",", "") + " ";
				}
				nmAlunoSql = nmAlunoSql.trim();
				connect.prepareStatement("UPDATE grl_pessoa SET nm_pessoa_temp = '"+nmAlunoSql+"' WHERE cd_pessoa = " + rsmAlunosComEspacos.getInt("cd_pessoa")).executeUpdate();
				connect.prepareStatement("UPDATE grl_pessoa SET nm_pessoa = TRANSLATE('"+nmAlunoSql+"', 'ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') WHERE cd_pessoa = " + rsmAlunosComEspacos.getInt("cd_pessoa")).executeUpdate();
			}
			
			ResultSetMap rsmMaeComEspacos = new ResultSetMap(connect.prepareStatement("SELECT cd_pessoa, nm_mae FROM grl_pessoa_fisica A, acd_aluno B WHERE A.cd_pessoa = B.cd_aluno AND nm_mae IS NOT NULL AND nm_mae <> ''").executeQuery());
			while(rsmMaeComEspacos.next()){
				String nmMaeArray [] = rsmMaeComEspacos.getString("nm_mae").split(" ");
				String nmMaeSql = "";
				for(String parteNome : nmMaeArray){
					if(!parteNome.trim().equals(""))
						nmMaeSql += parteNome.trim().replaceAll("'", "").replaceAll("\\.", "").replaceAll("-", "").replaceAll(";", "").replaceAll(":", "").replaceAll(",", "") + " ";
				}
				
				nmMaeSql = nmMaeSql.trim();
				
				connect.prepareStatement("UPDATE grl_pessoa_fisica SET nm_mae_temp = '"+nmMaeSql+"' WHERE cd_pessoa = " + rsmMaeComEspacos.getInt("cd_pessoa")).executeUpdate();
				connect.prepareStatement("UPDATE grl_pessoa_fisica SET nm_mae = TRANSLATE('"+nmMaeSql+"', 'ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') WHERE cd_pessoa = " + rsmMaeComEspacos.getInt("cd_pessoa")).executeUpdate();
			}
			System.out.println("2");
			ResultSetMap rsmPaiComEspacos = new ResultSetMap(connect.prepareStatement("SELECT cd_pessoa, nm_pai FROM grl_pessoa_fisica A, acd_aluno B WHERE A.cd_pessoa = B.cd_aluno AND nm_pai IS NOT NULL AND nm_pai <> ''").executeQuery());
			while(rsmPaiComEspacos.next()){
				String nmPaiArray [] = rsmPaiComEspacos.getString("nm_pai").split(" ");
				String nmPaiSql = "";
				for(String parteNome : nmPaiArray){
					if(!parteNome.trim().equals(""))
						nmPaiSql += parteNome.trim().replaceAll("'", "").replaceAll("\\.", "").replaceAll("-", "").replaceAll(";", "").replaceAll(":", "").replaceAll(",", "") + " ";
				}
				
				nmPaiSql = nmPaiSql.trim();
				
				connect.prepareStatement("UPDATE grl_pessoa_fisica SET nm_pai_temp = '"+nmPaiSql+"' WHERE cd_pessoa = " + rsmPaiComEspacos.getInt("cd_pessoa")).executeUpdate();
				connect.prepareStatement("UPDATE grl_pessoa_fisica SET nm_pai = TRANSLATE('"+nmPaiSql+"', 'ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') WHERE cd_pessoa = " + rsmPaiComEspacos.getInt("cd_pessoa")).executeUpdate();
			}
			System.out.println("3");
			ResultSetMap rsmDuplicidadesDiretas = new ResultSetMap(connect.prepareStatement("select B.nm_pessoa, C.dt_nascimento, C.nm_mae from acd_aluno A, grl_pessoa B, grl_pessoa_fisica C WHERE A.cd_Aluno = B.cd_pessoa AND A.cd_aluno = C.cd_pessoa  GROUP BY B.nm_pessoa, C.dt_nascimento, C.nm_mae HAVING COUNT(B.nm_pessoa) > 1 AND COUNT(C.dt_nascimento) > 1 AND COUNT(C.nm_mae) > 1 ORDER BY B.nm_pessoa, C.dt_nascimento, C.nm_mae").executeQuery());
			while(rsmDuplicidadesDiretas.next()){
				System.out.println("4");
				int cdAlunoReferencia = 0;
				String codigosAlunosSubstituidos = "";
				
				String nmAlunoArray [] = rsmDuplicidadesDiretas.getString("nm_pessoa").split(" ");
				String nmAlunoSql = "";
				for(String parteNome : nmAlunoArray){
					nmAlunoSql += parteNome.trim() + " ";
				}
				
				nmAlunoSql = nmAlunoSql.trim();
				
				String nmMaeArray [] = rsmDuplicidadesDiretas.getString("nm_mae").split(" ");
				String nmMaeSql = "";
				for(String parteNome : nmMaeArray){
					nmMaeSql += parteNome.trim() + " ";
				}
				
				nmMaeSql = nmMaeSql.trim();
				
				ResultSetMap rsmRegistrosDuplicados = new ResultSetMap(connect.prepareStatement("SELECT B.cd_pessoa, B.nm_pessoa, C.dt_nascimento, C.nm_mae, D.* from acd_aluno A " + 
																								"JOIN grl_pessoa B ON (A.cd_aluno = B.cd_pessoa) " + 
																								"JOIN grl_pessoa_fisica C ON (A.cd_aluno = C.cd_pessoa) " + 
																								"LEFT OUTER JOIN acd_matricula D ON (A.cd_aluno = D.cd_aluno)  " +
																								"WHERE B.nm_pessoa = '"+nmAlunoSql+"'" + 
																								"  AND C.dt_nascimento = '"+Util.convCalendarStringSqlCompleto(rsmDuplicidadesDiretas.getGregorianCalendar("dt_nascimento"))+"'"+ 
																								"  AND C.nm_mae = '"+nmMaeSql+"' ORDER BY B.nm_pessoa, C.dt_nascimento, C.nm_mae, D.dt_matricula DESC ").executeQuery());
				int i = 0;
				int j = 0;
				boolean possuiSemMatricula = false;
				ArrayList<Integer> codigosAluno = new ArrayList<Integer>();
				if(rsmRegistrosDuplicados.size() > 1){
					while(rsmRegistrosDuplicados.next()){
						
						if(codigosAluno.contains(rsmRegistrosDuplicados.getInt("cd_pessoa")))
							continue;
						
						if(i == 0 && rsmRegistrosDuplicados.getInt("cd_matricula") != 0){
							cdAlunoReferencia = rsmRegistrosDuplicados.getInt("cd_pessoa");
							i++;
						}
						else if(i > 0 && rsmRegistrosDuplicados.getInt("cd_matricula") != 0){
							if(i == 1 && !possuiSemMatricula){
								codigosAlunosSubstituidos += rsmRegistrosDuplicados.getInt("cd_pessoa") + "";
							}
							else{
								codigosAlunosSubstituidos += ", " + rsmRegistrosDuplicados.getInt("cd_pessoa");
							}
							i++;
						}
						else if(rsmRegistrosDuplicados.getInt("cd_matricula") == 0){
							if(j == 0)
								codigosAlunosSubstituidos += rsmRegistrosDuplicados.getInt("cd_pessoa") + "";
							else
								codigosAlunosSubstituidos += ", " + rsmRegistrosDuplicados.getInt("cd_pessoa") + "";
							possuiSemMatricula = true;
							j++;
						}
							
						codigosAluno.add(rsmRegistrosDuplicados.getInt("cd_pessoa"));
					}
					
					connect.prepareStatement("update acd_matricula set cd_aluno = "+cdAlunoReferencia+" WHERE cd_aluno IN ("+codigosAlunosSubstituidos+"); " +
											"update grl_ocorrencia set cd_pessoa = "+cdAlunoReferencia+" WHERE cd_pessoa IN ("+codigosAlunosSubstituidos+"); " +
											"update acd_aluno_recurso_prova set cd_aluno = "+cdAlunoReferencia+" WHERE cd_aluno IN ("+codigosAlunosSubstituidos+"); " +
											"update acd_aluno set cd_responsavel = "+cdAlunoReferencia+" WHERE cd_responsavel IN ("+codigosAlunosSubstituidos+"); " +
											"delete from acd_aluno_tipo_transporte where cd_aluno IN ("+codigosAlunosSubstituidos+"); " +
											"delete from grl_pessoa_alergia where cd_pessoa IN ("+codigosAlunosSubstituidos+"); " +
											"delete from grl_pessoa_doenca where cd_pessoa IN ("+codigosAlunosSubstituidos+"); " +
											"delete from grl_pessoa_necessidade_especial where cd_pessoa IN ("+codigosAlunosSubstituidos+"); " +
											"delete from grl_pessoa_tipo_documentacao where cd_pessoa IN ("+codigosAlunosSubstituidos+"); " +
											"delete from grl_pessoa_ficha_medica where cd_pessoa IN ("+codigosAlunosSubstituidos+"); " +
											"delete from acd_aluno where cd_aluno IN ("+codigosAlunosSubstituidos+"); " +
											"delete from grl_pessoa_fisica where cd_pessoa IN ("+codigosAlunosSubstituidos+"); " +
											"delete from grl_pessoa_endereco where cd_pessoa IN ("+codigosAlunosSubstituidos+"); " +
											"delete from grl_pessoa_empresa where cd_pessoa IN ("+codigosAlunosSubstituidos+"); " +
											"delete from grl_pessoa where cd_pessoa IN ("+codigosAlunosSubstituidos+");").executeUpdate();
				}
				
			}	
			System.out.println("5");
			rsmDuplicidadesDiretas = new ResultSetMap(connect.prepareStatement("select C.nr_documento from acd_aluno A, grl_pessoa B, grl_pessoa_tipo_documentacao C WHERE A.cd_aluno = B.cd_pessoa AND A.cd_aluno = C.cd_pessoa and nr_documento IS NOT NULL and nr_documento <> '' and length(nr_documento) = 32 group by C.nr_documento having count(C.nr_documento) > 1 ORDER BY nr_documento").executeQuery());
			while(rsmDuplicidadesDiretas.next()){
				System.out.println("6");
				int cdAlunoReferencia = 0;
				String codigosAlunosSubstituidos = "";
				ResultSetMap rsmRegistrosDuplicados = new ResultSetMap(connect.prepareStatement("SELECT B.cd_pessoa, B.nm_pessoa, C.nr_documento, D.* from acd_aluno A " + 
																								"JOIN grl_pessoa B ON (A.cd_aluno = B.cd_pessoa) " + 
																								"JOIN grl_pessoa_tipo_documentacao C ON (A.cd_aluno = C.cd_pessoa) " + 
																								"LEFT OUTER JOIN acd_matricula D ON (A.cd_aluno = D.cd_aluno)  " +
																								"WHERE C.nr_documento = '"+rsmDuplicidadesDiretas.getString("nr_documento")+"' ORDER BY nr_documento, D.dt_matricula DESC ").executeQuery());
				int i = 0;
				int j = 0;
				boolean possuiSemMatricula = false;
				ArrayList<Integer> codigosAluno = new ArrayList<Integer>();
				if(rsmRegistrosDuplicados.size() > 1){
					while(rsmRegistrosDuplicados.next()){
						
						if(codigosAluno.contains(rsmRegistrosDuplicados.getInt("cd_pessoa")))
							continue;
						
						if(i == 0 && rsmRegistrosDuplicados.getInt("cd_matricula") != 0){
							cdAlunoReferencia = rsmRegistrosDuplicados.getInt("cd_pessoa");
							i++;
						}
						else if(i > 0 && rsmRegistrosDuplicados.getInt("cd_matricula") != 0){
							if(i == 1 && !possuiSemMatricula){
								codigosAlunosSubstituidos += rsmRegistrosDuplicados.getInt("cd_pessoa") + "";
							}
							else{
								codigosAlunosSubstituidos += ", " + rsmRegistrosDuplicados.getInt("cd_pessoa");
							}
							i++;
						}
						else if(rsmRegistrosDuplicados.getInt("cd_matricula") == 0){
							if(j == 0)
								codigosAlunosSubstituidos += rsmRegistrosDuplicados.getInt("cd_pessoa") + "";
							else
								codigosAlunosSubstituidos += ", " + rsmRegistrosDuplicados.getInt("cd_pessoa") + "";
							possuiSemMatricula = true;
							j++;
						}
							
						codigosAluno.add(rsmRegistrosDuplicados.getInt("cd_pessoa"));
					}
					
					connect.prepareStatement("update acd_matricula set cd_aluno = "+cdAlunoReferencia+" WHERE cd_aluno IN ("+codigosAlunosSubstituidos+"); " +
											"update grl_ocorrencia set cd_pessoa = "+cdAlunoReferencia+" WHERE cd_pessoa IN ("+codigosAlunosSubstituidos+"); " +
											"update acd_aluno_recurso_prova set cd_aluno = "+cdAlunoReferencia+" WHERE cd_aluno IN ("+codigosAlunosSubstituidos+"); " +
											"update acd_aluno set cd_responsavel = "+cdAlunoReferencia+" WHERE cd_responsavel IN ("+codigosAlunosSubstituidos+"); " +
											"delete from acd_aluno_tipo_transporte where cd_aluno IN ("+codigosAlunosSubstituidos+"); " +
											"delete from grl_pessoa_alergia where cd_pessoa IN ("+codigosAlunosSubstituidos+"); " +
											"delete from grl_pessoa_doenca where cd_pessoa IN ("+codigosAlunosSubstituidos+"); " +
											"delete from grl_pessoa_necessidade_especial where cd_pessoa IN ("+codigosAlunosSubstituidos+"); " +
											"delete from grl_pessoa_tipo_documentacao where cd_pessoa IN ("+codigosAlunosSubstituidos+"); " +
											"delete from grl_pessoa_ficha_medica where cd_pessoa IN ("+codigosAlunosSubstituidos+"); " +
											"delete from acd_aluno where cd_aluno IN ("+codigosAlunosSubstituidos+"); " +
											"delete from grl_pessoa_fisica where cd_pessoa IN ("+codigosAlunosSubstituidos+"); " +
											"delete from grl_pessoa_endereco where cd_pessoa IN ("+codigosAlunosSubstituidos+"); " +
											"delete from grl_pessoa_empresa where cd_pessoa IN ("+codigosAlunosSubstituidos+"); " +
											"delete from grl_pessoa where cd_pessoa IN ("+codigosAlunosSubstituidos+");").executeUpdate();
				}
				
			}	
			System.out.println("7");
			connect.commit();
			
			return new Result(1, "correto");
		}
		catch (SQLException e) {
			System.out.println("e 2 message " + e.getMessage());
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		catch (Exception e) {
			System.out.println("e message " + e.getMessage());
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result identificarAlunosComProblemasDeCadastro(){
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = true;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdSecretaria, connect);
			int cdPeriodoAtual = 0;
			if(rsmPeriodoAtual.next())
				cdPeriodoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			
			ResultSetMap rsmAlunos = new ResultSetMap(connect.prepareStatement("SELECT A.cd_aluno from acd_aluno A, grl_pessoa B, grl_pessoa_fisica C "
					+ "															  WHERE A.cd_Aluno = B.cd_pessoa "
					+ "															    AND A.cd_aluno = C.cd_pessoa "
					+ "																AND EXISTS (SELECT * FROM acd_matricula AA, acd_instituicao_periodo BB "
					+ "																				WHERE AA.cd_aluno = A.cd_aluno "
					+ "																				  AND AA.cd_periodo_letivo = BB.cd_periodo_letivo "
					+ "																				  AND BB.cd_periodo_letivo_superior = "+cdPeriodoAtual
					+ "																				  AND AA.st_matricula = "+MatriculaServices.ST_ATIVA+")").executeQuery());
			System.out.println("Quantidade de alunos = " + rsmAlunos.size());
			while(rsmAlunos.next()){
				Aluno aluno = AlunoDAO.get(rsmAlunos.getInt("cd_aluno"), connect);
				System.out.println("aluno = " + aluno.getNmPessoa());
				if(aluno.getTpSexo() != 0 && aluno.getTpSexo() != 1){
					System.out.println("1");
					aluno.setLgCadastroProblema(1);
					if(AlunoDAO.update(aluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na identificacao");
					}
					continue;
				}
				
				if(aluno.getDtNascimento() == null){
					System.out.println("2");
					aluno.setLgCadastroProblema(1);
					if(AlunoDAO.update(aluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na identificacao");
					}
					continue;
				}
				
				if(aluno.getCdNaturalidade() == 0){
					System.out.println("3");
					aluno.setLgCadastroProblema(1);
					if(AlunoDAO.update(aluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na identificacao");
					}
					continue;
				}
				
				if(aluno.getTpNacionalidade() < 0 || aluno.getTpNacionalidade() > 2){
					System.out.println("4");
					aluno.setLgCadastroProblema(1);
					if(AlunoDAO.update(aluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na identificacao");
					}
					continue;
				}
//				
//				if((aluno.getNrTelefone1() == null || aluno.getNrTelefone1().equals("")) && (aluno.getNrTelefone2() == null || aluno.getNrTelefone2().equals("")) && (aluno.getNrCelular() == null || aluno.getNrCelular().equals(""))){
//					System.out.println("5");
//					aluno.setLgCadastroProblema(1);
//					if(AlunoDAO.update(aluno, connect) < 0){
//						if(isConnectionNull)
//							Conexao.rollback(connect);
//						return new Result(-1, "Erro na identificacao");
//					}
//					continue;
//				}
//				
//				if(aluno.getNmEmail() == null || aluno.getNmEmail().equals("")){
//					System.out.println("6");
//					aluno.setLgCadastroProblema(1);
//					if(AlunoDAO.update(aluno, connect) < 0){
//						if(isConnectionNull)
//							Conexao.rollback(connect);
//						return new Result(-1, "Erro na identificacao");
//					}
//					continue;
//				}
				
				if(aluno.getTpRaca() < 0 || aluno.getTpRaca() > 5){
					System.out.println("7");
					aluno.setLgCadastroProblema(1);
					if(AlunoDAO.update(aluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na identificacao");
					}
					continue;
				}
				
				if(aluno.getNmMae() == null || aluno.getNmMae().trim().equals("")){
					System.out.println("8");
					aluno.setLgCadastroProblema(1);
					if(AlunoDAO.update(aluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na identificacao");
					}
					continue;
				}
				
				if(aluno.getNmPai() != null && !aluno.getNmPai().trim().equals("")){
					if(aluno.getNmPai().contains("XX")){
						System.out.println("9");
						aluno.setLgCadastroProblema(1);
						if(AlunoDAO.update(aluno, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro na identificacao");
						}
						continue;
					}
					
				}
				
				ResultSetMap rsmPessoaDocumentacao = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_pessoa_tipo_documentacao WHERE cd_pessoa = " + aluno.getCdAluno() + " AND cd_tipo_documentacao IN ("+ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0)+", "+ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMNENTO", 0)+")").executeQuery());
				boolean possuiCert = rsmPessoaDocumentacao.next();
				
				if((aluno.getNrRg() == null || aluno.getNrRg().equals("")) && (aluno.getNrCpf() == null || aluno.getNrCpf().equals("")) && !possuiCert){
					System.out.println("9.5");
					aluno.setLgCadastroProblema(1);
					if(AlunoDAO.update(aluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na identificacao");
					}
					continue;
				}
				
				if(aluno.getNrRg() != null && aluno.getNrRg().trim().length() < 5){
					System.out.println("10");
					aluno.setLgCadastroProblema(1);
					if(AlunoDAO.update(aluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na identificacao");
					}
					continue;
				}
				
				ResultSetMap rsmRgAluno = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_pessoa_fisica A WHERE A.cd_pessoa <> " + aluno.getCdAluno() + " AND A.nr_rg = '"+aluno.getNrRg()+"' AND A.cd_estado_rg = " + aluno.getCdEstadoRg()).executeQuery());
				if(rsmRgAluno.next()){
					System.out.println("11");
					aluno.setLgCadastroProblema(1);
					if(AlunoDAO.update(aluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na identificacao");
					}
					continue;
				}
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_pessoa", "" + aluno.getCdAluno(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("lg_principal", "" + 1, ItemComparator.EQUAL, Types.INTEGER));
				
				PessoaEndereco pessoaEndereco = null;
				ResultSetMap rsmEndereco = PessoaEnderecoDAO.find(criterios, connect);
				if(rsmEndereco.next()){
					pessoaEndereco = PessoaEnderecoDAO.get(rsmEndereco.getInt("cd_endereco"), aluno.getCdAluno(), connect);
				}
				
				if(pessoaEndereco == null || pessoaEndereco.getTpZona() == 0){
					System.out.println("12");
					aluno.setLgCadastroProblema(1);
					if(AlunoDAO.update(aluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na identificacao");
					}
					continue;
				}
				
				if(pessoaEndereco == null || pessoaEndereco.getNrCep() == null || pessoaEndereco.getNrCep().trim().equals("") || pessoaEndereco.getNrCep().trim().equals("45000000") ){
					System.out.println("13");
					aluno.setLgCadastroProblema(1);
					if(AlunoDAO.update(aluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na identificacao");
					}
					continue;
				}
				
				ResultSetMap rsmCep = LogradouroServices.getEnderecoByCep(pessoaEndereco.getNrCep(), connect);
				
				if(rsmCep == null || (rsmCep.size() == 0)){
					System.out.println("pessoaEndereco.getNrCep() = " + pessoaEndereco.getNrCep());
					System.out.println("13.5");
					aluno.setLgCadastroProblema(1);
					if(AlunoDAO.update(aluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na identificacao");
					}
					continue;
				}
				
				if(pessoaEndereco == null || pessoaEndereco.getNmLogradouro() == null || pessoaEndereco.getNmLogradouro().trim().equals("")){
					System.out.println("14");
					aluno.setLgCadastroProblema(1);
					if(AlunoDAO.update(aluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na identificacao");
					}
					continue;
				}
				
				if(pessoaEndereco == null || pessoaEndereco.getNmBairro() == null || pessoaEndereco.getNmBairro().trim().equals("")){
					System.out.println("15");
					aluno.setLgCadastroProblema(1);
					if(AlunoDAO.update(aluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na identificacao");
					}
					continue;
				}
				
				if(pessoaEndereco == null || pessoaEndereco.getNrEndereco() == null || pessoaEndereco.getNrEndereco().trim().equals("")){
					System.out.println("16");
					aluno.setLgCadastroProblema(1);
					if(AlunoDAO.update(aluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na identificacao");
					}
					continue;
				}
				
				if(pessoaEndereco == null || pessoaEndereco.getCdCidade() == 0){
					System.out.println("17");
					aluno.setLgCadastroProblema(1);
					if(AlunoDAO.update(aluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na identificacao");
					}
					continue;
				}
				else{
					Cidade cidade = CidadeDAO.get(pessoaEndereco.getCdCidade(), connect);
					if(cidade.getCdEstado() == 0){
						System.out.println("18");
						aluno.setLgCadastroProblema(1);
						if(AlunoDAO.update(aluno, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro na identificacao");
						}
						continue;
					}
					else{
						Estado estado = EstadoDAO.get(cidade.getCdEstado(), connect);
						if(estado.getCdPais() == 0){
							System.out.println("19");
							aluno.setLgCadastroProblema(1);
							if(AlunoDAO.update(aluno, connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro na identificacao");
							}
							continue;
						}
					}
				}
				
				
				if(aluno.getCdNaturalidade() == 0){
					System.out.println("20");
					aluno.setLgCadastroProblema(1);
					if(AlunoDAO.update(aluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na identificacao");
					}
					continue;
				}
				
				boolean entrou = false;
				String[] partesNome = aluno.getNmPessoa().split(" ");
				for(String parteNome : partesNome){
					if(parteNome.trim().equals("")){
						System.out.println("21");
						aluno.setLgCadastroProblema(1);
						if(AlunoDAO.update(aluno, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro na identificacao");
						}
						entrou = true;
						break;
					}
					if(parteNome.length() <= 1 && !parteNome.trim().equals("E")){
						System.out.println("22");
						aluno.setLgCadastroProblema(1);
						if(AlunoDAO.update(aluno, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro na identificacao");
						}
						entrou = true;
						break;
					}
					
					if(parteNome.contains(",") || parteNome.contains(".") || parteNome.contains(";") || parteNome.contains("-") || parteNome.contains(":") || parteNome.contains("'") || parteNome.contains("*") || parteNome.contains("+") || parteNome.contains("/") || parteNome.contains("!") || parteNome.contains("@") || parteNome.contains("#") || parteNome.contains("$") || parteNome.contains("&")
					|| parteNome.contains("0") || parteNome.contains("1") || parteNome.contains("2") || parteNome.contains("3") || parteNome.contains("4") || parteNome.contains("5") || parteNome.contains("6") || parteNome.contains("7") || parteNome.contains("8") || parteNome.contains("9")){
						System.out.println("23");
						aluno.setLgCadastroProblema(1);
						if(AlunoDAO.update(aluno, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro na identificacao");
						}
						entrou = true;
						break;
					}
				}
				
				if(entrou){
					continue;
				}
				
				entrou = false;
				
				partesNome = aluno.getNmMae().split(" ");
				for(String parteNome : partesNome){
					if(parteNome.trim().equals("")){
						System.out.println("24");
						aluno.setLgCadastroProblema(1);
						if(AlunoDAO.update(aluno, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro na identificacao");
						}
						entrou = true;
						break;
					}
					if(parteNome.length() <= 1 && !parteNome.trim().equals("E")){
						System.out.println("25");
						aluno.setLgCadastroProblema(1);
						if(AlunoDAO.update(aluno, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro na identificacao");
						}
						entrou = true;
						break;
					}
					if(parteNome.contains(",") || parteNome.contains(".") || parteNome.contains(";") || parteNome.contains("-") || parteNome.contains(":") || parteNome.contains("'") || parteNome.contains("*") || parteNome.contains("+") || parteNome.contains("/") || parteNome.contains("!") || parteNome.contains("@") || parteNome.contains("#") || parteNome.contains("$") || parteNome.contains("&")
					|| parteNome.contains("0") || parteNome.contains("1") || parteNome.contains("2") || parteNome.contains("3") || parteNome.contains("4") || parteNome.contains("5") || parteNome.contains("6") || parteNome.contains("7") || parteNome.contains("8") || parteNome.contains("9")){
						System.out.println("26");
						aluno.setLgCadastroProblema(1);
						if(AlunoDAO.update(aluno, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro na identificacao");
						}
						entrou = true;
						break;
					}
				}
				
				if(entrou){
					continue;
				}
				
				entrou = false;
				
				if(aluno.getNmPai() != null && !aluno.getNmPai().equals("")){
					partesNome = aluno.getNmPai().split(" ");
					for(String parteNome : partesNome){
						if(parteNome.trim().equals("")){
							System.out.println("27");
							aluno.setLgCadastroProblema(1);
							if(AlunoDAO.update(aluno, connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro na identificacao");
							}
							entrou = true;
							break;
						}
						if(parteNome.length() <= 1 && !parteNome.trim().equals("E")){
							System.out.println("28");
							aluno.setLgCadastroProblema(1);
							if(AlunoDAO.update(aluno, connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro na identificacao");
							}
							entrou = true;
							break;
						}
						
						if(parteNome.contains(",") || parteNome.contains(".") || parteNome.contains(";") || parteNome.contains("-") || parteNome.contains(":") || parteNome.contains("'") || parteNome.contains("*") || parteNome.contains("+") || parteNome.contains("/") || parteNome.contains("!") || parteNome.contains("@") || parteNome.contains("#") || parteNome.contains("$") || parteNome.contains("&")
						|| parteNome.contains("0") || parteNome.contains("1") || parteNome.contains("2") || parteNome.contains("3") || parteNome.contains("4") || parteNome.contains("5") || parteNome.contains("6") || parteNome.contains("7") || parteNome.contains("8") || parteNome.contains("9")){
							System.out.println("29");
							aluno.setLgCadastroProblema(1);
							if(AlunoDAO.update(aluno, connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro na identificacao");
							}
							entrou = true;
							break;
						}
					}
					
					if(entrou)
						continue;
				}
				
				
				
				if(aluno.getNrRg() != null && (aluno.getDtEmissaoRg() == null || aluno.getCdEstadoRg() == 0 || aluno.getSgOrgaoRg() == null)){
					System.out.println("30");
					aluno.setLgCadastroProblema(1);
					if(AlunoDAO.update(aluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na identificacao");
					}
					continue;
				}
				
				if(aluno.getDtNascimento().after(Util.getDataAtual())){
					System.out.println("31");
					aluno.setLgCadastroProblema(1);
					if(AlunoDAO.update(aluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na identificacao");
					}
					continue;
				}
				
				if(aluno.getDtEmissaoRg() != null){
					if(aluno.getDtEmissaoRg().after(Util.getDataAtual())){
						System.out.println("32");
						aluno.setLgCadastroProblema(1);
						if(AlunoDAO.update(aluno, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro na identificacao");
						}
						continue;
					}
				}
				
				if(aluno.getNrCpf() != null && !aluno.getNrCpf().equals("")){
					
					criterios = new ArrayList<ItemComparator>(); 
					criterios.add(new ItemComparator("cd_pessoa", "" + aluno.getCdPessoa(), ItemComparator.DIFFERENT, Types.INTEGER));
					criterios.add(new ItemComparator("nr_cpf", "" + aluno.getNrCpf(), ItemComparator.EQUAL, Types.VARCHAR));
					ResultSetMap rsmPessoaCpf = PessoaFisicaDAO.find(criterios, connect);
					if(rsmPessoaCpf.next()){
						System.out.println("33");
						aluno.setLgCadastroProblema(1);
						if(AlunoDAO.update(aluno, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro na identificacao");
						}
						continue;
					}
					
					if(!Util.isCpfValido(aluno.getNrCpf())){
						System.out.println("34");
						aluno.setLgCadastroProblema(1);
						if(AlunoDAO.update(aluno, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro na identificacao");
						}
						continue;
					}
				}
				
				/*
				 * Registro
				 */
				PessoaTipoDocumentacao pessoaTipoDocumentacaoRegistro = PessoaTipoDocumentacaoDAO.get(aluno.getCdAluno(), ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0), connect);
				
				if(pessoaTipoDocumentacaoRegistro != null && pessoaTipoDocumentacaoRegistro.getNrDocumento() != null && pessoaTipoDocumentacaoRegistro.getNrDocumento().trim().length() > 0){
					
					if(pessoaTipoDocumentacaoRegistro.getTpModelo() == 0 && (pessoaTipoDocumentacaoRegistro.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0) || 
					   pessoaTipoDocumentacaoRegistro.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0))){
						System.out.println("35");
						aluno.setLgCadastroProblema(1);
						if(AlunoDAO.update(aluno, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro na identificacao");
						}
						continue;
					}
					
					if(pessoaTipoDocumentacaoRegistro.getCdTipoDocumentacao() == 0 && (pessoaTipoDocumentacaoRegistro.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_NOVO || pessoaTipoDocumentacaoRegistro.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_ANTIGO)){
						System.out.println("36");
						aluno.setLgCadastroProblema(1);
						if(AlunoDAO.update(aluno, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro na identificacao");
						}
						continue;
					}
					
					if(pessoaTipoDocumentacaoRegistro.getDtEmissao() == null){
						System.out.println("37");
						aluno.setLgCadastroProblema(1);
						if(AlunoDAO.update(aluno, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro na identificacao");
						}
						continue;
					}
					
					if((pessoaTipoDocumentacaoRegistro.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0) || 
						pessoaTipoDocumentacaoRegistro.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)) && pessoaTipoDocumentacaoRegistro.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_NOVO && pessoaTipoDocumentacaoRegistro.getNrDocumento().trim().length() != 32){
						System.out.println("38");
							aluno.setLgCadastroProblema(1);
							if(AlunoDAO.update(aluno, connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro na identificacao");
							}
							continue;
					}
					
					if((pessoaTipoDocumentacaoRegistro.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0) || 
							pessoaTipoDocumentacaoRegistro.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)) &&
							pessoaTipoDocumentacaoRegistro.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_NOVO &&
							(pessoaTipoDocumentacaoRegistro.getNrDocumento() == null || pessoaTipoDocumentacaoRegistro.getNrDocumento().length() != 32)){
						System.out.println("39");
						aluno.setLgCadastroProblema(1);
						if(AlunoDAO.update(aluno, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro na identificacao");
						}
						continue;
					}
					
					if((pessoaTipoDocumentacaoRegistro.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0) || 
							pessoaTipoDocumentacaoRegistro.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)) &&
							pessoaTipoDocumentacaoRegistro.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_NOVO){
						String nrDv = Util.getMod11(pessoaTipoDocumentacaoRegistro.getNrDocumento().substring(0, 30));
						if(!nrDv.equals(pessoaTipoDocumentacaoRegistro.getNrDocumento().substring(30, 32))){
							System.out.println("pessoaTipoDocumentacaoRegistro.getNrDocumento() = " + pessoaTipoDocumentacaoRegistro.getNrDocumento());
							System.out.println("40");
							aluno.setLgCadastroProblema(1);
							if(AlunoDAO.update(aluno, connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro na identificacao");
							}
							continue;
						}
					}
					
					
					if(pessoaTipoDocumentacaoRegistro.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_NOVO){
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_pessoa", "" + aluno.getCdAluno(), ItemComparator.DIFFERENT, Types.INTEGER));
						criterios.add(new ItemComparator("nr_documento", pessoaTipoDocumentacaoRegistro.getNrDocumento(), ItemComparator.EQUAL, Types.VARCHAR));
						if(PessoaTipoDocumentacaoDAO.find(criterios, connect).next()){
							System.out.println("41");
							aluno.setLgCadastroProblema(1);
							if(AlunoDAO.update(aluno, connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro na identificacao");
							}
							continue;
						}
					}
					
					if((pessoaTipoDocumentacaoRegistro.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0) || 
							pessoaTipoDocumentacaoRegistro.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)) &&
							pessoaTipoDocumentacaoRegistro.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_NOVO && pessoaTipoDocumentacaoRegistro.getNrDocumento() != null && pessoaTipoDocumentacaoRegistro.getNrDocumento().length() > 15){
						
						String nrAnoEmissaoParte[] = pessoaTipoDocumentacaoRegistro.getNrDocumento().substring(10, 14).split("");
						boolean todosDigitos = true;
						for(String nrAnoEmissaoParteNome : nrAnoEmissaoParte){
							if(!Util.isNumber(nrAnoEmissaoParteNome)){
								todosDigitos = false;
								break;
							}
						}
						
						if(pessoaTipoDocumentacaoRegistro.getNrDocumento().length() > 15){
							int nrAnoEmissaoRegistro = Integer.parseInt(pessoaTipoDocumentacaoRegistro.getNrDocumento().substring(10, 14));
							if(pessoaTipoDocumentacaoRegistro.getDtEmissao() != null){
								int nrAnoEmissao = pessoaTipoDocumentacaoRegistro.getDtEmissao().get(Calendar.YEAR);
								if(nrAnoEmissaoRegistro > nrAnoEmissao){
									System.out.println("42");
									aluno.setLgCadastroProblema(1);
									if(AlunoDAO.update(aluno, connect) < 0){
										if(isConnectionNull)
											Conexao.rollback(connect);
										return new Result(-1, "Erro na identificacao");
									}
									continue;
								}
							}
						}
						if(!todosDigitos){
							System.out.println("43");
							aluno.setLgCadastroProblema(1);
							if(AlunoDAO.update(aluno, connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro na identificacao");
							}
							continue;
						}
					}
					
					if((pessoaTipoDocumentacaoRegistro.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0) || 
							pessoaTipoDocumentacaoRegistro.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)) &&
							pessoaTipoDocumentacaoRegistro.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_NOVO){
						
						String idCartorio = pessoaTipoDocumentacaoRegistro.getNrDocumento().substring(0, 6);
						
						String sql = "SELECT A.cd_pessoa AS cd_cartorio " +
								     "FROM grl_pessoa A " +
									 "JOIN grl_pessoa_juridica D ON (A.cd_pessoa = D.cd_pessoa) " +
							      	 " WHERE A.cd_pessoa = D.cd_pessoa AND D.id_serventia = '"+idCartorio+"'";
						ResultSetMap rsmCartorio = new ResultSetMap(connect.prepareStatement(sql).executeQuery());
						if(rsmCartorio.next()){
							int cdCartorio = rsmCartorio.getInt("cd_cartorio");
							if(pessoaTipoDocumentacaoRegistro.getCdCartorio() != cdCartorio){
								System.out.println("44");
								aluno.setLgCadastroProblema(1);
								if(AlunoDAO.update(aluno, connect) < 0){
									if(isConnectionNull)
										Conexao.rollback(connect);
									return new Result(-1, "Erro na identificacao");
								}
								continue;
							}
						}
						else{
							System.out.println("45");
							aluno.setLgCadastroProblema(1);
							if(AlunoDAO.update(aluno, connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro na identificacao");
							}
							continue;
							
						}
						
					}
					
					if(pessoaTipoDocumentacaoRegistro.getDtEmissao() != null){
						if(pessoaTipoDocumentacaoRegistro.getDtEmissao().before(aluno.getDtNascimento())){
							System.out.println("46");
							aluno.setLgCadastroProblema(1);
							if(AlunoDAO.update(aluno, connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro na identificacao");
							}
							continue;
						}
					}
					
					if(pessoaTipoDocumentacaoRegistro.getDtEmissao() != null){
						if(pessoaTipoDocumentacaoRegistro.getDtEmissao().after(Util.getDataAtual())){
							System.out.println("47");
							aluno.setLgCadastroProblema(1);
							if(AlunoDAO.update(aluno, connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro na identificacao");
							}
							continue;
						}
					}
//					if(pessoaTipoDocumentacaoRegistro != null){
//						pessoaTipoDocumentacaoRegistro.setCdPessoa(aluno.getCdAluno());
//						pessoaTipoDocumentacaoRegistro.setCdTipoDocumentacao(TipoDocumentacaoDAO.get(ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0), connect).getCdTipoDocumentacao());
//						if(PessoaTipoDocumentacaoServices.save(pessoaTipoDocumentacaoRegistro, connect).getCode() <= 0){
//							System.out.println("48");
//							aluno.setLgCadastroProblema(1);
//							if(AlunoDAO.update(aluno, connect) < 0){
//								if(isConnectionNull)
//									Conexao.rollback(connect);
//								return new Result(-1, "Erro na identificacao");
//							}
//							continue;
//						}
//					}
				}
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.cd_pessoa", "" + aluno.getCdAluno(), ItemComparator.DIFFERENT, Types.INTEGER));
				criterios.add(new ItemComparator("A.nm_pessoa", aluno.getNmPessoa(), ItemComparator.EQUAL, Types.VARCHAR));
				criterios.add(new ItemComparator("B.nm_mae", aluno.getNmMae(), ItemComparator.EQUAL, Types.VARCHAR));
				criterios.add(new ItemComparator("B.dt_nascimento", Util.formatDateTime(aluno.getDtNascimento(), "dd/MM/yyyy"), ItemComparator.EQUAL, Types.TIMESTAMP));
				if(PessoaFisicaServices.find(criterios, connect).next()){
					System.out.println("49");
					aluno.setLgCadastroProblema(1);
					if(AlunoDAO.update(aluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na identificacao");
					}
					continue;
				}
				
				if(aluno.getDtEmissaoRg() != null){
					if(aluno.getDtEmissaoRg().before(aluno.getDtNascimento())){
						System.out.println("50");
						aluno.setLgCadastroProblema(1);
						if(AlunoDAO.update(aluno, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro na identificacao");
						}
						continue;
					}
				}
				
				if(aluno.getNrRg() != null && !aluno.getNrRg().trim().equals("")){
					String nrRg[] = aluno.getNrRg().split("");
					boolean contemApenasZero = true;
					for(int i = 0; i < nrRg.length; i++){
						if(!nrRg[i].equals("0"))
							contemApenasZero = false;
					}
					if(contemApenasZero){
						if(isConnectionNull)
							Conexao.rollback(connect);
						continue;
					}
				}
				
				
				aluno.setLgCadastroProblema(0);
				if(AlunoDAO.update(aluno, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro na identificacao");
				}
			}	
			
			connect.commit();
			
			return new Result(1, "correto");
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static sol.util.Result importEnderecosFromTxtCSV()	{
		Connection connect   = Conexao.conectar();
		RandomAccessFile raf = null;
		try {
			/***********************
			 * Importando clientes
			 ***********************/
			
			System.out.println("Importando endereï¿½os de arquivo CSV...");
			//
			ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile("E:/Ceps correios atualizado 2016.csv", ";", true);
	  		System.out.println("Arquivo carregado...");
			connect.setAutoCommit(false);
			while(rsm.next())	{
	  			System.out.println(rsm.getRegister());
				if(rsm.getString("NR_CEP") == null && rsm.getString("NM_CIDADE") == null)
					continue;
				
				if(!rsm.getString("NM_CIDADE").startsWith("45"))
					continue;
				
	  			String nmLogradouro = Util.retirarAcentos(rsm.getString("NM_LOGRADOURO").toUpperCase().trim());
				String nmBairro = Util.retirarAcentos(rsm.getString("NM_BAIRRO").toUpperCase().trim());
				String nrCep = (rsm.getString("NR_CEP") != null ? rsm.getString("NR_CEP").replace("-", "") : rsm.getString("NM_CIDADE").replace("-", ""));
				System.out.println("nrCep = " + nrCep);
				int cdBairro = 0;
				ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
//				System.out.println("nmBairro = " + nmBairro);
				crt.add(new ItemComparator("A.nm_bairro", nmBairro, ItemComparator.EQUAL, Types.VARCHAR));			
				ResultSetMap rsmBairro = BairroServices.find(crt, connect);
				
				if(rsmBairro.size() < 1) {
					cdBairro = BairroDAO.insert(new Bairro(0, 0, 2240, nmBairro, "", 16), connect);
				}
				
				if(rsmBairro.next()){
					cdBairro = cdBairro != 0 ? cdBairro : rsmBairro.getInt("CD_BAIRRO");
				}
				
				int cdLogradouro = 0;
				crt = new ArrayList<ItemComparator>();
//				System.out.println("nmLogradouro = " + nmLogradouro);
//				System.out.println("cdBairro = " + cdBairro);
				crt.add(new ItemComparator("B.nm_logradouro", nmLogradouro, ItemComparator.EQUAL, Types.VARCHAR));
				crt.add(new ItemComparator("A.cd_bairro", "" + cdBairro, ItemComparator.EQUAL, Types.VARCHAR));
				ResultSetMap rsmLogradouro = LogradouroBairroServices.find(crt, connect);
							
				if(rsmLogradouro.size() < 1) {
					cdLogradouro = LogradouroDAO.insert(new Logradouro(0, 0, 2240, 0, nmLogradouro, ""));
					if(cdLogradouro > 0){
						LogradouroBairroDAO.insert(new LogradouroBairro(cdBairro, cdLogradouro), connect);
					}
				}
				
				while(rsmLogradouro.next()){
					cdLogradouro = cdLogradouro != 0 ? cdLogradouro : rsmLogradouro.getInt("CD_LOGRADOURO");
				}
				
				crt = new ArrayList<ItemComparator>();
//				System.out.println("cdLogradouro = " + cdLogradouro);
				crt.add(new ItemComparator("cd_logradouro", "" + cdLogradouro, ItemComparator.EQUAL, Types.VARCHAR));
				crt.add(new ItemComparator("nr_cep", nrCep, ItemComparator.EQUAL, Types.VARCHAR));
				ResultSetMap rsmLogradouroCep = LogradouroCepServices.find(crt, connect);
				if(!rsmLogradouroCep.next())
					LogradouroCepDAO.insert(new LogradouroCep(cdLogradouro, nrCep, null, null), connect);
				
					
			}
			connect.commit();
			System.out.println("Importaï¿½ï¿½o de endereï¿½os concluï¿½da!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar produtos!", e);
		}
		finally{
			if(raf!=null)
		  		try {raf.close(); } catch(Exception e){};
			Conexao.desconectar(connect);
		}
	}
	
	public static Result corrigirMatrizModulo(){
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = true;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			connect.prepareStatement("UPDATE acd_turma set cd_matriz = 1").executeUpdate();
			connect.prepareStatement("UPDATE acd_matricula set cd_matriz = 1").executeUpdate();
			
			ResultSetMap rsmTurmas = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso IS NOT NULL AND cd_curso > 0").executeQuery());
			while(rsmTurmas.next()){
				ResultSetMap rsmCursoModulo = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_curso_modulo WHERE cd_curso = " + rsmTurmas.getInt("cd_curso")).executeQuery());
				if(rsmCursoModulo.next())
					connect.prepareStatement("UPDATE acd_turma set cd_curso_modulo = " + rsmCursoModulo.getInt("cd_curso_modulo") + " WHERE cd_turma = " + rsmTurmas.getInt("cd_turma")).executeUpdate();
				else{
					CursoModulo cursoModulo = new CursoModulo(0, 0, "MODULO PRINCIPAL", null, null, null, null, 0, null, null, 0, 0, 0, null, 0, null, 0, 0, rsmTurmas.getInt("cd_curso"), 0);
					cursoModulo.setCdCursoModulo(CursoModuloDAO.insert(cursoModulo, connect));
					if(cursoModulo.getCdCursoModulo() <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1);
					}
					connect.prepareStatement("UPDATE acd_turma set cd_curso_modulo = " + cursoModulo.getCdCursoModulo() + " WHERE cd_turma = " + rsmTurmas.getInt("cd_turma")).executeUpdate();
				}
			}
			
			connect.commit();
			
			return new Result(1, "correto");
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result adicionarSetoresCorpoDocente(){
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = true;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			ResultSetMap rsmInstituicoes = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_instituicao WHERE cd_instituicao NOT IN (SELECT cd_instituicao FROM acd_instituicao WHERE cd_instituicao IN (SELECT cd_empresa FROM grl_setor))").executeQuery());
			while(rsmInstituicoes.next()){
				ResultSetMap rsmMaxSetor = new ResultSetMap(connect.prepareStatement("SELECT MAX(cd_setor) AS cd_setor FROM grl_setor").executeQuery());
				rsmMaxSetor.next();
				connect.prepareStatement("INSERT INTO grl_setor (cd_setor, cd_empresa, nm_setor, st_setor) VALUES ("+(rsmMaxSetor.getInt("cd_setor") + 1)+", "+rsmInstituicoes.getInt("cd_instituicao")+", 'CORPO DOCENTE', 1)").executeUpdate();
			}
			
			connect.commit();
			
			return new Result(1, "correto");
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result arrumarOfertaProfessor(){
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = true;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			ResultSetMap rsmOfertas = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_oferta WHERE cd_professor IS NULL").executeQuery());
			while(rsmOfertas.next()){
				ResultSetMap rsmPessoaOferta = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_pessoa_oferta WHERE cd_oferta = " + rsmOfertas.getInt("cd_oferta")).executeQuery());
				if(rsmPessoaOferta.size() > 1){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Oferta com mais de um professor");
				}
				if(rsmPessoaOferta.next()){
					Oferta oferta = OfertaDAO.get(rsmOfertas.getInt("cd_oferta"), connect);
					oferta.setCdProfessor(rsmPessoaOferta.getInt("cd_pessoa"));
					if(OfertaDAO.update(oferta, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar oferta");
					}
				}
			}
			
			connect.commit();
			
			return new Result(1, "correto");
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result retirarAlunosSemMatricula(){
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = true;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			ResultSetMap rsmAlunos = new ResultSetMap(connect.prepareStatement("SELECT cd_aluno FROM acd_aluno A WHERE NOT EXISTS (select * from acd_matricula B WHERE A.cd_aluno = B.cd_aluno)").executeQuery());
			while(rsmAlunos.next()){
				int ret = connect.prepareStatement("delete from grl_ocorrencia WHERE cd_pessoa in ("+rsmAlunos.getInt("cd_aluno")+")").executeUpdate();
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar ocorrencia");
				}
				ret = connect.prepareStatement("delete from grl_pessoa_empresa WHERE cd_pessoa in ("+rsmAlunos.getInt("cd_aluno")+")").executeUpdate();
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar pessoa empresa");
				}
				ret = connect.prepareStatement("delete from grl_pessoa_doenca WHERE cd_pessoa in ("+rsmAlunos.getInt("cd_aluno")+")").executeUpdate();
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar pessoa doenca");
				}
				ret = connect.prepareStatement("delete from grl_pessoa_alergia WHERE cd_pessoa in ("+rsmAlunos.getInt("cd_aluno")+")").executeUpdate();
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar pessoa alergia");
				}
				ret = connect.prepareStatement("delete from grl_pessoa_ficha_medica WHERE cd_pessoa in ("+rsmAlunos.getInt("cd_aluno")+")").executeUpdate();
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar pessoa ficha medica");
				}
				ret = connect.prepareStatement("delete from acd_aluno_recurso_prova WHERE cd_aluno in ("+rsmAlunos.getInt("cd_aluno")+")").executeUpdate();
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar aluno recurso prova");
				}
				ret = connect.prepareStatement("delete from acd_aluno WHERE cd_aluno in ("+rsmAlunos.getInt("cd_aluno")+")").executeUpdate();
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar aluno");
				}
				
				ret = connect.prepareStatement("update acd_aluno set cd_responsavel = null WHERE cd_responsavel in ("+rsmAlunos.getInt("cd_aluno")+")").executeUpdate();
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar aluno responsavel");
				}
				
				ret = connect.prepareStatement("delete from grl_pessoa_fisica WHERE cd_pessoa in ("+rsmAlunos.getInt("cd_aluno")+")").executeUpdate();
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar pessoa fisica");
				}
				ret = connect.prepareStatement("delete from grl_pessoa_endereco WHERE cd_pessoa in ("+rsmAlunos.getInt("cd_aluno")+")").executeUpdate();
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar pessoa endereco");
				}
				ret = connect.prepareStatement("delete from grl_pessoa_tipo_documentacao WHERE cd_pessoa in ("+rsmAlunos.getInt("cd_aluno")+")").executeUpdate();
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar pessoa tipo documentacao");
				}
				ret = connect.prepareStatement("delete from grl_pessoa where cd_pessoa IN ("+rsmAlunos.getInt("cd_aluno")+")").executeUpdate();
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar pessoa");
				}
			}
			
			connect.commit();
			
			return new Result(1, "correto");
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static sol.util.Result importPaisesFromCSV()	{
		Connection connect   = Conexao.conectar();
		RandomAccessFile raf = null;
		try {
			/***********************
			 * Importando NCM
			 ***********************/
			
			connect.setAutoCommit(false);
			
			System.out.println("Importando paï¿½ses de arquivo CSV...");
			//
			ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile("C://paises.csv", ";", true);
			System.out.println("Arquivo carregado...");
			connect.setAutoCommit(false);
			int lines = 0;
			while(rsm.next())	{
				String codPais = Util.limparAcentos(rsm.getString("CO_PAIS"));
	  			String nmPais  = Util.limparAcentos(rsm.getString("NO_PAIS"));
	  			if(!nmPais.equals("BRASIL")){
		  			if(PaisDAO.insert(new Pais(0/*cdPais*/, nmPais, null/*sgPais*/, 0/*cdRegiao*/, codPais), connect) <= 0){
		  				Conexao.rollback(connect);
		  				return new Result(-1, "Erro ao incluir paï¿½s");
		  			}
	  			}
	  			
			}
			connect.commit();
			System.out.println("Importaï¿½ï¿½o de ncm concluï¿½da - "+lines+" registros inseridos!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar produtos!", e);
		}
		finally{
			if(raf!=null)
		  		try {raf.close(); } catch(Exception e){};
			Conexao.desconectar(connect);
		}
	}
	
	
	public static sol.util.Result importCNAEFromCSV(String path)	{
		//C://CNAE.csv
		Connection connect   = Conexao.conectar();
		RandomAccessFile raf = null;
		try {
			/***********************
			 * Importando CNAE
			 ***********************/
			
			connect.setAutoCommit(false);
			
			System.out.println("Importando CNAE de arquivo CSV...");
			ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile(path, ";", true);
			System.out.println("Arquivo carregado...");
			
			Result r;
			int lines = 0;
			ArrayList<Integer> cdCnaeSuperior = new ArrayList<Integer>();
			cdCnaeSuperior.add(0);
			boolean updateCdCnaeSuperior = true;
			int nrNivel = 1;
			while(rsm.next())	{
				String nmSecao = rsm.getString("NM_SECAO");
				String nmDivisao  = rsm.getString("NM_DIVISAO");
				String nmGrupo  = rsm.getString("NM_GRUPO");
				String nmClasse  = rsm.getString("NM_CLASSE");
				String nmCnae  = rsm.getString("NM_CNAE");
			
				updateCdCnaeSuperior = true;
				if( rsm.getString("NM_SECAO") != null && !rsm.getString("NM_SECAO").equals("") ){
					nrNivel = 1;
					cdCnaeSuperior.set(0, 0);
					if( cdCnaeSuperior.size() > 1   )
						cdCnaeSuperior.remove( cdCnaeSuperior.size()-1 );
				}
				
				if( rsm.getString("NM_DIVISAO") != null && !rsm.getString("NM_DIVISAO").equals("") ){
					nrNivel = 2;
					if( cdCnaeSuperior.size() > 2   )
						cdCnaeSuperior.remove( cdCnaeSuperior.size()-1 );
				}
				
				if( rsm.getString("NM_GRUPO") != null &&  !rsm.getString("NM_GRUPO").equals("") ){
					nrNivel = 3;
					if( cdCnaeSuperior.size() > 3   )
						cdCnaeSuperior.remove( cdCnaeSuperior.size()-1 );
				}
				
				
				if( rsm.getString("NM_CLASSE") != null &&  !rsm.getString("NM_CLASSE").equals("") ){
					nrNivel = 4;
					updateCdCnaeSuperior = false;
				}
		
				r = CnaeServices.save( new Cnae(0/*cdCnae*/, Util.limparAcentos(nmCnae)/*nmCnae*/, ""/*sgCnae*/, 
												nmSecao+""+nmDivisao+""+nmGrupo+""+nmClasse/*idCnae*/,
												cdCnaeSuperior.size()>=1?nrNivel:0,
												cdCnaeSuperior.get( cdCnaeSuperior.size()-1 ), ""/*nrCnae*/), null, connect);
				if( r.getCode() <= 0 ){
					Conexao.rollback(connect);
					return new sol.util.Result(-1, "Erro ao cadastrar CNAE");
				}
				if( updateCdCnaeSuperior )
					cdCnaeSuperior.add(r.getCode());
//	  			System.out.println(  String.valueOf(lines+1)+nmSecao+" "+nmDivisao+" "+nmGrupo+""+nmClasse+""+nmCnae );
	  			lines++;
			}
			connect.commit();
			System.out.println("Importaï¿½ï¿½o de cnaes concluï¿½da - "+lines+" registros inseridos!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar CNAES!", e);
		}
		finally{
			if(raf!=null)
		  		try {raf.close(); } catch(Exception e){};
			Conexao.desconectar(connect);
		}
	}
	
	public static sol.util.Result importCategoriaCusteioCapitalFromCSV( String path, int cdCategoriaCusteio,
			int cdServicoFisica, int cdServicoJuridica, int cdCategoriaCapital )	{
		//C://categorias-custeio-capital.csv
		Connection connect   = Conexao.conectar();
		RandomAccessFile raf = null;
		try {
			/***********************
			 * Importando Categorias Custeio Capital
			 ***********************/
			
			connect.setAutoCommit(false);
			
			System.out.println("Importando categorias custeio/capital de arquivo CSV...");
			ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile(path, ";", true);
			System.out.println("Arquivo carregado...");
			
			Result r;
			int cdCategoriaSuperior = 0;
			int lines = 0;
			while(rsm.next())	{
				String idCategoriaSuperior = rsm.getString("ID_CATEGORIA");
				String nmCategoria  = rsm.getString("NM_CATEGORIA");
				
				switch (idCategoriaSuperior) {
					case "1":
						cdCategoriaSuperior = cdCategoriaCusteio;
						break;
					case "2":
						cdCategoriaSuperior = cdServicoFisica;
						break;
					case "3":
						cdCategoriaSuperior = cdServicoJuridica;
						break;
					case "4":
						cdCategoriaSuperior = cdCategoriaCapital;
						break;
				}
				
				CategoriaEconomica novaCategoria = new CategoriaEconomica();
				novaCategoria.setNmCategoriaEconomica(Util.limparAcentos(nmCategoria));
				novaCategoria.setCdCategoriaSuperior(cdCategoriaSuperior);
				novaCategoria.setLgAtivo(1);
				r = CategoriaEconomicaServices.save(novaCategoria, connect);
				if( r.getCode() <= 0 ){
					Conexao.rollback(connect);
					return new sol.util.Result(-1, "Erro ao cadastrar categoria");
				}
				
	  			lines++;
			}
			
			CategoriaEconomica custeio = CategoriaEconomicaDAO.get(cdCategoriaCusteio, connect);
			CategoriaEconomica capital = CategoriaEconomicaDAO.get(cdCategoriaCapital, connect);
			
			r = CategoriaEconomicaServices.save(custeio, connect);
			if( r.getCode() <= 0 ){
				Conexao.rollback(connect);
				return new sol.util.Result(-1, "Erro ao ajustar cat. de custeio");
			}
			r = CategoriaEconomicaServices.save(capital, connect);
			if( r.getCode() <= 0 ){
				Conexao.rollback(connect);
				return new sol.util.Result(-1, "Erro ao ajustar cat. de capital");
			}
			
			connect.commit();
			System.out.println("Importaï¿½ï¿½o de categorias custeio/capital concluï¿½da - "+lines+" registros inseridos!");
			return new sol.util.Result(1, "Importaï¿½ï¿½o de categorias custeio/capital concluï¿½da - "+lines+" registros inseridos!");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar CNAES!", e);
		}
		finally{
			if(raf!=null)
		  		try {raf.close(); } catch(Exception e){};
			Conexao.desconectar(connect);
		}
	}
	
	public static sol.util.Result importContaUexFromCSV( String path, int cdAgencia )	{
		//C://contas-uex.csv
		Connection connect   = Conexao.conectar();
		RandomAccessFile raf = null;
		try {
			/***********************
			 * Importando Categorias Custeio Capital
			 ***********************/
			
			connect.setAutoCommit(false);
			
			System.out.println("Importando contas bancï¿½rias de arquivo CSV...");
			ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile(path, ";", true);
			System.out.println("Arquivo carregado...");
			
			Result r;
			ResultSet rs;
			
			int cdPdde = 3;
			int cdMaisEdu = 2;
			int cdPde = 5;
			int cdQualidade = 6;
			int cdEstrutura = 7;
			
			
			int lines = 0;
			int contasLines = 0;
			while(rsm.next())	{
				String nrCnpj = rsm.getString("NR_CNPJ");
				String nmUex = rsm.getString("NM_UEX");
				String nmPdde  = rsm.getString("NM_PDDE");
				String nmMaisEdu  = rsm.getString("NM_MAIS_EDUCACAO");
				String nmMaisEdu2  = rsm.getString("NM_MAIS_EDUCACAO2");
				String nmPde  = rsm.getString("NM_PDE");
				String nmPddeQualidade  = rsm.getString("NM_PDDE_QUALIDADE");
				String nmPddeEstrutura  = rsm.getString("NM_PDDE_ESTRUTURA");
				contasLines = 0;
				
				rs = connect.prepareStatement("SELECT * FROM GRL_PESSOA_JURIDICA a "+
												" JOIN GRL_EMPRESA B ON ( A.cd_pessoa = b.cd_empresa ) "+
												" WHERE NR_CNPJ = '"+nrCnpj+"'").executeQuery();
				UnidadeExecutora uex = null;
				if( rs.next() ){
					uex = UnidadeExecutoraDAO.get(rs.getInt("CD_PESSOA"), connect);
				}else{
					System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					System.out.println("Uex nï¿½o encontrada: "+nmUex+" - "+Util.formatCnpj(nrCnpj));
					System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					continue;
				}
							
				ContaFinanceira novaContaModelo = new ContaFinanceira();
				novaContaModelo.setCdEmpresa( uex.getCdUnidadeExecutora() );
				novaContaModelo.setCdAgencia(cdAgencia);
				novaContaModelo.setTpConta(ContaFinanceiraServices.TP_CONTA_BANCARIA);
				novaContaModelo.setTpOperacao(3);
				
				ArrayList<ContaFinanceira> contas = new ArrayList<ContaFinanceira>();
				ArrayList<Integer> programas = new ArrayList<Integer>();
				
				if( nmPdde.trim().length() > 0 ){
					ContaFinanceira contaPdde = (ContaFinanceira)novaContaModelo.clone();
					contaPdde.setNmConta("BB - "+nmPdde.substring(0, nmPdde.length()-1)+"-"+nmPdde.substring(nmPdde.length()-1));
					contaPdde.setNrConta(nmPdde.substring(0, nmPdde.length()-1));
					contaPdde.setNrDv(nmPdde.substring(nmPdde.length()-1));
					contas.add(contaPdde);
					programas.add(cdPdde);
				}
				if( nmMaisEdu.trim().length() > 0 ){
					ContaFinanceira contaMaisEdu = (ContaFinanceira)novaContaModelo.clone();
					contaMaisEdu.setNmConta("BB - "+nmMaisEdu.substring(0, nmMaisEdu.length()-1)+"-"+nmMaisEdu.substring(nmMaisEdu.length()-1));
					contaMaisEdu.setNrConta(nmMaisEdu.substring(0, nmMaisEdu.length()-1));
					contaMaisEdu.setNrDv(nmMaisEdu.substring(nmMaisEdu.length()-1));
					contas.add(contaMaisEdu);
					programas.add(cdMaisEdu);
				}
				if( nmMaisEdu2.trim().length() > 0 ){
					ContaFinanceira contaMaisEdu2 = (ContaFinanceira)novaContaModelo.clone();
					contaMaisEdu2.setNmConta("BB - "+nmMaisEdu2.substring(0, nmMaisEdu2.length()-1)+"-"+nmMaisEdu2.substring(nmMaisEdu2.length()-1));
					contaMaisEdu2.setNrConta(nmMaisEdu2.substring(0, nmMaisEdu2.length()-1));
					contaMaisEdu2.setNrDv(nmMaisEdu2.substring(nmMaisEdu2.length()-1));
					contas.add(contaMaisEdu2);
					programas.add(cdMaisEdu);
				}
				if( nmPde.trim().length() > 0 ){
					ContaFinanceira contaPde = (ContaFinanceira)novaContaModelo.clone();
					contaPde.setNmConta("BB - "+nmPde.substring(0, nmPde.length()-1)+"-"+nmPde.substring(nmPde.length()-1));
					contaPde.setNrConta(nmPde.substring(0, nmPde.length()-1));
					contaPde.setNrDv(nmPde.substring(nmPde.length()-1));
					contas.add(contaPde);
					programas.add(cdPde);
				}
				if( nmPddeQualidade.trim().length() > 0 ){
					ContaFinanceira contaPddeQ = (ContaFinanceira)novaContaModelo.clone();
					contaPddeQ.setNmConta("BB - "+nmPddeQualidade.substring(0, nmPddeQualidade.length()-1)+"-"+nmPddeQualidade.substring(nmPddeQualidade.length()-1));
					contaPddeQ.setNrConta(nmPddeQualidade.substring(0, nmPddeQualidade.length()-1));
					contaPddeQ.setNrDv(nmPddeQualidade.substring(nmPddeQualidade.length()-1));
					contas.add(contaPddeQ);
					programas.add(cdQualidade);
				}
				if( nmPddeEstrutura.trim().length() > 0 ){
					ContaFinanceira contaPddeEst = (ContaFinanceira)novaContaModelo.clone();
					contaPddeEst.setNmConta("BB - "+nmPddeEstrutura.substring(0, nmPddeEstrutura.length()-1)+"-"+nmPddeEstrutura.substring(nmPddeEstrutura.length()-1));
					contaPddeEst.setNrConta(nmPddeEstrutura.substring(0, nmPddeEstrutura.length()-1));
					contaPddeEst.setNrDv(nmPddeEstrutura.substring(nmPddeEstrutura.length()-1));
					contas.add(contaPddeEst);
					programas.add(cdEstrutura);
				}
					
				
				for( int i=0; i<contas.size();i++ ){
					r = ContaFinanceiraServices.save( contas.get(i) , connect);
					if( r.getCode() <= 0 ){
						Conexao.rollback(connect);
						return new sol.util.Result(-1, "Erro ao cadastrar conta bancï¿½ria");
					}
					int cdPrograma = programas.get(i);
					ProgramaContaFinanceira pc = new ProgramaContaFinanceira();
					pc.setCdConta(r.getCode());
					pc.setCdPrograma(cdPrograma);
					pc.setCdUnidadeExecutora(uex.getCdUnidadeExecutora());
					pc.setStVinculacao(ProgramaContaFinanceiraServices.ST_VINCULACAO_ATIVA );
					pc.setDtVinculacao(new GregorianCalendar());
					r = ProgramaContaFinanceiraServices.save(pc, connect);
					if( r.getCode() <= 0 ){
						Conexao.rollback(connect);
						return new sol.util.Result(-1, "Erro ao cadastrar conta bancï¿½ria");
					}
					contasLines++;
				}
				
				System.out.println(nmUex +": "+contasLines+" contas importadas!");
				lines++;
			}
			
			connect.commit();
			System.out.println("Importaï¿½ï¿½o de contas bancï¿½rias dos programas - "+lines+" registros inseridos!");
			return new sol.util.Result(1, "Importaï¿½ï¿½o de contas bancï¿½rias dos programas concluï¿½da - "+lines+" instituiï¿½ï¿½o inseridos!");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar CNAES!", e);
		}
		finally{
			if(raf!=null)
				try {raf.close(); } catch(Exception e){};
				Conexao.desconectar(connect);
		}
	}
	
	public static Result migrarSSAA()	{
		
		//tabelas que devem ser comitadas mais rapidamente que o restante
		ArrayList<String> fastCommitTables = new ArrayList<String>();
		fastCommitTables.add("grl_arquivo");
		fastCommitTables.add("prc_processo_arquivo");
		fastCommitTables.add("ptc_documento_arquivo");
		fastCommitTables.add("seg_release");
		
		//jdbc:firebirdsql://127.0.0.1:3050//tivic/database/jurismanager_.fdb?lc_ctype=ISO8859_1
		//jdbc:firebirdsql://127.0.0.1:3050/D:/database/SSAA_MIGRACAO.FDB?encoding=ISO8859_1
		
		MigrationManager.init("org.firebirdsql.jdbc.FBDriver", 
							  "jdbc:firebirdsql://127.0.0.1:3050//tivic/database/jurismanager_.fdb?lc_ctype=ISO8859_1", 
							  "sysdba", 
							  "masterkey",
							  
							  "org.postgresql.Driver", 
							  "jdbc:postgresql://127.0.0.1/juris_migracao", 
							  "postgres", 
							  "cdf3dp!",
							  
							  false /*autoCommit*/, 
							  1000 /*commitCount*/, 
							  4000 /*batchCount*/, 
							  3 /*fastCommitCount*/, 
							  6 /*fastBatchCount*/, 
							  fastCommitTables);
		
		
//		MigrationManager.printSourceMetaData();
//		return new Result(1);
		
		return MigrationManager.migrate();
	}
	
	public static Result migrarDrEdson()	{
		
		//tabelas que devem ser comitadas mais rapidamente que o restante
		ArrayList<String> fastCommitTables = new ArrayList<String>();
		fastCommitTables.add("grl_arquivo");
		fastCommitTables.add("prc_processo_arquivo");
		fastCommitTables.add("ptc_documento_arquivo");
		fastCommitTables.add("seg_release");
		
		//jdbc:firebirdsql://127.0.0.1:3050//tivic/database/jurismanager_.fdb?lc_ctype=ISO8859_1
		//jdbc:firebirdsql://127.0.0.1:3050/D:/database/SSAA_MIGRACAO.FDB?encoding=ISO8859_1
		
		MigrationManager.init("org.firebirdsql.jdbc.FBDriver", 
							  "jdbc:firebirdsql://127.0.0.1:3050/D:/edson.fdb?lc_ctype=ISO8859_1", 
							  "sysdba", 
							  "masterkey",
							  
							  "org.postgresql.Driver", 
							  "jdbc:postgresql://127.0.0.1/jurismanager_death", 
							  "postgres", 
							  "t1v1k!",
							  
							  false /*autoCommit*/, 
							  1000 /*commitCount*/, 
							  4000 /*batchCount*/, 
							  3 /*fastCommitCount*/, 
							  6 /*fastBatchCount*/, 
							  fastCommitTables);
		
		
//		MigrationManager.printSourceMetaData();
//		return new Result(1);
		
		return MigrationManager.migrate();
	}
	
	public static Result gerarQuantidadeDeMatriculas()	{
		Connection connect = Conexao.conectar();
		try	{
			ResultSetMap rsmFinal = new ResultSetMap();
			ResultSetMap rsmInstituicoes = InstituicaoServices.getAllAtivas();
			while(rsmInstituicoes.next()){
				ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT A.cd_aluno, A.cd_turma, A.cd_matricula, A.st_matricula, B.nm_pessoa AS nm_aluno, A.nr_matricula, A.st_aluno_censo, D.nm_produto_servico AS NM_CURSO_MATRICULA, PF.nm_mae, PF.dt_nascimento " +
						 "FROM acd_matricula A " +
	                     "JOIN grl_pessoa B ON (A.cd_aluno = B.cd_pessoa) " +
	                     "JOIN grl_pessoa_fisica PF ON (A.cd_aluno = PF.cd_pessoa) " +
	                     "JOIN acd_turma  C ON (A.cd_turma = C.cd_turma) " +
	                     "JOIN acd_instituicao_periodo  IP ON (A.cd_periodo_letivo = IP.cd_periodo_letivo) " +
	                     "JOIN grl_produto_servico  D ON (A.cd_curso = D.cd_produto_servico) " +
	                     "WHERE 1=1 AND C.cd_instituicao = "+rsmInstituicoes.getInt("cd_instituicao") + " AND IP.nm_periodo_letivo = '2016' " +
	                     " ORDER BY B.nm_pessoa").executeQuery());
				String dtReferenciaCenso = ParametroServices.getValorOfParametro("DT_REFERENCIA_CENSO", connect);
				dtReferenciaCenso = dtReferenciaCenso + "/2016";
				GregorianCalendar dtReferenciaCensoCalendar = Util.convStringToCalendar4(dtReferenciaCenso);
				
				InstituicaoPeriodo instituicaoPeriodoAtual = null;
				ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(rsmInstituicoes.getInt("cd_instituicao"), connect);
				if(rsmPeriodoAtual.next()){
					instituicaoPeriodoAtual = InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connect);
				}
				
				int x = 0;
				ResultSetMap rsmFromInstituicao = new ResultSetMap();
				ArrayList<Integer> codigos = new ArrayList<Integer>();
				
				while(rsm.next()){
									
					boolean excluido = false;
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_aluno", "" + rsm.getInt("cd_aluno"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_ALUNO, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmAlunosOcorrenciaAluno = OcorrenciaAlunoServices.find(criterios, connect);
					while(rsmAlunosOcorrenciaAluno.next()){
						if(rsmAlunosOcorrenciaAluno.getGregorianCalendar("dt_ocorrencia").after(dtReferenciaCensoCalendar)){
							rsm.deleteRow();
							if(x == 0)
								rsm.beforeFirst();
							else
								rsm.previous();
							excluido = true;
							break;
						}
					}
					rsmAlunosOcorrenciaAluno.beforeFirst();
					
					if(excluido)
						continue;
					
					excluido = false;
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_matricula_origem", "" + rsm.getInt("cd_matricula"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_MATRICULA, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmAlunosOcorrenciaMatricula = OcorrenciaMatriculaServices.find(criterios, connect);
					while(rsmAlunosOcorrenciaMatricula.next()){
						if(rsmAlunosOcorrenciaMatricula.getGregorianCalendar("dt_ocorrencia").after(dtReferenciaCensoCalendar)){
							rsm.deleteRow();
							if(x == 0)
								rsm.beforeFirst();
							else
								rsm.previous();
							excluido = true;
							break;
						}
					}
					rsmAlunosOcorrenciaAluno.beforeFirst();
					
					if(excluido)
						continue;
					
					rsmAlunosOcorrenciaMatricula = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_ocorrencia_matricula A, grl_ocorrencia B "
							+ "																				WHERE A.cd_ocorrencia = B.cd_ocorrencia "
							+ "																				  AND (cd_matricula_origem = "+rsm.getInt("cd_matricula") + " OR cd_matricula_destino = " + rsm.getInt("cd_matricula") + ")"
							+ "																				ORDER BY dt_ocorrencia DESC").executeQuery());
					
					Aluno aluno = AlunoDAO.get(rsm.getInt("cd_aluno"), connect);
	//					System.out.println("Aluno " + aluno.getNmAlunoCenso());
	//					System.out.println("Matricula " + MatriculaServices.situacao[rsm.getInt("st_matricula")]);
	//					System.out.println("rsmAlunosOcorrenciaMatricula = " + rsmAlunosOcorrenciaMatricula);
					while(rsmAlunosOcorrenciaMatricula.next()){
						Matricula matricula = MatriculaDAO.get(rsmAlunosOcorrenciaMatricula.getInt("cd_matricula_origem"), connect);
						if(instituicaoPeriodoAtual != null){
							InstituicaoPeriodo instituicaoPeriodoMatricula = InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connect);
							if(!instituicaoPeriodoMatricula.getNmPeriodoLetivo().equals(instituicaoPeriodoAtual.getNmPeriodoLetivo())){
								continue;
							}
						}
						
	//						System.out.println("dtOcorrencia = " + Util.convCalendarString3(rsmAlunosOcorrenciaMatricula.getGregorianCalendar("dt_ocorrencia")));
	//						System.out.println("dtReferenciaCensoCalendar = " + Util.convCalendarString3(dtReferenciaCensoCalendar));
	//						System.out.println("VALIDACAO = " + (rsmAlunosOcorrenciaMatricula.getGregorianCalendar("dt_ocorrencia").after(dtReferenciaCensoCalendar)));
						if(rsmAlunosOcorrenciaMatricula.getGregorianCalendar("dt_ocorrencia").after(dtReferenciaCensoCalendar)){
	//							System.out.println("!!!!!!rsm.stMatricula = " + rsm.getInt("st_matricula") + " -> " + rsmAlunosOcorrenciaMatricula.getInt("st_matricula_origem"));
							rsm.setValueToField("ST_MATRICULA", rsmAlunosOcorrenciaMatricula.getInt("st_matricula_origem"));
						}
					}
					rsmAlunosOcorrenciaMatricula.beforeFirst();
					
	//					System.out.println("Matricula FINAL " + MatriculaServices.situacao[rsm.getInt("st_matricula")]);
					if(rsm.getInt("ST_MATRICULA") != MatriculaServices.ST_ATIVA && rsm.getInt("ST_MATRICULA") != MatriculaServices.ST_CONCLUIDA){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						excluido = true;
						continue;
					}
					
					x++;
					
					
					if(!codigos.contains(rsm.getInt("cd_aluno"))){
	//					System.out.println("Cadastrado: " + rsm.getRegister());
						rsmFromInstituicao.addRegister(rsm.getRegister());
						codigos.add(rsm.getInt("cd_aluno"));
					}
					else{
	//					System.out.println("Nï¿½o cadastrado");
					}
				}
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("NM_INSTITUICAO", rsmInstituicoes.getString("NM_INSTITUICAO"));
				register.put("NR_MATRICULAS_2016", rsmFromInstituicao.size());
				register.put("NR_MATRICULAS_2017", InstituicaoServices.getAlunosOf(rsmInstituicoes.getInt("CD_INSTITUICAO"), instituicaoPeriodoAtual.getCdPeriodoLetivo()).size());
				rsmFinal.addRegister(register);
			}
			
			System.out.println(rsmFinal);
			
			return new Result(1);
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return new Result(-1);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}	
	
	public static Result migrarTransito()	{
		
		//tabelas que devem ser comitadas mais rapidamente que o restante
		ArrayList<String> fastCommitTables = new ArrayList<String>();
		
		fastCommitTables.add("str_ait_imagem");
		fastCommitTables.add("talonario");
		fastCommitTables.add("seg_release");
		
		ConfManager conf = Util.getConfManager();
		
		String driverOrigem = conf.getProps().getProperty("MIGRACAO_DRIVER_ORIGEM");
		String bdOrigem 	= conf.getProps().getProperty("MIGRACAO_BD_ORIGEM");
		String userOrigem 	= conf.getProps().getProperty("MIGRACAO_USER_ORIGEM");
		String senhaOrigem 	= conf.getProps().getProperty("MIGRACAO_SENHA_ORIGEM");
		

		String driverDestino = conf.getProps().getProperty("MIGRACAO_DRIVER_DESTINO");
		String bdDestino 	 = conf.getProps().getProperty("MIGRACAO_BD_DESTINO");
		String userDestino 	 = conf.getProps().getProperty("MIGRACAO_USER_DESTINO");
		String senhaDestino  = conf.getProps().getProperty("MIGRACAO_SENHA_DESTINO");

		String ignoreTablesStr  = conf.getProps().getProperty("MIGRACAO_IGNORE_TABLES");
		ArrayList<String> ignoreTables = null;
		if(ignoreTablesStr!=null && !ignoreTablesStr.equals("")) {
			StringTokenizer token = new StringTokenizer(ignoreTablesStr, "|");
			ignoreTables = new ArrayList<String>();
			while(token.hasMoreTokens())
				ignoreTables.add(token.nextToken());
		}
		
		
		
		//REMOVER NAS PROXIMAS MIGRACOES
//		ArrayList<String> ignoreTables = new ArrayList<String>();
//		ignoreTables.add("str_ait_imagem");
//		ArrayList<String> ignoreTables = null;
		
		
		//jdbc:firebirdsql://127.0.0.1:3050//tivic/database/jurismanager_.fdb?lc_ctype=ISO8859_1
		//jdbc:firebirdsql://127.0.0.1:3050/D:/database/SSAA_MIGRACAO.FDB?encoding=ISO8859_1
		
		//"jdbc:firebirdsql://192.168.1.20:3050//tivic/databases/etransito.fdb?lc_ctype=ISO8859_1",
		
		//"jdbc:firebirdsql://127.0.0.1:3050/D:/Database/transito_teixeira_final.fdb?lc_ctype=ISO8859_1", 
		//"jdbc:postgresql://127.0.0.1:5432/etransito_teixeira_final"
		
		//"jdbc:firebirdsql://10.0.0.10:3050/E:/tivic/database/Teixeira2.dat?lc_ctype=ISO8859_1",
		//"jdbc:postgresql://127.0.0.1:5432/etransito_teixeira", 
		
		MigrationManager.init(//origem
							  driverOrigem,// "org.firebirdsql.jdbc.FBDriver", 
							  bdOrigem, //"jdbc:firebirdsql://localhost:3050/E:/tivic/DB/ETRANSITO_SDB.FDB?lc_ctype=ISO8859_1", 
							  userOrigem, //"sysdba", 
							  senhaOrigem, //"masterkey",
							  //destino
							  driverDestino, //"org.postgresql.Driver", 
							  bdDestino, //"jdbc:postgresql://192.168.1.144:5432/etransito_sdb_migracao", 
							  userDestino, //"postgres", 
							  senhaDestino, //"433UQM!",
							  
							  false /*autoCommit*/, 
							  1000 /*commitCount*/, 
							  4000 /*batchCount*/, 
							  3 /*fastCommitCount*/, 
							  6 /*fastBatchCount*/, 
							  fastCommitTables,
							  ignoreTables);
		
		
//		MigrationManager.printSourceMetaData();
//		return new Result(1);
		
		return MigrationManager.migrate();
	}
	
	public static Result migrarMobilidadeToSQLite()	{
		
		//tabelas que devem ser comitadas mais rapidamente que o restante
		ArrayList<String> fastCommitTables = new ArrayList<String>();
		
		fastCommitTables.add("str_ait_imagem");
		fastCommitTables.add("talonario");
		fastCommitTables.add("seg_release");
		
		
						
		MigrationManager.init(
							  "org.postgresql.Driver", 
							  "jdbc:postgresql://192.168.1.20:5432/mobilidade2", 
							  "postgres", 
							  "t1v1k!",
							  "org.sqlite.JDBC", 
							  "jdbc:sqlite:E://mobilidade.db", 
							  "--", 
							  "--",							  
							  false /*autoCommit*/, 
							  1000 /*commitCount*/, 
							  4000 /*batchCount*/, 
							  3 /*fastCommitCount*/, 
							  6 /*fastBatchCount*/, 
							  fastCommitTables);
		
		
//		MigrationManager.printSourceMetaData();
//		return new Result(1);
		
		return MigrationManager.migrate();
	}
	
	/**
	 * 
	 */
	public static Result importPrcProcesso(String nmArquivo, AuthData auth) {
		return importPrcProcesso(nmArquivo, auth, null);
	}
	public static Result importPrcProcesso(String nmArquivo, AuthData auth, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if(isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			// aux objects
			RandomAccessFile raf 	= null;
			PreparedStatement pstmt = null;
			ResultSet rs 			= null;
			Result result 			= null;
			GregorianCalendar hoje	= new GregorianCalendar();
			
			// log
			File logFile = new File(nmArquivo+".log.txt");
			BufferedWriter logWriter = new BufferedWriter(new FileWriter(logFile));
			logWriter.write("==================================================");
			logWriter.newLine();
			logWriter.write("IMPORTAÇÃO DE PROCESSOS\n");
			logWriter.newLine();
			logWriter.write("\t"+Util.formatDate(hoje, "dd/MM/yyyy HH:mm")+"\n\n");
			logWriter.newLine();
			logWriter.write("==================================================");
			logWriter.newLine();
			
			int count 	= 0;
			int saved 	= 0;
			int ignored = 0;
			
			// fields
			String nrProcesso				 = null;
			String nmTribunal				 = null;
			String nmCidade					 = null;
			String sgEstado					 = null;
			int nrJuizo						 = 1;
			String nmJuizo 					 = null;
			String nmCliente 				 = null;
			String nmAdverso 				 = null;
			String nrCpfAdverso				 = null;
			String nmTipoAcao 				 = null;
			String nmTpRito 				 = null;
			String nmTpAutos 				 = null;
			GregorianCalendar dtDistribuicao = null;
			GregorianCalendar dtRepasse 	 = null;
			String nmTpRepasse 				 = null;
			String nmAdvResponsavel 		 = null;
			String nmGrupoTrabalho 			 = null;
			String nmAdvAdverso 	 		 = null;
			String nmTipoObjeto 	 		 = null;
			String nmGrupoProcesso   		 = null;
			String nmSistemaProcesso 		 = null;		
			String nmConteiner3				 = null;
			
		    raf = new RandomAccessFile(nmArquivo, "rw");
		    String line = "";
		    while((line = raf.readLine()) != null)	{
		    	
		    	count++;
		    	
		    	StringTokenizer tokens = new StringTokenizer(line, ";", false);
		    	
		    	nrProcesso 		  = tokens.nextToken().trim();
		    	nmTribunal 		  = tokens.nextToken().trim();
		    	nmCidade		  = tokens.nextToken().trim();
		    	sgEstado 		  = tokens.nextToken().trim();
		    	nrJuizo 		  = Integer.parseInt(tokens.nextToken().trim());
		    	nmJuizo 		  = tokens.nextToken().trim();
		    	nmCliente 		  = tokens.nextToken().trim();
		    	nmAdverso 		  = tokens.nextToken().trim();
		    	nrCpfAdverso	  = tokens.nextToken().trim().replaceAll(".", "").replaceAll("-", "");
		    	nmTipoAcao 		  = tokens.nextToken().trim();
		    	nmTpRito 		  = tokens.nextToken().trim();
		    	nmTpAutos 		  = tokens.nextToken().trim();
		    	dtDistribuicao 	  = Util.convStringToCalendar(tokens.nextToken().trim());
		    	dtRepasse	   	  = Util.convStringToCalendar(tokens.nextToken().trim());
		    	nmTpRepasse    	  = tokens.nextToken().trim();
		    	nmAdvResponsavel  = tokens.nextToken().trim();
		    	nmGrupoTrabalho	  = tokens.nextToken().trim();
		    	nmAdvAdverso   	  = tokens.nextToken().trim();
		    	nmTipoObjeto   	  = tokens.nextToken().trim();
		    	nmGrupoProcesso	  = tokens.nextToken().trim();
		    	nmSistemaProcesso = tokens.nextToken().trim();
		    	nmConteiner3	  = tokens.nextToken().trim();
		    	
		    	
		    	// PROCESSO  0805491%23%2014%8%20%0004
	  			String nrProcessoPartes = null;
	  			if(nrProcesso.length()>=20) {					
					nrProcessoPartes = nrProcesso.substring(0, 7) +"%"+
									   nrProcesso.substring(7, 9) +"%"+
									   nrProcesso.substring(9, 13) +"%"+
									   nrProcesso.substring(13, 14) +"%"+
									   nrProcesso.substring(14, 16) +"%"+
									   nrProcesso.substring(16, 20);
	  			}
	  			
	  			pstmt = connection.prepareStatement(
	  					" SELECT cd_processo FROM prc_processo "
	  				  + " WHERE nr_processo LIKE ? " + (nrProcessoPartes!=null ? " OR nr_processo LIKE '"+nrProcessoPartes+"'" : ""));	  
	  			pstmt.setString(1, nrProcesso);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					ignored++;
					logWriter.write("[linha "+count+"]\t Processo "+nrProcesso+" já existe na base, portanto, foi ignorado na importação.");
					logWriter.newLine();
					continue;
				}
				
				
				// TRIBUNAL + + + + + + + + + + 
	  			pstmt = connection.prepareStatement("SELECT cd_tribunal FROM prc_tribunal WHERE nm_tribunal iLIKE ?");
	  			pstmt.setString(1, nmTribunal);
	  			rs = pstmt.executeQuery();
	  			int cdTribunal = rs.next() ? rs.getInt("cd_tribunal") : 0;
	  			
	  			// CIDADE + + + + + + + + + + 
	  			pstmt = connection.prepareStatement(" SELECT A.cd_cidade FROM grl_cidade A "
	  											  + " JOIN grl_estado B ON (A.cd_estado = B.cd_estado)"
	  											  + " WHERE A.nm_cidade iLIKE ? and B.sg_estado = ?");
	  			pstmt.setString(1, nmCidade);
	  			pstmt.setString(2, sgEstado);
	  			rs = pstmt.executeQuery();
	  			int cdCidade = rs.next() ? rs.getInt("cd_cidade") : 0;
	  			
	  			// JUIZO + + + + + + + + + + 
	  			pstmt = connection.prepareStatement("SELECT cd_juizo FROM prc_juizo WHERE nm_juizo iLIKE ?");
	  			pstmt.setString(1, nmJuizo);
	  			rs = pstmt.executeQuery();
	  			int cdJuizo = rs.next() ? rs.getInt("cd_juizo") : 0;
	  			
	  			// CLIENTE + + + + + + + + + + 
	  			pstmt = connection.prepareStatement("SELECT cd_pessoa FROM grl_pessoa WHERE nm_pessoa iLIKE ?");
	  			pstmt.setString(1, nmCliente);
	  			rs = pstmt.executeQuery();
	  			int cdCliente = rs.next() ? rs.getInt("cd_pessoa") : 0;
	  		
	  			ResultSetMap rsmCliente = new ResultSetMap();
	  			HashMap<String, Object> cliente = new HashMap<>();
	  			cliente.put("CD_PESSOA", cdCliente);
	  			rsmCliente.addRegister(cliente);
	  			
	  			// ADVERSO + + + + + + + + + + 
	  			pstmt = connection.prepareStatement(" SELECT A.cd_pessoa FROM grl_pessoa A"
	  											  + " JOIN grl_pessoa_fisica B ON (A.cd_pessoa = B.cd_pessoa)"
	  											  + " WHERE A.nm_pessoa iLIKE ?");
	  			pstmt.setString(1, nmAdverso);
	  			rs = pstmt.executeQuery();
	  			int cdAdverso = 0;
	  			if(rs.next())
	  				cdAdverso = rs.getInt("cd_pessoa");
	  			else if(nrCpfAdverso!=null && !nrCpfAdverso.equalsIgnoreCase("")) {
	  				PessoaFisica adverso = new PessoaFisica();
	  				adverso.setNmPessoa(nmAdverso);
	  				adverso.setGnPessoa(PessoaServices.TP_FISICA);
	  				adverso.setStCadastro(PessoaServices.ST_ATIVO);
	  				adverso.setNrCpf(nrCpfAdverso);
	  				adverso.setDtCadastro(hoje);
	  				adverso.setCdUsuarioCadastro(auth.getUsuario().getCdUsuario());
	  				
	  				int cdVinculo = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_ADVERSO", 0, 0, connection);
	  				
	  				result = PessoaServices.save(adverso, null, auth.getEmpresa().getCdEmpresa(), cdVinculo, connection);
	  				if(result.getCode()>=0) {
	  					adverso = (PessoaFisica)result.getObjects().get("PESSOAFISICA");
	  					cdAdverso = adverso.getCdPessoa();
	  				}
	  			}

	  			ResultSetMap rsmAdverso = new ResultSetMap();
	  			HashMap<String, Object> adverso = new HashMap<>();
	  			adverso.put("CD_PESSOA", cdAdverso);
	  			rsmAdverso.addRegister(adverso);
	  			
	  			// TIPO ACAO + + + + + + + + + + 
	  			pstmt = connection.prepareStatement("SELECT cd_tipo_processo FROM prc_tipo_processo WHERE nm_tipo_processo iLIKE ?");
	  			pstmt.setString(1, nmTipoAcao);
	  			rs = pstmt.executeQuery();
	  			int cdTipoProcesso = rs.next() ? rs.getInt("cd_tipo_processo") : 0;
	  			
	  			// TP RITO + + + + + + + + + + 
	  			int tpRito = -1;
	  			for (int i=0; i<ProcessoServices.tiposRito.length; i++) {
					if(nmTpRito.equalsIgnoreCase(ProcessoServices.tiposRito[i])) {
						tpRito = i;
						break;
					}
				}
	  			
	  			// TP AUTOS + + + + + + + + + + 
	  			int tpAutos = -1;
	  			for (int i=0; i<ProcessoServices.tiposAutos.length; i++) {
					if(nmTpAutos.equalsIgnoreCase(ProcessoServices.tiposAutos[i])) {
						tpAutos = i;
						break;
					}
				}
	  			
	  			// TP REPASSE + + + + + + + + + + 
	  			int tpRepasse = -1;
	  			for (int i=0; i<ProcessoServices.tiposRepasse.length; i++) {
					if(nmTpRepasse.equalsIgnoreCase(ProcessoServices.tiposRepasse[i])) {
						tpRepasse = i;
						break;
					}
				}
	  			
	  			// ADV RESPONSAVEL + + + + + + + + + + 
	  			pstmt = connection.prepareStatement("SELECT cd_pessoa FROM grl_pessoa WHERE nm_pessoa iLIKE ?");
	  			pstmt.setString(1, nmAdvResponsavel);
	  			rs = pstmt.executeQuery();
	  			int cdAdvResponsavel = rs.next() ? rs.getInt("cd_pessoa") : 0;
	  			
	  			// ADV ADVERSO + + + + + + + + + + 
	  			pstmt = connection.prepareStatement("SELECT cd_pessoa FROM grl_pessoa WHERE nm_pessoa iLIKE ?");
	  			pstmt.setString(1, nmAdvAdverso);
	  			rs = pstmt.executeQuery();
	  			int cdAdvAdverso = rs.next() ? rs.getInt("cd_pessoa") : 0;
	  			
	  			// GRUPO DE TRABALHO + + + + + + + + + + 
	  			pstmt = connection.prepareStatement("SELECT cd_grupo FROM agd_grupo WHERE nm_grupo iLIKE ?");
	  			pstmt.setString(1, nmGrupoTrabalho);
	  			rs = pstmt.executeQuery();
	  			int cdGrupoTrabalho = rs.next() ? rs.getInt("cd_grupo") : 0;
	  			
	  			// TIPO OBJETO + + + + + + + + + + 
	  			pstmt = connection.prepareStatement("SELECT cd_tipo_objeto FROM prc_tipo_objeto WHERE nm_tipo_objeto iLIKE ?");
	  			pstmt.setString(1, nmTipoObjeto);
	  			rs = pstmt.executeQuery();
	  			int cdTipoObjeto = rs.next() ? rs.getInt("cd_tipo_objeto") : 0;
	  			
	  			// GRUPO PROCESSO + + + + + + + + + + 
	  			pstmt = connection.prepareStatement("SELECT cd_grupo_processo FROM prc_grupo_processo WHERE nm_grupo_processo iLIKE ?");
	  			pstmt.setString(1, nmGrupoProcesso);
	  			rs = pstmt.executeQuery();
	  			int cdGrupoProcesso = rs.next() ? rs.getInt("cd_grupo_processo") : 0;
	  			
	  			// SISTEMA PROCESSO + + + + + + + + + + 
	  			pstmt = connection.prepareStatement("SELECT cd_sistema_processo FROM prc_sistema_processo WHERE nm_sistema_processo iLIKE ?");
	  			pstmt.setString(1, nmSistemaProcesso);
	  			rs = pstmt.executeQuery();
	  			int cdSistemaProcesso = rs.next() ? rs.getInt("cd_sistema_processo") : 0;
	  			
	  			// validation
	  			if(cdTribunal==0) {
	  				logWriter.write("[linha "+count+"]\t Alerta! O processo "+nrProcesso+" carece de revisão. \"Tribunal\" não possui correspondente na base de dados.");
	  				logWriter.newLine();
	  				logWriter.write("[linha "+count+"]\t \t\tTribunal: "+nmTribunal);
	  				logWriter.newLine();
	  			} if(cdJuizo==0) {
	  				logWriter.write("[linha "+count+"]\t Alerta! O processo "+nrProcesso+" carece de revisão. \"Juízo\" não possui correspondente na base de dados.");
	  				logWriter.newLine();
	  				logWriter.write("[linha "+count+"]\t \t\tJuízo: "+nmJuizo);
	  				logWriter.newLine();
	  			} if(cdCidade==0) {
	  				logWriter.write("[linha "+count+"]\t Alerta! O processo "+nrProcesso+" carece de revisão. \"Cidade\" não possui correspondente na base de dados.");
	  				logWriter.newLine();
	  				logWriter.write("[linha "+count+"]\t \t\tCidade: "+nmCidade+" UF: "+sgEstado);
	  				logWriter.newLine();
	  			} if(cdCliente==0) {
	  				logWriter.write("[linha "+count+"]\t Alerta! O processo "+nrProcesso+" carece de revisão. \"Cliente\" não possui correspondente na base de dados.");
	  				logWriter.newLine();
	  				logWriter.write("[linha "+count+"]\t \t\tCliente: "+nmCliente);
	  				logWriter.newLine();
	  			} if(cdAdverso==0) {
	  				logWriter.write("[linha "+count+"]\t Alerta! O processo "+nrProcesso+" carece de revisão. \"Adverso\" não possui correspondente na base de dados.");
	  				logWriter.newLine();
	  				logWriter.write("[linha "+count+"]\t \t\tAdverso: "+nmAdverso);
	  				logWriter.newLine();
	  			} if(cdAdvResponsavel==0) {
	  				logWriter.write("[linha "+count+"]\t Alerta! O processo "+nrProcesso+" carece de revisão. \"Adv. Responsável\" não possui correspondente na base de dados.");
	  				logWriter.newLine();
	  				logWriter.write("[linha "+count+"]\t \t\tAdv. Responsável: "+nmAdvResponsavel);
	  				logWriter.newLine();
	  			} if(cdTipoProcesso==0) {
	  				logWriter.write("[linha "+count+"]\t Alerta! O processo "+nrProcesso+" carece de revisão. \"Tipo de Processo\" não possui correspondente na base de dados.");
	  				logWriter.newLine();
	  				logWriter.write("[linha "+count+"]\t \t\tTipo de Processo: "+nmTipoAcao);
	  				logWriter.newLine();
	  			} if(cdAdvAdverso==0) {
	  				logWriter.write("[linha "+count+"]\t Alerta! O processo "+nrProcesso+" carece de revisão. \"Adv. Adverso\" não possui correspondente na base de dados.");
	  				logWriter.newLine();
	  				logWriter.write("[linha "+count+"]\t \t\tAdv. Adverso: "+nmAdvAdverso);
	  				logWriter.newLine();
	  			} if(cdGrupoTrabalho==0) {
	  				logWriter.write("[linha "+count+"]\t Alerta! O processo "+nrProcesso+" carece de revisão. \"Grupo de Trabalho\" não possui correspondente na base de dados.");
	  				logWriter.newLine();
	  				logWriter.write("[linha "+count+"]\t \t\tGrupo de Trabalho: "+nmGrupoTrabalho);
	  				logWriter.newLine();
	  			} if(cdTipoObjeto==0) {
	  				logWriter.write("[linha "+count+"]\t Alerta! O processo "+nrProcesso+" carece de revisão. \"Tipo de Objeto\" não possui correspondente na base de dados.");
	  				logWriter.newLine();
	  				logWriter.write("[linha "+count+"]\t \t\tTipo do Objeto: "+nmTipoObjeto);
	  				logWriter.newLine();
	  			} if(cdGrupoProcesso==0) {
	  				logWriter.write("[linha "+count+"]\t Alerta! O processo "+nrProcesso+" carece de revisão. \"Grupo de Processo\" não possui correspondente na base de dados.");
	  				logWriter.newLine();
	  				logWriter.write("[linha "+count+"]\t \t\tGrupo de Processo: "+nmGrupoProcesso);
	  				logWriter.newLine();
	  			} if(cdSistemaProcesso==0) {
	  				logWriter.write("[linha "+count+"]\t Alerta! O processo "+nrProcesso+" carece de revisão. \"Sistema de Processo\" não possui correspondente na base de dados.");
	  				logWriter.newLine();
	  				logWriter.write("[linha "+count+"]\t \t\tSistema do Processo: "+nmSistemaProcesso);
	  				logWriter.newLine();
	  			} if(tpAutos<0) {
	  				logWriter.write("[linha "+count+"]\t Alerta! O processo "+nrProcesso+" carece de revisão. \"Tipo Processo\" não possui correspondente na base de dados.");
	  				logWriter.newLine();
	  				logWriter.write("[linha "+count+"]\t \t\tTipo Processo: "+nmTpAutos);
	  				logWriter.newLine();
	  			} if(tpRito<0) {
	  				logWriter.write("[linha "+count+"]\t Alerta! O processo "+nrProcesso+" carece de revisão. \"Tipo de Rito\" não possui correspondente na base de dados.");
	  				logWriter.newLine();
	  				logWriter.write("[linha "+count+"]\t \t\tTipo de Rito: "+nmTpRito);
	  				logWriter.newLine();
	  			} if(tpRepasse<0) {
	  				logWriter.write("[linha "+count+"]\t Alerta! O processo "+nrProcesso+" carece de revisão. \"Tipo de Repasse\" não possui correspondente na base de dados.");
	  				logWriter.newLine();
	  				logWriter.write("[linha "+count+"]\t \t\tTipo de Repasse: "+nmTpRepasse);
	  				logWriter.newLine();
	  			}
	  				
	  			Processo processo = new Processo(0, 
	  					cdTipoProcesso, 
	  					0, //cdOrgaoJudicial, 
	  					0, //cdComarca, 
	  					1, //cdTipoSituacao, 
	  					cdAdvResponsavel, //cdAdvogado, 
	  					cdAdvAdverso, //cdAdvogadoContrario, 
	  					0, //cdOrgao, 
	  					cdGrupoProcesso, 
	  					1, //lgClienteAutor, 
	  					null, //txtObjeto, 
	  					"Migrado em "+Util.formatDate(hoje, "dd/MM/yyyy"),//txtObservacao, 
	  					dtDistribuicao, 
	  					null, //dtAutuacao, 
	  					null, //txtSentenca, 
	  					null, //dtSentenca, 
	  					ProcessoServices.ST_PROCESSO_ATIVO, //stProcesso, 
	  					hoje, //dtSituacao, 
	  					0.0d, //vlProcesso, 
	  					0.0d, //vlAcordo, 
	  					0, //lgTaxaPapel, 
	  					0, //lgTaxaPaga, 
	  					null, //nrAntigo, 
	  					null, //dtUltimoAndamento, 
	  					0, //qtMaxDias, 
	  					0.0d, //vlSentenca, 
	  					0, //tpInstancia, 
	  					hoje, //dtAtualizacao, 
	  					0, //tpPerda, 
	  					0, //cdAdvogadoTitular, 
	  					0, //cdOficialJustica, 
	  					0, //cdTipoPedido, 
	  					cdTipoObjeto, 
	  					0, //cdResponsavelArquivo, 
	  					hoje, //dtCadastro, 
	  					0, //tpSentenca, 
	  					Integer.toString(nrJuizo), 
	  					cdCidade, 
	  					0, //qtMediaDias, 
	  					null, //dtPrazo, 
	  					0, //lgPrazo, 
	  					null, //nmConteiner1,
	  					null, //nmConteiner2, 
	  					nmConteiner3, //nmConteiner3, 
	  					0, //stLiminar, 
	  					0, //stArquivo, 
	  					dtRepasse, 
	  					0, //cdCentroCusto, 
	  					null, //dtAtualizacaoEdit, 
	  					null, //dtAtualizacaoEdi, 
	  					0, //stAtualizacaoEdi, 
	  					cdTribunal, 
	  					cdJuizo, 
	  					nrProcesso, 
	  					auth.getUsuario().getCdUsuario(), //cdUsuarioCadastro, 
	  					tpAutos, 
	  					null, //nrInterno, 
	  					cdGrupoTrabalho, 
	  					0, //cdProcessoPrincipal, 
	  					null, //idProcesso, 
	  					0, //cdJuiz, 
	  					cdSistemaProcesso, 
	  					tpRito, 
	  					tpRepasse, 
	  					null, //dtInativacao, 
	  					0 //lgImportante
	  				);
		    	
	  			result = ProcessoServices.save(processo, rsmCliente, rsmAdverso, null, auth.getUsuario().getCdUsuario(), null, auth, connection);
	  			
	  			if(result.getCode()<0) {
	  				if(isConnectionNull)
	  					Conexao.rollback(connection);
	  				
	  				logWriter.write("--------------------------------------------------");
	  				logWriter.newLine();
	  				logWriter.write("[linha "+count+"]\t Erro! Não foi possível registrar o processo "+nrProcesso+". Importação abortada.\n");
	  				logWriter.newLine();
	  				logWriter.flush();
	  				logWriter.close();
	  				
	  				return new Result(-1, "Erro na importação!", "log", new String(Files.readAllBytes(logFile.toPath())));
	  			} else {
	  				saved++;
	  				logWriter.write("[linha "+count+"]\t Processo "+nrProcesso+" gravado com sucesso.");
	  				logWriter.newLine();
	  			}
		    }
		    
		    logWriter.write("--------------------------------------------------");
			logWriter.newLine();
		    logWriter.write("Importação concluída.");
			logWriter.newLine();
			logWriter.write(saved+" processos foram registrados e "+ignored+" processos foram ignorados, em um total de "+count+" processos.");
			logWriter.newLine();
		    
		    logWriter.flush();
			logWriter.close();			
			
		    raf.close();
		    
		    if(isConnectionNull)
		    	connection.commit();
			
			return new Result(1, "Processos importados com sucesso", "log", new String(Files.readAllBytes(logFile.toPath())));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			if(isConnectionNull)
				Conexao.rollback(connection);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connection);
			
		}
	}
	
	
	public static Result importAitMovimento() {
		Connection connOrigin = null;
		Connection connDestination = null;
		try {
			
			connOrigin		= Conexao.conectar("org.postgresql.Driver", "jdbc\\:postgresql\\://192.168.1.244/conquista20190215", "postgres", "433uqm!");
			connDestination = Conexao.conectar();
			
			int counter = 0;
			
//			ResultSetMap rsm = connDes
//			
//			while()
			
			
			return new Result(1);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connOrigin);
			Conexao.rollback(connDestination);
			return null;
		} finally {
			Conexao.desconectar(connOrigin);
			Conexao.desconectar(connDestination);
			
		}
	}
	
	public static Result importTabelaCidadeMinas(){
		try{  
			File file=new File("D:\\Transito\\Remessa Detran MG\\Tabelas MG\\municipios.txt");    //creates a new file instance  
			FileReader fr=new FileReader(file);   //reads the file  
			BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
			String line;  
			int cidadesEncontradas = 0;
			int total = 0;
			while((line=br.readLine())!=null){
				total++;
				String codigo = line.substring(0, 5);
				String cidade = line.substring(5);
				System.out.println("Codigo = " + codigo + ", Cidade = " + cidade);
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("NM_CIDADE", cidade.replaceAll("'", "").trim(), ItemComparator.EQUAL, Types.VARCHAR));
				ResultSetMap rsmCidades = CidadeDAO.find(criterios);
				if(rsmCidades.next()){
					Cidade cidadeEncontrada = CidadeDAO.get(rsmCidades.getInt("cd_cidade"));
					cidadeEncontrada.setIdCidade(codigo);
					CidadeDAO.update(cidadeEncontrada);
					
					cidadesEncontradas++;
				}
				else{
					Cidade cidadeNova = new Cidade();
					cidadeNova.setCdEstado(5);
					cidadeNova.setNmCidade(cidade.replaceAll("'", "").trim());
					cidadeNova.setIdCidade(codigo);
					CidadeDAO.insert(cidadeNova);
				}
				
				
			}  
			fr.close();    //closes the stream and release the resources  
			
			System.out.println("Total = " + total);
			System.out.println("Cidades Encontradas = " + cidadesEncontradas);
			
			return new Result(1);
		}  
		catch(IOException e){  
			e.printStackTrace();  
			return new Result(-1, "Erro importação de cidade");
		}  
	}
	
}
