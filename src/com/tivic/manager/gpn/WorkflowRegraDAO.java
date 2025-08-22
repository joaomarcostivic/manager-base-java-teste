package com.tivic.manager.gpn;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class WorkflowRegraDAO{

	public static int insert(WorkflowRegra objeto) {
		return insert(objeto, null);
	}

	public static int insert(WorkflowRegra objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("gpn_workflow_regra", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRegra(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO gpn_workflow_regra (cd_regra,"+
			                                  "nm_regra,"+
			                                  "st_regra,"+
			                                  "dt_cadastro,"+
			                                  "id_regra,"+
			                                  "txt_descricao,"+
			                                  "dt_inicial,"+
			                                  "dt_final) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmRegra());
			pstmt.setInt(3,objeto.getStRegra());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setString(5,objeto.getIdRegra());
			pstmt.setString(6,objeto.getTxtDescricao());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(WorkflowRegra objeto) {
		return update(objeto, 0, null);
	}

	public static int update(WorkflowRegra objeto, int cdRegraOld) {
		return update(objeto, cdRegraOld, null);
	}

	public static int update(WorkflowRegra objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(WorkflowRegra objeto, int cdRegraOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE gpn_workflow_regra SET cd_regra=?,"+
												      		   "nm_regra=?,"+
												      		   "st_regra=?,"+
												      		   "dt_cadastro=?,"+
												      		   "id_regra=?,"+
												      		   "txt_descricao=?,"+
												      		   "dt_inicial=?,"+
												      		   "dt_final=? WHERE cd_regra=?");
			pstmt.setInt(1,objeto.getCdRegra());
			pstmt.setString(2,objeto.getNmRegra());
			pstmt.setInt(3,objeto.getStRegra());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setString(5,objeto.getIdRegra());
			pstmt.setString(6,objeto.getTxtDescricao());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setInt(9, cdRegraOld!=0 ? cdRegraOld : objeto.getCdRegra());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRegra) {
		return delete(cdRegra, null);
	}

	public static int delete(int cdRegra, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM gpn_workflow_regra WHERE cd_regra=?");
			pstmt.setInt(1, cdRegra);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static WorkflowRegra get(int cdRegra) {
		return get(cdRegra, null);
	}

	public static WorkflowRegra get(int cdRegra, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM gpn_workflow_regra WHERE cd_regra=?");
			pstmt.setInt(1, cdRegra);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new WorkflowRegra(rs.getInt("cd_regra"),
						rs.getString("nm_regra"),
						rs.getInt("st_regra"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
						rs.getString("id_regra"),
						rs.getString("txt_descricao"),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM gpn_workflow_regra");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<WorkflowRegra> getList() {
		return getList(null);
	}

	public static ArrayList<WorkflowRegra> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<WorkflowRegra> list = new ArrayList<WorkflowRegra>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				WorkflowRegra obj = WorkflowRegraDAO.get(rsm.getInt("cd_regra"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraDAO.getList: " + e);
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
		return Search.find("SELECT * FROM gpn_workflow_regra", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}