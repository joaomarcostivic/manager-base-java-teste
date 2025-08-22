package com.tivic.manager.eCarta;

import java.sql.Connection;
import java.util.HashMap;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;

public class GetParamnsBaseNova implements IGetParamns{
	
	public HashMap<String, Object> getParamns() {
		return getParamns(null);
	}
	
	public HashMap<String, Object> getParamns(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			} 
			HashMap<String, Object> paramns = new HashMap<>();
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
		finally {
			if (isConnectionNull){
				Conexao.desconectar(connect);
			}
		}
	}


}
