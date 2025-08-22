package com.tivic.manager.acd;

import java.sql.*;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;

import java.util.ArrayList;

public class MatriculaCurriculoDAO{

	public static int insert(MatriculaCurriculo objeto) {
		return insert(objeto, null);
	}

	public static int insert(MatriculaCurriculo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_matricula_curriculo (cd_matricula,"+
			                                  "cd_matriz,"+
			                                  "cd_curso,"+
			                                  "cd_curso_modulo,"+
			                                  "cd_disciplina) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdMatricula()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdMatricula());
			if(objeto.getCdMatriz()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMatriz());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCurso());
			if(objeto.getCdCursoModulo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCursoModulo());
			if(objeto.getCdDisciplina()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdDisciplina());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaCurriculoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaCurriculoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MatriculaCurriculo objeto) {
		return update(objeto, 0, 0, 0, 0, 0, null);
	}

	public static int update(MatriculaCurriculo objeto, int cdMatriculaOld, int cdMatrizOld, int cdCursoOld, int cdCursoModuloOld, int cdDisciplinaOld) {
		return update(objeto, cdMatriculaOld, cdMatrizOld, cdCursoOld, cdCursoModuloOld, cdDisciplinaOld, null);
	}

	public static int update(MatriculaCurriculo objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, 0, connect);
	}

	public static int update(MatriculaCurriculo objeto, int cdMatriculaOld, int cdMatrizOld, int cdCursoOld, int cdCursoModuloOld, int cdDisciplinaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_matricula_curriculo SET cd_matricula=?,"+
												      		   "cd_matriz=?,"+
												      		   "cd_curso=?,"+
												      		   "cd_curso_modulo=?,"+
												      		   "cd_disciplina=? WHERE cd_matricula=? AND cd_matriz=? AND cd_curso=? AND cd_curso_modulo=? AND cd_disciplina=?");
			pstmt.setInt(1,objeto.getCdMatricula());
			pstmt.setInt(2,objeto.getCdMatriz());
			pstmt.setInt(3,objeto.getCdCurso());
			pstmt.setInt(4,objeto.getCdCursoModulo());
			pstmt.setInt(5,objeto.getCdDisciplina());
			pstmt.setInt(6, cdMatriculaOld!=0 ? cdMatriculaOld : objeto.getCdMatricula());
			pstmt.setInt(7, cdMatrizOld!=0 ? cdMatrizOld : objeto.getCdMatriz());
			pstmt.setInt(8, cdCursoOld!=0 ? cdCursoOld : objeto.getCdCurso());
			pstmt.setInt(9, cdCursoModuloOld!=0 ? cdCursoModuloOld : objeto.getCdCursoModulo());
			pstmt.setInt(10, cdDisciplinaOld!=0 ? cdDisciplinaOld : objeto.getCdDisciplina());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaCurriculoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaCurriculoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMatricula, int cdMatriz, int cdCurso, int cdCursoModulo, int cdDisciplina) {
		return delete(cdMatricula, cdMatriz, cdCurso, cdCursoModulo, cdDisciplina, null);
	}

	public static int delete(int cdMatricula, int cdMatriz, int cdCurso, int cdCursoModulo, int cdDisciplina, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_matricula_curriculo WHERE cd_matricula=? AND cd_matriz=? AND cd_curso=? AND cd_curso_modulo=? AND cd_disciplina=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdMatriz);
			pstmt.setInt(3, cdCurso);
			pstmt.setInt(4, cdCursoModulo);
			pstmt.setInt(5, cdDisciplina);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaCurriculoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaCurriculoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MatriculaCurriculo get(int cdMatricula, int cdMatriz, int cdCurso, int cdCursoModulo, int cdDisciplina) {
		return get(cdMatricula, cdMatriz, cdCurso, cdCursoModulo, cdDisciplina, null);
	}

	public static MatriculaCurriculo get(int cdMatricula, int cdMatriz, int cdCurso, int cdCursoModulo, int cdDisciplina, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_curriculo WHERE cd_matricula=? AND cd_matriz=? AND cd_curso=? AND cd_curso_modulo=? AND cd_disciplina=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdMatriz);
			pstmt.setInt(3, cdCurso);
			pstmt.setInt(4, cdCursoModulo);
			pstmt.setInt(5, cdDisciplina);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MatriculaCurriculo(rs.getInt("cd_matricula"),
						rs.getInt("cd_matriz"),
						rs.getInt("cd_curso"),
						rs.getInt("cd_curso_modulo"),
						rs.getInt("cd_disciplina"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaCurriculoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaCurriculoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_curriculo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaCurriculoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaCurriculoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<MatriculaCurriculo> getList() {
		return getList(null);
	}

	public static ArrayList<MatriculaCurriculo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<MatriculaCurriculo> list = new ArrayList<MatriculaCurriculo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				MatriculaCurriculo obj = MatriculaCurriculoDAO.get(rsm.getInt("cd_matricula"), rsm.getInt("cd_matriz"), rsm.getInt("cd_curso"), rsm.getInt("cd_curso_modulo"), rsm.getInt("cd_disciplina"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaCurriculoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_matricula_curriculo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
