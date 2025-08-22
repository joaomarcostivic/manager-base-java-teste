package com.tivic.manager.mob.boat.validations;

import java.util.ArrayList;

import javax.ws.rs.BadRequestException;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.Boat;
import com.tivic.manager.mob.BoatVeiculo;
import com.tivic.manager.util.validator.Validator;

public class DeclaracaoVeiculoValidation implements Validator<Boat> {
	
	@Override
	public void validate(Boat boat, CustomConnection customConnection) throws Exception {
		validarQuantidadeVeiculos(boat);
	}
	
	private void validarQuantidadeVeiculos(Boat boat) {
		ArrayList<BoatVeiculo> veiculos = boat.getVeiculos();
		
		if (veiculos != null && veiculos.size() > 5) {
			throw new BadRequestException("Uma declaração não pode conter mais de 5 veículos associados.");	
		}		
	}
	

}
