package com.tivic.manager.mob.ocorrencia;

import java.util.List;

import com.tivic.manager.mob.Ocorrencia;
import javax.ws.rs.core.NoContentException;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class OcorrenciaService implements IOcorrenciaService{

	@Override
	public List<Ocorrencia> find(SearchCriterios searchCriterios) throws Exception {
		List<Ocorrencia> ocorrencias = searchOcorrencia(searchCriterios).getList(Ocorrencia.class);
		
		if(ocorrencias.isEmpty()) {
			throw new NoContentException("Nenhum Registro");
		}
		
		return ocorrencias;
	}
	
	private Search<Ocorrencia> searchOcorrencia(SearchCriterios searchCriterios) throws Exception {
		Search<Ocorrencia> search = new SearchBuilder<Ocorrencia>("mob_ocorrencia").searchCriterios(searchCriterios).build();
		return search;
	}

}
