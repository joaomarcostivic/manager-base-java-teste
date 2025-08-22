package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class EventoMotivoCancelamentoDAO{

	public static int insert(EventoMotivoCancelamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(EventoMotivoCancelamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_evento_motivo_cancelamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMotivoCancelamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_evento_motivo_cancelamento (cd_motivo_cancelamento,"+
			                                  "nm_motivo_cancelamento) VALUES (?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmMotivoCancelamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoMotivoCancelamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoMotivoCancelamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EventoMotivoCancelamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(EventoMotivoCancelamento objeto, int cdMotivoCancelamentoOld) {
		return update(objeto, cdMotivoCancelamentoOld, null);
	}

	public static int update(EventoMotivoCancelamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(EventoMotivoCancelamento objeto, int cdMotivoCancelamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_evento_motivo_cancelamento SET cd_motivo_cancelamento=?,"+
												      		   "nm_motivo_cancelamento=? WHERE cd_motivo_cancelamento=?");
			pstmt.setInt(1,objeto.getCdMotivoCancelamento());
			pstmt.setString(2,objeto.getNmMotivoCancelamento());
			pstmt.setInt(3, cdMotivoCancelamentoOld!=0 ? cdMotivoCancelamentoOld : objeto.getCdMotivoCancelamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoMotivoCancelamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoMotivoCancelamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMotivoCancelamento) {
		return delete(cdMotivoCancelamento, null);
	}

	public static int delete(int cdMotivoCancelamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_evento_motivo_cancelamento WHERE cd_motivo_cancelamento=?");
			pstmt.setInt(1, cdMotivoCancelamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoMotivoCancelamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoMotivoCancelamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EventoMotivoCancelamento get(int cdMotivoCancelamento) {
		return get(cdMotivoCancelamento, null);
	}

	public static EventoMotivoCancelamento get(int cdMotivoCancelamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_evento_motivo_cancelamento WHERE cd_motivo_cancelamento=?");
			pstmt.setInt(1, cdMotivoCancelamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EventoMotivoCancelamento(rs.getInt("cd_motivo_cancelamento"),
						rs.getString("nm_motivo_cancelamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoMotivoCancelamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoMotivoCancelamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_evento_motivo_cancelamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoMotivoCancelamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoMotivoCancelamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<EventoMotivoCancelamento> getList() {
		return getList(null);
	}

	public static ArrayList<EventoMotivoCancelamento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<EventoMotivoCancelamento> list = new ArrayList<EventoMotivoCancelamento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				EventoMotivoCancelamento obj = EventoMotivoCancelamentoDAO.get(rsm.getInt("cd_motivo_cancelamento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoMotivoCancelamentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_evento_motivo_cancelamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
