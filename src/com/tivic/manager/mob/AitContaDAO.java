package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class AitContaDAO{

	public static int insert(AitConta objeto) {
		return insert(objeto, null);
	}

	public static int insert(AitConta objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_ait");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdAit()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_ait_conta");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("mob_ait_conta", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAitConta(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_ait_conta (cd_ait,"+
			                                  "cd_ait_conta,"+
			                                  "cd_conta_receber,"+
			                                  "nr_ait,"+
			                                  "vl_pagamento,"+
			                                  "dt_pagamento,"+
			                                  "st_ait_conta) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdAit()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAit());
			pstmt.setInt(2, code);
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContaReceber());
			pstmt.setString(4,objeto.getNrAit());
			pstmt.setDouble(5,objeto.getVlPagamento());
			if(objeto.getDtPagamento()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtPagamento().getTimeInMillis()));
			pstmt.setInt(7,objeto.getStAitConta());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitContaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitContaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AitConta objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AitConta objeto, int cdAitOld, int cdAitContaOld) {
		return update(objeto, cdAitOld, cdAitContaOld, null);
	}

	public static int update(AitConta objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AitConta objeto, int cdAitOld, int cdAitContaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_ait_conta SET cd_ait=?,"+
												      		   "cd_ait_conta=?,"+
												      		   "cd_conta_receber=?,"+
												      		   "nr_ait=?,"+
												      		   "vl_pagamento=?,"+
												      		   "dt_pagamento=?,"+
												      		   "st_ait_conta=? WHERE cd_ait=? AND cd_ait_conta=?");
			pstmt.setInt(1,objeto.getCdAit());
			pstmt.setInt(2,objeto.getCdAitConta());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContaReceber());
			pstmt.setString(4,objeto.getNrAit());
			pstmt.setDouble(5,objeto.getVlPagamento());
			if(objeto.getDtPagamento()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtPagamento().getTimeInMillis()));
			pstmt.setInt(7,objeto.getStAitConta());
			pstmt.setInt(8, cdAitOld!=0 ? cdAitOld : objeto.getCdAit());
			pstmt.setInt(9, cdAitContaOld!=0 ? cdAitContaOld : objeto.getCdAitConta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitContaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitContaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAit, int cdAitConta) {
		return delete(cdAit, cdAitConta, null);
	}

	public static int delete(int cdAit, int cdAitConta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_ait_conta WHERE cd_ait=? AND cd_ait_conta=?");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, cdAitConta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitContaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitContaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitConta get(int cdAit, int cdAitConta) {
		return get(cdAit, cdAitConta, null);
	}

	public static AitConta get(int cdAit, int cdAitConta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_conta WHERE cd_ait=? AND cd_ait_conta=?");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, cdAitConta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AitConta(rs.getInt("cd_ait"),
						rs.getInt("cd_ait_conta"),
						rs.getInt("cd_conta_receber"),
						rs.getString("nr_ait"),
						rs.getDouble("vl_pagamento"),
						(rs.getTimestamp("dt_pagamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_pagamento").getTime()),
						rs.getInt("st_ait_conta"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitContaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitContaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_conta");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitContaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitContaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AitConta> getList() {
		return getList(null);
	}

	public static ArrayList<AitConta> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AitConta> list = new ArrayList<AitConta>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AitConta obj = AitContaDAO.get(rsm.getInt("cd_ait"), rsm.getInt("cd_ait_conta"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitContaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_ait_conta", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
