package com.tivic.manager.mob.ait;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.wsdl.detran.mg.consultaautobaseestadual.ConsultaAutoBaseEstadualDadosRetorno;

public class AitDirector {
	
	public Ait construirAitConsultaBaseEstadual(ConsultaAutoBaseEstadualDadosRetorno retorno) throws Exception {
		return new AitConsultaBaseEstadualBuilder(retorno).build();
	}
	
}
