package com.tivic.manager.acd;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class GradeConceitoDAO{

	public static int insert(GradeConceito objeto) {
		return insert(objeto, null);
	}

	public static int insert(GradeConceito objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int code = Conexao.getSequenceCode("acd_grade_conceito", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO acd_grade_conceito (cd_conceito,"+
			                                  "cd_curso,"+
			                                  "cd_grade,"+
			                                  "nm_conceito,"+
			                                  "vl_minimo,"+
			                                  "vl_maximo,"+
			                                  "lg_aprovado) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdCurso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCurso());
			if(objeto.getCdGrade()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdGrade());
			pstmt.setString(4,objeto.getNmConceito());
			pstmt.setFloat(5,objeto.getVlMinimo());
			pstmt.setFloat(6,objeto.getVlMaximo());
			pstmt.setInt(7,objeto.getLgAprovado());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GradeConceitoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GradeConceitoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(GradeConceito objeto) {
		return update(objeto, null);
	}

	public static int update(GradeConceito objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE acd_grade_conceito SET cd_curso=?,"+
			                                  "cd_grade=?,"+
			                                  "nm_conceito=?,"+
			                                  "vl_minimo=?,"+
			                                  "vl_maximo=?,"+
			                                  "lg_aprovado=? WHERE cd_conceito=?");
			if(objeto.getCdCurso()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdCurso());
			if(objeto.getCdGrade()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdGrade());
			pstmt.setString(3,objeto.getNmConceito());
			pstmt.setFloat(4,objeto.getVlMinimo());
			pstmt.setFloat(5,objeto.getVlMaximo());
			pstmt.setInt(6,objeto.getLgAprovado());
			pstmt.setInt(7,objeto.getCdConceito());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GradeConceitoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GradeConceitoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConceito) {
		return delete(cdConceito, null);
	}

	public static int delete(int cdConceito, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM acd_grade_conceito WHERE cd_conceito=?");
			pstmt.setInt(1, cdConceito);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GradeConceitoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GradeConceitoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static GradeConceito get(int cdConceito) {
		return get(cdConceito, null);
	}

	public static GradeConceito get(int cdConceito, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_grade_conceito WHERE cd_conceito=?");
			pstmt.setInt(1, cdConceito);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new GradeConceito(rs.getInt("cd_conceito"),
						rs.getInt("cd_curso"),
						rs.getInt("cd_grade"),
						rs.getString("nm_conceito"),
						rs.getFloat("vl_minimo"),
						rs.getFloat("vl_maximo"),
						rs.getInt("lg_aprovado"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GradeConceitoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GradeConceitoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_grade_conceito");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GradeConceitoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GradeConceitoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_grade_conceito", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
