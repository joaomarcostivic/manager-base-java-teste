package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoAvaliacaoDAO{

	public static int insert(TipoAvaliacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoAvaliacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_tipo_avaliacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoAvaliacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_tipo_avaliacao (cd_tipo_avaliacao,"+
			                                  "nm_tipo_avaliacao,"+
			                                  "id_tipo_avaliacao) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoAvaliacao());
			pstmt.setString(3,objeto.getIdTipoAvaliacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAvaliacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAvaliacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoAvaliacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoAvaliacao objeto, int cdTipoAvaliacaoOld) {
		return update(objeto, cdTipoAvaliacaoOld, null);
	}

	public static int update(TipoAvaliacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoAvaliacao objeto, int cdTipoAvaliacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_tipo_avaliacao SET cd_tipo_avaliacao=?,"+
												      		   "nm_tipo_avaliacao=?,"+
												      		   "id_tipo_avaliacao=? WHERE cd_tipo_avaliacao=?");
			pstmt.setInt(1,objeto.getCdTipoAvaliacao());
			pstmt.setString(2,objeto.getNmTipoAvaliacao());
			pstmt.setString(3,objeto.getIdTipoAvaliacao());
			pstmt.setInt(4, cdTipoAvaliacaoOld!=0 ? cdTipoAvaliacaoOld : objeto.getCdTipoAvaliacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAvaliacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAvaliacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoAvaliacao) {
		return delete(cdTipoAvaliacao, null);
	}

	public static int delete(int cdTipoAvaliacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_tipo_avaliacao WHERE cd_tipo_avaliacao=?");
			pstmt.setInt(1, cdTipoAvaliacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAvaliacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAvaliacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoAvaliacao get(int cdTipoAvaliacao) {
		return get(cdTipoAvaliacao, null);
	}

	public static TipoAvaliacao get(int cdTipoAvaliacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_avaliacao WHERE cd_tipo_avaliacao=?");
			pstmt.setInt(1, cdTipoAvaliacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoAvaliacao(rs.getInt("cd_tipo_avaliacao"),
						rs.getString("nm_tipo_avaliacao"),
						rs.getString("id_tipo_avaliacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAvaliacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAvaliacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_avaliacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAvaliacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAvaliacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoAvaliacao> getList() {
		return getList(null);
	}

	public static ArrayList<TipoAvaliacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoAvaliacao> list = new ArrayList<TipoAvaliacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoAvaliacao obj = TipoAvaliacaoDAO.get(rsm.getInt("cd_tipo_avaliacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAvaliacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_avaliacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
