package com.tivic.manager.acd;

import java.sql.*;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;

import java.util.HashMap;
import java.util.ArrayList;

public class AulaOcorrenciaDAO{

	public static int insert(AulaOcorrencia objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(AulaOcorrencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_aula");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdAula()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_ocorrencia");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("acd_aula_ocorrencia", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOcorrencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_aula_ocorrencia (cd_aula,"+
			                                  "cd_ocorrencia,"+
			                                  "txt_ocorrencia,"+
			                                  "dt_ocorrencia) VALUES (?, ?, ?, ?)");
			if(objeto.getCdAula()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAula());
			pstmt.setInt(2, code);
			pstmt.setString(3,objeto.getTxtOcorrencia());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaOcorrenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaOcorrenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AulaOcorrencia objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AulaOcorrencia objeto, int cdAulaOld, int cdOcorrenciaOld) {
		return update(objeto, cdAulaOld, cdOcorrenciaOld, null);
	}

	public static int update(AulaOcorrencia objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AulaOcorrencia objeto, int cdAulaOld, int cdOcorrenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_aula_ocorrencia SET cd_aula=?,"+
												      		   "cd_ocorrencia=?,"+
												      		   "txt_ocorrencia=?,"+
												      		   "dt_ocorrencia=? WHERE cd_aula=? AND cd_ocorrencia=?");
			pstmt.setInt(1,objeto.getCdAula());
			pstmt.setInt(2,objeto.getCdOcorrencia());
			pstmt.setString(3,objeto.getTxtOcorrencia());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			pstmt.setInt(5, cdAulaOld!=0 ? cdAulaOld : objeto.getCdAula());
			pstmt.setInt(6, cdOcorrenciaOld!=0 ? cdOcorrenciaOld : objeto.getCdOcorrencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaOcorrenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaOcorrenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAula, int cdOcorrencia) {
		return delete(cdAula, cdOcorrencia, null);
	}

	public static int delete(int cdAula, int cdOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_aula_ocorrencia WHERE cd_aula=? AND cd_ocorrencia=?");
			pstmt.setInt(1, cdAula);
			pstmt.setInt(2, cdOcorrencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaOcorrenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaOcorrenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AulaOcorrencia get(int cdAula, int cdOcorrencia) {
		return get(cdAula, cdOcorrencia, null);
	}

	public static AulaOcorrencia get(int cdAula, int cdOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_aula_ocorrencia WHERE cd_aula=? AND cd_ocorrencia=?");
			pstmt.setInt(1, cdAula);
			pstmt.setInt(2, cdOcorrencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AulaOcorrencia(rs.getInt("cd_aula"),
						rs.getInt("cd_ocorrencia"),
						rs.getString("txt_ocorrencia"),
						(rs.getTimestamp("dt_ocorrencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ocorrencia").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaOcorrenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaOcorrenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_aula_ocorrencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaOcorrenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaOcorrenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AulaOcorrencia> getList() {
		return getList(null);
	}

	public static ArrayList<AulaOcorrencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AulaOcorrencia> list = new ArrayList<AulaOcorrencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AulaOcorrencia obj = AulaOcorrenciaDAO.get(rsm.getInt("cd_aula"), rsm.getInt("cd_ocorrencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaOcorrenciaDAO.getList: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
