package com.tivic.manager.str.endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.tivic.sol.connection.Conexao;

public class EnderecoDAO {
	
	public static int insert(Endereco object, Connection connect) {
		try {
			int code = Conexao.getSequenceCode("grl_endereco", connect);
			object.setCdEndereco(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_endereco (cd_endereco," +
								 							   "cd_cidade," +
															   "ds_logradouro," +
								 							   "nr_cep," +
															   "nr_endereco," +
								 							   "ds_complemento," +
															   "nm_bairro) " +
								 							   "VALUES (?,?,?,?,?,?,?)");
			
			pstmt.setInt(1, code);
			pstmt.setInt(2, object.getCdCidade());
			pstmt.setString(3, object.getDsLogradouro());
			pstmt.setString(4, object.getNrCep());
			pstmt.setString(5, object.getNrEndereco());
			pstmt.setString(6, object.getDsComplemento());
			pstmt.setString(7, object.getNmBairro());
			System.out.println(pstmt);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EnderecoDAO.insert: " + e);
			return -1;
		} 
	}
	
	public static int update(Endereco object, Connection connect) {
		try {
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_endereco SET cd_endereco=?," +
								 							   "cd_cidade=?," +
															   "ds_logradouro=?," +
								 							   "nr_cep=?," +
															   "nr_endereco=?," +
								 							   "ds_complemento=?," +
															   "nm_bairro=? " +
								 							   "WHERE cd_endereco=?");
			
			pstmt.setInt(1, object.getCdEndereco());
			pstmt.setInt(2, object.getCdCidade());
			pstmt.setString(3, object.getDsLogradouro());
			pstmt.setString(4, object.getNrCep());
			pstmt.setString(5, object.getNrEndereco());
			pstmt.setString(6, object.getDsComplemento());
			pstmt.setString(7, object.getNmBairro());
			pstmt.setInt(8, object.getCdEndereco());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EnderecoDAO.insert: " + e);
			return -1;
		} 
	}
	
	public static Endereco get(int cdEndereco, Connection connect) {
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_endereco WHERE cd_endereco=?");
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return new Endereco(
						rs.getInt("cd_endereco"),
						rs.getInt("cd_cidade"),
						rs.getString("ds_logradouro"),
						rs.getString("nr_cep"),
						rs.getString("nr_endereco"),
						rs.getString("ds_complemento"),
						rs.getString("nm_bairro")
						);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EnderecoDAO.get: " + e);
			return null;
		} 
	}
}
