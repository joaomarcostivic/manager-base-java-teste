package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class OutroInteressadoDAO{

	public static int insert(OutroInteressado objeto) {
		return insert(objeto, null);
	}

	public static int insert(OutroInteressado objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "CD_OUTRO_INTERESSADO");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "CD_PROCESSO");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdProcesso()));
			int code = Conexao.getSequenceCode("PRC_OUTRO_INTERESSADO", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOutroInteressado(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PRC_OUTRO_INTERESSADO (CD_OUTRO_INTERESSADO,"+
			                                  "CD_PROCESSO,"+
			                                  "NM_OUTRO_INTERESSADO,"+
			                                  "NM_QUALIFICACAO) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProcesso());
			pstmt.setString(3,objeto.getNmOutroInteressado());
			pstmt.setString(4,objeto.getNmQualificacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OutroInteressadoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OutroInteressadoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OutroInteressado objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(OutroInteressado objeto, int cdOutroInteressadoOld, int cdProcessoOld) {
		return update(objeto, cdOutroInteressadoOld, cdProcessoOld, null);
	}

	public static int update(OutroInteressado objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(OutroInteressado objeto, int cdOutroInteressadoOld, int cdProcessoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_OUTRO_INTERESSADO SET CD_OUTRO_INTERESSADO=?,"+
												      		   "CD_PROCESSO=?,"+
												      		   "NM_OUTRO_INTERESSADO=?,"+
												      		   "NM_QUALIFICACAO=? WHERE CD_OUTRO_INTERESSADO=? AND CD_PROCESSO=?");
			pstmt.setInt(1,objeto.getCdOutroInteressado());
			pstmt.setInt(2,objeto.getCdProcesso());
			pstmt.setString(3,objeto.getNmOutroInteressado());
			pstmt.setString(4,objeto.getNmQualificacao());
			pstmt.setInt(5, cdOutroInteressadoOld!=0 ? cdOutroInteressadoOld : objeto.getCdOutroInteressado());
			pstmt.setInt(6, cdProcessoOld!=0 ? cdProcessoOld : objeto.getCdProcesso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OutroInteressadoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OutroInteressadoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOutroInteressado, int cdProcesso) {
		return delete(cdOutroInteressado, cdProcesso, null);
	}

	public static int delete(int cdOutroInteressado, int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PRC_OUTRO_INTERESSADO WHERE CD_OUTRO_INTERESSADO=? AND CD_PROCESSO=?");
			pstmt.setInt(1, cdOutroInteressado);
			pstmt.setInt(2, cdProcesso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OutroInteressadoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OutroInteressadoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OutroInteressado get(int cdOutroInteressado, int cdProcesso) {
		return get(cdOutroInteressado, cdProcesso, null);
	}

	public static OutroInteressado get(int cdOutroInteressado, int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_OUTRO_INTERESSADO WHERE CD_OUTRO_INTERESSADO=? AND CD_PROCESSO=?");
			pstmt.setInt(1, cdOutroInteressado);
			pstmt.setInt(2, cdProcesso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OutroInteressado(rs.getInt("CD_OUTRO_INTERESSADO"),
						rs.getInt("CD_PROCESSO"),
						rs.getString("NM_OUTRO_INTERESSADO"),
						rs.getString("NM_QUALIFICACAO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OutroInteressadoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OutroInteressadoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_OUTRO_INTERESSADO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OutroInteressadoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OutroInteressadoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OutroInteressado> getList() {
		return getList(null);
	}

	public static ArrayList<OutroInteressado> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OutroInteressado> list = new ArrayList<OutroInteressado>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OutroInteressado obj = OutroInteressadoDAO.get(rsm.getInt("CD_OUTRO_INTERESSADO"), rsm.getInt("CD_PROCESSO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OutroInteressadoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM PRC_OUTRO_INTERESSADO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
