package com.tivic.manager.mob.boat.validations;

import javax.ws.rs.BadRequestException;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.Boat;
import com.tivic.manager.mob.BoatDeclarante;
import com.tivic.manager.mob.Declarante;
import com.tivic.manager.mob.DeclaranteTipoRelacaoEnum;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.validator.Validator;

public class DeclaranteValidation implements Validator<Boat> {
	
	@Override
	public void validate(Boat boat, CustomConnection customConnection) throws Exception {
		validarDeclarante(boat);
	}
	
	private void validarDeclarante(Boat boat) {
		Declarante declarante = boat.getDeclarante();
		BoatDeclarante boatDeclarante = boat.getBoatDeclarante();

		if (declarante.getNmDeclarante() == null || declarante.getNmDeclarante().trim().equals("")) {
			throw new BadRequestException("O nome do declarante é obrigatório.");	
		}
		
		if (declarante.getNrCpf() == null || declarante.getNrCpf().trim().equals("") || !Util.isCpfValido(boat.getDeclarante().getNrCpf())) {
			throw new BadRequestException("O CPF do declarante informado não é válido.");	
		}
		
		if (declarante.getNrTelefone() == null || declarante.getNrTelefone().trim().equals("")) {
			throw new BadRequestException("O número de telefone do declarante é obrigatório.");	
		}
		
		if (DeclaranteTipoRelacaoEnum.valueOf(boatDeclarante.getTpRelacao()) == null) {
			throw new BadRequestException("O tipo de declarante não é válido.");
		}
	}
	

}
