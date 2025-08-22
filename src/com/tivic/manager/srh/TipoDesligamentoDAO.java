package com.tivic.manager.srh;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoDesligamentoDAO{

	public static int insert(TipoDesligamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoDesligamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("srh_tipo_desligamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoDesligamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO srh_tipo_desligamento (cd_tipo_desligamento,"+
			                                  "nm_tipo_desligamento,"+
			                                  "id_tipo_desligamento,"+
			                                  "tp_aviso_previo,"+
			                                  "nr_sefip) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoDesligamento());
			pstmt.setString(3,objeto.getIdTipoDesligamento());
			pstmt.setInt(4,objeto.getTpAvisoPrevio());
			pstmt.setString(5,objeto.getNrSefip());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoDesligamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoDesligamento objeto, int cdTipoDesligamentoOld) {
		return update(objeto, cdTipoDesligamentoOld, null);
	}

	public static int update(TipoDesligamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoDesligamento objeto, int cdTipoDesligamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE srh_tipo_desligamento SET cd_tipo_desligamento=?,"+
												      		   "nm_tipo_desligamento=?,"+
												      		   "id_tipo_desligamento=?,"+
												      		   "tp_aviso_previo=?,"+
												      		   "nr_sefip=? WHERE cd_tipo_desligamento=?");
			pstmt.setInt(1,objeto.getCdTipoDesligamento());
			pstmt.setString(2,objeto.getNmTipoDesligamento());
			pstmt.setString(3,objeto.getIdTipoDesligamento());
			pstmt.setInt(4,objeto.getTpAvisoPrevio());
			pstmt.setString(5,objeto.getNrSefip());
			pstmt.setInt(6, cdTipoDesligamentoOld!=0 ? cdTipoDesligamentoOld : objeto.getCdTipoDesligamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoDesligamento) {
		return delete(cdTipoDesligamento, null);
	}

	public static int delete(int cdTipoDesligamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM srh_tipo_desligamento WHERE cd_tipo_desligamento=?");
			pstmt.setInt(1, cdTipoDesligamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoDesligamento get(int cdTipoDesligamento) {
		return get(cdTipoDesligamento, null);
	}

	public static TipoDesligamento get(int cdTipoDesligamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM srh_tipo_desligamento WHERE cd_tipo_desligamento=?");
			pstmt.setInt(1, cdTipoDesligamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoDesligamento(rs.getInt("cd_tipo_desligamento"),
						rs.getString("nm_tipo_desligamento"),
						rs.getString("id_tipo_desligamento"),
						rs.getInt("tp_aviso_previo"),
						rs.getString("nr_sefip"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_tipo_desligamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM srh_tipo_desligamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
