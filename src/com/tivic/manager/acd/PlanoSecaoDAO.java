package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class PlanoSecaoDAO{

	public static int insert(PlanoSecao objeto) {
		return insert(objeto, null);
	}

	public static int insert(PlanoSecao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_plano_secao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdSecao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_plano_secao (cd_secao,"+
			                                  "nm_secao,"+
			                                  "txt_secao,"+
			                                  "id_secao,"+
			                                  "tp_plano) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmSecao());
			pstmt.setString(3,objeto.getTxtSecao());
			pstmt.setString(4,objeto.getIdSecao());
			pstmt.setInt(5,objeto.getTpPlano());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PlanoSecao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(PlanoSecao objeto, int cdSecaoOld) {
		return update(objeto, cdSecaoOld, null);
	}

	public static int update(PlanoSecao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(PlanoSecao objeto, int cdSecaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_plano_secao SET cd_secao=?,"+
												      		   "nm_secao=?,"+
												      		   "txt_secao=?,"+
												      		   "id_secao=?,"+
												      		   "tp_plano=? WHERE cd_secao=?");
			pstmt.setInt(1,objeto.getCdSecao());
			pstmt.setString(2,objeto.getNmSecao());
			pstmt.setString(3,objeto.getTxtSecao());
			pstmt.setString(4,objeto.getIdSecao());
			pstmt.setInt(5,objeto.getTpPlano());
			pstmt.setInt(6, cdSecaoOld!=0 ? cdSecaoOld : objeto.getCdSecao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdSecao) {
		return delete(cdSecao, null);
	}

	public static int delete(int cdSecao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_plano_secao WHERE cd_secao=?");
			pstmt.setInt(1, cdSecao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PlanoSecao get(int cdSecao) {
		return get(cdSecao, null);
	}

	public static PlanoSecao get(int cdSecao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_plano_secao WHERE cd_secao=?");
			pstmt.setInt(1, cdSecao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PlanoSecao(rs.getInt("cd_secao"),
						rs.getString("nm_secao"),
						rs.getString("txt_secao"),
						rs.getString("id_secao"),
						rs.getInt("tp_plano"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_plano_secao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PlanoSecao> getList() {
		return getList(null);
	}

	public static ArrayList<PlanoSecao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PlanoSecao> list = new ArrayList<PlanoSecao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PlanoSecao obj = PlanoSecaoDAO.get(rsm.getInt("cd_secao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_plano_secao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}