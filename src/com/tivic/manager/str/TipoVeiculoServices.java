package com.tivic.manager.str;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import sol.dao.ResultSetMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.util.Util;

public class TipoVeiculoServices {
	
	public static ResultSetMap getSyncData() {
		return getSyncData(null);
	}

	public static ResultSetMap getSyncData(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			boolean str2mob = ManagerConf.getInstance().getAsBoolean("STR_TO_MOB");
			if(str2mob) {
				return com.tivic.manager.fta.TipoVeiculoServices.getSyncData(connect);
			}
			
			String sql = "SELECT * FROM str_tipo_veiculo";
					
			if(Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1"))
				sql = "SELECT A.*, A.cod_tipo as cd_tipo FROM TIPO_VEICULO A";
			
			pstmt = connect.prepareStatement(sql);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static TipoVeiculo getByNmTipo(String nmTipo) {
		return getByNmTipo(nmTipo, null);
	}

	public static TipoVeiculo getByNmTipo(String nmTipo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			String sql = "SELECT * FROM fta_tipo_veiculo WHERE nm_tipo_veiculo=? ";
			
			if(lgBaseAntiga)
				sql = "SELECT * FROM tipo_veiculo WHERE nm_tipo=?";
			
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, nmTipo);
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(lgBaseAntiga) {
					return new TipoVeiculo(rs.getInt("cod_tipo"),
							rs.getString("nm_tipo"));
				}
				else {
					return new TipoVeiculo(rs.getInt("cd_tipo_veiculo"),
							rs.getString("nm_tipo_veiculo"));
				}
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoServices.getByNmTipo: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoServices.getByNmTipo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}