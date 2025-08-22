package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class VistoriaAtributoValorServices {

	public static Result save(VistoriaAtributoValor vistoriaAtributoValor){
		return save(vistoriaAtributoValor, null);
	}

	public static Result save(VistoriaAtributoValor vistoriaAtributoValor, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(vistoriaAtributoValor==null)
				return new Result(-1, "Erro ao salvar. VistoriaAtributoValor é nulo");

			int retorno;
			if(vistoriaAtributoValor.getCdVistoria()==0){
				retorno = VistoriaAtributoValorDAO.insert(vistoriaAtributoValor, connect);
				vistoriaAtributoValor.setCdVistoria(retorno);
			}
			else {
				retorno = VistoriaAtributoValorDAO.update(vistoriaAtributoValor, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "VISTORIAATRIBUTOVALOR", vistoriaAtributoValor);
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
	public static Result remove(int cdVistoria, int cdFormularioAtributo){
		return remove(cdVistoria, cdFormularioAtributo, false, null);
	}
	public static Result remove(int cdVistoria, int cdFormularioAtributo, boolean cascade){
		return remove(cdVistoria, cdFormularioAtributo, cascade, null);
	}
	public static Result remove(int cdVistoria, int cdFormularioAtributo, boolean cascade, Connection connect){
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
			retorno = VistoriaAtributoValorDAO.delete(cdVistoria, cdFormularioAtributo, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_vistoria_atributo_valor");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaAtributoValorServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaAtributoValorServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_vistoria_atributo_valor", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
