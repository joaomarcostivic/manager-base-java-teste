package com.tivic.manager.triagem.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.manager.triagem.entities.EventoEstacionamentoDigital;
import com.tivic.sol.connection.Conexao;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;

public class EventoEstacionamentoDigitalDAO{

	public static int insert(EventoEstacionamentoDigital objeto) {
		return insert(objeto, null);
	}

	public static int insert(EventoEstacionamentoDigital objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_evento_estacionamento_digital (cd_evento,"+
			                                  "nr_notificacao,"+
			                                  "ds_notificacao,"+
			                                  "st_notificacao,"+
			                                  "ds_motivo_cancelamento, " +
			                                  "dt_tempo_verificacao ) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEvento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEvento());
			pstmt.setString(2,objeto.getNrNotificacao());
			pstmt.setString(3,objeto.getDsNotificacao());
			pstmt.setInt(4,objeto.getStNotificacao());
			pstmt.setString(5,objeto.getDsMotivoCancelamento());
			if(objeto.getDtTempoVerificacao()==null)
				pstmt.setNull(6, Types.DATE);
			else
				pstmt.setTimestamp(6, new Timestamp(objeto.getDtTempoVerificacao().getTimeInMillis()));
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EventoEstacionamentoDigital objeto) {
		return update(objeto, null);
	}

	public static int update(EventoEstacionamentoDigital objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_evento_estacionamento_digital SET cd_evento=?,"+
												      		   "nr_notificacao=?,"+
												      		   "ds_notificacao=?,"+
												      		   "st_notificacao=?,"+
												      		   "ds_motivo_cancelamento=?,"+
												      		   "dt_tempo_verificacao=? WHERE cd_evento=?");
			pstmt.setInt(1,objeto.getCdEvento());
			pstmt.setString(2,objeto.getNrNotificacao());
			pstmt.setString(3,objeto.getDsNotificacao());
			pstmt.setInt(4,objeto.getStNotificacao());
			pstmt.setString(5,objeto.getDsMotivoCancelamento());
			if(objeto.getDtTempoVerificacao()==null)
				pstmt.setNull(6, Types.DATE);
			else
				pstmt.setTimestamp(6, new Timestamp(objeto.getDtTempoVerificacao().getTimeInMillis()));
			pstmt.setInt(7, objeto.getCdEvento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEvento) {
		return delete(cdEvento, null);
	}

	public static int delete(int cdEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_evento_estacionamento_digital WHERE cd_evento=?");
			pstmt.setInt(1, cdEvento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EventoEstacionamentoDigital get(int cdEvento) {
		return get(cdEvento, null);
	}

	public static EventoEstacionamentoDigital get(int cdEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_evento_estacionamento_digital WHERE cd_evento=?");
			pstmt.setInt(1, cdEvento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EventoEstacionamentoDigital(rs.getInt("cd_evento"),
						rs.getString("nr_notificacao"),
						rs.getString("ds_notificacao"),
						rs.getInt("st_notificacao"),
						rs.getString("ds_motivo_cancelamento"),
						rs.getTimestamp("dt_tempo_verificacao") == null ? null : Util.longToCalendar(rs.getTimestamp("dt_tempo_verificacao").getTime())
					);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_evento_estacionamento_digital");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<EventoEstacionamentoDigital> getList() {
		return getList(null);
	}

	public static ArrayList<EventoEstacionamentoDigital> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<EventoEstacionamentoDigital> list = new ArrayList<EventoEstacionamentoDigital>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				EventoEstacionamentoDigital obj = EventoEstacionamentoDigitalDAO.get(rsm.getInt("cd_evento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_evento_estacionamento_digital", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
