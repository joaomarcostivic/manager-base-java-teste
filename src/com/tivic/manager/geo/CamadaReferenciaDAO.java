package com.tivic.manager.geo;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CamadaReferenciaDAO{

	public static int insert(CamadaReferencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(CamadaReferencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO geo_camada_referencia (cd_referencia,"+
			                                  "cd_camada) VALUES (?, ?)");
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdReferencia());
			if(objeto.getCdCamada()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCamada());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CamadaReferenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaReferenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CamadaReferencia objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(CamadaReferencia objeto, int cdReferenciaOld, int cdCamadaOld) {
		return update(objeto, cdReferenciaOld, cdCamadaOld, null);
	}

	public static int update(CamadaReferencia objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(CamadaReferencia objeto, int cdReferenciaOld, int cdCamadaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE geo_camada_referencia SET cd_referencia=?,"+
												      		   "cd_camada=? WHERE cd_referencia=? AND cd_camada=?");
			pstmt.setInt(1,objeto.getCdReferencia());
			pstmt.setInt(2,objeto.getCdCamada());
			pstmt.setInt(3, cdReferenciaOld!=0 ? cdReferenciaOld : objeto.getCdReferencia());
			pstmt.setInt(4, cdCamadaOld!=0 ? cdCamadaOld : objeto.getCdCamada());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CamadaReferenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaReferenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdReferencia, int cdCamada) {
		return delete(cdReferencia, cdCamada, null);
	}

	public static int delete(int cdReferencia, int cdCamada, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM geo_camada_referencia WHERE cd_referencia=? AND cd_camada=?");
			pstmt.setInt(1, cdReferencia);
			pstmt.setInt(2, cdCamada);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CamadaReferenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaReferenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CamadaReferencia get(int cdReferencia, int cdCamada) {
		return get(cdReferencia, cdCamada, null);
	}

	public static CamadaReferencia get(int cdReferencia, int cdCamada, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM geo_camada_referencia WHERE cd_referencia=? AND cd_camada=?");
			pstmt.setInt(1, cdReferencia);
			pstmt.setInt(2, cdCamada);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CamadaReferencia(rs.getInt("cd_referencia"),
						rs.getInt("cd_camada"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CamadaReferenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaReferenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM geo_camada_referencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CamadaReferenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaReferenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CamadaReferencia> getList() {
		return getList(null);
	}

	public static ArrayList<CamadaReferencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CamadaReferencia> list = new ArrayList<CamadaReferencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CamadaReferencia obj = CamadaReferenciaDAO.get(rsm.getInt("cd_referencia"), rsm.getInt("cd_camada"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaReferenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM geo_camada_referencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
