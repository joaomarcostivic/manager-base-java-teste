package com.tivic.manager.acd;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoEquipamentoDAO{

	public static int insert(TipoEquipamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoEquipamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_tipo_equipamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoEquipamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_tipo_equipamento (cd_tipo_equipamento,"+
			                                  "nm_tipo_equipamento,"+
			                                  "id_tipo_equipamento) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoEquipamento());
			pstmt.setString(3,objeto.getIdTipoEquipamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoEquipamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoEquipamento objeto, int cdTipoEquipamentoOld) {
		return update(objeto, cdTipoEquipamentoOld, null);
	}

	public static int update(TipoEquipamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoEquipamento objeto, int cdTipoEquipamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_tipo_equipamento SET cd_tipo_equipamento=?,"+
												      		   "nm_tipo_equipamento=?,"+
												      		   "id_tipo_equipamento=? WHERE cd_tipo_equipamento=?");
			pstmt.setInt(1,objeto.getCdTipoEquipamento());
			pstmt.setString(2,objeto.getNmTipoEquipamento());
			pstmt.setString(3,objeto.getIdTipoEquipamento());
			pstmt.setInt(4, cdTipoEquipamentoOld!=0 ? cdTipoEquipamentoOld : objeto.getCdTipoEquipamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoEquipamento) {
		return delete(cdTipoEquipamento, null);
	}

	public static int delete(int cdTipoEquipamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_tipo_equipamento WHERE cd_tipo_equipamento=?");
			pstmt.setInt(1, cdTipoEquipamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoEquipamento get(int cdTipoEquipamento) {
		return get(cdTipoEquipamento, null);
	}

	public static TipoEquipamento get(int cdTipoEquipamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_equipamento WHERE cd_tipo_equipamento=?");
			pstmt.setInt(1, cdTipoEquipamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoEquipamento(rs.getInt("cd_tipo_equipamento"),
						rs.getString("nm_tipo_equipamento"),
						rs.getString("id_tipo_equipamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_equipamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_equipamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
