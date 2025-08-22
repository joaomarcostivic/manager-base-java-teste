package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class DefeitoVistoriaItemDAO{

	public static int insert(DefeitoVistoriaItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(DefeitoVistoriaItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_defeito_vistoria_item", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDefeitoVistoriaItem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_defeito_vistoria_item (cd_defeito_vistoria_item,"+
			                                  "cd_categoria_defeito_vistoria_item,"+
			                                  "nm_defeito,"+
			                                  "id_defeito) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getCdCategoriaDefeitoVistoriaItem());
			pstmt.setString(3,objeto.getNmDefeito());
			pstmt.setString(4,objeto.getIdDefeito());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DefeitoVistoriaItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DefeitoVistoriaItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DefeitoVistoriaItem objeto) {
		return update(objeto, 0, null);
	}

	public static int update(DefeitoVistoriaItem objeto, int cdDefeitoVistoriaItemOld) {
		return update(objeto, cdDefeitoVistoriaItemOld, null);
	}

	public static int update(DefeitoVistoriaItem objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(DefeitoVistoriaItem objeto, int cdDefeitoVistoriaItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_defeito_vistoria_item SET cd_defeito_vistoria_item=?,"+
												      		   "cd_categoria_defeito_vistoria_item=?,"+
												      		   "nm_defeito=?,"+
												      		   "id_defeito=? WHERE cd_defeito_vistoria_item=?");
			pstmt.setInt(1,objeto.getCdDefeitoVistoriaItem());
			pstmt.setInt(2,objeto.getCdCategoriaDefeitoVistoriaItem());
			pstmt.setString(3,objeto.getNmDefeito());
			pstmt.setString(4,objeto.getIdDefeito());
			pstmt.setInt(5, cdDefeitoVistoriaItemOld!=0 ? cdDefeitoVistoriaItemOld : objeto.getCdDefeitoVistoriaItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DefeitoVistoriaItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DefeitoVistoriaItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDefeitoVistoriaItem) {
		return delete(cdDefeitoVistoriaItem, null);
	}

	public static int delete(int cdDefeitoVistoriaItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_defeito_vistoria_item WHERE cd_defeito_vistoria_item=?");
			pstmt.setInt(1, cdDefeitoVistoriaItem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DefeitoVistoriaItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DefeitoVistoriaItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DefeitoVistoriaItem get(int cdDefeitoVistoriaItem) {
		return get(cdDefeitoVistoriaItem, null);
	}

	public static DefeitoVistoriaItem get(int cdDefeitoVistoriaItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_defeito_vistoria_item WHERE cd_defeito_vistoria_item=?");
			pstmt.setInt(1, cdDefeitoVistoriaItem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DefeitoVistoriaItem(rs.getInt("cd_defeito_vistoria_item"),
						rs.getInt("cd_categoria_defeito_vistoria_item"),
						rs.getString("nm_defeito"),
						rs.getString("id_defeito"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DefeitoVistoriaItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DefeitoVistoriaItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_defeito_vistoria_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DefeitoVistoriaItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DefeitoVistoriaItemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<DefeitoVistoriaItem> getList() {
		return getList(null);
	}

	public static ArrayList<DefeitoVistoriaItem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<DefeitoVistoriaItem> list = new ArrayList<DefeitoVistoriaItem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				DefeitoVistoriaItem obj = DefeitoVistoriaItemDAO.get(rsm.getInt("cd_defeito_vistoria_item"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DefeitoVistoriaItemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_defeito_vistoria_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
