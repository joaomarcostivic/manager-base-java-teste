package com.tivic.manager.wsdl.detran.mg.builders;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.wsdl.detran.mg.consultarplaca.ConsultarPlacaDadosRetorno;
import com.tivic.manager.wsdl.interfaces.DadosRetorno;

public class AitBuilderSearchPlaca extends AitBuilderSearchProdemge implements IAitBuilderSearchProdemge {

	public void build(Ait ait, DadosRetorno dadosRetorno) {
		ConsultarPlacaDadosRetorno retorno = (ConsultarPlacaDadosRetorno) dadosRetorno;
		if(retorno.getCodigoMarcaModelo() > 0) 
			ait.setCdMarcaVeiculo(retorno.getCodigoMarcaModelo());
	}
	
}
