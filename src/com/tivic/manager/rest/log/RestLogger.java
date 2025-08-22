package com.tivic.manager.rest.log;

import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.log.SistemaServices;
import com.tivic.manager.rest.request.filter.Criterios;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

public class RestLogger extends SistemaServices implements ILogger {
	
	private static RestLogger instance;
	
	private RestLogger() { }
	
	public static RestLogger getInstance() {
		if(instance == null) {
			synchronized (RestLogger.class) {
				if(instance == null) {
					instance = new RestLogger();
				}
			}
		}
		return instance;
	}

	@Override
	public Log log(Log log) {
		Result result = save(log);
		
		if(result.getCode() < 0)
			return null;
		
		return (Log)result.getObjects().get("SISTEMA");
	}

	@Override
	public List<Log> getAll(int cdUsuario) {
		return getAll(cdUsuario, -1);
	}
	
	public List<Log> getAll(int cdUsuario, int limit) {
		List<Log> logs = new ArrayList<Log>();
		
		Criterios crt = new Criterios("cd_usuario", Integer.toString(cdUsuario), ItemComparator.EQUAL, Types.INTEGER);
		
		if(limit > 0) {
			crt.add("limit", Integer.toString(limit), 0, 0);
		}
		
		ResultSetMap rsm = find(crt);
		
		while(rsm.next()) {
			logs.add(new Log(
					rsm.getGregorianCalendar("dt_log"), 
					rsm.getString("txt_log"), 
					rsm.getInt("cd_usuario"), 
					rsm.getString("nm_metodo"), 
					rsm.getString("nm_caminho"), 
					rsm.getInt("nr_http_status"), 
					rsm.getString("ds_ip_cliente"), 
					rsm.getString("ds_user_agent")));
		}
		
		return logs;
	}

	@Override
	public List<Log> getAll(int cdUsuario, GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Log> getAll(int cdUsuario, int tpMetodo, String nmCaminho) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

}
