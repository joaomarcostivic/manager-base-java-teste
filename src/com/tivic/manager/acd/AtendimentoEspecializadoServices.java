package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class AtendimentoEspecializadoServices {
	public static Result save(AtendimentoEspecializado atendimentoEspecializado){
		return save(atendimentoEspecializado, null);
	}
	
	public static Result save(AtendimentoEspecializado atendimentoEspecializado, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(atendimentoEspecializado==null)
				return new Result(-1, "Erro ao salvar. Atendimento especializado é nulo");
			
			int retorno;
			if(atendimentoEspecializado.getCdAtendimentoEspecializado()==0){
				retorno = AtendimentoEspecializadoDAO.insert(atendimentoEspecializado, connect);
				atendimentoEspecializado.setCdAtendimentoEspecializado(retorno);
			}
			else {
				retorno = AtendimentoEspecializadoDAO.update(atendimentoEspecializado, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ATENDIMENTOESPECIALIZADO", atendimentoEspecializado);
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
	
	public static Result remove(int cdAtendimentoEspecializado){
		return remove(cdAtendimentoEspecializado, false, null);
	}
	
	public static Result remove(int cdAtendimentoEspecializado, boolean cascade){
		return remove(cdAtendimentoEspecializado, cascade, null);
	}
	
	public static Result remove(int cdAtendimentoEspecializado, boolean cascade, Connection connect){
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
				retorno = AtendimentoEspecializadoDAO.delete(cdAtendimentoEspecializado, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este atendimento especializado está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Atendimento especializado excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir atendimento especializado!");
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_atendimento_especializado WHERE st_atendimento_especializado = 1 ORDER BY nm_atendimento_especializado");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoEspecializadoDAO.getAll: " + e);
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
		String limit = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if(criterios.get(i).getColumn().equalsIgnoreCase("limit")) {
				limit += " LIMIT "+ criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
		}
		ResultSetMap  _rsm = Search.find("SELECT * FROM acd_atendimento_especializado", "ORDER BY nm_atendimento_especializado" +limit, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	    return _rsm;
	}
}
