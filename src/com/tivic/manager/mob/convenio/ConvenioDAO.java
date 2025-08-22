package com.tivic.manager.mob.convenio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.tivic.sol.connection.Conexao;

public class ConvenioDAO {

	public static int insert(Convenio objeto, Connection connect) {
		try {
			int code = Conexao.getSequenceCode("mob_convenio", connect);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_convenio (cd_convenio, "
																					   + "nm_convenio, "
																					   + "tp_convenio, "
																					   + "lg_default) "
																					   + "VALUES (?,?,?)");
			pstmt.setInt(1, code);
			pstmt.setString(2, objeto.getNmConvenio());
			pstmt.setInt(3, objeto.getLgDefault());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConvenioDAO.insert: " + e);
			return -1;
		} 
	}
	
	public static int update(Convenio objeto, Connection connect) {
		try {
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_convenio SET cd_convenio=?, "
																					 + "nm_convenio=?, "
																					 + "tp_convenio=?, "
																					 + "lg_default=?  "
																					 + "WHERE cd_convenio=?");
			pstmt.setInt(1, objeto.getCdConvenio());
			pstmt.setString(2, objeto.getNmConvenio());
			pstmt.setInt(3, objeto.getLgDefault());
			pstmt.setInt(4, objeto.getCdConvenio());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConvenioDAO.update: " + e);
			return -1;
		} 
	}
	
	public static Convenio get(int cdConvenio, Connection connect) {
		ResultSet rs;
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM mob_convenio WHERE cd_convenio=?");
			pstmt.setInt(1, cdConvenio);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return new Convenio(
						rs.getInt("cd_convenio"),
						rs.getString("nm_convenio"),
						rs.getInt("tp_convenio"),
						rs.getInt("lg_default")
						);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConvenioDAO.get: " + e);
			return null;
		} 
	}
	
}
