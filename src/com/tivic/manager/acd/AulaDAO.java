package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class AulaDAO{

	public static int insert(Aula objeto) {
		return insert(objeto, null);
	}

	public static int insert(Aula objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_aula", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAula(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_aula (cd_aula,"+
			                                  "cd_tipo_aula,"+
			                                  "txt_conteudo,"+
			                                  "txt_observacao,"+
			                                  "st_aula,"+
			                                  "cd_professor,"+
			                                  "cd_professor_substituto,"+
			                                  "cd_oferta,"+
			                                  "dt_aula,"+
			                                  "cd_plano,"+
			                                  "cd_disciplina,"+
			                                  "cd_turma,"+
			                                  "cd_horario,"+
			                                  "txt_motivo_cancelamento,"+
			                                  "lg_reposicao,"+
			                                  "cd_aula_reposta,"+
			                                  "hr_inicio_reposicao,"+
			                                  "hr_termino_reposicao,"+
			                                  "txt_titulo,"+
			                                  "txt_objetivos_aprendizagem,"+
			                                  "txt_objetos_conhecimento,"+
			                                  "txt_campos_experiencia,"+
			                                  "txt_procedimentos_metodologicos,"+
			                                  "txt_recursos_didaticos,"+
			                                  "txt_avaliacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			
			pstmt.setInt(1, code);
			if(objeto.getCdTipoAula()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoAula());
			pstmt.setString(3,objeto.getTxtConteudo());
			pstmt.setString(4,objeto.getTxtObservacao());
			pstmt.setInt(5,objeto.getStAula());
			if(objeto.getCdProfessor()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdProfessor());
			if(objeto.getCdProfessorSubstituto()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdProfessorSubstituto());
			if(objeto.getCdOferta()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdOferta());
			if(objeto.getDtAula()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtAula().getTimeInMillis()));
			if(objeto.getCdPlano()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdPlano());
			if(objeto.getCdDisciplina()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdDisciplina());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdTurma());
			if(objeto.getCdHorario()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdHorario());
			pstmt.setString(14,objeto.getTxtMotivoCancelamento());
			pstmt.setInt(15,objeto.getLgReposicao());
			if(objeto.getCdAulaReposta()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdAulaReposta());
			if(objeto.getHrInicioReposicao()==null)
				pstmt.setNull(17, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(17,new Timestamp(objeto.getHrInicioReposicao().getTimeInMillis()));
			if(objeto.getHrTerminoReposicao()==null)
				pstmt.setNull(18, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(18,new Timestamp(objeto.getHrTerminoReposicao().getTimeInMillis()));
			pstmt.setString(19,objeto.getTxtTitulo());
			pstmt.setString(20,objeto.getTxtObjetivosAprendizagem());
			pstmt.setString(21,objeto.getTxtObjetosConhecimento());
			pstmt.setString(22,objeto.getTxtCamposExperiencia());
			pstmt.setString(23,objeto.getTxtProcedimentosMetodologicos());
			pstmt.setString(24,objeto.getTxtRecursosDidaticos());
			pstmt.setString(25,objeto.getTxtAvaliacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Aula objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Aula objeto, int cdAulaOld) {
		return update(objeto, cdAulaOld, null);
	}

	public static int update(Aula objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Aula objeto, int cdAulaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_aula SET cd_aula=?,"+
												      		   "cd_tipo_aula=?,"+
												      		   "txt_conteudo=?,"+
												      		   "txt_observacao=?,"+
												      		   "st_aula=?,"+
												      		   "cd_professor=?,"+
												      		   "cd_professor_substituto=?,"+
												      		   "cd_oferta=?,"+
												      		   "dt_aula=?,"+
												      		   "cd_plano=?,"+
												      		   "cd_disciplina=?,"+
												      		   "cd_turma=?,"+
												      		   "cd_horario=?,"+
												      		   "txt_motivo_cancelamento=?,"+
												      		   "lg_reposicao=?,"+
												      		   "cd_aula_reposta=?,"+
												      		   "hr_inicio_reposicao=?,"+
												      		   "hr_termino_reposicao=?,"+
												      		   "txt_titulo=?,"+
												      		   "txt_objetivos_aprendizagem=?,"+
												      		   "txt_objetos_conhecimento=?,"+
												      		   "txt_campos_experiencia=?,"+
												      		   "txt_procedimentos_metodologicos=?,"+
												      		   "txt_recursos_didaticos=?,"+
												      		   "txt_avaliacao=? WHERE cd_aula=?");
			
			
			
			pstmt.setInt(1,objeto.getCdAula());
			if(objeto.getCdTipoAula()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoAula());
			pstmt.setString(3,objeto.getTxtConteudo());
			pstmt.setString(4,objeto.getTxtObservacao());
			pstmt.setInt(5,objeto.getStAula());
			if(objeto.getCdProfessor()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdProfessor());
			if(objeto.getCdProfessorSubstituto()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdProfessorSubstituto());
			if(objeto.getCdOferta()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdOferta());
			if(objeto.getDtAula()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtAula().getTimeInMillis()));
			if(objeto.getCdPlano()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdPlano());
			if(objeto.getCdDisciplina()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdDisciplina());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdTurma());
			if(objeto.getCdHorario()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdHorario());
			pstmt.setString(14,objeto.getTxtMotivoCancelamento());
			pstmt.setInt(15,objeto.getLgReposicao());
			if(objeto.getCdAulaReposta()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdAulaReposta());
			if(objeto.getHrInicioReposicao()==null)
				pstmt.setNull(17, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(17,new Timestamp(objeto.getHrInicioReposicao().getTimeInMillis()));
			if(objeto.getHrTerminoReposicao()==null)
				pstmt.setNull(18, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(18,new Timestamp(objeto.getHrTerminoReposicao().getTimeInMillis()));
			pstmt.setString(19,objeto.getTxtTitulo());
			pstmt.setString(20,objeto.getTxtObjetivosAprendizagem());
			pstmt.setString(21,objeto.getTxtObjetosConhecimento());
			pstmt.setString(22,objeto.getTxtCamposExperiencia());
			pstmt.setString(23,objeto.getTxtProcedimentosMetodologicos());
			pstmt.setString(24,objeto.getTxtRecursosDidaticos());
			pstmt.setString(25,objeto.getTxtAvaliacao());
			pstmt.setInt(26, cdAulaOld!=0 ? cdAulaOld : objeto.getCdAula());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAula) {
		return delete(cdAula, null);
	}

	public static int delete(int cdAula, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_aula WHERE cd_aula=?");
			pstmt.setInt(1, cdAula);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Aula get(int cdAula) {
		return get(cdAula, null);
	}

	public static Aula get(int cdAula, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_aula WHERE cd_aula=?");
			pstmt.setInt(1, cdAula);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Aula(rs.getInt("cd_aula"),
						rs.getInt("cd_tipo_aula"),
						rs.getString("txt_conteudo"),
						rs.getString("txt_observacao"),
						rs.getInt("st_aula"),
						rs.getInt("cd_professor"),
						rs.getInt("cd_professor_substituto"),
						rs.getInt("cd_oferta"),
						(rs.getTimestamp("dt_aula")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_aula").getTime()),
						rs.getInt("cd_plano"),
						rs.getInt("cd_disciplina"),
						rs.getInt("cd_turma"),
						rs.getInt("cd_horario"),
						rs.getString("txt_motivo_cancelamento"),
						rs.getInt("lg_reposicao"),
						rs.getInt("cd_aula_reposta"),
						(rs.getTimestamp("hr_inicio_reposicao")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_inicio_reposicao").getTime()),
						(rs.getTimestamp("hr_termino_reposicao")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_termino_reposicao").getTime()),
						rs.getString("txt_titulo"),
						rs.getString("txt_objetivos_aprendizagem"),
						rs.getString("txt_objetos_conhecimento"),
						rs.getString("txt_campos_experiencia"),
						rs.getString("txt_procedimentos_metodologicos"),
						rs.getString("txt_recursos_didaticos"),
						rs.getString("txt_avaliacao"));
				
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_aula");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Aula> getList() {
		return getList(null);
	}

	public static ArrayList<Aula> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Aula> list = new ArrayList<Aula>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Aula obj = AulaDAO.get(rsm.getInt("cd_aula"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_aula", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
