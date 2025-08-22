package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class ContratoEventoServices {
	public static final String[] tipoRepeticao = {"Valor Geral", "Valor Individual"};

	public static final String[] tipoCalculo = {"% Valor Geral", "Valor Específico"};

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.id_evento_financeiro, B.nm_evento_financeiro " +
				           "FROM adm_contrato_evento A " +
				           "JOIN adm_evento_financeiro B ON (A.cd_evento_financeiro = B.cd_evento_financeiro) ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static int deleteAll(int cdContrato) {
		return deleteAll(cdContrato, null);
	}

	public static int deleteAll(int cdContrato, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_contrato_evento WHERE cd_contrato=?");
			pstmt.setInt(1, cdContrato);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoEventoServices.deleteAll: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoEventoServices.deleteAll: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
