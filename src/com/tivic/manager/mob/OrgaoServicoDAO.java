package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class OrgaoServicoDAO {
	
	public int insert(OrgaoServico obj) {
		return insert(obj, null);
	}
	
	public int insert(OrgaoServico obj, Connection conn) {
		boolean nullConnection = checkConn(conn);
		
		if(nullConnection)
			conn = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(
					  "INSERT INTO mob_orgao_servico "
					+ "(cd_orgao_servico, cd_orgao, id_orgao_servico, nm_orgao_servico, st_orgao_servico, nm_url_servico, nm_login, nm_senha) "
					+ "values (?, ?, ?, ?, ?, ?, ?, ?");
			
			pstmt.setInt(1, obj.getCdOrgaoServico());
			pstmt.setInt(2, obj.getCdOrgao());
			
			if(obj.getIdOrgaoServico() != null)
				pstmt.setString(3, obj.getIdOrgaoServico());
			else
				pstmt.setNull(4, Types.VARCHAR);
			
			pstmt.setString(4, obj.getNmOrgaoServico());
			pstmt.setInt(5, obj.getStOrgaoServico());
			pstmt.setString(6, obj.getNmUrlServico());
			pstmt.setString(7, obj.getNmLogin());
			pstmt.setString(8, obj.getNmSenha());
			
			pstmt.executeUpdate();
			
			return 1;
			
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace(System.out);
			System.err.println("Erro: OrgaoServicoDAO.insert: " + sqlEx);
			return(-1 * sqlEx.getErrorCode());
		} finally {
			if(nullConnection)
				Conexao.desconectar(conn);
		}
	}
	
	public int update(OrgaoServico obj) {
		return update(obj, null);
	}
	
	public int update(OrgaoServico obj, Connection conn) {
		boolean nullConnection = checkConn(conn);
		
		if(nullConnection)
			conn = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(
					  "UPDATE mob_orgao_servico SET "
					+ "cd_orgao_servico = ?, cd_orgao = ?, id_orgao_servico = ?, nm_orgao_servico = ?, st_orgao_servico = ?, nm_url_servico = ?, nm_login = ?, nm_senha = ?) "
					+ "WHERE cd_orgao_servico = ?");
			
			pstmt.setInt(1, obj.getCdOrgaoServico());
			pstmt.setInt(2, obj.getCdOrgao());
			
			if(obj.getIdOrgaoServico() != null)
				pstmt.setString(3, obj.getIdOrgaoServico());
			else
				pstmt.setNull(4, Types.VARCHAR);
			
			pstmt.setString(4, obj.getNmOrgaoServico());
			pstmt.setInt(5, obj.getStOrgaoServico());
			pstmt.setString(6, obj.getNmUrlServico());
			pstmt.setString(7, obj.getNmLogin());
			pstmt.setString(8, obj.getNmSenha());
			pstmt.setInt(9, obj.getCdOrgaoServico());
			
			pstmt.executeUpdate();
			
			return 1;
			
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace(System.out);
			System.err.println("Erro: OrgaoServicoDAO.insert: " + sqlEx);
			return(-1 * sqlEx.getErrorCode());
		} finally {
			if(nullConnection)
				Conexao.desconectar(conn);
		}
	}
	
	public ResultSetMap getAll() {
		return getAll(null);
	}
	
	public ResultSetMap getAll(Connection conn) {
		boolean nullConnection = this.checkConn(conn);
		
		if(nullConnection)
			conn = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM mob_orgao_servico");
			return new ResultSetMap(pstmt.executeQuery());
		} catch(SQLException sqlEx) {
			sqlEx.printStackTrace(System.out);
			System.err.println("Erro ! OrgaoServicoDAO.getAll: " + sqlEx);
			return null;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			System.err.println("Erro ! OrgaoServicoDAO.getAll: " + ex);
			return null;
		} finally {
			if(nullConnection)
				Conexao.desconectar(conn);
		}
		
	}
	
	public OrgaoServico get(int cdOrgaoServico) {
		return get(cdOrgaoServico, null);
	}
	
	public OrgaoServico get(int cdOrgaoServico, Connection conn) {
		boolean nullConnection = this.checkConn(conn);
		
		if(nullConnection)
			conn = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM mob_orgao_servico WHERE cd_orgao_servico = ?)");
			pstmt.setInt(1, cdOrgaoServico);
			
			ResultSetMap _rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(_rsm.next()) {
				return new OrgaoServico(
						_rsm.getInt("cd_orgao_servico"),
						_rsm.getInt("cd_orgao"),
						_rsm.getString("id_orgao_servico"),
						_rsm.getString("nm_orgao_servico"),
						_rsm.getInt("st_orgao_servico"),
						_rsm.getString("nm_url_servico"),
						_rsm.getString("nm_login"),
						_rsm.getString("nm_senha")
						);
			}
			
			return null;
		} catch(SQLException sqlEx) {
			sqlEx.printStackTrace(System.out);
			System.err.println("Erro! OrgaoServicoDAO.get: " + sqlEx);
			return null;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			System.err.println("Erro! OrgaoServicoDAO.get: " + ex);
			return null;
		} finally {
			if(nullConnection)
				Conexao.desconectar(conn);
		}
	}
	
	private boolean checkConn(Connection conn) {
		return conn == null;
	}

}
