package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.ItemComparator;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoOperacaoDAO{

	public static int insert(TipoOperacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoOperacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_tipo_operacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoOperacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_tipo_operacao (cd_tipo_operacao,"+
			                                  "nm_tipo_operacao,"+
			                                  "id_tipo_operacao,"+
			                                  "st_tipo_operacao,"+
			                                  "lg_contrato,"+
			                                  "cd_tabela_preco) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoOperacao());
			pstmt.setString(3,objeto.getIdTipoOperacao());
			pstmt.setInt(4,objeto.getStTipoOperacao());
			pstmt.setInt(5,objeto.getLgContrato());
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTabelaPreco());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoOperacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoOperacao objeto, int cdTipoOperacaoOld) {
		return update(objeto, cdTipoOperacaoOld, null);
	}

	public static int update(TipoOperacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoOperacao objeto, int cdTipoOperacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_tipo_operacao SET cd_tipo_operacao=?,"+
												      		   "nm_tipo_operacao=?,"+
												      		   "id_tipo_operacao=?,"+
												      		   "st_tipo_operacao=?,"+
												      		   "lg_contrato=?,"+
												      		   "cd_tabela_preco=? WHERE cd_tipo_operacao=?");
			pstmt.setInt(1,objeto.getCdTipoOperacao());
			pstmt.setString(2,objeto.getNmTipoOperacao());
			pstmt.setString(3,objeto.getIdTipoOperacao());
			pstmt.setInt(4,objeto.getStTipoOperacao());
			pstmt.setInt(5,objeto.getLgContrato());
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTabelaPreco());
			pstmt.setInt(7, cdTipoOperacaoOld!=0 ? cdTipoOperacaoOld : objeto.getCdTipoOperacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoOperacao) {
		return delete(cdTipoOperacao, null);
	}

	public static int delete(int cdTipoOperacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_tipo_operacao WHERE cd_tipo_operacao=?");
			pstmt.setInt(1, cdTipoOperacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoOperacao get(int cdTipoOperacao) {
		return get(cdTipoOperacao, null);
	}

	public static TipoOperacao get(int cdTipoOperacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_tipo_operacao WHERE cd_tipo_operacao=?");
			pstmt.setInt(1, cdTipoOperacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoOperacao(rs.getInt("cd_tipo_operacao"),
						rs.getString("nm_tipo_operacao"),
						rs.getString("id_tipo_operacao"),
						rs.getInt("st_tipo_operacao"),
						rs.getInt("lg_contrato"),
						rs.getInt("cd_tabela_preco"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_tipo_operacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_tipo_operacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
