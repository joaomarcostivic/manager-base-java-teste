package com.tivic.manager.log;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class ExecucaoAcaoDAO{

	public static int insert(ExecucaoAcao objeto) {
		return insert(objeto, null);
	}

	public static int insert(ExecucaoAcao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[4];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_execucao");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_acao");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdAcao()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_modulo");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdModulo()));
			keys[3] = new HashMap<String,Object>();
			keys[3].put("FIELD_NAME", "cd_sistema");
			keys[3].put("IS_KEY_NATIVE", "NO");
			keys[3].put("FIELD_VALUE", new Integer(objeto.getCdSistema()));
			int code = Conexao.getSequenceCode("log_execucao_acao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdExecucao(code);
						
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO log_execucao_acao (cd_execucao,"+
			                                  "cd_acao,"+
			                                  "cd_modulo,"+
			                                  "cd_sistema,"+
			                                  "cd_usuario,"+
			                                  "dt_execucao,"+
			                                  "txt_execucao,"+
			                                  "txt_resultado_execucao,"+
			                                  "tp_resultado_execucao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdAcao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAcao());
			if(objeto.getCdModulo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdModulo());
			if(objeto.getCdSistema()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdSistema());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdUsuario());
			if(objeto.getDtExecucao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtExecucao().getTimeInMillis()));
			pstmt.setString(7,objeto.getTxtExecucao());
			pstmt.setString(8,objeto.getTxtResultadoExecucao());
			pstmt.setInt(9,objeto.getTpResultadoExecucao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExecucaoAcaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ExecucaoAcaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ExecucaoAcao objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(ExecucaoAcao objeto, int cdExecucaoOld, int cdAcaoOld, int cdModuloOld, int cdSistemaOld) {
		return update(objeto, cdExecucaoOld, cdAcaoOld, cdModuloOld, cdSistemaOld, null);
	}

	public static int update(ExecucaoAcao objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(ExecucaoAcao objeto, int cdExecucaoOld, int cdAcaoOld, int cdModuloOld, int cdSistemaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE log_execucao_acao SET cd_execucao=?,"+
												      		   "cd_acao=?,"+
												      		   "cd_modulo=?,"+
												      		   "cd_sistema=?,"+
												      		   "cd_usuario=?,"+
												      		   "dt_execucao=?,"+
												      		   "txt_execucao=?,"+
												      		   "txt_resultado_execucao=?,"+
												      		   "tp_resultado_execucao=? WHERE cd_execucao=? AND cd_acao=? AND cd_modulo=? AND cd_sistema=?");
			pstmt.setInt(1,objeto.getCdExecucao());
			pstmt.setInt(2,objeto.getCdAcao());
			pstmt.setInt(3,objeto.getCdModulo());
			pstmt.setInt(4,objeto.getCdSistema());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdUsuario());
			if(objeto.getDtExecucao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtExecucao().getTimeInMillis()));
			pstmt.setString(7,objeto.getTxtExecucao());
			pstmt.setString(8,objeto.getTxtResultadoExecucao());
			pstmt.setInt(9,objeto.getTpResultadoExecucao());
			pstmt.setInt(10, cdExecucaoOld!=0 ? cdExecucaoOld : objeto.getCdExecucao());
			pstmt.setInt(11, cdAcaoOld!=0 ? cdAcaoOld : objeto.getCdAcao());
			pstmt.setInt(12, cdModuloOld!=0 ? cdModuloOld : objeto.getCdModulo());
			pstmt.setInt(13, cdSistemaOld!=0 ? cdSistemaOld : objeto.getCdSistema());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExecucaoAcaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ExecucaoAcaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdExecucao, int cdAcao, int cdModulo, int cdSistema) {
		return delete(cdExecucao, cdAcao, cdModulo, cdSistema, null);
	}

	public static int delete(int cdExecucao, int cdAcao, int cdModulo, int cdSistema, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM log_execucao_acao WHERE cd_execucao=? AND cd_acao=? AND cd_modulo=? AND cd_sistema=?");
			pstmt.setInt(1, cdExecucao);
			pstmt.setInt(2, cdAcao);
			pstmt.setInt(3, cdModulo);
			pstmt.setInt(4, cdSistema);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExecucaoAcaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ExecucaoAcaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ExecucaoAcao get(int cdExecucao, int cdAcao, int cdModulo, int cdSistema) {
		return get(cdExecucao, cdAcao, cdModulo, cdSistema, null);
	}

	public static ExecucaoAcao get(int cdExecucao, int cdAcao, int cdModulo, int cdSistema, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM log_execucao_acao WHERE cd_execucao=? AND cd_acao=? AND cd_modulo=? AND cd_sistema=?");
			pstmt.setInt(1, cdExecucao);
			pstmt.setInt(2, cdAcao);
			pstmt.setInt(3, cdModulo);
			pstmt.setInt(4, cdSistema);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ExecucaoAcao(rs.getInt("cd_execucao"),
						rs.getInt("cd_acao"),
						rs.getInt("cd_modulo"),
						rs.getInt("cd_sistema"),
						rs.getInt("cd_usuario"),
						(rs.getTimestamp("dt_execucao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_execucao").getTime()),
						rs.getString("txt_execucao"),
						rs.getString("txt_resultado_execucao"),
						rs.getInt("tp_resultado_execucao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExecucaoAcaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExecucaoAcaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM log_execucao_acao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExecucaoAcaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExecucaoAcaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ExecucaoAcao> getList() {
		return getList(null);
	}

	public static ArrayList<ExecucaoAcao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ExecucaoAcao> list = new ArrayList<ExecucaoAcao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ExecucaoAcao obj = ExecucaoAcaoDAO.get(rsm.getInt("cd_execucao"), rsm.getInt("cd_acao"), rsm.getInt("cd_modulo"), rsm.getInt("cd_sistema"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExecucaoAcaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM log_execucao_acao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
