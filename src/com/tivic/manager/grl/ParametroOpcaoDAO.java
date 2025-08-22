package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class ParametroOpcaoDAO{

	public static int insert(ParametroOpcao objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(ParametroOpcao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_parametro");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdParametro()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_opcao");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("grl_parametro_opcao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOpcao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_parametro_opcao (cd_parametro,"+
			                                  "cd_opcao,"+
			                                  "vl_apresentacao,"+
			                                  "vl_real,"+
			                                  "cd_pessoa,"+
			                                  "cd_empresa) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdParametro()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdParametro());
			pstmt.setInt(2, code);
			pstmt.setString(3,objeto.getVlApresentacao());
			pstmt.setString(4,objeto.getVlReal());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdPessoa());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEmpresa());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroOpcaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroOpcaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ParametroOpcao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ParametroOpcao objeto, int cdParametroOld, int cdOpcaoOld) {
		return update(objeto, cdParametroOld, cdOpcaoOld, null);
	}

	public static int update(ParametroOpcao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ParametroOpcao objeto, int cdParametroOld, int cdOpcaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_parametro_opcao SET cd_parametro=?,"+
												      		   "cd_opcao=?,"+
												      		   "vl_apresentacao=?,"+
												      		   "vl_real=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_empresa=? WHERE cd_parametro=? AND cd_opcao=?");
			pstmt.setInt(1,objeto.getCdParametro());
			pstmt.setInt(2,objeto.getCdOpcao());
			pstmt.setString(3,objeto.getVlApresentacao());
			pstmt.setString(4,objeto.getVlReal());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdPessoa());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEmpresa());
			pstmt.setInt(7, cdParametroOld!=0 ? cdParametroOld : objeto.getCdParametro());
			pstmt.setInt(8, cdOpcaoOld!=0 ? cdOpcaoOld : objeto.getCdOpcao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroOpcaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroOpcaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdParametro, int cdOpcao) {
		return delete(cdParametro, cdOpcao, null);
	}

	public static int delete(int cdParametro, int cdOpcao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_parametro_opcao WHERE cd_parametro=? AND cd_opcao=?");
			pstmt.setInt(1, cdParametro);
			pstmt.setInt(2, cdOpcao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroOpcaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroOpcaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ParametroOpcao get(int cdParametro, int cdOpcao) {
		return get(cdParametro, cdOpcao, null);
	}

	public static ParametroOpcao get(int cdParametro, int cdOpcao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_parametro_opcao WHERE cd_parametro=? AND cd_opcao=?");
			pstmt.setInt(1, cdParametro);
			pstmt.setInt(2, cdOpcao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ParametroOpcao(rs.getInt("cd_parametro"),
						rs.getInt("cd_opcao"),
						rs.getString("vl_apresentacao"),
						rs.getString("vl_real"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_empresa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroOpcaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroOpcaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_parametro_opcao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroOpcaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroOpcaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator>  criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator>  criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_parametro_opcao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
