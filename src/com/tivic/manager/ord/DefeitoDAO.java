package com.tivic.manager.ord;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class DefeitoDAO{

	public static int insert(Defeito objeto) {
		return insert(objeto, null);
	}

	public static int insert(Defeito objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ord_defeito", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDefeito(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ord_defeito (cd_defeito,"+
			                                  "nm_defeito,"+
			                                  "txt_defeito,"+
			                                  "nr_horas_previsao_reparo,"+
			                                  "lg_ativo,"+
			                                  "id_defeito) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmDefeito());
			pstmt.setString(3,objeto.getTxtDefeito());
			pstmt.setInt(4,objeto.getNrHorasPrevisaoReparo());
			pstmt.setInt(5,objeto.getLgAtivo());
			pstmt.setString(6,objeto.getIdDefeito());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DefeitoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DefeitoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Defeito objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Defeito objeto, int cdDefeitoOld) {
		return update(objeto, cdDefeitoOld, null);
	}

	public static int update(Defeito objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Defeito objeto, int cdDefeitoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ord_defeito SET cd_defeito=?,"+
												      		   "nm_defeito=?,"+
												      		   "txt_defeito=?,"+
												      		   "nr_horas_previsao_reparo=?,"+
												      		   "lg_ativo=?,"+
												      		   "id_defeito=? WHERE cd_defeito=?");
			pstmt.setInt(1,objeto.getCdDefeito());
			pstmt.setString(2,objeto.getNmDefeito());
			pstmt.setString(3,objeto.getTxtDefeito());
			pstmt.setInt(4,objeto.getNrHorasPrevisaoReparo());
			pstmt.setInt(5,objeto.getLgAtivo());
			pstmt.setString(6,objeto.getIdDefeito());
			pstmt.setInt(7, cdDefeitoOld!=0 ? cdDefeitoOld : objeto.getCdDefeito());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DefeitoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DefeitoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDefeito) {
		return delete(cdDefeito, null);
	}

	public static int delete(int cdDefeito, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ord_defeito WHERE cd_defeito=?");
			pstmt.setInt(1, cdDefeito);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DefeitoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DefeitoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Defeito get(int cdDefeito) {
		return get(cdDefeito, null);
	}

	public static Defeito get(int cdDefeito, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ord_defeito WHERE cd_defeito=?");
			pstmt.setInt(1, cdDefeito);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Defeito(rs.getInt("cd_defeito"),
						rs.getString("nm_defeito"),
						rs.getString("txt_defeito"),
						rs.getInt("nr_horas_previsao_reparo"),
						rs.getInt("lg_ativo"),
						rs.getString("id_defeito"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DefeitoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DefeitoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ord_defeito");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DefeitoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DefeitoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Defeito> getList() {
		return getList(null);
	}

	public static ArrayList<Defeito> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Defeito> list = new ArrayList<Defeito>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Defeito obj = DefeitoDAO.get(rsm.getInt("cd_defeito"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DefeitoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ord_defeito", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
