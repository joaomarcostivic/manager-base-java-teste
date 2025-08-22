package com.tivic.manager.mob.lote.impressao;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.lote.impressao.loteimpressaosearch.LoteImpressaoSearch;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public interface ILoteImpressaoRepository {
	public LoteImpressao insert(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception;
	public LoteImpressao update(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception;
	public LoteImpressao delete(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception;
	public LoteImpressao get(int cdLoteImpressao, CustomConnection customConnection) throws Exception;
	public List<LoteImpressao> getAll(CustomConnection customConnection) throws Exception;
	public List<LoteImpressao> find(ArrayList<ItemComparator> criterios, CustomConnection customConnection) throws Exception;
	public Search<LoteNotificacaoDTO> findLotes(LoteImpressaoSearch loteImpressaoSearch) throws Exception;
	public Search<LoteNotificacaoDTO> findLotes(LoteImpressaoSearch loteImpressaoSearch, CustomConnection customConnection) throws Exception;
	public Search<LoteImpressaoAitDTO> getLote(LoteImpressaoSearch loteImpressaoSearch) throws Exception;
	public Search<LoteImpressaoAitDTO> getLote(LoteImpressaoSearch loteImpressaoSearchs, CustomConnection customConnection) throws Exception;
	public Search<LoteImpressaoAitDTO> findLoteAits(LoteImpressaoSearch loteImpressaoSearch) throws Exception;
	public Search<LoteImpressaoAitDTO> findLoteAits(LoteImpressaoSearch loteImpressaoSearch, CustomConnection customConnection) throws Exception;
	public List<LoteImpressao> findLoteImpressao(SearchCriterios searchCriterios) throws Exception;
	public List<LoteImpressao> findLoteImpressao(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
