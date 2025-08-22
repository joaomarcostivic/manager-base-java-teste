package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ViaDAO{

	public static int insert(Via objeto) {
		return insert(objeto, null);
	}

	public static int insert(Via objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_via", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdVia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_via (cd_via,"+
			                                  "nm_via,"+
			                                  "id_via,"+
			                                  "tp_localizacao,"+
			                                  "tp_via,"+
			                                  "cd_referencia,"+
			                                  "cd_logradouro,"+
			                                  "cd_orgao) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmVia());
			pstmt.setString(3,objeto.getIdVia());
			pstmt.setInt(4,objeto.getTpLocalizacao());
			pstmt.setInt(5,objeto.getTpVia());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdReferencia());
			if(objeto.getCdLogradouro()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdLogradouro());
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdOrgao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ViaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Via objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Via objeto, int cdViaOld) {
		return update(objeto, cdViaOld, null);
	}

	public static int update(Via objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Via objeto, int cdViaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_via SET cd_via=?,"+
												      		   "nm_via=?,"+
												      		   "id_via=?,"+
												      		   "tp_localizacao=?,"+
												      		   "tp_via=?,"+
												      		   "cd_referencia=?,"+
												      		   "cd_logradouro=?,"+
												      		   "cd_orgao=? WHERE cd_via=?");
			pstmt.setInt(1,objeto.getCdVia());
			pstmt.setString(2,objeto.getNmVia());
			pstmt.setString(3,objeto.getIdVia());
			pstmt.setInt(4,objeto.getTpLocalizacao());
			pstmt.setInt(5,objeto.getTpVia());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdReferencia());
			if(objeto.getCdLogradouro()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdLogradouro());
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdOrgao());
			pstmt.setInt(9, cdViaOld!=0 ? cdViaOld : objeto.getCdVia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ViaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdVia) {
		return delete(cdVia, null);
	}

	public static int delete(int cdVia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_via WHERE cd_via=?");
			pstmt.setInt(1, cdVia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ViaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Via get(int cdVia) {
		return get(cdVia, null);
	}

	public static Via get(int cdVia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_via WHERE cd_via=?");
			pstmt.setInt(1, cdVia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Via(rs.getInt("cd_via"),
						rs.getString("nm_via"),
						rs.getString("id_via"),
						rs.getInt("tp_localizacao"),
						rs.getInt("tp_via"),
						rs.getInt("cd_referencia"),
						rs.getInt("cd_logradouro"),
						rs.getInt("cd_orgao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_via");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Via> getList() {
		return getList(null);
	}

	public static ArrayList<Via> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Via> list = new ArrayList<Via>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Via obj = ViaDAO.get(rsm.getInt("cd_via"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_via", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
