package com.tivic.manager.grl;

import java.sql.*;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class GrupoParametroDAO {
	
	public GrupoParametroDAO() {}
	
	public int insert(GrupoParametro obj) {
		return insert(obj, null);
	}
	
	public int insert(GrupoParametro obj, Connection conn) {
		boolean isConnNull = (conn == null);
		
		try {
			if(isConnNull)
				conn = Conexao.conectar();
			
			int code;
			if(obj.getCdGrupoParametro() <= 0) {
				code = Conexao.getSequenceCode("grl_grupo_parametro", conn);
				if(code <= 0) {
					if(isConnNull)
						Conexao.rollback(conn);
					return -1;
				}
			} else {
				code = obj.getCdGrupoParametro();
			}
			
			obj.setCdGrupoParametro(code);
			
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO grl_grupo_parametro (cd_grupo_parametro, nm_grupo_parametro) values (?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2, obj.getNmGrupoParametro());
			
			pstmt.executeUpdate();
			return code;			
		} catch(SQLException ex) {
			ex.printStackTrace(System.out);
			return -1;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return -1;
		} finally {
			if(isConnNull)
				Conexao.desconectar(conn);
		}
	}
	
	public int update(GrupoParametro obj) {
		return update(obj, null);
	}
	
	public int update(GrupoParametro obj, Connection conn) {
		boolean isConnNull = (conn == null);
		
		try {
			if(isConnNull)
				conn = Conexao.conectar();
			
			PreparedStatement pstmt = conn.prepareStatement("UPDATE grl_grupo_parametro SET nm_grupo_parametro = ? WHERE cd_grupo_parametro = ?");
			pstmt.setString(1, obj.getNmGrupoParametro());
			pstmt.setInt(2,  obj.getCdGrupoParametro());
			
			int res = pstmt.executeUpdate();
			return res;
		} catch(SQLException ex) {
			ex.printStackTrace(System.out);
			return -1;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return -1;
		} finally {
			if(isConnNull)
				Conexao.desconectar(conn);
		}
	}
	
	public GrupoParametro get(int cdParametro) {
		return get(cdParametro, null);
	}
	
	public GrupoParametro get(int cdParametro, Connection conn) {
		boolean isConnNull = (conn == null);
		
		try {
			if(isConnNull)
				conn = Conexao.conectar();
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM grl_grupo_parametro WHERE cd_grupo_parametro = ?");
			pstmt.setInt(1, cdParametro);
			
			ResultSetMap _rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(_rsm.next()) {
				return new GrupoParametro(_rsm.getInt("cd_grupo_parametro"), _rsm.getString("nm_grupo_parametro"));
			}
			
			return null;
		} catch(SQLException ex) {
			ex.printStackTrace(System.out);
			return null;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnNull)
				Conexao.desconectar(conn);
		}
	}
	
	public ResultSetMap getAll() {
		return getAll(null);
	}
	
	public ResultSetMap getAll(Connection connect) {
		boolean isConnNull = (connect == null);
		
		try {
			if(isConnNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_grupo_parametro");
			ResultSetMap _rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(_rsm.next())
				return _rsm;
			
			return null;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnNull)
				Conexao.desconectar(connect);
		}
	}

}
