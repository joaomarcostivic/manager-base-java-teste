package com.tivic.manager.ord;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoLaudoDAO{

	public static int insert(TipoLaudo objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoLaudo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ord_tipo_laudo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoLaudo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ord_tipo_laudo (cd_tipo_laudo,"+
			                                  "nm_tipo_laudo,"+
			                                  "id_tipo_laudo) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoLaudo());
			pstmt.setString(3,objeto.getIdTipoLaudo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoLaudoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoLaudoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoLaudo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoLaudo objeto, int cdTipoLaudoOld) {
		return update(objeto, cdTipoLaudoOld, null);
	}

	public static int update(TipoLaudo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoLaudo objeto, int cdTipoLaudoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ord_tipo_laudo SET cd_tipo_laudo=?,"+
												      		   "nm_tipo_laudo=?,"+
												      		   "id_tipo_laudo=? WHERE cd_tipo_laudo=?");
			pstmt.setInt(1,objeto.getCdTipoLaudo());
			pstmt.setString(2,objeto.getNmTipoLaudo());
			pstmt.setString(3,objeto.getIdTipoLaudo());
			pstmt.setInt(4, cdTipoLaudoOld!=0 ? cdTipoLaudoOld : objeto.getCdTipoLaudo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoLaudoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoLaudoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoLaudo) {
		return delete(cdTipoLaudo, null);
	}

	public static int delete(int cdTipoLaudo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ord_tipo_laudo WHERE cd_tipo_laudo=?");
			pstmt.setInt(1, cdTipoLaudo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoLaudoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoLaudoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoLaudo get(int cdTipoLaudo) {
		return get(cdTipoLaudo, null);
	}

	public static TipoLaudo get(int cdTipoLaudo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ord_tipo_laudo WHERE cd_tipo_laudo=?");
			pstmt.setInt(1, cdTipoLaudo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoLaudo(rs.getInt("cd_tipo_laudo"),
						rs.getString("nm_tipo_laudo"),
						rs.getString("id_tipo_laudo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoLaudoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoLaudoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ord_tipo_laudo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoLaudoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoLaudoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoLaudo> getList() {
		return getList(null);
	}

	public static ArrayList<TipoLaudo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoLaudo> list = new ArrayList<TipoLaudo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoLaudo obj = TipoLaudoDAO.get(rsm.getInt("cd_tipo_laudo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoLaudoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ord_tipo_laudo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
