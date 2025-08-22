package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class DisciplinaAvaliacaoAlunoQuestaoDAO{

	public static int insert(DisciplinaAvaliacaoAlunoQuestao objeto) {
		return insert(objeto, null);
	}

	public static int insert(DisciplinaAvaliacaoAlunoQuestao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_disciplina_avaliacao_aluno_questao (cd_matricula_disciplina,"+
			                                  "cd_oferta_avaliacao_questao,"+
			                                  "cd_oferta_avaliacao,"+
			                                  "cd_oferta,"+
			                                  "cd_alternativa,"+
			                                  "txt_resposta) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdMatriculaDisciplina()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdMatriculaDisciplina());
			if(objeto.getCdOfertaAvaliacaoQuestao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdOfertaAvaliacaoQuestao());
			if(objeto.getCdOfertaAvaliacao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdOfertaAvaliacao());
			if(objeto.getCdOferta()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdOferta());
			if(objeto.getCdAlternativa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdAlternativa());
			pstmt.setString(6,objeto.getTxtResposta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoQuestaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoQuestaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DisciplinaAvaliacaoAlunoQuestao objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(DisciplinaAvaliacaoAlunoQuestao objeto, int cdMatriculaDisciplinaOld, int cdOfertaAvaliacaoQuestaoOld, int cdOfertaAvaliacaoOld, int cdOfertaOld) {
		return update(objeto, cdMatriculaDisciplinaOld, cdOfertaAvaliacaoQuestaoOld, cdOfertaAvaliacaoOld, cdOfertaOld, null);
	}

	public static int update(DisciplinaAvaliacaoAlunoQuestao objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(DisciplinaAvaliacaoAlunoQuestao objeto, int cdMatriculaDisciplinaOld, int cdOfertaAvaliacaoQuestaoOld, int cdOfertaAvaliacaoOld, int cdOfertaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_disciplina_avaliacao_aluno_questao SET cd_matricula_disciplina=?,"+
												      		   "cd_oferta_avaliacao_questao=?,"+
												      		   "cd_oferta_avaliacao=?,"+
												      		   "cd_oferta=?,"+
												      		   "cd_alternativa=?,"+
												      		   "txt_resposta=? WHERE cd_matricula_disciplina=? AND cd_oferta_avaliacao_questao=? AND cd_oferta_avaliacao=? AND cd_oferta=?");
			pstmt.setInt(1,objeto.getCdMatriculaDisciplina());
			pstmt.setInt(2,objeto.getCdOfertaAvaliacaoQuestao());
			pstmt.setInt(3,objeto.getCdOfertaAvaliacao());
			pstmt.setInt(4,objeto.getCdOferta());
			if(objeto.getCdAlternativa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdAlternativa());
			pstmt.setString(6,objeto.getTxtResposta());
			pstmt.setInt(7, cdMatriculaDisciplinaOld!=0 ? cdMatriculaDisciplinaOld : objeto.getCdMatriculaDisciplina());
			pstmt.setInt(8, cdOfertaAvaliacaoQuestaoOld!=0 ? cdOfertaAvaliacaoQuestaoOld : objeto.getCdOfertaAvaliacaoQuestao());
			pstmt.setInt(9, cdOfertaAvaliacaoOld!=0 ? cdOfertaAvaliacaoOld : objeto.getCdOfertaAvaliacao());
			pstmt.setInt(10, cdOfertaOld!=0 ? cdOfertaOld : objeto.getCdOferta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoQuestaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoQuestaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMatriculaDisciplina, int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta) {
		return delete(cdMatriculaDisciplina, cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, null);
	}

	public static int delete(int cdMatriculaDisciplina, int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_disciplina_avaliacao_aluno_questao WHERE cd_matricula_disciplina=? AND cd_oferta_avaliacao_questao=? AND cd_oferta_avaliacao=? AND cd_oferta=?");
			pstmt.setInt(1, cdMatriculaDisciplina);
			pstmt.setInt(2, cdOfertaAvaliacaoQuestao);
			pstmt.setInt(3, cdOfertaAvaliacao);
			pstmt.setInt(4, cdOferta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoQuestaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoQuestaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DisciplinaAvaliacaoAlunoQuestao get(int cdMatriculaDisciplina, int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta) {
		return get(cdMatriculaDisciplina, cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, null);
	}

	public static DisciplinaAvaliacaoAlunoQuestao get(int cdMatriculaDisciplina, int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_disciplina_avaliacao_aluno_questao WHERE cd_matricula_disciplina=? AND cd_oferta_avaliacao_questao=? AND cd_oferta_avaliacao=? AND cd_oferta=?");
			pstmt.setInt(1, cdMatriculaDisciplina);
			pstmt.setInt(2, cdOfertaAvaliacaoQuestao);
			pstmt.setInt(3, cdOfertaAvaliacao);
			pstmt.setInt(4, cdOferta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DisciplinaAvaliacaoAlunoQuestao(rs.getInt("cd_matricula_disciplina"),
						rs.getInt("cd_oferta_avaliacao_questao"),
						rs.getInt("cd_oferta_avaliacao"),
						rs.getInt("cd_oferta"),
						rs.getInt("cd_alternativa"),
						rs.getString("txt_resposta"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoQuestaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoQuestaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_disciplina_avaliacao_aluno_questao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoQuestaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoQuestaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<DisciplinaAvaliacaoAlunoQuestao> getList() {
		return getList(null);
	}

	public static ArrayList<DisciplinaAvaliacaoAlunoQuestao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<DisciplinaAvaliacaoAlunoQuestao> list = new ArrayList<DisciplinaAvaliacaoAlunoQuestao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				DisciplinaAvaliacaoAlunoQuestao obj = DisciplinaAvaliacaoAlunoQuestaoDAO.get(rsm.getInt("cd_matricula_disciplina"), rsm.getInt("cd_oferta_avaliacao_questao"), rsm.getInt("cd_oferta_avaliacao"), rsm.getInt("cd_oferta"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoQuestaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_disciplina_avaliacao_aluno_questao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}