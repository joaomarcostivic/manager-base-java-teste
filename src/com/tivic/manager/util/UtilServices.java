package com.tivic.manager.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.tivic.manager.amf.DestinationsManager;
import com.tivic.manager.grl.ParametroServices;

import sol.util.HardwareManager;
import sol.util.Result;

/**
 * Encapsula métodos de com.tivic.manager.util.Util 
 * para uso no flex
 * 
 * @author Maurício
 * @since 16/07/2015
 */
public class UtilServices {

	public static GregorianCalendar getDataAtual() {
		return Util.getDataAtual();
	}
	
	/**
	 * Atualiza as configurações de log
	 * @author Léo Sapucaia
	 * @since 15/05/2016
	 * @return
	 */
	public static Result refreshLogConfiguration() {
		LogUtils.refreshLogConfiguration();
		return new Result(1, "Configurações de log atualizadas.");
	}
	
	/**
	 * Retorna o estado atual do servidor, considerando os tamanhos e uso de memória de alguns controles principais da aplicação
	 * @author Léo Sapucaia
	 * @since 02/06/2016
	 * @return
	 */
	public static Result getServerState() {
		
		Result r = new Result(1);
		r.addObject("DESTINATIONS_SIZE", DestinationsManager.getSize());
		r.addObject("DESTINATIONS_MEM_USAGE", (DestinationsManager.getMemoryUsage()/1024/1024)+"mb");
		r.addObject("POOL_SIZE", 0);
		r.addObject("POOL_MEM_USAGE", 0+"mb");
		r.addObject("SESSION_SIZE", SessionManager.getSize());
		r.addObject("SESSION_MEM_USAGE", (SessionManager.getMemoryUsage()/1024/1024)+"mb");
		r.addObject("SYSTEM_FREE_MEMORY", (Runtime.getRuntime().freeMemory()/1024/1024)+"mb");
		r.addObject("SYSTEM_TOTAL_MEMORY", (Runtime.getRuntime().totalMemory()/1024/1024)+"mb");
		r.addObject("SYSTEM_MAX_MEMORY", (Runtime.getRuntime().maxMemory()/1024/1024)+"mb");
		r.addObject("SYSTEM_MEMORY_USED", ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024/1024)+"mb");
		
		return r;
	}
	
	/**
	 * Executa o Garbage Collector na aplicação
	 * @author Léo Sapucaia
	 * @since 02/06/2016
	 * @return
	 */
	public static Result garbageCollectServer() {
		
		System.gc();
		
		return new Result(1, "Garbage Collector Executado.");
	}
	
	/**
	 * Faz um ping. Para ser usado via RemoteObject, determinando assim se os destinations estão inicializados. 
	 * @author Léo Sapucaia
	 * @since 08/06/2016
	 * @return
	 */
	public static Result ping() {
		return new Result(1, "Pong.");
	}
	
	/**
	 * Lê as N ultimas linhas de og do tomcat. 
	 * @author Léo Sapucaia
	 * @since 08/06/2016
	 * @return
	 */
	public static Result tailLogTomcat(int maxLines) {
		
		String pathTomcat = ParametroServices.getValorOfParametro("PATH_TOMCAT");
		
		if(pathTomcat!=null && !pathTomcat.equals("")){
			
			String logFile = pathTomcat + "/"+ (HardwareManager.isWindows() ? 
												"catalina."+Util.formatDateTime(System.currentTimeMillis(), "yyyy-MM-dd")+".log" : 
												"catalina.out");
			
			String text = tail(logFile, maxLines);
			
			return new Result(1, text);
		}
		else
			return new Result(1, "Não foi possível abrir o log do tomcat.");
	}
	
	/**
	 * Lê as N ultimas linhas de um arquivo de texto. 
	 * @author Léo Sapucaia
	 * @since 08/06/2016
	 * @return
	 */
	public static String tail(String file, int maxLines) {
	    java.io.RandomAccessFile fileHandler = null;
	    try {
	        fileHandler = new java.io.RandomAccessFile(file, "r");
	        long fileLength = fileHandler.length() - 1;
	        StringBuilder sb = new StringBuilder();
	        int line = 0;

	        for(long filePointer = fileLength; filePointer != -1; filePointer--){
	            fileHandler.seek( filePointer );
	            int readByte = fileHandler.readByte();

	             if( readByte == 0xA ) {
	                if (filePointer < fileLength) {
	                    line = line + 1;
	                }
	             } 
	             else if( readByte == 0xD ) {
	                if (filePointer < fileLength-1) {
	                    line = line + 1;
	                }
	            }
	            if (line >= maxLines) {
	                break;
	            }
	            sb.append( ( char ) readByte );
	        }

	        String lastLine = sb.reverse().toString();
	        return lastLine;
	    } 
	    catch( java.io.FileNotFoundException e ) {
	        e.printStackTrace();
	        return null;
	    } 
	    catch( java.io.IOException e ) {
	        e.printStackTrace();
	        return null;
	    }
	    finally {
	        if (fileHandler != null)
	            try {
	                fileHandler.close();
	            } 
	        	catch (IOException e) {
	            }
	    }
	}
	/**
	 * Lê um array de string e modifica todos os valores passados no array para o novo valor
	 */
	public static String replaceSequence(String word, ArrayList<CharSequence> charSequence, CharSequence newChar) {
		
		for (CharSequence oldChar : charSequence) {
			word = word.replace(oldChar, newChar);
		}
		
		return word;
	}
	
	/**
	 * Verifica se uma faixa de horário entre em choque com outra faixa
	 * @param primeiroHorarioInicial
	 * @param primeiroHorarioFinal
	 * @param segundoHorarioInicial
	 * @param segundoHorarioFinal
	 * @return
	 */
	public static boolean choqueHorarios(GregorianCalendar primeiroHorarioInicial, GregorianCalendar primeiroHorarioFinal, GregorianCalendar segundoHorarioInicial, GregorianCalendar segundoHorarioFinal){
		if((primeiroHorarioInicial.compareTo(segundoHorarioInicial) <= 0 && 
			primeiroHorarioFinal.compareTo(segundoHorarioInicial) == 1) ||
		    
		   (primeiroHorarioInicial.compareTo(segundoHorarioInicial) == 1 && 
		   	primeiroHorarioInicial.compareTo(segundoHorarioFinal) == -1)){
			return true;
		}
		
		return false;
	}
	
}
