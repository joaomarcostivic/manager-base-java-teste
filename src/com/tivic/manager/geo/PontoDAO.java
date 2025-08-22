package com.tivic.manager.geo;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class PontoDAO{

	public static int insert(Ponto objeto) {
		return insert(objeto, null);
	}

	public static int insert(Ponto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			@SuppressWarnings("unchecked")
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_ponto");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_referencia");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdReferencia()));
			int code = Conexao.getSequenceCode("geo_ponto", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPonto(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO geo_ponto (cd_ponto,"+
			                                  "cd_referencia,"+
			                                  "vl_latitude,"+
			                                  "vl_longitude) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdReferencia());
			pstmt.setFloat(3,objeto.getVlLatitude());
			pstmt.setFloat(4,objeto.getVlLongitude());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Ponto objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Ponto objeto, int cdPontoOld, int cdReferenciaOld) {
		return update(objeto, cdPontoOld, cdReferenciaOld, null);
	}

	public static int update(Ponto objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Ponto objeto, int cdPontoOld, int cdReferenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE geo_ponto SET cd_ponto=?,"+
												      		   "cd_referencia=?,"+
												      		   "vl_latitude=?,"+
												      		   "vl_longitude=? WHERE cd_ponto=? AND cd_referencia=?");
			pstmt.setInt(1,objeto.getCdPonto());
			pstmt.setInt(2,objeto.getCdReferencia());
			pstmt.setFloat(3,objeto.getVlLatitude());
			pstmt.setFloat(4,objeto.getVlLongitude());
			pstmt.setInt(5, cdPontoOld!=0 ? cdPontoOld : objeto.getCdPonto());
			pstmt.setInt(6, cdReferenciaOld!=0 ? cdReferenciaOld : objeto.getCdReferencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPonto, int cdReferencia) {
		return delete(cdPonto, cdReferencia, null);
	}

	public static int delete(int cdPonto, int cdReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM geo_ponto WHERE cd_ponto=? AND cd_referencia=?");
			pstmt.setInt(1, cdPonto);
			pstmt.setInt(2, cdReferencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Ponto get(int cdPonto, int cdReferencia) {
		return get(cdPonto, cdReferencia, null);
	}

	public static Ponto get(int cdPonto, int cdReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM geo_ponto WHERE cd_ponto=? AND cd_referencia=?");
			pstmt.setInt(1, cdPonto);
			pstmt.setInt(2, cdReferencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Ponto(rs.getInt("cd_ponto"),
						rs.getInt("cd_referencia"),
						rs.getFloat("vl_latitude"),
						rs.getFloat("vl_longitude"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM geo_ponto");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Ponto> getList() {
		return getList(null);
	}

	public static ArrayList<Ponto> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Ponto> list = new ArrayList<Ponto>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Ponto obj = PontoDAO.get(rsm.getInt("cd_ponto"), rsm.getInt("cd_referencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM geo_ponto", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
