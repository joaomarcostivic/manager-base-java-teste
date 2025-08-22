package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContaFactoringDAO{

	public static int insert(ContaFactoring objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContaFactoring objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_conta_factoring", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdContaFactoring(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_conta_factoring (cd_conta_factoring,"+
			                                  "cd_conta_pagar,"+
			                                  "cd_conta_receber_antecipada,"+
			                                  "qt_dias,"+
			                                  "pr_juros,"+
			                                  "vl_desconto, " +
			                                  "cd_conta_receber) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContaPagar());
			if(objeto.getCdContaReceberAntecipada()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContaReceberAntecipada());
			pstmt.setInt(4,objeto.getQtDias());
			pstmt.setFloat(5,objeto.getPrJuros());
			pstmt.setFloat(6,objeto.getVlDesconto());
			pstmt.setInt(7,objeto.getCdContaReceber());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFactoringDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFactoringDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaFactoring objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ContaFactoring objeto, int cdContaFactoringOld) {
		return update(objeto, cdContaFactoringOld, null);
	}

	public static int update(ContaFactoring objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ContaFactoring objeto, int cdContaFactoringOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_factoring SET cd_conta_factoring=?,"+
												      		   "cd_conta_pagar=?,"+
												      		   "cd_conta_receber_antecipada=?,"+
												      		   "qt_dias=?,"+
												      		   "pr_juros=?,"+
												      		   "vl_desconto=?, " +
												      		   "cd_conta_receber=? WHERE cd_conta_factoring=?");
			pstmt.setInt(1,objeto.getCdContaFactoring());
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContaPagar());
			if(objeto.getCdContaReceberAntecipada()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContaReceberAntecipada());
			pstmt.setInt(4,objeto.getQtDias());
			pstmt.setFloat(5,objeto.getPrJuros());
			pstmt.setFloat(6,objeto.getVlDesconto());
			pstmt.setInt(7,objeto.getCdContaReceber());
			pstmt.setInt(8, cdContaFactoringOld!=0 ? cdContaFactoringOld : objeto.getCdContaFactoring());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFactoringDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFactoringDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContaFactoring) {
		return delete(cdContaFactoring, null);
	}

	public static int delete(int cdContaFactoring, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_factoring WHERE cd_conta_factoring=?");
			pstmt.setInt(1, cdContaFactoring);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFactoringDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFactoringDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaFactoring get(int cdContaFactoring) {
		return get(cdContaFactoring, null);
	}

	public static ContaFactoring get(int cdContaFactoring, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_factoring WHERE cd_conta_factoring=?");
			pstmt.setInt(1, cdContaFactoring);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaFactoring(rs.getInt("cd_conta_factoring"),
						rs.getInt("cd_conta_pagar"),
						rs.getInt("cd_conta_receber_antecipada"),
						rs.getInt("qt_dias"),
						rs.getFloat("pr_juros"),
						rs.getFloat("vl_desconto"),
						rs.getInt("cd_conta_receber"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFactoringDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFactoringDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_factoring");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFactoringDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFactoringDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_conta_factoring", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
