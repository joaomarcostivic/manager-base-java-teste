package com.tivic.manager.mcr;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class EmpreendimentoClienteDAO{

	public static int insert(EmpreendimentoCliente objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(EmpreendimentoCliente objeto, Connection connect){
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
			keys[1].put("FIELD_NAME", "cd_cliente");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("mcr_empreendimento_cliente", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO mcr_empreendimento_cliente (cd_empreendimento,"+
			                                  "cd_cliente,"+
			                                  "nm_cliente,"+
			                                  "pr_vendas) VALUES (?, ?, ?, ?)");
			if(objeto.getCdEmpreendimento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpreendimento());
			pstmt.setInt(2, code);
			pstmt.setString(3,objeto.getNmCliente());
			pstmt.setFloat(4,objeto.getPrVendas());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoClienteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoClienteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EmpreendimentoCliente objeto) {
		return update(objeto, null);
	}

	public static int update(EmpreendimentoCliente objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE mcr_empreendimento_cliente SET nm_cliente=?,"+
			                                  "pr_vendas=? WHERE cd_empreendimento=? AND cd_cliente=?");
			pstmt.setString(1,objeto.getNmCliente());
			pstmt.setFloat(2,objeto.getPrVendas());
			pstmt.setInt(3,objeto.getCdEmpreendimento());
			pstmt.setInt(4,objeto.getCdCliente());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoClienteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoClienteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpreendimento, int cdCliente) {
		return delete(cdEmpreendimento, cdCliente, null);
	}

	public static int delete(int cdEmpreendimento, int cdCliente, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM mcr_empreendimento_cliente WHERE cd_empreendimento=? AND cd_cliente=?");
			pstmt.setInt(1, cdEmpreendimento);
			pstmt.setInt(2, cdCliente);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoClienteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoClienteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EmpreendimentoCliente get(int cdEmpreendimento, int cdCliente) {
		return get(cdEmpreendimento, cdCliente, null);
	}

	public static EmpreendimentoCliente get(int cdEmpreendimento, int cdCliente, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendimento_cliente WHERE cd_empreendimento=? AND cd_cliente=?");
			pstmt.setInt(1, cdEmpreendimento);
			pstmt.setInt(2, cdCliente);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EmpreendimentoCliente(rs.getInt("cd_empreendimento"),
						rs.getInt("cd_cliente"),
						rs.getString("nm_cliente"),
						rs.getFloat("pr_vendas"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoClienteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoClienteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendimento_cliente");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoClienteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoClienteDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM mcr_empreendimento_cliente", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
