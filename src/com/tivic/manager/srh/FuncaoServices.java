package com.tivic.manager.srh;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.TipoDocumentacao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class FuncaoServices {
	
	public static final String ID_PROFESSOR = "01";
	public static final String ID_AUXILIAR = "02";
	public static final String ID_MONITOR = "03";
	public static final String ID_INTERPRETE_LIBRAS = "04";
	public static final String ID_CUIDADOR = "08";
	
	public static Result save(Funcao funcao){
		return save(funcao, null);
	}
	
	public static Result save(Funcao funcao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(funcao==null)
				return new Result(-1, "Erro ao salvar. Função é nulo");
			
			int retorno;
			if(funcao.getCdFuncao()==0){
				retorno = FuncaoDAO.insert(funcao, connect);
				funcao.setCdFuncao(retorno);
			}
			else {
				retorno = FuncaoDAO.update(funcao, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "FUNCAO", funcao);
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
	
	public static Result remove(int cdFuncao){
		return remove(cdFuncao, false, null);
	}
	
	public static Result remove(int cdFuncao, boolean cascade){
		return remove(cdFuncao, cascade, null);
	}
	
	public static Result remove(int cdFuncao, boolean cascade, Connection connect){
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
				retorno = FuncaoDAO.delete(cdFuncao, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta funcao está vinculada a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Funcao excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir função!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAll() {
		return getAll(0, null);
	}

	public static ResultSetMap getAll(int cdEmpresa) {
		return getAll(cdEmpresa, null);
	}

	public static ResultSetMap getAll(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM srh_funcao " +
					 (cdEmpresa > 0 ? "WHERE cd_empresa = ?" : ""/*"WHERE cd_empresa IS NULL"*/) + " ORDER BY nm_funcao");
			if (cdEmpresa > 0)
				pstmt.setInt(1, cdEmpresa);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FuncaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllFuncaoEducacenso() {
		return getAllFuncaoEducacenso(null);
	}

	public static ResultSetMap getAllFuncaoEducacenso(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM srh_funcao WHERE cd_empresa IS NULL ORDER BY nm_funcao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FuncaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Funcao getById(String idFuncao) {
		return getById(idFuncao, null);
	}

	public static Funcao getById(String idFuncao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM srh_funcao WHERE id_funcao=?");
			pstmt.setString(1, idFuncao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return FuncaoDAO.get(rs.getInt("cd_funcao"), connect);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FuncaoServices.getById: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FuncaoServices.getById: " + e);
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
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try{
			return Search.find("SELECT * FROM srh_funcao", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		}catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FuncaoServices.find: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
}