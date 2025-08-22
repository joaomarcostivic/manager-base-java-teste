package com.tivic.manager.crt;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.manager.conexao.*;
import com.tivic.sol.connection.Conexao;

public class TipoOperacaoServices {
	public static int insertOperacaoVinculo(int cdOperacao, int cdVinculo) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("INSERT INTO CRT_VINCULO_OPERACAO "+
		                                     "(CD_TIPO_OPERACAO, CD_VINCULO) VALUES (?,?)");
			pstmt.setInt(1, cdOperacao);
			pstmt.setInt(2, cdVinculo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoServices.insertOperacaoVinculo: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static int deleteOperacaoVinculo(int cdOperacao, int cdVinculo) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM CRT_VINCULO_OPERACAO "+
		                                     "WHERE CD_TIPO_OPERACAO  = ? "+
		                                     "  AND CD_VINCULO = ?");
			pstmt.setInt(1, cdOperacao);
			pstmt.setInt(2, cdVinculo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoServices.deleteOperacaoVinculo: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findOperacaoVinculo(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT * FROM CRT_VINCULO_OPERACAO A, GRL_VINCULO B "+
		                   "WHERE A.CD_VINCULO = B.CD_VINCULO ", criterios, Conexao.conectar());
	}
}