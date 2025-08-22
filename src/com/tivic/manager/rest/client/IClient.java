package com.tivic.manager.rest.client;

import java.io.IOException;

import javax.ws.rs.core.Response;

public interface IClient {

	public Response post(String payload) throws IOException;
	public Response put(String payload);
	public Response get(String... resources);
	public Response patch(String payload);
	public Response delete(String... resources);
	
}
