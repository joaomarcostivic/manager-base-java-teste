package com.tivic.manager.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class DneUtilities {
	
	
	/**
	 * Buscar informações de um endereço utilizando o cep como critério.
	 * 
	 * @author Edgard Hufelande
	 * @param  cep      código do endereçamento postal da região na qual deseja buscar. 
	 * @param  output   formato em que o resultado da busca irá ser retornado. 
	 * @return json     dados da região informada.
	 * 
	 */
	
	public static String findEnderecoByCep(String cep, String output) throws Exception {
		return findEnderecoByCep(cep, output, null);
	}
	
	public static String findEnderecoByCep(String cep, String output, String ws) throws Exception {		
		try	{
			String json = new String();
			
			if( cep == null || cep.equals("") || !Util.isNumber(cep)) {
				return json;
			}
			
			if(output == null || output.equals("")) {
				output = "json";
			}
			
			if(ws == null || ws.equals("")) {
				ws = "ws";
			}
			
			cep  = cep.replaceAll( "[^\\d]", "" );		
			json = readUrl("http://viacep.com.br/"+ws+"/" + cep + "/" + output + "/");						
			
			return json;	
			
		} catch(Exception e) {
			return null;
		}			
	}
	
	/**
	 * Buscar informações de um endereço utilizando o próprio endereço como critério.
	 * 
	 * @author Edgard Hufelande
	 * @param  Endereco   código do endereçamento postal da região na qual deseja buscar. 
	 * @param  output     formato em que o resultado da busca irá ser retornado. 
	 * @return json       dados da região informada
	 * 
	 */
	
	public static String findEnderecoByEndereco(String uf, String cidade, String rua, String output) throws Exception {		
		try	{
			String json     = new String("[]");
			String endereco = new String();
			
			if(uf.equals("") || uf == null) {
				return json;
			}
			
			if(cidade.equals("") || cidade == null) {
				return json;
			}
			
			if(rua.equals("") || rua == null) {
				return json;
			}
			
			if(output.equals("") || output == null) {
				output = "json";
			}
			
			endereco  = uf + "/" + cidade + "/" + rua;		
			json      = readUrl("http://viacep.com.br/ws/" + endereco + "/" + output + "/");						
			
			return json;	
			
		}  finally {
	        
	    }			
	}
	
	private static String readUrl(String urlString) throws Exception {
	    BufferedReader reader = null;
	    try {
	        URL url = new URL(urlString);
	        reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 

	        return buffer.toString();
	    } finally {
	        if (reader != null)
	            reader.close();
	    }
	}

}