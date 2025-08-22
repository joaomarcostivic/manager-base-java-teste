package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class CorreiosLoteDAO{

	public static int insert(CorreiosLote objeto) {
		return insert(objeto, null);
	}

	public static int insert(CorreiosLote objeto, Connection connect){
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");	
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = lgBaseAntiga == true ? Conexao.getSequenceCode("stt_correios_lote", connect) : Conexao.getSequenceCode("mob_correios_lote", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLote(code);
			
			String insertBaseNova = "INSERT INTO mob_correios_lote (cd_lote,"+
                    				"dt_lote,"+
                    				"nr_inicial,"+
                    				"nr_final,"+
                    				"st_lote,"+
                    				"tp_lote,"+
                    				"sg_lote,"+
                    				"dt_vencimento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			
			String insertBaseAntiga = "INSERT INTO stt_correios_lote (cd_lote,"+
                    				  "dt_lote,"+
                    				  "nr_inicial,"+
                    				  "nr_final,"+
                    				  "st_lote,"+
                    				  "tp_lote,"+
                    				  "sg_lote,"+
                    				  "dt_vencimento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			
			String sqlInsert = lgBaseAntiga == true ? insertBaseAntiga : insertBaseNova;
			
			PreparedStatement pstmt = connect.prepareStatement(sqlInsert);
			pstmt.setInt(1, code);
			if(objeto.getDtLote()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtLote().getTimeInMillis()));
			pstmt.setInt(3,objeto.getNrInicial());
			pstmt.setInt(4,objeto.getNrFinal());
			pstmt.setInt(5,objeto.getStLote());
			pstmt.setInt(6,objeto.getTpLote());
			pstmt.setString(7,objeto.getSgLote());
			if(objeto.getDtVencimento()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtVencimento().getTimeInMillis()));
			
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CorreiosLoteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CorreiosLoteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CorreiosLote objeto) {
		return update(objeto, 0, null);
	}

	public static int update(CorreiosLote objeto, int cdLoteOld) {
		return update(objeto, cdLoteOld, null);
	}

	public static int update(CorreiosLote objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(CorreiosLote objeto, int cdLoteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_correios_lote SET cd_lote=?,"+
												      		   "dt_lote=?,"+
												      		   "nr_inicial=?,"+
												      		   "nr_final=?,"+
												      		   "st_lote=?,"+
												      		   "tp_lote=?,"+
												      		   "sg_lote=?,"+
												      		   "dt_vencimento = ? WHERE cd_lote=?");
			pstmt.setInt(1,objeto.getCdLote());
			if(objeto.getDtLote()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtLote().getTimeInMillis()));
			pstmt.setInt(3,objeto.getNrInicial());
			pstmt.setInt(4,objeto.getNrFinal());
			pstmt.setInt(5,objeto.getStLote());
			pstmt.setInt(6,objeto.getTpLote());
			pstmt.setString(7,objeto.getSgLote());
			if(objeto.getDtVencimento()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtVencimento().getTimeInMillis()));
			pstmt.setInt(9, cdLoteOld!=0 ? cdLoteOld : objeto.getCdLote());
			
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CorreiosLoteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CorreiosLoteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLote) {
		return delete(cdLote, null);
	}

	public static int delete(int cdLote, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_correios_lote WHERE cd_lote=?");
			pstmt.setInt(1, cdLote);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CorreiosLoteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CorreiosLoteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CorreiosLote get(int cdLote) {
		return get(cdLote, null);
	}

	public static CorreiosLote get(int cdLote, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_correios_lote WHERE cd_lote=?");
			pstmt.setInt(1, cdLote);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CorreiosLote(rs.getInt("cd_lote"),
						(rs.getTimestamp("dt_lote")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_lote").getTime()),
						rs.getInt("nr_inicial"),
						rs.getInt("nr_final"),
						rs.getInt("st_lote"),
						rs.getInt("tp_lote"),
						rs.getString("sg_lote"),
						(rs.getTimestamp("dt_vencimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vencimento").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CorreiosLoteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CorreiosLoteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_correios_lote");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CorreiosLoteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CorreiosLoteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CorreiosLote> getList() {
		return getList(null);
	}

	public static ArrayList<CorreiosLote> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CorreiosLote> list = new ArrayList<CorreiosLote>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CorreiosLote obj = CorreiosLoteDAO.get(rsm.getInt("cd_lote"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CorreiosLoteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_correios_lote", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
