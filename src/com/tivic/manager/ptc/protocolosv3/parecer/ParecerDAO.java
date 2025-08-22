package com.tivic.manager.ptc.protocolosv3.parecer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.tivic.sol.connection.Conexao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ParecerDAO {

	public static int insert(Parecer parecer) {
		return insert(parecer, null);
	}

	public static int insert(Parecer parecer, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ptc_parecer", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			parecer.setCdParecer(code);

			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_parecer (cd_parecer, cd_situacao_documento, nm_parecer, ds_parecer, tp_documento) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2, parecer.getCdSituacaoDocumento());
			pstmt.setString(3, parecer.getNmParecer());
			pstmt.setString(4, parecer.getDsParecer());
			pstmt.setInt(5, parecer.getTpDocumento());
			pstmt.executeUpdate();
			System.out.println(pstmt);
			return code;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParecerDAO.insert: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParecerDAO.insert: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Parecer parecer) {
		return update(parecer, null);
	}

	public static int update(Parecer parecer, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_parecer SET cd_parecer=?, cd_situacao_documento=?, nm_parecer=?, ds_parecer=?, tp_documento=? WHERE cd_parecer=?");
			pstmt.setInt(1, parecer.getCdParecer());
			pstmt.setInt(2, parecer.getCdSituacaoDocumento());
			pstmt.setString(3, parecer.getNmParecer());
			pstmt.setString(4, parecer.getDsParecer());
			pstmt.setInt(5, parecer.getTpDocumento());
			pstmt.setInt(6, parecer.getCdParecer());
			System.out.println(pstmt);
			pstmt.executeUpdate();
			return 1;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParecerDAO.update: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParecerDAO.update: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Parecer get(int cdParecer) {
		return get(cdParecer, null);
	}

	public static Parecer get(int cdParecer, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_parecer WHERE cd_parecer=?");
			pstmt.setInt(1, cdParecer);
			rs = pstmt.executeQuery();
			Parecer parecer = new Parecer();
			if (rs.next()) {
				parecer.setCdParecer(rs.getInt("cd_parecer"));
				parecer.setCdSituacaoDocumento(rs.getInt("cd_situacao_documento"));	
				parecer.setNmParecer(rs.getString("nm_parecer"));
				parecer.setDsParecer(rs.getString("ds_parecer"));
				parecer.setTpDocumento(rs.getInt("tp_documento"));
			} 
			return parecer;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParecerDAO.get: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParecerDAO.get: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdParecer) {
		return delete(cdParecer, null);
	}

	public static int delete(int cdParecer, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_parecer WHERE cd_parecer=?");
			pstmt.setInt(1, cdParecer);
			pstmt.executeUpdate();
			return 1;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParecerDAO.delete: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParecerDAO.delete: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static List<Parecer> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<Parecer> parecerList = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return parecerList;
		} finally {
			customConnection.closeConnection();
		}
	}

	public static List<Parecer> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Parecer> search = new SearchBuilder<Parecer>("ptc_parecer")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.log()
				.build();
		
		return search.getList(Parecer.class);
	}
}
