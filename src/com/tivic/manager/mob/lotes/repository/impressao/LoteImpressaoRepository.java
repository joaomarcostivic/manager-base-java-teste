package com.tivic.manager.mob.lotes.repository.impressao;

import java.util.List;

import com.tivic.manager.mob.lotes.builders.impressao.LoteImpressaoSearch;
import com.tivic.manager.mob.lotes.dto.impressao.AitDTO;
import com.tivic.manager.mob.lotes.dto.impressao.LoteImpressaoDTO;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface LoteImpressaoRepository {
	public LoteImpressao insert(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception;
	public LoteImpressao update(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception;
	public void delete(int cdLoteImpressao, CustomConnection customConnection) throws Exception;
	public LoteImpressao get(int cdLoteImpressao, CustomConnection customConnection) throws Exception;
	public List<LoteImpressao> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public Search<AitDTO> buscarAitsParaLoteImpressao(SearchCriterios searchCriterios, int tpNotificacao) throws Exception;
	public Search<LoteImpressaoDTO> findLotes(LoteImpressaoSearch loteImpressaoSearch) throws Exception;
	public Search<LoteImpressaoDTO> findLotes(LoteImpressaoSearch loteImpressaoSearch, CustomConnection customConnection) throws Exception;
	public Search<LoteImpressaoDTO> findLoteAits(LoteImpressaoSearch loteImpressaoSearch) throws Exception;
	public Search<LoteImpressaoDTO> findLoteAits(LoteImpressaoSearch loteImpressaoSearch, CustomConnection customConnection) throws Exception;
	public Search<LoteImpressaoDTO> getLote(LoteImpressaoSearch loteImpressaoSearch, CustomConnection customConnection) throws Exception;
	public Search<LoteImpressaoDTO> getLote(LoteImpressaoSearch loteImpressaoSearch) throws Exception;
}
