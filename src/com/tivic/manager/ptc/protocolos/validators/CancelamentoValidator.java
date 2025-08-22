package com.tivic.manager.ptc.protocolos.validators;

import java.util.List;

import javax.validation.ValidationException;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;

public class CancelamentoValidator implements IValidator<DadosProtocoloDTO> {

	@Override
	public void validate(DadosProtocoloDTO object, CustomConnection connection) throws Exception {
		List<AitMovimento> aitsMovimentoCancelamento = AitMovimentoServices.getAllCancelamentos(object.getAit().getCdAit(), connection.getConnection());
		if(aitsMovimentoCancelamento.isEmpty())
			return;
		throw new  ValidationException("O AIT está cancelado.");
	}

}
