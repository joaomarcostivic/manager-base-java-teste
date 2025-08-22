package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class DisciplinaAvaliacaoAlunoDAO{

	public static int insert(DisciplinaAvaliacaoAluno objeto) {
		return insert(objeto, null);
	}

	public static int insert(DisciplinaAvaliacaoAluno objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_disciplina_avaliacao_aluno (cd_oferta_avaliacao,"+
			                                  "cd_oferta,"+
			                                  "vl_conceito,"+
			                                  "vl_conceito_aproveitamento,"+
			                                  "dt_lancamento,"+
			                                  "dt_aplicacao,"+
			                                  "lg_segunda_chamada,"+
			                                  "cd_conceito,"+
			                                  "txt_observacao,"+
			                                  "cd_matricula) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdOfertaAvaliacao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdOfertaAvaliacao());
			if(objeto.getCdOferta()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdOferta());
			pstmt.setDouble(3,objeto.getVlConceito());
			pstmt.setDouble(4,objeto.getVlConceitoAproveitamento());
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			if(objeto.getDtAplicacao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtAplicacao().getTimeInMillis()));
			pstmt.setInt(7,objeto.getLgSegundaChamada());
			if(objeto.getCdConceito()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdConceito());
			pstmt.setString(9,objeto.getTxtObservacao());
			if(objeto.getCdMatricula()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdMatricula());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DisciplinaAvaliacaoAluno objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(DisciplinaAvaliacaoAluno objeto, int cdMatriculaOld, int cdOfertaAvaliacaoOld, int cdOfertaOld) {
		return update(objeto, cdMatriculaOld, cdOfertaAvaliacaoOld, cdOfertaOld, null);
	}

	public static int update(DisciplinaAvaliacaoAluno objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(DisciplinaAvaliacaoAluno objeto, int cdMatriculaOld, int cdOfertaAvaliacaoOld, int cdOfertaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_disciplina_avaliacao_aluno SET cd_oferta_avaliacao=?,"+
												      		   "cd_oferta=?,"+
												      		   "vl_conceito=?,"+
												      		   "vl_conceito_aproveitamento=?,"+
												      		   "dt_lancamento=?,"+
												      		   "dt_aplicacao=?,"+
												      		   "lg_segunda_chamada=?,"+
												      		   "cd_conceito=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_matricula=? WHERE cd_matricula=? AND cd_oferta_avaliacao=? AND cd_oferta=?");
			pstmt.setInt(1,objeto.getCdOfertaAvaliacao());
			pstmt.setInt(2,objeto.getCdOferta());
			pstmt.setDouble(3,objeto.getVlConceito());
			pstmt.setDouble(4,objeto.getVlConceitoAproveitamento());
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			if(objeto.getDtAplicacao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtAplicacao().getTimeInMillis()));
			pstmt.setInt(7,objeto.getLgSegundaChamada());
			if(objeto.getCdConceito()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdConceito());
			pstmt.setString(9,objeto.getTxtObservacao());
			pstmt.setInt(10,objeto.getCdMatricula());
			pstmt.setInt(11, cdMatriculaOld!=0 ? cdMatriculaOld : objeto.getCdMatricula());
			pstmt.setInt(12, cdOfertaAvaliacaoOld!=0 ? cdOfertaAvaliacaoOld : objeto.getCdOfertaAvaliacao());
			pstmt.setDouble(13, cdOfertaOld!=0 ? cdOfertaOld : objeto.getCdOferta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMatricula, int cdOfertaAvaliacao, int cdOferta) {
		return delete(cdMatricula, cdOfertaAvaliacao, cdOferta, null);
	}

	public static int delete(int cdMatricula, int cdOfertaAvaliacao, int cdOferta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_disciplina_avaliacao_aluno WHERE cd_matricula=? AND cd_oferta_avaliacao=? AND cd_oferta=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdOfertaAvaliacao);
			pstmt.setInt(3, cdOferta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DisciplinaAvaliacaoAluno get(int cdMatricula, int cdOfertaAvaliacao, int cdOferta) {
		return get(cdMatricula, cdOfertaAvaliacao, cdOferta, null);
	}

	public static DisciplinaAvaliacaoAluno get(int cdMatriculaDisciplina, int cdOfertaAvaliacao, int cdOferta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_disciplina_avaliacao_aluno WHERE cd_matricula_disciplina=? AND cd_oferta_avaliacao=? AND cd_oferta=?");
			pstmt.setInt(1, cdMatriculaDisciplina);
			pstmt.setInt(2, cdOfertaAvaliacao);
			pstmt.setInt(3, cdOferta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DisciplinaAvaliacaoAluno(rs.getInt("cd_oferta_avaliacao"),
						rs.getInt("cd_oferta"),
						rs.getFloat("vl_conceito"),
						rs.getFloat("vl_conceito_aproveitamento"),
						(rs.getTimestamp("dt_lancamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_lancamento").getTime()),
						(rs.getTimestamp("dt_aplicacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_aplicacao").getTime()),
						rs.getInt("lg_segunda_chamada"),
						rs.getInt("cd_conceito"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_matricula_disciplina"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_disciplina_avaliacao_aluno");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<DisciplinaAvaliacaoAluno> getList() {
		return getList(null);
	}

	public static ArrayList<DisciplinaAvaliacaoAluno> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<DisciplinaAvaliacaoAluno> list = new ArrayList<DisciplinaAvaliacaoAluno>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				DisciplinaAvaliacaoAluno obj = DisciplinaAvaliacaoAlunoDAO.get(rsm.getInt("cd_matricula"), rsm.getInt("cd_oferta_avaliacao"), rsm.getInt("cd_oferta"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_disciplina_avaliacao_aluno", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}