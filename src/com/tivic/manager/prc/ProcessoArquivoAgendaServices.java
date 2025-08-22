package com.tivic.manager.prc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.LogUtils;

public class ProcessoArquivoAgendaServices {
	public static Result save(ProcessoArquivoAgenda arquivo){
		return save(arquivo, null);
	}
	
	public static Result save(ProcessoArquivoAgenda arquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(arquivo==null)
				return new Result(-1, "Erro ao salvar. Dados enviados são nulos");
			
			int retorno;
			
			if(ProcessoArquivoAgendaDAO.get(arquivo.getCdAgendaItem(), arquivo.getCdArquivo(), arquivo.getCdProcesso(), connect) == null){
				retorno = ProcessoArquivoAgendaDAO.insert(arquivo, connect);
			}
			else {
				retorno = ProcessoArquivoAgendaDAO.update(arquivo, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PROCESSOARQUIVOAGENDA", arquivo);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdAgendaItem, int cdArquivo, int cdProcesso){
		return remove(cdAgendaItem, cdArquivo, cdProcesso, false, null);
	}
	
	public static Result remove(int cdAgendaItem, int cdArquivo, int cdProcesso, boolean cascade){
		return remove(cdAgendaItem, cdArquivo, cdProcesso, cascade, null);
	}
	
	public static Result remove(int cdAgendaItem, int cdArquivo, int cdProcesso, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
				
			if(!cascade || retorno>0)
				retorno = ProcessoArquivoAgendaDAO.delete(cdAgendaItem, cdArquivo, cdProcesso, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result removeByProcessoAgenda(int cdAgendaItem, int cdProcesso){
		return removeByProcessoAgenda(cdAgendaItem, cdProcesso, null);
	}
	
	public static Result removeByProcessoAgenda(int cdAgendaItem, int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PRC_PROCESSO_ARQUIVO_AGENDA WHERE CD_AGENDA_ITEM=? AND CD_PROCESSO=?");
			pstmt.setInt(1, cdAgendaItem);
			pstmt.setInt(2, cdProcesso);
			pstmt.executeUpdate();
			
			return new Result(1, "Arquivos excluídos com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByProcessoAgenda(int cdAgendaItem, int cdProcesso, boolean lgListAll) {
		return getAllByProcessoAgenda(cdAgendaItem, cdProcesso, lgListAll, false, null);
	}
	
	public static ResultSetMap getAllByProcessoAgenda(int cdAgendaItem, int cdProcesso, boolean lgListAll, boolean lgUsuarioAdm) {
		return getAllByProcessoAgenda(cdAgendaItem, cdProcesso, lgListAll, lgUsuarioAdm, null);
	}

	public static ResultSetMap getAllByProcessoAgenda(int cdAgendaItem, int cdProcesso, boolean lgListAll, boolean lgUsuarioAdm, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		
		LogUtils.debug("ProcessoArquivoAgendaServices.getAllByProcessoAgenda");
		LogUtils.createTimer("ARQUIVO_AGENDA");
		try {
			String sql = "SELECT A.cd_arquivo, A.cd_processo, A.cd_andamento, A.nm_arquivo, A.nm_documento, " +
					"A.dt_arquivamento, A.lg_comprimido, A.cd_tipo_documento, " +
					"B.dt_andamento, C.nm_tipo_andamento, D.nr_processo, E.nm_tipo_processo, F.nm_tipo_documento, " +
					" \' \' AS nm_cliente, \' \' AS nm_adverso, "+ (cdAgendaItem>0 ? " G.cd_agenda_item " : "-1 as cd_agenda_item ")+
	                "FROM prc_processo_arquivo A "+
	                (cdAgendaItem>0 ? ((lgListAll ?" LEFT OUTER":"") + " JOIN prc_processo_arquivo_agenda G ON (G.cd_processo = A.cd_processo " +
	                		"AND G.cd_arquivo = A.cd_arquivo " +
	                		"AND G.cd_agenda_item = "+ cdAgendaItem+") " ) : "") + 
	                "LEFT OUTER JOIN prc_processo_andamento B ON (A.cd_processo  = B.cd_processo "+
	                "                                         AND A.cd_andamento = B.cd_andamento"+
	                					   (!lgUsuarioAdm ? " AND B.tp_visibilidade <> " + ProcessoAndamentoServices.TP_VISIBILIDADE_CONFIDENCIAL : "")+") "+
	                "LEFT OUTER JOIN prc_tipo_andamento     C ON (B.cd_tipo_andamento  = C.cd_tipo_andamento) "+
	                "LEFT OUTER JOIN prc_processo           D ON (A.cd_processo = D.cd_processo) "+
	                "LEFT OUTER JOIN prc_tipo_processo      E ON (D.cd_tipo_processo = E.cd_tipo_processo) "+
	                "LEFT OUTER JOIN gpn_tipo_documento     F ON (A.cd_tipo_documento = F.cd_tipo_documento) "+
                    "WHERE A.cd_processo = "+cdProcesso;
//			System.out.println(sql);
			pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			LogUtils.logTimer("ARQUIVO_AGENDA", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
			LogUtils.destroyTimer("ARQUIVO_AGENDA");
			return rsm;
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
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return ProcessoArquivoAgendaDAO.find(criterios, connect);
	}
}
