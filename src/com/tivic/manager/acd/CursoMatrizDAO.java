package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class CursoMatrizDAO{

	public static int insert(CursoMatriz objeto) {
		return insert(objeto, null);
	}

	public static int insert(CursoMatriz objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_matriz");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_curso");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdCurso()));
			int code = Conexao.getSequenceCode("acd_curso_matriz", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMatriz(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_curso_matriz (cd_matriz,"+
			                                  "cd_curso,"+
			                                  "cd_periodo_letivo,"+
			                                  "nm_matriz,"+
			                                  "tp_conceito,"+
			                                  "nm_diario_oficial,"+
			                                  "nm_parecer,"+
			                                  "nm_resolucao,"+
			                                  "vl_conceito_maximo,"+
			                                  "vl_conceito_minimo,"+
			                                  "pr_conceito_minimo,"+
			                                  "pr_presenca_minima,"+
			                                  "qt_decimal_conceito,"+
			                                  "dt_vigencia_inicial,"+
			                                  "dt_vigencia_final) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdCurso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCurso());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			pstmt.setString(4,objeto.getNmMatriz());
			pstmt.setInt(5,objeto.getTpConceito());
			pstmt.setString(6,objeto.getNmDiarioOficial());
			pstmt.setString(7,objeto.getNmParecer());
			pstmt.setString(8,objeto.getNmResolucao());
			pstmt.setFloat(9,objeto.getVlConceitoMaximo());
			pstmt.setFloat(10,objeto.getVlConceitoMinimo());
			pstmt.setFloat(11,objeto.getPrConceitoMinimo());
			pstmt.setFloat(12,objeto.getPrPresencaMinima());
			pstmt.setInt(13,objeto.getQtDecimalConceito());
			if(objeto.getDtVigenciaInicial()==null)
				pstmt.setNull(14, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(14,new Timestamp(objeto.getDtVigenciaInicial().getTimeInMillis()));
			if(objeto.getDtVigenciaFinal()==null)
				pstmt.setNull(15, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(15,new Timestamp(objeto.getDtVigenciaFinal().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoMatrizDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoMatrizDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CursoMatriz objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(CursoMatriz objeto, int cdMatrizOld, int cdCursoOld) {
		return update(objeto, cdMatrizOld, cdCursoOld, null);
	}

	public static int update(CursoMatriz objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(CursoMatriz objeto, int cdMatrizOld, int cdCursoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_curso_matriz SET cd_matriz=?,"+
												      		   "cd_curso=?,"+
												      		   "cd_periodo_letivo=?,"+
												      		   "nm_matriz=?,"+
												      		   "tp_conceito=?,"+
												      		   "nm_diario_oficial=?,"+
												      		   "nm_parecer=?,"+
												      		   "nm_resolucao=?,"+
												      		   "vl_conceito_maximo=?,"+
												      		   "vl_conceito_minimo=?,"+
												      		   "pr_conceito_minimo=?,"+
												      		   "pr_presenca_minima=?,"+
												      		   "qt_decimal_conceito=?,"+
												      		   "dt_vigencia_inicial=?,"+
												      		   "dt_vigencia_final=? WHERE cd_matriz=? AND cd_curso=?");
			pstmt.setInt(1,objeto.getCdMatriz());
			pstmt.setInt(2,objeto.getCdCurso());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			pstmt.setString(4,objeto.getNmMatriz());
			pstmt.setInt(5,objeto.getTpConceito());
			pstmt.setString(6,objeto.getNmDiarioOficial());
			pstmt.setString(7,objeto.getNmParecer());
			pstmt.setString(8,objeto.getNmResolucao());
			pstmt.setFloat(9,objeto.getVlConceitoMaximo());
			pstmt.setFloat(10,objeto.getVlConceitoMinimo());
			pstmt.setFloat(11,objeto.getPrConceitoMinimo());
			pstmt.setFloat(12,objeto.getPrPresencaMinima());
			pstmt.setInt(13,objeto.getQtDecimalConceito());
			if(objeto.getDtVigenciaInicial()==null)
				pstmt.setNull(14, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(14,new Timestamp(objeto.getDtVigenciaInicial().getTimeInMillis()));
			if(objeto.getDtVigenciaFinal()==null)
				pstmt.setNull(15, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(15,new Timestamp(objeto.getDtVigenciaFinal().getTimeInMillis()));
			pstmt.setInt(16, cdMatrizOld!=0 ? cdMatrizOld : objeto.getCdMatriz());
			pstmt.setInt(17, cdCursoOld!=0 ? cdCursoOld : objeto.getCdCurso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoMatrizDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoMatrizDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMatriz, int cdCurso) {
		return delete(cdMatriz, cdCurso, null);
	}

	public static int delete(int cdMatriz, int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_curso_matriz WHERE cd_matriz=? AND cd_curso=?");
			pstmt.setInt(1, cdMatriz);
			pstmt.setInt(2, cdCurso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoMatrizDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoMatrizDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CursoMatriz get(int cdMatriz, int cdCurso) {
		return get(cdMatriz, cdCurso, null);
	}

	public static CursoMatriz get(int cdMatriz, int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_matriz WHERE cd_matriz=? AND cd_curso=?");
			pstmt.setInt(1, cdMatriz);
			pstmt.setInt(2, cdCurso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CursoMatriz(rs.getInt("cd_matriz"),
						rs.getInt("cd_curso"),
						rs.getInt("cd_periodo_letivo"),
						rs.getString("nm_matriz"),
						rs.getInt("tp_conceito"),
						rs.getString("nm_diario_oficial"),
						rs.getString("nm_parecer"),
						rs.getString("nm_resolucao"),
						rs.getFloat("vl_conceito_maximo"),
						rs.getFloat("vl_conceito_minimo"),
						rs.getFloat("pr_conceito_minimo"),
						rs.getFloat("pr_presenca_minima"),
						rs.getInt("qt_decimal_conceito"),
						(rs.getTimestamp("dt_vigencia_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vigencia_inicial").getTime()),
						(rs.getTimestamp("dt_vigencia_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vigencia_final").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoMatrizDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoMatrizDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_matriz");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoMatrizDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoMatrizDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CursoMatriz> getList() {
		return getList(null);
	}

	public static ArrayList<CursoMatriz> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CursoMatriz> list = new ArrayList<CursoMatriz>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CursoMatriz obj = CursoMatrizDAO.get(rsm.getInt("cd_matriz"), rsm.getInt("cd_curso"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoMatrizDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_curso_matriz", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}