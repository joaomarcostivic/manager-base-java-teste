package com.tivic.manager.agd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class AgendamentoDAO{

	public static int insert(Agendamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(Agendamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("agd_agendamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAgendamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_agendamento (cd_agendamento,"+
			                                  "nm_agendamento,"+
			                                  "nm_local,"+
			                                  "dt_inicial,"+
			                                  "dt_final,"+
			                                  "st_agendamento,"+
			                                  "txt_agendamento,"+
			                                  "lg_lembrete,"+
			                                  "qt_tempo_lembrete,"+
			                                  "tp_unidade_tempo_lembrete,"+
			                                  "lg_anexos,"+
			                                  "dt_cadastro,"+
			                                  "cd_recorrencia,"+
			                                  "id_agendamento,"+
			                                  "nr_recorrencia,"+
			                                  "cd_tipo_agendamento,"+
			                                  "lg_original,"+
			                                  "cd_agenda,"+
			                                  "txt_abertura,"+
			                                  "cd_mailing,"+
			                                  "cd_documento,"+
			                                  "dt_lembrete,"+
			                                  "cd_tipo_situacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmAgendamento());
			pstmt.setString(3,objeto.getNmLocal());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setInt(6,objeto.getStAgendamento());
			pstmt.setString(7,objeto.getTxtAgendamento());
			pstmt.setInt(8,objeto.getLgLembrete());
			pstmt.setInt(9,objeto.getQtTempoLembrete());
			pstmt.setInt(10,objeto.getTpUnidadeTempoLembrete());
			pstmt.setInt(11,objeto.getLgAnexos());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			if(objeto.getCdRecorrencia()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdRecorrencia());
			pstmt.setString(14,objeto.getIdAgendamento());
			pstmt.setInt(15,objeto.getNrRecorrencia());
			if(objeto.getCdTipoAgendamento()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdTipoAgendamento());
			pstmt.setInt(17,objeto.getLgOriginal());
			if(objeto.getCdAgenda()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdAgenda());
			pstmt.setString(19,objeto.getTxtAbertura());
			if(objeto.getCdMailing()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdMailing());
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdDocumento());
			if(objeto.getDtLembrete()==null)
				pstmt.setNull(22, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(22,new Timestamp(objeto.getDtLembrete().getTimeInMillis()));
			if(objeto.getCdTipoSituacao()==0)
				pstmt.setNull(23, Types.INTEGER);
			else
				pstmt.setInt(23,objeto.getCdTipoSituacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Agendamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Agendamento objeto, int cdAgendamentoOld) {
		return update(objeto, cdAgendamentoOld, null);
	}

	public static int update(Agendamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Agendamento objeto, int cdAgendamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agd_agendamento SET cd_agendamento=?,"+
												      		   "nm_agendamento=?,"+
												      		   "nm_local=?,"+
												      		   "dt_inicial=?,"+
												      		   "dt_final=?,"+
												      		   "st_agendamento=?,"+
												      		   "txt_agendamento=?,"+
												      		   "lg_lembrete=?,"+
												      		   "qt_tempo_lembrete=?,"+
												      		   "tp_unidade_tempo_lembrete=?,"+
												      		   "lg_anexos=?,"+
												      		   "dt_cadastro=?,"+
												      		   "cd_recorrencia=?,"+
												      		   "id_agendamento=?,"+
												      		   "nr_recorrencia=?,"+
												      		   "cd_tipo_agendamento=?,"+
												      		   "lg_original=?,"+
												      		   "cd_agenda=?,"+
												      		   "txt_abertura=?,"+
												      		   "cd_mailing=?,"+
												      		   "cd_documento=?,"+
												      		   "dt_lembrete=?,"+
												      		   "cd_tipo_situacao=? WHERE cd_agendamento=?");
			pstmt.setInt(1,objeto.getCdAgendamento());
			pstmt.setString(2,objeto.getNmAgendamento());
			pstmt.setString(3,objeto.getNmLocal());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setInt(6,objeto.getStAgendamento());
			pstmt.setString(7,objeto.getTxtAgendamento());
			pstmt.setInt(8,objeto.getLgLembrete());
			pstmt.setInt(9,objeto.getQtTempoLembrete());
			pstmt.setInt(10,objeto.getTpUnidadeTempoLembrete());
			pstmt.setInt(11,objeto.getLgAnexos());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			if(objeto.getCdRecorrencia()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdRecorrencia());
			pstmt.setString(14,objeto.getIdAgendamento());
			pstmt.setInt(15,objeto.getNrRecorrencia());
			if(objeto.getCdTipoAgendamento()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdTipoAgendamento());
			pstmt.setInt(17,objeto.getLgOriginal());
			if(objeto.getCdAgenda()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdAgenda());
			pstmt.setString(19,objeto.getTxtAbertura());
			if(objeto.getCdMailing()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdMailing());
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdDocumento());
			if(objeto.getDtLembrete()==null)
				pstmt.setNull(22, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(22,new Timestamp(objeto.getDtLembrete().getTimeInMillis()));
			if(objeto.getCdTipoSituacao()==0)
				pstmt.setNull(23, Types.INTEGER);
			else
				pstmt.setInt(23,objeto.getCdTipoSituacao());
			pstmt.setInt(24, cdAgendamentoOld!=0 ? cdAgendamentoOld : objeto.getCdAgendamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAgendamento) {
		return delete(cdAgendamento, null);
	}

	public static int delete(int cdAgendamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_agendamento WHERE cd_agendamento=?");
			pstmt.setInt(1, cdAgendamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Agendamento get(int cdAgendamento) {
		return get(cdAgendamento, null);
	}

	public static Agendamento get(int cdAgendamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_agendamento WHERE cd_agendamento=?");
			pstmt.setInt(1, cdAgendamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Agendamento(rs.getInt("cd_agendamento"),
						rs.getString("nm_agendamento"),
						rs.getString("nm_local"),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()),
						rs.getInt("st_agendamento"),
						rs.getString("txt_agendamento"),
						rs.getInt("lg_lembrete"),
						rs.getInt("qt_tempo_lembrete"),
						rs.getInt("tp_unidade_tempo_lembrete"),
						rs.getInt("lg_anexos"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
						rs.getInt("cd_recorrencia"),
						rs.getString("id_agendamento"),
						rs.getInt("nr_recorrencia"),
						rs.getInt("cd_tipo_agendamento"),
						rs.getInt("lg_original"),
						rs.getInt("cd_agenda"),
						rs.getString("txt_abertura"),
						rs.getInt("cd_mailing"),
						rs.getInt("cd_documento"),
						(rs.getTimestamp("dt_lembrete")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_lembrete").getTime()),
						rs.getInt("cd_tipo_situacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_agendamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Agendamento> getList() {
		return getList(null);
	}

	public static ArrayList<Agendamento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Agendamento> list = new ArrayList<Agendamento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Agendamento obj = AgendamentoDAO.get(rsm.getInt("cd_agendamento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM agd_agendamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
