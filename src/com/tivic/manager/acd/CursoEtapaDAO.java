package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CursoEtapaDAO{

	public static int insert(CursoEtapa objeto) {
		return insert(objeto, null);
	}

	public static int insert(CursoEtapa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_curso_etapa", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCursoEtapa(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_curso_etapa (cd_curso_etapa,"+
			                                  "cd_curso,"+
			                                  "cd_curso_etapa_posterior,"+
			                                  "id_curso_etapa,"+
			                                  "cd_etapa) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdCurso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCurso());
			if(objeto.getCdCursoEtapaPosterior()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCursoEtapaPosterior());
			pstmt.setString(4,objeto.getIdCursoEtapa());
			if(objeto.getCdEtapa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEtapa());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoEtapaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoEtapaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CursoEtapa objeto) {
		return update(objeto, 0, null);
	}

	public static int update(CursoEtapa objeto, int cdCursoEtapaOld) {
		return update(objeto, cdCursoEtapaOld, null);
	}

	public static int update(CursoEtapa objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(CursoEtapa objeto, int cdCursoEtapaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_curso_etapa SET cd_curso_etapa=?,"+
												      		   "cd_curso=?,"+
												      		   "cd_curso_etapa_posterior=?,"+
												      		   "id_curso_etapa=?,"+
												      		   "cd_etapa=? WHERE cd_curso_etapa=?");
			pstmt.setInt(1,objeto.getCdCursoEtapa());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCurso());
			if(objeto.getCdCursoEtapaPosterior()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCursoEtapaPosterior());
			pstmt.setString(4,objeto.getIdCursoEtapa());
			if(objeto.getCdEtapa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEtapa());
			pstmt.setInt(6, cdCursoEtapaOld!=0 ? cdCursoEtapaOld : objeto.getCdCursoEtapa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoEtapaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoEtapaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCursoEtapa) {
		return delete(cdCursoEtapa, null);
	}

	public static int delete(int cdCursoEtapa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_curso_etapa WHERE cd_curso_etapa=?");
			pstmt.setInt(1, cdCursoEtapa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoEtapaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoEtapaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CursoEtapa get(int cdCursoEtapa) {
		return get(cdCursoEtapa, null);
	}

	public static CursoEtapa get(int cdCursoEtapa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_etapa WHERE cd_curso_etapa=?");
			pstmt.setInt(1, cdCursoEtapa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CursoEtapa(rs.getInt("cd_curso_etapa"),
						rs.getInt("cd_curso"),
						rs.getInt("cd_curso_etapa_posterior"),
						rs.getString("id_curso_etapa"),
						rs.getInt("cd_etapa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoEtapaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoEtapaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_etapa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoEtapaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoEtapaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CursoEtapa> getList() {
		return getList(null);
	}

	public static ArrayList<CursoEtapa> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CursoEtapa> list = new ArrayList<CursoEtapa>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CursoEtapa obj = CursoEtapaDAO.get(rsm.getInt("cd_curso_etapa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoEtapaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_curso_etapa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
