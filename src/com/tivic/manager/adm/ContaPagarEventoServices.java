package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class ContaPagarEventoServices {

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.id_evento_financeiro, B.nm_evento_financeiro " +
				           "FROM adm_conta_pagar_evento A " +
				           "JOIN adm_evento_financeiro B ON (A.cd_evento_financeiro = B.cd_evento_financeiro) ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static int deleteAll(int cdContaPagar) {
		return deleteAll(cdContaPagar, null);
	}

	public static int deleteAll(int cdContaPagar, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE " +
					"FROM adm_conta_pagar_evento " +
					"WHERE cd_conta_pagar=?");
			pstmt.setInt(1, cdContaPagar);
			pstmt.execute();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarEventoServices.deleteAll: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}