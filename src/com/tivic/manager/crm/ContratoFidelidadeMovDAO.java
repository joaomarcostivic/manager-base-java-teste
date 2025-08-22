package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContratoFidelidadeMovDAO{

	public static int insert(ContratoFidelidadeMov objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContratoFidelidadeMov objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_contrato_fidelidade_mov (cd_contrato,"+
			                                  "cd_movimento) VALUES (?, ?)");
			if(objeto.getCdContrato()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContrato());
			if(objeto.getCdMovimento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMovimento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoFidelidadeMovDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoFidelidadeMovDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContratoFidelidadeMov objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ContratoFidelidadeMov objeto, int cdContratoOld, int cdMovimentoOld) {
		return update(objeto, cdContratoOld, cdMovimentoOld, null);
	}

	public static int update(ContratoFidelidadeMov objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ContratoFidelidadeMov objeto, int cdContratoOld, int cdMovimentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_contrato_fidelidade_mov SET cd_contrato=?,"+
												      		   "cd_movimento=? WHERE cd_contrato=? AND cd_movimento=?");
			pstmt.setInt(1,objeto.getCdContrato());
			pstmt.setInt(2,objeto.getCdMovimento());
			pstmt.setInt(3, cdContratoOld!=0 ? cdContratoOld : objeto.getCdContrato());
			pstmt.setInt(4, cdMovimentoOld!=0 ? cdMovimentoOld : objeto.getCdMovimento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoFidelidadeMovDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoFidelidadeMovDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContrato, int cdMovimento) {
		return delete(cdContrato, cdMovimento, null);
	}

	public static int delete(int cdContrato, int cdMovimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_contrato_fidelidade_mov WHERE cd_contrato=? AND cd_movimento=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdMovimento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoFidelidadeMovDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoFidelidadeMovDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContratoFidelidadeMov get(int cdContrato, int cdMovimento) {
		return get(cdContrato, cdMovimento, null);
	}

	public static ContratoFidelidadeMov get(int cdContrato, int cdMovimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_contrato_fidelidade_mov WHERE cd_contrato=? AND cd_movimento=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdMovimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContratoFidelidadeMov(rs.getInt("cd_contrato"),
						rs.getInt("cd_movimento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoFidelidadeMovDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoFidelidadeMovDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_contrato_fidelidade_mov");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoFidelidadeMovDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoFidelidadeMovDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM crm_contrato_fidelidade_mov", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
