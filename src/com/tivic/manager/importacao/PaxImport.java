package com.tivic.manager.importacao;

import java.sql.*;
import java.util.GregorianCalendar;

import sol.dao.ResultSetMap;

import com.tivic.manager.adm.*;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.*;


public class PaxImport {

	public static sol.util.Result importEnderecamento()	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = conectar();
		try {
			/***********************
			 * Importando CIDADE
			 ***********************/
			PreparedStatement pesqRegiao = connect.prepareStatement("SELECT * FROM grl_regiao WHERE cd_regiao = ?");
			PreparedStatement pesqEstado = connect.prepareStatement("SELECT * FROM grl_estado WHERE sg_estado = ?");
			PreparedStatement pesqCidade = connect.prepareStatement("SELECT * FROM grl_cidade WHERE nm_cidade = ?");
			PreparedStatement pesqDistri = connect.prepareStatement("SELECT * FROM grl_distrito WHERE cd_cidade = ? AND nm_distrito = \'DISTRITO SEDE\'");
			PreparedStatement pesqBairro = connect.prepareStatement("SELECT * FROM grl_bairro WHERE cd_cidade = ? AND nm_bairro = ?");
			PreparedStatement pesqLograd = connect.prepareStatement("SELECT * FROM grl_logradouro WHERE id_logradouro = ?");
			System.out.println("Importando cidades ...");
			//
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

	public static sol.util.Result fromFirebirdToPostgreSQL()	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = conectar();
		try {
			/***********************
			 * DATAPUMP 
			 ***********************/
			String[] tabelas = {"FUNCAO","PARENTESCO","CIDADE","REGIAO","BAIRRO","RUA","PORTADOR","CATEGORIA","SUB_CATEGORIA","EMPRESA","SETOR",
					            "CONTA_CAIXA_CORRENTE","CHEQUES","PRODUTO_SERVICO","FERIADO",
					            "PLANO","PLANO_PAGAMENTO","PLANO_PAGAMENTO_PARCELA","PLANO_TAXA","PLANO_PRD_SRV","PLANO_PLANO_SECUNDARIO",
					            "FUNCIONARIO","FUNCIONARIO_HORARIO","FUNCIONARIO_CAIXA","FUNCIONARIO_COMISSAO","FUNCIONARIO_COBRANCA","RELOGIO_PONTO",
					            "USUARIO","FORNECEDOR","CLIENTE","CLIENTE_ENDERECO","CLIENTE_DEPENDENTE",
					            "CONTRATO","CONTRATO_DEPENDENTE","CONTRATO_PLANO","CONTRATO_PRD_SRV",
					            "CONTA_RECEBER","CONTA_RECEBER_SUB_CATEGORIA","CONTA_RECEBER_ACRESCIMO","COMISSAO_PARCELA",
					            "CONTA_PAGAR","CONTA_PAGAR_SUB_CATEGORIA",
					            "MOVIMENTO_CONTA","MOVIMENTO_CONTA_RECEBER","MOVIMENTO_CONTA_PAGAR","MOVIMENTO_CONTA_SUB_CATEGORIA",
					            "DOCUMENTO","DOCUMENTO_SAIDA","CLIENTE_ATENDIMENTO","ETIQUETAS_RECEBIMENTO","ETIQUETAS_RECEBIMENTO_USADAS",
					            "SAIDA_ITENS","CONFERENCIA_CAIXA","DOCUMENTO_ENTRADA","ENTRADA_ITENS","NEGOCIACAO","PARAMETRO"};
			System.out.println("FIREBIRD -> POSTGRESQL...");
			int geral = 0;
			//
			for(int i=36; i<tabelas.length; i++)	{
				System.out.println("Tabela["+i+"]: "+tabelas[i]);
				ResultSetMap rsmColumns = new ResultSetMap(conOrigem.prepareStatement("select * from RDB$RELATION_FIELDS "
						                                                      +       "where rdb$relation_name = \'"+tabelas[i]+"\'").executeQuery());
				rsmColumns.beforeFirst();
				ResultSet rsOrigem = null;
				// DESTINO
				Statement stmt = connect.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);;
				int row  = 0;
				int skip = 0;
				boolean hasRecords = false;
				do {
					if((geral % 4000) == 0) {
						System.out.println("GARBAGE .... !");
						stmt.close();
						connect.close();
						conOrigem.close();
						System.gc();
						connect   = Conexao.conectar();
						conOrigem = conectar();
						rsmColumns = new ResultSetMap(conOrigem.prepareStatement("select * from RDB$RELATION_FIELDS "
                                                                         +       "where rdb$relation_name = \'"+tabelas[i]+"\'").executeQuery());
						stmt       = connect.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
						rsOrigem   = conOrigem.prepareStatement("SELECT FIRST 4000 SKIP "+skip+" * FROM "+tabelas[i]).executeQuery();
					}
					hasRecords = false;
					ResultSet rsDestino = stmt.executeQuery("SELECT * FROM "+tabelas[i]+" WHERE 1=3");
					while(rsOrigem.next())	{
						row++;
						geral++;
						hasRecords = true;
						rsDestino.moveToInsertRow();
						rsmColumns.beforeFirst();
						while(rsmColumns.next())	{
							rsDestino.updateObject(rsmColumns.getString("RDB$FIELD_NAME").trim(), rsOrigem.getObject(rsmColumns.getString("RDB$FIELD_NAME").trim()));
						}
						try	{
							rsDestino.insertRow();
							if((row % 1000) == 0)
								System.out.println("\t"+row+" registro(s) inseridos!");
						}
						catch(Exception e) { };
					}
					skip += 4000;
				}while(hasRecords);
				System.out.println("\t"+row+" registro(s) inseridos!");
			}
			System.out.println("Importacao concluida com sucesso!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "ERRO em DATAPUMP!", e);
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

	public static Connection conectar()	{
		try{
			Class.forName("org.firebirdsql.jdbc.FBDriver").newInstance();
	  		DriverManager.setLoginTimeout(80);
			return DriverManager.getConnection("jdbc:firebirdsql://127.0.0.1:3050//TIVIC/databases/gecap/gecap.gdb", "sysdba","masterkey");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
}
