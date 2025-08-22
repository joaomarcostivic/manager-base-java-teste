package com.tivic.manager.srh;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoAdmissaoDAO{

	public static int insert(TipoAdmissao objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoAdmissao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("srh_tipo_admissao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoAdmissao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO srh_tipo_admissao (cd_tipo_admissao,"+
			                                  "nm_tipo_admissao,"+
			                                  "id_tipo_admissao,"+
			                                  "cd_tipo_admissao_superior,"+
			                                  "st_tipo_admissao) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoAdmissao());
			pstmt.setString(3,objeto.getIdTipoAdmissao());
			if(objeto.getCdTipoAdmissaoSuperior()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTipoAdmissaoSuperior());
			pstmt.setInt(5,objeto.getStTipoAdmissao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAdmissaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAdmissaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoAdmissao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoAdmissao objeto, int cdTipoAdmissaoOld) {
		return update(objeto, cdTipoAdmissaoOld, null);
	}

	public static int update(TipoAdmissao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoAdmissao objeto, int cdTipoAdmissaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE srh_tipo_admissao SET cd_tipo_admissao=?,"+
												      		   "nm_tipo_admissao=?,"+
												      		   "id_tipo_admissao=?,"+
												      		   "cd_tipo_admissao_superior=?,"+
												      		   "st_tipo_admissao=? WHERE cd_tipo_admissao=?");
			pstmt.setInt(1,objeto.getCdTipoAdmissao());
			pstmt.setString(2,objeto.getNmTipoAdmissao());
			pstmt.setString(3,objeto.getIdTipoAdmissao());
			if(objeto.getCdTipoAdmissaoSuperior()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTipoAdmissaoSuperior());
			pstmt.setInt(5,objeto.getStTipoAdmissao());
			pstmt.setInt(6, cdTipoAdmissaoOld!=0 ? cdTipoAdmissaoOld : objeto.getCdTipoAdmissao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAdmissaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAdmissaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoAdmissao) {
		return delete(cdTipoAdmissao, null);
	}

	public static int delete(int cdTipoAdmissao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM srh_tipo_admissao WHERE cd_tipo_admissao=?");
			pstmt.setInt(1, cdTipoAdmissao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAdmissaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAdmissaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoAdmissao get(int cdTipoAdmissao) {
		return get(cdTipoAdmissao, null);
	}

	public static TipoAdmissao get(int cdTipoAdmissao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM srh_tipo_admissao WHERE cd_tipo_admissao=?");
			pstmt.setInt(1, cdTipoAdmissao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoAdmissao(rs.getInt("cd_tipo_admissao"),
						rs.getString("nm_tipo_admissao"),
						rs.getString("id_tipo_admissao"),
						rs.getInt("cd_tipo_admissao_superior"),
						rs.getInt("st_tipo_admissao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAdmissaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAdmissaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_tipo_admissao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAdmissaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAdmissaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM srh_tipo_admissao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
