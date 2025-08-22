package com.tivic.manager.mob.funset.validations;

import com.tivic.manager.mob.funset.FunsetAitDTO;
import com.tivic.manager.mob.funset.parts.FunsetCampo;

public class FunsetValidationObrigatorio extends FunsetValidation {

	@Override
	public void validate(FunsetCampo funsetCampo, FunsetAitDTO funsetAitDTO) throws Exception {
		isObrigatorio(funsetCampo);
		super.validate(funsetCampo, funsetAitDTO);
	}
	
	private void isObrigatorio(FunsetCampo funsetCampo) throws Exception{
		if(funsetCampo.getValorCampo() == null || funsetCampo.equals(""))
			throw new Exception("Campo de " + funsetCampo.getTipoRegistro() + " é obrigatório");
	}

}
