package com.tivic.manager.bdv;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class RestricaoDAO{

	public static int insert(Restricao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Restricao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_restricao");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_veiculo");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdVeiculo()));
			int code = Conexao.getSequenceCode("bdv_restricao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRestricao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO bdv_restricao (cd_restricao,"+
			                                  "cd_veiculo,"+
			                                  "txt_restricao,"+
			                                  "tp_restricao,"+
			                                  "st_restricao,"+
			                                  "dt_restricao) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdVeiculo());
			pstmt.setString(3,objeto.getTxtRestricao());
			pstmt.setInt(4,objeto.getTpRestricao());
			pstmt.setInt(5,objeto.getStRestricao());
			if(objeto.getDtRestricao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtRestricao().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RestricaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RestricaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Restricao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Restricao objeto, int cdRestricaoOld, int cdVeiculoOld) {
		return update(objeto, cdRestricaoOld, cdVeiculoOld, null);
	}

	public static int update(Restricao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Restricao objeto, int cdRestricaoOld, int cdVeiculoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE bdv_restricao SET cd_restricao=?,"+
												      		   "cd_veiculo=?,"+
												      		   "txt_restricao=?,"+
												      		   "tp_restricao=?,"+
												      		   "st_restricao=?,"+
												      		   "dt_restricao=? WHERE cd_restricao=? AND cd_veiculo=?");
			pstmt.setInt(1,objeto.getCdRestricao());
			pstmt.setInt(2,objeto.getCdVeiculo());
			pstmt.setString(3,objeto.getTxtRestricao());
			pstmt.setInt(4,objeto.getTpRestricao());
			pstmt.setInt(5,objeto.getStRestricao());
			if(objeto.getDtRestricao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtRestricao().getTimeInMillis()));
			pstmt.setInt(7, cdRestricaoOld!=0 ? cdRestricaoOld : objeto.getCdRestricao());
			pstmt.setInt(8, cdVeiculoOld!=0 ? cdVeiculoOld : objeto.getCdVeiculo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RestricaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RestricaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRestricao, int cdVeiculo) {
		return delete(cdRestricao, cdVeiculo, null);
	}

	public static int delete(int cdRestricao, int cdVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM bdv_restricao WHERE cd_restricao=? AND cd_veiculo=?");
			pstmt.setInt(1, cdRestricao);
			pstmt.setInt(2, cdVeiculo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RestricaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RestricaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Restricao get(int cdRestricao, int cdVeiculo) {
		return get(cdRestricao, cdVeiculo, null);
	}

	public static Restricao get(int cdRestricao, int cdVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM bdv_restricao WHERE cd_restricao=? AND cd_veiculo=?");
			pstmt.setInt(1, cdRestricao);
			pstmt.setInt(2, cdVeiculo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Restricao(rs.getInt("cd_restricao"),
						rs.getInt("cd_veiculo"),
						rs.getString("txt_restricao"),
						rs.getInt("tp_restricao"),
						rs.getInt("st_restricao"),
						(rs.getTimestamp("dt_restricao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_restricao").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RestricaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RestricaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM bdv_restricao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RestricaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RestricaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Restricao> getList() {
		return getList(null);
	}

	public static ArrayList<Restricao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Restricao> list = new ArrayList<Restricao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Restricao obj = RestricaoDAO.get(rsm.getInt("cd_restricao"), rsm.getInt("cd_veiculo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RestricaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM bdv_restricao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
