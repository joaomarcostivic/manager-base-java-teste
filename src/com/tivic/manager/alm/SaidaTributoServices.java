package com.tivic.manager.alm;

import java.sql.Connection;
import java.sql.ResultSet;

import sol.dao.ResultSetMap;

import com.tivic.sol.connection.Conexao;

public class SaidaTributoServices {

	public static ResultSetMap getAllByDocumentoSaida(int cdDocumentoSaida, Connection connect)	{
		boolean isConnNull = connect==null;
		try	{
			connect = isConnNull ? Conexao.conectar() : connect;
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM adm_saida_tributo WHERE cd_documento_saida = "+cdDocumentoSaida).executeQuery());
			
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnNull)
				Conexao.desconectar(connect);
		}
	}
	
}
