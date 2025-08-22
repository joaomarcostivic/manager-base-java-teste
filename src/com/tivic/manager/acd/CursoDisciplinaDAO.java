package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CursoDisciplinaDAO{

	public static int insert(CursoDisciplina objeto) {
		return insert(objeto, null);
	}

	public static int insert(CursoDisciplina objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_curso_disciplina (cd_curso,"+
			                                  "cd_curso_modulo,"+
			                                  "cd_disciplina,"+
			                                  "cd_matriz,"+
			                                  "cd_nucleo,"+
			                                  "qt_carga_horaria,"+
			                                  "nr_ordem,"+
			                                  "tp_classificacao,"+
			                                  "cd_instituicao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdCurso()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdCurso());
			if(objeto.getCdCursoModulo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCursoModulo());
			if(objeto.getCdDisciplina()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdDisciplina());
			if(objeto.getCdMatriz()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdMatriz());
			if(objeto.getCdNucleo()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdNucleo());
			pstmt.setInt(6,objeto.getQtCargaHoraria());
			pstmt.setInt(7,objeto.getNrOrdem());
			pstmt.setInt(8,objeto.getTpClassificacao());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdInstituicao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CursoDisciplina objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(CursoDisciplina objeto, int cdCursoOld, int cdCursoModuloOld, int cdDisciplinaOld, int cdMatrizOld) {
		return update(objeto, cdCursoOld, cdCursoModuloOld, cdDisciplinaOld, cdMatrizOld, null);
	}

	public static int update(CursoDisciplina objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(CursoDisciplina objeto, int cdCursoOld, int cdCursoModuloOld, int cdDisciplinaOld, int cdMatrizOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_curso_disciplina SET cd_curso=?,"+
												      		   "cd_curso_modulo=?,"+
												      		   "cd_disciplina=?,"+
												      		   "cd_matriz=?,"+
												      		   "cd_nucleo=?,"+
												      		   "qt_carga_horaria=?,"+
												      		   "nr_ordem=?,"+
												      		   "tp_classificacao=?,"+
												      		   "cd_instituicao=? WHERE cd_curso=? AND cd_curso_modulo=? AND cd_disciplina=? AND cd_matriz=?");
			pstmt.setInt(1,objeto.getCdCurso());
			pstmt.setInt(2,objeto.getCdCursoModulo());
			pstmt.setInt(3,objeto.getCdDisciplina());
			pstmt.setInt(4,objeto.getCdMatriz());
			if(objeto.getCdNucleo()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdNucleo());
			pstmt.setInt(6,objeto.getQtCargaHoraria());
			pstmt.setInt(7,objeto.getNrOrdem());
			pstmt.setInt(8,objeto.getTpClassificacao());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdInstituicao());
			pstmt.setInt(10, cdCursoOld!=0 ? cdCursoOld : objeto.getCdCurso());
			pstmt.setInt(11, cdCursoModuloOld!=0 ? cdCursoModuloOld : objeto.getCdCursoModulo());
			pstmt.setInt(12, cdDisciplinaOld!=0 ? cdDisciplinaOld : objeto.getCdDisciplina());
			pstmt.setInt(13, cdMatrizOld!=0 ? cdMatrizOld : objeto.getCdMatriz());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCurso, int cdCursoModulo, int cdDisciplina, int cdMatriz) {
		return delete(cdCurso, cdCursoModulo, cdDisciplina, cdMatriz, null);
	}

	public static int delete(int cdCurso, int cdCursoModulo, int cdDisciplina, int cdMatriz, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_curso_disciplina WHERE cd_curso=? AND cd_curso_modulo=? AND cd_disciplina=? AND cd_matriz=?");
			pstmt.setInt(1, cdCurso);
			pstmt.setInt(2, cdCursoModulo);
			pstmt.setInt(3, cdDisciplina);
			pstmt.setInt(4, cdMatriz);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CursoDisciplina get(int cdCurso, int cdCursoModulo, int cdDisciplina, int cdMatriz) {
		return get(cdCurso, cdCursoModulo, cdDisciplina, cdMatriz, null);
	}

	public static CursoDisciplina get(int cdCurso, int cdCursoModulo, int cdDisciplina, int cdMatriz, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_disciplina WHERE cd_curso=? AND cd_curso_modulo=? AND cd_disciplina=? AND cd_matriz=?");
			pstmt.setInt(1, cdCurso);
			pstmt.setInt(2, cdCursoModulo);
			pstmt.setInt(3, cdDisciplina);
			pstmt.setInt(4, cdMatriz);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CursoDisciplina(rs.getInt("cd_curso"),
						rs.getInt("cd_curso_modulo"),
						rs.getInt("cd_disciplina"),
						rs.getInt("cd_matriz"),
						rs.getInt("cd_nucleo"),
						rs.getInt("qt_carga_horaria"),
						rs.getInt("nr_ordem"),
						rs.getInt("tp_classificacao"),
						rs.getInt("cd_instituicao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_disciplina");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CursoDisciplina> getList() {
		return getList(null);
	}

	public static ArrayList<CursoDisciplina> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CursoDisciplina> list = new ArrayList<CursoDisciplina>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CursoDisciplina obj = CursoDisciplinaDAO.get(rsm.getInt("cd_curso"), rsm.getInt("cd_curso_modulo"), rsm.getInt("cd_disciplina"), rsm.getInt("cd_matriz"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_curso_disciplina", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
