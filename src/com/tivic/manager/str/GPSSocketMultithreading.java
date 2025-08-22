package com.tivic.manager.str;

import java.io.InputStream;
import java.util.Scanner;

public class GPSSocketMultithreading implements Runnable {
	
	private InputStream cliente;
	private GPSSocketServer servidor;	 

	public GPSSocketMultithreading(InputStream cliente, GPSSocketServer servidor) {
		// TODO Auto-generated constructor stub
		this.cliente = cliente;
	    this.servidor = servidor;
	}

	@Override
	public void run() {
		try{
			// quando chegar uma msg, distribui para todos
		    Scanner s = new Scanner(this.cliente);
		    while (s.hasNextLine()) {
		    	 servidor.distribuiMensagem(s.nextLine());
		    }
		    s.close();
		}catch (Exception e) {  
			System.err.println("Erro! GPSSocketMultithreading.run(): " +  e);
			e.printStackTrace(System.out);  
		}
	}

}
