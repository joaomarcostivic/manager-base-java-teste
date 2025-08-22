package com.tivic.manager.mob.ait.aitArquivo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tivic.sol.connection.Conexao;

public class AitArquivoDAO {

	public static int insert(AitArquivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(AitArquivo objeto, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					"INSERT INTO mob_ait_arquivo ( " 
							+ "cd_arquivo, "
							+ "cd_ait " 
							+ ") VALUES (?, ?)");
			pstmt.setInt(1, objeto.getCdArquivo());
			pstmt.setInt(2, objeto.getCdAit());
			pstmt.executeUpdate();
			return 1;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitArquivoDAO.insert: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitArquivoDAO.insert: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AitArquivo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AitArquivo objeto, int cdArquivoOld, int cdAitOld) {
		return update(objeto, cdArquivoOld, cdAitOld, null);
	}

	public static int update(AitArquivo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AitArquivo objeto, int cdArquivoOld, int cdAitOld, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect
					.prepareStatement("UPDATE mob_ait_arquivo SET cd_arquivo=?, "
							+ "cd_ait=? "
							+ "WHERE cd_arquivo=? AND cd_ait=? ");
			pstmt.setInt(1, objeto.getCdArquivo());
			pstmt.setInt(2, objeto.getCdAit());
			pstmt.setInt(3, cdArquivoOld != 0 ? cdArquivoOld : objeto.getCdArquivo());
			pstmt.setInt(4, cdAitOld != 0 ? cdAitOld : objeto.getCdAit());
			pstmt.executeUpdate();
			return 1;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitArquivoDAO.update: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitArquivoDAO.update: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdArquivo) {
		return delete(cdArquivo,  null);
	}

	public static int delete(int cdArquivo, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					"DELETE FROM mob_ait_arquivo WHERE cd_arquivo=?");
			pstmt.setInt(1, cdArquivo);
			pstmt.executeUpdate();
			return 1;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitArquivoDAO.delete: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitArquivoDAO.delete: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitArquivo get(int cdArquivo) {
		return get(cdArquivo, null);
	}

	public static AitArquivo get(int cdArquivo, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement(
					"SELECT * FROM mob_ait_arquivo WHERE cd_arquivo=?");
			pstmt.setInt(1, cdArquivo);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return new AitArquivo(
						rs.getInt("cd_arquivo"),
						rs.getInt("cd_ait")
						);
			} else {
				return null;
			}
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitArquivoDAO.get: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitArquivoDAO.get: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}
