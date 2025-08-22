package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class PlanoTopicoServices {

	public static Result save(PlanoTopico planoTopico){
		return save(planoTopico, null);
	}

	public static Result save(PlanoTopico planoTopico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(planoTopico==null)
				return new Result(-1, "Erro ao salvar. PlanoTopico é nulo");

			int retorno;
			
			PlanoTopico t = PlanoTopicoDAO.get(planoTopico.getCdPlano(), planoTopico.getCdSecao(), planoTopico.getCdTopico(), connect);
			
			if(t==null){
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_plano", "" + planoTopico.getCdPlano(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_secao", "" + planoTopico.getCdSecao(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmTodosPlanoTopico = PlanoTopicoDAO.find(criterios, connect);
				
				planoTopico.setNrOrdem(rsmTodosPlanoTopico.size());
				
				retorno = PlanoTopicoDAO.insert(planoTopico, connect);
				planoTopico.setCdPlano(retorno);
			}
			else {
				retorno = PlanoTopicoDAO.update(planoTopico, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PLANOTOPICO", planoTopico);
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
	public static Result remove(int cdPlano, int cdSecao, int cdTopico){
		return remove(cdPlano, cdSecao, cdTopico, false, null);
	}
	public static Result remove(int cdPlano, int cdSecao, int cdTopico, boolean cascade){
		return remove(cdPlano, cdSecao, cdTopico, cascade, null);
	}
	public static Result remove(int cdPlano, int cdSecao, int cdTopico, boolean cascade, Connection connect){
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
			
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_plano", "" + cdPlano, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_secao", "" + cdSecao, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_topico", "" + cdTopico, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmAulaTopico = AulaTopicoDAO.find(criterios, connect);
			if(rsmAulaTopico.next()){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Existem aulas registradas com esse tópico");
			}
			
			
			PlanoTopico planoTopico = PlanoTopicoDAO.get(cdPlano, cdSecao, cdTopico, connect);
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_plano", "" + cdPlano, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_secao", "" + cdSecao, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("nr_ordem", "" + planoTopico.getNrOrdem(), ItemComparator.GREATER, Types.INTEGER));
			ResultSetMap rsmPlanosTopico = find(criterios, connect);
			while(rsmPlanosTopico.next()){
				PlanoTopico planoTopicoMaior = PlanoTopicoDAO.get(cdPlano, cdSecao, cdTopico, connect);
				planoTopicoMaior.setNrOrdem(planoTopicoMaior.getNrOrdem()-1);
				if(PlanoTopicoDAO.update(planoTopicoMaior, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar topicos");
				}
			}
			
			if(!cascade || retorno>0)
				retorno = PlanoTopicoDAO.delete(cdPlano, cdSecao, cdTopico, connect);
			
			
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_plano_topico");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoTopicoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTopicoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Retorna as secoes e tópicos pelo plano
	 * 
	 * @param cdPlano
	 * @return ResultSetMap
	 */
	public static ResultSetMap getSecoesTopicosByPlano(int cdPlano) {
		return getSecoesTopicosByTipo(cdPlano, null);
	}

	public static ResultSetMap getSecoesTopicosByTipo(int cdPlano, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			Plano plano = PlanoDAO.get(cdPlano, connect);
			
			if(plano==null)
				return new ResultSetMap();
			
			
			ResultSetMap rsmSecoes = PlanoSecaoServices.getSecoesByTipo(plano.getTpPlano(), connect);
			
			while(rsmSecoes.next()) {
				HashMap<String, Object> register = rsmSecoes.getRegister();
				register.put("CD_PLANO", cdPlano);
				register.put("RSM_TOPICOS", getTopicosByPlano(rsmSecoes.getInt("CD_PLANO"), rsmSecoes.getInt("CD_SECAO"), connect));
			}
			rsmSecoes.beforeFirst();
			
			return rsmSecoes;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoServices.getSecoesByTipo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getTopicosArvore(int cdTurma, int cdCurso, int cdProfessor, int cdDisciplina) {
		return getTopicosArvore(cdTurma, cdCurso, cdProfessor, cdDisciplina, null);
	}

	public static ResultSetMap getTopicosArvore(int cdTurma, int cdCurso, int cdProfessor, int cdDisciplina, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_turma", "" + cdTurma, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_curso", "" + cdCurso, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_professor", "" + cdProfessor, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_disciplina", "" + cdDisciplina, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("st_plano", "" + PlanoServices.INATIVO, ItemComparator.DIFFERENT, Types.INTEGER));
			ResultSetMap rsm = PlanoServices.find(criterios, connect);
			if(rsm.next()){
				
				if(rsm.getInt("st_plano") == PlanoServices.PENDENTE){
					rsm.setValueToField("NM_PLANO", rsm.getString("NM_PLANO") + " (Plano não validado)");
				}
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("tp_plano", rsm.getString("tp_plano"), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmSecao = PlanoSecaoServices.find(criterios, connect);
				while(rsmSecao.next()){
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_plano", rsm.getString("cd_plano"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_secao", rsmSecao.getString("cd_secao"), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmTopicos = PlanoTopicoServices.find(criterios, connect);
					rsmSecao.setValueToField("TOPICOS", rsmTopicos);
				}
				rsmSecao.beforeFirst();
				
				rsm.setValueToField("SECOES", rsmSecao);
			}
			rsm.beforeFirst();
			
			return rsm;
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
	
	/**
	 * Retorna os tópicos das secões pelo plano
	 * 
	 * @param cdPlano, cdSecao
	 * @return ResultSetMap
	 */
	public static ResultSetMap getTopicosByPlano(int cdPlano, int cdSecao) {
		return getTopicosByPlano(cdPlano, cdSecao, null);
	}

	public static ResultSetMap getTopicosByPlano(int cdPlano, int cdSecao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			pstmt = connect.prepareStatement("SELECT * " + 
												 "FROM acd_plano_topico " + 
												 "WHERE cd_plano = "+cdPlano +
												 "  AND cd_secao = "+cdSecao +
												 " ORDER BY nr_ordem ");
		
			return new ResultSetMap(pstmt.executeQuery());
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
	
	public static Result alterarPosicaoTopico(int cdPlano, int cdSecao, int cdTopico, int sentido) {
		return alterarPosicaoTopico(cdPlano, cdSecao, cdTopico, sentido, null);
	}

	public static Result alterarPosicaoTopico(int cdPlano, int cdSecao, int cdTopico, int sentido, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			PlanoTopico planoTopico = PlanoTopicoDAO.get(cdPlano, cdSecao, cdTopico, connect);
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_plano", "" + cdPlano, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_secao", "" + cdSecao, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmTodosPlanoTopico = PlanoTopicoDAO.find(criterios, connect);
			
			if(sentido == 0 && planoTopico.getNrOrdem() == 0){
				return new Result(1, "Tópico já está no topo da lista");
			}
			
			if(sentido == 0){ 
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_plano", "" + cdPlano, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_secao", "" + cdSecao, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("nr_ordem", "" + (planoTopico.getNrOrdem()-1), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmPlanoTopico = PlanoTopicoDAO.find(criterios, connect);
				PlanoTopico planoTopicoSuperior = null;
				while(rsmPlanoTopico.next()){
					planoTopicoSuperior = PlanoTopicoDAO.get(cdPlano, cdSecao, rsmPlanoTopico.getInt("cd_topico"), connect);
				}
				
				int nrOrdemAtual = planoTopico.getNrOrdem();
				planoTopico.setNrOrdem(planoTopicoSuperior.getNrOrdem());
				planoTopicoSuperior.setNrOrdem(nrOrdemAtual);
				
				if(PlanoTopicoDAO.update(planoTopico, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar topico");
				}
				
				if(PlanoTopicoDAO.update(planoTopicoSuperior, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar topico");
				}
				
			}
			
			if(sentido == 1 && planoTopico.getNrOrdem() == (rsmTodosPlanoTopico.size()-1)){
				return new Result(1, "Tópico já está no final da lista");
			}
			
			if(sentido == 1){ 
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_plano", "" + cdPlano, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_secao", "" + cdSecao, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("nr_ordem", "" + (planoTopico.getNrOrdem()+1), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmPlanoTopico = PlanoTopicoDAO.find(criterios, connect);
				PlanoTopico planoTopicoInferior = null;
				while(rsmPlanoTopico.next()){
					planoTopicoInferior = PlanoTopicoDAO.get(cdPlano, cdSecao, rsmPlanoTopico.getInt("cd_topico"), connect);
				}
				
				int nrOrdemAtual = planoTopico.getNrOrdem();
				planoTopico.setNrOrdem(planoTopicoInferior.getNrOrdem());
				planoTopicoInferior.setNrOrdem(nrOrdemAtual);
				
				if(PlanoTopicoDAO.update(planoTopico, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar topico");
				}
				
				if(PlanoTopicoDAO.update(planoTopicoInferior, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar topico");
				}
				
			}
			
		
			return new Result(1, "Tópico atualizado com sucesso");
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

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM acd_plano_topico", " ORDER BY nr_ordem", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getPlanoTopicoConteudo(int cdTurma, int cdCurso, int cdProfessor, int cdDisciplina) {
		return getPlanoTopicoConteudo(cdTurma, cdCurso, cdProfessor, cdDisciplina, null);
	}

	public static ResultSetMap getPlanoTopicoConteudo(int cdTurma, int cdCurso, int cdProfessor, int cdDisciplina, Connection connect) {
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
												 "  AND B.cd_disciplina = "+cdDisciplina +
												 "  AND B.st_plano = " + PlanoServices.VALIDADO+
												 "	AND (nm_secao like '%CONTEUDO%'"+ 
												 "			OR nm_secao like '%CONTEÚDO%')"+ 
												 " ORDER BY C.id_secao, nr_ordem ");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
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