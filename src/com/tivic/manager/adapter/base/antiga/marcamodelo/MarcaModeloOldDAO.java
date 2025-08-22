package com.tivic.manager.adapter.base.antiga.marcamodelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class MarcaModeloOldDAO {
	
	public static int insert(MarcaModeloOld objeto) {
		return insert(objeto, null);
	}

	public static int insert(MarcaModeloOld objeto, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("marca_modelo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if (objeto.getCodMarca() <= 0)
				objeto.setCodMarca(code);
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO marca_modelo (cod_marca, nm_marca, nm_modelo, dt_atualizacao) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCodMarca());
			pstmt.setString(2, objeto.getNmMarca());
			pstmt.setString(3, objeto.getNmModelo());
			pstmt.setTimestamp(4, new Timestamp(objeto.getDtAtualizacao().getTimeInMillis()));
			pstmt.executeUpdate();
			return objeto.getCodMarca();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloOldDAO.insert: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MarcaModeloOld objeto) {
		return update(objeto, objeto.getCodMarca(), null);
	}

	public static int update(MarcaModeloOld objeto, int codMarcaOld) {
		return update(objeto, codMarcaOld, null);
	}

	public static int update(MarcaModeloOld objeto, Connection connect) {
		return update(objeto, objeto.getCodMarca(), connect);
	}

	public static int update(MarcaModeloOld objeto, int codMarcaOld, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE marca_modelo SET cod_marca = ?, nm_marca = ?, nm_modelo = ?, dt_atualizacao = ? WHERE cod_marca = ?");
			pstmt.setInt(1, objeto.getCodMarca());
			pstmt.setString(2, objeto.getNmMarca());
			pstmt.setString(3, objeto.getNmModelo());
			pstmt.setTimestamp(4, new Timestamp(objeto.getDtAtualizacao().getTimeInMillis()));
			pstmt.setInt(5, codMarcaOld);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloOldDAO.update: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int codMarca) {
		return delete(codMarca, null);
	}

	public static int delete(int codMarca, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM marca_modelo WHERE cod_marca = ?");
			pstmt.setInt(1, codMarca);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloOldDAO.delete: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MarcaModeloOld get(int codMarca) {
		return get(codMarca, null);
	}

	public static MarcaModeloOld get(int codMarca, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM marca_modelo WHERE cod_marca = ?");
			pstmt.setInt(1, codMarca);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				GregorianCalendar dtAtualizacao = new GregorianCalendar();
				dtAtualizacao.setTime(rs.getTimestamp("dt_atualizacao"));
				return new MarcaModeloOld(
						rs.getInt("cod_marca"),
						rs.getString("nm_marca"),
						rs.getString("nm_modelo"),
						dtAtualizacao
				);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloOldDAO.get: " + e);
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
		return Search.find("SELECT * FROM marca_modelo ", " ORDER BY cod_marca", criterios, connect != null ? connect : Conexao.conectar(), connect == null);
	}
}
