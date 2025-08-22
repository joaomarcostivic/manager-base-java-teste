package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoRemocaoDAO{

	public static int insert(TipoRemocao objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoRemocao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_tipo_remocao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoRemocao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_tipo_remocao (cd_tipo_remocao,"+
			                                  "nm_tipo_remocao,"+
			                                  "st_tipo_remocao) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoRemocao());
			pstmt.setInt(3,objeto.getStTipoRemocao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRemocaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRemocaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoRemocao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoRemocao objeto, int cdTipoRemocaoOld) {
		return update(objeto, cdTipoRemocaoOld, null);
	}

	public static int update(TipoRemocao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoRemocao objeto, int cdTipoRemocaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_tipo_remocao SET cd_tipo_remocao=?,"+
												      		   "nm_tipo_remocao=?,"+
												      		   "st_tipo_remocao=? WHERE cd_tipo_remocao=?");
			pstmt.setInt(1,objeto.getCdTipoRemocao());
			pstmt.setString(2,objeto.getNmTipoRemocao());
			pstmt.setInt(3,objeto.getStTipoRemocao());
			pstmt.setInt(4, cdTipoRemocaoOld!=0 ? cdTipoRemocaoOld : objeto.getCdTipoRemocao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRemocaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRemocaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoRemocao) {
		return delete(cdTipoRemocao, null);
	}

	public static int delete(int cdTipoRemocao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_tipo_remocao WHERE cd_tipo_remocao=?");
			pstmt.setInt(1, cdTipoRemocao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRemocaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRemocaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoRemocao get(int cdTipoRemocao) {
		return get(cdTipoRemocao, null);
	}

	public static TipoRemocao get(int cdTipoRemocao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_tipo_remocao WHERE cd_tipo_remocao=?");
			pstmt.setInt(1, cdTipoRemocao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoRemocao(rs.getInt("cd_tipo_remocao"),
						rs.getString("nm_tipo_remocao"),
						rs.getInt("st_tipo_remocao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRemocaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRemocaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_tipo_remocao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRemocaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRemocaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoRemocao> getList() {
		return getList(null);
	}

	public static ArrayList<TipoRemocao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoRemocao> list = new ArrayList<TipoRemocao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoRemocao obj = TipoRemocaoDAO.get(rsm.getInt("cd_tipo_remocao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRemocaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_tipo_remocao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}