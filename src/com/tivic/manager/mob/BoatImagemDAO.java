package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class BoatImagemDAO{

	public static int insert(BoatImagem objeto) {
		return insert(objeto, null);
	}

	public static int insert(BoatImagem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_imagem");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_boat");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdBoat()));
			int code = Conexao.getSequenceCode("mob_boat_imagem", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdImagem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_boat_imagem (cd_imagem,"+
			                                  "cd_boat,"+
			                                  "blb_imagem,"+
			                                  "tp_posicao,"+
			                                  "cd_boat_veiculo) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdBoat()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdBoat());
			if(objeto.getBlbImagem()==null)
				pstmt.setNull(3, Types.BINARY);
			else
				pstmt.setBytes(3,objeto.getBlbImagem());
			pstmt.setInt(4,objeto.getTpPosicao());
			pstmt.setInt(5,objeto.getCdBoatVeiculo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatImagemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatImagemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(BoatImagem objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(BoatImagem objeto, int cdImagemOld, int cdBoatOld) {
		return update(objeto, cdImagemOld, cdBoatOld, null);
	}

	public static int update(BoatImagem objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(BoatImagem objeto, int cdImagemOld, int cdBoatOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_boat_imagem SET cd_imagem=?,"+
												      		   "cd_boat=?,"+
												      		   "blb_imagem=?,"+
												      		   "tp_posicao=?,"+
												      		   "cd_boat_veiculo=? WHERE cd_imagem=? AND cd_boat=?");
			pstmt.setInt(1,objeto.getCdImagem());
			pstmt.setInt(2,objeto.getCdBoat());
			if(objeto.getBlbImagem()==null)
				pstmt.setNull(3, Types.BINARY);
			else
				pstmt.setBytes(3,objeto.getBlbImagem());
			pstmt.setInt(4,objeto.getTpPosicao());
			pstmt.setInt(5,objeto.getCdBoatVeiculo());
			pstmt.setInt(6, cdImagemOld!=0 ? cdImagemOld : objeto.getCdImagem());
			pstmt.setInt(7, cdBoatOld!=0 ? cdBoatOld : objeto.getCdBoat());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatImagemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatImagemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdImagem, int cdBoat) {
		return delete(cdImagem, cdBoat, null);
	}

	public static int delete(int cdImagem, int cdBoat, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_boat_imagem WHERE cd_imagem=? AND cd_boat=?");
			pstmt.setInt(1, cdImagem);
			pstmt.setInt(2, cdBoat);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatImagemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatImagemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static BoatImagem get(int cdImagem, int cdBoat) {
		return get(cdImagem, cdBoat, null);
	}

	public static BoatImagem get(int cdImagem, int cdBoat, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_imagem WHERE cd_imagem=? AND cd_boat=?");
			pstmt.setInt(1, cdImagem);
			pstmt.setInt(2, cdBoat);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new BoatImagem(rs.getInt("cd_imagem"),
						rs.getInt("cd_boat"),
						rs.getBytes("blb_imagem")==null?null:rs.getBytes("blb_imagem"),
						rs.getInt("tp_posicao"),
						rs.getInt("cd_boat_veiculo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatImagemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatImagemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_imagem");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatImagemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatImagemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<BoatImagem> getList() {
		return getList(null);
	}

	public static ArrayList<BoatImagem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<BoatImagem> list = new ArrayList<BoatImagem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				BoatImagem obj = BoatImagemDAO.get(rsm.getInt("cd_imagem"), rsm.getInt("cd_boat"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatImagemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_boat_imagem", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
