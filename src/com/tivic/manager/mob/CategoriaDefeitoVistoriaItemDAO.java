package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CategoriaDefeitoVistoriaItemDAO{

	public static int insert(CategoriaDefeitoVistoriaItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(CategoriaDefeitoVistoriaItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_categoria_defeito_vistoria_item", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCategoriaDefeitoVistoriaItem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_categoria_defeito_vistoria_item (cd_categoria_defeito_vistoria_item,"+
			                                  "nm_categoria_defeito,"+
			                                  "cd_categoria_superior) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmCategoriaDefeito());
			pstmt.setInt(3,objeto.getCdCategoriaSuperior());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaDefeitoVistoriaItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaDefeitoVistoriaItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CategoriaDefeitoVistoriaItem objeto) {
		return update(objeto, 0, null);
	}

	public static int update(CategoriaDefeitoVistoriaItem objeto, int cdCategoriaDefeitoVistoriaItemOld) {
		return update(objeto, cdCategoriaDefeitoVistoriaItemOld, null);
	}

	public static int update(CategoriaDefeitoVistoriaItem objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(CategoriaDefeitoVistoriaItem objeto, int cdCategoriaDefeitoVistoriaItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_categoria_defeito_vistoria_item SET cd_categoria_defeito_vistoria_item=?,"+
												      		   "nm_categoria_defeito=?,"+
												      		   "cd_categoria_superior=? WHERE cd_categoria_defeito_vistoria_item=?");
			pstmt.setInt(1,objeto.getCdCategoriaDefeitoVistoriaItem());
			pstmt.setString(2,objeto.getNmCategoriaDefeito());
			pstmt.setInt(3,objeto.getCdCategoriaSuperior());
			pstmt.setInt(4, cdCategoriaDefeitoVistoriaItemOld!=0 ? cdCategoriaDefeitoVistoriaItemOld : objeto.getCdCategoriaDefeitoVistoriaItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaDefeitoVistoriaItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaDefeitoVistoriaItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCategoriaDefeitoVistoriaItem) {
		return delete(cdCategoriaDefeitoVistoriaItem, null);
	}

	public static int delete(int cdCategoriaDefeitoVistoriaItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_categoria_defeito_vistoria_item WHERE cd_categoria_defeito_vistoria_item=?");
			pstmt.setInt(1, cdCategoriaDefeitoVistoriaItem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaDefeitoVistoriaItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaDefeitoVistoriaItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CategoriaDefeitoVistoriaItem get(int cdCategoriaDefeitoVistoriaItem) {
		return get(cdCategoriaDefeitoVistoriaItem, null);
	}

	public static CategoriaDefeitoVistoriaItem get(int cdCategoriaDefeitoVistoriaItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT a.*, b.nm_categoria_defeito as nm_categoria_defeito_superior FROM mob_categoria_defeito_vistoria_item a "+
											" LEFT OUTER JOIN mob_categoria_defeito_vistoria_item b "+
											"			 ON ( a.cd_categoria_superior = b.cd_categoria_defeito_vistoria_item ) "+
											" WHERE cd_categoria_defeito_vistoria_item=?");
			pstmt.setInt(1, cdCategoriaDefeitoVistoriaItem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CategoriaDefeitoVistoriaItem(rs.getInt("cd_categoria_defeito_vistoria_item"),
						rs.getString("nm_categoria_defeito"),
						rs.getInt("cd_categoria_superior"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaDefeitoVistoriaItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaDefeitoVistoriaItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_categoria_defeito_vistoria_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaDefeitoVistoriaItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaDefeitoVistoriaItemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CategoriaDefeitoVistoriaItem> getList() {
		return getList(null);
	}

	public static ArrayList<CategoriaDefeitoVistoriaItem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CategoriaDefeitoVistoriaItem> list = new ArrayList<CategoriaDefeitoVistoriaItem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CategoriaDefeitoVistoriaItem obj = CategoriaDefeitoVistoriaItemDAO.get(rsm.getInt("cd_categoria_defeito_vistoria_item"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaDefeitoVistoriaItemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_categoria_defeito_vistoria_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
