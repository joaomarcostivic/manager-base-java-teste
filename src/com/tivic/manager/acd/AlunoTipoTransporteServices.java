package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class AlunoTipoTransporteServices {

	public static Result save(AlunoTipoTransporte alunoTipoTransporte){
		return save(alunoTipoTransporte, null);
	}

	public static Result save(AlunoTipoTransporte alunoTipoTransporte, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(alunoTipoTransporte==null)
				return new Result(-1, "Erro ao salvar. AlunoTipoTransporte é nulo");

			ResultSetMap rsmAlunoTipoTransporte = getTipoTransporteByAluno(alunoTipoTransporte.getCdAluno(), connect);
			if(rsmAlunoTipoTransporte.size() == 3){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Aluno não pode ter mais do que 3 tipos de transporte cadastrado");
			}
			
			int retorno;
			if(AlunoTipoTransporteDAO.get(alunoTipoTransporte.getCdAluno(), alunoTipoTransporte.getCdTipoTransporte())==null){
				retorno = AlunoTipoTransporteDAO.insert(alunoTipoTransporte, connect);
				alunoTipoTransporte.setCdAluno(retorno);
			}
			else {
				retorno = AlunoTipoTransporteDAO.update(alunoTipoTransporte, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ALUNOTIPOTRANSPORTE", alunoTipoTransporte);
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
	
	public static Result saveTipoTransporte(int cdAluno, int cdTipoTransporte){
		return saveTipoTransporte(cdAluno, cdTipoTransporte, null);
	}

	public static Result saveTipoTransporte(int cdAluno, int cdTipoTransporte, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno = 1;
			
			if(AlunoTipoTransporteDAO.get(cdAluno, cdTipoTransporte, connect) == null){
				retorno = AlunoTipoTransporteDAO.insert(new AlunoTipoTransporte(cdAluno, cdTipoTransporte), connect);
				if(retorno < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao salvar");
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...");
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
	
	
	public static Result remove(int cdAluno, int cdTipoTransporte){
		return remove(cdAluno, cdTipoTransporte, false, null);
	}
	public static Result remove(int cdAluno, int cdTipoTransporte, boolean cascade){
		return remove(cdAluno, cdTipoTransporte, cascade, null);
	}
	public static Result remove(int cdAluno, int cdTipoTransporte, boolean cascade, Connection connect){
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
			retorno = AlunoTipoTransporteDAO.delete(cdAluno, cdTipoTransporte, connect);
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
	public static ResultSetMap getTipoTransporteByAluno(int cdAluno) {
		return getTipoTransporteByAluno(cdAluno, null);
	}
	
	public static ResultSetMap getTipoTransporteByAluno(int cdAluno, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_aluno_tipo_transporte A, fta_tipo_transporte B "
					+ " WHERE A.cd_tipo_transporte = B.cd_tipo_transporte "
					+ "   AND A.cd_aluno = ? ");
			pstmt.setInt(1, cdAluno);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoTipoTransporteServices.getTipoTransporteByAluno: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoTipoTransporteServices.getTipoTransporteByAluno: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_aluno_tipo_transporte");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoTipoTransporteServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoTipoTransporteServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_aluno_tipo_transporte", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
