package com.tivic.manager.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

public class HttpProxy {

	public static String read(String url){
		try {
			HttpClient client = new HttpClient();
			HttpMethod method = new GetMethod(url);
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK)
				System.err.println("Método falhou: " + method.getStatusLine());
			return method.getResponseBodyAsString().replaceAll("[\\n\\t\\r]", "");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return "";
		}
	}

	public String replaceAllAnsi(String str){
		str = str.replaceAll("[âÂàÀãÃáÁ]","a");
		str = str.replaceAll("[êÊèÈéÉ]","e");
		str = str.replaceAll("[íÍ]","i");
		str = str.replaceAll("[õÕôÔóÓ]","o");
		str = str.replaceAll("[üÜúÚ]","u");
		str = str.replaceAll("[çÇ]","c");
		return str;
	}
}
