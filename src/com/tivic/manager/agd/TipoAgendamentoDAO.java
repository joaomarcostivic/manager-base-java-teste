package com.tivic.manager.agd;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoAgendamentoDAO{

	public static int insert(TipoAgendamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoAgendamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("agd_tipo_agendamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoAgendamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_tipo_agendamento (cd_tipo_agendamento,"+
			                                  "nm_tipo_agendamento,"+
			                                  "tp_agendamento,"+
			                                  "lg_negrito_texto,"+
			                                  "id_cor_texto,"+
			                                  "id_cor_background) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoAgendamento());
			pstmt.setInt(3,objeto.getTpAgendamento());
			pstmt.setInt(4,objeto.getLgNegritoTexto());
			pstmt.setString(5,objeto.getIdCorTexto());
			pstmt.setString(6,objeto.getIdCorBackground());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAgendamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAgendamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoAgendamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoAgendamento objeto, int cdTipoAgendamentoOld) {
		return update(objeto, cdTipoAgendamentoOld, null);
	}

	public static int update(TipoAgendamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoAgendamento objeto, int cdTipoAgendamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agd_tipo_agendamento SET cd_tipo_agendamento=?,"+
												      		   "nm_tipo_agendamento=?,"+
												      		   "tp_agendamento=?,"+
												      		   "lg_negrito_texto=?,"+
												      		   "id_cor_texto=?,"+
												      		   "id_cor_background=? WHERE cd_tipo_agendamento=?");
			pstmt.setInt(1,objeto.getCdTipoAgendamento());
			pstmt.setString(2,objeto.getNmTipoAgendamento());
			pstmt.setInt(3,objeto.getTpAgendamento());
			pstmt.setInt(4,objeto.getLgNegritoTexto());
			pstmt.setString(5,objeto.getIdCorTexto());
			pstmt.setString(6,objeto.getIdCorBackground());
			pstmt.setInt(7, cdTipoAgendamentoOld!=0 ? cdTipoAgendamentoOld : objeto.getCdTipoAgendamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAgendamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAgendamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoAgendamento) {
		return delete(cdTipoAgendamento, null);
	}

	public static int delete(int cdTipoAgendamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_tipo_agendamento WHERE cd_tipo_agendamento=?");
			pstmt.setInt(1, cdTipoAgendamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAgendamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAgendamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoAgendamento get(int cdTipoAgendamento) {
		return get(cdTipoAgendamento, null);
	}

	public static TipoAgendamento get(int cdTipoAgendamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_tipo_agendamento WHERE cd_tipo_agendamento=?");
			pstmt.setInt(1, cdTipoAgendamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoAgendamento(rs.getInt("cd_tipo_agendamento"),
						rs.getString("nm_tipo_agendamento"),
						rs.getInt("tp_agendamento"),
						rs.getInt("lg_negrito_texto"),
						rs.getString("id_cor_texto"),
						rs.getString("id_cor_background"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAgendamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAgendamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_tipo_agendamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAgendamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAgendamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM agd_tipo_agendamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
