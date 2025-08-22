package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class VistoriaItemGrupoDAO{

	public static int insert(VistoriaItemGrupo objeto) {
		return insert(objeto, null);
	}

	public static int insert(VistoriaItemGrupo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_vistoria_item_grupo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdVistoriaItemGrupo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_vistoria_item_grupo (cd_vistoria_item_grupo,"+
			                                  "nm_grupo) VALUES (?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmGrupo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemGrupoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemGrupoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(VistoriaItemGrupo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(VistoriaItemGrupo objeto, int cdVistoriaItemGrupoOld) {
		return update(objeto, cdVistoriaItemGrupoOld, null);
	}

	public static int update(VistoriaItemGrupo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(VistoriaItemGrupo objeto, int cdVistoriaItemGrupoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_vistoria_item_grupo SET cd_vistoria_item_grupo=?,"+
												      		   "nm_grupo=? WHERE cd_vistoria_item_grupo=?");
			pstmt.setInt(1,objeto.getCdVistoriaItemGrupo());
			pstmt.setString(2,objeto.getNmGrupo());
			pstmt.setInt(3, cdVistoriaItemGrupoOld!=0 ? cdVistoriaItemGrupoOld : objeto.getCdVistoriaItemGrupo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemGrupoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemGrupoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdVistoriaItemGrupo) {
		return delete(cdVistoriaItemGrupo, null);
	}

	public static int delete(int cdVistoriaItemGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_vistoria_item_grupo WHERE cd_vistoria_item_grupo=?");
			pstmt.setInt(1, cdVistoriaItemGrupo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemGrupoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemGrupoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static VistoriaItemGrupo get(int cdVistoriaItemGrupo) {
		return get(cdVistoriaItemGrupo, null);
	}

	public static VistoriaItemGrupo get(int cdVistoriaItemGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_vistoria_item_grupo WHERE cd_vistoria_item_grupo=?");
			pstmt.setInt(1, cdVistoriaItemGrupo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new VistoriaItemGrupo(rs.getInt("cd_vistoria_item_grupo"),
						rs.getString("nm_grupo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemGrupoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemGrupoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_vistoria_item_grupo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemGrupoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemGrupoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<VistoriaItemGrupo> getList() {
		return getList(null);
	}

	public static ArrayList<VistoriaItemGrupo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<VistoriaItemGrupo> list = new ArrayList<VistoriaItemGrupo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				VistoriaItemGrupo obj = VistoriaItemGrupoDAO.get(rsm.getInt("cd_vistoria_item_grupo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemGrupoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_vistoria_item_grupo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
