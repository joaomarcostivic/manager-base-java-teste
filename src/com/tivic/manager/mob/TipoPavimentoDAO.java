package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoPavimentoDAO{

	public static int insert(TipoPavimento objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoPavimento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_tipo_pavimento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoPavimento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_tipo_pavimento (cd_tipo_pavimento,"+
			                                  "nm_tipo_pavimento,"+
			                                  "id_tipo_pavimento) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoPavimento());
			pstmt.setString(3,objeto.getIdTipoPavimento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoPavimentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPavimentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoPavimento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoPavimento objeto, int cdTipoPavimentoOld) {
		return update(objeto, cdTipoPavimentoOld, null);
	}

	public static int update(TipoPavimento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoPavimento objeto, int cdTipoPavimentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_tipo_pavimento SET cd_tipo_pavimento=?,"+
												      		   "nm_tipo_pavimento=?,"+
												      		   "id_tipo_pavimento=? WHERE cd_tipo_pavimento=?");
			pstmt.setInt(1,objeto.getCdTipoPavimento());
			pstmt.setString(2,objeto.getNmTipoPavimento());
			pstmt.setString(3,objeto.getIdTipoPavimento());
			pstmt.setInt(4, cdTipoPavimentoOld!=0 ? cdTipoPavimentoOld : objeto.getCdTipoPavimento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoPavimentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPavimentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoPavimento) {
		return delete(cdTipoPavimento, null);
	}

	public static int delete(int cdTipoPavimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_tipo_pavimento WHERE cd_tipo_pavimento=?");
			pstmt.setInt(1, cdTipoPavimento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoPavimentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPavimentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoPavimento get(int cdTipoPavimento) {
		return get(cdTipoPavimento, null);
	}

	public static TipoPavimento get(int cdTipoPavimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_tipo_pavimento WHERE cd_tipo_pavimento=?");
			pstmt.setInt(1, cdTipoPavimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoPavimento(rs.getInt("cd_tipo_pavimento"),
						rs.getString("nm_tipo_pavimento"),
						rs.getString("id_tipo_pavimento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoPavimentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPavimentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_tipo_pavimento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoPavimentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPavimentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoPavimento> getList() {
		return getList(null);
	}

	public static ArrayList<TipoPavimento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoPavimento> list = new ArrayList<TipoPavimento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoPavimento obj = TipoPavimentoDAO.get(rsm.getInt("cd_tipo_pavimento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPavimentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_tipo_pavimento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}