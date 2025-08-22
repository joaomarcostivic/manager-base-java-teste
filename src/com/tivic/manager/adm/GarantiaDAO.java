package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class GarantiaDAO{

	public static int insert(Garantia objeto) {
		return insert(objeto, null);
	}

	public static int insert(Garantia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_garantia", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdGarantia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_garantia (cd_garantia,"+
			                                  "cd_seguradora,"+
			                                  "cd_administradora,"+
			                                  "dt_garantia,"+
			                                  "tp_garantia,"+
			                                  "nr_tempo_garantia,"+
			                                  "cd_doc_saida_referencia) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdSeguradora()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdSeguradora());
			if(objeto.getCdAdministradora()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAdministradora());
			if(objeto.getDtGarantia()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtGarantia().getTimeInMillis()));
			pstmt.setInt(5,objeto.getTpGarantia());
			pstmt.setInt(6,objeto.getNrTempoGarantia());
			if(objeto.getCdDocSaidaReferencia()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdDocSaidaReferencia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GarantiaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GarantiaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Garantia objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Garantia objeto, int cdGarantiaOld) {
		return update(objeto, cdGarantiaOld, null);
	}

	public static int update(Garantia objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Garantia objeto, int cdGarantiaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_garantia SET cd_garantia=?,"+
												      		   "cd_seguradora=?,"+
												      		   "cd_administradora=?,"+
												      		   "dt_garantia=?,"+
												      		   "tp_garantia=?,"+
												      		   "nr_tempo_garantia=?,"+
												      		   "cd_doc_saida_referencia=? WHERE cd_garantia=?");
			pstmt.setInt(1,objeto.getCdGarantia());
			if(objeto.getCdSeguradora()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdSeguradora());
			if(objeto.getCdAdministradora()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAdministradora());
			if(objeto.getDtGarantia()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtGarantia().getTimeInMillis()));
			pstmt.setInt(5,objeto.getTpGarantia());
			pstmt.setInt(6,objeto.getNrTempoGarantia());
			if(objeto.getCdDocSaidaReferencia()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdDocSaidaReferencia());
			pstmt.setInt(8, cdGarantiaOld!=0 ? cdGarantiaOld : objeto.getCdGarantia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GarantiaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GarantiaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdGarantia) {
		return delete(cdGarantia, null);
	}

	public static int delete(int cdGarantia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_garantia WHERE cd_garantia=?");
			pstmt.setInt(1, cdGarantia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GarantiaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GarantiaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Garantia get(int cdGarantia) {
		return get(cdGarantia, null);
	}

	public static Garantia get(int cdGarantia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_garantia WHERE cd_garantia=?");
			pstmt.setInt(1, cdGarantia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Garantia(rs.getInt("cd_garantia"),
						rs.getInt("cd_seguradora"),
						rs.getInt("cd_administradora"),
						(rs.getTimestamp("dt_garantia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_garantia").getTime()),
						rs.getInt("tp_garantia"),
						rs.getInt("nr_tempo_garantia"),
						rs.getInt("cd_doc_saida_referencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GarantiaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GarantiaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_garantia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GarantiaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GarantiaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_garantia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
