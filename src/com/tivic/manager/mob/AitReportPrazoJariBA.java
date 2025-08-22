package com.tivic.manager.mob;

import java.sql.Connection;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class AitReportPrazoJariBA implements IAitReportCalcularPrazoNIP{

	@SuppressWarnings("static-access")
	@Override
	public GregorianCalendar PegarDataLimiteRecursoJari(int cdAit, Connection connect) 
	{
		
		AitReportGetParamns getParamns = new AitReportGetParamns();
		HashMap<String, Object> paramns = getParamns.getParamnsNIP (connect);
		int nrDiasJari =  Integer.parseInt( (String) paramns.get("MOB_PRAZOS_NR_RECURSO_JARI"));
		GregorianCalendar dtVencimento = new GregorianCalendar();
		dtVencimento.add(Calendar.DATE, nrDiasJari);
		
		return dtVencimento;
		
	}

}
