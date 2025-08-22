package com.tivic.manager.fta.especieveiculo;

import java.util.List;

import com.tivic.manager.fta.EspecieVeiculo;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface EspecieVeiculoRepository {
	public void insert(EspecieVeiculo especieVeiculo, CustomConnection customConnection) throws Exception;
	public void update(EspecieVeiculo especieVeiculo, CustomConnection customConnection) throws Exception;
	public EspecieVeiculo get(int cdEspecie) throws Exception;
	public EspecieVeiculo get(int cdEspecie, CustomConnection customConnection) throws Exception;
	public List<EspecieVeiculo> getAll() throws Exception;
	public List<EspecieVeiculo> getAll(CustomConnection customConnection) throws Exception;
	public List<EspecieVeiculo> find(SearchCriterios searchCriterios) throws Exception;
	public List<EspecieVeiculo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
