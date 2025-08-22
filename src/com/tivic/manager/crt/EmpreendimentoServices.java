package com.tivic.manager.crt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class EmpreendimentoServices {
	public static Result save(Empreendimento empreendimento){
		return save(empreendimento, null);
	}
	
	public static Result save(Empreendimento empreendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(empreendimento==null)
				return new Result(-1, "Erro ao salvar. Empreendimento é nulo");
			
			int retorno;
			if(empreendimento.getCdEmpreendimento()==0){
				retorno = EmpreendimentoDAO.insert(empreendimento, connect);
				empreendimento.setCdEmpreendimento(retorno);
			}
			else {
				retorno = EmpreendimentoDAO.update(empreendimento, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "EMPREENDIMENTO", empreendimento);
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
	
	public static Result remove(int cdEmpreendimento){
		return remove(cdEmpreendimento, false, null);
	}
	
	public static Result remove(int cdEmpreendimento, boolean cascade){
		return remove(cdEmpreendimento, cascade, null);
	}
	
	public static Result remove(int cdEmpreendimento, boolean cascade, Connection connect){
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
				retorno = EmpreendimentoDAO.delete(cdEmpreendimento, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este empreendimento está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Empreendimento excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir empreendimento!");
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
			pstmt = connect.prepareStatement("SELECT * FROM crt_empreendimento A " +
					" LEFT OUTER JOIN alm_local_armazenamento B ON (A.cd_local_armazenamento = B.cd_local_armazenamento) " +
					" ORDER BY nm_local_armazenamento");
			return new ResultSetMap(pstmt.executeQuery());
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
		return Search.find("SELECT * FROM crt_empreendimento A " +
				" LEFT OUTER JOIN alm_local_armazenamento B ON (A.cd_local_armazenamento = B.cd_local_armazenamento) " +
				" ORDER BY nm_local_armazenamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
