package com.tivic.manager.mob;

import java.sql.Connection;

public interface IAitReportCalcularPrazoDefesaNAI {
	String pegarDataDefesaPrimeiraVia(int cdAit, Connection connect);
}
