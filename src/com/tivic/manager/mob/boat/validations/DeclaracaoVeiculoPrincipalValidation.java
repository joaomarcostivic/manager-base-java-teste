package com.tivic.manager.mob.boat.validations;

import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.Boat;
import com.tivic.manager.mob.BoatVeiculo;
import com.tivic.manager.util.validator.Validator;

public class DeclaracaoVeiculoPrincipalValidation implements Validator<Boat> {
	
	@Override
	public void validate(Boat boat, CustomConnection customConnection) throws Exception {
		isObrigatorio(boat.getVeiculos());
		validarDadosProprietario(boat.getVeiculos().get(0));
		validarCaracteristicasVeiculoPrincipal(boat.getVeiculos().get(0));
	}
	
	private void isObrigatorio(List<BoatVeiculo> veiculos) {
		if(veiculos.size() == 0) {
			throw new BadRequestException("É obrigatório informar o veículo principal");
		}
	}
	
	private void validarDadosProprietario(BoatVeiculo veiculo) {
		if(veiculo == null) {
			throw new BadRequestException("Veículo principal não informado, por favor informe ao menos um veículo em sua declaração.");	
		}
		
		if(veiculo.getNmProprietario() == null || veiculo.getNmProprietario().trim().equals("")) {
			throw new BadRequestException("Por favor, revise o nome do proprietário do veículo principal.");			
		}
		
		if(veiculo.getNrCnhCondutor() == null || veiculo.getNrCnhCondutor().trim().equals("")) {
			throw new BadRequestException("Por favor, revise a CNH do condutor veículo principal.");			
		}
		
		if(veiculo.getDtPrimeiraHabilitacao() == null || veiculo.getDtPrimeiraHabilitacao().after(new GregorianCalendar())) {
			throw new BadRequestException("Por favor, revise a data da primeira habilitação do condutor.");			
		}
		
		if(veiculo.getDtVencimentoCnh() == null || veiculo.getDtVencimentoCnh().before(veiculo.getDtPrimeiraHabilitacao())) {
			throw new BadRequestException("Por favor, revise o vencimento da habilitação do condutor do veículo principal.");			
		}
	}
	
	private void validarCaracteristicasVeiculoPrincipal(BoatVeiculo veiculo) {
		if (veiculo.getCdMarca() == 0 || veiculo.getCdCategoria() == 0 || veiculo.getCdEspecie() == 0 || veiculo.getCdTipo() == 0) {
			throw new BadRequestException("Por favor, revise a marca, categoria, espécie e tipo do veículo principal.");
		}
	}
	

}
