package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class PlanoVistoriaItemDefeitoDAO{

	public static int insert(PlanoVistoriaItemDefeito objeto) {
		return insert(objeto, null);
	}

	public static int insert(PlanoVistoriaItemDefeito objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_plano_vistoria_item_defeito (cd_defeito_vistoria_item,"+
			                                  "cd_plano_vistoria,"+
			                                  "cd_vistoria_item,"+
			                                  "vl_prazo,"+
			                                  "tp_prazo) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdDefeitoVistoriaItem()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdDefeitoVistoriaItem());
			if(objeto.getCdPlanoVistoria()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPlanoVistoria());
			if(objeto.getCdVistoriaItem()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdVistoriaItem());
			pstmt.setInt(4,objeto.getVlPrazo());
			pstmt.setInt(5,objeto.getTpPrazo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDefeitoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDefeitoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PlanoVistoriaItemDefeito objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(PlanoVistoriaItemDefeito objeto, int cdDefeitoVistoriaItemOld, int cdPlanoVistoriaOld, int cdVistoriaItemOld) {
		return update(objeto, cdDefeitoVistoriaItemOld, cdPlanoVistoriaOld, cdVistoriaItemOld, null);
	}

	public static int update(PlanoVistoriaItemDefeito objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(PlanoVistoriaItemDefeito objeto, int cdDefeitoVistoriaItemOld, int cdPlanoVistoriaOld, int cdVistoriaItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_plano_vistoria_item_defeito SET cd_defeito_vistoria_item=?,"+
												      		   "cd_plano_vistoria=?,"+
												      		   "cd_vistoria_item=?,"+
												      		   "vl_prazo=?,"+
												      		   "tp_prazo=? WHERE cd_defeito_vistoria_item=? AND cd_plano_vistoria=? AND cd_vistoria_item=?");
			pstmt.setInt(1,objeto.getCdDefeitoVistoriaItem());
			pstmt.setInt(2,objeto.getCdPlanoVistoria());
			pstmt.setInt(3,objeto.getCdVistoriaItem());
			pstmt.setInt(4,objeto.getVlPrazo());
			pstmt.setInt(5,objeto.getTpPrazo());
			pstmt.setInt(6, cdDefeitoVistoriaItemOld!=0 ? cdDefeitoVistoriaItemOld : objeto.getCdDefeitoVistoriaItem());
			pstmt.setInt(7, cdPlanoVistoriaOld!=0 ? cdPlanoVistoriaOld : objeto.getCdPlanoVistoria());
			pstmt.setInt(8, cdVistoriaItemOld!=0 ? cdVistoriaItemOld : objeto.getCdVistoriaItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDefeitoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDefeitoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDefeitoVistoriaItem, int cdPlanoVistoria, int cdVistoriaItem) {
		return delete(cdDefeitoVistoriaItem, cdPlanoVistoria, cdVistoriaItem, null);
	}

	public static int delete(int cdDefeitoVistoriaItem, int cdPlanoVistoria, int cdVistoriaItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_plano_vistoria_item_defeito WHERE cd_defeito_vistoria_item=? AND cd_plano_vistoria=? AND cd_vistoria_item=?");
			pstmt.setInt(1, cdDefeitoVistoriaItem);
			pstmt.setInt(2, cdPlanoVistoria);
			pstmt.setInt(3, cdVistoriaItem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDefeitoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDefeitoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PlanoVistoriaItemDefeito get(int cdDefeitoVistoriaItem, int cdPlanoVistoria, int cdVistoriaItem) {
		return get(cdDefeitoVistoriaItem, cdPlanoVistoria, cdVistoriaItem, null);
	}

	public static PlanoVistoriaItemDefeito get(int cdDefeitoVistoriaItem, int cdPlanoVistoria, int cdVistoriaItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_plano_vistoria_item_defeito WHERE cd_defeito_vistoria_item=? AND cd_plano_vistoria=? AND cd_vistoria_item=?");
			pstmt.setInt(1, cdDefeitoVistoriaItem);
			pstmt.setInt(2, cdPlanoVistoria);
			pstmt.setInt(3, cdVistoriaItem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PlanoVistoriaItemDefeito(rs.getInt("cd_defeito_vistoria_item"),
						rs.getInt("cd_plano_vistoria"),
						rs.getInt("cd_vistoria_item"),
						rs.getInt("vl_prazo"),
						rs.getInt("tp_prazo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDefeitoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDefeitoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_plano_vistoria_item_defeito");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDefeitoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDefeitoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PlanoVistoriaItemDefeito> getList() {
		return getList(null);
	}

	public static ArrayList<PlanoVistoriaItemDefeito> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PlanoVistoriaItemDefeito> list = new ArrayList<PlanoVistoriaItemDefeito>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PlanoVistoriaItemDefeito obj = PlanoVistoriaItemDefeitoDAO.get(rsm.getInt("cd_defeito_vistoria_item"), rsm.getInt("cd_plano_vistoria"), rsm.getInt("cd_vistoria_item"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDefeitoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_plano_vistoria_item_defeito", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
