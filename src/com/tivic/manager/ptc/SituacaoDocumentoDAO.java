package com.tivic.manager.ptc;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class SituacaoDocumentoDAO{

	public static int insert(SituacaoDocumento objeto) {
		return insert(objeto, null);
	}

	public static int insert(SituacaoDocumento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ptc_situacao_documento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdSituacaoDocumento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_situacao_documento (cd_situacao_documento,"+
			                                  "nm_situacao_documento) VALUES (?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmSituacaoDocumento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoDocumentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoDocumentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(SituacaoDocumento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(SituacaoDocumento objeto, int cdSituacaoDocumentoOld) {
		return update(objeto, cdSituacaoDocumentoOld, null);
	}

	public static int update(SituacaoDocumento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(SituacaoDocumento objeto, int cdSituacaoDocumentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_situacao_documento SET cd_situacao_documento=?,"+
												      		   "nm_situacao_documento=? WHERE cd_situacao_documento=?");
			pstmt.setInt(1,objeto.getCdSituacaoDocumento());
			pstmt.setString(2,objeto.getNmSituacaoDocumento());
			pstmt.setInt(3, cdSituacaoDocumentoOld!=0 ? cdSituacaoDocumentoOld : objeto.getCdSituacaoDocumento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoDocumentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoDocumentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdSituacaoDocumento) {
		return delete(cdSituacaoDocumento, null);
	}

	public static int delete(int cdSituacaoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_situacao_documento WHERE cd_situacao_documento=?");
			pstmt.setInt(1, cdSituacaoDocumento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoDocumentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoDocumentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static SituacaoDocumento get(int cdSituacaoDocumento) {
		return get(cdSituacaoDocumento, null);
	}

	public static SituacaoDocumento get(int cdSituacaoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_situacao_documento WHERE cd_situacao_documento=?");
			pstmt.setInt(1, cdSituacaoDocumento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new SituacaoDocumento(rs.getInt("cd_situacao_documento"),
						rs.getString("nm_situacao_documento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoDocumentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoDocumentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_situacao_documento ORDER BY nm_situacao_documento");
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
		return Search.find("SELECT * FROM ptc_situacao_documento", "ORDER BY nm_situacao_documento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
