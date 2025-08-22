package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class VistoriaPlanoItemDefeitoDAO{

	public static int insert(VistoriaPlanoItemDefeito objeto) {
		return insert(objeto, null);
	}

	public static int insert(VistoriaPlanoItemDefeito objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_vistoria_plano_item_defeito", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdVistoriaPlanoItemDefeito(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_vistoria_plano_item_defeito (cd_vistoria_plano_item_defeito,"+
			                                  "cd_defeito_vistoria_item,"+
			                                  "cd_vistoria_plano_item,"+
			                                  "cd_plano_vistoria,"+
			                                  "cd_vistoria_item) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdDefeitoVistoriaItem()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDefeitoVistoriaItem());
			pstmt.setInt(3,objeto.getCdVistoriaPlanoItem());
			if(objeto.getCdPlanoVistoria()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdPlanoVistoria());
			if(objeto.getCdVistoriaItem()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdVistoriaItem());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDefeitoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDefeitoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(VistoriaPlanoItemDefeito objeto) {
		return update(objeto, 0, null);
	}

	public static int update(VistoriaPlanoItemDefeito objeto, int cdVistoriaPlanoItemDefeitoOld) {
		return update(objeto, cdVistoriaPlanoItemDefeitoOld, null);
	}

	public static int update(VistoriaPlanoItemDefeito objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(VistoriaPlanoItemDefeito objeto, int cdVistoriaPlanoItemDefeitoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_vistoria_plano_item_defeito SET cd_vistoria_plano_item_defeito=?,"+
												      		   "cd_defeito_vistoria_item=?,"+
												      		   "cd_vistoria_plano_item=?,"+
												      		   "cd_plano_vistoria=?,"+
												      		   "cd_vistoria_item=? WHERE cd_vistoria_plano_item_defeito=?");
			pstmt.setInt(1,objeto.getCdVistoriaPlanoItemDefeito());
			if(objeto.getCdDefeitoVistoriaItem()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDefeitoVistoriaItem());
			pstmt.setInt(3,objeto.getCdVistoriaPlanoItem());
			if(objeto.getCdPlanoVistoria()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdPlanoVistoria());
			if(objeto.getCdVistoriaItem()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdVistoriaItem());
			pstmt.setInt(6, cdVistoriaPlanoItemDefeitoOld!=0 ? cdVistoriaPlanoItemDefeitoOld : objeto.getCdVistoriaPlanoItemDefeito());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDefeitoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDefeitoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdVistoriaPlanoItemDefeito) {
		return delete(cdVistoriaPlanoItemDefeito, null);
	}

	public static int delete(int cdVistoriaPlanoItemDefeito, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_vistoria_plano_item_defeito WHERE cd_vistoria_plano_item_defeito=?");
			pstmt.setInt(1, cdVistoriaPlanoItemDefeito);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDefeitoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDefeitoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static VistoriaPlanoItemDefeito get(int cdVistoriaPlanoItemDefeito) {
		return get(cdVistoriaPlanoItemDefeito, null);
	}

	public static VistoriaPlanoItemDefeito get(int cdVistoriaPlanoItemDefeito, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_vistoria_plano_item_defeito WHERE cd_vistoria_plano_item_defeito=?");
			pstmt.setInt(1, cdVistoriaPlanoItemDefeito);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new VistoriaPlanoItemDefeito(rs.getInt("cd_vistoria_plano_item_defeito"),
						rs.getInt("cd_defeito_vistoria_item"),
						rs.getInt("cd_vistoria_plano_item"),
						rs.getInt("cd_plano_vistoria"),
						rs.getInt("cd_vistoria_item"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDefeitoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDefeitoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_vistoria_plano_item_defeito");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDefeitoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDefeitoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<VistoriaPlanoItemDefeito> getList() {
		return getList(null);
	}

	public static ArrayList<VistoriaPlanoItemDefeito> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<VistoriaPlanoItemDefeito> list = new ArrayList<VistoriaPlanoItemDefeito>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				VistoriaPlanoItemDefeito obj = VistoriaPlanoItemDefeitoDAO.get(rsm.getInt("cd_vistoria_plano_item_defeito"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDefeitoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_vistoria_plano_item_defeito", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
