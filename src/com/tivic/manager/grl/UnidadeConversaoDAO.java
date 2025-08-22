package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class UnidadeConversaoDAO{

	public static int insert(UnidadeConversao objeto) {
		return insert(objeto, null);
	}

	public static int insert(UnidadeConversao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_unidade_conversao (cd_unidade_origem,"+
			                                  "cd_unidade_destino,"+
			                                  "vl_fator_conversao,"+
			                                  "tp_operacao_conversao) VALUES (?, ?, ?, ?)");
			if(objeto.getCdUnidadeOrigem()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdUnidadeOrigem());
			if(objeto.getCdUnidadeDestino()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdUnidadeDestino());
			pstmt.setFloat(3,objeto.getVlFatorConversao());
			pstmt.setInt(4,objeto.getTpOperacaoConversao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeConversaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeConversaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(UnidadeConversao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(UnidadeConversao objeto, int cdUnidadeOrigemOld, int cdUnidadeDestinoOld) {
		return update(objeto, cdUnidadeOrigemOld, cdUnidadeDestinoOld, null);
	}

	public static int update(UnidadeConversao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(UnidadeConversao objeto, int cdUnidadeOrigemOld, int cdUnidadeDestinoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_unidade_conversao SET cd_unidade_origem=?,"+
												      		   "cd_unidade_destino=?,"+
												      		   "vl_fator_conversao=?,"+
												      		   "tp_operacao_conversao=? WHERE cd_unidade_origem=? AND cd_unidade_destino=?");
			pstmt.setInt(1,objeto.getCdUnidadeOrigem());
			pstmt.setInt(2,objeto.getCdUnidadeDestino());
			pstmt.setFloat(3,objeto.getVlFatorConversao());
			pstmt.setInt(4,objeto.getTpOperacaoConversao());
			pstmt.setInt(5, cdUnidadeOrigemOld!=0 ? cdUnidadeOrigemOld : objeto.getCdUnidadeOrigem());
			pstmt.setInt(6, cdUnidadeDestinoOld!=0 ? cdUnidadeDestinoOld : objeto.getCdUnidadeDestino());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeConversaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeConversaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdUnidadeOrigem, int cdUnidadeDestino) {
		return delete(cdUnidadeOrigem, cdUnidadeDestino, null);
	}

	public static int delete(int cdUnidadeOrigem, int cdUnidadeDestino, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_unidade_conversao WHERE cd_unidade_origem=? AND cd_unidade_destino=?");
			pstmt.setInt(1, cdUnidadeOrigem);
			pstmt.setInt(2, cdUnidadeDestino);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeConversaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeConversaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static UnidadeConversao get(int cdUnidadeOrigem, int cdUnidadeDestino) {
		return get(cdUnidadeOrigem, cdUnidadeDestino, null);
	}

	public static UnidadeConversao get(int cdUnidadeOrigem, int cdUnidadeDestino, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_unidade_conversao WHERE cd_unidade_origem=? AND cd_unidade_destino=?");
			pstmt.setInt(1, cdUnidadeOrigem);
			pstmt.setInt(2, cdUnidadeDestino);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new UnidadeConversao(rs.getInt("cd_unidade_origem"),
						rs.getInt("cd_unidade_destino"),
						rs.getFloat("vl_fator_conversao"),
						rs.getInt("tp_operacao_conversao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeConversaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeConversaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_unidade_conversao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeConversaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeConversaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_unidade_conversao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
