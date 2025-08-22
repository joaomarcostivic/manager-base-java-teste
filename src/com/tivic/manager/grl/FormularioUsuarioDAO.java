package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class FormularioUsuarioDAO{

	public static int insert(FormularioUsuario objeto) {
		return insert(objeto, null);
	}

	public static int insert(FormularioUsuario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_formulario_usuario", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFormularioUsuario(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_formulario_usuario (cd_formulario_usuario,"+
			                                  "cd_formulario,"+
			                                  "cd_usuario,"+
			                                  "st_resposta,"+
			                                  "qt_visualizacao,"+
			                                  "txt_observacao,"+
			                                  "dt_resposta) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFormulario());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdUsuario());
			pstmt.setInt(4,objeto.getStResposta());
			pstmt.setInt(5,objeto.getQtVisualizacao());
			pstmt.setString(6,objeto.getTxtObservacao());
			if(objeto.getDtResposta()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtResposta().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioUsuarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioUsuarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FormularioUsuario objeto) {
		return update(objeto, 0, null);
	}

	public static int update(FormularioUsuario objeto, int cdFormularioUsuarioOld) {
		return update(objeto, cdFormularioUsuarioOld, null);
	}

	public static int update(FormularioUsuario objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(FormularioUsuario objeto, int cdFormularioUsuarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_formulario_usuario SET cd_formulario_usuario=?,"+
												      		   "cd_formulario=?,"+
												      		   "cd_usuario=?,"+
												      		   "st_resposta=?,"+
												      		   "qt_visualizacao=?,"+
												      		   "txt_observacao=?,"+
												      		   "dt_resposta=? WHERE cd_formulario_usuario=?");
			pstmt.setInt(1,objeto.getCdFormularioUsuario());
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFormulario());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdUsuario());
			pstmt.setInt(4,objeto.getStResposta());
			pstmt.setInt(5,objeto.getQtVisualizacao());
			pstmt.setString(6,objeto.getTxtObservacao());
			if(objeto.getDtResposta()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtResposta().getTimeInMillis()));
			pstmt.setInt(8, cdFormularioUsuarioOld!=0 ? cdFormularioUsuarioOld : objeto.getCdFormularioUsuario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioUsuarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioUsuarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFormularioUsuario) {
		return delete(cdFormularioUsuario, null);
	}

	public static int delete(int cdFormularioUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_formulario_usuario WHERE cd_formulario_usuario=?");
			pstmt.setInt(1, cdFormularioUsuario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioUsuarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioUsuarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FormularioUsuario get(int cdFormularioUsuario) {
		return get(cdFormularioUsuario, null);
	}

	public static FormularioUsuario get(int cdFormularioUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_formulario_usuario WHERE cd_formulario_usuario=?");
			pstmt.setInt(1, cdFormularioUsuario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FormularioUsuario(rs.getInt("cd_formulario_usuario"),
						rs.getInt("cd_formulario"),
						rs.getInt("cd_usuario"),
						rs.getInt("st_resposta"),
						rs.getInt("qt_visualizacao"),
						rs.getString("txt_observacao"),
						(rs.getTimestamp("dt_resposta")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_resposta").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioUsuarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioUsuarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_formulario_usuario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioUsuarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioUsuarioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<FormularioUsuario> getList() {
		return getList(null);
	}

	public static ArrayList<FormularioUsuario> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<FormularioUsuario> list = new ArrayList<FormularioUsuario>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				FormularioUsuario obj = FormularioUsuarioDAO.get(rsm.getInt("cd_formulario_usuario"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioUsuarioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_formulario_usuario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
