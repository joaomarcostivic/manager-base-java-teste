package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.ItemComparator;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContaPagarCategoriaDAO{
	
	
	public static int insert(ContaPagarCategoria objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContaPagarCategoria objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_conta_pagar_categoria", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdContaPagarCategoria(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_conta_pagar_categoria (cd_conta_pagar,"+
			                                  "cd_categoria_economica,"+
			                                  "vl_conta_categoria,"+
			                                  "cd_centro_custo,"+
			                                  "cd_conta_pagar_categoria) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContaPagar());
			if(objeto.getCdCategoriaEconomica()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCategoriaEconomica());
			pstmt.setDouble(3,objeto.getVlContaCategoria());
			if(objeto.getCdCentroCusto()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCentroCusto());
			pstmt.setInt(5, code);
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaPagarCategoria objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ContaPagarCategoria objeto, int cdContaPagarCategoriaOld) {
		return update(objeto, cdContaPagarCategoriaOld, null);
	}

	public static int update(ContaPagarCategoria objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ContaPagarCategoria objeto, int cdContaPagarCategoriaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_pagar_categoria SET cd_conta_pagar=?,"+
												      		   "cd_categoria_economica=?,"+
												      		   "vl_conta_categoria=?,"+
												      		   "cd_centro_custo=?,"+
												      		   "cd_conta_pagar_categoria=? WHERE cd_conta_pagar_categoria=?");
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContaPagar());
			if(objeto.getCdCategoriaEconomica()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCategoriaEconomica());
			pstmt.setDouble(3,objeto.getVlContaCategoria());
			if(objeto.getCdCentroCusto()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCentroCusto());
			pstmt.setInt(5,objeto.getCdContaPagarCategoria());
			pstmt.setInt(6, cdContaPagarCategoriaOld!=0 ? cdContaPagarCategoriaOld : objeto.getCdContaPagarCategoria());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContaPagarCategoria) {
		return delete(cdContaPagarCategoria, null);
	}

	public static int delete(int cdContaPagarCategoria, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_pagar_categoria WHERE cd_conta_pagar_categoria=?");
			pstmt.setInt(1, cdContaPagarCategoria);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaPagarCategoria get(int cdContaPagarCategoria) {
		return get(cdContaPagarCategoria, null);
	}

	public static ContaPagarCategoria get(int cdContaPagarCategoria, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_pagar_categoria WHERE cd_conta_pagar_categoria=?");
			pstmt.setInt(1, cdContaPagarCategoria);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaPagarCategoria(rs.getInt("cd_conta_pagar"),
						rs.getInt("cd_categoria_economica"),
						rs.getFloat("vl_conta_categoria"),
						rs.getInt("cd_centro_custo"),
						rs.getInt("cd_conta_pagar_categoria"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ContaPagarCategoria> getList() {
		return getList(null);
	}

	public static ArrayList<ContaPagarCategoria> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ContaPagarCategoria> list = new ArrayList<ContaPagarCategoria>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ContaPagarCategoria obj = ContaPagarCategoriaDAO.get(rsm.getInt("cd_conta_pagar_categoria"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaDAO.getList: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	
	public static int update(ContaPagarCategoria objeto, int cdContaPagarOld, int cdCategoriaEconomicaOld) {
		return update(objeto, cdContaPagarOld, cdCategoriaEconomicaOld, null);
	}

	public static int update(ContaPagarCategoria objeto, int cdContaPagarOld, int cdCategoriaEconomicaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_pagar_categoria SET cd_conta_pagar=?,"+
												      		   "cd_categoria_economica=?,"+
												      		   "vl_conta_categoria=?," +
												      		   "cd_centro_custo=? WHERE cd_conta_pagar=? AND cd_categoria_economica=?");
			pstmt.setInt(1,objeto.getCdContaPagar());
			pstmt.setInt(2,objeto.getCdCategoriaEconomica());
			pstmt.setDouble(3,objeto.getVlContaCategoria());
			if(objeto.getCdCentroCusto()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCentroCusto());
			pstmt.setInt(5, cdContaPagarOld!=0 ? cdContaPagarOld : objeto.getCdContaPagar());
			pstmt.setInt(6, cdCategoriaEconomicaOld!=0 ? cdCategoriaEconomicaOld : objeto.getCdCategoriaEconomica());
			return pstmt.executeUpdate();
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContaPagar, int cdCategoriaEconomica) {
		return delete(cdContaPagar, cdCategoriaEconomica, null);
	}

	public static int delete(int cdContaPagar, int cdCategoriaEconomica, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_pagar_categoria WHERE cd_conta_pagar=? AND cd_categoria_economica=?");
			pstmt.setInt(1, cdContaPagar);
			pstmt.setInt(2, cdCategoriaEconomica);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaPagarCategoria get(int cdContaPagar, int cdCategoriaEconomica) {
		return get(cdContaPagar, cdCategoriaEconomica, null);
	}

	public static ContaPagarCategoria get(int cdContaPagar, int cdCategoriaEconomica, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_pagar_categoria WHERE cd_conta_pagar=? AND cd_categoria_economica=?");
			pstmt.setInt(1, cdContaPagar);
			pstmt.setInt(2, cdCategoriaEconomica);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaPagarCategoria(rs.getInt("cd_conta_pagar"),
						rs.getInt("cd_categoria_economica"),
						rs.getFloat("vl_conta_categoria"),
						rs.getInt("cd_centro_custo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_pagar_categoria");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_conta_pagar_categoria", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
