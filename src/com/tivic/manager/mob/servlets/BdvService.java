package com.tivic.manager.mob.servlets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.bdv.Veiculo;
import com.tivic.manager.util.Util;

public class BdvService {
	
	ArquivoConfiguracaoDetranServlet arquivoConfiguracaoDetranServlet;
	
	public BdvService() {
		arquivoConfiguracaoDetranServlet = new ArquivoConfiguracaoDetranServlet();
	}
	
	public void inserirVeiculo(Veiculo veiculo) throws MalformedURLException, IOException, Exception {
		HttpURLConnection http = createConnection("POST");
		save(http, veiculo);
	}

	public void updateVeiculo(Veiculo veiculo) throws MalformedURLException, IOException, Exception{
		HttpURLConnection http = createConnection("PUT", "/".concat(String.valueOf(veiculo.getCdVeiculo())));
		save(http, veiculo);
	}
	
	private HttpURLConnection createConnection(String httpMethod) throws MalformedURLException, IOException {
		return createConnection(httpMethod, "");
	}
	
	private HttpURLConnection createConnection(String httpMethod, String path) throws MalformedURLException, IOException {
		URL url = new URL(arquivoConfiguracaoDetranServlet.getUrlBdv() + path);
		URLConnection con = url.openConnection();
		HttpURLConnection http = (HttpURLConnection)con;
		http.setRequestMethod(httpMethod); 
		http.setDoInput(true);
		http.setDoOutput(true);
		http.addRequestProperty("Content-Type", "application/json");
		return http;
	}
	
	private void save(HttpURLConnection http, Veiculo veiculo) throws IOException, Exception {
		OutputStream out = http.getOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
		writer.write(veiculo.toString());
		writer.flush();
		writer.close();
		out.close();
		if (http.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new Exception("Erro ao se conectar ao BDV: " + http.getResponseMessage());
		}
	}
	
	public Veiculo consultaVeiculo(String nrPlaca) throws MalformedURLException, IOException, Exception{
		HttpURLConnection http = createConnection("GET", "/consulta/".concat(nrPlaca));
		return get(http);
	}

	private Veiculo get(HttpURLConnection http) throws IOException, Exception {
		String resultado = getResponseData(http);
		if (http.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new Exception("Erro ao se conectar ao BDV: " + http.getResponseMessage());
		}
		return convertData(resultado);
	}
	
	private String getResponseData(HttpURLConnection http) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}
	
	private Veiculo convertData(String veiculoString) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper.readValue(veiculoString, Veiculo.class);
	}
	
}
