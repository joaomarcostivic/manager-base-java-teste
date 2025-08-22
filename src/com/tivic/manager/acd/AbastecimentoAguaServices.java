package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class AbastecimentoAguaServices {
	
	public static Result save(AbastecimentoAgua abastecimentoAgua){
		return save(abastecimentoAgua, null);
	}

	public static Result save(AbastecimentoAgua abastecimentoAgua, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(abastecimentoAgua==null)
				return new Result(-1, "Erro ao salvar. AbastecimentoAgua é nulo");

			int retorno;
			if(abastecimentoAgua.getCdAbastecimentoAgua()==0){
				retorno = AbastecimentoAguaDAO.insert(abastecimentoAgua, connect);
				abastecimentoAgua.setCdAbastecimentoAgua(retorno);
			}
			else {
				retorno = AbastecimentoAguaDAO.update(abastecimentoAgua, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ABASTECIMENTOAGUA", abastecimentoAgua);
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
	public static Result remove(int cdAbastecimentoAgua){
		return remove(cdAbastecimentoAgua, false, null);
	}
	public static Result remove(int cdAbastecimentoAgua, boolean cascade){
		return remove(cdAbastecimentoAgua, cascade, null);
	}
	public static Result remove(int cdAbastecimentoAgua, boolean cascade, Connection connect){
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
			retorno = AbastecimentoAguaDAO.delete(cdAbastecimentoAgua, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_abastecimento_agua");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoAguaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoAguaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_abastecimento_agua", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static AbastecimentoAgua getById(String idAbastecimentoAgua) {
		return getById(idAbastecimentoAgua, null);
	}

	public static AbastecimentoAgua getById(String idAbastecimentoAgua, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_abastecimento_agua WHERE id_abastecimento_agua=?");
			pstmt.setString(1, idAbastecimentoAgua);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AbastecimentoAgua(rs.getInt("cd_abastecimento_agua"),
						rs.getString("nm_abastecimento_agua"),
						rs.getString("id_abastecimento_agua"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoAguaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoAguaDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}
