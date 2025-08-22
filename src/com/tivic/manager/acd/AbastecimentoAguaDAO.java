package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class AbastecimentoAguaDAO{

	public static int insert(AbastecimentoAgua objeto) {
		return insert(objeto, null);
	}

	public static int insert(AbastecimentoAgua objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_abastecimento_agua", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAbastecimentoAgua(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_abastecimento_agua (cd_abastecimento_agua,"+
			                                  "nm_abastecimento_agua,"+
			                                  "id_abastecimento_agua) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmAbastecimentoAgua());
			pstmt.setString(3,objeto.getIdAbastecimentoAgua());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoAguaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoAguaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AbastecimentoAgua objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AbastecimentoAgua objeto, int cdAbastecimentoAguaOld) {
		return update(objeto, cdAbastecimentoAguaOld, null);
	}

	public static int update(AbastecimentoAgua objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AbastecimentoAgua objeto, int cdAbastecimentoAguaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_abastecimento_agua SET cd_abastecimento_agua=?,"+
												      		   "nm_abastecimento_agua=?,"+
												      		   "id_abastecimento_agua=? WHERE cd_abastecimento_agua=?");
			pstmt.setInt(1,objeto.getCdAbastecimentoAgua());
			pstmt.setString(2,objeto.getNmAbastecimentoAgua());
			pstmt.setString(3,objeto.getIdAbastecimentoAgua());
			pstmt.setInt(4, cdAbastecimentoAguaOld!=0 ? cdAbastecimentoAguaOld : objeto.getCdAbastecimentoAgua());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoAguaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoAguaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAbastecimentoAgua) {
		return delete(cdAbastecimentoAgua, null);
	}

	public static int delete(int cdAbastecimentoAgua, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_abastecimento_agua WHERE cd_abastecimento_agua=?");
			pstmt.setInt(1, cdAbastecimentoAgua);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoAguaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoAguaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AbastecimentoAgua get(int cdAbastecimentoAgua) {
		return get(cdAbastecimentoAgua, null);
	}

	public static AbastecimentoAgua get(int cdAbastecimentoAgua, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_abastecimento_agua WHERE cd_abastecimento_agua=?");
			pstmt.setInt(1, cdAbastecimentoAgua);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AbastecimentoAgua(rs.getInt("cd_abastecimento_agua"),
						rs.getString("nm_abastecimento_agua"),
						rs.getString("id_abastecimento_agua"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoAguaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoAguaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_abastecimento_agua");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoAguaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoAguaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AbastecimentoAgua> getList() {
		return getList(null);
	}

	public static ArrayList<AbastecimentoAgua> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AbastecimentoAgua> list = new ArrayList<AbastecimentoAgua>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AbastecimentoAgua obj = AbastecimentoAguaDAO.get(rsm.getInt("cd_abastecimento_agua"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoAguaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_abastecimento_agua", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
