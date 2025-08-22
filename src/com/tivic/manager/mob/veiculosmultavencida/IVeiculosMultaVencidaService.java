package com.tivic.manager.mob.veiculosmultavencida;

import java.util.List;

import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.search.SearchCriterios;

public interface IVeiculosMultaVencidaService {
	public PagedResponse<VeiculosMultaVencidaDTO> find(SearchCriterios searchCriterios) throws Exception;
	public PagedResponse<AitVeiculoMultaVencidaDTO> findAits(SearchCriterios searchCriterios) throws Exception;
	public byte[] imprimir(List<VeiculosMultaVencidaDTO> itemsList) throws Exception;
}
