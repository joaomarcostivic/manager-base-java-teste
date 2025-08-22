package com.tivic.manager.ptc.portal.credencialestacionamento;

import java.util.HashMap;

public class BodyEmailProtocoloBuilder {

	 private HashMap<String, String> params;
	 
	 public BodyEmailProtocoloBuilder() {
		 this.params = new HashMap<String, String>();
	 }
	 
	 public BodyEmailProtocoloBuilder setParams(String key, String value) {
		 params.put(key, value);
		 return this;
	 }
	 
	 public HashMap<String, String> build() {
		 return params;
	 }
}
