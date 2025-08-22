package com.tivic.manager.fta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class CategoriaVeiculoServices {
	
	public static ResultSetMap getSyncData() {
		return getSyncData(null);
	}
	
	public static ResultSetMap getSyncData(Connection connect) {
		
		boolean isConnectionNull = connect == null;
		
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			ResultSetMap rsm = getAll(connect);
			return toStrCategorias(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}

	public static Result save(CategoriaVeiculo categoriaVeiculo){
		return save(categoriaVeiculo, null);
	}

	public static Result save(CategoriaVeiculo categoriaVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(categoriaVeiculo==null)
				return new Result(-1, "Erro ao salvar. CategoriaVeiculo é nulo");

			int retorno;
			if(categoriaVeiculo.getCdCategoria()==0){
				retorno = CategoriaVeiculoDAO.insert(categoriaVeiculo, connect);
				categoriaVeiculo.setCdCategoria(retorno);
			}
			else {
				retorno = CategoriaVeiculoDAO.update(categoriaVeiculo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CATEGORIAVEICULO", categoriaVeiculo);
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
	public static Result remove(int cdCategoria){
		return remove(cdCategoria, false, null);
	}
	public static Result remove(int cdCategoria, boolean cascade){
		return remove(cdCategoria, cascade, null);
	}
	public static Result remove(int cdCategoria, boolean cascade, Connection connect){
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
			retorno = CategoriaVeiculoDAO.delete(cdCategoria, connect);
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
			if(!Util.isStrBaseAntiga()) {
				pstmt = connect.prepareStatement("SELECT * FROM fta_categoria_veiculo");
				return new ResultSetMap(pstmt.executeQuery());
			} else {
				pstmt = connect.prepareStatement("SELECT *, cod_categoria as cd_categoria FROM categoria_veiculo");
				return new ResultSetMap(pstmt.executeQuery());
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaVeiculoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaVeiculoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}


	public static CategoriaVeiculo get(int cdCategoria) {
		return get(cdCategoria, null);
	}

	public static CategoriaVeiculo get(int cdCategoria, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {

			if(!Util.isStrBaseAntiga()) {
				pstmt = connect.prepareStatement("SELECT * FROM fta_categoria_veiculo WHERE cd_categoria=?");
				pstmt.setInt(1, cdCategoria);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return new CategoriaVeiculo(rs.getInt("cd_categoria"),
							rs.getString("nm_categoria"));
				}
			} else {
				pstmt = connect.prepareStatement("SELECT * FROM categoria_veiculo WHERE cod_categoria=?");
				pstmt.setInt(1, cdCategoria);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return new CategoriaVeiculo(rs.getInt("cod_categoria"),
							rs.getString("nm_categoria"));
				}
			}
			return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaVeiculoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaVeiculoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CategoriaVeiculo getByNome(String nmCategoria) {
		return getByNome(nmCategoria, null);
	}

	public static CategoriaVeiculo getByNome(String nmCategoria, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_categoria_veiculo WHERE nm_categoria LIKE ?"); 
			
			if(Util.isStrBaseAntiga()) {
				pstmt = connect.prepareStatement("SELECT * FROM categoria_veiculo WHERE nm_categoria LIKE ?");
			}
			
			pstmt.setString(1, "%"+nmCategoria+"%");
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(Util.isStrBaseAntiga()) {
					return new CategoriaVeiculo(rs.getInt("cod_categoria"),
							rs.getString("nm_categoria"));
				} else {
					return new CategoriaVeiculo(rs.getInt("cd_categoria"),
							rs.getString("nm_categoria"));
				}
				
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaVeiculoServices.getByNome: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaVeiculoServices.getByNome: " + e);
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
		int qtRegistros = 0;
		
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
				qtRegistros = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				i--;
			}
		}

		return Search.find("SELECT * FROM fta_categoria_veiculo", (qtRegistros > 0? " LIMIT " + qtRegistros : ""),
				criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	private static ResultSetMap toStrCategorias(ResultSetMap rsm) {
		
		if(Util.isStrBaseAntiga()) {
			
			ResultSetMap rsmOut = new ResultSetMap();
			
			while (rsm.next()) {
				
				CategoriaVeiculo catFta = new CategoriaVeiculo(
					rsm.getInt("cd_categoria"),
					rsm.getString("nm_categoria")
				);
				
				com.tivic.manager.str.CategoriaVeiculo conv = toStrCategoriaVeiculo(catFta);
				
				HashMap<String, Object> hash = new HashMap<String, Object>();
				
				hash.put("cd_categoria", conv.getCdCategoria());
				hash.put("nm_categoria", conv.getNmCategoria());
				
				rsmOut.addRegister(hash);
				
			}
			
			return rsmOut;
			
		} else {
			
			return rsm;
			
		}
		
	}
	
	private static com.tivic.manager.str.CategoriaVeiculo toStrCategoriaVeiculo(CategoriaVeiculo categoriaVeiculo) {
		return new com.tivic.manager.str.CategoriaVeiculo(
				categoriaVeiculo.getCdCategoria(),
				categoriaVeiculo.getNmCategoria()
				); 
	}

}
