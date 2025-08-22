package com.tivic.manager.acd;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class ProgramaRepasseServices {
	
	public static Result save(ProgramaRepasse programaRepasse){
		return save(programaRepasse, null);
	}

	public static Result save(ProgramaRepasse programaRepasse, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(programaRepasse==null)
				return new Result(-1, "Erro ao salvar. ProgramaRepasse é nulo");

			int retorno;
			
			ProgramaRepasse programaRepasseTmp = ProgramaRepasseDAO.get( 
																	programaRepasse.getCdPrograma(),
																	programaRepasse.getCdFonteReceita()
															);
			if(programaRepasseTmp==null){
				retorno = ProgramaRepasseDAO.insert(programaRepasse, connect);
				programaRepasse.setCdPrograma(retorno);
			}
			else {
				retorno = ProgramaRepasseDAO.update(programaRepasse, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PROGRAMAREPASSE", programaRepasse);
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
	public static Result remove(int cdPrograma, int cdFonteReceita){
		return remove(cdPrograma, cdFonteReceita, false, null);
	}
	public static Result remove(int cdPrograma, int cdFonteReceita, boolean cascade){
		return remove(cdPrograma, cdFonteReceita, cascade, null);
	}
	public static Result remove(int cdPrograma, int cdFonteReceita, boolean cascade, Connection connect){
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
			retorno = ProgramaRepasseDAO.delete(cdPrograma, cdFonteReceita, connect);
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
	
	public static ResultSetMap getAllByPrograma(int cdPrograma) {
		return getAllByPrograma(cdPrograma, null);
	}
	
	public static ResultSetMap getAllByPrograma(int cdPrograma, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
						" SELECT * FROM acd_programa_repasse  A "+
						" JOIN ctb_fonte_receita          B on ( A.cd_fonte_receita = B.cd_fonte_receita ) "+
						" JOIN ctb_centro_custo           C on ( A.cd_centro_custo_receita = C.cd_centro_custo ) "+
						" JOIN adm_categoria_economica    D on ( A.cd_categoria_economica_receita = D.cd_categoria_economica ) "+
						" WHERE A.cd_programa = "+cdPrograma);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseServices.getAll: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_programa_repasse");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_programa_repasse", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
