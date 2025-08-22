package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.Util;

public class QuadroVagasServices {

	/* situações */
	public static final int ST_PENDENTE 	= 0;
	public static final int ST_VALIDADO  	= 1;
	public static final int ST_CONCLUIDO  	= 2;
	
	public static final String[] situacoes = {"Pendente", "Validado", "Concluído"};
	
	public static Result save(QuadroVagas quadroVagas){
		return save(quadroVagas, null, null);
	}

	public static Result save(QuadroVagas quadroVagas, Connection connect){
		return save(quadroVagas, null, connect); 
		
	}
	
	public static Result save(QuadroVagas quadroVagas, ArrayList<HashMap<String, Object>> rsmQuadroVagasCurso){
		return save(quadroVagas, rsmQuadroVagasCurso, null);
	}

	public static Result save(QuadroVagas quadroVagas, ArrayList<HashMap<String, Object>> rsmQuadroVagasCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(quadroVagas==null)
				return new Result(-1, "Erro ao salvar. QuadroVagas é nulo");

			int retorno;
			if(quadroVagas.getCdQuadroVagas()==0){
				retorno = QuadroVagasDAO.insert(quadroVagas, connect);
				quadroVagas.setCdQuadroVagas(retorno);
			}
			else {
				retorno = QuadroVagasDAO.update(quadroVagas, connect);
			}
			
			Instituicao instituicao = InstituicaoDAO.get(quadroVagas.getCdInstituicao(), connect);
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Escolas que estao no modo offline nao podem ter seu quadro de vagas alterado");
			}
							
			int cdPeriodoLetivo = 0;
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoRecente(instituicao.getCdInstituicao(), connect);
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			if(retorno>0 && rsmQuadroVagasCurso!=null) {
				
				//Caso não haja matrículas ainda cadastradas, o sistema irá remover todas as turmas para que se possa recadatra-las automaticamente com o novo quadro de vagas
				ResultSetMap rsmMatriculas = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula M WHERE M.cd_periodo_letivo = " + cdPeriodoLetivo + " AND M.st_matricula IN (" + MatriculaServices.ST_ATIVA + ", " + MatriculaServices.ST_PENDENTE + ", " + MatriculaServices.ST_CONCLUIDA + ", " + MatriculaServices.ST_FECHADA + ")").executeQuery());
				if(rsmMatriculas.size() == 0){
					Result result = TurmaServices.removeAllByInstituicaoPeriodo(quadroVagas.getCdInstituicao(), quadroVagas.getCdPeriodoLetivo(), connect);
					if(result.getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return result;
					}
				}
				//Itera sobre os quadroVagasCurso cadastrados na tela de quadro de vagas, faz uma inserção/atualização dos cadastrados, e remove os não cadastrados (caso houvesse algum registro anterior)
				for (int i=0; rsmQuadroVagasCurso!=null && i<rsmQuadroVagasCurso.size(); i++) {
					HashMap<String, Object> obj = rsmQuadroVagasCurso.get(i);
					
					//Verifica o quadro de vagas para turmas integrais do curso em questão
					int qtTurmasIntegral = (obj.get("QT_TURMAS_INTEGRAL") != null && !obj.get("QT_TURMAS_INTEGRAL").equals("") ? Integer.parseInt(String.valueOf(obj.get("QT_TURMAS_INTEGRAL"))) : 0);
					if(obj.get("QT_TURMAS_INTEGRAL")!=null && String.valueOf(obj.get("QT_TURMAS_INTEGRAL")) != "") {
						QuadroVagasCurso qvc = new QuadroVagasCurso(
								quadroVagas.getCdQuadroVagas(), 
								quadroVagas.getCdInstituicao(), 
								(int)obj.get("CD_CURSO"), 
								TurmaServices.TP_INTEGRAL, 
								qtTurmasIntegral, 
								(String.valueOf(obj.get("QT_VAGAS_INTEGRAL")) != null && String.valueOf(obj.get("QT_VAGAS_INTEGRAL")) != "" && String.valueOf(obj.get("QT_VAGAS_INTEGRAL")) != "null" ? Integer.parseInt(String.valueOf(obj.get("QT_VAGAS_INTEGRAL"))) : 0));
						QuadroVagasCursoServices.save(qvc, connect);
					}
					else if(obj.get("QT_TURMAS_INTEGRAL")!=null && String.valueOf(obj.get("QT_TURMAS_INTEGRAL")) == ""){
						if(QuadroVagasCursoDAO.delete(quadroVagas.getCdQuadroVagas(), quadroVagas.getCdInstituicao(), (int)obj.get("CD_CURSO"), TurmaServices.TP_INTEGRAL, connect) <= 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao deletar quadro vagas curso");
						}
					}
					//Caso haja alunos
					if(rsmMatriculas.size() > 0){
						ResultSetMap rsmTurmas = TurmaServices.getAllTurmasByPeriodo(quadroVagas.getCdPeriodoLetivo(), (int)obj.get("CD_CURSO"), TurmaServices.TP_INTEGRAL, connect);
						//O sistema irá comparar o número atual de turmas com o atualizado pelo quadro de vagas (naquele turno e curso), caso o atual seja MAIOR
						//o sistema pedirá que o usuário, antes de atualizar o quadro de vagas, remaneje os alunos e inative a turma sobressalente
						if(rsmTurmas.size() > qtTurmasIntegral){
							Curso curso = CursoDAO.get((int)obj.get("CD_CURSO"), connect);
							
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "O número de turmas atual do curso " + curso.getNmProdutoServico() + " no turno integral é maior do que o colocado no quadro de vagas.");
						}
					}
					//Verifica o quadro de vagas para turmas matutinas do curso em questão
					int qtTurmasMatutino = (obj.get("QT_TURMAS_MATUTINO") != null && !obj.get("QT_TURMAS_MATUTINO").equals("") ? Integer.parseInt(String.valueOf(obj.get("QT_TURMAS_MATUTINO"))) : 0);
					if(obj.get("QT_TURMAS_MATUTINO")!=null && String.valueOf(obj.get("QT_TURMAS_MATUTINO")) != "") {
						QuadroVagasCurso qvc = new QuadroVagasCurso(
								quadroVagas.getCdQuadroVagas(), 
								quadroVagas.getCdInstituicao(), 
								(int)obj.get("CD_CURSO"), 
								TurmaServices.TP_MATUTINO, 
								qtTurmasMatutino, 
								(String.valueOf(obj.get("QT_VAGAS_MATUTINO")) != null && String.valueOf(obj.get("QT_VAGAS_MATUTINO")) != "" && String.valueOf(obj.get("QT_VAGAS_MATUTINO")) != "null" ? Integer.parseInt(String.valueOf(obj.get("QT_VAGAS_MATUTINO"))) : 0));
						QuadroVagasCursoServices.save(qvc, connect);
					}
					else if(obj.get("QT_TURMAS_MATUTINO")!=null && String.valueOf(obj.get("QT_TURMAS_MATUTINO")) == ""){
						if(QuadroVagasCursoDAO.delete(quadroVagas.getCdQuadroVagas(), quadroVagas.getCdInstituicao(), (int)obj.get("CD_CURSO"), TurmaServices.TP_MATUTINO, connect) <= 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao deletar quadro vagas curso");
						}
					}
					
					//Caso haja alunos
					if(rsmMatriculas.size() > 0){
						ResultSetMap rsmTurmas = TurmaServices.getAllTurmasByPeriodo(quadroVagas.getCdPeriodoLetivo(), (int)obj.get("CD_CURSO"), TurmaServices.TP_MATUTINO, connect);
						//O sistema irá comparar o número atual de turmas com o atualizado pelo quadro de vagas (naquele turno e curso), caso o atual seja MAIOR
						//o sistema pedirá que o usuário, antes de atualizar o quadro de vagas, remaneje os alunos e inative a turma sobressalente
						if(rsmTurmas.size() > qtTurmasMatutino){
							Curso curso = CursoDAO.get((int)obj.get("CD_CURSO"), connect);
							
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "O número de turmas atual do curso " + curso.getNmProdutoServico() + " no turno matutino ("+rsmTurmas.size()+") é maior do que o colocado no quadro de vagas ("+qtTurmasMatutino+"). Remaneje os alunos na turma sobressalente e intaive-a para atualizar o quadro de vagas");
						}
					}
					//Verifica o quadro de vagas para turmas vespertinas do curso em questão
					int qtTurmasVespertino = (obj.get("QT_TURMAS_VESPERTINO") != null && !obj.get("QT_TURMAS_VESPERTINO").equals("") ? Integer.parseInt(String.valueOf(obj.get("QT_TURMAS_VESPERTINO"))) : 0);
					if(obj.get("QT_TURMAS_VESPERTINO")!=null && String.valueOf(obj.get("QT_TURMAS_VESPERTINO")) != "") {
						QuadroVagasCurso qvc = new QuadroVagasCurso(
								quadroVagas.getCdQuadroVagas(), 
								quadroVagas.getCdInstituicao(), 
								(int)obj.get("CD_CURSO"), 
								TurmaServices.TP_VESPERTINO, 
								qtTurmasVespertino, 
								(String.valueOf(obj.get("QT_VAGAS_VESPERTINO")) != null && String.valueOf(obj.get("QT_VAGAS_VESPERTINO")) != "" && String.valueOf(obj.get("QT_VAGAS_VESPERTINO")) != "null" ? Integer.parseInt(String.valueOf(obj.get("QT_VAGAS_VESPERTINO"))) : 0));
						QuadroVagasCursoServices.save(qvc, connect);
					}
					else if(obj.get("QT_TURMAS_VESPERTINO")!=null && String.valueOf(obj.get("QT_TURMAS_VESPERTINO")) == ""){
						if(QuadroVagasCursoDAO.delete(quadroVagas.getCdQuadroVagas(), quadroVagas.getCdInstituicao(), (int)obj.get("CD_CURSO"), TurmaServices.TP_VESPERTINO, connect) <= 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao deletar quadro vagas curso");
						}
					}
					
