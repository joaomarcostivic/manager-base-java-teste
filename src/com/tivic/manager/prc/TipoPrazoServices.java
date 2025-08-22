package com.tivic.manager.prc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class TipoPrazoServices {
	
	public static final String[] tiposAgendaItem  = {"Audiência", "Prazo", "Tarefa", "Diligência"};
	
	public static final int TP_AUDIENCIA  = 0;
	public static final int TP_PRAZO 	  = 1;
	public static final int TP_TAREFA	  = 2;
	public static final int TP_DILIGENCIA = 3;
	
	public static Result save(TipoPrazo tipoPrazo){
		return save(tipoPrazo, null);
	}
	
	public static Result save(TipoPrazo tipoPrazo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tipoPrazo==null)
				return new Result(-1, "Erro ao salvar. Tipo de prazo é nulo");
			
			int retorno;
			if(tipoPrazo.getCdTipoPrazo()==0){
				retorno = TipoPrazoDAO.insert(tipoPrazo, connect);
				tipoPrazo.setCdTipoPrazo(retorno);
			}
			else {
				retorno = TipoPrazoDAO.update(tipoPrazo, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOPRAZO", tipoPrazo);
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
	
	public static Result remove(int cdTipoPrazo){
		return remove(cdTipoPrazo, false, null);
	}
	
	public static Result remove(int cdTipoPrazo, boolean cascade){
		return remove(cdTipoPrazo, cascade, null);
	}
	
	public static Result remove(int cdTipoPrazo, boolean cascade, Connection connect){
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
				retorno = TipoPrazoDAO.delete(cdTipoPrazo, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este tipo de prazo está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Tipo de prazo excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir tipo de prazo!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					"SELECT A.*,"
					+ " B.nm_pessoa, "
					+ " C.nm_grupo "
					+ " FROM prc_tipo_prazo A"
					+ " LEFT OUTER JOIN grl_pessoa B ON (A.cd_pessoa_padrao = B.cd_pessoa)"
					+ " LEFT OUTER JOIN agd_grupo  C ON (A.cd_grupo_trabalho_padrao = C.cd_grupo)"
					+ " ORDER BY A.tp_agenda_item, A.nm_tipo_prazo"
				);
			ResultSetMap rsm =  new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()) {
				rsm.setValueToField("ds_tipo_prazo", ("["+tiposAgendaItem[rsm.getInt("tp_agenda_item")]+"] " + rsm.getString("nm_tipo_prazo")));
				rsm.setValueToField("nm_tp_agenda_item", tiposAgendaItem[rsm.getInt("tp_agenda_item")]);
			}
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPrazoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByTipoAgenda() {
		return getAll();
	}
	
	public static ResultSetMap getAllByTipoAgenda(int tpAgendaItem) {
		return getAllByTipoAgenda(tpAgendaItem, -1, null);
	}
	
	public static ResultSetMap getAllByTipoAgenda(int tpAgendaItem, int stTipoPrazo) {
		return getAllByTipoAgenda(tpAgendaItem, stTipoPrazo, null);
	}

	public static ResultSetMap getAllByTipoAgenda(int tpAgendaItem, int stTipoPrazo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					"SELECT A.*,"
					+ " B.nm_pessoa, "
					+ " C.nm_grupo "
					+ " FROM prc_tipo_prazo A"
					+ " LEFT OUTER JOIN grl_pessoa B ON (A.cd_pessoa_padrao = B.cd_pessoa)"
					+ " LEFT OUTER JOIN agd_grupo  C ON (A.cd_grupo_trabalho_padrao = C.cd_grupo)"
					+ " WHERE A.tp_agenda_item = ?"
					+ (stTipoPrazo>-1? " AND A.st_tipo_prazo = "+stTipoPrazo : "")
					+ " ORDER BY A.nm_tipo_prazo");
			pstmt.setInt(1, tpAgendaItem);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPrazoServices.getAllByTipoAgenda: " + e);
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
		String nmTipoPrazo = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("NM_TIPO_PRAZO")) {
				nmTipoPrazo =	Util.limparTexto(criterios.get(i).getValue());
				nmTipoPrazo = nmTipoPrazo.trim();
				criterios.remove(i);
				i--;
			}
		}
		return Search.find("SELECT * FROM prc_tipo_prazo "+
				"WHERE 1=1 "+
				(!nmTipoPrazo.equals("") ?
				" AND TRANSLATE (nm_tipo_prazo, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', "+
						"					'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmTipoPrazo)+"%' "
						: "")
				, "ORDER BY nm_tipo_prazo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}