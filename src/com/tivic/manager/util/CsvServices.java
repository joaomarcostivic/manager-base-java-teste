package com.tivic.manager.util;

import sol.dao.ResultSetMap;
import sol.util.Result;


public class CsvServices {
	
	public static Result load(byte[] contentCSV, String delim) {			
		try {
			
			String content = new String(contentCSV);
			if(Util.getConfManager().getProps().getProperty("ENCODING")!=null &&
			   Util.getConfManager().getProps().getProperty("ENCODING").toUpperCase().equals("LATIN1")){
				content = new String(contentCSV, "ISO-8859-1");
			}
			
			ResultSetMap rsmDados = ResultSetMap.getResultsetMapFromByte(content.getBytes(), delim, true);
			
			Result r = new Result(1, "Carregado com sucesso.");
			r.addObject("RSM_DADOS", rsmDados);
			
			return r;
			
		}
		catch(Exception e){
			e.printStackTrace();
			return new Result(-1, "Erro ao carregar: "+ e.getMessage());
		}	
	}
}
