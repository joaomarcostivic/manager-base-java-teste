package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class GrupoParametroServices {
	
	private GrupoParametroDAO _dao = new GrupoParametroDAO();
	
	public int save(GrupoParametro grupoParametro) {
		return save(grupoParametro, null);
	}
	
	public int save(GrupoParametro grupoParametro, Connection conn) {
		boolean isConnNull = (conn == null);
		int res = 0;
		
		try {
			if(isConnNull)
				conn = Conexao.conectar();
			
			GrupoParametro _gp = this.getGrupoParametroByNome(grupoParametro.getNmGrupoParametro());
			
			if(_gp != null) {
				grupoParametro.setCdGrupoParametro(_gp.getCdGrupoParametro());
				res = this._dao.update(grupoParametro);
			} else {				
				res = this._dao.insert(grupoParametro);
			}
			
			return res;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return -1;
		} finally {
			if(isConnNull)
				Conexao.desconectar(conn);
		}
	}
	
	public GrupoParametro getGrupoParametroByNome(String nmGrupoParametro) {
		return getGrupoParametroByNome(nmGrupoParametro, null);
	}
	
	public GrupoParametro getGrupoParametroByNome(String nmGrupoParametro, Connection conn) {
		boolean isConnNull = (conn == null);
		
		try {
			if(isConnNull)
				conn = Conexao.conectar();
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM grl_grupo_parametro WHERE nm_grupo_parametro = ?");
			pstmt.setString(1, nmGrupoParametro);
			
			ResultSetMap _rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(_rsm.next())
				return new GrupoParametro(_rsm.getInt("cd_grupo_parametro"), _rsm.getString("nm_grupo_parametro"));
			
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
	
	public void initGruposMob() {
		this.save(new GrupoParametro(ParametroServices.DADOS_ORGAO, "Dados do Órgão"));
		this.save(new GrupoParametro(ParametroServices.OUTRAS_INFORMACOES, "Outras Informações"));
		this.save(new GrupoParametro(ParametroServices.MARCAS_CONFIG, "Marcas e Config. Impressos"));
		this.save(new GrupoParametro(ParametroServices.MOBILIDADE, "Mobilidade"));
		this.save(new GrupoParametro(ParametroServices.GERADORES_SEQUENCIAIS, "Geradores e Sequenciais"));
		this.save(new GrupoParametro(ParametroServices.PROTOCOLOS, "Protocolos"));
		this.save(new GrupoParametro(ParametroServices.INCONSISTENCIAS, "Inconsistência"));
		this.save(new GrupoParametro(ParametroServices.NAI, "NAI"));
		this.save(new GrupoParametro(ParametroServices.NIP, "NIP"));
		this.save(new GrupoParametro(ParametroServices.FINANCEIRO, "Financeiro"));
		this.save(new GrupoParametro(ParametroServices.PORTAL_CIDADAO, "Portal"));
		this.save(new GrupoParametro(ParametroServices.TALONARIO_ELETRONICO, "Talonário Eletrônico"));
	}
	
	public void resetGruposParametro() {
		resetGruposParametro(null);
	}
	
	public void resetGruposParametro(Connection connect) {
		boolean isConnectionNull = connect == null;
		
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM GRL_GRUPO_PARAMETRO");
			pstmt.executeUpdate();
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
