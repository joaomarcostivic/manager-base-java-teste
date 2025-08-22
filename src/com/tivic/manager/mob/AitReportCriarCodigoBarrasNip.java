package com.tivic.manager.mob;

import java.sql.Connection;
import java.text.ParseException;
import java.util.HashMap;

import org.codehaus.jettison.json.JSONException;

import sol.dao.ResultSetMap;

public class AitReportCriarCodigoBarrasNip {
	private ResultSetMap rsm;
	private HashMap<String, Object> paramns;
	private Connection connect;
	
	public AitReportCriarCodigoBarrasNip (ResultSetMap rsm,  HashMap<String, Object> paramns, Connection connect)
	{
		this.rsm = rsm;
		this.paramns = paramns;
		this.connect = connect;
	}
	
	public String criar (IAitReportCriarCodigoBarrasNip codigoBarras) throws ParseException, JSONException, org.json.JSONException
	{
		return codigoBarras.criarCodigoBarras(rsm, paramns, connect);	
	}
}
