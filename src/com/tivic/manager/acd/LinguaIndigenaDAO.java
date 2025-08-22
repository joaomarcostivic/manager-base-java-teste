package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class LinguaIndigenaDAO{

	public static int insert(LinguaIndigena objeto) {
		return insert(objeto, null);
	}

	public static int insert(LinguaIndigena objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_lingua_indigena", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLinguaIndigena(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_lingua_indigena (cd_lingua_indigena,"+
			                                  "nm_lingua_indigena,"+
			                                  "id_lingua_indigena) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmLinguaIndigena());
			pstmt.setString(3,objeto.getIdLinguaIndigena());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinguaIndigenaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LinguaIndigenaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LinguaIndigena objeto) {
		return update(objeto, 0, null);
	}

	public static int update(LinguaIndigena objeto, int cdLinguaIndigenaOld) {
		return update(objeto, cdLinguaIndigenaOld, null);
	}

	public static int update(LinguaIndigena objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(LinguaIndigena objeto, int cdLinguaIndigenaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_lingua_indigena SET cd_lingua_indigena=?,"+
												      		   "nm_lingua_indigena=?,"+
												      		   "id_lingua_indigena=? WHERE cd_lingua_indigena=?");
			pstmt.setInt(1,objeto.getCdLinguaIndigena());
			pstmt.setString(2,objeto.getNmLinguaIndigena());
			pstmt.setString(3,objeto.getIdLinguaIndigena());
			pstmt.setInt(4, cdLinguaIndigenaOld!=0 ? cdLinguaIndigenaOld : objeto.getCdLinguaIndigena());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinguaIndigenaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LinguaIndigenaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLinguaIndigena) {
		return delete(cdLinguaIndigena, null);
	}

	public static int delete(int cdLinguaIndigena, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_lingua_indigena WHERE cd_lingua_indigena=?");
			pstmt.setInt(1, cdLinguaIndigena);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinguaIndigenaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LinguaIndigenaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LinguaIndigena get(int cdLinguaIndigena) {
		return get(cdLinguaIndigena, null);
	}

	public static LinguaIndigena get(int cdLinguaIndigena, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_lingua_indigena WHERE cd_lingua_indigena=?");
			pstmt.setInt(1, cdLinguaIndigena);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LinguaIndigena(rs.getInt("cd_lingua_indigena"),
						rs.getString("nm_lingua_indigena"),
						rs.getString("id_lingua_indigena"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinguaIndigenaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinguaIndigenaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_lingua_indigena");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinguaIndigenaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinguaIndigenaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<LinguaIndigena> getList() {
		return getList(null);
	}

	public static ArrayList<LinguaIndigena> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<LinguaIndigena> list = new ArrayList<LinguaIndigena>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				LinguaIndigena obj = LinguaIndigenaDAO.get(rsm.getInt("cd_lingua_indigena"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinguaIndigenaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_lingua_indigena", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
