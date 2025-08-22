package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CursoMultiDAO{

	public static int insert(CursoMulti objeto) {
		return insert(objeto, null);
	}

	public static int insert(CursoMulti objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_curso_multi (cd_curso_multi,"+
			                                  "cd_curso) VALUES (?, ?)");
			if(objeto.getCdCursoMulti()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdCursoMulti());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCurso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoMultiDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoMultiDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CursoMulti objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(CursoMulti objeto, int cdCursoMultiOld, int cdCursoOld) {
		return update(objeto, cdCursoMultiOld, cdCursoOld, null);
	}

	public static int update(CursoMulti objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(CursoMulti objeto, int cdCursoMultiOld, int cdCursoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_curso_multi SET cd_curso_multi=?,"+
												      		   "cd_curso=? WHERE cd_curso_multi=? AND cd_curso=?");
			pstmt.setInt(1,objeto.getCdCursoMulti());
			pstmt.setInt(2,objeto.getCdCurso());
			pstmt.setInt(3, cdCursoMultiOld!=0 ? cdCursoMultiOld : objeto.getCdCursoMulti());
			pstmt.setInt(4, cdCursoOld!=0 ? cdCursoOld : objeto.getCdCurso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoMultiDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoMultiDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCursoMulti, int cdCurso) {
		return delete(cdCursoMulti, cdCurso, null);
	}

	public static int delete(int cdCursoMulti, int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_curso_multi WHERE cd_curso_multi=? AND cd_curso=?");
			pstmt.setInt(1, cdCursoMulti);
			pstmt.setInt(2, cdCurso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoMultiDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoMultiDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CursoMulti get(int cdCursoMulti, int cdCurso) {
		return get(cdCursoMulti, cdCurso, null);
	}

	public static CursoMulti get(int cdCursoMulti, int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_multi WHERE cd_curso_multi=? AND cd_curso=?");
			pstmt.setInt(1, cdCursoMulti);
			pstmt.setInt(2, cdCurso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CursoMulti(rs.getInt("cd_curso_multi"),
						rs.getInt("cd_curso"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoMultiDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoMultiDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_multi");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoMultiDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoMultiDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CursoMulti> getList() {
		return getList(null);
	}

	public static ArrayList<CursoMulti> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CursoMulti> list = new ArrayList<CursoMulti>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CursoMulti obj = CursoMultiDAO.get(rsm.getInt("cd_curso_multi"), rsm.getInt("cd_curso"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoMultiDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_curso_multi", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
