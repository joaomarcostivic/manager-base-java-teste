package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CondicaoViaDAO{

	public static int insert(CondicaoVia objeto) {
		return insert(objeto, null);
	}

	public static int insert(CondicaoVia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_condicao_via", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCondicaoVia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_condicao_via (cd_condicao_via,"+
			                                  "nm_condicao_via,"+
			                                  "id_condicao_via) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmCondicaoVia());
			pstmt.setString(3,objeto.getIdCondicaoVia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CondicaoViaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoViaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CondicaoVia objeto) {
		return update(objeto, 0, null);
	}

	public static int update(CondicaoVia objeto, int cdCondicaoViaOld) {
		return update(objeto, cdCondicaoViaOld, null);
	}

	public static int update(CondicaoVia objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(CondicaoVia objeto, int cdCondicaoViaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_condicao_via SET cd_condicao_via=?,"+
												      		   "nm_condicao_via=?,"+
												      		   "id_condicao_via=? WHERE cd_condicao_via=?");
			pstmt.setInt(1,objeto.getCdCondicaoVia());
			pstmt.setString(2,objeto.getNmCondicaoVia());
			pstmt.setString(3,objeto.getIdCondicaoVia());
			pstmt.setInt(4, cdCondicaoViaOld!=0 ? cdCondicaoViaOld : objeto.getCdCondicaoVia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CondicaoViaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoViaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCondicaoVia) {
		return delete(cdCondicaoVia, null);
	}

	public static int delete(int cdCondicaoVia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_condicao_via WHERE cd_condicao_via=?");
			pstmt.setInt(1, cdCondicaoVia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CondicaoViaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoViaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CondicaoVia get(int cdCondicaoVia) {
		return get(cdCondicaoVia, null);
	}

	public static CondicaoVia get(int cdCondicaoVia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_condicao_via WHERE cd_condicao_via=?");
			pstmt.setInt(1, cdCondicaoVia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CondicaoVia(rs.getInt("cd_condicao_via"),
						rs.getString("nm_condicao_via"),
						rs.getString("id_condicao_via"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CondicaoViaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoViaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_condicao_via");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CondicaoViaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoViaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CondicaoVia> getList() {
		return getList(null);
	}

	public static ArrayList<CondicaoVia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CondicaoVia> list = new ArrayList<CondicaoVia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CondicaoVia obj = CondicaoViaDAO.get(rsm.getInt("cd_condicao_via"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoViaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_condicao_via", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}