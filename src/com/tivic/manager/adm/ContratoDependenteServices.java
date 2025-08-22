package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.OcorrenciaServices;
import com.tivic.manager.grl.ParametroServices;

import sol.dao.ResultSetMap;
import sol.util.Result;

public class ContratoDependenteServices {

	public static final String[] tipoParentesco = {"Cônjuge (1º Grau)",
		   "Filho(a) (1º Grau)",
		   "Pai/Mãe (1º Grau)",
		   "Irmã(ão) (1º Grau)",
		   "Avô/Avó (2º Grau)",
		   "Neto(a) (2º Grau)",
		   "Tio(a) (2º Grau)",
		   "Sobrinho(a) (2º Grau)",
		   "Primo(a) (3º Grau)",
		   "Sogro(a) (Afinidade)",
		   "Cunhado(a) (Afinidade)",
		   "Outros"};

	public static final String[] stDependente = {"Inativo",
		"Ativo"};

	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO = 1;

	public static Result update(ContratoDependente dependente, ContratoPrdSrvDep prodSrvContratato) {
		return update(dependente, prodSrvContratato, null);
	}

	public static Result update(ContratoDependente dependente, ContratoPrdSrvDep prodSrvContratato, Connection connection) {
		boolean isConnectionNull = connection == null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());


			if (ContratoDependenteDAO.update(dependente, connection)<= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erros reportados ao atualizar dados do dependente. Entre em contato com o suporte técnico.");
			}

			if (prodSrvContratato!=null) {
				PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.cd_dependente " +
						"FROM adm_contrato_produto_servico A " +
						"LEFT OUTER JOIN adm_contrato_prd_srv_dep B ON (A.cd_contrato = B.cd_contrato AND " +
						"												A.cd_produto_servico = B.cd_produto_servico AND " +
						"												B.cd_dependente = ? AND " +
						"												B.cd_dependencia = ?) " +
						"WHERE A.cd_contrato = ? " +
						"  AND A.cd_produto_servico = ?");
				pstmt.setInt(1, dependente.getCdDependente());
				pstmt.setInt(2, dependente.getCdDependencia());
				pstmt.setInt(3, dependente.getCdContrato());
				pstmt.setInt(4, prodSrvContratato.getCdProdutoServico());
				ResultSet rs = pstmt.executeQuery();
				boolean next = rs.next();
				if (!next && ContratoProdutoServicoDAO.insert(new ContratoProdutoServico(dependente.getCdContrato(),
						prodSrvContratato.getCdProdutoServico(),
						0 /*vlProdutoServico*/,
						0 /*qtProdutoServico*/,
						null /*dtContratacao*/,
						0 /*cdTipoProdutoServico*/,
						0 /*cdGarantia*/,
						0/*cdReferencia*/,
						0/*cdEmpresa*/), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erros reportados ao incluir informação de produto ou serviço contratado. " +
							"Entre em contato com o suporte técnico.");
				}

				if ((next && rs.getInt("cd_dependente")!=0 ? ContratoPrdSrvDepDAO.update(prodSrvContratato, connection) :
					ContratoPrdSrvDepDAO.insert(prodSrvContratato, connection)) <= 0) {
					return new Result(-1, "Erros reportados ao incluir informação de produto ou serviço contratado. " +
							"Entre em contato com o suporte técnico.");
				}

