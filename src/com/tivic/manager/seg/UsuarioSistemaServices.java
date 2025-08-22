package com.tivic.manager.seg;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class UsuarioSistemaServices {
	public static ResultSetMap getModulosByUsuario(int cdUsuario) {
		return getModulosByUsuario(cdUsuario, null);
	}

	public static ResultSetMap getModulosByUsuario(int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT DISTINCT(A.*), B.CD_USUARIO, B.LG_NATUREZA, C.LG_NATUREZA AS LG_NATUREZA_GRUPO " +
											 " FROM SEG_MODULO A " +
											 " LEFT OUTER JOIN SEG_USUARIO_MODULO B ON (A.CD_MODULO = B.CD_MODULO AND B.CD_USUARIO = ?) " +
											 " LEFT OUTER JOIN SEG_GRUPO_MODULO C ON (A.CD_MODULO = C.CD_MODULO AND C.CD_GRUPO IN (SELECT CD_GRUPO FROM SEG_USUARIO_GRUPO WHERE CD_USUARIO = ?)) " +
											 " WHERE A.LG_ATIVO = 1" +
											 " ORDER BY NM_MODULO, C.LG_NATUREZA DESC");
			pstmt.setInt(1, cdUsuario);
			pstmt.setInt(2, cdUsuario);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! UsuarioModuloServices.getModulosByUsuario: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
