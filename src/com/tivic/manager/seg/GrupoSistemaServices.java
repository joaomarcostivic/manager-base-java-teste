package com.tivic.manager.seg;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class GrupoSistemaServices {
	public static ResultSetMap getModulosByGrupo(int cdGrupo) {
		return getModulosByGrupo(cdGrupo, null);
	}

	public static ResultSetMap getModulosByGrupo(int cdGrupo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.CD_GRUPO, B.LG_NATUREZA " +
											 " FROM SEG_MODULO A " +
											 " LEFT OUTER JOIN SEG_GRUPO_MODULO B ON (A.CD_MODULO = B.CD_MODULO AND B.CD_GRUPO = ?) " +
											 " WHERE A.LG_ATIVO = 1" +
											 " ORDER BY NM_MODULO");
			pstmt.setInt(1, cdGrupo);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! GrupoModuloServices.getModulosByGrupo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public int addPermissaoModulo(int cdGrupo, int cdModulo, int cdSistema, int lgNatureza) {
		return addPermissaoModulo(cdGrupo, cdModulo, cdSistema, lgNatureza, null);
	}

	public int addPermissaoModulo(int cdGrupo, int cdModulo, int cdSistema, int lgNatureza, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					     "UPDATE seg_grupo_modulo SET lg_natureza = " +lgNatureza+
						 " WHERE CD_GRUPO = " +cdGrupo+
						 "   AND CD_SISTEMA = " +cdSistema+
						 "   AND CD_MODULO  = " +cdModulo);
			if(pstmt.executeUpdate()<=0) {
				pstmt = connect.prepareStatement("INSERT INTO SEG_GRUPO_MODULO (CD_GRUPO, CD_SISTEMA, CD_MODULO, LG_NATUREZA) VALUES(?, ?, ?, ?)");
				pstmt.setInt(1, cdGrupo);
				pstmt.setInt(2, cdSistema);
				pstmt.setInt(3, cdModulo);
				pstmt.setInt(4, lgNatureza);
				pstmt.executeUpdate();
			}
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public int dropPermissaoModulo(int cdGrupo, int cdModulo, int cdSistema) {
		return dropPermissaoModulo(cdGrupo, cdModulo, cdSistema, null);
	}
	public int dropPermissaoModulo(int cdGrupo, int cdModulo, int cdSistema, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					                 "DELETE FROM SEG_GRUPO_MODULO " +
									 "WHERE CD_GRUPO = " + cdGrupo +
									 "  AND CD_SISTEMA = " + cdSistema +
									 "  AND CD_MODULO  = " + cdModulo);
			return pstmt.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
