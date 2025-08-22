package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContratoDependenteDAO{

	public static int insert(ContratoDependente objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(ContratoDependente objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_contrato");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdContrato()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_dependente");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdDependente()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_dependencia");
			keys[2].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("adm_contrato_dependente", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDependencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_contrato_dependente (cd_contrato,"+
			                                  "cd_dependente,"+
			                                  "dt_vinculacao,"+
			                                  "dt_desvinculacao,"+
			                                  "pr_pagamento,"+
			                                  "dt_final_vigencia,"+
			                                  "cd_dependencia,"+
			                                  "cd_agente,"+
			                                  "txt_observacao,"+
			                                  "nr_dependente,"+
			                                  "tp_parentesco,"+
			                                  "st_dependente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdContrato()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContrato());
			if(objeto.getCdDependente()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDependente());
			if(objeto.getDtVinculacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtVinculacao().getTimeInMillis()));
			if(objeto.getDtDesvinculacao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtDesvinculacao().getTimeInMillis()));
			pstmt.setFloat(5,objeto.getPrPagamento());
			if(objeto.getDtFinalVigencia()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtFinalVigencia().getTimeInMillis()));
			pstmt.setInt(7, code);
			if(objeto.getCdAgente()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdAgente());
			pstmt.setString(9,objeto.getTxtObservacao());
			pstmt.setString(10,objeto.getNrDependente());
			pstmt.setInt(11,objeto.getTpParentesco());
			pstmt.setInt(12,objeto.getStDependente());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDependenteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDependenteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContratoDependente objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(ContratoDependente objeto, int cdContratoOld, int cdDependenteOld, int cdDependenciaOld) {
		return update(objeto, cdContratoOld, cdDependenteOld, cdDependenciaOld, null);
	}

	public static int update(ContratoDependente objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(ContratoDependente objeto, int cdContratoOld, int cdDependenteOld, int cdDependenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_contrato_dependente SET cd_contrato=?,"+
												      		   "cd_dependente=?,"+
												      		   "dt_vinculacao=?,"+
												      		   "dt_desvinculacao=?,"+
												      		   "pr_pagamento=?,"+
												      		   "dt_final_vigencia=?,"+
												      		   "cd_dependencia=?,"+
												      		   "cd_agente=?,"+
												      		   "txt_observacao=?,"+
												      		   "nr_dependente=?,"+
												      		   "tp_parentesco=?,"+
												      		   "st_dependente=? WHERE cd_contrato=? AND cd_dependente=? AND cd_dependencia=?");
			pstmt.setInt(1,objeto.getCdContrato());
			pstmt.setInt(2,objeto.getCdDependente());
			if(objeto.getDtVinculacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtVinculacao().getTimeInMillis()));
			if(objeto.getDtDesvinculacao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtDesvinculacao().getTimeInMillis()));
			pstmt.setFloat(5,objeto.getPrPagamento());
			if(objeto.getDtFinalVigencia()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtFinalVigencia().getTimeInMillis()));
			pstmt.setInt(7,objeto.getCdDependencia());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdAgente());
			pstmt.setString(9,objeto.getTxtObservacao());
			pstmt.setString(10,objeto.getNrDependente());
			pstmt.setInt(11,objeto.getTpParentesco());
			pstmt.setInt(12,objeto.getStDependente());
			pstmt.setInt(13, cdContratoOld!=0 ? cdContratoOld : objeto.getCdContrato());
			pstmt.setInt(14, cdDependenteOld!=0 ? cdDependenteOld : objeto.getCdDependente());
			pstmt.setInt(15, cdDependenciaOld!=0 ? cdDependenciaOld : objeto.getCdDependencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDependenteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDependenteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContrato, int cdDependente, int cdDependencia) {
		return delete(cdContrato, cdDependente, cdDependencia, null);
	}

	public static int delete(int cdContrato, int cdDependente, int cdDependencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_contrato_dependente WHERE cd_contrato=? AND cd_dependente=? AND cd_dependencia=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdDependente);
			pstmt.setInt(3, cdDependencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDependenteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDependenteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContratoDependente get(int cdContrato, int cdDependente, int cdDependencia) {
		return get(cdContrato, cdDependente, cdDependencia, null);
	}

	public static ContratoDependente get(int cdContrato, int cdDependente, int cdDependencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_dependente WHERE cd_contrato=? AND cd_dependente=? AND cd_dependencia=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdDependente);
			pstmt.setInt(3, cdDependencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContratoDependente(rs.getInt("cd_contrato"),
						rs.getInt("cd_dependente"),
						(rs.getTimestamp("dt_vinculacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vinculacao").getTime()),
						(rs.getTimestamp("dt_desvinculacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_desvinculacao").getTime()),
						rs.getFloat("pr_pagamento"),
						(rs.getTimestamp("dt_final_vigencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final_vigencia").getTime()),
						rs.getInt("cd_dependencia"),
						rs.getInt("cd_agente"),
						rs.getString("txt_observacao"),
						rs.getString("nr_dependente"),
						rs.getInt("tp_parentesco"),
						rs.getInt("st_dependente"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDependenteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDependenteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_dependente");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDependenteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDependenteDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_contrato_dependente", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
