package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class DestinacaoLixoDAO{

	public static int insert(DestinacaoLixo objeto) {
		return insert(objeto, null);
	}

	public static int insert(DestinacaoLixo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_destinacao_lixo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDestinacaoLixo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_destinacao_lixo (cd_destinacao_lixo,"+
			                                  "nm_destinacao_lixo,"+
			                                  "id_destinacao_lixo) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmDestinacaoLixo());
			pstmt.setString(3,objeto.getIdDestinacaoLixo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DestinacaoLixoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DestinacaoLixoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DestinacaoLixo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(DestinacaoLixo objeto, int cdDestinacaoLixoOld) {
		return update(objeto, cdDestinacaoLixoOld, null);
	}

	public static int update(DestinacaoLixo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(DestinacaoLixo objeto, int cdDestinacaoLixoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_destinacao_lixo SET cd_destinacao_lixo=?,"+
												      		   "nm_destinacao_lixo=?,"+
												      		   "id_destinacao_lixo=? WHERE cd_destinacao_lixo=?");
			pstmt.setInt(1,objeto.getCdDestinacaoLixo());
			pstmt.setString(2,objeto.getNmDestinacaoLixo());
			pstmt.setString(3,objeto.getIdDestinacaoLixo());
			pstmt.setInt(4, cdDestinacaoLixoOld!=0 ? cdDestinacaoLixoOld : objeto.getCdDestinacaoLixo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DestinacaoLixoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DestinacaoLixoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDestinacaoLixo) {
		return delete(cdDestinacaoLixo, null);
	}

	public static int delete(int cdDestinacaoLixo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_destinacao_lixo WHERE cd_destinacao_lixo=?");
			pstmt.setInt(1, cdDestinacaoLixo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DestinacaoLixoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DestinacaoLixoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DestinacaoLixo get(int cdDestinacaoLixo) {
		return get(cdDestinacaoLixo, null);
	}

	public static DestinacaoLixo get(int cdDestinacaoLixo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_destinacao_lixo WHERE cd_destinacao_lixo=?");
			pstmt.setInt(1, cdDestinacaoLixo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DestinacaoLixo(rs.getInt("cd_destinacao_lixo"),
						rs.getString("nm_destinacao_lixo"),
						rs.getString("id_destinacao_lixo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DestinacaoLixoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DestinacaoLixoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_destinacao_lixo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DestinacaoLixoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DestinacaoLixoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<DestinacaoLixo> getList() {
		return getList(null);
	}

	public static ArrayList<DestinacaoLixo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<DestinacaoLixo> list = new ArrayList<DestinacaoLixo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				DestinacaoLixo obj = DestinacaoLixoDAO.get(rsm.getInt("cd_destinacao_lixo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DestinacaoLixoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_destinacao_lixo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
