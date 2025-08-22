package com.tivic.manager.ptc.protocolos.mg.validators.fici;

import java.sql.Connection;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;
import com.tivic.manager.ptc.protocolos.validators.IValidator;

public class DataValidator implements IValidator<DadosProtocoloDTO>{

	@Override
	public void validate(DadosProtocoloDTO obj, CustomConnection connection) throws ValidationException, Exception {
		AitMovimento nai = AitMovimentoServices.getNai(obj.getAit().getCdAit(), connection.getConnection());
		
		if(validateDataLancamento(obj.getDtProtocolo(), nai.getDtMovimento())) {
			throw new ValidationException("Data do FICI é anterior a data da NAI");
		}
	}

	private boolean validateDataLancamento (GregorianCalendar dataFici, GregorianCalendar dataNai) {
		return dataFici.compareTo(dataNai) < 0;
	}
}
