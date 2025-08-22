package com.tivic.manager.adapter.base.antiga.tipoveiculo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class TipoVeiculoOldDAO {
	
	public static int insert(TipoVeiculoOld objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoVeiculoOld objeto, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getNextCode("tipo_veiculo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if (objeto.getCodTipo() <= 0)
				objeto.setCodTipo(code);
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO tipo_veiculo (cod_tipo, nm_tipo) VALUES (?, ?)");
			pstmt.setInt(1, objeto.getCodTipo());
			pstmt.setString(2, objeto.getNmTipo());
			pstmt.executeUpdate();
			return objeto.getCodTipo();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoOldDAO.insert: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoVeiculoOld objeto) {
		return update(objeto, objeto.getCodTipo(), null);
	}

	public static int update(TipoVeiculoOld objeto, int codTipoOld) {
		return update(objeto, codTipoOld, null);
	}

	public static int update(TipoVeiculoOld objeto, Connection connect) {
		return update(objeto, objeto.getCodTipo(), connect);
	}

	public static int update(TipoVeiculoOld objeto, int codTipoOld, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE tipo_veiculo SET cod_tipo = ?, nm_tipo = ? WHERE cod_tipo = ?");
			pstmt.setInt(1, objeto.getCodTipo());
			pstmt.setString(2, objeto.getNmTipo());
			pstmt.setInt(3, codTipoOld);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoOldDAO.update: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int codTipo) {
		return delete(codTipo, null);
	}

	public static int delete(int codTipo, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM tipo_veiculo WHERE cod_tipo = ?");
			pstmt.setInt(1, codTipo);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoOldDAO.delete: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoVeiculoOld get(int codTipo) {
		return get(codTipo, null);
	}

	public static TipoVeiculoOld get(int codTipo, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM tipo_veiculo WHERE cod_tipo = ?");
			pstmt.setInt(1, codTipo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return new TipoVeiculoOld(
						rs.getInt("cod_tipo"),
						rs.getString("nm_tipo")
				);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoOldDAO.get: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM tipo_veiculo ", " ORDER BY cod_tipo", criterios, connect != null ? connect : Conexao.conectar(), connect == null);
	}
}
