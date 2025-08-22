package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class VistoriaPlanoItemArquivoDAO{

	public static int insert(VistoriaPlanoItemArquivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(VistoriaPlanoItemArquivo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_vistoria_plano_item_arquivo (cd_vistoria_plano_item,"+
			                                  "cd_arquivo) VALUES (?, ?)");
			if(objeto.getCdVistoriaPlanoItem()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdVistoriaPlanoItem());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdArquivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemArquivoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemArquivoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(VistoriaPlanoItemArquivo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(VistoriaPlanoItemArquivo objeto, int cdVistoriaPlanoItemOld, int cdArquivoOld) {
		return update(objeto, cdVistoriaPlanoItemOld, cdArquivoOld, null);
	}

	public static int update(VistoriaPlanoItemArquivo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(VistoriaPlanoItemArquivo objeto, int cdVistoriaPlanoItemOld, int cdArquivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_vistoria_plano_item_arquivo SET cd_vistoria_plano_item=?,"+
												      		   "cd_arquivo=? WHERE cd_vistoria_plano_item=? AND cd_arquivo=?");
			pstmt.setInt(1,objeto.getCdVistoriaPlanoItem());
			pstmt.setInt(2,objeto.getCdArquivo());
			pstmt.setInt(3, cdVistoriaPlanoItemOld!=0 ? cdVistoriaPlanoItemOld : objeto.getCdVistoriaPlanoItem());
			pstmt.setInt(4, cdArquivoOld!=0 ? cdArquivoOld : objeto.getCdArquivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemArquivoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemArquivoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdVistoriaPlanoItem, int cdArquivo) {
		return delete(cdVistoriaPlanoItem, cdArquivo, null);
	}

	public static int delete(int cdVistoriaPlanoItem, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_vistoria_plano_item_arquivo WHERE cd_vistoria_plano_item=? AND cd_arquivo=?");
			pstmt.setInt(1, cdVistoriaPlanoItem);
			pstmt.setInt(2, cdArquivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemArquivoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemArquivoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static VistoriaPlanoItemArquivo get(int cdVistoriaPlanoItem, int cdArquivo) {
		return get(cdVistoriaPlanoItem, cdArquivo, null);
	}

	public static VistoriaPlanoItemArquivo get(int cdVistoriaPlanoItem, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_vistoria_plano_item_arquivo WHERE cd_vistoria_plano_item=? AND cd_arquivo=?");
			pstmt.setInt(1, cdVistoriaPlanoItem);
			pstmt.setInt(2, cdArquivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new VistoriaPlanoItemArquivo(rs.getInt("cd_vistoria_plano_item"),
						rs.getInt("cd_arquivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemArquivoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemArquivoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_vistoria_plano_item_arquivo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemArquivoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemArquivoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<VistoriaPlanoItemArquivo> getList() {
		return getList(null);
	}

	public static ArrayList<VistoriaPlanoItemArquivo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<VistoriaPlanoItemArquivo> list = new ArrayList<VistoriaPlanoItemArquivo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				VistoriaPlanoItemArquivo obj = VistoriaPlanoItemArquivoDAO.get(rsm.getInt("cd_vistoria_plano_item"), rsm.getInt("cd_arquivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemArquivoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_vistoria_plano_item_arquivo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
