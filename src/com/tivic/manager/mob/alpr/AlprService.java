package com.tivic.manager.mob.alpr;

import java.io.IOException;

import org.json.JSONException;

public interface AlprService {
	
	public AlprResult recognize(byte[] file) throws IOException, InterruptedException, JSONException;
	public AlprResult recognize(byte[] file, String filename) throws IOException, InterruptedException, JSONException;
	public AlprResult recognize(byte[] file, String filename, String idCliente) throws IOException, InterruptedException, JSONException;
	

}
