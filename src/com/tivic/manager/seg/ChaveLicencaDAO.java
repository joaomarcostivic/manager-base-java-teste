package com.tivic.manager.seg;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class ChaveLicencaDAO{

	public static int insert(ChaveLicenca objeto) {
		return insert(objeto, null);
	}

	public static int insert(ChaveLicenca objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_chave");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_licenca");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdLicenca()));
			int code = Conexao.getSequenceCode("seg_chave_licenca", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdChave(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO seg_chave_licenca (cd_chave,"+
			                                  "cd_licenca,"+
			                                  "txt_chave,"+
			                                  "dt_criacao,"+
			                                  "dt_expiracao) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdLicenca()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdLicenca());
			pstmt.setString(3,objeto.getTxtChave());
			if(objeto.getDtCriacao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtCriacao().getTimeInMillis()));
			if(objeto.getDtExpiracao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtExpiracao().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ChaveLicencaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ChaveLicencaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ChaveLicenca objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ChaveLicenca objeto, int cdChaveOld, int cdLicencaOld) {
		return update(objeto, cdChaveOld, cdLicencaOld, null);
	}

	public static int update(ChaveLicenca objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ChaveLicenca objeto, int cdChaveOld, int cdLicencaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE seg_chave_licenca SET cd_chave=?,"+
												      		   "cd_licenca=?,"+
												      		   "txt_chave=?,"+
												      		   "dt_criacao=?,"+
												      		   "dt_expiracao=? WHERE cd_chave=? AND cd_licenca=?");
			pstmt.setInt(1,objeto.getCdChave());
			pstmt.setInt(2,objeto.getCdLicenca());
			pstmt.setString(3,objeto.getTxtChave());
			if(objeto.getDtCriacao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtCriacao().getTimeInMillis()));
			if(objeto.getDtExpiracao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtExpiracao().getTimeInMillis()));
			pstmt.setInt(6, cdChaveOld!=0 ? cdChaveOld : objeto.getCdChave());
			pstmt.setInt(7, cdLicencaOld!=0 ? cdLicencaOld : objeto.getCdLicenca());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ChaveLicencaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ChaveLicencaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdChave, int cdLicenca) {
		return delete(cdChave, cdLicenca, null);
	}

	public static int delete(int cdChave, int cdLicenca, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM seg_chave_licenca WHERE cd_chave=? AND cd_licenca=?");
			pstmt.setInt(1, cdChave);
			pstmt.setInt(2, cdLicenca);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ChaveLicencaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ChaveLicencaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ChaveLicenca get(int cdChave, int cdLicenca) {
		return get(cdChave, cdLicenca, null);
	}

	public static ChaveLicenca get(int cdChave, int cdLicenca, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_chave_licenca WHERE cd_chave=? AND cd_licenca=?");
			pstmt.setInt(1, cdChave);
			pstmt.setInt(2, cdLicenca);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ChaveLicenca(rs.getInt("cd_chave"),
						rs.getInt("cd_licenca"),
						rs.getString("txt_chave"),
						(rs.getTimestamp("dt_criacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_criacao").getTime()),
						(rs.getTimestamp("dt_expiracao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_expiracao").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ChaveLicencaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChaveLicencaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_chave_licenca");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ChaveLicencaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChaveLicencaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ChaveLicenca> getList() {
		return getList(null);
	}

	public static ArrayList<ChaveLicenca> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ChaveLicenca> list = new ArrayList<ChaveLicenca>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ChaveLicenca obj = ChaveLicencaDAO.get(rsm.getInt("cd_chave"), rsm.getInt("cd_licenca"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChaveLicencaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM seg_chave_licenca", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
