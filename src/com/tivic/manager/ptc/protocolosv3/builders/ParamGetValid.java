package com.tivic.manager.ptc.protocolosv3.builders;

import com.tivic.manager.grl.ParametroServices;

public class ParamGetValid {
	
	public int getParamValue(String paramValue) throws Exception {
		int value = ParametroServices.getValorOfParametroAsInteger(paramValue, 0);
		
		if(value <= 0) 
			throw new Exception("Parâmetro "+ paramValue +" não configurado");
		
		return value;
	}
}
