package com.tivic.manager.rest.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RestRequest<T> implements IRestRequest<T> {
	
	private URL url;
	private URLConnection conn;
	private HttpURLConnection http;
	
	private Map<String, Object> map;
	private JSONObject json;
	
	private Class<T> responseType;
	
	public RestRequest(Class<T> responseType) {
		this.responseType = responseType;		
				
		this.map = new HashMap<String, Object>();
		this.json = new JSONObject();
	}
	
	private void init(URL url, String method) {
		try {
			this.url = url;
			this.conn = this.url.openConnection();
			this.http = (HttpURLConnection) conn;
			http.setDoOutput(true);
			this.http.setRequestMethod(method); 
		} catch(IOException ioe) {
			ioe.printStackTrace(System.out);
		}
	}
	

	@Override
	public IRestRequest<T> setHeader(String key, String value) {
		this.conn.setRequestProperty(key, value);		
		return this;
	}

	@Override
	public IRestRequest<T> setBody(JSONObject json) {
		this.json = json;
		return this;
	}

	@Override
	public IRestRequest<T> setBodyField(String key, String value) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public IRestRequest<T> setBodyField(String key, File value) {
		// TODO Auto-generated method stub
		return this;
	}

	@SuppressWarnings("finally")
	@Override
	public T run() throws RestException {	
		InputStream is;
		try {
			this.http.connect();		
			byte[] out = this.json.toString().getBytes(StandardCharsets.UTF_8);
			
			OutputStream os = this.http.getOutputStream();
			os.write(out);
			
			is = this.http.getInputStream();
			
			ObjectMapper mapper = new ObjectMapper();
			T res = mapper.readValue(is, this.responseType);
			
			return res;
		} catch(IOException ioe) {
			ioe.printStackTrace(System.err);			
			String err = new BufferedReader(
				      new InputStreamReader(this.http.getErrorStream(), StandardCharsets.UTF_8))
				        .lines()
				        .collect(Collectors.joining("\n"));
			
			throw new RestException(400, err);
		}
	}

	@Override
	public IRestRequest<T> post(String url) throws RestException {
		try {
			this.init(new URL(url), "POST");
			return this;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RestException(400, e.getMessage());
		}
	}

	@Override
	public IRestRequest<T> put(String url) throws RestException {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public IRestRequest<T> patch(String url) throws RestException {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public IRestRequest<T> delete(String url) throws RestException {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public IRestRequest<T> get(String url) throws RestException {
		try {
			this.init(new URL(url), "GET");
			return this;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RestException(400, e.getMessage());
		}
	}


}
