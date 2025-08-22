package com.tivic.manager.mob.orgaoaquivo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class OrgaoArquivoDAO {
	public static int insert(OrgaoArquivo orgaoArquivo, Connection connect) {
		try {
			int code = Conexao.getSequenceCode("mob_orgao_arquivo", connect);
			if(code <= 0) {
				return -1;
			}
			orgaoArquivo.setCdOrgaoArquivo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_orgao_arquivo (cd_orgao_arquivo, "
																							+ "cd_orgao,"
																							+ "cd_arquivo,"
																							+ "tp_arquivo_documento) VALUES (?,?,?,?)");
			pstmt.setInt(1, orgaoArquivo.getCdOrgaoArquivo());
			pstmt.setInt(2, orgaoArquivo.getCdOrgao());
			pstmt.setInt(3, orgaoArquivo.getCdArquivo());
			pstmt.setInt(4, orgaoArquivo.getTpArquivoDocumento());
			pstmt.executeUpdate();
			return code;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro ao salvar OrgaoArquivo");
			return -1;
		}
	}
	public static int update(OrgaoArquivo orgaoArquivo, Connection connect) {
		try {
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_orgao_arquivo SET cd_orgao_arquivo=?, "
																							+"cd_orgao=?,"
																							+"cd_arquivo=?,"
																							+"tp_arquivo_documento=?"
																							+"WHERE cd_orgao_arquivo=?");
			pstmt.setInt(1, orgaoArquivo.getCdOrgaoArquivo());
			pstmt.setInt(2, orgaoArquivo.getCdOrgao());
			pstmt.setInt(3, orgaoArquivo.getCdArquivo());
			pstmt.setInt(4, orgaoArquivo.getTpArquivoDocumento());
			pstmt.setInt(5, orgaoArquivo.getCdOrgaoArquivo());
			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro ao salvar OrgaoArquivo");
			return -1;
		}
	}
	
	public static OrgaoArquivo get(int cdOrgaoArquivo) {
		return get(cdOrgaoArquivo, null);
	}
	
	public static OrgaoArquivo get(int cdOrgaoArquivo, Connection connect) {
		boolean isConnectionNull = connect == null;
		if(isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_orgao_arquivo WHERE cd_orgao_arquivo=?");
			pstmt.setInt(1, cdOrgaoArquivo);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				OrgaoArquivo orgaoArquivo = new OrgaoArquivo();
				orgaoArquivo.setCdOrgaoArquivo(rs.getInt("cd_orgao_arquivo"));
				orgaoArquivo.setCdOrgao(rs.getInt("cd_orgao"));
				orgaoArquivo.setCdArquivo(rs.getInt("cd_arquivo"));
				orgaoArquivo.setTpArquivoDocumento(rs.getInt("tp_arquivo"));
				return orgaoArquivo;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoArquivoDAO: "+ e);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
		
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM mob_orgao_arquivo", criterios, connect != null ? connect : Conexao.conectar(), connect == null);
	}
}
