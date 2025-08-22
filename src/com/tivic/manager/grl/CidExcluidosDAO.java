package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class CidExcluidosDAO{

	public static int insert(CidExcluidos objeto) {
		return insert(objeto, null);
	}

	public static int insert(CidExcluidos objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_cid_excluido");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_cid");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdCid()));
			int code = Conexao.getSequenceCode("grl_cid_excluidos", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCidExcluido(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_cid_excluidos (cd_cid_excluido,"+
			                                  "cd_cid,"+
			                                  "id_cid_excluido) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdCid()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCid());
			pstmt.setString(3,objeto.getIdCidExcluido());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidExcluidosDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CidExcluidosDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CidExcluidos objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(CidExcluidos objeto, int cdCidExcluidoOld, int cdCidOld) {
		return update(objeto, cdCidExcluidoOld, cdCidOld, null);
	}

	public static int update(CidExcluidos objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(CidExcluidos objeto, int cdCidExcluidoOld, int cdCidOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_cid_excluidos SET cd_cid_excluido=?,"+
												      		   "cd_cid=?,"+
												      		   "id_cid_excluido=? WHERE cd_cid_excluido=? AND cd_cid=?");
			pstmt.setInt(1,objeto.getCdCidExcluido());
			pstmt.setInt(2,objeto.getCdCid());
			pstmt.setString(3,objeto.getIdCidExcluido());
			pstmt.setInt(4, cdCidExcluidoOld!=0 ? cdCidExcluidoOld : objeto.getCdCidExcluido());
			pstmt.setInt(5, cdCidOld!=0 ? cdCidOld : objeto.getCdCid());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidExcluidosDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CidExcluidosDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCidExcluido, int cdCid) {
		return delete(cdCidExcluido, cdCid, null);
	}

	public static int delete(int cdCidExcluido, int cdCid, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_cid_excluidos WHERE cd_cid_excluido=? AND cd_cid=?");
			pstmt.setInt(1, cdCidExcluido);
			pstmt.setInt(2, cdCid);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidExcluidosDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CidExcluidosDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CidExcluidos get(int cdCidExcluido, int cdCid) {
		return get(cdCidExcluido, cdCid, null);
	}

	public static CidExcluidos get(int cdCidExcluido, int cdCid, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_cid_excluidos WHERE cd_cid_excluido=? AND cd_cid=?");
			pstmt.setInt(1, cdCidExcluido);
			pstmt.setInt(2, cdCid);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CidExcluidos(rs.getInt("cd_cid_excluido"),
						rs.getInt("cd_cid"),
						rs.getString("id_cid_excluido"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidExcluidosDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidExcluidosDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_cid_excluidos");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidExcluidosDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidExcluidosDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CidExcluidos> getList() {
		return getList(null);
	}

	public static ArrayList<CidExcluidos> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CidExcluidos> list = new ArrayList<CidExcluidos>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CidExcluidos obj = CidExcluidosDAO.get(rsm.getInt("cd_cid_excluido"), rsm.getInt("cd_cid"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidExcluidosDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_cid_excluidos", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}