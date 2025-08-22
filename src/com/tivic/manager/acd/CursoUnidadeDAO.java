package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class CursoUnidadeDAO{

	public static int insert(CursoUnidade objeto) {
		return insert(objeto, null);
	}

	public static int insert(CursoUnidade objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_unidade");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_curso");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdCurso()));
			int code = Conexao.getSequenceCode("acd_curso_unidade", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdUnidade(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_curso_unidade (cd_unidade,"+
			                                  "cd_curso,"+
			                                  "nm_unidade,"+
			                                  "dt_inicial,"+
			                                  "dt_final,"+
			                                  "txt_observacao,"+
			                                  "nr_ordem) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdCurso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCurso());
			pstmt.setString(3,objeto.getNmUnidade());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setString(6,objeto.getTxtObservacao());
			pstmt.setInt(7,objeto.getNrOrdem());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CursoUnidade objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(CursoUnidade objeto, int cdUnidadeOld, int cdCursoOld) {
		return update(objeto, cdUnidadeOld, cdCursoOld, null);
	}

	public static int update(CursoUnidade objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(CursoUnidade objeto, int cdUnidadeOld, int cdCursoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_curso_unidade SET cd_unidade=?,"+
												      		   "cd_curso=?,"+
												      		   "nm_unidade=?,"+
												      		   "dt_inicial=?,"+
												      		   "dt_final=?,"+
												      		   "txt_observacao=?,"+
												      		   "nr_ordem=? WHERE cd_unidade=? AND cd_curso=?");
			pstmt.setInt(1,objeto.getCdUnidade());
			pstmt.setInt(2,objeto.getCdCurso());
			pstmt.setString(3,objeto.getNmUnidade());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setString(6,objeto.getTxtObservacao());
			pstmt.setInt(7,objeto.getNrOrdem());
			pstmt.setInt(8, cdUnidadeOld!=0 ? cdUnidadeOld : objeto.getCdUnidade());
			pstmt.setInt(9, cdCursoOld!=0 ? cdCursoOld : objeto.getCdCurso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdUnidade, int cdCurso) {
		return delete(cdUnidade, cdCurso, null);
	}

	public static int delete(int cdUnidade, int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_curso_unidade WHERE cd_unidade=? AND cd_curso=?");
			pstmt.setInt(1, cdUnidade);
			pstmt.setInt(2, cdCurso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CursoUnidade get(int cdUnidade, int cdCurso) {
		return get(cdUnidade, cdCurso, null);
	}

	public static CursoUnidade get(int cdUnidade, int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_unidade WHERE cd_unidade=? AND cd_curso=?");
			pstmt.setInt(1, cdUnidade);
			pstmt.setInt(2, cdCurso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CursoUnidade(rs.getInt("cd_unidade"),
						rs.getInt("cd_curso"),
						rs.getString("nm_unidade"),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()),
						rs.getString("txt_observacao"),
						rs.getInt("nr_ordem"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int getNextNrOrdem(int cdCurso) {
		return getNextNrOrdem(cdCurso, null);
	}
	
	public static int getNextNrOrdem(int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT MAX(nr_ordem) AS nr_ordem FROM acd_curso_unidade WHERE cd_curso =?");
			pstmt.setInt(1, cdCurso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return rs.getInt("nr_ordem") + 1;
			}
			else{
				return 1;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeDAO.getNextNrOrdem: " + sqlExpt);
			return -1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeDAO.getNextNrOrdem: " + e);
			return -1;
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_unidade");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CursoUnidade> getList() {
		return getList(null);
	}

	public static ArrayList<CursoUnidade> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CursoUnidade> list = new ArrayList<CursoUnidade>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CursoUnidade obj = CursoUnidadeDAO.get(rsm.getInt("cd_unidade"), rsm.getInt("cd_curso"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_curso_unidade", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
