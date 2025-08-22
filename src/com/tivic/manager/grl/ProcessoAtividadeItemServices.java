package com.tivic.manager.grl;

import java.sql.Connection;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.agd.Agendamento;
import com.tivic.manager.agd.AgendamentoDAO;
import com.tivic.manager.agd.AgendamentoServices;
import com.tivic.sol.connection.Conexao;



public class ProcessoAtividadeItemServices {

	public static HashMap<String, Object> insert(ProcessoAtividadeItem atividadeItem, Agendamento agendamento, int cdUsuarioCriador,
			int cdUsuarioResponsavel) {
		return insert(atividadeItem, agendamento, cdUsuarioCriador, cdUsuarioResponsavel, null);
	}

	public static HashMap<String, Object> insert(ProcessoAtividadeItem atividadeItem, Agendamento agendamento, 
			int cdUsuarioCriador, int cdUsuarioResponsavel, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
					
			/* inclusao de agendamento (titulo definido a partir da atividade/acao indicada) */
			ProcessoAtividade atividade = ProcessoAtividadeDAO.get(atividadeItem.getCdAtividade(), atividadeItem.getCdProcesso(), connection);
			ProcessoItem processo = ProcessoItemDAO.get(atividadeItem.getCdProcessoItem(), connection);
			String nmAgendamento = atividade.getNmAtividade() + " (Proc. " + processo.getNrProcesso() + ")";
			agendamento.setNmAgendamento(nmAgendamento);
			/* tipo de agendamento padrao */
			agendamento.setCdTipoAgendamento(ParametroServices.getValorOfParametroAsInteger("CD_TIPO_AGENDAMENTO_DEFAULT", 0, 0, connection));
			HashMap<String, Object> hash = AgendamentoServices.insert(agendamento, 0 /*cdTipoLembrete*/, cdUsuarioCriador, cdUsuarioResponsavel, 
					null /*recorrencia*/, connection);
			if (hash==null) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}
			agendamento = (Agendamento)hash.get("agendamentoDefault");
			
			/* inclusao de atividade */
			atividadeItem.setCdAgendamento(agendamento.getCdAgendamento());
			atividadeItem.setDtLancamento(new GregorianCalendar());
			atividadeItem.setDtRecebimento(null);
			int cdAtividadeItem = 0;
			if ((cdAtividadeItem = ProcessoAtividadeItemDAO.insert(atividadeItem, connection)) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}
			atividadeItem.setCdAtividadeItem(cdAtividadeItem);
			
			if (isConnectionNull)
				connection.commit();
			
			hash.clear();
			hash.put("atividade", atividadeItem);
			hash.put("agendamento", agendamento);
			
			if (isConnectionNull)
				connection.commit();
			
			return hash;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeItemServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static HashMap<String, Object> update(ProcessoAtividadeItem atividadeItem, Agendamento agendamento,
			int cdUsuarioResponsavel) {
		return update(atividadeItem, agendamento, cdUsuarioResponsavel, null);
	}

	public static HashMap<String, Object> update(ProcessoAtividadeItem atividadeItem, Agendamento agendamento, 
			int cdUsuarioResponsavel, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
						
			/* atualizacao de agendamento */
			Agendamento agendTemp = AgendamentoDAO.get(agendamento.getCdAgendamento(), connection);
			agendTemp.setTxtAgendamento(agendamento.getTxtAgendamento());
			agendamento = agendTemp;
			HashMap<String, Object> hash = AgendamentoServices.update(agendamento, 0 /*cdTipoLembrete*/, cdUsuarioResponsavel, 
					null /*recorrencia*/, connection);
			if (hash==null) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}
			agendamento = (Agendamento)hash.get("agendamentoDefault");
			
			ProcessoAtividadeItem atividadeOld = ProcessoAtividadeItemDAO.get(atividadeItem.getCdProcessoItem(), atividadeItem.getCdAtividadeItem(), 
					connection);
			atividadeItem.setDtRecebimento(atividadeOld.getDtRecebimento());
			/* atualizacao de atividade */
			if (ProcessoAtividadeItemDAO.update(atividadeItem, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}
			
			if (isConnectionNull)
				connection.commit();
			
			hash.clear();
			hash.put("atividade", atividadeItem);
			hash.put("agendamento", agendamento);
			
			if (isConnectionNull)
				connection.commit();
			
			return hash;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeItemServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static int delete(int cdProcessoItem, int cdAtividadeItem) {
		return delete(cdProcessoItem, cdAtividadeItem, null);
	}

	public static int delete(int cdProcessoItem, int cdAtividadeItem, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
				
			ProcessoAtividadeItem atividade = ProcessoAtividadeItemDAO.get(cdProcessoItem, cdAtividadeItem, connection);
			int cdAgendamento = atividade.getCdAgendamento();
			
			if (ProcessoAtividadeItemDAO.delete(cdProcessoItem, cdAtividadeItem, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}
			
			if (cdAgendamento>0 && AgendamentoServices.delete(cdAgendamento, connection)<=0) {
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
			System.err.println("Erro! ProcessoAtividadeItemServices.insert: " +  e);
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
