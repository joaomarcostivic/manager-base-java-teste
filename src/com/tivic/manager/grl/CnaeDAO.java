package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CnaeDAO{

	public static int insert(Cnae objeto) {
		return insert(objeto, null);
	}

	public static int insert(Cnae objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_cnae", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCnae(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_cnae (cd_cnae,"+
			                                  "nm_cnae,"+
			                                  "sg_cnae,"+
			                                  "id_cnae,"+
			                                  "nr_nivel,"+
			                                  "cd_cnae_superior,"+
			                                  "nr_cnae) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmCnae());
			pstmt.setString(3,objeto.getSgCnae());
			pstmt.setString(4,objeto.getIdCnae());
			pstmt.setInt(5,objeto.getNrNivel());
			if(objeto.getCdCnaeSuperior()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCnaeSuperior());
			pstmt.setString(7,objeto.getNrCnae());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CnaeDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CnaeDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Cnae objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Cnae objeto, int cdCnaeOld) {
		return update(objeto, cdCnaeOld, null);
	}

	public static int update(Cnae objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Cnae objeto, int cdCnaeOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_cnae SET cd_cnae=?,"+
												      		   "nm_cnae=?,"+
												      		   "sg_cnae=?,"+
												      		   "id_cnae=?,"+
												      		   "nr_nivel=?,"+
												      		   "cd_cnae_superior=?,"+
												      		   "nr_cnae=? WHERE cd_cnae=?");
			pstmt.setInt(1,objeto.getCdCnae());
			pstmt.setString(2,objeto.getNmCnae());
			pstmt.setString(3,objeto.getSgCnae());
			pstmt.setString(4,objeto.getIdCnae());
			pstmt.setInt(5,objeto.getNrNivel());
			if(objeto.getCdCnaeSuperior()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCnaeSuperior());
			pstmt.setString(7,objeto.getNrCnae());
			pstmt.setInt(8, cdCnaeOld!=0 ? cdCnaeOld : objeto.getCdCnae());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CnaeDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CnaeDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCnae) {
		return delete(cdCnae, null);
	}

	public static int delete(int cdCnae, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_cnae WHERE cd_cnae=?");
			pstmt.setInt(1, cdCnae);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CnaeDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CnaeDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Cnae get(int cdCnae) {
		return get(cdCnae, null);
	}

	public static Cnae get(int cdCnae, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_cnae WHERE cd_cnae=?");
			pstmt.setInt(1, cdCnae);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Cnae(rs.getInt("cd_cnae"),
						rs.getString("nm_cnae"),
						rs.getString("sg_cnae"),
						rs.getString("id_cnae"),
						rs.getInt("nr_nivel"),
						rs.getInt("cd_cnae_superior"),
						rs.getString("nr_cnae"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CnaeDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CnaeDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_cnae");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CnaeDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CnaeDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Cnae> getList() {
		return getList(null);
	}

	public static ArrayList<Cnae> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Cnae> list = new ArrayList<Cnae>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Cnae obj = CnaeDAO.get(rsm.getInt("cd_cnae"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CnaeDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_cnae", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}