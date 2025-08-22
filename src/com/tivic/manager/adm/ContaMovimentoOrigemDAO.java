package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ContaMovimentoOrigemDAO{

	public static int insert(ContaMovimentoOrigem objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContaMovimentoOrigem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_conta_movimento_origem", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdContaMovimentoOrigem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_conta_movimento_origem (cd_conta_movimento_origem,"+
			                                  "cd_movimento_conta,"+
			                                  "cd_conta,"+
			                                  "cd_conta_receber) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdMovimentoConta()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMovimentoConta());
			if(objeto.getCdConta()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdConta());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdContaReceber());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaMovimentoOrigemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaMovimentoOrigemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaMovimentoOrigem objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ContaMovimentoOrigem objeto, int cdContaMovimentoOrigemOld) {
		return update(objeto, cdContaMovimentoOrigemOld, null);
	}

	public static int update(ContaMovimentoOrigem objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ContaMovimentoOrigem objeto, int cdContaMovimentoOrigemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_movimento_origem SET cd_conta_movimento_origem=?,"+
												      		   "cd_movimento_conta=?,"+
												      		   "cd_conta=?,"+
												      		   "cd_conta_receber=? WHERE cd_conta_movimento_origem=?");
			pstmt.setInt(1,objeto.getCdContaMovimentoOrigem());
			if(objeto.getCdMovimentoConta()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMovimentoConta());
			if(objeto.getCdConta()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdConta());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdContaReceber());
			pstmt.setInt(5, cdContaMovimentoOrigemOld!=0 ? cdContaMovimentoOrigemOld : objeto.getCdContaMovimentoOrigem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaMovimentoOrigemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaMovimentoOrigemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContaMovimentoOrigem) {
		return delete(cdContaMovimentoOrigem, null);
	}

	public static int delete(int cdContaMovimentoOrigem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_movimento_origem WHERE cd_conta_movimento_origem=?");
			pstmt.setInt(1, cdContaMovimentoOrigem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaMovimentoOrigemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaMovimentoOrigemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaMovimentoOrigem get(int cdContaMovimentoOrigem) {
		return get(cdContaMovimentoOrigem, null);
	}

	public static ContaMovimentoOrigem get(int cdContaMovimentoOrigem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_movimento_origem WHERE cd_conta_movimento_origem=?");
			pstmt.setInt(1, cdContaMovimentoOrigem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaMovimentoOrigem(rs.getInt("cd_conta_movimento_origem"),
						rs.getInt("cd_movimento_conta"),
						rs.getInt("cd_conta"),
						rs.getInt("cd_conta_receber"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaMovimentoOrigemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaMovimentoOrigemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_movimento_origem");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaMovimentoOrigemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaMovimentoOrigemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ContaMovimentoOrigem> getList() {
		return getList(null);
	}

	public static ArrayList<ContaMovimentoOrigem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ContaMovimentoOrigem> list = new ArrayList<ContaMovimentoOrigem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ContaMovimentoOrigem obj = ContaMovimentoOrigemDAO.get(rsm.getInt("cd_conta_movimento_origem"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaMovimentoOrigemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM adm_conta_movimento_origem", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
