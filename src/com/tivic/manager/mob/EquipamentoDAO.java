package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;

import java.util.ArrayList;

public class EquipamentoDAO{

	public static int insert(Equipamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(Equipamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = com.tivic.manager.grl.EquipamentoDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdEquipamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_equipamento (cd_equipamento) VALUES (?)");
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEquipamento());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static int insertCdEquipamento(int cdEquipamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}			
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_equipamento (cd_equipamento) VALUES (?)");		
			pstmt.setInt(1,cdEquipamento);
			pstmt.executeUpdate();
			
			if (isConnectionNull)
				connect.commit();
			return 0;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Equipamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Equipamento objeto, int cdEquipamentoOld) {
		return update(objeto, cdEquipamentoOld, null);
	}

	public static int update(Equipamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Equipamento objeto, int cdEquipamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			//TODO alguém me explica o porquê de usar isso aqui no DAO uma vez que a validação foi feita no SErvices.
//			Equipamento objetoTemp = get(objeto.getCdEquipamento(), connect);
//			if (objetoTemp == null) 
//				pstmt = connect.prepareStatement("INSERT INTO mob_equipamento (cd_equipamento) VALUES (?)");
//			else
//				pstmt = connect.prepareStatement("UPDATE mob_equipamento SET cd_equipamento=? WHERE cd_equipamento=?");
//						
//			pstmt.setInt(1,objeto.getCdEquipamento());
//			if (objetoTemp != null) {
//				pstmt.setInt(2, cdEquipamentoOld!=0 ? cdEquipamentoOld : objeto.getCdEquipamento());
//			}
//			pstmt.executeUpdate();
			if (com.tivic.manager.grl.EquipamentoDAO.update(objeto, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEquipamento) {
		return delete(cdEquipamento, null);
	}

	public static int delete(int cdEquipamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_equipamento WHERE cd_equipamento=?");
			pstmt.setInt(1, cdEquipamento);
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.equipamento.repository.EquipamentoDAO.delete(cdEquipamento, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Equipamento get(int cdEquipamento) {
		return get(cdEquipamento, null);
	}

	public static Equipamento get(int cdEquipamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_equipamento A, grl_equipamento B WHERE A.cd_equipamento=B.cd_equipamento AND A.cd_equipamento=?");
			pstmt.setInt(1, cdEquipamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Equipamento(rs.getInt("cd_equipamento"),
						rs.getString("nm_equipamento"),
						rs.getString("id_equipamento"),
						rs.getInt("tp_equipamento"),
						rs.getString("txt_observacao"),
						rs.getString("nm_marca"),
						rs.getString("nm_modelo"),
						rs.getInt("st_equipamento"),
						rs.getInt("cd_logradouro"),
						rs.getDouble("vl_latitude"),
						rs.getDouble("vl_longitude"),
						rs.getInt("cd_orgao"), 
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()),
						rs.getString("nm_host"),
						rs.getInt("nr_port"),
						rs.getString("nm_pwd"),
						rs.getInt("nr_channel"),
						rs.getString("nm_login"),
						rs.getString("nm_url_snapshot"),
						rs.getString("nm_url_stream"),
						rs.getString("ds_local"),
						rs.getInt("lg_criptografia"),
						rs.getInt("lg_sync_ftp"),
						(rs.getTimestamp("dt_afericao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_afericao").getTime()),
					    rs.getString("nr_lacre"),
					    rs.getString("nr_inventario_inmetro"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_equipamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Equipamento> getList() {
		return getList(null);
	}

	public static ArrayList<Equipamento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Equipamento> list = new ArrayList<Equipamento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Equipamento obj = EquipamentoDAO.get(rsm.getInt("cd_equipamento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_equipamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
