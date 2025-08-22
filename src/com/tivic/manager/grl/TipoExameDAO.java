package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoExameDAO{

	public static int insert(TipoExame objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoExame objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_tipo_exame", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoExame(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_tipo_exame (cd_tipo_exame,"+
			                                  "id_tipo_exame,"+
			                                  "nm_tipo_exame,"+
			                                  "st_tipo_exame) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getIdTipoExame());
			pstmt.setString(3,objeto.getNmTipoExame());
			pstmt.setInt(4,objeto.getStTipoExame());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoExameDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoExameDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoExame objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoExame objeto, int cdTipoExameOld) {
		return update(objeto, cdTipoExameOld, null);
	}

	public static int update(TipoExame objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoExame objeto, int cdTipoExameOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_tipo_exame SET cd_tipo_exame=?,"+
												      		   "id_tipo_exame=?,"+
												      		   "nm_tipo_exame=?,"+
												      		   "st_tipo_exame=? WHERE cd_tipo_exame=?");
			pstmt.setInt(1,objeto.getCdTipoExame());
			pstmt.setString(2,objeto.getIdTipoExame());
			pstmt.setString(3,objeto.getNmTipoExame());
			pstmt.setInt(4,objeto.getStTipoExame());
			pstmt.setInt(5, cdTipoExameOld!=0 ? cdTipoExameOld : objeto.getCdTipoExame());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoExameDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoExameDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoExame) {
		return delete(cdTipoExame, null);
	}

	public static int delete(int cdTipoExame, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_tipo_exame WHERE cd_tipo_exame=?");
			pstmt.setInt(1, cdTipoExame);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoExameDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoExameDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoExame get(int cdTipoExame) {
		return get(cdTipoExame, null);
	}

	public static TipoExame get(int cdTipoExame, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_exame WHERE cd_tipo_exame=?");
			pstmt.setInt(1, cdTipoExame);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoExame(rs.getInt("cd_tipo_exame"),
						rs.getString("id_tipo_exame"),
						rs.getString("nm_tipo_exame"),
						rs.getInt("st_tipo_exame"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoExameDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoExameDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_exame");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoExameDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoExameDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoExame> getList() {
		return getList(null);
	}

	public static ArrayList<TipoExame> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoExame> list = new ArrayList<TipoExame>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoExame obj = TipoExameDAO.get(rsm.getInt("cd_tipo_exame"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoExameDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_tipo_exame", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}