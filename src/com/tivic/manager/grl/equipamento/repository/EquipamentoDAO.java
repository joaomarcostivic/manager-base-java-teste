package com.tivic.manager.grl.equipamento.repository;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.equipamento.Equipamento;

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
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_equipamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdEquipamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_equipamento (cd_equipamento,"+
			                                  "nm_equipamento,"+
			                                  "id_equipamento,"+
			                                  "tp_equipamento,"+
			                                  "txt_observacao,"+
			                                  "nm_marca,"+
			                                  "nm_modelo,"+
			                                  "st_equipamento,"+
			                                  "cd_logradouro,"+
			                                  "vl_latitude,"+
			                                  "vl_longitude,"+
			                                  "dt_inicial,"+
			                                  "dt_final,"+
			                                  "cd_orgao,"+
			                                  "nm_host,"+
			                                  "nr_port,"+
			                                  "nm_pwd,"+
			                                  "nr_channel,"+
			                                  "nm_login,"+
			                                  "nm_url_snapshot,"+
			                                  "nm_url_stream,"+
			                                  "ds_local,"+
			                                  "lg_criptografia,"+
			                                  "lg_sync_ftp,"+
			                                  "dt_afericao,"+
			                                  "nr_lacre,"+
			                                  "nr_inventario_inmetro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmEquipamento());
			pstmt.setString(3,objeto.getIdEquipamento());
			pstmt.setInt(4,objeto.getTpEquipamento());
			pstmt.setString(5,objeto.getTxtObservacao());
			pstmt.setString(6,objeto.getNmMarca());
			pstmt.setString(7,objeto.getNmModelo());
			pstmt.setInt(8,objeto.getStEquipamento());
			if(objeto.getCdLogradouro()==0)
				pstmt.setNull(9,Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdLogradouro());
			if(objeto.getVlLatitude() == null || objeto.getVlLatitude()==0)
				pstmt.setNull(10,Types.DOUBLE);
			else
				pstmt.setDouble(10,objeto.getVlLatitude());
			if(objeto.getVlLongitude() == null ||objeto.getVlLongitude()==0)
				pstmt.setNull(11,Types.DOUBLE);
			else
				pstmt.setDouble(11,objeto.getVlLongitude());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdOrgao());
			pstmt.setString(15,objeto.getNmHost());
			pstmt.setInt(16,objeto.getNrPort());
			pstmt.setString(17,objeto.getNmPwd());
			pstmt.setInt(18,objeto.getNrChannel());
			pstmt.setString(19,objeto.getNmLogin());
			pstmt.setString(20,objeto.getNmUrlSnapshot());
			pstmt.setString(21,objeto.getNmUrlStream());
			pstmt.setString(22,objeto.getDsLocal());
			pstmt.setInt(23,objeto.getLgCriptografia());
			pstmt.setInt(24,objeto.getLgSyncFtp());
			if(objeto.getDtAfericao()==null)
				pstmt.setNull(25, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(25,new Timestamp(objeto.getDtAfericao().getTimeInMillis()));
			pstmt.setString(26,objeto.getNrLacre());
			pstmt.setString(27,objeto.getNrInventarioInmetro());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoDAO.insert: " +  e);
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
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_equipamento SET cd_equipamento=?,"+
												      		   "nm_equipamento=?,"+
												      		   "id_equipamento=?,"+
												      		   "tp_equipamento=?,"+
												      		   "txt_observacao=?,"+
												      		   "nm_marca=?,"+
												      		   "nm_modelo=?,"+
												      		   "st_equipamento=?,"+
												      		   "cd_logradouro=?,"+
												      		   "vl_latitude=?,"+
												      		   "vl_longitude=?,"+
												      		   "dt_inicial=?,"+
												      		   "dt_final=?,"+
												      		   "cd_orgao=?,"+
												      		   "nm_host=?,"+
												      		   "nr_port=?,"+
												      		   "nm_pwd=?,"+
												      		   "nr_channel=?,"+
												      		   "nm_login=?,"+
												      		   "nm_url_snapshot=?,"+
												      		   "nm_url_stream=?,"+
												      		   "ds_local=?,"+
												      		   "lg_criptografia=?,"+
												      		   "lg_sync_ftp=?,"+
												      		   "dt_afericao=?,"+
												      		   "nr_lacre=?,"+
												      		   "nr_inventario_inmetro=? WHERE cd_equipamento=?");
			pstmt.setInt(1,objeto.getCdEquipamento());
			pstmt.setString(2,objeto.getNmEquipamento());
			pstmt.setString(3,objeto.getIdEquipamento());
			pstmt.setInt(4,objeto.getTpEquipamento());
			pstmt.setString(5,objeto.getTxtObservacao());
			pstmt.setString(6,objeto.getNmMarca());
			pstmt.setString(7,objeto.getNmModelo());
			pstmt.setInt(8,objeto.getStEquipamento());
			if(objeto.getCdLogradouro()==0)
				pstmt.setNull(9,Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdLogradouro());			
			if(objeto.getVlLatitude() == null || objeto.getVlLatitude()==0)
				pstmt.setNull(10,Types.DOUBLE);
			else
				pstmt.setDouble(10,objeto.getVlLatitude());
			if(objeto.getVlLongitude() == null ||objeto.getVlLongitude()==0)
				pstmt.setNull(11,Types.DOUBLE);
			else
				pstmt.setDouble(11,objeto.getVlLongitude());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdOrgao());
			pstmt.setString(15,objeto.getNmHost());
			pstmt.setInt(16,objeto.getNrPort());
			pstmt.setString(17,objeto.getNmPwd());
			pstmt.setInt(18,objeto.getNrChannel());
			pstmt.setString(19,objeto.getNmLogin());
			pstmt.setString(20,objeto.getNmUrlSnapshot());
			pstmt.setString(21,objeto.getNmUrlStream());
			pstmt.setString(22,objeto.getDsLocal());
			pstmt.setInt(23,objeto.getLgCriptografia());
			pstmt.setInt(24,objeto.getLgSyncFtp());
			if(objeto.getDtAfericao()==null)
				pstmt.setNull(25, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(25,new Timestamp(objeto.getDtAfericao().getTimeInMillis()));
			pstmt.setString(26,objeto.getNrLacre());
			pstmt.setString(27,objeto.getNrInventarioInmetro());
			pstmt.setInt(28, cdEquipamentoOld!=0 ? cdEquipamentoOld : objeto.getCdEquipamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoDAO.update: " +  e);
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
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_equipamento WHERE cd_equipamento=?");
			pstmt.setInt(1, cdEquipamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoDAO.delete: " +  e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_equipamento WHERE cd_equipamento=?");
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
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()),
						rs.getInt("cd_orgao"),
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_equipamento");
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
		return Search.find("SELECT * FROM grl_equipamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
