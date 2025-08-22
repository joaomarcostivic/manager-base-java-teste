package com.tivic.manager.mob;
	   
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class GPSSocketServer {
	
	public static void main(String[] args) throws IOException {
	     // inicia o servidor
	     new GPSSocketServer (880).executa();
	}
	   
	private int porta;
	private List<PrintStream> clientes;
   
	public GPSSocketServer (int porta) {
		this.porta = porta;
		this.clientes = new ArrayList<PrintStream>();
	}
	   
	public void executa () throws IOException {
		try{
			@SuppressWarnings("resource")
			ServerSocket servidor = new ServerSocket(this.porta);
		    System.out.println("Porta 880 aberta!");
		     
		    while (true) {
		    	// aceita um cliente
		    	Socket cliente = servidor.accept();
		    	System.out.println("Nova conexão com o cliente " +  cliente.getInetAddress().getHostAddress());
		       
		    	// adiciona saida do cliente é lista
		    	PrintStream ps = new PrintStream(cliente.getOutputStream());
		    	this.clientes.add(ps);
		       
		    	// cria tratador de cliente numa nova thread
		    	GPSSocketMultithreading tc = new GPSSocketMultithreading(cliente.getInputStream(), this);
		    	new Thread(tc).start();
		    }
		} catch (Exception e) {
			 System.err.println("Erro! GPSSocketServer.executa(): " +e);
			 e.printStackTrace(System.out);
		 }
	 }
	
	 public void distribuiMensagem(String msg) {
		 // envia msg para todo mundo
		 try {
			 System.out.println(msg);
			 mountURL(msg);
		 } catch (IOException e) {
			 System.err.println("Erro! GPSSocketServer.distribuiMensagem(msg): " +e);
			 e.printStackTrace(System.out);
		 }
	 }
	
	 public static void mountURL(String URL) throws IOException  { 
		 try{    	
		  	String idOrgao = "";
			String idEquipamento = "";
			String nrMatriculaAgente = "";
			double vlLatitude = 0;
			double vlLongitude = 0;
			
			long time = 0;
		  	    	
			/*
			 * Tratamento de geoposicionamento para o caso do GPS veicular
			 * 
			 * O formato de recebimento é:
			 * 	160823162847,
			 *	+5577991105207,
			 *	GPRMC,
			 *		142847.000,
			 *		A,
			 *		1450.9971,
			 *		S,
			 *		04050.1775,
			 *		W,
			 *		0.00,
			 *		258.11,
			 *		230816,
			 *		,
			 *		,
			 *		A*6F,
			 *		F,
			 *	message	battery,
			 *	imei:013949007303293,
			 *	08,
			 *	931.8,
			 *	F:4.11V,
			 *	0,
			 *	149,
			 *	4952,
			 *	724,
			 *	31,
			 *	0D9A,
			 *	1F91
			 */
		//	    	String queryString = URL.getQueryString();
			
			String parts[] = URL.split(",", -1);
			
			DateFormat df = new SimpleDateFormat("ddMMyyHHmmss.S");
			Date parsed = df.parse(parts[11].substring(0, 6)+parts[3].substring(0, 10));
			time = parsed.getTime();
			
			int dlat    = new Integer(parts[5].substring(0, 2));
			double mlat = new Double(parts[5].substring(2, 9));
			    	
			//Negativo por ser Sul (S)
		    double ddlat = -(Math.abs(dlat) + (mlat / 60.0));	    	
		    	
	  	    int dlng    = new Integer(parts[7].substring(1, 3));
		    double mlng = new Double(parts[7].substring(3, 10));	    		    	
		    	
		  //Negativo por ser Sul (W)
		    double ddlng = -(Math.abs(dlng) + (mlng / 60.0));
	 
		    vlLatitude = ddlat;
		    vlLongitude = ddlng;
		    	
		    idEquipamento = parts[17].substring(6);
		    idOrgao = "GPS";		    	    		
			
			GregorianCalendar dtHistorico = new GregorianCalendar();
			dtHistorico.setTimeInMillis(time);
			
			//dtHistorico.add(Calendar.HOUR_OF_DAY, -3);
			
			readUrl("http://mobilidade.tivic.com.br:8080/etransporte/geotracker?idOrg="+idOrgao + "&idAg=" + nrMatriculaAgente + "&idEq=" +
																             idEquipamento + "&t=" + time + "&lat=" + vlLatitude + "&lng=" + vlLongitude);    	
//	    	GeoTracker tracker = new GeoTracker(0, idOrgao, idEquipamento, nrMatriculaAgente, vlLatitude, vlLongitude, dtHistorico);
//	    	GeoTrackerDAO.insert(tracker);
//	    	System.out.println(tracker.toString());
		}catch (Exception e) {  
			System.err.println("Erro! GPSSocketServer.mountURL(URL): " +  e);
	          e.printStackTrace(System.out);  
	    }	      
	 }
	  
	 private static String readUrl(String _url) throws Exception {
		 BufferedReader reader = null;
		 try {
			 URL url = new URL(_url);
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
