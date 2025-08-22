package com.tivic.manager.str;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class OrgaoDAO{

	public static int insert(Orgao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Orgao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("str_orgao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOrgao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO str_orgao (cd_orgao,"+
			                                  "nm_orgao,"+
			                                  "id_orgao,"+
			                                  "nm_suborgao,"+
			                                  "nm_coordenador,"+
			                                  "cd_cidade) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmOrgao());
			pstmt.setString(3,objeto.getIdOrgao());
			pstmt.setString(4,objeto.getNmSuborgao());
			pstmt.setString(5,objeto.getNmCoordenador());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCidade());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Orgao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Orgao objeto, int cdOrgaoOld) {
		return update(objeto, cdOrgaoOld, null);
	}

	public static int update(Orgao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Orgao objeto, int cdOrgaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE str_orgao SET cd_orgao=?,"+
												      		   "nm_orgao=?,"+
												      		   "id_orgao=?,"+
												      		   "nm_suborgao=?,"+
												      		   "nm_coordenador=?,"+
												      		   "cd_cidade=? WHERE cd_orgao=?");
			pstmt.setInt(1,objeto.getCdOrgao());
			pstmt.setString(2,objeto.getNmOrgao());
			pstmt.setString(3,objeto.getIdOrgao());
			pstmt.setString(4,objeto.getNmSuborgao());
			pstmt.setString(5,objeto.getNmCoordenador());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCidade());
			pstmt.setInt(7, cdOrgaoOld!=0 ? cdOrgaoOld : objeto.getCdOrgao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOrgao) {
		return delete(cdOrgao, null);
	}

	public static int delete(int cdOrgao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM str_orgao WHERE cd_orgao=?");
			pstmt.setInt(1, cdOrgao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Orgao get(int cdOrgao) {
		return get(cdOrgao, null);
	}

	public static Orgao get(int cdOrgao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM str_orgao WHERE cd_orgao=?");
			pstmt.setInt(1, cdOrgao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Orgao(rs.getInt("cd_orgao"),
						rs.getString("nm_orgao"),
						rs.getString("id_orgao"),
						rs.getString("nm_suborgao"),
						rs.getString("nm_coordenador"),
						rs.getInt("cd_cidade"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM str_orgao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Orgao> getList() {
		return getList(null);
	}

	public static ArrayList<Orgao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Orgao> list = new ArrayList<Orgao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Orgao obj = OrgaoDAO.get(rsm.getInt("cd_orgao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM str_orgao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
