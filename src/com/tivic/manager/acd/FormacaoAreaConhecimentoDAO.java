package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class FormacaoAreaConhecimentoDAO{

	public static int insert(FormacaoAreaConhecimento objeto) {
		return insert(objeto, null);
	}

	public static int insert(FormacaoAreaConhecimento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_formacao_area_conhecimento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFormacaoAreaConhecimento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_formacao_area_conhecimento (cd_formacao_area_conhecimento,"+
			                                  "nm_area,"+
			                                  "id_area) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmArea());
			pstmt.setString(3,objeto.getIdArea());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoAreaConhecimentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoAreaConhecimentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FormacaoAreaConhecimento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(FormacaoAreaConhecimento objeto, int cdFormacaoAreaConhecimentoOld) {
		return update(objeto, cdFormacaoAreaConhecimentoOld, null);
	}

	public static int update(FormacaoAreaConhecimento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(FormacaoAreaConhecimento objeto, int cdFormacaoAreaConhecimentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_formacao_area_conhecimento SET cd_formacao_area_conhecimento=?,"+
												      		   "nm_area=?,"+
												      		   "id_area=? WHERE cd_formacao_area_conhecimento=?");
			pstmt.setInt(1,objeto.getCdFormacaoAreaConhecimento());
			pstmt.setString(2,objeto.getNmArea());
			pstmt.setString(3,objeto.getIdArea());
			pstmt.setInt(4, cdFormacaoAreaConhecimentoOld!=0 ? cdFormacaoAreaConhecimentoOld : objeto.getCdFormacaoAreaConhecimento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoAreaConhecimentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoAreaConhecimentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFormacaoAreaConhecimento) {
		return delete(cdFormacaoAreaConhecimento, null);
	}

	public static int delete(int cdFormacaoAreaConhecimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_formacao_area_conhecimento WHERE cd_formacao_area_conhecimento=?");
			pstmt.setInt(1, cdFormacaoAreaConhecimento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoAreaConhecimentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoAreaConhecimentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FormacaoAreaConhecimento get(int cdFormacaoAreaConhecimento) {
		return get(cdFormacaoAreaConhecimento, null);
	}

	public static FormacaoAreaConhecimento get(int cdFormacaoAreaConhecimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_formacao_area_conhecimento WHERE cd_formacao_area_conhecimento=?");
			pstmt.setInt(1, cdFormacaoAreaConhecimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FormacaoAreaConhecimento(rs.getInt("cd_formacao_area_conhecimento"),
						rs.getString("nm_area"),
						rs.getString("id_area"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoAreaConhecimentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoAreaConhecimentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_formacao_area_conhecimento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoAreaConhecimentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoAreaConhecimentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<FormacaoAreaConhecimento> getList() {
		return getList(null);
	}

	public static ArrayList<FormacaoAreaConhecimento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<FormacaoAreaConhecimento> list = new ArrayList<FormacaoAreaConhecimento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				FormacaoAreaConhecimento obj = FormacaoAreaConhecimentoDAO.get(rsm.getInt("cd_formacao_area_conhecimento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoAreaConhecimentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_formacao_area_conhecimento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
