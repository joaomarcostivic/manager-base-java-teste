package com.tivic.manager.bpm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class BemManutencaoDAO{

	public static int insert(BemManutencao objeto) {
		return insert(objeto, null);
	}

	public static int insert(BemManutencao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("bpm_bem_manutencao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdManutencao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO bpm_bem_manutencao (cd_manutencao,"+
			                                  "cd_fornecedor,"+
			                                  "cd_responsavel,"+
			                                  "cd_referencia,"+
			                                  "cd_defeito,"+
			                                  "dt_manutencao,"+
			                                  "vl_previsto,"+
			                                  "dt_prevista,"+
			                                  "txt_observacao,"+
			                                  "st_manutencao,"+
			                                  "nr_os,"+
			                                  "dt_notificacao,"+
			                                  "dt_atendimento,"+
			                                  "txt_relatorio_tecnico,"+
			                                  "txt_avaliacao,"+
			                                  "txt_problema,"+
			                                  "vl_total,"+
			                                  "tp_manutencao,"+
			                                  "cd_agendamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdFornecedor()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFornecedor());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdResponsavel());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdReferencia());
			if(objeto.getCdDefeito()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdDefeito());
			if(objeto.getDtManutencao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtManutencao().getTimeInMillis()));
			pstmt.setFloat(7,objeto.getVlPrevisto());
			if(objeto.getDtPrevista()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtPrevista().getTimeInMillis()));
			pstmt.setString(9,objeto.getTxtObservacao());
			pstmt.setInt(10,objeto.getStManutencao());
			pstmt.setString(11,objeto.getNrOs());
			if(objeto.getDtNotificacao()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtNotificacao().getTimeInMillis()));
			if(objeto.getDtAtendimento()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtAtendimento().getTimeInMillis()));
			pstmt.setString(14,objeto.getTxtRelatorioTecnico());
			pstmt.setString(15,objeto.getTxtAvaliacao());
			pstmt.setString(16,objeto.getTxtProblema());
			pstmt.setFloat(17,objeto.getVlTotal());
			pstmt.setInt(18,objeto.getTpManutencao());
			if(objeto.getCdAgendamento()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdAgendamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BemManutencaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BemManutencaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(BemManutencao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(BemManutencao objeto, int cdManutencaoOld) {
		return update(objeto, cdManutencaoOld, null);
	}

	public static int update(BemManutencao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(BemManutencao objeto, int cdManutencaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE bpm_bem_manutencao SET cd_manutencao=?,"+
												      		   "cd_fornecedor=?,"+
												      		   "cd_responsavel=?,"+
												      		   "cd_referencia=?,"+
												      		   "cd_defeito=?,"+
												      		   "dt_manutencao=?,"+
												      		   "vl_previsto=?,"+
												      		   "dt_prevista=?,"+
												      		   "txt_observacao=?,"+
												      		   "st_manutencao=?,"+
												      		   "nr_os=?,"+
												      		   "dt_notificacao=?,"+
												      		   "dt_atendimento=?,"+
												      		   "txt_relatorio_tecnico=?,"+
												      		   "txt_avaliacao=?,"+
												      		   "txt_problema=?,"+
												      		   "vl_total=?,"+
												      		   "tp_manutencao=?,"+
												      		   "cd_agendamento=? WHERE cd_manutencao=?");
			pstmt.setInt(1,objeto.getCdManutencao());
			if(objeto.getCdFornecedor()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFornecedor());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdResponsavel());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdReferencia());
			if(objeto.getCdDefeito()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdDefeito());
			if(objeto.getDtManutencao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtManutencao().getTimeInMillis()));
			pstmt.setFloat(7,objeto.getVlPrevisto());
			if(objeto.getDtPrevista()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtPrevista().getTimeInMillis()));
			pstmt.setString(9,objeto.getTxtObservacao());
			pstmt.setInt(10,objeto.getStManutencao());
			pstmt.setString(11,objeto.getNrOs());
			if(objeto.getDtNotificacao()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtNotificacao().getTimeInMillis()));
			if(objeto.getDtAtendimento()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtAtendimento().getTimeInMillis()));
			pstmt.setString(14,objeto.getTxtRelatorioTecnico());
			pstmt.setString(15,objeto.getTxtAvaliacao());
			pstmt.setString(16,objeto.getTxtProblema());
			pstmt.setFloat(17,objeto.getVlTotal());
			pstmt.setInt(18,objeto.getTpManutencao());
			if(objeto.getCdAgendamento()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdAgendamento());
			pstmt.setInt(20, cdManutencaoOld!=0 ? cdManutencaoOld : objeto.getCdManutencao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BemManutencaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BemManutencaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdManutencao) {
		return delete(cdManutencao, null);
	}

	public static int delete(int cdManutencao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM bpm_bem_manutencao WHERE cd_manutencao=?");
			pstmt.setInt(1, cdManutencao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BemManutencaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BemManutencaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static BemManutencao get(int cdManutencao) {
		return get(cdManutencao, null);
	}

	public static BemManutencao get(int cdManutencao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM bpm_bem_manutencao WHERE cd_manutencao=?");
			pstmt.setInt(1, cdManutencao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new BemManutencao(rs.getInt("cd_manutencao"),
						rs.getInt("cd_fornecedor"),
						rs.getInt("cd_responsavel"),
						rs.getInt("cd_referencia"),
						rs.getInt("cd_defeito"),
						(rs.getTimestamp("dt_manutencao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_manutencao").getTime()),
						rs.getFloat("vl_previsto"),
						(rs.getTimestamp("dt_prevista")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_prevista").getTime()),
						rs.getString("txt_observacao"),
						rs.getInt("st_manutencao"),
						rs.getString("nr_os"),
						(rs.getTimestamp("dt_notificacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_notificacao").getTime()),
						(rs.getTimestamp("dt_atendimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_atendimento").getTime()),
						rs.getString("txt_relatorio_tecnico"),
						rs.getString("txt_avaliacao"),
						rs.getString("txt_problema"),
						rs.getFloat("vl_total"),
						rs.getInt("tp_manutencao"),
						rs.getInt("cd_agendamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BemManutencaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BemManutencaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM bpm_bem_manutencao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BemManutencaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BemManutencaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM bpm_bem_manutencao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
