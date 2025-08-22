package com.tivic.manager.grl;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class FormularioAtributoOpcaoServices {

	public static Result save(FormularioAtributoOpcao formularioAtributoOpcao){
		return save(formularioAtributoOpcao, null);
	}

	public static Result save(FormularioAtributoOpcao formularioAtributoOpcao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(formularioAtributoOpcao==null)
				return new Result(-1, "Erro ao salvar. FormularioAtributoOpcao é nulo");

			int retorno;
			
			FormularioAtributoOpcao opcao = FormularioAtributoOpcaoDAO.get(formularioAtributoOpcao.getCdOpcao(), formularioAtributoOpcao.getCdFormularioAtributo(), connect);
			
			if(opcao==null){
				retorno = FormularioAtributoOpcaoDAO.insert(formularioAtributoOpcao, connect);
				formularioAtributoOpcao.setCdFormularioAtributo(retorno);
			}
			else {
				retorno = FormularioAtributoOpcaoDAO.update(formularioAtributoOpcao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "FORMULARIOATRIBUTOOPCAO", formularioAtributoOpcao);
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
	public static Result remove(int cdFormularioAtributo, int cdOpcao){
		return remove(cdFormularioAtributo, cdOpcao, false, null);
	}
	public static Result remove(int cdFormularioAtributo, int cdOpcao, boolean cascade){
		return remove(cdFormularioAtributo, cdOpcao, cascade, null);
	}
	public static Result remove(int cdFormularioAtributo, int cdOpcao, boolean cascade, Connection connect){
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
			retorno = FormularioAtributoOpcaoDAO.delete(cdFormularioAtributo, cdOpcao, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM GRL_FORMULARIO_ATRIBUTO_OPCAO");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoOpcaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoOpcaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByFormularioAtributo(int cdFormularioAtributo) {
		return getAllByFormularioAtributo(cdFormularioAtributo, null);
	}

	public static ResultSetMap getAllByFormularioAtributo(int cdFormularioAtributo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_formulario_atributo_opcao " +
											 "WHERE cd_formulario_atributo = ?");
			pstmt.setInt(1, cdFormularioAtributo);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoOpcaoServices.getAllByFormularioAtributo: " + e);
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
		return Search.find("SELECT * FROM GRL_FORMULARIO_ATRIBUTO_OPCAO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
