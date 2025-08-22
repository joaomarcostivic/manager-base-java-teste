package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class MeioRemocaoDAO{

	public static int insert(MeioRemocao objeto) {
		return insert(objeto, null);
	}

	public static int insert(MeioRemocao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_meio_remocao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMeioRemocao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_meio_remocao (cd_meio_remocao,"+
			                                  "nm_meio_remocao) VALUES (?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmMeioRemocao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MeioRemocaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MeioRemocaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MeioRemocao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(MeioRemocao objeto, int cdMeioRemocaoOld) {
		return update(objeto, cdMeioRemocaoOld, null);
	}

	public static int update(MeioRemocao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(MeioRemocao objeto, int cdMeioRemocaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_meio_remocao SET cd_meio_remocao=?,"+
												      		   "nm_meio_remocao=? WHERE cd_meio_remocao=?");
			pstmt.setInt(1,objeto.getCdMeioRemocao());
			pstmt.setString(2,objeto.getNmMeioRemocao());
			pstmt.setInt(3, cdMeioRemocaoOld!=0 ? cdMeioRemocaoOld : objeto.getCdMeioRemocao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MeioRemocaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MeioRemocaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMeioRemocao) {
		return delete(cdMeioRemocao, null);
	}

	public static int delete(int cdMeioRemocao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_meio_remocao WHERE cd_meio_remocao=?");
			pstmt.setInt(1, cdMeioRemocao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MeioRemocaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MeioRemocaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MeioRemocao get(int cdMeioRemocao) {
		return get(cdMeioRemocao, null);
	}

	public static MeioRemocao get(int cdMeioRemocao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_meio_remocao WHERE cd_meio_remocao=?");
			pstmt.setInt(1, cdMeioRemocao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MeioRemocao(rs.getInt("cd_meio_remocao"),
						rs.getString("nm_meio_remocao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MeioRemocaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MeioRemocaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_meio_remocao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MeioRemocaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MeioRemocaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<MeioRemocao> getList() {
		return getList(null);
	}

	public static ArrayList<MeioRemocao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<MeioRemocao> list = new ArrayList<MeioRemocao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				MeioRemocao obj = MeioRemocaoDAO.get(rsm.getInt("cd_meio_remocao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MeioRemocaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_meio_remocao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}