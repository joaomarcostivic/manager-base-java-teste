package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class FormacaoCursoDAO{

	public static int insert(FormacaoCurso objeto) {
		return insert(objeto, null);
	}

	public static int insert(FormacaoCurso objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_formacao_curso", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFormacaoCurso(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_formacao_curso (cd_formacao_curso,"+
			                                  "cd_curso_superior,"+
			                                  "nm_curso,"+
			                                  "id_ocde,"+
			                                  "tp_grau_academico,"+
			                                  "cd_formacao_area_conhecimento) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdCursoSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCursoSuperior());
			pstmt.setString(3,objeto.getNmCurso());
			pstmt.setString(4,objeto.getIdOcde());
			pstmt.setInt(5,objeto.getTpGrauAcademico());
			if(objeto.getCdFormacaoAreaConhecimento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdFormacaoAreaConhecimento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoCursoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoCursoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FormacaoCurso objeto) {
		return update(objeto, 0, null);
	}

	public static int update(FormacaoCurso objeto, int cdFormacaoCursoOld) {
		return update(objeto, cdFormacaoCursoOld, null);
	}

	public static int update(FormacaoCurso objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(FormacaoCurso objeto, int cdFormacaoCursoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_formacao_curso SET cd_formacao_curso=?,"+
												      		   "cd_curso_superior=?,"+
												      		   "nm_curso=?,"+
												      		   "id_ocde=?,"+
												      		   "tp_grau_academico=?,"+
												      		   "cd_formacao_area_conhecimento=? WHERE cd_formacao_curso=?");
			pstmt.setInt(1,objeto.getCdFormacaoCurso());
			if(objeto.getCdCursoSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCursoSuperior());
			pstmt.setString(3,objeto.getNmCurso());
			pstmt.setString(4,objeto.getIdOcde());
			pstmt.setInt(5,objeto.getTpGrauAcademico());
			if(objeto.getCdFormacaoAreaConhecimento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdFormacaoAreaConhecimento());
			pstmt.setInt(7, cdFormacaoCursoOld!=0 ? cdFormacaoCursoOld : objeto.getCdFormacaoCurso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoCursoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoCursoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFormacaoCurso) {
		return delete(cdFormacaoCurso, null);
	}

	public static int delete(int cdFormacaoCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_formacao_curso WHERE cd_formacao_curso=?");
			pstmt.setInt(1, cdFormacaoCurso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoCursoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoCursoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FormacaoCurso get(int cdFormacaoCurso) {
		return get(cdFormacaoCurso, null);
	}

	public static FormacaoCurso get(int cdFormacaoCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_formacao_curso WHERE cd_formacao_curso=?");
			pstmt.setInt(1, cdFormacaoCurso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FormacaoCurso(rs.getInt("cd_formacao_curso"),
						rs.getInt("cd_curso_superior"),
						rs.getString("nm_curso"),
						rs.getString("id_ocde"),
						rs.getInt("tp_grau_academico"),
						rs.getInt("cd_formacao_area_conhecimento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoCursoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoCursoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_formacao_curso");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoCursoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoCursoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<FormacaoCurso> getList() {
		return getList(null);
	}

	public static ArrayList<FormacaoCurso> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<FormacaoCurso> list = new ArrayList<FormacaoCurso>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				FormacaoCurso obj = FormacaoCursoDAO.get(rsm.getInt("cd_formacao_curso"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoCursoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_formacao_curso", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
