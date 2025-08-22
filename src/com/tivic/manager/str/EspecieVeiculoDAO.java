package com.tivic.manager.str;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class EspecieVeiculoDAO{

	public static int insert(EspecieVeiculo objeto) {
		return insert(objeto, null);
	}

	public static int insert(EspecieVeiculo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("str_especie_veiculo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdEspecie(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO str_especie_veiculo (cd_especie,"+
			                                  "ds_especie) VALUES (?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getDsEspecie());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EspecieVeiculoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EspecieVeiculoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EspecieVeiculo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(EspecieVeiculo objeto, int cdEspecieOld) {
		return update(objeto, cdEspecieOld, null);
	}

	public static int update(EspecieVeiculo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(EspecieVeiculo objeto, int cdEspecieOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE str_especie_veiculo SET cd_especie=?,"+
												      		   "ds_especie=? WHERE cd_especie=?");
			pstmt.setInt(1,objeto.getCdEspecie());
			pstmt.setString(2,objeto.getDsEspecie());
			pstmt.setInt(3, cdEspecieOld!=0 ? cdEspecieOld : objeto.getCdEspecie());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EspecieVeiculoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EspecieVeiculoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEspecie) {
		return delete(cdEspecie, null);
	}

	public static int delete(int cdEspecie, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM str_especie_veiculo WHERE cd_especie=?");
			pstmt.setInt(1, cdEspecie);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EspecieVeiculoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EspecieVeiculoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EspecieVeiculo get(int cdEspecie) {
		return get(cdEspecie, null);
	}

	public static EspecieVeiculo get(int cdEspecie, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM str_especie_veiculo WHERE cd_especie=?");
			pstmt.setInt(1, cdEspecie);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EspecieVeiculo(rs.getInt("cd_especie"),
						rs.getString("ds_especie"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EspecieVeiculoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EspecieVeiculoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM str_especie_veiculo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EspecieVeiculoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EspecieVeiculoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<EspecieVeiculo> getList() {
		return getList(null);
	}

	public static ArrayList<EspecieVeiculo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<EspecieVeiculo> list = new ArrayList<EspecieVeiculo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				EspecieVeiculo obj = EspecieVeiculoDAO.get(rsm.getInt("cd_especie"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EspecieVeiculoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM str_especie_veiculo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
