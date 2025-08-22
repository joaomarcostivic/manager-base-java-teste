package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CursoAreaInteresseDAO{

	public static int insert(CursoAreaInteresse objeto) {
		return insert(objeto, null);
	}

	public static int insert(CursoAreaInteresse objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_curso_area_interesse (cd_area_interesse,"+
			                                  "cd_curso) VALUES (?, ?)");
			if(objeto.getCdAreaInteresse()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAreaInteresse());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCurso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoAreaInteresseDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoAreaInteresseDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CursoAreaInteresse objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(CursoAreaInteresse objeto, int cdAreaInteresseOld, int cdCursoOld) {
		return update(objeto, cdAreaInteresseOld, cdCursoOld, null);
	}

	public static int update(CursoAreaInteresse objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(CursoAreaInteresse objeto, int cdAreaInteresseOld, int cdCursoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_curso_area_interesse SET cd_area_interesse=?,"+
												      		   "cd_curso=? WHERE cd_area_interesse=? AND cd_curso=?");
			pstmt.setInt(1,objeto.getCdAreaInteresse());
			pstmt.setInt(2,objeto.getCdCurso());
			pstmt.setInt(3, cdAreaInteresseOld!=0 ? cdAreaInteresseOld : objeto.getCdAreaInteresse());
			pstmt.setInt(4, cdCursoOld!=0 ? cdCursoOld : objeto.getCdCurso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoAreaInteresseDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoAreaInteresseDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAreaInteresse, int cdCurso) {
		return delete(cdAreaInteresse, cdCurso, null);
	}

	public static int delete(int cdAreaInteresse, int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_curso_area_interesse WHERE cd_area_interesse=? AND cd_curso=?");
			pstmt.setInt(1, cdAreaInteresse);
			pstmt.setInt(2, cdCurso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoAreaInteresseDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoAreaInteresseDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CursoAreaInteresse get(int cdAreaInteresse, int cdCurso) {
		return get(cdAreaInteresse, cdCurso, null);
	}

	public static CursoAreaInteresse get(int cdAreaInteresse, int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_area_interesse WHERE cd_area_interesse=? AND cd_curso=?");
			pstmt.setInt(1, cdAreaInteresse);
			pstmt.setInt(2, cdCurso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CursoAreaInteresse(rs.getInt("cd_area_interesse"),
						rs.getInt("cd_curso"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoAreaInteresseDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoAreaInteresseDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_area_interesse");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoAreaInteresseDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoAreaInteresseDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CursoAreaInteresse> getList() {
		return getList(null);
	}

	public static ArrayList<CursoAreaInteresse> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CursoAreaInteresse> list = new ArrayList<CursoAreaInteresse>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CursoAreaInteresse obj = CursoAreaInteresseDAO.get(rsm.getInt("cd_area_interesse"), rsm.getInt("cd_curso"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoAreaInteresseDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_curso_area_interesse", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
