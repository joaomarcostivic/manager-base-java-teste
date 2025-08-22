package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ArquivoTipoErroDAO{

	public static int insert(ArquivoTipoErro objeto) {
		return insert(objeto, null);
	}

	public static int insert(ArquivoTipoErro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_arquivo_tipo_erro", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoErro(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_arquivo_tipo_erro (cd_tipo_erro,"+
			                                  "nm_tipo_erro,"+
			                                  "id_tipo_erro) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoErro());
			pstmt.setString(3,objeto.getIdTipoErro());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoTipoErroDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoTipoErroDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ArquivoTipoErro objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ArquivoTipoErro objeto, int cdTipoErroOld) {
		return update(objeto, cdTipoErroOld, null);
	}

	public static int update(ArquivoTipoErro objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ArquivoTipoErro objeto, int cdTipoErroOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_arquivo_tipo_erro SET cd_tipo_erro=?,"+
												      		   "nm_tipo_erro=?,"+
												      		   "id_tipo_erro=? WHERE cd_tipo_erro=?");
			pstmt.setInt(1,objeto.getCdTipoErro());
			pstmt.setString(2,objeto.getNmTipoErro());
			pstmt.setString(3,objeto.getIdTipoErro());
			pstmt.setInt(4, cdTipoErroOld!=0 ? cdTipoErroOld : objeto.getCdTipoErro());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoTipoErroDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoTipoErroDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoErro) {
		return delete(cdTipoErro, null);
	}

	public static int delete(int cdTipoErro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_arquivo_tipo_erro WHERE cd_tipo_erro=?");
			pstmt.setInt(1, cdTipoErro);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoTipoErroDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoTipoErroDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArquivoTipoErro get(int cdTipoErro) {
		return get(cdTipoErro, null);
	}

	public static ArquivoTipoErro get(int cdTipoErro, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_arquivo_tipo_erro WHERE cd_tipo_erro=?");
			pstmt.setInt(1, cdTipoErro);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ArquivoTipoErro(rs.getInt("cd_tipo_erro"),
						rs.getString("nm_tipo_erro"),
						rs.getString("id_tipo_erro"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoTipoErroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoTipoErroDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_arquivo_tipo_erro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoTipoErroDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoTipoErroDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_arquivo_tipo_erro", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
