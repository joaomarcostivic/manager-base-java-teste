package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class FaixaDAO{

	public static int insert(Faixa objeto) {
		return insert(objeto, null);
	}

	public static int insert(Faixa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_via");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdVia()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_faixa");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("mob_faixa", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFaixa(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_faixa (cd_via,"+
			                                  "cd_faixa,"+
			                                  "nr_faixa,"+
			                                  "tp_sentido) VALUES (?, ?, ?, ?)");
			if(objeto.getCdVia()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdVia());
			pstmt.setInt(2, code);
			pstmt.setInt(3,objeto.getNrFaixa());
			pstmt.setInt(4,objeto.getTpSentido());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FaixaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Faixa objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Faixa objeto, int cdViaOld, int cdFaixaOld) {
		return update(objeto, cdViaOld, cdFaixaOld, null);
	}

	public static int update(Faixa objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Faixa objeto, int cdViaOld, int cdFaixaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_faixa SET cd_via=?,"+
												      		   "cd_faixa=?,"+
												      		   "nr_faixa=?,"+
												      		   "tp_sentido=? WHERE cd_via=? AND cd_faixa=?");
			pstmt.setInt(1,objeto.getCdVia());
			pstmt.setInt(2,objeto.getCdFaixa());
			pstmt.setInt(3,objeto.getNrFaixa());
			pstmt.setInt(4,objeto.getTpSentido());
			pstmt.setInt(5, cdViaOld!=0 ? cdViaOld : objeto.getCdVia());
			pstmt.setInt(6, cdFaixaOld!=0 ? cdFaixaOld : objeto.getCdFaixa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FaixaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdVia, int cdFaixa) {
		return delete(cdVia, cdFaixa, null);
	}

	public static int delete(int cdVia, int cdFaixa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_faixa WHERE cd_via=? AND cd_faixa=?");
			pstmt.setInt(1, cdVia);
			pstmt.setInt(2, cdFaixa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FaixaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Faixa get(int cdVia, int cdFaixa) {
		return get(cdVia, cdFaixa, null);
	}

	public static Faixa get(int cdVia, int cdFaixa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_faixa WHERE cd_via=? AND cd_faixa=?");
			pstmt.setInt(1, cdVia);
			pstmt.setInt(2, cdFaixa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Faixa(rs.getInt("cd_via"),
						rs.getInt("cd_faixa"),
						rs.getInt("nr_faixa"),
						rs.getInt("tp_sentido"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FaixaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_faixa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FaixaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Faixa> getList() {
		return getList(null);
	}

	public static ArrayList<Faixa> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Faixa> list = new ArrayList<Faixa>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Faixa obj = FaixaDAO.get(rsm.getInt("cd_via"), rsm.getInt("cd_faixa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_faixa", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
