package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class UnidadeConversaoServices {

	public static final int OP_ADICAO = 0;
	public static final int OP_SUBTRACAO = 1;
	public static final int OP_MULTIPLICACAO = 2;
	public static final int OP_DIVISAO = 3;
	public static final int OP_EXPONENCIACAO = 4;

	public static final String[] tipoOperacaoConversao = {"Adição",
														  "Subtração",
														  "Multiplicação",
														  "Divisão",
														  "Exponenciação"};

	public static ResultSetMap getConversaoUnidadeMedida(int cdUnidadeOrigem, int cdUnidadeDestino) {
		return getConversaoUnidadeMedida(cdUnidadeOrigem, cdUnidadeDestino, null);
	}

	public static ResultSetMap getConversaoUnidadeMedida(int cdUnidadeOrigem, int cdUnidadeDestino, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_unidade_conversao "  +
					 "WHERE cd_unidade_origem = ? " +
					 "	AND cd_unidade_destino = ? ");
			pstmt.setInt(1, cdUnidadeOrigem);
			pstmt.setInt(2, cdUnidadeDestino);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeConversaoServices.getConversaoUnidadeMedida: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllUnidadeDestino(int cdUnidadeOrigem) {
		return getAllUnidadeDestino(cdUnidadeOrigem, null);
	}

	public static ResultSetMap getAllUnidadeDestino(int cdUnidadeOrigem, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT DISTINCT A.*, B.cd_unidade_origem " +
					"FROM grl_unidade_medida A, grl_unidade_conversao B "  +
					"WHERE (A.cd_unidade_medida = B.cd_unidade_destino) " +
					"	AND (B.cd_unidade_origem = ?) " +
					"UNION " +
					"SELECT distinct A.*, A.cd_unidade_medida AS cd_unidade_origem " +
					"FROM grl_unidade_medida A " +
					"LEFT OUTER JOIN grl_unidade_conversao B " +
					"	ON (A.cd_unidade_medida = B.cd_unidade_destino) " +
					"WHERE (A.cd_unidade_medida = ?) ");
			pstmt.setInt(1, cdUnidadeOrigem);
			pstmt.setInt(2, cdUnidadeOrigem);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeConversaoServices.getAllUnidadeDestino: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
