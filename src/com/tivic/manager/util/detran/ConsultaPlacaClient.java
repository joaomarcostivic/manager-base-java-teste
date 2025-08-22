package com.tivic.manager.util.detran;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.conf.ManagerConf;

public class ConsultaPlacaClient {

    public static VeiculoDTO consultar(int cdOrgao, String nrPlaca) throws Exception {
    	String urlConsulta =  ManagerConf.getInstance().get("CONSULTA_PLACA_DETRAN_URL");
        int maxRetries = ManagerConf.getInstance().getAsInteger("CONSULTA_PLACA_MAX_RETRIES"); 
        int timeout =  ManagerConf.getInstance().getAsInteger("CONSULTA_PLACA_TIMEOUT");      
        int retryDelayMs =  ManagerConf.getInstance().getAsInteger("CONSULTA_PLACA_RETRY_DELAY_MS");

        String url = String.format(urlConsulta, cdOrgao, nrPlaca);
        int attempt = 0;
        Exception lastException = null;

        while (attempt <= maxRetries) {
            try {
            	Client client = ClientBuilder.newBuilder()
            			.connectTimeout(timeout, TimeUnit.SECONDS)
            			.readTimeout(timeout, TimeUnit.SECONDS)
            			.build();
            	WebTarget target = client.target(url);
            	
                String response = target.request(MediaType.APPLICATION_JSON).get(String.class);
                JSONObject jsonResponse = new JSONObject(response);

                if (jsonResponse.has("message")) {
                    throw new Exception(jsonResponse.getString("message"));
                }

                return new ObjectMapper().readValue(response, VeiculoDTO.class);
            } catch (Exception e) {
            	e.printStackTrace();
                lastException = e;
                if (++attempt > maxRetries) {
                    throw lastException;
                }
                Thread.sleep(retryDelayMs);
            }
        }

        throw lastException;
    }
  
}
