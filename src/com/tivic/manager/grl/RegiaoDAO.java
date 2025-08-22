package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class RegiaoDAO{

	public static int insert(Regiao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Regiao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_regiao", connect);
			if (code <= 0) {
				if (isConnectionNull && !connect.getAutoCommit())
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRegiao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_regiao (cd_regiao,"+
			                                  "nm_regiao,"+
			                                  "tp_regiao) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmRegiao());
			pstmt.setInt(3,objeto.getTpRegiao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegiaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegiaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Regiao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Regiao objeto, int cdRegiaoOld) {
		return update(objeto, cdRegiaoOld, null);
	}

	public static int update(Regiao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Regiao objeto, int cdRegiaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_regiao SET cd_regiao=?,"+
												      		   "nm_regiao=?,"+
												      		   "tp_regiao=? WHERE cd_regiao=?");
			pstmt.setInt(1,objeto.getCdRegiao());
			pstmt.setString(2,objeto.getNmRegiao());
			pstmt.setInt(3,objeto.getTpRegiao());
			pstmt.setInt(4, cdRegiaoOld!=0 ? cdRegiaoOld : objeto.getCdRegiao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegiaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegiaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRegiao) {
		return delete(cdRegiao, null);
	}

	public static int delete(int cdRegiao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_regiao WHERE cd_regiao=?");
			pstmt.setInt(1, cdRegiao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegiaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegiaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Regiao get(int cdRegiao) {
		return get(cdRegiao, null);
	}

	public static Regiao get(int cdRegiao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_regiao WHERE cd_regiao=?");
			pstmt.setInt(1, cdRegiao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Regiao(rs.getInt("cd_regiao"),
						rs.getString("nm_regiao"),
						rs.getInt("tp_regiao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegiaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegiaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_regiao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegiaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegiaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_regiao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
