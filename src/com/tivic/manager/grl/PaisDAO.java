package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class PaisDAO{

	public static int insert(Pais objeto) {
		return insert(objeto, null);
	}

	public static int insert(Pais objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_pais", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPais(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_pais (cd_pais,"+
			                                  "nm_pais,"+
			                                  "sg_pais,"+
			                                  "cd_regiao," +
			                                  "id_pais) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmPais());
			pstmt.setString(3,objeto.getSgPais());
			if(objeto.getCdRegiao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdRegiao());
			if(objeto.getIdPais()==null)
				pstmt.setNull(5, Types.VARCHAR);
			else
				pstmt.setString(5,objeto.getIdPais());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PaisDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PaisDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Pais objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Pais objeto, int cdPaisOld) {
		return update(objeto, cdPaisOld, null);
	}

	public static int update(Pais objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Pais objeto, int cdPaisOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_pais SET cd_pais=?,"+
												      		   "nm_pais=?,"+
												      		   "sg_pais=?,"+
												      		   "cd_regiao=?," +
												      		   "id_pais=? WHERE cd_pais=?");
			pstmt.setInt(1,objeto.getCdPais());
			pstmt.setString(2,objeto.getNmPais());
			pstmt.setString(3,objeto.getSgPais());
			if(objeto.getCdRegiao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdRegiao());
			if(objeto.getIdPais()==null)
				pstmt.setNull(5, Types.VARCHAR);
			else
				pstmt.setString(5,objeto.getIdPais());
			pstmt.setInt(6, cdPaisOld!=0 ? cdPaisOld : objeto.getCdPais());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PaisDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PaisDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPais) {
		return delete(cdPais, null);
	}

	public static int delete(int cdPais, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_pais WHERE cd_pais=?");
			pstmt.setInt(1, cdPais);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PaisDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PaisDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Pais get(int cdPais) {
		return get(cdPais, null);
	}

	public static Pais get(int cdPais, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pais WHERE cd_pais=?");
			pstmt.setInt(1, cdPais);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Pais(rs.getInt("cd_pais"),
						rs.getString("nm_pais"),
						rs.getString("sg_pais"),
						rs.getInt("cd_regiao"),
						rs.getString("id_pais"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PaisDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PaisDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pais");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PaisDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PaisDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_pais", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
