package com.tivic.manager.prc;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ObjetoAcaoDAO{

	public static int insert(ObjetoAcao objeto) {
		return insert(objeto, null);
	}

	public static int insert(ObjetoAcao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("prc_tipo_objeto", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if(objeto.getCdObjetoAcao()<=0)
				objeto.setCdObjetoAcao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_tipo_objeto (cd_tipo_objeto,"+
			                                  "nm_tipo_objeto,"+
			                                  "id_tipo_objeto) VALUES (?, ?, ?)");
			pstmt.setInt(1, objeto.getCdObjetoAcao());
			pstmt.setString(2,objeto.getNmObjetoAcao());
			pstmt.setString(3,objeto.getIdObjetoAcao());
			pstmt.executeUpdate();
			return objeto.getCdObjetoAcao();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ObjetoAcaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ObjetoAcao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ObjetoAcao objeto, int cdObjetoAcaoOld) {
		return update(objeto, cdObjetoAcaoOld, null);
	}

	public static int update(ObjetoAcao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ObjetoAcao objeto, int cdObjetoAcaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_objeto_acao SET cd_objeto_acao=?,"+
												      		   "nm_objeto_acao=?,"+
												      		   "id_objeto_acao=? WHERE cd_objeto_acao=?");
			pstmt.setInt(1,objeto.getCdObjetoAcao());
			pstmt.setString(2,objeto.getNmObjetoAcao());
			pstmt.setString(3,objeto.getIdObjetoAcao());
			pstmt.setInt(4, cdObjetoAcaoOld!=0 ? cdObjetoAcaoOld : objeto.getCdObjetoAcao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ObjetoAcaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ObjetoAcaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdObjetoAcao) {
		return delete(cdObjetoAcao, null);
	}

	public static int delete(int cdObjetoAcao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_objeto_acao WHERE cd_objeto_acao=?");
			pstmt.setInt(1, cdObjetoAcao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ObjetoAcaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ObjetoAcaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ObjetoAcao get(int cdObjetoAcao) {
		return get(cdObjetoAcao, null);
	}

	public static ObjetoAcao get(int cdObjetoAcao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_objeto_acao WHERE cd_objeto_acao=?");
			pstmt.setInt(1, cdObjetoAcao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ObjetoAcao(rs.getInt("cd_objeto_acao"),
						rs.getString("nm_objeto_acao"),
						rs.getString("id_objeto_acao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ObjetoAcaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ObjetoAcaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_objeto_acao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ObjetoAcaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ObjetoAcaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM prc_objeto_acao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
