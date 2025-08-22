package com.tivic.manager.mob;

import java.sql.Connection;
import java.text.ParseException;

import org.codehaus.jettison.json.JSONException;

import sol.dao.ResultSetMap;

public interface IAitReportCalcularJurosNip {
	void calularJurosNip(ResultSetMap rsm, int cdAit,  Connection connect) throws ParseException, JSONException, org.json.JSONException, Exception;
}