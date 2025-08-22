package com.tivic.manager.mcr;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class EmpreendedorRendaDAO{

	public static int insert(EmpreendedorRenda objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(EmpreendedorRenda objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_pessoa");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdPessoa()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_renda");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("mcr_empreendedor_renda", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO mcr_empreendedor_renda (cd_pessoa,"+
			                                  "cd_renda,"+
			                                  "nm_pessoa,"+
			                                  "nm_fonte_renda,"+
			                                  "vl_mensal) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2, code);
			pstmt.setString(3,objeto.getNmPessoa());
			pstmt.setString(4,objeto.getNmFonteRenda());
			pstmt.setFloat(5,objeto.getVlMensal());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendedorRendaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendedorRendaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EmpreendedorRenda objeto) {
		return update(objeto, null);
	}

	public static int update(EmpreendedorRenda objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE mcr_empreendedor_renda SET nm_pessoa=?,"+
			                                  "nm_fonte_renda=?,"+
			                                  "vl_mensal=? WHERE cd_pessoa=? AND cd_renda=?");
			pstmt.setString(1,objeto.getNmPessoa());
			pstmt.setString(2,objeto.getNmFonteRenda());
			pstmt.setFloat(3,objeto.getVlMensal());
			pstmt.setInt(4,objeto.getCdPessoa());
			pstmt.setInt(5,objeto.getCdRenda());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendedorRendaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendedorRendaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoa, int cdRenda) {
		return delete(cdPessoa, cdRenda, null);
	}

	public static int delete(int cdPessoa, int cdRenda, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM mcr_empreendedor_renda WHERE cd_pessoa=? AND cd_renda=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdRenda);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendedorRendaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendedorRendaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EmpreendedorRenda get(int cdPessoa, int cdRenda) {
		return get(cdPessoa, cdRenda, null);
	}

	public static EmpreendedorRenda get(int cdPessoa, int cdRenda, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendedor_renda WHERE cd_pessoa=? AND cd_renda=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdRenda);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EmpreendedorRenda(rs.getInt("cd_pessoa"),
						rs.getInt("cd_renda"),
						rs.getString("nm_pessoa"),
						rs.getString("nm_fonte_renda"),
						rs.getFloat("vl_mensal"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendedorRendaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendedorRendaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendedor_renda");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendedorRendaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendedorRendaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM mcr_empreendedor_renda", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
