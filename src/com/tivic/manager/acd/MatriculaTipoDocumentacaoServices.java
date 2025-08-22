package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.TipoDocumentacaoServices;
import com.tivic.manager.util.Util;

public class MatriculaTipoDocumentacaoServices {

	public static Result save(int cdMatricula, String idTipoDocumentacao){
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("cd_matricula", "" + cdMatricula, ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("B.id_tipo_documentacao", idTipoDocumentacao, ItemComparator.EQUAL, Types.VARCHAR));
		ResultSetMap rsm = find(criterios);
		Result result = new Result(1);
		if(!rsm.next()){
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("id_tipo_documentacao", idTipoDocumentacao, ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsmTipoDocumentacao = TipoDocumentacaoServices.find(criterios);
			if(rsmTipoDocumentacao.next()){
				result = save(new MatriculaTipoDocumentacao(cdMatricula, rsmTipoDocumentacao.getInt("cd_tipo_documentacao"), 1, null, Util.getDataAtual()));
			}
		}
		
		return result;
	}
	
	public static Result save(MatriculaTipoDocumentacao matriculaTipoDocumentacao){
		return save(matriculaTipoDocumentacao, null);
	}

	public static Result save(MatriculaTipoDocumentacao matriculaTipoDocumentacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(matriculaTipoDocumentacao==null)
				return new Result(-1, "Erro ao salvar. MatriculaTipoDocumentacao é nulo");

			int retorno;
			
			if(MatriculaTipoDocumentacaoDAO.get(matriculaTipoDocumentacao.getCdMatricula(), matriculaTipoDocumentacao.getCdTipoDocumentacao(), connect)==null){
				retorno = MatriculaTipoDocumentacaoDAO.insert(matriculaTipoDocumentacao, connect);
				matriculaTipoDocumentacao.setCdMatricula(retorno);
			}
			else {
				retorno = MatriculaTipoDocumentacaoDAO.update(matriculaTipoDocumentacao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "MATRICULATIPODOCUMENTACAO", matriculaTipoDocumentacao);
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
	
	public static Result remove(int cdMatricula, String idTipoDocumentacao){
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("cd_matricula", "" + cdMatricula, ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("B.id_tipo_documentacao", idTipoDocumentacao, ItemComparator.EQUAL, Types.VARCHAR));
		ResultSetMap rsm = find(criterios);
		Result result = new Result(1);
		if(rsm.next()){
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("id_tipo_documentacao", idTipoDocumentacao, ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsmTipoDocumentacao = TipoDocumentacaoServices.find(criterios);
			if(rsmTipoDocumentacao.next()){
				result = remove(cdMatricula, rsmTipoDocumentacao.getInt("cd_tipo_documentacao"));
			}
		}
		
		return result;
	}
	
	public static Result remove(int cdMatricula, int cdTipoDocumentacao){
		return remove(cdMatricula, cdTipoDocumentacao, false, null);
	}
	public static Result remove(int cdMatricula, int cdTipoDocumentacao, boolean cascade){
		return remove(cdMatricula, cdTipoDocumentacao, cascade, null);
	}
	public static Result remove(int cdMatricula, int cdTipoDocumentacao, boolean cascade, Connection connect){
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
			retorno = MatriculaTipoDocumentacaoDAO.delete(cdMatricula, cdTipoDocumentacao, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_tipo_documentacao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoDocumentacaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoDocumentacaoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_matricula_tipo_documentacao A, grl_tipo_documentacao B WHERE A.cd_tipo_documentacao = B.cd_tipo_documentacao ", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
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
				if (rsm.getInt("cd_matricula") == cdMatricula) {
					retorno = remove(cdMatricula, rsm.getInt("cd_tipo_documentacao"), true, connect).getCode();
					if(retorno<=0){
						Conexao.rollback(connect);
						return new Result(-2, "Esta matrícula está vinculada a outros registros e não pode ser excluída!");
					}
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Matrícula excluída com sucesso!");
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
	
	public static ResultSetMap getAllByMatricula(int cdMatricula) {
		return getAllByMatricula(cdMatricula, null);
	}

	public static ResultSetMap getAllByMatricula(int cdMatricula, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_tipo_documentacao WHERE cd_matricula = " + cdMatricula);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoDocumentacaoServices.getAllByMatricula: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoDocumentacaoServices.getAllByMatricula: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
