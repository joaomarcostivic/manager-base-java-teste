package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import sol.dao.Conexao;
import sol.dao.ResultSetMap;

public class ParametroGrupoServices {
	
	private ParametroGrupoDAO _dao;
	
	public ParametroGrupoServices() {
		this._dao = new ParametroGrupoDAO();
	}
	
	public List<Parametro> getParametrosFromGrupo(int cdGrupoParametro) {
		return getParametrosFromGrupo(cdGrupoParametro, null);
	}
	
	public List<Parametro> getParametrosFromGrupo(int cdGrupoParametro, Connection connect) {
		boolean isConnectionNull = connect == null;
		
		try {
			ResultSetMap _rsm = _dao.getAll(cdGrupoParametro);
			
			if(!_rsm.next())
				return null;
			
			int[] cods = new int[_rsm.size()];
			int i = 0;
			
			do {
				cods[i] = _rsm.getInt("CD_PARAMETRO");
				i++;
			} while(_rsm.next());
			
			List<Parametro> parametros = ParametroServices.getParametrosFromCods(cods);
			
			return parametros;			
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public void resetParametrosGrupo() {
		resetParametrosGrupo(null);
	}
	
	public void resetParametrosGrupo(Connection connect) {
		boolean isConnectionNull = connect == null;
		
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM GRL_PARAMETRO_GRUPO");
			pstmt.executeUpdate();
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
