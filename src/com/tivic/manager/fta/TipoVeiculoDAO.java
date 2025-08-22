package com.tivic.manager.fta;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoVeiculoDAO{

	public static int insert(TipoVeiculo objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoVeiculo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fta_tipo_veiculo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if(objeto.getCdTipoVeiculo() < 0)
				objeto.setCdTipoVeiculo(code);

			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_tipo_veiculo (cd_tipo_veiculo,"+
			                                  												 "nm_tipo_veiculo,"+
																							 "txt_cnh_requerida) VALUES (?, ?, ?)");
			pstmt.setInt(1, objeto.getCdTipoVeiculo());
			pstmt.setString(2,objeto.getNmTipoVeiculo());
			pstmt.setString(3,objeto.getTxtCnhRequerida());
			pstmt.executeUpdate();
			return objeto.getCdTipoVeiculo();
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoVeiculo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoVeiculo objeto, int cdTipoVeiculoOld) {
		return update(objeto, cdTipoVeiculoOld, null);
	}

	public static int update(TipoVeiculo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoVeiculo objeto, int cdTipoVeiculoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_tipo_veiculo SET cd_tipo_veiculo=?,"+
												      		   "nm_tipo_veiculo=?, txt_cnh_requerida=? WHERE cd_tipo_veiculo=?");
			pstmt.setInt(1,objeto.getCdTipoVeiculo());
			pstmt.setString(2,objeto.getNmTipoVeiculo());
			pstmt.setString(3,objeto.getTxtCnhRequerida());
			pstmt.setInt(4, cdTipoVeiculoOld!=0 ? cdTipoVeiculoOld : objeto.getCdTipoVeiculo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoVeiculo) {
		return delete(cdTipoVeiculo, null);
	}

	public static int delete(int cdTipoVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_tipo_veiculo WHERE cd_tipo_veiculo=?");
			pstmt.setInt(1, cdTipoVeiculo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoVeiculo get(int cdTipoVeiculo) {
		return get(cdTipoVeiculo, null);
	}

	public static TipoVeiculo get(int cdTipoVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		System.out.println(cdTipoVeiculo);
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			if(!Util.isStrBaseAntiga()) {
				pstmt = connect.prepareStatement("SELECT * FROM fta_tipo_veiculo WHERE cd_tipo_veiculo=?");
				pstmt.setInt(1, cdTipoVeiculo);
				rs = pstmt.executeQuery();
				ResultSetMap rsm = new ResultSetMap(rs);
				if(rsm.next()){
					return new TipoVeiculo(
							rsm.getInt("cd_tipo_veiculo"),
							rsm.getString("nm_tipo_veiculo"),
							rsm.getString("txt_cnh_requerida")
							);
				}
			} else {
				pstmt = connect.prepareStatement("SELECT * FROM tipo_veiculo WHERE cod_tipo=?");
				pstmt.setInt(1, cdTipoVeiculo);
				rs = pstmt.executeQuery();
				ResultSetMap rsm = new ResultSetMap(rs);
				if(rsm.next()){
					return new TipoVeiculo(
							rsm.getInt("cod_tipo"),
							rsm.getString("nm_tipo"),
							rsm.getString("txt_cnh_requerida")
							);
				}
			}
			
			return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_tipo_veiculo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoVeiculo> getList() {
		return getList(null);
	}

	public static ArrayList<TipoVeiculo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoVeiculo> list = new ArrayList<TipoVeiculo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoVeiculo obj = TipoVeiculoDAO.get(rsm.getInt("cd_tipo_veiculo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM fta_tipo_veiculo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
