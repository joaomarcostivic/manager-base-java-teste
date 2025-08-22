package com.tivic.manager.mcr;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AtaComiteDAO{

	public static int insert(AtaComite objeto) {
		return insert(objeto, null);
	}

	public static int insert(AtaComite objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mcr_ata_comite", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAtaComite(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mcr_ata_comite (cd_ata_comite,"+
			                                  "dt_ata,"+
			                                  "txt_observacao) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getDtAta()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtAta().getTimeInMillis()));
			pstmt.setString(3,objeto.getTxtObservacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtaComiteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtaComiteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AtaComite objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AtaComite objeto, int cdAtaComiteOld) {
		return update(objeto, cdAtaComiteOld, null);
	}

	public static int update(AtaComite objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AtaComite objeto, int cdAtaComiteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mcr_ata_comite SET cd_ata_comite=?,"+
												      		   "dt_ata=?,"+
												      		   "txt_observacao=? WHERE cd_ata_comite=?");
			pstmt.setInt(1,objeto.getCdAtaComite());
			if(objeto.getDtAta()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtAta().getTimeInMillis()));
			pstmt.setString(3,objeto.getTxtObservacao());
			pstmt.setInt(4, cdAtaComiteOld!=0 ? cdAtaComiteOld : objeto.getCdAtaComite());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtaComiteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtaComiteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAtaComite) {
		return delete(cdAtaComite, null);
	}

	public static int delete(int cdAtaComite, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mcr_ata_comite WHERE cd_ata_comite=?");
			pstmt.setInt(1, cdAtaComite);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtaComiteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtaComiteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AtaComite get(int cdAtaComite) {
		return get(cdAtaComite, null);
	}

	public static AtaComite get(int cdAtaComite, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_ata_comite WHERE cd_ata_comite=?");
			pstmt.setInt(1, cdAtaComite);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AtaComite(rs.getInt("cd_ata_comite"),
						(rs.getTimestamp("dt_ata")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ata").getTime()),
						rs.getString("txt_observacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtaComiteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtaComiteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_ata_comite");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtaComiteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtaComiteDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM mcr_ata_comite", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
