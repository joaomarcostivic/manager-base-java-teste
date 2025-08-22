package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class AreaConhecimentoDAO{

	public static int insert(AreaConhecimento objeto) {
		return insert(objeto, null);
	}

	public static int insert(AreaConhecimento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_area_conhecimento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAreaConhecimento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_area_conhecimento (cd_area_conhecimento,"+
			                                  "nm_area_conhecimento,"+
			                                  "cd_area_conhecimento_superior,"+
			                                  "id_area) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmAreaConhecimento());
			if(objeto.getCdAreaConhecimentoSuperior()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAreaConhecimentoSuperior());
			pstmt.setString(4,objeto.getIdArea());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AreaConhecimentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AreaConhecimentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AreaConhecimento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AreaConhecimento objeto, int cdAreaConhecimentoOld) {
		return update(objeto, cdAreaConhecimentoOld, null);
	}

	public static int update(AreaConhecimento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AreaConhecimento objeto, int cdAreaConhecimentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_area_conhecimento SET cd_area_conhecimento=?,"+
												      		   "nm_area_conhecimento=?,"+
												      		   "cd_area_conhecimento_superior=?,"+
												      		   "id_area=? WHERE cd_area_conhecimento=?");
			pstmt.setInt(1,objeto.getCdAreaConhecimento());
			pstmt.setString(2,objeto.getNmAreaConhecimento());
			if(objeto.getCdAreaConhecimentoSuperior()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAreaConhecimentoSuperior());
			pstmt.setString(4,objeto.getIdArea());
			pstmt.setInt(5, cdAreaConhecimentoOld!=0 ? cdAreaConhecimentoOld : objeto.getCdAreaConhecimento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AreaConhecimentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AreaConhecimentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAreaConhecimento) {
		return delete(cdAreaConhecimento, null);
	}

	public static int delete(int cdAreaConhecimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_area_conhecimento WHERE cd_area_conhecimento=?");
			pstmt.setInt(1, cdAreaConhecimento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AreaConhecimentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AreaConhecimentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AreaConhecimento get(int cdAreaConhecimento) {
		return get(cdAreaConhecimento, null);
	}

	public static AreaConhecimento get(int cdAreaConhecimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_area_conhecimento WHERE cd_area_conhecimento=?");
			pstmt.setInt(1, cdAreaConhecimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AreaConhecimento(rs.getInt("cd_area_conhecimento"),
						rs.getString("nm_area_conhecimento"),
						rs.getInt("cd_area_conhecimento_superior"),
						rs.getString("id_area"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AreaConhecimentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AreaConhecimentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_area_conhecimento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AreaConhecimentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AreaConhecimentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AreaConhecimento> getList() {
		return getList(null);
	}

	public static ArrayList<AreaConhecimento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AreaConhecimento> list = new ArrayList<AreaConhecimento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AreaConhecimento obj = AreaConhecimentoDAO.get(rsm.getInt("cd_area_conhecimento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AreaConhecimentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_area_conhecimento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
