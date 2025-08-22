package com.tivic.manager.mob.lotes.repository.impressao;

import java.util.List;

import com.tivic.manager.mob.lotes.model.impressao.LoteImpressaoAit;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface ILoteImpressaoAitRepository {
	public void insert(LoteImpressaoAit loteImpressaoAit, CustomConnection customConnection) throws Exception;
	public void update(LoteImpressaoAit loteImpressaoAit, CustomConnection customConnection) throws Exception;
	public LoteImpressaoAit delete(LoteImpressaoAit loteImpressaoAit, CustomConnection customConnection) throws Exception;
	public LoteImpressaoAit get(int cdLoteImpressao, int cdAit, CustomConnection customConnection) throws Exception;
	public List<LoteImpressaoAit> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public List<LoteImpressaoAit> findByCdLoteImpressao(int cdLoteImpressao) throws Exception;
}
