package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TrechoRotaDAO{

	public static int insert(TrechoRota objeto) {
		return insert(objeto, null);
	}

	public static int insert(TrechoRota objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fta_trecho_rota", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTrecho(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_trecho_rota (cd_trecho,"+
			                                  "cd_rota,"+
			                                  "nm_trecho,"+
			                                  "qt_distancia_origem,"+
			                                  "qt_distancia_trecho,"+
			                                  "tp_pavimento,"+
			                                  "cd_cidade_parada) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdRota()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdRota());
			pstmt.setString(3,objeto.getNmTrecho());
			pstmt.setFloat(4,objeto.getQtDistanciaOrigem());
			pstmt.setFloat(5,objeto.getQtDistanciaTrecho());
			pstmt.setInt(6,objeto.getTpPavimento());
			if(objeto.getCdCidadeParada()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdCidadeParada());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrechoRotaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TrechoRotaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TrechoRota objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TrechoRota objeto, int cdTrechoOld) {
		return update(objeto, cdTrechoOld, null);
	}

	public static int update(TrechoRota objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TrechoRota objeto, int cdTrechoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_trecho_rota SET cd_trecho=?,"+
												      		   "cd_rota=?,"+
												      		   "nm_trecho=?,"+
												      		   "qt_distancia_origem=?,"+
												      		   "qt_distancia_trecho=?,"+
												      		   "tp_pavimento=?,"+
												      		   "cd_cidade_parada=? WHERE cd_trecho=?");
			pstmt.setInt(1,objeto.getCdTrecho());
			if(objeto.getCdRota()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdRota());
			pstmt.setString(3,objeto.getNmTrecho());
			pstmt.setFloat(4,objeto.getQtDistanciaOrigem());
			pstmt.setFloat(5,objeto.getQtDistanciaTrecho());
			pstmt.setInt(6,objeto.getTpPavimento());
			if(objeto.getCdCidadeParada()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdCidadeParada());
			pstmt.setInt(8, cdTrechoOld!=0 ? cdTrechoOld : objeto.getCdTrecho());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrechoRotaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TrechoRotaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTrecho) {
		return delete(cdTrecho, null);
	}

	public static int delete(int cdTrecho, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_trecho_rota WHERE cd_trecho=?");
			pstmt.setInt(1, cdTrecho);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrechoRotaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TrechoRotaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TrechoRota get(int cdTrecho) {
		return get(cdTrecho, null);
	}

	public static TrechoRota get(int cdTrecho, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_trecho_rota WHERE cd_trecho=?");
			pstmt.setInt(1, cdTrecho);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TrechoRota(rs.getInt("cd_trecho"),
						rs.getInt("cd_rota"),
						rs.getString("nm_trecho"),
						rs.getFloat("qt_distancia_origem"),
						rs.getFloat("qt_distancia_trecho"),
						rs.getInt("tp_pavimento"),
						rs.getInt("cd_cidade_parada"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrechoRotaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TrechoRotaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_trecho_rota");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrechoRotaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TrechoRotaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_trecho_rota", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
