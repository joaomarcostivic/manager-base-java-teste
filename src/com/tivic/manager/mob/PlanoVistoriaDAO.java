package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class PlanoVistoriaDAO{

	public static int insert(PlanoVistoria objeto) {
		return insert(objeto, null);
	}

	public static int insert(PlanoVistoria objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_plano_vistoria", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPlanoVistoria(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_plano_vistoria (cd_plano_vistoria,"+
			                                  "nm_plano_vistoria,"+
			                                  "cd_formulario,"+
			                                  "tp_concessao) VALUES (?, ?, ?,?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmPlanoVistoria());
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdFormulario());
			pstmt.setInt(4,objeto.getTpConcessao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PlanoVistoria objeto) {
		return update(objeto, 0, null);
	}

	public static int update(PlanoVistoria objeto, int cdPlanoVistoriaOld) {
		return update(objeto, cdPlanoVistoriaOld, null);
	}

	public static int update(PlanoVistoria objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(PlanoVistoria objeto, int cdPlanoVistoriaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_plano_vistoria SET cd_plano_vistoria=?,"+
												      		   "nm_plano_vistoria=?,"+
												      		   "cd_formulario=?,"+
												      		   "tp_concessao=? WHERE cd_plano_vistoria=?");
			pstmt.setInt(1,objeto.getCdPlanoVistoria());
			pstmt.setString(2,objeto.getNmPlanoVistoria());
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdFormulario());
			pstmt.setInt(4,objeto.getTpConcessao());
			pstmt.setInt(5, cdPlanoVistoriaOld!=0 ? cdPlanoVistoriaOld : objeto.getCdPlanoVistoria());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPlanoVistoria) {
		return delete(cdPlanoVistoria, null);
	}

	public static int delete(int cdPlanoVistoria, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_plano_vistoria WHERE cd_plano_vistoria=?");
			pstmt.setInt(1, cdPlanoVistoria);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PlanoVistoria get(int cdPlanoVistoria) {
		return get(cdPlanoVistoria, null);
	}

	public static PlanoVistoria get(int cdPlanoVistoria, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_plano_vistoria WHERE cd_plano_vistoria=?");
			pstmt.setInt(1, cdPlanoVistoria);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PlanoVistoria(rs.getInt("cd_plano_vistoria"),
						rs.getString("nm_plano_vistoria"),
						rs.getInt("cd_formulario"),
						rs.getInt("cd_concessao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_plano_vistoria");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PlanoVistoria> getList() {
		return getList(null);
	}

	public static ArrayList<PlanoVistoria> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PlanoVistoria> list = new ArrayList<PlanoVistoria>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PlanoVistoria obj = PlanoVistoriaDAO.get(rsm.getInt("cd_plano_vistoria"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_plano_vistoria", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
