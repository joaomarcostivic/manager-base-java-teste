package com.tivic.manager.ptc.protocolos.mg.validators.fici;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;
import com.tivic.manager.ptc.protocolos.validators.IValidator;

public class NaiValidator implements IValidator<DadosProtocoloDTO>{

	@Override
	public void validate(DadosProtocoloDTO obj, CustomConnection connection) throws ValidationException, Exception {
		AitMovimento nai = AitMovimentoServices.getNai(obj.getAit().getCdAit(), connection.getConnection());
		
		if(nai == null) {
			throw new ValidationException("AIT não possui NAI lançada.");
		}
	}

}
