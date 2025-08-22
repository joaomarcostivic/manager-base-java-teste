package com.tivic.manager.mob.boat.validations;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.Boat;
import com.tivic.manager.mob.BoatVeiculo;
import com.tivic.manager.util.validator.Validator;

public class DeclaracaoVeiculosImagensValidation implements Validator<Boat> {
	
	@Override
	public void validate(Boat boat, CustomConnection customConnection) throws Exception {
		validarImagensVeiculo(boat.getVeiculos());
	}
	
	private void validarImagensVeiculo(List<BoatVeiculo> veiculos) throws Exception {
		
		BoatVeiculo veiculoPrincipal = veiculos.get(0);
		
		if(veiculoPrincipal != null && 
		   veiculoPrincipal.getImagens() != null && 
		   veiculoPrincipal.getImagens().size() < 4) {
			throw new BadRequestException("Por favor, revise as imagens do veículo principal.");
		}
		
	}
	

}
