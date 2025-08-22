package com.tivic.manager.sinc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TabelaDependenciaDAO{

	public static int insert(TabelaDependencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(TabelaDependencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO sinc_tabela_dependencia (cd_dependente,"+
			                                  "cd_provedor," + 
			                                  "nm_chaves) VALUES (?, ?, ?)");
			if(objeto.getCdDependente()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdDependente());
			if(objeto.getCdProvedor()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProvedor());
			pstmt.setString(3, objeto.getNmChaves());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaDependenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaDependenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TabelaDependencia objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TabelaDependencia objeto, int cdDependenteOld, int cdProvedorOld) {
		return update(objeto, cdDependenteOld, cdProvedorOld, null);
	}

	public static int update(TabelaDependencia objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TabelaDependencia objeto, int cdDependenteOld, int cdProvedorOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE sinc_tabela_dependencia SET cd_dependente=?,"+
												      		   "cd_provedor=?," + 
												      		   "nm_chaves=? WHERE cd_dependente=? AND cd_provedor=?");
			pstmt.setInt(1,objeto.getCdDependente());
			pstmt.setInt(2,objeto.getCdProvedor());
			pstmt.setString(3, objeto.getNmChaves());
			pstmt.setInt(4, cdDependenteOld!=0 ? cdDependenteOld : objeto.getCdDependente());
			pstmt.setInt(5, cdProvedorOld!=0 ? cdProvedorOld : objeto.getCdProvedor());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaDependenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaDependenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDependente, int cdProvedor) {
		return delete(cdDependente, cdProvedor, null);
	}

	public static int delete(int cdDependente, int cdProvedor, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM sinc_tabela_dependencia WHERE cd_dependente=? AND cd_provedor=?");
			pstmt.setInt(1, cdDependente);
			pstmt.setInt(2, cdProvedor);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaDependenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaDependenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TabelaDependencia get(int cdDependente, int cdProvedor) {
		return get(cdDependente, cdProvedor, null);
	}

	public static TabelaDependencia get(int cdDependente, int cdProvedor, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM sinc_tabela_dependencia WHERE cd_dependente=? AND cd_provedor=?");
			pstmt.setInt(1, cdDependente);
			pstmt.setInt(2, cdProvedor);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TabelaDependencia(rs.getInt("cd_dependente"),
						rs.getInt("cd_provedor"),
						rs.getString("nm_chaves"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaDependenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaDependenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM sinc_tabela_dependencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaDependenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaDependenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TabelaDependencia> getList() {
		return getList(null);
	}

	public static ArrayList<TabelaDependencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TabelaDependencia> list = new ArrayList<TabelaDependencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TabelaDependencia obj = TabelaDependenciaDAO.get(rsm.getInt("cd_dependente"), rsm.getInt("cd_provedor"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaDependenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM sinc_tabela_dependencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
