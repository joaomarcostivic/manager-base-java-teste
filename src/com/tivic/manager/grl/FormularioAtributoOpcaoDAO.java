package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class FormularioAtributoOpcaoDAO{

	public static int insert(FormularioAtributoOpcao objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int insert(FormularioAtributoOpcao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_opcao");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_formulario_atributo");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdFormularioAtributo()));
			int code = Conexao.getSequenceCode("grl_formulario_atributo_opcao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOpcao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_formulario_atributo_opcao (cd_opcao,"+
			                                  "cd_formulario_atributo,"+
			                                  "txt_opcao,"+
			                                  "vl_referencia,"+
			                                  "id_opcao,"+
			                                  "nr_ordem) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdFormularioAtributo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFormularioAtributo());
			pstmt.setString(3,objeto.getTxtOpcao());
			pstmt.setFloat(4,objeto.getVlReferencia());
			pstmt.setString(5,objeto.getIdOpcao());
			pstmt.setInt(6,objeto.getNrOrdem());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoOpcaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoOpcaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FormularioAtributoOpcao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(FormularioAtributoOpcao objeto, int cdOpcaoOld, int cdFormularioAtributoOld) {
		return update(objeto, cdOpcaoOld, cdFormularioAtributoOld, null);
	}

	public static int update(FormularioAtributoOpcao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(FormularioAtributoOpcao objeto, int cdOpcaoOld, int cdFormularioAtributoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_formulario_atributo_opcao SET cd_opcao=?,"+
												      		   "cd_formulario_atributo=?,"+
												      		   "txt_opcao=?,"+
												      		   "vl_referencia=?,"+
												      		   "id_opcao=?,"+
												      		   "nr_ordem=? WHERE cd_opcao=? AND cd_formulario_atributo=?");
			pstmt.setInt(1,objeto.getCdOpcao());
			pstmt.setInt(2,objeto.getCdFormularioAtributo());
			pstmt.setString(3,objeto.getTxtOpcao());
			pstmt.setFloat(4,objeto.getVlReferencia());
			pstmt.setString(5,objeto.getIdOpcao());
			pstmt.setInt(6,objeto.getNrOrdem());
			pstmt.setInt(7, cdOpcaoOld!=0 ? cdOpcaoOld : objeto.getCdOpcao());
			pstmt.setInt(8, cdFormularioAtributoOld!=0 ? cdFormularioAtributoOld : objeto.getCdFormularioAtributo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoOpcaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoOpcaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOpcao, int cdFormularioAtributo) {
		return delete(cdOpcao, cdFormularioAtributo, null);
	}

	public static int delete(int cdOpcao, int cdFormularioAtributo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_formulario_atributo_opcao WHERE cd_opcao=? AND cd_formulario_atributo=?");
			pstmt.setInt(1, cdOpcao);
			pstmt.setInt(2, cdFormularioAtributo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoOpcaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoOpcaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FormularioAtributoOpcao get(int cdOpcao, int cdFormularioAtributo) {
		return get(cdOpcao, cdFormularioAtributo, null);
	}

	public static FormularioAtributoOpcao get(int cdOpcao, int cdFormularioAtributo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_formulario_atributo_opcao WHERE cd_opcao=? AND cd_formulario_atributo=?");
			pstmt.setInt(1, cdOpcao);
			pstmt.setInt(2, cdFormularioAtributo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FormularioAtributoOpcao(rs.getInt("cd_opcao"),
						rs.getInt("cd_formulario_atributo"),
						rs.getString("txt_opcao"),
						rs.getFloat("vl_referencia"),
						rs.getString("id_opcao"),
						rs.getInt("nr_ordem"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoOpcaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoOpcaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_formulario_atributo_opcao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoOpcaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoOpcaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_formulario_atributo_opcao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
