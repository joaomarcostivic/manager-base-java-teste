package com.tivic.manager.ctb;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContaCentroCustoDAO{

	public static int insert(ContaCentroCusto objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContaCentroCusto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ctb_conta_centro_custo (cd_centro_custo,"+
			                                  "cd_conta_plano_contas,"+
			                                  "pr_rateio) VALUES (?, ?, ?)");
			if(objeto.getCdCentroCusto()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdCentroCusto());
			if(objeto.getCdContaPlanoContas()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContaPlanoContas());
			pstmt.setFloat(3,objeto.getPrRateio());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaCentroCustoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaCentroCustoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaCentroCusto objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ContaCentroCusto objeto, int cdCentroCustoOld, int cdContaPlanoContasOld) {
		return update(objeto, cdCentroCustoOld, cdContaPlanoContasOld, null);
	}

	public static int update(ContaCentroCusto objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ContaCentroCusto objeto, int cdCentroCustoOld, int cdContaPlanoContasOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ctb_conta_centro_custo SET cd_centro_custo=?,"+
												      		   "cd_conta_plano_contas=?,"+
												      		   "pr_rateio=? WHERE cd_centro_custo=? AND cd_conta_plano_contas=?");
			pstmt.setInt(1,objeto.getCdCentroCusto());
			pstmt.setInt(2,objeto.getCdContaPlanoContas());
			pstmt.setFloat(3,objeto.getPrRateio());
			pstmt.setInt(4, cdCentroCustoOld!=0 ? cdCentroCustoOld : objeto.getCdCentroCusto());
			pstmt.setInt(5, cdContaPlanoContasOld!=0 ? cdContaPlanoContasOld : objeto.getCdContaPlanoContas());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaCentroCustoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaCentroCustoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCentroCusto, int cdContaPlanoContas) {
		return delete(cdCentroCusto, cdContaPlanoContas, null);
	}

	public static int delete(int cdCentroCusto, int cdContaPlanoContas, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ctb_conta_centro_custo WHERE cd_centro_custo=? AND cd_conta_plano_contas=?");
			pstmt.setInt(1, cdCentroCusto);
			pstmt.setInt(2, cdContaPlanoContas);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaCentroCustoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaCentroCustoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaCentroCusto get(int cdCentroCusto, int cdContaPlanoContas) {
		return get(cdCentroCusto, cdContaPlanoContas, null);
	}

	public static ContaCentroCusto get(int cdCentroCusto, int cdContaPlanoContas, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ctb_conta_centro_custo WHERE cd_centro_custo=? AND cd_conta_plano_contas=?");
			pstmt.setInt(1, cdCentroCusto);
			pstmt.setInt(2, cdContaPlanoContas);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaCentroCusto(rs.getInt("cd_centro_custo"),
						rs.getInt("cd_conta_plano_contas"),
						rs.getFloat("pr_rateio"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaCentroCustoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaCentroCustoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_conta_centro_custo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaCentroCustoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaCentroCustoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM ctb_conta_centro_custo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
