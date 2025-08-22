package com.tivic.manager.fsc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoOrigemDAO{

	public static int insert(TipoOrigem objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoOrigem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fsc_tipo_origem", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoOrigem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fsc_tipo_origem (cd_tipo_origem,"+
			                                  "nm_tipo_origem,"+
			                                  "id_tipo_origem,"+
			                                  "st_tipo_origem) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoOrigem());
			pstmt.setString(3,objeto.getIdTipoOrigem());
			pstmt.setInt(4,objeto.getStTipoOrigem());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOrigemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOrigemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoOrigem objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoOrigem objeto, int cdTipoOrigemOld) {
		return update(objeto, cdTipoOrigemOld, null);
	}

	public static int update(TipoOrigem objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoOrigem objeto, int cdTipoOrigemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fsc_tipo_origem SET cd_tipo_origem=?,"+
												      		   "nm_tipo_origem=?,"+
												      		   "id_tipo_origem=?,"+
												      		   "st_tipo_origem=? WHERE cd_tipo_origem=?");
			pstmt.setInt(1,objeto.getCdTipoOrigem());
			pstmt.setString(2,objeto.getNmTipoOrigem());
			pstmt.setString(3,objeto.getIdTipoOrigem());
			pstmt.setInt(4,objeto.getStTipoOrigem());
			pstmt.setInt(5, cdTipoOrigemOld!=0 ? cdTipoOrigemOld : objeto.getCdTipoOrigem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOrigemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOrigemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoOrigem) {
		return delete(cdTipoOrigem, null);
	}

	public static int delete(int cdTipoOrigem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fsc_tipo_origem WHERE cd_tipo_origem=?");
			pstmt.setInt(1, cdTipoOrigem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOrigemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOrigemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoOrigem get(int cdTipoOrigem) {
		return get(cdTipoOrigem, null);
	}

	public static TipoOrigem get(int cdTipoOrigem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_tipo_origem WHERE cd_tipo_origem=?");
			pstmt.setInt(1, cdTipoOrigem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoOrigem(rs.getInt("cd_tipo_origem"),
						rs.getString("nm_tipo_origem"),
						rs.getString("id_tipo_origem"),
						rs.getInt("st_tipo_origem"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOrigemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOrigemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fsc_tipo_origem");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOrigemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOrigemDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fsc_tipo_origem", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
