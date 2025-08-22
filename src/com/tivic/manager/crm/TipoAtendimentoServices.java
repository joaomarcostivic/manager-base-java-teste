package com.tivic.manager.crm;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.manager.amf.DestinationConfig;

@DestinationConfig(enabled = false)
public class TipoAtendimentoServices {

	public static final GregorianCalendar getPrevisaoResposta(int nrHoras) {
		GregorianCalendar dtAtual = new GregorianCalendar();
		dtAtual.add(Calendar.HOUR, nrHoras);
		return dtAtual;
	}
	
}
