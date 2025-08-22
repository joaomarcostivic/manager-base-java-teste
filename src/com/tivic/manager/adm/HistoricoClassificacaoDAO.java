package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.Util;

public class HistoricoClassificacaoDAO{

	public static int insert(HistoricoClassificacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(HistoricoClassificacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_historico_classificacao (cd_pessoa,"+
			                                  "cd_classificacao,"+
			                                  "dt_inicio) VALUES (?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdClassificacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdClassificacao());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HistoricoClassificacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! HistoricoClassificacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(HistoricoClassificacao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(HistoricoClassificacao objeto, int cdPessoaOld, int cdClassificacaoOld) {
		return update(objeto, cdPessoaOld, cdClassificacaoOld, null);
	}

	public static int update(HistoricoClassificacao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(HistoricoClassificacao objeto, int cdPessoaOld, int cdClassificacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_historico_classificacao SET cd_pessoa=?,"+
												      		   "cd_classificacao=?,"+
												      		   "dt_inicio=? WHERE cd_pessoa=? AND cd_classificacao=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getCdClassificacao());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			pstmt.setInt(4, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(5, cdClassificacaoOld!=0 ? cdClassificacaoOld : objeto.getCdClassificacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HistoricoClassificacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! HistoricoClassificacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoa, int cdClassificacao) {
		return delete(cdPessoa, cdClassificacao, null);
	}

	public static int delete(int cdPessoa, int cdClassificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_historico_classificacao WHERE cd_pessoa=? AND cd_classificacao=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdClassificacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HistoricoClassificacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! HistoricoClassificacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static HistoricoClassificacao get(int cdPessoa, int cdClassificacao) {
		return get(cdPessoa, cdClassificacao, null);
	}

	public static HistoricoClassificacao get(int cdPessoa, int cdClassificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_historico_classificacao WHERE cd_pessoa=? AND cd_classificacao=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdClassificacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new HistoricoClassificacao(rs.getInt("cd_pessoa"),
						rs.getInt("cd_classificacao"),
						(rs.getTimestamp("dt_inicio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HistoricoClassificacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! HistoricoClassificacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_historico_classificacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HistoricoClassificacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! HistoricoClassificacaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_historico_classificacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
