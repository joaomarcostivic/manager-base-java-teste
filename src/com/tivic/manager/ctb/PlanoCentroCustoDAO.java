package com.tivic.manager.ctb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class PlanoCentroCustoDAO{

	public static int insert(PlanoCentroCusto objeto) {
		return insert(objeto, null);
	}

	public static int insert(PlanoCentroCusto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ctb_plano_centro_custo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPlanoCentroCusto(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ctb_plano_centro_custo (cd_plano_centro_custo,"+
			                                  "nm_plano_centro_custo,"+
			                                  "dt_inativacao,"+
			                                  "ds_mascara_centro_custo,"+
			                                  "id_plano_centro_custo,"+
			                                  "cd_exercicio) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmPlanoCentroCusto());
			if(objeto.getDtInativacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInativacao().getTimeInMillis()));
			pstmt.setString(4,objeto.getDsMascaraCentroCusto());
			pstmt.setString(5,objeto.getIdPlanoCentroCusto());
			if(objeto.getCdExercicio()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdExercicio());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoCentroCustoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoCentroCustoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PlanoCentroCusto objeto) {
		return update(objeto, 0, null);
	}

	public static int update(PlanoCentroCusto objeto, int cdPlanoCentroCustoOld) {
		return update(objeto, cdPlanoCentroCustoOld, null);
	}

	public static int update(PlanoCentroCusto objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(PlanoCentroCusto objeto, int cdPlanoCentroCustoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ctb_plano_centro_custo SET cd_plano_centro_custo=?,"+
												      		   "nm_plano_centro_custo=?,"+
												      		   "dt_inativacao=?,"+
												      		   "ds_mascara_centro_custo=?,"+
												      		   "id_plano_centro_custo=?,"+
												      		   "cd_exercicio=? WHERE cd_plano_centro_custo=?");
			pstmt.setInt(1,objeto.getCdPlanoCentroCusto());
			pstmt.setString(2,objeto.getNmPlanoCentroCusto());
			if(objeto.getDtInativacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInativacao().getTimeInMillis()));
			pstmt.setString(4,objeto.getDsMascaraCentroCusto());
			pstmt.setString(5,objeto.getIdPlanoCentroCusto());
			if(objeto.getCdExercicio()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdExercicio());
			pstmt.setInt(7, cdPlanoCentroCustoOld!=0 ? cdPlanoCentroCustoOld : objeto.getCdPlanoCentroCusto());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoCentroCustoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoCentroCustoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPlanoCentroCusto) {
		return delete(cdPlanoCentroCusto, null);
	}

	public static int delete(int cdPlanoCentroCusto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ctb_plano_centro_custo WHERE cd_plano_centro_custo=?");
			pstmt.setInt(1, cdPlanoCentroCusto);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoCentroCustoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoCentroCustoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PlanoCentroCusto get(int cdPlanoCentroCusto) {
		return get(cdPlanoCentroCusto, null);
	}

	public static PlanoCentroCusto get(int cdPlanoCentroCusto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ctb_plano_centro_custo WHERE cd_plano_centro_custo=?");
			pstmt.setInt(1, cdPlanoCentroCusto);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PlanoCentroCusto(rs.getInt("cd_plano_centro_custo"),
						rs.getString("nm_plano_centro_custo"),
						(rs.getTimestamp("dt_inativacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inativacao").getTime()),
						rs.getString("ds_mascara_centro_custo"),
						rs.getString("id_plano_centro_custo"),
						rs.getInt("cd_exercicio"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoCentroCustoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoCentroCustoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_plano_centro_custo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoCentroCustoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoCentroCustoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PlanoCentroCusto> getList() {
		return getList(null);
	}

	public static ArrayList<PlanoCentroCusto> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PlanoCentroCusto> list = new ArrayList<PlanoCentroCusto>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PlanoCentroCusto obj = PlanoCentroCustoDAO.get(rsm.getInt("cd_plano_centro_custo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoCentroCustoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ctb_plano_centro_custo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
