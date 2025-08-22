package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.ComplianceManager;
import com.tivic.manager.util.Util;

import groovy.lang.GroovyResourceLoader;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class MovimentoContaPagarServices {

	public static Result save(MovimentoContaPagar movimentoContaPagar) {
		return save(movimentoContaPagar, null, null);
	}
	
	public static Result save(MovimentoContaPagar movimentoContaPagar, AuthData authData) {
		return save(movimentoContaPagar, authData, null);
	}
	
	public static Result save(MovimentoContaPagar movimentoContaPagar, Connection connect) {
		return save(movimentoContaPagar, null, connect);
	}

	public static Result save(MovimentoContaPagar movimentoContaPagar, AuthData authData, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if (movimentoContaPagar == null)
				return new Result(-1,
						"Erro ao salvar. MovimentoContaPagar é nulo");

			int tpAcao = ComplianceManager.TP_ACAO_ANY;
			int retorno;
			if (movimentoContaPagar.getCdConta() == 0) {
				retorno = MovimentoContaPagarDAO.insert(movimentoContaPagar,
						connect);
				movimentoContaPagar.setCdConta(retorno);
				tpAcao = ComplianceManager.TP_ACAO_INSERT;
			} else {
				retorno = MovimentoContaPagarDAO.update(movimentoContaPagar,
						connect);
				tpAcao = ComplianceManager.TP_ACAO_UPDATE;
			}

			//COMPLIANCE
			ComplianceManager.process(movimentoContaPagar, authData, tpAcao, connect);

			if (retorno <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno <= 0) ? "Erro ao salvar..."
					: "Salvo com sucesso...", "MOVIMENTOCONTAPAGAR",
					movimentoContaPagar);
		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result remove(int cdConta, int cdMovimentoConta, int cdContaPagar) {
		return remove(cdConta, cdMovimentoConta, cdContaPagar, false, null);
	}

	public static Result remove(int cdConta, int cdMovimentoConta, int cdContaPagar, boolean cascade) {
		return remove(cdConta, cdMovimentoConta, cdContaPagar, cascade, null);
	}

	public static Result remove(int cdConta, int cdMovimentoConta,
			int cdContaPagar, boolean cascade, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if (cascade) {
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			
			MovimentoContaPagar movimentoContaPagar = MovimentoContaPagarDAO.get(cdConta, cdMovimentoConta, cdContaPagar, connect);	
			
			//Verifica se há mais de um registro lançado para a mesma forma de pagamento ou recebimento
			//exenplo: Quando um cheque paga ou recebe duas ou mais contas			
			PreparedStatement pstmt = connect.prepareStatement("SELECT COUNT(A.cd_movimento_conta) AS qt_contas " +
															   "FROM adm_movimento_conta A "+
															   "JOIN adm_movimento_conta_pagar B on ( A.cd_conta = B.cd_conta AND A.cd_movimento_conta = B.cd_movimento_conta ) " +
												 			   "  WHERE B.cd_conta_pagar <> " +cdContaPagar+
												 			   "  AND A.cd_conta =" +cdConta+
												 			   "  AND A.cd_movimento_conta = " +cdMovimentoConta);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				if (rs.getInt("qt_contas") > 0){			
					Conexao.rollback(connect);
//					return new Result(-2, "Este pagamento está vinculado a outros e não pode ser excluído!");
					return new Result(-2, "Este pagamento faz parte de uma movimentação em conjunto e não pode ser excluída isoladamente!");
				}
			}
			
			/*
			 * Em caso de utilização de cheque atualiza sua situação.
			 */
			
			rs = connect.prepareStatement("SELECT * FROM adm_movimento_conta "+
							 			   "  WHERE cd_conta         = " +cdConta+
							 			   "  AND cd_movimento_conta = " +cdMovimentoConta).executeQuery();
			if(rs.next()){ 
				if (rs.getInt("cd_cheque") > 0){			
					connect.prepareStatement("UPDATE adm_cheque SET st_cheque = "+ChequeServices.NAO_EMITIDO+
											 " WHERE cd_cheque = "+rs.getInt("cd_cheque")).executeUpdate();
				}
			}
			
			/**
			 * Remove a movimentação da conta a pagar, da conta financeira e suas classificações
			 */
			retorno = MovimentoContaPagarDAO.delete(cdConta, cdMovimentoConta, cdContaPagar, connect);
			connect.prepareStatement("DELETE FROM adm_movimento_conta_categoria "
								+ "  WHERE cd_conta           = " + cdConta
								+ "  AND cd_movimento_conta = " + cdMovimentoConta
								+ "  AND cd_conta_pagar     = " + cdContaPagar).execute();
			MovimentoContaArquivoServices.removeAll(cdMovimentoConta, cdConta, connect);
			connect.prepareStatement("DELETE FROM adm_movimento_conta "
								+ "  WHERE cd_conta           = " + cdConta
								+ "  AND cd_movimento_conta = " + cdMovimentoConta).execute();
			
			//Atualiza a conta a pagar 
			int ret = setSituacaConta(cdContaPagar, connect);
			if (ret <= 0) {
				com.tivic.manager.util.Util.registerLog(new Exception(
						"Erro ao tentar atualizar conta a pagar!"));
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ret,
						"Erro ao tentar atualizar situacao da conta");
			}
			
			
			if (retorno <= 0) {
				Conexao.rollback(connect);
				return new Result(-2,
						"Este registro está vinculado a outros e não pode ser excluído!");
			} 
			

			//COMPLIANCE
			ComplianceManager.process(movimentoContaPagar, null, ComplianceManager.TP_ACAO_DELETE, connect);
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Registro excluído com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int insert(MovimentoContaPagar objeto) {
		return insert(objeto, null);
	}

	public static int insert(MovimentoContaPagar objeto, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect
					.getAutoCommit());

			// Verifica autorização de pagamento
			ContaPagar conta = ContaPagarDAO.get(objeto.getCdContaPagar(),
					connect);
			if (conta.getLgAutorizado() != 1
					&& conta.getVlBaseAutorizacao() == 0) {
				com.tivic.manager.util.Util.registerLog(new Exception(
						"Pagamento de conta não autorizado!"));
				return -10;
			}
			if( conta.getLgAutorizado() != 1 &&
				conta.getVlPago()+objeto.getVlPago() > conta.getVlBaseAutorizacao() ){
				com.tivic.manager.util.Util.registerLog(new Exception(
						"Total Pago Excede o teto pré autorizado!"));
				return -10;
			}
			int retorno = MovimentoContaPagarDAO.insert(objeto, connect);
			// Tenta gravar nova situação da conta
			retorno = retorno > 0 ? setSituacaConta(objeto.getCdContaPagar(),
					connect) : retorno;

			if (isConnectionNull) {
				if (retorno > 0)
					connect.commit();
				else
					connect.rollback();
			}

			return retorno;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err
					.println("Erro! MovimentoContaPagarServices.insert: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MovimentoContaPagar objeto) {
		return insert(objeto, null);
	}

	public static int update(MovimentoContaPagar objeto, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			if (isConnectionNull)
				connect.setAutoCommit(false);
			int retorno = MovimentoContaPagarDAO.update(objeto, connect);
			// tenta gravar nova situação da conta
			retorno = retorno > 0 ? setSituacaConta(objeto.getCdContaPagar(),
					connect) : retorno;
			//
			if (isConnectionNull)
				if (retorno > 0)
					connect.commit();
				else
					connect.rollback();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err
					.println("Erro! MovimentoContaPagarServices.update: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int setSituacaConta(int cdContaPagar, Connection connect) {
		return setSituacaConta(ContaPagarDAO.get(cdContaPagar, connect),
				connect);
	}

	public static int setSituacaConta(ContaPagar conta, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			PreparedStatement pstmt = connect
					.prepareStatement("SELECT SUM(vl_pago) AS vl_pago, SUM(vl_multa) AS vl_multa, "
							+ "       SUM(vl_juros) AS vl_juros, SUM(vl_desconto) AS vl_desconto, "
							+ "       MAX(B.dt_movimento) AS dt_movimento, MAX(B.dt_deposito) AS dt_deposito "
							+ " FROM adm_movimento_conta_pagar A, adm_movimento_conta B "
							+ " WHERE A.cd_conta = B.cd_conta "
							+ "  AND A.cd_movimento_conta = B.cd_movimento_conta "
							+ "  AND A.cd_conta_pagar = "
							+ conta.getCdContaPagar()
							+ "  AND B.st_movimento <> "
							+ MovimentoContaServices.ST_CANCELADO);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			Timestamp dtPagamento = null;
			Double vlPagoConta = rs.getDouble("vl_pago")
					- rs.getDouble("vl_multa") - rs.getDouble("vl_juros")
					+ rs.getDouble("vl_desconto");
			Double vlAPagar = (conta.getVlConta() + conta.getVlAcrescimo()
					- conta.getVlAbatimento() - vlPagoConta);

			int stConta = (vlAPagar >= 0.01) ? ContaPagarServices.ST_EM_ABERTO
					: ContaPagarServices.ST_PAGA;

			if (stConta == ContaPagarServices.ST_PAGA) {
				dtPagamento = rs.getTimestamp("dt_movimento");
				if (dtPagamento != null
						&& rs.getTimestamp("dt_deposito") != null
						&& rs.getTimestamp("dt_deposito").before(dtPagamento))
					dtPagamento = rs.getTimestamp("dt_deposito");
				if (conta.getCdContaOrigem() > 0)
					ContaPagarServices.gerarParcelasOutraConta(conta, false,
							connect);
			}
			/*
			 * Atualizando conta a pagar
			 */
			pstmt = connect
					.prepareStatement("UPDATE adm_conta_pagar SET vl_pago = "
							+ vlPagoConta + ", " + " st_conta = " + stConta
							+ ", dt_pagamento = ? "
							+ " WHERE cd_conta_pagar = "
							+ conta.getCdContaPagar());
			if (dtPagamento != null)
				pstmt.setTimestamp(1, dtPagamento);
			else
				pstmt.setNull(1, Types.TIMESTAMP);

			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getPagamentoOfContaPagar(int cdContaPagar) {
		return getPagamentoOfContaPagar(cdContaPagar, null);
	}

	public static ResultSetMap getPagamentoOfContaPagar(int cdContaPagar, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect
					.prepareStatement("SELECT B.*, A.*, C.nm_conta, C.nr_conta, C.nr_dv, C.tp_conta, E.nr_agencia, "
							+ "    F.nr_banco, F.nm_banco, D.nm_forma_pagamento, D.id_forma_pagamento, D.tp_forma_pagamento, "
							+ "	G.nr_cheque, G.st_cheque, G.dt_emissao AS dt_emissao_cheque "
							+ "FROM adm_movimento_conta_pagar    A "
							+ "JOIN adm_movimento_conta            B ON (A.cd_conta = B.cd_conta "
							+ "                                      AND A.cd_movimento_conta = B.cd_movimento_conta) "
							+ "JOIN adm_conta_financeira           C ON (B.cd_conta = C.cd_conta) "
							+ "LEFT OUTER JOIN adm_forma_pagamento D ON (B.cd_forma_pagamento = D.cd_forma_pagamento) "
							+ "LEFT OUTER JOIN grl_agencia         E ON (C.cd_agencia = E.cd_agencia) "
							+ "LEFT OUTER JOIN grl_banco           F ON (E.cd_banco = F.cd_banco) "
							+ "LEFT OUTER JOIN adm_cheque          G ON (G.cd_cheque = B.cd_cheque) "
							+ "WHERE A.cd_conta_pagar = " + cdContaPagar);
			return new ResultSetMap(pstmt.executeQuery());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getPagamentoOfMovimento(int cdConta,
			int cdMovimentoConta) {
		return getPagamentoOfMovimento(cdConta, cdMovimentoConta, null);
	}

	public static ResultSetMap getPagamentoOfMovimento(int cdConta, int cdMovimentoConta, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT B.*, A.*, C.nm_pessoa, D.sg_tipo_documento, D.nm_tipo_documento "
							+ "FROM adm_movimento_conta_pagar A "
							+ "JOIN adm_conta_pagar B ON (A.cd_conta_pagar = B.cd_conta_pagar) "
							+ "LEFT OUTER JOIN grl_pessoa         C ON (B.cd_pessoa = C.cd_pessoa) "
							+ "LEFT OUTER JOIN adm_tipo_documento D ON (B.cd_tipo_documento = D.cd_tipo_documento) "
							+ "WHERE A.cd_conta = "
							+ cdConta
							+ "  AND A.cd_movimento_conta = "
							+ cdMovimentoConta);
			return new ResultSetMap(pstmt.executeQuery());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err
					.println("Erro! MovimentoContaPagarServices.getPagamentoOfMovimento: "
							+ e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, new ArrayList<String>(), null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios,
			ArrayList<String> groupBy) {
		return find(criterios, groupBy, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, ArrayList<String> groupBy, Connection connect) {
		boolean lgCategoria = false, lgConferencia = false;
		boolean isConnNull = connect == null;
		if (isConnNull)
			connect = Conexao.conectar();
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		String ordenacao = "ASC";
		String nmPessoa = "";
		for (int i = 0; i < criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("lgcategoria")) {
				lgCategoria = true;
				criterios.remove(i);
				i--;
			} else if (criterios.get(i).getColumn()
					.equalsIgnoreCase("lgconferencia")) {
				lgConferencia = true;
				criterios.remove(i);
				i--;
			} else if (criterios.get(i).getColumn().equalsIgnoreCase("ordenacao")) {
				ordenacao = criterios.get(i).getValue();
				criterios.remove(i);
				if( ordenacao.equals("ASC") || !ordenacao.equals("DESC")  )
					ordenacao = "ASC";
				i--;
			}	else if (criterios.get(i).getColumn().equalsIgnoreCase("E.NM_PESSOA")) {
					nmPessoa =	Util.limparTexto(criterios.get(i).getValue());
					nmPessoa = nmPessoa.trim();
					criterios.remove(i);
					i--;
			} else
				crt.add(criterios.get(i));
		}
		
		// Processa agrupamentos enviados em groupBy
//		String[] retorno = com.tivic.manager.util.Util
//				.getFieldsAndGroupBy(
//						groupBy,
//						new String(fields),
//						groups,
//						" SUM(D.VL_CONTA) AS VL_CONTA, SUM(A.VL_JUROS) AS VL_JUROS, SUM(A.VL_MULTA) AS VL_MULTA, "
//								+ " SUM(A.VL_DESCONTO) AS VL_DESCONTO, SUM(A.VL_PAGO) AS VL_PAGO, COUNT(*) AS QT_PAGAMENTO");
//		String fieldsByGroup = retorno[0];
//		groups = retorno[1];
		
		ResultSetMap rsm = null;
		try {
			String orderBy = " ORDER BY B.dt_movimento "+ordenacao;
			String sql = "SELECT  A.*, B.dt_movimento, B.nr_documento, B.cd_forma_pagamento, B.st_movimento, B.tp_movimento, "
					+ " B.vl_movimento, B.tp_origem, B.ds_historico, C.nr_conta, C.nr_dv, C.nm_conta, C.tp_conta, "
					+ " D.ds_historico as DS_HISTORICO_CONTA, D.dt_vencimento, D.vl_conta, D.vl_acrescimo, D.vl_abatimento, "
					+ " D.nr_parcela, D.nr_documento as nr_doc_conta, D.nr_referencia, D.cd_tipo_documento, D.qt_parcelas, D.cd_empresa, "
					+ " E.nm_pessoa, F.nm_forma_pagamento, F.sg_forma_pagamento, G.nm_tipo_documento, G.sg_tipo_documento, "
					+ " H.*, I.*, J.nm_pessoa as nm_fantasia "
					+ " FROM adm_movimento_conta_pagar A "
					+ " JOIN adm_movimento_conta           			B ON (A.cd_conta = B.cd_conta "
					+ "                                     			 AND A.cd_movimento_conta = B.cd_movimento_conta) "
					+ " JOIN adm_conta_financeira           		C ON (A.cd_conta = C.cd_conta) "
					+ " JOIN adm_conta_pagar                		D ON (A.cd_conta_pagar = D.cd_conta_pagar) "
					+ " LEFT OUTER JOIN grl_pessoa          		E ON (D.cd_pessoa = E.cd_pessoa) "
					+ " LEFT OUTER JOIN adm_forma_pagamento 		F ON (B.cd_forma_pagamento = F.cd_forma_pagamento) "
					+ " LEFT OUTER JOIN adm_tipo_documento          G ON (D.cd_tipo_documento  = G.cd_tipo_documento) "
					+ " LEFT OUTER JOIN grl_pessoa_conta_bancaria   H ON (D.cd_conta_favorecido =  H.cd_conta_bancaria "
					+ "														AND D.cd_pessoa =  H.cd_pessoa ) "
					+ " LEFT OUTER JOIN grl_banco                   I ON (H.cd_banco =  I.cd_banco ) "
					+ " LEFT OUTER JOIN grl_pessoa                  J ON ( C.cd_empresa = J.cd_pessoa ) "
					+ " WHERE 1=1 "
					+ (!nmPessoa.equals("") ?
							"AND TRANSLATE (E.nm_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', " + 
							"				'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmPessoa)+"%' "
							: "");
			rsm = Search.find(sql, orderBy, crt, connect, connect==null);
		
			PreparedStatement pstmt = connect
					.prepareStatement("SELECT A.*, B.* "
							+ "FROM adm_movimento_conta_categoria A "
							+ "JOIN adm_categoria_economica B ON (A.cd_categoria_economica = B.cd_categoria_economica) "
							+ "WHERE A.cd_conta           = ? "
							+ "  AND A.cd_movimento_conta = ? "
							+ "  AND EXISTS (SELECT * FROM adm_conta_pagar_categoria CPC "
							+ "         	  WHERE CPC.cd_conta_pagar         = ? "
							+ "            	AND CPC.cd_categoria_economica = A.cd_categoria_economica)");
			while (lgCategoria && rsm.next()) {
				pstmt.setInt(1, rsm.getInt("cd_conta"));
				pstmt.setInt(2, rsm.getInt("cd_movimento_conta"));
				pstmt.setInt(3, rsm.getInt("cd_conta_pagar"));
				ResultSetMap rsmCategoriasConta = new ResultSetMap(pstmt.executeQuery());
				String dsCategoria = "", dsAlerta = "";
				// Verifica a classificação total do movimento
				Double vlTotalCategoria = 0.0;
				ResultSetMap rsmCategoriasMov = MovimentoContaCategoriaServices
						.getCategoriaOfMovimento(rsm.getInt("cd_conta"),
								rsm.getInt("cd_movimento_conta"), connect);
				while (rsmCategoriasMov.next())
					vlTotalCategoria += rsmCategoriasMov
							.getDouble("vl_movimento_categoria");
				if (Math.abs(vlTotalCategoria - rsm.getDouble("vl_movimento")) > 0.01)
					dsAlerta = (dsAlerta.equals("") ? "" : ", ")
							+ "Diferença no movimento: Movimento: "
							+ Util.formatNumber(rsm.getDouble("vl_movimento"))
							+ ", Categorias: "
							+ Util.formatNumber(vlTotalCategoria);
				// Verifica a classificação do recebimento
				// Se não tiver a classificação verifica se foi porque a
				// classificação da conta tá diferente
				if (rsmCategoriasConta.size() == 0) {
					ResultSetMap rsmContas = getPagamentoOfMovimento(
							rsm.getInt("cd_conta"),
							rsm.getInt("cd_movimento_conta"), connect);
					if (rsmCategoriasMov.size() > 0)
						dsAlerta = (dsAlerta.equals("") ? "" : ", ")
								+ "Classificação da conta diferente do recebimento";
					// Se houver classificação e também o movimento tiver pago
					// mais de uma conta, a classificação encontrada pode ser da
					// outra conta
					if (rsmContas.size() == 1)
						rsmCategoriasConta = rsmCategoriasMov;
				}
				Double vlAClassificar = rsm.getDouble("vl_recebido")
						+ rsm.getDouble("vl_multa") + rsm.getDouble("vl_juros")
						+ rsm.getDouble("vl_desconto");
				rsmCategoriasConta.beforeFirst();
				while (rsmCategoriasConta.next()) {
					dsCategoria += (dsCategoria.equals("") ? "" : ", ")
							+ rsmCategoriasConta
									.getString("nr_categoria_economica")
							+ "-"
							+ rsmCategoriasConta
									.getString("nm_categoria_economica");
					vlAClassificar -= rsmCategoriasConta
							.getDouble("vl_movimento_categoria");
					// Verificando se alguma categoria de receita foi usada
					if (lgConferencia) {
						int tpCategoria = rsmCategoriasConta
								.getInt("tp_categoria_economica");
						if ((tpCategoria != CategoriaEconomicaServices.TP_DESPESA && tpCategoria != CategoriaEconomicaServices.TP_DEDUCAO_RECEITA)
								&& rsmCategoriasConta
										.getDouble("vl_movimento_categoria") > rsm
										.getDouble("vl_abatimento"))
							dsAlerta += (dsAlerta.equals("") ? "" : ", ")
									+ "Categoria de despesa";
					}
				}
				if (lgConferencia && vlAClassificar > 0)
					dsAlerta += (dsAlerta.equals("") ? "" : ", ")
							+ "Diferença na classificacao: "
							+ Util.formatNumber(vlAClassificar);
				if (dsAlerta.equals(""))
					dsAlerta = "OK";
				rsm.setValueToField("ds_categoria_economica", dsCategoria);
				rsm.setValueToField("ds_alerta", dsAlerta);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			if (isConnNull)
				Conexao.desconectar(connect);
		}
		if (rsm != null) {
			rsm.beforeFirst();
		}
		return rsm;
	}
	
	public static ResultSetMap findGroupFavorecido(ArrayList<ItemComparator> criterios) {
		return findGroupFavorecido(criterios, null);
	}
	public static ResultSetMap findGroupFavorecido(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnNull = connect == null;
		try {
			if (isConnNull)
				connect = Conexao.conectar();
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			String ordenacao = "ASC";
			Boolean hasCriterioEmpresa = false;
			for (int i = 0; i < criterios.size(); i++) {
				if( criterios.get(i).getColumn().equalsIgnoreCase("C.cd_empresa") )
					hasCriterioEmpresa = true;
				
				if (criterios.get(i).getColumn().equalsIgnoreCase("ordenacao")) {
					ordenacao = criterios.get(i).getValue();
					criterios.remove(i);
					if( ordenacao.equals("ASC") || !ordenacao.equals("DESC")  )
						ordenacao = "ASC";
					i--;
				}else
					crt.add(criterios.get(i));
			}
			
			// Processa agrupamentos enviados em groupBy
	//		String[] retorno = com.tivic.manager.util.Util
	//				.getFieldsAndGroupBy(
	//						groupBy,
	//						new String(fields),
	//						groups,
	//						" SUM(D.VL_CONTA) AS VL_CONTA, SUM(A.VL_JUROS) AS VL_JUROS, SUM(A.VL_MULTA) AS VL_MULTA, "
	//								+ " SUM(A.VL_DESCONTO) AS VL_DESCONTO, SUM(A.VL_PAGO) AS VL_PAGO, COUNT(*) AS QT_PAGAMENTO");
	//		String fieldsByGroup = retorno[0];
	//		groups = retorno[1];
			
			ResultSetMap rsm = null;
		
			//String orderBy = " ORDER BY B.DT_MOVIMENTO "+ordenacao;
			String groupBy = "GROUP BY E.NM_PESSOA ";
			String fields = "";
			
			if( !hasCriterioEmpresa ){
				groupBy += ", H.nm_pessoa ";
				fields += "H.nm_pessoa as nm_fantasia, ";
			}
			
			String sql = "SELECT  E.NM_PESSOA, "+fields
					+ " SUM(A.VL_JUROS) AS VL_JUROS, SUM(A.VL_MULTA) AS VL_MULTA, "
					+ " SUM(A.VL_DESCONTO) AS VL_DESCONTO, SUM(A.VL_PAGO) AS VL_PAGO, COUNT(*) AS QT_PAGAMENTO "
					+ " FROM adm_movimento_conta_pagar A "
					+ " JOIN adm_movimento_conta             B ON (A.cd_conta = B.cd_conta "
					+ "                                         AND A.cd_movimento_conta = B.cd_movimento_conta) "
					+ " JOIN adm_conta_financeira            C ON (A.cd_conta = C.cd_conta) "
					+ " JOIN adm_conta_pagar                 D ON (A.cd_conta_pagar = D.cd_conta_pagar) "
					+ " LEFT OUTER JOIN grl_pessoa           E ON (D.cd_pessoa = E.cd_pessoa) "
					+ " LEFT OUTER JOIN adm_forma_pagamento  F ON (B.cd_forma_pagamento = F.cd_forma_pagamento) "
					+ " LEFT OUTER JOIN adm_tipo_documento   G ON (D.cd_tipo_documento  = G.cd_tipo_documento) "
					+ " JOIN grl_pessoa                      H ON ( C.cd_empresa = H.cd_pessoa ) ";
			rsm = Search.find(sql, groupBy, crt, connect, connect==null);
			if (rsm != null)
				rsm.beforeFirst();
			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result delete(int cdConta, int cdMovimentoConta, int cdContaPagar, int cdUsuario) {
		return delete(cdConta, cdMovimentoConta, cdContaPagar, cdUsuario, true,
				null);
	}

	public static Result delete(int cdConta, int cdMovimentoConta, int cdContaPagar,
								int cdUsuario, boolean atualizaEmCascata, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect
					.getAutoCommit());
			/*
			 * Verifica se o lançamento já foi conferido/concilicado
			 */
			PreparedStatement pstmt = connect
					.prepareStatement("SELECT * FROM adm_movimento_conta "
							+ "WHERE cd_conta           = " + cdConta
							+ "  AND cd_movimento_conta = " + cdMovimentoConta
							+ "  AND st_movimento = "
							+ MovimentoContaServices.ST_CONCILIADO);
			if (pstmt.executeQuery().next()) {
				com.tivic.manager.util.Util.registerLog(new Exception(
						"Movimento da conta já conferido/conciliado!"));
				return new Result(-10,
						"Movimento da conta já conferido/conciliado!");
			}

			connect.prepareStatement(
					"DELETE FROM adm_movimento_conta_categoria "
							+ "WHERE cd_conta = " + cdConta
							+ "  AND cd_movimento_conta = " + cdMovimentoConta
							+ "  AND cd_conta_pagar = " + cdContaPagar)
					.executeUpdate();

			/*
			 * Exclui o pagamento
			 */
			int ret = MovimentoContaPagarDAO.delete(cdConta, cdMovimentoConta,
					cdContaPagar, connect);
			if (ret <= 0) {
				com.tivic.manager.util.Util.registerLog(new Exception(
						"Erro ao tentar excluir recebimento!"));
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ret, "Erro ao tentar excluir recebimento!");
			}

			/*
			 * Dependendo do parametro passado atualiza ou não o movimento da
			 * conta
			 */
			if (atualizaEmCascata) {
				pstmt = connect
						.prepareStatement("SELECT SUM(vl_pago) AS vl_pago, "
								+ "       SUM(vl_juros) AS vl_juros, "
								+ "       SUM(vl_multa) AS vl_multa, "
								+ "       SUM(vl_desconto) AS vl_desconto "
								+ "FROM adm_movimento_conta_pagar "
								+ "WHERE cd_conta           = " + cdConta
								+ "  AND cd_movimento_conta = "
								+ cdMovimentoConta);
				ResultSet rs = pstmt.executeQuery();
				float vlMovimento = 0;
				if (rs.next())
					vlMovimento = rs.getFloat("vl_pago")
							- rs.getFloat("vl_desconto");
				Result result = new Result(1);
				if (vlMovimento > 0) {
					MovimentoConta movimentoConta = MovimentoContaDAO.get(
							cdMovimentoConta, cdConta, connect);
					movimentoConta.setVlMovimento( Double.parseDouble(Float.toString(vlMovimento)));
					result.setCode(MovimentoContaDAO.update(movimentoConta,
							connect));
				} else
					result = MovimentoContaServices.delete(cdConta,
							cdMovimentoConta, cdUsuario, false, connect);

				if (result.getCode() <= 0) {
					com.tivic.manager.util.Util.registerLog(new Exception(
							"Erro ao tentar excluir movimento de conta!"));
					if (isConnectionNull)
						Conexao.rollback(connect);
					result.setMessage("Falha ao tentar excluir movimento de conta! "
							+ result.getMessage());
					return result;
				}
			}
			/*
			 * Atualiza a conta a pagar
			 */
			ret = setSituacaConta(cdContaPagar, connect);
			if (ret <= 0) {
				com.tivic.manager.util.Util.registerLog(new Exception(
						"Erro ao tentar atualizar conta a pagar!"));
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ret,
						"Erro ao tentar atualizar situacao da conta");
			}
			/*
			 * Se a conexão não foi passada por outro método chamado, chama o
			 * commit
			 */
			if (isConnectionNull)
				connect.commit();

			return new Result(ret);
		} catch (Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar exclui pagamento de conta!",
					e);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result estornar(int cdConta, int cdMovimentoConta, int cdContaPagar, int cdUsuario) {
		return estornar(cdConta, cdMovimentoConta, cdContaPagar, cdUsuario, null);
	}

	public static Result estornar(int cdConta, int cdMovimentoConta, int cdContaPagar, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			/*
			 * Busca informações do recebimento
			 */
			MovimentoContaPagar pagamento = MovimentoContaPagarDAO.get(cdConta,
					cdMovimentoConta, cdContaPagar, connect);
			pagamento.setCdMovimentoConta(0);
			pagamento.setVlPago(-1 * pagamento.getVlPago());
			pagamento.setVlDesconto(0.0);
			pagamento.setVlMulta(0.0);
			pagamento.setVlJuros(0.0);

			ArrayList<MovimentoContaPagar> pagamentos = new ArrayList<MovimentoContaPagar>();
			pagamentos.add(pagamento);

			/*
			 * Cria movimento de conta
			 */
			MovimentoConta movimentoConta = MovimentoContaDAO.get(
					cdMovimentoConta, cdConta, connect);
			float vlMovimentoContaOriginal = movimentoConta.getVlMovimento().floatValue();
			movimentoConta.setCdMovimentoConta(0);
			movimentoConta.setCdContaOrigem(cdConta);
			movimentoConta.setCdMovimentoConta(cdMovimentoConta);
			movimentoConta.setCdUsuario(cdUsuario);
			movimentoConta.setDtMovimento(new GregorianCalendar());
			movimentoConta.setVlMovimento(Math.abs(pagamento.getVlPago()));
			movimentoConta.setTpMovimento(MovimentoContaServices.CREDITO);
			movimentoConta.setDsHistorico("Estorno de Pagamento");
			movimentoConta.setStMovimento(MovimentoContaServices.ST_CONFERIDO);
			movimentoConta.setIdExtrato(null);
			movimentoConta.setTpOrigem(MovimentoContaServices.toCREDITO);

			/*
			 * Categorias economicas
			 */
			ArrayList<MovimentoContaCategoria> movimentoCategorias = new ArrayList<MovimentoContaCategoria>();
			ResultSet rs = connect.prepareStatement(
					"SELECT * FROM adm_movimento_conta_categoria "
							+ "WHERE cd_conta = " + cdConta
							+ "  AND cd_movimento_conta = " + cdMovimentoConta)
					.executeQuery();
			while (rs.next()) {
				Double vlMovimentoCategoria = pagamento.getVlPago();
				if (vlMovimentoContaOriginal > 0
						&& vlMovimentoContaOriginal != pagamento.getVlPago())
					vlMovimentoCategoria = vlMovimentoCategoria
							/ vlMovimentoContaOriginal * pagamento.getVlPago();
				movimentoCategorias
						.add(new MovimentoContaCategoria(
								cdConta,
								0,
								rs.getInt("cd_categoria_economica"),
								vlMovimentoCategoria.floatValue(),
								0 /* cdMovimentoContaCategoria */,
								cdContaPagar,
								0 /* cdContaReceber */,
								MovimentoContaCategoriaServices.TP_PRE_CLASSIFICACAO /* tpMovimento */,
								rs.getInt("cd_centro_categoria")));
			}
			Result result = MovimentoContaServices.insert(movimentoConta,
					pagamentos, movimentoCategorias, false, connect);
			if (result.getCode() <= 0) {
				result.setMessage("Falhar ao tentar estoanr pagamento! \n"
						+ result.getMessage());
				com.tivic.manager.util.Util.registerLog(new Exception(result
						.getMessage()));
				if (isConnectionNull)
					Conexao.rollback(connect);
				return result;
			}
			/*
			 * Atualiza a conta a pagar
			 */
			int ret = setSituacaConta(cdContaPagar, connect);
			if (ret <= 0) {
				com.tivic.manager.util.Util.registerLog(new Exception(
						"Erro ao tentar atualizar conta a pagar!"));
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ret,
						"Erro ao tentar atualizar conta a pagar!");
			}
			/*
			 * ; Se a conexão não foi passada por outro método chamado, chama o
			 * commit
			 */
			if (isConnectionNull)
				connect.commit();

			return new Result(ret);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err
					.println("Erro! MovimentoContaPagarServices.delete: " + e);
			return new Result(-1, "Erro ao tentar lancar estorno!", e);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getExtratoOfMovimento(int cdConta, int cdMovimentoConta, String dtInicial, String dtFinal) {
		return getExtratoOfMovimento(cdConta, cdMovimentoConta, dtInicial, dtFinal, null);
	}

	public static ResultSetMap getExtratoOfMovimento(int cdConta, int cdMovimentoConta,
							                         String dtInicial, String dtFinal, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSetMap rsm = null;
		try {
			pstmt = connect
					.prepareStatement("SELECT B.*, A.*, "
							+ "C.nm_pessoa,  "
							+ "D.sg_tipo_documento, D.nm_tipo_documento, "
							+ "E.nm_pessoa AS nm_emitente, E.nr_telefone1 AS nr_telefone, E.nm_email,"
							+ "F.nr_inscricao_estadual, F.nr_cnpj, "
							+ "G.nm_logradouro, G.nm_bairro, "
							+ "H.nm_cidade, H.nr_cep "
							+ "FROM adm_movimento_conta_pagar A "
							+ "JOIN adm_conta_pagar     B ON (A.cd_conta_pagar = B.cd_conta_pagar) "
							+ "LEFT OUTER JOIN grl_pessoa C ON (B.cd_pessoa = C.cd_pessoa) "
							+ "LEFT OUTER JOIN adm_tipo_documento D ON (B.cd_tipo_documento = D.cd_tipo_documento) "
							+ "LEFT OUTER JOIN grl_pessoa E ON (E.cd_pessoa = B.cd_empresa) "
							+ "LEFT OUTER JOIN grl_pessoa_juridica F ON (F.cd_pessoa = B.cd_empresa) "
							+ "LEFT OUTER JOIN grl_pessoa_endereco G ON (G.cd_pessoa = B.cd_empresa) "
							+ "LEFT OUTER JOIN grl_cidade H ON (H.cd_cidade = G.cd_cidade) "
							+ "WHERE A.cd_conta = " + cdConta
							+ "  AND A.cd_movimento_conta = "
							+ cdMovimentoConta);

			rsm = new ResultSetMap(pstmt.executeQuery());

			while (rsm.next()) {
				rsm.setValueToField("DT_INICIAL", dtInicial);
				rsm.setValueToField("DT_FINAL", dtFinal);
				rsm.setValueToField("CL_VL_CONTA", NumberFormat
						.getCurrencyInstance().format(rsm.getFloat("vl_conta"))
						.toString());
				rsm.setValueToField("CL_VL_MULTA", NumberFormat
						.getCurrencyInstance().format(rsm.getFloat("vl_multa"))
						.toString());
				rsm.setValueToField("CL_VL_JUROS", NumberFormat
						.getCurrencyInstance().format(rsm.getFloat("vl_juros"))
						.toString());
				rsm.setValueToField(
						"CL_VL_DESCONTO",
						NumberFormat.getCurrencyInstance()
								.format(rsm.getFloat("vl_desconto")).toString());
				rsm.setValueToField("CL_VL_PAGO", NumberFormat
						.getCurrencyInstance().format(rsm.getFloat("vl_pago"))
						.toString());
			}

			rsm.beforeFirst();

			return rsm;

		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getProdutosOfMovimento(int cdConta, int cdMovimentoConta) {
		return getProdutosOfMovimento(cdConta, cdMovimentoConta, null);
	}

	public static ResultSetMap getProdutosOfMovimento(int cdConta, int cdMovimentoConta, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSetMap rsm = null;
		try {
			pstmt = connect
					.prepareStatement("SELECT A.*, B.vl_unitario AS vl_custo, B.qt_entrada AS vl_quantidade, (B.qt_entrada * B.vl_unitario) AS vl_total "
							+ "FROM adm_movimento_conta_pagar E "
							+ "JOIN adm_conta_pagar           D ON (E.cd_conta_pagar = D.cd_conta_pagar) "
							+ "JOIN alm_documento_entrada_item    B ON (B.cd_documento_entrada    = D.cd_documento_entrada) "
							+ "JOIN grl_produto_servico         A ON (A.cd_produto_servico    = B.cd_produto_servico) "
							+ "WHERE E.cd_movimento_conta = "
							+ cdMovimentoConta + " AND E.cd_conta = " + cdConta);

			rsm = new ResultSetMap(pstmt.executeQuery());

			while (rsm.next()) {
				rsm.setValueToField("CL_VL_CUSTO", NumberFormat
						.getCurrencyInstance().format(rsm.getFloat("vl_custo"))
						.toString());
				rsm.setValueToField("CL_VL_QUANTIDADE", NumberFormat
						.getInstance().format(rsm.getFloat("vl_quantidade"))
						.toString());
				rsm.setValueToField("CL_VL_TOTAL", NumberFormat
						.getCurrencyInstance().format(rsm.getFloat("vl_total"))
						.toString());
			}

			rsm.beforeFirst();

			return rsm;

		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getRelatorioConsolidacaoDespesas(
			String dtMovimentoInicial, String dtMovimentoFinal, String contas,
			String turnos) {
		return getRelatorioConsolidacaoDespesas(dtMovimentoInicial,
				dtMovimentoFinal, contas, turnos, null);
	}

	public static ResultSetMap getRelatorioConsolidacaoDespesas(
			String dtMovimentoInicial, String dtMovimentoFinal, String contas,
			String turnos, Connection connect) {
		boolean isConnectionNull = connect == null;
		ResultSetMap rsmDespesas = new ResultSetMap();
		ResultSetMap rsmTmpDespesas = new ResultSetMap();
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("B.dt_movimento",
					dtMovimentoInicial, ItemComparator.GREATER_EQUAL,
					Types.TIMESTAMP));
			criterios.add(new ItemComparator("B.dt_movimento",
					dtMovimentoFinal, ItemComparator.MINOR_EQUAL,
					Types.TIMESTAMP));
			criterios.add(new ItemComparator("B.cd_conta", contas,
					ItemComparator.IN, Types.INTEGER));
			criterios.add(new ItemComparator("B.cd_turno", turnos,
					ItemComparator.IN, Types.INTEGER));
			rsmTmpDespesas = find(criterios);
			rsmTmpDespesas.beforeFirst();
			while (rsmTmpDespesas.next()) {
				boolean hasRegister = false;
				while (rsmDespesas.next()) {
					// if( rsmDespesas.getInt("") == rsmTmpDespesas.getInt("")
					// ){
					// //CONTABILIZA TOTAL E SINALIZA QUE JÁ POSSUI O REGISTRO
					// NO RSM
					// hasRegister = true;
					// }
				}
				if (!hasRegister) {
					rsmDespesas.addRegister(rsmTmpDespesas.getRegister());
				}
				rsmDespesas.beforeFirst();
			}

			return rsmDespesas;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result gerarRelatorioPagamentos(ArrayList<ItemComparator> criterios) {
		return gerarRelatorioPagamentos(criterios, null);
	}

	public static Result gerarRelatorioPagamentos(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			ResultSetMap rsm = new ResultSetMap();

			rsm = find(criterios);

			while (rsm.next()) {
				rsm.setValueToField("NM_SITUACAO_MOVIMENTO", MovimentoContaServices.situacaoMovCaixa[rsm.getInt("ST_MOVIMENTO")]);
				rsm.setValueToField("VL_PAGO", rsm.getString("VL_PAGO"));				
				rsm.setValueToField("VL_CONTA", rsm.getString("VL_CONTA"));				
				rsm.setValueToField("VL_JUROS", rsm.getString("VL_JUROS"));
				rsm.setValueToField("VL_DESCONTO", rsm.getString("VL_DESCONTO"));
				rsm.setValueToField("VL_MULTA", rsm.getString("VL_MULTA"));
				rsm.setValueToField("VL_ACRESCIMO", rsm.getString("VL_ACRESCIMO"));
			}

			rsm.beforeFirst();

			HashMap<String, Object> param = new HashMap<String, Object>();
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);

			return result;

		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getLogCompliance(int cdMovimento, int cdConta, boolean lgDelete) {
		return getLogCompliance(cdMovimento, cdConta, lgDelete, null);
	}
	
	public static ResultSetMap getLogCompliance(int cdMovimento, int cdConta, boolean lgDelete, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			ResultSetMap rsm = ComplianceManager
					.search(" SELECT * FROM adm_movimento_conta_pagar "
						+ " WHERE 1=1"
						+ (lgDelete ? 
						  " AND tp_acao_compliance="+ComplianceManager.TP_ACAO_DELETE	
						  :
						  " AND cd_movimento_conta="+cdMovimento+" AND cd_conta_pagar="+cdConta)
						+ " ORDER BY dt_compliance DESC ");
			
			while(rsm.next()) {
				if(rsm.getPointer()==0 && !lgDelete)
					rsm.setValueToField("tp_versao_compliance", "ATUAL");
				else
					rsm.setValueToField("tp_versao_compliance", "ANTIGA");
				
				rsm.setValueToField("nm_tp_acao", ComplianceManager.tipoAcao[rsm.getInt("tp_acao_compliance")].toUpperCase());
				
				if(rsm.getInt("cd_usuario_compliance", 0) > 0) {
					Usuario usuario = UsuarioDAO.get(rsm.getInt("cd_usuario_compliance"), connect);
					Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa(), connect);
					rsm.setValueToField("nm_usuario_compliance", pessoa.getNmPessoa());
				}
				
				if(lgDelete) {
					ResultSetMap rsmDetalhes = new ResultSetMap();
					HashMap<String, Object> regAtual = (HashMap<String, Object>)rsm.getRegister().clone();
					regAtual.put("TP_ITEM_COMPLIANCE", " ");
					rsmDetalhes.addRegister(regAtual);
					rsm.setValueToField("RSM_DETALHE", rsmDetalhes);
					
				}
			}
			rsm.beforeFirst();

			
			if(!lgDelete) {
				while(rsm.next()) {
					ResultSetMap rsmDetalhes = new ResultSetMap();
					HashMap<String, Object> regAtual = (HashMap<String, Object>)rsm.getRegister().clone();
					regAtual.put("TP_ITEM_COMPLIANCE", "PARA");
					rsmDetalhes.addRegister(regAtual);
					
					if(rsm.next()) { //como a ordem é decrescente, o próximo registro representa a versão anterior
						HashMap<String, Object> regAnterior = (HashMap<String, Object>)rsm.getRegister().clone();
						regAnterior.put("TP_ITEM_COMPLIANCE", "DE");
						rsmDetalhes.addRegister(regAnterior);
						rsm.previous();
					}
					
					ArrayList<String> fields = new ArrayList<>();
					fields.add("TP_ITEM_COMPLIANCE");
					rsmDetalhes.orderBy(fields);
					rsm.setValueToField("RSM_DETALHE", rsmDetalhes);
					
				}
				rsm.beforeFirst();
			}
			
			return rsm;
		}
		catch (Exception e) {
			System.out.println("Erro ao buscar log: "+e.getMessage());
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
