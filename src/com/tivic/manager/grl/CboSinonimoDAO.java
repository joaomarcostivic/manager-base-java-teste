package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class CboSinonimoDAO{

	public static int insert(CboSinonimo objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(CboSinonimo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_cbo_sinonimo");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_cbo");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdCbo()));
			int code = Conexao.getSequenceCode("grl_cbo_sinonimo", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCboSinonimo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_cbo_sinonimo (cd_cbo_sinonimo,"+
			                                  "cd_cbo,"+
			                                  "nm_cbo_sinonimo,"+
			                                  "id_cbo_sinonimo) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdCbo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCbo());
			pstmt.setString(3,objeto.getNmCboSinonimo());
			pstmt.setString(4,objeto.getIdCboSinonimo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CboSinonimoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CboSinonimoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CboSinonimo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(CboSinonimo objeto, int cdCboSinonimoOld, int cdCboOld) {
		return update(objeto, cdCboSinonimoOld, cdCboOld, null);
	}

	public static int update(CboSinonimo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(CboSinonimo objeto, int cdCboSinonimoOld, int cdCboOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_cbo_sinonimo SET cd_cbo_sinonimo=?,"+
												      		   "cd_cbo=?,"+
												      		   "nm_cbo_sinonimo=?,"+
												      		   "id_cbo_sinonimo=? WHERE cd_cbo_sinonimo=? AND cd_cbo=?");
			pstmt.setInt(1,objeto.getCdCboSinonimo());
			pstmt.setInt(2,objeto.getCdCbo());
			pstmt.setString(3,objeto.getNmCboSinonimo());
			pstmt.setString(4,objeto.getIdCboSinonimo());
			pstmt.setInt(5, cdCboSinonimoOld!=0 ? cdCboSinonimoOld : objeto.getCdCboSinonimo());
			pstmt.setInt(6, cdCboOld!=0 ? cdCboOld : objeto.getCdCbo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CboSinonimoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CboSinonimoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCboSinonimo, int cdCbo) {
		return delete(cdCboSinonimo, cdCbo, null);
	}

	public static int delete(int cdCboSinonimo, int cdCbo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_cbo_sinonimo WHERE cd_cbo_sinonimo=? AND cd_cbo=?");
			pstmt.setInt(1, cdCboSinonimo);
			pstmt.setInt(2, cdCbo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CboSinonimoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CboSinonimoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CboSinonimo get(int cdCboSinonimo, int cdCbo) {
		return get(cdCboSinonimo, cdCbo, null);
	}

	public static CboSinonimo get(int cdCboSinonimo, int cdCbo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_cbo_sinonimo WHERE cd_cbo_sinonimo=? AND cd_cbo=?");
			pstmt.setInt(1, cdCboSinonimo);
			pstmt.setInt(2, cdCbo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CboSinonimo(rs.getInt("cd_cbo_sinonimo"),
						rs.getInt("cd_cbo"),
						rs.getString("nm_cbo_sinonimo"),
						rs.getString("id_cbo_sinonimo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CboSinonimoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CboSinonimoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_cbo_sinonimo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CboSinonimoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CboSinonimoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_cbo_sinonimo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
