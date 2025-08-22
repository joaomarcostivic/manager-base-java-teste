package com.tivic.manager.mob.grafica;

import java.sql.Connection;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.ParametroServices;

public class LoteImpressaoGetParamns {

	public HashMap<String, Object> getParamnsFtp(Connection connect) {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		boolean isConnectionNull = connect==null;
		
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
				 
			HashMap<String, Object> paramns = new HashMap<>();
			
			if (lgBaseAntiga)
			{
				
				
				paramns.put("mob_correios_nm_url_ftp", ParametroServices.getValorOfParametro("ds_ftp_correios", connect));
				paramns.put("mob_correios_nm_usuario_ftp_correios", ParametroServices.getValorOfParametro("nm_login_correios", connect));
				paramns.put("mob_correios_nm_senha_ftp_correios", ParametroServices.getValorOfParametro("ds_senha_correios", connect));
				
				return paramns;
			}
			else
			{
				
				paramns.put("mob_correios_nm_url_ftp", ParametroServices.getValorOfParametro("mob_correios_nm_url_ftp", connect));
				paramns.put("mob_correios_nm_usuario_ftp_correios", ParametroServices.getValorOfParametro("mob_correios_nm_usuario_ftp_correios", connect));
				paramns.put("mob_correios_nm_senha_ftp_correios", ParametroServices.getValorOfParametro("mob_correios_nm_senha_ftp_correios", connect));
				return paramns;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
}
