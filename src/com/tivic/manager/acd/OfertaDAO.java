package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class OfertaDAO{

	public static int insert(Oferta objeto) {
		return insert(objeto, null);
	}

	public static int insert(Oferta objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_oferta", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOferta(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_oferta (cd_oferta,"+
			                                  "cd_turma,"+
			                                  "cd_periodo_letivo,"+
			                                  "dt_inicio,"+
			                                  "dt_termino,"+
			                                  "nr_vagas,"+
			                                  "nr_dias,"+
			                                  "tp_turno,"+
			                                  "vl_disciplina,"+
			                                  "st_classe_disciplina,"+
			                                  "cd_instituicao_pratica,"+
			                                  "cd_supervisor_pratica,"+
			                                  "cd_professor,"+
			                                  "tp_controle_frequencia,"+
			                                  "cd_matriz,"+
			                                  "cd_curso,"+
			                                  "cd_curso_modulo,"+
			                                  "cd_disciplina,"+
			                                  "cd_dependencia,"+
			                                  "st_oferta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTurma()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTurma());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			if(objeto.getDtTermino()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtTermino().getTimeInMillis()));
			pstmt.setInt(6,objeto.getNrVagas());
			pstmt.setInt(7,objeto.getNrDias());
			pstmt.setInt(8,objeto.getTpTurno());
			pstmt.setFloat(9,0);//Temporário pois esta vindo como NaN
			pstmt.setInt(10,objeto.getStClasseDisciplina());
			if(objeto.getCdInstituicaoPratica()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdInstituicaoPratica());
			if(objeto.getCdSupervisorPratica()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdSupervisorPratica());
			if(objeto.getCdProfessor()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdProfessor());
			pstmt.setInt(14,objeto.getTpControleFrequencia());
			if(objeto.getCdMatriz()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdMatriz());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdCurso());
			if(objeto.getCdCursoModulo()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdCursoModulo());
			if(objeto.getCdDisciplina()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdDisciplina());
			if(objeto.getCdDependencia()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdDependencia());
			pstmt.setInt(20,objeto.getStOferta());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Oferta objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Oferta objeto, int cdOfertaOld) {
		return update(objeto, cdOfertaOld, null);
	}

	public static int update(Oferta objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Oferta objeto, int cdOfertaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_oferta SET cd_oferta=?,"+
												      		   "cd_turma=?,"+
												      		   "cd_periodo_letivo=?,"+
												      		   "dt_inicio=?,"+
												      		   "dt_termino=?,"+
												      		   "nr_vagas=?,"+
												      		   "nr_dias=?,"+
												      		   "tp_turno=?,"+
												      		   "vl_disciplina=?,"+
												      		   "st_classe_disciplina=?,"+
												      		   "cd_instituicao_pratica=?,"+
												      		   "cd_supervisor_pratica=?,"+
												      		   "cd_professor=?,"+
												      		   "tp_controle_frequencia=?,"+
												      		   "cd_matriz=?,"+
												      		   "cd_curso=?,"+
												      		   "cd_curso_modulo=?,"+
												      		   "cd_disciplina=?,"+
												      		   "cd_dependencia=?,"+
												      		   "st_oferta=? WHERE cd_oferta=?");
			pstmt.setInt(1,objeto.getCdOferta());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTurma());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			if(objeto.getDtTermino()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtTermino().getTimeInMillis()));
			pstmt.setInt(6,objeto.getNrVagas());
			pstmt.setInt(7,objeto.getNrDias());
			pstmt.setInt(8,objeto.getTpTurno());
			pstmt.setFloat(9,0);//Temporário pois esta vindo como NaN
			pstmt.setInt(10,objeto.getStClasseDisciplina());
			if(objeto.getCdInstituicaoPratica()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdInstituicaoPratica());
			if(objeto.getCdSupervisorPratica()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdSupervisorPratica());
			if(objeto.getCdProfessor()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdProfessor());
			pstmt.setInt(14,objeto.getTpControleFrequencia());
			if(objeto.getCdMatriz()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdMatriz());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdCurso());
			if(objeto.getCdCursoModulo()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdCursoModulo());
			if(objeto.getCdDisciplina()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdDisciplina());
			if(objeto.getCdDependencia()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdDependencia());
			pstmt.setInt(20,objeto.getStOferta());
			pstmt.setInt(21, cdOfertaOld!=0 ? cdOfertaOld : objeto.getCdOferta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOferta) {
		return delete(cdOferta, null);
	}

	public static int delete(int cdOferta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_oferta WHERE cd_oferta=?");
			pstmt.setInt(1, cdOferta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Oferta get(int cdOferta) {
		return get(cdOferta, null);
	}

	public static Oferta get(int cdOferta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta WHERE cd_oferta=?");
			pstmt.setInt(1, cdOferta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Oferta(rs.getInt("cd_oferta"),
						rs.getInt("cd_turma"),
						rs.getInt("cd_periodo_letivo"),
						(rs.getTimestamp("dt_inicio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio").getTime()),
						(rs.getTimestamp("dt_termino")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_termino").getTime()),
						rs.getInt("nr_vagas"),
						rs.getInt("nr_dias"),
						rs.getInt("tp_turno"),
						rs.getFloat("vl_disciplina"),
						rs.getInt("st_classe_disciplina"),
						rs.getInt("cd_instituicao_pratica"),
						rs.getInt("cd_supervisor_pratica"),
						rs.getInt("cd_professor"),
						rs.getInt("tp_controle_frequencia"),
						rs.getInt("cd_matriz"),
						rs.getInt("cd_curso"),
						rs.getInt("cd_curso_modulo"),
						rs.getInt("cd_disciplina"),
						rs.getInt("cd_dependencia"),
						rs.getInt("st_oferta"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Oferta> getList() {
		return getList(null);
	}

	public static ArrayList<Oferta> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Oferta> list = new ArrayList<Oferta>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Oferta obj = OfertaDAO.get(rsm.getInt("cd_oferta"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_oferta", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
