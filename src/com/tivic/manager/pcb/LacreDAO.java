package com.tivic.manager.pcb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class LacreDAO {
	
	public static int insert(Lacre objeto) {
		return insert(objeto, null);
	}

	public static int insert(Lacre objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			int code = Conexao.getSequenceCode("pcb_lacre", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLacre(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO pcb_lacre (cd_lacre,"+
			                                  "nr_lacre,"+
			                                  "txt_descricao,"+
			                                  "dt_registro, " +
			                                  "cd_bomba) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1,objeto.getCdLacre());
			pstmt.setString(2, objeto.getNrLacre());
			pstmt.setString(3, objeto.getTxtDescricao());
			if(objeto.getDtRegistro()==null)
				pstmt.setTimestamp(4, com.tivic.manager.util.Util.convCalendarToTimestamp(com.tivic.manager.util.Util.getDataAtual()));
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtRegistro().getTimeInMillis()));
			if(objeto.getCdBomba()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdBomba());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LacreDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LacreDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Lacre objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Lacre objeto, int cdLacreOld) {
		return update(objeto, cdLacreOld, null);
	}

	public static int update(Lacre objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Lacre objeto, int cdLacreOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE pcb_Lacre SET cd_lacre=?,"+
												      		   "nr_lacre=?,"+
												      		   "txt_descricao=?,"+
												      		   "dt_registro=?," +
												      		   "cd_bomba=? WHERE cd_lacre=?");
			if(objeto.getCdLacre()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdLacre());
			pstmt.setString(2, objeto.getNrLacre());
			pstmt.setString(3, objeto.getTxtDescricao());
			if(objeto.getDtRegistro()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtRegistro().getTimeInMillis()));
			if(objeto.getCdBomba()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdBomba());
			pstmt.setInt(6, cdLacreOld!=0 ? cdLacreOld : objeto.getCdLacre());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LacreDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LacreDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLacre) {
		return delete(cdLacre, null);
	}

	public static int delete(int cdLacre, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM pcb_lacre WHERE cd_lacre=?");
			pstmt.setInt(1, cdLacre);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LacreDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LacreDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Lacre get(int cdLacre) {
		return get(cdLacre, null);
	}

	public static Lacre get(int cdLacre, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM pcb_Lacre WHERE cd_lacre=?");
			pstmt.setInt(1, cdLacre);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Lacre(rs.getInt("cd_lacre"),
						rs.getString("nr_lacre"),
						rs.getString("txt_descricao"),
						(rs.getTimestamp("dt_Lacre")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_Lacre").getTime()),
						rs.getInt("cd_bomba"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LacreDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LacreDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM pcb_lacre");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LacreDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LacreDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM pcb_lacre","", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
