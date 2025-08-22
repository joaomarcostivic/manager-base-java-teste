package com.tivic.manager.acd;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class AulaOcorrenciaServices {

	public static Result save(AulaOcorrencia aulaOcorrencia){
		return save(aulaOcorrencia, null);
	}

	public static Result save(AulaOcorrencia aulaOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(aulaOcorrencia==null)
				return new Result(-1, "Erro ao salvar. AulaOcorrencia é nulo");

			int retorno;
			if(AulaOcorrenciaDAO.get(aulaOcorrencia.getCdAula(), aulaOcorrencia.getCdOcorrencia(), connect)==null) {
				retorno = AulaOcorrenciaDAO.insert(aulaOcorrencia, connect);
				aulaOcorrencia.setCdAula(retorno);
			}
			else {
				retorno = AulaOcorrenciaDAO.update(aulaOcorrencia, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AULAOCORRENCIA", aulaOcorrencia);
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
	public static Result remove(int cdAula, int cdOcorrencia){
		return remove(cdAula, cdOcorrencia, false, null);
	}
	public static Result remove(int cdAula, int cdOcorrencia, boolean cascade){
		return remove(cdAula, cdOcorrencia, cascade, null);
	}
	public static Result remove(int cdAula, int cdOcorrencia, boolean cascade, Connection connect){
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
			retorno = AulaOcorrenciaDAO.delete(cdAula, cdOcorrencia, connect);
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
	 * Retorna as ocorrencias por aula
	 * 
	 * @param cdAula (codigo da aula)
	 * @return ResultSetMap
	 */	
	public static ResultSetMap getAllByAula(int cdAula) {
		return getAllByAula(cdAula, null);
	}

	public static ResultSetMap getAllByAula(int cdAula, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_aula_ocorrencia " + 
												"WHERE cd_aula = " + cdAula);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaOcorrenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaOcorrenciaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Retorna todas as ocorrencias de uma oferta
	 * - Usada no diário de classe
	 * @param cdOferta (codigo da oferta)
	 * @return ResultSetMap
	 */	
	public static ResultSetMap getAllByOferta(int cdOferta) {
		return getAllByOferta(cdOferta, null);
	}

	public static ResultSetMap getAllByOferta(int cdOferta, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.* "
										+ " FROM acd_aula_ocorrencia A"
										+ " LEFT OUTER JOIN acd_aula B ON (A.cd_aula = B.cd_aula)"
										+ " LEFT OUTER JOIN acd_oferta C ON (B.cd_oferta = C.cd_oferta)"
										+ " WHERE C.cd_oferta = " + cdOferta);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaOcorrenciaServices.getAllByOferta: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaOcorrenciaServices.getAllByOferta: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_aula_ocorrencia");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaOcorrenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaOcorrenciaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_aula_ocorrencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}