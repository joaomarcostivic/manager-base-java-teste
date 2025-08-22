package com.tivic.manager.bdv;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class DebitoDAO{

	public static int insert(Debito objeto) {
		return insert(objeto, null);
	}

	public static int insert(Debito objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_debito");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_veiculo");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdVeiculo()));
			int code = Conexao.getSequenceCode("bdv_debito", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDebito(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO bdv_debito (cd_debito,"+
			                                  "cd_veiculo,"+
			                                  "txt_debito,"+
			                                  "vl_ipva,"+
			                                  "vl_licenciamento,"+
			                                  "vl_multa,"+
			                                  "vl_dpvat,"+
			                                  "vl_infracao,"+
			                                  "dt_debito) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdVeiculo());
			pstmt.setString(3,objeto.getTxtDebito());
			pstmt.setDouble(4,objeto.getVlIpva());
			pstmt.setDouble(5,objeto.getVlLicenciamento());
			pstmt.setDouble(6,objeto.getVlMulta());
			pstmt.setDouble(7,objeto.getVlDpvat());
			pstmt.setDouble(8,objeto.getVlInfracao());
			if(objeto.getDtDebito()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtDebito().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DebitoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DebitoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Debito objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Debito objeto, int cdDebitoOld, int cdVeiculoOld) {
		return update(objeto, cdDebitoOld, cdVeiculoOld, null);
	}

	public static int update(Debito objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Debito objeto, int cdDebitoOld, int cdVeiculoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE bdv_debito SET cd_debito=?,"+
												      		   "cd_veiculo=?,"+
												      		   "txt_debito=?,"+
												      		   "vl_ipva=?,"+
												      		   "vl_licenciamento=?,"+
												      		   "vl_multa=?,"+
												      		   "vl_dpvat=?,"+
												      		   "vl_infracao=?,"+
												      		   "dt_debito=? WHERE cd_debito=? AND cd_veiculo=?");
			pstmt.setInt(1,objeto.getCdDebito());
			pstmt.setInt(2,objeto.getCdVeiculo());
			pstmt.setString(3,objeto.getTxtDebito());
			pstmt.setDouble(4,objeto.getVlIpva());
			pstmt.setDouble(5,objeto.getVlLicenciamento());
			pstmt.setDouble(6,objeto.getVlMulta());
			pstmt.setDouble(7,objeto.getVlDpvat());
			pstmt.setDouble(8,objeto.getVlInfracao());
			if(objeto.getDtDebito()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtDebito().getTimeInMillis()));
			pstmt.setInt(10, cdDebitoOld!=0 ? cdDebitoOld : objeto.getCdDebito());
			pstmt.setInt(11, cdVeiculoOld!=0 ? cdVeiculoOld : objeto.getCdVeiculo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DebitoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DebitoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDebito, int cdVeiculo) {
		return delete(cdDebito, cdVeiculo, null);
	}

	public static int delete(int cdDebito, int cdVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM bdv_debito WHERE cd_debito=? AND cd_veiculo=?");
			pstmt.setInt(1, cdDebito);
			pstmt.setInt(2, cdVeiculo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DebitoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DebitoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Debito get(int cdDebito, int cdVeiculo) {
		return get(cdDebito, cdVeiculo, null);
	}

	public static Debito get(int cdDebito, int cdVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM bdv_debito WHERE cd_debito=? AND cd_veiculo=?");
			pstmt.setInt(1, cdDebito);
			pstmt.setInt(2, cdVeiculo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Debito(rs.getInt("cd_debito"),
						rs.getInt("cd_veiculo"),
						rs.getString("txt_debito"),
						rs.getDouble("vl_ipva"),
						rs.getDouble("vl_licenciamento"),
						rs.getDouble("vl_multa"),
						rs.getDouble("vl_dpvat"),
						rs.getDouble("vl_infracao"),
						(rs.getTimestamp("dt_debito")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_debito").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DebitoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DebitoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM bdv_debito");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DebitoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DebitoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Debito> getList() {
		return getList(null);
	}

	public static ArrayList<Debito> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Debito> list = new ArrayList<Debito>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Debito obj = DebitoDAO.get(rsm.getInt("cd_debito"), rsm.getInt("cd_veiculo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DebitoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM bdv_debito", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
