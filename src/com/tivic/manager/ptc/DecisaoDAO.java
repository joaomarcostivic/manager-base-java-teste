package com.tivic.manager.ptc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class DecisaoDAO{

	public static int insert(Decisao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Decisao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_decisao");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_fluxo");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdFluxo()));
			int code = Conexao.getSequenceCode("ptc_decisao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDecisao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_decisao (cd_decisao,"+
			                                  "nm_decisao,"+
			                                  "cd_fluxo) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmDecisao());
			if(objeto.getCdFluxo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdFluxo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DecisaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DecisaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Decisao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Decisao objeto, int cdDecisaoOld, int cdFluxoOld) {
		return update(objeto, cdDecisaoOld, cdFluxoOld, null);
	}

	public static int update(Decisao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Decisao objeto, int cdDecisaoOld, int cdFluxoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_decisao SET cd_decisao=?,"+
												      		   "nm_decisao=?,"+
												      		   "cd_fluxo=? WHERE cd_decisao=? AND cd_fluxo=?");
			pstmt.setInt(1,objeto.getCdDecisao());
			pstmt.setString(2,objeto.getNmDecisao());
			pstmt.setInt(3,objeto.getCdFluxo());
			pstmt.setInt(4, cdDecisaoOld!=0 ? cdDecisaoOld : objeto.getCdDecisao());
			pstmt.setInt(5, cdFluxoOld!=0 ? cdFluxoOld : objeto.getCdFluxo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DecisaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DecisaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDecisao, int cdFluxo) {
		return delete(cdDecisao, cdFluxo, null);
	}

	public static int delete(int cdDecisao, int cdFluxo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_decisao WHERE cd_decisao=? AND cd_fluxo=?");
			pstmt.setInt(1, cdDecisao);
			pstmt.setInt(2, cdFluxo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DecisaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DecisaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Decisao get(int cdDecisao, int cdFluxo) {
		return get(cdDecisao, cdFluxo, null);
	}

	public static Decisao get(int cdDecisao, int cdFluxo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_decisao WHERE cd_decisao=? AND cd_fluxo=?");
			pstmt.setInt(1, cdDecisao);
			pstmt.setInt(2, cdFluxo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Decisao(rs.getInt("cd_decisao"),
						rs.getString("nm_decisao"),
						rs.getInt("cd_fluxo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DecisaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DecisaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_decisao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DecisaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DecisaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Decisao> getList() {
		return getList(null);
	}

	public static ArrayList<Decisao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Decisao> list = new ArrayList<Decisao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Decisao obj = DecisaoDAO.get(rsm.getInt("cd_decisao"), rsm.getInt("cd_fluxo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DecisaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ptc_decisao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
