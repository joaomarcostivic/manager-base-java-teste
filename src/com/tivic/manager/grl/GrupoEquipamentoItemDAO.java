package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class GrupoEquipamentoItemDAO{

	public static int insert(GrupoEquipamentoItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(GrupoEquipamentoItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_grupo_equipamento_item (cd_grupo,"+
			                                  "cd_equipamento) VALUES (?, ?)");
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdGrupo());
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEquipamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoEquipamentoItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoEquipamentoItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(GrupoEquipamentoItem objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(GrupoEquipamentoItem objeto, int cdGrupoOld, int cdEquipamentoOld) {
		return update(objeto, cdGrupoOld, cdEquipamentoOld, null);
	}

	public static int update(GrupoEquipamentoItem objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(GrupoEquipamentoItem objeto, int cdGrupoOld, int cdEquipamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_grupo_equipamento_item SET cd_grupo=?,"+
												      		   "cd_equipamento=? WHERE cd_grupo=? AND cd_equipamento=?");
			pstmt.setInt(1,objeto.getCdGrupo());
			pstmt.setInt(2,objeto.getCdEquipamento());
			pstmt.setInt(3, cdGrupoOld!=0 ? cdGrupoOld : objeto.getCdGrupo());
			pstmt.setInt(4, cdEquipamentoOld!=0 ? cdEquipamentoOld : objeto.getCdEquipamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoEquipamentoItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoEquipamentoItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdGrupo, int cdEquipamento) {
		return delete(cdGrupo, cdEquipamento, null);
	}

	public static int delete(int cdGrupo, int cdEquipamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_grupo_equipamento_item WHERE cd_grupo=? AND cd_equipamento=?");
			pstmt.setInt(1, cdGrupo);
			pstmt.setInt(2, cdEquipamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoEquipamentoItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoEquipamentoItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static GrupoEquipamentoItem get(int cdGrupo, int cdEquipamento) {
		return get(cdGrupo, cdEquipamento, null);
	}

	public static GrupoEquipamentoItem get(int cdGrupo, int cdEquipamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_grupo_equipamento_item WHERE cd_grupo=? AND cd_equipamento=?");
			pstmt.setInt(1, cdGrupo);
			pstmt.setInt(2, cdEquipamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new GrupoEquipamentoItem(rs.getInt("cd_grupo"),
						rs.getInt("cd_equipamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoEquipamentoItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoEquipamentoItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_grupo_equipamento_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoEquipamentoItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoEquipamentoItemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<GrupoEquipamentoItem> getList() {
		return getList(null);
	}

	public static ArrayList<GrupoEquipamentoItem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<GrupoEquipamentoItem> list = new ArrayList<GrupoEquipamentoItem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				GrupoEquipamentoItem obj = GrupoEquipamentoItemDAO.get(rsm.getInt("cd_grupo"), rsm.getInt("cd_equipamento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoEquipamentoItemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_grupo_equipamento_item", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
