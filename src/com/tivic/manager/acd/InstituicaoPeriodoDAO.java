package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class InstituicaoPeriodoDAO{

	public static int insert(InstituicaoPeriodo objeto) {
		return insert(objeto, null);
	}

	public static int insert(InstituicaoPeriodo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_instituicao_periodo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPeriodoLetivo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_periodo (cd_periodo_letivo,"+
											  "cd_instituicao,"+
							                  "nm_periodo_letivo,"+
			                                  "dt_inicial,"+
			                                  "dt_final,"+
			                                  "nr_dias_letivos,"+
			                                  "st_periodo_letivo,"+
			                                  "cd_tipo_periodo,"+
			                                  "cd_periodo_letivo_superior) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInstituicao());
			pstmt.setString(3,objeto.getNmPeriodoLetivo());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setInt(6,objeto.getNrDiasLetivos());
			pstmt.setInt(7,objeto.getStPeriodoLetivo());
			if(objeto.getCdTipoPeriodo()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdTipoPeriodo());
			if(objeto.getCdPeriodoLetivoSuperior()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdPeriodoLetivoSuperior());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InstituicaoPeriodo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(InstituicaoPeriodo objeto, int cdPeriodoLetivoOld) {
		return update(objeto, cdPeriodoLetivoOld, null);
	}

	public static int update(InstituicaoPeriodo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(InstituicaoPeriodo objeto, int cdPeriodoLetivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_instituicao_periodo SET cd_periodo_letivo=?,"+
															   "cd_instituicao=?,"+							      		   
															   "nm_periodo_letivo=?,"+
												      		   "dt_inicial=?,"+
												      		   "dt_final=?,"+
												      		   "nr_dias_letivos=?,"+
												      		   "st_periodo_letivo=?,"+
												      		   "cd_tipo_periodo=?,"+
												      		   "cd_periodo_letivo_superior=? WHERE cd_periodo_letivo=?");
			pstmt.setInt(1,objeto.getCdPeriodoLetivo());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInstituicao());
			pstmt.setString(3,objeto.getNmPeriodoLetivo());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setInt(6,objeto.getNrDiasLetivos());
			pstmt.setInt(7,objeto.getStPeriodoLetivo());
			if(objeto.getCdTipoPeriodo()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdTipoPeriodo());
			if(objeto.getCdPeriodoLetivoSuperior()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdPeriodoLetivoSuperior());
			pstmt.setInt(10, cdPeriodoLetivoOld!=0 ? cdPeriodoLetivoOld : objeto.getCdPeriodoLetivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPeriodoLetivo) {
		return delete(cdPeriodoLetivo, null);
	}

	public static int delete(int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao_periodo WHERE cd_periodo_letivo=?");
			pstmt.setInt(1, cdPeriodoLetivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoPeriodo get(int cdPeriodoLetivo) {
		return get(cdPeriodoLetivo, null);
	}

	public static InstituicaoPeriodo get(int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_periodo_letivo=?");
			pstmt.setInt(1, cdPeriodoLetivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InstituicaoPeriodo(rs.getInt("cd_periodo_letivo"),
						rs.getInt("cd_instituicao"),
						rs.getString("nm_periodo_letivo"),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()),
						rs.getInt("nr_dias_letivos"),
						rs.getInt("st_periodo_letivo"),
						rs.getInt("cd_tipo_periodo"),
						rs.getInt("cd_periodo_letivo_superior"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_periodo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InstituicaoPeriodo> getList() {
		return getList(null);
	}

	public static ArrayList<InstituicaoPeriodo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InstituicaoPeriodo> list = new ArrayList<InstituicaoPeriodo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InstituicaoPeriodo obj = InstituicaoPeriodoDAO.get(rsm.getInt("cd_periodo_letivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_periodo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
