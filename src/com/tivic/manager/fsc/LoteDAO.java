package com.tivic.manager.fsc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class LoteDAO	{

	public static int insert(Lote objeto) {
		return insert(objeto, null);
	}

	public static int insert(Lote objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fsc_lote", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLote(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fsc_lote (cd_lote,"+
			                                  "dt_transmissao,"+
			                                  "dt_lote,"+
			                                  "nr_lote,"+
			                                  "st_lte) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getDtTransmissao()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtTransmissao().getTimeInMillis()));
			if(objeto.getDtLote()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtLote().getTimeInMillis()));
			pstmt.setString(4,objeto.getNrLote());
			pstmt.setInt(5,objeto.getStLte());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Lote objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Lote objeto, int cdLoteOld) {
		return update(objeto, cdLoteOld, null);
	}

	public static int update(Lote objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Lote objeto, int cdLoteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fsc_lote SET cd_lote=?,"+
												      		   "dt_transmissao=?,"+
												      		   "dt_lote=?,"+
												      		   "nr_lote=?,"+
												      		   "st_lte=? WHERE cd_lote=?");
			pstmt.setInt(1,objeto.getCdLote());
			if(objeto.getDtTransmissao()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtTransmissao().getTimeInMillis()));
			if(objeto.getDtLote()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtLote().getTimeInMillis()));
			pstmt.setString(4,objeto.getNrLote());
			pstmt.setInt(5,objeto.getStLte());
			pstmt.setInt(6, cdLoteOld!=0 ? cdLoteOld : objeto.getCdLote());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
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
			return connect.prepareStatement("DELETE FROM fsc_lote WHERE cd_lote="+cdLote).executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Lote get(int cdLote) {
		return get(cdLote, null);
	}

	public static Lote get(int cdLote, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_lote WHERE cd_lote=?");
			pstmt.setInt(1, cdLote);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Lote(rs.getInt("cd_lote"),
						(rs.getTimestamp("dt_transmissao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_transmissao").getTime()),
						(rs.getTimestamp("dt_lote")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_lote").getTime()),
						rs.getString("nr_lote"),
						rs.getInt("st_lte"));
			}
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
			pstmt = connect.prepareStatement("SELECT * FROM fsc_lote");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		return Search.find("SELECT * FROM fsc_lote", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
