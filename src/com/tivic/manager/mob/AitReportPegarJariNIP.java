package com.tivic.manager.mob;

import java.sql.Connection;
import java.util.GregorianCalendar;

public class AitReportPegarJariNIP {
	private int cdAit;
	private Connection connect;
	
	public AitReportPegarJariNIP(int cdAit, Connection connect)
	{
		this.cdAit = cdAit;
		this.connect = connect;
	}
	
	public GregorianCalendar pegar(IAitReportCalcularPrazoNIP estrategiaDataJariNip) throws AitReportErrorException
	{
		return estrategiaDataJariNip.PegarDataLimiteRecursoJari(cdAit, connect);
	}
}
