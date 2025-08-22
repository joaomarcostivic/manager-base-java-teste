package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.EstadoServices;
import com.tivic.manager.grl.Ocorrencia;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;

public class ErroRetornoDAO {

	public static ErroRetorno get(String nrErro) {
		return get(nrErro, null);
	}

	public static ErroRetorno get(String nrErro, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_erro_retorno WHERE nr_erro=? AND uf=?");
			pstmt.setString(1, nrErro);
			pstmt.setString(2, EstadoServices.getEstadoOrgaoAutuador().getSgEstado());
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				ErroRetorno erroRetorno = new ErroRetorno(rs.getString("nr_erro"),
						rs.getInt("tp_arquivo"),
						rs.getInt("tp_registro"),
						rs.getString("ds_erro"),
						rs.getInt("tp_retorno"),
						rs.getString("uf"),
						rs.getString("ds_sugestao_correcao"),
						rs.getInt("lg_correcao_automatica"));
				
				System.out.println(erroRetorno.toString());
				
				return erroRetorno;
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ErroRetornoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ErroRetornoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_erro_retorno");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ErroRetornoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ErroRetornoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ErroRetorno> getList() {
		return getList(null);
	}

	public static ArrayList<ErroRetorno> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ErroRetorno> list = new ArrayList<ErroRetorno>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ErroRetorno obj = ErroRetornoDAO.get(rsm.getString("nr_erro"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ErroRetornoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_erro_retorno", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
