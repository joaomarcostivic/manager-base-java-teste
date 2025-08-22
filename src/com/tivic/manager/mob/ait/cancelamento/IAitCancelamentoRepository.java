package com.tivic.manager.mob.ait.cancelamento;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface IAitCancelamentoRepository {

	public AitCancelamento insert(AitCancelamento aitCancelamento, CustomConnection connection) throws BadRequestException, Exception;
	public void update(AitCancelamento aitCancelamento, CustomConnection customConnection) throws Exception;
	public void delete(int cdArquivo, CustomConnection connection) throws Exception;
	public AitCancelamento get(int cdArquivo) throws Exception;
	public AitCancelamento get(int cdArquivo, CustomConnection customConnection) throws Exception;
	public List<AitCancelamento> find(SearchCriterios searchCriterios) throws Exception;
	public List<AitCancelamento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public Search<AitArquivoDTO> findArquivosCancelamento(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public Search<AitArquivoDTO> findArquivosCancelamento(SearchCriterios searchCriterios) throws Exception;

}
