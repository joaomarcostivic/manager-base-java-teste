package com.tivic.manager.seg;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ModuloDAO{

	public static int insert(Modulo objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(Modulo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_modulo");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_sistema");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdSistema()));
			int code = Conexao.getSequenceCode("seg_modulo", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if(objeto.getCdModulo()<=0)
				objeto.setCdModulo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO seg_modulo (cd_modulo,"+
			                                  "cd_sistema,"+
			                                  "nm_modulo,"+
			                                  "id_modulo,"+
			                                  "nr_versao,"+
			                                  "nr_release,"+
			                                  "lg_ativo) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdModulo());
			if(objeto.getCdSistema()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdSistema());
			pstmt.setString(3,objeto.getNmModulo());
			pstmt.setString(4,objeto.getIdModulo());
			pstmt.setString(5,objeto.getNrVersao());
			pstmt.setString(6,objeto.getNrRelease());
			pstmt.setInt(7,objeto.getLgAtivo());
			pstmt.executeUpdate();
			return objeto.getCdModulo();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModuloDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Modulo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Modulo objeto, int cdModuloOld, int cdSistemaOld) {
		return update(objeto, cdModuloOld, cdSistemaOld, null);
	}

	public static int update(Modulo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Modulo objeto, int cdModuloOld, int cdSistemaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE seg_modulo SET cd_modulo=?,"+
												      		   "cd_sistema=?,"+
												      		   "nm_modulo=?,"+
												      		   "id_modulo=?,"+
												      		   "nr_versao=?,"+
												      		   "nr_release=?,"+
												      		   "lg_ativo=? WHERE cd_modulo=? AND cd_sistema=?");
			pstmt.setInt(1,objeto.getCdModulo());
			pstmt.setInt(2,objeto.getCdSistema());
			pstmt.setString(3,objeto.getNmModulo());
			pstmt.setString(4,objeto.getIdModulo());
			pstmt.setString(5,objeto.getNrVersao());
			pstmt.setString(6,objeto.getNrRelease());
			pstmt.setInt(7,objeto.getLgAtivo());
			pstmt.setInt(8, cdModuloOld!=0 ? cdModuloOld : objeto.getCdModulo());
			pstmt.setInt(9, cdSistemaOld!=0 ? cdSistemaOld : objeto.getCdSistema());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModuloDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdModulo, int cdSistema) {
		return delete(cdModulo, cdSistema, null);
	}

	public static int delete(int cdModulo, int cdSistema, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM seg_modulo WHERE cd_modulo=? AND cd_sistema=?");
			pstmt.setInt(1, cdModulo);
			pstmt.setInt(2, cdSistema);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Modulo get(int cdModulo, int cdSistema) {
		return get(cdModulo, cdSistema, null);
	}

	public static Modulo get(int cdModulo, int cdSistema, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_modulo WHERE cd_modulo=? AND cd_sistema=?");
			pstmt.setInt(1, cdModulo);
			pstmt.setInt(2, cdSistema);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Modulo(rs.getInt("cd_modulo"),rs.getInt("cd_sistema"),rs.getString("nm_modulo"),rs.getString("id_modulo"),
							      rs.getString("nr_versao"),rs.getString("nr_release"),rs.getInt("lg_ativo"));
			}
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModuloDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_modulo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModuloDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM seg_modulo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
