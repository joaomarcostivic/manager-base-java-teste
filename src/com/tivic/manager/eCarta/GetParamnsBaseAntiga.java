package com.tivic.manager.eCarta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import com.tivic.sol.connection.Conexao;

public class GetParamnsBaseAntiga implements IGetParamns{
	public HashMap<String, Object> getParamns() throws SQLException {
		return getParamns(null);
	}
	
	public HashMap<String, Object> getParamns(Connection connect) throws SQLException {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			} 
			HashMap<String, Object> paramns = new HashMap<>();
			PreparedStatement pstmt = connect.prepareStatement(
							"SELECT ECT_NR_CARTAO_POSTAGEM, ECT_NR_CORREIOS, ECT_SG_CORREIOS, mob_correios_matriz_nai, mob_correios_matriz_nip,"
							+ " mob_ecarta_envio, tp_nai, tp_nip " 
							+ " FROM PARAMETRO ");
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				paramns.put("MOB_CORREIOS_NR_CARTAO_POSTAGEM", rs.getString("ECT_NR_CARTAO_POSTAGEM"));
				paramns.put("MOB_CORREIOS_NR_CONTRATO", rs.getString("ECT_NR_CORREIOS"));
				paramns.put("MOB_CORREIOS_SG_CLIENTE", rs.getString("ECT_SG_CORREIOS"));
				paramns.put("mob_correios_matriz_nai", rs.getString("mob_correios_matriz_nai"));
				paramns.put("mob_correios_matriz_nip", rs.getString("mob_correios_matriz_nip"));
				paramns.put("mob_ecarta_envio", rs.getString("mob_ecarta_envio"));
				paramns.put("mob_impressao_tp_modelo_nai", rs.getString("tp_nai"));
				paramns.put("mob_impressao_tp_modelo_nip", rs.getString("tp_nip"));
			}
			return paramns;
		}
		finally {
			if (isConnectionNull){
				Conexao.desconectar(connect);
			}
		}
	}

}
