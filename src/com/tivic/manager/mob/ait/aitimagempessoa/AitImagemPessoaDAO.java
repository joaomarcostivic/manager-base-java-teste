package com.tivic.manager.mob.ait.aitimagempessoa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AitImagemPessoaDAO {

	public static int insert(AitImagemPessoa objeto, Connection connect) {
		try {
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_ait_imagem_pessoa (cd_imagem, " +
															   "cd_ait_pessoa, " +
															   "blb_imagem, " +
															   "lg_impressao, " +
															   "tp_imagem) " +
															   "VALUES (?,?,?,?,?)");
			pstmt.setInt(1, objeto.getCdImagem());
			pstmt.setInt(2, objeto.getCdAitPessoa());
			pstmt.setBytes(4, objeto.getBlbImagem());
			pstmt.setInt(4, objeto.getLgImpressao());
			pstmt.setInt(5, objeto.getTpImagem());
			
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitImagemPessoaDAO.insert: " + e);
			return -1;
		} 
	}

	public static int update(AitImagemPessoa objeto, Connection connect) {
		try {
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_ait_imagem_pessoa SET " +
															   "cd_imagem=?," +
															   "cd_ait_pessoa=?, " +
															   "blb_imagem=?, " +
															   "lg_impressao=?, " +
															   "tp_imagem=? " +
															   "WHERE cd_imagem=?");
			pstmt.setInt(1, objeto.getCdImagem());
			pstmt.setInt(2, objeto.getCdAitPessoa());
			pstmt.setBytes(4, objeto.getBlbImagem());
			pstmt.setInt(4, objeto.getLgImpressao());
			pstmt.setInt(5, objeto.getTpImagem());
			pstmt.setInt(6, objeto.getCdImagem());
			
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitImagemPessoaDAO.update: " + e);
			return -1;
		} 
	}
	
	public static AitImagemPessoa get(int cdAitImagem, Connection connect) {
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_imagem_pessoa WHERE " +
											 "cd_ait_imagem=?");
			pstmt.setInt(1, cdAitImagem);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				AitImagemPessoa aitImagemPessoa = new AitImagemPessoa();
				aitImagemPessoa.setCdImagem(rs.getInt("cd_imagem"));
				aitImagemPessoa.setCdAitPessoa(rs.getInt("cd_ait_pessoa"));
				aitImagemPessoa.setBlbImagem(rs.getBytes("blb_imagem"));
				aitImagemPessoa.setLgImpressao(rs.getInt("lg_impressao"));
				return aitImagemPessoa;
			} else {
				return null;
			} 
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitImagemPessoaDAO.get: " + e);
			return null;
		} 
	}
}
