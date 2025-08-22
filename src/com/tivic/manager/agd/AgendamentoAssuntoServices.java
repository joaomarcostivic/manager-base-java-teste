package com.tivic.manager.agd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.util.Result;

public class AgendamentoAssuntoServices {

	/* tipos de prioridade */
	public static final int PRIOR_NORMAL = 0;
	public static final int PRIOR_URGENTE = 1;
	public static final int PRIOR_URGENTISSIMO = 2;

	public static final String[] tiposPrioridade = {"Normal",
		"Urgente",
		"Urgentíssimo"};

	/* situacoes */
	public static final int ST_NAO_DISCUTIDO = -1;
	public static final int ST_EM_ABERTO = 0;
	public static final int ST_AGUARDE_TERCEIROS = 1;
	public static final int ST_FECHADO = 2;

	public static final String[] situacoes = {"Em aberto/a resolver",
		"Aguardo de retorno de terceiros",
		"Solucionado/fechado"};

	public static Result setFechamentoAssunto(AgendamentoAssunto fechamentoAssunto, ArrayList<HashMap<String, Object>> posicoesContrarias) {
		return setFechamentoAssunto(fechamentoAssunto, posicoesContrarias, null);
	}

	public static Result setFechamentoAssunto(AgendamentoAssunto fechamentoAssunto, ArrayList<HashMap<String, Object>> posicoesContrarias,
			Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			/* atualiza assunto */
			AgendamentoAssunto assuntoOld = AgendamentoAssuntoDAO.get(fechamentoAssunto.getCdAssunto(), fechamentoAssunto.getCdAgendamento(),
					connection);
			fechamentoAssunto.setNrOrdem(assuntoOld.getNrOrdem());
			if (AgendamentoAssuntoDAO.update(fechamentoAssunto, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erros reportados ao registrar fechamento de tema. Entre em contato com o suporte técnico.");
			}

			Agendamento agendamento = AgendamentoDAO.get(fechamentoAssunto.getCdAgendamento(), connection);
			int cdAgenda = agendamento==null ? 0 : agendamento.getCdAgenda();

			/* atualiazacoes de posicoes contrarias ao fechamento do assunto */
			if (posicoesContrarias != null) {
				PreparedStatement pstmt = connection.prepareStatement("DELETE " +
						"FROM agd_assunto_posicionamento " +
						"WHERE cd_agendamento = ? " +
						"  AND cd_assunto = ? ");
				pstmt.setInt(1, fechamentoAssunto.getCdAgendamento());
				pstmt.setInt(2, fechamentoAssunto.getCdAssunto());
				pstmt.execute();

				for (int i=0; i<posicoesContrarias.size(); i++) {
					int cdParticipante = posicoesContrarias.get(i).get("cdParticipante")==null ? 0 : ((Integer)(posicoesContrarias.get(i).get("cdParticipante"))).intValue();
					int cdConvidado = posicoesContrarias.get(i).get("cdConvidado")==null ? 0 : ((Integer)(posicoesContrarias.get(i).get("cdConvidado"))).intValue();
					if (AssuntoPosicionamentoDAO.insert(new AssuntoPosicionamento(fechamentoAssunto.getCdAgendamento(),
							fechamentoAssunto.getCdAssunto(),
							0 /*cdPosicionamento*/,
							"" /*txtObservacao*/,
							1 /*lgContratio*/,
							cdAgenda,
							cdParticipante,
							cdConvidado), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erros registrados ao registar posicionamento contrário de participante da reunião. Entre " +
								"em contato com o suporte técnico.");
					}
				}
			}

			if (isConnectionNull)
				connection.commit();

			return new Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoAssuntoServices.setFechamentoAssunto: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erros reportados ao registrar fechamento de tema. Anote a mensagem de erro abaixo e entre em " +
					"contato com o suporte técnico.\n" +
					e.getLocalizedMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getPosicionamentos(int cdAgendamento, int cdAssunto) {
		return getPosicionamentos(cdAgendamento, cdAssunto, null);
	}

	public static ResultSetMap getPosicionamentos(int cdAgendamento, int cdAssunto, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			PreparedStatement pstmt = connection.prepareStatement("SELECT C.nm_pessoa AS nm_participante, A.cd_participante, " +
					"0 AS cd_convidado, B.txt_observacao, B.lg_contrario " +
					"FROM agd_agendamento_participante A " +
					"LEFT OUTER JOIN grl_pessoa C ON (A.cd_participante = C.cd_pessoa) " +
					"LEFT OUTER JOIN agd_assunto_posicionamento B ON (A.cd_agendamento = B.cd_agendamento AND " +
					"												  A.cd_participante = B.cd_participante AND " +
					"												  B.cd_assunto = ?) " +
					"WHERE A.cd_agendamento = ? " +
					"  AND A.lg_presenca = 1 " +
					"UNION " +
					"SELECT A.nm_convidado AS nm_participante, 0 AS cd_participante, A.cd_convidado, B.txt_observacao, B.lg_contrario " +
					"FROM agd_agendamento_convidado A " +
					"LEFT OUTER JOIN agd_assunto_posicionamento B ON (A.cd_agendamento = B.cd_agendamento AND " +
					"												  A.cd_convidado = B.cd_convidado AND " +
					"												  B.cd_assunto = ?) " +
					"WHERE A.cd_agendamento = ? " +
					"  AND A.lg_presenca = 1");
			pstmt.setInt(1, cdAssunto);
			pstmt.setInt(2, cdAgendamento);
			pstmt.setInt(3, cdAssunto);
			pstmt.setInt(4, cdAgendamento);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

}
