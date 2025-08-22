package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class PlanoServices {

	//	TP_PLANO
	public static final int CURSO = 0;
	public static final int AULA  = 1;	
	
	public static final String[] tipoPlano = {"Plano de Curso", "Plano de Aula"};
	
	// ST_PLANO
	public static final int INATIVO   = 0;
	public static final int PENDENTE  = 1;
	public static final int VALIDADO  = 2;

	public static final String[] statusPlano = {"Inativo", "Pendente", "Validado"};
	
	
	public static Result save(Plano plano){
		return save(plano, null);
	}

	public static Result save(Plano plano, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(plano==null)
				return new Result(-1, "Erro ao salvar. Plano é nulo");
			
			int retorno;
			if(plano.getCdPlano()==0){
				retorno = PlanoDAO.insert(plano, connect);
				plano.setCdPlano(retorno);
			}
			else {
				retorno = PlanoDAO.update(plano, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PLANO", plano);
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
	
	public static Result inativar(Plano plano){
		return inativar(plano, null);
	}

	public static Result inativar(Plano plano, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(plano==null)
				return new Result(-1, "Erro ao salvar. Plano é nulo");
			
			plano.setStPlano(INATIVO);
			int retorno = PlanoDAO.update(plano, connect);
			if(retorno < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar plano");
			}
			

			if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PLANO", plano);
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
	
	public static Result validar(Plano plano){
		return validar(plano, null);
	}

	public static Result validar(Plano plano, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(plano==null)
				return new Result(-1, "Erro ao salvar. Plano é nulo");
			
			plano.setStPlano(VALIDADO);
			int retorno = PlanoDAO.update(plano, connect);
			if(retorno < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar plano");
			}
			

			if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PLANO", plano);
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
	
	public static Result remove(int cdPlano){
		return remove(cdPlano, false, null);
	}
	public static Result remove(int cdPlano, boolean cascade){
		return remove(cdPlano, cascade, null);
	}
	public static Result remove(int cdPlano, boolean cascade, Connection connect){
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
			retorno = PlanoDAO.delete(cdPlano, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_plano");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoServices.getAll: " + e);
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
		boolean isConnectionNull = connect==null;
		
		try {
			
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			ResultSetMap rsm = Search.find("SELECT A.*, B.nm_produto_servico as nm_curso, C.nm_pessoa AS NM_PROFESSOR FROM acd_plano A " + 
							  " JOIN grl_produto_servico B ON (A.cd_curso = B.cd_produto_servico) " + 
							  " JOIN grl_pessoa C ON (A.cd_professor = C.cd_pessoa)", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoServices.getAll: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getPlanos(int cdDisciplina, int cdProfessor, int tpPlano) {
		return getPlanos(cdDisciplina, cdProfessor, tpPlano, null);
	}
	
	public static ResultSetMap getPlanos(int cdDisciplina, int cdProfessor, int tpPlano, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			//montar rsm com 2 registros (meus planos e planos compartilhados)
			ResultSetMap rsmPlanos = new ResultSetMap();
			
			HashMap<String, Object> regMeusPlanos = new HashMap<String, Object>();
			regMeusPlanos.put("NM_GRUPO", "Meus Planos");
			regMeusPlanos.put("RSM_PLANOS", getPlanosByProfessor(cdDisciplina, cdProfessor, tpPlano, 0, connect).getLines());
			rsmPlanos.addRegister(regMeusPlanos);
			
			
			HashMap<String, Object> regPlanosCompartilhados = new HashMap<String, Object>();
			regPlanosCompartilhados.put("NM_GRUPO", "Planos Compartilhados");
			regPlanosCompartilhados.put("RSM_PLANOS", getPlanosByProfessor(cdDisciplina, cdProfessor, tpPlano, 1, connect).getLines());
			rsmPlanos.addRegister(regPlanosCompartilhados);
			
			return rsmPlanos;
		
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}	
	}
	
	
	/**
	 * Busca os planos pelo professor, disciplina, tipo de plano e por tipo de busca (0=inclusiva, 1=exclusiva)
	 * @param cdDisciplina
	 * @param cdProfessor
	 * @param tpPlano
	 * @param tpBusca
	 * @return
	 */
	public static ResultSetMap getPlanosByProfessor(int cdDisciplina, int cdProfessor, int tpPlano, int tpBusca) {
		return getPlanosByProfessor(cdDisciplina, cdProfessor, tpPlano, tpBusca, null);
	}
	
	public static ResultSetMap getPlanosByProfessor(int cdDisciplina, int cdProfessor, int tpPlano, int tpBusca, Connection connect)	{
		boolean isConnectionNull = connect ==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.*, B.nm_produto_servico as nm_curso, C.nm_periodo_letivo, " + 
					"E.id_turma, E.tp_turno, D.nm_produto_servico as nm_disciplina, F.nm_pessoa as nm_professor " +
					"FROM acd_plano A " +
					"LEFT OUTER JOIN grl_produto_servico B ON (A.cd_curso = B.cd_produto_servico) " +
					"LEFT OUTER JOIN acd_instituicao_periodo C ON (A.cd_periodo_letivo = C.cd_periodo_letivo) " +
					"LEFT OUTER JOIN grl_produto_servico D ON (A.cd_disciplina = D.cd_produto_servico) " +
					"LEFT OUTER JOIN acd_turma E ON (A.cd_turma = E.cd_turma) " +
					"LEFT OUTER JOIN grl_pessoa F ON (A.cd_professor = F.cd_pessoa) " +
					"WHERE A.cd_disciplina = " + cdDisciplina +
					" AND A.tp_plano = " + tpPlano +
					(cdProfessor>0 ? " AND cd_professor "+ ( tpBusca == 0 ? "= " : "<> " ) +cdProfessor  : ""));
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}	
	}
	
	public static ResultSetMap getPlanosCompartilhadosByDisciplina(int cdDisciplina) {
		return getPlanosCompartilhadosByDisciplina(cdDisciplina, null);
	}
	
	public static ResultSetMap getPlanosCompartilhadosByDisciplina(int cdDisciplina, Connection connect)	{
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT nm_plano " +
					"FROM acd_plano " +
					"WHERE cd_disciplina = " + cdDisciplina +
					  "AND lg_compartilhado = 1");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}	
	}
	

	public static ResultSetMap gerarImpressaoPlanoCurso(int cdPlano) {
		return gerarImpressaoPlanoCurso(cdPlano, null);
	}

	public static ResultSetMap gerarImpressaoPlanoCurso(int cdPlano, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			ResultSetMap rsmPlano = new ResultSetMap(connect.prepareStatement("SELECT C.NM_TITULO, C.TXT_TOPICO FROM acd_plano A, acd_plano_secao B, acd_plano_topico C "
																		+ "		WHERE A.cd_plano = C.cd_plano"
																		+ "		  AND B.cd_secao = C.cd_secao"
																		+ "		  AND A.tp_plano = B.tp_plano"
																		+ "		  AND A.cd_plano = " + cdPlano
																		+ "	    ORDER BY B.id_secao, C.nr_ordem").executeQuery());
			
		
			return rsmPlano;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoServices.alterarPosicaoTopico: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
