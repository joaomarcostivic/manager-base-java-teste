package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class PlanoDAO{

	public static int insert(Plano objeto) {
		return insert(objeto, null);
	}

	public static int insert(Plano objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_plano", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPlano(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_plano (cd_plano,"+
			                                  "nm_plano,"+
			                                  "tp_plano,"+
			                                  "cd_instituicao,"+
			                                  "cd_periodo_letivo,"+
			                                  "cd_curso,"+
			                                  "cd_turma,"+
			                                  "cd_disciplina,"+
			                                  "cd_professor,"+
			                                  "st_plano,"+
			                                  "lg_compartilhado,"+
			                                  "qt_carga_horaria) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmPlano());
			pstmt.setInt(3,objeto.getTpPlano());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdInstituicao());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdPeriodoLetivo());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCurso());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTurma());
			if(objeto.getCdDisciplina()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdDisciplina());
			if(objeto.getCdProfessor()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdProfessor());
			pstmt.setInt(10,objeto.getStPlano());
			pstmt.setInt(11,objeto.getLgCompartilhado());
			pstmt.setInt(12,objeto.getQtCargaHoraria());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Plano objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Plano objeto, int cdPlanoOld) {
		return update(objeto, cdPlanoOld, null);
	}

	public static int update(Plano objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Plano objeto, int cdPlanoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_plano SET cd_plano=?,"+
												      		   "nm_plano=?,"+
												      		   "tp_plano=?,"+
												      		   "cd_instituicao=?,"+
												      		   "cd_periodo_letivo=?,"+
												      		   "cd_curso=?,"+
												      		   "cd_turma=?,"+
												      		   "cd_disciplina=?,"+
												      		   "cd_professor=?,"+
												      		   "st_plano=?,"+
												      		   "lg_compartilhado=?,"+
												      		   "qt_carga_horaria=? WHERE cd_plano=?");
			pstmt.setInt(1,objeto.getCdPlano());
			pstmt.setString(2,objeto.getNmPlano());
			pstmt.setInt(3,objeto.getTpPlano());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdInstituicao());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdPeriodoLetivo());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCurso());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTurma());
			if(objeto.getCdDisciplina()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdDisciplina());
			if(objeto.getCdProfessor()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdProfessor());
			pstmt.setInt(10,objeto.getStPlano());
			pstmt.setInt(11,objeto.getLgCompartilhado());
			pstmt.setFloat(12,objeto.getQtCargaHoraria());
			pstmt.setInt(13, cdPlanoOld!=0 ? cdPlanoOld : objeto.getCdPlano());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPlano) {
		return delete(cdPlano, null);
	}

	public static int delete(int cdPlano, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_plano WHERE cd_plano=?");
			pstmt.setInt(1, cdPlano);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Plano get(int cdPlano) {
		return get(cdPlano, null);
	}

	public static Plano get(int cdPlano, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_plano WHERE cd_plano=?");
			pstmt.setInt(1, cdPlano);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Plano(rs.getInt("cd_plano"),
						rs.getString("nm_plano"),
						rs.getInt("tp_plano"),
						rs.getInt("cd_instituicao"),
						rs.getInt("cd_periodo_letivo"),
						rs.getInt("cd_curso"),
						rs.getInt("cd_turma"),
						rs.getInt("cd_disciplina"),
						rs.getInt("cd_professor"),
						rs.getInt("st_plano"),
						rs.getInt("lg_compartilhado"),
						rs.getInt("qt_carga_horaria"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_plano");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Plano> getList() {
		return getList(null);
	}

	public static ArrayList<Plano> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Plano> list = new ArrayList<Plano>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Plano obj = PlanoDAO.get(rsm.getInt("cd_plano"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_plano", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
