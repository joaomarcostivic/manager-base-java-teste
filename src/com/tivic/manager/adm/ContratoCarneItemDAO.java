package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContratoCarneItemDAO{

	public static int insert(ContratoCarneItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContratoCarneItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_contrato_carne_item (cd_contrato,"+
			                                  "cd_carne,"+
			                                  "cd_conta_receber) VALUES (?, ?, ?)");
			if(objeto.getCdContrato()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContrato());
			if(objeto.getCdCarne()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCarne());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContaReceber());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoCarneItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoCarneItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContratoCarneItem objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(ContratoCarneItem objeto, int cdContratoOld, int cdCarneOld, int cdContaReceberOld) {
		return update(objeto, cdContratoOld, cdCarneOld, cdContaReceberOld, null);
	}

	public static int update(ContratoCarneItem objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(ContratoCarneItem objeto, int cdContratoOld, int cdCarneOld, int cdContaReceberOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_contrato_carne_item SET cd_contrato=?,"+
												      		   "cd_carne=?,"+
												      		   "cd_conta_receber=? WHERE cd_contrato=? AND cd_carne=? AND cd_conta_receber=?");
			pstmt.setInt(1,objeto.getCdContrato());
			pstmt.setInt(2,objeto.getCdCarne());
			pstmt.setInt(3,objeto.getCdContaReceber());
			pstmt.setInt(4, cdContratoOld!=0 ? cdContratoOld : objeto.getCdContrato());
			pstmt.setInt(5, cdCarneOld!=0 ? cdCarneOld : objeto.getCdCarne());
			pstmt.setInt(6, cdContaReceberOld!=0 ? cdContaReceberOld : objeto.getCdContaReceber());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoCarneItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoCarneItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContrato, int cdCarne, int cdContaReceber) {
		return delete(cdContrato, cdCarne, cdContaReceber, null);
	}

	public static int delete(int cdContrato, int cdCarne, int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_contrato_carne_item WHERE cd_contrato=? AND cd_carne=? AND cd_conta_receber=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdCarne);
			pstmt.setInt(3, cdContaReceber);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoCarneItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoCarneItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContratoCarneItem get(int cdContrato, int cdCarne, int cdContaReceber) {
		return get(cdContrato, cdCarne, cdContaReceber, null);
	}

	public static ContratoCarneItem get(int cdContrato, int cdCarne, int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_carne_item WHERE cd_contrato=? AND cd_carne=? AND cd_conta_receber=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdCarne);
			pstmt.setInt(3, cdContaReceber);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContratoCarneItem(rs.getInt("cd_contrato"),
						rs.getInt("cd_carne"),
						rs.getInt("cd_conta_receber"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoCarneItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoCarneItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_carne_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoCarneItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoCarneItemDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_contrato_carne_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
