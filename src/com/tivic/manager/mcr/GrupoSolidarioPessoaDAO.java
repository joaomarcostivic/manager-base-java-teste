package com.tivic.manager.mcr;

import java.sql.*;
import java.util.ArrayList;

import com.tivic.manager.conexao.*;
import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.dao.Search;

public class GrupoSolidarioPessoaDAO{

	public static int insert(GrupoSolidarioPessoa objeto) {
		return insert(objeto, null);
	}

	public static int insert(GrupoSolidarioPessoa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mcr_grupo_solidario_pessoa (cd_grupo_solidario,"+
			                                  "cd_pessoa,"+
			                                  "lg_coordenador) VALUES (?, ?, ?)");
			if(objeto.getCdGrupoSolidario()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdGrupoSolidario());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setInt(3,objeto.getLgCoordenador());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioPessoaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioPessoaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(GrupoSolidarioPessoa objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(GrupoSolidarioPessoa objeto, int cdGrupoSolidarioOld, int cdPessoaOld) {
		return update(objeto, cdGrupoSolidarioOld, cdPessoaOld, null);
	}

	public static int update(GrupoSolidarioPessoa objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(GrupoSolidarioPessoa objeto, int cdGrupoSolidarioOld, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mcr_grupo_solidario_pessoa SET cd_grupo_solidario=?,"+
												      		   "cd_pessoa=?,"+
												      		   "lg_coordenador=? WHERE cd_grupo_solidario=? AND cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdGrupoSolidario());
			pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setInt(3,objeto.getLgCoordenador());
			pstmt.setInt(4, cdGrupoSolidarioOld!=0 ? cdGrupoSolidarioOld : objeto.getCdGrupoSolidario());
			pstmt.setInt(5, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioPessoaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioPessoaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdGrupoSolidario, int cdPessoa) {
		return delete(cdGrupoSolidario, cdPessoa, null);
	}

	public static int delete(int cdGrupoSolidario, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mcr_grupo_solidario_pessoa WHERE cd_grupo_solidario=? AND cd_pessoa=?");
			pstmt.setInt(1, cdGrupoSolidario);
			pstmt.setInt(2, cdPessoa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioPessoaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioPessoaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static GrupoSolidarioPessoa get(int cdGrupoSolidario, int cdPessoa) {
		return get(cdGrupoSolidario, cdPessoa, null);
	}

	public static GrupoSolidarioPessoa get(int cdGrupoSolidario, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_grupo_solidario_pessoa WHERE cd_grupo_solidario=? AND cd_pessoa=?");
			pstmt.setInt(1, cdGrupoSolidario);
			pstmt.setInt(2, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new GrupoSolidarioPessoa(rs.getInt("cd_grupo_solidario"),
						rs.getInt("cd_pessoa"),
						rs.getInt("lg_coordenador"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioPessoaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioPessoaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_grupo_solidario_pessoa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioPessoaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioPessoaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM mcr_grupo_solidario_pessoa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
