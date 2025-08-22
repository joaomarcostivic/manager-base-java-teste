package com.tivic.manager.rest.log;

import java.util.GregorianCalendar;
import java.util.List;

public interface ILogger {
		
	public Log log(Log log);	
	public List<Log> getAll(int cdUsuario);
	public List<Log> getAll(int cdUsuario, GregorianCalendar dtInicial, GregorianCalendar dtFinal);
	public List<Log> getAll(int cdUsuario, int tpMetodo, String nmCaminho);	

}
