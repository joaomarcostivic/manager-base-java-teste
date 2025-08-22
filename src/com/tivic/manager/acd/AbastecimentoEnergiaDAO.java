package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class AbastecimentoEnergiaDAO{

	public static int insert(AbastecimentoEnergia objeto) {
		return insert(objeto, null);
	}

	public static int insert(AbastecimentoEnergia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_abastecimento_energia", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAbastecimentoEnergia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_abastecimento_energia (cd_abastecimento_energia,"+
			                                  "nm_abastecimento_energia,"+
			                                  "id_abastecimento_energia) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmAbastecimentoEnergia());
			pstmt.setString(3,objeto.getIdAbastecimentoEnergia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoEnergiaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoEnergiaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AbastecimentoEnergia objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AbastecimentoEnergia objeto, int cdAbastecimentoEnergiaOld) {
		return update(objeto, cdAbastecimentoEnergiaOld, null);
	}

	public static int update(AbastecimentoEnergia objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AbastecimentoEnergia objeto, int cdAbastecimentoEnergiaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_abastecimento_energia SET cd_abastecimento_energia=?,"+
												      		   "nm_abastecimento_energia=?,"+
												      		   "id_abastecimento_energia=? WHERE cd_abastecimento_energia=?");
			pstmt.setInt(1,objeto.getCdAbastecimentoEnergia());
			pstmt.setString(2,objeto.getNmAbastecimentoEnergia());
			pstmt.setString(3,objeto.getIdAbastecimentoEnergia());
			pstmt.setInt(4, cdAbastecimentoEnergiaOld!=0 ? cdAbastecimentoEnergiaOld : objeto.getCdAbastecimentoEnergia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoEnergiaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoEnergiaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAbastecimentoEnergia) {
		return delete(cdAbastecimentoEnergia, null);
	}

	public static int delete(int cdAbastecimentoEnergia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_abastecimento_energia WHERE cd_abastecimento_energia=?");
			pstmt.setInt(1, cdAbastecimentoEnergia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoEnergiaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoEnergiaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AbastecimentoEnergia get(int cdAbastecimentoEnergia) {
		return get(cdAbastecimentoEnergia, null);
	}

	public static AbastecimentoEnergia get(int cdAbastecimentoEnergia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_abastecimento_energia WHERE cd_abastecimento_energia=?");
			pstmt.setInt(1, cdAbastecimentoEnergia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AbastecimentoEnergia(rs.getInt("cd_abastecimento_energia"),
						rs.getString("nm_abastecimento_energia"),
						rs.getString("id_abastecimento_energia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoEnergiaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoEnergiaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_abastecimento_energia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoEnergiaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoEnergiaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AbastecimentoEnergia> getList() {
		return getList(null);
	}

	public static ArrayList<AbastecimentoEnergia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AbastecimentoEnergia> list = new ArrayList<AbastecimentoEnergia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AbastecimentoEnergia obj = AbastecimentoEnergiaDAO.get(rsm.getInt("cd_abastecimento_energia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoEnergiaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_abastecimento_energia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