				if (!next || rs.getInt("cd_dependente")==0) {
					pstmt = connection.prepareStatement("UPDATE adm_contrato_produto_servico " +
							"SET qt_produto_servico = qt_produto_servico + 1" +
							"WHERE cd_contrato = ? " +
							"  AND cd_produto_servico = ?");
					pstmt.setInt(1, prodSrvContratato.getCdContrato());
					pstmt.setInt(2, prodSrvContratato.getCdProdutoServico());
					pstmt.execute();
				}
			}

			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("cdDependencia", dependente.getCdDependencia());
			hash.put("nrDependente", dependente.getNrDependente());

			if (isConnectionNull)
				connection.commit();

			return new Result(1, "", "results", hash);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erros reportados ao incluir dependente. Anote a mensagem de erro e entre em contato com o suporte técnico.\n" +
					e.getLocalizedMessage());
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result insert(ContratoDependente dependente, ContratoPrdSrvDep prodSrvContratato) {
		return insert(dependente, prodSrvContratato, null);
	}

	public static Result insert(ContratoDependente dependente, ContratoPrdSrvDep prodSrvContratato, Connection connection) {
		boolean isConnectionNull = connection == null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			/* rotina de carater temporario para geração de número de dependente, conforme moldes da Plandonto
			 * by Alexandro dos Santos Silva
			 */
			Contrato contrato = ContratoDAO.get(dependente.getCdContrato(), connection);
			if (contrato.getGnContrato() == ContratoServices.gnPLANO_SAUDE) {
				if (contrato.getCdPessoa() == dependente.getCdDependente())
					dependente.setNrDependente(contrato.getNrContrato());
				else {
					PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) " +
							"FROM adm_contrato_dependente " +
							"WHERE cd_contrato = ?");
					pstmt.setInt(1, dependente.getCdContrato());
					ResultSet rs = pstmt.executeQuery();
					int nrDigitoDep = !rs.next() ? 1 : rs.getInt(1)+1;

					String nrDependente = contrato.getNrContrato().replaceAll("(\\.[0-9]*)","."+ new DecimalFormat("00").format(nrDigitoDep));
					dependente.setNrDependente(nrDependente);
				}
			}

			int cdDependencia = 0;
			if ((cdDependencia = ContratoDependenteDAO.insert(dependente, connection)) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erros reportados ao incluir dependente. Entre em contato com o suporte técnico.");
			}

			if (prodSrvContratato!=null) {
				prodSrvContratato.setCdDependencia(cdDependencia);
				PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
						"FROM adm_contrato_produto_servico " +
						"WHERE cd_contrato = ? " +
						"  AND cd_produto_servico = ?");
				pstmt.setInt(1, dependente.getCdContrato());
				pstmt.setInt(2, prodSrvContratato.getCdProdutoServico());
				ResultSet rs = pstmt.executeQuery();
				if (!rs.next() && ContratoProdutoServicoDAO.insert(new ContratoProdutoServico(dependente.getCdContrato(),
						prodSrvContratato.getCdProdutoServico(),
						0 /*vlProdutoServico*/,
						0 /*qtProdutoServico*/,
						null /*dtContratacao*/,
						0 /*cdTipoProdutoServico*/,
						0 /*cdGarantia*/,
						0 /*cdReferencia*/,
						0 /*cdEmpresa*/), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erros reportados ao incluir informação de produto ou serviço contratado. " +
							"Entre em contato com o suporte técnico.");
				}

				if (ContratoPrdSrvDepDAO.insert(prodSrvContratato, connection) <= 0) {
					return new Result(-1, "Erros reportados ao incluir informação de produto ou serviço contratado. " +
							"Entre em contato com o suporte técnico.");
				}

				pstmt = connection.prepareStatement("UPDATE adm_contrato_produto_servico " +
						"SET qt_produto_servico = qt_produto_servico + 1" +
						"WHERE cd_contrato = ? " +
						"  AND cd_produto_servico = ?");
				pstmt.setInt(1, prodSrvContratato.getCdContrato());
				pstmt.setInt(2, prodSrvContratato.getCdProdutoServico());
				pstmt.execute();
			}

			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("cdDependencia", cdDependencia);
			hash.put("nrDependente", dependente.getNrDependente());

			if (isConnectionNull)
				connection.commit();

			return new Result(1, "", "results", hash);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erros reportados ao incluir dependente. Anote a mensagem de erro e entre em contato com o suporte técnico.\n" +
					e.getLocalizedMessage());
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int delete(int cdContrato, int cdDependente, int cdDependencia) {
		return delete(cdContrato, cdDependente, cdDependencia, null);
	}

	public static int delete(int cdContrato, int cdDependente, int cdDependencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_contrato_produto_servico " +
					"SET qt_produto_servico = qt_produto_servico - 1 " +
					"WHERE (cd_contrato, cd_produto_servico) IN (SELECT cd_contrato, cd_produto_servico " +
					"											 FROM adm_contrato_prd_srv_dep " +
					"											 WHERE cd_contrato = ? " +
					"											   AND cd_dependente = ? " +
					"											   AND cd_dependencia = ?) " +
					"  AND qt_produto_servico > 0");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdDependente);
			pstmt.setInt(3, cdDependencia);
			pstmt.execute();

			pstmt = connect.prepareStatement("DELETE " +
					"FROM adm_contrato_prd_srv_dep " +
					"WHERE cd_contrato = ? " +
					"  AND cd_dependente = ? " +
					"  AND cd_dependencia = ?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdDependente);
			pstmt.setInt(3, cdDependencia);
			pstmt.execute();

			if (ContratoDependenteDAO.delete(cdContrato, cdDependente, cdDependencia, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDependenteDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int cancelarDependentes(int cdContrato, GregorianCalendar dtCancelamento, int cdUsuario,
			ArrayList<HashMap<String, Object>> dependentes) {
		return cancelarDependentes(cdContrato, dtCancelamento, cdUsuario, dependentes, null);
	}

	public static int cancelarDependentes(int cdContrato, GregorianCalendar dtCancelamento, int cdUsuario,
			ArrayList<HashMap<String, Object>> dependentes, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			int cdTipoOcorrencia = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OCORRENCIA_CANCEL_DEPENDENTE", 0, 0, connection);

			if (cdTipoOcorrencia<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			for (int i=0; dependentes!=null && i<dependentes.size(); i++) {
				int cdDependente = (Integer)dependentes.get(i).get("cdDependente");
				int cdDependencia = (Integer)dependentes.get(i).get("cdDependencia");
				ContratoDependente dependente = ContratoDependenteDAO.get(cdContrato, cdDependente, cdDependencia, connection);
				if (dependente.getStDependente() != ContratoDependenteServices.ST_ATIVO) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}

				Ocorrencia ocorrencia = new Ocorrencia(0 /*cdOcorrencia*/,
						cdDependente /*cdPessoa*/,
						"Cancelamento de Plano" /*txtOcorrencia*/,
						dtCancelamento /*dtOcorrencia*/,
						cdTipoOcorrencia,
						OcorrenciaServices.ST_CONCLUIDO /*stOcorrencia*/,
						0 /*cdSistema*/,
						cdUsuario,
						0 /*cdTituloCredito*/,
						cdContrato);
				if (OcorrenciaDAO.insert(ocorrencia, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}

				dependente.setStDependente(ContratoDependenteServices.ST_INATIVO);
				dependente.setDtDesvinculacao(dtCancelamento);
				if (ContratoDependenteDAO.update(dependente, connection) <= 0){
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}

				PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
						"FROM adm_conta_receber_evento " +
						"WHERE cd_pessoa = ? " +
						"  AND cd_contrato = ? " +
						"  AND st_evento = ? " +
						"  AND cd_conta_receber IN (SELECT cd_conta_receber " +
						"							FROM adm_conta_receber " +
						"							WHERE st_conta = ? " +
						"							  AND cd_contrato = ?)");
				pstmt.setInt(1, cdDependente);
				pstmt.setInt(2, cdContrato);
				pstmt.setInt(3, ContaReceberEventoServices.ST_ATIVO);
				pstmt.setInt(4, ContaReceberServices.ST_EM_ABERTO);
				pstmt.setInt(5, cdContrato);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					int cdContaReceber = rs.getInt("cd_conta_receber");
					ContaReceber contaReceber = ContaReceberDAO.get(cdContaReceber, connection);
					float vlEvento = rs.getFloat("vl_evento_financeiro");
					ContaReceberEvento eventoReceber = ContaReceberEventoDAO.get(cdContaReceber,
							rs.getInt("cd_evento_financeiro"),
							rs.getInt("cd_conta_receber_evento"), connection);
					eventoReceber.setStEvento(ContaReceberEventoServices.ST_INATIVO);
					if (ContaReceberEventoDAO.update(eventoReceber, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}

					pstmt = connection.prepareStatement("SELECT SUM(vl_recebido - vl_juros - vl_multa + vl_desconto) " +
							"FROM adm_movimento_conta_receber " +
							"WHERE cd_conta_receber = ?");
					pstmt.setInt(1, cdContaReceber);
					ResultSet rsPagamentos = pstmt.executeQuery();
					float vlPago = !rsPagamentos.next() ? 0 : rsPagamentos.getFloat(1);
					Double vlConta = contaReceber.getVlConta() + contaReceber.getVlAcrescimo() - contaReceber.getVlAbatimento();
					if (vlConta - (vlPago + vlEvento) <= 0) {
						contaReceber.setStConta(vlPago>0 ? ContaReceberServices.ST_RECEBIDA : ContaReceberServices.ST_CANCELADA);
						if (contaReceber.getStConta()!=ContaReceberServices.ST_CANCELADA)
							contaReceber.setVlConta(vlConta - vlEvento);
						if (ContaReceberDAO.update(contaReceber, connection) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return -1;
						}
					}
					else {
						contaReceber.setVlConta(vlConta - vlEvento);
						if (ContaReceberDAO.update(contaReceber, connection) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return -1;
						}
					}

					if (contaReceber.getStConta() != ContaReceberServices.ST_CANCELADA) {
						EventoFinanceiro evento = EventoFinanceiroDAO.get(rs.getInt("cd_evento_financeiro"), connection);
						int cdCategoria = evento!=null && evento.getCdCategoriaEconomica()!=0 ? evento.getCdCategoriaEconomica() : 0;
						pstmt = connection.prepareStatement("SELECT * " +
								"FROM adm_conta_receber_categoria " +
								"WHERE cd_conta_receber = ?");
						pstmt.setInt(1, cdContaReceber);
						ResultSetMap rsmCat = new ResultSetMap(pstmt.executeQuery());
						float vlEventoTemp = vlEvento;
						boolean locate = rsmCat.locate("cd_categoria_economica", cdCategoria, false, false, null);
						if (locate) {
							if (rsmCat.getFloat("vl_conta_categoria") > vlEvento) {
								vlEventoTemp = 0;
								if (ContaReceberCategoriaDAO.update(new ContaReceberCategoria(cdContaReceber,
										cdCategoria,
										rsmCat.getFloat("vl_conta_categoria") - vlEvento /*vlEventoFinanceiro*/, rsmCat.getInt("cd_centro_custo")), connection) <= 0) {
									if (isConnectionNull)
										Conexao.rollback(connection);
									return -1;
								}
							}
							else {
								vlEventoTemp -= rsmCat.getFloat("vl_conta_categoria");
								vlConta -= rsmCat.getFloat("vl_conta_categoria");
								if (ContaReceberCategoriaDAO.delete(cdContaReceber, cdCategoria, connection) <= 0) {
									if (isConnectionNull)
										Conexao.rollback(connection);
									return -1;
								}
							}
						}

						if (vlEventoTemp > 0) {
							pstmt = connection.prepareStatement("SELECT * " +
									"FROM adm_conta_receber_categoria " +
									"WHERE cd_conta_receber = ?");
							pstmt.setInt(1, cdContaReceber);
							rsmCat = new ResultSetMap(pstmt.executeQuery());
							while (rsmCat.next()) {
								if (ContaReceberCategoriaDAO.update(new ContaReceberCategoria(cdContaReceber,
										rsmCat.getInt("cd_categoria_economica"),
										rsmCat.getFloat("vl_conta_categoria") - (rsmCat.getFloat("vl_conta_categoria")/vlConta.floatValue() * vlEventoTemp) /*vlEventoFinanceiro*/, rsmCat.getInt("cd_centro_custo")), connection) <= 0) {
									if (isConnectionNull)
										Conexao.rollback(connection);
									return -1;
								}
							}
						}
					}
				}
			}

			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM adm_contrato_dependente " +
					"WHERE cd_contrato = ? " +
					"  AND st_dependente = ?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, ContratoDependenteServices.ST_ATIVO);
			if (!pstmt.executeQuery().next()) {
				Contrato contrato = ContratoDAO.get(cdContrato, connection);
				contrato.setStContrato(ContratoServices.ST_CANCELADO);
				if (ContratoDAO.update(contrato, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDependenteServices.cancelarDependentes: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

}
