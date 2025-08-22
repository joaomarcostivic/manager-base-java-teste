package com.tivic.manager.bpm;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class MarcaAgrupamentoDAO {

	public static int insert(MarcaAgrupamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(MarcaAgrupamento objeto, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect
					.prepareStatement("INSERT INTO bpm_marca_agrupamento (cd_grupo," + "cd_marca) VALUES (?, ?)");
			if (objeto.getCdGrupo() == 0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1, objeto.getCdGrupo());
			if (objeto.getCdMarca() == 0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2, objeto.getCdMarca());
			pstmt.executeUpdate();
			return 1;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaAgrupamentoDAO.insert: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaAgrupamentoDAO.insert: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MarcaAgrupamento objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(MarcaAgrupamento objeto, int cdGrupoOld, int cdMarcaOld) {
		return update(objeto, cdGrupoOld, cdMarcaOld, null);
	}

	public static int update(MarcaAgrupamento objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(MarcaAgrupamento objeto, int cdGrupoOld, int cdMarcaOld, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					"UPDATE bpm_marca_agrupamento SET cd_grupo=?," + "cd_marca=? WHERE cd_grupo=? AND cd_marca=?");
			pstmt.setInt(1, objeto.getCdGrupo());
			pstmt.setInt(2, objeto.getCdMarca());
			pstmt.setInt(3, cdGrupoOld != 0 ? cdGrupoOld : objeto.getCdGrupo());
			pstmt.setInt(4, cdMarcaOld != 0 ? cdMarcaOld : objeto.getCdMarca());
			pstmt.executeUpdate();
			return 1;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaAgrupamentoDAO.update: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaAgrupamentoDAO.update: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdGrupo, int cdMarca) {
		return delete(cdGrupo, cdMarca, null);
	}

	public static int delete(int cdGrupo, int cdMarca, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect
					.prepareStatement("DELETE FROM bpm_marca_agrupamento WHERE cd_grupo=? AND cd_marca=?");
			pstmt.setInt(1, cdGrupo);
			pstmt.setInt(2, cdMarca);
			pstmt.executeUpdate();
			return 1;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaAgrupamentoDAO.delete: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaAgrupamentoDAO.delete: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MarcaAgrupamento get(int cdGrupo, int cdMarca) {
		return get(cdGrupo, cdMarca, null);
	}

	public static MarcaAgrupamento get(int cdGrupo, int cdMarca, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM bpm_marca_agrupamento WHERE cd_grupo=? AND cd_marca=?");
			pstmt.setInt(1, cdGrupo);
			pstmt.setInt(2, cdMarca);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return new MarcaAgrupamento(rs.getInt("cd_grupo"), rs.getInt("cd_marca"));
			} else {
				return null;
			}
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaAgrupamentoDAO.get: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaAgrupamentoDAO.get: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM bpm_marca_agrupamento");
			return new ResultSetMap(pstmt.executeQuery());
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaAgrupamentoDAO.getAll: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaAgrupamentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM bpm_marca_agrupamento", criterios, true,
				connect != null ? connect : Conexao.conectar(), connect == null);
	}

}
