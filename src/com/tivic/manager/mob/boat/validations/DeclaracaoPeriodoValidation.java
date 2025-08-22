package com.tivic.manager.mob.boat.validations;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.ws.rs.BadRequestException;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.Boat;
import com.tivic.manager.util.validator.Validator;

public class DeclaracaoPeriodoValidation implements Validator<Boat> {
	
	@Override
	public void validate(Boat boat, CustomConnection customConnection) throws Exception {
		validatePeridoMaximo(boat);
		validatePeridoMinimo(boat);
	}

	public void validatePeridoMaximo(Boat boat) {
		GregorianCalendar dtOcorrencia = boat.getDtOcorrencia();
		GregorianCalendar dtPeriodoMaximo = new GregorianCalendar();
		dtPeriodoMaximo.add(Calendar.MONTH, -6);

		if (dtOcorrencia == null) {
			throw new BadRequestException("Data da ocorrência não informada.");
		}

		if (dtOcorrencia != null && dtOcorrencia.before(dtPeriodoMaximo)) {
			throw new BadRequestException("Período máximo de declaração é de 6 meses a partir da data da ocorrência.");
		}
	}

	public void validatePeridoMinimo(Boat boat) {
		GregorianCalendar dtOcorrencia = boat.getDtOcorrencia();
		GregorianCalendar dtPeriodoMinimo = new GregorianCalendar();

		if (!dtOcorrencia.before(dtPeriodoMinimo)) {
			throw new BadRequestException("Período mínim de declaração é do momento atual.");
		}
	}

}
