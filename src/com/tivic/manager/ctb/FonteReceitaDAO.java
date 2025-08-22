package com.tivic.manager.ctb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class FonteReceitaDAO{

	public static int insert(FonteReceita objeto) {
		return insert(objeto, null);
	}

	public static int insert(FonteReceita objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ctb_fonte_receita", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFonteReceita(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ctb_fonte_receita (cd_fonte_receita,"+
			                                  "nm_fonte_receita,"+
			                                  "txt_observacao,"+
			                                  "id_fonte_receita,"+
			                                  "st_fonte_receita) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmFonteReceita());
			pstmt.setString(3,objeto.getTxtObservacao());
			pstmt.setString(4,objeto.getIdFonteReceita());
			pstmt.setInt(5,objeto.getStFonteReceita());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FonteReceitaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FonteReceitaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FonteReceita objeto) {
		return update(objeto, 0, null);
	}

	public static int update(FonteReceita objeto, int cdFonteReceitaOld) {
		return update(objeto, cdFonteReceitaOld, null);
	}

	public static int update(FonteReceita objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(FonteReceita objeto, int cdFonteReceitaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ctb_fonte_receita SET cd_fonte_receita=?,"+
												      		   "nm_fonte_receita=?,"+
												      		   "txt_observacao=?,"+
												      		   "id_fonte_receita=?,"+
												      		   "st_fonte_receita=? WHERE cd_fonte_receita=?");
			pstmt.setInt(1,objeto.getCdFonteReceita());
			pstmt.setString(2,objeto.getNmFonteReceita());
			pstmt.setString(3,objeto.getTxtObservacao());
			pstmt.setString(4,objeto.getIdFonteReceita());
			pstmt.setInt(5,objeto.getStFonteReceita());
			pstmt.setInt(6, cdFonteReceitaOld!=0 ? cdFonteReceitaOld : objeto.getCdFonteReceita());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FonteReceitaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FonteReceitaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFonteReceita) {
		return delete(cdFonteReceita, null);
	}

	public static int delete(int cdFonteReceita, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ctb_fonte_receita WHERE cd_fonte_receita=?");
			pstmt.setInt(1, cdFonteReceita);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FonteReceitaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FonteReceitaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FonteReceita get(int cdFonteReceita) {
		return get(cdFonteReceita, null);
	}

	public static FonteReceita get(int cdFonteReceita, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ctb_fonte_receita WHERE cd_fonte_receita=?");
			pstmt.setInt(1, cdFonteReceita);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FonteReceita(rs.getInt("cd_fonte_receita"),
						rs.getString("nm_fonte_receita"),
						rs.getString("txt_observacao"),
						rs.getString("id_fonte_receita"),
						rs.getInt("st_fonte_receita"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FonteReceitaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FonteReceitaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_fonte_receita");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FonteReceitaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FonteReceitaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<FonteReceita> getList() {
		return getList(null);
	}

	public static ArrayList<FonteReceita> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<FonteReceita> list = new ArrayList<FonteReceita>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				FonteReceita obj = FonteReceitaDAO.get(rsm.getInt("cd_fonte_receita"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FonteReceitaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ctb_fonte_receita", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
