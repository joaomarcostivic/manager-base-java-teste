package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContaReceberNegociacaoDAO{

	public static int insert(ContaReceberNegociacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContaReceberNegociacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_conta_receber_negociacao (cd_contrato,"+
			                                  "cd_negociacao,"+
			                                  "cd_conta_receber) VALUES (?, ?, ?)");
			if(objeto.getCdContrato()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContrato());
			if(objeto.getCdNegociacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdNegociacao());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContaReceber());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberNegociacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberNegociacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaReceberNegociacao objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(ContaReceberNegociacao objeto, int cdContratoOld, int cdNegociacaoOld, int cdContaReceberOld) {
		return update(objeto, cdContratoOld, cdNegociacaoOld, cdContaReceberOld, null);
	}

	public static int update(ContaReceberNegociacao objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(ContaReceberNegociacao objeto, int cdContratoOld, int cdNegociacaoOld, int cdContaReceberOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_receber_negociacao SET cd_contrato=?,"+
												      		   "cd_negociacao=?,"+
												      		   "cd_conta_receber=? WHERE cd_contrato=? AND cd_negociacao=? AND cd_conta_receber=?");
			pstmt.setInt(1,objeto.getCdContrato());
			pstmt.setInt(2,objeto.getCdNegociacao());
			pstmt.setInt(3,objeto.getCdContaReceber());
			pstmt.setInt(4, cdContratoOld!=0 ? cdContratoOld : objeto.getCdContrato());
			pstmt.setInt(5, cdNegociacaoOld!=0 ? cdNegociacaoOld : objeto.getCdNegociacao());
			pstmt.setInt(6, cdContaReceberOld!=0 ? cdContaReceberOld : objeto.getCdContaReceber());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberNegociacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberNegociacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContrato, int cdNegociacao, int cdContaReceber) {
		return delete(cdContrato, cdNegociacao, cdContaReceber, null);
	}

	public static int delete(int cdContrato, int cdNegociacao, int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_receber_negociacao WHERE cd_contrato=? AND cd_negociacao=? AND cd_conta_receber=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdNegociacao);
			pstmt.setInt(3, cdContaReceber);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberNegociacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberNegociacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaReceberNegociacao get(int cdContrato, int cdNegociacao, int cdContaReceber) {
		return get(cdContrato, cdNegociacao, cdContaReceber, null);
	}

	public static ContaReceberNegociacao get(int cdContrato, int cdNegociacao, int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_receber_negociacao WHERE cd_contrato=? AND cd_negociacao=? AND cd_conta_receber=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdNegociacao);
			pstmt.setInt(3, cdContaReceber);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaReceberNegociacao(rs.getInt("cd_contrato"),
						rs.getInt("cd_negociacao"),
						rs.getInt("cd_conta_receber"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberNegociacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberNegociacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_receber_negociacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberNegociacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberNegociacaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_conta_receber_negociacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
