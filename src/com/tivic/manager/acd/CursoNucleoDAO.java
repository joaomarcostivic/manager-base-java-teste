package com.tivic.manager.acd;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class CursoNucleoDAO{

	public static int insert(CursoNucleo objeto) {
		return insert(objeto, null);
	}

	public static int insert(CursoNucleo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_curso_nucleo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdNucleo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_curso_nucleo (cd_nucleo,"+
			                                  "cd_curso,nm_nucleo,vl_carga_horaria,lg_obrigatorio) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdCurso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCurso());
			pstmt.setString(3,objeto.getNmNucleo());
			pstmt.setFloat(4,objeto.getVlCargaHoraria());
			pstmt.setInt(5,objeto.getLgObrigatorio());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CursoNucleo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(CursoNucleo objeto, int cdNucleoOld, int cdCursoOld) {
		return update(objeto, cdNucleoOld, cdCursoOld, null);
	}

	public static int update(CursoNucleo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(CursoNucleo objeto, int cdNucleoOld, int cdCursoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_curso_nucleo SET cd_nucleo=?,"+
												      		   "cd_curso=?,"+
												      		   "nm_nucleo=?,"+
												      		   "vl_carga_horaria=?,"+
												      		   "lg_obrigatorio=? WHERE cd_nucleo=? AND cd_curso=?");
			pstmt.setInt(1,objeto.getCdNucleo());
			pstmt.setInt(2,objeto.getCdCurso());
			pstmt.setString(3,objeto.getNmNucleo());
			pstmt.setFloat(4,objeto.getVlCargaHoraria());
			pstmt.setInt(5,objeto.getLgObrigatorio());
			pstmt.setInt(6, cdNucleoOld!=0 ? cdNucleoOld : objeto.getCdNucleo());
			pstmt.setInt(7, cdCursoOld!=0 ? cdCursoOld : objeto.getCdCurso());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdNucleo, int cdCurso) {
		return delete(cdNucleo, cdCurso, null);
	}

	public static int delete(int cdNucleo, int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_curso_nucleo WHERE cd_nucleo=? AND cd_curso=?");
			pstmt.setInt(1, cdNucleo);
			pstmt.setInt(2, cdCurso);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CursoNucleo get(int cdNucleo, int cdCurso) {
		return get(cdNucleo, cdCurso, null);
	}

	public static CursoNucleo get(int cdNucleo, int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_nucleo WHERE cd_nucleo=? AND cd_curso=?");
			pstmt.setInt(1, cdNucleo);
			pstmt.setInt(2, cdCurso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CursoNucleo(rs.getInt("cd_nucleo"),rs.getInt("cd_curso"), rs.getString("nm_nucleo"),
									   rs.getFloat("vl_carga_horaria"), rs.getInt("lg_obrigatorio"));
			}
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_nucleo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoNucleoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoNucleoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_curso_nucleo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
