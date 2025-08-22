package com.tivic.manager.grl;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class FormularioAtributoServices {

	public static final int TP_STRING = 0;
	public static final int TP_INT = 1;
	public static final int TP_FLOAT = 2;
	public static final int TP_DATA = 3;
	public static final int TP_BOOLEAN = 4;
	public static final int TP_MEMO = 5;
	public static final int TP_OPCOES = 6;
	public static final int TP_CALCULO = 7;
	public static final int TP_PESSOA = 8;
	
	public static final String tiposAtributo[] = {
		"Texto",
		"Inteiro",
		"Float",
		"Data",
		"Sim/Não",
		"Memo",
		"Opções",
		"Calculado",
		"Filtro a partir do Cadastro Geral"};
	
	public static Result save(FormularioAtributo formularioAtributo){
		return save(formularioAtributo, null);
	}

	public static Result save(FormularioAtributo formularioAtributo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(formularioAtributo==null)
				return new Result(-1, "Erro ao salvar. FormularioAtributo é nulo");

			int retorno;
			if(formularioAtributo.getCdFormularioAtributo()==0){
				retorno = FormularioAtributoDAO.insert(formularioAtributo, connect);
				formularioAtributo.setCdFormularioAtributo(retorno);
			}
			else {
				retorno = FormularioAtributoDAO.update(formularioAtributo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "FORMULARIOATRIBUTO", formularioAtributo);
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
	public static Result remove(int cdFormularioAtributo){
		return remove(cdFormularioAtributo, false, null);
	}
	public static Result remove(int cdFormularioAtributo, boolean cascade){
		return remove(cdFormularioAtributo, cascade, null);
	}
	public static Result remove(int cdFormularioAtributo, boolean cascade, Connection connect){
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
			retorno = FormularioAtributoDAO.delete(cdFormularioAtributo, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_formulario_atributo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByFormulario(int cdFormulario) {
		return getAllByFormulario(cdFormulario, null);
	}
	
	public static ResultSetMap getAllByFormulario(int cdFormulario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_formulario_atributo WHERE cd_formulario = ?");
			pstmt.setInt(1, cdFormulario);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoServices.getAllByFormulario: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoServices.getAllByFormulario: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllOpcoes(int cdFormularioAtributo) {
		return getAllOpcoes(cdFormularioAtributo, null);
	}

	public static ResultSetMap getAllOpcoes(int cdFormularioAtributo, Connection connect) {
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
			System.err.println("Erro! FormularioAtributoServices.getAllOpcoes: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllAtributosOpcao() {
		return getAllAtributosOpcao(null);
	}

	public static ResultSetMap getAllAtributosOpcao(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM grl_formulario_atributo " +
					"WHERE tp_dado = " + TP_OPCOES);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoServices.getAllAtributosOpcao: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllAtributosFiltroPessoa() {
		return getAllAtributosFiltroPessoa(null);
	}

	public static ResultSetMap getAllAtributosFiltroPessoa(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM grl_formulario_atributo " +
					"WHERE tp_dado = " + TP_PESSOA +
					"  AND NOT cd_vinculo IS NULL");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoServices.getAllAtributosFiltroPessoa: " + e);
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
		return Search.find("SELECT * FROM grl_formulario_atributo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	@Deprecated
	public static int delete(int cdFormularioAtributo) {
		return delete(cdFormularioAtributo, null);
	}

	@Deprecated
	public static int delete(int cdFormularioAtributo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_formulario_atributo_valor " +
					"WHERE cd_formulario_atributo = ?");
			pstmt.setInt(1, cdFormularioAtributo);
			pstmt.execute();

			pstmt = connect.prepareStatement("DELETE FROM grl_formulario_atributo_opcao " +
					"WHERE cd_formulario_atributo = ?");
			pstmt.setInt(1, cdFormularioAtributo);
			pstmt.execute();

			if (FormularioAtributoDAO.delete(cdFormularioAtributo, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoServices.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static FormularioAtributo getByNmAtributo(String nmAtributo, int cdFormulario) {	
		return getByNmAtributo(nmAtributo, cdFormulario, null);
	}
	
	public static FormularioAtributo getByNmAtributo(String nmAtributo, int cdFormulario, Connection connect) {
		
		boolean isConnectionNull = connect == null;
		
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM GRL_FORMULARIO_ATRIBUTO WHERE CD_FORMULARIO = ? AND NM_ATRIBUTO = ?");
			pstmt.setInt(1, cdFormulario);
			pstmt.setString(2, nmAtributo);
			
			ResultSet _rs = pstmt.executeQuery();
			FormularioAtributo _formularioAtributo = new FormularioAtributo();
			if(_rs.next())
				_formularioAtributo = FormularioAtributoDAO.get(_rs.getInt("cd_formulario_atributo"));
			
			return _formularioAtributo;				
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}