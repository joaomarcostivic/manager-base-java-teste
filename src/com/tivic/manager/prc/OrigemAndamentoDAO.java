package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class OrigemAndamentoDAO{

	public static int insert(OrigemAndamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(OrigemAndamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("prc_origem_andamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOrigemAndamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_origem_andamento (cd_origem_andamento,"+
			                                  "nm_origem_andamento,"+
			                                  "ds_origem_andamento,"+
			                                  "st_origem_andamento) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmOrigemAndamento());
			pstmt.setString(3,objeto.getDsOrigemAndamento());
			pstmt.setInt(4,objeto.getStOrigemAndamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrigemAndamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrigemAndamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OrigemAndamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(OrigemAndamento objeto, int cdOrigemAndamentoOld) {
		return update(objeto, cdOrigemAndamentoOld, null);
	}

	public static int update(OrigemAndamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(OrigemAndamento objeto, int cdOrigemAndamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_origem_andamento SET cd_origem_andamento=?,"+
												      		   "nm_origem_andamento=?,"+
												      		   "ds_origem_andamento=?,"+
												      		   "st_origem_andamento=? WHERE cd_origem_andamento=?");
			pstmt.setInt(1,objeto.getCdOrigemAndamento());
			pstmt.setString(2,objeto.getNmOrigemAndamento());
			pstmt.setString(3,objeto.getDsOrigemAndamento());
			pstmt.setInt(4,objeto.getStOrigemAndamento());
			pstmt.setInt(5, cdOrigemAndamentoOld!=0 ? cdOrigemAndamentoOld : objeto.getCdOrigemAndamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrigemAndamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrigemAndamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOrigemAndamento) {
		return delete(cdOrigemAndamento, null);
	}

	public static int delete(int cdOrigemAndamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_origem_andamento WHERE cd_origem_andamento=?");
			pstmt.setInt(1, cdOrigemAndamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrigemAndamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrigemAndamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OrigemAndamento get(int cdOrigemAndamento) {
		return get(cdOrigemAndamento, null);
	}

	public static OrigemAndamento get(int cdOrigemAndamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_origem_andamento WHERE cd_origem_andamento=?");
			pstmt.setInt(1, cdOrigemAndamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OrigemAndamento(rs.getInt("cd_origem_andamento"),
						rs.getString("nm_origem_andamento"),
						rs.getString("ds_origem_andamento"),
						rs.getInt("st_origem_andamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrigemAndamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrigemAndamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_origem_andamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrigemAndamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrigemAndamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OrigemAndamento> getList() {
		return getList(null);
	}

	public static ArrayList<OrigemAndamento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OrigemAndamento> list = new ArrayList<OrigemAndamento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OrigemAndamento obj = OrigemAndamentoDAO.get(rsm.getInt("cd_origem_andamento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrigemAndamentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM prc_origem_andamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
