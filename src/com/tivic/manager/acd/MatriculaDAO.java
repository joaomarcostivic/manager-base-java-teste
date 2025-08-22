package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class MatriculaDAO{

	public static int insert(Matricula objeto) {
		return insert(objeto, null);
	}

	public static int insert(Matricula objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_matricula", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMatricula(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_matricula (cd_matricula,"+
			                                  "cd_matriz,"+
			                                  "cd_turma,"+
			                                  "cd_periodo_letivo,"+
			                                  "dt_matricula,"+
			                                  "dt_conclusao,"+
			                                  "st_matricula,"+
			                                  "tp_matricula,"+
			                                  "nr_matricula,"+
			                                  "cd_aluno,"+
			                                  "cd_matricula_origem,"+
			                                  "cd_reserva,"+
			                                  "cd_area_interesse,"+
			                                  "txt_observacao,"+
			                                  "txt_boletim,"+
			                                  "cd_curso,"+
			                                  "cd_pre_matricula,"+
			                                  "tp_escolarizacao_outro_espaco,"+
			                                  "lg_transporte_publico,"+
			                                  "tp_poder_responsavel,"+
			                                  "tp_forma_ingresso,"+
			                                  "txt_documento_oficial,"+
			                                  "dt_interrupcao,"+
			                                  "lg_autorizacao_rematricula,"+
			                                  "lg_atividade_complementar,"+
			                                  "lg_reprovacao,"+
			                                  "st_matricula_centaurus,"+
			                                  "st_aluno_censo,"+
			                                  "nr_matricula_censo,"+
			                                  "st_censo_final,"+
			                                  "nm_ultima_escola,"+
			                                  "lg_autorizacao,"+
			                                  "nr_autorizacao,"+
			                                  "lg_matricula_em_curso,"+
			                                  "lg_permissao_fora_idade) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdMatriz()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMatriz());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTurma());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdPeriodoLetivo());
			if(objeto.getDtMatricula()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtMatricula().getTimeInMillis()));
			if(objeto.getDtConclusao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtConclusao().getTimeInMillis()));
			pstmt.setInt(7,objeto.getStMatricula());
			pstmt.setInt(8,objeto.getTpMatricula());
			pstmt.setString(9,objeto.getNrMatricula());
			if(objeto.getCdAluno()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdAluno());
			if(objeto.getCdMatriculaOrigem()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdMatriculaOrigem());
			if(objeto.getCdReserva()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdReserva());
			if(objeto.getCdAreaInteresse()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdAreaInteresse());
			pstmt.setString(14,objeto.getTxtObservacao());
			pstmt.setString(15,objeto.getTxtBoletim());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdCurso());
			if(objeto.getCdPreMatricula()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdPreMatricula());
			pstmt.setInt(18,objeto.getTpEscolarizacaoOutroEspaco());
			pstmt.setInt(19,objeto.getLgTransportePublico());
			pstmt.setInt(20,objeto.getTpPoderResponsavel());
			pstmt.setInt(21,objeto.getTpFormaIngresso());
			pstmt.setString(22,objeto.getTxtDocumentoOficial());
			if(objeto.getDtInterrupcao()==null)
				pstmt.setNull(23, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(23,new Timestamp(objeto.getDtInterrupcao().getTimeInMillis()));
			pstmt.setInt(24,objeto.getLgAutorizacaoRematricula());
			pstmt.setInt(25,objeto.getLgAtividadeComplementar());
			pstmt.setInt(26,objeto.getLgReprovacao());
			pstmt.setInt(27,objeto.getStMatriculaCentaurus());
			pstmt.setInt(28,objeto.getStAlunoCenso());
			pstmt.setString(29,objeto.getNrMatriculaCenso());
			pstmt.setInt(30,objeto.getStCensoFinal());
			pstmt.setString(31,objeto.getNmUltimaEscola());
			pstmt.setInt(32,objeto.getLgAutorizacao());
			pstmt.setString(33,objeto.getNrAutorizacao());
			pstmt.setInt(34,objeto.getLgMatriculaEmCurso());
			pstmt.setInt(35,objeto.getLgPermissaoForaIdade());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Matricula objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Matricula objeto, int cdMatriculaOld) {
		return update(objeto, cdMatriculaOld, null);
	}

	public static int update(Matricula objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Matricula objeto, int cdMatriculaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_matricula SET cd_matricula=?,"+
												      		   "cd_matriz=?,"+
												      		   "cd_turma=?,"+
												      		   "cd_periodo_letivo=?,"+
												      		   "dt_matricula=?,"+
												      		   "dt_conclusao=?,"+
												      		   "st_matricula=?,"+
												      		   "tp_matricula=?,"+
												      		   "nr_matricula=?,"+
												      		   "cd_aluno=?,"+
												      		   "cd_matricula_origem=?,"+
												      		   "cd_reserva=?,"+
												      		   "cd_area_interesse=?,"+
												      		   "txt_observacao=?,"+
												      		   "txt_boletim=?,"+
												      		   "cd_curso=?,"+
												      		   "cd_pre_matricula=?,"+
												      		   "tp_escolarizacao_outro_espaco=?,"+
												      		   "lg_transporte_publico=?,"+
												      		   "tp_poder_responsavel=?,"+
												      		   "tp_forma_ingresso=?,"+
												      		   "txt_documento_oficial=?,"+
												      		   "dt_interrupcao=?,"+
												      		   "lg_autorizacao_rematricula=?,"+
												      		   "lg_atividade_complementar=?,"+
												      		   "lg_reprovacao=?,"+
												      		   "st_matricula_centaurus=?,"+
												      		   "st_aluno_censo=?,"+
												      		   "nr_matricula_censo=?,"+
												      		   "st_censo_final=?,"+
												      		   "nm_ultima_escola=?,"+
												      		   "lg_autorizacao=?,"+
												      		   "nr_autorizacao=?,"+
												      		   "lg_matricula_em_curso=?,"+
												      		   "lg_permissao_fora_idade=? WHERE cd_matricula=?");
			pstmt.setInt(1,objeto.getCdMatricula());
			if(objeto.getCdMatriz()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMatriz());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTurma());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdPeriodoLetivo());
			if(objeto.getDtMatricula()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtMatricula().getTimeInMillis()));
			if(objeto.getDtConclusao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtConclusao().getTimeInMillis()));
			pstmt.setInt(7,objeto.getStMatricula());
			pstmt.setInt(8,objeto.getTpMatricula());
			pstmt.setString(9,objeto.getNrMatricula());
			if(objeto.getCdAluno()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdAluno());
			if(objeto.getCdMatriculaOrigem()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdMatriculaOrigem());
			if(objeto.getCdReserva()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdReserva());
			if(objeto.getCdAreaInteresse()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdAreaInteresse());
			pstmt.setString(14,objeto.getTxtObservacao());
			pstmt.setString(15,objeto.getTxtBoletim());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdCurso());
			if(objeto.getCdPreMatricula()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdPreMatricula());
			pstmt.setInt(18,objeto.getTpEscolarizacaoOutroEspaco());
			pstmt.setInt(19,objeto.getLgTransportePublico());
			pstmt.setInt(20,objeto.getTpPoderResponsavel());
			pstmt.setInt(21,objeto.getTpFormaIngresso());
			pstmt.setString(22,objeto.getTxtDocumentoOficial());
			if(objeto.getDtInterrupcao()==null)
				pstmt.setNull(23, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(23,new Timestamp(objeto.getDtInterrupcao().getTimeInMillis()));
			pstmt.setInt(24,objeto.getLgAutorizacaoRematricula());
			pstmt.setInt(25,objeto.getLgAtividadeComplementar());
			pstmt.setInt(26,objeto.getLgReprovacao());
			pstmt.setInt(27,objeto.getStMatriculaCentaurus());
			pstmt.setInt(28,objeto.getStAlunoCenso());
			pstmt.setString(29,objeto.getNrMatriculaCenso());
			pstmt.setInt(30,objeto.getStCensoFinal());
			pstmt.setString(31,objeto.getNmUltimaEscola());
			pstmt.setInt(32,objeto.getLgAutorizacao());
			pstmt.setString(33,objeto.getNrAutorizacao());
			pstmt.setInt(34,objeto.getLgMatriculaEmCurso());
			pstmt.setInt(35,objeto.getLgPermissaoForaIdade());
			pstmt.setInt(36, cdMatriculaOld!=0 ? cdMatriculaOld : objeto.getCdMatricula());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMatricula) {
		return delete(cdMatricula, null);
	}

	public static int delete(int cdMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_matricula WHERE cd_matricula=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Matricula get(int cdMatricula) {
		return get(cdMatricula, null);
	}

	public static Matricula get(int cdMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula WHERE cd_matricula=?");
			pstmt.setInt(1, cdMatricula);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Matricula(rs.getInt("cd_matricula"),
						rs.getInt("cd_matriz"),
						rs.getInt("cd_turma"),
						rs.getInt("cd_periodo_letivo"),
						(rs.getTimestamp("dt_matricula")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_matricula").getTime()),
						(rs.getTimestamp("dt_conclusao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_conclusao").getTime()),
						rs.getInt("st_matricula"),
						rs.getInt("tp_matricula"),
						rs.getString("nr_matricula"),
						rs.getInt("cd_aluno"),
						rs.getInt("cd_matricula_origem"),
						rs.getInt("cd_reserva"),
						rs.getInt("cd_area_interesse"),
						rs.getString("txt_observacao"),
						rs.getString("txt_boletim"),
						rs.getInt("cd_curso"),
						rs.getInt("cd_pre_matricula"),
						rs.getInt("tp_escolarizacao_outro_espaco"),
						rs.getInt("lg_transporte_publico"),
						rs.getInt("tp_poder_responsavel"),
						rs.getInt("tp_forma_ingresso"),
						rs.getString("txt_documento_oficial"),
						(rs.getTimestamp("dt_interrupcao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_interrupcao").getTime()),
						rs.getInt("lg_autorizacao_rematricula"),
						rs.getInt("lg_atividade_complementar"),
						rs.getInt("lg_reprovacao"),
						rs.getInt("st_matricula_centaurus"),
						rs.getInt("st_aluno_censo"),
						rs.getString("nr_matricula_censo"),
						rs.getInt("st_censo_final"),
						rs.getString("nm_ultima_escola"),
						rs.getInt("lg_autorizacao"),
						rs.getString("nr_autorizacao"),
						rs.getInt("lg_matricula_em_curso"),
						rs.getInt("lg_permissao_fora_idade"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Matricula> getList() {
		return getList(null);
	}

	public static ArrayList<Matricula> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Matricula> list = new ArrayList<Matricula>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Matricula obj = MatriculaDAO.get(rsm.getInt("cd_matricula"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_matricula", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
