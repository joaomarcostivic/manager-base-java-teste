package com.tivic.manager.fta.tipoveiculo;

import java.util.List;

import com.tivic.manager.fta.TipoVeiculo;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.search.SearchCriterios;

public class TipoVeiculoService implements ITipoVeiculoService {

	private TipoVeiculoRepository tipoVeiculoRepository;
	
	public TipoVeiculoService() throws Exception {
		this.tipoVeiculoRepository = (TipoVeiculoRepository) BeansFactory.get(TipoVeiculoRepository.class);
	}
	
	@Override
	public TipoVeiculo getByNmTipoVeiculo(String nmTipoVeiculo) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nm_tipo_veiculo", nmTipoVeiculo, true);
		List<TipoVeiculo> tiposVeiculo = this.tipoVeiculoRepository.find(searchCriterios);
		if(tiposVeiculo.isEmpty())
			throw new Exception("Nenhum tipo de veiculo encontrada");
		return tiposVeiculo.get(0);
	}

}
