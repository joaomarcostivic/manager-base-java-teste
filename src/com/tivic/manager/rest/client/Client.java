package com.tivic.manager.rest.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.core.Response;

import com.tivic.sol.response.ResponseFactory;

@Deprecated
public class Client implements IClient {
	
	private String url;
	private String authorization;		
	
	public Client() {
		super();
	}

	public Client(String url, String authorization) {
		super();
		this.url = url;
		this.authorization = authorization;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	@Override
	public Response post(String payload) throws IOException {
		
		System.out.println(payload);
		
		URL url = new URL(getUrl());
		URLConnection con = url.openConnection();
		HttpURLConnection http = (HttpURLConnection)con;
		http.setRequestMethod("POST"); 
		http.setDoOutput(true);
			
		byte[] out = payload.getBytes(StandardCharsets.UTF_8);
		
		setHeader(http, "Content-Type", "application/json");
		setHeader(http, "Authorization", getAuthorization());
		
		http.connect();
		
		OutputStream os = http.getOutputStream();
		os.write(out);
		
		System.out.println(http.getResponseCode());
		
		return ResponseFactory.build(http.getResponseCode(), http.getResponseMessage());	
	}

	@Override
	public Response put(String payload) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response get(String... resources) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response patch(String payload) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response delete(String... resources) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void setHeader(HttpURLConnection conn, String key, String value) {
		if(value!=null && !value.trim().equals("")) {
			conn.setRequestProperty(key, value);
		}
	}

}
