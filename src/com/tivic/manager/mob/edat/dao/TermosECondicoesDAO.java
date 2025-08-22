package com.tivic.manager.mob.edat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.edat.TermosECondicoes;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class TermosECondicoesDAO {

	public static int insert(TermosECondicoes termos) {
		return insert(termos, null);
	}

	public static int insert(TermosECondicoes termos, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_edat_termos", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			termos.setCdTermo(code);
			
			PreparedStatement pstmt = connect
					.prepareStatement("INSERT INTO mob_edat_termos (cd_termo," 
							+ "ds_termo," 
							+ "nr_posicao) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2, termos.getDsTermo());
			pstmt.setInt(3, termos.getNrPosicao());
			pstmt.executeUpdate();
			return code;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TermosECondicoesDAO.insert: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TermosECondicoesDAO.insert: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TermosECondicoes termos) {
		return update(termos, null);
	}

	public static int update(TermosECondicoes termos, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt = connect.prepareStatement(
					"UPDATE mob_edat_termos SET cd_termo=?," 
							+ "ds_termo=?," 
							+ "nr_posicao=? WHERE cd_termo=?");
			pstmt.setInt(1, termos.getCdTermo());
			pstmt.setString(2, termos.getDsTermo());
			pstmt.setInt(3, termos.getNrPosicao());
			pstmt.setInt(4, termos.getCdTermo());
			pstmt.executeUpdate();
			return 1;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TermosECondicoesDAO.update: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TermosECondicoesDAO.update: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int updateAll(List<TermosECondicoes> termosList) {
		return updateAll(termosList, null);
	}

	public static int updateAll(List<TermosECondicoes> termosList, Connection connect) {
		boolean isConnectionNull = connect == null;
		PreparedStatement pstmt = null;

		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			pstmt = connect.prepareStatement(
					"UPDATE mob_edat_termos SET cd_termo=?, ds_termo=?, nr_posicao=? WHERE cd_termo=?");

			for (TermosECondicoes termos : termosList) {
				pstmt.setInt(1, termos.getCdTermo());
				pstmt.setString(2, termos.getDsTermo());
				pstmt.setInt(3, termos.getNrPosicao());
				pstmt.setInt(4, termos.getCdTermo());
				pstmt.addBatch();
			}

			int[] updateCounts = pstmt.executeBatch();

			return updateCounts.length;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TermosECondicoesDAO.updateAll: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TermosECondicoesDAO.updateAll: " + e);
			return -1;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}

			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTermo) {
		return delete(cdTermo, null);
	}

	public static int delete(int cdTermo, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_edat_termos WHERE cd_termo=?");
			pstmt.setInt(1, cdTermo);
			pstmt.executeUpdate();
			return 1;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TermosECondicoesDAO.delete: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TermosECondicoesDAO.delete: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TermosECondicoes get(int cdTermo) {
		return get(cdTermo, null);
	}

	public static TermosECondicoes get(int cdTermo, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
				pstmt = connect.prepareStatement("SELECT * FROM mob_edat_termos WHERE cd_termo=?");
			pstmt.setInt(1, cdTermo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return new TermosECondicoes(rs.getInt("cd_termo"),
						rs.getString("ds_termo"),
						rs.getInt("nr_posicao"));
			} else {
				return null;
			}
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TermosECondicoesDAO.get: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TermosECondicoesDAO.get: " + e);
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
		return Search.find("SELECT * FROM mob_edat_termos", criterios, connect != null ? connect : Conexao.conectar(),
				connect == null);
	}
}
