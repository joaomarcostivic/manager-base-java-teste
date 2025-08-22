package com.tivic.manager.mob.lotes.repository.arquivo;

import java.util.List;

import com.tivic.manager.mob.lotes.model.arquivo.LoteImpressaoArquivo;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface ILoteImpressaoArquivoRepository {
	LoteImpressaoArquivo insert(LoteImpressaoArquivo loteImpressaoArquivo, CustomConnection customConnection) throws Exception;
	LoteImpressaoArquivo update(LoteImpressaoArquivo loteImpressaoArquivo, CustomConnection customConnection) throws Exception;
	LoteImpressaoArquivo delete(LoteImpressaoArquivo loteImpressaoArquivo, CustomConnection customConnection) throws Exception;
	LoteImpressaoArquivo get(int cdLoteImpressao, int cdArquivo) throws Exception;
	LoteImpressaoArquivo get(int cdLoteImpressao, int cdArquivo, CustomConnection customConnection) throws Exception;
	List<LoteImpressaoArquivo> find(SearchCriterios searchCriterios) throws Exception;
	List<LoteImpressaoArquivo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
