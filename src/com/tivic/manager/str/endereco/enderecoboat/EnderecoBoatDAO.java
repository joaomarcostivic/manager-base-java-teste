package com.tivic.manager.str.endereco.enderecoboat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EnderecoBoatDAO {

	public static int insert(EnderecoBoat object, Connection connect) {
		try {
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_endereco_boat (cd_endereco, cd_boat)" +
																							  " VALUES (?,?)");
			pstmt.setInt(1, object.getCdEndereco());
			pstmt.setInt(2, object.getCdBoat());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EnderecoBoatDAO.insert: " + e);
			return -1;
		} 
	}
	
	public static EnderecoBoat get(int cdEndereco, int cdBoat, Connection connect) {
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_endereco_boat WHERE cd_endereco=? AND cd_boat=?");
			pstmt.setInt(1, cdEndereco);
			pstmt.setInt(2, cdBoat);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return new EnderecoBoat(
						rs.getInt("cd_endereco"),
						rs.getInt("cd_boat")
						);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EnderecoBoatDAO.get: " + e);
			return null;
		} 
	}
}
