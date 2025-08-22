package com.tivic.manager.geo;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CamadaUsuarioDAO{

	public static int insert(CamadaUsuario objeto) {
		return insert(objeto, null);
	}

	public static int insert(CamadaUsuario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO geo_camada_usuario (cd_camada,"+
			                                  "cd_usuario) VALUES (?, ?)");
			if(objeto.getCdCamada()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdCamada());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdUsuario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CamadaUsuarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaUsuarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CamadaUsuario objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(CamadaUsuario objeto, int cdCamadaOld, int cdUsuarioOld) {
		return update(objeto, cdCamadaOld, cdUsuarioOld, null);
	}

	public static int update(CamadaUsuario objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(CamadaUsuario objeto, int cdCamadaOld, int cdUsuarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE geo_camada_usuario SET cd_camada=?,"+
												      		   "cd_usuario=? WHERE cd_camada=? AND cd_usuario=?");
			pstmt.setInt(1,objeto.getCdCamada());
			pstmt.setInt(2,objeto.getCdUsuario());
			pstmt.setInt(3, cdCamadaOld!=0 ? cdCamadaOld : objeto.getCdCamada());
			pstmt.setInt(4, cdUsuarioOld!=0 ? cdUsuarioOld : objeto.getCdUsuario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CamadaUsuarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaUsuarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCamada, int cdUsuario) {
		return delete(cdCamada, cdUsuario, null);
	}

	public static int delete(int cdCamada, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM geo_camada_usuario WHERE cd_camada=? AND cd_usuario=?");
			pstmt.setInt(1, cdCamada);
			pstmt.setInt(2, cdUsuario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CamadaUsuarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaUsuarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CamadaUsuario get(int cdCamada, int cdUsuario) {
		return get(cdCamada, cdUsuario, null);
	}

	public static CamadaUsuario get(int cdCamada, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM geo_camada_usuario WHERE cd_camada=? AND cd_usuario=?");
			pstmt.setInt(1, cdCamada);
			pstmt.setInt(2, cdUsuario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CamadaUsuario(rs.getInt("cd_camada"),
						rs.getInt("cd_usuario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CamadaUsuarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaUsuarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM geo_camada_usuario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CamadaUsuarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaUsuarioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CamadaUsuario> getList() {
		return getList(null);
	}

	public static ArrayList<CamadaUsuario> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CamadaUsuario> list = new ArrayList<CamadaUsuario>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CamadaUsuario obj = CamadaUsuarioDAO.get(rsm.getInt("cd_camada"), rsm.getInt("cd_usuario"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaUsuarioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM geo_camada_usuario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
