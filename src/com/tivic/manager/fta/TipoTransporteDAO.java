package com.tivic.manager.fta;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoTransporteDAO{

	public static int insert(TipoTransporte objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoTransporte objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fta_tipo_transporte", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoTransporte(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_tipo_transporte (cd_tipo_transporte,"+
			                                  "nm_tipo_transporte,"+
			                                  "id_tipo_transporte) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoTransporte());
			pstmt.setString(3,objeto.getIdTipoTransporte());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoTransporteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoTransporteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoTransporte objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoTransporte objeto, int cdTipoTransporteOld) {
		return update(objeto, cdTipoTransporteOld, null);
	}

	public static int update(TipoTransporte objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoTransporte objeto, int cdTipoTransporteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_tipo_transporte SET cd_tipo_transporte=?,"+
												      		   "nm_tipo_transporte=?,"+
												      		   "id_tipo_transporte=? WHERE cd_tipo_transporte=?");
			pstmt.setInt(1,objeto.getCdTipoTransporte());
			pstmt.setString(2,objeto.getNmTipoTransporte());
			pstmt.setString(3,objeto.getIdTipoTransporte());
			pstmt.setInt(4, cdTipoTransporteOld!=0 ? cdTipoTransporteOld : objeto.getCdTipoTransporte());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoTransporteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoTransporteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoTransporte) {
		return delete(cdTipoTransporte, null);
	}

	public static int delete(int cdTipoTransporte, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_tipo_transporte WHERE cd_tipo_transporte=?");
			pstmt.setInt(1, cdTipoTransporte);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoTransporteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoTransporteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoTransporte get(int cdTipoTransporte) {
		return get(cdTipoTransporte, null);
	}

	public static TipoTransporte get(int cdTipoTransporte, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_tipo_transporte WHERE cd_tipo_transporte=?");
			pstmt.setInt(1, cdTipoTransporte);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoTransporte(rs.getInt("cd_tipo_transporte"),
						rs.getString("nm_tipo_transporte"),
						rs.getString("id_tipo_transporte"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoTransporteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoTransporteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_tipo_transporte");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoTransporteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoTransporteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoTransporte> getList() {
		return getList(null);
	}

	public static ArrayList<TipoTransporte> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoTransporte> list = new ArrayList<TipoTransporte>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoTransporte obj = TipoTransporteDAO.get(rsm.getInt("cd_tipo_transporte"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoTransporteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM fta_tipo_transporte", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
