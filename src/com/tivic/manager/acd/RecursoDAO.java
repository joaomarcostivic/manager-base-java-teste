package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class RecursoDAO{

	public static int insert(Recurso objeto) {
		return insert(objeto, null);
	}

	public static int insert(Recurso objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_recurso");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_tipo_recurso");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdTipoRecurso()));
			int code = Conexao.getSequenceCode("acd_recurso", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRecurso(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_recurso (cd_recurso,"+
			                                  "cd_tipo_recurso,"+
			                                  "id_recurso,"+
			                                  "st_recurso,"+
			                                  "txt_observacao) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTipoRecurso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoRecurso());
			pstmt.setInt(3,objeto.getIdRecurso());
			pstmt.setInt(4,objeto.getStRecurso());
			pstmt.setString(5,objeto.getTxtObservacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecursoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RecursoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Recurso objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Recurso objeto, int cdRecursoOld, int cdTipoRecursoOld) {
		return update(objeto, cdRecursoOld, cdTipoRecursoOld, null);
	}

	public static int update(Recurso objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Recurso objeto, int cdRecursoOld, int cdTipoRecursoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_recurso SET cd_recurso=?,"+
												      		   "cd_tipo_recurso=?,"+
												      		   "id_recurso=?,"+
												      		   "st_recurso=?,"+
												      		   "txt_observacao=? WHERE cd_recurso=? AND cd_tipo_recurso=?");
			pstmt.setInt(1,objeto.getCdRecurso());
			pstmt.setInt(2,objeto.getCdTipoRecurso());
			pstmt.setInt(3,objeto.getIdRecurso());
			pstmt.setInt(4,objeto.getStRecurso());
			pstmt.setString(5,objeto.getTxtObservacao());
			pstmt.setInt(6, cdRecursoOld!=0 ? cdRecursoOld : objeto.getCdRecurso());
			pstmt.setInt(7, cdTipoRecursoOld!=0 ? cdTipoRecursoOld : objeto.getCdTipoRecurso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecursoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RecursoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRecurso, int cdTipoRecurso) {
		return delete(cdRecurso, cdTipoRecurso, null);
	}

	public static int delete(int cdRecurso, int cdTipoRecurso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_recurso WHERE cd_recurso=? AND cd_tipo_recurso=?");
			pstmt.setInt(1, cdRecurso);
			pstmt.setInt(2, cdTipoRecurso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecursoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RecursoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Recurso get(int cdRecurso, int cdTipoRecurso) {
		return get(cdRecurso, cdTipoRecurso, null);
	}

	public static Recurso get(int cdRecurso, int cdTipoRecurso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_recurso WHERE cd_recurso=? AND cd_tipo_recurso=?");
			pstmt.setInt(1, cdRecurso);
			pstmt.setInt(2, cdTipoRecurso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Recurso(rs.getInt("cd_recurso"),
						rs.getInt("cd_tipo_recurso"),
						rs.getInt("id_recurso"),
						rs.getInt("st_recurso"),
						rs.getString("txt_observacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecursoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecursoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_recurso");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecursoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecursoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Recurso> getList() {
		return getList(null);
	}

	public static ArrayList<Recurso> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Recurso> list = new ArrayList<Recurso>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Recurso obj = RecursoDAO.get(rsm.getInt("cd_recurso"), rsm.getInt("cd_tipo_recurso"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecursoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_recurso", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}