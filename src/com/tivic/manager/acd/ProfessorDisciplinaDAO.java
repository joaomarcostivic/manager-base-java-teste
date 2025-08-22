package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ProfessorDisciplinaDAO{

	public static int insert(ProfessorDisciplina objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProfessorDisciplina objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_professor_disciplina (cd_professor,"+
			                                  "cd_disciplina) VALUES (?, ?)");
			if(objeto.getCdProfessor()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdProfessor());
			if(objeto.getCdDisciplina()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDisciplina());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDisciplinaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDisciplinaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProfessorDisciplina objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ProfessorDisciplina objeto, int cdProfessorOld, int cdDisciplinaOld) {
		return update(objeto, cdProfessorOld, cdDisciplinaOld, null);
	}

	public static int update(ProfessorDisciplina objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ProfessorDisciplina objeto, int cdProfessorOld, int cdDisciplinaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_professor_disciplina SET cd_professor=?,"+
												      		   "cd_disciplina=? WHERE cd_professor=? AND cd_disciplina=?");
			pstmt.setInt(1,objeto.getCdProfessor());
			pstmt.setInt(2,objeto.getCdDisciplina());
			pstmt.setInt(3, cdProfessorOld!=0 ? cdProfessorOld : objeto.getCdProfessor());
			pstmt.setInt(4, cdDisciplinaOld!=0 ? cdDisciplinaOld : objeto.getCdDisciplina());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDisciplinaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDisciplinaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProfessor, int cdDisciplina) {
		return delete(cdProfessor, cdDisciplina, null);
	}

	public static int delete(int cdProfessor, int cdDisciplina, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_professor_disciplina WHERE cd_professor=? AND cd_disciplina=?");
			pstmt.setInt(1, cdProfessor);
			pstmt.setInt(2, cdDisciplina);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDisciplinaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDisciplinaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProfessorDisciplina get(int cdProfessor, int cdDisciplina) {
		return get(cdProfessor, cdDisciplina, null);
	}

	public static ProfessorDisciplina get(int cdProfessor, int cdDisciplina, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_professor_disciplina WHERE cd_professor=? AND cd_disciplina=?");
			pstmt.setInt(1, cdProfessor);
			pstmt.setInt(2, cdDisciplina);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProfessorDisciplina(rs.getInt("cd_professor"),
						rs.getInt("cd_disciplina"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDisciplinaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDisciplinaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_professor_disciplina");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDisciplinaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDisciplinaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ProfessorDisciplina> getList() {
		return getList(null);
	}

	public static ArrayList<ProfessorDisciplina> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ProfessorDisciplina> list = new ArrayList<ProfessorDisciplina>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ProfessorDisciplina obj = ProfessorDisciplinaDAO.get(rsm.getInt("cd_professor"), rsm.getInt("cd_disciplina"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDisciplinaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_professor_disciplina", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}