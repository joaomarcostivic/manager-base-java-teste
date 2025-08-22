package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.importacao.DesktopImport;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoDAO;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.DocumentoPessoa;
import com.tivic.manager.ptc.DocumentoServices;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.DateUtil;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

public class AitTransporteServices {

	/*
	 * SITUAÇÕES DA AIT
	 */

	/*
	 * ATENCAO! Houve mudança no nome das situacoes! ST_LAVRADA = Situacao
	 * Ocorrencia ST_EMITIDA = Lavrada
	 */

	public static final int AIT_COLETIVO_URBANO = 0;
	public static final int NAD_COLETIVO_URBANO = 1;
	public static final int AIT_COLETIVO_RURAL = 2;
	public static final int NIC_TAXI = 3;
	public static final int NAD_TAXI = 4;

	public static final int ST_CANCELADA = 0;
	public static final int ST_LAVRADA = 1;
	public static final int ST_EMITIDA = 2;
	public static final int ST_RECEBIDA = 3;
	public static final int ST_NIP_EMITIDA = 4;
	public static final int ST_EM_JULGAMENTO_1 = 5;
	public static final int ST_DEFERIDO_1 = 6;
	public static final int ST_INDEFERIDO_1 = 7;
	public static final int ST_NOTIFICADO_DEFERIMENTO_1 = 8;
	public static final int ST_NOTIFICADO_INDEFERIMENTO_1 = 9;
	public static final int ST_EM_JULGAMENTO_2 = 10;
	public static final int ST_DEFERIDO_2 = 11;
	public static final int ST_INDEFERIDO_2 = 12;
	public static final int ST_NOTIFICADO_DEFERIMENTO_2 = 13;
	public static final int ST_NOTIFICADO_INDEFERIEMNTO_2 = 14;

	public static final int ST_EXPIRADA = 15;

	public static final String[] situacaoAit = { "Cancelada", "Ocorrência", "Lavrada", "Recebida", "NIP Emitida",
			"Em Julgamento 1", "Deferido 1", "Indeferido 1", "Notificado Deferimento 1", "Notificado Indeferimento 1",
			"Em Julgamento 2", "Deferido 2", "Indeferido 2", "Notificado Deferimento 2", "Notificado Indeferimento 2",
			"Expirada" };

	/*
	 * FASES DO DOCUMENTO
	 */
	public static final int ST_JURIS_JULGAMENTO_1 = ParametroServices
			.getValorOfParametroAsInteger("ST_JURIS_JULGAMENTO_1", -1);
	public static final int ST_JURIS_DEFERIDO_1 = ParametroServices.getValorOfParametroAsInteger("ST_JURIS_DEFERIDO_1",
			-1);
	public static final int ST_JURIS_INDEFERIDO_1 = ParametroServices
			.getValorOfParametroAsInteger("ST_JURIS_INDEFERIDO_1", -1);
	public static final int ST_JURIS_DEFERIMENTO_NOTIFICADO_1 = ParametroServices
			.getValorOfParametroAsInteger("ST_JURIS_DEFERIMENTO_NOTIFICADO_1", -1);
	public static final int ST_JURIS_INDEFERIMENTO_NOTIFICADO_1 = ParametroServices
			.getValorOfParametroAsInteger("ST_JURIS_INDEFERIMENTO_NOTIFICADO_1", -1);
	public static final int ST_JURIS_JULGAMENTO_2 = ParametroServices
			.getValorOfParametroAsInteger("ST_JURIS_JULGAMENTO_2", -1);
	public static final int ST_JURIS_DEFERIDO_2 = ParametroServices.getValorOfParametroAsInteger("ST_JURIS_DEFERIDO_2",
			-1);
	public static final int ST_JURIS_INDEFERIDO_2 = ParametroServices
			.getValorOfParametroAsInteger("ST_JURIS_INDEFERIDO_2", -1);
	public static final int ST_JURIS_DEFERIMENTO_NOTIFICADO_2 = ParametroServices
			.getValorOfParametroAsInteger("ST_JURIS_DEFERIMENTO_NOTIFICADO_2", -1);
	public static final int ST_JURIS_INDEFERIMENTO_NOTIFICADO_2 = ParametroServices
			.getValorOfParametroAsInteger("ST_JURIS_INDEFERIMENTO_NOTIFICADO_2", -1);
	public static final int ST_JURIS_CANCELADO = ParametroServices.getValorOfParametroAsInteger("ST_JURIS_CANCELADO",
			-1);

	/**
	 * Prazo para entregar AIT
	 * 
	 */
	// lavratura ao recebimento
	public static final int PRAZO_NOTIFICACAO_INICIAL = ParametroServices
			.getValorOfParametroAsInteger("QT_DIAS_PRAZO_NOTIFICACAO_INICIAL", -1);
	// dias para a operadora entrar com recurso
	public static final int PRAZO_INTERPOR_RECURSO = ParametroServices
			.getValorOfParametroAsInteger("QT_DIAS_PRAZO_INTERPOR_INSTANCIA1", -1);
	// Prazo para analizar recurso
	public static final int PRAZO_ANALISAR_RECURSO = ParametroServices
			.getValorOfParametroAsInteger("QT_DIAS_PRAZO_ANALISAR_RECURSO1", -1);
	// Prazo para julgar recurso
	public static final int PRAZO_JULGAR_RECURSO = ParametroServices
			.getValorOfParametroAsInteger("QT_DIAS_PRAZO_JULGAR_INSTANCIA2", -1);
	// Prazo para notificar recurso
	public static final int PRAZO_NOTIFICAR_DECISAO = ParametroServices
			.getValorOfParametroAsInteger("QT_DIAS_PRAZO_NOTIFICAR_INSTANCIA2", -1);

	public static Result save(AitTransporte aitTransporte, int tpTalao) {
		return save(aitTransporte, null, tpTalao);
	}

	public static Result saveValidado(AitTransporte aitTransporte, Boolean importacao) {
		return saveValidado(aitTransporte, importacao, null);
	}

	public static Result save(AitTransporte aitTransporte, Connection connect, int tpTalao) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			/*
			 * Validações
			 */
			if (aitTransporte == null)
				return new Result(-1, "Erro ao salvar. AitTransporte é nulo");

			// Validacao para nao trocer numero do talao, uma vez que a ait ja tenha sido
			// gerada
			if (aitTransporte.getCdAit() != 0) {

				AitTransporte aitTransporte2 = AitTransporteDAO.get(aitTransporte.getCdAit(), connect);
				if (aitTransporte.getCdTalao() != aitTransporte2.getCdTalao()) {
					return new Result(-2, "Não é possível alterar um AIT de Talão.");
				}

			} else {

				ResultSetMap rsmNrAitValidacao = TalonarioAITServices.validarAplicacaoAit(aitTransporte.getNrAit(),
						tpTalao);
				if (rsmNrAitValidacao.getLines().size() == 0) {
					return new Result(-2, "O número da AIT não pertence a um talonário ativo válido");
				}
			}

			// Validacao para nao trocar agente quando a ait ja tiver sido gerada
			if (aitTransporte.getCdAit() != 0) {

				AitTransporte aitTransporte3 = AitTransporteDAO.get(aitTransporte.getCdAit(), connect);
				if (aitTransporte.getCdAgente() != aitTransporte3.getCdAgente()) {
					return new Result(-3, "Não é possível alterar o agente!");
				}

			} else {

				ResultSetMap rsmTalonarioAgenteValidacao = TalonarioAITServices
						.validarAgenteTalaoAit(aitTransporte.getNrAit(), aitTransporte.getCdAgente(), tpTalao);
				if (rsmTalonarioAgenteValidacao.getLines().size() != 1
						&& tpTalao != TalonarioAITServices.TP_TALONARIO_TRANSPORTE) {
					return new Result(-3, "O número da AIT inserida não pertence ao Talão do agente selecionado.");
				}
			}
			
			
			if (validarSituacao(aitTransporte) < 0) {
				return new Result(-1, "Há datas inválidas no preenchimento do AIT.");
			} else {
				int resultadoValidacao = validarSituacao(aitTransporte);
				System.out.println(resultadoValidacao);
				aitTransporte.setStAit(resultadoValidacao);
			}

			int retorno;
			if (aitTransporte.getCdAit() == 0) {
				retorno = AitTransporteDAO.insert(aitTransporte, connect);
				aitTransporte.setCdAit(retorno);
			} else {
				retorno = AitTransporteDAO.update(aitTransporte, connect);
			}

