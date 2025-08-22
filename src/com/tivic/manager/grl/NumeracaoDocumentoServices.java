package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.tivic.sol.connection.Conexao;


public class NumeracaoDocumentoServices {

	synchronized public static int getProximoNumero(String nmTipoDocumento, int nrAno, int cdEmpresa) {
		return getProximoNumero(nmTipoDocumento, nrAno, cdEmpresa, null);
	}

	synchronized public static int getProximoNumero(String nmTipoDocumento, int nrAno, int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if(isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			nrAno = nrAno <= 0 ? -1 : nrAno;
			nmTipoDocumento = nmTipoDocumento.toUpperCase();

			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM GRL_NUMERACAO_DOCUMENTO " +
					"WHERE nm_tipo_documento = ? " +
					"  AND nr_ano = ? " +
					(cdEmpresa > 0 ? "  AND cd_empresa = ?" : ""));
			pstmt.setString(1, nmTipoDocumento);
			pstmt.setInt(2, nrAno);
			if (cdEmpresa > 0)
				pstmt.setInt(3, cdEmpresa);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) {
				NumeracaoDocumento numeracao = new NumeracaoDocumento(0, nmTipoDocumento.toUpperCase(), 1, nrAno, cdEmpresa);
				if (NumeracaoDocumentoDAO.insert(numeracao, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
				else
					return 1;
			}
			else {
				NumeracaoDocumento numeracao = new NumeracaoDocumento(rs.getInt("cd_numeracao_documento"),
						nmTipoDocumento.toUpperCase(), rs.getInt("cd_ultimo_numero") + 1, nrAno, cdEmpresa);
				if (NumeracaoDocumentoDAO.update(numeracao, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
				else
					return rs.getInt("cd_ultimo_numero") + 1;
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connection);
		}
  	}
	
	
	synchronized public static int getProximoNumero2(String nmTipoDocumento, int nrAno, int cdEmpresa) {
		return getProximoNumero2(nmTipoDocumento, nrAno, cdEmpresa, null);
	}

	synchronized public static int getProximoNumero2(String nmTipoDocumento, int nrAno, int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if(isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			nrAno = nrAno <= 0 ? -1 : nrAno;
			nmTipoDocumento = nmTipoDocumento.toUpperCase();

			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM GRL_NUMERACAO_DOCUMENTO " +
					"WHERE nm_tipo_documento = ? " +
					"  AND nr_ano = ? " +
					(cdEmpresa > 0 ? "  AND cd_empresa = ?" : ""));
			pstmt.setString(1, nmTipoDocumento);
			pstmt.setInt(2, nrAno);
			if (cdEmpresa > 0)
				pstmt.setInt(3, cdEmpresa);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) {
				NumeracaoDocumento numeracao = new NumeracaoDocumento(0, nmTipoDocumento.toUpperCase(), 1, nrAno, cdEmpresa);
				if (NumeracaoDocumentoDAO.insert(numeracao, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
				else
					return 1;
			}
			else {
				NumeracaoDocumento numeracao = new NumeracaoDocumento(rs.getInt("cd_numeracao_documento"),
						nmTipoDocumento.toUpperCase(), rs.getInt("cd_ultimo_numero"), nrAno, cdEmpresa);
				if (NumeracaoDocumentoDAO.update(numeracao, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
				else
					return rs.getInt("cd_ultimo_numero");
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connection);
		}
  	}
}
