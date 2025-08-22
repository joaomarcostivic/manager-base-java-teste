package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class AulaTopicoServices {

	public static Result save(AulaTopico aulaTopico){
		return save(aulaTopico, null, null);
	}

	public static Result save(AulaTopico aulaTopico, AuthData authData){
		return save(aulaTopico, authData, null);
	}

	public static Result save(AulaTopico aulaTopico, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(aulaTopico==null)
				return new Result(-1, "Erro ao salvar. AulaTopico é nulo");

			int retorno;
			if(aulaTopico.getCdPlano()==0){
				retorno = AulaTopicoDAO.insert(aulaTopico, connect);
				aulaTopico.setCdPlano(retorno);
			}
			else {
				retorno = AulaTopicoDAO.update(aulaTopico, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AULATOPICO", aulaTopico);
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
	public static Result remove(AulaTopico aulaTopico) {
		return remove(aulaTopico.getCdPlano(), aulaTopico.getCdSecao(), aulaTopico.getCdTopico(), aulaTopico.getCdAula());
	}
	public static Result remove(int cdPlano, int cdSecao, int cdTopico, int cdAula){
		return remove(cdPlano, cdSecao, cdTopico, cdAula, false, null, null);
	}
	public static Result remove(int cdPlano, int cdSecao, int cdTopico, int cdAula, boolean cascade){
		return remove(cdPlano, cdSecao, cdTopico, cdAula, cascade, null, null);
	}
	public static Result remove(int cdPlano, int cdSecao, int cdTopico, int cdAula, boolean cascade, AuthData authData){
		return remove(cdPlano, cdSecao, cdTopico, cdAula, cascade, authData, null);
	}
	public static Result remove(int cdPlano, int cdSecao, int cdTopico, int cdAula, boolean cascade, AuthData authData, Connection connect){
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
			retorno = AulaTopicoDAO.delete(cdPlano, cdSecao, cdTopico, cdAula, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_aula_topico");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaTopicoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaTopicoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_aula_topico", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getAulasTopico(int cdTurma, int cdCurso, int cdProfessor, int cdDisciplina, int cdAula) {
		return getAulasTopico(cdTurma, cdCurso, cdProfessor, cdDisciplina, cdAula, null);
	}

	public static ResultSetMap getAulasTopico(int cdTurma, int cdCurso, int cdProfessor, int cdDisciplina, int cdAula, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_plano, C.nm_secao " + 
												 "FROM acd_plano_topico A, acd_plano B, acd_plano_secao C " + 
												 "WHERE A.cd_plano = B.cd_plano"+
												 "  AND A.cd_secao = C.cd_secao"+
												 "  AND B.tp_plano = C.tp_plano"+
												 "  AND B.cd_turma = "+cdTurma +
												 "  AND B.cd_curso = "+cdCurso +
												 "  AND B.cd_professor = "+cdProfessor +
												 (cdDisciplina > 0 ? "  AND B.cd_disciplina = "+cdDisciplina : "") +
												 "  AND B.st_plano = " + PlanoServices.VALIDADO+
												 "	AND (nm_secao like '%CONTEUDO%'"+ 
												 "			OR nm_secao like '%CONTEÚDO%')"+ 
												 " ORDER BY C.id_secao, nr_ordem ");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(cdAula > 0){
					ResultSetMap rsmAulaTopico = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_aula_topico WHERE cd_plano = " + rsm.getInt("cd_plano") + 
																														  " AND cd_secao = " + rsm.getInt("cd_secao") + 
																														  " AND cd_topico = " + rsm.getInt("cd_topico") + 
																														  " AND cd_aula = " + cdAula).executeQuery());
					if(rsmAulaTopico.next()){
						rsm.setValueToField("cd_aula", rsmAulaTopico.getInt("cd_aula"));
						rsm.setValueToField("lg_executado", rsmAulaTopico.getInt("lg_executado"));
						rsm.setValueToField("txt_observacao", rsmAulaTopico.getInt("txt_observacao"));
					}
				}
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoServices.getTopicosByPlano: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoServices.getTopicosByPlano: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAulasTopicoByAula(int cdAula) {
		return getAulasTopicoByAula(cdAula, null);
	}

	public static ResultSetMap getAulasTopicoByAula(int cdAula, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_aula_topico A "
					+ "														JOIN acd_plano_topico B ON (A.cd_plano = B.cd_plano"
					+ "																				AND A.cd_secao = B.cd_secao"
					+ " 																			AND A.cd_topico = B.cd_topico) "
					+ "														WHERE cd_aula = " + cdAula).executeQuery());
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoServices.getTopicosByPlano: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoServices.getTopicosByPlano: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	

}