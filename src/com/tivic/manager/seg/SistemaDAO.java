package com.tivic.manager.seg;

import java.sql.*;
import sol.dao.*;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class SistemaDAO{

	public static int insert(Sistema objeto) {
		return insert(objeto, null);
	}

	public static int insert(Sistema objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("seg_sistema", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if(objeto.getCdSistema()<=0)
				objeto.setCdSistema(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO seg_sistema (cd_sistema,"+
			                                  "nm_sistema,"+
			                                  "id_sistema,"+
			                                  "lg_ativo) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdSistema());
			pstmt.setString(2,objeto.getNmSistema());
			pstmt.setString(3,objeto.getIdSistema());
			pstmt.setInt(4,objeto.getLgAtivo());
			pstmt.executeUpdate();
			return objeto.getCdSistema();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SistemaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Sistema objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Sistema objeto, int cdSistemaOld) {
		return update(objeto, cdSistemaOld, null);
	}

	public static int update(Sistema objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Sistema objeto, int cdSistemaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE seg_sistema SET cd_sistema=?,"+
												      		   "nm_sistema=?,"+
												      		   "id_sistema=?,"+
												      		   "lg_ativo=? WHERE cd_sistema=?");
			pstmt.setInt(1,objeto.getCdSistema());
			pstmt.setString(2,objeto.getNmSistema());
			pstmt.setString(3,objeto.getIdSistema());
			pstmt.setInt(4,objeto.getLgAtivo());
			pstmt.setInt(5, cdSistemaOld!=0 ? cdSistemaOld : objeto.getCdSistema());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SistemaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SistemaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdSistema) {
		return delete(cdSistema, null);
	}

	public static int delete(int cdSistema, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM seg_sistema WHERE cd_sistema=?");
			pstmt.setInt(1, cdSistema);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SistemaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SistemaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Sistema get(int cdSistema) {
		return get(cdSistema, null);
	}

	public static Sistema get(int cdSistema, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_sistema WHERE cd_sistema=?");
			pstmt.setInt(1, cdSistema);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Sistema(rs.getInt("cd_sistema"),
						rs.getString("nm_sistema"),
						rs.getString("id_sistema"),
						rs.getInt("lg_ativo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SistemaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SistemaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_sistema");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SistemaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SistemaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM seg_sistema", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
