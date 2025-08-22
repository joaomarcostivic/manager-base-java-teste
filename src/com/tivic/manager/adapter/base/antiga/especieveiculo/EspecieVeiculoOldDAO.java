package com.tivic.manager.adapter.base.antiga.especieveiculo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class EspecieVeiculoOldDAO {
	
	public static int insert(EspecieVeiculoOld objeto) {
		return insert(objeto, null);
	}

	public static int insert(EspecieVeiculoOld objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("especie", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if(objeto.getCodEspecie()<=0)
				objeto.setCodEspecie(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO especie_veiculo (cod_especie,"+
			                                  "cod_especie,"+
			                                  "ds_especie) VALUES (?, ?)");
			pstmt.setInt(1, objeto.getCodEspecie());
			pstmt.setString(2,objeto.getDsEspecie());
			pstmt.executeUpdate();
			return objeto.getCodEspecie();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EspecieOldDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EspecieVeiculoOld objeto) {
		return update(objeto, 0, null);
	}

	public static int update(EspecieVeiculoOld objeto, int codEspecieOld) {
		return update(objeto, codEspecieOld, null);
	}

	public static int update(EspecieVeiculoOld objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(EspecieVeiculoOld objeto, int codEspecieOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE especie_veiculo SET cod_especie=?,"+
												      		   "ds_especie=? WHERE cod_especie=?");
			pstmt.setInt(1, objeto.getCodEspecie());
			pstmt.setString(2,objeto.getDsEspecie());
			pstmt.setInt(3, codEspecieOld!=0 ? codEspecieOld : objeto.getCodEspecie());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EspecieOldDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int codEspecie) {
		return delete(codEspecie, null);
	}

	public static int delete(int codEspecie, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM especie_veiculo WHERE cod_especie=?");
			pstmt.setInt(1, codEspecie);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EspecieOldDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EspecieVeiculoOld get(int codEspecie) {
		return get(codEspecie, null);
	}

	public static EspecieVeiculoOld get(int codEspecie, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM especie_veiculo WHERE cod_especie=?");
			pstmt.setInt(1, codEspecie);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EspecieVeiculoOld(rs.getInt("cod_especie"),
						rs.getString("ds_especie"));
			}
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EspecieOldDAO.get: " + e);
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
		return Search.find("SELECT * FROM especie_veiculo ", " ORDER BY cod_especie", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
