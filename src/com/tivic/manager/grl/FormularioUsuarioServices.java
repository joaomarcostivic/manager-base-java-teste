package com.tivic.manager.grl;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class FormularioUsuarioServices {

	public static Result save(FormularioUsuario formularioUsuario){
		return save(formularioUsuario, null);
	}

	public static Result save(FormularioUsuario formularioUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(formularioUsuario==null)
				return new Result(-1, "Erro ao salvar. FormularioUsuario é nulo");

			int retorno;
			if(formularioUsuario.getCdFormularioUsuario()==0){
				retorno = FormularioUsuarioDAO.insert(formularioUsuario, connect);
				formularioUsuario.setCdFormularioUsuario(retorno);
			}
			else {
				retorno = FormularioUsuarioDAO.update(formularioUsuario, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "FORMULARIOUSUARIO", formularioUsuario);
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
	public static Result remove(int cdFormularioUsuario){
		return remove(cdFormularioUsuario, false, null);
	}
	public static Result remove(int cdFormularioUsuario, boolean cascade){
		return remove(cdFormularioUsuario, cascade, null);
	}
	public static Result remove(int cdFormularioUsuario, boolean cascade, Connection connect){
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
			retorno = FormularioUsuarioDAO.delete(cdFormularioUsuario, connect);
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
	
	public static Result updateViews(Object objeto, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_formulario_usuario");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioUsuarioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioUsuarioServices.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_formulario_usuario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap findFormularioByUsuario() {
		return findFormularioByUsuario(new ArrayList<ItemComparator>(), null);
	}
	public static ResultSetMap findFormularioByUsuario(ArrayList<ItemComparator> criterios) {
		return findFormularioByUsuario(criterios, null);
	}

	public static ResultSetMap findFormularioByUsuario(ArrayList<ItemComparator> criterios, Connection connect) {
		String sql = "SELECT * FROM grl_formulario_usuario A, grl_formulario B WHERE A.cd_formulario = B.cd_formulario";
		return Search.find(sql, criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
