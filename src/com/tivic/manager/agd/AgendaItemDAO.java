package com.tivic.manager.agd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class AgendaItemDAO{

	public static int insert(AgendaItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(AgendaItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("agd_agenda_item", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAgendaItem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_agenda_item (cd_agenda_item,"+
			                                  "dt_inicial,"+
			                                  "dt_final,"+
			                                  "dt_lancamento,"+
			                                  "ds_detalhe,"+
			                                  "st_agenda_item,"+
			                                  "cd_tipo_prazo,"+
			                                  "cd_pessoa,"+
			                                  "cd_processo,"+
			                                  "cd_tipo_agendamento,"+
			                                  "dt_realizacao,"+
			                                  "ds_observacao,"+
			                                  "dt_alarme,"+
			                                  "cd_usuario,"+
			                                  "dt_alteracao,"+
			                                  "ds_assunto,"+
			                                  "dt_aceite,"+
			                                  "cd_documento,"+
			                                  "cd_tipo_prazo_documento,"+
			                                  "cd_usuario_aceite,"+
			                                  "cd_local,"+
			                                  "cd_empresa,"+
			                                  "dt_rejeite,"+
			                                  "cd_usuario_rejeite,"+
			                                  "vl_servico,"+
			                                  "txt_parecer,"+
			                                  "lg_preposto,"+
			                                  "cd_usuario_cumprimento,"+
			                                  "cd_grupo_trabalho,"+
			                                  "cd_agenda_item_superior,"+
			                                  "qt_preposto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getDtInicial()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			pstmt.setString(5,objeto.getDsDetalhe());
			pstmt.setInt(6,objeto.getStAgendaItem());
			if(objeto.getCdTipoPrazo()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTipoPrazo());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdPessoa());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdProcesso());
			pstmt.setInt(10,objeto.getCdTipoAgendamento());
			if(objeto.getDtRealizacao()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtRealizacao().getTimeInMillis()));
			pstmt.setString(12,objeto.getDsObservacao());
			if(objeto.getDtAlarme()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtAlarme().getTimeInMillis()));
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdUsuario());
			if(objeto.getDtAlteracao()==null)
				pstmt.setNull(15, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(15,new Timestamp(objeto.getDtAlteracao().getTimeInMillis()));
			pstmt.setString(16,objeto.getDsAssunto());
			if(objeto.getDtAceite()==null)
				pstmt.setNull(17, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(17,new Timestamp(objeto.getDtAceite().getTimeInMillis()));
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdDocumento());
			if(objeto.getCdTipoPrazoDocumento()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdTipoPrazoDocumento());
			if(objeto.getCdUsuarioAceite()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdUsuarioAceite());
			if(objeto.getCdLocal()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdLocal());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22,objeto.getCdEmpresa());
			if(objeto.getDtRejeite()==null)
				pstmt.setNull(23, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(23,new Timestamp(objeto.getDtRejeite().getTimeInMillis()));
			if(objeto.getCdUsuarioRejeite()==0)
				pstmt.setNull(24, Types.INTEGER);
			else
				pstmt.setInt(24,objeto.getCdUsuarioRejeite());
			pstmt.setDouble(25,objeto.getVlServico());
			pstmt.setString(26,objeto.getTxtParecer());
			pstmt.setInt(27,objeto.getLgPreposto());
			if(objeto.getCdUsuarioCumprimento()==0)
				pstmt.setNull(28, Types.INTEGER);
			else
				pstmt.setInt(28,objeto.getCdUsuarioCumprimento());
			if(objeto.getCdGrupoTrabalho()==0)
				pstmt.setNull(29, Types.INTEGER);
			else
				pstmt.setInt(29,objeto.getCdGrupoTrabalho());
			if(objeto.getCdAgendaItemSuperior()==0)
				pstmt.setNull(30, Types.INTEGER);
			else
				pstmt.setInt(30,objeto.getCdAgendaItemSuperior());
			pstmt.setInt(31,objeto.getQtPreposto());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AgendaItem objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AgendaItem objeto, int cdAgendaItemOld) {
		return update(objeto, cdAgendaItemOld, null);
	}

	public static int update(AgendaItem objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AgendaItem objeto, int cdAgendaItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agd_agenda_item SET cd_agenda_item=?,"+
												      		   "dt_inicial=?,"+
												      		   "dt_final=?,"+
												      		   "dt_lancamento=?,"+
												      		   "ds_detalhe=?,"+
												      		   "st_agenda_item=?,"+
												      		   "cd_tipo_prazo=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_processo=?,"+
												      		   "cd_tipo_agendamento=?,"+
												      		   "dt_realizacao=?,"+
												      		   "ds_observacao=?,"+
												      		   "dt_alarme=?,"+
												      		   "cd_usuario=?,"+
												      		   "dt_alteracao=?,"+
												      		   "ds_assunto=?,"+
												      		   "dt_aceite=?,"+
												      		   "cd_documento=?,"+
												      		   "cd_tipo_prazo_documento=?,"+
												      		   "cd_usuario_aceite=?,"+
												      		   "cd_local=?,"+
												      		   "cd_empresa=?,"+
												      		   "dt_rejeite=?,"+
												      		   "cd_usuario_rejeite=?,"+
												      		   "vl_servico=?,"+
												      		   "txt_parecer=?,"+
												      		   "lg_preposto=?,"+
												      		   "cd_usuario_cumprimento=?,"+
												      		   "cd_grupo_trabalho=?,"+
												      		   "cd_agenda_item_superior=?,"+
												      		   "qt_preposto=? WHERE cd_agenda_item=?");
			pstmt.setInt(1,objeto.getCdAgendaItem());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			pstmt.setString(5,objeto.getDsDetalhe());
			pstmt.setInt(6,objeto.getStAgendaItem());
			if(objeto.getCdTipoPrazo()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTipoPrazo());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdPessoa());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdProcesso());
			pstmt.setInt(10,objeto.getCdTipoAgendamento());
			if(objeto.getDtRealizacao()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtRealizacao().getTimeInMillis()));
			pstmt.setString(12,objeto.getDsObservacao());
			if(objeto.getDtAlarme()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtAlarme().getTimeInMillis()));
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdUsuario());
			if(objeto.getDtAlteracao()==null)
				pstmt.setNull(15, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(15,new Timestamp(objeto.getDtAlteracao().getTimeInMillis()));
			pstmt.setString(16,objeto.getDsAssunto());
			if(objeto.getDtAceite()==null)
				pstmt.setNull(17, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(17,new Timestamp(objeto.getDtAceite().getTimeInMillis()));
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdDocumento());
			if(objeto.getCdTipoPrazoDocumento()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdTipoPrazoDocumento());
			if(objeto.getCdUsuarioAceite()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdUsuarioAceite());
			if(objeto.getCdLocal()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdLocal());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22,objeto.getCdEmpresa());
			if(objeto.getDtRejeite()==null)
				pstmt.setNull(23, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(23,new Timestamp(objeto.getDtRejeite().getTimeInMillis()));
			if(objeto.getCdUsuarioRejeite()==0)
				pstmt.setNull(24, Types.INTEGER);
			else
				pstmt.setInt(24,objeto.getCdUsuarioRejeite());
			pstmt.setDouble(25,objeto.getVlServico());
			pstmt.setString(26,objeto.getTxtParecer());
			pstmt.setInt(27,objeto.getLgPreposto());
			if(objeto.getCdUsuarioCumprimento()==0)
				pstmt.setNull(28, Types.INTEGER);
			else
				pstmt.setInt(28,objeto.getCdUsuarioCumprimento());
			if(objeto.getCdGrupoTrabalho()==0)
				pstmt.setNull(29, Types.INTEGER);
			else
				pstmt.setInt(29,objeto.getCdGrupoTrabalho());
			if(objeto.getCdAgendaItemSuperior()==0)
				pstmt.setNull(30, Types.INTEGER);
			else
				pstmt.setInt(30,objeto.getCdAgendaItemSuperior());
			pstmt.setInt(31,objeto.getQtPreposto());
			pstmt.setInt(32, cdAgendaItemOld!=0 ? cdAgendaItemOld : objeto.getCdAgendaItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAgendaItem) {
		return delete(cdAgendaItem, null);
	}

	public static int delete(int cdAgendaItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_agenda_item WHERE cd_agenda_item=?");
			pstmt.setInt(1, cdAgendaItem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AgendaItem get(int cdAgendaItem) {
		return get(cdAgendaItem, null);
	}

	public static AgendaItem get(int cdAgendaItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_agenda_item WHERE cd_agenda_item=?");
			pstmt.setInt(1, cdAgendaItem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AgendaItem(rs.getInt("cd_agenda_item"),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()),
						(rs.getTimestamp("dt_lancamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_lancamento").getTime()),
						rs.getString("ds_detalhe"),
						rs.getInt("st_agenda_item"),
						rs.getInt("cd_tipo_prazo"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_processo"),
						rs.getInt("cd_tipo_agendamento"),
						(rs.getTimestamp("dt_realizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_realizacao").getTime()),
						rs.getString("ds_observacao"),
						(rs.getTimestamp("dt_alarme")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_alarme").getTime()),
						rs.getInt("cd_usuario"),
						(rs.getTimestamp("dt_alteracao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_alteracao").getTime()),
						rs.getString("ds_assunto"),
						(rs.getTimestamp("dt_aceite")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_aceite").getTime()),
						rs.getInt("cd_documento"),
						rs.getInt("cd_tipo_prazo_documento"),
						rs.getInt("cd_usuario_aceite"),
						rs.getInt("cd_local"),
						rs.getInt("cd_empresa"),
						(rs.getTimestamp("dt_rejeite")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_rejeite").getTime()),
						rs.getInt("cd_usuario_rejeite"),
						rs.getDouble("vl_servico"),
						rs.getString("txt_parecer"),
						rs.getInt("lg_preposto"),
						rs.getInt("cd_usuario_cumprimento"),
						rs.getInt("cd_grupo_trabalho"),
						rs.getInt("cd_agenda_item_superior"),
						rs.getInt("qt_preposto"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_agenda_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AgendaItem> getList() {
		return getList(null);
	}

	public static ArrayList<AgendaItem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AgendaItem> list = new ArrayList<AgendaItem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AgendaItem obj = AgendaItemDAO.get(rsm.getInt("cd_agenda_item"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM agd_agenda_item", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}