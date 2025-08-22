package com.tivic.manager.ptc;

import java.sql.Connection;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class AcaoServices {
	public static Result save(Acao acao){
		return save(acao, null);
	}
	
	public static Result save(Acao acao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(acao==null)
				return new Result(-1, "Erro ao salvar. Ação é nula");
			
			int retorno;
			if(acao.getCdAcao()==0){
				retorno = AcaoDAO.insert(acao, connect);
				acao.setCdAcao(retorno);
			}
			else {
				retorno = AcaoDAO.update(acao, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ACAO", acao);
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
	
	public static Result remove(int cdAcao, int cdDecisao, int cdFluxo){
		return remove(cdAcao, cdDecisao, cdFluxo, false, null);
	}
	
	public static Result remove(int cdAcao, int cdDecisao, int cdFluxo, boolean cascade){
		return remove(cdAcao, cdDecisao, cdFluxo, cascade, null);
	}
	
	public static Result remove(int cdAcao, int cdDecisao, int cdFluxo, boolean cascade, Connection connect){
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
				retorno = AcaoDAO.delete(cdAcao, cdDecisao, cdFluxo, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta ação está vinculada a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Ação excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir ação!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByDecisao(int cdDecisao, int cdFluxo) {
		return getAllByDecisao(cdDecisao, cdFluxo, null);
	}

	public static ResultSetMap getAllByDecisao(int cdDecisao, int cdFluxo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM ptc_acao A " +
					" WHERE A.cd_fluxo = "+cdFluxo+
					"   AND A.cd_decisao = "+cdDecisao+
					" ORDER BY A.tp_acao").executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		try {
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM ptc_acao ORDER BY tp_acao").executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		return Search.find("SELECT * FROM ptc_acao ", "ORDER BY tp_acao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
