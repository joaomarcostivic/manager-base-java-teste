package com.tivic.manager.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.tivic.manager.grl.FeriadoServices;

/**
 * Controla operações com datas
 * @author Bruno Codato
 * @version 1.0
 */
public class DateUtil {
	
   /**
    * Adiciona dias a uma data
    * @param date Data base do cálculo
    * @param qtdDiasd Quantidade de dias a serem adicionados     * 
    */
    public static void addDays(GregorianCalendar date, int qtdDias ){
    	date.add(Calendar.DAY_OF_MONTH,qtdDias);    		
    }
	
    /**
     * Adiciona dias úteis a uma data 
     * @param dtInicial Data inicial do período.
     * @param qtdDiasd Quantidade de dias a serem adicionados
     * @return O fim do período
    */
	public static GregorianCalendar addUtilsDays(GregorianCalendar date, int qtdDias){
		GregorianCalendar dtFinal = (GregorianCalendar)date.clone();
		
		while (qtdDias >= 0){
			if(!FeriadoServices.isFeriado(dtFinal) && !isWeekend(dtFinal) ){
				
				if(qtdDias != 0)
					addDays(dtFinal, 1);
				
				qtdDias -= 1;
			}
			else{
				addDays(dtFinal, 1);
			}
		}		
		return dtFinal;		
	}  	
	
	 /**
     * Verifica se a a data é um dia do final de semana
     * @param date Data a ser verificada
    */
	public static boolean isWeekend(GregorianCalendar date){		
		if(date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY )
			return true;	
		
		return false;
	}
	
	/***
	 * Hoje
	 * 
	 */	
	public static GregorianCalendar today(){		
		return new GregorianCalendar();
	}
	
	public static GregorianCalendar todayWithoutTime(){
		GregorianCalendar today = new GregorianCalendar();
		
		today.set(GregorianCalendar.HOUR_OF_DAY, 0);
		today.set(GregorianCalendar.MINUTE, 0);
		today.set(GregorianCalendar.SECOND, 0);
		today.set(GregorianCalendar.MILLISECOND, 0);
		
		return today;
		
	}
	 
	public static Date stringToDate(String date) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date formatedDate = (Date) simpleDateFormat.parse(date);
		return formatedDate;
	}

}
