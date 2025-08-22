package com.tivic.manager.pcb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

@DestinationConfig(enabled = false)
public class BombasServices {

	public static ResultSetMap getAll() {
		return BombasDAO.getAll();
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		ResultSetMap rsm = BombasDAO.find(criterios);
		while(rsm.next()){
			rsm.setValueToField("CL_BOMBA", rsm.getString("NM_BOMBA"));
		}
		rsm.beforeFirst();
		return rsm;
	}
	
	public static ResultSetMap getAllLacres(int cdBomba) {
		return getAllLacres(cdBomba, null);
	}

	public static ResultSetMap getAllLacres(int cdBomba, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement(
					 "SELECT A.* FROM pcb_lacre    A " +
					 "WHERE A.cd_bomba = ?");		
			pstmt.setInt(1, cdBomba);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BombasServices.getAllLacres: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}