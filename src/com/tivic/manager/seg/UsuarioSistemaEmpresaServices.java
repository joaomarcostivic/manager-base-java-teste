package com.tivic.manager.seg;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.EmpresaServices;

import sol.dao.ResultSetMap;

public class UsuarioSistemaEmpresaServices {

	public static ResultSetMap getAllEmpresasForUsuarioModulo(int cdUsuario, String idModulo) {
		return getAllEmpresasForUsuarioModulo(cdUsuario, idModulo, null);
	}

	public static ResultSetMap getAllEmpresasForUsuarioModulo(int cdUsuario, String idModulo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			Usuario usuario = UsuarioDAO.get(cdUsuario, connect);
			if (usuario.getTpUsuario() == UsuarioServices.ADMINISTRADOR)
				return EmpresaServices.getAll(connect);
			else {
				PreparedStatement pstmt = connect.prepareStatement("SELECT A.cd_empresa, B.cd_empresa AS LG_EMPRESA, " +
						"C.nm_pessoa AS nm_empresa, C.nm_pessoa AS nm_fantasia " +
						"FROM grl_empresa A, seg_usuario_modulo_empresa B, grl_pessoa C, seg_modulo D " +
						"WHERE A.cd_empresa = B.cd_empresa " +
						"  AND A.cd_empresa = C.cd_pessoa " +
						"  AND B.cd_modulo = D.cd_modulo " +
						"  AND B.cd_sistema = D.cd_sistema " +
						"  AND D.id_modulo = ? " +
						"  AND B.cd_usuario = ?");
				pstmt.setString(1, idModulo);
				pstmt.setInt(2, cdUsuario);
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				return rsm.size()>0 ? rsm : EmpresaServices.getAll(connect);
			}
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! UsuarioServices.getAllEmpresasForUsuario: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}
