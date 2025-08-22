package com.tivic.manager.geo;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CamadaDAO{

	public static int insert(Camada objeto) {
		return insert(objeto, null);
	}

	public static int insert(Camada objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("geo_camada", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCamada(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO geo_camada (cd_camada,"+
			                                  "nm_camada,"+
			                                  "txt_camada,"+
			                                  "cd_tipo,"+
			                                  "tp_origem,"+
			                                  "img_camada,"+
			                                  "vl_cor,"+
			                                  "cd_mapa) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmCamada());
			pstmt.setString(3,objeto.getTxtCamada());
			if(objeto.getCdTipo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTipo());
			pstmt.setInt(5,objeto.getTpOrigem());
			if(objeto.getImgCamada()==null)
				pstmt.setNull(6, Types.BINARY);
			else
				pstmt.setBytes(6,objeto.getImgCamada());
			pstmt.setInt(7,objeto.getVlCor());
			if(objeto.getCdMapa()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdMapa());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CamadaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Camada objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Camada objeto, int cdCamadaOld) {
		return update(objeto, cdCamadaOld, null);
	}

	public static int update(Camada objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Camada objeto, int cdCamadaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE geo_camada SET cd_camada=?,"+
												      		   "nm_camada=?,"+
												      		   "txt_camada=?,"+
												      		   "cd_tipo=?,"+
												      		   "tp_origem=?,"+
												      		   "img_camada=?,"+
												      		   "vl_cor=?,"+
												      		   "cd_mapa=? WHERE cd_camada=?");
			pstmt.setInt(1,objeto.getCdCamada());
			pstmt.setString(2,objeto.getNmCamada());
			pstmt.setString(3,objeto.getTxtCamada());
			if(objeto.getCdTipo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTipo());
			pstmt.setInt(5,objeto.getTpOrigem());
			if(objeto.getImgCamada()==null)
				pstmt.setNull(6, Types.BINARY);
			else
				pstmt.setBytes(6,objeto.getImgCamada());
			pstmt.setInt(7,objeto.getVlCor());
			if(objeto.getCdMapa()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdMapa());
			pstmt.setInt(9, cdCamadaOld!=0 ? cdCamadaOld : objeto.getCdCamada());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CamadaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCamada) {
		return delete(cdCamada, null);
	}

	public static int delete(int cdCamada, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM geo_camada WHERE cd_camada=?");
			pstmt.setInt(1, cdCamada);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CamadaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Camada get(int cdCamada) {
		return get(cdCamada, null);
	}

	public static Camada get(int cdCamada, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM geo_camada WHERE cd_camada=?");
			pstmt.setInt(1, cdCamada);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Camada(rs.getInt("cd_camada"),
						rs.getString("nm_camada"),
						rs.getString("txt_camada"),
						rs.getInt("cd_tipo"),
						rs.getInt("tp_origem"),
						rs.getBytes("img_camada")==null?null:rs.getBytes("img_camada"),
						rs.getInt("vl_cor"),
						rs.getInt("cd_mapa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CamadaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM geo_camada");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CamadaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Camada> getList() {
		return getList(null);
	}

	public static ArrayList<Camada> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Camada> list = new ArrayList<Camada>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Camada obj = CamadaDAO.get(rsm.getInt("cd_camada"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM geo_camada", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
