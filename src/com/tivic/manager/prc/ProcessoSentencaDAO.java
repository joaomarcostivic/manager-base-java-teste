package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;
import java.util.HashMap;

public class ProcessoSentencaDAO{

	public static int insert(ProcessoSentenca objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProcessoSentenca objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "CD_SENTENCA");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "CD_PROCESSO");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdProcesso()));
			int code = Conexao.getSequenceCode("PRC_PROCESSO_SENTENCA", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdSentenca(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_processo_sentenca (cd_sentenca,"+
			                                  "cd_processo,"+
			                                  "tp_sentenca,"+
			                                  "dt_sentenca,"+
			                                  "vl_sentenca,"+
			                                  "vl_acordo,"+
			                                  "vl_total) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProcesso());
			pstmt.setInt(3,objeto.getTpSentenca());
			if(objeto.getDtSentenca()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtSentenca().getTimeInMillis()));
			pstmt.setDouble(5,objeto.getVlSentenca());
			pstmt.setDouble(6,objeto.getVlAcordo());
			pstmt.setDouble(7,objeto.getVlTotal());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoSentencaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoSentencaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProcessoSentenca objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ProcessoSentenca objeto, int cdSentencaOld, int cdProcessoOld) {
		return update(objeto, cdSentencaOld, cdProcessoOld, null);
	}

	public static int update(ProcessoSentenca objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ProcessoSentenca objeto, int cdSentencaOld, int cdProcessoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_processo_sentenca SET cd_sentenca=?,"+
												      		   "cd_processo=?,"+
												      		   "tp_sentenca=?,"+
												      		   "dt_sentenca=?,"+
												      		   "vl_sentenca=?,"+
												      		   "vl_acordo=?,"+
												      		   "vl_total=? WHERE cd_sentenca=? AND cd_processo=?");
			pstmt.setInt(1,objeto.getCdSentenca());
			pstmt.setInt(2,objeto.getCdProcesso());
			pstmt.setInt(3,objeto.getTpSentenca());
			if(objeto.getDtSentenca()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtSentenca().getTimeInMillis()));
			pstmt.setDouble(5,objeto.getVlSentenca());
			pstmt.setDouble(6,objeto.getVlAcordo());
			pstmt.setDouble(7,objeto.getVlTotal());
			pstmt.setInt(8, cdSentencaOld!=0 ? cdSentencaOld : objeto.getCdSentenca());
			pstmt.setInt(9, cdProcessoOld!=0 ? cdProcessoOld : objeto.getCdProcesso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoSentencaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoSentencaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdSentenca, int cdProcesso) {
		return delete(cdSentenca, cdProcesso, null);
	}

	public static int delete(int cdSentenca, int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_processo_sentenca WHERE cd_sentenca=? AND cd_processo=?");
			pstmt.setInt(1, cdSentenca);
			pstmt.setInt(2, cdProcesso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoSentencaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoSentencaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProcessoSentenca get(int cdSentenca, int cdProcesso) {
		return get(cdSentenca, cdProcesso, null);
	}

	public static ProcessoSentenca get(int cdSentenca, int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_processo_sentenca WHERE cd_sentenca=? AND cd_processo=?");
			pstmt.setInt(1, cdSentenca);
			pstmt.setInt(2, cdProcesso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProcessoSentenca(rs.getInt("cd_sentenca"),
						rs.getInt("cd_processo"),
						rs.getInt("tp_sentenca"),
						(rs.getTimestamp("dt_sentenca")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_sentenca").getTime()),
						rs.getDouble("vl_sentenca"),
						rs.getDouble("vl_acordo"),
						rs.getDouble("vl_total"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoSentencaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoSentencaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_processo_sentenca");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoSentencaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoSentencaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ProcessoSentenca> getList() {
		return getList(null);
	}

	public static ArrayList<ProcessoSentenca> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ProcessoSentenca> list = new ArrayList<ProcessoSentenca>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ProcessoSentenca obj = ProcessoSentencaDAO.get(rsm.getInt("cd_sentenca"), rsm.getInt("cd_processo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoSentencaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM prc_processo_sentenca", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}