package com.tivic.manager.mob.grafica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.manager.seg.AuthData;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class LoteImpressaoAitServices {
	
	public static Result save(LoteImpressaoAit loteImpressaoAit){
		return save(loteImpressaoAit, null, null);
	}

	public static Result save(LoteImpressaoAit loteImpressaoAit, AuthData authData){
		return save(loteImpressaoAit, authData, null);
	}

	public static Result save(LoteImpressaoAit loteImpressaoAit, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(loteImpressaoAit==null)
				return new Result(-1, "Erro ao salvar. LoteImpressaoAit é nulo");

			int retorno;
			if(LoteImpressaoAitDAO.get(loteImpressaoAit.getCdLoteImpressao(), loteImpressaoAit.getCdAit(), connect) == null){
				retorno = LoteImpressaoAitDAO.insert(loteImpressaoAit, connect);
			}
			else {
				retorno = LoteImpressaoAitDAO.update(loteImpressaoAit, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "LOTEIMPRESSAOAIT", loteImpressaoAit);
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
	public static Result remove(LoteImpressaoAit loteImpressaoAit) {
		return remove(loteImpressaoAit.getCdLoteImpressao(), loteImpressaoAit.getCdAit());
	}
	public static Result remove(int cdLoteImpressao, int cdAit){
		return remove(cdLoteImpressao, cdAit, false, null, null);
	}
	public static Result remove(int cdLoteImpressao, int cdAit, boolean cascade){
		return remove(cdLoteImpressao, cdAit, cascade, null, null);
	}
	public static Result remove(int cdLoteImpressao, int cdAit, boolean cascade, AuthData authData){
		return remove(cdLoteImpressao, cdAit, cascade, authData, null);
	}
	public static Result remove(int cdLoteImpressao, int cdAit, boolean cascade, AuthData authData, Connection connect){
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
			retorno = LoteImpressaoAitDAO.delete(cdLoteImpressao, cdAit, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro est� vinculado a outros e n�o pode ser exclu�do!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro exclu�do com sucesso!");
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_lote_impressao_ait");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoAitServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoAitServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_lote_impressao_ait", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public LoteImpressaoAit getByCdAit(int cdAit, int tpDocumento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			LoteImpressaoAit loteImpressaoAit = getByCdAit(cdAit, tpDocumento, customConnection);
			customConnection.finishConnection();
			return loteImpressaoAit;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public LoteImpressaoAit getByCdAit(int cdAit, int tpDocumento, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("tp_documento", tpDocumento);
		com.tivic.sol.search.Search<LoteImpressaoAit> search = new SearchBuilder<LoteImpressaoAit>("mob_lote_impressao_ait A")
				.fields("A.*")
				.addJoinTable("JOIN mob_lote_impressao B ON(A.cd_lote_impressao = B.cd_lote_impressao)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(LoteImpressaoAit.class).get(0);
	}
}
