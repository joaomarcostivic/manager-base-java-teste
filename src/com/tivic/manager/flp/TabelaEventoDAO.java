package com.tivic.manager.flp;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TabelaEventoDAO{

	public static int insert(TabelaEvento objeto) {
		return insert(objeto, null);
	}

	public static int insert(TabelaEvento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("flp_tabela_evento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTabelaEvento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO flp_tabela_evento (cd_tabela_evento,"+
			                                  "nm_tabela_evento,"+
			                                  "id_tabela_evento) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTabelaEvento());
			pstmt.setString(3,objeto.getIdTabelaEvento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaEventoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaEventoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TabelaEvento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TabelaEvento objeto, int cdTabelaEventoOld) {
		return update(objeto, cdTabelaEventoOld, null);
	}

	public static int update(TabelaEvento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TabelaEvento objeto, int cdTabelaEventoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE flp_tabela_evento SET cd_tabela_evento=?,"+
												      		   "nm_tabela_evento=?,"+
												      		   "id_tabela_evento=? WHERE cd_tabela_evento=?");
			pstmt.setInt(1,objeto.getCdTabelaEvento());
			pstmt.setString(2,objeto.getNmTabelaEvento());
			pstmt.setString(3,objeto.getIdTabelaEvento());
			pstmt.setInt(4, cdTabelaEventoOld!=0 ? cdTabelaEventoOld : objeto.getCdTabelaEvento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaEventoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaEventoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTabelaEvento) {
		return delete(cdTabelaEvento, null);
	}

	public static int delete(int cdTabelaEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM flp_tabela_evento WHERE cd_tabela_evento=?");
			pstmt.setInt(1, cdTabelaEvento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaEventoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaEventoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TabelaEvento get(int cdTabelaEvento) {
		return get(cdTabelaEvento, null);
	}

	public static TabelaEvento get(int cdTabelaEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM flp_tabela_evento WHERE cd_tabela_evento=?");
			pstmt.setInt(1, cdTabelaEvento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TabelaEvento(rs.getInt("cd_tabela_evento"),
						rs.getString("nm_tabela_evento"),
						rs.getString("id_tabela_evento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaEventoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaEventoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM flp_tabela_evento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaEventoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaEventoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TabelaEvento> getList() {
		return getList(null);
	}

	public static ArrayList<TabelaEvento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TabelaEvento> list = new ArrayList<TabelaEvento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TabelaEvento obj = TabelaEventoDAO.get(rsm.getInt("cd_tabela_evento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaEventoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM flp_tabela_evento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
