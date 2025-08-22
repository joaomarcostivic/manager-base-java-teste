package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;

public class DisciplinaAvaliacaoServices {

	public static Result save(DisciplinaAvaliacao disciplinaAvaliacao){
		return save(disciplinaAvaliacao, null);
	}

	public static Result save(DisciplinaAvaliacao disciplinaAvaliacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(disciplinaAvaliacao==null)
				return new Result(-1, "Erro ao salvar. DisciplinaAvaliacao é nulo");

			int retorno;
			if(get(disciplinaAvaliacao) == null){
				retorno = DisciplinaAvaliacaoDAO.insert(disciplinaAvaliacao, connect);
				disciplinaAvaliacao.setCdDisciplinaAvaliacao(retorno);
			}
			else {
				retorno = DisciplinaAvaliacaoDAO.update(disciplinaAvaliacao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "DISCIPLINAAVALIACAO", disciplinaAvaliacao);
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
	
	public static Result gerarAvaliacoesPadrao(int cdCurso) {
		return gerarAvaliacoesPadrao(cdCurso, null);
	}
	
	public static Result gerarAvaliacoesPadrao(int cdCurso, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno = 0;
			
			ResultSetMap rsm, rsmUnidades;
			
			CursoMatrizServices.getMatrizVigente(cdCurso, connect);
			
			if (CursoMatrizServices.getMatrizVigente(cdCurso, connect) == null) {
				Conexao.rollback(connect);
				return new Result(-2, "Erro não existe matriz vigente para o curso!");
			}
				
			rsm				= CursoMatrizServices.getAllDisciplinas(cdCurso, CursoMatrizServices.getMatrizVigente(cdCurso, connect).getCdMatriz(), false, connect);
			rsmUnidades 	= CursoUnidadeServices.getAllByCurso(cdCurso, connect);
			
			while (rsm.next()) {
				while (rsmUnidades.next()) {
					retorno = DisciplinaAvaliacaoServices.save(new DisciplinaAvaliacao(0, cdCurso, rsm.getInt("cd_curso_modulo"), rsm.getInt("cd_disciplina"), 
																					   rsm.getInt("cd_matriz"), rsmUnidades.getInt("cd_unidade"), 
																					   ParametroServices.getValorOfParametroAsInteger("CD_TIPO_AVALIACAO_FINAL", 0, 0, connect), 
																					   rsmUnidades.getString("nm_unidade") + " Conceito Final", "", 0, "", 0), connect).getCode();						
					if(retorno<=0)	{
						Conexao.rollback(connect);
						return new Result(-3, "Erro ao criar avaliações padrão para o curso!");
					}
				}
				rsmUnidades.beforeFirst();
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar avaliações padrão.":"Avaliações padrão salvas com sucesso...");
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
	
	public static Result remove(int cdDisciplinaAvaliacao){
		return remove(cdDisciplinaAvaliacao, false, null);
	}
	
	public static Result remove(int cdDisciplinaAvaliacao, boolean cascade){
		return remove(cdDisciplinaAvaliacao, cascade, null);
	}
	
	public static Result remove(int cdDisciplinaAvaliacao, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				// Excluindo oferta avaliacao relacionadas a disciplina
				retorno = OfertaAvaliacaoServices.removeByDisciplinaAvaliacao(cdDisciplinaAvaliacao, connect).getCode();
			}
			if(!cascade || retorno>0)
			retorno = DisciplinaAvaliacaoDAO.delete(cdDisciplinaAvaliacao, connect);
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

	public static Result removeByCurso(int cdCurso) {
		return removeByCurso(cdCurso, null);
	}
	
	public static Result removeByCurso(int cdCurso, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			ResultSetMap rsm = getAll(connect);
			while (rsm.next()) {
				if (rsm.getInt("cd_curso") == cdCurso) {
					retorno = remove(rsm.getInt("cd_disciplina_avaliacao"), true, connect).getCode();
					if(retorno<=0){
						Conexao.rollback(connect);
						return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
					}
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
	
	public static Result removeByUnidade(int cdUnidade) {
		return removeByUnidade(cdUnidade, null);
	}
	
	public static Result removeByUnidade(int cdUnidade, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			ResultSetMap rsm = getAll(connect);
			while (rsm.next()) {
				if (rsm.getInt("cd_unidade") == cdUnidade) {
					retorno = remove(rsm.getInt("cd_disciplina_avaliacao"), true, connect).getCode();
					if(retorno<=0){
						Conexao.rollback(connect);
						return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
					}
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
	
	public static Result removeByMatriz(int cdMatriz) {
		return removeByMatriz(cdMatriz, null);
	}
	
	public static Result removeByMatriz(int cdMatriz, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			ResultSetMap rsm = getAll(connect);
			while (rsm.next()) {
				if (rsm.getInt("cd_matriz") == cdMatriz) {
					retorno = remove(rsm.getInt("cd_disciplina_avaliacao"), true, connect).getCode();
					if(retorno<=0){
						Conexao.rollback(connect);
						return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
					}
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
	
	public static DisciplinaAvaliacao get(DisciplinaAvaliacao disciplinaAvaliacao) {
		return get(disciplinaAvaliacao, null);
	}
	
	public static DisciplinaAvaliacao get(DisciplinaAvaliacao disciplinaAvaliacao, Connection connect) {
		return DisciplinaAvaliacaoDAO.get(disciplinaAvaliacao.getCdDisciplinaAvaliacao(), disciplinaAvaliacao.getCdCurso(), 
				  disciplinaAvaliacao.getCdCursoModulo(), disciplinaAvaliacao.getCdDisciplina(), disciplinaAvaliacao.getCdMatriz(), connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_disciplina_avaliacao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByDisciplina( int cdDisciplina ) {
		return getAllByDisciplina(cdDisciplina, null);
	}

	public static ResultSetMap getAllByDisciplina( int cdDisciplina, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_disciplina_avaliacao WHERE cd_disciplina = "+String.valueOf( cdDisciplina ) );
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoServices.getAllByDisciplina: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoServices.getAllByDisciplina: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByCurso( int cdCurso ) {
		return getAllByCurso(cdCurso, null);
	}
	
	public static ResultSetMap getAllByCurso( int cdCurso, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, C.nm_produto_servico AS nm_disciplina, D.nm_unidade FROM acd_disciplina_avaliacao A " +
 											 "JOIN grl_produto_servico		C ON (A.cd_disciplina = C.cd_produto_servico) " +
											 "LEFT OUTER JOIN acd_curso_unidade	D ON (A.cd_curso = D.cd_curso AND A.cd_unidade = D.cd_unidade) " +
											 "WHERE A.cd_curso = " + cdCurso +
											 "ORDER BY C.nm_produto_servico, D.nm_unidade");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoServices.getAllByCurso: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoServices.getAllByCurso: " + e);
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
		return Search.find("SELECT * FROM acd_disciplina_avaliacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}