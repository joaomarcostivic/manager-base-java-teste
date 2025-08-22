package com.tivic.manager.grl;

import java.sql.*;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;

import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoDocumentoDAO{

	public static int insert(TipoDocumento objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoDocumento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_tipo_documento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoDocumento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_tipo_documento (cd_tipo_documento,"+
			                                  "nm_tipo_documento,"+
			                                  "sg_tipo_documento,"+
			                                  "cd_empresa) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoDocumento());
			pstmt.setString(3,objeto.getSgTipoDocumento());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEmpresa());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoDocumento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoDocumento objeto, int cdTipoDocumentoOld) {
		return update(objeto, cdTipoDocumentoOld, null);
	}

	public static int update(TipoDocumento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoDocumento objeto, int cdTipoDocumentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_tipo_documento SET cd_tipo_documento=?,"+
												      		   "nm_tipo_documento=?,"+
												      		   "sg_tipo_documento=?,"+
												      		   "cd_empresa=?"+
												      		   " WHERE cd_tipo_documento=?");
			pstmt.setInt(1,objeto.getCdTipoDocumento());
			pstmt.setString(2,objeto.getNmTipoDocumento());
			pstmt.setString(3,objeto.getSgTipoDocumento());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEmpresa());
			pstmt.setInt(5, cdTipoDocumentoOld!=0 ? cdTipoDocumentoOld : objeto.getCdTipoDocumento());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoDocumento) {
		return delete(cdTipoDocumento, null);
	}

	public static int delete(int cdTipoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_tipo_documento WHERE cd_tipo_documento=?");
			pstmt.setInt(1, cdTipoDocumento);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoDocumento get(int cdTipoDocumento) {
		return get(cdTipoDocumento, null);
	}

	public static TipoDocumento get(int cdTipoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_documento WHERE cd_tipo_documento=?");
			pstmt.setInt(1, cdTipoDocumento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoDocumento(rs.getInt("cd_tipo_documento"),
						rs.getString("nm_tipo_documento"),
						rs.getString("sg_tipo_documento"),
						rs.getInt("cd_empresa"));
			}
			else{
				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoDAO.get: " + e);
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
		try {
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_tipo_documento ORDER BY nm_tipo_documento").executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_tipo_documento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
