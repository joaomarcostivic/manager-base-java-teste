package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoInstrumentosPedagogicosDAO{

	public static int insert(TipoInstrumentosPedagogicos objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoInstrumentosPedagogicos objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_tipo_instrumentos_pedagogicos", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoInstrumentosPedagogicos(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_tipo_instrumentos_pedagogicos (cd_tipo_instrumentos_pedagogicos,"+
			                                  "nm_tipo_instrumentos_pedagogicos,"+
			                                  "id_tipo_instrumentos_pedagogicos,"+
			                                  "st_tipo_instrumentos_pedagogicos) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoInstrumentosPedagogicos());
			pstmt.setString(3,objeto.getIdTipoInstrumentosPedagogicos());
			pstmt.setInt(4,objeto.getStTipoInstrumentosPedagogicos());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoInstrumentosPedagogicosDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoInstrumentosPedagogicosDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoInstrumentosPedagogicos objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoInstrumentosPedagogicos objeto, int cdTipoInstrumentosPedagogicosOld) {
		return update(objeto, cdTipoInstrumentosPedagogicosOld, null);
	}

	public static int update(TipoInstrumentosPedagogicos objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoInstrumentosPedagogicos objeto, int cdTipoInstrumentosPedagogicosOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_tipo_instrumentos_pedagogicos SET cd_tipo_instrumentos_pedagogicos=?,"+
												      		   "nm_tipo_instrumentos_pedagogicos=?,"+
												      		   "id_tipo_instrumentos_pedagogicos=?,"+
												      		   "st_tipo_instrumentos_pedagogicos=? WHERE cd_tipo_instrumentos_pedagogicos=?");
			pstmt.setInt(1,objeto.getCdTipoInstrumentosPedagogicos());
			pstmt.setString(2,objeto.getNmTipoInstrumentosPedagogicos());
			pstmt.setString(3,objeto.getIdTipoInstrumentosPedagogicos());
			pstmt.setInt(4,objeto.getStTipoInstrumentosPedagogicos());
			pstmt.setInt(5, cdTipoInstrumentosPedagogicosOld!=0 ? cdTipoInstrumentosPedagogicosOld : objeto.getCdTipoInstrumentosPedagogicos());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoInstrumentosPedagogicosDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoInstrumentosPedagogicosDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoInstrumentosPedagogicos) {
		return delete(cdTipoInstrumentosPedagogicos, null);
	}

	public static int delete(int cdTipoInstrumentosPedagogicos, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_tipo_instrumentos_pedagogicos WHERE cd_tipo_instrumentos_pedagogicos=?");
			pstmt.setInt(1, cdTipoInstrumentosPedagogicos);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoInstrumentosPedagogicosDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoInstrumentosPedagogicosDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoInstrumentosPedagogicos get(int cdTipoInstrumentosPedagogicos) {
		return get(cdTipoInstrumentosPedagogicos, null);
	}

	public static TipoInstrumentosPedagogicos get(int cdTipoInstrumentosPedagogicos, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_instrumentos_pedagogicos WHERE cd_tipo_instrumentos_pedagogicos=?");
			pstmt.setInt(1, cdTipoInstrumentosPedagogicos);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoInstrumentosPedagogicos(rs.getInt("cd_tipo_instrumentos_pedagogicos"),
						rs.getString("nm_tipo_instrumentos_pedagogicos"),
						rs.getString("id_tipo_instrumentos_pedagogicos"),
						rs.getInt("st_tipo_instrumentos_pedagogicos"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoInstrumentosPedagogicosDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoInstrumentosPedagogicosDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_instrumentos_pedagogicos");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoInstrumentosPedagogicosDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoInstrumentosPedagogicosDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoInstrumentosPedagogicos> getList() {
		return getList(null);
	}

	public static ArrayList<TipoInstrumentosPedagogicos> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoInstrumentosPedagogicos> list = new ArrayList<TipoInstrumentosPedagogicos>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoInstrumentosPedagogicos obj = TipoInstrumentosPedagogicosDAO.get(rsm.getInt("cd_tipo_instrumentos_pedagogicos"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoInstrumentosPedagogicosDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_instrumentos_pedagogicos", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}