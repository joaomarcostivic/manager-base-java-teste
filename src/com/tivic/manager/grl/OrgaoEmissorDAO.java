package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class OrgaoEmissorDAO{

	public static int insert(OrgaoEmissor objeto) {
		return insert(objeto, null);
	}

	public static int insert(OrgaoEmissor objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_orgao_emissor", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOrgaoEmissor(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_orgao_emissor (cd_orgao_emissor,"+
			                                  "nm_orgao_emissor,"+
			                                  "id_orgao_emissor) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmOrgaoEmissor());
			pstmt.setString(3,objeto.getIdOrgaoEmissor());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoEmissorDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoEmissorDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OrgaoEmissor objeto) {
		return update(objeto, 0, null);
	}

	public static int update(OrgaoEmissor objeto, int cdOrgaoEmissorOld) {
		return update(objeto, cdOrgaoEmissorOld, null);
	}

	public static int update(OrgaoEmissor objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(OrgaoEmissor objeto, int cdOrgaoEmissorOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_orgao_emissor SET cd_orgao_emissor=?,"+
												      		   "nm_orgao_emissor=?,"+
												      		   "id_orgao_emissor=? WHERE cd_orgao_emissor=?");
			pstmt.setInt(1,objeto.getCdOrgaoEmissor());
			pstmt.setString(2,objeto.getNmOrgaoEmissor());
			pstmt.setString(3,objeto.getIdOrgaoEmissor());
			pstmt.setInt(4, cdOrgaoEmissorOld!=0 ? cdOrgaoEmissorOld : objeto.getCdOrgaoEmissor());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoEmissorDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoEmissorDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOrgaoEmissor) {
		return delete(cdOrgaoEmissor, null);
	}

	public static int delete(int cdOrgaoEmissor, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_orgao_emissor WHERE cd_orgao_emissor=?");
			pstmt.setInt(1, cdOrgaoEmissor);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoEmissorDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoEmissorDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OrgaoEmissor get(int cdOrgaoEmissor) {
		return get(cdOrgaoEmissor, null);
	}

	public static OrgaoEmissor get(int cdOrgaoEmissor, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_orgao_emissor WHERE cd_orgao_emissor=?");
			pstmt.setInt(1, cdOrgaoEmissor);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OrgaoEmissor(rs.getInt("cd_orgao_emissor"),
						rs.getString("nm_orgao_emissor"),
						rs.getString("id_orgao_emissor"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoEmissorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoEmissorDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_orgao_emissor");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoEmissorDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoEmissorDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OrgaoEmissor> getList() {
		return getList(null);
	}

	public static ArrayList<OrgaoEmissor> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OrgaoEmissor> list = new ArrayList<OrgaoEmissor>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OrgaoEmissor obj = OrgaoEmissorDAO.get(rsm.getInt("cd_orgao_emissor"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoEmissorDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_orgao_emissor", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
