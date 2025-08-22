package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CursoDisciplinaRegraDAO{

	public static int insert(CursoDisciplinaRegra objeto) {
		return insert(objeto, null);
	}

	public static int insert(CursoDisciplinaRegra objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_curso_disciplina_regra (cd_curso,"+
			                                  "cd_disciplina,"+
			                                  "tp_permissao) VALUES (?, ?, ?)");
			if(objeto.getCdCurso()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdCurso());
			if(objeto.getCdDisciplina()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDisciplina());
			pstmt.setInt(3,objeto.getTpPermissao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaRegraDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaRegraDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CursoDisciplinaRegra objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(CursoDisciplinaRegra objeto, int cdCursoOld, int cdDisciplinaOld) {
		return update(objeto, cdCursoOld, cdDisciplinaOld, null);
	}

	public static int update(CursoDisciplinaRegra objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(CursoDisciplinaRegra objeto, int cdCursoOld, int cdDisciplinaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_curso_disciplina_regra SET cd_curso=?,"+
												      		   "cd_disciplina=?,"+
												      		   "tp_permissao=? WHERE cd_curso=? AND cd_disciplina=?");
			pstmt.setInt(1,objeto.getCdCurso());
			pstmt.setInt(2,objeto.getCdDisciplina());
			pstmt.setInt(3,objeto.getTpPermissao());
			pstmt.setInt(4, cdCursoOld!=0 ? cdCursoOld : objeto.getCdCurso());
			pstmt.setInt(5, cdDisciplinaOld!=0 ? cdDisciplinaOld : objeto.getCdDisciplina());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaRegraDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaRegraDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCurso, int cdDisciplina) {
		return delete(cdCurso, cdDisciplina, null);
	}

	public static int delete(int cdCurso, int cdDisciplina, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_curso_disciplina_regra WHERE cd_curso=? AND cd_disciplina=?");
			pstmt.setInt(1, cdCurso);
			pstmt.setInt(2, cdDisciplina);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaRegraDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaRegraDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CursoDisciplinaRegra get(int cdCurso, int cdDisciplina) {
		return get(cdCurso, cdDisciplina, null);
	}

	public static CursoDisciplinaRegra get(int cdCurso, int cdDisciplina, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_disciplina_regra WHERE cd_curso=? AND cd_disciplina=?");
			pstmt.setInt(1, cdCurso);
			pstmt.setInt(2, cdDisciplina);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CursoDisciplinaRegra(rs.getInt("cd_curso"),
						rs.getInt("cd_disciplina"),
						rs.getInt("tp_permissao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaRegraDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaRegraDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_disciplina_regra");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaRegraDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaRegraDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CursoDisciplinaRegra> getList() {
		return getList(null);
	}

	public static ArrayList<CursoDisciplinaRegra> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CursoDisciplinaRegra> list = new ArrayList<CursoDisciplinaRegra>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CursoDisciplinaRegra obj = CursoDisciplinaRegraDAO.get(rsm.getInt("cd_curso"), rsm.getInt("cd_disciplina"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaRegraDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_curso_disciplina_regra", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
