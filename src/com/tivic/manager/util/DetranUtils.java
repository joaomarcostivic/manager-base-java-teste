package com.tivic.manager.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.net.HttpURLConnection;

import org.json.JSONObject;
import org.json.XML;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.conf.ManagerConf;

public class DetranUtils {

	private String _response;

	public JSONObject asJSON() {
		try {
			if (this._response == null || this._response.trim().equals("")) {
				return null;
			}
			
			JSONObject json = XML.toJSONObject(this._response);

			if (!json.has("SOAP-ENV:Envelope")) {
				return null;
			}
			
			json = json.getJSONObject("SOAP-ENV:Envelope")
			.getJSONObject("SOAP-ENV:Body")
			.getJSONObject("consultaVeiculoDadosProprietResponse")
			.getJSONObject("consultaVeiculoDadosProprietResult");

			@SuppressWarnings("unchecked")
			Iterator<String> keys = json.keys();
			
			while(keys.hasNext()) {
				String key = keys.next();
			    if (json.get(key) instanceof String && json.getString(key).trim().equals("")) {
			    	json.put(key, JSONObject.NULL);
			    }
			}
			
			return json;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public HashMap<String, Object> asHashMap() {
		try {
			if (this._response == null || this._response.trim().equals("")) {
				throw new Exception("Nenhuma busca de veículo foi feita.");
			}
			
			JSONObject json = XML.toJSONObject(this._response)
					.getJSONObject("soap:Envelope")
					.getJSONObject("soap:Body")
					.getJSONObject("consultaVeiculoDadosProprietResponse")
					.getJSONObject("consultaVeiculoDadosProprietResult");			

			@SuppressWarnings("unchecked")
			Iterator<String> keys = json.keys();
			
			while(keys.hasNext()) {
				String key = keys.next();
			    if (json.get(key) instanceof String && json.getString(key).trim().equals("")) {
			    	json.put(key, JSONObject.NULL);
			    }
			}
			
			HashMap<String, Object> hashMap = new ObjectMapper().readValue(json.toString(), new TypeReference<HashMap<String, Object>>(){});
			
			return hashMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public DetranUtils getVeiculo(String nrPlaca) throws IOException, Exception {
		String url = ManagerConf.getInstance().get("URL_DETRAN"); 
		
		isLinkFunctional(url);
		

		
		InputStream is = new URL(url + nrPlaca).openStream();

		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			this._response = readAll(rd);

			return this;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			is.close();
		}
	}

	public static void isLinkFunctional(String urlStr) throws Exception {
        try {
    		if(urlStr.isEmpty()) {
    			throw new Exception("A variavel de ambiente 'URL_DETRAN' está VAZIA");	
    		}
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if(responseCode != 200)
            	throw new Exception("O link da variavel de ambiente 'URL_DETRAN' é INVALIDA");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	@SuppressWarnings("unused")
	private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}

		return sb.toString();
	}

}
