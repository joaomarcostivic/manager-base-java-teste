package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class LinhaRotaDAO{

	public static int insert(LinhaRota objeto) {
		return insert(objeto, null);
	}

	public static int insert(LinhaRota objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_linha");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdLinha()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_rota");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("mob_linha_rota", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRota(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_linha_rota (cd_linha,"+
			                                  "cd_rota,"+
			                                  "nm_rota,"+
			                                  "nr_rota,"+
			                                  "id_rota,"+
			                                  "st_rota,"+
			                                  "txt_observacao,"+
			                                  "qt_km,"+
			                                  "tp_rota) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdLinha()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdLinha());
			pstmt.setInt(2, code);
			pstmt.setString(3,objeto.getNmRota());
			pstmt.setInt(4,objeto.getNrRota());
			pstmt.setString(5,objeto.getIdRota());
			pstmt.setInt(6,objeto.getStRota());
			pstmt.setString(7,objeto.getTxtObservacao());
			pstmt.setDouble(8,objeto.getQtKm());
			pstmt.setInt(9,objeto.getTpRota());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaRotaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaRotaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LinhaRota objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(LinhaRota objeto, int cdLinhaOld, int cdRotaOld) {
		return update(objeto, cdLinhaOld, cdRotaOld, null);
	}

	public static int update(LinhaRota objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(LinhaRota objeto, int cdLinhaOld, int cdRotaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_linha_rota SET cd_linha=?,"+
												      		   "cd_rota=?,"+
												      		   "nm_rota=?,"+
												      		   "nr_rota=?,"+
												      		   "id_rota=?,"+
												      		   "st_rota=?,"+
												      		   "txt_observacao=?,"+
												      		   "qt_km=?,"+
												      		   "tp_rota=? WHERE cd_linha=? AND cd_rota=?");
			pstmt.setInt(1,objeto.getCdLinha());
			pstmt.setInt(2,objeto.getCdRota());
			pstmt.setString(3,objeto.getNmRota());
			pstmt.setInt(4,objeto.getNrRota());
			pstmt.setString(5,objeto.getIdRota());
			pstmt.setInt(6,objeto.getStRota());
			pstmt.setString(7,objeto.getTxtObservacao());
			pstmt.setDouble(8,objeto.getQtKm());
			pstmt.setInt(9,objeto.getTpRota());
			pstmt.setInt(10, cdLinhaOld!=0 ? cdLinhaOld : objeto.getCdLinha());
			pstmt.setInt(11, cdRotaOld!=0 ? cdRotaOld : objeto.getCdRota());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaRotaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaRotaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLinha, int cdRota) {
		return delete(cdLinha, cdRota, null);
	}

	public static int delete(int cdLinha, int cdRota, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_linha_rota WHERE cd_linha=? AND cd_rota=?");
			pstmt.setInt(1, cdLinha);
			pstmt.setInt(2, cdRota);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaRotaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaRotaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LinhaRota get(int cdLinha, int cdRota) {
		return get(cdLinha, cdRota, null);
	}

	public static LinhaRota get(int cdLinha, int cdRota, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_linha_rota WHERE cd_linha=? AND cd_rota=?");
			pstmt.setInt(1, cdLinha);
			pstmt.setInt(2, cdRota);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LinhaRota(rs.getInt("cd_linha"),
						rs.getInt("cd_rota"),
						rs.getString("nm_rota"),
						rs.getInt("nr_rota"),
						rs.getString("id_rota"),
						rs.getInt("st_rota"),
						rs.getString("txt_observacao"),
						rs.getDouble("qt_km"),
						rs.getInt("tp_rota"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaRotaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaRotaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_linha_rota");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaRotaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaRotaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<LinhaRota> getList() {
		return getList(null);
	}

	public static ArrayList<LinhaRota> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<LinhaRota> list = new ArrayList<LinhaRota>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				LinhaRota obj = LinhaRotaDAO.get(rsm.getInt("cd_linha"), rsm.getInt("cd_rota"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaRotaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_linha_rota", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}