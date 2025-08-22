package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class CategoriaDefeitoVistoriaItemServices {

	public static Result save(CategoriaDefeitoVistoriaItem categoriaDefeitoVistoriaItem){
		return save(categoriaDefeitoVistoriaItem, null);
	}

	public static Result save(CategoriaDefeitoVistoriaItem categoriaDefeitoVistoriaItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(categoriaDefeitoVistoriaItem==null)
				return new Result(-1, "Erro ao salvar. CategoriaDefeitoVistoriaItem é nulo");

			int retorno;
			if(categoriaDefeitoVistoriaItem.getCdCategoriaDefeitoVistoriaItem()==0){
				retorno = CategoriaDefeitoVistoriaItemDAO.insert(categoriaDefeitoVistoriaItem, connect);
				categoriaDefeitoVistoriaItem.setCdCategoriaDefeitoVistoriaItem(retorno);
			}
			else {
				retorno = CategoriaDefeitoVistoriaItemDAO.update(categoriaDefeitoVistoriaItem, connect);
			}

			if(retorno<=0){
				categoriaDefeitoVistoriaItem = CategoriaDefeitoVistoriaItemDAO.get(categoriaDefeitoVistoriaItem.getCdCategoriaDefeitoVistoriaItem());
				Conexao.rollback(connect);
			}else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CATEGORIADEFEITOVISTORIAITEM", categoriaDefeitoVistoriaItem);
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
	public static Result remove(int cdCategoriaDefeitoVistoriaItem){
		return remove(cdCategoriaDefeitoVistoriaItem, false, null);
	}
	public static Result remove(int cdCategoriaDefeitoVistoriaItem, boolean cascade){
		return remove(cdCategoriaDefeitoVistoriaItem, cascade, null);
	}
	public static Result remove(int cdCategoriaDefeitoVistoriaItem, boolean cascade, Connection connect){
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
			retorno = CategoriaDefeitoVistoriaItemDAO.delete(cdCategoriaDefeitoVistoriaItem, connect);
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
			pstmt = connect.prepareStatement("SELECT a.*, b.nm_categoria_defeito as nm_categoria_defeito_superior FROM mob_categoria_defeito_vistoria_item a "+
											" LEFT OUTER JOIN mob_categoria_defeito_vistoria_item b "+
											"			 ON ( a.cd_categoria_superior = b.cd_categoria_defeito_vistoria_item ) ");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaDefeitoVistoriaItemServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaDefeitoVistoriaItemServices.getAll: " + e);
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
		return Search.find("SELECT a.*, b.nm_categoria_defeito as nm_categoria_defeito_superior FROM mob_categoria_defeito_vistoria_item a "+
							" LEFT OUTER JOIN mob_categoria_defeito_vistoria_item b "+
							"			 ON ( a.cd_categoria_superior = b.cd_categoria_defeito_vistoria_item ) "
							, criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
