package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class EsgotoSanitarioServices {

	
	public static Result save(EsgotoSanitario esgotoSanitario){
		return save(esgotoSanitario, null);
	}

	public static Result save(EsgotoSanitario esgotoSanitario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(esgotoSanitario==null)
				return new Result(-1, "Erro ao salvar. EsgotoSanitario é nulo");

			int retorno;
			if(esgotoSanitario.getCdEsgotoSanitario()==0){
				retorno = EsgotoSanitarioDAO.insert(esgotoSanitario, connect);
				esgotoSanitario.setCdEsgotoSanitario(retorno);
			}
			else {
				retorno = EsgotoSanitarioDAO.update(esgotoSanitario, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ESGOTOSANITARIO", esgotoSanitario);
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
	public static Result remove(int cdEsgotoSanitario){
		return remove(cdEsgotoSanitario, false, null);
	}
	public static Result remove(int cdEsgotoSanitario, boolean cascade){
		return remove(cdEsgotoSanitario, cascade, null);
	}
	public static Result remove(int cdEsgotoSanitario, boolean cascade, Connection connect){
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
			retorno = EsgotoSanitarioDAO.delete(cdEsgotoSanitario, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_esgoto_sanitario");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EsgotoSanitarioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EsgotoSanitarioServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_esgoto_sanitario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static EsgotoSanitario getById(String idEsgotoSanitario) {
		return getById(idEsgotoSanitario, null);
	}

	public static EsgotoSanitario getById(String idEsgotoSanitario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_esgoto_sanitario WHERE id_esgoto_sanitario=?");
			pstmt.setString(1, idEsgotoSanitario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EsgotoSanitario(rs.getInt("cd_esgoto_sanitario"),
						rs.getString("nm_esgoto_sanitario"),
						rs.getString("id_esgoto_sanitario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EsgotoSanitarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EsgotoSanitarioDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
