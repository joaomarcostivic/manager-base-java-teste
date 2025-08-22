package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class VistoriaItemDAO{

	public static int insert(VistoriaItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(VistoriaItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_vistoria_item", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdVistoriaItem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_vistoria_item (cd_vistoria_item,"+
			                                  "cd_vistoria_item_grupo,"+
			                                  "nm_vistoria_item,"+
			                                  "cd_equipamento) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getCdVistoriaItemGrupo());
			pstmt.setString(3,objeto.getNmVistoriaItem());
			pstmt.setInt(4,objeto.getCdEquipamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(VistoriaItem objeto) {
		return update(objeto, 0, null);
	}

	public static int update(VistoriaItem objeto, int cdVistoriaItemOld) {
		return update(objeto, cdVistoriaItemOld, null);
	}

	public static int update(VistoriaItem objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(VistoriaItem objeto, int cdVistoriaItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_vistoria_item SET cd_vistoria_item=?,"+
												      		   "cd_vistoria_item_grupo=?,"+
												      		   "nm_vistoria_item=?,"+
												      		   "cd_equipamento=? WHERE cd_vistoria_item=?");
			pstmt.setInt(1,objeto.getCdVistoriaItem());
			pstmt.setInt(2,objeto.getCdVistoriaItemGrupo());
			pstmt.setString(3,objeto.getNmVistoriaItem());
			pstmt.setInt(4,objeto.getCdEquipamento());
			pstmt.setInt(5, cdVistoriaItemOld!=0 ? cdVistoriaItemOld : objeto.getCdVistoriaItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdVistoriaItem) {
		return delete(cdVistoriaItem, null);
	}

	public static int delete(int cdVistoriaItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_vistoria_item WHERE cd_vistoria_item=?");
			pstmt.setInt(1, cdVistoriaItem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static VistoriaItem get(int cdVistoriaItem) {
		return get(cdVistoriaItem, null);
	}

	public static VistoriaItem get(int cdVistoriaItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_vistoria_item WHERE cd_vistoria_item=?");
			pstmt.setInt(1, cdVistoriaItem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new VistoriaItem(rs.getInt("cd_vistoria_item"),
						rs.getInt("cd_vistoria_item_grupo"),
						rs.getString("nm_vistoria_item"),
						rs.getInt("cd_equipamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_vistoria_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<VistoriaItem> getList() {
		return getList(null);
	}

	public static ArrayList<VistoriaItem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<VistoriaItem> list = new ArrayList<VistoriaItem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				VistoriaItem obj = VistoriaItemDAO.get(rsm.getInt("cd_vistoria_item"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_vistoria_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
