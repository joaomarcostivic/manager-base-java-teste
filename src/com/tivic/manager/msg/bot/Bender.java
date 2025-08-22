package com.tivic.manager.msg.bot;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

import com.tivic.manager.msg.Mensagem;
import com.tivic.manager.rest.client.Client;
import com.tivic.manager.rest.client.IClient;

/**
 * Notifica o canal #radar no workspace etivic do Slack e o canal #chatbots no Discord
 * 
 * @author @mauriciocordeiro <mauricio@tivic.com.br>
 */
public class Bender implements ChatBot {
	
	private static final String SLACK = "https://hooks.slack.com/services/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";
	private static final String DISCORD = "https://discord.com/api/webhooks/799706916371693648/iVKyshu1gZDd8Zr84oEAixD-ve0am7VZ-6MeLTVx_hCEXPlLWjI8bCHvl1MRtBgDeYIg";

	@Override
	public void enviar(String mensagem) {
		toSlack(mensagem);
		toDiscord(mensagem);
	}

	@Override
	public void enviar(Mensagem mensagem) {
		// TODO Auto-generated method stub
		
	}
	
	private void toSlack(String mensagem) {
		try {
			URL url = new URL(SLACK);
			URLConnection con = url.openConnection();
			HttpURLConnection http = (HttpURLConnection)con;
			http.setRequestMethod("POST"); 
			http.setDoOutput(true);
			
			JSONObject json = new JSONObject();
			json.put("text", mensagem+"\nMas quem se importa?");
			
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
	
	private void toDiscord(String mensagem) {
		try {
			JSONObject json = new JSONObject();
			json.put("content", mensagem);
			
			IClient rest = new Client(DISCORD, null);
			rest.post(json.toString());
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
