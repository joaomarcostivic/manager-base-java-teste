package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class ProgramaRepasseProgramacaoServices {
	
	public static Result save(ProgramaRepasse programaRepasse, ArrayList<ProgramaRepasseProgramacao> programacao){
		return save(programaRepasse, programacao, null);
	}

	public static Result save(ProgramaRepasse programaRepasse, ArrayList<ProgramaRepasseProgramacao> programacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(programaRepasse==null)
				return new Result(-1, "Erro ao salvar. Programa Repasse é nulo");
			
			if(programacao==null)
				return new Result(-1, "Erro ao salvar. Lista de ProgramaRepasse é nulo");

			int retorno = 0;
			Result r;
			
			connect.prepareStatement("DELETE FROM ACD_PROGRAMA_REPASSE_PROGRAMACAO "+
									" WHERE  cd_programa = "+programaRepasse.getCdPrograma()+
									" AND    cd_fonte_receita = "+programaRepasse.getCdFonteReceita()
							).execute();
			
			Iterator<ProgramaRepasseProgramacao> iterator = programacao.iterator();
			while (iterator.hasNext()) {
				r = save(iterator.next(), connect);
				if( r.getCode() <= 0 ){
					if(isConnectionNull)
						Conexao.desconectar(connect);
					return new Result(-1, r.getMessage());
				}
				retorno = r.getCode();
			}
			
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PROGRAMAREPASSEPROGRAMACAO", programacao);
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
	
	public static Result save(ProgramaRepasseProgramacao programaRepasseProgramacao){
		return save(programaRepasseProgramacao, null);
	}

	public static Result save(ProgramaRepasseProgramacao programaRepasseProgramacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(programaRepasseProgramacao==null)
				return new Result(-1, "Erro ao salvar. ProgramaRepasseProgramacao é nulo");

			int retorno;
			if(programaRepasseProgramacao.getCdPrograma()==0){
				retorno = ProgramaRepasseProgramacaoDAO.insert(programaRepasseProgramacao, connect);
				programaRepasseProgramacao.setCdPrograma(retorno);
			}
			else {
				retorno = ProgramaRepasseProgramacaoDAO.update(programaRepasseProgramacao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PROGRAMAREPASSEPROGRAMACAO", programaRepasseProgramacao);
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
	public static Result remove(int cdPrograma, int cdFonteReceita, int cdCentroCustoDespesa, int cdCategoriaEconomicaDespesa){
		return remove(cdPrograma, cdFonteReceita, cdCentroCustoDespesa, cdCategoriaEconomicaDespesa, false, null);
	}
	public static Result remove(int cdPrograma, int cdFonteReceita, int cdCentroCustoDespesa, int cdCategoriaEconomicaDespesa, boolean cascade){
		return remove(cdPrograma, cdFonteReceita, cdCentroCustoDespesa, cdCategoriaEconomicaDespesa, cascade, null);
	}
	public static Result remove(int cdPrograma, int cdFonteReceita, int cdCentroCustoDespesa, int cdCategoriaEconomicaDespesa, boolean cascade, Connection connect){
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
			retorno = ProgramaRepasseProgramacaoDAO.delete(cdPrograma, cdFonteReceita, cdCentroCustoDespesa, cdCategoriaEconomicaDespesa, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_programa_repasse_programacao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseProgramacaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseProgramacaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllByProgramaRepasse( int cdPrograma, int cdFonteReceita ) {
		return getAllByProgramaRepasse(cdPrograma, cdFonteReceita, null);
	}
	
	public static ResultSetMap getAllByProgramaRepasse( int cdPrograma, int cdFonteReceita, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					"SELECT * FROM acd_programa_repasse_programacao A"+
					""+
					" WHERE A.cd_programa = "+cdPrograma+
					" AND   A.cd_fonte_receita = "+cdFonteReceita );
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseProgramacaoServices.getAllByProgramaRepasse: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseProgramacaoServices.getAllByProgramaRepasse: " + e);
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
		return Search.find("SELECT * FROM acd_programa_repasse_programacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
