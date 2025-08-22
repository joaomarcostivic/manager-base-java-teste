package com.tivic.manager.seg;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class UsuarioChaveDAO{

	public static int insert(UsuarioChave objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(UsuarioChave objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_chave");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_usuario");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdUsuario()));
			int code = Conexao.getSequenceCode("seg_usuario_chave", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdChave(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO seg_usuario_chave (cd_chave,"+
			                                  "cd_usuario,"+
			                                  "tp_chave,"+
			                                  "blb_chave_publica,"+
			                                  "blb_chave_privada,"+
			                                  "dt_criacao,"+
			                                  "dt_inativacao,"+
			                                  "st_chave) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdUsuario());
			pstmt.setInt(3,objeto.getTpChave());
			if(objeto.getBlbChavePublica()==null)
				pstmt.setNull(4, Types.BINARY);
			else
				pstmt.setBytes(4,objeto.getBlbChavePublica());
			if(objeto.getBlbChavePrivada()==null)
				pstmt.setNull(5, Types.BINARY);
			else
				pstmt.setBytes(5,objeto.getBlbChavePrivada());
			if(objeto.getDtCriacao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtCriacao().getTimeInMillis()));
			if(objeto.getDtInativacao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInativacao().getTimeInMillis()));
			pstmt.setInt(8,objeto.getStChave());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioChaveDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioChaveDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(UsuarioChave objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(UsuarioChave objeto, int cdChaveOld, int cdUsuarioOld) {
		return update(objeto, cdChaveOld, cdUsuarioOld, null);
	}

	public static int update(UsuarioChave objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(UsuarioChave objeto, int cdChaveOld, int cdUsuarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE seg_usuario_chave SET cd_chave=?,"+
												      		   "cd_usuario=?,"+
												      		   "tp_chave=?,"+
												      		   "blb_chave_publica=?,"+
												      		   "blb_chave_privada=?,"+
												      		   "dt_criacao=?,"+
												      		   "dt_inativacao=?,"+
												      		   "st_chave=? WHERE cd_chave=? AND cd_usuario=?");
			pstmt.setInt(1,objeto.getCdChave());
			pstmt.setInt(2,objeto.getCdUsuario());
			pstmt.setInt(3,objeto.getTpChave());
			if(objeto.getBlbChavePublica()==null)
				pstmt.setNull(4, Types.BINARY);
			else
				pstmt.setBytes(4,objeto.getBlbChavePublica());
			if(objeto.getBlbChavePrivada()==null)
				pstmt.setNull(5, Types.BINARY);
			else
				pstmt.setBytes(5,objeto.getBlbChavePrivada());
			if(objeto.getDtCriacao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtCriacao().getTimeInMillis()));
			if(objeto.getDtInativacao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInativacao().getTimeInMillis()));
			pstmt.setInt(8,objeto.getStChave());
			pstmt.setInt(9, cdChaveOld!=0 ? cdChaveOld : objeto.getCdChave());
			pstmt.setInt(10, cdUsuarioOld!=0 ? cdUsuarioOld : objeto.getCdUsuario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioChaveDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioChaveDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdChave, int cdUsuario) {
		return delete(cdChave, cdUsuario, null);
	}

	public static int delete(int cdChave, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM seg_usuario_chave WHERE cd_chave=? AND cd_usuario=?");
			pstmt.setInt(1, cdChave);
			pstmt.setInt(2, cdUsuario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioChaveDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioChaveDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static UsuarioChave get(int cdChave, int cdUsuario) {
		return get(cdChave, cdUsuario, null);
	}

	public static UsuarioChave get(int cdChave, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario_chave WHERE cd_chave=? AND cd_usuario=?");
			pstmt.setInt(1, cdChave);
			pstmt.setInt(2, cdUsuario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new UsuarioChave(rs.getInt("cd_chave"),
						rs.getInt("cd_usuario"),
						rs.getInt("tp_chave"),
						rs.getBytes("blb_chave_publica")==null?null:rs.getBytes("blb_chave_publica"),
						rs.getBytes("blb_chave_privada")==null?null:rs.getBytes("blb_chave_privada"),
						(rs.getTimestamp("dt_criacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_criacao").getTime()),
						(rs.getTimestamp("dt_inativacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inativacao").getTime()),
						rs.getInt("st_chave"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioChaveDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioChaveDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario_chave");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioChaveDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioChaveDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<UsuarioChave> getList() {
		return getList(null);
	}

	public static ArrayList<UsuarioChave> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<UsuarioChave> list = new ArrayList<UsuarioChave>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				UsuarioChave obj = UsuarioChaveDAO.get(rsm.getInt("cd_chave"), rsm.getInt("cd_usuario"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioChaveDAO.getList: " + e);
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
		return Search.find("SELECT * FROM seg_usuario_chave", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}