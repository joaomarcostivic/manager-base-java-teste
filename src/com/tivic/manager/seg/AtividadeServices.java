package com.tivic.manager.seg;

import java.sql.*;

import com.tivic.manager.conexao.*;
import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class AtividadeServices {

	public static ResultSetMap loadPermissaoByUser(int cdUsuario,int cdSistema){
		return loadPermissaoByUser(cdUsuario,cdSistema,null);
	}
	
	public static ResultSetMap loadPermissaoByUser(int cdUsuario,int cdSistema,Connection connect)
	{
		
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT B.id_atividade FROM seg_permissao_usuario A " +
											 "JOIN seg_atividade B ON(A.cd_atividade = B.cd_atividade) " +
											 "WHERE A.cd_usuario=? AND A.cd_sistema=?");
			pstmt.setInt(1, cdUsuario);
			pstmt.setInt(2, cdSistema);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PermissaoUsuarioDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
