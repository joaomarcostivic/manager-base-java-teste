package com.tivic.manager.ptc.protocolos.validators;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;

public class DtProtocoloValidator implements IValidator<DadosProtocoloDTO>{

	@Override
	public void validate(DadosProtocoloDTO obj, CustomConnection connection) throws ValidationException, Exception {
		GregorianCalendar dataAtual = new GregorianCalendar();
		dataAtual.set(Calendar.HOUR, 23);
		dataAtual.set(Calendar.MINUTE, 59);
		dataAtual.set(Calendar.SECOND, 59);
		if(obj.getDtProtocolo().after(dataAtual)) {
			throw new ValidationException("A data do protocolo não deve ser posterior a data atual.");
		}
		
	}

}
