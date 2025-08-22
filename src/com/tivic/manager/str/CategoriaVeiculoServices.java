package com.tivic.manager.str;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import sol.dao.ResultSetMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.util.Util;

public class CategoriaVeiculoServices {
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
				return com.tivic.manager.fta.CategoriaVeiculoServices.getSyncData(connect);
			}
			
			String sql = "SELECT * FROM str_categoria_veiculo";
					
			if(Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1"))
				sql = "SELECT A.*, A.cod_categoria as cd_categoria FROM CATEGORIA_VEICULO A";
			
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
	

	public static CategoriaVeiculo getByNmCategoria(String nmCategoria) {
		return getByNmCategoria(nmCategoria, null);
	}

	public static CategoriaVeiculo getByNmCategoria(String nmCategoria, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			String sql = "SELECT * FROM fta_categoria_veiculo WHERE nm_categoria=? ";
			
			if(lgBaseAntiga)
				sql = "SELECT * FROM categoria_veiculo WHERE nm_categoria=?";
			
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, nmCategoria);
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(lgBaseAntiga) {
					return new CategoriaVeiculo(rs.getInt("cod_categoria"),
							rs.getString("nm_categoria"));
				}
				else {
					return new CategoriaVeiculo(rs.getInt("cd_categoria"),
							rs.getString("nm_categoria"));
				}
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaVeiculoServices.getByNmCategoria: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaVeiculoServices.getByNmCategoria: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}