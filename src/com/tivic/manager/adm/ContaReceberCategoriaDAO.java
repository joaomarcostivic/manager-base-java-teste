package com.tivic.manager.adm;

import java.sql.*;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContaReceberCategoriaDAO{

	
	public static int insert(ContaReceberCategoria objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContaReceberCategoria objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_conta_receber_categoria", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdContaReceberCategoria(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_conta_receber_categoria (cd_conta_receber,"+
			                                  "cd_categoria_economica,"+
			                                  "vl_conta_categoria,"+
			                                  "cd_centro_custo,"+
			                                  "cd_conta_receber_categoria) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContaReceber());
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
			System.err.println("Erro! ContaReceberCategoriaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberCategoriaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaReceberCategoria objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ContaReceberCategoria objeto, int cdContaReceberCategoriaOld) {
		return update(objeto, cdContaReceberCategoriaOld, null);
	}

	public static int update(ContaReceberCategoria objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ContaReceberCategoria objeto, int cdContaReceberCategoriaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_receber_categoria SET cd_conta_receber=?,"+
												      		   "cd_categoria_economica=?,"+
												      		   "vl_conta_categoria=?,"+
												      		   "cd_centro_custo=?,"+
												      		   "cd_conta_receber_categoria=? WHERE cd_conta_receber_categoria=?");
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContaReceber());
			if(objeto.getCdCategoriaEconomica()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCategoriaEconomica());
			pstmt.setDouble(3,objeto.getVlContaCategoria());
			if(objeto.getCdCentroCusto()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCentroCusto());
			pstmt.setInt(5,objeto.getCdContaReceberCategoria());
			pstmt.setInt(6, cdContaReceberCategoriaOld!=0 ? cdContaReceberCategoriaOld : objeto.getCdContaReceberCategoria());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberCategoriaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberCategoriaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContaReceberCategoria) {
		return delete(cdContaReceberCategoria, null);
	}

	public static int delete(int cdContaReceberCategoria, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_receber_categoria WHERE cd_conta_receber_categoria=?");
			pstmt.setInt(1, cdContaReceberCategoria);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberCategoriaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberCategoriaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaReceberCategoria get(int cdContaReceberCategoria) {
		return get(cdContaReceberCategoria, null);
	}

	public static ContaReceberCategoria get(int cdContaReceberCategoria, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_receber_categoria WHERE cd_conta_receber_categoria=?");
			pstmt.setInt(1, cdContaReceberCategoria);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaReceberCategoria(rs.getInt("cd_conta_receber"),
						rs.getInt("cd_categoria_economica"),
						rs.getFloat("vl_conta_categoria"),
						rs.getInt("cd_centro_custo"),
						rs.getInt("cd_conta_receber_categoria"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberCategoriaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberCategoriaDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ContaReceberCategoria> getList() {
		return getList(null);
	}

	public static ArrayList<ContaReceberCategoria> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ContaReceberCategoria> list = new ArrayList<ContaReceberCategoria>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ContaReceberCategoria obj = ContaReceberCategoriaDAO.get(rsm.getInt("cd_conta_receber_categoria"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberCategoriaDAO.getList: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	
	

	public static int update(ContaReceberCategoria objeto, int cdContaReceberOld, int cdCategoriaEconomicaOld) {
		return update(objeto, cdContaReceberOld, cdCategoriaEconomicaOld, null);
	}

	public static int update(ContaReceberCategoria objeto, int cdContaReceberOld, int cdCategoriaEconomicaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_receber_categoria SET cd_conta_receber=?,"+
												      		   "cd_categoria_economica=?,"+
												      		   "vl_conta_categoria=?," +
												      		   "cd_centro_custo=? WHERE cd_conta_receber=? AND cd_categoria_economica=?");
			pstmt.setInt(1,objeto.getCdContaReceber());
			pstmt.setInt(2,objeto.getCdCategoriaEconomica());
			pstmt.setDouble(3,objeto.getVlContaCategoria());
			if(objeto.getCdCentroCusto()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCentroCusto());
			pstmt.setInt(5, cdContaReceberOld!=0 ? cdContaReceberOld : objeto.getCdContaReceber());
			pstmt.setInt(6, cdCategoriaEconomicaOld!=0 ? cdCategoriaEconomicaOld : objeto.getCdCategoriaEconomica());
			System.out.println( pstmt );
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberCategoriaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberCategoriaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContaReceber, int cdCategoriaEconomica) {
		return delete(cdContaReceber, cdCategoriaEconomica, null);
	}

	public static int delete(int cdContaReceber, int cdCategoriaEconomica, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_receber_categoria WHERE cd_conta_receber=? AND cd_categoria_economica=?");
			pstmt.setInt(1, cdContaReceber);
			pstmt.setInt(2, cdCategoriaEconomica);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberCategoriaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberCategoriaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaReceberCategoria get(int cdContaReceber, int cdCategoriaEconomica) {
		return get(cdContaReceber, cdCategoriaEconomica, null);
	}

	public static ContaReceberCategoria get(int cdContaReceber, int cdCategoriaEconomica, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_receber_categoria WHERE cd_conta_receber=? AND cd_categoria_economica=?");
			pstmt.setInt(1, cdContaReceber);
			pstmt.setInt(2, cdCategoriaEconomica);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaReceberCategoria(rs.getInt("cd_conta_receber"),
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
			System.err.println("Erro! ContaReceberCategoriaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberCategoriaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_receber_categoria");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberCategoriaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberCategoriaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_conta_receber_categoria", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
