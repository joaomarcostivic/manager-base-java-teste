package com.tivic.manager.importacao;

import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import sol.dao.ResultSetMap;
import sol.util.Result;

import com.tivic.manager.adm.ProdutoFornecedor;
import com.tivic.manager.adm.ProdutoFornecedorDAO;
import com.tivic.manager.adm.ProdutoServicoPreco;
import com.tivic.manager.adm.ProdutoServicoPrecoDAO;
import com.tivic.manager.adm.ProdutoServicoPrecoServices;
import com.tivic.manager.alm.Grupo;
import com.tivic.manager.alm.GrupoDAO;
import com.tivic.manager.alm.ProdutoGrupo;
import com.tivic.manager.alm.ProdutoGrupoDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.FormularioAtributoValor;
import com.tivic.manager.grl.Ncm;
import com.tivic.manager.grl.NcmDAO;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaJuridicaDAO;
import com.tivic.manager.grl.Produto;
import com.tivic.manager.grl.ProdutoDAO;
import com.tivic.manager.grl.ProdutoServicoEmpresa;
import com.tivic.manager.grl.ProdutoServicoEmpresaDAO;
import com.tivic.manager.grl.ProdutoServicoEmpresaServices;
import com.tivic.manager.grl.ProdutoServicoServices;
import com.tivic.manager.grl.UnidadeMedida;
import com.tivic.manager.grl.UnidadeMedidaDAO;
import com.tivic.manager.util.Util;

public class ProdutosImport {

	public static sol.util.Result importProdutosAtacadao()	{
		return importProdutos(1, 2, 1, 863, 6, 7);
	}
	
