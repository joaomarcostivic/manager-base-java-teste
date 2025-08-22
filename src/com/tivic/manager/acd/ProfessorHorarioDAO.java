package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ProfessorHorarioDAO{

	public static int insert(ProfessorHorario objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProfessorHorario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_professor_horario (cd_horario,"+
			                                  "cd_professor) VALUES (?, ?)");
			if(objeto.getCdHorario()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdHorario());
			if(objeto.getCdProfessor()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProfessor());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorHorarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorHorarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProfessorHorario objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ProfessorHorario objeto, int cdHorarioOld, int cdProfessorOld) {
		return update(objeto, cdHorarioOld, cdProfessorOld, null);
	}

	public static int update(ProfessorHorario objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ProfessorHorario objeto, int cdHorarioOld, int cdProfessorOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_professor_horario SET cd_horario=?,"+
												      		   "cd_professor=? WHERE cd_horario=? AND cd_professor=?");
			pstmt.setInt(1,objeto.getCdHorario());
			pstmt.setInt(2,objeto.getCdProfessor());
			pstmt.setInt(3, cdHorarioOld!=0 ? cdHorarioOld : objeto.getCdHorario());
			pstmt.setInt(4, cdProfessorOld!=0 ? cdProfessorOld : objeto.getCdProfessor());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorHorarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorHorarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdHorario, int cdProfessor) {
		return delete(cdHorario, cdProfessor, null);
	}

	public static int delete(int cdHorario, int cdProfessor, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_professor_horario WHERE cd_horario=? AND cd_professor=?");
			pstmt.setInt(1, cdHorario);
			pstmt.setInt(2, cdProfessor);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorHorarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorHorarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProfessorHorario get(int cdHorario, int cdProfessor) {
		return get(cdHorario, cdProfessor, null);
	}

	public static ProfessorHorario get(int cdHorario, int cdProfessor, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_professor_horario WHERE cd_horario=? AND cd_professor=?");
			pstmt.setInt(1, cdHorario);
			pstmt.setInt(2, cdProfessor);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProfessorHorario(rs.getInt("cd_horario"),
						rs.getInt("cd_professor"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorHorarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorHorarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_professor_horario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorHorarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorHorarioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ProfessorHorario> getList() {
		return getList(null);
	}

	public static ArrayList<ProfessorHorario> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ProfessorHorario> list = new ArrayList<ProfessorHorario>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ProfessorHorario obj = ProfessorHorarioDAO.get(rsm.getInt("cd_horario"), rsm.getInt("cd_professor"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorHorarioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_professor_horario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
