package com.tivic.manager.alm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.adm.Classificacao;
import com.tivic.manager.adm.ClassificacaoDAO;
import com.tivic.manager.adm.ClassificacaoServices;
import com.tivic.manager.adm.ContaFechamentoDAO;
import com.tivic.manager.adm.ContaFechamento;
import com.tivic.manager.adm.ContaFechamentoServices;
import com.tivic.manager.adm.ContaFinanceira;
import com.tivic.manager.adm.ContaFinanceiraDAO;
import com.tivic.manager.adm.ProdutoServicoPreco;
import com.tivic.manager.adm.ProdutoServicoPrecoDAO;
import com.tivic.manager.adm.SaidaItemAliquotaServices;
import com.tivic.manager.adm.TabelaPreco;
import com.tivic.manager.adm.TabelaPrecoServices;
import com.tivic.manager.adm.TipoOperacao;
import com.tivic.manager.adm.TipoOperacaoDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.EmpresaServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.ProdutoServicoEmpresa;
import com.tivic.manager.grl.ProdutoServicoEmpresaDAO;
import com.tivic.manager.grl.ProdutoServicoEmpresaServices;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class DocumentoSaidaItemServices {

	public static final int ERR_DOC_BALANCO = -2;

	public static ResultSetMap getAllAliquotas(int cdDocumentoSaida, int cdProdutoServico, int cdItem) {
		return getAllAliquotas(cdDocumentoSaida, cdProdutoServico, cdItem, null);
	}

	public static ResultSetMap getAllAliquotas(int cdDocumentoSaida, int cdProdutoServico, int cdItem, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.pr_aliquota AS pr_aliquota_tributo, B.pr_credito, B.st_tributaria, " +
					"B.tp_operacao, C.nm_tributo, C.id_tributo " +
					"FROM adm_saida_item_aliquota A, adm_tributo_aliquota B, adm_tributo C " +
					"WHERE A.cd_tributo_aliquota = B.cd_tributo_aliquota " +
					"  AND A.cd_tributo = B.cd_tributo " +
					"  AND B.cd_tributo = C.cd_tributo " +
					"  AND A.cd_documento_saida = " +cdDocumentoSaida+
					"  AND A.cd_produto_servico = " +cdProdutoServico+
					"  AND A.cd_item            = "+cdItem);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaItemServices.getAllAliquotas: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getTabelaPromocional(int cdTabelaPrecoBase, int cdProdutoServico, int cdGrupo, int cdCliente) {
		return getTabelaPromocional(cdTabelaPrecoBase, cdProdutoServico, cdGrupo, cdCliente, null);
	}

	public static ResultSetMap getTabelaPromocional(int cdTabelaPrecoBase, int cdProdutoServico, int cdGrupo, int cdCliente, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT A.tp_aplicacao_tabela, B.*, id_tabela_preco " +
										                                 "FROM adm_cliente_produto A, adm_produto_servico_preco B, adm_tabela_preco C " +
										                                 "WHERE A.cd_produto_servico = " +cdProdutoServico+
										                                 "  AND A.cd_pessoa          = "+cdCliente+
										                                 "  AND A.cd_produto_servico = B.cd_produto_servico " +
										                                 "  AND A.cd_tabela_preco    = B.cd_tabela_preco " +
										                                 "  AND B.dt_termino_validade IS NULL "+
										                                 "  AND A.cd_tabela_preco    = C.cd_tabela_preco ").executeQuery());
			if(rsm.next())
				cdTabelaPrecoBase = rsm.getInt("cd_tabela_preco");
			
			//Utiliza a tabela de preço padrão caso não caia em nenhum dos casos seguintes
			TabelaPreco tabelaPadrao = TabelaPrecoServices.getPadrao();
			if(tabelaPadrao != null){
				ResultSetMap rsmPadrao = new ResultSetMap(connect.prepareStatement("SELECT 2 AS tp_aplicacao_tabela, B.*, id_tabela_preco " +
										                        "FROM adm_cliente_produto A, adm_produto_servico_preco B, adm_tabela_preco C " +
										                        "WHERE B.cd_produto_servico = " +cdProdutoServico+
										                        "  AND C.cd_tabela_preco    = "+tabelaPadrao.getCdTabelaPreco()+
										                        "  AND B.dt_termino_validade IS NULL "+
										                        "  AND B.cd_tabela_preco    = C.cd_tabela_preco ").executeQuery());
				if(rsmPadrao.next()){
					cdTabelaPrecoBase = rsmPadrao.getInt("cd_tabela_preco");
					rsm = rsmPadrao;
				}
			}
			
			//Primeiro nivel - Cliente e Produto
			PreparedStatement pstmtTabelaPreco = connect.prepareStatement("SELECT 2 AS tp_aplicacao_tabela, B.*, C.id_tabela_preco " +
																		"	FROM adm_cliente_tabela_preco A, adm_produto_servico_preco B, adm_tabela_preco C " +
																		"	WHERE A.cd_pessoa  = ?" +
																		"	  AND A.cd_produto_servico = ? " +
																		"  	  AND A.cd_produto_servico = B.cd_produto_servico " +
										                                "  	  AND A.cd_tabela_preco    = B.cd_tabela_preco " +
										                                "  	  AND B.dt_termino_validade IS NULL "+
																		" 	  AND A.cd_tabela_preco = C.cd_tabela_preco");
			
			pstmtTabelaPreco.setInt(1, cdCliente);
			pstmtTabelaPreco.setInt(2, cdProdutoServico);
			ResultSetMap rsmTabelaPreco = new ResultSetMap(pstmtTabelaPreco.executeQuery());
			if(rsmTabelaPreco.next()){
				cdTabelaPrecoBase = rsm.getInt("cd_tabela_preco");
				rsm = rsmTabelaPreco;
			}
			else{
				//Segundo nivel - Cliente e Grupo de Produto
				pstmtTabelaPreco = connect.prepareStatement("SELECT 2 AS tp_aplicacao_tabela, B.*, C.id_tabela_preco FROM adm_cliente_tabela_preco A, adm_produto_servico_preco B, adm_tabela_preco C " +
														"	WHERE A.cd_pessoa  = ?" +
														"	  AND A.cd_grupo = ? " +
														"  	  AND A.cd_produto_servico = ? " +
									                    "  	  AND A.cd_tabela_preco    = B.cd_tabela_preco " +
									                    "  	  AND B.dt_termino_validade IS NULL "+
														"	  AND A.cd_tabela_preco = ? " +
														" 	  AND A.cd_tabela_preco = C.cd_tabela_preco");
				
				pstmtTabelaPreco.setInt(1, cdCliente);
				pstmtTabelaPreco.setInt(2, cdGrupo);
				pstmtTabelaPreco.setInt(3, cdProdutoServico);
				pstmtTabelaPreco.setInt(4, cdTabelaPrecoBase);
				rsmTabelaPreco = new ResultSetMap(pstmtTabelaPreco.executeQuery());
				if(rsmTabelaPreco.next()){
					cdTabelaPrecoBase = rsm.getInt("cd_tabela_preco");
					rsm = rsmTabelaPreco;
				}
				else{
					
					Pessoa cliente = PessoaDAO.get(cdCliente, connect);
					Classificacao classificacaoCliente = null;
					if (cliente != null)
						classificacaoCliente = ClassificacaoDAO.get(cliente.getCdClassificacao(), connect);
					//Utiliza a padrão caso o cliente não tenha
					if(classificacaoCliente == null)
						classificacaoCliente = ClassificacaoServices.getPadrao();

					if(classificacaoCliente != null){
						//Terceiro nivel - Categoria e Produto
						pstmtTabelaPreco = connect.prepareStatement("SELECT 2 AS tp_aplicacao_tabela, B.*, C.id_tabela_preco FROM adm_cliente_tabela_preco A, adm_produto_servico_preco B, adm_tabela_preco C " +
																"	WHERE A.cd_classificacao  = ?" +
																"	  AND A.cd_produto_servico = ? " +
																"  	  AND A.cd_produto_servico = B.cd_produto_servico " +
										                        "  	  AND A.cd_tabela_preco    = B.cd_tabela_preco " +
										                        "  	  AND B.dt_termino_validade IS NULL "+
																" 	  AND A.cd_tabela_preco = C.cd_tabela_preco");
						
						pstmtTabelaPreco.setInt(1, classificacaoCliente.getCdClassificacao());
						pstmtTabelaPreco.setInt(2, cdProdutoServico);
						rsmTabelaPreco = new ResultSetMap(pstmtTabelaPreco.executeQuery());
						if(rsmTabelaPreco.next()){
							cdTabelaPrecoBase = rsm.getInt("cd_tabela_preco");
							rsm = rsmTabelaPreco;
						}
						else{
							//Quarto nivel - Categoria e Grupo de Produto
							pstmtTabelaPreco = connect.prepareStatement("SELECT 2 AS tp_aplicacao_tabela, B.*, C.id_tabela_preco FROM adm_cliente_tabela_preco A, adm_produto_servico_preco B, adm_tabela_preco C " +
																	"	WHERE A.cd_classificacao  = ?" +
																	"	  AND A.cd_grupo = ? " +
																	"  	  AND A.cd_produto_servico = ? " +
												                    "  	  AND A.cd_tabela_preco    = B.cd_tabela_preco " +
												                    "  	  AND B.dt_termino_validade IS NULL "+
																	"	  AND A.cd_tabela_preco = ? " +
																	" 	  AND A.cd_tabela_preco = C.cd_tabela_preco");
							
							pstmtTabelaPreco.setInt(1, classificacaoCliente.getCdClassificacao());
							pstmtTabelaPreco.setInt(2, cdGrupo);
							pstmtTabelaPreco.setInt(3, cdProdutoServico);
							pstmtTabelaPreco.setInt(4, cdTabelaPrecoBase);
							rsmTabelaPreco = new ResultSetMap(pstmtTabelaPreco.executeQuery());
							if(rsmTabelaPreco.next()){
								cdTabelaPrecoBase = rsm.getInt("cd_tabela_preco");
								rsm = rsmTabelaPreco;
							}
						}
					}
				}
			}
			//
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT B.*, C.id_tabela_preco, 2 AS tp_aplicacao_tabela " +
					"FROM adm_tabela_preco_regra A, adm_produto_servico_preco B, adm_tabela_preco C "+
                    "WHERE A.cd_tabela_preco_base = "+cdTabelaPrecoBase+
                    "  AND(A.cd_produto_servico   = "+cdProdutoServico+
                    "   OR A.cd_grupo             = "+cdGrupo+")"+
                    "  AND A.cd_tabela_preco      = B.cd_tabela_preco "+
                    "  AND B.cd_produto_servico   = "+cdProdutoServico+
                    "  AND B.dt_termino_validade  IS NULL "+
                    "  AND A.tp_valor_base        = 2 "+ // Regra com incidência sobre outra tabela
                    "  AND A.cd_tabela_preco      = C.cd_tabela_preco "+
                    "  AND C.dt_inicio_validade   <= ? "+
                    "  AND (C.dt_final_validade IS NULL " +
                    "   OR  C.dt_final_validade >= ?)");
			pstmt.setTimestamp(1, new Timestamp(new GregorianCalendar().getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(new GregorianCalendar().getTimeInMillis()));
			ResultSetMap rsm2 = new ResultSetMap(pstmt.executeQuery());
			if(rsm2.next()) {
				rsm2.beforeFirst();
				return rsm2;
			}
			//
			else {
				rsm.beforeFirst();
				return rsm;
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllLocaisArmazenamento(int cdDocumentoSaida, int cdProdutoServico, int cdItem) {
		return getAllLocaisArmazenamento(cdDocumentoSaida, cdProdutoServico, cdItem, 0, null);
	}

	public static ResultSetMap getAllLocaisArmazenamento(int cdDocumentoSaida, int cdProdutoServico, int cdItem, int cdEmpresa) {
		return getAllLocaisArmazenamento(cdDocumentoSaida, cdProdutoServico, cdItem, cdEmpresa,  null);
	}

	public static ResultSetMap getAllLocaisArmazenamento(int cdDocumentoSaida, int cdProdutoServico, int cdItem, int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_local_armazenamento, B.id_local_armazenamento, B.cd_nivel_local, B.cd_setor, " +
											 "C.nm_nivel_local, D.nm_setor " +
											 "FROM alm_saida_local_item A " +
											 "JOIN alm_local_armazenamento B ON (A.cd_local_armazenamento = B.cd_local_armazenamento) " +
											 "LEFT OUTER JOIN alm_nivel_local C ON (B.cd_nivel_local = C.cd_nivel_local) " +
											 "LEFT OUTER JOIN grl_setor D ON (B.cd_setor = D.cd_setor) " +
											 "WHERE A.cd_documento_saida = ? " +
											 "  AND A.cd_produto_servico = ? "+
											 "  AND A.cd_item = ? "+
											 (cdEmpresa>0 ? "  AND A.cd_empresa = ?" : ""));
			pstmt.setInt(1, cdDocumentoSaida);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdItem);
			if (cdEmpresa > 0)
				pstmt.setInt(4, cdEmpresa);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaItemServices.getAllLocaisArmazenamento: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result save(DocumentoSaidaItem item, int cdLocal) {
		return insert(item, cdLocal, 0, false, null);
	}

	public static Result insert(DocumentoSaidaItem item, int cdLocal) {
		return insert(item, cdLocal, 0, false, null);
	}
	
	public static Result insert(DocumentoSaidaItem item, int cdLocal, Connection connect) {
		return insert(item, cdLocal, 0, false, connect);
	}

	public static Result insert(DocumentoSaidaItem item, int cdLocal, int cdLocalDestino) {
		return insert(item, cdLocal, cdLocalDestino, false, null);
	}

	public static Result insert(DocumentoSaidaItem item, int cdLocal, boolean registerTributacao) {
		return insert(item, cdLocal, 0, registerTributacao,  null);
	}

	public static Result insert(DocumentoSaidaItem item, int cdLocal, boolean registerTributacao, int cdDocumentoSaidaOrigem) {
		Connection connect = Conexao.conectar();
		try	{
			connect.setAutoCommit(false);
			if(cdDocumentoSaidaOrigem > 0) {
				// Excluindo o item
				Result result = DocumentoSaidaServices.delete(cdDocumentoSaidaOrigem, connect);
				if(result.getCode() <= 0)	{
					result.setMessage("Falha ao excluir documento de saida original do abastecimento pendente! "+result.getMessage());
					Conexao.rollback(connect);
					return result;
				}
			}
			// Insere o item novo
			Result result = insert(item, cdLocal, 0, registerTributacao, connect);
			if(result.getCode() <= 0)	{
				Conexao.rollback(connect);
				return result;
			}
			connect.commit();
			return result;
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			Conexao.rollback(connect);
			return new Result(-1, "Erro ao incluir item no documento de saída!", e);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static Result insert(DocumentoSaidaItem item, int cdLocal, int cdLocalDestino, boolean registerTributacao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM alm_balanco_doc_saida " +
					                                              "WHERE cd_documento_saida = "+item.getCdDocumentoSaida());
			if (pstmt.executeQuery().next())
				return new Result(ERR_DOC_BALANCO, "Documento de saída já fez parte de balanço, não pode mais ser alterado!");

			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			/* verificacao de vinculo de produto/servico com empresa */
			if (ProdutoServicoEmpresaDAO.get(item.getCdEmpresa(), item.getCdProdutoServico(), connection) == null) {
				if (ProdutoServicoEmpresaDAO.insert(new ProdutoServicoEmpresa(item.getCdEmpresa(),item.getCdProdutoServico(), item.getCdUnidadeMedida(),
						                                                      "" /*idReduzido*/,0 /*vlPrecoMedio*/,0 /*vlCustoMedio*/,0 /*vlUltimoCusto*/,
						                                                      0 /*qtIdeal*/,0 /*qtMinima*/,0 /*qtMaxima*/,0 /*qtDiasEstoque*/,
						                                                      0 /*qtPrecisaoCusto*/,0 /*qtPrecisaoUnidade*/,0 /*qtDiasGarantia*/, 0
						                                                      /*tpReabastecimento*/,
						                                                      ProdutoServicoEmpresaServices.CTL_INDIVIDUAL /*tpControleEstoque*/,
						                                                      ProdutoServicoEmpresaServices.TRANSP_COMUM /*tpTransporte*/,
						                                                      ProdutoServicoEmpresaServices.ST_ATIVO /*stProdutoEmpresa*/,
						                                                      null /*dtDesativacao*/,"" /*nrOrdem*/,0 /*lgEstoqueNegativo*/), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					com.tivic.manager.util.Util.registerLog(new Exception("Erro ao tentar salvar dados do produto na empresa!"));
					return new Result(-1, "Erro ao tentar salvar dados do produto na empresa!");
				}
			}
			/*Inclui preco de tabela no item*/
			int cdTipoOperacao = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0, item.getCdEmpresa());
			if(cdTipoOperacao > 0){
				TipoOperacao tipoOperacao = TipoOperacaoDAO.get(cdTipoOperacao);
				
				int cdTabelaPreco = tipoOperacao.getCdTabelaPreco();
				if(cdTabelaPreco == 0){
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Tipo de Operação atacado padrão não relacionada com uma tabela de preço!");
				}
				
				if(item.getCdTabelaPreco() > 0)
					cdTabelaPreco = item.getCdTabelaPreco();
				if(cdTabelaPreco > 0){
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_tabela_preco", "" + cdTabelaPreco, ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_produto_servico", "" + item.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("dt_termino_validade", null, ItemComparator.ISNULL, Types.INTEGER));
					ResultSetMap rsmProdutoPreco = ProdutoServicoPrecoDAO.find(criterios, connection);
					ProdutoServicoPreco produtoPreco = null;
					if(rsmProdutoPreco.next())
						produtoPreco = ProdutoServicoPrecoDAO.get(cdTabelaPreco, item.getCdProdutoServico(), rsmProdutoPreco.getInt("cd_produto_servico_preco"));
					if(produtoPreco != null)
						item.setVlPrecoTabela(produtoPreco.getVlPreco());
				}
			}
			
			
			/* Inclui item */
			Result result = new Result(DocumentoSaidaItemDAO.insert(item, connection));
			item.setCdItem(result.getCode());
			result.addObject("cdItem", item.getCdItem());
			if (item.getCdItem() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				com.tivic.manager.util.Util.registerLog(new Exception("Nao foi possivel gravar o item! cdItem = "+item.getCdItem()));
				return new Result(-1, "Nao foi possivel gravar o item! cdItem = "+item.getCdItem());
			}
			/* Calculo de Tributos */
			ResultSetMap rsmTributos = null;
			if (registerTributacao) {
				rsmTributos = SaidaItemAliquotaServices.calcTributos(item, connection);
				if (rsmTributos == null) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					com.tivic.manager.util.Util.registerLog(new Exception("Tributos nao calculados!"));
					return new Result(-1, "Erro ao tentar calcular tributos!");
				}
				result.addObject("tributos", rsmTributos);
			}

			/* registra saida no estoque */
			if (cdLocal > 0) {
				int cdProdutoServico = item.getCdProdutoServico();
				float qtSaida = item.getQtSaida();
				int cdEmpresa = item.getCdEmpresa();
				int cdDocumentoSaida = item.getCdDocumentoSaida();
				DocumentoSaida documento = DocumentoSaidaDAO.get(cdDocumentoSaida, connection);
				if (documento == null) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					com.tivic.manager.util.Util.registerLog(new Exception("Documento de saida inválido!"));
					return new Result(-1, "Documento de saida inválido! [cdDocumentoSaida:"+cdDocumentoSaida+"]");
				}
				SaidaLocalItem itenArmazenamento = new SaidaLocalItem(0 /*cdSaidaLocalItem*/,cdProdutoServico,cdDocumentoSaida,cdEmpresa,
																	  cdLocal, 0 /*cdPedidoVenda*/, null /*dtSaida*/,
																	  documento.getTpMovimentoEstoque()==DocumentoSaidaServices.MOV_ESTOQUE_NAO_CONSIGNADO || documento.getTpMovimentoEstoque()==DocumentoSaidaServices.MOV_AMBOS_TIPO_ESTOQUE ? qtSaida : 0 /*qtSaida*/,
																	  documento.getTpMovimentoEstoque() == DocumentoSaidaServices.MOV_ESTOQUE_CONSIGNADO ? qtSaida : 0 /*qtSaidaConsignada*/,
																	  SaidaLocalItemServices.ST_ENVIADO /*stSaidaLocalItem*/,"" /*idSaidaLocalItem*/,
																	  item.getCdItem());
				int ret = SaidaLocalItemServices.insert(itenArmazenamento, connection);
				if (ret <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					com.tivic.manager.util.Util.registerLog(new Exception("Erro ao registrar saida de local de armazenamento!"));
					return new Result(-1, "Erro ao registrar saida de local de armazenamento! [ERRO: "+ret+"]");
				}
			}

			/* em caso de transferencias, se informado codigo de local destino, registra entrada neste local */
			if (cdLocalDestino != 0) {
				ResultSet rs = connection.prepareStatement("SELECT A.cd_documento_entrada, A.st_documento_entrada FROM alm_documento_entrada A " +
						                                   "WHERE A.cd_documento_saida_origem = "+item.getCdDocumentoSaida()).executeQuery();
				while (rs.next()) {
					if (rs.getInt("st_documento_entrada") == DocumentoEntradaServices.ST_EM_ABERTO) {
						Result objReturn = DocumentoEntradaItemServices.insert(new DocumentoEntradaItem(rs.getInt("cd_documento_entrada"),
								                                                                        item.getCdProdutoServico(),item.getCdEmpresa(),
								                                                                        item.getQtSaida(),0 /*vlUnitario*/,0 /*vlAcrescimo*/,
								                                                                        0 /*vlDesconto*/,item.getCdUnidadeMedida(),
								                                                                        null /*dtEntregaPrevista*/, 0/*cdNaturezaOperacao*/, 0/*cdAdicao*/, 0/*cdItem*/, 0/*vlVucv*/, 0/*vlDevolucaoTotal*/, 0/*cdTipoCredito*/),cdLocalDestino /*cdLocalArmazenamento*/,
								                                                                        registerTributacao, connection);
						if (objReturn.getCode() <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							com.tivic.manager.util.Util.registerLog(new Exception("Documento de destino ja fechado!"));
							return objReturn;
						}
					}
				}
			}

			if (isConnectionNull)
				connection.commit();

			return result;
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao incluir item no documento de saída!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result update(DocumentoSaidaItem item, int cdLocal) {
		return update(item, cdLocal, 0, false, null);
	}

	public static Result update(DocumentoSaidaItem item, int cdLocal, int cdLocalDestino) {
		return update(item, cdLocal, cdLocalDestino, false, null);
	}

	public static Result update(DocumentoSaidaItem item, int cdLocal, boolean updateTributacao) {
		return update(item, cdLocal, 0, updateTributacao, null);
	}

	public static Result update(DocumentoSaidaItem item, int cdLocal, int cdLocalDestino, boolean updateTributacao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			int cdProdutoServico = item.getCdProdutoServico();
			int cdEmpresa = item.getCdEmpresa();
			int cdDocumentoSaida = item.getCdDocumentoSaida();

			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM alm_balanco_doc_saida " +
																  "WHERE cd_documento_saida = "+item.getCdDocumentoSaida());
			if (pstmt.executeQuery().next()) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(ERR_DOC_BALANCO, "Documento de saída já faz parte de um balanço não é permitido altera-lo!");
			}

			/* remocao de locais anteriores de local de armazenamento informado */
			if (cdLocal != 0) {
				pstmt = connection.prepareStatement("DELETE FROM alm_saida_local_item " +
						"WHERE cd_documento_saida = ? " +
						"  AND cd_empresa = ? " +
						"  AND cd_produto_servico = ?");
				pstmt.setInt(1, cdDocumentoSaida);
				pstmt.setInt(2, cdEmpresa);
				pstmt.setInt(3, cdProdutoServico);
				pstmt.execute();
			}

			/* atualizacao do item */
			int cdRetorno = DocumentoSaidaItemDAO.update(item, connection);
			if (cdRetorno <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao tentar salvar dados do item alterado!");
			}

			/* calculo de tributos */
			Result result = new Result(1);
			ResultSetMap rsmTributos = null;
			if (updateTributacao) {
				rsmTributos = SaidaItemAliquotaServices.calcTributos(item, connection);
				if (rsmTributos == null) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar calcular tributos!");
				}
				result.addObject("tributos", rsmTributos);
				result.addObject("cdItem", item.getCdItem());
			}

			/* registra saida no estoque */
			if (cdLocal != 0) {
				float qtSaida = item.getQtSaida();
				DocumentoSaida documento = DocumentoSaidaDAO.get(cdDocumentoSaida);
				if (documento == null) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Documento de saída inválido! [cdDocumentoSaida:"+cdDocumentoSaida+"]");
				}
				SaidaLocalItem itenArmazenamento = new SaidaLocalItem(0 /*cdSaidaLocalItem*/,cdProdutoServico,cdDocumentoSaida,cdEmpresa,cdLocal,
																	  0 /*cdPedidoVenda*/, null /*dtSaida*/,
																	  documento.getTpMovimentoEstoque()==DocumentoSaidaServices.MOV_ESTOQUE_NAO_CONSIGNADO
																	   || documento.getTpMovimentoEstoque()==DocumentoSaidaServices.MOV_AMBOS_TIPO_ESTOQUE ? qtSaida : 0 /*qtSaida*/,
																	  documento.getTpMovimentoEstoque() == DocumentoSaidaServices.MOV_ESTOQUE_CONSIGNADO ? qtSaida : 0 /*qtSaidaConsignada*/,
																	  SaidaLocalItemServices.ST_ENVIADO /*stSaidaLocalItem*/,"" /*idSaidaLocalItem*/,item.getCdItem());
				if (SaidaLocalItemServices.insert(itenArmazenamento, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro salvar dados da saída no local de armazenamento!");
				}
			}

			/* em caso de transferencias, se informado codigo de local destino, registra entrada neste local */
			if (cdLocalDestino != 0) {
				pstmt = connection.prepareStatement("SELECT A.cd_documento_entrada, A.st_documento_entrada, A.cd_empresa " +
						                            "FROM alm_documento_entrada A " +
													"WHERE A.cd_documento_saida_origem = "+cdDocumentoSaida);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					if (rs.getInt("st_documento_entrada") == DocumentoEntradaServices.ST_EM_ABERTO) {
						if (DocumentoEntradaItemDAO.get(rs.getInt("cd_documento_entrada"), cdProdutoServico, cdEmpresa, 1/*cdItem*/, connection) != null)	{
							result = DocumentoEntradaItemServices.delete(rs.getInt("cd_documento_entrada"), cdProdutoServico, cdEmpresa, 1/*cdItem*/,connection);
							if (result.getCode() <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								result.setMessage("Exclusao de entrada relacionada a saída: "+result.getMessage());
								return result;
							}
						}

						Result objReturn = DocumentoEntradaItemServices.insert(new DocumentoEntradaItem(rs.getInt("cd_documento_entrada"),cdProdutoServico,
								                                                                        rs.getInt("cd_empresa"), item.getQtSaida() /*qtEntrada*/,
								                                                                        0 /*vlUnitario*/,0 /*vlAcrescimo*/,0 /*vlDesconto*/,
								                                                                        item.getCdUnidadeMedida(),null /*dtEntregaPrevista*/, 0/*cdNaturezaOperacao*/, 0/*cdAdicao*/, 0/*cdItem*/, 0/*vlVucv*/, 0/*vlDevolucaoTotal*/, 0/*cdTipoCredito*/), cdLocalDestino, updateTributacao, connection);
						if (objReturn.getCode() <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return objReturn;
						}
					}
				}
			}

			if (isConnectionNull)
				connection.commit();

			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao atualizar dados do item de saída!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int cancelarItem(int cdDocumentoSaida, int cdProdutoServico, int cdEmpresa, int cdItem) {
		return cancelarItem(cdDocumentoSaida, cdProdutoServico, cdEmpresa, cdItem, true, null);
	}

	public static int cancelarItem(int cdDocumentoSaida, int cdProdutoServico, int cdEmpresa, int cdItem, boolean deleteItens) {
		return cancelarItem(cdDocumentoSaida, cdProdutoServico, cdEmpresa, cdItem, deleteItens, null);
	}

	public static int cancelarItem(int cdDocumentoSaida, int cdProdutoServico, int cdEmpresa, int cdItem, boolean deleteItens, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			/* Registrando o cancelamento */
			int code = Conexao.getSequenceCode("adm_cancelamento_ecf_item", connect);
			if (code <= 0) {
				if (isConnectionNull)
					sol.dao.Conexao.rollback(connect);
				return -1;
			}

			int cdReferenciaEcf = 0;
			GregorianCalendar dtMovimento = new GregorianCalendar();
			ResultSetMap rs = new ResultSetMap(connect.prepareStatement("SELECT * FROM alm_documento_saida " +
                                                    				 	"WHERE cd_documento_saida = "+cdDocumentoSaida).executeQuery());
			if (rs.next())	{
				cdReferenciaEcf = rs.getInt("cd_referencia_ecf");
				dtMovimento 	= rs.getGregorianCalendar("dt_documento_saida");
			}
			dtMovimento = new GregorianCalendar(dtMovimento.get(GregorianCalendar.YEAR), dtMovimento.get(GregorianCalendar.MONTH), dtMovimento.get(GregorianCalendar.DATE));
			DocumentoSaidaItem item = DocumentoSaidaItemDAO.get(cdDocumentoSaida, cdProdutoServico, cdEmpresa, cdItem, connect);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_cancelamento_ecf_item (cd_referencia,"+
			                                  "dt_movimento,"+
			                                  "cd_documento_saida,"+
			                                  "nr_item,"+
			                                  "qt_atendida,"+
			                                  "vl_unitario,"+
			                                  "vl_acrescimo,"+
			                                  "vl_desconto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, cdReferenciaEcf);
			pstmt.setTimestamp(2, new Timestamp(dtMovimento.getTimeInMillis()));
			pstmt.setInt(3, cdDocumentoSaida);
			pstmt.setInt(4, code);
			pstmt.setFloat(5,item.getQtSaida());
			pstmt.setFloat(6,item.getVlUnitario());
			pstmt.setFloat(7,item.getVlAcrescimo());
			pstmt.setFloat(8,item.getVlDesconto());
			pstmt.executeUpdate();

			if (delete(cdDocumentoSaida, cdProdutoServico, cdEmpresa, cdItem, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaItemServices.cancelarItem: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDocumentoSaida, int cdProdutoServico, int cdEmpresa, int cdItem) {
		return delete(cdDocumentoSaida, cdProdutoServico, cdEmpresa, cdItem, null);
	}

	public static int delete(int cdDocumentoSaida, int cdProdutoServico, int cdEmpresa, int cdItem, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM alm_balanco_doc_saida " +
					"WHERE cd_documento_saida = ?");
			pstmt.setInt(1, cdDocumentoSaida);
			if (pstmt.executeQuery().next()) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return ERR_DOC_BALANCO;
			}

			/* Excluindo alíquotas */
			pstmt = connection.prepareStatement("DELETE " +
					"FROM adm_saida_item_aliquota " +
					"WHERE cd_produto_servico = ? " +
					"  AND cd_documento_saida = ? " +
					"  AND cd_empresa = ? " +
					"  AND cd_item = ?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdDocumentoSaida);
			pstmt.setInt(3, cdEmpresa);
			pstmt.setInt(4, cdItem);
			pstmt.execute();

			/* Excluindo locais de armazenamento */
			ResultSet rs = connection.prepareStatement("SELECT * FROM alm_saida_local_item " +
												"WHERE cd_produto_servico = " +cdProdutoServico+
												"  AND cd_documento_saida = " +cdDocumentoSaida+
												"  AND cd_empresa = "+cdEmpresa +
												"  AND cd_item = "+cdItem).executeQuery();
			while(rs.next()) {
				if (SaidaLocalItemServices.delete(rs.getInt("cd_saida"), cdProdutoServico, cdDocumentoSaida, cdEmpresa,
						rs.getInt("cd_local_armazenamento"), rs.getInt("cd_item"), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}

				pstmt = connection.prepareStatement("SELECT A.cd_documento_entrada, A.tp_entrada, A.st_documento_entrada, B.cd_produto_servico, B.cd_item " +
													"FROM alm_documento_entrada A, alm_documento_entrada_item B " +
													"WHERE A.cd_documento_entrada = B.cd_documento_entrada " +
													"  AND A.cd_documento_saida_origem = " +cdDocumentoSaida+
													"  AND B.cd_produto_servico        = " +cdProdutoServico);
				ResultSet rsTemp = pstmt.executeQuery();
				while (rsTemp.next())
					if (DocumentoEntradaItemServices.delete(rsTemp.getInt("cd_documento_entrada"), cdProdutoServico, cdEmpresa, rsTemp.getInt("cd_item"), connection).getCode() <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
			}

			pstmt = connection.prepareStatement("DELETE FROM adm_venda_saida_item " +
					"WHERE cd_documento_saida = ? " +
					"  AND cd_empresa = ? " +
					"  AND cd_produto_servico = ?");
			pstmt.setInt(1, cdDocumentoSaida);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdProdutoServico);
			pstmt.execute();

			if (DocumentoSaidaItemDAO.delete(cdDocumentoSaida, cdProdutoServico, cdEmpresa, cdItem, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaItemServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, null, null, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, ArrayList<String> groupByFields) {
		return findCompleto(criterios, groupByFields, null, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, ArrayList<String> groupByFields, ArrayList<String> orderByFields) {
		return findCompleto(criterios, groupByFields, orderByFields, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, ArrayList<String> groupByFields, ArrayList<String> orderByFields, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		ResultSetMap rsm = null;
		try	{
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			if(groupByFields==null)
				groupByFields = new ArrayList<String>();
			// Repassando critérios
			boolean hashEntregaPendente = false;
			boolean includeFaturamento  = false;
			boolean semEstoque           = false;
			boolean hasNotaFiscal = false;
			String nrNotaFiscal = "";
			int cdEmpresa = 0, cdFornecedor = 0;
			String idReduzidoNrReferencia = "";
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for (int i=0; criterios!=null && i<criterios.size(); i++)	{
				includeFaturamento = includeFaturamento || (criterios.get(i).getColumn().toLowerCase().indexOf("forma_pagamento")>=0) ||
                                                           (criterios.get(i).getColumn().toLowerCase().indexOf("plano_pagamento")>=0);
				if(criterios.get(i).getColumn().equalsIgnoreCase("faturamento"))
					includeFaturamento = true;
				if(criterios.get(i).getColumn().equalsIgnoreCase("semEstoque")){
					semEstoque = true;
					criterios.remove(i);
					i--;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("cdFornecedor"))
					cdFornecedor = Integer.valueOf(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().toLowerCase().indexOf("id_reduzido_nr_referencia") >= 0)
					idReduzidoNrReferencia = criterios.get(i).getValue();
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgEntregaPendente"))
					hashEntregaPendente = true;
				else if(criterios.get(i).getColumn().equalsIgnoreCase("nr_nota_fiscal")){
					hasNotaFiscal = true;
					nrNotaFiscal = criterios.get(i).getValue();
					criterios.remove(i);
				} else	{
					if(criterios.get(i).getColumn().toLowerCase().indexOf("cd_empresa")>0)
						cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
					crt.add(criterios.get(i));
				}
			}
			//
			for(int i=0; i<groupByFields.size(); i++)
				includeFaturamento = includeFaturamento || (groupByFields.get(i).toLowerCase().indexOf("forma_pagamento")>=0) ||
									                       (groupByFields.get(i).toLowerCase().indexOf("plano_pagamento")>=0);

			String fields = " A.cd_documento_saida, A.cd_transportadora, A.cd_empresa, A.cd_cliente, A.dt_documento_saida, " +
							" A.st_documento_saida, A.nr_documento_saida, A.tp_documento_saida, A.tp_saida, A.tp_frete, tp_movimento_estoque, " +
							" A.cd_vendedor, A.cd_tipo_operacao, " +
							" (B.vl_unitario * B.qt_saida + B.vl_acrescimo - B.vl_desconto) AS vl_total_item, B.cd_item, B.qt_saida, B.vl_unitario, B.vl_acrescimo, B.vl_desconto, " +
							" B.cd_unidade_medida AS cd_unidade_medida_item, C.id_reduzido, C.cd_unidade_medida, " +
							" C.cd_produto_servico, C.vl_ultimo_custo, D.nm_produto_servico, D.nr_referencia, E.sg_unidade_medida, G.cd_grupo, G.nm_grupo, G2.nm_grupo AS nm_sub_grupo, " +
							" K.nm_razao_social AS nm_empresa, T.nm_turno, " +
							" H.nm_pessoa AS nm_cliente, I.nm_pessoa AS nm_vendedor, J.nm_pessoa AS nm_transportadora, L.nm_tipo_operacao " +
							(includeFaturamento ? ", nm_forma_pagamento, nm_plano_pagamento, M.vl_pagamento " : "")+
							(hashEntregaPendente ? ", (SELECT SUM(M.qt_saida + M.qt_saida_consignada) " +
							"                          FROM alm_saida_local_item M " +
							"                          WHERE M.cd_produto_servico = B.cd_produto_servico " +
							"	                         AND M.cd_documento_saida = B.cd_documento_saida " +
							"	                         AND M.cd_empresa = B.cd_empresa) AS qt_saida_local" : "");
			String groups = "";
			String [] retorno = Util.getFieldsAndGroupBy(groupByFields, fields, groups,
						        " SUM(B.vl_unitario * B.qt_saida + B.vl_acrescimo - B.vl_desconto) AS vl_item, " +
						        " SUM(B.vl_unitario * B.qt_saida) AS vl_geral, " +
						        " SUM(B.qt_saida) AS qt_saidas "+
						        (includeFaturamento ? ", SUM(vl_pagamento) AS vl_pagamento " : ""));
			fields = retorno[0];
			groups = retorno[1];
			
			/**/
			rsm = Search.find("SELECT " + fields + " " +
					"FROM alm_documento_saida A " +
					"JOIN alm_documento_saida_item       B ON (A.cd_documento_saida = B.cd_documento_saida) " +
					"JOIN grl_produto_servico_empresa    C ON (B.cd_produto_servico = C.cd_produto_servico " +
					"									   AND B.cd_empresa 		= C.cd_empresa) " +
					"JOIN grl_produto_servico            D ON (C.cd_produto_servico = D.cd_produto_servico) " +
					"LEFT OUTER JOIN grl_unidade_medida  E ON (B.cd_unidade_medida  = E.cd_unidade_medida) " +
					"LEFT OUTER JOIN alm_produto_grupo   F ON (C.cd_produto_servico = F.cd_produto_servico " +
					"									   AND C.cd_empresa         = F.cd_empresa " +
					"								       AND F.lg_principal       = 1)" +
					"LEFT OUTER JOIN alm_grupo           G ON (F.cd_grupo    	    = G.cd_grupo) " +
					"LEFT OUTER JOIN alm_produto_grupo   F2 ON (C.cd_produto_servico = F2.cd_produto_servico " +
					"									   AND C.cd_empresa         = F2.cd_empresa " +
					"								       AND F2.lg_principal       = 0)" +
					"LEFT OUTER JOIN alm_grupo           G2 ON (F2.cd_grupo = G2.cd_grupo) " +
					"LEFT OUTER JOIN grl_pessoa          H ON (A.cd_cliente  	    = H.cd_pessoa) " +
					"LEFT OUTER JOIN grl_pessoa          I ON (A.cd_vendedor        = I.cd_pessoa) " +
					"LEFT OUTER JOIN grl_pessoa          J ON (A.cd_transportadora  = J.cd_pessoa) " +
					"LEFT OUTER JOIN grl_empresa         N ON (A.cd_empresa 		= N.cd_empresa) " +
					"LEFT OUTER JOIN grl_pessoa_juridica K ON (N.cd_empresa  		= K.cd_pessoa) " +
					"LEFT OUTER JOIN adm_tipo_operacao   L ON (A.cd_tipo_operacao = L.cd_tipo_operacao) " +
					"LEFT OUTER JOIN adm_turno           T ON (A.cd_turno         = T.cd_turno) " +
					(includeFaturamento ?
						"LEFT OUTER JOIN adm_plano_pagto_documento_saida M ON (A.cd_documento_saida = M.cd_documento_saida) " +
						"LEFT OUTER JOIN adm_forma_pagamento O ON (M.cd_forma_pagamento = O.cd_forma_pagamento) " +
						"LEFT OUTER JOIN adm_plano_pagamento P ON (M.cd_plano_pagamento = P.cd_plano_pagamento) " : "")+
				    "WHERE 1 = 1 " +
				   (cdFornecedor>0 ? " AND EXISTS (SELECT * FROM alm_documento_entrada_item DEI, alm_documento_entrada DE " +
		   		             "             WHERE DEI.cd_produto_servico   = B.cd_produto_servico " +
		   		             "               AND DEI.cd_documento_entrada = DE.cd_documento_entrada " +
		   		             "               AND DE.cd_empresa            = A.cd_empresa " +
		   		             "               AND DE.cd_fornecedor         = "+cdFornecedor+")" : "")+
		   		             (!idReduzidoNrReferencia.equals("") ? " AND (C.id_reduzido = \'"+idReduzidoNrReferencia+"\' OR D.nr_referencia LIKE \'"+idReduzidoNrReferencia+"%\') ": "")+
				    (hashEntregaPendente ? "  AND B.qt_saida>0 AND (B.qt_saida <> (SELECT SUM(M.qt_saida + M.qt_saida_consignada) " +
				    					   "					                   FROM alm_saida_local_item M " +
				    					   "					                   WHERE M.cd_produto_servico = B.cd_produto_servico " +
				    					   "						                 AND M.cd_documento_saida = B.cd_documento_saida " +
				    					   "						                 AND M.cd_empresa = B.cd_empresa) " +
				    					   "					  					  OR NOT EXISTS (SELECT M.cd_saida_local_item " +
				    					   "								                         FROM alm_saida_local_item M " +
				    					   "								                         WHERE M.cd_produto_servico = B.cd_produto_servico " +
				    					   "									                       AND M.cd_documento_saida = B.cd_documento_saida " +
				    					   "									                       AND M.cd_empresa = B.cd_empresa)) " : ""),
				    					// se caso tenha preenchido o número da nota no filtro
				   (hasNotaFiscal ? "AND A.cd_documento_saida IN (SELECT cd_documento_saida FROM fsc_nota_fiscal_item nota_item LEFT JOIN fsc_nota_fiscal nota ON (nota.cd_nota_fiscal = nota_item.cd_nota_fiscal ) WHERE nota.nr_nota_fiscal ILIKE '%" + nrNotaFiscal + "%')" : "") +
				    groups , crt, connect, !isConnectionNull);
			
			
			if (orderByFields != null && orderByFields.size()>0)
				rsm.orderBy(orderByFields);
			rsm.beforeFirst();
			if(!semEstoque)
				while(rsm.next()){
					ResultSetMap map = ProdutoEstoqueServices.findProduto(cdEmpresa, 0/*cdLocalArmazenamento*/, rsm.getInt("CD_PRODUTO_SERVICO"));
					map.orderBy(orderByFields);
					while (map.next()) {
						rsm.setValueToField("QT_TOTAL_ENTRADA", map.getFloat("QT_ENTRADA"));
						rsm.setValueToField("VL_ENTRADA", map.getFloat("VL_ENTRADA"));
						rsm.setValueToField("QT_ESTOQUE", map.getFloat("QT_ESTOQUE"));
						rsm.setValueToField("QT_ENTRADA_CONSIGNADA", map.getFloat("QT_ENTRADA_CONSIGNADA"));
						rsm.setValueToField("VL_ENTRADA_CONSIGNADA", map.getFloat("VL_ENTRADA_CONSIGNADA"));
						rsm.setValueToField("QT_ESTOQUE_CONSIGNADO", map.getFloat("QT_ESTOQUE_CONSIGNADO"));
						rsm.setValueToField("QT_SAIDA", map.getFloat("QT_SAIDA"));
						rsm.setValueToField("QT_SAIDA_CONSIGANADA", map.getFloat("QT_SAIDA_CONSIGANADA"));
						rsm.setValueToField("VL_SAIDA", map.getFloat("VL_SAIDA"));
					}
				}
			/* Fornecedor */
			PreparedStatement pstmt = connect.prepareStatement("SELECT nm_pessoa AS nm_fornecedor FROM alm_documento_entrada D, alm_documento_entrada_item E, grl_pessoa F " +
				                                               "WHERE E.cd_produto_servico   = ? " +
				                                               "  AND E.cd_documento_entrada = D.cd_documento_entrada " +
				                                               "  AND E.cd_empresa           = " +cdEmpresa+
				                                               "  AND D.cd_empresa           = " +cdEmpresa+
				                                               "  AND D.st_documento_entrada = " +DocumentoEntradaServices.ST_LIBERADO+
				                                               "  AND D.tp_entrada IN ("+DocumentoEntradaServices.ENT_COMPRA+ ","+DocumentoEntradaServices.ENT_CONSIGNACAO+") " +
				                                               "  AND D.cd_fornecedor        = F.cd_pessoa " +
				                                               "ORDER BY dt_documento_entrada DESC " +
				                                               "LIMIT 1");
			while (rsm.next()) {
				if(rsm.getInt("cd_produto_servico") <= 0)
					continue;
				// Ultimo fornecedor
				pstmt.setInt(1, rsm.getInt("cd_produto_servico"));
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
					rsm.setValueToField("NM_FORNECEDOR", rs.getString("nm_fornecedor"));
			}
			rsm.beforeFirst();
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
		}
		return rsm;
	}

	public static double getSomatorioDosItensNota(int cdEmpresa, int cdDocumentoSaida){
		Connection connect=null;
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		double somatorio=0;

		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_documento_saida_item WHERE cd_documento_saida=? AND cd_empresa=?");
			pstmt.setInt(1, cdDocumentoSaida);
			pstmt.setInt(2, cdEmpresa);
			rs = pstmt.executeQuery();
			while(rs.next()){
				somatorio+=((rs.getInt("qt_saida")*rs.getDouble("vl_unitario")));
			}

			return somatorio;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaItemDAO.get: " + sqlExpt);
			return 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaItemDAO.get: " + e);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}

	}
	
	public static ResultSetMap getAllItensDocumentoSaida(int cdEmpresa, int cdDocumentoSaida){
		return getAllItensDocumentoSaida(cdEmpresa, cdDocumentoSaida, null);
	}
	
	public static ResultSetMap getAllItensDocumentoSaida(int cdEmpresa, int cdDocumentoSaida, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs=null;

		try {
			pstmt = connect.prepareStatement(
				"SELECT A.cd_produto_servico, A.qt_saida, B.nm_produto_servico, C.sg_unidade_medida, A.cd_item, A.cd_empresa " + 
				"FROM alm_documento_saida_item A "+ 
				"LEFT OUTER JOIN grl_produto_servico B ON (A.cd_produto_servico = B.cd_produto_servico) "+ 
				"LEFT OUTER JOIN grl_unidade_medida C ON (A.cd_unidade_medida = C.cd_unidade_medida) " + 
				"WHERE A.cd_documento_saida=? AND A.cd_empresa=? ORDER BY A.cd_produto_servico ASC "
			);
			pstmt.setInt(1, cdDocumentoSaida);
			pstmt.setInt(2, cdEmpresa);
			rs = pstmt.executeQuery();
			
			return new ResultSetMap(rs);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaItemDAO.getAllItensDocumentoSaida: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}


	public static ResultSet findSaidaAndItens(int cdTurno,GregorianCalendar dtSaidaInicial,GregorianCalendar dtSaidaFinal)
	{
		Connection connect=null;
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			PreparedStatement pstmt;
			ResultSet rs=null;
			String sql = "SELECT " +
						 "DS.nr_documento_saida,PS.id_produto_servico,PV.nm_pessoa as nm_vendedor,PC.nm_pessoa as nm_cliente,PS.nm_produto_servico," +
						 "DSI.qt_saida,DSI.vl_unitario,DSI.vl_desconto,PPDS.vl_pagamento, "+
						 "FP.nm_forma_pagamento,PP.nm_plano_pagamento,"+
						 "(SELECT SUM(E.qt_saida * vl_unitario + vl_acrescimo - vl_desconto) FROM alm_documento_saida_item E WHERE E.cd_documento_saida = DS.cd_documento_saida) AS vl_total_liquido,"+
						 "(SELECT SUM(E.qt_saida * vl_unitario) FROM alm_documento_saida_item E WHERE E.cd_documento_saida = DS.cd_documento_saida) AS vl_total_saida,"+
						 "(SELECT SUM(F.vl_desconto) FROM alm_documento_saida_item F WHERE F.cd_documento_saida = DS.cd_documento_saida) AS vl_total_descontos " +
						 "FROM alm_documento_saida_item DSI " +
						 "INNER JOIN alm_documento_saida DS ON(DS.cd_documento_saida = DSI.cd_documento_saida) "+
						 "INNER JOIN grl_produto_servico PS ON(DSI.cd_produto_servico = PS.cd_produto_servico) "+
						 "LEFT OUTER JOIN grl_pessoa     PV ON(DS.cd_vendedor = PV.cd_pessoa) "+
						 "LEFT OUTER JOIN grl_pessoa	PC ON(DS.cd_cliente  = PC.cd_pessoa) "+
						 "LEFT OUTER JOIN adm_plano_pagto_documento_saida PPDS ON(DS.cd_documento_saida = PPDS.cd_documento_saida) "+
						 "LEFT OUTER JOIN adm_forma_pagamento FP ON(PPDS.cd_forma_pagamento = FP.cd_forma_pagamento) "+
						 "LEFT OUTER JOIN adm_plano_pagamento PP ON(PPDS.cd_plano_pagamento = PP.cd_plano_pagamento) "+
						 "WHERE DS.cd_turno = ? AND DS.dt_documento_saida BETWEEN ? AND ? "+
						 "GROUP BY DS.cd_documento_saida,PS.cd_produto_servico,PV.nm_pessoa,PC.nm_pessoa, "+
						 "DSI.qt_saida,DSI.vl_unitario,DSI.vl_desconto,PPDS.vl_pagamento,FP.nm_forma_pagamento,PP.nm_plano_pagamento";

			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, cdTurno);
			pstmt.setTimestamp(2, new Timestamp(dtSaidaInicial.getTimeInMillis()));
			pstmt.setTimestamp(3, new Timestamp(dtSaidaFinal.getTimeInMillis()));
			rs = pstmt.executeQuery();
			return rs;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaItemDAO.getAllItensDocumentoSaida: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findSaidaAndItensByGrupo(int cdTurno,GregorianCalendar dtSaidaInicial,GregorianCalendar dtSaidaFinal, boolean combustivel)
	{
		return findSaidaAndItensByGrupo(cdTurno,dtSaidaInicial,dtSaidaFinal,0,0,combustivel);
	}

	public static ResultSetMap findSaidaAndItensByGrupo(int cdTurno,GregorianCalendar dtSaidaInicial,GregorianCalendar dtSaidaFinal)
	{
		return findSaidaAndItensByGrupo(cdTurno,dtSaidaInicial,dtSaidaFinal,0,0,false);
	}

	public static ResultSetMap findSaidaAndItensByGrupo(int cdTurno,GregorianCalendar dtSaidaInicial,GregorianCalendar dtSaidaFinal, int cdGrupo, int cdEmpresa, boolean combustivel)
	{
		Connection connect=null;
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			PreparedStatement pstmt;
			ResultSetMap rsm=null;
			dtSaidaInicial.set(Calendar.HOUR, 0);
			dtSaidaInicial.set(Calendar.MINUTE, 0);
			dtSaidaInicial.set(Calendar.SECOND, 0);

			dtSaidaFinal.set(Calendar.HOUR, 23);
			dtSaidaFinal.set(Calendar.MINUTE, 59);
			dtSaidaFinal.set(Calendar.SECOND, 59);
			int cdGrupoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa);

			GregorianCalendar diaMesInicial = (GregorianCalendar) dtSaidaInicial.clone();
			diaMesInicial.set(Calendar.DAY_OF_MONTH, 1);
			GregorianCalendar diaMesFinal = (GregorianCalendar) dtSaidaFinal.clone();
			diaMesFinal.set(Calendar.DAY_OF_MONTH, diaMesFinal.getMaximum(Calendar.DAY_OF_MONTH));

			GregorianCalendar diaAnteriorInicial = (GregorianCalendar) dtSaidaInicial.clone();
			diaAnteriorInicial.add(Calendar.DAY_OF_MONTH, -1);
			GregorianCalendar diaAnteriorFinal = (GregorianCalendar) dtSaidaFinal.clone();
			diaAnteriorFinal.add(Calendar.DAY_OF_MONTH, -1);

			String sql = "SELECT " +
						 "DS.nr_documento_saida,PS.id_produto_servico,PV.nm_pessoa as nm_vendedor,PC.nm_pessoa as nm_cliente,PS.nm_produto_servico," +
						 "DSI.qt_saida,SUM(DSI.vl_desconto) as vl_desconto, DSI.vl_unitario,PPDS.vl_pagamento, G.nm_grupo, G.cd_grupo,"+
						 "FP.nm_forma_pagamento,PP.nm_plano_pagamento, PG.cd_empresa, PG.cd_produto_servico, DS.cd_documento_saida, "+
						 "(SELECT SUM(E.qt_saida * vl_unitario + vl_acrescimo - vl_desconto) FROM alm_documento_saida_item E WHERE E.cd_documento_saida = DS.cd_documento_saida) AS vl_total_liquido,"+
						 "(SELECT SUM(E.qt_saida * vl_unitario) FROM alm_documento_saida_item E WHERE E.cd_documento_saida = DS.cd_documento_saida) AS vl_total_saida,"+
						 "(SELECT SUM(F.vl_desconto) FROM alm_documento_saida_item F WHERE F.cd_documento_saida = DS.cd_documento_saida) AS vl_total_descontos " +
						 "FROM alm_documento_saida_item DSI " +
						 "INNER JOIN alm_documento_saida DS ON(DS.cd_documento_saida = DSI.cd_documento_saida) "+
						 "INNER JOIN grl_produto_servico PS ON(DSI.cd_produto_servico = PS.cd_produto_servico)" +
						 "LEFT OUTER JOIN alm_produto_grupo PG ON(PG.cd_produto_servico = PS.cd_produto_servico AND " +
						 "							   			  DS.cd_empresa = PG.cd_empresa AND PG.lg_principal = 1) " +
						 "LEFT OUTER JOIN alm_grupo G ON(PG.cd_grupo = G.cd_grupo) "+
						 "LEFT OUTER JOIN grl_pessoa     PV ON(DS.cd_vendedor = PV.cd_pessoa) "+
						 "LEFT OUTER JOIN grl_pessoa	PC ON(DS.cd_cliente  = PC.cd_pessoa) "+
						 "LEFT OUTER JOIN adm_plano_pagto_documento_saida PPDS ON(DS.cd_documento_saida = PPDS.cd_documento_saida) "+
						 "LEFT OUTER JOIN adm_forma_pagamento FP ON(PPDS.cd_forma_pagamento = FP.cd_forma_pagamento) "+
						 "LEFT OUTER JOIN adm_plano_pagamento PP ON(PPDS.cd_plano_pagamento = PP.cd_plano_pagamento) "+
						 "WHERE DS.cd_turno = ? AND DS.dt_documento_saida BETWEEN ? AND ? " +
						 (cdGrupo>0?" AND G.cd_grupo = "+ cdGrupo:"") +
						 (combustivel==false?" AND G.cd_grupo <> "+ cdGrupoCombustivel:"")+
						 "GROUP BY G.cd_grupo,G.nm_grupo,PS.cd_produto_servico,DS.cd_documento_saida,PV.nm_pessoa,PC.nm_pessoa, "+
						 "DSI.qt_saida,DSI.vl_unitario,PPDS.vl_pagamento,FP.nm_forma_pagamento,PP.nm_plano_pagamento," +
						 "PG.cd_empresa,PG.cd_produto_servico,DS.nr_documento_saida,PS.id_produto_servico,PS.nm_produto_servico,DS.cd_documento_saida ";

			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, cdTurno);
			pstmt.setTimestamp(2, new Timestamp(dtSaidaInicial.getTimeInMillis()));
			pstmt.setTimestamp(3, new Timestamp(dtSaidaFinal.getTimeInMillis()));
			rsm = new ResultSetMap(pstmt.executeQuery());
			String sqlSaida = "SELECT sum(A.qt_saida) AS qt_saida, " +
								"       SUM(A.qt_saida * C.vl_unitario + (C.vl_acrescimo * (A.qt_saida/C.qt_saida))) AS vl_saida " +
								"FROM alm_saida_local_item A, alm_documento_saida_item C, alm_documento_saida B " +
								"WHERE A.cd_empresa         = ? "+
								"  AND B.st_documento_saida = " +DocumentoSaidaServices.ST_CONCLUIDO+
								"  AND A.cd_documento_saida = C.cd_documento_saida " +
								"  AND A.cd_produto_servico = C.cd_produto_servico " +
								"  AND A.cd_empresa         = C.cd_empresa " +
								"  AND A.cd_item            = C.cd_item " +
								"  AND A.cd_documento_saida = B.cd_documento_saida " +
								"  AND C.qt_saida > 0 " +
								"  AND B.dt_documento_saida < ? " +
								"  AND C.cd_produto_servico = ?";

			String sqlEntrada = "SELECT SUM(A.qt_entrada) AS QT_ENTRADA, " +
								"       SUM(A.qt_entrada * C.vl_unitario + (C.vl_acrescimo * (A.qt_entrada/C.qt_entrada))) AS vl_entrada " +
								"FROM alm_entrada_local_item A, alm_documento_entrada B, alm_documento_entrada_item C " +
								"WHERE A.cd_empresa           = ? "+
								"  AND A.cd_documento_entrada = C.cd_documento_entrada " +
								"  AND A.cd_produto_servico   = C.cd_produto_servico " +
								"  AND A.cd_empresa           = C.cd_empresa " +
								"  AND A.cd_documento_entrada = B.cd_documento_entrada " +
								"  AND B.tp_entrada NOT IN ("+DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO+","+DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO+") "+
								"  AND B.st_documento_entrada = " +DocumentoEntradaServices.ST_LIBERADO+
								"  AND C.qt_entrada           > 0 " +
								"  AND B.dt_documento_entrada < ? " +
								"  AND C.cd_produto_servico = ?";

			String sqlEntradaDia = "SELECT SUM(A.qt_entrada) AS QT_ENTRADA, " +
								   "       SUM(A.qt_entrada * C.vl_unitario + (C.vl_acrescimo * (A.qt_entrada/C.qt_entrada))) AS vl_entrada " +
								   "FROM alm_entrada_local_item A, alm_documento_entrada B, alm_documento_entrada_item C " +
								   "WHERE A.cd_empresa           = ? "+
								   "  AND A.cd_documento_entrada = C.cd_documento_entrada " +
								   "  AND A.cd_produto_servico   = C.cd_produto_servico " +
								   "  AND A.cd_empresa           = C.cd_empresa " +
								   "  AND A.cd_documento_entrada = B.cd_documento_entrada " +
								   "  AND B.tp_entrada NOT IN ("+DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO+","+DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO+") "+
								   "  AND B.st_documento_entrada = " +DocumentoEntradaServices.ST_LIBERADO+
								   "  AND C.qt_entrada           > 0 " +
								   "  AND B.dt_documento_entrada = ? " +
								   "  AND C.cd_produto_servico = ?";

			String sqlSaidaDia = "SELECT sum(A.qt_saida) AS qt_saida, " +
					"       SUM(A.qt_saida * C.vl_unitario + (C.vl_acrescimo * (A.qt_saida/C.qt_saida))) AS vl_saida " +
					"FROM alm_saida_local_item A, alm_documento_saida_item C, alm_documento_saida B " +
					"WHERE A.cd_empresa         = ? "+
					"  AND B.st_documento_saida = " +DocumentoSaidaServices.ST_CONCLUIDO+
					"  AND A.cd_documento_saida = C.cd_documento_saida " +
					"  AND A.cd_produto_servico = C.cd_produto_servico " +
					"  AND A.cd_empresa         = C.cd_empresa " +
					"  AND A.cd_item            = C.cd_item " +
					"  AND A.cd_documento_saida = B.cd_documento_saida " +
					"  AND C.qt_saida > 0 " +
					"  AND B.dt_documento_saida BETWEEN ? AND ? " +
					"  AND C.cd_produto_servico = ?";

			/*
			 * PARTE ESTOQUE ESCRITURAL/INICIAL COMBUSTIVEL
			 */

			String sqlMedicaoFisicaDia = "SELECT SUM(qt_volume) as qt_volume FROM pcb_medicao_fisica MF " +
										 " JOIN pcb_tanque T ON(MF.cd_tanque = T.cd_tanque) " +
										 " JOIN adm_conta_fechamento CF ON(MF.cd_fechamento = CF.cd_fechamento) " +
										 " WHERE T.cd_produto_servico = ? AND CF.dt_fechamento BETWEEN ? AND ? " +
										 " GROUP BY T.cd_produto_servico ";

			String sqlMedicaoFisicaDiaAnterior = "SELECT SUM(qt_volume) as qt_volume FROM pcb_medicao_fisica MF " +
												 " JOIN pcb_tanque T ON(MF.cd_tanque = T.cd_tanque) " +
												 " JOIN adm_conta_fechamento CF ON(MF.cd_fechamento = CF.cd_fechamento) " +
												 " WHERE T.cd_produto_servico = ? AND CF.dt_fechamento BETWEEN ? AND ? " +
												 " GROUP BY T.cd_produto_servico ";

			String sqlEntradaMedicaoDiaAnterior = "SELECT SUM(A.qt_entrada) AS QT_ENTRADA, " +
											      "       SUM(A.qt_entrada * C.vl_unitario + (C.vl_acrescimo * (A.qt_entrada/C.qt_entrada))) AS vl_entrada " +
											      "FROM alm_entrada_local_item A, alm_documento_entrada B, alm_documento_entrada_item C " +
											      "WHERE A.cd_empresa           = ? "+
											      "  AND A.cd_documento_entrada = C.cd_documento_entrada " +
											      "  AND A.cd_produto_servico   = C.cd_produto_servico " +
											      "  AND A.cd_empresa           = C.cd_empresa " +
											      "  AND A.cd_documento_entrada = B.cd_documento_entrada " +
											      "  AND B.tp_entrada NOT IN ("+DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO+","+DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO+") "+
											      "  AND B.st_documento_entrada = " +DocumentoEntradaServices.ST_LIBERADO+
											      "  AND C.qt_entrada           > 0 " +
											      "  AND B.dt_documento_entrada BETWEEN ? AND ? " +
											      "  AND C.cd_produto_servico = ?";

			String sqlSaidaMedicaoDiaAnterior = "SELECT sum(A.qt_saida) AS qt_saida, " +
												"       SUM(A.qt_saida * C.vl_unitario + (C.vl_acrescimo * (A.qt_saida/C.qt_saida))) AS vl_saida " +
												"FROM alm_saida_local_item A, alm_documento_saida_item C, alm_documento_saida B " +
												"WHERE A.cd_empresa         = ? "+
												"  AND B.st_documento_saida = " +DocumentoSaidaServices.ST_CONCLUIDO+
												"  AND A.cd_documento_saida = C.cd_documento_saida " +
												"  AND A.cd_produto_servico = C.cd_produto_servico " +
												"  AND A.cd_empresa         = C.cd_empresa " +
												"  AND A.cd_item            = C.cd_item " +
												"  AND A.cd_documento_saida = B.cd_documento_saida " +
												"  AND C.qt_saida > 0 " +
												"  AND B.dt_documento_saida BETWEEN ? AND ? " +
												"  AND C.cd_produto_servico = ?";

			String sqlSaidaMes = "SELECT sum(A.qt_saida) AS qt_saida, " +
									"       SUM(A.qt_saida * C.vl_unitario + (C.vl_acrescimo * (A.qt_saida/C.qt_saida))) AS vl_saida " +
									"FROM alm_saida_local_item A, alm_documento_saida_item C, alm_documento_saida B " +
									"WHERE A.cd_empresa         = ? "+
									"  AND B.st_documento_saida = " +DocumentoSaidaServices.ST_CONCLUIDO+
									"  AND A.cd_documento_saida = C.cd_documento_saida " +
									"  AND A.cd_produto_servico = C.cd_produto_servico " +
									"  AND A.cd_empresa         = C.cd_empresa " +
									"  AND A.cd_item            = C.cd_item " +
									"  AND A.cd_documento_saida = B.cd_documento_saida " +
									"  AND C.qt_saida > 0 " +
									"  AND B.dt_documento_saida BETWEEN ? AND ? " +
									"  AND C.cd_produto_servico = ?";

			while(rsm.next()){
				PreparedStatement pstmtSaida = connect.prepareStatement(sqlSaida);
				pstmtSaida.setInt(1, rsm.getInt("CD_EMPRESA"));
				pstmtSaida.setTimestamp(2, new Timestamp(dtSaidaInicial.getTimeInMillis()));
				pstmtSaida.setInt(3, rsm.getInt("CD_PRODUTO_SERVICO"));
				PreparedStatement pstmtEntrada = connect.prepareStatement(sqlEntrada);
				pstmtEntrada.setInt(1, rsm.getInt("CD_EMPRESA"));
				pstmtEntrada.setTimestamp(2, new Timestamp(dtSaidaInicial.getTimeInMillis()));
				pstmtEntrada.setInt(3, rsm.getInt("CD_PRODUTO_SERVICO"));
				PreparedStatement pstmtEntradaDia = connect.prepareStatement(sqlEntradaDia);
				pstmtEntradaDia.setInt(1, rsm.getInt("CD_EMPRESA"));
				pstmtEntradaDia.setTimestamp(2, new Timestamp(dtSaidaInicial.getTimeInMillis()));
				pstmtEntradaDia.setInt(3, rsm.getInt("CD_PRODUTO_SERVICO"));
				PreparedStatement pstmtSaidaDia = connect.prepareStatement(sqlSaidaDia);
				pstmtSaidaDia.setInt(1, rsm.getInt("CD_EMPRESA"));
				pstmtSaidaDia.setTimestamp(2, new Timestamp(dtSaidaInicial.getTimeInMillis()));
				pstmtSaidaDia.setTimestamp(3, new Timestamp(dtSaidaFinal.getTimeInMillis()));
				pstmtSaidaDia.setInt(4, rsm.getInt("CD_PRODUTO_SERVICO"));
				ResultSet rsSaida = pstmtSaida.executeQuery();
				ResultSet rsEntrada = pstmtEntrada.executeQuery();
				ResultSet rsEntradaDia = pstmtEntradaDia.executeQuery();
				ResultSet rsSaidaDia = pstmtSaidaDia.executeQuery();
				rsEntrada.next();
				rsSaida.next();
				rsEntradaDia.next();
				rsSaidaDia.next();
				PreparedStatement pstmtSaidaMes = connect.prepareStatement(sqlSaidaMes);
				pstmtSaidaMes.setInt(1, cdEmpresa);
				pstmtSaidaMes.setTimestamp(2, new Timestamp(diaMesInicial.getTimeInMillis()));
				pstmtSaidaMes.setTimestamp(3, new Timestamp(diaMesFinal.getTimeInMillis()));
				pstmtSaidaMes.setInt(4, rsm.getInt("CD_PRODUTO_SERVICO"));
				ResultSet rsSaidaMes = pstmtSaidaMes.executeQuery();
				rsSaidaMes.next();

				rsm.setValueToField("QT_ESTOQUE",rsEntrada.getDouble("QT_ENTRADA") - rsSaida.getDouble("qt_saida"));
				rsm.setValueToField("QT_COMPRA", rsEntradaDia.getDouble("QT_ENTRADA"));
				rsm.setValueToField("QT_VENDAS", rsSaidaDia.getDouble("qt_saida"));
				rsm.setValueToField("QT_SAIDA_MES", rsSaidaMes.getDouble("QT_SAIDA"));

				if(cdGrupoCombustivel == cdGrupo){
					PreparedStatement pstmtMedicaoFisicaDia = connect.prepareStatement(sqlMedicaoFisicaDia);
					pstmtMedicaoFisicaDia.setInt(1, rsm.getInt("CD_PRODUTO_SERVICO"));
					pstmtMedicaoFisicaDia.setTimestamp(2, new Timestamp(dtSaidaInicial.getTimeInMillis()));
					pstmtMedicaoFisicaDia.setTimestamp(3, new Timestamp(dtSaidaFinal.getTimeInMillis()));
					ResultSet rsMedicaoDia = pstmtMedicaoFisicaDia.executeQuery();
					rsMedicaoDia.next();

					PreparedStatement pstmtMedicaoFisicaDiaAnterior = connect.prepareStatement(sqlMedicaoFisicaDiaAnterior);
					pstmtMedicaoFisicaDiaAnterior.setInt(1, rsm.getInt("CD_PRODUTO_SERVICO"));
					pstmtMedicaoFisicaDiaAnterior.setTimestamp(2, new Timestamp(diaAnteriorInicial.getTimeInMillis()));
					pstmtMedicaoFisicaDiaAnterior.setTimestamp(3, new Timestamp(diaAnteriorFinal.getTimeInMillis()));
					ResultSet rsMedicaoDiaAnterior = pstmtMedicaoFisicaDiaAnterior.executeQuery();
					rsMedicaoDiaAnterior.next();

					PreparedStatement pstmtMedicaoEntradaDiaAnterior = connect.prepareStatement(sqlEntradaMedicaoDiaAnterior);
					pstmtMedicaoEntradaDiaAnterior.setInt(1, cdEmpresa);
					pstmtMedicaoEntradaDiaAnterior.setTimestamp(2, new Timestamp(diaAnteriorInicial.getTimeInMillis()));
					pstmtMedicaoEntradaDiaAnterior.setTimestamp(3, new Timestamp(diaAnteriorFinal.getTimeInMillis()));
					pstmtMedicaoEntradaDiaAnterior.setInt(4, rsm.getInt("CD_PRODUTO_SERVICO"));
					ResultSet rsMedicaoEntradaDiaAnterior = pstmtMedicaoEntradaDiaAnterior.executeQuery();
					rsMedicaoEntradaDiaAnterior.next();

					PreparedStatement pstmtMedicaoSaidaDiaAnterior = connect.prepareStatement(sqlSaidaMedicaoDiaAnterior);
					pstmtMedicaoSaidaDiaAnterior.setInt(1, cdEmpresa);
					pstmtMedicaoSaidaDiaAnterior.setTimestamp(2, new Timestamp(diaAnteriorInicial.getTimeInMillis()));
					pstmtMedicaoSaidaDiaAnterior.setTimestamp(3, new Timestamp(diaAnteriorFinal.getTimeInMillis()));
					pstmtMedicaoSaidaDiaAnterior.setInt(4, rsm.getInt("CD_PRODUTO_SERVICO"));
					ResultSet rsMedicaoSaidaDiaAnterior = pstmtMedicaoSaidaDiaAnterior.executeQuery();
					rsMedicaoSaidaDiaAnterior.next();

					rsm.setValueToField("QT_MEDICAO_FISICA", rsMedicaoDia.getDouble("QT_VOLUME"));
					rsm.setValueToField("QT_ESTOQUE_INICIAL",(rsMedicaoDiaAnterior.getDouble("QT_VOLUME") + rsMedicaoEntradaDiaAnterior.getDouble("QT_ENTRADA")) - rsMedicaoSaidaDiaAnterior.getDouble("qt_saida"));
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaItemDAO.getAllItensDocumentoSaida: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findResumoOfItensByGrupo(int cdTurno, GregorianCalendar dtSaida){
		Connection connect = Conexao.conectar();
		try {
			dtSaida.set(Calendar.HOUR, 0);
			dtSaida.set(Calendar.MINUTE, 0);
			dtSaida.set(Calendar.SECOND, 0);
			dtSaida.set(Calendar.MILLISECOND, 0);

			String sql = "SELECT G.cd_grupo, G.nm_grupo, SUM(DSI.qt_saida * DSI.vl_unitario + DSI.vl_acrescimo - DSI.vl_desconto) AS vl_total_liquido "+
						 "FROM alm_documento_saida_item     DSI " +
						 "INNER JOIN alm_documento_saida    DS ON (DS.cd_documento_saida = DSI.cd_documento_saida) "+
						 "LEFT OUTER JOIN alm_produto_grupo PG ON (PG.cd_produto_servico = DSI.cd_produto_servico " +
						 "                                     AND PG.cd_empresa         = DSI.cd_empresa " +
						 "                                     AND PG.lg_principal       = 1) " +
						 "LEFT OUTER JOIN alm_grupo         G  ON (PG.cd_grupo = G.cd_grupo) "+
						 "WHERE DS.cd_turno                         = " +cdTurno+
						 "  AND CAST(DS.dt_documento_saida AS DATE) = ? "+
						 "GROUP BY G.cd_grupo, G.nm_grupo ";

			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtSaida.getTimeInMillis()));
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaItemDAO.getAllItensDocumentoSaida: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findSaidaAndItensByProduto(int cdTurno, int cdEmpresa, GregorianCalendar dtSaidaInicial, GregorianCalendar dtSaidaFinal, boolean combustivel)
	{
		return findSaidaAndItensByProduto(cdTurno,cdEmpresa,dtSaidaInicial,dtSaidaFinal,0,combustivel,null);
	}

	public static ResultSetMap findSaidaAndItensByProduto(int cdTurno, int cdEmpresa, GregorianCalendar dtSaidaInicial, GregorianCalendar dtSaidaFinal, int cdGrupo, boolean combustivel, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			PreparedStatement pstmt;
			String codigosCombustivel = GrupoServices.getAllCombustivel(cdEmpresa, connect);
//			int cdGrupoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa);
			dtSaidaInicial.set(Calendar.HOUR, 0);
			dtSaidaInicial.set(Calendar.MINUTE, 0);
			dtSaidaInicial.set(Calendar.SECOND, 0);

			dtSaidaFinal.set(Calendar.HOUR, 23);
			dtSaidaFinal.set(Calendar.MINUTE, 59);
			dtSaidaFinal.set(Calendar.SECOND, 59);
			String sql = "SELECT DSI.cd_produto_servico, PSE.id_reduzido,PV.nm_pessoa as nm_vendedor, PS.nm_produto_servico," +
						 "       DSI.vl_unitario, PSE.qt_precisao_custo, " +
						 "		 SUM(DSI.qt_saida) AS qt_total_saida, SUM(DSI.qt_saida) AS qt_saida, "+
						 "		 SUM(DSI.qt_saida * DSI.vl_unitario + DSI.vl_acrescimo - DSI.vl_desconto) AS vl_total_liquido,"+
						 "		 SUM(DSI.qt_saida * DSI.vl_unitario) AS vl_total_saida,"+
						 "		 SUM(DSI.vl_desconto) AS vl_total_descontos, " +
						 "		 SUM(DSI.vl_acrescimo) AS vl_total_acrescimo " +
						 "FROM alm_documento_saida_item DSI " +
						 "INNER JOIN alm_documento_saida DS ON(DS.cd_documento_saida = DSI.cd_documento_saida) "+
						 "INNER JOIN grl_produto_servico PS ON(DSI.cd_produto_servico = PS.cd_produto_servico) " +
						 "INNER JOIN grl_produto_servico_empresa PSE ON(DSI.cd_produto_servico = PSE.cd_produto_servico AND PSE.cd_empresa = DS.cd_empresa) " +
						 "LEFT OUTER JOIN alm_produto_grupo PG ON(PG.cd_produto_servico = PS.cd_produto_servico AND " +
						 "							   			  DS.cd_empresa = PG.cd_empresa AND PG.lg_principal = 1) " +
						 "LEFT OUTER JOIN alm_grupo G ON(PG.cd_grupo = G.cd_grupo) "+
						 "LEFT OUTER JOIN grl_pessoa     PV ON(DS.cd_vendedor = PV.cd_pessoa) "+
						 "LEFT OUTER JOIN grl_pessoa	PC ON(DS.cd_cliente  = PC.cd_pessoa) "+
						 "WHERE DS.dt_documento_saida BETWEEN ? AND ? " +
						 "  AND DS.cd_empresa = " +cdEmpresa+
						 (cdTurno > 0 ? " AND DS.cd_turno = "+cdTurno : "" ) +
						 (cdGrupo         >0 ? " AND G.cd_grupo  = "+ cdGrupo : "") +
						 (!combustivel ? " AND G.cd_grupo NOT IN "+ codigosCombustivel : "")+
						 " GROUP BY DSI.cd_produto_servico,PV.nm_pessoa,PS.nm_produto_servico,PSE.id_reduzido,DSI.vl_unitario,PSE.qt_precisao_custo " +
						 " ORDER BY nm_produto_servico";
			Util.printInFile("/pdv.log", sql+"\n");
			pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtSaidaInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtSaidaFinal.getTimeInMillis()));
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findSaidaByProduto(int cdTurno, int cdProdutoServico,int cdEmpresa, GregorianCalendar dtSaida)	{
		return findSaidaByProduto(cdTurno, cdProdutoServico, cdEmpresa, dtSaida, 0, null);
	}

	public static ResultSetMap findSaidaByProduto(int cdTurno, int cdProdutoServico, int cdEmpresa, GregorianCalendar dtSaida, int cdGrupo)	{
		return findSaidaByProduto(cdTurno, cdProdutoServico, cdEmpresa,dtSaida, cdGrupo, null);
	}

	public static ResultSetMap findSaidaByProduto(int cdTurno, int cdProdutoServico, int cdEmpresa, GregorianCalendar dtSaida, int cdGrupo, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			dtSaida.set(Calendar.HOUR, 0);
			dtSaida.set(Calendar.MINUTE, 0);
			dtSaida.set(Calendar.SECOND, 0);
			dtSaida.set(Calendar.MILLISECOND, 0);

			String sql = "SELECT DSI.cd_produto_servico, PSE.id_reduzido, PS.nm_produto_servico, DSI.vl_unitario, PSE.qt_precisao_custo, " +
						 "       SUM(DSI.qt_saida) AS qt_saida, " +
						 "       SUM(DSI.vl_acrescimo) AS vl_acrescimo, " +
						 "       SUM(DSI.vl_desconto) AS vl_desconto " +
						 "FROM alm_documento_saida_item          DSI " +
						 "INNER JOIN alm_documento_saida         DS  ON (DS.cd_documento_saida  = DSI.cd_documento_saida) "+
						 "INNER JOIN grl_produto_servico         PS  ON (DSI.cd_produto_servico = PS.cd_produto_servico) " +
						 "INNER JOIN grl_produto_servico_empresa PSE ON (DSI.cd_produto_servico = PSE.cd_produto_servico " +
						 "                                           AND PSE.cd_empresa = DS.cd_empresa) " +
						 "INNER JOIN alm_produto_grupo           G   ON (DSI.cd_produto_servico = G.cd_produto_servico " +
						 "                                           AND G.lg_principal = 1) "+
						 "WHERE CAST(DS.dt_documento_saida AS DATE) = ? " +
						 "  AND DS.cd_empresa = " +cdEmpresa+
						 (cdTurno          > 0 ? " AND DS.cd_turno = "+cdTurno : "") +
						 (cdProdutoServico > 0 ? " AND DSI.cd_produto_servico = " + cdProdutoServico:"") +
						 (cdGrupo          > 0 ? " AND G.cd_grupo = " + cdGrupo : "")+
						 " GROUP BY DSI.cd_produto_servico, PSE.id_reduzido, PS.nm_produto_servico, DSI.vl_unitario, PSE.qt_precisao_custo ";
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtSaida.getTimeInMillis()));
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}


	public static ResultSetMap findSaidaByDiaOfProduto(int cdProdutoServico,int cdEmpresa, GregorianCalendar dtMovimento, int cdGrupo)	{
		return findSaidaByDiaOfProduto(cdProdutoServico,cdEmpresa, dtMovimento, cdGrupo,0,false,true,null);
	}

	public static ResultSetMap findSaidaByDiaOfProduto(int cdProdutoServico,int cdEmpresa, GregorianCalendar dtMovimento,int cdGrupo, int cdTurno)	{
		return findSaidaByDiaOfProduto(cdProdutoServico,cdEmpresa,dtMovimento, cdGrupo,cdTurno,false,true,null);
	}

	public static ResultSetMap findSaidaByDiaOfProduto(int cdProdutoServico,int cdEmpresa, GregorianCalendar dtMovimento,int cdGrupo, int cdTurno,boolean grupo,boolean combustivel)	{
		return findSaidaByDiaOfProduto(cdProdutoServico,cdEmpresa,dtMovimento,cdGrupo,cdTurno,grupo,combustivel,null);
	}

	public static ResultSetMap findSaidaByDiaOfProduto(int cdProdutoServico,int cdEmpresa,GregorianCalendar dtMovimento, int cdGrupo, int cdTurno, boolean grupo, boolean combustivel, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			PreparedStatement pstmt;
			ResultSetMap rsm=null;
			dtMovimento.set(Calendar.HOUR, 0);
			dtMovimento.set(Calendar.MINUTE, 0);
			dtMovimento.set(Calendar.SECOND, 0);
			dtMovimento.set(Calendar.MILLISECOND, 0);

			int cdGrupoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa);
			
			ArrayList<Integer> gruposCombustivelList = GrupoServices.getAllCombustivelAsArray(cdEmpresa, connect);
			String gruposCombustivel = "";
			for( Integer grupoCombustivel : gruposCombustivelList ){
				gruposCombustivel += grupoCombustivel+",";
			}
			gruposCombustivel = gruposCombustivel.substring( 0 , gruposCombustivel.length()-1);
			
			
			GregorianCalendar diaMesInicial = (GregorianCalendar) dtMovimento.clone();
			diaMesInicial.set(Calendar.DAY_OF_MONTH, 1);
			GregorianCalendar diaMesFinal = (GregorianCalendar) dtMovimento.clone();
			diaMesFinal.set(Calendar.DAY_OF_MONTH, diaMesFinal.getMaximum(Calendar.DAY_OF_MONTH));

			GregorianCalendar diaAnteriorInicial = (GregorianCalendar) dtMovimento.clone();
			diaAnteriorInicial.add(Calendar.DAY_OF_MONTH, -1);
			GregorianCalendar diaAnteriorFinal = (GregorianCalendar) dtMovimento.clone();
			diaAnteriorFinal.add(Calendar.DAY_OF_MONTH, -1);

			String sql = "SELECT DSI.cd_produto_servico, PSE.id_reduzido,PV.nm_pessoa as nm_vendedor, PS.nm_produto_servico," +
						 "       DSI.vl_unitario, PSE.qt_precisao_custo, " +
						 "       SUM(DSI.qt_saida) AS qt_total_saida, "+
						 "       SUM(DSI.qt_saida * DSI.vl_unitario + DSI.vl_acrescimo - DSI.vl_desconto) AS vl_total_liquido,"+
						 "       SUM(DSI.qt_saida * DSI.vl_unitario) AS vl_total_saida,"+
						 "       SUM(DSI.vl_desconto) AS vl_total_descontos, " +
						 "       SUM(DSI.vl_acrescimo) AS vl_total_acrescimo " +
						 (grupo ? " ,G.cd_grupo, G.nm_grupo " : "") +
						 "FROM alm_documento_saida_item DSI " +
						 "INNER JOIN alm_documento_saida         DS  ON (DS.cd_documento_saida = DSI.cd_documento_saida) "+
						 "INNER JOIN grl_produto_servico         PS  ON (DSI.cd_produto_servico = PS.cd_produto_servico) " +
						 "INNER JOIN grl_produto_servico_empresa PSE ON (DSI.cd_produto_servico = PSE.cd_produto_servico " +
						 "                                           AND PSE.cd_empresa = DS.cd_empresa) " +
						 "LEFT OUTER JOIN alm_produto_grupo      PG  ON (PG.cd_produto_servico = PS.cd_produto_servico " +
						 "							   			     AND DS.cd_empresa = PG.cd_empresa " +
						 "                                           AND PG.lg_principal = 1) " +
						 "LEFT OUTER JOIN alm_grupo              G   ON (PG.cd_grupo = G.cd_grupo) "+
						 "LEFT OUTER JOIN grl_pessoa             PV  ON (DS.cd_vendedor = PV.cd_pessoa) "+
						 "LEFT OUTER JOIN grl_pessoa	         PC  ON (DS.cd_cliente  = PC.cd_pessoa) "+
						 "WHERE CAST(DS.dt_documento_saida AS DATE) = ? " +
						 "  AND DS.cd_empresa = " +cdEmpresa+
						 (cdGrupo>0            ? " AND G.cd_grupo = "+ cdGrupo:"") +
						 (cdProdutoServico > 0 ? " AND DSI.cd_produto_servico = "+cdProdutoServico:"") +
						 (combustivel == false ? " AND G.cd_grupo NOT IN ("+gruposCombustivel+") ":"") +
						 (cdTurno > 0          ? " AND DS.cd_turno = "+cdTurno:"") +
						 " GROUP BY "+(grupo?" G.cd_grupo, G.nm_grupo, ":"") +
						 "       DSI.cd_produto_servico,PV.nm_pessoa,PS.nm_produto_servico,PSE.id_reduzido,DSI.vl_unitario,PSE.qt_precisao_custo " +
						 (grupo?" ORDER BY G.nm_grupo ":"");
			
			pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			rsm = new ResultSetMap(pstmt.executeQuery());
			String sqlEntradaDia = "SELECT SUM(A.qt_entrada) AS QT_ENTRADA, " +
								   "       SUM(A.qt_entrada * C.vl_unitario + (C.vl_acrescimo * (A.qt_entrada/C.qt_entrada))) AS vl_entrada " +
								   "FROM alm_entrada_local_item A, alm_documento_entrada B, alm_documento_entrada_item C " +
								   "WHERE A.cd_empresa           = "+cdEmpresa+
								   "  AND A.cd_documento_entrada = C.cd_documento_entrada " +
								   "  AND A.cd_produto_servico   = C.cd_produto_servico " +
								   "  AND A.cd_empresa           = C.cd_empresa " +
								   "  AND A.cd_documento_entrada = B.cd_documento_entrada " +
								   "  AND B.tp_entrada      NOT IN ("+DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO+","+DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO+") "+
								   "  AND B.st_documento_entrada = " +DocumentoEntradaServices.ST_LIBERADO+
								   "  AND C.qt_entrada           > 0 " +
								   "  AND CAST(B.dt_documento_entrada AS DATE) = ? " +
								   "  AND C.cd_produto_servico                 = ? ";
			PreparedStatement pstmtEntradaDia = connect.prepareStatement(sqlEntradaDia);
			//
			String sqlSaidaDia = "SELECT sum(A.qt_saida) AS qt_saida, " +
								 "       SUM(A.qt_saida * C.vl_unitario + (C.vl_acrescimo * (A.qt_saida/C.qt_saida))) AS vl_saida " +
								 "FROM alm_saida_local_item A, alm_documento_saida_item C, alm_documento_saida B " +
								 "WHERE A.cd_empresa         = "+cdEmpresa+
								 "  AND B.st_documento_saida = " +DocumentoSaidaServices.ST_CONCLUIDO+
								 "  AND A.cd_documento_saida = C.cd_documento_saida " +
								 "  AND A.cd_produto_servico = C.cd_produto_servico " +
								 "  AND A.cd_empresa         = C.cd_empresa " +
								 "  AND A.cd_item            = C.cd_item " +
								 "  AND A.cd_documento_saida = B.cd_documento_saida " +
								 "  AND C.qt_saida > 0 " +
								 "  AND CAST(B.dt_documento_saida AS DATE) = ? " +
								 "  AND C.cd_produto_servico               = ? ";
			PreparedStatement pstmtSaidaDia = connect.prepareStatement(sqlSaidaDia);
			//
			String sqlEntradaDiaAnterior = "SELECT SUM(A.qt_entrada) AS QT_ENTRADA, " +
										   "       SUM(A.qt_entrada * C.vl_unitario + (C.vl_acrescimo * (A.qt_entrada/C.qt_entrada))) AS vl_entrada " +
										   "FROM alm_entrada_local_item A, alm_documento_entrada B, alm_documento_entrada_item C " +
										   "WHERE A.cd_empresa           = "+cdEmpresa+
										   "  AND A.cd_documento_entrada = C.cd_documento_entrada " +
										   "  AND A.cd_produto_servico   = C.cd_produto_servico " +
										   "  AND A.cd_empresa           = C.cd_empresa " +
										   "  AND A.cd_documento_entrada = B.cd_documento_entrada " +
										   "  AND B.tp_entrada      NOT IN ("+DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO+","+DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO+") "+
										   "  AND B.st_documento_entrada = " +DocumentoEntradaServices.ST_LIBERADO+
										   "  AND C.qt_entrada           > 0 " +
										   "  AND B.dt_documento_entrada < ? " +
										   "  AND C.cd_produto_servico = ?";
			PreparedStatement pstmtEntradaDiaAnterior = connect.prepareStatement(sqlEntradaDiaAnterior);
			//
			String sqlSaidaDiaAnterior = "SELECT sum(A.qt_saida) AS qt_saida, " +
										 "       SUM(A.qt_saida * C.vl_unitario + (C.vl_acrescimo * (A.qt_saida/C.qt_saida))) AS vl_saida " +
										 "FROM alm_saida_local_item A, alm_documento_saida_item C, alm_documento_saida B " +
										 "WHERE A.cd_empresa         = "+cdEmpresa+
										 "  AND B.st_documento_saida = " +DocumentoSaidaServices.ST_CONCLUIDO+
										 "  AND A.cd_documento_saida = C.cd_documento_saida " +
										 "  AND A.cd_produto_servico = C.cd_produto_servico " +
										 "  AND A.cd_empresa         = C.cd_empresa " +
										 "  AND A.cd_item            = C.cd_item " +
										 "  AND A.cd_documento_saida = B.cd_documento_saida " +
										 "  AND C.qt_saida > 0 " +
										 "  AND B.dt_documento_saida < ? " +
										 "  AND C.cd_produto_servico = ?";
			PreparedStatement pstmtSaidaDiaAnterior = connect.prepareStatement(sqlSaidaDiaAnterior);
			//
			String sqlSaidaMes = "SELECT sum(A.qt_saida) AS qt_saida, " +
								 "       SUM(A.qt_saida * C.vl_unitario + (C.vl_acrescimo * (A.qt_saida/C.qt_saida))) AS vl_saida " +
								 "FROM alm_saida_local_item A, alm_documento_saida_item C, alm_documento_saida B " +
								 "WHERE A.cd_empresa         = "+cdEmpresa+
								 "  AND B.st_documento_saida = " +DocumentoSaidaServices.ST_CONCLUIDO+
								 "  AND A.cd_documento_saida = C.cd_documento_saida " +
								 "  AND A.cd_produto_servico = C.cd_produto_servico " +
								 "  AND A.cd_empresa         = C.cd_empresa " +
								 "  AND A.cd_item            = C.cd_item " +
								 "  AND A.cd_documento_saida = B.cd_documento_saida " +
								 "  AND C.qt_saida           > 0 " +
								 "  AND CAST(B.dt_documento_saida AS DATE) = ? " +
								 "  AND C.cd_produto_servico               = ?";
			PreparedStatement pstmtSaidaMes = connect.prepareStatement(sqlSaidaMes);

			/*
			 * PARTE ESTOQUE ESCRITURAL/INICIAL COMBUSTIVEL
			 */

			String sqlMedicaoFisicaDia = "SELECT MF.qt_volume FROM pcb_medicao_fisica MF " +
										 "JOIN pcb_tanque T ON(MF.cd_tanque = T.cd_tanque) " +
										 "JOIN adm_conta_fechamento CF ON(MF.cd_fechamento = CF.cd_fechamento) " +
										 "WHERE T.cd_produto_servico = ?" +
										 "  AND CF.dt_fechamento BETWEEN ? AND ? " +
										 "  AND MF.qt_volume > 0 " +
										 "ORDER BY CF.dt_fechamento";
			PreparedStatement pstmtMedicaoFisicaDia = connect.prepareStatement(sqlMedicaoFisicaDia);
			//
			String sqlMedicaoFisicaDiaAnterior = "SELECT SUM(qt_volume) as qt_volume FROM pcb_medicao_fisica MF " +
												 " JOIN pcb_tanque T ON(MF.cd_tanque = T.cd_tanque) " +
												 " JOIN adm_conta_fechamento CF ON(MF.cd_fechamento = CF.cd_fechamento) " +
												 " WHERE T.cd_produto_servico = ? AND CF.dt_fechamento BETWEEN ? AND ? " +
												 " GROUP BY T.cd_produto_servico ";
			PreparedStatement pstmtMedicaoFisicaDiaAnterior = connect.prepareStatement(sqlMedicaoFisicaDiaAnterior);
			//
			while(rsm.next())	{
				/*
				 * ENTRADAS
				 */
				pstmtEntradaDia.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
				pstmtEntradaDia.setInt(2, rsm.getInt("CD_PRODUTO_SERVICO"));
				ResultSet rsEntradaDia = pstmtEntradaDia.executeQuery();
				if(rsEntradaDia.next())
					rsm.setValueToField("QT_COMPRA", rsEntradaDia.getDouble("QT_ENTRADA"));
				/*
				 * SAÍDAS
				 */
				pstmtSaidaDia.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
				pstmtSaidaDia.setInt(2, rsm.getInt("CD_PRODUTO_SERVICO"));
				ResultSet rsSaidaDia = pstmtSaidaDia.executeQuery();
				rsSaidaDia.next();
				rsm.setValueToField("QT_VENDAS", rsSaidaDia.getDouble("QT_SAIDA"));
				/*
				 * ENTRADAS DIA ANTERIOR
				 */
				pstmtEntradaDiaAnterior.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
				pstmtEntradaDiaAnterior.setInt(2, rsm.getInt("CD_PRODUTO_SERVICO"));
				ResultSet rsEntradaDiaAnterior = pstmtEntradaDiaAnterior.executeQuery();
				rsEntradaDiaAnterior.next();
				/*
				 * SAÍDAS DIA ANTERIOR
				 */
				pstmtSaidaDiaAnterior.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
				pstmtSaidaDiaAnterior.setInt(2, rsm.getInt("CD_PRODUTO_SERVICO"));
				ResultSet rsSaidaDiaAnterior = pstmtSaidaDiaAnterior.executeQuery();
				rsSaidaDiaAnterior.next();
				rsm.setValueToField("QT_ESTOQUE", rsEntradaDiaAnterior.getDouble("QT_ENTRADA") - rsSaidaDiaAnterior.getDouble("qt_saida"));
				/*
				 * SAÍDAS DO MÊS
				 */
				pstmtSaidaMes.setTimestamp(1, new Timestamp(diaMesInicial.getTimeInMillis()));
				pstmtSaidaMes.setInt(2, rsm.getInt("CD_PRODUTO_SERVICO"));
				ResultSet rsSaidaMes = pstmtSaidaMes.executeQuery();
				rsSaidaMes.next();
				rsm.setValueToField("QT_SAIDA_MES", rsSaidaMes.getDouble("QT_SAIDA"));

				if(cdGrupoCombustivel == cdGrupo){
					pstmtMedicaoFisicaDia.setInt(1, rsm.getInt("CD_PRODUTO_SERVICO"));
					pstmtMedicaoFisicaDia.setTimestamp(2, new Timestamp(dtMovimento.getTimeInMillis()));
					ResultSet rsMedicaoDia = pstmtMedicaoFisicaDia.executeQuery();
					rsMedicaoDia.next();
					//
					pstmtMedicaoFisicaDiaAnterior.setInt(1, rsm.getInt("CD_PRODUTO_SERVICO"));
					pstmtMedicaoFisicaDiaAnterior.setTimestamp(1, new Timestamp(diaAnteriorInicial.getTimeInMillis()));
					ResultSet rsMedicaoDiaAnterior = pstmtMedicaoFisicaDiaAnterior.executeQuery();
					rsMedicaoDiaAnterior.next();

					rsm.setValueToField("QT_ESTOQUE_INICIAL", rsMedicaoDia.getDouble("QT_VOLUME"));
				}

			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaItemDAO.getAllItensDocumentoSaida: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static ResultSetMap getRelatorioConsolidacaoResumoVendasProdutos( ResultSetMap rsmFechamentos,int cdEmpresa ){
		return getRelatorioConsolidacaoResumoVendasProdutos(rsmFechamentos, cdEmpresa, null);
	}
	
	public static ResultSetMap getRelatorioConsolidacaoResumoVendasProdutos( ResultSetMap rsmFechamentos, int cdEmpresa, Connection connect ){
		boolean isConnectionNull = connect==null;
		ResultSetMap rsmVendasProdutos = new ResultSetMap();
		ResultSetMap rsmTmpVendasDias = new ResultSetMap();
		rsmFechamentos.beforeFirst();
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			while(rsmFechamentos.next()){
				rsmTmpVendasDias = DocumentoSaidaItemServices.findSaidaByDiaOfProduto(0, cdEmpresa, rsmFechamentos.getGregorianCalendar("DT_FECHAMENTO"), 0, rsmFechamentos.getInt("CD_TURNO"), true, false);
				rsmTmpVendasDias.beforeFirst();
				while( rsmTmpVendasDias.next() ){
					boolean hasRegister = false;
					while( rsmVendasProdutos.next() ){
						if( rsmVendasProdutos.getInt("CD_PRODUTO_SERVICO") == rsmTmpVendasDias.getInt("CD_PRODUTO_SERVICO") ){
							//CONTABILIZA TOTAL E SINALIZA QUE JÁ POSSUI O REGISTRO NO RSM
							
							Double qtTotalSaida = rsmVendasProdutos.getDouble("QT_TOTAL_SAIDA")+rsmTmpVendasDias.getDouble("QT_TOTAL_SAIDA");
							Double vlTotalSaida = rsmVendasProdutos.getDouble("VL_TOTAL_SAIDA")+rsmTmpVendasDias.getDouble("VL_TOTAL_SAIDA");
							Double vlTotalDesconto = rsmVendasProdutos.getDouble("VL_TOTAL_DESCONTO")+rsmTmpVendasDias.getDouble("VL_TOTAL_DESCONTO");
							Double vlTotalAcrescimo = rsmVendasProdutos.getDouble("VL_TOTAL_ACRESCIMO")+rsmTmpVendasDias.getDouble("VL_TOTAL_ACRESCIMO");
							Double qtEstoque = rsmVendasProdutos.getDouble("QT_ESTOQUE")+rsmTmpVendasDias.getDouble("QT_ESTOQUE");
							Double qtVendas = rsmVendasProdutos.getDouble("QT_VENDAS")+rsmTmpVendasDias.getDouble("QT_VENDAS");
							Double qtCompra = rsmVendasProdutos.getDouble("QT_COMPRA")+rsmTmpVendasDias.getDouble("QT_COMPRA");
							Double qtSaidaMes = rsmVendasProdutos.getDouble("QT_SAIDA_MES")+rsmTmpVendasDias.getDouble("QT_SAIDA_MES");
								
							rsmVendasProdutos.setValueToField("QT_TOTAL_SAIDA", qtTotalSaida);
							rsmVendasProdutos.setValueToField("VL_TOTAL_SAIDA", vlTotalSaida);
							rsmVendasProdutos.setValueToField("VL_TOTAL_DESCONTO", vlTotalDesconto);
							rsmVendasProdutos.setValueToField("VL_TOTAL_ACRESCIMO", vlTotalAcrescimo);
							rsmVendasProdutos.setValueToField("QT_ESTOQUE", qtEstoque);
							rsmVendasProdutos.setValueToField("QT_VENDAS", qtVendas);
							rsmVendasProdutos.setValueToField("QT_COMPRA", qtCompra);
							rsmVendasProdutos.setValueToField("QT_SAIDA_MES", qtSaidaMes);
								hasRegister = true;
						}
					}
					if(!hasRegister){
						rsmVendasProdutos.addRegister( rsmTmpVendasDias.getRegister() );
					}
					rsmVendasProdutos.beforeFirst();
				}
			}
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("NM_GRUPO");
			rsmVendasProdutos.orderBy(orderBy);
			return rsmVendasProdutos;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findResumoOfItensByGrupoPCB(int cdConta, int cdFechamento, GregorianCalendar dtMovimento, int cdTurno){
		Empresa empresa = EmpresaServices.getDefaultEmpresa();
		return findResumoOfItensByGrupoPCB(cdConta, cdFechamento, dtMovimento, cdTurno, false, empresa.getCdEmpresa());
	}
	
	public static ResultSetMap findResumoOfItensByGrupoPCB(int cdConta, int cdFechamento, GregorianCalendar dtMovimento, int cdTurno, boolean notGrupoPrincipal, int cdEmpresa){
		Connection connect = Conexao.conectar();
		try{
			ContaFinanceira conta = ContaFinanceiraDAO.get(cdConta, connect);
			if(cdFechamento <= 0) {
				ResultSetMap rsmFechamento = ContaFechamentoServices.getFechamentoOf(cdConta, dtMovimento);
				if(rsmFechamento.next())
					cdFechamento = rsmFechamento.getInt("cd_fechamento");
			}
			ContaFechamento fechamento = ContaFechamentoDAO.get(cdConta, cdFechamento, connect);
			if(fechamento!=null && fechamento.getDtFechamento()!=null)
				dtMovimento = fechamento.getDtFechamento(); 
			dtMovimento.set(Calendar.HOUR, 0);
			dtMovimento.set(Calendar.MINUTE, 0);
			dtMovimento.set(Calendar.SECOND, 0);
			dtMovimento.set(Calendar.MILLISECOND, 0);
			int cdGrupoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, conta.getCdEmpresa());
			/*
			 * Resumo por Grupo (EXCETO COMBUSTÍVEL)
			 */
			String sql = "SELECT G.cd_grupo, G.nm_grupo, SUM(DSI.qt_saida * DSI.vl_unitario) AS vl_total "+
						 "FROM alm_documento_saida_item     DSI " +
						 "INNER JOIN alm_documento_saida    DS ON (DS.cd_documento_saida = DSI.cd_documento_saida) "+
						 "LEFT OUTER JOIN alm_produto_grupo PG ON (PG.cd_produto_servico = DSI.cd_produto_servico " +
						 "                                     AND PG.cd_empresa         = DSI.cd_empresa " +
						 "                                     AND PG.lg_principal       = 1) " +
						 "LEFT OUTER JOIN alm_grupo         G  ON (PG.cd_grupo = G.cd_grupo) "+
						 "WHERE DS.cd_turno                         = " +cdTurno+
						 "  AND DS.cd_conta                         = " +cdConta+
						 "  AND CAST(DS.dt_documento_saida AS DATE) = ? " +
						 "  AND DS.st_documento_saida               = " + DocumentoSaidaServices.ST_CONCLUIDO +
						 (cdFechamento > 0 ? "  AND G.cd_grupo NOT IN " + GrupoServices.getAllCombustivel(cdEmpresa, connect) : "")+
						 (notGrupoPrincipal ? " AND NOT EXISTS (SELECT * FROM alm_grupo GT WHERE GT.cd_grupo_superior = G.cd_grupo)" : "")+
						 " GROUP BY G.cd_grupo, G.nm_grupo ";
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			/*
			 * COMBUSTÍVEIS
			 */
			if(cdGrupoCombustivel > 0) {
				ResultSetMap rsmCombustivel        = new ResultSetMap(connect.prepareStatement("SELECT APG.cd_grupo, AG.nm_grupo, SUM(vl_total) as VL_TOTAL FROM pcb_bico_encerrante BE " +
						                                                                       "JOIN adm_conta_fechamento CF ON(BE.cd_fechamento = CF.cd_fechamento) " +
						                                                                       "JOIN grl_produto_servico GPS ON(BE.cd_combustivel = GPS.cd_produto_servico) " +
						                                                                       "JOIN alm_produto_grupo APG ON(GPS.cd_produto_servico = APG.cd_produto_servico" + 
						                                                                       "								AND APG.lg_principal = 1) " +
						                                                                       "JOIN alm_grupo AG ON (APG.cd_grupo = AG.cd_grupo) " +
						                                                                       "WHERE CF.cd_fechamento = "+cdFechamento +
						                                                                       "  AND APG.cd_grupo <> " + cdGrupoCombustivel +
						                                                                       "  AND CF.cd_turno      = "+cdTurno+
						                                                                       "GROUP BY APG.cd_grupo, AG.nm_grupo").executeQuery());
//				rsmCombustivel.next();
//				Double vlTotal = (java.lang.Double)rsmCombustivel.getRegister().get("VL_TOTAL");
				while(rsmCombustivel.next()) {
					// Criando registro
					HashMap<String, Object> register   = new HashMap<String, Object>();
					register.put("CD_GRUPO", rsmCombustivel.getInt("cd_grupo"));
					register.put("NM_GRUPO", rsmCombustivel.getString("nm_grupo"));
					register.put("VL_TOTAL", rsmCombustivel.getDouble("VL_TOTAL"));
					
					rsm.addRegister(register); // Adicionando informações para o registro
				}
			}
			// Somando Descontos
			sql = "SELECT COUNT(*) AS qt_total_acrescimo, SUM(DSI.vl_acrescimo) AS vl_total_acrescimo "+
				  "FROM alm_documento_saida_item     DSI " +
				  "INNER JOIN alm_documento_saida    DS ON (DS.cd_documento_saida = DSI.cd_documento_saida) "+
				  "WHERE DS.cd_turno                         = " +cdTurno+
				  "  AND DS.cd_conta                         = " +cdConta+
				  "  AND DS.st_documento_saida               = " + DocumentoSaidaServices.ST_CONCLUIDO +
				  "  AND CAST(DS.dt_documento_saida AS DATE) = ? ";
			pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			if(rs.getFloat("vl_total_acrescimo") > 0) {
				HashMap<String, Object> register   = new HashMap<String, Object>();
				register.put("CD_GRUPO", -10);
				register.put("NM_GRUPO", "Acréscimos");
				register.put("VL_TOTAL", rs.getFloat("vl_total_acrescimo"));
				rsm.addRegister(register);
			}
			// Acréscimos Especiais
			int cdTabelaPreco  = 0;
			int cdTipoOperacao = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0, conta!=null ? conta.getCdEmpresa() : 0);
			TipoOperacao tipoOperacao = TipoOperacaoDAO.get(cdTipoOperacao, connect);
			if(tipoOperacao!=null)
				cdTabelaPreco = tipoOperacao.getCdTabelaPreco();
			sql = " SELECT B.cd_documento_saida, B.dt_documento_saida, B.vl_total_documento, D.nm_pessoa, B.nr_documento_saida, " +
			      "        F.nm_pessoa AS nm_usuario, A.vl_unitario, A.qt_saida, " +
			      "       (SELECT vl_preco FROM adm_produto_servico_preco PR "+ 
				  "        WHERE PR.cd_produto_servico = A.cd_produto_servico "+
				  "          AND PR.cd_tabela_preco      = "+cdTabelaPreco+
				  "          AND (((SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
				  "                 WHERE PR.dt_termino_validade > B.dt_documento_saida "+
				  "                   AND PR.cd_produto_servico = A.cd_produto_servico "+
				  "                   AND PR.cd_tabela_preco      = "+cdTabelaPreco+") IS NOT NULL " +
				  "          AND PR.dt_termino_validade = (SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
				  "                                        WHERE PR.dt_termino_validade > B.dt_documento_saida "+
				  "                                          AND PR.cd_produto_servico = A.cd_produto_servico "+
				  "                                          AND PR.cd_tabela_preco      = "+cdTabelaPreco+")) OR "+
				  "               ((SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
				  "                 WHERE PR.dt_termino_validade > B.dt_documento_saida "+
				  "                   AND PR.cd_produto_servico  = A.cd_produto_servico "+
				  "                   AND PR.cd_tabela_preco     = "+cdTabelaPreco+") IS NULL AND PR.dt_termino_validade IS NULL))) AS vl_preco_tabela "+
				  "FROM alm_documento_saida_item A, alm_documento_saida B "+
			      "LEFT OUTER JOIN grl_pessoa  D ON (B.cd_cliente   = D.cd_pessoa) "+
				  "LEFT OUTER JOIN seg_usuario E ON (B.cd_digitador = E.cd_usuario) " +
				  "LEFT OUTER JOIN grl_pessoa  F ON (F.cd_pessoa    = E.cd_pessoa) "+
				  "WHERE CAST(B.dt_documento_saida AS DATE) = ? "+
				  "  AND B.cd_conta                         = "+cdConta+
				  "  AND B.cd_turno                         = "+cdTurno+
				  "  AND A.cd_documento_saida               = B.cd_documento_saida "+
				  "  AND B.st_documento_saida               = " + DocumentoSaidaServices.ST_CONCLUIDO +
				  "  AND A.vl_unitario > (SELECT vl_preco FROM adm_produto_servico_preco PR "+ 
				  "                       WHERE PR.cd_produto_servico = A.cd_produto_servico "+
				  "                         AND PR.cd_tabela_preco      = "+cdTabelaPreco+
				  "                         AND (((SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
				  "                                WHERE PR.dt_termino_validade > B.dt_documento_saida "+
				  "                                  AND PR.cd_produto_servico = A.cd_produto_servico "+
				  "                                  AND PR.cd_tabela_preco      = "+cdTabelaPreco+") IS NOT NULL " +
				  "                         AND PR.dt_termino_validade = (SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
				  "                                                       WHERE PR.dt_termino_validade > B.dt_documento_saida "+
				  "                                                         AND PR.cd_produto_servico = A.cd_produto_servico "+
				  "                                                         AND PR.cd_tabela_preco      = "+cdTabelaPreco+")) OR "+
				  "                              ((SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
				  "                                WHERE PR.dt_termino_validade > B.dt_documento_saida "+
				  "                                  AND PR.cd_produto_servico = A.cd_produto_servico "+
				  "                                  AND PR.cd_tabela_preco      = "+cdTabelaPreco+") IS NULL AND PR.dt_termino_validade IS NULL))) ";
			pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			rs = pstmt.executeQuery();
			float vlAcrescimo  = 0;
			while(rs.next()) {
				//
				float vlTotalItem     = rs.getFloat("vl_unitario") * rs.getFloat("qt_saida");
				float vlTotalItemTab  = (rs.getFloat("vl_preco_tabela") * rs.getFloat("qt_saida"));
				float vlAcrescimoItem = vlTotalItem - vlTotalItemTab;
				//
				vlAcrescimo += vlAcrescimoItem;
			}
			if(vlAcrescimo > 0) {
				HashMap<String, Object> register   = new HashMap<String, Object>();
				register.put("CD_GRUPO", -20);
				register.put("NM_GRUPO", "Acréscimos Especiais");
				register.put("VL_TOTAL", vlAcrescimo);
				rsm.addRegister(register);
			}
			// Somando e calculando %
			float vlTotal = 0;
			rsm.beforeFirst();
			while(rsm.next()) 
				vlTotal += rsm.getFloat("vl_total");
			//
			rsm.beforeFirst();
			while(rsm.next()) 
				rsm.setValueToField("PR_TOTAL", (rsm.getFloat("vl_total") / vlTotal) * 100);
			//
			rsm.beforeFirst();
			
			Util.printInFile("c:\\tivic\\pdv\\pdv.log", rsm.toString());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getRelatorioConsolidacaoResumoByGrupoPCB(ResultSetMap rsmFechamentos){
		return getRelatorioConsolidacaoResumoByGrupoPCB(rsmFechamentos, null);
	}
	
	public static ResultSetMap getRelatorioConsolidacaoResumoByGrupoPCB(ResultSetMap rsmFechamentos, Connection connect){
		boolean isConnectionNull = connect==null;
		ResultSetMap rsmResumo = new ResultSetMap();
		boolean hasResumoRegister = false;
		rsmFechamentos.beforeFirst();
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			while( rsmFechamentos.next() ){
				ResultSetMap rsmTmpResumo = DocumentoSaidaItemServices.findResumoOfItensByGrupoPCB(rsmFechamentos.getInt("CD_CONTA"), rsmFechamentos.getInt("CD_FECHAMENTO"), rsmFechamentos.getGregorianCalendar("DT_FECHAMENTO"), rsmFechamentos.getInt("CD_TURNO"));
 				rsmTmpResumo.beforeFirst();
				while( rsmTmpResumo.next() ){
 					hasResumoRegister = false;
					while( rsmResumo.next() ){
						if( rsmResumo.getInt("CD_GRUPO") == rsmTmpResumo.getInt("CD_GRUPO") ){
							//CONTABILIZA TOTAL E SINALIZA QUE JÁ POSSUI O REGISTRO NO RSM
							Float vlTotal = rsmResumo.getFloat("VL_TOTAL")+rsmTmpResumo.getFloat("VL_TOTAL");
							rsmResumo.setValueToField("VL_TOTAL", vlTotal);
							hasResumoRegister = true;
						}
					}
					if(!hasResumoRegister){
						rsmResumo.addRegister( rsmTmpResumo.getRegister() );
					}
					rsmResumo.beforeFirst();
				}
			}
			return rsmResumo;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getRelatorioConsolidacaoResumoByPagamento(ResultSetMap rsmFechamentos){
		return getRelatorioConsolidacaoResumoByPagamento(rsmFechamentos, null);
	}
	
	public static ResultSetMap getRelatorioConsolidacaoResumoByPagamento(ResultSetMap rsmFechamentos, Connection connect){
		boolean isConnectionNull = connect==null;
		ResultSetMap rsmResumoFechamento = new ResultSetMap();
		boolean hasResumoFechamentoRegister = false;
		rsmFechamentos.beforeFirst();
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			while( rsmFechamentos.next() ){
				ResultSetMap rsmTmpResumoFechamento = ContaFechamentoServices.getResumoByPagamento(rsmFechamentos.getInt("CD_CONTA"), rsmFechamentos.getGregorianCalendar("DT_FECHAMENTO"), rsmFechamentos.getInt("CD_TURNO"));
 				rsmTmpResumoFechamento.beforeFirst();
 				while( rsmTmpResumoFechamento.next() ){
					hasResumoFechamentoRegister = false;
					while( rsmResumoFechamento.next() ){
						if( rsmResumoFechamento.getInt("CD_FORMA_PAGAMENTO") == rsmTmpResumoFechamento.getInt("CD_FORMA_PAGAMENTO") ){
							//CONTABILIZA TOTAL E SINALIZA QUE JÁ POSSUI O REGISTRO NO RSM
							Float vlTotal = rsmResumoFechamento.getFloat("VL_PAGAMENTO")+rsmTmpResumoFechamento.getFloat("VL_PAGAMENTO");
							rsmResumoFechamento.setValueToField("VL_PAGAMENTO", vlTotal);
							rsmResumoFechamento.setValueToField("VL_TOTAL", rsmResumoFechamento.getFloat("VL_PAGAMENTO") );
							hasResumoFechamentoRegister = true;
						}
					}
					if(!hasResumoFechamentoRegister){
						rsmResumoFechamento.addRegister( rsmTmpResumoFechamento.getRegister() );
					}
					rsmResumoFechamento.beforeFirst();
				}
			}
			return rsmResumoFechamento;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/***
	 * @category PCB
	 */
	public static ResultSetMap getDetalheAcrescimos(int cdConta, int cdGrupo, GregorianCalendar dtMovimento, int cdTurno){
		return getDetalheAcrescimos(cdConta, cdGrupo, dtMovimento, cdTurno, null);
	}
	
	public static ResultSetMap getDetalheAcrescimos(int cdConta, int cdGrupo, GregorianCalendar dtMovimento, int cdTurno, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			dtMovimento.set(Calendar.HOUR, 0);
			dtMovimento.set(Calendar.MINUTE, 0);
			dtMovimento.set(Calendar.SECOND, 0);
			dtMovimento.set(Calendar.MILLISECOND, 0);
			// Acréscimos
			if(cdGrupo == -10) {
				String sql = "SELECT B.cd_documento_saida, B.nr_documento_saida, B.vl_acrescimo, " +
						     "      (B.vl_total_documento + B.vl_desconto - B.vl_acrescimo) AS vl_total_documento, " +
						     "       D.nm_pessoa, D.nm_pessoa AS nm_cliente," +
						     "       ((B.vl_acrescimo / B.vl_total_documento) * 100) AS pr_acrescimo," +
						     "       B.nr_documento_saida, F.nm_pessoa AS nm_usuario, B.vl_acrescimo AS vl_pagamento, " +
						     "       B.vl_total_documento AS vl_liquido "+
							 "FROM alm_documento_saida B "+
							 "LEFT OUTER JOIN grl_pessoa  D ON (B.cd_cliente   = D.cd_pessoa) "+
							 "LEFT OUTER JOIN seg_usuario E ON (B.cd_digitador = E.cd_usuario) " +
							 "LEFT OUTER JOIN grl_pessoa  F ON (F.cd_pessoa    = E.cd_pessoa) "+
							 "WHERE B.cd_turno                         = " +cdTurno+
							 "  AND B.cd_conta                         = " +cdConta+
							 "  AND CAST(B.dt_documento_saida AS DATE) = ? " +
							 "  AND B.vl_acrescimo                     > 0 " +
							 "  AND B.st_documento_saida               = "+DocumentoSaidaServices.ST_CONCLUIDO+
							 "  AND B.vl_total_documento               > 0";
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
				return new ResultSetMap(pstmt.executeQuery());
			}
			// Acrescimos Especiais
			if(cdGrupo == -20) {
				ContaFinanceira conta = ContaFinanceiraDAO.get(cdConta, connect);
				int cdTabelaPreco  = 0;
				int cdTipoOperacao = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0, conta!=null ? conta.getCdEmpresa() : 0);
				TipoOperacao tipoOperacao = TipoOperacaoDAO.get(cdTipoOperacao, connect);
				if(tipoOperacao!=null)
					cdTabelaPreco = tipoOperacao.getCdTabelaPreco();
				String sql = " SELECT B.cd_documento_saida, B.dt_documento_saida, B.vl_total_documento, D.nm_pessoa, B.nr_documento_saida, " +
						     "        F.nm_pessoa AS nm_usuario, A.vl_unitario, A.qt_saida, " +
						     "       (SELECT vl_preco FROM adm_produto_servico_preco PR "+ 
							 "        WHERE PR.cd_produto_servico = A.cd_produto_servico "+
							 "          AND PR.cd_tabela_preco      = "+cdTabelaPreco+
							 "          AND (((SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
							 "                 WHERE PR.dt_termino_validade > B.dt_documento_saida "+
							 "                   AND PR.cd_produto_servico = A.cd_produto_servico "+
							 "                   AND PR.cd_tabela_preco      = "+cdTabelaPreco+") IS NOT NULL AND PR.dt_termino_validade > B.dt_documento_saida) OR "+
							 "               ((SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
							 "                 WHERE PR.dt_termino_validade > B.dt_documento_saida "+
							 "                   AND PR.cd_produto_servico  = A.cd_produto_servico "+
							 "                   AND PR.cd_tabela_preco     = "+cdTabelaPreco+") IS NULL AND PR.dt_termino_validade IS NULL))) AS vl_preco_tabela "+
							 "FROM alm_documento_saida_item A, alm_documento_saida B "+
						     "LEFT OUTER JOIN grl_pessoa  D ON (B.cd_cliente   = D.cd_pessoa) "+
							 "LEFT OUTER JOIN seg_usuario E ON (B.cd_digitador = E.cd_usuario) " +
							 "LEFT OUTER JOIN grl_pessoa  F ON (F.cd_pessoa    = E.cd_pessoa) "+
							 "WHERE CAST(B.dt_documento_saida AS DATE) = ? "+
							 "  AND A.cd_documento_saida               = B.cd_documento_saida "+
							 "  AND B.cd_turno                         = "+cdTurno+
							 "  AND A.vl_unitario > (SELECT vl_preco FROM adm_produto_servico_preco PR "+ 
							 "                       WHERE PR.cd_produto_servico = A.cd_produto_servico "+
							 "                         AND PR.cd_tabela_preco      = "+cdTabelaPreco+
							 "                         AND (((SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
							 "                                WHERE PR.dt_termino_validade > B.dt_documento_saida "+
							 "                                  AND PR.cd_produto_servico = A.cd_produto_servico "+
							 "                                  AND PR.cd_tabela_preco      = "+cdTabelaPreco+") IS NOT NULL AND PR.dt_termino_validade > B.dt_documento_saida) OR "+
							 "                              ((SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
							 "                                WHERE PR.dt_termino_validade > B.dt_documento_saida "+
							 "                                  AND PR.cd_produto_servico = A.cd_produto_servico "+
							 "                                  AND PR.cd_tabela_preco      = "+cdTabelaPreco+") IS NULL AND PR.dt_termino_validade IS NULL))) ";
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
				ResultSet rs = pstmt.executeQuery();
				ResultSetMap rsm = new ResultSetMap();
				float vlAcrescimo           = 0;
				int   cdDocumentoSaida     = 0;
				HashMap<String,Object> reg = new HashMap<String,Object>();
				while(rs.next()) {
					//
					if(cdDocumentoSaida!=rs.getInt("cd_documento_saida")) {
						if(cdDocumentoSaida > 0) {
							reg.put("VL_PAGAMENTO", vlAcrescimo);
							reg.put("VL_ACRESCIMO", vlAcrescimo);
							reg.put("PR_ACRESCIMO", vlAcrescimo / ((Float)reg.get("VL_TOTAL_DOCUMENTO") - vlAcrescimo));
							rsm.addRegister(reg);
							reg = new HashMap<String,Object>();
						}
						// Reset
						cdDocumentoSaida = rs.getInt("cd_documento_saida");
						vlAcrescimo       = 0;
						reg.put("NR_DOCUMENTO_SAIDA", rs.getObject("nr_documento_saida"));
						reg.put("CD_DOCUMENTO_SAIDA", rs.getObject("cd_documento_saida"));
						reg.put("DT_DOCUMENTO_SAIDA", rs.getObject("dt_documento_saida"));
						reg.put("VL_TOTAL_DOCUMENTO", rs.getFloat("vl_total_documento"));
						reg.put("VL_LIQUIDO", rs.getFloat("vl_total_documento"));
						reg.put("NM_PESSOA", rs.getObject("nm_pessoa"));
						reg.put("NM_USUARIO", rs.getObject("nm_usuario"));
					}
					float vlTotalItem     = rs.getFloat("vl_unitario") * rs.getFloat("qt_saida");
					float vlTotalItemTab  = (rs.getFloat("vl_preco_tabela") * rs.getFloat("qt_saida"));
					float vlAcrescimoItem = vlTotalItem - vlTotalItemTab;
					//
					vlAcrescimo += vlAcrescimoItem;
				}
				if(cdDocumentoSaida > 0) {
					reg.put("VL_ACRESCIMO", vlAcrescimo);
					reg.put("VL_PAGAMENTO", vlAcrescimo);
					reg.put("PR_ACRESCIMO", vlAcrescimo / ((Float)reg.get("VL_TOTAL_DOCUMENTO") - vlAcrescimo));
					rsm.addRegister(reg);
					reg = new HashMap<String,Object>();
				}
				rsm.beforeFirst();
				return rsm;
			}
			return null;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public Result gerarRelatorioDocSaidaItem(ArrayList<ItemComparator> crt){
		
		boolean isConnectionNull = true;
		Connection connection = null;
		try {

			ArrayList<String> groupByFields = new ArrayList<String>();
			ArrayList<String> orderByFields = new ArrayList<String>();
						
			crt.add(new ItemComparator("semEstoque", "1", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = findCompleto(crt, groupByFields, orderByFields);
			rsm.beforeFirst();
			while(rsm.next()){
				rsm.setValueToField("VL_TOTAL_ITEM", String.valueOf(rsm.getDouble("VL_TOTAL_ITEM")));
				rsm.setValueToField("VL_DESCONTO",  String.valueOf(rsm.getDouble("VL_DESCONTO")));
				rsm.setValueToField("VL_ACRESCIMO", String.valueOf(rsm.getDouble("VL_ACRESCIMO")));
				rsm.setValueToField("NM_ST_DOCUMENTO", DocumentoSaidaServices.situacoesSaidaServico[rsm.getInt("ST_DOCUMENTO")]);
				rsm.setValueToField("QT_SAIDA", rsm.getFloat("QT_SAIDA"));
				
				ResultSetMap rsmAliquotas = getAllAliquotas(rsm.getInt("cd_documento_saida"), rsm.getInt("cd_produto_servico"), rsm.getInt("cd_item"), connection);
				rsmAliquotas.beforeFirst();
				
//				System.out.println(rsmAliquotas);
				
				while(rsmAliquotas.next()){
					if(rsmAliquotas.getInt("cd_tributo") == ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0)){
						rsm.setValueToField("VL_ICMS", (rsmAliquotas.getDouble("VL_BASE_CALCULO") * rsmAliquotas.getDouble("PR_ALIQUOTA") / 100));
						rsm.setValueToField("PR_ALIQUOTA_ICMS", rsmAliquotas.getDouble("PR_ALIQUOTA"));
					}
					else if(rsmAliquotas.getInt("cd_tributo") == ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_IPI", 0)){
						rsm.setValueToField("VL_IPI", (rsmAliquotas.getDouble("VL_BASE_CALCULO") * rsmAliquotas.getDouble("PR_ALIQUOTA") / 100));
						rsm.setValueToField("PR_ALIQUOTA_IPI", rsmAliquotas.getDouble("PR_ALIQUOTA"));
					}
					else if(rsmAliquotas.getInt("cd_tributo") == ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0)){
						rsm.setValueToField("VL_PIS", (rsmAliquotas.getDouble("VL_BASE_CALCULO") * rsmAliquotas.getDouble("PR_ALIQUOTA") / 100));
						rsm.setValueToField("PR_ALIQUOTA_PIS", rsmAliquotas.getDouble("PR_ALIQUOTA"));
					}
					else if(rsmAliquotas.getInt("cd_tributo") == ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0)){
						rsm.setValueToField("VL_COFINS", (rsmAliquotas.getDouble("VL_BASE_CALCULO") * rsmAliquotas.getDouble("PR_ALIQUOTA") / 100));
						rsm.setValueToField("PR_ALIQUOTA_COFINS", rsmAliquotas.getDouble("PR_ALIQUOTA"));
					}
				}
			}
			
			System.out.println(rsm);
			
			HashMap<String, Object> param = new HashMap<String, Object>();
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
}
