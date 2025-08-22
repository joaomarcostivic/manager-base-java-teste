package com.tivic.manager.mob.ait.cancelamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tivic.sol.connection.Conexao;

public class AitCancelamentoDAO {
	
	public static int insert(AitCancelamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(AitCancelamento objeto, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					"INSERT INTO mob_ait_cancelamento ( " 
							+ "cd_arquivo, " 
							+ "cd_movimento, " 
							+ "cd_ait " 
							+ ") VALUES (?, ?, ?)");
			pstmt.setInt(1, objeto.getCdArquivo());
			pstmt.setInt(2, objeto.getCdMovimento());
			pstmt.setInt(3, objeto.getCdAit());
			pstmt.executeUpdate();
			return 1;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitCancelamentoDAO.insert: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitCancelamentoDAO.insert: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AitCancelamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AitCancelamento objeto, int cdArquivoOld) {
		return update(objeto, cdArquivoOld, null);
	}

	public static int update(AitCancelamento objeto, Connection connect) {
		return update(objeto, connect);
	}

	public static int update(AitCancelamento objeto, int cdArquivoOld,
			Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect
					.prepareStatement("UPDATE mob_ait_cancelamento SET cd_arquivo=?, "
							+ "cd_movimento=?, "
							+ "cd_ait=? "
							+ "WHERE cd_documento=? ");
			pstmt.setInt(1, objeto.getCdArquivo());
			pstmt.setInt(2, objeto.getCdMovimento());
			pstmt.setInt(3, objeto.getCdAit());
			pstmt.setInt(4, cdArquivoOld != 0 ? cdArquivoOld : objeto.getCdArquivo());
			pstmt.executeUpdate();
			return 1;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitCancelamentoDAO.update: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitCancelamentoDAO.update: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdArquivo) {
		return delete(cdArquivo, null);
	}

	public static int delete(int cdArquivo, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					"DELETE FROM mob_ait_cancelamento WHERE cd_arquivo=?");
			pstmt.setInt(1, cdArquivo);
			pstmt.executeUpdate();
			return 1;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitCancelamentoDAO.delete: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitCancelamentoDAO.delete: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitCancelamento get(int cdArquivo) {
		return get(cdArquivo, null);
	}

	public static AitCancelamento get(int cdArquivo, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement(
					"SELECT * FROM mob_ait_cancelamento WHERE cd_arquivo=?");
			pstmt.setInt(1, cdArquivo);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return new AitCancelamento(
						rs.getInt("cd_arquivo"),
						rs.getInt("cd_movimento"),
						rs.getInt("cd_ait")
						);
			} else {
				return null;
			}
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitCancelamentoDAO.get: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitCancelamentoDAO.get: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
