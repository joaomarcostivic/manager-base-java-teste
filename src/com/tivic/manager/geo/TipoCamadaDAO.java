package com.tivic.manager.geo;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoCamadaDAO{

	public static int insert(TipoCamada objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoCamada objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("geo_tipo_camada", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO geo_tipo_camada (cd_tipo,"+
			                                  "nm_tipo,"+
			                                  "txt_tipo,"+
			                                  "id_tipo) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipo());
			pstmt.setString(3,objeto.getTxtTipo());
			pstmt.setString(4,objeto.getIdTipo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCamadaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCamadaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoCamada objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoCamada objeto, int cdTipoOld) {
		return update(objeto, cdTipoOld, null);
	}

	public static int update(TipoCamada objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoCamada objeto, int cdTipoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE geo_tipo_camada SET cd_tipo=?,"+
												      		   "nm_tipo=?,"+
												      		   "txt_tipo=?,"+
												      		   "id_tipo=? WHERE cd_tipo=?");
			pstmt.setInt(1,objeto.getCdTipo());
			pstmt.setString(2,objeto.getNmTipo());
			pstmt.setString(3,objeto.getTxtTipo());
			pstmt.setString(4,objeto.getIdTipo());
			pstmt.setInt(5, cdTipoOld!=0 ? cdTipoOld : objeto.getCdTipo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCamadaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCamadaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipo) {
		return delete(cdTipo, null);
	}

	public static int delete(int cdTipo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM geo_tipo_camada WHERE cd_tipo=?");
			pstmt.setInt(1, cdTipo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCamadaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCamadaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoCamada get(int cdTipo) {
		return get(cdTipo, null);
	}

	public static TipoCamada get(int cdTipo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM geo_tipo_camada WHERE cd_tipo=?");
			pstmt.setInt(1, cdTipo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoCamada(rs.getInt("cd_tipo"),
						rs.getString("nm_tipo"),
						rs.getString("txt_tipo"),
						rs.getString("id_tipo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCamadaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCamadaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM geo_tipo_camada");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCamadaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCamadaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoCamada> getList() {
		return getList(null);
	}

	public static ArrayList<TipoCamada> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoCamada> list = new ArrayList<TipoCamada>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoCamada obj = TipoCamadaDAO.get(rsm.getInt("cd_tipo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCamadaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM geo_tipo_camada", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
