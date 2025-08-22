package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.srh.Funcao;
import com.tivic.manager.srh.FuncaoDAO;

import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.ItemComparator;
import sol.util.Result;

public class PaisServices {
	
	public static Result save(Pais pais){
		return save(pais, null);
	}
	
	public static Result save(Pais pais, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(pais==null)
				return new Result(-1, "Erro ao salvar. Pais é nulo");
			
			int retorno;
			if(pais.getCdPais()==0){
				retorno = PaisDAO.insert(pais, connect);
				pais.setCdPais(retorno);
			}
			else
				retorno = PaisDAO.update(pais, connect);
		
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PAIS", pais);
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
	
	public static Result remove(int cdPais){
		return remove(cdPais, false, null);
	}
	
	public static Result remove(int cdPais, boolean cascade){
		return remove(cdPais, cascade, null);
	}
	
	public static Result remove(int cdPais, boolean cascade, Connection connect){
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
				retorno = PaisDAO.delete(cdPais, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este pais está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Pais excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir pais!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.nm_regiao FROM grl_pais A " +
				           "LEFT OUTER JOIN grl_regiao B ON (A.cd_regiao = B.cd_regiao) ",
				           "ORDER BY nm_pais, sg_pais", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
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
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_regiao " +
					                         "FROM grl_pais A " +
					                         "LEFT OUTER JOIN grl_regiao B ON (A.cd_regiao = B.cd_regiao) " +
					                         "ORDER BY nm_pais");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PaisDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PaisDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int init()	{
		Connection connect = Conexao.conectar();
		try {
			// 
			ResultSetMap rsm = getAll(connect);
			if(rsm.next())
				return rsm.getInt("cd_pais");
			Pais pais = new Pais(0, "Brasil", "BR", 0, "01058");
			return PaisDAO.insert(pais, connect);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaFgts.init: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static Pais getById(String idPais) {
		return getById(idPais, null);
	}

	public static Pais getById(String idPais, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pais WHERE id_pais=?");
			pstmt.setString(1, idPais);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return PaisDAO.get(rs.getInt("cd_pais"), connect);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PaisDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PaisDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}