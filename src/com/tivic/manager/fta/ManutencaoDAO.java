package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ManutencaoDAO{

	public static int insert(Manutencao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Manutencao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = com.tivic.manager.bpm.BemManutencaoDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdManutencao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_manutencao (cd_manutencao,"+
			                                  "cd_checkup,"+
			                                  "cd_componente,"+
			                                  "cd_checkup_item) VALUES (?, ?, ?, ?)");
			if(objeto.getCdManutencao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdManutencao());
			if(objeto.getCdCheckup()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCheckup());
			if(objeto.getCdComponente()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdComponente());
			if(objeto.getCdCheckupItem()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCheckupItem());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ManutencaoDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ManutencaoDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Manutencao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Manutencao objeto, int cdManutencaoOld) {
		return update(objeto, cdManutencaoOld, null);
	}

	public static int update(Manutencao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Manutencao objeto, int cdManutencaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			Manutencao objetoTemp = get(objeto.getCdManutencao(), connect);
			if (objetoTemp == null)
				pstmt = connect.prepareStatement("INSERT INTO fta_manutencao (cd_manutencao,"+
			                                  "cd_checkup,"+
			                                  "cd_componente,"+
			                                  "cd_checkup_item) VALUES (?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE fta_manutencao SET cd_manutencao=?,"+
												      		   "cd_checkup=?,"+
												      		   "cd_componente=?,"+
												      		   "cd_checkup_item=? WHERE cd_manutencao=?");
			pstmt.setInt(1,objeto.getCdManutencao());
			if(objeto.getCdCheckup()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCheckup());
			if(objeto.getCdComponente()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdComponente());
			if(objeto.getCdCheckupItem()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCheckupItem());
			if (objetoTemp != null) {
				pstmt.setInt(5, cdManutencaoOld!=0 ? cdManutencaoOld : objeto.getCdManutencao());
			}
			pstmt.executeUpdate();
			if (com.tivic.manager.bpm.BemManutencaoDAO.update(objeto, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ManutencaoDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ManutencaoDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
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
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_manutencao WHERE cd_manutencao=?");
			pstmt.setInt(1, cdManutencao);
			pstmt.executeUpdate();
			if (com.tivic.manager.bpm.BemManutencaoDAO.delete(cdManutencao, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ManutencaoDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ManutencaoDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Manutencao get(int cdManutencao) {
		return get(cdManutencao, null);
	}

	public static Manutencao get(int cdManutencao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_manutencao A, bpm_bem_manutencao B WHERE A.cd_manutencao=B.cd_manutencao AND A.cd_manutencao=?");
			pstmt.setInt(1, cdManutencao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Manutencao(rs.getInt("cd_manutencao"),
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
						rs.getInt("cd_agendamento"),
						rs.getInt("cd_checkup"),
						rs.getInt("cd_componente"),
						rs.getInt("cd_checkup_item"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ManutencaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ManutencaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_manutencao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ManutencaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ManutencaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_manutencao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