					//Caso haja alunos
					if(rsmMatriculas.size() > 0){
						ResultSetMap rsmTurmas = TurmaServices.getAllTurmasByPeriodo(quadroVagas.getCdPeriodoLetivo(), (int)obj.get("CD_CURSO"), TurmaServices.TP_VESPERTINO, connect);
						//O sistema irá comparar o número atual de turmas com o atualizado pelo quadro de vagas (naquele turno e curso), caso o atual seja MAIOR
						//o sistema pedirá que o usuário, antes de atualizar o quadro de vagas, remaneje os alunos e inative a turma sobressalente
						if(rsmTurmas.size() > qtTurmasVespertino){
							Curso curso = CursoDAO.get((int)obj.get("CD_CURSO"), connect);
							
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "O número de turmas atual do curso " + curso.getNmProdutoServico() + " no turno vespertino ("+rsmTurmas.size()+") é maior do que o colocado no quadro de vagas ("+qtTurmasVespertino+"). Remaneje os alunos na turma sobressalente e intaive-a para atualizar o quadro de vagas");
						}
					}
					
					//Verifica o quadro de vagas para turmas noturnas do curso em questão
					int qtTurmasNoturno = (obj.get("QT_TURMAS_NOTURNO") != null && !obj.get("QT_TURMAS_NOTURNO").equals("") ? Integer.parseInt(String.valueOf(obj.get("QT_TURMAS_NOTURNO"))) : 0);
					if(obj.get("QT_TURMAS_NOTURNO")!=null && String.valueOf(obj.get("QT_TURMAS_NOTURNO")) != "") {
						QuadroVagasCurso qvc = new QuadroVagasCurso(
								quadroVagas.getCdQuadroVagas(), 
								quadroVagas.getCdInstituicao(), 
								(int)obj.get("CD_CURSO"), 
								TurmaServices.TP_NOTURNO, 
								qtTurmasNoturno, 
								(String.valueOf(obj.get("QT_VAGAS_NOTURNO")) != null && String.valueOf(obj.get("QT_VAGAS_NOTURNO")) != "" && String.valueOf(obj.get("QT_VAGAS_NOTURNO")) != "null" ? Integer.parseInt(String.valueOf(obj.get("QT_VAGAS_NOTURNO"))) : 0));
						QuadroVagasCursoServices.save(qvc, connect);
					}
					else if(obj.get("QT_TURMAS_NOTURNO")!=null && String.valueOf(obj.get("QT_TURMAS_NOTURNO")) == ""){
						if(QuadroVagasCursoDAO.delete(quadroVagas.getCdQuadroVagas(), quadroVagas.getCdInstituicao(), (int)obj.get("CD_CURSO"), TurmaServices.TP_NOTURNO, connect) <= 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao deletar quadro vagas curso");
						}
					}
					
					//Caso haja alunos
					if(rsmMatriculas.size() > 0){
						ResultSetMap rsmTurmas = TurmaServices.getAllTurmasByPeriodo(quadroVagas.getCdPeriodoLetivo(), (int)obj.get("CD_CURSO"), TurmaServices.TP_NOTURNO, connect);
						//O sistema irá comparar o número atual de turmas com o atualizado pelo quadro de vagas (naquele turno e curso), caso o atual seja MAIOR
						//o sistema pedirá que o usuário, antes de atualizar o quadro de vagas, remaneje os alunos e inative a turma sobressalente
						if(rsmTurmas.size() > qtTurmasNoturno){
							Curso curso = CursoDAO.get((int)obj.get("CD_CURSO"), connect);
							
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "O número de turmas atual do curso " + curso.getNmProdutoServico() + " no turno noturno ("+rsmTurmas.size()+") é maior do que o colocado no quadro de vagas ("+qtTurmasNoturno+"). Remaneje os alunos na turma sobressalente e intaive-a para atualizar o quadro de vagas");
						}
					}
					
					
				}
				
				//Caso ainda não haja matrículas, o sistema irá adicionar as turmas de acordo ao quadro de vagas cadastrado
				if(rsmMatriculas.size() == 0){
					adicionarTurmas(quadroVagas.getCdPeriodoLetivo(), connect);
				}
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "QUADROVAGAS", quadroVagas);
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
	public static Result remove(int cdQuadroVagas, int cdInstituicao){
		return remove(cdQuadroVagas, cdInstituicao, false, null);
	}
	public static Result remove(int cdQuadroVagas, int cdInstituicao, boolean cascade){
		return remove(cdQuadroVagas, cdInstituicao, cascade, null);
	}
	public static Result remove(int cdQuadroVagas, int cdInstituicao, boolean cascade, Connection connect){
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
			retorno = QuadroVagasDAO.delete(cdQuadroVagas, cdInstituicao, connect);
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
	public static Result removeByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_periodo_letivo", "" + cdPeriodoLetivo, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = QuadroVagasDAO.find(criterios, connect);
			while (rsm.next()) {
				int ret = QuadroVagasDAO.delete(rsm.getInt("cd_quadro_vagas"), cdInstituicao, connect);
				if(ret<0){
					Conexao.rollback(connect);
					return new Result(ret, "Erro ao excluir quadro vagas");
				}
			}
			
			if (isConnectionNull)
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_quadro_vagas");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getQuadroVagasAtual(int cdInstituicao) {
		return getQuadroVagasAtual(cdInstituicao, 0);
	}
	
	public static ResultSetMap getQuadroVagasAtual(int cdInstituicao, int cdCurso) {
		ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao);
		if(rsmPeriodoAtual.next()){
			return getQuadroVagas(cdInstituicao, rsmPeriodoAtual.getInt("cd_periodo_letivo"), cdCurso);
		}
		
		return null;
	}
	
	public static ResultSetMap getQuadroVagasRecente(int cdInstituicao) {
		return getQuadroVagasRecente(cdInstituicao, 0);
	}
	
	public static ResultSetMap getQuadroVagasRecente(int cdInstituicao, int cdCurso) {
		ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdInstituicao);
		if(rsmPeriodoAtual.next()){
			return getQuadroVagas(cdInstituicao, rsmPeriodoAtual.getInt("cd_periodo_letivo"), cdCurso);
		}
		
		return null;
	}
	
	public static ResultSetMap getQuadroVagas(int cdInstituicao, int cdPeriodoLetivo) {
		return getQuadroVagas(cdInstituicao, cdPeriodoLetivo, 0);
	}
	
	public static ResultSetMap getQuadroVagas(int cdInstituicao, int cdPeriodoLetivo, int cdCurso) {
		
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.cd_periodo_letivo", "" + cdPeriodoLetivo, ItemComparator.EQUAL, Types.INTEGER));
		if(cdCurso > 0)
			criterios.add(new ItemComparator("CD_CURSO", "" + cdCurso, ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsm = findCompleto(criterios, null);
		if(rsm.next()){
			rsm.beforeFirst();
			return rsm;
		}
		else{
			if(QuadroVagasDAO.insert(new QuadroVagas(0/*cdQuadroVagas*/, cdInstituicao, cdPeriodoLetivo, Util.getDataAtual(), ST_PENDENTE, null/*txtObservacao*/)) <= 0){
				return null;
			}
			return findCompleto(criterios, null);
		}
	}
	
	public static ResultSetMap getQuadroVagasAnoAnterior(int cdInstituicao, int cdPeriodoLetivo) {
		
		InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivo);
		int anoPeriodo = Integer.parseInt(instituicaoPeriodo.getNmPeriodoLetivo());
		anoPeriodo--;
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("nm_periodo_letivo", "" + anoPeriodo, ItemComparator.EQUAL, Types.VARCHAR));
		ResultSetMap rsmInstituicaoPeriodoAnterior = InstituicaoPeriodoDAO.find(criterios);
		InstituicaoPeriodo instituicaoPeriodoAnterior = null;
		if(rsmInstituicaoPeriodoAnterior.next()){
			instituicaoPeriodoAnterior = InstituicaoPeriodoDAO.get(rsmInstituicaoPeriodoAnterior.getInt("cd_periodo_letivo"));
		}
		
		if(instituicaoPeriodoAnterior == null){
			return new ResultSetMap();
		}
		
		criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.cd_periodo_letivo", "" + instituicaoPeriodoAnterior.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsm = findCompleto(criterios, null);
		if(rsm.next()){
			rsm.beforeFirst();
			return rsm;
		}
		else{
			if(QuadroVagasDAO.insert(new QuadroVagas(0/*cdQuadroVagas*/, cdInstituicao, cdPeriodoLetivo, Util.getDataAtual(), ST_PENDENTE, null/*txtObservacao*/)) <= 0){
				return null;
			}
			return findCompleto(criterios, null);
		}
	}
	
	
	public static ResultSetMap getLoteQuadroVagas() {
		Connection connect = Conexao.conectar();
		
		ResultSetMap rsmInstituicoes = InstituicaoServices.getAll(connect);
		
		while(rsmInstituicoes.next()) {
			int cdInstituicao = rsmInstituicoes.getInt("CD_INSTITUICAO");
			ResultSetMap rsmPeriodos = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
			int cdPeriodo = 0;
			
			if(rsmPeriodos.next())
				cdPeriodo = rsmPeriodos.getInt("CD_PERIODO_LETIVO");
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.cd_periodo_letivo", "" + cdPeriodo, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = findCompleto(criterios, connect);
			
			rsmInstituicoes.setValueToField("RSM_QUADROS", rsm.getLines());
		}
		
		
		rsmInstituicoes.beforeFirst();
		
		return rsmInstituicoes;
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM acd_quadro_vagas", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, Connection connect) {
		
		
		int cdCurso = 0;
		int cdInstituicao = 0;
		int cdPeriodoLetivo = 0;
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("CD_CURSO"))
				cdCurso = Integer.parseInt(criterios.get(i).getValue());
			else if (criterios.get(i).getColumn().equalsIgnoreCase("A.cd_instituicao")){
				cdInstituicao = Integer.parseInt(criterios.get(i).getValue());
				crt.add(criterios.get(i));
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("A.cd_periodo_letivo")){
				cdPeriodoLetivo = Integer.parseInt(criterios.get(i).getValue());
				crt.add(criterios.get(i));
			}
			else
				crt.add(criterios.get(i));
			
		}
		
		if(cdPeriodoLetivo == 0){
			ResultSetMap rsmPeriodoRecente = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdInstituicao, connect);
			if(rsmPeriodoRecente.next()){
				cdPeriodoLetivo = rsmPeriodoRecente.getInt("cd_periodo_letivo");
			}
		}
		String sql = "SELECT A.*, B.nm_pessoa as nm_instituicao, C.nm_periodo_letivo"
				+ " FROM acd_quadro_vagas A"
				+ " LEFT OUTER JOIN grl_pessoa B ON (A.cd_instituicao = B.cd_pessoa)"
				+ " LEFT OUTER JOIN acd_instituicao_periodo C ON (A.cd_periodo_letivo = C.cd_periodo_letivo) ";
		
		ResultSetMap rsmQuadroVagas = Search.find(sql, "", crt, connect!=null ? connect : Conexao.conectar(), connect==null);
		while(rsmQuadroVagas.next()) {
			ArrayList<ItemComparator> ctr = new ArrayList<ItemComparator>();
			ctr.add(new ItemComparator("CD_QUADRO_VAGAS", rsmQuadroVagas.getInt("CD_QUADRO_VAGAS")+"", ItemComparator.EQUAL, Types.INTEGER));
			ctr.add(new ItemComparator("CD_INSTITUICAO", rsmQuadroVagas.getInt("CD_INSTITUICAO")+"", ItemComparator.EQUAL, Types.INTEGER));
			if(cdCurso > 0)			
				ctr.add(new ItemComparator("A.CD_CURSO", cdCurso+"", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = QuadroVagasCursoServices.find(ctr, connect);
			int qtTotalVagas           = 0;
			int qtTotalVagasIntegral   = 0;
			int qtTotalVagaMatutino    = 0;
			int qtTotalVagasVespertino = 0;
			int qtTotalVagasNoturno    = 0;
			
			int qtTotalTurmas           = 0;
			int qtTotalTurmasIntegral   = 0;
			int qtTotalTurmasMatutino   = 0;
			int qtTotalTurmasVespertino = 0;
			int qtTotalTurmasNoturno    = 0;
			
			ResultSetMap rsmFinal = new ResultSetMap();
			HashMap<String, HashMap<String, Object>> mapResultado = new HashMap<String, HashMap<String,Object>>();
			while(rsm.next()) {
				qtTotalVagas  += rsm.getInt("QT_VAGAS");
				qtTotalTurmas += rsm.getInt("QT_TURMAS");

				String codConjunto = rsm.getString("cd_quadro_vagas") + rsm.getString("cd_instituicao") + rsm.getString("cd_curso");
				if(mapResultado.get(codConjunto) == null){
					HashMap<String, Object> register = rsm.getRegister();
					switch(rsm.getInt("tp_turno")) {
						case TurmaServices.TP_MATUTINO:
							qtTotalVagaMatutino += rsm.getInt("QT_VAGAS");
							qtTotalTurmasMatutino += rsm.getInt("QT_TURMAS");
							register.put("QT_VAGAS_MATUTINO", rsm.getInt("QT_VAGAS"));
							register.put("QT_TURMAS_MATUTINO", rsm.getInt("QT_TURMAS"));
							register.put("QT_MATRICULADOS_MATUTINO", InstituicaoServices.getMatriculadosPorCursoTurno(rsm.getInt("cd_instituicao"), cdPeriodoLetivo, rsm.getInt("cd_curso"), rsm.getInt("tp_turno")));
							break;
						case TurmaServices.TP_VESPERTINO:
							qtTotalVagasVespertino += rsm.getInt("QT_VAGAS");
							qtTotalTurmasVespertino += rsm.getInt("QT_TURMAS");
							register.put("QT_VAGAS_VESPERTINO", rsm.getInt("QT_VAGAS"));
							register.put("QT_TURMAS_VESPERTINO", rsm.getInt("QT_TURMAS"));
							register.put("QT_MATRICULADOS_VESPERTINO", InstituicaoServices.getMatriculadosPorCursoTurno(rsm.getInt("cd_instituicao"), cdPeriodoLetivo, rsm.getInt("cd_curso"), rsm.getInt("tp_turno")));
							break;
						case TurmaServices.TP_NOTURNO:
							qtTotalVagasNoturno += rsm.getInt("QT_VAGAS");
							qtTotalTurmasNoturno += rsm.getInt("QT_TURMAS");
							register.put("QT_VAGAS_NOTURNO", rsm.getInt("QT_VAGAS"));
							register.put("QT_TURMAS_NOTURNO", rsm.getInt("QT_TURMAS"));
							register.put("QT_MATRICULADOS_NOTURNO", InstituicaoServices.getMatriculadosPorCursoTurno(rsm.getInt("cd_instituicao"), cdPeriodoLetivo, rsm.getInt("cd_curso"), rsm.getInt("tp_turno")));
							break;
						case TurmaServices.TP_INTEGRAL:
							qtTotalVagasIntegral += rsm.getInt("QT_VAGAS");
							qtTotalTurmasIntegral += rsm.getInt("QT_TURMAS");
							register.put("QT_VAGAS_INTEGRAL", rsm.getInt("QT_VAGAS"));
							register.put("QT_TURMAS_INTEGRAL", rsm.getInt("QT_TURMAS"));
							register.put("QT_MATRICULADOS_INTEGRAL", InstituicaoServices.getMatriculadosPorCursoTurno(rsm.getInt("cd_instituicao"), cdPeriodoLetivo, rsm.getInt("cd_curso"), rsm.getInt("tp_turno")));
							break;
					}
					mapResultado.put(codConjunto, register);
				}
				else{
					HashMap<String, Object> register = mapResultado.get(codConjunto);
					switch(rsm.getInt("tp_turno")) {
					case TurmaServices.TP_MATUTINO:
						qtTotalVagaMatutino += rsm.getInt("QT_VAGAS");
						qtTotalTurmasMatutino += rsm.getInt("QT_TURMAS");
						register.put("QT_VAGAS_MATUTINO", rsm.getInt("QT_VAGAS"));
						register.put("QT_TURMAS_MATUTINO", rsm.getInt("QT_TURMAS"));
						register.put("QT_MATRICULADOS_MATUTINO", InstituicaoServices.getMatriculadosPorCursoTurno(rsm.getInt("cd_instituicao"), cdPeriodoLetivo, rsm.getInt("cd_curso"), rsm.getInt("tp_turno")));
						break;
					case TurmaServices.TP_VESPERTINO:
						qtTotalVagasVespertino += rsm.getInt("QT_VAGAS");
						qtTotalTurmasVespertino += rsm.getInt("QT_TURMAS");
						register.put("QT_VAGAS_VESPERTINO", rsm.getInt("QT_VAGAS"));
						register.put("QT_TURMAS_VESPERTINO", rsm.getInt("QT_TURMAS"));
						register.put("QT_MATRICULADOS_VESPERTINO", InstituicaoServices.getMatriculadosPorCursoTurno(rsm.getInt("cd_instituicao"), cdPeriodoLetivo, rsm.getInt("cd_curso"), rsm.getInt("tp_turno")));
						break;
					case TurmaServices.TP_NOTURNO:
						qtTotalVagasNoturno += rsm.getInt("QT_VAGAS");
						qtTotalTurmasNoturno += rsm.getInt("QT_TURMAS");
						register.put("QT_VAGAS_NOTURNO", rsm.getInt("QT_VAGAS"));
						register.put("QT_TURMAS_NOTURNO", rsm.getInt("QT_TURMAS"));
						register.put("QT_MATRICULADOS_NOTURNO", InstituicaoServices.getMatriculadosPorCursoTurno(rsm.getInt("cd_instituicao"), cdPeriodoLetivo, rsm.getInt("cd_curso"), rsm.getInt("tp_turno")));
						break;
					case TurmaServices.TP_INTEGRAL:
						qtTotalVagasIntegral += rsm.getInt("QT_VAGAS");
						qtTotalTurmasIntegral += rsm.getInt("QT_TURMAS");
						register.put("QT_VAGAS_INTEGRAL", rsm.getInt("QT_VAGAS"));
						register.put("QT_TURMAS_INTEGRAL", rsm.getInt("QT_TURMAS"));
						register.put("QT_MATRICULADOS_INTEGRAL", InstituicaoServices.getMatriculadosPorCursoTurno(rsm.getInt("cd_instituicao"), cdPeriodoLetivo, rsm.getInt("cd_curso"), rsm.getInt("tp_turno")));
						break;
					}
					mapResultado.put(codConjunto, register);
					
				}
			}
			rsm.beforeFirst();
			for(String chave : mapResultado.keySet()){
				rsmFinal.addRegister(mapResultado.get(chave));
			}
			rsmFinal.beforeFirst();
			
			ArrayList<Integer> conjuntoCodigosCurso = new ArrayList<Integer>();
			while(rsmFinal.next()){
				conjuntoCodigosCurso.add(rsmFinal.getInt("cd_curso"));
			}
			rsmFinal.beforeFirst();
			ResultSetMap rsmCursosRestantes = CursoServices.getAllCursosOf(rsmQuadroVagas.getInt("CD_INSTITUICAO"), false, false, false, false, cdPeriodoLetivo, connect);
			while(rsmCursosRestantes.next()){
				if(!conjuntoCodigosCurso.contains(rsmCursosRestantes.getInt("cd_curso")))
					rsmFinal.addRegister(rsmCursosRestantes.getRegister());
			}
			rsmFinal.beforeFirst();
			while(rsmFinal.next()){
				if(rsmFinal.getString("nm_curso")==null)
					rsmFinal.setValueToField("NM_CURSO", "");
			}
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_CURSO");
			rsmFinal.orderBy(fields);
			rsmFinal.beforeFirst();
			
			rsmQuadroVagas.setValueToField("QT_TOTAL_VAGAS", qtTotalVagas);
			rsmQuadroVagas.setValueToField("QT_TOTAL_TURMAS", qtTotalTurmas);
			
			rsmQuadroVagas.setValueToField("QT_TOTAL_VAGAS_INTEGRAL", qtTotalVagasIntegral);
			rsmQuadroVagas.setValueToField("QT_TOTAL_VAGAS_MATUTINO", qtTotalVagaMatutino);
			rsmQuadroVagas.setValueToField("QT_TOTAL_VAGAS_VESPERTINO", qtTotalVagasVespertino);
			rsmQuadroVagas.setValueToField("QT_TOTAL_VAGAS_NOTURNO", qtTotalVagasNoturno);
			
			rsmQuadroVagas.setValueToField("QT_TOTAL_TURMAS_INTEGRAL", qtTotalTurmasIntegral);
			rsmQuadroVagas.setValueToField("QT_TOTAL_TURMAS_MATUTINO", qtTotalTurmasMatutino);
			rsmQuadroVagas.setValueToField("QT_TOTAL_TURMAS_VESPERTINO", qtTotalTurmasVespertino);
			rsmQuadroVagas.setValueToField("QT_TOTAL_TURMAS_NOTURNO", qtTotalTurmasNoturno);
				
			rsmQuadroVagas.setValueToField("RSM_QUADRO_VAGAS_CURSO", rsmFinal);
		}
		rsmQuadroVagas.beforeFirst();
		return rsmQuadroVagas;
	}
	
	/**
	 * Metodo usado para criar turmas a partir do quadro de vagas 
	 * @param cdPeriodoLetivo
	 * @return
	 */
	public static Result adicionarTurmas(int cdPeriodoLetivo){
		return adicionarTurmas(cdPeriodoLetivo, null);
	}
	
	public static Result adicionarTurmas(int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		
		try {
		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect);
			
			//Faz uma busca, para saber se existe quadro de vagas na instituicao e periodo letivo passado
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_instituicao", "" + instituicaoPeriodo.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.cd_periodo_letivo", "" + instituicaoPeriodo.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = findCompleto(criterios, connect);
			if(rsm.next()){
				ResultSetMap rsmQuadroVagasCurso = (ResultSetMap)rsm.getObject("RSM_QUADRO_VAGAS_CURSO");
				//Itera sobre os quadro de vagas cursos
				while(rsmQuadroVagasCurso.next()){
					
					//Caso exista turmas já cadastradas para esse curso, o sistema passa para o próximo
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_instituicao", "" + instituicaoPeriodo.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_periodo_letivo", "" + instituicaoPeriodo.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_curso", "" + rsmQuadroVagasCurso.getInt("cd_curso"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("nm_turma", "TRANSFERENCIA", ItemComparator.DIFFERENT, Types.VARCHAR));
					criterios.add(new ItemComparator("st_turma", "" + TurmaServices.ST_INATIVO, ItemComparator.DIFFERENT, Types.INTEGER));
					if(TurmaDAO.find(criterios, connect).size() > 0){
						continue;
					}
					
					
					//Inicia o nome das turmas por 'A' e vai passando para B, C, D e assim por diante
					char nomeTurma = 'A';
					
					int qtTurmasMatutino = rsmQuadroVagasCurso.getInt("QT_TURMAS_MATUTINO");
					int qtVagasMatutino = rsmQuadroVagasCurso.getInt("QT_VAGAS_MATUTINO");
					
					//Caso o curso seja regular, coloca cinco dias de atendimento por semana
					int tpAtendimento = TurmaServices.TP_ATENDIMENTO_NAO_APLICA;
					int qtDiasSemana = 5;
					
					//Caso o curso seja de aee, coloca apenas dois dias de atendimento por semana
					if(rsmQuadroVagasCurso.getInt("cd_curso") == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)){
						tpAtendimento = TurmaServices.TP_ATENDIMENTO_AEE;
						qtDiasSemana = 2;
					}
					
					//Caso o curso seja de atividade complementar, permanece cinco dias de atendimento por semana
					if(rsmQuadroVagasCurso.getInt("cd_curso") == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)){
						tpAtendimento = TurmaServices.TP_ATENDIMENTO_ATIVIDADE_COMPLEMENTAR;
					}
					
					//Faz as inserções de turma para o turno matutino
					if(qtTurmasMatutino > 0){
						//Faz o calculo de vagas para cada turma, dividindo entre o cadastrado no quadro de vagas
						//e mantem a sobra da divisão para colocar na ultima turma
						int qtVagasPorTurma = qtVagasMatutino / qtTurmasMatutino;
						int qtVagasPorTurmaSobras = qtVagasMatutino % qtTurmasMatutino;
						
						//Itera sobre qtTurmas até a variável zerar, diminuindo em 1 a cada iteração
						while(qtTurmasMatutino > 0){
							
							//Busca a matriz do curso
							int cdMatriz = 0;
							ResultSetMap rsmCursoMatriz = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_curso_matriz WHERE cd_curso = " + rsmQuadroVagasCurso.getInt("cd_curso")).executeQuery());
							if(rsmCursoMatriz.next())
								cdMatriz = rsmCursoMatriz.getInt("cd_matriz");
							
							//Busca op módulo do curso
							int cdCursoModulo = 0;
							ResultSetMap rsmCursoModulo = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_curso_modulo WHERE cd_curso = " + rsmQuadroVagasCurso.getInt("cd_curso")).executeQuery());
							if(rsmCursoModulo.next())
								cdCursoModulo = rsmCursoModulo.getInt("cd_curso_modulo");
							
							//Adiciona a turma
							Turma turma = new Turma(0/*cdTurma*/, cdMatriz, cdPeriodoLetivo, "" + nomeTurma, Util.getDataAtual(), null, TurmaServices.TP_MATUTINO, 0/*cdCategoriaMensalidade*/, 0/*cdCategoriaMatricula*/, TurmaServices.ST_ATIVO, 0/*cdTabelaPreco*/, instituicaoPeriodo.getCdInstituicao(), rsmQuadroVagasCurso.getInt("cd_curso"), qtVagasPorTurma, cdCursoModulo, null, qtDiasSemana, tpAtendimento, TurmaServices.TP_MODALIDADE_REGULAR, null, 0, 0, 0/*cdTurmaAnterior*/, -1/*tpTurnoAtividadeComplementar*/, 0/*tpLocalDiferenciado*/);
							turma.setCdTurma(TurmaDAO.insert(turma, connect));
							if(turma.getCdTurma() < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro ao inserir turma");
							}
							
							//Adiciona +1 no nome da turma, passando assim o char para uma letra posterior
							nomeTurma++;
							
							//Decresce um da turma, para cumprir a exigência do while
							qtTurmasMatutino--;
							
							//Se a quantidade de turma chegar a zero, significa que é a ultima turma, então vai adicionar a quantidade de sobra nas vagas
							if(qtTurmasMatutino == 0){
								turma.setQtVagas(turma.getQtVagas() + qtVagasPorTurmaSobras);
								if(TurmaDAO.update(turma, connect) < 0){
									if(isConnectionNull)
										Conexao.rollback(connect);
									return new Result(-1, "Erro ao atualizar turma");
								}
							}
						}
					}
					
					//Repete o mesmo processo para vespertino, integral e noturno
					
					int qtTurmasVespertino = rsmQuadroVagasCurso.getInt("QT_TURMAS_VESPERTINO");
					int qtVagasVespertino = rsmQuadroVagasCurso.getInt("QT_VAGAS_VESPERTINO");
					
					if(qtTurmasVespertino > 0){
						int qtVagasPorTurma = qtVagasVespertino / qtTurmasVespertino;
						int qtVagasPorTurmaSobras = qtVagasVespertino % qtTurmasVespertino;
						
						while(qtTurmasVespertino > 0){
							
							int cdMatriz = 0;
							ResultSetMap rsmCursoMatriz = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_curso_matriz WHERE cd_curso = " + rsmQuadroVagasCurso.getInt("cd_curso")).executeQuery());
							if(rsmCursoMatriz.next())
								cdMatriz = rsmCursoMatriz.getInt("cd_matriz");
							
							int cdCursoModulo = 0;
							ResultSetMap rsmCursoModulo = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_curso_modulo WHERE cd_curso = " + rsmQuadroVagasCurso.getInt("cd_curso")).executeQuery());
							if(rsmCursoModulo.next())
								cdCursoModulo = rsmCursoModulo.getInt("cd_curso_modulo");
							
							
							Turma turma = new Turma(0/*cdTurma*/, cdMatriz, cdPeriodoLetivo, "" + nomeTurma, Util.getDataAtual(), null, TurmaServices.TP_VESPERTINO, 0/*cdCategoriaMensalidade*/, 0/*cdCategoriaMatricula*/, TurmaServices.ST_ATIVO, 0/*cdTabelaPreco*/, instituicaoPeriodo.getCdInstituicao(), rsmQuadroVagasCurso.getInt("cd_curso"), qtVagasPorTurma, cdCursoModulo, null, qtDiasSemana, tpAtendimento, TurmaServices.TP_MODALIDADE_REGULAR, null, 0, 0, 0/*cdTurmaAnterior*/, -1/*tpTurnoAtividadeComplementar*/, 0/*tpLocalDiferenciado*/);
							turma.setCdTurma(TurmaDAO.insert(turma, connect));
							if(turma.getCdTurma() < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro ao inserir turma");
							}
							nomeTurma++;
							qtTurmasVespertino--;
							
							if(qtTurmasVespertino == 0){
								turma.setQtVagas(turma.getQtVagas() + qtVagasPorTurmaSobras);
								if(TurmaDAO.update(turma, connect) < 0){
									if(isConnectionNull)
										Conexao.rollback(connect);
									return new Result(-1, "Erro ao atualizar turma");
								}
							}
						}
					}
					
					int qtTurmasNoturno = rsmQuadroVagasCurso.getInt("QT_TURMAS_NOTURNO");
					int qtVagasNoturno = rsmQuadroVagasCurso.getInt("QT_VAGAS_NOTURNO");
					
					if(qtTurmasNoturno > 0){
						int qtVagasPorTurma = qtVagasNoturno / qtTurmasNoturno;
						int qtVagasPorTurmaSobras = qtVagasNoturno % qtTurmasNoturno;
						
						while(qtTurmasNoturno > 0){
							
							int cdMatriz = 0;
							ResultSetMap rsmCursoMatriz = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_curso_matriz WHERE cd_curso = " + rsmQuadroVagasCurso.getInt("cd_curso")).executeQuery());
							if(rsmCursoMatriz.next())
								cdMatriz = rsmCursoMatriz.getInt("cd_matriz");
							
							int cdCursoModulo = 0;
							ResultSetMap rsmCursoModulo = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_curso_modulo WHERE cd_curso = " + rsmQuadroVagasCurso.getInt("cd_curso")).executeQuery());
							if(rsmCursoModulo.next())
								cdCursoModulo = rsmCursoModulo.getInt("cd_curso_modulo");
							
							
							Turma turma = new Turma(0/*cdTurma*/, cdMatriz, cdPeriodoLetivo, "" + nomeTurma, Util.getDataAtual(), null, TurmaServices.TP_NOTURNO, 0/*cdCategoriaMensalidade*/, 0/*cdCategoriaMatricula*/, TurmaServices.ST_ATIVO, 0/*cdTabelaPreco*/, instituicaoPeriodo.getCdInstituicao(), rsmQuadroVagasCurso.getInt("cd_curso"), qtVagasPorTurma, cdCursoModulo, null, qtDiasSemana, tpAtendimento, TurmaServices.TP_MODALIDADE_REGULAR, null, 0, 0, 0/*cdTurmaAnterior*/, -1/*tpTurnoAtividadeComplementar*/, 0/*tpLocalDiferenciado*/);
							turma.setCdTurma(TurmaDAO.insert(turma, connect));
							if(turma.getCdTurma() < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro ao inserir turma");
							}
							nomeTurma++;
							qtTurmasNoturno--;
							
							if(qtTurmasNoturno == 0){
								turma.setQtVagas(turma.getQtVagas() + qtVagasPorTurmaSobras);
								if(TurmaDAO.update(turma, connect) < 0){
									if(isConnectionNull)
										Conexao.rollback(connect);
									return new Result(-1, "Erro ao atualizar turma");
								}
							}
						}
					}
					
					int qtTurmasIntegral = rsmQuadroVagasCurso.getInt("QT_TURMAS_INTEGRAL");
					int qtVagasIntegral = rsmQuadroVagasCurso.getInt("QT_VAGAS_INTEGRAL");
					
					if(qtTurmasIntegral > 0){
						int qtVagasPorTurma = qtVagasIntegral / qtTurmasIntegral;
						int qtVagasPorTurmaSobras = qtVagasIntegral % qtTurmasIntegral;
						
						while(qtTurmasIntegral > 0){
							
							int cdMatriz = 0;
							ResultSetMap rsmCursoMatriz = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_curso_matriz WHERE cd_curso = " + rsmQuadroVagasCurso.getInt("cd_curso")).executeQuery());
							if(rsmCursoMatriz.next())
								cdMatriz = rsmCursoMatriz.getInt("cd_matriz");
							
							int cdCursoModulo = 0;
							ResultSetMap rsmCursoModulo = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_curso_modulo WHERE cd_curso = " + rsmQuadroVagasCurso.getInt("cd_curso")).executeQuery());
							if(rsmCursoModulo.next())
								cdCursoModulo = rsmCursoModulo.getInt("cd_curso_modulo");
							
							
							Turma turma = new Turma(0/*cdTurma*/, cdMatriz, cdPeriodoLetivo, "" + nomeTurma, Util.getDataAtual(), null, TurmaServices.TP_INTEGRAL, 0/*cdCategoriaMensalidade*/, 0/*cdCategoriaMatricula*/, TurmaServices.ST_ATIVO, 0/*cdTabelaPreco*/, instituicaoPeriodo.getCdInstituicao(), rsmQuadroVagasCurso.getInt("cd_curso"), qtVagasPorTurma, cdCursoModulo, null, qtDiasSemana, tpAtendimento, TurmaServices.TP_MODALIDADE_REGULAR, null, 0, 0, 0/*cdTurmaAnterior*/, -1/*tpTurnoAtividadeComplementar*/, 0/*tpLocalDiferenciado*/);
							turma.setCdTurma(TurmaDAO.insert(turma, connect));
							if(turma.getCdTurma() < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro ao inserir turma");
							}
							nomeTurma++;
							qtTurmasIntegral--;
							
							if(qtTurmasIntegral == 0){
								turma.setQtVagas(turma.getQtVagas() + qtVagasPorTurmaSobras);
								if(TurmaDAO.update(turma, connect) < 0){
									if(isConnectionNull)
										Conexao.rollback(connect);
									return new Result(-1, "Erro ao atualizar turma");
								}
							}
						}
						
					}
					
				}
				rsmQuadroVagasCurso.beforeFirst();
				
				//Caso não haja cursos cadastrados na instituição, da uma mensagem de erro
				if(rsmQuadroVagasCurso.size() == 0){
					return new Result(1, "Não há cursos cadastrados nessa Instituição");
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Turmas criadas com sucesso");
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result excluirTurmas(int cdPeriodoLetivo){
		return excluirTurmas(cdPeriodoLetivo, null);
	}
	
	public static Result excluirTurmas(int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		
		try {
		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_periodo_letivo", "" + cdPeriodoLetivo, ItemComparator.EQUAL, Types.INTEGER));
			if(MatriculaDAO.find(criterios, connect).size() > 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Turmas não podem ser excluídas pois existem matrículas já registradas");
			}
			
			ResultSetMap rsmTurmas = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_turma WHERE cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery());
			
			if(rsmTurmas.size()==0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Não há turmas cadastradas nesse período letivo");
			}
			
			while(rsmTurmas.next()){
				if(TurmaServices.getHorariosByTurma(rsmTurmas.getInt("cd_turma")).size() > 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Existem horários cadastrados para essas turmas");
				}
			}
			rsmTurmas.beforeFirst();
			
			int ret = connect.prepareStatement("DELETE FROM acd_turma WHERE cd_periodo_letivo = " + cdPeriodoLetivo).executeUpdate();
			if(ret < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao excluir turmas");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Turmas excluidas com sucesso");
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
}	

}
