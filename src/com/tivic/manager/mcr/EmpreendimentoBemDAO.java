package com.tivic.manager.mcr;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class EmpreendimentoBemDAO{

	public static int insert(EmpreendimentoBem objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(EmpreendimentoBem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_empreendimento");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdEmpreendimento()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_bem");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("mcr_empreendimento_bem", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO mcr_empreendimento_bem (cd_empreendimento,"+
			                                  "cd_bem,"+
			                                  "nm_bem,"+
			                                  "vl_mercado,"+
			                                  "lg_dacao_garantia) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdEmpreendimento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpreendimento());
			pstmt.setInt(2, code);
			pstmt.setString(3,objeto.getNmBem());
			pstmt.setFloat(4,objeto.getVlMercado());
			pstmt.setInt(5,objeto.getLgDacaoGarantia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoBemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoBemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EmpreendimentoBem objeto) {
		return update(objeto, null);
	}

	public static int update(EmpreendimentoBem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE mcr_empreendimento_bem SET nm_bem=?,"+
			                                  "vl_mercado=?,"+
			                                  "lg_dacao_garantia=? WHERE cd_empreendimento=? AND cd_bem=?");
			pstmt.setString(1,objeto.getNmBem());
			pstmt.setFloat(2,objeto.getVlMercado());
			pstmt.setInt(3,objeto.getLgDacaoGarantia());
			pstmt.setInt(4,objeto.getCdEmpreendimento());
			pstmt.setInt(5,objeto.getCdBem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoBemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoBemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpreendimento, int cdBem) {
		return delete(cdEmpreendimento, cdBem, null);
	}

	public static int delete(int cdEmpreendimento, int cdBem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM mcr_empreendimento_bem WHERE cd_empreendimento=? AND cd_bem=?");
			pstmt.setInt(1, cdEmpreendimento);
			pstmt.setInt(2, cdBem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoBemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoBemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EmpreendimentoBem get(int cdEmpreendimento, int cdBem) {
		return get(cdEmpreendimento, cdBem, null);
	}

	public static EmpreendimentoBem get(int cdEmpreendimento, int cdBem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendimento_bem WHERE cd_empreendimento=? AND cd_bem=?");
			pstmt.setInt(1, cdEmpreendimento);
			pstmt.setInt(2, cdBem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EmpreendimentoBem(rs.getInt("cd_empreendimento"),
						rs.getInt("cd_bem"),
						rs.getString("nm_bem"),
						rs.getFloat("vl_mercado"),
						rs.getInt("lg_dacao_garantia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoBemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoBemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendimento_bem");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoBemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoBemDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM mcr_empreendimento_bem", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
