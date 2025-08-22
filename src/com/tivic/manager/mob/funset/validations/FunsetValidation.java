package com.tivic.manager.mob.funset.validations;

import com.tivic.manager.mob.funset.FunsetAitDTO;
import com.tivic.manager.mob.funset.parts.FunsetCampo;

public class FunsetValidation {
	public void validate(FunsetCampo funsetCampo, FunsetAitDTO funsetAitDTO) throws Exception{
		if(funsetCampo.getValorCampo().length() > funsetCampo.getTamanho())
			throw new Exception(funsetCampo.getTipoRegistro() + " com tamanho maior do que o limite ("+funsetCampo.getTamanho()+")");
	}
}
