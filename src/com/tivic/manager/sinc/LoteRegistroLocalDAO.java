package com.tivic.manager.sinc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class LoteRegistroLocalDAO{

	public static int insert(LoteRegistroLocal objeto) {
		return insert(objeto, null);
	}

	public static int insert(LoteRegistroLocal objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO sinc_lote_registro_local (cd_lote_registro,"+
			                                  "cd_local,"+
			                                  "dt_sincronizacao,"+
			                                  "nm_chave_local) VALUES (?, ?, ?, ?)");
			if(objeto.getCdLoteRegistro()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdLoteRegistro());
			if(objeto.getCdLocal()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdLocal());
			if(objeto.getDtSincronizacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtSincronizacao().getTimeInMillis()));
			pstmt.setString(4,objeto.getNmChaveLocal());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroLocalDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroLocalDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LoteRegistroLocal objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(LoteRegistroLocal objeto, int cdLoteRegistroOld, int cdLocalOld) {
		return update(objeto, cdLoteRegistroOld, cdLocalOld, null);
	}

	public static int update(LoteRegistroLocal objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(LoteRegistroLocal objeto, int cdLoteRegistroOld, int cdLocalOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE sinc_lote_registro_local SET cd_lote_registro=?,"+
												      		   "cd_local=?,"+
												      		   "dt_sincronizacao=?,"+
												      		   "nm_chave_local=? WHERE cd_lote_registro=? AND cd_local=?");
			pstmt.setInt(1,objeto.getCdLoteRegistro());
			pstmt.setInt(2,objeto.getCdLocal());
			if(objeto.getDtSincronizacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtSincronizacao().getTimeInMillis()));
			pstmt.setString(4,objeto.getNmChaveLocal());
			pstmt.setInt(5, cdLoteRegistroOld!=0 ? cdLoteRegistroOld : objeto.getCdLoteRegistro());
			pstmt.setInt(6, cdLocalOld!=0 ? cdLocalOld : objeto.getCdLocal());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroLocalDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroLocalDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLoteRegistro, int cdLote, int cdLocal) {
		return delete(cdLoteRegistro, cdLote, cdLocal, null);
	}

	public static int delete(int cdLoteRegistro, int cdLote, int cdLocal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM sinc_lote_registro_local WHERE cd_lote_registro=? AND cd_lote=? AND cd_local=?");
			pstmt.setInt(1, cdLoteRegistro);
			pstmt.setInt(2, cdLote);
			pstmt.setInt(3, cdLocal);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroLocalDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroLocalDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LoteRegistroLocal get(int cdLoteRegistro, int cdLote, int cdLocal) {
		return get(cdLoteRegistro, cdLote, cdLocal, null);
	}

	public static LoteRegistroLocal get(int cdLoteRegistro, int cdLote, int cdLocal, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM sinc_lote_registro_local WHERE cd_lote_registro=? AND cd_lote=? AND cd_local=?");
			pstmt.setInt(1, cdLoteRegistro);
			pstmt.setInt(2, cdLote);
			pstmt.setInt(3, cdLocal);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LoteRegistroLocal(rs.getInt("cd_lote_registro"),
						rs.getInt("cd_local"),
						(rs.getTimestamp("dt_sincronizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_sincronizacao").getTime()),
						rs.getString("nm_chave_local"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroLocalDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroLocalDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM sinc_lote_registro_local");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroLocalDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroLocalDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<LoteRegistroLocal> getList() {
		return getList(null);
	}

	public static ArrayList<LoteRegistroLocal> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<LoteRegistroLocal> list = new ArrayList<LoteRegistroLocal>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				LoteRegistroLocal obj = LoteRegistroLocalDAO.get(rsm.getInt("cd_lote_registro"), rsm.getInt("cd_lote"), rsm.getInt("cd_local"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroLocalDAO.getList: " + e);
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
		return Search.find("SELECT * FROM sinc_lote_registro_local", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
