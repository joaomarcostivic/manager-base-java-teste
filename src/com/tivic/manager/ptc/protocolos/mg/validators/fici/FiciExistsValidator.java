package com.tivic.manager.ptc.protocolos.mg.validators.fici;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;
import com.tivic.manager.ptc.protocolos.validators.IValidator;


public class FiciExistsValidator implements IValidator<DadosProtocoloDTO>{

	@Override
	public void validate(DadosProtocoloDTO obj, CustomConnection connection) throws ValidationException, Exception{
		AitMovimento aitMovimento = AitMovimentoServices.getFici(obj.getMovimento(), connection.getConnection());
		if(aitMovimento != null) {
			throw new ValidationException("Esse AIT já possui Apresentação de Condutor lançada");
		}
	}
	
}
