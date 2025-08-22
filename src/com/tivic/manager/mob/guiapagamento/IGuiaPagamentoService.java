package com.tivic.manager.mob.guiapagamento;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface IGuiaPagamentoService {

	public PagedResponse<Ait> findGuiaPagamento(SearchCriterios searchCriterios) throws Exception;
	public Search<Ait> findGuiaPagamento(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public byte[] gerarNpFicticia() throws Exception;
	
}
