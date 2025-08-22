package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoAcidenteDAO{

	public static int insert(TipoAcidente objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoAcidente objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_tipo_acidente", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoAcidente(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_tipo_acidente (cd_tipo_acidente,"+
			                                  "nm_tipo_acidente,"+
			                                  "id_tipo_acidente,"+
			                                  "ds_tipo_acidente) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoAcidente());
			pstmt.setString(3,objeto.getIdTipoAcidente());
			pstmt.setString(4,objeto.getDsTipoAcidente());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAcidenteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAcidenteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoAcidente objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoAcidente objeto, int cdTipoAcidenteOld) {
		return update(objeto, cdTipoAcidenteOld, null);
	}

	public static int update(TipoAcidente objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoAcidente objeto, int cdTipoAcidenteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_tipo_acidente SET cd_tipo_acidente=?,"+
												      		   "nm_tipo_acidente=?,"+
												      		   "id_tipo_acidente=?,"+
												      		   "ds_tipo_acidente=? WHERE cd_tipo_acidente=?");
			pstmt.setInt(1,objeto.getCdTipoAcidente());
			pstmt.setString(2,objeto.getNmTipoAcidente());
			pstmt.setString(3,objeto.getIdTipoAcidente());
			pstmt.setString(4,objeto.getDsTipoAcidente());
			pstmt.setInt(5, cdTipoAcidenteOld!=0 ? cdTipoAcidenteOld : objeto.getCdTipoAcidente());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAcidenteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAcidenteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoAcidente) {
		return delete(cdTipoAcidente, null);
	}

	public static int delete(int cdTipoAcidente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_tipo_acidente WHERE cd_tipo_acidente=?");
			pstmt.setInt(1, cdTipoAcidente);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAcidenteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAcidenteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoAcidente get(int cdTipoAcidente) {
		return get(cdTipoAcidente, null);
	}

	public static TipoAcidente get(int cdTipoAcidente, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_tipo_acidente WHERE cd_tipo_acidente=?");
			pstmt.setInt(1, cdTipoAcidente);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoAcidente(rs.getInt("cd_tipo_acidente"),
						rs.getString("nm_tipo_acidente"),
						rs.getString("id_tipo_acidente"),
						rs.getString("ds_tipo_acidente"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAcidenteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAcidenteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_tipo_acidente");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAcidenteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAcidenteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoAcidente> getList() {
		return getList(null);
	}

	public static ArrayList<TipoAcidente> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoAcidente> list = new ArrayList<TipoAcidente>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoAcidente obj = TipoAcidenteDAO.get(rsm.getInt("cd_tipo_acidente"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAcidenteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_tipo_acidente", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
