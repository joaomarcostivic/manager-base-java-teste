package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.tivic.manager.agd.AgendamentoUsuarioServices;
import com.tivic.sol.connection.Conexao;


import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class ProcessoItemServices {

	public static final int ST_ABERTO = 0;
	public static final int ST_CONCLUIDO = 1;
	public static final int ST_CANCELADO = 2;

	public static final String[] situacoes = {"Em aberto",
		"Concluído",
		"Cancelado"};

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.nm_pessoa AS nm_cliente, C.nm_processo, D.nm_pessoa AS nm_responsavel, " +
				"E.nm_produto_servico " +
				"FROM grl_processo_item A " +
				"LEFT OUTER JOIN grl_pessoa B ON (A.cd_cliente = B.cd_pessoa) " +
				"LEFT OUTER JOIN grl_processo C ON (A.cd_processo = C.cd_processo) " +
				"LEFT OUTER JOIN grl_pessoa D ON (A.cd_responsavel = D.cd_pessoa) " +
				"LEFT OUTER JOIN grl_produto_servico E ON (A.cd_produto_servico = E.cd_produto_servico) ",
				criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ProcessoItem insert(ProcessoItem objeto) {
		return insert(objeto, null);
	}

	public static ProcessoItem insert(ProcessoItem objeto, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			Processo processo = ProcessoDAO.get(objeto.getCdProcesso(), connection);
			String nrProcesso = "";
			switch(processo.getTpNumeracao()) {
				case ProcessoServices.NUM_INCREMENTAL_SEM_PREFIXO:
					processo.setNrUltimaNumeracao(processo.getNrUltimaNumeracao() + 1);
					nrProcesso = Integer.toString(processo.getNrUltimaNumeracao());
					break;
				case ProcessoServices.NUM_INCREMENTAL_COM_PREXIFO:
					processo.setNrUltimaNumeracao(processo.getNrUltimaNumeracao() + 1);
					nrProcesso = processo.getIdPrefixoNumeracao() + Integer.toString(processo.getNrUltimaNumeracao());
					break;
			}
			if (ProcessoDAO.update(processo, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			objeto.setNrProcesso(nrProcesso);
			objeto.setDtProcesso(new GregorianCalendar());
			int code = ProcessoItemDAO.insert(objeto, connection);
			objeto.setCdProcessoItem(code>0 ? code : 0);

			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
			}
			else if (isConnectionNull)
				connection.commit();

			return code<=0 ? null : objeto;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoItemServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllAtividades(int cdProcessoItem) {
		return getAllAtividades(cdProcessoItem, null);
	}

	public static ResultSetMap getAllAtividades(int cdProcessoItem, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.dt_inicial, B.nm_agendamento, B.st_agendamento, " +
					"B.lg_original, C.cd_usuario AS cd_usuario_delegador, D.cd_usuario AS cd_usuario_responsavel, B.txt_agendamento, " +
					"E.nm_login AS nm_login_delegador, F.nm_pessoa AS nm_delegador, G.nm_login AS nm_login_responsavel, " +
					"H.nm_pessoa AS nm_responsavel, L.nm_atividade, L.txt_atividade " +
					"FROM grl_processo_atividade_item A " +
					"JOIN grl_processo_atividade L ON (A.cd_atividade = L.cd_atividade AND A.cd_processo = L.cd_processo) " +
					"LEFT OUTER JOIN agd_agendamento B ON (A.cd_agendamento = B.cd_agendamento) " +
					"LEFT OUTER JOIN agd_agendamento_usuario C ON (B.cd_agendamento = C.cd_agendamento AND " +
					"												(C.tp_nivel_usuario = " + AgendamentoUsuarioServices.TP_CRIADOR_RESPONSAVEL + " OR C.tp_nivel_usuario = " + AgendamentoUsuarioServices.TP_CRIADOR + ")) " +
					"LEFT OUTER JOIN agd_agendamento_usuario D ON (B.cd_agendamento = D.cd_agendamento AND " +
					"													(D.tp_nivel_usuario = " + AgendamentoUsuarioServices.TP_CRIADOR_RESPONSAVEL + " OR D.tp_nivel_usuario = " + AgendamentoUsuarioServices.TP_RESPONSAVEL + ")) " +
					"LEFT OUTER JOIN seg_usuario E ON (C.cd_usuario = E.cd_usuario) " +
					"LEFT OUTER JOIN grl_pessoa F ON (E.cd_pessoa = F.cd_pessoa) " +
					"LEFT OUTER JOIN seg_usuario G ON (D.cd_usuario = G.cd_usuario) " +
					"LEFT OUTER JOIN grl_pessoa H ON (G.cd_pessoa = H.cd_pessoa) " +
					"WHERE A.cd_processo_item = ?");
			pstmt.setInt(1, cdProcessoItem);
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

	public static int delete(int cdProcessoItem){
		return delete(cdProcessoItem, null);
	}

	public static int delete(int cdProcessoItem, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_atividade_item " +
					"FROM grl_processo_atividade_item " +
					"WHERE cd_processo_item = ?");
			pstmt.setInt(1, cdProcessoItem);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (ProcessoAtividadeItemServices.delete(cdProcessoItem, rs.getInt("cd_atividade_item"), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			if (ProcessoItemDAO.delete(cdProcessoItem, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoItemServices.delete: " +  e);
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
