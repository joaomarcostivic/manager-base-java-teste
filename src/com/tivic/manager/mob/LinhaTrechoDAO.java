package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class LinhaTrechoDAO{

	public static int insert(LinhaTrecho objeto) {
		return insert(objeto, null);
	}

	public static int insert(LinhaTrecho objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_linha");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdLinha()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_rota");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdRota()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_trecho");
			keys[2].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("mob_linha_trecho", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTrecho(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_linha_trecho (cd_linha,"+
			                                  "cd_rota,"+
			                                  "cd_trecho,"+
			                                  "cd_trecho_anterior,"+
			                                  "cd_parada,"+
			                                  "cd_georreferencia,"+
			                                  "qt_km) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdLinha()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdLinha());
			if(objeto.getCdRota()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdRota());
			pstmt.setInt(3, code);
			if(objeto.getCdTrechoAnterior()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTrechoAnterior());
			if(objeto.getCdParada()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdParada());
			if(objeto.getCdGeorreferencia()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdGeorreferencia());
			pstmt.setDouble(7,objeto.getQtKm());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaTrechoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaTrechoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LinhaTrecho objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(LinhaTrecho objeto, int cdLinhaOld, int cdRotaOld, int cdTrechoOld) {
		return update(objeto, cdLinhaOld, cdRotaOld, cdTrechoOld, null);
	}

	public static int update(LinhaTrecho objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(LinhaTrecho objeto, int cdLinhaOld, int cdRotaOld, int cdTrechoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_linha_trecho SET cd_linha=?,"+
												      		   "cd_rota=?,"+
												      		   "cd_trecho=?,"+
												      		   "cd_trecho_anterior=?,"+
												      		   "cd_parada=?,"+
												      		   "cd_georreferencia=?,"+
												      		   "qt_km=? WHERE cd_linha=? AND cd_rota=? AND cd_trecho=?");
			pstmt.setInt(1,objeto.getCdLinha());
			pstmt.setInt(2,objeto.getCdRota());
			pstmt.setInt(3,objeto.getCdTrecho());
			if(objeto.getCdTrechoAnterior()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTrechoAnterior());
			if(objeto.getCdParada()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdParada());
			if(objeto.getCdGeorreferencia()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdGeorreferencia());
			pstmt.setDouble(7,objeto.getQtKm());
			pstmt.setInt(8, cdLinhaOld!=0 ? cdLinhaOld : objeto.getCdLinha());
			pstmt.setInt(9, cdRotaOld!=0 ? cdRotaOld : objeto.getCdRota());
			pstmt.setInt(10, cdTrechoOld!=0 ? cdTrechoOld : objeto.getCdTrecho());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaTrechoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaTrechoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLinha, int cdRota, int cdTrecho) {
		return delete(cdLinha, cdRota, cdTrecho, null);
	}

	public static int delete(int cdLinha, int cdRota, int cdTrecho, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_linha_trecho WHERE cd_linha=? AND cd_rota=? AND cd_trecho=?");
			pstmt.setInt(1, cdLinha);
			pstmt.setInt(2, cdRota);
			pstmt.setInt(3, cdTrecho);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaTrechoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaTrechoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int deleteItinerario(int cdLinha, int cdRota) {
		return deleteItinerario(cdLinha, cdRota, null);
	}

	public static int deleteItinerario(int cdLinha, int cdRota, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_linha_trecho WHERE cd_linha=? AND cd_rota=?");
			pstmt.setInt(1, cdLinha);
			pstmt.setInt(2, cdRota);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaTrechoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaTrechoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LinhaTrecho get(int cdLinha, int cdRota, int cdTrecho) {
		return get(cdLinha, cdRota, cdTrecho, null);
	}

	public static LinhaTrecho get(int cdLinha, int cdRota, int cdTrecho, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_linha_trecho WHERE cd_linha=? AND cd_rota=? AND cd_trecho=?");
			pstmt.setInt(1, cdLinha);
			pstmt.setInt(2, cdRota);
			pstmt.setInt(3, cdTrecho);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LinhaTrecho(rs.getInt("cd_linha"),
						rs.getInt("cd_rota"),
						rs.getInt("cd_trecho"),
						rs.getInt("cd_trecho_anterior"),
						rs.getInt("cd_parada"),
						rs.getInt("cd_georreferencia"),
						rs.getDouble("qt_km"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaTrechoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaTrechoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_linha_trecho");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaTrechoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaTrechoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<LinhaTrecho> getList() {
		return getList(null);
	}

	public static ArrayList<LinhaTrecho> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<LinhaTrecho> list = new ArrayList<LinhaTrecho>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				LinhaTrecho obj = LinhaTrechoDAO.get(rsm.getInt("cd_linha"), rsm.getInt("cd_rota"), rsm.getInt("cd_trecho"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaTrechoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_linha_trecho", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}