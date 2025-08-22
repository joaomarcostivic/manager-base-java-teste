package com.tivic.manager.mob;

import java.sql.*;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;

import java.util.HashMap;
import java.util.ArrayList;

public class MotoristaFolgaAgendamentoDAO{

	public static int insert(MotoristaFolgaAgendamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(MotoristaFolgaAgendamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_folga_agendamento");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_motorista");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdMotorista()));
			int code = Conexao.getSequenceCode("mob_motorista_folga_agendamento", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFolgaAgendamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_motorista_folga_agendamento (cd_folga_agendamento,"+
			                                  "cd_motorista,"+
			                                  "dt_agendamento,"+
			                                  "st_folga,"+
			                                  "txt_observacao,"+
			                                  "cd_folga) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdMotorista()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMotorista());
			if(objeto.getDtAgendamento()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtAgendamento().getTimeInMillis()));
			pstmt.setInt(4,objeto.getStFolga());
			pstmt.setString(5,objeto.getTxtObservacao());
			if(objeto.getCdFolga()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdFolga());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaAgendamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaAgendamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MotoristaFolgaAgendamento objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(MotoristaFolgaAgendamento objeto, int cdFolgaAgendamentoOld, int cdMotoristaOld) {
		return update(objeto, cdFolgaAgendamentoOld, cdMotoristaOld, null);
	}

	public static int update(MotoristaFolgaAgendamento objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(MotoristaFolgaAgendamento objeto, int cdFolgaAgendamentoOld, int cdMotoristaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_motorista_folga_agendamento SET cd_folga_agendamento=?,"+
												      		   "cd_motorista=?,"+
												      		   "dt_agendamento=?,"+
												      		   "st_folga=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_folga=? WHERE cd_folga_agendamento=? AND cd_motorista=?");
			pstmt.setInt(1,objeto.getCdFolgaAgendamento());
			pstmt.setInt(2,objeto.getCdMotorista());
			if(objeto.getDtAgendamento()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtAgendamento().getTimeInMillis()));
			pstmt.setInt(4,objeto.getStFolga());
			pstmt.setString(5,objeto.getTxtObservacao());
			if(objeto.getCdFolga()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdFolga());
			pstmt.setInt(7, cdFolgaAgendamentoOld!=0 ? cdFolgaAgendamentoOld : objeto.getCdFolgaAgendamento());
			pstmt.setInt(8, cdMotoristaOld!=0 ? cdMotoristaOld : objeto.getCdMotorista());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaAgendamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaAgendamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFolgaAgendamento, int cdMotorista) {
		return delete(cdFolgaAgendamento, cdMotorista, null);
	}

	public static int delete(int cdFolgaAgendamento, int cdMotorista, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_motorista_folga_agendamento WHERE cd_folga_agendamento=? AND cd_motorista=?");
			pstmt.setInt(1, cdFolgaAgendamento);
			pstmt.setInt(2, cdMotorista);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaAgendamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaAgendamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MotoristaFolgaAgendamento get(int cdFolgaAgendamento, int cdMotorista) {
		return get(cdFolgaAgendamento, cdMotorista, null);
	}

	public static MotoristaFolgaAgendamento get(int cdFolgaAgendamento, int cdMotorista, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_motorista_folga_agendamento WHERE cd_folga_agendamento=? AND cd_motorista=?");
			pstmt.setInt(1, cdFolgaAgendamento);
			pstmt.setInt(2, cdMotorista);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MotoristaFolgaAgendamento(rs.getInt("cd_folga_agendamento"),
						rs.getInt("cd_motorista"),
						(rs.getTimestamp("dt_agendamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_agendamento").getTime()),
						rs.getInt("st_folga"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_folga"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaAgendamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaAgendamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_motorista_folga_agendamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaAgendamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaAgendamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<MotoristaFolgaAgendamento> getList() {
		return getList(null);
	}

	public static ArrayList<MotoristaFolgaAgendamento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<MotoristaFolgaAgendamento> list = new ArrayList<MotoristaFolgaAgendamento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				MotoristaFolgaAgendamento obj = MotoristaFolgaAgendamentoDAO.get(rsm.getInt("cd_folga_agendamento"), rsm.getInt("cd_motorista"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaAgendamentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_motorista_folga_agendamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}