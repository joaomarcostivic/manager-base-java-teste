package com.tivic.manager.mob.ait.aitArquivo;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface IAitArquivoRepository {

	public AitArquivo insert(AitArquivo aitArquivo, CustomConnection connection) throws BadRequestException, Exception;
	public void update(AitArquivo aitArquivo, CustomConnection customConnection) throws Exception;
	public void delete(int cdArquivo, CustomConnection connection) throws Exception;
	public AitArquivo get(int cdArquivo) throws Exception;
	public AitArquivo get(int cdArquivo, CustomConnection customConnection) throws Exception;
	public List<AitArquivo> find(SearchCriterios searchCriterios) throws Exception;
	public List<AitArquivo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public Search<TipoArquivoDTO> findArquivosAit(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public Search<TipoArquivoDTO> findArquivosAit(SearchCriterios searchCriterios) throws Exception;
}
