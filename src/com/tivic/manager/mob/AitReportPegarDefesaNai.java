package com.tivic.manager.mob;

import java.sql.Connection;

public class AitReportPegarDefesaNai {
	private int cdAit;
	private Connection connect;
	
	public AitReportPegarDefesaNai(int cdAit, Connection connect)
	{
		this.cdAit = cdAit;
		this.connect = connect;
	}
	
	public String pegar(IAitReportCalcularPrazoDefesaNAI estrategiaDataDefesaNai)
	{
		return estrategiaDataDefesaNai.pegarDataDefesaPrimeiraVia(cdAit, connect);
	}
	
}
