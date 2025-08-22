package com.tivic.manager.agd;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.Util;

public class GrupoPessoaDAO{

	public static int insert(GrupoPessoa objeto) {
		return insert(objeto, null);
	}

	public static int insert(GrupoPessoa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		System.out.println("GrupoPessoa: "+objeto);
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_grupo_pessoa (cd_pessoa,"+
			                                  "cd_grupo,"+
			                                  "dt_inclusao,"+
			                                  "st_grupo_pessoa) VALUES (?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdGrupo());
			if(objeto.getDtInclusao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInclusao().getTimeInMillis()));
			pstmt.setInt(4,objeto.getStGrupoPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoPessoaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoPessoaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(GrupoPessoa objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(GrupoPessoa objeto, int cdPessoaOld, int cdGrupoOld) {
		return update(objeto, cdPessoaOld, cdGrupoOld, null);
	}

	public static int update(GrupoPessoa objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(GrupoPessoa objeto, int cdPessoaOld, int cdGrupoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agd_grupo_pessoa SET cd_pessoa=?,"+
												      		   "cd_grupo=?,"+
												      		   "dt_inclusao=?,"+
												      		   "st_grupo_pessoa=? WHERE cd_pessoa=? AND cd_grupo=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getCdGrupo());
			if(objeto.getDtInclusao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInclusao().getTimeInMillis()));
			pstmt.setInt(4,objeto.getStGrupoPessoa());
			pstmt.setInt(5, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(6, cdGrupoOld!=0 ? cdGrupoOld : objeto.getCdGrupo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoPessoaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoPessoaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoa, int cdGrupo) {
		return delete(cdPessoa, cdGrupo, null);
	}

	public static int delete(int cdPessoa, int cdGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_grupo_pessoa WHERE cd_pessoa=? AND cd_grupo=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdGrupo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoPessoaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoPessoaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static GrupoPessoa get(int cdPessoa, int cdGrupo) {
		return get(cdPessoa, cdGrupo, null);
	}

	public static GrupoPessoa get(int cdPessoa, int cdGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_grupo_pessoa WHERE cd_pessoa=? AND cd_grupo=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdGrupo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new GrupoPessoa(rs.getInt("cd_pessoa"),
						rs.getInt("cd_grupo"),
						(rs.getTimestamp("dt_inclusao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inclusao").getTime()),
						rs.getInt("st_grupo_pessoa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoPessoaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoPessoaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_grupo_pessoa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoPessoaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoPessoaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM agd_grupo_pessoa", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
