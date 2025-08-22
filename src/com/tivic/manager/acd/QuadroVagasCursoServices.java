package com.tivic.manager.acd;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class QuadroVagasCursoServices {

	public static Result save(QuadroVagasCurso quadroVagasCurso){
		return save(quadroVagasCurso, null);
	}

	public static Result save(QuadroVagasCurso quadroVagasCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(quadroVagasCurso==null)
				return new Result(-1, "Erro ao salvar. QuadroVagasCurso é nulo");

			int retorno;
			if(QuadroVagasCursoDAO.get(quadroVagasCurso.getCdQuadroVagas(), quadroVagasCurso.getCdInstituicao(), quadroVagasCurso.getCdCurso(), quadroVagasCurso.getTpTurno())==null){
				retorno = QuadroVagasCursoDAO.insert(quadroVagasCurso, connect);
				quadroVagasCurso.setCdQuadroVagas(retorno);
			}
			else {
				retorno = QuadroVagasCursoDAO.update(quadroVagasCurso, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "QUADROVAGASCURSO", quadroVagasCurso);
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
	public static Result remove(int cdQuadroVagas, int cdInstituicao, int cdCurso, int tpTurno){
		return remove(cdQuadroVagas, cdInstituicao, cdCurso, tpTurno, false, null);
	}
	public static Result remove(int cdQuadroVagas, int cdInstituicao, int cdCurso, int tpTurno, boolean cascade){
		return remove(cdQuadroVagas, cdInstituicao, cdCurso, tpTurno, cascade, null);
	}
	public static Result remove(int cdQuadroVagas, int cdInstituicao, int cdCurso, int tpTurno, boolean cascade, Connection connect){
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
			retorno = QuadroVagasCursoDAO.delete(cdQuadroVagas, cdInstituicao, cdCurso, tpTurno, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_quadro_vagas_curso");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasCursoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasCursoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByQuadroVagas(int cdQuadroVagas, int cdInstituicao) {
		return getAllByQuadroVagas(cdQuadroVagas, cdInstituicao, null);
	}

	public static ResultSetMap getAllByQuadroVagas(int cdQuadroVagas, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_quadro_vagas_curso WHERE cd_quadro_vagas = " + cdQuadroVagas + " AND cd_instituicao = " + cdInstituicao);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasCursoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasCursoServices.getAll: " + e);
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
		return Search.find("SELECT A.*, B.*, C.nm_produto_servico, (H.nm_etapa || ' - ' || C.nm_produto_servico) AS nm_curso FROM acd_quadro_vagas_curso A " +
						   "  JOIN acd_curso B ON (A.cd_curso = B.cd_curso) " +
						   "  JOIN grl_produto_servico C ON (A.cd_curso = C.cd_produto_servico) " + 
						   " LEFT OUTER JOIN acd_curso_etapa G ON (G.cd_curso = A.cd_curso) " +
						   " LEFT OUTER JOIN acd_tipo_etapa H ON (H.cd_etapa = G.cd_etapa) "
				, " ORDER BY H.nm_etapa,  C.nm_produto_servico", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
