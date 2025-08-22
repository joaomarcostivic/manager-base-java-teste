package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoCheckupItemDAO{

	public static int insert(TipoCheckupItem objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(TipoCheckupItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_tipo_checkup");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdTipoCheckup()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_item");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("fta_tipo_checkup_item", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdItem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_tipo_checkup_item (cd_tipo_checkup,"+
			                                  "cd_item,"+
			                                  "vl_minimo,"+
			                                  "vl_maximo,"+
			                                  "cd_tipo_componente,"+
			                                  "nm_item,"+
			                                  "txt_observacao) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdTipoCheckup()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTipoCheckup());
			pstmt.setInt(2, code);
			pstmt.setFloat(3,objeto.getVlMinimo());
			pstmt.setFloat(4,objeto.getVlMaximo());
			if(objeto.getCdTipoComponente()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTipoComponente());
			pstmt.setString(6,objeto.getNmItem());
			pstmt.setString(7,objeto.getTxtObservacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoCheckupItem objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TipoCheckupItem objeto, int cdTipoCheckupOld, int cdItemOld) {
		return update(objeto, cdTipoCheckupOld, cdItemOld, null);
	}

	public static int update(TipoCheckupItem objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TipoCheckupItem objeto, int cdTipoCheckupOld, int cdItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_tipo_checkup_item SET cd_tipo_checkup=?,"+
												      		   "cd_item=?,"+
												      		   "vl_minimo=?,"+
												      		   "vl_maximo=?,"+
												      		   "cd_tipo_componente=?,"+
												      		   "nm_item=?,"+
												      		   "txt_observacao=? WHERE cd_tipo_checkup=? AND cd_item=?");
			pstmt.setInt(1,objeto.getCdTipoCheckup());
			pstmt.setInt(2,objeto.getCdItem());
			pstmt.setFloat(3,objeto.getVlMinimo());
			pstmt.setFloat(4,objeto.getVlMaximo());
			if(objeto.getCdTipoComponente()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTipoComponente());
			pstmt.setString(6,objeto.getNmItem());
			pstmt.setString(7,objeto.getTxtObservacao());
			pstmt.setInt(8, cdTipoCheckupOld!=0 ? cdTipoCheckupOld : objeto.getCdTipoCheckup());
			pstmt.setInt(9, cdItemOld!=0 ? cdItemOld : objeto.getCdItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoCheckup, int cdItem) {
		return delete(cdTipoCheckup, cdItem, null);
	}

	public static int delete(int cdTipoCheckup, int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_tipo_checkup_item WHERE cd_tipo_checkup=? AND cd_item=?");
			pstmt.setInt(1, cdTipoCheckup);
			pstmt.setInt(2, cdItem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoCheckupItem get(int cdTipoCheckup, int cdItem) {
		return get(cdTipoCheckup, cdItem, null);
	}

	public static TipoCheckupItem get(int cdTipoCheckup, int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_tipo_checkup_item WHERE cd_tipo_checkup=? AND cd_item=?");
			pstmt.setInt(1, cdTipoCheckup);
			pstmt.setInt(2, cdItem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoCheckupItem(rs.getInt("cd_tipo_checkup"),
						rs.getInt("cd_item"),
						rs.getFloat("vl_minimo"),
						rs.getFloat("vl_maximo"),
						rs.getInt("cd_tipo_componente"),
						rs.getString("nm_item"),
						rs.getString("txt_observacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_tipo_checkup_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupItemDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_tipo_checkup_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
