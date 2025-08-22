package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoArquivoDAO{

	public static int insert(TipoArquivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoArquivo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_tipo_arquivo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoArquivo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_tipo_arquivo (cd_tipo_arquivo,"+
			                                  "nm_tipo_arquivo,"+
			                                  "id_tipo_arquivo) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoArquivo());
			pstmt.setString(3,objeto.getIdTipoArquivo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoArquivoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoArquivoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoArquivo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoArquivo objeto, int cdTipoArquivoOld) {
		return update(objeto, cdTipoArquivoOld, null);
	}

	public static int update(TipoArquivo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoArquivo objeto, int cdTipoArquivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_tipo_arquivo SET cd_tipo_arquivo=?,"+
												      		   "nm_tipo_arquivo=?,"+
												      		   "id_tipo_arquivo=? WHERE cd_tipo_arquivo=?");
			pstmt.setInt(1,objeto.getCdTipoArquivo());
			pstmt.setString(2,objeto.getNmTipoArquivo());
			pstmt.setString(3,objeto.getIdTipoArquivo());
			pstmt.setInt(4, cdTipoArquivoOld!=0 ? cdTipoArquivoOld : objeto.getCdTipoArquivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoArquivoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoArquivoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoArquivo) {
		return delete(cdTipoArquivo, null);
	}

	public static int delete(int cdTipoArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_tipo_arquivo WHERE cd_tipo_arquivo=?");
			pstmt.setInt(1, cdTipoArquivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoArquivoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoArquivoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoArquivo get(int cdTipoArquivo) {
		return get(cdTipoArquivo, null);
	}

	public static TipoArquivo get(int cdTipoArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_arquivo WHERE cd_tipo_arquivo=?");
			pstmt.setInt(1, cdTipoArquivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoArquivo(rs.getInt("cd_tipo_arquivo"),
						rs.getString("nm_tipo_arquivo"),
						rs.getString("id_tipo_arquivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoArquivoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoArquivoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_arquivo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoArquivoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoArquivoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_tipo_arquivo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
