package com.tivic.manager.mob;

import java.sql.Connection;
import java.util.GregorianCalendar;

public interface IAitReportCalcularPrazoNIP {
	GregorianCalendar PegarDataLimiteRecursoJari(int cdAit, Connection connect) throws AitReportErrorException;
}
