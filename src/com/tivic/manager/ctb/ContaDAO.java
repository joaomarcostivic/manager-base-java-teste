package com.tivic.manager.ctb;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContaDAO{

	public static int insert(Conta objeto) {
		return insert(objeto, null);
	}

	public static int insert(Conta objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ctb_conta", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdConta(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ctb_conta (cd_conta,"+
			                                  "cd_historico_padrao,"+
			                                  "nm_conta,"+
			                                  "nr_conta_reduzida,"+
			                                  "nr_conta_impressao) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdHistoricoPadrao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdHistoricoPadrao());
			pstmt.setString(3,objeto.getNmConta());
			pstmt.setString(4,objeto.getNrContaReduzida());
			pstmt.setString(5,objeto.getNrContaImpressao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Conta objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Conta objeto, int cdContaOld) {
		return update(objeto, cdContaOld, null);
	}

	public static int update(Conta objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Conta objeto, int cdContaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ctb_conta SET cd_conta=?,"+
												      		   "cd_historico_padrao=?,"+
												      		   "nm_conta=?,"+
												      		   "nr_conta_reduzida=?,"+
												      		   "nr_conta_impressao=? WHERE cd_conta=?");
			pstmt.setInt(1,objeto.getCdConta());
			if(objeto.getCdHistoricoPadrao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdHistoricoPadrao());
			pstmt.setString(3,objeto.getNmConta());
			pstmt.setString(4,objeto.getNrContaReduzida());
			pstmt.setString(5,objeto.getNrContaImpressao());
			pstmt.setInt(6, cdContaOld!=0 ? cdContaOld : objeto.getCdConta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConta) {
		return delete(cdConta, null);
	}

	public static int delete(int cdConta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ctb_conta WHERE cd_conta=?");
			pstmt.setInt(1, cdConta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Conta get(int cdConta) {
		return get(cdConta, null);
	}

	public static Conta get(int cdConta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ctb_conta WHERE cd_conta=?");
			pstmt.setInt(1, cdConta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Conta(rs.getInt("cd_conta"),
						rs.getInt("cd_historico_padrao"),
						rs.getString("nm_conta"),
						rs.getString("nr_conta_reduzida"),
						rs.getString("nr_conta_impressao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_conta");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM ctb_conta", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
