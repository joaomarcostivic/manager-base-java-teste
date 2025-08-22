package com.tivic.manager.acd;

import java.sql.*;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;

import java.util.ArrayList;

public class MatriculaDisciplinaDAO{

	public static int insert(MatriculaDisciplina objeto) {
		return insert(objeto, null);
	}

	public static int insert(MatriculaDisciplina objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_matricula_disciplina", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMatriculaDisciplina(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_matricula_disciplina (cd_matricula_disciplina,"+
			                                  "cd_matricula,"+
			                                  "cd_periodo_letivo,"+
			                                  "cd_conceito,"+
			                                  "dt_inicio,"+
			                                  "dt_conclusao,"+
			                                  "nr_faltas,"+
			                                  "tp_matricula,"+
			                                  "vl_conceito,"+
			                                  "qt_ch_complemento,"+
			                                  "vl_conceito_aproveitamento,"+
			                                  "nm_institiuicao_aproveitamento,"+
			                                  "st_matricula_disciplina,"+
			                                  "cd_professor,"+
			                                  "cd_supervisor_pratica,"+
			                                  "cd_instituicao_pratica,"+
			                                  "cd_matriz,"+
			                                  "cd_curso,"+
			                                  "cd_curso_modulo,"+
			                                  "cd_disciplina,"+
			                                  "cd_oferta,"+
			                                  "lg_aprovado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdMatricula()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMatricula());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			if(objeto.getCdConceito()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdConceito());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			if(objeto.getDtConclusao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtConclusao().getTimeInMillis()));
			pstmt.setInt(7,objeto.getNrFaltas());
			pstmt.setInt(8,objeto.getTpMatricula());
			pstmt.setFloat(9,objeto.getVlConceito());
			pstmt.setInt(10,objeto.getQtChComplemento());
			pstmt.setFloat(11,objeto.getVlConceitoAproveitamento());
			pstmt.setString(12,objeto.getNmInstitiuicaoAproveitamento());
			pstmt.setInt(13,objeto.getStMatriculaDisciplina());
			if(objeto.getCdProfessor()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdProfessor());
			if(objeto.getCdSupervisorPratica()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdSupervisorPratica());
			if(objeto.getCdInstituicaoPratica()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdInstituicaoPratica());
			if(objeto.getCdMatriz()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdMatriz());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdCurso());
			if(objeto.getCdCursoModulo()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdCursoModulo());
			if(objeto.getCdDisciplina()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdDisciplina());
			if(objeto.getCdOferta()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdOferta());
			pstmt.setInt(22,objeto.getLgAprovado());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MatriculaDisciplina objeto) {
		return update(objeto, 0, null);
	}

	public static int update(MatriculaDisciplina objeto, int cdMatriculaDisciplinaOld) {
		return update(objeto, cdMatriculaDisciplinaOld, null);
	}

	public static int update(MatriculaDisciplina objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(MatriculaDisciplina objeto, int cdMatriculaDisciplinaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_matricula_disciplina SET cd_matricula_disciplina=?,"+
												      		   "cd_matricula=?,"+
												      		   "cd_periodo_letivo=?,"+
												      		   "cd_conceito=?,"+
												      		   "dt_inicio=?,"+
												      		   "dt_conclusao=?,"+
												      		   "nr_faltas=?,"+
												      		   "tp_matricula=?,"+
												      		   "vl_conceito=?,"+
												      		   "qt_ch_complemento=?,"+
												      		   "vl_conceito_aproveitamento=?,"+
												      		   "nm_institiuicao_aproveitamento=?,"+
												      		   "st_matricula_disciplina=?,"+
												      		   "cd_professor=?,"+
												      		   "cd_supervisor_pratica=?,"+
												      		   "cd_instituicao_pratica=?,"+
												      		   "cd_matriz=?,"+
												      		   "cd_curso=?,"+
												      		   "cd_curso_modulo=?,"+
												      		   "cd_disciplina=?,"+
												      		   "cd_oferta=?,"+
												      		   "lg_aprovado=? WHERE cd_matricula_disciplina=?");
			pstmt.setInt(1,objeto.getCdMatriculaDisciplina());
			if(objeto.getCdMatricula()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMatricula());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			if(objeto.getCdConceito()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdConceito());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			if(objeto.getDtConclusao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtConclusao().getTimeInMillis()));
			pstmt.setInt(7,objeto.getNrFaltas());
			pstmt.setInt(8,objeto.getTpMatricula());
			pstmt.setFloat(9,objeto.getVlConceito());
			pstmt.setInt(10,objeto.getQtChComplemento());
			pstmt.setFloat(11,objeto.getVlConceitoAproveitamento());
			pstmt.setString(12,objeto.getNmInstitiuicaoAproveitamento());
			pstmt.setInt(13,objeto.getStMatriculaDisciplina());
			if(objeto.getCdProfessor()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdProfessor());
			if(objeto.getCdSupervisorPratica()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdSupervisorPratica());
			if(objeto.getCdInstituicaoPratica()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdInstituicaoPratica());
			if(objeto.getCdMatriz()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdMatriz());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdCurso());
			if(objeto.getCdCursoModulo()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdCursoModulo());
			if(objeto.getCdDisciplina()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdDisciplina());
			if(objeto.getCdOferta()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdOferta());
			pstmt.setInt(22,objeto.getLgAprovado());
			pstmt.setInt(23, cdMatriculaDisciplinaOld!=0 ? cdMatriculaDisciplinaOld : objeto.getCdMatriculaDisciplina());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMatriculaDisciplina) {
		return delete(cdMatriculaDisciplina, null);
	}

	public static int delete(int cdMatriculaDisciplina, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_matricula_disciplina WHERE cd_matricula_disciplina=?");
			pstmt.setInt(1, cdMatriculaDisciplina);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MatriculaDisciplina get(int cdMatriculaDisciplina) {
		return get(cdMatriculaDisciplina, null);
	}

	public static MatriculaDisciplina get(int cdMatriculaDisciplina, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_disciplina WHERE cd_matricula_disciplina=?");
			pstmt.setInt(1, cdMatriculaDisciplina);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MatriculaDisciplina(rs.getInt("cd_matricula_disciplina"),
						rs.getInt("cd_matricula"),
						rs.getInt("cd_periodo_letivo"),
						rs.getInt("cd_conceito"),
						(rs.getTimestamp("dt_inicio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio").getTime()),
						(rs.getTimestamp("dt_conclusao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_conclusao").getTime()),
						rs.getInt("nr_faltas"),
						rs.getInt("tp_matricula"),
						rs.getFloat("vl_conceito"),
						rs.getInt("qt_ch_complemento"),
						rs.getFloat("vl_conceito_aproveitamento"),
						rs.getString("nm_institiuicao_aproveitamento"),
						rs.getInt("st_matricula_disciplina"),
						rs.getInt("cd_professor"),
						rs.getInt("cd_supervisor_pratica"),
						rs.getInt("cd_instituicao_pratica"),
						rs.getInt("cd_matriz"),
						rs.getInt("cd_curso"),
						rs.getInt("cd_curso_modulo"),
						rs.getInt("cd_disciplina"),
						rs.getInt("cd_oferta"),
						rs.getInt("lg_aprovado"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static MatriculaDisciplina getDisciplinasByMatricula(int cdMatriculaDisciplina) {
		return get(cdMatriculaDisciplina, null);
	}

	public static ArrayList<MatriculaDisciplina> getDisciplinasByMatricula(int cdMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			
			ArrayList<MatriculaDisciplina> matriculasDisciplina = new ArrayList<MatriculaDisciplina>();
			
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_disciplina WHERE cd_matricula=?");
			pstmt.setInt(1, cdMatricula);
			rs = pstmt.executeQuery();
			while(rs.next()){
				matriculasDisciplina.add(MatriculaDisciplinaDAO.get(rs.getInt("cd_matricula_disciplina")));
			}
			
			return matriculasDisciplina;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_disciplina");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<MatriculaDisciplina> getList() {
		return getList(null);
	}

	public static ArrayList<MatriculaDisciplina> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<MatriculaDisciplina> list = new ArrayList<MatriculaDisciplina>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				MatriculaDisciplina obj = MatriculaDisciplinaDAO.get(rsm.getInt("cd_matricula_disciplina"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_matricula_disciplina", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
