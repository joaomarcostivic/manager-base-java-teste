package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import sol.dao.ResultSetMap;

import com.tivic.sol.connection.Conexao;

public class PessoaReferenciaServices {
	public static final String[] tipoReferencia = {"Pessoal",
		  "Comercial"};

	public static final int TP_PESSOAL   = 0;
	public static final int TP_COMERCIAL = 1;

	public static ResultSetMap getReferencias(int cdPessoa) {
		return getReferencias(cdPessoa,null);
	}

	public static ResultSetMap getReferencias(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_referencia WHERE cd_pessoa = "+ cdPessoa);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaReferenciaServices.getReferencias: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaReferenciaServices.getReferencias: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getReferencias(int cdPessoa, int tpReferencia) {
		return getReferencias(cdPessoa, tpReferencia,null);
	}

	public static ResultSetMap getReferencias(int cdPessoa, int tpReferencia, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_referencia WHERE cd_pessoa = "+ cdPessoa + " AND tp_referencia = " + tpReferencia);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaReferenciaServices.getReferencias: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaReferenciaServices.getReferencias: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
