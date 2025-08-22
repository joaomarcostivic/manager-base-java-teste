package com.tivic.manager.mob.lotes.validator;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitReportErrorException;
import com.tivic.sol.connection.CustomConnection;

public class ExistenciaNicValidator implements IValidator<Ait> {

	@Override
	public void validate(Ait object, CustomConnection customConnection) throws Exception {
		if (object.getCdAitOrigem() > 0) {
			throw new  AitReportErrorException("Não e possivel emitir NAI para AIT de NIC");
		}
		return;	
	}
}
