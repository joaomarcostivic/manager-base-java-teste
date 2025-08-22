package com.tivic.manager.adapter.base.antiga.parametro;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class ParametroDAO {
	
	
	public int insert(ParametroOld<?> parametro, CustomConnection customConnection) {
		try {
			PreparedStatement pstmt = customConnection.getConnection()
					.prepareStatement("ALTER TABLE parametro ADD COLUMN " + parametro.getNmParametro() + " " + parametro.getTpDadoParametro());
			pstmt.executeUpdate();
			return 1;
		} catch (SQLException e) {
			return -1;
		}
	}
	
	public int update(ParametroOld<?> parametro, CustomConnection customConnection) {
		try {
			PreparedStatement pstmt = customConnection.getConnection()
					.prepareStatement("UPDATE parametro SET " + parametro.getNmParametro() + " = " + parametro.getValorParametro());
			pstmt.executeUpdate();
			return 1;
		} catch (SQLException e) {
			return -1;
		}
	}

	public String getValorOfParametroAsString(String nmParametro, CustomConnection customConnection) throws Exception {
		customConnection.initConnection(false);
		PreparedStatement pstmt = customConnection.getConnection().prepareStatement("SELECT " + nmParametro + " FROM parametro");
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
			return rs.getString(nmParametro);
		else
			throw new ValidacaoException("Nenhum parâmetro encontrado com o nome: " + nmParametro);
	}
	
	public int getValorOfParametroAsInt(String nmParametro, CustomConnection customConnection) throws Exception {
		customConnection.initConnection(false);
		PreparedStatement pstmt = customConnection.getConnection().prepareStatement("SELECT " + nmParametro + " FROM parametro");
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
			return rs.getInt(nmParametro);
		else
			throw new ValidacaoException("Nenhum parâmetro encontrado com o nome: " + nmParametro);
	}
	
	public byte[] getValorOfParametroAsByte(String nmParametro, CustomConnection customConnection) throws Exception {
		customConnection.initConnection(false);
		PreparedStatement pstmt = customConnection.getConnection().prepareStatement("SELECT " + nmParametro + " FROM parametro");
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
			return rs.getBytes(nmParametro);
		else
			throw new ValidacaoException("Nenhum parâmetro encontrado com o nome: " + nmParametro);
	}
	
	public boolean getValorOfParametroAsBoolean(String nmParametro, CustomConnection customConnection) throws Exception {
		PreparedStatement pstmt = customConnection.getConnection().prepareStatement("SELECT " + nmParametro + " FROM parametro");
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			try {
				return rs.getBoolean(nmParametro);
			} catch (Exception e) {
				throw new ValidacaoException("O parâmetro " + nmParametro + " não esta configurado corretamente.");
			}
		}
		else
			throw new ValidacaoException("Nenhum parâmetro encontrado com o nome: " + nmParametro);
	}
	
	public ParametroOld<String> getByName(String name, CustomConnection customConnection) throws SQLException {
		PreparedStatement pstmt = customConnection.getConnection().prepareStatement("SELECT " + name + " FROM parametro");
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			ParametroOld<String> parametro = new ParametroOld<String>();
			parametro.setNmParametro(name);
			parametro.setValorParametro(rs.getString(name));
			return parametro;
		}
		else {
			return null;
		}
	}
	
}
