package com.tivic.manager.str;
	   
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.GregorianCalendar;

import com.tivic.manager.util.Util;


public class RadarSocketServer {

	public static int SOCKET_PORT = 9182;
		
	public static void main(String[] args) throws IOException {
	     // inicia o servidor
	     new RadarSocketServer().executa();
	}
	   
	public void executa () throws IOException {
		try{
			@SuppressWarnings("resource")
			ServerSocket servidor = new ServerSocket(SOCKET_PORT);
		    System.out.println("Inicando socket na porta "+SOCKET_PORT+"...");
		     
		    while (true) {
		    	// aceita um cliente
		    	Socket cliente = servidor.accept();
		    	System.out.println("\n"+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm:ss")+"\nNova conexão com o cliente " +  cliente.getInetAddress().getHostAddress());
		       
		    	// cria tratador de cliente numa nova thread
		    	RadarSocketMultithreading tc = new RadarSocketMultithreading(cliente.getInputStream(), this);
		    	new Thread(tc).start();
		    }
		} catch (Exception e) {
			 System.err.println("Erro! RadarSocketServer.executa(): " +e);
			 e.printStackTrace(System.out);
		 }
	 }	 
}
