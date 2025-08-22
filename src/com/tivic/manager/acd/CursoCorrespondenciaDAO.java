package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CursoCorrespondenciaDAO{

	public static int insert(CursoCorrespondencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(CursoCorrespondencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_curso_correspondencia (cd_curso,"+
			                                  "cd_curso_correspondencia) VALUES (?, ?)");
			if(objeto.getCdCurso()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdCurso());
			if(objeto.getCdCursoCorrespondencia()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCursoCorrespondencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoCorrespondenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoCorrespondenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CursoCorrespondencia objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(CursoCorrespondencia objeto, int cdCursoOld, int cdCursoCorrespondenciaOld) {
		return update(objeto, cdCursoOld, cdCursoCorrespondenciaOld, null);
	}

	public static int update(CursoCorrespondencia objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(CursoCorrespondencia objeto, int cdCursoOld, int cdCursoCorrespondenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_curso_correspondencia SET cd_curso=?,"+
												      		   "cd_curso_correspondencia=? WHERE cd_curso=? AND cd_curso_correspondencia=?");
			pstmt.setInt(1,objeto.getCdCurso());
			pstmt.setInt(2,objeto.getCdCursoCorrespondencia());
			pstmt.setInt(3, cdCursoOld!=0 ? cdCursoOld : objeto.getCdCurso());
			pstmt.setInt(4, cdCursoCorrespondenciaOld!=0 ? cdCursoCorrespondenciaOld : objeto.getCdCursoCorrespondencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoCorrespondenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoCorrespondenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCurso, int cdCursoCorrespondencia) {
		return delete(cdCurso, cdCursoCorrespondencia, null);
	}

	public static int delete(int cdCurso, int cdCursoCorrespondencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_curso_correspondencia WHERE cd_curso=? AND cd_curso_correspondencia=?");
			pstmt.setInt(1, cdCurso);
			pstmt.setInt(2, cdCursoCorrespondencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoCorrespondenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoCorrespondenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CursoCorrespondencia get(int cdCurso, int cdCursoCorrespondencia) {
		return get(cdCurso, cdCursoCorrespondencia, null);
	}

	public static CursoCorrespondencia get(int cdCurso, int cdCursoCorrespondencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_correspondencia WHERE cd_curso=? AND cd_curso_correspondencia=?");
			pstmt.setInt(1, cdCurso);
			pstmt.setInt(2, cdCursoCorrespondencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CursoCorrespondencia(rs.getInt("cd_curso"),
						rs.getInt("cd_curso_correspondencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoCorrespondenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoCorrespondenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_correspondencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoCorrespondenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoCorrespondenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CursoCorrespondencia> getList() {
		return getList(null);
	}

	public static ArrayList<CursoCorrespondencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CursoCorrespondencia> list = new ArrayList<CursoCorrespondencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CursoCorrespondencia obj = CursoCorrespondenciaDAO.get(rsm.getInt("cd_curso"), rsm.getInt("cd_curso_correspondencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoCorrespondenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_curso_correspondencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}