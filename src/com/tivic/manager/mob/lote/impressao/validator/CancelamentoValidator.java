package com.tivic.manager.mob.lote.impressao.validator;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.AitReportErrorException;
import com.tivic.manager.util.validator.Validator;

public class CancelamentoValidator implements Validator<Ait> {

	@Override
	public void validate(Ait object, CustomConnection customConnection) throws Exception {
		List<AitMovimento> aitsMovimentoCancelamento = AitMovimentoServices.getAllCancelamentos(object.getCdAit(), customConnection.getConnection());
		if(aitsMovimentoCancelamento.isEmpty())
			return;
		throw new AitReportErrorException("O AIT está cancelado");
	}

}
