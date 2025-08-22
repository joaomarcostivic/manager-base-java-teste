package com.tivic.manager.rest.log;

import java.util.GregorianCalendar;

import com.tivic.manager.log.Sistema;
import com.tivic.manager.log.SistemaServices;

public class Log extends Sistema {

	public Log(GregorianCalendar dtLog, String txtLog, 
			int cdUsuario, String nmMetodo, String nmCaminho, 
			int nrHttpStatus, String dsIpCliente, String dsUserAgent) {
		super(0, dtLog, txtLog, SistemaServices.TP_LOG_GERAL, cdUsuario, nmMetodo, nmCaminho, nrHttpStatus, dsIpCliente, dsUserAgent);
	}	
}
