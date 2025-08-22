package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class PlanoPagamentoDAO{

	public static int insert(PlanoPagamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(PlanoPagamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_plano_pagamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPlanoPagamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_plano_pagamento (cd_plano_pagamento,"+
			                                  "nm_plano_pagamento,"+
			                                  "id_plano_pagamento,"+
			                                  "pr_juros_nominal,"+
			                                  "dt_inicio_vigencia,"+
			                                  "dt_final_vigencia) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmPlanoPagamento());
			pstmt.setString(3,objeto.getIdPlanoPagamento());
			pstmt.setFloat(4,objeto.getPrJurosNominal());
			if(objeto.getDtInicioVigencia()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtInicioVigencia().getTimeInMillis()));
			if(objeto.getDtFinalVigencia()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtFinalVigencia().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PlanoPagamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(PlanoPagamento objeto, int cdPlanoPagamentoOld) {
		return update(objeto, cdPlanoPagamentoOld, null);
	}

	public static int update(PlanoPagamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(PlanoPagamento objeto, int cdPlanoPagamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_plano_pagamento SET cd_plano_pagamento=?,"+
												      		   "nm_plano_pagamento=?,"+
												      		   "id_plano_pagamento=?,"+
												      		   "pr_juros_nominal=?,"+
												      		   "dt_inicio_vigencia=?,"+
												      		   "dt_final_vigencia=? WHERE cd_plano_pagamento=?");
			pstmt.setInt(1,objeto.getCdPlanoPagamento());
			pstmt.setString(2,objeto.getNmPlanoPagamento());
			pstmt.setString(3,objeto.getIdPlanoPagamento());
			pstmt.setFloat(4,objeto.getPrJurosNominal());
			if(objeto.getDtInicioVigencia()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtInicioVigencia().getTimeInMillis()));
			if(objeto.getDtFinalVigencia()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtFinalVigencia().getTimeInMillis()));
			pstmt.setInt(7, cdPlanoPagamentoOld!=0 ? cdPlanoPagamentoOld : objeto.getCdPlanoPagamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPlanoPagamento) {
		return delete(cdPlanoPagamento, null);
	}

	public static int delete(int cdPlanoPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_plano_pagamento WHERE cd_plano_pagamento=?");
			pstmt.setInt(1, cdPlanoPagamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PlanoPagamento get(int cdPlanoPagamento) {
		return get(cdPlanoPagamento, null);
	}

	public static PlanoPagamento get(int cdPlanoPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_plano_pagamento WHERE cd_plano_pagamento=?");
			pstmt.setInt(1, cdPlanoPagamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PlanoPagamento(rs.getInt("cd_plano_pagamento"),
						rs.getString("nm_plano_pagamento"),
						rs.getString("id_plano_pagamento"),
						rs.getFloat("pr_juros_nominal"),
						(rs.getTimestamp("dt_inicio_vigencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio_vigencia").getTime()),
						(rs.getTimestamp("dt_final_vigencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final_vigencia").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_plano_pagamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagamentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_plano_pagamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
