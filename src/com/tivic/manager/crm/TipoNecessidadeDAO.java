package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoNecessidadeDAO{

	public static int insert(TipoNecessidade objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoNecessidade objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("crm_tipo_necessidade", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoNecessidade(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_tipo_necessidade (cd_tipo_necessidade,"+
			                                  "nm_tipo_necessidade,"+
			                                  "id_tipo_necessidade,"+
			                                  "st_tipo_necessidade) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoNecessidade());
			pstmt.setString(3,objeto.getIdTipoNecessidade());
			pstmt.setInt(4,objeto.getStTipoNecessidade());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoNecessidade objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoNecessidade objeto, int cdTipoNecessidadeOld) {
		return update(objeto, cdTipoNecessidadeOld, null);
	}

	public static int update(TipoNecessidade objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoNecessidade objeto, int cdTipoNecessidadeOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_tipo_necessidade SET cd_tipo_necessidade=?,"+
												      		   "nm_tipo_necessidade=?,"+
												      		   "id_tipo_necessidade=?,"+
												      		   "st_tipo_necessidade=? WHERE cd_tipo_necessidade=?");
			pstmt.setInt(1,objeto.getCdTipoNecessidade());
			pstmt.setString(2,objeto.getNmTipoNecessidade());
			pstmt.setString(3,objeto.getIdTipoNecessidade());
			pstmt.setInt(4,objeto.getStTipoNecessidade());
			pstmt.setInt(5, cdTipoNecessidadeOld!=0 ? cdTipoNecessidadeOld : objeto.getCdTipoNecessidade());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoNecessidadeDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoNecessidade) {
		return delete(cdTipoNecessidade, null);
	}

	public static int delete(int cdTipoNecessidade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_tipo_necessidade WHERE cd_tipo_necessidade=?");
			pstmt.setInt(1, cdTipoNecessidade);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoNecessidadeDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoNecessidade get(int cdTipoNecessidade) {
		return get(cdTipoNecessidade, null);
	}

	public static TipoNecessidade get(int cdTipoNecessidade, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_tipo_necessidade WHERE cd_tipo_necessidade=?");
			pstmt.setInt(1, cdTipoNecessidade);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoNecessidade(rs.getInt("cd_tipo_necessidade"),
						rs.getString("nm_tipo_necessidade"),
						rs.getString("id_tipo_necessidade"),
						rs.getInt("st_tipo_necessidade"));
			}
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoNecessidadeDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_tipo_necessidade " +
					                         "ORDER BY nm_tipo_necessidade ");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		return Search.find("SELECT * FROM crm_tipo_necessidade " +
				           "ORDER BY nm_tipo_necessidade ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
