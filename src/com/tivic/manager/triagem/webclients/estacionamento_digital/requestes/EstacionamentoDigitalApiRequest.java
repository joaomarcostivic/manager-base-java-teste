package com.tivic.manager.triagem.webclients.estacionamento_digital.requestes;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tivic.manager.conf.ManagerConf;
import io.jsonwebtoken.io.IOException;

public class EstacionamentoDigitalApiRequest<T> {

	private String baseUrl;
    private URL url;
    private HttpURLConnection connection;
    private HashMap<String, String> params;
    private String token;
    private String endPoint;

    public EstacionamentoDigitalApiRequest() throws IOException, MalformedURLException {
    	this.baseUrl = ManagerConf.getInstance().get("URL_ZONA_AZUL");
    	this.token = ManagerConf.getInstance().get("TOKEN_ZONA_AZUL");
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }
    
    public String getEndPoint() {
    	return this.endPoint;
    }
    
    public void setEndPoint(String endPoint) {
    	this.endPoint = endPoint;
    }
    
    public List<T> getNotificacoes() throws IOException, java.io.IOException {
    	this.url =  new URL(this.baseUrl + "/" + this.endPoint);
    	this.initRequest();
    	return parseJsonArrayToNotificacoes(this.connection.getInputStream());
    }

    private void initRequest() throws IOException, java.io.IOException {
        String queryParams = buildQueryParams();
        URL urlWithParams = new URL(url.toString() + (queryParams.isEmpty() ? "" : "?" + queryParams));
        this.connection = (HttpURLConnection) urlWithParams.openConnection();
        this.connection.setRequestProperty("accept", "application/json");
        this.connection.setRequestProperty("Authorization", this.token);
        this.connection.setRequestProperty("x-access-key", this.token);
    }

    private String buildQueryParams() {
        if (getParams() == null) return "";
        return getParams().entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }
    
    private List<T> parseJsonArrayToNotificacoes(InputStream inputStream) throws IOException, java.io.IOException {
        Gson gson = new GsonBuilder().create();
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            return gson.fromJson(reader, new TypeToken<List<T>>(){}.getType());
        }
    }
    
    public byte[] getImagem(String imageUrl) throws IOException, java.io.IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        try (InputStream inputStream = connection.getInputStream()) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        }
    }
	
}
