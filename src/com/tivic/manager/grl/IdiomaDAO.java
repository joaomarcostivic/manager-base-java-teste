package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class IdiomaDAO{

	public static int insert(Idioma objeto) {
		return insert(objeto, null);
	}

	public static int insert(Idioma objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_idioma", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdIdioma(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_idioma (cd_idioma,"+
			                                  "nm_idioma,"+
			                                  "id_idioma) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmIdioma());
			pstmt.setString(3,objeto.getIdIdioma());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IdiomaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! IdiomaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Idioma objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Idioma objeto, int cdIdiomaOld) {
		return update(objeto, cdIdiomaOld, null);
	}

	public static int update(Idioma objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Idioma objeto, int cdIdiomaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_idioma SET cd_idioma=?,"+
												      		   "nm_idioma=?,"+
												      		   "id_idioma=? WHERE cd_idioma=?");
			pstmt.setInt(1,objeto.getCdIdioma());
			pstmt.setString(2,objeto.getNmIdioma());
			pstmt.setString(3,objeto.getIdIdioma());
			pstmt.setInt(4, cdIdiomaOld!=0 ? cdIdiomaOld : objeto.getCdIdioma());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IdiomaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! IdiomaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdIdioma) {
		return delete(cdIdioma, null);
	}

	public static int delete(int cdIdioma, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_idioma WHERE cd_idioma=?");
			pstmt.setInt(1, cdIdioma);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IdiomaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! IdiomaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Idioma get(int cdIdioma) {
		return get(cdIdioma, null);
	}

	public static Idioma get(int cdIdioma, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_idioma WHERE cd_idioma=?");
			pstmt.setInt(1, cdIdioma);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Idioma(rs.getInt("cd_idioma"),
						rs.getString("nm_idioma"),
						rs.getString("id_idioma"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IdiomaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IdiomaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_idioma");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IdiomaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IdiomaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Idioma> getList() {
		return getList(null);
	}

	public static ArrayList<Idioma> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Idioma> list = new ArrayList<Idioma>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Idioma obj = IdiomaDAO.get(rsm.getInt("cd_idioma"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IdiomaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_idioma", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}