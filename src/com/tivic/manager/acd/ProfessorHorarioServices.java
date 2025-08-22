package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class ProfessorHorarioServices {

	public static Result save(ProfessorHorario professorHorario){
		return save(professorHorario, null);
	}

	public static Result save(ProfessorHorario professorHorario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(professorHorario==null)
				return new Result(-1, "Erro ao salvar. ProfessorHorario é nulo");

			int retorno;
			retorno = ProfessorHorarioDAO.insert(professorHorario, connect);
			professorHorario.setCdHorario(retorno);
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PROFESSORHORARIO", professorHorario);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdHorario, int cdProfessor){
		return remove(cdHorario, cdProfessor, false, null);
	}
	public static Result remove(int cdHorario, int cdProfessor, boolean cascade){
		return remove(cdHorario, cdProfessor, cascade, null);
	}
	public static Result remove(int cdHorario, int cdProfessor, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
				retorno = ProfessorHorarioDAO.delete(cdHorario, cdProfessor, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
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
			pstmt = connect.prepareStatement("SELECT A.*, B.* "+
											 "FROM acd_professor_horario A "+
											 "JOIN acd_instituicao_horario B ON( A.cd_horario = B.cd_Horario )"+
											 "JOIN acd_instituicao_periodo C ON( B.cd_instituicao = C.cd_instituicao AND C.st_periodo_letivo = "+InstituicaoPeriodoServices.ST_ATUAL+" )"+
											 " WHERE B.cd_periodo_letivo = B.cd_periodo_letivo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorHorarioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorHorarioServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllOcupadosByInstituicao(int cdProfessor, int cdInstituicao, int cdPeriodoLetivo) {
		return getAllOcupadosByInstituicao(cdProfessor, cdInstituicao, cdPeriodoLetivo, null);
	}
	
	public static ResultSetMap getAllOcupadosByInstituicao(int cdProfessor, int cdInstituicao, int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.*, C.*, D.* "+
											 "FROM acd_professor_horario A "+
											 "JOIN acd_instituicao_horario B ON( A.cd_horario = B.cd_horario ) "+
											 "JOIN acd_oferta C ON( A.cd_professor = C.cd_professor ) "+
											 "JOIN acd_oferta_horario D ON(  C.cd_oferta = D.cd_oferta AND B.cd_horario = D.cd_horario_instituicao ) "+
											 "WHERE A.cd_professor = "+cdProfessor+
											 " AND B.cd_instituicao = "+cdInstituicao+
											 " AND C.cd_periodo_letivo = " + cdPeriodoLetivo+
											 " AND B.cd_periodo_letivo = " + cdPeriodoLetivo);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorHorarioServices.getAllOcupadosByOferta: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorHorarioServices.getAllOcupadosByOferta: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByProfessor(int cdProfessor) {
		return getAllByProfessor(cdProfessor, null);
	}

	public static ResultSetMap getAllByProfessor(int cdProfessor, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.* FROM acd_professor_horario A "+
											 "JOIN acd_instituicao_horario B ON ( A.cd_horario = B.cd_horario ) "+
											 "JOIN acd_instituicao_periodo C ON( B.cd_instituicao = C.cd_instituicao AND C.st_periodo_letivo = "+InstituicaoPeriodoServices.ST_ATUAL+" )"+
											 "WHERE B.cd_periodo_letivo = B.cd_periodo_letivo "+ 
											 "  AND cd_professor = "+cdProfessor);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorHorarioServices.getAllByProfessorInstituicao: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorHorarioServices.getAllByProfessorInstituicao: " + e);
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
		return Search.find("SELECT * FROM acd_professor_horario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
