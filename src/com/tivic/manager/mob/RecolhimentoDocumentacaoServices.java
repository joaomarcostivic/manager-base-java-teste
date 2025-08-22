package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class RecolhimentoDocumentacaoServices {

	public static Result save( ArrayList<RecolhimentoDocumentacao>  recolhimentoDocumentacoes, int cdRrd, int cdTrrav){
		return save(recolhimentoDocumentacoes, cdRrd, cdTrrav, null);
	}

	public static Result save(ArrayList<RecolhimentoDocumentacao>  recolhimentoDocumentacoes,int cdRrd, int cdTrrav,AuthData authData){
		return save(recolhimentoDocumentacoes,cdRrd, cdTrrav, authData, null);
	}

	public static Result save(ArrayList<RecolhimentoDocumentacao>  recolhimentoDocumentacoes, int cdRrd, int cdTrrav,AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(recolhimentoDocumentacoes==null)
				return new Result(-1, "Erro ao salvar. RecolhimentoDocumentacao é nulo");			
			
			int retorno = 0;
			for (RecolhimentoDocumentacao recolhimentoDocumentacao: recolhimentoDocumentacoes) {	
				
				if(cdRrd > 0){
					recolhimentoDocumentacao.setCdRrd(cdRrd);
				}
				
				if(cdTrrav > 0){
					recolhimentoDocumentacao.setCdTrrav(cdTrrav);
				}
				
				
				retorno = RecolhimentoDocumentacaoDAO.insert(recolhimentoDocumentacao, connect);
				
				if(retorno<=0)
					break;				
			}			

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "RECOLHIMENTODOCUMENTACAO", recolhimentoDocumentacoes);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(RecolhimentoDocumentacao recolhimentoDocumentacao) {
		return remove(recolhimentoDocumentacao.getCdRecolhimentoDocumentacao(), recolhimentoDocumentacao.getCdRrd());
	}
	public static Result remove(int cdRecolhimentoDocumentacao, int cdTipoDocumentacao){
		return remove(cdRecolhimentoDocumentacao, cdTipoDocumentacao, false, null, null);
	}
	public static Result remove(int cdRecolhimentoDocumentacao, int cdTipoDocumentacao, boolean cascade){
		return remove(cdRecolhimentoDocumentacao, cdTipoDocumentacao, cascade, null, null);
	}
	public static Result remove(int cdRecolhimentoDocumentacao, int cdTipoDocumentacao, boolean cascade, AuthData authData){
		return remove(cdRecolhimentoDocumentacao, cdTipoDocumentacao, cascade, authData, null);
	}
	public static Result remove(int cdRecolhimentoDocumentacao, int cdTipoDocumentacao, boolean cascade, AuthData authData, Connection connect){
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
			retorno = RecolhimentoDocumentacaoDAO.delete(cdRecolhimentoDocumentacao, cdTipoDocumentacao, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_recolhimento_documentacao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecolhimentoDocumentacaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecolhimentoDocumentacaoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_recolhimento_documentacao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
