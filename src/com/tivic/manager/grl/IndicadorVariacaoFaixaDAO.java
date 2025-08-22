package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.*;
import sol.dao.Util;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class IndicadorVariacaoFaixaDAO{

	public static int insert(IndicadorVariacaoFaixa objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(IndicadorVariacaoFaixa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_faixa");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_indicador");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdIndicador()));
			int code = Conexao.getSequenceCode("grl_indicador_variacao_faixa", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFaixa(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_indicador_variacao_faixa (cd_faixa,"+
			                                  "cd_indicador,"+
			                                  "dt_inicio,"+
			                                  "vl_referencia_1,"+
			                                  "vl_referencia_2,"+
			                                  "vl_referencia_3) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdIndicador()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdIndicador());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			pstmt.setFloat(4,objeto.getVlReferencia1());
			pstmt.setFloat(5,objeto.getVlReferencia2());
			pstmt.setFloat(6,objeto.getVlReferencia3());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoFaixaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoFaixaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(IndicadorVariacaoFaixa objeto) {
		return update(objeto, 0, 0, null, null);
	}

	public static int update(IndicadorVariacaoFaixa objeto, int cdFaixaOld, int cdIndicadorOld, GregorianCalendar dtInicioOld) {
		return update(objeto, cdFaixaOld, cdIndicadorOld, dtInicioOld, null);
	}

	public static int update(IndicadorVariacaoFaixa objeto, Connection connect) {
		return update(objeto, 0, 0, null, connect);
	}

	public static int update(IndicadorVariacaoFaixa objeto, int cdFaixaOld, int cdIndicadorOld, GregorianCalendar dtInicioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_indicador_variacao_faixa SET cd_faixa=?,"+
												      		   "cd_indicador=?,"+
												      		   "dt_inicio=?,"+
												      		   "vl_referencia_1=?,"+
												      		   "vl_referencia_2=?,"+
												      		   "vl_referencia_3=? WHERE cd_faixa=? AND cd_indicador=? AND dt_inicio=?");
			pstmt.setInt(1,objeto.getCdFaixa());
			pstmt.setInt(2,objeto.getCdIndicador());
			pstmt.setTimestamp(3, new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			pstmt.setFloat(4,objeto.getVlReferencia1());
			pstmt.setFloat(5,objeto.getVlReferencia2());
			pstmt.setFloat(6,objeto.getVlReferencia3());
			pstmt.setInt(7, cdFaixaOld!=0 ? cdFaixaOld : objeto.getCdFaixa());
			System.out.println("cdFaixa = "+(cdFaixaOld!=0 ? cdFaixaOld : objeto.getCdFaixa()));
			pstmt.setInt(8, cdIndicadorOld!=0 ? cdIndicadorOld : objeto.getCdIndicador());
			System.out.println("cdIndicador = "+(cdIndicadorOld!=0 ? cdIndicadorOld : objeto.getCdIndicador()));
			pstmt.setTimestamp(9, new Timestamp(dtInicioOld!=null?dtInicioOld.getTimeInMillis():objeto.getDtInicio().getTimeInMillis()));
			System.out.println("cdIndicador = "+(Util.formatDateTime(objeto.getDtInicio(), "dd.MM.yyyy")));
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoFaixaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoFaixaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFaixa, int cdIndicador, GregorianCalendar dtInicio) {
		return delete(cdFaixa, cdIndicador, dtInicio, null);
	}

	public static int delete(int cdFaixa, int cdIndicador, GregorianCalendar dtInicio, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_indicador_variacao_faixa WHERE cd_faixa=? AND cd_indicador=? AND dt_inicio=?");
			pstmt.setInt(1, cdFaixa);
			pstmt.setInt(2, cdIndicador);
			pstmt.setTimestamp(3, new Timestamp(dtInicio.getTimeInMillis()));
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoFaixaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoFaixaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static IndicadorVariacaoFaixa get(int cdFaixa, int cdIndicador, GregorianCalendar dtInicio) {
		return get(cdFaixa, cdIndicador, dtInicio, null);
	}

	public static IndicadorVariacaoFaixa get(int cdFaixa, int cdIndicador, GregorianCalendar dtInicio, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_indicador_variacao_faixa WHERE cd_faixa=? AND cd_indicador=? AND dt_inicio=?");
			pstmt.setInt(1, cdFaixa);
			pstmt.setInt(2, cdIndicador);
			pstmt.setTimestamp(3, new Timestamp(dtInicio.getTimeInMillis()));
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new IndicadorVariacaoFaixa(rs.getInt("cd_faixa"),
						rs.getInt("cd_indicador"),
						(rs.getTimestamp("dt_inicio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio").getTime()),
						rs.getFloat("vl_referencia_1"),
						rs.getInt("vl_referencia_2"),
						rs.getInt("vl_referencia_3"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoFaixaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoFaixaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_indicador_variacao_faixa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoFaixaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoFaixaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_indicador_variacao_faixa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
