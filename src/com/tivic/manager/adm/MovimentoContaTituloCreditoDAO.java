package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class MovimentoContaTituloCreditoDAO{

	public static int insert(MovimentoContaTituloCredito objeto) {
		return insert(objeto, null);
	}

	public static int insert(MovimentoContaTituloCredito objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_movimento_titulo_credito (cd_titulo_credito,"+
			                                  "cd_movimento_conta,"+
			                                  "cd_conta) VALUES (?, ?, ?)");
			if(objeto.getCdTituloCredito()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTituloCredito());
			if(objeto.getCdMovimentoConta()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMovimentoConta());
			if(objeto.getCdConta()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdConta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaTituloCreditoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaTituloCreditoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MovimentoContaTituloCredito objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(MovimentoContaTituloCredito objeto, int cdTituloCreditoOld, int cdMovimentoContaOld, int cdContaOld) {
		return update(objeto, cdTituloCreditoOld, cdMovimentoContaOld, cdContaOld, null);
	}

	public static int update(MovimentoContaTituloCredito objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(MovimentoContaTituloCredito objeto, int cdTituloCreditoOld, int cdMovimentoContaOld, int cdContaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_movimento_titulo_credito SET cd_titulo_credito=?,"+
												      		   "cd_movimento_conta=?,"+
												      		   "cd_conta=? WHERE cd_titulo_credito=? AND cd_movimento_conta=? AND cd_conta=?");
			pstmt.setInt(1,objeto.getCdTituloCredito());
			pstmt.setInt(2,objeto.getCdMovimentoConta());
			pstmt.setInt(3,objeto.getCdConta());
			pstmt.setInt(4, cdTituloCreditoOld!=0 ? cdTituloCreditoOld : objeto.getCdTituloCredito());
			pstmt.setInt(5, cdMovimentoContaOld!=0 ? cdMovimentoContaOld : objeto.getCdMovimentoConta());
			pstmt.setInt(6, cdContaOld!=0 ? cdContaOld : objeto.getCdConta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaTituloCreditoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaTituloCreditoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTituloCredito, int cdMovimentoConta, int cdConta) {
		return delete(cdTituloCredito, cdMovimentoConta, cdConta, null);
	}

	public static int delete(int cdTituloCredito, int cdMovimentoConta, int cdConta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_movimento_titulo_credito WHERE cd_titulo_credito=? AND cd_movimento_conta=? AND cd_conta=?");
			pstmt.setInt(1, cdTituloCredito);
			pstmt.setInt(2, cdMovimentoConta);
			pstmt.setInt(3, cdConta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaTituloCreditoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaTituloCreditoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MovimentoContaTituloCredito get(int cdTituloCredito, int cdMovimentoConta, int cdConta) {
		return get(cdTituloCredito, cdMovimentoConta, cdConta, null);
	}

	public static MovimentoContaTituloCredito get(int cdTituloCredito, int cdMovimentoConta, int cdConta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_movimento_titulo_credito WHERE cd_titulo_credito=? AND cd_movimento_conta=? AND cd_conta=?");
			pstmt.setInt(1, cdTituloCredito);
			pstmt.setInt(2, cdMovimentoConta);
			pstmt.setInt(3, cdConta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MovimentoContaTituloCredito(rs.getInt("cd_titulo_credito"),
						rs.getInt("cd_movimento_conta"),
						rs.getInt("cd_conta"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaTituloCreditoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaTituloCreditoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_movimento_titulo_credito");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaTituloCreditoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaTituloCreditoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_movimento_titulo_credito", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
