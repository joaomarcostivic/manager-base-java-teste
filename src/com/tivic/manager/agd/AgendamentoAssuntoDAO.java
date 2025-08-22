package com.tivic.manager.agd;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AgendamentoAssuntoDAO{

	public static int insert(AgendamentoAssunto objeto) {
		return insert(objeto, null);
	}

	public static int insert(AgendamentoAssunto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_agendamento_assunto (cd_assunto,"+
			                                  "cd_agendamento,"+
			                                  "nr_ordem,"+
			                                  "cd_responsavel,"+
			                                  "st_final,"+
			                                  "txt_memo,"+
			                                  "txt_fechamento,"+
			                                  "tp_prioridade,"+
			                                  "dt_previsao_conclusao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdAssunto()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAssunto());
			if(objeto.getCdAgendamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAgendamento());
			pstmt.setInt(3,objeto.getNrOrdem());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdResponsavel());
			pstmt.setInt(5,objeto.getStFinal());
			pstmt.setString(6,objeto.getTxtMemo());
			pstmt.setString(7,objeto.getTxtFechamento());
			pstmt.setInt(8,objeto.getTpPrioridade());
			if(objeto.getDtPrevisaoConclusao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtPrevisaoConclusao().getTimeInMillis()));
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoAssuntoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoAssuntoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AgendamentoAssunto objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AgendamentoAssunto objeto, int cdAssuntoOld, int cdAgendamentoOld) {
		return update(objeto, cdAssuntoOld, cdAgendamentoOld, null);
	}

	public static int update(AgendamentoAssunto objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AgendamentoAssunto objeto, int cdAssuntoOld, int cdAgendamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agd_agendamento_assunto SET cd_assunto=?,"+
												      		   "cd_agendamento=?,"+
												      		   "nr_ordem=?,"+
												      		   "cd_responsavel=?,"+
												      		   "st_final=?,"+
												      		   "txt_memo=?,"+
												      		   "txt_fechamento=?,"+
												      		   "tp_prioridade=?,"+
												      		   "dt_previsao_conclusao=? WHERE cd_assunto=? AND cd_agendamento=?");
			pstmt.setInt(1,objeto.getCdAssunto());
			pstmt.setInt(2,objeto.getCdAgendamento());
			pstmt.setInt(3,objeto.getNrOrdem());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdResponsavel());
			pstmt.setInt(5,objeto.getStFinal());
			pstmt.setString(6,objeto.getTxtMemo());
			pstmt.setString(7,objeto.getTxtFechamento());
			pstmt.setInt(8,objeto.getTpPrioridade());
			if(objeto.getDtPrevisaoConclusao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtPrevisaoConclusao().getTimeInMillis()));
			pstmt.setInt(10, cdAssuntoOld!=0 ? cdAssuntoOld : objeto.getCdAssunto());
			pstmt.setInt(11, cdAgendamentoOld!=0 ? cdAgendamentoOld : objeto.getCdAgendamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoAssuntoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoAssuntoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAssunto, int cdAgendamento) {
		return delete(cdAssunto, cdAgendamento, null);
	}

	public static int delete(int cdAssunto, int cdAgendamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_agendamento_assunto WHERE cd_assunto=? AND cd_agendamento=?");
			pstmt.setInt(1, cdAssunto);
			pstmt.setInt(2, cdAgendamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoAssuntoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoAssuntoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AgendamentoAssunto get(int cdAssunto, int cdAgendamento) {
		return get(cdAssunto, cdAgendamento, null);
	}

	public static AgendamentoAssunto get(int cdAssunto, int cdAgendamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_agendamento_assunto WHERE cd_assunto=? AND cd_agendamento=?");
			pstmt.setInt(1, cdAssunto);
			pstmt.setInt(2, cdAgendamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AgendamentoAssunto(rs.getInt("cd_assunto"),
						rs.getInt("cd_agendamento"),
						rs.getInt("nr_ordem"),
						rs.getInt("cd_responsavel"),
						rs.getInt("st_final"),
						rs.getString("txt_memo"),
						rs.getString("txt_fechamento"),
						rs.getInt("tp_prioridade"),
						(rs.getTimestamp("dt_previsao_conclusao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_previsao_conclusao").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoAssuntoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoAssuntoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_agendamento_assunto");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoAssuntoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoAssuntoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM agd_agendamento_assunto", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
