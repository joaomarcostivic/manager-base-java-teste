package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoConteinerDAO{

	public static int insert(TipoConteiner objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoConteiner objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_tipo_conteiner", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoConteiner(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_tipo_conteiner (cd_tipo_conteiner,"+
			                                  "nm_tipo_conteiner) VALUES (?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoConteiner());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoConteinerDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoConteinerDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoConteiner objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoConteiner objeto, int cdTipoConteinerOld) {
		return update(objeto, cdTipoConteinerOld, null);
	}

	public static int update(TipoConteiner objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoConteiner objeto, int cdTipoConteinerOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_tipo_conteiner SET cd_tipo_conteiner=?,"+
												      		   "nm_tipo_conteiner=? WHERE cd_tipo_conteiner=?");
			pstmt.setInt(1,objeto.getCdTipoConteiner());
			pstmt.setString(2,objeto.getNmTipoConteiner());
			pstmt.setInt(3, cdTipoConteinerOld!=0 ? cdTipoConteinerOld : objeto.getCdTipoConteiner());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoConteinerDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoConteinerDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoConteiner) {
		return delete(cdTipoConteiner, null);
	}

	public static int delete(int cdTipoConteiner, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_tipo_conteiner WHERE cd_tipo_conteiner=?");
			pstmt.setInt(1, cdTipoConteiner);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoConteinerDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoConteinerDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoConteiner get(int cdTipoConteiner) {
		return get(cdTipoConteiner, null);
	}

	public static TipoConteiner get(int cdTipoConteiner, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_conteiner WHERE cd_tipo_conteiner=?");
			pstmt.setInt(1, cdTipoConteiner);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoConteiner(rs.getInt("cd_tipo_conteiner"),
						rs.getString("nm_tipo_conteiner"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoConteinerDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoConteinerDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_conteiner");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoConteinerDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoConteinerDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_tipo_conteiner", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
