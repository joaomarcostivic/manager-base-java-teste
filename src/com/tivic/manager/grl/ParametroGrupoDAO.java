package com.tivic.manager.grl;

import java.sql.*;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class ParametroGrupoDAO {
	
	public ParametroGrupoDAO() {}
	
	public int insert(ParametroGrupo obj) {
		return insert(obj, null);
	}
	
	public int insert(ParametroGrupo obj, Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_parametro_grupo (cd_grupo_parametro, cd_parametro) VALUES (?, ?)");
			pstmt.setInt(1, obj.getCdGrupoParametro());
			pstmt.setInt(2, obj.getCdParametro());
			
			pstmt.executeUpdate();			
			return 1;
		} catch(SQLException ex) {
			ex.printStackTrace(System.out);
			return -1;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return -1;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public int update(ParametroGrupo obj) {
		return update(obj, null);
	}
	
	public int update(ParametroGrupo obj, Connection connect) {
		boolean isConnNull = (connect == null);
		
		try {
			if(isConnNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_parametro_grupo SET cd_grupo_parametro = ? WHERE cd_parametro = ?");
			pstmt.setInt(1, obj.getCdGrupoParametro());
			pstmt.setInt(2,  obj.getCdParametro());
			
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
				Conexao.desconectar(connect);
		}
	}
	
	public ParametroGrupo get(int cdParametro) {
		return get(cdParametro, null);
	}
	
	public ParametroGrupo get(int cdParametro, Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_parametro_grupo WHERE cd_parametro = ?");
			pstmt.setInt(1, cdParametro);
			
			ResultSetMap _rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(_rsm.next()) 
				return new ParametroGrupo(_rsm.getInt("cd_parametro"), _rsm.getInt("cd_grupo_parametro"));
			
			return null;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		}
	}
	
	public ResultSetMap getAll(int cdGrupoParametro) {
		return getAll(cdGrupoParametro, null);
	}
	
	public ResultSetMap getAll(int cdGrupoParametro, Connection connect) {
		boolean isConnNull = (connect == null);
		
		try {
			if(isConnNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_parametro_grupo WHERE cd_grupo_parametro = ?");
			pstmt.setInt(1, cdGrupoParametro);
			
			ResultSetMap _rsm = new ResultSetMap(pstmt.executeQuery());
			
			return _rsm;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		}
	}

}
