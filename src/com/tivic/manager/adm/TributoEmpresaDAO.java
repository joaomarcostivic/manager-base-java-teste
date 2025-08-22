package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TributoEmpresaDAO{

	public static int insert(TributoEmpresa objeto) {
		return insert(objeto, null);
	}

	public static int insert(TributoEmpresa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_tributo_empresa (cd_tributo,"+
			                                  "cd_empresa,"+
			                                  "cd_agente_tributador) VALUES (?, ?, ?)");
			if(objeto.getCdTributo()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTributo());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdAgenteTributador()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAgenteTributador());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TributoEmpresaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TributoEmpresaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TributoEmpresa objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TributoEmpresa objeto, int cdTributoOld, int cdEmpresaOld) {
		return update(objeto, cdTributoOld, cdEmpresaOld, null);
	}

	public static int update(TributoEmpresa objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TributoEmpresa objeto, int cdTributoOld, int cdEmpresaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_tributo_empresa SET cd_tributo=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_agente_tributador=? WHERE cd_tributo=? AND cd_empresa=?");
			pstmt.setInt(1,objeto.getCdTributo());
			pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdAgenteTributador()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAgenteTributador());
			pstmt.setInt(4, cdTributoOld!=0 ? cdTributoOld : objeto.getCdTributo());
			pstmt.setInt(5, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TributoEmpresaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TributoEmpresaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTributo, int cdEmpresa) {
		return delete(cdTributo, cdEmpresa, null);
	}

	public static int delete(int cdTributo, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_tributo_empresa WHERE cd_tributo=? AND cd_empresa=?");
			pstmt.setInt(1, cdTributo);
			pstmt.setInt(2, cdEmpresa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TributoEmpresaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TributoEmpresaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TributoEmpresa get(int cdTributo, int cdEmpresa) {
		return get(cdTributo, cdEmpresa, null);
	}

	public static TributoEmpresa get(int cdTributo, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_tributo_empresa WHERE cd_tributo=? AND cd_empresa=?");
			pstmt.setInt(1, cdTributo);
			pstmt.setInt(2, cdEmpresa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TributoEmpresa(rs.getInt("cd_tributo"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_agente_tributador"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TributoEmpresaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TributoEmpresaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_tributo_empresa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TributoEmpresaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TributoEmpresaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_tributo_empresa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
