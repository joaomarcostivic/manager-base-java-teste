package com.tivic.manager.rest.client;

import java.io.File;

import org.json.JSONObject;

public interface IRestRequest<T> {
	
	public IRestRequest<T> setHeader(String key, String value);
	public IRestRequest<T> setBody(JSONObject json);
	public IRestRequest<T> setBodyField(String key, String value);
	public IRestRequest<T> setBodyField(String key, File value);

	public IRestRequest<T> post(String url) throws RestException;
	public IRestRequest<T> put(String url) throws RestException;
	public IRestRequest<T> patch(String url) throws RestException;
	public IRestRequest<T> delete(String url) throws RestException;
	public IRestRequest<T> get(String url) throws RestException;
	
	public T run() throws RestException;
}
