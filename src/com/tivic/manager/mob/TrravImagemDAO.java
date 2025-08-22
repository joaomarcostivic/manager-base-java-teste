package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class TrravImagemDAO{

	public static int insert(TrravImagem objeto) {
		return insert(objeto, null);
	}

	public static int insert(TrravImagem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_imagem");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_trrav");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdTrrav()));
			int code = Conexao.getSequenceCode("mob_trrav_imagem", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdImagem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_trrav_imagem (cd_imagem,"+
			                                  "cd_trrav,"+
			                                  "blb_imagem,"+
			                                  "tp_imagem,"+
			                                  "lg_impressao) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTrrav()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTrrav());
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
			System.err.println("Erro! TrravImagemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravImagemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TrravImagem objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TrravImagem objeto, int cdImagemOld, int cdTrravOld) {
		return update(objeto, cdImagemOld, cdTrravOld, null);
	}

	public static int update(TrravImagem objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TrravImagem objeto, int cdImagemOld, int cdTrravOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_trrav_imagem SET cd_imagem=?,"+
												      		   "cd_trrav=?,"+
												      		   "blb_imagem=?,"+
												      		   "tp_imagem=?,"+
												      		   "lg_impressao=? WHERE cd_imagem=? AND cd_trrav=?");
			pstmt.setInt(1,objeto.getCdImagem());
			pstmt.setInt(2,objeto.getCdTrrav());
			if(objeto.getBlbImagem()==null)
				pstmt.setNull(3, Types.BINARY);
			else
				pstmt.setBytes(3,objeto.getBlbImagem());
			pstmt.setInt(4,objeto.getTpImagem());
			pstmt.setInt(5,objeto.getLgImpressao());
			pstmt.setInt(6, cdImagemOld!=0 ? cdImagemOld : objeto.getCdImagem());
			pstmt.setInt(7, cdTrravOld!=0 ? cdTrravOld : objeto.getCdTrrav());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrravImagemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravImagemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdImagem, int cdTrrav) {
		return delete(cdImagem, cdTrrav, null);
	}

	public static int delete(int cdImagem, int cdTrrav, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_trrav_imagem WHERE cd_imagem=? AND cd_trrav=?");
			pstmt.setInt(1, cdImagem);
			pstmt.setInt(2, cdTrrav);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrravImagemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravImagemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TrravImagem get(int cdImagem, int cdTrrav) {
		return get(cdImagem, cdTrrav, null);
	}

	public static TrravImagem get(int cdImagem, int cdTrrav, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_trrav_imagem WHERE cd_imagem=? AND cd_trrav=?");
			pstmt.setInt(1, cdImagem);
			pstmt.setInt(2, cdTrrav);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TrravImagem(rs.getInt("cd_imagem"),
						rs.getInt("cd_trrav"),
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
			System.err.println("Erro! TrravImagemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravImagemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_trrav_imagem");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrravImagemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravImagemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TrravImagem> getList() {
		return getList(null);
	}

	public static ArrayList<TrravImagem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TrravImagem> list = new ArrayList<TrravImagem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TrravImagem obj = TrravImagemDAO.get(rsm.getInt("cd_imagem"), rsm.getInt("cd_trrav"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravImagemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_trrav_imagem", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}