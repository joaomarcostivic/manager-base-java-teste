package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class FiltroCampoDAO{

	public static int insert(FiltroCampo objeto) {
		return insert(objeto, null);
	}

	public static int insert(FiltroCampo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_filtro_campo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCampo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_filtro_campo (cd_campo,"+
			                                  "nm_campo,"+
			                                  "id_campo,"+
			                                  "nm_tabela,"+
			                                  "tp_relatorio,"+
			                                  "tp_campo) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmCampo());
			pstmt.setString(3,objeto.getIdCampo());
			pstmt.setString(4,objeto.getNmTabela());
			pstmt.setInt(5,objeto.getTpRelatorio());
			pstmt.setInt(6,objeto.getTpCampo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FiltroCampoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FiltroCampoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FiltroCampo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(FiltroCampo objeto, int cdCampoOld) {
		return update(objeto, cdCampoOld, null);
	}

	public static int update(FiltroCampo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(FiltroCampo objeto, int cdCampoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_filtro_campo SET cd_campo=?,"+
												      		   "nm_campo=?,"+
												      		   "id_campo=?,"+
												      		   "nm_tabela=?,"+
												      		   "tp_relatorio=?,"+
												      		   "tp_campo=? WHERE cd_campo=?");
			pstmt.setInt(1,objeto.getCdCampo());
			pstmt.setString(2,objeto.getNmCampo());
			pstmt.setString(3,objeto.getIdCampo());
			pstmt.setString(4,objeto.getNmTabela());
			pstmt.setInt(5,objeto.getTpRelatorio());
			pstmt.setInt(6,objeto.getTpCampo());
			pstmt.setInt(7, cdCampoOld!=0 ? cdCampoOld : objeto.getCdCampo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FiltroCampoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FiltroCampoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCampo) {
		return delete(cdCampo, null);
	}

	public static int delete(int cdCampo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_filtro_campo WHERE cd_campo=?");
			pstmt.setInt(1, cdCampo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FiltroCampoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FiltroCampoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FiltroCampo get(int cdCampo) {
		return get(cdCampo, null);
	}

	public static FiltroCampo get(int cdCampo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_filtro_campo WHERE cd_campo=?");
			pstmt.setInt(1, cdCampo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FiltroCampo(rs.getInt("cd_campo"),
						rs.getString("nm_campo"),
						rs.getString("id_campo"),
						rs.getString("nm_tabela"),
						rs.getInt("tp_relatorio"),
						rs.getInt("tp_campo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FiltroCampoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FiltroCampoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_filtro_campo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FiltroCampoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FiltroCampoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<FiltroCampo> getList() {
		return getList(null);
	}

	public static ArrayList<FiltroCampo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<FiltroCampo> list = new ArrayList<FiltroCampo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				FiltroCampo obj = FiltroCampoDAO.get(rsm.getInt("cd_campo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FiltroCampoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_filtro_campo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}