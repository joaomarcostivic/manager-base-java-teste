package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ArquivoTipoRegistroDAO{

	public static int insert(ArquivoTipoRegistro objeto) {
		return insert(objeto, null);
	}

	public static int insert(ArquivoTipoRegistro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_arquivo_tipo_registro", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoRegistro(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_arquivo_tipo_registro (cd_tipo_registro,"+
			                                  "nm_tipo_registro,"+
			                                  "id_tipo_arquivo) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoRegistro());
			pstmt.setString(3,objeto.getIdTipoArquivo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoTipoRegistroDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoTipoRegistroDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ArquivoTipoRegistro objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ArquivoTipoRegistro objeto, int cdTipoRegistroOld) {
		return update(objeto, cdTipoRegistroOld, null);
	}

	public static int update(ArquivoTipoRegistro objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ArquivoTipoRegistro objeto, int cdTipoRegistroOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_arquivo_tipo_registro SET cd_tipo_registro=?,"+
												      		   "nm_tipo_registro=?,"+
												      		   "id_tipo_arquivo=? WHERE cd_tipo_registro=?");
			pstmt.setInt(1,objeto.getCdTipoRegistro());
			pstmt.setString(2,objeto.getNmTipoRegistro());
			pstmt.setString(3,objeto.getIdTipoArquivo());
			pstmt.setInt(4, cdTipoRegistroOld!=0 ? cdTipoRegistroOld : objeto.getCdTipoRegistro());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoTipoRegistroDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoTipoRegistroDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoRegistro) {
		return delete(cdTipoRegistro, null);
	}

	public static int delete(int cdTipoRegistro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_arquivo_tipo_registro WHERE cd_tipo_registro=?");
			pstmt.setInt(1, cdTipoRegistro);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoTipoRegistroDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoTipoRegistroDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArquivoTipoRegistro get(int cdTipoRegistro) {
		return get(cdTipoRegistro, null);
	}

	public static ArquivoTipoRegistro get(int cdTipoRegistro, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_arquivo_tipo_registro WHERE cd_tipo_registro=?");
			pstmt.setInt(1, cdTipoRegistro);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ArquivoTipoRegistro(rs.getInt("cd_tipo_registro"),
						rs.getString("nm_tipo_registro"),
						rs.getString("id_tipo_arquivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoTipoRegistroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoTipoRegistroDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_arquivo_tipo_registro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoTipoRegistroDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoTipoRegistroDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_arquivo_tipo_registro", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
