package com.tivic.manager.fta.tipoveiculo;

import java.util.List;

import com.tivic.manager.fta.TipoVeiculo;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface TipoVeiculoRepository {
	public void insert(TipoVeiculo tipoVeiculo, CustomConnection customConnection) throws Exception;
	public void update(TipoVeiculo tipoVeiculo, CustomConnection customConnection) throws Exception;
	public TipoVeiculo get(int cdTipoVeiculo) throws Exception;
	public TipoVeiculo get(int cdTipoVeiculo, CustomConnection customConnection) throws Exception;
	public List<TipoVeiculo> find(SearchCriterios searchCriterios) throws Exception;
	public List<TipoVeiculo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
