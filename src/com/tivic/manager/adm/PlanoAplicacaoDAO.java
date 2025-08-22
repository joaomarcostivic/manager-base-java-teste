package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class PlanoAplicacaoDAO{

	public static int insert(PlanoAplicacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(PlanoAplicacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int code = Conexao.getSequenceCode("adm_plano_aplicacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO adm_plano_aplicacao (cd_plano_aplicacao,"+
			                                  "cd_plano_aplicacao_superior,"+
			                                  "nr_conta,"+
			                                  "nm_conta,"+
			                                  "id_conta) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPlanoAplicacaoSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPlanoAplicacaoSuperior());
			pstmt.setString(3,objeto.getNrConta());
			pstmt.setString(4,objeto.getNmConta());
			pstmt.setString(5,objeto.getIdConta());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoAplicacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoAplicacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PlanoAplicacao objeto) {
		return update(objeto, null);
	}

	public static int update(PlanoAplicacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE adm_plano_aplicacao SET cd_plano_aplicacao_superior=?,"+
			                                  "nr_conta=?,"+
			                                  "nm_conta=?,"+
			                                  "id_conta=? WHERE cd_plano_aplicacao=?");
			if(objeto.getCdPlanoAplicacaoSuperior()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPlanoAplicacaoSuperior());
			pstmt.setString(2,objeto.getNrConta());
			pstmt.setString(3,objeto.getNmConta());
			pstmt.setString(4,objeto.getIdConta());
			pstmt.setInt(5,objeto.getCdPlanoAplicacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoAplicacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoAplicacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPlanoAplicacao) {
		return delete(cdPlanoAplicacao, null);
	}

	public static int delete(int cdPlanoAplicacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM adm_plano_aplicacao WHERE cd_plano_aplicacao=?");
			pstmt.setInt(1, cdPlanoAplicacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoAplicacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoAplicacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PlanoAplicacao get(int cdPlanoAplicacao) {
		return get(cdPlanoAplicacao, null);
	}

	public static PlanoAplicacao get(int cdPlanoAplicacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_plano_aplicacao WHERE cd_plano_aplicacao=?");
			pstmt.setInt(1, cdPlanoAplicacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PlanoAplicacao(rs.getInt("cd_plano_aplicacao"),
						rs.getInt("cd_plano_aplicacao_superior"),
						rs.getString("nr_conta"),
						rs.getString("nm_conta"),
						rs.getString("id_conta"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoAplicacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoAplicacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_plano_aplicacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoAplicacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoAplicacaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_plano_aplicacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
