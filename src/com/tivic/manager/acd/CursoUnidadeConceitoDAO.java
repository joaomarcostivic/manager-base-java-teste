package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class CursoUnidadeConceitoDAO{

	public static int insert(CursoUnidadeConceito objeto) {
		return insert(objeto, null);
	}

	public static int insert(CursoUnidadeConceito objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_curso_unidade_conceito (cd_unidade,"+
			                                  "cd_curso,"+
			                                  "cd_matricula_disciplina,"+
			                                  "vl_conceito,"+
			                                  "vl_conceito_aproveitamento,"+
			                                  "dt_lancamento,"+
			                                  "dt_resultado,"+
			                                  "cd_conceito,"+
			                                  "txt_observacao,"+
			                                  "lg_aprovado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdUnidade()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdUnidade());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCurso());
			if(objeto.getCdMatriculaDisciplina()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdMatriculaDisciplina());
			pstmt.setDouble(4,objeto.getVlConceito());
			pstmt.setDouble(5,objeto.getVlConceitoAproveitamento());
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			if(objeto.getDtResultado()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtResultado().getTimeInMillis()));
			if(objeto.getCdConceito()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdConceito());
			pstmt.setString(9,objeto.getTxtObservacao());
			pstmt.setInt(10,objeto.getLgAprovado());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeConceitoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeConceitoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CursoUnidadeConceito objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(CursoUnidadeConceito objeto, int cdUnidadeOld, int cdCursoOld, int cdMatriculaDisciplinaOld) {
		return update(objeto, cdUnidadeOld, cdCursoOld, cdMatriculaDisciplinaOld, null);
	}

	public static int update(CursoUnidadeConceito objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(CursoUnidadeConceito objeto, int cdUnidadeOld, int cdCursoOld, int cdMatriculaDisciplinaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_curso_unidade_conceito SET cd_unidade=?,"+
												      		   "cd_curso=?,"+
												      		   "cd_matricula_disciplina=?,"+
												      		   "vl_conceito=?,"+
												      		   "vl_conceito_aproveitamento=?,"+
												      		   "dt_lancamento=?,"+
												      		   "dt_resultado=?,"+
												      		   "cd_conceito=?,"+
												      		   "txt_observacao=?,"+
												      		   "lg_aprovado=? WHERE cd_unidade=? AND cd_curso=? AND cd_matricula_disciplina=?");
			pstmt.setInt(1,objeto.getCdUnidade());
			pstmt.setInt(2,objeto.getCdCurso());
			pstmt.setInt(3,objeto.getCdMatriculaDisciplina());
			pstmt.setDouble(4,objeto.getVlConceito());
			pstmt.setDouble(5,objeto.getVlConceitoAproveitamento());
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			if(objeto.getDtResultado()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtResultado().getTimeInMillis()));
			if(objeto.getCdConceito()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdConceito());
			pstmt.setString(9,objeto.getTxtObservacao());
			pstmt.setInt(10,objeto.getLgAprovado());
			pstmt.setInt(11, cdUnidadeOld!=0 ? cdUnidadeOld : objeto.getCdUnidade());
			pstmt.setInt(12, cdCursoOld!=0 ? cdCursoOld : objeto.getCdCurso());
			pstmt.setInt(13, cdMatriculaDisciplinaOld!=0 ? cdMatriculaDisciplinaOld : objeto.getCdMatriculaDisciplina());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeConceitoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeConceitoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdUnidade, int cdCurso, int cdMatriculaDisciplina) {
		return delete(cdUnidade, cdCurso, cdMatriculaDisciplina, null);
	}

	public static int delete(int cdUnidade, int cdCurso, int cdMatriculaDisciplina, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_curso_unidade_conceito WHERE cd_unidade=? AND cd_curso=? AND cd_matricula_disciplina=?");
			pstmt.setInt(1, cdUnidade);
			pstmt.setInt(2, cdCurso);
			pstmt.setInt(3, cdMatriculaDisciplina);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeConceitoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeConceitoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CursoUnidadeConceito get(int cdUnidade, int cdCurso, int cdMatriculaDisciplina) {
		return get(cdUnidade, cdCurso, cdMatriculaDisciplina, null);
	}

	public static CursoUnidadeConceito get(int cdUnidade, int cdCurso, int cdMatriculaDisciplina, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_unidade_conceito WHERE cd_unidade=? AND cd_curso=? AND cd_matricula_disciplina=?");
			pstmt.setInt(1, cdUnidade);
			pstmt.setInt(2, cdCurso);
			pstmt.setInt(3, cdMatriculaDisciplina);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CursoUnidadeConceito(rs.getInt("cd_unidade"),
						rs.getInt("cd_curso"),
						rs.getInt("cd_matricula_disciplina"),
						rs.getDouble("vl_conceito"),
						rs.getDouble("vl_conceito_aproveitamento"),
						(rs.getTimestamp("dt_lancamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_lancamento").getTime()),
						(rs.getTimestamp("dt_resultado")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_resultado").getTime()),
						rs.getInt("cd_conceito"),
						rs.getString("txt_observacao"),
						rs.getInt("lg_aprovado"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeConceitoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeConceitoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_unidade_conceito");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeConceitoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeConceitoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CursoUnidadeConceito> getList() {
		return getList(null);
	}

	public static ArrayList<CursoUnidadeConceito> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CursoUnidadeConceito> list = new ArrayList<CursoUnidadeConceito>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CursoUnidadeConceito obj = CursoUnidadeConceitoDAO.get(rsm.getInt("cd_unidade"), rsm.getInt("cd_curso"), rsm.getInt("cd_matricula_disciplina"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeConceitoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_curso_unidade_conceito", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}