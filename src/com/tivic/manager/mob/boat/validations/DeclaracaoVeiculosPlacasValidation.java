package com.tivic.manager.mob.boat.validations;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.Boat;
import com.tivic.manager.mob.BoatVeiculo;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.validator.Validator;

public class DeclaracaoVeiculosPlacasValidation implements Validator<Boat> {
	
	@Override
	public void validate(Boat boat, CustomConnection customConnection) throws Exception {
		validarPlacas(boat.getVeiculos());
	}
	
	private void validarPlacas(List<BoatVeiculo> veiculos) throws Exception {
		for (int i = 0; i < veiculos.size(); i++) {	
			BoatVeiculo veiculo = veiculos.get(i);
			
			if (!Util.validarPlaca(Util.limparFormatos(veiculo.getNrPlaca()))) {
				throw new BadRequestException("Por favor, revise a placa do "+(i+1)+"ª veículo informado.");
			}
		}
	}
	

}
