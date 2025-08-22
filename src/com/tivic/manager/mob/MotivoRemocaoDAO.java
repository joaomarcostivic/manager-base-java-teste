package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class MotivoRemocaoDAO{

	public static int insert(MotivoRemocao objeto) {
		return insert(objeto, null);
	}

	public static int insert(MotivoRemocao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_motivo_remocao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMotivoRemocao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_motivo_remocao (cd_motivo_remocao,"+
			                                  "nm_motivo_remocao,"+
			                                  "id_motivo_remocao) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmMotivoRemocao());
			pstmt.setString(3,objeto.getIdMotivoRemocao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoRemocaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoRemocaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MotivoRemocao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(MotivoRemocao objeto, int cdMotivoRemocaoOld) {
		return update(objeto, cdMotivoRemocaoOld, null);
	}

	public static int update(MotivoRemocao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(MotivoRemocao objeto, int cdMotivoRemocaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_motivo_remocao SET cd_motivo_remocao=?,"+
												      		   "nm_motivo_remocao=?,"+
												      		   "id_motivo_remocao=? WHERE cd_motivo_remocao=?");
			pstmt.setInt(1,objeto.getCdMotivoRemocao());
			pstmt.setString(2,objeto.getNmMotivoRemocao());
			pstmt.setString(3,objeto.getIdMotivoRemocao());
			pstmt.setInt(4, cdMotivoRemocaoOld!=0 ? cdMotivoRemocaoOld : objeto.getCdMotivoRemocao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoRemocaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoRemocaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMotivoRemocao) {
		return delete(cdMotivoRemocao, null);
	}

	public static int delete(int cdMotivoRemocao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_motivo_remocao WHERE cd_motivo_remocao=?");
			pstmt.setInt(1, cdMotivoRemocao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoRemocaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoRemocaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MotivoRemocao get(int cdMotivoRemocao) {
		return get(cdMotivoRemocao, null);
	}

	public static MotivoRemocao get(int cdMotivoRemocao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_motivo_remocao WHERE cd_motivo_remocao=?");
			pstmt.setInt(1, cdMotivoRemocao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MotivoRemocao(rs.getInt("cd_motivo_remocao"),
						rs.getString("nm_motivo_remocao"),
						rs.getString("id_motivo_remocao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoRemocaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoRemocaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_motivo_remocao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoRemocaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoRemocaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<MotivoRemocao> getList() {
		return getList(null);
	}

	public static ArrayList<MotivoRemocao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<MotivoRemocao> list = new ArrayList<MotivoRemocao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				MotivoRemocao obj = MotivoRemocaoDAO.get(rsm.getInt("cd_motivo_remocao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoRemocaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_motivo_remocao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}