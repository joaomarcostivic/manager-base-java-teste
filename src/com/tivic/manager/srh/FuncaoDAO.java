package com.tivic.manager.srh;

import java.sql.*;
import sol.dao.*;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class FuncaoDAO{

	public static int insert(Funcao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Funcao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("srh_funcao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFuncao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO srh_funcao (cd_funcao,"+
			                                  "cd_empresa,"+
			                                  "nm_funcao,"+
			                                  "id_funcao) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setString(3,objeto.getNmFuncao());
			pstmt.setString(4,objeto.getIdFuncao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FuncaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FuncaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Funcao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Funcao objeto, int cdFuncaoOld) {
		return update(objeto, cdFuncaoOld, null);
	}

	public static int update(Funcao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Funcao objeto, int cdFuncaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE srh_funcao SET cd_funcao=?,"+
												      		   "cd_empresa=?,"+
												      		   "nm_funcao=?,"+
												      		   "id_funcao=? WHERE cd_funcao=?");
			pstmt.setInt(1,objeto.getCdFuncao());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setString(3,objeto.getNmFuncao());
			pstmt.setString(4,objeto.getIdFuncao());
			pstmt.setInt(5, cdFuncaoOld!=0 ? cdFuncaoOld : objeto.getCdFuncao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FuncaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FuncaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFuncao) {
		return delete(cdFuncao, null);
	}

	public static int delete(int cdFuncao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM srh_funcao WHERE cd_funcao=?");
			pstmt.setInt(1, cdFuncao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FuncaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FuncaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Funcao get(int cdFuncao) {
		return get(cdFuncao, null);
	}

	public static Funcao get(int cdFuncao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM srh_funcao WHERE cd_funcao=?");
			pstmt.setInt(1, cdFuncao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Funcao(rs.getInt("cd_funcao"),
						rs.getInt("cd_empresa"),
						rs.getString("nm_funcao"),
						rs.getString("id_funcao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FuncaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FuncaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_funcao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FuncaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FuncaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM srh_funcao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
