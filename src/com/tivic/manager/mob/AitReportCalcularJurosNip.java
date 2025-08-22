package com.tivic.manager.mob;

import java.sql.Connection;
import java.text.ParseException;

import org.codehaus.jettison.json.JSONException;

import sol.dao.ResultSetMap;

public class AitReportCalcularJurosNip {
	private ResultSetMap rsm;
	private int cdAit;
	private Connection connect;
	
	public AitReportCalcularJurosNip (ResultSetMap rsm, int cdAit, Connection connect)
	{
		this.rsm = rsm;
		this.cdAit = cdAit;
		this.connect = connect;
	}

	public void calcular (IAitReportCalcularJurosNip calcularJuros) throws Exception
	{
		calcularJuros.calularJurosNip(rsm, cdAit, connect);	
	}
}
