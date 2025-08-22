package com.tivic.manager.ptc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class WorkflowGatilhoDAO{

	public static int insert(WorkflowGatilho objeto) {
		return insert(objeto, null);
	}

	public static int insert(WorkflowGatilho objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_gatilho");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_regra");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdRegra()));
			int code = Conexao.getSequenceCode("ptc_workflow_gatilho", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdGatilho(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_workflow_gatilho (cd_gatilho,"+
			                                  "cd_regra,"+
			                                  "tp_gatilho,"+
			                                  "vl_inicial,"+
			                                  "vl_final,"+
			                                  "cd_atributo,"+
			                                  "cd_entidade) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdRegra()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdRegra());
			pstmt.setInt(3,objeto.getTpGatilho());
			pstmt.setInt(4,objeto.getVlInicial());
			pstmt.setInt(5,objeto.getVlFinal());
			if(objeto.getCdAtributo()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdAtributo());
			if(objeto.getCdEntidade()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdEntidade());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowGatilhoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowGatilhoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(WorkflowGatilho objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(WorkflowGatilho objeto, int cdGatilhoOld, int cdRegraOld) {
		return update(objeto, cdGatilhoOld, cdRegraOld, null);
	}

	public static int update(WorkflowGatilho objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(WorkflowGatilho objeto, int cdGatilhoOld, int cdRegraOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_workflow_gatilho SET cd_gatilho=?,"+
												      		   "cd_regra=?,"+
												      		   "tp_gatilho=?,"+
												      		   "vl_inicial=?,"+
												      		   "vl_final=?,"+
												      		   "cd_atributo=?,"+
												      		   "cd_entidade=? WHERE cd_gatilho=? AND cd_regra=?");
			pstmt.setInt(1,objeto.getCdGatilho());
			pstmt.setInt(2,objeto.getCdRegra());
			pstmt.setInt(3,objeto.getTpGatilho());
			pstmt.setInt(4,objeto.getVlInicial());
			pstmt.setInt(5,objeto.getVlFinal());
			if(objeto.getCdAtributo()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdAtributo());
			if(objeto.getCdEntidade()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdEntidade());
			pstmt.setInt(8, cdGatilhoOld!=0 ? cdGatilhoOld : objeto.getCdGatilho());
			pstmt.setInt(9, cdRegraOld!=0 ? cdRegraOld : objeto.getCdRegra());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowGatilhoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowGatilhoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdGatilho, int cdRegra) {
		return delete(cdGatilho, cdRegra, null);
	}

	public static int delete(int cdGatilho, int cdRegra, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_workflow_gatilho WHERE cd_gatilho=? AND cd_regra=?");
			pstmt.setInt(1, cdGatilho);
			pstmt.setInt(2, cdRegra);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowGatilhoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowGatilhoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static WorkflowGatilho get(int cdGatilho, int cdRegra) {
		return get(cdGatilho, cdRegra, null);
	}

	public static WorkflowGatilho get(int cdGatilho, int cdRegra, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_workflow_gatilho WHERE cd_gatilho=? AND cd_regra=?");
			pstmt.setInt(1, cdGatilho);
			pstmt.setInt(2, cdRegra);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new WorkflowGatilho(rs.getInt("cd_gatilho"),
						rs.getInt("cd_regra"),
						rs.getInt("tp_gatilho"),
						rs.getInt("vl_inicial"),
						rs.getInt("vl_final"),
						rs.getInt("cd_atributo"),
						rs.getInt("cd_entidade"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowGatilhoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowGatilhoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_workflow_gatilho");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowGatilhoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowGatilhoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<WorkflowGatilho> getList() {
		return getList(null);
	}

	public static ArrayList<WorkflowGatilho> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<WorkflowGatilho> list = new ArrayList<WorkflowGatilho>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				WorkflowGatilho obj = WorkflowGatilhoDAO.get(rsm.getInt("cd_gatilho"), rsm.getInt("cd_regra"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowGatilhoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ptc_workflow_gatilho", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
