package com.tivic.manager.msg.bot;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

import com.tivic.manager.msg.Mensagem;

/**
 * Notifica o canal #desenvolvimento no workspace etivic do Slack
 * 
 * @author @mauriciocordeiro <mauricio@tivic.com.br>
 */
public class TinoMarcos implements ChatBot {
	
	private static final String URL = "https://hooks.slack.com/services/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";

	@Override
	public void enviar(String mensagem) {
		try {
			URL url = new URL(URL);
			URLConnection con = url.openConnection();
			HttpURLConnection http = (HttpURLConnection)con;
			http.setRequestMethod("POST"); 
			http.setDoOutput(true);
			
			JSONObject json = new JSONObject();
			json.put("text", "Galvão...\n" + mensagem);
			
			byte[] out = json.toString().getBytes(StandardCharsets.UTF_8);
			int length = out.length;

			http.setFixedLengthStreamingMode(length);
			http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			http.connect();
			try(OutputStream os = http.getOutputStream()) {
			    os.write(out);
			}
		} catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}

	@Override
	public void enviar(Mensagem mensagem) {
		// TODO Auto-generated method stub
		
	}

}
