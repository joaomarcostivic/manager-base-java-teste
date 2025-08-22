package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class EquipamentoHistoricoDAO{

	public static int insert(EquipamentoHistorico objeto) {
		return insert(objeto, null);
	}

	public static int insert(EquipamentoHistorico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("GRL_EQUIPAMENTO_HISTORICO", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdHistorico(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO GRL_EQUIPAMENTO_HISTORICO (CD_EQUIPAMENTO,"+
			                                  "CD_HISTORICO,"+
			                                  "CD_USUARIO,"+
			                                  "DT_HISTORICO,"+
			                                  "TXT_HISTORICO,"+
			                                  "TP_HISTORICO,"+
			                                  "VL_LATITUTE,"+
			                                  "VL_LONGITUDE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdEquipamento());
			pstmt.setInt(2, code);
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdUsuario());
			if(objeto.getDtHistorico()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtHistorico().getTimeInMillis()));
			pstmt.setString(5,objeto.getTxtHistorico());
			pstmt.setInt(6,objeto.getTpHistorico());
			pstmt.setDouble(7,objeto.getVlLatitude());
			pstmt.setDouble(8,objeto.getVlLongitude());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoHistoricoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoHistoricoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EquipamentoHistorico objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(EquipamentoHistorico objeto, int cdEquipamentoOld, int cdHistoricoOld) {
		return update(objeto, cdEquipamentoOld, cdHistoricoOld, null);
	}

	public static int update(EquipamentoHistorico objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(EquipamentoHistorico objeto, int cdEquipamentoOld, int cdHistoricoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE GRL_EQUIPAMENTO_HISTORICO SET CD_EQUIPAMENTO=?,"+
												      		   "CD_HISTORICO=?,"+
												      		   "CD_USUARIO=?,"+
												      		   "DT_HISTORICO=?,"+
												      		   "TXT_HISTORICO=?,"+
												      		   "TP_HISTORICO=?,"+
												      		   "VL_LATITUTE=?,"+
												      		   "VL_LONGITUDE=? WHERE CD_EQUIPAMENTO=? AND CD_HISTORICO=?");
			pstmt.setInt(1,objeto.getCdEquipamento());
			pstmt.setInt(2,objeto.getCdHistorico());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdUsuario());
			if(objeto.getDtHistorico()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtHistorico().getTimeInMillis()));
			pstmt.setString(5,objeto.getTxtHistorico());
			pstmt.setInt(6,objeto.getTpHistorico());
			pstmt.setDouble(7,objeto.getVlLatitude());
			pstmt.setDouble(8,objeto.getVlLongitude());
			pstmt.setInt(9, cdEquipamentoOld!=0 ? cdEquipamentoOld : objeto.getCdEquipamento());
			pstmt.setInt(10, cdHistoricoOld!=0 ? cdHistoricoOld : objeto.getCdHistorico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoHistoricoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoHistoricoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEquipamento, int cdHistorico) {
		return delete(cdEquipamento, cdHistorico, null);
	}

	public static int delete(int cdEquipamento, int cdHistorico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM GRL_EQUIPAMENTO_HISTORICO WHERE CD_EQUIPAMENTO=? AND CD_HISTORICO=?");
			pstmt.setInt(1, cdEquipamento);
			pstmt.setInt(2, cdHistorico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoHistoricoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoHistoricoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EquipamentoHistorico get(int cdEquipamento, int cdHistorico) {
		return get(cdEquipamento, cdHistorico, null);
	}

	public static EquipamentoHistorico get(int cdEquipamento, int cdHistorico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM GRL_EQUIPAMENTO_HISTORICO WHERE CD_EQUIPAMENTO=? AND CD_HISTORICO=?");
			pstmt.setInt(1, cdEquipamento);
			pstmt.setInt(2, cdHistorico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EquipamentoHistorico(rs.getInt("CD_EQUIPAMENTO"),
						rs.getInt("CD_HISTORICO"),
						rs.getInt("CD_USUARIO"),
						(rs.getTimestamp("DT_HISTORICO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_HISTORICO").getTime()),
						rs.getString("TXT_HISTORICO"),
						rs.getInt("TP_HISTORICO"),
						rs.getDouble("VL_LATITUTE"),
						rs.getDouble("VL_LONGITUDE"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoHistoricoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoHistoricoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM GRL_EQUIPAMENTO_HISTORICO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoHistoricoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoHistoricoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<EquipamentoHistorico> getList() {
		return getList(null);
	}

	public static ArrayList<EquipamentoHistorico> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<EquipamentoHistorico> list = new ArrayList<EquipamentoHistorico>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				EquipamentoHistorico obj = EquipamentoHistoricoDAO.get(rsm.getInt("CD_EQUIPAMENTO"), rsm.getInt("CD_HISTORICO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoHistoricoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM GRL_EQUIPAMENTO_HISTORICO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
