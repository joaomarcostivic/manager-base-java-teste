package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class VistoriaPlanoItemDAO{

	public static int insert(VistoriaPlanoItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(VistoriaPlanoItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_vistoria_plano_item", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdVistoriaPlanoItem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_vistoria_plano_item (cd_vistoria_plano_item,"+
			                                  "cd_vistoria_item,"+
			                                  "cd_plano_vistoria,"+
			                                  "cd_vistoria,"+
			                                  "st_item,"+
			                                  "ds_observacao) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getCdVistoriaItem());
			pstmt.setInt(3,objeto.getCdPlanoVistoria());
			pstmt.setInt(4,objeto.getCdVistoria());
			pstmt.setInt(5,objeto.getStItem());
			pstmt.setString(6,objeto.getDsObservacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(VistoriaPlanoItem objeto) {
		return update(objeto, 0, null);
	}

	public static int update(VistoriaPlanoItem objeto, int cdVistoriaPlanoItemOld) {
		return update(objeto, cdVistoriaPlanoItemOld, null);
	}

	public static int update(VistoriaPlanoItem objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(VistoriaPlanoItem objeto, int cdVistoriaPlanoItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_vistoria_plano_item SET cd_vistoria_plano_item=?,"+
												      		   "cd_vistoria_item=?,"+
												      		   "cd_plano_vistoria=?,"+
												      		   "cd_vistoria=?,"+
												      		   "st_item=?,"+
												      		   "ds_observacao=? WHERE cd_vistoria_plano_item=?");
			pstmt.setInt(1,objeto.getCdVistoriaPlanoItem());
			pstmt.setInt(2,objeto.getCdVistoriaItem());
			pstmt.setInt(3,objeto.getCdPlanoVistoria());
			pstmt.setInt(4,objeto.getCdVistoria());
			pstmt.setInt(5,objeto.getStItem());
			pstmt.setString(6,objeto.getDsObservacao());
			pstmt.setInt(7, cdVistoriaPlanoItemOld!=0 ? cdVistoriaPlanoItemOld : objeto.getCdVistoriaPlanoItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdVistoriaPlanoItem) {
		return delete(cdVistoriaPlanoItem, null);
	}

	public static int delete(int cdVistoriaPlanoItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_vistoria_plano_item WHERE cd_vistoria_plano_item=?");
			pstmt.setInt(1, cdVistoriaPlanoItem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static VistoriaPlanoItem get(int cdVistoriaPlanoItem) {
		return get(cdVistoriaPlanoItem, null);
	}

	public static VistoriaPlanoItem get(int cdVistoriaPlanoItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_vistoria_plano_item WHERE cd_vistoria_plano_item=?");
			pstmt.setInt(1, cdVistoriaPlanoItem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new VistoriaPlanoItem(rs.getInt("cd_vistoria_plano_item"),
						rs.getInt("cd_vistoria_item"),
						rs.getInt("cd_plano_vistoria"),
						rs.getInt("cd_vistoria"),
						rs.getInt("st_item"),
						rs.getString("ds_observacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_vistoria_plano_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<VistoriaPlanoItem> getList() {
		return getList(null);
	}

	public static ArrayList<VistoriaPlanoItem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<VistoriaPlanoItem> list = new ArrayList<VistoriaPlanoItem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				VistoriaPlanoItem obj = VistoriaPlanoItemDAO.get(rsm.getInt("cd_vistoria_plano_item"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_vistoria_plano_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
