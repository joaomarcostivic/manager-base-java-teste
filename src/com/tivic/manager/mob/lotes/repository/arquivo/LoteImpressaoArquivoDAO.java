package com.tivic.manager.mob.lotes.repository.arquivo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.manager.mob.lotes.model.arquivo.LoteImpressaoArquivo;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class LoteImpressaoArquivoDAO {
	
	public static int insert(LoteImpressaoArquivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(LoteImpressaoArquivo objeto, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					"INSERT INTO mob_lote_impressao_arquivo (cd_lote_impressao," + "cd_arquivo) VALUES (?, ?)");
			if (objeto.getCdLoteImpressao() == 0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1, objeto.getCdLoteImpressao());
			if (objeto.getCdArquivo() == 0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2, objeto.getCdArquivo());
			pstmt.executeUpdate();
			return 1;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoArquivoDAO.insert: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoArquivoDAO.insert: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LoteImpressaoArquivo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(LoteImpressaoArquivo objeto, int cdLoteImpressaoOld, int cdArquivoOld) {
		return update(objeto, cdLoteImpressaoOld, cdArquivoOld, null);
	}

	public static int update(LoteImpressaoArquivo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(LoteImpressaoArquivo objeto, int cdLoteImpressaoOld, int cdArquivoOld,
			Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect
					.prepareStatement("UPDATE mob_lote_impressao_arquivo SET cd_lote_impressao=?,"
							+ "cd_arquivo=? WHERE cd_lote_impressao=? AND cd_arquivo=?");
			pstmt.setInt(1, objeto.getCdLoteImpressao());
			pstmt.setInt(2, objeto.getCdArquivo());
			pstmt.setInt(3, cdLoteImpressaoOld != 0 ? cdLoteImpressaoOld : objeto.getCdLoteImpressao());
			pstmt.setInt(4, cdArquivoOld != 0 ? cdArquivoOld : objeto.getCdArquivo());
			pstmt.executeUpdate();
			return 1;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoArquivoDAO.update: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoArquivoDAO.update: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLoteImpressao, int cdArquivo) {
		return delete(cdLoteImpressao, cdArquivo, null);
	}

	public static int delete(int cdLoteImpressao, int cdArquivo, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					"DELETE FROM mob_lote_impressao_arquivo WHERE cd_lote_impressao=? AND cd_arquivo=?");
			pstmt.setInt(1, cdLoteImpressao);
			pstmt.setInt(2, cdArquivo);
			pstmt.executeUpdate();
			return 1;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoArquivoDAO.delete: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoArquivoDAO.delete: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LoteImpressaoArquivo get(int cdLoteImpressao, int cdArquivo) {
		return get(cdLoteImpressao, cdArquivo, null);
	}

	public static LoteImpressaoArquivo get(int cdLoteImpressao, int cdArquivo, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement(
					"SELECT * FROM mob_lote_impressao_arquivo WHERE cd_lote_impressao=? AND cd_arquivo=?");
			pstmt.setInt(1, cdLoteImpressao);
			pstmt.setInt(2, cdArquivo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return new LoteImpressaoArquivo(rs.getInt("cd_lote_impressao"), rs.getInt("cd_arquivo"));
			} else {
				return null;
			}
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoArquivoDAO.get: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoArquivoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_lote_impressao_arquivo");
			return new ResultSetMap(pstmt.executeQuery());
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoArquivoDAO.getAll: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoArquivoDAO.getAll: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<LoteImpressaoArquivo> getList() {
		return getList(null);
	}

	public static ArrayList<LoteImpressaoArquivo> getList(Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<LoteImpressaoArquivo> list = new ArrayList<LoteImpressaoArquivo>();
			ResultSetMap rsm = getAll(connect);
			while (rsm.next()) {
				LoteImpressaoArquivo obj = LoteImpressaoArquivoDAO.get(rsm.getInt("cd_lote_impressao"),
						rsm.getInt("cd_arquivo"), connect);
				list.add(obj);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoArquivoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_lote_impressao_arquivo", criterios,
				connect != null ? connect : Conexao.conectar(), connect == null);
	}

}
