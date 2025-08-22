package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class CursoDisciplinaServices {

	public static final int TP_CLASSIFICACAO_BASE_NACIONAL    = 0;
	public static final int TP_CLASSIFICACAO_DIVERSIFICADA    = 1;
	public static final int TP_CLASSIFICACAO_EXTRA_CURRICULAR = 2;
	
	public static final String[] tiposClassificacao = {"Base Nacional", 
													  "Diversificada", 
													  "Extra Curricular"};
	
	public static Result save(CursoDisciplina cursoDisciplina){
		return save(cursoDisciplina, null);
	}

	public static Result save(CursoDisciplina cursoDisciplina, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cursoDisciplina==null)
				return new Result(-1, "Erro ao salvar. CursoDisciplina é nulo");

			int retorno;
			if(CursoDisciplinaDAO.get(cursoDisciplina.getCdCurso(), cursoDisciplina.getCdCursoModulo(), cursoDisciplina.getCdDisciplina(), cursoDisciplina.getCdMatriz()) == null){
				retorno = CursoDisciplinaDAO.insert(cursoDisciplina, connect);
			}
			else {
				retorno = CursoDisciplinaDAO.update(cursoDisciplina, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CURSODISCIPLINA", cursoDisciplina);
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
	
	public static Result remove(int cdCurso, int cdCursoModulo, int cdDisciplina, int cdMatriz){
		return remove(cdCurso, cdCursoModulo, cdDisciplina, cdMatriz, false, null);
	}
	
	public static Result remove(int cdCurso, int cdCursoModulo, int cdDisciplina, int cdMatriz, boolean cascade){
		return remove(cdCurso, cdCursoModulo, cdDisciplina, cdMatriz, cascade, null);
	}
	
	public static Result remove(int cdCurso, int cdCursoModulo, int cdDisciplina, int cdMatriz, boolean cascade, Connection connect){
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
				retorno = CursoDisciplinaDAO.delete(cdCurso, cdCursoModulo, cdDisciplina, cdMatriz, connect);
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
				ResultSetMap rsm = getAll();
				while (rsm.next()) {
					if (rsm.getInt("cd_matriz") == cdMatriz) {
						retorno = remove(rsm.getInt("cd_curso"), rsm.getInt("cd_curso_modulo"), rsm.getInt("cd_disciplina"), cdMatriz, true, connect).getCode();
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
	
	public static ResultSetMap getAll() {
			return getAll(null);
		}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_disciplina");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoDisciplinaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllCursoDisciplinas(int cdCurso, int cdMatriz) {
		return getAllCursoDisciplinas(cdCurso, cdMatriz, null);
	}
	
	public static ResultSetMap getAllCursoDisciplinas(int cdCurso, int cdMatriz, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_produto_servico AS nm_disciplina, D.cd_area_conhecimento, D.nm_area_conhecimento FROM acd_curso_disciplina A " +
																  "LEFT OUTER JOIN grl_produto_servico		B ON (A.cd_disciplina = B.cd_produto_servico) " +
																  "LEFT OUTER JOIN acd_disciplina			C ON (A.cd_disciplina = C.cd_disciplina) " +
																  "LEFT OUTER JOIN acd_area_conhecimento	D ON (D.cd_area_conhecimento = C.cd_area_conhecimento) " +
																  "WHERE A.cd_curso = " +cdCurso+ "AND A.cd_matriz = " +cdMatriz+
																  "ORDER BY nr_ordem, nm_disciplina DESC");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getCursoDisciplinasByClassificacaoArea(int cdCurso, int tpClassificacao, int cdAreaConhecimento, int cdMatriz, int cdInstituicao) {
		return getCursoDisciplinasByClassificacaoArea(cdCurso, tpClassificacao, cdAreaConhecimento, cdMatriz, cdInstituicao, null);
	}
	
	public static ResultSetMap getCursoDisciplinasByClassificacaoArea(int cdCurso, int tpClassificacao, int cdAreaConhecimento, int cdMatriz, int cdInstituicao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_produto_servico AS nm_disciplina, D.cd_area_conhecimento, D.nm_area_conhecimento "+ 
																  "FROM acd_curso_disciplina A " +
																  "LEFT OUTER JOIN grl_produto_servico		B ON (A.cd_disciplina = B.cd_produto_servico) " +
																  "LEFT OUTER JOIN acd_disciplina			C ON (A.cd_disciplina = C.cd_disciplina) " +
																  "LEFT OUTER JOIN acd_area_conhecimento	D ON (D.cd_area_conhecimento = C.cd_area_conhecimento) " +
																  "WHERE A.cd_curso = ? AND A.cd_matriz = ? AND A.tp_classificacao = ? "+
																  "		 AND C.cd_area_conhecimento = ? "+ 
																  (cdInstituicao != ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0) ? " AND A.cd_instituicao = ? " : "")+ 
																  "ORDER BY nr_ordem, nm_disciplina DESC");
			
			PreparedStatement pstmtAreasInferiores = connection.prepareStatement("SELECT A.*" +
																				   "FROM acd_area_conhecimento A " +
																				   "WHERE A.cd_area_conhecimento_superior = ? ");
			
			PreparedStatement pstmtInstituicaoSuperior = connection.prepareStatement("SELECT A.cd_pessoa_superior " +
																			     "FROM grl_pessoa A " +
																			     "WHERE A.cd_pessoa = ? "+ 
																			     "  AND A.cd_pessoa_superior IS NOT NULL");

			pstmtAreasInferiores.setInt(1, cdAreaConhecimento);
			
			ResultSetMap rsmAreasInferiores = new ResultSetMap(pstmtAreasInferiores.executeQuery());
			
			if(!rsmAreasInferiores.next()){
				
				pstmt.setInt(1, cdCurso);
				pstmt.setInt(2, cdMatriz);
				pstmt.setInt(3, tpClassificacao);
				pstmt.setInt(4, cdAreaConhecimento);
				if(cdInstituicao != ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0))
					pstmt.setInt(5, cdInstituicao);
				
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				
				if(tpClassificacao == TP_CLASSIFICACAO_BASE_NACIONAL){
					pstmtInstituicaoSuperior.setInt(1, cdInstituicao);
					ResultSetMap rsmInstituicaoSuperior = new ResultSetMap(pstmtInstituicaoSuperior.executeQuery());
					if(rsmInstituicaoSuperior.next()){
						
						pstmt.setInt(1, cdCurso);
						pstmt.setInt(2, cdMatriz);
						pstmt.setInt(3, tpClassificacao);
						pstmt.setInt(4, cdAreaConhecimento);
						pstmt.setInt(5, rsmInstituicaoSuperior.getInt("cd_instituicao"));
						ResultSetMap rsmDisciplinasInstituicaoSuperior = new ResultSetMap(pstmt.executeQuery());
						while(rsmDisciplinasInstituicaoSuperior.next()){
							rsm.addRegister(rsmDisciplinasInstituicaoSuperior.getRegister());
						}
					}
				}
				return rsm;
			}
			else{
				
				ResultSetMap rsmAreasInferioresAux = new ResultSetMap();
				
				do{
					
					ResultSetMap rsmDisciplinas = CursoDisciplinaServices.getCursoDisciplinasByClassificacaoArea(cdCurso, tpClassificacao, rsmAreasInferiores.getInt("cd_area_conhecimento"), cdMatriz, cdInstituicao, connection);
					
					
					if(rsmDisciplinas.getLines().size()>0){
						rsmAreasInferiores.setValueToField("HIERARQUIA_MATRIZ", rsmDisciplinas.getLines());
						rsmAreasInferioresAux.addRegister(rsmAreasInferiores.getRegister());
					}
					
				}while(rsmAreasInferiores.next());
				
				return rsmAreasInferioresAux;
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	@Deprecated
	public static int insert(CursoDisciplina disciplina) {
		return insert(disciplina, null);
	}

	@Deprecated
	public static int insert(CursoDisciplina disciplina, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			if (CursoDisciplinaDAO.insert(disciplina, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}
			if (isConnectionNull)
				connection.commit();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static int update(CursoDisciplina disciplina) {
		return update(disciplina, null);
	}

	@Deprecated
	public static int update(CursoDisciplina disciplina, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			if (CursoDisciplinaDAO.update(disciplina, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}
			if (isConnectionNull)
				connection.commit();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		int cdTurma = 0;
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("cd_turma")) {
				cdTurma = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				break;
			}
		}
		return Search.find( "SELECT A.*, B.nm_matriz, C.nm_produto_servico AS nm_curso, D.nm_produto_servico AS nm_disciplina " +
							"FROM acd_curso_disciplina A " +
							"JOIN acd_curso_matriz    B ON (A.cd_matriz = B.cd_matriz " +
							"						    AND A.cd_curso = B.cd_curso) " +
							"JOIN grl_produto_servico C ON (A.cd_curso = C.cd_produto_servico) " +
							"JOIN grl_produto_servico D ON (A.cd_disciplina = D.cd_produto_servico) " +
							(cdTurma>0 ? "JOIN acd_turma E ON (B.cd_matriz = E.cd_matriz " +
							             "				   AND B.cd_curso  = E.cd_curso " +
							             "				   AND E.cd_turma  = " + cdTurma + ") " : ""), criterios, true,
							connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
