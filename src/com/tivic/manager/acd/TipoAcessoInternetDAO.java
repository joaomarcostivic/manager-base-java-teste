package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoAcessoInternetDAO{

	public static int insert(TipoAcessoInternet objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoAcessoInternet objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_tipo_acesso_internet", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoAcessoInternet(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_tipo_acesso_internet (cd_tipo_acesso_internet,"+
			                                  "nm_tipo_acesso_internet,"+
			                                  "id_tipo_acesso_internet,"+
			                                  "st_tipo_acesso_internet) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoAcessoInternet());
			pstmt.setString(3,objeto.getIdTipoAcessoInternet());
			pstmt.setInt(4,objeto.getStTipoAcessoInternet());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAcessoInternetDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAcessoInternetDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoAcessoInternet objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoAcessoInternet objeto, int cdTipoAcessoInternetOld) {
		return update(objeto, cdTipoAcessoInternetOld, null);
	}

	public static int update(TipoAcessoInternet objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoAcessoInternet objeto, int cdTipoAcessoInternetOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_tipo_acesso_internet SET cd_tipo_acesso_internet=?,"+
												      		   "nm_tipo_acesso_internet=?,"+
												      		   "id_tipo_acesso_internet=?,"+
												      		   "st_tipo_acesso_internet=? WHERE cd_tipo_acesso_internet=?");
			pstmt.setInt(1,objeto.getCdTipoAcessoInternet());
			pstmt.setString(2,objeto.getNmTipoAcessoInternet());
			pstmt.setString(3,objeto.getIdTipoAcessoInternet());
			pstmt.setInt(4,objeto.getStTipoAcessoInternet());
			pstmt.setInt(5, cdTipoAcessoInternetOld!=0 ? cdTipoAcessoInternetOld : objeto.getCdTipoAcessoInternet());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAcessoInternetDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAcessoInternetDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoAcessoInternet) {
		return delete(cdTipoAcessoInternet, null);
	}

	public static int delete(int cdTipoAcessoInternet, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_tipo_acesso_internet WHERE cd_tipo_acesso_internet=?");
			pstmt.setInt(1, cdTipoAcessoInternet);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAcessoInternetDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAcessoInternetDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoAcessoInternet get(int cdTipoAcessoInternet) {
		return get(cdTipoAcessoInternet, null);
	}

	public static TipoAcessoInternet get(int cdTipoAcessoInternet, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_acesso_internet WHERE cd_tipo_acesso_internet=?");
			pstmt.setInt(1, cdTipoAcessoInternet);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoAcessoInternet(rs.getInt("cd_tipo_acesso_internet"),
						rs.getString("nm_tipo_acesso_internet"),
						rs.getString("id_tipo_acesso_internet"),
						rs.getInt("st_tipo_acesso_internet"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAcessoInternetDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAcessoInternetDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_acesso_internet");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAcessoInternetDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAcessoInternetDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoAcessoInternet> getList() {
		return getList(null);
	}

	public static ArrayList<TipoAcessoInternet> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoAcessoInternet> list = new ArrayList<TipoAcessoInternet>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoAcessoInternet obj = TipoAcessoInternetDAO.get(rsm.getInt("cd_tipo_acesso_internet"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAcessoInternetDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_acesso_internet", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}