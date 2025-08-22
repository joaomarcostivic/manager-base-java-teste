package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class QuadroVagasOcorrenciaServices {

	public static Result save(QuadroVagasOcorrencia quadroVagasOcorrencia){
		return save(quadroVagasOcorrencia, null);
	}

	public static Result save(QuadroVagasOcorrencia quadroVagasOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(quadroVagasOcorrencia==null)
				return new Result(-1, "Erro ao salvar. QuadroVagasOcorrencia é nulo");

			int retorno;
			if(QuadroVagasOcorrenciaDAO.get(quadroVagasOcorrencia.getCdQuadroVagas(), quadroVagasOcorrencia.getCdInstituicao(), quadroVagasOcorrencia.getCdOcorrencia(), connect)==null){
				retorno = QuadroVagasOcorrenciaDAO.insert(quadroVagasOcorrencia, connect);
				quadroVagasOcorrencia.setCdQuadroVagas(retorno);
			}
			else {
				retorno = QuadroVagasOcorrenciaDAO.update(quadroVagasOcorrencia, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "QUADROVAGASOCORRENCIA", quadroVagasOcorrencia);
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
	public static Result remove(int cdQuadroVagas, int cdInstituicao, int cdOcorrencia){
		return remove(cdQuadroVagas, cdInstituicao, cdOcorrencia, false, null);
	}
	public static Result remove(int cdQuadroVagas, int cdInstituicao, int cdOcorrencia, boolean cascade){
		return remove(cdQuadroVagas, cdInstituicao, cdOcorrencia, cascade, null);
	}
	public static Result remove(int cdQuadroVagas, int cdInstituicao, int cdOcorrencia, boolean cascade, Connection connect){
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
			retorno = QuadroVagasOcorrenciaDAO.delete(cdQuadroVagas, cdInstituicao, cdOcorrencia, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_quadro_vagas_ocorrencia");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasOcorrenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasOcorrenciaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllOcorrencias() {
		return getAllOcorrencias(-1, -1, null);
	}
	
	public static ResultSetMap getAllOcorrencias(int cdQuadroVagas) {
		return getAllOcorrencias(cdQuadroVagas, -1, null);
	}
	
	public static ResultSetMap getAllOcorrencias(int cdQuadroVagas, int cdInstituicao) {
		return getAllOcorrencias(cdQuadroVagas, cdInstituicao, null);
	}

	public static ResultSetMap getAllOcorrencias(int cdQuadroVagas, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			pstmt = connect.prepareStatement("SELECT A.*, B.*, C.*, D.nm_pessoa AS nm_solicitante, E.nm_pessoa AS nm_instituicao FROM acd_quadro_vagas_ocorrencia A "
											+ "     JOIN acd_ocorrencia_quadro_vagas B ON (A.cd_ocorrencia = B.cd_ocorrencia) " 
											+ "     JOIN grl_ocorrencia C ON (A.cd_ocorrencia = C.cd_ocorrencia)"	
											+ "		LEFT OUTER JOIN grl_pessoa D ON (C.cd_pessoa = D.cd_pessoa) "
											+ "		JOIN grl_pessoa E ON (A.cd_instituicao = E.cd_pessoa) "
											+ "  WHERE 1=1 " + (cdQuadroVagas != -1 ? " AND A.cd_quadro_vagas = " + cdQuadroVagas : "")
											+ (cdInstituicao != -1 ? " AND A.cd_instituicao = " + cdInstituicao : "")
											+ " ORDER BY dt_ocorrencia DESC");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasOcorrenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasOcorrenciaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllOcorrenciasByUsuario(int cdUsuario) {
		return getAllOcorrenciasByUsuario(cdUsuario, null);
	}
	
	public static ResultSetMap getAllOcorrenciasByUsuario(int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			pstmt = connect.prepareStatement("SELECT A.*, AA.*, B.*, C.*, D.nm_pessoa AS nm_solicitante, E.nm_pessoa AS nm_instituicao "
											+ " FROM acd_quadro_vagas_ocorrencia A "
											+ "     JOIN acd_quadro_vagas AA ON (A.cd_quadro_vagas = AA.cd_quadro_vagas AND A.cd_instituicao = AA.cd_instituicao) " 
											+ "     JOIN acd_ocorrencia_quadro_vagas B ON (A.cd_ocorrencia = B.cd_ocorrencia) " 
											+ "     JOIN grl_ocorrencia C ON (A.cd_ocorrencia = C.cd_ocorrencia)"	
											+ "		JOIN grl_pessoa D ON (C.cd_pessoa = D.cd_pessoa) "
											+ "		JOIN grl_pessoa E ON (A.cd_instituicao = E.cd_pessoa) "
											+ "  WHERE C.cd_usuario = " + cdUsuario
											+ " ORDER BY dt_ocorrencia DESC");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasOcorrenciaServices.getAllOcorrenciasByUsuario: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasOcorrenciaServices.getAllOcorrenciasByUsuario: " + e);
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
		return Search.find("SELECT * FROM acd_quadro_vagas_ocorrencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_quadro_vagas_ocorrencia WHERE cd_quadro_vagas = " + cdQuadroVagas + " AND cd_instituicao = " + cdInstituicao);
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
	
}
