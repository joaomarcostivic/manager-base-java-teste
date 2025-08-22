package com.tivic.manager.ctb;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class SaldoContaExercicioDAO{

	public static int insert(SaldoContaExercicio objeto) {
		return insert(objeto, null);
	}

	public static int insert(SaldoContaExercicio objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ctb_saldo_conta_exercicio (cd_empresa,"+
			                                  "nr_ano_exercicio,"+
			                                  "cd_conta_plano_contas,"+
			                                  "vl_saldo) VALUES (?, ?, ?, ?)");
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpresa());
			pstmt.setString(2,objeto.getNrAnoExercicio());
			if(objeto.getCdContaPlanoContas()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContaPlanoContas());
			pstmt.setFloat(4,objeto.getVlSaldo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SaldoContaExercicioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SaldoContaExercicioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(SaldoContaExercicio objeto) {
		return update(objeto, 0, null, 0, null);
	}

	public static int update(SaldoContaExercicio objeto, int cdEmpresaOld, String nrAnoExercicioOld, int cdContaPlanoContasOld) {
		return update(objeto, cdEmpresaOld, nrAnoExercicioOld, cdContaPlanoContasOld, null);
	}

	public static int update(SaldoContaExercicio objeto, Connection connect) {
		return update(objeto, 0, null, 0, connect);
	}

	public static int update(SaldoContaExercicio objeto, int cdEmpresaOld, String nrAnoExercicioOld, int cdContaPlanoContasOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ctb_saldo_conta_exercicio SET cd_empresa=?,"+
												      		   "nr_ano_exercicio=?,"+
												      		   "cd_conta_plano_contas=?,"+
												      		   "vl_saldo=? WHERE cd_empresa=? AND nr_ano_exercicio=? AND cd_conta_plano_contas=?");
			pstmt.setInt(1,objeto.getCdEmpresa());
			pstmt.setString(2,objeto.getNrAnoExercicio());
			pstmt.setInt(3,objeto.getCdContaPlanoContas());
			pstmt.setFloat(4,objeto.getVlSaldo());
			pstmt.setInt(5, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setString(6, nrAnoExercicioOld!=null ? nrAnoExercicioOld : objeto.getNrAnoExercicio());
			pstmt.setInt(7, cdContaPlanoContasOld!=0 ? cdContaPlanoContasOld : objeto.getCdContaPlanoContas());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SaldoContaExercicioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SaldoContaExercicioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpresa, String nrAnoExercicio, int cdContaPlanoContas) {
		return delete(cdEmpresa, nrAnoExercicio, cdContaPlanoContas, null);
	}

	public static int delete(int cdEmpresa, String nrAnoExercicio, int cdContaPlanoContas, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ctb_saldo_conta_exercicio WHERE cd_empresa=? AND nr_ano_exercicio=? AND cd_conta_plano_contas=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setString(2, nrAnoExercicio);
			pstmt.setInt(3, cdContaPlanoContas);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SaldoContaExercicioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SaldoContaExercicioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static SaldoContaExercicio get(int cdEmpresa, String nrAnoExercicio, int cdContaPlanoContas) {
		return get(cdEmpresa, nrAnoExercicio, cdContaPlanoContas, null);
	}

	public static SaldoContaExercicio get(int cdEmpresa, String nrAnoExercicio, int cdContaPlanoContas, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ctb_saldo_conta_exercicio WHERE cd_empresa=? AND nr_ano_exercicio=? AND cd_conta_plano_contas=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setString(2, nrAnoExercicio);
			pstmt.setInt(3, cdContaPlanoContas);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new SaldoContaExercicio(rs.getInt("cd_empresa"),
						rs.getString("nr_ano_exercicio"),
						rs.getInt("cd_conta_plano_contas"),
						rs.getFloat("vl_saldo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SaldoContaExercicioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SaldoContaExercicioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_saldo_conta_exercicio");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SaldoContaExercicioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SaldoContaExercicioDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM ctb_saldo_conta_exercicio", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
