package com.tivic.manager.crm;

import java.sql.Connection;
import java.sql.PreparedStatement;

import sol.dao.ResultSetMap;

import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;

@DestinationConfig(enabled = false)
public class TipoNecessidadeServices {
	public static ResultSetMap getByAtendimento(int cdAtendimento) {
		return getByAtendimento(cdAtendimento, null);
	}

	public static ResultSetMap getByAtendimento(int cdAtendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSetMap rsm = new ResultSetMap();
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.vl_necessidade FROM crm_tipo_necessidade A JOIN crm_atendimento_necessidade B ON (A.cd_tipo_necessidade = B.cd_tipo_necessidade) WHERE B.cd_atendimento=?");
			pstmt.setInt(1, cdAtendimento);
			rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoNecessidadeServices.getByAtendimento: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
