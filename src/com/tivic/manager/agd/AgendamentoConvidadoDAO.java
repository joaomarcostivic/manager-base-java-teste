package com.tivic.manager.agd;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AgendamentoConvidadoDAO{

	public static int insert(AgendamentoConvidado objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(AgendamentoConvidado objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_convidado");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_agendamento");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdAgendamento()));
			int code = Conexao.getSequenceCode("agd_agendamento_convidado", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdConvidado(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_agendamento_convidado (cd_convidado,"+
			                                  "cd_agendamento,"+
			                                  "nm_convidado,"+
			                                  "nm_email,"+
			                                  "lg_presenca) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdAgendamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAgendamento());
			pstmt.setString(3,objeto.getNmConvidado());
			pstmt.setString(4,objeto.getNmEmail());
			pstmt.setInt(5,objeto.getLgPresenca());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoConvidadoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoConvidadoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AgendamentoConvidado objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AgendamentoConvidado objeto, int cdConvidadoOld, int cdAgendamentoOld) {
		return update(objeto, cdConvidadoOld, cdAgendamentoOld, null);
	}

	public static int update(AgendamentoConvidado objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AgendamentoConvidado objeto, int cdConvidadoOld, int cdAgendamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agd_agendamento_convidado SET cd_convidado=?,"+
												      		   "cd_agendamento=?,"+
												      		   "nm_convidado=?,"+
												      		   "nm_email=?,"+
												      		   "lg_presenca=? WHERE cd_convidado=? AND cd_agendamento=?");
			pstmt.setInt(1,objeto.getCdConvidado());
			pstmt.setInt(2,objeto.getCdAgendamento());
			pstmt.setString(3,objeto.getNmConvidado());
			pstmt.setString(4,objeto.getNmEmail());
			pstmt.setInt(5,objeto.getLgPresenca());
			pstmt.setInt(6, cdConvidadoOld!=0 ? cdConvidadoOld : objeto.getCdConvidado());
			pstmt.setInt(7, cdAgendamentoOld!=0 ? cdAgendamentoOld : objeto.getCdAgendamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoConvidadoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoConvidadoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConvidado, int cdAgendamento) {
		return delete(cdConvidado, cdAgendamento, null);
	}

	public static int delete(int cdConvidado, int cdAgendamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_agendamento_convidado WHERE cd_convidado=? AND cd_agendamento=?");
			pstmt.setInt(1, cdConvidado);
			pstmt.setInt(2, cdAgendamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoConvidadoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoConvidadoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AgendamentoConvidado get(int cdConvidado, int cdAgendamento) {
		return get(cdConvidado, cdAgendamento, null);
	}

	public static AgendamentoConvidado get(int cdConvidado, int cdAgendamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_agendamento_convidado WHERE cd_convidado=? AND cd_agendamento=?");
			pstmt.setInt(1, cdConvidado);
			pstmt.setInt(2, cdAgendamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AgendamentoConvidado(rs.getInt("cd_convidado"),
						rs.getInt("cd_agendamento"),
						rs.getString("nm_convidado"),
						rs.getString("nm_email"),
						rs.getInt("lg_presenca"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoConvidadoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoConvidadoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_agendamento_convidado");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoConvidadoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoConvidadoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM agd_agendamento_convidado", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
