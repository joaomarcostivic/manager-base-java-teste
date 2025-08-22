package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class UnidadeMedidaDAO{

	public static int insert(UnidadeMedida objeto) {
		return insert(objeto, null);
	}

	public static int insert(UnidadeMedida objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_unidade_medida", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdUnidadeMedida(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_unidade_medida (cd_unidade_medida,"+
			                                  "nm_unidade_medida,"+
			                                  "sg_unidade_medida,"+
			                                  "txt_unidade_medida,"+
			                                  "nr_precisao_medida,"+
			                                  "lg_ativo) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmUnidadeMedida());
			pstmt.setString(3,objeto.getSgUnidadeMedida());
			pstmt.setString(4,objeto.getTxtUnidadeMedida());
			pstmt.setInt(5,objeto.getNrPrecisaoMedida());
			pstmt.setInt(6,objeto.getLgAtivo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeMedidaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeMedidaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(UnidadeMedida objeto) {
		return update(objeto, 0, null);
	}

	public static int update(UnidadeMedida objeto, int cdUnidadeMedidaOld) {
		return update(objeto, cdUnidadeMedidaOld, null);
	}

	public static int update(UnidadeMedida objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(UnidadeMedida objeto, int cdUnidadeMedidaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_unidade_medida SET cd_unidade_medida=?,"+
												      		   "nm_unidade_medida=?,"+
												      		   "sg_unidade_medida=?,"+
												      		   "txt_unidade_medida=?,"+
												      		   "nr_precisao_medida=?,"+
												      		   "lg_ativo=? WHERE cd_unidade_medida=?");
			pstmt.setInt(1,objeto.getCdUnidadeMedida());
			pstmt.setString(2,objeto.getNmUnidadeMedida());
			pstmt.setString(3,objeto.getSgUnidadeMedida());
			pstmt.setString(4,objeto.getTxtUnidadeMedida());
			pstmt.setInt(5,objeto.getNrPrecisaoMedida());
			pstmt.setInt(6,objeto.getLgAtivo());
			pstmt.setInt(7, cdUnidadeMedidaOld!=0 ? cdUnidadeMedidaOld : objeto.getCdUnidadeMedida());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeMedidaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeMedidaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdUnidadeMedida) {
		return delete(cdUnidadeMedida, null);
	}

	public static int delete(int cdUnidadeMedida, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_unidade_medida WHERE cd_unidade_medida=?");
			pstmt.setInt(1, cdUnidadeMedida);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeMedidaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeMedidaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static UnidadeMedida get(int cdUnidadeMedida) {
		return get(cdUnidadeMedida, null);
	}

	public static UnidadeMedida get(int cdUnidadeMedida, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_unidade_medida WHERE cd_unidade_medida=?");
			pstmt.setInt(1, cdUnidadeMedida);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new UnidadeMedida(rs.getInt("cd_unidade_medida"),
						rs.getString("nm_unidade_medida"),
						rs.getString("sg_unidade_medida"),
						rs.getString("txt_unidade_medida"),
						rs.getInt("nr_precisao_medida"),
						rs.getInt("lg_ativo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeMedidaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeMedidaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_unidade_medida");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeMedidaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeMedidaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_unidade_medida", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
