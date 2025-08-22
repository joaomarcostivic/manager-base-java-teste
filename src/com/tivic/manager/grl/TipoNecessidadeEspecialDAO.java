package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoNecessidadeEspecialDAO{

	public static int insert(TipoNecessidadeEspecial objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoNecessidadeEspecial objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_tipo_necessidade_especial", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoNecessidadeEspecial(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_tipo_necessidade_especial (cd_tipo_necessidade_especial,"+
			                                  "nm_tipo_necessidade_especial,"+
			                                  "id_tipo_necessidade_especial,"+
			                                  "tp_necessidade_especial) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoNecessidadeEspecial());
			pstmt.setString(3,objeto.getIdTipoNecessidadeEspecial());
			pstmt.setInt(4,objeto.getTpNecessidadeEspecial());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoNecessidadeEspecialDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoNecessidadeEspecialDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoNecessidadeEspecial objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoNecessidadeEspecial objeto, int cdTipoNecessidadeEspecialOld) {
		return update(objeto, cdTipoNecessidadeEspecialOld, null);
	}

	public static int update(TipoNecessidadeEspecial objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoNecessidadeEspecial objeto, int cdTipoNecessidadeEspecialOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_tipo_necessidade_especial SET cd_tipo_necessidade_especial=?,"+
												      		   "nm_tipo_necessidade_especial=?,"+
												      		   "id_tipo_necessidade_especial=?,"+
												      		   "tp_necessidade_especial=? WHERE cd_tipo_necessidade_especial=?");
			pstmt.setInt(1,objeto.getCdTipoNecessidadeEspecial());
			pstmt.setString(2,objeto.getNmTipoNecessidadeEspecial());
			pstmt.setString(3,objeto.getIdTipoNecessidadeEspecial());
			pstmt.setInt(4,objeto.getTpNecessidadeEspecial());
			pstmt.setInt(5, cdTipoNecessidadeEspecialOld!=0 ? cdTipoNecessidadeEspecialOld : objeto.getCdTipoNecessidadeEspecial());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoNecessidadeEspecialDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoNecessidadeEspecialDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoNecessidadeEspecial) {
		return delete(cdTipoNecessidadeEspecial, null);
	}

	public static int delete(int cdTipoNecessidadeEspecial, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_tipo_necessidade_especial WHERE cd_tipo_necessidade_especial=?");
			pstmt.setInt(1, cdTipoNecessidadeEspecial);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoNecessidadeEspecialDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoNecessidadeEspecialDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoNecessidadeEspecial get(int cdTipoNecessidadeEspecial) {
		return get(cdTipoNecessidadeEspecial, null);
	}

	public static TipoNecessidadeEspecial get(int cdTipoNecessidadeEspecial, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_necessidade_especial WHERE cd_tipo_necessidade_especial=?");
			pstmt.setInt(1, cdTipoNecessidadeEspecial);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoNecessidadeEspecial(rs.getInt("cd_tipo_necessidade_especial"),
						rs.getString("nm_tipo_necessidade_especial"),
						rs.getString("id_tipo_necessidade_especial"),
						rs.getInt("tp_necessidade_especial"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoNecessidadeEspecialDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoNecessidadeEspecialDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_necessidade_especial");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoNecessidadeEspecialDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoNecessidadeEspecialDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoNecessidadeEspecial> getList() {
		return getList(null);
	}

	public static ArrayList<TipoNecessidadeEspecial> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoNecessidadeEspecial> list = new ArrayList<TipoNecessidadeEspecial>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoNecessidadeEspecial obj = TipoNecessidadeEspecialDAO.get(rsm.getInt("cd_tipo_necessidade_especial"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoNecessidadeEspecialDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_tipo_necessidade_especial", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}