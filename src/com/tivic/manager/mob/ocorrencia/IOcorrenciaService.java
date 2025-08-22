package com.tivic.manager.mob.ocorrencia;

import java.util.List;

import com.tivic.manager.mob.Ocorrencia;
import com.tivic.sol.search.SearchCriterios;

public interface IOcorrenciaService {
	public List<Ocorrencia> find(SearchCriterios searchCriterios) throws Exception;
}
