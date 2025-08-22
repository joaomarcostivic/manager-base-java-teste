package com.tivic.manager.log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;

public class BuscaPlacaDAO{

	public static int insert(BuscaPlaca objeto) {
		return insert(objeto, null);
	}

	public static int insert(BuscaPlaca objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("log_busca_placa", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLog(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO log_busca_placa (cd_log,"+
			                                  "tp_destino,"+
			                                  "dt_log,"+
			                                  "nr_placa,"+
			                                  "url_requisicao,"+
			                                  "txt_requisicao,"+
			                                  "txt_resposta,"+
			                                  "id_orgao,"+
			                                  "cd_usuario,"+
			                                  "tp_log) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getTpDestino());
			if(objeto.getDtLog()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtLog().getTimeInMillis()));
			pstmt.setString(4,objeto.getNrPlaca());
			pstmt.setString(5,objeto.getUrlRequisicao());
			pstmt.setString(6,objeto.getTxtRequisicao());
			pstmt.setString(7,objeto.getTxtResposta());
			pstmt.setString(8,objeto.getIdOrgao());
			pstmt.setInt(9,objeto.getCdUsuario());
			pstmt.setInt(10,objeto.getTpLog());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BuscaPlacaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BuscaPlacaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(BuscaPlaca objeto) {
		return update(objeto, 0, null);
	}

	public static int update(BuscaPlaca objeto, int cdLogOld) {
		return update(objeto, cdLogOld, null);
	}

	public static int update(BuscaPlaca objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(BuscaPlaca objeto, int cdLogOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE log_busca_placa SET cd_log=?,"+
												      		   "tp_destino=?,"+
												      		   "dt_log=?,"+
												      		   "nr_placa=?,"+
												      		   "url_requisicao=?,"+
												      		   "txt_requisicao=?,"+
												      		   "txt_resposta=?,"+
												      		   "id_orgao=?,"+
												      		   "cd_usuario=?,"+
												      		   "tp_log=? WHERE cd_log=?");
			pstmt.setInt(1,objeto.getCdLog());
			pstmt.setInt(2,objeto.getTpDestino());
			if(objeto.getDtLog()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtLog().getTimeInMillis()));
			pstmt.setString(4,objeto.getNrPlaca());
			pstmt.setString(5,objeto.getUrlRequisicao());
			pstmt.setString(6,objeto.getTxtRequisicao());
			pstmt.setString(7,objeto.getTxtResposta());
			pstmt.setString(8,objeto.getIdOrgao());
			pstmt.setInt(9,objeto.getCdUsuario());
			pstmt.setInt(10,objeto.getTpLog());
			pstmt.setInt(11, cdLogOld!=0 ? cdLogOld : objeto.getCdLog());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BuscaPlacaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BuscaPlacaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLog) {
		return delete(cdLog, null);
	}

	public static int delete(int cdLog, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM log_busca_placa WHERE cd_log=?");
			pstmt.setInt(1, cdLog);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BuscaPlacaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BuscaPlacaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static BuscaPlaca get(int cdLog) {
		return get(cdLog, null);
	}

	public static BuscaPlaca get(int cdLog, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM log_busca_placa WHERE cd_log=?");
			pstmt.setInt(1, cdLog);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new BuscaPlaca(rs.getInt("cd_log"),
						rs.getInt("tp_destino"),
						(rs.getTimestamp("dt_log")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_log").getTime()),
						rs.getString("nr_placa"),
						rs.getString("url_requisicao"),
						rs.getString("txt_requisicao"),
						rs.getString("txt_resposta"),
						rs.getString("id_orgao"),
						rs.getInt("cd_usuario"),
						rs.getInt("tp_log"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BuscaPlacaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BuscaPlacaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM log_busca_placa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BuscaPlacaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BuscaPlacaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<BuscaPlaca> getList() {
		return getList(null);
	}

	public static ArrayList<BuscaPlaca> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<BuscaPlaca> list = new ArrayList<BuscaPlaca>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				BuscaPlaca obj = BuscaPlacaDAO.get(rsm.getInt("cd_log"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BuscaPlacaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM log_busca_placa", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
