package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class RrdImagemDAO{

	public static int insert(RrdImagem objeto) {
		return insert(objeto, null);
	}

	public static int insert(RrdImagem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_imagem");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_rrd");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdRrd()));
			int code = Conexao.getSequenceCode("mob_rrd_imagem", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdImagem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_rrd_imagem (cd_imagem,"+
			                                  "cd_rrd,"+
			                                  "blb_imagem,"+
			                                  "tp_imagem,"+
			                                  "lg_impressao) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdRrd()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdRrd());
			if(objeto.getBlbImagem()==null)
				pstmt.setNull(3, Types.BINARY);
			else
				pstmt.setBytes(3,objeto.getBlbImagem());
			pstmt.setInt(4,objeto.getTpImagem());
			pstmt.setInt(5,objeto.getLgImpressao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdImagemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdImagemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(RrdImagem objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(RrdImagem objeto, int cdImagemOld, int cdRrdOld) {
		return update(objeto, cdImagemOld, cdRrdOld, null);
	}

	public static int update(RrdImagem objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(RrdImagem objeto, int cdImagemOld, int cdRrdOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_rrd_imagem SET cd_imagem=?,"+
												      		   "cd_rrd=?,"+
												      		   "blb_imagem=?,"+
												      		   "tp_imagem=?,"+
												      		   "lg_impressao=? WHERE cd_imagem=? AND cd_rrd=?");
			pstmt.setInt(1,objeto.getCdImagem());
			pstmt.setInt(2,objeto.getCdRrd());
			if(objeto.getBlbImagem()==null)
				pstmt.setNull(3, Types.BINARY);
			else
				pstmt.setBytes(3,objeto.getBlbImagem());
			pstmt.setInt(4,objeto.getTpImagem());
			pstmt.setInt(5,objeto.getLgImpressao());
			pstmt.setInt(6, cdImagemOld!=0 ? cdImagemOld : objeto.getCdImagem());
			pstmt.setInt(7, cdRrdOld!=0 ? cdRrdOld : objeto.getCdRrd());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdImagemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdImagemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdImagem, int cdRrd) {
		return delete(cdImagem, cdRrd, null);
	}

	public static int delete(int cdImagem, int cdRrd, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_rrd_imagem WHERE cd_imagem=? AND cd_rrd=?");
			pstmt.setInt(1, cdImagem);
			pstmt.setInt(2, cdRrd);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdImagemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdImagemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static RrdImagem get(int cdImagem, int cdRrd) {
		return get(cdImagem, cdRrd, null);
	}

	public static RrdImagem get(int cdImagem, int cdRrd, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_rrd_imagem WHERE cd_imagem=? AND cd_rrd=?");
			pstmt.setInt(1, cdImagem);
			pstmt.setInt(2, cdRrd);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new RrdImagem(rs.getInt("cd_imagem"),
						rs.getInt("cd_rrd"),
						rs.getBytes("blb_imagem")==null?null:rs.getBytes("blb_imagem"),
						rs.getInt("tp_imagem"),
						rs.getInt("lg_impressao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdImagemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdImagemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_rrd_imagem");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdImagemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdImagemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<RrdImagem> getList() {
		return getList(null);
	}

	public static ArrayList<RrdImagem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<RrdImagem> list = new ArrayList<RrdImagem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				RrdImagem obj = RrdImagemDAO.get(rsm.getInt("cd_imagem"), rsm.getInt("cd_rrd"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdImagemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_rrd_imagem", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
