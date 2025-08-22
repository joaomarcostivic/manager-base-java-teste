package com.tivic.manager.str;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import sol.dao.ResultSetMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.util.Util;

public class EspecieVeiculoServices {
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
				return com.tivic.manager.fta.EspecieVeiculoServices.getSyncData(connect);
			}
			
			String sql = "SELECT * FROM str_especie_veiculo";
					
			if(Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1"))
				sql = "SELECT A.*, A.cod_especie as cd_especie FROM ESPECIE_VEICULO A";
			
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
	

	public static EspecieVeiculo getByDsEspecie(String dsEspecie) {
		return getByDsEspecie(dsEspecie, null);
	}

	public static EspecieVeiculo getByDsEspecie(String dsEspecie, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			String sql = "SELECT * FROM fta_especie_veiculo WHERE ds_especie=? ";
			
			if(lgBaseAntiga)
				sql = "SELECT * FROM especie_veiculo WHERE ds_especie=?";
			
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, dsEspecie);
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(lgBaseAntiga) {
					return new EspecieVeiculo(rs.getInt("cod_especie"),
							rs.getString("ds_especie"));
				}
				else {
					return new EspecieVeiculo(rs.getInt("cd_especie"),
							rs.getString("ds_especie"));
				}
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EspecieVeiculoServices.getByDsEspecie: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EspecieVeiculoServices.getByDsEspecie: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}