package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class CursoEtapaServices {

	public static Result save(CursoEtapa cursoEtapa){
		return save(cursoEtapa, null);
	}

	public static Result save(CursoEtapa cursoEtapa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cursoEtapa==null)
				return new Result(-1, "Erro ao salvar. CursoEtapa é nulo");

			int retorno;
			if(cursoEtapa.getCdCursoEtapa()==0){
				retorno = CursoEtapaDAO.insert(cursoEtapa, connect);
				cursoEtapa.setCdCursoEtapa(retorno);
			}
			else {
				retorno = CursoEtapaDAO.update(cursoEtapa, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CURSOETAPA", cursoEtapa);
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
	public static Result remove(int cdCursoEtapa){
		return remove(cdCursoEtapa, false, null);
	}
	public static Result remove(int cdCursoEtapa, boolean cascade){
		return remove(cdCursoEtapa, cascade, null);
	}
	public static Result remove(int cdCursoEtapa, boolean cascade, Connection connect){
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
			retorno = CursoEtapaDAO.delete(cdCursoEtapa, connect);
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
	
	/**
	 * Busca todos os cursos de uma determinada etapa
	 * 
	 * @param cdEtapa código da etapa
	 * @return ResultSetMap Lista com etapas
	 * 
	 * @author Maurício
	 * @since 16/03/2015
	 */
	public static ResultSetMap getAllByEtapa(int cdEtapa) {
		return getAllByEtapa(cdEtapa, null);
	}

	public static ResultSetMap getAllByEtapa(int cdEtapa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_produto_servico AS nm_curso, "
					 + " D.nm_produto_servico AS nm_curso_etapa_posterior,"
					 + " E.nm_etapa"
				     + " FROM acd_curso_etapa A"
					 + " LEFT OUTER JOIN grl_produto_servico B ON (A.cd_curso = B.cd_produto_servico)"
					 + " LEFT OUTER JOIN acd_curso_etapa C ON (A.cd_curso_etapa_posterior = C.cd_curso_etapa)"
					 + " LEFT OUTER JOIN grl_produto_servico D ON (C.cd_curso = D.cd_produto_servico)"
					 + " LEFT OUTER JOIN acd_tipo_etapa E ON (A.cd_etapa = E.cd_etapa)"
					 + " WHERE A.cd_etapa = "+cdEtapa
					 + " ORDER BY nm_curso");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoEtapaServices.getAllByEtapa: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoEtapaServices.getAllByEtapa: " + e);
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
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_produto_servico AS nm_curso, "
					 + " D.nm_produto_servico AS nm_curso_etapa_posterior,"
					 + " E.nm_etapa"
				     + " FROM acd_curso_etapa A"
					 + " LEFT OUTER JOIN grl_produto_servico B ON (A.cd_curso = B.cd_produto_servico)"
					 + " LEFT OUTER JOIN acd_curso_etapa C ON (A.cd_curso_etapa_posterior = C.cd_curso_etapa)"
					 + " LEFT OUTER JOIN grl_produto_servico D ON (C.cd_curso = D.cd_produto_servico)"
					 + " LEFT OUTER JOIN acd_tipo_etapa E ON (A.cd_etapa = E.cd_etapa)"
					 + " ORDER BY A.nm_curso");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoEtapaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoEtapaServices.getAll: " + e);
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
		
		String limit = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if(criterios.get(i).getColumn().equalsIgnoreCase("limit")) {
				limit += " LIMIT "+ criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
		}
		ResultSetMap  _rsm = Search.find("SELECT A.*, B.nm_produto_servico AS nm_curso, "
						 + " D.nm_produto_servico AS nm_curso_etapa_posterior,"
						 + " E.nm_etapa, E.sg_tipo_etapa, E.lg_regular, E.lg_especial, E.lg_eja"
					     + " FROM acd_curso_etapa A"
						 + " LEFT OUTER JOIN grl_produto_servico B ON (A.cd_curso = B.cd_produto_servico)"
						 + " LEFT OUTER JOIN acd_curso_etapa C ON (A.cd_curso_etapa_posterior = C.cd_curso_etapa)"
						 + " LEFT OUTER JOIN grl_produto_servico D ON (C.cd_curso = D.cd_produto_servico)"
						 + " LEFT OUTER JOIN acd_tipo_etapa E ON (A.cd_etapa = E.cd_etapa)" +limit, 
				criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		
		  return _rsm;	
	}

	public static CursoEtapa getById(String idCursoEtapa) {
		return getById(idCursoEtapa, null);
	}

	public static CursoEtapa getById(String idCursoEtapa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_etapa WHERE id_curso_etapa=?");
			pstmt.setString(1, idCursoEtapa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CursoEtapa(rs.getInt("cd_curso_etapa"),
						rs.getInt("cd_curso"),
						rs.getInt("cd_curso_etapa_posterior"),
						rs.getString("id_curso_etapa"),
						rs.getInt("cd_etapa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoEtapaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoEtapaDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Curso getProximoCurso(int cdCursoOriginal, int cdInstituicao) {
		return getProximoCurso(cdCursoOriginal, cdInstituicao, null);
	}

	public static Curso getProximoCurso(int cdCursoOriginal, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSetMap rsm;
		try {
			
			int cdPeriodoLetivoAtual = 0;
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso A"
											+ " JOIN acd_curso_etapa B ON (A.cd_curso = B.cd_curso)"
											+ " WHERE A.cd_curso = ?");
			pstmt.setInt(1, cdCursoOriginal);
			rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()){
				rsm = new ResultSetMap(connect.prepareStatement("SELECT A.cd_curso, B.id_produto_servico FROM acd_curso_etapa A"
						+ " JOIN grl_produto_servico B ON (A.cd_curso = B.cd_produto_servico)"
						+ " WHERE cd_curso_etapa = " + rsm.getInt("cd_curso_etapa_posterior")).executeQuery());
				if(rsm.next()){
					if(InstituicaoCursoDAO.get(cdInstituicao, rsm.getInt("cd_curso"), cdPeriodoLetivoAtual, connect) != null)
						return CursoDAO.get(rsm.getInt("cd_curso"), connect);
					else{
						ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("A.cd_curso", rsm.getString("cd_curso"), ItemComparator.DIFFERENT, Types.INTEGER));
						criterios.add(new ItemComparator("B.id_produto_servico", rsm.getString("id_produto_servico"), ItemComparator.EQUAL, Types.VARCHAR));
						ResultSetMap rsmCursos = CursoServices.find(criterios, connect);
						while(rsmCursos.next()){
							if(InstituicaoCursoDAO.get(cdInstituicao, rsmCursos.getInt("cd_curso"), cdPeriodoLetivoAtual, connect) != null)
								return CursoDAO.get(rsmCursos.getInt("cd_curso"), connect);
						}
						rsmCursos.beforeFirst();
						
						return CursoDAO.get(rsm.getInt("cd_curso"), connect);
					}
				}
				else{
					return null;
				}
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoEtapaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoEtapaDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static boolean hasAnteriorCurso(int cdCursoOriginal, int cdInstituicao) {
		return hasAnteriorCurso(cdCursoOriginal, cdInstituicao, null);
	}

	public static boolean hasAnteriorCurso(int cdCursoOriginal, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdPeriodoLetivoAtual = 0;
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			pstmt = connect.prepareStatement("SELECT C.cd_curso AS cd_curso_anterior, A.cd_curso FROM acd_curso A"
											+ " JOIN acd_curso_etapa B ON (A.cd_curso = B.cd_curso)"
											+ " JOIN acd_curso_etapa C ON (B.cd_curso_etapa = C.cd_curso_etapa_posterior)"
											+ " WHERE A.cd_curso = ?");
			pstmt.setInt(1, cdCursoOriginal);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()){
				if(InstituicaoCursoDAO.get(cdInstituicao, rsm.getInt("cd_curso_anterior"), cdPeriodoLetivoAtual, connect) != null)
					return true;
				
				return false;
			}
			else{
				
				Curso curso = CursoDAO.get(cdCursoOriginal, connect);
				
				pstmt = connect.prepareStatement("SELECT C.cd_curso AS cd_curso_anterior FROM acd_curso A"
						+ " JOIN acd_curso_etapa B ON (A.cd_curso = B.cd_curso)"
						+ " JOIN acd_curso_etapa C ON (B.cd_curso_etapa = C.cd_curso_etapa_posterior)"
						+ " JOIN grl_produto_servico D ON (A.cd_curso = D.cd_produto_servico)"
						+ " WHERE A.cd_curso <> ? AND D.id_produto_servico = ?");
				pstmt.setInt(1, cdCursoOriginal);
				pstmt.setString(2, curso.getIdProdutoServico());
				ResultSetMap rsmCursos = new ResultSetMap(pstmt.executeQuery());
				while(rsmCursos.next()){
					if(InstituicaoCursoDAO.get(cdInstituicao, rsmCursos.getInt("cd_curso_anterior"), cdPeriodoLetivoAtual, connect) != null)
						return true;
				}
				
				return false;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoEtapaDAO.get: " + sqlExpt);
			return false;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoEtapaDAO.get: " + e);
			return false;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
