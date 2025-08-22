package com.tivic.manager.srh;

import java.sql.*;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.*;

public class MatriculaMovimentacaoServices	{

	public static ResultSetMap find(int cdMatricula) {
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		crt.add(new ItemComparator("A.cd_matricula", String.valueOf(cdMatricula), ItemComparator.EQUAL, Types.INTEGER));
		return find(crt, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.id_tipo_movimentacao, B.nm_tipo_movimentacao " +
				           "FROM srh_matricula_movimentacao A " +
				           "JOIN srh_tipo_movimentacao B ON (A.cd_tipo_movimentacao = B.cd_tipo_movimentacao)", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}