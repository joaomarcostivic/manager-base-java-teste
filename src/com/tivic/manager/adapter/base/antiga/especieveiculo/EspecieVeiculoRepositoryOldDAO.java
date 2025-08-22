package com.tivic.manager.adapter.base.antiga.especieveiculo;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.fta.EspecieVeiculo;
import com.tivic.manager.fta.especieveiculo.EspecieVeiculoRepository;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class EspecieVeiculoRepositoryOldDAO implements EspecieVeiculoRepository {
	
	private IAdapterService<EspecieVeiculoOld, EspecieVeiculo> adapterService;
	
	public EspecieVeiculoRepositoryOldDAO() throws Exception {
		this.adapterService = new AdapterEspecieVeiculoService();
	}

	@Override
	public void insert(EspecieVeiculo especieVeiculo, CustomConnection customConnection) throws Exception {
		EspecieVeiculoOld especieVeiculoOld = this.adapterService.toBaseAntiga(especieVeiculo);
		int codRetorno = EspecieVeiculoOldDAO.insert(especieVeiculoOld, customConnection.getConnection());
		if (codRetorno <= 0)
			throw new BadRequestException("Ocorreu um erro ao cadastrar especieVeiculo");
		especieVeiculo.setCdEspecie(codRetorno);
	}

	@Override
	public void update(EspecieVeiculo especieVeiculo, CustomConnection customConnection) throws Exception {
		EspecieVeiculoOld especieVeiculoOld = this.adapterService.toBaseAntiga(especieVeiculo);
		int codRetorno = EspecieVeiculoOldDAO.update(especieVeiculoOld, customConnection.getConnection());
		if (codRetorno <= 0)
			throw new BadRequestException("Ocorreu um erro ao cadastrar especieVeiculo");
	}

	@Override
	public EspecieVeiculo get(int cdEspecieVeiculo) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.finishConnection();
			EspecieVeiculo especieVeiculo = get(cdEspecieVeiculo, customConnection);
			return especieVeiculo;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public EspecieVeiculo get(int cdEspecieVeiculo, CustomConnection customConnection) throws Exception {
		EspecieVeiculoOld especieVeiculoOld = EspecieVeiculoOldDAO.get(cdEspecieVeiculo, customConnection.getConnection());
		return this.adapterService.toBaseNova(especieVeiculoOld);
	}

	@Override
	public List<EspecieVeiculo> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<EspecieVeiculo> especieVeiculoList = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return especieVeiculoList;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<EspecieVeiculo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		List<EspecieVeiculo> especieVeiculos = new ArrayList<EspecieVeiculo>();
		ResultSetMapper<EspecieVeiculoOld> rsm = new ResultSetMapper<EspecieVeiculoOld>(EspecieVeiculoOldDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), EspecieVeiculoOld.class);
		List<EspecieVeiculoOld> EspecieVeiculoOldList = rsm.toList();
		for (EspecieVeiculoOld especieVeiculoOld : EspecieVeiculoOldList) {
			EspecieVeiculo especieVeiculo = this.adapterService.toBaseNova(especieVeiculoOld);
			especieVeiculos.add(especieVeiculo);
		}
		return especieVeiculos;
	}

	@Override
	public List<EspecieVeiculo> getAll() throws Exception {
    	return getAll(new CustomConnection());
	}

	@Override
	public List<EspecieVeiculo> getAll(CustomConnection customConnection) throws Exception {
		List<EspecieVeiculo> especieVeiculos = new ArrayList<EspecieVeiculo>();
		SearchCriterios searchCriterios = new SearchCriterios();
		Search<EspecieVeiculoOld> search = new SearchBuilder<EspecieVeiculoOld>("especieVeiculo")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		List<EspecieVeiculoOld> especieVeiculoOldList = search.getList(EspecieVeiculoOld.class);
		
		for(EspecieVeiculoOld especieVeiculoOld : especieVeiculoOldList) {
			EspecieVeiculo especieVeiculo = this.adapterService.toBaseNova(especieVeiculoOld);
			especieVeiculos.add(especieVeiculo);
		}
		
		return especieVeiculos;
	}
}
