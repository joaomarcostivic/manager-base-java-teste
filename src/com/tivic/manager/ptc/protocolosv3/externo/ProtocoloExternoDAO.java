package com.tivic.manager.ptc.protocolosv3.externo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;

public class ProtocoloExternoDAO{

	public static int insert(ProtocoloExterno objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProtocoloExterno objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_documento");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdDocumento()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_documento_externo");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("mob_documento_externo", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDocumentoExterno(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_documento_externo (cd_documento_externo,"+
			                                  "cd_documento,"+
			                                  "id_ait,"+
			                                  "nr_placa,"+
			                                  "cd_infracao,"+
			                                  "nr_renainf,"+
			                                  "dt_entrada,"+
			                                  "nm_condutor," +
                    						  "cd_orgao_externo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDocumento());
			pstmt.setString(3,objeto.getIdAit());
			pstmt.setString(4,objeto.getNrPlaca());
			pstmt.setInt(5,objeto.getCdInfracao());
			pstmt.setString(6,objeto.getNrRenainf());
			if(objeto.getDtEntrada()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtEntrada().getTimeInMillis()));
			pstmt.setString(8, objeto.getNmCondutor());
			if(objeto.getCdOrgaoExterno()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9, objeto.getCdOrgaoExterno());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProtocoloExternoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProtocoloExternoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProtocoloExterno objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ProtocoloExterno objeto, int cdDocumentoOld, int cdDocumentoExternoOld) {
		return update(objeto, cdDocumentoOld, cdDocumentoExternoOld, null);
	}

	public static int update(ProtocoloExterno objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ProtocoloExterno objeto, int cdDocumentoOld, int cdDocumentoExternoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_documento_externo SET cd_documento_externo=?,"+
												      		   "cd_documento=?,"+
												      		   "id_ait=?,"+
												      		   "nr_placa=?,"+
												      		   "cd_infracao=?,"+
												      		   "nr_renainf=?,"+
												      		   "dt_entrada=?,"+
												      		   "nm_condutor=?,"+
												      		   "cd_orgao_externo=? WHERE cd_documento=? AND cd_documento_externo=?");
			pstmt.setInt(1,objeto.getCdDocumentoExterno());
			pstmt.setInt(2,objeto.getCdDocumento());
			pstmt.setString(3,objeto.getIdAit());
			pstmt.setString(4,objeto.getNrPlaca());
			pstmt.setInt(5,objeto.getCdInfracao());
			pstmt.setString(6,objeto.getNrRenainf());
			if(objeto.getDtEntrada()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtEntrada().getTimeInMillis()));
			pstmt.setString(8, objeto.getNmCondutor());
			pstmt.setInt(9, objeto.getCdOrgaoExterno());
			pstmt.setInt(10, cdDocumentoOld!=0 ? cdDocumentoOld : objeto.getCdDocumento());
			pstmt.setInt(11, cdDocumentoExternoOld!=0 ? cdDocumentoExternoOld : objeto.getCdDocumentoExterno());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProtocoloExternoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProtocoloExternoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDocumento, int cdDocumentoExterno) {
		return delete(cdDocumento, cdDocumentoExterno, null);
	}

	public static int delete(int cdDocumento, int cdDocumentoExterno, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_documento_externo WHERE cd_documento=? AND cd_documento_externo=?");
			pstmt.setInt(1, cdDocumento);
			pstmt.setInt(2, cdDocumentoExterno);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProtocoloExternoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProtocoloExternoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ProtocoloExterno get(int cdDocumentoExterno) {
		return get(cdDocumentoExterno, null);
	}

	public static ProtocoloExterno get(int cdDocumentoExterno, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_documento_externo WHERE cd_documento_externo=?");
			pstmt.setInt(1, cdDocumentoExterno);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProtocoloExterno(rs.getInt("cd_documento_externo"),
						rs.getInt("cd_documento"),
						rs.getString("id_ait"),
						rs.getString("nr_placa"),
						rs.getInt("cd_infracao"),
						rs.getString("nr_renainf"),
						(rs.getTimestamp("dt_entrada")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_entrada").getTime()),
						rs.getInt("cd_orgao_externo"),
						rs.getString("nm_condutor"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProtocoloExternoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProtocoloExternoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProtocoloExterno get(int cdDocumento, int cdDocumentoExterno) {
		return get(cdDocumento, cdDocumentoExterno, null);
	}

	public static ProtocoloExterno get(int cdDocumento, int cdDocumentoExterno, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_documento_externo WHERE cd_documento=? AND cd_documento_externo=?");
			pstmt.setInt(1, cdDocumento);
			pstmt.setInt(2, cdDocumentoExterno);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProtocoloExterno(rs.getInt("cd_documento_externo"),
						rs.getInt("cd_documento"),
						rs.getString("id_ait"),
						rs.getString("nr_placa"),
						rs.getInt("cd_infracao"),
						rs.getString("nr_renainf"),
						(rs.getTimestamp("dt_entrada")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_entrada").getTime()),
						rs.getInt("cd_orgao_externo"),
						rs.getString("nm_condutor"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProtocoloExternoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProtocoloExternoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_documento_externo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProtocoloExternoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProtocoloExternoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ProtocoloExterno> getList() {
		return getList(null);
	}

	public static ArrayList<ProtocoloExterno> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ProtocoloExterno> list = new ArrayList<ProtocoloExterno>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ProtocoloExterno obj = ProtocoloExternoDAO.get(rsm.getInt("cd_documento"), rsm.getInt("cd_documento_externo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProtocoloExternoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_documento_externo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}