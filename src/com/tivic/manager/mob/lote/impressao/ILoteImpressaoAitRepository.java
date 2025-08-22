package com.tivic.manager.mob.lote.impressao;

import java.util.List;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.sol.search.SearchCriterios;

public interface ILoteImpressaoAitRepository {
	public LoteImpressaoAit insert(LoteImpressaoAit objeto, CustomConnection customConnection) throws Exception;
	public LoteImpressaoAit update(LoteImpressaoAit objeto, CustomConnection customConnection) throws Exception;
	public LoteImpressaoAit delete(LoteImpressaoAit objeto, CustomConnection customConnection) throws Exception;
	public LoteImpressaoAit get(int cdLoteImpressao, int cdAit, CustomConnection customConnection) throws Exception;
	public List<LoteImpressaoAit> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public LoteImpressaoAitDTO getDTO(int cdAit, int tpDocumento, CustomConnection customConnection) throws Exception;
}
