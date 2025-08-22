package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class MatriculaModuloDAO{

	public static int insert(MatriculaModulo objeto) {
		return insert(objeto, null);
	}

	public static int insert(MatriculaModulo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_matricula_modulo (cd_matricula,"+
			                                  "cd_curso_modulo,"+
			                                  "cd_periodo_letivo) VALUES (?, ?, ?)");
			if(objeto.getCdMatricula()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdMatricula());
			if(objeto.getCdCursoModulo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCursoModulo());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaModuloDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaModuloDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MatriculaModulo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(MatriculaModulo objeto, int cdMatriculaOld, int cdCursoModuloOld) {
		return update(objeto, cdMatriculaOld, cdCursoModuloOld, null);
	}

	public static int update(MatriculaModulo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(MatriculaModulo objeto, int cdMatriculaOld, int cdCursoModuloOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_matricula_modulo SET cd_matricula=?,"+
												      		   "cd_curso_modulo=?,"+
												      		   "cd_periodo_letivo=? WHERE cd_matricula=? AND cd_curso_modulo=?");
			pstmt.setInt(1,objeto.getCdMatricula());
			pstmt.setInt(2,objeto.getCdCursoModulo());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			pstmt.setInt(4, cdMatriculaOld!=0 ? cdMatriculaOld : objeto.getCdMatricula());
			pstmt.setInt(5, cdCursoModuloOld!=0 ? cdCursoModuloOld : objeto.getCdCursoModulo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaModuloDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaModuloDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMatricula, int cdCursoModulo) {
		return delete(cdMatricula, cdCursoModulo, null);
	}

	public static int delete(int cdMatricula, int cdCursoModulo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_matricula_modulo WHERE cd_matricula=? AND cd_curso_modulo=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdCursoModulo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaModuloDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaModuloDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MatriculaModulo get(int cdMatricula, int cdCursoModulo) {
		return get(cdMatricula, cdCursoModulo, null);
	}

	public static MatriculaModulo get(int cdMatricula, int cdCursoModulo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_modulo WHERE cd_matricula=? AND cd_curso_modulo=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdCursoModulo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MatriculaModulo(rs.getInt("cd_matricula"),
						rs.getInt("cd_curso_modulo"),
						rs.getInt("cd_periodo_letivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaModuloDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaModuloDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_modulo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaModuloDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaModuloDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<MatriculaModulo> getList() {
		return getList(null);
	}

	public static ArrayList<MatriculaModulo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<MatriculaModulo> list = new ArrayList<MatriculaModulo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				MatriculaModulo obj = MatriculaModuloDAO.get(rsm.getInt("cd_matricula"), rsm.getInt("cd_curso_modulo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaModuloDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_matricula_modulo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
