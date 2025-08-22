package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;

public class ParametroValorDAO{

	public static int insert(ParametroValor objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(ParametroValor objeto, Connection connect){
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
			keys[1].put("FIELD_NAME", "cd_valor");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("grl_parametro_valor", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdValor(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_parametro_valor (cd_parametro,"+
			                                  "cd_valor,"+
			                                  "cd_opcao,"+
			                                  "cd_empresa,"+
			                                  "cd_pessoa,"+
			                                  "blb_valor,"+
			                                  "vl_inicial,"+
			                                  "vl_final) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdParametro()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdParametro());
			pstmt.setInt(2, code);
			if(objeto.getCdOpcao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdOpcao());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEmpresa());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdPessoa());
			if(objeto.getBlbValor()==null)
				pstmt.setNull(6, Types.BINARY);
			else
				pstmt.setBytes(6,objeto.getBlbValor());
			pstmt.setString(7,objeto.getVlInicial());
			pstmt.setString(8,objeto.getVlFinal());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroValorDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroValorDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ParametroValor objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ParametroValor objeto, int cdParametroOld, int cdValorOld) {
		return update(objeto, cdParametroOld, cdValorOld, null);
	}

	public static int update(ParametroValor objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ParametroValor objeto, int cdParametroOld, int cdValorOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_parametro_valor SET cd_parametro=?,"+
												      		   "cd_valor=?,"+
												      		   "cd_opcao=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_pessoa=?,"+
												      		   "blb_valor=?,"+
												      		   "vl_inicial=?,"+
												      		   "vl_final=? WHERE cd_parametro=? AND cd_valor=?");
			pstmt.setInt(1,objeto.getCdParametro());
			pstmt.setInt(2,objeto.getCdValor());
			if(objeto.getCdOpcao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdOpcao());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEmpresa());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdPessoa());
			if(objeto.getBlbValor()==null)
				pstmt.setNull(6, Types.BINARY);
			else
				pstmt.setBytes(6,objeto.getBlbValor());
			pstmt.setString(7,objeto.getVlInicial());
			pstmt.setString(8,objeto.getVlFinal());
			pstmt.setInt(9, cdParametroOld!=0 ? cdParametroOld : objeto.getCdParametro());
			pstmt.setInt(10, cdValorOld!=0 ? cdValorOld : objeto.getCdValor());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroValorDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroValorDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdParametro, int cdValor) {
		return delete(cdParametro, cdValor, null);
	}

	public static int delete(int cdParametro, int cdValor, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_parametro_valor WHERE cd_parametro=? AND cd_valor=?");
			pstmt.setInt(1, cdParametro);
			pstmt.setInt(2, cdValor);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroValorDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroValorDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ParametroValor get(int cdParametro, int cdValor) {
		return get(cdParametro, cdValor, null);
	}

	public static ParametroValor get(int cdParametro, int cdValor, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_parametro_valor WHERE cd_parametro=? AND cd_valor=?");
			pstmt.setInt(1, cdParametro);
			pstmt.setInt(2, cdValor);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ParametroValor(rs.getInt("cd_parametro"),
						rs.getInt("cd_valor"),
						rs.getInt("cd_opcao"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_pessoa"),
						rs.getBytes("blb_valor")==null?null:rs.getBytes("blb_valor"),
						rs.getString("vl_inicial"),
						rs.getString("vl_final"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroValorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroValorDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_parametro_valor");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroValorDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroValorDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_parametro_valor", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
