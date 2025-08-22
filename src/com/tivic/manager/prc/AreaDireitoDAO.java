package com.tivic.manager.prc;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AreaDireitoDAO{

	public static int insert(AreaDireito objeto) {
		return insert(objeto, null);
	}

	public static int insert(AreaDireito objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("prc_area_direito", connect);
			if (code <= 0) {
				if (isConnectionNull)
					sol.dao.Conexao.rollback(connect);
				return -1;
			}
			if(objeto.getCdAreaDireito()<=0)
				objeto.setCdAreaDireito(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_area_direito (cd_area_direito,"+
			                                  "nm_area_direito,"+
			                                  "id_area_direito) VALUES (?, ?, ?)");
			pstmt.setInt(1, objeto.getCdAreaDireito());
			pstmt.setString(2,objeto.getNmAreaDireito());
			pstmt.setString(3,objeto.getIdAreaDireito());
			pstmt.executeUpdate();
			return objeto.getCdAreaDireito();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AreaDireitoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AreaDireito objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AreaDireito objeto, int cdAreaDireitoOld) {
		return update(objeto, cdAreaDireitoOld, null);
	}

	public static int update(AreaDireito objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AreaDireito objeto, int cdAreaDireitoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_area_direito SET cd_area_direito=?,"+
												      		   "nm_area_direito=?,"+
												      		   "id_area_direito=? WHERE cd_area_direito=?");
			pstmt.setInt(1,objeto.getCdAreaDireito());
			pstmt.setString(2,objeto.getNmAreaDireito());
			pstmt.setString(3,objeto.getIdAreaDireito());
			pstmt.setInt(4, cdAreaDireitoOld!=0 ? cdAreaDireitoOld : objeto.getCdAreaDireito());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AreaDireitoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAreaDireito) {
		return delete(cdAreaDireito, null);
	}

	public static int delete(int cdAreaDireito, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_area_direito WHERE cd_area_direito=?");
			pstmt.setInt(1, cdAreaDireito);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AreaDireitoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AreaDireito get(int cdAreaDireito) {
		return get(cdAreaDireito, null);
	}

	public static AreaDireito get(int cdAreaDireito, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_area_direito WHERE cd_area_direito=?");
			pstmt.setInt(1, cdAreaDireito);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AreaDireito(rs.getInt("cd_area_direito"),
						rs.getString("nm_area_direito"),
						rs.getString("id_area_direito"));
			}
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AreaDireitoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_area_direito");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AreaDireitoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM prc_area_direito", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
