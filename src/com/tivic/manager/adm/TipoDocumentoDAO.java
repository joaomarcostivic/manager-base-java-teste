package com.tivic.manager.adm;

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
			int code = Conexao.getSequenceCode("adm_tipo_documento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoDocumento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_tipo_documento (cd_tipo_documento,"+
			                                  "nm_tipo_documento,"+
			                                  "sg_tipo_documento,"+
			                                  "id_tipo_documento,"+
			                                  "st_tipo_documento,"+
			                                  "cd_forma_transferencia," +
			                                  "id_sped) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoDocumento());
			pstmt.setString(3,objeto.getSgTipoDocumento());
			pstmt.setString(4,objeto.getIdTipoDocumento());
			pstmt.setInt(5,objeto.getStTipoDocumento());
			if(objeto.getCdFormaTransferencia()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdFormaTransferencia());
			pstmt.setString(7,objeto.getIdSped());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
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
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_tipo_documento SET cd_tipo_documento=?,"+
												      		   "nm_tipo_documento=?,"+
												      		   "sg_tipo_documento=?,"+
												      		   "id_tipo_documento=?,"+
												      		   "st_tipo_documento=?,"+
												      		   "cd_forma_transferencia=?," +
												      		   "id_sped=? WHERE cd_tipo_documento=?");
			pstmt.setInt(1,objeto.getCdTipoDocumento());
			pstmt.setString(2,objeto.getNmTipoDocumento());
			pstmt.setString(3,objeto.getSgTipoDocumento());
			pstmt.setString(4,objeto.getIdTipoDocumento());
			pstmt.setInt(5,objeto.getStTipoDocumento());
			if(objeto.getCdFormaTransferencia()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdFormaTransferencia());
			pstmt.setString(7,objeto.getIdSped());
			pstmt.setInt(8, cdTipoDocumentoOld!=0 ? cdTipoDocumentoOld : objeto.getCdTipoDocumento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
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
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_tipo_documento WHERE cd_tipo_documento=?");
			pstmt.setInt(1, cdTipoDocumento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_tipo_documento WHERE cd_tipo_documento=?");
			pstmt.setInt(1, cdTipoDocumento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoDocumento(rs.getInt("cd_tipo_documento"),
						rs.getString("nm_tipo_documento"),
						rs.getString("sg_tipo_documento"),
						rs.getString("id_tipo_documento"),
						rs.getInt("st_tipo_documento"),
						rs.getInt("cd_forma_transferencia"),
						rs.getString("id_sped"));
			}
			else
				return null;
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_tipo_documento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoDAO.getAll: " + sqlExpt);
			return null;
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
		return Search.find("SELECT * FROM adm_tipo_documento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
