package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class OcorrenciaDisciplinaAvaliacaoAlunoDAO{

	public static int insert(OcorrenciaDisciplinaAvaliacaoAluno objeto) {
		return insert(objeto, null);
	}

	public static int insert(OcorrenciaDisciplinaAvaliacaoAluno objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = com.tivic.manager.grl.OcorrenciaDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOcorrencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_ocorrencia_disciplina_avaliacao_aluno (cd_ocorrencia,"+
			                                  "cd_matricula_disciplina,"+
			                                  "cd_oferta_avaliacao,"+
			                                  "cd_oferta,"+
			                                  "vl_conceito_anterior,"+
			                                  "vl_conceito_posterior) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdOcorrencia()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdOcorrencia());
			if(objeto.getCdMatriculaDisciplina()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMatriculaDisciplina());
			if(objeto.getCdOfertaAvaliacao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdOfertaAvaliacao());
			if(objeto.getCdOferta()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdOferta());
			pstmt.setFloat(5,objeto.getVlConceitoAnterior());
			pstmt.setFloat(6,objeto.getVlConceitoPosterior());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDisciplinaAvaliacaoAlunoDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDisciplinaAvaliacaoAlunoDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OcorrenciaDisciplinaAvaliacaoAluno objeto) {
		return update(objeto, 0, null);
	}

	public static int update(OcorrenciaDisciplinaAvaliacaoAluno objeto, int cdOcorrenciaOld) {
		return update(objeto, cdOcorrenciaOld, null);
	}

	public static int update(OcorrenciaDisciplinaAvaliacaoAluno objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(OcorrenciaDisciplinaAvaliacaoAluno objeto, int cdOcorrenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			OcorrenciaDisciplinaAvaliacaoAluno objetoTemp = get(objeto.getCdOcorrencia(), connect);
			if (objetoTemp == null) 
				pstmt = connect.prepareStatement("INSERT INTO acd_ocorrencia_disciplina_avaliacao_aluno (cd_ocorrencia,"+
			                                  "cd_matricula_disciplina,"+
			                                  "cd_oferta_avaliacao,"+
			                                  "cd_oferta,"+
			                                  "vl_conceito_anterior,"+
			                                  "vl_conceito_posterior) VALUES (?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE acd_ocorrencia_disciplina_avaliacao_aluno SET cd_ocorrencia=?,"+
												      		   "cd_matricula_disciplina=?,"+
												      		   "cd_oferta_avaliacao=?,"+
												      		   "cd_oferta=?,"+
												      		   "vl_conceito_anterior=?,"+
												      		   "vl_conceito_posterior=? WHERE cd_ocorrencia=?");
			pstmt.setInt(1,objeto.getCdOcorrencia());
			if(objeto.getCdMatriculaDisciplina()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMatriculaDisciplina());
			if(objeto.getCdOfertaAvaliacao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdOfertaAvaliacao());
			if(objeto.getCdOferta()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdOferta());
			pstmt.setFloat(5,objeto.getVlConceitoAnterior());
			pstmt.setFloat(6,objeto.getVlConceitoPosterior());
			if (objetoTemp != null) {
				pstmt.setInt(7, cdOcorrenciaOld!=0 ? cdOcorrenciaOld : objeto.getCdOcorrencia());
			}
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.OcorrenciaDAO.update(objeto, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDisciplinaAvaliacaoAlunoDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDisciplinaAvaliacaoAlunoDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOcorrencia) {
		return delete(cdOcorrencia, null);
	}

	public static int delete(int cdOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_ocorrencia_disciplina_avaliacao_aluno WHERE cd_ocorrencia=?");
			pstmt.setInt(1, cdOcorrencia);
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.OcorrenciaDAO.delete(cdOcorrencia, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDisciplinaAvaliacaoAlunoDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDisciplinaAvaliacaoAlunoDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OcorrenciaDisciplinaAvaliacaoAluno get(int cdOcorrencia) {
		return get(cdOcorrencia, null);
	}

	public static OcorrenciaDisciplinaAvaliacaoAluno get(int cdOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_ocorrencia_disciplina_avaliacao_aluno A, grl_ocorrencia B WHERE A.cd_ocorrencia=B.cd_ocorrencia AND A.cd_ocorrencia=?");
			pstmt.setInt(1, cdOcorrencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OcorrenciaDisciplinaAvaliacaoAluno(rs.getInt("cd_ocorrencia"),
						rs.getInt("cd_pessoa"),
						rs.getString("txt_ocorrencia"),
						(rs.getTimestamp("dt_ocorrencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ocorrencia").getTime()),
						rs.getInt("cd_tipo_ocorrencia"),
						rs.getInt("st_ocorrencia"),
						rs.getInt("cd_sistema"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_matricula_disciplina"),
						rs.getInt("cd_oferta_avaliacao"),
						rs.getInt("cd_oferta"),
						rs.getFloat("vl_conceito_anterior"),
						rs.getFloat("vl_conceito_posterior"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDisciplinaAvaliacaoAlunoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDisciplinaAvaliacaoAlunoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_ocorrencia_disciplina_avaliacao_aluno");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDisciplinaAvaliacaoAlunoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDisciplinaAvaliacaoAlunoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OcorrenciaDisciplinaAvaliacaoAluno> getList() {
		return getList(null);
	}

	public static ArrayList<OcorrenciaDisciplinaAvaliacaoAluno> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OcorrenciaDisciplinaAvaliacaoAluno> list = new ArrayList<OcorrenciaDisciplinaAvaliacaoAluno>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OcorrenciaDisciplinaAvaliacaoAluno obj = OcorrenciaDisciplinaAvaliacaoAlunoDAO.get(rsm.getInt("cd_ocorrencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDisciplinaAvaliacaoAlunoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_ocorrencia_disciplina_avaliacao_aluno", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}