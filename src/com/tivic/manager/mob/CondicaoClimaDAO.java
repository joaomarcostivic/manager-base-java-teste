package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CondicaoClimaDAO{

	public static int insert(CondicaoClima objeto) {
		return insert(objeto, null);
	}

	public static int insert(CondicaoClima objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_condicao_clima", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCondicaoClima(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_condicao_clima (cd_condicao_clima,"+
			                                  "nm_condicao_clima,"+
			                                  "id_condicao_clima) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmCondicaoClima());
			pstmt.setString(3,objeto.getIdCondicaoClima());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CondicaoClimaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoClimaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CondicaoClima objeto) {
		return update(objeto, 0, null);
	}

	public static int update(CondicaoClima objeto, int cdCondicaoClimaOld) {
		return update(objeto, cdCondicaoClimaOld, null);
	}

	public static int update(CondicaoClima objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(CondicaoClima objeto, int cdCondicaoClimaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_condicao_clima SET cd_condicao_clima=?,"+
												      		   "nm_condicao_clima=?,"+
												      		   "id_condicao_clima=? WHERE cd_condicao_clima=?");
			pstmt.setInt(1,objeto.getCdCondicaoClima());
			pstmt.setString(2,objeto.getNmCondicaoClima());
			pstmt.setString(3,objeto.getIdCondicaoClima());
			pstmt.setInt(4, cdCondicaoClimaOld!=0 ? cdCondicaoClimaOld : objeto.getCdCondicaoClima());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CondicaoClimaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoClimaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCondicaoClima) {
		return delete(cdCondicaoClima, null);
	}

	public static int delete(int cdCondicaoClima, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_condicao_clima WHERE cd_condicao_clima=?");
			pstmt.setInt(1, cdCondicaoClima);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CondicaoClimaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoClimaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CondicaoClima get(int cdCondicaoClima) {
		return get(cdCondicaoClima, null);
	}

	public static CondicaoClima get(int cdCondicaoClima, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_condicao_clima WHERE cd_condicao_clima=?");
			pstmt.setInt(1, cdCondicaoClima);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CondicaoClima(rs.getInt("cd_condicao_clima"),
						rs.getString("nm_condicao_clima"),
						rs.getString("id_condicao_clima"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CondicaoClimaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoClimaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_condicao_clima");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CondicaoClimaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoClimaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CondicaoClima> getList() {
		return getList(null);
	}

	public static ArrayList<CondicaoClima> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CondicaoClima> list = new ArrayList<CondicaoClima>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CondicaoClima obj = CondicaoClimaDAO.get(rsm.getInt("cd_condicao_clima"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoClimaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_condicao_clima", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}