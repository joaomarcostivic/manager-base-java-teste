package com.tivic.manager.pcb;

import java.sql.Connection;
import java.sql.PreparedStatement;

import sol.dao.ResultSetMap;

import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;

@DestinationConfig(enabled = false)
public class LacreServices {

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_bomba FROM pcb_lacre A " + 
											 " JOIN pcb_bombas B ON(A.cd_bomba = B.cd_bomba)");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}
