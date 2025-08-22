package com.tivic.manager.triagem.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.manager.triagem.entities.EventoEstacionamentoDigitalImagem;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class EventoEstacionamentoDigitalImagemDAO{

	public static int insert(EventoEstacionamentoDigitalImagem objeto) {
		return insert(objeto, null);
	}

	public static int insert(EventoEstacionamentoDigitalImagem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_evento_estacionamento_digital_imagem (cd_evento,"+
			                                  "url_imagem,"+
			                                  "vl_latitude,"+
			                                  "vl_longitude) VALUES (?, ?, ?, ?)");
			if(objeto.getCdEvento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEvento());
			pstmt.setString(2,objeto.getUrlImagem());
			if(objeto.getVlLatitude() == null)
				pstmt.setNull(3, Types.DOUBLE);
			else
				pstmt.setDouble(3,objeto.getVlLatitude());
			if(objeto.getVlLongitude() == null)
				pstmt.setNull(4, Types.DOUBLE);
			else
				pstmt.setDouble(4,objeto.getVlLongitude());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalImagemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalImagemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EventoEstacionamentoDigitalImagem objeto) {
		return update(objeto, null);
	}

	public static int update(EventoEstacionamentoDigitalImagem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_evento_estacionamento_digital_imagem SET cd_evento=?,"+
												      		   "url_imagem=?,"+
												      		   "vl_latitude=?,"+
												      		   "vl_longitude=? WHERE cd_evento=?");
			pstmt.setInt(1,objeto.getCdEvento());
			pstmt.setString(2,objeto.getUrlImagem());
			pstmt.setDouble(3,objeto.getVlLatitude());
			pstmt.setDouble(4,objeto.getVlLongitude());
			pstmt.setInt(5, objeto.getCdEvento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalImagemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalImagemDAO.update: " +  e);
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
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_evento_estacionamento_digital_imagem WHERE cd_evento=?");
			pstmt.setInt(1, cdEvento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalImagemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalImagemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EventoEstacionamentoDigitalImagem get(int cdEvento) {
		return get(cdEvento, null);
	}

	public static EventoEstacionamentoDigitalImagem get(int cdEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_evento_estacionamento_digital_imagem WHERE cd_evento=?");
			pstmt.setInt(1, cdEvento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EventoEstacionamentoDigitalImagem(rs.getInt("cd_evento"),
						rs.getString("url_imagem"),
						rs.getDouble("vl_latitude"),
						rs.getDouble("vl_longitude"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalImagemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalImagemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_evento_estacionamento_digital_imagem");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalImagemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalImagemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<EventoEstacionamentoDigitalImagem> getList() {
		return getList(null);
	}

	public static ArrayList<EventoEstacionamentoDigitalImagem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<EventoEstacionamentoDigitalImagem> list = new ArrayList<EventoEstacionamentoDigitalImagem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				EventoEstacionamentoDigitalImagem obj = EventoEstacionamentoDigitalImagemDAO.get(rsm.getInt("cd_evento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEstacionamentoDigitalImagemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_evento_estacionamento_digital_imagem", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
