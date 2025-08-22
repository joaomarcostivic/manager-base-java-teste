package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoEventoDAO{

	public static int insert(TipoEvento objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoEvento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_tipo_evento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoEvento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_tipo_evento (cd_tipo_evento,"+
			                                  "nm_tipo_evento,"+
			                                  "id_tipo_evento,"+
			                                  "txt_tipo_evento,"+
			                                  "cd_infracao) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoEvento());
			pstmt.setString(3,objeto.getIdTipoEvento());
			pstmt.setString(4,objeto.getTxtTipoEvento());
			if(objeto.getCdInfracao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdInfracao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEventoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEventoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoEvento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoEvento objeto, int cdTipoEventoOld) {
		return update(objeto, cdTipoEventoOld, null);
	}

	public static int update(TipoEvento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoEvento objeto, int cdTipoEventoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_tipo_evento SET cd_tipo_evento=?,"+
												      		   "nm_tipo_evento=?,"+
												      		   "id_tipo_evento=?,"+
												      		   "txt_tipo_evento=?,"+
												      		   "cd_infracao=? WHERE cd_tipo_evento=?");
			pstmt.setInt(1,objeto.getCdTipoEvento());
			pstmt.setString(2,objeto.getNmTipoEvento());
			pstmt.setString(3,objeto.getIdTipoEvento());
			pstmt.setString(4,objeto.getTxtTipoEvento());
			if(objeto.getCdInfracao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdInfracao());
			pstmt.setInt(6, cdTipoEventoOld!=0 ? cdTipoEventoOld : objeto.getCdTipoEvento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEventoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEventoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoEvento) {
		return delete(cdTipoEvento, null);
	}

	public static int delete(int cdTipoEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_tipo_evento WHERE cd_tipo_evento=?");
			pstmt.setInt(1, cdTipoEvento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEventoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEventoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoEvento get(int cdTipoEvento) {
		return get(cdTipoEvento, null);
	}

	public static TipoEvento get(int cdTipoEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_tipo_evento WHERE cd_tipo_evento=?");
			pstmt.setInt(1, cdTipoEvento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoEvento(rs.getInt("cd_tipo_evento"),
						rs.getString("nm_tipo_evento"),
						rs.getString("id_tipo_evento"),
						rs.getString("txt_tipo_evento"),
						rs.getInt("cd_infracao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEventoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEventoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_tipo_evento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEventoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEventoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoEvento> getList() {
		return getList(null);
	}

	public static ArrayList<TipoEvento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoEvento> list = new ArrayList<TipoEvento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoEvento obj = TipoEventoDAO.get(rsm.getInt("cd_tipo_evento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEventoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_tipo_evento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}