package com.tivic.manager.mob.lotes.repository.dividaativa;

import java.util.List;

import com.tivic.manager.mob.lotes.dto.dividaativa.DividaAtivaDTO;
import com.tivic.manager.mob.lotes.dto.dividaativa.LoteDividaAtivaDTO;
import com.tivic.manager.mob.lotes.model.dividaativa.LoteDividaAtiva;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface LoteDividaAtivaRepository {
	public void insert(LoteDividaAtiva cdLoteDividaAtiva, CustomConnection customConnection) throws Exception;
	public void update(LoteDividaAtiva cdLoteDividaAtiva, CustomConnection customConnection) throws Exception;
	public LoteDividaAtiva get(int cdLoteDividaAtiva, CustomConnection customConnection) throws Exception;
	public List<LoteDividaAtiva> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public LoteDividaAtiva getLoteGerado(String idAit, CustomConnection customConnection) throws Exception;
	public List<DividaAtivaDTO> getInfoCSVByCdLote(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public PagedResponse<LoteDividaAtivaDTO> findLotesDividaAtiva(SearchCriterios searchCriterios) throws Exception;
	public PagedResponse<LoteDividaAtivaDTO> findLotesDividaAtiva(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	PagedResponse<DividaAtivaDTO> findCandidatos(SearchCriterios searchCriterios) throws Exception;
	PagedResponse<DividaAtivaDTO> findCandidatos(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
