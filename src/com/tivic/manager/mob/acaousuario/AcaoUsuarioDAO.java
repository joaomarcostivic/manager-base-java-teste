package com.tivic.manager.mob.acaousuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;

import com.tivic.sol.connection.Conexao;
import sol.dao.Util;

public class AcaoUsuarioDAO {
	
	public static int insert(AcaoUsuario objeto, Connection connect) {
		try {
			
			int code = Conexao.getSequenceCode("mob_acao_usuario", connect);
			objeto.setCdAcao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_acao_usuario " +
															   "(cd_acao," +
															   "cd_usuario," +
															   "ds_acao," +
															   "dt_acao) VALUES(?,?,?,?)");
			
			pstmt.setInt(1, code);
			pstmt.setInt(2, objeto.getCdUsuario());
			pstmt.setString(3, objeto.getDsAcao());
			if(objeto.getDtAcao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4, new Timestamp(objeto.getDtAcao().getTimeInMillis()));

			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcaoUsuarioDAO.insert: " + e);
			return -1;
		}
	}
	
	public static int update(AcaoUsuario objeto, Connection connect) {
		try {
			
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_acao_usuario " +
															   "cd_acao=?," +
															   "cd_usuario=?," +
															   "ds_acao=?," +
															   "dt_acao=? WHERE cd_acao=?");
			
			pstmt.setInt(1, objeto.getCdAcao());
			pstmt.setInt(2, objeto.getCdUsuario());
			pstmt.setString(3, objeto.getDsAcao());
			if(objeto.getDtAcao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4, new Timestamp(objeto.getDtAcao().getTimeInMillis()));
			pstmt.setInt(5, objeto.getCdAcao());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcaoUsuarioDAO.update: " + e);
			return -1;
		}
	}
	
	public static AcaoUsuario get(int cdAcao, Connection connect) {
		ResultSet rs;
		try {
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM mob_acao_usuario WHERE cd_acao=?");
			pstmt.setInt(1, cdAcao);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				AcaoUsuario acaoUsuario = new AcaoUsuario();
				acaoUsuario.setCdAcao(rs.getInt("cd_acao"));
				acaoUsuario.setCdUsuario(rs.getInt("cd_usuario"));
				acaoUsuario.setDsAcao(rs.getString("ds_acao"));
				acaoUsuario.setDtAcao(rs.getTimestamp("dt_acao")==null?null:Util.longToCalendar(rs.getTimestamp("dt_acao").getTime()));
				return acaoUsuario;
			} else {
				return null;
			}
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcaoUsuarioDAO.get: " + e);
			return null;
		} 
	}
}
