package com.tivic.manager.flp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class TabelaSalarioNivelServices 	{
	public static Result save(TabelaSalarioNivel tabelaSalarioNivel){
		return save(tabelaSalarioNivel, null);
	}
	
	public static Result save(TabelaSalarioNivel tabelaSalarioNivel, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tabelaSalarioNivel==null)
				return new Result(-1, "Erro ao salvar. Nível de salário é nulo");
			
			int retorno;
			if(tabelaSalarioNivel.getCdNivelSalario()==0){
				retorno = TabelaSalarioNivelDAO.insert(tabelaSalarioNivel, connect);
				tabelaSalarioNivel.setCdNivelSalario(retorno);
			}
			else {
				retorno = TabelaSalarioNivelDAO.update(tabelaSalarioNivel, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TABELASALARIONIVEL", tabelaSalarioNivel);
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
	
	public static Result remove(int cdTabelaSalario, int cdNivelSalario){
		return remove(cdTabelaSalario, cdNivelSalario, false, null);
	}
	
	public static Result remove(int cdTabelaSalario, int cdNivelSalario, boolean cascade){
		return remove(cdTabelaSalario, cdNivelSalario, cascade, null);
	}
	
	public static Result remove(int cdTabelaSalario, int cdNivelSalario, boolean cascade, Connection connect){
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
				retorno = TabelaSalarioNivelDAO.delete(cdTabelaSalario, cdNivelSalario, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta nível de salário está vinculada a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Nível de salário excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir nível de salário!");
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
			pstmt = connect.prepareStatement("SELECT * FROM flp_tabela_salario_nivel ORDER BY nm_nivel_salario");
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
		return Search.find("SELECT * FROM flp_tabela_salario_nivel ORDER BY nm_nivel_salario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}