package com.tivic.manager.grl;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class FormularioServices {

	public static Result save(Formulario formulario){
		return save(formulario, null);
	}

	public static Result save(Formulario formulario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(formulario==null)
				return new Result(-1, "Erro ao salvar. Formulario é nulo");

			int retorno;
			if(formulario.getCdFormulario()==0){
				retorno = FormularioDAO.insert(formulario, connect);
				formulario.setCdFormulario(retorno);
			}
			else {
				retorno = FormularioDAO.update(formulario, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "FORMULARIO", formulario);
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
	public static Result remove(int cdFormulario){
		return remove(cdFormulario, false, null);
	}
	public static Result remove(int cdFormulario, boolean cascade){
		return remove(cdFormulario, cascade, null);
	}
	public static Result remove(int cdFormulario, boolean cascade, Connection connect){
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
			retorno = FormularioDAO.delete(cdFormulario, connect);
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
			pstmt = connect.prepareStatement(" SELECT A.*, B.cd_tipo_documento, B.nm_tipo_documento "
										   + " FROM grl_formulario A "
										   + " LEFT OUTER JOIN gpn_tipo_documento B ON (A.cd_formulario = B.cd_formulario)"
										   + " WHERE A.st_formulario = 1 "
										   + " ORDER BY A.nm_formulario ");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllAtributos(int cdFormulario) {
		return getAllAtributos(cdFormulario, null);
	}

	public static ResultSetMap getAllAtributos(int cdFormulario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			return new ResultSetMap(connect.prepareStatement("SELECT * " +
															 "FROM grl_formulario_atributo A " +
															 "WHERE cd_formulario = "+cdFormulario+
															 " ORDER BY nr_ordem ").executeQuery());
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
	
	public static ResultSetMap find() {
		return find(new ArrayList<ItemComparator>(), null);
	}
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_formulario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}