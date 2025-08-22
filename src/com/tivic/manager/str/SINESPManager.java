package com.tivic.manager.str;

import org.json.JSONObject;
import org.lucassouza.jsinespclient.SinespClient;

import com.tivic.manager.grl.ParametroServices;

import sol.util.Result;

/**
 * Trata das consultas a base de dados do SINESP.
 * 
 * @author mauricio
 * @version 0.0.2
 * @since 27/08/2018
 */
public class SINESPManager {
	
	/**
	 * Consulta informações na base do SINESP
	 * @param placa
	 * @return json com os dados do veículo
	 * 
	 * @see <a href="https://github.com/Sorackb/JSinespClient">JSinespClient</a>
	 * @disclaimer envelopa a solução JSinespClient, com algumas adaptações
	 */
	public static Result consultar(String placa) {
		
		try {
			
			//chave de acesso do app
			String chave = ParametroServices.getValorOfParametroAsString("STR_CHAVE_APP_SINESP", null);
			
			//executa consulta
			JSONObject json = new JSONObject(SinespClient.search(placa.replaceAll("-", ""), chave).toJSON());
	    		    	
			//parser do resultado
	    	JSONObject jsonResponse = new JSONObject();
	    	jsonResponse.put("codigo", json.getInt("returnCode"));
	    	jsonResponse.put("mensagem", json.getString("returnMessage"));
	    	jsonResponse.put("status", json.getInt("statusCode"));
	    	jsonResponse.put("statusMensagem", json.get("statusMessage"));
	    	jsonResponse.put("modelo", json.getString("model"));
	    	jsonResponse.put("marca", json.getString("brand"));
	    	jsonResponse.put("cor", json.getString("color"));
	    	jsonResponse.put("ano", json.getInt("year"));
	    	jsonResponse.put("anoModelo", json.getInt("modelYear"));
	    	jsonResponse.put("placa", json.getString("plate"));
	    	jsonResponse.put("data", json.getString("date"));
	    	jsonResponse.put("uf", json.getString("state"));
	    	jsonResponse.put("municipio", json.getString("city"));
	    	jsonResponse.put("chassi", "************"+json.getString("vinCode"));
	    	
	    	return new Result(1, "Consulta realizada com sucesso!", "response", jsonResponse);
		}
		catch(RuntimeException runtimeException) {
			return new Result(-2, "Erro na execução da consulta.", "response", "{\"codigo\":-2,\"erro\":\"Erro na execução da consulta.\",\"message\":\""+runtimeException.getMessage()+"\"}");
		}
		catch(Exception e) {
			return new Result(-3, "Erro na execução da consulta.", "response", "{\"codigo\":-3,\"erro\":\"Erro.\",\"message\":\""+e.getMessage()+"\"}");
		}
	}
}
