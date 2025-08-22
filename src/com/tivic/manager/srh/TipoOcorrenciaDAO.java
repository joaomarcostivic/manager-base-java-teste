package com.tivic.manager.srh;

import java.sql.*;
import sol.dao.*;

import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoOcorrenciaDAO{

	public static int insert(TipoOcorrencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoOcorrencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("srh_tipo_ocorrencia", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoOcorrencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO srh_tipo_ocorrencia (cd_tipo_ocorrencia,"+
			                                  "nm_tipo_ocorrencia,"+
			                                  "id_tipo_ocorrencia) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoOcorrencia());
			pstmt.setString(3,objeto.getIdTipoOcorrencia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoOcorrencia objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoOcorrencia objeto, int cdTipoOcorrenciaOld) {
		return update(objeto, cdTipoOcorrenciaOld, null);
	}

	public static int update(TipoOcorrencia objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoOcorrencia objeto, int cdTipoOcorrenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE srh_tipo_ocorrencia SET cd_tipo_ocorrencia=?,"+
												      		   "nm_tipo_ocorrencia=?,"+
												      		   "id_tipo_ocorrencia=? WHERE cd_tipo_ocorrencia=?");
			pstmt.setInt(1,objeto.getCdTipoOcorrencia());
			pstmt.setString(2,objeto.getNmTipoOcorrencia());
			pstmt.setString(3,objeto.getIdTipoOcorrencia());
			pstmt.setInt(4, cdTipoOcorrenciaOld!=0 ? cdTipoOcorrenciaOld : objeto.getCdTipoOcorrencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoOcorrencia) {
		return delete(cdTipoOcorrencia, null);
	}

	public static int delete(int cdTipoOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM srh_tipo_ocorrencia WHERE cd_tipo_ocorrencia=?");
			pstmt.setInt(1, cdTipoOcorrencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoOcorrencia get(int cdTipoOcorrencia) {
		return get(cdTipoOcorrencia, null);
	}

	public static TipoOcorrencia get(int cdTipoOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM srh_tipo_ocorrencia WHERE cd_tipo_ocorrencia=?");
			pstmt.setInt(1, cdTipoOcorrencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoOcorrencia(rs.getInt("cd_tipo_ocorrencia"),
						rs.getString("nm_tipo_ocorrencia"),
						rs.getString("id_tipo_ocorrencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_tipo_ocorrencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM srh_tipo_ocorrencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
