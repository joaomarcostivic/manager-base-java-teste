package com.tivic.manager.agd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class AssuntoServices {

	/* tipos de prioridade */
	public static final int PRIOR_NORMAL = 0;
	public static final int PRIOR_URGENTE = 1;
	public static final int PRIOR_URGENTISSIMO = 2;

	public static final String[] tiposPrioridade = {"Normal",
		"Urgente",
		"Urgentíssimo"};

	/* tipos de assunto */
	public static final int TP_NORMAL = 0;
	public static final int TP_REPETITIVO = 1;

	public static final String[] tipos = {"Normal",
		"Repetitivo"};

	public static Assunto insert(Assunto assunto) {
		return insert(assunto, null);
	}

	public static Assunto insert(Assunto assunto, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			assunto.setDtCadastro(new GregorianCalendar());
			int cdAssunto = AssuntoDAO.insert(assunto, connection);
			if (cdAssunto <= 0)
				return null;

			assunto.setCdAssunto(cdAssunto);
			return assunto;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AssuntoServices.insert: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllOcorrencias(int cdAssunto) {
		return getAllOcorrencias(cdAssunto, null, null);
	}

	public static ResultSetMap getAllOcorrencias(int cdAssunto, GregorianCalendar dtAnterior, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.dt_inicial, B.id_agendamento, B.nr_recorrencia, " +
					"C.nr_documento " +
					"FROM agd_agendamento_assunto A, agd_agendamento B, ptc_documento C " +
					"WHERE A.cd_agendamento = B.cd_agendamento " +
					"  AND B.cd_documento = C.cd_documento " +
					"  AND A.cd_assunto = ? " +
					"  AND A.st_final <> ? " +
					(dtAnterior!=null ? "  AND B.dt_inicial < ? " : ""));
			pstmt.setInt(1, cdAssunto);
			pstmt.setInt(2, AgendamentoAssuntoServices.ST_NAO_DISCUTIDO);
			if (dtAnterior!=null)
				pstmt.setTimestamp(3, Util.convCalendarToTimestamp(dtAnterior));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while (rsm.next()) {
				rsm.getRegister().put("NR_AGENDAMENTO", rsm.getString("ID_AGENDAMENTO") + " - " + rsm.getInt("nr_recorrencia"));
			}
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("DT_INICIAL");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			return rsm;
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

	public static ResultSetMap find(int cdEmpresa) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("E.CD_EMPRESA", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios, null);
	}

	public static ResultSetMap getAsResultSet(int cdAssunto) {
		return getAsResultSet(cdAssunto, null);
	}

	public static ResultSetMap getAsResultSet(int cdAssunto, Connection connection) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_assunto", Integer.toString(cdAssunto), ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios, connection);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		int cdUsuario = 0;
		int stFinal = -1;
		for (int i=0; criterios!=null && i<criterios.size(); i++)
			if (criterios.get(i).getColumn().equalsIgnoreCase("cdUsuario")) {
				cdUsuario = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i--);
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("D.st_final")) {
				stFinal = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i--);
			}
		Usuario usuario = cdUsuario<=0 ? null : UsuarioDAO.get(cdUsuario, connect);
		int cdPessoa = usuario==null ? 0 : usuario.getCdPessoa();
		return Search.find("SELECT A.*, B.txt_fechamento, C.nm_pessoa AS nm_autor, D.st_final, D.tp_prioridade, D.dt_previsao_conclusao, " +
				"D.cd_agendamento " +
				"FROM agd_assunto A " +
				"LEFT OUTER JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa) " +
				"LEFT OUTER JOIN agd_agenda E ON (A.cd_agenda = E.cd_agenda) " +
				"LEFT OUTER JOIN agd_agendamento_assunto D ON (A.cd_assunto = D.cd_assunto AND " +
				"											   D.cd_agendamento = (SELECT MAX(F.cd_agendamento) " +
				"																   FROM agd_agendamento_assunto E, agd_agendamento F " +
				"																   WHERE E.cd_assunto = A.cd_assunto " +
				"																	 AND E.cd_agendamento = F.cd_agendamento " +
				"																	 AND F.dt_inicial = (SELECT MAX(G.dt_inicial) " +
				"																						 FROM agd_agendamento G, agd_agendamento_assunto H " +
				"																						 WHERE G.cd_agendamento = H.cd_agendamento " +
				"																						   AND H.cd_assunto = A.cd_assunto " +
				"																						   AND H.st_final <> " + AgendamentoAssuntoServices.ST_NAO_DISCUTIDO + "))) " +
				"LEFT OUTER JOIN agd_agendamento_assunto B ON (A.cd_assunto = B.cd_assunto AND " +
				"											   B.st_final = " + AgendamentoAssuntoServices.ST_FECHADO + ") " +
				"WHERE 1=1 " +
				(stFinal!=-1 ? " AND (D.st_final = " + stFinal +
						(stFinal==AgendamentoAssuntoServices.ST_EM_ABERTO ? " OR NOT EXISTS (SELECT * " +
								"															 FROM agd_agendamento_assunto " +
								"															 WHERE cd_assunto = A.cd_assunto) " : "") + ") " : "") +
				(cdPessoa>0 ? " AND (A.cd_agenda IS NULL OR A.cd_agenda IN (SELECT cd_agenda " +
				"					   FROM agd_agenda_participante " +
				"					   WHERE cd_participante = " + cdPessoa + " " +
				"						 AND st_participante = " + AgendaParticipanteServices.ST_ATIVO + ")) " : ""),
				"", criterios, null, connect!=null ? connect : Conexao.conectar(), connect==null, false, false);
	}

	public static int update(Assunto objeto){
		return update(objeto, null);
	}

	public static int update(Assunto objeto, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			PreparedStatement pstmt = connection.prepareStatement("SELECT A.* " +
					"FROM agd_agendamento_assunto A, agd_agendamento B " +
					"WHERE A.cd_agendamento = B.cd_agendamento " +
					"  AND A.cd_assunto = ? " +
					"  AND A.st_final <> ? " +
					"ORDER BY B.dt_inicial DESC ");
			pstmt.setInt(1, objeto.getCdAssunto());
			pstmt.setInt(2, AgendamentoAssuntoServices.ST_NAO_DISCUTIDO);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Assunto assuntoOld = AssuntoDAO.get(objeto.getCdAssunto(), connection);
				objeto.setTpPrioridadeInicial(assuntoOld.getTpPrioridadeInicial());
			}

			return AssuntoDAO.update(objeto, connection);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AssuntoServices.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllArquivos(int cdAssunto) {
		return getAllArquivos(cdAssunto, null);
	}

	public static ResultSetMap getAllArquivos(int cdAssunto, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT B.*, A.cd_usuario, " +
					"C.nm_tipo_arquivo " +
					"FROM AGD_ASSUNTO_ARQUIVO A " +
					"LEFT OUTER JOIN grl_arquivo B ON (A.cd_arquivo = B.cd_arquivo) " +
					"LEFT OUTER JOIN grl_tipo_arquivo C ON (B.cd_tipo_arquivo = C.cd_tipo_arquivo) " +
					"WHERE A.cd_assunto = ?");
			pstmt.setInt(1, cdAssunto);
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
