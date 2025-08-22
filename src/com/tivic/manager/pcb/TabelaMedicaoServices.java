package com.tivic.manager.pcb;

import java.sql.Connection;
import java.sql.ResultSet;

import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

@DestinationConfig(enabled = false)
public class TabelaMedicaoServices {
	public static float getVlLitrosOf(int cdTipoTanque, float vlCm)	{
		return getVlLitrosOf(cdTipoTanque, vlCm, null);
	}
	
	public static float getVlLitrosOf(int cdTipoTanque, float vlCm, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSet rs = connect.prepareStatement("SELECT vl_litros FROM pcb_tabela_medicao " +
					                               "WHERE cd_tipo_tanque = "+cdTipoTanque+
					                               "  AND vl_cm          = "+vlCm).executeQuery();
			if(rs.next())
				return rs.getFloat("vl_litros");
			return -1;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return -2;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}


}
