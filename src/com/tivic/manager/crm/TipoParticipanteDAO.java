package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoParticipanteDAO{

	public static int insert(TipoParticipante objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoParticipante objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("crm_tipo_participante", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoParticipante(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_tipo_participante (cd_tipo_participante,"+
			                                  "nm_tipo_participante) VALUES (?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoParticipante());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoParticipanteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoParticipanteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoParticipante objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoParticipante objeto, int cdTipoParticipanteOld) {
		return update(objeto, cdTipoParticipanteOld, null);
	}

	public static int update(TipoParticipante objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoParticipante objeto, int cdTipoParticipanteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_tipo_participante SET cd_tipo_participante=?,"+
												      		   "nm_tipo_participante=? WHERE cd_tipo_participante=?");
			pstmt.setInt(1,objeto.getCdTipoParticipante());
			pstmt.setString(2,objeto.getNmTipoParticipante());
			pstmt.setInt(3, cdTipoParticipanteOld!=0 ? cdTipoParticipanteOld : objeto.getCdTipoParticipante());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoParticipanteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoParticipanteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoParticipante) {
		return delete(cdTipoParticipante, null);
	}

	public static int delete(int cdTipoParticipante, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_tipo_participante WHERE cd_tipo_participante=?");
			pstmt.setInt(1, cdTipoParticipante);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoParticipanteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoParticipanteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoParticipante get(int cdTipoParticipante) {
		return get(cdTipoParticipante, null);
	}

	public static TipoParticipante get(int cdTipoParticipante, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_tipo_participante WHERE cd_tipo_participante=?");
			pstmt.setInt(1, cdTipoParticipante);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoParticipante(rs.getInt("cd_tipo_participante"),
						rs.getString("nm_tipo_participante"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoParticipanteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoParticipanteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_tipo_participante ORDER BY nm_tipo_participante");
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
		return Search.find("SELECT * FROM crm_tipo_participante", "ORDER BY nm_tipo_participante", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
