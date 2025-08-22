package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class LogradouroDAO{

	public static int insert(Logradouro objeto) {
		return insert(objeto, null);
	}

	public static int insert(Logradouro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int code = Conexao.getSequenceCode("grl_logradouro", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO grl_logradouro (cd_logradouro,"+
			                                  "cd_distrito,"+
			                                  "cd_cidade,"+
			                                  "cd_tipo_logradouro,"+
			                                  "nm_logradouro,"+
			                                  "id_logradouro,"+
			                                  "cd_regiao) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdDistrito()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDistrito());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCidade());
			if(objeto.getCdTipoLogradouro()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTipoLogradouro());
			pstmt.setString(5,objeto.getNmLogradouro());
			pstmt.setString(6,objeto.getIdLogradouro());
			if(objeto.getCdRegiao()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7, objeto.getCdRegiao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LogradouroDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LogradouroDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Logradouro objeto) {
		return update(objeto, null);
	}

	public static int update(Logradouro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE grl_logradouro SET cd_distrito=?,"+
			                                  "cd_cidade=?,"+
			                                  "cd_tipo_logradouro=?,"+
			                                  "nm_logradouro=?,"+
			                                  "id_logradouro=?,"+
			                                  "cd_regiao=? WHERE cd_logradouro=?");
			if(objeto.getCdDistrito()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdDistrito());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCidade());
			if(objeto.getCdTipoLogradouro()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoLogradouro());
			pstmt.setString(4,objeto.getNmLogradouro());
			pstmt.setString(5,objeto.getIdLogradouro());			
			if(objeto.getCdRegiao()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6, objeto.getCdRegiao());
			pstmt.setInt(7,objeto.getCdLogradouro());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LogradouroDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LogradouroDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLogradouro) {
		return delete(cdLogradouro, null);
	}

	public static int delete(int cdLogradouro, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM grl_logradouro WHERE cd_logradouro=?");
			pstmt.setInt(1, cdLogradouro);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LogradouroDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LogradouroDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Logradouro get(int cdLogradouro) {
		return get(cdLogradouro, null);
	}

	public static Logradouro get(int cdLogradouro, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_logradouro WHERE cd_logradouro=?");
			pstmt.setInt(1, cdLogradouro);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Logradouro(rs.getInt("cd_logradouro"),
						rs.getInt("cd_distrito"),
						rs.getInt("cd_cidade"),
						rs.getInt("cd_tipo_logradouro"),
						rs.getString("nm_logradouro"),
						rs.getString("id_logradouro"),
						rs.getInt("cd_regiao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LogradouroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LogradouroDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_logradouro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LogradouroDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LogradouroDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator>  criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator>  criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_logradouro", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
