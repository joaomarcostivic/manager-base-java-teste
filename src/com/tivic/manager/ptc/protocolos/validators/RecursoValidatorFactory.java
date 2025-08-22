package com.tivic.manager.ptc.protocolos.validators;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.ptc.protocolos.mg.validators.DefesaAdvertenciaValidator;
import com.tivic.manager.ptc.protocolos.mg.validators.DefesaPreviaValidator;
import com.tivic.manager.ptc.protocolos.mg.validators.FiciValidator;

public class RecursoValidatorFactory {
	
	public RecursoValidator generateRecursoValidator(int tpStatus) throws ValidationException {
		switch(tpStatus) {
			case AitMovimentoServices.APRESENTACAO_CONDUTOR:
				return new FiciValidator();
				
			case AitMovimentoServices.DEFESA_PREVIA:
				return new DefesaPreviaValidator();
			
			case AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA:
				return new DefesaAdvertenciaValidator();
			
			default:
				throw new ValidationException("Serviço não implementado");
		}
	}

}