			if (retorno <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno <= 0) ? "Erro ao salvar..." : "Salvo com sucesso...", "AITTRANSPORTE",
					aitTransporte);
		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-5, e.getMessage());
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	private static int validarSituacao(AitTransporte aitTransporte) {
		if (aitTransporte.getDtEmissaoNip() != null && aitTransporte.getDtNotificacaoInicial() != null && aitTransporte.getDtRecebimento() != null && aitTransporte.getDtInfracao() != null)
			return AitTransporteServices.ST_NIP_EMITIDA;
		
		if (aitTransporte.getDtNotificacaoInicial() != null && aitTransporte.getDtRecebimento() != null && aitTransporte.getDtInfracao() != null)
			return AitTransporteServices.ST_EMITIDA;
		
		if (aitTransporte.getDtRecebimento() != null && aitTransporte.getDtInfracao() != null )
			return AitTransporteServices.ST_RECEBIDA;
		
		if (aitTransporte.getDtInfracao() != null )
			return AitTransporteServices.ST_LAVRADA;
		
		return -1;
	}

	public static Result saveValidado(AitTransporte aitTransporte, Boolean importacao, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno;
			if (aitTransporte.getCdAit() == 0 || importacao) {
				retorno = AitTransporteDAO.insert(aitTransporte, connect);
				aitTransporte.setCdAit(retorno);
			} else {
				retorno = AitTransporteDAO.update(aitTransporte, connect);
			}

			if (retorno <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno <= 0) ? "Erro ao salvar..." : "Salvo com sucesso...", "AITTRANSPORTE",
					aitTransporte);
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

	public static Result removeSegundoRecurso(int cdAit, int cdDocumento) {
		return removeSegundoRecurso(cdAit, cdDocumento, null);
	}

	public static Result removeSegundoRecurso(int cdAit, int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;

			String sqlDesvincularRecurso = "UPDATE mob_ait_transporte SET cd_recurso2 = null " + " WHERE cd_ait = "
					+ cdAit;

			PreparedStatement pstmtAit = connect.prepareStatement(sqlDesvincularRecurso);
			retorno = pstmtAit.executeUpdate();

			if (retorno <= 0) {
				Conexao.rollback(connect);
				return new Result(-2, "Este registro estï¿½o vinculado a outros e não pode ser excluï¿½do!");
			} else if (isConnectionNull)
				connect.commit();
			DocumentoServices.remove(cdDocumento, true);
			return new Result(1, "Registro excluï¿½do com sucesso!");
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

	public static Result remove(int cdAit) {
		return remove(cdAit, false, null);
	}

	public static Result remove(int cdAit, boolean cascade) {
		return remove(cdAit, cascade, null);
	}

	public static Result remove(int cdAit, boolean cascade, Connection connect) {
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
			if (!cascade || retorno > 0)
				retorno = AitTransporteDAO.delete(cdAit, connect);
			if (retorno <= 0) {
				Conexao.rollback(connect);
				return new Result(-2, "Este registro estï¿½o vinculado a outros e não pode ser excluï¿½do!");
			} else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluï¿½do com sucesso!");
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

	/**
	 * Notifica as AIT's selecionadas
	 * 
	 * @author Bruno Codato
	 * @param cdAits        Ait's selecionadas que serão autalizadas para Notificado
	 * @param dtNotificacao Data que ocorreu a notificação
	 * @param novaSituacao  Novo situação das AIT's selecionadas
	 * @return AIT's que foram atualizadas
	 */
	public static ResultSetMap notificarAits(ResultSetMap rsmAit, GregorianCalendar dtNotificacao) {
		return notificarAits(rsmAit, dtNotificacao, null);
	}

	/**
	 * Notifica as AIT's selecionadas
	 * 
	 * @author Bruno Codato
	 * @param cdAits        Ait's selecionadas que serão autalizadas para Notificado
	 * @param dtNotificacao Data que ocorreu a notificação
	 * @param novaSituacao  Novo situação das AIT's selecionadas
	 * @param connect       Conexão com o banco
	 * @return AIT's que foram atualizadas
	 */
	public static ResultSetMap notificarAits(ResultSetMap rsmAit, GregorianCalendar dtNotificacao, Connection connect) {
		boolean isConnectionNull = connect == null;

		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			while (rsmAit.next()) {
				int retorno = setNotificarAit(rsmAit.getInt("cd_ait"), rsmAit.getInt("cd_recurso1"),
						rsmAit.getInt("cd_recurso2"), dtNotificacao, rsmAit.getInt("st_ait"), connect);

				if (retorno < 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return rsmAit;
				}
			}

			connect.commit();
			return rsmAit;
		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return rsmAit;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * Notifica as AIT's e o recurso da mesma.
	 * 
	 * @author Bruno Codato
	 * @param cdAits        Ait's selecionadas que serão autalizadas para Notificado
	 * @param dtNotificacao Data que ocorreu a notificação
	 * @param novaSituacao  Novo situação das AIT's selecionadas
	 * @param connect       Conexão com o banco
	 * @return Resultado da atualização da AIT. Se retornar -1, ocorreu um erro
	 */
	public static int setNotificarAit(int cdAit, int cdRecurso1, int cdRecurso2, GregorianCalendar dtNotificacao,
			int situacaoAit, Connection connect) {
		boolean isConnectionNull = connect == null;
		int retorno = 1;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			String sqlNotificarAit = "";
			@SuppressWarnings("unused")
			String sqlNotificarRecurso = "";
			// Verifica em qual fase do recurso a AIT estÃ¡ DEFERIDO_1 ou INDEFERIDO_1
			if (situacaoAit == ST_DEFERIDO_1 || situacaoAit == ST_INDEFERIDO_1) {
				// DEFERIDO_1
				if (situacaoAit == ST_DEFERIDO_1) {
					sqlNotificarAit = "UPDATE mob_ait_transporte SET st_ait= " + ST_NOTIFICADO_DEFERIMENTO_1
							+ ", dt_notificacao1 = ? " + "WHERE cd_ait = " + cdAit;

					sqlNotificarRecurso = "UPDATE ptc_documento SET cd_fase = " + ST_JURIS_DEFERIMENTO_NOTIFICADO_1
							+ " WHERE cd_documento =  " + cdRecurso1;

				} // INDEFERIDO_1
				else {
					sqlNotificarAit = "UPDATE mob_ait_transporte SET st_ait= " + ST_NOTIFICADO_INDEFERIMENTO_1
							+ ", dt_notificacao1 = ? " + "WHERE cd_ait = " + cdAit;

					sqlNotificarRecurso = "UPDATE ptc_documento SET cd_fase = " + ST_JURIS_INDEFERIMENTO_NOTIFICADO_1
							+ "WHERE cd_documento =  " + cdRecurso1;
				}
			}
			// Verifica em qual fase do recurso a AIT estÃ¡ DEFERIDO_2 ou INDEFERIDO_2
			else if (situacaoAit == ST_DEFERIDO_2 || situacaoAit == ST_INDEFERIDO_2) {
				// DEFERIDO_2
				if (situacaoAit == ST_DEFERIDO_2) {
					sqlNotificarAit = "UPDATE mob_ait_transporte SET st_ait= " + ST_NOTIFICADO_DEFERIMENTO_2
							+ ", dt_notificacao2 = ? " + "WHERE cd_ait = " + cdAit;

					sqlNotificarRecurso = "UPDATE ptc_documento SET cd_fase = " + ST_JURIS_DEFERIMENTO_NOTIFICADO_2
							+ "WHERE cd_documento =  " + cdRecurso2;
				}
				// INDEFERIDO_2
				else {
					sqlNotificarAit = "UPDATE mob_ait_transporte SET st_ait= " + ST_NOTIFICADO_INDEFERIEMNTO_2
							+ ", dt_notificacao2 = ? " + "WHERE cd_ait = " + cdAit;

					sqlNotificarRecurso = "UPDATE ptc_documento SET cd_fase = " + ST_JURIS_INDEFERIMENTO_NOTIFICADO_2
							+ "WHERE cd_documento =  " + cdRecurso2;

				}
			}

			PreparedStatement pstmtAit = connect.prepareStatement(sqlNotificarAit);
			pstmtAit.setTimestamp(1, new Timestamp(dtNotificacao.getTimeInMillis()));
			retorno = pstmtAit.executeUpdate();

			PreparedStatement pstmtRecurso = connect.prepareStatement(sqlNotificarAit);
			pstmtRecurso.setTimestamp(1, new Timestamp(dtNotificacao.getTimeInMillis()));
			retorno = retorno * pstmtRecurso.executeUpdate();

			return retorno;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteService.setReceberAit: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * @author Bruno Codato
	 * @param cdAit
	 * @return @
	 */
	public static Result imprimirAits(int[] cdAits, GregorianCalendar dtImpressao) {
		return imprimirAits(cdAits, dtImpressao, null);
	}

	/**
	 * @author Bruno Codato
	 * @param cdAit
	 * @return
	 */
	public static Result imprimirAits(int[] cdAits, GregorianCalendar dtImpressao, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			for (int i = 0; i < cdAits.length; i++) {
				int retorno = setImprimirAit(cdAits[i], dtImpressao, connect);
				if (retorno < 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(retorno, "Erro ao confirmar a impressï¿½o AIT's!");
				}

			}
			connect.commit();
			return new Result(2, "Ait's confirmada as impressï¿½es Sucesso!", "aitArray", cdAits);
		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao confirmar a impressï¿½o das AIT's");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * @author Bruno Codato
	 * @param cdAit
	 * @return @
	 */
	public static Result receberAits(int[] cdAits, GregorianCalendar dtRecebimento) {
		return receberAits(cdAits, dtRecebimento, null);
	}

	/**
	 * @author Bruno Codato
	 * @param cdAit
	 * @return
	 */
	public static Result receberAits(int[] cdAits, GregorianCalendar dtRecebimento, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			for (int i = 0; i < cdAits.length; i++) {
				int retorno = setReceberAit(cdAits[i], dtRecebimento, connect);
				if (retorno < 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(retorno, "Erro ao receber AIT's!");
				}

			}
			connect.commit();
			return new Result(2, "Ait's recebidas com Sucesso!", "aitArray", cdAits);
		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao receber AIT's");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * @author Bruno Codato
	 * @param cdAit
	 * @return
	 */
	public static int setImprimirAit(int cdAit, GregorianCalendar dtImpressao, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_ait_transporte SET st_ait= " + ST_EMITIDA
					+ ", dt_notificacao_inicial = ? " + "WHERE cd_ait = " + cdAit);
			pstmt.setTimestamp(1, new Timestamp(dtImpressao.getTimeInMillis()));
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteService.setImprimirAit: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * @author Bruno Codato
	 * @param cdAit
	 * @return
	 */
	public static int setReceberAit(int cdAit, GregorianCalendar dtRecebimento, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_ait_transporte SET st_ait= " + ST_RECEBIDA
					+ ", dt_recebimento = ? " + "WHERE cd_ait = " + cdAit);
			pstmt.setTimestamp(1, new Timestamp(dtRecebimento.getTimeInMillis()));
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteService.setReceberAit: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findRecursoAitSimplificado(ArrayList<ItemComparator> criterios) {
		return findRecursoAitSimplificado(criterios, null);
	}

	public static ResultSetMap findRecursoAitSimplificado(ArrayList<ItemComparator> criterios, Connection connect) {
		try {
			String sql = " SELECT A.*, B.cd_concessao, B.cd_concessionario, B.tp_concessao, B.nr_concessao, B.st_concessao, "
					+ " C.NM_PESSOA AS NM_CONCESSIONARIO, C.cd_pessoa, "
					+ " D.nr_documento AS nr_documento1, E.cd_fase AS cd_fase1, E.nm_fase, F.nr_documento AS nr_documento2, G.cd_fase AS cd_fase2, G.nm_fase, H.*, I.nr_infracao, I.ds_infracao "
					+ " FROM mob_ait_transporte A "
					+ " LEFT OUTER JOIN mob_concessao             B  ON ( A.cd_concessao = B.cd_concessao) "
					+ " LEFT OUTER JOIN grl_pessoa                C  ON ( B.cd_concessionario = C.cd_pessoa ) "
					+ " LEFT OUTER JOIN ptc_documento             D  ON ( A.cd_recurso1 = D.cd_documento ) "
					+ " LEFT OUTER JOIN ptc_fase               	 E  ON ( D.cd_fase = E.cd_fase) "
					+ " LEFT OUTER JOIN ptc_documento             F  ON ( A.cd_recurso2 = F.cd_documento ) "
					+ " LEFT OUTER JOIN ptc_fase               	 G  ON ( F.cd_fase = G.cd_fase) "
					+ " LEFT OUTER JOIN mob_talonario             H  ON ( A.cd_talao = H.cd_talao ) "
					+ " LEFT OUTER JOIN mob_infracao_transporte   I  ON ( A.cd_infracao = I.cd_infracao ) ";

			ResultSetMap rsm = Search.find(sql, "", criterios, connect != null ? connect : Conexao.conectar(),
					connect == null);

			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.carregarRecursos: " + e);
			return null;
		}
	}

	/**
	 * Metodo que retorna os dados completos relacionados a uma infracao de acordo o
	 * tipo de concessao
	 * 
	 * @param nrAit
	 * @param tpConcessao
	 * @return
	 */
	public static Result getAit(String nrAit, int tpConcessao) {
		return getAit(nrAit, tpConcessao, null);
	}

	public static Result getAit(String nrAit, int tpConcessao, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();

		try {

			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			ResultSetMap rsm = new ResultSetMap();

			String sql = "SELECT A.cd_ait FROM mob_ait_transporte A" + " JOIN mob_talonario B ON (A.cd_talao = B.cd_talao)"
					+ " JOIN mob_concessao C ON (A.cd_concessao = C.cd_concessao)" + " WHERE A.nr_ait = '" + nrAit + "'"
					+ " AND C.tp_concessao = " + tpConcessao;

			ResultSetMap rsmAits = new ResultSetMap(connect.prepareStatement(sql).executeQuery());

			switch (rsmAits.size()) {
			case 0:
				return new Result(0, "");

			case 1:
				rsmAits.next();

				switch (tpConcessao) {
				case ConcessaoServices.TP_COLETIVO_URBANO:

					criterios.add(
							new ItemComparator("cd_ait", rsmAits.getInt("cd_ait") + "", ItemComparator.EQUAL, Types.INTEGER));
					break;

				case ConcessaoServices.TP_TAXI:

					criterios.add(
							new ItemComparator("cd_ait", rsmAits.getInt("cd_ait") + "", ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("pesquisaPonto", "true", ItemComparator.EQUAL, Types.CHAR));
					break;

				default:
					return new Result(rsmAits.getInt("CD_AIT"), "", "AIT_TRANSPORTE", rsmAits.getRegister());
				}

				rsm = find(criterios);
				if (rsm.next())
					return new Result(rsm.getInt("CD_AIT"), "", "AIT_TRANSPORTE", rsm.getRegister());

			default:
				return new Result(-1, "Existe mais de uma infração com a mesma numeração!");
			}

		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.getAit: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.getAit: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public List<AitTransporteDTO> findAit(Criterios criterios) throws Exception {
		return findAit(criterios, null);
	}

	public List<AitTransporteDTO> findAit(Criterios criterios, Connection connection) throws Exception {
		boolean isConnNull = connection == null;
		try {

			if (isConnNull)
				connection = Conexao.conectar();

			Criterios crt = new Criterios();
			int qtLimite = 0;
			int qtDeslocamento = 0;
			String orderBy = "";

			for (int i = 0; criterios != null && i < criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite"))
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("qtDeslocamento"))
					qtDeslocamento = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY"))
					orderBy = " ORDER BY " + criterios.get(i).getValue().toString().trim();
				else
					crt.add(criterios.get(i));
			}

			String sql = "SELECT  A.cd_ait, A.cd_infracao, A.cd_agente, A.nr_ait, A.st_ait, A.tp_ait, A.cd_concessao_veiculo, A.cd_linha, "
					+ " 	A.ds_observacao,  A.vl_multa, G.nm_agente, B.tp_talao, I.nm_pessoa FROM mob_ait_transporte A LEFT OUTER JOIN mob_talonario B ON (A.cd_talao = B.cd_talao) "
					+ "		LEFT OUTER JOIN mob_concessao_veiculo C ON (C.cd_concessao_veiculo = A.cd_concessao_veiculo) "
					+ "		LEFT OUTER JOIN mob_infracao_transporte D ON (D.cd_infracao = A.cd_infracao) "
					+ "		LEFT OUTER JOIN fta_veiculo E ON (E.cd_veiculo = C.cd_veiculo) "
					+ "		LEFT OUTER JOIN mob_linha F ON (F.cd_linha = A.cd_linha) "
					+ "    LEFT OUTER JOIN mob_agente G ON (G.cd_agente = A.cd_agente) "
					+ "    LEFT OUTER JOIN	mob_concessao H ON (H.cd_concessao = A.cd_concessao) "
					+ "    LEFT OUTER JOIN grl_pessoa I ON (H.cd_concessionario = I.cd_pessoa) " + "		WHERE 1=1";

			ResultSetMap rsm = Search.find(sql, "", crt, connection, isConnNull);
			
			List<AitTransporteDTO> aits = new AitTransporteDTO.ListBuilder(rsm).build();
			
			return aits;
		} catch (Exception ex) {
			System.out.println("Erro! ConcessaoServices.findColetivoUrbano");
			ex.printStackTrace(System.out);
			throw ex;
		} finally {
			if (isConnNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_transporte");
			return new ResultSetMap(pstmt.executeQuery());
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.getAll: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.getAll: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getNipImpressao(ArrayList<ItemComparator> criterios) {
		return getNipImpressao(criterios, null);
	}

	public static ResultSetMap getNipImpressao(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {

			int qtDiasPrazoInterporInstancia1 = ParametroServices
					.getValorOfParametroAsInteger("QT_DIAS_PRAZO_INTERPOR_INSTANCIA1", 0, 0, connect);

			return Search.find(" SELECT A.*, B.*, B.NM_PESSOA AS NM_CONCESSIONARIO, C.*, D.*, E.*, F.*, G.* "
					+ " FROM mob_ait_transporte A "
					+ " LEFT OUTER JOIN grl_pessoa                B on ( A.cd_permissionario = B.cd_pessoa ) "
					+ " LEFT OUTER JOIN fta_veiculo               C on ( A.cd_veiculo = C.cd_veiculo )       "
					+ " LEFT OUTER JOIN mob_concessao_veiculo     D on ( C.cd_veiculo = D.cd_veiculo  )      "
					+ " LEFT OUTER JOIN mob_concessao             E on ( E.cd_concessao = D.cd_concessao AND E.cd_concessionario = A.cd_permissionario  ) "
					+ " LEFT OUTER JOIN mob_infracao_transporte   F on ( A.cd_infracao = F.cd_infracao ) "
					+ " LEFT OUTER JOIN mob_agente                G on ( A.cd_agente = G.cd_agente )"
					+ " WHERE ( EXTRACT( DAY FROM (NOW() - A.DT_NOTIFICACAO_INICIAL ) )  > " + qtDiasPrazoInterporInstancia1
					+ " AND A.DT_PROCESSO1 IS NULL )" + " OR ( A.DT_PROCESSO1 IS NOT NULL AND A.ST_PROCESSO1 = "
					+ ST_INDEFERIDO_1 + " ) " + " OR ( A.DT_PROCESSO2 IS NOT NULL AND A.ST_PROCESSO2 = " + ST_INDEFERIDO_2
					+ " ) ", criterios, connect != null ? connect : Conexao.conectar(), connect == null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.getNipImpressao: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findExpiradas(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = new ResultSetMap();
			ResultSetMap rsmExpiradas = new ResultSetMap();
			// alimenta rsm
			rsm = find(criterios, null);

			while (rsm.next()) {
				// data da infração
				GregorianCalendar dtPrazoFinal = null;

				HashMap<String, Object> register = rsm.getRegister();

				String msgSituacao = "";
				switch (rsm.getInt("st_ait")) {
				case ST_LAVRADA:
					dtPrazoFinal = DateUtil.addUtilsDays(rsm.getGregorianCalendar("dt_infracao"), PRAZO_NOTIFICACAO_INICIAL);
					msgSituacao = "Expirada na Lavratura - Prazo(" + Util.formatDate(dtPrazoFinal, "dd/MM/YYYY") + ")";
					break;
				case ST_EMITIDA:
					dtPrazoFinal = DateUtil.addUtilsDays(rsm.getGregorianCalendar("dt_notificacao_inicial"),
							PRAZO_NOTIFICACAO_INICIAL);
					msgSituacao = "Expirada na Emissão - Prazo(" + Util.formatDate(dtPrazoFinal, "dd/MM/YYYY") + ")";
					break;
				case ST_RECEBIDA:
					dtPrazoFinal = DateUtil.addUtilsDays(rsm.getGregorianCalendar("dt_recebimento"), PRAZO_INTERPOR_RECURSO);
					msgSituacao = "Expirou para Recurso 1 (" + Util.formatDate(dtPrazoFinal, "dd/MM/YYYY") + ")";
					break;
				case ST_EM_JULGAMENTO_1:
					dtPrazoFinal = DateUtil.addUtilsDays(rsm.getGregorianCalendar("dt_recebimento"),
							PRAZO_INTERPOR_RECURSO + PRAZO_ANALISAR_RECURSO + PRAZO_JULGAR_RECURSO);
					msgSituacao = "Expirou prazo Análise e Julgamento 1 (" + Util.formatDate(dtPrazoFinal, "dd/MM/yyyy")
							+ ")";
					break;
				case ST_INDEFERIDO_1:
					dtPrazoFinal = DateUtil.addUtilsDays(rsm.getGregorianCalendar("dt_julgamento1"),
							PRAZO_NOTIFICAR_DECISAO);
					msgSituacao = "Expirou prazo Informar Indeferido 1 (" + Util.formatDate(dtPrazoFinal, "dd/MM/yyyy")
							+ ")";
					break;
				case ST_NOTIFICADO_INDEFERIMENTO_1:
					dtPrazoFinal = DateUtil.addUtilsDays(rsm.getGregorianCalendar("dt_notificacao1"),
							PRAZO_NOTIFICACAO_INICIAL);
					msgSituacao = "Expirou prazo para Recurso 2 (" + Util.formatDate(dtPrazoFinal, "dd/MM/yyyy") + ")";
					break;
				case ST_EM_JULGAMENTO_2:
					dtPrazoFinal = DateUtil.addUtilsDays(rsm.getGregorianCalendar("dt_notificacao1"),
							PRAZO_NOTIFICACAO_INICIAL + PRAZO_INTERPOR_RECURSO + PRAZO_ANALISAR_RECURSO
									+ PRAZO_JULGAR_RECURSO);
					msgSituacao = "Expirou prazo Análise e Julgamento 2 (" + Util.formatDate(dtPrazoFinal, "dd/MM/yyyy")
							+ ")";
					break;
				case ST_INDEFERIDO_2:
					dtPrazoFinal = DateUtil.addUtilsDays(rsm.getGregorianCalendar("dt_notificacao2"),
							PRAZO_NOTIFICAR_DECISAO);
					msgSituacao = "Expirou prazo Informar Indeferido 2 (" + Util.formatDate(dtPrazoFinal, "dd/MM/yyyy")
							+ ")";
					break;

				default:
					msgSituacao = "AIT em dia";
					break;
				}

				// compara o prazo final com o dia atual
				if (dtPrazoFinal != null) {
					dtPrazoFinal.set(GregorianCalendar.HOUR_OF_DAY, 0);
					dtPrazoFinal.set(GregorianCalendar.MINUTE, 0);
					dtPrazoFinal.set(GregorianCalendar.SECOND, 0);
					dtPrazoFinal.set(GregorianCalendar.MILLISECOND, 0);

					if (DateUtil.todayWithoutTime().after(dtPrazoFinal)) {
						// Armazena a situação original para ser usada posteriormente, hoje, no flex.
						register.put("ST_AIT_ORIGINAL", rsm.getInt("st_ait"));
						// Altera o valor no rsm
						register.replace("ST_AIT", ST_EXPIRADA);
						register.put("DS_SITUACAO", msgSituacao);
						rsmExpiradas.addRegister(register);
					}
				}
			}

			return rsmExpiradas;

		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.findExpiradas: " + e);
			return null;
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		int qtLimite = 0;
		String orderBy = "";
		boolean pesquisaPonto = false;
		criterios.add(new ItemComparator("qtLimite", "80", ItemComparator.EQUAL, Types.INTEGER));

		for (int i = 0; criterios != null && i < criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("TP_AIT")
					&& criterios.get(i).getValue().equalsIgnoreCase("-1")) {
				criterios.remove(i);
				i--;
				continue;
			} else if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
				qtLimite = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				i--;
				continue;
			} else if (criterios.get(i).getColumn().equalsIgnoreCase("ST_AIT")
					&& criterios.get(i).getValue().equalsIgnoreCase("-1")) {
				criterios.remove(i);
				i--;
				continue;
			} else if (criterios.get(i).getColumn().equalsIgnoreCase("N.CD_GRUPO_PARADA")
					&& criterios.get(i).getValue().equalsIgnoreCase("-1")) {
				criterios.remove(i);
				i--;
				continue;
			} else if (criterios.get(i).getColumn().equalsIgnoreCase("CD_PARADA")
					&& criterios.get(i).getValue().equalsIgnoreCase("-1")) {
				criterios.remove(i);
				i--;
				continue;
			} else if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY")) {
				orderBy = " ORDER BY " + criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
				continue;
			} else if (criterios.get(i).getColumn().equalsIgnoreCase("pesquisaPonto")) {
				pesquisaPonto = true;
				criterios.remove(i);
				i--;
				continue;
			} else if (criterios.get(i).getColumn().equalsIgnoreCase("TP_AIT")
					&& criterios.get(i).getValue().equalsIgnoreCase(String.valueOf(NIC_TAXI))) {
				pesquisaPonto = true;
			}
		}

		String sql = " SELECT A.*, B.cd_concessao, B.cd_concessionario, B.tp_concessao, B.nr_concessao, B.st_concessao, "
				+ " C.NM_PESSOA AS NM_CONCESSIONARIO, C.cd_pessoa, "
				+ " D.cd_concessao_veiculo, D.cd_veiculo, D.nr_prefixo, D.st_concessao_veiculo, "
				+ " E.cd_veiculo, E.cd_proprietario, E.nr_placa, F.*, "
				+ " F1.cd_motivo, F1.nm_motivo, G.*, H.*, I.*, J.*, K.*, L.*, II.*, JJ.* "
				+ (pesquisaPonto
						? ", M.CD_PARADA, N.CD_GRUPO_PARADA, M.ds_referencia AS NR_ORDEM_PARADA, N.nm_grupo_parada AS NR_PONTO_PARADA, N.nm_grupo_parada, "
								+ "P.nm_logradouro, P.nm_bairro as nm_bairro_2, P.nr_endereco, Q.nm_tipo_logradouro"
						: "")
				+ " FROM mob_ait_transporte A "
				+ " LEFT OUTER JOIN mob_concessao             B  ON ( A.cd_concessao = B.cd_concessao) "
				+ " LEFT OUTER JOIN grl_pessoa                C  ON ( B.cd_concessionario = C.cd_pessoa ) "
				+ " LEFT OUTER JOIN mob_concessao_veiculo     D  ON ( A.cd_concessao_veiculo = D.cd_concessao_veiculo  ) "
				+ " LEFT OUTER JOIN fta_veiculo               E  ON ( D.cd_veiculo = E.cd_veiculo )       "
				+ " LEFT OUTER JOIN mob_infracao_transporte   F  ON ( A.cd_infracao = F.cd_infracao ) "
				+ " LEFT OUTER JOIN mob_infracao_motivo       F1 ON ( F.cd_infracao = F1.cd_infracao "
				+ "												AND  A.cd_motivo = F1.cd_motivo) "
				+ " LEFT OUTER JOIN mob_agente                G  ON ( A.cd_agente = G.cd_agente ) "
				+ " LEFT OUTER JOIN mob_linha                 H  ON ( A.cd_linha = H.cd_linha ) "
				+ " LEFT OUTER JOIN ptc_documento             I  ON ( A.cd_recurso1 = I.cd_documento ) "
				+ " LEFT OUTER JOIN ptc_fase               	 II ON ( I.cd_fase = II.cd_fase) "
				+ " LEFT OUTER JOIN ptc_documento             J  ON ( A.cd_recurso2 = I.cd_documento ) "
				+ " LEFT OUTER JOIN ptc_fase               	 JJ  ON ( J.cd_fase = JJ.cd_fase) "
				+ " LEFT OUTER JOIN mob_motivos_encerramento  K  ON ( A.cd_motivo_encerramento_ait = K.cd_motivo_encerramento ) "
				+ " LEFT OUTER JOIN mob_talonario             L  ON ( A.cd_talao = L.cd_talao ) "
				+ (pesquisaPonto ? " LEFT OUTER JOIN mob_parada			     M  ON (B.cd_concessao = M.cd_concessao) "
						+ " LEFT OUTER JOIN mob_grupo_parada          N  ON (M.cd_grupo_parada = N.cd_grupo_parada) "
						+ " LEFT OUTER JOIN grl_pessoa_endereco       P  ON (C.cd_pessoa = P.cd_pessoa) "
						+ " LEFT OUTER JOIN grl_tipo_logradouro       Q  ON (P.cd_tipo_logradouro = Q.cd_tipo_logradouro) "
						: "");

		ResultSetMap rsm = Search.find(sql,
				(orderBy != "" ? orderBy + " " : "") + (qtLimite == 0 ? "" : (" LIMIT " + qtLimite)), criterios,
				connect != null ? connect : Conexao.conectar(), connect == null);

		return rsm;
	}

	public static ResultSetMap carregarRecursos(int cdAit) {
		return carregarRecursos(cdAit, null);
	}

	public static ResultSetMap carregarRecursos(int cdAit, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(" SELECT A.*, " + " A.nr_assunto AS cd_assunto, " + " D.nm_tipo_documento, "
					+ " I.nm_situacao_documento, " + " J.nm_fase, " + " M.*, " + " P.nr_processo,  "
					+ " Q.dt_julgamento1, Q.dt_notificacao1, Q.dt_julgamento2, Q.dt_notificacao2 " + " FROM ptc_documento A "
					+ " LEFT OUTER JOIN gpn_tipo_documento     D  ON (A.cd_tipo_documento = D.cd_tipo_documento) "
					+ " LEFT OUTER JOIN ptc_tipo_documento     D1 ON (D.cd_tipo_documento = D1.cd_tipo_documento) "
					+ " LEFT OUTER JOIN ptc_situacao_documento I  ON (A.cd_situacao_documento = I.cd_situacao_documento) "
					+ " LEFT OUTER JOIN ptc_fase               J  ON (A.cd_fase = J.cd_fase) "
					+ " JOIN ptc_documento_tramitacao          M  ON (A.cd_documento = M.cd_documento) "
					+ " LEFT OUTER JOIN prc_processo           P  ON (A.cd_processo = P.cd_processo) "
					+ " LEFT OUTER JOIN mob_ait_transporte     Q  ON (A.cd_documento = Q.cd_recurso1 "
					+ "											OR A.cd_documento = Q.cd_recurso2 ) " + " WHERE 1 = 1 "
					+ " AND Q.cd_ait = ? " + " AND M.cd_tramitacao = (SELECT max(cd_tramitacao) "
					+ " 						FROM ptc_documento_tramitacao "
					+ " 						WHERE cd_documento = A.cd_documento) ORDER BY A.cd_documento ASC");

			pstmt.setInt(1, cdAit);
			return new ResultSetMap(pstmt.executeQuery());
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.carrearRecursos: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.carregarRecursos: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * Buscando informações das quantidades de ait por concessão
	 * 
	 * @author Bruno Codato
	 * @since 2017-04-13
	 * @param cdAits Array contendo uma lista de aits
	 * @return ResultSetMap
	 */
	public static ResultSetMap getGraficoAitConcessao(int[] cdAits) {
		return getGraficoAitConcessao(cdAits, null);
	}

	public static ResultSetMap getGraficoAitConcessao(int[] cdAits, Connection connect) {

		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String listarAits = new String();

			for (int i : cdAits) {
				/* Transformando a array em string */
				listarAits += i + ", ";
			}
			/* Buscando todos os clientes atravï¿½s dos processos que o mï¿½todo recebe. */
			PreparedStatement pstmtClientes = connect.prepareStatement(" SELECT count(*) AS QTD_AITS,  " + " ( "
					+ " SELECT count(*) AS TOTAL_AITS " + " FROM mob_ait_transporte A "
					+ " LEFT OUTER JOIN mob_concessao B ON ( A.cd_concessao = B.cd_concessao) "
					+ " LEFT OUTER JOIN grl_pessoa    C ON ( B.cd_concessionario = C.cd_pessoa) " + " WHERE A.cd_ait IN(  "
					+ listarAits.substring(0, listarAits.length() - 2) + " )  " + " ),  "
					+ " C.nm_pessoa AS NM_CONCESSIONARIO " + " FROM mob_ait_transporte A "
					+ " LEFT OUTER JOIN mob_concessao B ON ( A.cd_concessao = B.cd_concessao) "
					+ " LEFT OUTER JOIN grl_pessoa    C ON ( B.cd_concessionario = C.cd_pessoa) " + " WHERE A.cd_ait IN(  "
					+ listarAits.substring(0, listarAits.length() - 2) + " ) "
					+ " GROUP BY A.cd_concessao, B.cd_concessionario, cd_pessoa ");

			ResultSetMap rsmAits = new ResultSetMap(pstmtClientes.executeQuery());

			ResultSetMap rsm = new ResultSetMap();

			HashMap<String, Object> cliente = new HashMap<String, Object>();

			cliente.put("rsmQtdAitAConcessao", rsmAits.getLines());
			rsm.addRegister(cliente);

			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.getGraficoAitConcessao: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getGraficoAitLinhaConcessao(int[] cdAits) {
		return getGraficoAitLinhaConcessao(cdAits, null);
	}

	public static ResultSetMap getGraficoAitLinhaConcessao(int[] cdAits, Connection connect) {

		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String listarAits = new String();

			for (int i : cdAits) {
				/* Transformando a array em string */
				listarAits += i + ", ";
			}
			/* Buscando todos os clientes atravï¿½s dos processos que o mï¿½todo recebe. */
			PreparedStatement pstmtClientes = connect.prepareStatement(" SELECT count(*) AS QTD_AITS,  " + " ( "
					+ " SELECT count(*) AS TOTAL_AITS " + " FROM mob_ait_transporte A "
					+ " LEFT OUTER JOIN mob_linha B ON ( A.cd_linha = B.cd_linha)  " + " WHERE A.cd_ait IN(  "
					+ listarAits.substring(0, listarAits.length() - 2) + " )  " + " ),  " + " B.nr_linha "
					+ " FROM mob_ait_transporte A " + " LEFT OUTER JOIN mob_linha B ON ( A.cd_linha = B.cd_linha)  "
					+ " WHERE A.cd_ait IN(  " + listarAits.substring(0, listarAits.length() - 2) + " ) "
					+ " GROUP BY B.cd_linha " + " ORDER BY B.cd_concessao, B.nr_linha ");

			ResultSetMap rsmAits = new ResultSetMap(pstmtClientes.executeQuery());

			while (rsmAits.next()) {
				if (rsmAits.getString("nr_linha") == null) {
					HashMap<String, Object> register = rsmAits.getRegister();
					register.put("NR_LINHA", "ï¿½. Inf.");
					rsmAits.updateRegister(register);
				}
			}

			ResultSetMap rsm = new ResultSetMap();

			HashMap<String, Object> cliente = new HashMap<String, Object>();

			cliente.put("rsmQtdAitAConcessao", rsmAits.getLines());
			rsm.addRegister(cliente);

			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.getGraficoAitLinhaConcessao: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getGraficoInfracaoLinhaConcessao(int[] cdAits, int cdInfracao) {
		return getGraficoInfracaoLinhaConcessao(cdAits, cdInfracao, null);
	}

	public static ResultSetMap getGraficoInfracaoLinhaConcessao(int[] cdAits, int cdInfracao, Connection connect) {

		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String listarAits = new String();

			for (int i : cdAits) {
				/* Transformando a array em string */
				listarAits += i + ", ";
			}
			/* Buscando todos os clientes atravï¿½s dos processos que o mï¿½todo recebe. */
			PreparedStatement pstmtClientes = connect.prepareStatement(" SELECT count(*) AS QTD_AITS,  " + " ( "
					+ " SELECT count(*) AS TOTAL_AITS " + " FROM mob_ait_transporte A " + " WHERE A.cd_ait IN(  "
					+ listarAits.substring(0, listarAits.length() - 2) + " )  " + " ),  "
					+ " B.nr_linha, C.cd_infracao, C.nr_infracao, C.ds_infracao, D.nm_motivo "
					+ " FROM mob_ait_transporte A "
					+ " LEFT OUTER JOIN mob_linha               B ON ( A.cd_linha = B.cd_linha)  "
					+ " LEFT OUTER JOIN mob_infracao_transporte C ON ( A.cd_infracao = C.cd_infracao) "
					+ " LEFT OUTER JOIN mob_infracao_motivo     D ON ( C.cd_infracao = D.cd_infracao"
					+ "											AND  A.cd_motivo   = D.cd_motivo ) " + " WHERE A.cd_ait IN(  "
					+ listarAits.substring(0, listarAits.length() - 2) + " ) " + "   AND C.cd_infracao = " + cdInfracao
					+ " GROUP BY B.cd_linha, C.cd_infracao, D.nm_motivo " + " ORDER BY B.cd_concessao, B.nr_linha ");

			ResultSetMap rsmAits = new ResultSetMap(pstmtClientes.executeQuery());

			while (rsmAits.next()) {
				if (rsmAits.getString("nr_linha") == null) {
					HashMap<String, Object> register = rsmAits.getRegister();
					register.put("NR_LINHA", "ï¿½. Inf.");
					rsmAits.updateRegister(register);
				}
			}

			ResultSetMap rsm = new ResultSetMap();

			HashMap<String, Object> cliente = new HashMap<String, Object>();

			cliente.put("rsmQtdAitAConcessao", rsmAits.getLines());
			rsm.addRegister(cliente);

			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.getGraficoInfracaoLinhaConcessao: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getGraficoAitDiaConcessao(int[] cdAits) {
		return getGraficoAitDiaConcessao(cdAits, null);
	}

	public static ResultSetMap getGraficoAitDiaConcessao(int[] cdAits, Connection connect) {

		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String listarAits = new String();

			for (int i : cdAits) {
				/* Transformando a array em string */
				listarAits += i + ", ";
			}
			/* Buscando todos os clientes atravï¿½s dos processos que o mï¿½todo recebe. */
			PreparedStatement pstmtClientes = connect
					.prepareStatement(" SELECT count(*) AS QTD_AITS, CAST(A.dt_infracao AS DATE) AS DT_INFRACAO, " + " ( "
							+ " SELECT count(*) AS TOTAL_AITS " + " FROM mob_ait_transporte A " + " WHERE A.cd_ait IN(  "
							+ listarAits.substring(0, listarAits.length() - 2) + " )  " + " )  "
							+ " FROM mob_ait_transporte A " + " WHERE A.cd_ait IN(  "
							+ listarAits.substring(0, listarAits.length() - 2) + " ) "
							+ " GROUP BY CAST(A.dt_infracao AS DATE) " + " ORDER BY CAST(A.dt_infracao AS DATE) ");

			ResultSetMap rsmAits = new ResultSetMap(pstmtClientes.executeQuery());

			while (rsmAits.next()) {
				if (rsmAits.getString("dt_infracao") == null) {
					HashMap<String, Object> register = rsmAits.getRegister();
					register.put("DT_INFRACAO", "ï¿½. Inf.");
					rsmAits.updateRegister(register);
				}
			}

			ResultSetMap rsm = new ResultSetMap();

			HashMap<String, Object> cliente = new HashMap<String, Object>();

			cliente.put("rsmQtdAitAConcessao", rsmAits.getLines());
			rsm.addRegister(cliente);

			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.getGraficoAitDiaConcessao: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getGraficoAitAgenteConcessao(int[] cdAits) {
		return getGraficoAitAgenteConcessao(cdAits, null);
	}

	public static ResultSetMap getGraficoAitAgenteConcessao(int[] cdAits, Connection connect) {

		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String listarAits = new String();

			for (int i : cdAits) {
				/* Transformando a array em string */
				listarAits += i + ", ";
			}
			/* Buscando todos os clientes atravï¿½s dos processos que o mï¿½todo recebe. */
			PreparedStatement pstmtClientes = connect.prepareStatement(" SELECT count(*) AS QTD_AITS,  " + " ( "
					+ " SELECT count(*) AS TOTAL_AITS " + " FROM mob_ait_transporte A "
					+ " LEFT OUTER JOIN mob_agente B ON ( A.cd_agente = B.cd_agente)  " + " WHERE A.cd_ait IN(  "
					+ listarAits.substring(0, listarAits.length() - 2) + " )  " + " ),  " + " B.nm_agente "
					+ " FROM mob_ait_transporte A " + " LEFT OUTER JOIN mob_agente B ON ( A.cd_agente = B.cd_agente) "
					+ " WHERE A.cd_ait IN(  " + listarAits.substring(0, listarAits.length() - 2) + " ) "
					+ " GROUP BY B.cd_agente " + " ORDER BY B.nm_agente ");

			ResultSetMap rsmAits = new ResultSetMap(pstmtClientes.executeQuery());

			while (rsmAits.next()) {
				if (rsmAits.getString("nm_agente") == null) {
					HashMap<String, Object> register = rsmAits.getRegister();
					register.put("NM_AGENTE", "ï¿½. Inf.");
					rsmAits.updateRegister(register);
				}
			}

			ResultSetMap rsm = new ResultSetMap();

			HashMap<String, Object> cliente = new HashMap<String, Object>();

			cliente.put("rsmQtdAitAConcessao", rsmAits.getLines());
			rsm.addRegister(cliente);

			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.getGraficoAitAgenteConcessao: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getGraficoAitInfracaoConcessao(int[] cdAits) {
		return getGraficoAitInfracaoConcessao(cdAits, null);
	}

	public static ResultSetMap getGraficoAitInfracaoConcessao(int[] cdAits, Connection connect) {

		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String listarAits = new String();

			for (int i : cdAits) {
				/* Transformando a array em string */
				listarAits += i + ", ";
			}
			/* Buscando todos os clientes atravï¿½s dos processos que o mï¿½todo recebe. */
			PreparedStatement pstmtClientes = connect.prepareStatement(" SELECT count(*) AS QTD_AITS,  " + " ( "
					+ " SELECT count(*) AS TOTAL_AITS " + " FROM mob_ait_transporte A "
					+ " LEFT OUTER JOIN mob_infracao_transporte B ON ( A.cd_infracao = B.cd_infracao)  "
					+ " WHERE A.cd_ait IN(  " + listarAits.substring(0, listarAits.length() - 2) + " )  " + " ),  "
					+ " B.nr_infracao, B.ds_infracao " + " FROM mob_ait_transporte A "
					+ " LEFT OUTER JOIN mob_infracao_transporte B ON ( A.cd_infracao = B.cd_infracao) "
					+ " WHERE A.cd_ait IN(  " + listarAits.substring(0, listarAits.length() - 2) + " ) "
					+ " GROUP BY B.cd_infracao " + " ORDER BY B.nr_infracao ");

			ResultSetMap rsmAits = new ResultSetMap(pstmtClientes.executeQuery());

			while (rsmAits.next()) {
				if (rsmAits.getString("nr_infracao") == null) {
					HashMap<String, Object> register = rsmAits.getRegister();
					register.put("NR_INFRACAO", "ï¿½. Inf.");
					rsmAits.updateRegister(register);
				}
			}

			ResultSetMap rsm = new ResultSetMap();

			HashMap<String, Object> cliente = new HashMap<String, Object>();

			cliente.put("rsmQtdAitAConcessao", rsmAits.getLines());
			rsm.addRegister(cliente);

			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.getGraficoAitInfracaoConcessao: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * Para montar os graficos - retorna a quantidade de aits validas por empresa,
	 * dentro um periodo de tempo
	 */
	public static ResultSetMap getTotalAitsPorEmpresa(GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int tpConcessao) {
		return getTotalAitsPorEmpresa(dtInicial, dtFinal, tpConcessao, null);
	}

	public static ResultSetMap getTotalAitsPorEmpresa(GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int tpConcessao, Connection connect) {

		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {

			// Buscar
			String sql = "SELECT B.cd_concessionario, A.nm_pessoa AS nm_concessionario, B.cd_concessao "
					+ " FROM grl_pessoa A" + " JOIN mob_concessao B ON (A.cd_pessoa = B.cd_concessionario)"
					+ " WHERE B.st_concessao = " + ConcessaoServices.ST_ATIVO + " AND B.tp_concessao = " + tpConcessao
					+ " ORDER BY A.nm_pessoa";

			ResultSetMap rsmConcessoes = new ResultSetMap(connect.prepareStatement(sql).executeQuery());

			sql = "SELECT count(*) as total_ait, sum(vl_multa) as total_multa FROM mob_ait_transporte "
					+ " WHERE st_ait <> " + ST_CANCELADA +
//					" AND st_ait <> " + ST_DEFERIDO_1 +
//					" AND st_ait <> " + ST_DEFERIDO_2 +
					" AND dt_infracao >= ? AND dt_infracao <= ?" + " AND cd_concessao IN "
					+ " (SELECT cd_concessao FROM mob_concessao " + " WHERE st_concessao = " + ConcessaoServices.ST_ATIVO
					+ " AND tp_concessao = " + tpConcessao + ")";

			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));

			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			rsm.next();

			while (rsmConcessoes.next()) {

				sql = "SELECT count(*) as qtd_ait, sum(vl_multa) as vl_multa FROM mob_ait_transporte "
						+ " WHERE cd_concessao = " + rsmConcessoes.getInt("CD_CONCESSAO") + " AND st_ait <> " + ST_CANCELADA
						+ " AND dt_infracao >= ? AND dt_infracao <= ?";

				pstmt = connect.prepareStatement(sql);
				pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
				pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
				ResultSetMap rsmAits = new ResultSetMap(pstmt.executeQuery());

				if (rsmAits.next()) {
					rsmConcessoes.setValueToField("QTD_AITS", rsmAits.getInt("QTD_AIT"));
					rsmConcessoes.setValueToField("VL_MULTAS", rsmAits.getDouble("VL_MULTA"));

					rsmConcessoes.setValueToField("TOTAL_AITS", rsm.getInt("TOTAL_AIT"));
					rsmConcessoes.setValueToField("TOTAL_MULTAS", rsm.getDouble("TOTAL_MULTA"));
				}
			}

			return rsmConcessoes;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.getTotalAitsPorEmpresa: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * Para montar os graficos - retorna a quantidade de aitas validas de acordo a
	 * situacao, dentro um periodo de tempo
	 */
	public static ResultSetMap getTotalAitsSituacao(GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int tpConcessao) {
		return getTotalAitsSituacao(dtInicial, dtFinal, tpConcessao, null);
	}

	public static ResultSetMap getTotalAitsSituacao(GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int tpConcessao, Connection connect) {

		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {

			String sql = "SELECT count(*) AS qtd_ait, st_ait FROM mob_ait_transporte, mob_concessao WHERE tp_concessao = "
					+ tpConcessao + " AND st_ait <> " + ST_CANCELADA + " AND dt_infracao >= ? AND dt_infracao <= ? "
					+ " GROUP BY st_ait ORDER BY st_ait";

			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
			ResultSetMap rsmAits = new ResultSetMap(pstmt.executeQuery());

			ResultSetMap rsm = new ResultSetMap();

			while (rsmAits.next()) {
				if (rsmAits.getInt("QTD_AIT") > 0) {
					rsmAits.setValueToField("NM_ST_AIT", situacaoAit[rsmAits.getInt("ST_AIT")]);
					rsm.addRegister(rsmAits.getRegister());
				}
			}

			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.getTotalAitsSituacao: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getQtdAitsPorPeriodo(GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int tpConcessao, int cdInfracao, int cdAgente, int cdConcessao) {
		return getQtdAitsPorPeriodo(dtInicial, dtFinal, tpConcessao, cdInfracao, cdAgente, cdConcessao, null);
	}

	public static ResultSetMap getQtdAitsPorPeriodo(GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int tpConcessao, int cdInfracao, int cdAgente, int cdConcessao, Connection connect) {

		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {

			int dias = Util.getQuantidadeDias(dtInicial, dtFinal);
			String filtro = "date(dt_infracao)";

			if (dias > 31)
				filtro = "date_part('month', dt_infracao)";
			if (dias > 365)
				filtro = "date_part('year', dt_infracao)";

			String sql = "SELECT count(*) AS qtd_ait, " + filtro + " as dt" + " FROM mob_ait_transporte A"
					+ " LEFT OUTER JOIN mob_concessao B ON (A.cd_concessao =  B.cd_concessao)" + " WHERE B.tp_concessao = ?"
					+ " AND st_ait <> " + ST_CANCELADA + " AND dt_infracao >= ? AND dt_infracao <= ? "
					+ (cdAgente > 0 ? " AND cd_agente = " + cdAgente : "")
					+ (cdInfracao > 0 ? " AND cd_infracao = " + cdInfracao : "")
					+ (cdConcessao > 0 ? " AND A.cd_concessao = " + cdConcessao : "") + " GROUP BY dt ORDER BY dt";

			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, tpConcessao);
			pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
			pstmt.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));
			ResultSetMap rsmAits = new ResultSetMap(pstmt.executeQuery());

			ResultSetMap rsm = new ResultSetMap();

			while (rsmAits.next()) {

				switch (filtro) {
				case "date_part('month', dt_infracao)":
					int mes = (int) rsmAits.getDouble("dt");
					rsmAits.setValueToField("LABEL", Util.meses[--mes]);
					break;

				case "date_part('year', dt_infracao)":
					rsmAits.setValueToField("LABEL", (int) rsmAits.getDouble("dt"));
					break;

				default:
					String data = rsmAits.getDateFormat("DT", "dd/MM");
					rsmAits.setValueToField("LABEL", data);
					break;
				}

				rsm.addRegister(rsmAits.getRegister());
			}

			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.getQtdAitsPorPeriodo: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getQtdAitsPorLinha(GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int tpConcessao, int cdInfracao, int cdAgente, int cdConcessao) {
		return getQtdAitsPorLinha(dtInicial, dtFinal, tpConcessao, cdInfracao, cdAgente, cdConcessao, null);
	}

	public static ResultSetMap getQtdAitsPorLinha(GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int tpConcessao, int cdInfracao, int cdAgente, int cdConcessao, Connection connect) {

		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {

			String sql = "SELECT count(*) AS qtd_ait, C.cd_linha, C.nr_linha " + " FROM mob_ait_transporte A"
					+ " LEFT OUTER JOIN mob_concessao B ON (A.cd_concessao =  B.cd_concessao)"
					+ " LEFT OUTER JOIN mob_linha C ON (A.cd_linha =  C.cd_linha)" + " WHERE B.tp_concessao = ?"
					+ (cdAgente > 0 ? " AND A.cd_agente = " + cdAgente : "")
					+ (cdInfracao > 0 ? " AND A.cd_infracao = " + cdInfracao : "")
					+ (cdConcessao > 0 ? " AND A.cd_concessao = " + cdConcessao : "") + " AND A.st_ait <> " + ST_CANCELADA
					+ " AND A.dt_infracao>= ? AND A.dt_infracao <= ?" + " GROUP BY C.cd_linha ORDER BY C.nr_linha";

			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, tpConcessao);
			pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
			pstmt.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));
			ResultSetMap rsmAits = new ResultSetMap(pstmt.executeQuery());

			ResultSetMap rsm = new ResultSetMap();

			while (rsmAits.next()) {
				rsmAits.setValueToField("LABEL", rsmAits.getString("NR_LINHA"));
				rsm.addRegister(rsmAits.getRegister());
			}

			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.getQtdAitsPorLinha: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getQtdAitsPorAgente(GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int tpConcessao, int cdInfracao, int cdAgente, int cdConcessao) {
		return getQtdAitsPorAgente(dtInicial, dtFinal, tpConcessao, cdInfracao, cdAgente, cdConcessao, null);
	}

	public static ResultSetMap getQtdAitsPorAgente(GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int tpConcessao, int cdInfracao, int cdAgente, int cdConcessao, Connection connect) {

		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {

			String sql = "SELECT count(*) AS qtd_ait, C.cd_agente, C.nm_agente " + " FROM mob_ait_transporte A"
					+ " LEFT OUTER JOIN mob_concessao B ON (A.cd_concessao =  B.cd_concessao)"
					+ " LEFT OUTER JOIN mob_agente C ON (A.cd_agente =  C.cd_agente)" + " WHERE B.tp_concessao = ?"
					+ (cdAgente > 0 ? " AND A.cd_agente = " + cdAgente : "")
					+ (cdInfracao > 0 ? " AND A.cd_infracao = " + cdInfracao : "")
					+ (cdConcessao > 0 ? " AND A.cd_concessao = " + cdConcessao : "") + " AND A.st_ait <> " + ST_CANCELADA
					+ " AND A.dt_infracao>= ? AND A.dt_infracao <= ?" + " GROUP BY C.cd_agente ORDER BY C.nm_agente";

			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, tpConcessao);
			pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
			pstmt.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));
			ResultSetMap rsmAits = new ResultSetMap(pstmt.executeQuery());

			ResultSetMap rsm = new ResultSetMap();

			while (rsmAits.next()) {
				rsmAits.setValueToField("LABEL", rsmAits.getString("NM_AGENTE"));
				rsm.addRegister(rsmAits.getRegister());
			}

			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.getQtdAitsPorAgente: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * Realiza uma validaï¿½ï¿½o para verificar se existe algum parï¿½metro inválido
	 * nas fases do Juris
	 * 
	 * @return Retorna um boolean indicando o resutlado da validaï¿½ï¿½o
	 */
	public static boolean verificarFasesJuris() {
		int parametroValidacao = ST_JURIS_JULGAMENTO_1 * ST_JURIS_DEFERIDO_1 * ST_JURIS_INDEFERIDO_1
				* ST_JURIS_DEFERIMENTO_NOTIFICADO_1 * ST_JURIS_INDEFERIMENTO_NOTIFICADO_1 * ST_JURIS_JULGAMENTO_2
				* ST_JURIS_DEFERIDO_2 * ST_JURIS_INDEFERIDO_2 * ST_JURIS_DEFERIMENTO_NOTIFICADO_2
				* ST_JURIS_INDEFERIMENTO_NOTIFICADO_2 * ST_JURIS_CANCELADO;

		return parametroValidacao >= 0 ? true : false;

	}

	@SuppressWarnings("static-access")
	public void testeImportacao() {
		DesktopImport desktopImport = new DesktopImport();

		@SuppressWarnings("unused")
		Result rs = desktopImport.importTeste();
		@SuppressWarnings("unused")
		int n = 10;
	}

	/**
	 * Sicroniza a AIT Transporte de Talão Eletrï¿½nico no banco do Postgres.
	 * 
	 * @return Retorna um boolean indicando o resutlado do insert
	 */
	public static int inserirAitTalonarioEletronico(AitTransporte aitTransporte, Connection connect) {

		if (aitTransporte.getStAit() != 2) {
			// Salva a Ait Transporte com o status de lavarada
			aitTransporte.setStAit(AitTransporteServices.ST_LAVRADA);

			// Calcula o data Limite da infraï¿½ï¿½o usando como base o parametro de
			// quantidade de dias para emissï¿½o
			GregorianCalendar dataLimiteEmissao = DateUtil.addUtilsDays(aitTransporte.getDtInfracao(),
					PRAZO_NOTIFICACAO_INICIAL);
			aitTransporte.setDtLimiteEmissao(dataLimiteEmissao);
		} else {
			// Calcula o data Limite da infraï¿½ï¿½o usando como base o parametro de
			// quantidade de dias para emissï¿½o
			GregorianCalendar dataLimiteEmissao = DateUtil.addUtilsDays(aitTransporte.getDtInfracao(),
					PRAZO_NOTIFICACAO_INICIAL);
			aitTransporte.setDtLimiteEmissao(dataLimiteEmissao);

			// Calcula o data Limite da infraï¿½ï¿½o usando como base o parametro de
			// quantidade de dias para emissï¿½o
			GregorianCalendar dataInterporRecurso = DateUtil.addUtilsDays(aitTransporte.getDtInfracao(),
					PRAZO_INTERPOR_RECURSO);
			aitTransporte.setDtLimiteRecurso1(dataInterporRecurso);

		}

		return AitTransporteDAO.insert(aitTransporte, connect);
	}

	public static ResultSetMap findComRecurso(ArrayList<ItemComparator> criterios) {
		return findComRecurso(criterios, null);
	}

	public static ResultSetMap findComRecurso(ArrayList<ItemComparator> criterios, Connection connect) {
		try {
			ResultSetMap rsmAits = new ResultSetMap();
			ResultSetMap rsmRecursos = new ResultSetMap();
			rsmAits = find(criterios);
			while (rsmAits.next()) {
				rsmRecursos = carregarRecursos(rsmAits.getInt("cd_ait"));
				if (rsmRecursos.next()) {
					HashMap<String, Object> recursos = new HashMap<String, Object>();

					recursos.put("rsmRecursos", rsmRecursos.getRegister());
					rsmAits.addRegister(recursos);
				}
			}

			return rsmAits;

		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.findComRecurso: " + e);
			return null;
		}

	}

	public static Result saveRecurso(int cdAit, Documento documento, ArrayList<DocumentoPessoa> solicitantes,
			ArrayList<DocumentoOcorrencia> ocorrencias, GregorianCalendar dtJulgamento, GregorianCalendar dtNotificacao) {
		return saveRecurso(cdAit, documento, solicitantes, ocorrencias, dtJulgamento, dtNotificacao, null);
	}

	public static Result saveRecurso(int cdAit, Documento documento, ArrayList<DocumentoPessoa> solicitantes,
			ArrayList<DocumentoOcorrencia> ocorrencias, GregorianCalendar dtJulgamento, GregorianCalendar dtNotificacao,
			Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			// AIT
			AitTransporte ait = AitTransporteDAO.get(cdAit, connect);
			if (ait == null) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-2, "Erro ao salvar recurso.");
			}

			// VALIDAï¿½ï¿½ES
			if (ait.getDtRecebimento() == null) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-4, "O recurso não pode ser lanï¿½ado sem que o AIT tenha data de recebimento.");
			}

			if (Util.compareDates(ait.getDtRecebimento(), documento.getDtProtocolo()) > 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-3, "A data do recurso não pode ser anterior ao recebimento.");
			}

			if (documento.getCdDocumentoSuperior() > 0) {
				Documento docAnterior = DocumentoDAO.get(documento.getCdDocumentoSuperior(), connect);
				if (Util.compareDates(docAnterior.getDtProtocolo(), documento.getDtProtocolo()) > 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-3, "A data do recurso não pode ser anterior a data do primeiro recurso.");
				}
			}

			// salva o recurso (documento)
			Result result = DocumentoServices.save(documento, solicitantes, ocorrencias, connect);
			if (result.getCode() < 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return result;
			}
			documento = (Documento) result.getObjects().get("DOCUMENTO");

			boolean lgAitCancelada = false;
			if (ait.getStAit() == ST_CANCELADA)
				lgAitCancelada = true;

			// atualiza dados da AIT
			if (documento.getCdDocumentoSuperior() <= 0) {
				ait.setCdRecurso1(documento.getCdDocumento());
				ait.setDtJulgamento1(dtJulgamento);
				ait.setDtNotificacao1(dtNotificacao);

				if (documento.getCdFase() == ST_JURIS_JULGAMENTO_1) {
					ait.setStAit(ST_EM_JULGAMENTO_1);
					ait.setDtLimiteAnalisarRecurso1(DateUtil.addUtilsDays(documento.getDtProtocolo(),
							ParametroServices.getValorOfParametroAsInteger("QT_DIAS_PRAZO_ANALISAR_RECURSO1", 0)));
				} else if (documento.getCdFase() == ST_JURIS_DEFERIDO_1) {
					ait.setStAit(ST_DEFERIDO_1);
					ait.setDtLimiteNotificarRecurso1(DateUtil.addUtilsDays(ait.getDtJulgamento1(),
							ParametroServices.getValorOfParametroAsInteger("QT_DIAS_PRAZO_NOTIFICAR_RECURSO1", 0)));
				} else if (documento.getCdFase() == ST_JURIS_INDEFERIDO_1) {
					ait.setStAit(ST_INDEFERIDO_1);
					ait.setDtLimiteNotificarRecurso1(DateUtil.addUtilsDays(ait.getDtJulgamento1(),
							ParametroServices.getValorOfParametroAsInteger("QT_DIAS_PRAZO_NOTIFICAR_RECURSO1", 0)));
				} else if (documento.getCdFase() == ST_JURIS_DEFERIMENTO_NOTIFICADO_1) {
					ait.setStAit(ST_NOTIFICADO_DEFERIMENTO_1);
				} else if (documento.getCdFase() == ST_JURIS_INDEFERIMENTO_NOTIFICADO_1) {
					ait.setStAit(ST_NOTIFICADO_INDEFERIMENTO_1);
					ait.setDtLimiteRecurso2(DateUtil.addUtilsDays(ait.getDtNotificacao1(),
							ParametroServices.getValorOfParametroAsInteger("QT_DIAS_PRAZO_INTERPOR_INSTANCIA2", 0)));
				}

			} else {
				ait.setCdRecurso2(documento.getCdDocumento());
				ait.setDtJulgamento2(dtJulgamento);
				ait.setDtNotificacao2(dtNotificacao);

				if (documento.getCdFase() == ST_JURIS_JULGAMENTO_2) {
					ait.setStAit(ST_EM_JULGAMENTO_2);
					ait.setDtLimiteAnalisarRecurso2(DateUtil.addUtilsDays(documento.getDtProtocolo(),
							ParametroServices.getValorOfParametroAsInteger("QT_DIAS_PRAZO_JULGAR_INSTANCIA2", 0)));
				} else if (documento.getCdFase() == ST_JURIS_DEFERIDO_2) {
					ait.setStAit(ST_DEFERIDO_2);
					ait.setDtLimiteNotificarRecurso2(DateUtil.addUtilsDays(ait.getDtJulgamento2(),
							ParametroServices.getValorOfParametroAsInteger("QT_DIAS_PRAZO_NOTIFICAR_INSTANCIA2", 0)));
				} else if (documento.getCdFase() == ST_JURIS_INDEFERIDO_2) {
					ait.setStAit(ST_INDEFERIDO_2);
					ait.setDtLimiteNotificarRecurso2(DateUtil.addUtilsDays(ait.getDtJulgamento2(),
							ParametroServices.getValorOfParametroAsInteger("QT_DIAS_PRAZO_NOTIFICAR_INSTANCIA2", 0)));
				} else if (documento.getCdFase() == ST_JURIS_DEFERIMENTO_NOTIFICADO_2) {
					ait.setStAit(ST_NOTIFICADO_DEFERIMENTO_2);
				} else if (documento.getCdFase() == ST_JURIS_INDEFERIMENTO_NOTIFICADO_2) {
					ait.setStAit(ST_NOTIFICADO_INDEFERIEMNTO_2);
				} else if (documento.getCdFase() == ST_JURIS_CANCELADO) {
					ait.setStAit(ST_CANCELADA);
				}
			}

			if (lgAitCancelada)
				ait.setStAit(ST_CANCELADA);

			if (ait.getCdTalao() == 0) {
				return new Result(-5, "AIT sem Talão associado.");
			}

			int tpTalao = TalonarioAITDAO.get(ait.getCdTalao()).getTpTalao();

			result = save(ait, connect, tpTalao);
			if (result.getCode() < 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return result;
			}

			if (isConnectionNull)
				connect.commit();

			result = new Result(1, "Recurso salvo com sucesso!");
			result.addObject("DOCUMENTO", documento);
			result.addObject("AIT", ait);
			return result;
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

	public static Result removeRecurso(int cdAit, int cdDocumento, int nrRecurso) {
		return removeRecurso(cdAit, cdDocumento, nrRecurso, null);
	}

	public static Result removeRecurso(int cdAit, int cdDocumento, int nrRecurso, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			// AIT
			AitTransporte ait = AitTransporteDAO.get(cdAit, connect);
			if (ait == null) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-2, "Erro ao salvar recurso.");
			}

			// exclui o recurso (documento)
			Result result = DocumentoServices.remove(cdDocumento, true, connect);
			if (result.getCode() < 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return result;
			}

			// atualiza dados da AIT
			if (nrRecurso == 1) {
				ait.setCdRecurso1(0);
				ait.setDtJulgamento1(null);
				ait.setDtNotificacao1(null);
			} else if (nrRecurso == 2) {
				ait.setCdRecurso2(0);
				ait.setDtJulgamento2(null);
				ait.setDtNotificacao2(null);
			}
			result = save(ait, connect, TalonarioAITServices.TP_TALONARIO_ELETRONICO_TRANSPORTE);
			if (result.getCode() < 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return result;
			}

			if (isConnectionNull)
				connect.commit();

			return new Result(1, "Recurso salvo com sucesso!");
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

	public static Result getSyncData(ArrayList<AitTransporte> aitTransporte, ArrayList<AitTransporteImagem> aitImagens) {
		return getSyncData(aitTransporte, aitImagens, null);
	}

	public static Result getSyncData(ArrayList<AitTransporte> aitTransporte, ArrayList<AitTransporteImagem> aitImagens,
			Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			List<AitTransporteImagem> imagens = new ArrayList<AitTransporteImagem>();

			if (aitTransporte != null && aitTransporte.size() > 0) {
				for (AitTransporte aT : aitTransporte) {

					Talonario talao = TalonarioDAO.get(aT.getCdTalao(), connect);

					imagens.clear();

					if (aitImagens != null) {
						imagens = aitImagens.stream().filter(img -> img.getCdAit() == aT.getCdAit())
								.collect(Collectors.toList());
					}

					aT.setCdAit(0);
					Result save = save(aT, connect, talao.getTpTalao());

					if (save.getCode() <= 0) {
						throw new Exception(save.getMessage());
					}

					for (AitTransporteImagem img : imagens) {
						img.setCdAit(aT.getCdAit());
						img.setCdImagem(0);
						save = AitTransporteImagemServices.save(img, connect);

						if (save.getCode() <= 0) {
							throw new Exception(save.getMessage());
						}
					}

					System.out.println("[e-Transporte App] Sincronização completada: cdAit: " + aT.getCdAit()
							+ " - Imagens: " + imagens.size());
				}
			}

			connect.commit();

			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("AitTransporte", new ArrayList<AitTransporte>());

			return new Result(1, "Sincronização concluída com sucesso!", "RESULT", register);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
