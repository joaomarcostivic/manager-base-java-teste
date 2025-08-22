package com.tivic.manager.ord;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class PlanoTrabalhoItemDAO{

	public static int insert(PlanoTrabalhoItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(PlanoTrabalhoItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_plano_trabalho");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdPlanoTrabalho()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_item");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("ord_plano_trabalho_item", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdItem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ord_plano_trabalho_item (cd_plano_trabalho,"+
			                                  "cd_item,"+
			                                  "cd_documento_saida,"+
			                                  "cd_documento_entrada,"+
			                                  "tp_movimento) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdPlanoTrabalho()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPlanoTrabalho());
			pstmt.setInt(2, code);
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdDocumentoSaida());
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdDocumentoEntrada());
			pstmt.setInt(5,objeto.getTpMovimento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PlanoTrabalhoItem objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(PlanoTrabalhoItem objeto, int cdPlanoTrabalhoOld, int cdItemOld) {
		return update(objeto, cdPlanoTrabalhoOld, cdItemOld, null);
	}

	public static int update(PlanoTrabalhoItem objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(PlanoTrabalhoItem objeto, int cdPlanoTrabalhoOld, int cdItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ord_plano_trabalho_item SET cd_plano_trabalho=?,"+
												      		   "cd_item=?,"+
												      		   "cd_documento_saida=?,"+
												      		   "cd_documento_entrada=?,"+
												      		   "tp_movimento=? WHERE cd_plano_trabalho=? AND cd_item=?");
			pstmt.setInt(1,objeto.getCdPlanoTrabalho());
			pstmt.setInt(2,objeto.getCdItem());
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdDocumentoSaida());
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdDocumentoEntrada());
			pstmt.setInt(5,objeto.getTpMovimento());
			pstmt.setInt(6, cdPlanoTrabalhoOld!=0 ? cdPlanoTrabalhoOld : objeto.getCdPlanoTrabalho());
			pstmt.setInt(7, cdItemOld!=0 ? cdItemOld : objeto.getCdItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPlanoTrabalho, int cdItem) {
		return delete(cdPlanoTrabalho, cdItem, null);
	}

	public static int delete(int cdPlanoTrabalho, int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ord_plano_trabalho_item WHERE cd_plano_trabalho=? AND cd_item=?");
			pstmt.setInt(1, cdPlanoTrabalho);
			pstmt.setInt(2, cdItem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PlanoTrabalhoItem get(int cdPlanoTrabalho, int cdItem) {
		return get(cdPlanoTrabalho, cdItem, null);
	}

	public static PlanoTrabalhoItem get(int cdPlanoTrabalho, int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ord_plano_trabalho_item WHERE cd_plano_trabalho=? AND cd_item=?");
			pstmt.setInt(1, cdPlanoTrabalho);
			pstmt.setInt(2, cdItem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PlanoTrabalhoItem(rs.getInt("cd_plano_trabalho"),
						rs.getInt("cd_item"),
						rs.getInt("cd_documento_saida"),
						rs.getInt("cd_documento_entrada"),
						rs.getInt("tp_movimento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ord_plano_trabalho_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoItemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PlanoTrabalhoItem> getList() {
		return getList(null);
	}

	public static ArrayList<PlanoTrabalhoItem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PlanoTrabalhoItem> list = new ArrayList<PlanoTrabalhoItem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PlanoTrabalhoItem obj = PlanoTrabalhoItemDAO.get(rsm.getInt("cd_plano_trabalho"), rsm.getInt("cd_item"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoItemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ord_plano_trabalho_item", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}