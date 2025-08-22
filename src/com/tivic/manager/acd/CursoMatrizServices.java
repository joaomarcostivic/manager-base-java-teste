package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class CursoMatrizServices {

	/* tipos de conceitos */
	public static final int TP_NOTA = 0;
	public static final int TP_CONCEITO = 1;

	public static final String[] tiposConceitos = {"Nota",
		"Conceito"};

	public static ResultSetMap getAllDisciplinas(int cdCurso, int cdMatriz, boolean returnHierarquia) {
		return getAllDisciplinas(cdCurso, cdMatriz, 0, returnHierarquia, null);
	}

	public static ResultSetMap getAllDisciplinas(int cdCurso, int cdMatriz, boolean returnHierarquia, Connection connection) {
		return getAllDisciplinas(cdCurso, cdMatriz, 0, returnHierarquia, null);
	}

	
	public static ResultSetMap getAllDisciplinas(int cdCurso, int cdMatriz, int cdTurma, boolean returnHierarquia) {
		return getAllDisciplinas(cdCurso, cdMatriz, returnHierarquia, null);
	}

	public static ResultSetMap getAllDisciplinas(int cdCurso, int cdMatriz, int cdTurma, boolean returnHierarquia, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.*, B.gn_disciplina, C.nm_produto_servico AS nm_disciplina, " +
					"       D.nm_produto_servico AS nm_curso_modulo " + (cdTurma > 0 ? ", E.cd_oferta " : "") +
					"FROM acd_curso_disciplina A " +
					"JOIN acd_disciplina      B ON (A.cd_disciplina = B.cd_disciplina) " +
					"JOIN grl_produto_servico C ON (A.cd_disciplina = C.cd_produto_servico) " +
					"JOIN grl_produto_servico D ON (A.cd_curso_modulo = D.cd_produto_servico) " +
					(cdTurma > 0 ?
					"LEFT OUTER JOIN acd_oferta E ON (A.cd_curso = E.cd_curso " + 
					"							  AND A.cd_curso_modulo = E.cd_curso_modulo "+ 
					"							  AND A.cd_disciplina = E.cd_disciplina " +
					"							  AND A.cd_matriz = E.cd_matriz "+
					"							  AND E.cd_turma = "+cdTurma+" )": "")+
					"WHERE A.cd_matriz = "+cdMatriz+
					"  AND A.cd_curso  = "+cdCurso+
					" ORDER BY A.cd_curso_modulo");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if (returnHierarquia) {
				while (rsm.next()) {
					if (rsm.getObject("NM_ITEM")!=null)
						break;
					if (rsm.getInt("cd_curso_periodo") > 0) {
						int position = rsm.getPosition();
						int cdProdutoServicoPeriodo = rsm.getInt("cd_curso_modulo");
						String nmCursoPeriodo = rsm.getString("nm_curso_modulo");
						rsm.getRegister().put("NM_ITEM", rsm.getString("nm_disciplina"));
						boolean isFound = rsm.locate("CD_ITEM", rsm.getInt("cd_curso_modulo"), false, false);
						HashMap<String, Object> register = isFound ? rsm.getRegister() : null;
						if (register==null) {
							register = new HashMap<String, Object>();
							register.put("CD_ITEM", cdProdutoServicoPeriodo);
							register.put("NM_ITEM", nmCursoPeriodo);
							register.put("subResultSetMap", new ResultSetMap());
							rsm.addRegister(register);
						}
						rsm.goTo(position);
						((ResultSetMap)register.get("subResultSetMap")).addRegister(rsm.getRegister());
						rsm.deleteRow();
						rsm.goTo(position - 1);
					}
					else {
						rsm.getRegister().put("NM_ITEM", rsm.getString("nm_disciplina"));
					}
				}
				rsm.beforeFirst();
			}
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

	@Deprecated
	public static int delete(int cdMatriz, int cdCurso, int cdCursoPeriodo, int cdDisciplina) {
		return delete(cdMatriz, cdCurso,cdCursoPeriodo, cdDisciplina, null);
	}

	@Deprecated
	public static int delete(int cdMatriz, int cdCurso, int cdCursoPeriodo, int cdDisciplina, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_curso_disciplina " +
																  "FROM acd_curso_disciplina " +
																  "WHERE cd_matriz        = "+cdMatriz+
																  "  AND cd_curso         = "+cdCurso);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (CursoDisciplinaDAO.delete(cdMatriz, cdCurso, rs.getInt("cd_curso_periodo"), rs.getInt("cd_disciplina"), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			if (CursoMatrizDAO.delete(cdMatriz, cdCurso, connection) <= 0) {
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
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getMatrizesAtivas(int cdCurso) {
		return getMatrizesAtivas(cdCurso, null);
	}

	public static ResultSetMap getMatrizesAtivas(int cdCurso, Connection connection) {
		boolean isConnectionNull = connection==null;
		String now = DateFormatUtils.format(new Date(), "yyyy-MM-dd hh:mm:ss");
		
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM acd_curso_matriz A " +
																  "WHERE A.cd_curso  = " + cdCurso + " AND (A.dt_vigencia_final IS NULL OR A.dt_vigencia_final >= '" + now + "') " + 
																  "ORDER BY A.dt_vigencia_inicial DESC");
			return new ResultSetMap(pstmt.executeQuery());
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
	
	public static CursoMatriz getMatrizVigente(int cdCurso) {
		return getMatrizVigente(cdCurso, null);
	}
	
	public static CursoMatriz getMatrizVigente(int cdCurso, Connection connection) {
		boolean isConnectionNull = connection==null;
		String now = DateFormatUtils.format(new Date(), "yyyy-MM-dd hh:mm:ss");
		
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM acd_curso_matriz A " +
																  "WHERE A.cd_curso  = "+ cdCurso +" AND A.dt_vigencia_inicial < '" + now + "'" +
																  "	   AND (A.dt_vigencia_final IS NULL OR A.dt_vigencia_final >= '" + now + "') " + 
																  "ORDER BY A.dt_vigencia_inicial DESC");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if (rsm.next()) 
				return get (rsm.getInt("cd_matriz"), rsm.getInt("cd_curso"));
			else 
				return null;
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
	
	public static Result save(CursoMatriz matriz){
		return save(matriz, null);
	}
	
	public static Result save(CursoMatriz cursoMatriz, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(cursoMatriz==null)
				return new Result(-1, "Erro ao salvar. Matriz é nulo");
			
			int retorno;
			if(CursoMatrizDAO.get(cursoMatriz.getCdMatriz(), cursoMatriz.getCdCurso(), connect) == null){
				retorno = CursoMatrizDAO.insert(cursoMatriz, connect);
				cursoMatriz.setCdMatriz(retorno);
			}
			else {
				retorno = CursoMatrizDAO.update(cursoMatriz, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CURSOMATRIZ", cursoMatriz);
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
	
	public static Result remove(int cdMatriz, int cdCurso){
		return remove(cdMatriz, cdCurso, false, null);
	}
	
	public static Result remove(int cdMatriz, int cdCurso, boolean cascade){
		return remove(cdMatriz, cdCurso, cascade, null);
	}
	
	public static Result remove(int cdMatriz, int cdCurso, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			if(cascade){
				// Excluindo curso disciplina relacionados a matriz
				retorno = CursoDisciplinaServices.removeByMatriz(cdMatriz, connect).getCode();
				
				// Excluindo disciplina avaliacao relacionados a matriz
				retorno = DisciplinaAvaliacaoServices.removeByMatriz(cdMatriz, connect).getCode();
				
				// Excluindo turmas relacionadas a matriz
				retorno = TurmaServices.removeByMatriz(cdMatriz, connect).getCode();
			}
			
			if(!cascade || retorno>0)
				retorno = CursoMatrizDAO.delete(cdMatriz, cdCurso, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta matriz está vinculada a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Matriz excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir matriz!");
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
			ResultSetMap rsm = getAllByCurso(cdCurso);
			while (rsm.next()) {
				if (rsm.getInt("cd_curso") == cdCurso) {
					retorno = remove(rsm.getInt("cd_matriz"), cdCurso, true, connect).getCode();
					if(retorno<=0){
						Conexao.rollback(connect);
						return new Result(-2, "Esta matriz está vinculada a outros registros e não pode ser excluído!");
					}
				}
			}
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Matriz excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir matriz!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static CursoMatriz get (int cdMatriz, int cdCurso) {
		return get(cdMatriz, cdCurso, null);
	}
	
	public static CursoMatriz get (int cdMatriz, int cdCurso, Connection connect) {
		return CursoMatrizDAO.get(cdMatriz, cdCurso, connect);
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
			
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_produto_servico, B.id_produto_servico, C.* "+
												"FROM acd_curso A "+
									            "JOIN grl_produto_servico B ON( A.cd_curso = B.cd_produto_servico ) " +
									            "LEFT OUTER JOIN acd_curso_matriz C ON ( A.cd_curso = C.cd_curso ) ");
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
	
	public static ResultSetMap getAllByCurso(int cdCurso) {
		return getAllByCurso(cdCurso, null);
	}

	public static ResultSetMap getAllByCurso(int cdCurso, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_produto_servico, B.id_produto_servico, C.*, D.* "+
												"FROM acd_curso A "+
									            "JOIN grl_produto_servico B ON( A.cd_curso = B.cd_produto_servico ) " +
									            "LEFT OUTER JOIN acd_curso_matriz C ON ( A.cd_curso = C.cd_curso ) "+
									            "LEFT OUTER JOIN acd_instituicao_curso D ON ( A.cd_curso = D.cd_curso ) "+ 
									            " WHERE A.cd_curso = " + cdCurso);
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


	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.nm_produto_servico, B.id_produto_servico, C.*, E.*, H.nm_etapa, H.sg_tipo_etapa, H.lg_regular, H.lg_especial, H.lg_eja "+
							"FROM acd_curso A "+
				            "JOIN grl_produto_servico B ON( A.cd_curso = B.cd_produto_servico ) " +
				            "LEFT OUTER JOIN acd_curso_matriz C ON ( A.cd_curso = C.cd_curso) " +
				            "LEFT OUTER JOIN acd_curso_modulo E ON ( A.cd_curso = E.cd_curso) " + 
				            " LEFT OUTER JOIN acd_curso_etapa G ON (G.cd_curso = A.cd_curso) " +
							" LEFT OUTER JOIN acd_tipo_etapa H ON (H.cd_etapa = G.cd_etapa) ", 
				            " ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
