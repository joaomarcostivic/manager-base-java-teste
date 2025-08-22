package com.tivic.manager.agd;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AgendamentoUsuarioDAO{

	public static int insert(AgendamentoUsuario objeto) {
		return insert(objeto, null);
	}

	public static int insert(AgendamentoUsuario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_agendamento_usuario (cd_usuario,"+
			                                  "cd_agendamento,"+
			                                  "tp_nivel_usuario) VALUES (?, ?, ?)");
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdUsuario());
			if(objeto.getCdAgendamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAgendamento());
			pstmt.setInt(3,objeto.getTpNivelUsuario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoUsuarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoUsuarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AgendamentoUsuario objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AgendamentoUsuario objeto, int cdUsuarioOld, int cdAgendamentoOld) {
		return update(objeto, cdUsuarioOld, cdAgendamentoOld, null);
	}

	public static int update(AgendamentoUsuario objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AgendamentoUsuario objeto, int cdUsuarioOld, int cdAgendamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agd_agendamento_usuario SET cd_usuario=?,"+
												      		   "cd_agendamento=?,"+
												      		   "tp_nivel_usuario=? WHERE cd_usuario=? AND cd_agendamento=?");
			pstmt.setInt(1,objeto.getCdUsuario());
			pstmt.setInt(2,objeto.getCdAgendamento());
			pstmt.setInt(3,objeto.getTpNivelUsuario());
			pstmt.setInt(4, cdUsuarioOld!=0 ? cdUsuarioOld : objeto.getCdUsuario());
			pstmt.setInt(5, cdAgendamentoOld!=0 ? cdAgendamentoOld : objeto.getCdAgendamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoUsuarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoUsuarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdUsuario, int cdAgendamento) {
		return delete(cdUsuario, cdAgendamento, null);
	}

	public static int delete(int cdUsuario, int cdAgendamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_agendamento_usuario WHERE cd_usuario=? AND cd_agendamento=?");
			pstmt.setInt(1, cdUsuario);
			pstmt.setInt(2, cdAgendamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoUsuarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoUsuarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AgendamentoUsuario get(int cdUsuario, int cdAgendamento) {
		return get(cdUsuario, cdAgendamento, null);
	}

	public static AgendamentoUsuario get(int cdUsuario, int cdAgendamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_agendamento_usuario WHERE cd_usuario=? AND cd_agendamento=?");
			pstmt.setInt(1, cdUsuario);
			pstmt.setInt(2, cdAgendamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AgendamentoUsuario(rs.getInt("cd_usuario"),
						rs.getInt("cd_agendamento"),
						rs.getInt("tp_nivel_usuario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoUsuarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoUsuarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_agendamento_usuario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoUsuarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoUsuarioDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM agd_agendamento_usuario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
