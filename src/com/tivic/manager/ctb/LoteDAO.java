package com.tivic.manager.ctb;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class LoteDAO{

	public static int insert(Lote objeto) {
		return insert(objeto, null);
	}

	public static int insert(Lote objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ctb_lote", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLote(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ctb_lote (cd_lote,"+
			                                  "cd_usuario,"+
			                                  "nm_lote,"+
			                                  "nr_lote,"+
			                                  "dt_abertura,"+
			                                  "dt_encerramento,"+
			                                  "st_lote,"+
			                                  "cd_empresa,"+
			                                  "vl_lote) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdUsuario());
			pstmt.setString(3,objeto.getNmLote());
			pstmt.setString(4,objeto.getNrLote());
			if(objeto.getDtAbertura()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtAbertura().getTimeInMillis()));
			if(objeto.getDtEncerramento()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtEncerramento().getTimeInMillis()));
			pstmt.setInt(7,objeto.getStLote());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdEmpresa());
			pstmt.setFloat(9,objeto.getVlLote());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
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
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ctb_lote SET cd_lote=?,"+
												      		   "cd_usuario=?,"+
												      		   "nm_lote=?,"+
												      		   "nr_lote=?,"+
												      		   "dt_abertura=?,"+
												      		   "dt_encerramento=?,"+
												      		   "st_lote=?,"+
												      		   "cd_empresa=?,"+
												      		   "vl_lote=? WHERE cd_lote=?");
			pstmt.setInt(1,objeto.getCdLote());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdUsuario());
			pstmt.setString(3,objeto.getNmLote());
			pstmt.setString(4,objeto.getNrLote());
			if(objeto.getDtAbertura()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtAbertura().getTimeInMillis()));
			if(objeto.getDtEncerramento()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtEncerramento().getTimeInMillis()));
			pstmt.setInt(7,objeto.getStLote());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdEmpresa());
			pstmt.setFloat(9,objeto.getVlLote());
			pstmt.setInt(10, cdLoteOld!=0 ? cdLoteOld : objeto.getCdLote());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDAO.update: " +  e);
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
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ctb_lote WHERE cd_lote=?");
			pstmt.setInt(1, cdLote);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDAO.delete: " +  e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_lote WHERE cd_lote=?");
			pstmt.setInt(1, cdLote);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Lote(rs.getInt("cd_lote"),
						rs.getInt("cd_usuario"),
						rs.getString("nm_lote"),
						rs.getString("nr_lote"),
						(rs.getTimestamp("dt_abertura")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_abertura").getTime()),
						(rs.getTimestamp("dt_encerramento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_encerramento").getTime()),
						rs.getInt("st_lote"),
						rs.getInt("cd_empresa"),
						rs.getFloat("vl_lote"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_lote");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM ctb_lote", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
