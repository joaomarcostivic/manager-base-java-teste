package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoAulaDAO{

	public static int insert(TipoAula objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoAula objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_tipo_aula", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoAula(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_tipo_aula (cd_tipo_aula,"+
			                                  "nm_tipo_aula,"+
			                                  "vl_hora_aula) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoAula());
			pstmt.setFloat(3,objeto.getVlHoraAula());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAulaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAulaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoAula objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoAula objeto, int cdTipoAulaOld) {
		return update(objeto, cdTipoAulaOld, null);
	}

	public static int update(TipoAula objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoAula objeto, int cdTipoAulaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_tipo_aula SET cd_tipo_aula=?,"+
												      		   "nm_tipo_aula=?,"+
												      		   "vl_hora_aula=? WHERE cd_tipo_aula=?");
			pstmt.setInt(1,objeto.getCdTipoAula());
			pstmt.setString(2,objeto.getNmTipoAula());
			pstmt.setFloat(3,objeto.getVlHoraAula());
			pstmt.setInt(4, cdTipoAulaOld!=0 ? cdTipoAulaOld : objeto.getCdTipoAula());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAulaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAulaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoAula) {
		return delete(cdTipoAula, null);
	}

	public static int delete(int cdTipoAula, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_tipo_aula WHERE cd_tipo_aula=?");
			pstmt.setInt(1, cdTipoAula);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAulaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAulaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoAula get(int cdTipoAula) {
		return get(cdTipoAula, null);
	}

	public static TipoAula get(int cdTipoAula, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_aula WHERE cd_tipo_aula=?");
			pstmt.setInt(1, cdTipoAula);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoAula(rs.getInt("cd_tipo_aula"),
						rs.getString("nm_tipo_aula"),
						rs.getFloat("vl_hora_aula"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAulaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAulaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_aula");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAulaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAulaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoAula> getList() {
		return getList(null);
	}

	public static ArrayList<TipoAula> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoAula> list = new ArrayList<TipoAula>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoAula obj = TipoAulaDAO.get(rsm.getInt("cd_tipo_aula"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAulaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_aula", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
