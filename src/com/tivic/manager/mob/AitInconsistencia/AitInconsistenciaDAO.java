package com.tivic.manager.mob.AitInconsistencia;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class AitInconsistenciaDAO{

	public static int insert(AitInconsistencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(AitInconsistencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_ait_inconsistencia", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAitInconsistencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_ait_inconsistencia (cd_ait_inconsistencia,"+
			                                  "cd_ait,"+
			                                  "cd_movimento_atual,"+
			                                  "cd_inconsistencia,"+
			                                  "tp_status_pretendido,"+
			                                  "tp_status_atual,"+
			                                  "dt_inclusao_inconsistencia,"+
			                                  "st_inconsistencia,"+
			                                  "dt_resolucao_inconsistencia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdAit()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAit());
			pstmt.setInt(3,objeto.getCdMovimentoAtual());
			if(objeto.getCdInconsistencia()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdInconsistencia());
			pstmt.setInt(5,objeto.getTpStatusPretendido());
			pstmt.setInt(6,objeto.getTpStatusAtual());
			if(objeto.getDtInclusaoInconsistencia()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInclusaoInconsistencia().getTimeInMillis()));
			pstmt.setInt(8,objeto.getStInconsistencia());
			if(objeto.getDtResolucaoInconsistencia()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtResolucaoInconsistencia().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitInconsistenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitInconsistenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AitInconsistencia objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AitInconsistencia objeto, int cdAitInconsistenciaOld) {
		return update(objeto, cdAitInconsistenciaOld, null);
	}

	public static int update(AitInconsistencia objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AitInconsistencia objeto, int cdAitInconsistenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_ait_inconsistencia SET cd_ait_inconsistencia=?,"+
												      		   "cd_ait=?,"+
												      		   "cd_movimento_atual=?,"+
												      		   "cd_inconsistencia=?,"+
												      		   "tp_status_pretendido=?,"+
												      		   "tp_status_atual=?,"+
												      		   "dt_inclusao_inconsistencia=?,"+
												      		   "st_inconsistencia=?,"+
												      		   "dt_resolucao_inconsistencia=? WHERE cd_ait_inconsistencia=?");
			pstmt.setInt(1,objeto.getCdAitInconsistencia());
			if(objeto.getCdAit()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAit());
			pstmt.setInt(3,objeto.getCdMovimentoAtual());
			if(objeto.getCdInconsistencia()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdInconsistencia());
			pstmt.setInt(5,objeto.getTpStatusPretendido());
			pstmt.setInt(6,objeto.getTpStatusAtual());
			if(objeto.getDtInclusaoInconsistencia()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInclusaoInconsistencia().getTimeInMillis()));
			pstmt.setInt(8,objeto.getStInconsistencia());
			if(objeto.getDtResolucaoInconsistencia()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtResolucaoInconsistencia().getTimeInMillis()));
			pstmt.setInt(10, cdAitInconsistenciaOld !=0 ? cdAitInconsistenciaOld : objeto.getCdAitInconsistencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitInconsistenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitInconsistenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAitInconsistencia) {
		return delete(cdAitInconsistencia, null);
	}

	public static int delete(int cdAitInconsistencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_ait_inconsistencia WHERE cd_ait_inconsistencia=?");
			pstmt.setInt(1, cdAitInconsistencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitInconsistenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitInconsistenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitInconsistencia get(int cdAitInconsistencia) {
		return get(cdAitInconsistencia, null);
	}

	public static AitInconsistencia get(int cdAitInconsistencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_inconsistencia WHERE cd_ait_inconsistencia=?");
			pstmt.setInt(1, cdAitInconsistencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AitInconsistencia(rs.getInt("cd_ait_inconsistencia"),
						rs.getInt("cd_ait"),
						rs.getInt("cd_movimento_atual"),
						rs.getInt("cd_inconsistencia"),
						rs.getInt("tp_status_pretendido"),
						rs.getInt("tp_status_atual"),
						(rs.getTimestamp("dt_inclusao_inconsistencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inclusao_inconsistencia").getTime()),
						rs.getInt("st_inconsistencia"),
						(rs.getTimestamp("dt_resolucao_inconsistencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_resolucao_inconsistencia").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitInconsistenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitInconsistenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_inconsistencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitInconsistenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitInconsistenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AitInconsistencia> getList() {
		return getList(null);
	}

	public static ArrayList<AitInconsistencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AitInconsistencia> list = new ArrayList<AitInconsistencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AitInconsistencia obj = AitInconsistenciaDAO.get(rsm.getInt("cd_ait_inconsistencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitInconsistenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_ait_inconsistencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
