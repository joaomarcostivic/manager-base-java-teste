package com.tivic.manager.ptc.documentopessoa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.tivic.manager.ptc.DocumentoPessoa;

public class DocumentoPessoaDAO {

	public static int insert(DocumentoPessoa documentoPessoa, Connection connect) {
		try {
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_documento_pessoa (cd_documento, "
																							   + "cd_pessoa, "
																							   + "nm_qualificacao) "
																							   + "VALUES (?,?,?)");
			pstmt.setInt(1, documentoPessoa.getCdDocumento());
			pstmt.setInt(2, documentoPessoa.getCdPessoa());
			pstmt.setString(3, documentoPessoa.getNmQualificacao());
			
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoPessoa.insert: " + e);
			return -1;
		}
	}
	
	public static int update(DocumentoPessoa documentoPessoa, Connection connect) {
		try {
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_documento_pessoa SET cd_documento=?, " 
																							 + "cd_pessoa=?, "
																							 + "nm_qualificacao=? "
																							 + "WHERE cd_documento=? AND cd_pessoa=?");
			pstmt.setInt(1, documentoPessoa.getCdDocumento());
			pstmt.setInt(2, documentoPessoa.getCdPessoa());
			pstmt.setString(3, documentoPessoa.getNmQualificacao());
			pstmt.setInt(4, documentoPessoa.getCdDocumento());
			pstmt.setInt(5, documentoPessoa.getCdPessoa());
			return pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoPessoa.update: " + e);
			return -1;
		}
	}
	
	public static DocumentoPessoa get(int cdDocumento, int cdPessoa, Connection connect) {
		ResultSet rs;
		try {			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM ptc_documento_pessoa WHERE cd_documento=? AND cd_pessoa=? ");
			pstmt.setInt(1, cdDocumento);
			pstmt.setInt(2, cdPessoa);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				DocumentoPessoa documentoPessoa = new DocumentoPessoa();
				documentoPessoa.setCdDocumento(rs.getInt("cd_documento"));
				documentoPessoa.setCdPessoa(rs.getInt("cd_pessoa"));
				documentoPessoa.setNmQualificacao(rs.getString("nm_qualificacao"));
				return documentoPessoa;
			} else {
				return null;
			}
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoPessoa.get: " + e);
			return null;
		}
	}
}
