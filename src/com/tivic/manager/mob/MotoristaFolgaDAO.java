package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class MotoristaFolgaDAO{

	public static int insert(MotoristaFolga objeto) {
		return insert(objeto, null);
	}

	public static int insert(MotoristaFolga objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_folga");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_motorista");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdMotorista()));
			int code = Conexao.getSequenceCode("mob_motorista_folga", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFolga(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_motorista_folga (cd_folga,"+
			                                  "cd_motorista,"+
			                                  "txt_motivo,"+
			                                  "dt_registro,"+
			                                  "qt_dias) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdMotorista()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMotorista());
			pstmt.setString(3,objeto.getTxtMotivo());
			if(objeto.getDtRegistro()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtRegistro().getTimeInMillis()));
			pstmt.setInt(5,objeto.getQtDias());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MotoristaFolga objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(MotoristaFolga objeto, int cdFolgaOld, int cdMotoristaOld) {
		return update(objeto, cdFolgaOld, cdMotoristaOld, null);
	}

	public static int update(MotoristaFolga objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(MotoristaFolga objeto, int cdFolgaOld, int cdMotoristaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_motorista_folga SET cd_folga=?,"+
												      		   "cd_motorista=?,"+
												      		   "txt_motivo=?,"+
												      		   "dt_registro=?,"+
												      		   "qt_dias=? WHERE cd_folga=? AND cd_motorista=?");
			pstmt.setInt(1,objeto.getCdFolga());
			pstmt.setInt(2,objeto.getCdMotorista());
			pstmt.setString(3,objeto.getTxtMotivo());
			if(objeto.getDtRegistro()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtRegistro().getTimeInMillis()));
			pstmt.setInt(5,objeto.getQtDias());
			pstmt.setInt(6, cdFolgaOld!=0 ? cdFolgaOld : objeto.getCdFolga());
			pstmt.setInt(7, cdMotoristaOld!=0 ? cdMotoristaOld : objeto.getCdMotorista());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFolga, int cdMotorista) {
		return delete(cdFolga, cdMotorista, null);
	}

	public static int delete(int cdFolga, int cdMotorista, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_motorista_folga WHERE cd_folga=? AND cd_motorista=?");
			pstmt.setInt(1, cdFolga);
			pstmt.setInt(2, cdMotorista);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MotoristaFolga get(int cdFolga, int cdMotorista) {
		return get(cdFolga, cdMotorista, null);
	}

	public static MotoristaFolga get(int cdFolga, int cdMotorista, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_motorista_folga WHERE cd_folga=? AND cd_motorista=?");
			pstmt.setInt(1, cdFolga);
			pstmt.setInt(2, cdMotorista);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MotoristaFolga(rs.getInt("cd_folga"),
						rs.getInt("cd_motorista"),
						rs.getString("txt_motivo"),
						(rs.getTimestamp("dt_registro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_registro").getTime()),
						rs.getInt("qt_dias"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_motorista_folga");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<MotoristaFolga> getList() {
		return getList(null);
	}

	public static ArrayList<MotoristaFolga> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<MotoristaFolga> list = new ArrayList<MotoristaFolga>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				MotoristaFolga obj = MotoristaFolgaDAO.get(rsm.getInt("cd_folga"), rsm.getInt("cd_motorista"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_motorista_folga", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}