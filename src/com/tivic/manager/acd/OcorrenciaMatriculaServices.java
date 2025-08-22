package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class OcorrenciaMatriculaServices {

	public static Result save(OcorrenciaMatricula ocorrenciaMatricula){
		return save(ocorrenciaMatricula, null);
	}

	public static Result save(OcorrenciaMatricula ocorrenciaMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ocorrenciaMatricula==null)
				return new Result(-1, "Erro ao salvar. OcorrenciaMatricula é nulo");

			int retorno;
			if(ocorrenciaMatricula.getCdOcorrencia()==0){
				retorno = OcorrenciaMatriculaDAO.insert(ocorrenciaMatricula, connect);
				ocorrenciaMatricula.setCdOcorrencia(retorno);
			}
			else {
				retorno = OcorrenciaMatriculaDAO.update(ocorrenciaMatricula, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "OCORRENCIAMATRICULA", ocorrenciaMatricula);
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
	public static Result remove(int cdOcorrencia){
		return remove(cdOcorrencia, false, null);
	}
	public static Result remove(int cdOcorrencia, boolean cascade){
		return remove(cdOcorrencia, cascade, null);
	}
	public static Result remove(int cdOcorrencia, boolean cascade, Connection connect){
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
			retorno = OcorrenciaMatriculaDAO.delete(cdOcorrencia, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_ocorrencia_matricula");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaMatriculaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaMatriculaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_ocorrencia_matricula A, grl_ocorrencia B WHERE A.cd_ocorrencia = B.cd_ocorrencia ", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	
	public static ResultSetMap getAllByMatricula( int cdMatricula ) {
		return getAllByMatricula(cdMatricula, null);
	}

	public static ResultSetMap getAllByMatricula(int cdMatricula, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT *, P.nm_pessoa AS nm_usuario FROM acd_ocorrencia_matricula A"
					+ "							JOIN grl_ocorrencia B ON (A.cd_ocorrencia = B.cd_ocorrencia) "
					+ "							JOIN seg_usuario U ON (B.cd_usuario = U.cd_usuario) "
					+ "							JOIN grl_pessoa P ON (U.cd_pessoa = P.cd_pessoa) "
					+ "							LEFT OUTER JOIN grl_tipo_ocorrencia C ON (B.cd_tipo_ocorrencia = C.cd_tipo_ocorrencia) "
					+ "						  WHERE (A.cd_matricula_origem = "+cdMatricula+" OR A.cd_matricula_destino = " +cdMatricula+")"
					+ "						  ORDER BY dt_ocorrencia DESC");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaMatriculaServices.getAllByMatricula: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaMatriculaServices.getAllByMatricula: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result removeByMatricula(int cdMatricula) {
		return removeByMatricula(cdMatricula, null);
	}
	
	public static Result removeByMatricula(int cdMatricula, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			ResultSetMap rsm = getAllByMatricula(cdMatricula, connect);
			while (rsm.next()) {
				if (rsm.getInt("cd_matricula_origem") == cdMatricula || rsm.getInt("cd_matricula_destino") == cdMatricula) {
					retorno = remove(rsm.getInt("cd_ocorrencia"), true, connect).getCode();
					if(retorno<=0){
						Conexao.rollback(connect);
						return new Result(-2, "Esta matrícula está vinculada a outros registros e não pode ser excluída!");
					}
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Ocorrência excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir matrícula!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
