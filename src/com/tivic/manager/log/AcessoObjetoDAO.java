package com.tivic.manager.log;

import java.sql.*;
import sol.dao.*;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AcessoObjetoDAO{

	public static int insert(AcessoObjeto objeto) {
		return insert(objeto, null);
	}

	public static int insert(AcessoObjeto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = SistemaDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLog(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO log_acesso_objeto (cd_acesso,"+
			                                  "cd_formulario,"+
			                                  "cd_objeto,"+
			                                  "cd_sistema,"+
			                                  "txt_resultado_acesso,"+
			                                  "tp_resultado_acesso) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdLog()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdLog());
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFormulario());
			if(objeto.getCdObjeto()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdObjeto());
			if(objeto.getCdSistema()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdSistema());
			pstmt.setString(5,objeto.getTxtResultadoAcesso());
			pstmt.setInt(6,objeto.getTpResultadoAcesso());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcessoObjetoDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcessoObjetoDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AcessoObjeto objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AcessoObjeto objeto, int cdAcessoOld) {
		return update(objeto, cdAcessoOld, null);
	}

	public static int update(AcessoObjeto objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AcessoObjeto objeto, int cdAcessoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			AcessoObjeto objetoTemp = get(objeto.getCdLog(), connect);
			if (objetoTemp == null) 
				pstmt = connect.prepareStatement("INSERT INTO log_acesso_objeto (cd_acesso,"+
			                                  "cd_formulario,"+
			                                  "cd_objeto,"+
			                                  "cd_sistema,"+
			                                  "txt_resultado_acesso,"+
			                                  "tp_resultado_acesso) VALUES (?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE log_acesso_objeto SET cd_acesso=?,"+
												      		   "cd_formulario=?,"+
												      		   "cd_objeto=?,"+
												      		   "cd_sistema=?,"+
												      		   "txt_resultado_acesso=?,"+
												      		   "tp_resultado_acesso=? WHERE cd_acesso=?");
			pstmt.setInt(1,objeto.getCdLog());
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFormulario());
			if(objeto.getCdObjeto()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdObjeto());
			if(objeto.getCdSistema()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdSistema());
			pstmt.setString(5,objeto.getTxtResultadoAcesso());
			pstmt.setInt(6,objeto.getTpResultadoAcesso());
			if (objetoTemp != null) {
				pstmt.setInt(7, cdAcessoOld!=0 ? cdAcessoOld : objeto.getCdLog());
			}
			pstmt.executeUpdate();
			if (SistemaDAO.update(objeto, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcessoObjetoDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcessoObjetoDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAcesso) {
		return delete(cdAcesso, null);
	}

	public static int delete(int cdAcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM log_acesso_objeto WHERE cd_acesso=?");
			pstmt.setInt(1, cdAcesso);
			pstmt.executeUpdate();
			if (SistemaDAO.delete(cdAcesso, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcessoObjetoDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcessoObjetoDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AcessoObjeto get(int cdAcesso) {
		return get(cdAcesso, null);
	}

	public static AcessoObjeto get(int cdAcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM log_acesso_objeto A, log_sistema B WHERE A.cd_acesso=B.cd_log AND A.cd_acesso=?");
			pstmt.setInt(1, cdAcesso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AcessoObjeto(rs.getInt("cd_log"),
						(rs.getTimestamp("dt_log")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_log").getTime()),
						rs.getString("txt_log"),
						rs.getInt("tp_log"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_formulario"),
						rs.getInt("cd_objeto"),
						rs.getInt("cd_sistema"),
						rs.getString("txt_resultado_acesso"),
						rs.getInt("tp_resultado_acesso"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcessoObjetoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcessoObjetoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM log_acesso_objeto");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcessoObjetoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcessoObjetoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM log_acesso_objeto", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
