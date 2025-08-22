package com.tivic.manager.triagem.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.manager.triagem.entities.EventoEstacionamentoDigitalDetalhes;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class EventoEstacionamentoDigitalDetalhesDAO{

	public static int insert(EventoEstacionamentoDigitalDetalhes objeto) {
		return insert(objeto, null);
	}

	public static int insert(EventoEstacionamentoDigitalDetalhes objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_evento_estacionamento_digital_detalhes (cd_evento,"+
			                                  "id_dispositivo,"+
			                                  "nr_vaga,"+
			                                  "nm_rua_notificacao,"+
			                                  "nr_imovel_referencia,"+
			                                  "id_colaborador) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEvento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEvento());
			pstmt.setString(2,objeto.getIdDispositivo());
			pstmt.setString(3,objeto.getNrVaga());
			pstmt.setString(4,objeto.getNmRuaNotificacao());
			pstmt.setString(5,objeto.getNrImovelReferencia());
			pstmt.setString(6,objeto.getIdColaborador());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDetalhesDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDetalhesDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EventoEstacionamentoDigitalDetalhes objeto) {
		return update(objeto, null);
	}

	public static int update(EventoEstacionamentoDigitalDetalhes objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_evento_estacionamento_digital_detalhes SET cd_evento=?,"+
												      		   "id_dispositivo=?,"+
												      		   "nr_vaga=?,"+
												      		   "nm_rua_notificacao=?,"+
												      		   "nr_imovel_referencia=?,"+
												      		   "id_colaborador=? WHERE cd_evento=?");
			pstmt.setInt(1,objeto.getCdEvento());
			pstmt.setString(2,objeto.getIdDispositivo());
			pstmt.setString(3,objeto.getNrVaga());
			pstmt.setString(4,objeto.getNmRuaNotificacao());
			pstmt.setString(5,objeto.getNrImovelReferencia());
			pstmt.setString(6,objeto.getIdColaborador());
			pstmt.setInt(7, objeto.getCdEvento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDetalhesDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDetalhesDAO.update: " +  e);
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
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_evento_estacionamento_digital_detalhes WHERE cd_evento=?");
			pstmt.setInt(1, cdEvento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDetalhesDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDetalhesDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EventoEstacionamentoDigitalDetalhes get(int cdEvento) {
		return get(cdEvento, null);
	}

	public static EventoEstacionamentoDigitalDetalhes get(int cdEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_evento_estacionamento_digital_detalhes WHERE cd_evento=?");
			pstmt.setInt(1, cdEvento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EventoEstacionamentoDigitalDetalhes(rs.getInt("cd_evento"),
						rs.getString("id_dispositivo"),
						rs.getString("nr_vaga"),
						rs.getString("nm_rua_notificacao"),
						rs.getString("nr_imovel_referencia"),
						rs.getString("id_colaborador"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDetalhesDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDetalhesDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_evento_estacionamento_digital_detalhes");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDetalhesDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDetalhesDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<EventoEstacionamentoDigitalDetalhes> getList() {
		return getList(null);
	}

	public static ArrayList<EventoEstacionamentoDigitalDetalhes> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<EventoEstacionamentoDigitalDetalhes> list = new ArrayList<EventoEstacionamentoDigitalDetalhes>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				EventoEstacionamentoDigitalDetalhes obj = EventoEstacionamentoDigitalDetalhesDAO.get(rsm.getInt("cd_evento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalDetalhesDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_evento_estacionamento_digital_detalhes", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