	public static sol.util.Result importProdutos(int cdEmpresa1, int cdTabelaVarejo1, int cdTabelaAtacado1, int cdEmpresa2, int cdTabelaVarejo2, int cdTabelaAtacado2)	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = GenericImport.conectarMySQL();
		try {
			/***********************
			 * Importando produtos
			 ***********************/
			PreparedStatement pesqProduto  = connect.prepareStatement("SELECT * FROM grl_produto_servico A, grl_produto_servico_empresa B " +
													                  "WHERE A.cd_produto_servico = B.cd_produto_servico " +
													                  "  AND B.id_reduzido        = ? " +
													                  "  AND B.cd_empresa IN ("+cdEmpresa1+","+cdEmpresa2+")");
			PreparedStatement pesqProdutoEmp = connect.prepareStatement("SELECT * FROM grl_produto_servico_empresa B " +
								                                        "WHERE cd_produto_servico = ? " +
								                                        "  AND cd_empresa         = ? ");
			PreparedStatement pesqUnidade  = connect.prepareStatement("SELECT * FROM grl_unidade_medida WHERE sg_unidade_medida = ?");
			PreparedStatement pesqGrupo    = connect.prepareStatement("SELECT * FROM alm_grupo WHERE cd_grupo = ?");
			PreparedStatement pesqJuridica = connect.prepareStatement("SELECT * FROM grl_pessoa_juridica WHERE nr_cnpj = ?");
			PreparedStatement pesqPessoaID = connect.prepareStatement("SELECT * FROM grl_pessoa WHERE id_pessoa = ?");
			PreparedStatement updProduto   = connect.prepareStatement("UPDATE grl_produto_servico " +
					                                                  "SET id_produto_servico = ?, cd_classificacao_fiscal=?, cd_ncm =? " +
					                                                  "WHERE cd_produto_servico = ?");
			PreparedStatement pesqNCM      = connect.prepareStatement("SELECT * FROM grl_ncm WHERE nr_ncm = ?");
			PreparedStatement pesqProdFor  = connect.prepareStatement("SELECT * FROM adm_produto_fornecedor WHERE cd_produto_servico = ? AND cd_empresa = ?");
			PreparedStatement updProdutoEmpresa = connect.prepareStatement("UPDATE grl_produto_servico_empresa " +
														                   "SET cd_unidade_medida = ?, id_reduzido = ? " +
														                   "WHERE cd_produto_servico = ? AND cd_empresa = ?");
			System.out.println("Importando produtos...");
			boolean emptyTable = !connect.prepareStatement("SELECT * FROM grl_produto_servico").executeQuery().next();
			//
			ResultSet rs = conOrigem.prepareStatement("SELECT A.*, B.grupo AS nm_grupo, C.empresa AS fornecedor, C.cgc AS nr_cnpj_fornecedor "+
					                                  "FROM tbestoque A "+
					                                  "LEFT OUTER JOIN tbgrupo      B ON (A.grupo = B.controle) "+
					                                  "LEFT OUTER JOIN tbfornecedor C ON (A.CodFornecedor = C.Controle) ").executeQuery();
			System.out.println("Tabela carregada...");
			connect.setAutoCommit(false);
			int l = 0;
			int inserts = 0, updates = 0;
			while(rs.next())	{
				if((l%500==0) && l>0)	{
					System.out.println(l+" registros gravados. [inserts:"+inserts+",updates:"+updates+"]");
					connect.commit();
					connect.close();
					System.gc();
					connect = Conexao.conectar();
					connect.setAutoCommit(false);
					pesqProduto  = connect.prepareStatement("SELECT * FROM grl_produto_servico A, grl_produto_servico_empresa B " +
								                            "WHERE A.cd_produto_servico = B.cd_produto_servico " +
								                            "  AND B.id_reduzido        = ? " +
								                            "  AND B.cd_empresa IN ("+cdEmpresa1+","+cdEmpresa2+")");
					pesqProdutoEmp = connect.prepareStatement("SELECT * FROM grl_produto_servico_empresa B " +
									                          "WHERE cd_produto_servico = ? " +
									                          "  AND cd_empresa         = ? ");					
					pesqUnidade  = connect.prepareStatement("SELECT * FROM grl_unidade_medida WHERE sg_unidade_medida = ?");
					pesqGrupo    = connect.prepareStatement("SELECT * FROM alm_grupo WHERE cd_grupo = ?");
					pesqJuridica = connect.prepareStatement("SELECT * FROM grl_pessoa_juridica WHERE nr_cnpj = ?");
					pesqPessoaID = connect.prepareStatement("SELECT * FROM grl_pessoa WHERE id_pessoa = ?");
					pesqNCM      = connect.prepareStatement("SELECT * FROM grl_ncm WHERE nr_ncm = ?");
					updProduto   = connect.prepareStatement("UPDATE grl_produto_servico " +
								                            "SET id_produto_servico = ?, cd_classificacao_fiscal=?, cd_ncm =? " +
								                            "WHERE cd_produto_servico = ?");
					pesqProdFor  = connect.prepareStatement("SELECT * FROM adm_produto_fornecedor WHERE cd_produto_servico = ? AND cd_empresa = ?");
					updProdutoEmpresa = connect.prepareStatement("UPDATE grl_produto_servico_empresa " +
											                     "SET cd_unidade_medida = ?, id_reduzido = ? " +
											                     "WHERE cd_produto_servico = ? AND cd_empresa = ?");
				}
				l++;
				ResultSet rsProd = null;
				if(!emptyTable)	{
					pesqProduto.setString(1, rs.getString("Codigo"));
					rsProd = pesqProduto.executeQuery();
				}
				// Unidade de Medida
				int cdUnidadeMedida = 0;
				if (rs.getString("Unidade")!=null)	{
					pesqUnidade.setString(1, rs.getString("Unidade").trim().toUpperCase());
					ResultSet rsT = pesqUnidade.executeQuery();
					if(rsT.next())
						cdUnidadeMedida = rsT.getInt("cd_unidade_medida");
					else 
						cdUnidadeMedida = UnidadeMedidaDAO.insert(new UnidadeMedida(0,rs.getString("Unidade").trim().toUpperCase(),
								                                   rs.getString("Unidade").trim().toUpperCase(),null,0,1), connect);
				}
				//
				if(emptyTable || !rsProd.next())	{
					inserts++;
					int cdFabricante = 0;
					// Fabricante
					int cdFornecedor = 0;
					if (rs.getString("fornecedor")!=null)	{
						pesqPessoaID.setString(1, "FOR"+rs.getString("codfornecedor"));
						ResultSet rsT = pesqPessoaID.executeQuery();
						if(rsT.next())
							cdFornecedor = rsT.getInt("cd_pessoa");
						else	{
							String nrCnpj   = rs.getString("nr_cnpj_fornecedor");
							nrCnpj          = nrCnpj!=null ? nrCnpj.replaceAll("[ ]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[.]", "") : null;

							pesqJuridica.setString(1, nrCnpj);
							rsT = pesqJuridica.executeQuery();
							if(rsT.next())
								cdFornecedor = rsT.getInt("cd_pessoa");
						}
					}
					// Grupo
					int cdGrupo = 0;
					if (rs.getString("nm_grupo")!=null)	{
						pesqGrupo.setInt(1, rs.getInt("grupo"));
						ResultSet rsT = pesqGrupo.executeQuery();
						if(rsT.next())	{
							if(rsT.getString("nm_grupo").indexOf("GRUPO")>=0)	{
								System.out.println("Atualizou grupo "+rs.getString("grupo"));
								connect.prepareStatement("UPDATE alm_grupo SET nm_grupo = \'"+rs.getString("nm_grupo")+"\' WHERE cd_grupo = "+rs.getString("grupo")).executeUpdate();
							}
							cdGrupo = rsT.getInt("cd_grupo");
						}
						else	{
							connect.prepareStatement("INSERT INTO alm_grupo (cd_grupo,nm_grupo) VALUES ("+rs.getString("grupo")+",\'"+rs.getString("nm_grupo")+"\')").executeUpdate();
							cdGrupo = Integer.parseInt(rs.getString("grupo"));
						}
					}
					//
					float vlPrecoCusto   = rs.getFloat("PrecoCusto");
					float vlCustoMedio   = rs.getFloat("CustoMedio");
					float vlPrecoAtacado = rs.getFloat("PrecoVenda");
					float vlPrecoVarejo  = rs.getFloat("PrecoPrazo");
					float vlUltimoCusto = 0;
					String dsTamanho = rs.getString("Tamanho");

					Produto produto = new Produto(0,0/*cdCategoriaEconomica*/, rs.getString("Produto"), null /*txtProdutoServico*/,
																null /*txtEspecificacao*/, dsTamanho /*txtDadoTecnico*/, null /*txtPrazoEntrega*/,
																ProdutoServicoServices.TP_PRODUTO, rs.getString("CodigoBarra"), null/*sgProdutoServico*/,
																0 /*cdClassificacaoFiscal*/, cdFabricante, 0/*cdMarca*/, rs.getString("Codigo") /*nmModelo*/, 0/*cdNcm*/, null/*NrRefencia*/,
																0 /*vlPesoUnitario*/,
																0 /*vlPesoUnitarioEmbalagem*/,
																0 /*vlComprimento*/,
																0 /*vlLargura*/,
																0 /*vlAltura*/,
																0 /*vlComprimentoEmbalagem*/,
																0 /*vlLarguraEmbalagem*/,
																0 /*vlAlturaEmbalagem*/,
																0 /*qtEmbalagem*/);
					int cdProdutoServico = ProdutoDAO.insert(produto, connect);

					ProdutoServicoEmpresa produtoEmpresa = new ProdutoServicoEmpresa(cdEmpresa1,cdProdutoServico,cdUnidadeMedida,
							                                                         rs.getString("Codigo")/*idReduzido*/,vlPrecoCusto/*vlPrecoMedio*/,
							                                                         vlCustoMedio,
							                                                         vlUltimoCusto,0/*qtIdeal*/,0/*qtMinima*/,0/*qtMaxima*/,0/*qtDiasEstoque*/,
							                                                         2 /*qtPrecisaoCusto*/, 0/*qtPrecisaoUnidade*/,0 /*qtDiasGarantia*/,
							                                                         ProdutoServicoEmpresaServices.TP_MANUAL,
							                                                         ProdutoServicoEmpresaServices.CTL_QUANTIDADE,
							                                                         0/*tpTransporte*/,1/*stProdutoEmpresa*/,null/*dtDesativacao*/,
							                                                         null/*nrOrdem*/,0/*lgEstoqueNegativo*/);
					ProdutoServicoEmpresaDAO.insert(produtoEmpresa, connect);
					// Incluindo na segunda empresa
					produtoEmpresa.setCdEmpresa(cdEmpresa2);
					ProdutoServicoEmpresaDAO.insert(produtoEmpresa, connect);
					

					if(cdGrupo > 0){
						ProdutoGrupo produtoGrupo1 = new ProdutoGrupo(cdProdutoServico,cdGrupo,cdEmpresa1, 1/*lgPrincipal*/);
						ProdutoGrupoDAO.insert(produtoGrupo1, connect);
						//
						ProdutoGrupo produtoGrupo2 = new ProdutoGrupo(cdProdutoServico,cdGrupo,cdEmpresa2, 1/*lgPrincipal*/);
						ProdutoGrupoDAO.insert(produtoGrupo2, connect);
					}

					if (cdFornecedor > 0)	{
						ProdutoFornecedor fornecedor1 = new ProdutoFornecedor(cdFornecedor,cdEmpresa1,cdProdutoServico,0/*cdRepresentante*/,0,0,0,null,0,0,0);
						ProdutoFornecedorDAO.insert(fornecedor1, connect);
						//
						ProdutoFornecedor fornecedor2 = new ProdutoFornecedor(cdFornecedor,cdEmpresa2,cdProdutoServico,0/*cdRepresentante*/,0,0,0,null,0,0,0);
						ProdutoFornecedorDAO.insert(fornecedor2, connect);
					}
					
					ProdutoServicoPreco preco1_1 = new ProdutoServicoPreco(cdTabelaVarejo1,cdProdutoServico,0,null,vlPrecoVarejo);
					ProdutoServicoPrecoServices.insert(preco1_1, connect);
					ProdutoServicoPreco preco1_2 = new ProdutoServicoPreco(cdTabelaVarejo2,cdProdutoServico,0,null,vlPrecoVarejo);
					ProdutoServicoPrecoServices.insert(preco1_2, connect);
					//
					ProdutoServicoPreco preco2_1 = new ProdutoServicoPreco(cdTabelaAtacado1,cdProdutoServico,0,null,vlPrecoAtacado);
					ProdutoServicoPrecoServices.insert(preco2_1, connect);
					//
					ProdutoServicoPreco preco2_2 = new ProdutoServicoPreco(cdTabelaAtacado2,cdProdutoServico,0,null,vlPrecoAtacado);
					ProdutoServicoPrecoServices.insert(preco2_2, connect);
				}
				else	{
					updates++;
					// Fornecedor
					int cdFornecedor = 0;
					if (rs.getString("fornecedor")!=null)	{
						pesqPessoaID.setString(1, "FOR"+rs.getString("codfornecedor"));
						ResultSet rsT = pesqPessoaID.executeQuery();
						if(rsT.next())
							cdFornecedor = rsT.getInt("cd_pessoa");
						else	{
							String nrCpfCnpj   = rs.getString("nr_cnpj_fornecedor");
							nrCpfCnpj          = nrCpfCnpj!=null ? nrCpfCnpj.replaceAll("[ ]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[.]", "") : null;

							pesqJuridica.setString(1, nrCpfCnpj);
							rsT = pesqJuridica.executeQuery();
							if(rsT.next())
								cdFornecedor = rsT.getInt("cd_pessoa");
							else
								System.out.println("Fornecedor não localizado: "+rs.getString("codfornecedor"));
						}
					}
					int cdProdutoServico = rsProd.getInt("cd_produto_servico");
					int cdNCM            = rsProd.getInt("cd_ncm");
					if (rs.getInt("codNCM") > 0) {
						pesqNCM.setString(1, rs.getString("codNCM"));
						ResultSet rsTemp = pesqNCM.executeQuery();
						if(rsTemp.next()) 
							cdNCM = rsTemp.getInt("cd_ncm");
						else	{
							com.tivic.manager.grl.Ncm ncm = new com.tivic.manager.grl.Ncm(0,rs.getString("codNCM"),0,rs.getString("codNCM"));
							cdNCM = com.tivic.manager.grl.NcmDAO.insert(ncm, connect);
						}
					}
					// Atualizando o produto -> Código de Barras
					updProduto.setString(1, rs.getString("CodigoBarra"));
					updProduto.setInt(2, rs.getInt("key_controle_sit_trib"));
					if(cdNCM > 0)
						updProduto.setInt(3, cdNCM);
					else
						updProduto.setNull(3, java.sql.Types.INTEGER);
					updProduto.setInt(4, cdProdutoServico);
					updProduto.executeUpdate();
					
					/*
					 *  Verificando se o produto está incluido nas duas empresas
					 */
					// EMPRESA 1
					pesqProdutoEmp.setInt(1, rsProd.getInt("cd_produto_servico"));
					pesqProdutoEmp.setInt(2, cdEmpresa1);
					if(!pesqProdutoEmp.executeQuery().next()) {
						float vlPrecoCusto  = rs.getFloat("PrecoCusto");
						float vlCustoMedio  = rs.getFloat("CustoMedio");
						float vlUltimoCusto = 0;
						ProdutoServicoEmpresa produtoEmpresa = new ProdutoServicoEmpresa(cdEmpresa1,cdProdutoServico,cdUnidadeMedida,
														                                rs.getString("Codigo")/*idReduzido*/,vlPrecoCusto/*vlPrecoMedio*/,
														                                vlCustoMedio,
														                                vlUltimoCusto,0/*qtIdeal*/,0/*qtMinima*/,0/*qtMaxima*/,0/*qtDiasEstoque*/,
														                                2 /*qtPrecisaoCusto*/, 0/*qtPrecisaoUnidade*/,0 /*qtDiasGarantia*/,
														                                ProdutoServicoEmpresaServices.TP_MANUAL,
														                                ProdutoServicoEmpresaServices.CTL_QUANTIDADE,
														                                0/*tpTransporte*/,1/*stProdutoEmpresa*/,null/*dtDesativacao*/,
														                                null/*nrOrdem*/,0/*lgEstoqueNegativo*/);
						ProdutoServicoEmpresaDAO.insert(produtoEmpresa, connect);
					}
					else	if (cdUnidadeMedida > 0) {
						updProdutoEmpresa.setInt(1, cdUnidadeMedida);
						updProdutoEmpresa.setString(2, rs.getString("Codigo"));
						updProdutoEmpresa.setInt(3, cdProdutoServico);
						updProdutoEmpresa.setInt(4, cdEmpresa1);
						updProdutoEmpresa.executeUpdate();
					}
					// EMPRESA 2
					pesqProdutoEmp.setInt(1, rsProd.getInt("cd_produto_servico"));
					pesqProdutoEmp.setInt(2, cdEmpresa2);
					if(!pesqProdutoEmp.executeQuery().next()) {
						float vlPrecoCusto  = rs.getFloat("PrecoCusto");
						float vlCustoMedio  = rs.getFloat("CustoMedio");
						float vlUltimoCusto = 0;
						ProdutoServicoEmpresa produtoEmpresa = new ProdutoServicoEmpresa(cdEmpresa2,cdProdutoServico,cdUnidadeMedida,
														                                rs.getString("Codigo")/*idReduzido*/,vlPrecoCusto/*vlPrecoMedio*/,
														                                vlCustoMedio,
														                                vlUltimoCusto,0/*qtIdeal*/,0/*qtMinima*/,0/*qtMaxima*/,0/*qtDiasEstoque*/,
														                                2 /*qtPrecisaoCusto*/, 0/*qtPrecisaoUnidade*/,0 /*qtDiasGarantia*/,
														                                ProdutoServicoEmpresaServices.TP_MANUAL,
														                                ProdutoServicoEmpresaServices.CTL_QUANTIDADE,
														                                0/*tpTransporte*/,1/*stProdutoEmpresa*/,null/*dtDesativacao*/,
														                                null/*nrOrdem*/,0/*lgEstoqueNegativo*/);
						ProdutoServicoEmpresaDAO.insert(produtoEmpresa, connect);
					}
					else	if (cdUnidadeMedida > 0) {
						updProdutoEmpresa.setInt(1, cdUnidadeMedida);
						updProdutoEmpresa.setString(2, rs.getString("Codigo"));
						updProdutoEmpresa.setInt(3, cdProdutoServico);
						updProdutoEmpresa.setInt(4, cdEmpresa2);
						updProdutoEmpresa.executeUpdate();
					}
					// Fornecedor
					if (cdFornecedor > 0){
						pesqProdFor.setInt(1, rsProd.getInt("cd_produto_servico"));
						pesqProdFor.setInt(2, cdEmpresa1);
						if(!pesqProdFor.executeQuery().next()){
							ProdutoFornecedor fornecedor1 = new ProdutoFornecedor(cdFornecedor,cdEmpresa1,cdProdutoServico,0/*cdRepresentante*/,0,0,0,null,0,0,0);
							ProdutoFornecedorDAO.insert(fornecedor1, connect);
						}
						//
						pesqProdFor.setInt(1, rsProd.getInt("cd_produto_servico"));
						pesqProdFor.setInt(2, cdEmpresa2);
						if(!pesqProdFor.executeQuery().next()){
							ProdutoFornecedor fornecedor1 = new ProdutoFornecedor(cdFornecedor,cdEmpresa2,cdProdutoServico,0/*cdRepresentante*/,0,0,0,null,0,0,0);
							ProdutoFornecedorDAO.insert(fornecedor1, connect);
						}
					}
				}
			}
			connect.commit();
			System.out.println("Importação de produtos concluída!");
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
	
	public static sol.util.Result importProdutosFromTxt(int cdEmpresa, int cdTabelaPreco)	{
		Connection connect   = Conexao.conectar();
		RandomAccessFile raf = null;
		try {
			/***********************
			 * Importando produtos
			 ***********************/
			PreparedStatement pesqProduto  = connect.prepareStatement("SELECT * FROM grl_produto_servico WHERE nm_modelo = ?");
			PreparedStatement pesqUnidade  = connect.prepareStatement("SELECT * FROM grl_unidade_medida WHERE sg_unidade_medida = ?");
			PreparedStatement pesqGrupo    = connect.prepareStatement("SELECT * FROM alm_grupo WHERE nm_grupo = ?");
			PreparedStatement updProduto   = connect.prepareStatement("UPDATE grl_produto_servico SET id_produto_servico = ? WHERE cd_produto_servico = ?");
			PreparedStatement updProdutoEmpresa = connect.prepareStatement("UPDATE grl_produto_servico_empresa " +
														                   "SET cd_unidade_medida = ?, id_reduzido = ? " +
														                   "WHERE cd_produto_servico = ? AND cd_empresa = ?");
			System.out.println("Importando produtos de arquivo TXT...");
			boolean emptyTable = !connect.prepareStatement("SELECT * FROM grl_produto_servico").executeQuery().next();
			//
			String nmArquivo = "users/leo/desktop/intima.txt", line = "";
			raf = new RandomAccessFile("c:/"+nmArquivo, "rw");
	  		System.out.println("Arquivo carregado...");
			connect.setAutoCommit(false);
			int l = 0;
			int inserts = 0, updates = 0;
	  		while((line = raf.readLine()) != null)	{
				if((l%500==0) && l>0)	{
					System.out.println(l+" registros gravados. [inserts:"+inserts+",updates:"+updates+"]");
					connect.commit();
					connect.close();
					System.gc();
					connect = Conexao.conectar();
					connect.setAutoCommit(false);
					pesqProduto = connect.prepareStatement("SELECT * FROM grl_produto_servico WHERE nm_modelo = ?");
					pesqUnidade = connect.prepareStatement("SELECT * FROM grl_unidade_medida WHERE sg_unidade_medida = ?");
					pesqGrupo   = connect.prepareStatement("SELECT * FROM alm_grupo WHERE nm_grupo = ?");
					updProduto   = connect.prepareStatement("UPDATE grl_produto_servico SET id_produto_servico = ? WHERE cd_produto_servico = ?");
					updProdutoEmpresa   = connect.prepareStatement("UPDATE grl_produto_servico_empresa " +
							                                       "SET cd_unidade_medida = ?, id_reduzido = ? " +
							                                       "WHERE cd_produto_servico = ? AND cd_empresa = ?");
				}
				//
				String idReduzido   = line.substring(0,6).trim();
				String nmProduto    = line.substring(6, 52).trim();
				String sgUnidade    = line.substring(52,56).trim();
				String dsPreco      = line.substring(56,67).trim().replaceAll(",", "\\.");
				String dsCusto      = line.substring(67,78).trim().replaceAll(",", "\\.");
				String dsQuantidade = line.substring(100,111).trim().replaceAll(",", "\\.");
				String dsCIT        = line.substring(122,126).trim();
				l++;
				
				ResultSet rsProd = null;
				if(!emptyTable){
					pesqProduto.setString(1, idReduzido);
					rsProd = pesqProduto.executeQuery();
				}
				if(emptyTable || !rsProd.next())	{
					inserts++;
					int cdFabricante = 0;
					// Fornecedor
					int cdFornecedor = 0;
					// Grupo
					String nmGrupo = "";
					int cdGrupo = 0;
					if (nmGrupo!=null)	{
						pesqGrupo.setString(1, nmGrupo);
						ResultSet rsT = pesqGrupo.executeQuery();
						if(rsT.next())
							cdGrupo = rsT.getInt("cd_grupo");
						else	{
							com.tivic.manager.alm.Grupo grupo = new com.tivic.manager.alm.Grupo(0,0,0,0,nmGrupo,0,0,0,1,null);
							cdGrupo = com.tivic.manager.alm.GrupoDAO.insert(grupo, connect);
						}
					}
					// Unidade de Medida
					int cdUnidadeMedida = 0;
					if (sgUnidade!=null)	{
						pesqUnidade.setString(1, sgUnidade.toUpperCase());
						ResultSet rsT = pesqUnidade.executeQuery();
						if(rsT.next())
							cdUnidadeMedida = rsT.getInt("cd_unidade_medida");
						else
							System.out.println("Unidade de medida não localizada: "+sgUnidade.trim());
					}
					//
					float vlPrecoCusto  = Float.parseFloat(dsCusto);
					float vlPrecoVenda  = Float.parseFloat(dsPreco);
					String dsTamanho    = "";

					Produto produto = new Produto(0,0/*cdCategoriaEconomica*/, nmProduto, null /*txtProdutoServico*/,
												  null /*txtEspecificacao*/, dsTamanho /*txtDadoTecnico*/, null /*txtPrazoEntrega*/,
												  ProdutoServicoServices.TP_PRODUTO, null, null/*sgProdutoServico*/,
												  0 /*cdClassificacaoFiscal*/, cdFabricante, 0/*cdMarca*/, dsCIT, 0/*cdNcm*/, null/*NrReferenci*/,
												  0 /*vlPesoUnitario*/, 0 /*vlPesoUnitarioEmbalagem*/, 0 /*vlComprimento*/, 0 /*vlLargura*/,
												  0 /*vlAltura*/, 0 /*vlComprimentoEmbalagem*/, 0 /*vlLarguraEmbalagem*/, 
												  Float.parseFloat(dsQuantidade) /*vlAlturaEmbalagem*/,
												  0 /*qtEmbalagem*/);
					int cdProdutoServico = ProdutoDAO.insert(produto, connect);

					ProdutoServicoEmpresa produtoEmpresa = new ProdutoServicoEmpresa(cdEmpresa,cdProdutoServico,cdUnidadeMedida,
							                                                         idReduzido,vlPrecoCusto/*vlPrecoMedio*/,
							                                                         vlPrecoCusto/*vlCustoMedio*/,
							                                                         vlPrecoCusto/*vlUltimoCusto*/,0/*qtIdeal*/,0/*qtMinima*/,0/*qtMaxima*/,0/*qtDiasEstoque*/,
							                                                         2 /*qtPrecisaoCusto*/, 0/*qtPrecisaoUnidade*/,0 /*qtDiasGarantia*/,
							                                                         ProdutoServicoEmpresaServices.TP_MANUAL,
							                                                         ProdutoServicoEmpresaServices.CTL_QUANTIDADE,
							                                                         0/*tpTransporte*/,1/*stProdutoEmpresa*/,null/*dtDesativacao*/,
							                                                         null/*nrOrdem*/,0/*lgEstoqueNegativo*/);
					ProdutoServicoEmpresaDAO.insert(produtoEmpresa, connect);

					if(cdGrupo > 0){
						ProdutoGrupo produtoGrupo = new ProdutoGrupo(cdProdutoServico,cdGrupo,cdEmpresa,1/*lgPrincipal*/);

						ProdutoGrupoDAO.insert(produtoGrupo, connect);
					}

					if (cdFornecedor > 0){
						ProdutoFornecedor fornecedor = new ProdutoFornecedor(cdFornecedor,cdEmpresa,cdProdutoServico,0/*cdRepresentante*/,0,0,0,null,0,0,0);
						ProdutoFornecedorDAO.insert(fornecedor, connect);
					}

					ProdutoServicoPreco preco1 = new ProdutoServicoPreco(cdTabelaPreco,cdProdutoServico,0,null,vlPrecoVenda);
					ProdutoServicoPrecoDAO.insert(preco1, connect);
				}
				else	{
					updates++;
					// Unidade de Medida
					int cdUnidadeMedida = 0;
					if (sgUnidade!=null)	{
						pesqUnidade.setString(1, sgUnidade.trim().toUpperCase());
						ResultSet rsT = pesqUnidade.executeQuery();
						if(rsT.next())
							cdUnidadeMedida = rsT.getInt("cd_unidade_medida");
						else
							System.out.println("Unidade de medida não localizada: "+sgUnidade.trim());
					}
					// Fornecedor
					int cdProdutoServico = rsProd.getInt("cd_produto_servico");
					// Atualizando o produto
					updProduto.setString(1, null);
					updProduto.setInt(2, cdProdutoServico);
					updProduto.executeUpdate();
					// Atualizando unidade de medida
					if (cdUnidadeMedida > 0) {
						updProdutoEmpresa.setInt(1, cdUnidadeMedida);
						updProdutoEmpresa.setString(2, idReduzido);
						updProdutoEmpresa.setInt(3, cdProdutoServico);
						updProdutoEmpresa.setInt(4, cdEmpresa);
						updProdutoEmpresa.executeUpdate();
					}
				}
			}
			connect.commit();
			System.out.println("Importação de produtos concluída!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar pessoas!", e);
		}
		finally{
			if(raf!=null)
		  		try {raf.close(); } catch(Exception e){};
			Conexao.desconectar(connect);
		}
	}
	
	public static sol.util.Result importNcmFromTxt()	{
		Connection connect   = Conexao.conectar();
		RandomAccessFile raf = null;
		try {
			/***********************
			 * Importando produtos
			 ***********************/
			PreparedStatement pesqNumeroNcm  = connect.prepareStatement("SELECT * FROM grl_ncm WHERE nr_ncm = ?");
			PreparedStatement updateNomeNcm  = connect.prepareStatement("UPDATE grl_ncm set nm_ncm = ? WHERE cd_ncm = ?");
			
			System.out.println("Importando Ncms de arquivo TXT...");
			boolean emptyTable = !connect.prepareStatement("SELECT * FROM grl_ncm").executeQuery().next();
			//
			String nmArquivo = "ncm.txt", line = "";
			raf = new RandomAccessFile("c:/" + nmArquivo, "rw");
	  		System.out.println("Arquivo carregado...");
			connect.setAutoCommit(false);
			int l = 0;
			int inserts = 0, updates = 0;
			while((line = raf.readLine()) != null)	{
				if((l%500==0) && l>0)	{
					System.out.println(l+" registros gravados. [inserts:"+inserts+",updates:"+updates+"]");
					connect.commit();
					connect.close();
					System.gc();
					connect = Conexao.conectar();
					connect.setAutoCommit(false);
					pesqNumeroNcm = connect.prepareStatement("SELECT * FROM grl_ncm WHERE nr_ncm = ?");
					updateNomeNcm = connect.prepareStatement("UPDATE grl_ncm set nm_ncm = ? WHERE cd_ncm = ?");
				}
				String nrNcm        = line.substring(0,8).trim();
				String nmNcm        = line.substring(11).trim();

				if(nmNcm.length() > 49)
					nmNcm = nmNcm.substring(0, 49);
				
				l++;
				
				ResultSet rsNcm = null;
				if(!emptyTable){
					pesqNumeroNcm.setString(1, nrNcm);
					rsNcm = pesqNumeroNcm.executeQuery();
				}
				if(emptyTable || !rsNcm.next())	{
					inserts++;
					// Unidade de Medida
					int cdUnidadeMedida = 1;
					Ncm ncm = new Ncm(0, nmNcm, cdUnidadeMedida, nrNcm);
							
					NcmDAO.insert(ncm, connect);

				}
				else{
					updates++;
					int cdNcm = rsNcm.getInt("cd_ncm");
					// Atualizando o produto
					updateNomeNcm.setString(1, nmNcm);
					updateNomeNcm.setInt(2, cdNcm);
					updateNomeNcm.executeUpdate();
					
				}
			}
			connect.commit();
			System.out.println("Importação de ncms concluída!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar pessoas!", e);
		}
		finally{
			if(raf!=null)
		  		try {raf.close(); } catch(Exception e){};
			Conexao.desconectar(connect);
		}
	}
	
	public static sol.util.Result importProdutosFromTxtCSV(int cdEmpresa, int cdTabelaPreco)	{
		Connection connect   = Conexao.conectar();
		RandomAccessFile raf = null;
		try {
			/***********************
			 * Importando produtos
			 ***********************/
			PreparedStatement pesqProdutoServico         = connect.prepareStatement("SELECT * FROM grl_produto_servico WHERE nm_produto_servico = ?");
			PreparedStatement pesqNcm  					 = connect.prepareStatement("SELECT * FROM grl_ncm WHERE nr_ncm = ?");
			PreparedStatement pesqUnidade  				 = connect.prepareStatement("SELECT * FROM grl_unidade_medida WHERE sg_unidade_medida = ?");
			PreparedStatement pesqGrupo    				 = connect.prepareStatement("SELECT * FROM alm_grupo WHERE id_grupo = ?");
			PreparedStatement updProduto   = connect.prepareStatement("UPDATE grl_produto_servico SET cd_ncm = ? WHERE cd_produto_servico = ?");
			PreparedStatement updProdutoEmpresa = connect.prepareStatement("UPDATE grl_produto_servico_empresa " +
														                   "SET cd_unidade_medida = ?, id_reduzido = ? " +
														                   "WHERE cd_produto_servico = ? AND cd_empresa = ?");
			System.out.println("Importando produtos de arquivo TXT...");
			boolean emptyTable = !connect.prepareStatement("SELECT * FROM grl_produto_servico").executeQuery().next();
			//
			String nmArquivo = "produtos.txt", line = "";
			raf = new RandomAccessFile("c:/"+nmArquivo, "rw");
	  		System.out.println("Arquivo carregado...");
			connect.setAutoCommit(false);
			int l = 0;
			int inserts = 0, updates = 0;
	  		while((line = raf.readLine()) != null)	{
	  			if((l%500==0) && l>0)	{
					System.out.println(l+" registros gravados. [inserts:"+inserts+",updates:"+updates+"]");
					connect.commit();
					connect.close();
					System.gc();
					connect = Conexao.conectar();
					connect.setAutoCommit(false);
					pesqProdutoServico = connect.prepareStatement("SELECT * FROM grl_produto_servico WHERE nm_produto_servico = ?");
					pesqNcm  		   = connect.prepareStatement("SELECT * FROM grl_ncm WHERE nr_ncm = ?");
					pesqUnidade  	   = connect.prepareStatement("SELECT * FROM grl_unidade_medida WHERE sg_unidade_medida = ?");
					pesqGrupo    	   = connect.prepareStatement("SELECT * FROM alm_grupo WHERE id_grupo = ?");
					updProduto         = connect.prepareStatement("UPDATE grl_produto_servico SET cd_ncm = ? WHERE cd_produto_servico = ?");
					updProdutoEmpresa  = connect.prepareStatement("UPDATE grl_produto_servico_empresa " +
																  "SET cd_unidade_medida = ?, id_reduzido = ? " +
																  "WHERE cd_produto_servico = ? AND cd_empresa = ?");
				}
				//
				String[] fields = new String[] {"idReduzido", "nrNcm", "sp1", "sp2", "sp3", "sp4", "sp5",
						                        "nmProdutoServico", "qtProdutoServico", "sgUnidadeMedida", "vlPreco"};
				StringTokenizer tokens     = new StringTokenizer(line, ";", false);
				HashMap<String,String> reg = new HashMap<String,String>();
	  			int f = 0;
	  			while(tokens.hasMoreTokens())	{
	  				if(f>=fields.length)
	  					break;
	  				reg.put(fields[f], tokens.nextToken());
	  				f++;
	  			}
				String idReduzido   = reg.get("idReduzido");
				String idGrupo      = "";
				if(idReduzido!=null)	{
					idReduzido = idReduzido.replaceAll("\\.", "-");
					if(idReduzido.indexOf('-')>0)
						idGrupo = reg.get("idReduzido").substring(idReduzido.indexOf('-')+1);
					// idReduzido = idReduzido.replaceAll("-", "");
				}
				String nmProduto    = reg.get("nmProdutoServico").trim();
				String sgUnidade    = reg.get("sgUnidadeMedida").trim();
				String nrNcm        = reg.get("nrNcm").trim();
				float vlPreco       = 0;
				l++;
				// Unidade de Medida
				int cdUnidadeMedida = 0;
				if (sgUnidade!=null)	{
					pesqUnidade.setString(1, sgUnidade.toUpperCase());
					ResultSet rsT = pesqUnidade.executeQuery();
					if(rsT.next())
						cdUnidadeMedida = rsT.getInt("cd_unidade_medida");
					else{
						com.tivic.manager.grl.UnidadeMedida unidadeMedida = new com.tivic.manager.grl.UnidadeMedida(0, sgUnidade, sgUnidade, null, 0, 1);
						cdUnidadeMedida = com.tivic.manager.grl.UnidadeMedidaDAO.insert(unidadeMedida, connect);
					}
				}
				// Grupo
				int cdGrupo = 0;
				if (!idGrupo.equals(""))	{
					pesqGrupo.setString(1, idGrupo);
					ResultSet rsT = pesqGrupo.executeQuery();
					if(rsT.next())
						cdGrupo = rsT.getInt("cd_grupo");
					else{
						com.tivic.manager.alm.Grupo grupo = new com.tivic.manager.alm.Grupo(0,0,0,0,"GRUPO "+idGrupo,0,0,0,1,idGrupo);
						cdGrupo = com.tivic.manager.alm.GrupoDAO.insert(grupo, connect);
					}
						
				}
				// NCM
				int cdNcm = 0;
				if (nrNcm!=null)	{
					pesqNcm.setString(1, nrNcm.toUpperCase());
					ResultSet rsN = pesqNcm.executeQuery();
					if(rsN.next())
						cdNcm = rsN.getInt("cd_ncm");
					else	{
						com.tivic.manager.grl.Ncm ncm = new com.tivic.manager.grl.Ncm(0, nrNcm, cdUnidadeMedida, nrNcm);
						cdNcm = com.tivic.manager.grl.NcmDAO.insert(ncm, connect);
					}
						
				}
				// Se a tabela não estava vazia no início, pesquisa o produto
				ResultSet rsProd = null;
				if(!emptyTable){
					pesqProdutoServico.setString(1, nmProduto);
					rsProd = pesqProdutoServico.executeQuery();
				}
				// Incluindo
				if(emptyTable || !rsProd.next())	{
					inserts++;
					Produto produto = new Produto(0,0/*cdCategoriaEconomica*/, nmProduto, null /*txtProdutoServico*/,
												  null /*txtEspecificacao*/, null /*txtDadoTecnico*/, null /*txtPrazoEntrega*/,
												  ProdutoServicoServices.TP_PRODUTO, null, null/*sgProdutoServico*/,
												  0 /*cdClassificacaoFiscal*/, 0, 0/*cdMarca*/, null, cdNcm/*cdNcm*/, null/*NrReferenci*/,
												  0 /*vlPesoUnitario*/, 0 /*vlPesoUnitarioEmbalagem*/, 0 /*vlComprimento*/, 0 /*vlLargura*/,
												  0 /*vlAltura*/, 0 /*vlComprimentoEmbalagem*/, 0 /*vlLarguraEmbalagem*/, 
												  0 /*vlAlturaEmbalagem*/,
												  0 /*qtEmbalagem*/);
					int cdProdutoServico = ProdutoDAO.insert(produto, connect);

					ProdutoServicoEmpresa produtoEmpresa = new ProdutoServicoEmpresa(cdEmpresa,cdProdutoServico,cdUnidadeMedida,
							                                                         idReduzido,0/*vlPrecoMedio*/,
							                                                         0/*vlCustoMedio*/,
							                                                         0/*vlUltimoCusto*/,0/*qtIdeal*/,0/*qtMinima*/,0/*qtMaxima*/,0/*qtDiasEstoque*/,
							                                                         2 /*qtPrecisaoCusto*/, 0/*qtPrecisaoUnidade*/,0 /*qtDiasGarantia*/,
							                                                         ProdutoServicoEmpresaServices.TP_MANUAL,
							                                                         ProdutoServicoEmpresaServices.CTL_QUANTIDADE,
							                                                         0/*tpTransporte*/,1/*stProdutoEmpresa*/,null/*dtDesativacao*/,
							                                                         null/*nrOrdem*/,0/*lgEstoqueNegativo*/);
					ProdutoServicoEmpresaDAO.insert(produtoEmpresa, connect);

					if(cdGrupo > 0){
						ProdutoGrupo produtoGrupo = new ProdutoGrupo(cdProdutoServico,cdGrupo,cdEmpresa,1/*lgPrincipal*/);

						ProdutoGrupoDAO.insert(produtoGrupo, connect);
					}
					if(vlPreco > 0)	{
						ProdutoServicoPreco preco1 = new ProdutoServicoPreco(cdTabelaPreco,cdProdutoServico,0,null,vlPreco);
						ProdutoServicoPrecoDAO.insert(preco1, connect);
					}
				}
				else	{
					updates++;
					int cdProdutoServico = rsProd.getInt("cd_produto_servico");
					// Atualizando o produto
					updProduto.setInt(1, cdNcm);
					updProduto.setInt(2, cdProdutoServico);
					updProduto.executeUpdate();
					// Atualizando unidade de medida
					if (cdUnidadeMedida > 0) {
						updProdutoEmpresa.setInt(1, cdUnidadeMedida);
						updProdutoEmpresa.setString(2, idReduzido);
						updProdutoEmpresa.setInt(3, cdProdutoServico);
						updProdutoEmpresa.setInt(4, cdEmpresa);
						updProdutoEmpresa.executeUpdate();
					}
				}
			}
			connect.commit();
			System.out.println("Importação de produtos concluída!");
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
	
    public static sol.util.Result importProdutosFromTxtCSVSADDE()	{
    	int cdEmpresa     = 3;
    	int cdTabelaPreco = 1;
		Connection connect   = Conexao.conectar();
		try {
			/***********************
			 * Importando produtos
			 ***********************/
			PreparedStatement pesqProdutoServico         = connect.prepareStatement("SELECT * FROM grl_produto_servico_empresa WHERE id_reduzido = ?");
			PreparedStatement pesqPessoa	             = connect.prepareStatement("SELECT * FROM grl_pessoa WHERE nm_pessoa = ?");
			PreparedStatement pesqPessoaProduto          = connect.prepareStatement("SELECT * FROM adm_produto_fornecedor WHERE cd_produto_servico = ? AND cd_fornecedor = ?");
			PreparedStatement pesqUnidade  				 = connect.prepareStatement("SELECT * FROM grl_unidade_medida WHERE sg_unidade_medida = ?");
			PreparedStatement pesqGrupo                  = connect.prepareStatement("SELECT * FROM alm_grupo WHERE nm_grupo = ?");
			
			System.out.println("Importando produtos de arquivo TXT...");
			//
			ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile("c:/produtos.csv", ";", true);
	  		System.out.println("Arquivo carregado...");
			connect.setAutoCommit(false);
	  		while(rsm.next())	{
				//
				String nmGrupo                 = rsm.getString("GRUPO");//grl_produto_servico_empresa
				String idReduzido              = rsm.getString("CODIGO");//grl_produto_servico_empresa
				String nrReferencia            = rsm.getString("REFERENCIA");//grl_produto_servico
				String txtDadoTecnico          = rsm.getString("GRADE");//grl_produto_servico_empresa - txt_especificacao
				String txtEspecificacao        = rsm.getString("GENERO");//grl_produto_servico_empresa - txt_especificacao
				String nmProdutoServico        = rsm.getString("NOME");//grl_produto_servico
				String nmRazaoSocialFornecedor = rsm.getString("FORNECEDOR");//grl_pessoa_juridica - Colocar em caixa_alta
				String sgUnidadeMedida         = "UND"; // rsm.getString("MEDIDA");//grl_unidade_medida
				String vlPreco                 = (rsm.getString("PRECO") != null && !rsm.getString("PRECO").equals("".trim())) ? rsm.getString("PRECO") : "0";//adm_produto_servico_preco
				String vlUltimoCusto           = (rsm.getString("CUSTO") != null && !rsm.getString("CUSTO").equals("".trim())) ? rsm.getString("CUSTO") : "0";//grl_produto_servico_empresa
				
				System.out.println("idReduzido = " + idReduzido + ", nrReferencia = " + nrReferencia + ", txtEspecificacao = " + txtEspecificacao + ", nmProdutoServico = " + nmProdutoServico + ", nmRazaoSocialFornecedor = " + nmRazaoSocialFornecedor + ", sgUnidadeMedida = " + sgUnidadeMedida + ", vlPreco = " + vlPreco + ", vlCustoMedio = " + vlUltimoCusto + ", vlPrecoMedio = " + vlUltimoCusto);
				
				
				// Unidade de Medida
				int cdUnidadeMedida = 0;
				if (sgUnidadeMedida!=null && !sgUnidadeMedida.trim().equals(""))	{
					pesqUnidade.setString(1, sgUnidadeMedida.toUpperCase());
					ResultSet rsT = pesqUnidade.executeQuery();
					if(rsT.next())
						cdUnidadeMedida = rsT.getInt("cd_unidade_medida");
					else{
						com.tivic.manager.grl.UnidadeMedida unidadeMedida = new com.tivic.manager.grl.UnidadeMedida(0, sgUnidadeMedida, sgUnidadeMedida, null, 0, 1);
						cdUnidadeMedida = com.tivic.manager.grl.UnidadeMedidaDAO.insert(unidadeMedida, connect);
						if(cdUnidadeMedida <= 0){
							connect.rollback();
							return new Result(-1 , "Erro: Unidade de Medida");
						}
					}
				}
				
				// Se a tabela não estava vazia no início, pesquisa o produto
				pesqProdutoServico.setString(1, idReduzido);
				ResultSet rsProd = pesqProdutoServico.executeQuery();
				
				// Incluindo
				if(!rsProd.next())	{
//					inserts++;
					Produto produto = new Produto(0,0/*cdCategoriaEconomica*/, nmProdutoServico.toUpperCase(), null /*txtProdutoServico*/,
												  txtEspecificacao /*txtEspecificacao*/, txtDadoTecnico, null /*txtPrazoEntrega*/,
												  ProdutoServicoServices.TP_PRODUTO, null, null/*sgProdutoServico*/,
												  0 /*cdClassificacaoFiscal*/, 0, 0/*cdMarca*/, null, 0/*cdNcm*/, nrReferencia/*NrReferenci*/,
												  0 /*vlPesoUnitario*/, 0 /*vlPesoUnitarioEmbalagem*/, 0 /*vlComprimento*/, 0 /*vlLargura*/,
												  0 /*vlAltura*/, 0 /*vlComprimentoEmbalagem*/, 0 /*vlLarguraEmbalagem*/, 
												  0 /*vlAlturaEmbalagem*/,
												  0 /*qtEmbalagem*/);
					int cdProdutoServico = ProdutoDAO.insert(produto, connect);
//					System.out.println(cdProdutoServico);
					if(cdProdutoServico <= 0){
						connect.rollback();
						return new Result(-1, "Erro: ProdutoServico");
					}
					ProdutoServicoEmpresa produtoEmpresa = new ProdutoServicoEmpresa(cdEmpresa,cdProdutoServico,cdUnidadeMedida,
							                                                         idReduzido,Float.parseFloat(vlUltimoCusto.replaceAll(",", "."))/*vlPrecoMedio*/,
							                                                         Float.parseFloat(vlUltimoCusto.replaceAll(",", "."))/*vlCustoMedio*/,
							                                                         Float.parseFloat(vlUltimoCusto.replaceAll(",", "."))/*vlUltimoCusto*/,0/*qtIdeal*/,0/*qtMinima*/,0/*qtMaxima*/,0/*qtDiasEstoque*/,
							                                                         2 /*qtPrecisaoCusto*/, 0/*qtPrecisaoUnidade*/,0 /*qtDiasGarantia*/,
							                                                         ProdutoServicoEmpresaServices.TP_MANUAL,
							                                                         ProdutoServicoEmpresaServices.CTL_QUANTIDADE,
							                                                         0/*tpTransporte*/,1/*stProdutoEmpresa*/,null/*dtDesativacao*/,
							                                                         null/*nrOrdem*/,0/*lgEstoqueNegativo*/);
					if(ProdutoServicoEmpresaDAO.insert(produtoEmpresa, connect) <= 0){
						connect.rollback();
						return new Result(-1, "Erro ProdutoServicoEmpresa");
					}
					// Grupo
					int cdGrupo = 0;
					if (nmGrupo!=null)	{
						pesqGrupo.setString(1, nmGrupo);
						ResultSet rsGrupo = pesqGrupo.executeQuery();
						if(rsGrupo.next())	{
							cdGrupo = rsGrupo.getInt("cd_grupo");
						}
						else	{
							Grupo grupo = new Grupo(0, 0, 0, 0, nmGrupo, 0, 0, 0, 1, "");
							cdGrupo = GrupoDAO.insert(grupo, connect);
						}
					}
					if(cdGrupo > 0)	{
						ProdutoGrupo produtoGrupo = new ProdutoGrupo(cdProdutoServico,cdGrupo,cdEmpresa,1/*lgPrincipal*/);

						ProdutoGrupoDAO.insert(produtoGrupo, connect);
					}
					// PREÇO
					if(Float.parseFloat(vlPreco.replaceAll(",", ".")) > 0)	{
						ProdutoServicoPreco preco1 = new ProdutoServicoPreco(cdTabelaPreco,cdProdutoServico,0,null,Float.parseFloat(vlPreco.replaceAll(",", ".")));
						if(ProdutoServicoPrecoDAO.insert(preco1, connect) <= 0){
							connect.rollback();
							return new Result(-1, "Erro ProdutoServicoPreco");
						}
					}
					// FORNECEDOR
					int cdFornecedor = 0;
					if (nmRazaoSocialFornecedor!=null && !nmRazaoSocialFornecedor.trim().equals(""))	{
						pesqPessoa.setString(1, nmRazaoSocialFornecedor.toUpperCase());
						ResultSet rsT = pesqPessoa.executeQuery();
						if(rsT.next())
							cdFornecedor = rsT.getInt("cd_pessoa");
						else{
							cdFornecedor = PessoaJuridicaDAO.insert(new PessoaJuridica(0, 0, 0, nmRazaoSocialFornecedor.toUpperCase(), null, null, null, null, null, Util.getDataAtual(), 0, null, 1, null, null, null, 0, null, 0, 0, null, null, nmRazaoSocialFornecedor.toUpperCase(), null, null, 0, null, 0, 0, null), connect);
							if(cdFornecedor <= 0){
								connect.rollback();
								return new Result(-1, "Erro Fornecedor");
							}
						}
						pesqPessoaProduto.setInt(1, cdProdutoServico);
						pesqPessoaProduto.setInt(2, cdFornecedor);
						ResultSet rsPF = pesqPessoaProduto.executeQuery();
						if(!rsPF.next())
							if(com.tivic.manager.adm.ProdutoFornecedorDAO.insert(new ProdutoFornecedor(cdFornecedor, cdEmpresa, cdProdutoServico, 0, Float.parseFloat(vlUltimoCusto.replaceAll(",", ".")), 0, 0, idReduzido, 0, 1, cdUnidadeMedida), connect) <= 0 ){
								connect.rollback();
								return new Result(-1, "Erro ProdutoFornecedor");
							}
					}
					
			}
	  		
		}
	  		connect.commit();
			System.out.println("Importação de produtos concluída!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar produtos!", e);
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
    
	public static sol.util.Result importProdutosFromVitrine(int cdEmpresa, int cdTabelaPreco) throws SQLException	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = GenericImport.conectarSqlServer("vitrine");
		try {
			/***********************
			 * Importando produtos
			 ***********************/
			System.out.println("Importando produtos...");
			//
			connect.setAutoCommit(false);
			PreparedStatement pesqProduto = connect.prepareStatement("SELECT * FROM grl_produto_servico_empresa WHERE id_reduzido = ?");
			
			ResultSetMap rsProduto = new ResultSetMap(conOrigem.prepareStatement(
					"SELECT A.*, grunom, grusubnom, " +
					"       (SELECT MAX(B1.TPRPRE) FROM tabpre1 B1 WHERE A.procod = B1.procod) AS TPRPRE, " +
					"       (SELECT MAX(PROBARD)   FROM pro2    B2 WHERE A.procod = B2.procod AND PROBARD <> '''') AS PROBARD, " +
					"       (SELECT MAX(PROREF)    FROM pro2    B3 WHERE A.procod = B3.procod AND PROREF  <> '''') AS PROREF  " +
					"FROM produto A " +
					"LEFT OUTER JOIN grupo   B ON (A.grucod    = B.grucod) " +
					"LEFT OUTER JOIN grupo1  C ON (A.grucod    = C.grucod  " +
					"                          AND A.grusubcod = C.grusubcod) ").executeQuery());
			
			PreparedStatement pstmtPesqUni   = connect.prepareStatement("SELECT * from grl_unidade_medida where sg_unidade_medida = ?");
			PreparedStatement pstmtPesqGrupo = connect.prepareStatement("SELECT * FROM alm_grupo WHERE id_grupo = ?");
			//Produto
			while(rsProduto.next())	{
				int idProdutoServico    = Integer.parseInt(rsProduto.getString("procod"));
				String nmProdutoServico = rsProduto.getString("pronom");
				int qtEmbalagem			= 1;
				String sgUnidade        = rsProduto.getString("prouni");
				String idReduzido       = Util.fillNum(idProdutoServico, 4);
				String nrCodigoBarras   = rsProduto.getString("PROBARD");
				String nrReferencia     = rsProduto.getString("PROREF");
				// Verificando se já foi importado
				pesqProduto.setString(1, idReduzido);
				if(pesqProduto.executeQuery().next())
					continue;
				float vlPreco          = rsProduto.getFloat("tprpre");
				float vlUltimoCusto    = rsProduto.getFloat("proultaq");
				float qtEstoque        = 0;
				/*
				 * GRUPO
				 */
				int cdGrupo = 0;
				String idGrupo = rsProduto.getString("grucod")+"_"+rsProduto.getString("grusubcod");
				pstmtPesqGrupo.setString(1, idGrupo);
				ResultSet rsGrupo = pstmtPesqGrupo.executeQuery();
				if(rsGrupo.next())
					cdGrupo = rsGrupo.getInt("cd_grupo");
				else {
					int cdGrupoSuperior = 0;
					String idGrupoSup   = rsProduto.getString("grucod");
					pstmtPesqGrupo.setString(1, idGrupo);
					rsGrupo = pstmtPesqGrupo.executeQuery();
					if(rsGrupo.next())
						cdGrupoSuperior = rsGrupo.getInt("cd_grupo");
					else	{
						Grupo grupo = new Grupo(0,0,0,0,rsProduto.getString("grunom"),0,0,0,1,idGrupoSup);
						cdGrupoSuperior = GrupoDAO.insert(grupo, connect);
					}
					//
					Grupo grupo = new Grupo(0,cdGrupoSuperior,0,0,rsProduto.getString("grusubnom"),0,0,0,1,idGrupo);
					cdGrupo = GrupoDAO.insert(grupo, connect);
				}
				/*
				 * UNIDADE DE MEDIDA
				 */
				int cdUnidadeMedida = 0;
				pstmtPesqUni.setString(1, sgUnidade);
				ResultSet rsUnidadeMedida = pstmtPesqUni.executeQuery();
				if(rsUnidadeMedida.next())
					cdUnidadeMedida = rsUnidadeMedida.getInt("cd_unidade_medida");
				else
					if(UnidadeMedidaDAO.insert(new UnidadeMedida(0, sgUnidade, sgUnidade, null, 4, 1), connect) <= 0){
						connect.rollback();
						return new Result(-1, "Erro ao cadastrar Unidade de Medida");
					}
				/*
				 * NCM
				 *
				int cdNcm = 0;
				PreparedStatement pstmtNcm = connect.prepareStatement("SELECT * from grl_ncm where nr_ncm = '" + nrNcm + "'");
				ResultSet rsNcm = pstmtNcm.executeQuery();
				if(rsNcm.next())
					cdNcm = rsNcm.getInt("cd_ncm");
				else{
					if(NcmDAO.insert(new Ncm(0, nrNcm, cdUnidadeMedida, nrNcm), connect) <= 0){
						connect.rollback();
						return new Result(-1, "Erro ao cadastrar NCM");
					}
				}
				*/
				/*
				 * INCLUINDO O PRODUTO
				 */
				Produto produtoServico = new Produto(0, 0/*categoria economica*/, nmProdutoServico, null/*txtProdutoServico*/, null/*txtEspecificacao*/, null/*txtDadoTecnico*/, null/*txtPrazoEntrega*/, 0/*TpProdutoServico*/, 
													 nrCodigoBarras, null/*sgProdutoServico*/, 0/*cdClassificacaoFiscal*/, 0/*cdFabricante*/, 0/*cdMarca*/, null/*nmModelo*/,  0/*cdNcm*/, nrReferencia,0/*vlPesoUnitario*/, 0/*vlPesoUnitarioEmbalagem*/, 0/*vlComprimento*/, 0/*vlLargura*/, 0/*vlAltura*/, 0/*vlComprimentoEmbalagem*/, 0/*vlLarguraEmbalagem*/, 0/*vlAlturaEmbalagem*/, qtEmbalagem); 
				
				ProdutoServicoEmpresa produtoServicoEmpresa = new ProdutoServicoEmpresa(cdEmpresa, 0, cdUnidadeMedida, idReduzido, vlUltimoCusto, vlUltimoCusto, vlUltimoCusto, 0, 0, qtEstoque, 0/*qtDiasEstoque*/, 5/*qtPrecisaoCusto*/, 2/*qtPrecisaoUnidade*/, 0/*qtDiasGarantia*/, 0/*tpReabastecimento*/, 0/*tpControleEstoque*/, 0/*tpTransporte*/, 1/*stProdutoEmpresa*/, null/*dtDesativacao*/, null/*nrOrdem*/, 0);
				int cdProdutoServico = ProdutoServicoEmpresaServices.insert(produtoServico, produtoServicoEmpresa, cdGrupo, 0 /*cdGrupo2*/, new ArrayList<FormularioAtributoValor>(), null, connect);
				
				if(cdProdutoServico <= 0 )	{
					connect.rollback();
					Conexao.desconectar(connect);
					return new Result(-1, "Erro ao cadastrar Produto");
				}
				
				ProdutoServicoPreco produtoPre = new ProdutoServicoPreco(cdTabelaPreco, cdProdutoServico, 1, null, vlPreco);
				
				if(ProdutoServicoPrecoDAO.insert(produtoPre, connect) <= 0){
					connect.rollback();
					return new Result(-1, "Erro ao cadastrar ProdutoPreco");
				}
			}
			
			connect.commit();
			
			System.out.println("Importação de produtos concluída!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar pessoas!", e);
		}
		finally{
			Conexao.desconectar(connect);
			conOrigem.close();
		}
	}

	public static sol.util.Result importProdutosFromLoja(int cdEmpresa, int cdTabelaPreco) throws SQLException	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = GenericImport.conectar();
		try {
			/***********************
			 * Importando produtos
			 ***********************/
			System.out.println("Importando produtos...");
			//
			connect.setAutoCommit(false);
			PreparedStatement pesqProduto = connect.prepareStatement("SELECT * FROM grl_produto_servico_empresa WHERE id_reduzido = ?");
			
			ResultSetMap rsProduto = new ResultSetMap(conOrigem.prepareStatement("SELECT * FROM produto").executeQuery());
			
			//Produto
			while(rsProduto.next())	{
				int idProdutoServico    = rsProduto.getInt("produto_id");
				String nmProdutoServico = rsProduto.getString("descricao");
				int qtEmbalagem			= Math.round(Float.parseFloat(rsProduto.getString("qtd_embalagem")));
				String nrNcm			= rsProduto.getString("codigo_ncm");
				int cdProdutoNivel2 	= rsProduto.getInt("produto_nivel2_id");
				String idReduzido       = Util.fillNum(idProdutoServico, 4);
				// Verificando se já foi importado
				pesqProduto.setString(1, idReduzido);
				if(pesqProduto.executeQuery().next())
					continue;
				// Buscando dados da 
				PreparedStatement pstmtProdutoEmpresa = conOrigem.prepareStatement("SELECT * FROM produto_empresa where produto_id = ?");
				pstmtProdutoEmpresa.setInt(1, idProdutoServico);
				ResultSet rsProdutoEmpresa = pstmtProdutoEmpresa.executeQuery();
				//Produto Empresa
				float vlPrecoInicial = 0;
				float vlPrecoVista = 0;
				float qtEstoque = 0;
				float qtEstoqueInicial = 0;
				float qtEstoqueMinima = 0;
				if(rsProdutoEmpresa.next())	{
					vlPrecoInicial   = rsProdutoEmpresa.getFloat("prc_custo_inicial");
					vlPrecoVista     = rsProdutoEmpresa.getFloat("prc_ven_vista");
					qtEstoque        = rsProdutoEmpresa.getFloat("qtd_estoque");
					qtEstoqueInicial = rsProdutoEmpresa.getFloat("qtd_estoque_inicial");
					qtEstoqueMinima  = rsProdutoEmpresa.getFloat("qtd_minima");
				}
				
				PreparedStatement pstmtProdutoNivel2 = conOrigem.prepareStatement("SELECT * FROM produto_nivel2 where produto_nivel2_id = ?");
				
				pstmtProdutoNivel2.setInt(1, cdProdutoNivel2);
				ResultSet rsProdutoNivel2 = pstmtProdutoNivel2.executeQuery();
				//Produto Nivel 2
				String nmGrupo2 = "";
				int cdProdutoNivel1_2 = 0;
				if(rsProdutoNivel2.next())	{
					nmGrupo2 = rsProdutoNivel2.getString("descricao");
					cdProdutoNivel1_2 = rsProdutoNivel2.getInt("produto_nivel1_id");
				}
				PreparedStatement pstmtProdutoNivel1 = conOrigem.prepareStatement("SELECT * FROM produto_nivel1 where produto_nivel1_id = ?");
				pstmtProdutoNivel1.setInt(1, cdProdutoNivel1_2);
				ResultSet rsProdutoNivel1 = pstmtProdutoNivel1.executeQuery();
				//Produto Nivel 1
				String nmGrupo = "";
				if(rsProdutoNivel1.next())
					nmGrupo = rsProdutoNivel1.getString("descricao");
				/*
				 * CÓDIGO DE BARRAS
				 */
				PreparedStatement pstmtCodBarrasProduto =  conOrigem.prepareStatement("SELECT * FROM cod_barra_produto where produto_id = ?");
				pstmtCodBarrasProduto.setInt(1, idProdutoServico);
				ResultSet rsCodBarrasProduto = pstmtCodBarrasProduto.executeQuery();
				//Codigo Barras Produto
				String nrCodigoBarras = "";
				if(rsCodBarrasProduto.next())	{
					nrCodigoBarras = rsCodBarrasProduto.getString("cod_barra");
				}
				/*
				 * UNIDADE DE MEDIDA
				 */
				int cdUnidadeMedida = 0;
				PreparedStatement pstmtUni = connect.prepareStatement("SELECT * from grl_unidade_medida where sg_unidade_medida = 'UN'");
				ResultSet rsUnidadeMedida = pstmtUni.executeQuery();
				if(rsUnidadeMedida.next())
					cdUnidadeMedida = rsUnidadeMedida.getInt("cd_unidade_medida");
				else
					if(UnidadeMedidaDAO.insert(new UnidadeMedida(0, "Unidade", "UN", null, 4, 1), connect) <= 0){
						connect.rollback();
						return new Result(-1, "Erro ao cadastrar Unidade de Medida");
					}
				/*
				 * NCM
				 */
				int cdNcm = 0;
				PreparedStatement pstmtNcm = connect.prepareStatement("SELECT * from grl_ncm where nr_ncm = '" + nrNcm + "'");
				ResultSet rsNcm = pstmtNcm.executeQuery();
				if(rsNcm.next())
					cdNcm = rsNcm.getInt("cd_ncm");
				else{
					if(NcmDAO.insert(new Ncm(0, nrNcm, cdUnidadeMedida, nrNcm), connect) <= 0){
						connect.rollback();
						return new Result(-1, "Erro ao cadastrar NCM");
					}
				}
				/*
				 * INCLUINDO O PRODUTO
				 */
				Produto produtoServico = new Produto(0, 0/*categoria economica*/, nmProdutoServico, null/*txtProdutoServico*/, null/*txtEspecificacao*/, null/*txtDadoTecnico*/, null/*txtPrazoEntrega*/, 0/*TpProdutoServico*/, 
													 nrCodigoBarras, null/*sgProdutoServico*/, 0/*cdClassificacaoFiscal*/, 0/*cdFabricante*/, 0/*cdMarca*/, null/*nmModelo*/, cdNcm, ""/*nrReferencia*/, 0/*vlPesoUnitario*/, 0/*vlPesoUnitarioEmbalagem*/, 0/*vlComprimento*/, 0/*vlLargura*/, 0/*vlAltura*/, 0/*vlComprimentoEmbalagem*/, 0/*vlLarguraEmbalagem*/, 0/*vlAlturaEmbalagem*/, qtEmbalagem); 
				
				ProdutoServicoEmpresa produtoServicoEmpresa = new ProdutoServicoEmpresa(cdEmpresa, 0, cdUnidadeMedida, idReduzido, vlPrecoInicial, vlPrecoInicial, vlPrecoInicial, qtEstoqueInicial, qtEstoqueMinima, qtEstoque, 0/*qtDiasEstoque*/, 5/*qtPrecisaoCusto*/, 2/*qtPrecisaoUnidade*/, 0/*qtDiasGarantia*/, 0/*tpReabastecimento*/, 0/*tpControleEstoque*/, 0/*tpTransporte*/, 1/*stProdutoEmpresa*/, null/*dtDesativacao*/, null/*nrOrdem*/, 0);
				/*
				 * INCLUINDO OS GRUPOS
				 */
				int cdGrupo = 0;
				int cdGrupo2 = 0;
				
				PreparedStatement pstmtGrupo =  connect.prepareStatement("SELECT * FROM alm_grupo where nm_grupo = ?");
				pstmtGrupo.setString(1, nmGrupo.trim().toUpperCase());
				
				ResultSet rsGrupo1 = pstmtGrupo.executeQuery();
				
				if(rsGrupo1.next())
					cdGrupo = rsGrupo1.getInt("cd_grupo");
				else{
					cdGrupo = GrupoDAO.insert(new Grupo(0, 0, 0, 0, nmGrupo, 0, 0, 0, 1, null), connect);
					if(cdGrupo <= 0){
						connect.rollback();
						return new Result(-1, "Erro ao cadastrar Grupo");
					}
				}
				pstmtGrupo =  connect.prepareStatement("SELECT * FROM alm_grupo where nm_grupo = ?");
				pstmtGrupo.setString(1, nmGrupo2.trim().toUpperCase());
				
				ResultSet rsGrupo2 = pstmtGrupo.executeQuery();
				
				if(rsGrupo2.next())
					cdGrupo2 = rsGrupo2.getInt("cd_grupo");
				else{
					cdGrupo2 = GrupoDAO.insert(new Grupo(0, cdGrupo, 0, 0, nmGrupo2, 0, 0, 0, 1, null), connect);
					
					if(cdGrupo2 <= 0){
						connect.rollback();
						return new Result(-1, "Erro ao cadastrar Grupo");
					}
				}
				
				if(cdGrupo == cdGrupo2)
					cdGrupo2 = 0;
				
				int cdProdutoServico = ProdutoServicoEmpresaServices.insert(produtoServico, produtoServicoEmpresa, cdGrupo, cdGrupo2, new ArrayList<FormularioAtributoValor>(), null, connect);
				
				if(cdProdutoServico <= 0 )	{
					connect.rollback();
					Conexao.desconectar(connect);
					return new Result(-1, "Erro ao cadastrar Produto");
				}
				
				ProdutoServicoPreco produtoPre = new ProdutoServicoPreco(cdTabelaPreco, cdProdutoServico, 1, null, vlPrecoVista);
				
				if(ProdutoServicoPrecoDAO.insert(produtoPre, connect) <= 0){
					connect.rollback();
					return new Result(-1, "Erro ao cadastrar ProdutoPreco");
				}
			}
			
			connect.commit();
			
			System.out.println("Importação de produtos concluída!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar pessoas!", e);
		}
		finally{
			Conexao.desconectar(connect);
			conOrigem.close();
		}
	}
	
	public static sol.util.Result importProdutosFromDados(int cdEmpresa, int cdTabelaPreco) throws SQLException	{
		Connection connect   = Conexao.conectar();
		Connection conOrigem = GenericImport.conectarFirebird("//127.0.0.1:3050/C:/TIVIC/databases/parcon/DADOS.fdb");
		try {
			/***********************
			 * Importando produtos
			 ***********************/
			System.out.println("Importando produtos...");
			//
			connect.setAutoCommit(false);
			PreparedStatement pesqProduto = connect.prepareStatement("SELECT * FROM grl_produto_servico_empresa WHERE id_reduzido = ?");
			
			ResultSetMap rsProduto = new ResultSetMap(conOrigem.prepareStatement("SELECT * FROM tabela_produtos").executeQuery());
			
			//Produto
			while(rsProduto.next())	{
				int idProdutoServico    = rsProduto.getInt("id");
				String nmProdutoServico = rsProduto.getString("nome");
				int qtEmbalagem			= 1; // Math.round(rsProduto.getFloat("embalagem"));
				String idReduzido       = Util.fillNum(idProdutoServico, 4);
				String nrReferencia     = rsProduto.getString("referencia");
				// Verificando se já foi importado
				pesqProduto.setString(1, idReduzido);
				if(pesqProduto.executeQuery().next())
					continue;
				// Buscando dados da 
				//Produto Empresa
				float vlCusto   = 0;
				try {
					if(rsProduto.getObject("custo")!=null)
						vlCusto = Float.parseFloat(rsProduto.getString("custo"));
				}
				catch(Exception e) {
					System.out.println(rsProduto.getString("custo"));
					e.printStackTrace(System.out);
				}
				float vlPrecoVista     = Float.parseFloat(rsProduto.getString("preco"));
				float qtEstoque        = Float.parseFloat(rsProduto.getString("estoque"));
				float qtEstoqueInicial = 0;
				float qtEstoqueMinima  = Float.parseFloat(rsProduto.getString("estoque_minimo"));
				
				/*
				 * UNIDADE DE MEDIDA
				 */
				int cdUnidadeMedida = 0;
				PreparedStatement pstmtUni = connect.prepareStatement("SELECT * from grl_unidade_medida where sg_unidade_medida = ?");
				pstmtUni.setString(1, rsProduto.getString("unidade"));
				ResultSet rsUnidadeMedida = pstmtUni.executeQuery();
				if(rsUnidadeMedida.next())
					cdUnidadeMedida = rsUnidadeMedida.getInt("cd_unidade_medida");
				else
					if(UnidadeMedidaDAO.insert(new UnidadeMedida(0, rsProduto.getString("unidade"), rsProduto.getString("unidade"), null, 4, 1), connect) <= 0){
						connect.rollback();
						return new Result(-1, "Erro ao cadastrar Unidade de Medida");
					}
				/*
				 * INCLUINDO O PRODUTO
				 */
				Produto produtoServico = new Produto(0, 0/*categoria economica*/, nmProdutoServico, null/*txtProdutoServico*/, null/*txtEspecificacao*/, null/*txtDadoTecnico*/, null/*txtPrazoEntrega*/, 0/*TpProdutoServico*/, 
													 ""/*nrCodigoBarras*/, null/*sgProdutoServico*/, 0/*cdClassificacaoFiscal*/, 0/*cdFabricante*/, 0/*cdMarca*/, null/*nmModelo*/, 0/*cdNcm*/, 
													 nrReferencia, 0/*vlPesoUnitario*/, 0/*vlPesoUnitarioEmbalagem*/, 0/*vlComprimento*/, 0/*vlLargura*/, 0/*vlAltura*/, 0/*vlComprimentoEmbalagem*/, 0/*vlLarguraEmbalagem*/, 0/*vlAlturaEmbalagem*/, qtEmbalagem); 
				
				ProdutoServicoEmpresa produtoServicoEmpresa = new ProdutoServicoEmpresa(cdEmpresa, 0, cdUnidadeMedida, idReduzido, vlCusto, vlCusto, vlCusto, qtEstoqueInicial, qtEstoqueMinima, qtEstoque, 0/*qtDiasEstoque*/, 5/*qtPrecisaoCusto*/, 2/*qtPrecisaoUnidade*/, 0/*qtDiasGarantia*/, 0/*tpReabastecimento*/, 0/*tpControleEstoque*/, 0/*tpTransporte*/, 1/*stProdutoEmpresa*/, null/*dtDesativacao*/, null/*nrOrdem*/, 0);
				int cdGrupo  = 0;
				int cdGrupo2 = 0;
				
				int cdProdutoServico = ProdutoServicoEmpresaServices.insert(produtoServico, produtoServicoEmpresa, cdGrupo, cdGrupo2, new ArrayList<FormularioAtributoValor>(), null, connect);
				
				if(cdProdutoServico <= 0 )	{
					connect.rollback();
					Conexao.desconectar(connect);
					return new Result(-1, "Erro ao cadastrar Produto");
				}
				
				ProdutoServicoPreco produtoPre = new ProdutoServicoPreco(cdTabelaPreco, cdProdutoServico, 1, null, vlPrecoVista);
				
				if(ProdutoServicoPrecoDAO.insert(produtoPre, connect) <= 0){
					connect.rollback();
					return new Result(-1, "Erro ao cadastrar ProdutoPreco");
				}
			}
			
			connect.commit();
			
			System.out.println("Importação de produtos concluída!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar pessoas!", e);
		}
		finally{
			Conexao.desconectar(connect);
			conOrigem.close();
		}
	}
}
