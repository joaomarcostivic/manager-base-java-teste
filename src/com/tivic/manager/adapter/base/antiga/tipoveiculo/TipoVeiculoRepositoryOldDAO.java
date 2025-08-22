package com.tivic.manager.adapter.base.antiga.tipoveiculo;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.fta.TipoVeiculo;
import com.tivic.manager.fta.tipoveiculo.TipoVeiculoRepository;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class TipoVeiculoRepositoryOldDAO implements TipoVeiculoRepository {
	
	private IAdapterService<TipoVeiculoOld, TipoVeiculo> adapterService;
	
	public TipoVeiculoRepositoryOldDAO() throws Exception {
		this.adapterService = new AdapterTipoVeiculoService();
	}

	@Override
	public void insert(TipoVeiculo tipoVeiculo, CustomConnection customConnection) throws Exception {
		TipoVeiculoOld tipoVeiculoOld = this.adapterService.toBaseAntiga(tipoVeiculo);
		int codRetorno = TipoVeiculoOldDAO.insert(tipoVeiculoOld, customConnection.getConnection());
		if (codRetorno <= 0)
			throw new BadRequestException("Ocorreu um erro ao cadastrar a tipoVeiculo");
		tipoVeiculo.setCdTipoVeiculo(codRetorno);
	}

	@Override
	public void update(TipoVeiculo tipoVeiculo, CustomConnection customConnection) throws Exception {
		TipoVeiculoOld tipoVeiculoOld = this.adapterService.toBaseAntiga(tipoVeiculo);
		int codRetorno = TipoVeiculoOldDAO.update(tipoVeiculoOld, customConnection.getConnection());
		if (codRetorno <= 0)
			throw new BadRequestException("Ocorreu um erro ao cadastrar tipoVeiculo");
	}

	@Override
	public TipoVeiculo get(int cdTipoVeiculo) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.finishConnection();
			TipoVeiculo tipoVeiculo = get(cdTipoVeiculo, customConnection);
			return tipoVeiculo;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public TipoVeiculo get(int cdTipoVeiculo, CustomConnection customConnection) throws Exception {
		TipoVeiculoOld tipoVeiculoOld = TipoVeiculoOldDAO.get(cdTipoVeiculo, customConnection.getConnection());
		return this.adapterService.toBaseNova(tipoVeiculoOld);
	}

	@Override
	public List<TipoVeiculo> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<TipoVeiculo> tipoVeiculoList = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return tipoVeiculoList;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<TipoVeiculo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		List<TipoVeiculo> tipoVeiculoList = new ArrayList<TipoVeiculo>();
		ResultSetMapper<TipoVeiculoOld> rsm = new ResultSetMapper<TipoVeiculoOld>(TipoVeiculoOldDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), TipoVeiculoOld.class);
		List<TipoVeiculoOld> tipoVeiculoOldList = rsm.toList();
		for (TipoVeiculoOld tipoVeiculoOld : tipoVeiculoOldList) {
			TipoVeiculo tipoVeiculo = this.adapterService.toBaseNova(tipoVeiculoOld);
			tipoVeiculoList.add(tipoVeiculo);
		}
		return tipoVeiculoList;
	}
}
