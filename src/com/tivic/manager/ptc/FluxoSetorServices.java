package com.tivic.manager.ptc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class FluxoSetorServices {
	public static Result save(FluxoSetor fluxoSetor){
		return save(fluxoSetor, null);
	}
	
	public static Result save(FluxoSetor fluxoSetor, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(fluxoSetor==null)
				return new Result(-1, "Erro ao salvar. Setor é nulo");
			
			// Verifica se já existe setor no fluxo
			ResultSet rs = connect.prepareStatement("SELECT * FROM ptc_fluxo_setor A " +
					                                "WHERE A.cd_fluxo = " +fluxoSetor.getCdFluxo()+
					   								"  AND A.cd_setor ="+fluxoSetor.getCdSetor()).executeQuery();
			if(rs.next())
				return new Result(-1, "Este setor já foi incluído no fluxo.");
			
			int retorno = FluxoSetorDAO.insert(fluxoSetor, connect);
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "FLUXOSETOR", fluxoSetor);
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
	
	public static Result remove(int cdFluxo, int cdSetor){
		return remove(cdFluxo, cdSetor, false, null);
	}
	
	public static Result remove(int cdFluxo, int cdSetor, boolean cascade){
		return remove(cdFluxo, cdSetor, cascade, null);
	}
	
	public static Result remove(int cdFluxo, int cdSetor, boolean cascade, Connection connect){
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
				retorno = FluxoSetorDAO.delete(cdFluxo, cdSetor, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByFluxo(int cdFluxo) {
		return getAllByFluxo(cdFluxo, null);
	}

	public static ResultSetMap getAllByFluxo(int cdFluxo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM ptc_fluxo_setor A " +
					" JOIN grl_setor B ON (A.cd_setor = B.cd_setor)" +
					" WHERE cd_fluxo = "+cdFluxo+
					" ORDER BY B.nm_setor").executeQuery());
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
		return Search.find("SELECT * FROM ptc_fluxo_setor A " +
				" JOIN grl_setor B ON (A.cd_setor = B.cd_setor)", "ORDER BY B.nm_setor", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
