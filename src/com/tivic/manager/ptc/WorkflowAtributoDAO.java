package com.tivic.manager.ptc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class WorkflowAtributoDAO{

	public static int insert(WorkflowAtributo objeto) {
		return insert(objeto, null);
	}

	public static int insert(WorkflowAtributo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_atributo");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_entidade");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdEntidade()));
			int code = Conexao.getSequenceCode("ptc_workflow_atributo", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAtributo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_workflow_atributo (cd_atributo,"+
			                                  "cd_entidade,"+
			                                  "nm_atributo) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdEntidade()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEntidade());
			pstmt.setString(3,objeto.getNmAtributo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAtributoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAtributoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(WorkflowAtributo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(WorkflowAtributo objeto, int cdAtributoOld, int cdEntidadeOld) {
		return update(objeto, cdAtributoOld, cdEntidadeOld, null);
	}

	public static int update(WorkflowAtributo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(WorkflowAtributo objeto, int cdAtributoOld, int cdEntidadeOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_workflow_atributo SET cd_atributo=?,"+
												      		   "cd_entidade=?,"+
												      		   "nm_atributo=? WHERE cd_atributo=? AND cd_entidade=?");
			pstmt.setInt(1,objeto.getCdAtributo());
			pstmt.setInt(2,objeto.getCdEntidade());
			pstmt.setString(3,objeto.getNmAtributo());
			pstmt.setInt(4, cdAtributoOld!=0 ? cdAtributoOld : objeto.getCdAtributo());
			pstmt.setInt(5, cdEntidadeOld!=0 ? cdEntidadeOld : objeto.getCdEntidade());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAtributoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAtributoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAtributo, int cdEntidade) {
		return delete(cdAtributo, cdEntidade, null);
	}

	public static int delete(int cdAtributo, int cdEntidade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_workflow_atributo WHERE cd_atributo=? AND cd_entidade=?");
			pstmt.setInt(1, cdAtributo);
			pstmt.setInt(2, cdEntidade);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAtributoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAtributoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static WorkflowAtributo get(int cdAtributo, int cdEntidade) {
		return get(cdAtributo, cdEntidade, null);
	}

	public static WorkflowAtributo get(int cdAtributo, int cdEntidade, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_workflow_atributo WHERE cd_atributo=? AND cd_entidade=?");
			pstmt.setInt(1, cdAtributo);
			pstmt.setInt(2, cdEntidade);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new WorkflowAtributo(rs.getInt("cd_atributo"),
						rs.getInt("cd_entidade"),
						rs.getString("nm_atributo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAtributoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAtributoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_workflow_atributo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAtributoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAtributoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<WorkflowAtributo> getList() {
		return getList(null);
	}

	public static ArrayList<WorkflowAtributo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<WorkflowAtributo> list = new ArrayList<WorkflowAtributo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				WorkflowAtributo obj = WorkflowAtributoDAO.get(rsm.getInt("cd_atributo"), rsm.getInt("cd_entidade"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAtributoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ptc_workflow_atributo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
