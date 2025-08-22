package com.tivic.manager.alm;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class NivelLocalDAO{

	public static int insert(NivelLocal objeto) {
		return insert(objeto, null);
	}

	public static int insert(NivelLocal objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("alm_nivel_local", connect);
			if (code <= 0) {
				if (isConnectionNull)
					sol.dao.Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdNivelLocal(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_nivel_local (cd_nivel_local,"+
			                                  "nm_nivel_local,"+
			                                  "lg_armazena,"+
			                                  "lg_setor,"+
			                                  "cd_nivel_local_superior) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmNivelLocal());
			pstmt.setInt(3,objeto.getLgArmazena());
			pstmt.setInt(4,objeto.getLgSetor());
			if(objeto.getCdNivelLocalSuperior()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdNivelLocalSuperior());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NivelLocalDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NivelLocalDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(NivelLocal objeto) {
		return update(objeto, 0, null);
	}

	public static int update(NivelLocal objeto, int cdNivelLocalOld) {
		return update(objeto, cdNivelLocalOld, null);
	}

	public static int update(NivelLocal objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(NivelLocal objeto, int cdNivelLocalOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_nivel_local SET cd_nivel_local=?,"+
												      		   "nm_nivel_local=?,"+
												      		   "lg_armazena=?,"+
												      		   "lg_setor=?,"+
												      		   "cd_nivel_local_superior=? WHERE cd_nivel_local=?");
			pstmt.setInt(1,objeto.getCdNivelLocal());
			pstmt.setString(2,objeto.getNmNivelLocal());
			pstmt.setInt(3,objeto.getLgArmazena());
			pstmt.setInt(4,objeto.getLgSetor());
			if(objeto.getCdNivelLocalSuperior()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdNivelLocalSuperior());
			pstmt.setInt(6, cdNivelLocalOld!=0 ? cdNivelLocalOld : objeto.getCdNivelLocal());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NivelLocalDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NivelLocalDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdNivelLocal) {
		return delete(cdNivelLocal, null);
	}

	public static int delete(int cdNivelLocal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_nivel_local WHERE cd_nivel_local=?");
			pstmt.setInt(1, cdNivelLocal);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NivelLocalDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NivelLocalDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static NivelLocal get(int cdNivelLocal) {
		return get(cdNivelLocal, null);
	}

	public static NivelLocal get(int cdNivelLocal, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_nivel_local WHERE cd_nivel_local=?");
			pstmt.setInt(1, cdNivelLocal);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new NivelLocal(rs.getInt("cd_nivel_local"),
						rs.getString("nm_nivel_local"),
						rs.getInt("lg_armazena"),
						rs.getInt("lg_setor"),
						rs.getInt("cd_nivel_local_superior"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NivelLocalDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NivelLocalDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_nivel_local");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NivelLocalDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NivelLocalDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM alm_nivel_local", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
