package com.tivic.manager.str;

import java.sql.Connection;
import java.sql.PreparedStatement;

import sol.dao.ResultSetMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.util.Util;

public class InfracaoObservacaoServices {
	public static ResultSetMap getSyncData() {
		return getSyncData(null);
	}

	public static ResultSetMap getSyncData(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			boolean str2mob = ManagerConf.getInstance().getAsBoolean("STR_TO_MOB");
			if(str2mob) {
				return com.tivic.manager.mob.InfracaoObservacaoServices.getSyncData(connect);
			}
			
			String sql = "SELECT * FROM str_infracao_observacao";
			
			if(Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1"))
				sql = "SELECT A.*, A.cod_infracao as cd_infracao FROM str_infracao_observacao A";
			pstmt = connect.prepareStatement(sql);
			
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
