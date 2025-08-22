package com.tivic.manager.cae;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class PreparacaoIngredienteDAO{

	public static int insert(PreparacaoIngrediente objeto) {
		return insert(objeto, null);
	}

	public static int insert(PreparacaoIngrediente objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO cae_preparacao_ingrediente (cd_preparacao,"+
			                                  "cd_ingrediente,"+
			                                  "vl_utilizado) VALUES (?, ?, ?)");
			if(objeto.getCdPreparacao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPreparacao());
			if(objeto.getCdIngrediente()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdIngrediente());
			pstmt.setFloat(3,objeto.getVlUtilizado());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoIngredienteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoIngredienteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PreparacaoIngrediente objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(PreparacaoIngrediente objeto, int cdPreparacaoOld, int cdIngredienteOld) {
		return update(objeto, cdPreparacaoOld, cdIngredienteOld, null);
	}

	public static int update(PreparacaoIngrediente objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(PreparacaoIngrediente objeto, int cdPreparacaoOld, int cdIngredienteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE cae_preparacao_ingrediente SET cd_preparacao=?,"+
												      		   "cd_ingrediente=?,"+
												      		   "vl_utilizado=? WHERE cd_preparacao=? AND cd_ingrediente=?");
			pstmt.setInt(1,objeto.getCdPreparacao());
			pstmt.setInt(2,objeto.getCdIngrediente());
			pstmt.setFloat(3,objeto.getVlUtilizado());
			pstmt.setInt(4, cdPreparacaoOld!=0 ? cdPreparacaoOld : objeto.getCdPreparacao());
			pstmt.setInt(5, cdIngredienteOld!=0 ? cdIngredienteOld : objeto.getCdIngrediente());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoIngredienteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoIngredienteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPreparacao, int cdIngrediente) {
		return delete(cdPreparacao, cdIngrediente, null);
	}

	public static int delete(int cdPreparacao, int cdIngrediente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM cae_preparacao_ingrediente WHERE cd_preparacao=? AND cd_ingrediente=?");
			pstmt.setInt(1, cdPreparacao);
			pstmt.setInt(2, cdIngrediente);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoIngredienteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoIngredienteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PreparacaoIngrediente get(int cdPreparacao, int cdIngrediente) {
		return get(cdPreparacao, cdIngrediente, null);
	}

	public static PreparacaoIngrediente get(int cdPreparacao, int cdIngrediente, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM cae_preparacao_ingrediente WHERE cd_preparacao=? AND cd_ingrediente=?");
			pstmt.setInt(1, cdPreparacao);
			pstmt.setInt(2, cdIngrediente);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PreparacaoIngrediente(rs.getInt("cd_preparacao"),
						rs.getInt("cd_ingrediente"),
						rs.getFloat("vl_utilizado"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoIngredienteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoIngredienteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM cae_preparacao_ingrediente");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoIngredienteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoIngredienteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PreparacaoIngrediente> getList() {
		return getList(null);
	}

	public static ArrayList<PreparacaoIngrediente> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PreparacaoIngrediente> list = new ArrayList<PreparacaoIngrediente>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PreparacaoIngrediente obj = PreparacaoIngredienteDAO.get(rsm.getInt("cd_preparacao"), rsm.getInt("cd_ingrediente"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoIngredienteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM cae_preparacao_ingrediente", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
