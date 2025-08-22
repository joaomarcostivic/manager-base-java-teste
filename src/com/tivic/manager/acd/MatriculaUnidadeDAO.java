package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class MatriculaUnidadeDAO{

	public static int insert(MatriculaUnidade objeto) {
		return insert(objeto, null);
	}

	public static int insert(MatriculaUnidade objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_matricula_unidade (cd_matricula,"+
			                                  "cd_unidade,"+
			                                  "cd_curso,"+
			                                  "txt_parecer,"+
			                                  "dt_parecer) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdMatricula()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdMatricula());
			if(objeto.getCdUnidade()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdUnidade());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCurso());
			pstmt.setString(4,objeto.getTxtParecer());
			if(objeto.getDtParecer()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtParecer().getTimeInMillis()));
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaUnidadeDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaUnidadeDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MatriculaUnidade objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(MatriculaUnidade objeto, int cdMatriculaOld, int cdUnidadeOld, int cdCursoOld) {
		return update(objeto, cdMatriculaOld, cdUnidadeOld, cdCursoOld, null);
	}

	public static int update(MatriculaUnidade objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(MatriculaUnidade objeto, int cdMatriculaOld, int cdUnidadeOld, int cdCursoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_matricula_unidade SET cd_matricula=?,"+
												      		   "cd_unidade=?,"+
												      		   "cd_curso=?,"+
												      		   "txt_parecer=?,"+
												      		   "dt_parecer=? WHERE cd_matricula=? AND cd_unidade=? AND cd_curso=?");
			pstmt.setInt(1,objeto.getCdMatricula());
			pstmt.setInt(2,objeto.getCdUnidade());
			pstmt.setInt(3,objeto.getCdCurso());
			pstmt.setString(4,objeto.getTxtParecer());
			if(objeto.getDtParecer()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtParecer().getTimeInMillis()));
			pstmt.setInt(6, cdMatriculaOld!=0 ? cdMatriculaOld : objeto.getCdMatricula());
			pstmt.setInt(7, cdUnidadeOld!=0 ? cdUnidadeOld : objeto.getCdUnidade());
			pstmt.setInt(8, cdCursoOld!=0 ? cdCursoOld : objeto.getCdCurso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaUnidadeDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaUnidadeDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMatricula, int cdUnidade, int cdCurso) {
		return delete(cdMatricula, cdUnidade, cdCurso, null);
	}

	public static int delete(int cdMatricula, int cdUnidade, int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_matricula_unidade WHERE cd_matricula=? AND cd_unidade=? AND cd_curso=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdUnidade);
			pstmt.setInt(3, cdCurso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaUnidadeDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaUnidadeDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MatriculaUnidade get(int cdMatricula, int cdUnidade, int cdCurso) {
		return get(cdMatricula, cdUnidade, cdCurso, null);
	}

	public static MatriculaUnidade get(int cdMatricula, int cdUnidade, int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_unidade WHERE cd_matricula=? AND cd_unidade=? AND cd_curso=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdUnidade);
			pstmt.setInt(3, cdCurso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MatriculaUnidade(rs.getInt("cd_matricula"),
						rs.getInt("cd_unidade"),
						rs.getInt("cd_curso"),
						rs.getString("txt_parecer"),
						(rs.getTimestamp("dt_parecer")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_parecer").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaUnidadeDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaUnidadeDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static MatriculaUnidade getParecer(int cdMatricula, int cdUnidade, int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_unidade WHERE cd_matricula=? AND cd_unidade=? AND cd_curso=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdUnidade);
			pstmt.setInt(3, cdCurso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MatriculaUnidade(rs.getInt("cd_matricula"),
						rs.getInt("cd_unidade"),
						rs.getInt("cd_curso"),
						rs.getString("txt_parecer"),
						(rs.getTimestamp("dt_parecer")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_parecer").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaUnidadeDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaUnidadeDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_unidade");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaUnidadeDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaUnidadeDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<MatriculaUnidade> getList() {
		return getList(null);
	}

	public static ArrayList<MatriculaUnidade> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<MatriculaUnidade> list = new ArrayList<MatriculaUnidade>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				MatriculaUnidade obj = MatriculaUnidadeDAO.get(rsm.getInt("cd_matricula"), rsm.getInt("cd_unidade"), rsm.getInt("cd_curso"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaUnidadeDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_matricula_unidade", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}