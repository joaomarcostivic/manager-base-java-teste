package com.tivic.manager.agd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoSituacaoDAO{

	public static int insert(TipoSituacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoSituacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("agd_tipo_situacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoSituacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_tipo_situacao (cd_tipo_situacao,"+
			                                  "nm_tipo_situacao,"+
			                                  "id_tipo_situacao,"+
			                                  "nr_ordem) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoSituacao());
			pstmt.setString(3,objeto.getIdTipoSituacao());
			pstmt.setInt(4,objeto.getNrOrdem());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoSituacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoSituacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoSituacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoSituacao objeto, int cdTipoSituacaoOld) {
		return update(objeto, cdTipoSituacaoOld, null);
	}

	public static int update(TipoSituacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoSituacao objeto, int cdTipoSituacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agd_tipo_situacao SET cd_tipo_situacao=?,"+
												      		   "nm_tipo_situacao=?,"+
												      		   "id_tipo_situacao=?,"+
												      		   "nr_ordem=? WHERE cd_tipo_situacao=?");
			pstmt.setInt(1,objeto.getCdTipoSituacao());
			pstmt.setString(2,objeto.getNmTipoSituacao());
			pstmt.setString(3,objeto.getIdTipoSituacao());
			pstmt.setInt(4,objeto.getNrOrdem());
			pstmt.setInt(5, cdTipoSituacaoOld!=0 ? cdTipoSituacaoOld : objeto.getCdTipoSituacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoSituacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoSituacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoSituacao) {
		return delete(cdTipoSituacao, null);
	}

	public static int delete(int cdTipoSituacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_tipo_situacao WHERE cd_tipo_situacao=?");
			pstmt.setInt(1, cdTipoSituacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoSituacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoSituacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoSituacao get(int cdTipoSituacao) {
		return get(cdTipoSituacao, null);
	}

	public static TipoSituacao get(int cdTipoSituacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_tipo_situacao WHERE cd_tipo_situacao=?");
			pstmt.setInt(1, cdTipoSituacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoSituacao(rs.getInt("cd_tipo_situacao"),
						rs.getString("nm_tipo_situacao"),
						rs.getString("id_tipo_situacao"),
						rs.getInt("nr_ordem"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoSituacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoSituacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_tipo_situacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoSituacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoSituacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoSituacao> getList() {
		return getList(null);
	}

	public static ArrayList<TipoSituacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoSituacao> list = new ArrayList<TipoSituacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoSituacao obj = TipoSituacaoDAO.get(rsm.getInt("cd_tipo_situacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoSituacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM agd_tipo_situacao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
