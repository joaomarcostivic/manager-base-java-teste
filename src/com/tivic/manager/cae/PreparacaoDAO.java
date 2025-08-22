package com.tivic.manager.cae;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class PreparacaoDAO{

	public static int insert(Preparacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Preparacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("cae_preparacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPreparacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO cae_preparacao (cd_preparacao,"+
											                    "nm_preparacao,"+
											                    "txt_modo_preparo,"+
											                    "nr_minutos) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmPreparacao());
			pstmt.setString(3,objeto.getTxtModoPreparo());
			pstmt.setInt(4,objeto.getNrMinutos());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Preparacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Preparacao objeto, int cdPreparacaoOld) {
		return update(objeto, cdPreparacaoOld, null);
	}

	public static int update(Preparacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Preparacao objeto, int cdPreparacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE cae_preparacao SET cd_preparacao=?,"+
												      		   "nm_preparacao=?,"+
												      		   "txt_modo_preparo=?,"+
												      		   "nr_minutos=? WHERE cd_preparacao=?");
			pstmt.setInt(1,objeto.getCdPreparacao());
			pstmt.setString(2,objeto.getNmPreparacao());
			pstmt.setString(3,objeto.getTxtModoPreparo());
			pstmt.setInt(4,objeto.getNrMinutos());
			pstmt.setInt(5, cdPreparacaoOld!=0 ? cdPreparacaoOld : objeto.getCdPreparacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPreparacao) {
		return delete(cdPreparacao, null);
	}

	public static int delete(int cdPreparacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM cae_preparacao WHERE cd_preparacao=?");
			pstmt.setInt(1, cdPreparacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Preparacao get(int cdPreparacao) {
		return get(cdPreparacao, null);
	}

	public static Preparacao get(int cdPreparacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM cae_preparacao WHERE cd_preparacao=?");
			pstmt.setInt(1, cdPreparacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Preparacao(rs.getInt("cd_preparacao"),
						rs.getString("nm_preparacao"),
						rs.getString("txt_modo_preparo"),
						rs.getInt("nr_minutos"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM cae_preparacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Preparacao> getList() {
		return getList(null);
	}

	public static ArrayList<Preparacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Preparacao> list = new ArrayList<Preparacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Preparacao obj = PreparacaoDAO.get(rsm.getInt("cd_preparacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM cae_preparacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
