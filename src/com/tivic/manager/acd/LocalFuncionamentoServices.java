package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class LocalFuncionamentoServices {
	public static Result save(LocalFuncionamento localFuncionamento){
		return save(localFuncionamento, null);
	}
	
	public static Result save(LocalFuncionamento localFuncionamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(localFuncionamento==null)
				return new Result(-1, "Erro ao salvar. Local de funcionamento é nulo");
			
			int retorno;
			if(localFuncionamento.getCdLocalFuncionamento()==0){
				retorno = LocalFuncionamentoDAO.insert(localFuncionamento, connect);
				localFuncionamento.setCdLocalFuncionamento(retorno);
			}
			else {
				retorno = LocalFuncionamentoDAO.update(localFuncionamento, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "LOCALFUNCIONAMENTO", localFuncionamento);
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
	
	public static Result remove(int cdLocalFuncionamento){
		return remove(cdLocalFuncionamento, false, null);
	}
	
	public static Result remove(int cdLocalFuncionamento, boolean cascade){
		return remove(cdLocalFuncionamento, cascade, null);
	}
	
	public static Result remove(int cdLocalFuncionamento, boolean cascade, Connection connect){
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
				retorno = LocalFuncionamentoDAO.delete(cdLocalFuncionamento, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este local de funcionamento está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Local de funcionamento excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir local de funcionamento!");
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_local_funcionamento ORDER BY nm_local_funcionamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalFuncionamentoDAO.getAll: " + e);
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
		ResultSetMap  _rsm =  Search.find("SELECT * FROM acd_local_funcionamento ", " ORDER BY nm_local_funcionamento " +limit, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	    return _rsm;
	
	}
}