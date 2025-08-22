package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ProcessoAtividadeItemDAO{

	public static int insert(ProcessoAtividadeItem objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(ProcessoAtividadeItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_processo_item");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdProcessoItem()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_atividade_item");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("grl_processo_atividade_item", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAtividadeItem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_processo_atividade_item (cd_processo_item,"+
			                                  "cd_atividade_item,"+
			                                  "cd_atividade,"+
			                                  "cd_processo,"+
			                                  "cd_agendamento,"+
			                                  "dt_recebimento,"+
			                                  "dt_lancamento) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdProcessoItem()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdProcessoItem());
			pstmt.setInt(2, code);
			if(objeto.getCdAtividade()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAtividade());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProcesso());
			if(objeto.getCdAgendamento()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdAgendamento());
			if(objeto.getDtRecebimento()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtRecebimento().getTimeInMillis()));
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProcessoAtividadeItem objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ProcessoAtividadeItem objeto, int cdProcessoItemOld, int cdAtividadeItemOld) {
		return update(objeto, cdProcessoItemOld, cdAtividadeItemOld, null);
	}

	public static int update(ProcessoAtividadeItem objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ProcessoAtividadeItem objeto, int cdProcessoItemOld, int cdAtividadeItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_processo_atividade_item SET cd_processo_item=?,"+
												      		   "cd_atividade_item=?,"+
												      		   "cd_atividade=?,"+
												      		   "cd_processo=?,"+
												      		   "cd_agendamento=?,"+
												      		   "dt_recebimento=?,"+
												      		   "dt_lancamento=? WHERE cd_processo_item=? AND cd_atividade_item=?");
			pstmt.setInt(1,objeto.getCdProcessoItem());
			pstmt.setInt(2,objeto.getCdAtividadeItem());
			if(objeto.getCdAtividade()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAtividade());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProcesso());
			if(objeto.getCdAgendamento()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdAgendamento());
			if(objeto.getDtRecebimento()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtRecebimento().getTimeInMillis()));
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			pstmt.setInt(8, cdProcessoItemOld!=0 ? cdProcessoItemOld : objeto.getCdProcessoItem());
			pstmt.setInt(9, cdAtividadeItemOld!=0 ? cdAtividadeItemOld : objeto.getCdAtividadeItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProcessoItem, int cdAtividadeItem) {
		return delete(cdProcessoItem, cdAtividadeItem, null);
	}

	public static int delete(int cdProcessoItem, int cdAtividadeItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_processo_atividade_item WHERE cd_processo_item=? AND cd_atividade_item=?");
			pstmt.setInt(1, cdProcessoItem);
			pstmt.setInt(2, cdAtividadeItem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProcessoAtividadeItem get(int cdProcessoItem, int cdAtividadeItem) {
		return get(cdProcessoItem, cdAtividadeItem, null);
	}

	public static ProcessoAtividadeItem get(int cdProcessoItem, int cdAtividadeItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_processo_atividade_item WHERE cd_processo_item=? AND cd_atividade_item=?");
			pstmt.setInt(1, cdProcessoItem);
			pstmt.setInt(2, cdAtividadeItem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProcessoAtividadeItem(rs.getInt("cd_processo_item"),
						rs.getInt("cd_atividade_item"),
						rs.getInt("cd_atividade"),
						rs.getInt("cd_processo"),
						rs.getInt("cd_agendamento"),
						(rs.getTimestamp("dt_recebimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_recebimento").getTime()),
						(rs.getTimestamp("dt_lancamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_lancamento").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeItemDAO.get: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_processo_atividade_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeItemDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_processo_atividade_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
