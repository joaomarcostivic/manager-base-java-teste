package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContratoCarneDAO{

	public static int insert(ContratoCarne objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(ContratoCarne objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_contrato");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdContrato()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_carne");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("adm_contrato_carne", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCarne(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_contrato_carne (cd_contrato,"+
			                                  "cd_carne,"+
			                                  "nr_via,"+
			                                  "dt_lancamento) VALUES (?, ?, ?, ?)");
			if(objeto.getCdContrato()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContrato());
			pstmt.setInt(2, code);
			pstmt.setInt(3,objeto.getNrVia());
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoCarneDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoCarneDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContratoCarne objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ContratoCarne objeto, int cdContratoOld, int cdCarneOld) {
		return update(objeto, cdContratoOld, cdCarneOld, null);
	}

	public static int update(ContratoCarne objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ContratoCarne objeto, int cdContratoOld, int cdCarneOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_contrato_carne SET cd_contrato=?,"+
												      		   "cd_carne=?,"+
												      		   "nr_via=?,"+
												      		   "dt_lancamento=? WHERE cd_contrato=? AND cd_carne=?");
			pstmt.setInt(1,objeto.getCdContrato());
			pstmt.setInt(2,objeto.getCdCarne());
			pstmt.setInt(3,objeto.getNrVia());
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			pstmt.setInt(5, cdContratoOld!=0 ? cdContratoOld : objeto.getCdContrato());
			pstmt.setInt(6, cdCarneOld!=0 ? cdCarneOld : objeto.getCdCarne());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoCarneDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoCarneDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContrato, int cdCarne) {
		return delete(cdContrato, cdCarne, null);
	}

	public static int delete(int cdContrato, int cdCarne, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_contrato_carne WHERE cd_contrato=? AND cd_carne=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdCarne);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoCarneDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoCarneDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContratoCarne get(int cdContrato, int cdCarne) {
		return get(cdContrato, cdCarne, null);
	}

	public static ContratoCarne get(int cdContrato, int cdCarne, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_carne WHERE cd_contrato=? AND cd_carne=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdCarne);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContratoCarne(rs.getInt("cd_contrato"),
						rs.getInt("cd_carne"),
						rs.getInt("nr_via"),
						(rs.getTimestamp("dt_lancamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_lancamento").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoCarneDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoCarneDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_carne");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoCarneDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoCarneDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_contrato_carne", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
