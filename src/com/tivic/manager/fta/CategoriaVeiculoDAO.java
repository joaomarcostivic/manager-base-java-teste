package com.tivic.manager.fta;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CategoriaVeiculoDAO{

	public static int insert(CategoriaVeiculo objeto) {
		return insert(objeto, null);
	}

	public static int insert(CategoriaVeiculo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fta_categoria_veiculo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			
			if(objeto.getCdCategoria() <= 0)
				objeto.setCdCategoria(code);
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_categoria_veiculo (cd_categoria,"+
			                                  "nm_categoria) VALUES (?, ?)");
			pstmt.setInt(1, objeto.getCdCategoria());
			pstmt.setString(2,objeto.getNmCategoria());
			pstmt.executeUpdate();
			return objeto.getCdCategoria();
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaVeiculoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaVeiculoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CategoriaVeiculo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(CategoriaVeiculo objeto, int cdCategoriaOld) {
		return update(objeto, cdCategoriaOld, null);
	}

	public static int update(CategoriaVeiculo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(CategoriaVeiculo objeto, int cdCategoriaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_categoria_veiculo SET cd_categoria=?,"+
												      		   "nm_categoria=? WHERE cd_categoria=?");
			pstmt.setInt(1,objeto.getCdCategoria());
			pstmt.setString(2,objeto.getNmCategoria());
			pstmt.setInt(3, cdCategoriaOld!=0 ? cdCategoriaOld : objeto.getCdCategoria());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaVeiculoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaVeiculoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCategoria) {
		return delete(cdCategoria, null);
	}

	public static int delete(int cdCategoria, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_categoria_veiculo WHERE cd_categoria=?");
			pstmt.setInt(1, cdCategoria);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaVeiculoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaVeiculoDAO.delete: " +  e);
			return -1;
		}
		finally{
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_categoria_veiculo WHERE cd_categoria=?");
			pstmt.setInt(1, cdCategoria);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CategoriaVeiculo(rs.getInt("cd_categoria"),
						rs.getString("nm_categoria"));
			}
			else{
				return null;
			}
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

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_categoria_veiculo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaVeiculoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaVeiculoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CategoriaVeiculo> getList() {
		return getList(null);
	}

	public static ArrayList<CategoriaVeiculo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CategoriaVeiculo> list = new ArrayList<CategoriaVeiculo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CategoriaVeiculo obj = CategoriaVeiculoDAO.get(rsm.getInt("cd_categoria"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaVeiculoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM fta_categoria_veiculo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
