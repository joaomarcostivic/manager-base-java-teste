package com.tivic.manager.sinc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class TabelaDAO{

	public static int insert(Tabela objeto) {
		return insert(objeto, null);
	}

	public static int insert(Tabela objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("sinc_tabela", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTabela(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO sinc_tabela (cd_tabela,"+
			                                  "nm_tabela,"+
			                                  "dt_inicio,"+
			                                  "st_sincronizacao,"+
			                                  "nm_campo_unico,"+
			                                  "nm_campo_chave,"+
			                                  "nm_campo_nao_nulo,"+
			                                  "tp_sincronizacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTabela());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			pstmt.setInt(4,objeto.getStSincronizacao());
			pstmt.setString(5,objeto.getNmCampoUnico());
			pstmt.setString(6,objeto.getNmCampoChave());
			pstmt.setString(7,objeto.getNmCampoNaoNulo());
			pstmt.setInt(8,objeto.getTpSincronizacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Tabela objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Tabela objeto, int cdTabelaOld) {
		return update(objeto, cdTabelaOld, null);
	}

	public static int update(Tabela objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Tabela objeto, int cdTabelaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE sinc_tabela SET cd_tabela=?,"+
												      		   "nm_tabela=?,"+
												      		   "dt_inicio=?,"+
												      		   "st_sincronizacao=?,"+
												      		   "nm_campo_unico=?,"+
												      		   "nm_campo_chave=?,"+
												      		   "nm_campo_nao_nulo=?,"+
												      		   "tp_sincronizacao=? WHERE cd_tabela=?");
			pstmt.setInt(1,objeto.getCdTabela());
			pstmt.setString(2,objeto.getNmTabela());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			pstmt.setInt(4,objeto.getStSincronizacao());
			pstmt.setString(5,objeto.getNmCampoUnico());
			pstmt.setString(6,objeto.getNmCampoChave());
			pstmt.setString(7,objeto.getNmCampoNaoNulo());
			pstmt.setInt(8,objeto.getTpSincronizacao());
			pstmt.setInt(9, cdTabelaOld!=0 ? cdTabelaOld : objeto.getCdTabela());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTabela) {
		return delete(cdTabela, null);
	}

	public static int delete(int cdTabela, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM sinc_tabela WHERE cd_tabela=?");
			pstmt.setInt(1, cdTabela);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Tabela get(int cdTabela) {
		return get(cdTabela, null);
	}

	public static Tabela get(int cdTabela, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM sinc_tabela WHERE cd_tabela=?");
			pstmt.setInt(1, cdTabela);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Tabela(rs.getInt("cd_tabela"),
						rs.getString("nm_tabela"),
						(rs.getTimestamp("dt_inicio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio").getTime()),
						rs.getInt("st_sincronizacao"),
						rs.getString("nm_campo_unico"),
						rs.getString("nm_campo_chave"),
						rs.getString("nm_campo_nao_nulo"),
						rs.getInt("tp_sincronizacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM sinc_tabela");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Tabela> getList() {
		return getList(null);
	}

	public static ArrayList<Tabela> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Tabela> list = new ArrayList<Tabela>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Tabela obj = TabelaDAO.get(rsm.getInt("cd_tabela"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM sinc_tabela", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
