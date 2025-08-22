package com.tivic.manager.grl;

import java.sql.*;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;

import java.util.GregorianCalendar;
import java.util.ArrayList;

public class IndicadorVariacaoDAO{

	public static int insert(IndicadorVariacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(IndicadorVariacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			if(objeto.getCdIndicador()<=0)	{
				int code = Conexao.getSequenceCode("grl_indicador_variacao", connect);
				if (code <= 0)
					return -1;
				objeto.setCdIndicador(code);
			}
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_indicador_variacao (cd_indicador,"+
			                                  "dt_inicio,"+
			                                  "pr_variacao) VALUES (?, ?, ?)");
			if(objeto.getCdIndicador()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdIndicador());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			pstmt.setFloat(3,objeto.getPrVariacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(IndicadorVariacao objeto) {
		return update(objeto, 0, null, null);
	}

	public static int update(IndicadorVariacao objeto, int cdIndicadorOld, GregorianCalendar dtInicioOld) {
		return update(objeto, cdIndicadorOld, dtInicioOld, null);
	}

	public static int update(IndicadorVariacao objeto, Connection connect) {
		return update(objeto, 0, null, connect);
	}

	public static int update(IndicadorVariacao objeto, int cdIndicadorOld, GregorianCalendar dtInicioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_indicador_variacao SET cd_indicador=?,"+
												      		   "dt_inicio=?,"+
												      		   "pr_variacao=? WHERE cd_indicador=? AND dt_inicio=?");
			pstmt.setInt(1,objeto.getCdIndicador());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			pstmt.setFloat(3,objeto.getPrVariacao());
			pstmt.setInt(4, cdIndicadorOld!=0 ? cdIndicadorOld : objeto.getCdIndicador());
			if(dtInicioOld==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(dtInicioOld.getTimeInMillis()));
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdIndicador, GregorianCalendar dtInicio) {
		return delete(cdIndicador, dtInicio, null);
	}

	public static int delete(int cdIndicador, GregorianCalendar dtInicio, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_indicador_variacao WHERE cd_indicador=? AND dt_inicio=?");
			pstmt.setInt(1, cdIndicador);
			if(dtInicio==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(dtInicio.getTimeInMillis()));
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static IndicadorVariacao get(int cdIndicador, GregorianCalendar dtInicio) {
		return get(cdIndicador, dtInicio, null);
	}

	public static IndicadorVariacao get(int cdIndicador, GregorianCalendar dtInicio, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_indicador_variacao WHERE cd_indicador=? AND dt_inicio=?");
			pstmt.setInt(1, cdIndicador);
			if(dtInicio==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(dtInicio.getTimeInMillis()));
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new IndicadorVariacao(rs.getInt("cd_indicador"),
						(rs.getTimestamp("dt_inicio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio").getTime()),
						rs.getFloat("pr_variacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_indicador_variacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<IndicadorVariacao> getList() {
		return getList(null);
	}

	public static ArrayList<IndicadorVariacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<IndicadorVariacao> list = new ArrayList<IndicadorVariacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				IndicadorVariacao obj = IndicadorVariacaoDAO.get(rsm.getInt("cd_indicador"), rsm.getGregorianCalendar("dt_inicio"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_indicador_variacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
