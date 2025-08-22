package com.tivic.manager.log;

import java.sql.*;
import sol.dao.*;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class SistemaDAO{

	public static int insert(Sistema objeto) {
		return insert(objeto, null);
	}

	public static int insert(Sistema objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("log_sistema", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLog(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO log_sistema (cd_log,"+
			                                  "dt_log,"+
			                                  "txt_log,"+
			                                  "tp_log,"+
			                                  "cd_usuario,"+
			                                  "nm_http_metodo,"+
			                                  "nm_caminho,"+
			                                  "nr_http_status,"+
			                                  "ds_ip_cliente,"+
			                                  "ds_user_agent) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getDtLog()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtLog().getTimeInMillis()));
			pstmt.setString(3,objeto.getTxtLog());
			pstmt.setInt(4,objeto.getTpLog());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdUsuario());
			pstmt.setString(6,objeto.getNmHttpMetodo());
			pstmt.setString(7, objeto.getNmCaminho());

			pstmt.setInt(8, objeto.getNrHttpStatus());
			pstmt.setString(9, objeto.getDsIpCliente());
			pstmt.setString(10, objeto.getDsUserAgent());
			
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SistemaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SistemaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Sistema objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Sistema objeto, int cdLogOld) {
		return update(objeto, cdLogOld, null);
	}

	public static int update(Sistema objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Sistema objeto, int cdLogOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE log_sistema SET cd_log=?,"+
												      		   "dt_log=?,"+
												      		   "txt_log=?,"+
												      		   "tp_log=?,"+
												      		   "cd_usuario=?"+
												      		   "nm_http_metodo=?,"+
												      		   "nm_caminho=?,"+
												      		   "nr_http_status=?,"+
												      		   "ds_ip_cliente=?,"+
												      		   "ds_user_agent=?"+
												      		   " WHERE cd_log=?");
			pstmt.setInt(1,objeto.getCdLog());
			if(objeto.getDtLog()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtLog().getTimeInMillis()));
			pstmt.setString(3,objeto.getTxtLog());
			pstmt.setInt(4,objeto.getTpLog());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdUsuario());
			pstmt.setString(6,objeto.getNmHttpMetodo());
			pstmt.setString(7, objeto.getNmCaminho());
			pstmt.setInt(8, objeto.getNrHttpStatus());
			pstmt.setString(9, objeto.getDsIpCliente());
			pstmt.setString(10, objeto.getDsUserAgent());
			
			pstmt.setInt(11, cdLogOld!=0 ? cdLogOld : objeto.getCdLog());
			
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SistemaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SistemaDAO.update: " +  e);
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
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM log_sistema WHERE cd_log=?");
			pstmt.setInt(1, cdLog);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SistemaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SistemaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Sistema get(int cdLog) {
		return get(cdLog, null);
	}

	public static Sistema get(int cdLog, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM log_sistema WHERE cd_log=?");
			pstmt.setInt(1, cdLog);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Sistema(rs.getInt("cd_log"),
						(rs.getTimestamp("dt_log")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_log").getTime()),
						rs.getString("txt_log"),
						rs.getInt("tp_log"),
						rs.getInt("cd_usuario"),
						rs.getString("nm_http_metodo"),
						rs.getString("nm_caminho"),
						rs.getInt("nr_http_status"),
						rs.getString("ds_ip_cliente"),
						rs.getString("ds_user_agent"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SistemaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SistemaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM log_sistema");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SistemaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SistemaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM log_sistema", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
