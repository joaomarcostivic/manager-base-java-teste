package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class InstituicaoIndicadorDAO{

	public static int insert(InstituicaoIndicador objeto) {
		return insert(objeto, null);
	}

	public static int insert(InstituicaoIndicador objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_indicador (cd_instituicao,"+
			                                  "cd_indicador,"+
			                                  "cd_periodo_letivo,"+
			                                  "vl_indicador_inicial,"+
			                                  "vl_indicador_final,"+
			                                  "dt_indicador) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdInstituicao());
			if(objeto.getCdIndicador()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdIndicador());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			pstmt.setDouble(4,objeto.getVlIndicadorInicial());
			pstmt.setDouble(5,objeto.getVlIndicadorFinal());
			if(objeto.getDtIndicador()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtIndicador().getTimeInMillis()));
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoIndicadorDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoIndicadorDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InstituicaoIndicador objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(InstituicaoIndicador objeto, int cdInstituicaoOld, int cdIndicadorOld, int cdPeriodoLetivoOld) {
		return update(objeto, cdInstituicaoOld, cdIndicadorOld, cdPeriodoLetivoOld, null);
	}

	public static int update(InstituicaoIndicador objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(InstituicaoIndicador objeto, int cdInstituicaoOld, int cdIndicadorOld, int cdPeriodoLetivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_instituicao_indicador SET cd_instituicao=?,"+
												      		   "cd_indicador=?,"+
												      		   "cd_periodo_letivo=?,"+
												      		   "vl_indicador_inicial=?,"+
												      		   "vl_indicador_final=?,"+
												      		   "dt_indicador=? WHERE cd_instituicao=? AND cd_indicador=? AND cd_periodo_letivo=?");
			pstmt.setInt(1,objeto.getCdInstituicao());
			pstmt.setInt(2,objeto.getCdIndicador());
			pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			pstmt.setDouble(4,objeto.getVlIndicadorInicial());
			pstmt.setDouble(5,objeto.getVlIndicadorFinal());
			if(objeto.getDtIndicador()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtIndicador().getTimeInMillis()));
			pstmt.setInt(7, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.setInt(8, cdIndicadorOld!=0 ? cdIndicadorOld : objeto.getCdIndicador());
			pstmt.setInt(9, cdPeriodoLetivoOld!=0 ? cdPeriodoLetivoOld : objeto.getCdPeriodoLetivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoIndicadorDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoIndicadorDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdInstituicao, int cdIndicador, int cdPeriodoLetivo) {
		return delete(cdInstituicao, cdIndicador, cdPeriodoLetivo, null);
	}

	public static int delete(int cdInstituicao, int cdIndicador, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao_indicador WHERE cd_instituicao=? AND cd_indicador=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdIndicador);
			pstmt.setInt(3, cdPeriodoLetivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoIndicadorDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoIndicadorDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoIndicador get(int cdInstituicao, int cdIndicador, int cdPeriodoLetivo) {
		return get(cdInstituicao, cdIndicador, cdPeriodoLetivo, null);
	}

	public static InstituicaoIndicador get(int cdInstituicao, int cdIndicador, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_indicador WHERE cd_instituicao=? AND cd_indicador=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdIndicador);
			pstmt.setInt(3, cdPeriodoLetivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InstituicaoIndicador(rs.getInt("cd_instituicao"),
						rs.getInt("cd_indicador"),
						rs.getInt("cd_periodo_letivo"),
						rs.getDouble("vl_indicador_inicial"),
						rs.getDouble("vl_indicador_final"),
						(rs.getTimestamp("dt_indicador")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_indicador").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoIndicadorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoIndicadorDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_indicador");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoIndicadorDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoIndicadorDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InstituicaoIndicador> getList() {
		return getList(null);
	}

	public static ArrayList<InstituicaoIndicador> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InstituicaoIndicador> list = new ArrayList<InstituicaoIndicador>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InstituicaoIndicador obj = InstituicaoIndicadorDAO.get(rsm.getInt("cd_instituicao"), rsm.getInt("cd_indicador"), rsm.getInt("cd_periodo_letivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoIndicadorDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_indicador", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}