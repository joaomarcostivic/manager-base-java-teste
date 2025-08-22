package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class VinculoDAO{

	public static int insert(Vinculo objeto) {
		return insert(objeto, null);
	}

	public static int insert(Vinculo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_vinculo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdVinculo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_vinculo (cd_vinculo,"+
			                                  "nm_vinculo,"+
			                                  "lg_estatico,"+
			                                  "lg_funcao,"+
			                                  "cd_formulario,"+
			                                  "lg_cadastro) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmVinculo());
			pstmt.setInt(3,objeto.getLgEstatico());
			pstmt.setInt(4,objeto.getLgFuncao());
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdFormulario());
			pstmt.setInt(6,objeto.getLgCadastro());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VinculoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VinculoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Vinculo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Vinculo objeto, int cdVinculoOld) {
		return update(objeto, cdVinculoOld, null);
	}

	public static int update(Vinculo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Vinculo objeto, int cdVinculoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_vinculo SET cd_vinculo=?,"+
												      		   "nm_vinculo=?,"+
												      		   "lg_estatico=?,"+
												      		   "lg_funcao=?,"+
												      		   "cd_formulario=?,"+
												      		   "lg_cadastro=? WHERE cd_vinculo=?");
			pstmt.setInt(1,objeto.getCdVinculo());
			pstmt.setString(2,objeto.getNmVinculo());
			pstmt.setInt(3,objeto.getLgEstatico());
			pstmt.setInt(4,objeto.getLgFuncao());
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdFormulario());
			pstmt.setInt(6,objeto.getLgCadastro());
			pstmt.setInt(7, cdVinculoOld!=0 ? cdVinculoOld : objeto.getCdVinculo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VinculoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VinculoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdVinculo) {
		return delete(cdVinculo, null);
	}

	public static int delete(int cdVinculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_vinculo WHERE cd_vinculo=?");
			pstmt.setInt(1, cdVinculo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VinculoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VinculoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Vinculo get(int cdVinculo) {
		return get(cdVinculo, null);
	}

	public static Vinculo get(int cdVinculo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_vinculo WHERE cd_vinculo=?");
			pstmt.setInt(1, cdVinculo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Vinculo(rs.getInt("cd_vinculo"),
						rs.getString("nm_vinculo"),
						rs.getInt("lg_estatico"),
						rs.getInt("lg_funcao"),
						rs.getInt("cd_formulario"),
						rs.getInt("lg_cadastro"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VinculoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VinculoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_vinculo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VinculoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VinculoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_vinculo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
