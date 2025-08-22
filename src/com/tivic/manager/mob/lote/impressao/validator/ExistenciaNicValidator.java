package com.tivic.manager.mob.lote.impressao.validator;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitReportErrorException;
import com.tivic.manager.util.validator.Validator;

public class ExistenciaNicValidator implements Validator<Ait> {

	@Override
	public void validate(Ait object, CustomConnection customConnection) throws Exception {
		if (object.getCdAitOrigem() > 0) {
			throw new  AitReportErrorException("Não e possivel emitir NAI para AIT de NIC");
		}
		return;	
	}
}
