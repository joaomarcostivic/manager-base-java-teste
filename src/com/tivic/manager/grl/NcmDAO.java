package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.*;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class NcmDAO{

	public static int insert(Ncm objeto) {
		return insert(objeto, null);
	}

	public static int insert(Ncm objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_ncm", connect);
			if (code <= 0) {
				if (isConnectionNull)
					sol.dao.Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdNcm(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_ncm (cd_ncm,"+
			                                  "nm_ncm,"+
			                                  "cd_unidade_medida,"+
			                                  "nr_ncm) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmNcm());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdUnidadeMedida());
			pstmt.setString(4,objeto.getNrNcm());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NcmDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NcmDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Ncm objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Ncm objeto, int cdNcmOld) {
		return update(objeto, cdNcmOld, null);
	}

	public static int update(Ncm objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Ncm objeto, int cdNcmOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_ncm SET cd_ncm=?,"+
												      		   "nm_ncm=?,"+
												      		   "cd_unidade_medida=?,"+
												      		   "nr_ncm=? WHERE cd_ncm=?");
			pstmt.setInt(1,objeto.getCdNcm());
			pstmt.setString(2,objeto.getNmNcm());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdUnidadeMedida());
			pstmt.setString(4,objeto.getNrNcm());
			pstmt.setInt(5, cdNcmOld!=0 ? cdNcmOld : objeto.getCdNcm());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NcmDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NcmDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdNcm) {
		return delete(cdNcm, null);
	}

	public static int delete(int cdNcm, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_ncm WHERE cd_ncm=?");
			pstmt.setInt(1, cdNcm);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NcmDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NcmDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Ncm get(int cdNcm) {
		return get(cdNcm, null);
	}

	public static Ncm get(int cdNcm, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_ncm WHERE cd_ncm=?");
			pstmt.setInt(1, cdNcm);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Ncm(rs.getInt("cd_ncm"),
						rs.getString("nm_ncm"),
						rs.getInt("cd_unidade_medida"),
						rs.getString("nr_ncm"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NcmDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NcmDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_ncm");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NcmDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NcmDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_ncm", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
