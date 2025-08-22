package com.tivic.manager.bpm;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ClassificacaoDAO{

	public static int insert(Classificacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Classificacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int code = Conexao.getSequenceCode("bpm_classificacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO bpm_classificacao (cd_classificacao,"+
			                                  "cd_classificacao_superior,"+
			                                  "nm_classificacao,"+
			                                  "nr_classificacao,"+
			                                  "sg_classificacao) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdClassificacaoSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdClassificacaoSuperior());
			pstmt.setString(3,objeto.getNmClassificacao());
			pstmt.setString(4,objeto.getNrClassificacao());
			pstmt.setString(5,objeto.getSgClassificacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Classificacao objeto) {
		return update(objeto, null);
	}

	public static int update(Classificacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE bpm_classificacao SET cd_classificacao_superior=?,"+
			                                  "nm_classificacao=?,"+
			                                  "nr_classificacao=?,"+
			                                  "sg_classificacao=? WHERE cd_classificacao=?");
			if(objeto.getCdClassificacaoSuperior()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdClassificacaoSuperior());
			pstmt.setString(2,objeto.getNmClassificacao());
			pstmt.setString(3,objeto.getNrClassificacao());
			pstmt.setString(4,objeto.getSgClassificacao());
			pstmt.setInt(5,objeto.getCdClassificacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdClassificacao) {
		return delete(cdClassificacao, null);
	}

	public static int delete(int cdClassificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM bpm_classificacao WHERE cd_classificacao=?");
			pstmt.setInt(1, cdClassificacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Classificacao get(int cdClassificacao) {
		return get(cdClassificacao, null);
	}

	public static Classificacao get(int cdClassificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM bpm_classificacao WHERE cd_classificacao=?");
			pstmt.setInt(1, cdClassificacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Classificacao(rs.getInt("cd_classificacao"),
						rs.getInt("cd_classificacao_superior"),
						rs.getString("nm_classificacao"),
						rs.getString("nr_classificacao"),
						rs.getString("sg_classificacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM bpm_classificacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM bpm_classificacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
