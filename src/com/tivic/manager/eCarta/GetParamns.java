package com.tivic.manager.eCarta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.ParametroServices;

public class GetParamns {

	
	public static  HashMap<String, Object> getParamns() {
		return getParamns(null);
	}
	
	public static HashMap<String, Object> getParamns(Connection connect) {
		boolean lgOldBase = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			} 
			HashMap<String, Object> paramns = new HashMap<>();
			if (lgOldBase){
				PreparedStatement pstmt = connect.prepareStatement(
								"SELECT ECT_NR_CARTAO_POSTAGEM, ECT_NR_CORREIOS, ECT_sg_correios, mob_correios_matriz_nai, mob_correios_matriz_nip,"
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
			
			paramns.put("MOB_CORREIOS_NR_CARTAO_POSTAGEM", ParametroServices.getValorOfParametro("MOB_CORREIOS_NR_CARTAO_POSTAGEM", connect));
			paramns.put("MOB_CORREIOS_NR_CONTRATO", ParametroServices.getValorOfParametro("MOB_CORREIOS_NR_CONTRATO", connect));
			paramns.put("MOB_CORREIOS_SG_CLIENTE", ParametroServices.getValorOfParametro("MOB_CORREIOS_SG_CLIENTE", connect));
			paramns.put("mob_correios_matriz_nai", ParametroServices.getValorOfParametro("mob_correios_matriz_nai", connect));
			paramns.put("mob_correios_matriz_nip", ParametroServices.getValorOfParametro("mob_correios_matriz_nip", connect));
			paramns.put("mob_ecarta_envio", ParametroServices.getValorOfParametro("mob_ecarta_envio", connect));
			paramns.put("mob_impressao_tp_modelo_nai", ParametroServices.getValorOfParametro("mob_impressao_tp_modelo_nai", connect));
			paramns.put("mob_impressao_tp_modelo_nip", ParametroServices.getValorOfParametro("mob_impressao_tp_modelo_nip", connect));
			
			return paramns;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			if (isConnectionNull){
				Conexao.desconectar(connect);
			}
		}
	}
}
