package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoMovimentacaoDAO{

	public static int insert(TipoMovimentacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoMovimentacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fta_tipo_movimentacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoMovimentacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_tipo_movimentacao (cd_tipo_movimentacao,"+
			                                  "nm_tipo_movimentacao,"+
			                                  "tp_situacao_pneu) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoMovimentacao());
			pstmt.setInt(3,objeto.getTpSituacaoPneu());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoMovimentacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoMovimentacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoMovimentacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoMovimentacao objeto, int cdTipoMovimentacaoOld) {
		return update(objeto, cdTipoMovimentacaoOld, null);
	}

	public static int update(TipoMovimentacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoMovimentacao objeto, int cdTipoMovimentacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_tipo_movimentacao SET cd_tipo_movimentacao=?,"+
												      		   "nm_tipo_movimentacao=?,"+
												      		   "tp_situacao_pneu=? WHERE cd_tipo_movimentacao=?");
			pstmt.setInt(1,objeto.getCdTipoMovimentacao());
			pstmt.setString(2,objeto.getNmTipoMovimentacao());
			pstmt.setInt(3,objeto.getTpSituacaoPneu());
			pstmt.setInt(4, cdTipoMovimentacaoOld!=0 ? cdTipoMovimentacaoOld : objeto.getCdTipoMovimentacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoMovimentacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoMovimentacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoMovimentacao) {
		return delete(cdTipoMovimentacao, null);
	}

	public static int delete(int cdTipoMovimentacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_tipo_movimentacao WHERE cd_tipo_movimentacao=?");
			pstmt.setInt(1, cdTipoMovimentacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoMovimentacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoMovimentacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoMovimentacao get(int cdTipoMovimentacao) {
		return get(cdTipoMovimentacao, null);
	}

	public static TipoMovimentacao get(int cdTipoMovimentacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_tipo_movimentacao WHERE cd_tipo_movimentacao=?");
			pstmt.setInt(1, cdTipoMovimentacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoMovimentacao(rs.getInt("cd_tipo_movimentacao"),
						rs.getString("nm_tipo_movimentacao"),
						rs.getInt("tp_situacao_pneu"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoMovimentacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoMovimentacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_tipo_movimentacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoMovimentacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoMovimentacaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_tipo_movimentacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
