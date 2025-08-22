package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class AitImagemDAO{

	public static int insert(AitImagem objeto) {
		return insert(objeto, null);
	}

	public static int insert(AitImagem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_imagem");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_ait");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdAit()));
			int code = Conexao.getSequenceCode("mob_ait_imagem", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdImagem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_ait_imagem (cd_imagem,"+
			                                  "cd_ait,"+
			                                  "blb_imagem,"+
			                                  "lg_impressao,"+
			                                  "tp_imagem) VALUES (?, ?, ?, ?, ?)");
			
			pstmt.setInt(1, code);
			if(objeto.getCdAit()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAit());
			if(objeto.getBlbImagem()==null)
				pstmt.setNull(3, Types.BINARY);
			else
				pstmt.setBytes(3,objeto.getBlbImagem());
			pstmt.setInt(4,objeto.getLgImpressao());
			pstmt.setInt(5,objeto.getTpImagem());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AitImagem objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AitImagem objeto, int cdImagemOld, int cdAitOld) {
		return update(objeto, cdImagemOld, cdAitOld, null);
	}

	public static int update(AitImagem objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AitImagem objeto, int cdImagemOld, int cdAitOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_ait_imagem SET cd_imagem=?,"+
												      		   "cd_ait=?,"+
												      		   "blb_imagem=?,"+
												      		   "tp_imagem=? WHERE cd_imagem=? AND cd_ait=?");
			pstmt.setInt(1,objeto.getCdImagem());
			pstmt.setInt(2,objeto.getCdAit());
			if(objeto.getBlbImagem()==null)
				pstmt.setNull(3, Types.BINARY);
			else
				pstmt.setBytes(3,objeto.getBlbImagem());
			pstmt.setInt(4,objeto.getTpImagem());
			pstmt.setInt(5, cdImagemOld!=0 ? cdImagemOld : objeto.getCdImagem());
			pstmt.setInt(6, cdAitOld!=0 ? cdAitOld : objeto.getCdAit());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdImagem, int cdAit) {
		return delete(cdImagem, cdAit, null);
	}

	public static int delete(int cdImagem, int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_ait_imagem WHERE cd_imagem=? AND cd_ait=?");
			pstmt.setInt(1, cdImagem);
			pstmt.setInt(2, cdAit);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitImagem get(int cdImagem, int cdAit) {
		return get(cdImagem, cdAit, null);
	}

	public static AitImagem get(int cdImagem, int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA"); 
		
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			
			if (lgBaseAntiga)
			{
				pstmt = connect.prepareStatement("SELECT * FROM str_ait_imagem WHERE cd_imagem=? AND cd_ait=?");
				pstmt.setInt(1, cdImagem);
				pstmt.setInt(2, cdAit);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return new AitImagem(rs.getInt("cd_imagem"),
							rs.getInt("cd_ait"),
							rs.getBytes("blb_imagem")==null?null:rs.getBytes("blb_imagem"),
							0,
							0);
				}
				else{
					return null;
				}
			}
			else
			{
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_imagem WHERE cd_imagem=? AND cd_ait=?");
			pstmt.setInt(1, cdImagem);
			pstmt.setInt(2, cdAit);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AitImagem(rs.getInt("cd_imagem"),
						rs.getInt("cd_ait"),
						rs.getBytes("blb_imagem")==null?null:rs.getBytes("blb_imagem"),
						rs.getInt("lg_impressao"),
						rs.getInt("tp_imagem"));
			}
			else{
				return null;
			}
		}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_imagem");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AitImagem> getList() {
		return getList(null);
	}

	public static ArrayList<AitImagem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AitImagem> list = new ArrayList<AitImagem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AitImagem obj = AitImagemDAO.get(rsm.getInt("cd_imagem"), rsm.getInt("cd_ait"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_ait_imagem", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

