package com.tivic.manager.crt;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.manager.conexao.*;
import com.tivic.sol.connection.Conexao;

public class SituacaoDAO{

	public static int insert(Situacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Situacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int code = Conexao.getSequenceCode("CRT_SITUACAO");
			pstmt = connect.prepareStatement("INSERT INTO CRT_SITUACAO (CD_SITUACAO,"+
			                                  "NM_SITUACAO,"+
			                                  "TP_SITUACAO,"+
			                                  "LG_IGNORAR) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmSituacao());
			pstmt.setInt(3,objeto.getTpSituacao());
			pstmt.setInt(4,objeto.getLgIgnorar());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Situacao objeto) {
		return update(objeto, null);
	}

	public static int update(Situacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE CRT_SITUACAO SET NM_SITUACAO=?,"+
			                                  "TP_SITUACAO=?,"+
			                                  "LG_IGNORAR=? WHERE CD_SITUACAO=?");
			pstmt.setString(1,objeto.getNmSituacao());
			pstmt.setInt(2,objeto.getTpSituacao());
			pstmt.setInt(3,objeto.getLgIgnorar());
			pstmt.setInt(4,objeto.getCdSituacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdSituacao) {
		return delete(cdSituacao, null);
	}

	public static int delete(int cdSituacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM CRT_SITUACAO WHERE CD_SITUACAO=?");
			pstmt.setInt(1, cdSituacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Situacao get(int cdSituacao) {
		return get(cdSituacao, null);
	}

	public static Situacao get(int cdSituacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM CRT_SITUACAO WHERE CD_SITUACAO=?");
			pstmt.setInt(1, cdSituacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Situacao(rs.getInt("CD_SITUACAO"),
						rs.getString("NM_SITUACAO"),
						rs.getInt("TP_SITUACAO"),
						rs.getInt("LG_IGNORAR"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM CRT_SITUACAO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllStContrato() {
		return Search.find("SELECT * FROM CRT_SITUACAO WHERE tp_situacao = 0", new ArrayList<ItemComparator>(), Conexao.conectar());
	}

	public static ResultSetMap getAllStComissao() {
		return Search.find("SELECT * FROM CRT_SITUACAO WHERE tp_situacao = 1", new ArrayList<ItemComparator>(), Conexao.conectar());
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT * FROM CRT_SITUACAO", criterios, Conexao.conectar());
	}

}
