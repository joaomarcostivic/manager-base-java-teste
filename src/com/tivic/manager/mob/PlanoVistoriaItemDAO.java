package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class PlanoVistoriaItemDAO{

	public static int insert(PlanoVistoriaItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(PlanoVistoriaItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
//			int code = Conexao.getSequenceCode("mob_plano_vistoria_item", connect);
			if (objeto.getCdPlanoVistoria() <= 0 || objeto.getCdVistoriaItem() <=0 ) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
//			objeto.setCdPlanoVistoria(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_plano_vistoria_item (cd_plano_vistoria,"+
			                                  "cd_vistoria_item,"+
			                                  "nr_ordem_grupo,"+
			                                  "nr_ordem_item) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdPlanoVistoria());
			pstmt.setInt(2, objeto.getCdVistoriaItem());
			pstmt.setInt(3,objeto.getNrOrdemGrupo());
			pstmt.setInt(4,objeto.getNrOrdemItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PlanoVistoriaItem objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(PlanoVistoriaItem objeto, int cdPlanoVistoriaOld, int cdVistoriaItemOld) {
		return update(objeto, cdPlanoVistoriaOld, cdVistoriaItemOld, null);
	}

	public static int update(PlanoVistoriaItem objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(PlanoVistoriaItem objeto, int cdPlanoVistoriaOld, int cdVistoriaItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_plano_vistoria_item SET cd_plano_vistoria=?,"+
												      		   "cd_vistoria_item=?,"+
												      		   "nr_ordem_grupo=?,"+
												      		   "nr_ordem_item=? WHERE cd_plano_vistoria=? AND cd_vistoria_item=?");
			pstmt.setInt(1,objeto.getCdPlanoVistoria());
			pstmt.setInt(2,objeto.getCdVistoriaItem());
			pstmt.setInt(3,objeto.getNrOrdemGrupo());
			pstmt.setInt(4,objeto.getNrOrdemItem());
			pstmt.setInt(5, cdPlanoVistoriaOld!=0 ? cdPlanoVistoriaOld : objeto.getCdPlanoVistoria());
			pstmt.setInt(6, cdVistoriaItemOld!=0 ? cdVistoriaItemOld : objeto.getCdVistoriaItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPlanoVistoria, int cdVistoriaItem) {
		return delete(cdPlanoVistoria, cdVistoriaItem, null);
	}

	public static int delete(int cdPlanoVistoria, int cdVistoriaItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_plano_vistoria_item WHERE cd_plano_vistoria=? AND cd_vistoria_item=?");
			pstmt.setInt(1, cdPlanoVistoria);
			pstmt.setInt(2, cdVistoriaItem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PlanoVistoriaItem get(int cdPlanoVistoria, int cdVistoriaItem) {
		return get(cdPlanoVistoria, cdVistoriaItem, null);
	}

	public static PlanoVistoriaItem get(int cdPlanoVistoria, int cdVistoriaItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_plano_vistoria_item WHERE cd_plano_vistoria=? AND cd_vistoria_item=?");
			pstmt.setInt(1, cdPlanoVistoria);
			pstmt.setInt(2, cdVistoriaItem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PlanoVistoriaItem(rs.getInt("cd_plano_vistoria"),
						rs.getInt("cd_vistoria_item"),
						rs.getInt("nr_ordem_grupo"),
						rs.getInt("nr_ordem_item"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_plano_vistoria_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PlanoVistoriaItem> getList() {
		return getList(null);
	}

	public static ArrayList<PlanoVistoriaItem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PlanoVistoriaItem> list = new ArrayList<PlanoVistoriaItem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PlanoVistoriaItem obj = PlanoVistoriaItemDAO.get(rsm.getInt("cd_plano_vistoria"), rsm.getInt("cd_vistoria_item"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_plano_vistoria_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
