package com.tivic.manager.fta.especieveiculo;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.fta.EspecieVeiculo;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class EspecieVeiculoService implements IEspecieVeiculoService {

	private EspecieVeiculoRepository especieVeiculoRepository;
	
	public EspecieVeiculoService() throws Exception{
		this.especieVeiculoRepository = (EspecieVeiculoRepository) BeansFactory.get(EspecieVeiculoRepository.class);
	}
	
	@Override
	public EspecieVeiculo getByNome(String dsEspecie) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("ds_especie", dsEspecie, true);
		List<EspecieVeiculo> especiesVeiculo = this.especieVeiculoRepository.find(searchCriterios);
		if(especiesVeiculo.isEmpty())
			throw new Exception("Nenhuma especie de veículo encontrado");
		return especiesVeiculo.get(0);
	}
	
	@Override
	public EspecieVeiculo get(int cdEspecie) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			EspecieVeiculo especieVeiculo = get(cdEspecie, customConnection);
			customConnection.finishConnection();
			return especieVeiculo;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public EspecieVeiculo get(int cdEspecie, CustomConnection customConnection) throws Exception {
		EspecieVeiculo especieVeiculo = this.especieVeiculoRepository.get(cdEspecie, customConnection);
		
		if(especieVeiculo == null)
			throw new NoContentException("Nenhuma espécie veículo encontrada");
		
		return especieVeiculo;
	}

}
